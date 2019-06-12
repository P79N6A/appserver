package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 预定时间规则表
 * 
 * @author Administrator
 * 
 * @date 2015年7月14日
 * @time 下午4:47:26
 */
public class BookTimeRuleDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3289188835348761750L;
	private Long timeRuleId;
    private Long settingId;
    private Integer ruleType;
    private Date beginTime;
    private Date endTime;
    private Date stopBeginDate;
    private Date stopEndDate;
    private Integer weekDay;
    private Date createTime;
    
	public BookTimeRuleDto() {
		super();
	}
	
	public Long getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(Long timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getStopBeginDate() {
		return stopBeginDate;
	}
	public void setStopBeginDate(Date stopBeginDate) {
		this.stopBeginDate = stopBeginDate;
	}
	public Date getStopEndDate() {
		return stopEndDate;
	}
	public void setStopEndDate(Date stopEndDate) {
		this.stopEndDate = stopEndDate;
	}
	public Integer getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    

}