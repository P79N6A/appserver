package com.idcq.appserver.index.quartz.job.rebates;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.dao.rebates.IAgentRebatesDao;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.rebates.AgentRebatesDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

/**
 * 
 * @Description: 店铺购买V产品返现定时任务
 * 
 */
public class AgentRebatesJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
    private ICommonService commonService;
    @Autowired
    private IAgentRebatesDao agentRebatesDao;
    @Autowired
    private IAgentDao agentDao;
    @Autowired
    private IUserAccountDao userAccountDao;
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private IUserDao userDao;
	@SuppressWarnings("rawtypes")
    @Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("代理商返利定时任务-start");
		try {
			Integer limit = 0;
			Integer pageSize = 20;
			Map map = new HashMap();
			for (;;) {
				AgentRebatesDto agentRebates = new AgentRebatesDto();
				agentRebates.setLimit(limit);
				agentRebates.setPageSize(pageSize);
				agentRebates.setIsFinish(0);
				List<AgentRebatesDto> agentRebatesList  = agentRebatesDao.getAgentRebates(agentRebates);
				if (CollectionUtils.isEmpty(agentRebatesList)) {
					logger.info("查询agentRebates为空,跳出..");
					break;
				} else {
				    for (AgentRebatesDto agentRebatesDto : agentRebatesList) {

			            AgentDto agent = agentDao.getAgentByUserIdAndAgentId(
			                    agentRebatesDto.getUserId(), agentRebatesDto.getAgentId());
			            if(agent==null){
			                logger.info("代理不存在");
			                return;
			            }
			            Double dRebatesRatioByYear = agentRebatesDto.getAgentRebatesRatioYear();
			            if(dRebatesRatioByYear == null) {
			                Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
	                        Object dRebatesConfig = configMap.get("d_rebates_ratio_by_year");
	                        dRebatesRatioByYear = dRebatesConfig == null ? 0 : Double.valueOf(dRebatesConfig.toString());
	                       
			            }
			            Long agentId = agent.getAgentId();
			            Long userId = agent.getUserId();
			            //代理费
			            Double slottingFee = agentRebatesDto.getSlottingFee();
			            // 年返还金额
			            Double dRebatesYearMoney = NumberUtil.multiply(
			                    Double.valueOf(dRebatesRatioByYear.toString()), agentRebatesDto.getSlottingFee());
			            // 日返还
			            Double dRebatesDayMoney = NumberUtil.divide(dRebatesYearMoney, 365D, 4);
			            
			            //待返金额 
			            Double agentWaitRebatesMoney = agentRebatesDto.getAgentWaitRebatesMoney();
			            
			            UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
			            //总收益
			            Double totalIncome = NumberUtil.add(userAccount.getLegendTotal(),userAccount.getConsumeTotal(), userAccount.getVoucherTotal());
			            //总代理费
			            Double totalSlottingFee = agentRebatesDao.getUserTotalSlottingFee(userId);
			            if(totalIncome.compareTo(NumberUtil.multiply(totalSlottingFee, 2D)) > 0) {
			                logger.info("收益超过总代理费的2倍，停止返利。总收益：" + totalIncome + "总代理费：" + totalSlottingFee);
			                agentRebatesDto.setIsFinish(1);
			                agentRebatesDao.updateAgentRebates(agentRebatesDto);
			                continue;
			            }
			            
			            if(agentWaitRebatesMoney.compareTo(dRebatesDayMoney) <= 0) {
                            logger.info("待返利金额小于需要返利的金额，将返还待还金额，待返利金额为：" + agentWaitRebatesMoney + "需返利金额为：" + dRebatesDayMoney);
                            //将剩余的钱全返
                            dRebatesDayMoney = agentWaitRebatesMoney;
                            agentRebatesDto.setIsFinish(1);//标记为已返完
                        }
			            
			            commonService.agentRebate(agentId, userId, slottingFee, dRebatesRatioByYear, dRebatesDayMoney);
			            
			            //更新返利表的待返金额
			            agentRebatesDto.setAgentWaitRebatesMoney(NumberUtil.sub(agentWaitRebatesMoney, dRebatesDayMoney));
			            agentRebatesDto.setLastUpdateTime(new Date());
			            agentRebatesDao.updateAgentRebates(agentRebatesDto);
			            //封装短信参数
//			            putAdd(userId.toString(), dRebatesDayMoney, map);
			            
			        }
				}
				// 每页20条
				limit = limit + 20;
			}
//			Iterator it = map.keySet().iterator();
//			while(it.hasNext()){
//			    String key = it.next().toString();
//			    logger.info("用户ID:"+ key);
//			    //记录短信，不发送
//	            saveSmsMobile(Long.valueOf(key), Double.valueOf(map.get(key).toString()));
//			}
			
		} catch (Exception e) {
			logger.info("代理商返利定时任务-异常");
			e.printStackTrace();
		}

	}
	
	private void saveSmsMobile(Long userId, Double money) throws Exception{
	    UserDto user = userDao.getUserById(userId);
		if(user!=null){
	        //保存短信
	        SmsReplaceContent replaceContent = new SmsReplaceContent();
	        replaceContent.setMobile(user.getMobile());
	        replaceContent.setMoney(money);
	        replaceContent.setUsage("agent_rebates");
	        replaceContent.setStatus(5);//待发送
	        sendSmsService.insertSmsMobileCode(replaceContent);
		}
	}

	
	public static void putAdd(String userId, Double dRebatesDayMoney, Map map){
        if(!map.containsKey(userId)){
            map.put(userId, dRebatesDayMoney);
        } else {
            dRebatesDayMoney = NumberUtil.add(dRebatesDayMoney, Double.valueOf(map.get(userId).toString()));
            map.put(userId, dRebatesDayMoney);
        }
    }
	
	public static void main(String[] args) {
	    Map map = new HashMap();
	    putAdd("1", 1D, map);
	    putAdd("1", 2D, map);
	    putAdd("2", 2D, map);
	    System.out.println(map.toString());
    }
}
