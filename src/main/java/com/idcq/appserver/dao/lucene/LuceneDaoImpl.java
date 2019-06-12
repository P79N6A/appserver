package com.idcq.appserver.dao.lucene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.lucene.LuceneDto;

@Repository
public class LuceneDaoImpl extends BaseDao<LuceneDto> implements ILuceneDao{

	public List<LuceneDto> getLuceneList(LuceneDto lucene, Integer pNo, Integer pSize)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", (pNo-1)*pSize);
		map.put("m", pSize);
		map.put("lucene", lucene);
		return (List<LuceneDto>)findList(generateStatement("getLuceneList"),lucene);
	}
	
}
