package com.idcq.appserver.dao.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.ShopUserVantages;
import com.idcq.appserver.dto.user.ShopUserVantagesLog;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.dto.user.UserSearchHistory;


public interface IUserDao {
	
	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	UserDto saveUser(UserDto user) throws Exception;
	
	/** 
	 * 修改用户密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updatePassword(UserDto user) throws Exception;
	
	/**
	 * 修改手机号码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updateMobile(UserDto user) throws Exception;
	
	/**
	 * 修改openId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updateWeiXinNo(UserDto user) throws Exception;
	
	/**
	 * 修改会员
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updateUser(UserDto user) throws Exception;
	
	/**
	 * 根据手机获取用户
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByMobile(String mobile) throws Exception;
	
	/**
	 * 根据手机号从数据库中获取用户信息
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByMobileFromDB(String mobile) throws Exception;
	
	/**
	 * 根据微信用户openId从缓存获取用户信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByWeiXinNo(String openId) throws Exception;
	/**
	 * 根据微信用户openId从数据库获取用户信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByWeiXinNoFromDB(String openId) throws Exception;
	
	/**
	 * 获取指定ID的用户先缓存取，缓存没有从数据库中取
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getUserById(Long userId) throws Exception;
	
	/**
	 * 从数据库中获取指定ID的用户
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getDBUserById(Long userId) throws Exception;
	 /**
     * 查询会员账单统计
     * @Description:
     *
     * @param parpMap
     * @return
     * @throws Exception
     */
    List<Map<String, Object>>getBillStat(Map<String, Object> parpMap)throws Exception;
	
	/**
	 * 根据手机获取密码
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	String getPasswordByMobile(String mobile) throws Exception;
	
	/**
	 * 修改会员支付密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updatePayPassword(UserDto user) throws Exception;
	/**
	 * 实名认证接口，修改真实姓名、身份证号码
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int authRealName(UserDto user) throws Exception;
	/**
	 * 查询用户会员卡列表
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	 List<Map<String, Object>>  getUserMembershipCardList(Long userId,int pageNo,int pageSize) throws Exception;
	 /**
	  * 查询用户会员卡列表总记录数
	  * @param userId
	  * @return
	  */
	 int getUserMembershipCardListCount(Long userId);
	 /**
	  * 查询会员卡详细详细
	  * @param accountId
	  * @return
	  * @throws Exception
	  */
	 Map<String, Object> getUserMembershipCardInfo (Long accountId) throws Exception;
	
	/**
	 * 获取会员的支付密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	String getPayPasswordByUser(UserDto user) throws Exception;
	
	/**
	 * 验证用户存在性
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Long authenUserById(Long userId) throws Exception;
	/**
	 * 增加用户搜索记录
	 * @return
	 */
	int addUserSearchHistory(UserSearchHistory userSearchHistory)  throws Exception;
	/**
	 * 查询搜索记录
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getSearchHistory(Long userId,int pNo,int pSize)  throws Exception;
	/**
	 * 查询搜索记录总数
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	int getSearchHistoryCount(Long userId)  throws Exception;
	/**
	 * 清空用户保存的历史记录
	 * @param userId
	 * @return
	 */
	int deleteUserSearchHistory(Long userId,String searchContent) throws Exception;

	/**
	 * 保存用户推荐信息
	 * @param userRefer
	 * @return
	 * @throws Exception
	 */
	public int isnertUserReferInfo(Map userRefer)throws Exception;

	/**
	 * 查询用户推荐码信息
	 * @param param
	 * @return
	 */
	public int queryUserReferInfo(Map param)throws Exception;
	
	/**
	 * 根据手机号码验证会员存在性
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	int queryUserExistsByMobile(String mobile)throws Exception;
	
	/**
	 * 更新用户头像
	 * @param map
	 * @throws Exception
	 */
	void updateUserLogo(Map map) throws Exception;
	
	/**
	 * 获取我的推荐用户列表
	 * @param userId
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<Map> getMyRefUsers(Long userId ,int pNo, int pSize) throws Exception;
	
	/**
	 * 推荐会员奖励求和
	 * @param mobile
	 * @param billStatus
	 * @param billType
	 * @return
	 * @throws Exception
	 */
	Double sumAmount(String mobile, Integer billStatus, String billType, Long userId) throws Exception;
   
	int getMyRefUsersCount(Long userId) throws Exception;
	
	/**
	 * 查询推荐人信息
	 * @param mobile
	 * @param refecode
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getReferUserBy(String mobile, String refecode)throws Exception;

	
	void insertUserSearchHistory(UserSearchHistory userSearchHistory)throws Exception;

	/** 
	 * 根据用户id和搜索内容获取
	 * @Title: getCountByUserIdAndSearchContent 
	 * @Description: TODO
	 * @param @param userSearchHistory
	 * @param @return
	 * @return int    返回类型 
	 * @throws 
	 */
	int getCountByUserIdAndSearchContent(UserSearchHistory userSearchHistory);
	
	/**
	 * 查询用户状态
	 * @param userId
	 * @return
	 */
	public Map queryUserStatus(Long userId);

	/**
	 * 根据手机号码及用户编号查询推荐码，如果存在，则将推荐码返回
	 * @param userRefer
	 * @return
	 */
	Map queryUserReferCode(Map userRefer);

	/**
	 * 修改当前记录的推荐码为新生成的推荐码
	 * @param reMap
	 * @return
	 */
	int updateUserReferCode(Map reMap);
	
	/**
	 * 查询当前手机号码是否是平台会员
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	int queryNormalUserByMobile(String mobile)throws Exception;
	
	int updateUserStatus(UserDto user)throws Exception;
	
	int updateUserMac(UserDto user) throws Exception;
	
	int updateUserLastLoginTime(UserDto user) throws Exception;
	
	/**
	 * 增加用户的账户金额
	 * @Title: addUserAmount 
	 * @param @param userId
	 * @param @param price
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void addUserAmount(Long userId,Double price)throws Exception;
	
	/**
	 * 修改用户的历史搜索记录
	 * @Title: updateUserSearchHistory 
	 * @param @param userSearchHistory
	 * @return void    返回类型 
	 * @throws
	 */
	void updateUserSearchHistory(UserSearchHistory userSearchHistory);
	/**
	 * 查询用户用户消费金接口
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.getUserXbill
	 * @Description:
	 *
	 * @param pMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月17日 下午2:48:32
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月17日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getUserXbill(Map<String, Object> pMap)throws Exception;
	
	/**
	 * 查询会员账单明细
	 * @param requestParamMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBillDetail(Map<String, Object> requestParamMap)throws Exception;
	
	/**
     * 查询会员账单明细
     * @param requestParamMap
     * @return
     * @throws Exception
     */
    int getBillDetailCount(Map<String, Object> requestParamMap)throws Exception;
    
	
	/**
	 * 查询用户用户消费金count
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.getUserXbill
	 * @Description:
	 *
	 * @param pMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月17日 下午2:48:32
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月17日    ChenYongxin      v1.0.0         create
	 */
	Integer getUserXbillCount(Map<String, Object> pMap)throws Exception;
	/**
	 * 插入商铺会员积分
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.insertShopUserVantages
	 * @Description:
	 *
	 * @param shopUserVantages
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月28日 下午3:24:53
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月28日    ChenYongxin      v1.0.0         create
	 */
	int insertShopUserVantages(ShopUserVantages shopUserVantages) throws Exception;
	/**
	 * 插入日志积分记录
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.insertShopUserVantageslog
	 * @Description:
	 *
	 * @param shopUserVantages
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月28日 下午3:50:33
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月28日    ChenYongxin      v1.0.0         create
	 */
	int insertShopUserVantageslog(ShopUserVantagesLog shopUserVantagesLog) throws Exception;
	/**
	 * 查询商铺会员积分
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.getUserVantagesByUserIdAndShopId
	 * @Description:
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年10月13日 上午9:47:35
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年10月13日    ChenYongxin      v1.0.0         create
	 */
	BigDecimal getUserVantagesByUserIdAndShopId(Long userId,Long shopId) throws Exception;
	
	/**
	 * 更新会员积分
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.updateShopUserVantages
	 * @Description:
	 *
	 * @param shopUserVantages
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年10月14日 上午9:27:00
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年10月14日    ChenYongxin      v1.0.0         create
	 */
	int updateShopUserVantages(ShopUserVantages shopUserVantages) throws Exception;
    /**
     * 查询归属地为null的用户id
     * 
     * @Function: com.idcq.appserver.dao.user.IUserDao.getIdListByProvinceIdIsNull
     * @Description:
     *
     * @param limit
     * @param pNo
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年11月9日 下午2:53:35
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年11月9日    ChenYongxin      v1.0.0         create
     */
	List<Map<String, Object>>  getUserInfoByProvinceIdIsNull(Integer limit,Integer pNo)throws Exception;
	/**
	 * 显示数据临时处理--更新用户归属地
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.updateUserPlace
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月9日 下午4:14:06
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月9日    ChenYongxin      v1.0.0         create
	 */
	int updateUserPlace(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>>  getNoCityMobile(Integer limit,Integer pNo)throws Exception;
	/**
	 * 显示数据临时处理--更新用户归属地
	 * 
	 * @Function: com.idcq.appserver.dao.user.IUserDao.updateUserPlace
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月9日 下午4:14:06
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月9日    ChenYongxin      v1.0.0         create
	 */
	int updateCityMobile(Map<String, Object> map) throws Exception;
	
	void updateIdentity(Map<String, Object> map) throws Exception;

	/**
	 * 更新用户为店铺管理者
	 * @param userId
	 */
	void updateUserIsManager(Long userId);

	Integer getMyRefUserListCount(Map<String, Object> map) throws Exception;

	/**
	 * U43：我的推荐用户列表接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMyRefUserList(Map<String, Object> map) throws Exception;

	void updateUser(Long userId, String identityCardNo);
	
	/**
	 * 根据用户id列表查询
	 * @Title: queryUserByIdList 
	 * @param @param userIdList
	 * @param @return
	 * @param @throws Exception
	 * @return List<UserDto>    返回类型 
	 * @throws
	 */
	List<UserDto>queryUserByIdList(List<Long>userIdList)throws Exception;
	
	/**
	 * 修改待激活状态
	 * 
	 * @param userId
	 * @param isMember
	 * @return
	 * @throws Exception
	 */
	int updateIsMember(long userId, int isMember) throws Exception;
	
	String getMobileByUserId(Long userId) throws Exception;

	UserDto getUserByMap(Map<String, Object> requestMap);

	UserDto getShopPrincipalInfoByMap(Map<String, Object> reaultMap);
	
}
