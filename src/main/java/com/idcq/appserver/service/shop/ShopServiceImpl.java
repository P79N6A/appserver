package com.idcq.appserver.service.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.bill.IUserXBillDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.column.IColumnDao;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IShopResourceGroupDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.pay.IWithdrawDao;
import com.idcq.appserver.dao.pay.IXorderPayDao;
import com.idcq.appserver.dao.shop.IBookRuleDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopConfigureSettingDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.shop.IShopRsrcDao;
import com.idcq.appserver.dao.shop.IShopRsrcGroupDao;
import com.idcq.appserver.dao.shop.IShopWithDrawDao;
import com.idcq.appserver.dao.shop.IUserShopCommentDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.bill.UserXBillDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.HourResouce;
import com.idcq.appserver.dto.goods.PlaceGoodsDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDetailDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.shop.UserShopCommentDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.wxscan.MD5Util;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;

/**
 * 商铺service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:22:35
 */
@Service
public class ShopServiceImpl implements IShopServcie
{

    private static final Logger logger = Logger.getLogger(ShopServiceImpl.class);

    @Autowired
    public IShopDao shopDao;

    @Autowired
    public IGoodsDao goodsDao;

    @Autowired
    public IShopRsrcGroupDao shopRsrcGroupDao;

    @Autowired
    private IShopResourceGroupDao shopResourceGroupDao;

    @Autowired
    private IShopRsrcDao shopRsrcDao;

    @Autowired
    public IUserDao userDao;

    @Autowired
    public IUserShopCommentDao userShopCommentDao;

    @Autowired
    public IShopDeviceDao shopDeviceDao;

    @Autowired
    private IBookRuleDao bookRuleDao;

    @Autowired
    private IShopBillDao shopBillDao;

    @Autowired
    private IShopAccountDao shopAccountDao;

    @Autowired
    private IAttachmentRelationDao attachmentRelationDao;

    @Autowired
    private IGoodsGroupDao goodsGroupDao;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private IUserCashCouponDao userCouponDao;

    @Autowired
    private IUserAccountDao userAccountDao;

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    public IXorderDao xoderDao;

    @Autowired
    public IPlatformBillDao platformBillDao;

    @Autowired
    public IUserBillDao userBillDao;

    @Autowired
    public IPayDao payDao;

    @Autowired
    public IXorderPayDao xorderPayDao;

    @Autowired
    public ITransactionDao transactionDao;

    @Autowired
    private IUserXBillDao userXBillDao;

    @Autowired
    private IUserCashCouponDao userCashCouponDao;

    @Autowired
    private IOrderServcie orderServcie;

    @Autowired
    private IShopConfigureSettingDao shopSettingDao;

    @Autowired
    private IGoodsCategoryDao goodsCategoryDao;

    @Autowired
    private IOrderLogDao orderLogDao;

    @Autowired
    private IWithdrawDao withDrawDao;
    
    @Autowired
    private IShopWithDrawDao shopWithDrawDao;

    @Autowired
    private IPayServcie payServcie;

    @Autowired
    private IPacketService packetService;
    
    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IPacketDao packetDao;
    
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IShopMemberDao shopMemberDao;

    
    public ShopDto getShopMainOfCacheById(Long shopId) throws Exception
    {
        return this.shopDao.getNormalShopById(shopId);
    }

    public ShopDetailDto getShopDetailById(Long shopId) throws Exception
    {
        CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
        ShopDto shop = new ShopDto();
        shop.setShopId(shopId);
        ShopDto pModel = this.shopDao.getShopExtendByIdAndStatus(shopId, null);
        CommonValidUtil.validObjectNull(pModel, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        ShopDetailDto shopDetail = new ShopDetailDto();
        PropertyUtils.copyProperties(shopDetail, pModel);

        // 设置商铺logo
        List<AttachmentRelationDto> attachmentRelationDtos = getAttachment(pModel.getShopId(),
                CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE,null);
        if (!CollectionUtils.isEmpty(attachmentRelationDtos))
        {
            shopDetail.setShopLogoUrl(FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl()));
        }else if(pModel.getShopLogoId() != null){
        	Attachment attachmemt = attachmentDao.queryAttachmentById(pModel.getShopLogoId().longValue());
        	if(attachmemt != null && attachmemt.getFileUrl() != null){
        		shopDetail.setShopLogoUrl(FdfsUtil.getFileProxyPath(attachmemt.getFileUrl()));
        	}
        }
        //添加推荐人信息
        if(pModel.getReferUserId()!=null){
        	 UserDto userDto1 =userDao.getUserById(pModel.getReferUserId());
        	 if(userDto1!=null){
        		 shopDetail.setReferMobileOrUserId(userDto1.getMobile());
        	 }
        }
        
        // 设置店主名
        shopDetail.setShopkeeper(pModel.getShopManagerName());
        UserDto serverUser =userDao.getUserById(pModel.getShopServerUserId());
        if(serverUser!=null){
        	shopDetail.setShopServerUserId(serverUser.getUserId());
        	shopDetail.setShopServerUserName(serverUser.getUserName());
        	shopDetail.setShopServeUserMobile(serverUser.getMobile());
        }
        

        // 设置商铺环境图，多个以逗号分隔
        attachmentRelationDtos = getAttachment(pModel.getShopId(), CommonConst.BIZ_TYPE_IS_SHOP,
                CommonConst.PIC_TYPE_IS_CYCLE_PLAY,null);
        if (!CollectionUtils.isEmpty(attachmentRelationDtos))
        {
            StringBuffer buffer = new StringBuffer();
            StringBuffer ids = new StringBuffer();
            int i = 0;
            for (AttachmentRelationDto att : attachmentRelationDtos)
            {
                buffer.append(FdfsUtil.getFileProxyPath(att.getFileUrl()));
                ids.append(att.getAttachmentId());
                i++;
                if (i < attachmentRelationDtos.size())
                {
                    buffer.append(",");
                    ids.append(",");
                }
            }
            shopDetail.setShopSettingImgs(buffer.toString());
            shopDetail.setShopSettingIds(ids.toString());
        }
        
     // 设置商铺身份证正反面图以及身份证号，多个以逗号分隔
        UserDto userDto = userDao.getDBUserById(pModel.getPrincipalId());
//        if(pModel.getAuditStatus()!=null){
        	 IColumnDao columnDao=BeanFactory.getBean(IColumnDao.class);
             List<ColumnDto> columnDtoList= columnDao.getMultiColumnByShopId(shopId);
             if(columnDtoList!=null && columnDtoList.size()>0){
                 String subColumnId="";
                 String shopIndustryName=""; 
             	for (int i = 0; i < columnDtoList.size(); i++) {
     				if(i!=columnDtoList.size()-1){
     					subColumnId+=columnDtoList.get(i).getColumnId()+",";
     					shopIndustryName+=columnDtoList.get(i).getColumnName()+",";
     				}else{
     					subColumnId=subColumnId+columnDtoList.get(i).getColumnId();
     					shopIndustryName=shopIndustryName+columnDtoList.get(i).getColumnName();
     				}
     			}
             	shopDetail.setSubColumnId(subColumnId);
             	shopDetail.setShopIndustryName(shopIndustryName);       
             }
             
             if(userDto!=null){
             	if(pModel.getShopManagerIdentityCardPic1()!=null){
             		 Attachment attachment = attachmentDao.queryAttachmentById(Long.valueOf(pModel.getShopManagerIdentityCardPic1()));
             		 if(attachment!=null){
             			 shopDetail.setIdentityCardPicId(attachment.getAttachmentId());
                 		 shopDetail.setIdentityCardPicUrl(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             		 }
             	}
             	if(pModel.getShopManagerIdentityCardPic2()!=null){
     	       		 Attachment attachment = attachmentDao.queryAttachmentById(Long.valueOf(pModel.getShopManagerIdentityCardPic2()));
     	       		 if(attachment!=null){
             			 shopDetail.setIdentityCardNextPicId(attachment.getAttachmentId());
                 		 shopDetail.setIdentityCardNextPicUrl(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             		 }
     	       	}
             	shopDetail.setIdentityCardNo(pModel.getShopManagerIdentityCardNo());
             	
             	// 设置个人技能证书图，多个以逗号分隔
                attachmentRelationDtos = getAttachment(userDto.getUserId(),
                        CommonConst.BIZ_TYPE_IS_USER,CommonConst.PIC_TYPE_IS_PERSONAL_SKILL,null);
                if (!CollectionUtils.isEmpty(attachmentRelationDtos))
                {
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer ids = new StringBuffer();
                    StringBuffer busCertificateNo = new StringBuffer();
                    int i = 0;
                    for (AttachmentRelationDto att : attachmentRelationDtos)
                    {
                        buffer.append(FdfsUtil.getFileProxyPath(att.getFileUrl()));
                        ids.append(att.getAttachmentId());
                        busCertificateNo.append(att.getFileNo());
                        i++;
                        if (i < attachmentRelationDtos.size())
                        {
                            buffer.append(",");
                            ids.append(",");
                            busCertificateNo.append(",");
                        }
                    }
                    shopDetail.setSkillsCertificatePics(buffer.toString());
                    shopDetail.setSkillsCertificatePicIds(ids.toString());
                    shopDetail.setSkillsCertificateNos(busCertificateNo.toString());
                }
             	
             }
             // 设置商铺营业执照照片以及营业执照号
             shopDetail.setBusinessLicenceNo(pModel.getBusinessLicenceNo());
             if(pModel.getBusinessLicensePic()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(Long.parseLong(pModel.getBusinessLicensePic()));
             	// Attachment attachment = attachmentDao.getAttachmentById(Integer.valueOf(pModel.getBusinessLicensePic()));
             	 if(attachment!=null){
             		 shopDetail.setBusinessLicenceId(attachment.getAttachmentId().toString());
             		 shopDetail.setBusinessLicencePicUrl(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             	 }
             }
             //组织机构代码证图片
             if(pModel.getOrganizationCodePicId()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(pModel.getOrganizationCodePicId().longValue());
             	// Attachment attachment = attachmentDao.getAttachmentById(Integer.valueOf(pModel.getBusinessLicensePic()));
             	 if(attachment!=null){
             		 shopDetail.setOrganizationCodePicId(pModel.getOrganizationCodePicId());
             		 shopDetail.setOrganizationCodePic(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             		shopDetail.setOrganizationCode(pModel.getOrganizationCode());
             	 }
             }
             
             //税务登记证图片
             if(pModel.getTaxRegistrationCertificatePicId()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(pModel.getTaxRegistrationCertificatePicId().longValue());
             	// Attachment attachment = attachmentDao.getAttachmentById(Integer.valueOf(pModel.getBusinessLicensePic()));
             	 if(attachment!=null){
             		 shopDetail.setTaxRegistrationCertificatePicId(pModel.getTaxRegistrationCertificatePicId());
             		 shopDetail.setTaxRegistrationCertificatePic(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             		shopDetail.setTaxRegistrationCertificate(pModel.getTaxRegistrationCertificate());
             	 }
             }
             
             //授权书图片
             if(pModel.getAuthorizationPicId()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(pModel.getAuthorizationPicId().longValue());
             	// Attachment attachment = attachmentDao.getAttachmentById(Integer.valueOf(pModel.getBusinessLicensePic()));
             	 if(attachment!=null){
             		 shopDetail.setAuthorizationPicId(pModel.getAuthorizationPicId());
             		 shopDetail.setAuthorizationPic(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             	 }
             }
             
           //法人身份证正面图片
             if(pModel.getPrincipalIdentityCardPicId1()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(pModel.getPrincipalIdentityCardPicId1().longValue());
             	 if(attachment!=null){
             		 shopDetail.setPrincipalIdentityCardPicId1(pModel.getPrincipalIdentityCardPicId1());
             		 shopDetail.setPrincipalIdentityCardPicUrl1(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             	 }
             }
             
           //法人身份证正面图片
             if(pModel.getPrincipalIdentityCardPicId2()!=null){
             	 Attachment attachment = attachmentDao.queryAttachmentById(pModel.getPrincipalIdentityCardPicId2().longValue());
             	 if(attachment!=null){
             		 shopDetail.setPrincipalIdentityCardPicId2(pModel.getPrincipalIdentityCardPicId2());
             		 shopDetail.setPrincipalIdentityCardPicUrl2(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
             	 }
             }
             
          // 设置经营许可证图，多个以逗号分隔
             attachmentRelationDtos = getAttachment(pModel.getShopId(), CommonConst.BIZ_TYPE_IS_SHOP,
                     CommonConst.PIC_TYPE_IS_BUS_LICENCE,CommonConst.PIC_BIZ_INDEX_IS_BUS_CERTIFICATE);
             if (!CollectionUtils.isEmpty(attachmentRelationDtos))
             {
                 StringBuffer buffer = new StringBuffer();
                 StringBuffer ids = new StringBuffer();
                 StringBuffer busCertificateNo = new StringBuffer();
                 int i = 0;
                 for (AttachmentRelationDto att : attachmentRelationDtos)
                 {
                     buffer.append(FdfsUtil.getFileProxyPath(att.getFileUrl()));
                     ids.append(att.getAttachmentId());
                     busCertificateNo.append(att.getFileNo());
                     i++;
                     if (i < attachmentRelationDtos.size())
                     {
                         buffer.append(",");
                         ids.append(",");
                         busCertificateNo.append(",");
                     }
                 }
                 shopDetail.setBusinessCertificatePics(buffer.toString());
                 shopDetail.setBusinessCertificatePicIds(ids.toString());
                 shopDetail.setBusinessCertificates(busCertificateNo.toString());
             }else{
            	 if(pModel.getBusinessCertificatePicIds() != null){
            		 Attachment attachment = attachmentDao.queryAttachmentById(Long.valueOf(pModel.getBusinessCertificatePicIds()));
                 	 if(attachment!=null){
                 		 shopDetail.setBusinessCertificatePicIds(pModel.getBusinessCertificatePicIds());
                 		 shopDetail.setBusinessCertificates(pModel.getBusinessCertificates());
                 		 shopDetail.setBusinessCertificatePics(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
                 	 }
            	 }
             }
             
//        }
        return shopDetail;
    }

    private List<AttachmentRelationDto> getAttachment(Long bizId, Integer bizType, Integer picType, Integer bizIndex) throws Exception
    {
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        attachmentRelationDto.setBizId(bizId);
        attachmentRelationDto.setBizType(bizType);
        attachmentRelationDto.setPicType(picType);
        attachmentRelationDto.setBizIndex(bizIndex);
        return attachmentRelationDao.findByCondition(attachmentRelationDto);
    }

    public PageModel getShopList(ShopDto shop, int page, int pageSize) throws Exception
    {
        List<ShopDto> list = this.shopDao.getList(shop, page, pageSize);
        // if(list != null && list.size() > 0){
        // for(){
        //
        // }
        // }else{
        //
        // }

        PageModel pm = new PageModel();
        pm.setToPage(page);
        pm.setPageSize(pageSize);
        pm.setList(list);
        return pm;
    }

    public PageModel getShopComments(Long shopId, int page, int pageSize) throws Exception
    {
        List<UserShopCommentDto> list = this.userShopCommentDao.getShopComments(shopId, page, pageSize);
        ShopDto shopDto = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        list = updateImgUrl(list, shopDto);
        // 获取总记录数
        PageModel pm = new PageModel();
        pm.setTotalItem(this.userShopCommentDao.getShopCommentsTotal(shopId));
        pm.setToPage(page);
        pm.setPageSize(pageSize);
        pm.setList(list);
        return pm;
    }

    /**
     * 增加图片url绝对路径返回
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<UserShopCommentDto> updateImgUrl(List<UserShopCommentDto> list, ShopDto shop) throws Exception
    {
        List<UserShopCommentDto> newlist = new ArrayList<UserShopCommentDto>();
        Integer columnId = shop.getColumnId();
        if (CollectionUtils.isNotEmpty(list))
        {
            for (UserShopCommentDto shopComment : list)
            {
                String imgBigUrl = shopComment.getImgBig();
                String imgSmallUrl = shopComment.getImgSmall();
                imgBigUrl = FdfsUtil.getFileProxyPath(imgBigUrl);
                imgSmallUrl = FdfsUtil.getFileProxyPath(imgSmallUrl);
                shopComment.setImgBig(imgBigUrl);
                shopComment.setImgSmall(imgSmallUrl);
                // columnid等于13代表商铺类型为便利店
                if (CommonConst.SHOP_TYPE_STORE == columnId)
                {
                    // 与前台约定type等于1代表便利店
                    shopComment.setType(1);
                }
                else
                {
                    // 非便利店2
                    shopComment.setType(2);
                }

                newlist.add(shopComment);
            }
        }
        return newlist;
    }

    public PageModel searchShop(Map param) throws Exception
    {
        List<ShopDto> list = this.shopDao.searchShop(param);
        // 获取总记录数
        PageModel pm = new PageModel();
        pm.setTotalItem(this.shopDao.searchShopTotal(param));
        pm.setToPage(Integer.valueOf((String) param.get("pageNO")));
        pm.setPageSize(Integer.valueOf((String) param.get("pageSize")));
        pm.setList(list);
        return pm;
    }

    public Map getShopXyById(Long shopId) throws Exception
    {
        Map shop = this.shopDao.getShopXyById(shopId);
        if (shop == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        Map pModel = new HashMap();
        pModel.put("longitude", shop.get("longitude"));
        pModel.put("latitude", shop.get("latitude"));
        return pModel;
    }

    public Map<String, Integer> praise(String zanType, String bizId, String userId) throws Exception
    {

        UserDto userDB = this.userDao.getUserById(Long.valueOf(userId));
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        // 暂时不限制重复点赞
        // String cacheUserId = DataCacheApi.get(CommonConst.KEY_USER_PRAISE +
        // userId);
        // if (cacheUserId != null && cacheUserId.equals(userId)){
        // throw new ValidateException(CodeConst.CODE_PRAISE_REPEAT,"不允许重复点赞");
        // }
        //
        Map<String, Integer> map = new HashMap<String, Integer>();
        Integer zanNumber = 0;
        int updateNum = 0;
        if (("1").equals(zanType))
        {

            // 为商品点赞
            Long goodsId = Long.valueOf(bizId);
            updateNum = this.goodsDao.updateGoodsZan(goodsId);
            this.goodsGroupDao.updateGoodsGroupZan(goodsId);
            if (updateNum == 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
            }
            zanNumber = this.goodsDao.getGoodsZanById(goodsId);

            goodsDao.addGoodsZanLog(userDB.getUserId(), goodsId, 1);
        }
        else if (("2").equals(zanType))
        {

            // 为商铺点赞
            Long shopId = Long.valueOf(bizId);

            // 更新点赞次数
            updateNum = this.shopDao.updateShopZan(shopId);
            if (updateNum == 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }
            zanNumber = this.shopDao.getShopZanById(shopId);
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ZANTYPE);
        }
        map.put("zanNumber", zanNumber);
        // DataCacheApi.setex(CommonConst.KEY_USER_PRAISE + userId, userId,
        // CommonConst.CACHE_USER_OUT_TIME);
        return map;
    }

    /**
     * 根据id列表批量查询商铺信息
     */
    public List<ShopDto> getShopListByIds(List<Long> shopIdList)
    {
        return shopDao.getListByShopIds(shopIdList);
    }

    /**
     * 分页查找
     */
    public PageModel getShopByPage(PageModel pageModel, int... queryTotalCount)
    {
        return shopDao.getShopPageModel(pageModel, new ShopDto(), queryTotalCount);
    }

    /**
     * 查询商铺资源动态接口
     */
    public List<Map> queryShopResourceSnapshot(Long shopId) throws Exception
    {
        List<Map> resultList = new ArrayList<>();// 结果集合
        List<Map> resources = null;
        Map shopResourceGroupMap = null;
        ShopDto shop = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        // 商铺资源分组信息
        List<Map> shopResourceList = shopRsrcDao.getShopGroupResourceList(shopId);
        Map map = null;
        String resourceType = null;
        List<Map> list = null;
        shopResourceGroupMap = new HashMap();
        if (null != shopResourceList && shopResourceList.size() > 0)
        {
            for (int i = 0, len = shopResourceList.size(); i < len; i++)
            {
                map = shopResourceList.get(i);
                resourceType = (String) map.get("resourceType");
                if (shopResourceGroupMap.get("resourceType") == null)
                {
                    shopResourceGroupMap.put("resourceType", resourceType);
                    list = new ArrayList<Map>();
                    map.remove("resourceType");
                    list.add(map);
                }
                else
                {
                    if (resourceType.equals(shopResourceGroupMap.get("resourceType")))
                    {
                        map.remove("resourceType");
                        list.add(map);
                    }
                    else
                    {
                        shopResourceGroupMap.put("resources", list);
                        resultList.add(shopResourceGroupMap);
                        shopResourceGroupMap = new HashMap();
                        list = new ArrayList<Map>();
                        map.remove("resourceType");
                        list.add(map);
                        shopResourceGroupMap.put("resourceType", resourceType);
                    }
                }
            }
            shopResourceGroupMap.put("resources", list);
            resultList.add(shopResourceGroupMap);
        }
        return resultList;
    }

    /**
     * 根据商铺编号找到商铺的负责人
     */
    public Long getUserIdByShopId(Long shopId) throws Exception
    {
        return shopDao.getUserIdByShopIed(shopId);
    }

    public int queryShopExists(Long shopId) throws Exception
    {
        return this.shopDao.queryNormalShopExists(shopId);
    }

    public int queryShopEmplExists(Long shopId, Long employeeId) throws Exception
    {
        return this.shopDao.queryShopEmplExists(shopId, employeeId);
    }

    /**
     * 根据商铺编号查找商铺的基本信息
     * @Title: getShopEssentialInfoById
     * @param @param shopId
     * @param @return
     * @throws
     */
    @Override
    public ShopDto getShopEssentialInfoById(Long shopId) throws Exception
    {
        return shopDao.getShopEssentialInfo(shopId);
    }

    @Override
    public PageModel getShopBooktFeeSetting(Long shopId, Integer pSize, Integer pNo) throws Exception
    {
        // 总记录数
        Integer count = bookRuleDao.getShopBooktFeeSettingCount(shopId);
        List<Map<String, Object>> bookRuleList = this.bookRuleDao.getShopBooktFeeSetting(shopId, pSize, pNo);
        PageModel pm = new PageModel();
        pm.setList(bookRuleList);
        pm.setTotalItem(count);
        return pm;
    }

    /**
     * 根据id查找商铺信息
     * @Title: getShopById
     * @param @param shopId
     * @param @return
     * @param @throws Exception
     * @throws
     */
    public ShopDto getShopById(Long shopId) throws Exception
    {
        CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
        ShopDto pModel = this.shopDao.getShopById(shopId);
        return pModel;
    }

    public ShopDto getShopExtendByIdAndStatus(Long shopId, Integer shopStatus) throws Exception
    {
        ShopDto pModel = this.shopDao.getShopExtendByIdAndStatus(shopId, shopStatus);
        return pModel;
    }

    public Map<String, Object> getShopAccountMoney(Map<String, Object> parms) throws Exception
    {
        return shopAccountDao.getShopAccountMoney(parms);
    }

    @Override
    public void giveShopDeposit(Long shopId, Double depositAmount) throws Exception
    {
        shopDao.giveShopDeposit(shopId, depositAmount);
    }

    @Override
    public PageModel getShopBill(Map<String, Object> map) throws Exception
    {
        PageModel pageMode = new PageModel();
        List<Map<String, Object>> list = shopBillDao.getShopBill(map);
        Integer count = shopBillDao.getShopBillCount(map);
        pageMode.setList(list);
        pageMode.setTotalItem(count);
        return pageMode;
    }

    @Override
    public PageModel getShopAward(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        // type===>0：全部，1：会员，2：店铺，默认0
        // 商铺数据
        Integer shopCount = shopAccountDao.getShopAwardCount(map);
        List<Map<String, Object>> shopList = shopAccountDao.getShopAwardList(map);
        pageModel.setList(shopList);
        pageModel.setTotalItem(shopCount);
        return pageModel;
    }

    @Override
    public Map<String, Object> getShopAwardTotal(Map<String, Object> map) throws Exception
    {
        // type===>0：全部，1：会员，2：店铺，默认0
        Map<String, Object> resultMap = shopAccountDao.getShopAwardTotal(map);
        resultMap = updateResultMap(resultMap);
        return resultMap;
    }

    public Map<String, Object> updateResultMap(Map<String, Object> resultMap)
    {
        if (resultMap != null && resultMap.size() != 0)
        {
            BigDecimal resultMoney = (BigDecimal) resultMap.get("recommandAward");
            BigDecimal setScale = resultMoney.setScale(2, resultMoney.ROUND_DOWN);
            resultMap.put("recommandAward", setScale);
        }
        return resultMap;
    }

    @Override
    public String getShopPasswordById(Long shopId) throws Exception
    {
        return shopDao.getShopPasswordById(shopId);
    }

    @Override
    public Map<String, Object> queryShopOrderCount(Map<String, Object> pMap) throws Exception
    {
        /*
         * Map<String, Object> orderMap = new HashMap<String,Object>();
         * Map<String, Object> payMap = new HashMap<String,Object>();
         * //查询订单各种状态的总数 orderMap = shopDao.getShopOrderCount(paramMap);
         * if(orderMap!=null){ //订单状态为3,5的状态 Integer totalOrder =
         * shopDao.getOrderTotalCount(paramMap); orderMap.put("totalOrder",
         * totalOrder); //查询订单支付金额 payMap =
         * shopDao.getOrderAmountCount(paramMap); if(null!=payMap){
         * orderMap.putAll(payMap); } }
         */
        Map<String, Object> resultMap = shopDao.shopOrderCount(pMap);
        return resultMap;
    }

    @Override
    public PageModel getBizLogo(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = shopDao.getBizLogoCount(map);
        if (count != null && 0 != count)
        {
            List<Map<String, Object>> list = shopDao.getBizLogo(map);
            list = updateLogoUrl(list);
            pageModel.setTotalItem(count);
            pageModel.setList(list);
        }

        return pageModel;
    }

    /**
     * 增加图片url绝对路径返回
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> updateLogoUrl(List<Map<String, Object>> list) throws Exception
    {
        List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(list))
        {
            if (CollectionUtils.isNotEmpty(list))
            {
                for (Map<String, Object> map : list)
                {
                    String imgUrl = (String) map.get("fileUrl");
                    imgUrl = FdfsUtil.getFileProxyPath(imgUrl);
                    map.put("fileUrl", imgUrl);
                    newlist.add(map);
                }
            }
        }
        return newlist;
    }

    @Override
    public List<PlaceGoodsDto> getPlaceGoods(Map<String, Object> paramMap) throws Exception
    {
        List<PlaceGoodsDto> list = new ArrayList<PlaceGoodsDto>();
        // 1.获取原始的预定资源(商品)列表
        List<Map<String, Object>> results = getPreResources(paramMap);
        for (Map<String, Object> map : results)
        {
            // 2.拆分每个预定资源(商品)的时间段(按1小时长度)，并检测是否被预定
            List<HourResouce> hourResouces = splitPreTime(paramMap, map);

            // TODO 如果要过滤当前时间以前的时间段，可再这里加代码对hourResources操作

            for (HourResouce hourResouce : hourResouces)
            {
                // 3.构造按每小时拆分的预定资源信息
                list.add(buildPlaceGoods(map, hourResouce));
            }

        }
        return list;
    }

    private PlaceGoodsDto buildPlaceGoods(Map<String, Object> map, HourResouce hourResouce)
    {
        PlaceGoodsDto placeGoodsDto = new PlaceGoodsDto();
        placeGoodsDto.setGroupGoodsId((Long) map.get("groupGoodsId") + "");
        placeGoodsDto.setGoodsId((Long) map.get("goodsId") + "");
        placeGoodsDto.setPrice((BigDecimal) map.get("price") + "");
        placeGoodsDto.setFromTime(hourResouce.getBeginTime());
        placeGoodsDto.setToTime(hourResouce.getEndTime());
        placeGoodsDto.setStatus(hourResouce.getStatus());
        return placeGoodsDto;
    }

    // 按每小时拆分预定时间段并判断是否被占用
    private List<HourResouce> splitPreTime(Map<String, Object> paramMap, Map<String, Object> map) throws Exception
    {

        // proValue值格式：2#08:00-12:00
        String proValue = (String) map.get("proValue");
        logger.info("获取商品族属性值proValue=" + proValue);
        String[] dateTime = proValue.split("#")[1].split("-");

        // 设置资源开始和结束时间
        logger.info("获得资源开始时间：" + dateTime[0] + " 结束时间：" + dateTime[1]);
        logger.info("按1个小时长度开始拆分...");
        int beginIndex = Integer.parseInt(dateTime[0].split(":")[0]);
        int endIndex = Integer.parseInt(dateTime[1].split(":")[0]);
        List<HourResouce> hourResouces = new ArrayList<HourResouce>();
        HourResouce hr = null;
        // 按1个小时拆分
        for (int i = beginIndex; i < endIndex; i++)
        {
            hr = new HourResouce();
            hr.setBegin(i);
            hr.setEnd(i + 1);
            paramMap.put("beginTime", hr.getBeginTime());
            paramMap.put("endTime", hr.getEndTime());
            // 判断资源是否被占
            if (shopDao.isUsedResource(paramMap))
            {
                hr.setStatus(2); // 资源状态,枚举值：1-可用，2-不可用
            }
            hourResouces.add(hr);
        }
        return hourResouces;
    }

    // 获取原始的预定资源(商品)列表
    private List<Map<String, Object>> getPreResources(Map<String, Object> paramMap) throws Exception
    {
        // 取出原来的查询日期
        String dateFromApp = (String) paramMap.get("resourceDate");
        int week = DateUtils.getDayOfWeek(dateFromApp);

        paramMap.put("resourceDate", week + "#");
        List<Map<String, Object>> results = shopDao.getPlaceGoods(paramMap);
        logger.info("---------------------获取预定资源列表成功" + results);
        // 重新把日期设置回去提供给判断资源是否被预定使用
        paramMap.put("resourceDate", dateFromApp);
        return results;
    }

    public Integer getShopConfirmMinute(Long shopId) throws Exception
    {
        return this.shopDao.getShopConfirmMinute(shopId);
    }

    public Integer getIsHomeService(Long shopId) throws Exception
    {
        return this.shopDao.getIsHomeService(shopId);
    }

    public Integer getBookFlag(Long shopId) throws Exception
    {
        return this.shopDao.getBookFlag(shopId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#bookSwitch(java.util.Map)
     */
    @Override
    public Integer bookSwitch(Map<String, Object> mapParams) throws Exception
    {
        Integer updateFlge = shopDao.bookSwitch(mapParams);
        // 清除缓存
        DataCacheApi.del(CommonConst.KEY_SHOP + mapParams.get("shopId"));
        return updateFlge;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#getAccountingStat(java.util
     * .Map)
     */
    @Override
    public PageModel getAccountingStat(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = shopDao.getAccountingStatCount(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = shopDao.getAccountingStat(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#getOrderList(java.util.Map)
     */
    @Override
    public PageModel getOrderList(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = shopDao.getOrderListCount(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = shopDao.getOrderList(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#queryShopServerList(java
     * .util.Map)
     */
    @Override
    public List<Map<String, Object>> queryShopServerList(Map<String, Object> map) throws Exception
    {
        return shopDao.queryShopServerList(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#opeShopEmployeeInfo(com.
     * idcq.appserver.dto.shop.ShopEmployeeDto)
     */
    @Override
    public Map opeShopEmployeeInfo(ShopEmployeeDto shopEmployeeDto) throws Exception
    {
        Map map = new HashMap();
        Long employeeId = 0L;
        Integer operateType = shopEmployeeDto.getOperateType();
        // 删除
        if (2 == operateType)
        {
            shopDao.deleteEmployee(shopEmployeeDto);
            
            employeeId = shopEmployeeDto.getEmployeeId();
        }
        else
        {
            employeeId = shopDao.queryEmployeeByCodeAndShopId(shopEmployeeDto);
            //增加
            if (operateType == 0 )
            {
                // 给数据库必填字段默认值
                // 正常
                shopEmployeeDto.setStatus(1);
                // 初始密码123456
                shopEmployeeDto.setPassword(MD5Util.getMD5Str("123456"));
                // 默认性别：未知
                shopEmployeeDto.setSex(2);
                // 技能说明
                shopEmployeeDto.setSkill("");
                // 增加
                shopDao.insertEmployee(shopEmployeeDto);
                
                employeeId = shopEmployeeDto.getEmployeeId();
            }
            else
            {
                shopDao.updateEmployee(shopEmployeeDto);
            }
        }
        map.put("employeeId", employeeId);
        return map;
    }

    /**
     * 判断店铺员工编号是否存在
     * 
     * @Function: 
     *            com.idcq.appserver.service.shop.ShopServiceImpl.queryEmpIsExist
     * @Description:
     * 
     * @param shopEmployeeDto
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @throws Exception
     * @date:2015年9月22日 上午10:38:01
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月22日 ChenYongxin v1.0.0 create
     */
    private Boolean queryEmpIsExist(ShopEmployeeDto shopEmployeeDto) throws Exception
    {
        Long employeeId = shopDao.queryEmployeeByCodeAndShopId(shopEmployeeDto);
        if (employeeId != null && employeeId != 0)
        {
            throw new ValidateException(CodeConst.CODE_FINAL_STATUS_ERROR, "店铺员工编号重复");
        }
        else
        {
            return false;
        }
    }

    public Map handleShopBackOrder(OrderDto order, String payType, String veriCode, String isUseRedPacket)
            throws Exception
    {
        // 校验店铺信息
        Map<String, Object> map = new HashMap<String, Object>();// 返回结果-一点管家增加订单编号返回-pchzhang
        Long shopId = order.getShopId();
        Map result = checkShopInfo(shopId, order.getCashierUsername());
        ShopDto shop = (ShopDto) result.get("shop");
        Long cashierId = (Long) result.get("cashierId");
        order.setCashierId(cashierId);
        String mobile = order.getMobile();
        // 判断是否为会员
        UserDto userDto = userDao.getUserByMobile(mobile);
        // 31位订单
        String orderId = FieldGenerateUtil.generateOrderId(shopId, 6);
        order.setOrderId(orderId);
        map.put("orderId", orderId);
        Date date = new Date();
        Double onLinePayment = 0D;
        Double cashCouponPayment = 0D;
        Double cashPayment = 0D;
        Double redPacketPayment = 0D;
        Double settlePrice = order.getSettlePrice();
        // 根据支付方式校验判断是否校验验证码
        if ("1".equals(payType))
        {
            cashPayment = settlePrice;
            // 现金支付
            if (userDto == null)
            {
                // 非会员订单
                order.setIsMember(CommonConst.USER_IS_NOT_MEMBER);
                this.orderDao.saveOrder(order);
                dealXorderPay(shopId, settlePrice, orderId);
                orderServcie.handleAccountingStatByUser(order);
                orderServcie.updateGoodsAndShopSoldNum(orderId);
    	        //插入反结账订单商品线上支付账单
    	        payServcie.insertReverseShopBill(order);
                map.put("orderType", 2);// 非会员订单

            }
            else
            {
                // 会员订单 先保存订单信息，在保存支付信息，最后触发结账
                order.setIsMember(CommonConst.USER_IS_MEMBER);
                order.setOrderRealSettlePrice(settlePrice);
                order.setUserId(userDto.getUserId());
                orderDao.saveOrder(order);
                PayDto payDto = installPayDto(shopId, settlePrice, orderId, CommonConst.PAY_TYPE_CASH,
                        CommonConst.PAYEE_TYPE_SHOP, null, date, userDto.getUserId());
                if(payDto.getPayAmount()>0){
                    this.payDao.addOrderPay(payDto);
                }
                // 结算
                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, CommonConst.PAY_TYPE_SINGLE);
                map.put("orderType", 1);// 会员订单
            }
        }
        else if ("2".equals(payType))
        {
            CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_MEMBER_NOT_SMS_PAY, "该用户不支付短信支付");
            Long userId = userDto.getUserId();
            String orderTitle = order.getOrderTitle();
            map.put("orderType", 1);// 会员订单
            CommonValidUtil.validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
            // 短信支付
            boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode, true);
            if (!flag)
            {
                // 验证不通过
                throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
            }
            order.setIsMember(CommonConst.USER_IS_MEMBER);
            order.setOrderRealSettlePrice(settlePrice);
            order.setUserId(userDto.getUserId());
            orderDao.saveOrder(order);

            // 店铺名称
            String shopName = shop.getShopName();
            Integer userType = userDto.getUserType();
            // 查询传奇宝账户
            UserAccountDto account = userAccountDao.getAccountMoney(userId);
            // 消费卡余额
            Double couponBalance = userCashCouponDao.getUserCashCouponBalance(userId);
            // 剩余支付金额
            Double residualAmount = settlePrice;
            if (!"0".equals(isUseRedPacket))
            {
                redPacketPayment = payServcie.payByRedPacket(residualAmount, order, userId, true,null);

                // 剩余金额
                residualAmount = NumberUtil.sub(residualAmount, redPacketPayment);
            }
            if (couponBalance >= residualAmount)
            {
                // 消费卡足额支付
                cashCouponPayment = payCashCoupon(shopId, order, date, userId, residualAmount);// 获取支付了的金额
            }
            else
            {
                // 先扣消费卡,扣完
                cashCouponPayment = payCashCoupon(shopId, order, date, userId, couponBalance); // 获取支付了的金额
                residualAmount = NumberUtil.sub(residualAmount, cashCouponPayment); // 剩余支付金额

                // 再扣传奇宝，传奇宝余额
                Double onlineBalance = 0D;
                // 传奇宝账户正常
                if (null != account && CommonConst.ACCOUNT_NORMAL_STATUS == account.getAccountStatus())
                {
                    onlineBalance = NumberUtil.add(account.getRewardAmount(), account.getCouponAmount());
                }
                if (onlineBalance != 0 && residualAmount > 0)
                {
                    if (onlineBalance >= residualAmount)
                    {
                        onlinePay(shopId, mobile, residualAmount, shopName, orderId, orderTitle, date, userId,
                                userType, account);
                        onLinePayment = residualAmount;
                        residualAmount = 0D;
                    }
                    else
                    {
                        onlinePay(shopId, mobile, onlineBalance, shopName, orderId, orderTitle, date, userId, userType,
                                account);
                        onLinePayment = onlineBalance;
                        residualAmount = NumberUtil.sub(residualAmount, onlineBalance);

                    }
                }
                cashPayment = residualAmount;// 现金支付的金额
                if (cashPayment > 0)
                {
                    PayDto payDto = installPayDto(shopId, cashPayment, orderId, CommonConst.PAY_TYPE_CASH,
                            CommonConst.PAYEE_TYPE_SHOP, null, date, userId);
                    this.payDao.addOrderPay(payDto);
                }
            }
            if (cashCouponPayment != 0D)
            {
                insertPlatformBill(mobile, cashCouponPayment, orderId, userId, CommonConst.PLT_BILL_MNY_SOURCE_XFK);
            }
            Double sendMoney = packetService.sendRedPacketToUser(order);
            if (sendMoney != 0)
            {
                // 更新订单结算价格
                Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
                if (orderRealSettlePrice < 0)
                {
                    orderRealSettlePrice = 0D;
                }
                orderDao.updateOrderRealSettlePrice(orderId, orderRealSettlePrice, sendMoney);
                map.put("sendRedPacketMoney", sendMoney);
            }
            // 结算
            OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, CommonConst.PAY_TYPE_SINGLE);
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "payType格式不对");
        }
        // 保存订单日志
        saveOrderLong(orderId, date);
        orderServcie.updateGoodsAndShopSoldNum(orderId);
        map.put("onLinePayment", onLinePayment);
        map.put("cashCouponPayment", cashCouponPayment);
        map.put("cashPayment", cashPayment);
        map.put("usedRedPacketMoney", redPacketPayment);

        logger.info("传奇宝消费：" + onLinePayment + "消费卡消费" + cashCouponPayment + "现金消费" + cashPayment + "红包消费"
                + redPacketPayment);
        return map;
    }

    private void saveOrderLong(String orderId, Date date) throws Exception
    {
        OrderLogDto orderLog = new OrderLogDto();
        orderLog.setOrderId(orderId);
        orderLog.setLastUpdateTime(date);
        orderLog.setOrderStatus(CommonConst.ORDER_STS_YJZ);
        orderLog.setPayStatus(CommonConst.PAY_STATUS_PAYED);
        orderLog.setRemark("后台直接收银");
        orderLog.setUserId(0L);// 系统处理
        orderLogDao.saveOrderLog(orderLog);
    }

    private Double payCashCoupon(Long shopId, OrderDto order, Date date, Long userId, Double residualAmount)
            throws Exception
    {
        Double cashCouponPayment = 0D;
        if (residualAmount <= 0)
        {
            return cashCouponPayment;
        }
        List<UserCashCouponDto> userCashCoupons = userCashCouponDao.getUserCashCouponByUserId(userId);
        for (UserCashCouponDto userCashCouponDto : userCashCoupons)
        {
            Double price = userCashCouponDto.getPrice(); // 代金券面额
            Double usedPrice = userCashCouponDto.getUsedPrice(); // 已使用金额
            Double usableBalance = NumberUtil.sub(price, usedPrice); // 单张代金券余额
            Long uccId = userCashCouponDto.getUccId();
            if (usableBalance >= residualAmount)
            {
                // 一张够用
                logger.info("这张代金券足够支付，代金券ID:" + uccId);
                useCashCoupon(order, shopId, userId, date, residualAmount, usedPrice, usableBalance, uccId);
                cashCouponPayment = NumberUtil.add(cashCouponPayment, residualAmount);
                break;
            }
            else
            {
                // 一张不够用
                logger.info("这张代金券不够支付，需要多次支付，每次支付代金券ID:" + uccId);
                useCashCoupon(order, shopId, userId, date, usableBalance, usedPrice, usableBalance, uccId);
                residualAmount = NumberUtil.sub(residualAmount, usableBalance);
                cashCouponPayment = NumberUtil.add(cashCouponPayment, usableBalance);
            }

        }
        return cashCouponPayment;
    }

    /**
     * 
     * 处理非会员订单，包含下单和支付流程
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.dealXorderPay
     * @Description:
     * 
     * @param shopId 店铺Id
     * @param settlePrice 订单结算价
     * @param orderId 订单编号
     * @throws Exception
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月14日 下午5:05:34
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月14日 shengzhipeng v1.0.0 create
     */
    private void dealXorderPay(Long shopId, Double settlePrice, String orderId) throws Exception
    {
    	if(settlePrice>0){
            PayDto payDto = new PayDto();
            payDto.setOrderId(orderId);
            payDto.setPayType(CommonConst.PAY_TYPE_CASH);
            payDto.setPayAmount(settlePrice);
            payDto.setOrderPayType(0);
            payDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
            payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP); // 收款人类型
            payDto.setShopId(shopId);
            this.payDao.addOrderPay(payDto);
    	}
    }

    /**
     * 
     * 传奇宝支付流程，包括记录交易流水、支付记录、更新账余额、记录用户账单
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.onlinePay
     * @Description:
     * 
     * @param shopId 店铺id
     * @param mobile 会员手机号码
     * @param settlePrice 传奇宝支付金额
     * @param shop 店铺实体
     * @param orderId 订单id
     * @param date 下单日期
     * @param userId 会员Id
     * @param account 会员账户
     * @throws Exception
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月14日 下午5:07:27
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月14日 shengzhipeng v1.0.0 create
     */
    private void onlinePay(Long shopId, String mobile, Double settlePrice, String shopName, String orderId,
            String orderTitle, Date date, Long userId, Integer userType, UserAccountDto account) throws Exception
    {
        // 交易流水
        TransactionDto transaction = getTransactionDto(userId, orderId, settlePrice, DateUtils.format(date, null)); // 传奇宝交易流水
        transactionDao.addTransaction(transaction);

        Double couponPayAmount = 0D;// 消费金支付金额
        Double rewardPayAmount = 0D;// 奖励支付金额
        Double couponAmount = account.getCouponAmount(); // 消费金余额
        if (couponAmount >= settlePrice)
        {
            couponPayAmount = settlePrice;
        }
        else
        {
            couponPayAmount = couponAmount;
            rewardPayAmount = NumberUtil.sub(settlePrice, couponPayAmount);
            PayDto payDto = installPayDto(shopId, rewardPayAmount, orderId, CommonConst.PAY_TYPE_REWARD,
                    CommonConst.PAYEE_TYPE_PLATFORM, transaction.getTransactionId(), date, userId);
            if(payDto.getPayAmount()>0){
                this.payDao.addOrderPay(payDto);
            }        
         		}
        PayDto payDto = installPayDto(shopId, couponPayAmount, orderId, CommonConst.PAY_TYPE_CONSUM,
                CommonConst.PAYEE_TYPE_PLATFORM, transaction.getTransactionId(), date, userId);
        UserBillDto userBillDto = new UserBillDto();
        userBillDto.setUserRole(NumberUtil.getStr(userType));
        userBillDto.setBillType("消费");
        userBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
        userBillDto.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());
        userBillDto.setOrderId(payDto.getOrderId());
        userBillDto.setCreateTime(new Date());
        userBillDto.setBillDesc("短信支付消费");
        userBillDto.setUserId(userId);
        userBillDto.setConsumerUserId(userId);
        userBillDto.setOrderPayType(payDto.getOrderPayType());
        userBillDto.setBillStatusFlag(0); // 已完成
        userBillDto.setConsumerMobile(mobile);
        userBillDto.setBillTitle(orderTitle != null ? orderTitle : "消费" + settlePrice);
        userBillDto.setTransactionId(transaction.getTransactionId());
        userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
        // 可见
        userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
        if (couponPayAmount > 0)
        {
            this.payDao.addOrderPay(payDto);
            // 记录用户消费金账单
            buildUserBill(userBillDto, -couponPayAmount, NumberUtil.sub(couponAmount, couponPayAmount),
                    account.getAmount(), CommonConst.USER_ACCOUNT_TYPE_MONETARY);
            userBillDao.insertUserBill(userBillDto);
        }
        // 更新账户余额
        userAccountDao.updateUserAccount(userId, -settlePrice, -rewardPayAmount, null, -couponPayAmount,
        		null,null,null,null,null,null,null,null,null,null);
        if (rewardPayAmount > 0)
        {
            // 消费金不够支付，需要使用到平台奖励，需要记录平台奖励账单
            buildUserBill(userBillDto, -rewardPayAmount, NumberUtil.sub(account.getRewardAmount(), rewardPayAmount),
                    NumberUtil.sub(account.getAmount(), couponPayAmount), CommonConst.USER_ACCOUNT_TYPE_REWARD);

            userBillDao.insertUserBill(userBillDto);
        }

        // 线上收到的那部分金额需要记录平台账单
        insertPlatformBill(mobile, settlePrice, orderId, userId, CommonConst.PLT_BILL_MNY_SOURCE_CQB);
    }

    private void insertPlatformBill(String mobile, Double settlePrice, String orderId, Long userId, int moneySoure)
            throws Exception
    {
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillType("会员消费");
        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBillDto.setOrderId(orderId);
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setConsumerUserId(userId);
        platformBillDto.setConsumerMobile(mobile);
        platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
        platformBillDto.setMoney(settlePrice);
        platformBillDto.setBillDesc("会员消费使用线上支付金额");
        platformBillDto.setMoneySource(moneySoure);
        platformBillDao.insertPlatformBill(platformBillDto);
    }

    /**
     * 构建userbill
     * @param payDto 支付实体
     * @param order 订单实体
     * @param userAccount 会员账户实体
     * @param billType 账单类型（消费，充值等）
     * @param billDirection '账单类型:1（账户资金增加）,-1（账户资金减少）'
     * @return
     */
    public void buildUserBill(UserBillDto userBillDto, Double money, Double accountAfterAmount, Double accountAmount,
            int accountType)
    {
        // 修改账单记录为负数
        userBillDto.setMoney(money);
        // 消费记录相关账户使用后的余额
        userBillDto.setAccountAfterAmount(accountAfterAmount);
        userBillDto.setAccountType(accountType);
        userBillDto.setAccountAmount(accountAmount);
    }

    /**
     * 生成交易流水
     * 
     * @Function: 
     *            com.idcq.appserver.service.shop.ShopServiceImpl.getTransactionDto
     * @Description:
     * 
     * @param userId 会员Id
     * @param orderId 订单编号
     * @param payAmount 支付金额
     * @param nowTime 当前时间
     * @return
     * @throws Exception
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月14日 下午5:11:17
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月14日 shengzhipeng v1.0.0 create
     */
    private TransactionDto getTransactionDto(Long userId, String orderId, Double payAmount, String nowTime)
            throws Exception
    {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(userId);
        transactionDto.setOrderId(orderId);
        transactionDto.setPayAmount(payAmount);
        transactionDto.setTransactionTime(nowTime);
        transactionDto.setStatus(1); // 支付完成
        transactionDto.setUserPayChannelId(new Long(1));
        transactionDto.setOrderPayType(0); // 单订单
        transactionDto.setLastUpdateTime(nowTime);
        transactionDto.setTransactionType(0); // 消费
        return transactionDto;

    }

    /**
     * 
     * 生成支付对象
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.installPayDto
     * @Description:
     * 
     * @param shopId 店铺id
     * @param settlePrice 支付金额
     * @param orderId 订单编号
     * @param payType 支付类型
     * @param payeeType 收款人类型：1点传奇平台收款-0, 商铺收款-1
     * @param payId 
     *        支付的唯一标记，支付类型为0和1时填写transation_id,代金券支付时填写代金券ID，红包支付时填写红包ID，优惠券支付时填写优惠券ID
     * @param date 付款时间
     * @param userId 付款人ID
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月14日 下午5:12:24
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月14日 shengzhipeng v1.0.0 create
     */
    private PayDto installPayDto(Long shopId, Double settlePrice, String orderId, int payType, int payeeType,
            Long payId, Date date, Long userId)
    {
        PayDto payDto = new PayDto();
        payDto.setOrderId(orderId);
        payDto.setPayType(payType);
        payDto.setPayAmount(settlePrice);
        payDto.setOrderPayType(0);
        payDto.setOrderPayTime(DateUtils.format(date, null));
        payDto.setPayeeType(payeeType); // 收款人类型
        payDto.setShopId(shopId);
        payDto.setPayId(payId);
        payDto.setUserId(userId);
        return payDto;
    }

    /**
     * 
     * 生成订单实体
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.installOrder
     * @Description:
     * 
     * @param shopId 店铺id
     * @param orderTitle 订单标题
     * @param totalPrice 订单总价
     * @param settlePrice 订单结算价
     * @param username 收银员用户名
     * @param cashierId 收银员id
     * @param userDto 会员实体
     * @param orderId 订单编号
     * @param date 当前时间
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月14日 下午5:14:29
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月14日 shengzhipeng v1.0.0 create
     */
    private OrderDto installOrder(Long shopId, String orderTitle, Double totalPrice, Double settlePrice,
            String username, Long cashierId, UserDto userDto, String orderId, Date date, String remark)
    {
        OrderDto order = new OrderDto();
        order.setOrderId(orderId);
        order.setOrderTitle(orderTitle);
        order.setOrderTotalPrice(totalPrice);
        order.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
        order.setSettlePrice(settlePrice);
        order.setShopId(shopId);
        order.setCashierId(cashierId);
        order.setCashierUsername(username);
        order.setUserId(userDto.getUserId());
        order.setOrderTime(date);
        order.setPayStatus(CommonConst.PAY_STATUS_PAYED);
        order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
        order.setLastUpdateTime(date);
        order.setGoodsNumber(0);// 无商品
        order.setOrderRealSettlePrice(settlePrice);
        order.setSettleType(CommonConst.ORDER_SETTLE_ONE); // 按订单结算价结算
        order.setOrderChannelType(3); // 后台下单
        order.setOrderFinishTime(date);
        order.setRemark(remark);
        order.setMobile(userDto.getMobile());
        order.setIsMember(CommonConst.USER_IS_MEMBER);
        return order;
    }

    /**
     * 
     * 
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.useCashCoipon
     * @Description:
     * 
     * @param orderId 订单编号
     * @param shopId 店铺id
     * @param userId 用户id
     * @param nowTime 操作时间
     * @param residualAmount 剩余支付金额
     * @param usedPrice 支付前已使用的代金券金额
     * @param usableBalance 单张代金券余额
     * @param uccId 代金券Id
     * @throws Exception
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年9月24日 下午2:09:12
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月24日 shengzhipeng v1.0.0 create
     */
    private void useCashCoupon(OrderDto order, Long shopId, Long userId, Date date, Double residualAmount,
            Double usedPrice, Double usableBalance, Long uccId) throws Exception
    {
        if (residualAmount > 0)
        {
            PayDto userCashPay = installPayDto(shopId, residualAmount, order.getOrderId(),
                    CommonConst.PAY_TYPE_CASH_COUPON, CommonConst.PAYEE_TYPE_PLATFORM, uccId, date, userId);
            UserXBillDto userXBill = getUserXBillDto(userId, uccId, userCashPay, residualAmount, usableBalance,
                    order.getOrderTitle());
            if(userCashPay.getPayAmount()>0){
                payDao.addOrderPay(userCashPay);
            }
            userXBillDao.insertUserXBillDao(userXBill);
            // 支付后代金券使用金额=支付前使用金额+当前支付使用金额
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("usedPrice", NumberUtil.add(usedPrice, residualAmount));
            param.put("payId", uccId);
            userCashCouponDao.updateUserCashCoupon(param);
        }
    }

    /**
     * 
     * 
     * @Function: 
     *            com.idcq.appserver.service.shop.ShopServiceImpl.getUserXBillDto
     * @Description:
     * 
     * @param userId 用户id
     * @param uccId 代金券主键id
     * @param payDto 支付记录
     * @param useMoney 使用的金额
     * @param accountAmount 使用前金额
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年9月23日 下午3:39:08
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月23日 shengzhipeng v1.0.0 create
     */
    private UserXBillDto getUserXBillDto(Long userId, Long uccId, PayDto payDto, Double useMoney, Double accountAmount,
            String billType)
    {

        // 生成代金券支付账单记录
        UserXBillDto userXBill = new UserXBillDto();
        userXBill.setUserId(userId);
        userXBill.setUccId(uccId);
        userXBill.setOrderPayType(payDto.getOrderPayType());
        userXBill.setOrderId(payDto.getOrderId());
        userXBill.setMoney(-useMoney); // 使用金额
        userXBill.setCreateTime(new Date());
        userXBill.setBillType(CommonConst.USER_CASHCOUPON_USE);
        userXBill.setBillTitle(billType);
        userXBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());// 账单状态为已完成
        userXBill.setBillDesc("订单支付");
        userXBill.setAccountAmount(accountAmount); // 处理前金额
        return userXBill;
    }

    public void checkSmsPayVeriCode(Long shopId, String mobile, String veriCode, String usage, Double settlePrice,
            String username) throws Exception
    {
        // 校验店铺信息
        checkShopInfo(shopId, username);
        UserDto userDto = userDao.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_MEMBER_NOT_EXIST, "手机号不是会员");
        boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, usage, veriCode, false);
        if (!flag)
        {
            // 验证不通过
            throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
        }
        Long userId = userDto.getUserId();

        // 代金券余额
        Double couponBalance = userCouponDao.getUserCashCouponBalance(userId);

        // 查询传奇宝账户
        UserAccountDto account = userAccountDao.getAccountMoney(userId);

        // 传奇宝余额
        Double onlineBalance = 0D;
        // 传奇宝账户正常
        if (null != account && CommonConst.ACCOUNT_NORMAL_STATUS == account.getAccountStatus())
        {
            onlineBalance = NumberUtil.add(account.getCouponAmount(), account.getRewardAmount());
        }
        // 在该店铺可用红包金额
        Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
        // 总余额
        Double totalBalance = NumberUtil.add(NumberUtil.add(couponBalance, onlineBalance), redPacketAmount);
        if (totalBalance < settlePrice)
        {
            // 总余额不够支付，需要支付的现金金额
            Double cashBalance = NumberUtil.sub(settlePrice, totalBalance);
            StringBuffer sb = new StringBuffer();
            sb.append("余额不足，扣除余额").append(totalBalance).append("元，还需支付现金").append(cashBalance).append("元！");
            throw new ValidateException(CodeConst.CODE_MEMBER_NOT_ENOUGH_MONEY, sb.toString());
        }
    }

    public void checkMember(Long shopId, String mobile, String username) throws Exception
    {
        // 校验店铺信息
        checkShopInfo(shopId, username);
        UserDto userDto = userDao.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_MEMBER_NOT_EXIST, "手机号不是会员");
    }

    /**
     * 扫码支付预下单
     * @Title: preOrderForScanCode
     * @param @param orderDto
     * @param @throws Exception
     * @return void 返回类型
     * @throws
     */
    public void preOrderForScanCode(OrderDto orderDto) throws Exception
    {
        Map result = checkShopInfo(orderDto.getShopId(), orderDto.getUserName());
        orderDto.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);// 设置状态为未支付
        orderDto.setCashierId((Long) result.get("cashierId"));
        // 校验店铺信息
        // 判断是否为会员
        UserDto userDto = userDao.getUserByMobile(orderDto.getMobile());
        if (userDto == null)
        {// 非会员
            orderDto.setIsMember(CommonConst.ORDER_NOT_MEMBER);
        }
        else
        {// 会员订单
            orderDto.setIsMember(CommonConst.ORDER_IS_MEMBER);
        }
        dealMemberPrePayOrderForHousekeeper(orderDto);
    }

    /**
     * 
     * 处理一点管家会员预付订单下单
     * @Title: dealMemberPrePayOrderForHousekeeper
     * @param @param orderDto
     * @param @return
     * @param @throws Exception
     * @return OrderDto 返回类型
     * @throws
     */
    private OrderDto dealMemberPrePayOrderForHousekeeper(OrderDto orderDto) throws Exception
    {
        orderDto.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
        orderDto.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
        orderDto.setOrderStatus(CommonConst.ORDER_STS_YKD);
        orderDto.setLastUpdateTime(new Date());
        orderDto.setGoodsNumber(0);// 无商品
        orderDto.setOrderRealSettlePrice(orderDto.getSettlePrice());
        orderDto.setSettleType(CommonConst.ORDER_SETTLE_ONE); // 按订单结算价结算
        orderDto.setOrderChannelType(orderDto.getOrderChannelType()); // 后台下单
        orderDto.setCashierUsername(orderDto.getUserName());
        orderDao.saveOrder(orderDto);
        return orderDto;
    }

    /**
     * 一点管家非会员订单
     * @Title: dealXorderPrePayForHousekeeper
     * @param @param orderDto
     * @param @return
     * @param @throws Exception
     * @return XorderDto 返回类型
     * @throws
     */
    private XorderDto dealXorderPrePayForHousekeeper(OrderDto orderDto) throws Exception
    {
        XorderDto xorder = new XorderDto();
        xorder.setXorderId(orderDto.getOrderId());
        xorder.setOrderTitle(orderDto.getOrderTitle());
        xorder.setOrderTotalPrice(orderDto.getOrderTotalPrice());// 订单总价
        xorder.setSettlePrice(orderDto.getSettlePrice());// 结算价格
        xorder.setShopId(orderDto.getShopId());
        xorder.setUserInfo(orderDto.getMobile());// 会员手机号
        xorder.setCashierId(orderDto.getCashierId());// 收银员
        xorder.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
        xorder.setCashierUsername(orderDto.getUserName());
        xorder.setOrderTime(orderDto.getOrderTime());
        xorder.setPayStatus(orderDto.getPayStatus());// 支付状态
        xorder.setOrderStatus(CommonConst.ORDER_STS_YKD);// 订单状态为已开单
        xorder.setSettleType(1); // 按订单结算价结算
        xorder.setOrderChannelType(4); // 一点管家下单
        xorder.setRemark(orderDto.getRemark());
        xorder.setOrderChannelType(orderDto.getOrderChannelType());
        xoderDao.addXorderDto(xorder);// 生成非会员订单
        return xorder;
    }

    /**
     * 校验店铺基本信息，以及管理员信息是否正常
     * 
     * @Function: com.idcq.appserver.service.shop.ShopServiceImpl.checkShopInfo
     * @Description:
     * 
     * @param shopId 店铺id
     * @param username 登录管理员用户名
     * @throws Exception
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年10月10日 下午3:59:55
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年10月10日 shengzhipeng v1.0.0 create
     */
    private Map checkShopInfo(Long shopId, String username) throws Exception
    {
        // 检查店铺信息是否正常，雇员信息是否存在。
        ShopDto shop = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        Integer shopStatus = shop.getShopStatus();
        CommonValidUtil.validShopStatus(shopStatus, new Integer[]
        { CommonConst.SHOP_LACK_MONEY_STATUS });
        Long cashierId = 0L; // 代表老板
        UserDto user = userDao.getUserById(shop.getPrincipalId());
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺会员账号不存在");
        // 如果不是老板登录就是雇员登录，判断是否为老板登录，比较手机号码
        if (username != null && !username.equals(user.getUserName()) && !username.equals(user.getMobile()))
        {
            // 代表雇员登录
            Map map = shopDao.queryShopEmployee(username, shopId);
            CommonValidUtil.validObjectNull(map, CodeConst.CODE_PARAMETER_NOT_EXIST, "该店铺不存在指定管理员");
            Integer status = Integer.valueOf(String.valueOf(map.get("status")));
            if (null == status || CommonConst.SHOP_EMPLOYEE_STATUS_NORMAL != status)
            {
                throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
            }
            cashierId = (Long) map.get("operatorId");
        }
        Map map = new HashMap();
        map.put("shop", shop);
        map.put("cashierId", cashierId);
        return map;
    }

    /**
     * 获得一点管家的店铺账单
     * @Title: getIdgjShopBill
     * @param @param map
     * @param @return
     * @param @throws Exception
     * @throws
     */
    public PageModel getIdgjShopBill(Map<String, Object> map) throws Exception
    {
        PageModel pageMode = new PageModel();
        List<Map<String, Object>> list = shopBillDao.getShopCombineBill(map);
        dealShopBillResult(list);
        Integer count = shopBillDao.getShopCombineBillCount(map);
        pageMode.setList(list);
        pageMode.setTotalItem(count);
        return pageMode;
    }

    /**
     * 处理店铺账单结果
     * @Title: dealShopBillResult
     * @param @param list
     * @return void 返回类型
     * @throws
     */
    private void dealShopBillResult(List<Map<String, Object>> list) throws Exception
    {
        List<String> withDrawOrderIds = new ArrayList<String>();// 提现订单Id,需要去提现表查某些信息
        Map<String, Map<String, Object>> withDrawMap = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> item : list)
        {
            String billType = item.get("bill_type") + "";
            if (CommonConst.BILL_TYPE_RECOMMAND_REWARD.equals(billType))
            {// 类型是推荐奖励
                item.put("billRemark", CommonConst.IDGJ_RECOMMAND_REWARD_NAME);
            }
            else if (CommonConst.BILL_TYPE_SALE.equals(billType))
            {// 销售商品
                item.put("billRemark", CommonConst.IDGJ_SALE_GOODS_NAME);
            }
            else if (CommonConst.BILL_TYPE_WITH_DRAW.equals(billType))
            {// 类型是提现
                withDrawOrderIds.add(item.get("order_id") + "");
                withDrawMap.put(item.get("transactionId") + "", item);
            }
            item.remove("transactionId");
            item.remove("billDesc");
        }
        if (withDrawOrderIds.size() > 0)
        {// 提现订单的数量大于0需要去提现表查详细信息
            List<WithdrawDto> withDrawList = withDrawDao.getWithDrawListByIdList(withDrawOrderIds);
            for (WithdrawDto withDrawDto : withDrawList)
            {
                Map<String, Object> item = withDrawMap.get(withDrawDto.getWithdrawId() + "");
                if (item != null)
                {
                    item.put("billRemark", withDrawDto.getBankName());
                    if (withDrawDto.getApplyTime() != null)
                    {
                        item.put("billTime", DateUtils.format(withDrawDto.getApplyTime(), DateUtils.DATETIME_FORMAT1));
                    }
                }
            }
        }

    }

    /**
     * 一点管家账单统计
     * @Title: getIdcqBillStatistics
     * @param @param params
     * @param @return
     * @param @throws Exception
     * @throws
     */
    @Override
    public Map<String, Object> getIdcqBillStatistics(Map<String, Object> params) throws Exception
    {
        List<Map<String, Object>> resultList = shopBillDao.getIdcqBillStatistics(params);
        // 统计按开始结束时间统计总收入支出
        Integer count = shopBillDao.getIdcqBillStatisticsCount(params);
        params.put("billDirection", 1);
        Double amount = shopBillDao.getIdcqBillStatisticsAmount(params);// 总收入
        params.put("billDirection", -1);
        Double expend = shopBillDao.getIdcqBillStatisticsAmount(params);
        Map<String, Object> resultObj = new HashMap<String, Object>();
        resultObj.put("expend", NumberUtil.fmtDouble(expend != null ? expend : 0, 2));// 支出
        resultObj.put("income", NumberUtil.fmtDouble(amount != null ? amount : 0, 2));
        resultObj.put("billData", resultList);
        resultObj.put("totalItem", count);
        return resultObj;
    }

    @Override
    public List<Map<String, Object>> getBillStat(Map<String, Object> map) throws Exception
    {
        return shopBillDao.getBillStat(map);
    }

    @Override
    public PageModel getBillDetail(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = shopBillDao.getBillDetailCount(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = shopBillDao.getBillDetail(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    @Override
    public Map<String, Object> fillBail(Long shopId, Double onlineIncomeMoney, Double rewardMoney) throws Exception
    {
        /**
         * 1、当两者（onlineIncomeMoney、rewardMoney）都不传的时候，优先从商铺收入扣除，收入不够再从奖励扣除，
         * 直到充值到配置项阀值。 2、当只传onlineIncomeMoney的时候，处理逻辑同1。
         * 3、当只传rewardMoney的时候，优先扣除奖励，不够再从商铺收入扣，直到充值到配置项阀值。
         */
        logger.info("传入参数：shopId=" + shopId + ",onlineIncomeMoney=" + onlineIncomeMoney + ",rewardMoney=" + rewardMoney);
        // 校验店铺是否存在
        ShopDto shopDto = shopDao.getShopEssentialInfo(shopId);

        if (shopDto == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        Integer shopStatus = shopDto.getShopStatus();
        logger.info("店铺状态：" + shopStatus);
        if (shopStatus == null || (shopStatus == 99 || shopStatus == 1 || shopStatus == 2))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_STATUS);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 商铺账户
        ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
        // 账户总金额
        Double accountAmount = account.getAmount();
        // 保证金余额
        Double depositAmount = account.getDepositAmount();
        // 线上营业收入余额
        Double onlineIncomeAmount = account.getOnlineIncomeAmount();
        // 平台奖励余额
        Double rewardAmount = account.getRewardAmount();
        // 转充总金额
        Double fillMoney = 0D;
        logger.info("转充前：账户总金额=" + accountAmount + ",保证金=" + depositAmount + ",线上营业收入余额=" + onlineIncomeAmount
                + ",平台奖励余额=" + rewardAmount);
        // 线上营业收入不足
        if (null != onlineIncomeMoney && NumberUtil.sub(onlineIncomeAmount, onlineIncomeMoney) < 0)
        {
            throw new ValidateException(CodeConst.ONLINE_INCOME_AMOUNT_ERROR, "在线营业收入余额不足");
        }
        // 平台奖励不足
        if (null != rewardMoney && NumberUtil.sub(rewardAmount, rewardMoney) < 0)
        {
            throw new ValidateException(CodeConst.REWARD_AMOUNT_ERROR, "平台奖励余额不足");
        }

        // 交易记录
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(shopId);// 用户id,shopid
        transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_INNER_CHARGE);// 商铺内转充
                                                                                     // 目前无常量，主版本再追加
        transactionDto.setTerminalType(CommonConst.TERMINAL_TYPE_CHARGE);
        transactionDto.setStatus(CommonConst.TRANSACTION_STATUS_FINISH);
        transactionDto.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE);
        transactionDto.setRdOrgName(CommonConst.TERMINAL_TYPE_CHARGE);
        transactionDto.setTransactionTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
        transactionDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
        transactionDao.addTransaction(transactionDto);
        Long transactionId = transactionDto.getTransactionId();

        // 线上收入
        if (null != onlineIncomeMoney)
        {
            // 商铺线上收入余额=线上收入余额-传入的线上收入余额
            onlineIncomeAmount = NumberUtil.sub(onlineIncomeAmount, onlineIncomeMoney);
            insertShopBillForFillBail(shopId, -onlineIncomeMoney, accountAmount, CommonConst.BILL_DIRECTION_DOWN,
                    CommonConst.SHOP_ACCOUNT_TYPE_INCOME, transactionId, onlineIncomeAmount);
        }
        // 奖励收入
        if (null != rewardMoney)
        {
            rewardAmount = NumberUtil.sub(rewardAmount, rewardMoney);
            // 插入流水
            insertShopBillForFillBail(shopId, -rewardMoney, accountAmount, CommonConst.BILL_DIRECTION_DOWN,
                    CommonConst.SHOP_ACCOUNT_TYPE_REWARD, transactionId, rewardAmount);
        }
        // 假如奖励金额和线上收入金额都不传，假如保证金不足则自动从线上收入和奖励金额扣除
        if (null == onlineIncomeMoney && null == rewardMoney)
        {
            // 获取商品配置信息
         /*   List<ShopConfigureSettingDto> shopSettings = shopSettingDao.queryShopConfigureSetting(shopId,
                    CommonConst.SHOP_SETTING_TYPE_CHARGE);*/
            
            ConfigDto searchCondition = new ConfigDto();
            searchCondition.setBizType(1);
            searchCondition.setBizId(shopId);
            searchCondition.setConfigGroup("DEPOSIT_CONFIG");
            List<ConfigDto> config = commonService.queryConfigDto(searchCondition);
            Double bailAmount = CommonConst.SHOP_SETTING_BAIL_AMOUNT_VALUE; // 默认500
            if (CollectionUtils.isNotEmpty(config))
            {
                for (ConfigDto temp : config)
                {
                    // 获取自动充值配置
                    String key = temp.getConfigKey();
                    String value = temp.getConfigValue();
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
                    {
                        if (CommonConst.SHOP_SETTING_BAIL_AMOUNT.equals(key))
                        {
                            bailAmount = Double.valueOf(value);
                        }
                    }
                }
            }
            // 差值 = 保障金余额-阀值
            Double depositAmountBalance = NumberUtil.sub(depositAmount, bailAmount);
            // 保障金不足
            if (depositAmountBalance < 0)
            {
                // 商铺线上收入=线上收入余额-差值(负值)
                Double tempAmount = NumberUtil.add(onlineIncomeAmount, depositAmountBalance);
                // 线上收入足额转充
                if (tempAmount >= 0)
                {
                    // 线上充入金额=保障金和-阀值
                    onlineIncomeMoney = -depositAmountBalance;
                    // 线上账户余额
                    insertShopBillForFillBail(shopId, -onlineIncomeMoney, accountAmount,
                            CommonConst.BILL_DIRECTION_DOWN, CommonConst.SHOP_ACCOUNT_TYPE_INCOME, transactionId,
                            NumberUtil.sub(onlineIncomeAmount, onlineIncomeMoney));
                }
                else
                {
                    // 线上收入不足转充，需要从奖励金额扣
                    // 奖励金额还需扣除的金额 = 阀值-线上收入余额
                    Double needRewardMoney = -depositAmountBalance;
                    Double tempRewardMoney = NumberUtil.sub(needRewardMoney, onlineIncomeAmount);
                    // 线上充入金额=保障金和-阀值
                    onlineIncomeMoney = onlineIncomeAmount;
                    if (onlineIncomeMoney != 0)
                    {
                        Double accountAfterAmount = 0d;
                        insertShopBillForFillBail(shopId, -onlineIncomeMoney, accountAmount,
                                CommonConst.BILL_DIRECTION_DOWN, CommonConst.SHOP_ACCOUNT_TYPE_INCOME, transactionId,
                                accountAfterAmount);

                    }
                    // 需要扣出的奖励金额大于实际奖励余额，则此次转充需要扣出全部奖励金额
                    if (tempRewardMoney > rewardAmount)
                    {
                        rewardMoney = rewardAmount;
                    }
                    else
                    {
                        rewardMoney = tempRewardMoney;
                    }
                    if (rewardMoney != null && rewardMoney != 0)
                    {
                        Double accountAfterAmount = NumberUtil.add(rewardAmount, -rewardMoney);
                        insertShopBillForFillBail(shopId, -rewardMoney, accountAmount, CommonConst.BILL_DIRECTION_DOWN,
                                CommonConst.SHOP_ACCOUNT_TYPE_REWARD, transactionId, accountAfterAmount);
                    }

                }
            }
        }
        // 转充总金额 = 奖励金额 - 线上金额(负数)
        fillMoney = NumberUtil.add(rewardMoney, onlineIncomeMoney);
        // 插入保证金增加账单
        if (rewardMoney == null)
        {
            rewardMoney = 0D;
        }
        if (onlineIncomeMoney == null)
        {
            onlineIncomeMoney = 0D;
        }
        if (rewardMoney != 0 || onlineIncomeMoney != 0)
        {
            Double accountAfterAmount = NumberUtil.add(depositAmount, fillMoney);
            insertShopBillForFillBail(shopId, fillMoney, accountAmount, CommonConst.BILL_DIRECTION_ADD,
                    CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT, transactionId, accountAfterAmount);
        }
        Integer arrearsFlag = null;
        Double balanceMoney = NumberUtil.add(depositAmount, fillMoney);
        logger.info("店铺状态：" + shopStatus + "---充值后金额：" + balanceMoney);
        if (balanceMoney >= 0 && shopStatus == CommonConst.SHOP_LACK_MONEY_STATUS)
        {
            arrearsFlag = CommonConst.ARREARS_FLAG_FALSE;
            ShopDto shop = new ShopDto();
            shop.setShopStatus(CommonConst.SHOP_NORMAL_STATUS);
            shop.setShopId(shopId);
            shopDao.updateShopStatus(shop);
            // 店铺状态有修改，清空缓存
            DataCacheApi.del(CommonConst.KEY_SHOP + shopId);
        }

        // 更新账户余额
        shopAccountDao.updateShopAccount(shopId, null, -onlineIncomeMoney, -rewardMoney, null, fillMoney, null,
                arrearsFlag,null,null, null);
        // 更新交易流水金额
        transactionDto.setPayAmount(fillMoney);
        transactionDao.updateTransaction(transactionDto);

        /*
         * shopId int 是 商铺ID fillMoney double 是 转充总金额 onlineIncomeMoney double 是
         * 转充店铺收入金额 rewardMoney double 是
         * 转充平台奖励金额。fillMoney=onlineIncomeMoney+rewardMoney balanceMoney double
         * 是 转充后，保证金余额
         */
        resultMap.put("shopId", shopId);
        resultMap.put("fillMoney", fillMoney);
        resultMap.put("onlineIncomeMoney", onlineIncomeMoney);
        resultMap.put("rewardMoney", rewardMoney);
        resultMap.put("balanceMoney", balanceMoney);

        return resultMap;
    }

    /**
     * 构建商铺账单
     * @param shopId
     * @param onlineIncomeMoney
     * @param rewardMoney
     * @param accountAmount
     * @param billDirection
     * @throws Exception
     */
    public void insertShopBillForFillBail(Long shopId, Double money, Double accountAmount, Integer billDirection,
            Integer accountType, Long transactionId, Double accountAfterAmount) throws Exception
    {
        // 插入商铺流水
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setBillStatus(2); // 账单状态:处理中=1,成功=2,失败=3
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setShopId(shopId);
        shopBillDto.setMoney(money);
        shopBillDto.setAccountAfterAmount(accountAfterAmount);
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_TRANSFER);// '账单类型:销售商品=1,支付保证金=2,购买红包=3,提现=4,充值=5,推荐奖励=6',
        shopBillDto.setBillDirection(billDirection);// 1代表增加
        shopBillDto.setAccountAmount(accountAmount);
        shopBillDto.setBillDesc("转充");
        if(accountType == CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT) {
            shopBillDto.setBillTitle("转到保证金" + NumberUtil.formatDoubleStr(Math.abs(money), 2));
        } else {
            shopBillDto.setBillTitle("余额转充" + NumberUtil.formatDoubleStr(Math.abs(money), 2));
        }
       
        shopBillDto.setAccountType(accountType);// 保证金CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT578
        shopBillDto.setTransactionId(transactionId);
        shopBillDao.insertShopBill(shopBillDto);
    }

    public double getShopRewardTotalBy(Map<String, Object> map) throws Exception
    {
        return shopBillDao.getShopRewardTotalBy(map);
    }

    public PageModel getPromotionShop(Long cityId, String queryDate, int pageNo, int pageSize) throws Exception
    {
        PageModel pm = new PageModel();
        int totalItem = this.shopDao.getPromotionShopCount(cityId, queryDate);
        if (totalItem > 0)
        {
            List<Map> list = this.shopDao.getPromotionShop(cityId, queryDate, pageNo, pageSize);
            pm.setList(list);
        }

        pm.setToPage(pageNo);
        pm.setPageSize(pageSize);
        pm.setTotalItem(totalItem);
        return pm;
    }

    public Map getPromotionShopDetail(Long shopId, String actCode) throws Exception
    {
        Map map = this.shopDao.getPromotionShopDetail(shopId, actCode);
        if (map != null)
        {
            String actPictureUrlStr = (String) map.get("actPicture");
            if (StringUtils.isNotBlank(actPictureUrlStr))
            {
                String[] actPictureUrl = actPictureUrlStr.split(CommonConst.COMMA_SEPARATOR);
                List<Map> actPictureUrls = new ArrayList<Map>();
                for (String string : actPictureUrl)
                {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("actPictureUrl", string);
                    actPictureUrls.add(param);
                }
                map.put("actPictureUrls", actPictureUrls);
            }
            map.remove("actPicture");
        }
        return map;
    }

    /**
     * 一点管家 微信扫码预下单
     * @Title: preOrderForWxScanCode
     * @param @param orderDto
     * @param @throws Exception
     * @throws
     */
    public void preOrderForWxScanCode(OrderDto orderDto) throws Exception
    {
        Map result = checkShopInfo(orderDto.getShopId(), orderDto.getUserName());
        orderDto.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);// 设置状态为未支付
        orderDto.setCashierId((Long) result.get("cashierId"));
        // 校验店铺信息
        // 判断是否为会员
        UserDto userDto = userDao.getUserByMobile(orderDto.getMobile());
        if (userDto != null)
        {// 记录是否是会员订单
            orderDto.setIsMember(CommonConst.ORDER_IS_MEMBER);
        }
        else
        {
            orderDto.setIsMember(CommonConst.ORDER_NOT_MEMBER);
        }
        dealMemberPrePayOrderForHousekeeper(orderDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#getGoodsSalesStat(java.util
     * .Map)
     */
    @Override
    public PageModel getGoodsSalesStat(Map<String, Object> pMap) throws Exception
    {
        PageModel pm = new PageModel();
        int totalItem = this.goodsCategoryDao.getGoodsSalesStatCount(pMap);
        if (totalItem > 0)
        {
            List<Map<String, Object>> list = this.goodsCategoryDao.getGoodsSalesStat(pMap);
            pm.setList(list);
        }
        pm.setTotalItem(totalItem);
        return pm;
    }
    @Override
    public PageModel getGoodsCategorySalesStat(Map<String, Object> pMap) throws Exception
    {
        PageModel pm = new PageModel();
        int totalItem = this.goodsCategoryDao.getGoodsCategorySalesStatCount(pMap);
        if (totalItem > 0)
        {
            List<Map<String, Object>> list = this.goodsCategoryDao.getGoodsCategorySalesStat(pMap);
            pm.setList(list);
        }
        pm.setTotalItem(totalItem);
        return pm;
    }
    @Override
    public PageModel shopIncomeStatDetail(Map<String, Object> parms) throws Exception
    {
        PageModel pm = new PageModel();
        int totalItem = shopDao.shopIncomeStatDetailCount(parms);
        if (totalItem > 0)
        {
            List<Map<String, Object>> list = shopDao.shopIncomeStatDetail(parms);
            pm.setList(list);
        }
        pm.setTotalItem(totalItem);
        return pm;
    }
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#shopDayIncomeStatDetail(java.util.Map)
	 */
	@Override
	public PageModel shopDayIncomeStatDetail(Map<String, Object> parms)
			throws Exception {

        PageModel pm = new PageModel();
        int totalItem = shopDao.shopDayIncomeStatDetailCount(parms);
        if (totalItem > 0)
        {
            List<Map<String, Object>> resultList = shopDao.shopDayIncomeStatDetail(parms);
            pm.setList(updateDayIncomeStatDetailList(resultList));
        }
        pm.setTotalItem(totalItem);
        return pm;
    
	}
	private List<Map<String, Object>> updateDayIncomeStatDetailList(List<Map<String, Object>> resultList) throws Exception{
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();

		if(CollectionUtils.isNotEmpty(resultList)){
			for (Map<String, Object> income : resultList) {
				String orderId = (String) income.get("orderId");
				List<Map<String, Object>> payList = updateOrderPayList(payDao.getOrderPayList(orderId, 1));//成功
				income.put("paies", payList);
				newList.add(income);
			}
		}
		return newList;
	}
	private List<Map<String, Object>> updateOrderPayList(List<PayDto> paylist) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(CollectionUtils.isNotEmpty(paylist)){
			for (PayDto payDto : paylist) {
				Map<String, Object> payMap = new HashMap<String, Object>();
				payMap.put("payType", payDto.getPayType());
				payMap.put("payAmount", payDto.getPayAmount());
				resultList.add(payMap);
			}
		}
		return resultList;
	}

		
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#getOperateShopList(java.
     * util.Map)
     */
    @Override
    public PageModel getOperateShopList(Map<String, Object> pMap) throws Exception
    {
        PageModel pm = new PageModel();
        Integer totalItem = this.shopDao.getOperateShopCount(pMap);
        if (totalItem != null && totalItem > 0)
        {
            List<Map<String, Object>> resultList = this.shopDao.getOperateShopList(pMap);
            // 重组返回结果
            pm.setList(updateResultList(resultList));
        }
        pm.setTotalItem(totalItem);
        return pm;
    }

    @Override
    public Map<String, Object> getOperateShopStat(Map<String, Object> requestMap) throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer shopSum = shopDao.getOperateShopNumOfAgent(requestMap);
        Integer hasDeviceShopSum = shopDao.getOperateDeviceShopNumOfAgent(requestMap);
        resultMap.put("shopSum", shopSum);
        resultMap.put("hasDeviceShopSum", hasDeviceShopSum);
        return resultMap;

    }

    private List<Map<String, Object>> updateResultList(List<Map<String, Object>> resultList) throws Exception
    {
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(resultList))
        {
            for (Map<String, Object> resultMap : resultList)
            {
                Long shopId = (Long) resultMap.get("shopId");

                // 查询商铺是否有收银机
                Map<String, Object> pMap = new HashMap<String, Object>();

                pMap.put("shopId", shopId);
                pMap.put("deviceType", CommonConst.DEVICE_TYPE_CASH);
                int num = shopDao.getOperateDeviceShopNumOfAgent(pMap);
                if (num > 0)
                {// TODO 后面会更改是否安装收银机的规则
                    resultMap.put("hasDevice", 1);// 存在收银机
                }
                else
                {
                    resultMap.put("hasDevice", 0);// 不存在收银机
                }
                newList.add(resultMap);
            }

        }
        return newList;
    }

    @Override
    public Map getTotalMember(Long shopId, String startTime, String endTime)
    {
        Map map = shopDao.getTotalMember(shopId, startTime, endTime);
        return map;
    }

    public Map<String, Object> getShopResource(Long shopId, Long resourceId) throws Exception
    {
        return shopDao.getShopResource(shopId, resourceId);
    }

    @Override
    public PageModel getShopRefMembers(Map<String, Object> map)
    {
        PageModel pageModel = new PageModel();
        Integer count = shopDao.getShopRefTotalMembers(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = shopDao.getShopRefMembers(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    @Override
    public PageModel getShopSalestatistics(Long shopId, Integer cashierId, Integer queryMode, String memberType,String startTime,
            String endTime, Integer pNo, Integer pSize)
    {
        PageModel pageModel = new PageModel();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("shopId", shopId);
        map.put("cashierId", cashierId);
        map.put("memberType", memberType);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("n", (pNo - 1) * pSize);
        map.put("m", pSize);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (cashierId == null)
        {// 销售统计
            if (queryMode == 0)
            {// 按收银员查询
                resultList = shopDao.getShopSalestatisticsByCashier(map);
                pageModel.setList(resultList);
                map.put("flag", 1);
                resultList = shopDao.getShopSalestatisticsByCashier(map);
            }
            else if (queryMode == 1)
            {//按照支付方式查询
                map.put("incomeType", CommonConst.INCOME_TYPE_CONSUME);
                resultList = shopDao.getShopSalestatisticsByPayWay(map);
                pageModel.setList(resultList);
            }
            else if(queryMode == 2){//按照顾客类型查询
            	if(memberType != null && memberType.equals("1")){//查询明细
            		// 单个商品销售统计明细
                    resultList = shopDao.getShopSalestatisticsByCashierId(map);
                    pageModel.setList(resultList);
                    map.put("flag", 1);
                    resultList = shopDao.getShopSalestatisticsByCashierId(map);
            	}else{
            		 resultList = shopDao.getShopSalestatisticsByIsMember(map);
                     pageModel.setList(resultList);
            	}
            }
        }
        else
        {	// 单个商品销售统计明细
            resultList = shopDao.getShopSalestatisticsByCashierId(map);
            pageModel.setList(resultList);
            map.put("flag", 1);
            resultList = shopDao.getShopSalestatisticsByCashierId(map);
        }
        pageModel.setTotalItem(resultList.size());
        return pageModel;
    }

    @Override
    public boolean verifyCardNo(Map<String, Object> map)
    {
        int count = shopDao.verifyCardNo(map);
        if (count == 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public Map getUserInfoByUserId(Long userId)
    {
        return shopDao.getUserInfoByUserId(userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.idcq.appserver.service.shop.IShopServcie#getOpenedCitis(java.util
     * .Map)
     */
    @Override
    public PageModel getOpenedCitis(Map<String, Object> pMap) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = shopDao.getOpenedCitisCount(pMap);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = shopDao.getOpenedCitis(pMap);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    public List<ShopDto> getShopList(Map<String, Object> params) throws Exception
    {
        return shopDao.getShopListByParams(params);
    }

    public Integer getShopListCount(Map<String, Object> params) throws Exception
    {
        return shopDao.getShopListByParamsCount(params);
    }

	@Override
	public Map<String, Object> getShopMemberStatisticsInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Object shopIdStr= map.get("shopId");
//		Long shopId = (Long)shopIdStr;
//		ShopDto shop = this.shopDao.getShopExtendByIdAndStatus(shopId, null);
		Map<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("shopId", String.valueOf(map.get("shopId")));
		requestMap.put("status", String.valueOf(CommonConst.SHOP_STATUS_NORMAL));
		int count = shopDao.getShopCountByMap(requestMap);
		if(count==0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP); 
		}
//		CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		//会员储值统计信息
		map.put("orderStatus", CommonConst.ORDER_STATUS_FINISH);
		map.put("memberStatus", CommonConst.MEMBER_STATUS_DELETE);
		//查询新增店内会员数
		int addShopMembersCount = shopMemberDao.getNewAddShopMemberTotal(map);
		//查询在店内消费的会员数
		int consumeShopMembersCount = orderDao.getConsumeShopMembersCount(map);
		List<Map<String,Object>> shopMemberCardStatisticsInfoMap = shopMemberDao.getConsumeShopMembersCount(map);
		resultMap.put("addShopMembersCount", addShopMembersCount);
		resultMap.put("consumeShopMembersCount", consumeShopMembersCount);
		resultMap.put("shopMemberCardStatisticsInfo", shopMemberCardStatisticsInfoMap);
		return resultMap;
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getShopAuditInfo(java.lang.Long)
	 */
	@Override
	public Map<String, Object> getShopAuditInfo(Long withdrawId)
			throws Exception {
       /*
                        基准余额：最早一个未提现成功的前一条记录
		收入总和：orderPay表统计线上收入
		提现总和：withdraw表统计
		可提现余额：基准点余额+收入总和-提现总和
		*/
		Map<String, Object> reaultMap = new HashMap<String, Object>();
		
		//获取提现信息
		ShopWithDrawDto shopWithDraw = shopWithDrawDao.getShopWithDrawById(withdrawId);
        CommonValidUtil.validObjectNull(shopWithDraw, CodeConst.CODE_PARAMETER_NOT_EXIST, "提现记录不存在");
		Double withDrawAmount = shopWithDraw.getAmount();
		Date withdrawTime = shopWithDraw.getApplyTime();
		Long shopId = shopWithDraw.getShopId();
		BigDecimal nextWithdrawAmount = new BigDecimal(shopWithDraw.getNextWithdrawAmount());
		
		//账号信息
		ShopAccountDto shopAccountDB = shopAccountDao.getShopAccount(shopId);
		//获取基准余额，最近未审核id最小的一条记录的钱一条记录
		Map<String, Object> standardWithdrawDto = shopWithDrawDao.getStandardMoney(shopId);
		Date standardWithdrawTime = null;
		BigDecimal standardMoney = new BigDecimal(0);
		if(standardWithdrawDto != null){
			standardWithdrawTime = (Date) standardWithdrawDto.get("withdrawTime");
			standardMoney = (BigDecimal) standardWithdrawDto.get("nextWithdrawAmount");
		}
		
		//获取成功提现总额
		Map<String, Object> withdrawSuccessTotalMoneyDto = shopWithDrawDao.getWithdrawTotalMoney(shopId, CommonConst.WITHDRAW_STATUS_SUCCESS, standardWithdrawTime, withdrawTime);
		BigDecimal withdrawSuccessTotalMoney = new BigDecimal(0);
		if(withdrawSuccessTotalMoneyDto != null){
			withdrawSuccessTotalMoney = (BigDecimal) withdrawSuccessTotalMoneyDto.get("withdrawTotalMoney");
		}
		
		//获取审核通过提现总额
		Map<String, Object> withdrawAuditingPassTotalMoneyDto = shopWithDrawDao.getWithdrawTotalMoney(shopId, CommonConst.WITHDRAW_STATUS_AUDITING_PASS, standardWithdrawTime, withdrawTime);
		BigDecimal withdrawAuditingPassTotalMoney = new BigDecimal(0);
		if(withdrawAuditingPassTotalMoneyDto != null){
			withdrawAuditingPassTotalMoney = (BigDecimal) withdrawAuditingPassTotalMoneyDto.get("withdrawTotalMoney");
		}
		
		//获取待审核提现总额
		Map<String, Object> withdrawTodoTotalMoneyDto = shopWithDrawDao.getWithdrawTotalMoney(shopId, CommonConst.WITHDRAW_STATUS_TODO, standardWithdrawTime, withdrawTime);
		BigDecimal withdrawTodoTotalMoney = new BigDecimal(0);
		if(withdrawTodoTotalMoneyDto != null){
			withdrawTodoTotalMoney = (BigDecimal) withdrawTodoTotalMoneyDto.get("withdrawTotalMoney");
		}

		//获取收入总和
		Map<String, Object> incomeTotalMoneyDto = payDao.getIncomeTotalMoney(shopId, CommonConst.ORDER_PAYSTS_FINISHED, CommonConst.PAYEE_TYPE_PLATFORM, standardWithdrawTime, withdrawTime);
		BigDecimal incomeTotalMoney = new BigDecimal(0);
		if(incomeTotalMoneyDto != null){
			incomeTotalMoney = (BigDecimal) incomeTotalMoneyDto.get("incomeTotalMoney");
		}
		
		//冻结金额
		Double freezeAmount = shopAccountDB.getFreezeAmount();
		
		//转充金额 ,通过对保证金账户类型为转充的求和，有可能为负值，当店铺因特殊情况将保证金全部转入店铺收入时有可能为负
		Map<String, Object> chargeTotalMoneyDto = shopBillDao.getchargeTotalMoney(shopId,CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT ,
				Integer.parseInt(CommonConst.SHOP_BILL_TYPE_TRANSFER), standardWithdrawTime, withdrawTime);
		BigDecimal chargeTotalMoney = new BigDecimal(0);
		if(chargeTotalMoneyDto != null){
			chargeTotalMoney = (BigDecimal) chargeTotalMoneyDto.get("chargeTotalMoney");
		}
		
		//奖励金额  
		Map<String, Object> rewardDto = shopBillDao.getchargeTotalMoney(shopId,CommonConst.SHOP_ACCOUNT_TYPE_REWARD,
				Integer.parseInt(CommonConst.SHOP_BILL_TYPE_REWARD), standardWithdrawTime, withdrawTime);
		BigDecimal rewardMoney = new BigDecimal(0);
		if(rewardDto != null){
			rewardMoney = (BigDecimal) rewardDto.get("chargeTotalMoney");
			incomeTotalMoney = rewardMoney.add(incomeTotalMoney);//计算收入总额需要额外增加奖励
		}
		
		//红包金额
		Map<String, Object> redPacketDto = shopBillDao.getchargeTotalMoney(shopId,CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,
				Integer.parseInt(CommonConst.SHOP_BILL_TYPE_REDPACKET), standardWithdrawTime, withdrawTime);
		BigDecimal redPacketMoney = new BigDecimal(0);
		if(redPacketDto != null){
			redPacketMoney = (BigDecimal) redPacketDto.get("chargeTotalMoney");
			incomeTotalMoney = incomeTotalMoney.add(redPacketMoney);//计算收入总额减掉红包金额；redPacketMoney为负值

		}
		//手续费
		Map<String, Object> withdrawCommission  = shopWithDrawDao.getWithdrawCommissionTotal(shopId,standardWithdrawTime, withdrawTime);
		BigDecimal withdrawCommissionTotalMoney = new BigDecimal(0);
		if(withdrawCommission!=null){
			withdrawCommissionTotalMoney = (BigDecimal) withdrawCommission.get("withdrawCommissionTotalMoney");
		}
		//提现总和
		BigDecimal withdrawTotalMoney  = withdrawSuccessTotalMoney.add(withdrawAuditingPassTotalMoney).add(withdrawTodoTotalMoney);
		
		//可提现余额：基准点余额+收入总和-(提现总和)-转充金额-本次提现后余额-手续费
		BigDecimal errorValues = standardMoney.add(incomeTotalMoney).
				subtract(withdrawTotalMoney).subtract(chargeTotalMoney).
				subtract(nextWithdrawAmount).subtract(withdrawCommissionTotalMoney);
		
		reaultMap.put("withdrawId", withdrawId);
		reaultMap.put("withDrawAmount", NumberUtil.formatDouble(withDrawAmount.doubleValue(), 2));
		reaultMap.put("redPacketTotalMoney", NumberUtil.formatDouble(redPacketMoney.doubleValue(), 2));
		reaultMap.put("standardMoney", NumberUtil.formatDouble(standardMoney.doubleValue(), 2));
		reaultMap.put("incomeTotalMoney", NumberUtil.formatDouble(incomeTotalMoney.doubleValue(), 2));
		reaultMap.put("withdrawSuccessTotalMoney", NumberUtil.formatDouble(withdrawSuccessTotalMoney.doubleValue(), 2));
		reaultMap.put("withdrawAuditingPassTotalMoney", NumberUtil.formatDouble(withdrawAuditingPassTotalMoney.doubleValue(), 2));
		reaultMap.put("withdrawTodoTotalMoney", NumberUtil.formatDouble(withdrawTodoTotalMoney.doubleValue(), 2));
		reaultMap.put("withdrawTotalMoney", NumberUtil.formatDouble(withdrawTotalMoney.doubleValue(), 2));
		reaultMap.put("chargeTotalMoney", NumberUtil.formatDouble(chargeTotalMoney.doubleValue(), 2));
		reaultMap.put("freezeAmount", NumberUtil.formatDouble(freezeAmount.doubleValue(), 2));
		reaultMap.put("nextWithdrawAmount", NumberUtil.formatDouble(nextWithdrawAmount.doubleValue(), 2));
		reaultMap.put("withdrawTime", withdrawTime);
		reaultMap.put("standardTime", standardWithdrawTime);
		reaultMap.put("errorValues", NumberUtil.formatDouble(errorValues.doubleValue(), 2));
		reaultMap.putAll(withdrawCommission);
		return reaultMap;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getShopByPrincipalId(java.lang.Long)
	 */
	@Override
	public ShopDto getShopByPrincipalId(Long principalId) throws Exception {
		
		return shopDao.getShopByPrincipalId(principalId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getSalerPerformanceList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getSalerPerformanceList(
			Map<String, Object> param) throws Exception {
		return shopDao.getSalerPerformanceList(param);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getSalerPerformanceCount(java.util.Map)
	 */
	@Override
	public int getSalerPerformanceCount(Map<String, Object> param)
			throws Exception {
		return shopDao.getSalerPerformanceCount(param);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getShopListByHeadShopId(java.lang.Long)
	 */
	@Override
	public List<ShopDto> getShopListByHeadShopId(Long shopId) throws Exception {
		
		return shopDao.getShopListByHeadShopId(shopId);
	}


	@Override
	public UserDto getShopPrincipalInfoByShopId(Long shopId) {
		Map<String, Object> reaultMap = new HashMap<String, Object>();
		reaultMap.put("shopId", shopId);
		return userDao.getShopPrincipalInfoByMap(reaultMap);
	}

    @Override
    public Map getBillDetailById(Long billId, Long shopId) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Long> shopIds = new ArrayList<Long>();
        shopIds.add(shopId);
        param.put("billId", billId);
        param.put("shopId", shopIds);
        param.put("n", 0);
        param.put("m", 1);
        List<Map<String, Object>> resultList = shopBillDao.getBillDetail(param);
        if(CollectionUtils.isNotEmpty(resultList)) {
            Map map = resultList.get(0);
            String billType = map.get("moneyType") == null ? null : map.get("moneyType").toString();
            if (CommonConst.BILL_TYPE_REWARD.equals(billType)) {
                if(map.get("orderId") != null) {
                    String orderId = map.get("orderId").toString();
                    OrderDto order = orderDao.getOrderById(orderId);
                    if(null != order) {
                        UserDto user = userDao.getUserById(order.getUserId());
                        if(null != user) {
                            map.put("referUserName", user.getUserName());
                            map.put("referUserMobile", user.getMobile());
                        }
                    }
                }
            }
            return map;
        }
        return null;
    }

	@Override
	public PageModel shopIncomeStatByTimePeriod(
			Map<String, Object> searchParams) {
		PageModel pageModel = new PageModel();
		int count = orderDao.shopIncomeStatByTimePeriodCount(searchParams);
		if(count > 0 ){
			List<Map<String, Object>> list =orderDao.shopIncomeStatByTimePeriod(searchParams);
			pageModel.setList(list);
		}
		pageModel.setTotalItem(count);
		return pageModel;
	}
}
