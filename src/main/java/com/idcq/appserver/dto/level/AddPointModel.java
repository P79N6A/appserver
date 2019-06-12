package com.idcq.appserver.dto.level;

import java.io.Serializable;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;

/**
 * 增加积分model
 * @author ChenYongxin
 *
 */
public class AddPointModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3430272067180471612L;

	/*    
	 *  token	string		条件	设备令牌。Token鉴权方式必填
	    shopId	int		条件	商铺ID，token鉴权时shopId必填
	    bizType	int		是	业务类型:店铺类型=1,会员类型=2
	    bizId	int		是	业务id(店铺Id ,会员Id)
	    ruleType	int		是	积分规则类型
	    subRuleType	int		是	积分规则子类型
        pointSourceType	int		否	积分来源类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
        pointSourceId	string		否	积分来源Id
	  */
    private Long shopId;
    private Long operaterId;
    private String operaterName;
	private String token;
    @Check(recurse=true)
    List<AddPointDetailModel> addPointList;

    public String getOperaterName() {
		return operaterName;
	}
	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}
	public List<AddPointDetailModel> getAddPointList() {
		return addPointList;
	}
	public void setAddPointList(List<AddPointDetailModel> addPointList) {
		this.addPointList = addPointList;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getOperaterId() {
		return operaterId;
	}
	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}

}
