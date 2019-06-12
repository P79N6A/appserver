package com.idcq.appserver.utils.message.bussinse;

import com.idcq.appserver.common.enums.MsgCenterHandleTypeEnum;
import com.idcq.appserver.common.enums.MsgCenterMsgStatusEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyModelEnum;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.shop.ShopDto;

public class ShopMessage extends BaseMessage {
    /**
     * for test
     */
    @Deprecated
	public void doBussinse(MessageCenterDto messageCenterDto,Object classz) throws Exception {
		// TODO Auto-generated method stub
		logger.info("处理商铺数据...............");
		ShopDto shopDto = (ShopDto) classz;
		messageCenterDto.setHandleType(MsgCenterHandleTypeEnum.DISPLAY.getValue());
		messageCenterDto.setReceiverId(shopDto.getShopId());
		messageCenterDto.setMessageStatus(MsgCenterMsgStatusEnum.SEND_OK.getValue());
		messageCenterDto.setNotifyModel(MsgCenterNotifyModelEnum.END2END.getValue());
		sendMessage(messageCenterDto, classz);
	}

}
