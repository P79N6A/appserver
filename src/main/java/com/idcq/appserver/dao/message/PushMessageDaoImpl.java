package com.idcq.appserver.dao.message;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;

/**
 * 消息dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:57:36
 */
@Repository
public class PushMessageDaoImpl extends BaseDao<Map> implements IPushMessageDao{
	
	public int saveRegistrationID(String registrationID) throws Exception {
		return super.insert("saveRegistrationID", registrationID);
	}
	
}
