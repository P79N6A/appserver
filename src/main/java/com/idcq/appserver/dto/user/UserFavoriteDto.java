package com.idcq.appserver.dto.user;

import java.util.Date;

/**
 * @author Administrator
 * 用户收藏历史记录实体类
 */
public class UserFavoriteDto {
	
/*	  `favorite_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，唯一标识一个收藏',
	  `user_id` int(10) unsigned NOT NULL COMMENT '用户ID',
	  `biz_id` int(10) unsigned NOT NULL COMMENT '收藏类型为商铺存shop_id，为商品存goods_id',
	  `biz_type` tinyint(5) unsigned NOT NULL DEFAULT '0' COMMENT '收藏类型：店铺=0,商品=1,其他=2',
	  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	  `favorite_url` varchar(1000) DEFAULT NULL COMMENT '分类链接的URL地址',*/
	private Long favoriteId;
	/**
	 * 用户id
	 * 
	 */
	private Long userId;
	/**
	 * '收藏类型为商铺存shop_id，为商品存goods_id',
	 */
	private Long bizId;
	/**
	 * 收藏类型：店铺=0,商品=1,其他=2
	 */
	private Integer bizType;
	/**
	 * 创建时间（为用户收藏时间）
	 */
	private Date createTime;
	/**
	 * 取消收藏时间
	 */
	private Date cancelTime;
	/**
	 * 操作类型
	 */
	private Integer operType;
	/**
	 * 分类链接的URL地址
	 */
	private String favoriteUrl;
	
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public Long getFavoriteId() {
		return favoriteId;
	}
	public void setFavoriteId(Long favoriteId) {
		this.favoriteId = favoriteId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFavoriteUrl() {
		return favoriteUrl;
	}
	public void setFavoriteUrl(String favoriteUrl) {
		this.favoriteUrl = favoriteUrl;
	}
	
}
