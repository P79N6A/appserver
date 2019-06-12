package com.idcq.idianmgr.service.goodsGroup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupPropertyDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
/**
 * 获取商品族属性
 * @ClassName: GoodsGroupPropertyServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年8月1日 上午11:53:08 
 *
 */
@Service
public class GoodsGroupPropertyServiceImp implements IGoodsGroupPropertyService{
	
	@Autowired
	private IGoodsGroupPropertyDao goodsGroupPropertyDao;
	@Override
	public List<GoodsGroupPropertyDto> getGoodsGroupPropertyListByGroupId(
			Long groupId) throws Exception {
		return goodsGroupPropertyDao.getGoodsGroupProperyByGroupId(groupId);
	}
	
}
