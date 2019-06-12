package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 技师信息dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月29日
 * @time 下午5:45:05
 */
public class ShopTechnicianDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8979005057653994402L;
	private Long techId;
    private Long shopId;		
    private Integer serverMode;		//服务方式
    private Integer technicianOrder;//技师排序
    private Integer workStatus;		//服务状态
    private String techName;	//名称
    private Integer techTypeId;//技师类型ID
    private String skill;			//技能说明/特长
    private BigDecimal workLife;	//工作年限
    private Integer sex;			//性别
    private String description;		//描述
    private Integer attachementId;//技师logo
    private Date createTime;		
    private Date lastUpdateTime;		
    private Integer receptionNumber;//接单数
    private Integer isValid;		//是否有效
    private String workNumber;//工号
    private String technicianSimpleName;//技师姓名首字母
    private String birthday;//技师生日
    private String phone;//技师联系方式
    private String token;//
    
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTechnicianSimpleName() {
		return technicianSimpleName;
	}

	public void setTechnicianSimpleName(String technicianSimpleName) {
		this.technicianSimpleName = technicianSimpleName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
     * 操作类型
     */
    private Integer operateType;
    /**
     * 技师服务商品族id
     */
    private String goodsGroupIds;
    
    
	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public String getGoodsGroupIds() {
		return goodsGroupIds;
	}

	public void setGoodsGroupIds(String goodsGroupIds) {
		this.goodsGroupIds = goodsGroupIds;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public ShopTechnicianDto() {
		super();
	}


	public Long getTechId() {
		return techId;
	}

	public void setTechId(Long techId) {
		this.techId = techId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getServerMode() {
		return serverMode;
	}

	public void setServerMode(Integer serverMode) {
		this.serverMode = serverMode;
	}

	public Integer getTechnicianOrder() {
		return technicianOrder;
	}

	public void setTechnicianOrder(Integer technicianOrder) {
		this.technicianOrder = technicianOrder;
	}

	public Integer getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(Integer workStatus) {
		this.workStatus = workStatus;
	}



	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}


	public BigDecimal getWorkLife() {
		return workLife;
	}

	public void setWorkLife(BigDecimal workLife) {
		this.workLife = workLife;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

	public Integer getTechTypeId() {
		return techTypeId;
	}

	public void setTechTypeId(Integer techTypeId) {
		this.techTypeId = techTypeId;
	}

	public Integer getAttachementId() {
		return attachementId;
	}

	public void setAttachementId(Integer attachementId) {
		this.attachementId = attachementId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getReceptionNumber() {
		return receptionNumber;
	}

	public void setReceptionNumber(Integer receptionNumber) {
		this.receptionNumber = receptionNumber;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	
    
    
    

}