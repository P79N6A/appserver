package com.idcq.appserver.dao.common;

import java.util.List;

import com.idcq.appserver.dto.common.AttachmentRelationDto;
/**
 * 附件关联
* @ClassName: IAttachmentRelationDao 
* @Description: TODO
* @author 张鹏程 
* @date 2015年7月14日 下午6:06:57 
*
 */
public interface IAttachmentRelationDao {
	
	/**
	 * 根据条件查找
	* @Title: findByCondition 
	* @param @param dto
	* @param @return
	* @param @throws Exception
	* @return List<AttachmentRelationDto>    返回类型 
	* @throws
	 */
	public List<AttachmentRelationDto> findByCondition(AttachmentRelationDto dto)throws Exception;
	public List<AttachmentRelationDto> findByConditionIn(AttachmentRelationDto dto,List<Long>idList)throws Exception;
	
	List<AttachmentRelationDto> queryAttachmentRelation(AttachmentRelationDto dto) throws Exception;
	int delAttachmentRelation(AttachmentRelationDto dto) throws Exception;
	/**
	 * 批量新增附件信息
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int addAttachmentRelationBatch(List<AttachmentRelationDto> list)throws Exception;
	
	/**
	 * 删除服务附件信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int delAttachmentRelationByGgId(Long id)throws Exception;
	
	/**
	 * 删除指定类型的附件信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int delAttachmentRelationByCondition(Long id,Integer bizType,Integer picType)throws Exception;
	
	/**
	 * 批量删除指定类型的附件信息
	 * @param ids
	 * @param picType
	 * @return
	 * @throws Exception
	 */
	int delAttachmentRelationByConditionBatch(List<Long> ids,Integer bizType,Integer picType)throws Exception;
	
	/**
	 * 条件查找附件ID列表
	 * @param bizId
	 * @param bizType
	 * @param picType
	 * @return
	 * @throws Exception
	 */
	List<Long> getAttachRelatIdListByCondition(Long bizId,Integer bizType, Integer picType)throws Exception;
	
	
	
}
