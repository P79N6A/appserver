package com.idcq.appserver.dao.column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.column.ColumnDto;
/* 
 * @date 2015年3月4日
 * @time 下午3:57:36
 */
@Repository
public class ColumnDaoImpl extends BaseDao<ColumnDto>implements IColumnDao{
	
	public List<ColumnDto> getColumnList(ColumnDto column, int page,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return super.findList(generateStatement("getColumnList"), map);
	}

	public int getColumnListCount(ColumnDto column) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		return (Integer)super.selectOne(generateStatement("getColumnListCount"), map);
	}

	@Override
	public Integer getColumnIdByChildId(int columnId) {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("columnId", columnId);
		return (Integer)super.selectOne(generateStatement("getColumnIdByChildId"), paramMap);
	}

	@Override
	public List<ColumnDto> getMultiColumnByShopId(Long shopId) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId", shopId);
		return super.findList(generateStatement("getMultiColumnByShopId"),params);
	}

	@Override
	public List<ColumnDto> getMultiColumnByShopId(List<Long> shopIdList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopIdList", shopIdList);
		return super.findList(generateStatement("getMultiColumnByShopIdList"),params);
	}

	
	/**
	 * 根据columnId查找满足条件的column对象
	 * @Title: getColumnInfoByColumnId 
	 * @param @param columnIdList
	 * @param @return  
	 * @throws
	 */
	public List<ColumnDto> getColumnInfoByColumnId(List<String> columnIdList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("columnIdList", columnIdList);
		return super.findList(generateStatement("getColumnInfoByColumnId"),params);
	}

	@Override
	public void addColumnRelation(Long shopId, Integer columnId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("columnId", columnId);
		map.put("shopId", shopId);
		insert("insertColumnRelation", map);
	}

	@Override
	public void delColumnRelation(Long shopId) throws Exception {
		delete("delColumnRelation", shopId);
	}

	@Override
	public boolean hasChildrenColumn(Long parentColumnId) throws Exception {
		Integer count = (Integer)selectOne("hasChildrenColumn", parentColumnId);
		if(count != null && count > 0){
			return true;
		}
		return false;
	}
	
	
}
