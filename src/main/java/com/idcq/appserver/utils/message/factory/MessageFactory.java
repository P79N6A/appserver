package com.idcq.appserver.utils.message.factory;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.utils.message.bussinse.BaseMessage;
import com.idcq.appserver.utils.message.bussinse.MemberMessage;
import com.idcq.appserver.utils.message.bussinse.ShopMessage;

public class MessageFactory {
	public static BaseMessage createMessage(int notifyType){
		BaseMessage baseMessage = null;
		switch(notifyType){
		case CommonConst.NOTIRY_TYPE_SHOP:
			baseMessage = new ShopMessage();
			break;
		case CommonConst.NOTIRY_TYPE_MEMBER:
			baseMessage = new MemberMessage();
			break;
			
		}
		return baseMessage;
	}
}
