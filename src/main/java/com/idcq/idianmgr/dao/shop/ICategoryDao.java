package com.idcq.idianmgr.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.shop.CategoryDto;

/**
 * 操作分类Dao
 * @author huangrui
 *
 */
public interface ICategoryDao {

	void insertCategory(CategoryDto categoryDto);
	void updateCategory(CategoryDto categoryDto);
	void deleteCategory(Long categoryId);
	
	/**
	 * 删除图片关联关系
	 * @param map 包含参数：
	 *			bizId, 
	 * 			bizType,
	 * 			picType
	 * 
	 */
	void delAttachmentRelation(Map<String, Object> map);
	
	/**
	 * 新增图片关联关系
	 * @param map 包含参数：
	 * 			attachementId，
	 * 			bizId, 
	 * 			bizType, 
	 * 			picType
	 */
	void insertAttachmentRelation(Map<String, Object> map);
	
	/**
	 * 修改关联关系
	 * 
	 * @Function: com.idcq.idianmgr.dao.shop.ICategoryDao.updateAttachmentRelation
	 * @Description:
	 *
	 * @param map
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月29日 下午3:32:55
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月29日    shengzhipeng       v1.0.0         create
	 */
	int updateAttachmentRelation(Map<String, Object> map);
	
	
	List<CategoryDto> getCategorys(CategoryDto categoryDto);
	
	/**
	 * 删除分类关联关系
	 * @param string
	 */
	void deleteCategoryRelation(String categoryId);
	
	/**
	 * 更新分类对应的商品族状态
	 * @param status
	 * @param categoryId
	 */
	void updateGroupGoodsOfCatgory(int status, Long categoryId);
	
	/**
	 * 修改商品族名称
	 * @param newName
	 * @param categoryId
	 */
	void updateGroupGoodsName(String newName, Long categoryId);
	
	/**
	 * 根据子分类编号获取父分类编号
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	Long getParentCategoryIdByChildId(Long categoryId)throws Exception;
	
	/**
	 * 根据分类编号查询子分类编号
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	Long getChildCategoryIdByCategoryId(Long categoryId) throws Exception;
	
	/**
	 * 校验服务分类名称唯一性
	 * @param categoryDto
	 * @return
	 */
	boolean isNameExist(CategoryDto categoryDto);
	
	Integer getCategoryIndexBy(CategoryDto categoryDto);
	
}
