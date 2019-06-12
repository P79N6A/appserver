package com.idcq.idianmgr.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopRsrcDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.idianmgr.common.MgrCodeConst;
import com.idcq.idianmgr.dao.order.IShopOrderDao;
import com.idcq.idianmgr.dto.order.OfflineOrderDto;
import com.idcq.idianmgr.dto.order.ShopOrderDetailDto;
import com.idcq.idianmgr.dto.order.ShopOrderDto;

/**
 * 店铺订单实现类
 * @author shengzhipeng
 * @date:2015年7月30日 下午2:12:43
 */

@Service
public class ShopOrderServcieImpl implements IShopOrderServcie{

	@Autowired
	private IShopOrderDao shopOrderDao;
	
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IOrderLogDao orderLogDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private ICollectService collectService;
	
	@Autowired
	private IPacketDao packetDao;
	
	@Autowired
	private IOrderGoodsDao orderGoodsDao;
	
	@Autowired
	private IXorderDao xorderDao;
	
	@Autowired
	private IShopDao shopDao;
	
	@Autowired
	private IShopRsrcDao shopRsrcDao; 
	
	@Autowired
	private IOrderShopRsrcDao osrDao;
	
	@Autowired
	private IOrderShopRsrcDao orderShopRsrcDao;
	
	@Autowired
	private IGoodsCategoryDao goodsCategoryDao;
	
	@Autowired
	private IShopTechnicianService shopTechnicianService;
	
	
	public PageModel getShopOrders(Map map, int page, int pageSize)
			throws Exception {
		
		List<ShopOrderDto> list = this.shopOrderDao.getShopOrders(map, page,
				pageSize);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ShopOrderDto shopOrderDto : list) {
				String logoUrl = shopOrderDao.getGoodsLogoByOrderId(shopOrderDto.getOrderId());
				shopOrderDto.setGoodsLogo(FdfsUtil.getFileProxyPath(logoUrl));
			}
		}
		int totalItem = this.shopOrderDao.getShopOrdersCount(map);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(totalItem);
		pm.setList(list);
		return pm;
	}
	
	public List<Map<String, Object>> getShopOrdersNumber(Long shopId) throws Exception {
		
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();

		/*
		 * queryStatus 对应的orderStatus 
		 * 0（待确认）：  9（待确认） 已支付 
		 * 1（待开单） ：0（已预定）
		 * 2（服务中） ：1（已开单） 
		 * 3（退订审核）  ：  4 （退单中 ）
		 * 4（完成） ： 3（已完成）、5（已退订）
		 */
		List<Map<String, Object>> list = this.shopOrderDao.getShopOrdersNumber(shopId);

		//需要过滤的待确认订单个数
		int filterNum = this.shopOrderDao.getNotPayOrderNum(shopId, CommonConst.ORDER_STS_DQR);
		
		// 5种状态，可以通过一个数组来存在对应的个数
		int[] total = new int[5];
		Integer orderStatus;
		long num;
		for (Map<String, Object> param : list) {
			orderStatus = Integer.valueOf(param.get("orderStatus")+"");
			num = (long) param.get("number");
			if (CommonConst.ORDER_STS_DQR == orderStatus) {
				total[0] += num;
			} else if (CommonConst.ORDER_STS_YYD == orderStatus) {
				total[1] += num;
			} else if (CommonConst.ORDER_STS_YKD == orderStatus) {
				total[2] += num;
			} else if (CommonConst.ORDER_STS_TDZ == orderStatus) {
				total[3] += num;
			}else if (CommonConst.ORDER_STS_YJZ == orderStatus || CommonConst.ORDER_STS_YTD == orderStatus) {
				total[4] += num;
			}
		}
		for (int i = 0; i < 5; i++) {
			Map<String, Object> tmp = new HashMap<String, Object>();
			if(i == 0) {
				//待确认需要过滤的个数
				total[i] -= filterNum;
			}
			tmp.put("queryStatus", i);
			tmp.put("orderNum", total[i]);
			resList.add(tmp);
		}

		return resList;
	}

	public Map updateOrderStatus(Long userId, String orderId, String operateType) throws Exception {
		OrderDto orderDto = orderDao.getOrderMainById(orderId);
		CommonValidUtil.validObjectNull(orderDto,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER);
		ShopDto shopDto = shopDao.getNormalShopById(orderDto.getShopId());
		CommonValidUtil.validObjectNull(shopDto,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		UserDto user = userDao.getUserById(userId);
		if(null == user || (null != shopDto.getPrincipalId() && !userId.equals(shopDto.getPrincipalId()))) {
			//如果用户在会员表中不存在，则userId有可能是雇员
			int count = shopDao.queryShopEmplExists(shopDto.getShopId(), userId);
			if (0 == count) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			}
		} 
		
		String shopName = shopDto.getShopName();
		Integer orderStatus = orderDto.getOrderStatus();
		Integer serviceType = orderDto.getOrderServiceType();
		OrderDto orderInfo = new OrderDto();
		String remark = null;
		String refuseReason = null;
		Integer updateStatus = orderStatus;
		//用来标识这个方法是否需要更新订单状态
		boolean updateFlag = false;

		StringBuilder content = new StringBuilder();
		if (CommonConst.ORDER_STS_DQR == orderStatus) {
			if(CommonConst.OPERATE_TYPE_JD.equals(operateType)) {
				//已预订
				updateStatus = CommonConst.ORDER_STS_YYD;
				remark = "商家接单操作";
				updateFlag = true;
				String reserveTime = DateUtils.format(orderDto.getServiceTimeFrom(), DateUtils.DATETIME_FORMAT1);;
				Map<String, Object> resourceMap =  orderShopRsrcDao.getOrderResourceByOrderId(orderId);
				if(null != resourceMap){
					Date startTime = (Date) resourceMap.get("startTime");
					Date reserveDate = (Date) resourceMap.get("reserveDate");
					String startTimeStr = DateUtils.format(startTime, DateUtils.TIME_FORMAT2);
					String sreserveDateStr = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT);
					reserveTime = sreserveDateStr + CommonConst.SPACE + startTimeStr;
				} 
				
				if (null != serviceType && serviceType == CommonConst.ORDER_SERVICE_TYPE_STORE) {
					//到店服务
					content.append("预约成功！").append(shopName);
					content.append("已接受您的预约，请于").append(reserveTime);
					content.append("前往消费");
					
				} else {
					//上门服务
					content.append("预约成功！").append(shopName);
					content.append("已接受您的预约，将于").append(reserveTime);
					content.append("为您提供上门服务，请安排好您的时间，保持联系方式通畅。");
				}
				
				
			} else if (CommonConst.OPERATE_TYPE_JJJD.equals(operateType)) {
				//已退单
				updateStatus = CommonConst.ORDER_STS_YTD;
				remark = "商家拒绝接单操作";
				refuseReason = "商家不接单";
				//直接调用退单接口
				collectService.dealRefund(orderDto);
				updateFlag = true;
				content.append("预约失败！").append(shopName).append("暂时无法为您提供服务，已拒绝了您的预约。您支付的款项将退回到您的账户余额中。");
			}
		} else if (CommonConst.ORDER_STS_YYD == orderStatus && CommonConst.OPERATE_TYPE_KD.equals(operateType)) {
			 //已开单
			updateStatus = CommonConst.ORDER_STS_YKD;
			
			//开单时将技师状态修改
			shopTechnicianService.updateTechnicianWorKStatusByOrderId(orderId, CommonConst.TECH_STATUS_FWZ);
			remark = "商家开单操作";
			updateFlag = true;
			if (null != serviceType && serviceType == CommonConst.ORDER_SERVICE_TYPE_DOOR) {
				//到店服务
				content.append("欢迎光临，").append(shopName).append("已为您安排了服务");
			} else {
				//上门服务
				content.append(shopName).append("已为您安排了服务");
			}
		} else if (CommonConst.ORDER_STS_YKD == orderStatus && CommonConst.OPERATE_TYPE_JZ.equals(operateType)) {
			//已完成
			updateStatus = CommonConst.ORDER_STS_YJZ;
			remark = "商家结账操作";
			updateFlag = true;
			content.append("谢谢惠顾，您本次在").append(shopName).append("的消费已经结账。");
		} else if (CommonConst.ORDER_STS_TDZ == orderStatus) {
			if(CommonConst.OPERATE_TYPE_TYTD.equals(operateType)) {
				//已退单
				updateStatus = CommonConst.ORDER_STS_YTD;
				refuseReason = "用户取消订单";
				remark = "商家同意退单操作";
				collectService.dealRefund(orderDto);
				updateFlag = true;
				content.append("退订成功！").append(shopName).append("已接受您的退订申请，已支付的款项将退回到您的账户余额中。");
			} else if (CommonConst.OPERATE_TYPE_JJTD.equals(operateType)) {
				//已预订
				updateStatus = CommonConst.ORDER_STS_YYD;
				remark = "商家拒绝退单操作";
				updateFlag = true;
				content.append("退订失败！").append(shopName).append("拒绝了您的退订申请。");
			}
		} 
		if (!updateFlag) {
			throw new ValidateException(CodeConst.CODE_ORDER_NOT_UPDATE, CodeConst.MSG_ORDER_NOT_UPDATE);
		}
		orderInfo.setOrderId(orderId);
		orderInfo.setOrderStatus(updateStatus);
		orderInfo.setRefuseReason(refuseReason);
		orderInfo.setPayStatus(orderDto.getPayStatus());
		
		//更新订单状态和记录日志
		collectService.dealOrderStatus(orderInfo, userId, remark);
		if(CommonConst.ORDER_STS_YJZ == updateStatus) {
			//结账释放资源
			osrDao.updateStatusByOrderId(orderId, CommonConst.OSRESOURCE_STATUS_INVALID);
			
			shopTechnicianService.updateTechnicianWorKStatusByOrderId(orderId, CommonConst.TECH_STATUS_KXZ);
			
			//结账，因为传进来的userId为操作人的id，不允许使用该id结算
			OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, 0);
		}
		Map map = new HashMap();
		map.put("content", content);
		map.put("userId", orderDto.getUserId());
		
		return map;
	}

	public ShopOrderDetailDto getShopOrderDetailById(String orderId) throws Exception {
		// 获取订单信息
		ShopOrderDto pShopOrder = this.shopOrderDao.getShopOrderById(orderId);
		CommonValidUtil.validObjectNull(pShopOrder,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_ORDER);
		// 获取订单商品列表
		List<Map> gList = this.orderGoodsDao.getOrderGoodsListById(orderId);	
		ShopOrderDetailDto orderDetail = new ShopOrderDetailDto();
		PropertyUtils.copyProperties(orderDetail, pShopOrder);
		if(gList != null && gList.size() > 0){
			String url = null;
			String proxyServer = FdfsUtil.getFileProxyServer();
			for(Map e : gList){
				// logo拼上文件服务器地址
				url = (String)e.get("goodsImg");
				if(!StringUtils.isBlank(url)){
					e.put("goodsImg", FdfsUtil.getFileFQN(proxyServer, url));
				}
				e.put("shopId", pShopOrder.getShopId());
				e.put("shopName", pShopOrder.getShopName());
			}
			orderDetail.setGoods(gList);
		}
		// 获取订单支付信息
		// 已支付金额
		BigDecimal payedAmount = this.packetDao.queryOrderPayAmount(orderId,CommonConst.PAY_TYPE_SINGLE);
		// 订单总价
		BigDecimal settlePrice = packetDao.queryOrderAmount(orderId);
		orderDetail.setPayedAmount(payedAmount.doubleValue());
		// 未支付金额
		Double notPayedAmount = null;
		if(settlePrice != null && settlePrice.doubleValue() > payedAmount.doubleValue()){
			notPayedAmount = settlePrice.subtract(payedAmount).doubleValue();
		}else{
			notPayedAmount = 0D;
		}
		orderDetail.setNotPayedAmount(notPayedAmount < 0 ? 0 : notPayedAmount);
		return orderDetail;
	}

	
	public List<Map> getShopOfflineOrders(Long shopId, Integer day) throws Exception {
		
		List<Map> list = this.xorderDao.getShopOfflineOrders(shopId, day);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map map : list) {
				String logoUrl = xorderDao.getGoodsLogoByXorderId(String.valueOf(map.get("xOrderId")));
				map.put("goodsLogo", FdfsUtil.getFileProxyPath(logoUrl));
			}
		}
		
		return list;
	}

	public List<Map> getShopCategoryOrders(Long shopId, Long categoryId,
			String dates) throws Exception {
		
		//封装时间查询
		String[] date = null;
		if(StringUtils.isNotBlank(dates)) {
			date = dates.split(CommonConst.COMMA_SEPARATOR);
		}
		List<Map> list = this.shopOrderDao.getShopCategoryOrders(shopId, categoryId, date);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map map : list) {
				String logoUrl = shopOrderDao.getGoodsLogoByOrderId(String.valueOf(map.get("orderId")));
				map.put("goodsLogo", FdfsUtil.getFileProxyPath(logoUrl));
			}
		}
		return list;
	}

	public String OfflinePlaceOrder(OfflineOrderDto olOrder) throws Exception {
		// 数据校验
		OfflinePlaceOrderValid(olOrder);
		
		Long shopId = olOrder.getShopId();
		String userName = olOrder.getUserName();
		String mobile = olOrder.getMobile();
		Long resourceId = olOrder.getResourceId();
		Date serviceTimeFrom = olOrder.getServiceTimeFrom();
		Date serviceTimeTo = olOrder.getServiceTimeTo();
		Double orderTotalPrice = olOrder.getOrderTotalPrice();
		String resourceName = olOrder.getResourceName();
		
		// 订单主表入库
		XorderDto xorderDto = new XorderDto();
		xorderDto.setOrderType(CommonConst.PAY_STATUS_NOT_PAY);
		xorderDto.setOrderTime(new Date());
		xorderDto.setOrderServiceType(0);
		xorderDto.setShopId(shopId);
		xorderDto.setUserInfo(userName);
		xorderDto.setServiceTimeFrom(serviceTimeFrom);
		xorderDto.setServiceTimeTo(serviceTimeTo);
		xorderDto.setOrderTotalPrice(orderTotalPrice);
		xorderDto.setMobile(mobile);
		xorderDto.setOrderTitle(getOrderTitle(resourceId));
		xorderDto.setOrderStatus(CommonConst.ORDER_STS_YYD);
		// 生成场地订单号
		String orderId = FieldGenerateUtil.generate32bitOrderId(shopId);
		xorderDto.setXorderId(orderId);
		this.xorderDao.addXorderDto(xorderDto);
		// 订单预定资源数据入库
		OrderShopRsrcDto osr = new OrderShopRsrcDto();
		osr.setOrderId(orderId);
		osr.setShopId(shopId);
		osr.setUserName(userName);
		osr.setMobile(mobile);
		osr.setReserveResourceDate(serviceTimeFrom);
		osr.setBizId(resourceId);
		osr.setBizName(resourceName);
		osr.setResourceType("3");
		osr.setStatus(1);
		osr.setResourceNumber(1);
		osr.setCreateTime(new Date());
		osr.setStartTime(DateUtils.format(serviceTimeFrom, DateUtils.TIME_FORMAT2));
		osr.setEndTime(DateUtils.format(serviceTimeTo, DateUtils.TIME_FORMAT2));
		this.osrDao.saveOrderShopRsrc(osr);
		// 返回订单号
		return orderId;
	}
	
	/**
	 * 商铺线下订单获取订单标题
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	private String getOrderTitle(Long resourceId) throws Exception {
		// 若是场馆预定，获取商品分类作为标题
		String orderTitle = null;
		Map<String, Object> rsrMap = this.shopRsrcDao.getCategoryIdAndRsrNameByRsrId(resourceId);
		List<String> categoryList = new ArrayList<String>();
		Long categoryId = null;
		String rsrName = null;
		if (rsrMap != null) {
			categoryId = (Long) rsrMap.get("categoryId");
			rsrName = (String) rsrMap.get("resourceName");
			categoryList.add(rsrName);
		}
		// 商品分类列表（包含层级）
		Map<String, Object> map = null;
		// 循环获取商品分类名称（包含上下级）
		do {
			map = this.goodsCategoryDao.getGoodsCategoryById(categoryId);
			categoryId = null;
			if (map != null) {
				categoryList.add((String) map.get("categoryName"));
				categoryId = (Long) map.get("pId");
			}
		} while (categoryId != null && categoryId > 0L);
		// 倒排商品列表，以空格隔开作为订单标题
		if (categoryList != null && categoryList.size() > 0) {
			StringBuilder orderTitle2 = new StringBuilder();
			for (int i = categoryList.size() - 1; i >= 0; i--) {
				orderTitle2.append(categoryList.get(i));
				orderTitle2.append(" ");
			}
			if (orderTitle2.length() > 0) {
				orderTitle = orderTitle2.substring(0,orderTitle2.lastIndexOf(" "));
			}
		}
		return orderTitle;
	}
	
	/**
	 * 商家线下下订单数据校验
	 * @param olOrder
	 * @throws Exception
	 */
	private void OfflinePlaceOrderValid(OfflineOrderDto olOrder) throws Exception{
		Long shopId = olOrder.getShopId();
		String userName = olOrder.getUserName();
		String mobile = olOrder.getMobile();
		Long resourceId = olOrder.getResourceId();
		Date serviceTimeFrom = olOrder.getServiceTimeFrom();
		Date serviceTimeTo = olOrder.getServiceTimeTo();
		Double orderTotalPrice = olOrder.getOrderTotalPrice();
		
		// 商铺ID必填及存在性
		CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		Integer flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 用户名必填
		CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USERNAME);
		// 手机号码必填
		CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
		// 资源ID必填及存在性
		CommonValidUtil.validLongNull(resourceId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_RESOURCE_ID);
		CommonValidUtil.validPositLong(resourceId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_RESOURCE_ID);
		flag = this.shopRsrcDao.queryShopResourceExists(resourceId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RESOURCE);
		String resourceName = this.shopRsrcDao.getShopResourceName(resourceId);
		olOrder.setResourceName(resourceName);
		// 服务开始时间必填
		CommonValidUtil.validObjectNull(serviceTimeFrom,  CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STARTTIME);
		// 服务结束时间必填
		CommonValidUtil.validObjectNull(serviceTimeTo,  CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STOPTIME);
		// 订单总价必填
		CommonValidUtil.validDoubleNullZero(orderTotalPrice, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OTOTAL_PRICE);
				
	}

	public int cancelOfflineOrder(HttpServletRequest request) throws Exception {
		String orderId = RequestUtils.getQueryParam(request, "xOrderId");
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String handerId = RequestUtils.getQueryParam(request, "handerId");
		// 订单号必填及存在性
		CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
//		int flag = this.orderDao.queryOrderExists(orderId);
		int flag = this.xorderDao.queryXorderExists(orderId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER);
		// 商铺ID必填及存在性
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
		CommonValidUtil.validPositLong(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		Long shopId = Long.valueOf(shopIdStr);
		flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 操作人必填及存在性及权限
		Long userId = null;
		if(!StringUtils.isBlank(handerId)){
			CommonValidUtil.validPositLong(handerId, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_HANDERID);
			userId = Long.valueOf(handerId);
		}
		XorderDto order = this.xorderDao.getXorderSimple(orderId);
		// 订单主表信息置为无效
		this.xorderDao.canclXorder(orderId);
		// 预定资源信息置为无效
		this.osrDao.updateStatusByOrderId(orderId, 0);
		// 生成订单日志
		saveOrderLog(order,"订单取消",userId);
		return 1;
	}
	
	/**
	 * 保存订单修改日志
	 * 
	 * @param order
	 * @param remark
	 * @throws Exception
	 */
	private void saveOrderLog(XorderDto order, String remark,Long userId) throws Exception {
		OrderLogDto orderLogDto = new OrderLogDto();
		orderLogDto.setOrderId(order.getXorderId());
		orderLogDto.setUserId(userId);
		orderLogDto.setPayStatus(order.getPayStatus());
		orderLogDto.setOrderStatus(order.getOrderStatus());
		orderLogDto.setRemark(remark);
		orderLogDto.setLastUpdateTime(new Date());
		orderLogDao.saveOrderLog(orderLogDto);
	}

}
