package com.idcq.appserver.service.busArea.shopMember;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopMemberDto;

public interface IShopMemberService
{
    /**
     * 批量添加店内会员并返回结果，这里会对ShopMemberDto进行验证，主要包括验证手机号是否合法，姓名是否存在，店内会员是否已经存在；
     * 特别注意这里会检查mobile平台会员是否存在，不存在则自动添加
     * 
     * @param shopMemberDtos
     * @param 验证类型 
     *        0,验证条件为验证用户名手机号都不为,错误返回参数中包含用户名，手机号；1，验证条件为手机号码,qq,微信有一个不为空，错误返回参数包含手机号
     *        ，qq，微信
     * @return 这里每个map代表一个插入失败的对象，其中包括mobile,name,以及失败原因
     */

    List<Map<String, String>> addShopMembers(List<ShopMemberDto> shopMemberDtos, Integer validator);

    /**
     * 
     * 添加店内会员并返回结果，这里会对ShopMemberDto进行验证，主要包括验证手机号是否合法，姓名是否存在，店内会员是否已经存在；
     * 特别注意这里会检查mobile平台会员是否存在，不存在则自动添加
     * 
     * @param shopMemberDtos
     */
    ShopMemberDto addShopMember(ShopMemberDto shopMemberDto, Integer validtor);

    ShopMemberDto getShopMemberDetail(Long memberId) throws Exception;

    List<ShopMemberDto> queryShopMemberList(Map<String, Object> requestMap) throws Exception;

    Integer queryShopMemberCount(Map<String, Object> requestMap) throws Exception;

    Integer getShopMemberCount(Long shopId) throws Exception;

    /**
     * 更新店内会员，注间这里只要ShopMemberDto存在的属性都会更新，这里会验证手机号是否合法，姓名是否存在
     * 
     * @param shopMemberDto
     * @return
     * @throws Exception
     */
    Long updateShopMember(ShopMemberDto shopMemberDto) throws Exception;

    int delShopMemberByIds(Map<String, Object> requestMap) throws Exception;

    Map<String, Object> weixinBindShopMember(String openId, Long shopId, String nickName) throws Exception;

    /**
     * 根据是否有(memberId,qq,微信,mobile)决定是更新还是添加店内会员，其中它们的优先级为memberId,mobile,微信,qq,
     * 并且，各种更新情况只考虑添加号码时增加对应平台会员
     * @param shopMemberDto
     * @return
     * @throws Exception
     */
    Map<String, String> editShopMember(ShopMemberDto shopMemberDto) throws Exception;
    
    /**
   	 * 验证该会员是否 为店铺会员，
   	 * @param shopId：店铺ID
   	 * @param mobile：手机号
   	 * @param isUpdatePurchaseNo:是否更新店铺会员的消费次数
   	 * @return
   	 * @throws Exception
   	 */
   	 boolean isShopMemberValify(Long shopId, String mobile,BigDecimal purchaseMoney,boolean isUpdatePurchaseNo) throws Exception;

	ShopMemberDto getShopMemberDetailByMap(Map<String, Object> map) throws Exception;

    Map<String, Object> getShopMemberStat(Map<String, Object> queryParams) throws Exception;

    /**
     * 统计店内会员变动情况，查询参数中包括分页pNo,pSize
     * @param queryParams
     * @return
     * @throws Exception
     */
    Map<String, Object> getShopMemberStatDetail(Map<String, Object> queryParams) throws Exception;

    /**
     * 统计店内会员变动情况，查询参数中包括分页pNo,pSize
     * @param detail 为1时则返回相应会员列表，为2则不返回相应会员列表
     * @param queryParams
     * @return
     * @throws Exception
     */
    List<Object> getShopMemberInfo(Long shopId, Integer detail, String params) throws Exception;

	Map<String, Object> getMemberConsumerStat(Map<String, Object> searchParams);

	int updateShopMemberExceptDelAndCurrentMonth(ShopMemberDto shopMemberDto);
}
