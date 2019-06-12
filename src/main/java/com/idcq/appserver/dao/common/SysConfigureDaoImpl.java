package com.idcq.appserver.dao.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.shop.ConfigTypeConvertor;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.common.SysConfigureDto;
/**
 * 该类为兼容旧版本，操作的数据是1dcq_config，不再提供更新
 * @Date 2016-05-26
 * @author zc
 *
 */
@Deprecated
@Repository
public class SysConfigureDaoImpl extends BaseDao<SysConfigureDto> implements ISysConfigureDao {
    
    @Autowired
    private IConfigDao configDao;
    
/*	public SysConfigureDto getSysConfigureDtoByKey(String configureKey)
			throws Exception {
		return (SysConfigureDto) this.selectOne(generateStatement("getSysConfigureDtoByKey"), configureKey);
	}*/
    
	public SysConfigureDto getSysConfigureDtoByKey(String configureKey){
	    SysConfigureDto result = null;
	    ConfigDto searchTemp = new ConfigDto();
	    boolean isColumn = false;
	    String tempColumnId = null;
        if(configureKey.matches("autoSettleFlag_[0-9]*$")){
            String tempKey = configureKey.substring(0, configureKey.lastIndexOf("_"));
            tempColumnId = configureKey.substring(configureKey.lastIndexOf("_") + 1);
                
            searchTemp.setBizType(19);
            searchTemp.setBizId(Long.parseLong(tempColumnId));
            searchTemp.setConfigKey(tempKey);
            isColumn = true;
        }else{
            searchTemp.setBizType(0);
            searchTemp.setBizId(0l);
            searchTemp.setConfigKey(configureKey);
        }
        List<ConfigDto> tempResult = configDao.queryForAllConfig(searchTemp);
        if(null != tempResult && tempResult.size() > 0){
            result = new SysConfigureDto();
            ConfigDto configResult = tempResult.get(0);
            result.setConfigureId(Integer.valueOf(configResult.getConfigId()+""));
            result.setConfigureDesc(configResult.getConfigDesc());
            if(isColumn){
                result.setConfigureKey(configResult.getConfigKey() + "_" + tempColumnId);
            }else{
                result.setConfigureKey(configResult.getConfigKey());
            }
            result.setConfigureValue(configResult.getConfigValue());
            result.setCreateTime(configResult.getCreateTime());
            result.setLastUpdateTime(configResult.getLastUpdateTime());
            result.setConfigureType(ConfigTypeConvertor.convertForOldSysConfigureType(configResult.getConfigGroup()));
        }
	    return result;
	}

/*    public List<Map> getSysConfiguresByKeys(Map<String, Object> requestMap) throws Exception
    {
        return this.getSqlSession().selectList("getSysConfiguresByKeys", requestMap);
    }*/
	/**
	 * @Safe
	 */
	@SuppressWarnings("unchecked")
    public List<Map> getSysConfiguresByKeys(Map<String, Object> requestMap) throws Exception
    {   
        List<Map> result = new ArrayList<Map>();
        Object configKeysObj = requestMap.get("configureKeys");
        Object configTypeObj = requestMap.get("configureType");
        List<String> searchConfigKeys = new LinkedList<String>();
        List<String> searchConfigGroups = new LinkedList<String>();
        if(null != configKeysObj){
            String[] configKeys = (String[])configKeysObj;
            for(String str : configKeys){
                if(str.matches("autoSettleFlag_[0-9]*$")){
                    String tempKey = str.substring(0, str.lastIndexOf("_"));
                    String tempColumnId = str.substring(str.lastIndexOf("_") + 1);
                    ConfigDto searchTemp = new ConfigDto();
                    searchTemp.setBizType(19);
                    searchTemp.setBizId(Long.parseLong(tempColumnId));
                    searchTemp.setConfigKey(tempKey);
                    List<ConfigDto> tempResult = configDao.queryForAllConfig(searchTemp);
                    if(null != tempResult && tempResult.size() > 0){
                        ConfigDto configResult = tempResult.get(0);
                        Map tempMap = new HashMap();
                        tempMap.put("configureKey", configResult.getConfigKey() + "_" + tempColumnId);
                        tempMap.put("configureValue", configResult.getConfigValue());
                        result.add(tempMap);
                    }
                }else{
                    searchConfigKeys.add(str); 
                }
            }
        }
        if(null != configTypeObj){
            for(String str : (String[])configTypeObj){
                searchConfigGroups.add(ConfigTypeConvertor.convertSysOldConfigureTypeForNew(Integer.valueOf(str))); 
            }
        }
        if(!searchConfigKeys.isEmpty() || !searchConfigGroups.isEmpty()){
            ConfigQueryCondition sCondition = new ConfigQueryCondition();
            sCondition.setBizId(0l);
            sCondition.setBizType(0);
            List<Integer> extendsTypes = new ArrayList<Integer>();
            extendsTypes.add(1);
            extendsTypes.add(2);
            sCondition.setExtendsType(extendsTypes);
            if(!searchConfigKeys.isEmpty()){
                int size = searchConfigKeys.size();
                String[] searchConfigKeysArr = new String[size];
                for(int i=0; i<size; i++){
                    searchConfigKeysArr[i] = searchConfigKeys.get(0);
                }
                sCondition.setConfigKeys(searchConfigKeysArr);
            }
            if(!searchConfigGroups.isEmpty()){
                int size = searchConfigGroups.size();
                String[] searchConfigGroupsArr = new String[size];
                for(int i=0; i<size; i++){
                    searchConfigGroupsArr[i] = searchConfigGroups.get(0);
                }
                sCondition.setConfigGroups(searchConfigGroupsArr);
            }
            Page pageModel = Page.getDefaulPage();
            pageModel.setpSize(Integer.MAX_VALUE);
            List<ConfigDto> searchResults = configDao.queryForConfig(sCondition, pageModel);
            if(null != searchResults && searchResults.size() > 0){
                for(ConfigDto tempDto : searchResults){
                    Map tempMap = new HashMap();
                    tempMap.put("configureKey", tempDto.getConfigKey());
                    tempMap.put("configureValue", tempDto.getConfigValue());
                    result.add(tempMap);
                }
            }
        }
        return result;
    }

}
