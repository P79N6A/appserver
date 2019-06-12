package com.idcq.appserver.service.pay;

import java.util.List;

import com.idcq.appserver.dto.packet.RedPacketPayInfo;
import com.idcq.appserver.dto.pay.GroupPayModel;
import com.idcq.appserver.dto.plugin.PluginDto;
import com.idcq.appserver.dto.plugin.ShopPluginDto;

public interface IChuanQiPayService {

	void payShopPlugin(PluginDto pluginDto,ShopPluginDto shopPluginDto) throws Exception;
	void groupPay(GroupPayModel groupPayModel) throws Exception;
	void payOrderByChuanQiPay(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception;
	void payOrderByConsumCard(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception;
	void payOrderByRedPacket(Long userId, String orderId, double payAmount,Long redPacketId,Integer clientSystem, Integer autoSettleFlag) throws Exception;
	void checkRedPacketValid(Long shopId, Long userId,double payAmount,Long redPacketId) throws Exception;
	void payOrderByVoucher(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception;
	List<RedPacketPayInfo> getRedPacketPayInfoByPayAmount(Long shopId, Long userId,double payAmount) throws Exception;
	List<RedPacketPayInfo> getRedPacketPayInfoByIds(List<Long> redPacketIds,double payAmount) throws Exception;
}
