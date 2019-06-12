package com.idcq.appserver.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 会员账号dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class UserAccountDaoImpl extends BaseDao<UserAccountDto> implements IUserAccountDao{
	
	public UserAccountDto getAccountMoney(Long userId) throws Exception {
		return (UserAccountDto) super.selectOne(generateStatement("getAccountMoney"),userId) ;
	}

	public Map<String, Object> getRedPackUserDetail(Long userId) throws Exception{
		return (Map<String, Object>)super.selectOne("getRedPacketUserDetail", userId);
	}
	
	public void updateAccountMoney(Long userId,double amount) throws Exception {
		Map param=new HashMap();
		param.put("userId", userId);
		param.put("amount", amount);
		super.update("updateAccountMoney", param);
	}
	
	public void updateCouponAndRewardAmount(long userId,Double amount,Double coupon,Double reward,Double voucherAmount) throws Exception {
		Map param=new HashMap();
		param.put("userId", userId);
		param.put("amount",  amount == null ? null :NumberUtil.formatDouble(amount,4));
		param.put("rewardAmount", reward == null ? null : NumberUtil.formatDouble(reward,4));
		param.put("couponAmount", coupon == null ? null : NumberUtil.formatDouble(coupon,4));
		param.put("voucherAmount",  voucherAmount == null ? null : NumberUtil.formatDouble(voucherAmount,4));
		super.update("updateCouponAndRewardAmount", param);
	}
	
	public void saveAccount(UserAccountDto userAccountDto) throws Exception {
		super.insert(generateStatement("saveAccount"), userAccountDto);
	}

	public void backAccountMoney(Long userId, Double amount) throws Exception {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("amount",  NumberUtil.formatDouble(amount,4));
		super.update("backAccountMoney", param);
	}

	@Override
	public Integer updateFreezeMoney(Long userId, double freezeAmount)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("freezeAmount",  NumberUtil.formatDouble(freezeAmount,4));
		return super.update("updateFreezeMoney", param);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.user.IUserAccountDao#updateUserAccount(java.lang.Long, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double)
     */
    @Override
    public Integer updateUserAccount(Long userId, Double changeAmount, Double changeRewardAmount,
            Double changeRewardTotal, Double changeCouponAmount, Double changeFreezeAmount,
            Double legendTotal, Double consumeAmount, Double consumeTotal,Double voucherAmount,
            Double voucherTotal, Double deductionCountValue, Double deductionTotal,Double consumeRebateTotal,
            Double consumeRebateMoney) throws Exception
    {
        Map<String, Object> param = buildParam(userId, changeAmount, changeRewardAmount, changeRewardTotal,
                changeCouponAmount, changeFreezeAmount, legendTotal, consumeAmount, consumeTotal, voucherAmount,
                voucherTotal, deductionCountValue, deductionTotal, consumeRebateTotal, consumeRebateMoney, null);
       return  super.update("updateUserAccount", param);
    }

	public Integer updateUserAccountByKey(Long accountId, Double amount,
			Double rewardAmount, Double couponAmount, Double rewardTotal,
			Double freezeAmount,Double voucherAmount) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountId", accountId);
        param.put("amount", amount == null ? null : NumberUtil.formatDouble(amount,4));
        param.put("rewardAmount",  rewardAmount == null ? null :NumberUtil.formatDouble(rewardAmount,4));
        param.put("couponAmount",  couponAmount == null ? null :NumberUtil.formatDouble(couponAmount,4));
        param.put("rewardTotal",  rewardTotal == null ? null :NumberUtil.formatDouble(rewardTotal,4));
        param.put("freezeAmount",  freezeAmount == null ? null : NumberUtil.formatDouble(freezeAmount,4));
        param.put("voucherAmount",  voucherAmount == null ? null :NumberUtil.formatDouble(voucherAmount,4));
       return  super.update("updateUserAccountByKey", param);
	}
    
	@Override
	public Map<String, Object> rewardsum(Long userId) throws Exception {
		return (Map)selectOne("rewardsum", userId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserAccountDao#getAccountMoney(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<UserAccountDto> getAllAccount(Integer limit, Integer pageSize, Double money)
			throws Exception {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("limit", limit);
		parms.put("pageSize", pageSize);
		parms.put("money", money);
		return findList("getAllUserAccount",parms);
	}

    @Override
    public Integer updateUserAccount(Long userId, Double changeAmount, Double changeRewardAmount,
            Double changeRewardTotal, Double changeCouponAmount, Double changeFreezeAmount, Double legendTotal,
            Double consumeAmount, Double consumeTotal, Double voucherAmount, Double voucherTotal,
            Double deductionCountValue, Double deductionTotal, Double consumeRebateTotal, Double consumeRebateMoney,
            Double couponRebateTotal) throws Exception {
        Map<String, Object> param = buildParam(userId, changeAmount, changeRewardAmount, changeRewardTotal,
                changeCouponAmount, changeFreezeAmount, legendTotal, consumeAmount, consumeTotal, voucherAmount,
                voucherTotal, deductionCountValue, deductionTotal, consumeRebateTotal, consumeRebateMoney, couponRebateTotal);
       return  super.update("updateUserAccount", param);
    }
    
    private Map<String, Object> buildParam(Long userId, Double changeAmount, Double changeRewardAmount,
            Double changeRewardTotal, Double changeCouponAmount, Double changeFreezeAmount, Double legendTotal,
            Double consumeAmount, Double consumeTotal, Double voucherAmount, Double voucherTotal,
            Double deductionCountValue, Double deductionTotal, Double consumeRebateTotal, Double consumeRebateMoney,
            Double couponRebateTotal) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("changeAmount", NumberUtil.formatDouble(changeAmount, 4));
        param.put("changeRewardAmount", NumberUtil.formatDouble(changeRewardAmount, 4));
        param.put("changeRewardTotal",  NumberUtil.formatDouble(changeRewardTotal,4));
        param.put("changeCouponAmount",  NumberUtil.formatDouble(changeCouponAmount,4));
        param.put("changeFreezeAmount", changeFreezeAmount);
        
        param.put("legendTotal",  NumberUtil.formatDouble(legendTotal,4));
        param.put("consumeAmount",  NumberUtil.formatDouble(consumeAmount,4));
        param.put("consumeTotal",  NumberUtil.formatDouble(consumeTotal,4));
        param.put("voucherAmount",  NumberUtil.formatDouble(voucherAmount,4));
        param.put("voucherTotal",  NumberUtil.formatDouble(voucherTotal,4));
        param.put("deductionCountValue",  NumberUtil.formatDouble(deductionCountValue,4));
        param.put("deductionTotal",  NumberUtil.formatDouble(deductionTotal,4));
        param.put("consumeRebateTotal",  NumberUtil.formatDouble(consumeRebateTotal,4));
        param.put("consumeRebateMoney",  NumberUtil.formatDouble(consumeRebateMoney,4));
        param.put("couponRebateTotal",  NumberUtil.formatDouble(couponRebateTotal,4));
        return param;
    }

}
