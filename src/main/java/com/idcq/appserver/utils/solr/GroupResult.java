package com.idcq.appserver.utils.solr;

import java.util.List;
/**
 * 分组结果
 * @ClassName: GroupResult 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 上午9:46:01 
 *
 */
public class GroupResult {
	
	private String shopId;
	
	private List<SearchContent>data;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public List<SearchContent> getData() {
		return data;
	}

	public void setData(List<SearchContent> data) {
		this.data = data;
	}
}
