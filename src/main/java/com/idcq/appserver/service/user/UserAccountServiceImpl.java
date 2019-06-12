package com.idcq.appserver.service.user;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dto.user.UserAccountDto;

@Service
public class UserAccountServiceImpl implements IUserAccountService{
	@Autowired
	private IUserAccountDao	 userAccountDao;

	@Override
	public UserAccountDto getAccountMoney(Long userId) throws Exception {
		return this.userAccountDao.getAccountMoney(userId);
	}
	
	public Map<String, Object> getRedPackUserDetail(Long userId) throws Exception{
		return this.userAccountDao.getRedPackUserDetail(userId);
	}
	
}
