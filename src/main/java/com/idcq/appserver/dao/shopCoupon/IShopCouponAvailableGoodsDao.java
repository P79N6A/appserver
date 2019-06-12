package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableGoodsDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;

public interface IShopCouponAvailableGoodsDao {
	/**
	 * 插入店铺优惠券可用商品关系
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IShopCouponAvailableGoodsDao.insertShopCouponAvailableGoods
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月21日 上午9:54:35
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月21日    ChenYongxin      v1.0.0         create
	 */
	Integer insertShopCouponAvailableGoods(ShopCouponAvailableGoodsDto shopCouponAvailableGoods) throws Exception;

	int delShopCouponAvailableGoods(ShopCouponDto shopCouponDto);

	List<ShopCouponAvailableGoodsDto> getShopCouponAvailableGoodsByMap(Map<String, Object> param);

}
