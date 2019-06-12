package com.idcq.idianmgr.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.utils.BeanFactory;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;

/**
 * 
 * @author Administrator
 *
 */
public class CheckGoodsGroupStatusJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("删除草稿状态的商品/商品族-start");
		try {
			IGoodsGroupDao goodsGroupDao = BeanFactory.getBean(IGoodsGroupDao.class);
			List<GoodsGroupDto> list=goodsGroupDao.getDriftGoodsGroupList();
			List<Map<String,Object>> ids = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			GoodsGroupDto g=null;
			for(int i=0,len=list.size();i<len;i++){
				g=list.get(i);
				map = new HashMap<String, Object>();
				map.put("ggId",g.getGoodsGroupId());
				ids.add(map);
			}
			if(ids.size()>0){
				goodsGroupDao.delGoodsGroupBatch(ids);
				goodsGroupDao.delGoodsBatchByGoodsGroupId(ids);
			}
		} catch (Exception e) {
			logger.info("删除草稿状态的商品/商品族-异常");
			e.printStackTrace();
		}

	}

}
