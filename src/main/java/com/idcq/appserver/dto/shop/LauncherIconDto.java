package com.idcq.appserver.dto.shop;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class LauncherIconDto implements Serializable{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1070857705210856167L;
	
	@JsonIgnore
	private Long id;
    
    private Integer position;
   
    private Integer jumpType;
   
    private String jumpValue;
    @JsonIgnore
    private Integer launcherType;

    private String altText;
    
    private String iconUrl;

	public LauncherIconDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getJumpType() {
		return jumpType;
	}

	public void setJumpType(Integer jumpType) {
		this.jumpType = jumpType;
	}

	public String getJumpValue() {
		return jumpValue;
	}

	public void setJumpValue(String jumpValue) {
		this.jumpValue = jumpValue;
	}

	public Integer getLauncherType() {
		return launcherType;
	}

	public void setLauncherType(Integer launcherType) {
		this.launcherType = launcherType;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	
	
    
	
	
	
    
}