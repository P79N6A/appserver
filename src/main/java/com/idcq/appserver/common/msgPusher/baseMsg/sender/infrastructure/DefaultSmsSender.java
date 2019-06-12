package com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.msgPusher.baseMsg.enums.MsgTargetCategory;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.MsgSender;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.SendCallBack;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.common.ISendSmsRecordDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.HttpClientUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.HttpSender;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.wxscan.MD5Util;

/**
 * 默认短信发送设施,该处理器会做默认的日志记录功能，即向1dcq_send_sms_record插入记录，但是务必保证{@link PushMsg}
 * 中的contextInfos中包含以下信息（如果该信息是必须的）： 1.veriCode 2.usage
 * @author Administrator
 *
 */
@Component(value = "SMSSENDER")
public class DefaultSmsSender implements MsgSender
{
    private static Logger logger = LoggerFactory.getLogger(DefaultSmsSender.class);

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ISendSmsRecordDao sendSmsRecordDao;

    @Autowired
    private ICommonDao commonDao;

    public String getSmsChannel(boolean isFirst) throws Exception
    {
        logger.info("===================>>获取短信发送通道-start,isFirst=" + isFirst);
        String value = "";
        // 从内存中获取短信发送的通道
        Properties properties = ContextInitListener.COMMON_PROPS;
        if (null != properties)
        {
            String tmpValue = properties.getProperty(CommonConst.KEY_SMS_CHANNEL);
            if (!StringUtils.isBlank(tmpValue))
            {
                String[] keys = tmpValue.split(",");
                if (keys.length > 1)
                {
                    if (isFirst)
                    {
                        value = keys[0];
                    }
                    else
                    {
                        value = keys[1];
                    }
                }
                else
                {
                    value = keys[0];
                }
            }
        }
        if (StringUtils.isBlank(value))
        {
            value = CommonConst.SMS_MD_CHANNEL_KEY;// 默认设置为漫道科技通道
        }
        logger.info("===================>>当前发送短信使用的通道为：" + value);
        return value;
    }

    public Map<String, Object> sendSmsMobileCode(PushMsg pushMsg) throws Exception
    {
        // 会员校验
        // userExistsFlag(replaceContent);

        // 获取短信发送通道
        String channel = getSmsChannel(true);
        // 获取短信发送参数
        Map<String, Object> smsParams = getSmsParams(channel, pushMsg);
        Map<String, String> formParams = (Map<String, String>) smsParams.get("formParams");
        String url = (String) smsParams.get(CommonConst.SMS_URL);
        // String content = (String) smsParams.get("content");
        String content = new String(pushMsg.getContent(), "UTF-8");
        // 提交参数至第三方短信平台
        HttpClientUtils httpUtil = HttpClientUtils.getInstance();
        String responseStr = null;
        if (CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
        {
            String account = (String) smsParams.get(CommonConst.SMS_SN);
            String pswd = (String) smsParams.get(CommonConst.SMS_PWD);
            // 调用创蓝接口
            responseStr = HttpSender.batchSend(url, account, pswd, formParams.get("mobile"), content, true, null, null);
        }
        else
        {
            try
            {
                responseStr = httpUtil.doPost(url, formParams);
            }
            catch (Exception e)
            {
                responseStr = "error";
            }

        }
        // 解析返回结果
        Map<String, Object> resultMap = httpUtil.analyticResult(responseStr, channel);
        // 记录发送的短信内容
        // saveSendSmsRecord(replaceContent,content,resultMap,channel);
        // boolean resultFlag = httpUtil.isSendSuccess(sendStatus);
        // if (resultFlag && replaceContent.isCreateCodeFlag() &&
        // replaceContent.isCacheCodeFlag()) {
        // //缓存短信验证码
        // cacheCode(replaceContent);
        // }
        return resultMap;
    }

    /**
     * 缓存验证码
     * @param MobileService
     * @param usage
     * @param replaceContent
     * @throws Exception
     */
    private void cacheCode(SmsReplaceContent replaceContent) throws Exception
    {
        replaceContent.setSendTime(new Date().getTime());
        // 如果发送成功，则将短信验证码存入缓存中
        String mKey = CommonConst.REDIS_VERICODE_OBJ + replaceContent.getMobile();
        // TODO +":"+usage; 后续需要增加场景
        // 获取短信验证码缓存有效时间，单位秒
        int cacheTime = getSettingValue(CommonConst.SETTING_CODE_MOBILE_CODE, CommonConst.SETTING_KEY_MOBILE_CODE);
        logger.info("短信验证码缓存有效时间：" + cacheTime);
        DataCacheApi.setObjectEx(mKey, replaceContent, cacheTime);
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
    private Map<String, Object> getSmsParams(String channel, PushMsg pushMsg)
            throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException
    {
        Map<String, Object> smsParams = new HashMap<String, Object>();
        String tmpKey = channel;
        // boolean subFlag = false;
        boolean isAesDecrypt = true; // 是否需要解密
        if (CommonConst.SMS_MD_CHANNEL_KEY.equals(channel))
        {
            tmpKey = tmpKey + "_";
        }
        else if (CommonConst.SMS_CL_CHANNEL_KEY.equals(channel))
        {
            if (MsgTargetCategory.SMS_SEO.equals(pushMsg.getMsgTargetCategory()))
            {
                // 使用创蓝的营销短信通道
                tmpKey = tmpKey + "_sem" + "_";
            }
            else
            {
                tmpKey = tmpKey + "_";
            }
            isAesDecrypt = false;
            // subFlag = true;
        }
        else
        {
            tmpKey = "";// 志晴平台默认为空
            // subFlag = true;
        }
        String url = getSmsValueByKey(tmpKey + CommonConst.SMS_URL, isAesDecrypt);
        String sn = getSmsValueByKey(tmpKey + CommonConst.SMS_SN, isAesDecrypt);
        String pwd = getSmsValueByKey(tmpKey + CommonConst.SMS_PWD, isAesDecrypt);
        String content = new String(pushMsg.getContent(),"UTF-8");
        // String usage =
        // replaceContent.getUsage()+replaceContent.getUsageFlag();
        // if
        // (CommonConst.MSG_SETTING_KEY_SMS_PWD.equals(replaceContent.getUsage()))
        // {
        // content = getSmsContentByKey(replaceContent.getUsage());
        // }else{
        /*
         * if (CommonConst.SMS_CONTENT_KEYS.contains(usage)) { content =
         * getSmsValueByKey(usage,false); }
         */
        // content = getSmsValueByKey(usage,false);
        // if (null == content) {
        // content = getSmsValueByKey(CommonConst.SMS_GENERAL,false);
        // }
        // }
        // if (null == url || sn == null || pwd == null || content == null) {
        // throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
        // "没有找到短信发送相关参数信息");
        // }
        // 将模板中的code替换为真实的code
        // content = doContent(content, replaceContent);
        // if(subFlag){
        // //需要去掉签名
        // content = content.replace(CommonConst.SMS_CONTENT_SIGN, "");
        // }
        String mobile = "";
        for (String str : pushMsg.getTargetId())
        {
            mobile = mobile + str;
        }
        ;
        // 封装表单参数
        Map<String, String> formParams = encapSmsParams(sn, pwd, new String(pushMsg.getContent(), "UTF-8"), mobile, channel);
        smsParams.put("formParams", formParams);
        smsParams.put(CommonConst.SMS_URL, url);
        smsParams.put("content", content);
        smsParams.put(CommonConst.SMS_SN, sn);
        smsParams.put(CommonConst.SMS_PWD, pwd);
        return smsParams;
    }

    /**
     * 保存短信发送记录
     * @param replaceContent
     * @param content
     * @param sendStatus
     * @param sendChannle
     * @return
     */
    private int saveSendSmsRecord(SmsReplaceContent replaceContent, String content, Map resultMap, String sendChannle)
    {
        String usage = replaceContent.getUsage() + replaceContent.getUsageFlag();
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
        try
        {
            // 记录之前，先将之前的标记为过期
            sendSmsRecordDao.updateSendStatusExpire(dto.getSendMobile());
            re = sendSmsRecordDao.saveSendSmsRecord(dto);
        }
        catch (Exception e)
        {
            logger.warn("记录短信异常", e);
        }
        return re;
    }

    private int saveSendSmsRecord(String veriCode, String content, Map resultMap, String sendChannle, String usage,
            String mobile)
    {
        SendSmsRecordDto dto = new SendSmsRecordDto();
        dto.setSendUsage(usage);
        dto.setSendCode(veriCode);
        dto.setSendContent(content);
        dto.setSendMobile(mobile);
        dto.setSendChannle(sendChannle);
        dto.setSendStatus((Integer) resultMap.get("sendStatus"));
        dto.setMsgId(resultMap.get("msgId") + "");
        dto.setSendTime(new Date());
        int re = 0;
        try
        {
            // 记录之前，先将之前的标记为过期
            sendSmsRecordDao.updateSendStatusExpire(dto.getSendMobile());
            re = sendSmsRecordDao.saveSendSmsRecord(dto);
        }
        catch (Exception e)
        {
            logger.warn("记录短信异常", e);
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

    /**
     * 获取短信发送参数
     * @param key
     * @param isAesDecrypt
     * @return
     */
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

    /**
     * 获取短信验证码缓存时间
     * @param code
     * @param key
     * @return
     * @throws Exception
     */
    private int getSettingValue(String code, String key) throws Exception
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

    /**
     * php后台发送经销商、代理商账号密码，查询短信内容模板key
     * @param key
     * @return
     */
    /*
     * private String getSmsContentByKey(String key) { String content = null;
     * try { Map contMap = commonDao.getMsgSettingContent(key); if (null !=
     * contMap && contMap.size() > 0) { Integer remandFlag =
     * CommonValidUtil.isEmpty(contMap.get("remand_flag")) ? null :
     * Integer.parseInt(contMap.get("remand_flag") + ""); if (null != remandFlag
     * && remandFlag.intValue() != 1) { throw new
     * ValidateException(CodeConst.CODE_PHP_REMAND_FLAG_CLOSE, "提醒已经关闭"); }
     * content = CommonValidUtil.isEmpty(contMap.get("msg_content")) ? null :
     * (contMap.get("msg_content") + ""); } } catch (Exception e) {
     * logger.error("获取短信平台参数异常", e); } return content; }
     */

    public boolean sendSmsAndSave(String mobile, String content, String usage)
    {
        try
        {
            // 获取短信发送通道，每个通道需要根据不同的key取相对应的url等参数
            String channel = getSmsChannel(true);
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
            // 保存
            SendSmsRecordDto dto = new SendSmsRecordDto();
            dto.setSendUsage(usage);
            dto.setSendContent(content);
            dto.setSendMobile(mobile);
            dto.setSendChannle(channel);
            dto.setSendStatus(sendStatus);
            dto.setSendTime(new Date());
            sendSmsRecordDao.saveSendSmsRecord(dto);
            return httpUtil.isSendSuccess(sendStatus);
        }
        catch (Exception e)
        {
            logger.error("发送短信过程中产生了异常", e);
            return false;
        }
    }

    public boolean checkSmsCodeIsOk(String mobile, String usage, String code, boolean delFalg) throws Exception
    {
        String key = CommonConst.REDIS_VERICODE_OBJ + mobile;
        boolean switchOn = DataCacheApi.switchOn();
        logger.info("redis开关状态：" + switchOn);
        String cacheCode = getCacheCode(key, mobile, usage, switchOn);
        logger.info("请求验证码：" + code + "，缓存验证码：" + cacheCode);
        if (StringUtils.isBlank(cacheCode))
        {
            return false;
        }
        logger.info("请求验证码长度：" + code.length() + "，缓存验证码长度：" + cacheCode.length());
        boolean re = StringUtils.equals(code, cacheCode);
        if (re)
        {
            // 认证成功后，将记录标记为过期
            sendSmsRecordDao.updateSendStatusExpire(mobile);
            if (switchOn && delFalg)
            {
                // 校验成功，且缓存开关为打开状态，将验证码失效
                DataCacheApi.delObject(key);
            }
        }
        return re;
    }

    private String getCacheCode(String key, String mobile, String usage, boolean switchOn) throws Exception
    {
        String cacheCode = null;
        if (switchOn)
        {
            // 如果缓存开关为打开状态，则从缓存获取验证码比较
            Object obj = DataCacheApi.getObject(key);
            if (null == obj)
                return null;
            SmsReplaceContent content = (SmsReplaceContent) obj;
            cacheCode = content.getCode();
            logger.info("redis中获取的验证码：" + cacheCode);
        }
        else
        {
            // 否则从数据库中获取
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("mobile", mobile);
            param.put("usage", usage);
            logger.info("查询验证码参数：" + param);
            // cacheCode = sendSmsRecordDao.getSendCodeByMobileAndUsage(param);
            SendSmsRecordDto ssrDto = sendSmsRecordDao.getSendSmsRecordDtoByMobileAndUsage(param);
            logger.info("数据库中验证码信息：" + ssrDto);
            if (null != ssrDto)
            {
                cacheCode = ssrDto.getSendCode();
                if (!StringUtils.isEmpty(cacheCode))
                {
                    Date sendTime = ssrDto.getSendTime();
                    if (null != sendTime)
                    {
                        long nowTime = new Date().getTime();
                        // 获取验证码有效时间
                        int cacheTime = getSettingValue(CommonConst.SETTING_CODE_MOBILE_CODE,
                                CommonConst.SETTING_KEY_MOBILE_CODE);
                        logger.info("短信验证码缓存有效时间：" + cacheTime);
                        if ((nowTime - sendTime.getTime()) > cacheTime * 1000)
                        {
                            cacheCode = null;
                        }
                    }
                }
            }
            logger.info("数据库中获取的验证码：" + cacheCode);
        }
        return cacheCode;
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

    @Override
    public void sendMsg(PushMsg pushMsg)
    {
        Map<String, Object> sendRs = null;
        Integer code = 500;
        String msgId = null;
        String infoMsg = null;
        Exception er = null;
        try
        {
            sendRs = this.sendSmsMobileCode(pushMsg);
            code = (int) sendRs.get("sendStatus");
            code = code == 0 ? 0 : 400;
            infoMsg = (String) sendRs.get("infoMsg");
            msgId = (String) sendRs.get("msgId");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            er = e;
            infoMsg = "系统异常";
        }
        // 保存消息记录，该处为兼容暂时处理
        if (null == er)
        {
            String mobiles = Arrays.toString(pushMsg.getTargetId());
            Map<String, Object> contextInfos = pushMsg.getContextInfos();
            contextInfos = contextInfos == null ? new HashMap<String, Object>() : contextInfos;
            try
            {
                this.saveSendSmsRecord((String) contextInfos.get("veriCode"), new String(pushMsg.getContent(),"UTF-8"), sendRs,
                        this.getSendChannel(null).toString(), (String) contextInfos.get("usage"),
                        mobiles.substring(1, mobiles.length() - 1));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        /*
         * 注意这里不使用传过来的默认的回调，
         */
        SendCallBack call = pushMsg.getSendCallBack();
        if (null != call) // 如果有回调，则封闭处理结果，并调用回调
        {
            HttpClientUtils httpUtil = HttpClientUtils.getInstance();
            // 封装处理结果
            PushResult rs = new PushResult();
            rs.setCode(code);
            rs.setSource(pushMsg);
            rs.setE(er);
            rs.setInfoMsg(infoMsg);
            rs.setMsgId(msgId);
            boolean success = httpUtil.isSendSuccess(code);
            rs.setSuccess(success);
            if (success)
            {
                call.onSuccess(rs);
            }
            else
            {
                call.onFailed(rs);
            }
        }
    }

    @Override
    public InfrastructureType getSendChannel(String code)
    {
        return InfrastructureType.CL;
    }


}
