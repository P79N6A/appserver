package com.idcq.appserver.dao.shopMemberCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.utils.FdfsUtil;

/**
 * 店铺会员卡数据访问层
 * @ClassName: ShopMemberCardDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午2:09:45 
 *
 */
@Repository
public class ShopMemberCardDaoImp extends BaseDao<ShopMemberCardDto> implements IShopMemberCardDao{
	
	@Autowired
	IGoodsSetDao goodsSetDao;
	
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	/**
	 * 添加会员卡
	 * @Title: insertShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception  
	 * @throws
	 */
	public Integer insertShopMemberCard(ShopMemberCardDto shopMemberCardDto)
			throws Exception {
		return super.insert(generateStatement("insertShopMemberCard"), shopMemberCardDto);
	}	

	/**
	 * 店铺会员卡充值
	 * @Title: chargeShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception  
	 * @throws
	 */
	public void chargeShopMemberCard(ShopMemberCardDto shopMemberCardDto)
			throws Exception {
		super.update(generateStatement("chargeShopMemberCard"), shopMemberCardDto);
	}	
	
	/**
	 * 获取店铺会员卡
	 * @Title: getShopMemberCards 
	 * @param @param shopMemberCardDto
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getShopMemberCards(ShopMemberCardDto shopMemberCardDto,
			PageModel pageModel) throws Exception {
		int start=(pageModel.getToPage()-1)*pageModel.getPageSize();
		int limit=pageModel.getPageSize();
		shopMemberCardDto.setStart(start);
		shopMemberCardDto.setLimit(limit);
		List<ShopMemberCardDto> shopMemberCardDtos=getShopMemberCardList(shopMemberCardDto);
		Map<String, Object> param= new HashMap<String, Object>();
		for (ShopMemberCardDto shopMemberCardDtoInfo : shopMemberCardDtos) {
			param.put("shopId", shopMemberCardDtoInfo.getShopId());
			param.put("goodsId", shopMemberCardDtoInfo.getGoodsSetId());
			shopMemberCardDtoInfo.setGoodsList(goodsSetDao
					.getGoodsSetList(param));
			AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
			attachmentRelationDto.setBizId(shopMemberCardDtoInfo.getShopId());
			attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_SHOP);
			attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
			List<AttachmentRelationDto> attachmentRelationList = attachmentRelationDao
					.findByCondition(attachmentRelationDto);
			if(CollectionUtils.isNotEmpty(attachmentRelationList)){
				shopMemberCardDtoInfo.setShopLogoUrl(FdfsUtil.getFileProxyPath(attachmentRelationList.get(0).getFileUrl()));
			}
		}
		pageModel.setList(shopMemberCardDtos);
		pageModel.setTotalItem(getShopMemberCardCount(shopMemberCardDto));
		return pageModel;
	}

	
	/**
	 * 查询店铺会员卡列表
	 * @Title: getShopMemberCardList 
	 * @param @param shopMemberCardDto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopMemberCardDto> getShopMemberCardList(
			ShopMemberCardDto shopMemberCardDto) throws Exception {
		if(shopMemberCardDto.getCardType()!=null && shopMemberCardDto.getCardType().equals("2")){
			return super.findList(generateStatement("getShopTimeCardList"),shopMemberCardDto);
		}else{
			return super.findList(generateStatement("getShopMemberCardList"),shopMemberCardDto);
		}
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
	public Integer getShopMemberCardCount(ShopMemberCardDto shopMemberCardDto)throws Exception{
		if(shopMemberCardDto.getCardType()!=null && shopMemberCardDto.getCardType().equals("2")){
			Integer count=(Integer) super.selectOne(generateStatement("getShopTimeCardListCount"), shopMemberCardDto);
			return count==null?0:count;
		}else{
			Integer count=(Integer) super.selectOne(generateStatement("getShopMemberCardCount"), shopMemberCardDto);	
			return count==null?0:count;
		}
	}
	
	/**
	 * 消费店铺会员卡
	 * @Title: consumeShopMemberCard 
	 * @param @param shopMemberCardDto
	 * @param @throws Exception  
	 * @throws
	 */
	public void consumeShopMemberCard(ShopMemberCardDto shopMemberCardDto)
			throws Exception {
		super.update(generateStatement("consumeShopMemberCard"), shopMemberCardDto);	
	}

	/**
	 * 修改店铺会员卡
	 * @Title: updateShopMemberCard 
	 * @param @param shopMemberCardDto  
	 * @throws
	 */
	public void updateShopMemberCard(ShopMemberCardDto shopMemberCardDto) {
		if(null == shopMemberCardDto){
			return;
		}
		if(StringUtils.isBlank(shopMemberCardDto.getName()) && null == shopMemberCardDto.getBirthday() && null == shopMemberCardDto.getSex())
		{
			return;
		}
		super.update(generateStatement("updateShopMemberCard"), shopMemberCardDto);	
	}

    @Override
    public void delShopMemberCardByIds(Map<String, Object> requestMap) throws Exception {
        super.delete(generateStatement("delShopMemberCardByIds"), requestMap);
    }

    @Override
    public ShopMemberCardDto getShopMemberCardById(Integer cardId) throws Exception {
        return (ShopMemberCardDto) super.selectOne(generateStatement("getShopMemberCardById"), cardId);
    }
	
}
