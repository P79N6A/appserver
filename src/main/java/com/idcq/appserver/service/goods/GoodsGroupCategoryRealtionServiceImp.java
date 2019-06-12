package com.idcq.appserver.service.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupCategoryRelationDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;
/**
 * 商品族的商品种类业务处理层
 * @ClassName: GoodsGroupCategoryRealtionServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 下午5:38:42 
 *
 */
@Service
public class GoodsGroupCategoryRealtionServiceImp implements IGoodsGroupCategoryRelationService{

		@Autowired
		private IGoodsGroupCategoryRelationDao dao;
		
		/**
		 * 获取商品族的商品种类
		 * @Title: getCategoryListByGroupId 
		 * @param @param goodsGroupId
		 * @param @return
		 * @param @throws Exception  
		 * @throws
		 */
		public List<GoodsGroupCategoryRelationDto>getCategoryListByGroupId(Long goodsGroupId)throws Exception
		{
			return dao.getCategoryListByGroupId(goodsGroupId);
		}
		
}	
