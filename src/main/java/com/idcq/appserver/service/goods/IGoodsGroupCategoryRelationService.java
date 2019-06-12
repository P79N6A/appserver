package com.idcq.appserver.service.goods;

import java.util.List;

import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;

public interface IGoodsGroupCategoryRelationService {
	
	List<GoodsGroupCategoryRelationDto>getCategoryListByGroupId(Long goodsGroupId)throws Exception;
}
