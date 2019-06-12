package com.idcq.appserver.dto.advertise;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 广告dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月18日
 * @time 上午9:09:32
 */
public class AdvertiseDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5548639181805915483L;
	@JsonIgnore
	private Long adId;	
	@JsonIgnore
    private String adName;			//名称
    @JsonIgnore
    private Long adSpaceId;		//广告位ID		
    @JsonIgnore
    private Date startTime;			
    @JsonIgnore
    private Date endTime;
    @JsonProperty(value="adImgUrl")
    private String adPic;			//图片
    @JsonProperty(value="adLink")
    private String adUrl;			//链接
    @JsonIgnore
    private Byte status;			//状态
    @JsonIgnore
    private String orderId;			//支付对应的订单ID
    @JsonIgnore
    private Date createTime;		//创建时间
    @JsonIgnore
    private Integer adTypeId;		//类型
    @JsonProperty(value="index")
    private Integer adIndex;		//类型
    private String adDesc;			//广告描述 
    
    /*----------------*/
    @JsonIgnore
    private Long cityCode;		//城市编号
    @JsonProperty(value="adSpaceCode")
    private String adPosId;		//广告位编号
    @JsonIgnore
    private Date curTime;			//创建时间
    @JsonIgnore
    private String[] adPosIds; //编码数组
    
	public AdvertiseDto() {
		super();
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public Long getAdSpaceId() {
		return adSpaceId;
	}

	public void setAdSpaceId(Long adSpaceId) {
		this.adSpaceId = adSpaceId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAdPic() {
		return adPic;
	}

	public void setAdPic(String adPic) {
		this.adPic = adPic;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAdTypeId() {
		return adTypeId;
	}

	public void setAdTypeId(Integer adTypeId) {
		this.adTypeId = adTypeId;
	}

	public Integer getAdIndex() {
		return adIndex;
	}

	public void setAdIndex(Integer adIndex) {
		this.adIndex = adIndex;
	}

	public String getAdDesc() {
		return adDesc;
	}

	public void setAdDesc(String adDesc) {
		this.adDesc = adDesc;
	}

	public Long getCityCode() {
		return cityCode;
	}

	public void setCityCode(Long cityCode) {
		this.cityCode = cityCode;
	}


	public String getAdPosId() {
		return adPosId;
	}

	public void setAdPosId(String adPosId) {
		this.adPosId = adPosId;
	}

	public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}

    public String[] getAdPosIds()
    {
        return adPosIds;
    }

    public void setAdPosIds(String[] adPosIds)
    {
        this.adPosIds = adPosIds;
    }


	
}