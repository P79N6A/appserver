package com.idcq.appserver.dao.shopcart;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shopcart.ShopCartItemDto;

/**
 * 购物车商品项dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午3:38:53
 */
@Repository
public class ShopCartItemDaoImpl extends BaseDao<ShopCartItemDto>implements IShopCartItemDao{

	public List<ShopCartItemDto> getItemListByUserId(ShopCartItemDto item,
			int page, int pageSize) throws Exception {
		return super.findList(generateStatement("getItemListByUserId"),item);
	}

	public int getItemListCountByUserId(ShopCartItemDto item) throws Exception {
		return (Integer)super.selectOne(generateStatement("getItemListCountByUserId"),item);
	}

	public int updateCartItem(ShopCartItemDto item) throws Exception {
		return super.update(generateStatement("updateCartItem"),item);
	}

	public int delCartItemsByUserId(Long userId) throws Exception {
		return super.delete(generateStatement("delCartItemsByUserId"),userId);
	}

	public int saveCartItem(ShopCartItemDto item) throws Exception {
		return super.insert(generateStatement("saveCartItem"),item);
	}
	
	
	
	
}
