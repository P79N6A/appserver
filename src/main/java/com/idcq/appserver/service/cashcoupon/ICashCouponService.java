package com.idcq.appserver.service.cashcoupon;

import java.util.List;

import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.dto.common.PageModel;

public interface ICashCouponService {

	/**
	 * 获取top代金券列表
	 * 
	 * @param cityId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getTopCashCouponList(long cityId,int pageNo,int pageSize) throws Exception;
	
	PageModel getUserCashCouponList(long userId,int pageNo,int pageSize) throws Exception;
	
	CashCouponDto getCouponDtoById(long cashCouponId) throws Exception;
	
	//修改可用数量
	int updateAvaliableNum(long cashCouponId) throws Exception;
	
	PageModel getShopCoupon(Long shopId,Integer queryType,Integer pageNo,Integer pageSize) throws Exception;
	
	//获取领取有效期内的代金券
	CashCouponDto getCouponDtoByIdForGrab(long cashCouponId) throws Exception;
	
	
	List<CashCouponDto>getCashCouponByShopId(Long shopId)throws Exception;
	
}
