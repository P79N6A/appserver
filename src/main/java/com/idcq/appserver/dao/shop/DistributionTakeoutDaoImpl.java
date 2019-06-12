package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.DistributionTakeoutSetting;

/**
 * 店铺外卖费用配置dao
 * 
 * @author Administrator
 * 
 * @date 2015年5月11日
 * @time 下午7:04:06
 */
@Repository
public class DistributionTakeoutDaoImpl extends BaseDao<DistributionTakeoutSetting> implements IDistributionTakeoutDao{

	@Override
	public DistributionTakeoutSetting getDistributionTakeoutSettingByShopId(Long shopId) throws Exception {
		return (DistributionTakeoutSetting) super.selectOne("getDistributionTakeoutSettingByShopId", shopId);
	}

	@Override
	public List<Map> getDeliveryTime(Map param) throws Exception {
		return (List) this.findList("getDeliveryTime", param);
	}
	
}
