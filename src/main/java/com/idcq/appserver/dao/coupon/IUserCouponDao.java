package com.idcq.appserver.dao.coupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.coupon.UserCouponDto;

public interface IUserCouponDao {
	
	/**
	 * 获取优惠券列表
	 * 
	 * @param coupon
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<UserCouponDto> getUserCouponList(UserCouponDto coupon,int pNo,int pSize) throws Exception;
	
	/**
	 * 获取符合条件优惠券总记录数
	 * 
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	int getUserCouponListCount(UserCouponDto coupon) throws Exception;
	
	/**
	 * 用户领取优惠券
	 * 
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	int grabCoupon(UserCouponDto coupon) throws Exception;
	
	/**
	 * 新增用户优惠券
	 * 
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	Long addUserCoupon(UserCouponDto coupon) throws Exception;
	
	int consumeCoupon(Long userId,String orderId, Long ucId);
	
	/**
	 * 获取优惠券
	 * @param userId
	 * @param ucId
	 * @return
	 */
	UserCouponDto getUserCouponById(Long userId, Long ucId);
	
	int updateUserCoupon(Long userId,String orderId,int couponStatus);
	
	/**
	 * 根据用户优惠劵id更新优惠价状态
	 * @param map
	 */
	void updateUserCouponByMap(Map map);
	
}
