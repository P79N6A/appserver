package com.idcq.appserver.dto.shopMemberCard;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.dto.goods.GoodsSetDto;

/**
 * 商铺会员卡
 * @ClassName: ShopMemberCardDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午12:00:20 
 *
 */
public class ShopMemberCardDto {
	
    
    /**
     *  
     */
    private Long memberId;
    
	/**
	 * 店铺会员卡id
	 */
	private Integer cardId;
	
	/**
	 * 用户 编号
	 */
	private Long userId;
	
	/**
	 * 卡号
	 */
	private String oldCardNo;
	
	/**
	 * 卡类型
	 */
	private String cardType;
	@JsonIgnore
	private String cardDesc;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 持卡人名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private Integer sex;
	
	/**
	 * 已使用金额
	 */
	private Double useMoney;
	
	/**
	 * 店铺编号
	 */
	private Long shopId;
	/**
	 * 商铺logo
	 */
	private String	shopLogoUrl;
	
	/**
	 * 操作时间
	 */
	private Date opertaterTime;
	
	/**
	 * 开卡人
	 */
	private Integer opertaterId;
	
	/**
	 * 生日 
	 */
	private String birthday;
	
	/**
	 * 分页开始
	 */
	@JsonIgnore
	private Integer start;
	
	/**
	 * 分页结束
	 */
	@JsonIgnore
	private Integer limit;
	
	/**
	 * 充值金额
	 */
	@JsonIgnore
	private Double chargeMoney;
	
	/**
	 * 赠送金额
	 */
	@JsonIgnore
	private Double presentMoney;
	
	private Double cardMoney;
	
	private Double usedMoney;
	
	private Double amount;
	
	/**
	 * 开卡状态
	 */
	private Integer cardStatus;
	/**
	 * 消费金额
	 */
	@JsonIgnore
	private Double consumeMoney;
	
	/**
	 * 订单号
	 */
	@JsonIgnore
	private String orderId;
	
	/**
	 * 返回给服务端的userName
	 */
	private String userName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createCardDate;
	
	/**
	 * 使用次数
	 */
	private Integer usedNum;
	
	/**
	 * 客户端需要的使用次数，
	 */
	private Integer usedTimes;
	
	private Double totalUsedAmount;
	
	/**
	 * 次卡中消费的商品名称
	 */
	private String goodsName;
	
	private String shopName;
	
	/**
	 * 次卡中消费的商品id
	 */
	private Long goodsId;
	
	private String goodsSetId;//购买套餐（次卡 ID）
	private Date validStartTime;//有效期开始日期，填写了 goods_set_id 时必填
	private Date validEndTime;//有效期结束日期，填写了 goods_set_id 时必填
	private String isUseFinished;//是否使用完成
	private String billerId;//开卡人
	
	
	private BigDecimal goodsNumber;//商品数量
	private BigDecimal price;//商品套餐内单价
	private String goodsSetName;//套餐名称
	
	
	private List<GoodsSetDto> goodsList;//商品list
	/**
	 * 支付类型：支付宝支付-0,传奇宝支付-1,代金券支付-2,红包支付-3,优惠券支付-4,会员卡支付-5,
	 *          现金支付-6,POS支付-7,微信支付-8,建行银行卡=9,建行信用卡=10,传奇宝(消费金)=11,
	 *          传奇宝(平台奖励)=12,次卡支付=15,农行银行卡=16,农行信用卡=17
	 */
	private Integer payType;
	
	private Integer isSendSms;
	
	private Date clientRechargeTime;
	
	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	private Integer queryChargeCard;
	
	public BigDecimal getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(BigDecimal goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getGoodsSetName() {
		return goodsSetName;
	}

	public void setGoodsSetName(String goodsSetName) {
		this.goodsSetName = goodsSetName;
	}

	public List<GoodsSetDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsSetDto> goodsList) {
		this.goodsList = goodsList;
	}

	public String getGoodsSetId() {
		return goodsSetId;
	}

	public void setGoodsSetId(String goodsSetId) {
		this.goodsSetId = goodsSetId;
	}

	public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public String getIsUseFinished() {
		return isUseFinished;
	}

	public void setIsUseFinished(String isUseFinished) {
		this.isUseFinished = isUseFinished;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Double getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(Double useMoney) {
		this.useMoney = useMoney;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Integer getOperaterId() {
		return opertaterId;
	}

	public void setOperaterId(Integer opertaterId) {
		this.opertaterId = opertaterId;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Double getPresentMoney() {
		return presentMoney;
	}

	public void setPresentMoney(Double presentMoney) {
		this.presentMoney = presentMoney;
	}

	public Double getCardMoney() {
		return cardMoney;
	}

	public void setCardMoney(Double cardMoney) {
		this.cardMoney = cardMoney;
	}

	public Double getUsedMoney() {
		return usedMoney;
	}

	public void setUsedMoney(Double usedMoney) {
		this.usedMoney = usedMoney;
	}

	public Double getAmount() {
		if(amount==null){
			amount=cardMoney;
		}
		return amount;
	}

	public void setAmount(Double amount) {
		if(amount==null){
			amount=cardMoney;
		}
		this.amount = amount;
	}

	public Double getConsumeMoney() {
		return consumeMoney;
	}

	public void setConsumeMoney(Double consumeMoney) {
		this.consumeMoney = consumeMoney;
	}

	public String getOldCardNo() {
		return oldCardNo;
	}

	public void setOldCardNo(String oldCardNo) {
		this.oldCardNo = oldCardNo;
	}

	public Date getOpertaterTime() {
		return opertaterTime;
	}

	public void setOpertaterTime(Date opertaterTime) {
		this.opertaterTime = opertaterTime;
	}

	public Integer getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(Integer cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		if(userName==null){
			userName=name;
		}
		return userName;
	}

	public void setUserName(String userName) {
		if(userName==null){
			userName=name;
		}
		this.userName = userName;
	}

	public Date getCreateCardDate() {
		if(createCardDate==null){
			createCardDate=opertaterTime;
		}
		return createCardDate;
	}

	public void setCreateCardDate(Date createCardDate) {
		if(createCardDate==null){
			createCardDate=opertaterTime;
		}
		this.createCardDate = createCardDate;
	}

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}

	public Integer getUsedTimes() {
		if(usedTimes==null){
			usedTimes=usedNum;
		}
		return usedTimes;
	}

	public void setUsedTimes(Integer usedTimes) {
		if(usedTimes==null){
			usedTimes=usedNum;
		}
		this.usedTimes = usedTimes;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardDesc() {
		return cardDesc;
	}

	public void setCardDesc(String cardDesc) {
		this.cardDesc = cardDesc;
	}
	
	
	public Double getTotalUsedAmount() {
		if(totalUsedAmount==null){
			totalUsedAmount=usedMoney;
		}
		return totalUsedAmount;
	}

	public void setTotalUsedAmount(Double totalUsedAmount) {
		this.totalUsedAmount = totalUsedAmount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getQueryChargeCard() {
		return queryChargeCard;
	}

	public void setQueryChargeCard(Integer queryChargeCard) {
		this.queryChargeCard = queryChargeCard;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getIsSendSms() {
		return isSendSms;
	}

	public void setIsSendSms(Integer isSendSms) {
		this.isSendSms = isSendSms;
	}

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

	public String getShopLogoUrl() {
		return shopLogoUrl;
	}

	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

    public Date getClientRechargeTime() {
        return clientRechargeTime;
    }

    public void setClientRechargeTime(Date clientRechargeTime) {
        this.clientRechargeTime = clientRechargeTime;
    }
}
