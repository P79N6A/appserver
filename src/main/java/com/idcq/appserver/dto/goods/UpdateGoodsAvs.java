package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;

public class UpdateGoodsAvs implements Serializable {
	private static final long serialVersionUID = 8097978196187226358L;
	/*	
		token	string		条件	设备令牌。Token鉴权方式必填
		shopId	int		    是	      商铺Id
		operateType	int		是	操作类型： 0-新增，1-修改
		avsList	list		否	增值服务列表
   */
	private String token;
	@Check
	private Long shopId;
	@Check
	private Integer operateType;
	@Check
	private List<GoodsAvsDto> avsList;
	
	public Long getShopId() {
		return shopId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public List<GoodsAvsDto> getAvsList() {
		return avsList;
	}
	public void setAvsList(List<GoodsAvsDto> avsList) {
		this.avsList = avsList;
	}
	
}
