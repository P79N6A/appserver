package com.idcq.appserver.dao.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.WithdrawDto;
/**
 * 提现dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class WithdrawDaoImpl extends BaseDao<WithdrawDto>implements IWithdrawDao{

	public Integer withdraw(WithdrawDto withdrawDto) throws Exception {
		return super.insert(generateStatement("withdraw"),withdrawDto);
	}

	public List<Map<String, Object>> getWithdrawList(Map<String, Object> map,
			int pageNo, int pageSize) throws Exception {
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return (List)super.findList(this.generateStatement("getWithdrawList"), map);
	}
	
	public List<Map<String, Object>> getShopWithdrawList(Map<String, Object> map,
			int pageNo, int pageSize) throws Exception {
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return (List)super.findList(this.generateStatement("getShopWithdrawList"), map);
	}

	public int getWithdrawListCount(Map<String, Object> map) throws Exception {
		return (Integer)this.selectOne(this.generateStatement("getWithdrawListCount"), map);
	}
	
	public int getShopWithdrawListCount(Map<String, Object> map) throws Exception {
		return (Integer)this.selectOne(this.generateStatement("getShopWithdrawListCount"), map);
	}

	@Override
	public Map<String, Object> getWithdrawInfoById(Long withdrawId) throws Exception {
		return ( Map<String, Object>)this.selectOne(this.generateStatement("getWithdrawInfoById"), withdrawId);
	}

	@Override
	public List<Map<String, Object>> getServiceWithdrawList(
			Integer withdrawStatus, Long userId, String mobile, int pageNo,
			int pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("withdrawStatus", withdrawStatus);
		map.put("userId", userId);
		map.put("mobile", mobile);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return (List)super.findList(this.generateStatement("getServiceWithdrawList"), map);
	}

	@Override
	public Integer getServiceWithdrawCount(Integer withdrawStatus, Long userId,
			String mobile) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("withdrawStatus", withdrawStatus);
		map.put("userId", userId);
		map.put("mobile", mobile);
		return (Integer)this.selectOne(this.generateStatement("getServiceWithdrawCount"), map);
	}

	@Override
	public WithdrawDto getWithdrawById(Long withdrawId) throws Exception {
		return (WithdrawDto)selectOne(generateStatement("getWithdrawById"), withdrawId);
	}

	@Override
	public void updateWithdraw(WithdrawDto withdrawDto) throws Exception {
		update(generateStatement("updateWithdraw"), withdrawDto);
	}
	
	/**
	 * 查询提现列表
	 * @Title: getWithDrawListByIdList 
	 * @param @param withDrawList
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<WithdrawDto> getWithDrawListByIdList(List<String> withDrawList)
			throws Exception {
		Map<String,Object>paramList=new HashMap<String,Object>();
		paramList.put("withDrawList", withDrawList);
		return findList(generateStatement("getWithDrawListByIdList"),paramList);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.pay.IWithdrawDao#getStandardWithdrawMoney(java.util.Map)
	 */
	@Override
	public Map<String, Object> getStandardWithdrawMoney(Long userId)
			throws Exception {
		
		return (Map<String, Object>) selectOne(generateStatement("getStandardWithdrawMoney"), userId);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.pay.IWithdrawDao#getUserWithdrawTotalMoney(java.util.Map)
	 */
	@Override
	public Map<String, Object> getUserWithdrawTotalMoney(Long userId,
			Integer withdrawStatus, Date startTime, Date endTime)
			throws Exception {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("userId", userId);
		parms.put("withdrawStatus", withdrawStatus);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
		
		
		return (Map<String, Object>) selectOne(generateStatement("getUserWithdrawTotalMoney"), parms);

	}

}
