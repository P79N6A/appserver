package com.idcq.appserver.utils;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.listeners.ContextInitListener;

public class Jpush {
	 protected static final Logger LOG = LoggerFactory.getLogger(Jpush.class);

	 // demo App defined in resources/jpush-api.conf 

    public static final String ALERT = "恭喜，经领导批准给您涨工资啦。。。";
    public static final String TAG = "tag_api";
	private static final ExecutorService executors = Executors.newFixedThreadPool(3);
    
//    public  static JPushClient jpushClient=null;
	
    
    /**
     *  消息推送给所有ios设备
     * @param content
     */
	public static void pushToIos(String masterSecret, String appKey, int maxRetryTimes,String content) {
	    JPushClient jpushClient = new JPushClient(masterSecret,appKey, 3);
		 //生成推送的内容，这里我们先测试全部推送
		PushPayload payload=buildPushObject_all_alias_alert(content);
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
	}
	 /**
     *  消息推送给所有android设备
     * @param content
     */
	public static void pushToAndroid(String masterSecret, String appKey, int maxRetryTimes,String content) {
	    JPushClient jpushClient = new JPushClient(masterSecret,appKey, 3);
		 //生成推送的内容，这里我们先测试全部推送
		PushPayload payload=buildPushObject_all_alias_alert(content);
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
	}
	
    /**
     *  消息推送给所有设备
     * @param osInfo 系统类型
     * @param content
     */
	public static void pushToAll(String content) {
		Properties props=ContextInitListener.JPUSH_PROPS;
		//推送给用户
		pushToAndroid(props.getProperty("androidSecret"), props.getProperty("androidAppKey"),3,content);
		pushToIos(props.getProperty("iosSecret"), props.getProperty("iosAppKey"),3,content);
		
		//推送给免费商家
		pushToAndroid(props.getProperty("freeSellerAndroidSecret"), props.getProperty("freeSellerAndroidAppKey"),3,content);
		pushToIos(props.getProperty("freeSellerIosSecret"), props.getProperty("freeSellerIosAppKey"),3,content);
	}
	
	/**
	 * 向指定的设备推送
	 * @Deprecated
	 * @param osInfo 系统类型
	 * @param registrationId
	 * @param content
	 */
	/*public static void sendPushToTarget(final String osInfo, final Integer userType, final String registrationId,final String title,final String content) {
	    new Thread(){
			public void run() {
				LOG.error("开始推送了");
				JPushClient jpushClient = null;
				Properties props=ContextInitListener.JPUSH_PROPS;
				//向指定的设备推送
			    PushPayload payload=null;
			    String platform = osInfo.toLowerCase();
				if(platform.startsWith("ios")){
					if(userType != null && userType.equals(CommonConst.USER_TYPE_TEN)) {
						jpushClient = new JPushClient(props.getProperty("shopManagerIosSecret"), props.getProperty("shopManagerIosAppKey"), 3);
						payload=buildPushUserContentToTarget(registrationId,title, content);
					} else {
						jpushClient = new JPushClient(props.getProperty("iosSecret"), props.getProperty("iosAppKey"), 3);
						payload=buildPushObjectToTarget(registrationId,title, content);
					}
					
				} else if(platform.equals(CommonConst.RESTAURANT) || platform.equals(CommonConst.FASTRESTAURANT)) {
					jpushClient = new JPushClient(props.getProperty("restaurantSecret"), props.getProperty("restaurantAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if(platform.equals(CommonConst.GOODS)) {
					jpushClient = new JPushClient(props.getProperty("goodsSecret"), props.getProperty("goodsAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if(platform.equals(CommonConst.SERVICE)) {
					jpushClient = new JPushClient(props.getProperty("serviceSecret"), props.getProperty("serviceAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if (platform.equals(CommonConst.AUTO)){
				    jpushClient = new JPushClient(props.getProperty("autoSecret"), props.getProperty("autoAppKey"), 3);
                    payload=buildPushObjectToTargetShop(registrationId, content);
				} else{
					if(userType != null && userType.equals(CommonConst.USER_TYPE_TEN)) {
						jpushClient = new JPushClient(props.getProperty("shopManagerAndroidSecret"), props.getProperty("shopManagerAndroidAppKey"), 3);
						payload=buildPushUserContentToTarget(registrationId,title, content);
					} else {
						jpushClient = new JPushClient(props.getProperty("androidSecret"), props.getProperty("androidAppKey"), 3);
						payload=buildPushObjectToTarget(registrationId,title,content);
					}
				}
		       try {
		    	   LOG.info(payload.toString());
		           PushResult result = jpushClient.sendPush(payload);
		           LOG.info("Got result - " + result);
		       } catch (APIConnectionException e) {
		           LOG.error("Connection error. Should retry later. ", e);
		       } catch (APIRequestException e) {
		           LOG.error("Error response from JPush server. Should review and fix it. ", e);
		           LOG.info("HTTP Status: " + e.getStatus());
		           LOG.info("Error Code: " + e.getErrorCode());
		           LOG.info("Error Message: " + e.getErrorMessage());
		           LOG.info("Msg ID: " + e.getMsgId());
		       }
			}
		}.start();
	}
*/
	/**
	 * 向指定的设备推送
	 * @param osInfo 
	 * @param userType 用户：0；店铺管理者：10
	 * @param registrationId
	 * @param title, 为必填字段，可以为空串
     * @param content
     */
	public static void sendPushToTarget(final String osInfo, final Integer userType, final String registrationId,final String title,final String content) {
		Runnable job = new Runnable(){
			public void run() {
				LOG.error("开始推送了");
				JPushClient jpushClient = null;
				Properties props=ContextInitListener.JPUSH_PROPS;
				//向指定的设备推送
				PushPayload payload=null;
				String platform = osInfo.toLowerCase();
				if(platform.startsWith("ios")){
					if(userType != null && userType.equals(CommonConst.USER_TYPE_TEN)) {
						jpushClient = new JPushClient(props.getProperty("shopManagerIosSecret"), props.getProperty("shopManagerIosAppKey"), 3);
						payload=buildPushUserContentToTarget(registrationId,title, content);
					} else {
						jpushClient = new JPushClient(props.getProperty("iosSecret"), props.getProperty("iosAppKey"), 3);
						payload=buildPushObjectToTarget(registrationId,title, content);
					}

				} else if(platform.equals(CommonConst.RESTAURANT) || platform.equals(CommonConst.FASTRESTAURANT)) {
					jpushClient = new JPushClient(props.getProperty("restaurantSecret"), props.getProperty("restaurantAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if(platform.equals(CommonConst.GOODS)) {
					jpushClient = new JPushClient(props.getProperty("goodsSecret"), props.getProperty("goodsAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if(platform.equals(CommonConst.SERVICE)) {
					jpushClient = new JPushClient(props.getProperty("serviceSecret"), props.getProperty("serviceAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else if (platform.equals(CommonConst.AUTO)){
					jpushClient = new JPushClient(props.getProperty("autoSecret"), props.getProperty("autoAppKey"), 3);
					payload=buildPushObjectToTargetShop(registrationId, content);
				} else{
					if(userType != null && userType.equals(CommonConst.USER_TYPE_TEN)) {
						jpushClient = new JPushClient(props.getProperty("shopManagerAndroidSecret"), props.getProperty("shopManagerAndroidAppKey"), 3);
						payload=buildPushUserContentToTarget(registrationId,title, content);
					} else {
						jpushClient = new JPushClient(props.getProperty("androidSecret"), props.getProperty("androidAppKey"), 3);
						payload=buildPushObjectToTarget(registrationId,title,content);
					}
				}
				try {
					LOG.info(payload.toString());
					PushResult result = jpushClient.sendPush(payload);
					LOG.info("Got result - " + result);
				} catch (APIConnectionException e) {
					LOG.error("Connection error. Should retry later. ", e);
				} catch (APIRequestException e) {
					LOG.error("Error response from JPush server. Should review and fix it. ", e);
					LOG.info("HTTP Status: " + e.getStatus());
					LOG.info("Error Code: " + e.getErrorCode());
					LOG.info("Error Message: " + e.getErrorMessage());
					LOG.info("Msg ID: " + e.getMsgId());
				}
			}
		};
		executors.execute(job);
	}
	
	public static PushPayload buildPushObject_all_all_alert() {
	    return PushPayload.alertAll(ALERT);
	}
	
    public static PushPayload buildPushObject_all_alias_alert(String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.newBuilder()
    					.setAlert("消息提醒")
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.addExtra("content", content).build())
                		.addPlatformNotification(IosNotification.newBuilder()
                				.addExtra("content", content).build())
                		.build())
    			.build();
    }
    
    public static PushPayload buildPushObjectToTarget(String registrationId,String title,String content){
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.all())
    			.setAudience(Audience.newBuilder()
    						.addAudienceTarget(AudienceTarget.registrationId(registrationId))
    						.build())
    			.setNotification(Notification.newBuilder()
    					.setAlert(title)
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.addExtra("content", content).build())
                		.addPlatformNotification(IosNotification.newBuilder()
                				.addExtra("content", content).build())
                		.build())
                .setMessage(Message.newBuilder()
    					.setMsgContent("msgContent")
    					.setTitle(title)
    					.build()
    			).build();
    }
    
    /**
     * 推送自定义消息
     * @param registrationId
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushUserContentToTarget(String registrationId,String title,String content){
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.all())
    			.setAudience(Audience.newBuilder()
    						.addAudienceTarget(AudienceTarget.registrationId(registrationId))
    						.build())
                .setMessage(Message.newBuilder()
    					.setMsgContent("msgContent")
    					.addExtra("content", content)
    					.build()
    			).build();
    }
    
    public static PushPayload buildPushObjectToTargetShop(String registrationId,String content){
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.all())
    			.setAudience(Audience.newBuilder()
    						.addAudienceTarget(AudienceTarget.registrationId(registrationId))
    						.build())
    			.setNotification(Notification.newBuilder()
    					.setAlert("消息提醒")
    					.build())
    			.setMessage(Message.newBuilder()
    					.setMsgContent("msgContent")
    					.addExtra("content", content)
    					.build()
    			).build();
    }
    
    public static PushPayload buildPushObject_android_and_ios() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.newBuilder()
                		.setAlert("alert content")
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle("Android Title").build())
                		.addPlatformNotification(IosNotification.newBuilder()
                				.incrBadge(1)
                				.addExtra("extra_key", "extra_value").build())
                		.build())
                .build();
    }
    
    public static void main(String[] args) throws APIConnectionException, APIRequestException {
        JPushClient jpushClient = new JPushClient("f5bff8bab06c333d3b581948", "a112896ede613ade395ccae2", 3);
        PushPayload payload=buildPushObjectToTarget("160a3797c80c233bbdc", "你中奖了", "恭喜恭喜，您本次消费100获得最终的返现100元恭喜恭喜，您本次消费100获得最终的返现100元恭喜恭喜，您本次消费100获得最终的返现100元");
        PushResult result = jpushClient.sendPush(payload);
    }
   
}
