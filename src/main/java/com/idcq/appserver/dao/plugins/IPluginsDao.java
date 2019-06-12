package com.idcq.appserver.dao.plugins;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.plugin.PluginDto;

public interface IPluginsDao {
	/**
	 * 获取平台插件列表接口
	 * 
	 * @Function: com.idcq.appserver.dao.plugins.IPluginsDao.getPlugins
	 * @Description:
	 *
	 * @param pMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月1日 下午1:41:26
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月1日    ChenYongxin      v1.0.0         create
	 */
    public List<Map<String, Object>> getShopPlugins(Map<String, Object> pMap) throws Exception;
    /**
     * 获取平台插件列表总数接口
     * 
     * @Function: com.idcq.appserver.dao.plugins.IPluginsDao.getPlugins
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月1日 下午1:41:26
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月1日    ChenYongxin      v1.0.0         create
     */
    public Integer getShopPluginsCount(Map<String, Object> pMap) throws Exception;
    /**
     * 获取商铺插件列表接口
     * 
     * @Function: com.idcq.appserver.dao.plugins.IPluginsDao.getShopPlugins
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月1日 下午1:41:32
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月1日    ChenYongxin      v1.0.0         create
     */
    public List<Map<String, Object>> getPlugins(Map<String, Object> pMap) throws Exception;
    /**
     * 获取商铺插件列表接口
     * 
     * @Function: com.idcq.appserver.dao.plugins.IPluginsDao.getShopPlugins
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月1日 下午1:41:32
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月1日    ChenYongxin      v1.0.0         create
     */
    public Integer getPluginsCount(Map<String, Object> pMap) throws Exception;


    public Boolean queryPluginIsExist(Integer pluginId) throws Exception;
    
    public PluginDto getPluginById(Integer pluginId) throws Exception;

	Map<String, Object> getPluginDetail(Map<String, Object> params);

	/**
	 * 获取插件版本情况，可以根据attachmentId或<pluginId与version_num>获取相关版本信息
	 * @param queryParams
	 * @return
     */
	Map<String, Object> getPluginVersionInfo(Map<String, Object> queryParams);

	/**
	 * 向1dcq_shop_plugin中插入下载记录
	 * @param params
     */
	void insertPluginUseRecord(Map<String, Object> params);

	void incrementDownloadCount(Integer pluginId);
}
