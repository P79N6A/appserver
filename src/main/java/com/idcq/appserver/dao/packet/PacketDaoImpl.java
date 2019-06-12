package com.idcq.appserver.dao.packet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.packet.RedPacket;

@Repository
public class PacketDaoImpl extends BaseDao<RedPacket> implements IPacketDao {

	public void obtainRedPacket(Map param) {
		this.update(generateStatement("obtainRedPacket"), param);
	}

	public RedPacket queryRedPacketById(Long redPacketId) {
		return (RedPacket) this.selectOne(generateStatement("queryRedPacketById"),
				redPacketId);
	}

	public List<Map> queryRedPacketByIds(List<Long> redPacketIds) throws Exception {
		return (List) this.findList(generateStatement("queryRedPacketByIds"), redPacketIds);
	}

	public List<RedPacket> getRedPacketByIds(Map<String, Object> queryMap) throws Exception {
		return this.findList(generateStatement("getRedPacketByIds"), queryMap);
	}
	public void insertOrderPay(Map param) {
		this.insert("insertOrderPay", param);
	}

	public Integer queryUserInfo(Long userId) {
		return (Integer) this.selectOne(generateStatement("queryUserInfo"),
				userId);
	}

	public void updateRedPacketFlag(Map param) {
		this.update(generateStatement("updateRedPacketFlag"), param);
	}

	public Map<String,Object> queryOrderIsExists(Long userId, String orderId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("orderId", orderId);
		return (Map) this.selectOne(generateStatement("queryOrderIsExists"), param);
	}

	public BigDecimal queryOrderAmount(String orderId) throws Exception {
		return (BigDecimal) this.selectOne(
				generateStatement("queryOrderAmount"), orderId);
	}

	public BigDecimal queryOrderPayAmount(String orderId, int orderPayType)
			throws Exception {
		Map param = new HashMap();
		param.put("orderId", orderId);
		param.put("orderPayType", orderPayType);
		return (BigDecimal) this.selectOne(
				generateStatement("queryOrderPayAmount"), param);
	}

	public int updateOrderStatus(Map param) {
		return (Integer) super.update(generateStatement("updateOrderStatus"),
				param);
	}

	public Integer getPacketCountBy(Long userId, Integer status) {
		Map<String, Object> param = setParam(userId, status);
		return (Integer) this.selectOne(generateStatement("getPacketCountBy"),
				param);
	}

	public Double getPacketAmountBy(Long userId, Integer status) {
		Map<String, Object> param = setParam(userId, status);
		return (Double) this.selectOne(generateStatement("getPacketAmountBy"),
				param);
	}

	/**
	 * 设置参数
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	private Map<String, Object> setParam(Long userId, Integer status) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("status", status);
		return param;
	}

	public List<Map> queryShopRedPacketsByGroup(Map param) {
		return (List) this.findList(
				generateStatement("queryShopRedPacketsByGroup"), param);
	}

	public int queryShopRedPacketsByGroupCount(Map param) {
		return (Integer) this.selectOne(
				generateStatement("queryShopRedPacketsByGroupCount"), param);
	}

	public List<Map> queryRedPackets(Map param) {
		return (List) this
				.findList(generateStatement("queryRedPackets"), param);
	}

	public List<Integer> queryPacketApplyShopByPacketId(Long redPacketId)
			throws Exception {
		return (List) this.findList(
				generateStatement("queryPacketApplyShopByPacketId"),
				redPacketId);
	}

	public List<Map> queryShopByOrderGroupId(String groupId) throws Exception {
		return (List) this.findList(
				generateStatement("queryShopByOrderGroupId"), groupId);
	}

	public int updateOrderStatusByOrderId(String orderId) throws Exception {
		return (Integer) super.update(
				generateStatement("updateOrderStatusByOrderId"), orderId);
	}

	public Map queryRedPacketIdByBatchNo(String batchNo) throws Exception {
		return (Map) super.selectOne(
				generateStatement("queryRedPacketIdByBatchNo"), batchNo);
	}

	public List<Map> queryShopRedPackets(Map param) throws Exception {
		return (List) this.findList(generateStatement("queryShopRedPackets"),
				param);
	}

	public Integer queryShopRedPacketsCount(Map param) throws Exception {
		return (Integer) this.selectOne(
				generateStatement("queryShopRedPacketsCount"), param);
	}

	public int insertRedPacket(List<Map> datas) {
		return this.insert(generateStatement("insertRedPacket"), datas);
	}

	public BigDecimal queryXorderPayAmount(String xOrderId, int orderPayType)
			throws Exception {
		Map param = new HashMap();
		param.put("orderId", xOrderId);
		param.put("orderPayType", orderPayType);
		return (BigDecimal) this.selectOne(generateStatement("queryXorderPayAmount"), param);
	}

	public BigDecimal queryXorderAmount(String orderId) throws Exception {
		return (BigDecimal) this.selectOne(
				generateStatement("queryXorderAmount"), orderId);
	}

    @Override
    public int addRedPacket(RedPacket redPacket) {
        return this.insert(generateStatement("addRedPacket"), redPacket);
    }

    @Override
    public Double getRedPacketAmountBy(Long shopId, Long userId, Integer status) {
        Map param = new HashMap();
        param.put("shopId", shopId);
        param.put("userId", userId);
        param.put("status", status);
        return (Double) this.selectOne(generateStatement("getRedPacketAmountBy"), param);
    }

    @Override
    public List<RedPacket> getRedPacketBy(Long shopId, Long userId, Integer status) {
        Map param = new HashMap();
        param.put("shopId", shopId);
        param.put("userId", userId);
        param.put("status", status);
        return this.findList(generateStatement("getRedPacketBy"), param);
    }

    @Override
    public RedPacket getRedPacketByOrderId(String orderId) {
        return (RedPacket) this.selectOne(generateStatement("getRedPacketByOrderId"), orderId);
    }
}
