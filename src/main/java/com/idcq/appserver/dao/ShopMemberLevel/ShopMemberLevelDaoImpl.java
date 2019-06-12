package com.idcq.appserver.dao.ShopMemberLevel;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopMemberLevelDto;

@Repository
public class ShopMemberLevelDaoImpl extends BaseDao<ShopMemberLevelDto> implements IShopMemberLevelDao {

	@Override
	public int updateShopMemberLevel(ShopMemberLevelDto shopMemberLevelDto) {
		return super.update("updateShopMemberLevel",shopMemberLevelDto);
	}

	@Override
	public Integer addShopMemberLevelDto(ShopMemberLevelDto shopMemberLevelDto) {
		super.insert("addShopMemberLevelDto", shopMemberLevelDto);
		 return shopMemberLevelDto.getShopMemberLevelId();
	}

	@Override
	public List<ShopMemberLevelDto> getShopMemberLevelList(Map<String, Object> paramMap) {
		return super.findList("getShopMemberLevelList", paramMap);
	}
	

	@Override
	public int deleteShopMemberLevel(Map<String, Object> map){
		// TODO Auto-generated method stub
		return super.update(generateStatement("deleteShopMemberLevel"),map);
	}

}
