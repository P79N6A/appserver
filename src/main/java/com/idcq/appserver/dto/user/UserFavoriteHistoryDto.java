package com.idcq.appserver.dto.user;

import java.util.Date;

/**
 * @author Administrator
 * 用户收藏历史实体类
 */
public class UserFavoriteHistoryDto {
	/**
	 * 主键
	 */
	private Long favoriteHistoryId;
	/**
	 * 用户收藏表id
	 */
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
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date cancelTime;
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
	public Long getFavoriteHistoryId() {
		return favoriteHistoryId;
	}
	public void setFavoriteHistoryId(Long favoriteHistoryId) {
		this.favoriteHistoryId = favoriteHistoryId;
	}
	
}
