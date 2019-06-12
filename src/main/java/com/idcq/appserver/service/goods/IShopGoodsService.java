package com.idcq.appserver.service.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.ShopGoodsDto;

public interface IShopGoodsService {
	
	PageModel getPageShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy, Integer pageNo,Integer pageSize,String goodsServerMode) throws Exception;
	
	ShopGoodsDto getDtoShopGoods(Long goodsId) throws Exception ;
	
	PageModel getShopGoodsByCondition(ShopGoodsDto shopGoodsDto,PageModel pageModel)throws Exception;
	
	Integer getGoodsGroupCountByShopId(Long shopId)throws Exception;

	Map<String,Object> getAttachment(Long goodsId,
			Integer bizTypeIsGoods, Integer picTypeIsCyclePlay) throws Exception;

	Map<String, Object> getShopGoodsStatisticsByCondition(
			ShopGoodsDto shopGoodsDto, PageModel pageModel);
	
	List<Long> getGoodsGroupIdByShopId(Long shopId) throws Exception;
}
