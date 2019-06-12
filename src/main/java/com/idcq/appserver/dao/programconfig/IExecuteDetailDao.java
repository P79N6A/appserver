package com.idcq.appserver.dao.programconfig;

import java.util.List;

import com.idcq.appserver.dto.programconfig.ExecuteDetailDto;
/**
 * 执行详情dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IExecuteDetailDao {
	
	/**
	 * 新增执行详情
	 * @param executeDetailDto
	 * @return
	 * @throws Exception
	 */
	int addExecuteDetail(ExecuteDetailDto executeDetailDto) throws Exception;
	
	/**
	 * 删除指定的执行详情
	 * @param executeDetailId
	 * @return
	 * @throws Exception
	 */
	int delExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto) throws Exception;
	
	/**
	 * 修改指定的执行详情
	 * @param executeDetailDto
	 * @return
	 * @throws Exception
	 */
	int updateExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto) throws Exception;
	
	/**
	 * 获取指定的执行详情
	 * @param executeDetailDto
	 * @return
	 * @throws Exception
	 */
	ExecuteDetailDto getExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto) throws Exception;
	
	/**
	 * 获取执行详情列表
	 * @param executeDetailDto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<ExecuteDetailDto> getExecuteDetailList(ExecuteDetailDto executeDetailDto,int pageNo,int pageSize) throws Exception;
	
}
