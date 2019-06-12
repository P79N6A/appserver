package com.idcq.idianmgr.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.shop.CategoryDto;
@Repository
public class CategoryDaoImpl extends BaseDao<CategoryDto> implements ICategoryDao{

	@Override
	public void insertCategory(CategoryDto categoryDto) {
		insert(generateStatement("insertCategory"), categoryDto);
	}

	@Override
	public void updateCategory(CategoryDto categoryDto) {
		update(generateStatement("updateCategory"), categoryDto);
	}

	@Override
	public void deleteCategory(Long categoryId) {
		delete("deleteCategoryById", categoryId);
	}

	@Override
	public void delAttachmentRelation(Map<String, Object> map) {
		delete(generateStatement("delAttachmentRelation"), map);
	}

	@Override
	public void insertAttachmentRelation(Map<String, Object> map) {
		insert(generateStatement("insertAttachmentRelation"), map);
	}

	@Override
	public List<CategoryDto> getCategorys(CategoryDto categoryDto) {
		return findList(generateStatement("selectCategorys"), categoryDto);
	}

	@Override
	public void deleteCategoryRelation(String categoryId) {
		 delete("deleteCategoryRelation", categoryId);
	}

	@Override
	public int updateAttachmentRelation(Map<String, Object> map) {
		return update(generateStatement("updateAttachmentRelation"), map);
	}

	@Override
	public void updateGroupGoodsOfCatgory(int status, Long categoryId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", status);
		param.put("categoryId", categoryId);
		update("updateGroupGoodsOfCatgory", param);
		update("updateGoodsOfGroupGoods",param);
	}

	@Override
	public void updateGroupGoodsName(String newName, Long categoryId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", newName);
		param.put("categoryId", categoryId);
		update("updateGroupGoodsNameByCategoryId", param);
	}

	public Long getParentCategoryIdByChildId(Long categoryId) throws Exception {
		return (Long) selectOne(generateStatement("getParentCategoryIdByChildId"), categoryId);
	}

	@Override
	public Long getChildCategoryIdByCategoryId(Long categoryId)
			throws Exception {
		return (Long) selectOne(generateStatement("getChildCategoryIdByCategoryId"), categoryId);
	}

	@Override
	public boolean isNameExist(CategoryDto categoryDto) {
		Integer count = (Integer)selectOne("isGoodCategoryNameUsed", categoryDto);
		if(count != null && count > 0)
			return true;
		return false;
	}

    @Override
    public Integer getCategoryIndexBy(CategoryDto categoryDto)
    {
        return (Integer) selectOne(generateStatement("getCategoryIndexBy"), categoryDto);
    }
	
}
