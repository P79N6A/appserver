package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;


public interface IUserShopCouponDao {
	/**
	 * 插入用户商铺优惠券
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao.insertUserShopCoupon
	 * @Description:
	 *
	 * @param userShopCouponDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月21日 上午11:10:16
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月21日    ChenYongxin      v1.0.0         create
	 */
	Integer insertUserShopCoupon(UserShopCouponDto userShopCouponDto) throws Exception;
	/**
	 * 插入用户商铺优惠券
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao.insertUserShopCoupon
	 * @Description:
	 *
	 * @param userShopCouponDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月21日 上午11:10:16
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月21日    ChenYongxin      v1.0.0         create
	 */
	Integer beachInsertUserShopCoupon(List<UserShopCouponDto> userShopCouponDtos)  throws Exception;
	/**
	 * 获取用户持有优惠券列表
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao.getUserShopCouponList
	 * @Description:
	 *
	 * @param userShopCouponDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月22日 上午10:19:28
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月22日    ChenYongxin      v1.0.0         create
	 */
	List<UserShopCouponDto> getUserShopCouponList(UserShopCouponDto userShopCouponDto)  throws Exception;
	/**
	 * 获取总记录数
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao.getUserShopCouponCount
	 * @Description:
	 *
	 * @param userShopCouponDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月23日 上午9:14:05
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月23日    ChenYongxin      v1.0.0         create
	 */
	Integer getUserShopCouponCount(UserShopCouponDto userShopCouponDto)  throws Exception;

	/**
	 * 更新用户持有优惠券
	 * 
	 * @Function: com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao.updateUserShopCoupon
	 * @Description:
	 *
	 * @param userShopCouponDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月22日 下午7:38:54
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月22日    ChenYongxin      v1.0.0         create
	 */
	Integer updateUserShopCouponByCouponId(UserShopCouponDto userShopCouponDto) throws Exception;
	void batchUpdateUserShopCoupon(Map<String, Object> params);
}
