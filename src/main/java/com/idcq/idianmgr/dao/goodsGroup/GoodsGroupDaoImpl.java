package com.idcq.idianmgr.dao.goodsGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.VerifyGoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;

/**
 * GoodsGroupDaoImpl
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午8:30:12
 */
@Repository
public class GoodsGroupDaoImpl extends BaseDao<GoodsGroupDto> implements IGoodsGroupDao{
	public List<Map> getGoodsListByGroupId(Map<String, Object> param) {
		return (List)this.findList(generateStatement("getGoodsListByGroupId"), param);
	}

	public List<Map> getGoodsPropertyListByGoodsIds(List<Map> list) throws Exception {
		return (List)this.findList(generateStatement("getGoodsPropertyListByGoodsIds"), list);
	}

	@Override
	public Integer queryGoodsGroupLastSequence() throws Exception {
		return (Integer)super.selectOne(generateStatement("queryGoodsGroupLastSequence"), null);
	}
	public Long addGoodsGroup(GoodsGroupDto dto) throws Exception {
		super.insert("addGoodsGroup",dto);
		Long goodsGroupId = dto.getGoodsGroupId();
		return goodsGroupId;
	}

	@Override
	public int updateGoodsGroup(GoodsGroupDto dto) throws Exception {
		return super.update("updateGoodsGroup",dto);
	}

	@Override
	public Double getGoodsGroupMaxPrice(Long goodsGroupId) throws Exception {
		return (Double)super.selectOne(generateStatement("getGoodsGroupMaxPrice"), goodsGroupId);
	}
	
	@Override
	public Double getGoodsGroupMinPrice(Long goodsGroupId) throws Exception {
		return (Double)super.selectOne(generateStatement("getGoodsGroupMinPrice"), goodsGroupId);
	}
	
	public List<Long> getGoodsIdsByGroupIdAndShopId(Map<String,Object> param) throws Exception  {
		return (List)this.findList(generateStatement("getGoodsIdsByGroupIdAndShopId"), param);
	}

	public GoodsGroupDto getGoodsGroupById(Long goodsGroupId) throws Exception  {
		return (GoodsGroupDto) this.selectOne(generateStatement("getGoodsGroupById"), goodsGroupId);
	}

	public int updateGoodsGroupStatusBatch(List<Map<String,Object>> list)
			throws Exception {
		return super.update("updateGoodsGroupStatusBatch",list);
	}

	public int updateGoodsGroupStatus(Map<String, Object> map) throws Exception {
		return super.update("updateGoodsGroupStatus",map);
	}

	@Override
	public List<GoodsGroupDto> getGoodsByPageAndLastUpdateTime(
			String lastUpdateTime, PageModel pageModel) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("lastUpdateTime", lastUpdateTime);
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		return super.findList(generateStatement("getGoodsByPageAndLastUpdateTime"),params);
	}

	@Override
	public List<GoodsGroupDto> findGoodsGroupByIdList(List idList)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("groupIdList", idList);
		return super.findList(generateStatement("findGoodsGroupByIdList"),params);
	}

	@Override
	public PageModel getGoodsGroupListForSearch(GoodsDto goods, PageModel pageModel) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("goods", goods);
		map.put("start", pageModel.getPageSize()*(pageModel.getToPage()-1));
		map.put("limit", pageModel.getPageSize());
		List<GoodsGroupDto>goodsGroupList= findList(generateStatement("getGoodsGroupListForSearch"), map);
		pageModel.setList(goodsGroupList);
		return pageModel;
	}

	public int queryValidOrderByGgId(Long ggId) throws Exception {
		return (int)super.selectOne("queryValidOrderByGgId", ggId);
	}

	public int delGoodsGroupById(Long ggId) throws Exception {
		return super.delete("delGoodsGroupById", ggId);
	}

	public int delGoodsGroupBatch(List<Map<String,Object>> list) throws Exception {
		return super.delete("delGoodsGroupBatch", list);
	}

	public List<GoodsGroupDto> getGoodsGroupList(Long shopId, String goodsKey, Integer status,int pNo,
			int pSize) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId",shopId );
		if(!StringUtils.isBlank(goodsKey)){
			map.put("goodsKey", "%"+goodsKey+"%");
		}
		if(status != null){
			map.put("goodsStatus", status);
		}
		map.put("pNo", (pNo-1)*pSize);
		map.put("pSize", pSize);
//		GGroupQueyDto q = new GGroupQueyDto();
//		q.setShopId(shopId);
//		q.setGoodsKey(goodsKey);
//		q.setGoodsStatus(status);
//		q.setpNo(pNo);
//		q.setpSize(pSize);
		return super.findList("getGoodsGroupList",map);
	}

	public int getGoodsGroupCount(Long shopId, String goodsKey,Integer status)
			throws Exception {
//		GGroupQueyDto q = new GGroupQueyDto();
//		q.setShopId(shopId);
//		q.setGoodsKey(goodsKey);
//		q.setGoodsStatus(status);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId",shopId );
		if(!StringUtils.isBlank(goodsKey)){
			map.put("goodsKey", "%"+goodsKey+"%");
		}
		if(status != null){
			map.put("goodsStatus", status);
		}
		return (int)super.selectOne("getGoodsGroupCount", map);
	}

	public GoodsGroupDto getGoodsGroupBycategoryIdAndShopId(
			Map<String, Object> param) {
		return (GoodsGroupDto) this.selectOne(generateStatement("getGoodsGroupBycategoryIdAndShopId"), param);
	}

	public int queryTechniExists(Long id) throws Exception {
		return (int)super.selectOne("queryTechniExists", id);
	}
	
	/**
	 * 获取草稿状态的集合
	 * @return
	 * @throws Exception
	 */
	public List<GoodsGroupDto> getDriftGoodsGroupList() throws Exception {
		return super.findList("getDriftGoodsGroupList");
	}

	@Override
	public int delGoodsBatchByGoodsGroupId(List<Map<String, Object>> list)
			throws Exception {
		return super.delete("delGoodsBatchByGoodsGroupId", list);
	}

	public int queryGoodsGroupExists(Long id) throws Exception {
		return (int)super.selectOne("queryGoodsGroupExists", id);
	}

	@Override
	public int queryGoodsExistsByGgId(Long ggId) throws Exception {
		return (int)super.selectOne("queryGoodsExistsByGgId", ggId);
	}

	@Override
	public int queryGgPropertyByGgId(Long ggId) throws Exception {
		return (int)super.selectOne("queryGgPropertyByGgId", ggId);
	}
	
	/**
	 * 根据商铺id获取商品族的个数
	 * @Title: getGoodsGroupCountByShopId 
	 * @param @param shopId
	 * @param @return  
	 * @throws
	 */
	public Integer getGoodsGroupCountByShopId(Long shopId) {
		return (Integer)super.selectOne("getGoodsGroupCountByShopId", shopId);
	}
	
	/**
	 * 根据id获取商品族列表 
	 * @Title: getGoodsGroupListByCondition 
	 * @param @param goodsGroupDto
	 * @param @param pageModel
	 * @param @return  
	 * @throws
	 */
	public PageModel getGoodsGroupListByCondition(GoodsGroupDto goodsGroupDto,
			PageModel pageModel) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("start",pageModel.getPageSize()*(pageModel.getToPage()-1));
		params.put("limit", pageModel.getPageSize());
		params.put("goodsServerModel",goodsGroupDto.getGoodsServerModeParam());
		if(goodsGroupDto.getCategoryIdList()!=null&&goodsGroupDto.getCategoryIdList().size()>0)
		{
			params.put("categoryIdList", goodsGroupDto.getCategoryIdList());
		}
		params.put("goodsStatus", goodsGroupDto.getGoodsStatus());
		params.put("shopId", goodsGroupDto.getShopId());
		params.put("orderBy",goodsGroupDto.getOrderBy());
		return findPagedList(generateStatement("getGoodsGroupListByCondition"), generateStatement("getGoodsGroupCountByCondition"), params);
	}

	@Override
	public List<GoodsGroupDto> getListByShopIdListAndNum(
			List<Long> needQueryShopIdForGoods, int num) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("list", needQueryShopIdForGoods);
		params.put("num", num);
		return findList(generateStatement("getListByShopIdListAndNum"),params);
	}

	@Override
	public int updateGoodsGroupZan(Long goodsId) throws Exception {
		return super.update(generateStatement("updateGoodsGroupZan"), goodsId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao#getGoodsGroup(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<GoodsGroupDto> getGoodsGroup(VerifyGoodsGroupDto vrifyGoodsGroupDto)
			throws Exception {
		return (List<GoodsGroupDto>) this.findList(generateStatement("getGoodsGroup"), vrifyGoodsGroupDto);
	}

    @Override
    public List<Long> getGoodsGroupIdByShopId(Long shopId) throws Exception {
        return super.getSqlSession().selectList(generateStatement("getGoodsGroupIdByShopId"), shopId);
    }
}
