package com.idcq.appserver.dao.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
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
public class ShopStorageDetailDaoImpl extends BaseDao<ShopStorageDetailDto> implements IShopStorageDetailDao{
	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageDetailDao#batchInsertShopStorageDetail(java.util.List)
	 */
	@Override
	public Integer batchInsertShopStorageDetail(
			List<ShopStorageDetailDto> shopStorageDetailDtos) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("shopStorageDetailDtos",shopStorageDetailDtos);
		return super.insert(generateStatement("batchInsertShopStorageDetail"),param);	
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageDetailDao#getShopStorageDetailByBizId(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getShopStorageDetailList(Long bizId , Integer bizType) {
		
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("bizId", bizId);
		parm.put("bizType", bizType);
		
		return (List)super.findList(generateStatement("getShopStorageDetailList"), parm);
		
	}
}
