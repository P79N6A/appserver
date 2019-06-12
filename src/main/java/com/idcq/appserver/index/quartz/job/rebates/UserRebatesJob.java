package com.idcq.appserver.index.quartz.job.rebates;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.settle.ISettleService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 
 * @Description: 会员、推荐返现定时任务
 * 
 */
public class UserRebatesJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(UserRebatesJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("会员、推荐返现定时任务-start");
		try {
			IUserAccountDao userAccountDao = BeanFactory
					.getBean(IUserAccountDao.class);
			Integer limit = 0;
			Integer pageSize = 20;
			while (true) {
				List<UserAccountDto> userAccountList = userAccountDao.getAllAccount(limit, pageSize, 0D);
				if (CollectionUtils.isEmpty(userAccountList)) {
					break;
				} else {
					updateUserAccount(userAccountList);
				}
				// 每页20条
				limit = limit + 20;
			}
		} catch (Exception e) {
			logger.info("会员、推荐返现定时任务-异常");
			e.printStackTrace();
		}

	}

	/**
	 * 会员、推荐返现定时任务
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void updateUserAccount(List<UserAccountDto> userAccountList) throws Exception {
		
		ISettleService settleService = BeanFactory.getBean(ISettleService.class);
		IUserDao userDao = BeanFactory.getBean(IUserDao.class);
		ICommonService commonService = BeanFactory.getBean(ICommonService.class);

        Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
       
      
		for (UserAccountDto userAccount : userAccountList) {
			
			UserDto user = userDao.getUserById(userAccount.getUserId());
			if(user==null){
				logger.info("查询user为空，userId="+userAccount.getUserId());
				break;
			}
			  //返利比例
	        Object ratioConfig = configMap.get(user.getRebatesLevel());
	        Double ratioRatio = ratioConfig == null ? 0 : Double.valueOf(ratioConfig.toString());
	        
	        
			//消费返还总额
			Double consumeRebateTotal = userAccount.getConsumeRebateTotal();
			
			//已经消费返还总额
			Double consumeRebateMoney = userAccount.getConsumeRebateMoney();
			//待返余额
			Double balanceMoney = NumberUtil.sub(consumeRebateTotal, consumeRebateMoney);
			if(balanceMoney <= 0){
				logger.info("代办余额小于等于0，开始处理下一个用户");
				continue;
			}
			Double balanceMoneyForDay = NumberUtil.multiply(balanceMoney, ratioRatio);
			//处理用户返利
			OrderDto orderDto = new OrderDto();
			orderDto.setOrderTitle("返利"+balanceMoneyForDay+"元");
			orderDto.setMobile(user.getMobile());
			settleService.userAccountSettle(orderDto, configMap, user.getUserId(), user.getRebatesLevel(),
					balanceMoneyForDay, CommonConst.USER_BILL_TYPE_CONSUMER_REBATE, false, false, null);
			//用户返利
			Long referUserId = user.getReferUserId();
            if (referUserId != null) {
                UserDto referUser = userDao.getDBUserById(referUserId);
                if (referUser != null) {
                	String referUserRebatesLevel = referUser.getRebatesLevel();
                    if (!"normal_ratio".equals(referUserRebatesLevel)) {
                        Object referRatioConfig = configMap.get("a_refer_a_ratio");
                        if (referRatioConfig != null) {
//                            String[] ratios = referRatioConfig.toString().split(":");
//                            Double referIncome = NumberUtil.multiply(balanceMoneyForDay, Double.valueOf(ratios[0]));
//                            referIncome = NumberUtil.divide(referIncome, Double.valueOf(ratios[1]), 4);
//                            //处理推荐人分成
//                			OrderDto orderRefer = new OrderDto();
//                			orderRefer.setOrderTitle("推荐返利"+referIncome+"元");
                			settleService.userAccountSettle(new OrderDto(), configMap, referUserId, referUserRebatesLevel,
                			        balanceMoneyForDay, CommonConst.USER_BILL_TYPE_USER_REWARD, true, false, null);
                        }
                    }
                }
            }
			
		}
	}
}
