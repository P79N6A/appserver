package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.shop.IDistributionTakeoutDao;
import com.idcq.appserver.dto.shop.DistributionTakeoutSetting;

/**
 * 店铺外卖费用配置service
 * 
 * @author Administrator
 * 
 * @date 2015年5月11日
 * @time 下午7:04:06
 */
@Service
public class DistributionTakeoutServiceImpl implements IDistributionTakeoutService{
	
	@Autowired
	public IDistributionTakeoutDao distributionTakeoutDao;

	@Override
	public DistributionTakeoutSetting getDistributionTakeoutSettingByShopId(
			Long shopId) throws Exception {
		return distributionTakeoutDao.getDistributionTakeoutSettingByShopId(shopId);
	}

	@Override
	public List<Map> getDeliveryTime(Map param) throws Exception {
		return distributionTakeoutDao.getDeliveryTime(param);
	}
	

	
}
