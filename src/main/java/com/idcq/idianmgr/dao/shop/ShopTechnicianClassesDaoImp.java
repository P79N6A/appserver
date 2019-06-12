package com.idcq.idianmgr.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.shop.ShopTechnicianClassesDto;
/**
 * 获取商铺排班设置
 * @ClassName: ShopTechnicianClassesDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午11:30:46 
 *
 */
@Repository
public class ShopTechnicianClassesDaoImp extends BaseDao<ShopTechnicianClassesDto> implements IShopTechnicianClassesDao{
	
	
	/**
	 * 获取排班设置
	 * @Title: getScheduleSetting 
	 * @param @param shopId
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param techId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopTechnicianClassesDto> getScheduleSetting(String shopId,
			String startDate, String endDate, String techId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId",shopId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("techId",techId);
		return findList(generateStatement("getScheduleSetting"),params);
	}
	
	/**
	 * 排班设置
	 * @Title: setScheduleSetting 
	 * @param @param shopClassesList  
	 * @throws
	 */
	public void setScheduleSetting(
			List<ShopTechnicianClassesDto> shopClassesList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("list", shopClassesList);
		insert(generateStatement("setScheduleSetting"), params);
	}

	@Override
	public void deleteByCondition(ShopTechnicianClassesDto shopClassesObj) {
		delete(generateStatement("deleteByCondition"), shopClassesObj);
	}

}
