package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableShopDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;

public interface IShopCouponAvailableShopDao {

	int delShopCouponAvailableShopDao(ShopCouponDto shopCouponDto);

	int insertShopCouponAvailableShop(ShopCouponAvailableShopDto shopCouponAvailableShopDto);

	List<ShopCouponAvailableShopDto> getShopCouponAvailableShopByMap(Map<String, Object> param);

}
