package com.idcq.appserver.service.shop;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.shop.IHandoverInfoDao;
import com.idcq.appserver.dto.shop.HandoverInfoDto;
import com.idcq.appserver.exception.ValidateException;

/**
 * 交接班service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:22:35
 */
@Service
public class HandoverInfoServiceImpl implements IHandoverInfoServcie
{

    private static final Logger logger = Logger.getLogger(HandoverInfoServiceImpl.class);

  
    @Autowired
    private IHandoverInfoDao handoverInfoDao;

    
   

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#insertShopHandoverInfo(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public Long insertShopHandoverInfo(HandoverInfoDto handoverInfoDto)
			throws Exception {
		
	    if(handoverInfoDto.getHandoverInfoId() != null) {
	        int count  = handoverInfoDao.getShopHandoverInfoListCount(handoverInfoDto); 
	        if(count>0){
	            throw new ValidateException(CodeConst.CODE_FINAL_STATUS_ERROR,"交接班记录已经存在,handoverCode:"+handoverInfoDto.getHandoverCode());
	        } 
	    }
		
		return handoverInfoDao.insertShopHandoverInfo(handoverInfoDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getShopHandoverInfoList(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public List<HandoverInfoDto> getShopHandoverInfoList(
			HandoverInfoDto handoverInfoDto) throws Exception {
		
		return handoverInfoDao.getShopHandoverInfoList(handoverInfoDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#getShopHandoverInfoListCount(com.idcq.appserver.dto.shop.HandoverInfoDto)
	 */
	@Override
	public int getShopHandoverInfoListCount(HandoverInfoDto handoverInfoDto)
			throws Exception {
		
		return handoverInfoDao.getShopHandoverInfoListCount(handoverInfoDto);
	}
}
