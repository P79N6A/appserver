package com.idcq.appserver.service.level.processor;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.shop.ShopDto;

@Processor("pointRuleType:3")
public class PublishGoodPointProcessor extends PointProcessor{
	
	@Autowired
	private IGoodsDao goodsDao;
	
	@Override
	protected void processPointByRule(CalculatePointMessageModel messageModel,
										List<PointRuleDto> ruleList,Object pointTarget) 
										throws Exception {
		
		logger.info("发布商品类积分处理开始---messageModel："+messageModel);
		
		ShopDto shop = (ShopDto)pointTarget;
		
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		String pointSourceId = messageModel.getPointSourceId();
		Integer pointSourceType = messageModel.getPointSourceType();
		
		if (pointSourceId == null) {
			logger.info("积分来源类型："+messageModel.getPointSourceType()+" 积分来源Id不能为空");
			return;
		}
		
		GoodsDto good = goodsDao.getGoodsById(Long.valueOf(pointSourceId));
		
		if (good == null) {
			logger.info("积分来源类型："+pointSourceType+" 积分来源Id:"+pointSourceId+" 发布商品信息不存在，停止计算发布商品类积分");
			return;
		}
		
		String goodName = good.getGoodsName() == null ? "" : good.getGoodsName();
		Integer afterPoint = 0 ;
		logger.info("积分前店铺信息----积分类型：{} shopId:{} shopName:{} shopPoint:{} pointSourceId:{}",
				"发布商品类", shop.getShopId(),shop.getShopName(),shop.getShopPoint(),pointSourceId);
		for (PointRuleDto rule : ruleList) {
			PointDetailDto pointDetailDto = new PointDetailDto();
			if (rule.getSubRuleType() == 2 || rule.getSubRuleType() == 3) {
				
				if (rule.getSubRuleType() == 2) {
					if (good.getGoodsStatus() == null || !good.getGoodsStatus().equals(1)) {
						continue;
					}
				}
				
				if (rule.getSubRuleType() == 3) {
					if (good.getGoodsStatus() != 2) {
						continue;
					}
				}
				pointDetailDto.setPointSourceType(3);
				pointDetailDto.setPointSourceId(good.getGoodsId().toString());
			}
			
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
					rule.getRuleName()+"["+goodName+"]", rule.getPointRuleId(), 
					messageModel.getPointSourceType(), messageModel.getPointSourceId(),null,null,null);
			logger.info("积分后店铺信息----积分类型：发布商品类 shopId:{} shopName:{} shopPoint:{} 增加积分：{} pointSourceId:{}",
					shop.getShopId(),shop.getShopName(),afterPoint,rule.getPointValue(),pointSourceId);
		}
	}
}
