package com.idcq.appserver.dao.programconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.programconfig.ExecutePointDto;

@Repository
public class ExecutePointDaoImpl extends BaseDao<ExecutePointDto>implements IExecutePointDao{

	@Override
	public int addExecutePoint(ExecutePointDto executePointDto)
			throws Exception {
		return super.insert("addExecutePoint",executePointDto);
	}

	@Override
	public int delExecutePointById(Integer executePointId) throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delExecutePointById", executePointId);
	}

	@Override
	public int updateExecutePointById(ExecutePointDto executePointDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateExecutePointById", executePointDto);
	}

	@Override
	public ExecutePointDto getExecutePointById(Integer executePointId)
			throws Exception {
		// TODO Auto-generated method stub
		return (ExecutePointDto)super.selectOne("getExecutePointById", executePointId);
	}

	@Override
	public List<ExecutePointDto> getExecutePointList(
			ExecutePointDto executePointDto, int pageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start", (pageNo - 1) * pageSize);
		map.put("limit", pageSize);
		return super.findList("getExecutePointList",map);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.programconfig.IExecutePointDao#getExecutePointByCode(java.lang.String, java.lang.Integer)
     */
    @Override
    public ExecutePointDto getExecutePointByCode(String code, Integer pointType) throws Exception
    {
        Map<String,Object> parms = new HashMap<String, Object>();
        parms.put("code", code);
        parms.put("pointType", pointType);
        return (ExecutePointDto)super.selectOne("getExecutePointByCode", parms);
    }
	
	
}