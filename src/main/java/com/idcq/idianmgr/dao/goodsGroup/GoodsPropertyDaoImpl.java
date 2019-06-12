package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsPropertyDto;
@Repository
public class GoodsPropertyDaoImpl extends BaseDao<GoodsPropertyDto> implements
		IGoodsPropertyDao {

	public int batchAddGoodsProperty(List<GoodsPropertyDto> list) {
		return this.insert(generateStatement("batchAddGoodsProperty"), list);
	}

	public int batchDelGoodsPropertyByGoodsId(List<Long> list)
			throws Exception {
		return this.delete(generateStatement("batchDelGoodsPropertyByGoodsId"), list);
	}

	public int addGoodsPropertyDto(GoodsPropertyDto goodsPropertyDto)
			throws Exception {
		return this.insert(generateStatement("addGoodsPropertyDto"), goodsPropertyDto);
	}

	public List<Long> getGoodsPropertyIdByGoodsIds(List<Long> list)
			throws Exception {
		return (List)this.findList(generateStatement("getGoodsPropertyIdByGoodsIds"), list);
	}
	
	/**
	 * 根据商品列表获取商品的属性列表
	 * @Title: getGoodsPropertyByGoodsIdList 
	 * @param @param goodsIdList
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<GoodsPropertyDto> getGoodsPropertyByGoodsIdList(
			List<Long> goodsIdList) throws Exception {
		return findList(generateStatement("getGoodsPropertyByGoodsIdList"),goodsIdList);
	}

	@Override
	public int delGoodsProperty(Map param) throws Exception {
		return this.delete(generateStatement("delGoodsProperty"), param);
	}

	@Override
	public List<Map> getGoodsProListByGoodsId(Long goodsId)
			throws Exception {
		return super.getSqlSession().selectList("getGoodsProListByGoodsId",goodsId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.idianmgr.dao.goodsGroup.IGoodsPropertyDao#getGoodsProperty(com.idcq.idianmgr.dto.goodsGroup.GoodsPropertyDto)
	 */
	@Override
	public List<GoodsPropertyDto> getGoodsProperty(
			GoodsPropertyDto goodsProperty) throws Exception {
		
		return findList("getGoodsProperty",goodsProperty);

	}

	
}
