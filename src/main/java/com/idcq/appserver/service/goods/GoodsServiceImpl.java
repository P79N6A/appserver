package com.idcq.appserver.service.goods;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.AsynchronousTask.producer.MqPusher;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.GoodsSetDaoImp;
import com.idcq.appserver.dao.goods.IGoodsAvsDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dao.goods.IGoodsSoldOutDao;
import com.idcq.appserver.dao.goods.IShopGoodsPropertyDao;
import com.idcq.appserver.dao.goods.IShopMemberCardGoodsDao;
import com.idcq.appserver.dao.goods.ITopGoodsDao;
import com.idcq.appserver.dao.goods.IUserGoodsCommentDao;
import com.idcq.appserver.dao.level.ILevelDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsAvsDto;
import com.idcq.appserver.dto.goods.GoodsBarcodeDto;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.GoodsSoldOutDto;
import com.idcq.appserver.dto.goods.GoodsUnitDto;
import com.idcq.appserver.dto.goods.GroupPropertyModel;
import com.idcq.appserver.dto.goods.ShopGoodsPropertyDto;
import com.idcq.appserver.dto.goods.SyncGoodsDto;
import com.idcq.appserver.dto.goods.SyncGoodsInfoDto;
import com.idcq.appserver.dto.goods.TopGoodsDto;
import com.idcq.appserver.dto.goods.UpdateGoodsAvs;
import com.idcq.appserver.dto.goods.UserGoodsCommentDto;
import com.idcq.appserver.dto.goods.VerifyGoodsGroupDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.goods.enums.ChainStoresTypeEnum;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.excel.ExcelParser;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.pinyin.PinyinUtil;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupCategoryRelationDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupProValuesDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupPropertyDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsPropertyDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsPropertyDto;


/**
 * 商品（服务）service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:37:31
 */
@Service
public class GoodsServiceImpl implements IGoodsServcie{
	private final static Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
	@Autowired
	public IGoodsDao goodsDao;
	@Autowired
	public IGoodsGroupDao goodsGroupDao;
	@Autowired
	public ITopGoodsDao topGoodsDao;
	@Autowired
	public IUserGoodsCommentDao userGoodsCommentDao;
	@Autowired
	public IGoodsCategoryDao goodsCategoryDao;
	@Autowired
	public ILevelDao levelDao;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IGoodsSetDao goodsSetDao;
	@Autowired
	private IShopMemberCardGoodsDao shopMemberCardGoodsDao;
	@Autowired
	private ICollectDao collectDao;
    @Autowired
    private IStorageServcie storageServcie;
    @Autowired
    private ILevelService levelService;
    @Autowired
    private ICollectService collectService;
    @Autowired
    private IGoodsGroupPropertyDao goodsGroupPropertyDao;
    @Autowired
    private IGoodsGroupProValuesDao goodsGroupProValuesDao;
    @Autowired
    private IGoodsPropertyDao goodsPropertyDao;
    @Autowired
    private IGoodsGroupCategoryRelationDao goodsGroupCategoryRelationDao;
	@Autowired
	private IGoodsSoldOutDao goodsSoldOutDao;
    @Autowired
    private IShopGoodsPropertyDao shopGoodsPropertyDao;
    @Autowired
    private IGoodsAvsDao goodsAvsDao;
    
	public PageModel getTopGoodsList(TopGoodsDto topGoods, Integer page,
			Integer pageSize) throws Exception {
		PageModel pm = new PageModel();
		pm.setTotalItem(this.topGoodsDao.getTopGoodsListCount(topGoods));
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		List<TopGoodsDto> list = this.topGoodsDao.getTopGoodsList(topGoods, page, pageSize);
		String logo1 = null;
		String logo2 = null;
		if(list != null && list.size() > 0){
			for(TopGoodsDto e : list){
				logo1 = e.getGoodsLogo1();
				logo2 = e.getGoodsLogo2();
				if(!StringUtils.isBlank(logo1)){
					e.setGoodsLogo1(FdfsUtil.getFileProxyPath(logo1));
				}
				if(!StringUtils.isBlank(logo2)){
					e.setGoodsLogo1(FdfsUtil.getFileProxyPath(logo2));
				}
			}
		}
		pm.setList(list);
		return pm;
	}

	public void updateGoods(Map<String, Object> requestMap,GoodsDto goodsDto,
			GoodsGroupDto goodsGroupDto, List<GroupPropertyModel> groupPropertysModel,
			List<GoodsSetDto> goodsSetList) throws Exception {
		
		ShopDto shop = shopDao.getShopById(goodsDto.getShopId());
	    SyncGoodsDto needSyncGoods = new SyncGoodsDto();
	    needSyncGoods.setShopId(shop.getShopId());
	    List<Long> syncGoodsIdList = new LinkedList<Long>();
	    needSyncGoods.setGoodsList(syncGoodsIdList);
	    
	    if (CollectionUtils.isNotEmpty(goodsSetList)) {
	    	Double originalPrice = Double.valueOf(0);
	    	for (GoodsSetDto goodsSet : goodsSetList) {
	    		originalPrice = NumberUtil.add(originalPrice, NumberUtil.multiply(goodsSet.getPrice().doubleValue(), goodsSet.getGoodsNumber()));
	    	}
	    	
	    	goodsDto.setOriginalPrice(originalPrice);
	    	goodsDto.setGoodsType(3000);//套餐
	    }
	    
	    if (goodsDto.getGoodsId() == null) {
			//操作多规格商品
			if(CollectionUtils.isNotEmpty(groupPropertysModel)){
				/**
				 * 1、增加商品族，建立分类与商品族关系
				 * 2、增加规格属性名称
				 * 3、增加规格属性值
				 * 4、增加商品
				 * 5、增加商品属性关系
				 */
				
				logger.info("增加多规格商品");
				//1、增加商品族
				Boolean updateGoodsGroup = goodsGroupDto.getGoodsGroupId() == null ? false : true;
				
				Long goodsGroupId = addGoodsGroup(goodsGroupDto);
			    
				Set<Long> updateGoodsSet = new HashSet<Long>();
				List<Long> updateGoodsPropertyValueNotSelectList = new LinkedList<Long>();
				
				if (updateGoodsGroup) {
					for (GroupPropertyModel groupPropertyModel : groupPropertysModel) {
						Long updateGoodsId = groupPropertyModel.getGoodsId();
						if (updateGoodsId != null) {
							updateGoodsSet.add(updateGoodsId);
						}
					}
					
					List<Long> needDeleteGoodsList = new LinkedList<Long>();
					
					Map<String, Object> parms = new HashMap<String, Object>();
					parms.put("shopId", goodsDto.getShopId());
					parms.put("goodsGroupId", goodsGroupId);
					List<GoodsDto> goodsGroupGoodsList = goodsDao.getGoodsByGroupMap(parms);
					
					for (GoodsDto goods : goodsGroupGoodsList) {
						if (!updateGoodsSet.contains(goods.getGoodsId())) {
							needDeleteGoodsList.add(goods.getGoodsId());
							if (StringUtils.isNotBlank(goods.getGoodsProValuesIds())) {
								for (String proValueId : goods.getGoodsProValuesIds().split(",")) {
									updateGoodsPropertyValueNotSelectList.add(Long.valueOf(proValueId));
								}
							}
						}
					}
					
					if (CollectionUtils.isNotEmpty(needDeleteGoodsList)) {
						goodsDao.updateGoodsInfoStatus(goodsDto.getShopId(), needDeleteGoodsList, 2);
						goodsPropertyDao.batchDelGoodsPropertyByGoodsId(needDeleteGoodsList);
						goodsGroupProValuesDao.updateIsNotSelectByIds(updateGoodsPropertyValueNotSelectList);
					}
					
				}
		
				//TODO 操作商品族，增加操作类型情况处理，后面考虑优化
	 			for (GroupPropertyModel groupPropertyModel : groupPropertysModel) {
					
					//参数举例==》规格属性名称：颜色,尺寸     规格属性值：白色,XL
					String groupPropertyNames = groupPropertyModel.getGroupPropertyName();
					String proValuesName = groupPropertyModel.getProValuesName();
					String shopPropertyIds = groupPropertyModel.getShopPropertyIds();

					String[] groupPropertyNameArr = groupPropertyNames.split(",");
					String[] proValuesNameArr = proValuesName.split(",");
			
					StringBuffer groupPropertyIds = new StringBuffer();	
					StringBuffer goodsProValuesIds = new StringBuffer();
					int arrLength = groupPropertyNameArr.length;
					String[] shopPropertyIdArr = new String[0];
					if (StringUtils.isNotBlank(shopPropertyIds)) {
						shopPropertyIdArr = shopPropertyIds.split(",");
					}
					
					int shopPropertyLength = shopPropertyIdArr.length - 1;
					
					for (int i = 0; i < arrLength; i++) {
						//2、增加属性名称，同一商品族下的属性名称不能重复
						Long groupPropertyId = addGoodsGroupProBackId(goodsGroupId, groupPropertyNameArr[i]
													,i <= shopPropertyLength? Long.valueOf(shopPropertyIdArr[i]) : null);
						groupPropertyIds.append(groupPropertyId);
						if(i!=arrLength-1){
							groupPropertyIds.append(",");
						}
						
						//3、增加属性值，同一个属性值下面属性值不能重复
						Long proValuesId = addGoodsGoodsGroupProValues(groupPropertyId, proValuesNameArr[i]);
						goodsProValuesIds.append(proValuesId);
						if(i!=arrLength-1){
							goodsProValuesIds.append(",");
						}
						
					}

					//4、增加商品
					goodsGroupDto.setGoodsGroupId(goodsGroupId);
					goodsDto.setGoodsGroupId(goodsGroupId);
					goodsDto.setGoodsProValuesIds(sortArray(goodsProValuesIds.toString()));
					goodsDto.setGoodsProValuesNames(groupPropertyModel.getProValuesName());
					
					goodsDto.setAlarmNumberMax(groupPropertyModel.getAlarmNumberMax());
					goodsDto.setAlarmNumberMin(groupPropertyModel.getAlarmNumberMin());
					goodsDto.setStandardPrice(groupPropertyModel.getStandardPrice());
					goodsDto.setGoodsNo(groupPropertyModel.getGoodsNo());;
					goodsDto.setBarcode(groupPropertyModel.getBarcode());
					if (groupPropertyModel.getGoodsId() == null) {
						goodsDto.setStorageTotalNumber(groupPropertyModel.getStorageTotalNumber());
					}else {
						goodsDto.setStorageTotalNumber(null);
					}
					goodsDto.setGoodsId(groupPropertyModel.getGoodsId());
					Long goodsId  = updateSingleGoods(requestMap, goodsDto, goodsGroupDto, groupPropertysModel);
					syncGoodsIdList.add(goodsId);
					//5、增加商品属性关联关系，多条记录
					addGoodsPropertyDto(groupPropertyIds.toString(), goodsProValuesIds.toString(), goodsId);
				}
				
			}
			//原来操作商品逻辑
			else{
				logger.info("增加非多规格商品");
				Long goodsId  = updateSingleGoods(requestMap, goodsDto, goodsGroupDto, groupPropertysModel);
				syncGoodsIdList.add(goodsId);
			}
	    }else {
	    	if(CollectionUtils.isNotEmpty(groupPropertysModel)){
	    		for (GroupPropertyModel groupPropertyModel : groupPropertysModel) {
					goodsDto.setAlarmNumberMax(groupPropertyModel.getAlarmNumberMax());
					goodsDto.setAlarmNumberMin(groupPropertyModel.getAlarmNumberMin());
					goodsDto.setStandardPrice(groupPropertyModel.getStandardPrice());
					goodsDto.setGoodsNo(groupPropertyModel.getGoodsNo());;
					goodsDto.setBarcode(groupPropertyModel.getBarcode());
	    		}
	    	}
			Long goodsId  = updateSingleGoods(requestMap, goodsDto, goodsGroupDto, groupPropertysModel);
			syncGoodsIdList.add(goodsId);
	    }

	    if (CollectionUtils.isNotEmpty(goodsSetList)) {
	    	for (GoodsSetDto goodsSet : goodsSetList) {
	    		goodsSet.setGoodsSetId(goodsDto.getGoodsId());
	    	}
	    	
	    	goodsSetDao.deleteGoodsSet(goodsDto.getGoodsId());
	    	goodsSetDao.batchInsertGoodsSet(goodsSetList);
	    }
	    
        if (shop.getHeadShopId() == null && CollectionUtils.isNotEmpty(syncGoodsIdList)) {
        	SyncGoodsInfoDto syncGoodsInfo = new SyncGoodsInfoDto();
        	List<SyncGoodsDto> syncGoodsList = new LinkedList<SyncGoodsDto>();
        	syncGoodsList.add(needSyncGoods);
        	syncGoodsInfo.setSyncGoodsList(syncGoodsList);
    		MqPusher.pushMessage("SyncGoodsByUpdate", syncGoodsInfo);
        }
		
	}
	/**
	 * 从小到大排序 属性值id  1,2,3
	 */
	public static String sortArray(String goodsProValuesIds) {
		String afferStr = "";
		if(StringUtils.isNotBlank(goodsProValuesIds)){
			String[] arrayStr = goodsProValuesIds.split(",");
			int[] score = strArrayChangeIntArray(arrayStr);
			for (int i = 0; i < score.length - 1; i++) { // 最多做n-1趟排序
				for (int j = 0; j < score.length - i - 1; j++) { // 对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
					if (score[j] > score[j + 1]) { // 把小的值交换到后面
						int temp = score[j];
						score[j] = score[j + 1];
						score[j + 1] = temp;
					}
				}
			}
			for (int a = 0; a < score.length; a++) {
				afferStr += score[a]+",";
			}
			afferStr = afferStr.substring(0,afferStr.length()-1);
			logger.info("goodsProValuesIds最终排序结果："+afferStr.substring(0,afferStr.length()-1));
		}
		
		
		return afferStr;
	
	}
	public static int[] strArrayChangeIntArray(String[] arrayStr){
		int[] intArr = new int[arrayStr.length];
		for(int i = 0; i < arrayStr.length; i++)
		{
		    //先由字符串转换成char,再转换成String,然后Integer

			intArr[i] = Integer.parseInt( String.valueOf(arrayStr[i]));

		}
		return intArr;
	}
	/**
	 * 增加商品族属性
	 *
	 */
	private Long addGoodsGroup(GoodsGroupDto goodsGroupDto) throws Exception{
		Long goodsGroupId = goodsGroupDto.getGoodsGroupId();
		Date now = new Date();
		if (goodsGroupId != null) {
			goodsGroupDao.updateGoodsGroup(goodsGroupDto);
			goodsGroupDto.setLastUpdateTime(now);
	/*		goodsDao.updateGoodsStatsIsDelByGoodsGroupId(goodsGroupId, 2);
			goodsGroupPropertyDao.getGoodsGroupProperyByGroupId(goodsGroupId);*/
			return goodsGroupId;
		}
		
		VerifyGoodsGroupDto verifyGoodsGroupDto = new VerifyGoodsGroupDto();
		verifyGoodsGroupDto.setGoodsName(goodsGroupDto.getGoodsName());
		verifyGoodsGroupDto.setUnitName(goodsGroupDto.getUnit());
		if(CollectionUtils.isNotEmpty(goodsGroupDto.getCategoryIdList())){
			verifyGoodsGroupDto.setCategoryIdList(goodsGroupDto.getCategoryIdList());
		}
		int propertyNum = 0;
		List<String> groupPropertyName = goodsGroupDto.getGroupPropertyName();
		if(CollectionUtils.isNotEmpty(groupPropertyName)){
			verifyGoodsGroupDto.setGroupPropertyName(groupPropertyName);
			propertyNum = groupPropertyName.size();
		}
		List<GoodsGroupDto> goodsGroupDBList  = goodsGroupDao.getGoodsGroup(verifyGoodsGroupDto);
		int size = 0;
		if(CollectionUtils.isNotEmpty(goodsGroupDBList)){
			size = goodsGroupDBList.size();
			logger.info("goodsGroupDBList长度:"+size);
		}
		
		if(CollectionUtils.isEmpty(goodsGroupDBList) || size != propertyNum){
			if(CollectionUtils.isNotEmpty(groupPropertyName)){
				logger.info("商品族不存在！开始增加商品族:"+goodsGroupDto.getGoodsName());
//				Integer lastSequence = goodsGroupDao.queryGoodsGroupLastSequence();
//				goodsGroupDto.setSequence(++lastSequence);
				goodsGroupDto.setCreateTime(now);
				goodsGroupDto.setShopStatus(CommonConst.GOODS_GROUP_STS_XJ);
				goodsGroupId = goodsGroupDao.addGoodsGroup(goodsGroupDto); 
				
				//建立分类和商品族关系
				List<GoodsGroupCategoryRelationDto> goodsGroupCategoryRelationList = buildGoodsGroupCategoryRelationList(goodsGroupDto);
				if(CollectionUtils.isNotEmpty(goodsGroupCategoryRelationList)){
					goodsGroupCategoryRelationDao.addGoodsGroupCategoryRelationBatch(goodsGroupCategoryRelationList);

				}
			}
			
		}
		else{
			goodsGroupId = goodsGroupDBList.get(0).getGoodsGroupId();
			logger.info("商品族已经存在！商品名称:"+goodsGroupDto.getGoodsName());
		}
		
		return goodsGroupId;
		
	}
	

	private List<GoodsGroupCategoryRelationDto> buildGoodsGroupCategoryRelationList(GoodsGroupDto goodsGroupDto){
		List<GoodsGroupCategoryRelationDto> goodsGroupCategoryRelationList = new ArrayList<GoodsGroupCategoryRelationDto>();
		List<Long> categoryIdList = goodsGroupDto.getCategoryIdList();
		if(CollectionUtils.isNotEmpty(categoryIdList)){
			for (Long categoryId : categoryIdList) {
				GoodsGroupCategoryRelationDto goodsGroupCategoryRelationDto = new GoodsGroupCategoryRelationDto();
				goodsGroupCategoryRelationDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
				goodsGroupCategoryRelationDto.setParentCategoryId(goodsGroupDto.getParentCategoryId());
				goodsGroupCategoryRelationDto.setGroupCategoryId(categoryId);
				goodsGroupCategoryRelationDto.setCrStatus(1);//启用
				goodsGroupCategoryRelationDto.setCreateTime(new Date());
				goodsGroupCategoryRelationDto.setParentCategoryId(goodsGroupDto.getParentCategoryId());
				goodsGroupCategoryRelationList.add(goodsGroupCategoryRelationDto);
			}
		}
		return goodsGroupCategoryRelationList;

	}
	
	/**
	 * 增加商品族属性名称 
	 *
	 */
	private Long addGoodsGroupProBackId(Long goodsGroupId,String groupPropertyName,Long shopPropertyId) throws Exception{
		
		Long groupPropertyId  = null;	
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("goodsGroupId", goodsGroupId);
		parm.put("groupPropertyName", groupPropertyName);
		//验证是否重复增加是否存在
		GoodsGroupPropertyDto property = goodsGroupPropertyDao.getGoodsGroupProperty(parm);
		if(property==null){
			GoodsGroupPropertyDto goodsGroupPropertyDto = new GoodsGroupPropertyDto();
			goodsGroupPropertyDto.setGoodsGroupId(goodsGroupId);
			goodsGroupPropertyDto.setGroupPropertyName(groupPropertyName);
			goodsGroupPropertyDto.setShopPropertyId(shopPropertyId);
			groupPropertyId = goodsGroupPropertyDao.addGoodsGroupProBackId(goodsGroupPropertyDto);
		}
		else{
			logger.info("商品族属性名称已经存在！groupPropertyName:"+groupPropertyName);
			groupPropertyId = property.getGroupPropertyId();
			if (shopPropertyId != null && property.getShopPropertyId() != shopPropertyId) {
				property.setShopPropertyId(shopPropertyId);
				goodsGroupPropertyDao.updateGoodsGroupPro(property);
			}
		}
		
		return groupPropertyId;
		
	}
	/**
	 * 增加商品族属性值
	 *
	 */
	private Long addGoodsGoodsGroupProValues(Long groupPropertyId,String proValuesName) throws Exception{
		
		Long proValuesId  = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupPropertyId", groupPropertyId);
		param.put("proValuesName", proValuesName);
		GoodsGroupProValuesDto proValues = goodsGroupProValuesDao.getGoodsGroupProValues(param);
		if(proValues==null){
			GoodsGroupProValuesDto goodsGroupProValuesDto = new GoodsGroupProValuesDto();
			goodsGroupProValuesDto.setGroupPropertyId(groupPropertyId);
			goodsGroupProValuesDto.setProValue(proValuesName);
			goodsGroupProValuesDto.setIsSelect(1);//显示，是否可选
			proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(goodsGroupProValuesDto);
		}
		else{
			proValuesId = proValues.getProValuesId();
			logger.info("商品族属性值已经存在！proValuesName:"+proValuesName);
		}
		return proValuesId;
		
	}
	/**
	 * 增加商品属性关联关系
	 *  参数举例==》规格属性名称：颜色,尺寸     规格属性值：白色,XL
	 *
	 */
	private void addGoodsPropertyDto(String groupPropertyIds,String proValuesIds,Long goodsId) throws Exception{
		
		if (StringUtils.isBlank(groupPropertyIds) || goodsId == null) {
			return;
		}
		String[] groupPropertyIdsArr = groupPropertyIds.split(",");
		String[] proValuesIdsArr = proValuesIds.split(",");

		Map<String, String> param = new HashMap<String, String>();
		param.put("goodsId", goodsId.toString());
		goodsPropertyDao.delGoodsProperty(param);
		
		int length = proValuesIdsArr.length;
		for (int i = 0; i < length; i++) {
			Long groupPropertyId = Long.valueOf(groupPropertyIdsArr[i]);
			Long proValuesId = Long.valueOf(proValuesIdsArr[i]);
/*			GoodsPropertyDto parms = new GoodsPropertyDto();
			parms.setGoodsId(goodsId);
			parms.setGroupPropertyId(groupPropertyId);
			parms.setProValuesId(proValuesId);
			List<GoodsPropertyDto>  propertyList = goodsPropertyDao.getGoodsProperty(parms);
			if(CollectionUtils.isEmpty(propertyList)){
				//增加商品属性
				GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
				goodsPropertyDto.setGoodsId(goodsId);
				goodsPropertyDto.setGroupPropertyId(groupPropertyId);
				goodsPropertyDto.setProValuesId(proValuesId);
				goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);
			}
			else{
				logger.info("商品属性已经存在，故不重复增加商品属性！goodsId:"+goodsId);
			}*/
		
			//增加商品属性
			GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
			goodsPropertyDto.setGoodsId(goodsId);
			goodsPropertyDto.setGroupPropertyId(groupPropertyId);
			goodsPropertyDto.setProValuesId(proValuesId);
			goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);

		}

		
	}
	
	/**
	 * 操作单个商品
	 */
	private Long updateSingleGoods(Map<String, Object> requestMap,GoodsDto goodsDto,
			GoodsGroupDto goodsGroupDto,List<GroupPropertyModel> groupPropertysModel) throws Exception{
		Long goodsId = null;
        Long shopId = Long.valueOf(requestMap.get("shopId").toString());
        ShopDto shop = shopDao.getShopById(shopId);
        Double settlePrice = Double.valueOf(requestMap.get("standardPrice").toString());
        Double memberDiscount = shop.getMemberDiscount();
        Double discountPrice = NumberUtil.multiply(settlePrice, memberDiscount);
        goodsDto.setDiscountPrice(discountPrice);
        goodsDto.setVipPrice(discountPrice);
        goodsDto.setFinalPrice(discountPrice);
        Date now = new Date();
        goodsDto.setCreateTime(now);
        goodsDto.setLastUpdateTime(now);
        
        if(requestMap.get("goodsLogoId")!=null){
        	goodsDto.setGoodsLogo1((Long)requestMap.get("goodsLogoId"));
        }
        
		if (goodsDto.getGoodsId() == null) {
	/*		Long goodsGroupId = goodsGroupDao.addGoodsGroup(goodsGroupDto);
			goodsDto.setGoodsGroupId(goodsGroupId);
			requestMap.put("goodsGroupId", goodsGroupId);*/
			Integer goodsStatus = goodsDto.getGoodsStatus();
			goodsDto.setGoodsStatus(CommonConst.GOODS_STATUS_SC);
			int isExist = goodsDao.queryGoodIsExist(goodsDto);
			if (isExist > 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该商铺中已存在相同的商品");
			}
			goodsDto.setGoodsStatus(goodsStatus);
			goodsDto.setGoodsId(null);
			goodsId = goodsDao.addGoodsDto(goodsDto);
			if(goodsDto.getBarcode()!=null){
			    GoodsBarcodeDto goodsBarcodeDto=new GoodsBarcodeDto();
			    goodsBarcodeDto.setBarcode(goodsDto.getBarcode());
                goodsBarcodeDto.setGoodsName(goodsDto.getGoodsName());
                goodsBarcodeDto.setGoodsSize(goodsDto.getSpecsDesc());
                goodsBarcodeDto.setInPrice(goodsDto.getDiscountPrice()==0 ? 1 : goodsDto.getDiscountPrice());
                goodsBarcodeDto.setProductName(goodsDto.getGoodsNo());
                goodsBarcodeDto.setSalePrice(goodsDto.getFinalPrice());
                goodsBarcodeDto.setUnitName(goodsDto.getUnitName());
				Map map=collectDao.getStandardGoodsByBarCode(goodsDto.getBarcode());
				if(map==null){
					goodsDao.addGoodsBarcode(goodsBarcodeDto);
				}else if(map.get("id")!=null){
					goodsBarcodeDto.setId(Integer.valueOf(String.valueOf(map.get("id"))));
					goodsDao.updateGoodsBarcode(goodsBarcodeDto);
				}
			}
			insertAttachmentRelation(goodsId,goodsGroupDto.getGoodsLogoId(),(String[])requestMap.get("attachementIds"));
			
			if (goodsDto.getStorageTotalNumber() != null && goodsDto.getStorageTotalNumber() > 0) {
				storageServcie.insertShopStorage(goodsDto);
			}
			requestMap.put("goodsId", goodsId);
			
			levelService.pushAddPointMessage(3, null, 1, shopId.intValue(), 3, goodsId.toString(),true);

		} else {
			goodsId = goodsDto.getGoodsId();
			goodsDao.updateGoods(goodsDto);
			//删除图片：第一步：先删除关联关系（图片保留）；第二步：保存
    		//第一步 ：删除关联关系
			int n = this.attachmentRelationDao.delAttachmentRelationByGgId(goodsDto.getGoodsId());
			//第二步：保存
			insertAttachmentRelation(goodsDto.getGoodsId(),goodsGroupDto.getGoodsLogoId(),(String[])requestMap.get("attachementIds"));
			//goodsGroupDao.updateGoodsGroup(goodsGroupDto);
		}
		DataCacheApi.del(CommonConst.KEY_GOODS + goodsId);
		return goodsId;
	
	}
	
	/**
	 * 处理分店数据
	 * @throws Exception 
	 */
	public void addBranchShopGoods(GoodsDto goodsDto, ShopDto shop) throws Exception {
		
		//总店
		if (ChainStoresTypeEnum.HEADQUARTERS.getValue() == shop.getChainStoresType()) {
			logger.info("更新分店商品");
			List<ShopDto> shopBranchs  = shopDao.getShopListByHeadShopId(shop.getShopId());
			if(CollectionUtils.isNotEmpty(shopBranchs)){
				for (ShopDto shopDto : shopBranchs) {
					goodsDto.setShopId(shopDto.getShopId());
					goodsDao.addGoodsDto(goodsDto);
				}
				
			}
			

		}
	}
	/**
	 * 批量更新分店商品
	 * @throws Exception 
	 */
	public void updateBranchShopGoods(GoodsDto goodsDto, ShopDto shop) throws Exception {
		
		//总店
		if (ChainStoresTypeEnum.HEADQUARTERS.getValue() == shop.getChainStoresType()) {
			logger.info("更新分店商品");
			List<ShopDto> shopBranchs  = shopDao.getShopListByHeadShopId(shop.getShopId());
			if(CollectionUtils.isNotEmpty(shopBranchs)){
				for (ShopDto shopDto : shopBranchs) {
					goodsDto.setSourceGoodsId(goodsDto.getSourceGoodsId());
					goodsDto.setShopId(shopDto.getShopId());
					goodsDao.updateGoods(goodsDto);
				}
				
			}
			

		}
	}	
	
	private void insertAttachmentRelation(Long goodsId,Long goodsLogoId,String[] attachementIds) throws Exception {
		List<AttachmentRelationDto> insertRelations = new ArrayList<AttachmentRelationDto>();
		if (goodsLogoId != null) {
			//insertRelations.add(createAttachmentRelation(true, true, goodsGroupId, goodsLogoId));
			insertRelations.add(createAttachmentRelation(false, true, goodsId, goodsLogoId));
		}
		
		if (attachementIds != null) {
			for (String attachementIdStr : attachementIds) {
				Long attachementId = Long.valueOf(attachementIdStr);
				insertRelations.add(createAttachmentRelation(false, false, goodsId, attachementId));
			}
		}
		if (!insertRelations.isEmpty())
			attachmentRelationDao.addAttachmentRelationBatch(insertRelations);
	}
	
	private AttachmentRelationDto createAttachmentRelation(Boolean group,Boolean thumbnail,Long bizId,Long attachmentId) {
		AttachmentRelationDto relation = new AttachmentRelationDto();
		if (group == true) {
			relation.setBizType(9);
		}
		else {
			relation.setBizType(8);
		}
		if (thumbnail == true) {
			
			relation.setPicType(1);
		}
		else {
			
			relation.setPicType(2);
		}
		relation.setBizId(bizId);
		relation.setAttachmentId(attachmentId);
		return relation;
	}
	public PageModel getGoodsComments(Long goodsId, Integer page, Integer pageSize)
			throws Exception {
		List<UserGoodsCommentDto> list = this.userGoodsCommentDao.getGoodsComments(goodsId, page, pageSize);
		//获取总记录数
		PageModel pm = new PageModel();
		pm.setTotalItem(this.userGoodsCommentDao.getGoodsCommentsTotal(goodsId));
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setList(list);
		return pm;
	}
	
	/**
	 * 获取商铺中的商品分类
	 */
	public PageModel getShopGoodsCategory(Long shopId, Integer columnId,String goodsGroupId,String parentCategoryId, Integer page, Integer pageSize,Integer goodsType)
			throws Exception {
		List<GoodsCategoryDto> list=null;
		//获取总记录数
		PageModel pm = new PageModel();
		int total=0;
		//代表商品族ID
		if(null !=goodsGroupId){
			list = this.goodsCategoryDao.getShopGoodsGroupCategory(shopId,columnId,goodsGroupId,parentCategoryId,page,pageSize);
			total=this.goodsCategoryDao.getShopGoodsCategoryTotal(shopId,columnId,parentCategoryId);
		}else{
			list = this.goodsCategoryDao.getShopGoodsCategory(shopId,columnId,parentCategoryId,page,pageSize);
			total=this.goodsCategoryDao.getShopGoodsCategoryTotal(shopId,columnId,parentCategoryId);
		}
		pm.setTotalItem(total);
		List<GoodsCategoryDto> result=new ArrayList<GoodsCategoryDto>();
		if(null !=list && list.size()>0){
			GoodsCategoryDto category=null;
			Map param=new HashMap();
			param.put("n", 0);
			param.put("m", 10);
			param.put("bizType", 11);
			StringBuffer sbIds=null;
			StringBuffer sbUrls=null;
			for(int i=0,len=list.size();i<len;i++){
				sbIds=new StringBuffer();
				sbUrls=new StringBuffer();
				category=list.get(i);
				param.put("bizId",category.getGoodsCategoryId());
				param.put("picType",2);//轮播图
				List<Map<String, Object>> picList=shopDao.getBizLogo(param);//查询轮播图
				if(null != picList && picList.size()>0){
					for(int j=0,length=picList.size();j<length;j++){
						if(j==length-1){
							sbIds.append(picList.get(j).get("attachementId"));
							sbUrls.append(FdfsUtil.getFileProxyPath(String.valueOf(picList.get(j).get("fileUrl"))));
						}else{
							sbIds.append(picList.get(j).get("attachementId")+",");
							sbUrls.append(FdfsUtil.getFileProxyPath(String.valueOf(picList.get(j).get("fileUrl")))+",");
						}
					}
				}
				category.setCarouselAttachmentIds(sbIds.toString());
				category.setCarouselUrls(sbUrls.toString());
				//查询缩略图
				param.put("picType",1);//轮播图
				List<Map<String, Object>> logo=shopDao.getBizLogo(param);//查询缩略图
				if(null!=logo && logo.size()>0){
					category.setLogoId(String.valueOf(logo.get(0).get("attachementId")));
					category.setLogoUrl(FdfsUtil.getFileProxyPath(String.valueOf(logo.get(0).get("fileUrl"))));
				}
				//分类下商品的数量
				int totalGoods = goodsDao.getGoodsCountByCategoryId(shopId, category.getGoodsCategoryId(),goodsType);
				logger.debug("更新分店商品"+totalGoods);
				
				category.setGoodsTotal(totalGoods);
				
				result.add(category);
				
				
			}
		}
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setList(result);
		return pm;
	}

	public PageModel getGoodsList(GoodsDto goods, Integer page, Integer pageSize)
			throws Exception {
		List<GoodsDto> list = this.goodsDao.getGoodsList(goods, page, pageSize);
		PageModel pm = new PageModel();
		pm.setList(list);
		return pm;
	}
	
	public List<GoodsDto> getGoodsListToIndex(GoodsDto goods, Integer startId,
			Integer pageSize) throws Exception {
		return this.goodsDao.getGoodsListToIndex(goods, startId, pageSize);
	}
	
	public int getGoodsLastId() throws Exception {
		return this.goodsDao.getLastId();
	}

	public GoodsDto getGoodsById(Long id) throws Exception {
		CommonValidUtil.validPositLong(id, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_ID);
		GoodsDto pModel = this.goodsDao.getGoodsById(id);
		CommonValidUtil.validObjectNull(pModel, CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_GOODS);
		return pModel;
	}
	
	/**
	 * 批量查询商品根据id列表
	 */
	public List<GoodsDto> getGoodsListByIds(List<Long> idList) {
		List<GoodsDto>list=goodsDao.getGoodsListByIds(idList);
		return list;
	}

	/**
	 * 增加solr爬取数据的接口
	 */
	public PageModel getGoodsListForSearch(GoodsDto goods,PageModel pageModel,int... queryTotalCount) throws Exception {
		PageModel pm = this.goodsDao.getGoodsListForSearch(goods,pageModel,queryTotalCount);
		return pm;
	}

	public int validGoodsExists(Long shopId, Long goodsId)
			throws Exception {
		CommonValidUtil.validPositLong(goodsId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_ID);
		return this.goodsDao.validGoodsExists(shopId, goodsId);
	}

	public GoodsDto getGoodsByIds(Long shopId, Long goodsId)
			throws Exception {
		return this.goodsDao.getGoodsByIds(shopId, goodsId);
	}

	/** 
	 * 根据id进行查找
	* @Title: getGoodsSetById 
	* @Description: 
	* @param @param goodsSetId
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	public GoodsDto getGoodsSetById(Long goodsSetId) throws Exception {
		return goodsDao.getGoodsById(goodsSetId);
	}	
	
	/**
	 * 获取每个商铺销量最高的goodNum个商品,根据商铺列表
	 * @Title: getGoodsGroupByShopIdList 
	 * @param @param shopIdList
	 * @param @param goodsNum
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<GoodsDto> getGoodsGroupByShopIdList(List<Long> shopIdList,
			Integer goodsNum) throws Exception {
		return goodsDao.getGoodsGroupByShopIdList(shopIdList,goodsNum);
	}

	@Override
	public List<GoodsDto> getGoodsByGroupMap(Map<String,Object> parms) throws Exception {
		return goodsDao.getGoodsByGroupMap(parms);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.goods.IGoodsServcie#getGoodsList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getGoodsList(Map<String, Object> map)
			throws Exception {
		return goodsDao.getGoodsList(map);
	}

	@Override
	public PageModel getShopFullCategory(Long shopId, Integer page, Integer pageSize) throws Exception {
		//获取总记录数
		PageModel pm = new PageModel();
		int total=0;
		List<Map> list = goodsCategoryDao.getShopCategory(shopId,"0",page,pageSize);
		total=this.goodsCategoryDao.getShopGoodsCategoryTotal(shopId,null,"0");
		List<Map> childList = new ArrayList<Map>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Map map: list){
				childList = goodsCategoryDao.getShopCategory(shopId, map.get("categoryId")+"", 1, 100);
				map.put("childCategorys", childList);
			}
			
		}
		pm.setTotalItem(total);
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setList(list);
		return pm;
	}

	/*@Override
	public void saveGoodsBarcode(Map<String, String> goodsBarcodeMap) {
		goodsDao.saveGoodsBarCode(goodsBarcodeMap);
	}*/

	@Override
	public Long updateGoodsDtO(GoodsDto goodsDto,List<GoodsSetDto> goodsSetDtoList) throws Exception {
		Double settlePrice = Double.valueOf(goodsDto.getStandardPrice()); //套餐售卖价格
		Double agoPrice=0.0;//商品总原价
		for (GoodsSetDto goodsSetDto : goodsSetDtoList) {
			GoodsDto goodsDtoInfo=goodsDao.getGoodsById(goodsSetDto.getGoodsId());
			CommonValidUtil.validObjectNull(goodsDtoInfo, CodeConst.CODE_PARAMETER_NOT_NULL, "商品ID:"+goodsSetDto.getGoodsId()+"不存在");
			agoPrice+=NumberUtil.multiply(goodsDtoInfo.getStandardPrice(),goodsSetDto.getGoodsNumber());
		}
		if(agoPrice<=0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商品原价不能为0");
		}
		Double discount=NumberUtil.divide(settlePrice, agoPrice);//计算出折扣
		goodsDto.setDiscountPrice(settlePrice);
		goodsDto.setVipPrice(settlePrice);
		goodsDto.setFinalPrice(settlePrice);
		goodsDto.setCreateTime(new Date());
		goodsDto.setOriginalPrice(agoPrice);
		//如果goodsId为空，添加操作
		if(CommonValidUtil.isEmpty(goodsDto.getGoodsId())){
			goodsDto.setGoodsStatus(CommonConst.GOODS_STATUS_NORMAL);
			Long goodsId = goodsDao.addGoodsDto(goodsDto);
			for (GoodsSetDto goodsSetDto : goodsSetDtoList) {
				GoodsDto goodsDtoInfo=goodsDao.getGoodsById(goodsSetDto.getGoodsId());
				goodsSetDto.setGoodsSetId(goodsId); //套餐ID，外键到1dcq_goods.goods_id
				if(goodsSetDto.getPrice()==null){//套餐内单价，未填写时，自动计算
					goodsSetDto.setPrice(new java.math.BigDecimal(NumberUtil.multiply(goodsDtoInfo.getStandardPrice(),discount)));// 计算出 折扣后的套餐单价
				}
			}
			goodsSetDao.batchInsertGoodsSet(goodsSetDtoList);
			return goodsId;
		}else{
			Long goodsId =goodsDto.getGoodsId();
			//否则修改
			goodsDto.setLastUpdateTime(new Date());
			goodsDao.updateGoods(goodsDto);
			return goodsId;
		}
	}

	@Override
	public Double sumGoodsPirce(List<GoodsSetDto> list) throws Exception {
		return goodsDao.sumGoodsPirce(list);
	}

	@Override
	public PageModel getShopGoodsList(Map<String, Object> param,Integer page,Integer pageSize) throws Exception {
		PageModel pm = new PageModel();
		param.put("pNo", page-1);
		param.put("pSize", pageSize);
		List<GoodsSetDto> goodsList=goodsSetDao.getShopGoodsList(param);
		pm.setTotalItem(goodsSetDao.getShopGoodsListCount(param));
		for (GoodsSetDto goodsSetDto : goodsList) {
			param.put("goodsId",goodsSetDto.getGoodsSetId());
			goodsSetDto.setGoodsList(goodsSetDao.getGoodsSetList(param));
		}
		pm.setList(goodsList);
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		return pm;
	}

	@Override
	public void updateGoodsUnit(GoodsUnitDto goodsUnitDto) {
		//同一店铺内不能添加重复的单位
		GoodsUnitDto goodsUnitDto2 = goodsDao.getShopGoodUnit(goodsUnitDto);
		if(goodsUnitDto2!=null){
			throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_GOODSUNITNAME_EXIST);
		}
		if(goodsUnitDto!=null && goodsUnitDto.getUnitId()==null){
			goodsDao.insertGoodsUnit(goodsUnitDto);
		}else{
			GoodsUnitDto goodsUnitDto1=goodsDao.getShopGoodUnitByUnitId(goodsUnitDto.getUnitId());
			if(goodsUnitDto.getDigitScale()!=null){
				goodsUnitDto1.setDigitScale(goodsUnitDto.getDigitScale());
			}
			if(goodsUnitDto.getUnitName()!=null){
				goodsUnitDto1.setUnitName(goodsUnitDto.getUnitName());
			}
			goodsDao.updateGoodsUnit(goodsUnitDto1);
		}
	}

	@Override
	public void delGoodsUnit(Map<String, Object> result) {
		goodsDao.delGoodsUnit(result);
	}

	@Override
	public PageModel getGoodsUnitList(Map<String, Object> result,Integer pageNO, Integer pageSize) {
		PageModel pageModel=new PageModel();
		result.put("m", (pageNO - 1) * pageSize);
		result.put("n",  pageSize);
		List<GoodsUnitDto> goodsList=goodsDao.getGoodsUnitList(result);
		pageModel.setList(goodsList);
		pageModel.setTotalItem(goodsList.size());
		pageModel.setToPage(pageNO);
		pageModel.setPageSize(pageSize);
		return pageModel;
	}

	@Override
	public int getGoodsUnit(Integer unitId) {
		return goodsDao.getGoodsUnit(unitId);
	}

	@Override
	public int getGoodsByUnitId(Integer unitId) {
		return goodsDao.getGoodsByUnitId(unitId);
	}

	@Override
	public Map<String, Object> isExistSameName(Map<String, Object> map) {
		int n =goodsDao.isExistSameName(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(n>0){
			resultMap.put("isExistSameName", 1);
		}else{
			resultMap.put("isExistSameName", 0);
		}
		return resultMap;
	}

	@Override
	public PageModel getGoodsSalestatistics(Map<String, Object> param) {
		PageModel pageModel = new PageModel();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		int count = goodsDao.getGoodsSalestatisticsCount(param);
		if(count > 0) {
		    resultList = goodsDao.getGoodsSalestatistics(param);
		}
		pageModel.setTotalItem(count);
		pageModel.setList(resultList);
		return pageModel;
	}


	@Override
	public void syncGoods(SyncGoodsInfoDto syncGoodsInfo,List<ShopDto> branchShopList) throws Exception {
		Date now = new Date();
		List<AttachmentRelationDto> needUpdateGoodsRelations = new LinkedList<AttachmentRelationDto>();
		List<GoodsDto> needUpdateGoodsList = new LinkedList<GoodsDto>();
		List<GoodsDto> needInsertGoodsList = new LinkedList<GoodsDto>();
		
		for (SyncGoodsDto syncGood : syncGoodsInfo.getSyncGoodsList()) {
			Long shopId = syncGood.getShopId();
			ShopDto shop = shopDao.getShopById(shopId);
			if (shop == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"总店店铺不存在 shopId:"+shopId);
			}
			
			if (CollectionUtils.isEmpty(branchShopList)) {
				branchShopList = shopDao.getShopListByHeadShopId(shopId);
			}
			
			if (CollectionUtils.isEmpty(branchShopList)) {
				continue;
			}
			
			List<Long> syncGoodIdList = syncGood.getGoodsList();
			for (Long syncGoodId : syncGoodIdList) {
				GoodsDto good = goodsDao.getGoodsByIdFromDb(syncGoodId.longValue());
				if (good == null) {
					logger.info("同步商品不存在 goodId:{}",syncGoodId);
					continue;
				}
				
				Long goodsCategoryId = good.getGoodsCategoryId();
				
				if (!good.getShopId().equals(shopId)) {
					logger.info("同步商品在店铺内不存在  goodId:{}，shopId:{}",syncGoodId,shopId);
					continue;
				}
				
				GoodsDto searchCondition = new GoodsDto();
				searchCondition.setSourceGoodsId(syncGoodId.longValue());
				List<GoodsDto> needToUpdateBranchGoods = goodsDao.queryGoods(searchCondition);
				
				for (GoodsDto updateGood : needToUpdateBranchGoods) {
					good.setGoodsId(updateGood.getGoodsId());
					good.setShopId(updateGood.getShopId());
					good.setSourceGoodsId(syncGoodId.longValue());
					GoodsCategoryDto updateGoodsCategory = addGoodsCategory(goodsCategoryId, updateGood.getShopId());
					good.setGoodsCategoryId(updateGoodsCategory.getGoodsCategoryId());
					good.setLastUpdateTime(now);
					needUpdateGoodsList.add(good.clone());
				}
				
				for (ShopDto branchShop : branchShopList) {
					Long branchShopId = branchShop.getShopId();
					searchCondition.setShopId(branchShopId);
					List<GoodsDto> branchGoodsList = goodsDao.queryGoods(searchCondition);
					if (CollectionUtils.isNotEmpty(branchGoodsList)) {
						continue;
					}
					
					good.setGoodsId(null);
					good.setShopId(branchShopId);
					good.setSourceGoodsId(syncGoodId.longValue());
					
					if (goodsCategoryId != null) {
						//添加分店分类
						GoodsCategoryDto brachGoodCategory = addGoodsCategory(goodsCategoryId, branchShopId);
						good.setGoodsCategoryId(brachGoodCategory.getGoodsCategoryId());
					}
					//添加分店商品
					good.setCreateTime(now);
					good.setLastUpdateTime(now);
					needInsertGoodsList.add(good.clone());
				}
			}
		}
		
		//更新商品
		if (CollectionUtils.isNotEmpty(needUpdateGoodsList)) {
			goodsDao.batchUpdateGoodsDto(needUpdateGoodsList);
			for (GoodsDto updateGood : needUpdateGoodsList) {
				setNeedUpdateGoodsRelations(updateGood.getSourceGoodsId(), updateGood.getGoodsId(), needUpdateGoodsRelations);
			}
		}
	
		//增加商品
		if (CollectionUtils.isNotEmpty(needInsertGoodsList)) {
			for (GoodsDto insertGood : needInsertGoodsList) {
				goodsDao.addGoodsDto(insertGood);
				setNeedUpdateGoodsRelations(insertGood.getSourceGoodsId(), insertGood.getGoodsId(), needUpdateGoodsRelations);
			}
		}
	
		//更新商品附件
		if (CollectionUtils.isNotEmpty(needUpdateGoodsRelations)) {
			attachmentRelationDao.addAttachmentRelationBatch(needUpdateGoodsRelations);
		}
	}
	
	private void setNeedUpdateGoodsRelations(Long headGoodsId, Long branchGoodsId,
											List<AttachmentRelationDto> needUpdateGoodsRelations) throws Exception {
		
		if (needUpdateGoodsRelations == null) {
			return;
		}
		
		List<AttachmentRelationDto> updateGoodsAttachReList = getSyncGoodsAttachment(headGoodsId, branchGoodsId);
		
		if (CollectionUtils.isNotEmpty(updateGoodsAttachReList)) {
			needUpdateGoodsRelations.addAll(updateGoodsAttachReList);
		}
		
		
	}
	private List<AttachmentRelationDto> getSyncGoodsAttachment(Long headGoodsId, Long branchGoodsId) throws Exception {
		AttachmentRelationDto  attachSearch = new AttachmentRelationDto();
		attachSearch.setBizId(headGoodsId);
		attachSearch.setBizType(8);
		List<AttachmentRelationDto> updateGoodsAttachReList = attachmentRelationDao.queryAttachmentRelation(attachSearch);
		
		if (CollectionUtils.isEmpty(updateGoodsAttachReList)) {
			return new LinkedList<AttachmentRelationDto>();
		}
		
		for (AttachmentRelationDto upateGoodsAttachRe : updateGoodsAttachReList) {
			upateGoodsAttachRe.setAttachmentRelationId(null);
			upateGoodsAttachRe.setBizId(branchGoodsId);
		}
		
		attachSearch.setBizId(branchGoodsId);
		attachmentRelationDao.delAttachmentRelation(attachSearch);
		return updateGoodsAttachReList;
	}
	
	@Override
	public GoodsCategoryDto addGoodsCategory(Long goodsCategoryId,Long branchShopId) throws Exception {
		GoodsCategoryDto searchCategory = new GoodsCategoryDto();
		searchCategory.setShopId(branchShopId);
		searchCategory.setSourceGoodsCategoryId(goodsCategoryId);
		List<GoodsCategoryDto> brachGoodCategoryList = goodsCategoryDao.queryGoodsCategory(searchCategory);
		GoodsCategoryDto brachGoodCategory = null;
		if (CollectionUtils.isEmpty(brachGoodCategoryList)) {
			brachGoodCategory = addGoodsCategoryRecurse(goodsCategoryId, branchShopId);
		}else {
			brachGoodCategory = brachGoodCategoryList.get(0);
		}
		
		return brachGoodCategory;
	}
	
	private GoodsCategoryDto addGoodsCategoryRecurse(Long goodsCategoryId,Long branchShopId) throws Exception {
		GoodsCategoryDto goodCategory = goodsCategoryDao.queryGoodsCategoryById(goodsCategoryId);
		Long parentCategoryId = goodCategory.getParentCategoryId();
		
		goodCategory.setGoodsCategoryId(null);
		goodCategory.setShopId(branchShopId);
		goodCategory.setSourceGoodsCategoryId(goodsCategoryId);
		if (parentCategoryId == null || parentCategoryId == 0) {
			goodCategory.setParentCategoryId(parentCategoryId);
		}else {
			GoodsCategoryDto brachGoodCategory = addGoodsCategory(parentCategoryId, branchShopId);
			goodCategory.setParentCategoryId(brachGoodCategory.getGoodsCategoryId());
		}
		
		goodsCategoryDao.addGoodsCategory(goodCategory);
		return goodCategory;
	}

	@Override
	public Double getGoodsSaleSalestatistics(Map<String, Object> param) {
		return goodsDao.getGoodsSaleSalestatistics(param);
	}
	
	
	@Override
	public  Map<String, Object> importGoodsByExcel(Long shopId,Integer templateType,MultipartFile file) throws Exception {
        ShopDto shop = shopDao.getShopById(shopId);
        Map<Integer, List<String>> excelData = null;
        excelData = ExcelParser.getExcelData(file.getInputStream());
        if(excelData.size()==0){
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"模板数据为空");
        }
        Map<String, Integer> excelColumnIndexMap = new LinkedHashMap<String, Integer>();
        List<String> excelColumnList =  excelData.remove(0);
        
        for (int columnIndex = 0; columnIndex < excelColumnList.size(); columnIndex++) {
        	excelColumnIndexMap.put(excelColumnList.get(columnIndex).replace("*", "").trim(), columnIndex);
        }
        
        List<Map<String, String>> failList = new LinkedList<Map<String,String>>();
        Integer importTotalNum = excelData.size();
        if (importTotalNum <= 0) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"导入数据不能为空");
        }
        
        SyncGoodsDto needSyncGoods = new SyncGoodsDto();
        needSyncGoods.setShopId(shopId);
        List<Long> syncGoodsIdList = new LinkedList<Long>();
        needSyncGoods.setGoodsList(syncGoodsIdList);
        
        if (templateType == 1) {
        	importByGoodsTemplate(excelData, shop, excelColumnIndexMap, failList, syncGoodsIdList);
        } else if (templateType == 2) {
        	importByDishesTemplate(excelData, shop, excelColumnIndexMap, failList, syncGoodsIdList);
        } else {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"模板不可识别");
        }
        
        if (shop.getHeadShopId() == null && CollectionUtils.isNotEmpty(syncGoodsIdList)) {
        	SyncGoodsInfoDto syncGoodsInfo = new SyncGoodsInfoDto();
        	List<SyncGoodsDto> syncGoodsList = new LinkedList<SyncGoodsDto>();
        	syncGoodsList.add(needSyncGoods);
        	syncGoodsInfo.setSyncGoodsList(syncGoodsList);
    		MqPusher.pushMessage("SyncGoodsByUpdate", syncGoodsInfo);
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer failNum = failList.size();
        Integer successNum = importTotalNum - failNum;
        resultMap.put("successNum", successNum);
        resultMap.put("failNum", failNum);
        resultMap.put("failList", failList);
        
        String returnMsg = "";
        Integer returnCode = CodeConst.CODE_SUCCEED;
        
        if (successNum == importTotalNum) {
        	returnMsg = "全部导入成功";
        }else if (failNum == importTotalNum) {
        	returnMsg = "全部导入失败";
        	returnCode = CodeConst.CODE_FAILURE;
        }else if (successNum < importTotalNum) {
        	returnMsg = "部分导入失败";
        	returnCode = CodeConst.CODE_FAILURE;
        }
        
        resultMap.put("returnMsg", returnMsg);
        resultMap.put("returnCode", returnCode);
        return resultMap;
	}
	
	private void importByDishesTemplate(Map<Integer, List<String>> excelData,ShopDto shop,Map<String, Integer> excelColumnIndexMap,
			List<Map<String, String>> failList,List<Long> syncGoodsIdList) throws Exception {
		Date now = new Date();
		Long shopId = shop.getShopId();
	    GoodsCategoryDto searchGoodsCategory = new GoodsCategoryDto();
        searchGoodsCategory.setShopId(shopId);
        for (Integer rowNo : excelData.keySet()) {
        	List<String> excelRowData = excelData.get(rowNo);
        	
        	GoodsDto importGoods = new GoodsDto();
        	importGoods.setShopId(shopId);
         	if (excelColumnIndexMap.get("菜品名称") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的菜品导入模板");
        	}
         	
           	String goodsName = excelRowData.get(excelColumnIndexMap.get("菜品名称"));
        	if (StringUtils.isBlank(goodsName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品名称不能为空"));
        		continue;
        	}
        	
        	if (goodsName.length() > 10) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品名称长度超出限制"));
        		continue;
        	}
        	
          	try {
        		SensitiveWordsUtil.checkSensitiveWords("菜品名称", goodsName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
        	importGoods.setGoodsName(goodsName);
        	importGoods.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
          	if (excelColumnIndexMap.get("助记码") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的菜品导入模板");
        	}
          	
          	String goodsNo = excelRowData.get(excelColumnIndexMap.get("助记码"));
          	if (StringUtils.isNotBlank(goodsNo)) {
          		goodsNo = goodsNo.trim();
          		if (!CommonValidUtil.validIntegerStr(goodsNo)) {
       				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "助记码应为数字"));
            		continue;
       			}
          		
          	  	if (goodsNo.length() > 6 || goodsNo.length() < 4) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "助记码应为4-6位数字"));
            		continue;
            	}
          	}
        	importGoods.setGoodsNo(goodsNo);
        	
         	if (excelColumnIndexMap.get("菜品一级分类") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的菜品导入模板");
        	}
        	String firstCategoryName = excelRowData.get(excelColumnIndexMap.get("菜品一级分类"));
          	if (StringUtils.isBlank(firstCategoryName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品一级分类不能为空"));
        		continue;
        	}
          	
          	firstCategoryName = firstCategoryName.trim();
        	if (firstCategoryName.length() > 8) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品一级分类名称长度超出限制"));
        		continue;
        	}
        	
        	try {
        		SensitiveWordsUtil.checkSensitiveWords("菜品一级分类名称", firstCategoryName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
        	
          	searchGoodsCategory.setCategoryName(firstCategoryName);
          	List<GoodsCategoryDto> firstCategoryList = goodsCategoryDao.queryGoodsCategory(searchGoodsCategory);
          	Long parentCategoryId = Long.valueOf(0);
          	
          	GoodsCategoryDto firstCategory = null;
          	GoodsCategoryDto searchCategoryIndex = new GoodsCategoryDto();
          	if (CollectionUtils.isEmpty(firstCategoryList)) {
          		firstCategory = new GoodsCategoryDto();
          		firstCategory.setShopId(shopId);
          		firstCategory.setCategoryName(firstCategoryName);
          		firstCategory.setColumnId(shop.getColumnId());
          		firstCategory.setParentCategoryId(Long.valueOf(0));
          		
          		searchCategoryIndex.setShopId(shopId);
          		searchCategoryIndex.setParentCategoryId(firstCategory.getParentCategoryId());
          		Integer categoryIndex = goodsCategoryDao.queryGoodsCategoryLastIndex(searchCategoryIndex);
          		firstCategory.setCategoryIndex(categoryIndex == null ? 1 : ++categoryIndex);
          		goodsCategoryDao.addGoodsCategory(firstCategory);
          	}else {
          		firstCategory = firstCategoryList.get(0);
          	}
          	
          	
        	importGoods.setGoodsFirstCategoryName(firstCategoryName);
        	String secondCategoryName = excelRowData.get(excelColumnIndexMap.get("菜品二级分类"));
        	
        	if (StringUtils.isBlank(secondCategoryName)) {
        		importGoods.setGoodsCategoryId(firstCategory.getGoodsCategoryId());
        	}else {
        		secondCategoryName = secondCategoryName.trim();
        	   	if (secondCategoryName.length() > 8) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品二级分类名称长度超出限制"));
            		continue;
            	}
            	try {
            		SensitiveWordsUtil.checkSensitiveWords("菜品二级分类名称", secondCategoryName);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
        		searchGoodsCategory.setCategoryName(secondCategoryName);
        		List<GoodsCategoryDto> secondtCategoryList = goodsCategoryDao.queryGoodsCategory(searchGoodsCategory);
        		
             	GoodsCategoryDto secondCategory = null;
              	if (CollectionUtils.isEmpty(secondtCategoryList)) {
              		secondCategory = new GoodsCategoryDto();
              		secondCategory.setShopId(shopId);
              		secondCategory.setCategoryName(secondCategoryName);
              		secondCategory.setColumnId(shop.getColumnId());
              		secondCategory.setParentCategoryId(firstCategory.getGoodsCategoryId());
              		
              		searchCategoryIndex.setShopId(shopId);
              		searchCategoryIndex.setParentCategoryId(secondCategory.getParentCategoryId());
              		Integer categoryIndex = goodsCategoryDao.queryGoodsCategoryLastIndex(searchCategoryIndex);
              		secondCategory.setCategoryIndex(categoryIndex == null ? 1 : ++categoryIndex);
              		goodsCategoryDao.addGoodsCategory(secondCategory);
              	}else {
              		secondCategory = secondtCategoryList.get(0);
              	}
              	
              	importGoods.setGoodsCategoryId(secondCategory.getGoodsCategoryId());
              	parentCategoryId = secondCategory.getParentCategoryId();
        	}
        	
        	importGoods.setGoodsSecondCategoryName(secondCategoryName);
        	
        	String price = excelRowData.get(excelColumnIndexMap.get("价格"));
        
       		if (StringUtils.isBlank(price) ) {
    			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "价格不能为空"));
        		continue;
    		}
       		
       		price = price.trim();
       		
       		String goodsProperty = excelRowData.get(excelColumnIndexMap.get("规格"));
       		
       		if (StringUtils.isNotBlank(goodsProperty)) {
       			goodsProperty = goodsProperty.trim();
       			String[] propertyPrices = price.split("；");
       			String[] propertys = goodsProperty.split("；");
       			
       			int pricesLen = propertyPrices.length;
       			int propertyLen = propertys.length;
       			
       			if (pricesLen != propertyLen) {
       				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格与价格不对应"));
            		continue;
       			}
       			
       			List<Map<String, String>> goodsPriceSpecList = new LinkedList<Map<String,String>>();
       			Boolean avaliProperty = true;
       			for (int i=0; i < propertyLen; i++) {
       				String property = propertys[i].trim();
       				String propertyPrice = propertyPrices[i].trim();
       				
       		       	if (property.length() > 7) {
       		       		avaliProperty = false;
       	        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格名称长度超出限制  规格："+property));
       	        		break;
       	        	}
       				if (!CommonValidUtil.validDecimalStr(propertyPrice)) {
       					avaliProperty = false;
           				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格价格类型错误"));
           				break;
           			}
       				
       				if (Double.valueOf(propertyPrice) > CommonConst.PRICE_LIMIT) {
       					avaliProperty = false;
       					failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格价格位数超出限制"));
       					break;
       				}
       				
       				Map<String, String> goodsPriceSpec = new LinkedHashMap<String, String>();
       				goodsPriceSpec.put("spec", property);
       				goodsPriceSpec.put("price", propertyPrice);
       				goodsPriceSpecList.add(goodsPriceSpec);
       			}
       			
       			if (!avaliProperty) {
       				continue;
       			}
       			importGoods.setGoodsPriceSpec(JSON.toJSONString(goodsPriceSpecList));
       			price = propertyPrices[0].trim();
       		}else {
       			if (!CommonValidUtil.validDecimalStr(price)) {
       				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "价格类型错误"));
            		continue;
       			}
       			
       			if (Double.valueOf(price) > CommonConst.PRICE_LIMIT) {
   					failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "价格位数超出限制"));
            		continue;
   				}
       		}
       		
       		Double settlePrice = Double.valueOf(price);
        	importGoods.setStandardPrice(settlePrice);
            Double memberDiscount = shop.getMemberDiscount();
            Double discountPrice = NumberUtil.multiply(settlePrice, memberDiscount);
            importGoods.setDiscountPrice(discountPrice);
            importGoods.setVipPrice(discountPrice);
            importGoods.setFinalPrice(discountPrice);
            importGoods.setLastUpdateTime(now);
        	String unitName = excelRowData.get(excelColumnIndexMap.get("单位"));
          	if (StringUtils.isBlank(unitName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "单位不能为空"));
        		continue;
        	}
          	
          	unitName = unitName.trim();
          	if (unitName.length() > 4) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "单位名称长度超出限制"));
        		continue;
        	}
           	try {
        		SensitiveWordsUtil.checkSensitiveWords("单位名称", unitName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
          	long unitId = collectService.saveUnit(unitName, shopId, null);
          	importGoods.setUnitId(unitId);
          	importGoods.setUnitName(unitName);
          	
          	String serverModel = excelRowData.get(excelColumnIndexMap.get("服务类型"));
          	if (StringUtils.isNotBlank(serverModel)) {
          		if (serverModel.equals("到店")) {
          			importGoods.setGoodsServerMode(2);
          		}else if (serverModel.equals("到店和外卖")) {
          			importGoods.setGoodsServerMode(3);
          		}else if (serverModel.equals("外卖")) {
          			importGoods.setGoodsServerMode(1);
          		}
          	}
          	
        	String goodsDesc = excelRowData.get(excelColumnIndexMap.get("菜品简介"));
        	String goodsDetailDesc = excelRowData.get(excelColumnIndexMap.get("详细介绍"));
        	
        	if (StringUtils.isNotBlank(goodsDesc)) {
        		goodsDesc = goodsDesc.trim();
        	   	if (goodsDesc.length() > 200) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "菜品简介长度超出限制"));
            		continue;
            	}
               	try {
            		SensitiveWordsUtil.checkSensitiveWords("菜品简介", goodsDesc);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
        	   	importGoods.setGoodsDesc(goodsDesc);
        	}
        	
        	if (StringUtils.isNotBlank(goodsDetailDesc)) {
        		goodsDetailDesc = goodsDetailDesc.trim();
        /*	   	if (goodsDetailDesc.length() > 500) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "详细介绍长度超出限制"));
            		continue;
            	}*/
             	try {
            		SensitiveWordsUtil.checkSensitiveWords("详细介绍", goodsDetailDesc);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
        	   	importGoods.setGoodsDetailDesc(goodsDetailDesc);
        	}
        	
        	String key = excelRowData.get(excelColumnIndexMap.get("关键字"));
        	if (StringUtils.isNotBlank(key)) {
        		StringBuilder goodsKey = new StringBuilder();
        		Boolean avaliKey = true;
        		for (String keyWord : key.split("；")) {
        	  	   	if (keyWord.length() > 10) {
                		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "单个关键字长度超出限制多个关键字应以中文分号分割"));
                		avaliKey = false;
                		break;
                	}
        			goodsKey.append(keyWord).append(",");
        		}
        		
        		if (!avaliKey) {
        			continue;
        		}
        		
        		int keyLen = goodsKey.toString().length() - 1;
        		importGoods.setGoodsKey(goodsKey.toString().substring(0, keyLen));
        	}
        	
        	try {
        		GoodsDto existGood = goodsDao.queryExistGood(importGoods);
    			if (existGood != null) {
    				importGoods.setGoodsId(existGood.getGoodsId());
    				goodsDao.updateGoods(importGoods);
//    				importGoods.setGoodsGroupId(existGood.getGoodsGroupId());
//    				updateGoodsGroupPriceRange(importGoods);
    			}else {
                	GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
                	goodsGroupDto.setShopId(shopId);
                	goodsGroupDto.setGoodsName(goodsName);
                	goodsGroupDto.setGoodsDesc(importGoods.getGoodsDesc());
                	goodsGroupDto.setGoodsDetailDesc(importGoods.getGoodsDetailDesc());
                	goodsGroupDto.setMinPrice(new BigDecimal(settlePrice));
                	goodsGroupDto.setMaxPrice(new BigDecimal(settlePrice));
                	goodsGroupDto.setUnitId(unitId);
                	goodsGroupDto.setUnit(unitName);
                	List<Long> categoryIdList = new LinkedList<Long>();
                	categoryIdList.add(importGoods.getGoodsCategoryId());
                	goodsGroupDto.setCategoryIdList(categoryIdList);
                	goodsGroupDto.setParentCategoryId(parentCategoryId);
                	goodsGroupDto.setPinyincode(importGoods.getPinyinCode());
//                	Long goodsGroupId = addGoodsGroup(goodsGroupDto);
//                	importGoods.setGoodsGroupId(goodsGroupId);
                	
    				importGoods.setCreateTime(now);
    				insertGoods(importGoods);
    			}
    			
    			syncGoodsIdList.add(importGoods.getGoodsId());
        		DataCacheApi.del(CommonConst.KEY_GOODS + importGoods.getGoodsId());
			} catch (Exception e) {
				logger.error("Excel批量导入菜品失败 -- 行数：{} 失败原因：{}", rowNo,e);
				logger.error("失败详细原因", e);
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "导入失败"));
			}
          	
        }
	}
	
	private void importByGoodsTemplate(Map<Integer, List<String>> excelData,ShopDto shop,Map<String, Integer> excelColumnIndexMap,
										List<Map<String, String>> failList,List<Long> syncGoodsIdList) throws Exception{
		Date now = new Date();
		Long shopId = shop.getShopId();
	    GoodsCategoryDto searchGoodsCategory = new GoodsCategoryDto();
        searchGoodsCategory.setShopId(shopId);
        for (Integer rowNo : excelData.keySet()) {
        	List<String> excelRowData = excelData.get(rowNo);
        	
        	GoodsDto importGoods = new GoodsDto();
        	importGoods.setShopId(shopId);
        	if (excelColumnIndexMap.get("商品名称") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的商品导入模板");
        	}
        	String goodsName = excelRowData.get(excelColumnIndexMap.get("商品名称"));
        	if (StringUtils.isBlank(goodsName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品名称不能为空"));
        		continue;
        	}
        	
        	if (goodsName.length() > 30) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品名称长度超出限制"));
        		continue;
        	}
        	
        	try {
        		SensitiveWordsUtil.checkSensitiveWords("商品名称", goodsName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
        	
        	importGoods.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
        	importGoods.setGoodsName(goodsName);
         	if (excelColumnIndexMap.get("编码") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的商品导入模板");
        	}
        	importGoods.setGoodsNo(excelRowData.get(excelColumnIndexMap.get("编码")));
        	
          	if (excelColumnIndexMap.get("条形码") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的商品导入模板");
        	}
        	importGoods.setBarcode(excelRowData.get(excelColumnIndexMap.get("条形码")));
        	
           	if (excelColumnIndexMap.get("商品一级分类") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的商品导入模板");
        	}
           	
        	String firstCategoryName = excelRowData.get(excelColumnIndexMap.get("商品一级分类"));
          	if (StringUtils.isBlank(firstCategoryName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品一级分类不能为空"));
        		continue;
        	}
          	
          	firstCategoryName = firstCategoryName.trim();
        	if (firstCategoryName.length() > 8) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品一级分类名称长度超出限制"));
        		continue;
        	}
        	
           	try {
        		SensitiveWordsUtil.checkSensitiveWords("商品一级分类名称", firstCategoryName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
          	searchGoodsCategory.setCategoryName(firstCategoryName);
          	List<GoodsCategoryDto> firstCategoryList = goodsCategoryDao.queryGoodsCategory(searchGoodsCategory);
          	Long parentCategoryId = Long.valueOf(0);
          	
          	GoodsCategoryDto firstCategory = null;
          	GoodsCategoryDto searchCategoryIndex = new GoodsCategoryDto();
          	if (CollectionUtils.isEmpty(firstCategoryList)) {
          		firstCategory = new GoodsCategoryDto();
          		firstCategory.setShopId(shopId);
          		firstCategory.setCategoryName(firstCategoryName);
          		firstCategory.setColumnId(shop.getColumnId());
          		firstCategory.setParentCategoryId(Long.valueOf(0));
          		
          		searchCategoryIndex.setShopId(shopId);
          		searchCategoryIndex.setParentCategoryId(firstCategory.getParentCategoryId());
          		Integer categoryIndex = goodsCategoryDao.queryGoodsCategoryLastIndex(searchCategoryIndex);
          		firstCategory.setCategoryIndex(categoryIndex == null ? 1 : ++categoryIndex);
          		goodsCategoryDao.addGoodsCategory(firstCategory);
          	}else {
          		firstCategory = firstCategoryList.get(0);
          	}
          	
          	
        	importGoods.setGoodsFirstCategoryName(firstCategoryName);
        	String secondCategoryName = excelRowData.get(excelColumnIndexMap.get("商品二级分类"));
        	
        	if (StringUtils.isBlank(secondCategoryName)) {
        		importGoods.setGoodsCategoryId(firstCategory.getGoodsCategoryId());
        	}else {
        		secondCategoryName = secondCategoryName.trim();
        	   	if (secondCategoryName.length() > 8) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品二级分类名称长度超出限制"));
            		continue;
            	}
        	   	
            	try {
            		SensitiveWordsUtil.checkSensitiveWords("商品二级分类名称", secondCategoryName);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
            	
        		searchGoodsCategory.setCategoryName(secondCategoryName);
        		List<GoodsCategoryDto> secondtCategoryList = goodsCategoryDao.queryGoodsCategory(searchGoodsCategory);
        		
             	GoodsCategoryDto secondCategory = null;
              	if (CollectionUtils.isEmpty(secondtCategoryList)) {
              		secondCategory = new GoodsCategoryDto();
              		secondCategory.setShopId(shopId);
              		secondCategory.setCategoryName(secondCategoryName);
              		secondCategory.setColumnId(shop.getColumnId());
              		secondCategory.setParentCategoryId(firstCategory.getGoodsCategoryId());
              		
              		searchCategoryIndex.setShopId(shopId);
              		searchCategoryIndex.setParentCategoryId(secondCategory.getParentCategoryId());
              		Integer categoryIndex = goodsCategoryDao.queryGoodsCategoryLastIndex(searchCategoryIndex);
              		secondCategory.setCategoryIndex(categoryIndex == null ? 1 : ++categoryIndex);
              		goodsCategoryDao.addGoodsCategory(secondCategory);
              	}else {
              		secondCategory = secondtCategoryList.get(0);
              	}
              	
              	importGoods.setGoodsCategoryId(secondCategory.getGoodsCategoryId());
              	parentCategoryId = secondCategory.getParentCategoryId();
        	}
        	
        	importGoods.setGoodsSecondCategoryName(secondCategoryName);
        	
           	if (excelColumnIndexMap.get("价格") == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"请选择正确的商品导入模板");
        	}
           	
        	String price = excelRowData.get(excelColumnIndexMap.get("价格"));
        	
       		if (StringUtils.isBlank(price) || !CommonValidUtil.validDecimalStr(price.trim())) {
    			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, StringUtils.isBlank(price) ? "价格不能为空":"价格类型错误"));
        		continue;
    		}
       		
			if (Double.valueOf(price) >= CommonConst.PRICE_LIMIT) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "价格位数超出限制"));
				continue;
			}
			
       		Double settlePrice = Double.valueOf(price);
        	importGoods.setStandardPrice(settlePrice);
        	
        	String unitName = excelRowData.get(excelColumnIndexMap.get("单位"));
          	if (StringUtils.isBlank(unitName)) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "单位不能为空"));
        		continue;
        	}
          	
          	unitName = unitName.trim();
         	if (unitName.length() > 4) {
        		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "单位名称长度超出限制"));
        		continue;
        	}
         	
         	try {
        		SensitiveWordsUtil.checkSensitiveWords("单位名称", unitName);
			} catch (ValidateException e) {
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
        		continue;
			}
          	long unitId = collectService.saveUnit(unitName, shopId, null);
          	importGoods.setUnitId(unitId);
          	importGoods.setUnitName(unitName);
          	
          	StringBuilder goodsProValuesNames = new StringBuilder();
          	Map<String, String> goodsProValueMapper = new HashMap<String, String>();
          	List<String> groupPropertyNameList = new LinkedList<String>();
          	
          	String sku1 = excelRowData.get(excelColumnIndexMap.get("商品规格1"));
        	String sku1Value = excelColumnIndexMap.get("规格项1") == null ? null : excelRowData.get(excelColumnIndexMap.get("规格项1"));
        	if (StringUtils.isNotBlank(sku1)) {
        		sku1 = sku1.trim();
        	   	if (sku1.length() > 5) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品规格1长度超出限制"));
            		continue;
            	}
        	   	
        		if (StringUtils.isBlank(sku1Value)) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格项1为空"));
            		continue;
        		}
        		sku1Value = sku1Value.trim();
        		goodsProValuesNames.append(sku1Value);
        		goodsProValueMapper.put(sku1, sku1Value);
        		groupPropertyNameList.add(sku1);
        	}
        	
        	String sku2 = excelRowData.get(excelColumnIndexMap.get("商品规格2"));
        	String sku2Value = excelColumnIndexMap.get("规格项2") == null ? null : excelRowData.get(excelColumnIndexMap.get("规格项2"));
        	
        	if (StringUtils.isNotBlank(sku2)) {
        		sku2 = sku2.trim();
        	   	if (sku2.length() > 5) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品规格2长度超出限制"));
            		continue;
            	}
        	   	
        		if (StringUtils.isBlank(sku2Value)) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "规格项2为空"));
            		continue;
        		}
        		sku2Value = sku2Value.trim();
        		if (goodsProValuesNames.toString().isEmpty()) {
        			goodsProValuesNames.append(sku2Value);
        		}else {
        			goodsProValuesNames.append(",").append(sku2Value);
        		}
        		goodsProValueMapper.put(sku2, sku2Value);
        		groupPropertyNameList.add(sku2);
        	}
        	
        	if (!goodsProValuesNames.toString().isEmpty()) {
        		importGoods.setGoodsProValuesNames(goodsProValuesNames.toString());
        		importGoods.setGoodsProValueMapper(goodsProValueMapper);
        	}else {
        		importGoods.setGoodsProValuesNames("");
        	}
        	
        	String goodsDesc = excelRowData.get(excelColumnIndexMap.get("商品简介"));
        	String goodsDetailDesc = excelRowData.get(excelColumnIndexMap.get("详细介绍"));
        	
        	if (StringUtils.isNotBlank(goodsDesc)) {
        		goodsDesc = goodsDesc.trim();
        	   	if (goodsDesc.length() > 200) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品简介长度超出限制"));
            		continue;
            	}
        	   	
              	try {
            		SensitiveWordsUtil.checkSensitiveWords("商品简介", goodsDesc);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
        	   	importGoods.setGoodsDesc(goodsDesc);
        	}
        	
        	if (StringUtils.isNotBlank(goodsDetailDesc)) {
        		goodsDetailDesc = goodsDetailDesc.trim();
        	/*   	if (goodsDetailDesc.length() > 500) {
            		failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "商品详细介绍长度超出限制"));
            		continue;
            	}*/
        	   	
               	try {
            		SensitiveWordsUtil.checkSensitiveWords("详细介绍", goodsDetailDesc);
    			} catch (ValidateException e) {
    				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, e.getMessage()));
            		continue;
    			}
        	   	importGoods.setGoodsDetailDesc(goodsDetailDesc);
        	}
        	
        	
        	String storage = excelRowData.get(excelColumnIndexMap.get("库存"));
        	if (StringUtils.isNotBlank(storage)) {
        		storage = storage.trim();
        		if (!CommonValidUtil.validDecimalStr(storage)) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "库存类型错误"));
            		continue;
        		}
        		importGoods.setStorageTotalNumber(Double.valueOf(storage));
        	}
        	
        	String alarmNumMax = excelRowData.get(excelColumnIndexMap.get("库存告警最大值"));
        	
        	if (StringUtils.isNotBlank(alarmNumMax)) {
        		alarmNumMax = alarmNumMax.trim();
        		if (!CommonValidUtil.validDecimalStr(alarmNumMax)) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "库存告警最大值类型错误"));
            		continue;
        		}
        		importGoods.setAlarmNumberMax(Double.valueOf(alarmNumMax));
        	}
        	
        	String alarmNumMin = excelRowData.get(excelColumnIndexMap.get("库存告警最小值"));
        	
        	if (StringUtils.isNotBlank(alarmNumMin)) {
        		alarmNumMin = alarmNumMin.trim();
        		if (!CommonValidUtil.validDecimalStr(alarmNumMin)) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "库存告警最小值类型错误"));
            		continue;
        		}
        		importGoods.setAlarmNumberMin(Double.valueOf(alarmNumMin));
        	}
        	
        	if (importGoods.getAlarmNumberMax() != null && importGoods.getAlarmNumberMin() != null) {
        		if (importGoods.getAlarmNumberMax() < importGoods.getAlarmNumberMin()) {
        			failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "库存告警最大值不能小于最小值"));
            		continue;
        		}
        	}
        	
            Double memberDiscount = shop.getMemberDiscount();
            Double discountPrice = NumberUtil.multiply(settlePrice, memberDiscount);
            importGoods.setDiscountPrice(discountPrice);
            importGoods.setVipPrice(discountPrice);
            importGoods.setFinalPrice(discountPrice);
            importGoods.setLastUpdateTime(now);
            
            try {
    			GoodsDto existGood = goodsDao.queryExistGood(importGoods);
    			if (existGood != null) {
    				importGoods.setGoodsId(existGood.getGoodsId());
    				Double storageNumber = importGoods.getStorageTotalNumber();
    				if (storageNumber != null) {
    					importGoods.setStorageTotalNumber(NumberUtil.add(storageNumber, existGood.getStorageTotalNumber()));
    				}
    				goodsDao.updateGoods(importGoods);
    				importGoods.setGoodsGroupId(existGood.getGoodsGroupId());
    				updateGoodsGroupPriceRange(importGoods);
    				if (storageNumber != null && storageNumber > 0) {
    					importGoods.setStorageAfterNumber(importGoods.getStorageTotalNumber());
    					importGoods.setStorageTotalNumber(storageNumber);
    					storageServcie.insertShopStorage(importGoods);
    				}
    			}else {
    				if (sku1 == null && sku2 == null) {
                    	importGoods.setCreateTime(now);
        				insertGoods(importGoods);
                    }else {
                    	GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
                    	goodsGroupDto.setShopId(shopId);
                    	goodsGroupDto.setGoodsName(goodsName);
                    	goodsGroupDto.setGoodsDesc(importGoods.getGoodsDesc());
                    	goodsGroupDto.setGoodsDetailDesc(importGoods.getGoodsDetailDesc());
                    	goodsGroupDto.setMinPrice(new BigDecimal(settlePrice));
                    	goodsGroupDto.setMaxPrice(new BigDecimal(settlePrice));
                    	goodsGroupDto.setUnitId(unitId);
                    	goodsGroupDto.setUnit(unitName);
                    	List<Long> categoryIdList = new LinkedList<Long>();
                    	categoryIdList.add(importGoods.getGoodsCategoryId());
                    	goodsGroupDto.setCategoryIdList(categoryIdList);
                    	goodsGroupDto.setParentCategoryId(parentCategoryId);
                    	goodsGroupDto.setGroupPropertyName(groupPropertyNameList);
                    	goodsGroupDto.setPinyincode(importGoods.getPinyinCode());
                    	Long goodsGroupId = addGoodsGroup(goodsGroupDto);
                    	importGoods.setGoodsGroupId(goodsGroupId);
                    	
        				StringBuffer tempGroupPropertyIds = new StringBuffer();
        				StringBuffer tempGoodsProValuesIds = new StringBuffer();
        				
        				ShopGoodsPropertyDto searchShopGoodsProperty = new ShopGoodsPropertyDto();
        				ShopGoodsPropertyDto searchShopGoodsPropertyValue = new ShopGoodsPropertyDto();
        				searchShopGoodsPropertyValue.setShopId(shopId);
        				
        				for (String propertyName : goodsProValueMapper.keySet()) {
        					String propertyValue = goodsProValueMapper.get(propertyName);
        					//增加店铺规格
        					searchShopGoodsProperty.setShopId(shopId);
        					searchShopGoodsProperty.setShopPropertyId(null);
        					searchShopGoodsProperty.setParentShopPropertyId(null);
        					searchShopGoodsProperty.setSubPropertyName(null);
        					searchShopGoodsProperty.setPropertyOrder(null);
        					searchShopGoodsProperty.setShopPropertyName(propertyName);
        					searchShopGoodsProperty.setSubPropertyName(propertyName);
        					List<ShopGoodsPropertyDto> shopGoodsPropertys = shopGoodsPropertyDao.getShopGoodsProperty(searchShopGoodsProperty);
        					if (CollectionUtils.isEmpty(shopGoodsPropertys)) {
        						shopGoodsPropertyDao.addShopGoodsProperty(searchShopGoodsProperty);
        					}else {
        						Boolean needAddShopProperty = true;
        						for (ShopGoodsPropertyDto shopProperty : shopGoodsPropertys) {
        							Boolean isSingalProperty = shopProperty.getShopPropertyName().equals(shopProperty.getSubPropertyName());
        							if (isSingalProperty || shopProperty.getSubPropertyName() == null) {
        								needAddShopProperty = false;
        								searchShopGoodsProperty = shopProperty;
        								break;
        							}
        						}
        						
        						if (needAddShopProperty) {
        							searchShopGoodsProperty.setShopId(null);
        							searchShopGoodsProperty.setShopPropertyId(null);
                					searchShopGoodsProperty.setParentShopPropertyId(null);
                					searchShopGoodsProperty.setPropertyOrder(null);
                					searchShopGoodsProperty.setShopPropertyName(propertyName);
                					searchShopGoodsProperty.setSubPropertyName(null);
        							shopGoodsPropertyDao.addShopGoodsProperty(searchShopGoodsProperty);
        						}
        						
        					}
        					
        					searchShopGoodsPropertyValue.setShopPropertyId(null);
        					searchShopGoodsPropertyValue.setSubPropertyName(null);
        					searchShopGoodsPropertyValue.setPropertyOrder(null);
        					searchShopGoodsPropertyValue.setShopPropertyName(propertyValue);
        					searchShopGoodsPropertyValue.setParentShopPropertyId(searchShopGoodsProperty.getShopPropertyId());
        					List<ShopGoodsPropertyDto> shopGoodsPropertyValues = shopGoodsPropertyDao.getShopGoodsProperty(searchShopGoodsPropertyValue);
        					if (CollectionUtils.isEmpty(shopGoodsPropertyValues)) {
        						shopGoodsPropertyDao.addShopGoodsProperty(searchShopGoodsPropertyValue);
        					}
        					
        					//增加属性名称，同一商品族下的属性名称不能重复
        					Long groupPropertyId = addGoodsGroupProBackId(goodsGroupId, propertyName,searchShopGoodsProperty.getShopPropertyId());
        					tempGroupPropertyIds.append(groupPropertyId).append(",");
        					
        					//增加属性值，同一个属性值下面属性值不能重复
        					Long proValuesId = addGoodsGoodsGroupProValues(groupPropertyId, propertyValue);
        					tempGoodsProValuesIds.append(proValuesId).append(",");
        					
        				}
                    	
        				String groupPropertyIds = StringUtils.isBlank(tempGroupPropertyIds.toString()) ? "" : tempGroupPropertyIds.substring(0, tempGroupPropertyIds.length()-1);
        				String goodsProValuesIds = StringUtils.isBlank(tempGoodsProValuesIds.toString()) ? "" : tempGoodsProValuesIds.substring(0, tempGoodsProValuesIds.length()-1);
        				importGoods.setGoodsProValuesIds(sortArray(goodsProValuesIds));
        				importGoods.setCreateTime(now);
        				insertGoods(importGoods);
        				addGoodsPropertyDto(groupPropertyIds, goodsProValuesIds, importGoods.getGoodsId());
                    }
    			}
    			syncGoodsIdList.add(importGoods.getGoodsId());
    			DataCacheApi.del(CommonConst.KEY_GOODS + importGoods.getGoodsId());
                
			} catch (Exception e) {
				logger.error("Excel批量导入商品失败 -- 行数：{} 失败原因：{}", rowNo,e);
				logger.error("失败详细原因", e);
				failList.add(getExcelFailRowInfo(rowNo, excelRowData, excelColumnIndexMap, "导入失败"));
			}
        }
	}
	private Map<String, String> getExcelFailRowInfo(Integer rowNo,List<String> excelRowData,
													Map<String, Integer> excelColumnIndexMap,String failReason) {
		
			Map<String, String> failRowInfo = new LinkedHashMap<String, String>();
			
			for (Entry<String, Integer> columnIndex : excelColumnIndexMap.entrySet()) {
				failRowInfo.put(columnIndex.getKey(), excelRowData.get(columnIndex.getValue()));
			}
			
			failRowInfo.put("出错行数", rowNo.toString());
			failRowInfo.put("出错原因", failReason);
			return failRowInfo;
	}
	
	private void insertGoods(GoodsDto goodsDto) throws Exception {
		Long goodsId = goodsDao.addGoodsDto(goodsDto);
		if(goodsDto.getBarcode()!=null){
		    GoodsBarcodeDto goodsBarcodeDto=new GoodsBarcodeDto();
		    goodsBarcodeDto.setBarcode(goodsDto.getBarcode());
            goodsBarcodeDto.setGoodsName(goodsDto.getGoodsName());
            goodsBarcodeDto.setGoodsSize(goodsDto.getSpecsDesc());
            goodsBarcodeDto.setInPrice(goodsDto.getDiscountPrice()==0 ? 1 : goodsDto.getDiscountPrice());
            goodsBarcodeDto.setProductName(goodsDto.getGoodsNo());
            goodsBarcodeDto.setSalePrice(goodsDto.getFinalPrice());
            goodsBarcodeDto.setUnitName(goodsDto.getUnitName());
			Map map=collectDao.getStandardGoodsByBarCode(goodsDto.getBarcode());
			if(map==null){
				goodsDao.addGoodsBarcode(goodsBarcodeDto);
			}else if(map.get("id")!=null){
				goodsBarcodeDto.setId(Integer.valueOf(String.valueOf(map.get("id"))));
				goodsDao.updateGoodsBarcode(goodsBarcodeDto);
			}
		}
		
		if (goodsDto.getStorageTotalNumber() != null && goodsDto.getStorageTotalNumber() > 0) {
			storageServcie.insertShopStorage(goodsDto);
		}
		updateGoodsGroupPriceRange(goodsDto);
		levelService.pushAddPointMessage(3, null, 1, goodsDto.getShopId().intValue(), 3, goodsId.toString(),true);
	}

	private void updateGoodsGroupPriceRange(GoodsDto goodsDto) throws Exception{
		Double standardPrice = goodsDto.getStandardPrice();
		Long goodsGroupId = goodsDto.getGoodsGroupId();
		if (goodsGroupId != null) {
			GoodsGroupDto goodsGroup = new GoodsGroupDto();
			goodsGroup.setGoodsGroupId(goodsGroupId);
			Double maxPrice = goodsGroupDao.getGoodsGroupMaxPrice(goodsGroupId);
			Double minPrice = goodsGroupDao.getGoodsGroupMinPrice(goodsGroupId);
			goodsGroup.setMinPrice(BigDecimal.valueOf(standardPrice < minPrice ? standardPrice : minPrice));
			goodsGroup.setMaxPrice(BigDecimal.valueOf(standardPrice > maxPrice ? standardPrice : maxPrice));
			goodsGroupDao.updateGoodsGroup(goodsGroup);
		}
	}
	
	@Override
	public boolean processGoodsSoldOut(Long shopId, List<Map<String, String>> goodsList) {
		//TODO 该函数可优化
		Iterator<Map<String, String>> iterator = goodsList.iterator();
		Map<String, String> param = null;
		String operatorIdStr = null;
		String operatorName = null;
		Long operatorId = null;
		Date soldOutTime = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while(iterator.hasNext()){
			param = iterator.next();
			String bizType = param.get("bizType");
			String soldOutFlag = param.get("soldOutFlag");
			String bizId = param.get("bizId");
			String soldOutTimeStr = param.get("soldOutTime");
			bizType = bizType.trim();
			List<GoodsSoldOutDto> condition = new ArrayList<>();
			if(StringUtils.isBlank(soldOutFlag))
			{
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "soldOutFlag不能为空");
			}
			boolean delete = "0".equals(soldOutFlag.trim());
			if(!delete){
				operatorIdStr = param.get("operatorId");
				operatorName = param.get("operatorName");

				if(StringUtils.isBlank(operatorName)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operatorName不能为空");
				}
				if(StringUtils.isBlank(operatorIdStr)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operatorId不能为空");
				}
				try{
					operatorId = Long.parseLong(operatorIdStr);
				}catch (Exception e){
					logger.error(e.getMessage(), e);
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "operatorId格式不正确");
				}
				if(StringUtils.isBlank(soldOutTimeStr))
				{
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "soldOutTime不能为空");
				}
				try{
					soldOutTime = dateFormat.parse(soldOutTimeStr);
				}catch (Exception e){
					logger.error(e.getMessage(), e);
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "soldOutTime格式不正确");
				}
			}
			if("1".equals(bizType))	//按商品
			{

				if(delete)	//未沽清
				{
					GoodsSoldOutDto goodsSoldOutDto = new GoodsSoldOutDto();
					goodsSoldOutDto.setShopId(shopId);
					goodsSoldOutDto.setGoodsId(Long.parseLong(bizId));
					condition.add(goodsSoldOutDto);
				}else {					//已沽清
					GoodsDto goodsDto = null;
					try {
						goodsDto = goodsDao.getGoodsById(Long.parseLong(bizId.trim()));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					if(null == goodsDto)
					{
						throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "商品不存在");
					}
					List<GoodsDto> soodsDtos = new ArrayList<>();
					soodsDtos.add(goodsDto);
					this.putIfAbsent(soodsDtos, shopId, operatorId, operatorName, soldOutTime);
				}
			}
			else if("2".equals(bizType))	//按category
			{
				GoodsDto goodsDto = new GoodsDto();
				goodsDto.setShopId(shopId);
				goodsDto.setGoodsCategoryId(Long.parseLong(bizId));
				this.dealtGoodsSoldOutByCategoryIdOrAll(goodsDto, shopId, operatorId, operatorName, condition, delete, soldOutTime);
			}
			else if("3".equals(bizType))	//所有全部
			{
				GoodsDto goodsDto = new GoodsDto();
				goodsDto.setShopId(shopId);
				this.dealtGoodsSoldOutByCategoryIdOrAll(goodsDto, shopId, operatorId, operatorName, condition, delete, soldOutTime);
			}
			if(condition != null && !condition.isEmpty())	//判断是否删除
			{
				goodsSoldOutDao.batchDelete(condition);
			}
		}
		return true;
	}

	/**
	 *
	 * @param getCondition
	 * @param shopId
	 * @param operatorId
	 * @param operatorName
	 * @param deletes
	 * @param delete
     * @return 添加返回true
     */
	private boolean dealtGoodsSoldOutByCategoryIdOrAll(GoodsDto getCondition, Long shopId, Long operatorId, String operatorName, List<GoodsSoldOutDto> deletes, boolean delete, Date soldOutTime){
		List<GoodsDto> goodsDtos = goodsDao.queryGoodsByShopIdAndCategoryId(getCondition, 0, Integer.MAX_VALUE);
		if(null != goodsDtos && !goodsDtos.isEmpty()) {
			if (!delete) {    //如果是沽清
				this.putIfAbsent(goodsDtos, shopId, operatorId, operatorName, soldOutTime);
				return true;
			}
			//以下为未沽清的情况下
			Iterator<GoodsDto> iterator1 = goodsDtos.iterator();
			GoodsDto temp = null;
			while (iterator1.hasNext()) {
				temp = iterator1.next();
				deletes.add(this.initGoodsSoldOutDtoByGoodsDto(temp, operatorId, operatorName, soldOutTime));
			}
		}
		return false;
	}

	private GoodsSoldOutDto initGoodsSoldOutDtoByGoodsDto(GoodsDto origin, Long operatorId, String operatorName, Date soldOutTime){
		GoodsSoldOutDto Target = new GoodsSoldOutDto();
		Target.setCreateTime(soldOutTime);
		Target.setGoodsCategoryId(origin.getGoodsCategoryId());
		Target.setGoodsStatus(origin.getGoodsStatus());
		Target.setShopId(origin.getShopId());
		Target.setGoodsId(origin.getGoodsId());
		Target.setOperatorId(operatorId);
		Target.setOperatorName(operatorName);
		return Target;
	}

	private void putIfAbsent(List<GoodsDto> goodsDtos, Long shopId, Long operatorId, String operatorName, Date soldOutTime){
		List<GoodsSoldOutDto> inserts = new ArrayList<>();
		if(goodsDtos.size() == 1)
		{
			GoodsSoldOutDto insert = this.initGoodsSoldOutDtoByGoodsDto(goodsDtos.get(0), operatorId, operatorName, soldOutTime);
			List<GoodsSoldOutDto> list = goodsSoldOutDao.getItemsByCondition(insert, 1, 10);
			if(null == list || list.isEmpty()){
				inserts.add(insert);
			}

		}else {
			Iterator<GoodsDto> iterator = goodsDtos.iterator();
			GoodsDto temp = null;
			while (iterator.hasNext()){
				temp = iterator.next();
				inserts.add(this.initGoodsSoldOutDtoByGoodsDto(temp, operatorId, operatorName, soldOutTime));
			}
		}
		if(!inserts.isEmpty())
		{
			goodsSoldOutDao.batchInsert(inserts);
		}
	}

	@Override
	public List<GoodsSoldOutDto> getSoldOuts(Long shopId) {
		GoodsSoldOutDto goodsSoldOutDto = new GoodsSoldOutDto();
		goodsSoldOutDto.setShopId(shopId);
		return goodsSoldOutDao.getItemsByCondition(goodsSoldOutDto, 1, Integer.MAX_VALUE);
	}
	
	@Override
	public List<GoodsAvsDto> getGoodsAvsList(GoodsAvsDto goodsAvs) {
		return goodsAvsDao.getGoodsAvsList(goodsAvs);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.goods.IGoodsServcie#updateGoodsAvs(com.idcq.appserver.dto.goods.UpdateGoodsAvs)
	 */
	@Override
	public List<Map<String, Object>> updateGoodsAvs(UpdateGoodsAvs updateGoodsAvs) throws Exception {
		// 0-新增，1-修改
		Integer operateType  = updateGoodsAvs.getOperateType();
		List<GoodsAvsDto> goodsAvsList = updateGoodsAvs.getAvsList();
		List<Map<String, Object>> reMap = new ArrayList<Map<String,Object>>();
		if(operateType==0){
			for (GoodsAvsDto avs : goodsAvsList) {
				
				validGoodsAvs(avs, operateType);
				avs.setGoodsAvsId(null);
				goodsAvsDao.insertGoodsAvsDto(avs);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("goodsAvsId", avs.getGoodsAvsId());
				resultMap.put("avsName", avs.getAvsName());
				resultMap.put("shopId", avs.getShopId());
				resultMap.put("avsPrice", avs.getAvsPrice());

				reMap.add(resultMap);
			}
		}
		else{
			for (GoodsAvsDto avs : goodsAvsList) {
				
				validGoodsAvs(avs, operateType);
				
				goodsAvsDao.updateGoodsAvsDto(avs);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("goodsAvsId", avs.getGoodsAvsId());
				resultMap.put("avsName", avs.getAvsName());
				resultMap.put("shopId", avs.getShopId());
				resultMap.put("avsPrice", avs.getAvsPrice());

				reMap.add(resultMap);
			}

		}
		return reMap;
		
	}
	
	public void validGoodsAvs(GoodsAvsDto goodsAvsDto,Integer operateType) throws Exception {
		if(operateType==1){
			CommonValidUtil.validObjectNull(goodsAvsDto.getGoodsAvsId(), CodeConst.CODE_PARAMETER_NOT_VALID, "goodsAvsId不能为空");
		}
		CommonValidUtil.validObjectNull(goodsAvsDto.getAvsName(), CodeConst.CODE_PARAMETER_NOT_VALID, "avsName不能为空");
		CommonValidUtil.validObjectNull(goodsAvsDto.getAvsFlag(), CodeConst.CODE_PARAMETER_NOT_VALID, "avsFlag不能为空");
		CommonValidUtil.validObjectNull(goodsAvsDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
		
        		GoodsAvsDto parms = new GoodsAvsDto();
		parms.setShopId(goodsAvsDto.getShopId());
		parms.setAvsName(goodsAvsDto.getAvsName());
		if(operateType==1){//修改的时候才增加id查询
			parms.setGoodsAvsId(goodsAvsDto.getGoodsAvsId());
		}
		parms.setPageNo((goodsAvsDto.getPageNo()-1)*goodsAvsDto.getPageSize());
		List<GoodsAvsDto> avsList = goodsAvsDao.getGoodsAvsList(parms);
		if(CollectionUtils.isNotEmpty(avsList)){
			//修改
			if(operateType==1 && 1==avsList.size()){
				//交易增值服务id是否与其他服务id一致
				if(avsList.get(0).getGoodsAvsId()==goodsAvsDto.getGoodsAvsId()){
			        return;
				}
				
			}
			else{
		        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "增值服务已经存在");

			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.goods.IGoodsServcie#deleteGoodsAvs(java.lang.Long)
	 */
	@Override
	public int deleteGoodsAvs(Long goodsAvsId) {
		return goodsAvsDao.deleteGoodsAvs(goodsAvsId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.goods.IGoodsServcie#getGoodsAvsCount(com.idcq.appserver.dto.goods.GoodsAvsDto)
	 */
	@Override
	public int getGoodsAvsCount(GoodsAvsDto goodsAvs) {
		return goodsAvsDao.getGoodsAvsCount(goodsAvs);
	}

}
