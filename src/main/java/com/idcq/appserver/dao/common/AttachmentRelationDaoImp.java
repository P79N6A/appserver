package com.idcq.appserver.dao.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;

/**
 * 附件关联操作
* @ClassName: AttachmentRelationDaoImp 
* @Description: TODO
* @author 张鹏程 
* @date 2015年7月14日 下午6:09:07 
*
 */
@Repository
public class AttachmentRelationDaoImp  extends BaseDao<AttachmentRelationDto> implements IAttachmentRelationDao{
	
	/**
	 * 根据条件查找
	 * @Title: findByCondition 
	 * @param @param dto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<AttachmentRelationDto> findByCondition(AttachmentRelationDto dto)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("param", dto);
		return super.findList(generateStatement("findByCondition"),params);
	}
	
	
	/**
	 * 查找缩略图
	 * @Title: findByConditionIn 
	 * @param @param dto
	 * @param @param idList
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<AttachmentRelationDto> findByConditionIn(
			AttachmentRelationDto dto, List<Long> idList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("param", dto);
		params.put("idList", idList);
		return super.findList(generateStatement("findByConditionIn"),params);
	}

	@Override
	public List<AttachmentRelationDto> queryAttachmentRelation(AttachmentRelationDto dto) throws Exception {
		return super.findList(generateStatement("queryAttachmentRelation"),dto);
	}
	public int addAttachmentRelationBatch(List<AttachmentRelationDto> list)
			throws Exception {
		return (int)super.insert("addAttachmentRelationBatch",list);
	}

	public int delAttachmentRelationByGgId(Long id) throws Exception {
		return super.delete("delAttachmentRelationByGgId",id);
	}

	public int delAttachmentRelationByCondition(Long id,Integer bizType,Integer picType) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("bizType", bizType);
		map.put("picType", picType);
		return super.delete("delAttachmentRelationByCondition", map);
	}

	public int delAttachmentRelationByConditionBatch(List<Long> ids,
			Integer bizType, Integer picType) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ids",ids);
		map.put("bizType",bizType);
		map.put("picType",picType);
		return super.delete("delAttachmentRelationByConditionBatch", map);
	}
	
	@Override
	public int delAttachmentRelation(AttachmentRelationDto dto) throws Exception {
		return super.delete("delAttachmentRelationByDto", dto);
	}
	
	public List<Long> getAttachRelatIdListByCondition(Long bizId,
			Integer bizType, Integer picType) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("bizId", bizId);
		map.put("bizType", bizType);
		map.put("picType", picType);
		return super.getSqlSession().selectList("getAttachRelatIdListByCondition",map);
	}

	
	
}
