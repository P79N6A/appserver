package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.order.IOrderShopRsrcService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
/**
 * 订单状态监测定时器
 * @ClassName: OrderStatusMonitorJob 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年8月1日 上午10:46:19 
 *
 */
public class OrderStatusMonitorJob  extends QuartzJobBean{
	IOrderDao orderDao=null;
	private Log logger = LogFactory.getLog(getClass());
	private ICommonService commonService;
	
	private IOrderShopRsrcService orderShopRsrcService;
	
	private IOrderServcie orderService;
	
	private final IPushService pushService=BeanFactory.getBean(IPushService.class);
	public void executeInternal(JobExecutionContext arg)
			throws JobExecutionException{
			String date=DateUtils.format(DateUtils.addHours(new Date(), -24), "yyyy-MM-dd HH:mm:ss");//当前时间24小时之前的时间
			if(orderDao==null)
			{
				orderDao=BeanFactory.getBean(IOrderDao.class);
			}
			if(commonService==null)
			{	
				commonService=BeanFactory.getBean(ICommonService.class);
			}
			if(orderShopRsrcService==null)
			{
				orderShopRsrcService=BeanFactory.getBean(IOrderShopRsrcService.class);
			}
			if(orderService==null)
			{
				orderService=BeanFactory.getBean(IOrderServcie.class);
			}
			try {
				pushInfoToUser();
				while(true)//每次处理10000条
				{
					List<OrderShopRsrcDto>orderDtos=orderShopRsrcService.getNeedAutoFinishOrder(date);
					if(orderDtos.size()==0)
					{
						break;
					}
					List<String>orderIdList=getOrderIdList(orderDtos);
					orderService.autoFinishOrder(orderIdList);
					//batchUpdateOrderStatus(orderIdList);//改变订单状态同时释放订单资源
					//pushOrderInfoChangeToShop(orderDtos);//推送给收音机
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void pushInfoToUser() throws Exception
	{
		logger.error("提醒用户订单将自动结束---start");
		new Thread(){
			public void run(){
				String startDate=DateUtils.format(DateUtils.addHours(new Date(), -22), "yyyy-MM-dd HH:mm:ss");//当前时间24小时之前的时间
				String endDate=DateUtils.format(DateUtils.addHours(new Date(), -23), "yyyy-MM-dd HH:mm:ss");
				int pageSize=10000;
				int page=1;
				while(true)
				{
					try{
						List<OrderDto>orderDtos=orderDao.getNeedPushFinishOrderForPage(startDate,endDate, (page-1)*pageSize, pageSize);
						logger.error("提醒用户订单,订单数目:"+orderDtos.size());
						if(orderDtos.size()==0)
						{
							logger.error("提醒用户订单将自动结束---结束");
							break;
						}
						page++;
						for(OrderDto orderDto:orderDtos)
						{
							try {
								logger.error("提醒推送第一步1");
								commonService.pushUserMsg("order","您在"+orderDto.getShopName()+"的预约订单即将过期，请尽快联系商家处理，否则您已支付的款项将打入商家账户",orderDto.getUserId(), 0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					catch(Exception e)
					{
						logger.error("订单由已预订或已完成状态自动已完成时，提醒用户过程中产生了异常",e);
					}
					
				}
				
			}
		}.start();
	}
	
	private void batchUpdateOrderStatus(List<String>orderIdList)throws Exception
	{
		if(orderIdList.size()>0)
		{
			orderDao.batchUpdateOrderStatus(orderIdList, OrderStatusEnum.SETTLE.getValue());
			//释放商铺资源
			orderShopRsrcService.releaseShopResource(orderIdList);
			
		}
		
	}
	
	/**
	 * 获取订单编号表
	 * @Title: getOrderIdList 
	 * @param @param orderShopRsrcDtoList
	 * @param @return
	 * @return List<String>    返回类型 
	 * @throws
	 */
	private List<String>getOrderIdList(List<OrderShopRsrcDto>orderShopRsrcDtoList)
	{
		List<String>orderIdList=new ArrayList<String>();
		for(OrderShopRsrcDto dto:orderShopRsrcDtoList)
		{
			orderIdList.add(dto.getOrderId());
		}
		return orderIdList;
	}
	
	private void pushOrderInfoChangeToShop(final List<OrderShopRsrcDto>orderShopRsrcDtoList)
	{
				for(OrderShopRsrcDto dto:orderShopRsrcDtoList)
				{
					try{
						JSONObject pushTarget=new JSONObject();
						pushTarget.put("action", "orderRemindToShop");
						pushTarget.put("shopId",dto.getShopId() );  
						pushTarget.put("lastUpdate", DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
						PushDto push = new PushDto();
						push.setShopId(dto.getShopId() );
						push.setAction("orderRemindToShop");
						pushService.pushInfoToShop2(push);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		
	}

}
