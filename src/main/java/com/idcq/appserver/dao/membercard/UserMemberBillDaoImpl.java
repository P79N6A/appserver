package com.idcq.appserver.dao.membercard;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.membercard.UserMemberBillDto;

@Repository
public class UserMemberBillDaoImpl extends BaseDao<UserMemberBillDto> implements
		IUserMemberBillDao {
	
	/**
	 * 查询用户会员卡账单接口
	 */
	public List<UserMemberBillDto> getListUserBill(Integer pageNo, Integer pageSize,
			Map<String, Object> map) {
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		
		return super.findList(this.generateStatement("getListUserBill"), map);
	}

	public int getCountUserBill(Map<String,Object> map){
		return (Integer)this.selectOne(this.generateStatement("getCountUserBill"), map);
	}

	public void saveChargeBill(UserChargeDto userCharge) throws Exception {
		
		super.insert(this.generateStatement("saveCharge"), userCharge);
	}


	public void updateUsesrBillByTransactionId(UserChargeDto userChargeDto) {
		super.update(this.generateStatement("updateUsesrBillByTransactionId"), userChargeDto);
	}

}
