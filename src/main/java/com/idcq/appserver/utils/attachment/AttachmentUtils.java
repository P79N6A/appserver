/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.utils.attachment.ASs
 * @description: 附件工具类
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年3月16日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.utils.attachment;

import java.util.List;

import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.utils.BeanFactory;

public class AttachmentUtils
{
    /**
     * 查询附件信息
     * 
     * @Function: com.idcq.appserver.utils.attachment.ASs.getAttachment
     * @Description:
     *
     * @param bizId 对应业务表的主键，比如shop_id,user_id,bank_id,goods_id,goods_group_id,technician_id,商品分类ID,商圈活动ID',
     * @param bizType '业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师=10,商品分类=11,launcher主页图标=12,商圈活动=13,收银机日志=14,商圈活动类型=15,消息中心消息=16',
     * @param picType '图片类型，缩略图=1，轮播图=2',
     * @return 附件列表
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月16日 上午11:09:05
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月16日    ChenYongxin      v1.0.0         create
     */
    public static List<AttachmentRelationDto> getAttachment(Long bizId, Integer bizType, Integer picType) throws Exception {
        IAttachmentRelationDao attachmentRelationDao  = BeanFactory.getBean(IAttachmentRelationDao.class);
        AttachmentRelationDto attachmentRelationDto=new AttachmentRelationDto();
        
        attachmentRelationDto.setBizId(bizId);
        attachmentRelationDto.setBizType(bizType);
        attachmentRelationDto.setPicType(picType);
        
        return attachmentRelationDao.findByCondition(attachmentRelationDto);
    }

}
