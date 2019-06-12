package com.idcq.appserver.dao.help;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.HelpOf1dsxyDto;
import com.idcq.appserver.dto.help.HelpDto;

@Repository
public class HelpDao extends BaseDao<HelpDto> implements IHelpDao {

	@Override
	public List<HelpDto> getHelpList(HelpDto help, int page, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("help", help);
		map.put("n", (page - 1) * pageSize);
		map.put("m", pageSize);
		return findList(generateStatement("getHelpList"), map);
	}

	@Override
	public int getHelpCount(HelpDto help) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("help", help);
		return (Integer) selectOne(generateStatement("getHelpCount"), map);
	}

	@Override
	public int getHelpOfYDSXYCount(Map<String, Object> param) {
		return (Integer) selectOne(generateStatement("getHelpOfYDSXYCount"), param);
	}

	@Override
	public List<HelpOf1dsxyDto> getHelpOfYDSXYList(Map<String, Object> param) {
		return (List)super.findList(generateStatement("getHelpOfYDSXYList"),param);
	}

	

}
