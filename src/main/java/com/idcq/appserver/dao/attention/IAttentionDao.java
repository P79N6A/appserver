package com.idcq.appserver.dao.attention;

import java.util.List;

import com.idcq.appserver.dto.attention.AttentionDto;

public interface IAttentionDao {
	
	/**
	 * 获取我的关注列表
	 * 
	 * @param atten
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<AttentionDto> getAttenList(AttentionDto atten,int page,int pageSize) throws Exception ;
}
