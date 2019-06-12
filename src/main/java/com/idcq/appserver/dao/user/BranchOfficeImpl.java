package com.idcq.appserver.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.BranchOfficeDto;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class BranchOfficeImpl extends BaseDao<BranchOfficeDto> implements IBranchOfficeDao{

	@Override public List<BranchOfficeDto> searchBranchOfficeByCondition(BranchOfficeDto BranchOffice)
	{
		return this.getSqlSession().selectList(generateStatement("searchBranchOfficeByCondition"), BranchOffice);
	}
}
