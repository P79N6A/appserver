package com.idcq.appserver.dao.cashcoupon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.utils.DateUtils;

@Repository
public class CashCouponDaoImpl extends BaseDao<CashCouponDto> implements ICashCouponDao {

	public List<CashCouponDto> getCashCouponList(long cityId,int pageNo, int pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cityId", cityId);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return super.findList(generateStatement("getCashCouponList"),map);
	}

	public int getCashCouponListCount(long cityId) {
		return (Integer)this.selectOne(this.generateStatement("getCashCouponListCount"), cityId);
	}
	
	public List<CashCouponDto> getUserCashCouponList(long userId, int pageNo,
			int pageSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		map.put("n",(pageNo-1)*pageSize);
		map.put("m", pageSize);
		return super.findList(generateStatement("getUserCashCouponList"),map);
	}

	public int getUserCashCouponListCount(long userId) {
		return (Integer)this.selectOne(generateStatement("getUserCashCouponListCount"), userId);
	}

	public CashCouponDto getCouponDto(long cashCouponId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cashCouponId", cashCouponId);
		map.put("curDate",	DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return (CashCouponDto)super.selectOne(generateStatement("getCouponDtoById"), map);
	}

	public int updateAvaliableNum(long cashCouponId) {
		return super.update(generateStatement("updateAvaliableNum"), cashCouponId);
	}

	public List<Map> getShopCouponList(Map param) {
		Integer pageNo= (Integer) param.get("pageNo");
		Integer pageSize=  (Integer) param.get("pageSize");
		param.put("n", (pageNo-1)*pageSize);
		param.put("m", pageSize);
		return (List)this.findList(generateStatement("getShopCouponList"),param);
	}

	public int getShopCouponTotal(Map param) {
		return (Integer)this.selectOne(this.generateStatement("getShopCouponTotal"), param);
	}

	public CashCouponDto getCouponDtoForGrab(long cashCouponId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cashCouponId", cashCouponId);
		map.put("curDate",	DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return (CashCouponDto)super.selectOne(generateStatement("getCouponDtoByIdForGrab"), map);
	}

	public int getUserCashCouponCountBy(Long userId, Integer status, Integer useStatus) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("status", status);
		map.put("useStatus", useStatus);
		return (Integer)this.selectOne(this.generateStatement("getUserCashCouponCountBy"), map);
	}

	/** 
	* @Title: getCashCouponByShopId 
	* @Description: TODO(根据商铺编号获取代金卷) 
	* @param @param shopId
	* @param @return  
	* @throws 
	*/
	public List<CashCouponDto> getCashCouponByShopId(Long shopId) {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("shopId", shopId);
		return this.findList(generateStatement("getCashCouponListByShopId"), paramMap);
	}

	@Override
	public Map<String, Object> getCashCouponAmount(Long userId) {
		return (Map)this.selectOne(this.generateStatement("getCashCouponAmount"), userId);
	}

	
}
