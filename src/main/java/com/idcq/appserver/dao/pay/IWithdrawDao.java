package com.idcq.appserver.dao.pay;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.pay.WithdrawDto;

public interface IWithdrawDao {
	/**
	 * 用户发起提现接口
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception
	 */
	Integer withdraw(WithdrawDto withdrawDto) throws Exception;
	/**
	 * 查询用户提现记录
	 * @param userId 用户id
	 * @param startTime 查询开始时间(查询起始时间，为空时表示查询一周内)
	 * @param endTime  查询结束时间(查询截止时间，为空时表示当天)
	 * @param pageNo 当前页码
	 * @param pageSize 页大小
	 * @return
	 */
	List<Map<String, Object>> getWithdrawList(Map<String,Object> map,int pageNo,int pageSize) throws Exception;
	List<Map<String, Object>> getShopWithdrawList(Map<String,Object> map,int pageNo,int pageSize) throws Exception;
	
	/**
	 *  查询用户提现记录总数
	 * @param userId 用户id
	 * @param startTime 查询开始时间(查询起始时间，为空时表示查询一周内)
	 * @param endTime  查询结束时间(查询截止时间，为空时表示当天)
	 * @return 
	 */
	int getWithdrawListCount(Map<String,Object> map) throws Exception;
	int getShopWithdrawListCount(Map<String,Object> map) throws Exception;
	
	Map<String, Object> getWithdrawInfoById(Long withdrawId) throws Exception;
	
		/**
	 * 服务端查询提现记录
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getServiceWithdrawList(Integer withdrawStatus,Long userId,String mobile,int pageNo,int pageSize) throws Exception;
	/**
	 * 服务端查询提现总数
	 * @param withdrawStatus
	 * @param userId
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	Integer getServiceWithdrawCount(Integer withdrawStatus,Long userId,String mobile) throws Exception;
	
	/**
	 * 根据ID查询提现信息
	 * @param withdrawId
	 * @return
	 * @throws Exception
	 */
	WithdrawDto getWithdrawById(Long withdrawId) throws Exception;
	/**
	 * 更新提现信息
	 * @param withdrawDto
	 * @throws Exception
	 */
	void updateWithdraw(WithdrawDto withdrawDto) throws Exception;
	
	/**
	 * 查询提现列表
	 * @Title: getWithDrawListByIdList 
	 * @param @param withDrawList
	 * @param @return
	 * @param @throws Exception
	 * @return List<WithdrawDto>    返回类型 
	 * @throws
	 */
	List<WithdrawDto>getWithDrawListByIdList(List<String>withDrawList)throws Exception;
	/**
	 * 获取提现基准信息
	 * 
	 * @throws Exception
	 */
	Map<String,Object> getStandardWithdrawMoney(Long userId) throws Exception;
	/**
	 * 获取时间段提现总额
	 * 
	 * @throws Exception
	 */
	Map<String,Object> getUserWithdrawTotalMoney(Long userId,Integer withdrawStatus, Date startTime, Date endTime) throws Exception;
	


}
