package com.idcq.appserver.service.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.goods.IShopResourceGroupDao;
import com.idcq.appserver.dto.common.PageModel;

@Service
public class ShopResourceGroupServiceImpl implements IShopResourceGroupService {

	@Autowired
	private IShopResourceGroupDao shopResourceGroupDao;
	
	
	public PageModel getPageSRG(Long shopId, Integer pageNo, Integer pageSize)
			throws Exception {
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		page.setTotalItem(shopResourceGroupDao.getCountSRG(shopId));
		page.setList(shopResourceGroupDao.getListSRG(shopId, pageNo, pageSize));
		
		return page;
	}

}
