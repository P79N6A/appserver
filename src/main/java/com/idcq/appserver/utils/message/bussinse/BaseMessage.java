package com.idcq.appserver.utils.message.bussinse;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.utils.message.channel.BaseChannel;
import com.idcq.appserver.utils.message.factory.ChannelFactory;

public abstract class BaseMessage {
	public static Log logger = LogFactory.getLog(BaseMessage.class.getSimpleName());
	public List<BaseChannel> channels;
	protected Object params;
	
	/**
	 * 获取消息发送渠道
	 */
	public void getChannels(){
		String notifyChannel = CommonConst.NOTIFY_CHANNEL;
		channels = ChannelFactory.createChannel(notifyChannel);
		System.out.println("通道列表："+channels.size());
	}
	
	/**
	 * 发送消息
	 * @param obj
	 * @throws Exception
	 */
	@Deprecated
	public void sendMessage(MessageCenterDto messageCenterDto, Object classz) throws Exception{
		/**
		 * 步骤一：获取发送渠道，可能多个并行--getChannels()
		 * 
		 */
		
		/**
		 * 步骤二：不同类型消息单独处理--doBussinse()
		 * 
		 */
		
		
		/**
		 * 步骤三：先保存消息，返回主键
		 * 
		 * 1.部分消息推送需要消息主键
		 * 
		 */
		
		/**
		 * 步骤四：发送消息
		 * 
		 * 1.填充消息信息
		 */
		getChannels();//获取发送渠道
		for(BaseChannel channel : channels){
			messageCenterDto.setNotifyChannel(channel.getNotifyChannel());
			channel.send(messageCenterDto,classz);
		}
	}
	
	/**
	 * 发送消息
	 * @param messageCenterDto
	 * @throws Exception
	 */
	public abstract void doBussinse(MessageCenterDto messageCenterDto,Object classz) throws Exception;

}
