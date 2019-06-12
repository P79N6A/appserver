package com.idcq.appserver.dao.activity;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.common.PageModel;
/**
 * 商圈活动dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IBusinessAreaActivityDao {
	
	/**
	 * 新增商圈活动
	 * @param shopMemberDto
	 * @return
	 * @throws Exception
	 */
	int addBusinessAreaActivity(BusinessAreaActivityDto businessAreaActivityDto) throws Exception;
	
	/**
	 * 删除指定的商圈活动
	 * @param businessAreaActivityId
	 * @return
	 * @throws Exception
	 */
	int delBusinessAreaActivityById(Long businessAreaActivityId) throws Exception;
	
	/**
	 * 修改指定的商圈活动
	 * @param businessAreaActivityDto
	 * @return
	 * @throws Exception
	 */
	int updateBusinessAreaActivityById(BusinessAreaActivityDto businessAreaActivityDto) throws Exception;
	
	/**
	 * 查询生效的活动
	 * @param businessAreaActivityId
	 * @param long1 
	 * @return
	 * @throws Exception
	 */
	List<BusinessAreaActivityDto> getBusinessAreaActivityBy(Long shopId, Long businessAreaActivityId, String date) throws Exception;
	
	/**
	 * 获取指定的商圈活动
	 * @param businessAreaActivityId
	 * @param long1 
	 * @return
	 * @throws Exception
	 */
	BusinessAreaActivityDto getBusinessAreaActivityById(Long businessAreaActivityId) throws Exception;
	
	/**
	 * 获取商圈活动列表
	 * @param businessAreaActivityDto
	 * @return
	 * @throws Exception
	 */
	List<BusinessAreaActivityDto> getBusinessAreaActivityList(BusinessAreaActivityDto businessAreaActivityDto,int pageNo,int pageSize) throws Exception;
	
	
	Integer getBusinessAreaActivityCount(BusinessAreaActivityDto businessAreaActivityDto)throws Exception;
	/**
	 * 获取参与活动商铺数 
	 * @param map
	 * @return
	 */
	Integer getActivitysCount(Map<String, Object> map);

	List getActivitys(Map<String, Object> map);

	List<Map> getAllActivitys(Map<String, Object> map);
	
	/**
	 * 根据条件获取商圈活动
	 * @Title: getBusinessAreaActivityListByCondition 
	 * @param @param businessAreaActivityDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return List<BusinessAreaActivityDto>    返回类型 
	 * @throws
	 */
	List<BusinessAreaActivityDto>getBusinessAreaActivityListByCondition(BusinessAreaActivityDto businessAreaActivityDto,PageModel pageModel)throws Exception;
	
	
	/**
	 * 批量更新商圈活动状态
	 * @Title: batchUpdateBusinessAreaActivity 
	 * @param @param busAreaActivityList
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void batchUpdateBusinessAreaActivity(List<BusinessAreaActivityDto>busAreaActivityList)throws Exception;
	
	/**
	 * 判断商圈活动是否存在
	 * @param businessAreaActivityId
	 * @return
	 * @throws Exception
	 */
	int getBusinessAreaActivityExistsById(Long businessAreaActivityId)throws Exception;

	Integer getAllActivitysCount(Map<String, Object> map);

	/**
	 * 查询每个活动的参加数量有多少
	 * @Title: queryActivityNumInfo 
	 * @param @param activityList
	 * @param @return
	 * @param @throws Exception
	 * @return List<BusinessAreaActivityDto>    返回类型 
	 * @throws
	 */
	List<BusinessAreaActivityDto> queryActivityNumInfo(List<BusinessAreaActivityDto> activityList)throws Exception;

	/**
	 * 获取商圈活动的参与商家总数
	 * @param map
	 * @return
	 */
	int getActivityShopListCount(Map<String, Object> map);
	
	/**
	 * 获取商圈活动的参与商家列表
	 * @param map
	 * @return
	 */
	List<Map> getActivityShopList(Map<String, Object> map);

	Integer findbusAreaAcitivity(String shopId);

	List<Map> getAllActivitysWithoutJoin(Map<String, Object> map);

	Integer getActivitysCountWithoutJoin(Map<String, Object> map);

	Integer getActivitiesNoShopTypeCount(Map<String, Object> map);

	List<Map> getActivitiesNoShopType(Map<String, Object> map);

	Integer getActivitiesShopTypeCount(Map<String, Object> map);

	List<Map> getActivitiesShopType(Map<String, Object> map);

	Integer getActivitiesAppCount(Map<String, Object> map);

	List<Map> getActivitiesApp(Map<String, Object> map);

	Integer getActivitiesNoShopIdCount(Map<String, Object> map);

	List<Map> getActivitiesNoShopId(Map<String, Object> map);
	
	/**
	 * 根据活动Id列表查找活动
	 * @Title: getActivityListByActivityIdList 
	 * @param @param idList
	 * @param @return
	 * @param @throws Exception
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	List<BusinessAreaActivityDto>getActivityListByActivityIdList(List<Long>idList)throws Exception;

	List<BusinessAreaActivityDto> getActivitiesByShopId(
			BusinessAreaActivityDto businessAreaActivityDto);
	
	List<Map<String, Object>> getBusAreaActivityListWithShopId(Map<String, Object> requestMap) throws Exception;
	List<Map<String, Object>> getBusAreaActivityListWithOutShopId(Map<String, Object> requestMap) throws Exception;
	int getBusAreaActivityListWithShopIdCount(Map<String, Object> requestMap) throws Exception;
	int getBusAreaActivityListWithOutShopIdCount(Map<String, Object> requestMap) throws Exception;
}
