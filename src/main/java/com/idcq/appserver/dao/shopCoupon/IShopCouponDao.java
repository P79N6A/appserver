package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;


public interface IShopCouponDao {

	void updateShopCoupon(ShopCouponDto shopCouponDto);

	int addShopCoupon(ShopCouponDto shopCouponDto);

	List<Map<String, Object>> getShopCouponList(ShopCouponDto shopCouponDto);

	int getShopCouponListCount(ShopCouponDto shopCouponDto);

	Map<String, Object> shopCouponDetail(ShopCouponDto shopCouponDto);

	int operateShopCoupon(Map<String, Object> map);
	
	List<Map<String, Object>> getUserShopCouponList(UserShopCouponDto userShopCoupon);
	List<Map<String, Object>> getUserHoldShopCouponListNotFilter(UserShopCouponDto userShopCoupon);
	List<Map<String, Object>> getCouponReceiveDetail(Long shopId ,Long shopCouponId,Integer status,Integer pageNo,Integer pageSize);

	/**
	 * 获取单个优惠券
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IShopCouponDao.getShopCouponById
	 * @Description:
	 *
	 * @param shopCouponId
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月21日 上午11:31:48
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月21日    ChenYongxin      v1.0.0         create
	 */
	ShopCouponDto getShopCouponById(Long shopCouponId);

	List<ShopCouponDto> getShopCouponListByDtoAndPage(Map<String,Object>params);

	void batchUpdateShopCoupon(Map<String, Object> params);

}
