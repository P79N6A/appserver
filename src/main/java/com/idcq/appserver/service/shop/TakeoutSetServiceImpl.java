package com.idcq.appserver.service.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.shop.ITakeoutSetDao;
import com.idcq.appserver.dto.shop.TakeoutSetDto;

/**
 * 店铺外卖费用配置service
 * 
 * @author Administrator
 * 
 * @date 2015年5月11日
 * @time 下午7:04:06
 */
@Service
public class TakeoutSetServiceImpl implements ITakeoutSetService{
	
	@Autowired
	public ITakeoutSetDao takeoutSetDao;
	
	public Map<String,Object> getTakeoutSetByShopId(Long shopId) throws Exception {
		TakeoutSetDto pTake = this.takeoutSetDao.getTakeoutSetByShopId(shopId);
		if(pTake != null){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("deliveryPrice", pTake.getDeliveryPrice());
			map.put("reduction", pTake.getReduction());
			map.put("paymentType", pTake.getPaymentType());
			BigDecimal bookMoney = pTake.getBookMoney();
			Integer totalMoneyFlag = pTake.getTotalMoneyFlag();
			String remark = pTake.getRemark();
			if(bookMoney != null){
				map.put("bookMoney", bookMoney);
			}
			if(totalMoneyFlag != null){
				map.put("totalMoneyFlag", totalMoneyFlag);
			}
			if(!StringUtils.isBlank(remark)){
				map.put("remark", remark);
			}
			return map;
		}else{
			return null;
		}
		
	}

	
}
