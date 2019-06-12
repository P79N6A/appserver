package com.idcq.idianmgr.dao.shop;

import java.util.List;

import com.idcq.idianmgr.dto.shop.ShopClassesDto;
/**
 * 商铺班次表
 * @ClassName: ShopClassesDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午11:43:17 
 *
 */
/**
 * 商铺班次表
 * @ClassName: ShopClassesDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午11:43:17 
 *
 */
public interface IShopClassesDao {
	
	/**
	 * 批量设置排班信息
	 * @Title: batchSetClassSetting 
	 * @param @param shopClassesDtos
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void batchSetClassSetting(List<ShopClassesDto>shopClassesDtos)throws Exception;
	
	/**
	 * 获得商铺排班列表
	 * @Title: getShopClassesList 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopClassesDto>    返回类型 
	 * @throws
	 */
	List<ShopClassesDto>getShopClassesList(Long shopId)throws Exception;

	void deleteByShopId(long shopId)throws Exception;
}
