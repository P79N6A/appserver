//package com.idcq.appserver.index.quartz.job;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import redis.clients.jedis.Jedis;
//
//import com.google.gson.Gson;
//import com.idcq.appserver.dto.common.PageModel;
//import com.idcq.appserver.dto.message.MessageDto;
//import com.idcq.appserver.service.home.IHomeServcie;
//import com.idcq.appserver.utils.BeanFactory;
//import com.idcq.appserver.utils.DateUtils;
//import com.idcq.appserver.utils.JedisPoolUtils;
//import com.idcq.appserver.utils.jedis.DataCacheApi;
//
///**
// * 
// * @ClassName: SolrCatchDataJob
// * @Description: TODO (缓存消息)
// * @author 陈永鑫
// * @date 2015年4月27日 下午4:48:52
// * 
// */
//public class MessageCacheDataJob implements Job {
//
//	private final Log logger = LogFactory.getLog(getClass());
//
//	private static String lastUpdateTime = DateUtils.format(new Date(),
//			"yyyy-MM-dd HH:mm:ss");
//
//	public void execute(JobExecutionContext context)
//			throws JobExecutionException {
//		Map properties = context.getMergedJobDataMap();
//		try {
//			String temporyUpdateTime = DateUtils.format(new Date(),
//					"yyyy-MM-dd HH:mm:ss");
//			//loadSystemMessageToRedis();
//			loadShopMessageToRedis();
//			lastUpdateTime = temporyUpdateTime;// 在执行创建索引的过程中，可能有新的记录产生
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 加载系统消息数据
//	 */
//	public static void loadSystemMessageToRedis() throws Exception {
//		Gson gson = new Gson();
//		//DataCacheApi dataCacheApi = DataCacheApi.getInstance();
//		Jedis jedis=JedisPoolUtils.getJedis();
//		IHomeServcie homeService = BeanFactory.getBean(IHomeServcie.class);
//		MessageDto message = new MessageDto();
//		//系统消息
//		message.setMsgType(0);
//		PageModel pageModel = new PageModel();
//		pageModel.setPageSize(10);
//		while (true) {
//			pageModel = homeService.getMessage(message, 1, 10);
//			long endTime = new Date().getTime();
//			if (pageModel.getList() == null || pageModel.getList().size() == 0) {
//				break;
//			}
//			List<MessageDto> data = (List<MessageDto>) pageModel.getList();
//			for (MessageDto messageDto : data) {
//				//String  resultData = dataCacheApi.get("system_message" + messageDto.getId());
//				String  resultData = jedis.get("system_message" + messageDto.getId());
//				//如果缓存不存在，增加
//				if(StringUtils.isBlank(resultData)){
//					String messageJsonData = gson.toJson(messageDto);
//					//TODO 目前写死1200秒失效
//					//dataCacheApi.setex("system_message" + messageDto.getId(), messageJsonData, 1200);
//					jedis.setex("system_message" + messageDto.getId(), 1200, messageJsonData);
//				}
//			}
//			endTime = new Date().getTime();
//			pageModel.setToPage(pageModel.getToPage() + 1);
//		}
//	}
//	/**
//	 * 加载商铺消息数据
//	 */
//	public static void loadShopMessageToRedis() throws Exception {
//		Gson gson = new Gson();
//		//DataCacheApi dataCacheApi = DataCacheApi.getInstance();
//		Jedis jedis=JedisPoolUtils.getJedis();
//		IHomeServcie homeService = BeanFactory.getBean(IHomeServcie.class);
//		MessageDto message = new MessageDto();
//		//商铺消息
//		message.setMsgType(1);
//		PageModel pageModel = new PageModel();
//		pageModel.setPageSize(10);
//		while (true) {
//			pageModel = homeService.getMessage(message, pageModel.getToPage(), 10);
//			if (pageModel.getList() == null || pageModel.getList().size() == 0) {
//				break;
//			}
//			List<MessageDto> data = (List<MessageDto>) pageModel.getList();
//			for (MessageDto messageDto : data) {
//				//String  resultData = dataCacheApi.get("shop_message" + messageDto.getId());
//				String  resultData = jedis.get("shop_message" + messageDto.getId());
//				//如果缓存不存在，增加
//				if(StringUtils.isBlank(resultData)){
//					String messageJsonData = gson.toJson(messageDto);
//					//TODO 目前写死1200秒失效
//					//dataCacheApi.setex("shop_message" + messageDto.getId(), messageJsonData, 1200);
//					jedis.setex("shop_message" + messageDto.getId(), 1200,messageJsonData);
//				}
//			}
//			pageModel.setToPage(pageModel.getToPage() + 1);
//		}
//		JedisPoolUtils.returnRes(jedis);
//	}	
//}
