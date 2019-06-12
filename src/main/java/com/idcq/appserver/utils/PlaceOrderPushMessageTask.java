package com.idcq.appserver.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dao.user.IUserAddressDao;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.BookInfo;
import com.idcq.appserver.dto.order.DataPushJsonDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.SeatInfo;
import com.idcq.appserver.dto.user.UserAddressDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IPushService;

/**
 * 下订单推送消息工具
 * 
 * @author chenyongxin
 * 
 */
public class PlaceOrderPushMessageTask implements Runnable {

	private static Logger logger = Logger
			.getLogger(PlaceOrderPushMessageTask.class);
	private OrderDto order;
	
	public PlaceOrderPushMessageTask(OrderDto order) {
		super();
		this.order = order;
	}
	/**
	 * 下订单推送消息给商铺
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public void detailSingleOrder(OrderDto od) throws Exception {
		logger.info("推送订单消息-start");
		logger.info("订单实体："+od);
		IMemberServcie memberServcie = BeanFactory
				.getBean(IMemberServcie.class);
		IPushService pushService = BeanFactory.getBean(IPushService.class);
		IOrderShopRsrcDao orderShopRsrcDao = BeanFactory.getBean(IOrderShopRsrcDao.class);
		// 全额支付才执行
		// 推送给商铺
		if(null!=od){
			List<OrderGoodsDto> listGoods = od.getGoods();
			DataPushJsonDto data = new DataPushJsonDto();
			// 商品列表
			if(null!=listGoods&&0!=listGoods.size()){
				List<BookInfo> listBook = deailBookInfo(listGoods);
				// 封装data
				data.setBookInfo(listBook);
			}
			Long userId = od.getUserId();
			String orderId = od.getOrderId();
			UserDto user = memberServcie.getUserByUserId(userId);
			CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			String mobile = null;
			if(user.getIsMember() == CommonConst.USER_IS_MEMBER) {
			    mobile = user.getMobile();
			}
			data.setId(orderId);
			data.setMobile(mobile);
			String orderTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
			data.setTime(orderTime);
			data.setAdvance(od.getPrepayMoney());
			//用户备注 6.24
			data.setUserRemark(od.getUserRemark());
			//7.3增加订单状态
			data.setOrderStatus(od.getOrderStatus());
			data.setClientSystemType(od.getClientSystemType());
			Double memberDiscount  = od.getMemberDiscount();
			//如果折扣等于null或者等于0，则传1给收银机
			if(null!=memberDiscount&&0!=memberDiscount){
				data.setDiscount(memberDiscount);//TODO 会员折扣,暂时无折扣
			}
			else{
				data.setDiscount(1.0);
			}
			Integer payStatus = od.getPayStatus();
			//支付状态
			if(null!=payStatus){
				data.setPayStatus(payStatus);
			}
			data.setContactPhone(mobile);
			Integer isWm = od.getOrderSceneType();
			// 是否外卖
			if (null!=isWm&&2 == isWm) {
				data.setIsWm(true);
			} else {
				data.setIsWm(false);
				if(null!=isWm&&3==isWm){//如果场景等于3（服务订单）时候，还需要判断订单服务类型
					//上门服务：1
					if(1==od.getOrderServiceType()){
						data.setIsWm(true);
					}
				}
			}
			
			if(null!=od.getAddressId()){
				Long addressId = od.getAddressId();
				// 外卖地址 TODO 外卖地址
				//String inputAddress = getInputAddress(addressId);
				Map<String, Object> addressMap = getInputAddress(addressId);
				String inputAddress = (String) addressMap.get("inputAddress");
				String contactPhone = (String) addressMap.get("contactPhone");
				String username = (String) addressMap.get("uname");
				//外卖地址
				data.setAddress(inputAddress);
				//联系人电话
				data.setContactPhone(contactPhone);
				//联系人姓名
				data.setpName(username);
			}
			// TODO 人数 资源类型
			data.setpNum(order.getPeopleNumber()==null ? 0 : order.getPeopleNumber());
			/** TODO 5-20变更
			 * 1、通过orderId查询1dcq_order_shop_resource(订单资源表)得到inteval_id、book_id
			 * 2、根据inteval_id查询1dcq_shop_time_inteval(商铺可用时段表)==> start_time、end_time 
			 * 3、通过book_rule_id查询1dcq_book_rule(商铺预定规则表)===>book_type
			 */
//			List<Map<String, Object>> listResource  = orderShopRsrcDao.getOrderShopResourceByOrderId(orderId);
//			if(CollectionUtils.isNotEmpty(listResource)){  TODO 7.24变更 屏蔽 时间段表废弃，数据目前只存在order_shop_resouce
//				Map<String,Object> map = listResource.get(0);
//				Long intevalId = (Long) map.get("intevalId");
//				Map<String,Object> mapTime = orderShopRsrcDao.getIntevalTimeById(intevalId);
//				if(null!=mapTime&&0!=mapTime.size()){
//					Date timeFrom = (Date) mapTime.get("startTime");
//					Date timeTo = (Date) mapTime.get("endTime");
//					if(null!=timeFrom){
//						String timeFromStr = DateUtils.format(timeFrom, DateUtils.TIME_FORMAT);
//						data.setEatTimeFrom(timeFromStr);
//					}
//					if(null!=timeTo){
//						String timeToStr = DateUtils.format(timeTo, DateUtils.TIME_FORMAT);
//						data.setEatTimeTo(timeToStr);
//					}
//				}
//			}
//			else{
//				logger.info("根据订单id:"+orderId+",查询订单资源表信息为空");
//			}
			Map<String, Object> resourceMap  = orderShopRsrcDao.getOrderResourceByOrderId(orderId);
			String startTimeStr = "";
			if(null!=resourceMap&&0!=resourceMap.size()){
				Date startTime = (Date) resourceMap.get("startTime");
				Date reserveDate = (Date) resourceMap.get("reserveDate");
				String reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT);
				startTimeStr = reserveDateStr+" "+DateUtils.format(startTime, DateUtils.TIME_FORMAT2);
			}
			//TODO 目前下单开始时间和结束一样，因此此处时间都取一个值
			data.setEatTimeFrom(startTimeStr);
			data.setEatTimeTo(startTimeStr);
			if(CommonConst.PLACE_ORDER_LIVE==od.getOrderSceneType()){
				//资源类型、预订数量
				List<SeatInfo> listSeatInfo  = deailShopResource(resourceMap);
				data.setSeatInfo(listSeatInfo);
			}
			if(null!=od.getOrderSceneType()){
				data.setOrderSceneType(od.getOrderSceneType());
			}
			Long resourceId = orderShopRsrcDao.getResourceIdByOrderId(orderId);
			//资源id
			if(null!=resourceId&&0!=resourceId){
				data.setSeatId(resourceId);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopId", od.getShopId());
			jsonObject.put("action", "book");
			jsonObject.put("data", data);
			// 推送给商铺
			PushDto pushDto = new PushDto();
			pushDto.setAction("book");
			pushDto.setContent(jsonObject.toString());
			pushDto.setShopId(od.getShopId());
			pushService.pushInfoToShop2(pushDto);
			
		}
		logger.info("推送订单消息-end");
	}
	/**
	 * 获取外卖地址
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> getInputAddress(Long addressId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		IUserAddressDao userAddressDao = BeanFactory.getBean(IUserAddressDao.class);
		UserAddressDto  userAddressDto  = userAddressDao.getAddressDetialById(addressId);
		StringBuilder inputAddress = new StringBuilder();
		String mobile = "";
		String uname = "";
		if(null!=userAddressDto){
			String provinceName = userAddressDto.getProvinceName();
			if(StringUtils.isNotBlank(provinceName)){
				inputAddress.append(provinceName);
			}
			String cityName = userAddressDto.getCityName();
			if(StringUtils.isNotBlank(cityName)){
				inputAddress.append(cityName);
			}
			String districtName = userAddressDto.getDistrictName();
			if(StringUtils.isNotBlank(districtName)){
				inputAddress.append(districtName);
			}
			String townName = userAddressDto.getTownName();
			if(StringUtils.isNotBlank(townName)){
				inputAddress.append(townName);
			}
			String address = userAddressDto.getAddress();
			if(StringUtils.isNotBlank(address)){
				inputAddress.append(address);
			}
			mobile = userAddressDto.getMobile();
			uname= userAddressDto.getUserName();
		}
		else{
			logger.info("根据addressId:"+addressId+",查询地址表信息为空");
		}
		map.put("inputAddress", inputAddress.toString());
		map.put("contactPhone", mobile);
		map.put("uname", uname);
		return map;
	}
	/**
	 * 处理商品信息，转换成推送信息
	 * @param listGoods
	 * @return
	 */
	public static List<BookInfo> deailBookInfo(List<OrderGoodsDto> listGoods) {
		List<BookInfo> listBook = new ArrayList<BookInfo>();
		if(null!=listGoods&&0!=listGoods.size()){
			for (OrderGoodsDto goods : listGoods) {
				BookInfo book = new BookInfo();
				Long goodsId = goods.getGoodsId();
				//TODO 商品数量有可能为小数1.5斤
				Double num = goods.getGoodsNumber().doubleValue();
				book.setDishId(goodsId);
				book.setNum(num);
				listBook.add(book);
			}
		}
		return listBook;

	}
	/**
	 * 处理商铺资源信息
	 * @param listGoods TODO 7.24变更 屏蔽 时间段表废弃，数据目前只存在order_shop_resouce
	 * @return 
	 * @throws Exception 
	 */
	public static List<SeatInfo> deailShopResource(Map<String, Object> mapResource) throws Exception{
//		IOrderShopRsrcDao orderShopRsrcDao = BeanFactory.getBean(IOrderShopRsrcDao.class);
		List<SeatInfo> listSeat = new ArrayList<SeatInfo>();
		if(mapResource!=null){
				SeatInfo seatInfo = new SeatInfo();
//				Long bookRuleId = (Long) mapResource.get("bookRuleId");
				//Long num = (Long) mapResource.get("resourceNumber");
//				Map<String, Object> mapBook = orderShopRsrcDao.getBookTypeById(bookRuleId);
//				if(null!=mapBook&&0!=mapBook.size()){
//					String bookType = (String) mapBook.get("bookType");
//					seatInfo.setSeatCate(bookType);
//				}
				//TODO 7.24变更 num为 seatCate为=order_shop_resource的resourcType
				String resourcType = (String) mapResource.get("resourcType");
				seatInfo.setSeatCate(resourcType);
				seatInfo.setSeatNum(1);
				listSeat.add(seatInfo);
		}
		return listSeat;

	}
	@Override
	public void run() {
		try {
			detailSingleOrder(this.order);
		} catch (Exception e) {
			logger.error("推送订单消息异常",e);
			e.printStackTrace();
		}
	}
	

}
