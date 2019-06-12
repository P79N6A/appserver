package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BusAreaActStatusEnum;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 商圈活动切换
 * @ClassName: BusAreaActivityStatusChange 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年3月14日 上午10:23:24 
 *
 */
public class BusAreaActivityStatusChangeJob extends QuartzJobBean{

	private Log logger = LogFactory.getLog(this.getClass());
	
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
	    logger.info("商圈活动状态更新定时任务开始");
		PageModel pageModel=new PageModel();
		pageModel.setToPage(1);
		try{
			/**
			 * 步骤一:查找需要切换活动状态的活动
			 * 
			 * 分页查找出所有活动(状态不等于已结束的)
			 */
			BusinessAreaActivityDto searchCondition=new BusinessAreaActivityDto();
			searchCondition.setNotStatus(BusAreaActStatusEnum.STOP.getValue());
			IBusinessAreaActivityDao businessAreaActivityDao=(IBusinessAreaActivityDao)BeanFactory.getBean(IBusinessAreaActivityDao.class);
			IShopDao shopDao=BeanFactory.getBean(IShopDao.class);
			while(true){
				List<BusinessAreaActivityDto>busAreaActivityList=businessAreaActivityDao.getBusinessAreaActivityListByCondition(searchCondition, pageModel);
				if(busAreaActivityList.size()==0){
					break;
				}
				dealBusinessAreaActivity(busAreaActivityList, businessAreaActivityDao, shopDao); 
				pageModel.setToPage(pageModel.getToPage()+1);
			}
			/**
			 * 步骤二:对活动列表中的活动状态进行处理
			 * 
			 * 比较当前时间和活动的各个时间点,变更数据库中活动状态
			 */
			
			/**
			 * 步骤三:当发起方开始活动或者结束活动后，变更 solr中的店铺
			 */
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理商圈活动
	 * @Title: dealBusinessAreaActivity 
	 * @param @param busAreaActivityList
	 * @return void    返回类型 
	 * @throws
	 */
	private void dealBusinessAreaActivity(List<BusinessAreaActivityDto>busAreaActivityList,IBusinessAreaActivityDao businessAreaActivityDao,IShopDao shopDao)throws Exception{
		Date currentDate=new Date();
		currentDate = DateUtils.parse(DateUtils.format(currentDate, DateUtils.ZEROTIME_FORMAT));
		List<BusinessAreaActivityDto>businessAreaActivityList=new ArrayList<BusinessAreaActivityDto>();
		List<ShopDto>refreshSolrShopList=new ArrayList<ShopDto>();//需要刷新solr的店铺列表
		List<BusinessAreaActivityDto>needSendMessageToUserActivitys=new ArrayList<BusinessAreaActivityDto>();
		for(BusinessAreaActivityDto businessAreaActivityDto:busAreaActivityList){
			ShopDto shopDto=new ShopDto();
			shopDto.setShopId(businessAreaActivityDto.getShopId());//solr需要刷新
			int activityStatus=businessAreaActivityDto.getActivityStatus();
			if(BusAreaActStatusEnum.NOT_START.getValue()==activityStatus||BusAreaActStatusEnum.APPLYING.getValue()==activityStatus){//活动状态为未开始
				if(DataCacheApi.get(CommonConst.KEY_SEND_ACTIVITY+businessAreaActivityDto.getBusinessAreaActivityId())==null){//避免重复发送:此处redis挂了就会有bug
					if(BusAreaActStatusEnum.APPLYING.getValue()==activityStatus){
						if(currentDate.compareTo(businessAreaActivityDto.getSignEndDate())>0){//过了活动报名截止时间就给所有店铺的店内会员发送消息
							DataCacheApi.setex(CommonConst.KEY_SEND_ACTIVITY+businessAreaActivityDto.getBusinessAreaActivityId(),"true" ,60*60*24*30);// 存入redis
							needSendMessageToUserActivitys.add(businessAreaActivityDto);//代表此活动需要推送消息给该商圈参与方店铺的店内会员
						}
					}		
				}
				if(currentDate.compareTo(businessAreaActivityDto.getBeginDate())>=0){
					DataCacheApi.del(CommonConst.KEY_SEND_ACTIVITY+businessAreaActivityDto.getBusinessAreaActivityId());
					shopDto.setBusinessAreaActivityFlag(1);//设置商铺正在发起商圈活动
					refreshSolrShopList.add(shopDto);
					businessAreaActivityDto.setActivityStatus(BusAreaActStatusEnum.RUNNING.getValue());//将状态变更为正在运行
					businessAreaActivityList.add(businessAreaActivityDto);
				}
				
				if (currentDate.compareTo(businessAreaActivityDto.getEndDate())>0) {
					DataCacheApi.del(CommonConst.KEY_SEND_ACTIVITY+businessAreaActivityDto.getBusinessAreaActivityId());
					shopDto.setBusinessAreaActivityFlag(0);//设置商铺正在发起商圈活动
					refreshSolrShopList.add(shopDto);
					businessAreaActivityDto.setActivityStatus(BusAreaActStatusEnum.STOP.getValue());//将状态变更为正在运行
					businessAreaActivityList.add(businessAreaActivityDto);
				}
				
			}else if(BusAreaActStatusEnum.RUNNING.getValue()==activityStatus){//活动状态为进行中
				if(currentDate.compareTo(businessAreaActivityDto.getEndDate())>0){//过了活动时间
					refreshSolrShopList.add(shopDto);
					shopDto.setBusinessAreaActivityFlag(0);//设置商铺正在发起商圈活动
					businessAreaActivityDto.setActivityStatus(BusAreaActStatusEnum.STOP.getValue());//活动状态为已结束
					businessAreaActivityList.add(businessAreaActivityDto);
				}
			}
		}
		if(businessAreaActivityList.size()>0){//批量更新活动的状态
			businessAreaActivityDao.batchUpdateBusinessAreaActivity(businessAreaActivityList);
		}
		if(refreshSolrShopList.size()>0){
			shopDao.batchUpdateShopMarketing(refreshSolrShopList);//改变数据库中的状态值,定时器会定时器去建索引
		}
		//TODO 推送这块需要好好规划，不能一并发短信处理
//		if(needSendMessageToUserActivitys.size()>0){//需要推送给用户信息的活动列表
//			dealPushActivityInfoToUser(needSendMessageToUserActivitys, businessAreaActivityDao);
//		}
	}
	
	/**
	 * 处理推送活动信息给用户
	 * @Title: dealPushActivityInfoToUser 
	 * @param @param activityList
	 * @return void    返回类型 
	 * @throws
	 */
	private void dealPushActivityInfoToUser(List<BusinessAreaActivityDto>activityList,IBusinessAreaActivityDao businessAreaActivityDao){
		try{
			for(BusinessAreaActivityDto activity:activityList){
				List<Long>activityIdList=new ArrayList<Long>();
				activityIdList.add(activity.getBusinessAreaActivityId());
				IShopMemberDao shopMemberDao=BeanFactory.getBean(IShopMemberDao.class);
				List<ShopMemberDto>shopMemberDtoList=shopMemberDao.getShopMemberStatisInfoByActivityIdList(activityIdList);//店内会员数目统计，店内会员为0的没必要推送
				List<Long>needPushToUserShopList=new ArrayList<Long>();//需要推送店内会员的店铺Id列表
				for(ShopMemberDto shopMemberDto:shopMemberDtoList){
					if(shopMemberDto.getShopMemberNum()>0){//代表店内会员数目大于0，需要推送消息
						needPushToUserShopList.add(shopMemberDto.getShopId());
					}
				}
				if(needPushToUserShopList.size()>0){
					List<ShopMemberDto>shopMemberList=shopMemberDao.getNeedPushShopMemberList(needPushToUserShopList);
					pushMessageToShopMember(shopMemberList,activity.getBusinessAreaActivityId());
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 推送消息给店内会员
	 * @Title: pushMessageToShopMember 
	 * @param @param shopMemberList
	 * @return void    返回类型 
	 * @throws
	 */
	private void pushMessageToShopMember(List<ShopMemberDto>shopMemberList,Long activityId){
		try{
			for(ShopMemberDto shopMemberDto:shopMemberList){
				Map<String,Object>params=new HashMap<String,Object>();
				params.put("businessAreaActivityId", activityId);
				params.put("userData", shopMemberDto);
				ProgramUtils.executeBeanByExecutePointCode("businessAreaPushUserMessagePoint", 1, params);
			}
		}catch(Exception e){
			logger.error("发送短信过程中产生了异常",e);
		}
	}
}
