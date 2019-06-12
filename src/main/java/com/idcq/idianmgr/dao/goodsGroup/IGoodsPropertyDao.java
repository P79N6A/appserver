package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.goodsGroup.GoodsPropertyDto;

public interface IGoodsPropertyDao {

	/**
	 * 批量保存商品属性信息
	 * @param goodsPropertyDtos
	 * @return
	 */
	int batchAddGoodsProperty(List<GoodsPropertyDto> goodsPropertyDtos)throws Exception;

	/**
	 * 根据商品编号批量删除商品属性表中记录
	 * @param delGoodsIds
	 * @return
	 * @throws Exception
	 */
	int batchDelGoodsPropertyByGoodsId(List<Long> delGoodsIds)throws Exception;

	/**
	 * 添加商品族属性记录
	 * @param goodsPropertyDto
	 * @return
	 * @throws Exception
	 */
	int addGoodsPropertyDto(GoodsPropertyDto goodsPropertyDto)throws Exception;

	
	/**
	 * 根据商品编号查询商品属性值编号集合（场地类的商品跟商品属性为一一对应）
	 * @param goodsIds
	 * @return
	 * @throws Exception
	 */
	List<Long> getGoodsPropertyIdByGoodsIds(List<Long> goodsIds)throws Exception;
	
	
	/**
	 * 获取商品属性
	 * @Title: getGoodsPropertyByGoodsIdList 
	 * @param @param goodsIdList
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsPropertyDto>    返回类型 
	 * @throws
	 */
	List<GoodsPropertyDto>getGoodsPropertyByGoodsIdList(List<Long>goodsIdList)throws Exception;
	
	/**
	 * 获取指定商品的属性列表
	 * @param goodsIdList
	 * @return
	 * @throws Exception
	 */
	List<Map> getGoodsProListByGoodsId(Long goodsId)throws Exception;
	
	/**
	 * 获取商品的属性列表
	 * @param goodsProperty
	 * @return
	 * @throws Exception
	 */
	List<GoodsPropertyDto> getGoodsProperty(GoodsPropertyDto goodsProperty)throws Exception;
	
	/**
	 * 根据属性ID或属性值ID删除商品属性
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int delGoodsProperty(Map param) throws Exception;
	
}
