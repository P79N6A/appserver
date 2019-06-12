package com.idcq.appserver.service.shop;

import java.util.Map;

import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.order.OrderDto;

/**
 * 商铺账单流水业务逻辑层
 * @ClassName: IShopBillService 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年11月24日 下午7:32:02 
 *
 */
public interface IShopBillService {
	
	
	/**
	 * 查询商铺的账单
	 * @Title: queryShopBillById 
	 * @param @param shopBillId
	 * @param @return
	 * @param @throws Exception
	 * @return ShopBillDto    返回类型 
	 * @throws
	 */
	public ShopBillDto queryShopBillById(Integer shopBillId)throws Exception;
	
	public void insertShopBill(Integer billDirection,Double payAmount,
			Long transactionId, Long shopId, Double amount,
			Double accountAfterAmount ,Integer accountType,String billType,
			String billTitle,String billDesc, OrderDto order) 
			throws Exception;
	/**
	 * 查询提现账单详情
	 * @Title: queryDrawShopBillDto 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception
	 * @return ShopBillDto    返回类型 
	 * @throws
	 */
	public Map<String,Object> queryDrawShopBillDto(ShopBillDto shopBillDto)throws Exception;
	
	/**
	 * 查询推荐奖励账单
	 * @Title: queryRecommandRewardBill 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	public Map<String,Object>queryRecommandRewardBill(ShopBillDto shopBillDto)throws Exception;
	
	/**
	 * 查询收银账单
	 * @Title: queryCashierBill 
	 * @param @param shopBillDto
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	public Map<String,Object>queryCashierBill(ShopBillDto shopBillDto)throws Exception;
}
