/**
 * 
 */
package com.idcq.appserver.service.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.GoodsSetDto;

/** 
 * 商品
 * @ClassName: IGoodsService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月18日 上午11:20:34 
 *  
 */
public interface IGoodsSetService {
	
	
	/**
	 * 根据套餐编号查找商品编号
	* @Title: getGoodsIdListByGoodsSetId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param goodsSetId
	* @param @return
	* @param @throws Exception
	* @return List<Integer>    返回类型 
	* @throws
	 */
	public List<GoodsSetDto>getGoodsIdListByGoodsSetId(Long goodsSetId)throws Exception;
	
	/** 
	* @Title: IGoodsSetService.java
	* @Description: 批量添加goodsset表数据
	* @param @param goodsSetDtos    
	* @throws 
	*/
	public void batchInsertGoodsSet(List<GoodsSetDto> goodsSetDtos)throws Exception;
}
