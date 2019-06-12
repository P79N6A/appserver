package com.idcq.appserver.dao.ShopMemberLevel;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopMemberLevelDto;



public interface IShopMemberLevelDao {

	int updateShopMemberLevel(ShopMemberLevelDto shopMemberLevelDto);

	Integer addShopMemberLevelDto(ShopMemberLevelDto shopMemberLevelDto);

	List<ShopMemberLevelDto> getShopMemberLevelList(Map<String, Object> paramMap);
	
	int deleteShopMemberLevel(Map<String, Object> map);


}
