package com.idcq.appserver.dto.order;
/**
 * 商品Dto
 * 用于post提交
 * @author Administrator
 *
 */
public class BookInfo implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2670308902706895617L;
	private Long dishId;
	private Double num;
	
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}

}
