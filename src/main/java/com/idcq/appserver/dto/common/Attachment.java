/**
 * 
 */
package com.idcq.appserver.dto.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/** 
 * 附件实体类
 * @ClassName: Attachment 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 上午11:23:30 
 *  
 */
public class Attachment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4615838374159342633L;

	/**
	 * 附件编号
	 */
	private Long attachmentId;
	
	/**
	 * 附件路径
	 */
	private String 	fileUrl;
	
	//文件名
	private String fileName;
	
	//文件大小
	private Double fileSize;

	//文件类型
	private String fileType;
	
	//上传者id
	private Long uploadUserId;
	
	//上传者类型
	private Integer uploadUserType;
	
	//上传时间
	private Date createTime;
	//'对应业务表的主键，比如shop_id,user_id,bank_id',
	private Long bizId;
	//类型
	private String bizType;

	public Map<String, Object> getPluginDownladRecord() {
		return pluginDownladRecord;
	}

	public void setPluginDownladRecord(Map<String, Object> pluginDownladRecord) {
		this.pluginDownladRecord = pluginDownladRecord;
	}

	/**
	 * 该字段在下载应用时使用，存在时则会记录应用下载次数
	 */
	@JsonIgnore
	private transient Map<String, Object> pluginDownladRecord;
	
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

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getFileSize() {
		return fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Integer getUploadUserType() {
		return uploadUserType;
	}

	public void setUploadUserType(Integer uploadUserType) {
		this.uploadUserType = uploadUserType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
