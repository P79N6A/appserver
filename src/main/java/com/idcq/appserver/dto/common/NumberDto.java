package com.idcq.appserver.dto.common;

import java.io.Serializable;

/**
 * 解决int数据类型引用传递问题
 * 
 * @author Administrator
 * 
 * @date 2015年6月3日
 * @time 上午9:59:47
 */
public class NumberDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7450163731283018589L;
	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public void incrNum(int num) {
		this.num += num;
	}
	
	
	
}
