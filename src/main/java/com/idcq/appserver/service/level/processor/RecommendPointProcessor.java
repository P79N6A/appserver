package com.idcq.appserver.service.level.processor;

import java.util.List;

import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.shop.ShopDto;

@Processor("pointRuleType:4")
public class RecommendPointProcessor extends PointProcessor {

	@Override
	protected void processPointByRule(CalculatePointMessageModel messageModel,
			List<PointRuleDto> ruleList, Object pointTarget) throws Exception {
		
		logger.info("推荐类积分处理开始---messageModel："+messageModel);
		
		ShopDto shop = (ShopDto)pointTarget;
		
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		String pointSourceId = messageModel.getPointSourceId();
		Integer pointSourceType = messageModel.getPointSourceType();
		Integer subRuleType = messageModel.getSubRuleType();
		Integer ruleType = messageModel.getRuleType();
		
		if (subRuleType == null) {
			logger.info("积分规则类型："+ruleType+"缺少推荐积分子规则类型  停止计算推荐类积分");
			return;
		}
		
		Object pointSource = null;
		
		if (pointSourceType == 1) {
			pointSource = shopDao.getShopById(Long.valueOf(pointSourceId));
		} else if (pointSourceType == 2) {
			pointSource = userDao.getUserById(Long.valueOf(pointSourceId));
		}
		
		if (pointSource == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id:"+pointSourceId+" 推荐信息不存在，停止计算推荐类积分");
			return;
		}
		
		PointRuleDto rule = ruleList.get(0);
		Integer afterPoint =  0 ;
		logger.info("积分前店铺信息----积分类型：{} shopId:{} shopName:{} shopPoint:{} pointSourceId:{}",
				messageModel.getRuleType(), shop.getShopId(),shop.getShopName(),shop.getShopPoint(),pointSourceId);
		if (subRuleType == 2) {
			//推荐会员
			afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), rule.getPointValue());
			levelService.insertPointDetail(pointTargetId, pointTargetType, 
					rule.getPointValue(), afterPoint, 
					rule.getRuleName(), rule.getPointRuleId(), 
					pointSourceType, pointSourceId,null,null,null);
			
			logger.info("积分后店铺信息----积分类型：{} shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
					"推荐会员", shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
			//是否首次推荐会员
			PointRuleDto searchRuleDto = new PointRuleDto();
			searchRuleDto.setRuleType(4);
			searchRuleDto.setSubRuleType(3);
			List<PointRuleDto> searchRuleList = levelDao.getPointRuleList(searchRuleDto);
			
			if (searchRuleList == null || searchRuleList.size() == 0) {
				logger.info("首次推荐会员积分规则不存在");
				return;
			}
			
			PointRuleDto searchRule = searchRuleList.get(0);
			PointDetailDto pointDetailDto = new PointDetailDto();
			pointDetailDto.setPointRuleId(searchRule.getPointRuleId());
			pointDetailDto.setBizId(pointTargetId);
			pointDetailDto.setBizType(pointTargetType);
			int count = levelDao.getPointDetailCount(pointDetailDto);
			
			if (count > 0) {
				return;
			}
			
			afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(),searchRule.getPointValue());
			levelService.insertPointDetail(pointTargetId, pointTargetType, 
					searchRule.getPointValue(), afterPoint, 
					searchRule.getRuleName(), searchRule.getPointRuleId(), 
					pointSourceType, pointSourceId,null,null,null);
			
			logger.info("积分后店铺信息----积分类型：{} shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
					"推荐会员", shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
		}

	}

}
