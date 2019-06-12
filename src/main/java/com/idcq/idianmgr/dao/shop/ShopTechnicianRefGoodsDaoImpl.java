package com.idcq.idianmgr.dao.shop;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.shop.ShopTechnicianRefGoodsDto;

@Repository
public class ShopTechnicianRefGoodsDaoImpl extends BaseDao<ShopTechnicianRefGoodsDto> implements 
	IShopTechnicianRefGoodsDao{

	@Override
	public int addShopTechnicianRefGoodsBatch(
			List<ShopTechnicianRefGoodsDto> list) throws Exception {
		return super.insert("addShopTechnicianRefGoodsBatch",list);
	}

	@Override
	public int delShopTechnicianRefGoodsByGgId(Long groupGroupId)
			throws Exception {
		return super.delete("delShopTechnicianRefGoodsByGgId", groupGroupId);
	}
	
}
