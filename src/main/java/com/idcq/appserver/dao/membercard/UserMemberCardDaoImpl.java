package com.idcq.appserver.dao.membercard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.membercard.UserMemberCardDto;

@Repository
public class UserMemberCardDaoImpl extends BaseDao<UserMemberCardDto> implements
		IUserMemberCardDao {
	
	/**
	 * 查询会员卡余额接口
	 */
	public UserMemberCardDto getLeftMoney(Integer queryType, Long accountId, Long userId,
			Long shopId) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("queryType", queryType);
		map.put("accountId", accountId);
		map.put("userId", userId);
		map.put("shopId", shopId);
		
		return (UserMemberCardDto)super.selectOne(generateStatement("getLeftMoney"), map);
	}

	public UserMemberCardDto getMemberCardInfoById(Long accountId) {
		return (UserMemberCardDto)super.selectOne(generateStatement("getMemberCardInfoById"), accountId);
	}
	
	public Integer queryShopIdByUmcId(Long umcId) {
		return (Integer) this.selectOne(generateStatement("queryShopIdByUmcId"), umcId);
	}

}
