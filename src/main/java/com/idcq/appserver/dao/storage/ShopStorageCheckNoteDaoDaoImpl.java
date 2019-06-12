package com.idcq.appserver.dao.storage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.storage.ShopStorageCheckNoteDto;

/**
 * 出入库详单
 * 
 * @author Administrator
 * 
 * @date 2015年4月6日
 * @time 下午5:10:44
 */
@Repository
public class ShopStorageCheckNoteDaoDaoImpl extends BaseDao<ShopStorageCheckNoteDto> implements IShopStorageCheckNoteDao{
	


	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckNoteDao#insertShopStorageCheckNote(com.idcq.appserver.dto.storage.ShopStorageCheckNoteDto)
	 */
	@Override
	public Long insertShopStorageCheckNote(
			ShopStorageCheckNoteDto shopStorageCheckNote) throws Exception {
		
		return (long) super.insert(generateStatement("insertShopStorageCheckNote"),shopStorageCheckNote);	
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckNoteDao#getShopStorageCheckNoteBeseInfo(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getShopStorageCheckNoteBeseInfo(
			Map<String, Object> parms) {
		
		return (List)super.findList(generateStatement("getShopStorageCheckNoteBeseInfo"), parms);
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckNoteDao#getShopStorageCheckNoteBeseInfo()
	 */
	@Override
	public Integer getShopStorageCheckNoteBeseInfoCount(Map<String, Object> parms)throws Exception {
		
		return (Integer) super.selectOne(generateStatement("getShopStorageCheckNoteBeseInfoCount"), parms);
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageCheckNoteDao#queryStorageCheckNoExist(java.lang.Integer)
	 */
	@Override
	public Integer queryStorageCheckNoExist(String storageCheckno)throws Exception {
		
		return (Integer) super.selectOne(generateStatement("queryStorageCheckNoExist"), storageCheckno);

	}

}
