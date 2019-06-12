package com.idcq.appserver.dao.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.XorderGoodsDto;

@Repository
public class XorderGoodsDaoImpl extends BaseDao<XorderGoodsDto> implements IXorderGoodsDao{

	public int addXorderGoodsDto(XorderGoodsDto xorderGoodsDto)
			throws Exception {
		return super.insert(generateStatement("addXorderGoodsDto"),xorderGoodsDto);
	}

	public int updateSettlePrice(String orderId, Double ratio) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("ratio", ratio);
		return super.update(generateStatement("updateSettlePrice"),map);
	}

	public int delGoodsByOrderId(String orderId) throws Exception {
		return super.delete(generateStatement("delGoodsByOrderId"), orderId);
	}

	public XorderGoodsDto getXorderGoodsDto(XorderGoodsDto dto)
			throws Exception {
		return (XorderGoodsDto) super.selectOne(generateStatement("getXorderGoodsDto"), dto);
	}

	
	
}
