package com.idcq.appserver.dao.user;

import com.idcq.appserver.dto.user.BranchOfficeDto;

import java.util.List;

public interface IBranchOfficeDao
{
	List<BranchOfficeDto> searchBranchOfficeByCondition(BranchOfficeDto BranchOffice);
}
