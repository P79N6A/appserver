package com.idcq.appserver.dao.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.message.MessageDto;
import com.idcq.appserver.utils.RandomUtil;

/**
 * 消息dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:57:36
 */
@Repository
public class MessageDaoImpl extends BaseDao<MessageDto>implements IMessageDao{
	
	public List<MessageDto> getMsgList(MessageDto message,int page,int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);
		return super.findList(generateStatement("getMsgList"), map);

	}
	
	public int getMsgListCount(MessageDto message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message); 
		return (Integer) super.selectOne(generateStatement("getMsgListCount"), map);

	}

	
	public Integer addMessage(MessageDto messageDto) {
		List<MessageDto2> list = new ArrayList<MessageDto2>();
		for(int i = 1 ;i<=5000;i++){
			MessageDto2 message = new MessageDto2();
			Integer id = RandomUtil.getIntNum(666666, 999999999);
			message.setId(Long.parseLong(id.toString()));
			message.setCityId(1);
			message.setDistrictId(1);
			message.setMessageStatus(1);
			message.setMsgDesc("备注");
			message.setMsgImg("图片.jpg");
			message.setMsgTitle("标题");
			message.setMsgType(0);
			message.setProvinceId(1);
			message.setPubTime(new Date());
			message.setPubUserId(123456);
			message.setShopId(Long.parseLong("218"));
			message.setTargetUserType("会员");
			list.add(message);
		}
		return (Integer) super.insert(generateStatement("addMessage"),list);
	}

	@Override
	public MessageDto getMsgById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		MessageDto message = new MessageDto();
		message.setMessageId(id);
		map.put("message", message);

		MessageDto messageDto = (MessageDto) super.selectOne(
				generateStatement("getMsgById"), map);
		if (messageDto != null && messageDto.getShopId() != null) {
			messageDto.setShopName((String) selectOne(
					generateStatement("getShopNameById"),
					messageDto.getShopId()));
		}

		return messageDto;
	}

	@Override
	public void updateMessage(MessageDto messageDto) {
		super.update("updateMessage", messageDto);
	}

}
