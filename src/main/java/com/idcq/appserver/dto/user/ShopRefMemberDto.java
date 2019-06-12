package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ShopRefMemberDto implements Serializable {
	private static final long serialVersionUID = 4549146677985881424L;
	
	@JsonIgnore
    private Long totalSum;
	@JsonIgnore
	private Long termSum;
	public Long getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(Long totalSum) {
		this.totalSum = totalSum;
	}
	public Long getTermSum() {
		return termSum;
	}
	public void setTermSum(Long termSum) {
		this.termSum = termSum;
	}
	
	
}
