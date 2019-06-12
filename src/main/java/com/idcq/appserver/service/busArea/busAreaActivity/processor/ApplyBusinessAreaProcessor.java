package com.idcq.appserver.service.busArea.busAreaActivity.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;

@Service("ApplyBusinessAreaProcessor")
public class ApplyBusinessAreaProcessor implements IProcessor {

	@Autowired
	private IBusAreaActivityService busAreaActivityService;
	
	@Override
	public Object exective(Map<String, Object> params) throws Exception{
		return busAreaActivityService.applyBusinessArea(params);
	}

}
