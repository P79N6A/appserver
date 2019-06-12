package com.idcq.appserver.exception;

/**
 * 服务接口调用业务层异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 上午9:08:41
 */
public class APIBusinessException extends AppException{
	private int code;
	
	public APIBusinessException(String message){
		super(message);
	}
	public APIBusinessException(int code,String message){
		super(message);
		this.code = code;
	}
	
	public APIBusinessException(ServiceException e){
		super(e.getMessage());
		this.code = e.getCode();
	}

	public APIBusinessException(Throwable cause) {
		super(cause);
	}
	
	public int getCode(){
		return this.code;
	}
	
}
