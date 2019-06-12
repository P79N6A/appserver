package com.idcq.appserver.service.common.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.service.common.ICommonService;

@Service("OperateBusAreaActFbProcessor")
public class OperateBusAreaActFbProcessor implements IProcessor {

	@Autowired
	private ICommonService commonService;
	
	@Override
	public Object exective(Map<String, Object> params) throws Exception{
		return commonService.BusAreaOperateFeedback(params);
	}

}
