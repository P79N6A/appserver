package com.idcq.appserver.dao.cashcoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;

public interface IUserCashCouponDao {
	
	/**
	 * 领取代金券
	 */
	int obtainCoupon(UserCashCouponDto dto);
	
	
	/**
	 * 批量领取代金券
	 */
	int obtainCoupons(List<UserCashCouponDto> lst);
	
	
	/**
	 * 消费代金券
	 */
	int consumeCoupon(Long userId,String orderId, Long uccId,double usePrice);
	
	int deleteCoupon0(UserCashCouponDto dto) ;

	UserCashCouponDto getUserCoupon(Long userId,Long cashCouponId);
	
	
	UserCashCouponDto getUserCouponDto(Long userId,Long uccId);
	
	void updateUserCashCoupon(Map param);
	
	public Integer selectPerDayPerPerson(Long userId);
	
	public Integer selectCountUseNumber(String orderId);
	
	/**
	 * 获取指定订单已经使用代金券的使用数量
	 * 
	 * @param orderId
	 * @param ccId
	 * @return
	 * @throws Exception
	 */
	int getCashUsedNumOfOrder(String orderId,Long ccId,Integer orderPayType) throws Exception;
	
	/**
	 * 根据代金券编号查询代金券信息
	 * @param uccIds
	 * @return
	 * @throws Exception
	 */
	List<Map> queryUserCashCouponsByUccIds(List<Long> uccIds)throws Exception;
	
	/**
	 * 获取代金券详情
	 * @param uccId
	 * @return
	 * @throws Exception
	 */
	UserCashCouponDto getUserCashCouponInfo(Long uccId) throws Exception;
	
	/**
	 * 获取用户消费金余额
	 * 
	 * @Function: com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao.getUserCashCouponBalance
	 * @Description:
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月23日 上午10:16:19
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    shengzhipeng       v1.0.0         create
	 */
	Double getUserCashCouponBalance(Long userId) throws Exception;
	
	List<UserCashCouponDto> getUserCashCouponByUserId(Long userId) throws Exception;
	
	/**
	 * 根据用户编号查询用户消费卡信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Map queryUserCashCouponsByUserId(Long userId) throws Exception;
	
}
