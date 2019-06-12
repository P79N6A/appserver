package com.idcq.appserver.service.membercard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.membercard.IUserMemberCardDao;
import com.idcq.appserver.dto.membercard.UserMemberCardDto;

@Service
public class UserMemberCardServiceImpl implements IUserMemberCardService {

	@Autowired
	private IUserMemberCardDao userMemberCardDao;
	
	/**
	 * 查询会员卡余额接口
	 */
	public UserMemberCardDto getLeftMoney(Integer queryType, Long accountId, Long userId,
			Long shopId) throws Exception {
		return userMemberCardDao.getLeftMoney(queryType, accountId, userId, shopId);
	}

}
