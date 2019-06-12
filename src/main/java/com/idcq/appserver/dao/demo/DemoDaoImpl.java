package com.idcq.appserver.dao.demo;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.demo.UserDto2;

/**
 * dao demo 
 * 
 * @author Administrator
 * 
 * @date 2015年3月2日
 * @time 下午4:56:47
 */
@Repository
public class DemoDaoImpl extends BaseDao<UserDto2> implements IDemoDao{

	public UserDto2 insert(UserDto2 user) {
		super.insert(user);
		return user;
	}
	
}
