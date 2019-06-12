package com.idcq.appserver.index.quartz.job;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserShopCommentDao;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 
 * @ClassName: ShopCommentJob
 * @Description: 定时更新商铺平均分
 * @author 陈永鑫
 * @date 2015年5月5日 下午4:12:06
 * 
 */
public class ComputeAvgGradeShopJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("计算商铺平均分-start");
		try {
			IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
			// DataCacheApi dataCacheApi = DataCacheApi.getInstance();
			Integer limit = 0;
			Integer pSize = 20;
			Integer  shopStatus = CommonConst.SHOP_STATUS_NORMAL;
			while (true) {
				List<Long> idList = shopDao.getIdListByStatus(limit, pSize,shopStatus);
				if (CollectionUtils.isEmpty(idList)) {
					break;
				} else {
					for (Long shopId : idList) {
						updateShopGrade(shopId);
					}
				}
				limit = limit + 20;
			}
		} catch (Exception e) {
			logger.info("计算商铺平均分-异常");
			e.printStackTrace();
		}

	}

	/**
	 * 更新商铺平均分
	 * 
	 * @param shopId
	 * @throws Exception
	 */
	public void updateShopGrade(Long shopId) throws Exception {
		logger.info("计算商铺平均分-shopId:"+shopId);
		IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
		IUserShopCommentDao userShopCommentDao = BeanFactory
				.getBean(IUserShopCommentDao.class);
		//缓存查询商铺信息
		ShopDto shopDB = shopDao.getNormalShopById(shopId);
		if(null!=shopDB){
			Integer columnId = shopDB.getColumnId();
			// 获取商铺平均分
			Map<String, Object> map = userShopCommentDao.getGradeById(shopId);
			if (null != map && 0 != map.size()) {
				//默认给5分好评
				BigDecimal envGrade = (BigDecimal) map.get("envGrade");
				if(null==envGrade){
					envGrade = new BigDecimal(5);
				}
				BigDecimal serviceGrade = (BigDecimal) map.get("serviceGrade");
				if(null==serviceGrade){
					serviceGrade = new BigDecimal(5);
				}
				BigDecimal logisticsGrade = (BigDecimal) map.get("logisticsGrade");
				if(null==logisticsGrade){
					logisticsGrade = new BigDecimal(5);
				}
				BigDecimal num = new BigDecimal(2);
				//非便利店：星级评分等=(环境评分+服务评分)/2；便利店：星级评分等=(配送速度评分+服务评分)/2
				BigDecimal starLevelGrade  = new BigDecimal(0);
				if(CommonConst.SHOP_TYPE_STORE==columnId){
					//便利店
					starLevelGrade = serviceGrade.add(logisticsGrade).divide(num);
				}
				else{
					//非便利店
					starLevelGrade = envGrade.add(serviceGrade).divide(num);
				}
				ShopDto shopDto = new ShopDto();
				shopDto.setEnvGrade(envGrade.doubleValue());
				shopDto.setServiceGrade(serviceGrade.doubleValue());
				shopDto.setStarLevelGrade(starLevelGrade.doubleValue());;
				shopDto.setShopId(shopId);
				shopDto.setLogisticsGgrade(logisticsGrade.doubleValue());
				shopDao.updateShopGrade(shopDto);
			}
		}
	}
}
