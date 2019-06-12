package com.idcq.appserver.service.message;

import com.idcq.appserver.dto.common.PageModel;

import java.util.Map;

public interface IPushMessageService {
	
	int saveRegistrationID(String registrationID) throws Exception ;

	PageModel getPushMsgs(Map<String, Object> searchParams);

	void informPushMsgs(Map<String, Object> requestParams);
	
}
