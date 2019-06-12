package com.idcq.idianmgr.service.goodsGroup;

import java.util.List;

import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;

public interface IGoodsGroupPropertyService {
	
	List<GoodsGroupPropertyDto>getGoodsGroupPropertyListByGroupId(Long groupId)throws Exception;
}
