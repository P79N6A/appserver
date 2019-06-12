package com.idcq.appserver.dto.column;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 栏目dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 上午10:18:32
 */
public class ColumnDto implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5528966660549067164L;

    private Long columnId;

    private String columnName; // 栏目名称

    private String columnDesc; // 栏目描述

    @JsonProperty(value = "index")
    private Long columnIndex; // 排序

    @JsonIgnore
    private Integer columnStatus; // 栏目状态

    @JsonIgnore
    private Long parentColumnId; // 父栏目ID

    private String columnImgUrl; // 图片

    private String columnUrl; // 连接

    @JsonIgnore
    private Long cityId;

    /*-----------------*/
    private Integer columnType;

    @JsonIgnore
    private Long shopId;
    
    private Integer hasChildren; // 是否有子分类: 没有=0，有=1
    @JsonIgnore
    private Integer shopClassify;//商户类型:1-个人商户；2-个体商户；3-企业商户,
    
    

    public Integer getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}

	public ColumnDto()
    {
        super();
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnDesc()
    {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc)
    {
        this.columnDesc = columnDesc;
    }

    public Long getColumnId()
    {
        return columnId;
    }

    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnIndex()
    {
        return columnIndex;
    }

    public void setColumnIndex(Long columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    public Long getParentColumnId()
    {
        return parentColumnId;
    }

    public void setParentColumnId(Long parentColumnId)
    {
        this.parentColumnId = parentColumnId;
    }

    public Integer getColumnStatus()
    {
        return columnStatus;
    }

    public void setColumnStatus(Integer columnStatus)
    {
        this.columnStatus = columnStatus;
    }

    public String getColumnImgUrl()
    {
        return columnImgUrl;
    }

    public void setColumnImgUrl(String columnImgUrl)
    {
        this.columnImgUrl = columnImgUrl;
    }

    public String getColumnUrl()
    {
        return columnUrl;
    }

    public void setColumnUrl(String columnUrl)
    {
        this.columnUrl = columnUrl;
    }

    public Long getCityId()
    {
        return cityId;
    }

    public void setCityId(Long cityId)
    {
        this.cityId = cityId;
    }

    public Integer getColumnType()
    {
        return columnType;
    }

    public void setColumnType(Integer columnType)
    {
        this.columnType = columnType;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

	public Integer getShopClassify() {
		return shopClassify;
	}

	public void setShopClassify(Integer shopClassify) {
		this.shopClassify = shopClassify;
	}

}