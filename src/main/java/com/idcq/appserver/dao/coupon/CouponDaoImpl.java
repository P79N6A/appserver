package com.idcq.appserver.dao.coupon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.utils.DateUtils;

/**
 * 优惠券dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月30日
 * @time 上午10:47:34
 */
@Repository
public class CouponDaoImpl extends BaseDao<CouponDto>implements ICouponDao{

	public List<CouponDto> getCouponList(CouponDto coupon, int pNo, int pSize)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("coupon",coupon);
		map.put("n", (pNo-1)*pSize);
		map.put("m", pSize);
		return super.findList(generateStatement("getCouponList"),map);
	}

	public int getCouponListCount(CouponDto coupon) throws Exception {
		return (Integer)super.selectOne(generateStatement("getCouponListCount"), coupon);
	}
	
	public CouponDto getCouponDtoById(Long couponId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("couponId", couponId);
		map.put("curDate",	DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return (CouponDto)super.selectOne(generateStatement("getCouponDtoById"), map);
	}
	
	public CouponDto getCouponById(Long cpId) {
		return (CouponDto)super.selectOne(generateStatement("getCouponById"), cpId);
	}

	public int getUserCouponCountBy(Long userId, Integer couponStatus, Integer useStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("couponStatus", couponStatus);
		map.put("useStatus", useStatus);
		return (Integer)super.selectOne(generateStatement("getUserCouponCountBy"), map);
	}

	public int getGrapNumByDateRange(Long userId, Long cpId, Date startDate,
			Date endDate) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("cpId",cpId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		Integer num = (Integer)super.selectOne(generateStatement("getGrapNumByDateRange"), map);
		return num == null? 0 : num;
	}

	public int delCouponAvailNum(Long cpId,int num) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("cpId", cpId);
		map.put("num",num);
		return super.update(generateStatement("delCouponAvailNum"),map);
	}

	public int incrCouponUsedNum(Long cpId, int num) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("cpId", cpId);
		map.put("num",num);
		return super.update(generateStatement("incrCouponUsedNum"),map);
	}

	public Long queryCouponExists(Long cpId) throws Exception {
//		String couponRedis = DataCacheApi.get(CommonConst.KEY_COUPON+cpId);
//		if(!StringUtils.isBlank(couponRedis)){
//			CouponDto coupon = (CouponDto)JacksonUtil.jsonToObject(couponRedis, CouponDto.class, null);
//			return coupon.getCouponId();
//		}else{
//			return this.couponDao.queryCouponExists(cpId);
//		}
		return (Long)super.selectOne(generateStatement("queryCouponExists"), cpId);
	}

	
	
}
