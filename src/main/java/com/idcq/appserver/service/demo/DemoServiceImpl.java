package com.idcq.appserver.service.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.demo.IDemoDao;
import com.idcq.appserver.dto.demo.UserDto2;

/**
 * service demo
 * 
 * @author Administrator
 * 
 * @date 2015��3��2��
 * @time ����4:57:15
 */
@Service
public class DemoServiceImpl implements IDemoService{
	
	@Autowired
	public IDemoDao demoDao;

	public UserDto2 getUserById(long uId) throws Exception {
		// TODO Auto-generated method stub
		UserDto2 user = new UserDto2();
		user.setId(1);
		user.setName("���Է����");
		return user;
	}
	
	public UserDto2 insertUser(UserDto2 user) throws Exception{
		demoDao.insert(user);
		return user;
	}
}
