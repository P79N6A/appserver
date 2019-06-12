package com.idcq.appserver.service.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dao.goods.IShopGoodsDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.ShopGoodsDto;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;

@Service
public class ShopGoodsServiceImpl implements IShopGoodsService {

	@Autowired
	private IShopGoodsDao shopGoodsDao;
	
	@Autowired
	private IGoodsGroupDao goodsGroupDao;
	
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IGoodsSetDao goodsSetDao;
	
	public PageModel getPageShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy, Integer pageNo, Integer pageSize,String goodsServerMode) throws Exception{
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		page.setTotalItem(shopGoodsDao.getCountShopGoods(shopId, goodsCategoryList, orderBy,goodsServerMode));
		List<ShopGoodsDto> shopGoodsList = shopGoodsDao.getListShopGoods(shopId,goodsCategoryList, orderBy, pageNo, pageSize,goodsServerMode);
		page.setList(shopGoodsList);
		return page;
	}
	
	public ShopGoodsDto getDtoShopGoods(Long goodsId) throws Exception {
		ShopGoodsDto dto = shopGoodsDao.getDtoShopGoods(goodsId);
//		String goodsProValuesIds = dto.getGoodsProValuesIds();
//		StringBuilder goodsProValuesNameBuilder = new StringBuilder("");
//		if(goodsProValuesIds!=null){
//			String[] ids = goodsProValuesIds.split("\\,");
//			for (String id : ids) {
//				String propertyValue = shopGoodsDao.getDtoProValueName(id);
//				if(propertyValue!=null){
//					goodsProValuesNameBuilder.append(propertyValue+",");
//				}
//				
//			}
//		}
//		if(goodsProValuesNameBuilder!=null&&goodsProValuesNameBuilder.length()>0&&goodsProValuesNameBuilder.lastIndexOf(",")==goodsProValuesNameBuilder.length()-1){
//			dto.setGoodsProValuesName(goodsProValuesNameBuilder.substring(0, goodsProValuesNameBuilder.length()-1));
//		}
		return dto;
	}

	@Override
	public PageModel getShopGoodsByCondition(ShopGoodsDto shopGoodsDto,
			PageModel pageModel) throws Exception {
		PageModel page = new PageModel();
		List<ShopGoodsDto> shopGoodsList = shopGoodsDao.getListShopGoodsByCondition(shopGoodsDto,pageModel);
		shopGoodsList = updateResultList(shopGoodsList);
		page.setTotalItem(shopGoodsDao.getCountShopGoods(shopGoodsDto,pageModel));
		page.setList(shopGoodsList);
		return page;
	}
	private List<ShopGoodsDto> updateResultList(List<ShopGoodsDto> shopGoodsList){
		
		List<ShopGoodsDto> newGoodsList = new ArrayList<ShopGoodsDto>();
		
		if(CollectionUtils.isNotEmpty(shopGoodsList)){
			for (ShopGoodsDto shopGoodsDto : shopGoodsList) {
				List<GoodsSetDto> goodsSetList = goodsSetDao.getGoodsIdListByGoodsSetId(shopGoodsDto.getGoodsId());
				if(CollectionUtils.isNotEmpty(goodsSetList)){
					shopGoodsDto.setGoodsSetList(goodsSetList);
				}
				newGoodsList.add(shopGoodsDto);
			}
		}
		return shopGoodsList;
	}
	
	@Override
	public Map<String, Object> getShopGoodsStatisticsByCondition(
			ShopGoodsDto shopGoodsDto, PageModel pageModel) {
		return shopGoodsDao.getShopGoodsStatisticsByCondition(shopGoodsDto,pageModel);
	}

	/**
	 * 获取商铺商品族的条数
	 * @Title: getGoodsGroupCountByShopId 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public Integer getGoodsGroupCountByShopId(Long shopId) throws Exception {
		return goodsGroupDao.getGoodsGroupCountByShopId(shopId);
	}

	@Override
	public Map<String,Object> getAttachment(Long goodsId,
			Integer bizTypeIsGoods, Integer picTypeIsCyclePlay) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		List<AttachmentRelationDto> attachmentRelationDtos = getAttachmentRelationDto(goodsId,bizTypeIsGoods,picTypeIsCyclePlay);
		if(!CollectionUtils.isEmpty(attachmentRelationDtos)){
			StringBuffer buffer = new StringBuffer();
			StringBuffer ids = new StringBuffer();
			int i = 0;
			for(AttachmentRelationDto att: attachmentRelationDtos){
				buffer.append(FdfsUtil.getFileProxyPath(att.getFileUrl()));
				ids.append(att.getAttachmentId());
				i++;
				if(i < attachmentRelationDtos.size()){
					buffer.append(",");
					ids.append(",");
				}
			}
			map.put("urls", buffer.toString());
			map.put("ids", ids.toString());
		}
		return map;
	}
	
	private List<AttachmentRelationDto> getAttachmentRelationDto(Long bizId, Integer bizType, Integer picType) throws Exception {
		AttachmentRelationDto attachmentRelationDto=new AttachmentRelationDto();
		attachmentRelationDto.setBizId(bizId);
		attachmentRelationDto.setBizType(bizType);
		attachmentRelationDto.setPicType(picType);
		return attachmentRelationDao.findByCondition(attachmentRelationDto);
	}
	
	public List<Long> getGoodsGroupIdByShopId(Long shopId) throws Exception {
        return goodsGroupDao.getGoodsGroupIdByShopId(shopId);
	}
}
