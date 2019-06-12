package com.idcq.appserver.dao.common;

import java.util.List;

import com.idcq.appserver.dto.common.CodeDto;


public interface ICodeDao {
	
	/**
	 * 调用此接口获取code。同一codeType的code按次序号排序。
	 * @param codeType
	 * @return
	 * @throws Exception
	 */
	List<CodeDto> getCodeByType(String codeType) throws Exception;

	
}
