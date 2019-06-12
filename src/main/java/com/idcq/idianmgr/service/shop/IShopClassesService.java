package com.idcq.idianmgr.service.shop;

import java.util.List;

import com.idcq.idianmgr.dto.shop.ShopClassesDto;

public interface IShopClassesService {

	List<ShopClassesDto>getShopClassesList(Long shopId)throws Exception;

	void batchSetClassSetting(List<ShopClassesDto> shopClassesList)throws Exception;

	void deleteByShopId(long parseLong)throws Exception;
}
