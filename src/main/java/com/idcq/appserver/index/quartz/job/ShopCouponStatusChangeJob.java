package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BusAreaActStatusEnum;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.shopCoupon.IShopCouponService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 商圈活动切换
 * @ClassName: BusAreaActivityStatusChange 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年3月14日 上午10:23:24 
 *
 */
public class ShopCouponStatusChangeJob extends QuartzJobBean{

	private Log logger = LogFactory.getLog(this.getClass());
	
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		
		PageModel pageModel=new PageModel();
		pageModel.setToPage(1);
		
		
		ShopCouponDto shopCouponDto = new ShopCouponDto();
		shopCouponDto.setEndDate(new Date());
//		shopCouponDto.setCouponStatus(CommonConst.COUPON_IS_ENABLE);
		shopCouponDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		IShopCouponService shopCouponService = (IShopCouponService)BeanFactory.getBean(IShopCouponService.class);
		while(true){
			List<ShopCouponDto> shopCouponDtoList=shopCouponService.getShopCouponListByDtoAndPage(shopCouponDto,pageModel);
			if(shopCouponDtoList==null || shopCouponDtoList.size()==0){
				break;
			}
			dealShopCoupon(shopCouponDtoList, shopCouponService); 
			pageModel.setToPage(pageModel.getToPage()+1);
		}
		
		PageModel pageModel2=new PageModel();
		pageModel2.setToPage(1);
		while (true) {
			List<UserShopCouponDto> userShopCouponDtoList = new ArrayList<UserShopCouponDto>();
			try {
				userShopCouponDtoList = shopCouponService.getUserShopCouponList(pageModel2);
			} catch (Exception e) {
				 throw new ValidateException(CodeConst.CODE_SYSTEM_ERROR, "查询用户优惠券数据异常");
			}
			if(userShopCouponDtoList==null || userShopCouponDtoList.size() == 0){
				break;
			}
			dealUserShopCoupon(userShopCouponDtoList, shopCouponService); 
			pageModel2.setToPage(pageModel2.getToPage()+1);
		}
	}
	
	private void dealUserShopCoupon(List<UserShopCouponDto> userShopCouponDtoList,IShopCouponService shopCouponService) {
		Date currentDate=new Date();
		List<UserShopCouponDto> userShopCouponDtoUpdateList=new ArrayList<UserShopCouponDto>();
		UserShopCouponDto updateUserShopCouponDto = null;
		for (UserShopCouponDto userShopCouponDto : userShopCouponDtoList) {
			if(currentDate.compareTo(DateUtils.parse(DateUtils.format(userShopCouponDto.getEndDate(), DateUtils.ENDTIME_FORMAT)))>0){
				updateUserShopCouponDto = new UserShopCouponDto();
				updateUserShopCouponDto.setCouponStatus(CommonConst.USER_SHOP_COUPON_EXPIRE);
				updateUserShopCouponDto.setUserShopCouponId(userShopCouponDto.getUserShopCouponId());
				userShopCouponDtoUpdateList.add(updateUserShopCouponDto);
			}
		}
		if(userShopCouponDtoUpdateList!=null && userShopCouponDtoUpdateList.size()>0){
			shopCouponService.batchUpdateUserShopCoupon(userShopCouponDtoUpdateList);
		}
	}

	private void dealShopCoupon(List<ShopCouponDto> shopCouponDtoList,IShopCouponService shopCouponService) {
		Date currentDate=new Date();
		List<ShopCouponDto> shopCouponDtoUpdateList=new ArrayList<ShopCouponDto>();
		ShopCouponDto updateShopCouponDto = null;
		for (ShopCouponDto shopCouponDto : shopCouponDtoList) {
			if(currentDate.compareTo(DateUtils.parse(DateUtils.format(shopCouponDto.getEndDate(), DateUtils.ENDTIME_FORMAT)))>0){
				updateShopCouponDto = new ShopCouponDto();
				updateShopCouponDto.setCouponStatus(CommonConst.COUPON_IS_OUT_OF_DATE);
				updateShopCouponDto.setShopCouponId(shopCouponDto.getShopCouponId());
				shopCouponDtoUpdateList.add(updateShopCouponDto);
			}
		}
		if(shopCouponDtoUpdateList!=null && shopCouponDtoUpdateList.size()>0){
			shopCouponService.batchUpdateShopCoupon(shopCouponDtoUpdateList);
		}
	}

	
}
