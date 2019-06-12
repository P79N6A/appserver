package com.idcq.appserver.service.goods;

import org.apache.ibatis.annotations.Param;

import com.idcq.appserver.dto.goods.ShopMemberCardGoods;

/** 
* @ClassName: IShopMemberCardGoodsService 
* @Description: TODO
* @author dengjihai
* @date 2016年2月17日 
*/ 
public interface IShopMemberCardGoodsService {

	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	ShopMemberCardGoods selectByPrimaryKey(@Param("id") Long id) throws Exception;

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	int deleteByPrimaryKey(@Param("id") Long id)  throws Exception;


	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(ShopMemberCardGoods record)  throws Exception;

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(ShopMemberCardGoods record) throws Exception;
	 
}
