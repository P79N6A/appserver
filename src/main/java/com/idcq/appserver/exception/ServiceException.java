package com.idcq.appserver.exception;

/**
 * 服务层异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 上午9:09:10
 */
public class ServiceException extends AppException{
	private int code ;
	public ServiceException(String message){
		super(message);
	}
	public ServiceException(int code,String message){
		super(message);
		this.code = code;
	}
	
	public int getCode(){
		return this.code;
	}
}
