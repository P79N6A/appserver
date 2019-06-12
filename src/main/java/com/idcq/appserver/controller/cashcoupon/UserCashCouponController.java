package com.idcq.appserver.controller.cashcoupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcoupon.ICashCouponService;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;


@Controller
@RequestMapping(value="/coupon")
public class UserCashCouponController {
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IUserCashCouponService userCouponService;
	@Autowired
	private IMemberServcie memberService;
	@Autowired
	private ICashCouponService cashCouponService;
	@Autowired
	private IPayServcie payService;
	@Autowired
	private IOrderServcie orderService;
	@Autowired
	private IPacketService packetService;
	@Autowired
	private IOrderServcie orderServcie;
	@Autowired
	private IPushService pushService;
	
	/**
	 * 用户领取代金券
	 * 
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="/grabCashCoupon",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto grabCashCoupon(HttpServletRequest request){
//		try{
//			logger.info("用户领取代金券-start");
//			String uid = RequestUtils.getQueryParam(request, "userId");
//			String ccid = RequestUtils.getQueryParam(request, "cashCouponId");
//			CommonValidUtil.validStrNull(uid, CodeConst.CODE_REQUIRED,CodeConst.MSG_REQUIRED_USER_ID);
//			CommonValidUtil.validNumStr(uid, CodeConst.CODE_DATAFORMAT_ERROR,CodeConst.MSG_FORMAT_ERROR_USERID);
//			CommonValidUtil.validStrNull(ccid, CodeConst.CODE_REQUIRED,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
//			CommonValidUtil.validNumStr(ccid, CodeConst.CODE_DATAFORMAT_ERROR,CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
//			
//			//获取代金券可用数量；可用数量<=0 代金券已经领取完
//			CashCouponDto couponDto = cashCouponService.getCouponDtoByIdForGrab(cashCouponId);//包括领取有效期
//			//可用数量、每天上限数量
//			int avaliableNum = 0;int obtainNumberPerDayPerPerson = 0;
//			if(null!=couponDto){
//				avaliableNum= couponDto.getAvailableNumber();
//				//每人每天最多领取张数userId,new Date() count(1)
//				obtainNumberPerDayPerPerson = couponDto.getObtainNumberPerDayPerPerson();
//			}
//			if(avaliableNum<=0){
//				return ResultUtil.getResult(CodeConst.CODE_USER_CASHCOUPON_INVALID, CodeConst.MSG_USER_CASHCOUPON_INVALID, null);
//			}
//			
//			//获取每人每天已领取张数
//			int countOfGrab = usercashCouponService.selectPerDayPerPerson(userId);
//			if(countOfGrab>=obtainNumberPerDayPerPerson||obtainNumberPerDayPerPerson<=0){
//				throw new ValidateException(CodeConst.CODE_USER_CASHCOUPON_GRAB_LIMIT, CodeConst.MSG_USER_CASHCOUPON_GRAB_LIMIT);
//			}
//			
//			
//			//修改代金券的可用数量，可用数量-1
//			cashCouponService.updateAvaliableNum(cashCouponId);
//			
//			UserCashCouponDto userCouponDto = new UserCashCouponDto();
//			userCouponDto.setUserId(userId);
//			userCouponDto.setCashCouponId(cashCouponId);
//			//获取price
//			userCouponDto.setPrice(couponDto.getPrice());
//			//update
//			userCouponDto.setObtainTime(new Date());
//			Integer b = 0;
//			userCouponDto.setCouponStatus(b);
//			userCouponDto.setUsedPrice(0.0);
//			
//			
//			int flag = userCouponService.obtainCoupon(userCouponDto);
//			if(flag>=1){
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("uccId", userCouponDto.getUccId());
//				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "领取代金券成功！", map);
//			}else{
//				return ResultUtil.getResult(CodeConst.CODE_USER_CASHCOUPON_GRAB_ERR, CodeConst.MSG_USER_CASHCOUPON_GRAB_ERR, null);
//			}
//		}catch(ServiceException e){
//			throw new APIBusinessException(e);
//		}catch(Exception e){
//			e.printStackTrace();
//			logger.error("领取代金券失败！throws Exception", e);
//			throw new APISystemException("领取代金券失败", e);
//		}
//		
//	}
	
	//用户领取代金券 comment-date:20150401
	@RequestMapping(value="/grabCashCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto grabCashCoupon(HttpServletRequest request){
		try{
			logger.info("用户领取代金券-start");
			String uid = RequestUtils.getQueryParam(request, "userId");
			String ccid = RequestUtils.getQueryParam(request, "cashCouponId");
			CommonValidUtil.validStrNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validNumStr(uid, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validStrNull(ccid, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
			CommonValidUtil.validNumStr(ccid, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
			long userId = Long.parseLong(uid);
			long cashCouponId = Long.parseLong(ccid);
			UserDto user=memberService.getUserByUserId(userId);
			CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
			//获取代金券可用数量；可用数量<=0 代金券已经领取完
			CashCouponDto couponDto = cashCouponService.getCouponDtoByIdForGrab(cashCouponId);//包括领取有效期
			//可用数量、每天上限数量
			long avaliableNum = 0;long obtainNumberPerDayPerPerson = 0;
			if(null!=couponDto){
				avaliableNum= couponDto.getAvailableNumber();
				//每人每天最多领取张数userId,new Date() count(1)
				obtainNumberPerDayPerPerson = couponDto.getObtainNumberPerDayPerPerson();
			}
			if(avaliableNum<=0){
				return ResultUtil.getResult(CodeConst.CODE_USER_CASHCOUPON_INVALID, CodeConst.MSG_USER_CASHCOUPON_INVALID, null);
			}
		
			//获取每人每天已领取张数
			long countOfGrab = userCouponService.selectPerDayPerPerson(userId);
			if(countOfGrab>=obtainNumberPerDayPerPerson||obtainNumberPerDayPerPerson<=0){
				throw new ValidateException(CodeConst.CODE_USER_CASHCOUPON_GRAB_LIMIT, CodeConst.MSG_USER_CASHCOUPON_GRAB_LIMIT);
			}
		
			//修改代金券的可用数量，可用数量-1
			cashCouponService.updateAvaliableNum(cashCouponId);
		
			UserCashCouponDto userCouponDto = new UserCashCouponDto();
			userCouponDto.setUserId(userId);
			userCouponDto.setCashCouponId(cashCouponId);
			//获取price
			userCouponDto.setPrice(couponDto.getPrice());
			//update
			userCouponDto.setObtainTime(new Date());
			Integer b = 0;
			userCouponDto.setCouponStatus(b);
			userCouponDto.setUsedPrice(0.0);
		
			int flag = userCouponService.obtainCoupon(userCouponDto);
			if(flag>=1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("uccId", userCouponDto.getUccId());
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "领取代金券成功！", map);
			}else{
				return ResultUtil.getResult(CodeConst.CODE_USER_CASHCOUPON_GRAB_ERR, CodeConst.MSG_USER_CASHCOUPON_GRAB_ERR, null);
			}
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("用户领取代金券-系统异常", e);
				throw new APISystemException("用户领取代金券-系统异常", e);
			}
	}
	
	/**
	 * 用户消费代金券接口
	 * <b>
	 * 	auth： 聂久乾
	 * 	date：2015-07-14
	 * </b>
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/consumeCashCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object consumeCashCoupon(HttpServletRequest request){
		long startTime = System.currentTimeMillis();
		try {
			logger.info("用户消费代金券接口-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			String uccIdStrs = RequestUtils.getQueryParam(request, "uccId");
			String orderPayTypeStr = RequestUtils.getQueryParam(request, "orderPayType");
			String payAmountStr = RequestUtils.getQueryParam(request, "payAmount");
			String pwdTypeStr = RequestUtils.getQueryParam(request, "pwdType");
			String password = RequestUtils.getQueryParam(request, "password");
			//===============================参数校验start==================================
			//userId校验
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			//订单编号校验
			CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_ORDERID);
			//订单支付类型校验，不填默认为单个订单支付
			if(!StringUtils.isBlank(orderPayTypeStr)){
				CommonValidUtil.validNumStr(orderPayTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_PAYTYPE);
			}else{
				orderPayTypeStr = CommonConst.PAY_TYPE_SINGLE+"";
			}
			//代金券编号校验
			CommonValidUtil.validStrNull(uccIdStrs, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
			List<Long> uccIds = validUccIdStrs(uccIdStrs);
			//支付密码校验，如果校验类型不为空，则密码为必填项
			Integer pwdType = null;
			if(!StringUtils.isBlank(pwdTypeStr)){
				pwdType = CommonValidUtil.validStrIntFmt(pwdTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "密码校验类型格式错误");
				//密码值非空校验
				CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL,"密码值不能为空");
			}
			//====================================参数校验end===============================
			int re = userCouponService.consumeCashCoupon(userId,orderId,Integer.parseInt(orderPayTypeStr),uccIds,pwdType,password);
			
			//向收银机推送
			OrderDto order = this.orderServcie.getOrderMainById(orderId);
			if(null != order) {
				Integer orderStatus = order.getOrderStatus();
				//如果订单还是已预订状态则需要给收银机进行推送  -- 暂时不推
//				if (null != orderStatus && CommonConst.ORDER_STS_YYD == orderStatus) {
//					PlaceOrderPushMessageUtil.detailSingleOrder(orderId);
//				}
				Long shopId = order.getShopId();
				if(null != shopId) {
					StringBuilder content = new StringBuilder();
					content.append("{");
					content.append("\"shopId\":"+shopId+",");
					content.append("\"action\":\"cashOrder\",");
					content.append("\"data\":{\"id\":\""+orderId+"\",\"orderStatus\":"+orderStatus+",\"payStatus\":"+order.getPayStatus()+"}");
					content.append("}");
					PushDto push = new PushDto();
					push.setAction("cashOrder");
					push.setContent(content.toString());
					push.setUserId(userId);
					push.setShopId(shopId);
					pushService.pushInfoToShop2(push);
				}
			}
			
			logger.info("代金券消费共用时："+(System.currentTimeMillis() - startTime));
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "消费代金券成功！", null);
		} catch(ServiceException e){
			logger.info("用户消费代金券接口-异常",e);
			throw new APIBusinessException(e);
		} catch(Exception e){
			logger.info("用户消费代金券接口-系统异常",e);
			throw new APISystemException("用户消费代金券接口-系统异常", e);
		}
	}
	
	/**
	 * 校验代金券格式
	 * @param uccIdStrs
	 * @return
	 * @throws Exception
	 */
	public List<Long> validUccIdStrs(String uccIdStrs) throws Exception{
		String[] strs = uccIdStrs.split(CommonConst.PAY_CASH_COUPON_SPLIT_KEY);
		List<Long> uccIds = null;
		if (null != strs && strs.length > 0) {
			uccIds = new ArrayList<Long>();
			for (int i = 0; i < strs.length; i++) {
				Long uccId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID, "编号["+strs[i]+"]"+CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
				uccIds.add(uccId);
			}
		}
		if (uccIds == null || uccIds.size() <= 0) {
			throw new APIBusinessException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
		}
		strs = null;
		return uccIds;
	}
	
	
	/**
	 * 用户消费代金券
	 * 
	 * <p>	
	 *  modify by： 卢建平<br>
	 *  modify date：20150417
	 * </p>
	 * <p>	
	 *  重构：支持多张代金券同时消费<br>
	 *  重构人： 卢建平<br>
	 *  重构日期：20150506
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/consumeCashCoupon_bak",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto consumeCoupon(HttpServletRequest request){
		try{
			String uid = RequestUtils.getQueryParam(request, "userId");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			String uccIds = RequestUtils.getQueryParam(request, "uccId");
			String orderPayTypeStr = RequestUtils.getQueryParam(request, "orderPayType");
			CommonValidUtil.validStrNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validPositLong(uid, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_ORDERID);
			CommonValidUtil.validStrNull(uccIds, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
//			CommonValidUtil.validPositLong(uccid, CodeConst.CODE_DATAFORMAT_ERROR,CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
			if(!StringUtils.isBlank(orderPayTypeStr)){
				CommonValidUtil.validNumStr(orderPayTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_PAYTYPE);
			}else{
				orderPayTypeStr = CommonConst.PAY_TYPE_SINGLE+"";
			}
			long userId = Integer.parseInt(uid);
			Integer orderPayType = Integer.valueOf(orderPayTypeStr);
			/*---------改造start date 20150417-------*/
			//用户存在性
			userId = this.memberService.authenUserById(userId);
			CommonValidUtil.validPositLong(userId, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			userCouponService.consumeCoupon(userId,uccIds,orderId,orderPayType);
			/*---------改造end-------*/
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "消费代金券成功！", null);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("消费代金券失败！", e);
			throw new APISystemException("消费代金券失败", e);
		}
	}
	
//	commnet date : 20150506 by lujianping
//	@RequestMapping(value="/consumeCashCoupon")
//	@ResponseBody
//	public ResultDto consumeCoupon(HttpServletRequest request){
//		try{
//			String uid = RequestUtils.getQueryParam(request, "userId");
//			String oid = RequestUtils.getQueryParam(request, "orderId");
//			String uccid = RequestUtils.getQueryParam(request, "uccId");
//			String orderPayTypeStr = RequestUtils.getQueryParam(request, "orderPayType");
//			CommonValidUtil.validStrNull(uid, CodeConst.CODE_REQUIRED,CodeConst.MSG_REQUIRED_USER_ID);
//			CommonValidUtil.validPositLong(uid, CodeConst.CODE_DATAFORMAT_ERROR, CodeConst.MSG_FORMAT_ERROR_USERID);
//			CommonValidUtil.validStrNull(oid, CodeConst.CODE_REQUIRED,CodeConst.MSG_REQUIRED_ORDERID);
//			CommonValidUtil.validStrNull(uccid, CodeConst.CODE_REQUIRED,CodeConst.MSG_REQUIRED_CASHCOUNPONID);
//			CommonValidUtil.validPositLong(uccid, CodeConst.CODE_DATAFORMAT_ERROR,CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
//			if(!StringUtils.isBlank(orderPayTypeStr)){
//				CommonValidUtil.validNumStr(orderPayTypeStr, CodeConst.CODE_DATAFORMAT_ERROR,CodeConst.MSG_FORMAT_ERROR_PAYTYPE);
//			}else{
//				orderPayTypeStr = CommonConst.PAY_TYPE_SINGLE+"";
//			}
//			
//			long userId = Integer.parseInt(uid);
//			String orderId = oid;
//			long uccId = Integer.parseInt(uccid);
//			Integer orderPayType = Integer.valueOf(orderPayTypeStr);
//			/*---------改造start date 20150417-------*/
//			//用户存在性
//			userId = this.memberService.authenUserById(userId);
//			CommonValidUtil.validPositLong(userId, CodeConst.CODE_MISS_MODEL, CodeConst.MSG_MISS_MEMBER);
//			//代金券存在性
//			UserCashCouponDto userCouponDto = userCouponService.getUserCouponDto(userId, uccId);
//			CommonValidUtil.validObjectNull(userCouponDto, CodeConst.CODE_MISS_MODEL, CodeConst.MSG_MISS_CASHCOUPON);
//			//是否使用过
//			CommonValidUtil.validIntNoEqual(userCouponDto.getCouponStatus(), 1, CodeConst.CODE_INVALID, CodeConst.MSG_BE_USED_CC);
//			CashCouponDto couponDto = cashCouponService.getCouponDtoById(userCouponDto.getCashCouponId());
//			CommonValidUtil.validObjectNull(couponDto, CodeConst.CODE_MISS_MODEL, CodeConst.MSG_MISS_CASHCOUPON);
//			List<Map> list = null;
//			OrderDto validOrder = null;
//			BigDecimal amount = null;			//订单总金额
//			Double amountTotal = null;			//订单总金额
//			BigDecimal  payAmount = null;		//已经支付的金额
//			if(CommonConst.PAY_TYPE_GROUP == orderPayType){//订单组支付
//				//订单组订单列表
//				list = this.payService.queryOrderGroupById(orderId);
//				if (list==null || list.size() == 0) {
//					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
//				}
//				for(Map map : list){
//					validOrder=this.orderService.getOrderById((String)map.get("orderId"));
//					if(CommonConst.PAY_STATUS_PAYED == validOrder.getPayStatus()){
//						throw new ValidateException(CodeConst.CODE_PAYED,CodeConst.MSG_PAY_STATUS_SUCCESS);
//					}
//					//适用商铺
//					Long shopId = couponDto.getShopId();//适用商铺
//					if(couponDto.getIssuerShopId() != null){//不是平台所发行，仅适用指定店铺
//						//查询订单的商铺
//						if(!(shopId.toString()).equals(validOrder.getShopId().toString())){
//							throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_CASHCOUPON_OTHER_SHOP);
//						}
//					}
//				}
//				amountTotal = this.payService.getSumOrderGroupAmount(orderId);
//				amount = new BigDecimal(amountTotal);							
//				payAmount = this.packetService.queryOrderPayAmount(orderId,orderPayType);
//			}else{//单个订单支付
//				//订单存在性
//				validOrder = orderService.getOrderById(orderId,0);
//				CommonValidUtil.validObjectNull(validOrder, CodeConst.CODE_MISS_MODEL, CodeConst.MSG_MISS_ORDER);
//				//订单支付状态：未支付
//				CommonValidUtil.validIntNoEqual(validOrder.getPayStatus(), CommonConst.PAY_STATUS_PAYED, 
//						CodeConst.CODE_PAYED, CodeConst.MSG_PAYED_ODDER);
//				//适用商铺
//				Long shopId = couponDto.getShopId();//适用商铺
//				if(couponDto.getIssuerShopId() != null){//不是平台所发行，仅适用指定店铺
//					//查询订单的商铺
//					if(!(shopId.toString()).equals(validOrder.getShopId().toString())){
//						throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_CASHCOUPON_OTHER_SHOP);
//					}
//				}
//				amount = packetService.queryOrderAmount(orderId);
//				payAmount = packetService.queryOrderPayAmount(orderId,orderPayType);
//			}
//			
//			//消费有效时间范围
//			CommonValidUtil.validCurDateOfDateRange(couponDto.getStartTime(), couponDto.getStopTime(), 
//					CodeConst.CODE_NOT_IN_DATERANGE, CodeConst.MSG_NOT_IN_COUSUME_DATERANGE);
//			//满多少可以使用
//			Double useablePrice = couponDto.getConditionPrice();
////			Double orderSettlePrice = pOrder.getSettlePrice();
//			//订单金额未达到使用代金券额度
//			CommonValidUtil.validDoubleGreaterThan(amount.doubleValue(), useablePrice, CodeConst.CODE_INVALID, 
//					CodeConst.MSG_USER_CASHCOUPON_CONDITION_PRICE);
//			//能否和同类券一起使用
//			int useableTogether = couponDto.getUseTogetherFlag();
//			if(useableTogether == 0){
//				//查询是否使用代金券支付过
//				int num = this.payService.checkOrderIsPayByCash(orderId,uccId,orderPayType);
//				CommonValidUtil.validIntNotZero(num, CodeConst.CODE_INVALID, CodeConst.MSG_USER_CC_NOTUSE_SAME);
//			}
//			//每单一次可用几张  
//			long useNumberPerOrder = couponDto.getUseNumberPerOrder();
//			//检查已经使用了多少张
//			int usedNumber = this.userCouponService.getCashUsedNumOfOrder(orderId, uccId,orderPayType);
//			CommonValidUtil.validLongGreaterEqual(useNumberPerOrder, Long.valueOf(usedNumber), CodeConst.CODE_INVALID, 
//					CodeConst.MSG_USER_CC_USEFUL_PER_ORDER);
//			//面值
//			Double price = couponDto.getPrice();
//			//获取已支付总金额
////			Double sumPayedAmount = payService.getSumPayAmount(orderId);
//			//剩余金额 = 应支付总额-已支付总额
//			Double needPayAmount = amount.doubleValue() - payAmount.doubleValue() ; 
//			if(needPayAmount<=0){
//				throw new ValidateException(CodeConst.CODE_CASHCOUPON_NOT, CodeConst.MSG_CASHCOUPON_NOT);//剩余金额<=0
//			}
//			//修改用户代金券信息
//			PayDto pay = new PayDto();
//			pay.setOrderId(orderId);
//			pay.setPayType(2);//代金券支付
//			pay.setUccId(uccId);
//			pay.setPrice(price);
//			pay.setUserId(userId);
//			pay.setNeedPayAmount(needPayAmount);
//			int flag = userCouponService.consumeCoupon(list,validOrder,pay,orderPayType);
//			/*---------改造end-------*/
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "消费代金券成功！", null);
//		}catch(ServiceException e){
//			throw new APIBusinessException(e);
//		}catch(Exception e){
//			e.printStackTrace();
//			logger.error("消费代金券失败！", e);
//			throw new APISystemException("消费代金券失败", e);
//		}
//	}
	
	private boolean isExists(List<OrderGoodsDto> lst, Long shopId) throws Exception{
		boolean flag = false;
		if(null!=shopId){//商铺下发的代金券
			//根据订单找到相应的商铺
			if(null!=lst&&lst.size()>0){
				for(OrderGoodsDto dto : lst){
					if(shopId.equals(dto.getShopId())){
						flag = true;
						break;
					}
				}	
			}
		}
		return flag;
	}
}
