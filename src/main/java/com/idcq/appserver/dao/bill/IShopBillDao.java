package com.idcq.appserver.dao.bill;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.bill.ShopBillDto;

public interface IShopBillDao {
	
	public int insertShopBill(ShopBillDto shopBillDto)  throws Exception;
	/**
	 * 查询商铺账单列表
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getShopBill(Map<String, Object> map) throws Exception;
	/**
	 * 查询商铺账单总记录数
	 * @return
	 * @throws Exception
	 */
	Integer getShopBillCount(Map<String, Object> map) throws Exception;
	
	public void updateShopBill(ShopBillDto shopBillDto) throws Exception;
	
	void updateShopBillById(ShopBillDto shopBillDto) throws Exception;
	
	void updateShopBillByTransactionId(ShopBillDto shopBillDto) throws Exception;
	
	List<ShopBillDto> getShopBillByOrderId(String orderId, Integer billStatus) throws Exception;
	
	/**
	 * 获得店铺账单流水和非会员订单的结合
	 * @Title: getShopCombineBill 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<Map<String,Object>>    返回类型 
	 * @throws
	 */
	List<Map<String,Object>> getShopCombineBill(Map<String,Object>map)throws Exception;
	
	/**
	 * 获得非会员订单和会员订单的账单总数
	 * @Title: getShopCombineBillCount 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return Integer    返回类型 
	 * @throws
	 */
	Integer getShopCombineBillCount(Map<String, Object> map)throws Exception;
	
	/**
	 * 一点管家账单统计
	 * @Title: getIdcqBillStatistics 
	 * @param @param params
	 * @param @return
	 * @return List<Map<String,Object>>    返回类型 
	 * @throws
	 */
	public List<Map<String, Object>> getIdcqBillStatistics(
			Map<String, Object> params);
	
	/**
	 * 统计一点传奇账单数量
	 * @Title: getIdcqBillStatisticsCount 
	 * @param @param params
	 * @param @return
	 * @return Integer    返回类型 
	 * @throws
	 */
	public Integer getIdcqBillStatisticsCount(Map<String, Object> params);
	
	/**
	 * 查询单个店铺账单
	 * @Title: queryShopBill 
	 * @param @param shopBillId
	 * @param @return
	 * @param @throws Exception
	 * @return ShopBillDto    返回类型 
	 * @throws
	 */
	ShopBillDto queryShopBill(Integer shopBillId)throws Exception;
	
	/**
	 * 按时间统计收入支出
	 * @Title: getIdcqBillStatisticsAmount 
	 * @param @param params
	 * @param @return
	 * @return Double    返回类型 
	 * @throws
	 */
	public Double getIdcqBillStatisticsAmount(Map<String, Object> params);
	
	 /**
     * 新增店铺临时账单
     * <功能详细描述>
     * @param shopBillDto 账单对象
     * @return
     * @author  shengzhipeng
     */
    int insertShopMiddleBill(ShopBillDto shopBillDto)  throws Exception;
    
    /**
     * 根据订单ID获取临时账单列表
     * <功能详细描述>
     * @param orderId 订单ID
     * @return
     * @author  shengzhipeng
     */
    List<ShopBillDto> getShopBillMiddleByOrderId(String orderId) throws Exception;
    
    /**
     * 批量删除临时账单
     * @param orderId 订单ID
     * @param shopBillIds 账单ID列表
     * @author  shengzhipeng
     */
    void deleteShopBillMiddle(String orderId, List<Long> shopBillIds);
    
    /**
     * CS19：查询店铺账户账单统计接口
     * @param map
     * @return
     * @throws Exception
     */
	public List<Map<String, Object>> getBillStat(Map<String, Object> map) throws Exception;
	
	Integer getBillDetailCount(Map<String, Object> map) throws Exception;

	/**
	 *  CS20：查询店铺账户账单明细接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBillDetail(Map<String, Object> map) throws Exception;
    
	double getShopRewardTotalBy(Map<String, Object> map) throws Exception;
	/**
	 *  获取转充总额
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getchargeTotalMoney(Long shopId,Integer accountType,Integer billType,Date startTime,Date endTime) throws Exception;
	
	public double getShopBillSumMoney(String orderId,Integer amountType);
}
