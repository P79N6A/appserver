package com.idcq.appserver.dao.column;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.idianmgr.dto.shop.ShopBean;

public interface IColumnDao {
	
	/**
	 * 获取栏目列表
	 * 
	 * @param column
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ColumnDto> getColumnList(ColumnDto column,int page,int pageSize) ;
	
	/**
	 * 获取栏目列表记录
	 * 
	 * @param column
	 * @return
	 */
	int getColumnListCount(ColumnDto column) ;
	
	/**
	 * 获取column 的Pid
	* @Title: getColumnIdByChildId 
	* @param @param columnId
	* @param @return
	* @return int    返回类型 
	* @throws
	 */
	Integer getColumnIdByChildId(int columnId);
	
	/**
	 * 根据columnid查找所有的column对象
	* @Title: getColumnInfoByColumnId 
	* @param @param columnIdList
	* @param @return
	* @return List<ColumnDto>    返回类型 
	* @throws
	 */
	List<ColumnDto>getColumnInfoByColumnId(List<String>columnIdList);
	
	List<ColumnDto>getMultiColumnByShopId(Long shopId);
	
	List<ColumnDto>getMultiColumnByShopId(List<Long>shopIdList);
	
	/**
	 * 新增商铺与二级行业分类关系
	 * @param shopId
	 * @param columnId
	 * @throws Exception
	 */
	void addColumnRelation(Long shopId, Integer columnId) throws Exception;
	
	void delColumnRelation(Long shopId) throws Exception;
	
	/**
	 * 判断是否有子分类
	 * @param parentColumnId
	 * @return
	 * @throws Exception
	 */
	boolean hasChildrenColumn(Long parentColumnId) throws Exception;
}
