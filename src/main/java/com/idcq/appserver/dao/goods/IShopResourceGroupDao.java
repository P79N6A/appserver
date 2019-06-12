package com.idcq.appserver.dao.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.goods.ShopResourceGroupDto;

public interface IShopResourceGroupDao {
	
	List<ShopResourceGroupDto> getListSRG(Long shopId, Integer pageNo, Integer pageSize);
	
	int getCountSRG(Long shopId);
	
	public ShopResourceGroupDto getShopResGroupById(Long groupId);
	
}
