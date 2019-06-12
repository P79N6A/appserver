package com.idcq.appserver.service.packet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BusAreaActStatusEnum;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.activity.IBusinessAreaConfigDao;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.OrderGoodsSettleDao;
import com.idcq.appserver.dao.redpacket.IRedPacketDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.BillUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.attachment.AttachmentUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;

@Service
public class PacketServiceImpl implements IPacketService {
    
    private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private IPacketDao packetDao;
	@Autowired
	public OrderGoodsSettleDao orderGoodsSettleDao;
	@Autowired
	public IOrderGoodsDao orderGoodsDao;
	@Autowired
	public IOrderDao orderDao;
	@Autowired
	private IPayDao payDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IOrderLogDao orderLogDao;
	@Autowired
	private IShopDao shopDao;
    @Autowired
    public IPlatformBillDao platformBillDao;
    @Autowired
    public IUserBillDao userBillDao;
    @Autowired
    public  IRedPacketDao redPacketDao;     
    @Autowired
    public IBusinessAreaConfigDao businessAreaConfigDao;
    @Autowired
    public IBusinessAreaActivityDao  businessAreaActivityDao;
    @Autowired
    private IStorageServcie storageService;
    
	public String queryUserRedPacket(Long userId, int pSize, int pNo)
			throws Exception {
		validUser(userId);//用户校验
		boolean flag = false;
		//按key+userId缓存
		String cacheKey = CommonConst.REDIS_USER_RED_PACKET+userId+":"+pNo+":"+pSize;
		String cacheValue = DataCacheApi.get(cacheKey);
		if (flag = (cacheValue != null)) {
			return cacheValue.toString();
		}
		List<Map> shopRedPackets = null;
		String result = null;
		if (!flag) {
			Map param = new HashMap();
			param.put("userId", userId);
			param.put("stNo", (pNo - 1) * pSize);
			param.put("pSize", pSize);
			param.put("useFlag", CommonConst.RED_PACKET_NOT_USE_FLAG);// 0-查询未使用过的红包，1-查询已使用过的，其他查询全部
			// 先查询当前用户红包对应的商铺及使用金额分组
			int rCount = packetDao.queryShopRedPacketsByGroupCount(param);
			if (rCount > 0) {
				shopRedPackets = packetDao.queryShopRedPacketsByGroup(param);
				if (null != shopRedPackets && (shopRedPackets.size()) > 0) {
					List<Long> shopIds = new ArrayList<Long>();
					for(Map bean : shopRedPackets){
						Long shopId =  CommonValidUtil.isEmpty(bean.get("shopId"))?null:Long.valueOf(bean.get("shopId")+"");
						if (null != shopId) {
							shopIds.add(shopId);
						}
						//从redis中获取商铺对象，获取商铺名称
						bean.put("shopName", getShopName(shopId));
					}
					if (shopIds.size() > 0) {
						param.put("shopIds", shopIds);
					}
					// 查询用户所有红包，根据用户编号及商铺编号集合
					List<Map> userAllRedPackets = packetDao.queryRedPackets(param);
					for (Map bean : shopRedPackets) {
						double amount =  CommonValidUtil.isEmpty(bean.get("amount")) ? 0 : Double.valueOf(bean.get("amount") + "");
						String shopId =  CommonValidUtil.isEmpty(bean.get("shopId")) ? null : bean.get("shopId") + "";
						List<Map> userRedPackets = new ArrayList<Map>();
						Iterator<Map> ite = userAllRedPackets.iterator();
						while(ite.hasNext()){
							Map map = ite.next();
							double amountTmp = CommonValidUtil.isEmpty(map.get("amount")) ? 0 : Double.valueOf(map.get("amount") + "");
							String shopIdTmp = CommonValidUtil.isEmpty(map.get("shop_id")) ? null : map.get("shop_id") + "";
							if (amount == amountTmp && StringUtils.equals(shopId, shopIdTmp)) {
								map.remove("shop_id");
								map.remove("amount");
								userRedPackets.add(map);
								ite.remove();
							}
						}
						bean.put("urps", userRedPackets);
					}
				}
				Map pModel = new HashMap();
				pModel.put("pNo", pNo);
				pModel.put("count", rCount);
				pModel.put("lst", shopRedPackets);
				result = ResultUtil.getResultJson(0, "获取我的红包列表成功", pModel,DateUtils.DATETIME_FORMAT);
				if (pNo < 2) {
					//存储前两页的数据
					DataCacheApi.set(cacheKey, result);//将结果放入redis缓存中
					//将当前查询结果存储的key存储进redis中
					setQueryUserRedPacketToRedis(cacheKey, userId);
				}
			}
		}
		return result;
	}
	
	public boolean setQueryUserRedPacketToRedis(String cacheKey,Long userId){
		try {
			Gson gson = new Gson();
			String key = CommonConst.QR_REDPACKET_KEYS+userId;
			List<String> qrKeyList = new ArrayList<String>();
			String obj = DataCacheApi.get(key);
			if (null != obj) {
				qrKeyList =  gson.fromJson(obj, List.class);
			}
			qrKeyList.add(cacheKey);
			DataCacheApi.set(key, gson.toJson(qrKeyList, List.class));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean delQueryUserREdPacketToRedis(Long userId) throws Exception{
		String key = CommonConst.QR_REDPACKET_KEYS+userId;
		String value=DataCacheApi.get(key);
		if (null != value) {
			Gson gson = new Gson();
			List<String> keys = gson.fromJson(value, List.class);
			for(String str : keys){
				DataCacheApi.del(str);
			}
			DataCacheApi.del(key);
		}
		return true;
	}

	private String getShopName(Long shopId){
		boolean flag = false;
		String result = null;
		Object redisShopData=DataCacheApi.getObject("shop"+shopId);
		if(!(flag = (redisShopData == null))){//从redis中取得附件Id
			ShopDto redisShopDto=(ShopDto) redisShopData;
			if (null != redisShopDto) {
				result =  redisShopDto.getShopName();
			}else flag = true;
		}
		if (flag) {
			result = shopDao.getShopNameById(shopId);
		}
		return result;
	}
	

	public Map obtainRedPacket(Long userId, String batchNo) throws Exception {
		validUser(userId);//用户校验
		// 是否还有红包
		Map redPacket = packetDao.queryRedPacketIdByBatchNo(batchNo);
		if (null == redPacket || redPacket.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58601,
					CodeConst.MSG_REDPACKET_USE_58601);
		}
		if (!CommonValidUtil.isEmpty(redPacket.get("owner_id"))) {
			throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58601,
					CodeConst.MSG_REDPACKET_USE_58601);
		}
		if (!CommonValidUtil.isEmpty(redPacket.get("stop_time"))) {
			String stopTime = redPacket.get("stop_time").toString();
			int re = DateUtils.compareDate(stopTime);
			if (re == 1) {
				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58504,
						CodeConst.MSG_REDPACKET_USE_58504);
			}
			if (re == -2) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
						CodeConst.MSG_FORMAT_TIME_ERROR_REDPACKET);
			}
		}
		String obtainTime = DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT);
		Integer redPacketId = Integer.parseInt(redPacket.get("red_packet_id")+"");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("redPacketId",redPacketId );
		params.put("batchNo", batchNo);
		params.put("obtainTime", obtainTime);
		// 如果存在还可以领取的红包，则将该红包的持有者改为当前用户
		packetDao.obtainRedPacket(params);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("redPacketId", redPacketId);
		return model;
	}

	public void payByRedPacket(Long userId, String orderId,String redPacketIdStrs,int orderPayType) throws Exception {
		validUser(userId);//用户校验
		//订单校验
		List<Map> orders = validOrder(userId, orderId, orderPayType);
		List<Long> redPacketIds = convertRedPacket(redPacketIdStrs);
		// 红包校验
		List<Map> redPackets = validRedPacket(redPacketIds, userId);
		//Map redPacket = validRedPacket(redPacketId, userId);
		// 获取订单实际需要支付的金额
		BigDecimal amount = new BigDecimal(0);
		if (orderPayType == 0) {
			amount = packetDao.queryOrderAmount(orderId);
		} else {
			amount = new BigDecimal(payDao.getSumOrderGroupAmount(orderId));
		}
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = packetDao.queryOrderPayAmount(orderId, orderPayType);
		if (null == payAmount) {
			payAmount = new BigDecimal(0);
		}
		// 实际需要支付金额-已经支付金额 = 剩余需要支付金额
		BigDecimal surplusAmount = amount.subtract(payAmount);
		if (surplusAmount.compareTo(new BigDecimal(0)) > 0) {
			boolean flag = true;
			for(Map redPacket:redPackets){
				Long redPacketId = Long.parseLong(redPacket.get("red_packet_id")+"");
				// 红包中的金额
				BigDecimal redAmount = new BigDecimal(redPacket.get("amount") + "");
				// 红包实际金额跟剩余需要支付金额比较
				int compResult = surplusAmount.compareTo(redAmount);
				Map param = new HashMap();
				param.put("orderId", orderId);
				param.put("redPacketId", redPacketId);
				param.put("payType", CommonConst.PAY_TYPE_RED_PACKET);// 红包支付
				//本次支付实际支付金额
				BigDecimal actualAmount = new BigDecimal(0);
				if (compResult > 0) {
					// 剩余支付金额跟红包金额相等或者大于红包金额时，订单支付表中的支付金额为红包金额
					actualAmount = redAmount;
				} else if (compResult <= 0) {
					actualAmount = surplusAmount;
					// 剩余支付金额小于红包金额，订单支付表中的支付金额为实际剩余的支付金额
					// 更新订单表中的支付状态，修改为已支付 (支付状态：未支付-0,已支付-1,支付失败-2) 订单状态；已预定-0,已开单-1,派送中-2,已结账-3，退单中-4,已退单-5,已完成-6
					if (flag) {
						flag = false;
						if (orderPayType == 0) {
							OrderDto orderDto = new OrderDto();
							orderDto.setOrderId(orderId);
							orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
							orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
							orderDto.setLastUpdateTime(new Date());
							orderDao.updateOrder(orderDto);
							
				    		//修改库存
				    		storageService.insertShopStorageByOrderId(orderId,orderDto.getShopId());
						} else {
							// 订单组，需要更改订单组中所有订单状态
							//List<Map> groupList = payDao.queryOrderGroupById(orderId);
							if (null != orders && orders.size() > 0) {
								for (Map bean : orders) {
									String ordId = bean.get("order_id") == null ? null : bean.get("order_id") + "";
									if (null != ordId) {
										OrderDto orderDto = new OrderDto();
										orderDto.setOrderId(ordId);
										orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
										orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
										orderDto.setLastUpdateTime(new Date());
										orderDao.updateOrder(orderDto);
									}
								}
							}
						}
						//记录订单日志：当订单状态发生变更时，才会记录日志
						batchSaveOrderLog(orderId, orderPayType, orders);
					}
				}
				String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
				param.put("amount", actualAmount);
				param.put("orderPayType", orderPayType);
				param.put("orderPayTime", nowTime);
				param.put("lastUpdateTime", nowTime);
				param.put("payeeType", 0);
				packetDao.insertOrderPay(param);//新增支付记录
				// 修改红包标记 0-未使用  1-已使用
				param = new HashMap();
				param.put("useFlag", 1);
				param.put("redPacketId", redPacketId);
				packetDao.updateRedPacketFlag(param);
				//每支付一次，都将需要支付的金额递减
				surplusAmount = surplusAmount.subtract(actualAmount);
			}
		} else {
			throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58505,CodeConst.MSG_REDPACKET_USE_58505);
		}
 	}
	
	public int batchSaveOrderLog(String orderId,int orderPayType,List<Map> orders) throws Exception{
		List<OrderLogDto> orderLogs = new ArrayList<OrderLogDto>();
		if (orderPayType == 0) {
			OrderLogDto orderLogDto = new OrderLogDto();
			orderLogDto.setRemark("红包支付");
			orderLogDto.setLastUpdateTime(new Date());
			orderLogDto.setOrderId(orderId);
			orderLogDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
			orderLogDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
			orderLogs.add(orderLogDto);
		}else{
			if (null != orders && orders.size() > 0) {
				for (Map bean : orders) {
					OrderLogDto orderLogDto = new OrderLogDto();
					orderLogDto.setRemark("红包支付");
					orderLogDto.setLastUpdateTime(new Date());
					String ordId = bean.get("order_id") == null ? null : bean.get("order_id") + "";
					orderLogDto.setOrderId(ordId);
					//orderLogDto.setOrderStatus(Integer.parseInt(bean.get("order_status")+""));
					//记录当前订单状态
					orderLogDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
					//Integer payStatus = CommonValidUtil.isEmpty(bean.get("pay_status"))?null:Integer.parseInt(bean.get("pay_status")+"");
					orderLogDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
					orderLogs.add(orderLogDto);
				}
			}
			//订单日志
		}
		if (orderLogs.size() > 0) {
			orderLogDao.batchSaveOrderLogs(orderLogs);
		}
		return 1;
	}
	
	/**
	 * 红包编号转换
	 * @param redPacketIdStrs
	 * @return
	 * @throws Exception 
	 */
	public List<Long> convertRedPacket(String redPacketIdStrs) throws Exception{
		List<Long> redPacketIds = new ArrayList<Long>();
		String[] strs = redPacketIdStrs.split(CommonConst.PAY_REDPACKET_SPLIT_KEY);
		int arrLen = 0;
		if (null == strs || (arrLen = strs.length) == 0) {
			throw new ValidateException( CodeConst.CODE_PARAMETER_NOT_VALID, concatMsg(redPacketIdStrs)+CodeConst.MSG_FORMAT_ERROR_REDPACKET_ID+"，多个红包需以英文状态["+CommonConst.PAY_REDPACKET_SPLIT_KEY+"]分隔");
		}
		for (int i = 0; i < strs.length; i++) {
			if(null == strs[i]) continue;
			Long redPacketId = CommonValidUtil.validStrLongFmt(strs[i],CodeConst.CODE_PARAMETER_NOT_VALID, concatMsg(strs[i])+CodeConst.MSG_FORMAT_ERROR_REDPACKET_ID+"，多个红包需以英文状态["+CommonConst.PAY_REDPACKET_SPLIT_KEY+"]分隔");
			redPacketIds.add(redPacketId);
		}
		int size = redPacketIds.size();
		if (size == 0 || (size != arrLen)) {
			throw new ValidateException( CodeConst.CODE_PARAMETER_NOT_VALID, concatMsg(redPacketIdStrs)+CodeConst.MSG_FORMAT_ERROR_REDPACKET_ID);
		}
		return redPacketIds;
	}
	
	/**
	 * 会员信息检测
	 * @param userId
	 * @return
	 */
	public boolean validUser(Long userId) throws Exception{
		Map user = userDao.queryUserStatus(userId);
		if (user == null || user.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_MEMBER);
		}
		int status = CommonValidUtil.isEmpty(user.get("status"))?0:Integer.valueOf(user.get("status")+"");
		if ( status != 1) {
			throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
		}
		return true;
	}
	
	/**
	 * 订单检测
	 * @param userId
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	private List<Map> validOrder(Long userId,String orderId,int orderPayType) throws Exception{
		List<Map> orders = new ArrayList<Map>();
		Map<String,Object> order = null;
		if (0 == orderPayType) {
			Integer payStatus = null;
			Integer orderStatus = null;
			order = packetDao.queryOrderIsExists(userId, orderId);
			if (null == order || order.size() <=0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_ORDER);
			}else{
				payStatus = Integer.parseInt(order.get("pay_status")+"");
				orderStatus = Integer.parseInt(order.get("order_status")+"");
				order.put("order_status", orderStatus);
			}
			orders.add(order);
			// TODO 当只有订单支付状态=0 未支付，才可以进行支付
			if (payStatus.intValue() == 1) {
				//已经支付，不需要再支付
				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58505,CodeConst.MSG_REDPACKET_USE_58505);
			}
			// TODO 订单状态为（已预定-0,已开单-1,派送中-2）时，才可以进行支付
			if (orderStatus.intValue() >=3) {
				//该状态已经不可支付
				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58507,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
		} else if (1 == orderPayType) {// 订单组支付
			//orders = payDao.queryOrderGroupById(orderId);
			orders = payDao.queryOrderGroupByOrderId(orderId);
			//CommonValidUtil.validObjectNull(list,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			if (null == orders || orders.size() == 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
		}
		return orders;
	}
	
	/**
	 * 红包校验
	 * @param redPacketId
	 * @return
	 * @throws Exception 
	 */
	private List<Map> validRedPacket(List<Long> redPacketIds,Long userId) throws Exception{
		List<Map> redPacketMaps = new ArrayList<Map>();
//		for(Long redPacketId:redPacketIds){
//			RedPacket redPacket = packetDao.queryRedPacketById(redPacketId);
//			if (null == redPacket) {
//				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,concatMsg(redPacketId)+CodeConst.MSG_MISS_REDPACKET_NOT_EXIST);
//			}
//			if (!CommonValidUtil.isEmpty(redPacket.get("shop_id"))) {//发行商铺为空，表示平台发行，支持所有商铺
//				int shopStatus = CommonValidUtil.isEmpty(redPacket.get("shop_status")) ? -1 : (Integer.parseInt(redPacket.get("shop_status") + ""));
//				if (shopStatus != 0) {
//					throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58506,concatMsg(redPacketId)+CodeConst.MSG_REDPACKET_USE_58506);
//				}
//			}
//			if (CommonValidUtil.isEmpty(redPacket.get("owner_id")) || !(userId + "").equals(redPacket.get("owner_id") + "")) {
//				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58501,concatMsg(redPacketId)+CodeConst.MSG_REDPACKET_USE_58501);
//			}
//			int useFlag = Integer.valueOf(redPacket.get("use_flag") + "");
//			if (useFlag == 1) {
//				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58502,concatMsg(redPacketId)+CodeConst.MSG_REDPACKET_USE_58502);
//			}
//			// 检测红包使用期限 时间格式：yyyy-MM-dd HH:mm:ss 其他格式会异常
//			/*只需要校验红包停止使用时间，不需要校验开始时间？*/
//			if (!CommonValidUtil.isEmpty(redPacket.get("start_time"))) {
//				String startTime = redPacket.get("start_time").toString();
//				int re = DateUtils.compareDate(startTime);
//				if (re == -1) {
//					throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58503,concatMsg(redPacketId)+CodeConst.MSG_REDPACKET_USE_58503);
//				}
//				if (re == -2) {
//					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,concatMsg(redPacketId)+CodeConst.MSG_FORMAT_TIME_ERROR_REDPACKET);
//				}
//			}
//			if (!CommonValidUtil.isEmpty(redPacket.get("stop_time"))) {
//				String stopTime = redPacket.get("stop_time").toString();
//				int re = DateUtils.compareDate(stopTime);
//				if (re == 1) {
//					throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58504,concatMsg(redPacketId)+CodeConst.MSG_REDPACKET_USE_58504);
//				}
//				if (re == -2) {
//					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,concatMsg(redPacketId)+CodeConst.MSG_FORMAT_TIME_ERROR_REDPACKET);
//				}
//			}
//			redPacketMaps.add(redPacket);
//		}
		return redPacketMaps;
	}
	
	private String concatMsg(Object redPacketId){
		return "红包编号["+redPacketId+"]使用异常：";
	}
	
	public BigDecimal queryOrderPayAmount(String orderId, int orderPayType)
			throws Exception {
		return this.packetDao.queryOrderPayAmount(orderId, orderPayType);
	}

	public BigDecimal queryOrderAmount(String orderId) throws Exception {
		return packetDao.queryOrderAmount(orderId);
	}

	public int insertRedPacket(List<Map> datas) {
		return packetDao.insertRedPacket(datas);
	}
	
	 /* (non-Javadoc)
     * @see com.idcq.appserver.service.packet.IPacketService#getRedPacketSendMoney(java.lang.Long, java.lang.Double)
     */
    @Override
    public Double getRedPacketSendMoney(Long businessAreaActivityId, Double money) throws Exception{
    	//红包
        Double configValue = 0d;
    	BusinessAreaActivityDto businessAreaActivity = businessAreaActivityDao.getBusinessAreaActivityById(businessAreaActivityId);
    	if(businessAreaActivity!=null){
    		//校验商铺活动状态,不在进行中，就返回红包0元
    		if(BusAreaActStatusEnum.RUNNING.getValue()!=businessAreaActivity.getActivityStatus()){
    			return configValue;
    		}
    		
            //获取送value
            Map<String, Object> resultGive = businessAreaConfigDao.getGiveConfigValueByCodeAndAreaId(businessAreaActivityId,
                    CommonConst.CONFIG_CODE_OVER,CommonConst.CONFIG_CODE_GIVE,money);
            if(null != resultGive && 0 != resultGive.size()){
                configValue = Double.valueOf(Double.parseDouble((String)resultGive.get("configValue")));
            }
    	}
        return configValue;
    }
    
    @Override
    public Map<String, Object> getRedPacketDetail(Long redPacketId) throws Exception
    {
        
        Map<String, Object> resultMap = redPacketDao.getRedPacketDetail(redPacketId);
        
        if(resultMap!=null){
        	resultMap = updateResultMap(resultMap);
        }
        return resultMap;
    }
    /**
     * 更新map返回结果
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public Map<String, Object> updateResultMap(Map<String, Object> resultMap)throws Exception{


        // 商铺logo
        String shopLogoUrl = "";
        // 设置商铺logo
        List<AttachmentRelationDto> attachmentRelationDtos = AttachmentUtils.getAttachment(
                (Long) resultMap.get("shopId"), CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE);
        if (!CollectionUtils.isEmpty(attachmentRelationDtos)){
            shopLogoUrl = FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl());
            resultMap.put("shopLogoUrl", shopLogoUrl);
        }

        // shops组装
        resultMap.put("shops", getBusinessShopsById(resultMap));
        return resultMap;

    }

    public PageModel getMemberRedPackets(Map<String, Object> parms) throws Exception{
        PageModel pageModel = new PageModel();
        Integer count = redPacketDao.getMemberRedPacketsCount(parms);
        if(count!=null&&count!=0){
            List<Map<String, Object>> resultList = redPacketDao.getMemberRedPackets(parms);
            pageModel.setList(updateResultList(resultList));
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }
    /**
     * 更新list返回结果
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> updateResultList(List<Map<String, Object>> list)
            throws Exception {
        
        List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();
        
        if (CollectionUtils.isNotEmpty(list)) {
            //返回对象shops
            
            for (Map<String, Object> rlt : list) {
                //商铺logo
                String shopLogoUrl = "";
                //设置商铺logo
                List<AttachmentRelationDto> attachmentRelationDtos = AttachmentUtils.getAttachment((Long)rlt.get("shopId"), CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE);
                if(!CollectionUtils.isEmpty(attachmentRelationDtos)){
                    shopLogoUrl = FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl());
                    rlt.put("shopLogoUrl", shopLogoUrl);
                }
                
                //shops组装
                rlt.put("shops", getBusinessShopsById(rlt));
                
                newlist.add(rlt);
            }
        }
        return newlist;
    }
    /**
     * 组装shops
     * 
     * @Function: com.idcq.appserver.service.packet.PacketServiceImpl.getBusinessShopsById
     * @Description:
     *
     * @param reltMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getBusinessShopsById(Map<String, Object> reltMap) throws Exception{
        
        List<Map<String, Object>> shopsList = new ArrayList<Map<String,Object>>();
        if(reltMap!=null){
            //获取商圈商铺信息
            shopsList = redPacketDao.getBusinessShopsById((Long)reltMap.get("businessAreaActivityId"));
            for (Map<String, Object> shop : shopsList)
            {
                String shopLogoUrl = "";
                List<AttachmentRelationDto> attachmentRelationDtos = AttachmentUtils.getAttachment((Long)shop.get("shopId"), CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE);
                if(!CollectionUtils.isEmpty(attachmentRelationDtos)){
                    shopLogoUrl = FdfsUtil.getFileProxyPath(attachmentRelationDtos.get(0).getFileUrl());
                    shop.put("shopLogoUrl", shopLogoUrl);
                }
            }
        }

        return shopsList;
    }


    @Override
    public Double sendRedPacketToUser(OrderDto order) {
        Double sendMoney = 0D;
        if(null == order.getUserId()) {
            logger.info("订单用户ID为空不发送红包");
            return sendMoney;
        }
        String orderId = order.getOrderId();
        if(order.getOrderStatus() != CommonConst.ORDER_STS_YJZ) {
            logger.info("订单未结账不参与发红包,订单ID:" + orderId);
            
            return sendMoney;
        }
        if(null == order.getBusinessAreaActivityId()) {
            logger.info("订单不参与发红包,订单ID:" + orderId);
            return sendMoney;
        }
        try {
            RedPacket packet = packetDao.getRedPacketByOrderId(orderId);
            if(null != packet)
            {
                logger.info("该订单已发过红包,订单ID:" + orderId);
                return sendMoney;
            }
            List<BusinessAreaActivityDto> activitys = businessAreaActivityDao.getBusinessAreaActivityBy(order.getShopId(), order.getBusinessAreaActivityId(), DateUtils.format(new Date(), DateUtils.DATE_FORMAT));
            if (CollectionUtils.isEmpty(activitys)) {
                logger.info("该订单不参与商圈活动" + orderId);
                return sendMoney;
            }
            //计算红包金额
            sendMoney = getRedPacketSendMoney(order.getBusinessAreaActivityId(), order.getSettlePrice());
            if (sendMoney == 0) {
                logger.info("支付金额不够不发红包");
                return sendMoney;
            }
           
            //判断是否可以发送红包，新增红包记录，订单金额是否达到发红包标准，线上支付金额是否大于等于红包金额
            Double payAmount = payDao.getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_PLATFORM);
            if (payAmount < sendMoney) {
                logger.info("线上支付金额:" + payAmount +"小于需要发送的红包金额:" + sendMoney);
                return 0D;
            }
            RedPacket redPacket = new RedPacket();
            redPacket.setUserId(order.getUserId());
            redPacket.setAmount(sendMoney);
            redPacket.setPrice(sendMoney);
            redPacket.setCreateTime(new Date());
            redPacket.setSourceOrderId(orderId);
            redPacket.setStatus(RedPacketStatusEnum.USEABLE.getValue());
            redPacket.setBeginDate(DateUtils.getCurrentDate(DateUtils.DATE_FORMAT));
            redPacket.setShopId(order.getShopId());
            redPacket.setBusinessAreaActivityId(order.getBusinessAreaActivityId());
            packetDao.addRedPacket(redPacket);
            //新增用户红包账单账单记录
            UserBillDto userBill = BillUtil.buildUserBillForRedPacket(order, redPacket, sendMoney, false);
            userBillDao.insertUserBill(userBill);
            //新增平台发红包账单
            PlatformBillDto platformBillDto = BillUtil.buildPlatformBill(order, sendMoney, CommonConst.PLT_BILL_MNY_SOURCE_HB, CommonConst.PLATFORM_BILL_TYPE_S_RED_PACKET, "发红包", false);
            platformBillDto.setBillDesc("给用户发红包");
            platformBillDto.setRedPacketId(redPacket.getRedPacketId());
            platformBillDao.insertPlatformBill(platformBillDto);
            return sendMoney;
        } catch (Exception e) {
            logger.error("发送红包失败" + e);
            return 0D;
        }
    }

    @Override
    public Double getRedPacketAmountBy(Long shopId, Long userId, int status) {
        return packetDao.getRedPacketAmountBy(shopId, userId, status);
    }
}
