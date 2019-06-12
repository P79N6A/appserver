package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;

public class SyncGoodsInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2124939023871457607L;
	@Check(recurse=true)
	List<SyncGoodsDto> syncGoodsList;
	public List<SyncGoodsDto> getSyncGoodsList() {
		return syncGoodsList;
	}
	public void setSyncGoodsList(List<SyncGoodsDto> syncGoodsList) {
		this.syncGoodsList = syncGoodsList;
	}
}
