package com.idcq.appserver.service.msgPush.appPush;

import com.idcq.appserver.dto.message.AppPushRecordDto;

public interface PushRecordService
{
    void sendRecord(AppPushRecordDto appPushRecordDto);
    void updateRecord(AppPushRecordDto appPushRecordDto);
}
