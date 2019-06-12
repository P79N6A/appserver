package com.idcq.appserver.service.operationclassify;

import java.util.List;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.operationclassify.OperationClassifyDto;

public interface IOperationClassifyService {
	
	public PageModel getOperationClassify(String cityId,String parentClassifyId,String pNo,String pSize, int order)throws Exception;
	
	public List<OperationClassifyDto> queryOperationClassifyDto(OperationClassifyDto operationClassifyDto, Integer pSize, Integer pNo);
}
