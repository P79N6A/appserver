package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.GoodsAvsDto;

@Repository
public class GoodsAvsDaoImpl extends BaseDao<GoodsAvsDto> implements IGoodsAvsDao {

	@Override
	public List<GoodsAvsDto> getGoodsAvsList(GoodsAvsDto goodsAvs) {
		return (List)super.findList(generateStatement("getGoodsAvsList"),goodsAvs);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsAvsDao#insertGoodsAvsDto(com.idcq.appserver.dto.goods.GoodsAvsDto)
	 */
	@Override
	public int insertGoodsAvsDto(GoodsAvsDto goodsAvs) {
		return update("insertGoodsAvsDto", goodsAvs);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsAvsDao#updateGoodsAvsDto(com.idcq.appserver.dto.goods.GoodsAvsDto)
	 */
	@Override
	public int updateGoodsAvsDto(GoodsAvsDto goodsAvs) {
		
		return update("updateGoodsAvsDto", goodsAvs);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsAvsDao#queryGoodsAvs(com.idcq.appserver.dto.goods.GoodsAvsDto)
	 */
	@Override
	public int queryGoodsAvsCount(GoodsAvsDto goodsAvs) {
		
		return (int) selectOne("queryGoodsAvsCount", goodsAvs);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsAvsDao#deleteGoodsAvs(java.lang.Long)
	 */
	@Override
	public int deleteGoodsAvs(Long goodsAvsId) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("goodsAvsId", goodsAvsId);
		return delete("deleteGoodsAvs", parms);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsAvsDao#getGoodsAvsCount(com.idcq.appserver.dto.goods.GoodsAvsDto)
	 */
	@Override
	public int getGoodsAvsCount(GoodsAvsDto goodsAvs) {
		return (int) selectOne("getGoodsAvsCount", goodsAvs);
	}
}
