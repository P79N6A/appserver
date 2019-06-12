package com.idcq.appserver.service.shopMemberCard;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillNewDto;

public interface IShopMemberCardBillService {
	
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
	public PageModel getShopMemberCardBills(ShopMemberCardBillDto shopMemberCardBillDto,PageModel pageModel)throws Exception;

	public PageModel getShopMemberCardBillsNew(ShopMemberCardBillNewDto shopMemberCardBillDto, PageModel pageModel) throws Exception;
}
