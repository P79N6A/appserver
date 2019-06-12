/**
 * 
 */
package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.GoodsSetDto;

/** 
 * @ClassName: GoodsSetDaoImp 
 * @Description: TODO(商品套餐数据访问层) 
 * @author 张鹏程 
 * @date 2015年4月18日 上午11:23:09 
 *  
 */
@Repository
public class GoodsSetDaoImp extends BaseDao<GoodsSetDto>implements IGoodsSetDao{

	/** 
	* @Title: getGoodsIdListByGoodsSetId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param goodsSetId
	* @param @return  
	* @throws 
	*/
	@Override
	public List<GoodsSetDto> getGoodsIdListByGoodsSetId(Long goodsSetId) {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("goodsSetId", goodsSetId);
		return super.findList(generateStatement("getGoodsIdByGoodsSetId"), paramMap);
	}

	@Override
	public void batchInsertGoodsSet(List<GoodsSetDto> goodsSetDtos)throws Exception{
		super.insert(generateStatement("batchInsertGoodsSet"), goodsSetDtos);
	}

	@Override
	public void batchUpdateGoodsSet(List<GoodsSetDto> goodsSetDtos) throws Exception {
		super.update(generateStatement("batchUpdateGoodsSet"),goodsSetDtos);
	}

	@Override
	public void deleteGoodsSet(Long goodsSetId) throws Exception {
		if (goodsSetId == null) {
			return;
		}
		super.update(generateStatement("deleteGoodsSet"),goodsSetId);
	}
	@Override
	public List<GoodsSetDto> getShopGoodsList(Map<String, Object> param) throws Exception {
		return super.findList(generateStatement("getShopGoodsList"),param);
	}

	@Override
	public List<GoodsSetDto> getGoodsSetList(Map<String, Object> param) throws Exception {
		return super.findList(generateStatement("getGoodsSetList"),param);
	}

	@Override
	public Integer getShopGoodsListCount(Map<String, Object> param) throws Exception {
		return (Integer) super.selectOne(generateStatement("getShopGoodsListCount"), param);
	}

}
