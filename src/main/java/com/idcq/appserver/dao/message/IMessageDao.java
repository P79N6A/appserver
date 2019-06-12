package com.idcq.appserver.dao.message;

import java.util.List;

import com.idcq.appserver.dto.message.MessageDto;

public interface IMessageDao {
	
	/**
	 * 检索指定类型的消息列表
	 * 
	 * @param message
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<MessageDto> getMsgList(MessageDto message,int page,int pageSize) ;
	
	/**
	 * 统计指定类型消息记录数
	 * 
	 * @param message
	 * @return
	 */
	int getMsgListCount(MessageDto message) ;
	/**
	 * 测试增加数据
	 * @return
	 */
	Integer addMessage(MessageDto messageDto);
	
	/**
	 * 获取消息详情
	 * @param id
	 * @return
	 */
	MessageDto getMsgById(Long id);
	
	public void updateMessage(MessageDto messageDto);
	
}
