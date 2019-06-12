package com.idcq.appserver.dao.cashcoupon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.utils.DateUtils;

@Repository
public class UserCashCouponDaoImpl extends BaseDao<UserCashCouponDto> implements
		IUserCashCouponDao {

	public int obtainCoupon(UserCashCouponDto dto) {
		
		return super.insert(this.generateStatement("obtainUserCoupon"), dto);
	}

	
	public int obtainCoupons(List<UserCashCouponDto> lst) {
		return super.insertBatch(generateStatement("obtainUserCoupons"), lst);
	}

	public int consumeCoupon(Long userId,String orderId,Long uccId,double usePrice) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("orderId", orderId);
		map.put("uccId", uccId);
		map.put("usedPrice", usePrice);
		return super.update(generateStatement("consumeUserCoupon"), map);
	}

	public int deleteCoupon0(UserCashCouponDto dto) {
		return super.delete(generateStatement("deleteUserCoupon0"), dto);
	}


	public UserCashCouponDto getUserCoupon(Long userId, Long cashCouponId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("cashCouponId", cashCouponId);
		return (UserCashCouponDto)super.selectOne(generateStatement("selectUserCoupon"),map );
	}

	public UserCashCouponDto getUserCouponDto(Long userId,Long uccId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("uccId", uccId);
		return (UserCashCouponDto)super.selectOne(generateStatement("getUserCouponDto"),map );
	}


	public void updateUserCashCoupon(Map param) {
		super.update("updateUserCashCoupon",param);
	}
	
	public Integer selectPerDayPerPerson(Long userId){
		Date curDate = new Date();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("curDateBegin", DateUtils.format(curDate, "yyyy-MM-dd 00:00:00"));
		map.put("curDateEnd", DateUtils.format(curDate, "yyyy-MM-dd 23:59:59"));
		return (Integer) super.selectOne(generateStatement("selectPerDayPerPerson"), map);
	}
	
	public Integer selectCountUseNumber(String orderId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderId", orderId);
		return(Integer)super.selectOne(generateStatement("selectCountUseNumber"), map);
	}

	public int getCashUsedNumOfOrder(String orderId, Long ccId,Integer orderPayType)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("ccId", ccId);
		map.put("orderPayType", orderPayType);
		Integer num = (Integer)super.selectOne(generateStatement("getCashUsedNumOfOrder"), map);
		return num == null ? 0 : num ;
	}


	public List<Map> queryUserCashCouponsByUccIds(List<Long> uccIds)
			throws Exception {
		return (List)findList(generateStatement("queryUserCashCouponsByUccIds"), uccIds);
	}
	
	public UserCashCouponDto getUserCashCouponInfo(Long uccId){
		return (UserCashCouponDto) super.selectOne(generateStatement("getUserCashCouponInfo"),uccId);
	}

	public Double getUserCashCouponBalance(Long userId) throws Exception {
		return (Double) super.selectOne(generateStatement("getUserCashCouponBalance"), userId);
	}

	public List<UserCashCouponDto> getUserCashCouponByUserId(Long userId)
			throws Exception {
		return findList(generateStatement("getUserCashCouponByUserId"), userId);
	}

	public Map queryUserCashCouponsByUserId(Long userId) throws Exception {
		return (Map) selectOne(generateStatement("queryUserCashCouponsByUserId"), userId);
	}

}
