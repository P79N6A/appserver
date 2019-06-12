package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.TopGoodsDto;

/**
 * 热卖商品dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 下午4:07:28
 */
@Repository
public class TopGoodsDaoImpl extends BaseDao<TopGoodsDto>implements ITopGoodsDao{


	public List<TopGoodsDto> getTopGoodsList(TopGoodsDto topGoods, Integer page,
			Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topGoods", topGoods);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return super.findList(generateStatement("getTopGoodsList"), map);
	}

	public int getTopGoodsListCount(TopGoodsDto topGoods) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topGoods", topGoods);
		return (Integer)super.selectOne(generateStatement("getTopGoodsListCount"), map);
	}
	
}
