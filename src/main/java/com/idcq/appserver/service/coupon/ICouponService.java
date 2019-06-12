package com.idcq.appserver.service.coupon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.dto.coupon.UserCouponDto;

public interface ICouponService {

	
	/**
	 * 获取商铺优惠券列表
	 * 
	 * @param coupon
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	PageModel getShopCouponList(CouponDto coupon ,int pNo,int pSize) throws Exception;
	
	/**
	 * 获取商铺优惠券集合
	 * 
	 * @param coupon
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<CouponDto> getShopCouponListById(Long shopId) throws Exception;
	
	/**
	 * 获取用户优惠券列表
	 * 
	 * @param coupon
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	PageModel getUserCouponList(UserCouponDto coupon ,int pNo,int pSize) throws Exception;
	
	/**
	 * 获取指定的优惠券
	 * 
	 * @param cpid
	 * @return
	 * @throws Exception
	 */
	CouponDto getCouponById(Long cpId) throws Exception;
	
	/**
	 * 检查当天已经领取了指定优惠券的张数
	 * 
	 * @param cpId
	 * @return
	 * @throws Exception
	 */
	int getGrapNumOfDateRange(Long userId,Long cpId,Date startDate,Date endDate) throws Exception;
	
	/**
	 * 用户领取优惠券
	 * <p>
	 * 	主要业务：先关联用户领取优惠券表，对应优惠券可用数量减去1
	 * </P>
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	String grabCoupon(UserCouponDto coupon) throws Exception;
	
	int consumeCoupon(Long userId,String orderId,Long ucId,Integer orderPayType) throws Exception;

	/**
	 * 获取商铺红包
	 * @param shopId
	 * @param pNo
	 * @param pSize
	 * @return
	 */
	Map getShopRedPacket(Long shopId, int pNo, int pSize) throws Exception;
}
