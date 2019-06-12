package com.idcq.appserver.index.quartz.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.solr.SolrOperateService;

/**
 * 
 * @ClassName: checkShopStatusJob
 * @Description: 定时检测商铺的合同截止日，并更新商铺状态
 * @author 陈永鑫
 * @date 2015年5月5日 下午4:12:06
 * 
 */
public class CheckShopStatusJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("检测商铺合同日截止日-start");
		try {
			IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
			Integer minPage = 0;
			Integer maxPage = 20;
			while (true) {
				List<Map<String, Object>> shopList = shopDao.getBaseShopList(minPage, maxPage);
				if (CollectionUtils.isEmpty(shopList)) {
					break;
				} else {
						updateShopStatus(shopList);
				}
				minPage = minPage + 20;
			}
		} catch (Exception e) {
			logger.info("检测商铺合同日截止日-异常");
		}

	}

	/**
	 * 更新商铺状态
	 * 
	 * @param shopId
	 * @throws Exception
	 */
	public void updateShopStatus(List<Map<String, Object>> list) throws Exception {
		IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
		Date nowDate = DateUtils.getCurrentDate(DateUtils.DATE_FORMAT);
		StringBuffer shopIdStr = new StringBuffer();
		for (Map<String, Object> mapShop : list) {
			if (null != mapShop && 0 != mapShop.size()) {
				Long shopId = (Long) mapShop.get("shopId");
				logger.info("检测商铺合同日截止日,shopId:"+shopId);
				Date contractValidTo = (Date) mapShop.get("contractValidTo");
				if(null!=contractValidTo){
					Integer nowFlag = nowDate.compareTo(contractValidTo);
					if(nowFlag>0){
						logger.info("商铺合同过期，更改商品状态为下线，商铺ID："+shopId);
						ShopDto shopDto = new ShopDto();
						shopDto.setShopId(shopId);
						//下线状态
						shopDto.setShopStatus(1);
						shopDao.updateShopStatus(shopDto);
						shopIdStr.append(shopId+",");
						//店铺状态有修改，清空缓存
						DataCacheApi.del(CommonConst.KEY_SHOP + shopId);
						//推送消息给收银机
						pushShopMessage(shopId);
					}
				}
			}
		}
		int strIndex = shopIdStr.indexOf(",");
		if(-1!=strIndex){
			SolrOperateService.refreshShop(shopIdStr.toString());
		}
	}
	
	public void pushShopMessage(Long shopId){
		try {
			IPushService pushService = BeanFactory.getBean(IPushService.class);
			JSONObject pushTarget=new JSONObject();
			pushTarget.put("action", "shopDataUpdate");
			pushTarget.put("shopId",shopId );  
			pushTarget.put("lastUpdate", DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
			PushDto push = new PushDto();
			push.setShopId(shopId);
			push.setAction("shopDataUpdate");
			push.setContent(pushTarget.toString());
			pushService.pushInfoToShop2(push);
		} catch (Exception e) {
			logger.error("检测商铺合同日截止日，推送消息给收银机异常："+e.toString());
		}	
	}


}
