package com.idcq.appserver.service.shopCoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopCoupon.RequstCouponDeductModel;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;

public interface IShopCouponService {
	/**
	 * 发送优惠券
	 * 
	 * @Function: com.idcq.appserver.service.shopCoupon.IShopCouponService.sendCoupon
	 * @Description:
	 *
	 * @param shopId
	 * @param shopCouponId
	 * @param mobile
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月20日 下午5:10:36
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月20日    ChenYongxin      v1.0.0         create
	 */
	void sendCoupon(Long shopId ,Long shopCouponId,String mobile) throws Exception;
	/**
	 * 获取优惠券
	 * 
	 * @Function: com.idcq.appserver.service.shopCoupon.IShopCouponService.getCoupon
	 * @Description:
	 *
	 * @param shopId
	 * @param shopCouponId
	 * @param mobile
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月21日 下午3:28:57
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月21日    ChenYongxin      v1.0.0         create
	 */
	void userGetCoupon(Long shopId ,Long shopCouponId,String mobile) throws Exception;
	/**
	 * 获取PCS8获取优惠券领用明细
	 * 
	 */
	 Map<String, Object> getCouponReceiveDetail(Long shopId ,Long shopCouponId,Integer status,Integer pageNo,Integer pageSize) throws Exception;;

	int updateShopCoupon(ShopCouponDto shopCouponDto) throws Exception;

	PageModel getShopCouponList(ShopCouponDto shopCouponDto);

	Map<String, Object> getCouponDeductAmount(RequstCouponDeductModel requestModel) throws Exception;
	Map<String,Object> shopCouponDetail(ShopCouponDto shopCouponDto) throws Exception;

	void operateShopCoupon(String shopId, String shopCouponIds,
			String operateType);
	List<ShopCouponDto> getShopCouponListByDtoAndPage(ShopCouponDto shopCouponDto,PageModel pageModel);
	void batchUpdateShopCoupon(List<ShopCouponDto> shopCouponDtoUpdateList);
	List<UserShopCouponDto> getUserShopCouponList(PageModel pageModel2) throws Exception;
	void batchUpdateUserShopCoupon(List<UserShopCouponDto> userShopCouponDtoUpdateList);


}
