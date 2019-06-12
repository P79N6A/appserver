package com.idcq.appserver.dao.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.storage.ShopStorageCheckDetailDto;
import com.idcq.appserver.dto.storage.ShopStorageDetailDto;

/**
 * 出入库详单
 * 
 * @author Administrator
 * 
 * @date 2015年4月6日
 * @time 下午5:10:44
 */
@Repository
public class ShopStorageCheckDetailDaoImpl extends BaseDao<ShopStorageCheckDetailDto> implements IShopStorageCheckDetailDao{
	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckDetailDao#batchInsertShopStorageCheckDetail(java.util.List)
	 */
	@Override
	public Integer batchInsertShopStorageCheckDetail(
			List<ShopStorageCheckDetailDto> shopStorageCheckDetails)
			throws Exception {
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("shopStorageCheckDetailDtos",shopStorageCheckDetails);
		
		return super.insert(generateStatement("batchInsertShopStorageCheckDetail"),param);	
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckDetailDao#getShopStorageCheckDetailList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getShopStorageCheckDetailList(Long storageCheckId,Long goodsId) {
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("storageCheckId",storageCheckId);
		if(goodsId != null){
			param.put("goodsId",goodsId);
		}
		return (List)super.findList(generateStatement("getShopStorageCheckDetailList"), param);

	}
}
