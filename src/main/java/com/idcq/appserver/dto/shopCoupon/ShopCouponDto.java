package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.common.annotation.Check;


public class ShopCouponDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7608263873727486881L;
	
	/**
	 * 店铺优惠券id
	 */
	private Integer shopCouponId;
	
	/**
	 * 店铺id
	 */
	private Long shopId;
	
	/**
	 * 优惠券状态：启用-1,停用-0
	 */
	private Integer couponStatus;
	
	/**
	 * 优惠券名称
	 */
	@Check(required=false,sensitive=true)/*必填字段增加此注释*/
	private String couponName;
	
	/**
	 * 优惠券适用类型：全部商品-1，指定商品类别-2
	 */
	private Integer couponType;
	
	/**
	 * 本次领取优惠券数目
	 */
	private Integer receiveNum;
	
	/**
	 * 优惠券面额
	 */
	private Double couponAmount;
	
	/**
	 * 优惠券使用条件,暂存满多少金额使用
	 */
	private Double couponUsedCondition;
	
	/**
	 * 每单可使用优惠券数
	 */
	private Integer maxNumPerOrder;
	
	/**
	 * 每人限领优惠券数
	 */
	private Integer maxNumPerPerson;
	
	/**
	 * 是否允许分享领取：允许-1，不允许-0
	 */
	private Integer isShared;
	
	/**
	 * 是否允许与其他优惠券共用：允许-1，不允许-0
	 */
	private Integer isUsedTogether;
	
	/**
	 * 优惠券发行总数
	 */
	private Integer totalNum;
	
	/**
	 * 优惠券已领取总数
	 */
	private Integer getTotalNum;
	
	/**
	 * 优惠券已使用总数
	 */
	private Integer usedTotalNum;
	
	/**
	 * 领取起始日期
	 */
	private Date issueFromDate;
	
	/**
	 * 领取截止日期
	 */
	private Date issueToDate;
	
	/**
	 * 有效期开始日期
	 */
//	@Check(pattern="yyyy-MM-dd")
	private Date beginDate;
	
	/**
	 * 有效期结束日期
	 */
//	@Check(pattern="yyyy-MM-dd")
	private Date endDate;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 使用须知
	 */
	private String couponRemark;
	
	/**
	 * 是否删除：是-1，否-0
	 */
	private Integer isDelete;
	
	/**
	 * 商品分类（当优惠券类型=2时必填）：多个通过英文逗号(,)分隔，如1,2,3
	 */
	private String goodsCategoryIds;
	private List<Integer> goodsCategoryIdsList = new ArrayList<Integer>();
	
	/**
	 * 适用门店Id,多个通过英文逗号(,)分隔，如1,2,3
	 */
	private String availableShopIds ;
	private List<Integer> availableShopIdsList = new ArrayList<Integer>();
	
	private Integer pageNo;
	
	private Integer pageSize;
	
	private String mobile;
	
	private Long userId;
	
	/**
	 * 用户优惠券id，多个以英文逗号分隔
	 */
	@JsonIgnore
	private String userShopCouponIds;
	
	/**
	 * 用户优惠券使用状态：未使用=0,已使用=1,过期=2
	 */
	@JsonIgnore
	private String userShopCouponStatus;
	
	/**
	 * 用户使用优惠券使用标识：1-是；0-否，userShopCouponIds填写时必填 
	 */
	@JsonIgnore
	private Integer usedFlag;
	
	/**
	 * 排序： 领取优惠券时间=1，有效期截止日期=2
	 */
	@JsonIgnore
	private Integer orderBy;

	/**
	 * 排序方式：倒序-0；顺序-1
	 */
	@JsonIgnore
	private Integer orderByMode;
	@JsonIgnore
	private List<Integer> userShopCouponIdsList = new ArrayList<Integer>();
	private List<Long> userShopCouponStatusList;
	private Long userShopCouponId;//用户优惠券id
	
	public List<Integer> getUserShopCouponIdsList() {
		return userShopCouponIdsList;
	}

	public void setUserShopCouponIdsList(List<Integer> userShopCouponIdsList) {
		this.userShopCouponIdsList = userShopCouponIdsList;
	}

	public String getUserShopCouponIds() {
		return userShopCouponIds;
	}

	public void setUserShopCouponIds(String userShopCouponIds) {
		this.userShopCouponIds = userShopCouponIds;
	}

	public String getUserShopCouponStatus() {
		return userShopCouponStatus;
	}

	public void setUserShopCouponStatus(String userShopCouponStatus) {
		this.userShopCouponStatus = userShopCouponStatus;
	}

	public Integer getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(Integer usedFlag) {
		this.usedFlag = usedFlag;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderByMode() {
		return orderByMode;
	}

	public void setOrderByMode(Integer orderByMode) {
		this.orderByMode = orderByMode;
	}

	public Integer getShopCouponId() {
		return shopCouponId;
	}

	public void setShopCouponId(Integer shopCouponId) {
		this.shopCouponId = shopCouponId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Double getCouponUsedCondition() {
		return couponUsedCondition;
	}

	public void setCouponUsedCondition(Double couponUsedCondition) {
		this.couponUsedCondition = couponUsedCondition;
	}

	public Integer getMaxNumPerOrder() {
		return maxNumPerOrder;
	}

	public void setMaxNumPerOrder(Integer maxNumPerOrder) {
		this.maxNumPerOrder = maxNumPerOrder;
	}

	public Integer getMaxNumPerPerson() {
		return maxNumPerPerson;
	}

	public void setMaxNumPerPerson(Integer maxNumPerPerson) {
		this.maxNumPerPerson = maxNumPerPerson;
	}

	public Integer getIsShared() {
		return isShared;
	}

	public void setIsShared(Integer isShared) {
		this.isShared = isShared;
	}

	public Integer getIsUsedTogether() {
		return isUsedTogether;
	}

	public void setIsUsedTogether(Integer isUsedTogether) {
		this.isUsedTogether = isUsedTogether;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getGetTotalNum() {
		return getTotalNum;
	}

	public void setGetTotalNum(Integer getTotalNum) {
		this.getTotalNum = getTotalNum;
	}

	public Integer getUsedTotalNum() {
		return usedTotalNum;
	}

	public void setUsedTotalNum(Integer usedTotalNum) {
		this.usedTotalNum = usedTotalNum;
	}

	public Date getIssueFromDate() {
		return issueFromDate;
	}

	public void setIssueFromDate(Date issueFromDate) {
		this.issueFromDate = issueFromDate;
	}

	public Date getIssueToDate() {
		return issueToDate;
	}

	public void setIssueToDate(Date issueToDate) {
		this.issueToDate = issueToDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCouponRemark() {
		return couponRemark;
	}

	public void setCouponRemark(String couponRemark) {
		this.couponRemark = couponRemark;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getGoodsCategoryIds() {
		return goodsCategoryIds;
	}

	public void setGoodsCategoryIds(String goodsCategoryIds) {
		this.goodsCategoryIds = goodsCategoryIds;
	}

	public String getAvailableShopIds() {
		return availableShopIds;
	}

	public void setAvailableShopIds(String availableShopIds) {
		this.availableShopIds = availableShopIds;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Integer> getGoodsCategoryIdsList() {
		return goodsCategoryIdsList;
	}

	public void setGoodsCategoryIdsList(List<Integer> goodsCategoryIdsList) {
		this.goodsCategoryIdsList = goodsCategoryIdsList;
	}

	public List<Integer> getAvailableShopIdsList() {
		return availableShopIdsList;
	}

	public void setAvailableShopIdsList(List<Integer> availableShopIdsList) {
		this.availableShopIdsList = availableShopIdsList;
	}

	public List<Long> getUserShopCouponStatusList() {
		return userShopCouponStatusList;
	}

	public void setUserShopCouponStatusList(List<Long> userShopCouponStatusList) {
		this.userShopCouponStatusList = userShopCouponStatusList;
	}

	public Long getUserShopCouponId() {
		return userShopCouponId;
	}

	public void setUserShopCouponId(Long userShopCouponId) {
		this.userShopCouponId = userShopCouponId;
	}

	public Integer getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(Integer receiveNum) {
		this.receiveNum = receiveNum;
	}	
	
}
