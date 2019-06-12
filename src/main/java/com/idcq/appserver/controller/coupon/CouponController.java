package com.idcq.appserver.controller.coupon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.dto.coupon.UserCouponDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.coupon.ICouponService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;



/**
 * 优惠券controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月30日
 * @time 上午11:02:55
 */
@Controller
public class CouponController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public ICouponService couponService;
	@Autowired
	public IMemberServcie memberServcie;
	
	
	/**
	 * 获取商铺优惠券
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/coupon/getShopCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getShopCoupon(HttpServletRequest request){
		try {
			logger.info("获取商铺优惠券列表-start");
			String pageNO = RequestUtils.getQueryParam(request,CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request,CommonConst.PAGE_SIZE);
			String shopId = RequestUtils.getQueryParam(request,"shopId");
			CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			CouponDto coupon = new CouponDto();
			coupon.setShopId(Long.valueOf(shopId));
			/*
			 * 首先检索符合条件的总记录数
			 * 然后检索数据列表
			 * 最后封装到pageModel
			 */
			PageModel pageModel = this.couponService.
					getShopCouponList(coupon, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));
			
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取商铺优惠券列表", msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺优惠券列表-系统异常", e);
			throw new APISystemException("获取商铺优惠券列表-系统异常", e);
		}
	}
	
	/**
	 * 获取用户优惠券
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/coupon/getUserCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserCoupon(HttpServletRequest request){
		try {
			logger.info("获取用户优惠券列表-start");
			String pageNO = RequestUtils.getQueryParam(request,CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request,CommonConst.PAGE_SIZE);
			String userId = RequestUtils.getQueryParam(request,"userId");
			String couponStatus = RequestUtils.getQueryParam(request,"couponStatus");
			CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			Long num = this.memberServcie.authenUserById(Long.valueOf(userId));
			CommonValidUtil.validPositLong(num, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			UserCouponDto coupon = new UserCouponDto();
			if(!StringUtils.isBlank(couponStatus)){
				CommonValidUtil.validNumStr(couponStatus, CodeConst.CODE_PARAMETER_NOT_VALID, 
						CodeConst.MSG_FORMAT_ERROR_COUPONSTATUS);
				coupon.setCouponStatus(Integer.valueOf(couponStatus));
			}
			coupon.setUserId(Long.valueOf(userId));
			/*
			 * 首先检索符合条件的总记录数
			 * 然后检索数据列表
			 * 最后封装到pageModel
			 */
			PageModel pageModel = this.couponService.
					getUserCouponList(coupon, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));
			
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取用户优惠券列表", msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户优惠券列表-系统异常", e);
			throw new APISystemException("获取用户优惠券列表-系统异常", e);
		}
	}
	
	/**
	 * 用户领取优惠券
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/coupon/grabCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object grabCoupon(HttpServletRequest request){
		try {
			logger.info("用户领取优惠券-start");
			String userId = RequestUtils.getQueryParam(request,"userId");
			String couponId = RequestUtils.getQueryParam(request,"couponId");
			String numberStr = RequestUtils.getQueryParam(request,"number");
			CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validStrNull(couponId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_COUNPONID);
			CommonValidUtil.validNumStr(couponId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_COUNPONID);
			Long uId = Long.valueOf(userId);
			Long cpId = Long.valueOf(couponId);
			if(null==numberStr){
				numberStr="1";
			}
			int number=Integer.parseInt(numberStr);
			/*---------领取前的一系列验证-----------*/
			//1,用户存在性
			Long id = this.memberServcie.authenUserById(uId);
			CommonValidUtil.validPositLong(id, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			//2,优惠券存在性
			CouponDto coupon = this.couponService.getCouponById(cpId);
			CommonValidUtil.validObjectNull(coupon, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_COUPON);
			//3,可用数量
			CommonValidUtil.validPositInt(coupon.getAvailableNumber(),CodeConst.CODE_NOT_AVAIL_NUM,
					CodeConst.MSG_NOT_AVAIL_NUM_COUPON);
			//4,领取时间范围
			CommonValidUtil.validCurDateOfDateRange(coupon.getIssueFromDate(), coupon.getIssueToDate(), 
					CodeConst.CODE_NOT_IN_GRAB_DATERANGE, CodeConst.MSG_NOT_IN_GRAB_DATERANGE);
			//5,每人最多可领取多少张
			int perNum = coupon.getObtainNumberPerDayPerPerson();
			//6,当天已经领取了多少张
			Date startDate = DateUtils.getZeroTimeOfCurDate();	//当天零点
			Date endDate = DateUtils.getEndTimeOfCurDate();		//当天最后一秒
			int num = this.couponService.getGrapNumOfDateRange(uId, cpId, startDate, endDate);
			//7,当天是否可以继续领取
			if(num >= perNum){
				throw new ValidateException(CodeConst.CODE_CANNOT_GRAB_CURDATE,
						CodeConst.MSG_CANNOT_GRAB_CURDATE);
			}
			/*---------验证通过，领取-----------*/
			UserCouponDto coupon2 = new UserCouponDto();
			coupon2.setShopId(coupon.getShopId());
			coupon2.setUserId(uId);
			coupon2.setCouponId(cpId);
			coupon2.setPrice(coupon.getPrice());
			coupon2.setCouponStatus(0);
			coupon2.setObtainTime(new Date());
			coupon2.setAvailableNumber(coupon.getAvailableNumber());
			coupon2.setGoodsId(coupon.getGoodsId());
			coupon2.setUsedNumber(coupon.getUsedNumber());
			coupon2.setNumber(number);
			coupon2.setValue(coupon.getValue());
			/*
			 * 1,生产优惠券类型的订单
			 * 2,关联用户领取优惠券表及对应优惠券订单号
			 * 3,对应优惠券可用数量减去1
			 */
			String orderId = this.couponService.grabCoupon(coupon2);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("amount", coupon.getPrice()*number);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "用户领取优惠券成功", map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户领取优惠券-系统异常", e);
			throw new APISystemException("用户领取优惠券-系统异常", e);
		}
	}
	
	/**
	 * 获取商铺红包接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/coupon/getShopRedPacket",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getShopRedPacket(HttpServletRequest request){
		try {
			logger.info("获取商铺红包-start");
			String shopId =RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validNumStr(shopId,CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validStrLongFormat(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Map pModel = couponService.getShopRedPacket(Long.parseLong(shopId),pNo,pSize);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取信息成功！", pModel);
		} catch(ServiceException e){
			logger.error("获取商铺红包异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取商铺红包系统异常",e);
			throw new APISystemException("获取商铺红包系统异常", e);
		}
	}
	
}