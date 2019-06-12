package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import com.google.code.yanf4j.util.ConcurrentHashSet;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.column.IColumnDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
import com.idcq.appserver.index.quartz.SolrBuildIndexForTimeStamp;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.temporyOperate.ITemporyOperateService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.solr.SearchContent;
import com.idcq.appserver.utils.solr.SearchServer;
import com.idcq.appserver.utils.solr.SolrContext;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;

/**
 * 
* @ClassName: SolrCatchDataJob 
* @Description: TODO(用来爬取数据库中数据建立索引) 
* @author 张鹏程 
* @date 2015年3月28日 下午4:24:03 
*
 */
public class SolrCatchDataJob extends QuartzJobBean{

	private final Log logger = LogFactory.getLog(getClass());
	static ConcurrentHashSet<String>shopExistPage=new ConcurrentHashSet<String>();
	static ConcurrentHashSet<String>goodsExistPage=new ConcurrentHashSet<String>();
	static ConcurrentHashSet<String>errorIndexPage=new ConcurrentHashSet<String>();
	SolrBuildIndexForTimeStamp buildIndexForTimeStamp=new SolrBuildIndexForTimeStamp();
	public static String lastUpdateTime=DateUtils.format(DateUtils.addSeconds(new Date(), -30), "yyyy-MM-dd HH:mm:ss");
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		try {
			String catchMode=CommonConst.DEFAULT_CATCH_MODE;
			if(ContextInitListener.SOLR_PROPS!=null)//判断数据的增量方式 是时间戳还是触发器 触发的
			{		
				String mode=ContextInitListener.SOLR_PROPS.getProperty("solr.catchMode");
				if(!StringUtils.isEmpty(mode))
				{
					catchMode=mode;
				}
			}
			if(CommonConst.DEFAULT_CATCH_MODE.equals(catchMode))//如果是时间戳方式
			{
				String temporyUpdateTime=DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				Date lastUpdateDate=DateUtils.parse(lastUpdateTime,"yyyy-MM-dd HH:mm:ss");
				lastUpdateDate=DateUtils.addSeconds(lastUpdateDate, -30);
				lastUpdateTime=DateUtils.format(lastUpdateDate,  "yyyy-MM-dd HH:mm:ss");
				buildIndexForTimeStamp.loadNewDataToTemporyIndex(lastUpdateTime,null);
				lastUpdateTime=temporyUpdateTime;
			}
			else
			{	
				loadShopData();
				loadGoodsData(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	
	/**
	 * 加载商铺的修改数据
	 */
	private void loadShopData()
	{
		try{
			ITemporyOperateService temporyOperateService=BeanFactory.getBean(ITemporyOperateService.class);
			IShopServcie shopService=BeanFactory.getBean(IShopServcie.class);
			Map<String,Object>deleteParams=new HashMap<String,Object>();
			while(true)
			{	
				List<TemporyOperateDto>temporyOperateDtos=temporyOperateService.queryList(CommonConst.INDEX_TYPE_IS_SHOP,CommonConst.CATCH_PAGE_SIZE);
				if(temporyOperateDtos.size()==0)
				{
					break;
				}
				deleteParams.put("type", CommonConst.INDEX_TYPE_IS_SHOP);
				List<Long>allList=new ArrayList<Long>();
				List<Long>idList=new ArrayList<Long>();
				List<String>deleteIdList=new ArrayList<String>();
				for(TemporyOperateDto temporyOperateDto:temporyOperateDtos)
				{
					allList.add(temporyOperateDto.getId());
					if(!CommonConst.INDEX_DELETE.equals(temporyOperateDto.getOperateType()))
					{	
						idList.add(temporyOperateDto.getId());
					}
					else if(CommonConst.INDEX_DELETE.equals(temporyOperateDto.getOperateType()))
					{
						String type=temporyOperateDto.getOperateType();
						String relateId=temporyOperateDto.getId()+"";
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
				List<ShopDto>shops=shopService.getShopListByIds(idList);
				parseShopData(shops,null);
				if(deleteIdList.size()>0)
				{
					SolrContext.getInstance().deleteByIds(deleteIdList);
				}
				deleteParams.put("list", allList);
				temporyOperateService.deleteByParams(deleteParams);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("爬取临时索引表加载商铺数据时产生了异常",e);
		}
		
	}
	
	/**
	 * 加载商品数据
	 */
	private void loadGoodsData(String ip)
	{
		try{
			ITemporyOperateService temporyOperateService=BeanFactory.getBean(ITemporyOperateService.class);
			IGoodsServcie goodsService=BeanFactory.getBean(IGoodsServcie.class);
			Map<String,Object>deleteParams=new HashMap<String,Object>();
			while(true)
			{	
				List<TemporyOperateDto>temporyOperateDtos=temporyOperateService.queryList(CommonConst.INDEX_TYPE_IS_GOODS,CommonConst.CATCH_PAGE_SIZE);
				if(temporyOperateDtos.size()==0)
				{
					break;
				}
				deleteParams.put("type", CommonConst.INDEX_TYPE_IS_GOODS);
				List<Long>allList=new ArrayList<Long>();
				List<Long>idList=new ArrayList<Long>();
				for(TemporyOperateDto temporyOperateDto:temporyOperateDtos)
				{
					allList.add(temporyOperateDto.getId());
					if(!CommonConst.INDEX_DELETE.equals(temporyOperateDto.getOperateType()))
					{	
						idList.add(temporyOperateDto.getId());
					}
				}
				List<GoodsDto>goods=goodsService.getGoodsListByIds(idList);
				parseGoodsData(goods,ip);
				deleteParams.put("list", allList);
				temporyOperateService.deleteByParams(deleteParams);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("爬取临时索引表加载商铺数据时产生了异常",e);
		}
	}
	
	/**
	 * 解析商铺数据
	 * @param data
	 */
	public void parseShopData(Collection<ShopDto>data,String ip)
	{
		List<SearchContent>searchContents=new ArrayList<SearchContent>();
		List<Long>shopIdList=getShopIdFromShopList(data);//根据商品列表获得商品id列表
		List<String>shopColumnIdList=getShopDistinctColumnId(data);
		Map<String,Float>leastBookPriceInfoMap=SolrBuildIndexForTimeStamp.findShopLeastBookPrice(3, shopIdList);//根绝商铺id列表，查找这些商铺的最低起订价格
		initShopLeastBookInfo(data,leastBookPriceInfoMap);//获得商铺的最低起订价格
		Map<String,List<ColumnDto>>columnInfoMap=new HashMap<String,List<ColumnDto>>();
		initColumnInfoByGoodsIdList(columnInfoMap, shopIdList);
		Map<Long,ColumnDto>columnIdAndPidMap=initColumnPidList(shopColumnIdList);//找到商铺所属行业的父级行业 
		for(ShopDto shopDto:data)
		{
			try{
				SearchContent searchContent=new SearchContent();
				List<String>columnNames=new ArrayList<String>();
				if(shopDto.getShopId()!=null)//获取商品所属商铺的行业列表
				{
					List<String>searchKeys=new ArrayList<String>();
					shopDto.setSearchKeys(searchKeys);//设置商铺的搜索关键字
					List<ColumnDto>columnDtos=columnInfoMap.get(shopDto.getShopId()+"");
					if(columnDtos!=null)
					{
						List<String>shopColumnNames=initColumnInfoToSearchWordList(columnDtos);
						columnNames.addAll(shopColumnNames);
						shopDto.setShopMultiColumns(columnDtos);
						shopDto.getSearchKeys().addAll(initColumnInfoToSearchWordList(columnDtos));//将商铺的行业名称全部转换为关键字
					}
					if(shopDto.getColumnId()!=null)
					{
						ColumnDto shopColumnPInfo=columnIdAndPidMap.get((long)shopDto.getColumnId());
						if(shopColumnPInfo!=null)
						{	
							columnNames.add(shopColumnPInfo.getColumnName());
							shopDto.setShopColumnPid(shopColumnPInfo.getColumnId().intValue());
							shopDto.getSearchKeys().add(shopColumnPInfo.getColumnName());//将商铺所属行业的父行业也加入到搜索关键字
							Long parentColumnId=shopColumnPInfo.getParentColumnId();
							if(parentColumnId!=null&&parentColumnId.intValue()!=0)//将父级行业的行业名称也加入搜索关键字
							{
								ColumnDto parentColumnDto=columnIdAndPidMap.get((long)parentColumnId);
								if(parentColumnDto!=null)
								{
									shopDto.getSearchKeys().add(parentColumnDto.getColumnName());
								}
							}
						}
					}
				}
				//findShopPColumnId(shopDto);
				DataConvertUtil.propertyConvertIncludeDefaultProp(shopDto, searchContent, CommonConst.SHOPS_SEARCH_MAP);//将属性进行转换
				searchContent.setColumnNames(columnNames);
				List<String>shopKeys=convertStrToList(shopDto.getShopKey());
				searchContent.setShopKeys(shopKeys);
				convertGoodsServerMode(searchContent);
				searchContent.setId(CommonConst.SHOP_ID_PREFIX+shopDto.getShopId());
				if(!StringUtils.isEmpty(shopDto.getLatitude())&&!StringUtils.isEmpty(shopDto.getLongitude()))
				{	
					searchContent.setShopLocation(shopDto.getLatitude()+","+shopDto.getLongitude());//设置经纬度
				}
				if(shopDto.getLatitude()!=null&&(shopDto.getLatitude()>90||shopDto.getLatitude()<-90))
				{
					logger.error("商铺编号为:"+shopDto.getShopId()+"纬度超出了范围");
					continue;
				}
				if(shopDto.getLongitude()!=null&&(shopDto.getLongitude()>180||shopDto.getLongitude()<-180))
				{
					logger.error("商铺编号为:"+shopDto.getShopId()+"精度超出了范围");
					continue;
				}
				if(shopDto.getAuditTime()!=null){
					searchContent.setAuditTimeNum(shopDto.getAuditTime().getTime());
				}
				searchContent.setContentType(CommonConst.INDEX_TYPE_IS_SHOP);
				searchContents.add(searchContent);
			}catch(Exception e)
			{
				logger.error("商铺编号:"+shopDto.getShopId()+"更新索引失败了",e);
			}
		}
		if(searchContents.size()>0)
		{	
			if(ip==null)
			{
				SolrContext.getInstance().batchAddDocument(searchContents);
			}
			else
			{
				SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
				if(searchServer!=null)
				{
					searchServer.setIndexdNum(searchServer.getIndexdNum()+searchContents.size());;
				}
				SolrContext.getInstance().batchAddDocument(searchContents,ip);
			}
		}
	}
	
	/**
	 * 根据,号区分返回字符串分割
	 * @Title: convertStrToList 
	 * @param @param source
	 * @param @return
	 * @return List<String>    返回类型 
	 * @throws
	 */
	private List<String>convertStrToList(String source){
		if(!StringUtils.isEmpty(source)){
			String[] sourceArray=source.split(",");
			List<String>result=new ArrayList<String>();
			for(String item:sourceArray){
				result.add(item);
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 商品服务模式变成多种
	 * @Title: convertGoodsServerMode 
	 * @param @param searchContent
	 * @return void    返回类型 
	 * @throws
	 */
	private void convertGoodsServerMode(SearchContent searchContent)
	{
		if(searchContent!=null&&searchContent.getGoodsServerMode()!=null)
		{
			List<Integer>goodsServerModelList=new ArrayList<Integer>();
			if(1==searchContent.getGoodsServerMode().intValue())
			{
				goodsServerModelList.add(1);
				goodsServerModelList.add(3);
			} 
			else if(2==searchContent.getGoodsServerMode().intValue())
			{
				goodsServerModelList.add(2);
				goodsServerModelList.add(3);
			}
			else if(3==searchContent.getGoodsServerMode().intValue())
			{
				goodsServerModelList.add(1);
				goodsServerModelList.add(2);
				goodsServerModelList.add(3);
			}
			else if(0==searchContent.getGoodsServerMode().intValue())
			{
				goodsServerModelList.add(0);
				goodsServerModelList.add(1);
				goodsServerModelList.add(2);
				goodsServerModelList.add(3);
			}
			searchContent.setSearchModes(goodsServerModelList);
		}
	}
	/**
	 * 重建所有索引
	 */
	public void rebuildAll(String ip)
	{
		try {
			long startTime=new Date().getTime();
			if(ip!=null)
			{
				calculateTotalCount(ip);//将需要建索引的个数保存起来，需要在页面显示建索引进度
			}
			rebuildShopData(ip);
			rebuildGoodsData(null,null,ip);
			long endTime=new Date().getTime();
			if(!StringUtils.isEmpty(ip))
			{
				SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
				if(searchServer!=null)
				{
					searchServer.setStatus(CommonConst.SOLR_NODE_INDEX_END);
				}
			}
			logger.error("建索引时间"+(double)((endTime-startTime)/1000));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 重建商铺数据
	 * @throws Exception 
	 */
	public void rebuildShopData(String ip)throws Exception
	{
		IShopServcie shopService=BeanFactory.getBean(IShopServcie.class);
		IShopDao shopDao=BeanFactory.getBean(IShopDao.class);
		Long shopCount=shopDao.getShopTotalCount();
		if(shopCount>10000)
		{
			int totalPageNum=shopCount.intValue()/1000;//总页数
			if(totalPageNum*1000<shopCount)
			{
				totalPageNum++;
			}
			int perThreadNum=totalPageNum/5;//5个线程,每个线程处理多少页
			for(int i=1;i<=5;i++)
			{
						if(i!=5)
						{
							multiThreadLoadShopsData(perThreadNum, i,perThreadNum*i, shopService);
						}
						else
						{
							multiThreadLoadShopsData(perThreadNum, i,totalPageNum, shopService);
						}
			}
		}
		else
		{	
			PageModel pageModel=new PageModel();
			while(true)
			{
				pageModel.setPageSize(80);
				pageModel=shopService.getShopByPage(pageModel);
				if(pageModel.getList()==null||pageModel.getList().size()==0)
				{
					break;
				}
				List<ShopDto>data=(List<ShopDto>)pageModel.getList();
				parseShopData(data,ip);
				pageModel.setToPage(pageModel.getToPage()+1);
			}
		}
	}
	
	/**
	 * 计算总条数
	 * @Title: calculateTotalCount 
	 * @param @param ip
	 * @return void    返回类型 
	 * @throws
	 */
	private void calculateTotalCount(String ip)
	{
		try{
			IShopDao shopDao=BeanFactory.getBean(IShopDao.class);
			Long shopCount=shopDao.getShopTotalCount();
			IGoodsDao goodsDao=BeanFactory.getBean(IGoodsDao.class);
			int goodsCount=goodsDao.getGoodsTotalCount();
			int totalCount=goodsCount+shopCount.intValue();
			SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
			if(searchServer!=null)
			{
				searchServer.setTotalNum(totalCount);
			}
		}catch(Exception e)
		{
			
		}
	}
	
	/**
	 * 多线程中的方法加载商铺数据
	* @Title: multiThreadLoadShopsData 
	* @Description: TODO
	* @param @param perThreadNum
	* @param @param i
	* @param @param shopService
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	private void multiThreadLoadShopsData(final int perThreadNum,final int i,final int totalPage,final IShopServcie shopService)throws Exception
	{
		new Thread(){
			public void run(){
				PageModel pageModel=new PageModel();
				pageModel.setPageSize(1000);
				for(int j=(perThreadNum)*(i-1)+1;j<=totalPage;j++)
				{
					if(shopExistPage.contains(j+"")){
						System.out.println("页数重复了"+j);
					}
					shopExistPage.add(j+"");
					System.out.println("商品当前页"+j);
					pageModel.setToPage(j);
					if(loadShopDataForRebuild(pageModel,shopService)==false)
					{
						break;
					}
				}
			}
			
		}.start();
	}
	
	/**
	 * 多线程中的方法加载商品数据
	* @Title: multiThreadLoadGoodsData 
	* @Description: TODO
	* @param @param perThreadNum
	* @param @param i
	* @param @param goodsService
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	private void multiThreadLoadGoodsData(final int perThreadNum,final int i,final int totalPage,final IGoodsServcie goodsService,final Integer pageSize,final String ip)throws Exception
	{
		new Thread(){
			public void run(){
				PageModel pageModel=new PageModel();
				pageModel.setPageSize(pageSize==null?250:pageSize);
				for(int j=(perThreadNum)*(i-1)+1;j<=totalPage;j++)
				{
					if(goodsExistPage.contains(j+"")){
						System.out.println("页数重复了"+j);
					}
					goodsExistPage.add(j+"");
					pageModel.setToPage(j);
					System.out.println("商铺当前页"+j);
					if(loadGoodsDataForRebuild(pageModel,goodsService,ip)==false)
					{
						break;
					}
				}
			}
		}.start();
	}
	/**
	 * 重建商品数据
	 * @throws Exception
	 */
	public void rebuildGoodsData(Integer threadNum,Integer loadPageSize,String ip)throws Exception
	{
		IGoodsServcie goodsService=BeanFactory.getBean(IGoodsServcie.class);
		IGoodsGroupDao goodsGroupDao=BeanFactory.getBean(IGoodsGroupDao.class);
		IGoodsDao goodsDao=BeanFactory.getBean(IGoodsDao.class);
		int goodsCount=goodsDao.getGoodsTotalCount();
		if(loadPageSize==null||loadPageSize==0)
		{
			loadPageSize=250;
		}
		if(threadNum==null||threadNum==0)
		{
			threadNum=1;
		}
		if(goodsCount>100000)//但商品数据达到多少条的是否，开启多线程处理
		{
			int totalPageNum=goodsCount/loadPageSize;//总页数
			if(totalPageNum*loadPageSize<goodsCount)
			{
				totalPageNum++;
			}
			int perThreadNum=totalPageNum/threadNum;//5个线程,每个线程处理多少页
			for(int i=1;i<=threadNum;i++)
			{
				if(i!=threadNum)
				{
					multiThreadLoadGoodsData(perThreadNum,i,perThreadNum*i,goodsService,loadPageSize,ip);
				}
				else
				{
					multiThreadLoadGoodsData(perThreadNum,i,totalPageNum,goodsService,loadPageSize,ip);
				}
			}
		}
		else
		{
			PageModel pageModel=new PageModel();
			while(true)
			{	
				pageModel.setPageSize(5000);
				pageModel=goodsService.getGoodsListForSearch(new GoodsDto(),pageModel);
				if(pageModel.getList()==null||pageModel.getList().size()==0)
				{
					break;
				}
				List<GoodsDto>data=(List<GoodsDto>)pageModel.getList();
				parseGoodsData(data,ip);
				pageModel.setToPage(pageModel.getToPage()+1);
			}
			
			pageModel=new PageModel();
			while(true)
			{
				pageModel.setPageSize(5000);
				pageModel=goodsGroupDao.getGoodsGroupListForSearch(new GoodsDto(),pageModel);
				if(pageModel.getList()==null||pageModel.getList().size()==0)
				{
					break;
				}
				List<GoodsGroupDto>data=(List<GoodsGroupDto>)pageModel.getList();
				parseGoodsData(new SolrBuildIndexForTimeStamp().convertGoodsGroupToGoodsForProp(data),ip,1);//代表是商品族
				pageModel.setToPage(pageModel.getToPage()+1);
			}
		}
	}
	
	/**
	 * 全量建索引时加载商品数据的方法
	* @Title: loadGoodsDataForRebuild 
	* @Description: TODO
	* @param @param pageModel
	* @param @param goodsService
	* @param @return
	* @param @throws Exception
	* @return boolean    返回类型 
	* @throws
	 */
	private boolean loadGoodsDataForRebuild(PageModel pageModel,IGoodsServcie goodsService,String ip)
	{
		try{
			PageModel dataModel=goodsService.getGoodsListForSearch(new GoodsDto(),pageModel,0);//代表不差总条数
			if(dataModel.getList()==null||dataModel.getList().size()==0)
			{
				return false;
			}
			List<GoodsDto>data=(List<GoodsDto>)dataModel.getList();
			parseGoodsData(data,ip);
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 全量建索引时加载商铺数据
	* @Title: loadShopDataForRebuild 
	* @Description: TODO
	* @param @param pageModel
	* @param @param shopService
	* @param @return
	* @param @throws Exception
	* @return Boolean    返回类型 
	* @throws
	 */
	private Boolean loadShopDataForRebuild(PageModel pageModel,IShopServcie shopService)
	{
		try{
			PageModel dataModel=shopService.getShopByPage(pageModel,0);//代表不查总条数
			if(dataModel.getList()==null||dataModel.getList().size()==0)
			{
				return false;
			}
			List<ShopDto>data=(List<ShopDto>)dataModel.getList();
			parseShopData(data,null);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 第三个参数是用来执行一系列的业务链
	 * 解析商品数据
	 * @param data
	 */
	public void parseGoodsData(final List<GoodsDto>data,String ip,Integer...goodsGroupFlag)
	{
		List<SearchContent>searchContents=new ArrayList<SearchContent>();
		List<Long>shopIdList=getShopIdFromGoodsList(data);//根据商品列表获得商品id列表
		List<String>shopColumnIdList=getGoodsDistinctColumnId(data);
		Map<String,List<ColumnDto>>columnInfoMap=new HashMap<String,List<ColumnDto>>();
		initColumnInfoByGoodsIdList(columnInfoMap, shopIdList);
		Map<String,List<String>>goodsCategoryMap=new HashMap<String,List<String>>();
		List<String>goodsCategoryIdList=getGoodsDistinctCategoryId(data,goodsCategoryMap);//初始化商品 类别
		initGoodsCategoryPid(goodsCategoryIdList,goodsCategoryMap);
		Map<Long,ColumnDto>columnIdAndPidMap=null;
		if(shopColumnIdList.size()>0){
			columnIdAndPidMap=initColumnPidList(shopColumnIdList);
		}
		else{
			columnIdAndPidMap=new HashMap<Long,ColumnDto>();
		}
		for(GoodsDto goodsDto:data)
		{
			try{
				SearchContent searchContent=new SearchContent();
				if(goodsDto.getLatitude()!=null&&(goodsDto.getLatitude()>90||goodsDto.getLatitude()<-90))//判断纬度是否超出了范围
				{
					logger.error("商铺编号为:"+goodsDto.getShopId()+"纬度超出了范围");
					continue;
				}
				if(goodsDto.getLongitude()!=null&&(goodsDto.getLongitude()>180||goodsDto.getLongitude()<-180))//判断经度是否超出了范围
				{
					logger.error("商铺编号为:"+goodsDto.getShopId()+"精度超出了范围");
					continue;
				}
				
				if(goodsDto.getShopId()!=null)//获取商品所属商铺的行业列表
				{
					List<String>searchKeys=new ArrayList<String>();
					List<ColumnDto>columnDtos=columnInfoMap.get(goodsDto.getShopId()+"");
					goodsDto.setSearchKeys(searchKeys);
					if(columnDtos!=null)
					{
						goodsDto.setShopMultiColumns(columnDtos);
						goodsDto.getSearchKeys().addAll(initColumnInfoToSearchWordList(columnDtos));
					}
					if(goodsDto.getShopColumnId()!=null)
					{
						ColumnDto shopColumnPInfo=columnIdAndPidMap.get((long)goodsDto.getShopColumnId());
						if(shopColumnPInfo!=null)
						{	
							goodsDto.setShopColumnPid(shopColumnPInfo.getColumnId().intValue());
							goodsDto.getSearchKeys().add(shopColumnPInfo.getColumnName());//将所属行业的关键字加入搜索关键字
							Long parentColumnId=shopColumnPInfo.getParentColumnId();
							if(parentColumnId!=null&&parentColumnId.intValue()!=0)//将父级行业的行业名称也加入搜索关键字
							{
								ColumnDto parentColumnDto=columnIdAndPidMap.get(parentColumnId+"");
								if(parentColumnDto!=null)
								{
									goodsDto.getSearchKeys().add(parentColumnDto.getColumnName());
								}
							}
						}
					}
					if(goodsDto.getGoodsNameList()!=null)
					{
						goodsDto.getSearchKeys().addAll(goodsDto.getGoodsNameList());
					}
				}
				if(goodsDto.getGoodsCategoryId()!=null){
					List<String>categoryIdList=goodsCategoryMap.get(goodsDto.getGoodsCategoryId()+"");
					if(categoryIdList!=null){
						searchContent.setGoodsCategoryIdList(categoryIdList);
					}
				}
				//findGoodsPColumnId(goodsDto);
				DataConvertUtil.propertyConvertIncludeDefaultProp(goodsDto, searchContent, CommonConst.GOODS_SEARCH_MAP);//将属性进行转换
				convertGoodsServerMode(searchContent);
				if(goodsGroupFlag!=null&&goodsGroupFlag.length>0)
				{
					searchContent.setId(CommonConst.GOODS_GROUP_ID_PREFIX+goodsDto.getGoodsId());
				}
				else
				{
					searchContent.setId(CommonConst.GOODS_ID_PREFIX+goodsDto.getGoodsId());
				}
				if(!StringUtils.isEmpty(goodsDto.getLatitude())&&!StringUtils.isEmpty(goodsDto.getLongitude()))
				{	
					searchContent.setShopLocation(goodsDto.getLatitude()+","+goodsDto.getLongitude());//设置经纬度
				}
				searchContent.setContentType(CommonConst.INDEX_TYPE_IS_GOODS);
				searchContents.add(searchContent);
			}catch(Exception e)
			{
				logger.error("商品编号:"+goodsDto.getGoodsId()+"更新索引失败了",e);
			}
		}
		if(searchContents.size()>0)
		{	
			if(ip==null)
			{	
				SolrContext.getInstance().batchAddDocument(searchContents);
			}
			else
			{
				SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
				if(searchServer!=null)
				{
						searchServer.setIndexdNum(searchServer.getIndexdNum()+searchContents.size());//实时的存储已经建索引条数
				}
				SolrContext.getInstance().batchAddDocument(searchContents, ip);
			}
		}
	}
	
	/**
	 * 根据 商品类别id列表初始化商品父id
	 * @Title: initGoodsCategoryPid 
	 * @param @param goodsCategoryIdList
	 * @return void    返回类型 
	 * @throws
	 */
	private void initGoodsCategoryPid(List<String> goodsCategoryIdList,Map<String,List<String>>goodsCategoryMap) {
		try{
			if(goodsCategoryIdList.size()>0){
				IGoodsCategoryDao goodsCategoryDao=BeanFactory.getBean(IGoodsCategoryDao.class);
				List<GoodsCategoryDto>goodsCategoryList=goodsCategoryDao.getCategoryListByIdList(goodsCategoryIdList);//根据ID查询所有的类别记录
				for(GoodsCategoryDto goodsCategoryDto:goodsCategoryList){//将商品类别编号和父编号用一个List存起来
					Long goodsCategoryId=goodsCategoryDto.getGoodsCategoryId();
					if(goodsCategoryId!=null){
						String categoryKey=goodsCategoryDto.getGoodsCategoryId()+"";
						List<String>categoryIdList=goodsCategoryMap.get(categoryKey);
						String categoryPid=goodsCategoryDto.getParentCategoryId()+"";
						if(categoryIdList!=null){
							categoryIdList.add(categoryPid+"");
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 获取所有商品共同的商品类别id
	 * @Title: getGoodsDistinctCategoryId 
	 * @param @param data
	 * @param @return
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	private List<String> getGoodsDistinctCategoryId(List<GoodsDto> data,Map<String,List<String>>goodsCategoryMap) {
		List<String>categoryIdList=new ArrayList<String>();
		for(GoodsDto goodsDto:data){
			List<String>goodsCategoryIdList=new ArrayList<String>();
			Long goodsCategoryId=goodsDto.getGoodsCategoryId();
			if(!StringUtils.isEmpty(goodsCategoryId)){
				String goodsCategoryIdKey=goodsCategoryId+"";
				if(!categoryIdList.contains(goodsCategoryIdKey)){
					categoryIdList.add(goodsCategoryIdKey);
				}
				goodsCategoryIdList.add(goodsCategoryIdKey);
				goodsCategoryMap.put(goodsCategoryIdKey, goodsCategoryIdList);
			}
		}
		return categoryIdList;
	}


	/**
	 * 根据columnId获取父级columnId列表
	 * @Title: initColumnPidList 
	 * @param @param shopColumnIdList
	 * @param @return
	 * @return Map<Long,Long>    返回类型 
	 * @throws
	 */
	public static Map<Long,ColumnDto> initColumnPidList(
			List<String> shopColumnIdList) {
		IColumnDao columnDao=BeanFactory.getBean(IColumnDao.class);
		List<ColumnDto>columnDtos=columnDao.getColumnInfoByColumnId(shopColumnIdList);
		Map<Long,ColumnDto>columnMap=new HashMap<Long,ColumnDto>();
		List<String>columnPidList=new ArrayList<String>();
		for(ColumnDto columnDto:columnDtos)
		{
			if(columnMap.get(columnDto.getColumnId())==null)
			{
				columnMap.put(columnDto.getColumnId(),columnDto);
			}
			if(columnDto.getParentColumnId()!=null&&columnDto.getParentColumnId().intValue()!=0)
			{
				columnPidList.add(columnDto.getParentColumnId()+"");
			}
		}
		if(columnPidList.size()>0)
		{
			List<ColumnDto>parentColumnDtos=columnDao.getColumnInfoByColumnId(columnPidList);
			for(ColumnDto columnDto:parentColumnDtos)
			{
				if(columnMap.get(columnDto.getColumnId())==null)
				{
					columnMap.put(columnDto.getColumnId(),columnDto);
				}
			}
		}
		return columnMap;
	}

	/**
	 * 针对重建索引
	* @Title: parseGoodsDataForRebuild 
	* @Description: TODO
	* @param @param data
	* @return void    返回类型 
	* @throws
	 */
	public void parseGoodsDataForRebuild(final List<GoodsDto>data,Integer threadNumParam) throws Exception
	{
		int num=8;
		if(threadNumParam!=null)//将数据划分给多个线程进行处理
		{
			num=threadNumParam;
		}
			
		final int pageNum=data.size()/num;//每个线程需要处理多少页数据
		final int threadNum=num;
		final CountDownLatch downLatch=new CountDownLatch(data.size());  
		for(int i=0;i<=threadNum-1;i++)
		{
			final int k=i;
			new Thread(){
				public void run(){
						
						List<SearchContent>searchContents=new ArrayList<SearchContent>();
						for(int j=k*pageNum;j<=(k+1)*pageNum-1;j++)
						{
							GoodsDto goodsDto=data.get(j);
							try{
								SearchContent searchContent=new SearchContent();
								DataConvertUtil.propertyConvertIncludeDefaultProp(goodsDto, searchContent, CommonConst.GOODS_SEARCH_MAP);//将属性进行转换
								searchContent.setId(CommonConst.GOODS_ID_PREFIX+goodsDto.getGoodsId());
								if(!StringUtils.isEmpty(goodsDto.getLatitude())&&!StringUtils.isEmpty(goodsDto.getLongitude()))
								{	
									searchContent.setShopLocation(goodsDto.getLatitude()+","+goodsDto.getLongitude());//设置经纬度
								}
								searchContent.setContentType(CommonConst.INDEX_TYPE_IS_GOODS);
								searchContents.add(searchContent);
								if(searchContents.size()%1000==0)//5000进行一次软提交
								{
									SolrContext.getInstance().batchAddDocument(searchContents,0);
									searchContents.clear();
								}
							}catch(Exception e)
							{
								logger.error("商品编号:"+goodsDto.getGoodsId()+"更新索引失败了",e);
							}
							finally{
								downLatch.countDown();
							}
						}
						if(searchContents.size()>0)
						{
							SolrContext.getInstance().batchAddDocument(searchContents);
						}
						SolrContext.getInstance().commitServer();
					}
					
				
				
			}.start();
		}

		downLatch.await();
	/*	List<SearchContent>searchContents=new ArrayList<SearchContent>();
		for(GoodsDto goodsDto:data)
		{
			try{
				SearchContent searchContent=new SearchContent();
				DataConvertUtil.propertyConvertIncludeDefaultProp(goodsDto, searchContent, CommonConst.GOODS_SEARCH_MAP);//将属性进行转换
				searchContent.setId(CommonConst.GOODS_ID_PREFIX+goodsDto.getGoodsId());
				if(!StringUtils.isEmpty(goodsDto.getLatitude())&&!StringUtils.isEmpty(goodsDto.getLongitude()))
				{	
					searchContent.setShopLocation(goodsDto.getLatitude()+","+goodsDto.getLongitude());//设置经纬度
				}
				searchContent.setContentType(CommonConst.INDEX_TYPE_IS_GOODS);
				searchContents.add(searchContent);
				if(searchContents.size()>=1000)//5000进行一次软提交
				{
					SolrContext.getInstance().batchAddDocument(searchContents,0);
					searchContents.clear();
				}
			}catch(Exception e)
			{
				logger.error("商品编号:"+goodsDto.getGoodsId()+"更新索引失败了",e);
			}
		}
			
			SolrContext.getInstance().commitServer();*/
	}
	
	/**
	 * 根据商品id列表查找初始化商品所属商铺的行业列表
	* @Title: initColumnInfoByGoodsIdList 
	* @param @param columnInfoMap
	* @param @param goodsIdList
	* @return void    返回类型 
	* @throws
	 */
	public static void initColumnInfoByGoodsIdList(Map<String,List<ColumnDto>>columnInfoMap,List<Long>goodsIdList)
	{
		IColumnDao columnDao=BeanFactory.getBean(IColumnDao.class);
		int size=10000;
		int num=goodsIdList.size()/10000;//此处要分为10000主要是担心拼接sql语句太大导致报错
		if(num*size<goodsIdList.size())
		{
			num++;
		}
		int index=0;
		while(index<num)
		{
			if((index+1)*10000<goodsIdList.size())
			{
				List<Long>temporyIdList=goodsIdList.subList(index*10000, (index+1)*10000);
				if(temporyIdList.size()>0)
				{
					List<ColumnDto>columnDtos=columnDao.getMultiColumnByShopId(temporyIdList);
					parseGoodsColumnInfo(columnInfoMap, columnDtos);
				}
			}
			else
			{
				List<Long>temporyIdList=goodsIdList.subList(index*10000, goodsIdList.size());
				if(temporyIdList.size()>0)
				{
					List<ColumnDto>columnDtos=columnDao.getMultiColumnByShopId(temporyIdList);
					parseGoodsColumnInfo(columnInfoMap, columnDtos);
				}
			}
			index++;
		}
	}
	
	/**
	 * 
	 * @Title: initShopLeastBookInfo 
	 * @param @param shopList
	 * @param @param leastBookInfos
	 * @return void    返回类型 
	 * @throws
	 */
	private static void initShopLeastBookInfo(Collection<ShopDto>shopList,Map<String,Float>leastBookInfos)
	{
		for(ShopDto shopDto:shopList)
		{
			Float price=leastBookInfos.get(shopDto.getShopId()+"");
			shopDto.setLeastBookPrice(price);
		}
	}
	/**
	 * 解析商品关联商铺所属的行业分类
	* @Title: parseGoodsColumnInfo 
	* @param @param goodsDtoMap
	* @param @param columnDtos
	* @return void    返回类型 
	* @throws
	 */
	private static void parseGoodsColumnInfo(Map<String,List<ColumnDto>>goodsDtoMap,List<ColumnDto>columnDtos)
	{
		for(ColumnDto columnDto:columnDtos)
		{
			Long shopId=columnDto.getShopId();
			String shopKey=shopId+"";
			if(goodsDtoMap.containsKey(shopId+""))
			{
				List<ColumnDto> columnDtoList=goodsDtoMap.get(shopKey);
			    columnDtoList.add(columnDto);
				goodsDtoMap.put(shopKey, columnDtoList);
				
			}
			else
			{
				List<ColumnDto>columnDtoList=new ArrayList<ColumnDto>();
				columnDtoList.add(columnDto);
				goodsDtoMap.put(shopKey, columnDtoList);
			}
		}
	}
	
	/**
	 * 根据商品获取商品所述的商铺的所有idlist
	 * @Title: getGoodsIdFromGoodsList 
	 * @param @param goodsDtos
	 * @param @return
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	public static List<Long>getShopIdFromGoodsList(List<GoodsDto>goodsDtos)
	{
		List<Long>shopIdList=new ArrayList<Long>();
		for(GoodsDto goodsDto:goodsDtos)
		{
			shopIdList.add(goodsDto.getShopId());
		}
		return shopIdList;
	}
	
	/**
	 * 根据商铺获取所有的商铺idList
	* @Title: getShopIdFromShopList 
	* @param @param shopDtos
	* @param @return
	* @return List<Long>    返回类型 
	* @throws
	 */
	public static List<Long>getShopIdFromShopList(Collection<ShopDto>shopDtos)
	{
		List<Long>shopIdList=new ArrayList<Long>();
		for(ShopDto shopDto:shopDtos)
		{
			shopIdList.add(shopDto.getShopId());
		}
		return shopIdList;
	}
	
	
	/**
	 * 获取商品所属商铺的行业id列表
	 * 已去除重复的
	* @Title: getGoodsDistinctColumnId 
	* @param @return
	* @return List<Long>    返回类型 
	* @throws
	 */
	public static List<String>getGoodsDistinctColumnId(List<GoodsDto>goodsDtos)
	{
		List<String>columnIdList=new ArrayList<String>();
		for(GoodsDto goodsDto:goodsDtos)
		{
			if(!StringUtils.isEmpty(goodsDto.getShopColumnId()))
			{
				if(!columnIdList.contains(goodsDto.getShopColumnId()))
				{
					columnIdList.add(goodsDto.getShopColumnId()+"");
				}
			}
		}
		return columnIdList;
	}
	
	/**
	 * 获取商铺所属的行业id列表
	 * 已去除重复的
	* @Title: getShopDistinctColumnId 
	* @param @param shopDtos
	* @param @return
	* @return List<String>    返回类型 
	* @throws
	 */
	public static List<String>getShopDistinctColumnId(Collection<ShopDto>shopDtos)
	{
		List<String>columnIdList=new ArrayList<String>();
		for(ShopDto shopDto:shopDtos)
		{
			if(!StringUtils.isEmpty(shopDto.getColumnId()))
			{
				if(!columnIdList.contains(shopDto.getColumnId()))
				{
					columnIdList.add(shopDto.getColumnId()+"");
				}
			}
		}
		return columnIdList;
	}
	/**
	 * 根据商品查找商品所属的所有行业
	* @Title: findGoodsPColumnId 
	* @param @param dto
	* @return void    返回类型 
	* @throws
	 */
	private void findGoodsPColumnId(GoodsDto  dto)
	{
		IColumnDao columnDao=BeanFactory.getBean(IColumnDao.class);
		if(dto.getShopId()!=null)
		{
			List<ColumnDto>columnDtos=columnDao.getMultiColumnByShopId(dto.getShopId());
			dto.setShopMultiColumns(columnDtos);
		}
		if(dto.getShopColumnId()!=null)
		{
			dto.setShopColumnPid(columnDao.getColumnIdByChildId(dto.getShopColumnId()));
		}
	}
	
	/**
	 * 根据商铺查找商铺所属的多个行业
	* @Title: findShopPColumnId 
	* @param @param dto
	* @return void    返回类型 
	* @throws
	 */
	private void findShopPColumnId(ShopDto dto)
	{
		IColumnDao columnDao=BeanFactory.getBean(IColumnDao.class);
		if(dto.getShopId()!=null)
		{
			List<ColumnDto>columnDtos=columnDao.getMultiColumnByShopId(dto.getShopId());
			dto.setShopMultiColumns(columnDtos);
		}
		if(dto.getColumnId()!=null)
		{ 
			dto.setShopColumnPid(columnDao.getColumnIdByChildId(dto.getColumnId()));
		}
	}
	
	
	/**
	 * 将solr节点状态写进redis
	 * @Title: writeStatusToRedis 
	 * @param 
	 * @return void    返回类型 
	 */
	public static void writeStatusToRedis()
	{
		List<SearchServer>searchServers=new ArrayList<SearchServer>();
		for(Entry<String, SearchServer>entry:SolrContext.getInstance().getNodes().entrySet())
		{
			SearchServer node=entry.getValue();
			SearchServer temporyVar=new SearchServer(node.getSolrUrl());
			temporyVar.setStatus(node.getStatus());
			searchServers.add(temporyVar);
		}
		DataCacheApi.setObject("solrNodes",searchServers);
	}
	
	/**
	 * 将行业名称转换为关键字搜索
	 * @Title: initColumnInfoToSearchWordList 
	 * @param @param columnList
	 * @param @return
	 * @return List<String>    返回类型 
	 * @throws
	 */
	private static List<String> initColumnInfoToSearchWordList(List<ColumnDto>columnList)
	{
		List<String>searchWordList=new ArrayList<String>();
		for(ColumnDto columnInfo:columnList)
		{
			searchWordList.add(columnInfo.getColumnName());
		}
		return searchWordList;
	}
}
