package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.ShopResourceGroupDto;

@Repository
public class ShopResourceGroupDaoImpl extends BaseDao<ShopResourceGroupDto>
		implements IShopResourceGroupDao {

	public List<ShopResourceGroupDto> getListSRG(Long shopId, Integer pageNo,
			Integer pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopId);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		
		return super.findList(generateStatement("getListSRG"),map);
	}

	public int getCountSRG(Long shopId) {
		
		return (Integer)super.selectOne(generateStatement("getCountSRG"), shopId);
	}

	@Override
	public ShopResourceGroupDto getShopResGroupById(Long groupId) {
		return (ShopResourceGroupDto) super.selectOne("getShopResGroupById", groupId);
	}
	
}
