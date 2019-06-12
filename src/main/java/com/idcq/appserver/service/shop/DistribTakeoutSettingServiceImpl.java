package com.idcq.appserver.service.shop;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IBookTimeRuleDao;
import com.idcq.appserver.dao.shop.IDistribTakeoutSettingDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.shop.BookTimeRuleDto;
import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;

/**
 * 店铺外卖费用设置service
 * 
 * @author Administrator
 * 
 * @date 2015年7月14日
 * @time 下午4:59:51
 */
@Service
public class DistribTakeoutSettingServiceImpl implements IDistribTakeoutSettingService{

	@Autowired
	public IDistribTakeoutSettingDao distribTakeoutSettingDao ;
	@Autowired
	public IBookTimeRuleDao bookTimeRuleDao;
	@Autowired
	public IShopDao shopDao;
	
	public DistribTakeoutSettingDto getDistribTakeoutSetting(Long shopId,
			Integer settingType) throws Exception {
		DistribTakeoutSettingDto dts = this.distribTakeoutSettingDao.getDistribTakeoutSetting(shopId, settingType);
		if(dts == null){
			return null;
		}
		// 预约规则开关
		Integer bookFlag = getBookFlag(shopId,settingType);
		dts.setFlag(bookFlag);
//		CommonValidUtil.validObjectNull(dts, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_DIS_BOOK_RULE);
		//获取时间规则列表
		List<BookTimeRuleDto> timeRuleList = this.bookTimeRuleDao.getTimeRuleListBySettingId(dts.getSettingId());
		if(timeRuleList == null || timeRuleList.size() <= 0){
			return dts;
		}
//		CommonValidUtil.validListNull(timeRuleList, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_TIME_BOOK_RULE);
		StringBuilder deliveryTimeStr = new StringBuilder();			//接单时间
		StringBuilder stopDateStr = new StringBuilder();				//暂停预定日期
		StringBuilder weekDayStr = new StringBuilder();					//可预定周期
		int ruleType;
		Date beginTime ;
		Date endTime ;
		Integer weekDay;
		//拼凑各类时间
		for(BookTimeRuleDto e : timeRuleList){
			ruleType = e.getRuleType().intValue();
			if(ruleType == 1){//接单时间
				beginTime = e.getBeginTime();
				endTime = e.getEndTime();
				if(beginTime != null && endTime != null){
					deliveryTimeStr.append(DateUtils.getTimeStrByPattern(beginTime,DateUtils.TIME_FORMAT2))
					.append("&")
					.append(DateUtils.getTimeStrByPattern(endTime,DateUtils.TIME_FORMAT2))
					.append(",");
				}
			}else if(ruleType == 3){//暂停预定日期
				beginTime = e.getStopBeginDate();
				endTime = e.getStopEndDate();
				if(beginTime != null && endTime != null){
					stopDateStr.append(DateUtils.getTimeStrByPattern(beginTime,DateUtils.DATE_FORMAT))
					.append("&")
					.append(DateUtils.getTimeStrByPattern(endTime,DateUtils.DATE_FORMAT))
					.append(",");
				}
			}else if(ruleType == 2){//可预定周期
				weekDay = e.getWeekDay();
				if(weekDay != null){
					weekDayStr.append(e.getWeekDay())
					.append(",");
				}
			}
		}
		if (deliveryTimeStr.length() > 0) {
			dts.setDeliveryTime(deliveryTimeStr.substring(0, deliveryTimeStr.lastIndexOf(",")));
		}
		if (stopDateStr.length() > 0) {
			dts.setStopDate(stopDateStr.substring(0, stopDateStr.lastIndexOf(",")));
		}
		if (weekDayStr.length() > 0) {
			dts.setWeekDay(weekDayStr.substring(0, weekDayStr.lastIndexOf(",")));
		}
		return dts;
	}

	/**
	 * 根据settingType获取预约规则启用标志
	 * @param shopId
	 * @param settingType 配送=1；到店=2；外卖=3；预约上门=4
	 * @return
	 * @throws Exception
	 */
	private Integer getBookFlag(Long shopId,Integer settingType) throws Exception{
		Integer flag = null;
		if(settingType == CommonConst.TAKEOUT_SETTINGTYPE_PS 
				|| settingType == CommonConst.TAKEOUT_SETTINGTYPE_DD){//配送(便利店)
			flag = this.shopDao.getBookFlag(shopId);
		}else if(settingType == CommonConst.TAKEOUT_SETTINGTYPE_WM){//外卖
			flag = this.shopDao.getTakeoutFlag(shopId);
		}else if(settingType == CommonConst.TAKEOUT_SETTINGTYPE_SM){//预约上门
			flag = this.shopDao.getIsHomeService(shopId);
		}
		return flag;
	}
	
	
	
	public Long addDistribTakeoutSetting(DistribTakeoutSettingDto dto)
			throws Exception {
		return this.distribTakeoutSettingDao.addDistribTakeoutSetting(dto);
	}

	
	
	
}
