package com.idcq.idianmgr.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.idianmgr.dao.shop.IShopClassesDao;
import com.idcq.idianmgr.dto.shop.ShopClassesDto;
/**
 * 商铺排班设置
 * @ClassName: ShopClassesServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 下午1:55:28 
 *
 */
@Service
public class ShopClassesServiceImp implements IShopClassesService{
	
	@Autowired
	private IShopClassesDao shopClassesDao;
	/**
	 * 获得商铺排班设置列表
	 * @Title: getShopClassesList 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopClassesDto> getShopClassesList(Long shopId)
			throws Exception {
		return shopClassesDao.getShopClassesList(shopId);
	}
	
	/**
	 * 批量插入排班设置
	 * @Title: batchSetClassSetting 
	 * @param @param shopClassesList  
	 * @throws
	 */
	public void batchSetClassSetting(List<ShopClassesDto> shopClassesList)throws Exception {
		shopClassesDao.batchSetClassSetting(shopClassesList);
		//修改技师状态
	}

	@Override
	public void deleteByShopId(long shopId) throws Exception{
		shopClassesDao.deleteByShopId(shopId);
	}
	
	
}
