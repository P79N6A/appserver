package com.idcq.appserver.service.cashcoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;

public interface IUserCashCouponService {
	
	int obtainCoupon(UserCashCouponDto dto) throws Exception;
	
	int obtainCoupons(List<UserCashCouponDto> lst) throws Exception;
	
	/**
	 * 用户消费代金券
	 * 
	 * @param orders	订单列表
	 * @param order
	 * @param pay		支付流水
	 * @param orderPayType	订单支付类型
	 * @return
	 * @throws Exception
	 */
	int consumeCoupon(Long userId, String uccIds, String orderId,Integer orderPayType) throws Exception;
//	int consumeCoupon(List<Map> orders,OrderDto  order,PayDto pay,Integer orderPayType) throws Exception;
	
	//根据uccId获取代金券面额
	UserCashCouponDto getUserCouponDto(Long userId,Long uccId) throws Exception;
	
	Integer selectPerDayPerPerson(Long userId) throws Exception;
	
	Integer selectCountUseNumber(String orderId) throws Exception;
	
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
	 * 用户使用代金券
	 * @param userId 用户编号
	 * @param orderId 订单编号
	 * @param orderPayType  订单支付类型（0-单个订单 1-订单组）
	 * @param uccIds 代金券集合
	 * @param pwdType 密码校验类型
	 * @param password 密码值
	 * @return
	 * @throws Exception
	 */
	int consumeCashCoupon(Long userId, String orderId, int orderPayType,
			List<Long> uccIds, Integer pwdType, String password) throws Exception;
	/**
	 * 获取用户消费金余额
	 * 
	 * @Function: com.idcq.appserver.service.cashcoupon.IUserCashCouponService.getUserCashCouponBalance
	 * @Description:
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月23日 上午10:22:02
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    shengzhipeng       v1.0.0         create
	 */
	Double getUserCashCouponBalance(Long userId) throws Exception;
	/**
	 * 通过用户id查询代金券信息
	 * 
	 * @Function: com.idcq.appserver.service.cashcoupon.IUserCashCouponService.queryUserCashCouponsByUserId
	 * @Description:
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月2日 下午4:10:03
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月2日    ChenYongxin      v1.0.0         create
	 */
	Map queryUserCashCouponsByUserId(Long userId) throws Exception;
}
