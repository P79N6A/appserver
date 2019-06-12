package com.idcq.idianmgr.dao.shop;

import java.util.List;

import com.idcq.idianmgr.dto.shop.ShopTechnicianClassesDto;

public interface IShopTechnicianClassesDao {

	List<ShopTechnicianClassesDto> getScheduleSetting(String shopId,
			String startDate, String endDate, String techId) throws Exception;

	void setScheduleSetting(List<ShopTechnicianClassesDto> shopClassesList);

	void deleteByCondition(ShopTechnicianClassesDto shopClassesObj);

}
