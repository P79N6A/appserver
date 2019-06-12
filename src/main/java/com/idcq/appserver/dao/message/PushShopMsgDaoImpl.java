package com.idcq.appserver.dao.message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.utils.jedis.DataCacheApi;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.message.PushShopMsgDto;

@Repository
public class PushShopMsgDaoImpl extends BaseDao<PushShopMsgDto> implements IPushShopMsgDao{

	public int insertSelective(PushShopMsgDto dto)throws Exception {
		try
		{
			DataCacheApi.del("shop_msgC_" + dto.getShopId());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return this.insert(generateStatement("insertSelective"), dto);
	}

	public int batchInsertSelective(List<PushShopMsgDto> list) throws Exception{
		Iterator<PushShopMsgDto> iterator = list.iterator();
		while (iterator.hasNext()){
			try
			{
				DataCacheApi.del("shop_msgC_" + iterator.next().getShopId());
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return this.insert(generateStatement("batchInsertSelective"), list);
	}

	@Override public int countMsgByConditions(Map<String, Object> searchParams)
	{
		return (int)this.selectOne(generateStatement("countMsgByConditions"), searchParams);
	}

	@Override public List<PushShopMsgDto> searchMsgByConditions(Map<String, Object> searchParams)
	{
		return (List)super.getSqlSession().selectList(generateStatement("searchMsgByConditions"), searchParams);
	}

	@Override public void updateShopPushMsgStatus(Map<String, Object> updateParams)
	{
		this.update(generateStatement("updateShopPushMsgStatus"), updateParams);
	}
}
