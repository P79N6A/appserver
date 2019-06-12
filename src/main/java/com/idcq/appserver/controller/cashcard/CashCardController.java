package com.idcq.appserver.controller.cashcard;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.admin.IAdminService;
import com.idcq.appserver.service.cashcard.ICashCardService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

@Controller
public class CashCardController {

	private static final Logger logger = Logger.getLogger(CashCardController.class);
	
	@Autowired
	public ICashCardService cashCardServcie;
	@Autowired
	public IAdminService adminServcie;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private ICollectService collectService;
	/**
	 * 生成充值卡接口 
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/service/pay/generateCashCard", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String generateCashCard(@RequestParam("cashCardBatchId") String cashCardBatchId,
			@RequestParam("operaterId") String operaterId){
		
		try {
			logger.info("批次生成充值卡-start");
			Map<String, Object> requestMap = checkValidParamOfgenerateCashCard(cashCardBatchId, operaterId);
			cashCardServcie.generateCashCardAsynchronously(requestMap);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "充值卡正在批次生成", null);
		} catch (ServiceException e) {
			logger.error("批次生成充值卡异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("批次生成充值卡异常",e);
			throw new APISystemException("批次生成充值卡异常", e);
		}
	}
	
	private Map<String, Object> checkValidParamOfgenerateCashCard(String cashCardBatchIdStr,String operaterIdStr) throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		CommonValidUtil.validStrNull(cashCardBatchIdStr,
				CodeConst.CODE_PARAMETER_NOT_NULL,
				"充值卡批次ID");
		
		Long cashCardBatchId = CommonValidUtil.validStrLongFmt(cashCardBatchIdStr,
				CodeConst.CODE_PARAMETER_NOT_VALID,
				"cashCardBatchId数据类型错误");
		cashCardServcie.checkCashCardBatchvalidity(cashCardBatchId,false);
		requestMap.put("cashCardBatchId", cashCardBatchId);
		
		CommonValidUtil.validStrNull(operaterIdStr, 
				CodeConst.CODE_PARAMETER_NOT_VALID,
				"操作人ID");
		
		Integer operaterId = CommonValidUtil.validStrIntFmt(operaterIdStr,
				CodeConst.CODE_PARAMETER_NOT_VALID,
				"operaterId数据类型错误");
		adminServcie.checkAdminValid(operaterId);
		requestMap.put("operaterId", operaterId);
		return requestMap;
	}
	
    /**
     * 下载充值卡接口
     * @param request
     * @return
     */
    @RequestMapping(value="/service/pay/downloadCashCard",produces="application/json;charset=utf-8")
    @ResponseBody
    public String downloadCashCard(HttpServletRequest request){
        try {
            logger.info("下载充值卡接口-start");
            /*
             * cashCardBatchId int 是 充值卡批次ID 
             * operaterId int 是 操作人ID
             */
            String cashCardBatchIdStr = RequestUtils.getQueryParam(request, "cashCardBatchId");
            String operaterIdStr = RequestUtils.getQueryParam(request, "operaterId");
            
            CommonValidUtil.validStrNull(operaterIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "operaterId不能为空");
            CommonValidUtil.validStrNull(cashCardBatchIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "cashCardBatchId不能为空");
            
            Long operaterId = NumberUtil.strToLong(operaterIdStr, "operaterId");
            Long cashCardBatchId = NumberUtil.strToLong(cashCardBatchIdStr, "cashCardBatchId");
            
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("operaterId", operaterId);
            pMap.put("cashCardBatchId", cashCardBatchId);
            Map<String, Object> resultMap = cashCardServcie.downloadCashCard(pMap);
            
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "正在生成", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e){
            logger.error("下载充值卡接口-异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("下载充值卡接口-异常",e);
            throw new APISystemException("下载充值卡接口-异常", e);
        }
    }
    /**
     * 充值卡充值接口
     * @param request
     * @return
     */
    @RequestMapping(value={"/service/pay/useCashCard","/session/pay/useCashCard","/token/pay/useCashCard"},produces="application/json;charset=utf-8")
    @ResponseBody
    public String useCashCard(HttpServletRequest request){
        try {
            logger.info("充值卡充值接口-start");
            /*
             * cashCardNo String 是 充值卡号 
             * cardPassword String 是 卡密码 
             * accountType int 1 否 充值账户类型。 1：用户 2：商铺 
             * mobile String 条件 充值到手机号账户 mobile和Id需填写一个。 accountType=1时，用户手机号 accountType=2时，商铺手机号
             * bizId int 条件 充值账户ID。 mobile和bizId需填写一个。两个都填，不允许，报错：无效参数。accountType=1时，用户ID accountType=2时，商铺ID 
             * fromSystem int 是 来源系统。1：收银机 2：管家APP 3：消费者APP 4：微信商城 5：公众号 6：商铺后台
             * operaterId int 是 操作人ID。来源系统收银机时，shopId，其他的userId。 
             * remark String 条件 充值备注。收银机必填。
             */
            String token = RequestUtils.getQueryParam(request, "token");
            String cashCardNoStr = RequestUtils.getQueryParam(request, "cashCardNo");
            String cardPassword = RequestUtils.getQueryParam(request, "cardPassword");
            String accountTypeStr = RequestUtils.getQueryParam(request, "accountType");
            String mobile = RequestUtils.getQueryParam(request, "mobile");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
            String operaterIdStr = RequestUtils.getQueryParam(request, "operaterId");
            String fromSystemStr = RequestUtils.getQueryParam(request, "fromSystem");
            String remark = RequestUtils.getQueryParam(request, "remark");
            
            
            Map<String, Object> pMap = new HashMap<String, Object>();
            
            CommonValidUtil.validStrNull(cashCardNoStr, CodeConst.CODE_PARAMETER_NOT_NULL, "cashCardNo不能为空");
            CommonValidUtil.validStrNull(cardPassword, CodeConst.CODE_PARAMETER_NOT_NULL, "cardPassword不能为空");
            CommonValidUtil.validStrNull(fromSystemStr, CodeConst.CODE_PARAMETER_NOT_NULL, "fromSystem不能为空");
            CommonValidUtil.validStrNull(operaterIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "operaterId不能为空");
            //两个都填，不允许，报错：无效参数
            if(StringUtils.isNotBlank(mobile)
                    && StringUtils.isNotBlank(bizIdStr)){
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_INVALID_PARAMETER);
            }
            if(StringUtils.isBlank(mobile) && 
                    StringUtils.isNotBlank(bizIdStr)){
                CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId不能为空");
                Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");
                pMap.put("bizId", bizId);
            }
            if(StringUtils.isNotBlank(mobile)
                    && StringUtils.isBlank(bizIdStr)){
                CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "mobile不能为空");
                pMap.put("mobile", mobile);
            }
            if(StringUtils.isNotBlank(remark)){
                pMap.put("remark", remark);
            }
            if(StringUtils.isBlank(accountTypeStr)){
                //默认值 1用户,2商铺
                pMap.put("accountType", 1);
            }
            else{
                Integer accountType = NumberUtil.strToNum(accountTypeStr, "accountType");
                pMap.put("accountType", accountType);
            }
            //操作人ID
            Long operaterId = NumberUtil.strToLong(operaterIdStr, "operaterId");
            //系统类型
            Integer fromSystem = NumberUtil.strToNum(fromSystemStr, "fromSystem");
            if(fromSystem==1){//来源为收银机
                //校验token和店铺是否存在
                collectService.queryShopAndTokenExists(operaterId,token);
            }
            pMap.put("cashCardNo", cashCardNoStr);
            pMap.put("cardPassword", AESUtil.aesEncrypt(cardPassword, CommonConst.CASHCARD_PASSWORD_KEY));
            pMap.put("operaterId", operaterId);
            pMap.put("fromSystem", fromSystem);
            
            Map<String, Object> resultMap = cashCardServcie.useCashCard(pMap);
            resultMap.remove("accountId");
            
            //发送短信
            Integer chargeType = (Integer) resultMap.get("chargeType");//充值类型
            String accountName = (String) resultMap.get("accountName");//账号名称、商铺名称
            String accounMobile = (String) resultMap.get("accounMobile");//手机号码
            Double afterAllAmount = (Double) resultMap.get("afterAllAmount");/*充值后余额。  会员包括：平台奖励+消费金、  商铺包括：线上营业收入+平台奖励+保证金*/
            Double money = (Double) resultMap.get("money");//充值金额
            if(chargeType!=null && chargeType==1){//用户充值
                SmsReplaceContent src = new SmsReplaceContent();
                src.setConsumeDate(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT1));
                src.setAcountAmount(afterAllAmount);
                src.setAmount(money);
                src.setMobile(accounMobile);
                src.setUsage(CommonConst.CHARGE_USER);
                sendSmsService.sendSmsMobileCode(src);
            }
            if(chargeType!=null && chargeType==2){//商铺充值
                SmsReplaceContent src = new SmsReplaceContent();
                src.setConsumeDate(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT1));
                /*充值后余额。
                                                     会员包括：平台奖励+消费金
                                                      商铺包括：线上营业收入+平台奖励+保证金
                */
                src.setAcountAmount(afterAllAmount);
                src.setAmount(money);
                src.setMobile(accounMobile);
                src.setUsage(CommonConst.CHARGE_SHOP);
                src.setShopName(accountName);
                sendSmsService.sendSmsMobileCode(src);
            }
            
            //移除无用返回结果
            resultMap.remove("chargeType");//充值类型
            resultMap.remove("accountName");//账号名称、商铺名称
            resultMap.remove("accounMobile");//手机号码
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "充值成功", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e){
            logger.error("充值卡充值接口异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("充值卡充值接口异常",e);
            throw new APISystemException("充值卡充值接口异常", e);
        }
    }
    /**
     * 充值卡充值记录查询接口/pay/getCashCardUsed
     * @return
     */
    @RequestMapping(value={"/service/pay/getCashCardUsed","/session/pay/getCashCardUsed","/token/pay/getCashCardUsed"},produces="application/json;charset=utf-8")
    @ResponseBody
    public String getCashCardUsed(HttpServletRequest request){
        try {
            /*
             * token string 条件 设备令牌。Token鉴权方式必填
             *  cashCardNo String 是 充值卡号
             * accountType int 1 否 充值账户类型。 1：用户 2：商铺 bizId int 是 充值账户ID。
             * accountType=1时，用户ID accountType=2时，商铺ID
             */
            String cashCardNoStr = RequestUtils.getQueryParam(request, "cashCardNo");
            String accountTypeStr = RequestUtils.getQueryParam(request, "accountType");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");

            Map<String, Object> pMap = new HashMap<String, Object>();
            
            CommonValidUtil.validStrNull(cashCardNoStr, CodeConst.CODE_PARAMETER_NOT_NULL, "cashCardNo不能为空");
            CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId不能为空");
            
            Long bizId = NumberUtil.strToLong(bizIdStr, "biz");
            
            if(StringUtils.isBlank(accountTypeStr)){
                //默认值 1用户,2商铺
                pMap.put("accountType", 1);
            }
            else{
                Integer accountType = NumberUtil.strToNum(accountTypeStr, "accountType");
                pMap.put("accountType", accountType);
            }
            pMap.put("cashCardNo", cashCardNoStr);
            pMap.put("bizId", bizId);
            Map<String, Object>  resultMap = cashCardServcie.getCashCardUsed(pMap);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "充值卡充值记录查询接口", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("充值卡充值记录查询接口 - 系统异常", e);
            throw new APISystemException("充值卡充值记录查询接口-系统异常", e);
        }
    }
}
