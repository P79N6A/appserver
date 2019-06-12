package com.idcq.appserver.service.user;

import java.math.BigInteger;
import java.util.Map;

import com.idcq.appserver.dto.user.UserAccountDto;

public interface IUserAccountService {
	
	/**
	 * 获取用户账户余额
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserAccountDto getAccountMoney(Long userId) throws Exception;
	
	/**
	 * 获取领取红包用户的信息
	 * @param redPacketId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getRedPackUserDetail(Long userId) throws Exception;
}
