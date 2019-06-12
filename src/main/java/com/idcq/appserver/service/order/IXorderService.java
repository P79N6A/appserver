package com.idcq.appserver.service.order;

import java.util.Map;

import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.XorderPayDto;


public interface IXorderService {
	/**
	 * 查询非会员订单存在性
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int queryXorderExists(String orderId) throws Exception;
	
	/**
	 * 获取指定的订单
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Integer getOrderStatusById(String orderId) throws Exception;
	
	XorderDto getXOrderById(String orderId) throws Exception;
	
	void updateXOrder(XorderDto orderDto)throws Exception;
	
	/**
	 * 获取收银订单的详情
	 * @Title: getCashierBillDetail 
	 * @param @param dto
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	Map<String,Object>getCashierBillDetail(XorderDto dto)throws Exception;
	
	/**
	 * 生成交易记录和支付记录
	 * @Title: generateTransactionAndPay 
	 * @param @param transactionDto
	 * @param @param xorderPayDto
	 * @return void    返回类型 
	 * @throws
	 */
	void generateTransactionAndPay(Transaction3rdDto transactionDto,
			XorderPayDto xorderPayDto)throws Exception;
	
	/**
	 * 一点管家 非会员订单详情
	 * @Title: getIdgjXorderDetail 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception
	 * @return XorderDto    返回类型 
	 * @throws
	 */
	XorderDto getIdgjXorderDetail(String orderId)throws Exception;

	/**
	 * 生成平台账单
	 * @Title: generatePlatformBill 
	 * @param @param xorderDto
	 * @param @param pltBillMnySourceZfb
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void generatePlatformBill(XorderDto xorderDto, Integer pltBillMnySourceZfb)throws Exception;

	/**
	 * 生成店铺账单中间表
	 * @Title: generateShopMiddleBill 
	 * @param @param xorderDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void generateShopMiddleBill(XorderDto xorderDto)throws Exception;
}
