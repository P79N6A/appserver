package com.idcq.appserver.dao.temporyOperate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
/**
 * 
* @ClassName: TemporyOperateDaoImp 
* @Description: TODO(索引表临时操作记录数据访问层实现) 
* @author 张鹏程 
* @date 2015年3月28日 下午6:06:38 
*
 */
@Repository
public class TemporyOperateDaoImp extends BaseDao<TemporyOperateDto>implements ITemporyOperateDao{

	
	/**
	 * 查询列表
	 */
	public List<TemporyOperateDto> queryList(String type,int pageSize)
	{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("type", type);
		params.put("pageSize", pageSize);
		return findList(generateStatement("getTemporyOperateListByType"),params);
	}

	public TemporyOperateDto insertTemporyOperate(
			TemporyOperateDto temporyOperateDto) {
		 super.insert(temporyOperateDto);
		 return temporyOperateDto;
	}

	public void deleteByParams(Map<String, Object> params) {
		super.delete(generateStatement("deleteByParams"), params);	
	}

	/** 
	* @Title: getNeedIndexShopCount 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param lastUpdateTime
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	public int getNeedIndexShopCount(String lastUpdateTime) throws Exception {
		
		Map<String,String>params=new HashMap<String,String>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (int)super.selectOne(generateStatement("queryNeedIndexShopCount"), params);
	}

	/**
	 * 获取需要索引的商品数量 
	* @Title: getNeedIndexGoodsCount 
	* @param @param lastUpdateTime
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public int getNeedIndexGoodsCount(String lastUpdateTime) throws Exception {
		Map<String,String>params=new HashMap<String,String>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (int)super.selectOne(generateStatement("queryNeedIndexGoodsCount"), params);
	}

	/** 
	 * 获取需要更新索引的商铺数量通过商铺关联表查询
	 * @Title: getNeedIndexShopForMarketCount 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public int getNeedIndexShopForMarketCount(String lastUpdateTime)
			throws Exception {
		Map<String,String>params=new HashMap<String,String>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (int)super.selectOne(generateStatement("queryNeedIndexShopForMarketCount"), params);
	}

	/** 
	 * @Title: deleteByMaxGoodsId 
	 * @param @param maxGoodsId
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public void deleteByMaxGoodsId(Long maxGoodsId,Long deleteSize) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("maxGoodsId", maxGoodsId);
		params.put("pageSize", deleteSize);
		super.delete(generateStatement("deleteByMaxGoodsId"), params);
	}

	/** 
	 * @Title: copyGoodsDataIntoTable 
	 * @param @param pageSize
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public void copyGoodsDataIntoTable(Long pageSize) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("pageSize", pageSize);
		super.update(generateStatement("copyGoodsDataIntoTable"), params);
	}

	/** 
	 * @Title: queryMaxGoodsId 
	 * @param @param pageSize
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public Long queryMaxGoodsId(Long pageSize) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("pageSize", pageSize);
		return (Long)super.selectOne(generateStatement("queryMaxGoodsId"), params);
	}

	@Override
	public int getNeedIndexGoodsGroupCount(String lastUpdateTime)
			throws Exception {
		Map<String,String>params=new HashMap<String,String>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (Integer)super.selectOne(generateStatement("getNeedIndexGoodsGroupCount"), params);
	}

	@Override
	public int getNeedIndexGoodsForGroupCount(String lastUpdateTime)
			throws Exception {
		Map<String,String>params=new HashMap<String,String>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (Integer)super.selectOne(generateStatement("getNeedIndexGoodsForGroupCount"), params);
	}
	
}
