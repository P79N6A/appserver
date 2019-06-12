package com.idcq.appserver.dto.goods;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * 商品沽清表实体
 * Created by Administrator on 2016/8/19 0019.
 */
public class GoodsSoldOutDto {
    //商铺ID
    @JsonIgnore
    private Long shopId;
    //商品ID
    private Long goodsId;
    //上架状态，下架-0,上架-1,删除-2
    private Integer goodsStatus;
    //商品分类：外键到goodsCategory表
    @JsonIgnore
    private  Long goodsCategoryId;
    //操作人ID：暂存雇员ID(=0为老板)
    private Long operatorId;
    //操作人名（也可存操作人手机号码）
    private String operatorName;
    //创建时间
    @JsonProperty("soldOutTime")
    private Date createTime;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Long getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(Long goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
