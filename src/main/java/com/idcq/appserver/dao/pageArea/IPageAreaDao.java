package com.idcq.appserver.dao.pageArea;

import java.util.List;

import com.idcq.appserver.dto.pageArea.PageAreaDto;

public interface IPageAreaDao {

	/**
	 * 获取投放页面URL
	 * @Title: getPageAreaUrl 
	 * @param @param cityId
	 * @param @param positionType
	 * @param @return
	 * @param @throws Exception
	 * @return String    返回类型 
	 * @throws
	 */
	public List<PageAreaDto> getPageAreaUrl(String cityId,String positionType)throws Exception;
}
