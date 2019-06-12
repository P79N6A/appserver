/**
 * 
 */
package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.PageModel;

/** 
 * @ClassName: IAttachmentDao 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 上午11:28:15 
 *  
 */
public interface IAttachmentDao {

	/**
	 * 分页查找附件
	* @Title: queryAttachmentByPageModel 
	* @Description: TODO
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return PageModel    返回类型 
	* @throws
	 */
	public PageModel queryAttachmentByPageModel(PageModel pageModel)throws Exception;
	
	
	Attachment queryAttachmentById(Long id)throws Exception;
	
	/**
	 * @Function: com.idcq.appserver.dao.common.IAttachmentDao.saveAttachment
	 * @Description: 保存附件信息
	 *
	 * @param attachment 附件对象
	 * @return 返回保存的个数
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:szp
	 * @date:2015年7月30日 上午11:50:16
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日       szp       v1.0.0         create
	 */
	int saveAttachment(Attachment attachment) throws Exception;
	
	/**
	 * 获取附件地址url列表
	 * @param bizId 业务主键id
	 * @param bizType 类型 商铺=1，用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师10,商品分类11
	 * @param picType 图片类型，缩略图=1，轮播图=2，法人图片
	 * @return List<String> 返回附件地址url
	 * @throws Exception 数据库异常
	 * @author  shengzhipeng
	 */
	List<String> getAttachUrlListByCondition(Long bizId, Integer bizType, Integer picType)throws Exception;
	
	List<Map> getAttachUrlListByCondition(String bizType, String fileName) throws Exception;
	
	List<Attachment> queryAttachmentBybizId(Long bizId) throws Exception;


	public int saveAttachmentByPoster(Attachment attachment);


	public int updateAttachment(Map<String, Object> map);
	
    /**
     * 根据附件ID查询附件地址，或者根据附件表的bizId和bizType获取最新的附件地址
     * @param attachementId
     * @param bizId
     * @param bizType
     * @return
     * @author  shengzhipeng
     * @date  2016年7月23日
     */
    String getAttachmentUrl(Long attachementId, Long bizId, String bizType)throws Exception;
}
