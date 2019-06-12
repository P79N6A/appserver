package com.idcq.appserver.dao.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.ShopUserVantages;
import com.idcq.appserver.dto.user.ShopUserVantagesLog;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.dto.user.UserSearchHistory;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class UserDaoImpl extends BaseDao<UserDto> implements IUserDao{
	
	public String getPasswordByMobile(String mobile) throws Exception {
		return (String) super.selectOne(generateStatement("getPasswordByMobile"),mobile);
	}
	
	public String getMobileByUserId(Long userId) throws Exception {
		return (String) super.selectOne(generateStatement("getMobileByUserId"),userId);
	}
	public UserDto getUserById(Long userId) throws Exception {
		return (UserDto)HandleCacheUtil.getEntityCacheByClass(UserDto.class, userId, CommonConst.CACHE_USER_OUT_TIME);
	}
	
	public UserDto getDBUserById(Long userId) throws Exception {
		return (UserDto) super.selectOne(generateStatement("getUserById"), userId);
	}
	
	
	public UserDto getUserByMobile(String mobile) throws Exception {
		UserDto user = null;
		if(StringUtils.isBlank(mobile)) {
			return user;
		}
		String mobileKey = CommonConst.KEY_MOBILE + mobile;
		String userId = DataCacheApi.get(mobileKey);
		if (StringUtils.isNotBlank(userId)){
			//查询缓存
			Object obj = (UserDto) DataCacheApi.getObject(CommonConst.KEY_USER + userId);
			if (obj != null) {
				user = (UserDto) obj;
			}
		}
		if (null == user) {
			logger.info("缓存没有查询到数据改查数据库getUserByMobile():" + mobile);
			//查询数据库
			user = (UserDto)super.selectOne(generateStatement("getUserByMobile"), mobile);
			if (null != user) {
				String userIdStr = String.valueOf(user.getUserId());
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userIdStr, user, CommonConst.CACHE_USER_OUT_TIME);
				DataCacheApi.setex(mobileKey, userIdStr, CommonConst.CACHE_USER_OUT_TIME);
			}
			
		}
		return user;
	}

	public UserDto getUserByWeiXinNo(String openId) throws Exception {
		UserDto userDto = null;
		if (StringUtils.isBlank(openId))
			return userDto;
		String weixinNoKey = CommonConst.KEY_WEIXIN_NO + openId;
		String userId = DataCacheApi.get(weixinNoKey);
		if (StringUtils.isNotBlank(userId))
			userDto = (UserDto)DataCacheApi.getObject(CommonConst.KEY_USER + userId);
		
		if (userDto == null) {
			logger.info("缓存没有查询到数据改查数据库getUserByOpenId():" + openId);
			userDto = getUserByWeiXinNoFromDB(openId);
			if (userDto != null) {
				DataCacheApi.setObjectEx(CommonConst.KEY_USER + userDto.getUserId(), userDto, CommonConst.CACHE_USER_OUT_TIME);
				DataCacheApi.setex(weixinNoKey, userDto.getUserId().toString(), CommonConst.CACHE_USER_OUT_TIME);
			}
		}
		return userDto;
	}
	public UserDto getUserByMobileFromDB(String mobile) throws Exception{
		return (UserDto)super.selectOne(generateStatement("getUserByMobile"), mobile);
	}
	
	public UserDto getUserByWeiXinNoFromDB(String openId) throws Exception {
		return (UserDto)super.selectOne(generateStatement("getUserByOpenId"), openId);
	}
	public UserDto saveUser(UserDto user) throws Exception {
		 super.insert(generateStatement("saveUser"),user);
		 return user;
	}

	public int updatePassword(UserDto user) throws Exception {
		 return super.update(generateStatement("updatePassword"),user);
	}
	
	public int updateMobile(UserDto user) throws Exception {
		return super.update(generateStatement("updateMobile"),user);
	}
	
	public int updateWeiXinNo(UserDto user) throws Exception {
		return super.update(generateStatement("updateWeiXinNo"),user);
	}
	public int updateUser(UserDto user) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("user", user);
		return super.update(generateStatement("updateUser"),map);
	}

	public int updatePayPassword(UserDto user) throws Exception {
		 return super.update(generateStatement("updatePayPassword"),user);
	}

	public int authRealName(UserDto user) throws Exception {
		 return super.update(generateStatement("authRealName"),user);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUserMembershipCardList(Long userId, int pageNo,
			int pageSize) throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m",pageSize);
		return (List)super.findList(generateStatement("getUserMembershipCardList"), map);
	}
	
	public String getPayPasswordByUser(UserDto user) throws Exception {
		return (String)super.selectOne(generateStatement("getPayPasswordByUser"),user.getMobile());
	}

	public int getUserMembershipCardListCount(Long userId) {
		return (Integer) super.selectOne(generateStatement("getUserMembershipCardListCount"), userId);
	}

	public Map<String, Object> getUserMembershipCardInfo(Long accountId)
			throws Exception {
		return (Map)super.selectOne(generateStatement("getUserMembershipCardInfo"),accountId);
	}

	public Long authenUserById(Long userId) throws Exception {
		Long uId = (Long)super.selectOne(generateStatement("authenUserById"), userId);
		return uId == null ? 0L : uId;
	}
	public int addUserSearchHistory(UserSearchHistory userSearchHistory)
			throws Exception {
		// TODO 后续需要 可以补增加
		return 0;
	}

	public List<Map<String, Object>> getSearchHistory(Long userId, int pNo,
			int pSize) throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("n", (pNo-1)*pSize);
		map.put("m",pSize);
		return (List)super.findList(generateStatement("getSearchHistory"), map);
	}

	public int getSearchHistoryCount(Long userId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getSearchHistoryCount"), userId);
	}

	public int deleteUserSearchHistory(Long userId,String searchContent) throws Exception {
		UserSearchHistory userSearchHistory = new UserSearchHistory();
		userSearchHistory.setUserId(userId);
		userSearchHistory.setSearchContent(searchContent);
		return (Integer) super.delete(generateStatement("deleteUserSearchHistory"), userSearchHistory);
	}

	public int isnertUserReferInfo(Map param) throws Exception{
		return (Integer)super.insert(generateStatement("isnertUserReferInfo"), param);
	}

	public int queryUserReferInfo(Map param) throws Exception {
		return (Integer)super.selectOne(generateStatement("queryUserReferInfo"), param);
	}

	public void updateUserLogo(Map map) throws Exception {
		super.update(generateStatement("updateUserLogo"), map);
	}

	public int queryUserExistsByMobile(String mobile) throws Exception {
		return (Integer)super.selectOne(generateStatement("queryUserExistsByMobile"),mobile);
	}

	@Override
	public List<Map> getMyRefUsers(Long userId, int pNo, int pSize)
			throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("n", (pNo-1)*pSize);
		map.put("m",pSize);
		return (List)super.findList(generateStatement("getMyRefUsers"), map);
	}
	
	public int getMyRefUsersCount(Long userId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getMyRefUsersCount"), userId);
	}

	public List<UserDto> getReferUserBy(String mobile, String refecode) throws Exception {
		Map<String, String> map  = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("refecode", refecode);
		return (List<UserDto>) super.findList(generateStatement("getReferUserBy"), map);
	}

	public Double sumAmount(String mobile, Integer billStatus, String billType, Long userId)
			throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("mobile", mobile);
		map.put("billStatus", billStatus);
		map.put("billType", billType);
		map.put("userId", userId);
		return (Double) super.selectOne(generateStatement("sumAmount"), map);
	}

	/** 
	 * 添加用户搜索记录
	 * @Title: insertUserSearchHistory 
	 * @param @param userSearchHistory
	 * @param @throws Exception  
	 * @throws  
	 */
	@Override
	public void insertUserSearchHistory(UserSearchHistory userSearchHistory)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("searchContent", userSearchHistory.getSearchContent());
		params.put("userId", userSearchHistory.getUserId());
		params.put("createTime",new Date());
		super.insert(generateStatement("insertUserSearchHistory"), params);
	}

	/** 
	 * @Title: getCountByUserIdAndSearchContent 
	 * @param @param userSearchHistory
	 * @param @return  
	 * @throws 
	 */
	@Override
	public int getCountByUserIdAndSearchContent(
			UserSearchHistory userSearchHistory) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("userId", userSearchHistory.getUserId());
		params.put("searchContent", userSearchHistory.getSearchContent());
		return (int)super.selectOne(generateStatement("getCountByUserIdAndSearchContent"), userSearchHistory);
	}

	public Map queryUserStatus(Long userId) {
		return (Map)super.selectOne(generateStatement("queryUserStatus"), userId);
	}

	public Map queryUserReferCode(Map userRefer) {
		return (Map)super.selectOne(generateStatement("queryUserReferCode"), userRefer);
	}

	public int updateUserReferCode(Map param) {
		return super.update(generateStatement("updateUserReferCode"), param);
	}

	public int queryNormalUserByMobile(String mobile) throws Exception {
		return (int)super.selectOne(generateStatement("queryNormalUserByMobile"), mobile);
	}

	public int updateUserStatus(UserDto user) throws Exception {
		return super.update(generateStatement("updateUserStatus"), user);
	}

	public int updateUserMac(UserDto user) throws Exception {
		return super.update(generateStatement("updateUserMac"), user);
	}

	public int updateUserLastLoginTime(UserDto user) throws Exception {
		return super.update(generateStatement("updateUserLastLoginTime"), user);
	}

	@Override
	public void addUserAmount(Long userId, Double price) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("price", price);
		update(generateStatement("addUserAmount"),params);
	}

	@Override
	public void updateUserSearchHistory(UserSearchHistory userSearchHistory) {
		super.update(generateStatement("updateUserSearchHistory"), userSearchHistory);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#getUserXbill(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getUserXbill(Map<String, Object> pMap)
			throws Exception {
		return (List)super.findList(generateStatement("getUserXbill"), pMap);
	}

	@Override
	public List<Map<String, Object>> getBillDetail(Map<String, Object> requestParamMap)
			throws Exception {
		return (List)super.findList(generateStatement("getBillDetail"), requestParamMap);
	}
	
	public int getBillDetailCount(Map<String, Object> requestParamMap)throws Exception {
        return (int) super.selectOne(generateStatement("getBillDetailCount"), requestParamMap);
	    
	}
	
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#getUserXbillCount(java.util.Map)
	 */
	@Override
	public Integer getUserXbillCount(Map<String, Object> pMap)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getUserXbillCount"), pMap);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#insertShopUserVantages(com.idcq.appserver.dto.user.ShopUserVantages)
	 */
	@Override
	public int insertShopUserVantages(ShopUserVantages shopUserVantages)
			throws Exception {
		return super.insert(generateStatement("insertShopUserVantages"), shopUserVantages);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#insertShopUserVantageslog(com.idcq.appserver.dto.user.ShopUserVantages)
	 */
	@Override
	public int insertShopUserVantageslog(ShopUserVantagesLog shopUserVantagesLog)
			throws Exception {
		return super.insert(generateStatement("insertShopUserVantagesLog"), shopUserVantagesLog);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#getUserVantagesByUserIdAndShopId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public BigDecimal getUserVantagesByUserIdAndShopId(Long userId, Long shopId)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("shopId", shopId);
		BigDecimal userVantages  = (BigDecimal)super.selectOne(generateStatement("getUserVantagesByUserIdAndShopId"), map);
		return userVantages;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#updateShopUserVantages(java.util.Map)
	 */
	@Override
	public int updateShopUserVantages(ShopUserVantages shopUserVantages) throws Exception {
		return super.update(generateStatement("updateShopUserVantages"), shopUserVantages);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#getIdListByProvinceIdIsNull(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getUserInfoByProvinceIdIsNull(Integer limit, Integer pNo)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("limit", limit);
		params.put("pNo", pNo);
		return (List)super.findList(generateStatement("getUserInfoByProvinceIdIsNull"),params);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#updateUserPlace(java.util.Map)
	 */
	@Override
	public int updateUserPlace(Map<String, Object> map) throws Exception {
		return super.update(generateStatement("updateUserPlace"), map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#getNoCityMobile(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getNoCityMobile(Integer limit, Integer pNo){
	Map<String,Object> params = new HashMap<String,Object>();
	params.put("limit", limit);
	params.put("pNo", pNo);
	return (List)super.findList(generateStatement("getNoCityMobile"),params);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.user.IUserDao#updateCityMobile(java.util.Map)
	 */
	@Override
	public int updateCityMobile(Map<String, Object> map) throws Exception {
		return super.update(generateStatement("updateNoCityMobile"), map);
	}

	@Override
	public void updateIdentity(Map<String, Object> map) throws Exception {
		update("updateIdentity", map);
	}

	@Override
	public void updateUserIsManager(Long userId) {
		update("updateUserIsManager", userId);
	}

	@Override
	public List<Map<String, Object>> getBillStat(Map<String, Object> parpMap)
			throws Exception {
		return (List)super.findList(generateStatement("getBillStat"), parpMap);
	}

	@Override
	public Integer getMyRefUserListCount(Map<String, Object> map)
			throws Exception {
		return (Integer)selectOne("getMyRefUserListCount", map);
	}

	@Override
	public List<Map<String, Object>> getMyRefUserList(Map<String, Object> map)
			throws Exception {
		return (List)findList("getMyRefUserList", map);
	}

	@Override
	public void updateUser(Long userId, String identityCardNo) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("identityCardNo", identityCardNo);
		update(generateStatement("updateUserCardNo"),params);
		
	}

	@Override
	public List<UserDto> queryUserByIdList(List<Long> userIdList)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("userIdList", userIdList);
		return super.findList(generateStatement("queryUserByIdList"),params);
	}

	@Override
	public int updateIsMember(long userId, int isMember) throws Exception {
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("isMember", isMember);
		return super.update("updateIsMember",params);
	}

	@Override
	public UserDto getUserByMap(Map<String, Object> requestMap) {
		return (UserDto)super.selectOne(generateStatement("getUserByMap"), requestMap);
	}

	@Override
	public UserDto getShopPrincipalInfoByMap(Map<String, Object> requestMap) {
		return (UserDto)super.selectOne(generateStatement("getShopPrincipalInfoByShopId"), requestMap);
	}
	
	
}
