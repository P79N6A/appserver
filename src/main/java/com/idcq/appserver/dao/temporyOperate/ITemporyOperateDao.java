package com.idcq.appserver.dao.temporyOperate;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
/**
 * 索引表操作记录 数据访问层
* @ClassName: ITemporyOperateDao 
* @Description: TODO(索引表操作记录 数据访问层) 
* @author 张鹏程 
* @date 2015年3月28日 下午6:05:06 
*
 */
public interface ITemporyOperateDao {

	/**
	 * 根据类型进行查询
	 * @param type
	 * @return
	 */
	public List<TemporyOperateDto> queryList(String type,int pageSize);
	
	/**
	 * 加入临时操作
	 * @param temporyOperateDto
	 * @return
	 */
	public TemporyOperateDto insertTemporyOperate(TemporyOperateDto temporyOperateDto);

	/**
	 * 根据参数进行删除
	 * @param params
	 */
	public void deleteByParams(Map<String, Object> params);
	
	/**
	 * 获取需要索引的商铺条数
	* @Title: getNeedIndexShopCount 
	* @Description: TODO
	* @param @param lastUpdateTime
	* @param @return
	* @param @throws Exception
	* @return int    返回类型 
	* @throws
	 */
	public int getNeedIndexShopCount(String lastUpdateTime)throws Exception;
	
	 /**
	  * 获取需要索引的商品数据
	 * @Title: getNeedIndexGoodsCount 
	 * @Description: TODO
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	  */
	public int getNeedIndexGoodsCount(String lastUpdateTime)throws Exception;
	
	/**
	 * 需要更新的商品族条数
	 * @Title: getNeedIndexGoodsGroupCount 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	 */
	public int getNeedIndexGoodsGroupCount(String lastUpdateTime)throws Exception;
	
	
	/**
	 * 由于商品族的商品发生了改变，需要更新的商品族的个数
	 * @Title: getNeedIndexGoodsForGroupCount 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	 */
	public int getNeedIndexGoodsForGroupCount(String lastUpdateTime)throws Exception;
	
	/**
	 * 根据商铺第三方表获取需要更新索引的商品数据
	 * @Title: getNeedIndexShopForMarketCount 
	 * @Description: TODO
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	 */
	public int getNeedIndexShopForMarketCount(String lastUpdateTime)throws Exception;
	
	/**
	 * 根据商品id最大值删除商品
	 * @Title: deleteByMaxGoodsId 
	 * @Description: TODO
	 * @param @param maxGoodsId
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void deleteByMaxGoodsId(Long maxGoodsId,Long deleteSize)throws Exception;
	
	/**
	 * 将商品数据复制到其他表中
	* @Title: copyGoodsDataIntoTable 
	* @Description: TODO
	* @param @param pageSize
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	public void copyGoodsDataIntoTable(Long pageSize)throws Exception;
	
	public Long  queryMaxGoodsId(Long pageSize)throws Exception;
}
