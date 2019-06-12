package com.idcq.appserver.dao.wifidog;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopDeviceDto;


public interface IShopDeviceDao{
	/**
	 * 根据设备唯一标识查询路由器信息
	 * @param snId
	 * @return
	 */
	Integer queryShopDeviceBySnid(String snId) throws Exception;
	
	/**
	 * 
	 * @param shopDeviceDto
	 * @return
	 * @throws Exception
	 */
	Integer queryShopDeviceBySnidAndShopId(ShopDeviceDto shopDeviceDto) throws Exception;

	/**
	 * 查询配置信息
	 * @param param
	 * @return
	 */
	List<Map> queryAppConfigInfo(Map param) throws Exception;
	
	/**
	 * 查询引用信息
	 * @param appId
	 * @return
	 */
	int queryAppInfo(Long appId) throws Exception;
	
	/**
	 * 查询APP应用版本信息
	 * @param appId
	 * @return
	 */
	Map queryAppVersion(Long appId) throws Exception;
	
	
	/**
	 * 查询手机短信下发参数模板
	 * @param appId
	 * @return
	 * @throws Exception
	 */
	List<Map> querySmsParam(Long appId) throws Exception;
	
	/**
	 * 根据设备序列号查询商铺id和商铺模式
	 * @param sn
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getShopInfoBySn(String sn) throws Exception;
	
	/**
	 * 根据用户手机号码查询商铺信息
	 * @param mobile
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getShopInfoByParam(String mobile, String shopId) throws Exception;
	
	/**
	 * 更新商铺设备表的token和regId
	 * @param sn
	 * @param regId
	 * @param token
	 * @throws Exception
	 */
	int updateShopDeviceRegBy(String sn, String regId, String token, String shopId)throws Exception;
	
	/**
	 * 更新非登录状态的regId和token为空
	 * @param regId
	 * @param token
	 * @param shopId
	 * @throws Exception
	 */
	void updateShopDeviceRegNull(String regId, String token, String shopId)throws Exception;
	
	/**
	 * 根据用户id查询商铺id
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Long getShopIdByUserId(Long userId)throws Exception;
	
	/**
	 * 查询商铺token是否正确
	 * @param param
	 * @return
	 */
	int queryShopTokenExists(Map<String, Object> param);
	
	/**
	 * 根据token获取shopId
	 * @param param
	 * @return
	 */
	Long getShopIdByToken(Map<String, Object> param);
	
	/**
	 * 根据shopId查询用户id
	 * @return
	 * @throws Exception
	 */
	Long getUserIdByShopId(Long shopId)throws Exception;
	
	/**
	 * 获取商铺注册码
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	String getShopRegIdByShopId(Long shopId) throws Exception;
	
	/**
	 * 获取商铺注册码列表
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopRegIdsByShopId(Long shopId) throws Exception;

	/**
	 * 获取收银机API地址
	 * @param configKey 查询key，固定值
	 * @return
	 * @throws Exception
	 */
	String getCRAddress(String configKey) throws Exception;
	
	/**
	 * 查询shop对应的logoId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Long getLogoIdByShopId(Long shopId) throws Exception;
	
	/**
	 * 新增设备信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int insertShopDevice(ShopDeviceDto dto) throws Exception;
	
	/**
	 * 更新设备信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int updateShopDeviceBySnId(ShopDeviceDto dto) throws Exception;
	
	/**
	 * 干掉设备信息
	 * @param dto 设备信息
	 * @return
	 * @throws Exception
	 */
	int deleteShopDeviceBySnId(ShopDeviceDto dto) throws Exception;
	
	List<Map> getWifiMacWhitelist(Long shopId) throws Exception;
	
	/**
	 * 获取商铺管理者店铺列表
	 * @param userId
	 * @param shopMode
	 * @return
	 * @throws Exception
	 */
	List<Map> getOwnShopList(Long userId, String shopMode, String authentication) throws Exception;
	
	/**
	 * 根据店铺id获取店铺管理者密码
	 * 
	 * @Function: com.idcq.appserver.dao.wifidog.IShopDeviceDao.getPasswordByShopId
	 * @Description:
	 *
	 * @param shopId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月20日 上午9:26:51
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月20日    shengzhipeng       v1.0.0         create
	 */
	String getPasswordByShopId(Long shopId) throws Exception;
	
    /**
     * 查询店铺收银机是否存在
     * @param userId
     * @param shopMode
     * @return
     * @throws Exception
     */
	Integer getShopDeviceIsExist(Map<String, Object> pMap) throws Exception;
	
}
