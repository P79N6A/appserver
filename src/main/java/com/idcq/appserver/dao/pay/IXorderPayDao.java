package com.idcq.appserver.dao.pay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.pay.XorderPayDto;

public interface IXorderPayDao {

	
	/**
	 * 新增非会员支付信息
	 * @param xOrderPay
	 * @return
	 * @throws Exception
	 */
	int addXorderPayDto(XorderPayDto xOrderPay) throws Exception;
	/**
	 * 根据订单id和收款人类型查询非会员订单支付
	 * 
	 * @Function: com.idcq.appserver.dao.pay.IXorderPayDao.getAmountByXOrderIdAndPayeeType
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月23日 上午11:30:58
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getAmountByXOrderIdAndPayeeType(Map<String, Object> map)throws Exception;
	/**
	 * 	 根据订单id和支付类型查询非会员订单支付
	 * 
	 * @Function: com.idcq.appserver.dao.pay.IXorderPayDao.getAmountByXorderIdAndPayType
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月23日 上午11:33:33
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getAmountByXorderIdAndPayType(Map<String, Object> map)throws Exception;
	
	/**
	 * 非支付记录会员订单
	 * @Title: queryXorderPayList 
	 * @param @param xorderId
	 * @param @return
	 * @param @throws Exception
	 * @return XorderPayDto    返回类型 
	 * @throws
	 */
	List<XorderPayDto> queryXorderPayList(String xorderId)throws Exception;
	
	/**
	 * 查询订单支付记录，返回map
	 * @param xorderId
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> getPayLogByXorderId(String xorderId) throws Exception;
	
	/**
	 * 获取当前订单最大的支付序号
	 * @param xorderId
	 * @return
	 * @throws Exception
	 */
	Integer getMaxPayIndex(String xorderId)throws Exception;
	
	/**
	 * 获取改订单所有支付记录
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<XorderPayDto> getAllPayLog(String xorderId)throws Exception;
	
	/**
	 * 删除非会员订单支付记录
	 * @param xorderId
	 * @return
	 */
	int delXorderPayDtoByOrderId(String xorderId)throws Exception;
	
	/**
	 * 获取非会员订单已经支付金额
	 * @param xorderId
	 * @return
	 * @throws Exception
	 */
	BigDecimal getTotalPayAmountByXorderId(String xorderId)throws Exception;
	
	/**
	 * 根据订单编号集合查询订单支付记录（cashPay、posPay、onlinePay）
	 * @param xorderIds
	 * @return
	 * @throws Exception
	 */
	List<Map> getXorderPayDetail(List<String> xorderIds)throws Exception;
	
	
	List<Map> getXorderPayList(String orderId)throws Exception;
}
