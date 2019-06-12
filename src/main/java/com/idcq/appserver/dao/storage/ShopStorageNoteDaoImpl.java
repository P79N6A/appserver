package com.idcq.appserver.dao.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.storage.ShopStorageNoteDto;

/**
 * 出入库详单
 * 
 * @author Administrator
 * 
 * @date 2015年4月6日
 * @time 下午5:10:44
 */
@Repository
public class ShopStorageNoteDaoImpl extends BaseDao<ShopStorageNoteDto> implements IShopStorageNoteDao{
	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#insertShopStorageNote(com.idcq.appserver.dto.storage.ShopStorageNoteDto)
	 */
	@Override
	public Integer insertShopStorageNote(ShopStorageNoteDto shopStorageNoteDto)
			throws Exception {
		
		return super.insert(generateStatement("insertShopStorageNote"),shopStorageNoteDto);	
	}
	@Override
	public Integer updateShopStorageNote(ShopStorageNoteDto shopStorageNoteDto)
			throws Exception {
		
		return super.update(generateStatement("updateShopStorageNoteByStorageNo"),shopStorageNoteDto);	
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#getShopStorageByMap(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getShopStorageByMap(Map<String, Object> parm) {
		
		return (List)super.findList(generateStatement("getShopStorageByMap"), parm);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#getShopStorageNoteBeseInfo(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getShopStorageNoteBeseInfo(
			Map<String, Object> parm) {
		
		return (List)super.findList(generateStatement("getShopStorageNoteBeseInfo"), parm);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#getShopStorageCountByMap(java.util.Map)
	 */
	@Override
	public Integer getShopStorageCountByMap(Map<String, Object> parm) {
		
		return (Integer)super.selectOne(generateStatement("getShopStorageCountByMap"), parm);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#getShopStorageNoteBeseInfoCount(java.util.Map)
	 */
	@Override
	public Integer getShopStorageNoteBeseInfoCount(Map<String, Object> parm) {
		
		return (Integer)super.selectOne(generateStatement("getShopStorageNoteBeseInfoCount"), parm);
	}

	@Override
	public int isStorageNoExist(String storageNo, Long shopId) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("storageNo", storageNo);
		map.put("shopId", shopId);
		return (Integer)super.selectOne(generateStatement("isStorageNoExist"), map);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.storage.IShopStorageNoteDao#getStorageNoByStorageNo(java.lang.String)
	 */
	@Override
	public ShopStorageNoteDto getStorageNoByStorageNo(String storageNo) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("storageNo", storageNo);
		return (ShopStorageNoteDto) selectOne("getStorageNoByStorageNo", parms);
	}
}
