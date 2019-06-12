package com.idcq.appserver.service.shopMemberCard;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;

public interface IShopMemberCardService {
	
	/**
	 * 操作店铺会员卡
	 * @Title: delShopMemberCardByIds 
	 * @param @param map
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void delShopMemberCardByIds(Map<String, Object> requestMap)throws Exception;
	
	/**
	 * 判断会员卡是否存在
	 * @Title: checkCardExist 
	 * @param @param shopId
	 * @param @param mobile
	 * @param @return
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean checkCardExist(Long shopId, String mobile)throws Exception;
	
	boolean checkCardExistByMid(Long memberId)throws Exception;
	
	/**
	 * 添加店铺会员卡
	 * @Title: insertShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public Integer insertShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	
	/**
	 * 充值店铺会员卡
	 * @Title: chargeShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public Map<String,Object> chargeShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 查询店铺会员卡列表
	 * @Title: queryShopMemberCardList 
	 * @param @param shopMemberCardDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel    返回类型 
	 * @throws
	 */
	public PageModel queryShopMemberCardList(ShopMemberCardDto shopMemberCardDto,PageModel pageModel)throws Exception;
	
	/**
	 * 店铺会员卡消费
	 * @Title: shopMemberCardComsume 
	 * @param @param shopMemberCardDto
	 * @param @param orderId
	 * @param @throws Exception
	 * @return Double  返回会员卡余额
	 * @throws
	 */
	public Double shopMemberCardComsume(ShopMemberCardDto shopMemberCardDto,String orderId)throws Exception;
	
	/**
	 * 修改店铺会员卡
	 * @Title: updateShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @return void    返回类型 
	 * @throws
	 */
	public void updateShopMemberCard(ShopMemberCardDto shopMemberCardDto)throws Exception;
	
	/**
	 * 店铺次卡消费
	 * @Title: shopTimeCardComsume 
	 * @param @param shopCardList
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void shopTimeCardComsume(List<ShopMemberCardDto>shopCardList)throws Exception;
}	
