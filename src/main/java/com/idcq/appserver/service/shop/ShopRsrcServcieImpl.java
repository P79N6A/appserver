package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.shop.IShopRsrcDao;
import com.idcq.appserver.dto.shop.ShopRsrcPramDto;

/**
 * 商品资源service
 * @author ChenYongxin
 *
 */
@Service
public class ShopRsrcServcieImpl implements IShopRsrcServcie {
	
	@Autowired
	private IShopRsrcDao shopRsrcDao;

	@Override
	public List<Map<String, Object>> getShopCategoryResource(
			Map<String, Object> map) throws Exception {
		return shopRsrcDao.getShopCategoryResource(map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopRsrcServcie#operateResource(com.idcq.appserver.dto.shop.ShopRsrcDto)
	 */
	@Override
	public void operateResource(ShopRsrcPramDto shopRsrcPramDto) throws Exception {
		Integer operateType  = shopRsrcPramDto.getOperateType();
		//增加
		if(0==operateType){
			shopRsrcDao.insertShopCategoryResource(shopRsrcPramDto);
		}
		//修改		
		if(1==operateType){
			shopRsrcDao.updateShopCategoryResource(shopRsrcPramDto);
		}
		//删除
		if(2==operateType){
			shopRsrcDao.deleteShopCategoryResource(shopRsrcPramDto);
		}
		
	}

	public int queryResourceExists(Long resourceId) throws Exception {
		return this.shopRsrcDao.queryResourceExists(resourceId);
	}

	public String getResourceName(Long resourceId) throws Exception {
		return this.shopRsrcDao.getShopResourceName(resourceId);
	}
	
	
	
}
