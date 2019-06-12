package com.idcq.appserver.index.quartz.job.rebates;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.dao.rebates.IShopRebatesDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.rebates.ShopRebatesDto;
import com.idcq.appserver.service.common.ICommonService;

/**
 * 该定时任务执行红绿店购买V商品的返点
 * Created by Administrator on 2016/9/10 0010.
 */
public class BuyvMoneyRebateJob extends QuartzJobBean
{
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IShopRebatesDao shopRebatesDao;
    @Autowired
    private IShopDao shopDao;
    @Override protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        int jobNum = shopRebatesDao.countRemainedRebates();
        if(jobNum <= 0)
        {
            return;
        }
        int start = 0;
        int end = 100;
        int pSize = 100;
        List<ShopRebatesDto> toRebates = null;
        while (end - jobNum < pSize){
            RowBounds rowBounds = new RowBounds(start, pSize);
            toRebates = shopRebatesDao.getToExecuteRebates(rowBounds);
            try
            {
                this.rebating(toRebates);
            }
            catch (Exception e)
            {
                throw new JobExecutionException(e);
            }
            start = pSize + start;
            end = pSize + end;
        }
    }

    /**
     * 处理返点，并记账单
     * @param toRebates
     */
    private void rebating(List<ShopRebatesDto> toRebates) throws Exception{
        if (CollectionUtils.isNotEmpty(toRebates)) {
            Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
            for (ShopRebatesDto shopRebatesDto : toRebates) {
                commonService.shopBuyvMoneyRebates(shopRebatesDto, false, shopDao.getUserIdByShopIed(shopRebatesDto.getShopId()), configMap);
            }
        }
    }
}
