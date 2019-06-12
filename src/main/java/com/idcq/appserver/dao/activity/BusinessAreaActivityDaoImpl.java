package com.idcq.appserver.dao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.common.PageModel;

@Repository
public class BusinessAreaActivityDaoImpl extends BaseDao<BusinessAreaActivityDto>implements IBusinessAreaActivityDao{

	@Override
	public int addBusinessAreaActivity(
			BusinessAreaActivityDto businessAreaActivityDto) throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addBusinessAreaActivity",businessAreaActivityDto);
	}

	@Override
	public int delBusinessAreaActivityById(Long businessAreaActivityId)
			throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delBusinessAreaActivityById",businessAreaActivityId);
	}

	@Override
	public int updateBusinessAreaActivityById(
			BusinessAreaActivityDto businessAreaActivityDto) throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateBusinessAreaActivityById",businessAreaActivityDto);
	}

	@Override
	public BusinessAreaActivityDto getBusinessAreaActivityById(
			Long businessAreaActivityId) throws Exception {
		// TODO Auto-generated method stub
		return (BusinessAreaActivityDto)super.selectOne("getBusinessAreaActivityById",businessAreaActivityId);
	}

	@Override
	public List<BusinessAreaActivityDto> getBusinessAreaActivityList(
			BusinessAreaActivityDto businessAreaActivityDto, int pageNo,
			int pageSize) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start", (pageNo - 1) * pageSize);
		map.put("limit", pageSize);
		map.put("businessAreaActivityDto", businessAreaActivityDto);
		return super.findList("getBusinessAreaActivityList", map);
	}
	
	/**
	 * 条件获取商圈活动列表
	 * @Title: getBusinessAreaActivityListByCondition 
	 * @param @param businessAreaActivityDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<BusinessAreaActivityDto> getBusinessAreaActivityListByCondition(
			BusinessAreaActivityDto businessAreaActivityDto, PageModel pageModel)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("businessAreaActivityDto", businessAreaActivityDto);
		if(pageModel!=null){
			params.put("start", (pageModel.getToPage() - 1) * pageModel.getPageSize());
			params.put("limit", pageModel.getPageSize());
		}
		return super.findList(generateStatement("getBusinessAreaActivityListByCondition"),params);
	}
	
	/**
	 * 漂亮更新商圈活动状态
	 * @Title: batchUpdateBusinessAreaActivity 
	 * @param @param busAreaActivityList
	 * @param @throws Exception  
	 * @throws
	 */
	public void batchUpdateBusinessAreaActivity(
			List<BusinessAreaActivityDto> busAreaActivityList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("busAreaActivityList", busAreaActivityList);
		super.update(generateStatement("batchUpdateBusinessAreaActivity"), params);
	}

	@Override
	public Integer getActivitysCount(Map<String, Object> map) {
		return (Integer)getSqlSession().selectOne("getShopCountByActivity", map);
	}

	@Override
	public List<BusinessAreaActivityDto> getActivitys(Map<String, Object> map) {
		return super.findList(generateStatement("getBusAreaActivitys"), map);
	}

	@Override
	public List<Map> getAllActivitys(Map<String, Object> map) {
		return this.getSqlSession().selectList("getBusAreaAllActivitys", map);
	}


	public int getBusinessAreaActivityExistsById(Long businessAreaActivityId)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getBusinessAreaActivityExistsById"), businessAreaActivityId);
	}

	@Override
	public Integer getAllActivitysCount(Map<String, Object> map) {
		return (Integer)getSqlSession().selectOne("getAllActivitysCount", map);
	}
	
	/**
	 * 查询活动的参与方数量信息
	 * @Title: queryActivityNumInfo 
	 * @param @param activityList
	 * @param @return+
	 * @param @throws Exception  
	 * @throws
	 */
	public List<BusinessAreaActivityDto> queryActivityNumInfo(
			List<BusinessAreaActivityDto> activityList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("activityList", activityList);
		return super.findList(generateStatement("queryActivityNumInfo"),params);
	}

	@Override
	public int getActivityShopListCount(Map<String, Object> map) {
		return (Integer)getSqlSession().selectOne("getActivityShopListCount", map);
	}

	@Override
	public List<Map> getActivityShopList(Map<String, Object> map) {
		return this.getSqlSession().selectList("getActivityShopList", map);
	}

	@Override
	public List<BusinessAreaActivityDto> getBusinessAreaActivityBy(Long shopId,
			Long businessAreaActivityId, String date) throws Exception {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("businessAreaActivityId", businessAreaActivityId);
		map.put("shopId", shopId);
		map.put("date", date);
		return this.getSqlSession().selectList("getBusinessAreaActivityBy", map);
	}

	@Override
	public Integer findbusAreaAcitivity(String shopId) {
		return (Integer)getSqlSession().selectOne("isJoinActivity", shopId);
	}

	@Override
	public List<Map> getAllActivitysWithoutJoin(Map<String, Object> map) {
		return this.getSqlSession().selectList("getAllActivitysWithoutJoin", map);
	}

	@Override
	public Integer getActivitysCountWithoutJoin(Map<String, Object> map) {
		return (Integer)getSqlSession().selectOne("getActivitysCountWithoutJoin", map);
	}

	@Override
	public Integer getActivitiesNoShopTypeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)getSqlSession().selectOne("getActivitiesNoShopTypeCount", map);
	}

	@Override
	public List<Map> getActivitiesNoShopType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList("getActivitiesNoShopType", map);
	}

	@Override
	public Integer getActivitiesShopTypeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)getSqlSession().selectOne("getActivitiesShopTypeCount", map);
	}

	@Override
	public List<Map> getActivitiesShopType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList("getActivitiesShopType", map);
	}

	@Override
	public Integer getActivitiesAppCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)getSqlSession().selectOne("getActivitiesAppCount", map);
	}

	@Override
	public List<Map> getActivitiesApp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList("getActivitiesApp", map);
	}

	@Override
	public Integer getActivitiesNoShopIdCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)getSqlSession().selectOne("getActivitiesNoShopIdCount", map);
	}

	@Override
	public List<Map> getActivitiesNoShopId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList("getActivitiesNoShopId", map);
	}

	@Override
	public List<BusinessAreaActivityDto> getActivityListByActivityIdList(List<Long> idList)
			throws Exception {
		return findList(generateStatement("getActivityListByActivityIdList"), idList);
	}

	@Override
	public Integer getBusinessAreaActivityCount(
			BusinessAreaActivityDto businessAreaActivityDto) throws Exception {
		return (Integer)selectOne(generateStatement("getBusinessAreaActivityCount"), businessAreaActivityDto);
	}

	@Override
	public List<BusinessAreaActivityDto> getActivitiesByShopId(
			BusinessAreaActivityDto businessAreaActivityDto) {
		return findList(generateStatement("getActivitiesByShopId"), businessAreaActivityDto);
	}

	@Override
	public List<Map<String, Object>> getBusAreaActivityListWithShopId(Map<String, Object> requestMap) throws Exception {
		return (List)findList(generateStatement("getBusAreaActivityListWithShopId"), requestMap);
	}
	
	@Override
	public List<Map<String, Object>> getBusAreaActivityListWithOutShopId(Map<String, Object> requestMap) throws Exception {
		return (List)findList(generateStatement("getBusAreaActivityListWithOutShopId"), requestMap);
	}
    @Override
    public int getBusAreaActivityListWithShopIdCount(Map<String, Object> requestMap) throws Exception {
        return (int) selectOne(generateStatement("getBusAreaActivityListWithShopIdCount"), requestMap);
    }
    
    @Override
    public int getBusAreaActivityListWithOutShopIdCount(Map<String, Object> requestMap) throws Exception {
        return (int) selectOne(generateStatement("getBusAreaActivityListWithOutShopIdCount"), requestMap);
    }	
	
}