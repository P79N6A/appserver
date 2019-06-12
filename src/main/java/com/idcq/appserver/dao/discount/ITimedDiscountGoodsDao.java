package com.idcq.appserver.dao.discount;

import com.idcq.appserver.dto.discount.TimedDiscountGoodsDto;

public interface ITimedDiscountGoodsDao {
	
	/**
	 * 获取指定商品的限时折扣
	 * 
	 * @param shopId
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	TimedDiscountGoodsDto getGoodsDicount(Integer shopId,Integer goodsId) throws Exception;
	
	/**
	 * 获取指定商铺的限时折扣
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	TimedDiscountGoodsDto getShopDicount(Integer shopId) throws Exception;
	
	
}
