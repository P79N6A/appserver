package com.idcq.appserver.service.level.processor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDto;

@Processor("pointRuleType:5")
public class OrderPointProcessor extends PointProcessor {

	@Autowired
	private IOrderDao orderDao;
	
	@Override
	protected void processPointByRule(CalculatePointMessageModel messageModel,
			List<PointRuleDto> ruleList, Object pointTarget) throws Exception {
		
		logger.info("制单类积分处理开始---messageModel："+messageModel);
		
		ShopDto shop = (ShopDto)pointTarget;
		
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		String pointSourceId = messageModel.getPointSourceId();
		Integer pointSourceType = messageModel.getPointSourceType();
		
		if (pointSourceId == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id不能为空");
			return;
		}
		
		OrderDto order = orderDao.getOrderById(pointSourceId);
		
		if (order == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id:"+pointSourceId+" 订单不存在，停止计算制单积分");
			return;
		}
		
		Integer afterPoint = shop.getShopPoint() == null ? 0 : shop.getShopPoint();
		logger.info("积分前店铺信息----积分类型：制单类积分 shopId:{} shopName:{} shopPoint:{} pointSourceId：{}",
												shop.getShopId(),shop.getShopName(),shop.getShopPoint(),pointSourceId);
		for (PointRuleDto rule : ruleList) {
			
			if (rule.getSubRuleType() == 1) {
				
				PointDetailDto searchDto = new PointDetailDto();
				searchDto.setBizType(1);
				searchDto.setBizId(shop.getShopId().intValue());
				searchDto.setPointRuleId(rule.getPointRuleId());
				
				Date now = new Date();
				Calendar cal = Calendar.getInstance();  
				cal.setTime(now);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				Date startTime = cal.getTime();
				cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
				Date endTime = cal.getTime();
				
				searchDto.setStartTime(startTime);
				searchDto.setEndTime(endTime);
				
				int getPointValuePerDay = levelService.getPointDetailValueSum(searchDto);
				if (getPointValuePerDay >= 100) {
					logger.info("当日制单金额累计积分达到限制");
					continue;
				}
				
				afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), rule.getPointValue());
				levelService.insertPointDetail(pointTargetId, pointTargetType, 
						rule.getPointValue(), afterPoint, 
						rule.getRuleName()+"["+order.getOrderTitle()+"]", rule.getPointRuleId(), 
						messageModel.getPointSourceType(), messageModel.getPointSourceId(),null,null,null);
				logger.info("积分后店铺信息----积分类型：制单类积分 shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
						shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
				
			} else if (rule.getSubRuleType() == 2) {
				Integer pointTimes = order.getSettlePrice().intValue() / 100;
				Integer pointValue = rule.getPointValue() * pointTimes;
				
				if (pointValue > 0) {
					afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), pointValue);
					levelService.insertPointDetail(pointTargetId, pointTargetType, 
							pointValue, afterPoint, 
							rule.getRuleName()+"["+order.getOrderTitle()+"]", rule.getPointRuleId(), 
							messageModel.getPointSourceType(), messageModel.getPointSourceId(),null,null,null);
					logger.info("积分后店铺信息----积分类型：制单类积分 shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
							shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
				}
				
			} else if (rule.getSubRuleType() == 3) {
				PointDetailDto pointDetailDto = new PointDetailDto();
				pointDetailDto.setPointRuleId(rule.getPointRuleId());
				pointDetailDto.setBizId(pointTargetId);
				pointDetailDto.setBizType(pointTargetType);
				int count = levelDao.getPointDetailCount(pointDetailDto);
				
				if (count > 0) {
					continue;
				}
				
				afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), rule.getPointValue());
				levelService.insertPointDetail(pointTargetId, pointTargetType, 
						rule.getPointValue(), afterPoint, 
						rule.getRuleName()+"["+order.getOrderTitle()+"]", rule.getPointRuleId(), 
						messageModel.getPointSourceType(), messageModel.getPointSourceId(),null,null,null);
				logger.info("积分后店铺信息----积分类型：制单类积分 shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
						shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
			}
			
		}
		
	}

}
