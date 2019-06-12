package com.idcq.appserver.service.shopMemberCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.shopMemberCard.IShopMemberCardBillDao;
import com.idcq.appserver.dao.shopMemberCard.IShopMemberCardDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillNewDto;

/**
 * 店铺会员卡账单业务逻辑层
 * @ClassName: ShopMemberCardBillServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午2:23:02 
 *
 */
@Service
public class ShopMemberCardBillServiceImp implements IShopMemberCardBillService{
	
	/**
	 * 店铺会员卡账单
	 */
	@Autowired
	private IShopMemberCardBillDao shopMemberCardBillDao;
	
	@Autowired
	private IShopMemberCardDao shopMemberCardDao;
	/**
	 * 获取店铺会员卡账单
	 * @Title: getShopMemberCardBills 
	 * @param @param shopMemberCardBillDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getShopMemberCardBills(
			ShopMemberCardBillDto shopMemberCardBillDto, PageModel pageModel)
			throws Exception {
		return shopMemberCardBillDao.getShopMemberCardBills(shopMemberCardBillDto, pageModel);
	}
	@Override
	public PageModel getShopMemberCardBillsNew(
			ShopMemberCardBillNewDto shopMemberCardBillDto, PageModel pageModel) throws Exception {
		return shopMemberCardBillDao.getShopMemberCardBillsNew(shopMemberCardBillDto, pageModel);
	}
	
	
	
	
}
