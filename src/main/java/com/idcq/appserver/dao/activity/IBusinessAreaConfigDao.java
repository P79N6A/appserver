package com.idcq.appserver.dao.activity;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.activity.BusinessAreaConfigDto;
/**
 * 商圈活动配置dao接口
 * @author szp
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IBusinessAreaConfigDao {
    
    /**
     * 新增商圈活动配置
     * @param businessAreaConfigDto
     * @return
     * @throws Exception
     */
    int addBusinessAreaConfig(BusinessAreaConfigDto businessAreaConfigDto) throws Exception;
    
    /**
     * 批量新增商圈活动配置
     * @param businessAreaConfigDtos
     * @return
     * @throws Exception
     */
    int batchAddBusinessAreaConfig(List<BusinessAreaConfigDto> businessAreaConfigDtos) throws Exception;
    
    /**
     * 删除指定的商圈活动配置
     * @param businessAreaConfigId
     * @return
     * @throws Exception
     */
    int delBusinessAreaConfigById(Long businessAreaConfigId) throws Exception;
    
    int delBusinessAreaConfigByActivityId(Long businessAreaActivityId) throws Exception;
    /**
     * 修改指定的商圈活动配置
     * @param businessAreaConfigDto
     * @return
     * @throws Exception
     */
    int updateBusinessAreaConfigById(BusinessAreaConfigDto businessAreaConfigDto) throws Exception;
    
    /**
     * 获取指定的商圈活动配置
     * @param businessAreaConfigId
     * @return
     * @throws Exception
     */
    BusinessAreaConfigDto getBusinessAreaConfigById(Long businessAreaConfigId) throws Exception;
    
    /**
     * 获取商圈活动配置列表
     * @param businessAreaConfigDto
     * @return
     * @throws Exception
     */
    List<BusinessAreaConfigDto> getBusinessAreaConfigList(BusinessAreaConfigDto businessAreaConfigDto, int pageNo, int pageSize) throws Exception;

    public List<Map> getBusinessAreaConfigByActivityId(BusinessAreaConfigDto businessAreaConfigDto);
    /**
     * 
     * 获取商圈活动满value
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaConfigDao.getMaxConfigValueByCodeAndareaId
     * @Description:
     *
     * @param businessAreaActivityId
     * @param configCode
     * @param configVlaues
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午4:41:40
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
	Map<String, Object> getOverConfigValueByCodeAndAreaId(Long businessAreaActivityId,String configCode,String configValue) throws Exception;
    /**
     * 
     *获取商圈活动送value
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaConfigDao.getGiveConfigValueByCodeAndAreaId
     * @Description:
     *
     * @param businessAreaActivityId
     * @param configCode
     * @param configVlaues
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午4:45:09
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
	Map<String, Object> getGiveConfigValueByCodeAndAreaId(Long businessAreaActivityId,String over,
            String give,Double money) throws Exception;
	
	List<BusinessAreaConfigDto> getBusinessAreaConfigByActivityId(Long businessAreaActivityId);
	String getbusAreaConfigNameByActivityId(Long businessAreaActivityId);
}
