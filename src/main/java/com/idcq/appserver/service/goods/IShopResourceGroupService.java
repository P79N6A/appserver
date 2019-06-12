package com.idcq.appserver.service.goods;

import com.idcq.appserver.dto.common.PageModel;

public interface IShopResourceGroupService {

	/**
	 * 获取商铺资源分组
	 */	
	PageModel getPageSRG(Long shopId, Integer pageNo, Integer pageSize) throws Exception;
	
	
}
