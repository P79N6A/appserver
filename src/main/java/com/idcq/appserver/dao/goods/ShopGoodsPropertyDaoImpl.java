package com.idcq.appserver.dao.goods;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.ShopGoodsPropertyDto;
@Repository
public class ShopGoodsPropertyDaoImpl extends BaseDao<ShopGoodsPropertyDto> implements IShopGoodsPropertyDao {

	@Override
	public List<ShopGoodsPropertyDto> getShopGoodsProperty(ShopGoodsPropertyDto shopGoodsProperty) throws Exception {
		return super.findList(generateStatement("getShopGoodsProperty"), shopGoodsProperty);
	}
	
	@Override
	public int addShopGoodsProperty(ShopGoodsPropertyDto shopGoodsProperty) throws Exception {
		return super.insert(generateStatement("addShopGoodsProperty"), shopGoodsProperty);
	}
}
