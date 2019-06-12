package com.idcq.appserver.index.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SorlFreshJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        SolrCatchDataJob job =new SolrCatchDataJob();
        job.rebuildAll(null);
    }
   
}
