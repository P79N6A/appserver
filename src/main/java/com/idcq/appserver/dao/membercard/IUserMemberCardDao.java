package com.idcq.appserver.dao.membercard;

import com.idcq.appserver.dto.membercard.UserMemberCardDto;

public interface IUserMemberCardDao {
	
	/**
	 * 获取会员卡余额
	 */
	UserMemberCardDto getLeftMoney(Integer queryType, Long accountId, Long userId, Long shopId) ;
	
	UserMemberCardDto getMemberCardInfoById(Long accountId);
	
	/**
	 * 根据会员卡编号查询商铺编号信息
	 * @param umcId
	 * @return
	 */
	Integer queryShopIdByUmcId(Long umcId);
}
