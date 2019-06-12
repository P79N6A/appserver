package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopTechnicianDto;
import com.idcq.appserver.dto.shop.ShopTechnicianRefGoodsDto;

/**
 * 技师dao
 * 
 * @author Administrator
 * 
 * @date 2015年7月29日
 * @time 下午5:51:46
 */
@Repository
public class ShopTechnicianDaoImpl extends BaseDao<ShopTechnicianDto> implements IShopTechnicianDao{

	@Override
	public int queryTechnicianExists(Long id) throws Exception {
		return (Integer)super.selectOne("queryTechnicianExists", id);
		
	}

	@Override
	public Integer insertShopTechnician(ShopTechnicianDto shopTechnicianDto)
			throws Exception {
		return super.insert(generateStatement("insertShopTechnician"), shopTechnicianDto);
	}

	@Override
	public Integer updateShopTechnician(ShopTechnicianDto shopTechnicianDto)
			throws Exception {
		return super.update(generateStatement("updateShopTechnician"), shopTechnicianDto);
	}

	@Override
	public Integer deleteShopTechnician(ShopTechnicianDto shopTechnicianDto)
			throws Exception {
		return super.delete(generateStatement("deleteShopTechnician"), shopTechnicianDto);
	}
	@Override
	public Integer insertShopTechRefGoods(List<ShopTechnicianRefGoodsDto> list)
			throws Exception {
		return super.insert(generateStatement("insertShopTechRefGoods"), list);
	}

	@Override
	public Integer updateShopTechRefGoods(ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception {
		return super.update(generateStatement("updateShopTechRefGoods"), shopTechnicianRefGoodsDto);
	}

	@Override
	public Integer deleteShopTechRefGoods(
			ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception {
		return super.delete(generateStatement("deleteShopTechRefGoods"), shopTechnicianRefGoodsDto);
	}	
	public Integer deleteBatcheShopTechRefGoods(
			List<Long> techIds)
			throws Exception {
		return super.delete(generateStatement("deleteBatcheShopTechRefGoods"), techIds);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#deleteBatcheShopTechRefGoods()
	 */
	@Override
	public Integer deleteBatchShopTechnician(Map<String,Object> map) throws Exception {
		return super.delete(generateStatement("deleteBatchShopTechnician"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianServiceItems(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getTechnicianServiceItems(
			Map<String, Object> map) {
		return (List)super.findList(generateStatement("getTechnicianServiceItems"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianServiceItemsCount(java.util.Map)
	 */
	@Override
	public Integer getTechnicianServiceItemsCount(Map<String, Object> map) {
		return (Integer) super.selectOne(generateStatement("getTechnicianServiceItemsCount"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getScheduleSetting(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getScheduleSetting(Map<String, Object> map)
			throws Exception {
		return (List)super.findList(generateStatement("getScheduleSetting"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#updateBatchShopTechnician(java.util.Map)
	 */
	@Override
	public Integer updateBatchShopTechnician(
			Map<String, Object> map) throws Exception {
		return super.update(generateStatement("updateBatchShopTechnician"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getTechnicianList(Map<String, Object> map)
			throws Exception {
		return (List)super.findList(generateStatement("getTechnicianList"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianListCount(java.util.Map)
	 */
	@Override
	public Integer getTechnicianListCount(Map<String, Object> map)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getTechnicianListCount"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getClassesTypeByTime(java.util.Date)
	 */
	@Override
	public Integer getClassesTypeByTime(String dateStr,Long shopId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serviceTime", dateStr);
		map.put("shopId", shopId);
		return (Integer) super.selectOne(generateStatement("getClassesTypeByTime"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechServiceCount(java.lang.Long)
	 */
	@Override
	public Integer getTechServiceCount(Long techId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getTechServiceCount"), techId);
	}

	@Override
	public String getTechnicianName(Long id) throws Exception {
		return (String)super.selectOne("getTechnicianName", id);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechWorkTimeByMap(java.util.Map)
	 */
	@Override
	public String getTechWorkTimeByMap(Map<String, Object> map)
			throws Exception {
		return (String) super.selectOne(generateStatement("getTechWorkTimeByMap"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getScheduleSettingCount(java.util.Map)
	 */
	@Override
	public Integer getScheduleSettingCount(Map<String, Object> map)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getScheduleSettingCount"), map);
	}

	@Override
	public int statisTechnicianOrderNumExecute(Date startTime, Date endTime)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return super.update(generateStatement("statisTechnicianOrderNumExecute"),map);
	}

	public int updateTechnicianWorkStatus(Map<String, Object> param)
			throws Exception {
		return super.update(generateStatement("updateTechnicianWorkStatus"), param);
	}

	public List<Long> getTechIdList(
			Map<String, Object> param) throws Exception {
		return (List)super.findList(generateStatement("getTechIdList"), param);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianOrderNum(java.util.Map)
	 */
	@Override
	public Map<String, Object> getTechnicianOrderNum(Map<String, Object> param)
			throws Exception {
		return (Map<String, Object>) super.selectOne(generateStatement("getTechnicianOrderNum"), param);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopTechnicianDao#getTechnicianById(java.lang.Long)
	 */
	@Override
	public Integer getTechWorkStatusById(Long technicianId)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getTechWorkStatusById"), technicianId);

	}

	@Override
	public Integer validateTechExit(Map<String, Object> map) throws Exception {
		return (Integer) this.selectOne(generateStatement("validateTechExit"), map);
	}		
	@Override
	public List<ShopTechnicianDto> getGoodTechs(String goodsId) {
		return this.findList("getGoodTechs",goodsId);
	}
	
}
