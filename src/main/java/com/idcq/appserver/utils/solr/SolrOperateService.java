package com.idcq.appserver.utils.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.index.quartz.SolrBuildIndexForTimeStamp;
import com.idcq.appserver.index.quartz.job.SolrCatchDataJob;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 操作solr的服务类
* @ClassName: SolrOperateService 
* @Description: TODO
* @author 张鹏程 
* @date 2015年5月7日 下午4:46:18 
*
 */
public class SolrOperateService {
	
	private static IShopDao shopDao;
	public static  Log  logger=LogFactory.getLog(SolrOperateService.class);
	private static IGoodsDao goodsDao;
	private static SolrCatchDataJob catchDataJob=new SolrCatchDataJob();
	private static SolrBuildIndexForTimeStamp solrBuildIndex=new SolrBuildIndexForTimeStamp();
	
	static{
		shopDao=BeanFactory.getBean(IShopDao.class);
		goodsDao=BeanFactory.getBean(IGoodsDao.class);
	}
	/**
	 * 刷新solr中的商铺
	* @Title: refreshShop 
	* @param @param shopIds
	* @param @return
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean refreshShop(String shopIds)
	{
		try{
			List<Long>idList=new ArrayList<Long>();
			String idArray[]=shopIds.split(",");
			if(idArray==null||idArray.length==0)
			{
				return false;
			}
			for(String id:idArray)
			{
				idList.add(Long.parseLong(id));
			}
			List<ShopDto>shopDtos=shopDao.getListByShopIds(idList);
			catchDataJob.parseShopData(shopDtos,null);
			solrBuildIndex.batchSynchronizeGoods(shopDtos, null);
			return true;
		}catch(Exception e )
		{
			logger.error("刷新索引过程中产生了异常", e);
			return false;
		}
	}
	
	
	public static boolean refreshGoods(String goodsIds)
	{
		List<Long>idList=new ArrayList<Long>();
		String idArray[]=goodsIds.split(",");
		if(idArray==null||idArray.length==0)
		{
			return false;
		}
		for(String id:idArray)
		{
			idList.add(Long.parseLong(id));
		}
		List<GoodsDto>goodsDtos=goodsDao.getGoodsListByIds(idList);
		catchDataJob.parseGoodsData(goodsDtos,null);
		return true;
	}
}	
