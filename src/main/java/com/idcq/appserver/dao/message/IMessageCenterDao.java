package com.idcq.appserver.dao.message;

import java.util.List;

import com.idcq.appserver.dto.message.MessageCenterDto;
/**
 * 消息中心dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IMessageCenterDao {
	
	/**
	 * 新增消息中心
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	int addMessageCenterDto(MessageCenterDto messageCenterDto) throws Exception;
	
	/**
	 * 删除指定的消息中心
	 * @param messageCenterDtoId
	 * @return
	 * @throws Exception
	 */
	int delMessageCenterDtoById(Long messageCenterDtoId) throws Exception;
	
	/**
	 * 修改指定的消息中心
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	int updateMessageCenterDtoById(MessageCenterDto messageCenterDto) throws Exception;
	
	/**
	 * 修改反馈类型
	 * @param messageCenterDtoId
	 * @param feedBackType
	 * @param receiverId 
	 * @param notifyType 
	 * @param bizType 
	 * @return
	 * @throws Exception
	 */
	int updateFeedBackTypeById(Long messageCenterDtoId, Integer bizType, int feedBackType, Integer notifyType, Integer receiverId) throws Exception;
	
	/**
	 * 获取指定的消息中心
	 * @param messageCenterDtoId
	 * @return
	 * @throws Exception
	 */
	MessageCenterDto getMessageCenterDtoById(Long messageCenterDtoId) throws Exception;
	
	/**
	 * 统计数量
	 * @param messageCenterDtoId
	 * @param receiverId 
	 * @param notifyType 
	 * @param receiverId2 
	 * @param bizType 
	 * @return
	 * @throws Exception
	 */
	int getCountById(Long messageCenterDtoId, Integer bizType, Integer notifyType, Integer receiverId) throws Exception;
	
	/**
	 * 获取消息中心列表
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	List<MessageCenterDto> getMessageCenterDtoList(MessageCenterDto messageCenterDto,int pageNo,int pageSize) throws Exception;
	
	/**
	 * 获取消息中心列表数量
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	int getMessageCenterDtoListCount(MessageCenterDto messageCenterDto) throws Exception;
	
}
