package com.idcq.appserver.dao.bill;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class ShopBillDaoImpl extends BaseDao<ShopBillDto> implements IShopBillDao{

    
	@Override
	public int insertShopBill(ShopBillDto shopBillDto) throws Exception {
        Double money = NumberUtil.formatDouble(shopBillDto.getMoney(),4);
        shopBillDto.setMoney(money);
        if(money == 0) {
            return 0;
        }
	    shopBillDto.setMoney(money);
		return super.insert("insertShopBill", shopBillDto);
	}

	@Override
	public List<Map<String, Object>> getShopBill(Map<String, Object> map) throws Exception {
		return (List)super.findList(generateStatement("getShopBill"),map);
	}

	@Override
	public Integer getShopBillCount(Map<String, Object> map) throws Exception {
		return (Integer)super.selectOne(generateStatement("getShopBillCount"),map);
	}
	
	@Override
	public void updateShopBill(ShopBillDto shopBillDto) throws Exception {
		super.update(generateStatement("updateShopBill"), shopBillDto);		
	}
	
	public void updateShopBillById(ShopBillDto shopBillDto) throws Exception {
		super.update(generateStatement("updateShopBillById"), shopBillDto);		
	}

	@Override
	public void updateShopBillByTransactionId(ShopBillDto shopBillDto)
			throws Exception {
		update(generateStatement("updateShopBillByTransactionId"), shopBillDto);
	}

	@Override
	public List<ShopBillDto> getShopBillByOrderId(String orderId, Integer billStatus)
			throws Exception {
		Map map = new HashMap();
		map.put("orderId", orderId);
		map.put("billStatus", billStatus);
		return (List)super.findList(generateStatement("getShopBillByOrderId"), map);
	}
	
	/**
	 * 获得店铺账单流水和非会员订单的结合
	 * @Title: getShopCombineBill 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<Map<String, Object>> getShopCombineBill(Map<String, Object> map)
			throws Exception {
		
		return (List)super.findList(generateStatement("getShopCombineBill"),map);
	}

	@Override
	public Integer getShopCombineBillCount(Map<String, Object> map)
			throws Exception {
		return (Integer)super.selectOne(generateStatement("getShopCombineBillCount"),map);
	}
	
	/**
	 * 一点管家账单统计
	 * @Title: getIdcqBillStatistics 
	 * @param @param params
	 * @param @return  
	 * @throws
	 */
	public List<Map<String, Object>> getIdcqBillStatistics(
			Map<String, Object> params) {
		return (List)findList(generateStatement("getIdcqBillStatistics"),params);
	}
	
	/**
	 * 获得一点管家统计账单的数量
	 * @Title: getIdcqBillStatisticsCount 
	 * @param @param params
	 * @param @return  
	 * @throws
	 */
	public Integer getIdcqBillStatisticsCount(Map<String, Object> params) {
		return (Integer)super.selectOne(generateStatement("getShopCombineBillCount"),params);
	}
	
	/**
	 * 查询店铺额账单
	 * @Title: queryShopBill 
	 * @param @param shopBillId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public ShopBillDto queryShopBill(Integer shopBillId) throws Exception {
		return (ShopBillDto)super.selectOne(generateStatement("queryShopBill"), shopBillId);
	}
	
	/**
	 * 按参数对账单进行统计
	 * @Title: getIdcqBillStatisticsAmount 
	 * @param @param params
	 * @param @return  
	 * @throws
	 */
	public Double getIdcqBillStatisticsAmount(Map<String, Object> params) {
		return (Double)super.selectOne(generateStatement("getIdcqBillStatisticsAmount"), params);//账单金额统计
	}

    public int insertShopMiddleBill(ShopBillDto shopBillDto) throws Exception
    {
        Double money = NumberUtil.formatDouble(shopBillDto.getMoney(),4);
        shopBillDto.setMoney(money);
        if(money == 0) {
            return 0;
        }
        return super.insert("insertShopMiddleBill", shopBillDto);
    }

    public List<ShopBillDto> getShopBillMiddleByOrderId(String orderId) throws Exception
    {
        return (List)findList(generateStatement("getShopBillMiddleByOrderId"), orderId);
    }

    public void deleteShopBillMiddle(String orderId, List<Long> shopBillIds)
    {
        Map map = new HashMap();
        map.put("orderId", orderId);
        map.put("shopBillIds", shopBillIds);
        super.delete(generateStatement("deleteShopBillMiddle"), map);
    }

	@Override
	public List<Map<String, Object>> getBillStat(Map<String, Object> map)
			throws Exception {
		return (List)findList("getShopBillStat", map);
	}
	
	@Override
	public Integer getBillDetailCount(Map<String, Object> map) throws Exception {
		return (Integer)selectOne("getShopBillDetailCount", map);
	}

	@Override
	public List<Map<String, Object>> getBillDetail(Map<String, Object> map)
			throws Exception {
		return (List)findList("getShopBillDetail", map);
	}

    public double getShopRewardTotalBy(Map<String, Object> map) throws Exception
    {
        return (double) selectOne("getShopRewardTotalBy", map);
    }

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.bill.IShopBillDao#getchargeTotalMoney(java.util.Map)
	 */
	@Override
	public Map<String, Object> getchargeTotalMoney(Long shopId,Integer accountType,Integer billType,Date startTime,Date endTime)
			throws Exception {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("shopId", shopId);
		parms.put("accountType", accountType);
		parms.put("billType", billType);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
        return (Map<String, Object>) selectOne("getchargeTotalMoney", parms);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.bill.IShopBillDao#getShopBillSumMoney(java.lang.String, java.lang.Integer)
	 */
	@Override
	public double getShopBillSumMoney(String orderId, Integer amountType) {
		Map param=new HashMap();
		param.put("orderId", orderId);
		param.put("amountType", amountType);
		return (Double)super.selectOne(generateStatement("getShopBillSumMoney"), param);
	}
	
}
