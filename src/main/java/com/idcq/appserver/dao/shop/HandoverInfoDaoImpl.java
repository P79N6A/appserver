package com.idcq.appserver.dao.shop;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.HandoverInfoDto;

/**
 * 交接班dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class HandoverInfoDaoImpl extends BaseDao<HandoverInfoDto>implements IHandoverInfoDao{

	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#insertShopHandoverInfo(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public Long insertShopHandoverInfo(HandoverInfoDto handoverInfoDto)
			throws Exception {
		
		super.insert("insertShopHandoverInfo", handoverInfoDto);
		
		return handoverInfoDto.getHandoverInfoId();
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getShopHandoverInfo(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public List<HandoverInfoDto> getShopHandoverInfoList(
			HandoverInfoDto handoverInfoDto) throws Exception {
		
		return super.getSqlSession().selectList(generateStatement("getShopHandoverInfoList"), handoverInfoDto);
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getShopHandoverInfoListCount(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public int getShopHandoverInfoListCount(HandoverInfoDto handoverInfoDto)
			throws Exception {
		return super.getSqlSession().selectOne(generateStatement("getShopHandoverInfoListCount"), handoverInfoDto);

	}
	
	
	
	
	
}
