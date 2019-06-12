package com.idcq.appserver.dao.shop;

import com.idcq.appserver.dto.shop.HeartbeatLogDto;

public interface IHeartbeatLogDao {
	
	
	/**
     * 查询心跳
     * @param heartbeatLogDto
     * @return
     * @throws Exception
     */
	HeartbeatLogDto getHeartbeat(Long shopId,Integer systemType) throws Exception;
    /**
     * 查询心跳
     * @param heartbeatLogDto
     * @return
     * @throws Exception
     */
    Integer addHeartbeat(HeartbeatLogDto heartbeatLogDto) throws Exception;
    /**
     * 查询心跳
     * @param heartbeatLogDto
     * @return
     * @throws Exception
     */
    Integer updateHeartbeat(HeartbeatLogDto heartbeatLogDto) throws Exception;

}
