package com.idcq.appserver.dto.message;

import java.io.Serializable;
import java.util.List;

/**
 * 返回实体
 * @author Administrator
 *
 */
public class ResultListToClient implements Serializable{
	
	private static final long serialVersionUID = 3281977782732739772L;
	private List lst;	//数据列表 
	public List getLst() {
		return lst;
	}
	public void setLst(List lst) {
		this.lst = lst;
	}
	
}
