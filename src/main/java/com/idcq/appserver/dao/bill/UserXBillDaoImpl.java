package com.idcq.appserver.dao.bill;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bill.UserXBillDto;

@Repository
public class UserXBillDaoImpl extends BaseDao<UserXBillDto> implements IUserXBillDao{

	public int insertUserXBillDao(UserXBillDto userXBill) throws Exception {
		
		return super.insert(generateStatement("insertUserXBillDao"), userXBill);
	}

	
}
