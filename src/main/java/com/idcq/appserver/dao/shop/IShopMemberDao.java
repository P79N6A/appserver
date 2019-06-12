package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shop.ShopMemberLevelDto;
import com.idcq.appserver.dto.shop.ShopMemberStatInfo;

/**
 * 商铺会员dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IShopMemberDao {
	
	/**
	 * 新增店内会员信息
	 * @param shopMemberDto
	 * @return
	 * @throws Exception
	 */
	int addShopMember(ShopMemberDto shopMemberDto) throws Exception;
	Integer getShopMemberCount(Long shopId) throws Exception;
	/**
	 * 删除指定的店内会员
	 * @param shopMemberId
	 * @return
	 * @throws Exception
	 */
	int delShopMemberByIds(Map<String, Object> requestMap) throws Exception;
	
	/**
	 * 修改指定的店内会员
	 * @param shopMemberDto
	 * @return
	 * @throws Exception
	 */
	int updateShopMemberById(ShopMemberDto shopMemberDto) throws Exception;
	
	/**
	 * 获取指定的店内会员信息
	 * @param shopMemberId
	 * @return
	 * @throws Exception
	 */
	ShopMemberDto getShopMemberById(Long shopMemberId) throws Exception;
	
	/**
	 * 获取店内会员信息列表
	 * @param shopMemberDto
	 * @return
	 * @throws Exception
	 */
	List<ShopMemberDto> getShopMemberList(Map<String, Object> requestMap) throws Exception;
	
	/**
	 * 统计商圈店铺的店内会员数目为多少
	 * @Title: getShopMemberStatisInfoByActivityIdList 
	 * @param @param activityList
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopMemberDto>    返回类型 
	 * @throws
	 */
	List<ShopMemberDto>getShopMemberStatisInfoByActivityIdList(List<Long>activityList)throws Exception;
	
	/**
	 * 获取需要推送的店铺会员列表
	 * @Title: getNeedPushShopMemberList 
	 * @param @param needPushToUserShopList
	 * @param @return
	 * @param @throws Exception
	 * @return List<ShopMemberDto>    返回类型 
	 * @throws
	 */
	List<ShopMemberDto> getNeedPushShopMemberList(List<Long> needPushToUserShopList)throws Exception;
	/**
	 * 验证店内会员是否存在
	 * @param mobile
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	boolean validShopMbByMobileAndShopId(String mobile,Long shopId) throws Exception;
	
	/**
	 * 
	 * @param mobile
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopMemberDto getShopMbByMobileAndShopId(String mobile,Long shopId, Integer memberStatus) throws Exception;
	
	/**
	 * 检查符合条件的店铺内会员，条件为满足其中之一即可
	 * @param shopMemberDto
	 * @return
	 */
	List<ShopMemberDto> searchShopMbByShopIdAndInfos(ShopMemberDto shopMemberDto);
	
	/**
	 * 查询店铺会员数量
	 * @Title: queryShopMemberCount 
	 * @param @param requestMap
	 * @param @return
	 * @return Integer    返回类型 
	 * @throws
	 */
	Integer queryShopMemberCount(Map<String, Object> requestMap);
	
	int updateShopMemberByMobile(ShopMemberDto shopMemberDto);
	
	int updateShopMemberByWeixin(ShopMemberDto shopMemberDto);
	
	int updateShopMemberByQq(ShopMemberDto shopMemberDto);
	
	List<ShopMemberDto> getShopMemberByCoreInfo(ShopMemberDto shopMemberDto);
	
	int updateShopMemberPurchaseNum(Map<String,Object> map);
	int getNewAddShopMemberTotal(Map<String, Object> resultMap);
	List<Map<String, Object>> getConsumeShopMembersCount(Map<String, Object> resultMap);
	ShopMemberDto getShopMemberByIdMap(Map<String, Object> map);

	/**
	 * 计算一段时间内的店内会员变动情况
	 * @return
     */
	Integer countShopMemberByTime(Map<String, Object> params);

	/**
	 * 统计店内会员的变化情况
	 * @return
     */
	List<ShopMemberStatInfo> getShopMemberStatDetail(Map<String, Object> params);

	/**
	 * 统计店内会员的变化情况
	 * @return
     */
	Integer countShopMemberStatDetail(Map<String, Object> params);


	/**
	 * 查询满足消费次数的会员数量
	 * @param params
	 * @return
     */
	Integer countMemberByConsumeCount(Map<String, Object> params);

	/**
	 * 查询满足消费次数的会员
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMemberByConsumeCount(Map<String, Object> params);
	/**
	 * 查询满足消费金额的会员数量
	 * @param params
	 * @return
	 */
	Integer countMemberByConsumeAmount(Map<String, Object> params);
	/**
	 * 查询满足消费金额的会员
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMemberByConsumeAmount(Map<String, Object> params);

	/**
	 * 查询最近一次消费时间满足的会员数量
	 * @param params
	 * @return
	 */
	Integer countMemberBylastConsumeTime(Map<String, Object> params);

	/**
	 * 查询最近一次消费时间满足的会员
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMemberBylastConsumeTime(Map<String, Object> params);

	/**
	 * 查询一段时间未消费的会员数量
	 * @param params
	 * @return
	 */
	Integer countMemberByWithoutConsumeTime(Map<String, Object> params);

	/**
	 * 查询最近一次消费时间满足的会员
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMemberByWithoutConsumeTime(Map<String, Object> params);

	/**
	 * 一直未消费的会员数量
	 * @param params
	 * @return
	 */
	Integer countMemberByWithoutConsume(Map<String, Object> params);
	/**
	 * 一直未消费的会员数量
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMemberByWithoutConsume(Map<String, Object> params);

	Integer getMemberConsumerStatCount(Map<String, Object> searchParams);
	List<Map> getMemberConsumerStat(Map<String, Object> searchParams);
	int updateShopMemberExceptDelAndCurrentMonth(ShopMemberDto shopMemberDto);
	List<Map<String,Integer>> queryBirthDayMemberNum(Map<String, String> param);
	int updateShopMemberByMobileOrUserId(ShopMemberDto shopMemberDto);
	int updateShopMemberByLevelEntity(ShopMemberLevelDto shopMemberLevelDto);

}
