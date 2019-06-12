package com.idcq.appserver.service.goods;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.goods.IShopTimeIntevalDao;
import com.idcq.appserver.dao.shop.IDistributionTakeoutDao;
import com.idcq.appserver.dto.common.PageModel;

@Service
public class ShopTimeIntevalServiceImpl implements IShopTimeIntevalService {

	@Autowired
	private IShopTimeIntevalDao shopTimeIntevalDao;
	@Autowired
	private IDistributionTakeoutDao distributionTakeoutDao;
	
	public PageModel getPage(Long shopId,Integer serverMode,Integer pageNo, Integer pageSize)
			throws Exception {
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		Map param=new HashMap();
		param.put("shopId", shopId);
		
		List<Map> result=null;
		//获取商铺可预订的时间
		if(1==serverMode){//上门
			param.put("settingType", 4);
			param.put("ruleType", 1);
			result=distributionTakeoutDao.getDeliveryTime(param);
		}else{
			//到店
			param.put("settingType", 2);
			param.put("ruleType", 1);
			result=distributionTakeoutDao.getDeliveryTime(param);
		}
		
		List<Map> listFinal = new ArrayList<Map>();
		if(null != result && result.size()>0){
			int start=1;
			Long interval=(long) (30*60*1000);//时间间隔
			for(int i=0,len=result.size();i<len;i++){
				List<Map> temp=getTimeInterval((Time)result.get(i).get("begin_time"),(Time)result.get(i).get("end_time"),interval,start);
				start+=temp.size();
				listFinal.addAll(temp);
			}
		}
		page.setTotalItem(listFinal.size());
		page.setList(listFinal);
		return page;
	}
	
	/**
	 * 
	 * @param begin
	 * @param end
	 * @param interval 切割时间长度
	 * @param start 顺序标记
	 * @return
	 */
	public List<Map> getTimeInterval(Time begin,Time end,long interval,int start){
		DateFormat df = new SimpleDateFormat("HH:mm");
		Long beginTime=begin.getTime();
		Long endTime=end.getTime();
		//Long interval=(long) (30*60*1000);//时间间隔
		List<Map> timeInterval=new ArrayList<Map>();
		Map temp = new HashMap();;
		Long tempVal = beginTime;
		temp.put("showIndex", start);
		temp.put("startTime", df.format(beginTime));
		timeInterval.add(temp);
		while (true) {
			start++;
			temp = new HashMap();
			tempVal = tempVal + interval;
			if (endTime > tempVal) {
				temp.put("showIndex", start);
				temp.put("startTime", df.format(tempVal));
				timeInterval.add(temp);
			}else{
				break;
			}
		}
		/*Long beginTime=null;
		Long endTime=null;
		try {
			beginTime = df.parse(begin).getTime();
			endTime = df.parse(end).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long interval=(long) (30*60*1000);//时间间隔
		List<Map> timeInterval=new ArrayList<Map>();
		Map temp = null;
		int i = 1;
		Long tempVal = null;
		temp.put("showIndex", i);
		temp.put("startTime", beginTime);
		while (true) {
			i++;
			temp = new HashMap();
			tempVal = beginTime + interval;
			if (endTime > tempVal) {
				temp.put("showIndex", i);
				temp.put("startTime", tempVal);
				timeInterval.add(temp);
			}else{
				break;
			}
		}*/
		return timeInterval;
	}
	
}
