package com.idcq.appserver.exception;


/**
 * 基本应用异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 上午9:08:12
 */
public class AppException extends RuntimeException{

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}
	
	public static void main(String[] args) {
		
		try {
			throw new ValidateException(1, "发生异常");
		} catch (APIBusinessException e) {
			e.printStackTrace();
			System.out.println("业务层异常 ： "+e.getMessage());
		} catch (ServiceException e) {
			e.printStackTrace();
			System.out.println("errorCode : "+((ValidateException)e).getCode()+"  errorMsg : "+e.getMessage());
		}
	}
	
}
