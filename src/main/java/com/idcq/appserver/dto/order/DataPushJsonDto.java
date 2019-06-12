package com.idcq.appserver.dto.order;

import java.util.List;

/**
 * json数据实体
 * @author Administrator
 *
 */
public class DataPushJsonDto implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8677018553923409322L;
	private List<BookInfo> bookInfo;
	private List<SeatInfo> seatInfo;
	/**
	 * 预定订单id
	 */
	private String id;
	/**
	 * 服务订单号
	 */
//	private Long webBookId; 
	/**
	 * 创建时间
	 */
	private String time;
	/**
	 * 吃饭开始时间
	 */
	private String eatTimeFrom;
	/**
	 * 吃饭结束时间
	 */
	private String eatTimeTo;
	
	/**
	 * 数量
	 */
	private Integer pNum;
	/**
	 * 名称
	 */
	private String pName;
	/**
	 * 电话
	 */
	private String mobile;
	/**
	 * 联系人电话
	 */
	private String  contactPhone;
	/**
	 * 预付款
	 */
	private Double advance;
	/**
	 * 会员折扣
	 */
	private Double discount;
    /**
     * 外卖地址
     */
    private String address;
    /**
     * true :外卖。 false 不是
     */
    private boolean isWm;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 订单场景分类
     */
    private Integer orderSceneType; //订单场景分类：1(到店点菜订单,非外卖订单）,2(外卖订单),3(服务订单),4(商品订单)[增补orderSceneType字段。]    
    
    /**
     * 商铺资源id
     * 
     */
    private Long seatId;
    /**
     * 用户备注（加辣、咸等）
     */
    private String userRemark;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    
    /**
     * 来源系统
     */
    private Integer clientSystemType;
    
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public Long getSeatId() {
		return seatId;
	}
	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Integer getOrderSceneType() {
		return orderSceneType;
	}
	public void setOrderSceneType(Integer orderSceneType) {
		this.orderSceneType = orderSceneType;
	}
	public boolean getIsWm() {
		return isWm;
	}
	public void setIsWm(boolean isWm) {
		this.isWm = isWm;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<BookInfo> getBookInfo() {
		return bookInfo;
	}
	public void setBookInfo(List<BookInfo> bookInfo) {
		this.bookInfo = bookInfo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEatTimeFrom() {
		return eatTimeFrom;
	}
	public void setEatTimeFrom(String eatTimeFrom) {
		this.eatTimeFrom = eatTimeFrom;
	}
	public String getEatTimeTo() {
		return eatTimeTo;
	}
	public void setEatTimeTo(String eatTimeTo) {
		this.eatTimeTo = eatTimeTo;
	}
	public Integer getpNum() {
		return pNum;
	}
	public void setpNum(Integer pNum) {
		this.pNum = pNum;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getAdvance() {
		return advance;
	}
	public void setAdvance(Double advance) {
		this.advance = advance;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public List<SeatInfo> getSeatInfo() {
		return seatInfo;
	}
	public void setSeatInfo(List<SeatInfo> seatInfo) {
		this.seatInfo = seatInfo;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public void setWm(boolean isWm) {
		this.isWm = isWm;
	}
    public Integer getClientSystemType() {
        return clientSystemType;
    }
    public void setClientSystemType(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
    }
}
