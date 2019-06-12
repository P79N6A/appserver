package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.ShopMemberCardGoods;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;

/** 
* @ClassName: ShopMemberCardGoodsDaoImpl 
* @Description: 次卡限定商品表DaoImpl
* @author dengjihai
* @date 2016年2月17日 
*/ 
@Repository
public class ShopMemberCardGoodsDaoImpl  extends BaseDao<ShopMemberCardGoods> implements IShopMemberCardGoodsDao{

	@Override
	public ShopMemberCardGoods selectByPrimaryKey(Long id) {
		return (ShopMemberCardGoods) super.selectOne(generateStatement("selectByPrimaryKey"), id);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.delete(generateStatement("deleteByPrimaryKey"), id);
	}

	@Override
	public int insertSelective(ShopMemberCardGoods record) {
		return super.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(ShopMemberCardGoods record) {
		return super.update(record);
	}
	
	/**
	 * 批量使用次卡
	 * @Title: batchUseTimesCard 
	 * @param @param shopMemberCardDtos
	 * @param @throws Exception  
	 * @throws
	 */
	public void  batchUseTimesCard(List<ShopMemberCardDto>shopMemberCardDtos)throws Exception{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("cardGoodsList", shopMemberCardDtos);
		super.update(generateStatement("batchUseTimesCard"), params);
	}

	@Override
	public void batchInsertShopMemberCardGoods(List<ShopMemberCardGoods> goodsList) {
		super.insert(generateStatement("batchInsertShopMemberCardGoods"),goodsList);
	}

	@Override
	public ShopMemberCardGoods getMemberCardGoods(
			Map<String, Object> map) {
		return (ShopMemberCardGoods) super.selectOne(generateStatement("getMemberCardGoods"),map);
	}

}
