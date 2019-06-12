package com.idcq.appserver.service.message;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.user.PushUserTableDto;

public interface IPushService {
	
	int pushInfoToUser(PushDto push,Integer userType) throws Exception;
	
	int pushInfoToShop(PushDto push) throws Exception;
	
	/**
	 * 向用户推送消息
	 * 
	 * @param push
	 * @return
	 * @throws Exception
	 */
	int pushInfoToUser2(PushDto push ,Integer userType) throws Exception ;
	
	/**
	 * 向商家推送消息
	 * 
	 * @param push
	 * @return
	 * @throws Exception
	 */
	int pushInfoToShop2(PushDto push) throws Exception ;
	
	/**
	 * 向指定商铺/会员推送消息
	 * @param osInfo
	 * @param userType
	 * @param registrationId
	 * @param title
	 * @param content
	 * @param action
	 * @return
	 * @throws Exception
	 */
	int pushInfo(String osInfo, Integer userType, String registrationId,String title,String content,String action) throws Exception;
	
	/**
	 * 根据商铺编号获取商铺设备regid集合
	 * @return
	 * @throws Exception
	 */
	List<Map> getRegIdsByShopId(Long shopId) throws Exception;
	
	/**
	 * 获取用户设备列表
	 * @param userId userType=0时为用户ID，userType=10时为商铺ID
	 * @param userType 用户类型，会员-0,店铺管理者-10
	 * @return
	 * @throws Exception
	 */
	List<PushUserTableDto> getRegIdsByUserId(Long userId,int userType) throws Exception;
}
