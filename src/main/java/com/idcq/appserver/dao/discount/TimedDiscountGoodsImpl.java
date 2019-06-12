package com.idcq.appserver.dao.discount;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.discount.TimedDiscountGoodsDto;

/**
 * 商品的限时折扣dao
 * 
 * @author Administrator
 * 
 * @date 2015年4月8日
 * @time 下午3:53:54
 */
@Repository
public class TimedDiscountGoodsImpl extends BaseDao<TimedDiscountGoodsDto> implements ITimedDiscountGoodsDao{

	public TimedDiscountGoodsDto getGoodsDicount(Integer shopId, Integer goodsId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("goodsId", goodsId);
		return (TimedDiscountGoodsDto)super.selectOne(generateStatement("getGoodsDicount"), map);
	}

	public TimedDiscountGoodsDto getShopDicount(Integer shopId)
			throws Exception {
		return (TimedDiscountGoodsDto)super.selectOne(generateStatement("getShopDicount"), shopId);
	}

	
	
}
