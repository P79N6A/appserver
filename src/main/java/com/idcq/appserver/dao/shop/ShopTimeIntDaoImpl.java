package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopTimeIntDto;

/**
 * 商铺可用时间段dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午8:47:44
 */
@Repository
public class ShopTimeIntDaoImpl extends BaseDao<ShopTimeIntDto> implements IShopTimeIntDao{

	public Long saveTimeInteval(ShopTimeIntDto time) throws Exception {
		super.insert(generateStatement("saveTimeInteval"),time) ;
		return time.getIntevalId();
	}

	public Long getShopTimeIdById(Long shopId,Long intevalId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("intevalId",intevalId);
		Long id2 = (Long)super.selectOne(generateStatement("getShopTimeIdById"), map);
		return id2 == null ? 0 : id2;
	}

	public Long getBookRuleIdByTimeId(Long intevalId) throws Exception {
		Long ruleId = (Long)super.selectOne(generateStatement("getBookRuleIdByTimeId"), intevalId);
		return ruleId == null ? 0L : ruleId;
	}

	public ShopTimeIntDto getShopTimeById(Long intevalId) throws Exception {
		return (ShopTimeIntDto)super.selectOne(generateStatement("getShopTimeById"), intevalId);
	}
	
	
}
