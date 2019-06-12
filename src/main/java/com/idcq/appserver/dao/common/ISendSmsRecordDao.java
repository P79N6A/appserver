package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.sun.rowset.internal.Row;
import org.apache.ibatis.session.RowBounds;

public interface ISendSmsRecordDao {
	
	/**
	 * 保存短信发送记录
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public int saveSendSmsRecord(SendSmsRecordDto dto) throws Exception;
	
	public List<SendSmsRecordDto> getSmsListByMobile(String mobile) throws Exception;
	
	/**
	 * 根据手机号码短信内容
	 * @param mobile 手机号码
	 * @param usage 应用场景
	 * @return
	 * @throws Exception
	 */
	public SendSmsRecordDto getSendContentByMobileAndUsage(String mobile, String usage)throws Exception;
	
	/**
	 * 根据手机号码和场景获取手机验证码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SendSmsRecordDto getSendSmsRecordDtoByMobileAndUsage(Map param)throws Exception;
	
	/**
	 * 发送验证码之前，将该号码所有send_code is not null AND send_status=0的记录的send_status置为1
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public int updateSendStatusExpire(String mobile)throws Exception;
	
	/**
	 * 一段时间内同一手机号码同一场景下的已经发送的条数
	 * @param mobile 手机号码
	 * @param usage 应用场景
	 * @param hour 小时
	 * @return
	 * @author  shengzhipeng
	 */
	int getSmsCountBy(String mobile, String usage, Integer hour)throws Exception;

	int countRemainMsg(SendSmsRecordDto sendSmsRecordDto);

	List<SendSmsRecordDto> getRemainMsg(SendSmsRecordDto sendSmsRecordDto, RowBounds rowBounds);

	void updateStatusById(SendSmsRecordDto sendSmsRecordDto);
}
