package com.idcq.appserver.dao.attention;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.attention.AttentionDto;

/**
 * 我的关注dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:58:13
 */
@Repository
public class AttentionDaoImpl extends BaseDao<AttentionDto>implements IAttentionDao{


	public List<AttentionDto> getAttenList(AttentionDto atten, int page,
			int pageSize) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
