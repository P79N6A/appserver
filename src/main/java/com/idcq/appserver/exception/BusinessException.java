package com.idcq.appserver.exception;

/**
 * 业务层异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 上午9:08:41
 */
public class BusinessException extends AppException{
	private int code;
	
	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(ServiceException e){
		super(e.getMessage());
		this.code = e.getCode();
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	public int getCode(){
		return this.code;
	}
	
}
