package com.idcq.appserver.service.goods.processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.AsynchronousTask.processor.BaseProcessor;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.SyncGoodsDto;
import com.idcq.appserver.dto.goods.SyncGoodsInfoDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.goods.IGoodsServcie;

@Service("SyncGoodsByAudit")
public class SyncGoodsByAuditProcessor extends BaseProcessor {
	private final static Logger logger = LoggerFactory.getLogger(SyncGoodsByAuditProcessor.class);
	@Autowired
	public IGoodsServcie goodsService;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	public IGoodsDao goodsDao;
	@Autowired
	public IGoodsCategoryDao goodsCategoryDao;
	
	@Override
	public void consumerAsynMessage(byte[] body) throws Exception {
		logger.info("分店审核后同步总店商品到分店start");
		long start=System.currentTimeMillis();
		Long branchShopId = getMessageModel(body, Long.class);
		ShopDto branchShop = shopDao.getShopById(branchShopId);
		if (branchShop == null) {
			logger.info("分店不存在，分店shopId:{}", branchShopId);
			return;
		}
		
		Long headShopId = branchShop.getHeadShopId();
		if (headShopId == null) {
			return;
		}
		SyncGoodsInfoDto syncGoodsInfo = new SyncGoodsInfoDto();
		List<SyncGoodsDto> syncGoodsList = new ArrayList<SyncGoodsDto>();
		syncGoodsInfo.setSyncGoodsList(syncGoodsList);
		
		SyncGoodsDto syncGoods = new SyncGoodsDto();
		syncGoods.setShopId(headShopId);
		List<Long> syncGoodsIdList = null;
		
		
		GoodsDto searchGood = new GoodsDto();
		searchGood.setShopId(headShopId);
		syncGoodsIdList = goodsDao.queryGoodsIdList(searchGood);
		
		if (CollectionUtils.isEmpty(syncGoodsIdList)) {
			logger.info("总店无商品可同步    总店shopId:{}", headShopId);
			return;
		}
		syncGoods.setGoodsList(syncGoodsIdList);
		syncGoodsList.add(syncGoods);
		
		
		List<ShopDto> branchShopList = new LinkedList<ShopDto>();
		branchShopList.add(branchShop);
		
		//同步总店所有商品分类
		List<Long> headGoodsCategoryList = goodsCategoryDao.queryGoodsCategoryIdsByShopId(headShopId);
		
		if (CollectionUtils.isNotEmpty(headGoodsCategoryList)) {
			for (Long goodsCategoryId : headGoodsCategoryList) {
				goodsService.addGoodsCategory(goodsCategoryId, branchShopId);
			}
		}
		
		goodsService.syncGoods(syncGoodsInfo, branchShopList);
		
		logger.info("分店审核后同步总店商品到分店end   用时：{}ms",System.currentTimeMillis()-start);
	}

}
