package com.idcq.appserver.controller.cashcoupon;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.cashcoupon.ICashCouponService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;



@Controller
@RequestMapping("/coupon")
public class CashCouponController {
	
	private final Logger logger = Logger.getLogger(CashCouponController.class);
	
	@Autowired
	private ICashCouponService couponService;
	@Autowired
	private IMemberServcie memberService;
	
	@RequestMapping(value="/getTopCashCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getTopCashCoupon(HttpServletRequest request){
		try{
			logger.info("获取top代金券列表-start");
			String pNo = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
			String pSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
			String cityId = RequestUtils.getQueryParam(request, "cityId");
			
			CommonValidUtil.validStrNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CITYID);
			CommonValidUtil.validNumStr(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CITYID);
			PageModel page = couponService.getTopCashCouponList(Integer.valueOf(cityId),PageModel.handPageNo(pNo), 
					PageModel.handPageSize(pSize));
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(page.getToPage());
			msgDto.setpSize(page.getPageSize());
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取top代金券列表成功！", msgDto);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取top代金券列表-系统异常！",e);
			throw new APISystemException("获取top代金券列表-系统异常！", e);
		}
	}
	@RequestMapping(value="/getUserCashCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getUserCashCoupon(HttpServletRequest request){
		try{
			String pNo = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
			String pSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
			String userId = RequestUtils.getQueryParam(request, "userId");
			
			CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_USERID);
			PageModel page = couponService.getUserCashCouponList(Long.valueOf(userId), 
					PageModel.handPageNo(pNo), PageModel.handPageSize(pSize));
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(page.getToPage());
			msgDto.setpSize(page.getPageSize());
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取用户代金券列表成功！", msgDto,DateUtils.DATETIME_FORMAT)	;
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取用户代金券列表失败",e);
			throw new APISystemException("获取用户代金券列表失败", e);
		}
	}
	
	/**
	 * 获取商铺的代金券
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShopCashCoupon",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopCoupon(HttpServletRequest request){
		try{
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String queryType = RequestUtils.getQueryParam(request, "queryType");
			CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			CommonValidUtil.validNumStr(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId数据格式错误");
			Integer pageNo=PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pageSize=PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Long shopId=NumberUtil.strToLong(shopIdStr, "shopId");
			if(queryType==null){
				queryType="0";
			}
			CommonValidUtil.validNumStr(queryType, CodeConst.CODE_PARAMETER_NOT_VALID, "queryType数据格式错误");
			PageModel page = couponService.getShopCoupon(shopId, Integer.parseInt(queryType), pageNo, pageSize);
			
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(pageNo);
			msgDto.setpSize(pageSize);
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			
			//return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺的代金券列表成功！", msgDto);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺的代金券列表成功！", msgDto);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取商铺的代金券列表失败！",e);
			throw new APISystemException("获取商铺的代金券列表失败！", e);
		}
	}
	
	
	
}
