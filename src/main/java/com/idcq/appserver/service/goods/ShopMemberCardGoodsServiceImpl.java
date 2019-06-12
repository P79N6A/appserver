package com.idcq.appserver.service.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.goods.IShopMemberCardGoodsDao;
import com.idcq.appserver.dto.goods.ShopMemberCardGoods;

/**
 * @ClassName: ShopMemberCardGoodsServiceImpl
 * @Description: 次卡限定商品表 业务实现层
 * @author dengjihai
 * @date 2016年2月17日 上午10:05:23
 */
@Service
public class ShopMemberCardGoodsServiceImpl implements IShopMemberCardGoodsService {

	@Autowired
	IShopMemberCardGoodsDao shopMemberCardGoodsDao;

	@Override
	public ShopMemberCardGoods selectByPrimaryKey(Long id) throws Exception {
		return shopMemberCardGoodsDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteByPrimaryKey(Long id) throws Exception {
		return shopMemberCardGoodsDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(ShopMemberCardGoods record) throws Exception {
		return shopMemberCardGoodsDao.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(ShopMemberCardGoods record) throws Exception {
		return shopMemberCardGoodsDao.updateByPrimaryKeySelective(record);
	}

}
