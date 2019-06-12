package com.idcq.appserver.dto.common;

public class AttachmentRelationDto {
	/**
	 * 附件关系id
	 */
	private Integer attachmentRelationId;
	
	/**
	 * 附件id
	 */
	private Long attachmentId;
	
	/**
	 * 业务主键
	 */
	private Long bizId;
	
	/**
	 * 业务主键类型
	 */
	private Integer bizType;
	
	/**
	 * 图片类型
	 */
	private Integer picType;
	
	/**
	 * 展示顺序
	 */
	private Integer bizIndex;
	
	private String fileUrl;
	/**
	 * 证书/证件号
	 */
	private String fileNo;

	public Integer getAttachmentRelationId() {
		return attachmentRelationId;
	}

	public void setAttachmentRelationId(Integer attachmentRelationId) {
		this.attachmentRelationId = attachmentRelationId;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getPicType() {
		return picType;
	}

	public void setPicType(Integer picType) {
		this.picType = picType;
	}

	public Integer getBizIndex() {
		return bizIndex;
	}

	public void setBizIndex(Integer bizIndex) {
		this.bizIndex = bizIndex;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
}
