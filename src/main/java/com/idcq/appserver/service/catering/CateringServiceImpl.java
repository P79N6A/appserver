package com.idcq.appserver.service.catering;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.catering.ICateringDao;
import com.idcq.appserver.dto.catering.CateringDto;
import com.idcq.appserver.dto.common.PageModel;
/**
 * 餐饮业service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午4:16:45
 */
@Service
public class CateringServiceImpl implements ICateringServcie{

	@Autowired
	public ICateringDao cateringDao;

	public PageModel getList(CateringDto catering, int page, int pageSize)
			throws Exception {
//		List<ShopCartDto> shopList = this.showCartDao.getListByMember(shop, page, pageSize);
//		List<CateringDto> caterList = this.cateringDao.getList(catering, page, pageSize);
		
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		List<CateringDto> shopList = new ArrayList<CateringDto>();
		CateringDto sh = new CateringDto();
		sh.setId((long) 1);
		shopList.add(sh);
		pm.setList(shopList);
		return pm;
	}
	
	
	
}
