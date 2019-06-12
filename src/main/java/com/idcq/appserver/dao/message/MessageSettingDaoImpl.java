package com.idcq.appserver.dao.message;

import org.springframework.stereotype.Repository;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.message.MessageSettingDto;

/**
 * 消息dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:57:36
 */
@Repository
public class MessageSettingDaoImpl extends BaseDao<MessageSettingDto>implements IMessageSettingDao{

	@Override
	public MessageSettingDto getMsgSettingByKey(String key) {
		return (MessageSettingDto) super.selectOne("getMsgSettingByKey", key);
	}
	
}
