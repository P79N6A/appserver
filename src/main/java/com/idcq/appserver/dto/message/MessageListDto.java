package com.idcq.appserver.dto.message;

import java.io.Serializable;
import java.util.List;

/**
 * 消息列表dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:48:33
 */
public class MessageListDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3281977782732739772L;
	private int pNo;	//页码
	private int pSize;	//页容量
	private int rCount; //总记录数
	private List lst;	//数据列表 
	       
	public MessageListDto() {
		super();
	}

	public int getpNo() {
		return pNo;
	}

	public void setpNo(int pNo) {
		this.pNo = pNo;
	}

	public int getpSize() {
		return pSize;
	}

	public void setpSize(int pSize) {
		this.pSize = pSize;
	}

	public int getrCount() {
		return rCount;
	}

	public void setrCount(int rCount) {
		this.rCount = rCount;
	}

	public List getLst() {
		return lst;
	}

	public void setLst(List lst) {
		this.lst = lst;
	}


	
	
}
