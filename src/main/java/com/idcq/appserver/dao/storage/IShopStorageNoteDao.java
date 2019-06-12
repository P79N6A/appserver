package com.idcq.appserver.dao.storage;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.storage.ShopStorageNoteDto;


public interface IShopStorageNoteDao {
	

	/**
	 * 插入出入库记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer insertShopStorageNote(ShopStorageNoteDto shopStorageNoteDto) throws Exception;
	/**
	 * 更新出入库记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateShopStorageNote(ShopStorageNoteDto shopStorageNoteDto) throws Exception;

	/**
	 * 获取库存变动详情
	 * 
	 * @param parm
	 * @return
	 */
	List<Map<String, Object>> getShopStorageByMap(Map<String, Object> parm);
	/**
	 * 根据流水号查询库存流水
	 * 
	 * @param parm
	 * @return
	 */
	ShopStorageNoteDto getStorageNoByStorageNo(String storageNo);
	/**
	 * 获取库存变动总数
	 * 
	 * @param parm
	 * @return
	 */
	Integer getShopStorageCountByMap(Map<String, Object> parm);
	/**
	 * 获取出入库基本信息
	 * 
	 * @param parm
	 * @return
	 */
	List<Map<String, Object>> getShopStorageNoteBeseInfo(Map<String, Object> parm);
	/**
	 * 获取出入库数量
	 * 
	 * @param parm
	 * @return
	 */
	Integer getShopStorageNoteBeseInfoCount(Map<String, Object> parm);
	/**
	 * 验证出入库单号是否存在
	 * @param storageNo
	 * @param shopId
	 * @return
	 */
	int isStorageNoExist(String storageNo, Long shopId);
}
