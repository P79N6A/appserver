package com.idcq.appserver.dao.coupon;

import java.util.Date;
import java.util.List;

import com.idcq.appserver.dto.coupon.CouponDto;

public interface ICouponDao {
	
	/**
	 * 获取优惠券列表
	 * 
	 * @param coupon
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<CouponDto> getCouponList(CouponDto coupon,int pNo,int pSize) throws Exception;
	
	/**
	 * 获取符合条件优惠券总记录数
	 * 
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	int getCouponListCount(CouponDto coupon) throws Exception;
	
	CouponDto getCouponDtoById(Long couponId);
	
	/**
	 * 获取指定的优惠券
	 * 
	 * @param cpId
	 * @return
	 * @throws Exception
	 */
	CouponDto getCouponById(Long cpId) throws Exception;
	
	/**
	 * 验证优惠券存在性
	 * 
	 * @param cpId
	 * @return
	 * @throws Exception
	 */
	Long queryCouponExists(Long cpId) throws Exception;
	
	/**
	 * 优惠券可用数量减1
	 * 
	 * @param cpId
	 * @return
	 * @throws Exception
	 */
	int delCouponAvailNum(Long cpId ,int num) throws Exception;
	
	/**
	 * 优惠券已用数量加1
	 * 
	 * @param cpId
	 * @param num
	 * @return
	 * @throws Exception
	 */
	int incrCouponUsedNum(Long cpId ,int num) throws Exception;
	
	/**
	 * 获取指定时间范围内用户已经领取了指定优惠券的数量
	 * 
	 * @param userId	
	 * @param cpId	优惠券ID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	int getGrapNumByDateRange(Long userId,Long cpId,Date startDate,Date endDate) throws Exception;
	
	/**
	 * 查询用户优惠券数量
	 * @param userId 用户id
	 * @param couponStatus 优惠券本身状态
	 * @param useStatus 优惠劵使用状态
	 * @return
	 */
	int getUserCouponCountBy(Long userId, Integer couponStatus, Integer useStatus) ;
}
