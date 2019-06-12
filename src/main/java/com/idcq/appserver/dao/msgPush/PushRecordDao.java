package com.idcq.appserver.dao.msgPush;

import com.idcq.appserver.dto.message.AppPushRecordDto;

public interface PushRecordDao
{
    void saveRecord(AppPushRecordDto appPushRecordDto);
    
    void updateRecordById(AppPushRecordDto appPushRecordDto);
}
