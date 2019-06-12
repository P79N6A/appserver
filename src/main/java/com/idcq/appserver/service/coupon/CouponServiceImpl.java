package com.idcq.appserver.service.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.coupon.ICouponDao;
import com.idcq.appserver.dao.coupon.IUserCouponDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.dto.coupon.UserCouponDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;


/**
 * 优惠券service
 * 
 * @author Administrator
 * 
 * @date 2015年3月30日
 * @time 上午11:03:06
 */
@Service
public class CouponServiceImpl implements ICouponService{
	@Autowired
	private IUserCouponDao userCouponDao;
	@Autowired
	private ICouponDao couponDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IOrderDao orderDao;
	@Autowired
	private IPacketDao packetDao;
	@Autowired
	private IOrderGoodsDao orderGoodsDao;
	@Autowired
	private IPayDao payDao;
	@Autowired
	private IOrderServcie orderServcie;
	@Autowired
	private IGoodsDao goodsDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IPayServcie payServcie;
	@Autowired
	private IStorageServcie storageService;
	
	public PageModel getShopCouponList(CouponDto coupon, int pNo, int pSize)
			throws Exception {
		CommonValidUtil.validPositLong(coupon.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		PageModel pm = new PageModel();
		pm.setTotalItem(this.couponDao.getCouponListCount(coupon));
		pm.setList(this.couponDao.getCouponList(coupon, pNo, pSize));
		pm.setToPage(pNo);
		pm.setPageSize(pSize);
		return pm;
	}
	
	public PageModel getUserCouponList(UserCouponDto coupon, int pNo, int pSize)
			throws Exception {
		CommonValidUtil.validPositLong(coupon.getUserId(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
		PageModel pm = new PageModel();
		pm.setTotalItem(this.userCouponDao.getUserCouponListCount(coupon));
		pm.setList(this.userCouponDao.getUserCouponList(coupon, pNo, pSize));
		pm.setToPage(pNo);
		pm.setPageSize(pSize);
		return pm;
	}

	public String grabCoupon(UserCouponDto coupon) throws Exception {
		//优惠券数量
		int number=coupon.getNumber();
		Double couponValue = Double.valueOf(coupon.getValue()+"");//优惠券面值
		Double couponPrice = Double.valueOf(coupon.getPrice()+"");//优惠券单价
		Double goodsPrice = couponPrice * number;
		Double goodsPriceBeforeDiscount = couponValue * number;
		//1,生成优惠券订单
		GoodsDto pGoods = this.goodsDao.getGoodsById(coupon.getGoodsId());
		CommonValidUtil.validObjectNull(pGoods, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
		OrderDto couponOrder = new OrderDto();
		couponOrder.setShopId(coupon.getShopId());
		couponOrder.setUserId(coupon.getUserId());
		couponOrder.setOrderType(CommonConst.ORDER_TYPE_COUPON);
		couponOrder.setGoodsPrice(goodsPrice);
		couponOrder.setSettlePrice(goodsPrice);
		couponOrder.setLogisticsPrice(0D);
		couponOrder.setOrderTotalPrice(goodsPrice);
		couponOrder.setGoodsPriceBeforeDiscount(goodsPriceBeforeDiscount);
		couponOrder.setPayTimeType(0);;
		couponOrder.setOrderTime(new Date());
		couponOrder.setCouponId(coupon.getCouponId());
		couponOrder.setCouponNum(number);
		couponOrder.setOrderStatus(0);
		if(pGoods.getGoodsType() == CommonConst.GOODS_TYPE_GOODS ||pGoods.getGoodsType() == CommonConst.GOODS_TYPE_SET){
			couponOrder.setOrderSceneType(CommonConst.PLACE_ORDER_GOODS);
		}else if(pGoods.getGoodsType() == CommonConst.GOODS_TYPE_SERVICE){
			couponOrder.setOrderSceneType(CommonConst.PLACE_ORDER_SERVICE);
		}
		List<OrderGoodsDto> gList = new ArrayList<OrderGoodsDto>();
		OrderGoodsDto og = new OrderGoodsDto();
		og.setGoodsId(pGoods.getGoodsId());
		og.setShopId(pGoods.getShopId());
		og.setGoodsNumber(Double.valueOf(number));
		gList.add(og);
		couponOrder.setGoods(gList);
		//下单
		OrderDto pOrder = this.orderServcie.placeOrder(couponOrder,1);
		String orderId = pOrder.getOrderId();
		coupon.setOrderId(orderId);
		for(int i=0;i<number;i++){
			//2,关联用户领取优惠券表
			Long ucId = this.userCouponDao.addUserCoupon(coupon);
		}
		//3,对应优惠券可用数量减去1
		this.couponDao.delCouponAvailNum(coupon.getCouponId(), coupon.getAvailableNumber()-number);
		//4,已用优惠券数量加上1
		this.couponDao.incrCouponUsedNum(coupon.getCouponId(), coupon.getUsedNumber()+number);
		return orderId;
	}

	
	
	public int consumeCoupon(Long userId,String orderId,Long ucId,Integer orderPayType) throws Exception {
		//用户校验
		UserDto user=userDao.getUserById(userId);
		CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		//订单校验
		OrderDto order=null;
		List<Map> list = null;
		//优惠券校验
		UserCouponDto userCoupon=userCouponDao.getUserCouponById(userId, ucId);
		CommonValidUtil.validObjectNull(userCoupon, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_COUPON_NOT_EXIST);
		if(1 !=userCoupon.getCouponStatus()){
			throw new ValidateException(CodeConst.CODE_COUPON_STATUS_NOT_AVAILABLE, CodeConst.MSG_COUPON_STATUS_NOT_AVAILABLE);
		}
		// 获取订单实际需要支付的金额
		BigDecimal amount = null;
		Double amountTotal = 0.0;
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = null;
		if(orderPayType==1){
			boolean flag=false;
			//订单组支付
			list = payDao.queryOrderGroupById(orderId);
			if (list==null || list.size() == 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
			for(int i=0,len=list.size();i<len;i++){
				order=orderDao.getOrderById((String) list.get(i).get("orderId"));
				if(1==order.getPayStatus()){
					throw new ValidateException(CodeConst.CODE_PAY_STATUS_SUCCESS,CodeConst.MSG_PAY_STATUS_SUCCESS);
				}
				if(isPay(userCoupon,order)){//优惠券可用于商品
					flag=true;//可用于支付
					break;
				}
			}
			if(!flag){
				throw new ValidateException(CodeConst.CODE_COUPON_NOT_AVAILABLE_GOODS,CodeConst.MSG_COUPON_NOT_AVAILABLE_GOODS);
			}
			amountTotal = payDao.getSumOrderGroupAmount(orderId);
			amount = new BigDecimal(amountTotal);
			payAmount = packetDao.queryOrderPayAmount(orderId,orderPayType);
		}else{
			order=orderDao.getOrderById(orderId);
			CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
			if(1==order.getPayStatus()){
				throw new ValidateException(CodeConst.CODE_PAY_STATUS_SUCCESS,CodeConst.MSG_PAY_STATUS_SUCCESS);
			}
			if(!isPay(userCoupon,order)){//优惠券不可用于其他商品
				throw new ValidateException(CodeConst.CODE_COUPON_NOT_AVAILABLE_GOODS,CodeConst.MSG_COUPON_NOT_AVAILABLE_GOODS);
			}
			amount = packetDao.queryOrderAmount(orderId);
			payAmount = packetDao.queryOrderPayAmount(orderId,orderPayType);
		}
		int result=0;
		//result=userCouponDao.consumeCoupon(userId,orderId,ucId);
		if(payAmount==null){
			payAmount=new BigDecimal(0);
		}
		if(payAmount.doubleValue()>=amount.doubleValue()){//已支付金额 > 订单需支付的金额
			order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
			orderDao.updateOrder(order);
			payServcie.saveOrderLog(order,"消费优惠券");
			//修改库存
			storageService.insertShopStorageByOrderId(orderId,order.getShopId());
		}else{
			//需支付的金额
			double needPayAmount=amount.doubleValue()-payAmount.doubleValue();
			result=userCouponDao.consumeCoupon(userId,orderId,ucId);
			if (userCoupon.getPrice() >= needPayAmount){//足额支付
				// 修改订单状态
				if (1 == orderPayType) {
					payServcie.updateOrderGroupStatus(list,CommonConst.PAY_STATUS_PAY_SUCCESS,CommonConst.ORDER_STS_YJZ,"消费优惠券");// 批量更新订单状态
				} else {
					order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
					order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
					orderDao.updateOrder(order);
					payServcie.saveOrderLog(order,"消费优惠券");
				}
				OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, orderPayType);
			} else {
				// 修改订单状态
				if (1 == orderPayType) {
					payServcie.updateOrderGroupStatus(list, CommonConst.PAY_STATUS_NOT_PAY,CommonConst.ORDER_STS_YYD,"消费优惠券");// 批量更新订单状态
				} else {
					order.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
					orderDao.updateOrder(order);
					payServcie.saveOrderLog(order,"消费优惠券");
				}
			}
			//回写order_pay
			if(userCoupon.getPrice()>0){
				PayDto payDto = new PayDto();
				payDto.setOrderId(orderId);
				payDto.setPayType(4);// 优惠券支付
				payDto.setPayId(Long.valueOf(ucId));// update
				payDto.setPayAmount(new Double(userCoupon.getPrice()));
				payDto.setOrderPayType(orderPayType);
				payDao.addOrderPay(payDto);
			}
		}
		return result;
	}
	
	/**
	 * 判断优惠券是否可用
	 * @param userCoupon
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public boolean isPay(UserCouponDto userCoupon,OrderDto order) throws Exception{
		boolean flag=false;//是否可支付，true:可支付    false:不可支付
		OrderGoodsDto orderGoodsDto=new OrderGoodsDto();
		orderGoodsDto.setOrderId(order.getOrderId());
		List<OrderGoodsDto> goods=orderGoodsDao.getOGoodsListByOrderId(orderGoodsDto);
		CouponDto couponDto=couponDao.getCouponDtoById(userCoupon.getCouponId());
		CommonValidUtil.validObjectNull(couponDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_COUPON_NOT_EXIST);
		if(goods!=null){
			Long goodId=couponDto.getGoodsId();
			for(int i=0,len=goods.size();i<len;i++){
				if(goodId==goods.get(i).getGoodsId().intValue()){
					flag=true;
					continue;
				}
			}
		}
		return flag;
	}

	public CouponDto getCouponById(Long cpId) throws Exception {
		//常规验证
		return this.couponDao.getCouponById(cpId);
	}

	public int getGrapNumOfDateRange(Long userId, Long cpId, Date startDate,
			Date endDate) throws Exception {
		//常规验证
		return this.couponDao.getGrapNumByDateRange(userId, cpId, startDate, endDate);
	}

	public Map getShopRedPacket(Long shopId, int pNo, int pSize)
			throws Exception {
		int cnt = shopDao.queryNormalShopExists(shopId);
		if (cnt == 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		}
		Map param = new HashMap();
		param.put("shopId", shopId);
		param.put("nowTime", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		param.put("stNo", (pNo-1)*pSize);  
		param.put("pSize", pSize);
		List<Map> shopRedPackets = new ArrayList<Map>();
		int count = packetDao.queryShopRedPacketsCount(param);
		if (count > 0) {
			shopRedPackets = packetDao.queryShopRedPackets(param);
		}
		Map pModel = new HashMap();
		pModel.put("pNo", pNo);
		pModel.put("count", count);
		pModel.put("lst", shopRedPackets);
		return pModel;
	}

	@Override
	public List<CouponDto> getShopCouponListById(Long shopId) throws Exception {
		CouponDto coupon=new CouponDto();
		coupon.setShopId(shopId);
		List<CouponDto> couponList=this.couponDao.getCouponList(coupon,1,10000);
		return couponList;
	}
	
}	
