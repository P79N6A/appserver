package com.idcq.appserver.dto.goods;

/**
 * 小时资源
 * @author Administrator
 *
 */
public class HourResouce {
	
	private String beginTime;
	private String endTime;
	private int begin;
	private int end;
	private int status = 1;  // 资源状态,枚举值：1-可用，2-不可用
	
	
	public String getBeginTime() {
		if(beginTime == null)
			return begin + ":00";
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		if(endTime == null)
			return end+":00";
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
