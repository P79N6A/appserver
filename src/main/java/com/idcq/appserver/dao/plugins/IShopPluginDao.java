package com.idcq.appserver.dao.plugins;

import java.util.List;

import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.plugin.PluginModel;
import com.idcq.appserver.dto.plugin.ShopPluginDto;

public interface IShopPluginDao {

	public ShopPluginDto getShopPlugin(Integer pluginId, Long shopId) throws Exception;
	public ShopPluginDto getShopPluginById(Integer shopPluginId) throws Exception;
	void insertShopPlugin(ShopPluginDto shopPluginDto) throws Exception;
	void updateShopPlugin(ShopPluginDto shopPluginDto) throws Exception;
	public void updateShopPluginAfterPaySuccess(Integer shopPluginId) throws Exception;
	public List<PluginModel> getPluginUpdateInfoForShop(PluginModel pluginModel, Page page);
	public int getPluginCountForShop(PluginModel pluginModel);
}
