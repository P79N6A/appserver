package com.idcq.appserver.dao.message;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.message.PushUserMsgDto;

@Repository
public class PushUserMsgDaoImpl extends BaseDao<PushUserMsgDto> implements IPushUserMsgDao{

	public int insertSelective(PushUserMsgDto dto) throws Exception  {
		return this.insert(generateStatement("insertSelective"), dto);
	}

	public int batchInsertSelective(List<PushUserMsgDto> dtos) throws Exception {
		return this.insert(generateStatement("batchInsertSelective"), dtos);
	}

	@Override public List<PushUserMsgDto> getPushUserMsg(PushUserMsgDto pushUserMsgDto, RowBounds rowBounds)
	{
		return this.getSqlSession().selectList("getPushUserMsg", pushUserMsgDto, rowBounds);
	}

	@Override public int countPushUserMsg(PushUserMsgDto pushUserMsgDto)
	{
		return this.getSqlSession().selectOne("countPushUserMsg", pushUserMsgDto);
	}

	@Override public void batchUpdateOrInsert(List<PushUserMsgDto> pushUserMsgDtos)
	{
		this.getSqlSession().update("batchUpdateOrInsert", pushUserMsgDtos);
	}
}
