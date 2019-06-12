package com.idcq.appserver.dao.activity;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.activity.ActivityModelConfigDto;
/**
 * 活动类型配置dao接口
 * @author szp
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IActivityModelConfigDao {
    
    /**
     * 新增活动类型配置
     * @param activityModelConfigDto
     * @return
     * @throws Exception
     */
    int addActivityModelConfig(ActivityModelConfigDto activityModelConfigDto) throws Exception;
    
    /**
     * 删除指定的活动类型配置
     * @param activityModelConfigId
     * @return
     * @throws Exception
     */
    int delActivityModelConfigById(Long activityModelConfigId) throws Exception;
    
    /**
     * 修改指定的活动类型配置
     * @param activityModelConfigDto
     * @return
     * @throws Exception
     */
    int updateActivityModelConfigById(ActivityModelConfigDto activityModelConfigDto) throws Exception;
    
    /**
     * 获取指定的活动类型配置
     * @param activityModelConfigId
     * @return
     * @throws Exception
     */
    ActivityModelConfigDto getActivityModelConfigById(Long activityModelConfigId) throws Exception;
    
    /**
     * 获取活动类型配置列表
     * @param activityModelConfigDto
     * @return
     * @throws Exception
     */
    List<ActivityModelConfigDto> getActivityModelConfigList(ActivityModelConfigDto activityModelConfigDto,int pageNo,int pageSize) throws Exception;

	List<Map> getActivityModelConfigListByModelId(ActivityModelConfigDto activityModelConfigDto);
    
}
