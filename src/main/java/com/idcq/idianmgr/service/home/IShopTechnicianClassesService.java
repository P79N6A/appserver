package com.idcq.idianmgr.service.home;

import java.util.List;

import com.idcq.idianmgr.dto.shop.ShopTechnicianClassesDto;

public interface IShopTechnicianClassesService {
	
	/**
	 * 获取排班设置接口
	 * @Title: getScheduleSetting 
	 * @param @param shopId
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param techId
	 * @param @return
	 * @return List<ShopTechnicianClassesDto>    返回类型 
	 * @throws
	 */
	List<ShopTechnicianClassesDto>getScheduleSetting(String shopId,String startDate,String endDate,String techId)throws Exception;

	void setScheduleSetting(List<ShopTechnicianClassesDto> shopClassesList)throws Exception;
	
	/**
	 * 根据对象属性进行删除
	 * @Title: deleteByCondition 
	 * @param @param shopClassesObj
	 * @return void    返回类型 
	 * @throws
	 */
	void deleteByCondition(ShopTechnicianClassesDto shopClassesObj);
}
