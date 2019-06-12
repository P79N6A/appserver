package com.idcq.appserver.dao.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
@Repository
public class SendSmsRecordDaoImpl extends BaseDao<SendSmsRecordDto> implements
		ISendSmsRecordDao {

	public int saveSendSmsRecord(SendSmsRecordDto dto) throws Exception {
		return super.insert(generateStatement("saveSendSmsRecord"), dto);
	}

	@Override
	public List<SendSmsRecordDto> getSmsListByMobile(String mobile)
			throws Exception {
		return findList(generateStatement("getSmsListByMobile"), mobile);
	}

	public SendSmsRecordDto getSendContentByMobileAndUsage(String mobile, String usage) throws Exception {
	    Map<String, Object> param = new HashMap<String, Object>();
        param.put("mobile", mobile);
        param.put("usage", usage);
		return (SendSmsRecordDto) selectOne(generateStatement("getSendContentByMobileAndUsage"), param);
	}

	public SendSmsRecordDto getSendSmsRecordDtoByMobileAndUsage(Map param)
			throws Exception {
		return (SendSmsRecordDto) selectOne(generateStatement("getSendSmsRecordDtoByMobileAndUsage"), param);
	}

	public int updateSendStatusExpire(String mobile) throws Exception {
		return super.update(generateStatement("updateSendStatusExpire"), mobile);
	}
	
    public int getSmsCountBy(String mobile, String usage, Integer hour) throws Exception
    {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mobile", mobile);
        param.put("usage", usage);
        param.put("hour", hour);
        return (int) selectOne(generateStatement("getSmsCountBy"), param);
    }

	@Override public int countRemainMsg(SendSmsRecordDto sendSmsRecordDto)
	{
		return (int)this.selectOne("countRemainMsg", sendSmsRecordDto);
	}

	@Override public List<SendSmsRecordDto> getRemainMsg(SendSmsRecordDto sendSmsRecordDto, RowBounds rowBounds)
	{
		return this.getSqlSession().selectList(generateStatement("getRemainMsg"), sendSmsRecordDto, rowBounds);
	}

	@Override public void updateStatusById(SendSmsRecordDto sendSmsRecordDto)
	{
		this.getSqlSession().update(generateStatement("updateStatusById"), sendSmsRecordDto);
	}
}
