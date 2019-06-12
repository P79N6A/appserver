package com.idcq.appserver.utils.message.factory;

import java.util.ArrayList;
import java.util.List;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.utils.message.channel.BaseChannel;
import com.idcq.appserver.utils.message.channel.JpushChannel;
import com.idcq.appserver.utils.message.channel.SmsChannelImpl;

public class ChannelFactory {
	public static List<BaseChannel> createChannel(String notifyChannel ){
		List<BaseChannel> channels = new ArrayList<BaseChannel>();
		BaseChannel channel = null;
		String[] strings = notifyChannel.split(",");
		if (null != strings && strings.length > 0) {
			for (int i = 0; i < strings.length; i++) {
				String notifyType = strings[i];
				switch(notifyType){
				case CommonConst.NOTIFY_CHANNEL_JPUSH:
					//jpush
					channel = new JpushChannel();
					channel.setNotifyChannel(CommonConst.NOTIFY_CHANNEL_MAP.get(notifyType));
					channels.add(channel);
					break;
				case CommonConst.NOTIFY_CHANNEL_SMS:
					//sms
					channel = new SmsChannelImpl();
					channel.setNotifyChannel(CommonConst.NOTIFY_CHANNEL_MAP.get(notifyType));
					channels.add(channel);
					break;
				}
			}
		}
		//默认使用Jpush
		if (channels.isEmpty()) {
			channel = new JpushChannel();
			channels.add(channel);
		}
		return channels;
	}
}
