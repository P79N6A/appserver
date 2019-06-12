package com.idcq.appserver.dao.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.message.MessageCenterDto;

@Repository
public class MessageCenterDaoImpl extends BaseDao<MessageCenterDto>implements IMessageCenterDao{

	@Override
	public int addMessageCenterDto(MessageCenterDto messageCenterDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addMessageCenterDto",messageCenterDto);
	}

	@Override
	public int delMessageCenterDtoById(Long messageCenterDtoId)
			throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delMessageCenterDtoById",messageCenterDtoId);
	}

	@Override
	public int updateMessageCenterDtoById(MessageCenterDto messageCenterDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateMessageCenterDtoById",messageCenterDto);
	}

	@Override
	public MessageCenterDto getMessageCenterDtoById(Long messageCenterDtoId)
			throws Exception {
		// TODO Auto-generated method stub
		return (MessageCenterDto)super.selectOne("getMessageCenterDtoById",messageCenterDtoId);
	}

	@Override
	public List<MessageCenterDto> getMessageCenterDtoList(
			MessageCenterDto messageCenterDto, int pageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("clientSystemType",messageCenterDto.getClientSystemType());
		map.put("bizId",messageCenterDto.getBizId());
		map.put("bizType",messageCenterDto.getBizType());
		map.put("feedbackType",messageCenterDto.getFeedbackType());
		map.put("receiverId",messageCenterDto.getReceiverId());
		map.put("notifyType ",messageCenterDto.getNotifyType());
		map.put("limit",(pageNo - 1) * pageSize);
		map.put("pageSize",pageSize);
		return super.findList("getMessageCenterDtoList",map);
	}

	@Override
	public int getMessageCenterDtoListCount(MessageCenterDto messageCenterDto)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("clientSystemType",messageCenterDto.getClientSystemType());
		map.put("bizId",messageCenterDto.getBizId());
		map.put("bizType",messageCenterDto.getBizType());
		map.put("feedbackType",messageCenterDto.getFeedbackType());
		map.put("receiverId",messageCenterDto.getReceiverId());
		map.put("notifyType ",messageCenterDto.getNotifyType());
		return (int)super.selectOne("getMessageCenterDtoListCount", map);
	}

	@Override
	public int updateFeedBackTypeById(Long messageCenterDtoId, Integer bizType,  int feedBackType, Integer notifyType, Integer receiverId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("messageCenterDtoId",messageCenterDtoId);
		map.put("feedBackType",feedBackType);
		map.put("notifyType",notifyType);
		map.put("receiverId",receiverId);
		map.put("bizType",bizType);
		return super.update("updateFeedBackTypeById",map);
	}

	@Override
	public int getCountById(Long messageCenterDtoId, Integer bizType,  Integer notifyType, Integer receiverId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("messageCenterDtoId",messageCenterDtoId);
		map.put("notifyType",notifyType);
		map.put("receiverId",receiverId);
		map.put("bizType",bizType);
		return (int)super.selectOne("getCountById", map);
	}
	
	
	
}