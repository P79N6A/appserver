package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.UserAccountDto;


public interface IUserAccountDao {
	
	/**
	 * 查询传奇宝账户余额
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserAccountDto getAccountMoney(Long userId) throws Exception;
	/**
	 * 查询所有用户账户
	 */
	public List<UserAccountDto> getAllAccount(Integer limit,Integer pageSize, Double money) throws Exception;
	
	/**
	 * 获取领取红包的用户的详细信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRedPackUserDetail(Long userId) throws Exception;
	
	/**
	 * 更新传奇宝账户余额
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	void updateAccountMoney(Long userId,double amount) throws Exception;
	
	/**
	 * 更新传奇宝账户消费金和平台奖励余额
	 * @param userId
	 * @param amount
	 * @param coupon
	 * @param reward
	 * @throws Exception
	 */
	void updateCouponAndRewardAmount(long userId,Double amount,Double coupon,Double reward,Double voucherAmount) throws Exception;
	/**
	 * 保存用户账户
	 * @param userAccountDto
	 * @throws Exception
	 */
	void saveAccount(UserAccountDto userAccountDto) throws Exception;
	
	/**
	 * 退钱到传奇宝
	 * @param userId
	 * @param amount
	 * @throws Exception
	 */
	void backAccountMoney(Long userId, Double amount) throws Exception;
	/**
	 * 更新冻结资金
	 * @param userId
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	Integer updateFreezeMoney(Long userId,double amount) throws Exception;
	
	/**
	 * 更新用户账户表中各账户余额信息
     * 不需要更新的账户字段传null值
	 * @param userId
	 * @param changeAmount 账户总额变动的值，新增为正，减少为负
	 * @param changeRewardAmount 平台奖励余额变动值
	 * @param changeRewardTotal 平台总奖励变动值
	 * @param changeCouponAmount 消费金余额
	 * @param changeFreezeAmount 冻结资金余额变动值
	 * @return
	 * @throws Exception
	 */
    public Integer updateUserAccount(Long userId, Double changeAmount, Double changeRewardAmount,
            Double changeRewardTotal, Double changeCouponAmount, Double changeFreezeAmount,
            Double legendTotal, Double consumeAmount, Double consumeTotal,Double voucherAmount,
            Double voucherTotal, Double deductionCountValue, Double deductionTotal,Double consumeRebateTotal,
            Double consumeRebateMoney) throws Exception;
    
    /**
     * 更新用户账户表中各账户余额信息
     * 不需要更新的账户字段传null值
     * @param userId
     * @param changeAmount 账户总额变动的值，新增为正，减少为负
     * @param changeRewardAmount 平台奖励余额变动值
     * @param changeRewardTotal 平台总奖励变动值
     * @param changeCouponAmount 消费金余额
     * @param changeFreezeAmount 冻结资金余额变动值
     * @param couponRebateTotal 消费金奖励总额
     * @return
     * @throws Exception
     */
    public Integer updateUserAccount(Long userId, Double changeAmount, Double changeRewardAmount,
            Double changeRewardTotal, Double changeCouponAmount, Double changeFreezeAmount,
            Double legendTotal, Double consumeAmount, Double consumeTotal,Double voucherAmount,
            Double voucherTotal, Double deductionCountValue, Double deductionTotal,Double consumeRebateTotal,
            Double consumeRebateMoney, Double couponRebateTotal) throws Exception;

    /**
     * 更新账户余额信息
     * @param accountId 主键编号
     * @param amount 总余额
     * @param rewardAmount 奖励余额
     * @param couponAmount 消费金余额
     * @param rewardTotal 累计奖励
     * @param freezeAmount 冻结金额
     * @return
     * @throws Exception
     */
	public Integer updateUserAccountByKey(Long accountId, Double amount,
			Double rewardAmount, Double couponAmount, Double rewardTotal,
			Double freezeAmount,Double voucherAmount) throws Exception;

	/**
	 * P45：查询用户奖励总额接口
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> rewardsum(Long userId) throws Exception;
	
}
