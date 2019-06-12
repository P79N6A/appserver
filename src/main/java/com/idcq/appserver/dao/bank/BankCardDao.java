package com.idcq.appserver.dao.bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bank.BankCardDto;

/**
 * 银行卡dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 下午8:30:12
 */
@Repository
public class BankCardDao extends BaseDao<BankCardDto> implements IBankCardDao{

	public int saveBankCard(BankCardDto bankCard) throws Exception {
		return super.insert(generateStatement("saveBankCard"),bankCard);
	}

	public List<BankCardDto> getBankCardListByUser(BankCardDto bankCard,int pNo,int pSize)
			throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", bankCard.getUserId());
		map.put("n", (pNo-1)*pSize);
		map.put("m",pSize);		
		return super.findList(generateStatement("getBankCardListByUser"),map);
	}
	public int getBankCardCountByUser(Long userId)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getBankCardCountByUser"),userId);
	}

	public Integer getBankCardByMap(Long userId,String cardNumber) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("cardNumber", cardNumber);
		return (Integer) super.selectOne(generateStatement("getBankCardByMap"),map);
	}

	public int updateBankCardUseTime(String cardNumber, Long userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cardNumber", cardNumber);
		param.put("userId", userId);
		return super.update(generateStatement("updateBankCardUseTime"), param);
	}

	@Override
	public int unBindBankCard(BankCardDto bankCard) throws Exception {
		return super.delete(generateStatement("unBindBankCard"),bankCard);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.bank.IBankCardDao#getSubbranchNameByNum(java.lang.String)
	 */
	@Override
	public String getSubbranchNameByNum(String cardNumber) {
		return (String) super.selectOne(generateStatement("getSubbranchNameByNum"),cardNumber);

	}

	@Override
	public BankCardDto getBankCardByCard(String cardNumber, String userId,Integer accountType) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cardNumber", cardNumber);
		map.put("userId", userId);
		map.put("accountType", accountType);
		return (BankCardDto)selectOne("getBankCardByCard", map);
	}

}
