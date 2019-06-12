package com.idcq.idianmgr.dao.shop;

import java.util.List;

import com.idcq.idianmgr.dto.shop.TechTypeDto;

/**
 * 操作分类Dao
 * @author huangrui
 *
 */
public interface ITechTypeDao {

	
	void addTechType(TechTypeDto techTypeDto);
	void updateTechType(TechTypeDto techTypeDto);
	
	/**
	 * 分页查询技师类别
	 * @param techTypeDto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<TechTypeDto> getTechTypeDtos(TechTypeDto techTypeDto, int page, int pageSize);
	
	int getTechTypeCount(TechTypeDto techTypeDto);
	/**
	 * 判断技师级别是否被使用
	 * @param string
	 * @return
	 */
	boolean isTechTypeUsed(String techTypeId);
	
	/**
	 * 技术级别名称重复判断
	 * @param techTypeDto
	 * @return
	 */
	boolean isNameExist(TechTypeDto techTypeDto);
}
