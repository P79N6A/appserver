package com.idcq.appserver.dao.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class Transaction3rdDaoImpl extends BaseDao<Transaction3rdDto>implements ITransaction3rdDao{
	
    public Long payBy3rd(Transaction3rdDto transaction3rdDto){
		return (long) super.insert(generateStatement("payBy3rd"),transaction3rdDto);
	}

	public List<Map<String, Object>> getMy3rdPay(Long userId, int status,
			String rdOrgName, int pageNo, int pageSize) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("status", status);
		map.put("rdOrgName", rdOrgName);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m",pageSize);
		return (List)super.findList(generateStatement("getMy3rdPay"), map);
	}

	public int getMy3rdPayCount(Long userId, int status, String rdOrgName,
			int pageNo, int pageSiz) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("status", status);
		map.put("rdOrgName", rdOrgName);
		return (Integer) super.selectOne(generateStatement("getMy3rdPayCount"), map);
	}

	public int nofity3rdPayStatus(Transaction3rdDto transaction3rdDto) {
		return (Integer) super.update(generateStatement("nofity3rdPayStatus"), transaction3rdDto);
	}

	public Long getPayByUserIdOrderId(Transaction3rdDto transaction3rdDto) {
		return (Long) super.selectOne(generateStatement("getPayByUserIdOrderId"), transaction3rdDto);
	}

	public Transaction3rdDto ge3rdPayById(Transaction3rdDto transaction3rdDto) {
		return (Transaction3rdDto) super.selectOne(generateStatement("ge3rdPayById"), transaction3rdDto);
	}

	@Override
	public List<Map<String, Object>> getShopRechargeList(Map<String, Object> map)
			throws Exception {
		return (List)super.findList(generateStatement("getShopRechargeList"),map);
	}

	@Override
	public Integer getShopRechargeCount(Map<String, Object> map)
			throws Exception {
		return (Integer)super.selectOne(generateStatement("getShopRechargeCount"),map);
	}
	
	/**
	 * 添加交易记录
	 * @Title: addTransaction 
	 * @param @param transactionDto  
	 * @throws
	 */
	public Integer addTransaction(Transaction3rdDto transactionDto) {
		return (int)super.insert(generateStatement("addTransaction"), transactionDto);
	}

}
