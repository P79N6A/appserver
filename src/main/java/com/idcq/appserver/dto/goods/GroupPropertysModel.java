package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

public class GroupPropertysModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6735520098062308112L;

	private List<GroupPropertyModel> groupPropertys;

	public List<GroupPropertyModel> getGroupPropertys() {
		return groupPropertys;
	}

	public void setGroupPropertys(List<GroupPropertyModel> groupPropertys) {
		this.groupPropertys = groupPropertys;
	}
	
	

	
}
