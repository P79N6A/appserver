package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableShopDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;

@Repository
public class ShopCouponAvailableShopDaoImpl extends BaseDao<ShopCouponAvailableShopDto> implements IShopCouponAvailableShopDao {

	@Override
	public int delShopCouponAvailableShopDao(ShopCouponDto shopCouponDto) {
		return super.update(generateStatement("delShopCouponAvailableShopDao"),shopCouponDto);
		
	}

	@Override
	public int insertShopCouponAvailableShop(ShopCouponAvailableShopDto shopCouponAvailableShopDto) {
		return super.insert("insertShopCouponAvailableShop", shopCouponAvailableShopDto);
		
	}

	@Override
	public List<ShopCouponAvailableShopDto> getShopCouponAvailableShopByMap(
			Map<String, Object> param) {
		return (List)super.findList(generateStatement("getShopCouponAvailableShopByMap"),param);
	}

}
