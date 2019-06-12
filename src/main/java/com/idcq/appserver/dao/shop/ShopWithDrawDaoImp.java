package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
/**
 * 商铺提现
* @ClassName: ShopWithDrawDaoImp 
 */
@Repository
public class ShopWithDrawDaoImp extends BaseDao<ShopWithDrawDto> implements IShopWithDrawDao{

	@Override
	public List<Map<String, Object>> getShopWithdrawList(Map<String, Object> map) throws Exception {
		return (List)super.findList(generateStatement("getShopWithdrawList"),map);
	}

	@Override
	public Integer getShopWithdrawCount(Map<String, Object> map) throws Exception {
		return (Integer) super.selectOne(generateStatement("getShopWithdrawCount"),map);
	}

	@Override
	public Integer shopWithdraw(ShopWithDrawDto shopWithDrawDto)
			throws Exception {
		return super.insert(generateStatement("shopWithdraw"),shopWithDrawDto);
	}

	@Override
	public ShopWithDrawDto getShopWithDrawById(Long id) throws Exception {
		return (ShopWithDrawDto)selectOne(generateStatement("getShopWithDrawById"), id);
	}

	@Override
	public Integer updateShopWithdraw(ShopWithDrawDto shopWithDrawDto)
			throws Exception {
		return (Integer)update(generateStatement("updateShopWithdraw"), shopWithDrawDto);
		
	}

	@Override
	public Map<String, Object> getWithdrawTotalMoney(Long shopId,
			Integer withdrawStatus, Date startTime, Date endTime)
			throws Exception {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("shopId", shopId);
		parms.put("withdrawStatus", withdrawStatus);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
		
		return (Map<String, Object>)selectOne(generateStatement("getWithdrawTotalMoney"), parms);

	}

	@Override
	public Map<String, Object> getStandardMoney(Long shopId) throws Exception {
		
		
		return (Map<String, Object>)selectOne(generateStatement("getStandardMoney"), shopId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopWithDrawDao#getWithdrawCommissionTotal(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public Map<String, Object> getWithdrawCommissionTotal(Long shopId,
			Date startTime, Date endTime) throws Exception {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("shopId", shopId);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
		return (Map<String, Object>)selectOne(generateStatement("getWithdrawCommissionTotal"), parms);
	}

	

}
