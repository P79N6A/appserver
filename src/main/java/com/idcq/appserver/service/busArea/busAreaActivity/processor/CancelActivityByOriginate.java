package com.idcq.appserver.service.busArea.busAreaActivity.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.activity.IBusinessAreaConfigDao;
import com.idcq.appserver.dao.activity.IBusinessAreaShopDao;
import com.idcq.appserver.dao.activity.IBusinessAreaUserDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;

@Service("CancelActivityByOriginate")
public class CancelActivityByOriginate implements IProcessor {

	@Autowired
	private IBusinessAreaActivityDao businessAreaActivityDao;
	@Autowired
	private IShopMemberDao shopMemberDao;
	@Autowired
	private IBusinessAreaShopDao busAreaShopDao;
	@Autowired
	private IBusinessAreaUserDao busAreaUserDao;
	@Autowired
	private IBusinessAreaConfigDao busAreaConfigDao;
	@Override
	public Object exective(Map<String, Object> params) throws Exception {
		Long businessAreaActivityId = Long.valueOf(params.get("businessAreaActivityId").toString());
		
		businessAreaActivityDao.delBusinessAreaActivityById(businessAreaActivityId);
		
		busAreaShopDao.delBusinessAreaShopByActivityId(businessAreaActivityId);
		
		busAreaUserDao.delBusinessAreaUserByActivityId(businessAreaActivityId);
		
		busAreaConfigDao.delBusinessAreaConfigByActivityId(businessAreaActivityId);
		return CodeConst.CODE_SUCCEED;
	}

}
