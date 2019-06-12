package com.idcq.appserver.service.discount;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;

public interface IShopTimeDiscountService {

	/**
	 * 获取商铺限时折扣
	 * @param params
	 * @return
	 */
	Map getShopTimedDiscount(Map<String, Object> params)throws Exception;

	/**
	 * 获取限时折扣对应商铺信息
	 * @param params
	 * @return
	 */
	Map getShopTimedDiscountGoods(Map<String, Object> params)throws Exception;

	/**
	 * 搜索商铺限时折扣
	 * @param model
	 * @param isDistance 是否需要返回距离
	 * @return
	 */
	Map searchShopTimeDiscount(PageModel model,boolean isDistance) throws Exception;

	/**
	 * 初始化限时折扣数据
	 * @param timedLists
	 * @return
	 */
	int insertTimedDiscountData(List<Map> timedLists);

}
