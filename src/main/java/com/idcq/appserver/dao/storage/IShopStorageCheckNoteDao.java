package com.idcq.appserver.dao.storage;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.storage.ShopStorageCheckNoteDto;


public interface IShopStorageCheckNoteDao {
	

	/**
	 * 插入出入库记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Long insertShopStorageCheckNote(ShopStorageCheckNoteDto shopStorageCheckNote) throws Exception;
	/**
	 * 查询盘点记录基本信息
	 * 
	 * @param parms
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>>getShopStorageCheckNoteBeseInfo(Map<String, Object> parms) throws Exception;
	/**
	 * 查询盘点记录总数
	 * 
	 * @param parms
	 * @return
	 * @throws Exception
	 */
	public Integer getShopStorageCheckNoteBeseInfoCount(Map<String, Object> parms) throws Exception;

	/**
	 * 查询盘点单号是否存在
	 * 
	 * @param storageCheckno
	 * @return
	 * @throws Exception
	 */
	public Integer queryStorageCheckNoExist(String storageCheckno) throws Exception;
	
}
