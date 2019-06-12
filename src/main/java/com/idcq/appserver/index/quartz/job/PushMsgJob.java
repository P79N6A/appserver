package com.idcq.appserver.index.quartz.job;

import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.message.IPushUserMsgService;
import com.idcq.appserver.utils.BeanFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Administrator on 2016/9/11 0011.
 */
public class PushMsgJob  extends QuartzJobBean
{
    @Override protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        IPushUserMsgService pushUserMsgService = BeanFactory.getBean(IPushUserMsgService.class);
        try
        {
            pushUserMsgService.pushAllRemainedMsg();
        }
        catch (Exception e)
        {

        }
        ISendSmsService sendSmsService = BeanFactory.getBean(ISendSmsService.class);
        sendSmsService.sendAll3721RemainedMsg();
    }
}
