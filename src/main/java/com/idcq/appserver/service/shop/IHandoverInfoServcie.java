package com.idcq.appserver.service.shop;

import java.util.List;

import com.idcq.appserver.dto.shop.HandoverInfoDto;

public interface IHandoverInfoServcie {

	
	
	/**
	 * 插入店铺交接班信息
	 */
	public Long insertShopHandoverInfo(HandoverInfoDto handoverInfoDto)throws Exception;
	/**
	 * 获取店铺交接班信息
	 */
	public List<HandoverInfoDto> getShopHandoverInfoList(
			HandoverInfoDto handoverInfoDto) throws Exception;
	/**
	 * 获取店铺交接班信息总记录
	 */
	public int getShopHandoverInfoListCount(HandoverInfoDto handoverInfoDto)
			throws Exception;


}
