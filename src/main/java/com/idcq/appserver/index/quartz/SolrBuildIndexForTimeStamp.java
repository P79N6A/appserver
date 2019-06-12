/**
 * 
 */
package com.idcq.appserver.index.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.shop.ShopController;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.ITakeoutSettingDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.temporyOperate.ITemporyOperateDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.TakeoutSettingDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
import com.idcq.appserver.index.quartz.job.SolrCatchDataJob;
import com.idcq.appserver.service.temporyOperate.ITemporyOperateService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.solr.SolrContext;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;

/** 
 * @ClassName: SolrBuildIndexForTimeStamp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月28日 下午3:00:41 
 *  
 */
public class SolrBuildIndexForTimeStamp {
	
	private ITemporyOperateDao temporyOperateDao;
	
	private IShopDao shopDao;
	
	private IGoodsDao goodsDao;
	
	private ITemporyOperateService temporyOperateService;
	
	private static ITakeoutSettingDao takeoutSettingDao;
	
	private static final Logger logger =Logger.getLogger(ShopController.class);
	
	private IGoodsGroupDao goodsGroupDao;
	
	public SolrBuildIndexForTimeStamp()
	{
		if(temporyOperateDao==null)
		{
			temporyOperateDao=BeanFactory.getBean(ITemporyOperateDao.class);
		}
		if(goodsGroupDao==null)
		{
			goodsGroupDao=BeanFactory.getBean(IGoodsGroupDao.class);
		}
		if(shopDao==null)
		{
			shopDao=BeanFactory.getBean(IShopDao.class);
		}
		if(goodsDao==null)
		{
			goodsDao=BeanFactory.getBean(IGoodsDao.class);
		}
	}
	/**
	 * 加载需要索引的数据根据上次索引更新时间
	* @Title: loadNewDataToTemporyIndex 
	* @Description: TODO
	* @param @param lastUpdateTime
	* @return void    返回类型 
	* @throws
	 */
	public void loadNewDataToTemporyIndex(final String lastUpdateTime,final String ip)
	{
		
		new Thread(){
			public void run(){
				buildShopIndex(lastUpdateTime,ip);
				buildShopForMarketIndex(lastUpdateTime,ip);
			}
		}.start();
		
		new Thread(){
			public void run(){
				buildGoodsIndex(lastUpdateTime,ip);
			}
		}.start();
		new Thread(){
			public void run()
			{
				buildDeleteIndex();
			}			
		}.start();
		new Thread(){
			public void run()
			{
				buildGoodsGroupIndex(lastUpdateTime,ip);
			}

			
		}.start();
	}
	
	
	/**
	 *将删除了的数据在索引中同步删除
	* @Title: buildDeleteIndex 
	* @Description: TODO
	* @param 
	* @return void    返回类型 
	* @throws
	 */
	public  void buildDeleteIndex()
	{
		
		if(temporyOperateService==null)
		{
			temporyOperateService=BeanFactory.getBean(ITemporyOperateService.class);
		}
		deleteIndexByType(CommonConst.INDEX_TYPE_IS_SHOP);
		deleteIndexByType(CommonConst.INDEX_TYPE_IS_GOODS);
	}
	
	/**
	 * 删除商品中索引
	* @Title: deleteGoods 
	* @param 
	* @return void    返回类型 
	* @throws
	 */
	private void deleteIndexByType(String deleteType)
	{
		Map<String,Object>deleteParams=new HashMap<String,Object>();
		while(true)
		{	
			List<TemporyOperateDto>temporyOperateDtos=temporyOperateDao.queryList(deleteType,CommonConst.CATCH_PAGE_SIZE);
			if(temporyOperateDtos.size()==0)
			{
				break;
			}
			List<String>deleteIdList=new ArrayList<String>();
			List<Long>deleteIds=new ArrayList<Long>();
			for(TemporyOperateDto temporyOperateDto:temporyOperateDtos)
			{
				if(CommonConst.INDEX_DELETE.equals(temporyOperateDto.getOperateType()))
				{
					String type=temporyOperateDto.getType();
					String relateId=temporyOperateDto.getId()+"";
					deleteIds.add(temporyOperateDto.getId());
					if(CommonConst.INDEX_TYPE_IS_SHOP.equals(type))
					{
						relateId="shop"+relateId;
					}
					else if(CommonConst.INDEX_TYPE_IS_GOODS.equals(type)) 
					{
						relateId="goods"+relateId;
					}
					deleteIdList.add(relateId);
				}
			}
			if(deleteIdList.size()>0)
			{
				SolrContext.getInstance().deleteByIds(deleteIdList);
			}
			if(deleteIds.size()>0)
			{
				deleteParams.put("list", deleteIds);
				temporyOperateService.deleteByParams(deleteParams);
			}
		}
	}
	
	
	/**
	 * 
	 * @Title: buildShopIndex 
	 * @Description: TODO
	 * @param @param lastUpdateTime
	 * @return void    返回类型 
	 * @throws
	 */
	public void buildShopIndex(String lastUpdateTime,String ip)
	{
		int defaultPageSize=50;
		SolrCatchDataJob job=new SolrCatchDataJob();
		try {
			int count=temporyOperateDao.getNeedIndexShopCount(lastUpdateTime);//需要更新的商铺条数
			if(count>0)
			{
				int pageNum=count/defaultPageSize;//代表需要分多少页
				if(pageNum*defaultPageSize<count)
				{
					pageNum++;
				}
				PageModel pageModel=new PageModel();
				for(int currentPage=1;currentPage<=pageNum;currentPage++)
				{
					pageModel.setToPage(currentPage);
					List<ShopDto>shopDtos=shopDao.getShopByPageAndLastUpdateTime(lastUpdateTime, pageModel);
					if(shopDtos.size()==0)
					{
						break;
					}
					//findShopPColumnId(shopDtos);
					job.parseShopData(shopDtos,ip);
					batchSynchronizeGoods(shopDtos,ip);
					batchSynchronizeGoodsGroup(shopDtos, ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 构建商铺索引数据
	 * @Title: buildShopForMarketIndex 
	 * @Description: TODO
	 * @param @param lastUpdateTime
	 * @return void    返回类型 
	 * @throws
	 */
	public void buildShopForMarketIndex(String lastUpdateTime,String ip)
	{
		int defaultPageSize=500;
		SolrCatchDataJob job=new SolrCatchDataJob();
		try {
			int count=temporyOperateDao.getNeedIndexShopForMarketCount(lastUpdateTime);//需要更新的商铺条数
			if(count>0)
			{
				int pageNum=count/defaultPageSize;//代表需要分多少页
				if(pageNum*defaultPageSize<count)
				{
					pageNum++;
				}
				PageModel pageModel=new PageModel();
				for(int currentPage=1;currentPage<=pageNum;currentPage++)
				{
					pageModel.setPageSize(defaultPageSize);
					pageModel.setToPage(currentPage);
					List<ShopDto>shopDtos=shopDao.getShopMarketInfoByParam(lastUpdateTime, pageModel);
					if(shopDtos.size()==0)
					{
						break;
					}
					job.parseShopData(shopDtos,ip);
					batchSynchronizeGoods(shopDtos,ip);
					batchSynchronizeGoodsGroup(shopDtos, ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void batchSynchronizeGoods(List<ShopDto>shopDtos,String ip)
	{
		for(ShopDto shopDto:shopDtos)
		{
			if(shopDto!=null&&shopDto.getShopId()!=null)
			{
				buildGoodsForShopChange(shopDto.getShopId(),ip);
			}
		}
	}
	
	/**
	 * 批量同步商品族
	 * @Title: batchSynchronizeGoodsGroup 
	 * @param @param shopDtos
	 * @param @param ip
	 * @return void    返回类型 
	 * @throws
	 */
	public void batchSynchronizeGoodsGroup(List<ShopDto>shopDtos,String ip)
	{
		for(ShopDto shopDto:shopDtos)
		{
			if(shopDto!=null&&shopDto.getShopId()!=null)
			{
				buildGoodsGroupForShopChange(shopDto.getShopId(),ip);
			}
		}
	}
	
	/**
	 * 因为商品发生了改变所以商品信息也要同步改变
	 * @Title: buildGoodsGroupForShopChange 
	 * @param @param shopId
	 * @param @param ip
	 * @return void    返回类型 
	 * @throws
	 */
	public void buildGoodsGroupForShopChange(Long shopId, String ip) {
		SolrCatchDataJob job=new SolrCatchDataJob();
		logger.info("同步商铺:"+shopId+"关联的商品信息--start");
		PageModel pageModel=new PageModel();
		pageModel.setPageSize(80);
		GoodsGroupDto goodsGroupDto=new GoodsGroupDto();
		goodsGroupDto.setShopId(shopId);
		while(true)
		{
			try{
				PageModel pageResult=goodsGroupDao.getGoodsGroupListByCondition(goodsGroupDto, pageModel);
				List<GoodsGroupDto>goodsGroupDtos=pageResult.getList();
				if(goodsGroupDtos==null||goodsGroupDtos.size()==0)
				{
					break;
				}
				job.parseGoodsData(convertGoodsGroupToGoodsForProp(goodsGroupDtos),ip,1);
				pageModel.setToPage(pageModel.getToPage()+1);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		logger.info("同步商铺:"+shopId+"关联的商品信息--end");
	}
	/**
	 * 构建商品数据.
	* @Title: buildGoodsIndex 
	* @Description: TODO
	* @param @param lastUpdateTime
	* @return void    返回类型 
	* @throws
	 */
	public void buildGoodsIndex(String lastUpdateTime,String ip)
	{
		SolrCatchDataJob job=new SolrCatchDataJob();
		int defaultPageSize=50;
		try {
			int count=temporyOperateDao.getNeedIndexGoodsCount(lastUpdateTime);//需要更新的商铺条数
			if(count>0)
			{
				int pageNum=count/defaultPageSize;//代表需要分多少页
				if(pageNum*defaultPageSize<count)
				{
					pageNum++;
				}
				PageModel pageModel=new PageModel();
				for(int currentPage=1;currentPage<=pageNum;currentPage++)
				{
					pageModel.setPageSize(defaultPageSize);
					pageModel.setToPage(currentPage);
					List<GoodsDto>goodsDtos=goodsDao.getGoodsByPageAndLastUpdateTime(lastUpdateTime, pageModel);
					if(goodsDtos.size()==0)
					{
						break;
					}
					job.parseGoodsData(goodsDtos,ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造商品族索引
	 * @Title: buildGoodsGroupIndex 
	 * @param @param lastUpdateTime
	 * @param @param ip
	 * @return void    返回类型 
	 * @throws
	 */
	private void buildGoodsGroupIndex(String lastUpdateTime, String ip) {
		SolrCatchDataJob job=new SolrCatchDataJob();
		int defaultPageSize=50;
		try {
			int count=temporyOperateDao.getNeedIndexGoodsGroupCount(lastUpdateTime);//需要更新的商品族条数
			if(count>0)
			{
				int pageNum=count/defaultPageSize;//代表需要分多少页
				if(pageNum*defaultPageSize<count)
				{
					pageNum++;
				}
				PageModel pageModel=new PageModel();
				for(int currentPage=1;currentPage<=pageNum;currentPage++)
				{
					pageModel.setPageSize(defaultPageSize);
					pageModel.setToPage(currentPage);
					List<GoodsGroupDto>goodsGroupDtos=goodsGroupDao.getGoodsByPageAndLastUpdateTime(lastUpdateTime, pageModel);
					if(goodsGroupDtos.size()==0)
					{
						break;
					}
					job.parseGoodsData(convertGoodsGroupToGoodsForProp(goodsGroupDtos),ip,1);
				}
			}
			List<Object>groupIdList=goodsDao.getDistinctGoodsGroupIdForIndex(lastUpdateTime);
			if(groupIdList.size()>0)
			{
				List<GoodsGroupDto>groupDtoList=goodsGroupDao.findGoodsGroupByIdList(groupIdList);
				job.parseGoodsData(convertGoodsGroupToGoodsForProp(groupDtoList),ip,1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将商品族转换为商品
	 * @Title: convertGoodsGroupToGoodsForProp 
	 * @param @param goodsGroupList
	 * @param @return
	 * @return List<GoodsDto>    返回类型 
	 * @throws
	 */
	public   List<GoodsDto> convertGoodsGroupToGoodsForProp(List<GoodsGroupDto>goodsGroupList)throws Exception
	{
		Map<Long,GoodsDto>goodsDtoMap=new HashMap<Long,GoodsDto>();
		List<Long>goodsGroupIdList=new ArrayList<Long>();
		List<GoodsDto>goodsDtos=new ArrayList<GoodsDto>();
		for(GoodsGroupDto goodsGroupDto:goodsGroupList)
		{
			GoodsDto dto=new GoodsDto();
			DataConvertUtil.propertyConvertIncludeDefaultProp(goodsGroupDto,dto , CommonConst.GET_GOODS_GROUP_DETAIL);
			goodsDtos.add(dto);
			dto.setGoodsId(goodsGroupDto.getGoodsGroupId());
			goodsGroupIdList.add(dto.getGoodsId());
			goodsDtoMap.put(dto.getGoodsId(), dto);
		}
		if(goodsDao==null)
		{
			goodsDao=BeanFactory.getBean(IGoodsDao.class);
		}
		List<GoodsDto>goodsNameDtoList=goodsDao.getGoodNameListByGroupId(goodsGroupIdList);
		for(GoodsDto dto:goodsNameDtoList)
		{	
			if(dto!=null)
			{	
				GoodsDto goodsGroupInfo=goodsDtoMap.get(dto.getGoodsGroupId());
				if(goodsGroupInfo!=null)
				{
					goodsGroupInfo.setGoodsNameList(DataConvertUtil.convertStrToList(dto.getGoodsName()+",", CommonConst.GOODS_GROUP_SPLIT_STR));
				}
			}
		}
		return goodsDtos;
	}
   /**
	* 商铺信息发生更改，需要同步修改
	* @Title: buildGoodsForShopChange 
	* @param @param shopId
	* @return void    返回类型 
	* @throws
	*/
	public void buildGoodsForShopChange(Long shopId,String ip)
	{
		SolrCatchDataJob job=new SolrCatchDataJob();
		logger.info("同步商铺:"+shopId+"关联的商品信息--start");
		PageModel pageModel=new PageModel();
		pageModel.setPageSize(1000);
		while(true)
		{
			List<GoodsDto>goodsDtos=goodsDao.getGoodsListByShopId(shopId, pageModel);
			if(goodsDtos==null||goodsDtos.size()==0)
			{
				break;
			}
			job.parseGoodsData(goodsDtos,ip);
			pageModel.setToPage(pageModel.getToPage()+1);
		}
		logger.info("同步商铺:"+shopId+"关联的商品信息--end");
	}
	
	/**
	 * 
	 * @Title: findShopLeastBookPrice 
	 * @param @param settingType
	 * @param @param shopIdList
	 * @param @return
	 * @return Map<String,Float>    返回类型 
	 * @throws
	 */
	public static Map<String,Float> findShopLeastBookPrice(Integer settingType,List<Long> shopIdList)
	{
		Map<String,Float>leastBookPriceMap=new HashMap<String,Float>();
		try{
			if(takeoutSettingDao==null)
			{
				takeoutSettingDao=BeanFactory.getBean(ITakeoutSettingDao.class);
			}
			if(shopIdList.size()>0)
			{
				List<TakeoutSettingDto>takeoutSettingList=takeoutSettingDao.findTakeoutSettingByShopList(shopIdList, settingType);
				for(TakeoutSettingDto dto:takeoutSettingList)
				{
					leastBookPriceMap.put(dto.getShopId()+"",dto.getLeastBookPrice());
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		return leastBookPriceMap;
	}
}
