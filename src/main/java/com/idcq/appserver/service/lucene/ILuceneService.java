package com.idcq.appserver.service.lucene;

import java.util.List;

import com.idcq.appserver.dto.lucene.LuceneDto;

/**
 * @author Administrator
 * 
 * @date 2015年3月23日
 * @time 下午3:41:48
 */
public interface ILuceneService {
	
	/**
	 * 分页获取商品及商铺的操作记录信息列表
	 * 
	 * @param lucene
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<LuceneDto> getLuceneList(LuceneDto lucene,int pNo,int pSize) throws Exception;
}
