package com.idcq.appserver.dao.shop;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.TakeoutSetDto;

/**
 * 店铺外卖费用配置dao
 * 
 * @author Administrator
 * 
 * @date 2015年5月11日
 * @time 下午7:04:06
 */
@Repository
public class TakeoutSetDaoImpl extends BaseDao<TakeoutSetDto> implements ITakeoutSetDao{

	public TakeoutSetDto getTakeoutSetByShopId(Long shopId) throws Exception {
		return (TakeoutSetDto)super.selectOne(generateStatement("getTakeoutSetByShopId"), shopId);
	}

	public Map queryTakeoutSetInit(Long shopId) throws Exception {
		return (Map) super.selectOne(generateStatement("queryTakeoutSetInit"), shopId);
	}
	
}
