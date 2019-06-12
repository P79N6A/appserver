package com.idcq.appserver.service.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.goods.IShopTechRefGoodsDao;
import com.idcq.appserver.dto.goods.ShopTechRefGoodsDto;
/**
 * 商品族关联的技师
 * @ClassName: ShopTechRefGoodsServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 下午6:17:05 
 *
 */
@Service
public class ShopTechRefGoodsServiceImp implements IShopTechRefGoodsService{
	
	@Autowired
	private IShopTechRefGoodsDao shopTechRefGoodsDao;
	@Override
	public List<ShopTechRefGoodsDto> queryListByGoodsGroupId(Long groupId)
			throws Exception {
		return shopTechRefGoodsDao.queryListByGoodsGroupId(groupId);
	}

}
