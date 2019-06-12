package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopAccountDto;

public interface IShopAccountDao {
	
	/**
	 * 更新商铺账户余额
	 * @param userId
	 * @param amount
	 * @throws Exception
	 */
	void updateShopAccountMoney(Long shopId, double amount) throws Exception;
	public List<ShopAccountDto> getAllAccount(Integer limit, Integer pageSize) throws Exception;
	public ShopAccountDto getShopAccount(Long shopId) throws Exception;
	/**
	 * 查询商铺账户基本信息
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getShopAccountMoney(Map<String, Object> parms) throws Exception;
	/**
	 * 查询商铺奖励接口
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<Map<String, Object>>    返回类型 
	 * @throws
	 */
	List<Map<String, Object>> getShopAwardList(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺奖励总数
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return int   返回类型 
	 * @throws
	 */
	Integer getShopAwardCount(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺奖励接口
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<Map<String, Object>>    返回类型 
	 * @throws
	 */
	List<Map<String, Object>> getUserAwardList(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺奖励总数
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return int   返回类型 
	 * @throws
	 */
	Integer getUserAwardCount(Map<String, Object> map)throws Exception;
	/**
	 * 查询所有奖励记录
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return List<Map<String, Object>>    返回类型 
	 * @throws
	 */
	List<Map<String, Object>> getAllAwardList(Map<String, Object> map)throws Exception;
	/**
	 *  查询所有奖励记录总数
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return int   返回类型 
	 * @throws
	 */
	Integer getAllAwardCount(Map<String, Object> map)throws Exception;
	/**
	 *  查询所有奖励金额
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return int   返回类型 
	 * @throws
	 */
	Map<String, Object> getShopAwardTotal(Map<String, Object> map)throws Exception;
	/**
	 *  查询用户所有奖励金额
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return int   返回类型 
	 * @throws
	 */
	Map<String, Object> getUserAwardTotal(Map<String, Object> map)throws Exception;
	/**
	 * 更新商铺冻结金额
	 * @param shopId
	 * @param freezeAmount
	 * @return
	 * @throws Exception
	 */
	public Integer updateFreezeMoney(Long shopId, double freezeAmount)throws Exception;	
	/**
	 * 查询是否赠送过保证金
	 * @Title: queryIsGiveDepositFlag 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception
	 * @return Integer    返回类型 
	 * @throws
	 */
	Integer queryIsGiveDepositFlag(Long shopId)throws Exception;
	
	/**
	 * 给商铺赠送保证金
	 * @Title: giveShopDeposit 
	 * @param @param shopId
	 * @param @param amount
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void giveShopDeposit(Long shopId, Double amount)throws Exception;
	
	/**
     * 更新商铺账户表中各账户余额信息
     * 不需要更新的账户字段传null值
     * @param shopId  商铺ID
     * @param changeAmount 账户总额变动的值，新增为正，减少为负
     * @param changeOnlineAmount 线上营业余额变动值
     * @param changeRewardAmount 平台奖励余额变动值
     * @param changeRewardTotal 平台总奖励变动值
     * @param changeDepositAmount 保证金余额变动值
     * @param changeFreezeAmount 冻结资金余额变动值
     * @param arrearsFlag 是否欠费标识(跟店铺状态无关)：1=欠费，0=未欠费
     * @throws Exception [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     * @author  shengzhipeng
     */
    public void updateShopAccount(Long shopId, Double changeAmount, Double changeOnlineAmount,
            Double changeRewardAmount, Double changeRewardTotal, Double changeDepositAmount, Double changeFreezeAmount,
            Integer arrearsFlag,Double marketAmount, Double marketTotal, Double legendTotal, Double marketRebateTotal, Double marketRebateMoney) throws Exception;
    
    public void updateShopAccount(Long shopId, Double changeAmount, Double changeOnlineAmount,
            Double changeRewardAmount, Double changeRewardTotal, Double changeDepositAmount, Double changeFreezeAmount,
            Integer arrearsFlag,Double marketAmount, Double marketTotal, Double legendTotal) throws Exception;
}
