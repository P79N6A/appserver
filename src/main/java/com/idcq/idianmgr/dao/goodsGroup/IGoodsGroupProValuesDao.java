/**
 * 
 */
package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;

/**
 * 
 * @author zhq
 *
 */
public interface IGoodsGroupProValuesDao {
	
	public List<GoodsGroupProValuesDto> getGoodsGroupProValuesList(Map param)throws Exception;
	
	public GoodsGroupProValuesDto getGoodsGroupProValues(Map param) throws Exception;
	
	public int delGoodsGroupProValue(int proValuesId) throws Exception;
	
	public int updateGoodsGroupProValue(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception;
	int updateIsNotSelectByIds(List<Long> list) throws Exception;
	public Long insertGoodsGroupProValue(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception;

	/**
	 * 添加商品族属性记录，返回主键
	 * @param ggpvDto
	 * @return
	 * @throws Exception
	 */
	public Long addGoodsGroupProValueBackId(GoodsGroupProValuesDto ggpvDto) throws Exception;

	/**
	 * 根据商品族属性值主键批量删除商品族属性值记录
	 * @param proValuesIds
	 * @return
	 */
	public int batchDelGoodsGroupProValuesDtoByIds(List<Long> proValuesIds);
	
	public List<GoodsGroupProValuesDto> getGoodsGroupProValuesList(List<Long> propertyIdList)throws Exception;

	/**
	 * 将商品族属性值置为选中
	 * @param goodsGroupProValuesIds
	 * @return
	 * @throws Exception
	 */
	public int updateIsSelectByIds(List<Long> goodsGroupProValuesIds)throws Exception;
	
	/**
	 * 将当前商品族属性值
	 * @return
	 * @throws Exception
	 */
	public int updateIsSelectEqZero(Long goodsGroupId) throws Exception;
	
}	
