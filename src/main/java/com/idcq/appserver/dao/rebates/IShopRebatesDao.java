package com.idcq.appserver.dao.rebates;

import java.util.List;

import com.idcq.appserver.dto.rebates.ShopRebatesDto;
import org.apache.ibatis.session.RowBounds;

public interface IShopRebatesDao {
	
	/**
	 * 查询记录店铺返利
	 * 
	 */
	public List<ShopRebatesDto> getShopRebates(ShopRebatesDto shopRebatesDto) throws Exception;
	/**
	 * 更新记录店铺返利
	 * 
	 */
	public int updateShopRebates(ShopRebatesDto shopRebatesDto) throws Exception;

	/**
	 * 插入返点记录
	 * @param shopRebatesDto
     */
	void insertShopRebates(ShopRebatesDto shopRebatesDto);

	/**
	 * 查询购买V商品等返点的商铺数量
	 * @return
     */
	int countRemainedRebates();

	/**
	 *
	 */
	List<ShopRebatesDto> getToExecuteRebates(RowBounds rowBounds);

}
