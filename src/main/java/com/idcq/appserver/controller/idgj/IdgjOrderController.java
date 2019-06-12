package com.idcq.appserver.controller.idgj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.IdgjCommonConst;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.order.IXorderService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 一点管家订单
 * @ClassName: IdgjOrderController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年11月26日 上午10:27:18 
 *
 */
@Controller
public class IdgjOrderController {
	
	/**
	 * 会员订单
	 */
	@Autowired
	private IOrderServcie orderService;
	
	/**
	 * 非会员订单
	 */
	@Autowired
	private IXorderService xorderService;
	
	/**
	 * 查询一点管家订单详情
	 * @Title: getOrderDetail 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="order/getIdgjOrderDetail",produces = "application/json;charset=UTF-8")
	public @ResponseBody String getOrderDetail(HttpServletRequest request){
		try{
			String orderTypeStr=RequestUtils.getQueryParam(request, "orderType");//订单类型
			String orderId=RequestUtils.getQueryParam(request,"orderId");//订单编号
			//检验shopId是否为空
			CommonValidUtil.validStrNull(orderId,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_ORDERID);//订单编号不能为空
			CommonValidUtil.validStrNull(orderTypeStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_ORDERTYPE);//订单类型不嫩为空
			Integer orderType = NumberUtil.strToNum(orderTypeStr, "orderType");
			Map<String,Object>resultMap=new HashMap<String,Object>();
			OrderDto order=orderService.getIdgjOrderDetail(orderId);
			if(order!=null){
				resultMap.put("orderId", order.getOrderId());
				resultMap.put("orderStatus", order.getOrderStatus());
				resultMap.put("orderAmount", order.getSettlePrice());//订单金额
				resultMap.put("memberMobile", order.getMobile());
				resultMap.put("remark", order.getRemark());//订单标题
				resultMap.put("orderTime",DateUtils.format(order.getOrderTime(),DateUtils.DATETIME_FORMAT));
				resultMap.put("payType",order.getPayType());
				resultMap.put("cashierName", order.getCashierUsername());
			}
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
					"查询订单详情成功！", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new APISystemException("获取一点管家订单详情-系统异常", e);
		}
	}
}
