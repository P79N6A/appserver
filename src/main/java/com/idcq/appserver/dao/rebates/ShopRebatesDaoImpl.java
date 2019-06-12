package com.idcq.appserver.dao.rebates;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.rebates.ShopRebatesDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class ShopRebatesDaoImpl extends BaseDao<ShopRebatesDto>implements IShopRebatesDao{
	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.rebates.IShopRebatesDao#getShopRebates(com.idcq.appserver.dto.rebates.ShopRebatesDto)
	 */
	@Override
	public List<ShopRebatesDto> getShopRebates(ShopRebatesDto shopRebatesDto)
			throws Exception {
		
		return (List<ShopRebatesDto>)super.findList("getShopRebates", shopRebatesDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.rebates.IShopRebatesDao#updateShopRebates(com.idcq.appserver.dto.rebates.ShopRebatesDto)
	 */
	@Override
	public int updateShopRebates(ShopRebatesDto shopRebatesDto)
			throws Exception {
		return update("updateShopRebates", shopRebatesDto);
	}

	@Override public void insertShopRebates(ShopRebatesDto shopRebatesDto)
	{
		super.insert("insertShopRebates", shopRebatesDto);
	}

	@Override public int countRemainedRebates()
	{
		return this.getSqlSession().selectOne(generateStatement("countRemainedRebates"));
	}

	@Override public List<ShopRebatesDto> getToExecuteRebates(RowBounds rowBounds)
	{
		return this.getSqlSession().selectList(generateStatement("getToExecuteRebates"), null, rowBounds);
	}
}
