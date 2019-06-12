/**
 * 
 */
package com.idcq.appserver.service.commonconf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.commonconf.IShopFeedbackDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.commonconf.ShopFeedBackDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.NumberUtil;

/** 
 * @ClassName: ShopFeedBackServiceImp 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月16日 下午3:04:52 
 *  
 */
@Service
public class ShopFeedBackServiceImp implements IShopFeedBackService{

	@Autowired
	private IShopFeedbackDao shopFeedBackDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
    private IAttachmentRelationDao attachmentRelationDao;
	
	/** 
	* @Title: insertShopFeedBack 
	* @Description: TODO(添加商铺反馈) 
	* @param @param shopFeedBackDto
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public void insertShopFeedBack(ShopFeedBackDto shopFeedBackDto, String attachementIdStr)
			throws Exception {
		ShopDto shopDto = shopDao.getShopById(shopFeedBackDto.getShopId());
		CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		shopFeedBackDao.insertShopFeedback(shopFeedBackDto);
		if (StringUtils.isNotBlank(attachementIdStr)) {
		    //保存图片和反馈信息的关联关系
		    List<AttachmentRelationDto> list = new ArrayList<AttachmentRelationDto>();
		    String[] attachementIds = attachementIdStr.split(CommonConst.COMMA_SEPARATOR);
		    AttachmentRelationDto attachmentRelation = null;
		    int i = 1;
		    for (String attachementId : attachementIds) {
		        attachmentRelation = new AttachmentRelationDto();
		        attachmentRelation.setAttachmentId(NumberUtil.stringToLong(attachementId));
	            attachmentRelation.setBizId(shopFeedBackDto.getFeedbackId());
	            attachmentRelation.setBizType(BizTypeEnum.SHOP_FEEDBACK.getValue());
	            attachmentRelation.setPicType(CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
	            attachmentRelation.setBizIndex(i);
	            i++;
	            list.add(attachmentRelation);
            }
		    if(CollectionUtils.isNotEmpty(list)) {
		        attachmentRelationDao.addAttachmentRelationBatch(list);
		    }
		    
		}
	}

	/**
	 * 查询商铺反馈历史接口 
	 * @Title: getShopFeedbackList 
	 * @param @param shopId
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getShopFeedbackList(Long shopId, PageModel pageModel)
			throws Exception {
		return shopFeedBackDao.getShopFeedBackList(shopId,pageModel);
	}

    @Override
    public ShopFeedBackDto getShopFeedbackInfo(Long feedbackId) throws Exception {
        ShopFeedBackDto shopFeedBackDto = shopFeedBackDao.getShopFeedbackInfo(feedbackId);
        CommonValidUtil.validObjectNull(shopFeedBackDto, CodeConst.CODE_PARAMETER_NOT_EXIST, "反馈信息不存在");
        AttachmentRelationDto dto = new AttachmentRelationDto();
        dto.setBizId(feedbackId);
        dto.setBizType(BizTypeEnum.SHOP_FEEDBACK.getValue());
        dto.setPicType(CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
        List<Map> attachementUrls = null;
        List<AttachmentRelationDto> attachRelations = attachmentRelationDao.findByCondition(dto);
        if (CollectionUtils.isNotEmpty(attachRelations)) {
            attachementUrls = new ArrayList<Map>();
            for (AttachmentRelationDto attachmentRelationDto : attachRelations) {
                String url = attachmentRelationDto.getFileUrl();
                Map<String, String> map = new HashMap<String, String>();
                map.put("attachementUrl", FdfsUtil.getFileProxyPath(url));
                attachementUrls.add(map);
            }
        }
        shopFeedBackDto.setAttachementUrls(attachementUrls);
        return shopFeedBackDto;
    }

}
