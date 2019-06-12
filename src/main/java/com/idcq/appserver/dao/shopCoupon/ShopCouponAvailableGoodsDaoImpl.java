package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableGoodsDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;

@Repository
public class ShopCouponAvailableGoodsDaoImpl extends BaseDao<ShopCouponAvailableGoodsDto> implements IShopCouponAvailableGoodsDao {

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IShopCouponAvailableGoodsDao#insertShopCouponAvailableGoods(com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableGoodsDto)
	 */
	@Override
	public Integer insertShopCouponAvailableGoods(
			ShopCouponAvailableGoodsDto shopCouponAvailableGoods)
			throws Exception {
		
		return super.insert("insertShopCouponAvailableGoods", shopCouponAvailableGoods);
	}

	@Override
	public int delShopCouponAvailableGoods(ShopCouponDto shopCouponDto) {
		return super.update(generateStatement("delShopCouponAvailableGoods"),shopCouponDto);
	}

	@Override
	public List<ShopCouponAvailableGoodsDto> getShopCouponAvailableGoodsByMap(
			Map<String, Object> param) {
		return (List)super.findList(generateStatement("getShopCouponAvailableGoodsByMap"),param);
	}
	
	

}
