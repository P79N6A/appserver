package com.idcq.appserver.dao.help;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.help.HelpCategoryDto;

@Repository
public class HelpCategoryDao extends BaseDao<HelpCategoryDto> implements
		IHelpCategoryDao {

	@Override
	public List<HelpCategoryDto> getHelpCategoryList(
			HelpCategoryDto helpCategory, int page, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("helpCategory", helpCategory);
		map.put("n", (page - 1) * pageSize);
		map.put("m", pageSize);
		return findList(generateStatement("getHelpCategoryList"), map);
	}

	@Override
	public int getHelpCategoryCount(HelpCategoryDto helpCategory) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("helpCategory", helpCategory);
		return (Integer) selectOne(generateStatement("getHelpCategoryCount"),
				map);
	}

}
