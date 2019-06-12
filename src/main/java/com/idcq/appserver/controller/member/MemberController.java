package com.idcq.appserver.controller.member;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.dto.common.MobileAttributionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.message.PushUserMsgDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IMessageSettingService;
import com.idcq.appserver.service.message.IPushUserMsgService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.ArrayUtil;
import com.idcq.appserver.utils.BindCardPushMessageTask;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.Jpush;
import com.idcq.appserver.utils.MobileUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.SessionUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.utils.thread.ThreadPoolManager;

/**
 * 会员信息controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午7:43:09
 */
@Controller
public class MemberController {
	private final Logger logger = Logger.getLogger(MemberController.class);
	@Autowired
	private IMemberServcie memberService;
	@Autowired
	private IPayServcie payServcie;	
	@Autowired
	private ICommonService commonService;
	@Autowired
	public IPushUserMsgService pushUserMsgService;
	@Autowired
	public  IMessageSettingService messageSettingService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
    private IShopServcie shopService;
	@Autowired
	private IUserCashCouponService userCashCouponService;
	@Autowired
	private ILevelService levelService;
	/**
	 * 会员登录
	 *  
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/login",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(@ModelAttribute(value="user")UserDto user, HttpServletRequest request, HttpServletResponse response){
		try {
			logger.info("会员登录-start");
			//接口中参数为大写，实体中选择用小写，所以无法直接映射
			String sn = RequestUtils.getQueryParam(request, "SN");
			user.setSn(sn);
			UserDto userDB = this.memberService.login(user);
			Map<String, Object> map = new HashMap<String, Object>();
			if (null != userDB) {
				map.put(CommonConst.USER_ID, userDB.getUserId());
				map.put("imgBig", FdfsUtil.getFileProxyPath(userDB.getBigLogo()));
				map.put("imgSmall", FdfsUtil.getFileProxyPath(userDB.getSmallLogo()));
				map.put("nikeName", userDB.getNikeName());
				map.put("sex", userDB.getSex());
				map.put("address", userDB.getResidentTown());
				map.put("cityId", userDB.getCityId());
				map.put("cityName", userDB.getCityName());
				Integer payPasswordFlag = 0;
				if (null != userDB.getPayPassword()) {
					payPasswordFlag = 1;
				}
				map.put("payPasswordFlag", payPasswordFlag);
				DataCacheApi.setex(CommonConst.KEY_MOBILE + userDB.getMobile(), userDB.getUserId() + "", CommonConst.CACHE_USER_OUT_TIME);
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userDB.getUserId(), userDB, CommonConst.CACHE_USER_OUT_TIME);
				//创建会话id
				String sessionId = SessionUtil.dealSession(request);
				logger.info("app登录生成的sessionId:" + sessionId);
			}
			
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_LOGIN, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("会员登录-系统异常", e);
			throw new APISystemException("会员登录-系统异常", e);
		}
	}

	/**
	 * 注册会员
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/register",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto register(HttpServletRequest request, HttpServletResponse response) throws Exception{
			logger.info("注册会员-start");
			
			//注册人手机号
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			//密码
			String password = RequestUtils.getQueryParam(request, "password");
			//验证码
			String veriCode = RequestUtils.getQueryParam(request, "veriCode");
			//推荐码
			String refecode = RequestUtils.getQueryParam(request, "refecode");
			//推荐人手机号或者userId
			String refeUser = RequestUtils.getQueryParam(request, "refeUser");
			//推荐类型：0：用户 1：店铺
			String refeType = RequestUtils.getQueryParam(request, "refeType");
			//注册方式
			String registerWay = RequestUtils.getQueryParam(request, "registerWay");
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_MOBILE);
			CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_REQUIRED_MOBILE_VALID);
//			CommonValidUtil.validStrNull(password,CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD); //允许密码为空，如果为空自动生成
			Integer registerType = null;
			if(StringUtils.isNotBlank(registerWay))
			{
			    registerType = NumberUtil.strToNum(registerWay, "registerWay");
			}
			
			//注册该用户
			UserDto userDB = this.memberService.saveUser(mobile, password, veriCode, refecode, refeUser, refeType, registerType);
			
			if (refeType!=null && refeType.equals("1") && refeUser != null) {
				levelService.pushAddPointMessage(4, 2, 1, Integer.valueOf(refeUser), 2, userDB.getUserId().toString(),false);
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			if (null != userDB) {
				Long userId = userDB.getUserId();				//响应参数设定
				memberService.addShopMember(userId, refeUser, refeType, mobile);
				map.put(CommonConst.USER_ID, userId);
				if(StringUtils.isBlank(refeUser) && StringUtils.isBlank(refecode)) {
					//处理session信息
					String sessionId = SessionUtil.dealSession(request);
					logger.info("app注册生成的sessionId:" + sessionId);
				}
				if(StringUtils.isBlank(password)) {
					sendSmsToUser(mobile, userDB.getConfPassword());
				} 
				//推荐注册推送
				//根据注册人的手机号码查询出推荐人
				Long refUserId = userDB.getReferUserId();
				String referMobile = userDB.getReferMobile();
				if (null != refUserId){
					if (StringUtils.isNotBlank(refecode) && !StringUtils.isBlank(password)){ 
						//如果帮人注册，需要下发初始密码 取手机号码后6位，发送短信失败不影响注册。
						String newPassword = mobile.substring(mobile.length() - 6);
						sendSmsToUser(mobile, newPassword);
					}
					//推送给推荐人
					pushToUser(referMobile, refUserId, "refRegUser");
				}
				//推送给注册人
				pushToUser(mobile, userId, "regUser");
			
				//保存完成以后需要更新缓存数据 
				UserDto userCache = this.memberService.getUserByMobileFromDB(mobile);
				DataCacheApi.setex(CommonConst.KEY_MOBILE + mobile, userId + "", CommonConst.CACHE_USER_OUT_TIME);
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userId, userCache, CommonConst.CACHE_USER_OUT_TIME);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_REG, map);
	}

	/*
	 * 给用户推送消息
	 */
	private void pushToUser(String mobile, Long userId, String action) throws Exception {
		try {
			//注册成功以后发送推送消息
			JSONObject jsonObject = new JSONObject();
			//构造消息体
			jsonObject.put("action", action);
			jsonObject.put("mobile", mobile);
			
			this.commonService.pushUserMsg(jsonObject, userId, CommonConst.USER_TYPE_ZREO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("推送消息给用户失败", e);
		}
		
	}
	
	/*
	 * 发送明文密码给用户
	 */
	private void sendSmsToUser(String mobile, String password) {
		try {
			SmsReplaceContent src = new SmsReplaceContent();
			src.setMobile(mobile);
			src.setCode(password);
			src.setUsage(CommonConst.USER_REGISTR_SUCCESS);
			sendSmsService.sendSmsMobileCode(src);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送短信验证码失败", e);
		}
	}
	
	
	/**
	 * 重设密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/modifyPwd",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto restPassword(HttpServletRequest request){
		try {
			logger.info("重置会员密码-start");
			memberService.updatePassword(request);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_RESETPWD, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("会员重置密码-系统异常",e);
			throw new APISystemException("会员重置密码-系统异常", e);
		}
	}
	
	
	/**
	 * 编辑会员基本资料
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/modifyBaseInfo",produces = "application/json;charset=UTF-8",
			method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto modifyBaseInfo(@RequestBody Map<String, String> hashMap,
			HttpServletRequest request){
		try {
			logger.info("编辑会员基本资料-start" + hashMap);
			UserDto user = getRequestUser(hashMap);
			int num = this.memberService.updateBaseInfo(user); 
			if(0 == num) {
				// 验证用户存在性
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			
			//从数据库中查询最新的用户信息更新缓存
			Long userId = user.getUserId();
			UserDto userDto = this.memberService.getDBUserById(userId);
			DataCacheApi.setObjectEx(CommonConst.KEY_USER + userId, userDto, CommonConst.CACHE_USER_OUT_TIME);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_EDITUSER, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);  
		} catch (Exception e) {
			this.logger.error("编辑会员基本资料-系统异常",e);
			throw new APISystemException("编辑会员基本资料-系统异常", e);
		}
	}

	
	
	/**
	 * 绑定银行卡
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/bindBankCard",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto bindBankCard(HttpServletRequest request){
		try {
			logger.info("会员绑定银行卡-start");

			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			String type = RequestUtils.getQueryParam(request, "type"); // 类型：1-绑定用户银行卡 2-绑定商铺银行卡 ，当type=2时userId传商铺ID
			String cardNumber = RequestUtils.getQueryParam(request, "cardNumber");
 			String bankName = RequestUtils.getQueryParam(request, "bankName");
 			String bankSubbranchName = RequestUtils.getQueryParam(request, "bankSubbranchName");
			String name = RequestUtils.getQueryParam(request, "name");
			String idCard = RequestUtils.getQueryParam(request, "idCard");
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			BankCardDto bankCard = new BankCardDto();
			
			
			if(StringUtils.isNotEmpty(type) && "2".equals(type)){
				bankCard.setAccountType(2); // 商铺账户
			}else{
				bankCard.setAccountType(1); // 用户账户
			}
			bankCard.setUserId(userId);
			bankCard.setName(name);
			bankCard.setCardNumber(cardNumber);
			bankCard.setIdNum(idCard);
			bankCard.setTime(new Date());
			bankCard.setBankName(bankName);
			bankCard.setBankSubbranchName(bankSubbranchName);
			bankCard.setLastUseTime(new Date());
			memberService.bindBankCard(bankCard);
			
			// 商铺不推
			if(bankCard.getAccountType() == 1){
				//推送信息
				ThreadPoolManager threadPoolManager = ThreadPoolManager
						.getInstance();
				BindCardPushMessageTask task = new BindCardPushMessageTask(bankCard);
				threadPoolManager.execute(task);
			}
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_BINDCARD, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("会员绑定银行卡-系统异常",e);
			throw new APISystemException("会员绑定银行卡-系统异常", e);
		}
	}
	/**
	 * 解绑银行卡
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/unbindBankCard",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto unbindBankCard(HttpServletRequest request){
		try {
			logger.info("解除绑定银行卡-start");

			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			String cardNumber = RequestUtils.getQueryParam(request, "cardNumber");
			String type = RequestUtils.getQueryParam(request, "type"); // 1-用户  2-商铺
			
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			if (StringUtils.isBlank(cardNumber)) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
						CodeConst.MSG_REQUIRED_BANKCARD);
			}
			
			
			if("2".equals(type)){
				// 商铺解绑
				ShopDto shopDto = shopService.getShopById(userId);
				CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST,"商铺不存在");
			}else {
				// 查询用户是否存在
				UserDto userDB = memberService.getUserByUserId(userId);
				CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_MISS_MEMBER);
			}
			BankCardDto bankCard = new BankCardDto();
			bankCard.setUserId(userId);
			bankCard.setCardNumber(cardNumber);
			memberService.unBindBankCard(bankCard);
			//推送信息
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UNBINDCARD, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("解除绑定银行卡-系统异常",e);
			throw new APISystemException("解除绑定银行卡-系统异常", e);
		}
	}
	
	/**
	 * 修改手机号码
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/modifyMobile",produces = "application/json;charset=UTF-8",
			method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public ResultDto modifyMobile(@RequestBody Map<String, String> hashMap, HttpServletRequest request){
		try {
			logger.info("修改手机号码-start" + hashMap);
			this.memberService.updateMobile(hashMap);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_RESETMOBILE, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("修改手机号码-系统异常",e);
			throw new APISystemException("修改手机号码-系统异常", e);
		}
	}
	
	/**
	 * 获取会员基本资料
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getBaseInfo",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getBaseInfo(HttpServletRequest request){
		try {
			logger.info("获取会员基本资料-start");
			String userId = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			Long uid = NumberUtil.strToLong(userId, "userId");
			UserDto user = memberService.getBaseInfo(uid);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_MEMBER, user);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取会员基本资料-系统异常",e);
			throw new APISystemException("获取会员基本资料-系统异常", e);
		}
	}
	
	/**
	 * 修改支付密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/modifyPayPwd", method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto modifyPayPwd(@RequestBody Map<String, String> hashMap, HttpServletRequest request){
		try {
			logger.info("修改支付密码-start:" + hashMap);
			String userIdStr = hashMap.get(CommonConst.USER_ID);
			
			int type = 1;
			String typeStr = hashMap.get("type");
			if("2".equals(typeStr)){
				type = 2;
			}
			
			String payPassword = hashMap.get("payPassword");
			String newPayPwd = hashMap.get("newPayPwd");//新密码
			String confNewPayPwd = hashMap.get("confNewPayPwd"); //确认密码
			
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			CommonValidUtil.validStrNull(newPayPwd, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_NEWPAYPWD);
			CommonValidUtil.validStrNull(confNewPayPwd, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CFPWD);
			
			if(!newPayPwd.equals(confNewPayPwd)){//密码不一致
				throw new ValidateException(CodeConst.CODE_PWD_NOT_SAME, CodeConst.MSG_NOSAME_PWD);
			}
			
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			
			
			if(type == 1){
				UserDto user = memberService.getUserByUserId(userId);
				CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				if (1 != user.getStatus()) {
					throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
				}
				if(!payPassword.equals(user.getPayPassword())){//原支付密码不正确
					throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAY_PWD_ERROR);
				}
				
				user.setPayPassword(newPayPwd);
				memberService.updatePayPassword(user);
				//更新缓存
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userIdStr, user, CommonConst.CACHE_USER_OUT_TIME);
			}else {
				memberService.updateShopPayPwd(userId,payPassword,newPayPwd);
			}
			
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_MODIFY_PAY_PWD, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("修改支付密码-系统异常",e);
			throw new APISystemException("修改支付密码-系统异常", e);
		}
	}
	/**
	 * 认证用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/authRealName",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto authRealName(HttpServletRequest request){
		try {
			logger.info("认证用户-start");
			//userid
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			//真实姓名
			String trueName = RequestUtils.getQueryParam(request, "trueName");
			//身份证号码
			String identityCardNo = RequestUtils.getQueryParam(request, "identityCardNo");
			//验证userid是否为空
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
			// 验证long
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			//验证用户是否存在 
			UserDto user = memberService.getUserByUserId(userId);
			if(null==user){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}		
			user.setTrueName(trueName);
			user.setIdentityCardNo(identityCardNo);
			//实名认证，修改用户真实姓名、身份证号码
			memberService.authRealName(user);
			// 判断消息推送开关是否开启
			MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey("authRealName");
			if(null != messageSettingDto && 1==messageSettingDto.getRemandFlag()) {
			//TODO 推送信息
				List<PushUserTableDto> listPushUser  = memberService.getPushUserByUserId(userId,CommonConst.USER_TYPE_ZREO);
				pushMessage(listPushUser, "authRealName");
			}
  			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_AUTHUSER, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("认证用户-系统异常",e);
			throw new APISystemException("认证用户-系统异常", e);
		}
	}
	/**
	 * 查询用户会员卡列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserMembershipCardList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserMembershipCardList(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("查询用户会员卡列表-start");
		
			//userid
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			//当前页
			Integer pageNo = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			//页大小
			Integer pageSize =  PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			//验证参数
			Long userId = verifyParameter(userIdStr,1);
			int rCount = memberService.getUserMembershipCardListCount(userId);
			//查询列表
			List<Map<String, Object>>  lst = memberService.getUserMembershipCardList(userId, pageNo, pageSize);
			map.put("pNo", pageNo);
			map.put("rCount", rCount);
			map.put("lst", lst);
			//return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_USER_CARD, map);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,CodeConst.MSG_SUCCEED_USER_CARD,map,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询用户会员卡列表-系统异常",e);
			throw new APISystemException("查询用户会员卡列表-系统异常", e);
		}
	}	
	/**
	 * 查询用户会员卡详细信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserMembershipCardInfo",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserMembershipCardInfo(HttpServletRequest request){
		try {
			logger.info("查询会员卡详细信息-start");
			//会员卡id
			String accountId = RequestUtils.getQueryParam(request, "accountId");
			if(StringUtils.isEmpty(accountId)){
				throw new ValidateException(CodeConst.CODE_ACCOUNT_NULL, CodeConst.MSG_ACCOUNT_NULL);
			}
			//userId参数不合法
			if(!NumberUtil.isNumeric(accountId)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ACCOUNT_ID);
			}
			CommonValidUtil.validStrLongFormat(accountId, CodeConst.CODE_PARAMETER_NOT_VALID,  CodeConst.MSG_FORMAT_ERROR_ACCOUNT_ID);
			//查询会员卡信息信息
			Map<String, Object> map =  memberService.getUserMembershipCardInfo(Long.parseLong(accountId));
			if(null==map){
				throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_EXIST, CodeConst.MSG_ACCOUNT_NOT_EXIST);				
			}
			//return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CARD_INFO, map);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,CodeConst.MSG_SUCCEED_CARD_INFO,map,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询会员卡详细信息-系统异常",e);
			throw new APISystemException("查询会员卡详细信息-系统异常", e);
		}
	}		
	
	/**
	 * 查询传奇宝账户余额
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getAccountMoney",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getAccountMoney(HttpServletRequest request){
		try {
			logger.info("查询传奇宝账户余额-start");
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			UserDto user=memberService.getUserByUserId(userId);
			if(user==null){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			UserAccountDto userAccount = memberService.getAccountMoney(userId);
			if(userAccount==null){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_ACCOUNT_NOT_EXIST);
			}
			Double avaMoney = NumberUtil.add(userAccount.getCouponAmount(),userAccount.getRewardAmount());
			Map<String, Object> map = new HashMap<String,Object>();
			//TODO 上线前放开
			map.put("amount", NumberUtil.formatDouble(avaMoney,4));
			map.put("rewardAmount",NumberUtil.formatDoubleStr(userAccount.getRewardAmount(),4));
			map.put("shoppingAmount",NumberUtil.formatDoubleStr(userAccount.getCouponAmount(),4));
			map.put("rewardTotal",NumberUtil.formatDoubleStr(userAccount.getRewardTotal(),4));
			map.put("economizeTotal",NumberUtil.formatDoubleStr(userAccount.getSalesTotal(),4));
			map.put("accountStatus", userAccount.getAccountStatus());


			map.put("legendTotal", NumberUtil.formatDoubleStr(userAccount.getLegendTotal(),4));
			map.put("consumeRebateMoney", NumberUtil.formatDoubleStr(userAccount.getConsumeRebateMoney(),4));
			map.put("consumeRebateTotal", NumberUtil.formatDoubleStr(userAccount.getConsumeRebateTotal(),4));
			map.put("deductionCountValue", NumberUtil.formatDoubleStr(userAccount.getDeductionCountValue(),4));
			map.put("deductionTotal", NumberUtil.formatDoubleStr(userAccount.getDeductionTotal(),4));
			map.put("voucherAmount", NumberUtil.formatDoubleStr(userAccount.getVoucherAmount(),4));
			map.put("voucherTotal", NumberUtil.formatDoubleStr(userAccount.getVoucherTotal(),4));
			map.put("consumeAmount", NumberUtil.formatDoubleStr(userAccount.getConsumeAmount(),4));
			map.put("consumeTotal", NumberUtil.formatDoubleStr(userAccount.getConsumeTotal(),4));
			map.put("couponRebatesTotal", NumberUtil.formatDoubleStr(userAccount.getCouponRebatesTotal(),4));
			//消费卡余额
			Double cashCouponAmount  = userCashCouponService.getUserCashCouponBalance(userId)==null 
					? 0D : userCashCouponService.getUserCashCouponBalance(userId);
			//账户总额  账户总额=传奇宝余额+消费卡余额
			Double totalAmount = NumberUtil.add(avaMoney, cashCouponAmount);
			map.put("cashCouponAmount",NumberUtil.formatDoubleStr(cashCouponAmount,2));
			map.put("totalAmount",NumberUtil.formatDoubleStr(totalAmount,4));
			//支付密码 1代表用户
	        Map<String, Object> payPasswordMap = this.memberService.getPayPwdStatus(userId, 1);
	        map.put("payPasswordFlag",payPasswordMap.get("payPwdStatus"));
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_ACCOUNT_MONEY, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询传奇宝账户余额-系统异常",e);
			throw new APISystemException("查询传奇宝账户余额-系统异常", e);
		}
	}
	
	/**
	 * 获取用户的银行卡列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserBankCards",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserBankCards(HttpServletRequest request){
		try {
			logger.info("获取用户的银行卡列表-start");
			
			//userid
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			
			int type =1;  // 类型：1-用户银行卡 2-商铺银行卡 ，当type=2时userId传商铺ID	
			String typeStr = RequestUtils.getQueryParam(request, "type");
			if(StringUtils.isNotBlank(typeStr) && "2".equals(typeStr)){
				type = 2;
			}
			
			Integer pNo = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));

			Integer pSize = PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			
			Long userId = verifyParameter(userIdStr, type);
			PageModel pm = memberService.getUserBankCards(userId, pNo, pSize);
			MessageListDto msgList = new MessageListDto();
			msgList.setLst(pm.getList());
			msgList.setpNo(pNo);
			msgList.setpSize(pSize);
			msgList.setrCount(pm.getTotalItem());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_BANKCARDS, msgList,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取用户的银行卡列表-系统异常",e);
			throw new APISystemException("获取用户的银行卡列表-系统异常", e);
		}
	}
	
	
	
	/**
	 * 查询用户的账单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserBill",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserBill(HttpServletRequest request){
		try {
			logger.info("查询用户的账单列表-start");
			long start=System.currentTimeMillis();
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			String billType = RequestUtils.getQueryParam(request, "billType");
			String billStatusFlagStr = RequestUtils.getQueryParam(request, "billStatusFlag");//账单状态的进行中标记：1（进行中），0（已完成）
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			Integer pNo = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pSize = PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String nowDate=format.format(new Date());
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			final Long userId = NumberUtil.strToLong(userIdStr, "userId");
			/*if(startTime==null){
				startTime=format.format(new Date().getTime()-7*24*60*60*1000)+" 00:00:00";
			}
			if(endTime==null){
				endTime=nowDate+" 23:59:59";
			}*/
			if("".equals(billType)){
				billType=null;
			}
			if("".equals(billStatusFlagStr)){
				billStatusFlagStr=null;
			}
			Integer billStatusFlag=null;
			if(null != billStatusFlagStr){
				billStatusFlag=NumberUtil.strToNum(billStatusFlagStr,"billStatusFlag");
			}
			PageModel pm = memberService.getUserBill(userId, billType, startTime, endTime, pNo, pSize,billStatusFlag);
			MessageListDto msgList = new MessageListDto();
			msgList.setLst(pm.getList());
			msgList.setrCount(pm.getTotalItem());
			msgList.setpNo(pm.getToPage());
			msgList.setpSize(pm.getPageSize());
			logger.info("查询用户的账单共耗时:"+(System.currentTimeMillis()-start));
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_USER_BILL, msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询用户的账单列表-系统异常",e);
			//JedisPoolUtils.returnRes(jedis,true);
			throw new APISystemException("查询用户的账单列表-系统异常", e);
		}
	}
	/**
	 * 查询用户搜索记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getSearchHistory",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getSearchHistory(HttpServletRequest request){
		try {
			logger.info("查询用户搜索记录-start");
			Map<String, Object> map = new HashMap<String, Object>();
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			Integer pNo = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pSize = PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			//验证参数合法性
			Long userId = verifyParameter(userIdStr,1);
			//用户搜索记录总数
			int count = memberService.getSearchHistoryCount(userId);
			//用户搜索记录
			List<Map<String, Object>> list = memberService.getSearchHistory(userId, pNo, pSize);
			map.put("pNo", pNo);
			map.put("rCount", count);
			map.put("lst", list);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_QUERY_HISTORY, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询用户搜索记录-系统异常",e);
			throw new APISystemException("查询用户搜索记录-系统异常", e);
		}
	}	
	/**
	 * 清空用户搜索记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/clearSearchHistory",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object clearSearchHistory(HttpServletRequest request){
		try {
			logger.info("清空用户搜索记录-start");
			Map<String, Object> map = new HashMap<String, Object>();
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			String searchContent = RequestUtils.getQueryParam(request,"searchContent");
			if((StringUtils.isBlank(userIdStr))){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
			}
			
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			
			//验证用户是否存在
			UserDto user = memberService.getUserByUserId(userId);
			if(null==user){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			memberService.deleteUserSearchHistory(userId,searchContent);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_DELETE_HISTORY, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("清空用户搜索记录-系统异常",e);
			throw new APISystemException("清空用户搜索记录-系统异常", e);
		}
	}	
	
	/**
	 * 获取推荐码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getRefecode",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getRefeCode(HttpServletRequest request){
		try {
			logger.info("获取推荐码");
			//推荐人编号
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			//被推荐人手机号码
			String referMobile = RequestUtils.getQueryParam(request, "referMobile");
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validStrNull(referMobile, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_REFER_MOBILE);
			CommonValidUtil.validMobileStr(referMobile,CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_REQUIRED_MOBILE_VALID);
			String refecode = "";
			//synchronized(this){}
			refecode = memberService.insertUserReferInfo(userId,referMobile);
			Map pModel = new HashMap();
			pModel.put("refecode", refecode);
			return ResultUtil.getResult(0, "获取推荐码成功！", pModel);
		}catch (ServiceException e){
			logger.error("获取推荐码异常",e);
			throw new APIBusinessException(e);
		}catch (Exception e) {
			logger.error("获取推荐码异常",e);
			throw new APISystemException("获取推荐码异常", e);
		}
	}
	
	/**
	 * 推荐码验证
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/verifyRefecode",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto verifyRefeCode(HttpServletRequest request){
		try {
			logger.info("推荐码验证");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String refecode = RequestUtils.getQueryParam(request, "refecode");
			int valiFlag = memberService.verifyReferCode(mobile,refecode);
			if (valiFlag == 0) {
				//失败
				return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_NULL, "推荐码验证失败，推荐码跟手机号码不匹配！", null);
			}else{
				//成功
				return ResultUtil.getResult(0, "推荐码验证通过！", null);
			}
		}catch (ServiceException e){
			logger.error("推荐码验证异常",e);
			throw new APIBusinessException(e);
		}catch (Exception e) {
			logger.error("推荐码验证异常",e);
			throw new APISystemException("推荐码验证异常", e);
		}
	}
	
	@RequestMapping(value="/user/setPayPwd", method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto setPayPwd(@RequestBody Map<String, String> hashMap, HttpServletRequest request){
		try {
			logger.info("设置支付密码-start" + hashMap);
			String userIdStr = hashMap.get(CommonConst.USER_ID);
			int type = 1;
			String typeStr = hashMap.get("type");
			if("2".equals(typeStr)){
				type = 2;
			}
			String payPwd = hashMap.get("payPwd");
			String confPayPwd = hashMap.get("confPayPwd");
			String veriCode = hashMap.get("veriCode");
			String usage = hashMap.get("usage");
			if(type == 1){
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			}else{
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			}
			CommonValidUtil.validStrNull(payPwd, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			CommonValidUtil.validStrNull(confPayPwd, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CFPWD);
			
			if (!payPwd.equals(confPayPwd)) {// 密码不一致
				throw new ValidateException(CodeConst.CODE_PWD_NOT_SAME, CodeConst.MSG_NOSAME_PWD);
			}
			
			if(type == 1){
				UserDto user = memberService.getUserByUserId(NumberUtil.strToLong(userIdStr, "userId"));
				CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				if(StringUtils.isNotBlank(veriCode)) {
		            boolean flag = sendSmsService.checkSmsCodeIsOk(user.getMobile(), usage, veriCode);
		            if(!flag) {
		                //验证不通过
		                throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
		            }
				}
				user.setPayPassword(payPwd);
				memberService.updatePayPassword(user);
				//更新缓存
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userIdStr, user, CommonConst.CACHE_USER_OUT_TIME);
			}else {
				// 修改商铺支付密码
				memberService.setShopPayPwd(NumberUtil.strToLong(userIdStr, "userId"), payPwd);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_INSERT_PAY_PWD, null);
		} catch (ServiceException e) {
			logger.error("设置支付密码异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("设置支付密码异常", e);
			throw new APISystemException("设置支付密码异常", e);
		}
	}
	
	/**
	 * 查询用户抵用券汇总
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserVoucherInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getUserVoucherInfo(HttpServletRequest request){
		try {
			String userId = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			//验证用户的存在性、userId 格式
			CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			UserDto userDB = this.memberService.getUserByUserId(NumberUtil.strToLong(userId, "userId"));
			CommonValidUtil.validObjectNull(userDB,
					CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			Map<String, Object> map = this.memberService.getUserVoucherInfo(NumberUtil.strToLong(userId, "userId"));
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GET_USERVOUCHERINFO, map);
		} catch (ServiceException e) {
			logger.error("查询用户抵用券汇总信息异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询用户抵用券汇总信息异常", e);
			throw new APISystemException("查询用户抵用券汇总信息异常", e);
		}
	}
	
	
	/**
	 * 验证userId、pNo、pSize参数合法性
	 * @param userId
	 * @param pNo
	 * @param pSize
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Long verifyParameter(String userIdStr, int type) throws Exception{
		//验证userid是否为空
		
		CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
		//userId参数不合法
		Long userId = NumberUtil.strToLong(userIdStr, "userId");

		//验证用户是否存在 
		if(type == 1) {
			UserDto user = memberService.getUserByUserId(userId);
			CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		}
		
		return userId;
	}
	
	/**
	 * 封装user对象
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	private UserDto getRequestUser(Map<String, String> hashMap)
			throws Exception {
		String userId = hashMap.get(CommonConst.USER_ID);
		String imgBig = hashMap.get("imgBig");
		String imgSmall = hashMap.get("imgSmall");
		String nikeName = hashMap.get("nikeName");
		String sex = hashMap.get("sex");
		String provinceId = hashMap.get("provinceId");
		String cityId = hashMap.get("cityId");
		String districtId = hashMap.get("districtId");
		String townId = hashMap.get("townId");
		String birthday =  hashMap.get("birthday");
		
		CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
		CommonValidUtil.validStrNull(provinceId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PROVINCEID);
		CommonValidUtil.validStrNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CITYID);
		
		UserDto user = new UserDto(); 
		user.setUserId(NumberUtil.strToLong(userId, "userId"));
		user.setBigLogo(imgBig);
		user.setSmallLogo(imgSmall);
		user.setNikeName(nikeName);
		if(StringUtils.isNotEmpty(sex)) {
			user.setSex(NumberUtil.strToNum(sex, "sex"));
		}
		user.setProvinceId(NumberUtil.strToLong(provinceId, "provinceId"));
		user.setCityId(NumberUtil.strToLong(cityId, "cityId"));
		if(StringUtils.isNotEmpty(districtId)) {
			user.setDistrictId(NumberUtil.strToLong(districtId, "districtId"));
		}
		if(StringUtils.isNotEmpty(townId)) {
			user.setTownId(NumberUtil.strToLong(townId, "townId"));
		}
		if(StringUtils.isNotEmpty(birthday)) {
			Date date = DateUtils.stringToDate(birthday);
			if(null == date)
			{
				String message = "birthday" + CodeConst.MSG_INVALID_PARAMETER;
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, message);
			}
			user.setBirthday(date);
		}
		user.setLastUpdateTime(new Date());
		
		return user;
	}
	/**
	 * 用户提现记录列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getWithdrawList",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getWithdrawList(HttpServletRequest request){
		try {
			logger.info("查询用户提现记录-start");
			//userId
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			
			int type = 1;
			String typeStr = RequestUtils.getQueryParam(request, "type");
			if("2".equals(typeStr)){
				type = 2;
			}
			
			
			//查询起始时间
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			//查询结束时间
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			//当前页
			Integer pageNo = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			//页大小
			Integer pageSize =  PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
		
			//验证参数
			Long userId = verifyParameter(userIdStr, type);
			Date curDate1 = DateUtils.getCurrentDate("yyyy-MM-dd 23:59:59");
			Date curDate2 = DateUtils.getCurrentDate("yyyy-MM-dd 00:00:00");
			if(StringUtils.isBlank(startTime)){
				Calendar c = Calendar.getInstance();
			    c.setTime(curDate2);
			    //为空时，一周内信息
			    c.add(Calendar.DATE, -7);
			    startTime = DateUtils.format(c.getTime(), "yyyy-MM-dd 00:00:00");
				
			}
			if(StringUtils.isBlank(endTime)){ 
				endTime =  DateUtils.format(curDate1, "yyyy-MM-dd 23:59:59");
			}
			Map<String,Object> mapParameter =  new HashMap<String,Object>();
			mapParameter.put("startTime", startTime);
			mapParameter.put("endTime", endTime);
			mapParameter.put("userId", userId);
			
			int count = 0;
			List<Map<String, Object>> lst = null;
			
			if(type == 1){
				count = payServcie.getWithdrawListCount(mapParameter);
				lst = payServcie.getWithdrawList(mapParameter, pageNo, pageSize);
			}else {
				count = payServcie.getShopWithdrawListCount(mapParameter);
				lst = payServcie.getShopWithdrawList(mapParameter, pageNo, pageSize);
			}
			
			Map<String,Object> map =  new HashMap<String,Object>();
			map.put("pNo", pageNo);
			map.put("count", count);
			map.put("lst", lst);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SEARCH_WITHDRAW_SUCCESS, map, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询用户提现记录-系统异常",e);
			throw new APISystemException("查询用户提现记录-系统异常", e);
		}
	}
	
	/**
	 * 查询账单详情接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getUserBillDetail",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserBillDetail(HttpServletRequest request){
		try {
			logger.info("查询账单详情接口-start");
			String billId = RequestUtils.getQueryParam(request, "billId");
			CommonValidUtil.validStrNull(billId, CodeConst.CODE_PARAMETER_NOT_NULL,"账单Id不能为空");
			CommonValidUtil.validNumStr(billId, CodeConst.CODE_PARAMETER_NOT_VALID, "账单Id格式非法");
			
			Map<String, Object> map = memberService.getUserBillDetail(Long.valueOf(billId));
			
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取账单详情成功！", map, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询账单详情接口-系统异常",e);
			throw new APISystemException("查询账单详情接口-系统异常", e);
		}
	}
	

	@RequestMapping(value="/user/uploadFile", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadFile(@RequestParam("file") MultipartFile myfile, HttpServletRequest request) {
		logger.info("上传文件-start");
		try{
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String usageType = RequestUtils.getQueryParam(request, "usageType");
			String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
			String mimeType = RequestUtils.getQueryParam(request, "mimeType");
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validStrNull(usageType, CodeConst.CODE_PARAMETER_NOT_NULL, "usageType" + CodeConst.MSG_REQUIRED_NOT_NULL);
			CommonValidUtil.validPositInt(usageType, CodeConst.CODE_PARAMETER_NOT_VALID, "usageType" + CodeConst.MSG_FORMAT_ERROR_PARAM);
			CommonValidUtil.validStrNull(mimeType, CodeConst.CODE_PARAMETER_NOT_NULL, "mimeType" + CodeConst.MSG_REQUIRED_NOT_NULL);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			//9种图片格式
			String imgExt = CommonConst.IMAGE_FORMAT;
			if (!imgExt.contains(mimeType.toUpperCase())) { 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "mimeType" + CodeConst.MSG_INVALID_PARAMETER );
			}
			Long bizId = null;
			if (!usageType.startsWith("1")) {
				CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId" + CodeConst.MSG_REQUIRED_NOT_NULL);
				bizId = NumberUtil.strToLong(bizIdStr, "bizId");
			}
			if(myfile.isEmpty()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "图片上传失败");
			}
			logger.info("文件大小: " + myfile.getSize() + " 文件类型: "  + myfile.getContentType() + " 文件名称: " + myfile.getName());
			
			if(myfile.getSize() > 10485760) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "图片大小超过10M不允许上传");
			}
			
			//更新商品评论、商铺评论、用户头像logo。
			Map map= this.memberService.updateLogo(userId, usageType, bizId, mimeType, myfile);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UPLOADFILE, map);
		} catch (ServiceException e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("上传失败-系统异常",e);
			throw new APISystemException("上传失败-系统异常", e);
		}
	}
	
	/**
	 * 推送消息系统给客户端注册
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/addRegId",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object addRegId(HttpServletRequest request){
		try {
			logger.info("推送消息系统给客户端注册-start");
			this.memberService.addRegId(request);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_REGID_SUCCESS, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("推送消息系统给客户端注册失败-系统异常",e);
			throw new APISystemException("推送消息系统给客户端注册失败-系统异常", e);
		}
	}
	private void pushMessage(List<PushUserTableDto> listPushUser,String actonName) throws Exception{
		if(CollectionUtils.isNotEmpty(listPushUser)){
			for (PushUserTableDto pushUserTableDto : listPushUser) {
				Map<String, Object> map = new HashMap<String, Object>();
				String registrationId = pushUserTableDto.getRegId();
				String osInfo= pushUserTableDto.getOsInfo();
				map.put("action", actonName);
				String messageStr = JacksonUtil.objToString(map);
				//发送消息
				Jpush.sendPushToTarget(osInfo,pushUserTableDto.getUserType(), registrationId,null, messageStr);
				//保存推送信息
				PushUserMsgDto pushUserMsgDto = new PushUserMsgDto();
				pushUserMsgDto.setAction(actonName);
				pushUserMsgDto.setMessageContent(messageStr);
				pushUserMsgDto.setUserId(pushUserTableDto.getUserId());
				pushUserMsgDto.setRegId(registrationId);
				pushUserMsgDto.setSendTime(new Date());
				pushUserMsgService.insertSelective(pushUserMsgDto);
			}
		}
	}
	
	/**
	 * 我的推荐用户列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getMyRefUsers",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getMyRefUsers(HttpServletRequest request) {
		try{
			logger.info("查询我的推荐用户列表-start");
			String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
			String userIdStr =  RequestUtils.getQueryParam(request,  CommonConst.USER_ID);
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			PageModel pageModel = this.memberService.getMyRefUsers(userId, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_MYREF_SUCCESS, msgList, DateUtils.DATE_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询我的推荐用户列表失败-系统异常",e);
			throw new APISystemException("查询我的推荐用户列表失败-系统异常", e);
		}
	}
	
	/**
	 * 查询用户可用红包总额接口 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getRedPacketTotalMoney",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getRedPacketTotalMoney(HttpServletRequest request) {
		try{
			logger.info("查询我的可用红包总额-start");
			String userIdStr =  RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			Map<String, Object> map = this.memberService.getRedPacketTotalMoney(userId);
		
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_REDPACKET_MONEY, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询我的可用红包总额失败-系统异常",e);
			throw new APISystemException("查询我的可用红包总额失败-系统异常", e);
		}
	}
	
	/**
	 * P38 查询支付密码设定状态接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getPayPwdStatus",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getPayPwdStatus(HttpServletRequest request) {
		try{
			logger.info("查询支付密码设定状态接口-start");
			String userIdStr =  RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			int type =1;
			String typeStr =  RequestUtils.getQueryParam(request, "type");
			if("2".equals(typeStr)){
				type = 2;
			}
			if(type == 1){
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			}else{
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			}
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			
			Map<String, Object> map = this.memberService.getPayPwdStatus(userId, type);
		
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GET_PAYPWD_STATUS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询支付密码设定状态接口-系统异常",e);
			throw new APISystemException("查询支付密码设定状态接口", e);
		}
	}
    /**
     * 查询用户奖励类型
     * @param request
     * @return
     */
    @RequestMapping(value="/user/getMyRewardType",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getMyRewardType(HttpServletRequest request) throws Exception
    {
            logger.info("获取奖励类型接口-start");
            String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
            // 验证userid
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
            Long userId = NumberUtil.strToLong(userIdStr, "userId");
            UserDto user = memberService.getUserByUserId(userId);
            if (null == user)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            }
			String accountTypeStr = RequestUtils.getQueryParam(request, "accountType");
			String[] accountTypes = null;
			if(StringUtils.isNotBlank(accountTypeStr))
			{
			    accountTypes = accountTypeStr.split(CommonConst.COMMA_SEPARATOR);
			}
            List<Map<String, Object>> resultList = memberService.getMyRewardType(userId, accountTypes);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取奖励类型列表成功！", resultList);
    }
    /**
     * P38：查询会员账单统计 
     * @param request
     * @return
     */
    @RequestMapping(value="/user/getBillStat",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getBillStat(HttpServletRequest request) throws Exception
    {
            logger.info("查询会员账单统计-start");
            String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
            String accountTypeStr = RequestUtils.getQueryParam(request, "accountType");
            String startTimeStr = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String moneyTypeStr = RequestUtils.getQueryParam(request, "moneyType");
            String areaIdStr = RequestUtils.getQueryParam(request, "areaId");
            Map<String, Object> paraMap = new HashMap<String, Object>();
            // 验证userid
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
            Long userId = NumberUtil.strToLong(userIdStr, "userId");
            UserDto user = memberService.getUserByUserId(userId);
            if (null == user)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            }
            paraMap.put("userId", userId);
            // accountType
            CommonValidUtil.validStrNull(accountTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "accountType不能为空");
            paraMap.put("accountType", accountTypeStr.split(","));
            // startTime
            CommonValidUtil.validStrNull(startTimeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");
            CommonValidUtil.validDateTimeFormat(startTimeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            paraMap.put("startTime", startTimeStr);
            // endTime
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                paraMap.put("endTime", endTime);
            }
            // money
            if (StringUtils.isNotBlank(moneyTypeStr))
            {
            	 paraMap.put("moneyType", moneyTypeStr.split(","));
            }
        	//校验areaId  
			if (StringUtils.isNotBlank(areaIdStr)) {
				Integer areaId = CommonValidUtil.validStrIntFmt(areaIdStr,
						CodeConst.CODE_PARAMETER_NOT_VALID, "areaId类型错误");
				 paraMap.put("areaId", areaId);
			}
			paraMap.put("isShow", CommonConst.USER_BILL_IS_SHOW);
            List<Map<String, Object>> reList = memberService.getBillStat(paraMap);
            NumberUtil.formatDoubleResult2BigDecimalResult(reList, new String[] {"money"}, 4);
        
            Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", reList);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询会员账单统计成功", resultMap);
    }
	/**
	 * 刷新用户归属地信息
	 * 条件：用户归属地为null
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/updateNoCityMobile",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateNoCityMobile(HttpServletRequest request) {
		try{
			new Thread(){
				public void run(){
					//1-5w数据
					updateCityMobileThread1();
				}
			}.start();
			new Thread(){
				public void run(){
					//5w-10w
					updateCityMobileThread2();
				}
			}.start();
			new Thread(){
				public void run(){
					//10w-20w
					updateCityMobileThread3();
				}
			}.start();
			new Thread(){
				public void run(){
					//20w-30w
					updateCityMobileThread4();
				}
			}.start();
			new Thread(){
				public void run(){
					//30w-??
					updateCityMobileThread5();
				}
			}.start();
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "刷新临时表用户归属地成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("刷新临时表用户归属地成功-系统异常",e);
			throw new APISystemException("刷新临时表用户归属地成功", e);
		}
	}
	
	private void updateCityMobileThread1() {
		try {
			logger.info("刷新用户归属地信息1-start");
			Integer limit = 0;
			Integer pNo = 20;
			while (true) {
				List<Map<String, Object>> userList = memberService.getNoCityMobile(limit, pNo);
				if (CollectionUtils.isEmpty(userList)||limit>=20000) {//10w
					break;
				} else {
					for (Map<String, Object> map : userList) {
						String mobile = (String) map.get("mobile");
						//更新归属地
						updateNoCityMobile(mobile);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateCityMobileThread2(){
		try {
			logger.info("刷新用户归属地信息2-start");
			Integer limit = 20000;
			Integer pNo = 20;
			while (true) {
				List<Map<String, Object>> userList = memberService.getNoCityMobile(limit, pNo);
				if (CollectionUtils.isEmpty(userList)||limit>=40000) {
					break;
				} else {
					for (Map<String, Object> map : userList) {
						String mobile = (String) map.get("mobile");
						//更新归属地
						updateNoCityMobile(mobile);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateCityMobileThread3(){
		try {
			logger.info("刷新用户归属地信息3-start");
			Integer limit = 40000;
			Integer pNo = 20;
			while (true) {
				List<Map<String, Object>> userList = memberService.getNoCityMobile(limit, pNo);
				if (CollectionUtils.isEmpty(userList)||limit>=60000) {
					break;
				} else {
					for (Map<String, Object> map : userList) {
						String mobile = (String) map.get("mobile");
						//更新归属地
						updateNoCityMobile(mobile);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateCityMobileThread4(){
		try {
			logger.info("刷新用户归属地信息4-start");
			Integer limit = 60000;
			Integer pNo = 20;
			while (true) {
				List<Map<String, Object>> userList = memberService.getNoCityMobile(limit, pNo);
				if (CollectionUtils.isEmpty(userList)||limit>=80000) {
					break;
				} else {
					for (Map<String, Object> map : userList) {
						String mobile = (String) map.get("mobile");
						//更新归属地
						updateNoCityMobile(mobile);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateCityMobileThread5(){
		try {
			logger.info("刷新用户归属地信息5-start");
			Integer limit = 80000;
			Integer pNo = 20;
			while (true) {
				List<Map<String, Object>> userList = memberService.getNoCityMobile(limit, pNo);
				if (CollectionUtils.isEmpty(userList)) {
					break;
				} else {
					for (Map<String, Object> map : userList) {
						String mobile = (String) map.get("mobile");
						//更新归属地
						updateNoCityMobile(mobile);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新临时表手机号码归属地
	 * @Description:
	 *
	 * @param mobile
	 * @throws Exception 
	 */
	private void updateNoCityMobile(String mobile) throws Exception {
		logger.info("手机号码：" + mobile);
		// 获取手机归属地信息
		MobileAttributionDto mad = MobileUtil.getAddressByMobile(mobile);
		if (mad != null) {
			// 城市名称
			String cityName = mad.getCity();
			if (StringUtils.isNotBlank(cityName)) {
				cityName = cityName + "市";
				Map<String, Object> pMap = new HashMap<String, Object>();
				pMap.put("cityName", cityName);
				pMap.put("mobile", mobile);
				// 更新归属地
				memberService.updateCityMobile(pMap);
			}
		}

	}
	/**
	 * 查询用户账务接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/getAccountingStat",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getAccountingStat(HttpServletRequest request){
		long startTime = System.currentTimeMillis();
		try{
			logger.info("查询支付密码设定状态接口-start");
			String userIdStr =  RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			String startTimeStr =  RequestUtils.getQueryParam(request, "startTime");
			String endTimeStr =  RequestUtils.getQueryParam(request, "endTime");
			if (!StringUtils.isEmpty(startTimeStr)) {
				CommonValidUtil.validDateTimeFormat(startTimeStr,DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID, "起始时间格式有误");
			}
			if (!StringUtils.isEmpty(endTimeStr)) {
				CommonValidUtil.validDateTimeFormat(endTimeStr,DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID, "截止时间格式有误");
			}
			//如果用户不是服务人员，则不返回服务店铺奖励
			//如果用户不是区域代理，则不返回代理奖励
			Map<String, Object> resultMap = memberService.getAccountingStat(userId, startTimeStr, endTimeStr);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询账务成功", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询支付密码设定状态接口-系统异常",e);
			throw new APISystemException("查询支付密码设定状态接口", e);
		}finally{
			logger.info("查询用户账单，共耗时："+(System.currentTimeMillis() - startTime)+"毫秒");
		}
	}
	
	/**
	 * U41：查询用户奖励总额接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/rewardsum", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String rewardsum(HttpServletRequest request) {
		try {
			logger.info("查询用户奖励总额接口-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			
			
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "userId数据格式非法");
			
			
			Map<String, Object> resultMap = memberService.rewardsum(userId);
			
			logger.info("查询用户奖励总额接口-end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取用户累计奖励金额,奖励余额和推荐会员数成功！", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询用户奖励总额接口-系统异常", e);
			throw new APISystemException("查询用户奖励总额接口-系统异常", e);
		}
	}
	
	/**
	 * U42：获取用户奖励列表接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/allrewards", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String allrewards(HttpServletRequest request) throws Exception{
			logger.info("获取用户奖励列表接口-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String rewardType = RequestUtils.getQueryParam(request, "rewardType"); //奖励类型
			
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			Map<String, Object> map = new HashMap<String, Object>();
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
			Long  userId= CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "userId数据格式错误");
			
			//CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL,"startTime不能为空");
			if(StringUtils.isNotBlank(startTime)){
			    CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
			    map.put("startTime", startTime + " 00:00:00");
			}
			
			//CommonValidUtil.validStrNull(endTime, CodeConst.CODE_PARAMETER_NOT_NULL,"endTime不能为空");
			if(StringUtils.isNotBlank(endTime)){
			    CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
			    map.put("endTime", endTime + " 23:59:59");
			}
			
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			map.put("userId", userId);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			
			if(StringUtils.isEmpty(rewardType) || "0".equals(rewardType)){
				// 不传或传0都查全部奖励
				map.put("rewardType", ArrayUtil.toArray("4,5,6,7,8,9,10,14,16,17,18,19,20,22,26,27", null));
			}else{
				map.put("rewardType", ArrayUtil.toArray(rewardType, null));
			}
			
			
			PageModel pageModel = memberService.allrewards(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			
			logger.info("获取用户奖励列表接口-end");
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取用户奖励列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * U43：我的推荐用户列表接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/getMyRefUserList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMyRefUserList(HttpServletRequest request) {
		try {
			logger.info("我的推荐用户列表接口-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
			Long  userId= CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "userId数据格式错误");
	         // 查询用户是否存在
            UserDto userDB = memberService.getUserByUserId(userId);
            CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST,
                    CodeConst.MSG_MISS_MEMBER);
            
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			
			PageModel pageModel = memberService.getMyRefUserList(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			
			logger.info("我的推荐用户列表接口-end");
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取用户推荐的会员列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("我的推荐用户列表接口-系统异常", e);
			throw new APISystemException("我的推荐用户列表接口-系统异常", e);
		}
	}
	
    /**
     * 获取运营商发展商铺的统计接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/session/user/getOperateShopStat","/service/user/getOperateShopStat","/token/user/getOperateShopStat"},produces="application/json;charset=utf-8")
    @ResponseBody
    public String getOperateShopStat(HttpServletRequest request){
        try {
            logger.info("获取运营商发展商铺的统计-start");
            Map<String, Object> requestMap = checkValidParamOfGetOperateShopStat(request);
            Map<String, Object> resultMap = shopService.getOperateShopStat(requestMap);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取运营商发展商铺的统计成功!", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e){
            logger.error("获取运营商发展商铺的统计接口异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("获取运营商发展商铺的统计接口异常",e);
            throw new APISystemException("获取运营商发展商铺的统计接口异常", e);
        }
    }
    
    private Map<String, Object> checkValidParamOfGetOperateShopStat(HttpServletRequest request) throws Exception {
    	Map<String, Object> requestMap = new HashMap<String, Object>();
    	String userIdStr = RequestUtils.getQueryParam(request, "userId");
        String areaIdStr = RequestUtils.getQueryParam(request, "areaId");
        String shopStatus = RequestUtils.getQueryParam(request, "shopStatus");
		CommonValidUtil.validStrNull(userIdStr,
				CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_REQUIRED_USER_ID);
		
		Long userId = CommonValidUtil.validStrLongFmt(userIdStr,
				CodeConst.CODE_PARAMETER_NOT_VALID,
				"用户ID数据格式错误");
		
		CommonValidUtil.validObjectNull(memberService.getUserByUserId(userId),
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
		requestMap.put("userId", userId);
		
		if (StringUtils.isNotBlank(areaIdStr)) {
			Integer areaId = CommonValidUtil.validStrIntFmt(areaIdStr, 
					CodeConst.CODE_PARAMETER_NOT_VALID, "运营区域Id格式错误");
			requestMap.put("areaId", areaId);
		}
		if (StringUtils.isNotBlank(shopStatus)) {
			requestMap.put("shopStatus", shopStatus.split(","));
		}
    	return requestMap;
    }
    /**
     * PCS32：获取提现校准信息接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value ={"/session/user/getUserAuditInfo", "/service/user/getUserAuditInfo",
            "/token/user/getUserAuditInfo" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopAuditInfo(HttpServletRequest request) throws Exception
    {

            logger.info("PCS32：获取用户提现校准信息接口    -start");
            String withdrawIdStr = RequestUtils.getQueryParam(request, "withdrawId");
            CommonValidUtil.validStrNull(withdrawIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawId不能为空");
            Long withdrawId = NumberUtil.strToLong(withdrawIdStr,"withdrawId");
            Map<String, Object> resultMap = memberService.getUserAuditInfo(withdrawId);
            logger.info("PCS32：获取用户提现校准信息接口    -end");
            
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取提现校准信息成功！", resultMap, DateUtils.DATETIME_FORMAT);
            
            
    }
    
    /**
     * PU2：获取运营商发展的商铺列表接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/session/user/getOperateShopList","/service/user/getOperateShopList","/token/user/getOperateShopList"},produces="application/json;charset=utf-8")
    @ResponseBody
    public String getOperateShopList(HttpServletRequest request){
        try {
            logger.info("PU2：获取运营商发展的商铺列表接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-start");
            /*
             * token string 条件 设备令牌。Token鉴权方式必填 
             * userId int 是 用户ID 
             * areaId int 否 运营区域Id。只匹配乡镇一级区域。（中山或东莞是市只管镇） 
             * shopStatus string 否商铺状态。多个状态之间以英文逗号分隔。 99:审核中 0:正常 1:下线 2:删除 3:欠费
             * pNo int 1 否 页码
             * pSize int 10 否 每页记录数
             */
            
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String areaIdStr = RequestUtils.getQueryParam(request, "areaId");
            String shopStatusStr = RequestUtils.getQueryParam(request, "shopStatus");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            
            Map<String, Object> pMap = new HashMap<String, Object>();
            
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
            Long userId = NumberUtil.strToLong(userIdStr, "userId");
            
            //分类id
            if(StringUtils.isNotBlank(areaIdStr)){
                Integer areaId = NumberUtil.strToNum(areaIdStr, "areaId");
                pMap.put("areaId", areaId);
            }
            //商铺状态
            if(StringUtils.isNotBlank(shopStatusStr)){
                Integer shopStatus = NumberUtil.strToNum(shopStatusStr, "shopStatus");
                pMap.put("shopStatus", shopStatus);
            }
            
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            
            pMap.put("userId", userId);
            pMap.put("limit", (pNo - 1) * pSize);
            pMap.put("pSize", pSize);
            
            PageModel pageModel =  shopService.getOperateShopList(pMap);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("pNo", pNo);
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("lst", pageModel.getList());
            
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取我运营的商铺列表成功!", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e){
            logger.error("PU2：获取运营商发展的商铺列表接口异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("PU2：获取运营商发展的商铺列表接口异常",e);
            throw new APISystemException("PU2：获取运营商发展的商铺列表接口异常", e);
        }
    }
    
    /**
     * 验证shopId公共方法
     * 
     * @Function: com.idcq.appserver.controller.session.SessionController.validShopId
     * @Description:
     *
     * @param shopIdStr
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月23日 下午8:40:06
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年12月23日    ChenYongxin      v1.0.0         create
     */
    public Long validShopId(String shopIdStr) throws Exception{
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
                CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        //校验店铺是否存在
        ShopDto shopDto  = shopService.getShopById(shopId);
        if (shopDto == null) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOP);
        }
        return shopId;
    }
    
    
    /**
     * MS31：获取用户信息接口
     * @param request
     * @return
     */
    @RequestMapping(value="/user/getUserInfo",produces="application/json;charset=utf-8")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request){
        try {
            logger.info("获取用户信息接口-start");
            
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            
            
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
            Long userId = NumberUtil.strToLong(userIdStr, "userId");
            Map<String, Object> resultMap=shopService.getUserInfoByUserId(userId);
            
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取用户信息成功!", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e){
            logger.error("MS31：获取用户信息接口异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("MS31：获取用户信息接口异常",e);
            throw new APISystemException("：获取用户信息接口异常", e);
        }
    }
    
   
}
