package com.idcq.appserver.dao.storage;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.storage.ShopStorageCheckDetailDto;

public interface IShopStorageCheckDetailDao {

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	Integer batchInsertShopStorageCheckDetail(List<ShopStorageCheckDetailDto> shopStorageCheckDetail) throws Exception;
	/**
	 * 查询盘点流水
	 * 
	 * @param parms
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getShopStorageCheckDetailList(Long storageCheckId,Long goodsId);
}
