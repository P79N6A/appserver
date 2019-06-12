package com.idcq.appserver.service.level.processor;

import com.idcq.appserver.dto.level.CalculatePointMessageModel;

public interface IPointProcessor {
	
	void processPoint(final CalculatePointMessageModel messageModel) throws Exception;
}
