package com.idcq.appserver.dao.shop;

import java.util.List;

import com.idcq.appserver.dto.shop.BookTimeRuleDto;

public interface IBookTimeRuleDao {
	
	List<BookTimeRuleDto> getTimeRuleListBySettingId(Long settingId) throws Exception;
	
	/**
	 * 新增时间规则
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int addTimeRuleDto(BookTimeRuleDto dto) throws Exception;
	
	/**
	 * 删除时间规则
	 * 
	 * @param settingId
	 * @return
	 * @throws Exception
	 */
	int delTimeRuleDtoBySettingId(Long settingId) throws Exception;
}
