package com.idcq.idianmgr.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dao.log.ILogDao;
import com.idcq.appserver.dto.log.ShopOperationLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.idianmgr.dao.shop.IShopCashierDao;

import javax.servlet.http.HttpServletRequest;

@Service
public class MgrCommonServiceImpl implements IMgrCommonService {

	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
    private IShopCashierDao shopCashierDao;
	@Autowired
	private ILogDao logDao;
	
	public Map commonUploadFile(Long userId, String mimeType, MultipartFile myfile, Long bizId, String bizType) throws Exception {
	    if(userId != null) {
	        //验证用户的存在性
	        UserDto userDB = this.userDao.getUserById(userId);
	        if(null == userDB)
	        {
	            Map param = new HashMap();
	            param.put("cashierId", userId);
	            int count = shopCashierDao.findShopCashierExists(param);
	            if (count == 0)
	            {
	                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "操作用户不存在");
	            }
	        }
	    }
		
		//上传文件
		String fileUrl = FdfsUtil.uploadFile(mimeType, myfile);
		Attachment attachment = new Attachment();
		attachment.setFileType(mimeType);
		attachment.setFileSize(Double.valueOf(myfile.getSize()/1024));
		attachment.setFileUrl(fileUrl);
		attachment.setFileName(myfile.getOriginalFilename());
		attachment.setCreateTime(new Date());
		attachment.setUploadUserId(userId);
		attachment.setUploadUserType(1); // 用户上传
		attachment.setBizId(bizId);
		attachment.setBizType(bizType);
		attachmentDao.saveAttachment(attachment);
		//封装返回对象
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("attachementId", attachment.getAttachmentId());
		map.put("fileUrl", FdfsUtil.getFileProxyPath(fileUrl));
		return map;
	}

	/**
	 * 1.是雇员优先按雇员登录
	 * 2.不是雇员，是会员才按会员登录
	 */
	public Map shopLogin(String mobile, String password,Integer roleMode) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		if(roleMode ==null){
			//优先按雇员登录
			if(!isEmployeeLogin(param, mobile, password,roleMode)){
				// 会员登录
				isUserLogin(param, mobile, password);
			}
		}else if(roleMode == 1){
			isEmployeeLogin(param, mobile, password,roleMode);
		}else{
			// 会员登录
			isUserLogin(param, mobile, password);
		}
		return param;
		
	}

	/**
	 * 会员登录
	 * @param param
	 * @param mobile
	 * @param password
	 */
	private boolean isUserLogin(Map<String, Object> param, String mobile,
			String password) throws Exception {
		boolean loginSuccess = false;
		UserDto userDB = this.userDao.getUserByMobileFromDB(mobile);
		if (userDB == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					"用户不存在");
		}
		// 校验用户状态1代表正常状态
		Integer status = userDB.getStatus();
		CommonValidUtil.validObjectNull(status,
				CodeConst.CODE_USER_STATUS_ERROR,
				CodeConst.MSG_USER_STATUS_UNUSUAL);
		if (CommonConst.USER_FREEZE_STATUS == status) {
			throw new ValidateException(CodeConst.CODE_USER_STATUS_FREEZE,
					CodeConst.MSG_USER_STATUS_FREEZE_FAIL);
		} else if (CommonConst.USER_LOGOUT_STATUS == status) {
			throw new ValidateException(CodeConst.CODE_USER_STATUS_LOGOUT,
					CodeConst.MSG_USER_STATUS_LOGOUT_FAIL);
		}
		// 校验密码
		if (!StringUtils.equals(password, userDB.getPassword())) {
			throw new ValidateException(CodeConst.CODE_PWD_ERROR,
					CodeConst.MSG_PWD_ERROR);
		}
		long operatorId = userDB.getUserId();
		int operatorType = CommonConst.SHOP_OWNER_TYPE;

		// 查商铺
		List<Map> shopList = shopDao.queryNormalShopListBy(operatorId,
				operatorType);
		// 店铺是空就返回空，让客户端引导开店
		if (!CollectionUtils.isEmpty(shopList)) {
			for (Map shopMap : shopList) {
				shopMap.put("shopLogoUrl", FdfsUtil.getFileProxyPath(String
						.valueOf(shopMap.get("shopLogoUrl"))));
			}
		}
		param.put("operatorId", operatorId);
		param.put("operatorType", operatorType);
		param.put("idCard", userDB.getIdentityCardNo());
		param.put("shopList", shopList);
		loginSuccess = true;

		return loginSuccess;

	}

	/**
	 * 雇员登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	private boolean isEmployeeLogin(Map<String, Object> param, String mobile, String password,Integer roleMode) throws Exception {
//		Map map = shopDao.queryShopEmployee(mobile, null);
		
		Map<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("shopId",  null);
		requestMap.put("mobile",  mobile);
		requestMap.put("status", String.valueOf(CommonConst.SHOP_EMPLOYEE_STATUS_NORMAL));
		List<Map<String, String>> mapList =shopDao.queryShopEmployeeByMap(requestMap);
		if(mapList==null || mapList.size()==0){
			if(roleMode !=null && roleMode == 1){//雇员登录，安卓V3.5以及以后的版本
				throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_NOT_EXIST);
			}else{
				return false;
			}
		}
		boolean isPasswordTrue = false;
		Map<String,String> returnShopEmployeeMap = new HashMap<String, String>();
		for (Map<String, String> map : mapList) {
		    String isCheck = map.get("isCheck") == null ? "0" : String.valueOf(map.get("isCheck"));
		    if("1".equals(isCheck)) {
                UserDto userDB = userDao.getUserByMobile(mobile);
                // 校验密码
                if (userDB != null && StringUtils.equals(password, userDB.getPassword())) {
                    returnShopEmployeeMap = map;
                    isPasswordTrue = true;
                    break;
                }
                
            } else if (StringUtils.equals(password, String.valueOf(map.get("password")))) {
				returnShopEmployeeMap = map;
				isPasswordTrue = true;
				break;
			}
		}
		if(!isPasswordTrue){
			throw new ValidateException(CodeConst.CODE_PWD_ERROR,CodeConst.MSG_PWD_ERROR);
		}
		
		//是否验证默认无验证
//		int isCheck = (Integer) map.get("isCheck")==null ? 
//					CommonConst.SHOP_EMPLOYEE_NOT_IS_CHECK : (Integer)map.get("isCheck");
//		已验证过手机号码直接走走用户登录流程，否则走老雇员登录流程
//		if(roleMode ==null || roleMode!=1){
//			return false;
//		}
//		if(CommonConst.SHOP_EMPLOYEE_IS_CHECK==isCheck){
//			if(roleMode!=1){
//				return false;
//			}
//		}
	
//		Integer status = Integer.valueOf(String.valueOf(map.get("status")));
//		if (null == status || CommonConst.SHOP_EMPLOYEE_STATUS_NORMAL != status) {
//			throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
//		}
		//校验密码
//		if (!StringUtils.equals(password, String.valueOf(map.get("password")))) {
//			throw new ValidateException(CodeConst.CODE_PWD_ERROR,CodeConst.MSG_PWD_ERROR);
//		}
		
		long operatorId = Long.valueOf(String.valueOf(returnShopEmployeeMap.get("operatorId")));
		int operatorType = CommonConst.SHOP_EMPLOYEE_TYPE;
		long bizId = Long.valueOf(String.valueOf(returnShopEmployeeMap.get("shopId")));
		
		// 查商铺
		List<Map> shopList = shopDao.queryNormalShopListBy(bizId, operatorType);
		if (CollectionUtils.isEmpty(shopList)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺不存在或者店铺状态异常");
		}
		for (Map shopMap : shopList) {
			shopMap.put("shopLogoUrl", FdfsUtil.getFileProxyPath(String.valueOf(shopMap.get("shopLogoUrl"))));
		}
		param.put("operatorId", operatorId);
		param.put("operatorType", operatorType);
		param.put("shopList", shopList);
		return true;
		
		
	}

	@Override
	public Map shopManageLogin(String mobile, String password,Integer roleMode) throws Exception {
		Map<String,Object> resutlMap = new HashMap<String, Object>();
		List<Map> listMap = new ArrayList<Map>();
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mobile", mobile);
		paramMap.put("status", String.valueOf(CommonConst.SHOP_EMPLOYEE_STATUS_NORMAL));
		
		List<Map<String,Object>> employeesMap = null;
		List<Map<String,Object>> bossesMap = null;
		if(roleMode != null){
			if(roleMode == 1){
				employeesMap = shopDao.getAllShopEmployeeByMap(paramMap);
			}
			if(roleMode == 0){
				bossesMap = shopDao.getAllShopBossesByMap(paramMap);
			}
		}else{
			//第一步、首先根据用户名所有的雇员，包括已经审核通过的雇员，已审核通过的雇员则查询user表中的信息，且该雇员的is_Check=1
			employeesMap = shopDao.getAllShopEmployeeByMap(paramMap);
			//第二步（与第一步没有先后关系）：根据用户名查询出所有能和shop表中的principleId关联的user,此时登录的为老板
			bossesMap = shopDao.getAllShopBossesByMap(paramMap);
		}
		//第三步：如果前两步没有查询到数据，则说明该帐号不存在，如果查询到了，在去匹配密码，如果密码没有匹配到，在提示密码错误
		//如果用户名和密码都存在，则将符合的雇员和老板对应的shopId都列举出来（老板，老雇员（is_check=1），新雇员（is_check=1）分开来列举）
		if((employeesMap==null || employeesMap.size()==0) && (bossesMap == null || bossesMap.size() == 0)){
			Map<String,Object> requestMap = new HashMap<String, Object>();
			requestMap.put("mobile", mobile);
			requestMap.put("isMember", 1);
			UserDto userDto = userDao.getUserByMap(requestMap);
			if(userDto == null){
				throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_NOT_EXIST);
			}else{
				if(userDto.getPassword().equals(password)){
					if(userDto.getIsMember()==1){
						Map<String,Object> shopMap = new HashMap<String, Object>();
						shopMap.put("operatorId", userDto.getUserId());
						shopMap.put("operatorType", CommonConst.SHOP_OWNER_TYPE);
						shopMap.put("userName",  userDto.getUserName());
						shopMap.put("idCard",  null);
						shopMap.put("isCheck",  null);
						shopMap.put("shop", null);
						resutlMap.put("lst", listMap);
						listMap.add(shopMap);
						return resutlMap;
					}
					
				}else{
					throw new ValidateException(CodeConst.CODE_PWD_ERROR,CodeConst.MSG_PWD_ERROR);
				}
				
			}
			
		}
		boolean isPwdNotRight = true;
		if(employeesMap != null && employeesMap.size()>0){
			for (Map<String, Object> map : employeesMap) {
				Map<String,Object> shopMap = new HashMap<String, Object>();
				long bizId = Long.valueOf(String.valueOf(map.get("shopId")));
				if (StringUtils.equals(password, String.valueOf(map.get("password")))) {
				    isPwdNotRight = false;
					List<Map> shopList = shopDao.queryNormalShopListBy(bizId, 2);
					if(shopList != null && shopList.size()>0){
						for (Map map2 : shopList) {
							map2.put("shopLogoUrl", map2.get("shopLogoUrl")==null?"":FdfsUtil.getFileProxyPath(String.valueOf(map2.get("shopLogoUrl"))));
						}
					}
					if(shopList!=null && shopList.size()>0){
						shopMap.put("operatorId", map.get("operatorId"));
						shopMap.put("operatorType", map.get("operatorType"));
						shopMap.put("userName",  map.get("userName"));
						if(shopList.get(0)!=null){
							shopMap.put("idCard",  shopList.get(0).get("idCard"));
						}else{
							shopMap.put("idCard",  null);
						}
						shopMap.put("isCheck",  map.get("isCheck"));
						shopMap.put("shop",  shopList.get(0));
						listMap.add(shopMap);
					}
				}
			}
		}
		
		if(bossesMap != null && bossesMap.size()>0){
			for (Map<String, Object> map : bossesMap) {
				Map<String,Object> shopMap = new HashMap<String, Object>();
				long bizId = Long.valueOf(String.valueOf(map.get("shopId")));
				if (StringUtils.equals(password, String.valueOf(map.get("password")))) {
				    isPwdNotRight = false;
					List<Map> shopList = shopDao.queryNormalShopListBy(bizId, 2);
					if(shopList != null && shopList.size()>0){
						for (Map map2 : shopList) {
							map2.put("shopLogoUrl", map2.get("shopLogoUrl")==null?"":FdfsUtil.getFileProxyPath(String.valueOf(map2.get("shopLogoUrl"))));
						}
					}
					if(shopList!=null && shopList.size()>0){
						shopMap.put("operatorId", map.get("operatorId"));
						shopMap.put("operatorType", map.get("operatorType"));
						shopMap.put("userName",  map.get("userName"));
						if(shopList.get(0)!=null){
							shopMap.put("idCard",  shopList.get(0).get("idCard"));
						}else{
							shopMap.put("idCard",  null);
						}
						shopMap.put("isCheck",  map.get("isCheck"));
						shopMap.put("shop",  shopList.get(0));
						listMap.add(shopMap);
					}
				}
			}
		}
		
		if(isPwdNotRight){
			throw new ValidateException(CodeConst.CODE_PWD_ERROR,CodeConst.MSG_PWD_ERROR);
		}
		//第四步：通过第三步的shop查询出对应的shop信息，然后将老板、老雇员以及新雇员的信息合并到一个添加到list中返回给用户
		resutlMap.put("lst", listMap);
		return resutlMap;
	}

	@Override
	public void upgradeEmployee(Long shopId,Long employeeId, Long userId, String mobile,
			String password) throws Exception {
		Map<String,String> param = new HashMap<String, String>();
		UserDto userDto = null;
		param.put("shopId", shopId+"");
		int count = shopDao.getShopCountByMap(param);
		if(count==0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		}
		count = shopDao.queryShopEmplExists(shopId, employeeId);
		if(count==0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "雇员不存在");
		} 
		if(userId != null){
			userDto = userDao.getDBUserById(userId);
			CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_USER);
		}
		if(mobile != null && password!= null){
			Map<String,Object> requestMap = new HashMap<String, Object>();
			requestMap.put("mobile", mobile);
			requestMap.put("isMember", CommonConst.USER_IS_MEMBER);
			userDto = userDao.getUserByMap(requestMap);
			CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_USER);
			if(userDto != null){
				if(!userDto.getPassword().equals(password)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_PASSWROD_ERROR);
				}
			}
		}
		//更新雇员表信息
		if(userDto !=null){
			param.put("employeeId", employeeId+"");
			param.put("status", CommonConst.SHOP_EMPLOYEE_STATUS_NORMAL+"");
			List<ShopEmployeeDto> shopEmployeeDtoList = shopDao.queryEmployeeListByMap(param);
			if(shopEmployeeDtoList!=null && shopEmployeeDtoList.size()>0){
				ShopEmployeeDto shopEmployeeDto = shopEmployeeDtoList.get(0);
				shopEmployeeDto.setPassword(userDto.getPassword());
				shopEmployeeDto.setIsCheck(1);
				shopEmployeeDto.setUserId(userDto.getUserId());
				shopEmployeeDto.setMobile(userDto.getMobile());
				//将姓名改成手机好  20160803 文震宇
				shopEmployeeDto.setUserName(userDto.getMobile());
				if(shopEmployeeDto != null){
					shopDao.updateEmployeeByDto(shopEmployeeDto);
				}
			}
		}
	}

	@Override
	public Map<String, Object> saveLoginInfo(Map<String, Object> infoParams) {
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("code", 20000);
		rs.put("code", "保存日志失败");
		ShopOperationLog shopOperationLog = new ShopOperationLog();
		shopOperationLog.setShopId((Long)infoParams.get("shopId"));
		Integer infoType = (Integer)infoParams.get("infoType");
		shopOperationLog.setLogType(infoType);
		String act = 0 == infoType ? "退出" : "登录";

		Integer clientType = (Integer)infoParams.get("clientType");
		shopOperationLog.setClientSystemTypel(clientType);
		String clientDesc = 1 == clientType ? "收银机" : 2 == clientType ? "一点管家" : 6 == clientType ? "商铺后台" : "盒子";

		shopOperationLog.setOperateDesc(clientDesc + act);
		Object operateUserIdObj =  infoParams.get("operateUserId");
		Long operateUserId = operateUserIdObj == null ? null : (Long)operateUserIdObj;
		shopOperationLog.setOperateUserId(operateUserId);
		Object userTypeIdObj =  infoParams.get("userTypeId");
		Integer userTypeId = userTypeIdObj == null ? null : (Integer)userTypeIdObj;
		shopOperationLog.setUserTypeId(userTypeId);
		shopOperationLog.setCreateTime(new Date());
		shopOperationLog.setMobile((String)infoParams.get("mobile"));
		logDao.addShopOperatorLog(shopOperationLog);
		rs.put("code", CodeConst.CODE_SUCCEED);
		rs.put("msg", "通知成功");
		return rs;
	}
}
