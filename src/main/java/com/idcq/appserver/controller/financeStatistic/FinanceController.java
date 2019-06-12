package com.idcq.appserver.controller.financeStatistic;

import com.idcq.appserver.common.AsynchronousTask.producer.MqPusher;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.controller.shop.ShopController;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.IUnitDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.dto.goods.*;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.file.IFileService;
import com.idcq.appserver.service.finance.IFinanceServcie;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.goods.IGoodsSetService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.*;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
import com.idcq.appserver.utils.mq.goods.GoodsMessageUtil;
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
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto;
import com.idcq.appserver.service.finance.IFinanceServcie;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 商品（服务）controller
 *
 * @author Administrator
 * @date 2015年3月8日
 * @time 下午5:38:07
 */
@Controller public class FinanceController extends BaseController
{
    private final static Logger log = LoggerFactory.getLogger(FinanceController.class);

    @Autowired private IFinanceServcie financeServcie;

    /**
     * PCF1：账务统计接口
     *
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/finance/financeStatistic", "/token/finance/financeStatistic",
            "/session/finance/financeStatistic",
            "/service/finance/financeStatistic" }, method = RequestMethod.POST, produces = "application/json;charset=utf-8") @ResponseBody public Object financeStatistic(
            HttpServletRequest request, HttpEntity<String> entity) throws Exception
    {
        String queryParams = entity.getBody();
        CommonValidUtil.validStrNull(queryParams, CodeConst.CODE_PARAMETER_NOT_NULL, "请求参数不能为空");
        Map<String, Object> rs = financeServcie.financeStatistic(queryParams);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "请求完成", rs);
    }

    @RequestMapping(value = { "/token/finance/getFinanceStatisticDetail", "/session/finance/getFinanceStatisticDetail",
            "/service/finance/getFinanceStatisticDetail", }, produces = "application/json;charset=UTF-8") @ResponseBody public String getShopWithdrawList(
            HttpServletRequest request) throws Exception
    {

        log.info("PCF2：查询账务统计明细接口-start");
        Map<String, Object> parms = getRequestMap(request);
        String beginDate = (String) parms.get("beginDate");
        String endDate = (String) parms.get("endDate");
        String statisticType = (String) parms.get("statisticType");
        String operatorId = (String) parms.get("operatorId");
        String operatorName = (String) parms.get("operatorName");
        String isPass = (String) parms.get("isPass");
        String beginStatisticTime = (String) parms.get("beginStatisticTime");
        String endStatisticTime = (String) parms.get("endStatisticTime");
        String pageNo = (String) parms.get("pageNo");
        String pageSize = (String) parms.get("pageSize");

        FinanceStatisticDto financeStatis = new FinanceStatisticDto();
        if (StringUtils.isNotBlank(beginDate))
        {
            financeStatis.setBeginDate(DateUtils.stringToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate))
        {
            financeStatis.setEndDate(DateUtils.stringToDate(endDate));
        }
        if (StringUtils.isNotBlank(statisticType))
        {
            financeStatis.setStatisticType(NumberUtil.stringToInteger(statisticType));
        }
        if (StringUtils.isNotBlank(operatorId))
        {
            financeStatis.setOperatorId(NumberUtil.stringToLong(operatorId));
        }
        if (StringUtils.isNotBlank(operatorName))
        {
            financeStatis.setOperatorName(operatorName);
        }
        if (StringUtils.isNotBlank(isPass))
        {
            financeStatis.setIsPass(NumberUtil.stringToInteger(isPass));
        }
        if (StringUtils.isNotBlank(beginStatisticTime))
        {
            financeStatis.setBeginStatisticTime(DateUtils.parse(beginStatisticTime, DateUtils.DATETIME_FORMAT));
        }
        if (StringUtils.isNotBlank(endStatisticTime))
        {
            financeStatis.setEndStatisticTime(DateUtils.parse(endStatisticTime, DateUtils.DATETIME_FORMAT));
        }
        if (StringUtils.isNotBlank(isPass))
        {
            financeStatis.setIsPass(NumberUtil.stringToInteger(isPass));
        }
        financeStatis.setLimit((PageModel.handPageNo(pageNo) - 1) * PageModel.handPageSize(pageSize));
        financeStatis.setPageSize(PageModel.handPageSize(pageSize));

        PageModel pageModel = financeServcie.getFinanceStatisticDetail(financeStatis);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("pNo", pageNo);
        resultMap.put("rCount", pageModel.getTotalItem());
        resultMap.put("lst", pageModel.getList());
        log.info("PCF2：查询账务统计明细接口-end");
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询账务统计明细成功！", resultMap, DateUtils.DATETIME_FORMAT);
    }

    @RequestMapping(value = { "/token/finance/getFinanceStat", "/session/finance/getFinanceStat",
            "/service//finance/getFinanceStat",
            "/finance/getFinanceStat" }, produces = "application/json;charset=UTF-8") @ResponseBody public String getFinanceStat(
            HttpServletRequest request) throws Exception
    {

        log.info("PCF3：查询账务统计状态接口-start");
        Map<String, Object> parms = getRequestMap(request);
        String beginStatisticTime = (String) parms.get("beginStatisticTime");
        String endStatisticTime = (String) parms.get("endStatisticTime");

        FinanceStatisticDto financeStatis = new FinanceStatisticDto();
        if (StringUtils.isNotBlank(beginStatisticTime))
        {
            financeStatis.setBeginStatisticTime(DateUtils.parse(beginStatisticTime, DateUtils.DATETIME_FORMAT));
        }
        if (StringUtils.isNotBlank(endStatisticTime))
        {
            financeStatis.setEndStatisticTime(DateUtils.parse(endStatisticTime, DateUtils.DATETIME_FORMAT));
        }
        List<Map<String, Object>> resultMap = financeServcie.getFinanceStat(parms);

        log.info("PCF3：查询账务统计状态接口-end");

        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询账务统计状态成功！", resultMap, DateUtils.DATETIME_FORMAT);
    }
}
