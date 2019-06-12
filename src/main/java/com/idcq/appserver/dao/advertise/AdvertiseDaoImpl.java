package com.idcq.appserver.dao.advertise;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.advertise.AdvertiseDto;
import com.idcq.appserver.utils.DateUtils;

/**
 * 广告dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午8:52:24
 */
@Repository
public class AdvertiseDaoImpl extends BaseDao<AdvertiseDto>implements IAdvertiseDao{
	
	public List<AdvertiseDto> getAdList(AdvertiseDto advertise, int page,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ad", advertise);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);    
//		map.put("curTime2", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		return super.findList(generateStatement("getAdList"), map);
	}

	public int getAdListCount(AdvertiseDto advertise) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ad", advertise);
//		map.put("curTime2", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		return (Integer) super.selectOne(generateStatement("getAdListCount"), map);
	}
	public static void main(String[] args) {
		System.out.println(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
	}
	
	
}
