package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.ShopTimeIntevalDto;

public interface IShopTimeIntevalDao {
	
	List<ShopTimeIntevalDto> getListSTI(Long shopId,Long bookRuleId, Integer pageNo, Integer pageSize);
	
	int getCountSTI(Long shopId);
	
	
}
