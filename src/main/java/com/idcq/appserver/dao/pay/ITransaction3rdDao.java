package com.idcq.appserver.dao.pay;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.pay.Transaction3rdDto;

public interface ITransaction3rdDao {
	/**
	 * 使用第三方支付接口
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception
	 */
	Long payBy3rd(Transaction3rdDto transaction3rdDto) throws Exception;
	/**
	 *  我的第三方支付列表接口
	 * @param userId 用户id
	 * @param status 交易状态
	 * @param rdOrgName 第三方支付名称
	 * @param pageNo 当前页
	 * @param pageSiz 页大小
	 * @return
	 */
	List<Map<String, Object>> getMy3rdPay(Long userId ,int status,String rdOrgName,int pageNo,int pageSize)throws Exception;
	/**
	 * 我的第三方支付列表总数
	 * @param userId 用户id
	 * @param status 交易状态
	 * @param rdOrgName 第三方支付名称
	 * @param pageNo 当前页
	 * @param pageSiz 页大小
	 * @return 
	 */
	int getMy3rdPayCount(Long userId ,int status,String rdOrgName,int pageNo,int pageSize)throws Exception;
	/**
	 * 修改第三方支付状态
	 * @param transaction3rdDto
	 * @return
	 */
	int nofity3rdPayStatus(Transaction3rdDto transaction3rdDto)throws Exception;
	/**
	 * 根据userId和orderId查询记录数
	 * @param transaction3rdDto
	 * @return
	 */
    Long getPayByUserIdOrderId(Transaction3rdDto transaction3rdDto)throws Exception;
    /**
     * 根据id获取第三方支付信息
     * @param transactionId
     * @return
     */
    Transaction3rdDto ge3rdPayById(Transaction3rdDto transaction3rdDto)throws Exception;
	/**
	 * 查询商铺充值记录接口
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopWithDraw>    返回类型 
	 * @throws
	 */
	List<Map<String, Object>> getShopRechargeList(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺充值记录总数
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopWithDraw>    返回类型 
	 * @throws
	 */
	Integer getShopRechargeCount(Map<String,Object> map)throws Exception;
	
	/**
	 * 添加 交易记录
	 * @Title: addTransaction 
	 * @param @param transactionDto
	 * @return void    返回类型 
	 * @throws
	 */
	Integer addTransaction(Transaction3rdDto transactionDto);
}
