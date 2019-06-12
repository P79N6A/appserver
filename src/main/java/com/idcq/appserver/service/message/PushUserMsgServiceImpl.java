package com.idcq.appserver.service.message;

import com.idcq.appserver.utils.Jpush;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.message.IPushUserMsgDao;
import com.idcq.appserver.dto.message.PushUserMsgDto;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PushUserMsgServiceImpl implements IPushUserMsgService{
	@Autowired
	public IPushUserMsgDao pushUserMsgDao;
	
	public int insertSelective(PushUserMsgDto dto) throws Exception{
		return this.pushUserMsgDao.insertSelective(dto);
	}

	@Override public void pushAllRemainedMsg()
	{
		Date curTime = new Date();
		PushUserMsgDto searchCondition = new PushUserMsgDto();
		//未发送消息
		searchCondition.setMessageStatus(2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curTime);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		searchCondition.setCreateTime(calendar.getTime());
		List<PushUserMsgDto> toUpdatesMsg = new ArrayList<>();
		int jobNum = pushUserMsgDao.countPushUserMsg(searchCondition);
		int start = 0;
		int pSize = 100;
		List<PushUserMsgDto> tempToPushes = null;
		RowBounds tempRowBounds = null;
		Iterator<PushUserMsgDto> iterator = null;
		PushUserMsgDto  tempDto = null;
		String title = null;
		while (start < jobNum){
			tempRowBounds = new RowBounds(start, pSize);
			tempToPushes = pushUserMsgDao.getPushUserMsg(searchCondition, tempRowBounds);
			iterator = tempToPushes.iterator();
			String msgContent = null;
			while(iterator.hasNext()){
				tempDto = iterator.next();
				title = tempDto.getTitle();
				msgContent = tempDto.getMessageContent();
				title = null == title ? msgContent : title;
				Jpush.sendPushToTarget(tempDto.getOsInfo(), tempDto.getUserType(), tempDto.getRegId(), title, msgContent);
				tempDto.setMessageStatus(0);
				tempDto.setSendTime(curTime);
				toUpdatesMsg.add(tempDto);
			}
			pushUserMsgDao.batchUpdateOrInsert(toUpdatesMsg);
			toUpdatesMsg.clear();
			start = start + pSize;
		}
	}
}
