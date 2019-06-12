package com.idcq.appserver.dao.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.plugin.PluginModel;
import com.idcq.appserver.dto.plugin.ShopPluginDto;

@Repository
public class ShopPluginDaoImpl extends BaseDao<ShopPluginDto> implements IShopPluginDao {

	@Override
	public ShopPluginDto getShopPlugin(Integer pluginId, Long shopId) throws Exception {
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("pluginId", pluginId);
		pMap.put("shopId", shopId);
		List<ShopPluginDto> shopPluginList = (List<ShopPluginDto>) super.findList(generateStatement("getShopPluginInDeclaredDate"), pMap);
		return shopPluginList.get(0);
		
	}
	@Override
	public void insertShopPlugin(ShopPluginDto shopPluginDto) throws Exception {
		super.insert(generateStatement("insertShopPlugin"), shopPluginDto);
	}
	
	@Override
	public void updateShopPlugin(ShopPluginDto shopPluginDto) throws Exception {
		super.update(generateStatement("updateShopPlugin"), shopPluginDto);
	}
	
	@Override
	public ShopPluginDto getShopPluginById(Integer shopPluginId) throws Exception {
		return (ShopPluginDto) super.selectOne(generateStatement("getShopPluginById"), shopPluginId);
	}
	
	@Override
	public void updateShopPluginAfterPaySuccess(Integer shopPluginId) throws Exception {
		ShopPluginDto updateModel = new ShopPluginDto();
		updateModel.setShopPluginId(shopPluginId);
		updateModel.setIsPaid(1);
		updateModel.setIsActive(1);
		updateModel.setIsSettled(1);
		updateShopPlugin(updateModel);
	}
	
	
    @Override
    public List<PluginModel> getPluginUpdateInfoForShop(PluginModel pluginModel, Page page)
    {   
        /* 设置基础查询条件  */
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("shopId", pluginModel.getShopId());
        searchMap.put("baseSystem", pluginModel.getBaseSystem());
        searchMap.put("functionType", pluginModel.getFunctionType());
        searchMap.put("pointId", pluginModel.getPointId());
        searchMap.put("pluginId", pluginModel.getPluginId());
        searchMap.put("startRows", page.getStartRows());
        searchMap.put("pSize", page.getpSize());
        List<Object> tempResult = (List<Object>) this.getSqlSession().selectList(
                this.generateStatement("getShopPluginUpdateInfo"), searchMap);
        List<PluginModel> result = new ArrayList<PluginModel>();
        if (tempResult != null)
        {
            for (Object temp : tempResult)
            {
                result.add((PluginModel) temp);
            }
        }
        return result;
    }
    @Override
    public int getPluginCountForShop(PluginModel pluginModel)
    {
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("shopId", pluginModel.getShopId());
        searchMap.put("baseSystem", pluginModel.getBaseSystem());
        searchMap.put("functionType", pluginModel.getFunctionType());
        searchMap.put("pointId", pluginModel.getPointId());
        searchMap.put("pluginId", pluginModel.getPluginId());
        return (int)super.selectOne(this.generateStatement("getPluginCountForShop"), searchMap);
    }

}
