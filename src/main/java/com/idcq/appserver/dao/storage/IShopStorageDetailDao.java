package com.idcq.appserver.dao.storage;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.storage.ShopStorageDetailDto;

public interface IShopStorageDetailDao {

	/**
	 * 批量插入出入库详情
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	Integer batchInsertShopStorageDetail(List<ShopStorageDetailDto> shopStorageDetailDtos) throws Exception;
	/**
	 * 获取出入库变动详情
	 * 
	 * @Description:
	 *
	 * @param bizId
	 * @param bizType
	 * @return
	 */
	List<Map<String, Object>> getShopStorageDetailList(Long bizId,Integer bizType);
}
