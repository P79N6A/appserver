/**
 * 
 */
package com.idcq.appserver.service.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dto.goods.GoodsSetDto;

/** 
 * @ClassName: GoodsSetServiceImp 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月18日 上午11:21:15 
 *  
 */
@Service
public class GoodsSetServiceImp implements IGoodsSetService {

	@Autowired
	private IGoodsSetDao goodsSetDao;
	
	/** 
	* @Title: getGoodsIdListByGoodsSetId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param goodsSetId
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public List<GoodsSetDto> getGoodsIdListByGoodsSetId(Long goodsSetId)
			throws Exception {
		return goodsSetDao.getGoodsIdListByGoodsSetId(goodsSetId);
	}

	@Override
	public void batchInsertGoodsSet(List<GoodsSetDto> goodsSetDtos) throws Exception{
		goodsSetDao.batchInsertGoodsSet(goodsSetDtos);;
	}

}
