package com.idcq.appserver.dao.pay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.XorderPayDto;

@Repository
public class XorderPayDaoImpl extends BaseDao<XorderPayDto> implements IXorderPayDao{

	public int addXorderPayDto(XorderPayDto xOrderPay) throws Exception {
		return super.insert("addXorderPayDto",xOrderPay);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.pay.IXorderPayDao#getAmountByXOrderIdAndPayeeType(java.util.Map)
	 */
	@Override
	public Map<String, Object> getAmountByXOrderIdAndPayeeType(
			Map<String, Object> map) throws Exception {
		return (Map)super.selectOne("getAmountByXOrderIdAndPayeeType", map);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.pay.IXorderPayDao#getAmountByXorderIdAndPayType(java.util.Map)
	 */
	@Override
	public Map<String, Object> getAmountByXorderIdAndPayType(
			Map<String, Object> map) throws Exception {
		return (Map)super.selectOne("getAmountByXorderIdAndPayType", map);
	}
	
	/**
	 * 非会员订单支付记录
	 * @Title: queryXorderPayList 
	 * @param @param xorderId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<XorderPayDto> queryXorderPayList(String xorderId) throws Exception {
		return findList(generateStatement("queryXorderPayList"),xorderId);
	}

	public List<Map<String, Object>> getPayLogByXorderId(String xorderId)
			throws Exception {
		return (List) findList(generateStatement("getPayLogByXorderId"), xorderId);
	}

	public Integer getMaxPayIndex(String xorderId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getMaxPayIndex"), xorderId);
	}

	public List<XorderPayDto> getAllPayLog(String xorderId) throws Exception {
		return super.findList(generateStatement("getAllPayLog"), xorderId);
	}

	public int delXorderPayDtoByOrderId(String xorderId) throws Exception {
		return super.insert("delXorderPayDtoByOrderId",xorderId);
	}

	public BigDecimal getTotalPayAmountByXorderId(String xorderId)
			throws Exception {
		return (BigDecimal) selectOne(generateStatement("getTotalPayAmountByXorderId"), xorderId);
	}

	public List<Map> getXorderPayDetail(List<String> xorderIds)
			throws Exception {
		return (List)super.findList(generateStatement("getXorderPayDetail"), xorderIds);
	}

	@Override
	public List<Map> getXorderPayList(String xorderId) throws Exception {
		return (List)super.findList(generateStatement("getXorderPayList"),xorderId );
	}
	
}
