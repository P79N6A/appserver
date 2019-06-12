package com.idcq.idianmgr.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.shop.ShopClassesDto;
/**
 * 商铺班次表
 * @ClassName: ShopClassesDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午11:43:17 
 *
 */
@Repository
public class ShopClassesDaoImp extends BaseDao<ShopClassesDto> implements IShopClassesDao{
	
	/**
	 * 批量排班
	 * @Title: batchSetClassSetting 
	 * @param @param shopClassesDtos
	 * @param @throws Exception  
	 * @throws
	 */
	public void batchSetClassSetting(List<ShopClassesDto> shopClassesDtos)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("list", shopClassesDtos);
		insert(generateStatement("batchSetClassSetting"), params);
	}
	
	/**
	 * 获得商铺排班列表
	 * @Title: getShopClassesList 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopClassesDto> getShopClassesList(Long shopId)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId", shopId);
		return super.findList(generateStatement("getShopClassesList"),params);
	}
	
	/**
	 * 根据商铺id删除商铺的排班设置
	 * @Title: deleteByShopId 
	 * @param @param shopId
	 * @param @throws Exception  
	 * @throws
	 */
	public void deleteByShopId(long shopId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId", shopId);
		super.delete(generateStatement("deleteByShopId"), params);
	}
	
}
