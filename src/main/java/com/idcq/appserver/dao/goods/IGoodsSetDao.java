/**
 * 
 */
package com.idcq.appserver.dao.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.goods.GoodsSetDto;

/** 
 * @ClassName: IGoodsSetDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月18日 上午11:23:03 
 *  
 */
public interface IGoodsSetDao {
	
	/**
	 * 根据商品套餐编号查找该套餐所有商品编号
	* @Title: getGoodsIdListByGoodsSetId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param goodsSetId
	* @param @return
	* @return List<Integer>    返回类型 
	* @throws
	 */
	public List<GoodsSetDto>getGoodsIdListByGoodsSetId(Long goodsSetId);
	
	/** 
	* @Title: IGoodsSetDao.java
	* @Description: 批量添加goodsset表数据
	* @param @param goodsSetDtos    
	* @throws 
	*/
	public void batchInsertGoodsSet(List<GoodsSetDto> goodsSetDtos) throws Exception;

	/** 
	* @Title: IGoodsSetDao.java
	* @Description: 批量修改goodsSetDtos表数据
	* @param @param goodsSetDtos
	* @param @throws Exception    
	* @throws 
	*/
	public void batchUpdateGoodsSet(List<GoodsSetDto> goodsSetDtos) throws Exception;
	public void deleteGoodsSet(Long goodsSetId) throws Exception;
	/** 
	* @Title: IGoodsSetDao.java
	* @Description: 查询出商铺的套餐list
	* @param @param param
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	public List<GoodsSetDto> getShopGoodsList(Map<String, Object> param) throws Exception;
	
	/** 
	* @Title: IGoodsSetDao.java
	* @Description: count ( 查询出商铺的套餐list)
	* @param @param param
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	public Integer getShopGoodsListCount(Map<String, Object> param) throws Exception;
	
	/** 
	* @Title: IGoodsSetDao.java
	* @Description: 根据套餐id查询出该套餐下所有的服务
	* @param @param param
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	public List<GoodsSetDto> getGoodsSetList(Map<String, Object> param) throws Exception;
	
}
