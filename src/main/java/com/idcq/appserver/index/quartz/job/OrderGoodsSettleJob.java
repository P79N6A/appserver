package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.dao.message.IPushUserMsgDao;
import com.idcq.appserver.dao.pay.OrderGoodsSettleDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.message.PushUserMsgDto;
import com.idcq.appserver.dto.pay.OrderGoodsSettle;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.service.message.IMessageSettingService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.Jpush;

/**
 * 
 * @ClassName: OrderGoodsSettleJob
 * @Description: 定时调用处理分账存储过程
 * @author 陈永鑫
 * @date 2015年4月7日 下午4:12:06
 * 
 */
public class OrderGoodsSettleJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("调用处理订单结算存储过程-start");
		try {
			OrderGoodsSettleDao orderGoodsSettleDao = BeanFactory
					.getBean(OrderGoodsSettleDao.class);
			Integer skip = 0;
			while (true) {
				List<OrderGoodsSettle> list = orderGoodsSettleDao
						.getOrderGoodsSettle(null, null, null, null, skip);
				if (CollectionUtils.isEmpty(list)) {
					break;
				} else {
					deailUserSetting(list);
				}
				// 每页20条
				skip = skip + 20;
			}
		} catch (Exception e) {
			logger.info("调用处理订单结算存储过程-异常");
			e.printStackTrace();
		}

	}

	/**
	 * 处理用户分账
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void deailUserSetting(List<OrderGoodsSettle> list) throws Exception {
		OrderGoodsSettleDao orderGoodsSettleDao = BeanFactory
				.getBean(OrderGoodsSettleDao.class);
		// 账单
		IUserBillDao userBillDao = BeanFactory.getBean(IUserBillDao.class);
		IMessageSettingService messageSettingService = BeanFactory
				.getBean(IMessageSettingService.class);
		// 判断消息推送开关是否开启
		MessageSettingDto messageSettingDto = messageSettingService
				.isSendMsgSettingByKey("award");
		for (OrderGoodsSettle ogs : list) {
			Long userid = ogs.getUserId();
			Long shopid = ogs.getShopId();
			Long goodsid = ogs.getGoodsId();
			String orderid = ogs.getOrderId();
			// 相关方在订单支付完成后几天自动结算
			int userSettleDelayDays = ogs.getUserSettleDelayDays();
			// 店铺服务完成后几天自动结算
			int shopSettleDelayDays = ogs.getShopSettleDelayDays();
			// 最后核算时间
			Date createTime = ogs.getCreateTime();
			int days = daysBetween(createTime);
			// 存储过程是否更新全部的标示，0为不更新，1为更新。
			int type = 0;
			/*
			 * 商铺结算修改为调用SP_UPDATE_ORDER_GOODS_SETTLE_SHOP ，
			 * 用户结算SP_UPDATE_ORDER_GOODS_SETTLE
			 */
			if (days == userSettleDelayDays) {
				// 如果用户天数大于商铺分账天数
				if (userSettleDelayDays > shopSettleDelayDays) {
					type = 1;
				}
				// 分账
				orderGoodsSettleDao.updateOrderGoodsSettle(userid, shopid,
						goodsid, orderid, type);
				if (null != messageSettingDto
						&& 1 == messageSettingDto.getRemandFlag()) {
					UserBillDto userBillDto = new UserBillDto();
					// 推送
					userBillDto.setUserId(userid);
					userBillDto = userBillDao.getUserBillByUserId(userBillDto);
					// 推送分账信息
					pushMessage(userBillDto);
				}
			}
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date
	 * @return 返回订单结束时间到现在距离的天数
	 */
	public int daysBetween(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time1 = cal.getTimeInMillis();
		cal.setTime(new Date());
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 推送消息
	 * @deprecated 2016-06-30
	 * @param arg
	 * @param listPushUser
	 * @param actonName
	 * @throws Exception
	 */
	private void pushMessage(UserBillDto userBillDto) throws Exception {
		// 用户推送dao
		IPushUserMsgDao pushUserMsgDao = BeanFactory
				.getBean(IPushUserMsgDao.class);
		List<PushUserTableDto> listPushUser = new ArrayList<PushUserTableDto>();
		if (CollectionUtils.isNotEmpty(listPushUser)) {
			for (PushUserTableDto pushUserTableDto : listPushUser) {
				Map<String, Object> map = new HashMap<String, Object>();
				String registrationId = pushUserTableDto.getRegId();
				String osInfo = pushUserTableDto.getOsInfo();
				String billType = userBillDto.getBillType();
				Double amount = userBillDto.getMoney();
				map.put("action", "award");
				map.put("billType", billType);
				map.put("amount", amount);
				String messageStr = JacksonUtil.objToString(map);
				// 发送消息
				Jpush.sendPushToTarget(osInfo, pushUserTableDto.getUserType(), registrationId, null, messageStr);
				// 保存推送信息
				PushUserMsgDto pushUserMsgDto = new PushUserMsgDto();
				pushUserMsgDto.setAction("award");
				pushUserMsgDto.setMessageContent(messageStr);
				pushUserMsgDto.setUserId(pushUserTableDto.getUserId());
				pushUserMsgDto.setRegId(registrationId);
				pushUserMsgDto.setSendTime(new Date());
				pushUserMsgDao.insertSelective(pushUserMsgDto);
			}
		}
	}
}
