package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.ShopGoodsPropertyDto;

public interface IShopGoodsPropertyDao {
	public List<ShopGoodsPropertyDto> getShopGoodsProperty(ShopGoodsPropertyDto shopGoodsProperty) throws Exception;
	public int addShopGoodsProperty(ShopGoodsPropertyDto shopGoodsProperty) throws Exception;
}
