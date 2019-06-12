package com.idcq.appserver.dto;


/**
 * 接口响应实体
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:42:18
 */
public class ResultDto {
	
	private int code;
	private String msg;
	private Object data;
	
	public ResultDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
