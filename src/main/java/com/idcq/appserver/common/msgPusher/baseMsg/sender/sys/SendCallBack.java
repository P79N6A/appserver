package com.idcq.appserver.common.msgPusher.baseMsg.sender.sys;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult;

public interface SendCallBack
{   
    /**
     * 
     * @param pushMsg
     */
    void onSuccess(PushResult rs);
    
    /**
     * 
     * @param errorCode 这里暂时取为剩作调用频率
     * @param pushMsg
     */
    void onFailed(PushResult rs);
}
