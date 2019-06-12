package com.idcq.appserver.dao.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class UserBillDaoImpl extends BaseDao<UserBillDto> implements IUserBillDao{
	
	public UserAccountDto getAccountMoney(Long userId) throws Exception {
		return (UserAccountDto) super.selectOne(generateStatement("getAccountMoney"),userId) ;
	}

	public List<UserBillDto> getUserBill(Map param) throws Exception {
		return super.findList(generateStatement("getUserBill"), param);
	}
	
	public int getUserBillTotal(Map param) {
		return (Integer)super.selectOne(generateStatement("getUserBillTotal"), param);
	}

	@Override
    public int insertUserBill(UserBillDto userBillDto) throws Exception {
        Double money = NumberUtil.formatDouble(userBillDto.getMoney(), 4);
        userBillDto.setMoney(money);
        if (money == 0) {
            return 0;
        }
        userBillDto.setMoney(money);
        userBillDto.setSettleTime(new Date());
        return super.insert("insertUserBill", userBillDto);
    }
	public UserBillDto getUserBillByUserId(UserBillDto userBillDto)
			throws Exception {
		return (UserBillDto)this.selectOne(this.generateStatement("getUserBillByUserId"), userBillDto);
	}

	@Override
	public Map<String, Object> getUserBillById(Long billId) throws Exception {
		return (Map<String, Object>)selectOne(this.generateStatement("getUserBillById"), billId);
	}
	
	@Override
	public List<UserBillDto> getUserBillByDto(UserBillDto userBillDto) throws Exception {
		return (List<UserBillDto>) findList(this.generateStatement("getUserBillByDto"), userBillDto);
	}

	@Override
	public void updateStatusByTransactionId(UserChargeDto userBillDto) {
		update("updateStatusByTransactionId",userBillDto);
	}

	@Override
	public void updateUserBillStatusBy(Long billId, Integer billStatusFlag, Integer billStatus) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("billStatusFlag", billStatusFlag);
		map.put("billStatus", billStatus);
		update("updateUserBillStatusBy",map);
	}

    public int insertUserBillMiddle(UserBillDto userBillDto) throws Exception
    {
        Double money = NumberUtil.formatDouble(userBillDto.getMoney(), 4);
        userBillDto.setMoney(money);
        if (money == 0) {
            return 0;
        }
        userBillDto.setMoney(money);
        return super.insert("insertUserBillMiddle", userBillDto);
    }

    public List<UserBillDto> getUserBillMiddleByOrderId(String orderId) throws Exception
    {
        return (List<UserBillDto>) findList(this.generateStatement("getUserBillMiddleByOrderId"), orderId);
    }

    public void deleteUserBillMiddle(String orderId, List<Long> userBillIds)
    {
        Map map = new HashMap();
        map.put("orderId", orderId);
        map.put("userBillIds", userBillIds);
        super.delete(generateStatement("deleteUserBillMiddle"), map);
    }

	public Double getUserAccountingStat(Map<String, Object> params)
			throws Exception {
		return (Double) super.selectOne(generateStatement("getUserAccountingStat"), params);
	}

	public List<Map<String, Object>> getAgentRewardList(Map<String, Object> params)
			throws Exception {
		return (List) super.findList(generateStatement("getAgentRewardList"),params);
	}

	@Override
	public Integer allrewardsCount(Map<String, Object> map) throws Exception {
		return (Integer)selectOne("allrewardsCount", map);
	}

	@Override
	public List<Map<String, Object>> allrewards(Map<String, Object> map)
			throws Exception {
		return (List)findList("allrewards", map);
	}

	@Override 
	public List<Integer> getMyRewardType(Long userId, String[] accountTypes)
	{
	    Map map = new HashMap();
        map.put("userId", userId);
        map.put("accountTypes", accountTypes);
		return (List)findList("getMyRewardType", map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserBillDao#getRewardTotalMoney(java.lang.Long, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date)
	 */
	@Override
	public Map<String, Object> getTotalMoneyByWithdraw(Long userId,
			Integer billDirection, Integer accountType, Date startTime,
			Date endTime) throws Exception {
		Map<String, Object>  parms = new HashMap<String, Object>();
		parms.put("userId", userId);
		parms.put("billDirection", billDirection);
		parms.put("accountType", accountType);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
		return (Map<String, Object>) selectOne("getTotalMoneyByWithdraw", parms);

	}
}
