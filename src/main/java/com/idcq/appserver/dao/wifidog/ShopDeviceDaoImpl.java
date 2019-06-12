package com.idcq.appserver.dao.wifidog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopDeviceDto;
import com.idcq.appserver.utils.jedis.DataCacheApi;
@Repository
public class ShopDeviceDaoImpl extends BaseDao<ShopDeviceDto> implements IShopDeviceDao {

	public Integer queryShopDeviceBySnid(String snId) throws Exception {
		return (Integer)super.selectOne(generateStatement("queryShopDeviceBySnid"),snId) ;
	}

	@Override
	public Integer queryShopDeviceBySnidAndShopId(ShopDeviceDto shopDeviceDto)
			throws Exception {
		return (Integer)super.selectOne(generateStatement("queryShopDeviceBySnidAndShopId"),shopDeviceDto) ;
	}

	public List<Map> queryAppConfigInfo(Map param)  throws Exception{
		return (List)super.findList(generateStatement("queryAppConfigInfo"),param) ;
	}

	public int queryAppInfo(Long appId)  throws Exception{
		return (Integer)super.selectOne(generateStatement("queryAppInfo"), appId);
	}

	public Map queryAppVersion(Long appId) throws Exception{
		return (Map)super.selectOne(generateStatement("queryAppVersion"),appId) ;
	}

	public List<Map> querySmsParam(Long appId) throws Exception {
		return (List)super.findList(generateStatement("querySmsParam"),appId) ;
	}

	public Map<String, Object> getShopInfoBySn(String sn) throws Exception {
		return (Map<String, Object>) super.selectOne(generateStatement("getShopInfoBySn"),sn);
	}
	
	public List<Map<String, Object>> getShopInfoByParam(String mobile, String shopId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("shopId", shopId);
		return (List) super.findList(generateStatement("getShopInfoByParam"), map);
	}
	
	public int updateShopDeviceRegBy(String sn, String regId, String token, String shopId)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sn", sn);
		map.put("regId", regId);
		map.put("token", token);
		map.put("shopId", shopId);
		return super.update(generateStatement("updateShopDeviceRegBy"),map);
	}
	
	public Long getShopIdByUserId(Long userId) throws Exception {
		return (Long)super.selectOne(generateStatement("getShopIdByUserId"), userId);
	}
	
	public int queryShopTokenExists(Map<String, Object> param) {
		Integer num = (Integer)super.selectOne(generateStatement("queryShopTokenExists"), param);
		return num == null ? 0 : num ;
	}

	public Long getUserIdByShopId(Long shopId) throws Exception {
		return (Long)super.selectOne(generateStatement("getUserIdByShopId"), shopId);
	}

	public Long getShopIdByToken(Map<String, Object> param) {
		return (Long) super.selectOne(generateStatement("getShopIdByToken"), param);
	}

	public String getShopRegIdByShopId(Long shopId) throws Exception {
		String shopRegId = null;
		Object obj = DataCacheApi.getObject(CommonConst.KEY_SHOP_DEVICE + shopId);
		if (obj != null) {
			Map<String, String> map = (Map)obj;
			shopRegId = map.get(CommonConst.REG_ID);
			if(shopRegId == null) {
				shopRegId = (String)super.selectOne(generateStatement("getShopRegIdByShopId"), shopId);
			}
		}else{
			shopRegId = (String)super.selectOne(generateStatement("getShopRegIdByShopId"), shopId);
		}
		return shopRegId;
	}
	
	public List<Map> getShopRegIdsByShopId(Long shopId) throws Exception{
		List<Map> shopRegIds = (List)super.findList(generateStatement("getShopRegIdsByShopId"), shopId);
		return shopRegIds;
	}

	public String getCRAddress(String configKey) throws Exception {
		return (String) super.selectOne(generateStatement("getCRAddress"), configKey);
	}

	public void updateShopDeviceRegNull(String regId,String token, String shopId)
			throws Exception {
	    super.update(generateStatement("updateShopDeviceShopIdNull"), shopId);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("regId", regId);
		parameter.put("token", token);
		parameter.put("shopId", shopId);
		super.update(generateStatement("updateShopDeviceRegNull"), parameter);
	}

	public Long getLogoIdByShopId(Long shopId) throws Exception {
		return (Long) super.selectOne(generateStatement("getLogoIdByShopId"), shopId);
	}

	public int insertShopDevice(ShopDeviceDto dto) throws Exception {
		return super.insert(generateStatement("insertShopDevice"), dto);
	}

	public int updateShopDeviceBySnId(ShopDeviceDto dto) throws Exception {
		return super.update(generateStatement("updateShopDeviceBySnId"), dto);
	}

	public int deleteShopDeviceBySnId(ShopDeviceDto dto) throws Exception {
		return super.delete(generateStatement("deleteShopDeviceBySnId"), dto);
	}

	public List<Map> getWifiMacWhitelist(Long shopId) throws Exception {
		if(shopId == null) {
			return null;
		}
		return (List)super.findList(generateStatement("getWifiMacWhitelist"), shopId) ;
	}

	public List<Map> getOwnShopList(Long userId, String shopMode, String authentication)
			throws Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("userId", userId);
		parameter.put("shopMode", shopMode);
		parameter.put("authentication", authentication);
		return (List)super.findList(generateStatement("getOwnShopList"), parameter);
	}
	
	public String getPasswordByShopId(Long shopId) throws Exception {
		return (String) super.selectOne(generateStatement("getPasswordByShopId"), shopId);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.wifidog.IShopDeviceDao#getShopDeviceIsExist(java.util.Map)
     */
    @Override
    public Integer getShopDeviceIsExist(Map<String, Object> pMap) throws Exception
    {
        return (int) super.selectOne(generateStatement("getShopDeviceIsExist"), pMap);

    }
	
}
