package com.idcq.appserver.controller.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.NumberFormat;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.ShopGoodsDto;
import com.idcq.appserver.dto.goods.ShopTechRefGoodsDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.goods.IGoodsGroupCategoryRelationService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.goods.IShopGoodsService;
import com.idcq.appserver.service.goods.IShopTechRefGoodsService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;

@Controller
@RequestMapping(value = "/goods")
public class ShopGoodsController
{
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private IShopGoodsService shopGoodsService;

    @Autowired
    private IShopServcie shopService;

    @Autowired
    private IGoodsServcie goodsService;

    @Autowired
    private IGoodsSetDao goodsSetDao;
    /**
     * 商品类别
     */
    @Autowired
    private IGoodsCategoryDao goodCategoryDao;

    @Autowired
    private IAttachmentRelationDao attachmentRelationDao;

    @Autowired
    private IGoodsGroupCategoryRelationService goodsGroupCategoryService;

    @Autowired
    private IShopTechnicianService shopTechnicianService;

    @Autowired
    private IShopTechRefGoodsService shopTechRefGoodsService;

    /**
     * 商品族
     */
    @Autowired
    private IGoodsGroupService goodsGroupService;

    /**
     * 
     * @Title: getShopGoods
     * @Description: TODO(获取商铺商品数据)
     * @param @param request
     * @param @return
     * @return ResultDto 返回类型
     * @throws
     */
    @RequestMapping(value = "/getShopGoods")
    @ResponseBody
    public ResultDto getShopGoods(HttpServletRequest request)
    {
        try
        {
        	logger.info("获取商铺中的商品列表接口-start" + request.getQueryString());
            String pno = RequestUtils.getQueryParam(request, "pNo");
            String psize = RequestUtils.getQueryParam(request, "pSize");
            String sid = RequestUtils.getQueryParam(request, "shopId");
            String gcid = RequestUtils.getQueryParam(request, "goodsCategoryId");
            String oby = RequestUtils.getQueryParam(request, "orderBy");
            String storageAlarmType = RequestUtils.getQueryParam(request, "storeAlarmType");

            String goodsServerModeParam = RequestUtils.getQueryParam(request, "goodsServerMode");
            // =======新增=====
            String goodsNameParam = RequestUtils.getQueryParam(request, "goodsName");
            String goodsTypeStr = RequestUtils.getQueryParam(request, "goodsType");
            String isNeedCheck = RequestUtils.getQueryParam(request, "isNeedCheck");
            String queryData = RequestUtils.getQueryParam(request, "queryData");
            // ====================
            //==========符飞勇  新增  2016年7月4日 15:43:17==============
            String barcode = RequestUtils.getQueryParam(request, "barcode");
            String storageNumStr = RequestUtils.getQueryParam(request, "storeNum");
            String orderByModeStr = RequestUtils.getQueryParam(request, "orderByMode");
            String searchRangeStr = RequestUtils.getQueryParam(request, "searchRange");
            //==========符飞勇  新增 end==============

            if (!StringUtils.isEmpty(goodsServerModeParam))
            {
                if ("2".equals(goodsServerModeParam))
                {
                    goodsServerModeParam = "2,3";
                }
                else if ("1".equals(goodsServerModeParam))
                {
                    goodsServerModeParam = "1,3";
                }
                else if ("3".equals(goodsServerModeParam))
                {
                    goodsServerModeParam = "1,2,3";
                }
            }
            Integer searchRange = null;
            if(null != searchRangeStr){
            	searchRange = CommonValidUtil.validStrIntFmt(searchRangeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "searchRange格式错误");
            }else{
            	searchRange = 1;
            }
            CommonValidUtil.validObjectNull(sid, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Long shopId = CommonValidUtil.validStrLongFmt(sid, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId格式错误");
//            Long goodsCategoryId = null;
            int orderBy = 0;
            String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");
            // 99代表上架和下架
            if (StringUtils.isEmpty(goodsStatus))
            {
                goodsStatus = "1";
            }
            /*if (!StringUtils.isBlank(gcid))
            {
                goodsCategoryId = CommonValidUtil.validStrLongFmt(gcid, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "goodsCategoryId格式错误");
            }*/
            // 新增
            int goodsType = 0;
            if (!StringUtils.isBlank(goodsTypeStr))
            {
                CommonValidUtil.validNumStr(goodsTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsType格式错误");
                goodsType = Integer.parseInt(goodsTypeStr);
            }
            // ====

            if (!StringUtils.isBlank(oby))
            {
                CommonValidUtil.validNumStr(oby, CodeConst.CODE_PARAMETER_NOT_VALID, "orderBy格式错误");
                orderBy = Integer.parseInt(oby);
            }else{
            	orderBy=2;
            }
            Double storageNum = null;
            if(storageNumStr != null){
            	CommonValidUtil.validDoubleStr(storageNumStr, CodeConst.CODE_PARAMETER_NOT_VALID, "storeNum格式错误");
            	storageNum = Double.parseDouble(storageNumStr);
            }
            Integer orderByMode = null;
            if(orderByModeStr != null){
            	CommonValidUtil.validNumStr(orderByModeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "orderByMode格式错误");
            	orderByMode = Integer.parseInt(orderByModeStr);
            }
            
            int pageNo = CommonValidUtil.validCurrentPage(pno);// 如果格式不正确，默认赋予1
            int pageSize = CommonValidUtil.validPageSize(psize); // 如果格式不正确，默认赋予10
            Object shopTarget = shopService.getShopMainOfCacheById(shopId);
            if (shopTarget == null)
            {
                return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在", new JSONObject());
            }
            List<Long> categoryIdList = new ArrayList<Long>();
            
            if(null != gcid){
            	if(gcid.contains(",")){
            		Long goodsCategoryId = null;
            		for (String gcidString : gcid.split(",")) {
            			goodsCategoryId = CommonValidUtil.validStrLongFmt(gcidString, CodeConst.CODE_PARAMETER_NOT_VALID,
                                "goodsCategoryId格式错误");
            			categoryIdList.add(goodsCategoryId);
            			
					}
            	}else{
            		Long goodsCategoryId = CommonValidUtil.validStrLongFmt(gcid, CodeConst.CODE_PARAMETER_NOT_VALID,
                             "goodsCategoryId格式错误");
            		categoryIdList.add(goodsCategoryId);
            	}
            	
            	List<GoodsCategoryDto> goodsCategoryList = goodCategoryDao.getSubGoodsCategoryByPid(categoryIdList);
            	convertCategoryParam(categoryIdList, goodsCategoryList);
            }
            
            PageModel pageModel = new PageModel();
            pageModel.setToPage(pageNo);
            pageModel.setPageSize(pageSize);
                ShopGoodsDto shopGoodsDto = new ShopGoodsDto();
                shopGoodsDto.setShopId(shopId);
                shopGoodsDto.setOrderBy(orderBy);
                shopGoodsDto.setGoodsStatus(goodsStatus);
                shopGoodsDto.setGoodsServerModeParam(goodsServerModeParam);
                shopGoodsDto.setGoodsCategoryList(categoryIdList);
                shopGoodsDto.setQueryData(queryData);
                if(!StringUtils.isEmpty(isNeedCheck)){
                	shopGoodsDto.setIsNeedCheck(Integer.parseInt(isNeedCheck));
                }
                // 新增
                shopGoodsDto.setGoodsName(goodsNameParam);
                shopGoodsDto.setGoodsType(goodsType);
                shopGoodsDto.setStorageAlarmType(storageAlarmType);
                shopGoodsDto.setBarcode(barcode);
                shopGoodsDto.setOrderByMode(orderByMode);
                shopGoodsDto.setStorageNum(storageNum);
                shopGoodsDto.setSearchRange(searchRange);
                PageModel page = shopGoodsService.getShopGoodsByCondition(shopGoodsDto, pageModel);
                convertGoods(page);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                NumberFormat nf = NumberFormat.getInstance();
                	Map<String,Object> map = shopGoodsService.getShopGoodsStatisticsByCondition(shopGoodsDto, pageModel);
                	if(map != null){
                		if(map.get("storageSumMoney") != null){
                			double storageSumMoney =  Double.parseDouble(String.valueOf(map.get("storageSumMoney")));
                			resultMap.put("storageSumMoney", nf.format(storageSumMoney).replaceAll(",", ""));
                		}
                		if(map.get("storageTotalNum") != null){
                			double storageTotalNum = Double.parseDouble(String.valueOf( map.get("storageTotalNum")));
                			resultMap.put("storageTotalNum", nf.format(storageTotalNum).replaceAll(",", ""));
                		}
                	}
                resultMap.put("pSize",pageSize);
                resultMap.put("lst",page.getList());
    			resultMap.put("rCount", page.getTotalItem());
    			resultMap.put("pNo", pageNo);
    			
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺中的商品列表成功！", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商铺中的 商品列表失败！", e);
            throw new APISystemException("获取商铺中的商品列表失败！", e);
        }
    }

    /**
     * 转换种类参数
     * @Title: convertCategoryParam
     * @param @param categoryIdList
     * @param @param goodsCategoryList
     * @return void 返回类型
     * @throws
     */
    private void convertCategoryParam(List<Long> categoryIdList, List<GoodsCategoryDto> goodsCategoryList)
    {
        if (goodsCategoryList != null)
        {
            for (GoodsCategoryDto goodsCategoryDto : goodsCategoryList)
            {
                categoryIdList.add(goodsCategoryDto.getGoodsCategoryId());
            }
        }
    }

    private void convertGoods(PageModel pageModel) throws Exception
    {
        List<ShopGoodsDto> data = pageModel.getList();
        if (data != null)
        {
            for (ShopGoodsDto goodsDto : data)
            {
                AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
                attachmentRelationDto.setBizId(goodsDto.getGoodsId());
                attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
                attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
                List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao
                        .findByCondition(attachmentRelationDto);
                if (attachmentRelationDtos != null && attachmentRelationDtos.size() > 0)
                {
                    goodsDto.setGoodsLogo1(FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl()));
                }
                else
                {
                    goodsDto.setGoodsLogo1(goodsDto.getGoodsLogo1()==null?null:FdfsUtil.getFileProxyPath(goodsDto.getGoodsLogo1()));
                }
                goodsDto.setGoodsLogo2(goodsDto.getGoodsLogo2()==null?null:FdfsUtil.getFileProxyPath(goodsDto.getGoodsLogo2()));
            }
        }
    }

    /**
     * 获取商品详情接口
     * @Title: getGoodsDetail
     * @Description: TODO(获取商品详情)
     * @param @param request
     * @param @return
     * @return ResultDto 返回类型
     * @throws
     */
    @RequestMapping(value = "/getGoodsDetail")
    @ResponseBody
    public ResultDto getGoodsDetail(HttpServletRequest request)
    {
        try
        {
            String gid = RequestUtils.getQueryParam(request, "goodsId");
            String queryType = RequestUtils.getQueryParam(request, "queryType");// 查询类型
            CommonValidUtil.validObjectNull(gid, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsId不能为空");
            CommonValidUtil.validObjectNull(queryType, CodeConst.CODE_PARAMETER_NOT_NULL, "queryType不能为空");
            if ("0".equals(queryType))
            {
                Long goodsId = CommonValidUtil.validStrLongFmt(gid, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsId格式错误");
                ShopGoodsDto dto = shopGoodsService.getDtoShopGoods(goodsId);

                if (dto == null)
                {
                    return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST, "商品不存在", null);
                }
                else
                {
                    logger.info("查看一下具体的价格--:"+dto.getCostPrice());
                    
                    logger.info("查看一下规格--:"+dto.getGoodsProValuesIds()+"=++="+dto.getGoodsProValuesNames());
                    
                }
                // if(!("1").equals(dto.getGoodsStatus()))
                // {
                // return
                // ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST,
                // "该商品已下架", null);
                // }
                AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
                attachmentRelationDto.setBizId(dto.getGoodsId());
                attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
                attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
                List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao
                        .findByCondition(attachmentRelationDto);
                if (attachmentRelationDtos != null && attachmentRelationDtos.size() > 0)
                {
                    dto.setGoodsLogo1(FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl()));
                    dto.setGoodsLogoId(attachmentRelationDtos.get(0).getAttachmentId());
                }
                if (!StringUtils.isEmpty(dto.getGoodsLogo2()))
                {
                    dto.setGoodsLogo2(FdfsUtil.getFileProxyPath(dto.getGoodsLogo2()));
                }
                Map<String, Object> map = DataConvertUtil.convertObjToMap(dto, CommonResultConst.GET_SHOP_DETAIL);
                
                //商品套餐信息
                if (dto.getGoodsType() == 3000) {
					List<GoodsSetDto> goodsSetList = goodsSetDao.getGoodsIdListByGoodsSetId(goodsId);
					map.put("setGoodsList", goodsSetList);
                }
                
                Map<String, Object> attachmentURLmap = shopGoodsService.getAttachment(dto.getGoodsId(),
                        CommonConst.BIZ_TYPE_IS_GOODS, CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
                AttachmentRelationDto attachmentRelationDto1 = new AttachmentRelationDto();
                attachmentRelationDto1.setBizId(dto.getGoodsId());
                attachmentRelationDto1.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
                attachmentRelationDto1.setPicType(CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
                List<AttachmentRelationDto> attachmentRelationDtos1 = attachmentRelationDao
                        .findByCondition(attachmentRelationDto1);
                String attachementIds = "";
                for (AttachmentRelationDto attachmentRelationDto2 : attachmentRelationDtos1)
                {
                    attachementIds += attachmentRelationDto2.getAttachmentId() + ",";
                }
                if (!attachementIds.equals(""))
                {
                    attachementIds = attachementIds.substring(0, attachementIds.length() - 1);
                }
                map.put("goodsGroupLogoUrls", attachmentURLmap.get("urls"));
                map.put("attachementIds", attachementIds);
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商品详情成功！", map);
            }
            else
            {
                ShopGoodsDto goodsDto = getGoodsDetailForGroup(gid);
                if (goodsDto == null)
                {
                    return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST, "商品信息不存在！", null);
                }
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商品详情成功！",
                        DataConvertUtil.convertObjToMap(goodsDto, CommonResultConst.GET_SHOP_DETAIL));
            }
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取商品详情失败！", e);
            throw new APISystemException("获取商品详情失败！", e);
        }

    }

    /**
     * 获取商品组详情
     * @Title: getGoodsDetailForGroup
     * @param @param goodsId
     * @param @return
     * @param @throws Exception
     * @return GoodsDto 返回类型
     * @throws
     */
    private ShopGoodsDto getGoodsDetailForGroup(String gid) throws Exception
    {
        Long goodsId = CommonValidUtil.validStrLongFmt(gid, CodeConst.CODE_PARAMETER_NOT_VALID, "goodsId格式错误");
        ShopGoodsDto dto = new ShopGoodsDto();
        GoodsGroupDto goodsGroup = goodsGroupService.findGoodsGroupByGroupId(goodsId);
        if (goodsGroup == null)
        {
            return null;
        }
        DataConvertUtil.propertyConvertIncludeDefaultProp(goodsGroup, dto, CommonConst.GET_GOODS_GROUP_DETAIL);
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        dto.setGoodsId(goodsGroup.getGoodsGroupId());
        attachmentRelationDto.setBizId(dto.getGoodsId());
        attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS_GROUP);
        attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
        List<AttachmentRelationDto> attachmentRelationDtos = attachmentRelationDao
                .findByCondition(attachmentRelationDto);// 查找缩略图
        StringBuffer atIdbuffer = new StringBuffer("-1");
        StringBuffer atUrlBuffer = new StringBuffer("-1");
        boolean hasAtt = false;
        for (AttachmentRelationDto atDto : attachmentRelationDtos)// 拼接轮播图url及id
        {
            hasAtt = true;
            atIdbuffer.append("," + atDto.getAttachmentId());
            atUrlBuffer.append("," + FdfsUtil.getFileProxyPath(atDto.getFileUrl()));
        }
        attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
        List<AttachmentRelationDto> attaSuoNue = attachmentRelationDao.findByCondition(attachmentRelationDto);
        if (attaSuoNue != null && attaSuoNue.size() > 0)
        {
            AttachmentRelationDto suoNueDto = attaSuoNue.get(0);
            dto.setGoodsLogoId(suoNueDto.getAttachmentId());
            dto.setGoodsLogo1(FdfsUtil.getFileProxyPath(suoNueDto.getFileUrl()));
        }
        if (hasAtt)// 设置商品族的附件logo
        {
            String atIdStr = atIdbuffer.toString().replace("-1,", "");
            String atUrlStr = atUrlBuffer.toString().replace("-1,", "");
            dto.setAttachementIds(atIdStr);
            dto.setGoodsGroupLogoUrls(atUrlStr);
        }
        /* 查找商品族分类 */
        List<GoodsGroupCategoryRelationDto> goodsGroupCategoryList = goodsGroupCategoryService
                .getCategoryListByGroupId(goodsId);
        StringBuffer categoryIdbuffer = new StringBuffer("-1");
        StringBuffer categoryNameBuffer = new StringBuffer("-1");
        boolean hasCategory = false;
        for (GoodsGroupCategoryRelationDto categoryDto : goodsGroupCategoryList)// 拼接轮播图url及id
        {
            hasCategory = true;
            categoryIdbuffer.append("," + categoryDto.getGroupCategoryId());
            categoryNameBuffer.append("," + categoryDto.getCategoryName());
        }
        if (hasCategory)// 设置商品族的附件logo
        {
            String categoryIdStr = categoryIdbuffer.toString().replace("-1,", "");
            String categoryNameStr = categoryNameBuffer.toString().replace("-1,", "");
            dto.setGoodsGroupCategoryIds(categoryIdStr);
            dto.setGoodsGroupCategoryNames(categoryNameStr);
        }
        /* 查找技师 */
        StringBuffer teacIdbuffer = new StringBuffer("-1");
        StringBuffer teacNameBuffer = new StringBuffer("-1");
        boolean hasTeac = false;
        List<ShopTechRefGoodsDto> goodsGroupRefTechList = shopTechRefGoodsService.queryListByGoodsGroupId(goodsId);
        for (ShopTechRefGoodsDto techRefGoodsDto : goodsGroupRefTechList)// 拼接轮播图url及id
        {
            hasTeac = true;
            teacIdbuffer.append("," + techRefGoodsDto.getTechId());
            teacNameBuffer.append("," + techRefGoodsDto.getTechName());
        }
        if (hasTeac)// 设置商品族的附件logo
        {
            String teacIdStr = teacIdbuffer.toString().replace("-1,", "");
            String teacNameStr = teacNameBuffer.toString().replace("-1,", "");
            dto.setTechIds(teacIdStr);
            dto.setTechNames(teacNameStr);
        }
        return dto;
    }

}
