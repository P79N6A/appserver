package com.idcq.appserver.dao.order;

import com.idcq.appserver.dto.order.XorderGoodsDto;

public interface IXorderGoodsDao {
	/**
	 *  新增非会员订单商品
	 * @param xorderGoodsDto
	 * @return
	 * @throws Exception
	 */
	int addXorderGoodsDto(XorderGoodsDto xorderGoodsDto) throws Exception;
	
	/**
	 * 同比修改订单商品项参与结算的金额
	 * 
	 * @param orderId
	 * @param ratio
	 * @return
	 * @throws Exception
	 */
	int updateSettlePrice(String orderId ,Double ratio) throws Exception;
	
	/**
	 * 删除商品列表
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int delGoodsByOrderId(String orderId) throws Exception;
	
	/**
	 * 获取订单商品信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	XorderGoodsDto getXorderGoodsDto(XorderGoodsDto dto) throws Exception;
}
