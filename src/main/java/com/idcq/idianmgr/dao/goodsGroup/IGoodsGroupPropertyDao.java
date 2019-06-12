/**
 * 
 */
package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;

/**
 * 
 * @author zhq
 *
 */
public interface IGoodsGroupPropertyDao {
	
	public List<GoodsGroupPropertyDto> getGoodsGroupProList(Map param)throws Exception;
	
	public GoodsGroupPropertyDto getGoodsGroupProperty(Map param) throws Exception;
	
	public int delGoodsGroupProperty(int propertyId) throws Exception ;
	
	public int updateGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception;
	
	public Long insertGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception;

	/**
	 * 根据属性值编号查询属性信息
	 * @param proValuesId
	 * @return
	 */
	public GoodsGroupPropertyDto getGoodsGroupPropertyByProValuesId(
			Long proValuesId);

	/**
	 * 添加商品族属性记录，返回主键
	 * @param ggpDto
	 * @throws Exception
	 */
	public Long addGoodsGroupProBackId(GoodsGroupPropertyDto ggpDto) throws Exception ;

	/**
	 * 根据商品族编号查询商品族属性记录
	 * @param goodsGroupId
	 * @return
	 * @throws Exception
	 */
	public GoodsGroupPropertyDto getGoodsGroupPropertyByGroupId(
			Long goodsGroupId) throws Exception ;
	
	public List<GoodsGroupPropertyDto>getGoodsGroupProperyByGroupId(Long groupId)throws Exception;
	
}	
