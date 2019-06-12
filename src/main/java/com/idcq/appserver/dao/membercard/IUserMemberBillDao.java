package com.idcq.appserver.dao.membercard;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.membercard.UserMemberBillDto;

public interface IUserMemberBillDao {
	/**
	 * 查询用户会员卡账单
	 */
	List<UserMemberBillDto> getListUserBill(Integer pageNo, Integer pageSize,Map<String,Object> map) ;
	
	int getCountUserBill(Map<String,Object> map) ;
	
	/**
	 * 保存用户充值到账单表
	 * @param userCharge
	 * @throws Exception
	 */
	void saveChargeBill(UserChargeDto userCharge) throws Exception;
	/**
	 * 根据交易表id更新用户账单信息
	 * @param userBillDto
	 * @throws Exception
	 */
	void updateUsesrBillByTransactionId(UserChargeDto userChargeDto)throws Exception;
	
}
