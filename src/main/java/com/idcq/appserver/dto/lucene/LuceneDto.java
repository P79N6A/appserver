package com.idcq.appserver.dto.lucene;

import java.io.Serializable;
import java.util.Date;

public class LuceneDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1281485613985096373L;
	private Long id;
    private Long recordId;
    private Integer recordType;
    private Integer operation;
    private Integer flag;
    private Date createTime;
    
	public LuceneDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
  
}