package com.idcq.appserver.dao.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.user.UserBillDto;


public interface IUserBillDao {
	
	/**
	 * 查询用户的账单
	 * @param userId
	 * @param billType
	 * @param startTime
	 * @param endTime
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<UserBillDto> getUserBill(Map param) throws Exception;
	
	public int getUserBillTotal(Map param)  throws Exception;
	
	public int insertUserBill(UserBillDto userBillDto)  throws Exception;
	/**
	 * 获取单个账单信息
	 * @return
	 * @throws Exception
	 */
	UserBillDto getUserBillByUserId(UserBillDto userBillDto) throws Exception;
	
	Map<String, Object> getUserBillById(Long billId) throws Exception;
	
	public List<UserBillDto> getUserBillByDto(UserBillDto userBillDto) throws Exception;

	/**
	 * 根据流水号更新账单状态
	 * @param userBillDto
	 */
	void updateStatusByTransactionId(UserChargeDto userBillDto);
	
	void updateUserBillStatusBy(Long billId, Integer billStatusFlag, Integer billStatus);
	 
	/**
	 * 新增临时账单
	 * @param userBillDto
	 * @throws Exception [参数说明]
	 * @return int 成功返回1，失败返回0
	 * @author  shengzhipeng
	 */
	int insertUserBillMiddle(UserBillDto userBillDto)  throws Exception;
	
	List<UserBillDto> getUserBillMiddleByOrderId(String orderId) throws Exception;
	
	void deleteUserBillMiddle(String orderId, List<Long> userBillIds);

	/**
	 * 查询用户账单金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Double getUserAccountingStat(Map<String, Object> params) throws Exception;
	
	/**
	 * 查询用户账单，获取代理奖励列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAgentRewardList(Map<String, Object> params) throws Exception;

	Integer allrewardsCount(Map<String, Object> map) throws Exception;

	/**
	 * U42：获取用户奖励列表接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> allrewards(Map<String, Object> map) throws Exception;

	List<Integer> getMyRewardType(Long userId, String[] accountTypes);
	/**
	 * 获取奖励收入总额
	 */
	Map<String, Object> getTotalMoneyByWithdraw(Long userId,Integer billDirection,Integer accountType,Date startTime,Date endTime) throws Exception;

}
