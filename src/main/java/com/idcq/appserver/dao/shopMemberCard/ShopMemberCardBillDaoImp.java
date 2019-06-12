package com.idcq.appserver.dao.shopMemberCard;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillNewDto;
/**
 * 店铺会员卡账单
 * @ClassName: ShopMemberCardBillDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午2:28:21 
 *
 */
@Repository
public class ShopMemberCardBillDaoImp extends BaseDao<ShopMemberCardBillDto> implements IShopMemberCardBillDao{
	
	
	/**
	 * 查询店铺会员卡账单
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
		pageModel.setList(super.findList(generateStatement("getShopMemberCardBills"),shopMemberCardBillDto));
		pageModel.setTotalItem(getShopMemberCardBillCount(shopMemberCardBillDto));
		return pageModel;
	}
	
	/**
	 * 获得会员卡数量 
	 * @Title: getShopMemberCardCount 
	 * @param @param shopMemberCardDto
	 * @param @return
	 * @param @throws Exception
	 * @return long    返回类型 
	 * @throws
	 */
	public Integer getShopMemberCardBillCount(ShopMemberCardBillDto shopMemberCardBillDto)throws Exception{
		Integer count=(Integer) super.selectOne(generateStatement("getShopMemberCardBillCount"), shopMemberCardBillDto);
		return count==null?0:count;
	}
	
	/**
	 * 插入店铺会员卡账单
	 * @Title: insertShopMemberCardBill 
	 * @param @param shopMemberCardBillDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void insertShopMemberCardBill(ShopMemberCardBillDto shopMemberCardBillDto)throws Exception{
		shopMemberCardBillDto.setBillTime(new Date());
		super.insert(generateStatement("insertShopMemberCardBill"), shopMemberCardBillDto);
	}
	
	/**
	 * 批量插入店铺会员卡
	 * @Title: batchInsertShopMemberCardBill 
	 * @param @param shopMemberCardBillDtos
	 * @param @throws Exception  
	 * @throws
	 */
	public void batchInsertShopMemberCardBill(
			List<ShopMemberCardBillDto> shopMemberCardBillDtos)
			throws Exception {
		Map<String,Object>param=new HashMap<String,Object>();
		param.put("shopMemberCardBillDtos",shopMemberCardBillDtos);
		super.insert(generateStatement("batchInsertShopMemberCardBill"),param);	
	}

	@Override
	public PageModel getShopMemberCardBillsNew(
			ShopMemberCardBillNewDto shopMemberCardBillDto, PageModel pageModel) throws Exception {
		pageModel.setList(super.findList(generateStatement("getShopMemberCardBillsNew"),shopMemberCardBillDto));
		pageModel.setTotalItem(getShopMemberCardBillCountNew(shopMemberCardBillDto));
		return pageModel;
	}
	
	public Integer getShopMemberCardBillCountNew(ShopMemberCardBillNewDto shopMemberCardBillDto)throws Exception{
		Integer count=(Integer) super.selectOne(generateStatement("getShopMemberCardBillCountNew"), shopMemberCardBillDto);
		return count==null?0:count;
	}

}
