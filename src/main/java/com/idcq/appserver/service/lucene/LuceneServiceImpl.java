package com.idcq.appserver.service.lucene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.lucene.ILuceneDao;
import com.idcq.appserver.dto.lucene.LuceneDto;

@Service
public class LuceneServiceImpl implements ILuceneService{
	@Autowired
	public ILuceneDao luceneDao;
	
	public List<LuceneDto> getLuceneList(LuceneDto lucene, int pNo, int pSize)
			throws Exception {
		return this.luceneDao.getLuceneList(lucene,pNo,pSize);
	}
	
}
