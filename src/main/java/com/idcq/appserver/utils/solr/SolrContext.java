package com.idcq.appserver.utils.solr;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.GroupParams;
import org.springframework.util.StringUtils;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.index.quartz.job.SolrCatchDataJob;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.ReflectionUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.solr.analyzer.IkAnalyzerContext;
/**
 * solr 工具
 * @author pchzhang
 *
 */
public class SolrContext {
	
	
	private final Log logger = LogFactory.getLog(getClass());
	private HttpSolrServer httpSolrServer;
	private CloudSolrServer solrServer;
	private static SolrContext solrContext=new SolrContext();
	private ConcurrentHashMap<String,SearchServer>solrServerMap=new ConcurrentHashMap<String,SearchServer>();
	List<String>solrHostList=new ArrayList<String>();
	String nginxHost=null;
	
	
	private HttpSolrServer nginxSolr=null;
	/**
	 * 默认为单节点模式
	 */
	private String solrMode=CommonConst.SOLR_SINGLE_NODE_MODE;
	private SolrContext(){
		
		init();
	}
	
	/**
	 * 初始化solr上下文
	 */
	private void init()
	{
		try {
			Properties props=ContextInitListener.SOLR_PROPS;
			solrMode=props.getProperty(CommonConst.SOLR_SERVER_MODE);
			String host=props.getProperty(CommonConst.SOLR_SERVER_HOST);
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))
			{
				solrServer=new CloudSolrServer(props.getProperty(CommonConst.SOLR_SERVER_HOST));
				solrServer.setDefaultCollection(props.getProperty(CommonConst.SOLR_DEFAULT_COLLECTION));
			}
			else 
			{
				String[] hostArray=host.split(",");//可能存在多台solr主机
				for(String solrHostItem:hostArray)
				{
					try{
						SearchServer solrServerItem=new SearchServer(solrHostItem);
						solrServerItem.setSoTimeout(10000);
						solrServerMap.put(solrHostItem,solrServerItem);
						solrHostList.add(solrHostItem);
					}catch(Exception e)
					{
						logger.error("solr主机:"+solrHostItem+"的连接出现了异常");
					}
				}
				//httpSolrServer=new HttpSolrServer(props.getProperty(CommonConst.SOLR_SERVER_HOST));
			}
			nginxHost=props.getProperty(CommonConst.SOLR_NGINX_HOST);
			if(!StringUtils.isEmpty(nginxHost))//搜索读操作，经过nginx进行分发
			{
				nginxSolr=new HttpSolrServer(nginxHost);
			}
		} catch (MalformedURLException e) {
			logger.error("solr服务器启动过程中产生了异常");
			e.printStackTrace();
		}
	}
	
	public static SolrContext getInstance()
	{
		return solrContext;
	}
	
	
	/**
	 * 获得solrserver
	 * @return
	 */
	private SolrServer getSolrServerInstance()
	{
		if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))
		{
			return solrServer;
		}
		else 
		{
			return httpSolrServer;
		}
	}
	
	
	/**
	 * 构建文档
	 * @param solrEntity
	 */
	public SolrInputDocument buildDocument(SearchContent searchCotent)
	{
		SolrInputDocument doc=new SolrInputDocument();
		Field[]fields=searchCotent.getClass().getDeclaredFields();
		for(Field field:fields)
		{	
			SearchCondition searchCondition=field.getAnnotation(SearchCondition.class);
			boolean store=true;
			if(searchCondition!=null&&searchCondition.store()==false)
			{
				store=false;
			}
			String fieldName=field.getName();
			if("doToken".equals(fieldName)){
			    continue;
			}
			Object value=ReflectionUtils.getFieldValue(searchCotent, fieldName);//通过反射取值
			if(!StringUtils.isEmpty(value)&&store==true)
			{
				if("shopColumnId".equals(fieldName))
				{	
					List<ColumnDto>columnDtos=searchCotent.getShopMultiColumns();
					List<Long>columnIds=new ArrayList<Long>();
					if(columnDtos!=null)
					{
						for(ColumnDto columnDto:columnDtos)
						{
							columnIds.add(columnDto.getColumnId());
						}
					}
					columnIds.add((long)searchCotent.getShopColumnId());
					doc.addField(fieldName,columnIds);
				}
				else if("goodsCategoryId".equals(fieldName)){
					List<String>goodsCategoryIdList=searchCotent.getGoodsCategoryIdList();
					doc.addField(fieldName,goodsCategoryIdList);
				}
				else if("searchKeys".equals(fieldName))
				{
					List<String>searchKeys=searchCotent.getSearchKeys();
					searchKeys.add(searchCotent.getGoodsName());
					doc.addField(fieldName, searchKeys);
				}else if("isRecommend".equals(fieldName)){
				    doc.addField("judgeFlag", value);
				}
				else
				{	
					doc.addField(fieldName, value);
				}
			}
		}
		return doc;
	}
	
	/**
	 * 批量添加文档
	 * @param solrEntitys
	 */
	public void batchAddDocument(List<SearchContent>searchContents,int ...notFlag)
	{
		Collection<SolrInputDocument>docs=new ArrayList<SolrInputDocument>();
		for(SearchContent solrEntity:searchContents)
		{
			docs.add(buildDocument(solrEntity));
		}
		try {
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.add(docs);
				if(notFlag==null||notFlag.length==0)
				{	
					if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
					{
						solrServer.commit();
					}
				}
			}
			else//非集群模式的时候，需要开启多写模式，即向多台solr写索引，以确保读的时候每台返回的数据是一致的
			{
				asyncAddDocs(docs,notFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("添加记录失败了");
			throw new RuntimeException("添加索引记录失败了");
		}
	}
	
	/**
	 * 只对指定的ip solr进行添加索引
	 * @Title: batchAddDocument 
	 * @param @param searchContents
	 * @param @param ip
	 * @param @param notFlag
	 * @return void    返回类型 
	 * @throws
	 */
	public void batchAddDocument(List<SearchContent>searchContents,String ip,int ...notFlag)
	{
		Collection<SolrInputDocument>docs=new ArrayList<SolrInputDocument>();
		for(SearchContent solrEntity:searchContents)
		{
			docs.add(buildDocument(solrEntity));
		}
		try {
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.add(docs);
				if(notFlag==null||notFlag.length==0)
				{	
					if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
					{
						solrServer.commit();
					}
				}
			}
			else//非集群模式的时候，需要开启多写模式，即向多台solr写索引，以确保读的时候每台返回的数据是一致的
			{
				for(final Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
				{
						try {
							if(ip.equals(solrItem.getKey()))//如果节点ip与制定ip相同，则添加索引
							{
								solrItem.getValue().add(docs);
								if(notFlag==null||notFlag.length==0)
								{	
									solrItem.getValue().commit();
								}
							}
						} catch (Exception e) {
							boolean isLastOne=false;
							SearchServer searchServer=solrItem.getValue();
							String content="solr主机ip为"+solrItem.getKey()+"在添加索引过程中产生了异常";
							if(solrServerMap.size()>1)
							{	
								searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);//改变其状态
								searchServer.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
								//solrServerMap.remove(solrItem.getKey());//移除当前这台solr服务器
								refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
							}
							else
							{
								isLastOne=true;
								content+=",由于是最后一台Solr节点不能将其移除";
							}
							ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
							String mobile="13902479582";//此处需要换成可配置的
							if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
							{	
								String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
								if(StringUtils.isEmpty(isSendMsg))
								{
									DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
									smsService.sendSms(mobile, content);
								}
							}
							else
							{
								smsService.sendSms(mobile, content);
							}
							solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
							logger.error("ip为"+solrItem.getKey()+"的solr服务器添加索引产生了异常,此时索引修改时间为"+SolrCatchDataJob.lastUpdateTime,e);
							e.printStackTrace();
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("添加记录失败了");
			throw new RuntimeException("添加索引记录失败了");
		}
	}   
	
	
	/**
	 * 向每台solr异步添加文档
	* @Title: asyncAddDocs 
	* @param @param docs
	* @param @param notFlag
	* @return void    返回类型 
	* @throws
	 */
	public void asyncAddDocs(final Collection<SolrInputDocument>docs,final int ...notFlag) throws Exception
	{
		final CountDownLatch downLatch=new CountDownLatch(solrServerMap.size()); //加上这个计数器主要是避免异步提交过于频繁，导致solr重新打开searcher报错，所以才去等所有的solr添加完文档，再结束当前这个方法
		Date startDate=new Date();
		for(final Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
		{
			new Thread(){
				public void run(){
					try {
						if(CommonConst.SOLR_NODE_REMOVE!=solrItem.getValue().getStatus())
						{
							solrItem.getValue().add(docs);
							if(notFlag==null||notFlag.length==0)
							{	
								solrItem.getValue().commit();
							}
						}
					} catch (Exception e) {
						SearchServer searchServer=solrItem.getValue();
						String content="solr主机ip为"+solrItem.getKey()+"在添加索引过程中产生了异常";
						boolean isLastOne=false;
						boolean removeFlag=!(e instanceof SolrServerException&&e.getMessage().contains("IOException occured"));//如果不是这种异常则移除掉，势就做其他的处理
						if(solrServerMap.size()>1)
						{	
							if(removeFlag)//如果不是这种异常则不移除节点，把丢失的文档重新建一次
							{
								searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);//改变其状态
								searchServer.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
								refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
								solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
							}
						}
						else
						{
							isLastOne=true;
							content+=",由于是最后一台Solr节点不能将其移除";
						}
						ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
						String mobile="13902479582";//此处需要换成可配置的
						if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
						{	
							String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
							if(StringUtils.isEmpty(isSendMsg))
							{
								DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
								smsService.sendSms(mobile, content);
							}
						}
						else
						{
							smsService.sendSms(mobile, content);
						}
						if(!removeFlag)//IOException时再次尝试添加文档
						{
							new RepeatIndexThread(docs, solrItem.getKey()).start();
						}
						logger.error("ip为"+solrItem.getKey()+"的solr服务器添加索引产生了异常,此时索引修改时间为"+SolrCatchDataJob.lastUpdateTime,e);
						e.printStackTrace();
					}
					finally{
						downLatch.countDown();
					}
				}
				
			}.start();
		}
		
		downLatch.await(3*60*1000, TimeUnit.MILLISECONDS);//计数器为0代表每个线程都已经提交完，或者三分钟过后会，程序自动结束
	}
	
   /**
	* 这对某台节点丢失的部分文档进行重复建立
	* @Title: repeatAddDocs 
	* @param @param docs
	* @param @param ip
	* @return void    返回类型 
	* @throws
	 */
	public void repeatAddDocs(final Collection<SolrInputDocument>docs,String ip)
	{
		for(final Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
		{
					try {
						if(CommonConst.SOLR_NODE_REMOVE!=solrItem.getValue().getStatus()&&solrItem.getKey().equals(ip))
						{
								solrItem.getValue().add(docs);
								solrItem.getValue().commit();
						}
					} catch (Exception e) {
						SearchServer searchServer=solrItem.getValue();
						String content="solr主机ip为"+solrItem.getKey()+"在第二次添加索引过程中产生了异常";
						boolean isLastOne=false;
						if(solrServerMap.size()>1)
						{	
								searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);//改变其状态
								searchServer.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
								//solrServerMap.remove(solrItem.getKey());//移除当前这台solr服务器
								refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
								solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
						}
						else
						{
							isLastOne=true;
							content+=",由于是最后一台Solr节点不能将其移除";
						}
						ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
						String mobile="13902479582";//此处需要换成可配置的
						if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
						{	
							String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
							if(StringUtils.isEmpty(isSendMsg))
							{
								DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
								smsService.sendSms(mobile, content);
							}
						}
						else
						{
							smsService.sendSms(mobile, content);
						}
						logger.error("ip为"+solrItem.getKey()+"的solr服务器第二次添加索引产生了异常,此时索引修改时间为"+SolrCatchDataJob.lastUpdateTime,e);
						e.printStackTrace();
					}
				}
				
	}
	
	
	/**
	 * 提交内存中的索引
	* @Title: commitServer 
	* @param 
	* @return void    返回类型 
	* @throws
	 */
	public void commitServer()
	{
		try {
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.commit();
			}
			else//单节点模式,存在多台机器
			{
				multiServerCommit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("添加记录失败了");
			throw new RuntimeException("添加索引记录失败了");
		}
	}
	
	
	/**
	 * 多台机器提交
	* @Title: multiServerCommit 
	* @param 
	* @return void    返回类型 
	* @throws
	 */
	private void multiServerCommit()
	{
		for(Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
		{
			try {
				solrItem.getValue().commit();
			} catch (Exception e) {
				String content="solr主机ip为"+solrItem.getKey()+"在提交索引过程中产生了异常";
				SearchServer searchServer=solrItem.getValue();
				if(solrServerMap.size()>1)
				{	
					searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);//改变其状态
					searchServer.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
					refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
					solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
				}
				else
				{
					content+=",由于是最后一台Solr节点不能将其移除";
				}
				ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
				String mobile="13902479582";//此处需要换成可配置的
				smsService.sendSms(mobile, content);
				logger.error("solr主机"+solrItem.getKey()+"提交索引产生了异常",e);
			} 
		}
	}
	/**
	 * 修改文档
	 * @param solrEntity
	 */
	public void updateDocument(List<SearchContent> searchContents)
	{
		Collection<SolrInputDocument>docs=new ArrayList<SolrInputDocument>();
		List<String>ids=new ArrayList<String>();
		for(SearchContent searchContent:searchContents)
		{
			docs.add(buildDocument(searchContent));
			ids.add(searchContent.getId());
		}
		if(deleteByIds(ids)==false)
		{
			return;
		}
		else
		{
			try {
				getSolrServerInstance().add(docs);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("添加文档过程中产生了异常");
			}
		}
	}
	
	
	/**
	 * 根据id列表进行删除
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(List<String>ids)
	{
		try {
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.deleteById(ids);
				solrServer.commit();
			}
			else
			{
				for(Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
				{
					SearchServer solrServerItem=solrItem.getValue();
					try{
						if(CommonConst.SOLR_NODE_REMOVE!=solrServerItem.getStatus())
						{
							solrServerItem.deleteById(ids);
							solrServerItem.commit();
						}
					}catch(Exception e)
					{
						boolean isLastOne=false;
						String content="solr主机ip为"+solrItem.getKey()+"在删除索引过程中产生了异常";
						if(solrServerMap.size()>1)
						{
							solrServerItem.setStatus(CommonConst.SOLR_NODE_REMOVE);
							solrServerItem.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
							refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
							solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
						}
						else
						{
							isLastOne=true;
							content+=",由于是最后一台Solr节点不能将其移除";
						}
						ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
						String mobile="13902479582";//此处需要换成可配置的
						if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
						{	
							String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
							if(StringUtils.isEmpty(isSendMsg))
							{
								DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
								smsService.sendSms(mobile, content);
							}
						}
						else
						{
							smsService.sendSms(mobile, content);
						}
						logger.error("solr主机:"+solrItem.getKey()+"删除索引过程中产生了异常",e);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除文档过程中产生了异常",e);
			return false;
		}
	}
	
	
	
	/**
	 * 通过查询表达式进行删除
	* @Title: deleteByQuery 
	* @Description: TODO
	* @param @param query
	* @param @return
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean deleteByQuery(String ip)
	{
		try{
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.deleteByQuery("*:*");
				solrServer.commit();
			}
			else
			{
				for(Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
				{
					SearchServer solrServerItem=solrItem.getValue();
					try{
						if(solrItem.getKey().equals(ip))
						{
							solrServerItem.deleteByQuery("*:*");
							solrServerItem.commit();
						}
					}catch(Exception e)
					{
						boolean isLastOne=false;
						String content="solr主机ip为"+solrItem.getKey()+"在删除索引过程中产生了异常";
						if(solrServerMap.size()>1)//必须保证至少有一台存活的solr机器
						{	
							solrServerItem.setStatus(CommonConst.SOLR_NODE_REMOVE);
							solrServerItem.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
							refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
							solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
						}
						else
						{
							content+=",由于是最后一台Solr节点不能将其移除";
							isLastOne=true;
						}
						ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
						String mobile="13902479582";//此处需要换成可配置的
						if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
						{	
							String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
							if(StringUtils.isEmpty(isSendMsg))
							{
								DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
								smsService.sendSms(mobile, content);
							}
						}
						else
						{
							smsService.sendSms(mobile, content);
						}
						logger.error("solr主机:"+solrItem.getKey()+"删除索引过程中产生了异常,此时索引修改时间为"+SolrCatchDataJob.lastUpdateTime,e);
						e.printStackTrace();
					}
				}
			}
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("删除文档过程中产生了异常",e);
			return false;
		}
	}

	
	
	/**
	 * 删除所有的索引
	 * @return
	 */
	public boolean deleteAll()
	{
		try {
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				solrServer.deleteByQuery("*:*");
				solrServer.commit();
			}
			else
			{
				for(Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
				{
					SearchServer solrServerItem=solrItem.getValue();
					try{
						if(CommonConst.SOLR_NODE_REMOVE!=solrServerItem.getStatus())
						{
							solrServerItem.deleteByQuery("*:*");
							solrServerItem.commit();
						}
					}catch(Exception e)
					{
						boolean isLastOne=false;
						String content="solr主机ip为"+solrItem.getKey()+"在删除索引过程中产生了异常";
						if(solrServerMap.size()>1)//必须保证至少有一台存活的solr机器
						{	
							solrServerItem.setStatus(CommonConst.SOLR_NODE_REMOVE);
							solrServerItem.setHappendTime(SolrCatchDataJob.lastUpdateTime);//设置故障发生时间
							refreshSolrStatus(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);
							solrExceptionInform(solrItem.getKey(), CommonConst.SOLR_NODE_REMOVE);//通知solr节点
						}
						else
						{
							isLastOne=true;
							content+=",由于是最后一台Solr节点不能将其移除";
						}
						ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
						String mobile="13902479582";//此处需要换成可配置的
						if(isLastOne)//如果是最后一台机器发送短信不要太过于频繁
						{	
							String isSendMsg=DataCacheApi.get(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
							if(StringUtils.isEmpty(isSendMsg))
							{
								DataCacheApi.setex(solrItem.getKey()+DateUtils.format(new Date(),DateUtils.DATE_FORMAT),"true", 21600);
								smsService.sendSms(mobile, content);
							}
						}
						else
						{
							smsService.sendSms(mobile, content);
						}
						logger.error("solr主机:"+solrItem.getKey()+"删除索引过程中产生了异常,此时索引修改时间为"+SolrCatchDataJob.lastUpdateTime,e);
						e.printStackTrace();
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 删除某个节点的所有数据
	* @Title: deleteAll 
	* @param @param ip
	* @param @return
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean deleteAll(String ip)
	{
		try{
			for(Entry<String,SearchServer> solrItem:solrServerMap.entrySet())
			{
				if(ip.equals(solrItem.getKey()))
				{
					solrItem.getValue().deleteByQuery("*:*");
				}
			}
			return true;
		}catch(Exception e)
		{
			logger.error("删除节点"+ip+"的索引数据过程中产生了异常");
			return false;
		}
	}
	
	/**
	 * 查询根据
	 * @Title: queryGroupSingleResult 
	 * @param @param searchContent
	 * @param @param page
	 * @param @return
	 * @return PageModel    返回类型 
	 * @throws
	 */
	public PageModel queryGroupSingleResult(SearchContent searchContent,PageModel page){
		try
		{
			SolrQuery query=buildQuery(searchContent);
			query.setStart(page.getPageSize()*(page.getToPage()-1));
			query.setRows(page.getPageSize());
			SolrServer currentSolrServer=null;
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				currentSolrServer=solrServer;
			}
			else
			{
				if(!StringUtils.isEmpty(nginxHost))
				{
					currentSolrServer=nginxSolr;
				}
				else
				{	
					if(solrServerMap.size()==0)
					{
						throw new Exception("solr节点存在问题");
					}
					else
					{
						String currentSolrHost=solrHostList.get(0);
						currentSolrServer=solrServerMap.get(currentSolrHost);
					}
				}
			}
			QueryResponse queryResponse=currentSolrServer.query(query);
			GroupResponse groupResponse=queryResponse.getGroupResponse();
			List<SearchContent>groupResults=new ArrayList<SearchContent>();
			Set<String>groupValue=new HashSet<String>();
			if(groupResponse!=null)
			{
				List<GroupCommand>groupList=groupResponse.getValues();
				for(GroupCommand groupCommand:groupList)
				{
					List<Group> groups = groupCommand.getValues();  
					page.setTotalItem(groupCommand.getNGroups());
			        for(Group group : groups) {  
			        	groupValue.add(group.getGroupValue());
			        	groupResults.addAll(buildResultEntity(group.getResult()));
			        }  
				}
				
			}
			page.setList(groupResults);
			return page;
		}catch(Exception e)
		{
			logger.error("查询过程中产生了异常", e);
			e.printStackTrace();
			return new 	PageModel();
		}
	}
	
	/**
	 * 构造分组查询结果
	 * @Title: queryGroupResult 
	 * @param @param searchContent
	 * @param @param page
	 * @param @return
	 * @return PageModel    返回类型 
	 * @throws
	 */
	public PageModel queryGroupResult(SearchContent searchContent,PageModel page)
	{
		try
		{
			SolrQuery query=buildQuery(searchContent);
			query.setStart(page.getPageSize()*(page.getToPage()-1));
			query.setRows(page.getPageSize());
			SolrServer currentSolrServer=null;
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				currentSolrServer=solrServer;
			}
			else
			{
				if(!StringUtils.isEmpty(nginxHost))
				{
					currentSolrServer=nginxSolr;
				}
				else
				{	
					if(solrServerMap.size()==0)
					{
						throw new Exception("solr节点存在问题");
					}
					else
					{
						String currentSolrHost=solrHostList.get(0);
						currentSolrServer=solrServerMap.get(currentSolrHost);
					}
				}
			}
			QueryResponse queryResponse=currentSolrServer.query(query);
			GroupResponse groupResponse=queryResponse.getGroupResponse();
			List<GroupResult>groupResults=new ArrayList<GroupResult>();
			if(groupResponse!=null)
			{
				List<GroupCommand>groupList=groupResponse.getValues();
				for(GroupCommand groupCommand:groupList)
				{
					List<Group> groups = groupCommand.getValues();  
					page.setTotalItem(groupCommand.getNGroups());
			        for(Group group : groups) {  
			        	GroupResult groupResult=new GroupResult();
			        	groupResult.setShopId(group.getGroupValue());
			        	groupResult.setData(buildResultEntity(group.getResult()));
			        	groupResults.add(groupResult);
			        }  
				}
				
			}
			page.setList(groupResults);
			return page;
	}catch(Exception e)
		{
			logger.error("查询过程中产生了异常", e);
			e.printStackTrace();
			return new 	PageModel();
		}
	}
	
	/**
	 * 搜索结果查询
	 * @param solrEntity
	 * @param page
	 * @return
	 */
	public PageModel queryResult(SearchContent searchContent,PageModel page)
	{
		SolrQuery query=buildQuery(searchContent);
		query.setStart(page.getPageSize()*(page.getToPage()-1));
		query.setRows(page.getPageSize());
		try
		{
			SolrServer currentSolrServer=null;
			if(CommonConst.SOLR_CLOUD_NODE_MODE.equals(solrMode))//集群模式
			{
				currentSolrServer=solrServer;
			}
			else
			{
				if(!StringUtils.isEmpty(nginxHost))
				{
					currentSolrServer=nginxSolr;
				}
				else
				{	
					if(solrServerMap.size()==0)
					{
						throw new Exception("solr节点存在问题");
					}
					else
					{
						String currentSolrHost=solrHostList.get(0);
						currentSolrServer=solrServerMap.get(currentSolrHost);
					}
				}
			}
			QueryResponse queryResponse=currentSolrServer.query(query);
			return buildQueryResult(queryResponse,page);
		}catch(Exception e)
		{
			logger.error("查询过程中产生了异常", e);
			e.printStackTrace();
			return new 	PageModel();
		}
	}
	
	
	/**
	 * 构造查询条件
	 * @param searchContent
	 * @return
	 */
	public SolrQuery buildQuery(SearchContent searchContent)
	{
		SolrQuery solrQuery=new SolrQuery();
		StringBuffer queryBuffer=new StringBuffer();
		solrQuery.addFilterQuery("NOT isShow:0");
		for(Field field:searchContent.getClass().getDeclaredFields())
		{
			Object value=ReflectionUtils.getFieldValue(searchContent, field.getName());
			if(StringUtils.isEmpty(value))//检测属性时候为空
			{
				continue;
			}
			else
			{
				SearchCondition searchCondition=field.getAnnotation(SearchCondition.class);//获取查询条件
				if(searchCondition!=null)
				{
					SearchType searchType=searchCondition.seachType();
					String fieldName=field.getName();
					if(searchType==SearchType.EQUALS)//相等方式
					{
						if("shopColumnId".equals(fieldName))
						{
							solrQuery.addFilterQuery("(shopColumnId:"+value+" OR shopColumnPid:"+value+")");
						}
						if("goodsServerMode".equals(fieldName))
						{
							solrQuery.addFilterQuery("searchModes:"+value);
						}else if("isRecommend".equals(fieldName)){
						    solrQuery.addFilterQuery("judgeFlag:"+value);
						}
						else
						{	
							solrQuery.addFilterQuery(field.getName()+":"+value);
						}
					}
					else if(searchType==SearchType.KEYWORD)//关键字方式搜索
					{
						value=ClientUtils.escapeQueryChars(value+"");
						int maxCnWordLength=IkAnalyzerContext.getMaxCnLengthForAnalyzer(value+"");
						if("goodsName".equals(field.getName())&&maxCnWordLength>=2)
						{
							if(searchContent.getQueryOnlyColumn()!=null)
							{   if(!searchContent.getDoToken())
							    {
								    queryBuffer.append("columnNames:"+value+" ");
							    }else
							    {
							        String[] keys = ((String)value).split(",");
							        for(String str : keys){
							            queryBuffer.append("columnNames:\""+str+"\" ");
							        }
							    }
							}
							else
							{	
								queryBuffer.append("searchKeys:\""+value+"\" or searchKeys:\""+value+"\"^4"+" or shopKeys:\""+value+"\"^8");
							}
						}
						else
						{
							queryBuffer.append(" content:\""+value+"\" or content:\""+value+"\"^4"+" or shopKeys:\""+value+"\"^8");
						}
						/*solrQuery.set("defType","dismax");
						solrQuery.set("qf", "goodsName^10 content^1");//名称权重为Content权重的10倍
*/						if(!CommonConst.INDEX_TYPE_IS_GOODS.equals(searchContent.getContentType()))//商铺搜索时，商品的状态不能为0
						{
							solrQuery.addFilterQuery("NOT goodsStatus:0");
							solrQuery.addFilterQuery("NOT goodsStatus:2");
							solrQuery.addFilterQuery("NOT goodsStatus:3");
						}
					}
					else if(searchType==SearchType.LOCATIONSEARCH)//地理位置搜索
					{
						solrQuery.set("d",value+""); 
						solrQuery.set("sfield",CommonConst.LOCATION_FIELD); 
						solrQuery.set("pt",searchContent.getShopLocation()); 
						solrQuery.set("fl", "*,distance:geodist()");//solr返回的字段
						solrQuery.addFilterQuery("{!geofilt}");
					}
					else if(searchType==SearchType.IN)//如果是In的方式搜索,则采用or方式
					{
						String[] array=(value+"").split(",");
						String inFieldName=searchCondition.searchInRelateField();
						boolean first=true;
						for(String item:array)
						{
							if(first)
							{	
								queryBuffer.append(inFieldName+":"+item);
								first=false;
							}
							else
							{
								queryBuffer.append(" or "+inFieldName+":"+item);
							}
						}
					}
					else if(searchType==SearchType.ORDERBY&&!"0".equals(value+"")){
						String orderByField=searchCondition.orderByField();
						
						ORDER order = (int)value == CommonConst.SEARCH_ORDER_ASC ? ORDER.desc : ORDER.asc;
//						solrQuery.addSort(orderByField, ORDER.desc);
						solrQuery.addSort(orderByField, order);
					}
					else if(searchType==SearchType.LOCATIONORDERBY&&!"0".equals(value+"")){//按照地理位置排序
						solrQuery.addSort("geodist()",ORDER.asc);
					}
					else if(searchType==SearchType.GROUPSEARCH&&!StringUtils.isEmpty(value)){
						solrQuery.setParam(GroupParams.GROUP, true);
						solrQuery.setParam(GroupParams.GROUP_FIELD, "shopId");
						solrQuery.setParam(GroupParams.GROUP_LIMIT,searchContent.getGroupLimit()!=null?searchContent.getGroupLimit()+"":"3");
						solrQuery.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
					}else if(searchType==SearchType.GT){
						solrQuery.addFilterQuery(fieldName+":["+value+" TO *]");
					}
				}
			}
		}
		if(StringUtils.isEmpty(queryBuffer.toString()))
		{
			solrQuery.setQuery(CommonConst.SOLR_QUERY_ALL);
		}
		else
		{	
			solrQuery.setQuery(queryBuffer.toString());
		}	
		logger.error("查询条件为:"+solrQuery.toString());
		return solrQuery;
	}
	
	/**
	 * 构造查询结果
	 * @param queryResponse
	 * @return
	 */
	public PageModel buildQueryResult(QueryResponse queryResponse,PageModel page)
	{
		try {
			//Map<String, Map<String, List<String>>>highlightMap=queryResponse.getHighlighting();
			SolrDocumentList docList=queryResponse.getResults();
			List<SearchContent>queryData=buildResultEntity(docList);
			logger.info("查询时间:"+queryResponse.getQTime());
			page.setList(queryData);
			page.setTotalItem((int)docList.getNumFound());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询过程中产生了异常",e);
		}
		return page;
	}
	
	/**
	 * 构造实体结果
	 * @Title: buildResultEntity 
	 * @param @param docList
	 * @param @return
	 * @return List<SearchContent>    返回类型 
	 * @throws
	 */
	private List<SearchContent> buildResultEntity(SolrDocumentList docList)
	{
		List<SearchContent>queryData=new ArrayList<SearchContent>();
		for(SolrDocument doc:docList)
		{
			SearchContent resultEntity=new SearchContent();
			for(String fieldName:doc.getFieldNames())//通过反射的方式将值设置进去
			{
				Object value=doc.get(fieldName);
				if("judgeFlag".equalsIgnoreCase(fieldName)){
				    fieldName = "isRecommend";
				}
				if(!StringUtils.isEmpty(value))
				{
					Field field=ReflectionUtils.getDeclaredField(resultEntity, fieldName);
					if(field!=null)
					{	
						Class fieldClassType=field.getType();//字段类型
						value=ReflectionUtils.convertValue(value, fieldClassType);//转换类型
						ReflectionUtils.setFieldValue(resultEntity, fieldName, value);//设值
					}
				}
			}
			//高亮的代码暂时干掉
			/*if(!StringUtils.isEmpty(searchContent.getGoodsName()))
			{		
				try{
					String	highLightGoodsName=highlightMap.get(id).get("goodsName").get(0);
					if(!StringUtils.isEmpty(highLightGoodsName))
					{
						resultEntity.setGoodsName(highLightGoodsName);
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("高亮产生了异常");
				}
			}*/
			queryData.add(resultEntity);
		}
		return queryData;
	}
	
	
	/**
	 * 告知其它的appServer,某个solr节点有状态变更了
	* @Title: refreshSolrStatus 
	* @param @param solrHost 
	* @param @param action
	* @return void    返回类型 
	* @throws
	 */
	private void refreshSolrStatus(final String solrHost,final Integer action)
	{
		String refreshSolrStatusUrl=ContextInitListener.SOLR_PROPS.getProperty("solr.refreshStatusUrl");
		if(!StringUtils.isEmpty(refreshSolrStatusUrl))
		{
			new Thread()
			{
				public void run(){
					 URL localURL;
					try {
						ContextInitListener.SOLR_PROPS.getProperty("");
						localURL = new URL("");
						URLConnection connection = localURL.openConnection();
						HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
						InputStream inputStream=httpURLConnection.getInputStream();
						IOUtils.toString(inputStream);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	
	/**
	 * 某台solr节点产生异常时，做出的解决方案
	 * @Title: solrExceptionInform 
	 * @param @param solrHost
	 * @param @param action
	 * @return void    返回类型 
	 * @throws
	 */
	public void solrExceptionInform(final String solrHost,final Integer action)
	{
		new Thread(){
			public void run(){
				URL localURL;
				try {
					String exceptionInformUrl=ContextInitListener.SOLR_PROPS.getProperty(CommonConst.SOLR_EXCEPTION_INFORM_KEY);
					exceptionInformUrl+="?ip="+solrHost+"&action="+action;
					localURL = new URL(exceptionInformUrl);
					URLConnection connection = localURL.openConnection();
					HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
					InputStream inputStream=httpURLConnection.getInputStream();
					String result=IOUtils.toString(inputStream);
					System.out.println(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public ConcurrentHashMap<String,SearchServer>getNodes()
	{
		return this.solrServerMap;
	}
	
}
