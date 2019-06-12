package com.idcq.appserver.service.level.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;

@Processor("pointRuleType:6")
public class RegisterPointProcessor extends PointProcessor {

	@Autowired
	private IUserDao userDao;
	
	@Override
	protected void processPointByRule(CalculatePointMessageModel messageModel,
										List<PointRuleDto> ruleList, Object pointTarget)
										throws Exception {

		logger.info("注册类积分处理开始---messageModel："+messageModel);
		
		ShopDto shop = (ShopDto)pointTarget;
		
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		String pointSourceId = messageModel.getPointSourceId();
		Integer pointSourceType = messageModel.getPointSourceType();
		Integer subRuleType = messageModel.getSubRuleType();
		Integer ruleType = messageModel.getRuleType();
		
		if (subRuleType == null) {
			logger.info("积分规则类型："+ruleType+" 积分子规则类型:"+subRuleType+" 注册消息缺少注册信息，停止计算注册积分");
			return;
		}
		
		if (pointSourceId == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id不能为空");
			return;
		}
		
		UserDto user = userDao.getUserById(Long.valueOf(pointSourceId));
		
		if (user == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id:"+pointSourceId+" 注册信息不存在，停止计算注册积分");
			return;
		}
		
		Integer afterPoint = shop.getShopPoint() == null ? 0 : shop.getShopPoint();
		logger.info("积分前店铺信息----积分类型：{} shopId:{} shopName:{} shopPoint:{} pointSourceId:{}",
				"注册类积分", shop.getShopId(),shop.getShopName(),shop.getShopPoint(),pointSourceId);
		for (PointRuleDto rule : ruleList) {
			
				afterPoint = levelService.updateShopByPoint(shop.getShopId(), shop.getLevelId(), rule.getPointValue());
				
				levelService.insertPointDetail(pointTargetId, pointTargetType, 
						rule.getPointValue(), afterPoint, 
						rule.getRuleName(), rule.getPointRuleId(), 
						pointSourceType, pointSourceId,null,null,null);
				logger.info("积分后店铺信息----积分类型：注册积分  shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}", 
						shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
		}
	}

}
