package com.idcq.appserver.dao.operationclassify;

import java.util.List;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.operationclassify.OperationClassifyDto;

public interface IOperationClassifyDao {
	
	PageModel getOperationClassify(String cityId,String pNo,String pSize,String parentClassifyId, int order)throws Exception;
	Integer getOperationClassifyCountByCondition(String cityId,String parentClassifyId)throws Exception;
	
	
	List<OperationClassifyDto> queryChildCountByParentList(List<Integer>idList)throws Exception;
	
	List<OperationClassifyDto> queryOperationClassify(OperationClassifyDto operationClassifyDto, Integer pSize, Integer pNo);
}
