package com.idcq.idianmgr.controller.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.idianmgr.common.MgrCodeConst;
import com.idcq.idianmgr.dto.order.OfflineOrderDto;
import com.idcq.idianmgr.dto.order.ShopOrderDetailDto;
import com.idcq.idianmgr.service.order.IShopOrderServcie;

/**
 * 商铺订单控制类
 * @author shengzhipeng
 * @date:2015年7月30日 下午1:52:34
 */

@Controller
public class ShopOrderController {
	
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private IShopOrderServcie shopOrderServcie;
	
	@Autowired
	public IShopServcie shopServcie;
	
	@Autowired
	ICommonService commonService;
	
	@Autowired
	IOrderServcie orderService;

	/**
	 * MO2：获取商铺订单列表接口
	 * $1dcp_Home/interface/myorder/getShopOrders
	 * @Function: com.idcq.idianmgr.controller.order.ShopOrderController.getShopOrders
	 * @Description:获取商铺订单列表默认按照下单时间升序。
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:szp
	 * @date:2015年7月30日 下午1:57:15
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日       shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value="/myorder/getShopOrders", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopOrders(HttpServletRequest request) {
		try {
			logger.info("分页获取商铺订单列表-start");
			String pageNO = RequestUtils.getQueryParam(request,
					CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request,
					CommonConst.PAGE_SIZE);
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String status = RequestUtils.getQueryParam(request, "queryStatus");
			
			//此号码不一定是会员的手机号码
			String mobile = RequestUtils.getQueryParam(request, "mobile");

			//检验shopId是否为空
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_MISS_SHOP_ID);
			
			// 商铺存在性
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			ShopDto pShop = this.shopServcie.getShopMainOfCacheById(shopId);
			CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			
			Integer orderStatus = null;
			if (StringUtils.isBlank(status)) {
				// 默认值
				orderStatus = 0;
			} else {
				orderStatus = NumberUtil.strToNum(status, "queryStatus");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("orderStatus", orderStatus);
			map.put("mobile", mobile);
			/*
			 * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
			 */
			PageModel pageModel = this.shopOrderServcie.getShopOrders(map,
					PageModel.handPageNo(pageNO),
					PageModel.handPageSize(pageSize));

			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					CodeConst.MSG_ORDERS_SUCCESS, msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺订单列表-系统异常", e);
			throw new APISystemException("获取商铺订单列表-系统异常", e);
		}
	}
	
	/**
	 * MO3：订单操作接口
	 * $1dcp_Home/interface/order/updateOrderStatus
	 * @Function: com.idcq.idianmgr.controller.order.ShopOrderController.updateOrderStatus
	 * @Description:
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:41:45
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value = "/order/updateOrderStatus", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto updateOrderStatus(HttpServletRequest request) {
		try {
			logger.info("订单操作接口-start"); 
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			String operateType = RequestUtils.getQueryParam(request, "operateType");

			// 检验userId是否为空
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validStrNull(orderId,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_ORDERID);
			CommonValidUtil.validStrNull(operateType,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					"operateType不能为空");
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			
			//操作订单
			Map map = this.shopOrderServcie.updateOrderStatus(userId, orderId, operateType);
			if (null != map) {
				commonService.pushUserMsg(CommonConst.ACTION_ORDER_DATA_UPDATE, String.valueOf(map.get("content")), Long.valueOf(String.valueOf(map.get("userId"))), CommonConst.USER_TYPE_ZREO);
			}

			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					"更新订单状态成功!", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单操作接口-系统异常", e);
			throw new APISystemException("订单操作接口-系统异常", e);
		}
	}
	
	/**
	 * MO4：统计订单数量接口
	 * $1dcp_Home/interface/myorder/getShopOrdersCount
	 * @Function: com.idcq.idianmgr.controller.order.ShopOrderController.getShopOrdersNumber
	 * @Description: 根据商家id统计商家订单数量，如果是待确认状态时，未付款的不统计进来，商家删除了的订单也不统计进来
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:07:56
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value = "/myorder/getShopOrdersCount", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopOrdersNumber(HttpServletRequest request) {
		try {
			logger.info("统计商家订单数量-start"); 
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

			//检验shopId是否为空
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			
			//校验商铺
			ShopDto pShop = this.shopServcie.getShopMainOfCacheById(shopId);
			CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			// 获取店铺订单数量统计
			List<Map<String, Object>> list = this.shopOrderServcie.getShopOrdersNumber(shopId);

			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					CodeConst.MSG_ORDERNUM_SUCCESS, list);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计商家订单个数-系统异常", e);
			throw new APISystemException("统计商家订单个数-系统异常", e);
		}
	}
	
	/**
	 * 获取订单详情（丽人）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order/getShopOrderDetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getShopOrderDetail(HttpServletRequest request) {
		try {
			logger.info("获取订单详情-start");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
			ShopOrderDetailDto detail = this.shopOrderServcie.getShopOrderDetailById(orderId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取订单详情成功！", detail);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单详情-系统异常", e);
			throw new APISystemException("获取订单详情-系统异常", e);
		}
	}
	
	/**
	 * 商家线下下单（场地类）
	 * <p>订单数据入库1dcq_xorder表
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/order/offlineOrder", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8",consumes = "application/json")
	@ResponseBody
	public Object offlineOrder(HttpEntity<String> entity) {
		try {
			logger.info("商家线下下单-start");
			OfflineOrderDto olOrder = (OfflineOrderDto) JacksonUtil.postJsonToObj(entity,OfflineOrderDto.class,DateUtils.DATETIME_FORMAT1);
			String orderId = this.shopOrderServcie.OfflinePlaceOrder(olOrder);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("xOrderId",orderId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商家线下下单成功！", map);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商家线下下单-系统异常", e);
			throw new APISystemException("商家线下下单-系统异常", e);
		}
	}
	
	/**
	 * 商家取消线下订单（场地类）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order/cancelOfflineOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object cancelOfflineOrder(HttpServletRequest request) {
		try {
			logger.info("商家取消线下订单-start");
			this.shopOrderServcie.cancelOfflineOrder(request);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商家取消线下订单成功！", null);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商家取消线下订单-系统异常", e);
			throw new APISystemException("商家取消线下订单-系统异常", e);
		}
	}
	
	/**
	 * MO5：获取商铺线下订单列表接口 (场地类)
	 * $1dcp_Home/interface/order/getShopOfflineOrders
	 * @Function: com.idcq.idianmgr.controller.order.ShopOrderController.getShopOfflineOrders
	 * @Description:获取商铺线下订单列表，默认按照下单时间降序
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月31日 上午9:11:55
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value = "/order/getShopOfflineOrders", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopOfflineOrders(HttpServletRequest request) {
		try {
			logger.info("获取商铺线下订单列表-start"); 
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

			//检验shopId是否为空
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			
			//校验商铺
			ShopDto pShop = this.shopServcie.getShopMainOfCacheById(shopId);
			CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			// 获取商铺线下订单列表
			List<Map> list = this.shopOrderServcie.getShopOfflineOrders(shopId, MgrCodeConst.QUERY_DAY);

			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					"获取商铺线下订单列表成功！", list);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺线下订单列表-系统异常", e);
			throw new APISystemException("获取商铺线下订单列表-系统异常", e);
		}
	}
	
	/**
	 * M06: 获取商铺分类下的订单列表接口 
	 * $1dcp_Home/interface/order/getShopCategoryOrders
	 * @Function: com.idcq.idianmgr.controller.order.ShopOrderController.getShopCategoryOrders
	 * @Description:
	 *
	 * @param request
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月31日 下午3:14:12
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    shengzhipeng       v1.0.0         create
	 */
	@RequestMapping(value = "/order/getShopCategoryOrders", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopCategoryOrders(HttpServletRequest request) {
		try {
			logger.info("获取商铺分类下的订单列表-start"); 
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String categoryIdStr = RequestUtils.getQueryParam(request, "categoryId");
			String dates = RequestUtils.getQueryParam(request, "dates");
			
			//检验shopId是否为空
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			CommonValidUtil.validStrNull(categoryIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					"categoryId不能为空");
			Long categoryId = NumberUtil.strToLong(categoryIdStr, "categoryId");
			ShopDto pShop = this.shopServcie.getShopMainOfCacheById(shopId);
			CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			// 获取商铺线下订单列表
			List<Map> list = this.shopOrderServcie.getShopCategoryOrders(shopId, categoryId, dates);

			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					"获取商铺分类下的订单列表成功！", list);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺分类下的订单列表-系统异常", e);
			throw new APISystemException("获取商铺分类下的订单列表-系统异常", e);
		}
	}
	/**
	 * CO12：获取订单列表接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/getAllOrderList", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getAllOrderList(HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("获取订单列表接口    -start");
			//shopid
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);

			String dateTypeStr = RequestUtils.getQueryParam(request, "dateType");
			String startDate = RequestUtils.getQueryParam(request, "startDate");
			String endDate = RequestUtils.getQueryParam(request, "endDate");
			String orderStatus = RequestUtils.getQueryParam(request, "orderStatus");
			String orderTransactionTypeStr = RequestUtils.getQueryParam(request, "orderTransactionType");
			String payTypeStr = RequestUtils.getQueryParam(request, "payType");
			String billerIdStr = RequestUtils.getQueryParam(request, "billerId");
			String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
			
			int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Integer dateType = 1;
			if (!StringUtils.isEmpty(dateTypeStr)) {
				dateType = CommonValidUtil.validStrIntFmt(dateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "查询时间字段类型有误");
				if (dateType == null || !(dateType == 1 || dateType == 2 || dateType == 3 )) {
					dateType = 1;
				}
			}
			CommonValidUtil.validStrNull(startDate, CodeConst.CODE_PARAMETER_NOT_NULL, "查询开始日期不能为空");
			CommonValidUtil.validDateTimeFormat(startDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID, "查询开始日期格式错误");
			if (!StringUtils.isEmpty(endDate)) {
				CommonValidUtil.validDateTimeFormat(endDate, DateUtils.DATE_FORMAT, CodeConst.CODE_PARAMETER_NOT_VALID, "查询截止日期格式错误");
			}
			List<Integer> orderStatuss = null;
			if (!StringUtils.isEmpty(orderStatus)) {
				orderStatuss = new ArrayList<Integer>();
				String[] strs = orderStatus.split(CommonConst.COMMA_SEPARATOR);
				for (int i = 0; i < strs.length; i++) {
					String str = strs[i];
					orderStatuss.add(CommonValidUtil.validStrIntFmt(str, CodeConst.CODE_PARAMETER_NOT_VALID, "订单状态类型有误"));
				}
			}
			Integer orderTransactionType = null;
			if (!StringUtils.isEmpty(orderTransactionTypeStr)) {
				orderTransactionType = CommonValidUtil.validStrIntFmt(orderTransactionTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "订单交易状态类型有误");
			}
			Integer payType = null;
			if (!StringUtils.isEmpty(payTypeStr)) {
				payType = CommonValidUtil.validStrIntFmt(payTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "支付方式类型有误");
			}
			Integer billerId = null;
			if (!StringUtils.isEmpty(billerIdStr)) {
				billerId = CommonValidUtil.validStrIntFmt(billerIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "导购员类型有误");
			}

			Integer cashierId = null;
			if (!StringUtils.isEmpty(cashierIdStr)) {
				cashierId = CommonValidUtil.validStrIntFmt(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "收银员编号类型有误");
			}
			Map<String,Object> resultMap = orderService.getAllOrderList(getIdListsByHeadShopId(shopId),dateType,startDate,endDate,orderStatuss,orderTransactionType,payType,billerId,cashierId,pNo,pSize,null,null,null);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询订单列表成功！", resultMap,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			logger.error("获取订单列表接口    -异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取订单列表接口    -系统异常", e);
			throw new APISystemException("获取订单列表接口    -系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
    private List<Long> getIdListsByHeadShopId(Long headShopId) throws Exception{
    	
    	List<Long> shopIdList = new ArrayList<Long>();
    	
        List<ShopDto> shopList = shopServcie.getShopListByHeadShopId(headShopId);
        if(CollectionUtils.isNotEmpty(shopList)){
        	for (ShopDto shopDto : shopList) {
        		shopIdList.add(shopDto.getShopId());
			}
        }
        shopIdList.add(headShopId);
		return shopIdList;

    }
}
