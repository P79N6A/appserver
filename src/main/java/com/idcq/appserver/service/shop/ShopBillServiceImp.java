package com.idcq.appserver.service.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.IWithdrawDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.utils.DateUtils;

/**
 * 店铺账单流水
 * @ClassName: ShopBillServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年11月24日 下午7:32:44 
 *
 */
@Service
public class ShopBillServiceImp implements IShopBillService{
	
	@Autowired
	private IShopBillDao shopBillDao;
	@Autowired
	private IWithdrawDao withDrawDao;
	
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IPayDao payDao;
	
	/**
	 * 根据账单id查找单个账单
	 * @Title: queryShopBillById 
	 * @param @param shopBillId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	@Override
	public ShopBillDto queryShopBillById(Integer shopBillId) throws Exception {
		return shopBillDao.queryShopBill(shopBillId);
	}

	/**
	 * 查询商铺的提现账单
	 * @Title: queryDrawShopBillDto 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	@Override
	public Map<String,Object> queryDrawShopBillDto(ShopBillDto shopBillDto)
			throws Exception {
		Long transactionId=shopBillDto.getTransactionId();
		Map<String,Object>resultJson=new HashMap<String,Object>();
		if(transactionId != null){//查询体现表
			WithdrawDto withDrawDto = withDrawDao.getWithdrawById(transactionId);
			resultJson.put("money",withDrawDto.getAmount());
			resultJson.put("billTime",DateUtils.format(withDrawDto.getApplyTime(), DateUtils.DATETIME_FORMAT));
			resultJson.put("bankName",withDrawDto.getBankName());//银行名称
		}
		return resultJson;
	}
	
	/**
	 * 查询推荐奖励账单
	 * @Title: queryRecommandRewardBill 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	@Override
	public Map<String, Object> queryRecommandRewardBill(ShopBillDto shopBillDto)
			throws Exception {
		Map<String,Object>resultJson=new HashMap<String,Object>();
		OrderDto orderDto=orderDao.getOrderById(shopBillDto.getOrderId());
		if(orderDto!=null){
			resultJson.put("money", shopBillDto.getMoney());
			resultJson.put("billTime", DateUtils.format(shopBillDto.getSettleTime(),DateUtils.DATETIME_FORMAT));//账单详情
			resultJson.put("member", orderDto.getUserId());
			resultJson.put("orderAmount",orderDto.getSettlePrice());//订单结算价格为消费金额
			resultJson.put("orderNo",orderDto.getOrderId());//订单编号
		}
		return resultJson;
	}	
	
	/**
	 * 查询收银账单
	 * @Title: queryCashierBill 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public Map<String, Object> queryCashierBill(ShopBillDto shopBillDto)
			throws Exception {
		Map<String,Object>resultJson=new HashMap<String,Object>();
		OrderDto orderDto=orderDao.getOrderById(shopBillDto.getOrderId());
		
		if(orderDto!=null){
			List<PayDto>payDtos=payDao.getOrderPayList(orderDto.getOrderId(),CommonConst.PAY_STATUS_PAY_SUCCESS);
			UserDto userDto=userDao.getDBUserById( orderDto.getUserId());
			resultJson.put("money", orderDto.getSettlePrice());
			resultJson.put("billTime", DateUtils.format(shopBillDto.getSettleTime(),DateUtils.DATETIME_FORMAT));//账单详情
			if(userDto!=null){
				resultJson.put("member", userDto.getMobile());
			}
			if(payDtos!=null&&payDtos.size()>0){
				PayDto payDto=payDtos.get(0);
				if(payDto!=null){
					resultJson.put("payType",payDto.getPayType());
				}
			}
			resultJson.put("remark",orderDto.getOrderTitle());//订单备注
			resultJson.put("orderNo",orderDto.getOrderId());//订单编号
		}
		return resultJson;
	}

	
	public void insertShopBill(Integer billDirection,Double payAmount,
								Long transactionId, Long shopId, Double amount,
								Double accountAfterAmount ,Integer accountType,String billType,
								String billTitle,String billDesc, OrderDto order) 
								throws Exception {
		
			ShopBillDto shopBillDto = new ShopBillDto();
			shopBillDto.setTransactionId(transactionId);
			if(order != null) {
			    shopBillDto.setOrderId(order.getOrderId());
			    shopBillDto.setSettlePrice(order.getSettlePrice());
			}
			shopBillDto.setBillStatus(2); 
			shopBillDto.setCreateTime(new Date());
			shopBillDto.setShopId(shopId);
			shopBillDto.setMoney(billDirection == CommonConst.BILL_DIRECTION_ADD ? payAmount : -payAmount);
			shopBillDto.setBillType(billType);
			shopBillDto.setBillDirection(billDirection);
			shopBillDto.setAccountAmount(amount);
			shopBillDto.setBillDesc(billDesc);
			shopBillDto.setAccountType(accountType);
			shopBillDto.setAccountAfterAmount(accountAfterAmount);
			shopBillDto.setBillTitle(billTitle);
			shopBillDto.setSettleTime(new Date());
			if(order != null) {
			    shopBillDto.setConsumerMobile(order.getMobile());
			    shopBillDto.setConsumerUserId(order.getUserId());
			}
			shopBillDao.insertShopBill(shopBillDto);
	}
}
