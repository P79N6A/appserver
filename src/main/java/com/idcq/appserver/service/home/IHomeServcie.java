package com.idcq.appserver.service.home;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.city.CityDto;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.help.HelpCategoryDto;
import com.idcq.appserver.dto.help.HelpDto;
import com.idcq.appserver.dto.message.MessageDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
public interface IHomeServcie {

	
	/**
	 * 获取城市信息列表
	 * 
	 * @param city
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getCityList(CityDto city,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取栏目列表
	 * 
	 * @param column
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getColumnList(ColumnDto column,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取消息列表
	 * 
	 * @param msg
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getMessageList(MessageDto msg,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取消息详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	MessageDto getMessageById(Long id) throws Exception;
	
	
	/**
	 * 获取区域信息
	 * 
	 * @param pCode
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getDistricts(Long cityId,int page,int pageSize) throws Exception ;
	/**
	 * 获取所有县信息
	 * 
	 * @param pCode
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<DistrictDto> getAllDistricits() throws Exception;
	
	/**
	 * 获取省份信息
	 * @return
	 * @throws Exception
	 */
	List<ProvinceDto> getAllProvinces() throws Exception ;
	
	/**
	 * 获取城市信息
	 * @return
	 * @throws Exception
	 */
	PageModel getCitis(Long provinceId,String cityName,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取街道信息
	 * @return
	 * @throws Exception
	 */
	PageModel getTowns(Long districtId,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取所有城市信息
	 * @return
	 * @throws Exception
	 */
	List<Map> getAllCitis() throws Exception ;
	
	Map<String, Object> getCityInfoByName(String cityName) throws Exception ;
	/**
	 * 测试增加数据
	 * @return
	 */
	Integer addMessage(MessageDto messageDto);
	
	/**
	 * 获取帮助信息
	 * @param help
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getHelpList(HelpDto help,int page,int pageSize) throws Exception;
	
	/**
	 * 获取帮助信息分类
	 * @param helpCategory
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getHelpCategoryList(HelpCategoryDto helpCategory,int page,int pageSize) throws Exception;
	
	/**
	 * 获取
	 * 
	 * @param column
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	String getColumnListInCache(ColumnDto column, int page, int pageSize) throws Exception;

	void updateMessage(MessageDto messageDto);

	PageModel getHelpOfYDSXYList(Map<String, Object> param);
}
