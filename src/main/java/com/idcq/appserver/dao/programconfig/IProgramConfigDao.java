package com.idcq.appserver.dao.programconfig;

import java.util.List;

import com.idcq.appserver.dto.programconfig.ExecutePointDto;
import com.idcq.appserver.dto.programconfig.ProgramConfigDto;
/**
 * 程序配置dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IProgramConfigDao {
	
	/**
	 * 新增程序配置
	 * @param programConfigDto
	 * @return
	 * @throws Exception
	 */
	int addProgramConfig(ProgramConfigDto programConfigDto) throws Exception;
	
	/**
	 * 删除指定的程序配置
	 * @param programConfigId
	 * @return
	 * @throws Exception
	 */
	int delProgramConfigById(Integer programConfigId) throws Exception;
	
	/**
	 * 修改指定的程序配置
	 * @param programConfigDto
	 * @return
	 * @throws Exception
	 */
	int updateProgramConfigById(ProgramConfigDto programConfigDto) throws Exception;
	
	/**
	 * 获取指定的程序配置
	 * @param programConfigId
	 * @return
	 * @throws Exception
	 */
	ProgramConfigDto getProgramConfigById(Integer programConfigId) throws Exception;
	
	/**
	 * 获取程序配置列表
	 * @param programConfigDto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<ProgramConfigDto> getProgramConfigList(ProgramConfigDto programConfigDto,int pageNo,int pageSize) throws Exception;
	
	/**
	 * 获取指定执行点的程序配置列表
	 * @param executePointId
	 * @return
	 * @throws Exception
	 */
	List<ProgramConfigDto> getProgramConfigListByExecPointId(Integer executePointId) throws Exception;
	
    /**
     * 通过插入点获取程序配置
     * 
     * @param executePointDto
     * @return
     * @throws Exception
     */
    ProgramConfigDto getBeanByExecutePointCode(ExecutePointDto executePointDto) throws Exception;	
    
    /**
     * 通过程序配置code、modelCode获取程序配置
     * 
     * @param executePointDto
     * @return
     * @throws Exception
     */
    ProgramConfigDto getBeanByProgramConfigCode(ProgramConfigDto programConfigDto) throws Exception;   
}
