/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.shop.HandoverDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年10月20日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.common.annotation.Check;
import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 交接表信息
 * @author ChenYongxin
 *
 */
public class HandoverInfoDto implements Serializable{

	    private static final long serialVersionUID = -5402141905485454830L;
	    
	    
	    private Long handoverInfoId;//主键
	    
	    @Check(required=true)/*必填字段增加此注释*/
	    private Long shopId; //商铺ID
	    
	    @JsonIgnore
	    private String token;//令牌
	    
	    @Check(required = true)
	    private Long localId;//收银机主键
	    
	    private String cashierName; //收银员名称
	    
	    
	    private Long cashierId; //收银员id
	    
	    @JsonSerialize(using = CustomDateSerializer.class) 
	    private Date logoutTime; //退出时间
	    
	    @JsonSerialize(using = CustomDateSerializer.class) 
	    private Date handoverTime; //交接时间
	    
	    @JsonSerialize(using = CustomDateSerializer.class) 
	    private Date loginTime; //登录时间
	    
	    
	    private Integer deleteOrderNum; //退单总数
	    
	    private Integer bookOrderNum; //预订订单数
	    
	    private Double memberPosTurnover; //会员订单 pos机收入
	    
	    private Double nonmemberPosTurnover; //非会员订单 pos机收入
	    
	    private Double memberOnlinePayTurnover; //会员线上支付总额
	    
	    private Double nonmemberOnlinePayTurnover; //非会员线上支付总额
	    
	    private Double memberCashTurnover; //会员订单 现金收入
	    
	    private Double nonmemberCashTurnover; //非会员订单 现金收入
	    
	    private Double memberMalingAmount; //会员订单 抹零抹去金额
	    
	    private Double nonmemberMalingAmount; //非会员订单 抹零抹去金额
	    
	    private Integer isHandover; //是否已经交接完成：0=否；1=是
	    
	    private Integer createOrdersNum; //开总单数
	    
	    private Double advance; //预付款
	    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private Date updateTime; //更新时间

	    private String deviceId; //设备ID
	    
	    private Double memberOrderTurnover; //会员订单营业总额
	    
	    private Integer memberOrdersNum; //会员结账订单数
	    
	    private Integer notPayOrdersNum; //未结单数
	    
	    private Integer nonmembermallingNum; //非会员订单  抹零总数
	    
	    private Integer membermallingNum; //会员订单  抹零总数
	    
	    private Integer peopleNum; //总人数
	    
	    private Double tuicaiNum; //退菜总数
	    
	    private Double nonmemberTurnover; //非会员订单营业总额
	    private String clientSign;//客户端标识
	    
	    private Integer payOrdersNum ; //结单数
	    
	    private Double turnoverAll ; //营业总额
	    
	    private Double revolveTurnover ; //备用金额
	    
	    private Double cashRechargeTurnover ;// 会员现金充值
	    
	    private Double posRechargeTurnover ;// 会员POST充值
	    
	    private Double saleTurnover; // 销售总额
	    
	    private String handoverCode;//唯一编码，生成规则同订单号
	    
	    public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		private Double allCashTurnover ; // 现金收银
	    
	    private Double allPosTurnover ; // POS收银
	    
	    private Double allOnlineTurnover ; //线上收银总额
	    @JsonIgnore
	    private String startTime; 
	    
	    @JsonIgnore
	    private String endTime; 
	    
	    @JsonIgnore
	    private Integer pageSize;
	    
	    @JsonIgnore
	    private Integer pageNo;

		public Long getLocalId() {
			return localId;
		}

		public void setLocalId(Long localId) {
			this.localId = localId;
		}

		public String getCashierName() {
			return cashierName;
		}

		public void setCashierName(String cashierName) {
			this.cashierName = cashierName;
		}

		public Long getCashierId() {
			return cashierId;
		}

		public void setCashierId(Long cashierId) {
			this.cashierId = cashierId;
		}


		public Integer getDelOrderNum() {
			return deleteOrderNum;
		}

		public void setDelOrderNum(Integer deleteOrderNum) {
			this.deleteOrderNum = deleteOrderNum;
		}

		public Integer getBookOrderNum() {
			return bookOrderNum;
		}

		public void setBookOrderNum(Integer bookOrderNum) {
			this.bookOrderNum = bookOrderNum;
		}

		public Double getMemberPosTurnover() {
			return memberPosTurnover;
		}

		public void setMemberPosTurnover(Double memberPosTurnover) {
			this.memberPosTurnover = memberPosTurnover;
		}

		public Double getNonmemberPosTurnover() {
			return nonmemberPosTurnover;
		}

		public void setNonmemberPosTurnover(Double nonmemberPosTurnover) {
			this.nonmemberPosTurnover = nonmemberPosTurnover;
		}

		public Double getMemberOnlinePayTurnover() {
			return memberOnlinePayTurnover;
		}

		public void setMemberOnlinePayTurnover(Double memberOnlinePayTurnover) {
			this.memberOnlinePayTurnover = memberOnlinePayTurnover;
		}

		public Double getNonmemberOnlinePayTurnover() {
			return nonmemberOnlinePayTurnover;
		}

		public void setNonmemberOnlinePayTurnover(Double nonmemberOnlinePayTurnover) {
			this.nonmemberOnlinePayTurnover = nonmemberOnlinePayTurnover;
		}

		public Double getMemberCashTurnover() {
			return memberCashTurnover;
		}

		public void setMemberCashTurnover(Double memberCashTurnover) {
			this.memberCashTurnover = memberCashTurnover;
		}

		public Double getNonmemberCashTurnover() {
			return nonmemberCashTurnover;
		}

		public Date getLogoutTime() {
			return logoutTime;
		}

		public void setLogoutTime(Date logoutTime) {
			this.logoutTime = logoutTime;
		}

		public Date getHandoverTime() {
			return handoverTime;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public Integer getPageSize() {
			return pageSize;
		}

		public void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}

		public Integer getPageNo() {
			return pageNo;
		}

		public void setPageNo(Integer pageNo) {
			this.pageNo = pageNo;
		}

		public void setHandoverTime(Date handoverTime) {
			this.handoverTime = handoverTime;
		}

		public Date getLoginTime() {
			return loginTime;
		}

		public String getHandoverCode() {
			return handoverCode;
		}

		public void setHandoverCode(String handoverCode) {
			this.handoverCode = handoverCode;
		}

		public void setLoginTime(Date loginTime) {
			this.loginTime = loginTime;
		}

		public void setNonmemberCashTurnover(Double nonmemberCashTurnover) {
			this.nonmemberCashTurnover = nonmemberCashTurnover;
		}

		public Double getMemberMalingAmount() {
			return memberMalingAmount;
		}

		public void setMemberMalingAmount(Double memberMalingAmount) {
			this.memberMalingAmount = memberMalingAmount;
		}

		public Double getNonmemberMalingAmount() {
			return nonmemberMalingAmount;
		}

		public void setNonmemberMalingAmount(Double nonmemberMalingAmount) {
			this.nonmemberMalingAmount = nonmemberMalingAmount;
		}

		public Long getShopId() {
			return shopId;
		}

		public void setShopId(Long shopId) {
			this.shopId = shopId;
		}

		public Integer getIsHandover() {
			return isHandover;
		}

		public void setIsHandover(Integer isHandover) {
			this.isHandover = isHandover;
		}


		public Double getAdvance() {
			return advance;
		}

		public void setAdvance(Double advance) {
			this.advance = advance;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public Long getHandoverInfoId() {
			return handoverInfoId;
		}

		public void setHandoverInfoId(Long handoverInfoId) {
			this.handoverInfoId = handoverInfoId;
		}

		public Integer getDeleteOrderNum() {
			return deleteOrderNum;
		}

		public void setDeleteOrderNum(Integer deleteOrderNum) {
			this.deleteOrderNum = deleteOrderNum;
		}


		public Double getMemberOrderTurnover() {
			return memberOrderTurnover;
		}

		public void setMemberOrderTurnover(Double memberOrderTurnover) {
			this.memberOrderTurnover = memberOrderTurnover;
		}

		public Integer getMemberOrdersNum() {
			return memberOrdersNum;
		}

		public void setMemberOrdersNum(Integer memberOrdersNum) {
			this.memberOrdersNum = memberOrdersNum;
		}

		public Integer getNotPayOrdersNum() {
			return notPayOrdersNum;
		}

		public void setNotPayOrdersNum(Integer notPayOrdersNum) {
			this.notPayOrdersNum = notPayOrdersNum;
		}

		public Integer getNonmembermallingNum() {
			return nonmembermallingNum;
		}

		public void setNonmembermallingNum(Integer nonmembermallingNum) {
			this.nonmembermallingNum = nonmembermallingNum;
		}

		public Integer getMembermallingNum() {
			return membermallingNum;
		}

		public void setMembermallingNum(Integer membermallingNum) {
			this.membermallingNum = membermallingNum;
		}

		public Integer getCreateOrdersNum() {
			return createOrdersNum;
		}

		public void setCreateOrdersNum(Integer createOrdersNum) {
			this.createOrdersNum = createOrdersNum;
		}

		public Integer getPeopleNum() {
			return peopleNum;
		}

		public void setPeopleNum(Integer peopleNum) {
			this.peopleNum = peopleNum;
		}

		public Double getTuicaiNum() {
			return tuicaiNum;
		}

		public void setTuicaiNum(Double tuicaiNum) {
			this.tuicaiNum = tuicaiNum;
		}

		public Double getNonmemberTurnover() {
			return nonmemberTurnover;
		}

		public void setNonmemberTurnover(Double nonmemberTurnover) {
			this.nonmemberTurnover = nonmemberTurnover;
		}

		public String getClientSign() {
			return clientSign;
		}

		public void setClientSign(String clientSign) {
			this.clientSign = clientSign;
		}

		public Integer getPayOrdersNum() {
			return payOrdersNum;
		}

		public void setPayOrdersNum(Integer payOrdersNum) {
			this.payOrdersNum = payOrdersNum;
		}

		public Double getTurnoverAll() {
			return turnoverAll;
		}

		public void setTurnoverAll(Double turnoverAll) {
			this.turnoverAll = turnoverAll;
		}

		public Double getRevolveTurnover() {
			return revolveTurnover;
		}

		public void setRevolveTurnover(Double revolveTurnover) {
			this.revolveTurnover = revolveTurnover;
		}

		public Double getCashRechargeTurnover() {
			return cashRechargeTurnover;
		}

		public void setCashRechargeTurnover(Double cashRechargeTurnover) {
			this.cashRechargeTurnover = cashRechargeTurnover;
		}

		public Double getPosRechargeTurnover() {
			return posRechargeTurnover;
		}

		public void setPosRechargeTurnover(Double posRechargeTurnover) {
			this.posRechargeTurnover = posRechargeTurnover;
		}

		public Double getSaleTurnover() {
			return saleTurnover;
		}

		public void setSaleTurnover(Double saleTurnover) {
			this.saleTurnover = saleTurnover;
		}

		public Double getAllCashTurnover() {
			return allCashTurnover;
		}

		public void setAllCashTurnover(Double allCashTurnover) {
			this.allCashTurnover = allCashTurnover;
		}

		public Double getAllPosTurnover() {
			return allPosTurnover;
		}

		public void setAllPosTurnover(Double allPosTurnover) {
			this.allPosTurnover = allPosTurnover;
		}

		public Double getAllOnlineTurnover() {
			return allOnlineTurnover;
		}

		public void setAllOnlineTurnover(Double allOnlineTurnover) {
			this.allOnlineTurnover = allOnlineTurnover;
		}
	    
	    
}
