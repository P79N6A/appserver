package com.idcq.appserver.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.message.IPushShopMsgDao;
import com.idcq.appserver.dto.message.PushShopMsgDto;

@Service
public class PushShopMsgServiceImpl implements IPushShopMsgService{
	@Autowired
	public IPushShopMsgDao pushShopMsgDao;
	
	public int insertSelective(PushShopMsgDto dto) throws Exception{
		return this.pushShopMsgDao.insertSelective(dto);
	}

}
