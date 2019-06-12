package com.idcq.appserver.dto.order;
/**
 * 商铺资源
 * @author Administrator
 *
 */
public class SeatInfo implements java.io.Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3731480317160784453L;
	/**
	 * 桌子数量
	 */
    private Integer seatNum;
    /**
     * 桌子类型
     */
    private String seatCate;
    
	public Integer getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}
	public String getSeatCate() {
		return seatCate;
	}
	public void setSeatCate(String seatCate) {
		this.seatCate = seatCate;
	}
	

}
