package com.idcq.appserver.service.busArea.busAreaActivity;

import java.util.List;
import java.util.Map;

import org.aspectj.weaver.ast.Instanceof;

import com.idcq.appserver.dto.common.PageModel;


import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;


public interface IBusAreaActivityService {

	List<Map> getDynamicConfig(Integer bizId) throws Exception;

	List<Map> getBusAreaConfig(Integer bizId);


	PageModel getActivityList(Map<String, Object> map) throws Exception;

	PageModel getBusAreaActivityList(Map<String, Object> map) throws Exception;

	/**
	 * 保存/修改商圈活动信息
	 * @param businessAreaActivityDto
	 * @return 活动编号
	 * @throws Exception
	 */
	Long updateActivity(BusinessAreaActivityDto businessAreaActivityDto) throws Exception;
	
	/**
	 * 生成用户活动资格数据
	 * @param busAreaActId
	 * @param userSourceType
	 * @param userSourceId
	 * @param userSourceChannel
	 * @param mobile
	 * @return	1:获取活动资格成功；0:该手机号码已经获取了活动资格
	 * @throws Exception
	 */
	int applyBusinessArea(long busAreaActId,int userSourceType,Long userSourceId,Integer userSourceChannel,String mobile, String veriCode) throws Exception;
	
	/**
	 * 获取用户活动资格数据
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	String applyBusinessArea(Map<String,? extends Object> paramMap) throws Exception;

	Map<String, Object> getActivityDetail(Long shopId,Integer businessAreaActivityId) throws NumberFormatException, Exception;
	
	/**
	 * 获取商圈活动的参与商家列表接口
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	PageModel getActivityShopList(Map<String, Object> map) throws Exception;

	/**
	 * 获取指定的商圈活动
	 * @param businessAreaActivityId
	 * @return
	 * @throws Exception
	 */
	BusinessAreaActivityDto getBusinessAreaActivityById(Long businessAreaActivityId) throws Exception;
	
	BusinessAreaShopDto getBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception;
	void operateActivity(Map<String, Object> requestMap) throws Exception;

	PageModel getActivityList1(Map<String, Object> map, boolean isTokenOrSession) throws Exception;

	List<BusinessAreaActivityDto> getActivitiesByShopId(BusinessAreaActivityDto businessAreaActivityDto);
	
	List<BusinessAreaActivityDto>getActivityListByCondition(BusinessAreaActivityDto businessAreaActivityDto)throws Exception;
}
