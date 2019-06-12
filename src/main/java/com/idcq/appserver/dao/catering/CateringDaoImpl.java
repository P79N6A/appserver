package com.idcq.appserver.dao.catering;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.catering.CateringDto;

/**
 * 餐饮业dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午4:09:38
 */
@Repository
public class CateringDaoImpl extends BaseDao<CateringDto>implements ICateringDao{

	public List<CateringDto> getList(CateringDto catering, int page, int pageSize)
			throws Exception {
		return null;
	}
}
