package com.idcq.appserver.dao.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.XorderDto;

@Repository
public class XorderDaoImpl extends BaseDao<XorderDto> implements IXorderDao{

	public int addXorderDto(XorderDto xorderDto) throws Exception {
		return super.insert(generateStatement("addXorderDto"),xorderDto);
	}

	public int queryXorderExists(String orderId) throws Exception {
		Integer num = (Integer)super.selectOne(generateStatement("queryXorderExists"), orderId);
		return num == null ? 0 : num;
	}

	public int updateXorderDto(XorderDto xorderDto) throws Exception {
		return super.update(generateStatement("updateXorderDto"),xorderDto);
	}

	public Integer getOrderStatusById(String orderId) throws Exception {
		return (Integer)super.selectOne(generateStatement("getOrderStatusById"), orderId);
	}

	
	public XorderDto getXOrderById(String orderId) throws Exception {
		return (XorderDto)super.selectOne(generateStatement("getXOrderById"), orderId);
	}

	public List<Map> getShopOfflineOrders(Long shopId, Integer day) throws Exception {
		Map map = new HashMap();
		map.put("shopId", shopId);
		map.put("day", day);
		return super.getSqlSession().selectList("getShopOfflineOrders", map);
	}

	public int canclXorder(String orderId) throws Exception {
		return super.update("canclXorder",orderId);
	}

	@Override
	public XorderDto getXorderSimple(String orderId) throws Exception {
		return (XorderDto)super.selectOne("getXorderSimple", orderId);
	}

	@Override
	public int delXorderDto(String orderId) throws Exception {
		return super.delete("delXorderDto", orderId);
	}

	@Override
	public String getGoodsLogoByXorderId(String xOrderId) throws Exception {
		return (String) super.selectOne(generateStatement("getGoodsLogoByXorderId"), xOrderId);
	}

	public BigDecimal getSettingPriceByxorderId(String xorderId)
			throws Exception {
		return (BigDecimal) selectOne(generateStatement("getSettingPriceByxorderId"), xorderId);
	}
	
	
	
}
