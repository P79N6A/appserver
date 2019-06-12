package com.idcq.appserver.service.level.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.level.ILevelDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.service.level.ILevelService;

public abstract class PointProcessor implements IPointProcessor {

	protected final static Logger logger = LoggerFactory.getLogger(PointProcessor.class);
	
	@Autowired
	protected ILevelDao levelDao;
	@Autowired
	protected IShopDao shopDao;
	@Autowired
	protected IUserDao userDao;
	@Autowired
	protected ILevelService levelService;
	
	protected abstract void processPointByRule(CalculatePointMessageModel messageModel,
												List<PointRuleDto> ruleList,Object pointTarget) 
												throws Exception;
	@Override
	public void processPoint(final CalculatePointMessageModel messageModel) throws Exception {
		Integer ruleType = messageModel.getRuleType();
		Integer pointTargetType = messageModel.getPointTargetType();
		Integer pointTargetId = messageModel.getPointTargetId();
		
		if (ruleType == null || pointTargetType == null || pointTargetId == null) {
			logger.info("messageModel缺少关键信息   ruleType:"+ruleType+
											"pointTargetType:"+pointTargetType+
											"pointTargetId"+pointTargetId);
			return;
		}
		
		Object pointTartget = null;
		if (pointTargetType == 1) {
			pointTartget = shopDao.getShopById(pointTargetId.longValue());
		} else if (pointTargetType == 2) {
			pointTartget = userDao.getUserById(pointTargetId.longValue());
		}
		
		
		if (pointTartget == null) {
			logger.info("pointTargetType:"+pointTargetType+" pointTargetId"+pointTargetId+" 积分目标信息不存在");
			return;
		}
		
		PointRuleDto pointRuleDto = new PointRuleDto();
		pointRuleDto.setRuleType(ruleType);
		pointRuleDto.setSubRuleType(messageModel.getSubRuleType());
		pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		List<PointRuleDto> ruleList = levelDao.getPointRuleList(pointRuleDto);
		
		if (ruleList == null ||ruleList.size() == 0) {
			logger.info("找不到对应积分规则---ruleType:"+ruleType);
			return;
		}
		
		processPointByRule(messageModel, ruleList, pointTartget);
	}

}
