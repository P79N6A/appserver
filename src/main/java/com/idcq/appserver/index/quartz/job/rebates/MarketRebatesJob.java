package com.idcq.appserver.index.quartz.job.rebates;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.bill.IPlatformBillService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.shop.IShopBillService;
import com.idcq.appserver.utils.NumberUtil;

public class MarketRebatesJob extends QuartzJobBean {

	private final static Logger logger = LoggerFactory.getLogger(MarketRebatesJob.class);
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IShopAccountDao shopAccountDao;
	@Autowired
	private IPlatformBillService platformBillService;
	@Autowired
	private IShopBillService shopBillService;
	@Autowired
	private IShopDao shopDao;
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("每日返还店铺生意金定时任务-start");
		Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
        Object orderMoney4RatioConfig = configMap.get("b1_order_ratio_money4"); //绿店每人返利比例
        Object voucherMoney4RatioConfig = configMap.get("b2_voucher_ratio_money4");  //红店每人返利比例
        Double orderMoney4Ratio = orderMoney4RatioConfig == null ? 0 : Double.valueOf(orderMoney4RatioConfig.toString());
        Double voucherMoney4Ratio = voucherMoney4RatioConfig == null ? 0 : Double.valueOf(voucherMoney4RatioConfig.toString());
        try {
	        //更新店铺账户
			Integer limit = 0;
			Integer pageSize = 20;
			for (;;) {
				List<ShopAccountDto> shopAccountList = shopAccountDao.getAllAccount(limit, pageSize);
				if (CollectionUtils.isEmpty(shopAccountList)) {
					break;
				}
				
				for (ShopAccountDto shopAccount : shopAccountList) {
					Long shopId = shopAccount.getShopId();
					Double marketBlance = NumberUtil.sub(shopAccount.getMarketRebateTotal(), shopAccount.getMarketRebateMoney());
					if (marketBlance <= 0) {
						continue;
					}
					Double ratio = 0D;
					Double monkey4Amount = 0D;     
					ShopDto shop = shopDao.getShopById(shopId);
					if(null != shop.getShopIdentity() && shop.getShopIdentity() == 2) {
					    ratio = voucherMoney4Ratio;
					} else {
					    ratio = orderMoney4Ratio;
					}
				    monkey4Amount = NumberUtil.multiply(marketBlance, ratio);
				    shopAccountDao.updateShopAccount(shopId, monkey4Amount, null, null, null, null, 
				    		null, null, monkey4Amount, monkey4Amount, null, null,monkey4Amount);
			        //插入店铺账单
			        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopId);
			        //分账前金额
			        Double changeAmountBefore = NumberUtil.sub(shopAccountDto.getAmount(), monkey4Amount);
			        shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_ADD, monkey4Amount,
			                null, shopId, changeAmountBefore, shopAccountDto.getMarketAmount(), CommonConst.SHOP_ACCOUNT_TYPE_MARKET,
			                CommonConst.SHOP_BILL_TYPE_RETURN_MARKET,"会员制单每日返利", "3721消费获得生意金", null);
					OrderDto recordOrder = new OrderDto();
					recordOrder.setShopId(shopId);

			        //平台出账
			        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, monkey4Amount, 
			                CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, recordOrder, null, "消费每日返还生意金", "3721消费每日返还生意金,返还比例："+ratio,
			                CommonConst.PLATFORM_BILL_TYPE_BACK_MARKET, CommonConst.PLATFORM_BILL_STATUS_OVER);
				}
				
				limit = limit + pageSize;
			}
	        
			
		} catch (Exception e) {
			logger.info("每日返还店铺生意金定时任务-异常",e);
		}
	}

}
