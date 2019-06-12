package com.idcq.appserver.dao.lucene;

import java.util.List;

import com.idcq.appserver.dto.lucene.LuceneDto;

/**
 * lucene dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月23日
 * @time 下午3:23:34
 */
public interface ILuceneDao {
	
	List<LuceneDto> getLuceneList(LuceneDto lucene,Integer pNo,Integer pSize) throws Exception;
}
