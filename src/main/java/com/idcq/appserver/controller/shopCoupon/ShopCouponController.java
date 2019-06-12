package com.idcq.appserver.controller.shopCoupon;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shopCoupon.RequstCouponDeductModel;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shopCoupon.IShopCouponService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class ShopCouponController  extends BaseController{
	private final static Logger logger = LoggerFactory.getLogger(ShopCouponController.class);
	@Autowired
	private IShopCouponService shopCouponService;
	@Autowired
	private IShopServcie shopServcie;
	@Autowired
	private IShopMemberService shopMemberServcie;
	@Autowired
	private ICollectService collectService;
	/**
	 * PSC1： 新增/修改优惠券接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/shopCoupon/updateShopCoupon",
			"/service/shopCoupon/updateShopCoupon",
			"/session/shopCoupon/updateShopCoupon" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateShopCoupon(HttpServletRequest request) throws Exception {
		ShopCouponDto shopCouponDto = getRequestModel(request, ShopCouponDto.class);
		validateShopCoupon(shopCouponDto);
//		String token = RequestUtils.getQueryParam(request, "token");
//		collectService.queryShopAndTokenExists(shopCouponDto.getShopId(), token);
		int shopCouponId = shopCouponService.updateShopCoupon(shopCouponDto);
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("shopCouponId", shopCouponId);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增/修改优惠券成功！",map);
	}
	
	/**
	 * PSC5发放优惠券接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/shopCoupon/sendCoupon","/service/shopCoupon/sendCoupon","/session/shopCoupon/sendCoupon"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object sendCoupon(HttpServletRequest request) throws Exception{
		
		/*		
		shopId	int		是	商铺ID
		memberIds	string		是	店铺会员ID, 多个通过英文逗号(,)分隔，如1,2,3
		shopCouponId	String		是	店铺优惠券ID
		*/
		
		logger.info("PSC5发放优惠券接口-start" );
		Map<String, Object> parms = getRequestMap(request);
		String shopIdStr = (String) parms.get("shopId");
		String shopCouponIdStr = (String) parms.get("shopCouponId");
		String memberIds = (String) parms.get("memberIds");
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "mobile不能为空");
		CommonValidUtil.validStrNull(shopCouponIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopCouponId不能为空");
		CommonValidUtil.validStrNull(memberIds, CodeConst.CODE_PARAMETER_NOT_VALID, "memberIds不能为空");
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		Long shopCouponId = NumberUtil.strToLong(shopCouponIdStr, "shopCouponId");
		
		shopCouponService.sendCoupon(shopId, shopCouponId, memberIds);
		
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "发送成功！",null, DateUtils.DATETIME_FORMAT);
	}

	/**
	 * PSC6领取优惠券接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/shopCoupon/getCoupon","/service/shopCoupon/getCoupon","/session/shopCoupon/getCoupon"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object userGetCoupon(HttpServletRequest request) throws Exception{
		
		/*		
			shopId	int		是	商铺ID
			mobile	string		是	手机号码
			shopCouponId	String		是	店铺优惠券ID
		*/
		
		logger.info("PSC6领取优惠券接口-start" );
		Map<String, Object> parms = getRequestMap(request);
		String shopIdStr = (String) parms.get("shopId");
		String shopCouponIdStr = (String) parms.get("shopCouponId");
		String mobile = (String) parms.get("mobile");
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
		CommonValidUtil.validStrNull(shopCouponIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopCouponId不能为空");
		CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, "mobile不能为空");
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		Long shopCouponId = NumberUtil.strToLong(shopCouponIdStr, "shopCouponId");
		
		shopCouponService.userGetCoupon(shopId, shopCouponId, mobile);
		
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "领取优惠券成功！",null, DateUtils.DATETIME_FORMAT);
	}

	/**
	 * PSC6领取优惠券接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/shopCoupon/getCouponReceiveDetail","/service/shopCoupon/getCouponReceiveDetail","/session/shopCoupon/getCouponReceiveDetail"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCouponReceiveDetail(HttpServletRequest request) throws Exception{
		
		/*		
			shopId	int		是	商铺ID，当token参数必填时，shopId必填
			shopCouponId	int		是	店铺优惠券ID
			couponStatus	int		否	优惠券使用状态。未使用=0,已使用=1'
		*/
		
		logger.info("PCS8获取优惠券领用明细-start" );
		Map<String, Object> parms = getRequestMap(request);
		String shopIdStr = (String) parms.get("shopId");
		String shopCouponIdStr = (String) parms.get("shopCouponId");
		String couponStatusStr = (String) parms.get("couponStatus");
		String pageSize = (String) parms.get("pageSize");
		String pageNo = (String) parms.get("pageNo");
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
		CommonValidUtil.validStrNull(shopCouponIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopCouponId不能为空");
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		Long shopCouponId = NumberUtil.strToLong(shopCouponIdStr, "shopCouponId");
		Integer couponStatus = null;
		if(StringUtils.isNotBlank(couponStatusStr)){
			couponStatus = NumberUtil.strToNum(couponStatusStr, "couponStatus");

		}

		Map<String, Object>  result = shopCouponService.getCouponReceiveDetail(shopId, shopCouponId, couponStatus, 
				PageModel.handPageNo(pageNo), PageModel.handPageSize(pageSize));
		
		logger.info("PCS8获取优惠券领用明细-end" );
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取优惠券领用明细成功！",result, DateUtils.DATETIME_FORMAT);
	}
	
	private void validateShopCoupon(ShopCouponDto shopCouponDto) throws Exception {
		if(shopCouponDto != null){
			CommonValidUtil.validLongNull(shopCouponDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			//优惠券名称
			CommonValidUtil.validStrNull(shopCouponDto.getCouponName(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_COUPON_NAME_NOT_NULL);
			//优惠券面额
			CommonValidUtil.validDoubleNull(shopCouponDto.getCouponAmount(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_COUPON_AMOUNT_NOT_NULL);
//			优惠券发行总数
			CommonValidUtil.validIntNoNull(shopCouponDto.getTotalNum(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_COUPON_TOTAL_NUM_NOT_NULL);
//			有效期开始日期
			if(shopCouponDto.getBeginDate()==null){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_BEGIN_DATE_NOT_NULL);
			}
//			有效期结束日期
			if(shopCouponDto.getEndDate() == null){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_END_DATE_NOT_NULL);
			}
			if(shopCouponDto.getCouponType() != null){
				if(shopCouponDto.getCouponType() != CommonConst.COUPON_TYPE_IS_ALL_GOODS
						&& shopCouponDto.getCouponType() != CommonConst.COUPON_TYPE_IS_GOODS_TYPE){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_COUPON_TYPE);
				}
			}else{
				shopCouponDto.setCouponType(CommonConst.COUPON_TYPE_IS_ALL_GOODS);
			}
			if(shopCouponDto.getIsUsedTogether() != null){
				if(shopCouponDto.getIsUsedTogether() != CommonConst.USED_TOGETHER_IS_FALSE
						&& shopCouponDto.getIsUsedTogether() != CommonConst.USED_TOGETHER_IS_TRUE){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USED_TOGETHER);
				}
			}else{
				shopCouponDto.setIsUsedTogether(CommonConst.USED_TOGETHER_IS_FALSE);
			}
			CommonValidUtil.validIntNoNull(shopCouponDto.getIsShared(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_IS_SHARED_NOT_NULL);
			if(shopCouponDto.getIsShared() != null){
				if(shopCouponDto.getIsShared() != CommonConst.SHARED_IS_FALSE
						&& shopCouponDto.getIsShared() != CommonConst.SHARED_IS_TRUE){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHARED);
				}
			}else{
				shopCouponDto.setIsShared(CommonConst.SHARED_IS_FALSE);
			}
			if(shopCouponDto.getIsShared() == 1){
				CommonValidUtil.validIntNoNull(shopCouponDto.getMaxNumPerPerson(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MAX_NUMPER_PERSON_NOT_NULL);
			}
			//如果客户端没有限制没人限制领取张数，默认为不限制（即设置为最大值）
			if(shopCouponDto.getMaxNumPerPerson()== null || shopCouponDto.getMaxNumPerPerson() <= 0){
				shopCouponDto.setMaxNumPerPerson(shopCouponDto.getTotalNum());
			}
			if(shopCouponDto.getMaxNumPerOrder() == null || shopCouponDto.getMaxNumPerOrder() == 0){
				shopCouponDto.setMaxNumPerOrder(1);
			}
			if(shopCouponDto.getIsDelete() == null){
				shopCouponDto.setIsDelete(0);
			}
			if(shopCouponDto.getCouponType() == CommonConst.COUPON_TYPE_IS_GOODS_TYPE){
				if(shopCouponDto.getGoodsCategoryIds()==null || shopCouponDto.getGoodsCategoryIds().length() == 0){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_GOODSCATEGORYIDS);
				}
			}
		}else{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_SHOP_COUPON_NOT_NULL);
		}
	}
	
	
	/**
	 * PSC2：查询优惠券列表信息接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/shopCoupon/shopCouponList",
			"/service/shopCoupon/shopCouponList",
			"/session/shopCoupon/shopCouponList" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object shopCouponList(HttpServletRequest request) throws Exception {
		ShopCouponDto shopCouponDto = getRequestModel(request, ShopCouponDto.class);
//		String token = RequestUtils.getQueryParam(request, "token");
//		if(token != null){
//			CommonValidUtil.validLongNull(shopCouponDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
//			collectService.queryShopAndTokenExists(shopCouponDto.getShopId(), token);
//		}
		Integer pageNo = shopCouponDto.getPageNo();
		if(pageNo==null){
			shopCouponDto.setPageNo(1);
		}
		if(shopCouponDto.getPageSize() == null){
			shopCouponDto.setPageSize(10);
		}
		shopCouponDto.setPageNo((shopCouponDto.getPageNo() - 1) * shopCouponDto.getPageSize());
		
		if(shopCouponDto!=null && shopCouponDto.getIsDelete()==null){
			shopCouponDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		}
		if(null != shopCouponDto.getUserShopCouponIds()){
			CommonValidUtil.validIntNoNull(shopCouponDto.getUsedFlag(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_USERFLAG_NOT_NULL);
		}
		if(null == shopCouponDto.getUserShopCouponStatus() && shopCouponDto.getUsedFlag() != null && shopCouponDto.getUsedFlag() == 0){
			shopCouponDto.setUserShopCouponStatus(CommonConst.USER_SHOP_COUPON_UNUSED.toString());
		}
		shopCouponDto.setUserShopCouponIdsList(getUserShopCouponStatusList(shopCouponDto.getUserShopCouponStatus()));
		PageModel pageModel =shopCouponService.getShopCouponList(shopCouponDto);
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("pNo", pageNo==null?1:pageNo);
		resultMap.put("rCount", pageModel.getTotalItem());
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询优惠券列表信息成功！",resultMap, DateUtils.DATE_FORMAT);
	}
	private List<Integer> getUserShopCouponStatusList(String userShopCouponStatus){
		List<Integer> userShopCouponStatusList = new ArrayList<Integer>();
		if(StringUtils.isNotBlank(userShopCouponStatus)){
			String[] userShopCouponStatusArr = userShopCouponStatus.split(",");
			for (String status : userShopCouponStatusArr) {
				userShopCouponStatusList.add(NumberUtil.stringToInteger(status));
			}
		}
		
		return userShopCouponStatusList;
	}
	/**
	 * PSC3：查询优惠券详细信息接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/shopCoupon/shopCouponDetail",
			"/service/shopCoupon/shopCouponDetail",
			"/session/shopCoupon/shopCouponDetail" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object shopCouponDetail(HttpServletRequest request) throws Exception {
		
		ShopCouponDto shopCouponDto = getRequestModel(request, ShopCouponDto.class);
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String userShopCouponId = RequestUtils.getQueryParam(request, "userShopCouponId");

		if(shopCouponDto == null ||(shopCouponDto.getShopCouponId() == null)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_SHOPCOUPONID);
		}
		if(shopCouponDto!=null && shopCouponDto.getIsDelete()==null){
			shopCouponDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		}
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		if(userShopCouponId!=null){
			CommonValidUtil.validStrLongFmt(userShopCouponId, CodeConst.CODE_PARAMETER_NOT_VALID, "优惠优惠券id格式错误");
		}
		Map<String,Object> resultMap =shopCouponService.shopCouponDetail(shopCouponDto);
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询优惠券详细信息接口成功！",resultMap, DateUtils.DATE_FORMAT);
	}
	
	/**
	 * PSC4：操作优惠券信息接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/shopCoupon/operateShopCoupon",
			"/service/shopCoupon/operateShopCoupon",
			"/session/shopCoupon/operateShopCoupon" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object operateShopCoupon(HttpServletRequest request) throws Exception {
		logger.info("PSC4： 操作优惠券信息接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		//操作等级 Id,多个以逗号分隔
		String shopCouponIds = RequestUtils.getQueryParam(request, "shopCouponIds");
		//操作类型： 1=删除
		String operateType = RequestUtils.getQueryParam(request, "operateType");
//		String token = RequestUtils.getQueryParam(request, "token");
//		if(token != null){
//			CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
//			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
//			collectService.queryShopAndTokenExists(Long.valueOf(shopId), token);
//		}
		CommonValidUtil.validStrNull(shopCouponIds, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOPCOUPONID_NOT_NULL);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		CommonValidUtil.validStrNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_OPERATETYPE_NOT_NULL);
		CommonValidUtil.validStrIntFmt(operateType, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_OPERATETYPE_NOT_NULL);
		if(Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_DELETE 
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_STOP
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_ENABLE){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		}
		shopCouponService.operateShopCoupon(shopId,shopCouponIds,operateType);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作优惠券信息接口成功！",null);
	}


	
	@RequestMapping(value={"/token/shopCoupon/getCouponDeductAmount",
					"/service/shopCoupon/getCouponDeductAmount",
					"/session/shopCoupon/getCouponDeductAmount"},
					method=RequestMethod.POST,
					consumes="application/json",
					produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCouponDeductAmount(HttpServletRequest request) throws Exception{
		logger.info("PSC7 计算优惠券可抵扣金额接口-start");
		RequstCouponDeductModel requestModel = getRequestModel(request, RequstCouponDeductModel.class);
		checkCouponDeductRequestValid(requestModel);
		Map<String, Object> resultMap = shopCouponService.getCouponDeductAmount(requestModel);
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "计算优惠券可抵扣金额成功",resultMap, DateUtils.DATETIME_FORMAT); 
	}
	
	private void checkCouponDeductRequestValid(RequstCouponDeductModel requestModel) throws Exception {
		Integer shopMemberId = requestModel.getShopMemberId();
		String mobile = requestModel.getMobile();
		Long shopId = requestModel.getShopId();
		Double payAmount = requestModel.getPayAmount();
		
		if (payAmount == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "payAmount不能为空");
		}
		
		if (payAmount <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付金额需大于零");
		}
		
		if (shopMemberId == null &&mobile == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopMemberId/mobile至少填一个");
		}
		
		if (shopMemberId != null) {
			ShopMemberDto shopMember = shopMemberServcie.getShopMemberDetail(shopMemberId.longValue());
			if (shopMember == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "店铺会员不存在");
			}
		}
		
		if (mobile != null) {
			if (shopId == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "填写mobile时需填写shopId");
			}
			
			int flag = this.shopServcie.queryShopExists(shopId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            
            CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, "手机号格式错误");
		}
		
/*		if (requestModel.getGoodsCategoryInfo() != null) {
			
			Double categoryDeductAmount = Double.valueOf(0);
			
			for (GoodsCategoryInfo goodsCategory : requestModel.getGoodsCategoryInfo()) {
				categoryDeductAmount += NumberUtil.add(categoryDeductAmount,goodsCategory.getGoodsCategoryAmount());
			}
			
			if (!categoryDeductAmount.equals(payAmount)) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商品分类支付金额与总支付金额不符");
			}
		}*/
		
	}

}
