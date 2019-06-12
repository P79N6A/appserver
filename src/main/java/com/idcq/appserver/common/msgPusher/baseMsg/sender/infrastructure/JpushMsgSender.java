package com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.MsgSender;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.SendCallBack;
import com.idcq.appserver.listeners.ContextInitListener;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
/**
 * JPUSH App消息推送设施
 * @author Administrator
 *
 */
@Component(value = "JPUSH")
public class JpushMsgSender implements MsgSender
{
    protected static final Logger log = LoggerFactory.getLogger(JpushMsgSender.class);

    // demo App defined in resources/jpush-api.conf

    // public static final String ALERT = "恭喜，经领导批准给您涨工资啦。。。";
    //
    // public static final String TAG = "tag_api";

    @Override
    public void sendMsg(PushMsg pushMsg)
    {
        Integer code = 500;
        String msgId = null;
        String infoMsg = "系统异常";
        Exception er = null;
        boolean success = false;
        PushResult tempResult = null;
        try
        {
            if (pushMsg.getToAll().intValue() == 1)
            {
                // 向所有app与ios发送
                tempResult = this.pushToAll(new String(pushMsg.getContent(),"UTF-8"));
            }
            else
            {
                tempResult = this.sendPushToTarget(pushMsg.getMsgTargetCategory().name(), pushMsg.getUserType(),
                        pushMsg.getTitle(), new String(pushMsg.getContent(), "UTF-8"), pushMsg.getTargetId());
            }
        }
        catch (APIConnectionException e)
        {   
            er = e;
            log.error("Connection error. Should retry later. ", e);
        }
        catch (APIRequestException e)
        {   
            er = e;
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
        }
        catch (Exception e)
        {
            er = e;
            log.error(e.getMessage(), e);
        }
        finally
        { // 执行回调
            SendCallBack callBack = pushMsg.getSendCallBack();
            if (null != callBack)
            {   
                com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult rs = new com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult();
                if(null != tempResult){ //有发送反馈
                    if(tempResult.isResultOK()){    //发送成功
                        code = 0;
                        infoMsg = "发送成功";
                        msgId = tempResult.msg_id + "";
                        success = true;
                    }else{
                        code = 400;
                        infoMsg = tempResult.getOriginalContent();
                        msgId = tempResult.msg_id + "";
                    }
                }
                rs.setCode(code);
                rs.setE(er);
                rs.setInfoMsg(infoMsg);
                rs.setMsgId(msgId);
                rs.setSource(pushMsg);
                rs.setSuccess(success);
                if (success)
                {
                    callBack.onSuccess(rs);
                }
                else
                {
                    callBack.onFailed(rs);
                }
            }
        }

    }

    /**
     * 消息推送给所有ios设备
     * @param content
     */
    private PushResult pushToIos(String masterSecret, String appKey, int maxRetryTimes, String content) throws Exception
    {
        PushResult result = null;
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        // 生成推送的内容，这里我们先测试全部推送
        PushPayload payload = buildPushObject_all_alias_alert(content);
        result = jpushClient.sendPush(payload);
        return result;
    }

    /**
     * 消息推送给所有android设备
     * @param content
     */
    private PushResult pushToAndroid(String masterSecret, String appKey, int maxRetryTimes, String content)
            throws Exception
    {
        PushResult result = null;
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        // 生成推送的内容，这里我们先测试全部推送
        PushPayload payload = buildPushObject_all_alias_alert(content);
        result = jpushClient.sendPush(payload);
        log.info("Got result - " + result);
        return result;
    }

    /**
     * 消息推送给所有设备
     * 尤其注意这里的失败处理策略（包括异常导致失败或者第三方发送失败），当有一部分推送异常或者其它原因发送失败时，推送将继续进行，在最后，若有异常，则抛出最先发生部分的异常；
     * 如果有其它原因部分发送失败（不是因为{@link Exception}），则返回最先发送失败部分的原因({@link PushResult})
     * @param osInfo 系统类型
     * @param content
     */
    private PushResult pushToAll(String content) throws Exception
    {
        PushResult result = null;
        PushResult tempResult = null;
        Exception er = null;
        Properties props = ContextInitListener.JPUSH_PROPS;
        // 推送给用户
        try
        {
            result = pushToAndroid(props.getProperty("androidSecret"), props.getProperty("androidAppKey"), 3, content);
        }
        catch (Exception e)
        {   
            er = e;
            if (e instanceof APIConnectionException)
            {
                log.error("Connection error. Should retry later. ", e);
            }
            if (e instanceof APIRequestException)
            {
                APIRequestException e1 = (APIRequestException) e;
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.info("HTTP Status: " + e1.getStatus());
                log.info("Error Code: " + e1.getErrorCode());
                log.info("Error Message: " + e1.getErrorMessage());
                log.info("Msg ID: " + e1.getMsgId());
            }
        }
        try
        {   
            tempResult = pushToIos(props.getProperty("iosSecret"), props.getProperty("iosAppKey"), 3, content);
        }
        catch (Exception e)
        {   
            if(null == er){
                er = e;
            }
            if (e instanceof APIConnectionException)
            {
                log.error("Connection error. Should retry later. ", e);
            }
            if (e instanceof APIRequestException)
            {
                APIRequestException e1 = (APIRequestException) e;
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.info("HTTP Status: " + e1.getStatus());
                log.info("Error Code: " + e1.getErrorCode());
                log.info("Error Message: " + e1.getErrorMessage());
                log.info("Msg ID: " + e1.getMsgId());
            }
        }
        
        if(result != null && result.isResultOK() && null != tempResult){
            result = tempResult; 
        }
        
        // 推送给免费商家
        try
        {   
            tempResult = pushToAndroid(props.getProperty("freeSellerAndroidSecret"), props.getProperty("freeSellerAndroidAppKey"), 3,
                    content);
        }
        catch (Exception e)
        {   
            if(null == er){
                er = e;
            }
            if (e instanceof APIConnectionException)
            {
                log.error("Connection error. Should retry later. ", e);
            }
            if (e instanceof APIRequestException)
            {
                APIRequestException e1 = (APIRequestException) e;
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.info("HTTP Status: " + e1.getStatus());
                log.info("Error Code: " + e1.getErrorCode());
                log.info("Error Message: " + e1.getErrorMessage());
                log.info("Msg ID: " + e1.getMsgId());
            }
        }
        if(result != null && result.isResultOK() && null != tempResult){
            result = tempResult; 
        }
        try
        {   
            tempResult = pushToIos(props.getProperty("freeSellerIosSecret"), props.getProperty("freeSellerIosAppKey"), 3, content);
        }
        catch (Exception e)
        {   
            if(null == er){
                er = e;
            }
            if (e instanceof APIConnectionException)
            {
                log.error("Connection error. Should retry later. ", e);
            }
            if (e instanceof APIRequestException)
            {
                APIRequestException e1 = (APIRequestException) e;
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.info("HTTP Status: " + e1.getStatus());
                log.info("Error Code: " + e1.getErrorCode());
                log.info("Error Message: " + e1.getErrorMessage());
                log.info("Msg ID: " + e1.getMsgId());
            }
        }
        if(result != null && result.isResultOK() && null != tempResult){
            result = tempResult; 
        }
        if(null != er){
            throw er;
        }
        return result;
    }

    /**
     * 向指定的设备推送
     * @param osInfo 系统类型
     * @param registrationId
     * @param content
     */
    public PushResult sendPushToTarget(String osInfo, Integer userType, String title, String content,
            String... registrationId) throws Exception
    {
        log.error("开始推送了");
        PushResult result = null;
        JPushClient jpushClient = null;
        Properties props = ContextInitListener.JPUSH_PROPS;
        // 向指定的设备推送
        PushPayload payload = null;
        String platform = osInfo.toLowerCase();
        if (platform.startsWith("ios"))
        {
            if (userType != null && userType.equals(CommonConst.USER_TYPE_TEN))
            {
                jpushClient = new JPushClient(props.getProperty("shopManagerIosSecret"),
                        props.getProperty("shopManagerIosAppKey"), 3);
                payload = buildPushUserContentToTarget(title, content, registrationId);
            }
            else
            {
                jpushClient = new JPushClient(props.getProperty("iosSecret"), props.getProperty("iosAppKey"), 3);
                payload = buildPushObjectToTarget(title, content, registrationId);
            }

        }
        else if (platform.equals(CommonConst.RESTAURANT) || platform.equals(CommonConst.FASTRESTAURANT))
        {
            jpushClient = new JPushClient(props.getProperty("restaurantSecret"), props.getProperty("restaurantAppKey"),
                    3);
            payload = buildPushObjectToTargetShop(content, registrationId);
        }
        else if (platform.equals(CommonConst.GOODS))
        {
            jpushClient = new JPushClient(props.getProperty("goodsSecret"), props.getProperty("goodsAppKey"), 3);
            payload = buildPushObjectToTargetShop(content, registrationId);
        }
        else if (platform.equals(CommonConst.SERVICE))
        {
            jpushClient = new JPushClient(props.getProperty("serviceSecret"), props.getProperty("serviceAppKey"), 3);
            payload = buildPushObjectToTargetShop(content, registrationId);
        }
        else if (platform.equals(CommonConst.AUTO))
        {
            jpushClient = new JPushClient(props.getProperty("autoSecret"), props.getProperty("autoAppKey"), 3);
            payload = buildPushObjectToTargetShop(content, registrationId);
        }
        else
        {
            if (userType != null && userType.equals(CommonConst.USER_TYPE_TEN))
            {
                jpushClient = new JPushClient(props.getProperty("shopManagerAndroidSecret"),
                        props.getProperty("shopManagerAndroidAppKey"), 3);
                payload = buildPushUserContentToTarget(content, title, registrationId);
            }
            else
            {
                jpushClient = new JPushClient(props.getProperty("androidSecret"), props.getProperty("androidAppKey"),
                        3);
                payload = buildPushObjectToTarget(content, title, registrationId);
            }
        }
       log.info(payload.toString());
       result = jpushClient.sendPush(payload);
       log.info("Got result - " + result);
   
        return result;
    }

    /*
     * public PushPayload buildPushObject_all_all_alert() { return
     * PushPayload.alertAll(ALERT); }
     */

    private PushPayload buildPushObject_all_alias_alert(String content)
    {
        return PushPayload.newBuilder().setPlatform(Platform.all())// 设置接受的平台
                .setAudience(Audience.all())// Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.newBuilder().setAlert("消息提醒")
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra("content", content).build())
                        .addPlatformNotification(IosNotification.newBuilder().addExtra("content", content).build())
                        .build())
                .build();
    }

    private PushPayload buildPushObjectToTarget(String title, String content, String... registrationId)
    {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(
                        Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(registrationId)).build())
                .setNotification(Notification.newBuilder().setAlert(title)
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra("content", content).build())
                        .addPlatformNotification(IosNotification.newBuilder().addExtra("content", content).build())
                        .build())
                .setMessage(Message.newBuilder().setMsgContent("msgContent").setTitle(title).build()).build();
    }

    /**
     * 推送自定义消息
     * @param registrationId
     * @param title
     * @param content
     * @return
     */
    private PushPayload buildPushUserContentToTarget(String title, String content, String... registrationId)
    {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(
                        Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(registrationId)).build())
                .setMessage(Message.newBuilder().setMsgContent("msgContent").addExtra("content", content).build())
                .build();
    }

    private PushPayload buildPushObjectToTargetShop(String content, String... registrationId)
    {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(
                        Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(registrationId)).build())
                .setNotification(Notification.newBuilder().setAlert("消息提醒").build())
                .setMessage(Message.newBuilder().setMsgContent("msgContent").addExtra("content", content).build())
                .build();
    }

    private PushPayload buildPushObject_android_and_ios()
    {
        return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.tag("tag1"))
                .setNotification(Notification.newBuilder().setAlert("alert content")
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle("Android Title").build())
                        .addPlatformNotification(
                                IosNotification.newBuilder().incrBadge(1).addExtra("extra_key", "extra_value").build())
                        .build())
                .build();
    }

    @Override
    public InfrastructureType getSendChannel(String code)
    {
        return null;
    }


}
