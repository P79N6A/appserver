/**
 * 
 */
package com.idcq.appserver.controller.goods;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.index.quartz.CopyDataRebuild;
import com.idcq.appserver.index.quartz.SolrBuildIndexForTimeStamp;
import com.idcq.appserver.index.quartz.job.SolrCatchDataJob;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JedisPoolUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.httpClient.HttpClientUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.solr.SearchServer;
import com.idcq.appserver.utils.solr.SolrContext;

/**
 * SolrSolrSolrSolrSolrSolrSolrSolr
 * 测试期间用来操作redis及solr的接口请求
 * @ClassName: InterfaceController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 下午1:55:45 
 *  
 */
@Controller
public class InterfaceController {

	
	private static final Logger logger =Logger.getLogger(InterfaceController.class);
	/**
	 * 删除所有redis数据
	* @Title: deleteAllRedisData 
	* @Description: TODO
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="deleteAllRedisData")
	public @ResponseBody  String deleteAllRedisData()
	{
		JSONObject obj=new JSONObject();
		Jedis jedis=null;
		try{
			jedis=JedisPoolUtils.getJedis();
			jedis.flushAll();
			obj.put("success", true);
			return obj.toString();
		}catch(Exception e)
		{
			obj.put("success", false);
			e.printStackTrace();
			return obj.toString();
		}
		finally{
			if(jedis!=null)
			{
				JedisPoolUtils.returnRes(jedis);
			}
			
		}
	}
	
	
	
	/**
	 * 全量重建索引
	 * @return
	 */
	@RequestMapping(value="shop/rebuildAllIndex")
	public @ResponseBody String rebuildIndex( @RequestParam(required=false) Integer threadNum, @RequestParam(required=false) Long pageSize,@RequestParam(required=false)  Integer way,@RequestParam(required=false)  Integer copySize,@RequestParam(required=false)String userName,@RequestParam(required=false)String password)
	{
		JSONObject jsonObj=new JSONObject();
		if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password))
		{
			jsonObj.put("success", false);
			jsonObj.put("msg","UserName or Password is not allow empty");
			return jsonObj.toString();
		}
		if(userName.equals(ContextInitListener.SOLR_PROPS.get("solr.userName"))&&password.equals(ContextInitListener.SOLR_PROPS.get("solr.password")))
		{
			jsonObj.put("success", true);
			new Thread(){
				
				public void run(){
					SolrContext.getInstance().deleteAll();
					SolrCatchDataJob job=new SolrCatchDataJob();
					job.rebuildAll(null);
				}
			}.start();
			
			return jsonObj.toString();
		}
		else
		{
			jsonObj.put("success",false);
			jsonObj.put("msg", "userName or password is input error");
			return jsonObj.toString();
		}
	}
	
	
	/**
	 * 重建商铺索引
	* @Title: rebuildShopIndex 
	* @param @param threadNum
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="shop/rebuildShopIndex")
	public @ResponseBody String rebuildShopIndex(@RequestParam(required=false)Integer threadNum)
	{
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("success", true);
		new Thread(){
			
			public void run(){
				try{
				SolrCatchDataJob job=new SolrCatchDataJob();
				job.rebuildShopData(null);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
		
		return jsonObj.toString();
	}
	
	/**
	 * 重建商品数据
	* @Title: rebuildGoodsIndex 
	* @Description: TODO
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="shop/rebuildGoodsIndex")
	public String rebuildGoodsIndex( @RequestParam(required=false) final int threadNum, @RequestParam(required=false) final Integer pageSize,@RequestParam(required=false)  int way,@RequestParam(required=false)  long copySize)
	{
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("success", true);
		new Thread(){
			
			public void run(){
				
				SolrCatchDataJob job=new SolrCatchDataJob();
				try {
					job.rebuildGoodsData(threadNum,pageSize,null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		return jsonObj.toString();
	}
	
	
	
	
	/**
	 * 删除所有索引
	 * @return
	 */
	@RequestMapping(value="shop/deleteAllIndex")
	public @ResponseBody String deleteAllIndex(@RequestParam(value="queryStr",required=false) String queryStr,@RequestParam(required=false)String userName,@RequestParam(required=false)String password)
	{
		JSONObject jsonObj=new JSONObject();
		if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password))
		{
			jsonObj.put("success", false);
			jsonObj.put("msg","UserName or Password is not allow empty");
			return jsonObj.toString();
		}
		if(userName.equals(ContextInitListener.SOLR_PROPS.get("solr.userName"))&&password.equals(ContextInitListener.SOLR_PROPS.get("solr.password")))
		{
			if(StringUtils.isEmpty(queryStr))
			{	
				SolrContext.getInstance().deleteAll();
			}
			else
			{
				SolrContext.getInstance().deleteByQuery(queryStr);
			}
		}
		else
		{
			jsonObj.put("success",false);
			jsonObj.put("msg", "userName or password is input error");
			return jsonObj.toString();
		}
		return jsonObj.toString();
	}
	
	/**
	 * 通过复制数据的方式重建
	* @Title: rebuildByCopyData 
	* @Description: TODO
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="shop/rebuildByCopyData")
	public @ResponseBody  String rebuildByCopyData( @RequestParam(required=false)final Long pageSize,@RequestParam(required=false)final  Long copySize,@RequestParam(required=false)final Integer threadNum)
	{
		new Thread(){
			public void run(){
				CopyDataRebuild copyRebuild=BeanFactory.getBean(CopyDataRebuild.class);
				copyRebuild.copyData(copySize, pageSize,threadNum);
			}
			
		}.start();
		JSONObject obj=new JSONObject();
		obj.put("success", true);
		return obj.toString();
	}
	
	/**
	 * 修改最后修改时间
	* @Title: updateLastUpdateTime 
	* @param @param lastUpdateTime
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="updateLastUpdateTime")
	public @ResponseBody  String updateLastUpdateTime(@RequestParam(required=true)String lastUpdateTime) 
	{
		SolrCatchDataJob.lastUpdateTime=lastUpdateTime;
		JSONObject obj=new JSONObject();
		obj.put("success", true);
		return obj.toString();
	}
	
	
	
	
	/**
	 * 登录方法
	* @Title: loginIn 
	* @param @param userName
	* @param @param password
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="solr/loginIn",method=RequestMethod.POST)
	public String loginIn(@RequestParam(value="userName")String userName,@RequestParam(value="password")String password)
	{
		Properties props=ContextInitListener.SOLR_PROPS;
		if(props.getProperty("solr.userName")==null||(!props.getProperty("solr.userName").equals(userName)))
		{
			return "login";
		}
		if(props.getProperty("solr.password")==null||(!props.getProperty("solr.password").equals(password)))
		{
			return "login";
		}
		DataCacheApi.setex("searchLogin", "true", 900);
		return "redirect:/interface/showSolrStatus";
		
	}
	
	/**
	 * 显示solr节点状态
	* @Title: showSolrNodeStatus 
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="showSolrStatus")
	public String showSolrNodeStatus(Model model)
	{
		if(StringUtils.isEmpty(DataCacheApi.get("searchLogin")))
		{
			return "login";
		}
		model.addAttribute("solrNodes",SolrContext.getInstance().getNodes());
		return "WEB-INF/solrStatus";
	}
	
	/**
	 * 重建某个单节点的索引
	* @Title: rebuildSingleNodeIndex 
	* @param @param model
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value="rebuildSingleNodeIndex")
	public @ResponseBody String rebuildSingleNodeIndex(Model model,HttpServletRequest request)
	{
		JSONObject obj=new JSONObject();
		try{
			final String nodeKey=RequestUtils.getQueryParam(request, "nodeKey");
			final String lastUpdateTime=RequestUtils.getQueryParam(request, "lastUpdateTime");
			if(StringUtils.isEmpty(nodeKey))
			{
				obj.put("success",false);
				return obj.toString();
			}
			else 
			{
				if(judgeNormalNodeNum(nodeKey))
				{
					SearchServer searchServer=SolrContext.getInstance().getNodes().get(nodeKey);
					searchServer.setIndexdNum(0);
					searchServer.setTotalNum(0);
					searchServer.setStatus(CommonConst.SOLR_NODE_INDEXING);
					new Thread(){
						public void run(){
							if(StringUtils.isEmpty(lastUpdateTime))
							{
								SolrContext.getInstance().deleteByQuery(nodeKey);
								new SolrCatchDataJob().rebuildAll(nodeKey);
							}
							else
							{
								new SolrBuildIndexForTimeStamp().loadNewDataToTemporyIndex(lastUpdateTime,nodeKey);;
							}
						}
						
					}.start();
					obj.put("success",true);
				}
				else
				{
					obj.put("success", false);
					obj.put("msg", "已经是最后一台正常的Solr节点，不能再移除了");
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg","重建指定solr节点索引过程中产生了异常");
		}
		finally{
			return obj.toString();
		}
	}
	
	/**
	 * 将此节点从Lvs中移除掉
	* @Title: removeFromLvs 
	* @param @param model
	* @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="removeFromLvs",produces = "application/json;charset=UTF-8")
	public @ResponseBody String removeFromLvs(Model model,HttpServletRequest request)
	{
		JSONObject obj=new JSONObject();
		try{
			String ip=RequestUtils.getQueryParam(request, "ip");
			if(!StringUtils.isEmpty(ip))
			{
				SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
				if(searchServer!=null)
				{
					searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);
					searchServer.setHappendTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
					SolrContext.getInstance().solrExceptionInform(ip,CommonConst.SOLR_NODE_REMOVE);
					obj.put("success",true);
					obj.put("msg","已通知lvs移除节点"+ip);
				}
			}
		}catch(Exception e)
		{
			obj.put("success", false);
			obj.put("msg","从Lvs中移除成功");
		}
		return obj.toString();
	}
	
	/**
	 * 判断某个节点除外是否还有其它节点是正常的
	 * 如果没有则不允许移除这个节点
	 * @Title: judgeNormalNodeNum 
	 * @param @param ip
	 * @param @return
	 * @return boolean    返回类型 
	 * @throws
	 */
	private boolean judgeNormalNodeNum(String ip)
	{
		for(Entry<String,SearchServer>entry:SolrContext.getInstance().getNodes().entrySet())
		{
			if(entry.getKey().equals(ip))
			{
				
				continue;
			}
			SearchServer searchServer=entry.getValue();
			if(CommonConst.SOLR_NODE_NORMAL.equals(searchServer.getStatus()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将solr 节点从lvs移除后，lvs所在服务器，告知solr node已经被移除
	* @Title: doRemoveFromLvsConfirm 
	* @param @param ip
	* @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="removeFromLvsConfirm",produces = "application/json;charset=UTF-8")
	public @ResponseBody String doRemoveFromLvsConfirm(String ip,HttpServletRequest request)
	{
		logger.error("lvs确定删除了节点:"+ip);
		JSONObject obj=new JSONObject();
		if(!StringUtils.isEmpty(ip))
		{
			SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
			if(searchServer!=null)
			{
				searchServer.setStatus(CommonConst.SOLR_NODE_CONFIRM_REMOVE);
			}
		}
		obj.put("success", true);
		return obj.toString();
	}
	
	/**
	 * 将节点从lvs中恢复
	* @Title: doRecoveryFromLvs 
	* @param @param ip
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="recoveryFromLvs",produces = "application/json;charset=UTF-8")
	public @ResponseBody String doRecoveryFromLvs(String ip)
	{
		JSONObject obj=new JSONObject();
		try{
			if(!StringUtils.isEmpty(ip))
			{
				SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
				searchServer.setStatus(CommonConst.SOLR_NODE_NORMAL);
				if(searchServer!=null)
				{	
					obj.put("success", true);
					obj.put("msg","已通知LVS移除当前节点");
					SolrContext.getInstance().solrExceptionInform(ip,CommonConst.SOLR_NODE_RECOVERY);
				}
			}
			else
			{
				obj.put("success", false);
				obj.put("msg","通知LVS移除节点失败");
			}
		}catch(Exception e)
		{ 
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg", "从lvs中恢复节点"+ip+"失败，并产生了异常"+e.getMessage());
		}
		finally{
			return obj.toString();
		}
	}
	
	/**
	 * lvs告知appServer确实恢复了节点
	* @Title: recoveryFromLvsConfirm 
	* @param @param ip
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="recoveryFromLvsConfirm")
	public @ResponseBody String recoveryFromLvsConfirm(String ip)
	{
		logger.error("lvs确定恢复了节点:"+ip);
		JSONObject obj=new JSONObject();
		if(!StringUtils.isEmpty(ip))
		{
			SearchServer searchServer=SolrContext.getInstance().getNodes().get(ip);
			searchServer.setStatus(CommonConst.SOLR_NODE_REMOVE);
			if(searchServer!=null)
			{
				searchServer.setStatus(CommonConst.SOLR_NODE_NORMAL);
			}
		}
		obj.put("success", true);
		return obj.toString();
	}
	
	/**
	 * 加载索引 进度信息
	* @Title: loadIndexInfo 
	* @param @param nodeKey
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="loadIndexInfo",produces = "application/json;charset=UTF-8")
	public @ResponseBody String loadIndexInfo(String nodeKey)
	{
		Gson gson=new Gson();
		SearchServer searchServer=SolrContext.getInstance().getNodes().get(nodeKey);
		if(searchServer!=null)
		{
			Map<String,Object>result=new HashMap<String,Object>();
			result.put("totalNum", searchServer.getTotalNum());
			result.put("indexdNum",searchServer.getIndexdNum());
			return gson.toJson(result);
		}
		return null;
	}
	
	/**
	 * 添加新词汇
	* @Title: addNewWords 
	* @param @param newWord
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="addNewWords")
	public @ResponseBody  String addNewWords(String newWords)
	{
		JSONObject obj=new JSONObject();
		if(!StringUtils.isEmpty(newWords))
		{
			Map<String,Object>params=new HashMap<String,Object>();
			params.put("newWords",newWords);
			String responseResult=HttpClientUtil.sendPostRequest(ContextInitListener.SOLR_PROPS.getProperty("solr.addNewWordUrl"), params); 
			if(!StringUtils.isEmpty(responseResult)){
				
			}
		}
		obj.put("success", true);
		return obj.toString();
	}
	
}
