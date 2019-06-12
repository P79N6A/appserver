package com.idcq.appserver.dao.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.idcq.appserver.dto.goods.ShopMemberCardGoods;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;

/** 
* @ClassName: IShopMemberCardGoodsDao 
* @Description: interface
* @author dengjihai
* @date 2016年2月17日 
*/ 
public interface IShopMemberCardGoodsDao {

	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	ShopMemberCardGoods selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	int deleteByPrimaryKey(@Param("id") Long id);


	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(ShopMemberCardGoods record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(ShopMemberCardGoods record);
	
	/**
	 * 批量使用
	 * @Title: batchUseTimesCard 
	 * @param @param shopMemberCardDtos
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void  batchUseTimesCard(List<ShopMemberCardDto>shopMemberCardDtos)throws Exception;
	
	void batchInsertShopMemberCardGoods(List<ShopMemberCardGoods> goodsList );
	
	/** 
	 * 通过goodsid，cardid查询出会员卡的次卡限定商品
	 * @param map
	 * @return
	 */
	public ShopMemberCardGoods getMemberCardGoods(Map<String, Object> map);
}
