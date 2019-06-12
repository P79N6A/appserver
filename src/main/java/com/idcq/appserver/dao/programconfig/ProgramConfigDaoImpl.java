package com.idcq.appserver.dao.programconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.programconfig.ExecutePointDto;
import com.idcq.appserver.dto.programconfig.ProgramConfigDto;

@Repository
public class ProgramConfigDaoImpl extends BaseDao<ProgramConfigDto>implements IProgramConfigDao{

	@Override
	public int addProgramConfig(ProgramConfigDto programConfigDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addProgramConfig",programConfigDto);
	}

	@Override
	public int delProgramConfigById(Integer programConfigId) throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delProgramConfigById", programConfigId);
	}

	@Override
	public int updateProgramConfigById(ProgramConfigDto programConfigDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateProgramConfigById",programConfigDto);
	}

	@Override
	public ProgramConfigDto getProgramConfigById(Integer programConfigId)
			throws Exception {
		// TODO Auto-generated method stub
		return (ProgramConfigDto)super.selectOne("getProgramConfigById", programConfigId);
	}

	@Override
	public List<ProgramConfigDto> getProgramConfigList(
			ProgramConfigDto programConfigDto, int pageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start", (pageNo - 1) * pageSize);
		map.put("limit", pageSize);
		return super.findList("getProgramConfigList",map);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.programconfig.IProgramConfigDao#getBeanByExecutePointCode(com.idcq.appserver.dto.programconfig.ExecutePointDto)
     */
    @Override
    public ProgramConfigDto getBeanByExecutePointCode(ExecutePointDto executePointDto) throws Exception
    {
        return (ProgramConfigDto)super.selectOne("getBeanByExecutePointCode", executePointDto);

    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.programconfig.IProgramConfigDao#getBeanByProgramConfigCode(com.idcq.appserver.dto.programconfig.ProgramConfigDto)
     */
    @Override
    public ProgramConfigDto getBeanByProgramConfigCode(ProgramConfigDto programConfigDto) throws Exception
    {
        return (ProgramConfigDto)super.selectOne("getBeanByProgramConfigCode", programConfigDto);
    }

	@Override
	public List<ProgramConfigDto> getProgramConfigListByExecPointId(
			Integer executePointId) throws Exception {
		// TODO Auto-generated method stub
		return super.findList("getProgramConfigListByExecPointId",executePointId);
	}
    
    

}