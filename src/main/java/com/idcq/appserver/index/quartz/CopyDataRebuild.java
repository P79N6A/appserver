/**
 * 
 */
package com.idcq.appserver.index.quartz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.temporyOperate.ITemporyOperateDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.index.quartz.job.SolrCatchDataJob;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/** 
 * @ClassName: CopyDataRebuild 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月29日 下午3:00:17 
 *  
 */
@Service
public class CopyDataRebuild {
	
	@Autowired
	private static ITemporyOperateDao temporyOperateDao;
	
	
	@Autowired
	private IGoodsDao goodsDao;
	/**
	 * 复制表的数据
	*  @Title: copyData 
	*  @Description: TODO
	*  @param 
	*  @return void    返回类型 
	*  @throws
	 */
	public void copyData(Long copySize,Long loadSize,Integer threadNum,int... refreshToRedis)
	{
		if(temporyOperateDao==null)
		{
			temporyOperateDao=BeanFactory.getBean(ITemporyOperateDao.class);
		}
		if(goodsDao==null)
		{
			goodsDao=BeanFactory.getBean(IGoodsDao.class);
		}
		if(copySize==null||copySize.longValue()==0)
		{	
			copySize=100000l;
		}
		if(loadSize==null||loadSize==0)
		{	
			loadSize=5000l;
		}
		rebuildGoodsData(copySize,loadSize,threadNum,refreshToRedis);
	}
	
	/**
	 * 复制商品的数据
	* @Title: copyGoodsData 
	* @param @param copySize
	* @param @param loadSize
	* @return void    返回类型 
	* @throws
	 */
	private void rebuildGoodsData(Long copySize,Long loadSize,Integer threadNum,int... refreshToRedis)
	{
		SolrCatchDataJob solrCatchDataJob=new SolrCatchDataJob();
		
			int goodsCount;
			while((goodsCount=goodsDao.getGoodsReplicationCount())!=0)
			{	
				try {
					if(goodsCount<copySize)
					{
						copySize=(long)goodsCount;
					}
					long initTime=new Date().getTime();
					long startTime=new Date().getTime();
					temporyOperateDao.copyGoodsDataIntoTable(copySize);//件商品数据复制到另外一张表
					long endTime=new Date().getTime();
					System.out.println("拷贝"+copySize+",耗时:"+((double)(endTime-startTime)/(double)1000));
					long maxGoodsId=temporyOperateDao.queryMaxGoodsId(copySize-1);//找出N条记录中最大的goodsId
					startTime=endTime;
					endTime=new Date().getTime();
					System.out.println("查找最大"+maxGoodsId+",耗时:"+((double)(endTime-startTime)/(double)1000));
					temporyOperateDao.deleteByMaxGoodsId(maxGoodsId,copySize);
					startTime=endTime;
					endTime=new Date().getTime();
					System.out.println("根据最大值删除"+maxGoodsId+",耗时:"+((double)(endTime-startTime)/(double)1000));
					parseGoodsData(copySize,loadSize,solrCatchDataJob,threadNum,refreshToRedis);
					startTime=endTime;
					endTime=new Date().getTime();
					System.out.println("解析创建索引耗时"+maxGoodsId+",耗时:"+((double)(endTime-startTime)/(double)1000));
					startTime=endTime;
					endTime=new Date().getTime();
					System.out.println("构建索引数"+copySize+"此次构建商品索引耗时:"+((double)(endTime-initTime)/(double)1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}
	/**
	 * 解析数据库中的商品数据
	* @Title: parseGoodsData 
	* @Description: TODO
	* @param @param copySize
	* @param @param loadSize
	* @param @param solrCatchDataJob
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	private void parseGoodsData(Long copySize,Long loadSize,SolrCatchDataJob solrCatchDataJob,Integer threadNum,int... refreshToRedis)throws Exception
	{
		int loadedSize=0;
		PageModel pageModel=new PageModel();
		pageModel.setPageSize(loadSize.intValue());
		while(true&&loadedSize!=copySize.intValue())
		{
			long startTime=new Date().getTime();
			try {
				List<GoodsDto>goodsData=goodsDao.getGoodsDataFromTemporyTable(pageModel);
				if(goodsData.size()==0)
				{
					break;
				}
				if(refreshToRedis!=null&&refreshToRedis.length>0)
				{
					refreshToRedis(goodsData);
				}
				loadedSize+=loadSize;
				solrCatchDataJob.parseGoodsDataForRebuild(goodsData,threadNum);
				long endTime=new Date().getTime();
				System.out.println("档次构建"+loadSize+"此次构建商品索引耗时:"+((double)(endTime-startTime)/(double)1000));
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				pageModel.setToPage(pageModel.getToPage()+1);
			}
		}
		goodsDao.deleteTemporyGoodsByMaxGoodsId();//清空临时表的数据
	}
	
	/**
	 * 将数据刷新到redis
	 * @Title: refreshToRedis 
	 * @param @param goodsDto
	 * @return void    返回类型 
	 * @throws
	 */
	private void refreshToRedis(final List<GoodsDto>goodsDtos)
	{
		new Thread(){
			public void run()
			{
				System.out.println("刷新redis--start");
				for(GoodsDto goodsDto:goodsDtos)
				{
					DataCacheApi.setObject(CommonConst.KEY_GOODS+goodsDto.getGoodsId(), goodsDto);
				}
			}
		}.start();
	}
}
