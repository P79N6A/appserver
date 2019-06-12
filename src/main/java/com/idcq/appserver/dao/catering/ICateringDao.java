package com.idcq.appserver.dao.catering;

import java.util.List;

import com.idcq.appserver.dto.catering.CateringDto;

public interface ICateringDao {
	
	/**
	 * 获取餐饮业服务列表
	 * 
	 * @param catering
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<CateringDto> getList(CateringDto catering,int page,int pageSize) throws Exception ;
}
