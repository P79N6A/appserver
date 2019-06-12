package com.idcq.appserver.dao.bank;

import java.util.List;

import com.idcq.appserver.dto.bank.BankCardDto;

public interface IBankCardDao {
	
	/**
	 * 新增银行卡
	 * 
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	int saveBankCard(BankCardDto bankCard) throws Exception;
	/**
	 * 解绑银行卡
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public int unBindBankCard(BankCardDto bankCard) throws Exception;
	
	/**
	 * 获取制定用户的银行卡列表
	 * 
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	List<BankCardDto> getBankCardListByUser(BankCardDto bankCard,int pNo,int pSize) throws Exception;
	/**
	 * 查询用户银行卡总记录数
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public int getBankCardCountByUser(Long userId)
			throws Exception;
	/**
	 * 根据userId和银行卡Id查询具体银行卡信息
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	Integer getBankCardByMap(Long userId,String cardNumber) throws Exception;
	/**
	 * 更新银行卡最后使用时间
	 * @return
	 */
	int updateBankCardUseTime(String cardNumber, Long userId);
	/**
	 * 更新银行卡最后使用时间
	 * @return
	 */
	String getSubbranchNameByNum(String cardNumber);
	
	BankCardDto getBankCardByCard(String cardNumber, String userId,Integer accountType) throws Exception;
}
