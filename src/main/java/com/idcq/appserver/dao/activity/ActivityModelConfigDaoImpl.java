package com.idcq.appserver.dao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.ActivityModelConfigDto;

@Repository
public class ActivityModelConfigDaoImpl extends BaseDao<ActivityModelConfigDto> implements IActivityModelConfigDao {

    @Override
    public int addActivityModelConfig(ActivityModelConfigDto activityModelConfigDto) throws Exception {
        
        return super.insert("addActivityModelConfig",activityModelConfigDto);
    }

    @Override
    public int delActivityModelConfigById(Long activityModelConfigId) throws Exception {
        return super.delete("delActivityModelConfigById", activityModelConfigId);
    }

    @Override
    public int updateActivityModelConfigById(ActivityModelConfigDto activityModelConfigDto) throws Exception {
        return super.update("updateActivityModelConfigById",activityModelConfigDto);
    }

    @Override
    public ActivityModelConfigDto getActivityModelConfigById(Long activityModelConfigId) throws Exception {
        return (ActivityModelConfigDto)super.selectOne("getActivityModelConfigById", activityModelConfigId);
    }

    @Override
    public List<ActivityModelConfigDto> getActivityModelConfigList(ActivityModelConfigDto activityModelConfigDto,
            int pageNo, int pageSize) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("am", activityModelConfigDto);
        map.put("n", (pageNo-1)*pageSize);                   
        map.put("m", pageSize);    
        return super.findList(generateStatement("getActivityModelConfigList"), map);
    }

	@Override
	public List<Map> getActivityModelConfigListByModelId(ActivityModelConfigDto activityModelConfigDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelId", activityModelConfigDto.getModelId());
		return this.getSqlSession().selectList("getActivityModelConfigListByModelId", map);
	}

}
