package com.idcq.appserver.service.bill;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.user.UserBillDto;

@Service
public class UserBillServiceImpl implements IUserBillService {
	@Autowired
	public IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IUserBillDao userBillDao;
	
	@Override
	public void insertUserBill(Long userId,Long transactionId,Integer billDirection,
			  					Double payAmount,Double accountAfterAmount,
		  						Double userAcountAmount,OrderDto order,
	  							String billType,Integer orderPayType,String billDesc,
	  							Integer userBillType,Integer accountType, Long agentId) throws Exception{
		
		UserBillDto userBill = buildUserBill(userId,transactionId, billDirection, 
								payAmount, accountAfterAmount, userAcountAmount, 
								order, billType, orderPayType, 
								billDesc, userBillType, accountType, agentId);
		
		userBillDao.insertUserBill(userBill);
	}
	
	private UserBillDto buildUserBill(Long userId,Long transactionId,Integer billDirection,
									  Double payAmount,Double accountAfterAmount,
									  Double userAcountAmount,OrderDto order,
									  String billType,Integer orderPayType,String billDesc,
									  Integer userBillType,Integer accountType, Long agentId) throws Exception{
		int orderStatus = order.getOrderStatus()==null ? 21 : order.getOrderStatus();
	    int billStatus=orderStatus+20;
		UserBillDto userBillDto=new UserBillDto();
		userBillDto.setTransactionId(transactionId);
		userBillDto.setBillType(billType);
		userBillDto.setBillDirection(billDirection);
		userBillDto.setBillStatus(billStatus);
		userBillDto.setUserBillType(userBillType);
		userBillDto.setAccountType(accountType);
		userBillDto.setMoney(billDirection == 1 ? payAmount : -payAmount);
		userBillDto.setAccountAmount(userAcountAmount);
		userBillDto.setAccountAfterAmount(accountAfterAmount);
		userBillDto.setOrderId(order.getOrderId());
		userBillDto.setCreateTime(new Date());
		userBillDto.setBillDesc(billDesc);
		Long comsumerUserId = order.getUserId();
		userBillDto.setUserId(userId);
		userBillDto.setConsumerUserId(comsumerUserId != null ? comsumerUserId : Long.valueOf(0));
		userBillDto.setOrderPayType(orderPayType);
		userBillDto.setAgentId(agentId);
		userBillDto.setConsumerMobile(order.getMobile());
		userBillDto.setSettlePrice(order.getSettlePrice());
		if(billStatus==ConsumeEnum.ORDERED.getValue() 
				|| billStatus==ConsumeEnum.HAVE_ORDER.getValue() 
				|| billStatus==ConsumeEnum.DISPATCH.getValue()
				|| billStatus==ConsumeEnum.CHARGEBACKING.getValue()){
			
			userBillDto.setBillStatusFlag(1);
		}else{
			userBillDto.setBillStatusFlag(0);
		}
		userBillDto.setConsumerMobile(order.getMobile());
		String orderTitle = order.getOrderTitle();
		
		if(!StringUtils.isEmpty(orderTitle)){
			userBillDto.setBillTitle(orderTitle);
		}else{
			userBillDto.setBillTitle("消费"+payAmount);
		}
		userBillDto.setSettleTime(new Date());
		//billLogo设置为商铺logo
		List<Long> shopMicroLogo = attachmentRelationDao.getAttachRelatIdListByCondition(order.getShopId(), CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE);
		if(!CollectionUtils.isEmpty(shopMicroLogo)){
			userBillDto.setBillLogo(shopMicroLogo.get(0));
		}
		return userBillDto;
	}

	@Override 
	public List<Integer> getMyRewardType(Long userId, String[] accountTypes)
	{
		return userBillDao.getMyRewardType(userId, accountTypes);
	}
}
