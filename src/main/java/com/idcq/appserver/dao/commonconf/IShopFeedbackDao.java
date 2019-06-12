/**
 * 
 */
package com.idcq.appserver.dao.commonconf;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.commonconf.ShopFeedBackDto;

/** 
 * @ClassName: IShopFeedbackDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月16日 下午3:06:52 
 *  
 */
public interface IShopFeedbackDao {

	void insertShopFeedback(ShopFeedBackDto shopFeedbackDto)throws Exception;
	
	
	/**
	 * 获取商铺反馈列表
	 * @Title: getShopFeedBackList 
	 * @param @param shopId
	 * @param @param pageModel
	 * @return void    返回类型 
	 * @throws
	 */
	PageModel getShopFeedBackList(Long shopId, PageModel pageModel)throws Exception;
	
	ShopFeedBackDto getShopFeedbackInfo(Long feedbackId) throws Exception;
}
