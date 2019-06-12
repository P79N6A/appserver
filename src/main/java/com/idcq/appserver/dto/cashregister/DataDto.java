package com.idcq.appserver.dto.cashregister;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.idcq.appserver.utils.ToStringMethod;

/**
 * 订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class DataDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3106590533291837994L;
	private Date createTime;	
    private Double discount;//折扣
    private Double total;//菜单总价
    private Double outfee;//服务费
    private Double advance;//预付
    private Double maling;//抹零价
    private Double payable;//应付
    private String id;//订单id
    private Integer isMaling;//0无抹零，1抹元，2抹角
    private List<OrderInfoDto> orderInfo;
	private Long billerId;   					//下单员工                  
	private Long cashierId;				    	//收银员 
	private Double additionalDiscount;			//折上折   
	
	private Integer orderSceneType;
	private Long clientLastTime;
	private Long businessAreaActivityId;
    
	public DataDto() {
		super();
	}

	public String toString(){
		return ToStringMethod.toString(this);
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getOutfee() {
		return outfee;
	}

	public void setOutfee(Double outfee) {
		this.outfee = outfee;
	}

	public Double getAdvance() {
		return advance;
	}

	public void setAdvance(Double advance) {
		this.advance = advance;
	}

	public Double getMaling() {
		return maling;
	}

	public void setMaling(Double maling) {
		this.maling = maling;
	}

	public Double getPayable() {
		return payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<OrderInfoDto> getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(List<OrderInfoDto> orderInfo) {
		this.orderInfo = orderInfo;
	}

	public Integer getIsMaling() {
		return isMaling;
	}

	public void setIsMaling(Integer isMaling) {
		this.isMaling = isMaling;
	}

	public Long getBillerId() {
		return billerId;
	}

	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}

	public Long getCashierId() {
		return cashierId;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

	public Double getAdditionalDiscount() {
		return additionalDiscount;
	}

	public void setAdditionalDiscount(Double additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}

	public Integer getOrderSceneType() {
		return orderSceneType;
	}

	public void setOrderSceneType(Integer orderSceneType) {
		this.orderSceneType = orderSceneType;
	}

    public Long getClientLastTime()
    {
        return clientLastTime;
    }

    public void setClientLastTime(Long clientLastTime)
    {
        this.clientLastTime = clientLastTime;
    }

    public Long getBusinessAreaActivityId() {
        return businessAreaActivityId;
    }

    public void setBusinessAreaActivityId(Long businessAreaActivityId) {
        this.businessAreaActivityId = businessAreaActivityId;
    }
	
}
