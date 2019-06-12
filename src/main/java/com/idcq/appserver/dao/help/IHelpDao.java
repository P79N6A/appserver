package com.idcq.appserver.dao.help;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.HelpOf1dsxyDto;
import com.idcq.appserver.dto.help.HelpDto;

public interface IHelpDao {

	/**
	 * 获取帮助信息列表
	 * 
	 * @param help
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<HelpDto> getHelpList(HelpDto help, int page, int pageSize);

	/**
	 * 获取帮助信息总记录数
	 * 
	 * @param help
	 * @return
	 */
	int getHelpCount(HelpDto help);

	int getHelpOfYDSXYCount(Map<String, Object> param);

	List<HelpOf1dsxyDto> getHelpOfYDSXYList(Map<String, Object> param);

}
