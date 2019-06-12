package com.idcq.idianmgr.service.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopTechnicianDao;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.idianmgr.dao.shop.IShopTechnicianClassesDao;
import com.idcq.idianmgr.dto.shop.ShopTechnicianClassesDto;
/**
 * 店铺技师排班
 * @ClassName: ShopTechnicianClassesServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午11:23:48 
 *
 */
@Service
public class ShopTechnicianClassesServiceImp implements IShopTechnicianClassesService{
	
	@Autowired
	private IShopTechnicianClassesDao shopTechnicianClassesDao;
	@Autowired
	private IShopTechnicianService shopTechnicianService;
	@Autowired
	private IShopTechnicianDao shopTechnicianDao;
	
	public List<ShopTechnicianClassesDto> getScheduleSetting(String shopId,
			String startDate, String endDate, String techId) throws Exception{
		return shopTechnicianClassesDao.getScheduleSetting(shopId,
				startDate,endDate,  techId);
	}

	@Override
	public void setScheduleSetting(
			List<ShopTechnicianClassesDto> shopClassesList) throws Exception {
		shopTechnicianClassesDao.setScheduleSetting(shopClassesList);
		//更新技师状态 TODO 修改问题单id=3753 by陈永鑫
		updateTechnicianWorkStatus(shopClassesList);
	}
	/**
	 * 更新技师状态
	 * @throws Exception 
	 */
	public void updateTechnicianWorkStatus(List<ShopTechnicianClassesDto> shopClassesList) throws Exception {
		if(CollectionUtils.isNotEmpty(shopClassesList)){
			for (ShopTechnicianClassesDto stc : shopClassesList) {
				Date classesDate = stc.getClassesDate();
				Date nowDate = DateUtils.getCurrentDate(DateUtils.DATE_FORMAT);
				Long technicianId = Long.parseLong(stc.getTechId());
				List<Long> techIds = new ArrayList<Long>();
				techIds.add(technicianId);
				//只更新当天排版
				if(0==classesDate.compareTo(nowDate) ){
					Long classesType = stc.getClassesType();
					if(CommonConst.CLASSES_TYPE_XX==classesType){
						shopTechnicianService.updateTechnicianWorkStatus(techIds, CommonConst.TECH_STATUS_XXZ);
					}
					//技师控制层已经校验存在
					Integer workStatusDB = shopTechnicianDao.getTechWorkStatusById(technicianId);
					//技师状态是休息中
					if(CommonConst.TECH_STATUS_XXZ==workStatusDB&&(CommonConst.CLASSES_TYPE_ZB==classesType||CommonConst.CLASSES_TYPE_WB==classesType||CommonConst.CLASSES_TYPE_TB==classesType)){
						shopTechnicianService.updateTechnicianWorkStatus(techIds, CommonConst.TECH_STATUS_KXZ);
					}
				}

				
			}
		}
	}

	@Override
	public void deleteByCondition(ShopTechnicianClassesDto shopClassesObj) {
		shopTechnicianClassesDao.deleteByCondition(shopClassesObj);
	}

}
