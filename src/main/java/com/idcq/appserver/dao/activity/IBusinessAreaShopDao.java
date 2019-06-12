package com.idcq.appserver.dao.activity;


import java.util.List;

import com.idcq.appserver.dto.activity.BusinessAreaShopDto;
/**
 * 商圈活动店铺dao接口
 * @author szp
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IBusinessAreaShopDao {
    
    /**
     * 新增商圈活动店铺
     * @param businessAreaShopDto
     * @return
     * @throws Exception
     */
    int addBusinessAreaShop(BusinessAreaShopDto businessAreaShopDto) throws Exception;
    
    /**
     * 删除指定的商圈活动店铺
     * @param businessAreaActivityId
     * @param shopId
     * @return
     * @throws Exception
     */
    int delBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception;
    
    int delBusinessAreaShopByActivityId(Long businessAreaActivityId)throws Exception;
    /**
     * 修改指定的商圈活动店铺
     * @param businessAreaShopDto
     * @return
     * @throws Exception
     */
    int updateBusinessAreaShopByCompKey(BusinessAreaShopDto businessAreaShopDto) throws Exception;
    
    /**
     * 获取指定的商圈活动店铺
     * @param businessAreaActivityId
     * @param shopId
     * @return
     * @throws Exception
     */
    BusinessAreaShopDto getBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception;
    
    /**
     * 获取商圈活动店铺列表
     * @param businessAreaShopDto
     * @return
     * @throws Exception
     */
    List<BusinessAreaShopDto> getBusinessAreaShopList(BusinessAreaShopDto businessAreaShopDto, int pageNo, int pageSize) throws Exception;
    
    /**
     * 获取总条数
     * @Title: getBusinessAreaShopCount 
     * @param @param businessAreaShopDto
     * @param @return
     * @return Integer    返回类型 
     * @throws
     */
	Integer getBusinessAreaShopCount(BusinessAreaShopDto businessAreaShopDto);
    
}