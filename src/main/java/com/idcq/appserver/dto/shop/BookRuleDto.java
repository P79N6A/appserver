package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 预定资源规则dto
 * 
 * @author Administrator
 * 
 * @date 2015年5月4日
 * @time 下午5:16:17
 */
public class BookRuleDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1569413576663336227L;
	private Long bookRuleId;
    private Long shopId;
    private String ruleName;
    private Integer ruleOrder;
    private Date availableTimeFrom;
    private Date availableTimeTo;
    private String unavailableDatetimeFrom;
    private String unavailableDatetimeTo;
    private Integer bookDates;
    private Double bookHours;
    private Double bookOverdueDates;
    private Double bookOverdueHours;
    private String bookType;
    private Integer bookPeopleLimitFrom;
    private Integer bookPeopleLimitTo;
    private Integer groupId;
    private Double bookMoney;
    private Integer totalMoneyFlag;
    private Integer bookNumber;
    private Integer leastBookPrice;
    private Date createTime;
    private Integer isBook;
    private Long columnId;
    private Integer payType;
    private String remark;
    
	public BookRuleDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getBookRuleId() {
		return bookRuleId;
	}
	public void setBookRuleId(Long bookRuleId) {
		this.bookRuleId = bookRuleId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Integer getRuleOrder() {
		return ruleOrder;
	}
	public void setRuleOrder(Integer ruleOrder) {
		this.ruleOrder = ruleOrder;
	}
	public Date getAvailableTimeFrom() {
		return availableTimeFrom;
	}
	public void setAvailableTimeFrom(Date availableTimeFrom) {
		this.availableTimeFrom = availableTimeFrom;
	}
	public Date getAvailableTimeTo() {
		return availableTimeTo;
	}
	public void setAvailableTimeTo(Date availableTimeTo) {
		this.availableTimeTo = availableTimeTo;
	}
	public String getUnavailableDatetimeFrom() {
		return unavailableDatetimeFrom;
	}
	public void setUnavailableDatetimeFrom(String unavailableDatetimeFrom) {
		this.unavailableDatetimeFrom = unavailableDatetimeFrom;
	}
	
	public String getUnavailableDatetimeTo() {
		return unavailableDatetimeTo;
	}
	public void setUnavailableDatetimeTo(String unavailableDatetimeTo) {
		this.unavailableDatetimeTo = unavailableDatetimeTo;
	}
	public Integer getBookDates() {
		return bookDates;
	}
	public void setBookDates(Integer bookDates) {
		this.bookDates = bookDates;
	}
	public Double getBookHours() {
		return bookHours;
	}
	public void setBookHours(Double bookHours) {
		this.bookHours = bookHours;
	}
	public Double getBookOverdueDates() {
		return bookOverdueDates;
	}
	public void setBookOverdueDates(Double bookOverdueDates) {
		this.bookOverdueDates = bookOverdueDates;
	}
	public Double getBookOverdueHours() {
		return bookOverdueHours;
	}
	public void setBookOverdueHours(Double bookOverdueHours) {
		this.bookOverdueHours = bookOverdueHours;
	}
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	public Integer getBookPeopleLimitFrom() {
		return bookPeopleLimitFrom;
	}
	public void setBookPeopleLimitFrom(Integer bookPeopleLimitFrom) {
		this.bookPeopleLimitFrom = bookPeopleLimitFrom;
	}
	public Integer getBookPeopleLimitTo() {
		return bookPeopleLimitTo;
	}
	public void setBookPeopleLimitTo(Integer bookPeopleLimitTo) {
		this.bookPeopleLimitTo = bookPeopleLimitTo;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Double getBookMoney() {
		return bookMoney;
	}
	public void setBookMoney(Double bookMoney) {
		this.bookMoney = bookMoney;
	}
	public Integer getTotalMoneyFlag() {
		return totalMoneyFlag;
	}
	public void setTotalMoneyFlag(Integer totalMoneyFlag) {
		this.totalMoneyFlag = totalMoneyFlag;
	}
	public Integer getBookNumber() {
		return bookNumber;
	}
	public void setBookNumber(Integer bookNumber) {
		this.bookNumber = bookNumber;
	}
	public Integer getLeastBookPrice() {
		return leastBookPrice;
	}
	public void setLeastBookPrice(Integer leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getIsBook() {
		return isBook;
	}
	public void setIsBook(Integer isBook) {
		this.isBook = isBook;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
    
}