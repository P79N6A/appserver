package com.idcq.appserver.controller.shop;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.aliscanpay.f2fpay.ToAlipayQrTradePay;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.region.ITownDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.dto.goods.PlaceGoodsDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.operationclassify.OperationClassifyDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.pay.WithdrawListDto;
import com.idcq.appserver.dto.pay.WithdrawRequestModel;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;
import com.idcq.appserver.dto.shop.BatchInsertCookingDetailModel;
import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;
import com.idcq.appserver.dto.shop.HandoverInfoDto;
import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
import com.idcq.appserver.dto.shop.ShopCookingDetails;
import com.idcq.appserver.dto.shop.ShopDetailDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.shop.ShopRsrcPramDto;
import com.idcq.appserver.dto.shop.ShopSettingsDto;
import com.idcq.appserver.dto.shop.ShopTechnicianDto;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.dto.user.UserSearchHistory;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.cashcoupon.ICashCouponService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.coupon.ICouponService;
import com.idcq.appserver.service.discount.IShopTimeDiscountService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.operationclassify.IOperationClassifyService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IDistribTakeoutSettingService;
import com.idcq.appserver.service.shop.IHandoverInfoServcie;
import com.idcq.appserver.service.shop.IShopConfigureSettingService;
import com.idcq.appserver.service.shop.IShopRsrcServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.service.shop.ITakeoutSetService;
import com.idcq.appserver.utils.ArrayUtil;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.ReflectionUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.utils.solr.SearchContent;
import com.idcq.appserver.utils.solr.SolrContext;
import com.idcq.appserver.wxscan.WXScanUtil;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;

/**
 * 商铺controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:38:07
 */
@Controller
public class ShopController extends BaseController
{
    private static final Logger logger = Logger.getLogger(ShopController.class);

    @Autowired
    public IShopServcie shopServcie;

    @Autowired
    public IShopRsrcServcie shopRsrcServcie;

    @Autowired
    private ICashCouponService cashCouponService;

    @Autowired
    private IShopTimeDiscountService shopTimeDiscountService;

    @Autowired
    private ICouponService couponService;

    @Autowired
    private IMemberServcie memberService;

    @Autowired
    private ITakeoutSetService takeoutSetService;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IRegionDao regionDao;

    @Autowired
    private ITownDao townDao;

    @Autowired
    private IPayServcie payServcie;

    @Autowired
    private IDistribTakeoutSettingService distribTakeoutSettingService;

    @Autowired
    private IShopTechnicianService iShopTechnicianService;

    @Autowired
    private IShopTechnicianService shopTechnicianService;

    @Autowired
    private IAttachmentRelationDao attachmentRelationDao;

    @Autowired
    private IGoodsGroupService goodsGroupService;

    @Autowired
    private ICollectService collectService;

    @Autowired
    private IShopConfigureSettingService shopSettingService;

    @Autowired
    private IOrderServcie orderServcie;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private IOrderServcie orderService;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private IBusAreaActivityService busAreaActivityService;

    @Autowired
    private IOperationClassifyService operationClassifyService;
    @Autowired
    public IHandoverInfoServcie handoverInfoServcie;

    /**
     * 获取商铺详情
     * 
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getShopDetails", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopDetail(HttpServletRequest request)
    {
        try
        {
            logger.info("获取指定商铺-start");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String longitudeStr = RequestUtils.getQueryParam(request, "longitude");// 经度
            String latitudeStr = RequestUtils.getQueryParam(request, "latitude");// 维度

            CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            ShopDetailDto pModel = this.shopServcie.getShopDetailById(Long.valueOf(shopId));

            if (pModel != null )
            {
            	if(pModel.getDistrictId() != null){
            		 DistrictDto districtDto = regionDao.getDistrictById((long) pModel.getDistrictId());
                     if (districtDto != null)
                     {
                         pModel.setDistrictName(districtDto.getDistrictName());
                     }
            	}
            	
            	 if(pModel.getTownId() != null ){
                 	TownDto townDto = regionDao.getTownById(pModel.getTownId());
                 	if(townDto!=null){
                 		pModel.setTownName(townDto.getTownName());
                 	}
                 }
            	 
            	 if(pModel.getCityId() != null ){
     	           	CitiesDto cityDto = regionDao.getCityById(pModel.getCityId().intValue());
     	           	if(cityDto!=null){
     	           		pModel.setCityName(cityDto.getCityName());
     	           	}
                }
            	 
            	if(pModel.getProvinceId() != null ){
            		ProvinceDto provinceDto = regionDao.getProvinceId(pModel.getProvinceId());
     	           	if(provinceDto!=null){
     	           		pModel.setProvinceName(provinceDto.getProvinceName());
     	           	}
                }
               
            }
            
            
            // 计算距离
            if (StringUtils.isNotBlank(longitudeStr) && StringUtils.isNotBlank(latitudeStr))
            {
                Double pLongitude = NumberUtil.strToDouble(longitudeStr, "longitude");
                Double pLatitude = NumberUtil.strToDouble(latitudeStr, "latitude");
                Double pLongitudeDB = pModel.getLongitude();
                Double latitudeDB = pModel.getLatitude();

                Double distance = NumberUtil.computeDistance(pLongitude, pLatitude, pLongitudeDB, latitudeDB);
                pModel.setDistance(distance);
            }

            if (1 == pModel.getBusinessAreaActivityFlag())
            {
                Long tempShopId = Long.valueOf(shopId);
                List<Long> activeShopIdsd = new ArrayList<>();
                activeShopIdsd.add(tempShopId);
                BusinessAreaActivityDto businessAreaActivityDto = new BusinessAreaActivityDto();
                businessAreaActivityDto.setShopIdList(activeShopIdsd);
                List<BusinessAreaActivityDto> activityList = busAreaActivityService
                        .getActivityListByCondition(businessAreaActivityDto);
                if (activityList != null && activityList.size() > 0)
                {
                    pModel.setActivityRuleName(activityList.get(activityList.size() - 1).getActivityRuleName());
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("obj", pModel);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取指定商铺成功", map, DateUtils.DATE_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取指定商铺-系统异常", e);
            throw new APISystemException("获取指定商铺-系统异常", e);
        }
    }

    /**
     * 搜索店铺
     * 
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/searchShop", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String searchShop(HttpServletRequest request)
    {
        try
        {
            // 解析请求参数并搜索符合条件的商铺
            PageModel pageModel = commonSearch(request, null);
            generateSearchHistory(request);
            if (pageModel != null && pageModel.getList() != null)
            {
                List<SearchContent> searchResult = (List<SearchContent>) pageModel.getList();
                pageModel.setList(convertSearchResult(searchResult, pageModel));
            }
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setLst(pageModel.getList());
            msgList.setrCount(pageModel.getTotalItem());
            List<MessageListDto> dataList = new ArrayList<MessageListDto>();
            dataList.add(msgList);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "搜索店铺成功", msgList, DateUtils.TIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("搜索店铺-系统异常", e);
            throw new APISystemException("搜索店铺-系统异常", e);
        }
    }

    /**
     * 将搜索结果进行转换
     * 
     * @param searchContents
     * @return
     */
    private List convertSearchResult(List<SearchContent> searchContents, PageModel pageModelParam) throws Exception
    {
        IAttachmentDao attachmentDao = BeanFactory.getBean(IAttachmentDao.class);
        List data = new ArrayList();
        Map<String, Integer> indexMap = new HashMap<String, Integer>();
        List<String> existShopId = new ArrayList<String>();
        Map<Long, Map<String, Object>> shopInfoMap = new HashMap<Long, Map<String, Object>>();
        List<Long> needQueryShopIdList = new ArrayList<Long>();
        List<Long> needQueryActivityShopIdList = new ArrayList<Long>();
        try
        {
            for (SearchContent searchContent : searchContents)
            {
                if (CommonConst.INDEX_TYPE_IS_GOODS.equals(searchContent.getContentType())
                        && !existShopId.contains(searchContent.getShopId() + ""))// 代表搜索出来的是商品
                {
                    String shopId = searchContent.getShopId();// 商铺id
                    ShopDto shopTarget = shopServcie.getShopExtendByIdAndStatus(Long.parseLong(shopId),
                            CommonConst.SHOP_STATUS_NORMAL);
                    if (shopTarget != null)
                    {
                        ShopDto shopDto = (ShopDto) shopTarget;
                        Integer shopLogoId = shopDto.getShopLogoId();
                        if (shopLogoId != null)
                        {
                            Object attachmentTarget = attachmentDao.queryAttachmentById((long) shopLogoId);
                            if (attachmentTarget != null)
                            {
                                Attachment attachment = (Attachment) attachmentTarget;
                                shopDto.setShopLogoUrl(FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
                            }
                        }
                        if (searchContent.getDistance() != null)
                        {
                            shopDto.setDistance(1000 * searchContent.getDistance());
                        }
                        if (shopDto.getDistrictId() != null)
                        {
                            DistrictDto districtDto = (DistrictDto) DataCacheApi.getObject(CommonConst.KEY_DISTRICT
                                    + shopDto.getDistrictId());
                            if (districtDto == null)
                            {
                                districtDto = regionDao.getDistrictById((long) shopDto.getDistrictId());
                                if (districtDto != null)
                                {
                                    DataCacheApi.setObjectEx(CommonConst.KEY_DISTRICT + shopDto.getDistrictId(),
                                            districtDto, 43200);
                                }
                            }
                            if (districtDto != null)
                            {
                                shopDto.setDistrictName(districtDto.getDistrictName());
                            }
                        }
                        if (shopDto.getTownId() != null)
                        {
                            TownDto townDto = (TownDto) DataCacheApi.getObject(CommonConst.KEY_TOWN
                                    + shopDto.getTownId());
                            if (townDto == null)
                            {
                                townDto = townDao.getTownById((long) shopDto.getTownId());
                                if (townDto != null)
                                {
                                    DataCacheApi
                                            .setObjectEx(CommonConst.KEY_TOWN + shopDto.getTownId(), townDto, 43200);
                                }
                            }
                            if (townDto != null)
                            {
                                shopDto.setTownName(townDto.getTownName());
                            }
                        }
                        Map<String, Object> shopMap = DataConvertUtil.convertObjToMap(shopDto,
                                CommonResultConst.SEARCH_SHOP_RETURN_FIELDS);
                        String shopLogoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopDto.getShopId());
                        if (shopLogoUrl == null)
                        {
                            needQueryShopIdList.add(shopDto.getShopId());
                        }
                        else
                        {
                            shopMap.put("shopLogoUrl", shopLogoUrl);
                        }
                        data.add(shopMap);// 将shop实体转换为返回需要的map
                        existShopId.add(shopId + "");
                        indexMap.put(shopId, searchContents.indexOf(searchContent));// 记录这个商品所属商铺在搜索结果中的位置
                    }
                }
                else if (CommonConst.INDEX_TYPE_IS_SHOP.equals(searchContent.getContentType())
                        && !existShopId.contains(searchContent.getShopId()))
                {
                    if (null != searchContent.getBusinessAreaActivityFlag() && 1 == searchContent.getBusinessAreaActivityFlag())
                    {
                        needQueryActivityShopIdList.add(Long.parseLong(searchContent.getShopId()));
                    }
                    ShopDto shopDto = new ShopDto();
                    shopDto.setIsRecommend(searchContent.getIsRecommend());
                    existShopId.add(searchContent.getShopId());
                    DataConvertUtil.propertyConvertIncludeDefaultProp(searchContent, shopDto,
                            CommonConst.SEARCH_SHOPS_MAP);
                    shopDto.setColumnId(searchContent.getShopColumnPid());
                    logger.info("商铺的图片id" + shopDto.getShopLogoId());
                    String shopLocation = searchContent.getShopLocation();
                    if (!StringUtils.isEmpty(shopLocation))// 经纬度解析
                    {
                        String[] locationArray = shopLocation.split(",");
                        if (locationArray.length > 1)
                        {
                            shopDto.setLatitude(Double.parseDouble(locationArray[0]));
                            shopDto.setLongitude(Double.parseDouble(locationArray[1]));
                        }
                    }
                    if (shopDto.getDistance() != null)
                    {
                        shopDto.setDistance(1000 * shopDto.getDistance());
                    }
                    logger.info("商铺的图片途径" + shopDto.getShopLogoUrl());
                    if (shopDto.getDistrictId() != null)
                    {
                        DistrictDto districtDto = (DistrictDto) DataCacheApi.getObject(CommonConst.KEY_DISTRICT
                                + shopDto.getDistrictId());
                        if (districtDto == null)
                        {
                            districtDto = regionDao.getDistrictById((long) shopDto.getDistrictId());
                            if (districtDto != null)
                            {
                                DataCacheApi.setObjectEx(CommonConst.KEY_DISTRICT + shopDto.getDistrictId(),
                                        districtDto, 43200);
                            }
                        }
                        if (districtDto != null)
                        {
                            shopDto.setDistrictName(districtDto.getDistrictName());
                        }
                    }
                    if (shopDto.getTownId() != null)
                    {
                        TownDto townDto = (TownDto) DataCacheApi.getObject(CommonConst.KEY_TOWN + shopDto.getTownId());
                        if (townDto == null)
                        {
                            townDto = townDao.getTownById((long) shopDto.getTownId());
                            if (townDto != null)
                            {
                                DataCacheApi.setObjectEx(CommonConst.KEY_TOWN + shopDto.getTownId(), townDto, 43200);
                            }
                        }
                        if (townDto != null)
                        {
                            shopDto.setTownName(townDto.getTownName());
                        }
                    }
                    Map<String, Object> shopMap = DataConvertUtil.convertObjToMap(shopDto,
                            CommonResultConst.SEARCH_SHOP_RETURN_FIELDS);
                    String shopLogoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopDto.getShopId());
                    if (shopLogoUrl == null)
                    {
                        needQueryShopIdList.add(shopDto.getShopId());
                    }
                    else
                    {
                        shopMap.put("shopLogoUrl", shopLogoUrl);
                    }
                    data.add(shopMap);
                    shopInfoMap.put(shopDto.getShopId(), shopMap);
                }
                else
                {
                    pageModelParam.setTotalItem(pageModelParam.getTotalItem() - 1);
                }
            }
            convertShopLogo(needQueryShopIdList, shopInfoMap);
            if (needQueryActivityShopIdList.size() > 0)
            {
                BusinessAreaActivityDto businessAreaActivityDto = new BusinessAreaActivityDto();
                businessAreaActivityDto.setShopIdList(needQueryActivityShopIdList);
                List<BusinessAreaActivityDto> activityList = busAreaActivityService
                        .getActivityListByCondition(businessAreaActivityDto);
                initActivityRuleNameForShop(activityList, shopInfoMap);
            }
            return data;
        }
        catch (Exception e)
        {
            throw e;
        }

    }

    /**
     * 初始化店铺的活动规则
     * @Title: initActivityRuleNameForShop
     * @param @param activityList
     * @param @param shopInfoMap
     * @return void 返回类型
     * @throws
     */
    private void initActivityRuleNameForShop(List<BusinessAreaActivityDto> activityList,
            Map<Long, Map<String, Object>> shopInfoMap)
    {
        for (BusinessAreaActivityDto dto : activityList)
        {
            Map<String, Object> shopInfoItem = shopInfoMap.get(dto.getShopId());
            if (shopInfoItem != null)
            {
                shopInfoItem.put("activityRuleName", dto.getActivityRuleName());// 活动规则
            }
        }
    }

    /**
     * 获得商铺的Logo
     * @Title: convertShopLogo
     * @param @param needQueryShopIdList
     * @param @param shopInfoMap
     * @param @throws Exception
     * @return void 返回类型
     * @throws
     */
    public void convertShopLogo(List<Long> needQueryShopIdList, Map<Long, Map<String, Object>> shopInfoMap)
            throws Exception
    {
        if (needQueryShopIdList.size() == 0)
        {
            return;
        }
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_SHOP);
        attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
        List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao.findByConditionIn(
                attachmentRelationDto, needQueryShopIdList);
        for (AttachmentRelationDto dto : attachmentRelationDtos)
        {
            Map<String, Object> shopProp = shopInfoMap.get((long) dto.getBizId());
            if (shopProp != null)
            {
                try
                {
                    String shopLogo = FdfsUtil.getFileProxyPath(dto.getFileUrl());
                    shopProp.put("shopLogoUrl", shopLogo);
                    DataCacheApi.setex(CommonConst.KEY_SHOP_LOGO + dto.getBizId(), shopLogo, 86400);// 将商品缩略图放入缓存
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    /**
     * 获取指定商铺的经纬度信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shop/getShopXy", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopXy(HttpServletRequest request)
    {
        logger.info("获取指定商铺经纬度-start");
        try
        {
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            if (null == shopId || "".equals(shopId))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            }
            CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            CommonValidUtil.validStrLongFormat(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_LEN_ERROR_SHOP_ID);
            Map pModel = this.shopServcie.getShopXyById(Long.parseLong(shopId));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取信息成功！", pModel);
        }
        catch (ServiceException e)
        {
            logger.error("获取指定商铺经纬度信息异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取指定商铺经纬度信息异常", e);
            throw new APISystemException("获取指定商铺经纬度信息异常", e);
        }
    }

    /**
     * 用户点赞接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/zan", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getZanNumber(HttpServletRequest request)
    {
        logger.info("用户点赞-start");
        try
        {
            String zanType = RequestUtils.getQueryParam(request, "zanType");
            String bizId = RequestUtils.getQueryParam(request, "bizId");
            String userId = RequestUtils.getQueryParam(request, "userId");
            CommonValidUtil.validStrNull(zanType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ZANTYPE);
            CommonValidUtil.validStrNull(bizId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZID);
            CommonValidUtil.validNumStr(bizId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BIZID);
            CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
            CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
            Map<String, Integer> map = this.shopServcie.praise(zanType, bizId, userId);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCESS_ZAN, map);
        }
        catch (ServiceException e)
        {
            logger.error("用户点赞异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("用户点赞异常", e);
            throw new APISystemException("用户点赞异常", e);
        }
    }

    /**
     * 查询商铺资源动态
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/resource/queryShopResourceSnapshot", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryShopResourceSnapshot(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺资源动态-start");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            List<Map> list = this.shopServcie.queryShopResourceSnapshot(Long.parseLong(shopId));
            MessageListDto msgList = new MessageListDto();
            msgList.setrCount(list.size());
            msgList.setLst(list);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取商铺预定资源列表成功", msgList, DateUtils.DATETIME_FORMAT);
        }
        catch (JsonMappingException e)
        {
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺资源动态-系统异常", e);
            throw new APISystemException("查询商铺资源动态-系统异常", e);
        }
    }

    /**
     * 商铺及商品结果查询
     * 
     * @Title: commonSearch
     * @Description:
     * @param @param request
     * @param @return
     * @param @throws Exception
     * @return PageModel 返回类型
     * @throws
     */
    public PageModel commonSearch(HttpServletRequest request, Integer type, Integer... groupFlag) throws Exception
    {
        String pageNO = RequestUtils.getQueryParam(request, "pNo");
        String pageSize = RequestUtils.getQueryParam(request, "pSize");
        String columnId = RequestUtils.getQueryParam(request, "columnId");// 行业分类
        String userId = RequestUtils.getQueryParam(request, "userId");// 搜索的用户ID
        String cityId = RequestUtils.getQueryParam(request, "cityId");// 所属市ID
        String districtId = RequestUtils.getQueryParam(request, "districtId");// 所属区、县ID
        String searchRadius = RequestUtils.getQueryParam(request, "searchRadius");// 搜索半径
        String townId = RequestUtils.getQueryParam(request, "townId");// 街道，镇id
        String orderBy = RequestUtils.getQueryParam(request, "orderBy");// 排序方式；0-离我最近，1-评价最好，2-订单最多（订单量降序）--新增
                                                                        // 3-折扣排序
        String searchKey = RequestUtils.getQueryParam(request, "searchKey");// 搜索关键字
        String longitude = RequestUtils.getQueryParam(request, "longitude");// 搜索时所处的经度
        String latitude = RequestUtils.getQueryParam(request, "latitude");// 搜索时所处的维度
        String shopMode = RequestUtils.getQueryParam(request, "shopMode");// 商铺模式：restaurant=餐饮开桌,fastrestaurant=餐饮快餐,service=服务,goods=商品,auto=汽修,beauty=美容
        String takeoutFlag = RequestUtils.getQueryParam(request, "takeoutFlag");// 是否开启了外卖
        String goodsServerMode = RequestUtils.getQueryParam(request, "goodsServerMode");// 服务方式：1(代表上门)，2(代表到店),3(代表上门到店)
        String goodsCategoryId = RequestUtils.getQueryParam(request, "goodsCategoryId"); // 商品分类：外键到goods_category表
        String businessAreaActivityFlag = RequestUtils.getQueryParam(request, "businessAreaActivityFlag");// 是否正在发起商圈活动
        String iSNewShop = RequestUtils.getQueryParam(request, "isNewShop"); // 是否是新开商铺
        String isRecommend = RequestUtils.getQueryParam(request, "isRecommend");//是否搜索推荐店铺
        if (StringUtils.isBlank(iSNewShop)) {
            //兼容接口之前定义不规范
            iSNewShop = RequestUtils.getQueryParam(request, "iSNewShop");
        }
        // 搜索来源 0-用户主动搜索 1-系统搜索
        String searchSource = RequestUtils.getQueryParam(request, "searchSource");

        String classifyId = RequestUtils.getQueryParam(request, "classifyId");// 搜索来源
                                                                              // 0用户主动搜索
                                                                              // 1.系统搜索
        //是否采用分房搜索
        boolean doToken = false;
        if (!StringUtils.isBlank(classifyId))
        {
            searchSource = "1";
            String str = DataCacheApi.get(CommonConst.KEY_CLASSIFY_OPERATION_SEARCH + classifyId);
            if (str == null)
            {
                OperationClassifyDto operationClassifyDto = new OperationClassifyDto();
                operationClassifyDto.setClassifyId(Integer.valueOf(classifyId));
                List<OperationClassifyDto> re = operationClassifyService.queryOperationClassifyDto(
                        operationClassifyDto, 10, 1);
                str = re.get(0).getClassifyKey();
//                DataCacheApi.set(CommonConst.KEY_CLASSIFY_OPERATION_SEARCH + classifyId, str);
                DataCacheApi.setex(CommonConst.KEY_CLASSIFY_OPERATION_SEARCH + classifyId, str, 60*60*24);
            }
            searchKey = str;
            doToken = true;
        }
        // 设置默认页码
        if (pageNO == null)
        {
            pageNO = "1";
        }
        if (pageSize == null)
        {
            pageSize = "10";
        }
        // 如果排序方式以及搜索关键字为空,那么默认评价最好
        if (orderBy == null && StringUtils.isEmpty(searchKey))
        {
            orderBy = "1";
        }
        // 验证userId合法性
        if (!StringUtils.isEmpty(userId))
        {
            CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, "userId格式错误");
        }
        // 验证cityId非空以及合法性
        CommonValidUtil.validObjectNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL, "cityId不能为空");
        CommonValidUtil.validNumStr(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, "cityId格式错误");
        SearchContent searchContent = new SearchContent();
        searchContent.setDoToken(doToken);
        if (!StringUtils.isEmpty(iSNewShop) && CommonConst.IS_NEW_SHOP.equals(iSNewShop))
        {// 查询最新加盟店铺
            initNewShopSearchParam(searchContent);
        }
        if (CommonConst.SHOP_SUPPORT_TIME_DISCOUNT == type)
        {// 支持限时折扣
            searchContent.setTimedDiscountFlag(1);
        }
        else if (CommonConst.SHOP_SUPPORT_COUPON == type)
        {// 支持优惠券
            searchContent.setCouponFlag(1);
        }
        else if (CommonConst.SHOP_SUPPORT_CASH_COUPON == type)
        {// 支持代金券
            searchContent.setCashCouponFlag(1);
        }
        /* 这个分组是怎么操作的 */
        searchContent.setGroupLimit(CommonConst.GROUP_SINGLE);// 单个
        if (groupFlag != null && groupFlag.length > 0 && groupFlag[0] != 2)// 表示进行分组查询
        {
            searchContent.setGroupFlag(1);
            searchContent.setGroupLimit(3);// 分组的结果数目
        }
        // 设置店铺状态
        searchContent.setShopStatus(CommonConst.SHOP_STATUS_NORMAL);
        ReflectionUtils.setValueNotEmpty(searchContent, "cityId", cityId);
        ReflectionUtils.setValueNotEmpty(searchContent, "districtId", districtId);
        ReflectionUtils.setValueNotEmpty(searchContent, "goodsServerMode", goodsServerMode);
        // 设置搜索关键字*******
        if (!StringUtils.isEmpty(searchKey) && searchKey.length() == 1)
        {
            ReflectionUtils.setValueNotEmpty(searchContent, "content", searchKey);
        }
        else
        {// ????????????????
            ReflectionUtils.setValueNotEmpty(searchContent, "goodsName", searchKey);
        }
        ReflectionUtils.setValueNotEmpty(searchContent, "townId", townId);
        ReflectionUtils.setValueNotEmpty(searchContent, "shopMode", shopMode);
        // 是否外卖
        ReflectionUtils.setValueNotEmpty(searchContent, "takeoutFlag", takeoutFlag);
        ReflectionUtils.setValueNotEmpty(searchContent, "goodsCategoryId", goodsCategoryId);
        // 设置是否参加商圈活动
        ReflectionUtils.setValueNotEmpty(searchContent, "businessAreaActivityFlag", businessAreaActivityFlag);
        /*
         * if (groupFlag == null || groupFlag.length == 0)// 关键字为空并且不是分组查询则只搜索商铺
         * // //StringUtils.isEmpty(searchKey)&& {
         * searchContent.setContentType(CommonConst.INDEX_TYPE_IS_SHOP); } if
         * ((groupFlag != null && groupFlag.length > 0 && groupFlag[0] == 2)) {
         * searchContent.setContentType(CommonConst.INDEX_TYPE_IS_SHOP);
         * searchContent.setQueryOnlyColumn(1); }
         */
        ReflectionUtils.setValueNotEmpty(searchContent, "shopColumnId", columnId);
        if (groupFlag != null && groupFlag.length > 0 && groupFlag[0] == 1)
        {
            orderBy = null;// 按距离排序
        }
        boolean queryGroupSingleFlag = false;// 分组查询并且每个分组只用一条
        if (CommonConst.SEARCH_BY_OPERATION.equals(searchSource))
        {
            searchContent.setContentType(CommonConst.INDEX_TYPE_IS_SHOP);
            searchContent.setQueryOnlyColumn(1);
        }
        else
        {
            if (!StringUtils.isEmpty(searchKey))
            {// 关键字不为空，需要分组
                searchContent.setGroupFlag(1);
                queryGroupSingleFlag = true;
            }
        }
        if (!StringUtils.isEmpty(orderBy))// 不包括地理位置排序需要排序
        {
            if (CommonConst.LOACTION_ORDERBY.equals(orderBy))
            {
                ReflectionUtils.setFieldValue(searchContent, "orderByLocation", CommonConst.SEARCH_ORDER_ASC);
            }
            else
            {
                int direction = orderBy.equals("3") ? CommonConst.SEARCH_ORDER_DESC : CommonConst.SEARCH_ORDER_ASC;
                // ReflectionUtils.setFieldValue(searchContent,
                // CommonConst.SEARCH_ORDERBY_MAP.get(orderBy),
                // CommonConst.SEARCH_ORDER_ASC);
                ReflectionUtils.setFieldValue(searchContent, CommonConst.SEARCH_ORDERBY_MAP.get(orderBy), direction);
            }
        }

        /* 设置搜索位置范围 */
        if (StringUtils.isEmpty(searchRadius))
        {
            searchRadius = "6371393";
        }
        if (!StringUtils.isEmpty(longitude) && !StringUtils.isEmpty(latitude))
        {
            CommonValidUtil.validDoubleStr(searchRadius, CodeConst.CODE_PARAMETER_NOT_VALID, "搜索半径格式错误");
            searchRadius = (Double.parseDouble(searchRadius) / (double) 1000) + "";
            searchContent.setShopLocation(latitude + "," + longitude);
            searchContent.setSearchRadius(searchRadius);
        }
        else if (StringUtils.isEmpty(longitude) && StringUtils.isEmpty(latitude) && "0".equals(orderBy))
        {// 没有经纬度查同城，按订单量排序
            ReflectionUtils.setFieldValue(searchContent, CommonConst.SEARCH_ORDERBY_MAP.get("2"),
                    CommonConst.SEARCH_ORDER_ASC);
            // 重置按距离排序
            ReflectionUtils.setFieldValue(searchContent, "orderByLocation", 0);
        }
        
        if(!StringUtils.isEmpty(isRecommend)){
            ReflectionUtils.setFieldValue(searchContent, "isRecommend", Integer.valueOf(isRecommend));
        }

        /* 设置分页 */
        PageModel pageModel = new PageModel();
        pageModel.setToPage(Integer.parseInt(pageNO));
        pageModel.setPageSize(Integer.parseInt(pageSize));
        // 如果没有排序规则那么按销量排序
        if (searchContent.getOrderByLocation() == 0 && searchContent.getOrderByMemberDiscount() == 0
                && searchContent.getOrderBySoldNumber() == 0 && searchContent.getOrderByStarLevelGrade() == 0)
        {
            ReflectionUtils.setFieldValue(searchContent, CommonConst.SEARCH_ORDERBY_MAP.get("2"),
                    CommonConst.SEARCH_ORDER_ASC);
        }
        if (groupFlag != null && groupFlag.length > 0 && groupFlag[0] != 2) // 分组查询
        {
            pageModel = SolrContext.getInstance().queryGroupResult(searchContent, pageModel);
        }
        else
        // 不分组查询
        {
            if (queryGroupSingleFlag)
            {
                pageModel = SolrContext.getInstance().queryGroupSingleResult(searchContent, pageModel);
            }
            else
            {
                pageModel = SolrContext.getInstance().queryResult(searchContent, pageModel);
            }
        }
        return pageModel;
    }

    /**
     * 封装最新加盟店铺的参数
     * @Title: initNewShopSearchParam
     * @param @param searchContent
     * @param @throws Exception
     * @return void 返回类型
     * @throws
     */
    public static void initNewShopSearchParam(SearchContent searchContent) throws Exception
    {
        String cacheValue = DataCacheApi.get(CommonConst.NEW_SHOP_JUDGE_PARAM_NAME);
        String newDayParamValue = null;
        if (StringUtils.isEmpty(cacheValue))
        {
            ICommonDao commonDao = BeanFactory.getBean(ICommonDao.class);
            newDayParamValue = commonDao.getConfigValueByKey(CommonConst.NEW_SHOP_JUDGE_PARAM_NAME);
            if (StringUtils.isEmpty(newDayParamValue))
            {
                newDayParamValue = "30";
            }
            DataCacheApi.set(CommonConst.NEW_SHOP_JUDGE_PARAM_NAME, newDayParamValue);
        }
        Long currentDateSecondMill = new Date().getTime();
        currentDateSecondMill = currentDateSecondMill - 1000 * 60 * 60 * 24
                * Long.parseLong(newDayParamValue == null ? "30" : newDayParamValue);
        searchContent.setAuditTimeNum(currentDateSecondMill);
    }

    /**
     * 通用的商铺结果查询
     * 
     * @Title: commonSearchShop
     * @Description:
     * @param @param request
     * @param @return
     * @param @throws Exception
     * @return PageModel 返回类型
     * @throws
     */
    public PageModel commonSearchShop(HttpServletRequest request, Integer searchType) throws Exception
    {
        IRegionDao regionDao = BeanFactory.getBean(IRegionDao.class);
        try
        {
            PageModel pageModel = commonSearch(request, searchType);
            if (pageModel != null)
            {
                List<SearchContent> data = pageModel.getList();
                List<ShopDto> shopDtos = new ArrayList<ShopDto>();
                if (data != null)
                {
                    for (SearchContent searchContent : data)
                    {
                        ShopDto shopDto = new ShopDto();
                        DataConvertUtil.propertyConvertIncludeDefaultProp(searchContent, shopDto,
                                CommonConst.SEARCH_SHOPS_MAP);
                        if (shopDto.getDistance() != null)
                        {
                            shopDto.setDistance(shopDto.getDistance() * 1000);
                        }
                        if (shopDto.getDistrictId() != null && shopDto.getDistrictId() != 0)
                        {
                            DistrictDto districtDto = (DistrictDto) DataCacheApi.getObject("district:"
                                    + shopDto.getDistrictId());
                            if (districtDto != null)
                            {
                                shopDto.setDistrictName(districtDto.getDistrictName());
                            }
                            else
                            {
                                if (regionDao != null)
                                {
                                    districtDto = regionDao.getDistrictById((long) shopDto.getDistrictId());
                                    if (districtDto != null)
                                    {
                                        shopDto.setDistrictName(districtDto.getDistrictName());
                                    }
                                }
                            }
                        }
                        shopDtos.add(shopDto);
                    }
                }
                pageModel.setList(shopDtos);
            }
            return pageModel;
        }
        catch (Exception e)
        {
            logger.error("商铺搜索产生了一场", e);
            throw e;
        }
    }

    /**
     * 搜索商铺限时折扣
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/searchShopTimeDiscount", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchShopTimeDiscount(HttpServletRequest request)
    {
        try
        {
            logger.info("搜索商铺限时折扣-start");
            long start = System.currentTimeMillis();
            PageModel model = commonSearchShop(request, CommonConst.SHOP_SUPPORT_TIME_DISCOUNT);
            if (null == model || (model.getList() == null) || (model.getList().size() <= 0))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "未查询到相关信息");
            }
            String longitude = RequestUtils.getQueryParam(request, "longitude");// 搜索时所处的经度
            String latitude = RequestUtils.getQueryParam(request, "latitude");// 搜索时所处的维度
            boolean isDistance = (!StringUtils.isBlank(latitude) && !StringUtils.isBlank(longitude));
            Map pModel = shopTimeDiscountService.searchShopTimeDiscount(model, isDistance);
            logger.info("查询成功，共耗时：" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取商铺限时折扣信息成功", pModel, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("搜索商铺限时折扣-异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("搜索商铺限时折扣-系统异常", e);
            throw new APISystemException("搜索商铺限时折扣-系统异常", e);
        }
    }

    /**
     * 
     * @Title: searchShopCashCoupon
     * @Description: (搜索商铺代金卷接口)
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "shop/searchShopCashCoupon", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String searchShopCashCoupon(HttpServletRequest request)
    {
        try
        {
            PageModel pageModel = commonSearchShop(request, CommonConst.SHOP_SUPPORT_CASH_COUPON);
            MessageListDto msgList = new MessageListDto();
            if (pageModel != null)
            {
                List<ShopDto> shopDtos = pageModel.getList();
                List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
                for (ShopDto shopDto : shopDtos)
                {
                    List<CashCouponDto> cashCouponList = cashCouponService.getCashCouponByShopId(shopDto.getShopId());// 根据商铺查找该商铺适用的代金卷
                    Map<String, Object> recordMapItem = DataConvertUtil.convertObjToMap(shopDto,
                            CommonResultConst.GET_SHOP_CASH_SHOP_FIELDS);// 将需要返回的字段转化为Map
                    recordMapItem.put("cashCoupons", DataConvertUtil.convertListObjToMap(cashCouponList,
                            CommonResultConst.GET_SHOP_CASH_COUPONS));// 将代金卷转化为需要返回的字段
                    resultMap.add(recordMapItem);
                }
                pageModel.setList(resultMap);
                msgList.setpNo(pageModel.getToPage());
                msgList.setpSize(pageModel.getPageSize());
                msgList.setLst(resultMap);
                msgList.setrCount(pageModel.getTotalItem());
                List<MessageListDto> dataList = new ArrayList<MessageListDto>();
                dataList.add(msgList);
                return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "搜索店铺代金卷成功!", msgList, DateUtils.DATE_FORMAT);
            }
            else
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "搜索店铺代金卷成功!", msgList);
            }
        }
        catch (JsonMappingException e)
        {
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺代金卷-系统异常", e);
            throw new APISystemException("查询商铺代金卷-系统异常", e);
        }
    }

    /**
     * 搜索店铺优惠券
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/searchShopCoupon", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String searchShopCoupon(HttpServletRequest request)
    {
        try
        {
            PageModel pageModel = commonSearchShop(request, CommonConst.SHOP_SUPPORT_COUPON);
            MessageListDto msgList = new MessageListDto();
            if (pageModel != null)
            {
                List<ShopDto> shopDtos = pageModel.getList();
                List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
                CouponDto coupon = null;
                for (ShopDto shopDto : shopDtos)
                {
                    List<CouponDto> couponList = couponService.getShopCouponListById(shopDto.getShopId());// 根据商铺查找该商铺适用的优惠卷
                    Map<String, Object> recordMapItem = DataConvertUtil.convertObjToMap(shopDto,
                            CommonResultConst.GET_SHOP_COUPON_FIELDS);// 将需要返回的字段转化为Map
                    recordMapItem.put("coupons",
                            DataConvertUtil.convertListObjToMap(couponList, CommonResultConst.GET_SHOP_COUPON));// 将代金卷转化为需要返回的字段
                    resultMap.add(recordMapItem);
                }
                pageModel.setList(resultMap);
                msgList.setpNo(pageModel.getToPage());
                msgList.setpSize(pageModel.getPageSize());
                msgList.setLst(resultMap);
                msgList.setrCount(pageModel.getTotalItem());
                List<MessageListDto> dataList = new ArrayList<MessageListDto>();
                dataList.add(msgList);
                return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "搜索店铺优惠券成功", msgList, DateUtils.DATE_FORMAT);
            }
            else
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "搜索店铺优惠券成功", msgList);
            }
        }
        catch (JsonMappingException e)
        {
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("搜索店铺优惠券-系统异常", e);
            throw new APISystemException("搜索店铺优惠券-系统异常", e);
        }
    }

    /**
     * 生成搜索历史记录
     * 
     * @Title: generateSearchHistory
     * @Description:
     * @param @param request
     * @return void 返回类型
     * @throws
     */
    public static void generateSearchHistory(HttpServletRequest request) throws Exception
    {
        final IMemberServcie memService = BeanFactory.getBean(IMemberServcie.class);
        final String searchKey = RequestUtils.getQueryParam(request, "searchKey");// 搜索关键字
        final String userId = RequestUtils.getQueryParam(request, "userId");// 搜索的用户ID
        new Thread()
        {
            public void run()
            {
                if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(searchKey))// 判断用户id和搜索关键字时候为空
                {
                    try
                    {
                        CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, "userId格式错误");
                        UserDto user = (UserDto) HandleCacheUtil.getEntityCacheByClass(UserDto.class, userId, 21600);
                        if (user == null)
                        {
                            return;
                        }
                        UserSearchHistory userSearchHistory = new UserSearchHistory();
                        userSearchHistory.setCreateTime(new Date());
                        userSearchHistory.setUserId(Long.parseLong(userId));
                        userSearchHistory.setSearchContent(searchKey);
                        int count = memService.getCountByUserIdAndSearchContent(userSearchHistory);
                        if (count == 0)
                        {
                            memService.insertUserSearchHistory(userSearchHistory);
                        }
                        else
                        {
                            memService.updateUserSearchHistory(userSearchHistory);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 获取商铺外卖费用设置
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getShopTakeoutFeeSetting", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopTakeoutFeeSetting(HttpServletRequest request)
    {
        logger.info("获取商铺外卖费用设置-start");
        try
        {
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String settingType = RequestUtils.getQueryParam(request, "settingType");
            CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            if (!StringUtils.isBlank(settingType))
            {
                // 校验数据
                CommonValidUtil.validStrIntFormat(settingType, CodeConst.CODE_PARAMETER_NOT_NULL,
                        CodeConst.MSG_FORMAT_ERROR_TAKEOUT_SETTYPE);
            }
            else
            {
                // 默认为外卖规则
                settingType = CommonConst.TAKEOUT_SETTINGTYPE_WM + "";
            }
            // Map<String,Object> takeoutSet =
            // this.takeoutSetService.getTakeoutSetByShopId(Long.valueOf(shopId));

            DistribTakeoutSettingDto dts = this.distribTakeoutSettingService.getDistribTakeoutSetting(
                    Long.valueOf(shopId), Integer.valueOf(settingType));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("obj", dts);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCESS_GET_TAKEOUT, map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取商铺外卖费用设置-系统异常", e);
            throw new APISystemException("获取商铺外卖费用设置-系统异常", e);
        }
    }

    // 获取店铺外卖旧逻辑 comment by lujianping date 20150714
    // @RequestMapping(value="/shop/getShopTakeoutFeeSetting",produces =
    // "application/json;charset=UTF-8")
    // @ResponseBody
    // public Object getShopTakeoutFeeSetting(HttpServletRequest request) {
    // logger.info("获取商铺外卖费用设置-start");
    // try {
    // String shopId = RequestUtils.getQueryParam(request, "shopId");
    // CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_SHOPID);
    // CommonValidUtil.validPositLong(shopId,
    // CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
    // Map<String,Object> takeoutSet =
    // this.takeoutSetService.getTakeoutSetByShopId(Long.valueOf(shopId));
    // Map<String,Object> map = new HashMap<String, Object>();
    // map.put("obj", takeoutSet);
    // return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
    // CodeConst.MSG_SUCCESS_GET_TAKEOUT, map);
    // } catch (ServiceException e) {
    // throw new APIBusinessException(e);
    // } catch (Exception e) {
    // logger.error("获取商铺外卖费用设置-系统异常", e);
    // throw new APISystemException("获取商铺外卖费用设置-系统异常", e);
    // }
    // }

    /**
     * 获取通用预定设置费用
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "shop/getShopBooktFeeSetting", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopBooktFeeSetting(HttpServletRequest request)
    {
        try
        {
            logger.info("获取通用预定设置费用-start");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            PageModel pageModel = shopServcie.getShopBooktFeeSetting(shopId, pSize, pNo);
            List list = pageModel.getList();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pNo", pNo);
            map.put("rCount", pageModel.getTotalItem());
            map.put("lst", list);
            logger.info("获取通用预定设置费用-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取信息成功！", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取通用预定设置费用-系统异常", e);
            throw new APISystemException("获取通用预定设置费用-系统异常", e);
        }
    }

    /**
     * 查询商铺基本信息接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "shop/getShopAccountMoney", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopAccountMoney(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺账户基本信息-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            
            Map<String, Object> parms = new HashMap<String, Object>();
            parms.put("shopId",getIdListsByHeadShopId(shopId));
            Map<String, Object> map = shopServcie.getShopAccountMoney(parms);
            
            if (null == map)
            {
                // CommonValidUtil
                // .validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
                // "商铺账号不存在");
                map = new HashMap<String, Object>();
                map.put("amount", 0);
                map.put("freezeAmount", 0);
                map.put("onlineIncomeAmount", 0);
                map.put("rewardAmount", 0);
                map.put("bailAmount", 0);
                map.put("incomeTotal", 0);
                map.put("rewardTotal", 0);
            }
            logger.info("查询商铺账户基本信息-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询商铺账户余额成功！", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺账户基本信息-系统异常", e);
            throw new APISystemException("查询商铺账户基本信息-系统异常", e);
        }
    }

    /**
     * 查询商铺账单接口[新增接口]
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "shop/getShopBill", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopBill(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺账户基本信息-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String billType = RequestUtils.getQueryParam(request, "billType");
            String billStatus = RequestUtils.getQueryParam(request, "billStatus");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();
            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", shopId);
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // billType
            if (StringUtils.isNotBlank(billType))
            {
                CommonValidUtil.validStrIntFormat(billType, CodeConst.CODE_PARAMETER_NOT_VALID, "billType格式错误");
                map.put("billType", billType);
            }
            // billStatus
            if (StringUtils.isNotBlank(billStatus))
            {
                CommonValidUtil.validStrIntFormat(billStatus, CodeConst.CODE_PARAMETER_NOT_VALID, "billStatus格式错误");
                map.put("billStatus", billStatus);
            }
            // startTime
            if (StringUtils.isNotBlank(startTime))
            {
                CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
                map.put("startTime", startTime);
            }
            // endTime
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                map.put("endTime", endTime);
            }
            PageModel pageModel = shopServcie.getShopBill(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("查询商铺账户基本信息-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询商铺账户余额成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺账户基本信息-系统异常", e);
            throw new APISystemException("查询商铺账户基本信息-系统异常", e);
        }
    }

    /**
     * 查询商铺的提现记录
     * 
     * @Title: getShopWithdrawList
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value =
    { "/token/shop/getShopWithdrawList", "/session/shop/getShopWithdrawList", "/service/shop/getShopWithdrawList",
            "/shop/getShopWithdrawList" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopWithdrawList(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺的提现记录-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            /* 20160510新增提现流水号 */
            String withdrawId = RequestUtils.getQueryParam(request, "withdrawId");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();
            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", getIdListsByHeadShopId(shopId));
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // startTime
            Date newTime = new Date();
            if (StringUtils.isNotBlank(startTime))
            {
                CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
                map.put("startTime", startTime + " 00:00:00");
            }
            else
            {
                Calendar c = Calendar.getInstance();
                c.setTime(newTime);
                c.add(Calendar.DATE, -7);// 为空时，一周内信息
                startTime = DateUtils.format(c.getTime(), DateUtils.ZEROTIME_FORMAT);
                map.put("startTime", startTime);
            }
            // endTime
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                map.put("endTime", endTime + " 23:59:59");
            }
            else
            {
                endTime = DateUtils.format(newTime, "yyyy-MM-dd 23:59:59");
                map.put("endTime", endTime);
            }
            /* 20160510新增提现流水号 */
            if(StringUtils.isNotBlank(withdrawId)){
                map.put("withdrawId", CommonValidUtil.validStrIntFmt(withdrawId.trim(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_WITHDRAWID));
            }
            PageModel pageModel = payServcie.getShopWithdrawList(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("查询商铺的提现记录-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取提现信息成功！", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺的提现记录-系统异常", e);
            throw new APISystemException("查询商铺的提现记录-系统异常", e);
        }
    }

    /**
     * 查询商铺的充值记录
     * 
     * @Title: getShopWithdrawList
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value =
    { "/shop/getShopRechargeList", "/service/shop/getShopRechargeList", "/token/shop/getShopRechargeList",
            "/session/shop/getShopRechargeList" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopRechargeList(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺充值记录-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            /* 20160510添加内部交易号查询条件 */
            String transactionId = RequestUtils.getQueryParam(request, "transactionId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();
            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", getIdListsByHeadShopId(shopId));
            /* 20160510添加内部交易号查询条件 */
            if(StringUtils.isNotBlank(transactionId)){
                map.put("transactionId",  CommonValidUtil.validStrIntFmt(transactionId.trim(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_TRANSACTIONID));
            }
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // startTime
            Date newTime = new Date();
            if (StringUtils.isNotBlank(startTime))
            {
                CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
                map.put("startTime", startTime + " 00:00:00");
            }
            else
            {
                Calendar c = Calendar.getInstance();
                c.setTime(DateUtils.getZeroTimeOfCurDate());
                c.add(Calendar.DATE, -7);// 为空时，一周内信息
                startTime = DateUtils.format(c.getTime(), DateUtils.ZEROTIME_FORMAT);
                map.put("startTime", startTime);
            }
            // endTime
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                map.put("endTime", endTime + " 23:59:59");
            }
            else
            {
                endTime = DateUtils.format(newTime, "yyyy-MM-dd 23:59:59");
                map.put("endTime", endTime);
            }
            PageModel pageModel = payServcie.getShopRechargeList(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("查询商铺充值记录-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取充值信息成功！", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺充值记录-系统异常", e);
            throw new APISystemException("查询商铺充值记录-系统异常", e);
        }
    }

    /**
     * 查询商铺奖励接口
     * 
     * @Title: getShopWithdrawList
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "shop/getShopAward", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopAward(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺ID year int 是 指定年份,默认当前年份 month int 是 月份 type int
             * 0 否 被推荐人类型： 0：全部，1：会员，2：店铺，默认0 searchContent varchar 否
             * 搜索项：根据搜索项(被推荐人账号/商铺名称/商铺简称)进行搜索 pNo int 1 否 页码,从第1页开始 pSize int
             * 20 否 每页记录数
             */
            logger.info("查询商铺奖励-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String yearStr = RequestUtils.getQueryParam(request, "year");
            String monthStr = RequestUtils.getQueryParam(request, "month");
            String typeStr = RequestUtils.getQueryParam(request, "type");
            String searchContent = RequestUtils.getQueryParam(request, "searchContent");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();

            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", shopId);
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // year
            // CommonValidUtil.validStrNull(yearStr,
            // CodeConst.CODE_PARAMETER_NOT_NULL, "year不能为空");
            if (StringUtils.isBlank(yearStr))
            {
                // 为空，当前年
                Calendar a = Calendar.getInstance();
                map.put("year", a.get(Calendar.YEAR));
            }
            else
            {
                Integer year = CommonValidUtil.validStrIntFmt(yearStr, CodeConst.CODE_PARAMETER_NOT_VALID, "year格式错误");
                map.put("year", year);
            }
            // month
            CommonValidUtil.validStrNull(monthStr, CodeConst.CODE_PARAMETER_NOT_NULL, "month不能为空");
            Integer month = CommonValidUtil.validStrIntFmt(monthStr, CodeConst.CODE_PARAMETER_NOT_VALID, "month格式错误");
            map.put("month", month);
            // type
            if (StringUtils.isBlank(typeStr))
            {
                map.put("type", 0);
            }
            else
            {
                Integer type = CommonValidUtil.validStrIntFmt(typeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "type格式错误");
                map.put("type", type);
            }
            // searchContent
            map.put("searchContent", searchContent);

            PageModel pageModel = shopServcie.getShopAward(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("查询商铺奖励-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取账单成功!", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺奖励-系统异常", e);
            throw new APISystemException("查询商铺奖励-系统异常", e);
        }
    }

    /**
     * 查询商铺奖励接口
     * 
     * @Title: getShopWithdrawList
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "shop/getShopAwardTotal", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getShopAwardTotal(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺ID year int 是 指定年份,默认当前年份 month int 是 月份 type int
             * 0 否 被推荐人类型： 0：全部，1：会员，2：店铺，默认0 searchContent varchar 否
             * 搜索项：根据搜索项(被推荐人账号/商铺名称/商铺简称)进行搜索 pNo int 1 否 页码,从第1页开始 pSize int
             * 20 否 每页记录数
             */
            logger.info("查询商铺奖励总额-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String yearStr = RequestUtils.getQueryParam(request, "year");
            String monthStr = RequestUtils.getQueryParam(request, "month");
            String typeStr = RequestUtils.getQueryParam(request, "type");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();

            // 验证商铺
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", shopId);
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // year
            CommonValidUtil.validStrNull(yearStr, CodeConst.CODE_PARAMETER_NOT_NULL, "year不能为空");
            Integer year = CommonValidUtil.validStrIntFmt(yearStr, CodeConst.CODE_PARAMETER_NOT_VALID, "year格式错误");
            map.put("year", year);
            // month
            CommonValidUtil.validStrNull(yearStr, CodeConst.CODE_PARAMETER_NOT_NULL, "month不能为空");
            Integer month = CommonValidUtil.validStrIntFmt(monthStr, CodeConst.CODE_PARAMETER_NOT_VALID, "month格式错误");
            map.put("month", month);
            // type
            if (StringUtils.isBlank(typeStr))
            {
                map.put("type", 0);
            }
            else
            {
                Integer type = CommonValidUtil.validStrIntFmt(typeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "type格式错误");
                map.put("type", type);
            }
            Map<String, Object> resultMap = shopServcie.getShopAwardTotal(map);
            logger.info("查询商铺奖励总额-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取账单成功!", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺奖励总额-系统异常", e);
            throw new APISystemException("查询商铺奖励总额-系统异常", e);
        }
    }

    /**
     * 查询商铺订单数接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getShopOrder")
    @ResponseBody
    public ResultDto getShopOrder(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺订单数- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
            Map<String, Object> map = new HashMap<String, Object>();
            // 验证商铺格式
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            
            // cashierId
            if (StringUtils.isNotBlank(cashierIdStr))
            {
                Long cashierId = CommonValidUtil.validStrLongFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "cashierId格式错误");
                map.put("cashierId", cashierId);
            }
            // startTime 为空时为当天0点
            Date newTime = new Date();
            if (StringUtils.isNotBlank(startTime))
            {
                CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
                map.put("startTime", startTime);
            }
            else
            {
                startTime = DateUtils.format(newTime, "yyyy-MM-dd 00:00:00");
                map.put("startTime", startTime);
            }
            // endTime 为空时为当天23点59分
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                map.put("endTime", endTime);
            }
            else
            {
                endTime = DateUtils.format(newTime, "yyyy-MM-dd 23:59:59");
                map.put("endTime", endTime);
            }
            
            // 商铺存在性 
            ShopDto shopDto = this.shopServcie.getShopById(shopId);
            CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            map.put("shopId", shopId);
            
            Map<String, Object> resultMap = shopServcie.queryShopOrderCount(map);
            logger.info("查询商铺订单数- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取订单信息成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺订单数 - 系统异常", e);
            throw new APISystemException("查询商铺订单数 - 系统异常", e);
        }
    }
    
    private List<Long> getIdListsByHeadShopId(Long headShopId) throws Exception{
    	
    	List<Long> shopIdList = new ArrayList<Long>();
    	
        List<ShopDto> shopList = shopServcie.getShopListByHeadShopId(headShopId);
        if(CollectionUtils.isNotEmpty(shopList)){
        	for (ShopDto shopDto : shopList) {
        		shopIdList.add(shopDto.getShopId());
			}
        }
        shopIdList.add(headShopId);
		return shopIdList;

    }

    /**
     * 查询图片logo接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getBizLogo")
    @ResponseBody
    public ResultDto getBizLogo(HttpServletRequest request)
    {
        try
        {
            logger.info("查询图片logo- start");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
            String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
            String picType = RequestUtils.getQueryParam(request, "picType");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();

            if (StringUtils.isNotBlank(picType))
            {
                CommonValidUtil.validNumStr(picType, CodeConst.CODE_PARAMETER_NOT_VALID, "bizIndex格式错误");
            }
            // bizType
            CommonValidUtil.validStrNull(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizType不能为空");

            Integer bizType = CommonValidUtil.validStrIntFmt(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    "bizType格式错误");
            // 验证bizId
            CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId不能为空");
            Long bizId = CommonValidUtil.validStrLongFmt(bizIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "bizIndex格式错误");
            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            map.put("bizId", bizId);
            map.put("bizType", bizType);
            map.put("picType", picType);
            PageModel pageModel = shopServcie.getBizLogo(map);
            // 返回结果
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("pNo", pNo);
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("lst", pageModel.getList());
            logger.info("查询图片logo- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取logo成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询图片logo - 系统异常", e);
            throw new APISystemException("查询图片logo - 系统异常", e);
        }
    }

    /**
     * 增加/更新技师
     * 
     * @Function: com.idcq.appserver.controller.shop.ShopController.placeOrder
     * @Description:
     * 
     * @param entity
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年7月30日 下午2:55:36
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年7月30日 ChenYongxin v1.0.0 create
     */
    @RequestMapping(value =
    { "/token/shop/addTechnician", "/addTechnician" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto addTechnician(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("增加/更新技师-start" + entity.toString());
            ShopTechnicianDto shopTechnicianDto = (ShopTechnicianDto) JacksonUtil.postJsonToObj(entity,
                    ShopTechnicianDto.class, DateUtils.DATETIME_FORMAT);
            // 工号
            CommonValidUtil
                    .validStrNull(shopTechnicianDto.getWorkNumber(), CodeConst.CODE_PARAMETER_NOT_NULL, "工号不能为空");
            // 首字母
            CommonValidUtil.validStrNull(shopTechnicianDto.getTechnicianSimpleName(),
                    CodeConst.CODE_PARAMETER_NOT_NULL, "姓名首字母不能为空");
            // shopId
            Long shopId = shopTechnicianDto.getShopId();
            if (null == shopId)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            }
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (shopDto == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }
            // operateType
            Integer operateType = shopTechnicianDto.getOperateType();
            if (null == operateType)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operateType不能为空");
            }
            // 操作类型：删除，删除的时候techid必填
            if (1 == operateType)
            {
                Long techId = shopTechnicianDto.getTechId();
                if (null == techId)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "techId不能为空");
                }
            }
            // techName
            String techName = shopTechnicianDto.getTechName();
            CommonValidUtil.validStrNull(techName, CodeConst.CODE_PARAMETER_NOT_NULL, "techName不能为空");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("workNumber", shopTechnicianDto.getWorkNumber());
            map.put("shopId", shopTechnicianDto.getShopId());
            Integer technicianId = shopTechnicianService.validateTechExit(map);
            if (operateType.equals(1))
            {
                // 修改操作。如果不等于当前自己的，重复了的话，不能修改、
                if (technicianId != null && !technicianId.equals(shopTechnicianDto.getTechId().intValue()))
                {
                    return ResultUtil.getResult(CodeConst.CODE_TECH_WORK_NUMBER_IS_EXIT, "该工号已存在", null);
                }
            }
            else
            {
                // 新增操作，如果
                if (technicianId != null)
                {
                    return ResultUtil.getResult(CodeConst.CODE_TECH_WORK_NUMBER_IS_EXIT, "该工号已存在", null);
                }
            }
            Long techId = iShopTechnicianService.insertAndUpdateShopTechnician(shopTechnicianDto);
            map.clear();
            map.put("techId", techId);
            if (operateType.equals(1))
            {
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "修改技师成功！", map);
            }
            else
            {
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "添加技师成功！", map);
            }
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("增加/更新技师-系统异常", e);
            throw new APISystemException("增加/更新技师-系统异常", e);
        }
    }

    /**
     * MS3：删除技师接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/token/shop/delTechnician", "/delTechnician" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto delTechnician(HttpServletRequest request)
    {
        try
        {
            logger.info("MS3：删除技师接口- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String techIds = RequestUtils.getQueryParam(request, "techIds");
            // TODO 目前operateType只用来标记删除，后续有更新操作使用
            String operateType = RequestUtils.getQueryParam(request, "operateType");
            CommonValidUtil.validStrNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, "operateType不能为空");
            // bizType
            CommonValidUtil.validStrNull(techIds, CodeConst.CODE_PARAMETER_NOT_NULL, "techIds不能为空");
            // shopId
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 商铺存在性
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            shopTechnicianService.deleteShopTechnician(shopId, techIds);
            logger.info("MS3：删除技师接口- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除技师成功！", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("MS3：删除技师接口 - 系统异常", e);
            throw new APISystemException("MS3：删除技师接口 - 系统异常", e);
        }
    }

    /**
     * 获取商铺技师服务项目接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getTechnicianServiceItems")
    @ResponseBody
    public ResultDto getTechnicianServiceItems(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId techId goodsServerMode pNo pSize
             */
            logger.info("获取商铺技师服务项目- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String techIdStr = RequestUtils.getQueryParam(request, "techId");
            String goodsServerModeStr = RequestUtils.getQueryParam(request, "goodsServerMode");
            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /***** 参数校验 ******/
            // 非空
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(techIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "techId不能为空");
            CommonValidUtil.validStrNull(goodsServerModeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsServerMode不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            // 技师id
            Integer techId = NumberUtil.strToNum(techIdStr, "techIdStr");
            paramMap.put("techId", techId);
            // 服务类型
            Integer goodsServerMode = NumberUtil.strToNum(goodsServerModeStr, "goodsServerMode");
            paramMap.put("goodsServerMode", goodsServerMode);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            paramMap.put("pSize", pSize);
            paramMap.put("skip", (pNo - 1) * pSize);
            paramMap.put("shopId", shopId);
            PageModel pageModel = shopTechnicianService.getTechnicianServiceItems(paramMap);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("pNo", pNo);
            resultMap.put("rcount", pageModel.getTotalItem());
            logger.info("获取商铺技师服务项目- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取技师服务列表成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺技师服务项目 - 系统异常", e);
            throw new APISystemException("获取商铺技师服务项目 - 系统异常", e);
        }
    }

    /**
     * 获取商铺技师接单
     * 
     * @Function: 
     *            com.idcq.appserver.controller.shop.ShopController.getScheduleSetting
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年7月31日 上午11:51:14
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年7月31日 ChenYongxin v1.0.0 create
     */
    @RequestMapping(value = "/shopManage/getTechReserves")
    @ResponseBody
    public ResultDto getTechReserves(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺Id queryDate date 是 日期 techId int 是 技师ID times
             * string 否 时间点[新增字段]，格式为hh-mm,如08:00
             */
            logger.info(" 获取商铺技师接单- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String techIdStr = RequestUtils.getQueryParam(request, "techId");
            String queryDateStr = RequestUtils.getQueryParam(request, "queryDate");
            String timesStr = RequestUtils.getQueryParam(request, "times");
            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /***** 参数校验 ******/
            // shopId
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 技师id
            CommonValidUtil.validStrNull(techIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "techId不能为空");
            Integer techId = NumberUtil.strToNum(techIdStr, "techId");
            paramMap.put("techId", techId);
            // 日期
            CommonValidUtil.validStrNull(queryDateStr, CodeConst.CODE_PARAMETER_NOT_NULL, "queryDate不能为空");
            Date queryDate = DateUtils.validDateStr(queryDateStr, DateUtils.DATE_FORMAT);
            // 时间点
            if (StringUtils.isNotBlank(timesStr))
            {
                DateUtils.validDateStr(timesStr, DateUtils.TIME_FORMAT2);
                paramMap.put("times", timesStr);
            }
            paramMap.put("queryDate", queryDate);
            paramMap.put("shopId", shopId);
            List<Map<String, Object>> resultMap = shopTechnicianService.getScheduleSetting(paramMap);
            logger.info(" 获取商铺技师接单- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取排班成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(" 获取商铺技师接单 - 系统异常", e);
            throw new APISystemException("获取商铺技师接单 - 系统异常", e);
        }
    }

    /**
     * 获取商铺技师接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/token/shop/getTechnicianList", "/getTechnicianList" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto getTechnicianList(HttpServletRequest request)
    {
        try
        {
            logger.info("获取商铺技师- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String techIdStr = RequestUtils.getQueryParam(request, "techId");
            String goodsGroupIdStr = RequestUtils.getQueryParam(request, "goodsGroupId");
            String serviceTime = RequestUtils.getQueryParam(request, "serviceTime");
            String techTypeIdStr = RequestUtils.getQueryParam(request, "techTypeId");
            String orderByStr = RequestUtils.getQueryParam(request, "orderBy");
            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /***** 参数校验 ******/
            // userId
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 校验对象是否为空
            // CommonValidUtil.validObjectNull(userDB,
            // CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            // 技师id
            if (StringUtils.isNotBlank(techIdStr))
            {
                Integer techId = NumberUtil.strToNum(techIdStr, "techIdStr");
                paramMap.put("techId", techId);
            }
            // group
            if (StringUtils.isNotBlank(goodsGroupIdStr))
            {
                Long goodsGroupId = NumberUtil.strToLong(goodsGroupIdStr, "goodsGroupId");
                paramMap.put("goodsGroupId", goodsGroupId);
            }
            // 服务时间
            if (StringUtils.isNotBlank(serviceTime))
            {
                DateUtils.validDateStr(serviceTime, DateUtils.TIME_FORMAT2);
                paramMap.put("serviceTime", serviceTime);
            }
            // techType
            if (StringUtils.isNotBlank(techTypeIdStr))
            {
                Integer techTypeId = NumberUtil.strToNum(techTypeIdStr, "techTypeId");
                paramMap.put("techTypeId", techTypeId);
            }
            // orderby排序
            if (StringUtils.isNotBlank(orderByStr))
            {
                Integer orderBy = NumberUtil.strToNum(orderByStr, "orderBy");
                paramMap.put("orderBy", orderBy);
            }
            else
            {
                paramMap.put("orderBy", 0);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            paramMap.put("pSize", pSize);
            paramMap.put("skip", (pNo - 1) * pSize);
            paramMap.put("shopId", shopId);
            PageModel pageModel = shopTechnicianService.getTechnicianList(paramMap);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("pNo", pNo);
            resultMap.put("rCount", pageModel.getTotalItem());
            logger.info("获取商铺技师- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取技师列表成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺技师 - 系统异常", e);
            throw new APISystemException("获取商铺技师 - 系统异常", e);
        }
    }

    /**
     * S31：获取商铺资源接口[新增](场地类)
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/goods/getShopCategoryResource")
    @ResponseBody
    public ResultDto getShopCategoryResource(HttpServletRequest request)
    {
        try
        {
            logger.info("S31：获取商铺资源接口- start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String categoryIdStr = RequestUtils.getQueryParam(request, "categoryId");
            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /***** 参数校验 ******/
            // userId
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "categoryId不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            Long categoryId = NumberUtil.strToLong(categoryIdStr, "categoryId");
            // 校验对象是否为空
            int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            paramMap.put("categoryId", categoryId);
            paramMap.put("shopId", shopId);
            List<Map<String, Object>> resultList = shopRsrcServcie.getShopCategoryResource(paramMap);
            Integer resultCount = 0;
            if (CollectionUtils.isNotEmpty(resultList))
            {
                resultCount = resultList.size();
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", resultList);
            resultMap.put("rCount", resultCount);
            logger.info("S31：获取商铺资源接口- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺资源成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("S31：获取商铺资源接口[新增](场地类) - 系统异常", e);
            throw new APISystemException("S31：获取商铺资源接口 - 系统异常", e);
        }
    }

    /**
     * 添加/修改/删除场地号接口
     * 
     * @Function: 
     *            com.idcq.appserver.controller.shop.ShopController.operateResource
     * @Description:
     * 
     * @param entity
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年8月1日 上午11:44:06
     * 
     *                 Modification History: Date Author Version Description
     *                 ----
     *                 ------------------------------------------------------
     *                 ------- 2015年8月1日 ChenYongxin v1.0.0 create
     */
    @RequestMapping(value = "/shopManage/operateResource", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResultDto operateResource(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺Id categoryId int 是 分类ID operateType int 0 否
             * 操作类型：0-新增，1-修改，2-删除 当1或2是，resourceId必填 resourceId int 0 条件 资源ID
             * resourceName String 条件 场地名称
             */
            logger.info("添加/修改/删除场地号接口-start");
            ShopRsrcPramDto shopPramDto = (ShopRsrcPramDto) JacksonUtil.postJsonToObj(entity, ShopRsrcPramDto.class,
                    DateUtils.DATETIME_FORMAT);
            // 校验
            Long shopId = shopPramDto.getShopId();
            if (null == shopId)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            }
            // categoryId
            Long categoryId = shopPramDto.getCategoryId();
            if (null == categoryId)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "categoryId不能为空");
            }
            Integer operateType = shopPramDto.getOperateType();
            if (null == operateType || 0 == operateType)
            {
                operateType = 0;
                shopPramDto.setOperateType(operateType);
                String resourceName = shopPramDto.getResourceName();
                CommonValidUtil.validStrNull(resourceName, CodeConst.CODE_PARAMETER_NOT_NULL, "resourceName不能为空");
            }
            // 删除或者更新的时候，resourceId必填
            if (operateType == 1 || operateType == 2)
            {
                Long resourceId = shopPramDto.getResourceId();
                if (null == resourceId)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "resourceId不能为空");
                }
            }
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (shopDto == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }
            shopRsrcServcie.operateResource(shopPramDto);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作场地号成功！", null);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("添加/修改/删除场地号接口-系统异常", e);
            throw new APISystemException("添加/修改/删除场地号接口-系统异常", e);
        }
    }

    /**
     * S30：获取商铺预定资源列表接口(场地类)
     * @param request
     * @return
     */
    @RequestMapping(value = "/goods/getShopPreResources")
    @ResponseBody
    public ResultDto getShopPreResources(HttpServletRequest request)
    {
        try
        {
            logger.info("获取商铺预定资源列表接口- start");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String categoryId = RequestUtils.getQueryParam(request, "categoryId");
            String resourceId = RequestUtils.getQueryParam(request, "resourceId");
            String resourceDate = RequestUtils.getQueryParam(request, "resourceDate");

            CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(resourceId, CodeConst.CODE_PARAMETER_NOT_NULL, "resourceId不能为空");
            CommonValidUtil.validStrNull(resourceDate, CodeConst.CODE_PARAMETER_NOT_NULL, "resourceDate不能为空");

            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("shopId", shopId);
            paramMap.put("categoryId", categoryId);
            paramMap.put("resourceId", resourceId);
            paramMap.put("resourceDate", resourceDate);

            List<PlaceGoodsDto> results = shopServcie.getPlaceGoods(paramMap);

            logger.info("获取商铺预定资源列表接口- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺预定资源列表成功！", results);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺预定资源列表接口 - 系统异常", e);
            throw new APISystemException("获取商铺预定资源列表接口 - 系统异常", e);
        }
    }

    /**
     * 4.2.16MS16：店铺预约开关设置接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/shopManage/bookSwitch")
    @ResponseBody
    public ResultDto bookSwitch(HttpServletRequest request)
    {
        try
        {
            logger.info("4.2.16MS16：店铺预约开关设置接口- start");
            /*
             * settingType flag shopId
             */
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String flagStr = RequestUtils.getQueryParam(request, "flag");
            String settingTypeStr = RequestUtils.getQueryParam(request, "settingType");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(settingTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "settingType不能为空");
            CommonValidUtil.validStrNull(flagStr, CodeConst.CODE_PARAMETER_NOT_NULL, "flag不能为空");

            Integer flag = NumberUtil.strToNum(flagStr, "flag");
            Integer settingType = NumberUtil.strToNum(settingTypeStr, "settingType");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");

            // 参数map
            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("shopId", shopId);
            mapParams.put("settingType", settingType);
            mapParams.put("flag", flag);
            // 默认10分钟
            mapParams.put("confirmMinute", 10);

            shopServcie.bookSwitch(mapParams);

            logger.info("4.2.16MS16：店铺预约开关设置接口- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "预约开关设置成功！", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("4.2.16MS16：店铺预约开关设置接口 - 系统异常", e);
            throw new APISystemException("4.2.16MS16：店铺预约开关设置接口 - 系统异常", e);
        }
    }

    /**
     * 4.2.16MS16：店铺预约开关设置接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/shopManage/getTechnicianOrderNum")
    @ResponseBody
    public ResultDto getTechnicianOrderNum(HttpServletRequest request)
    {
        try
        {
            logger.info("4.2.18MS18：获取技师订单数接口- start");
            /*
             * shopId int 是 商铺Id techId string 是 技师ID queryStatus int 4 否
             * 查询状态：已完成-4
             */
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String techIdStr = RequestUtils.getQueryParam(request, "techId");
            String queryStatusStr = RequestUtils.getQueryParam(request, "queryStatus");
            // 参数map
            Map<String, Object> mapParams = new HashMap<String, Object>();

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(techIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "techId不能为空");
            if (StringUtils.isBlank(queryStatusStr))
            {
                mapParams.put("queryStatus", 4);
            }
            else
            {
                Integer queryStatus = NumberUtil.strToNum(queryStatusStr, "queryStatus");
                mapParams.put("queryStatus", queryStatus);
            }
            Integer techId = NumberUtil.strToNum(techIdStr, "techId");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            mapParams.put("shopId", shopId);
            mapParams.put("techId", techId);
            Map<String, Object> resultMap = shopTechnicianService.getTechnicianOrderNum(mapParams);

            logger.info("4.2.18MS18：获取技师订单数接口- end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取技师订单数成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("4.2.18MS18：获取技师订单数接口 - 系统异常", e);
            throw new APISystemException("4.2.18MS18：获取技师订单数接口 - 系统异常", e);
        }
    }

    /**
     * @Carefull 该接口禁止再次更新，业务再次更新时，应转移使用 PC19接口，@Date 2016-05-24
     * 收银机接口：查询商铺配置项接口
     * @deprecated
     * @Function: com.idcq.appserver.controller.shop.ShopController.
     *            queryShopIntegralSetting
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年9月21日 上午10:42:34
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月21日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value = {"/shop/queryShopConfigureSetting","token/shop/queryShopConfigureSetting","/service/shop/queryShopConfigureSetting","/session/shop/queryShopConfigureSetting"})
    @ResponseBody
    public ResultDto queryShopConfigureSetting(HttpServletRequest request)
    {
        try
        {
            logger.info("收银机接口：查询商铺配置项接口 - start");
            /*
             * shopId int 是 商铺Id token string 是 设备令牌 settingType int 4 是
             * 设置类型：1-积分设置 2-打印设置
             */
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            String settingTypeStr = RequestUtils.getQueryParam(request, "settingType");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(settingTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SETTING_TYPE);
            Integer settingType = NumberUtil.strToNum(settingTypeStr, "settingType");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 校验token和店铺是否存在
            String requestPath = request.getRequestURI();
            if(CommonConst.QUERY_SHOPCONFIGURESETTING_URL.equals(requestPath)){
                CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, "token不能为空");
            	collectService.queryShopAndTokenExists(shopId, token);
            }
            
            List<ShopConfigureSettingDto> list = shopSettingService.queryShopConfigureSetting(shopId, settingType);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询商铺配置项成功！", list);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("收银机接口：查询商铺配置项接口 - 系统异常", e);
            throw new APISystemException("收银机接口：查询商铺配置项接口 - 系统异常", e);
        }
    }

    /**
     * @Carefull 该接口禁止再次更新，业务再次更新时，应转移使用 PC18接口，@Date 2016-05-24
     * 商铺配置项设置接口
     * @Function: 
     *            com.idcq.appserver.controller.shop.ShopController.shopConfigureSetting
     * @Description:
     * @deprecated
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年9月21日 上午11:26:39
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月21日 shengzhipeng v1.0.0 create
     */
    @Deprecated
    @RequestMapping(value = {"/shop/shopConfigureSetting","/session/shop/shopConfigureSetting","/service/shop/shopConfigureSetting","/token/shop/shopConfigureSetting"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto shopConfigureSetting(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("收银机接口：商铺配置项设置接口  - start");
            ShopSettingsDto shopSettingsDto = (ShopSettingsDto) JacksonUtil
                    .postJsonToObj(entity, ShopSettingsDto.class);

            CommonValidUtil.validObjectNull(shopSettingsDto.getSettingType(), CodeConst.CODE_PARAMETER_NOT_NULL,
                    "settingType不能为空");
            String requestPath = request.getRequestURI();
            if(CommonConst.SHOPCONFIGURESETTING_URL.equals(requestPath)){
            	 // 校验token和店铺是否存在
                collectService.queryShopAndTokenExists(shopSettingsDto.getShopId(), shopSettingsDto.getToken());
             
            }
            // 保存配置信息
            shopSettingService.saveConfigureSetting(shopSettingsDto);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "同步商铺配置项成功！", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("收银机接口：商铺配置项设置接口  - 系统异常", e);
            throw new APISystemException("收银机接口：商铺配置项设置接口  - 系统异常", e);
        }
    }
    
    private ShopConfigureSettingDto getSetting(Long shopId, String settingKey, Object settingValue)
    {
        ShopConfigureSettingDto shopConfigureSettingDto = new ShopConfigureSettingDto();
        shopConfigureSettingDto.setSettingKey(settingKey);
        shopConfigureSettingDto.setSettingValue(String.valueOf(settingValue));
        shopConfigureSettingDto.setSettingType(CommonConst.SHOP_SETTING_TYPE_CHARGE);
        shopConfigureSettingDto.setShopId(shopId);
        return shopConfigureSettingDto;
    }

    /**
     * 账务统计查询接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/shop/getAccountingStat", "/service/shop/getAccountingStat", "session/shop/getAccountingStat",
            "/token/shop/getAccountingStat" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAccountingStat(HttpServletRequest request)
    {
        try
        {
            // http://localhost:8080/appServer/interface/shop/getAccountingStat?shopId=10001&token=1213&startTime=2015-05-05%2000:00:00&endTime=2015-05-05%2023:00:00
            logger.info("账务统计查询接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            Map<String, Object> map = new HashMap<String, Object>();
            // 校验商铺及商铺设备token
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 校验店铺是否存在
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (shopDto == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }

            Long cashierId = null;
            if (!StringUtils.isEmpty(cashierIdStr))
            {
                cashierId = CommonValidUtil.validStrLongFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_CASHIERID);
            }
            
            map.put("shopId", getIdListsByHeadShopId(shopId));
            map.put("cashierId", cashierId);
            // 分页默认20条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            // startTime
            CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");
            CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            map.put("startTime", startTime);
            // endTime
            CommonValidUtil.validStrNull(endTime, CodeConst.CODE_PARAMETER_NOT_NULL, "endTime不能为空");
            CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
            map.put("endTime", endTime);
            PageModel pageModel = shopServcie.getAccountingStat(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<Map<String, Object>> list = pageModel.getList();
            // 总营业收入
            double salesIncomeTotal = 0;
            // 总平台服务费
            double serviceTotal = 0;
            //会员卡充值金额
            double memberCardChargeTotal = 0D;
            //会员卡支付金额
            double memberCardPayTotal = 0D;
            if (list != null && list.size() > 0)
            {
                for (Map e : list)
                {
                    if (e != null)
                    {
                        salesIncomeTotal += (e.get("total") == null ? 0d : ((BigDecimal) e.get("total")).doubleValue());
                        serviceTotal += (e.get("shopServiceSharePrice") == null ? 0d : ((BigDecimal) e
                                .get("shopServiceSharePrice")).doubleValue());
                        memberCardChargeTotal += (e.get("chargePrice") == null ? 0d : ((BigDecimal) e
                                .get("chargePrice")).doubleValue());
                        memberCardPayTotal += (e.get("memberCardTotal") == null ? 0d : ((BigDecimal) e
                                .get("memberCardTotal")).doubleValue()); 
                    }
                }
            }
            // 总平台奖励
            map.put("billType", CommonConst.SHOP_BILL_TYPE_REWARD);
            double rewardTotal = shopServcie.getShopRewardTotalBy(map);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("salesIncomeTotal", salesIncomeTotal);
            resultMap.put("rewardTotal", rewardTotal);
            resultMap.put("serviceTotal", serviceTotal);
            resultMap.put("memberCardChargeTotal", memberCardChargeTotal);
            resultMap.put("memberCardPayTotal", memberCardPayTotal);
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("账务统计查询接口-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "账务统计查询成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("账务统计查询接口-系统异常", e);
            throw new APISystemException("账务统计查询接口-系统异常", e);
        }
    }

    /**
     * 订单列表查询接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/getOrderList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getOrderList(HttpServletRequest request)
    {
        try
        {
            logger.info("订单列表查询接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            String queryDate = RequestUtils.getQueryParam(request, "queryDate");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            // 校验商铺及商铺设备token
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            collectService.queryShopAndTokenExists(shopId, token);
            // 分页默认10条，第1页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            // queryDate
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "queryDate不能为空");
            CommonValidUtil.validDateStr(queryDate, CodeConst.CODE_PARAMETER_NOT_VALID, "queryDate格式错误");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shopId", shopId);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
            map.put("queryDate", queryDate);
            PageModel pageModel = shopServcie.getOrderList(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("订单列表查询接口-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询订单列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("订单列表查询接口-系统异常", e);
            throw new APISystemException("订单列表查询接口-系统异常", e);
        }
    }

    /**
     * 查询商铺服务人员信息接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/queryShopEmployeeList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryShopServerList(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 token string 是 设备令牌 employeeCode bigint 否
             * 服务人员编码，非空时表示查询指定的服务人员信息，否则查询商铺所有的服务人员信息
             */
            logger.info("查询商铺服务人员信息接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            String employeeCodeStr = RequestUtils.getQueryParam(request, "employeeCode");
            // 校验商铺及商铺设备token
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            collectService.queryShopAndTokenExists(shopId, token);
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(employeeCodeStr))
            {
                Long employeeCode = NumberUtil.strToLong(employeeCodeStr, "employeeCode");
                map.put("employeeCode", employeeCode);
            }
            map.put("shopId", shopId);
            List<Map<String, Object>> resultList = shopServcie.queryShopServerList(map);
            logger.info("查询商铺服务人员信息接口-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询服务人员信息成功！", resultList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺服务人员信息接口-系统异常", e);
            throw new APISystemException("查询商铺服务人员信息接口-系统异常", e);
        }
    }

    @RequestMapping(value = "/shop/opeShopEmployeeInfo", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResultDto opeShopEmployeeInfo(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 token string 是 设备令牌 employeeCode bigint 是
             * 服务人员编码 userName string 否 服务人员名称 operateType int 是 0：新增 1：修改 2：删除
             */
            logger.info("商铺服务人员信息变更接口-start");
            ShopEmployeeDto shopEmployeeDto = (ShopEmployeeDto) JacksonUtil
                    .postJsonToObj(entity, ShopEmployeeDto.class);
            // 校验
            Integer operateType = shopEmployeeDto.getOperateType();
            if (null == operateType)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operateType不能为空");
            }
            // shopId
            Long shopId = shopEmployeeDto.getShopId();
            if (null == shopId)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            }
            // token
            String token = shopEmployeeDto.getToken();
            if (null == token)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "token不能为空");
            }
            collectService.queryShopAndTokenExists(shopId, token);
            // 员工编码
            String employeeCode = shopEmployeeDto.getEmployeeCode();
            if (null == employeeCode)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "employeeCode不能为空");
            }
            Map map = shopServcie.opeShopEmployeeInfo(shopEmployeeDto);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "同步服务人员信息成功！", map);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("商铺服务人员信息变更接口-异常", e);
            throw new APISystemException("商铺服务人员信息变更接口-异常", e);
        }
    }

    @RequestMapping(value = "/shop/shopBackSettle", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResultDto shopBackSettle(@RequestBody
    Map<String, String> hashMap, HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 orderTitle string 否 订单标题 mobile string 是
             * 手机号码根据此区分会员 totalPrice double 是 订单总价 settlePrice double 是 订单结算价
             * payType int 是 支付方式 1=现金支付，2=短信支付 veriCode string payType=2时必填
             * username string 是 管理员用户名 linePayFlag 短信支付时线上不足是否支持线下付款 0=不支持，1=支持
             */

            logger.info("商铺后台收银接口-start" + hashMap);
            // 校验
            String shopIdStr = hashMap.get("shopId");
            String orderTitle = hashMap.get("orderTitle");
            String mobile = hashMap.get("mobile");
            String totalPriceStr = hashMap.get("totalPrice");
            String settlePriceStr = hashMap.get("settlePrice");
            String payType = hashMap.get("payType");
            String veriCode = hashMap.get("veriCode");
            String username = hashMap.get("username");
            String remark = hashMap.get("remark");// 店铺备注
            String cashierId = hashMap.get("cashierId");
            String busAreaActivityIdStr = hashMap.get("businessAreaActivityId");
            String isUseRedPacket = hashMap.get("isUseRedPacket");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(totalPriceStr, CodeConst.CODE_PARAMETER_NOT_NULL, "totalPrice不能为空");
            CommonValidUtil.validStrNull(settlePriceStr, CodeConst.CODE_PARAMETER_NOT_NULL, "settlePrice不能为空");
            CommonValidUtil.validStrNull(payType, CodeConst.CODE_PARAMETER_NOT_NULL, "payType不能为空");
            CommonValidUtil.validStrNull(username, CodeConst.CODE_PARAMETER_NOT_NULL, "username不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            Double totalPrice = NumberUtil.strToDouble(totalPriceStr, "totalPrice");
            Double settlePrice = NumberUtil.strToDouble(settlePriceStr, "settlePrice");
            Long businessAreaActivityId = NumberUtil.strToLong(busAreaActivityIdStr, "businessAreaActivityId");
            OrderDto order = new OrderDto();
            Date date = new Date();
            order.setOrderTitle(orderTitle);
            order.setOrderTotalPrice(totalPrice);
            order.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
            order.setSettlePrice(settlePrice);
            order.setShopId(shopId);
            order.setCashierUsername(username);
            order.setOrderTime(date);
            order.setPayStatus(CommonConst.PAY_STATUS_PAYED);
            order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
            order.setLastUpdateTime(date);
            order.setGoodsNumber(0);// 无商品
            order.setSettleType(CommonConst.ORDER_SETTLE_ONE); // 按订单结算价结算
            order.setOrderChannelType(3); // 后台下单
            order.setOrderFinishTime(date);
            order.setRemark(remark);
            order.setMobile(mobile);
            order.setBusinessAreaActivityId(businessAreaActivityId);
            if ("1".equals(payType) || "2".equals(payType))
            {// 现金支付或短信支付
                Map map = shopServcie.handleShopBackOrder(order, payType, veriCode, isUseRedPacket);
                if ("2".equals(payType))
                {
                    // 短信通知
                    SmsReplaceContent src = new SmsReplaceContent();
                    Double onLinePayment = (Double) map.get("onLinePayment");
                    Double cashCouponPayment = (Double) map.get("cashCouponPayment");
                    Double usedRedPacketMoney = (Double) map.get("usedRedPacketMoney");
                    src.setOnLinePayment(NumberUtil.formatDouble(onLinePayment, 2));
                    src.setCashCouponPayment(NumberUtil.formatDouble(cashCouponPayment, 2));
                    src.setConsumeDate(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
                    src.setUsedRedPacketMoney(NumberUtil.formatDouble(usedRedPacketMoney, 2));
                    src.setAmount(NumberUtil.formatDouble(settlePrice, 2)); // 消费金额
                    src.setMobile(mobile);
                    StringBuffer usage = new StringBuffer();
                    usage.append("sms_pay");
                    if (onLinePayment > 0)
                    {
                        usage.append("_cqb");
                    }
                    if (cashCouponPayment > 0)
                    {
                        usage.append("_xfk");
                    }
                    if (usedRedPacketMoney > 0)
                    {
                        usage.append("_hb");
                    }
                    src.setUsage(usage.toString());
                    if (!"sms_pay".equals(src.getUsage()))
                    {
                        sendSmsService.sendSmsMobileCode(src);
                    }
                }
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "收银成功！", map);
            }
            else if ("3".equals(payType))
            {// 扫码支付
                Map<String, Object> resultMap = new HashMap<String, Object>();
                // 31位订单
                String orderId = FieldGenerateUtil.generateOrderId(shopId, 6);
                String price = totalPrice + "";
                UserDto userDto = userDao.getUserByMobile(mobile);
                if (userDto != null && settlePrice != null)
                {
                    price = settlePrice + "";
                }
                String result = ToAlipayQrTradePay.qrPay(orderId, price, orderTitle);// 支付宝预下单返回的结果
                JSONObject qrResult = JSONObject.fromObject(result);
                if ("0".equals(qrResult.get("code")))
                {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderTotalPrice(totalPrice);// 设置订单总价
                    orderDto.setShopId(shopId);// 设置店铺id
                    orderDto.setSettlePrice(settlePrice);// 订单结算价格
                    orderDto.setOrderTitle(orderTitle);// 设置订单标题
                    orderDto.setPayType(Integer.parseInt(payType));// 支付类型
                    orderDto.setMobile(mobile);// 会员手机号
                    orderDto.setRemark(remark);
                    if (!StringUtils.isEmpty(cashierId))
                    {
                        orderDto.setCashierId(Long.parseLong(cashierId));
                    }
                    orderDto.setOrderChannelType(CommonConst.ORDER_CHANNEL_MERCHANTBACK);// 商铺后台下单
                    orderDto.setCashierUsername(username);
                    // Todo查找会员编号
                    if (userDto != null)
                    {
                        orderDto.setUserId(userDto.getUserId());
                        orderDto.setBusinessAreaActivityId(businessAreaActivityId);
                    }
                    orderDto.setUserName(username);// 登录一点管家的用户名
                    orderDto.setOrderTime(new Date());
                    shopServcie.preOrderForScanCode(orderDto);
                    resultMap.put("orderNo", orderId);
                    resultMap.put("qrcode", qrResult.get("qrcode"));
                    DataCacheApi.setObjectEx(CommonConst.ALISCANPAY_KEY + orderId, orderDto, 60 * 5);// 订单5分钟允许失效
                    return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取支付宝扫码支付二维码成功", resultMap);
                }
                else
                {
                    return ResultUtil.getResult(CodeConst.CODE_REQUESTQRCODE_FAIL, "获取支付宝扫码支付二维码失败", resultMap);
                }
            }
            else if ("4".equals(payType))
            {// 微信扫码支付
                Map<String, Object> resultMap = new HashMap<String, Object>();
                String orderId = FieldGenerateUtil.generateOrderId(shopId, 6);
                String price = totalPrice + "";
                UserDto userDto = userDao.getUserByMobile(mobile);
                if (userDto != null && settlePrice != null)
                {
                    price = settlePrice + "";
                }
                Map<String, String> scanResult = WXScanUtil.scanRequest("", price, orderId);// 微信请求二维码
                if (!StringUtils.isEmpty(scanResult.get("qrcode")))
                {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderTotalPrice(totalPrice);// 设置订单总价
                    orderDto.setShopId(shopId);// 设置店铺id
                    orderDto.setSettlePrice(settlePrice);// 订单结算价格
                    orderDto.setOrderTitle(orderTitle);// 设置订单标题
                    orderDto.setPayType(Integer.parseInt(payType));// 支付类型
                    orderDto.setMobile(mobile);// 会员手机号
                    orderDto.setRemark(remark);
                    if (!StringUtils.isEmpty(cashierId))
                    {
                        orderDto.setCashierId(Long.parseLong(cashierId));
                    }
                    orderDto.setOrderChannelType(CommonConst.ORDER_CHANNEL_MERCHANTBACK);// 商铺后台下单
                    orderDto.setCashierUsername(username);
                    // Todo查找会员编号
                    if (userDto != null)
                    {
                        orderDto.setUserId(userDto.getUserId());
                        orderDto.setBusinessAreaActivityId(businessAreaActivityId);
                    }
                    orderDto.setOrderTime(new Date());
                    orderDto.setUserName(username);// 登录一点管家的用户名
                    shopServcie.preOrderForWxScanCode(orderDto);
                    resultMap.put("orderNo", orderId);
                    resultMap.put("qrcode", scanResult.get("qrcode"));
                }
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取微信扫码支付二维码成功", resultMap);
            }
            else
            {
                return ResultUtil
                        .getResult(CodeConst.CODE_REQUESTQRCODE_FAIL, "支付方式不存在", new HashMap<String, Object>());
            }
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("商铺后台收银接口-异常", e);
            throw new APISystemException("商铺后台收银接口-异常", e);
        }
    }

    @RequestMapping(value = "/shop/checkSmsPayVeriCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto checkSmsPayVeriCode(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 mobile string 是 手机号码根据此区分会员 settlePrice double
             * 是 订单结算价 veriCode string 必填 usage string 非必填 预留 username string 是
             * 管理员用户名
             */
            logger.info("验证短信支付验证码接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String veriCode = RequestUtils.getQueryParam(request, "veriCode");
            String usage = RequestUtils.getQueryParam(request, "usage");
            String settlePriceStr = RequestUtils.getQueryParam(request, "settlePrice");
            String username = RequestUtils.getQueryParam(request, "username");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "mobile不能为空");
            CommonValidUtil.validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL, "veriCode不能为空");
            CommonValidUtil.validStrNull(settlePriceStr, CodeConst.CODE_PARAMETER_NOT_NULL, "settlePrice不能为空");
            CommonValidUtil.validStrNull(username, CodeConst.CODE_PARAMETER_NOT_NULL, "username不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            Double settlePrice = NumberUtil.strToDouble(settlePriceStr, "settlePrice");
            shopServcie.checkSmsPayVeriCode(shopId, mobile, veriCode, usage, settlePrice, username);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "验证成功！", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("验证短信支付验证码接口 -异常", e);
            throw new APISystemException("验证短信支付验证码接口 -异常", e);
        }
    }

    @RequestMapping(value = "/shop/checkMember", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto checkMember(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 mobile string 是 手机号码根据此区分会员 username string 是
             * 管理员用户名
             */
            logger.info("校验会员信息接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String username = RequestUtils.getQueryParam(request, "username");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "mobile不能为空");
            CommonValidUtil.validStrNull(username, CodeConst.CODE_PARAMETER_NOT_NULL, "username不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            shopServcie.checkMember(shopId, mobile, username);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "验证成功！", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("校验会员信息接口 -异常", e);
            throw new APISystemException("校验会员信息接口 -异常", e);
        }
    }

    /**
     * CS19：查询店铺账户账单统计接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/shop/getBillStat", "/token/shop/getBillStat", "/session/shop/getBillStat", "/service/shop/getBillStat" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBillStat(HttpServletRequest request)
    {
        try
        {
            logger.info("查询店铺账户账单统计接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            String url = request.getRequestURI();
            if (CommonConst.GETBILLSTAT_URL.equals(url))
            {
                String token = RequestUtils.getQueryParam(request, "token");
                // token不能为空
                CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
                // 商铺存在性
                collectService.queryShopAndTokenExists(shopId, token);
            }
            String accountTypeStr = RequestUtils.getQueryParam(request, "accountType"); // 账户类型.
                                                                                        // 1:线上营业收入2:平台奖励3:保证金

            String moneyTypeStr = RequestUtils.getQueryParam(request, "moneyType"); // 收支类型
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");

            CommonValidUtil.validStrNull(accountTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "账户类型不能为空");
            CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");
            CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            int[] accountTypes = convertType(ArrayUtil.toArray(accountTypeStr, null));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shopId", getIdListsByHeadShopId(shopId));
            map.put("accountType", accountTypes);
            map.put("moneyType", ArrayUtil.toArray(moneyTypeStr, null));
            map.put("startTime", startTime);

            // endTime
            if (StringUtils.isNotEmpty(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
            }
            else
            {
                endTime = DateUtils.format(new Date(), null);
            }
            map.put("endTime", endTime);

            List<Map<String, Object>> list = shopServcie.getBillStat(map);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", list);
            logger.info("查询店铺账户账单统计接口-end");
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "店铺账户账单统计查询成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询店铺账户账单统计接口-系统异常", e);
            throw new APISystemException("查询店铺账户账单统计接口-系统异常", e);
        }
    }

    /**
     * CS20：查询店铺账户账单明细接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/shop/getBillDetail", "/service/shop/getBillDetail", "/session/shop/getBillDetail", "/token/shop/getBillDetail" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBillDetail(HttpServletRequest request) throws Exception
    {
            logger.info("查询店铺账户账单明细接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String accountTypeStr = RequestUtils.getQueryParam(request, "accountType"); // 账户类型.
                                                                                        // 1:线上营业收入2:平台奖励3:保证金4：生意金

            String moneyTypeStr = RequestUtils.getQueryParam(request, "moneyType"); // 收支类型
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");

            CommonValidUtil.validStrNull(accountTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "账户类型不能为空");
            CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");
            CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            
            // 分页默认10条，第1页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);

            Map<String, Object> map = new HashMap<String, Object>();
            int[] accountTypes = convertType(ArrayUtil.toArray(accountTypeStr, null));
            map.put("shopId", getIdListsByHeadShopId(shopId));
            map.put("accountType", accountTypes);
            map.put("moneyType", ArrayUtil.toArray(moneyTypeStr, null));
            map.put("startTime", startTime);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);

            // endTime
            if (StringUtils.isNotEmpty(endTime))
            {
                CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
            }
            else
            {
                endTime = DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
            }
            map.put("endTime", endTime);

            PageModel pageModel = shopServcie.getBillDetail(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);

            logger.info("查询店铺账户账单明细接口-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "店铺账户账单明细查询成功", resultMap,
                    DateUtils.DATETIME_FORMAT);
    }

    @RequestMapping(value =
    { "/service/shop/getBillDetailById", "/session/shop/getBillDetailById", "/token/shop/getBillDetailById" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBillDetailById (HttpServletRequest request) throws Exception {
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String billId = RequestUtils.getQueryParam(request, "billId"); // 账单ID
        CommonValidUtil.validStrNull(billId, CodeConst.CODE_PARAMETER_NOT_NULL, "账单ID不能为空");
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "店铺ID不能为空");
        Map resultMap = shopServcie.getBillDetailById(NumberUtil.strToLong(billId, "billId"), NumberUtil.strToLong(shopIdStr, "shopId"));
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "店铺账户账单明细查询成功", resultMap,
                DateUtils.DATETIME_FORMAT);
    }
    /**
     * 接口传递的accountType的值与数据库对应的值进行转换 1:线上营业收入 --> 0 2:平台奖励 --> 1
     * @param accountTypes 账户类型数组
     * @return void [返回类型说明]
     * @author shengzhipeng
     * @throws Exception
     * @date 2016年2月22日
     */
    private int[] convertType(String[] accountTypes) throws Exception
    {
        int[] intAccountTypes = new int[accountTypes.length];
        if (accountTypes != null && accountTypes.length > 0)
        {
            for (int i = 0; i < accountTypes.length; i++)
            {
                String string = accountTypes[i];
                if ("1".equals(string))
                {
                    intAccountTypes[i] = 0;
                }
                else if ("2".equals(string))
                {
                    intAccountTypes[i] = 1;
                }
                else
                {
                    intAccountTypes[i] = NumberUtil.strToNum(accountTypes[i], "accountTypes");
                }

            }
        }
        return intAccountTypes;
    }

    @RequestMapping(value =
    { "/shop/getGoodsSalesStat", "/token/shop/getGoodsSalesStat", "/session/shop/getGoodsSalesStat",
            "/service/shop/getGoodsSalesStat" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getGoodsSalesStat(HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺商品销售统计接口-start");
            /*
             * shopId int 是 店铺ID token string 条件 设备令牌。Token鉴权方式必填
             * goodsCategoryId int 否 商品分类ID。 不填时，搜索全部分类商品。 填写时，搜索分类与下级分类商品。
             * startTime date 是 开始时间 endTime date 否 结束时间 pNo int 否
             * 页码,从第1页开始，默认为1 pSize int 否 每页记录数，默认为10
             */

            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String goodsCategoryIdStr = RequestUtils.getQueryParam(request, "goodsCategoryId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String isShow = RequestUtils.getQueryParam(request, "isShow");
            String searchKey = RequestUtils.getQueryParam(request, "searchKey");
            String orderBy = RequestUtils.getQueryParam(request, "orderBy");
            String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");

            if (StringUtils.isEmpty(isShow))
            { 
                // 默认查询显示的商品
                isShow = "1";
            }
            else if ("2".equals(isShow) || !isShow.matches("1|0"))
            {
                // 不限制商品是否显示
                isShow = null;
            }

            Map<String, Object> pMap = new HashMap<String, Object>();

            CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");

            // 开始时间
            CommonValidUtil.validDateStr(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            pMap.put("startTime", startTime);

            // 结束时间
            if (StringUtils.isNotBlank(endTime))
            {
                CommonValidUtil.validDateStr(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
                pMap.put("endTime", endTime);
            }
            else
            {
                endTime = DateUtils.format(new Date(), "yyyy-MM-dd");
                pMap.put("endTime", endTime);
            }

            // 分类id
            if (StringUtils.isNotBlank(goodsCategoryIdStr))
            {
                Integer goodsCategoryId = NumberUtil.strToNum(goodsCategoryIdStr, "goodsCategoryId");
                pMap.put("goodsCategoryId", goodsCategoryId);
            }
            // shopid
            if (StringUtils.isNotBlank(shopIdStr))
            {
                Integer shopId = NumberUtil.strToNum(shopIdStr, "shopId");
                pMap.put("shopId", shopId);
            }
            
            // 排序
            if (StringUtils.isNotBlank(orderBy))
            {
            	Integer orderByNum = NumberUtil.strToNum(orderBy, "orderBy");
            	if(!(orderByNum.intValue()==1 || orderByNum.intValue()==2)){
            		orderByNum = 2;
            	}
            	pMap.put("orderBy", orderByNum);
            }
            else
            {
            	pMap.put("orderBy", 2);//降序
            }

            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);

            pMap.put("limit", (pNo - 1) * pSize);
            pMap.put("pSize", pSize);
            pMap.put("isShow", isShow);
            pMap.put("goodsStatus", goodsStatus);
            pMap.put("searchKey", searchKey);

            PageModel pageModel = shopServcie.getGoodsSalesStat(pMap);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("pNo", pNo);
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("lst", pageModel.getList());

            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "调用成功", resultMap, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("查询商铺商品销售统计接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("查询商铺商品销售统计接口异常", e);
            throw new APISystemException("查询商铺商品销售统计接口异常", e);
        }
    }
    
    
	@RequestMapping(value = {"/shop/getGoodsCategorySalesStat",
			"/token/shop/getGoodsCategorySalesStat",
			"/session/shop/getGoodsCategorySalesStat",
			"/service/shop/getGoodsCategorySalesStat"}, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getGoodsCategorySalesStat(HttpServletRequest request) {
		try {
			/*
			 * shopId int 是 商铺ID token string 条件 设备令牌。Token鉴权方式必填 orderBy 2 否
			 * 按销量排序，1为升序，2为降序 startTime date 是 开始时间 endTime date 否 结束时间 pNo int
			 * 否 页码,从第1页开始，默认为1 pSize int 否 每页记录数，默认为10
			 */
			logger.info("PCS38：查询商铺分类商品销售统计-start");

			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String goodsCategoryIdStr = RequestUtils.getQueryParam(request,
					"goodsCategoryId");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			String orderBy = RequestUtils.getQueryParam(request, "orderBy");

			Map<String, Object> pMap = new HashMap<String, Object>();

			CommonValidUtil.validStrNull(startTime,
					CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");

			// 开始时间
			CommonValidUtil.validDateTimeFormat(startTime,
					CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
			pMap.put("startTime", startTime + " 00:00:00");

			// 结束时间
			if (StringUtils.isNotBlank(endTime)) {
				CommonValidUtil.validDateTimeFormat(endTime,
						CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
				pMap.put("endTime", endTime + " 23:59:59");
			} else {
				endTime = DateUtils.format(new Date(), "yyyy-MM-dd 23:59:59");
				pMap.put("endTime", endTime);
			}

			// 分类id
			if (StringUtils.isNotBlank(goodsCategoryIdStr)) {
				Integer goodsCategoryId = NumberUtil.strToNum(
						goodsCategoryIdStr, "goodsCategoryId");
				pMap.put("goodsCategoryId", goodsCategoryId);
			}

			// 结束时间
			if (StringUtils.isNotBlank(orderBy)) {
				pMap.put("orderBy", NumberUtil.strToNum(orderBy, "orderBy"));
			} else {
				pMap.put("orderBy", 2);// 降序
			}

			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);

			pMap.put("shopId", Long.parseLong(shopIdStr));
			pMap.put("limit", (pNo - 1) * pSize);
			pMap.put("pSize", pSize);

			PageModel pageModel = shopServcie.getGoodsCategorySalesStat(pMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("pNo", pNo);
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("lst", pageModel.getList());

			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "调用成功",
					resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			logger.error("PCS38：查询商铺分类商品销售统计接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("PCS38：查询商铺分类商品销售统计-异常", e);
			throw new APISystemException("PCS38：查询商铺分类商品销售统计-异常", e);
		}
	}
	
	@RequestMapping(value = {"/shop/shopIncomeStatDetail",
			"/token/shop/shopIncomeStatDetail",
			"/session/shop/shopIncomeStatDetail",
			"/service/shop/shopIncomeStatDetail"}, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String shopIncomeStatDetail(HttpServletRequest request) {
		try {
/*			shopId	int		是	店铺ID，商家端必填
			token	string		条件	店铺令牌，收银机操作必填
			startDate	date		是	查询开始日期， 
			endDate	date		否	查询结束日期，默认当日
			pNo	int		否	页码,从第1页开始，默认为1
			pSize	int		否	每页记录数，默认为10*/
			logger.info("PCS40：获取商铺营收统计详情接口-start");

			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String startDate  = RequestUtils.getQueryParam(request, "startDate");
			String endDate = RequestUtils.getQueryParam(request, "endDate");
			String dateType = RequestUtils.getQueryParam(request, "dateType");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			String orderBy = RequestUtils.getQueryParam(request, "orderBy");
			Map<String, Object> pMap = new HashMap<String, Object>();

			CommonValidUtil.validStrNull(startDate,
					CodeConst.CODE_PARAMETER_NOT_NULL, "startDate不能为空");
			
			CommonValidUtil.validStrNull(dateType,
					CodeConst.CODE_PARAMETER_NOT_NULL, "dateType不能为空");

			// 开始时间
			CommonValidUtil.validDateStr(startDate, CodeConst.CODE_PARAMETER_NOT_VALID, "startDate格式错误");
			pMap.put("startDate", startDate);

			// 结束时间
			if (StringUtils.isNotBlank(endDate)) {
				CommonValidUtil.validDateTimeFormat(endDate,
						CodeConst.CODE_PARAMETER_NOT_VALID, "endDate格式错误");
				pMap.put("endDate", endDate);
			} else {
				pMap.put("endTime", DateUtils.format(new Date(), "yyyy-MM-dd"));
			}
			if(StringUtils.isBlank(orderBy)) {
			    orderBy = "1";
			}

			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);

			pMap.put("dateType", NumberUtil.strToNum(dateType, "dateType"));
			pMap.put("shopId", Long.parseLong(shopIdStr));
			pMap.put("limit", (pNo - 1) * pSize);
			pMap.put("pSize", pSize);
			pMap.put("orderBy", orderBy);

			PageModel pageModel = shopServcie.shopIncomeStatDetail(pMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("pNo", pNo);
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("lst", pageModel.getList());

			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "调用成功",
					resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			logger.error("PCS40：获取商铺营收统计详情接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("PCS40：获取商铺营收统计详情接口-异常", e);
			throw new APISystemException("PCS40：获取商铺营收统计详情接口-异常", e);
		}
	}

	@RequestMapping(value = {"/shop/shopDayIncomeStatDetail",
			"/token/shop/shopDayIncomeStatDetail",
			"/session/shop/shopDayIncomeStatDetail",
			"/service/shop/shopDayIncomeStatDetail"}, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String shopDayIncomeStatDetail(HttpServletRequest request) {
		try {
			
			logger.info("PCS41：获取商铺日营收详情接口-start");

			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String queryDate = RequestUtils.getQueryParam(request, "queryDate");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			String orderByStr = RequestUtils.getQueryParam(request, "orderBy");


			Map<String, Object> pMap = new HashMap<String, Object>();

			CommonValidUtil.validStrNull(queryDate,
					CodeConst.CODE_PARAMETER_NOT_NULL, "queryDate不能为空");
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			Integer orderBy = 1;
			if(StringUtils.isNotBlank(orderByStr)){
				orderBy = NumberUtil.strToInteger(orderByStr, "orderBy");
			}
			pMap.put("orderBy", orderBy);
			// 开始时间
			CommonValidUtil.validDateStr(queryDate, CodeConst.CODE_PARAMETER_NOT_VALID, "queryDate格式错误");
			pMap.put("queryDate", queryDate);
			

			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);

			pMap.put("shopId", Long.parseLong(shopIdStr));
			pMap.put("limit", (pNo - 1) * pSize);
			pMap.put("pSize", pSize);

			PageModel pageModel = shopServcie.shopDayIncomeStatDetail(pMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("pNo", pNo);
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("lst", pageModel.getList());

			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "调用成功",
					resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			logger.error("PCS41：获取商铺日营收详情接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("PCS41：获取商铺日营收详情接口-异常", e);
			throw new APISystemException("PCS41：获取商铺日营收详情接口-异常", e);
		}
	}
	
    /**
     * 商铺余额转充接口
     * 
     * @Function: com.idcq.appserver.controller.shop.ShopController.fillBail
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月2日 下午2:39:27
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年12月2日 ChenYongxin v1.0.0 create
     */
    @RequestMapping(value = "/shop/fillBail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto fillBail(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 token string 是 设备令牌 onlineIncomeMoney double 否
             * 转充店铺收入金额。填充此字段时，指定此账户转充指定金额。 rewardMoney double 否
             * 转充平台奖励金额。填充此字段时，指定此账户转充指定金额。
             */
            logger.info("余额转充接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            String onlineIncomeMoneyStr = RequestUtils.getQueryParam(request, "onlineIncomeMoney");
            String rewardMoneyStr = RequestUtils.getQueryParam(request, "rewardMoney");
            Double onlineIncomeMoney = null;
            Double rewardMoney = null;

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");

            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 校验token和店铺是否存在
            collectService.queryShopAndTokenExists(shopId, token);
            // 验证在线收入格式
            if (StringUtils.isNotBlank(onlineIncomeMoneyStr))
            {
                onlineIncomeMoney = NumberUtil.strToDouble(onlineIncomeMoneyStr, "onlineIncomeMoney");
                if (onlineIncomeMoney <= 0)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "onlineIncomeMoney不能小于等于0");
                }
            }
            // 验证平台奖励格式
            if (StringUtils.isNotBlank(rewardMoneyStr))
            {
                rewardMoney = NumberUtil.strToDouble(rewardMoneyStr, "rewardMoney");
                if (rewardMoney <= 0)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "rewardMoney不能小于等于0");
                }
            }
            Map<String, Object> resulMap = shopServcie.fillBail(shopId, onlineIncomeMoney, rewardMoney);
            //结算
            orderService.dealSettleOrderByShopId(shopId);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "转充成功！", resulMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("余额转充接口  -异常", e);
            throw new APISystemException("余额转充接口  -异常", e);
        }
    }

    /**
     * 区域活动接口，临时接口，已过期
     */
    @Deprecated
    @RequestMapping(value = "/shop/getPromotionShop", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto getPromotionShop(HttpServletRequest request)
    {
        try
        {
            logger.info("获取区域活动店铺列表接口 -start");
            // 校验
            String cityIdStr = RequestUtils.getQueryParam(request, "cityId");
            String queryDate = RequestUtils.getQueryParam(request, "queryDate");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);

            CommonValidUtil.validStrNull(cityIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "cityId不能为空");
            CommonValidUtil.validStrNull(queryDate, CodeConst.CODE_PARAMETER_NOT_NULL, "queryDate不能为空");
            Long cityId = NumberUtil.strToLong(cityIdStr, "cityId");
            CommonValidUtil.validDateTimeFormat(queryDate, CodeConst.CODE_PARAMETER_NOT_VALID, "queryDate格式错误");
            PageModel pageModel = this.shopServcie.getPromotionShop(cityId, queryDate, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "调用成功！", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取区域活动店铺列表接口  -异常", e);
            throw new APISystemException("获取区域活动店铺列表接口  -异常", e);
        }
    }

    @Deprecated
    @RequestMapping(value = "/shop/getPromotionShopDetail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto getPromotionShopDetail(HttpServletRequest request)
    {
        try
        {
            logger.info("获取商铺活动详情接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String actCode = RequestUtils.getQueryParam(request, "actCode");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(actCode, CodeConst.CODE_PARAMETER_NOT_NULL, "actCode不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            Map map = this.shopServcie.getPromotionShopDetail(shopId, actCode);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "调用成功！", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺活动详情接口  -异常", e);
            throw new APISystemException("获取商铺活动详情接口  -异常", e);
        }
    }

    /**
     * 
     * @param request
     * @return
     * @date 2016年1月14日11:40:51
     */
    @RequestMapping(value =
    { "/session/shop/getReferUserStat", "/token/shop/getReferUserStat", "/service/shop/getReferUserStat" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto getReferUserStat(HttpServletRequest request)
    {
        try
        {
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, "startTime不能为空");
            Map map = this.shopServcie.getTotalMember(shopId, startTime, endTime);

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "调用成功！", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺活动详情接口  -异常", e);
            throw new APISystemException("获取商铺活动详情接口  -异常", e);
        }
    }

    @RequestMapping(value =
    { "/token/shop/getShopResource", "/session/shop/getShopResource", "/service/shop/getShopResource" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto getShopResource(HttpServletRequest request)
    {
        try
        {
            logger.info("获取商铺资源接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String resourceIdStr = RequestUtils.getQueryParam(request, "resourceId");
            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(resourceIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "resourceId不能为空");
            Long resourceId = NumberUtil.strToLong(resourceIdStr, "resourceId");
            Map map = this.shopServcie.getShopResource(shopId, resourceId);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "调用成功！", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺资源接口  -异常", e);
            throw new APISystemException("获取商铺资源接口  -异常", e);
        }
    }

    @RequestMapping(value =
    { "/service/shop/fillBail", "/token/shop/fillBail", "/session/shop/fillBail" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto fillBailByCash(HttpServletRequest request)
    {
        try
        {
            /*
             * shopId int 是 商铺编号 token string 是 设备令牌 onlineIncomeMoney double 否
             * 转充店铺收入金额。填充此字段时，指定此账户转充指定金额。 rewardMoney double 否
             * 转充平台奖励金额。填充此字段时，指定此账户转充指定金额。
             */
            logger.info("余额转充接口 -start");
            // 校验
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String onlineIncomeMoneyStr = RequestUtils.getQueryParam(request, "onlineIncomeMoney");
            String rewardMoneyStr = RequestUtils.getQueryParam(request, "rewardMoney");
            Double onlineIncomeMoney = null;
            Double rewardMoney = null;

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");

            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 验证在线收入格式
            if (StringUtils.isNotBlank(onlineIncomeMoneyStr))
            {
                onlineIncomeMoney = NumberUtil.strToDouble(onlineIncomeMoneyStr, "onlineIncomeMoney");
                if (onlineIncomeMoney <= 0)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "onlineIncomeMoney不能小于等于0");
                }
            }
            // 验证平台奖励格式
            if (StringUtils.isNotBlank(rewardMoneyStr))
            {
                rewardMoney = NumberUtil.strToDouble(rewardMoneyStr, "rewardMoney");
                if (rewardMoney <= 0)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "rewardMoney不能小于等于0");
                }
            }
            Map<String, Object> resulMap = shopServcie.fillBail(shopId, onlineIncomeMoney, rewardMoney);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "转充成功！", resulMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("余额转充接口  -异常", e);
            throw new APISystemException("余额转充接口  -异常", e);
        }
    }

    /**
     * CO12：获取订单列表接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/service/shop/getAllOrderList", "/session/shop/getAllOrderList", "/token/shop/getAllOrderList" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAllOrderList(HttpServletRequest request)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            logger.info("获取订单列表接口    -start");
            // shopid
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);

            String dateTypeStr = RequestUtils.getQueryParam(request, "dateType");
            String startDate = RequestUtils.getQueryParam(request, "startDate");
            String endDate = RequestUtils.getQueryParam(request, "endDate");
            String orderStatus = RequestUtils.getQueryParam(request, "orderStatus");
            String orderTransactionTypeStr = RequestUtils.getQueryParam(request, "orderTransactionType");
            String payTypeStr = RequestUtils.getQueryParam(request, "payType");
            String billerIdStr = RequestUtils.getQueryParam(request, "billerId");
            String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
            int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
            int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String yearOnYearStr = RequestUtils.getQueryParam(request, "yearOnYear");
            String ringStr = RequestUtils.getQueryParam(request, "ring");
            Integer dateType = 1;
            if (!StringUtils.isEmpty(dateTypeStr))
            {
                dateType = CommonValidUtil
                        .validStrIntFmt(dateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "查询时间字段类型有误");
                if (dateType == null || !(dateType == 1 || dateType == 2 || dateType == 3))
                {
                    dateType = 1;
                }
            }
            CommonValidUtil.validStrNull(startDate, CodeConst.CODE_PARAMETER_NOT_NULL, "查询开始日期不能为空");
            CommonValidUtil.validDateTimeFormat(startDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID,
                    "查询开始日期格式错误");
            if (!StringUtils.isEmpty(endDate))
            {
                CommonValidUtil.validDateTimeFormat(endDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "查询截止日期格式错误");
            }
            List<Integer> orderStatuss = null;
            if (!StringUtils.isEmpty(orderStatus))
            {
                orderStatuss = new ArrayList<Integer>();
                String[] strs = orderStatus.split(CommonConst.COMMA_SEPARATOR);
                for (int i = 0; i < strs.length; i++)
                {
                    String str = strs[i];
                    orderStatuss.add(CommonValidUtil
                            .validStrIntFmt(str, CodeConst.CODE_PARAMETER_NOT_VALID, "订单状态类型有误"));
                }
            }
            Integer orderTransactionType = null;
            if (!StringUtils.isEmpty(orderTransactionTypeStr))
            {
                orderTransactionType = CommonValidUtil.validStrIntFmt(orderTransactionTypeStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID, "订单交易状态类型有误");
            }
            Integer payType = null;
            if (!StringUtils.isEmpty(payTypeStr))
            {
                payType = CommonValidUtil.validStrIntFmt(payTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "支付方式类型有误");
            }
            Integer billerId = null;
            if (!StringUtils.isEmpty(billerIdStr))
            {
                billerId = CommonValidUtil.validStrIntFmt(billerIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "导购员类型有误");
            }
            Integer cashierId = null;
            if (!StringUtils.isEmpty(cashierIdStr))
            {
                cashierId = CommonValidUtil.validStrIntFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "收银员编号类型有误");
            }
            Long userId = null;
            if(userIdStr!=null){
            	CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "userId数据格式不正确");
            	userId=Long.valueOf(userIdStr);
            }
            
            Map<String, Object> resultMap = orderService.getAllOrderList(getIdListsByHeadShopId(shopId), dateType, startDate, endDate,
                    orderStatuss, orderTransactionType, payType, billerId, cashierId, pNo, pSize,userId,yearOnYearStr,ringStr);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询订单列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取订单列表接口    -异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取订单列表接口    -系统异常", e);
            throw new APISystemException("获取订单列表接口    -系统异常", e);
        }
        finally
        {
            logger.info("共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }

    /**
     * CO13：获取日订单统计接口（多条件）
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/shop/getDayOrderStat", "/session/shop/getDayOrderStat", "/service/shop/getDayOrderStat",
            "/token/shop/getDayOrderStat" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getDayOrderStat(HttpServletRequest request)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            logger.info("获取日订单统计接口    -start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            String requestPath = request.getRequestURI();
            if (CommonConst.DAYORDERSTAT_URL.equals(requestPath))
            {
                // token不能为空
                String token = RequestUtils.getQueryParam(request, "token");
                CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
                // 商铺存在性
                collectService.queryShopAndTokenExists(shopId, token);
            }
            // 令牌
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            String dateTypeStr = RequestUtils.getQueryParam(request, "dateType");
            String startDate = RequestUtils.getQueryParam(request, "startDate");
            String endDate = RequestUtils.getQueryParam(request, "endDate");
            String orderStatus = RequestUtils.getQueryParam(request, "orderStatus");
            String orderTransactionTypeStr = RequestUtils.getQueryParam(request, "orderTransactionType");
            String payTypeStr = RequestUtils.getQueryParam(request, "payType");
            String billerIdStr = RequestUtils.getQueryParam(request, "billerId");
            String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
            int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
            String yearOnYearStr = RequestUtils.getQueryParam(request, "yearOnYear");
            String ringStr = RequestUtils.getQueryParam(request, "ring");
            CommonValidUtil.validStrNull(startDate, CodeConst.CODE_PARAMETER_NOT_NULL, "查询开始日期不能为空");
            Integer dateType = 1;
            if (!StringUtils.isEmpty(dateTypeStr))
            {
                dateType = CommonValidUtil
                        .validStrIntFmt(dateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "查询时间字段类型有误");
                if (dateType == null || !(dateType == 1 || dateType == 2 || dateType == 3))
                {
                    dateType = 1;
                }
            }
            CommonValidUtil.validDateTimeFormat(startDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID,
                    "查询开始日期格式错误");
            if (!StringUtils.isEmpty(endDate))
            {
                CommonValidUtil.validDateTimeFormat(endDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "查询截止日期格式错误");
            }
            List<Integer> orderStatuss = null;
            if (!StringUtils.isEmpty(orderStatus))
            {
                orderStatuss = new ArrayList<Integer>();
                String[] strs = orderStatus.split(CommonConst.COMMA_SEPARATOR);
                for (int i = 0; i < strs.length; i++)
                {
                    String str = strs[i];
                    orderStatuss.add(CommonValidUtil
                            .validStrIntFmt(str, CodeConst.CODE_PARAMETER_NOT_VALID, "订单状态类型有误"));
                }
            }
            Integer orderTransactionType = null;
            if (!StringUtils.isEmpty(orderTransactionTypeStr))
            {
                orderTransactionType = CommonValidUtil.validStrIntFmt(orderTransactionTypeStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID, "订单交易状态类型有误");
            }
            Integer payType = null;
            if (!StringUtils.isEmpty(payTypeStr))
            {
                payType = CommonValidUtil.validStrIntFmt(payTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "支付方式类型有误");
            }
            Integer billerId = null;
            if (!StringUtils.isEmpty(billerIdStr))
            {
                billerId = CommonValidUtil.validStrIntFmt(billerIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "导购员编号类型有误");
            }
            Integer cashierId = null;
            if (!StringUtils.isEmpty(cashierIdStr))
            {
                cashierId = CommonValidUtil.validStrIntFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "收银员编号类型有误");
            }
            Long userId = null;
            if(StringUtils.isNotBlank(userIdStr)) {
                userId = NumberUtil.strToLong(userIdStr, "userId");
                UserDto user = userDao.getUserById(userId);
                CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
            }
            Map<String, Object> resultMap = orderServcie.getDayOrderStat(userId, shopId, dateType, startDate, endDate,
                    orderStatuss, orderTransactionType, payType, billerId, cashierId, pNo, pSize,yearOnYearStr,ringStr);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询订单列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取日订单统计接口    -异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取日订单统计接口    -系统异常", e);
            throw new APISystemException("获取日订单统计接口    -系统异常", e);
        }
        finally
        {
            logger.info("共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }

    /**
     * PCS28：批量上传商铺菜品备注接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/service/shop/batchInsertCookingDetail", "/token/shop/batchInsertCookingDetail",
            "/session/shop/batchInsertCookingDetail" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto batchInsertCookingDetail(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("批量上传商铺菜品备注接口 -start");
            List<ShopCookingDetails> cookDetailList = checkCookingDetailValid(entity);
            collectService.batchInsertCookingDetail(cookDetailList);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "批量上传商铺菜品备注成功", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("批量上传商铺菜品备注接口  -异常", e);
            throw new APISystemException("批量上传商铺菜品备注接口  -异常", e);
        }
    }

    private List<ShopCookingDetails> checkCookingDetailValid(HttpEntity<String> entity) throws Exception
    {

        BatchInsertCookingDetailModel cookingDetailModel = (BatchInsertCookingDetailModel) JacksonUtil.postJsonToObj(
                entity, BatchInsertCookingDetailModel.class, DateUtils.DATETIME_FORMAT);

        Long shopId = cookingDetailModel.getShopId();

        if (shopId == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
        }

        int flag = this.shopServcie.queryShopExists(shopId);
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);

        int typeOrder = 1;
        List<ShopCookingDetails> cookDetailList = cookingDetailModel.getCookDetail();
        for (ShopCookingDetails cookDetail : cookDetailList)
        {
            if (cookDetail.getDetailsType() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "detailsType不能为空");
            }

            if (cookDetail.getDetailsName() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "detailsName不能为空");
            }

            cookDetail.setShopId(shopId);
            cookDetail.setTypeOrder(typeOrder++);
        }

        return cookDetailList;

    }
    /**
     * PCS32：获取提现校准信息接口
     * @param request
     * @return
     */
    @RequestMapping(value ={"/session/shop/getShopAuditInfo", "/service/shop/getShopAuditInfo",
            "/token/shop/getShopAuditInfo" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopAuditInfo(HttpServletRequest request)
    {
        try
        {

            logger.info("PCS32：获取提现校准信息接口    -start");
            String withdrawIdStr = RequestUtils.getQueryParam(request, "withdrawId");
            CommonValidUtil.validStrNull(withdrawIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawId不能为空");
            Long withdrawId = NumberUtil.strToLong(withdrawIdStr,"withdrawId");
            Map<String, Object> resultMap = shopServcie.getShopAuditInfo(withdrawId);
            logger.info("PCS32：获取提现校准信息接口    -end");
            
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取提现校准信息成功！", resultMap, DateUtils.DATETIME_FORMAT);
            
            
        }
        catch (ServiceException e)
        {
            logger.error("PCS32：获取提现校准信息接口 -异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("PCS32：获取提现校准信息接口 -系统异常", e);
            throw new APISystemException("PCS32：获取提现校准信息接口-系统异常", e);
        }
    }
    /**
     * PCS33：连锁总部申请提现接口
     * @param request
     * @return
     */
	@RequestMapping(value={"/token/shop/headquartersWithdraw","/service/shop/headquartersWithdraw","/session/shop/headquartersWithdraw"},produces="application/json;charset=UTF-8")
	@ResponseBody 
	public Object headquartersWithdraw(HttpServletRequest request) throws Exception {

    	/**
    	 * 1、校验店铺信息
    	 * 2、扣出分店收入，转入总店（记录分店一条支付记录、总店一条入账记录）
    	 * 3、生成一条总店记录
    	 */
		logger.info("PCS33：连锁总部申请提现接口 -start");
		
		WithdrawRequestModel requestModel = getRequestModel(request, WithdrawRequestModel.class);
		
		//验证支付密码
		String dbPassWord = shopServcie.getShopPasswordById(requestModel.getShopId());
		if(!StringUtils.equals(dbPassWord, requestModel.getPayPassword())) {
			throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
		}
		List<Map<String, Object>> resultList  = new ArrayList<Map<String,Object>>();

		List<WithdrawListDto> withdrawListDtos  = requestModel.getWithdrawList();
		for (WithdrawListDto withdrawDto : withdrawListDtos) {
			Map<String, Object> resultMap = withdrawByShop(withdrawDto, requestModel.getCardNumber());
			resultList.add(resultMap);
		}

		logger.info("PCS33：连锁总部申请提现接口 -end");
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "申请提现成功！", resultList);
		
	}
	private Map<String, Object> withdrawByShop(WithdrawListDto withdrawDto,String cardNumber) throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		Long shopId = withdrawDto.getShopId();
        // 商铺存在性 
        ShopDto shopDto = this.shopServcie.getShopById(shopId);
        CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        
		ShopWithDrawDto shopWithDrawDto = new ShopWithDrawDto();
		shopWithDrawDto.setHandleMark(shopDto.getShopName());
		shopWithDrawDto.setAmount(withdrawDto.getMoney());
		shopWithDrawDto.setShopId(shopId);
//		shopWithDrawDto.setUserName(userName);
		shopWithDrawDto.setTerminalType("WEB");
//		shopWithDrawDto.setBankName(bankName);
		shopWithDrawDto.setCardNumber(cardNumber);
		//默认为未审核状态
		shopWithDrawDto.setWithdrawStatus(CommonConst.WITHDRAW_STATUS_TODO);
		
		result = payServcie.withdrawByShop(shopWithDrawDto);
		
		return result;
	}
	
    /**
     * PCS33：连锁总部申请提现接口
     * @param request
     * @return
     */
	@RequestMapping(value={"/token/shop/depositChange","/service/shop/depositChange","/session/shop/depositChange"},produces="application/json;charset=UTF-8")
	@ResponseBody 
	public Object depositChange(HttpServletRequest request) throws Exception {

		logger.info("PCS34：商铺保证金转移接口 -start");
/*		shopId	int		是	商铺ID。
		money	int		是	转换金额
		accountType	int		是	账户类型。：1-线上收入账户  2-奖励账户 3-平台账户*/
		
		Map<String, Object> parms = getRequestMap(request);
		
        CommonValidUtil.validObjectNull(parms.get("shopId"), CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        CommonValidUtil.validObjectNull(parms.get("money"), CodeConst.CODE_PARAMETER_NOT_NULL, "money不能为空");
        CommonValidUtil.validObjectNull(parms.get("accountType"), CodeConst.CODE_PARAMETER_NOT_NULL, "accountType不能为空");		
		Long shopId = NumberUtil.strToLong((String)parms.get("shopId"), "shopId");
		Double money = NumberUtil.strToDouble((String)parms.get("money"), "money");
		Integer accountType = NumberUtil.strToInteger((String)parms.get("accountType"), "accountType");
		Integer billType = NumberUtil.strToInteger((String)parms.get("billType"), "billType");
		payServcie.depositChange(shopId, accountType, money, billType);
		

		logger.info("PCS34：商铺保证金转移接口 -end");
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "保证金转移成功", null);
	}
    /**
     * PCS39：获取商铺营收统计接口
     * @param request
     * @return
     */
	@RequestMapping(value={"/token/shop/shopIncomeStat","/service/shop/shopIncomeStat","/session/shop/shopIncomeStat"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object shopIncomeStat(HttpServletRequest request, Long shopId, String startDate, String endDate) throws Exception {
		logger.info("PCS39：获取商铺营收统计接口");
        // 校验商铺是否存在
        if (null == shopId) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
        }
        //校验店铺是否存在
        collectService.checkShopExists(shopId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateD = null;
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isBlank(startDate))  //如果为空则为当月第一天
        {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MINUTE,0);
            //去掉毫秒数的影响
            startDateD = dateFormat.parse(dateFormat.format(calendar.getTime()));
        }else{
            startDateD = dateFormat.parse(startDate.trim());
        }
        //截止日期如果为空则为当前时间
        Date endDateD =  null;
        if(StringUtils.isBlank(endDate)){
            endDateD = new Date();
        }else{
            calendar.setTime(dateFormat.parse(endDate.trim()));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            endDateD = calendar.getTime();
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("shopId", shopId);
        searchParams.put("startDate", startDateD);
        searchParams.put("endDate", endDateD);
        Map<String, Object> tempRs = collectService.getShopIncomeStat(searchParams);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询店铺收入信息成功!", tempRs);
	}

    /**
     * PCS48：查询商铺导购员业绩列表接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/token/shop/getSalerPerformanceList","/service/shop/getSalerPerformanceList","/session/shop/getSalerPerformanceList"},produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getSalerPerformanceList(HttpServletRequest request, Long shopId, String startTime, String endTime,Integer pSize, Integer pNo, 
    		String sortBy,Integer orderBy) throws Exception {
        logger.info("PCS48：查询商铺导购员业绩列表接口-start");
        // 校验商铺是否存在
        if (null == shopId) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
        }

        Date startDate = null;
        Date endDate = null;
        if(StringUtils.isNotBlank(startTime))   //如果传入了时间
        {
            startDate = DateUtils.parse(startTime);
        } else {    //否则起始日期为当前月第一天
            Calendar calendar =  Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            startDate = DateUtils.parse(DateUtils.format(startDate, "yyyy-MM-dd"));
        }
        if(StringUtils.isNotBlank(endTime)) //如果传入了截止日期
        {
            endDate = DateUtils.parse(endTime);
        }else { //否则截止日期为当前时间
            endDate = new Date();
        }

        if(null == pSize)
        {
            pSize = 10;
        }
        if(null == pNo)
        {
            pNo = 1;
        }

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("shopId", shopId);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("limit", (pNo-1)*pSize);
        searchParams.put("pSize", pSize);
        searchParams.put("sortBy", sortBy);
        searchParams.put("orderBy", orderBy);

        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("lst", shopServcie.getSalerPerformanceList(searchParams));
        resultMap.put("rCount", shopServcie.getSalerPerformanceCount(searchParams));
        resultMap.put("pNo", pNo);
        
        logger.info("PCS48：查询商铺导购员业绩列表接口-end");
        
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询成功", resultMap);
    }

    /**
     * PCS49：查询商铺导购员业绩明细接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/token/shop/getSalerPerformanceDetail","/service/shop/getSalerPerformanceDetail","/session/shop/getSalerPerformanceDetail"},produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getSalerPerformanceDetail(HttpServletRequest request, Long shopId, String startTime, String endTime, Long employeeId, Integer detailType, Integer pSize, Integer pNo, String orderField, Integer orderDir) throws Exception {
        logger.info("PCS49：查询商铺导购员业绩明细接口");
        // 校验商铺是否存在
        if (null == shopId) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
        }
        //校验店铺是否存在
        collectService.checkShopExists(shopId);

        // 校验雇员Id是否存在
        if (null == employeeId) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "employeeId不能为空");
        }
        // 校验雇员detailType是否存在
        if (null == detailType) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "detailType不能为空");
        }
        Date startDate = null;
        Date endDate = null;
        if(StringUtils.isNotBlank(startTime))   //如果传入了时间
        {
            startDate = DateUtils.parse(startTime);
        } else {    //否则起始日期为当前月第一天
            Calendar calendar =  Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            startDate = DateUtils.parse(DateUtils.format(startDate, "yyyy-MM-dd"));
        }
        if(StringUtils.isNotBlank(endTime)) //如果传入了截止日期
        {
            endDate = DateUtils.parse(endTime);
        }else { //否则截止日期为当前时间
            endDate = new Date();
        }

        if(null == pSize)
        {
            pSize = 10;
        }
        if(null == pNo)
        {
            pNo = 1;
        }

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("shopId", shopId);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("employeeId", employeeId);
//        searchParams.put("limit", (pNo-1)*pSize);
        searchParams.put("pNo", pNo);
        searchParams.put("pSize", pSize);
        searchParams.put("orderField", orderField);
        searchParams.put("orderDir", orderDir);
        Map<String, Object> tempRs = null;
        if(1 == detailType) //根据订单查详情
        {
            tempRs = collectService.getSalerPerformanceByOrder(searchParams);
        } else if(2 == detailType){ //根据商品查详情
            tempRs = collectService.getSalerPerformanceByOrderGoods(searchParams);
        }

        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询成功", tempRs);
    }
    
    /**
     * PCS50 ：按消费时段分析客户消费情况接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/token/shop/shopIncomeStatByTimePeriod","/service/shop/shopIncomeStatByTimePeriod","/session/shop/shopIncomeStatByTimePeriod"},produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object shopIncomeStatByTimePeriod(HttpServletRequest request) throws Exception {
        logger.info("PCS50 ：按消费时段分析客户消费情况接口");
        
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String startDate = RequestUtils.getQueryParam(request, "startDate");
        String endDate = RequestUtils.getQueryParam(request, "endDate");
        String startDatePeriod = RequestUtils.getQueryParam(request, "startDatePeriod");
        String endDatePeriod = RequestUtils.getQueryParam(request, "endDatePeriod");
        String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        //校验店铺是否存在
        collectService.checkShopExists(shopId);
        // 分页默认10条，第1页
		Integer pNo = PageModel.handPageNo(pNoStr);
		Integer pSize = PageModel.handPageSize(pSizeStr);
        
        
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("shopId", shopId);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("startDatePeriod", startDatePeriod);
        searchParams.put("endDatePeriod", endDatePeriod);
        searchParams.put("orderStatus", CommonConst.ORDER_STATUS_FINISH);
        searchParams.put("pNo", (pNo - 1) * pSize);
        searchParams.put("pSize", pSize);
        PageModel pageModel = shopServcie.shopIncomeStatByTimePeriod(searchParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("rCount", pageModel.getTotalItem());
		resultMap.put("pNo", pNo);
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询成功", resultMap);
    }
    
    /**
     * PCS50 ：按消费时段分析客户消费情况接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/token/shop/getHandoverList","/service/shop/getHandoverList","/session/shop/getHandoverList"},produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getHandoverList(HttpServletRequest request) throws Exception {
        logger.info("CS26：获取交接班记录列表接口-start");
        
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String startTime = RequestUtils.getQueryParam(request, "startTime");
        String endTime = RequestUtils.getQueryParam(request, "endTime");
        String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
        
        HandoverInfoDto searchParams = new HandoverInfoDto();
        
        // 验证商铺
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        searchParams.setShopId(shopId);
        
        // startTime
        if (StringUtils.isNotBlank(startTime))
        {
            CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            searchParams.setStartTime(startTime);
        }
        // endTime
        if (StringUtils.isNotBlank(endTime))
        {
            CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
            searchParams.setEndTime(endTime);
        }
        
        // 分页默认10条，第1页
		Integer pNo = PageModel.handPageNo(pNoStr);
		Integer pSize = PageModel.handPageSize(pSizeStr);
        searchParams.setPageNo((pNo - 1) * pSize);
        searchParams.setPageSize(pSize);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("lst", handoverInfoServcie.getShopHandoverInfoList(searchParams));
		resultMap.put("rCount", handoverInfoServcie.getShopHandoverInfoListCount(searchParams));
		resultMap.put("pNo", pNo);
		
        logger.info("CS26：获取交接班记录列表接口-end");

        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询交接班成功", resultMap,"yyyy-MM-dd HH:mm");
    }
    
	/**
	 * CS25：同步交接班信息接口成功
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/shop/syncHandoverInfo",
			"/service/shop/syncHandoverInfo",
			"/session/shop/syncHandoverInfo" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object syncHandoverInfo(HttpServletRequest request) throws Exception {
		
		HandoverInfoDto handoverInfoDto = getRequestModel(request, HandoverInfoDto.class);
		
		Long handoverInfoId  = handoverInfoServcie.insertShopHandoverInfo(handoverInfoDto);
		
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("handoverInfoId", handoverInfoId);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "CS25：同步交接班信息接口成功！",reMap);
	}
    
    public static void main(String[]args){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        System.out.println(dateFormat.format(calendar.getTime()));
    }

}
