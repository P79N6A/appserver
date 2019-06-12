package com.idcq.appserver.dao.plugins;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.plugin.PluginDto;

@Repository
public class PluginsDaoImpl extends BaseDao<PluginDto> implements IPluginsDao {

    public List<Map<String, Object>> getPlugins(Map<String, Object> pMap)throws Exception
    {
        return (List)super.findList(this.generateStatement("getPlugins"), pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.plugins.IPluginsDao#getShopPlugins(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> getShopPlugins(Map<String, Object> pMap) throws Exception
    {
        return (List)super.findList(this.generateStatement("getShopPlugins"), pMap);
    }


    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.plugins.IPluginsDao#getPluginsCount(java.util.Map)
     */
    @Override
    public Integer getPluginsCount(Map<String, Object> pMap) throws Exception
    {
        return (Integer)super.selectOne(this.generateStatement("getPluginsCount"), pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.plugins.IPluginsDao#getShopPluginsCount(java.util.Map)
     */
    @Override
    public Integer getShopPluginsCount(Map<String, Object> pMap) throws Exception
    {
        return (Integer)super.selectOne(this.generateStatement("getShopPluginsCount"), pMap);
    }

	@Override
    public Boolean queryPluginIsExist(Integer pluginId) throws Exception {
    	Integer count = (Integer) super.selectOne(generateStatement("queryPluginIsExist"), pluginId);
    	return count == 0 ? false : true;
    }
    
	@Override
    public PluginDto getPluginById(Integer pluginId) throws Exception {
		return (PluginDto) super.selectOne(generateStatement("getPluginByPluginId"), pluginId);
    }

    @Override
    public Map<String, Object> getPluginDetail(Map<String, Object> params) {
        return (Map<String, Object>) super.selectOne(generateStatement("getPluginDetail"), params);
    }

    @Override
    public Map<String, Object> getPluginVersionInfo(Map<String, Object> queryParams) {
        return (Map<String, Object>) super.selectOne(generateStatement("getPluginVersionInfo"), queryParams);
    }

    @Override
    public void insertPluginUseRecord(Map<String, Object> params) {
        super.insert(generateStatement("insertPluginUseRecord"), params);
    }

    @Override
    public void incrementDownloadCount(Integer pluginId) {
        super.update("incrementDownloadCount", pluginId);
    }
}
