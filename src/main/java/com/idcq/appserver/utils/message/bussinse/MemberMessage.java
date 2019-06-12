package com.idcq.appserver.utils.message.bussinse;

import com.idcq.appserver.common.enums.MsgCenterHandleTypeEnum;
import com.idcq.appserver.common.enums.MsgCenterMsgStatusEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyModelEnum;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.user.UserDto;

public class MemberMessage extends BaseMessage {
    
    @Deprecated
	@Override
	public void doBussinse(MessageCenterDto messageCenterDto,Object classz) throws Exception {
		logger.info("处理会员数据...............");
		UserDto userDto = (UserDto) classz;
		messageCenterDto.setHandleType(MsgCenterHandleTypeEnum.DISPLAY.getValue());
		messageCenterDto.setReceiverId(userDto.getUserId());
		messageCenterDto.setMessageStatus(MsgCenterMsgStatusEnum.SEND_OK.getValue());
		messageCenterDto.setNotifyModel(MsgCenterNotifyModelEnum.END2END.getValue());
		sendMessage(messageCenterDto, classz);
	}

}
