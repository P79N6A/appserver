package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.ShopTimeIntevalDto;

@Repository
public class ShopTimeIntevalDaoImpl extends BaseDao<ShopTimeIntevalDto>
		implements IShopTimeIntevalDao {

	
	
	public List<ShopTimeIntevalDto> getListSTI(Long shopId,Long bookRuleId, Integer pageNo,
			Integer pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopId);
		map.put("bookRuleId",bookRuleId);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return super.findList(generateStatement("getListSTI"),map);
	}

	public int getCountSTI(Long shopId) {
		
		return (Integer)super.selectOne(generateStatement("getCountSTI"), shopId);
		
	}
	
}
