package com.idcq.appserver.dao.shopCoupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
@Repository
public class ShopCouponDaoImpl extends BaseDao<ShopCouponDto> implements IShopCouponDao{

	@Override
	public void updateShopCoupon(ShopCouponDto shopCouponDto) {
		super.update(generateStatement("updateShopCoupon"),shopCouponDto);
	}

	@Override
	public int addShopCoupon(ShopCouponDto shopCouponDto) {
		return super.insert(generateStatement("addShopCoupon"), shopCouponDto);
	}

	@Override
	public List<Map<String, Object>> getShopCouponList(ShopCouponDto shopCouponDto) {
		return (List)super.findList(generateStatement("getShopCouponList"),shopCouponDto);
	}

	@Override
	public int getShopCouponListCount(ShopCouponDto shopCouponDto) {
		return (Integer)selectOne(generateStatement("getShopCouponListCount"),shopCouponDto);
	}

	@Override
	public Map<String, Object> shopCouponDetail(ShopCouponDto shopCouponDto) {
		return (Map) super.selectOne(generateStatement("shopCouponDetail"), shopCouponDto);
	}

	@Override
	public int operateShopCoupon(Map<String, Object> map) {
		return super.update(generateStatement("operateShopCoupon"),map);
		
	}

	@Override
	public List<Map<String, Object>> getUserShopCouponList(UserShopCouponDto userShopCoupon) {
		return (List)super.findList(generateStatement("getUserHoldShopCouponList"),userShopCoupon);
	}
	
	@Override
	public List<Map<String, Object>> getUserHoldShopCouponListNotFilter(UserShopCouponDto userShopCoupon) {
		return (List)super.findList(generateStatement("getUserHoldShopCouponListNotFilter"),userShopCoupon);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IShopCouponDao#getShopCouponById()
	 */
	@Override
	public ShopCouponDto getShopCouponById(Long shopCouponId) {
		
		return (ShopCouponDto) super.selectOne("getShopCouponById", shopCouponId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IShopCouponDao#getCouponReceiveDetail(java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getCouponReceiveDetail(Long shopId,
			Long shopCouponId, Integer status, Integer pageNo, Integer pageSize) {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("shopId", shopId);
		parms.put("shopCouponId", shopCouponId);
		parms.put("couponStatus", status);
		parms.put("pageSize", pageSize);
		parms.put("pageNo", (pageNo-1)*pageSize);
		
		return (List)super.findList(generateStatement("getCouponReceiveDetail"),parms);
	}

	@Override
	public List<ShopCouponDto> getShopCouponListByDtoAndPage(Map<String,Object>params) {
		return super.findList(generateStatement("getShopCouponListByDtoAndPage"),params);
	}

	@Override
	public void batchUpdateShopCoupon(Map<String, Object> params) {
		super.update(generateStatement("batchUpdateShopCoupon"), params);
	}

	
}
