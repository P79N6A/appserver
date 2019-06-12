/**
 * 
 */
package com.idcq.appserver.dto.bank;

import java.io.Serializable;
import java.util.Date;

/** 
 * @ClassName: BankDto 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月15日 下午2:39:13 
 *  
 */
public class BankDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7456055412989110433L;

	/**
	 * 银行id
	 */
	private Long bankId;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 展示顺序
	 */
	private Long showIndex;
	
	/**
	 * 银行Logo
	 */
	private String bankLogoUrl;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public BankDto() {
		super();
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
	}

	public String getBankLogoUrl() {
		return bankLogoUrl;
	}

	public void setBankLogoUrl(String bankLogoUrl) {
		this.bankLogoUrl = bankLogoUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
	