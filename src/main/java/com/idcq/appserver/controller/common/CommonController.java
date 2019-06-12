package com.idcq.appserver.controller.common;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.ClientSystemTypeEnum;
import com.idcq.appserver.common.enums.ExecutePointEnum;
import com.idcq.appserver.common.enums.MsgCenterFBTypeEnum;
import com.idcq.appserver.common.enums.OptFbTypeEnum;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.CodeDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.OperateFeedBackDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.common.PageResult;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.dto.common.SmsDto;
import com.idcq.appserver.dto.common.UserPermission;
import com.idcq.appserver.dto.message.AppPushRecordDto;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.bill.IPlatformBillService;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.msgPush.appPush.PushRecordService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.HttpClientUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

/**
 * 公共组件controller
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午10:06:50
 */
@Controller
public class CommonController extends BaseController
{
    private final static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private ICollectService collectService;

    @Autowired
    private IShopServcie shopServcie;
    
    @Autowired
    private PushRecordService pushRecordService;
    
    @Autowired
    private IShopMemberService shopMemberService;
    
    @Autowired
    private IMemberServcie memberService;
    
    @Autowired
    private IAgentDao agentDao;
    @Autowired
    private IPlatformBillService platformBillService;
    /**
     * httpclient连接测试
     * @return
     */
    @RequestMapping(value = "/common/validHttpClient", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String validHttpClient(HttpServletRequest request)
    {
        try
        {
            logger.info("httpClient连接压力测试-start");
            String url = "http://ip.taobao.com/service/getIpInfo.php";
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("ip", "183.15.83.28");
            String urlStr = RequestUtils.getQueryParam(request, "urlStr");
            if (urlStr != null || "".equals(urlStr))
            {
                url = urlStr;
                queryParams = null;
            }
            HttpClientUtils httpClient = HttpClientUtils.getInstance();
            String result = httpClient.doGet(url, queryParams);
            return ResultUtil.getResultJsonStr(0, "查询成功", result);
        }
        catch (ServiceException e)
        {
            this.logger.error("httpclient连接测试-异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            this.logger.error("httpclient连接测试-系统异常", e);
            throw new APISystemException("httpclient连接测试-系统异常", e);
        }
    }

    /**
     * 获取手机验证码时直接下发短信 <br/>
     * 验证码生成规则：6位可重复随机数字
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value =
    { "/common/getVeriCode", "/token/common/getVeriCode" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto getVeriCode(HttpServletRequest request) throws Exception
    {
        long start = System.currentTimeMillis();
        logger.info("获取手机验证码-start");
        String usage = RequestUtils.getQueryParam(request, "usage");
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        // 手机号码非空校验
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        // 手机号码格式校验
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_REQUIRED_MOBILE_VALID);
        // usage(应用场景)非空校验
        CommonValidUtil.validStrNull(usage, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_VERICODE_55902);
        // 生成规定时间内有效的6位随机整数验证码
        if (CommonConst.IS_IP_LIMIT)
        {
            String ip = RequestUtils.getIpAddr(request);
            logger.info("访问者的IP地址: " + ip);
            // IP限制
            CommonValidUtil.validRequestId(ip, mobile);
        }
        boolean checkFlag = CommonConst.LIMIT_SMS_PREVENT_ATTACK_KEYS.contains(usage);
        if (checkFlag)
        {
            boolean attackflag = sendSmsService.checkSmsAttack(mobile, usage);
            if (attackflag)
            {
                return ResultUtil.getResult(CodeConst.CODE_REQUEST_OFTEN, "您获取验证码次数太多了，请稍后再试吧！", null);
            }
        }
        SmsReplaceContent content = new SmsReplaceContent();
        content.setMobile(mobile);
        content.setUsage(usage);
        content.setCacheCodeFlag(true);// 需要缓存验证码
        content.setCreateCodeFlag(true);// 需要创建验证码
        boolean flag = sendSmsService.sendSmsMobileCode(content);
        logger.info("共耗时：" + (System.currentTimeMillis() - start));
        if (flag)
        {
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_SMSCODE, null);
        }
        else
        {
            return ResultUtil.getResult(CodeConst.CODE_VERICODE_55901, CodeConst.MSG_VERICODE_55901, null);
        }
        
    }

    /*
     * @RequestMapping(value="/getVeriCode",produces=
     * "application/json;charset=utf-8")
     * 
     * @ResponseBody public ResultDto getVeriCode(HttpServletRequest request){
     * long start = System.currentTimeMillis(); try {
     * logger.info("获取手机验证码-start"); String usage =
     * RequestUtils.getQueryParam(request, "usage"); String mobile =
     * RequestUtils.getQueryParam(request, "mobile"); //手机号码非空校验
     * CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,
     * CodeConst.MSG_REQUIRED_MOBILE); //手机号码格式校验
     * CommonValidUtil.validMobileStr(mobile,
     * CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
     * //usage(应用场景)非空校验 CommonValidUtil.validStrNull(usage,
     * CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_VERICODE_55902);
     * //生成规定时间内有效的6位随机整数验证码 if (CommonConst.IS_IP_LIMIT) { String ip =
     * RequestUtils.getIpAddr(request); logger.info("访问者的IP地址: "+ip); //IP限制
     * CommonValidUtil.validRequestId(ip,mobile); } String veriCode =
     * SmsVeriCodeUtil.getIntNum(100000, 899999)+""; boolean flag =
     * commonService.sendSmsMobileCode(mobile, veriCode, usage); if (flag) {
     * //如果发送成功，则将短信验证码存入缓存中 String mKey = CommonConst.REDIS_VERICODE+mobile;
     * //获取短信验证码缓存有效时间，单位秒 int
     * cacheTime=commonService.getSettingValue(CommonConst.
     * SETTING_CODE_MOBILE_CODE, CommonConst.SETTING_KEY_MOBILE_CODE);
     * DataCacheApi.setex(mKey, veriCode, cacheTime); return
     * ResultUtil.getResult(CodeConst.CODE_SUCCEED,
     * CodeConst.MSG_SUCCEED_SMSCODE, null); }else{ return
     * ResultUtil.getResult(CodeConst.CODE_VERICODE_55901,
     * CodeConst.MSG_VERICODE_55901, null); } } catch (ServiceException e) {
     * logger.error("下发手机短信验证码异常",e); throw new APIBusinessException(e); } catch
     * (Exception e) { logger.error("下发手机短信验证码异常",e); throw new
     * APISystemException("获取手机验证码-系统异常", e); }finally{
     * logger.info("共耗时："+(System.currentTimeMillis()-start)); } }
     */

    /**
     * 手机验证码验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/common/checkVeriCode", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto checkVeriCode(HttpServletRequest request)
    {
        try
        {
            logger.info("检验短信验证码-start");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String veriCode = RequestUtils.getQueryParam(request, "veriCode");
            String usage = RequestUtils.getQueryParam(request, "usage");
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
            CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_MOBILE_VALID);
            CommonValidUtil.validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
            boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, usage, veriCode);
            if (flag)
            {
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_VERICODE, null);
            }
            else
            {
                return ResultUtil.getResult(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR, null);
            }
        }
        catch (ServiceException e)
        {
            this.logger.error("检验短信验证码-异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            this.logger.error("检验短信验证码-系统异常", e);
            throw new APISystemException("检验短信验证码-系统异常", e);
        }

    }

    /*
     * @RequestMapping(value="/checkVeriCode",produces=
     * "application/json;charset=utf-8")
     * 
     * @ResponseBody public ResultDto checkVeriCode(HttpServletRequest request){
     * try { logger.info("检验短信验证码-start"); String mobile =
     * RequestUtils.getQueryParam(request, "mobile"); String veriCode =
     * RequestUtils.getQueryParam(request, "veriCode");
     * CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,
     * CodeConst.MSG_REQUIRED_MOBILE); CommonValidUtil.validMobileStr(mobile,
     * CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
     * CommonValidUtil.validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL,
     * CodeConst.MSG_REQUIRED_VERI_CODE); String mKey =
     * CommonConst.REDIS_VERICODE+mobile; boolean flag =
     * SmsVeriCodeUtil.checkSmsCode(mKey,veriCode); if (flag) { //校验成功，将验证码失效
     * DataCacheApi.del(mKey); return
     * ResultUtil.getResult(CodeConst.CODE_SUCCEED,
     * CodeConst.MSG_SUCCEED_VERICODE, null); }else{ return
     * ResultUtil.getResult(CodeConst.CODE_VERICODE_53101,
     * CodeConst.MSG_VC_ERROR, null); } } catch (ServiceException e) {
     * this.logger.error("检验短信验证码-异常",e); throw new APIBusinessException(e); }
     * catch (Exception e) { this.logger.error("检验短信验证码-系统异常",e); throw new
     * APISystemException("检验短信验证码-系统异常", e); }
     * 
     * }
     */

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping(value = "/common/sendCode", produces = "application/json;charset=utf-8")
    @ResponseBody
    @Deprecated
    public ResultDto sendMobileCode(HttpServletRequest request)
    {
        try
        {
            String usage = RequestUtils.getQueryParam(request, "usage");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String code = RequestUtils.getQueryParam(request, "veriCode");
            // 手机号码非空校验
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
            // 手机号码格式校验
            CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_MOBILE_VALID);
            CommonValidUtil.validStrNull(code, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
            if (CommonConst.IS_IP_LIMIT)
            {
                String ip = RequestUtils.getIpAddr(request);
                logger.info("访问者的IP地址: " + ip);
                // IP限制
                CommonValidUtil.validRequestId(ip, mobile);
            }
            boolean flag = commonService.sendSmsMobileCode(mobile, code, usage);
            String mKey = CommonConst.REDIS_VERICODE_OBJ + mobile;
            if (flag)
            {
                // 如果发送成功，则将短信验证码存入缓存中
                DataCacheApi.setex(mKey, code, CommonConst.CACHE_TIME_CODE);
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_SMSCODE, null);
            }
            else
            {
                return ResultUtil.getResult(CodeConst.CODE_VERICODE_55901, CodeConst.MSG_VERICODE_55901, null);
            }
        }
        catch (ServiceException e)
        {
            logger.error("下发手机短信验证码异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("下发手机短信验证码异常", e);
            throw new APISystemException("下发手机短信验证码异常", e);
        }
    }

    /**
     * 发送手机短信
     * @param request
     * @return
     */
    @RequestMapping(value = "/common/sendMobileSms", produces = "text/plain;charset=utf-8")
    @ResponseBody
    @Deprecated
    public String sendMobileSms(HttpServletRequest request)
    {
        try
        {
            logger.info("发送手机短信-start");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String usage = RequestUtils.getQueryParam(request, "usage");
            String code = RequestUtils.getQueryParam(request, "code");
            // 手机号码非空校验
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
            // 手机号码格式校验
            CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_MOBILE_VALID);
            // 发送code非空校验
            CommonValidUtil.validStrNull(code, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
            boolean flag = commonService.sendSmsMobileCode(mobile, code, usage);
            if (flag)
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCC_MOBILE_SMS, null);
            }
            else
            {
                return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_FAIL_MOBILE_SMS, null);
            }
        }
        catch (ServiceException e)
        {
            logger.error("发送手机短信异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("发送手机短信异常", e);
            throw new APISystemException("发送手机短信异常", e);
        }
    }

    /**
     * 刷新内存中Properties中的某个key的value
     * @param request
     * @return
     */
    @RequestMapping(value = "/common/refreshAppconfig", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String refreshAppconfig(HttpServletRequest request)
    {
        try
        {
            logger.info("刷新内存中某个Properties文件中的某个key的value-start");
            String key = RequestUtils.getQueryParam(request, "key");
            String val = RequestUtils.getQueryParam(request, "val");
            String obj = RequestUtils.getQueryParam(request, "obj");
            CommonValidUtil.validStrNull(key, CodeConst.CODE_PARAMETER_NOT_NULL, "key不能为空");
            CommonValidUtil.validStrNull(obj, CodeConst.CODE_PARAMETER_NOT_NULL, "刷新对象不能为空");
            Properties properties = CommonConst.PROP_MAP.get(obj);
            CommonValidUtil.validObjectNull(properties, CodeConst.CODE_PARAMETER_NOT_EXIST, "未找到指定属性对象Properties");
            Set keys = properties.keySet();
            boolean flag = false;
            if (null != keys)
            {
                for (Iterator ite = keys.iterator(); ite.hasNext();)
                {
                    String k = (String) ite.next();
                    if (!StringUtils.isEmpty(k) && StringUtils.equals(k, key))
                    {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "刷新对象中，未找到指定key");
            }
            properties.setProperty(key, val);
            return ResultUtil.getResultJsonStr(0, "success", null);
        }
        catch (ServiceException e)
        {
            logger.error("刷新内存中Properties中的某个key的value异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("刷新内存中Properties中的某个key的value系统异常", e);
            throw new APISystemException("刷新内存中Properties中的某个key的value系统异常", e);
        }
    }

    /**
     * 根据指定key、Properties，查询value
     * @param request
     * @return
     */
    @RequestMapping(value = "/common/queryAppconfig", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String queryAppconfig(HttpServletRequest request)
    {
        try
        {
            logger.info("根据指定key、Properties，查询value-start");
            String key = RequestUtils.getQueryParam(request, "key");
            String obj = RequestUtils.getQueryParam(request, "obj");
            CommonValidUtil.validStrNull(key, CodeConst.CODE_PARAMETER_NOT_NULL, "key不能为空");
            CommonValidUtil.validStrNull(obj, CodeConst.CODE_PARAMETER_NOT_NULL, "刷新对象不能为空");
            Properties properties = CommonConst.PROP_MAP.get(obj);
            CommonValidUtil.validObjectNull(properties, CodeConst.CODE_PARAMETER_NOT_EXIST, "未找到指定属性对象Properties");
            Set keys = properties.keySet();
            boolean flag = false;
            if (null != keys)
            {
                for (Iterator ite = keys.iterator(); ite.hasNext();)
                {
                    String k = (String) ite.next();
                    if (!StringUtils.isEmpty(k) && StringUtils.equals(k, key))
                    {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "在指定的对象Properties中，未找到指定key");
            }
            String value = properties.getProperty(key);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("value", value);
            return ResultUtil.getResultJsonStr(0, "success", data);
        }
        catch (ServiceException e)
        {
            logger.error("根据指定key、Properties，查询value异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("根据指定key、Properties，查询value系统异常", e);
            throw new APISystemException("根据指定key、Properties，查询value系统异常", e);
        }
    }

    /**
     * 查询短信登录 @Title: loginIn @param @param userName @param @param
     * password @param @return @return String 返回类型 @throws
     */
    @RequestMapping(value = "/common/login", method = RequestMethod.POST)
    public String loginIn(HttpServletRequest request, @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password)
    {
        try
        {
            // 校验是否为空
            CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);

            // 校验手机号码
            CommonValidUtil.validMobileStr(userName, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_MOBILE_VALID);

            CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);

            if (!commonService.login(userName, password))
            {
                return "smsLogin";
            }
            request.getSession().setAttribute("userName", userName);

            return "WEB-INF/smsManager";
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("查询短信登录 - 系统异常", e);
            throw new APISystemException("系统异常", e);
        }
    }

    /**
     * 查询短信列表
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/common/querySmsList", method = RequestMethod.GET)
    public String querySmsList(HttpServletRequest request, Model model)
    {

        try
        {
            String userName = (String) request.getSession().getAttribute("userName");
            if (userName == null)
            {
                return "smsLogin";
            }
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            logger.debug("-----------------查询短信开始，条件mobile=" + mobile);

            if (StringUtils.isEmpty(mobile))
            {
                return "WEB-INF/smsManager";
            }

            List<SendSmsRecordDto> smsList = commonService.getSmsListByMobile(mobile);

            model.addAttribute("smsList" + "", smsList);
            model.addAttribute("mobile", mobile);

            // 写查询日志
            commonService.writeQueryLog(userName, mobile);

            return "WEB-INF/smsManager";

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("查询短信 - 系统异常", e);
            throw new APISystemException("系统异常", e);
        }
    }

    /**
     * 短信群发入口
     * @param request
     * @return
     */
    @RequestMapping(value = "/common/batchSendSms")
    public String batchSendSms(HttpServletRequest request)
    {
        try
        {
            logger.info("短信群发--start");
            new BatchSendSms().start();
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("短信群发- 系统异常", e);
            throw new APISystemException("系统异常", e);
        }
        return "success";
    }

    class BatchSendSms extends Thread
    {
        @Override
        public void run()
        {
            logger.info("开始执行短信发送任务--start");
            int count = 0;
            long startTime = System.currentTimeMillis();
            String content = "一点明月寄相思，传奇千里送真情！在全体家人的努力下，公司产品全面落地、伦敦上市在即，特遥祝您及家人：阖家团圆，幸福美满！";
            try
            {
                while (true)
                {
                    if (count == 4)
                    {
                        logger.info("执行完毕，退出线程");
                        break;
                    }
                    try
                    {
                        Map bean = commonService.getMobileInfo();
                        if (bean == null || bean.size() <= 0)
                        {
                            count++;
                        }
                        else
                        {
                            String mobile = CommonValidUtil.isEmpty(bean.get("mobile")) ? null
                                    : bean.get("mobile") + "";
                            if (!StringUtils.isBlank(mobile))
                            {
                                boolean re = commonService.sendSmsBatch(mobile, content);
                                if (re)
                                {
                                    commonService.updateStatus(mobile);
                                }
                            }
                        }
                        Thread.sleep(3000);
                    }
                    catch (Exception e)
                    {
                        logger.info("发送异常", e);
                    }
                }
            }
            catch (Exception e)
            {
                logger.info("出错了。。。", e);
            }
            finally
            {
                logger.info("共耗时：" + (System.currentTimeMillis() - startTime));
            }
        }

    }

    /**
     * 查询系统配置列表
     * @param mobile
     * @return
     */
    @RequestMapping(value =
    { "/session/common/getConfigureSettings", "/service/common/getConfigureSettings",
            "/token/common/getConfigureSettings" }, method = RequestMethod.GET)
    @ResponseBody
    public String getConfigureSettings(HttpServletRequest request)
    {
        try
        {
            String configureKeys = RequestUtils.getQueryParam(request, "configureKeys");
            String configureType = RequestUtils.getQueryParam(request, "configureType");
            Map<String, Object> requestMap = new HashMap<String, Object>();
            if (configureKeys != null)
            {
                requestMap.put("configureKeys", configureKeys.split(CommonConst.COMMA_SEPARATOR));
            }

            if (configureType != null)
            {
                requestMap.put("configureType", configureType.split(CommonConst.COMMA_SEPARATOR));
            }
            List<Map> sysConfigures = commonService.getSysConfiguresByKeys(requestMap);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询系统配置项成功", sysConfigures);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("查询系统配置项失败 - 系统异常", e);
            throw new APISystemException("查询系统配置列表系统异常", e);
        }
    }
    
    /**
     * 删除配置项
     * @param mobile
     * @return
     * @throws Exception 
     */
    @RequestMapping(value =
    { "/session/common/delConfigs", "/service/common/delConfigs",
            "/token/common/delConfigs" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delConfigs(HttpServletRequest request) throws Exception
    {
        /**
         * 1.参数解析
         */
        String configKeys = RequestUtils.getQueryParam(request, "configKeys");
        String configGroups = RequestUtils.getQueryParam(request, "configGroups");
        String bizId = RequestUtils.getQueryParam(request, "bizId");
        String bizType = RequestUtils.getQueryParam(request, "bizType");
        /**
         * 2.校验参数合法性
         */
        CommonValidUtil.validStrNull(bizId, CodeConst.CODE_PARAMETER_NOT_NULL,"bizId不允许为空");
        CommonValidUtil.validStrNull(bizType, CodeConst.CODE_PARAMETER_NOT_NULL, "bizType不允许为空");
        if (StringUtils.isBlank(configGroups) && StringUtils.isBlank(configKeys)) {
            CommonValidUtil.validStrNull(configKeys, CodeConst.CODE_PARAMETER_NOT_NULL, "configKeys和configGroups不允许同时为空");
        }
        /**
         * 3.构造查询参数
         */
        ConfigQueryCondition delCondition = new ConfigQueryCondition();
        delCondition.setBizId(NumberUtil.strToLong(bizId, "bizId"));
        delCondition.setBizType(NumberUtil.strToInteger(bizType, "bizType"));
        if (StringUtils.isNotBlank(configKeys))
        {
            String[] strs = configKeys.split(",");
            delCondition.setConfigKeys(strs);
        }

        if (StringUtils.isNotBlank(configGroups))
        {
            String[] groups = configGroups.split(",");
            delCondition.setConfigGroups(groups);
        }

        /**
         * 4.调用service查询 与合并
         */
        commonService.deleteConfigs(delCondition);

        /**
         * 5.返回查询结果
         */
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "删除配置项成功", null);

    }


    /**
     * 查询配置列表
     * @param mobile
     * @return
     */
    @RequestMapping(value =
    { "/session/common/getConfigs", "/service/common/getConfigs",
            "/token/common/getConfigs" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getConfigs(HttpServletRequest request)
    {
        try
        {
            /**
             * 1.参数解析
             */
            String configKeys = RequestUtils.getQueryParam(request, "configKeys");
            String configGroup = RequestUtils.getQueryParam(request, "configGroup");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String bizId = RequestUtils.getQueryParam(request, "bizId");
            String bizType = RequestUtils.getQueryParam(request, "bizType");
            /**
             * 2.校验参数合法性
             */
            // 校验bizId与bizType
            if ((null == bizId && null != bizType) || (null == bizType && null != bizId))
            {
                throw new ServiceException(CodeConst.CODE_PARAMETER_ILLEGAL, "bizId与bizType必须同时为空或者非空");
            }
            // 校验shopId与bizId与bizType不能同时存在
            if (null != shopId && null != bizId)
            {
                throw new ServiceException(CodeConst.CODE_PARAMETER_ILLEGAL, "bizId与bizType与shopId不能同时存在");
            }
            // 如果传入参数可以获得shopId，则校验对应的店铺是否存在
            String checkShopId = "1".equals(bizType) ? bizId : shopId;
            if (null != checkShopId)
            {
                ShopDto shopDto = shopServcie.getShopById(Long.parseLong(checkShopId));
                if (null == shopDto)
                {
                    throw new ValidateException(CodeConst.CODE_SHOP_NOT_EXISTS, CodeConst.MSG_MISS_SHOP);
                }
                if (null == shopDto.getShopStatus() || !(shopDto.getShopStatus() == CommonConst.SHOP_NORMAL_STATUS || shopDto.getShopStatus() == CommonConst.SHOP_AUDIT_STATUS))
                {
                    throw new ValidateException(CodeConst.CODE_SHOP_STATUS_ABNORMAL, CodeConst.MSG_MISS_SHOP_STATUS);
                }
            }
            /**
             * 3.构造查询参数
             */
            ConfigQueryCondition queryCondition = new ConfigQueryCondition();
            if (null != bizId)
            {
                queryCondition.setBizId(Long.parseLong(bizId));
                queryCondition.setBizType(Integer.parseInt(bizType));
            }
            if (null != shopId)
            {
                queryCondition.setShopId(Long.parseLong(shopId));
            }
            if (null != configKeys)
            {
                String[] strs = configKeys.split(",");
                queryCondition.setConfigKeys(strs);
            }
            if (null != configGroup)
            {
                String[] strs = configGroup.split(",");
                queryCondition.setConfigGroups(strs);
            }

            /**
             * 4.调用service查询 与合并
             */
            PageResult<ConfigDto> result = commonService.queryForConfig(queryCondition);

            /**
             * 5.返回查询结果
             */
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询配置项成功", result);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("查询系统配置项失败 - 系统异常", e);
            throw new APISystemException("查询系统配置列表系统异常", e);
        }
    }

    @RequestMapping(value =
    { "/session/common/updateShopConfigs", "/service/common/updateShopConfigs",
            "/token/common/updateShopConfigs" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateShopConfigs(HttpServletRequest request,HttpEntity<String> entity)
    {
        try
        {
            /**
             * 1.参数解析
             */

            ObjectMapper map = new ObjectMapper();
            Map<String, Object> params = map.readValue(entity.getBody(), new TypeReference<Map<String, Object>>()
            {
            });
            String shopIdStr = null == params.get("shopId") ? null : params.get("shopId") + "";
            List<Map<String, String>> configListStr = null == params.get("configList") ? null
                    : (List<Map<String, String>>) params.get("configList");
            /**
             * 2.校验参数合法性
             */
            // 初步校验传入的configList是否存在合法
            if (null == configListStr || configListStr.size() == 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "configList不能为空");
            }
            // 校验传入的shopId对应的商铺是否存在
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, "shopId格式不正确");
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (null == shopDto)
            {
                throw new ValidateException(CodeConst.CODE_SHOP_NOT_EXISTS, CodeConst.MSG_MISS_SHOP);
            }
            if (null == shopDto.getShopStatus() || shopDto.getShopStatus() == CommonConst.SHOP_OFFLINE_STATUS 
                    || shopDto.getShopStatus() == CommonConst.SHOP_DEL_STATUS )
            {
                throw new ValidateException(CodeConst.CODE_SHOP_STATUS_ABNORMAL, CodeConst.MSG_MISS_SHOP_STATUS);
            }

            /**
             * 3.解析参数为指定实体
             */
            List<ConfigDto> configs = new ArrayList<ConfigDto>();
            ConfigDto temp = null;
            for (Map<String, String> tempMap : configListStr)
            {
                temp = new ConfigDto();
                temp.setConfigKey(tempMap.get("configKey"));
                temp.setConfigGroup(tempMap.get("configGroup"));
                temp.setConfigValue(tempMap.get("configValue"));
                temp.setConfigDesc(tempMap.get("configDesc"));
                configs.add(temp);
            }

            /**
             * 4.更新商铺配置
             */
            commonService.updateConfigForShop(shopId, configs);

            /**
             * 5.返回查询结果
             */
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "更新配置项成功", null);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("更新店铺配置项失败 - 系统异常", e);
            throw new APISystemException("更新店铺配置异常", e);
        }
    }

    /**
     * 获取code
     * @return
     */
    @RequestMapping(value =
    { "/session/common/getCode", "/service/common/getCode",
            "/token/common/getCode" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getCode(HttpServletRequest request)
    {
        try
        {
            /*
             * token string 条件 设备令牌。Token鉴权方式必填 cashCardNo String 是 充值卡号
             * accountType int 1 否 充值账户类型。 1：用户 2：商铺 bizId int 是 充值账户ID。
             * accountType=1时，用户ID accountType=2时，商铺ID
             */
            String codeTypeStr = RequestUtils.getQueryParam(request, "codeType");
            CommonValidUtil.validStrNull(codeTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "codeType不能为空");
            List<CodeDto> resultList = commonService.getCodeByType(codeTypeStr);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("lst", resultList);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取code接口", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取code接口 - 系统异常", e);
            throw new APISystemException("获取code接口-系统异常", e);
        }
    }

    @RequestMapping(value = "/service/common/checkSession", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto checkSession(HttpServletRequest request)
    {
        try
        {
            logger.info("校验session接口 -start");
            String sessionId = RequestUtils.getQueryParam(request, "sessionId");
            CommonValidUtil.validStrNull(sessionId, CodeConst.CODE_PARAMETER_NOT_NULL, "sessionId不能为空");

            // 防止cookie没传从参数中获取下jsessionId
            boolean flag = false;
            String value = DataCacheApi.get(CommonConst.KEY_JSESSIONID + sessionId);
            if (StringUtils.isNotBlank(value))
            {
                flag = true;
            }

            if (!flag)
            {
                throw new ValidateException(CodeConst.CODE_ERROR_SESSION, CodeConst.MSG_ERROR_SESSION);
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "校验成功!", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("校验session接口-异常", e);
            throw new APISystemException("校验session接口-异常", e);
        }
    }

    @RequestMapping(value = "/service/common/checkToken", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto checkToken(HttpServletRequest request)
    {
        try
        {
            logger.info("校验token接口 -start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, "token不能为空");

            Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
            // 校验token和店铺是否存在
            collectService.queryShopAndTokenExists(shopId, token);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "校验成功!", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("校验token接口   -异常", e);
            throw new APISystemException("校验token接口   -异常", e);
        }
    }

    @RequestMapping(value =
    { "/session/common/heartbeat", "/service/common/heartbeat",
            "/token/common/heartbeat" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String heartbeat(HttpServletRequest request)
    {
        try
        {
            logger.info("心跳接口-start");
            /*
             * token string 是 店铺令牌 shopId int 是 店铺ID systemType int 是 系统。 1=收银机
             * 2=管家 clientTime long 是 收银机服务器时间（精确到毫秒
             */
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String systemTypeStr = RequestUtils.getQueryParam(request, "systemType");
            CommonValidUtil.validStrNull(systemTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "systemType不能为空");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            Integer systemType = NumberUtil.strToNum(systemTypeStr, "systemType");
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 校验店铺是否存在
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (shopDto == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }

            // 处理心跳逻辑
            commonService.heartbeat(shopId, systemType);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("serverTime", System.currentTimeMillis());
            return ResultUtil.getResultJsonStr(0, "调用成功", resultMap);
        }
        catch (ServiceException e)
        {
            logger.error("心跳接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("心跳接口异常", e);
            throw new APISystemException("心跳接口异常", e);
        }
    }

    @RequestMapping(value =
    { "/session/common/getUserAuthList", "/service/common/getUserAuthList",
            "/token/common/getUserAuthList" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUserAuthList(HttpServletRequest request) throws Exception
    {
        logger.info("PC21：获取权限列表接口  -- start");
        UserPermission userPermission = getRequestModel(request, UserPermission.class);
        List<Map<String, Object>> result = commonService.getUserAuthList(userPermission);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取用户权限列表成功", result);
    }
    
    public static void main(String[] args) {
        System.out.println(DateUtils.longStrToDate(1474341357599L, null));
    }

    /**
     * 获取客户端是否在线接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/session/getClientOnlineStatus", "/service/getClientOnlineStatus",
            "/token/getClientOnlineStatus" }, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getClientOnlineStatus(HttpServletRequest request)
    {
        try
        {
            logger.info("获取客户端是否在线接口-start");
            /*
             * token string 是 店铺令牌 shopId int 是 店铺ID systemType int 是 系统。
             * 1=收银机2=管家 intervalTimes int 3 否 心跳间隔次数。超过间隔次数无心跳的，视为离线。
             */
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String systemTypeStr = RequestUtils.getQueryParam(request, "systemType");
            String intervalTimesStr = RequestUtils.getQueryParam(request, "intervalTimes");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validStrNull(systemTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "systemType不能为空");
            CommonValidUtil.validStrNull(intervalTimesStr, CodeConst.CODE_PARAMETER_NOT_NULL, "intervalTimes不能为空");
            Integer systemType = NumberUtil.strToNum(systemTypeStr, "systemType");
            Integer intervalTimes = NumberUtil.strToNum(systemTypeStr, "intervalTimesStr");
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            // 校验店铺是否存在
            ShopDto shopDto = shopServcie.getShopById(shopId);
            if (shopDto == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }

            // 处理心跳逻辑
            Map<String, Object> resultMap = commonService.getClientOnlineStatus(shopId, systemType, intervalTimes);

            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "调用成功", resultMap, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取客户端是否在线接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取客户端是否在线接口异常", e);
            throw new APISystemException("获取客户端是否在线接口异常", e);
        }
    }

    /**
     * PC12：发起推送接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/service/common/launchPush", produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String launchPush(HttpEntity<String> entity)
    {
        try
        {
            logger.info("发起推送接口-start");
            /*
             * mobile string 手机号码 clientType int 1=收银机,2=管家APP,3=消费者APP data
             * json数据
             */
            Map<String, String> hashMap = JacksonUtil.postJsonToMap(entity);
            String mobile = hashMap.get("mobile");
            String clientType = hashMap.get("clientType");
            String data = hashMap.get("data");
            String action = hashMap.get("action");
            commonService.launchPush(mobile, clientType, action, data);

            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
        }
        catch (ServiceException e)
        {
            logger.error("发起推送接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("发起推送接口异常", e);
            throw new APISystemException("发起推送接口异常", e);
        }
    }

    /**
     * PC13：操作回馈接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/service/common/operateFeedback", "/token/common/operateFeedback",
            "/session/common/operateFeedback" }, consumes = "application/json", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String operateFeedback(HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("操作回馈接口-start");
            OperateFeedBackDto operateFeedBackDto = (OperateFeedBackDto) JacksonUtil.postJsonToObj(entity,
                    OperateFeedBackDto.class, DateUtils.DATETIME_FORMAT);
            Long bizId = operateFeedBackDto.getBizId();
            Integer bizType = operateFeedBackDto.getBizType();
            Integer feedbackType = operateFeedBackDto.getFeedbackType();
            Long userId = operateFeedBackDto.getUserId();
            Long shopId = operateFeedBackDto.getShopId();
            Integer clientSystemType = operateFeedBackDto.getClientSystem();
            Integer notifyType = operateFeedBackDto.getNotifyType();
            Integer receiverId = operateFeedBackDto.getReceiverId();
            Date clientTime = operateFeedBackDto.getClientTime();

            /**
             * 步骤一：接口入参合法性校验
             * 
             * 校验入参必填、格式合法、枚举、数据存在性
             */

            /* 校验bizId必填 */
            if(bizId != null){
            	 /* 校验bizType必填及枚举 */
                CommonValidUtil.validObjectNull(bizType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZTYPE);
                BizTypeEnum.getNameByValue(bizType);
                /* 根据bizType校验userId是否有值及数据存在性 */
                /* 根据bizType校验shopId是否有值及数据存在性 */
               /* if (bizType == BizTypeEnum.USER.getValue())
                {
                    // 用户ID必填
                    CommonValidUtil.validObjectNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_REQUIRED_USER_ID);
                    CommonValidUtil.validPositLong(bizId, CodeConst.CODE_PARAMETER_NOT_VALID,
                            CodeConst.MSG_FORMAT_ERROR_USERID);
                }
                else if (bizType == BizTypeEnum.SHOP.getValue() || bizType == BizTypeEnum.BUSAREA_ACTIVITY.getValue())
                {
                    // 商铺ID必填
                    CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_REQUIRED_SHOPID);
                    CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                            CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
                }*/
                //
//                if(receiverId != null){
//                	throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "bizId与receiverId不能同时存在");
//                }
            }
            if(null != bizType){
            	  CommonValidUtil.validObjectNull(bizId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZID);
            	  if(bizType == 13){
            		  // 商铺ID必填
                      CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,
                              CodeConst.MSG_REQUIRED_SHOPID);
                      CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                              CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            	  }
            }
//            if(receiverId != null){
//            	CommonValidUtil.validObjectNull(notifyType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_NOTIFYTYPE_NOT_NULL);
//            }
            
            /* 校验clientSystem必填及枚举 */
            CommonValidUtil.validObjectNull(clientSystemType, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_CLIENT_TYPE);
            ClientSystemTypeEnum.getNameByValue(clientSystemType);
            /* 校验反馈类型必填及枚举 */
            CommonValidUtil.validObjectNull(feedbackType, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_FEEDBACK_TYPE);
            OptFbTypeEnum.getNameByValue(feedbackType);

            if(null != notifyType){
            	CommonValidUtil.validObjectNull(receiverId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_RECEIVERIDSTR_NOT_NULL);
            	//通知类型:1=通知商铺会员,2=通知商铺,3=通知平台会员
            	if(notifyType == 1 ){
            		ShopMemberDto shopMemberDto =shopMemberService.getShopMemberDetail(Long.valueOf(receiverId));
            		if(null == shopMemberDto){
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_SHOPMEMBER);
            		}
            	}else if(notifyType == 2 ){
            		ShopDto shopDto = shopServcie.getShopById(Long.valueOf(receiverId));
            		if(null == shopDto){
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_SHOP);
            		}
            	}else if(notifyType == 3 ){
            		UserDto userDto = memberService.getUserByUserId(Long.valueOf(receiverId));
            		if(null == userDto){
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_USER);
            		}
            	}
            	
            }
            StringBuilder code = new StringBuilder();
            code.append("commonOperateFbPoint").append("_").append(bizType==null?"16":bizType)
                    // .append("_")
                    // .append(bizId)
                    .append("_").append(feedbackType);

            /**
             * 步骤二：根据程序执行点找到并执行具体的业务bean
             */
            
            String msg = (String) ProgramUtils.executeBeanByExecutePointCode(code.toString(),
                    ExecutePointEnum.getValueByName(code.toString()), operateFeedBackDto.getParamMap());

            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, msg, null);
        }
        catch (ServiceException e)
        {
            logger.error("操作回馈接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("操作回馈接口异常", e);
            throw new APISystemException("操作回馈接口异常", e);
        }
    }

    /**
     * PC14：获取消息列表接口
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/service/common/getMessageList", "/token/common/getMessageList",
            "/session/common/getMessageList" }, produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageList(HttpServletRequest request)
    {
        try
        {
            logger.info("获取消息列表接口-start");
            String clientSystemTypeStr = RequestUtils.getQueryParam(request, "clientSystemType");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
            String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
            String feedbackTypeStr = RequestUtils.getQueryParam(request, "feedbackType");
            String receiverIdStr = RequestUtils.getQueryParam(request, "receiverId");
            String notifyTypeStr = RequestUtils.getQueryParam(request, "notifyType");
            String pNo = RequestUtils.getQueryParam(request, "pNo");
            String pSize = RequestUtils.getQueryParam(request, "pSize");

            /**
             * 步骤一：接口入参合法性校验
             * 
             * 校验入参必填、格式合法、枚举、数据存在性
             */

            /* 校验接收消息终端必填及枚举 */
            CommonValidUtil.validStrNull(clientSystemTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_CLIENT_TYPE);
            CommonValidUtil.validStrIntFmt(clientSystemTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_CLIENT_TYPE);
            int clientSystemType = Integer.valueOf(clientSystemTypeStr);
            ClientSystemTypeEnum.getNameByValue(clientSystemType);
            /* 校验业务ID必填 */
            // CommonValidUtil.validStrNull(bizIdStr,
            // CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZID);
            // if(StringUtils.isNotBlank(bizIdStr)){
            // CommonValidUtil.validStrLongFmt(bizIdStr,
            // CodeConst.CODE_PARAMETER_NOT_VALID,
            // CodeConst.MSG_FORMAT_ERROR_BIZID);
            // }
            // long bizId = Long.valueOf(bizIdStr);
            /* 校验业务类型必填及枚举 */
           

            /* 校验反馈类型必填及枚举 */
            Integer feedbackType = null;
            if (!StringUtils.isBlank(feedbackTypeStr))
            {
                CommonValidUtil.validStrIntFmt(feedbackTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_FEEDBACK_TYPE);
                feedbackType = Integer.valueOf(feedbackTypeStr);
                MsgCenterFBTypeEnum.getNameByValue(feedbackType);
            }

            /**
             * 步骤二：检索消息列表
             */

            MessageCenterDto messageCenterDto = new MessageCenterDto();
            messageCenterDto.setClientSystemType(clientSystemType);
            if (StringUtils.isNotBlank(bizIdStr))
            {
                messageCenterDto.setBizId(NumberUtil.strToLong(bizIdStr, "bizId"));
                CommonValidUtil.validStrNull(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZTYPE);
                CommonValidUtil.validStrIntFmt(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_BIZTYPE);
                int bizType = Integer.valueOf(bizTypeStr);
                messageCenterDto.setBizType(bizType);
            }
           
            messageCenterDto.setFeedbackType(feedbackType);
            if (StringUtils.isNotBlank(receiverIdStr))
            {
            	CommonValidUtil.validStrLongFormat(receiverIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_RECEIVERIDSTR);
            	Long receiverId = Long.valueOf(receiverIdStr);
            	Integer notifyType = null;
            	if(notifyTypeStr != null){
            		CommonValidUtil.validStrIntFmt(notifyTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_NOTIFYTYPE);
                	notifyType = Integer.valueOf(notifyTypeStr);
                	messageCenterDto.setNotifyType(notifyType);
                	//通知类型:1=通知商铺会员,2=通知商铺,3=通知平台会员
                	if(notifyType == 1 ){
                		ShopMemberDto shopMemberDto =shopMemberService.getShopMemberDetail(Long.valueOf(receiverId));
                		if(null == shopMemberDto){
                			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_SHOPMEMBER);
                		}
                	}else if(notifyType == 2 ){
                		ShopDto shopDto = shopServcie.getShopById(Long.valueOf(receiverId));
                		if(null == shopDto){
                			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_SHOP);
                		}
                	}else if(notifyType == 3 ){
                		UserDto userDto = memberService.getUserByUserId(Long.valueOf(receiverId));
                		if(null == userDto){
                			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_MISS_USER);
                		}
                	}
            	}
//            	else{
//            		CommonValidUtil.validStrNull(notifyTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_NOTIFYTYPE_NOT_NULL);
//            	}
                messageCenterDto.setReceiverId(NumberUtil.strToLong(receiverIdStr, "receiverId"));
            }
            /* 调用服务层接口获取消息列表 */
            PageModel pageModel = commonService.getMessageList(messageCenterDto, PageModel.handPageNo(pNo),
                    PageModel.handPageSize(pSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());

            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取消息列表成功", msgList, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APISystemException("获取消息列表系统异常", e);
        }
    }

    /**
     * 缓存页面 @Title: loginIn @param @param userName @param @param
     * password @param @return @return String 返回类型 @throws
     */
    @RequestMapping(value = "/common/clearCache", method = RequestMethod.GET)
    public String loginIn(@RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password)
    {
        Properties props = ContextInitListener.SOLR_PROPS;
        if (props.getProperty("solr.password") != null || (props.getProperty("solr.password").equals(password)))
        {
            return "WEB-INF/clearCache";
        }
        else
        {
            return "Version";
        }

    }

    @RequestMapping(value =
    { "/service/common/getMqttInfo", "/token/common/getMqttInfo",
            "/session/common/getMqttInfo" }, produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String getMqttInfo(HttpServletRequest request)
    {   
        try
        {
            logger.info("获取消息列表接口-start");
            String bizId = RequestUtils.getQueryParam(request, "bizId");
            String bizType = RequestUtils.getQueryParam(request, "bizType");
            String appVersion = RequestUtils.getQueryParam(request, "appVersion");
            String clientType = RequestUtils.getQueryParam(request, "clientType");
            CommonValidUtil.validStrNull(bizId, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "bizId不能为空");
            CommonValidUtil.validStrNull(bizType, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "bizType不能为空");
            CommonValidUtil.validStrNull(appVersion, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "appVersion不能为空");
            CommonValidUtil.validStrNull(clientType, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "clientType不能为空");
            /*Map<String, Object> rs = new HashMap<String, Object>();
            rs.put("topic", new String[]{topic});
            rs.put("url", "tcp://192.168.1.209:1883");
            rs.put("userName", "");
            rs.put("password", "");*/
            Map<String, Object> rs = commonService.getAllMqttInitInfos();
            rs.putAll(commonService.getMqttInitInfoForClient(clientType, Integer.valueOf(bizType), Integer.valueOf(bizId)));
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取信息成功!", rs, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APISystemException("获取消息列表系统异常", e);
        }
    }
    
    @RequestMapping(value =
        { "/service/common/infoMsgReceived", "/token/common/infoMsgReceived",
        "/session/common/infoMsgReceived" }, produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String infoMsgReceived(HttpServletRequest request)
    {   
        try
        {   

            logger.info("上报消息-start");
            String messageId = RequestUtils.getQueryParam(request, "messageId");
            logger.info("上报消息id:" + messageId);
            String receiveTime = RequestUtils.getQueryParam(request, "receiveTime");
            String isUsed = RequestUtils.getQueryParam(request, "isUsed");
            CommonValidUtil.validStrNull(messageId, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "messageId不能为空");
            CommonValidUtil.validStrNull(receiveTime, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "receiveTime不能为空");
            CommonValidUtil.validStrNull(isUsed, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "isUsed不能为空");
//            Map<String, Object> rs = new HashMap<String, Object>();
            AppPushRecordDto appPushRecordDto = new AppPushRecordDto();
            Date receivedDate = new Date(Long.parseLong(receiveTime));
            Date reportDate = new Date();
            appPushRecordDto.setMessageId(messageId);
            appPushRecordDto.setReceiveTimeClient(receivedDate);
            appPushRecordDto.setReportTime(reportDate);
            appPushRecordDto.setIsUsed(Integer.valueOf(isUsed));
            appPushRecordDto.setMessageStatus(2);
            pushRecordService.updateRecord(appPushRecordDto);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "上传信息成功!", null, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("获取消息列表接口异常", e);
            throw new APISystemException("获取消息列表系统异常", e);
        }
    }
    
  /*   public static void main(String[] args)
    {
        System.out.println(System.currentTimeMillis());
    }*/
    @RequestMapping(value =
        { "/service/common/download", "/token/common/download",
        "/session/common/download" }, method = RequestMethod.GET)
    public void fileDownloader(HttpServletResponse response) throws Exception{
        response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=dict.txt");  
        response.setContentType("application/octet-stream; charset=utf-8");  
        OutputStream o = response.getOutputStream();
        FileInputStream in = new FileInputStream("C:/Users/Administrator/Desktop/serverapp-debug.apk");
        byte[] tem = new byte[1024];
        int len = 0;
        while((len = in.read(tem)) != -1){
            o.write(tem, len, len);
            o.flush();
        }
        in.close();
        o.close();
    }
/*    public static void main(String[] args)
    {   
        int i = 1;
        System.out.println((i = 3) != 3);
    }*/
    
    /**
     * PC27发送短信接口
     * @param response
     * @throws Exception
     */
    @RequestMapping(value =
        { "/service/common/sendSMS", "/token/common/sendSMS",
        "/session/common/sendSMS" }, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object sendSMS(HttpEntity<String> entity, HttpServletRequest request) throws Exception{
    	try
        {
            logger.info("发送短信接口-start");
            SmsDto smsDto = (SmsDto)JacksonUtil.postJsonToObj(entity,SmsDto.class);
            if(smsDto != null){
            	if(!((null != smsDto.getMemberIds() && null == smsDto.getUserIds() && null ==smsDto.getMobiles())
            		|| (null != smsDto.getUserIds() && null == smsDto.getMemberIds() && null ==smsDto.getMobiles())
            		|| (null != smsDto.getMobiles() && null == smsDto.getMemberIds() && null ==smsDto.getUserIds())
            			)){
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "userIds,memberIds和mobiles互斥，且三者必填其一");
            	}
            	CommonValidUtil.validLongNull(smsDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_SHOP_ID_NULL);
            	CommonValidUtil.validStrNull(smsDto.getSmsContent(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_SMSCONTENT_ID_NULL);
            	CommonValidUtil.validIntNoNull(smsDto.getSmsType(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_SMSTYPE_ID_NULL);
            	if(smsDto.getSmsType() != CommonConst.SMS_TYPE_ACCOUNT
            			&& smsDto.getSmsType() != CommonConst.SMS_TYPE_MARKETING_SMS){
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "短信账户类型参数无效");
            	}
            	SensitiveWordsUtil.checkSensitiveWords("smsContent", smsDto.getSmsContent());
            	commonService.sendSmsByEntity(smsDto);
            }else{
            	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "发送短信传递的参数不能为空");
            }
            logger.info("发送短信接口-end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"发送成功！", null);
        }catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e){
            logger.error("发送短信接口异常", e);
            throw new APISystemException("发送短信接口异常", e);
        }
    }
    
    /**
     * PC28代理商信息变动通知接口
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = { "/service/common/agentInfoChange", "/token/common/agentInfoChange",
            "/session/common/agentInfoChange" }, method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object agentInfoChange(HttpEntity<String> entity, HttpServletRequest request) throws Exception {
        logger.info("PC28代理商信息变动通知接口-start");
        String userId = RequestUtils.getQueryParam(request, "userId");
        String agentId = RequestUtils.getQueryParam(request, "agentId");

        CommonValidUtil.validStrNull(agentId, CodeConst.CODE_PARAMETER_NOT_VALID, "agentId不能为空");
        CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_VALID, "userId不能为空");
        UserDto user = memberService.getUserByUserId(NumberUtil.strToLong(userId, "userId"));
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, "用户不存在");
        AgentDto agent = agentDao.getAgentByUserIdAndAgentId(Long.valueOf(userId), Long.valueOf(agentId));
        CommonValidUtil.validObjectNull(agent, CodeConst.CODE_PARAMETER_NOT_EXIST, "代理商不存在");
        
        platformBillService.insertPlatformBill(1, agent.getSlottingFee(), null, new OrderDto(), 
        		null, "消费支付", "o2o线下添加", CommonConst.PLATFORM_BILL_TYPE_PAY, CommonConst.PLATFORM_BILL_STATUS_OVER);
        
        Double dRebatesDayMoney = commonService.agentInfoChange(NumberUtil.strToLong(agentId, "agentId"), NumberUtil.strToLong(userId, "userId"), null);
        DataCacheApi.del(CommonConst.KEY_USER + userId);
        if(dRebatesDayMoney > 0D) {
            //发送短信通知给代理商
            SmsReplaceContent src = new SmsReplaceContent();
            src.setMobile(user.getMobile());
            src.setMoney(dRebatesDayMoney);
            src.setUsage("agent_rebates");
            sendSmsService.sendSmsMobileCode(src);
        }
        logger.info("PC28代理商信息变动通知接口-end");

        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "请求成功！", null);
    }
}
