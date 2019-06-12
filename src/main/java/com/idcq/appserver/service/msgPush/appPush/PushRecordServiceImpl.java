package com.idcq.appserver.service.msgPush.appPush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.msgPush.PushRecordDao;
import com.idcq.appserver.dto.message.AppPushRecordDto;

@Service
public class PushRecordServiceImpl implements PushRecordService
{   
    private static final Logger log = LoggerFactory.getLogger(PushRecordServiceImpl.class);
    @Autowired
    private PushRecordDao recordDao;
    
    @Override
    public void sendRecord(AppPushRecordDto appPushRecordDto)
    {
        this.recordDao.saveRecord(appPushRecordDto);
    }
    @Override
    public void updateRecord(AppPushRecordDto appPushRecordDto)
    {
        this.recordDao.updateRecordById(appPushRecordDto);
    }
    
    
}
