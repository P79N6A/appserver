package com.idcq.appserver.service.member;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.dto.user.UserSearchHistory;

/**
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午10:06:16
 */
public interface IMemberServcie {
	
	/**
	 * 从数据库获取用户信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByMobileFromDB(String mobile) throws Exception;
	
	/**
	 * 根据微信用户openId从数据库获取用户信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByWeiXinNoFromDB(String openId) throws Exception;
	
	/**
	 * 根据微信用户openId从缓存获取用户信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByWeiXinNo(String openId) throws Exception;
	
	/**
	 * 根据手机号获取密码
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	String getPasswordByMobile(String mobile) throws Exception;

	/**
	 * 根据手机号获取会员信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByMobile(String mobile) throws Exception;
	
	/**
	 * 为会员创建账户
	 * @return
	 * @throws Exception
	 */
	void createAccountForUser(UserDto user) throws Exception;
	
	/**
	 * 新增会员信息
	 * 该方法会默认初始化会员返点等级为普通
	 * @return
	 * @throws Exception
	 */
	UserDto saveUser(String mobile, String password, String veriCode, String refecode, String refeUser, String refeType, Integer registerType) throws Exception;
	
	/**
	 * 微信注册用户
	 * @return
	 * @throws Exception
	 */
	UserDto saveUserFromWeiXin(String openId) throws Exception;
	
	/**
	 * 微信绑定注册用户
	 * @param openId
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto registerUserFromWeiXinBind(String openId,String mobile, Long referUserId) throws Exception;
	/**
	 * 修改会员登录密码
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	int updatePassword(HttpServletRequest request) throws Exception;
	
	/**
	 * 修改待激活状态
	 * 
	 * @param userId
	 * @param isMember
	 * @return
	 * @throws Exception
	 */
	int updateIsMember(long userId, int isMember) throws Exception;
	
	/**
	 * 修改会员基本资料
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updateBaseInfo(UserDto user) throws Exception;
	
	/**
	 * 绑定银行卡
	 * 
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	int bindBankCard(BankCardDto bankCard) throws Exception;
	
	/**
	 * 解绑银行卡
	 * 
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	int unBindBankCard(BankCardDto bankCard) throws Exception;
	
	/**
	 * 修改手机号码
	 * 
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	int updateMobile(Map<String, String> hashMap) throws Exception;
	
	/**
	 * 修改openId
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	int updateWeiXinNo(UserDto userDto) throws Exception;
	/**
	 * 会员登录验证
	 * 
	 * @param user
	 * @return UserDto
	 * @throws Exception
	 */
	UserDto login(UserDto user) throws Exception;
	
	/**
	 * 根据用户ID获取会员信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getUserByUserId(Long userId) throws Exception;
	 /**
	  * 1.1.4获取奖励类型接口
      * 
      * @param userId
      * @return
      * @throws Exception
      */
	List<Map<String, Object>> getMyRewardType(Long userId, String[] accountTypes)throws Exception;
	
	/**
	 * 从数据库获取用户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getDBUserById(Long userId) throws Exception;
	
	/**
	 * 验证用户存在性
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Long authenUserById(Long userId) throws Exception;
	
	/**
	 * 验证用户存在性
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	int queryUserExists(String mobile) throws Exception;
	
	/**
	 * 修改会员支付密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int updatePayPassword(UserDto user) throws Exception;
	/**
	 * 实名认证接口，修改身份证、姓名
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int authRealName(UserDto user) throws Exception;
	
	/**
	 * 查询传奇宝账户余额
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserAccountDto getAccountMoney(Long userId) throws Exception;
	
	/**
	 * 获取用户的银行卡
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	PageModel getUserBankCards(Long userId, Integer pNo, Integer pSize) throws Exception;
	
	/**
	 * 验证支付密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	int authPayPassword(UserDto user) throws Exception;
	/**
	 * 查询用户会员卡列表
	 * @param userId 用户id
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getUserMembershipCardList(Long userId,
			Integer pageNo, Integer pageSize) throws Exception;
	/**
	 * 查询用户会员卡列表列表总数
	 * @param userId 用户id
	 * @return
	 */
	public int getUserMembershipCardListCount(Long userId) throws  Exception;
	/**
	 * 查询用户会员卡详细信息
	 * @param accountId 会员卡id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUserMembershipCardInfo(Long accountId)throws Exception ;
	
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
	PageModel getUserBill(Long userId,String billType,String startTime,String endTime, Integer page, Integer pageSize,Integer billStatusFlag) throws Exception;

	/**
	 * 获取推荐码
	 * @param userId 推荐用户
	 * @param referMobile 被推荐人手机号码
	 * @return
	 */
	public String insertUserReferInfo(Long userId, String referMobile)throws Exception;

	/**
	 * 推荐码验证
	 * @param mobile
	 * @param referCode
	 * @return
	 */
	int verifyReferCode(String mobile, String referCode)throws Exception;
	
	/**
	 * 查询搜索记录
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getSearchHistory(Long userId, Integer pNo, Integer pSize)  throws Exception;
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
	
	Map<String, Object> getUserVoucherInfo(Long userId) throws Exception;
	/**
	 * 增加用用户对商品或商铺的评论
	 * @return
	 * @throws Exception
	 */
	Long makeComment(String commentType, String bizId,String userId,String serviceGrade,String envGrade,
			String commentContent,String logisticsGrade) throws Exception;
	
	/**
	 * 更新商品和商铺评论以及用户头像logo
	 * @param userId 用户id
	 * @param usageType 图片类型
	 * @param bizId 商品或商铺id
	 * @param mimeType 文件类型
	 * @param myfile 文件流
	 * @return
	 */
	Map<String, String> updateLogo(Long userId, String usageType, Long bizId, String mimeType, MultipartFile myfile) throws Exception;
	
	
	/**
	 * 保存推送注册id
	 * @param pushUser
	 * @return
	 */
	int savePushUser(PushUserTableDto pushUser) throws Exception;
	
	/**
	 * app上报注册id
	 * @param request
	 * @throws Exception
	 */
	void addRegId(HttpServletRequest request) throws Exception;
	
	/**
	 * 根据用户查询推送注册注册信息
	 * @param userId
	 * @param userType
	 * @return
	 */
	List<PushUserTableDto> getPushUserByUserId(Long userId, Integer userType) throws Exception;
	
	/**
	 * 根据regId查询推送注册注册信息
	 * @param regId
	 * @return
	 */
	PushUserTableDto getPushUserByRegId(String regId) throws Exception;
	
	/**
	 * 更新注册信息
	 * @param pushUser
	 * @return
	 */
	int updatePushUser(PushUserTableDto pushUser) throws Exception;
	
	/**
	 * 获取我的推荐用户列表
	 * @param userId
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	PageModel getMyRefUsers(Long userId, Integer pNo, Integer pSize) throws Exception;
	
	/**
	 * 查询推荐人信息
	 * @param mobile
	 * @param refecode
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getReferUserBy(String mobile, String refecode)throws Exception;
	
	/**
	 * 根据评论编号找到用户编号
	 * @param commentId
	 * @return
	 */
	Long findUserIdByCommentId(Long commentId, Integer commentType)throws Exception;
	
	/**
	 * 查询评论订单信息
	 * @param orderId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getOrderComments(String orderId, Integer pNo, Integer pSize, Long userId)  throws Exception;	
	/**
	 * 根据orderId获取订单列表总数
	 * @param commentId
	 * @return
	 */
	int getOrderCommentCountById(String commentId);
	
	
	/**
	 * 
	 * 根据用户id获取用户基本信息接口
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getBaseInfo(Long userId)throws Exception;
	
	/**
	 * 获取用户可用红包总额
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getRedPacketTotalMoney(Long userId) throws Exception;
	/**
	 * 获取评论总数
	 * @param commentType
	 * @param bizId
	 * @return
	 * @throws Exception
	 */
	public int getCommentTotalNumber(String commentType, String bizId) throws Exception;
	
	
	/**
	 * 插入用户搜索历史记录
	* @Title: insertUserSearchHistory 
	* @Description: TODO
	* @param @param userSearchHistory
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	public void insertUserSearchHistory(UserSearchHistory userSearchHistory)throws Exception;
	
	/**
	 * 根据用户编号和搜索内容查看以前是否存在过
	* @Title: getCountByUserIdAndSearchContent 
	* @Description: TODO
	* @param @param userSearchHistory
	* @param @return
	* @param @throws Exception
	* @return int    返回类型 
	* @throws
	 */
	int getCountByUserIdAndSearchContent(UserSearchHistory userSearchHistory)throws Exception;
	/**
	 * 刷新用户缓存
	 * @param userId
	 */
	void refreshUser(Long userId) throws Exception;
	
	/**
	 * 获取账单详情
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getUserBillDetail(Long billId) throws Exception;
	
	/**
	 * 获取用户支付密码是否设置标识
	 * @param userId
	 * @return 1：已设定  0：未设定
	 * @throws Exception
	 */
	Map<String, Object> getPayPwdStatus(Long userId, int type) throws Exception;
	
	/**
	 * 获取商铺账户
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopAccountDto getShopAccountById(Long shopId) throws Exception;
	
	/**
	 * 修改用户搜索历史记录
	 * @Title: updateUserSearchHistory 
	 * @param @param userSearchHistory
	 * @return void    返回类型 
	 * @throws
	 */
	void updateUserSearchHistory(UserSearchHistory userSearchHistory)throws Exception;
	/**
	 * 查询用户消费金
	 * 
	 * @Function: com.idcq.appserver.service.member.IMemberServcie.getUserXbill
	 * @Description:
	 *
	 * @param pMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月17日 下午2:50:42
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月17日    ChenYongxin      v1.0.0         create
	 */
	PageModel getUserXbill(Map<String, Object> pMap)throws Exception;
	
	/**
	 * 查询会员账单明细
	 * @param requestParamMap
	 * @return
	 * @throws Exception
	 */
	PageModel getBillDetail(Map<String, Object> requestParamMap)throws Exception;
	/**
	 * 获取非归属地电话-临时表
	 * 
	 * @Function: com.idcq.appserver.service.member.IMemberServcie.getNoCityMobile
	 * @Description:
	 *
	 * @param limit
	 * @param pNo
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月10日 上午9:55:20
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月10日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>>  getNoCityMobile(Integer limit,Integer pNo)throws Exception;
	/**
	 * 
	 * 更新临时表手机号码归属地
	 * @Function: com.idcq.appserver.service.member.IMemberServcie.updateCityMobile
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月10日 上午9:59:51
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月10日    ChenYongxin      v1.0.0         create
	 */
	int updateCityMobile(Map<String, Object> map) throws Exception;
	
	/**
	 * 设置商铺支付密码
	 * @param strToLong
	 * @param payPwd
	 */
	void setShopPayPwd(Long strToLong, String payPwd)  throws Exception;
	
	/**
	 * 修改商铺支付密码
	 * @param shopId
	 * @param payPassword
	 * @param newPayPwd
	 * @throws Exception
	 */
	void updateShopPayPwd(Long shopId, String payPassword, String newPayPwd) throws Exception;
	
	/**
	 * 验证商铺支付密码
	 * @param shopId
	 * @param payPassword
	 * @throws Exception
	 */
	void authShopPayPassword(String shopId, String payPassword) throws Exception;
	/**
	 * 查询会员账单统计
	 * @Function: com.idcq.appserver.service.member.IMemberServcie.getBillStat
	 * @Description:
	 *
	 * @param parpMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>>getBillStat(Map<String, Object> parpMap)throws Exception;
	
	/**
	 * 查询用户账单
	 * @param userId
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAccountingStat(Long userId, String startTimeStr,
			String endTimeStr)throws Exception;
	/**
	 * U41：查询用户奖励总额接口
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> rewardsum(Long userId) throws Exception;
	
	/**
	 * U42：获取用户奖励列表接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	PageModel allrewards(Map<String, Object> map) throws Exception;
	
	/**
	 * U43：我的推荐用户列表接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	PageModel getMyRefUserList(Map<String, Object> map) throws Exception;

	/**
	 * MS19：新增商铺接口
	 * 更新商品用户身份证号
	 * @param userId
	 * @param identityCardNo
	 */
	void updateUser(Long userId, String identityCardNo);
	
	  //注册成为一点传奇会员之后，判断该会员是否为该店内会员，如果不是，则添加
    void addShopMember(Long userId, String refeUser, String refeType, String mobile) throws Exception;
    /**
     * 获取用户提现校准信息接口
     */
	public Map<String, Object> getUserAuditInfo(Long withdrawId)
			throws Exception ;
}
