package com.idcq.appserver.service.msgPush.appPush.intercepters.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.msgPusher.baseMsg.enums.MsgType;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
import com.idcq.appserver.common.msgPusher.busMsg.msgInterpreter.PushMsgGenerater;
import com.idcq.appserver.dao.message.IPushShopMsgDao;
import com.idcq.appserver.dto.message.AppPushRecordDto;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.message.PushShopMsgDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.service.message.IMessageSettingService;
import com.idcq.appserver.service.msgPush.appPush.PushRecordService;

@Component("THIRD_PAY_PUSH_INTERPRETER")
public class ThirdPayPushInterpreter implements PushMsgGenerater
{
    private static final Logger log = LoggerFactory.getLogger(ThirdPayPushInterpreter.class);
    @Autowired
    private IMessageSettingService messageSettingService;
    @Autowired
    private IPushShopMsgDao pushShopMsgDao;
    @Autowired
    private PushRecordService pushRecordService;
    
    @Override
    public PushMsg[] interpret(BusMsg busMsg)
    {   
        PushMsg[] rs = null;
        try
        {
            /* 判断推送开关 */
            MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey("cashOrder");
            if(null == messageSettingDto || 1 != messageSettingDto.getRemandFlag()){
                return null;
            }
            
            /* 获取参数 */
            Map<String, Object> params = busMsg.getParams();
            OrderDto order = (OrderDto)params.get("order");
            if(order.getOrderChannelType() != 2){
                return null;
            }
//            Integer payReason = (Integer)params.get("payReason");
            Long shopId = order.getShopId();
            /* 生成消息体 */
/*            StringBuilder content = new StringBuilder();
            content.append("{");
            content.append("\"shopId\":" + shopId + ",");
            content.append("\"action\":\"cashOrder\",");
            content.append("\"data\":{\"id\":\"" + order.getOrderId() + "\",\"orderStatus\":" + order.getOrderStatus()
                    + ",\"payStatus\":" + order.getPayStatus() + "}");
            content.append("}");*/
            Date createTime = busMsg.getCreateTime();
            createTime = null == createTime ? new Date() : createTime;
            StringBuilder content = new StringBuilder();
            content.append("{");
            content.append("id:\"").append("${msg_id}").append("\",");
            content.append("ti:").append(createTime.getTime()).append(",");
            content.append("ct:{");
            content.append("\"shopId\":").append(shopId).append(",");
            content.append("\"action\":\"cashOrder\",");
            content.append("\"data\":{\"id\":\"" + order.getOrderId() + "\",\"orderStatus\":" + order.getOrderStatus()
                    + ",\"payStatus\":" + order.getPayStatus() + "}");
            content.append("}");
            content.append("}");
            //保存推送纪录，临时性质
            AppPushRecordDto appPushRecordDto = new AppPushRecordDto();
            appPushRecordDto.setTargetId("s1/cas/" + shopId);
            appPushRecordDto.setIsUsed(0);
            appPushRecordDto.setMessageContent(content.toString());
            appPushRecordDto.setSendTimeServer(createTime);
            appPushRecordDto.setMessageStatus(1);
            pushRecordService.sendRecord(appPushRecordDto);
//            content.
            String msgContent = content.toString().replaceFirst("\\$\\{msg_id}", appPushRecordDto.getMessageId() + "");
            PushDto push = new PushDto();
            push.setAction("cashOrder");
            push.setContent(msgContent);
            PushMsg msg = new PushMsg();
            msg.setContent(msgContent.getBytes("UTF-8"));
            msg.setCrateTime(createTime);
            //设置消息类型
            msg.setMsgType(MsgType.APP_PUSH);
            msg.setQos(2);
            //是否是广播消息
            msg.setPublicBroadcaster(false);
            msg.setTargetId(new String[]{"s1/cas/" + shopId});
            //保存消息纪录至1dcq_push_shop_message
//            this.saveSendRecord("cashOrder", shopId, msg);
            //线上测试阶段使用
            rs = new PushMsg[]{msg};
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e.getMessage(), e);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return rs;
    }
    
    public static void main(String[] args)
    {   
        StringBuilder content = new StringBuilder();
        content.append("{");
        content.append("id:\"").append("${msg_id}").append("\",");
        System.out.println(content.toString().replaceFirst("\\$\\{msg_id\\}", "123456"));
    }
    
    private void saveSendRecord(String action, Long shopId, PushMsg pmsg){
        PushShopMsgDto msg = new PushShopMsgDto();
        msg.setShopId(shopId);
        msg.setRegId(pmsg.getIdStr());
        msg.setAction(action);
        msg.setSendTime(pmsg.getCrateTime());
        try
        {
            msg.setMessageContent(new String(pmsg.getContent(),"UTF-8"));   
            this.pushShopMsgDao.insertSelective(msg);
        }
        catch (Exception e)
        {
           log.error(e.getMessage(), e);
        }
    }
    
/*    public static void main(String[] args)
    {   
        StringBuilder content = new StringBuilder();
        content.append("{");
        content.append("id:\"").append("222").append("\",");
        content.append("ti:").append("12121222").append(",");
        content.append("ct:{");
        content.append("\"shopId\":" + "123456" + ",");
        content.append("\"action\":\"cashOrder\",");
        content.append("\"data\":{\"id\":\"" + "123456" + "\",\"orderStatus\":" + "1"
                + ",\"payStatus\":" + "6" + "}");
        content.append("}");
        content.append("}");
        System.out.println(content);
    }*/
    
}
