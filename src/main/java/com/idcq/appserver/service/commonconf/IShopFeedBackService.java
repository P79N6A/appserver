/**
 * 
 */
package com.idcq.appserver.service.commonconf;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.commonconf.ShopFeedBackDto;

/** 
 * @ClassName: IShopFeedBackService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月16日 下午3:04:42 
 *  
 */
public interface IShopFeedBackService {
	
	
	void insertShopFeedBack(ShopFeedBackDto shopFeedBackDto, String attachementIdStr)throws Exception;
	
	/**
	 * 查询商铺反馈历史接口 
	* @Title: getShopFeedbackList 
	* @param @param shopId
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return PageModel    返回类型 
	* @throws
	 */
	PageModel getShopFeedbackList(Long shopId,PageModel pageModel)throws Exception;
	
	ShopFeedBackDto getShopFeedbackInfo(Long feedbackId)throws Exception;
}
