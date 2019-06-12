package com.idcq.appserver.service.demo;

import com.idcq.appserver.dto.demo.UserDto2;

public interface IDemoService {
	UserDto2 getUserById(long uId) throws Exception;
	
	UserDto2 insertUser(UserDto2 user) throws Exception;
}
