package com.idcq.appserver.service.catering;

import com.idcq.appserver.dto.catering.CateringDto;
import com.idcq.appserver.dto.common.PageModel;

public interface ICateringServcie {

	/**
	 * 获取餐饮业服务列表
	 * 
	 * @param catering
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getList(CateringDto catering,int page,int pageSize) throws Exception ;
}
