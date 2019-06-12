package com.idcq.appserver.index.quartz.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopRsrcDao;
import com.idcq.appserver.dto.shop.ShopRsrcDto;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 
 * @ClassName: OrderGoodsSettleJob
 * @Description: 定时调用处理分账存储过程
 * @author ZHQ
 * @date 2015年5月4日 下午4:12:06
 * 
 */
public class ShopResourceJob extends QuartzJobBean {

	private final Logger logger = Logger.getLogger(ShopResourceJob.class);
	
	@Override
	protected void executeInternal(JobExecutionContext arg) throws JobExecutionException {
		logger.info("商铺资源状态更新-start");
		try {
			IShopRsrcDao shopRsrcDao = BeanFactory.getBean(IShopRsrcDao.class);
			ShopRsrcDto shopRsrcDto=new ShopRsrcDto();
			//shopRsrcDto.setResourceStatus(CommonConst.RESOURCE_STATUS_IN_ORDER);
			List<ShopRsrcDto> list=shopRsrcDao.getShopResourceList(shopRsrcDto);
			if( null != list){
				for(int i=0,len=list.size();i<len;i++){
					shopRsrcDto=list.get(i);
					shopRsrcDto.setResourceStatus(CommonConst.RESOURCE_STATUS_NOT_IN_USE);
					shopRsrcDao.updateShopResource(shopRsrcDto);
				}
			}
		} catch (Exception e) {
			logger.info("调用处理订单结算存储过程-异常");
			e.printStackTrace();
		}
		logger.info("商铺资源状态更新-end");
	}

}
