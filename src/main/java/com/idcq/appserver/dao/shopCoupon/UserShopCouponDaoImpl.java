package com.idcq.appserver.dao.shopCoupon;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
@Repository
public class UserShopCouponDaoImpl extends BaseDao<UserShopCouponDto> implements IUserShopCouponDao{

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao#insertUserShopCoupon(com.idcq.appserver.dto.shopCoupon.UserShopCouponDto)
	 */
	@Override
	public Integer insertUserShopCoupon(UserShopCouponDto userShopCouponDto)
			throws Exception {
		
		return super.insert("insertUserShopCoupon", userShopCouponDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao#beachInsertUserShopCoupon(java.util.List)
	 */
	@Override
	public Integer beachInsertUserShopCoupon(
			List<UserShopCouponDto> userShopCouponDtos) throws Exception {
		
		return super.insert("beachInsertUserShopCoupon", userShopCouponDtos);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao#getUserShopCouponList(com.idcq.appserver.dto.shopCoupon.UserShopCouponDto)
	 */
	@Override
	public List<UserShopCouponDto> getUserShopCouponList(
			UserShopCouponDto userShopCouponDto) throws Exception {
		
		return findList("getUserShopCouponList", userShopCouponDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao#updateUserShopCoupon(com.idcq.appserver.dto.shopCoupon.UserShopCouponDto)
	 */
	@Override
	public Integer updateUserShopCouponByCouponId(UserShopCouponDto userShopCouponDto)
			throws Exception {
		
		return update("updateUserShopCouponByCouponId", userShopCouponDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao#getUserShopCouponCount(com.idcq.appserver.dto.shopCoupon.UserShopCouponDto)
	 */
	@Override
	public Integer getUserShopCouponCount(UserShopCouponDto userShopCouponDto)
			throws Exception {
		return (Integer) selectOne("getUserShopCouponCount", userShopCouponDto);
	}

	@Override
	public void batchUpdateUserShopCoupon(Map<String, Object> params) {
		super.update(generateStatement("batchUpdateUserShopCoupon"), params);
	}

}
