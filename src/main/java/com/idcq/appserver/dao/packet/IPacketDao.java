package com.idcq.appserver.dao.packet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.packet.RedPacket;

public interface IPacketDao {
	
	/**
	 * 查询订单实际需要支付金额
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public BigDecimal queryOrderAmount(String orderId)throws Exception;
	
	/**
	 * 查询非会员订单实际需要支付金额
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public BigDecimal queryXorderAmount(String orderId)throws Exception;
	
	/**
	 * 查询订单已经实际支付金额
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public BigDecimal queryOrderPayAmount(String orderId,int orderPayType) throws Exception;
	
	/**
	 * 查询非会员订单已支付金额
	 * @param xOrderId
	 * @param orderPayType
	 * @return
	 * @throws Exception
	 */
	public BigDecimal queryXorderPayAmount(String xOrderId,int orderPayType) throws Exception;
	
	
	/**
	 * 用户获取红包接口
	 * @param param
	 */
	public void obtainRedPacket(Map param);

	/**
	 * 查询红包信息
	 * @param redPacketId
	 * @return
	 */
	public RedPacket queryRedPacketById(Long redPacketId);
	
	/**
	 * 查询红包信息
	 * @param redPacketIds 红包编号集合
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryRedPacketByIds(List<Long> redPacketIds) throws Exception;

	/**
	 * 保存用户使用红包信息
	 * @param param
	 */
	public void insertOrderPay(Map param);

	/**
	 * 查询用户是否存在
	 * @param userId
	 * @return
	 */
	public Integer queryUserInfo(Long userId);

	/**
	 * 修改红包使用标记<br><b>0-未使用 1-已使用</b>
	 * @param param
	 */
	public void updateRedPacketFlag(Map param);

	/**
	 * 订单存在检测
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public Map<String,Object> queryOrderIsExists(Long userId, String orderId);

	/**
	 * 更新订单支付状态(支付状态：未支付-0,已支付-1,支付失败-2)
	 * @param param
	 * @return
	 */
	public int updateOrderStatus(Map param);
	
	/**
	 * 更改订单状态
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public int updateOrderStatusByOrderId(String orderId) throws Exception;
	
	/**
	 * 根据红包状态和用户id查询红包的个数
	 * @param userId
	 * @param status
	 * @return
	 */
	Integer getPacketCountBy(Long userId, Integer status);

	/**
	 * 金额分组，查询用户红包对应的商铺
	 * @param param
	 * @return
	 */
	public List<Map> queryShopRedPacketsByGroup(Map param);
	
	/**
	 * 金额分组，查询用户红包对应的商铺总数
	 * @param param
	 * @return
	 */
	public int queryShopRedPacketsByGroupCount(Map param);

	/**
	 * 根据红包金额、发行商铺、用户编号、使用状态
	 * @param param
	 * @return
	 */
	public List<Map> queryRedPackets(Map param);
 
	/**
	 * 根据红包状态和用户id查询红包的总额
	 * @param userId
	 * @param status
	 * @return
	 */
	Double getPacketAmountBy(Long userId, Integer status);
	
	/**
	 * 查询红包支持的商铺
	 * @param redPacketId
	 * @return
	 * @throws Exception
	 */
	public List<Integer> queryPacketApplyShopByPacketId(Long redPacketId) throws Exception;
	
	/**
	 * 查询订单组中所有订单对应的商品及商铺
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryShopByOrderGroupId(String groupId) throws Exception;
	
	/**
	 * 根据红包批次号查询可以领取的红包，如果数据存在，则随机返回一条数据的红包编号
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	public Map queryRedPacketIdByBatchNo(String batchNo)throws Exception;
	
	/**
	 * 查询商铺红包
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryShopRedPackets(Map param)throws Exception;
	
	/**
	 * 查询商铺红包总数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer queryShopRedPacketsCount(Map param)throws Exception;

	
	/**
	 * 批量插入红包信息
	 * @param datas
	 * @return
	 */
	public int insertRedPacket(List<Map> datas);
	
	/**
	 * 新增红包信息
	 * @param shopId 店铺ID
	 * @param userId 用户ID
	 * @param status 状态 1=可用,2=已转赠,3=已使用完,4=过期
	 * @author  shengzhipeng
	 * @date  2016年3月15日
	 */
	int addRedPacket(RedPacket redPacket);
	
	Double getRedPacketAmountBy(Long shopId, Long userId, Integer status);
	
	List<RedPacket> getRedPacketBy(Long shopId, Long userId, Integer status);
	
	RedPacket getRedPacketByOrderId(String orderId);
	List<RedPacket> getRedPacketByIds(Map<String, Object> queryMap) throws Exception;
}
