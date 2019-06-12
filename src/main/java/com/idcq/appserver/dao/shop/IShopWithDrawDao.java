package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopWithDrawDto;

public interface IShopWithDrawDao {
	
	/**
	 * 查询商铺提现记录接口
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopWithDraw>    返回类型 
	 * @throws
	 */
	List<Map<String, Object>> getShopWithdrawList(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺提现记录总数
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopWithDraw>    返回类型 
	 * @throws
	 */
	Integer getShopWithdrawCount(Map<String, Object> map)throws Exception;
	/**
	 * 用户发起提现接口
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception
	 */
	Integer shopWithdraw(ShopWithDrawDto shopWithDrawDto) throws Exception;
	
	public ShopWithDrawDto getShopWithDrawById(Long id) throws Exception;
	
	Integer updateShopWithdraw(ShopWithDrawDto shopWithDrawDto) throws Exception;

	/**
	 * 获取时间段提现总额,用于商铺后台
	 * @Title: getWithdrawTotalMoney 
	 * @param   shopId
	 * @param   withdrawStatus
	 * @param   startTime
	 * @param   endTime
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String, Object>    返回类型 
	 */
	Map<String, Object> getWithdrawTotalMoney(Long shopId,Integer withdrawStatus,Date startTime,Date endTime ) throws Exception;
	/**
	 * 查询提现手续费
	 * @Function: com.idcq.appserver.dao.shop.IShopWithDrawDao.getwithdrawCommissionTotal
	 * @Description:
	 *
	 * @param shopId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getWithdrawCommissionTotal(Long shopId,Date startTime,Date endTime ) throws Exception;

	/**
	 * 获取提现基准金额,用于商铺后台
	 * @Title: getWithdrawTotalMoney 
	 * @param   shopId
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String, Object>    返回类型 
	 */
	Map<String, Object> getStandardMoney(Long shopId) throws Exception;
	
	
}
