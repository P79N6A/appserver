package com.idcq.appserver.utils.settle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.pay.IOrderSettleHandleDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.UserDto;

/**
 * 针对用户账户流水作订单结算	
 * @ClassName: OrderSettleForUserBill 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月22日 下午2:01:22 
 * 
 */
@Service
public class OrderSettleForUserBill {
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private  IOrderSettleHandleDao orderSettleHandleDao;
	
	/**
	 * 用户
	 */
	@Autowired
	private IUserDao userDao;
	
	/**
	 * 用户账户
	 */
	@Autowired
	private IUserAccountDao userAccountDao;
	/**
	 * 开始处理
	 * @Title: startHandle 
	 * @param @param orderId
	 * @param @param shopId
	 * @param @param userId
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void startHandle(Long orderId,Long shopId,Long userId)throws Exception
	{
			OrderSettleUserAmountDto userAmountInfoDto= orderSettleHandleDao.queryUserSettleInfo(orderId);//查找相关的用户结算的金额
			if(userAmountInfoDto!=null)
			{
				if(userAmountInfoDto.getUserShareSettleFlag()!=null&&userAmountInfoDto.getUserShareSettleFlag().intValue()==1)//状态必须要为未结算的
				{
					
				}
			}
	}
	
	/**
	 * 处理会员返点
	 * @Title: handleMemberReturnReward 
	 * @param @param userId
	 * @param @param userAmountInfoDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	private void handleMemberReturnReward(Long userId,OrderSettleUserAmountDto userAmountInfoDto)throws Exception
	{
		UserDto userDto=userDao.getDBUserById(userId);
		if(userDto!=null) 
		{
			if(userDto.getStatus()!=null&&userDto.getStatus().intValue()==1)
			{
				userDao.addUserAmount(userId,userAmountInfoDto.getUserSharePrice());
			}
			else
			{
				logger.error("订单结算-用户返点时,用户"+userId+"状态不正确");
			}
		}
		else
		{
			logger.error("订单结算-用户返点时,用户"+userId+"不存在");
		}
	}
	
	/**
	 * 处理会员推荐奖励
	 * 此时代表会员的推荐类型为用户
	 * @Title: handleRecommandMemberReward 
	 * @param @param userId
	 * @param @param userAmountInfoDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	private void handleRecommandMemberReward(Long userId,OrderSettleUserAmountDto userAmountInfoDto)throws Exception
	{
		UserDto userDto=userDao.getDBUserById(userId);
		if(userDto!=null)
		{
			Integer userType=userDto.getUserType();
			if(userType!=null)
			{
				int userTypeValue=userType.intValue();
				if(userTypeValue==20)
				{
					//AgentDto agentDto=
				}
						
			}
		}
		else
		{
			logger.error("订单结算-推荐用户奖励时,用户"+userId+"不存在");
		}
	}
	
}
