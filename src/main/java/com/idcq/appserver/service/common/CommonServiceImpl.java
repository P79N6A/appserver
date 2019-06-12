package com.idcq.appserver.service.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.dao.activity.IBusinessAreaStatDao;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.common.ICodeDao;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.common.IConfigDao;
import com.idcq.appserver.dao.common.ISendSmsRecordDao;
import com.idcq.appserver.dao.common.ISysConfigureDao;
import com.idcq.appserver.dao.common.IUserPermissionDao;
import com.idcq.appserver.dao.message.IMessageCenterDao;
import com.idcq.appserver.dao.message.IPushUserMsgDao;
import com.idcq.appserver.dao.rebates.IAgentRebatesDao;
import com.idcq.appserver.dao.rebates.IShopRebatesDao;
import com.idcq.appserver.dao.shop.IHeartbeatLogDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dao.user.IBranchOfficeDao;
import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.activity.BusinessAreaStatDto;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.CodeDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.common.PageResult;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.dto.common.SmsDto;
import com.idcq.appserver.dto.common.SysConfigureDto;
import com.idcq.appserver.dto.common.UserPermission;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.message.PushUserMsgDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.rebates.AgentRebatesDto;
import com.idcq.appserver.dto.rebates.ShopRebatesDto;
import com.idcq.appserver.dto.shop.HeartbeatLogDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.BranchOfficeDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.bill.IPlatformBillService;
import com.idcq.appserver.service.bill.IUserBillService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IMessageSettingService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.settle.ISettleService;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.HttpClientUtil;
import com.idcq.appserver.utils.HttpClientUtils;
import com.idcq.appserver.utils.Jpush;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.PropertyUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.wxscan.MD5Util;

@Service
public class CommonServiceImpl implements ICommonService
{
	private final static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    IShopDeviceDao shopDeviceDao;

    @Autowired
    IPushUserTableDao pushUserTableDao;

    @Autowired
    IPushUserMsgDao pushUserMsgDao;

    @Autowired
    private ICommonDao commonDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IMessageSettingService messageSettingService;

    @Autowired
    private ISendSmsRecordDao sendSmsRecordDao;
    @Autowired
    private IShopBillDao shopBillDao;

    @Autowired
    private ISysConfigureDao sysConfigureDao;
    @Autowired
    private IPlatformBillDao platformBillDao;
    
    @Autowired
    private IUserAccountDao userAccountDao;
    
    @Autowired
    private IUserBillDao userBillDao;
    
    @Autowired
    private IShopAccountDao shopAccountDao;


    @Autowired
    private IShopDao shopDao;

    @Autowired
    private IHeartbeatLogDao heartbeatLogDao;

    @Autowired
    private ICodeDao codeDao;

    @Autowired
    private IPushService pushServiceImpl;
    
    @Autowired
    private IAgentDao agentDao;

    @Autowired
    private IMessageCenterDao messageCenterDao;

    @Autowired
    private IBusinessAreaStatDao businessAreaStatDao;

    @Autowired
    private IConfigDao configDao;
    
    @Autowired
    private IUserPermissionDao userPermissionDao;
    
    @Autowired
    private IMemberServcie memberServcie;
    
    @Autowired
    private IUserBillService userBillService;
    
    @Autowired
    private IShopMemberDao shopMemberDao;
    @Autowired
    private IShopRebatesDao shopRebatesDao;
    @Autowired
    private IAgentRebatesDao agentRebatesDao;    
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private ISettleService settleService;
    
    @Autowired
    private IPlatformBillService platformBillService;
    
    @Autowired 
    private IBranchOfficeDao branchOfficeDao;

    /*
     * @Autowired private ICommonService commonService;
     */

    public List<Map> querySmsParam(Long appId) throws Exception
    {
        return shopDeviceDao.querySmsParam(appId);
    }

    public void pushUserMsg(JSONObject jsonObject, Long userId, Integer userType) throws Exception
    {
        if (null != userId && null != jsonObject)
        {
            List<PushUserTableDto> userPushTables = pushUserTableDao.getPushUserByUserId(userId, userType);
            if (CollectionUtils.isNotEmpty(userPushTables))
            {
                List<PushUserMsgDto> userMsgDtos = new ArrayList<PushUserMsgDto>();
                String action = jsonObject.getString("action");
                // 查询开关,返回1为打开，其他抛出异常
                MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(action);
                if (null != messageSettingDto && 1 == messageSettingDto.getRemandFlag())
                {
                    String content = jsonObject.toString();
                    Date sendTime = DateUtils.getCurrentDate(null);
                    PushUserMsgDto msgDto = null;
                    for (PushUserTableDto dto : userPushTables)
                    {
                        Jpush.sendPushToTarget(dto.getOsInfo(), dto.getUserType(), dto.getRegId(),
                                messageSettingDto.getMsgTitle(), content);
                        msgDto = new PushUserMsgDto();
                        msgDto.setAction(action);
                        msgDto.setSendTime(sendTime);
                        msgDto.setRegId(dto.getRegId());
                        msgDto.setUserId(userId);
                        msgDto.setMessageContent(content);
                        msgDto.setMessageStatus(0);
                        userMsgDtos.add(msgDto);
                    }
                    if (CollectionUtils.isNotEmpty(userMsgDtos))
                    {
                        pushUserMsgDao.batchInsertSelective(userMsgDtos);
                    }
                }
                else
                {
                    logger.warn("----消息发送配置已关闭----" + action);
                }
            }
        }
    }

    @Override
    public void pushUserMsg(String action,Long bizId, Integer bizType,SmsReplaceContent pushReplaceContent) throws Exception {
    	if (action == null && bizId == null && bizType == null && pushReplaceContent == null) {
    		logger.info("推送信息不全，停止推送   action:{} bizId{} bizType:{} pushReplaceContent:{}",action,bizId,bizType,pushReplaceContent);
    		return;
    	}
    	
    	try {
    		
		 	 List<PushUserTableDto> userPushTables = pushUserTableDao.getPushUserByUserId(bizId, bizType);
    	
	       	 if (CollectionUtils.isNotEmpty(userPushTables))
	            {
	                List<PushUserMsgDto> userMsgDtos = new ArrayList<PushUserMsgDto>();
	                // 查询开关,返回1为打开，其他抛出异常
	                MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(action);
	                if (null != messageSettingDto && 1 == messageSettingDto.getRemandFlag())
	                {
	                    Date sendTime = DateUtils.getCurrentDate(null);
	                    PushUserMsgDto msgDto = null;
	                	String contentModel = messageSettingDto.getMsgContent();
                    	if (StringUtils.isBlank(contentModel)) {
                    		logger.info("action:{} 消息配置表中模板缺失 停止推送",action);
                    		return;
                    	}
                    	String pushContent = sendSmsService.doContent(contentModel, pushReplaceContent);
                    	
	                    for (PushUserTableDto dto : userPushTables)
	                    {
	                        Jpush.sendPushToTarget(dto.getOsInfo(), dto.getUserType(), dto.getRegId(),
	                                messageSettingDto.getMsgTitle(), pushContent);
	                        msgDto = new PushUserMsgDto();
	                        msgDto.setAction(action);
	                        msgDto.setSendTime(sendTime);
	                        msgDto.setRegId(dto.getRegId());
	                        msgDto.setUserId(bizId);
	                        msgDto.setMessageContent(pushContent);
	                        msgDto.setMessageStatus(0);
                            msgDto.setCreateTime(new Date());
                            msgDto.setOsInfo(dto.getOsInfo());
                            msgDto.setUserType(dto.getUserType());
	                        userMsgDtos.add(msgDto);
	                    }
	                    if (CollectionUtils.isNotEmpty(userMsgDtos))
	                    {
	                        pushUserMsgDao.batchInsertSelective(userMsgDtos);
	                    }
	                }
	            }
		} catch (Exception e) {
			logger.error("推送异常：   action:{} bizId{} bizType:{} pushReplaceContent:{}",action,bizId,bizType,pushReplaceContent);
			logger.error("异常信息", e);
		}
    	
    }
    
    public void insertUserMsg(String action,Long bizId, Integer bizType,SmsReplaceContent pushReplaceContent) throws Exception {
    	if (action == null && bizId == null && bizType == null && pushReplaceContent == null) {
    		logger.info("推送信息不全，停止推送   action:{} bizId{} bizType:{} pushReplaceContent:{}",action,bizId,bizType,pushReplaceContent);
    		return;
    	}
    	
    	try {
    		
		 	 List<PushUserTableDto> userPushTables = pushUserTableDao.getPushUserByUserId(bizId, bizType);
    	
	       	 if (CollectionUtils.isNotEmpty(userPushTables))
	            {
	                List<PushUserMsgDto> userMsgDtos = new ArrayList<PushUserMsgDto>();
	                // 查询开关,返回1为打开，其他抛出异常
	                MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(action);
	                if (null != messageSettingDto && 1 == messageSettingDto.getRemandFlag())
	                {
	                    Date sendTime = DateUtils.getCurrentDate(null);
	                    PushUserMsgDto msgDto = null;
	                	String contentModel = messageSettingDto.getMsgContent();
                    	if (StringUtils.isBlank(contentModel)) {
                    		logger.info("action:{} 消息配置表中模板缺失 停止推送",action);
                    		return;
                    	}
                    	String pushContent = sendSmsService.doContent(contentModel, pushReplaceContent);
                    	
	                    for (PushUserTableDto dto : userPushTables)
	                    {
	                        msgDto = new PushUserMsgDto();
	                        msgDto.setAction(action);
	                        msgDto.setSendTime(sendTime);
	                        msgDto.setRegId(dto.getRegId());
	                        msgDto.setUserId(bizId);
	                        msgDto.setMessageContent(pushContent);
	                        msgDto.setMessageStatus(2);
                            msgDto.setCreateTime(new Date());
                            msgDto.setOsInfo(dto.getOsInfo());
                            msgDto.setUserType(dto.getUserType());
	                        userMsgDtos.add(msgDto);
	                    }
	                    if (CollectionUtils.isNotEmpty(userMsgDtos))
	                    {
	                        pushUserMsgDao.batchInsertSelective(userMsgDtos);
	                    }
	                }
	            }
		} catch (Exception e) {
			logger.error("记录推送信息异常：   action:{} bizId{} bizType:{} pushReplaceContent:{}",action,bizId,bizType,pushReplaceContent);
			logger.error("记录推送信息异常信息", e);
		}
    	
    }
   
    public void pushUserMsg(String action, String content, Long userId, Integer userType) throws Exception
    {
        if (null != userId && null != content)
        {
            logger.info("提醒推送第2步2");
            List<PushUserTableDto> userPushTables = pushUserTableDao.getPushUserByUserId(userId, userType);
            if (CollectionUtils.isNotEmpty(userPushTables))
            {
                logger.error("提醒推送第3步3");
                List<PushUserMsgDto> userMsgDtos = new ArrayList<PushUserMsgDto>();
                // 查询开关,返回1为打开，其他抛出异常
                MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(action);
                if (null != messageSettingDto && 1 == messageSettingDto.getRemandFlag())
                {
                    logger.error("提醒推送第4步4");
                    Date sendTime = DateUtils.getCurrentDate(null);
                    PushUserMsgDto msgDto = null;
                    for (PushUserTableDto dto : userPushTables)
                    {
                        Jpush.sendPushToTarget(dto.getOsInfo(), dto.getUserType(), dto.getRegId(),
                                messageSettingDto.getMsgTitle(), content);
                        msgDto = new PushUserMsgDto();
                        msgDto.setAction(action);
                        msgDto.setSendTime(sendTime);
                        msgDto.setRegId(dto.getRegId());
                        msgDto.setUserId(userId);
                        msgDto.setMessageContent(content);
                        msgDto.setMessageStatus(0);
                        userMsgDtos.add(msgDto);
                    }
                    if (CollectionUtils.isNotEmpty(userMsgDtos))
                    {
                        pushUserMsgDao.batchInsertSelective(userMsgDtos);
                    }
                }
            }
        }
    }

    public int getSettingValue(String code, String key) throws Exception
    {
        int re = 0;
        try
        {
            String redisKey = CommonConst.REDIS_SMS_PARAM + CommonConst.MOBILE_CODE_TIMEOUT_KEY;
            String redisValue = DataCacheApi.get(redisKey);
            if (!StringUtils.isEmpty(redisValue))
            {
                return Integer.valueOf(redisValue + "");
            }
            Map<String, String> param = new HashMap<String, String>();
            param.put("settingCode", code);
            param.put("settingKey", key);
            String jsonStr = commonDao.getSettingValue(param);
            Object settingValue = CommonValidUtil.convertJsonStr(jsonStr, CommonConst.MOBILE_CODE_TIMEOUT_KEY);
            if (CommonValidUtil.isEmpty(settingValue))
            {
                re = CommonConst.CACHE_TIME_CODE;// 默认缓存时间，30分钟
            }
            else
            {
                re = Integer.valueOf(settingValue + "") * 60;
                DataCacheApi.setex(redisKey, re + "", CommonConst.CACHE_TIME_SMSPARAM);
            }
        }
        catch (Exception e)
        {
            re = CommonConst.CACHE_TIME_CODE;// 默认缓存时间，30分钟
            logger.warn("获取短信有效时间异常，赋予默认值：30分钟", e);
        }
        return re;
    }

    public String getSmsValueByKey(String key, boolean isAesDecrypt)
    {
        String configValue = null;
        String redisKey = CommonConst.REDIS_SMS_PARAM + key;
        try
        {
            configValue = DataCacheApi.get(redisKey);
            if (null == configValue)
            {
                configValue = commonDao.getSmsValueByKey(key);
                logger.info("======================>>>" + key + "==" + configValue);
                if (null != configValue)
                {
                    if (isAesDecrypt)
                    {
                        configValue = AESUtil.aesDecrypt(configValue, AESUtil.key);
                    }
                    DataCacheApi.setex(redisKey, configValue, CommonConst.CACHE_TIME_SMSPARAM);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("获取短信平台参数异常", e);
        }
        return configValue;
    }

    @Deprecated
    public boolean sendSmsMobileCode_bak(String mobile, String code, String usage) throws Exception
    {
        String url = getSmsValueByKey(CommonConst.SMS_URL, true);
        String sn = getSmsValueByKey(CommonConst.SMS_SN, true);
        String pwd = getSmsValueByKey(CommonConst.SMS_PWD, true);
        String content = getSmsValueByKey(usage, false);
        if (null == content)
        {
            content = getSmsValueByKey(CommonConst.SMS_GENERAL, false);
        }
        if (null == url || sn == null || pwd == null || content == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "没有找到短信发送相关参数信息");
        }
        // 将模板中的code替换为真实的code
        content = content.replaceFirst("code", code);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("Sn", sn));
        params.add(new BasicNameValuePair("Pwd", sn));
        params.add(new BasicNameValuePair("content", content));
        String xmlStr = HttpClientUtil.sendPost(url, params);
        return HttpClientUtil.analyticResult(xmlStr);
    }

    public boolean sendSmsMobileCode(String mobile, String code, String usage) throws Exception
    {
        userExistsFlag(mobile, usage);
        // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
        String channel = getSmsChannel();
        String tmpKey = channel;
        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel) || CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
        {
            tmpKey = tmpKey + "_";
        }
        String url = getSmsValueByKey(tmpKey + CommonConst.SMS_URL, true);
        String sn = getSmsValueByKey(tmpKey + CommonConst.SMS_SN, true);
        String pwd = getSmsValueByKey(tmpKey + CommonConst.SMS_PWD, true);
        String content = null;
        if (CommonConst.SMS_CONTENT_KEYS.contains(usage))
        {
            content = getSmsValueByKey(usage, false);
        }
        if (null == content)
        {
            content = getSmsValueByKey(CommonConst.SMS_GENERAL, false);
        }
        if (null == url || sn == null || pwd == null || content == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "没有找到短信发送相关参数信息");
        }
        // 将模板中的code替换为真实的code
        content = content.replaceFirst(CommonConst.SMS_CODE, code);
        // 封装表单参数
        Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
        HttpClientUtils httpUtil = HttpClientUtils.getInstance();
        String responseStr = null;
        try
        {
            responseStr = httpUtil.doPost(url, formParams);
        }
        catch (Exception e)
        {
            responseStr = "error";
        }
        // 不同的通道，解析返回结果不一样
        Map map = httpUtil.analyticResult(responseStr, channel);
        int sendStatus = (int) map.get("sendStatus");
        // 记录发送的短信内容
        SendSmsRecordDto dto = new SendSmsRecordDto();
        dto.setSendUsage(usage);
        dto.setSendContent(content);
        dto.setSendMobile(mobile);
        dto.setSendTime(new Date());
        dto.setSendStatus(sendStatus);
        saveSendSmsRecord(dto);
        return httpUtil.isSendSuccess(sendStatus);
    }

    public boolean sendSmsMobileCode(String mobile, Double acountAmount, Double amount, String shopName, String code,
            String usage) throws Exception
    {
        userExistsFlag(mobile, usage);
        // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
        String channel = getSmsChannel();
        String tmpKey = channel;
        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel) || CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
        {
            tmpKey = tmpKey + "_";
        }
        String url = getSmsValueByKey(tmpKey + CommonConst.SMS_URL, true);
        String sn = getSmsValueByKey(tmpKey + CommonConst.SMS_SN, true);
        String pwd = getSmsValueByKey(tmpKey + CommonConst.SMS_PWD, true);
        String content = null;
        if (CommonConst.SMS_CONTENT_KEYS.contains(usage))
        {
            content = getSmsValueByKey(usage, false);
        }
        if (null == content)
        {
            content = getSmsValueByKey(CommonConst.SMS_GENERAL, false);
        }
        if (null == url || sn == null || pwd == null || content == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "没有找到短信发送相关参数信息");
        }
        // 将模板中的code替换为真实的code

        content = content.replaceFirst(CommonConst.SMS_ACOUNT_AMOUNT, acountAmount + "");
        content = content.replaceFirst(CommonConst.SMS_AMOUNT, amount + "");
        content = content.replaceFirst(CommonConst.SMS_SHOP_NAME, shopName);
        content = content.replaceFirst(CommonConst.SMS_CODE, code);
        // 封装表单参数
        Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
        HttpClientUtils httpUtil = HttpClientUtils.getInstance();
        String responseStr = null;
        try
        {
            responseStr = httpUtil.doPost(url, formParams);
        }
        catch (Exception e)
        {
            responseStr = "error";
        }
        // 不同的通道，解析返回结果不一样
        Map map = httpUtil.analyticResult(responseStr, channel);
        int sendStatus = (int) map.get("sendStatus");
        // 记录发送的短信内容
        SendSmsRecordDto dto = new SendSmsRecordDto();
        dto.setSendUsage(usage);
        dto.setSendContent(content);
        dto.setSendMobile(mobile);
        dto.setSendTime(new Date());
        dto.setSendStatus(sendStatus);
        saveSendSmsRecord(dto);
        return httpUtil.isSendSuccess(sendStatus);
    }

    /**
     * 判断当前短信发送场景是否需要校验当前手机号码是否会员
     * @param mobile 手机号码
     * @param usage 场景
     * @throws Exception
     */
    private void userExistsFlag(String mobile, String usage) throws Exception
    {
        boolean flag1 = CommonConst.LIMIT_SMS_CONTENT_KEYS.contains(usage);
        boolean flag2 = CommonConst.LIMIT_SMS_CONTENT_KEYS_REG.contains(usage);
        boolean flag = false;
        if (flag2 || flag1)
        {
            flag = queryNormalUserByMobile(mobile);
        }
        if (flag1)
        {
            if (!flag)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该手机号码未注册");
            }
        }
        if (flag2)
        {
            if (flag)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该手机号码已注册");
            }
        }
    }

    public boolean sendSmsMobileCode(String userName, String mobile, String code, String usage) throws Exception
    {
        // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
        String channel = getSmsChannel();
        String tmpKey = channel;
        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel) || CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
        {
            tmpKey = tmpKey + "_";
        }
        String url = getSmsValueByKey(tmpKey + CommonConst.SMS_URL, true);
        String sn = getSmsValueByKey(tmpKey + CommonConst.SMS_SN, true);
        String pwd = getSmsValueByKey(tmpKey + CommonConst.SMS_PWD, true);
        String content = getSmsContentByKey(usage);
        if (null == content)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "短信发送内容为空");
        }
        if (null == url || sn == null || pwd == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "没有找到短信发送相关参数信息");
        }
        // 将模板中的code替换为真实的code
        content = content.replaceFirst(CommonConst.PHP_SMS_USERNAME, userName);
        content = content.replaceFirst(CommonConst.PHP_SMS_MOBILE, mobile);
        content = content.replaceFirst(CommonConst.PHP_SMS_PWD, code);
        Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
        HttpClientUtils httpUtil = HttpClientUtils.getInstance();
        String responseStr = httpUtil.doPost(url, formParams);
        Map map = httpUtil.analyticResult(responseStr, channel);
        int sendStatus = (int) map.get("sendStatus");

        // 记录发送的短信内容
        SendSmsRecordDto dto = new SendSmsRecordDto();
        dto.setSendUsage(usage);
        dto.setSendContent(content);
        dto.setSendMobile(mobile);
        dto.setSendTime(new Date());
        dto.setSendStatus(sendStatus);
        saveSendSmsRecord(dto);
        return httpUtil.isSendSuccess(sendStatus);
    }

    private boolean queryNormalUserByMobile(String mobile) throws Exception
    {
        int re = userDao.queryNormalUserByMobile(mobile);
        if (re == 0)
        {
            return false;
        }
        return true;
    }

    /**
     * php后台发送经销商、代理商账号密码，查询短信内容模板key
     * @param key
     * @return
     */
    public String getSmsContentByKey(String key)
    {
        String content = null;
        try
        {
            Map contMap = commonDao.getMsgSettingContent(key);
            if (null != contMap && contMap.size() > 0)
            {
                Integer remandFlag = CommonValidUtil.isEmpty(contMap.get("remand_flag")) ? null
                        : Integer.parseInt(contMap.get("remand_flag") + "");
                if (null != remandFlag && remandFlag.intValue() != 1)
                {
                    throw new ValidateException(CodeConst.CODE_PHP_REMAND_FLAG_CLOSE, "提醒已经关闭");
                }
                content = CommonValidUtil.isEmpty(contMap.get("msg_content")) ? null
                        : (contMap.get("msg_content") + "");
            }
        }
        catch (Exception e)
        {
            logger.error("获取短信平台参数异常", e);
        }
        return content;
    }

    @Override
    public Map getSettingValueByKey(String key) throws Exception
    {
        Map param = new HashMap();
        param.put("settingKey", key);
        return commonDao.getSettingValueByKey(param);
    }

    public int saveSendSmsRecord(SendSmsRecordDto dto)
    {
        int re = 0;
        try
        {
            re = sendSmsRecordDao.saveSendSmsRecord(dto);
        }
        catch (Exception e)
        {
            logger.warn("记录短信异常", e);
        }
        return re;
    }

    /**
     * 发送短信内容 @Title: sendSms @param @param mobile @param @param
     * content @param @return @return String 返回类型 @throws
     */
    public boolean sendSms(String mobile, String content)
    {
        try
        {
            // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
            String channel = getSmsChannel();
            String tmpKey = channel;
            if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel) || CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
            {
                tmpKey = tmpKey + "_";
            }
            String url = getSmsValueByKey(tmpKey + CommonConst.SMS_URL, true);
            String sn = getSmsValueByKey(tmpKey + CommonConst.SMS_SN, true);
            String pwd = getSmsValueByKey(tmpKey + CommonConst.SMS_PWD, true);
            Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, channel);
            HttpClientUtils httpUtil = HttpClientUtils.getInstance();
            String responseStr = httpUtil.doPost(url, formParams);
            Map map = httpUtil.analyticResult(responseStr, channel);
            int sendStatus = (int) map.get("sendStatus");
            return httpUtil.isSendSuccess(sendStatus);
        }
        catch (Exception e)
        {
            logger.error("发送短信过程中产生了异常", e);
            return false;
        }
    }

    @Override
    public boolean sendSmsBatch(String mobile, String content) throws Exception
    {
        try
        {
            // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
            String url = "http://124.172.250.160/WebService.asmx/mt";
            String sn = "SDK-ZQ-SJHY-0817";
            String pwd = "n871vcz";
            Map<String, String> formParams = encapSmsParams(sn, pwd, content, mobile, "zq");
            HttpClientUtils httpUtil = HttpClientUtils.getInstance();
            String responseStr = httpUtil.doPost(url, formParams);
            Map map = httpUtil.analyticResult(responseStr, "zq");
            int sendStatus = (int) map.get("sendStatus");
            SendSmsRecordDto dto = new SendSmsRecordDto();
            dto.setSendChannle("zq");
            dto.setSendContent(content);
            dto.setSendMobile(mobile);
            dto.setSendTime(new Date());
            dto.setSendStatus(sendStatus);
            saveSendSmsRecord(dto);
            return httpUtil.isSendSuccess(sendStatus);
        }
        catch (Exception e)
        {
            logger.error("发送短信过程中产生了异常", e);
            return false;
        }
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
    private Map<String, String> encapSmsParams(String sn, String pwd, String content, String mobile, String channel)
            throws UnsupportedEncodingException
    {
        Map<String, String> formParams = new HashMap<String, String>();
        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel))
        {
            formParams.put("mobile", mobile);
            formParams.put("sn", sn);
            // 32位大写加密
            formParams.put("pwd", MD5Util.getMD5Str(sn + pwd).toUpperCase());
            formParams.put("content", URLEncoder.encode(content, "utf-8"));
            formParams.put("ext", "");
            formParams.put("stime", "");
            formParams.put("rrid", "");
            formParams.put("msgfmt", "");
        }
        else
        {
            formParams.put("mobile", mobile);
            formParams.put("Sn", sn);
            formParams.put("Pwd", pwd);
            formParams.put("content", content);
        }
        return formParams;
    }

    public String getSmsChannel() throws Exception
    {
        logger.info("===================>>获取短信发送通道-start");
        String result = "";
        String key = CommonConst.REDIS_SMS_PARAM + CommonConst.KEY_SMS_CHANNEL;
        logger.info("===================>>获取短信发送通道的key：" + key);
        // 缓存中获取
        String value = DataCacheApi.get(key);
        logger.info("===================>>获取短信发送通道的value：" + value);
        if (!StringUtils.isBlank(value))
        {
            result = value;
        }
        else
        {
            // 读配置文件
            String appProp = "properties/common.properties";
            // 商品索引文档路径
            value = PropertyUtil.readProperty(appProp, CommonConst.KEY_SMS_CHANNEL);
            if (StringUtils.isBlank(value))
            {
                value = CommonConst.SMS_ZQ_CHANNEL_KEY;// 默认设置为志晴科技通道
            }
            // 缓存3分钟
            DataCacheApi.setex(key, value, CommonConst.CACHE_TIME_SMS_CHANNEL);
            result = value;
        }
        logger.info("===================>>当前发送短信使用的通道为：" + result);
        if (!StringUtils.isBlank(result) && StringUtils.equals(result, CommonConst.SMS_MD_CHANNEL_KEY))
        {
            return result;
        }
        else
        {
            // 返回空，则为zq
            return "";
        }
    }

    @Deprecated
    public SysConfigureDto getSysConfigureDtoByKey(String key) throws Exception
    {
        return sysConfigureDao.getSysConfigureDtoByKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.common.ICommonService#pushShopUserMsg(java.
     * lang.String, java.lang.String, java.lang.Long)
     */
    @Override
    public void pushShopUserMsg(String action, String content, Long shopId) throws Exception
    {
        Long userId = shopDao.getUserByShopId(shopId);
        if (null == userId)
        {
            logger.info("店老板principal_id为空，shopId=" + shopId);
            return;
        }
        pushUserMsg(action, content, userId, CommonConst.USER_TYPE_TEN);

    }

    @Override
    public boolean login(String userName, String password) throws Exception
    {

        boolean login = true;

        // 查询用户
        UserDto userDB = this.userDao.getUserByMobileFromDB(userName);
        // 校验对象是否为空
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);

        // 校验密码是否一致
        if (!userDB.getPassword().equals(MD5Util.getMD5Str(password)))
        {
            login = false;
            throw new ValidateException(CodeConst.CODE_PWD_NOT_SAME, "密码不正确");
        }

        return login;
    }

    @Override
    public List<SendSmsRecordDto> getSmsListByMobile(String mobile) throws Exception
    {
        return sendSmsRecordDao.getSmsListByMobile(mobile);
    }

    @Override
    public void writeQueryLog(String userName, String mobile) throws Exception
    {
        commonDao.writeQueryLog(userName, mobile);
    }

    @Override
    public Map getMobileInfo() throws Exception
    {
        return commonDao.getMobileInfo();
    }

    @Override
    public int updateStatus(String mobile) throws Exception
    {
        return commonDao.updateStatus(mobile);
    }

    public void heartbeat(Long shopId, Integer systemType) throws Exception
    {
        // 缓存key
        String cachKey = "HeartbeatLogDto:" + shopId + ":" + systemType;
        logger.info("此次放入缓存的key为：" + cachKey);
        HeartbeatLogDto heartbeatDB = heartbeatLogDao.getHeartbeat(shopId, systemType);
        if (heartbeatDB != null)
        {
            // 将原来的最后更新时间更新为现上次更新时间
            Date lastTime = heartbeatDB.getLastTime();
            heartbeatDB.setPreviousTime(lastTime);
            heartbeatDB.setLastTime(new Date());
            heartbeatLogDao.updateHeartbeat(heartbeatDB);
            // 放入缓存
            DataCacheApi.del(CommonConst.KEY_SHOP + cachKey);
            DataCacheApi.setObjectEx(cachKey, heartbeatDB, CommonConst.CACHE_HEARTBEAT_LOG_OUT_TIME);
        }
        else
        {
            // 不存在==>更新上次心跳时间和最后心跳更新时间
            HeartbeatLogDto newHeartbeat = new HeartbeatLogDto();
            newHeartbeat.setLastTime(new Date());
            newHeartbeat.setPreviousTime(new Date());
            newHeartbeat.setShopId(shopId);
            newHeartbeat.setSystemType(systemType);
            heartbeatLogDao.addHeartbeat(newHeartbeat);
            // 放入缓存
            DataCacheApi.setObjectEx(cachKey, newHeartbeat, CommonConst.CACHE_HEARTBEAT_LOG_OUT_TIME);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.common.ICommonService#getClientOnlineStatus(
     * java.lang.Long, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Map<String, Object>> getUserAuthList(UserPermission userPermission) throws Exception {
    	return userPermissionDao.getUserAuthList(userPermission);
    }
    @Override
    public Map<String, Object> getClientOnlineStatus(Long shopId, Integer systemType, Integer intervalTimes)
            throws Exception
    {
        // 1dcq_sys_configure表key
        String sysKey = CommonConst.HEART_BEAT_SECS;
        // 心跳间隔时间，单位：秒，初始值=5
        int heartBeatSecs = 5;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ConfigDto searchCondition = new ConfigDto();
        searchCondition.setBizId(0l);
        searchCondition.setBizType(0);
        searchCondition.setConfigKey(CommonConst.HEART_BEAT_SECS);
        ConfigDto config = this.getConfigDto(searchCondition);

        /*
         * SysConfigureDto sysConfigureDto =
         * sysConfigureDao.getSysConfigureDtoByKey(sysKey); if (null !=
         * sysConfigureDto) { heartBeatSecs =
         * Integer.valueOf(sysConfigureDto.getConfigureValue()); }
         */
        if (null != config)
        {
            heartBeatSecs = Integer.valueOf(config.getConfigValue());
        }

        HeartbeatLogDto heartbeatDB = heartbeatLogDao.getHeartbeat(shopId, systemType);
        Date lastTime = heartbeatDB.getLastTime();

        // 两次时间相隔描述
        long interval = (new Date().getTime() - lastTime.getTime()) / 1000;
        // 假如心跳时间差次大于参数（心跳间隔次数），则认为服务端已离线
        if ((interval / heartBeatSecs) < intervalTimes)
        {
            resultMap.put("intervalTimes", 1);
        }
        else
        {
            resultMap.put("intervalTimes", 0);
        }
        return resultMap;
    }

    @Deprecated
    public List<Map> getSysConfiguresByKeys(Map<String, Object> requestMap) throws Exception
    {
        return sysConfigureDao.getSysConfiguresByKeys(requestMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.common.ICommonService#getCodeByType(java.lang.
     * String)
     */
    @Override
    public List<CodeDto> getCodeByType(String codeType) throws Exception
    {
        return codeDao.getCodeByType(codeType);
    }

    @Override
    public void launchPush(String mobile, String clientType, String action, String data) throws Exception
    {
        UserDto user = userDao.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        Long userId = user.getUserId();
        if ("1".equals(clientType))
        {
            // 收银机
            ShopDto shop = shopDao.getShopByPrincipalId(userId);
            CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            String regId = shopDeviceDao.getShopRegIdByShopId(shop.getShopId());
            if (StringUtils.isNotBlank(regId))
            {
                PushDto push = createPushDto(action, data, regId, userId, shop.getShopId(), shop.getShopMode());
                pushServiceImpl.pushInfoToShop(push);
            }
        }
        else if ("2".equals(clientType))
        {
            // 管家APP
            pushToUser(action, data, userId, CommonConst.USER_TYPE_TEN);
        }
        else if ("3".equals(clientType))
        {
            // 消费者APP
            pushToUser(action, data, userId, CommonConst.USER_TYPE_ZREO);
        }

    }

    private void pushToUser(String action, String data, Long userId, int userType) throws Exception
    {
        List<PushUserTableDto> pushUserTables = pushUserTableDao.getPushUserByUserId(userId,
                CommonConst.USER_TYPE_ZREO);
        if (CollectionUtils.isNotEmpty(pushUserTables))
        {
            for (PushUserTableDto pushUserTableDto : pushUserTables)
            {
                PushDto push = createPushDto(action, data, pushUserTableDto.getRegId(), userId, null,
                        pushUserTableDto.getOsInfo());
                pushServiceImpl.pushInfoToUser(push, userType);
            }
        }
    }

    private PushDto createPushDto(String action, String data, String regId, Long userId, Long shopId, String platForm)
    {
        PushDto push = new PushDto();
        push.setAction(action);
        push.setContent(data);
        push.setRegId(regId);
        push.setUserId(userId);
        push.setShopId(shopId);
        push.setPlatForm(platForm);
        return push;
    }

    @Override
    public PageModel getMessageList(MessageCenterDto messageCenterDto, int pageNo, int pageSize) throws Exception
    {
        int count = messageCenterDao.getMessageCenterDtoListCount(messageCenterDto);
        List<MessageCenterDto> list = null;
        if (count > 0)
        {
            list = messageCenterDao.getMessageCenterDtoList(messageCenterDto, pageNo, pageSize);
        }
        PageModel pm = new PageModel();
        pm.setTotalItem(count);
        pm.setList(list);
        pm.setToPage(pageNo);
        pm.setPageSize(pageSize);
        return pm;
    }

    @Override
    public String BusAreaOperateFeedback(Map<String, Object> paramMap) throws Exception
    {
        Long bizId = (Long) paramMap.get("bizId");
        Integer bizType = (Integer) paramMap.get("bizType");
        Integer feedbackType = (Integer) paramMap.get("feedbackType");
        Long shopId = (Long) paramMap.get("shopId");
        // Long userId = (Long)paramMap.get("userId");
        // Integer clientSystemType = (Integer)paramMap.get("clientSystemType");
        // Date clientTime = (Date)paramMap.get("clientTime");

        String msg = CodeConst.MSG_SUCCESS_OPT_FB;

        BusinessAreaStatDto businessAreaStatDto = new BusinessAreaStatDto();
        businessAreaStatDto.setBusinessAreaActivityId(bizId);
        businessAreaStatDto.setShopId(shopId);

        int count = businessAreaStatDao.getCountCompKey(businessAreaStatDto);
        CommonValidUtil.validPositInt(count, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BUSAREA_STAT);

        /**
         * 商圈活动统计处理
         * 
         * 根据bizType分发统计商圈活动信息阅读、分享的数量
         */

        if (bizType == BizTypeEnum.BUSAREA_ACTIVITY.getValue())
        {
            switch (feedbackType)
            {
                case 1:// 收到
                    break;
                case 2:// 点击（阅读）
                    businessAreaStatDao.addUpReadNumByCompKey(businessAreaStatDto);
                    break;
                case 3:// 分享
                    businessAreaStatDao.addUpSharedNumByCompKey(businessAreaStatDto);
                    break;
                default:
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_INVALID_FB_TYPE);
            }
        }
        else
        {
            msg = CodeConst.MSG_INVALID_BIZTYPE;
        }

        return msg;
    }

    @Override
    public String MsgOperateFeedback(Map<String, Object> paramMap) throws Exception
    {
        Long bizId = (Long) paramMap.get("bizId");
        Integer bizType = (Integer) paramMap.get("bizType");
        Integer feedbackType = (Integer) paramMap.get("feedbackType");
        Integer notifyType = (Integer) paramMap.get("notifyType");
        Integer receiverId = (Integer) paramMap.get("receiverId");
        // Long userId = (Long)paramMap.get("userId");
        // Long shopId = (Long)paramMap.get("shopId");
        // Integer clientSystemType = (Integer)paramMap.get("clientSystemType");
        // Date clientTime = (Date)paramMap.get("clientTime");

        int count = messageCenterDao.getCountById(bizId,bizType,notifyType,receiverId);
        CommonValidUtil.validPositInt(count, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MESSAGE);

        /**
         * 修改消息反馈状态
         */

        messageCenterDao.updateFeedBackTypeById(bizId,bizType, feedbackType,notifyType,receiverId);

        return CodeConst.MSG_SUCCESS_OPT_FB;

    }

    @Override
    public PageResult<ConfigDto> queryForConfig(ConfigQueryCondition configQueryCondition)
    {
        Page pageModel = Page.getDefaulPage();
        pageModel.setpSize(Integer.MAX_VALUE);
        ConfigQueryCondition tempSearch = configQueryCondition.getCopy();
        // 系统配置
        List<ConfigDto> sysConfigs = null;
        // 行业配置
        List<ConfigDto> columnConfigs = null;
        // 店铺配置
        List<ConfigDto> shopConfigs = null;
        // 继承类型
        List<Integer> extendsTyps = new ArrayList<Integer>();

        /* 1.查询系统配置 */
        // 平台配置默认bizId与bizType为0;
        tempSearch.setBizId(0l);
        tempSearch.setBizType(0);
        // 设置继承类型为可继承或者修改
        extendsTyps.add(1);
        extendsTyps.add(2);
        tempSearch.setExtendsType(extendsTyps);
        logger.debug("开始查询系统配置");
        sysConfigs = configDao.queryForConfig(tempSearch, pageModel);
        /* 2.查询商铺配置 */
        if (Integer.valueOf(1).equals(configQueryCondition.getBizType()))
        {
            configQueryCondition.setShopId(configQueryCondition.getBizId());
        }
        Long shopId = configQueryCondition.getShopId();
        if (null != shopId)
        { // 如果shopId不为空，搜索指定商铺配置
            List<Long> shopIds = new ArrayList<Long>();
            shopIds.add(shopId);
            ShopDto shopDto = shopDao.getListByShopIds(shopIds).get(0);
            Integer columnId = shopDto.getColumnId();
            // 商铺对应的bizType为1
            tempSearch.setBizType(1);
            tempSearch.setBizId(shopId);
            // 搜索商铺私有配置时继承关系不限制
            tempSearch.setExtendsType(null);
            logger.debug("开始查询商铺配置");
            shopConfigs = configDao.queryForConfig(tempSearch, pageModel);

            // 传入了shopId则查询行业时的bizId、bizType以查出的为行业对应的bizType为19
            // 当传入了shopId时，必须为可继承的
            extendsTyps.clear();
            extendsTyps.add(1);
            extendsTyps.add(2);
            tempSearch.setExtendsType(extendsTyps);
            tempSearch.setBizType(19);
            tempSearch.setBizId(Long.valueOf(columnId + ""));
        }
        else
        { // 否则bizId,bizType分别为传入值
            tempSearch.setExtendsType(null);
            tempSearch.setBizId(configQueryCondition.getBizId());
            tempSearch.setBizType(configQueryCondition.getBizType());
        }

        /* 3.查询行业配置 */
        // 如果行业信息不为空，则查询行业配置
        if (null != tempSearch.getBizType() && null != tempSearch.getBizId())
        {
            // 是否只是搜索行业仅可被商铺继承的配置
            logger.debug("开始查询行业配置");
            columnConfigs = configDao.queryForConfig(tempSearch, pageModel);
        }
        // 合并配置
        logger.debug("合并三级配置");
        List<ConfigDto> tempResult = this.mergeConfig(sysConfigs, columnConfigs, shopConfigs);
        PageResult<ConfigDto> result = new PageResult<ConfigDto>();
        result.setpNo(1);
        result.setrCount(tempResult.size());
        result.setLst(tempResult);
        return result;
    }

    /**
     * 按优先级合并配置，必须按照顺序传入参数
     * @param sysConfigs 平台配置
     * @param columnConfigs 行业配置
     * @param shopConfigs 商铺配置
     * @return
     */
    private List<ConfigDto> mergeConfig(List<ConfigDto> sysConfigs, List<ConfigDto> columnConfigs,
            List<ConfigDto> shopConfigs)
    {
        List<ConfigDto> result = new ArrayList<ConfigDto>();
        /* 以下三顺序不能变动 */
        // 加入商铺配置
        this.addIfAbsent(result, shopConfigs);
        // 加入行业配置
        this.addIfAbsent(result, columnConfigs);
        // 加入平台配置
        this.addIfAbsent(result, sysConfigs);

        return result;
    }

    private void addIfAbsent(List<ConfigDto> base, List<ConfigDto> eles)
    {
        if (null != eles)
        {
            if (base.size() == 0)
            {
                base.addAll(eles);
            }
            else
            {
                for (ConfigDto temp : eles)
                {
                    if (!base.contains(temp))
                    {
                        base.add(temp);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateConfigForShop(Long shopId, List<ConfigDto> configs)
    {
        List<Long> shopIds = new ArrayList<Long>();
        shopIds.add(shopId);
        ShopDto shopDto = shopDao.getListByShopIds(shopIds).get(0);
        Integer columnId = shopDto.getColumnId();
        // 系统配置
        List<ConfigDto> sysConfigs = null;
        // 行业配置
        List<ConfigDto> columnConfigs = null;
        // 店铺配置
        List<ConfigDto> shopConfigs = null;
        Page pageModel = Page.getDefaulPage();
        pageModel.setpSize(Integer.MAX_VALUE);
        ConfigQueryCondition tempSearch = new ConfigQueryCondition();
        /* 1.查询系统配置 */
//        sysConfigs = (List<ConfigDto>) DataCacheApi.getObject("sysConfig:1");
//        if (null == sysConfigs)
//        {
            // 平台配置默认bizId与bizType为0;
            tempSearch.setBizId(0l);
            tempSearch.setBizType(0);
            // 设置继承类型为可继承或者修改
            tempSearch.setExtendsType(null);
            sysConfigs = configDao.queryForConfig(tempSearch, pageModel);
//            DataCacheApi.setObject("sysConfig:1", sysConfigs);
//        }

        /* 2.查询行业配置 */
//        columnConfigs = (List<ConfigDto>) DataCacheApi.getObject("columnConfig1:" + columnId);
//        if (null == columnConfigs)
//        {
            // 行业配置默认bizType为19;
            tempSearch.setBizId(Long.parseLong(columnId + ""));
            tempSearch.setBizType(19);
            // 设置继承类型为可继承或者修改
            tempSearch.setExtendsType(null);
            columnConfigs = configDao.queryForConfig(tempSearch, pageModel);
//            DataCacheApi.setObject("columnConfig1:" + columnId, sysConfigs);
//        }

        /* 3.查询商铺配置 */
//        shopConfigs = (List<ConfigDto>) DataCacheApi.getObject("shopConfig:" + shopId);
//        if (null == shopConfigs)
//        {
            // 商铺配置默认bizType为1;
            tempSearch.setBizId(shopId);
            tempSearch.setBizType(1);
            // 设置继承类型为可继承或者修改
            tempSearch.setExtendsType(null);
            shopConfigs = configDao.queryForConfig(tempSearch, pageModel);
//            DataCacheApi.setObject("shopConfig:" + shopId, shopConfigs);
//        }
        this.updateConfigs(shopConfigs, columnConfigs, sysConfigs, configs, shopId);
    }

    /**
     * 注意参数的顺序必须严格保证
     * @param shopConfigs 商铺配置
     * @param columnConfigs 行业配置
     * @param sysConfigs 平台配置
     * @param updateConfigs 要更新的配置
     * @param shopId 要更新的shopId
     */
    private void updateConfigs(List<ConfigDto> shopConfigs, List<ConfigDto> columnConfigs, List<ConfigDto> sysConfigs,
            List<ConfigDto> updateConfigs, Long shopId)
    {
        /**
         * 该方法假设每次更新/添加的配置不多的情况下进行的操作，没有就大批量更新的情景进行算法上的优化 ,这里的实现采用简单for循环
         */

        Long bizId = shopId;
        // 商铺配置bizType默认为1
        Integer bizType = 1;

        List<ConfigDto> updators = new ArrayList<ConfigDto>();
        List<ConfigDto> inserts = new ArrayList<ConfigDto>();
        boolean shopConfigExists = null != shopConfigs && shopConfigs.size() > 0;
        boolean columnConfigExists = null != columnConfigs && columnConfigs.size() > 0;
        boolean sysConfigExists = null != sysConfigs && sysConfigs.size() > 0;
        /* 考虑性能因素以及编码雅观，使用HashMap */
        Map<String, ConfigDto> shopConfigMap = shopConfigExists ? this.getConfigListAsMap(shopConfigs) : null;
        Map<String, ConfigDto> columnConfigMap = columnConfigExists ? this.getConfigListAsMap(columnConfigs) : null;
        Map<String, ConfigDto> sysConfigMap = sysConfigExists ? this.getConfigListAsMap(sysConfigs) : null;
        for (ConfigDto temp : updateConfigs)
        {
            if (temp.getConfigKey() == null || temp.getConfigValue() == null)
            {
                throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_NULL,
                        temp.getConfigKey() + ":" + temp.getConfigKey() + " 对应的键或者值不能为null");
            }
            boolean isAdd = true;
            // 设置必要的属性
            temp.setBizId(bizId);
            temp.setBizType(bizType);
            // 1，先检测shop配置是否冲突
            if (shopConfigExists)
            {
                isAdd = !this.checkExistsAndInitialize(shopConfigMap, temp, true, isAdd);
            }

            // 2.检测行业配置是否冲突
            if (columnConfigExists)
            {
                this.checkExistsAndInitialize(columnConfigMap, temp, false, isAdd);
            }

            // 3.检测平台配置是否冲突
            if (sysConfigExists)
            {
                this.checkExistsAndInitialize(sysConfigMap, temp, false, isAdd);
            }

            // 判断是更新配置还是添加配置
            if (isAdd)
            {
                // 商铺配置默认继承类型为0
                if (null == temp.getExtendsType())
                {
                    temp.setExtendsType(0);
                }
                // 商铺配置默认离线修改为允许
                if (null == temp.getIsOfflineModify())
                {
                    temp.setIsOfflineModify(1);
                }
                inserts.add(temp);
            }
            else
            {
                updators.add(temp);
            }
        }

        // 更新
        if (updators.size() > 0)
        {
            configDao.batchUpdate(updators);
        }
        // 添加
        if (inserts.size() > 0)
        {
            configDao.batchInsert(inserts);
        }

        // 更新商铺缓存
//        DataCacheApi.del("shopConfig:" + shopId);
    }

    /**
     * 将list转换为{@link HashMap}
     * @param lst
     * @return
     */
    private Map<String, ConfigDto> getConfigListAsMap(List<ConfigDto> lst)
    {
        Map<String, ConfigDto> result = new HashMap<String, ConfigDto>();
        for (ConfigDto dto : lst)
        {
            result.put(dto.getConfigKey(), dto);
        }
        return result;
    }

    /**
     * 
     * @param modelMap
     * @param updateDto 要更新或者添加的{@link ConfigDto}对象
     * @param checkConfigGroupOnly
     *        是否只是校验configGroup，如果是，则该方法只校验configGroup，否则将校验extendsType是否等于2
     * @param isAdd 是否是添加updateDto, 如果是，
     * @return modelMap是否可以能过updateDto.configKey取得得对象
     */
    private boolean checkExistsAndInitialize(Map<String, ConfigDto> modelMap, ConfigDto updateDto,
            boolean checkConfigGroupOnly, boolean isAdd)
    {
        ConfigDto modelConfig = modelMap.get(updateDto.getConfigKey());
        boolean exists = null != modelConfig;
        if (exists)
        {
            // 检测是否允许修改
            if (!checkConfigGroupOnly)
            {
                if (modelConfig.getExtendsType() != 2)
                {
                    throw new ServiceException(CodeConst.CODE_PARAMETER_ILLEGAL, modelConfig.getConfigKey() + " 不允许修改");
                }
            }
            String configGroup = updateDto.getConfigGroup();
            // 默认如果没有传入configGroup则该字段不更新
            if (null != configGroup && !configGroup.equals(modelConfig.getConfigGroup()))
            {
                // TODO 该处有必要进一步处理
                throw new ServiceException(CodeConst.CODE_PARAMETER_ILLEGAL,
                        updateDto.getConfigKey() + " configGroup不正确");
            }
            if (isAdd)
            {
                updateDto.setConfigDesc(modelConfig.getConfigDesc());
                updateDto.setConfigGroup(modelConfig.getConfigGroup());
            }
        }
        return exists;
    }

    @Override
    public ConfigDto getConfigDto(ConfigDto searchCondition)
    {
        ConfigDto result = null;
        List<ConfigDto> dtos = this.queryConfigDto(searchCondition);
        if (null != dtos && dtos.size() > 0)
        {
            result = dtos.get(0);
        }
        return result;
    }

    @Override
    public List<ConfigDto> queryConfigDto(ConfigDto searchCondition)
    {
        return configDao.queryForAllConfig(searchCondition);
    }

    @Override
    public Map<String, Object> getConfigByGroup(String configGroup) {
    	Map<String, Object> configMap = new HashMap<String, Object>(); 
    	ConfigDto searchCondition = new ConfigDto();
    	searchCondition.setConfigGroup(configGroup);
    	List<ConfigDto> configList = configDao.queryForAllConfig(searchCondition);
    	if (CollectionUtils.isNotEmpty(configList)) {
    		for (ConfigDto config : configList) {
    			configMap.put(config.getConfigKey(), config.getConfigValue());
    		}
    	}
    	return configMap;
    }
    
    @Override
    public void initShopConfig(Long shopId)
    {
        if (null == shopId)
        {
            return;
        }
        // 首先删除已有的(如果存在)配置
        ConfigDto delCondition = new ConfigDto();
        delCondition.setBizType(1);
        delCondition.setBizId(shopId);
        configDao.deleteConfig(delCondition);
        // 添加默认初始化配置
        String value = CommonConst.ONLINE_SETTING_BAIL_AMOUNT_VALUE + "";
        ConfigDto searchCondition = new ConfigDto();
        searchCondition.setBizId(0l);
        searchCondition.setBizType(0);
        searchCondition.setConfigKey(CommonConst.SHOP_SETTING_BAIL_AMOUNT);
        ConfigDto config = this.getConfigDto(searchCondition);
        if (null != config)
        {
            value = config.getConfigValue();
        }
        List<ConfigDto> configs = new LinkedList<ConfigDto>();
        configs.add(this.builShopInitConfig(shopId, CommonConst.SHOP_SETTING_BAIL_FLAG,
                CommonConst.SHOP_SETTING_BAIL_FLAG_FALSE, "DEPOSIT_CONFIG"));
        configs.add(this.builShopInitConfig(shopId, CommonConst.SHOP_SETTING_BAIL_ALTER_AMOUNT,
                String.valueOf(CommonConst.ONLINE_SETTING_BAIL_ALTER_AMOUNT_VALUE), "DEPOSIT_CONFIG"));
        configs.add(this.builShopInitConfig(shopId, CommonConst.SHOP_SETTING_BAIL_AMOUNT, value, "DEPOSIT_CONFIG"));
        // 如果是线上签约的店铺，需要设置自动转充保证金配置
        configDao.batchInsert(configs);
    }

    private ConfigDto builShopInitConfig(Long shopId, String configKey, String configValue, String configGroup)
    {
        ConfigDto result = new ConfigDto();
        result.setBizType(1);
        result.setBizId(shopId);
        result.setConfigKey(configKey);
        result.setConfigValue(configValue);
        result.setConfigGroup(configGroup);
        result.setExtendsType(0);
        result.setIsOfflineModify(1);
        Date now = new Date();
        result.setLastUpdateTime(now);
        result.setCreateTime(now);
        return result;
    }

    @Override
    public Map<String, Object> getAllMqttInitInfos()
    {   
        Map<String, Object> rs = new HashMap<String, Object>();
        ConfigDto searchCondition = new ConfigDto();
        //EMQTT中的group为EMQTT
        searchCondition.setConfigGroup("EMQTT");
        List<ConfigDto> configs = configDao.queryForAllConfig(searchCondition);
//        boolean continueToGetPassword = true;
        for(ConfigDto config : configs){
            if("url".equalsIgnoreCase(config.getConfigKey())){
                rs.put("url", config.getConfigValue());
            }else if(config.getConfigKey().startsWith("userName")){
                rs.put("userName", config.getConfigValue());
            }else if(config.getConfigKey().startsWith("password")){
                rs.put("password", config.getConfigValue());
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> getMqttInitInfoForClient(String clientType, Integer bizType, Integer bizId)
    {   
        Map<String, Object> rs = new HashMap<String, Object>();
        if(1 == bizType.intValue() && "1".equals(clientType)){  //店铺收银机
            //生成topic
            rs.put("topic", new String[]{"s1/cas/" + bizId.intValue()});
        }
        return rs;
    }

    public int deleteConfigs(ConfigQueryCondition searchCondition) {
        return commonDao.deleteConfigs(searchCondition);
    }

	@Override
	public void sendSmsByEntity(SmsDto smsDto) throws NumberFormatException, Exception {
		String mobiles = "";
		if(null != smsDto.getMemberIds()){
			String[] memberIdArray = smsDto.getMemberIds().split(",");
			for (String memberId : memberIdArray) {
				CommonValidUtil.validStrLongFmt(memberId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_MEMBERIDS);
				ShopMemberDto shopMemberDto = shopMemberDao.getShopMemberById(Long.valueOf(memberId));
				mobiles += shopMemberDto.getMobile()+",";
			}
		}else if (null != smsDto.getUserIds()){
			String[] userIdArray = smsDto.getUserIds().split(",");
			for (String userId : userIdArray) {
				CommonValidUtil.validStrLongFmt(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERIDS);
				UserDto userDto = userDao.getUserById(Long.valueOf(userId));
				mobiles += userDto.getMobile()+",";
			}
		}else{
			mobiles = smsDto.getMobiles()+",";
		}
		mobiles = mobiles.substring(0, mobiles.length()-1);
		String[] mobileArray = mobiles.split(",");
		SmsReplaceContent content = null;
		for (String mobile : mobileArray) {
			content = new SmsReplaceContent();
	        content.setMobile(mobile);
	        content.setContent(smsDto.getSmsContent());
	        content.setSmsType(smsDto.getSmsType()+"");
	        boolean flag = sendSmsService.sendSmsMobileCode(content);
//			this.sendSms(mobile, smsDto.getSmsContent());
		}
		if(null != smsDto.getSmsModelType()){
			if(smsDto.getSmsModelType() == CommonConst.SMS_MODEL_TYPE_IS_BIRTHDAY){
				if(null != smsDto.getMemberIds()){
					String[] memeberIdsArray = smsDto.getMemberIds().split(",");
					ShopMemberDto shopMemberDto  = null;
					for (int i = 0; i < memeberIdsArray.length; i++) {
						shopMemberDto = new ShopMemberDto();
						shopMemberDto.setMemberId(Long.valueOf(memeberIdsArray[i]));
						shopMemberDto.setIsSendBirthdaySms(CommonConst.IS_SEND_BIRTHDAY_SMS_IS_TRUE);
						shopMemberDao.updateShopMemberById(shopMemberDto);
					}
				}else if(null != smsDto.getUserIds()){
					String[] userIdsArray = smsDto.getUserIds().split(",");
					ShopMemberDto shopMemberDto  = null;
					for (int i = 0; i < userIdsArray.length; i++) {
						shopMemberDto = new ShopMemberDto();
						shopMemberDto.setUserId(Long.valueOf(userIdsArray[i]));
						shopMemberDto.setShopId(smsDto.getShopId());
						shopMemberDto.setIsSendBirthdaySms(CommonConst.IS_SEND_BIRTHDAY_SMS_IS_TRUE);
						shopMemberDto.setMemberStatus(CommonConst.MEMBER_STATUS_DELETE);
						shopMemberDao.updateShopMemberByMobileOrUserId(shopMemberDto);
					}
				}else if(null != smsDto.getMobiles()){
					String[] mobilesArray = smsDto.getMobiles().split(",");
					ShopMemberDto shopMemberDto = null;
					for (int i = 0; i < mobilesArray.length; i++) {
						shopMemberDto= new ShopMemberDto();
						shopMemberDto.setMobile(Long.valueOf(mobilesArray[i]));
						shopMemberDto.setShopId(smsDto.getShopId());
						shopMemberDto.setIsSendBirthdaySms(CommonConst.IS_SEND_BIRTHDAY_SMS_IS_TRUE);
						shopMemberDto.setMemberStatus(CommonConst.MEMBER_STATUS_DELETE);
						shopMemberDao.updateShopMemberByMobileOrUserId(shopMemberDto);
					}
				}
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.common.ICommonService#agentInfoChange(java.lang.Long, java.lang.Long)
	 */
	@Override
    public Double agentInfoChange(Long agentId, Long userId, String orderId) throws Exception {
        /*
         * 1、判断代理商ID和用户ID是否匹配 2、获取店铺的购买产品金额,判断是否为了0，如果为0直接结束
         * 3、先去1dcq_agent_rebates表中根据代理id查询代理是否存在， 如果存在流程结 束，不存在进行返利操作
         * 4、开始返利，先去获取返利配置b_buy_v_rebates_ratio的值
         * ，计算当日返利金额=b_buy_v_rebates_ratio*buy_v_money 5、发送短信 6、给代理商加钱
         * 7、给代理商推荐用户返利
         */

        /* 1、判断代理商ID和用户ID是否匹配 */
	    Double dRebatesDayMoney = 0D;
        AgentDto agent = agentDao.getAgentByUserIdAndAgentId(userId, agentId);
        CommonValidUtil.validObjectNull(agent, CodeConst.CODE_PARAMETER_NOT_EXIST, "代理商不存在");
        if(!DateUtils.format(agent.getCreateTime(), DateUtils.DATE_FORMAT).equals(DateUtils.format(new Date(), DateUtils.DATE_FORMAT))) {
            logger.info("代理商不是当天添加的不支付返利");
            return 0D;
        }
        
        /* 2、获取店铺的购买产品金额,判断是否为了0，如果为0直接结束 */
        Double slottingFee = agent.getSlottingFee();// 代理佣金
        if (slottingFee == null || slottingFee == 0D) {
            return 0D;
        }
        
        //为了防止代理商对应的用户信息有误，需更新代理商对应的用户信息
        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, "代理商用户信息不存在");
        user.setRebatesLevel(getRebatesLevel(agent.getAgentType()));
        userDao.updateUser(user);
        // 处理推荐人
        UserDto referUserDto = userDao.getUserById(agent.getReferUserId());
        Map<String, Object> configMap = getConfigByGroup("GROUP_3721");
        
        if(agent.getAgentType() != null && (agent.getAgentType() == 31 || agent.getAgentType() == 32
                || agent.getAgentType() == 33)) {
            /* 3、根据代理id查询代理是否存在， 如果存在流程结束，不存在进行返利操作 */
            AgentRebatesDto agentRebatesDto = new AgentRebatesDto();
            agentRebatesDto.setAgentId(agentId);
            agentRebatesDto.setUserId(userId);
            List<AgentRebatesDto> agentRebatesDtoList = agentRebatesDao.getAgentRebates(agentRebatesDto);
            if (CollectionUtils.isNotEmpty(agentRebatesDtoList)) {
                return 0D;
            }
            Object dRebatesConfig = configMap.get("d_rebates_ratio_by_year");
            Double dRebatesRatioByYear = dRebatesConfig == null ? 0 : Double.valueOf(dRebatesConfig.toString());
            // 年返还金额
            Double dRebatesYearMoney = NumberUtil.multiply(dRebatesRatioByYear, slottingFee);
            // 日返还
            dRebatesDayMoney = NumberUtil.divide(dRebatesYearMoney, 365D, 4);
            // 剩余
            Double agentWaitRebatesMoney = NumberUtil.sub(slottingFee, dRebatesDayMoney);

            // 插入首次记录
            AgentRebatesDto agentRebates = new AgentRebatesDto();
            agentRebates.setAgentId(agentId);
            agentRebates.setAgentRebatesRatioYear(dRebatesRatioByYear);
            agentRebates.setAgentWaitRebatesMoney(agentWaitRebatesMoney);
            agentRebates.setCreateTime(new Date());
            agentRebates.setIsFinish(0);
            agentRebates.setLastUpdateTime(new Date());
            agentRebates.setSlottingFee(slottingFee);
            agentRebates.setUserId(userId);
            agentRebatesDao.insertAgentRebates(agentRebates);

            agentRebate(agentId, userId, slottingFee, dRebatesRatioByYear, dRebatesDayMoney);
          
			OrderDto orderRefer = new OrderDto();
			orderRefer.setOrderTitle("返还"+dRebatesDayMoney+"元");
			orderRefer.setOrderId(orderId);
			orderRefer.setUserId(userId);
			//推荐代理商收益
            agentAccountCommission(orderRefer, configMap, referUserDto, slottingFee,
                    CommonConst.USER_BILL_TYPE_RECOMMEND_AGENT_AWARD);
        } else if (agent.getAgentType() != null && (agent.getAgentType() == 20 || agent.getAgentType() == 21
                || agent.getAgentType() == 22)) {
            if (referUserDto == null || ("normal_ratio").equals(referUserDto.getRebatesLevel())) {
                //普通会员没有奖励
                return 0D;
            }
            Long referUserId = referUserDto.getUserId();
            //计算平台税率
            Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
            Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
            //推荐经销商奖励
            Object areferARatioConfig = configMap.get("a_refer_a_commission");
            Double areferARatio = areferARatioConfig == null ? 0 : Double.valueOf(areferARatioConfig.toString());
            Double rewardAmount = NumberUtil.multiply(slottingFee, areferARatio, (1 - platformTaxDeductionRatio));

            // 扣减收益循环计数值,每达到上限值需减去上限值,普通C不计入
            Double deductionCountValue = rewardAmount;

            userAccountDao.updateUserAccount(referUserId, rewardAmount, rewardAmount, rewardAmount, null, null, null,
                    null, null, null, null, deductionCountValue, null, null, null);

            UserAccountDto userAccount = userAccountDao.getAccountMoney(referUserId);
            // 账单前的账户总额
            Double amount = NumberUtil.sub(userAccount.getAmount(), rewardAmount);
            OrderDto order = new OrderDto();//为了公用记账方法,构造订单对象
            order.setOrderTitle("推荐经销商奖励");
            order.setSettlePrice(slottingFee);
            order.setUserId(userId);
            order.setMobile(user.getMobile());
            order.setOrderId(orderId);
            // 记录用户账单
            userBillService.insertUserBill(referUserId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, rewardAmount,
                    userAccount.getRewardAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                    "3721推荐经销商奖励-平台奖励,奖励比例："+areferARatio, CommonConst.USER_BILL_TYPE_RECOMMED_DEALER_AWARD, CommonConst.USER_ACCOUNT_TYPE_REWARD,
                    null);

            String platformBillDesc = "3721推荐奖励-支付推荐经销商奖励,奖励比例："+areferARatio +"推荐经销商ID:"+agent.getAgentId();

            // 平台出帐，只记录一条，在描述中区分币的值
            platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, rewardAmount,
                    CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, null, "支付推荐经销商奖励", platformBillDesc, CommonConst.PLATFORM_BILL_TYPE_REFER_AGENCY,
                    CommonConst.PLATFORM_BILL_STATUS_OVER);
            
            //平台税收入账 
            Double taxDeductionMoney = NumberUtil.multiply(slottingFee, areferARatio, platformTaxDeductionRatio);
            if(taxDeductionMoney > 0) {
                // 平台记账，税金只记一条账单
                platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxDeductionMoney,
                        null, order, null, "收取返利税金",
                        "3721-推荐经销商奖励平台扣税", CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                        CommonConst.PLATFORM_BILL_STATUS_OVER);
            }
            //处理管理费
            manageMoneySettle(order, referUserId, true, configMap);
            
            //推送奖励信息
            SmsReplaceContent pushReplaceContent = new SmsReplaceContent();
            rewardAmount = rewardAmount == null ? 0 : rewardAmount;
            pushReplaceContent.setMoney(NumberUtil.fmtDouble(rewardAmount, 4));
            pushReplaceContent.setUsage("recommend_dealer");
            pushUserMsg("recommend_dealer", referUserId, 0, pushReplaceContent);
        }
        return dRebatesDayMoney;
    }

	private String getRebatesLevel(Integer agentType) {
	    if(agentType == null) {
	        agentType = 0;
	    }
	    String value = "normal_ratio";
	    switch (agentType) {
	        case 20:
                value = "initial_ratio";
                break;
            case 21:
                value = "initial_ratio";
                break;
            case 22:
                value = "middle_ratio";
                break;
            case 31:
                value = "middle_ratio";
                break;
            case 32:
                value = "middle_ratio";
                break;
            case 33:
                value = "middle_ratio";
                break;
            default:
                break;
        }
	    return value;
	}

    public void agentRebate(Long agentId, Long userId, Double slottingFee, Double dRebatesRatioByYear,
            Double dRebatesDayMoney) throws Exception {
        // 更新代理商账户
        /*
         * Long userId,
         * Double changeAmount,
         * Double changeRewardAmount, 
         * Double changeRewardTotal, 
         * Double changeCouponAmount, 
         * Double  changeFreezeAmount,
         * Double legendTotal,
         * Double consumeAmount,
         * Double consumeTotal,
         * Double voucherAmount,
         * Double voucherTotal,
         * Double  deductionCountValue,
         * Double deductionTotal, 
         * Double consumeRebateTotal,
         * Double consumeRebateMoney
         * 
         */
        // 更新用户账号
        userAccountDao.updateUserAccount(
        		userId, 
        		dRebatesDayMoney,
        		dRebatesDayMoney, 
        		dRebatesDayMoney,
        		null,
        		null,
        		dRebatesDayMoney, 
        		null,
        		null,
        		null, 
        		null, 
        		null,
        		null, 
        		null, 
        		null);
        
        UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
        OrderDto order = new OrderDto();//为了公用记账方法,构造订单对象
        order.setOrderTitle("代理费返还");
        order.setSettlePrice(slottingFee);
        //增加用户流水①币
        userBillService.insertUserBill(userId,
        		null,
        		CommonConst.BILL_DIRECTION_ADD,
        		dRebatesDayMoney, userAccount.getRewardAmount(), 
        		NumberUtil.sub(userAccount.getAmount(), dRebatesDayMoney), order, "3721计划", 
        		CommonConst.PAY_TYPE_SINGLE, "3721代理返利", 
        		CommonConst.USER_BILL_TYPE_AGENT_REBATE,
        		CommonConst.USER_ACCOUNT_TYPE_REWARD,
        		agentId);
        
        // 平台出帐，只记录一条，在描述中区分12币的值
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, dRebatesDayMoney,
                CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, null, "支付代理费返还",
                "3721支付代理费返还，返还年比例：" + dRebatesRatioByYear, CommonConst.PLATFORM_BILL_TYPE_BACK_AGENT,
                CommonConst.PLATFORM_BILL_STATUS_OVER);
    }	
	
    private void agentAccountCommission(OrderDto order, Map<String, Object> configMap, UserDto user,
            Double slottingFee, Integer userBillType) throws Exception {
        if (user == null || !("middle_ratio").equals(user.getRebatesLevel())) {
            return;
        }
        //计算平台税率
        Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        Long userId = user.getUserId();
        // 1币收益(现钱)
        Object legendRatioConfig = configMap.get("a_refer_d_commission_money1");
        Double legendRatio = legendRatioConfig == null ? 0 : Double.valueOf(legendRatioConfig.toString());
        Double legendAmount = NumberUtil.multiply(slottingFee, legendRatio, (1 - platformTaxDeductionRatio));

        // 消费金收益（对应3币），之前方案为消费币，后来有调整
        Object couponRatioConfig = configMap.get("a_refer_d_commission_money2");
        Double couponRatio = couponRatioConfig == null ? 0 : Double.valueOf(couponRatioConfig.toString());
        Double couponAmount = NumberUtil.multiply(slottingFee, couponRatio, (1 - platformTaxDeductionRatio));

        // 总收入
        Double changeAmount = NumberUtil.add(legendAmount, couponAmount);

        // 扣减收益循环计数值,每达到上限值需减去上限值,普通C不计入
        Double deductionCountValue = changeAmount;

        userAccountDao.updateUserAccount(userId, changeAmount, legendAmount, legendAmount, couponAmount, null, legendAmount,
                null, null, null, null, deductionCountValue, null, null, null, couponAmount);

        UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
        // 账单前的账户总额
        Double amount = NumberUtil.sub(userAccount.getAmount(), changeAmount);
        order.setOrderTitle("推荐代理商奖励");
        order.setSettlePrice(slottingFee);
        // 记录用户账单
        userBillService.insertUserBill(userId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, legendAmount,
                userAccount.getRewardAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                "3721推荐代理商奖励-平台奖励", userBillType, CommonConst.USER_ACCOUNT_TYPE_REWARD,
                null);

        userBillService.insertUserBill(userId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, couponAmount,
                userAccount.getConsumeAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                "3721推荐代理商奖励-平台奖励", userBillType, CommonConst.USER_ACCOUNT_TYPE_MONETARY,
                null);

        String platformBillDesc = "3721推荐代理-支付推荐代理商奖励, 其中现钱、消费金值为：" + legendAmount + CommonConst.COMMA_SEPARATOR + couponAmount;

        // 平台出帐，只记录一条，在描述中区分12币的值
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, changeAmount,
                CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, null, "支付推荐代理商奖励", platformBillDesc, CommonConst.PLATFORM_BILL_TYPE_REFER_AGENT,
                CommonConst.PLATFORM_BILL_STATUS_OVER);
        
        
        //平台税收入账 
        Double taxDeductionMoney = NumberUtil.multiply(slottingFee, NumberUtil.add(couponRatio, legendRatio), platformTaxDeductionRatio);
        if(taxDeductionMoney > 0) {
            // 平台记账，税金只记一条账单
            platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxDeductionMoney,
                    null, order, null, "收取返利税金",
                    "3721-推荐代理奖励平台扣税", CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                    CommonConst.PLATFORM_BILL_STATUS_OVER);
        }
        
        SmsReplaceContent pushReplaceContent = new SmsReplaceContent();
        changeAmount = changeAmount == null ? 0 : changeAmount;
        pushReplaceContent.setMoney(NumberUtil.fmtDouble(changeAmount, 4));
        pushReplaceContent.setUsage("recommend_agent_rebate");
        pushUserMsg("recommend_agent_rebate", userId, 0, pushReplaceContent);
    }

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.common.ICommonService#checkForRebateForShopRegister(com.idcq.appserver.dto.shop.ShopDto)
	 */
	@Override
	public void checkForRebateForShopRegister(ShopDto shopDto) throws Exception {
		Double buyvMoney = shopDto.getBuyvMoney();//累计购买金额
		Double totalFee = shopDto.getTotalFee();//本次购买金额
		if(totalFee != null && totalFee != 0D){
			buyvMoney = totalFee;
		}
        
        Long shopId = shopDto.getShopId();
        Date curTime = new Date();
        //如果是红店或者绿店并且buyvmoney大于0，则考虑计算返点
        if((1 == shopDto.getShopIdentity() || 2 == shopDto.getShopIdentity())  && buyvMoney != null && buyvMoney > 0)
        {
            ShopRebatesDto condition = new ShopRebatesDto();
            condition.setShopId(shopDto.getShopId());
            List<ShopRebatesDto> items = shopRebatesDao.getShopRebates(condition);
            if(null != items && items.size() > 0)
            {
                logger.info("该店铺已完成返点，不再进行相关操作");
                return;
            }

            /* 自身返点 */
            Map<String, Object> configMap = getConfigByGroup("GROUP_3721");
            condition.setBuyvMoney(buyvMoney);
            condition.setShopWaitRebatesMoney(shopDto.getBuyvMoney());
            condition.setCreateTime(curTime);
            condition.setOrderId(shopDto.getOrderId());
            this.shopBuyvMoneyRebates(condition, true, shopDto.getPrincipalId(), configMap);

            /* 店铺推荐奖励分成 */
            Long referUserId = shopDto.getReferUserId();
            if(null == referUserId || "0".equals(referUserId + "")){
                return;
            }
            UserDto referUserDto = userDao.getUserById(referUserId);
            if(!"middle_ratio".equals(referUserDto.getRebatesLevel()))
            {
                return;
            }
           
            //计算平台税率
            Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
            Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
            Object rewardRatioConfig = configMap.get("a_refer_b_commission_money1");
            Double rewardRatio = rewardRatioConfig == null ? 0 : Double.valueOf(rewardRatioConfig.toString());
            Object couponRatioConfig = configMap.get("a_refer_b_commission_money2");
            Double couponRatio = couponRatioConfig == null ? 0 : Double.valueOf(couponRatioConfig.toString());
            
            logger.info("开始向店铺推荐人返点：" + referUserId);
            logger.info("购买v产品金额：" + buyvMoney);
            logger.info("配置参数===》平台扣税：" + platformTaxDeductionRatio.toString());
            logger.info("配置参数===》a_refer_b_commission_money1：" + rewardRatio);
            logger.info("配置参数===》a_refer_b_commission_money2：" + couponRatioConfig);

            
            //1币现钱
            Double rewardAmountAdd = NumberUtil.multiply(buyvMoney, rewardRatio, (1 - platformTaxDeductionRatio));
            //3币消费金
            Double couponAmountAdd = NumberUtil.multiply(buyvMoney, couponRatio, (1 - platformTaxDeductionRatio));
            //总收益
            Double changeAmount = NumberUtil.add(rewardAmountAdd, couponAmountAdd);
            
            userAccountDao.updateUserAccount(referUserId, changeAmount, rewardAmountAdd, rewardAmountAdd, couponAmountAdd, null, rewardAmountAdd, null, null, null, null, null, null, null, null, couponAmountAdd);
          
            UserAccountDto userAccountDto = userAccountDao.getAccountMoney(referUserId);
            
            //开始插入相关用户账单
            UserBillDto userBillDto = new UserBillDto();
            logger.info("========orderId:"+shopDto.getOrderId());
            userBillDto.setOrderId(shopDto.getOrderId());
            userBillDto.setUserId(referUserId);
            userBillDto.setUserRole("中级经销商");
            userBillDto.setBillType("3721计划");
            userBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
            //状态为已结算
            userBillDto.setBillStatus(41);
            userBillDto.setAccountAmount(NumberUtil.sub(userAccountDto.getAmount(), changeAmount));
            userBillDto.setAccountAfterAmount(userAccountDto.getRewardAmount());
            userBillDto.setCreateTime(curTime);
            userBillDto.setSettleTime(curTime);
            userBillDto.setBillDesc("中级经销商推荐商铺购买产品返利");
            userBillDto.setBillTitle("推荐店铺返利");
            userBillDto.setBillStatusFlag(0);
            //推荐店铺奖励
            userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_SHOP_REWARD);
            //平台奖励
            userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_REWARD);
            userBillDto.setMoney(rewardAmountAdd);
            //插入1币账单
            userBillDao.insertUserBill(userBillDto);
            
            userBillDto.setBillId(null);
            userBillDto.setMoney(couponAmountAdd);
            userBillDto.setAccountAfterAmount(userAccountDto.getCouponAmount());
            userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);

            //插入3币账单
            userBillDao.insertUserBill(userBillDto);
            SmsReplaceContent smsReplaceContent = new SmsReplaceContent();
            
            smsReplaceContent.setAmount(changeAmount);
            smsReplaceContent.setUsername("商家");
            try
            {
                this.pushUserMsg("recommendReward",referUserId, 0,smsReplaceContent);
            }catch (Exception e){
                logger.info("审核商铺时推送给中级经销商信息异常",e);
            }

            //开始插入平台账单
            PlatformBillDto platformBillDto = new PlatformBillDto();
            platformBillDto.setOrderId(shopDto.getOrderId());
            platformBillDto.setBillDesc("中级经销商推荐商铺购买产品获得现钱、消费金奖励，对应金额："+ rewardAmountAdd + CommonConst.COMMA_SEPARATOR + couponAmountAdd);
            platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
            platformBillDto.setCreateTime(curTime);
            platformBillDto.setSettleTime(curTime);
            platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
            platformBillDto.setMoney(-changeAmount);
            platformBillDto.setMoneySource(CommonConst.MONEY_SOURCE_CQB);
            platformBillDto.setBillType("支付推荐商铺奖励");
            //支付推荐商铺奖励
            platformBillDto.setPlatformBillType(6);
            platformBillDto.setShopId(shopId);
            logger.info("支付推荐商铺奖励：money="+-changeAmount);

            platformBillDao.insertPlatformBill(platformBillDto);
            //平台税金收入 
            Double taxDeductionMoney = NumberUtil.multiply(buyvMoney, NumberUtil.add(rewardRatio, couponRatio), platformTaxDeductionRatio);
            if(taxDeductionMoney > 0) {
                platformBillDto.setBillId(null);
                platformBillDto.setBillType("收取返利税金");
                platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION);
                platformBillDto.setBillDesc("3721-推荐商铺返利平台扣税");
                platformBillDto.setMoney(taxDeductionMoney);
                logger.info("收取返利税金="+taxDeductionMoney);
                platformBillDao.insertPlatformBill(platformBillDto);
            }
            OrderDto order = new OrderDto();
            order.setOrderId(shopDto.getOrderId());
            order.setUserId(shopDto.getPrincipalId());
            order.setSettlePrice(buyvMoney);
            //组长管理费处理
            manageMoneySettle(order, referUserId, false, configMap);
           
            //分公司管理费处理
            settlefilialUser(buyvMoney, shopDto, configMap);
            
            logger.info("店铺返利完成");
            
        }
    }
	/**
	 * 管理费结算
	 * 
	 * @param money 组长的管理费
	 * @param userId 用户id
	 * @param referUserId 推荐人id
	 * @param newAgent 是否需要更新推荐经销人个数
	 * @throws Exception
	 *
	 */
	public void manageMoneySettle(OrderDto order, Long referUserId, boolean newAgent, Map configMap) throws Exception {
		logger.info("管理费结算开始-start");
		if(null == order || order.getSettlePrice() == null) {
		    return;
		}
		Double money = order.getSettlePrice();
		UserDto referUser = userDao.getUserById(referUserId); 
		if(newAgent) //如果推荐人是经销商而且是新增经销商
        {
            //开始处理直接推荐人
		    Object groupLeaderNumConfig = configMap.get("group_leader_refer_num");
		    Integer transformNumber = groupLeaderNumConfig == null ? 0 : Integer.valueOf(groupLeaderNumConfig.toString());
            UserDto updateUser = new UserDto();
            if ((!referUser.getIsGroupLeader()) && (
                    referUser.getReferAgentNum() + referUser.getReferAgentNum2() + 1 >= transformNumber))
            {   //满足条件，转变为组长
                logger.info("更新为组长");
                updateUser.setIsGroupLeader(true);
            }
            updateUser.setUserId(referUser.getUserId());
            updateUser.setReferAgentNum(1);
            updateUser.setReferAgentNum2(0);
            userDao.updateUser(updateUser);
            DataCacheApi.del(CommonConst.KEY_USER + referUser.getUserId());

            //开始处理间接推荐人
            Long referUserId2 = referUser.getReferUserId();
                
            UserDto referUser2 = userDao.getUserById(referUserId2);
            if(null != referUser2 && !"normal_ratio".equals(referUser2.getRebatesLevel()))
            {       //如果是经销商
                updateUser = new UserDto();
                if ((!referUser2.getIsGroupLeader()) && (
                        referUser2.getReferAgentNum() + referUser2.getReferAgentNum2() + 1 >= transformNumber))
                {       //查看是否可以转化组长
                    logger.info("更新为组长");
                    updateUser.setIsGroupLeader(true);
                }
                updateUser.setUserId(referUser2.getUserId());
                updateUser.setReferAgentNum(0);
                updateUser.setReferAgentNum2(1);
                userDao.updateUser(updateUser);
                DataCacheApi.del(CommonConst.KEY_USER + referUser2.getUserId());
            }
        }
	  
	    if (null == referUser || "normal_ratio".equals(referUser.getRebatesLevel()) || !referUser.getIsGroupLeader()) {
	        return;
	    }
	    Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        Object groupLeaderConfig = configMap.get("group_leader_ratio");
        Double groupLeaderRatio = groupLeaderConfig == null ? 0 : Double.valueOf(groupLeaderConfig.toString());
        
        //管理费金额
        Double rewardMoney = NumberUtil.multiply(money, groupLeaderRatio, (1 - platformTaxDeductionRatio));
        //平台税金收入 
        Double taxDeductionMoney = NumberUtil.multiply(money, groupLeaderRatio, platformTaxDeductionRatio);
	    
		order.setOrderTitle("组长收益");
		order.setSettlePrice(rewardMoney);
		UserAccountDto userAccount = userAccountDao.getAccountMoney(referUserId);
		// 账单后账户总额
		Double amount = NumberUtil.add(userAccount.getAmount(),
				rewardMoney);
		// 处理后奖励总额
		Double accountAfterAmount = NumberUtil.add(
				userAccount.getRewardAmount(), rewardMoney);
		//更新账户
        userAccountDao.updateUserAccount(referUserId, rewardMoney, rewardMoney, rewardMoney, null, null, null,
                null, null, null, null, null, null, null, null);
        
		// 记录用户账单
		userBillService.insertUserBill(referUserId, null,
				CommonConst.BILL_DIRECTION_ADD, rewardMoney,
				accountAfterAmount, amount, order, "3721计划",
				CommonConst.PAY_TYPE_SINGLE, "3721组长收益"
						+ rewardMoney,
				CommonConst.USER_BILL_TYPE_HEADMAN_REBATE,
				CommonConst.USER_ACCOUNT_TYPE_REWARD, null);

		// 平台账单
		logger.debug("开始插入平台账单");
		PlatformBillDto platformBillDto = new PlatformBillDto();
		platformBillDto.setBillDesc("组长收益");
		platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
		platformBillDto.setCreateTime(new Date());
		platformBillDto.setSettleTime(new Date());
		platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
		platformBillDto.setMoney(-rewardMoney);
		platformBillDto.setMoneySource(CommonConst.MONEY_SOURCE_CQB);
		platformBillDto.setPlatformBillType(CommonConst.USER_BILL_TYPE_HEADMAN_REBATE);
		platformBillDto.setConsumerUserId(order.getUserId());
		platformBillDto.setConsumerMobile(null);
		platformBillDto.setBillType("组长收益");
		platformBillDto.setOrderId(order.getOrderId());
		platformBillDao.insertPlatformBill(platformBillDto);
		if(taxDeductionMoney > 0) {
            platformBillDto.setBillId(null);
            platformBillDto.setBillType("收取返利税金");
            platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION);
            platformBillDto.setBillDesc("3721-组长收益平台扣税");
            platformBillDto.setMoney(taxDeductionMoney);
            logger.info("收取返利税金="+taxDeductionMoney);
            platformBillDao.insertPlatformBill(platformBillDto);
        }
		logger.info("管理费结算开始-end");

	}

    private void settlefilialUser(Double buyvMoney, ShopDto shop, Map configMap) throws Exception {
        if (shop == null) {
            return;
        }
        BranchOfficeDto searchCondition = new BranchOfficeDto();
        searchCondition.setCityId(shop.getCityId());
        List<BranchOfficeDto> branchOfficeDtos = branchOfficeDao.searchBranchOfficeByCondition(searchCondition);
        if(CollectionUtils.isEmpty(branchOfficeDtos)) {
            return;
        }
        Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        Object branchOfficeConfig = configMap.get("branch_office_ratio");
        Double branchOfficeRatio = branchOfficeConfig == null ? 0 : Double.valueOf(branchOfficeConfig.toString());
        
        //管理费金额
        Double rewardMoney = NumberUtil.multiply(buyvMoney, branchOfficeRatio, (1 - platformTaxDeductionRatio));
        //平台税金收入 
        Double taxDeductionMoney = NumberUtil.multiply(buyvMoney, branchOfficeRatio, platformTaxDeductionRatio);
        OrderDto order = new OrderDto();
        order.setOrderTitle("公司管理费补贴");
        order.setUserId(shop.getPrincipalId());
        String orderId = shop.getOrderId();
        order.setOrderId(orderId);
        for (BranchOfficeDto branchOfficeDto : branchOfficeDtos) {
            Long userId = branchOfficeDto.getUserId();
            UserDto user = userDao.getUserById(userId);
            if(user == null || "normal_ratio".equals(user.getRebatesLevel())) {
                continue;
            }
            order.setSettlePrice(rewardMoney);

            UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
            // 账单后账户总额
            Double amount = NumberUtil.add(userAccount.getAmount(),
                    rewardMoney);
            // 处理后奖励总额
            Double accountAfterAmount = NumberUtil.add( 
                    userAccount.getRewardAmount(), rewardMoney); 
            //更新账户
            userAccountDao.updateUserAccount(userId, rewardMoney, rewardMoney, rewardMoney, null, null, null,
                    null, null, null, null, null, null, null, null);

            // 记录用户账单
            userBillService.insertUserBill(userId, null,
                    CommonConst.BILL_DIRECTION_ADD, rewardMoney,
                    accountAfterAmount, amount, order, "3721计划",
                    CommonConst.PAY_TYPE_SINGLE, "3721分公司管理费补贴"
                            + rewardMoney,
                    CommonConst.USER_BILL_TYPE_COMPANY_REBATE,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD, null);

            // 平台账单
            logger.debug("开始插入平台账单");
        
            PlatformBillDto platformBillDto = new PlatformBillDto();
            platformBillDto.setBillDesc("分公司管理费补贴");
            platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
            platformBillDto.setCreateTime(new Date());
            platformBillDto.setSettleTime(new Date());
            platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
            platformBillDto.setMoney(-rewardMoney);
            platformBillDto.setMoneySource(CommonConst.MONEY_SOURCE_CQB);
            platformBillDto.setPlatformBillType(CommonConst.USER_BILL_TYPE_COMPANY_REBATE);
            platformBillDto.setConsumerUserId(shop.getPrincipalId());
            platformBillDto.setConsumerMobile(null);
            platformBillDto.setBillType("分公司管理费补贴");
            platformBillDto.setOrderId(orderId);
            platformBillDao.insertPlatformBill(platformBillDto);
            if(taxDeductionMoney > 0) {
                platformBillDto.setBillId(null);
                platformBillDto.setBillType("收取返利税金");
                platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION);
                platformBillDto.setBillDesc("3721-分公司管理费补贴平台扣税");
                platformBillDto.setMoney(taxDeductionMoney);
                logger.info("收取返利税金="+taxDeductionMoney);
                platformBillDao.insertPlatformBill(platformBillDto);
            }
        }
       
    }



    public void shopBuyvMoneyRebates(ShopRebatesDto shopRebatesDto, boolean insert, Long principalId, Map configMap) throws Exception{
        Date curTime = new Date();
        Long shopId = shopRebatesDto.getShopId();
        Double buyvMoney = shopRebatesDto.getBuyvMoney();
        Double waitToRebate = shopRebatesDto.getShopWaitRebatesMoney();
        //返点比利
        Double rebateRate = shopRebatesDto.getbBuyvRebatesRatio();
        if(rebateRate == null)
        {
            Object rebateRateConfig = configMap.get("b_buy_v_rebates_ratio");
            rebateRate = rebateRateConfig == null ? 0 : Double.valueOf(rebateRateConfig.toString());
            shopRebatesDto.setbBuyvRebatesRatio(rebateRate);
        }
        
        //最终返点金额,购买平台产品 * 返点比例  店铺购买产品返还不扣税
        Double preTaxRebated = NumberUtil.multiply(buyvMoney, rebateRate);
        //判断是否返点结束
        if(preTaxRebated >= waitToRebate)
        {   //返点数大于待返点，则结束该定时任务
            preTaxRebated = waitToRebate;
            waitToRebate = 0d;
            //未完成
            shopRebatesDto.setIsFinish(1);
        }else {
            shopRebatesDto.setIsFinish(0);
            waitToRebate = NumberUtil.sub(waitToRebate, preTaxRebated);
        }
        shopRebatesDto.setLastUpdateTime(new Date());
        shopRebatesDto.setShopWaitRebatesMoney(waitToRebate);
        if(insert)
        {
            ShopRebatesDto parm = new ShopRebatesDto();
            parm.setShopId(shopId);
            List<ShopRebatesDto> rebates = shopRebatesDao.getShopRebates(parm);
            if(CollectionUtils.isEmpty(rebates)){
                shopRebatesDao.insertShopRebates(shopRebatesDto);
            }
            else{
                shopRebatesDao.updateShopRebates(shopRebatesDto);
            }
        }else {
            shopRebatesDao.updateShopRebates(shopRebatesDto);
        }
        //更新shop_account表
        shopAccountDao.updateShopAccount(shopId, preTaxRebated, null, preTaxRebated, preTaxRebated, null, null, null,null,null, preTaxRebated);

        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopRebatesDto.getShopId());
        logger.debug("开始插入店铺账单");
        //插入shop_bill账单
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setCreateTime(curTime);
        shopBillDto.setShopId(shopId);
        shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
        shopBillDto.setMoney(preTaxRebated);
        //账单类型为购买产品返利
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_CONSUME_V_REBATE);
        shopBillDto.setPlatformTotalIncomePrice(buyvMoney);
        shopBillDto.setPayAmount(buyvMoney);
        shopBillDto.setSettleTime(curTime);
        //账单为平台奖励
        shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_REWARD);
        shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        shopBillDto.setBillDesc("购买产品返利");
        shopBillDto.setAccountAmount(NumberUtil.sub(shopAccountDto.getAmount(), preTaxRebated));
        shopBillDto.setAccountAfterAmount(shopAccountDto.getRewardAmount());
        String mobile = userDao.getMobileByUserId(principalId);
        shopBillDto.setSettlePrice(buyvMoney);
        shopBillDto.setConsumerUserId(principalId);
        shopBillDto.setConsumerMobile(mobile);
        shopBillDto.setBillTitle("购买产品返利");
        shopBillDto.setOrderId(shopRebatesDto.getOrderId());
        shopBillDao.insertShopBill(shopBillDto);
        logger.debug("插入店铺账单");

        logger.debug("开始插入平台账单");
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillDesc("购买产品返点");
        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
        platformBillDto.setCreateTime(curTime);
        platformBillDto.setSettleTime(curTime);
        platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBillDto.setMoney(-preTaxRebated);
        platformBillDto.setMoneySource(CommonConst.MONEY_SOURCE_CQB);
        platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_BUY_V_BACK);
        platformBillDto.setShopId(shopId);
        platformBillDto.setConsumerUserId(principalId);
        platformBillDto.setConsumerMobile(mobile);
        platformBillDto.setBillType("购买产品返点");
        platformBillDto.setOrderId(shopRebatesDto.getOrderId());
        platformBillDao.insertPlatformBill(platformBillDto);
        logger.debug("完成插入平台账单");
    }

}
