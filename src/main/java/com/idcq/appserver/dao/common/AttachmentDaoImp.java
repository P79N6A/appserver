/**
 * 
 */
package com.idcq.appserver.dao.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;

/**
 * @ClassName: AttachmentDaoImp
 * @Description: TODO
 * @author 张鹏程
 * @date 2015年4月21日 上午11:23:04
 * 
 */
@Repository
public class AttachmentDaoImp extends BaseDao<Attachment> implements IAttachmentDao
{

    /**
     * @Title: queryAttachmentByPageModel
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param pageModel
     * @param @return
     * @param @throws Exception
     * @throws
     */
    @Override
    public PageModel queryAttachmentByPageModel(PageModel pageModel) throws Exception
    {
        return super.findPagedList(generateStatement("queryAttachmentByPage"),
                generateStatement("queryAttachmentCount"), pageModel);
    }

    public Attachment queryAttachmentById(Long attachmentId) throws Exception
    {
        return (Attachment) HandleCacheUtil.getEntityCacheByClass(Attachment.class, attachmentId,
                CommonConst.CACH_EX_TIME_ATTACHMENT);
    }

    public int saveAttachment(Attachment attachment) throws Exception
    {
        return super.insert(generateStatement("saveAttachment"), attachment);
    }

    public List<String> getAttachUrlListByCondition(Long bizId, Integer bizType, Integer picType) throws Exception
    {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("bizId", bizId);
        map.put("bizType", bizType);
        map.put("picType", picType);
        
        return this.getSqlSession().selectList(generateStatement("getAttachUrlListByCondition"), map);
    }
    
    public List<Map> getAttachUrlListByCondition(String bizType, String fileName) throws Exception
    {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("bizType", bizType);
        map.put("fileName", fileName);
        
        return this.getSqlSession().selectList(generateStatement("getAttachUrlListByPoster"), map);
    }
    
    public List<Attachment> queryAttachmentBybizId(Long bizId) throws Exception{
    	 Map<String,Object> map = new HashMap<String, Object>();
         map.put("bizId", bizId);
         return this.getSqlSession().selectList(generateStatement("queryAttachmentBybizId"), map);
    }

	@Override
	public int saveAttachmentByPoster(Attachment attachment) {
		return super.insert(generateStatement("saveAttachmentByPoster"), attachment);
		
	}

	@Override
	public int updateAttachment(Map<String, Object> map) {
		return super.update(generateStatement("updateAttachment"),map);		
	}

    @Override
    public String getAttachmentUrl(Long attachementId, Long bizId, String bizType) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("attachementId", attachementId);
        map.put("bizId", bizId);
        map.put("bizType", bizType);
        return super.getSqlSession().selectOne("getAttachmentUrl", map);
    }

}
