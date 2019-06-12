package com.idcq.appserver.dao.shopMemberCard;

import java.util.List;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillNewDto;

/**
 * 店铺会员卡账单
 * @ClassName: IShopMemberCardBillDao 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午2:27:25 
 *
 */
public interface IShopMemberCardBillDao {
	
	/**
	 * 获取店铺会员卡账单
	 * @Title: getShopMemberCardBills 
	 * @param @param shopMemberCardBillDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel    返回类型 
	 * @throws
	 */
	public PageModel getShopMemberCardBills(
			ShopMemberCardBillDto shopMemberCardBillDto, PageModel pageModel)
			throws Exception;
	
	/**
	 * 插入用户账单
	 * @Title: insertShopMemberCardBill 
	 * @param @param shopMemberCardBillDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void insertShopMemberCardBill(ShopMemberCardBillDto shopMemberCardBillDto)throws Exception; 
	
	
	/**
	 * 批量插入次卡
	 * @Title: batchInsertShopMemberCardBill 
	 * @param @param shopMemberCardBillDtos
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void batchInsertShopMemberCardBill(List<ShopMemberCardBillDto>shopMemberCardBillDtos)throws Exception;

	public PageModel getShopMemberCardBillsNew(ShopMemberCardBillNewDto shopMemberCardBillDto, PageModel pageModel) throws Exception;
}
