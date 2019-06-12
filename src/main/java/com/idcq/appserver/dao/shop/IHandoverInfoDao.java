package com.idcq.appserver.dao.shop;

import java.util.List;

import com.idcq.appserver.dto.shop.HandoverInfoDto;

public interface IHandoverInfoDao {
	
	Long insertShopHandoverInfo(HandoverInfoDto handoverInfoDto) throws Exception;
	/**
	 * 获取商铺交接表list
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getShopHandoverInfo
	 * @Description:
	 *
	 * @param handoverInfoDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年10月21日 上午10:09:13
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年10月21日    ChenYongxin      v1.0.0         create
	 */
	public List<HandoverInfoDto> getShopHandoverInfoList(HandoverInfoDto handoverInfoDto)
			throws Exception ;
	
	public int getShopHandoverInfoListCount(HandoverInfoDto handoverInfoDto)
			throws Exception ;

}
