package com.idcq.idianmgr.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.SessionUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.idianmgr.service.common.IMgrCommonService;

/**
 * 一点管家公共类接口
 * @author shengzhipeng
 * @date:2015年7月30日 下午2:09:30
 */
@Controller
public class MgrCommonController {
	private final Logger logger = Logger.getLogger(MgrCommonController.class);
	
	@Autowired
	private IMgrCommonService mgrCommonService;

	/**
	 * $1dcp_Home/interface/user/commonUploadFile
	 * 上传文件接口
	 * @Function: com.idcq.idianmgr.controller.common.CommonController.commonUploadFile
	 * @Description: 客户端调用该接口可以将文件上传到文件服务器
	 *
	 * @param myfile
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:szp
	 * @date:2015年7月30日 下午1:47:51
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日       shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value="/user/commonUploadFile", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object commonUploadFile(@RequestParam("file") MultipartFile myfile, HttpServletRequest request) {
		logger.info("上传文件-start");
		try{
			
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String mimeType = RequestUtils.getQueryParam(request, "mimeType");
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validStrNull(mimeType, CodeConst.CODE_PARAMETER_NOT_NULL, "mimeType" + CodeConst.MSG_REQUIRED_NOT_NULL);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			if(myfile.isEmpty()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件上传失败");
			}
			logger.info("文件大小: " + myfile.getSize() + " 文件类型: "  + myfile.getContentType() + " 文件名称: " + myfile.getName());
			
			if(myfile.getSize() > 10485760) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件大小超过10M不允许上传");
			}
			Map<String, Object> map = mgrCommonService.commonUploadFile(userId, mimeType, myfile, null, null);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UPLOADFILE, map);
		} catch (ServiceException e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APISystemException("上传失败-系统异常", e);
		}
	}
	
	@RequestMapping(value={"/user/commonBatchUploadFile", "/token/common/commonBatchUploadFile", "/session/common/commonBatchUploadFile", 
	        "/service/common/commonBatchUploadFile"}, method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object commonBatchUploadFile(MultipartHttpServletRequest request) {
		logger.info("批量上传文件-start");
		try{
			
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String mimeType = RequestUtils.getQueryParam(request, "mimeType");
			String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
			String bizType = RequestUtils.getQueryParam(request, "bizType");
			CommonValidUtil.validStrNull(mimeType, CodeConst.CODE_PARAMETER_NOT_NULL, "mimeType" + CodeConst.MSG_REQUIRED_NOT_NULL);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");
			List<MultipartFile> files = request.getFiles("file");
			List<Map> list = new ArrayList<Map>();
			if(files != null && files.size() > 0){
				Map<String, Object> map = null;
				for(MultipartFile myfile : files){
					if(myfile.isEmpty()) {
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件上传失败");
					}
					if(myfile.getSize() > 10485760) {
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件大小超过10M不允许上传");
					}
					logger.info("文件大小: " + myfile.getSize() + " 文件类型: "  + myfile.getContentType() + " 文件名称: " + myfile.getName());
					map = mgrCommonService.commonUploadFile(userId, mimeType, myfile, bizId, bizType);
					list.add(map);
				}
			}else{
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件上传失败");
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UPLOADFILE, list);
		} catch (ServiceException e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APISystemException("上传失败-系统异常", e);
		}
	}
	
	/**
	 * 一点管家登录接口
	 * $1dcp_Home/interface/shop/AuthentLogin
	 * @Function: com.idcq.idianmgr.controller.common.MgrCommonController.shopLogin
	 * @Description: 登录成功后需要返回cookies信息给客户端，后面的请求进行cookies认证
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月4日 下午3:38:50
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月4日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value="/shop/shopLogin", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object shopLogin(HttpServletRequest request) {
		logger.info("一点管家登录接口-start");
		try{
			
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String password = RequestUtils.getQueryParam(request, "password");
			String roleModeStr = RequestUtils.getQueryParam(request, "roleMode");
			// 校验是否为空
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MOBILE);
			CommonValidUtil.validStrNull(password,
					CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
			Integer roleMode = null;
			if(roleModeStr != null ){
				CommonValidUtil.validStrIntFmt(roleModeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ROLEMODE);
				roleMode = Integer.valueOf(roleModeStr);
			}
			Map map = mgrCommonService.shopLogin(mobile, password,roleMode);
			if (null != map) {
				//创建会话id
				String sessionId = SessionUtil.dealSession(request);
				logger.info("一点管家登录生成的sessionId:" + sessionId);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_LOGIN, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("一点管家登录接口-系统异常",e);
			throw new APISystemException("一点管家登录接口-系统异常", e);
		}
	}
	
	/**
	 * 统一认证接口
	 * 
	 * @Function: com.idcq.idianmgr.controller.common.MgrCommonController.authentLogin
	 * @Description:
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月4日 下午5:04:52
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月4日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value="/shop/authentLogin", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object authentLogin(HttpServletRequest request) {
		logger.info("一点管家登录认证接口-start");
		try{
			HttpSession session = request.getSession(false);
			String id = null;
			if (null != session) {
				 id = session.getId();
			}
			Cookie[] cookies = request.getCookies();
			CommonValidUtil.validObjectNull(cookies, CodeConst.CODE_COOKIES_NULL, CodeConst.MSG_COOKIES_NULL);
			if (null != cookies) {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();
					if (CommonConst.JSESSIONID.equalsIgnoreCase(name)) {
						String sessionId = DataCacheApi.get(CommonConst.KEY_JSESSIONID + value);
						logger.info("缓存中存储的cookie信息：" + sessionId);
						if (StringUtils.isNotBlank(sessionId) || (null != id && id.equals(value))) {
							return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "认证成功", null);
						}
					}
				}
			}
			return ResultUtil.getResult(CodeConst.CODE_COOKIES_INVALIDATE, CodeConst.MSG_COOKIES_INVALIDATE, null);
		} catch (ServiceException e) {
			this.logger.error("一点管家登录认证接口-系统异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("一点管家登录认证接口-系统异常",e);
			throw new APISystemException("一点管家登录认证接口-系统异常", e);
		}
	}
	
	/**
	 * 一点管家以及商铺后台登录接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/shop/shopManageLogin", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object shopManageLogin(HttpServletRequest request) {
		logger.info("一点管家登录接口-start");
		try{
			
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String password = RequestUtils.getQueryParam(request, "password");
			String roleModeStr = RequestUtils.getQueryParam(request, "roleMode");
			Integer roleMode = null;
			if(roleModeStr != null){
				CommonValidUtil.validStrIntFmt(roleModeStr,  CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ROLEMODE);
				roleMode=Integer.valueOf(roleModeStr);
			}
			// 校验是否为空
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MOBILE);
			CommonValidUtil.validStrNull(password,
					CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
			Map map = mgrCommonService.shopManageLogin(mobile, password,roleMode);
			if (null != map) {
				//创建会话id
				String sessionId = SessionUtil.dealSession(request);
				logger.info("一点管家登录生成的sessionId:" + sessionId);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_LOGIN, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("一点管家登录接口-系统异常",e);
			throw new APISystemException("一点管家登录接口-系统异常", e);
		}
	}
	
	/**
	 * 一点管家雇员升级接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/shop/upgradeEmployee", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object upgradeEmployee(HttpServletRequest request) {
		logger.info("一点管家雇员升级接口-start");
		try{
			
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String password = RequestUtils.getQueryParam(request, "password");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String employeeIdStr = RequestUtils.getQueryParam(request, "employeeId");
			
			CommonValidUtil.validStrNull(employeeIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_EMPLOYEE_NOT_NULL);
			CommonValidUtil.validStrLongFmt(employeeIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, CodeConst.MSG_FORMAT_ERROR_EMPLOYEE_ID);
			Long employeeId = Long.valueOf(employeeIdStr);
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_ID_NULL);
			CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			Long shopId = Long.valueOf(shopIdStr);
			Long userId = null;
			if(userIdStr != null){
				if(mobile != null || password!= null){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PARAMETER_NOT_ALL_EXIST);
				}
				CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, CodeConst.MSG_FORMAT_ERROR_USERID);
				userId = Long.valueOf(userIdStr);
			}else{
				CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MOBILE_NOT_NULL);
				CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PASSWROD_NOT_NULL);
			}
			mgrCommonService.upgradeEmployee(shopId,employeeId,userId,mobile,password);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "升级成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("一点管家雇员升级接口-系统异常",e);
			throw new APISystemException("一点管家雇员升级接口-系统异常", e);
		}
	}
	/**
	 * 上报登录接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/shop/clientStatInfo",
			"/token/shop/clientStatInfo",
			"/session/shop/clientStatInfo",
			"/service/shop/clientStatInfo"}, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object infoLogin(HttpServletRequest request){
		try{
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String infoTypeStr = RequestUtils.getQueryParam(request, "infoType");
			String clientTypeStr = RequestUtils.getQueryParam(request, "clientType");
			String operateUserIdStr = RequestUtils.getQueryParam(request, "operateUserId");
			String userTypeIdStr = RequestUtils.getQueryParam(request, "userTypeId");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			Long shopId = null;
			try{
				shopId = Long.parseLong(shopIdStr.trim());
			}catch (Exception e){
				logger.error(e.getMessage(), e);
				throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "shopId格式不正确");
			}
			Integer infoType = null;
			try{
				infoType = Integer.valueOf(infoTypeStr.trim());
			}catch (Exception e){
				logger.error(e.getMessage(), e);
				throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "infoType格式不正确");
			}
			Integer clientType = null;
			try{
				clientType = Integer.valueOf(clientTypeStr.trim());
			}catch (Exception e){
				logger.error(e.getMessage(), e);
				throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "clientType格式不正确");
			}
			Long operateUserId = null;
			Integer userTypeId = null;
			if(StringUtils.isNotBlank(operateUserIdStr))
			{
				try{
					operateUserId = Long.valueOf(operateUserIdStr.trim());
					userTypeId = Integer.valueOf(userTypeIdStr.trim());
				}catch (Exception e){
					logger.error(e.getMessage(), e);
					throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "operateUserId或者userTypeId格式不正确");
				}
			}
			Map<String, Object> infoParams = new HashMap<String, Object>();
			infoParams.put("shopId", shopId);
			infoParams.put("infoType", infoType);
			infoParams.put("clientType", clientType);
			infoParams.put("operateUserId", operateUserId);
			infoParams.put("userTypeId", userTypeId);
			infoParams.put("mobile", mobile);
			Map<String, Object> rs = mgrCommonService.saveLoginInfo(infoParams);
			return ResultUtil.getResult((Integer)rs.get("code"), (String)rs.get("msg"), null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("登录状态报告接口-系统异常",e);
			throw new APISystemException("登录状态报告接口-系统异常", e);
		}
	}
}
