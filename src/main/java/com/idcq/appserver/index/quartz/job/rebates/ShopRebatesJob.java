package com.idcq.appserver.index.quartz.job.rebates;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.dao.rebates.IShopRebatesDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.rebates.ShopRebatesDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

/**
 * 
 * @Description: 店铺购买V产品返现定时任务
 * 
 */
public class ShopRebatesJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IShopRebatesDao shopRebatesDao;
    @Autowired
    private IShopDao shopDao;
	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("店铺购买V产品返现定时任务-start");
		try {
			Integer limit = 0;
			Integer pageSize = 20;
			for (;;) {
				ShopRebatesDto shopRebatesDto = new ShopRebatesDto();
				shopRebatesDto.setLimit(limit);
				shopRebatesDto.setPageSize(pageSize);
				List<ShopRebatesDto> shopRebatesList  = shopRebatesDao.getShopRebates(shopRebatesDto);
				if (CollectionUtils.isEmpty(shopRebatesList)) {
					break;
				} else {
					updateShopRebates(shopRebatesList);
				}
				// 每页20条
				limit = limit + 20;
			}
		} catch (Exception e) {
			logger.info("店铺购买V产品返现定时任务-异常");
			e.printStackTrace();
		}

	}

	/**
	 * 更新店铺购买V产品返现
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void updateShopRebates(List<ShopRebatesDto> shopRebatesList) throws Exception {
		
		IShopRebatesDao shopRebatesDao = BeanFactory
				.getBean(IShopRebatesDao.class);
		for (ShopRebatesDto shopRebatesDto : shopRebatesList) {
			//店铺购买V产品金额
			Double buyVMoney = shopRebatesDto.getBuyvMoney();
			//日均返还比例
			Double bBuyVRebatesRatio = shopRebatesDto.getbBuyvRebatesRatio();
			//店铺待返还金额
			Double beforeShopWaitRebatesMoney = shopRebatesDto.getShopWaitRebatesMoney();
			//每日返还金额
			Double changeMoney = NumberUtil.multiply(buyVMoney, bBuyVRebatesRatio);
			//处理后的待返金额
			Double afterShopWaitRebatesMoney = NumberUtil.sub(beforeShopWaitRebatesMoney, changeMoney);
			
			ShopRebatesDto parm = new ShopRebatesDto();
			parm.setShopWaitRebatesMoney(afterShopWaitRebatesMoney);
			parm.setLastUpdateTime(new Date());
			parm.setShopId(shopRebatesDto.getShopId());
			
			shopRebatesDao.updateShopRebates(shopRebatesDto);
			Long userId = shopDao.getUserIdByShopIed(shopRebatesDto.getShopId());
            saveSmsMobile(userId, changeMoney);
		}
	}
	
    private void saveSmsMobile(Long userId, Double money) throws Exception{
        UserDto user = userDao.getUserById(userId);
        if(user!=null){
            //保存短信
            SmsReplaceContent replaceContent = new SmsReplaceContent();
            replaceContent.setMobile(user.getMobile());
            replaceContent.setMoney(money);
            replaceContent.setUsage("shop_rebates");
            replaceContent.setStatus(5);//待发送
            sendSmsService.insertSmsMobileCode(replaceContent);
        }
    }
}
