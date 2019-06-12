package com.idcq.appserver.service.packet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDto;

public interface IPacketService {
	/**
	 * 查询用户红包接口
	 * @param userId
	 * @param pSize
	 * @param pNo
	 * @return
	 * @throws Exception
	 */
	public String queryUserRedPacket(Long userId,int pSize,int pNo)throws Exception;
	
	/**
	 * 用户获取红包接口
	 * @param param
	 */
	public Map obtainRedPacket(Long userId,String batchNo) throws Exception;

	/**
	 * 用户使用红包接口
	 * @param userId 用户编号
	 * @param orderId 订单编号，如果订单支付类型为0 则为单个订单编号；如果订单支付类型为1，则为订单组编号
	 * @param redPacketIds 红包编号 多个用逗号分隔
	 * @param orderPayType 订单支付类型（0-单个订单 1-多个订单）
	 */
	public void payByRedPacket(Long userId, String orderId,String redPacketIds,int orderPayType)throws Exception;
	
	/**
	 * 订单组已经支付的金额
	 * 
	 * @param order
	 * @param orderPayType
	 * @return
	 * @throws Exception
	 */
	BigDecimal queryOrderPayAmount(String orderId,int orderPayType) throws Exception;
	
	/**
	 * 查询单个订单的金额
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	BigDecimal queryOrderAmount(String orderId) throws Exception;

	/**
	 * 批量初始化红包数据
	 * @param datas
	 * @return
	 */
	public int insertRedPacket(List<Map> datas);
	
	/**
	 * 用户校验
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean validUser(Long userId) throws Exception;
	
	/**
	 * 将当前用户查询红包时，存储进redis中的查询结果对应的key值也保存进redis中
	 * @param key 需要存储的key
	 * @param userId 用户编号
	 * @return
	 */
	public boolean setQueryUserRedPacketToRedis(String key,Long userId);
    /**
     * 获取用户红包列表
     * @param parms
     * @return
     * @throws Exception
     */
    PageModel getMemberRedPackets(Map<String, Object> parms) throws Exception;
    
    
    /**
     * 获取用户红包详情
     * 
     * @Function: com.idcq.appserver.service.packet.IPacketService.getRedPacketDetail
     * @Description:
     *
     * @param redPacketId
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月16日 下午4:31:39
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月16日    ChenYongxin      v1.0.0         create
     */
    Map<String, Object> getRedPacketDetail(Long redPacketId) throws Exception;
	
	/**
	 * 清空用户查询红包缓存
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean delQueryUserREdPacketToRedis(Long userId) throws Exception;
	/**
	 * 获取发送红包金额
	 * 
	 * @Function: com.idcq.appserver.service.packet.IPacketService.getRedPacketSendMoney
	 * @Description:
	 *
	 * @param businessAreaActivityId
	 * @param money
	 * @return
	 *
	 */
	Double getRedPacketSendMoney(Long businessAreaActivityId,Double money) throws Exception;
	
	/**
	 * 发送红包接口
	 * @param order 订单对象
	 * @return Double 发送红包金额，
	 * @author  shengzhipeng
	 * @date  2016年3月14日
	 */
	Double sendRedPacketToUser(OrderDto order);
	
	Double getRedPacketAmountBy(Long shopId, Long userId, int status);
	
}
