package com.idcq.appserver.exception;

/**
 * 数据验证异常
 * 
 * @author Administrator
 * 
 * @date 2015年3月6日
 * @time 下午5:44:37
 */
public class ValidateException extends ServiceException {
	private final int code ;

	public ValidateException(int errorCode, String message) {
		super(message);
		this.code = errorCode;
	}

	public int getCode() {
		return this.code;
	}

}
