package com.idcq.appserver.controller.plugins;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.plugin.PluginModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.plugins.IPluginsService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
public class PluginsController {
    private static final Logger logger = Logger.getLogger(PluginsController.class);

    @Autowired
    private IPluginsService pluginsService;

    /*
     * @Autowired private IShopServcie shopServcie;
     */

    @Autowired
    private IShopServcie shopService;

    /**
     * PPI3：商铺选择/购买插件列表接口
     *
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value =
            {"/service/plugin/buyShopPlugin", "/token/plugin/buyShopPlugin", "/session/plugin/buyShopPlugin"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object buyShopPlugin(HttpEntity<String> entity, HttpServletRequest request) {
        logger.info("商铺选择/购买插件列表接口-start");
        try {
            Map<String, Object> requestMap = checkRequestParamVaild(entity);
            Map<String, Object> resultMap = pluginsService.buyShopPlugin(requestMap);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商铺插件订单生成成功", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("商铺选择/购买插件列表接口三方支付-系统异常", e);
            throw new APISystemException("商铺选择/购买插件列表接口-系统异常", e);
        }
    }

    private Map<String, Object> checkRequestParamVaild(HttpEntity<String> entity) throws Exception {
        Map<String, Object> requestMap = new HashMap<String, Object>();

        Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
        String shopIdStr = postParamMap.get("shopId");
        String pluginIdStr = postParamMap.get("pluginId");
        String buyNumberStr = postParamMap.get("buyNumber");
        String moneyStr = postParamMap.get("money");
        String payChannelStr = postParamMap.get("payChannel");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺Id不能为空");

        CommonValidUtil.validStrNull(pluginIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "插件Id不能为空");

        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId类型错误");

        int flag = this.shopService.queryShopExists(Long.valueOf(shopIdStr));
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        requestMap.put("shopId", shopId);

        Integer pluginId = CommonValidUtil.validStrIntFmt(pluginIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "插件Id类型错误");

        Boolean isExist = pluginsService.queryPluginIsExist(pluginId);
        if (isExist == false) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "要购买插件不存在，插件Id:" + pluginId);
        }
        requestMap.put("pluginId", pluginId);

        Integer buyNumber = buyNumberStr == null ? 1 : CommonValidUtil.validStrIntFmt(buyNumberStr,
                CodeConst.CODE_PARAMETER_NOT_VALID, "购买数量类型错误");

        if (buyNumber < 0) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "购买数量不能为负数");
        }
        requestMap.put("buyNumber", buyNumber);

        Double money = moneyStr == null ? 0 : CommonValidUtil.validStrDoubleFmt(moneyStr,
                CodeConst.CODE_PARAMETER_NOT_VALID, "购买金额数据类型错误");

        if (money < 0) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付金额不能为负数");
        }
        requestMap.put("money", money);

        Integer payChannel = payChannelStr == null ? 0 : CommonValidUtil.validStrIntFmt(payChannelStr,
                CodeConst.CODE_PARAMETER_NOT_VALID, "支付渠道类型错误");
        requestMap.put("payChannel", payChannel);
        return requestMap;
    }

    /**
     * 获取平台插件列表接口
     *
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value =
            {"/service/plugin/getPlugins", "/session/plugin/getPlugins", "/token/plugin/getPlugins"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto getPlugins(HttpServletRequest request) {
        try {
            logger.info("获取平台插件列表接口-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String columnIdStr = RequestUtils.getQueryParam(request, "columnId");
            String pluginPackages = RequestUtils.getQueryParam(request, "pluginPackages");

            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(shopIdStr)) {
                Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
                ShopDto shopDto = shopService.getShopById(shopId);
                if (null != shopDto) {
//                    Integer columnId = shopDto.getColumnId();
//                    paramMap.put("columnId", "," + columnId + ",");
                } else {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                }
            }
            /***** 参数校验 ******/
            if (StringUtils.isNotBlank(columnIdStr)) {
                Long columnId = NumberUtil.strToLong(columnIdStr, "columnId");
                // 数据库columnId字段格式：,1,2,3,
                paramMap.put("columnId",  columnId  + "");
            }
            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            if (StringUtils.isNotBlank(pluginPackages)) {
                String[] temp = pluginPackages.split(",");
                paramMap.put("pluginPackages", Arrays.asList(temp));
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            paramMap.put("pSize", pSize);
            paramMap.put("limit", (pNo - 1) * pSize);

            PageModel pageModel = pluginsService.getPlugins(paramMap);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("pNo", pNo);
            resultMap.put("rcount", pageModel.getTotalItem());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取平台插件列表接口！", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取平台插件列表接口-系统异常", e);
            throw new APISystemException("获取平台插件列表接口-系统异常", e);
        }
    }

    /**
     * 获取商铺插件列表接口
     *
     * @param product
     * @param request
     * @return
     */
    @RequestMapping(value =
            {"/service/plugin/getShopPlugins", "/session/plugin/getShopPlugins", "/token/plugin/getShopPlugins"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto getShopComments(HttpServletRequest request) {
        try {
            logger.info("获取商铺插件列表接口-start");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String isPaidStr = RequestUtils.getQueryParam(request, "isPaid");
            // 参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /***** 参数校验 ******/
            if (StringUtils.isNotBlank(isPaidStr)) {
                Integer isPaid = NumberUtil.strToNum(isPaidStr, "isPaid");
                paramMap.put("isPaid", isPaid);
            }

            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            paramMap.put("pSize", pSize);
            paramMap.put("limit", (pNo - 1) * pSize);
            paramMap.put("shopId", Long.valueOf(shopId));

            PageModel pageModel = pluginsService.getShopPlugins(paramMap);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("pNo", pNo);
            resultMap.put("rcount", pageModel.getTotalItem());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取平台插件列表接口！", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取商铺插件列表接口-系统异常", e);
            throw new APISystemException("获取商铺插件列表接口-系统异常", e);
        }
    }

    /**
     * 提供查看指定插件是否有新版本所需要的信息
     *
     * @return
     */
    @RequestMapping(value =
            {"/service/plugin/getPluginInfoForShop", "/session/plugin/getPluginInfoForShop",
                    "/token/plugin/getPluginInfoForShop"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getPluginInfoForShop(PluginModel searchModel, String token, Integer pNo, Integer pSize) {

        try {
            /*
             * 以下为参数校验
             */
            CommonValidUtil.validObjectNull(searchModel.getShopId(), CodeConst.CODE_PARAMETER_NOT_EXIST, "shopId");
            /*CommonValidUtil.validObjectNull(searchModel.getBaseSystem(), CodeConst.CODE_PARAMETER_NOT_EXIST,
                    "baseSystem");*/
            logger.debug("get update info for shopId * and baseSystem * :" + searchModel.getShopId() + ","
                    + searchModel.getBaseSystem());
            /* 设置分页信息 */
            if (null == pSize) {
                pSize = PageModel.PAGE_SIZE_20;
            }
            if (null == pNo) {
                pNo = PageModel.FIRST_PAGE;
            }
            Page page = new Page(pNo, pSize);
//            List<PluginModel> results = pluginsService.getPluginUpdateInfoForShop(searchModel, page);
            PageModel pageModel = pluginsService.getPluginUpdateInfoForShop(searchModel, page);
            List<PluginModel> dataList = pageModel.getList();
            /* 将下载链接转换成实际连接 */
            for (PluginModel model : dataList) {// 设置版本文件下载地址
                String tempUrl = model.getLastApprovedUrl();
                tempUrl = FdfsUtil.getFileProxyPath(tempUrl);
                model.setLastApprovedUrl(tempUrl);
                // 设置图标文件下载地址
                tempUrl = model.getLogoUrl();
                if (!StringUtils.isBlank(tempUrl)) {
                    model.setLogoUrl(FdfsUtil.getFileProxyPath(tempUrl));
                }
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
//            int rowCount = results == null ? 0 : results.size();
            resultMap.put("lst", dataList);
            resultMap.put("pNo", pNo);
            resultMap.put("rcount", pageModel.getTotalItem());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取平台插件列表接口！", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("获取商铺插件列表接口-系统异常", e);
            throw new APISystemException("获取商铺插件列表-系统异常", e);
        }
    }


    /**
     *
     * @param request
     * @param pluginId
     * @param versionNum
     * @param shopId
     * @return
     */
    @RequestMapping(value =
            {"/service/plugin/getPluginDetail", "/session/plugin/getPluginDetail",
                    "/token/plugin/getPluginDetail"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getPluginDetail(HttpServletRequest request, Integer pluginId, String versionNum, Integer shopId) {
        try {
            if (null == pluginId) {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "pluginId不能为空");
            }
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("pluginId", pluginId);
            if (StringUtils.isNotBlank(versionNum)) {
                queryParam.put("versionNum", versionNum);
            }
            Map<String, Object> rs = pluginsService.getPluginDetail(queryParam);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取平台插件列表接口！", rs);

        }catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取应用详情接口-系统异常", e);
            throw new APISystemException("获取应用详情接口-系统异常", e);
        }
    }


}
