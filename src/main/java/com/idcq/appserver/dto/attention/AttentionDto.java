package com.idcq.appserver.dto.attention;

import java.io.Serializable;

/**
 * 订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:02:35
 */
public class AttentionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432741524870381753L;
	private long id;
	private long memberId;	//会员ID
	private int status;		//订单状态
	private int orderBy;	//排序选项
	
	public AttentionDto() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		return "OrderDto [id=" + id + ", memberId=" + memberId + ", status="
				+ status + ", orderBy=" + orderBy + "]";
	}
	
	
}
