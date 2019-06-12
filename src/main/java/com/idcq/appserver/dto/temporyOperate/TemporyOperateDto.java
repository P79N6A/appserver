package com.idcq.appserver.dto.temporyOperate;
/**
 * 保存用户的操作 
* @ClassName: TemporyOperateDto 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 张鹏程 
* @date 2015年3月28日 下午5:46:19 
*
 */
public class TemporyOperateDto {
	
	/**
	 * 关联id
	 */
	private Long id;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 操作类型
	 */
	private String operateType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
}
