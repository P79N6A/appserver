package com.idcq.appserver.dao.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.ShopGoodsDto;

public interface IShopGoodsDao {
	
	List<ShopGoodsDto> getListShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy, Integer pageNo, Integer pageSize,String goodsServerMode);
	
	int getCountShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy,String goodsServerMode);
	
	ShopGoodsDto getDtoShopGoods(Long goodsId);

	List<ShopGoodsDto> getListShopGoodsByCondition(ShopGoodsDto shopGoodsDto,
			PageModel pageModel);

	int getCountShopGoods(ShopGoodsDto shopGoodsDto, PageModel pageModel);

	Map<String, Object> getShopGoodsStatisticsByCondition(
			ShopGoodsDto shopGoodsDto, PageModel pageModel);
	
	public String getDtoProValueName(String proValuesId);
	
}
