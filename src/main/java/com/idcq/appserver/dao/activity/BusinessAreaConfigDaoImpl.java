package com.idcq.appserver.dao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaConfigDto;

@Repository
public class BusinessAreaConfigDaoImpl extends BaseDao<BusinessAreaConfigDto> implements IBusinessAreaConfigDao {

    @Override
    public int addBusinessAreaConfig(BusinessAreaConfigDto businessAreaConfigDto) throws Exception {
        return super.insert("addBusinessAreaConfig",businessAreaConfigDto);
    }

    @Override
	public int batchAddBusinessAreaConfig(
			List<BusinessAreaConfigDto> businessAreaConfigDtos)
			throws Exception {
    	// TODO ...还未写sql
		return super.insert("batchAddBusinessAreaConfig",businessAreaConfigDtos);
	}

	@Override
    public int delBusinessAreaConfigById(Long businessAreaConfigId) throws Exception {
        return super.delete("delBusinessAreaConfigById", businessAreaConfigId);
    }

	@Override
    public int delBusinessAreaConfigByActivityId(Long businessAreaActivityId) throws Exception {
        return super.delete("delBusinessAreaConfigByActivityId", businessAreaActivityId);
    }
    @Override
    public int updateBusinessAreaConfigById(BusinessAreaConfigDto businessAreaConfigDto) throws Exception {
        return super.update("updateBusinessAreaConfigById",businessAreaConfigDto);
    }

    @Override
    public BusinessAreaConfigDto getBusinessAreaConfigById(Long businessAreaConfigId) throws Exception {
        return (BusinessAreaConfigDto)super.selectOne("getBusinessAreaConfigById", businessAreaConfigId);
    }

    @Override
    public List<BusinessAreaConfigDto> getBusinessAreaConfigList(BusinessAreaConfigDto businessAreaConfigDto,
            int pageNo, int pageSize) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ba", businessAreaConfigDto);
        map.put("n", (pageNo-1)*pageSize);                   
        map.put("m", pageSize);    
        return super.findList(generateStatement("getBusinessAreaConfigList"), map);
    }


	@Override
	public List<Map> getBusinessAreaConfigByActivityId(
			BusinessAreaConfigDto businessAreaConfigDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("businessAreaActivityId", businessAreaConfigDto.getBusinessAreaActivityId());
		return this.getSqlSession().selectList("getBusinessAreaConfigByActivityId", map);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaConfigDao#getOverConfigValueByCodeAndAreaId(java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Object> getOverConfigValueByCodeAndAreaId(Long businessAreaActivityId, String configCode,
            String configValue) throws Exception
    {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("businessAreaActivityId",businessAreaActivityId );
        parms.put("configCode",configCode );
        parms.put("configValue", configValue);
        
        return (Map<String, Object>) super.selectOne("getOverConfigValueByCodeAndAreaId", parms);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaConfigDao#getGiveConfigValueByCodeAndAreaId(java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Object> getGiveConfigValueByCodeAndAreaId(Long businessAreaActivityId, String over,
            String give,Double money) throws Exception
    {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("businessAreaActivityId",businessAreaActivityId );
        parms.put("over",over );
        parms.put("give",give );
        parms.put("configValue", money);
        
        return (Map<String, Object>) super.selectOne("getGiveConfigValueByCodeAndAreaId", parms);
    }

    @Override
    public List<BusinessAreaConfigDto> getBusinessAreaConfigByActivityId(Long businessAreaActivityId) {
        return super.findList(generateStatement("getBusinessAreaConfigByActId"), businessAreaActivityId);
    }

    @Override
    public String getbusAreaConfigNameByActivityId(Long businessAreaActivityId) {
    	Map<String , Object> parms = new HashMap<String, Object>();
    	parms.put("businessAreaActivityId",businessAreaActivityId );
    	parms.put("configType",2);
    	List<BusinessAreaConfigDto> list = (List<BusinessAreaConfigDto>)super.findList(generateStatement("getBusinessAreaConfigByActId"), parms);
    	StringBuilder nameBuilder = new StringBuilder();
    	for (BusinessAreaConfigDto config: list) {
    		nameBuilder.append(config.getConfigValue()).append("，");
    	}
    	
    	int length = nameBuilder.toString().length();
 		return nameBuilder.toString().substring(0,length - 1);
    }
}
