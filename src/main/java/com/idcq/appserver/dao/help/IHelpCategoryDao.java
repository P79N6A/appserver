package com.idcq.appserver.dao.help;

import java.util.List;

import com.idcq.appserver.dto.help.HelpCategoryDto;

public interface IHelpCategoryDao {

	/**
	 * 获取帮助信息分类列表
	 * 
	 * @param helpCategory
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<HelpCategoryDto> getHelpCategoryList(HelpCategoryDto helpCategory,
			int page, int pageSize);

	/**
	 * 获取帮助信息分类总记录数
	 * 
	 * @param helpCategory
	 * @return
	 */
	int getHelpCategoryCount(HelpCategoryDto helpCategory);
}
