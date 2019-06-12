package com.idcq.appserver.controller.goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.common.AsynchronousTask.producer.MqPusher;
import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.controller.shop.ShopController;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.IUnitDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.dto.goods.BatchUpdateGoodsModel;
import com.idcq.appserver.dto.goods.GoodsAvsDto;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.GoodsSoldOutDto;
import com.idcq.appserver.dto.goods.GoodsUnitDto;
import com.idcq.appserver.dto.goods.GroupPropertyModel;
import com.idcq.appserver.dto.goods.SyncGoodsInfoDto;
import com.idcq.appserver.dto.goods.TopGoodsDto;
import com.idcq.appserver.dto.goods.UpdateGoodsAvs;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.goods.IGoodsSetService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.ReflectionUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
import com.idcq.appserver.utils.mq.goods.GoodsMessageUtil;
import com.idcq.appserver.utils.pinyin.PinyinUtil;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.appserver.utils.solr.GroupResult;
import com.idcq.appserver.utils.solr.SearchContent;
import com.idcq.appserver.utils.solr.SolrContext;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupProValuesDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsPropertyDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupPropertyService;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;

/**
 * 商品（服务）controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:38:07
 */
@Controller
public class GoodsController extends BaseController
{
	private final static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    public IGoodsServcie goodsService;

    @Autowired
    public IShopServcie shopServcie;

    @Autowired
    private IGoodsSetService goodsSetService;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IUnitDao unitDao;

    @Autowired
    private IRegionDao regionDao;

    @Autowired
    private IAttachmentRelationDao attachmentRelationDao;

    @Autowired
    private IGoodsGroupService goodsGroupService;

    @Autowired
    private IGoodsGroupPropertyService goodsGroupPropertyService;

    @Autowired
    private IGoodsGroupProValuesDao goodsGroupProValueDao;

    @Autowired
    private IGoodsPropertyDao goodsPropertyDao;

    @Autowired
    private ICollectService collectService;
    
    @Autowired
    private ShopController shopController;

	@Autowired
	public IGoodsGroupDao goodsGroupDao;
	
	@Autowired
	private ILevelService levelService;
	
	@Autowired
	private IGoodsDao goodsDao;
    /**
     * 获取热卖品列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/goods/getHotGoods", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getTopGoodsList(HttpServletRequest request)
    {
        try
        {
            logger.info("获取热卖品列表-start");
            String topType = RequestUtils.getQueryParam(request, "topType");
            String cityId = RequestUtils.getQueryParam(request, "cityId");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            CommonValidUtil.validPositLong(cityId, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_CITYID);
            if (StringUtils.isBlank(topType))
            {
                topType = CommonConst.GOODS_HOT; // 默认筛选热卖商品
            }
            else if (!StringUtils.equals(CommonConst.GOODS_GOOD, topType)
                    && !StringUtils.equals(CommonConst.GOODS_HOT, topType))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_TOPTYPE);
            }
            TopGoodsDto topGoods = new TopGoodsDto();
            topGoods.setCityId(Integer.valueOf(cityId));
            topGoods.setTopType(topType);
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.goodsService.getTopGoodsList(topGoods, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());

            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取热卖品列表成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取热卖品列表-系统异常", e);
            throw new APISystemException("获取热卖品列表-系统异常", e);
        }
    }

    /**
     * 获取商品评论
     * 
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value = "/comment/getGoodsComments")
    @ResponseBody
    public ResultDto getGoodsComments(HttpServletRequest request)
    {
        try
        {
            long start = System.currentTimeMillis();
            logger.info("获取指定商品评论-start:" + start);
            String goodsId = RequestUtils.getQueryParam(request, "goodsId");
            String pageNO = RequestUtils.getQueryParam(request, "pNo");
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            CommonValidUtil.validObjectNull(goodsId, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsId不能为空");
            CommonValidUtil.validNumStr(goodsId, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsId参数不合法");
            if (pageNO == null || pageSize == null)
            {
                pageNO = "1";
                pageSize = "10";
            }
            PageModel pageModel = this.goodsService.getGoodsComments(Long.valueOf(goodsId), Integer.valueOf(pageNO),
                    Integer.valueOf(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setLst(pageModel.getList());
            msgList.setrCount(pageModel.getTotalItem());
            List<MessageListDto> dataList = new ArrayList<MessageListDto>();
            dataList.add(msgList);
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取指定商品评论成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取指定商品评论-系统异常", e);
            throw new APISystemException("获取指定商品评论-系统异常", e);
        }
    }

    /**
     * 获取商铺中的商品分类
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = {"/goods/getShopGoodsCategory","/token/goods/getGoodsCategory",
    		"/session/goods/getGoodsCategory","/service/goods/getGoodsCategory"})
    @ResponseBody
    public ResultDto getShopGoodsCategory(HttpServletRequest request)
    {
        try
        {
            logger.info("获取商铺中的商品分类-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String goodsGroupId = RequestUtils.getQueryParam(request, "goodsGroupId");// 商品族ID
            String parentCategoryId = RequestUtils.getQueryParam(request, "parentCategoryId");
            String columnIdStr = RequestUtils.getQueryParam(request, "columnId");
            String goodsTypeStr = RequestUtils.getQueryParam(request, "goodsType");
            if (shopIdStr == null && columnIdStr == null)
            	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"商铺或行业ID至少填写一个");
            Integer pageNO = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
            Integer pageSize = PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
            
            Integer columnId = null;
            if (StringUtils.isNotBlank(columnIdStr)) {
            	columnId = CommonValidUtil.validStrIntFmt(columnIdStr, 
            			CodeConst.CODE_PARAMETER_NOT_VALID, "columnId类型错误");
            }
            Long shopId = null;
            if (StringUtils.isNotBlank(shopIdStr)) {
                shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            }
           
            // 新增
            Integer goodsType = null;
            if (!StringUtils.isBlank(goodsTypeStr))
            {
                CommonValidUtil.validNumStr(goodsTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsType格式错误");
                goodsType = Integer.parseInt(goodsTypeStr);
            }
            PageModel pageModel = this.goodsService.getShopGoodsCategory(shopId,columnId, goodsGroupId, parentCategoryId,
                    pageNO, pageSize,goodsType);

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setLst(pageModel.getList());
            msgList.setrCount(pageModel.getTotalItem());
            List<MessageListDto> dataList = new ArrayList<MessageListDto>();
            dataList.add(msgList);

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺中的商品分类成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺中的商品分类-系统异常", e);
            throw new APISystemException("获取商铺中的商品分类-系统异常", e);
        }
    }
    /**
     * 商品状态变更接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/goods/updateGoodsInfo", "/token/goods/updateGoodsInfo", "/service/goods/updateGoodsInfo", "/session/goods/updateGoodsInfo"}, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object updateGoodsInfo(HttpServletRequest request){
        long startTime = System.currentTimeMillis();
        try {
            logger.info("商品状态变更接口    -start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String goodsIdStrs = RequestUtils.getQueryParam(request, "goodsIds");
            String operateTypeStr = RequestUtils.getQueryParam(request, "operateType");
            CommonValidUtil.validStrNull(goodsIdStrs, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_ID);
            
            List<Long> goodsIds = new ArrayList<Long>();
            String[] strs = goodsIdStrs.split(CommonConst.COMMA_SEPARATOR);
            for (int i = 0; i < strs.length; i++) {
                Long goodsId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_GOODS_ID);
                goodsIds.add(goodsId);
            }
            Integer operateType = null;
            if (StringUtils.isEmpty(operateTypeStr)) {
                //默认操作：上架
                operateType = 1;
            }else{
                operateType = CommonValidUtil.validStrIntFmt(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "操作类型格式错误");
            }
            Long shopId = Long.parseLong(shopIdStr);
            if (operateType == 1 || operateType == 2 || operateType == 0) {
                int re = collectService.updateGoodsInfo(shopId,goodsIds,operateType);
                Map<String, Object> result = null;
                if (re > 0) {
                    result = new HashMap<String, Object>();
                    result.put("goodsIds", goodsIdStrs);
                    result.put("operateTypeStr", operateTypeStr);
                    //清空缓存
                    for (Long goodsId : goodsIds)
                    {
                    	levelService.pushAddPointMessage(3, null, 1,shopId.intValue(), 3, goodsId.toString(),true);
                        DataCacheApi.del(CommonConst.KEY_GOODS+goodsId);
                    }
                    DataCacheApi.del(CommonConst.KEY_SHOP_TOP_GOODS + shopIdStr);
                    //推送消息
                    GoodsMessageUtil.pushGoodsMessage(goodsIds, Long.parseLong(shopIdStr), operateType);
                }
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商品状态更新成功", result);
            }else{
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "未知的操作类型");
            }
        }  catch (ServiceException e) {
            logger.error("商品状态变更接口    -异常", e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("商品状态变更接口    -系统异常", e);
            throw new APISystemException("商品状态变更接口    -系统异常", e);
        }finally{
            logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
        }
    }

    /**
     * 获取套餐中的商品详情接口
     * @Title: getGoodsSetDetail
     * @Description:
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "goods/getGoodsSetDetail", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getGoodsSetDetail(HttpServletRequest request)
    {
        try
        {
            String goodsSetId = RequestUtils.getQueryParam(request, "goodsSetId");
            CommonValidUtil.validObjectNull(goodsSetId, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsSetId不能为空");
            CommonValidUtil.validNumStr(goodsSetId, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsSetId格式错误");
            GoodsSetDto goodsSetEntity = (GoodsSetDto) DataCacheApi.getObject(CommonConst.KEY_GOODS_SET + goodsSetId);
            if (goodsSetEntity == null)// 不存在先去数据库找
            {

                return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "套餐不存在", new JSONObject());
            }
            List<GoodsSetDto> goodsSetDtos = goodsSetService.getGoodsIdListByGoodsSetId(Long.parseLong(goodsSetId));
            for (GoodsSetDto goodsSetDto : goodsSetDtos)
            {
                Long goodsId = goodsSetDto.getGoodsId();
                GoodsDto goodsDto = (GoodsDto) DataCacheApi.getObject("goods" + goodsId);
                if (goodsDto != null)
                {
                    Map<String, String> propsMap = new HashMap<String, String>();
                    propsMap.put("unitId", "unit");
                    DataConvertUtil.propertyConvertIncludeDefaultProp(goodsDto, goodsSetDto, propsMap);
                }
                if (!StringUtils.isEmpty(goodsSetDto.getUnit()))// 获取单元名称
                {
                    Unit unit = (Unit) DataCacheApi.getObject(CommonConst.KEY_UNIT + goodsSetDto.getUnit());
                    if (unit != null)
                    {
                        goodsSetDto.setUnit(unit.getUnitName());
                    }
                    else
                    {
                        unit = (Unit) HandleCacheUtil.getEntityCacheByClass(Unit.class, goodsSetDto.getUnit(), 21600);
                        if (unit != null)
                        {
                            goodsSetDto.setUnit(unit.getUnitName());
                        }
                    }
                }
                if (goodsSetDto.getGoodsCategoryId() != null)
                {
                    GoodsCategoryDto goodsCategoryDto = (GoodsCategoryDto) DataCacheApi
                            .getObject(CommonConst.KEY_GOODSCATEGORY + goodsSetDto.getGoodsCategoryId());
                    if (goodsCategoryDto != null)
                    {
                        goodsSetDto.setGoodsCategoryName(goodsCategoryDto.getCategoryName());
                    }
                }
                else
                {
                    GoodsCategoryDto goodsCategoryDto = (GoodsCategoryDto) HandleCacheUtil.getEntityCacheByClass(
                            GoodsCategoryDto.class, goodsSetDto.getGoodsCategoryId(), 21600);// 缓存6个小时
                    if (goodsCategoryDto != null)
                    {
                        goodsSetDto.setGoodsCategoryName(goodsCategoryDto.getCategoryName());
                    }
                }
                if (!StringUtils.isEmpty(goodsSetDto.getGoodsLogo1()))// 设置goodsLogo1属性
                {
                    Attachment attachment = attachmentDao.queryAttachmentById(goodsDto.getGoodsLogo1());
                    if (attachment != null)
                    {
                        goodsSetDto.setGoodsLogo1(attachment.getFileUrl());
                        goodsSetDto.setGoodsUrl(attachment.getFileUrl());
                    }
                }
                if (!StringUtils.isEmpty(goodsSetDto.getGoodsLogo2()))// 设置goodsLogo2属性
                {
                    Attachment attachment = attachmentDao.queryAttachmentById(goodsDto.getGoodsLogo2());
                    if (attachment != null)
                    {
                        goodsSetDto.setGoodsLogo2(attachment.getFileUrl());
                    }

                }
            }
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(1);
            msgList.setpSize(goodsSetDtos.size());
            msgList.setLst(goodsSetDtos);
            msgList.setrCount(goodsSetDtos.size());
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取商品套餐详情成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商品套餐详情产生异常", e);
            throw new APISystemException("查找商品套餐产生异常", e);
        }
    }

    /**
     * 获取商品族的商品
     * @Title: getGoodsGroupGoods
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "goods/getGoodsGroupGoodsList", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getGoodsGroupGoods(HttpServletRequest request)
    {
        try
        {

            
            //新增参数校验
            Map<String, Object>  parms = buildGroupGoodsParms(request);
            Long goodsGroupId =  (Long) parms.get("goodsGroupId");
            Long shopId =  (Long) parms.get("shopId");
            GoodsGroupDto goodsGroupDto = goodsGroupService.getGoodsGroupById(goodsGroupId);
            if (goodsGroupDto == null)
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "商品族不存在",
                        new HashMap<String, String>());
            }
            if (!(shopId + "").equals(goodsGroupDto.getShopId() + ""))
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "商品族不属于该商铺",
                        new HashMap<String, String>());
            }
            List<GoodsDto> goodsDtoList = goodsService.getGoodsByGroupMap(parms);
           
            Map<String, GoodsDto> goodsPicture = new HashMap<String, GoodsDto>();// 用来临时保存商品属性，最后统一查询缩略图后，再获取商品缩略图
            initGoodsPictureMap(goodsDtoList, goodsPicture);
            List<Long> needQueryGoodsPictureIdList = getGoodsIdList(goodsDtoList);
//            List<Long> allGoodsIdList = getGoodsIdAllList(goodsDtoList);
//            initGoodsProValue(goodsPicture, allGoodsIdList);// 获取商品的属性值 注释商品属性值，现版本商品属性值已经保存在goods表goodsProValue字段
            if (needQueryGoodsPictureIdList.size() > 0)// 一次性查找所有商品的缩略图
            {
                AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
                attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
                attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
                //附件
                List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao.findByConditionIn(
                        attachmentRelationDto, needQueryGoodsPictureIdList);
                parseGoodsPictureMap(attachmentRelationDtos, goodsPicture);
            }
            List<Map<String, Object>> goodsMap = DataConvertUtil.convertListObjToMap(goodsDtoList,
                    CommonResultConst.GET_GOODS_GROUP_DETAIL_FOR_GOODS);
            goodsGroupDto.setGoods(goodsMap);
            List<GoodsGroupPropertyDto> groupPropertyDtos = goodsGroupPropertyService
                    .getGoodsGroupPropertyListByGroupId(goodsGroupId);
            Map<Long, GoodsGroupPropertyDto> groupProperyMap = new HashMap<Long, GoodsGroupPropertyDto>();
            List<Long> groupPropertyIdList = new ArrayList<Long>();
            parseGoodsGroupPropertyList(groupPropertyDtos, groupProperyMap, groupPropertyIdList);
            if (groupPropertyIdList.size() > 0)
            {
                List<GoodsGroupProValuesDto> groupProValuesList = goodsGroupProValueDao
                        .getGoodsGroupProValuesList(groupPropertyIdList);
                parseGoodsGroupProValueList(groupProValuesList, groupProperyMap);
            }
            List<Map<String, Object>> goodsPropMap = DataConvertUtil.convertListObjToMap(groupPropertyDtos,
                    CommonResultConst.GET_GOODS_GROUP_DETAIL_FOR_PROPS);
            goodsGroupDto.setGroupProperties(goodsPropMap);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取商品族属性成功！",
                    DataConvertUtil.convertObjToMap(goodsGroupDto, CommonResultConst.GET_GOODS_GROUP_DETAIL));
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商品族的商品异常", e);
            throw new APISystemException("获取商品族的商品异常", e);
        }
    }

	private Map<String, Object> buildGroupGoodsParms(HttpServletRequest request)
			throws Exception {

		Map<String, Object> parms = new HashMap<String, Object>();
		String goodsCategoryIdStr = RequestUtils.getQueryParam(request,"goodsCategoryId");
		String storageAlarmType = RequestUtils.getQueryParam(request,"storeAlarmType");
		String goodsServerModeParam = RequestUtils.getQueryParam(request,"goodsServerMode");
		String goodsTypeStr = RequestUtils.getQueryParam(request, "goodsType");
		String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");
		String goodsProValuesIds = RequestUtils.getQueryParam(request, "goodsProValuesIds");
		String goodsProValuesNames = RequestUtils.getQueryParam(request, "goodsProValuesNames");

        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String goodsGroupIdStr = RequestUtils.getQueryParam(request, "goodsGroupId");
        
        CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
        parms.put("shopId", shopId);
        
        CommonValidUtil.validObjectNull(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsGroupId不能为空");
        Long goodsGroupId = NumberUtil.strToLong(goodsGroupIdStr, "goodsGroupId");
        
        parms.put("goodsGroupId", goodsGroupId);
		if (StringUtils.isEmpty(goodsStatus)) {
			goodsStatus = "1";

		} else {
			parms.put("goodsStatus",
					NumberUtil.strToNum(goodsStatus, "goodsStatus"));
		}
		
		if (StringUtils.isNotBlank(goodsTypeStr)) {
			parms.put("goodsCategoryId",
					NumberUtil.strToNum(goodsTypeStr, "goodsType"));
		}
		
		if (StringUtils.isNotBlank(storageAlarmType)) {
			parms.put("storageAlarmType",
					NumberUtil.strToNum(storageAlarmType, "storageAlarmType"));
		}
		
		if (StringUtils.isNotBlank(goodsCategoryIdStr)) {
			Long goodsCategoryId = CommonValidUtil.validStrLongFmt(
					goodsCategoryIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
					"goodsCategoryId格式错误");
			parms.put("goodsCategoryId", goodsCategoryId);
		}
		
		if (StringUtils.isNotEmpty(goodsServerModeParam)) {
			if ("2".equals(goodsServerModeParam)) {
				goodsServerModeParam = "2,3";
			} else if ("1".equals(goodsServerModeParam)) {
				goodsServerModeParam = "1,3";
			} else if ("3".equals(goodsServerModeParam)) {
				goodsServerModeParam = "1,2,3";
			}
			parms.put("goodsServerModeParam", goodsServerModeParam);
		}
		
		parms.put("goodsProValuesIds", goodsProValuesIds);
		parms.put("goodsProValuesNames", goodsProValuesNames);
		
		return parms;
	}
    /**
     * 初始化商品的属性值
     * @Title: initGoodsProValue
     * @param @param goodsMap
     * @param @param goodsIdList
     * @param @throws Exception
     * @return void 返回类型
     * @throws
     */
//    private void initGoodsProValue(Map<String, GoodsDto> goodsMap, List<Long> goodsIdList) throws Exception
//    {
//        if (goodsIdList.size() == 0)
//        {
//            return;
//        }
//        List<GoodsPropertyDto> goodsProDtoList = goodsPropertyDao.getGoodsPropertyByGoodsIdList(goodsIdList);
//        for (GoodsPropertyDto dto : goodsProDtoList)
//        {
//            GoodsDto goodsDto = goodsMap.get(dto.getGoodsId() + "");
//            if (goodsDto != null)
//            {
//                StringBuffer proBuffer = goodsDto.getGoodsProsValueIds();
//                if (proBuffer == null)
//                {
//                    proBuffer = new StringBuffer("");
//                }
//                proBuffer.append("," + dto.getProValuesId());
//                goodsDto.setGoodsProsValueIds(proBuffer);
//            }
//        }
//        for (Entry<String, GoodsDto> item : goodsMap.entrySet())
//        {
//            GoodsDto dto = item.getValue();
//            StringBuffer proBuffer = dto.getGoodsProsValueIds();
//            if (proBuffer != null)
//            {
//                StringBuffer buffer = proBuffer.replace(0, 4, "");
//                dto.setGoodsProsValueIds(buffer);
//            }
//        }
//
//    }

    /**
     * 初始化商品缩略图
     * @Title: initGoodsPictureMap
     * @param @param goodsDtoList
     * @param @param goodsPicture
     * @return void 返回类型
     * @throws
     */
    private void initGoodsPictureMap(List<GoodsDto> goodsDtoList, Map<String, GoodsDto> goodsPicture)
    {
        for (GoodsDto goodsDto : goodsDtoList)
        {
            goodsPicture.put(goodsDto.getGoodsId() + "", goodsDto);
        }
    }

    private void parseGoodsGroupPropertyList(List<GoodsGroupPropertyDto> groupPropertyDtos,
            Map<Long, GoodsGroupPropertyDto> groupProperyMap, List<Long> groupPropertyIdList)
    {
        for (GoodsGroupPropertyDto dto : groupPropertyDtos)
        {
            groupProperyMap.put(dto.getGroupPropertyId(), dto);
            groupPropertyIdList.add(dto.getGroupPropertyId());
        }
    }

    /**
     * 根据商品对象列表获取商品id列表
     * @Title: getGoodsIdList
     * @param @param goodsDtos
     * @param @return
     * @return List<Long> 返回类型
     * @throws
     */
    private List<Long> getGoodsIdList(List<GoodsDto> goodsDtos)
    {
        List<Long> idList = new ArrayList<Long>();
        for (GoodsDto gto : goodsDtos)
        {
            String goodsLogo = DataCacheApi.get(CommonConst.KEY_GOODS_PICTURE + gto.getGoodsId());
            if (StringUtils.isEmpty(goodsLogo))
            {
                idList.add(gto.getGoodsId());
            }
            else
            {
                gto.setGoodsLogo(goodsLogo);
            }
        }
        return idList;
    }

    /**
     * 获取所有的商品id
     * @Title: getGoodsIdAllList
     * @param @param goodsDtos
     * @param @return
     * @return List<Long> 返回类型
     * @throws
     */
    public List<Long> getGoodsIdAllList(List<GoodsDto> goodsDtos)
    {
        List<Long> idList = new ArrayList<Long>();
        for (GoodsDto gto : goodsDtos)
        {
            idList.add(gto.getGoodsId());
        }
        return idList;

    }

    /**
     * 解析属性值
     * @Title: parseGoodsGroupProValueList
     * @param @param valueDtos
     * @param @param groupProperyMap
     * @return void 返回类型
     * @throws
     */
    private void parseGoodsGroupProValueList(List<GoodsGroupProValuesDto> valueDtos,
            Map<Long, GoodsGroupPropertyDto> groupProperyMap)
    {
        for (GoodsGroupProValuesDto dto : valueDtos)
        {
            Long groupPropertyId = dto.getGroupPropertyId();
            GoodsGroupPropertyDto goodsGroupPropertyDto = groupProperyMap.get(groupPropertyId);
            if (goodsGroupPropertyDto != null)
            {
                goodsGroupPropertyDto.getGroupPropertyValues().add(dto);
            }
        }
    }

    /**
     * 搜索商品
     * @Title: searchGoods
     * @param @param request
     * @param @return
     * @param @throws Exception
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "goods/searchGoods", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String searchGoods(HttpServletRequest request)
    {
        try
        {
            String pageNO = RequestUtils.getQueryParam(request, "pNo");
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            String columnId = RequestUtils.getQueryParam(request, "columnId");// 行业分类
            String cityId = RequestUtils.getQueryParam(request, "cityId");// 所属市ID
            String districtId = RequestUtils.getQueryParam(request, "districtId");// 所属区、县ID
            String searchRadius = RequestUtils.getQueryParam(request, "searchRadius");// 搜索半径
            String townId = RequestUtils.getQueryParam(request, "townId");// 街道，镇id
            String orderBy = RequestUtils.getQueryParam(request, "orderBy");// 排序方式；0-离我最近，1-评价最好，2-订单最多（订单量降序）
            String searchKey = RequestUtils.getQueryParam(request, "searchKey");// 搜索关键字
            String longitude = RequestUtils.getQueryParam(request, "longitude");// 搜索时所处的经度
            String latitude = RequestUtils.getQueryParam(request, "latitude");// 搜索时所处的维度
            String goodsType = RequestUtils.getQueryParam(request, "goodsType");
            if (pageNO == null)
            {
                pageNO = "1";

            }
            if (pageSize == null)
            {
                pageSize = "10";
            }
            if (StringUtils.isEmpty(searchKey) && StringUtils.isEmpty(orderBy))
            {
                orderBy = "1";
            }
            CommonValidUtil.validObjectNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL, "cityId不能为空");
            CommonValidUtil.validNumStr(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, "cityId格式错误");
            SearchContent searchContent = new SearchContent();
            searchContent.setContentType(CommonConst.INDEX_TYPE_IS_GOODS);
            if (StringUtils.isEmpty(searchRadius))
            {
                searchRadius = "6371393";
            }
            if (!StringUtils.isEmpty(orderBy))// 不包括地理位置排序需要排序
            {
                if (CommonConst.LOACTION_ORDERBY.equals(orderBy))
                {
                    ReflectionUtils.setFieldValue(searchContent, "orderByLocation", CommonConst.SEARCH_ORDER_ASC);
                }
                else
                {
                    ReflectionUtils.setFieldValue(searchContent, CommonConst.SEARCH_GOODS_ORDERBY_MAP.get(orderBy),
                            CommonConst.SEARCH_ORDER_ASC);
                }
            }
            if (!StringUtils.isEmpty(longitude) && !StringUtils.isEmpty(latitude))
            {
                CommonValidUtil.validDoubleStr(searchRadius, CodeConst.CODE_PARAMETER_NOT_VALID, "搜索半径格式错误");
                searchRadius = (Double.parseDouble(searchRadius) / (double) 1000) + "";
                searchContent.setShopLocation(latitude + "," + longitude);
                searchContent.setSearchRadius(searchRadius);
            }
            ReflectionUtils.setValueNotEmpty(searchContent, "cityId", cityId);
            ReflectionUtils.setValueNotEmpty(searchContent, "goodsType", goodsType);
            ReflectionUtils.setValueNotEmpty(searchContent, "districtId", districtId);
            if (!StringUtils.isEmpty(searchKey) && searchKey.length() == 1)
            {
                ReflectionUtils.setValueNotEmpty(searchContent, "content", searchKey);
            }
            else
            {
                ReflectionUtils.setValueNotEmpty(searchContent, "goodsName", searchKey);
            }
            ReflectionUtils.setValueNotEmpty(searchContent, "townId", townId);
            ReflectionUtils.setValueNotEmpty(searchContent, "shopColumnId", columnId);
            searchContent.setShopStatus(CommonConst.SHOP_STATUS_NORMAL);
            searchContent.setGoodsStatus(CommonConst.GOODS_STATUS_NORMAL);
            PageModel pageModel = new PageModel();
            pageModel.setToPage(Integer.parseInt(pageNO));
            pageModel.setPageSize(Integer.parseInt(pageSize));
            pageModel = SolrContext.getInstance().queryResult(searchContent, pageModel);
            if (pageModel.getList() != null)
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
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "搜索商品成功", msgList, DateUtils.TIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商品套餐详情产生异常", e);
            throw new APISystemException("查找商品套餐产生异常", e);
        }
    }

    /**
     * 将搜索结果进行转换
     * @param searchContents
     * @return
     */
    private List convertSearchResult(List<SearchContent> searchContents, PageModel pageModelParam) throws Exception
    {
        try
        {
            List<GoodsDto> goodsDtos = new ArrayList<GoodsDto>();
            List resultData = new ArrayList();
            for (SearchContent searchContent : searchContents)
            {
                GoodsDto goodsDto = new GoodsDto();
                DataConvertUtil
                        .propertyConvertIncludeDefaultProp(searchContent, goodsDto, CommonConst.SEARCH_GOODS_MAP);
                if (goodsDto.getShopId() != null)
                {
                    ShopDto shopTarget = shopServcie.getShopExtendByIdAndStatus(goodsDto.getShopId(), CommonConst.SHOP_STATUS_NORMAL);
                    if (shopTarget != null)
                    {
                        goodsDto.setShopName(shopTarget.getShopName());
                    }
                }
                if (goodsDto.getDistance() != null)
                {
                    goodsDto.setDistance(1000 * goodsDto.getDistance());
                }
                if (goodsDto.getDistrictId() != null)
                {
                    DistrictDto districtDto = (DistrictDto) DataCacheApi.getObject(CommonConst.KEY_DISTRICT
                            + goodsDto.getDistrictId());
                    if (districtDto == null)
                    {
                        districtDto = regionDao.getDistrictById((long) goodsDto.getDistrictId());
                        if (districtDto != null)
                        {
                            DataCacheApi.setObject(CommonConst.KEY_DISTRICT + goodsDto.getDistrictId(), districtDto);
                        }
                    }
                    if (districtDto != null)
                    {
                        goodsDto.setDistrictName(districtDto.getDistrictName());
                    }
                }
                // goodsDto.setGoodsLogo1(null);
                Map<String, Object> goodsItemMap = DataConvertUtil.convertObjToMap(goodsDto,
                        CommonResultConst.SEARCH_GOODS);
                // 查找商品的缩略图
                AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
                attachmentRelationDto.setBizId(goodsDto.getGoodsId());
                attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
                attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
                List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao
                        .findByCondition(attachmentRelationDto);
                if (attachmentRelationDtos != null && attachmentRelationDtos.size() > 0)
                {
                    goodsItemMap.put("goodsLogo1",
                            FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl()));
                }
                if (goodsDto.getGoodsLogo1() != null)
                {
                    Object attachmentTarget = attachmentDao.queryAttachmentById(goodsDto.getGoodsLogo1());
                    if (attachmentTarget != null)
                    {
                        Attachment attachment = (Attachment) attachmentTarget;
                        if (attachment != null)
                        {
                            goodsItemMap.put("goodsUrl", FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
                        }
                    }
                }
                if (goodsDto.getGoodsLogo2() != null)
                {
                    Attachment attachmentLogo2Target = attachmentDao.queryAttachmentById(goodsDto.getGoodsLogo2());
                    if (attachmentLogo2Target != null)
                    {
                        Attachment attachment = (Attachment) attachmentLogo2Target;
                        if (attachment != null)
                        {
                            goodsItemMap.put("goodsLogo2", FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
                        }
                    }
                }
                if (goodsDto.getUnitId() != null)
                {
                    Object unitTarget = HandleCacheUtil.getEntityCacheByClass(Unit.class, goodsDto.getUnitId(), 21600);
                    if (unitTarget != null)
                    {
                        Unit unit = (Unit) unitTarget;
                        if (unit != null)
                        {
                            goodsItemMap.put("unit", unit.getUnitName());
                        }
                    }
                }
                resultData.add(goodsItemMap);
            }
            return resultData;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * 搜索商铺的商品
     * @Title: searchShopGoods
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "goods/searchShopGoods", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String searchShopGoods(HttpServletRequest request)
    {
        try
        {
            PageModel pageModel = null;
            String searchSource = RequestUtils.getQueryParam(request, "searchSource");
            if (StringUtils.isEmpty(searchSource))
            {
                searchSource = "0";
            }
            MessageListDto msgList = new MessageListDto();
            if ("0".equals(searchSource))
            {
                ShopController.generateSearchHistory(request);
                pageModel = shopController.commonSearch(request, null, 1);// 绗笁涓弬鏁帮紝浠ｈ〃杩斿洖缁撴灉闇�鍒嗙粍
                msgList.setrCount(pageModel.getTotalItem());
                msgList.setpNo(pageModel.getToPage());
                msgList.setpSize(pageModel.getPageSize());
                msgList.setLst(parseGroupResult((List<GroupResult>) pageModel.getList()));

            }
            else
            {
                pageModel = shopController.commonSearch(request, null, 2);
                msgList.setrCount(pageModel.getTotalItem());
                msgList.setpNo(pageModel.getToPage());
                msgList.setpSize(pageModel.getPageSize());
                msgList.setLst(parseCommonResult((List<SearchContent>) pageModel.getList()));
            }
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "搜索店铺与商品成功", msgList, DateUtils.TIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("搜索店铺与商品产生异常", e);
            throw new APISystemException("搜索店铺与商品产生异常", e);
        }
    }

    private List<Map<String, Object>> parseCommonResult(List<SearchContent> searchContents)
    {
        List<Long> needQueryShopIdList = new ArrayList<Long>();
        List<ShopDto> existShops = new ArrayList<ShopDto>();
        Map<String, Integer> shopIndexMap = new HashMap<String, Integer>();// 保存每个商铺编号的位置，避免，排序打乱
        List<Long> needQueryLogoShopIdList = new ArrayList<Long>();// 代表这些商品在缓存中不存在
        Map<Long, Double> distanceMap = new HashMap<Long, Double>();
        List<Long> needQueryShopIdForGoods = new ArrayList<Long>();// 如果某个商铺找出来的商品数量为0,则从数据库中查询两个销量最高的商品
        Map<String, Map<String, Object>> goodsPicture = new HashMap<String, Map<String, Object>>();// 用来临时保存商品属性，最后统一查询缩略图后，再获取商品缩略图
        Map<String, Map<String, Object>> goodsGroupPicture = new HashMap<String, Map<String, Object>>();// 用来临时保存商品属性，最后统一查询缩略图后，再获取商品缩略图
        List<Long> needQueryGoodsPictureIdList = new ArrayList<Long>();// 需要查询缩略图的商品列表集合
        List<Long> needQueryGoodsGroupPictureIdList = new ArrayList<Long>();
        int index = 0;
        Map<Long, List<ShopDto>> needQueryDistrictMap = new HashMap<Long, List<ShopDto>>();// 需要查询商铺的区县
        Map<Long, ShopDto> shopMap = new HashMap<Long, ShopDto>();
        for (SearchContent searchContent : searchContents)
        {
            try
            {
                Long shopId = Long.parseLong(searchContent.getShopId());
                ShopDto shopDto = (ShopDto) DataCacheApi.getObject(CommonConst.KEY_SHOP + shopId);
                shopIndexMap.put(shopId + "", index++);
                if (shopDto == null)
                {
                    needQueryShopIdList.add(shopId);
                    needQueryLogoShopIdList.add(shopId);
                    needQueryShopIdForGoods.add(shopId);
                    if (searchContent.getDistance() != null)
                    {
                        distanceMap.put(shopId, NumberUtil.formatDoubleForSolr(searchContent.getDistance() * 1000, 2));
                    }
                }
                else
                {
                    existShops.add(shopDto);
                    if (searchContent.getDistance() != null)
                    {
                        distanceMap.put(shopId, NumberUtil.formatDoubleForSolr(searchContent.getDistance() * 1000, 2));
                    }
                    String shopLogoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopDto.getShopId());
                    if (!StringUtils.isEmpty(shopLogoUrl))
                    {
                        shopDto.setShopLogoUrl(shopLogoUrl);
                    }
                    else
                    {
                        needQueryLogoShopIdList.add(shopDto.getShopId());
                    }
                    List<Map<String, Object>> cacheShopTopGoods = (List<Map<String, Object>>) DataCacheApi
                            .getObject(CommonConst.KEY_SHOP_TOP_GOODS + shopDto.getShopId());// 商铺销量最高的商品
                    if (cacheShopTopGoods != null)
                    {
                        shopDto.setGoods(cacheShopTopGoods);
                    }
                    else
                    {
                        needQueryShopIdForGoods.add(shopId);
                    }
                    if (shopDto.getDistance() != null)
                    {
                        shopDto.setDistance(NumberUtil.formatDoubleForSolr(searchContent.getDistance() * 1000, 2));
                    }
                    initDistrictInfo(shopDto, needQueryDistrictMap);
                    shopMap.put(shopDto.getShopId(), shopDto);
                }

            }
            catch (Exception e)
            {
                logger.error("解析搜索结果过程中产生了异常", e);
            }
        }
        if (needQueryShopIdList.size() > 0)// 代表需要从数据库中查找这些商铺
        {
            List<ShopDto> persitShopDtos = shopServcie.getShopListByIds(needQueryShopIdList);//
            for (ShopDto shopItem : persitShopDtos)
            {
                shopItem.setDistance(distanceMap.get(shopItem.getShopId()));
                initDistrictInfo(shopItem, needQueryDistrictMap);
                String cacheKey = CommonConst.KEY_SHOP + shopItem.getShopId();
                DataCacheApi.setObjectEx(cacheKey, shopItem, 43200);// 存入redis
                shopItem.setDistance(distanceMap.get(shopItem.getShopId()));
                existShops.add(shopItem);
                shopMap.put(shopItem.getShopId(), shopItem);
            }
        }
        if (needQueryLogoShopIdList.size() > 0)// 批量查询商铺Logo被放入缓存
        {
            try
            {
                convertShopLogo(needQueryLogoShopIdList, shopMap);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsMap = null;
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsGroupMap = null;// 用来保存商铺销量最高的商品族
        if (needQueryShopIdForGoods.size() > 0)// 代表这些商铺列表没有在solr中查到相应的商品，需要从数据库中查找
        {
            try
            {
                List<GoodsGroupDto> goodsGroupDtoList = goodsGroupService.getListByShopIdListAndNum(
                        needQueryShopIdForGoods, 2);
                needCacheShopGoodsGroupMap = parseGoodsGroupForShop(goodsGroupDtoList, shopMap,
                        needQueryGoodsGroupPictureIdList, goodsGroupPicture, needQueryShopIdForGoods);
                if (needQueryShopIdForGoods.size() > 0)
                {
                    List<GoodsDto> groupGoods = goodsService.getGoodsGroupByShopIdList(needQueryShopIdForGoods, 2);
                    needCacheShopGoodsMap = parseGoodsForShopGroup(groupGoods, shopMap, needQueryGoodsPictureIdList,
                            goodsPicture);// 针对商品查找店铺销量最高的商品
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (needCacheShopGoodsMap != null && needCacheShopGoodsMap.size() > 0)
        {
            writeShopTopGoodsToRedis(needCacheShopGoodsMap);// 将商铺销量最高的商品放入缓存
        }
        if (needCacheShopGoodsGroupMap != null && needCacheShopGoodsGroupMap.size() > 0)// 将销量最高的商品族放入缓存
        {
            writeShopTopGoodsGroupToRedis(needCacheShopGoodsGroupMap);
        }
        sortShop(existShops, shopIndexMap);
        if (needQueryDistrictMap.size() > 0)
        {
            Set<Long> districtIdSet = needQueryDistrictMap.keySet();
            List<DistrictDto> districtList = regionDao.getDistrictByIdList(districtIdSet);
            writeDistrictToRedis(districtList, needQueryDistrictMap);
        }
        dealDistance(existShops, distanceMap);// 处理距离
        return DataConvertUtil.convertCollectionToListMap(existShops, CommonResultConst.GET_GROUP_SHOP);
    }

    /**
     * 暂时统一处理距离
     */
    private void dealDistance(List<ShopDto> shopDtos, Map<Long, Double> distanceMap)
    {
        for (ShopDto shopDto : shopDtos)
        {
            Double distance = distanceMap.get(shopDto.getShopId());
            shopDto.setDistance(distance);
        }
    }

    /**
     * @Title: parseGroupResult
     * @param @param results
     * @param @return
     * @return List<ShopDto> 返回类型
     * @throws
     */
    private List<Map<String, Object>> parseGroupResult(List<GroupResult> results) throws Exception
    {
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
        attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
        List<ShopDto> shopDtos = new ArrayList<ShopDto>();
        Map<String, List<Map<String, Object>>> shopGoods = new HashMap<String, List<Map<String, Object>>>();// 用来临时保存每个商铺对应的分组商品，因为这些商铺需要从数据库查出来
        Map<String, Map<String, Object>> goodsPicture = new HashMap<String, Map<String, Object>>();// 用来临时保存商品属性，最后统一查询缩略图后，再获取商品缩略图
        Map<String, Map<String, Object>> goodsGroupPicture = new HashMap<String, Map<String, Object>>();// 用来临时保存商品属性，最后统一查询缩略图后，再获取商品缩略图
        List<Long> needQueryGoodsPictureIdList = new ArrayList<Long>();// 需要查询缩略图的商品列表集合
        List<Long> needQueryGoodsGroupPictureIdList = new ArrayList<Long>();
        List<Long> needQueryShopIdList = new ArrayList<Long>();
        Map<Long, Double> distanceMap = new HashMap<Long, Double>();
        List<Long> needQueryShopIdForGoods = new ArrayList<Long>();// 如果某个商铺找出来的商品数量为0,则从数据库中查询两个销量最高的商品
        Map<Long, ShopDto> shopMap = new HashMap<Long, ShopDto>();
        List<Long> needQueryLogoShopIdList = new ArrayList<Long>();// 代表这些商品在缓存中不存在
        Map<String, Integer> shopIndexMap = new HashMap<String, Integer>();// 保存每个商铺编号的位置，避免，排序打乱
        Integer shopIndex = 0;
        Map<Long, List<ShopDto>> needQueryDistrictMap = new HashMap<Long, List<ShopDto>>();
        for (GroupResult groupResult : results)
        {
            List<SearchContent> searchContents = groupResult.getData();
            ShopDto shopDto = null;
            List<Map<String, Object>> goodsList = new ArrayList<Map<String, Object>>();
            Double distance = null;
            for (SearchContent searchContent : searchContents)
            {
                if (CommonConst.INDEX_TYPE_IS_GOODS.equals(searchContent.getContentType()))
                {
                    GoodsDto goodsDto = new GoodsDto();
                    DataConvertUtil.propertyConvertIncludeDefaultProp(searchContent, goodsDto,
                            CommonConst.SEARCH_GOODS_MAP);
                    Map<String, Object> goodsPropertyMap = DataConvertUtil.convertObjToMap(goodsDto,
                            CommonResultConst.GET_GROUP_GOOD);
                    goodsPropertyMap.put("categoryId", goodsDto.getGoodsCategoryId());
                    if (!searchContent.getId().startsWith("goodsGroup"))// 代表不是商品族
                    {
                        String pictureUrl = DataCacheApi.get(CommonConst.KEY_GOODS_PICTURE + goodsDto.getGoodsId());
                        if (StringUtils.isEmpty(pictureUrl))// 代表缓存中没有此商品的缩略图
                        {
                            goodsPicture.put(goodsDto.getGoodsId() + "", goodsPropertyMap);
                            needQueryGoodsPictureIdList.add(goodsDto.getGoodsId());
                        }
                        else
                        {
                            goodsPropertyMap.put("goodsLogo", pictureUrl);
                        }
                    }
                    else
                    // 代表是商品族
                    {
                        String pictureUrl = DataCacheApi
                                .get(CommonConst.KEY_GOODSGROUP_PICTURE + goodsDto.getGoodsId());// 从缓存中获取缩略图
                        if (StringUtils.isEmpty(pictureUrl))// 代表缓存中没有此商品的缩略图
                        {
                            goodsGroupPicture.put(goodsDto.getGoodsId() + "", goodsPropertyMap);
                            needQueryGoodsGroupPictureIdList.add(goodsDto.getGoodsId());
                        }
                        else
                        {
                            goodsPropertyMap.put("goodsLogo", pictureUrl);
                        }
                    }
                    if (searchContent.getId().startsWith("goodsGroup"))
                    {
                        goodsPropertyMap.put("goodsGroupId", goodsDto.getGoodsId());
                    }
                    else
                    {
                        goodsPropertyMap.put("minPrice", goodsDto.getStandardPrice());
                    }
                    if (goodsList.size() < 2)
                    {
                        goodsList.add(goodsPropertyMap);// 只返回需要的属性
                    }
                    distance = goodsDto.getDistance();
                }
                else
                {
                    shopDto = new ShopDto();
                    DataConvertUtil.propertyConvertIncludeDefaultProp(searchContent, shopDto,
                            CommonConst.SEARCH_SHOPS_MAP);
                    initDistrictInfo(shopDto, needQueryDistrictMap);
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
                    shopDto.setColumnId(searchContent.getShopColumnPid());
                }
            }
            if (shopDto == null)
            {
                shopDto = (ShopDto) DataCacheApi.getObject(CommonConst.KEY_SHOP + groupResult.getShopId());
                if (shopDto == null)
                {
                    if (distance != null)
                    {
                        distanceMap.put(Long.parseLong(groupResult.getShopId()), distance * 1000);
                    }
                    needQueryShopIdList.add(Long.parseLong(groupResult.getShopId()));
                    needQueryLogoShopIdList.add(Long.parseLong(groupResult.getShopId()));
                    shopGoods.put(groupResult.getShopId(), goodsList);// 此时先将缓存中没有的商铺编号记录下来，在后面一起查询，避免和数据库的交互次数
                }
                else
                {
                    shopDto.setGoods(goodsList);
                    initDistrictInfo(shopDto, needQueryDistrictMap);
                    if (goodsList.size() == 0)
                    {
                        List<Map<String, Object>> cacheShopTopGoods = (List<Map<String, Object>>) DataCacheApi
                                .getObject(CommonConst.KEY_SHOP_TOP_GOODS + shopDto.getShopId());
                        if (cacheShopTopGoods != null)
                        {
                            shopDto.setGoods(cacheShopTopGoods);
                        }
                        else
                        {
                            needQueryShopIdForGoods.add(shopDto.getShopId());
                        }
                    }
                    shopDtos.add(shopDto);
                }
                if (shopDto != null)
                {
                    shopDto.setDistance(distance);
                    String shopLogoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopDto.getShopId());
                    if (!StringUtils.isEmpty(shopLogoUrl))
                    {
                        shopDto.setShopLogoUrl(shopLogoUrl);
                    }
                    else
                    {
                        needQueryLogoShopIdList.add(shopDto.getShopId());
                    }
                    if (shopDto.getDistance() != null)
                    {
                        shopDto.setDistance(1000 * shopDto.getDistance());
                    }
                    shopMap.put(shopDto.getShopId(), shopDto);
                    initDistrictInfo(shopDto, needQueryDistrictMap);
                }
            }
            else
            {
                if (goodsList.size() == 0)
                {
                    List<Map<String, Object>> cacheShopTopGoods = (List<Map<String, Object>>) DataCacheApi
                            .getObject(CommonConst.KEY_SHOP_TOP_GOODS + shopDto.getShopId());
                    if (cacheShopTopGoods != null)
                    {
                        shopDto.setGoods(cacheShopTopGoods);
                    }
                    else
                    {
                        shopDto.setGoods(goodsList);
                        needQueryShopIdForGoods.add(shopDto.getShopId());
                    }
                }
                else
                {
                    shopDto.setGoods(goodsList);
                }
                shopMap.put(shopDto.getShopId(), shopDto);
                String shopLogoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopDto.getShopId());
                if (!StringUtils.isEmpty(shopLogoUrl))
                {
                    shopDto.setShopLogoUrl(shopLogoUrl);
                }
                else
                {
                    needQueryLogoShopIdList.add(shopDto.getShopId());
                }
                if (shopDto.getDistance() != null)
                {
                    shopDto.setDistance(1000 * shopDto.getDistance());
                }
                initDistrictInfo(shopDto, needQueryDistrictMap);
                shopDtos.add(shopDto);
                shopMap.put(shopDto.getShopId(), shopDto);
            }
            shopIndexMap.put(groupResult.getShopId(), shopIndex++);

        }
        if (needQueryShopIdList.size() > 0)// 代表需要从数据库中查找这些商铺
        {
            List<ShopDto> persitShopDtos = shopServcie.getShopListByIds(needQueryShopIdList);//
            for (ShopDto shopItem : persitShopDtos)
            {
                shopItem.setDistance(distanceMap.get(shopItem.getShopId()));
                initDistrictInfo(shopItem, needQueryDistrictMap);
                String cacheKey = CommonConst.KEY_SHOP + shopItem.getShopId();
                DataCacheApi.setObjectEx(cacheKey, shopItem, 43200);// 存入redis
                List<Map<String, Object>> goodsDtoList = shopGoods.get(shopItem.getShopId() + "");
                shopItem.setGoods(goodsDtoList);
                shopDtos.add(shopItem);
                shopMap.put(shopItem.getShopId(), shopItem);
            }
        }
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsMap = null;
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsGroupMap = null;// 用来保存商铺销量最高的商品族
        if (needQueryShopIdForGoods.size() > 0)// 代表这些商铺列表没有在solr中查到相应的商品，需要从数据库中查找
        {
            List<GoodsGroupDto> goodsGroupDtoList = goodsGroupService.getListByShopIdListAndNum(
                    needQueryShopIdForGoods, 2);
            needCacheShopGoodsGroupMap = parseGoodsGroupForShop(goodsGroupDtoList, shopMap,
                    needQueryGoodsGroupPictureIdList, goodsGroupPicture, needQueryShopIdForGoods);
            if (needQueryShopIdForGoods.size() > 0)
            {
                List<GoodsDto> groupGoods = goodsService.getGoodsGroupByShopIdList(needQueryShopIdForGoods, 2);
                needCacheShopGoodsMap = parseGoodsForShopGroup(groupGoods, shopMap, needQueryGoodsPictureIdList,
                        goodsPicture);// 针对商品查找店铺销量最高的商品
            }
        }
        if (needQueryGoodsPictureIdList.size() > 0)// 一次性查找所有商品的缩略图
        {
            List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao.findByConditionIn(
                    attachmentRelationDto, needQueryGoodsPictureIdList);
            parseGoodsPicture(attachmentRelationDtos, goodsPicture);
        }
        if (needQueryGoodsGroupPictureIdList.size() > 0)// 一次性查找所有商品族的缩略图
        {
            attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS_GROUP);
            List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao.findByConditionIn(
                    attachmentRelationDto, needQueryGoodsGroupPictureIdList);
            parseGoodsPicture(attachmentRelationDtos, goodsGroupPicture);
        }
        if (needCacheShopGoodsMap != null)
        {
            writeShopTopGoodsToRedis(needCacheShopGoodsMap);// 将商铺销量最高的商品放入缓存
        }
        if (needCacheShopGoodsGroupMap != null)// 将销量最高的商品族放入缓存
        {
            writeShopTopGoodsGroupToRedis(needCacheShopGoodsGroupMap);
        }
        if (needQueryLogoShopIdList.size() > 0)
        {
            convertShopLogo(needQueryLogoShopIdList, shopMap);
        }
        if (needQueryLogoShopIdList.size() > 0)// 加载商铺的Logo
        {
            convertShopLogo(needQueryLogoShopIdList, shopMap);
        }
        List<Long> shopIdList = sortShop(shopDtos, shopIndexMap);
        if (needQueryDistrictMap.size() > 0)
        {
            Set<Long> districtIdSet = needQueryDistrictMap.keySet();
            List<DistrictDto> districtList = regionDao.getDistrictByIdList(districtIdSet);
            writeDistrictToRedis(districtList, needQueryDistrictMap);
        }
        //TODO 查询行业分类名称
        
        return DataConvertUtil.convertCollectionToListMap(shopDtos, CommonResultConst.GET_GROUP_SHOP);
    }

    private void writeDistrictToRedis(List<DistrictDto> districtList, Map<Long, List<ShopDto>> needQueryDistrictMap)
    {
        for (DistrictDto dto : districtList)
        {
            Long districtId = dto.getDistrictId();
            if (districtId != null)
            {
                List<ShopDto> shopDtos = needQueryDistrictMap.get(districtId);
                if (shopDtos != null)
                {
                    for (ShopDto item : shopDtos)
                    {
                        item.setDistrictName(dto.getDistrictName());
                    }
                }
            }
        }
    }

    /**
     * 初始化区县信息
     * @Title: initDistrictInfo
     * @param @param shopDto
     * @return void 返回类型
     * @throws
     */
    private void initDistrictInfo(ShopDto shopDto, Map<Long, List<ShopDto>> needQueryDistrictMap)
    {
        if (shopDto.getDistrictId() != null)// 区县id不能为空
        {
            DistrictDto districtDto = (DistrictDto) DataCacheApi.getObject(CommonConst.KEY_DISTRICT
                    + shopDto.getDistrictId());
            if (districtDto != null)
            {
                shopDto.setDistrictName(districtDto.getDistrictName());
            }
            else
            {
                List<ShopDto> shopDtos = needQueryDistrictMap.get((long) shopDto.getDistrictId());
                if (shopDtos != null)
                {
                    shopDtos.add(shopDto);
                }
                else
                {
                    shopDtos = new ArrayList<ShopDto>();
                    needQueryDistrictMap.put((long) shopDto.getDistrictId(), shopDtos);
                    shopDtos.add(shopDto);
                }
            }
        }
    }

    private List<Long> sortShop(List<ShopDto> shopDtos, Map<String, Integer> shopIndexMap)
    {
        List<Long> shopIdList = new ArrayList<Long>();
        for (ShopDto shopDto : shopDtos)
        {
            Long shopId = shopDto.getShopId();
            Integer shopIndex = shopIndexMap.get(shopId + "");// 代表这个商铺本应处的位置
            shopDto.setSortIndex(shopIndex);
            shopIdList.add(shopId);
        }
        Collections.sort(shopDtos);
        return shopIdList;
    }

    /**
     * 将销量最高的商品族放入缓存
     * @Title: writeShopTopGoodsGroupToRedis
     * @param @param needCacheShopGoodsGroupMap
     * @return void 返回类型
     * @throws
     */
    private void writeShopTopGoodsGroupToRedis(Map<Long, List<Map<String, Object>>> needCacheShopGoodsGroupMap)
    {
        for (Entry<Long, List<Map<String, Object>>> entryItem : needCacheShopGoodsGroupMap.entrySet())
        {
            DataCacheApi
                    .setObjectEx(CommonConst.KEY_SHOP_TOP_GOODS + entryItem.getKey(), entryItem.getValue(), 1296000);// 保存6个小时
        }
    }

    /**
     * 代表商铺销量最高的是商品族
     * @Title: parseGoodsGroupForShop
     * @param @param goodsGroupDtoList
     * @param @param shopMap
     * @param @param needQueryGoodsGroupPictureIdList
     * @param @param goodsGroupPicture
     * @return void 返回类型
     * @throws
     */
    private Map<Long, List<Map<String, Object>>> parseGoodsGroupForShop(List<GoodsGroupDto> goodsGroupDtoList,
            Map<Long, ShopDto> shopDtoMap, List<Long> needQueryGoodsGroupPictureIdList,
            Map<String, Map<String, Object>> goodsGroupPicture, List<Long> needQueryShopIdForGoods)
    {
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsMap = new HashMap<Long, List<Map<String, Object>>>();// 需要缓存的商铺销量最高的商品
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList)
        {
            ShopDto shopDto = shopDtoMap.get(goodsGroupDto.getShopId());
            if (shopDto != null)
            {
                needQueryShopIdForGoods.remove(goodsGroupDto.getShopId());
                Map<String, Object> goodsPropertyMap = DataConvertUtil.convertObjToMap(goodsGroupDto,
                        CommonResultConst.GET_GROUP_GOOD);
                String pictureUrl = DataCacheApi.get(CommonConst.KEY_GOODSGROUP_PICTURE
                        + goodsGroupDto.getGoodsGroupId());
                if (StringUtils.isEmpty(pictureUrl))// 代表缓存中没有此商品的缩略图
                {
                    goodsGroupPicture.put(goodsGroupDto.getGoodsGroupId() + "", goodsPropertyMap);
                    needQueryGoodsGroupPictureIdList.add(goodsGroupDto.getGoodsGroupId());
                }
                else
                {
                    goodsPropertyMap.put("goodsLogo", pictureUrl);
                }
                if (shopDto.getGoods() == null)
                {
                    shopDto.setGoods(new ArrayList<Map<String, Object>>());
                }
                shopDto.getGoods().add(goodsPropertyMap);
                List<Map<String, Object>> shopSaleTopGoods = needCacheShopGoodsMap.get(shopDto.getShopId());// 销量最高的商品
                if (shopSaleTopGoods == null)
                {
                    shopSaleTopGoods = new ArrayList<Map<String, Object>>();
                    shopSaleTopGoods.add(goodsPropertyMap);
                    needCacheShopGoodsMap.put(shopDto.getShopId(), shopSaleTopGoods);
                }
                else
                {
                    shopSaleTopGoods.add(goodsPropertyMap);
                }
            }
        }
        return needCacheShopGoodsMap;
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
    public void convertShopLogo(List<Long> needQueryShopIdList, Map<Long, ShopDto> shopInfoMap) throws Exception
    {
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_SHOP);
        attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
        if (needQueryShopIdList.size() > 0)
        {
            List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao.findByConditionIn(
                    attachmentRelationDto, needQueryShopIdList);
            for (AttachmentRelationDto dto : attachmentRelationDtos)
            {
                ShopDto shopProp = shopInfoMap.get((long) dto.getBizId());
                if (shopProp != null)
                {
                    try
                    {
                        String shopLogo = FdfsUtil.getFileProxyPath(dto.getFileUrl());
                        shopProp.setShopLogoUrl(shopLogo);
                        DataCacheApi.setex(CommonConst.KEY_SHOP_LOGO + dto.getBizId(), shopLogo, 3600);// 将商品缩略图放入缓存
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 解析获取商品的缩略图
     * @Title: parseGoodsPicture
     * @param @param attachmentRelationDtos
     * @param @param goodsPicture
     * @return void 返回类型
     * @throws
     */
    private void parseGoodsPicture(List<AttachmentRelationDto> attachmentRelationDtos,
            Map<String, Map<String, Object>> goodsPicture)
    {
        for (AttachmentRelationDto attachmentRelationDto : attachmentRelationDtos)
        {
            Map<String, Object> goodsProp = goodsPicture.get(attachmentRelationDto.getBizId() + "");
            if (goodsProp != null)
            {
                try
                {
                    String goodsLogo = FdfsUtil.getFileProxyPath(attachmentRelationDto.getFileUrl());
                    goodsProp.put("goodsLogo", goodsLogo);
                    DataCacheApi.setex(CommonConst.KEY_GOODSGROUP_PICTURE + attachmentRelationDto.getBizId(),
                            goodsLogo, 3600);// 将商品缩略图放入缓存
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    private void parseGoodsPictureMap(List<AttachmentRelationDto> attachmentRelationDtos,
            Map<String, GoodsDto> goodsPicture) throws Exception
    {
        for (AttachmentRelationDto attachmentRelationDto : attachmentRelationDtos)
        {
            GoodsDto goodsProp = goodsPicture.get(attachmentRelationDto.getBizId() + "");
            if (goodsProp != null)
            {
                String goodsLogo = FdfsUtil.getFileProxyPath(attachmentRelationDto.getFileUrl());
                goodsProp.setGoodsLogo(goodsLogo);
                DataCacheApi.setex(CommonConst.KEY_GOODS_PICTURE + attachmentRelationDto.getBizId(), goodsLogo, 3600);// 将商品缩略图放入缓存
            }
        }
    }

    /**
     * 从数据中查找出来的商铺销量最高的商品，和商铺进行绑定
     * @Title: parseGoodsForShopGroup
     * @param @param goodsDtos
     * @param @param shopDtoMap
     * @param @param needQueryGoodsPictureIdList 查找出来的商品如果没有缩略图还需要查询缩略图
     * @param @param goodsPicture 将没有缩略图的商品对象保存起来
     * @param @throws Exception
     * @return void 返回类型
     * @throws
     */
    private Map<Long, List<Map<String, Object>>> parseGoodsForShopGroup(List<GoodsDto> goodsDtos,
            Map<Long, ShopDto> shopDtoMap, List<Long> needQueryGoodsPictureIdList,
            Map<String, Map<String, Object>> goodsPicture) throws Exception
    {
        Map<Long, List<Map<String, Object>>> needCacheShopGoodsMap = new HashMap<Long, List<Map<String, Object>>>();// 需要缓存的商铺销量最高的商品
        for (GoodsDto goodsDto : goodsDtos)
        {
            if (goodsDto != null)
            {
                goodsDto.setMinPrice(new BigDecimal(goodsDto.getStandardPrice() + ""));
            }
            ShopDto shopDto = shopDtoMap.get(goodsDto.getShopId());
            if (shopDto != null)
            {
                Map<String, Object> goodsPropertyMap = DataConvertUtil.convertObjToMap(goodsDto,
                        CommonResultConst.GET_GROUP_GOOD);
                goodsPropertyMap.put("categoryId", goodsDto.getGoodsCategoryId());
                String pictureUrl = DataCacheApi.get(CommonConst.KEY_GOODS_PICTURE + goodsDto.getGoodsId());
                if (StringUtils.isEmpty(pictureUrl))// 代表缓存中没有此商品的缩略图
                {
                    goodsPicture.put(goodsDto.getGoodsId() + "", goodsPropertyMap);
                    needQueryGoodsPictureIdList.add(goodsDto.getGoodsId());
                }
                if (shopDto.getGoods() == null)
                {
                    shopDto.setGoods(new ArrayList<Map<String, Object>>());
                }
                shopDto.getGoods().add(goodsPropertyMap);
                List<Map<String, Object>> shopSaleTopGoods = needCacheShopGoodsMap.get(shopDto.getShopId());// 销量最高的商品
                if (shopSaleTopGoods == null)
                {
                    shopSaleTopGoods = new ArrayList<Map<String, Object>>();
                    shopSaleTopGoods.add(goodsPropertyMap);
                    needCacheShopGoodsMap.put(shopDto.getShopId(), shopSaleTopGoods);
                }
                else
                {
                    shopSaleTopGoods.add(goodsPropertyMap);
                }
            }
        }
        return needCacheShopGoodsMap;
    }
    /**
     * 不鉴权添加/修改商品接口
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/goods/updateGoods", "/service/goods/updateGoods","/token/goods/updateGoods","/session/goods/updateGoods"},
            method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object updateGoods(HttpEntity<String> entity, HttpServletRequest request) throws Exception{
        logger.info("不鉴权添加/修改商品接口-start");
        Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
        Map<String, Object> requestMap = getVaildRequestParamOfUpdateGoodsMethod(postParamMap);
        List<GroupPropertyModel>  groupPropertysModel = null;
        if(null != postParamMap.get("groupPropertys")){
        	List<Map<String, String>> groupPropertyModelList = JacksonUtil.jsonToObject(postParamMap.get("groupPropertys"),List.class,DateUtils.DATETIME_FORMAT);
        	groupPropertysModel  = buildGroupPropertysModel(groupPropertyModelList);
        }
        
        List<GoodsSetDto> goodsSetList = null;
        if (null != postParamMap.get("setGoodsList")) {
        	List<Map<String, String>> goodsSetMapList = JacksonUtil.jsonToObject(postParamMap.get("setGoodsList"), List.class, DateUtils.DATETIME_FORMAT);
        	goodsSetList = buildGoodsSetModel(goodsSetMapList);
        }
        goodsService.updateGoods(requestMap,(GoodsDto)requestMap.remove("goodsDto"),
                (GoodsGroupDto)requestMap.remove("goodsGroupDto"),groupPropertysModel,goodsSetList);
        
        
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "添加/修改商品成功！", requestMap);
    }
    
    
	@RequestMapping(value={"/token/goods/syncGoods",
			"/service/goods/syncGoods",
			"/session/goods/syncGoods"},
			method=RequestMethod.POST,
			consumes="application/json",
			produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object syncGoods(HttpServletRequest request) throws Exception{
		logger.info("PCG42：同步总店商品到分店接口-start");
		SyncGoodsInfoDto syncGoodsInfo = getRequestModel(request, SyncGoodsInfoDto.class);
		MqPusher.pushMessage("SyncGoodsByUpdate", syncGoodsInfo);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "正在同步商品到分店", null);
	}
	
    @RequestMapping(value ={"/service/goods/importGoodsByExcel",
            "/token/goods/importGoodsByExcel",
            "/session/goods/importGoodsByExcel" }, method = RequestMethod.POST, 
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object importGoodsByExcel(HttpServletRequest request, Long shopId,Integer templateType) throws Exception {
    	logger.info("PCG44：Excel批量导入商品-start");
        if (shopId == null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
        }
    
        int flag = this.shopServcie.queryShopExists(shopId);
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        
        if (templateType != 1 && templateType != 2) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"模板不可识别");
        }
        MultipartRequest mutipartRequest = (MultipartRequest) request;
        MultipartFile file = mutipartRequest.getFile(mutipartRequest.getFileNames().next());
        
        Map<String, Object> resultMap = goodsService.importGoodsByExcel(shopId, templateType,file);
        String resultMsg = resultMap.remove("returnMsg").toString();
        Integer returnCode = Integer.valueOf(resultMap.remove("returnCode").toString());
        return ResultUtil.getResult(returnCode, resultMsg, resultMap);
    }
    /**
     * 批量添加/修改商品接口
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/goods/batchUpdateGoods","/token/goods/batchUpdateGoods","/session/goods/batchUpdateGoods"},
            method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object batchUpdateGoods(HttpServletRequest request) throws Exception{
        logger.info("批量添加/修改商品接口-start");
    	BatchUpdateGoodsModel updateGoodsModel = getRequestModel(request, BatchUpdateGoodsModel.class);
    	List<Object> resultList = new ArrayList<Object>();
        for (Map<String, String> postParamMap : updateGoodsModel.getLst()) {
            Map<String, Object> requestMap = getVaildRequestParamOfUpdateGoodsMethod(postParamMap);
            List<GroupPropertyModel>  groupPropertysModel = null;
            if(null != postParamMap.get("groupPropertys")){
            	List<Map<String, String>> groupPropertyModelList = JacksonUtil.jsonToObject(postParamMap.get("groupPropertys"),List.class,DateUtils.DATETIME_FORMAT);
            	groupPropertysModel  = buildGroupPropertysModel(groupPropertyModelList);
            }
            goodsService.updateGoods(requestMap,(GoodsDto)requestMap.remove("goodsDto"),
                    (GoodsGroupDto)requestMap.remove("goodsGroupDto"),groupPropertysModel,null);
            resultList.add(requestMap);
        }
   
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "批量添加/修改商品接口成功！", resultList);
    }
    private Map<String, Object> getVaildRequestParamOfUpdateGoodsMethod(Map<String, String> postParamMap) throws Exception {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        GoodsDto goodsDto = new GoodsDto();
        GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
        
        String shopIdStr = postParamMap.get("shopId");
        String goodsIdStr = postParamMap.get("goodsId");
        String goodsName = postParamMap.get("goodsName");
        String goodsCategoryIds = postParamMap.get("goodsCategoryIds");
        String standardPriceStr = postParamMap.get("standardPrice");
        String unitIdStr = postParamMap.get("unitId");
        String unitName = postParamMap.get("unitName");
        String goodsServerModeStr = postParamMap.get("goodsServerMode");
        String goodsTypeStr = postParamMap.get("goodsType");
        String goodsNo = postParamMap.get("goodsNo");
        String barcode = postParamMap.get("barcode");
        
        String specsDesc = postParamMap.get("specsDesc");
        String sequenceStr = postParamMap.get("sequence");
        String goodsDesc = postParamMap.get("goodsDesc");
        String goodsDetailDesc = postParamMap.get("goodsDetailDesc");
        String goodsKey = postParamMap.get("goodsKey");
        String lowestUnitStr = postParamMap.get("lowestUnit");
        String isShowStr = postParamMap.get("isShow");
        String recommendFlagStr = postParamMap.get("recommendFlag");
        String hotFlagStr = postParamMap.get("hotFlag");
        String goodsSettleFlagStr = postParamMap.get("goodsSettleFlag");
        String extraChargeStr = postParamMap.get("extraCharge");
        String takeoutStr = postParamMap.get("takeout");
        String goodsPriceSpec = postParamMap.get("goodsPriceSpec");
        String attachementIds = postParamMap.get("attachementIds");
        String goodsLogoIdStr = postParamMap.get("goodsLogoId");
        String goodsRebateFlagStr = postParamMap.get("goodsRebateFlag");
        String storageTotalNumberStr = postParamMap.get("storageTotalNumber");
        String alarmNumberMaxStr = postParamMap.get("alarmNumberMax");
        String alarmNumberMinStr = postParamMap.get("alarmNumberMin");
        String isNeedCheckStr = postParamMap.get("isNeedCheck");
        String goodsStatusStr = postParamMap.get("goodsStatus");
        /*20160705请求参数新添加costPrice成本价字段*/
        String costPriceStr = postParamMap.get("costPrice");
        String goodsGroupIdStr = postParamMap.get("goodsGroupId");
        String minPriceStr = postParamMap.get("minPrice");
        String maxPriceStr = postParamMap.get("maxPrice");
        String plu = postParamMap.get("plu");
        String parentCategoryIdStr = postParamMap.get("parentCategoryId");
        String isOrderDiscountStr = postParamMap.get("isOrderDiscount");
        String sellModeStr = postParamMap.get("sellMode");
        sellModeStr = StringUtils.isBlank(sellModeStr) ? "0" : sellModeStr;
        String sellModeValueStr = postParamMap.get("sellModeValue");
        Integer sellMode = Integer.parseInt(sellModeStr.trim());

        if(StringUtils.isNotBlank(plu)){
        	goodsDto.setPluCode(plu);
        }
        if(StringUtils.isNotBlank(parentCategoryIdStr)){
        	Long parentCategoryId = NumberUtil.strToLong(parentCategoryIdStr, "parentCategoryId");
        	goodsGroupDto.setParentCategoryId(parentCategoryId);
        }
        if(StringUtils.isNotBlank(costPriceStr)){
        	Double costPrice = NumberUtil.strToDouble(costPriceStr, "costPrice");
        	goodsDto.setCostPrice(costPrice);
        }
        if(StringUtils.isNotBlank(goodsGroupIdStr)){
        	Long goodsGroupId = NumberUtil.strToLong(goodsGroupIdStr, "goodsGroupId");
        	int goodsGroupExist = goodsGroupDao.queryGoodsGroupExists(goodsGroupId);
        	if (goodsGroupExist > 0) {
        	 	goodsDto.setGoodsGroupId(goodsGroupId);
            	goodsGroupDto.setGoodsGroupId(goodsGroupId);
        	}
        }
        
        if(StringUtils.isNotBlank(isNeedCheckStr)){
        	Integer isNeedCheck = NumberUtil.strToNum(isNeedCheckStr, "isNeedCheck");
        	goodsDto.setIsNeedCheck(isNeedCheck);
        }
        
        if(StringUtils.isNotBlank(goodsStatusStr)){
        	Integer goodsStatus = NumberUtil.strToNum(goodsStatusStr, "goodsStatus");
        	goodsDto.setGoodsStatus(goodsStatus);
        }
        
        if (shopIdStr == null)
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
        
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
                CodeConst.CODE_PARAMETER_NOT_VALID,"shopId类型错误");
    
        int flag = this.shopServcie.queryShopExists(shopId);
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        requestMap.put("shopId", shopId);
        goodsDto.setShopId(shopId);
        goodsGroupDto.setShopId(shopId);
        if (StringUtils.isNotBlank(goodsIdStr)) {
            Long goodsId = CommonValidUtil.validStrLongFmt(goodsIdStr,
                    CodeConst.CODE_PARAMETER_NOT_VALID,
                    "goodsId数据格式错误");
            GoodsDto goods = goodsService.getGoodsById(goodsId);
            if (goods == null) {
            	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"商品不存在");
            }
            /*           Long goodsGroupId = goods.getGoodsGroupId();
            if (goodsGroupId != null) {
            	 Map<String,Object> parms = new HashMap<String, Object>();
                 parms.put("goodsGroupId", goodsGroupId);
                 List<GoodsDto> goodsList = goodsService.getGoodsByGroupMap(parms);
                 if (goodsList.size() > 1)
                     throw new ValidateException(CodeConst.CODE_IS_NOT_SIGLE_GOODS_GROUP,"该商品所属商品族有多个商品");
			}*/
           
            requestMap.put("goodsId", goodsId);
            //requestMap.put("goodsGroupId", goodsGroupId);
            goodsDto.setGoodsId(goodsId);
            //goodsDto.setGoodsGroupId(goodsGroupId);
            //goodsGroupDto.setGoodsGroupId(goodsGroupId);
        }
        
        if (StringUtils.isBlank(goodsName)) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"goodsName不能为空");
        }
        SensitiveWordsUtil.checkSensitiveWords("goodsName", goodsName);
        requestMap.put("goodsName", goodsName);
        goodsDto.setGoodsName(goodsName);
        goodsGroupDto.setGoodsName(goodsName);
  
        String pinyinCode = PinyinUtil.getPinYinHeadChar(goodsName);
        requestMap.put("pinyinCode", pinyinCode);
        goodsDto.setPinyinCode(pinyinCode);
        goodsGroupDto.setPinyincode(pinyinCode);
        
        CommonValidUtil.validStrNull(goodsCategoryIds,
                CodeConst.CODE_PARAMETER_NOT_NULL,
                "goodsCategoryIds不能为空");
        
        Long tempCategoryId = CommonValidUtil.validStrLongFmt(goodsCategoryIds.split(",")[0], 
                CodeConst.CODE_PARAMETER_NOT_VALID, "goodsCategoryIds类型错误");
        
        requestMap.put("goodsCategoryIds", tempCategoryId);
        goodsDto.setGoodsCategoryId(tempCategoryId);
        
        List<Long> goodsCategoryIdList =  buildGoodsCategoryIdList(goodsCategoryIds);
        goodsGroupDto.setCategoryIdList(goodsCategoryIdList);
        
        CommonValidUtil.validStrNull(standardPriceStr,
                CodeConst.CODE_PARAMETER_NOT_NULL,
                "standardPrice不能为空");
        
        Double standardPrice = CommonValidUtil.validStrDoubleFmt(standardPriceStr, 
                CodeConst.CODE_PARAMETER_NOT_VALID, "standardPrice类型错误");
        
        requestMap.put("standardPrice", standardPrice);
        if (goodsIdStr == null) {
            goodsGroupDto.setMinPrice(BigDecimal.valueOf(standardPrice));
            goodsGroupDto.setMaxPrice(BigDecimal.valueOf(standardPrice));
        }
        goodsDto.setStandardPrice(standardPrice);
        CommonValidUtil.validStrNull(unitName,
                CodeConst.CODE_PARAMETER_NOT_NULL,
                "unitName不能为空");
        
        SensitiveWordsUtil.checkSensitiveWords("unitName", unitName);
        requestMap.put("unitName", unitName);
        goodsDto.setUnitName(unitName);
        goodsGroupDto.setUnit(unitName);
        
        Long unitId = new Long(0);
        
        if (unitIdStr == null) {
        	unitId = collectService.getGoodsUnitByUnitName(unitName,shopId);
        }else {
        	unitId = CommonValidUtil.validStrLongFmt(unitIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "unitId类型错误");
        }
        
        if(unitId==0){
        	 unitId = collectService.saveUnit(unitName, shopId, 0);
        }
        goodsGroupDto.setUnitId(unitId);
        goodsDto.setUnitId(unitId);
        if (StringUtils.isNotBlank(goodsServerModeStr)) {
            Integer goodsServerMode = CommonValidUtil.validStrIntFmt(goodsServerModeStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsServerMode类型错误");
            requestMap.put("goodsServerMode", goodsServerMode);
            goodsDto.setGoodsServerMode(goodsServerMode);
            goodsGroupDto.setGoodsServerMode(goodsServerMode);
        }
        
        if (StringUtils.isNotBlank(goodsTypeStr)) {
            Integer goodsType = CommonValidUtil.validStrIntFmt(goodsTypeStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsType类型错误");
            if (goodsType.equals(3000)) {
            	if (postParamMap.get("setGoodsList") == null) {
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品不能为空");
            	}
            }else{
            	if (postParamMap.get("setGoodsList") != null) {
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"商品类型应为套餐");
            	}
            }
            requestMap.put("goodsType", goodsType);
            goodsDto.setGoodsType(goodsType);
        }else {
        	if (postParamMap.get("setGoodsList") != null) {
        		requestMap.put("goodsType", 3000);//套餐
        		goodsDto.setGoodsType(3000);
        	}
        }
        
        if (StringUtils.isNotBlank(goodsNo)) {
            requestMap.put("goodsNo", goodsNo);
            goodsDto.setGoodsNo(goodsNo);
        }
        
        if (StringUtils.isNotBlank(barcode)) {
            if(barcode.length() > 30)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_TOOLENGTH,"barcode超过长度");
            }
            requestMap.put("barcode", barcode);
            goodsDto.setBarcode(barcode);
        }
        
        if (StringUtils.isNotBlank(specsDesc)) {
            requestMap.put("specsDesc", specsDesc);
            goodsDto.setSpecsDesc(specsDesc);
        }
        
        if (StringUtils.isNotBlank(sequenceStr)) {
            Integer sequence = CommonValidUtil.validStrIntFmt(sequenceStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "sequence类型错误");
            
            requestMap.put("sequence", sequence);
            goodsDto.setSequence(sequence);
            goodsGroupDto.setSequence(sequence);
        }
        
        if (StringUtils.isNotBlank(goodsDesc)) {
        	SensitiveWordsUtil.checkSensitiveWords("goodsDesc", goodsDesc);
            requestMap.put("goodsDesc", goodsDesc);
            goodsDto.setGoodsDesc(goodsDesc);
            goodsGroupDto.setGoodsDesc(goodsDesc);
        }
        
        if (StringUtils.isNotBlank(goodsDetailDesc)) {
        	SensitiveWordsUtil.checkSensitiveWords("goodsDetailDesc", goodsDetailDesc);
            requestMap.put("goodsDetailDesc", goodsDetailDesc);
            goodsDto.setGoodsDetailDesc(goodsDetailDesc);
            goodsGroupDto.setGoodsDetailDesc(goodsDetailDesc);
        }
        
        if (StringUtils.isNotBlank(goodsKey)) {
            requestMap.put("goodsKey", goodsKey);
            goodsDto.setGoodsKey(goodsKey);
            goodsGroupDto.setGoodsGroupKey(goodsKey);
        }
        
        if (StringUtils.isNotBlank(lowestUnitStr)) {
            Integer lowestUnit = CommonValidUtil.validStrIntFmt(lowestUnitStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "lowestUnit类型错误");
            
            requestMap.put("lowestUnit", lowestUnit);
            goodsDto.setLowestUnit(lowestUnit.longValue());
        }
        
        if (StringUtils.isNotBlank(isShowStr)) {
            Integer isShow = CommonValidUtil.validStrIntFmt(isShowStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "isShow类型错误");
            
            requestMap.put("isShow", isShow);
            goodsDto.setIsShow(isShow);
        }
        
        if (StringUtils.isNotBlank(recommendFlagStr)) {
            Integer recommendFlag = CommonValidUtil.validStrIntFmt(recommendFlagStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "recommendFlag类型错误");
            
            requestMap.put("recommendFlag", recommendFlag);
            goodsDto.setRecommendFlag(recommendFlag);
            goodsGroupDto.setRecommendFlag(recommendFlag);
        }
        
        if (StringUtils.isNotBlank(hotFlagStr)) {
            Integer hotFlag = CommonValidUtil.validStrIntFmt(hotFlagStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "hotFlag类型错误");
            
            requestMap.put("hotFlag", hotFlag);
            goodsDto.setHotFlag(hotFlag);
            goodsGroupDto.setHotFlag(hotFlag);
        }
        
        if (StringUtils.isNotBlank(goodsSettleFlagStr)) {
            Integer goodsSettleFlag = CommonValidUtil.validStrIntFmt(goodsSettleFlagStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsSettleFlag类型错误");
            
            requestMap.put("goodsSettleFlag", goodsSettleFlag);
            goodsDto.setGoodsSettleFlag(goodsSettleFlag);
        }
        if (StringUtils.isNotBlank(goodsRebateFlagStr)) {
            Integer goodsRebateFlag = CommonValidUtil.validStrIntFmt(goodsRebateFlagStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsRebateFlag类型错误");
            
            requestMap.put("goodsRebateFlag", goodsRebateFlag);
            goodsDto.setGoodsRebateFlag(goodsRebateFlag);
        }
        if (StringUtils.isNotBlank(extraChargeStr)) {
            Double extraCharge = CommonValidUtil.validStrDoubleFmt(extraChargeStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "extraCharge类型错误");
            requestMap.put("extraCharge", extraCharge);
            goodsDto.setExtraCharge(extraCharge);
        }
        
        if (StringUtils.isNotBlank(takeoutStr)) {
            Integer takeout = CommonValidUtil.validStrIntFmt(takeoutStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "takeout类型错误");
            
            requestMap.put("takeout", takeout);
            goodsDto.setTakeout(takeout);
        }
        requestMap.put("goodsPriceSpec", goodsPriceSpec);
        goodsDto.setGoodsPriceSpec(goodsPriceSpec);
        if(StringUtils.isNotBlank(isOrderDiscountStr)) {
            goodsDto.setIsOrderDiscount(NumberUtil.strToInteger(isOrderDiscountStr, "isOrderDiscount"));
        }
    
        if (StringUtils.isNotBlank(attachementIds)) {
            String[] attachementIdCol = attachementIds.split(",");
            requestMap.put("attachementIds", attachementIdCol);
            int index = 0;
            int len = attachementIdCol.length;
            if (index < len) {
                goodsDto.setGoodsLogo1(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
            if (index < len) {
                goodsDto.setGoodsLogo2(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
            if (index < len) {
                goodsDto.setGoodsLogo3(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
            if (index < len) {
                goodsDto.setGoodsLogo4(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
            if (index < len) {
                goodsDto.setGoodsLogo5(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
            if (index < len) {
                goodsDto.setGoodsLogo6(CommonValidUtil.validStrLongFmt(attachementIdCol[index++], 
                        CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId"+index+"数据类型错误"));
            }
        }
        
        if (StringUtils.isNotBlank(goodsLogoIdStr)) {
            Long goodsLogoId = CommonValidUtil.validStrLongFmt(goodsLogoIdStr, 
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsLogoId类型错误");
            
            requestMap.put("goodsLogoId", goodsLogoId);
            goodsGroupDto.setGoodsLogoId(goodsLogoId);
            goodsDto.setGoodsLogo(goodsLogoId.toString());
        }
        
        if (StringUtils.isNotBlank(storageTotalNumberStr)) {
        	goodsDto.setStorageTotalNumber(CommonValidUtil.validStrDoubleFmt(storageTotalNumberStr, 
        									CodeConst.CODE_PARAMETER_NOT_VALID, "storageTotalNumber类型错误"));
        }
        
        if (StringUtils.isNotBlank(alarmNumberMaxStr)) {
        	goodsDto.setAlarmNumberMax(CommonValidUtil.validStrDoubleFmt(alarmNumberMaxStr, 
                    					CodeConst.CODE_PARAMETER_NOT_VALID, "alarmNumberMax类型错误"));
        }
        
        if (StringUtils.isNotBlank(alarmNumberMinStr)) {
        	goodsDto.setAlarmNumberMin(CommonValidUtil.validStrDoubleFmt(alarmNumberMinStr, 
                    					CodeConst.CODE_PARAMETER_NOT_VALID, "alarmNumberMin类型错误"));
        
        }
        goodsDto.setSellMode(sellMode);
        if(0 != sellMode.intValue())    //如果为开桌配置相关，则进行一些默认设置
        {
            goodsDto.setIsShow(0);
            sellModeValueStr = StringUtils.isBlank(sellModeValueStr) ? "1" : sellModeValueStr;
            goodsDto.setSellModeValue(Integer.valueOf(sellModeValueStr.trim()));
        }
        if (StringUtils.isNotBlank(maxPriceStr)) {
        	goodsGroupDto.setMaxPrice(BigDecimal.valueOf(CommonValidUtil.validStrDoubleFmt(maxPriceStr, 
                    					CodeConst.CODE_PARAMETER_NOT_VALID, "maxPrice类型错误")));
        }
        if (StringUtils.isNotBlank(minPriceStr)) {
        	goodsGroupDto.setMinPrice(BigDecimal.valueOf(CommonValidUtil.validStrDoubleFmt(minPriceStr, 
                    					CodeConst.CODE_PARAMETER_NOT_VALID, "minPrice类型错误")));
        }
        if(postParamMap.get("groupPropertys")!=null){
        	 List<Map<String, String>> groupPropertyMapList = JacksonUtil.jsonToObject(postParamMap.get("groupPropertys"),List.class,DateUtils.DATETIME_FORMAT);
             List<GroupPropertyModel> groupPropertyList  = buildGroupPropertysModel(groupPropertyMapList);
             List<String> groupPropertyNames = buildGroupPropertyNames(groupPropertyList);
             goodsGroupDto.setGroupPropertyName(groupPropertyNames);
        }
       
        //非法关键字屏蔽
        SensitiveWordsUtil.checkSensitiveWords("goodsDetailDesc", postParamMap.get("goodsDetailDesc"));
        SensitiveWordsUtil.checkSensitiveWords("goodsDesc", postParamMap.get("goodsDesc"));
        SensitiveWordsUtil.checkSensitiveWords("unitName", postParamMap.get("unitName"));
        SensitiveWordsUtil.checkSensitiveWords("goodsName", postParamMap.get("goodsName"));
        requestMap.put("goodsDto", goodsDto);
        requestMap.put("goodsGroupDto", goodsGroupDto);
        return requestMap;
    }

	private List<String> buildGroupPropertyNames(List<GroupPropertyModel> groupPropertysModel) throws Exception {
		
		List<String> groupPropertyNames = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(groupPropertysModel)) {
			for (GroupPropertyModel groupProperty : groupPropertysModel) {
				 CommonValidUtil.validStrNull(groupProperty.getGroupPropertyName(),CodeConst.CODE_PARAMETER_NOT_NULL,
                "groupPropertyName不能为空");
				 CommonValidUtil.validStrNull(groupProperty.getProValuesName(),CodeConst.CODE_PARAMETER_NOT_NULL,
			                "proValuesName不能为空");
				groupPropertyNames.add(groupProperty.getProValuesName());
			}
		}
		return groupPropertyNames;
	}
	private List<Long> buildGoodsCategoryIdList(String goodsCategoryIds) throws Exception {
		List<Long> categoryIdList = new ArrayList<Long>();

		String[] categoryIdArr = goodsCategoryIds.split(",");
		for (int i = 0; i < categoryIdArr.length; i++) {
			Long categoryId = Long.valueOf(categoryIdArr[i]);
			categoryIdList.add(categoryId);
			
		}
		return categoryIdList;
	}	

	private List<GroupPropertyModel> buildGroupPropertysModel(List<Map<String, String>> groupPropertysModel) throws Exception {
		
		List<GroupPropertyModel> groupPropertyModelList = new ArrayList<GroupPropertyModel>();
		if (CollectionUtils.isNotEmpty(groupPropertysModel)) {
			for (Map<String, String> jsonMap : groupPropertysModel) {
				String jsonStr = JacksonUtil.map2Json(jsonMap);
				GroupPropertyModel groupPropertyModel = JacksonUtil.jsonToObject(jsonStr, GroupPropertyModel.class, DateUtils.DATETIME_FORMAT);
				if (groupPropertyModel.getStandardPrice() == null) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "多规格商品价格不能为空");
				}
				groupPropertyModelList.add(groupPropertyModel);
			}
		}
		return groupPropertyModelList;
	}
	
	private List<GoodsSetDto> buildGoodsSetModel(List<Map<String, String>> goodsSetMapList) throws Exception {
		
		List<GoodsSetDto> goodsSetModelList = new ArrayList<GoodsSetDto>();
		if (CollectionUtils.isNotEmpty(goodsSetMapList)) {
			for (Map<String, String> jsonMap : goodsSetMapList) {
				String jsonStr = JacksonUtil.map2Json(jsonMap);
				GoodsSetDto goodsSetModel = JacksonUtil.jsonToObject(jsonStr, GoodsSetDto.class, DateUtils.DATETIME_FORMAT);
		   		if (goodsSetModel.getGoodsId() == null) {
	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品id不能为空");
	    		}
	    		
	    		if (goodsSetModel.getGoodsNumber() == null) {
	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品数量不能为空");
	    		}
	    		
	    		if (goodsSetModel.getGoodsIndex() == null) {
	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品排序不能为空");
	    		}
	    		
	    		if (goodsSetModel.getPrice() == null) {
	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品原价不能为空");
	    		}
	    	
	    		int isExist = goodsDao.queryGoodsExists(goodsSetModel.getGoodsId());
	    		if (isExist == 0) {
	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品不存在  goodsId:"+goodsSetModel.getGoodsId());
	    		}
				goodsSetModelList.add(goodsSetModel);
			}
		}
		return goodsSetModelList;
	}	
    /**
     * 保存商铺销量最高的两个商品2个小时
     * @Title: writeShopTopGoodsToRedis
     * @param @param needCacheShopGoodsMap
     * @return void 返回类型
     * @throws
     */
    private void writeShopTopGoodsToRedis(Map<Long, List<Map<String, Object>>> needCacheShopGoodsMap)
    {
        for (Entry<Long, List<Map<String, Object>>> entryItem : needCacheShopGoodsMap.entrySet())
        {
            DataCacheApi.setObjectEx(CommonConst.KEY_SHOP_TOP_GOODS + entryItem.getKey(), entryItem.getValue(), 1296000);// 保存6个小时
        }
    }
    
    /**
     * MS32：新增/修改商品单位
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/goods/updateGoodsUnit","/token/goods/updateGoodsUnit","/session/goods/updateGoodsUnit"},
            method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object updateGoodsUnit(HttpEntity<String> entity, HttpServletRequest request) {
        logger.info("新增/修改商品单位接口-start");
        try {
        	GoodsUnitDto goodsUnitDto = (GoodsUnitDto)JacksonUtil.postJsonToObj(entity,GoodsUnitDto.class);
        	if(goodsUnitDto!=null){
        		CommonValidUtil.validStrNull(goodsUnitDto.getUnitName(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODSUNITNAME);
        	}else{
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSUNIT);
        	}
        	SensitiveWordsUtil.checkSensitiveWords("unitName", goodsUnitDto.getUnitName());
            goodsService.updateGoodsUnit(goodsUnitDto);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增/修改商品单位成功！",null);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("新增/修改商品单位接口-系统异常",e);
            throw new APISystemException("新增/修改商品单位接口-系统异常", e);
        }
    }
    
    
    /**
     * MS35：查询是否存在同名的公共单位 
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/goods/isExistSameName","/token/goods/isExistSameName","/session/goods/isExistSameName"},
           produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object isExistSameName(HttpServletRequest request) {
    	logger.info("查询是否存在同名的公共单位 接口-start");
    	String shopId = RequestUtils.getQueryParam(request, "shopId");
    	String unitName = RequestUtils.getQueryParam(request, "unitName");
    	try {
    		CommonValidUtil.validStrNull(unitName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODSUNITNAME);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("unitName", unitName);
    		map.put("shopId", shopId);
    		Map<String, Object> resultMap =goodsService.isExistSameName(map);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询是否存在同名的公共单位 成功！",resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询接口-系统异常",e);
            throw new APISystemException("查询是否存在同名的公共单位 接口-系统异常", e);
        }
    }
    
    
    /**
     * MS33：删除商品单位
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/goods/delGoodsUnit","/token/goods/delGoodsUnit","/session/goods/delGoodsUnit"},
            produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object delGoodsUnit(HttpServletRequest request) {
        logger.info("删除商品单位接口-start");
        try {
        	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        	String unitIdStr = RequestUtils.getQueryParam(request, "unitId");
        	Integer unitId = CommonValidUtil.validStrIntFmt(unitIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "单位名称不能为空");
        	Long shopId=null;
        	if(shopIdStr !=null && !shopIdStr.equals("null")){
        		shopId=CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "操作类型格式错误");
        	}
        	//不能删除公共的单位
        	int n = goodsService.getGoodsUnit(unitId);
        	if(n>0){
        		throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_GOODSUNITNAME_COMMON);
        	}
        	//已被使用的单位不能被删除
        	int goodsTotal=goodsService.getGoodsByUnitId(unitId);
        	if(goodsTotal>0){
        		throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_GOODSUNIT_USED);
        	}
        	Map<String, Object> result = new HashMap<String, Object>();
        	result.put("unitId", unitId);
        	result.put("shopId", shopId);
            goodsService.delGoodsUnit(result);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除商品单位成功！",null);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("删除商品单位接口-系统异常",e);
            throw new APISystemException("删除商品单位接口-系统异常", e);
        }
    }
    
    /**
     * MS34：查询商品单位
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/goods/getGoodsUnitList","/token/goods/getGoodsUnitList","/session/goods/getGoodsUnitList"},
            produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getGoodsUnitList(HttpServletRequest request) {
        logger.info("查询商品单位接口-start");
        try {
        	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        	String unitName = RequestUtils.getQueryParam(request, "unitName");
        	String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        	String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
        	
        	 Integer pageNO = PageModel.handPageNo(pNoStr);
             Integer pageSize = PageModel.handPageSize(pSizeStr);
        	
        	Long shopId=null;
        	if(!shopIdStr.isEmpty()){
        		shopId=CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "操作类型格式错误");
        	}
        	Map<String, Object> result = new HashMap<String, Object>();
        	result.put("unitName", unitName);
        	result.put("shopId", shopId);
        	PageModel pageModel = goodsService.getGoodsUnitList(result,pageNO,pageSize);
        	
    	   Map<String, Object> map = new HashMap<String, Object>();
           map.put("pNo", pageModel.getToPage());
           map.put("rCount", pageModel.getTotalItem());
           map.put("lst", pageModel.getList());
        	
        	return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询商品单位成功！",map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询商品单位接口-系统异常",e);
            throw new APISystemException("查询商品单位接口-系统异常", e);
        }
    }
    
    /**
     * MS36：经营分析商品销售统计接口 
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="goods/getGoodsSalestatistics",produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getGoodsSalestatistics(HttpServletRequest request) {
        logger.info("MS36：经营分析商品销售统计接口-start "+request.getPathInfo());
        try {
        	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        	String startTime = RequestUtils.getQueryParam(request, "startTime");
        	String endTime = RequestUtils.getQueryParam(request, "endTime");
        	String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
        	String goodsCategoryIdStr  = RequestUtils.getQueryParam(request, "goodsCategoryId");
        	String goodsName  = RequestUtils.getQueryParam(request, "goodsName");
        	String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        	String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
        	//=====================2016年7月8日 新增字段================================
        	String goodsCategoryIdsStr = RequestUtils.getQueryParam(request, "goodsCategoryIds");
        	String orderByStr = RequestUtils.getQueryParam(request, "orderBy");
        	String orderByModeStr = RequestUtils.getQueryParam(request, "orderByMode");
        	String goodsGroupIdStr  = RequestUtils.getQueryParam(request, "goodsGroupId");
        	
        	String statisticsMode  = RequestUtils.getQueryParam(request, "statisticsMode");
        	String userId  = RequestUtils.getQueryParam(request, "userId");
        	
        	Integer pageNO = PageModel.handPageNo(pNoStr);
            Integer pageSize = PageModel.handPageSize(pSizeStr);
        	
        	Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_SHOPID);
        	Integer goodsCategoryId = null;
        	List<String> goodsCategoryIds = null;
        	Integer orderBy = null;
        	Integer orderByMode = null;
        	if(null != goodsGroupIdStr){
        		CommonValidUtil.validStrLongFmt(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_SHOPID);
        	}
        	if(goodsCategoryIdStr != null){
        		goodsCategoryId = CommonValidUtil.validStrIntFmt(goodsCategoryIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_GOODS_CATEGORY_ID);
        		if(goodsCategoryIdsStr !=null ){
        			throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_CATEGORYIDS_CATEGORYID_NOT_ALL_EXIST);
        		}
        	}else{
        		if(goodsCategoryIdsStr !=null ){
        			goodsCategoryIds = Arrays.asList(goodsCategoryIdsStr.split(","));
        		}
        	}
        	if(orderByStr != null){
        		CommonValidUtil.validStrIntFmt(orderByStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ORDERBYSTR);
        		orderBy = Integer.valueOf(orderByStr);
        	}
        	if(orderByModeStr != null){
        		CommonValidUtil.validStrIntFmt(orderByModeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ORDERBYMODESTR);
        		orderByMode = Integer.valueOf(orderByModeStr);
        	}
        	Integer cashierId = null;
        	if(cashierIdStr != null){
        		cashierId = CommonValidUtil.validStrIntFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CASHIER_ID);
        	}
        	
        	Map<String, Object> param = new HashMap<String, Object>();
        	param.put("shopId", shopId);
        	param.put("startTime", startTime);
        	param.put("endTime", endTime);
        	param.put("cashierId", cashierId);
        	param.put("goodsCategoryId", goodsCategoryId);
        	param.put("goodsName", goodsName);
        	param.put("goodsCategoryIds", goodsCategoryIds);
        	param.put("orderBy", orderBy);
        	param.put("orderByMode", orderByMode);
        	param.put("n", (pageNO - 1) * pageSize);
        	param.put("m", pageSize);
        	param.put("orderStatus", OrderStatusEnum.SETTLE.getValue());
        	param.put("goodsGroupId", goodsGroupIdStr);
        	
        	param.put("statisticsMode", statisticsMode);
        	param.put("userId", userId);
        	PageModel pageModel = goodsService.getGoodsSalestatistics(param);
        	//获取销售统计金额总额
        	Double saleMoneySum = goodsService.getGoodsSaleSalestatistics(param);
    	    Map<String, Object> map = new HashMap<String, Object>();
            map.put("pNo", pageNO);
            map.put("rCount", pageModel.getTotalItem());
            map.put("lst", pageModel.getList());
            map.put("saleMoneySum",saleMoneySum);
        	
        	return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "经营分析商品销售统计成功！",map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("MS36：经营分析商品销售统计接口-系统异常",e);
            throw new APISystemException("MS36：经营分析商品销售统计接口-系统异常", e);
        }
    }
    @RequestMapping(value =
        { "/goods/getUnits","/token/goods/getUnits",
                "/session/goods/getUnits","/service/goods/getUnits" }, method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUnits(HttpServletRequest request, Integer shopId) throws Exception{
        if(null == shopId){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        }
        logger.debug("查询店铺商品单位：" + shopId);
        List<Unit> rs = collectService.queryUnitsForShop(shopId);
        MessageListDto msgList = new MessageListDto();
        int size = rs.size();
        msgList.setpNo(size);
        msgList.setpSize(size);
        msgList.setrCount(size);
        msgList.setLst(rs);
        logger.debug("获取单位成功：" + shopId);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获单位成功", msgList, DateUtils.DATETIME_FORMAT);
    }

    /**
     * PCG46：菜品沽清操作接口
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
        { "/goods/operateSoldOut","/token/goods/operateSoldOut",
                "/session/goods/operateSoldOut","/service/goods/operateSoldOut" }, method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String operateSoldOut(HttpServletRequest request, HttpEntity<String> entity) throws Exception{
        Map<String, String> params = JacksonUtil.postJsonToMap(entity);
        String shopIdStr = params.get("shopId");
        String goodsListStr = params.get("goodsList");
        //校验相关参数是否必传
        if(StringUtils.isBlank(shopIdStr)){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        }

        if(StringUtils.isBlank(goodsListStr)){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "goodsList不能为空");
        }
        //参数解析
        Long shopId = null;
        try{
            shopId = Long.parseLong(shopIdStr);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId格式不正确");
        }
        ObjectMapper mapper = new ObjectMapper();
        JavaType temp = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
        JavaType finalType = mapper.getTypeFactory().constructParametricType(List.class, temp);
        List<Map<String, String>> goodsList = mapper.readValue(goodsListStr.trim(), finalType);
        boolean rs = goodsService.processGoodsSoldOut(shopId, goodsList);
        logger.debug("菜品沽清操作接口：" + shopIdStr);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "操作成功", null, DateUtils.DATETIME_FORMAT);
    }

    /**
     * PCG47：获取所有沽清菜品接口
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
        { "/goods/getSoldOut","/token/goods/getSoldOut",
                "/session/goods/getSoldOut","/service/goods/getSoldOut" }, method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getSoldOut(HttpServletRequest request) throws Exception{
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        //校验相关参数是否必传
        if(StringUtils.isBlank(shopIdStr)){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        }
        //参数解析
        Long shopId = null;
        try{
            shopId = Long.parseLong(shopIdStr);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId格式不正确");
        }
        List<GoodsSoldOutDto> rs = goodsService.getSoldOuts(shopId);
        logger.debug("菜品沽清操作接口：" + shopIdStr);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "操作成功", rs, DateUtils.DATETIME_FORMAT);
    }
    
	/**
	 * PCG50：查询商品增值服务接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/goods/getGoodsAvs",
							"/service/goods/getGoodsAvs",
							"/session/goods/getGoodsAvs"},
					produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getGoodsAvs(HttpServletRequest request) throws Exception{
		logger.info("PCG50：查询商品增值服务接口-start");
		GoodsAvsDto goodsAvs = getRequestModel(request, GoodsAvsDto.class);
		goodsAvs.setPageNo((goodsAvs.getPageNo()-1)*goodsAvs.getPageSize()<0 ? 0 : (goodsAvs.getPageNo()-1)*goodsAvs.getPageSize());
		
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("lst", goodsService.getGoodsAvsList(goodsAvs));
        resultMap.put("rCount", goodsService.getGoodsAvsCount(goodsAvs));
        resultMap.put("pNo", goodsAvs.getPageNo());
        
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获商品增值服务成功",resultMap,
										DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCG48：新增/修改增值服务接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/goods/updateGoodsAvs",
			"/service/goods/updateGoodsAvs",
			"/session/goods/updateGoodsAvs" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateShopCoupon(HttpServletRequest request) throws Exception {
		
		UpdateGoodsAvs updateGoodsAvs = getRequestModel(request, UpdateGoodsAvs.class);
		List<Map<String, Object>> resultMap = goodsService.updateGoodsAvs(updateGoodsAvs);
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！",resultMap);
		
	}

	/**
	 * PCG48：新增/修改增值服务接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/goods/deleteGoodsAvs",
			"/service/goods/deleteGoodsAvs",
			"/session/goods/deleteGoodsAvs" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteGoodsAvs(HttpServletRequest request) throws Exception {
		
        String goodsVasId = RequestUtils.getQueryParam(request, "goodsVasId");
        CommonValidUtil.validObjectNull(goodsVasId, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsVasId不能为空");
        goodsService.deleteGoodsAvs(NumberUtil.stringToLong(goodsVasId));
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！",goodsVasId);
		
	}
}
