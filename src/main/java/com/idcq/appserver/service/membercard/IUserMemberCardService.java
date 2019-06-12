package com.idcq.appserver.service.membercard;

import com.idcq.appserver.dto.membercard.UserMemberCardDto;

public interface IUserMemberCardService {
	
	/**
	 * 获取会员卡余额
	 */
	UserMemberCardDto getLeftMoney(Integer queryType,Long accountId,Long userId,Long shopId) throws Exception ;
	
	
}
