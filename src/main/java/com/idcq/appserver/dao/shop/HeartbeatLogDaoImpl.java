package com.idcq.appserver.dao.shop;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.HeartbeatLogDto;

/**
 * 心跳日志dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class HeartbeatLogDaoImpl extends BaseDao<HeartbeatLogDto> implements IHeartbeatLogDao
{

    @Override
    public HeartbeatLogDto getHeartbeat( Long shopId,Integer systemType) throws Exception
    {
        HeartbeatLogDto heartbeatLogDto = new HeartbeatLogDto();
        heartbeatLogDto.setShopId(shopId);
        heartbeatLogDto.setSystemType(systemType);
        return this.getSqlSession().selectOne(generateStatement("getHeartbeat"), heartbeatLogDto);
    }

    @Override
    public Integer addHeartbeat(HeartbeatLogDto heartbeatLogDto) throws Exception
    {
        return insert("insertHeartbeat", heartbeatLogDto);
    }

    @Override
    public Integer updateHeartbeat(HeartbeatLogDto heartbeatLogDto) throws Exception
    {
        return update("updateHeartbeat", heartbeatLogDto);
    }

}
