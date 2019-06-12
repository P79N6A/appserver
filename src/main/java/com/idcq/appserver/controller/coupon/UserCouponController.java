package com.idcq.appserver.controller.coupon;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.coupon.ICouponService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;


@Controller
//@RequestMapping(value="/coupon")
public class UserCouponController {
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IMemberServcie memberService;
	
	@Autowired
	private ICouponService couponService;
	
	@Autowired
	private IPayServcie payService;
	
	@Autowired
	private IOrderServcie orderService;
	
	/**
	 * 用户消费优惠券
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/coupon/consumeCoupon")
	@ResponseBody
	public ResultDto consumeCoupon(HttpServletRequest request){
		try{
				long start=System.currentTimeMillis();
				logger.info("消费优惠券start:"+start);	
				String userId = RequestUtils.getQueryParam(request, "userId");
				String orderId = RequestUtils.getQueryParam(request, "orderId");
				String ucId = RequestUtils.getQueryParam(request, "ucId");
				String orderPayType = RequestUtils.getQueryParam(request, "orderPayType");// 订单支付类型：0(单个订单支付），1（多个订单支付）
				if(orderPayType==null){
					orderPayType="0";
				}
				CommonValidUtil.validObjectNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
				CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID,"userId格式错误");
				CommonValidUtil.validObjectNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,"orderId不能为空");
				CommonValidUtil.validNumStr(orderId, CodeConst.CODE_PARAMETER_NOT_VALID,"orderId格式错误");
				CommonValidUtil.validObjectNull(ucId, CodeConst.CODE_PARAMETER_NOT_NULL,"ucId不能为空");
				String[] ucIds=ucId.split(";");
				for(String ucid:ucIds){
					couponService.consumeCoupon(Long.parseLong(userId), orderId, Long.parseLong(ucid),Integer.parseInt(orderPayType));
				}
				logger.info("共耗时:"+(System.currentTimeMillis()-start));
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "消费优惠券成功！", null);
			
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("消费优惠券失败！", e);
				throw new APISystemException("消费优惠券失败", e);
			}
	}
	
}
