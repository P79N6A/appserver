package com.idcq.appserver.dao.programconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.programconfig.ExecuteDetailDto;

@Repository
public class ExecuteDetailDaoImpl extends BaseDao<ExecuteDetailDto>implements IExecuteDetailDao{

	@Override
	public int addExecuteDetail(ExecuteDetailDto executeDetailDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addExecuteDetail",executeDetailDto);
	}

	@Override
	public int delExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto) throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delExecuteDetailById", executeDetailDto);
	}

	@Override
	public int updateExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateExecuteDetailByCompKey",executeDetailDto);
	}

	@Override
	public ExecuteDetailDto getExecuteDetailByCompKey(ExecuteDetailDto executeDetailDto)
			throws Exception {
		// TODO Auto-generated method stub
		return (ExecuteDetailDto)super.selectOne("getExecuteDetailByCompKey", executeDetailDto);
	}

	@Override
	public List<ExecuteDetailDto> getExecuteDetailList(
			ExecuteDetailDto executeDetailDto, int pageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start", (pageNo - 1) * pageSize);
		map.put("limit", pageSize);
		return super.findList("getExecuteDetailList",map);
	}

	
	
	
}