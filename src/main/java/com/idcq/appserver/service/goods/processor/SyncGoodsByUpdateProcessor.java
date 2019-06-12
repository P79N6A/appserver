package com.idcq.appserver.service.goods.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.AsynchronousTask.processor.BaseProcessor;
import com.idcq.appserver.dto.goods.SyncGoodsInfoDto;
import com.idcq.appserver.service.goods.IGoodsServcie;

@Service("SyncGoodsByUpdate")
public class SyncGoodsByUpdateProcessor extends BaseProcessor {
	private final static Logger logger = LoggerFactory.getLogger(SyncGoodsByUpdateProcessor.class);
	@Autowired
	public IGoodsServcie goodsService;
	
	@Override
	public void consumerAsynMessage(byte[] body) throws Exception {
		logger.info("总店更新商品后同步到分店start");
		long start=System.currentTimeMillis();
		SyncGoodsInfoDto syncGoodsInfo = getMessageModel(body, SyncGoodsInfoDto.class);
		goodsService.syncGoods(syncGoodsInfo,null);
		logger.info("总店更新商品后同步到分店end   用时：{}ms",System.currentTimeMillis()-start);
	}

}
