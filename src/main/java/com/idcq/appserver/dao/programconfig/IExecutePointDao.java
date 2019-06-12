package com.idcq.appserver.dao.programconfig;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.programconfig.ExecutePointDto;
/**
 * 执行点dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IExecutePointDao {
	
	/**
	 * 新增执行点
	 * @param executePointDto
	 * @return
	 * @throws Exception
	 */
	int addExecutePoint(ExecutePointDto executePointDto) throws Exception;
	
	/**
	 * 删除指定的执行点
	 * @param executePointId
	 * @return
	 * @throws Exception
	 */
	int delExecutePointById(Integer executePointId) throws Exception;
	
	/**
	 * 修改指定的执行点
	 * @param executePointDto
	 * @return
	 * @throws Exception
	 */
	int updateExecutePointById(ExecutePointDto executePointDto) throws Exception;
	/**
	 * 
	 * 获取单个执行定对象
	 * @Description:
	 *
	 * @param code
	 * @param pointType
	 * @return
	 * @throws Exception
	 */
	ExecutePointDto getExecutePointByCode(String code, Integer pointType) throws Exception;
	/**
	 * 获取指定的执行点
	 * @param executePointId
	 * @return
	 * @throws Exception
	 */
	ExecutePointDto getExecutePointById(Integer executePointId) throws Exception;
	
	/**
	 * 获取执行点列表
	 * @param executePointDto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<ExecutePointDto> getExecutePointList(ExecutePointDto executePointDto,int pageNo,int pageSize) throws Exception;
	
}
