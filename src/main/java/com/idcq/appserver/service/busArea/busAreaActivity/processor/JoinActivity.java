package com.idcq.appserver.service.busArea.busAreaActivity.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.BusAreaActShopStatusEnum;
import com.idcq.appserver.common.enums.BusAreaActShopTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActUserGetSourceTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActUserSourceTypeEnum;
import com.idcq.appserver.common.enums.BusAreaOperateTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.activity.IBusinessAreaShopDao;
import com.idcq.appserver.dao.activity.IBusinessAreaUserDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;
import com.idcq.appserver.dto.activity.BusinessAreaUserDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.thread.ThreadPoolManager;

@Service("JoinActivity")
public class JoinActivity implements IProcessor {

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
		BusinessAreaActivityDto activity = businessAreaActivityDao.getBusinessAreaActivityById(businessAreaActivityId);
		Integer actNum = activity.getActNum();
		actNum  = actNum + 1;
		activity.setActNum(actNum);
		businessAreaActivityDao.updateBusinessAreaActivityById(activity);
		
		
		Map<String, Object> queryMemberMap = new HashMap<String, Object>();
		Long shopId = Long.valueOf(params.get("shopId").toString());
		queryMemberMap.put("shopId", shopId);
		List<ShopMemberDto> shopMemeberList = shopMemberDao.getShopMemberList(queryMemberMap);
		
		BusinessAreaShopDto busAreaShop = new BusinessAreaShopDto();
		busAreaShop.setBusinessAreaActivityId(businessAreaActivityId);
		busAreaShop.setShopId(shopId);
		busAreaShop.setShopType(BusAreaActShopTypeEnum.JOIN_SHOP.getValue());
		busAreaShop.setStatus(BusAreaActShopStatusEnum.JOIN.getValue());
		busAreaShop.setMemberNum(shopMemeberList.size());
		busAreaShop.setJoinTime(new Date());
		busAreaShopDao.addBusinessAreaShop(busAreaShop);
		
		BusinessAreaUserDto busAreaUser = new BusinessAreaUserDto();
		busAreaUser.setBusinessAreaActivityId(businessAreaActivityId);
		busAreaUser.setShopId(shopId);
		for (ShopMemberDto shopMember : shopMemeberList) {
			if (shopMember.getMobile() == null) {
				continue;
			}
			busAreaUser.setMemberId(shopMember.getMemberId());
			busAreaUser.setUserId(shopMember.getUserId());
			busAreaUser.setMobile(shopMember.getMobile());
			busAreaUser.setGetSourceType(BusAreaActUserGetSourceTypeEnum.SHOP_IMPORT.getValue());
			busAreaUser.setUserSourceType(BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue());
			busAreaUserDao.addBusinessAreaUser(busAreaUser);
		}
		
		final Map<String, Object> notifyParams = new HashMap<String, Object>();
		notifyParams.put("shopId", shopId);//参与方商铺
		notifyParams.put("businessAreaActivityId", businessAreaActivityId);//操作的活动
		notifyParams.put("operateType", BusAreaOperateTypeEnum.JOIN_ACTIVITY.getIndex());//1-参与方参与活动  2-参与方取消活动
		ThreadPoolManager.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					ProgramUtils.executeBeanByExecutePointCode("joinShopOptActivityPoint", 1, notifyParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return CodeConst.CODE_SUCCEED;
	}

	
}
