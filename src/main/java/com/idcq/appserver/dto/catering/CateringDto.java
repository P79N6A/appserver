package com.idcq.appserver.dto.catering;

import java.io.Serializable;

/**
 * 餐饮业dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午4:02:44
 */
public class CateringDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6308139243062146096L;
	private Long id;
	private int typeId;		//类型ID
	private int area;		//区域
	private int orderBy;	//排序方式
	public CateringDto() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	@Override
	public String toString() {
		return "CateringDto [id=" + id + ", typeId=" + typeId + ", area="
				+ area + ", orderBy=" + orderBy + "]";
	}
	
	
}
