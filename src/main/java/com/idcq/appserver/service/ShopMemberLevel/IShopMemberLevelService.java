package com.idcq.appserver.service.ShopMemberLevel;

import java.util.Map;

import com.idcq.appserver.dto.shop.ShopMemberLevelDto;

public interface IShopMemberLevelService {

	Map<String, Integer> updateShopMemberLevel(ShopMemberLevelDto shopMemberLevelDto);

	Map<String, Object> getShopMemberLevelList(Map<String, Object> paramMap);
	
	/**
	 * 升级会员等级：
	 * 情况1：如果消费为0属于对应的店内会员的某个等级，
	 *        则将新加入的店内会员加入该等级
	 * 情况2：如果店内会员消费了，则查询该会员的消费总额
	 *        是否达到了对应的某个等级，如果该之前存在等
	 *        级，且要求自动升级，则该店内会员就进行升级。
	 * @param mobile 会员手机号,必传 
	 * @param shopId 会员所在店铺Id,必传 
	 * @param flag 新增会员-1；下单结算-2,必传 
	 * @throws Exception 
	 */
	void upgrateShopMemberLevel(String mobile,Long shopId,int flag);
	
	int deleteShopMemberLevel(String shopIdStr,String shopMemberLevelIdStr,String operateTypeStr);

}
