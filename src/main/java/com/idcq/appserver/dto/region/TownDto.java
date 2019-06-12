package com.idcq.appserver.dto.region;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class TownDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -617587869134999731L;
	
	private Long townId;
	
	private String townName;
	
	@JsonIgnore
	private Long districtId;
	
	@JsonIgnore
	private String dateCreated;
	
	@JsonIgnore
	private String dateUpdated;

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
}
