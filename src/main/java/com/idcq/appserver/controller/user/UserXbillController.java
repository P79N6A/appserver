package com.idcq.appserver.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 会员信息controller
 *
 * @author Administrator
 * @date 2015年3月3日
 * @time 下午7:43:09
 */
@Controller @RequestMapping(value = "/user") public class UserXbillController
{
    private final Logger logger = Logger.getLogger(UserXbillController.class);

    @Autowired private IMemberServcie memberService;

    /**
     * P41：查询用户消费金账单接口
     *
     * @param @param  request
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: getUserXbill
     */
    @RequestMapping(value = "/getUserXbill", produces = "application/json;charset=UTF-8") @ResponseBody public String getUserXbill(
            HttpServletRequest request)
    {
        try
        {
/*			userId
            billType
			startTime
			endTime
			pNo
			pSize*/
            logger.info("P41：查询用户消费金账单接口-start");
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String billTypeStr = RequestUtils.getQueryParam(request, "billType");
            String startTime = RequestUtils.getQueryParam(request, "startTime");
            String endTime = RequestUtils.getQueryParam(request, "endTime");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String uccId = RequestUtils.getQueryParam(request, "uccId");
            Map<String, Object> map = new HashMap<String, Object>();
            //billType
            if (StringUtils.isNotBlank(billTypeStr))
            {
                Integer billType = NumberUtil.strToNum(billTypeStr, "billType");
                map.put("billType", billType);
            }
            // user
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
            Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "用户ID数据格式错误");
            CommonValidUtil.validObjectNull(memberService.getUserByUserId(userId), CodeConst.CODE_PARAMETER_NOT_EXIST,
                    CodeConst.MSG_MISS_MEMBER);
            map.put("userId", userId);
            map.put("uccId", uccId);
            // 分页默认10条，第一页
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            map.put("n", (pNo - 1) * pSize);
            map.put("m", pSize);
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
            PageModel pageModel = memberService.getUserXbill(map);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", pageModel.getList());
            resultMap.put("rCount", pageModel.getTotalItem());
            resultMap.put("pNo", pNo);
            logger.info("P41：查询用户消费金账单接口-end");
            return ResultUtil
                    .getResultJson(CodeConst.CODE_SUCCEED, "获取用户消费金账单成功！", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("P41：查询用户消费金账单接口-系统异常", e);
            throw new APISystemException("P41：查询用户消费金账单接口-系统异常", e);
        }
    }

    /**
     * 查询会员账单明细接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getBillDetail", produces = "application/json;charset=UTF-8") @ResponseBody public String getBillDetail(
            HttpServletRequest request) throws Exception
    {
        logger.info("P45：查询会员账单明细接口-start");
        String accountTypeStr = RequestUtils.getQueryParam(request, "accountType");
        String userIdStr = RequestUtils.getQueryParam(request, "userId");
        String startTime = RequestUtils.getQueryParam(request, "startTime");
        String endTime = RequestUtils.getQueryParam(request, "endTime");
        String moneyType = RequestUtils.getQueryParam(request, "moneyType");
        String areaIdStr = RequestUtils.getQueryParam(request, "areaId");
        String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        String pSizeStr = RequestUtils.getQueryParam(request, "pSize");

        Map<String, Object> requestParamMap = new HashMap<String, Object>();
        //验证userid
        CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);

        Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "用户ID数据格式错误");

        CommonValidUtil.validObjectNull(memberService.getUserByUserId(userId), CodeConst.CODE_PARAMETER_NOT_EXIST,
                CodeConst.MSG_MISS_MEMBER);

        requestParamMap.put("userId", userId);

        //验证accountType
        CommonValidUtil
                .validStrNull(accountTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ACCOUNT_TYPE);

        requestParamMap.put("accountType", accountTypeStr.split(","));
        //验证startTime
        CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_START_TIME);

        CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");

        requestParamMap.put("startTime", startTime);

        //校验endTime
        if (StringUtils.isNotBlank(endTime))
        {
            CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");

            requestParamMap.put("endTime", endTime);
        }

        //校验moneyType
        if (StringUtils.isNotBlank(moneyType))
        {
            requestParamMap.put("moneyType", moneyType.split(","));
        }

        //校验areaId
        if (StringUtils.isNotBlank(areaIdStr))
        {
            Integer areaId = CommonValidUtil
                    .validStrIntFmt(areaIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "areaId类型错误");
            requestParamMap.put("areaId", areaId);
        }

        // 分页默认10条，第一页
        Integer pNo = PageModel.handPageNo(pNoStr);
        Integer pSize = PageModel.handPageSize(pSizeStr);
        requestParamMap.put("pageNum", (pNo - 1) * pSize);
        requestParamMap.put("pageSize", pSize);
        requestParamMap.put("isShow", CommonConst.USER_BILL_IS_SHOW);
        PageModel pageModel = memberService.getBillDetail(requestParamMap);
        NumberUtil.formatDoubleResult2BigDecimalResult(pageModel.getList(), new String[] { "money", "balance" }, 4);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("lst", pageModel.getList());
        resultMap.put("rCount", pageModel.getTotalItem());
        resultMap.put("pNo", pNo);

        logger.info("P45：查询会员账单明细接口-end");
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取会员账单明细成功！", resultMap, DateUtils.DATETIME_FORMAT);
    }

}
