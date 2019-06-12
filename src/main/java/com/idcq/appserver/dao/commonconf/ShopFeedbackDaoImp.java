/**
 * 
 */
package com.idcq.appserver.dao.commonconf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.commonconf.ShopFeedBackDto;

/** 
 * @ClassName: ShopFeedbackDaoImp 
 * @Description: TODO(商铺反馈数据访问层) 
 * @author 张鹏程 
 * @date 2015年4月16日 下午3:07:06 
 *  
 */
@Repository
public class ShopFeedbackDaoImp extends BaseDao<ShopFeedBackDto> implements IShopFeedbackDao {

	/** 
	* @Title: insertShopFeedback 
	* @Description: TODO(添加商铺反馈) 
	* @param @param shopFeedbackDto
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public void insertShopFeedback(ShopFeedBackDto shopFeedbackDto)
			throws Exception {
		super.insert(generateStatement("insertShopFeedback"), shopFeedbackDto);
	}
	
	/**
	 * 获取商铺反馈列表
	 * @Title: getShopFeedBackList 
	 * @param @param shopId
	 * @param @param pageModel
	 * @param @return  
	 * @throws
	 */
	public PageModel getShopFeedBackList(Long shopId, PageModel pageModel)throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("start", pageModel.getPageSize()*(pageModel.getToPage()-1));
		params.put("limit", pageModel.getPageSize());
		params.put("shopId", shopId);
		pageModel.setList(super.findList(generateStatement("getShopFeedBackList"),params));
		pageModel.setTotalItem((int)super.selectOne(generateStatement("getShopFeedBackCount"),params));
		return pageModel;
	}

    @Override
    public ShopFeedBackDto getShopFeedbackInfo(Long feedbackId) throws Exception {
        return (ShopFeedBackDto) super.selectOne(generateStatement("getShopFeedbackInfo"), feedbackId);
    }
}
