package com.idcq.appserver.service.attention;

import com.idcq.appserver.dto.attention.AttentionDto;
import com.idcq.appserver.dto.common.PageModel;

public interface IAttentionServcie {

	
	/**
	 * 获取我的关注列表
	 * 
	 * @param atten
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getAttenList(AttentionDto atten,int page,int pageSize) throws Exception ;
}
