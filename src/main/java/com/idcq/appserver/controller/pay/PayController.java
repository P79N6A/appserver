package com.idcq.appserver.controller.pay;

import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.InputSource;

import com.abc.trustpay.client.ebus.PaymentResult;
import com.idcq.appserver.alipay.config.AlipayConfig;
import com.idcq.appserver.alipay.util.AlipayNotify;
import com.idcq.appserver.common.BankInfo;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.ccb.RSASig;
import com.idcq.appserver.common.enums.UserStatusEnum;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.bank.BankDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.message.ResultListToClient;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.packet.RedPacketPayInfo;
import com.idcq.appserver.dto.pay.GroupPayDetailModel;
import com.idcq.appserver.dto.pay.GroupPayModel;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.plugin.ShopPluginDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.bank.IBankService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.membercard.IUserMemberBillService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IChuanQiPayService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.pay.IThirdPayService;
import com.idcq.appserver.service.pay.ali.util.AliPayUtil;
import com.idcq.appserver.service.pay.model.PayNotifyResult;
import com.idcq.appserver.service.plugins.IPluginsService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.wxscan.MD5Util;
/**
 * 支付controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:20:07
 */
@Controller
public class PayController {
	private final static Logger logger = LoggerFactory.getLogger(PayController.class);
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	@Autowired
	public IPayServcie payServcie;
	@Autowired
	public IMemberServcie memberServcie;
	@Autowired
	public IUserMemberBillService userBillService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IShopServcie shopServcie;
	/**
	 * 银行查询
	 */
	@Autowired
	private IBankService bankService;
	@Autowired
	private IPushService pushService;
	@Autowired
	private IOrderServcie orderServcie;
    @Autowired
    private IMemberServcie memberService;	
	
    @Autowired
	private IPluginsService pluginsService;
    @Autowired
    private IChuanQiPayService chuanQiPayService;
    @Autowired
    private IPacketService packetServcie;
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private IAgentDao agentDao;
	/**
	 * 传奇宝支付
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/payByLegender")
	@ResponseBody
	public ResultDto payByLegender(HttpServletRequest request){
		try {
			long start=System.currentTimeMillis();
			logger.debug("传奇宝支付-start:"+start);
			String userIdStr = RequestUtils.getQueryParam(request,"userId");
			String orderId = RequestUtils.getQueryParam(request,"orderId");
			String orderPayType = RequestUtils.getQueryParam(request,"orderPayType");
			String payAmount = RequestUtils.getQueryParam(request,"payAmount");
			String payPassword = RequestUtils.getQueryParam(request,"payPassword");
			
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			CommonValidUtil.validNumStr(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,"userId参数不合法");
			CommonValidUtil.validObjectNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,"orderId不能为空");
			CommonValidUtil.validObjectNull(payAmount, CodeConst.CODE_PARAMETER_NOT_NULL,"payAmount不能为空");
			CommonValidUtil.validObjectNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL,"payPassword不能为空");
			if(orderPayType==null){
				orderPayType="0";
			}
			Long userId=NumberUtil.strToLong(userIdStr,"userId");
			UserDto user=memberServcie.getUserByUserId(userId);
			if(user==null){//用户不存在
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_REQUIRED_MEMBER);
			}
			if(!payPassword.equals(user.getPayPassword())){//密码错误
				throw new ValidateException(CodeConst.CODE_PWD_ERROR, CodeConst.MSG_PWD_ERROR);
			}
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime=format.format(new Date());
			TransactionDto transactionDto=new TransactionDto();
			transactionDto.setUserId(userId);
			transactionDto.setOrderId(orderId);
			transactionDto.setPayAmount(Double.parseDouble(payAmount));
			transactionDto.setTransactionTime(nowTime);
			transactionDto.setStatus(0);
			transactionDto.setUserPayChannelId(new Long(1));
			transactionDto.setOrderPayType(Integer.parseInt(orderPayType));
			transactionDto.setLastUpdateTime(nowTime);
			transactionDto.setTransactionType(0);
			Integer tranId=this.payServcie.addTransaction(transactionDto);
			
			PayDto payDto=new PayDto();
			payDto.setOrderId(orderId);
			//payDto.setPayId(tranId);
			payDto.setPayType(1);
			payDto.setPayAmount(Double.parseDouble(payAmount));
			payDto.setOrderPayType(Integer.parseInt(orderPayType));
			payDto.setUserId(userId);
			payDto.setOrderPayTime(nowTime);
			payDto.setLastUpdateTime(nowTime);
			payDto.setPayeeType(0);
			Map<String,Object> resultMap = this.payServcie.addOrderTransAndPay(transactionDto,payDto);
			//OrderGoodsSettleUtil.detailSingleOrder(userId,orderId);
			logger.info("共耗时:"+(System.currentTimeMillis()-start));
			
			//推送消息
			pushOrder(orderId, userId, resultMap);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "传奇宝支付成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("传奇宝支付-系统异常", e);
			throw new APISystemException("传奇宝支付-系统异常", e);
		}
	}

	// 推送消息
	private void pushOrder(String orderId, Long userId, Map<String, Object> resultMap) {
        try
        {
            // 支付成功，向用户推送消息，并将推送的消息记录
            if (null != resultMap && resultMap.size() > 0)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", "pay");
                jsonObject.put("orderId", orderId);
                jsonObject.put("amount", resultMap.get("amount"));
                commonService.pushUserMsg(jsonObject, userId, CommonConst.USER_TYPE_ZREO);

                // 向收银机推送
                OrderDto order = this.orderServcie.getOrderMainById(orderId);
                if (order != null)
                {
                    // TODO 空指针
                    logger.info("订单信息：orderId=" + orderId + ",orderStatus=" + order.getOrderStatus()
                            + ",orderPayStatus=" + order.getPayStatus() + "。");
                    // 订单状态为待确认、支付状态为已支付，向一点管家推送订单确认消息
                    if (order.getOrderStatus() == CommonConst.ORDER_STS_DQR
                            && order.getPayStatus() == CommonConst.PAY_STATUS_PAYED)
                    {
                        commonService.pushShopUserMsg("confirmOrder", "您有新订单，请在10分钟内处理", order.getShopId());
                    }

                    if (null != order)
                    {
                        Integer orderStatus = order.getOrderStatus();
                        // 如果订单还是已预订状态则需要给收银机进行推送 -- 暂时不推
                        // if (null != orderStatus && CommonConst.ORDER_STS_YYD
                        // == orderStatus) {
                        // PlaceOrderPushMessageUtil.detailSingleOrder(orderId);
                        // }
                        Long shopId = order.getShopId();
                        if (null != shopId)
                        {
                            StringBuilder content = new StringBuilder();
                            content.append("{");
                            content.append("\"shopId\":" + shopId + ",");
                            content.append("\"action\":\"cashOrder\",");
                            content.append("\"data\":{\"id\":\"" + orderId + "\",\"orderStatus\":" + orderStatus
                                    + ",\"payStatus\":" + order.getPayStatus() + "}");
                            content.append("}");
                            PushDto push = new PushDto();
                            push.setAction("cashOrder");
                            push.setContent(content.toString());
                            push.setUserId(userId);
                            push.setShopId(shopId);
                            pushService.pushInfoToShop2(push);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("传奇宝支付消息推送异常", e);
        }
	}
	
	/**
	 * 多个订单形成支付订单组的接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/groupOrders")
	@ResponseBody
	public ResultDto groupOrders(HttpServletRequest request){
		try {
			long start=System.currentTimeMillis();
			logger.info("多个订单形成支付订单组-start:"+start);
			String orderIds = RequestUtils.getQueryParam(request,"orderIds");
			CommonValidUtil.validObjectNull(orderIds, CodeConst.CODE_PARAMETER_NOT_NULL,"orderIds不能为空");
			String orderGroupId = this.payServcie.groupOrders(orderIds);
			Map map=new HashMap();
			map.put("orderId", orderGroupId);
			logger.info("共耗时:"+(System.currentTimeMillis()-start));
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "多个订单形成支付订单组成功", map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("多个订单形成支付订单组-系统异常", e);
			throw new APISystemException("多个订单形成支付订单组-系统异常", e);
		}
	}
	
	/**
	 * 查询订单组待支付金额
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/getOrdersToBePayedAmount")
	@ResponseBody
	public ResultDto getOrdersToBePayedAmount(HttpServletRequest request){
		try {
			long start=System.currentTimeMillis();
			logger.info("查询订单组待支付金额-start:"+start);
			String orderId = RequestUtils.getQueryParam(request,"orderId");
			String orderPayType = RequestUtils.getQueryParam(request,"orderPayType");
			CommonValidUtil.validObjectNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,"orderId不能为空");
			if(orderPayType==null){
				orderPayType="0";
			}
			//CommonValidUtil.validObjectNull(orderPayType, CodeConst.CODE_PARAMETER_NOT_NULL,"orderPayType不能为空");
			CommonValidUtil.validNumStr(orderPayType, CodeConst.CODE_PARAMETER_NOT_VALID,"orderPayType格式错误");
			Map map=this.payServcie.getOrdersToBePayedAmount(orderId,Integer.parseInt(orderPayType));
			logger.info("共耗时:"+(System.currentTimeMillis()-start));
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询订单组待支付金额成功", map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询订单组待支付金额-系统异常", e);
			throw new APISystemException("查询订单组待支付金额-系统异常", e);
		}
	}
	
	
	/**
	 * 验证用户的支付密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/authPayPassword",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object authPayPassword(HttpServletRequest request){
		try {
			logger.info("验证用户的支付密码-start");
			
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String payPassword = RequestUtils.getQueryParam(request, "payPassword");
			
			int type = 1;
			String typeStr =  RequestUtils.getQueryParam(request, "type");
			if("2".equals(typeStr)){
				type = 2;
			}
			CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			if(type == 1){
				CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
				CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
				UserDto user = new UserDto();
				user.setMobile(mobile);
				user.setPayPassword(payPassword);
				this.memberServcie.authPayPassword(user);
			}else {
				CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
				memberServcie.authShopPayPassword(mobile, payPassword);
				
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_AUTHPAYPWD, null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("验证用户的支付密码-系统异常",e);
			throw new APISystemException("验证用户的支付密码-系统异常", e);
		}
	}
	/**
	 * 验证userId、pNo、pSize参数合法性
	 * @param userId
	 * @param pNo
	 * @param pSize
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void verifyParameter(String userId,String pNo,String pSize) throws Exception{
		if(null==userId){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
		}
		//userId参数不合法
		NumberUtil.strToLong(userId, "userId");
		//验证userid是否为空
		if((StringUtils.isEmpty(userId))){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
		}
		//验证用户是否
 
		UserDto user = memberServcie.getUserByUserId(Long.parseLong(userId));
		if(null==user){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		}		
		//pNo参数不合法
		if(!NumberUtil.isNumeric(pNo)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PNO_FMT_ERROR);
		}
		//pSize参数不合法
		if(!NumberUtil.isNumeric(pSize)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PSIZE_FMT_ERROR);
		}
	}	
	/**
	 * 使用第三方支付
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/payBy3rd",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object payBy3rd(HttpServletRequest request){
		try {
			logger.info("使用第三方支付-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			//订单
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			//金额
			String payAmount = RequestUtils.getQueryParam(request, "payAmount");
			//发起交易的终端类型，必填
			String terminalType = RequestUtils.getQueryParam(request, "terminalType");
			//第三方支付名称，填
			String rdOrgName = RequestUtils.getQueryParam(request, "3rdOrgName");
			//用户支付渠道id
			String userPayChannelId = RequestUtils.getQueryParam(request, "userPayChannelId");
			//订单支付类型 
			String orderPayType = RequestUtils.getQueryParam(request, "orderPayType");
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_REQUIRED_MEMBER);
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			//验证用户是否存在 
			UserDto user = memberServcie.getUserByUserId(userId);
			if(null==user){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}	
			Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
			transaction3rdDto.setUserId(userId);
			transaction3rdDto.setOrderId(orderId);
			transaction3rdDto.setTerminalType(terminalType);
			transaction3rdDto.setRdOrgName(rdOrgName);
			//默认为0 (消费)
			transaction3rdDto.setTransactionType(0);
			if(StringUtils.isNotEmpty(orderPayType)){
				transaction3rdDto.setOrderPayType(Integer.parseInt(orderPayType));
			}
			else{
				//订单支付类型：0(单个订单支付），1（多个订单支付）
				transaction3rdDto.setOrderPayType(0);
			}
			//支付金额
			if(StringUtils.isNotEmpty(payAmount)){
				NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR,payAmount);
				transaction3rdDto.setPayAmount(Double.parseDouble(payAmount));
			}
			if(StringUtils.isNotEmpty(userPayChannelId)){
				//用户支付渠道id不合法
				if(!NumberUtil.isNumeric(userPayChannelId)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PAYCHANNELID_FORMAT_ERROR);
				}
				transaction3rdDto.setUserPayChannelId(Long.parseLong(userPayChannelId));
			}
			
			String transactionId = payServcie.payBy3rd(transaction3rdDto);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("transactionId", transactionId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDER_IS_EXIST, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("使用第三方支付-系统异常",e);
			throw new APISystemException("使用第三方支付-系统异常", e);
		}
	}
	
	/**
	 * 页面调用支付宝支付接口
	 * 样例：http://61.144.170.170:9101/appServer/interface/alipay?userId=10001&orderId=11111&subject=测试是个奶瓶&fee=0.01
	 * @param request  TODO 商铺充值
	 * @param attre
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/serverCommunicate/alipay3rd")
	public String alipay(HttpServletRequest request, RedirectAttributes attre) throws Exception{
		
		//商铺ID
		String shopId = RequestUtils.getQueryParam(request, "shopId"); 
		CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺号shopId不能为空");
		// 订单标题
		String subject = RequestUtils.getQueryParam(request, "subject");
		if(StringUtils.isBlank(subject)){
			subject = "充值";
		}
		//付款金额
		String total_fee =  RequestUtils.getQueryParam(request, "fee");
		
		// 验证用户是否存在
		ShopAccountDto shopAccount = memberServcie.getShopAccountById(Long.parseLong(shopId));
		if (null == shopAccount) {
			return "accountNotExsit";
		}
		
		// 生成交易记录
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setUserId(Long.valueOf(shopId));
		transactionDto.setStatus(CommonConst.TRANSACTION_STATUS_WAIT); // 待支付
		transactionDto.setPayAmount(Double.valueOf(total_fee));
		transactionDto.setRdOrgName("支付宝");
		transactionDto.setTransactionType(3); // 商铺充值
		transactionDto.setTerminalType(CommonConst.TERMINAL_TYPE_PHP);
		transactionDto.setTransactionTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		transactionDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		transactionDto.setOrderPayType(0); // 订单支付类型：0(单个订单支付），1（多个订单支付）
		
		payServcie.addTransaction(transactionDto);
		long transactionId = transactionDto.getTransactionId();
		
		//生成商铺账单
		ShopBillDto shopBillDto = buildShopBill(transactionDto);
		// 设置商铺账单的账户余额
		shopBillDto.setAccountAmount(shopAccount.getAmount());
		userBillService.insertShopBill(shopBillDto);
		//生成支付宝订单
		String orderId = FieldGenerateUtil.generatebitOrderId(transactionId);
		attre.addAttribute("WIDout_trade_no", orderId);
		attre.addAttribute("WIDsubject", subject);
		attre.addAttribute("WIDtotal_fee", total_fee);
				
		return "redirect:/alipayapi.jsp";
		
	}
	

	// 构建商铺管理员充值账单
	private ShopBillDto buildShopBill( TransactionDto transactionDto) {
		
		ShopBillDto shopBillDto = new ShopBillDto();
		shopBillDto.setShopId(transactionDto.getUserId());
		shopBillDto.setMoney(transactionDto.getPayAmount());
		//待反馈进度
		shopBillDto.setBillStatus(1); // 账单状态:处理中=1,成功=2,失败=3
		shopBillDto.setBillDesc(CommonConst.SHOPER_CHARGE);
		shopBillDto.setTransactionId(transactionDto.getTransactionId());
		//账单类型:1（账户资金增加）,-1（账户资金减少）
		shopBillDto.setBillDirection(1);
		shopBillDto.setCreateTime(new Date());
		shopBillDto.setBillType("5");// 账单类型:销售商品=1,支付保证金=2,购买红包=3,提现=4,充值=5,推荐奖励=6
		
		return shopBillDto;
	}
	
	
	/**
	 * 页面调用支付宝支付支付，支付成功后跳转同步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/callBack")
	public String callBack(HttpServletRequest request){
		
		String trade_no = "";
		String fee = "";
		String url = "";
		try {
			//获取支付宝GET过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = RequestUtils.getQueryParam(request, "out_trade_no"); 

			//支付宝交易号
			trade_no = RequestUtils.getQueryParam(request, "trade_no"); 

			//交易状态
			String trade_status = RequestUtils.getQueryParam(request, "trade_status");
			
			String notifyId = RequestUtils.getQueryParam(request, "notify_id");
			
			fee = RequestUtils.getQueryParam(request, "total_fee");
			
			logger.info("支付宝跳转同步通知：商家订单号："+out_trade_no+"; 支付宝交易号："+trade_no+"; 交易状态："+trade_status);
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params, AlipayConfig.sign_type_MD5);
			
			logger.info("支付宝跳转成功, 验证verify:"+verify_result);
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
				
				Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
				transaction3rdDto.setRdTransactionId(trade_no);
				transaction3rdDto.setRdNotifyId(notifyId);
				String transactionIdStr = out_trade_no;
                if(out_trade_no.length() > 15)
                {
                    transactionIdStr = out_trade_no.substring(15);//15为时间戳长度yyMMddHHmmssSSS
                }
				transaction3rdDto.setTransactionId(Long.parseLong(transactionIdStr));
				
				// 支付成功
				transaction3rdDto.setStatus(CodeConst.CODE_STATUS_SUCCSSS);

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					
					payServcie.handleAlipayBack(transaction3rdDto);
				}
				
				//该页面可做页面美工编辑
				logger.info("验证成功<br />");
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{
				//该页面可做页面美工编辑
				logger.info("验证失败");
			}
			
		} catch(ServiceException e)
		{
			throw new APIBusinessException(e);
			
		} catch (Exception e) {
			logger.error("使用第三方支付-系统异常",e);
			
		}
		
		Properties properties = ContextInitListener.COMMON_PROPS;
		if(properties != null)
			url = properties.getProperty("phpCallBack");
		
		return "redirect:"+url+"?trade_no="+trade_no+"&fee="+fee;
	}
	
	
	

	/**
	 * 页面支付成功后支付宝服务器异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/notifyfromPageAlipay")
	public void notifyfromPageAlipay(HttpServletRequest request, HttpServletResponse response){
		try {
			//获取支付宝GET过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String valueStr = "";
				valueStr = RequestUtils.getQueryParam(request, name);
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = RequestUtils.getQueryParam(request, "out_trade_no"); 

			//支付宝交易号
			String trade_no = RequestUtils.getQueryParam(request, "trade_no"); 

			//交易状态
			String trade_status = RequestUtils.getQueryParam(request, "trade_status"); 
			
			String notifyId = RequestUtils.getQueryParam(request, "notify_id");
			
			logger.info("支付宝异步通知：商家订单号："+out_trade_no+"; 支付宝交易号："+trade_no+"; 交易状态："+trade_status);
			
			//打印body是不是乱码
			String body = RequestUtils.getQueryParam(request, "body");
			logger.info("body="+body);
			
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params, AlipayConfig.sign_type_MD5);
			
			
			logger.info("支付宝异步通知成功, 验证verify:"+verify_result);
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
				transaction3rdDto.setRdTransactionId(trade_no);
				transaction3rdDto.setRdNotifyId(notifyId);
				String transactionIdStr = out_trade_no;
                if(out_trade_no.length() > 15)
                {
                    transactionIdStr = out_trade_no.substring(15);//15为时间戳长度yyMMddHHmmssSSS
                }
				transaction3rdDto.setTransactionId(Long.parseLong(transactionIdStr));
				
				// 支付成功
				transaction3rdDto.setStatus(CodeConst.CODE_STATUS_SUCCSSS);
				
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					payServcie.handleAlipayBack(transaction3rdDto);
					
					
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					payServcie.handleAlipayBack(transaction3rdDto);
					
					
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
				}
			
			}
			
			response.getWriter().write("success");
			
		} catch (Exception e) {
			logger.error("使用第三方支付-系统异常",e);
			throw new APISystemException("使用第三方支付-系统异常", e);
		}
		
	}
	
	
	
	/**
	 * 客户端支付宝支付，支付成功后支付宝服务器异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response){
		try {
			//获取支付宝GET过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String valueStr = "";
				valueStr = RequestUtils.getQueryParam(request, name);
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = RequestUtils.getQueryParam(request, "out_trade_no"); 

			//支付宝交易号
			String trade_no = RequestUtils.getQueryParam(request, "trade_no"); 

			//交易状态
			String trade_status = RequestUtils.getQueryParam(request, "trade_status"); 
			
			String notifyId = RequestUtils.getQueryParam(request, "notify_id");
			
			logger.info("支付宝异步通知：商家订单号："+out_trade_no+"; 支付宝交易号："+trade_no+"; 交易状态："+trade_status);
			
			//打印body是不是乱码
			String body = RequestUtils.getQueryParam(request, "body");
			logger.info("body="+body);
			
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params, AlipayConfig.sign_type_RSA);
			
			logger.info("支付宝异步通知成功, 验证verify:"+verify_result);
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
				transaction3rdDto.setRdTransactionId(trade_no);
				transaction3rdDto.setRdNotifyId(notifyId);
				String transactionIdStr = out_trade_no.substring(15);//15为时间戳长度yyMMddHHmmssSSS
				transaction3rdDto.setTransactionId(Long.parseLong(transactionIdStr));
				
				// 支付成功
				transaction3rdDto.setStatus(CodeConst.CODE_STATUS_SUCCSSS);
				
				
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					logger.debug("payServcie.nofity3rdPayStatus -- start");
					handleClientAlipayNotify(transaction3rdDto,CommonConst.USER_BILL_TYPE_ALIPAY);
					logger.debug("payServcie.nofity3rdPayStatus -- end");
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					logger.debug("payServcie.nofity3rdPayStatus -- start");
					handleClientAlipayNotify(transaction3rdDto,CommonConst.USER_BILL_TYPE_ALIPAY);
					logger.debug("payServcie.nofity3rdPayStatus -- end");
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
				}
			
			}
			
			response.getWriter().write("success");
			
		} catch (Exception e) {
			logger.error("使用第三方支付-系统异常",e);
		}
		
	}

	/**
	 * 处理APP支付宝支付后，支付宝异步通知处理逻辑
	 * @param transaction3rdDto
	 * @throws Exception
	 */
	public void handleClientAlipayNotify(Transaction3rdDto transaction3rdDto,Integer userBillType)
			throws Exception {
		Map<String,Object> resultMap = payServcie.nofity3rdPayStatus(transaction3rdDto,userBillType);
		
		//推送消息
		if(resultMap != null && resultMap.size() > 0){
			pushOrder(resultMap.get("orderId")+"", Long.parseLong(resultMap.get("userId")+""), resultMap);
		}
		
	}

    /**
     * 商铺充值
     * 建设银行支付接口 样例： https://ibsbjstar.ccb.com.cn/app/ccbMain?
     * MERCHANTID=105320148140002 商户代码 &POSID=100001135 柜台代码 &BRANCHID=320000000
     * &ORDERID=88487 &PAYMENT=0.01 &CURCODE=01 &TXCODE=520100 &REMARK1=
     * &REMARK2= &TYPE=1 &GATEWAY= &CLIENTIP=128.128.80.125 &REGINFO=xiaofeixia
     * &PROINFO=digital &REFERER=
     * &MAC=b2a1adfc9f9a44b57731440e31710740前面urlMD5加密串
     * @param request
     * @param attre
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/serverCommunicate/ccb3rd")
    @ResponseBody
    public ResultDto ccb3rd(HttpServletRequest request)
    {
        try
        {
            logger.info("建设银行支付-start");
            // 建设银行卡类型 1借记卡或2贷记卡
            String posType = RequestUtils.getQueryParam(request, "posType");
            CommonValidUtil.validStrNull(posType, CodeConst.CODE_PARAMETER_NOT_EXIST, "建设银行支付类型不能为空");

            // 支付金额
            String paymentStr = RequestUtils.getQueryParam(request, "payment");
            CommonValidUtil.validStrNull(paymentStr, CodeConst.CODE_PARAMETER_NOT_EXIST, "支付金额不能为空");
            Double payment = NumberUtil.strToDouble(paymentStr, "支付金额");

            // 充值类型：user为用户充值，shop为商铺充值,order为用户消费
            String subject = RequestUtils.getQueryParam(request, "subject");
            CommonValidUtil.validStrNull(subject, CodeConst.CODE_PARAMETER_NOT_EXIST, "充值类型不能为空");

            // id，可能为shopId和userI、orderId
            String id = RequestUtils.getQueryParam(request, "id");
            CommonValidUtil.validStrNull(subject, CodeConst.CODE_PARAMETER_NOT_EXIST, "id不能为空");

            // remark1存放shopid或者userid；remark2存放类别用来区分remark1到底是userId、shopId
            String remark1 = "";
            //
            TransactionDto transactionDto = new TransactionDto();
            // 验证商铺，商铺充值
            if (StringUtils.equals(CommonConst.SHOP_TYPE, subject))
            {
                Long shopId = CommonValidUtil.validStrLongFmt(id, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_SHOPID);
                int flag = this.shopServcie.queryShopExists(shopId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_SHOP); // 商铺充值
                transactionDto.setUserId(Long.valueOf(id));// 商铺id
                transactionDto.setTerminalType(CommonConst.TERMINAL_TYPE_PHP);
                remark1 = id;
            }
            // 验证用户，用户充值
            if (StringUtils.equals(CommonConst.USER_TYPE, subject))
            {
                Long userId = NumberUtil.strToLong(id, "userId");
                UserDto userDB = memberService.getUserByUserId(userId);
                CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
                transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_USER); // 用户充值
                transactionDto.setUserId(Long.valueOf(id));// 用户id
                transactionDto.setTerminalType(CommonConst.TERMINAL_TYPE_ANDROID);
                remark1 = id;
            }
            // 订单，用户消费
            if (StringUtils.equals(CommonConst.ORDER_TYPE, subject))
            {
                // 订单ID不能为空
                CommonValidUtil.validStrNull(id, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_ORDERID);
                OrderDto order = orderServcie.getOrderMainById(id);
                CommonValidUtil.validObjectNull(order,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_NOT_EXIST);
                transactionDto.setOrderId(id);// 订单id，用户消费时需要保存该字段
                transactionDto.setUserId(order.getUserId());// 用户id
                transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_ORDER); // 订单
                transactionDto.setTerminalType(CommonConst.TERMINAL_TYPE_ANDROID);

            }

            // 生成交易流水
            transactionDto.setStatus(CommonConst.TRANSACTION_STATUS_WAIT); // 待支付
            transactionDto.setPayAmount(payment);
            transactionDto.setRdOrgName(CommonConst.CCB_NAME);
            transactionDto.setTransactionTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
            transactionDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
            transactionDto.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE); // 订单支付类型：0(单个订单支付），1（多个订单支付）
            payServcie.addTransaction(transactionDto);
            

            long transactionId = transactionDto.getTransactionId();
            // 生成建设银行流水号
            String orderId = FieldGenerateUtil.generatebitOrderId(transactionId);
            
            //需要保存建行流水号，方便后续对账
            transactionDto.setRdTransactionId(orderId);
            transactionDto.setOrderId(orderId);
            payServcie.updateTransactionAfterRdPaySuccess(transactionDto);
            
            logger.info("订单id为：" + orderId + ",交易流水ID为：" + transactionId);

            /******************* 以下组装请求url **********************/
            // 建行请求url前缀
            StringBuffer ccbRequestUrl = new StringBuffer();

            // 商户代码
            ccbRequestUrl.append("MERCHANTID=" + CommonConst.MERCHANTID);

            // 商户柜台代码， 根据类型选择银行卡类型,默认借记卡
            String pubKey = "";
            // 贷记卡
            if (StringUtils.equals(CommonConst.POS_TYPE_CREDIT_CARD, posType))
            {
                // 贷记卡公钥
                pubKey = CommonConst.CREDIT_CARD_PUBLIC_KEY;
                ccbRequestUrl.append("&POSID=" + CommonConst.POSID_CREDIT_CARD);
            }
            // 借记卡
            if (StringUtils.equals(CommonConst.POS_TYPE_DEBIT_CARD, posType))
            {
                // 借记卡公钥
                pubKey = CommonConst.DEBIT_CARD_PUBLIC_KEY;
                ccbRequestUrl.append("&POSID=" + CommonConst.POSID_DEBIT_CARD);
            }
            // B2B
            if (StringUtils.equals(CommonConst.POS_TYPE_B2B, posType))
            {
                ccbRequestUrl.append("&POSID=" + CommonConst.POSID_B2B);
            }
            // 支行代码
            ccbRequestUrl.append("&BRANCHID=" + CommonConst.BRANCHID);
            // 订单号ORDERID
            ccbRequestUrl.append("&ORDERID=" + orderId);
            // PAYMENT 支付金额
            ccbRequestUrl.append("&PAYMENT=" + payment);
            // 币种
            ccbRequestUrl.append("&CURCODE=" + CommonConst.CURCODE);
            // 交易码
            ccbRequestUrl.append("&TXCODE=" + CommonConst.TXCODE);
            // 备注1 存放shopId、userId
            ccbRequestUrl.append("&REMARK1=" + remark1);
            // 备注2 存放充值类型，user为用户充值，shop为商铺充值
            ccbRequestUrl.append("&REMARK2=" + subject);
            // 接口类型：防钓鱼接口
            ccbRequestUrl.append("&TYPE=" + CommonConst.TYPE);
            // 公钥
            ccbRequestUrl.append("&PUB=" + pubKey);
            // 网关类型（默认选择=》 网关类型 ：W0Z1或W0Z2仅显示帐号支付标签）
            ccbRequestUrl.append("&GATEWAY=" + CommonConst.GATEWAY);
            // 客户ip地址
            String ipAdress = RequestUtils.getIpAddr(request);
            ccbRequestUrl.append("&CLIENTIP=");// TODO 目前写死测试
            // 客户注册信息 TODO 目前常量写死,后面如果需要再进行转码
            // String valueStr = new String(valueStr.getBytes("ISO-8859-1"),
            // "utf-8");
            ccbRequestUrl.append("&REGINFO=" + CommonConst.REGINFO);
            // 商铺信息 TODO 目前常量写死
            ccbRequestUrl.append("&PROINFO=" );
            // REFERER 商户送空值即可，银行从后台读取商户设置的一级域名
            ccbRequestUrl.append("&REFERER=");
            // 最后之前的拼接URL进行MD5机密，作为最后一个参数（MAC）的value值
            String ccbRequestStr = ccbRequestUrl.toString();
            String macVlues = MD5Util.getMD5Str(ccbRequestStr);
            ccbRequestUrl.append("&MAC=" + macVlues);
            // PUB参数为非传递参数
            String ccbRequestNewStr = CommonConst.CCB_REQUEST_URL
                    + ccbRequestUrl.toString().replace("&PUB=" + pubKey, "");
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("url", ccbRequestNewStr);
            resultMap.put("transactionId", transactionId);

            // TODO 写业务逻辑 1、回调之前，只往交易表记录信息 2、回调成功
            logger.info("建设银行支付-end");
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "建设银行生成交易流水成功", resultMap);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            this.logger.error("建设银行支付-系统异常", e);
            throw new APISystemException("建设银行支付-系统异常", e);
        }
    }
    
    /**
     * 建设银行回调接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/ccbCallBack")
    @ResponseBody
    public ResultDto ccbCallBack(HttpServletRequest request){
        
/*         * 商户通知串的格式如下：
         * POSID=000000000&BRANCHID=110000000&ORDERID=19991101234
         * &PAYMENT=500.00
         * &CURCODE=01
         * &REMARK1=
         * &REMARK2=
         * &ACC_TYPE=12
         * &SUCCESS=Y
         * &TYPE=1
         * &REFERER=http://www.ccb.com/index.jsp
         * &CLIENTIP=172.0.0.1
         * &ACCDATE=20100907
         * &USRMSG=T4NJx%2FVgocRsLyQnrMZLyuQQkFzMAxQjdqyzf6pM%2Fcg%3D
         * &SIGN=317b7dd349c1fbcabc26a20ba117a778da5a685c588be5e7378682651062a25b0885e36ee237c19a143f7271c9529a0e9bf198c8735709dc74233d96e1a276cec9d4835422bee597100b0a47d11b44dbba74bdf9cbde0587f138141ce79a3536733d5f6b53ed119c13708dca52ee8d3fcf7e67dcdb20053889adff1989a8c859
         * 
         * 参与签名运算的字符串及其顺序如下：
         * POSID=000000000
         * &BRANCHID=110000000
         * &ORDERID=19991101234
         * &PAYMENT=500.00
         * &CURCODE=01
         * &REMARK1=
         * &REMARK2=
         * &ACC_TYPE=12
         * &SUCCESS=Y
         * &TYPE=1
         * &REFERER=http://www.ccb.com/index.jsp
         * &CLIENTIP=172.0.0.1
         * &ACCDATE=20100907
         * &USRMSG=T4NJx%2FVgocRsLyQnrMZLyuQQkFzMAxQjdqyzf6pM%2Fcg%3D*/
         
        try
        {
            logger.info("建设银行回调接口-start");
            //商户柜台代码
            String posid = RequestUtils.getQueryParam(request, "POSID");
            //分行代码
            String branchid = RequestUtils.getQueryParam(request, "BRANCHID");
            //订单号
            String orderId = RequestUtils.getQueryParam(request, "ORDERID");
            //付款金额
            String paymentStr = RequestUtils.getQueryParam(request, "PAYMENT");
            //币种
            String curcode = RequestUtils.getQueryParam(request, "CURCODE");
            //备注一
            String remark1 = RequestUtils.getQueryParam(request, "REMARK1");
            //备注二
            String remark2 = RequestUtils.getQueryParam(request, "REMARK2");
            // 账户类型
            String accType = RequestUtils.getQueryParam(request, "ACC_TYPE");
            //成功标示
            String success = RequestUtils.getQueryParam(request, "SUCCESS");
            //接口类型
            String type = RequestUtils.getQueryParam(request, "TYPE");
            //Referer信息
            String referer = RequestUtils.getQueryParam(request, "REFERER");
            //客户端IP
            String clientip = RequestUtils.getQueryParam(request, "CLIENTIP");
            //系统记账日期
            String accdate = RequestUtils.getQueryParam(request, "ACCDATE");
            //支付账户信息
            String usrmsg = RequestUtils.getQueryParam(request, "USRMSG");
            logger.info("支付账户信息为："+usrmsg);
            // 数字签名
            String sign = RequestUtils.getQueryParam(request, "SIGN");
            
            /**验证关键信息**/
            // 支付金额
            CommonValidUtil.validStrNull(paymentStr, CodeConst.CODE_PARAMETER_NOT_EXIST, "支付金额不能为空");
            Double payment = NumberUtil.strToDouble(paymentStr, "支付金额");
            
            //测试日志打印
            logger.info("回调是否成功标示："+success);
            logger.info("数字签名："+sign);
            logger.info("账户类型："+accType);

            //公钥
            String pubKey = "";
            //用户账单logo类型
            Integer userBillType = CommonConst.USER_BILL_TYPE_CCB_DEBIT;
            //根据账号类型 取不同的公钥,30为贷记卡，其他类型暂时都默认借记卡
            if(StringUtils.equals(CommonConst.ACC_TYPE,accType))
            {
                //贷记卡公钥
                pubKey = CommonConst.CREDIT_CARD_PUBLIC_KEY_ALL;
                userBillType = CommonConst.USER_BILL_TYPE_CCB_CREDIT;
                logger.info("公钥类型为贷记卡公钥："+pubKey);
            }
            else
            {
                //借记卡公钥
                pubKey = CommonConst.DEBIT_CARD_PUBLIC_KEY_ALL;
                userBillType = CommonConst.USER_BILL_TYPE_CCB_DEBIT;
                logger.info("公钥类型为借记卡公钥："+pubKey);
            }
            
            /**
             * 组装交易记录流水信息
             * 1、remake1为shopId;
             * 2、transactionId为orderId截取时间戳剩余部分
            **/
            String transactionIdStr = orderId.substring(15);//15为时间戳长度yyMMddHHmmssSSS
            Long transactionId = Long.valueOf(transactionIdStr);
            Transaction3rdDto transaction3rd = new Transaction3rdDto();
            transaction3rd.setTransactionId(transactionId);
            transaction3rd.setRdTransactionId(orderId);
            transaction3rd.setPayAmount(payment);
            transaction3rd.setRdOrgName(CommonConst.CCB_NAME);
            transaction3rd.setTransactionTime(new Date());
            transaction3rd.setRdNotifyTime(new Date());
            transaction3rd.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE); // 订单支付类型：0(单个订单支付），1（多个订单支付）
            if(StringUtils.isNotBlank(remark1)){
                transaction3rd.setUserId(Long.valueOf(remark1));//shopId或者userId
            }
            //如果充值类型为商铺，走商铺充值分支
            if(StringUtils.equals(CommonConst.SHOP_TYPE, remark2))
            {
                transaction3rd.setTransactionType(CommonConst.TRANSACTION_TYPE_SHOP); // 商铺充值
                transaction3rd.setTerminalType(CommonConst.TERMINAL_TYPE_PHP);
            }
            //用户充值
            if(StringUtils.equals(CommonConst.USER_TYPE, remark2))
            {
                transaction3rd.setTransactionType(CommonConst.TRANSACTION_TYPE_USER); // 用户充值
                transaction3rd.setTerminalType(CommonConst.TERMINAL_TYPE_ANDROID);
            }
            //订单
            if(StringUtils.equals(CommonConst.ORDER_TYPE, remark2))
            {
                transaction3rd.setTransactionType(CommonConst.TRANSACTION_TYPE_ORDER); // 订单充值
                transaction3rd.setTerminalType(CommonConst.TERMINAL_TYPE_ANDROID);
            }
            // 银行返回成功
            if (StringUtils.equals(success, CommonConst.SUCCESS_TYPE))
            {
                // 参与签名运算的字符串及其顺序
                StringBuffer srcStr = new StringBuffer();
                srcStr.append("POSID=" + posid);
                srcStr.append("&BRANCHID=" + branchid);
                srcStr.append("&ORDERID=" + orderId);
                srcStr.append("&PAYMENT=" + payment);
                srcStr.append("&CURCODE=" + curcode);
                srcStr.append("&REMARK1=" + remark1);
                srcStr.append("&REMARK2=" + remark2);
                // 仅服务器通知中有此字段返回且参与验签，页面通知无此字段返回且不参与验签。
                if (StringUtils.isNotBlank(accType))
                {
                    srcStr.append("&ACC_TYPE=" + accType);
                }
                srcStr.append("&SUCCESS=" + success);
                srcStr.append("&TYPE=" + type);
                srcStr.append("&REFERER=" + referer);
                srcStr.append("&CLIENTIP="+clientip );
                srcStr.append("&ACCDATE=" + accdate);
                // 业务人员在ECTIP后台设置返回账户信息的开关且支付成功时,将返回账户加密信息且该字段参与验签，否则无此字段返回且不参与验签，格式如下：“姓名|账号”
                if (StringUtils.isNotBlank(usrmsg))
                {
                    srcStr.append("&USRMSG=" + usrmsg);
                }
                
                logger.info("加密串：" + srcStr);
                
                // 验证签名
                RSASig rsa = new RSASig();
                // pkey 商户的公钥
                rsa.setPublicKey(pubKey);


                // 签名前的源串,例如：POSID=000000&BRANCHID=110000000&ORDERID=00320995&PAYMENT=0.01&CURCODE=01&REMARK1=test1&REMARK2=test2&SUCCESS=Y
                boolean verfySign = rsa.verifySigature(sign, srcStr.toString());
                
                // 验证成功
                if (verfySign)
                {
                    logger.info("加密串验证成功");
                    transaction3rd.setStatus(CommonConst.TRANSACTION_STATUS_FINISH); // 支付成功
                    //如果充值类型为商铺，走商铺充值分支
                    if(StringUtils.equals(CommonConst.SHOP_TYPE, remark2))
                    {
                        logger.info("进入商铺充值入口");
                        payServcie.handleAlipayBack(transaction3rd);
                    }
                    //用户充值或者订单消费走同一个流程
                    if(StringUtils.equals(CommonConst.USER_TYPE, remark2)
                            || StringUtils.equals(CommonConst.ORDER_TYPE, remark2))
                    {
                        logger.info("进入用户充值或者订单入口");
                        handleClientAlipayNotify(transaction3rd,userBillType);
                    }
                    
                    
                }
                else
                {// 失败
                    logger.info("加密串验证失败");
                  //更新交易状态为失败
                    payServcie.updateTransactionByid(transactionId,orderId);
                }
            }
            else
            {// 返回失败
                logger.error("银行返回结果：失败");
                transaction3rd.setStatus(CommonConst.TRANSACTION_STATUS_FAIL); // 支付失败
               //更新交易状态为失败
                payServcie.updateTransactionByid(transactionId,orderId);
            }
            logger.info("建设银行回调接口-end");
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);

        }
        catch (Exception e)
        {
            logger.error("建设银行回调接口-系统异常", e);

        }
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "建设银行回调成功", null);
    }
	
	/**
	 * 查询第三方支付信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/getMy3rdPay",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getMy3rdPay(HttpServletRequest request){
		try {
//			pNo	int	1	否	页码
//			pSize	int	10	否	每页记录数
//			userId	int		是	用户Id
//			status	int	0	是	交易状态：
//			0：待反馈支付进度
//			1：支付成功
//			2：支付失败
//			3rdOrgName	String		否	第三方支付平台的名称
			logger.info("查询我的第三方支付列表-start");
			String userId = RequestUtils.getQueryParam(request, "userId");
			String status = RequestUtils.getQueryParam(request, "status");
			String rdOrgName = RequestUtils.getQueryParam(request, "3rdOrgName");
			String pageNo = RequestUtils.getQueryParam(request, "pNo");
			String pageSize = RequestUtils.getQueryParam(request, "pSize");
			if(null==pageNo || "".equals(pageNo)){
				pageNo="1";
			}
			if(null==pageSize || "".equals(pageSize)){
				pageSize="10";
			}
			if(StringUtils.isEmpty(status)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_RDORG_NAME_IS_NULL);
			}
			//验证userid、pNO、pSize
			verifyParameter(userId, pageNo, pageSize);
			Integer  count = payServcie.getMy3rdPayCount(Long.parseLong(userId),Integer.parseInt(status) , 
					rdOrgName,Integer.parseInt(pageNo), Integer.parseInt(pageSize));
			List<Map<String, Object>> list = payServcie.getMy3rdPay(Long.parseLong(userId),Integer.parseInt(status) , 
					rdOrgName,Integer.parseInt(pageNo), Integer.parseInt(pageSize));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("count",count);
			map.put("lst", list);
			map.put("pNo", pageNo);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,CodeConst.MSG_PAY_LIST_SUCCESS,map,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查询我的第三方支付列表-系统异常",e);
			throw new APISystemException("查询我的第三方支付列表-系统异常", e);
		}
	}	
	/**
	 * 第三方支付交易状态反馈
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/nofity3rdPayStatus",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object nofity3rdPayStatus(HttpServletRequest request){
		try {
			logger.info("第三方支付交易状态反馈-start");
			 
/*				transactionId	long		是	1点传奇平台生成的交易流水号
				3rdTransactionId	string		是	第三方支付平台中的交易流水号
				successFlag	int		是	交易是否成功的标记 -->1：交易成功 0：交易失败
				notifyTime	datetime		否	第三方支付平台异步通知的时间
				notifyId	String		否	第三方支付平台异步通知的唯一标识*/
			
			String transactionId = RequestUtils.getQueryParam(request, "transactionId");
			String rdTransactionId = RequestUtils.getQueryParam(request, "3rdTransactionId");
			String notifyId = RequestUtils.getQueryParam(request, "notifyId");
			String successFlag = RequestUtils.getQueryParam(request, "successFlag");
			if(StringUtils.isEmpty(rdTransactionId)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_TRANSACTIONID_IS_NULL);
			}
			if (null==transactionId) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_3RD_TRANSACTIONID_IS_NULL);
			}
			//状态
			if(null==successFlag){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_STATUS_IS_NULL);
			}
			if(!NumberUtil.isNumeric(successFlag)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_STATUS_FORMAT_ERROR);
			}
			//验证一点传奇交易流水号
			NumberUtil.isLong("transactionId", transactionId);
			Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
			transaction3rdDto.setRdTransactionId(rdTransactionId);
			transaction3rdDto.setRdNotifyId(notifyId);
			transaction3rdDto.setTransactionId(Long.parseLong(transactionId));
			transaction3rdDto.setStatus(Integer.parseInt(successFlag));
			handleClientAlipayNotify(transaction3rdDto,CommonConst.USER_BILL_TYPE_ALIPAY);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,CodeConst.MSG_NOFITY_3RDPAY_STATUS_SUCCESS,null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("第三方支付交易状态反馈-系统异常",e);
			throw new APISystemException("第三方支付交易状态反馈-系统异常", e);
		}			

	}
	/**
	 * 用户提现
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/withdraw",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object withdraw(HttpServletRequest request){
		try {
			logger.info("用户提现-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			//用户名称
			String userName = RequestUtils.getQueryParam(request, "userName");
			//金额
			String amount = RequestUtils.getQueryParam(request, "amount");
			//发起交易的终端类型，必填
			String terminalType = RequestUtils.getQueryParam(request, "terminalType");
			//银行名称
			String bankName = RequestUtils.getQueryParam(request, "bankName");
			//支付密码的MD5加密串
			String payPassword = RequestUtils.getQueryParam(request, "payPassword");
			//支行名称
			//String bankSubbranchName = RequestUtils.getQueryParam(request, "bankSubbranchName");
			//卡号
			String cardNumber = RequestUtils.getQueryParam(request, "cardNumber");
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_REQUIRED_MEMBER);
			//userId参数不合法
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_EXIST, "userName不能为空");
			CommonValidUtil.validStrNull(amount, CodeConst.CODE_PARAMETER_NOT_EXIST, "amount不能为空");
			CommonValidUtil.validStrNull(terminalType, CodeConst.CODE_PARAMETER_NOT_EXIST, "terminalType不能为空");
			CommonValidUtil.validStrNull(bankName, CodeConst.CODE_PARAMETER_NOT_EXIST, "bankName不能为空");
			CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR,amount);
			
			UserDto user = memberServcie.getUserByUserId(userId);
			if(null==user){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			
			// 用户账号状态异常不能提现
			if(!UserStatusEnum.NORMAL.getValue().equals(user.getStatus())){
				throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
			}
			//验证支付密码
			if(!StringUtils.equals(user.getPayPassword(), payPassword)) {
				throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
			}
			
			WithdrawDto withdrawDto = new WithdrawDto();
			withdrawDto.setAmount(Double.parseDouble(amount));
			withdrawDto.setUserId(userId);
			withdrawDto.setUserName(userName);
			withdrawDto.setTerminalType(terminalType);
			withdrawDto.setBankName(bankName);
			withdrawDto.setCardNumber(cardNumber);
			withdrawDto.setMobile(user.getMobile());
			//默认为未审核状态
			withdrawDto.setWithdrawStatus(0);
			//用户类型  2015.6.5 用于判断店铺保障金
			withdrawDto.setUserType(user.getUserType());
			Long  withdrawId = payServcie.withdrawByUser(withdrawDto);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("withdrawId", withdrawId);
			
			//推送用户 
			sendToUser(withdrawDto);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_WITHDRAW_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("用户提现-系统异常",e);
			throw new APISystemException("用户提现-系统异常", e);
		}
	}

	/**
	 * 推送提现信息给用户
	 * @param withdrawDto
	 */
	private void sendToUser(WithdrawDto withdrawDto) {
		try {
			StringBuilder content = new StringBuilder();
			// 待审核
			if (withdrawDto.getWithdrawStatus() == 0) { 
				content.append("亲爱的用户，您于")
						.append(DateUtils.format(new Date(), null))
						.append("提现金额")
						.append(withdrawDto.getAmount())
						.append("元至")
						.append(withdrawDto.getBankName())
						.append("卡（卡号：")
						.append(NumberUtil.deailCarNumber(withdrawDto
								.getCardNumber()))
						.append("已在审核中，请耐心等待。祝您工作顺心，生活愉快！");
			// 提现成功
			} else if (withdrawDto.getWithdrawStatus() == 4) {
				content.append("亲爱的用户，您已于")
						.append(DateUtils.format(withdrawDto.getHandleTime(),
								null))
						.append("成功提现金额")
						.append(withdrawDto.getAmount())
						.append("元至")
						.append(withdrawDto.getBankName())
						.append("卡（卡号：")
						.append(NumberUtil.deailCarNumber(withdrawDto
								.getCardNumber())).append("。祝您工作顺心，生活愉快！");
			}
			commonService.pushUserMsg("withdraw", content.toString(),
					withdrawDto.getUserId(), CommonConst.USER_TYPE_ZREO);
		} catch (Exception e) {
			logger.error("推送提现信息异常", e);
		}
	}

	/**
	 * 用户充值接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/charge",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object charge(HttpServletRequest request) {
		logger.info("用户充值-start");
		try {
			
			Map<String, Object> map = userBillService.saveCharge(request);
			try {
				//消息推送，不影响整体流程
				JSONObject jsonObject =(JSONObject) map.get("pushJson");
				Long userId = jsonObject.getLong("userId");
				int type = jsonObject.getInt("chargeType");
				if (type == 2) {
					//{"action":"umcCharge","shopId":"32342343","umcId":"12345678","amount":99.99}
					//会员卡支付
					jsonObject.put("action", "umcCharge");
				}else{
					//传奇宝支付
					jsonObject.put("action", "charge");
				}
				jsonObject.remove("userId");
				jsonObject.remove("chargeType");
				commonService.pushUserMsg(jsonObject, userId, CommonConst.USER_TYPE_ZREO);
			} catch (Exception e) {
				logger.error("消息推送异常",e);
			}
			map.remove("pushJson");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,CodeConst.MSG_CHARGE_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("充值失败-系统异常",e);
			throw new APISystemException("充值失败-系统异常", e);
		}
	}
	/**
	 * 商铺充值接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/charge",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object serverCommunicateCharge(HttpServletRequest request) {
		logger.info("用户充值-start");
		try {
			
			Map<String, Object> map = userBillService.saveCharge(request);
			try {
				//消息推送，不影响整体流程
				JSONObject jsonObject =(JSONObject) map.get("pushJson");
				Long userId = jsonObject.getLong("userId");
				int type = jsonObject.getInt("chargeType");
				if (type == 2) {
					//{"action":"umcCharge","shopId":"32342343","umcId":"12345678","amount":99.99}
					//会员卡支付
					jsonObject.put("action", "umcCharge");
				}else{
					//传奇宝支付
					jsonObject.put("action", "charge");
				}
				jsonObject.remove("userId");
				jsonObject.remove("chargeType");
				commonService.pushUserMsg(jsonObject, userId, CommonConst.USER_TYPE_ZREO);
			} catch (Exception e) {
				logger.error("消息推送异常",e);
			}
			map.remove("pushJson");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,CodeConst.MSG_CHARGE_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("充值失败-系统异常",e);
			throw new APISystemException("充值失败-系统异常", e);
		}
	}
	
	/**
	 * 
	* @Title: getBanks 
	* @Description: (查询银行接口列表) 
	* @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="pay/getBanks",produces="application/json;charset=UTF-8")
	public @ResponseBody String getBanks(HttpServletRequest request)
	{
		try{
			String pNoStr=request.getParameter("pNo");
			String pSizeStr=request.getParameter("pSize");
			PageModel pageModel=new PageModel();
			pageModel=bankService.getBankList(PageModel.handPageNo(pNoStr),PageModel.handPageSize(pSizeStr));
			convertBankUrl(pageModel);
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,"获取银行信息成功！", msgList,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取银行列表错误",e);
			throw new APISystemException("获取银行列表产生了异常", e);
		}
	}
	
	/**
	 * 将银行Url进行拼接
	* @Title: convertBankUrl 
	* @param @param pageModel
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	private  void convertBankUrl(PageModel pageModel)throws Exception
	{
		List<BankDto>bankList=(List<BankDto>)pageModel.getList();
		if(bankList!=null)
		{
			for(BankDto bankDto:bankList)
			{
				if(!StringUtils.isEmpty(bankDto.getBankLogoUrl()))
				{
					bankDto.setBankLogoUrl(FdfsUtil.getFileProxyPath(bankDto.getBankLogoUrl()));
				}
			}
		}
	}
	/**
	 * 
	* @Title: getBanks 
	* @Description: (查询银行接口列表) 
	* @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="pay/cancelTransaction",produces="application/json;charset=UTF-8")
	public @ResponseBody String cancelTransaction(HttpServletRequest request)
	{
		try{
			String transactionIdStr = RequestUtils.getQueryParam(request, "transactionId");
			CommonValidUtil.validStrNull(transactionIdStr, CodeConst.CODE_PARAMETER_NOT_EXIST, "transactionId不能为空");
			 if(transactionIdStr.length() > 15)
             {
                 transactionIdStr = transactionIdStr.substring(15);//15为时间戳长度yyMMddHHmmssSSS
             }
			Long transactionId=NumberUtil.strToLong(transactionIdStr,"transactionId");
			payServcie.updateTransactionByid(transactionId,null);
			
			//更新用户账单为“充值失败”
//			payServcie.cancelRecharge(transactionId);
			
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,"交易取消成功！",null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("交易取消失败",e);
			throw new APISystemException("交易取消异常", e);
		}
	}
	/**
	 * 商铺提现
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/withdraw",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object communicateWithdraw(HttpServletRequest request){
		try {
			logger.info("商铺提现-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			//用户名称
			String userName = RequestUtils.getQueryParam(request, "userName");
			//金额
			String amount = RequestUtils.getQueryParam(request, "amount");
			//发起交易的终端类型，必填
			String terminalType = RequestUtils.getQueryParam(request, "terminalType");
			//银行名称
			String bankName = RequestUtils.getQueryParam(request, "bankName");
			//银行名称
			String payPassword = RequestUtils.getQueryParam(request, "payPassword");
			//支行名称
			//String bankSubbranchName = RequestUtils.getQueryParam(request, "bankSubbranchName");
			//卡号
			String cardNumber = RequestUtils.getQueryParam(request, "cardNumber");
			
			String onlineIncomeAmount = RequestUtils.getQueryParam(request, "onlineIncomeAmount"); // 从在线营业收入提现金额
			String rewardAmount = RequestUtils.getQueryParam(request, "rewardAmount"); // 从平台奖励提现金额
			String withdrawCommissionStr = RequestUtils.getQueryParam(request, "withdrawCommission"); // 手续费

			
			CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_EXIST, "shopId不能为空");
			//shopId参数不合法
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			//CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_EXIST, "userName不能为空");
			CommonValidUtil.validStrNull(amount, CodeConst.CODE_PARAMETER_NOT_EXIST, "amount不能为空");
			CommonValidUtil.validStrNull(terminalType, CodeConst.CODE_PARAMETER_NOT_EXIST, "terminalType不能为空");
			CommonValidUtil.validStrNull(cardNumber, CodeConst.CODE_PARAMETER_NOT_EXIST, "cardNumber不能为空");
			CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			Double withdrawCommission  = Double.valueOf(0);
			if(StringUtils.isNotEmpty(withdrawCommissionStr)){
				withdrawCommission = NumberUtil.strToDouble(withdrawCommissionStr, "withdrawCommission");
			}
			String dbPassWord = shopServcie.getShopPasswordById(shopId);
			//验证支付密码
			if(!StringUtils.equals(dbPassWord, payPassword)) {
				throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
			}
			
				
			NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR,amount);
			
			// 校验提现金额是否和预期相等
			verifyAmount(Double.parseDouble(amount),onlineIncomeAmount,rewardAmount);
			
			
			
			ShopWithDrawDto shopWithDrawDto = new ShopWithDrawDto();
			shopWithDrawDto.setAmount(Double.parseDouble(amount));
			shopWithDrawDto.setShopId(shopId);
			shopWithDrawDto.setUserName(userName);
			shopWithDrawDto.setTerminalType(terminalType);
			shopWithDrawDto.setBankName(bankName);
			shopWithDrawDto.setCardNumber(cardNumber);
			shopWithDrawDto.setWithdrawCommission(withdrawCommission);
			//默认为未审核状态
			shopWithDrawDto.setWithdrawStatus(0);
			// 2015.12.1 add by huangrui
			if(NumberUtil.isDouble(onlineIncomeAmount)){
				shopWithDrawDto.setOnlineIncomeFreeze(Double.parseDouble(onlineIncomeAmount));
			}
			if(NumberUtil.isDouble(rewardAmount)){
				shopWithDrawDto.setRewardFreeze(Double.parseDouble(rewardAmount));
			}
			
			Map<String, Object> map = payServcie.withdrawByShop(shopWithDrawDto);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_WITHDRAW_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("商铺提现-系统异常",e);
			throw new APISystemException("商铺提现-系统异常", e);
		}
	}
	
	
	private void verifyAmount(double amount, String onlineIncomeAmount,
			String rewardAmount) {
		
		double needAmount = 0; // 用户设置的金额
		boolean isNeedVerify = false; // 是否需要校验
		if(StringUtils.isNotEmpty(onlineIncomeAmount) && NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR, onlineIncomeAmount)){
			isNeedVerify = true;
			needAmount = NumberUtil.add(needAmount,Double.parseDouble(onlineIncomeAmount));
		}
		if(StringUtils.isNotEmpty(rewardAmount) && NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR, rewardAmount)){
			isNeedVerify = true;
			needAmount = NumberUtil.add(needAmount,Double.parseDouble(rewardAmount));
		}
		
		if(isNeedVerify && needAmount != amount){
		    throw new ValidateException(CodeConst.CODE_MONEY_NOT_EQUAIL, "资金之间金额关系错误");
		}
	}

	/**
	 * 用户提现
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/userWithdraw",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object userWithdraw(HttpServletRequest request){
		try {
			logger.info("用户提现-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			//用户名称
			String userName = RequestUtils.getQueryParam(request, "userName");
			//金额
			String amount = RequestUtils.getQueryParam(request, "amount");
			//发起交易的终端类型，必填
			String terminalType = RequestUtils.getQueryParam(request, "terminalType");
			//银行名称
			String bankName = RequestUtils.getQueryParam(request, "bankName");
			//支付密码
			String payPassword = RequestUtils.getQueryParam(request, "payPassword");
			//支行名称
			//String bankSubbranchName = RequestUtils.getQueryParam(request, "bankSubbranchName");
			//卡号
			String cardNumber = RequestUtils.getQueryParam(request, "cardNumber");
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_EXIST, "userId不能为空");
			//shopId参数不合法
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_EXIST, "userName不能为空");
			CommonValidUtil.validStrNull(amount, CodeConst.CODE_PARAMETER_NOT_EXIST, "amount不能为空");
			NumberUtil.isDouble(CodeConst.MSG_PAYAMOUNT_FORMAT_ERROR,amount);
			CommonValidUtil.validStrNull(terminalType, CodeConst.CODE_PARAMETER_NOT_EXIST, "terminalType不能为空");
			CommonValidUtil.validStrNull(bankName, CodeConst.CODE_PARAMETER_NOT_EXIST, "bankName不能为空");
			CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYPWD);
			
			UserDto user = memberServcie.getUserByUserId(userId);
			if(null==user){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			//验证支付密码
			if(!StringUtils.equals(user.getPayPassword(), payPassword)) {
				throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
			}
		
			WithdrawDto withdrawDto = new WithdrawDto();
			withdrawDto.setAmount(Double.parseDouble(amount));
			withdrawDto.setUserId(userId);
			withdrawDto.setUserName(userName);
			withdrawDto.setTerminalType(terminalType);
			withdrawDto.setBankName(bankName);
			withdrawDto.setCardNumber(cardNumber);
			withdrawDto.setMobile(user.getMobile());
			//默认为未审核状态
			withdrawDto.setWithdrawStatus(0);
			//用户类型 2015.6.5 用于判断店铺保障金
			withdrawDto.setUserType(user.getUserType());
			Long  withdrawId = payServcie.withdrawByUser(withdrawDto);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("withdrawId", withdrawId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_WITHDRAW_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("用户提现-系统异常",e);
			throw new APISystemException("用户提现-系统异常", e);
		}
	}
	
	/**
	 * 服务端获取提现记录列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/getWithdrawList",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getWithdrawList(HttpServletRequest request){
		try {
			logger.info("获取后台服务提现记录列表-start");
			String withdrawStatusStr = RequestUtils.getQueryParam(request, "withdrawStatus");
			String pageNO = RequestUtils.getQueryParam(request, "pNo");
			String pageSize = RequestUtils.getQueryParam(request, "pSize");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			Long userId = null;
			Integer withdrawStatus = null;
			if(StringUtils.isNotBlank(withdrawStatusStr)){
				withdrawStatus = NumberUtil.strToNum(withdrawStatusStr, "withdrawStatusStr");
			}
			if(StringUtils.isNotBlank(withdrawStatusStr)){
				userId = NumberUtil.strToLong(userIdStr, "userId");
				UserDto user = memberServcie.getUserByUserId(userId);
				CommonValidUtil.validObjectNull(user,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
			PageModel pageModel = this.payServcie.
					getServiceWithdrawList(withdrawStatus, userId, mobile, PageModel.handPageNo(pageNO),PageModel.handPageSize(pageSize));
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SEARCH_WITHDRAW_SUCCESS, msgList, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取后台服务提现记录列表-系统异常", e);
			throw new APISystemException("获取后台服务提现记录列表-系统异常", e);
		}
	}


	/**
	 * 管理员处理商铺提现接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/adminWithdraw", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object adminWithdraw(HttpServletRequest request){
		try {
			logger.info("管理员处理商铺提现接口 - start");
			
			String withdrawId = RequestUtils.getQueryParam(request, "withdrawId"); //提现流水号
			String withdrawStatus = RequestUtils.getQueryParam(request, "withdrawStatus"); 
			String handleUserId = RequestUtils.getQueryParam(request, "handleUserId");
			String handleTime = RequestUtils.getQueryParam(request, "handleTime");
			String handleMark = RequestUtils.getQueryParam(request, "handleMark");
			String withdrawTime = RequestUtils.getQueryParam(request, "withdrawTime");
			
			//校验
			CommonValidUtil.validStrNull(withdrawId,  CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawId不能为空");
			CommonValidUtil.validStrNull(withdrawStatus, CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawStatus不能为空");
			CommonValidUtil.validStrNull(handleUserId, CodeConst.CODE_PARAMETER_NOT_NULL, "handleUserId不能为空");
			CommonValidUtil.validStrNull(handleTime, CodeConst.CODE_PARAMETER_NOT_NULL, "handleTime不能为空");
			
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("withdrawId", withdrawId);
			paramMap.put("withdrawStatus", withdrawStatus);
			paramMap.put("handleUserId", handleUserId);
			paramMap.put("handleTime", handleTime);
			paramMap.put("handleMark", handleMark);
			paramMap.put("withdrawTime", withdrawTime);
			
			ShopWithDrawDto withdrawDto =payServcie.adminWithdraw(paramMap);
			//给用户推送消息（商铺不推送）
			//sendToUser(withdrawDto);
			
			logger.info("管理员处理商铺提现接口 - end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "处理提现成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		}
		catch (Exception e) {
			logger.error("管理员处理商铺提现接口 - 系统异常", e);
			throw new APISystemException("管理员处理商铺提现接口 - 系统异常", e);
		}
	}
	
	
	/**
	 * 管理员处理用户提现接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/serverCommunicate/adminWithdrawForUser", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object adminWithdrawForUser(HttpServletRequest request){
		try {
			logger.info("管理员处理用户提现接口 - start");
			
			String withdrawId = RequestUtils.getQueryParam(request, "withdrawId"); //提现流水号
			String withdrawStatus = RequestUtils.getQueryParam(request, "withdrawStatus"); 
			String handleUserId = RequestUtils.getQueryParam(request, "handleUserId");
			String handleTime = RequestUtils.getQueryParam(request, "handleTime");
			String handleMark = RequestUtils.getQueryParam(request, "handleMark");
			String withdrawTime = RequestUtils.getQueryParam(request, "withdrawTime");
			
			//校验
			CommonValidUtil.validStrNull(withdrawId,  CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawId不能为空");
			CommonValidUtil.validStrNull(withdrawStatus, CodeConst.CODE_PARAMETER_NOT_NULL, "withdrawStatus不能为空");
			CommonValidUtil.validStrNull(handleUserId, CodeConst.CODE_PARAMETER_NOT_NULL, "handleUserId不能为空");
			CommonValidUtil.validStrNull(handleTime, CodeConst.CODE_PARAMETER_NOT_NULL, "handleTime不能为空");
			
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("withdrawId", withdrawId);
			paramMap.put("withdrawStatus", withdrawStatus);
			paramMap.put("handleUserId", handleUserId);
			paramMap.put("handleTime", handleTime);
			paramMap.put("handleMark", handleMark);
			paramMap.put("withdrawTime", withdrawTime);
			
			payServcie.adminWithdrawForUser(paramMap);
			logger.info("管理员处理用户提现接口 - end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "处理提现成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		}
		catch (Exception e) {
			logger.error("管理员处理用户提现接口 - 系统异常", e);
			throw new APISystemException("管理员处理用户提现接口 - 系统异常", e);
		}
	}
	
	/**
	 *  P40：查询订单支付详情接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/getOrderPayDetail",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getOrderPayDetail(HttpServletRequest request){
		try {
			logger.info("查询订单支付详情接口-start");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			CommonValidUtil.validStrNull(orderId,  CodeConst.CODE_PARAMETER_NOT_NULL, "orderId不能为空");
			
			List<Map> list= this.payServcie.getOrderPayDetail(orderId);
					
			ResultListToClient msgList = new ResultListToClient();
			msgList.setLst(list);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询订单支付详情成功！", msgList, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询订单支付详情接口-系统异常", e);
			throw new APISystemException("查询订单支付详情接口-系统异常", e);
		}
	}
    /**
     *  查询交易流水接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value="/pay/getPayResult",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object ge3rdPayById(HttpServletRequest request){
        try {
            logger.info("查询交易流水接口-start");
            String transactionIdStr = RequestUtils.getQueryParam(request, "transactionId");
            CommonValidUtil.validStrNull(transactionIdStr,  CodeConst.CODE_PARAMETER_NOT_NULL, "transactionId不能为空");
            Long transactionId = NumberUtil.strToLong( transactionIdStr,"transactionId");
            
            Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
            transaction3rdDto.setTransactionId(transactionId);
            
            transaction3rdDto = this.payServcie.ge3rdPayById(transaction3rdDto);
                    
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询交易流水接口", transaction3rdDto, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询交易流水接口-系统异常", e);
            throw new APISystemException("查询交易流水接口-系统异常", e);
        }
    }
    
    /**
     *  查询交易流水接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value={"/session/pay/getPayResult","/token/pay/getPayResult","/service/pay/getPayResult"},produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPayResult(HttpServletRequest request){
        try {
            
/*            token   int     是   设备令牌
            shopId  int     条件  店铺ID。ShopId/userId至少填一个
            userId  int     条件  用户ID。ShopId/userId至少填一个
            transactionId   long        是   交易号
            checkFlag   int 1   否   是否要求同第三方支付对账。
            1：不对账
            2：无支付时，与第三方支付对账。*/
            
            logger.info("支付结果查询接口-start");
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String transactionIdStr = RequestUtils.getQueryParam(request, "transactionId");
            String checkFlagStr = RequestUtils.getQueryParam(request, "checkFlag");
            Map<String, Object> pMap = new HashMap<String, Object>();
            
//            if(StringUtils.isBlank(shopIdStr)&&StringUtils.isBlank(userIdStr)){
//                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "userId和shopId不能都为空");
//            }
            // 验证用户id
            if(StringUtils.isNotBlank(userIdStr)){
                Long userId = CommonValidUtil.validStrLongFmt(userIdStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_USERID);
                UserDto user = memberService.getUserByUserId(userId);
                CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, "会员不存在");
                pMap.put("userId", userId);
            }
            
            // 验证商铺
            if(StringUtils.isNotBlank(shopIdStr)){
                Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_SHOPID);
                int flag = this.shopServcie.queryShopExists(shopId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
                        CodeConst.MSG_MISS_SHOP);
                pMap.put("userId", shopId);
            }
            //transactionId
            CommonValidUtil.validStrNull(transactionIdStr,  CodeConst.CODE_PARAMETER_NOT_NULL, "transactionId不能为空");
            Long transactionId = NumberUtil.strToLong( transactionIdStr,"transactionId");
            pMap.put("transactionId", transactionId);
            
            if(StringUtils.isNotBlank(checkFlagStr)){
                Long checkFlag = NumberUtil.strToLong( checkFlagStr,"checkFlag");
                pMap.put("checkFlag", checkFlag);
            }
            
            
            Map<String, Object> resultMap = this.payServcie.getPayResult(pMap);
                    
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "支付结果查询接口", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("支付结果查询接口-系统异常", e);
            throw new APISystemException("支付结果查询接口-系统异常", e);
        }
    }
    
    /**
	 *  鉴权方式：不鉴权第三方支付
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/service/pay/payBy3rd","/token/pay/payBy3rd","/session/pay/payBy3rd"},method=RequestMethod.POST,consumes="application/json", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object payByThirdChannel(HttpEntity<String> entity, HttpServletRequest request){
		logger.info("不鉴权第三方支付-start");
	
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
			
			String shopIdStr = postParamMap.get("shopId");
			String userIdStr = postParamMap.get("userId");
			String payReasonStr = postParamMap.get("payReason");
			String targetIdStr = postParamMap.get("targetId");
			String payAmountStr = postParamMap.get("payAmount");
			String redPackageMoneyStr = postParamMap.get("redPackageMoney");
			String redPackageIdsStr = postParamMap.get("readPackageIds");
			String clientSystemStr = postParamMap.get("clientSystem");
			String payChannelStr = postParamMap.get("payChannel");
			String subPayModelStr = postParamMap.get("subPayModel");
			String autoSettleFlagStr = postParamMap.get("autoSettleFlag");
			String parameterTypeStr = postParamMap.get("parameterType");
			String channelParameter = postParamMap.get("channelParameter");
			String notifyCashierMobile = postParamMap.get("notifyCashierMobile");
			
			if (notifyCashierMobile != null) {
				CommonValidUtil.validMobileStr(notifyCashierMobile, CodeConst.CODE_PARAMETER_NOT_VALID, "收银员手机号格式错误");
				requestMap.put("notifyCashierMobile", notifyCashierMobile);
				
			}
			if (shopIdStr == null && userIdStr == null)
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"shopId/userId至少填一个");
			
			CommonValidUtil.validStrNull(payReasonStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_PAY_REASON);
			
			Integer payReason = CommonValidUtil.validStrIntFmt(payReasonStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"payReason数据格式错误");
			
			requestMap.put("payReason", payReason);
			
			if (payReason == 1 || payReason ==3) {
				if (userIdStr == null)
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"会员充值或会员支付订单时userId不能为空");
			}
			if (payReason == 2 && shopIdStr == null) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"店铺充值时shopId不能为空");
			}
			if (payReason == 5 && shopIdStr == null) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"店铺购买插件时shopId不能为空");
			}
			if (shopIdStr != null) {
				Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
						CodeConst.CODE_PARAMETER_NOT_VALID,"shopId类型错误");
			
				int flag = this.shopServcie.queryShopExists(Long.valueOf(shopIdStr));
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
				requestMap.put("shopId", shopId);
			}
			
			if (userIdStr != null) {
				Long userId = CommonValidUtil.validStrLongFmt(userIdStr,
						CodeConst.CODE_PARAMETER_NOT_VALID, "userId类型错误");
				UserDto userDto = memberService.getUserByUserId(Long.valueOf(userIdStr));
				if (userDto == null) 
					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
				
				requestMap.put("userId", userId);
			}
			
			CommonValidUtil.validStrNull(targetIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_TARGET_ID);
			
			requestMap.put("targetId", targetIdStr);
			
			CommonValidUtil.validStrNull(payAmountStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_PAYAMOUNT);
			
			Double payAmount = CommonValidUtil.validStrDoubleFmt(payAmountStr, 
											CodeConst.CODE_PARAMETER_NOT_VALID,
											"payAmount数据格式错误");
			
			requestMap.put("payAmount", payAmount);
			
			if (redPackageMoneyStr != null) {
				if (payReason != 3) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
							"红包支付只支持会员支付订单");
				}
				Double redPackageMoney = CommonValidUtil.validStrDoubleFmt(redPackageMoneyStr, 
														CodeConst.CODE_PARAMETER_NOT_VALID,
														"redPackageMoney数据格式错误");
				
				requestMap.put("redPackageMoney", redPackageMoney);
				
				if (redPackageIdsStr != null) {
					
					List<Long> redPayList = new ArrayList<Long>();
					for (String redIdStr : redPackageIdsStr.split(",")) {
						Long redId = CommonValidUtil.validStrLongFmt(redIdStr, 
												CodeConst.CODE_PARAMETER_NOT_VALID,
												"红包Id类型错误  错误Id:"+redIdStr);
						redPayList.add(redId);
					}
					
					requestMap.put("redPackageIds", redPayList);
				}
			}
			
			checkOrderValid(payReason, targetIdStr,requestMap);
			
			CommonValidUtil.validStrNull(clientSystemStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_CLIENT_SYSTEM);
			
			Integer clientSystem = CommonValidUtil.validStrIntFmt(clientSystemStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"clientSystem数据格式错误");
			
			requestMap.put("clientSystem", clientSystem);
			
			CommonValidUtil.validStrNull(payChannelStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_PAY_CHANNEL);
			
			Integer payChannel = CommonValidUtil.validStrIntFmt(payChannelStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"payChannel数据格式错误");
			
			requestMap.put("payChannel", payChannel);
			
			CommonValidUtil.validStrNull(subPayModelStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SUB_PAY_MODEL);
			
			Integer subPayModel = CommonValidUtil.validStrIntFmt(subPayModelStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"subPayModel数据格式错误");
			
			requestMap.put("subPayModel", subPayModel);
			
			if (autoSettleFlagStr != null) {
				Integer autoSettleFlag = CommonValidUtil.validStrIntFmt(autoSettleFlagStr,
						CodeConst.CODE_PARAMETER_NOT_VALID,
						"autoSettleFlag数据格式错误");
				
				requestMap.put("autoSettleFlag", autoSettleFlag);
			}
			
			if (parameterTypeStr != null) {
				Integer parameterType = CommonValidUtil.validStrIntFmt(parameterTypeStr,
						CodeConst.CODE_PARAMETER_NOT_VALID,
						"parameterType数据格式错误");
				
				CommonValidUtil.validStrNull(channelParameter,
						CodeConst.CODE_PARAMETER_NOT_NULL,
						"parameterType参数填写时则第三方支付请求数据不能为空");
				requestMap.put("parameterType", parameterType);
				
			}
			
			if (channelParameter != null) {
				requestMap.put("channelParameter", channelParameter);
			}
			String payChannelKey = getPayChannelKey(payChannel);
			IThirdPayService thirdPayService = (IThirdPayService)BeanFactory.getBean(payChannelKey);
			if (thirdPayService == null)
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"第三方支付processor不存在，payChannelKey："+payChannelKey);
			Map<String, Object> prepayMap = thirdPayService.prePayBy3rd(requestMap);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDER_IS_EXIST, prepayMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("不鉴权第三方支付-系统异常",e);
			throw new APISystemException("不鉴权第三方支付-系统异常", e);
		}
	}
	
	private void checkOrderValid(Integer payReason, String targetId,Map<String, Object> requestMap) throws Exception {
		if (payReason == 1) {
			Long userId = CommonValidUtil.validStrLongFmt(targetId,
					CodeConst.CODE_PARAMETER_NOT_VALID, "会员充值targetId类型错误");
			UserDto userDto = memberService.getUserByUserId(userId);
			if (userDto == null) 
				throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "充值用户不存在");
			requestMap.put("subject", "用户充值");
			return;
		}
		
		if (payReason ==2) {
			Long shopId = CommonValidUtil.validStrLongFmt(targetId,
					CodeConst.CODE_PARAMETER_NOT_VALID,"商铺充值targetId类型错误");
		
			int flag = this.shopServcie.queryShopExists(Long.valueOf(shopId));
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, "充值商铺不存在");
            requestMap.put("subject", "商铺充值");
            return;
		}
		
		if (payReason ==5) {
			Integer shopPluginId = CommonValidUtil.validStrIntFmt(targetId, 
					CodeConst.CODE_PARAMETER_NOT_VALID, "商铺插件Id类型错误");
			
			ShopPluginDto shopPlugin = pluginsService.getShopPluginById(shopPluginId);
			if (shopPlugin == null) {
				throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "商铺插件未下单  shopPluginId:"+shopPluginId);
			}
			if (shopPlugin.getIsPaid() == 1) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺插件以购买");
			}
			
			Double amount = shopPlugin.getMoney();
			Double payAmount = Double.valueOf(requestMap.get("payAmount").toString());
			if (payAmount.doubleValue() !=  amount.doubleValue()) {
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
						"支付金额校验错误  发起支付金额："+payAmount+" 应该支付金额："+amount);
			}
			requestMap.put("subject", "店铺插件购买");
			return;
		}
		
		Integer orderStatus = null;
		OrderDto orderDto = orderServcie.getOrderDtoById(targetId);
		if (orderDto == null) {
			String errorInfo  = "";
			if (payReason == 3)
				errorInfo = "会员订单不存在-订单号：";
			if (payReason == 4)
				errorInfo = "非会员订单不存在-订单号：";
			throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST,
					errorInfo+targetId);
		}
		orderStatus = orderDto.getOrderStatus();
		if((CommonConst.ORDER_STS_YJZ == orderStatus && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()) 
				 || CommonConst.ORDER_STS_TDZ == orderStatus 
				 || CommonConst.ORDER_STS_YTD == orderStatus){
			//订单状态为不可支付状态
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
					"订单不能支付,订单状态为"+orderStatus+" 订单号："+targetId);
		}
		
		BigDecimal payedAmount = packetServcie.queryOrderPayAmount(orderDto.getOrderId(), CommonConst.PAY_TYPE_SINGLE);
		Double notPayAmount = NumberUtil.sub(orderDto.getSettlePrice(), payedAmount.doubleValue());
		
		Double payAmount = Double.valueOf(requestMap.get("payAmount").toString());
		
		//购买经销商身份
		Integer orderType = orderDto.getOrderType();
		if (CommonConst.ORDER_TYPE_PAY_PAIMARY_AGENT == orderType ||
				CommonConst.ORDER_TYPE_PAY_MIDDLE_AGENT == orderType || 
				CommonConst.ORDER_TYPE_PAY__PAIMARY_UPGRADE_MIDDLE_AGENT == orderType) {
			
			if (requestMap.get("userId") == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "购买代理商用户userId不能为空");
			}
			
			Long userId = Long.valueOf(requestMap.get("userId").toString());
			UserDto userDto = memberService.getUserByUserId(userId);
			String userRemark = orderDto.getUserRemark();
			if (StringUtils.isNotBlank(userRemark)) {
				Long referUserId = CommonValidUtil.validStrLongFmt(userRemark,
						CodeConst.CODE_PARAMETER_NOT_VALID, "购买代理商用户推荐人userId类型错误");
				if (userId.equals(referUserId)) {
					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "购买代理商用户推荐人不能是自己");
				}
				UserDto referUserDto = memberService.getUserByUserId(referUserId);
				
				if (referUserDto == null) {
					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "购买代理商用户推荐人不存在");
				}
			}
			
			if (userDto == null) {
				throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "购买代理商用户不存在");
			}
			
//			AgentDto agent = agentDao.getAgent(userId, null, null, null, null, null);
//			
//			if (CommonConst.ORDER_TYPE_PAY_PAIMARY_AGENT == orderType) {
//				if (!(agent == null || agent.getAgentType() < 21)) {
//					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户已经购买初级经销商资格");
//				}
//			}else if (CommonConst.ORDER_TYPE_PAY_MIDDLE_AGENT == orderType) {
//				if (!(agent == null || agent.getAgentType() < 22)) {
//					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户已经购买中级经销商资格");
//				}
//			}else if (CommonConst.ORDER_TYPE_PAY__PAIMARY_UPGRADE_MIDDLE_AGENT == orderType) {
//				if (agent == null || agent.getAgentType() < 21) {
//					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户还不是初级经销商");
//				}
//				
//				if (!(agent == null || agent.getAgentType() < 22)) {
//					throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户已经购买中级经销商资格");
//				}
//			}
		}

		
		if (requestMap.get("redPackageMoney") == null) {
			if (!payAmount.equals(notPayAmount)) {
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
						"支付金额校验错误，发起第三方支付金额："+payAmount+"订单还需金额为："+notPayAmount+
						"订单金额为："+orderDto.getSettlePrice());
			}
		}else {
			
			if (payReason != 3) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "会员支付订单时才可使用红包");
			}
			
			if (requestMap.get("userId") == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "使用红包时必须传userId");
			}
			
			Long userId = Long.valueOf(requestMap.get("userId").toString());
			
			Double redPackageMoney = Double.valueOf(requestMap.get("redPackageMoney").toString());
			chuanQiPayService.checkRedPacketValid(orderDto.getShopId(), userId, redPackageMoney, null);
			
			Double totalPayAmount =  NumberUtil.add(payAmount, redPackageMoney);
			if (!totalPayAmount.equals(notPayAmount)) {
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
						"支付金额校验错误，发起第三方支付金额："+payAmount+"发起红包支付金额："+
						redPackageMoney+"订单还需金额为："+notPayAmount+
						"订单金额为："+orderDto.getSettlePrice());
			}
			
			//获得红包支付信息
			if (requestMap.get("redPackageIds") == null) {
				
				List<RedPacketPayInfo> redPacketPayList = chuanQiPayService.getRedPacketPayInfoByPayAmount(
																					orderDto.getShopId(), 
																					userId,
																					redPackageMoney);
				
				requestMap.put("redPackageIds", redPacketPayList);
			}else {
				
				List<Long> redPacketIds = (List<Long>)requestMap.get("redPackageIds");
				List<RedPacketPayInfo> redPacketPayList = chuanQiPayService.getRedPacketPayInfoByIds(redPacketIds, redPackageMoney);
				
				requestMap.put("redPackageIds", redPacketPayList);
			}
		}
		
		String orderTitle = orderDto.getOrderTitle();
		requestMap.put("subject", orderTitle !=null ? orderTitle : targetId);
		if(requestMap.get("shopId") == null) {
			requestMap.put("shopId", orderDto.getShopId());
		}
	}
	
	private String getPayChannelKey(Integer payChannel) {
		StringBuilder payChannelKeyBuild = new StringBuilder();
		payChannelKeyBuild.append(CommonConst.KEY_PAY_CHANNEL).append(payChannel);
		return payChannelKeyBuild.toString();
	}
	
	/**
	 * 微信支付异步通知
	 * @Title: weixinPayNotify 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/service/weixinPayNotify")
	public void weixinPayNotify(HttpServletRequest request,HttpServletResponse response){ 
		String resXml = "";
		try{
			String inputLine;
			String notityXml = "";
			try {
				while ((inputLine = request.getReader().readLine()) != null) {
					notityXml += inputLine;
				}
				request.getReader().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			logger.info("微信支付回调返回信息：" + notityXml);
			Map<String, Object> weixinPayResultMap = parseXmlToList2(notityXml);//将解析的xml转换为map
		    final PayNotifyResult payResult = buildWeixinPayResult(weixinPayResultMap);
			if("SUCCESS".equals(payResult.getReturnCode()) && "SUCCESS".equals(payResult.getResultCode())){
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Map<String, Object> attach = payResult.getAttach();
							String payChannelKey = getPayChannelKey(Integer.valueOf(attach.get("payChannel").toString()));
							IThirdPayService thirdPayService = (IThirdPayService)BeanFactory.getBean(payChannelKey);
							if (thirdPayService == null)
								throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"第三方支付processor不存在，payChannelKey："+payChannelKey);
							thirdPayService.dealPayBy3rdNotify(payResult);
						} catch (Exception e) {
							logger.error("处理微信支付成功异步回调异常", e);
						}
				
					}
				});
				resXml = buildSuccessNotifyToWeixin();
			}else{
				resXml = buildFailNotifyToWeixin();
				logger.error("微信支付失败返回信息：" + notityXml);
				throw new ValidateException(CodeConst.CODE_WEIXIN_NOTIFY_EXCEPTION,payResult.getReturnMsg());
			}

		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信支付回调异常", e);
			throw new APISystemException("微信支付回调异常", e);
		}finally{
			try{
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
			}catch(Exception e){
				logger.error("响应微信异步回调异常",e);
			}
		}
	}
	
	private String buildSuccessNotifyToWeixin() {
		String returnSuccessXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		return returnSuccessXml;
	}
	
	private String buildFailNotifyToWeixin() {
		String returnFailXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
				+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		return returnFailXml;
	}
	
	private PayNotifyResult buildWeixinPayResult(Map<String, Object> weixinPayResultMap) throws Exception{
		final PayNotifyResult payResult = new PayNotifyResult();
		payResult.setReturnCode(weixinPayResultMap.get("return_code").toString());
		if (payResult.getReturnCode().equals("FAIL")) {
			return payResult;
		}
		payResult.setAppid(weixinPayResultMap.get("appid").toString());
		payResult.setBankType(weixinPayResultMap.get("bank_type").toString());
		payResult.setCashFee(weixinPayResultMap.get("cash_fee").toString());
		payResult.setFeeType(weixinPayResultMap.get("fee_type").toString());
		payResult.setIsSubscribe(weixinPayResultMap.get("is_subscribe").toString());
		payResult.setMchId(weixinPayResultMap.get("mch_id").toString());
		payResult.setNonceStr(weixinPayResultMap.get("nonce_str").toString());
		payResult.setOpenid(weixinPayResultMap.get("openid").toString());
		payResult.setOutTradeNo(weixinPayResultMap.get("out_trade_no").toString());
		payResult.setResultCode(weixinPayResultMap.get("result_code").toString());
		payResult.setSign(weixinPayResultMap.get("sign").toString());
		payResult.setTimeEnd(weixinPayResultMap.get("time_end").toString());
		double totalFee = NumberUtil.fmtDouble(Double.parseDouble(weixinPayResultMap.get("total_fee").toString())/100, 2);
		payResult.setTotalFee(Double.valueOf(totalFee).toString());
		payResult.setTradeType(weixinPayResultMap.get("trade_type").toString());
		payResult.setTransactionId(weixinPayResultMap.get("transaction_id").toString());
		try {
			Map<String, Object> attachMap = JacksonUtil.parseJson2Map(weixinPayResultMap.get("attach").toString());
			payResult.setAttach(attachMap);
			payResult.setPayChannel(Integer.valueOf(attachMap.get("payChannel").toString()));
			payResult.setPayReason(Integer.valueOf(attachMap.get("payReason").toString()));
		} catch (Exception e) {
			logger.error("微信处理attachData异常",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"微信支付异步回调channelData(json串格式错误)");
		}
		return payResult;
	}
	
	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static Map<String,Object> parseXmlToList2(String xml) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

	
	/**
	 * 获取银行卡号信息接口
	 * @Title: weixinPayNotify 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	 @RequestMapping(value = {"session/pay/getBankCardInfo","service/pay/getBankCardInfo","token/pay/getBankCardInfo"}, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	public Object getBankCardInfo(HttpServletRequest request){ 
		  try {
			logger.info("获取银行卡号信息-start");
			  String cardNo = RequestUtils.getQueryParam(request, "cardNo");
			  CommonValidUtil.validStrNull(cardNo, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CARDNO);
			  if(!BankInfo.checkBankCard(cardNo))
			  {
		            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
		                    "银行卡号不正确");
			  }
			  Map<String,String> map = this.payServcie.findBankNameByCardNo(cardNo);
			  return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取银行卡号信息", map);
		 } catch (ServiceException e) {
				throw new APIBusinessException(e);
		} catch (Exception e) {
			 logger.error("获取银行卡号信息接口   -系统异常", e);
	         throw new APISystemException("获取银行卡号信息接口    -系统异常", e);
		}
	}

	
	/**
	 * 支付宝支付异步通知
	 * @Title: aliPayNotify 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/service/aliPayNotify")
	public void aliPayNotify(HttpServletRequest request,HttpServletResponse response){
		logger.info("支付宝异步回调通知---start");
		Map<String, String> notifyParamMap = getNotifyParamMap(request);
		logger.info("开始验证支付宝签名");
		logger.info("回调通知结果:{}",notifyParamMap);
		try {
			  if (AliPayUtil.verify(notifyParamMap)) {
				logger.info("验证支付宝签名通过");
			    final PayNotifyResult payResult = buildAliPayResult(notifyParamMap);
				if(payResult.getTradeStatus().equals("TRADE_SUCCESS") || payResult.getTradeStatus().equals("TRADE_FINISHED")){
					executor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								Map<String, Object> attach = payResult.getAttach();
								String payChannelKey = getPayChannelKey(Integer.valueOf(attach.get("payChannel").toString()));
								IThirdPayService thirdPayService = (IThirdPayService)BeanFactory.getBean(payChannelKey);
								if (thirdPayService == null)
									throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"第三方支付processor不存在，payChannelKey："+payChannelKey);
								thirdPayService.dealPayBy3rdNotify(payResult);
							} catch (Exception e) {
								logger.error("处理支付宝支付成功异步回调异常", e);
							}
					
						}
					});
				}
			}
			else {
				logger.error("验证支付宝签名失败");
			}
			response.getWriter().write("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝支付回调异常", e);
			throw new APISystemException("支付宝支付回调异常", e);
		}
	}
	

	/**
	 * 农业银行支付异步通知
	 * @Title: aliPayNotify 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/service/abcPayNotify")
	public void abcPayNotify(HttpServletRequest request,HttpServletResponse response){
		logger.info("农业银行支付异步通知---start");
		try {
				String msg = request.getParameter("MSG");
				PaymentResult abcPayResult = new PaymentResult(msg);
				logger.info("农业银行支付异步回调通知结果：{}",abcPayResult);
				if (abcPayResult.isSuccess()) {
					logger.info("农行回调验证签名通过");
					
					final PayNotifyResult payResult = buildAbcPayResult(abcPayResult);
					executor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								Map<String, Object> attach = payResult.getAttach();
								String payChannelKey = getPayChannelKey(Integer.valueOf(attach.get("payChannel").toString()));
								IThirdPayService thirdPayService = (IThirdPayService)BeanFactory.getBean(payChannelKey);
								if (thirdPayService == null)
									throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"第三方支付processor不存在，payChannelKey："+payChannelKey);
								thirdPayService.dealPayBy3rdNotify(payResult);
							} catch (Exception e) {
								logger.error("处理农行支付成功异步回调异常", e);
							}
					
						}
					});
					
				}else {
					logger.error("农行回调验证签名或解析失败");
					logger.error("农行回调验证签名或解析失败---------回调失败原因：{}",abcPayResult.getErrorMessage());
				}
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("农行支付回调异常", e);
			throw new APISystemException("农行支付回调异常", e);
		}
	}
	
	/**
	 * 建设银行支付异步通知
	 * @Title: aliPayNotify 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/service/ccbPayNotify")
	public void ccbPayNotify(HttpServletRequest request,HttpServletResponse response){
		logger.info("建设银行支付异步通知---start");
		try {
				Map<String, String> ccbNotifyParam = getCcbNotifyParamMap(request);
				logger.info("建行支付异步回调通知结果：{}",ccbNotifyParam);
				if (ccbNotifyParam.get("SUCCESS").equals(CommonConst.SUCCESS_TYPE)) {
					logger.info("建行支付成功，开始验证签名");
					
					if (verifyCcbNotify(ccbNotifyParam)) {
						logger.info("建设银行支付成功-----回调验证签名通过-----------");
						final PayNotifyResult payResult = buildCcbPayResult(ccbNotifyParam);
						executor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									Map<String, Object> attach = payResult.getAttach();
									String payChannelKey = getPayChannelKey(Integer.valueOf(attach.get("payChannel").toString()));
									IThirdPayService thirdPayService = (IThirdPayService)BeanFactory.getBean(payChannelKey);
									if (thirdPayService == null)
										throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"第三方支付processor不存在，payChannelKey："+payChannelKey);
									thirdPayService.dealPayBy3rdNotify(payResult);
								} catch (Exception e) {
									logger.error("处理建设银行支付成功异步回调异常", e);
								}
						
							}
						});
					} else {
						logger.info("建设银行支付成功---------回调验证签名失败----------");
					}
					
				}else {
					logger.info("建行支付失败");
				}
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("建设银行支付回调异常", e);
			throw new APISystemException("建设银行支付回调异常", e);
		}
	}
	
	private PayNotifyResult buildCcbPayResult(Map<String, String> ccbNotifyParam) {
		PayNotifyResult ccbPayResult = new PayNotifyResult();
		ccbPayResult.setOutTradeNo(ccbNotifyParam.get("ORDERID"));
		ccbPayResult.setTotalFee(ccbNotifyParam.get("PAYMENT"));
		ccbPayResult.setTimeEnd(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		ccbPayResult.setTransactionId(ccbNotifyParam.get("ORDERID"));
		ccbPayResult.setPayChannel(Integer.valueOf(ccbNotifyParam.get("REMARK1")));
		ccbPayResult.setPayReason(Integer.valueOf(ccbNotifyParam.get("REMARK2")));
		
		Map<String, Object> attachMap = new HashMap<String, Object>();
		attachMap.put("payChannel", ccbPayResult.getPayChannel());
		attachMap.put("payReason", ccbPayResult.getPayReason());
		ccbPayResult.setAttach(attachMap);
		return ccbPayResult;
	}
	
	private Boolean verifyCcbNotify(Map<String,String> notifyParams) {
        StringBuffer srcStr = new StringBuffer();
        srcStr.append("POSID=" + notifyParams.get("POSID"));
        srcStr.append("&BRANCHID=" + notifyParams.get("BRANCHID"));
        srcStr.append("&ORDERID=" + notifyParams.get("ORDERID"));
        srcStr.append("&PAYMENT=" + notifyParams.get("PAYMENT"));
        srcStr.append("&CURCODE=" + notifyParams.get("CURCODE"));
        srcStr.append("&REMARK1=" + notifyParams.get("REMARK1"));
        srcStr.append("&REMARK2=" + notifyParams.get("REMARK2"));
        if (StringUtils.isNotBlank(notifyParams.get("ACC_TYPE")))
        {
            srcStr.append("&ACC_TYPE=" + notifyParams.get("ACC_TYPE"));
        }
        srcStr.append("&SUCCESS=" + notifyParams.get("SUCCESS"));
        srcStr.append("&TYPE=" + notifyParams.get("TYPE"));
        srcStr.append("&REFERER=" + notifyParams.get("REFERER"));
        srcStr.append("&CLIENTIP="+notifyParams.get("CLIENTIP") );
        srcStr.append("&ACCDATE=" + notifyParams.get("ACCDATE"));
        if (StringUtils.isNotBlank(notifyParams.get("USRMSG")))
        {
            srcStr.append("&USRMSG=" + notifyParams.get("USRMSG"));
        }
        
        // 验证签名
        RSASig rsa = new RSASig();
        rsa.setPublicKey(notifyParams.get("PUBKEY"));
        logger.info("参与验签参数："+srcStr.toString());
        return rsa.verifySigature(notifyParams.get("SIGN"), srcStr.toString());
	}
	
	private Map<String, String> getCcbNotifyParamMap(HttpServletRequest request) {
		Map<String,String> notifyParams = new HashMap<String,String>();
		
		notifyParams.put("POSID", RequestUtils.getQueryParam(request, "POSID"));
		notifyParams.put("BRANCHID", RequestUtils.getQueryParam(request, "BRANCHID"));
		notifyParams.put("ORDERID", RequestUtils.getQueryParam(request, "ORDERID"));
		notifyParams.put("PAYMENT", RequestUtils.getQueryParam(request, "PAYMENT"));
		notifyParams.put("CURCODE", RequestUtils.getQueryParam(request, "CURCODE"));
		notifyParams.put("REMARK1", RequestUtils.getQueryParam(request, "REMARK1"));
		notifyParams.put("REMARK2", RequestUtils.getQueryParam(request, "REMARK2"));
		notifyParams.put("ACC_TYPE", RequestUtils.getQueryParam(request, "ACC_TYPE"));
		notifyParams.put("SUCCESS", RequestUtils.getQueryParam(request, "SUCCESS"));
		notifyParams.put("TYPE", RequestUtils.getQueryParam(request, "TYPE"));
		notifyParams.put("REFERER", RequestUtils.getQueryParam(request, "REFERER"));
		notifyParams.put("CLIENTIP", RequestUtils.getQueryParam(request, "CLIENTIP"));
		notifyParams.put("ACCDATE", RequestUtils.getQueryParam(request, "ACCDATE"));
		notifyParams.put("USRMSG", RequestUtils.getQueryParam(request, "USRMSG"));
		notifyParams.put("SIGN", RequestUtils.getQueryParam(request, "SIGN"));
		
		String PUBKEY = "";
        if(StringUtils.equals(CommonConst.ACC_TYPE,notifyParams.get("ACC_TYPE")))
        {
            //贷记卡公钥
        	PUBKEY = CommonConst.CREDIT_CARD_PUBLIC_KEY_ALL;
        }
        else
        {
            //借记卡公钥
        	PUBKEY = CommonConst.DEBIT_CARD_PUBLIC_KEY_ALL;
        }
        
        notifyParams.put("PUBKEY", PUBKEY);
		return notifyParams;
	}
	private Map<String, String> getNotifyParamMap(HttpServletRequest request) {
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String valueStr = "";
			valueStr = RequestUtils.getQueryParam(request, name);
			params.put(name, valueStr);
		}
		return params;
	}
	
	private PayNotifyResult buildAbcPayResult(PaymentResult abcPayResult) throws Exception {
		final PayNotifyResult payResult = new PayNotifyResult();
		payResult.setOutTradeNo(abcPayResult.getValue("OrderNo"));
		payResult.setTransactionId(abcPayResult.getValue("iRspRef"));
		payResult.setNotifyId(abcPayResult.getValue("VoucherNo"));
		
		String payDate = abcPayResult.getValue("HostDate");
		String payTime = abcPayResult.getValue("HostTime");
		String timeEnd = payDate +" " +payTime;
		payResult.setTimeEnd(timeEnd.replaceAll("/", "-"));
		payResult.setTotalFee(abcPayResult.getValue("Amount"));
		
		try {
			Map<String, Object> attachMap = getAbcAttachData(abcPayResult.getValue("MerchantRemarks"));
			payResult.setAttach(attachMap);
			payResult.setPayChannel(Integer.valueOf(attachMap.get("payChannel").toString()));
			payResult.setPayReason(Integer.valueOf(attachMap.get("payReason").toString()));
		} catch (Exception e) {
			logger.error("农行处理attachData异常",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"农行支付异步回调attachData(json串格式错误)");
		}
		return payResult;
	}
	
	private Map<String, Object> getAbcAttachData(String MerchantRemarks) {
		Map<String, Object> attachMap = new HashMap<String, Object>();
		
		for (String data : MerchantRemarks.split(",")) {
			String[] item = data.split("=");
			attachMap.put(item[0], item[1]);
		}
		
		return attachMap;
		
	}
	
	private PayNotifyResult buildAliPayResult(Map<String, String> aliPayResultMap) throws Exception{
		final PayNotifyResult payResult = new PayNotifyResult();
		payResult.setOutTradeNo(aliPayResultMap.get("out_trade_no"));
		payResult.setTradeStatus(aliPayResultMap.get("trade_status"));
		payResult.setTransactionId(aliPayResultMap.get("trade_no"));
		payResult.setNotifyId(aliPayResultMap.get("notify_id"));
		payResult.setTimeEnd(aliPayResultMap.get("notify_time"));
		if (aliPayResultMap.get("total_amount") != null) {
			payResult.setTotalFee(aliPayResultMap.get("total_amount"));
		} else {
			payResult.setTotalFee(aliPayResultMap.get("total_fee"));
		}
		try {
			Map<String, Object> attachMap = new HashMap<String, Object>();
			if (aliPayResultMap.get("body") != null) {
				attachMap = JacksonUtil.parseJson2Map(aliPayResultMap.get("body").toString());
			}else {
				attachMap = JacksonUtil.parseJson2Map(aliPayResultMap.get("extra_common_param").toString());
			}
			payResult.setAttach(attachMap);
			payResult.setPayChannel(Integer.valueOf(attachMap.get("payChannel").toString()));
			payResult.setPayReason(Integer.valueOf(attachMap.get("payReason").toString()));
		} catch (Exception e) {
			logger.error("支付宝处理attachData异常",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"支付宝支付异步回调attachData(json串格式错误)");
		}
		return payResult;
	}

	/**
	 *P46：组合支付接口
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/pay/groupPayForPlatform"},
			        method=RequestMethod.POST,
			        consumes="application/json", 
			        produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object groupPayForPlatform(HttpEntity<String> entity, HttpServletRequest request) {
		logger.info("组合支付接口-start");
		try {
			GroupPayModel groupPayModel = checkGroupPayParamValid(entity);
			chuanQiPayService.groupPay(groupPayModel);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "组合支付成功",null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("组合支付接口-系统异常",e);
			throw new APISystemException("组合支付接口-系统异常", e);
		}
	}
	
	private GroupPayModel checkGroupPayParamValid(HttpEntity<String> entity) throws Exception {
		GroupPayModel groupPayModel = (GroupPayModel) JacksonUtil.postJsonToObj(entity, 
												GroupPayModel.class, DateUtils.DATETIME_FORMAT);
		
		CommonValidUtil.validObjectNull(groupPayModel.getUserId(), 
							CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
		
		if (groupPayModel.getAutoSettleFlag() == null || groupPayModel.getAutoSettleFlag() < 0) {
			groupPayModel.setAutoSettleFlag(0);
		}
		UserDto userDto = memberService.getUserByUserId(groupPayModel.getUserId());
		if (userDto == null) {
			throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户不存在");
		}
		
		CommonValidUtil.validObjectNull(groupPayModel.getOrderId(), 
				CodeConst.CODE_PARAMETER_NOT_NULL,"orderId不能为空");
		
		CommonValidUtil.validObjectNull(groupPayModel.getOrderAmount(), 
				CodeConst.CODE_PARAMETER_NOT_NULL,"订单支付金额不能为空");
		String password = groupPayModel.getPayPassword();
		String veriCode = groupPayModel.getVeriCode();
		if(StringUtils.isBlank(password) && StringUtils.isBlank(veriCode)) {
		    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "用户支付密码和验证码不能都为空");
		}

        if (StringUtils.isNotBlank(password) && !password.equals(userDto.getPayPassword())) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付密码错误");
        }
        if(StringUtils.isNotBlank(veriCode)) {
            //短信支付
            boolean flag = sendSmsService.checkSmsCodeIsOk(userDto.getMobile(), groupPayModel.getUsage(), veriCode, true);
            if(!flag) {
                //验证不通过
                throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
            }
        }
		
		if (groupPayModel.getPayInfo() == null || groupPayModel.getPayInfo().isEmpty()) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付列表不能为空");
		}
		String orderId = groupPayModel.getOrderId();
		OrderDto orderDto = orderServcie.getOrderDtoById(orderId);
		if (orderDto == null) {
			throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST,
					"订单不存在");
		}
		
		Integer orderStatus = orderDto.getOrderStatus();
		Integer payStatus = orderDto.getPayStatus();
		
		if (payStatus == CommonConst.PAY_STATUS_PAY_SUCCESS) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
					"订单已经支付成功");
		}
		
		if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=orderDto.getSettleFlag().intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus 
				 || CommonConst.ORDER_STS_YTD == orderStatus){
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
					"订单不能支付,订单状态为"+orderStatus+" 订单号："+orderId);
		}
		//已支付金额
		BigDecimal payedAmount = packetServcie.queryOrderPayAmount(orderId, CommonConst.PAY_TYPE_SINGLE);
		Double notPayAmount = NumberUtil.sub(orderDto.getSettlePrice(), payedAmount.doubleValue());
		if (groupPayModel.getOrderAmount().doubleValue() != notPayAmount.doubleValue()) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,
					"支付金额校验错误，发起支付金额："+groupPayModel.getOrderAmount()+" 未支付订单金额为："+ notPayAmount);
		}
		
		double payListTotalAmount = 0;
		//payAmount的值都传了,需要校验总和对不对
		for (GroupPayDetailModel payDetail : groupPayModel.getPayInfo()) {
			if (payDetail.getPayType() == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付列表中支付方式不能为空"); 
			}
			if(payDetail.getPayAmount() == null) {
			    continue;
			}
			double payAmount = payDetail.getPayAmount().doubleValue();
			payListTotalAmount = NumberUtil.add(payListTotalAmount,payAmount);
		}
		
	    if (payListTotalAmount != groupPayModel.getOrderAmount().doubleValue()) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "组合支付列表金额之和与订单支付金额不符");
        }
		return groupPayModel;
	}
}
