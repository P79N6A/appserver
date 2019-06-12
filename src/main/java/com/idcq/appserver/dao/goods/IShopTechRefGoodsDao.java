package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.ShopTechRefGoodsDto;

public interface IShopTechRefGoodsDao {
	
	/**
	 * 根绝商品族id查找技师
	 * @Title: queryListByGoodsGroupId 
	 * @param @param groupId
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopTechRefGoodsDto>    返回类型 
	 * @throws
	 */
	List<ShopTechRefGoodsDto>queryListByGoodsGroupId(Long groupId)throws Exception;
}
