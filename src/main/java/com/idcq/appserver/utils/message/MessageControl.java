package com.idcq.appserver.utils.message;

import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.utils.message.bussinse.BaseMessage;
import com.idcq.appserver.utils.message.factory.MessageFactory;


public class MessageControl {
	
	
	/**
	 *  执行发送消息
	 * @param title 显示标题
	 * @param content 显示内容
	 * @param messageImage 消息图片路径
	 * @param notifyType 1-商铺消息 2-会员消息
	 * @param bizType 业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师=10,商品分类=11,launcher主页图标=12,商圈活动=13,收银机日志=14,商圈活动类型=15,消息中心消息=16',
	 * @param bizId 主键ID：如商铺ID，用户ID，商圈活动ID...
	 * @param classz notifyType=0时为ShopDto，notifyType=1时为UserDto
	 */
    @Deprecated
	public static void execute(String title, String content,String messageImage,int notifyType,int bizType,Long bizId, Object classz){
		try {
			MessageCenterDto messageCenterDto = new MessageCenterDto();
			messageCenterDto.setMessageTitle(title);
			messageCenterDto.setShowContent(content);
			messageCenterDto.setMessageImage(messageImage);
			messageCenterDto.setNotifyType(notifyType);
			messageCenterDto.setBizId(bizId);
			messageCenterDto.setBizType(bizType);
			BaseMessage message = MessageFactory.createMessage(notifyType);
			message.doBussinse(messageCenterDto,classz);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
