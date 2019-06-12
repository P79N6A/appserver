package com.idcq.appserver.service.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.common.ISendSmsRecordDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.HttpClientUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.ReflectionUtils;
import com.idcq.appserver.utils.SmsVeriCodeUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.HttpSender;
import com.idcq.appserver.utils.smsutil.IngoreField;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.wxscan.MD5Util;
/**
 * 短信相关业务
 * @author nie_jq
 * @datetime 2015-09-07 11:24
 *
 */
@Service
public class SendSmsServiceImpl implements ISendSmsService {

	private static Log logger = LogFactory.getLog(SendSmsServiceImpl.class);
	@Autowired
	private IUserDao userDao;
	@Autowired
	private ISendSmsRecordDao sendSmsRecordDao;
	@Autowired
	private ICommonDao commonDao;
	
	public String getSmsChannel(boolean isFirst) throws Exception {
		logger.info("===================>>获取短信发送通道-start,isFirst="+isFirst);
		String value = "";
		//从内存中获取短信发送的通道
		Properties properties = ContextInitListener.COMMON_PROPS;
		if (null != properties) {
			String tmpValue = properties.getProperty(CommonConst.KEY_SMS_CHANNEL);
			if(!StringUtils.isBlank(tmpValue)){
				String[] keys = tmpValue.split(",");
				if (keys.length > 1) {
					if (isFirst) {
						value = keys[0];
					}else{
						value = keys[1];
					}
				}else{
					value = keys[0];
				}
			}
		}
		if(StringUtils.isBlank(value)){
			value = CommonConst.SMS_MD_CHANNEL_KEY;//默认设置为漫道科技通道
		}
		logger.info("===================>>当前发送短信使用的通道为："+value);
		return value;
	}
	
	/**
	 * 判断当前短信发送场景是否需要校验当前手机号码是否会员
	 * @param MobileService 手机号码
	 * @param usage 场景
	 * @throws Exception
	 */
	private void userExistsFlag(SmsReplaceContent replaceContent) throws Exception {
		boolean flag1 = CommonConst.LIMIT_SMS_CONTENT_KEYS.contains(replaceContent.getUsage());
		boolean flag2 = CommonConst.LIMIT_SMS_CONTENT_KEYS_REG.contains(replaceContent.getUsage());
		boolean flag = false;
		if (flag2 || flag1) {
			flag = queryUserExists(replaceContent);
		}
		if (flag1) {
			if (!flag) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该手机号码未注册");
			}
		}
		if (flag2) {
			if (flag) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该手机号码已注册");
			}
		}
	}
	/**
	 * 根据手机号码查询会员是否存在
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	private boolean queryUserExists(SmsReplaceContent replaceContent) throws Exception{
		int re = userDao.queryNormalUserByMobile(replaceContent.getMobile());
		if (re == 0) {
			return false;
		}
		return true;
	}
	
	public boolean sendSmsMobileCode(SmsReplaceContent replaceContent)
			throws Exception {
		//会员校验
		userExistsFlag(replaceContent);
		if(replaceContent == null){
			replaceContent = new SmsReplaceContent();
		}
		if (replaceContent.isCreateCodeFlag()) {
			//获取发送验证码
			getSendCode(replaceContent);
		}
		//获取短信发送通道
		String channel = getSmsChannel(true);
		//获取短信发送参数
		Map<String, Object> smsParams = getSmsParams(channel, replaceContent);
		Map<String, String> formParams = (Map<String, String>) smsParams.get("formParams");
		String url = (String) smsParams.get(CommonConst.SMS_URL);
		String content = (String) smsParams.get("content");
		   //提交参数至第三方短信平台
        HttpClientUtils httpUtil = HttpClientUtils.getInstance();
        String responseStr = null;
		if(CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
		{
		    String account = (String) smsParams.get(CommonConst.SMS_SN);
		    String pswd  = (String) smsParams.get(CommonConst.SMS_PWD);
		    //调用创蓝接口 
		    responseStr = HttpSender.batchSend(url, account, pswd, replaceContent.getMobile(), content, true, null, null);
		} 
		else 
		{
	        try {
	            responseStr = httpUtil.doPost(url, formParams);
	        } catch (Exception e) {
	            responseStr = "error";
	        }
	       
		}
		 //解析返回结果
        Map resultMap = httpUtil.analyticResult(responseStr,channel);
        int sendStatus = (int) resultMap.get("sendStatus");
		//记录发送的短信内容
		saveSendSmsRecord(replaceContent,content,resultMap,channel);
		boolean resultFlag = httpUtil.isSendSuccess(sendStatus);
		if (resultFlag && replaceContent.isCreateCodeFlag() && replaceContent.isCacheCodeFlag()) {
			//缓存短信验证码
			cacheCode(replaceContent);
		}
		return resultFlag;
	}
	
	public void insertSmsMobileCode(SmsReplaceContent replaceContent)
			throws Exception {
		
		//会员校验
		userExistsFlag(replaceContent);
		if(replaceContent == null){
			replaceContent = new SmsReplaceContent();
		}
		if (replaceContent.isCreateCodeFlag()) {
			//获取发送验证码
			getSendCode(replaceContent);
		}
		//获取短信发送通道
		String channel = getSmsChannel(true);
		//获取短信发送参数
		Map<String, Object> smsParams = getSmsParams(channel, replaceContent);
		String content = (String) smsParams.get("content");
        Map<String, Object> resultMap = new HashMap<String,Object>();
        resultMap.put("sendStatus", replaceContent.getStatus());//待发送
		//记录发送的短信内容
		saveSendSmsRecord(replaceContent,content,resultMap,channel);
		
	}
	
	/**
	 * 缓存验证码
	 * @param MobileService
	 * @param usage
	 * @param replaceContent
	 * @throws Exception
	 */
	private void cacheCode(SmsReplaceContent replaceContent) throws Exception{
		replaceContent.setSendTime(new Date().getTime());
		//如果发送成功，则将短信验证码存入缓存中    
		String mKey = CommonConst.REDIS_VERICODE_OBJ+replaceContent.getMobile();
		// TODO +":"+usage; 后续需要增加场景
		//获取短信验证码缓存有效时间，单位秒
		int cacheTime=getSettingValue(CommonConst.SETTING_CODE_MOBILE_CODE, CommonConst.SETTING_KEY_MOBILE_CODE);
		logger.info("短信验证码缓存有效时间："+cacheTime);
		DataCacheApi.setObjectEx(mKey, replaceContent, cacheTime);
	}
	
	/**
	 * 获取短信发送的验证码
	 * @param replaceContent
	 * @throws Exception
	 */
	private void getSendCode(SmsReplaceContent replaceContent)throws Exception{
		logger.info("=================>>获取短信验证码 start"+replaceContent.toString());
		String mKey = CommonConst.REDIS_VERICODE_OBJ+replaceContent.getMobile();
		Object obj = DataCacheApi.getObject(mKey);
		logger.info("=================>>查看缓存中是否存在验证码"+obj);
		boolean flag = (null == obj);
		if (!flag) {
			SmsReplaceContent cacheContent = (SmsReplaceContent) obj;
			long sendTime = cacheContent.getSendTime();
			long nowTime = new Date().getTime();
			int re = (int) (nowTime - sendTime)/1000;
			logger.info("=================>>时间是否已经过了一分钟");
			if (re < 60) {
				DataCacheApi.delObject(mKey);
				replaceContent.setCode(cacheContent.getCode());
				replaceContent.setUsageFlag("1");
			}else{
				flag = true;
			}
		}
		if (flag) {
			String code = SmsVeriCodeUtil.getIntNum(100000, 899999)+"";
			replaceContent.setCode(code);
		}
		logger.info("=======================>>最后的验证码为："+replaceContent.toString());
	}
	
	/**
	 * 封装短信发送所需参数
	 * @param MobileService
	 * @param channel
	 * @param usage
	 * @param code
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private Map<String, Object> getSmsParams(String channel,SmsReplaceContent replaceContent) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException{
		Map<String, Object> smsParams = new HashMap<String, Object>();
		String tmpKey = channel;
		boolean subFlag = false;
		boolean isAesDecrypt = true; //是否需要解密
		if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel)) 
		{
			tmpKey = tmpKey+ "_";
		}
		else if (CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
		{
		    if(CommonConst.SMS_TYPE_SEO.equals(replaceContent.getSmsType())) {
		        //使用创蓝的营销短信通道
		        tmpKey = tmpKey+ "_sem" + "_";
		    } else {
		        tmpKey = tmpKey+ "_";
		    }
		    isAesDecrypt = false;
            subFlag = true;
		}
		else
		{
			tmpKey = "";//志晴平台默认为空
			subFlag = true;
		}
		String url = getSmsValueByKey(tmpKey+CommonConst.SMS_URL, isAesDecrypt);
		String sn = getSmsValueByKey(tmpKey+CommonConst.SMS_SN, isAesDecrypt);
		String pwd = getSmsValueByKey(tmpKey+CommonConst.SMS_PWD, isAesDecrypt);
		String content = replaceContent.getContent();
		String usage = replaceContent.getUsage()+replaceContent.getUsageFlag();
		if(StringUtils.isBlank(content)) {
			if (CommonConst.MSG_SETTING_KEY_SMS_PWD.equals(replaceContent.getUsage())) {
				content = getSmsContentByKey(replaceContent.getUsage());
			}else{
				/*if (CommonConst.SMS_CONTENT_KEYS.contains(usage)) {
					content = getSmsValueByKey(usage,false);
				}*/
				content = getSmsValueByKey(usage,false);
				if (null == content) {
					content = getSmsValueByKey(CommonConst.SMS_GENERAL,false);
				}
			}
			// 将模板中的code替换为真实的code
			content = doContent(content, replaceContent);
			if(subFlag){
				//需要去掉签名
				content = content.replace(CommonConst.SMS_CONTENT_SIGN, "");
			}
		}
		if (null == url || sn == null || pwd == null || content == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "没有找到短信发送相关参数信息");
		}
		//封装表单参数
		Map<String, String> formParams = encapSmsParams(sn, pwd, content, replaceContent.getMobile(), channel);
		smsParams.put("formParams", formParams);
		smsParams.put(CommonConst.SMS_URL, url);
		smsParams.put("content", content);
		smsParams.put(CommonConst.SMS_SN, sn);
		smsParams.put(CommonConst.SMS_PWD, pwd);
		return smsParams;
	}
	
	/**
	 * 处理发送内容，将内容中的关键字替换为实际的值
	 * @param content
	 * @param replaceContent
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public String doContent(String content,SmsReplaceContent replaceContent) throws IllegalArgumentException, IllegalAccessException{
		if (replaceContent != null) {
			Field[] fields = replaceContent.getClass().getDeclaredFields();
			if(fields != null && fields.length > 0){
				for(Field field:fields){
					IngoreField ingore = field.getAnnotation(IngoreField.class);
					String fieldName = field.getName();
					Object value=ReflectionUtils.getFieldValue(replaceContent, fieldName);//通过反射取值
					if (value != null && ingore == null) {
						content = content.replaceAll(fieldName, value+"");
					}else if("shopManagerName".equals(fieldName) && value == null){
					    content = content.replaceFirst(fieldName, "用户");
					}
				}
			}
		}
		return content;
	}
	
	/**
	 * 保存短信发送记录
	 * @param replaceContent
	 * @param content
	 * @param sendStatus
	 * @param sendChannle
	 * @return
	 */
	private int saveSendSmsRecord(SmsReplaceContent replaceContent,String content, Map resultMap,String sendChannle){
		String usage = replaceContent.getUsage()+replaceContent.getUsageFlag();
		SendSmsRecordDto dto = new SendSmsRecordDto();
		dto.setSendUsage(usage);
		dto.setSendCode(replaceContent.getCode());
		dto.setSendContent(content);
		dto.setSendMobile(replaceContent.getMobile());
		dto.setSendChannle(sendChannle);
		dto.setSendStatus((Integer) resultMap.get("sendStatus"));
		dto.setMsgId(resultMap.get("msgId") + "");
		dto.setSendTime(new Date());
		int re = 0;
		try {
			//记录之前，先将之前的标记为过期
			sendSmsRecordDao.updateSendStatusExpire(dto.getSendMobile());
			re = sendSmsRecordDao.saveSendSmsRecord(dto);
		} catch (Exception e) {
			logger.warn("记录短信异常",e);
		}
		return re;
	}
	
	/**
	 * 封装发送参数
	 * @param sn
	 * @param pwd
	 * @param content
	 * @param mobile
	 * @param channel 使用的通道 md-漫道 zq-志晴
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private Map<String, String> encapSmsParams(String sn,String pwd,String content,String mobile,String channel) throws UnsupportedEncodingException{
		Map<String, String> formParams = new HashMap<String, String>();
		if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel)) {
			formParams.put("mobile", mobile);
			formParams.put("sn", sn);
			//32位大写加密
			formParams.put("pwd", MD5Util.getMD5Str(sn+pwd).toUpperCase());
			formParams.put("content", URLEncoder.encode(content, "utf-8"));
			formParams.put("ext", "");
			formParams.put("stime", "");
			formParams.put("rrid", "");
			formParams.put("msgfmt", "");
		}else{
			formParams.put("mobile", mobile);
			formParams.put("Sn", sn);
			formParams.put("Pwd", pwd);
			formParams.put("content", content);
		}
		return formParams;
	}
	
	/**
	 * 获取短信发送参数
	 * @param key
	 * @param isAesDecrypt
	 * @return
	 */
	public String getSmsValueByKey(String key,boolean isAesDecrypt){
		String configValue = null;
		String redisKey = CommonConst.REDIS_SMS_PARAM+key;
		try {
			configValue = DataCacheApi.get(redisKey);
			if (null == configValue) {
				configValue = commonDao.getSmsValueByKey(key);
				logger.info("======================>>>"+key+"=="+configValue);
				if (null != configValue) {
					if (isAesDecrypt) {
						configValue=AESUtil.aesDecrypt(configValue, AESUtil.key);
					}
					DataCacheApi.setex(redisKey, configValue,CommonConst.CACHE_TIME_SMSPARAM);
				}
			}
		} catch (Exception e) {
			logger.error("获取短信平台参数异常",e);
		}
		return configValue;
	}
	
	/**
	 * 获取短信验证码缓存时间
	 * @param code
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private int getSettingValue(String code, String key) throws Exception {
		int re = 0;
		try {
			String redisKey = CommonConst.REDIS_SMS_PARAM+CommonConst.MOBILE_CODE_TIMEOUT_KEY;
			String redisValue = DataCacheApi.get(redisKey);
			if (!StringUtils.isEmpty(redisValue)) {
				return Integer.valueOf(redisValue+"");
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("settingCode", code);
			param.put("settingKey", key);
			String jsonStr = commonDao.getSettingValue(param);
			Object settingValue = CommonValidUtil.convertJsonStr(jsonStr, CommonConst.MOBILE_CODE_TIMEOUT_KEY);
			if (CommonValidUtil.isEmpty(settingValue)) {
				re = CommonConst.CACHE_TIME_CODE;//默认缓存时间，30分钟
			}else{
				re = Integer.valueOf(settingValue+"")*60;
				DataCacheApi.setex(redisKey, re+"", CommonConst.CACHE_TIME_SMSPARAM);
			}
		} catch (Exception e) {
			re = CommonConst.CACHE_TIME_CODE;//默认缓存时间，30分钟
			logger.warn("获取短信有效时间异常，赋予默认值：30分钟",e);
		}
		return re;
	}
	
	/**
	 * php后台发送经销商、代理商账号密码，查询短信内容模板key
	 * @param key
	 * @return
	 */
	private String getSmsContentByKey(String key){
		String content = null;
		try {
			Map contMap = commonDao.getMsgSettingContent(key);
			if (null != contMap && contMap.size() > 0) {
				Integer remandFlag = CommonValidUtil.isEmpty(contMap.get("remand_flag"))?null:Integer.parseInt(contMap.get("remand_flag")+"");
				if (null != remandFlag && remandFlag.intValue() != 1) {
					throw new ValidateException(CodeConst.CODE_PHP_REMAND_FLAG_CLOSE, "提醒已经关闭");
				}
				content = CommonValidUtil.isEmpty(contMap.get("msg_content"))?null:(contMap.get("msg_content")+"");
			}
		} catch (Exception e) {
			logger.error("获取短信平台参数异常",e);
		}
		return content;
	}
	
	
	public  boolean sendSms(String mobile,String content)
	{
		try{
			//获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
			String channel = getSmsChannel(true);
			String tmpKey = channel;
			boolean isAesDecrypt = true; //是否需要解密
	        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel)) 
	        {
	            tmpKey = tmpKey+ "_";
	        }
	        else if (CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
	        {
	            tmpKey = tmpKey+ "_";
	            isAesDecrypt = false;
	        }
	        else
	        {
	            tmpKey = "";//志晴平台默认为空
	        }
			String url = getSmsValueByKey(tmpKey+CommonConst.SMS_URL,isAesDecrypt);
			String sn = getSmsValueByKey(tmpKey+CommonConst.SMS_SN,isAesDecrypt);
			String pwd = getSmsValueByKey(tmpKey+CommonConst.SMS_PWD,isAesDecrypt);
			Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
			HttpClientUtils httpUtil = HttpClientUtils.getInstance();
			String responseStr = httpUtil.doPost(url, formParams);
			Map map = httpUtil.analyticResult(responseStr,channel);
			int sendStatus = (int) map.get("sendStatus");
			return httpUtil.isSendSuccess(sendStatus);
		}catch(Exception e)
		{
			logger.error("发送短信过程中产生了异常",e);
			return false;
		}
	}
	public  boolean sendSmsByContent(String mobile,String content,String usage)
	{
		try{
			//获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
			String channel = getSmsChannel(true);
			String tmpKey = channel;
			if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel) || CommonConst.SMS_CL_CHANNEL_KEY.equals(channel)) {
				tmpKey = tmpKey+ "_";
			}
			String url = getSmsValueByKey(tmpKey+CommonConst.SMS_URL,true);
			String sn = getSmsValueByKey(tmpKey+CommonConst.SMS_SN,true);
			String pwd = getSmsValueByKey(tmpKey+CommonConst.SMS_PWD,true);
			Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
			HttpClientUtils httpUtil = HttpClientUtils.getInstance();
			String responseStr = httpUtil.doPost(url, formParams);
			Map map = httpUtil.analyticResult(responseStr,channel);
			int sendStatus = (int) map.get("sendStatus");
			
			return httpUtil.isSendSuccess(sendStatus);
		}catch(Exception e)
		{
			logger.error("发送短信过程中产生了异常",e);
			return false;
		}
	}

	public boolean checkSmsCodeIsOk(String mobile, String usage, String code) throws Exception {
		return checkSmsCodeIsOk(mobile, usage, code, true);
	}
	
	public boolean checkSmsCodeIsOk(String mobile, String usage, String code, boolean delFalg) throws Exception {
		String key = CommonConst.REDIS_VERICODE_OBJ +mobile;
		boolean switchOn = DataCacheApi.switchOn();
		logger.info("redis开关状态："+switchOn);
		String cacheCode = getCacheCode(key, mobile, usage, switchOn);
		logger.info("请求验证码："+code+"，缓存验证码："+cacheCode);
		if(StringUtils.isBlank(cacheCode)){
			return false;
		}
		logger.info("请求验证码长度："+code.length()+"，缓存验证码长度："+cacheCode.length());
		boolean re = StringUtils.equals(code, cacheCode);
		if (re) {
			//认证成功后，将记录标记为过期
			sendSmsRecordDao.updateSendStatusExpire(mobile);
			if(switchOn && delFalg){
				//校验成功，且缓存开关为打开状态，将验证码失效
				DataCacheApi.delObject(key);
			}
		}
		return re;
	}
	
	private String getCacheCode(String key,String mobile, String usage,boolean switchOn) throws Exception{
		String cacheCode = null;
		if (switchOn) {
			//如果缓存开关为打开状态，则从缓存获取验证码比较
			Object obj = DataCacheApi.getObject(key);
			if(null == obj)return null;
			SmsReplaceContent content = (SmsReplaceContent) obj;
			cacheCode = content.getCode();
			logger.info("redis中获取的验证码："+cacheCode);
		}else{
			//否则从数据库中获取
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mobile", mobile);
			param.put("usage", usage);
			logger.info("查询验证码参数："+param);
			//cacheCode = sendSmsRecordDao.getSendCodeByMobileAndUsage(param);
			SendSmsRecordDto ssrDto = sendSmsRecordDao.getSendSmsRecordDtoByMobileAndUsage(param);
			logger.info("数据库中验证码信息："+ssrDto);
			if(null != ssrDto){
				cacheCode = ssrDto.getSendCode();
				if(!StringUtils.isEmpty(cacheCode)){
					Date sendTime = ssrDto.getSendTime();
					if(null != sendTime){
						long nowTime = new Date().getTime();
						//获取验证码有效时间
						int cacheTime=getSettingValue(CommonConst.SETTING_CODE_MOBILE_CODE, CommonConst.SETTING_KEY_MOBILE_CODE);
						logger.info("短信验证码缓存有效时间："+cacheTime);
						if((nowTime-sendTime.getTime())> cacheTime*1000){
							cacheCode = null;
						}
					}
				}
			}
			logger.info("数据库中获取的验证码："+cacheCode);
		}
		return cacheCode;
	}
	
	public boolean checkSmsCodeIsOkByCashCoupon(String mobile,String usage,String code)throws Exception {
		//消费金短信支付，验证码不用加密
		String key = CommonConst.REDIS_VERICODE_OBJ +mobile;
		boolean switchOn = DataCacheApi.switchOn();
		logger.info("redis开关状态："+switchOn);
		String cacheCode = getCacheCode(key, mobile, usage, switchOn);
		if(StringUtils.isBlank(cacheCode)){
			return false;
		}
		//加密
		cacheCode = MD5Util.getMD5Str(code);
		boolean re = StringUtils.equals(cacheCode, code);
		if (re) {
			//认证成功后，将记录标记为过期
			sendSmsRecordDao.updateSendStatusExpire(mobile);
			DataCacheApi.delObject(key);//校验成功，清除缓存验证码
		}
		return re;
	}

	@Override
	@Deprecated
	public boolean getSmsCodeIsOk(String mobile, String usage, String code)
			throws Exception {
		return getSmsCodeIsOk(mobile, usage, code,true);
	}

	@Override
	@Deprecated
	public boolean getSmsCodeIsOk(String mobile, String usage, String code,
			boolean delFalg) throws Exception {
		String key = CommonConst.REDIS_VERICODE_OBJ +mobile;
		boolean switchOn = DataCacheApi.switchOn();
		logger.info("redis开关状态："+switchOn);
		String cacheCode = getCacheCode(key, mobile, usage, switchOn);
		logger.info("请求验证码："+code+"，缓存验证码："+cacheCode);
		if(StringUtils.isBlank(cacheCode)){
			return false;
		}
		logger.info("请求验证码长度："+code.length()+"，缓存验证码长度："+cacheCode.length());
		boolean re = StringUtils.equals(code, cacheCode);
		if (re && switchOn && delFalg) {
			//校验成功，且缓存开关为打开状态，将验证码失效
			DataCacheApi.delObject(key);
		}
		return re;
	}

	@Override
	@Deprecated
	public boolean getSmsCodeIsOkByCashCoupon(String mobile, String usage,
			String code) throws Exception {
		//消费金短信支付，验证码不用加密
		String key = CommonConst.REDIS_VERICODE_OBJ +mobile;
		boolean switchOn = DataCacheApi.switchOn();
		logger.info("redis开关状态："+switchOn);
		String cacheCode = getCacheCode(key, mobile, usage, switchOn);
		if(StringUtils.isBlank(cacheCode)){
			return false;
		}
		//加密
		cacheCode = MD5Util.getMD5Str(code);
		boolean re = StringUtils.equals(cacheCode, code);
		if (re) {
			DataCacheApi.delObject(key);//校验成功，清除缓存验证码
		}
		return re;
	}
	
    public boolean checkSmsAttack(String mobile, String usage) throws Exception
    {
        Properties properties = ContextInitListener.COMMON_PROPS;
        String flag = "false";
        if (null != properties)
        {
            flag = properties.getProperty("sms_binding_flag");
            if ("true".equals(flag))
            {
                String countStr = properties.getProperty("sms_binding_count");
                String hourStr = properties.getProperty("sms_binding_hours");
                int hour = NumberUtil.stringToInteger(hourStr);
                int smsCount = NumberUtil.stringToInteger(countStr);
                int count = sendSmsRecordDao.getSmsCountBy(mobile, usage, hour);
                if (count >= smsCount)
                {
                    return true;
                }
            }
        }
        return false;
    }

	@Override public void sendAll3721RemainedMsg()
	{
		Date curTime = new Date();
		SendSmsRecordDto searchCondition = new SendSmsRecordDto();
		//未发送的消息
		searchCondition.setSendStatus(5);
		//搜索一天之内的消息
		Date timeCondition = curTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timeCondition);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		timeCondition = calendar.getTime();
		searchCondition.setCreateTime(timeCondition);
		RowBounds tempRowBounds = null;
		int pSize = 100;
		int jobNum = sendSmsRecordDao.countRemainMsg(searchCondition);
		int start = 0;
		List<SendSmsRecordDto > tempToSend = null;
		Iterator<SendSmsRecordDto> iterator = null;
		//数据库里查出的未发送消息
		SendSmsRecordDto tempDto = null;
		while (start < jobNum){
			tempRowBounds = new RowBounds(start, pSize);
			tempToSend = sendSmsRecordDao.getRemainMsg(searchCondition, tempRowBounds);
			iterator = tempToSend.iterator();
			while(iterator.hasNext()){
				try
				{
					tempDto = iterator.next();
					SmsReplaceContent smsReplaceContent = new SmsReplaceContent();
					smsReplaceContent.setContent(tempDto.getSendContent());
					smsReplaceContent.setMobile(tempDto.getSendMobile());
					smsReplaceContent.setUsage(tempDto.getSendUsage());
					sendSmsMobileCode(smsReplaceContent);
					//在这里发送
					tempDto.setSendStatus(0);
					tempDto.setSendTime(curTime);
					sendSmsRecordDao.updateStatusById(tempDto);
				}
				catch (Exception e)
				{
					logger.info(e.getMessage(), e);
				}
			}
			start = start + pSize;
		}
	}
}
