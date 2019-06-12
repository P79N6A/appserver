package com.idcq.appserver.dao.activity;

import java.util.List;

import com.idcq.appserver.dto.activity.BusinessAreaModelDto;
/**
 * 商圈商圈类型模板dao接口
 * @author szp
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IBusinessAreaModelDao {
    
    /**
     * 新增商圈类型模板
     * @param businessAreaModelDto
     * @return
     * @throws Exception
     */
    int addBusinessAreaModel(BusinessAreaModelDto businessAreaModelDto) throws Exception;
    
    /**
     * 删除指定的商圈类型模板
     * @param businessAreaModelId
     * @return
     * @throws Exception
     */
    int delBusinessAreaModelById(Long businessAreaModelId) throws Exception;
    
    /**
     * 修改指定的商圈类型模板
     * @param businessAreaModelDto
     * @return
     * @throws Exception
     */
    int updateBusinessAreaModelById(BusinessAreaModelDto businessAreaModelDto) throws Exception;
    
    /**
     * 获取指定的商圈类型模板
     * @param businessAreaModelId
     * @return
     * @throws Exception
     */
    BusinessAreaModelDto getBusinessAreaModelById(Long businessAreaModelId) throws Exception;
    
    /**
     * 获取商圈类型模板列表
     * @param businessAreaModelDto
     * @return
     * @throws Exception
     */
    List<BusinessAreaModelDto> getBusinessAreaModelList(BusinessAreaModelDto businessAreaModelDto, int pageNo, int pageSize) throws Exception;
    
}
