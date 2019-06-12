package com.idcq.appserver.exception;

/**
 * 服务接口调用系统异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 上午9:09:23
 */
public class APISystemException extends AppException{

	public APISystemException(String message, Throwable cause) {
		super(message, cause);
	}
}
