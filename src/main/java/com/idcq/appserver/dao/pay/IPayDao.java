package com.idcq.appserver.dao.pay;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.pay.PayDto;

public interface IPayDao {
	
	/**
	 * 新增支付
	 * @return
	 * @throws Exception
	 */
	public Integer addOrderPay(PayDto payDto) throws Exception;
	
	public PayDto getDBOrderPayById(Long orderPayId) throws Exception;
	/**
	 * 第三方支付成功修改支付记录
	 * @param transactionId
	 * @throws Exception
	 */
	public void updateOrderPayAfterRdPaySuccess(PayDto payDto) throws Exception; 
	
	List<PayDto> getOrderPayByIdAndGroupId(String orderId,String groupId) throws Exception;
	/**
	 * 根据orderId求支付合计金额
	 */
	public double getSumPayAmount(String orderId,Integer payeeType) throws Exception;
	/**
     * 根据orderId求支付合计金额
     */
	public double getSumPayAmount(String orderId,Integer payeeType, Integer payType) throws Exception;
	/**
	 * 根据orderId获取支付记录
	 */
	public List<PayDto> getOrderPayList(String orderId,Integer payStatus) throws Exception;
	
	/**
	 * 	删除支付记录
	 * @param orderPayId
	 * @throws Exception
	 */
	public void deletePayByOrderPayId(Long orderPayId) throws Exception;
	 /**
     *  通过订单id删除支付记录
     * @param orderId
     * @throws Exception
     */
    public void deletePayByOrderId(String orderId) throws Exception;
	
	/**
	 * 形成订单支付组
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	public void groupOrders(String orderGroupId,String orderId) throws Exception;
	
	public List<Map> queryOrderGroupById(String orderGroupId) throws Exception;
	
	/**
	 * 根据订单组编号查询订单组中各订单信息
	 * @param orderGroupId
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryOrderGroupByOrderId(String orderGroupId) throws Exception;
	
	public double getSumOrderGroupAmount(String orderGroupId) throws Exception;
	
	/**
	 * 根据orderId查询商铺id
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Long getShopIdByOrderId(String orderId) throws Exception;
	
	Long getOrderPayIdByPayId(Long transactionId) throws Exception;
	
	/**
	 * 检查指定订单是否使用代金券支付过
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int checkOrderIsPayByCash(String orderId,Long ccId,Integer orderPayType) throws Exception;

	/**
	 * P40：查询订单支付详情接口
	 * @param orderId
	 * @return
	 */
	public List<Map> getOrderPayDetail(String orderId) throws Exception;
	/**
	 * 根据订单id和收款人类型查询订单支付
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getAmountByOrderIdAndPayeeType
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月23日 上午9:35:54
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getAmountByOrderIdAndPayeeType(Map<String, Object> map)throws Exception;
	/**
	 * 	 根据订单id和支付类型查询订单支付
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getAmountByOrderIdAndPayType
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月23日 上午9:36:27
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getAmountByOrderIdAndPayType(Map<String, Object> map)throws Exception;
	

	/**
	 * 查询订单支付记录
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> getPayLogByOrderId(String orderId)throws Exception;
	
	/**
	 * 获取当前订单最大的支付序号
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Integer getMaxPayIndex(String orderId)throws Exception;

	/**
	 * 根据订单编号列表批量查询订单支付信息（cashPay、posPay、onlinePay）
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	public List<Map> getAllOrderPayDetail(List<String> orderIds)throws Exception;
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.dao.pay.IPayDao.getPayResult
	 * @Description:
	 */
	public Map<String, Object> getPayResult(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取时间段提现总额,用于商铺后台
	 * @Title: getWithdrawTotalMoney 
	 * @param   shopId
	 * @param   payStatus '支付状态:待反馈支付进度=0,支付成功=1,支付失败=2',
	 * @param   payeeType 收款人类型：1点传奇平台收款-0, 商铺收款-1
	 * @param   startTime
	 * @param   endTime
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String, Object>    返回类型 
	 */
	Map<String, Object> getIncomeTotalMoney(Long shopId,Integer payStatus,Integer payeeType,Date startTime,Date endTime ) throws Exception; 
}
