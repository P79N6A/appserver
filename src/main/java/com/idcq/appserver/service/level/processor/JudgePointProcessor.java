package com.idcq.appserver.service.level.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;

@Processor("pointRuleType:7")
public class JudgePointProcessor extends PointProcessor {

	@Autowired
	private IUserDao userDao;
	
	@Override
	protected void processPointByRule(CalculatePointMessageModel messageModel,
			List<PointRuleDto> ruleList, Object pointTarget) throws Exception {
		
		logger.info("评价类积分处理开始---messageModel："+messageModel);
		
		ShopDto shop = (ShopDto)pointTarget;
		
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		String pointSourceId = messageModel.getPointSourceId();
		Integer pointSourceType = messageModel.getPointSourceType();
		Integer subRuleType = messageModel.getSubRuleType();
		Integer ruleType = messageModel.getRuleType();
		
		if (subRuleType == null) {
			logger.info("积分规则类型："+ruleType+" 积分子规则类型:"+subRuleType+" 会员评价信息为空，停止计算会员消费评价积分");
			return;
		}
		
		if (pointSourceId == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id不能为空");
			return;
		}
		
		UserDto user = userDao.getUserById(Long.valueOf(pointSourceId));
		
		if (user == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id:"+pointSourceId+" 会员不存在，停止计算会员消费评价积分");
			return;
		}
		
		Integer afterPoint = 0;
		logger.info("积分前店铺信息----积分类型：评价类  shopId:{} shopName:{} shopPoint:{} pointSourceId:{}", 
				shop.getShopId(),shop.getShopName(),shop.getShopPoint(),pointSourceId);
		for (PointRuleDto rule : ruleList) {
				afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), rule.getPointValue());
				levelService.insertPointDetail(pointTargetId, pointTargetType, 
						rule.getPointValue(), afterPoint, 
						rule.getRuleName(), rule.getPointRuleId(), 
						messageModel.getPointSourceType(), messageModel.getPointSourceId(),null,null,null);
				logger.info("积分后店铺信息----积分类型：评价类  shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
						shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
		}
	}

}
