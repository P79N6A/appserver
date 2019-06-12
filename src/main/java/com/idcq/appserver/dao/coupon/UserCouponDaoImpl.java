package com.idcq.appserver.dao.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.coupon.UserCouponDto;


@Repository
public class UserCouponDaoImpl extends BaseDao<UserCouponDto>implements IUserCouponDao{

	public List<UserCouponDto> getUserCouponList(UserCouponDto coupon, int pNo, int pSize)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("coupon",coupon);
		map.put("n", (pNo-1)*pSize);
		map.put("m", pSize);
		return super.findList(generateStatement("getUserCouponList"),map);
	}

	public int getUserCouponListCount(UserCouponDto coupon) throws Exception {
		return (Integer)super.selectOne(generateStatement("getUserCouponListCount"), coupon);
	}
	
	public int grabCoupon(UserCouponDto coupon) throws Exception {
		Integer ucId = (Integer)super.selectOne(generateStatement("grabCoupon"),coupon);
		return ucId == null ? 0 : ucId;
	}

	public int consumeCoupon(Long userId,String orderId,Long ucId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("orderId", orderId);
		map.put("ucId", ucId);
		return super.update(generateStatement("consumeCoupon"), map);
	}

	public UserCouponDto getUserCouponById(Long userId, Long ucId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("ucId", ucId);
		return (UserCouponDto) super.selectOne("getUserCouponById", map);
	}

	public Long addUserCoupon(UserCouponDto coupon) throws Exception {
		super.insert(generateStatement("addUserCoupon"),coupon);
		Long ucId = coupon.getUcId();
		return  ucId == null ? 0 : ucId;
	}

	public int updateUserCoupon(Long userId, String orderId, int couponStatus) {
		Map param=new HashMap();
		param.put("userId", userId);
		param.put("orderId", orderId);
		param.put("couponStatus", couponStatus);
		return super.update("updateUserCoupon", param);
	}

	public void updateUserCouponByMap(Map map) {
		super.update("updateUserCouponByMap", map);
	}
	
	
}
