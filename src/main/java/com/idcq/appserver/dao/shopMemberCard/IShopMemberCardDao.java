package com.idcq.appserver.dao.shopMemberCard;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;

public interface IShopMemberCardDao {
	
    
    void delShopMemberCardByIds(Map<String, Object> requestMap) throws Exception;
	/**
	 * 增加店铺会员卡
	 * @Title: insertShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public Integer insertShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 会员卡充值
	 * @Title: chargeShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void chargeShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 获取店铺会员卡
	 * @Title: getShopMemberCards 
	 * @param @param shopMemberCardDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel    返回类型 
	 * @throws
	 */
	public PageModel getShopMemberCards(ShopMemberCardDto shopMemberCardDto,PageModel pageModel)throws Exception;
	
	/**
	 * 查询店铺会员卡列表 
	 * @Title: getShopMemberCardList 
	 * @param @param shopMemberCardDto
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopMemberCardDto>    返回类型 
	 * @throws
	 */
	public List<ShopMemberCardDto>getShopMemberCardList(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 店铺会员卡消费
	 * @Title: consumeShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void consumeShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 修改店铺会员卡
	 * @Title: updateShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @return void    返回类型 
	 * @throws
	 */
	public void updateShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	public Integer getShopMemberCardCount(ShopMemberCardDto shopMemberCardDto) throws Exception;
	
	public ShopMemberCardDto getShopMemberCardById(Integer cardId)throws Exception;
	
}
