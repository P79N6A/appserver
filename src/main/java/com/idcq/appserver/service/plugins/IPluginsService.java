package com.idcq.appserver.service.plugins;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.plugin.PluginModel;
import com.idcq.appserver.dto.plugin.ShopPluginDto;

public interface IPluginsService {
    /**
     * 获取商铺插件列表接口
     * @Function: com.idcq.appserver.service.plugins.IPluginsService.getShopPlugins
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月1日 下午1:45:51
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月1日    ChenYongxin      v1.0.0         create
     */
    public PageModel getShopPlugins(Map<String, Object> pMap) throws Exception;
    /**
     * 获取插件列表接口
     * 
     * @Function: com.idcq.appserver.service.plugins.IPluginsService.getPlugins
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月1日 下午1:45:33
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月1日    ChenYongxin      v1.0.0         create
     */
    public PageModel getPlugins(Map<String, Object> pMap) throws Exception;

	public Map<String, Object> buyShopPlugin(Map<String, Object> requestMap) throws Exception;
	public Boolean queryPluginIsExist(Integer pluginId) throws Exception;
	public ShopPluginDto getShopPluginById(Integer shopPluginId) throws Exception;
	public void insertPlatformBillForShopPlugin(TransactionDto transaction,Integer moneySource,String billType) throws Exception;
	/**
	 * 获得具体程序启动时更新插件所需要的信息
	 * @param pluginModel
	 * @param page
	 * @return
	 */
	PageModel getPluginUpdateInfoForShop(PluginModel pluginModel, Page page);

	/**
	 * 获取插件详情
	 * @param searchParam 查询的参数，包括pluginId, versionNum
	 * @return
     */
	Map<String, Object> getPluginDetail(Map<String, Object> searchParam);

	/**
	 * 记录插件下载情况
	 * @param shopId
	 * @param pluginId
	 * @param attachmentId 根据该字段查出下载插件的版本号version
     * @param sn
     */
	void recordPluginDownload(Integer shopId, Integer pluginId, Integer attachmentId, String sn);
}
