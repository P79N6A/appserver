package com.idcq.appserver.service.busArea.busAreaActivity.processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.BusAreaOperateTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.activity.IBusinessAreaShopDao;
import com.idcq.appserver.dao.activity.IBusinessAreaUserDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.utils.ProgramUtils;

@Service("CancelActivityByJoiner")
public class CancelActivityByJoiner implements IProcessor {

	@Autowired
	private IBusinessAreaActivityDao businessAreaActivityDao;
	@Autowired
	private IShopMemberDao shopMemberDao;
	@Autowired
	private IBusinessAreaShopDao busAreaShopDao;
	@Autowired
	private IBusinessAreaUserDao busAreaUserDao;
	
	@Override
	public Object exective(Map<String, Object> params) throws Exception {
		Long businessAreaActivityId = Long.valueOf(params.get("businessAreaActivityId").toString());
		Long shopId = Long.valueOf(params.get("shopId").toString());
		BusinessAreaActivityDto activity = businessAreaActivityDao.getBusinessAreaActivityById(businessAreaActivityId);
		Integer actNum = activity.getActNum();
		actNum = actNum - 1;
		if (actNum < 1) {
			activity.setActNum(1);
		}else {
			activity.setActNum(actNum);
		}
		
		businessAreaActivityDao.updateBusinessAreaActivityById(activity);
		
		busAreaShopDao.delBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
		
		busAreaUserDao.delBusinessAreaUserByShopId(shopId);
		
		Map<String, Object> notifyParams = new HashMap<String, Object>();
		notifyParams.put("shopId", shopId);//参与方商铺
		notifyParams.put("businessAreaActivityId", businessAreaActivityId);//操作的活动
		notifyParams.put("operateType", BusAreaOperateTypeEnum.CANCEL_BY_JOINER.getIndex());//1-参与方参与活动  2-参与方取消活动
		ProgramUtils.executeBeanByExecutePointCode("joinShopOptActivityPoint", 1, notifyParams);
		return CodeConst.CODE_SUCCEED;
	}

}
