package com.idcq.appserver.service.finance;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.financeStatistic.IfinanceStatisticDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.admin.IAdminService;
import com.idcq.appserver.utils.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

/**
 * 优惠券service
 *
 * @author Administrator
 * @date 2015年3月30日
 * @time 上午11:03:06
 */
@Service public class FinanceServiceImpl implements IFinanceServcie
{
    private static final Logger log = LoggerFactory.getLogger(FinanceServiceImpl.class);

    @Autowired private IfinanceStatisticDao financeStatisticDao;

    @Autowired private IAdminService adminService;

    @Autowired private ITransactionDao transactionDao;

    @Autowired private IfinanceStatisticDao ifinanceStatisticDao;

    @Override public Map<String, Object> financeStatistic(String param) throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> params = JacksonUtil.simpleParseJson2Map(param);
        String endDateStr = params.get("endDate");
        //操作人id: 1dcq_admin.admin_id
        String operatorIdStr = params.get("operatorId");
        //操作人名字
        String operatorName = params.get("operatorName");
        //财务统计账务列表
        String financeStatisticListStr = params.get("financeStatisticList");
        //账务统计备注
        String remark = params.get("remark");
        String beginDateStr = params.get("beginDate");
        //校验beginDate
        Date beginDate = CommonValidUtil
                .validDateStr(beginDateStr, CodeConst.CODE_PARAMETER_NOT_NULL, "beginDate不能为空或者格式不正确");
        //校验endDate
        Date endDate = CommonValidUtil
                .validDateStr(endDateStr, CodeConst.CODE_PARAMETER_NOT_NULL, "endDate不能为空或者格式不正确");
        //校验operatorId
        Long operatorId = CommonValidUtil
                .validStrLongFmt(operatorIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "operatorId不能为空或者格式不正确");
        adminService.checkAdminValid(Integer.valueOf(operatorIdStr.trim()));
        //验证operatorName不能为空
        if (StringUtils.isBlank(operatorName))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operatorName不能为空");
        }
        operatorName = operatorName.trim();
        //验证statisticTypeStr
        //        Integer statisticType = CommonValidUtil.validStrIntFmt(statisticTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "statisticType不能为空或者格式不正确");
        if (StringUtils.isBlank(financeStatisticListStr))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "financeStatisticList不能为空");
        }
        ObjectMapper mapper = new ObjectMapper();
        JavaType temp = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
        JavaType finalType = mapper.getTypeFactory().constructParametricType(List.class, temp);
        List<Map<String, String>> financeParams = mapper.readValue(financeStatisticListStr, finalType);
        if (null == financeParams || financeParams.isEmpty())
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "financeStatisticList不能为空");
        }

        // --初步校验完毕
        //统计并比对结果
        int isPass = 0;

        List<Map<String, Object>> staticsRecord = new LinkedList<>();
        if (this.compareFinance(financeParams, beginDate, endDate, staticsRecord))
        {
            isPass = 1;
        }

        /* 开始处理相关记录 */
        //插入表1dcq_finance_statistic
        FinanceStatisticDto financeStatisticDto = new FinanceStatisticDto();
        financeStatisticDto.setBeginDate(beginDate);
        financeStatisticDto.setEndDate(endDate);
        financeStatisticDto.setIsPass(isPass);
        financeStatisticDto.setOperatorId(operatorId);
        financeStatisticDto.setOperatorName(operatorName);
        financeStatisticDto.setRemark(remark);
        financeStatisticDto.setStatisticTime(new Date());
        financeStatisticDto.setStatisticType(1);
        ifinanceStatisticDao.insertFinanceStatistic(financeStatisticDto);

        //插入1dcq_finance_statistic_detail表
        Long statisticId = financeStatisticDto.getStatisticId();
        Iterator<Map<String, Object>> iterator = staticsRecord.iterator();
        Map<String, Object> tempRecord = null;
        while (iterator.hasNext())
        {
            tempRecord = iterator.next();
            tempRecord.put("statisticId", statisticId);
        }
        ifinanceStatisticDao.insertfinanceStatisticDetails(staticsRecord);

        //插入1dcq_finance_statistic_stat表
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        Long beginDaysTimeMills = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        Long endDaysTimeMills = calendar.getTimeInMillis();
        int dayBetween = (int) ((endDaysTimeMills - beginDaysTimeMills) / (1000 * 60 * 60 * 24));
        List<Map<String, Object>> financeStatisticStats = new LinkedList<Map<String, Object>>();
        Map<String, Object> tempFinanceStatisticStat = null;
        Date createTime = new Date();
        for (int i = 0; i <= dayBetween; i++)
        {
            GregorianCalendar worldTour = new GregorianCalendar();
            worldTour.setTime(beginDate);
            worldTour.add(GregorianCalendar.DATE, i);
            tempFinanceStatisticStat = new HashMap<String, Object>();
            tempFinanceStatisticStat.put("isPass", isPass);
            tempFinanceStatisticStat.put("statisticId", statisticId);
            tempFinanceStatisticStat.put("createTime", createTime);
            tempFinanceStatisticStat.put("statisticDate", worldTour.getTime());
            financeStatisticStats.add(tempFinanceStatisticStat);
        }
        ifinanceStatisticDao.insertFinanceStatisticStats(financeStatisticStats);

        /* 构造返回结果 */
        result.put("beginDate", beginDateStr);
        result.put("endDate", endDateStr);
        result.put("isPass", isPass);
        result.put("statisticList", staticsRecord);
        return result;
    }

    private boolean compareFinance(List<Map<String, String>> financeParams, Date beginDate, Date endDate,
            List<Map<String, Object>> inserts)
    {
        boolean isPass = true;

        /* 一次查出时间段内的统计信息 */
        //初始化时间信息
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        endDate = calendar.getTime();
        /* 查出指定时间段的统计结果 */
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("beginDate", beginDate);
        queryParams.put("endDate", endDate);
        //        queryParams.put("billDirection", statisticType);
        List<TransactionDto> staticsResult = transactionDao.getPlatformIncomeStaticsByTimeAndMoneySource(queryParams);

        /* 开始比较统计结果 */
        Iterator<Map<String, String>> iterator = financeParams.iterator();
        Iterator<TransactionDto> tempIterator = null;
        TransactionDto tempStatics = null;
        Map<String, String> tempParams = null;
        String moneySource = null;
        String amountFinance = null;
        Map<String, Object> record = null;
        while (iterator.hasNext())
        {
            tempIterator = staticsResult.iterator();
            tempParams = iterator.next();
            moneySource = tempParams.get("moneySource");
            //校验moneySource不能为空
            if (StringUtils.isBlank(moneySource))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "moneySource不能为空");
            }
            moneySource = moneySource.trim();

            //形成插入记录
            record = new HashMap<String, Object>();

            amountFinance = tempParams.get("amountFinance");
            //amountFinance,处理上传的金额，并且处理成两位小数进行比较
            if (StringUtils.isBlank(amountFinance))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "amountFinance不能为空");
            }
            Double amountFinanceDouble = Double.parseDouble(amountFinance);
            BigDecimal b = new BigDecimal(amountFinanceDouble);
            amountFinanceDouble = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            record.put("amountFinance", amountFinanceDouble);
            record.put("moneySource", Integer.valueOf(moneySource));

            moneySource = this.getTypeDesc(Integer.valueOf(moneySource));
            while (tempIterator.hasNext())
            { //依次检查
                tempStatics = tempIterator.next();

                //如果是所查类型moneySource
                if (moneySource.equals(tempStatics.getRdOrgName()))
                {
                    Double staticFinanceDouble = tempStatics.getPayAmount();
                    staticFinanceDouble = staticFinanceDouble == null ? 0d : staticFinanceDouble;
                    BigDecimal b1 = new BigDecimal(staticFinanceDouble);
                    staticFinanceDouble = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    record.put("amountOnline", staticFinanceDouble);
                    record.put("amountDiffer", amountFinanceDouble.doubleValue() - staticFinanceDouble.doubleValue());
                    if (amountFinanceDouble.doubleValue() != staticFinanceDouble.doubleValue())
                    {
                        //标志整个结果失败
                        if (isPass)
                        {
                            isPass = false;
                        }
                        record.put("isPass", 0);

                    }
                    else
                    {
                        record.put("isPass", 1);
                    }
                    break;
                }
            }
            if (record.get("isPass") == null)    //如果没有匹配的
            {
                record.put("amountOnline", 0d);
                record.put("amountDiffer", amountFinanceDouble.doubleValue());
                if (amountFinanceDouble.doubleValue() != 0)
                {
                    if(isPass)
                    {
                        isPass = false;
                    }
                    record.put("isPass", 0);
                }else{
                    record.put("isPass", 1);
                }
            }
            inserts.add(record);
        }

        return isPass;
    }

    private String getTypeDesc(Integer moneySource)
    {
        String result = "";
        switch (moneySource)
        {
            case 1:
                result = "支付宝";
                break;
            case 2:
                result = "建设银行";
                break;
      /*      case 3:
                result = "转充";
                break;*/
            case 4:
                result = "微信";
                break;
            case 5:
                result = "农业银行";
                break;
            default:
                log.error("不受扶持的moneySource类型");
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "不受扶持的moneySource类型");
        }
        return result;
    }

    public static void main(String[] args)
    {
        GregorianCalendar worldTour = new GregorianCalendar();
        worldTour.add(GregorianCalendar.DATE, 200); // 当前日期加100天\
        Date d = worldTour.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String s = df.format(d);
        System.out.println("100天后的日期是：" + s);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.finance.IFinanceServcie#getFinanceStatisticDetail(java.util.Map)
     */
    @Override public PageModel getFinanceStatisticDetail(FinanceStatisticDto parms) throws Exception
    {
        PageModel pageModel = new PageModel();
        int count = financeStatisticDao.getFinanceStatisticCount(parms);
        if (count > 0)
        {
            List<FinanceStatisticDto> financeStatisticList = financeStatisticDao.getFinanceStatistic(parms);
            pageModel.setList(updateFinanceStatisticDetail(financeStatisticList));
            pageModel.setTotalItem(count);
        }

        return pageModel;
    }

    /**
     * 查询财务流水
     */
    private List<Map<String, Object>> updateFinanceStatisticDetail(List<FinanceStatisticDto> financeStatisticList)
            throws Exception
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(financeStatisticList))
        {
            for (FinanceStatisticDto financeStatisticDto : financeStatisticList)
            {
                Map<String, Object> financeStatisticMap = new HashMap<String, Object>();
                financeStatisticMap.put("statisticId", financeStatisticDto.getStatisticId());
                financeStatisticMap.put("beginDate", financeStatisticDto.getBeginDate());
                financeStatisticMap.put("endDate", financeStatisticDto.getEndDate());
                financeStatisticMap.put("operatorId", financeStatisticDto.getOperatorId());
                financeStatisticMap.put("operatorName", financeStatisticDto.getOperatorName());
                financeStatisticMap.put("statisticType", financeStatisticDto.getStatisticType());
                financeStatisticMap.put("remark", financeStatisticDto.getRemark());
                financeStatisticMap.put("statisticResult", financeStatisticDto.getStatisticResult());
                financeStatisticMap.put("isPass", financeStatisticDto.getIsPass());
                financeStatisticMap.put("statisticTime", financeStatisticDto.getStatisticTime());
                financeStatisticMap.put("financeStatisticList", financeStatisticDao
                        .getFinanceStatisticDetailBystatisticId(financeStatisticDto.getStatisticId()));
                result.add(financeStatisticMap);
            }
        }

        return result;

    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.finance.IFinanceServcie#getFinanceStat(java.util.Map)
     */
    @Override public List<Map<String, Object>> getFinanceStat(Map<String, Object> parms) throws Exception
    {
        return financeStatisticDao.getFinanceStat(parms);
    }
}
