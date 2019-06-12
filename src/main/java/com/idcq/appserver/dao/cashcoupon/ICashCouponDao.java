package com.idcq.appserver.dao.cashcoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.cashcoupon.CashCouponDto;

/**
 * @author Administrator
 * 
 * @date 2015年4月1日
 * @time 下午3:21:41
 */
public interface ICashCouponDao {

	/**
	 * 获取代金券列表
	 * @param pageNo 页码 
	 * @param pageSize 每页记录数
	 * @return 
	 */
	List<CashCouponDto> getCashCouponList(long cityId,int pageNo,int pageSize);
	
	/**
	 * 获取代金券总数
	 */
	int getCashCouponListCount(long cityId);
	
	/**
	 * 获取用户代金券列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<CashCouponDto> getUserCashCouponList(long userId,int pageNo,int pageSize);
	
	/**
	 * 获取用户代金券总数
	 * 
	 * @param userId
	 * @return
	 */
	int getUserCashCouponListCount(long userId);
	
	
	CashCouponDto getCouponDto(long cashCouponId);
	
	//修改可用数量
	int updateAvaliableNum(long cashCouponId);
	
	/**
	 * 获取商铺的代金券列表
	 * @param pageNo 页码 
	 * @param pageSize 每页记录数
	 * @return 
	 */
	List<Map> getShopCouponList(Map param);
	
	/**
	 * 获取商铺的代金券总数
	 */
	int getShopCouponTotal(Map param);
	
	//获取有效的cash_coupon
	CashCouponDto getCouponDtoForGrab(long cashCouponId);
	
	/**
	 * 根据状态查询用户持有代金券数
	 * @param userId 用户id
	 * @param status 用户是否使用 已使用-1,未使用-0
	 * @param useStatus 代金券状态：启用-1,停用-0
	 * @return
	 */
	int getUserCashCouponCountBy(Long userId, Integer status, Integer useStatus);
	
	/**
	 * 
	* @Title: getCashCouponByShopId 
	* @Description: TODO(根据商铺编号查找代金卷) 
	* @param @param shopId
	* @param @return
	* @return List<CashCouponDto>    返回类型 
	* @throws
	 */
	List<CashCouponDto>getCashCouponByShopId(Long shopId);
	/**
	 * 获取用户代金券余额
	 * @return
	 */
	Map<String, Object> getCashCouponAmount(Long userId);
}
