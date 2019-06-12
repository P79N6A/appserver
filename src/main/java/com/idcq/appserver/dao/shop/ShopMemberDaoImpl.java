package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopMemberLevelDto;
import com.idcq.appserver.dto.shop.ShopMemberStatInfo;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopMemberDto;

@Repository
public class ShopMemberDaoImpl extends BaseDao<ShopMemberDto>implements IShopMemberDao{

	@Override
	public int addShopMember(ShopMemberDto shopMemberDto) throws Exception {
		return super.insert("addShopMember", shopMemberDto);
	}

	@Override
	public int delShopMemberByIds(Map<String, Object> requestMap) throws Exception {
		return super.delete("delShopMemberByIds", requestMap);
	}

	@Override
	public int updateShopMemberById(ShopMemberDto shopMemberDto)
			throws Exception {
		return super.update("updateShopMemberById",shopMemberDto);
	}

	@Override
	public ShopMemberDto getShopMemberById(Long shopMemberId) throws Exception {
		return (ShopMemberDto)super.selectOne("getShopMemberById",shopMemberId);
	}


	@Override
	public List<ShopMemberDto> getShopMemberList(Map<String, Object> requestMap)
			throws Exception {
		
		return super.findList("getShopMemberList",requestMap);
	}
	@Override
	public boolean validShopMbByMobileAndShopId(String mobile, Long shopId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("mobile",mobile);
		map.put("shopId",shopId);
		Integer count = (Integer)super.selectOne("validShopMbByMobileAndShopId", map);
		return count != null ? (count > 0 ? true : false ) : false;
	}

	@Override
	public Integer getShopMemberCount(Long shopId) throws Exception {
		return (Integer)super.selectOne("getShopMemberCount",shopId);
	}
	@Override
	public ShopMemberDto getShopMbByMobileAndShopId(String mobile, Long shopId, Integer memberStatus)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("mobile",mobile);
		map.put("shopId",shopId);
		map.put("memberStatus",memberStatus);
		return (ShopMemberDto)super.selectOne("getShopMbByMobileAndShopId", map);
	}
	
	/**
	 * 获取商圈店铺店内会员的统计信息
	 * @Title: getShopMemberStatisInfoByActivityIdList 
	 * @param @param activityList
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopMemberDto> getShopMemberStatisInfoByActivityIdList(
			List<Long> activityIdList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("activityIdList", activityIdList);
		return super.findList(generateStatement("getShopMemberStatisInfoByActivityIdList"),params);
	}
	
	
	/**
	 * 获取需要推送的商铺店内会员列表
	 * @Title: getNeedPushShopMemberList 
	 * @param @param needPushToUserShopList
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopMemberDto> getNeedPushShopMemberList(
			List<Long> needPushToUserShopList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopIdList", needPushToUserShopList);
		return super.findList(generateStatement("getNeedPushShopMemberList"),params);
	}

	@Override
	public List<ShopMemberDto> searchShopMbByShopIdAndInfos(
			ShopMemberDto shopMemberDto) {
		return super.findList("searchByIdAndConditions", shopMemberDto);
	}
	
	/**
	 * 查询店铺会员数量
	 * @Title: queryShopMemberCount 
	 * @param @param requestMap
	 * @param @return  
	 * @throws
	 */
	public Integer queryShopMemberCount(Map<String, Object> requestMap) {
		return (Integer)super.selectOne("getShopMemberCount", requestMap);
	}

    @Override
    public int updateShopMemberByMobileOrUserId(ShopMemberDto shopMemberDto)
    {
        return super.update("updateShopMemberByMobileOrUserId",shopMemberDto);
    }
    
    @Override
    public int updateShopMemberByMobile(ShopMemberDto shopMemberDto)
    {
        return super.update("updateShopMemberByMobile",shopMemberDto);
    }

    @Override
    public int updateShopMemberByWeixin(ShopMemberDto shopMemberDto)
    {
        return super.update("updateShopMemberByWeixin",shopMemberDto);
    }

    @Override
    public int updateShopMemberByQq(ShopMemberDto shopMemberDto)
    {
        return super.update("updateShopMemberByQq",shopMemberDto);
    }

    @Override
    public List<ShopMemberDto> getShopMemberByCoreInfo(ShopMemberDto shopMemberDto)
    {
        // TODO Auto-generated method stub
        return super.findList("getShopMemberByCoreInfo", shopMemberDto);
    }
	
    @Override
    public int updateShopMemberPurchaseNum(Map<String,Object> map)
    {
        return super.update("updateShopMemberPurchaseNum",map);
    }

	@Override
	public int getNewAddShopMemberTotal(Map<String, Object> resultMap) {
		return (Integer)super.selectOne("getNewAddShopMemberTotal", resultMap);
	}

	@Override
	public List<Map<String, Object>> getConsumeShopMembersCount(
			Map<String, Object> resultMap) {
		return (List) findList(generateStatement("getConsumeShopMembersCount"), resultMap);
	}

	@Override
	public ShopMemberDto getShopMemberByIdMap(Map<String, Object> map) {
		return (ShopMemberDto)super.selectOne("getShopMemberByIdMap", map);
	}


	@Override
	public Integer countShopMemberByTime(Map<String, Object> params) {
		return (Integer)super.selectOne("countShopMemberByTime", params);
	}

	@Override
	public List<ShopMemberStatInfo> getShopMemberStatDetail(Map<String, Object> params) {
		return (List) findList(generateStatement("getShopMemberStatDetail"), params);
	}

	@Override
	public Integer countShopMemberStatDetail(Map<String, Object> params) {
		return (Integer)super.selectOne("countShopMemberStatDetail", params);
	}

	@Override
	public Integer countMemberByConsumeCount(Map<String, Object> params) {
		return (Integer)super.selectOne("countMemberByConsumeCount", params);
	}

	@Override
	public List<Map<String, Object>> queryMemberByConsumeCount(Map<String, Object> params) {
		return (List) findList(generateStatement("queryMemberByConsumeCount"), params);
	}

	@Override
	public Integer countMemberByConsumeAmount(Map<String, Object> params) {
		return (Integer)super.selectOne("countMemberByConsumeAmount", params);
	}

	@Override
	public List<Map<String, Object>> queryMemberByConsumeAmount(Map<String, Object> params) {
		return (List) findList(generateStatement("queryMemberByConsumeAmount"), params);
	}

	@Override
	public Integer countMemberBylastConsumeTime(Map<String, Object> params) {
		return (Integer)super.selectOne("countMemberBylastConsumeTime", params);
	}

	@Override
	public List<Map<String, Object>> queryMemberBylastConsumeTime(Map<String, Object> params) {
		return (List) findList(generateStatement("queryMemberBylastConsumeTime"), params);
	}

	@Override
	public Integer countMemberByWithoutConsume(Map<String, Object> params) {
		return (Integer)super.selectOne("countMemberByWithoutConsume", params);
	}

	@Override
	public List<Map<String, Object>> queryMemberByWithoutConsume(Map<String, Object> params) {
		return (List) findList(generateStatement("queryMemberByWithoutConsume"), params);
	}

	@Override
	public Integer countMemberByWithoutConsumeTime(Map<String, Object> params) {
		return (Integer)super.selectOne("countMemberByWithoutConsumeTime", params);
	}

	@Override
	public List<Map<String, Object>> queryMemberByWithoutConsumeTime(Map<String, Object> params) {
		return (List) findList(generateStatement("queryMemberByWithoutConsumeTime"), params);
	}


	@Override
	public Integer getMemberConsumerStatCount(Map<String, Object> searchParams) {
		return (Integer)super.selectOne("getMemberConsumerStatCount", searchParams);
	}

	@Override
	public List<Map> getMemberConsumerStat(Map<String, Object> searchParams) {
		return (List) findList(generateStatement("getMemberConsumerStat"), searchParams);
	}

	@Override
	public int updateShopMemberExceptDelAndCurrentMonth(
			ShopMemberDto shopMemberDto) {
		return super.update("updateShopMemberExceptDelAndCurrentMonth",shopMemberDto);
	}

	@Override
	public List<Map<String,Integer>> queryBirthDayMemberNum(Map<String, String> param) {
		return (List) findList(generateStatement("queryBirthDayMemberNum"), param);
	}

	@Override
	public int updateShopMemberByLevelEntity(
			ShopMemberLevelDto shopMemberLevelDto) {
		return super.update("updateShopMemberByLevelEntity",shopMemberLevelDto);
	}
	
	

}