package com.idcq.appserver.controller.discount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.discount.IShopTimeDiscountService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 商铺限时折扣
 * @author nie_jq
 *
 */
@Controller
@RequestMapping(value="/coupon")
public class ShopTimeDiscountController {
	private Log logger = LogFactory.getLog(getClass());
	@Autowired
	private IShopTimeDiscountService shopTimeDiscountService;
	/**
	 * 获取商铺的限时折扣
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShopTimedDiscount",produces="application/json;charset=utf-8")
	@ResponseBody
	public Object getShopTimedDiscount(HttpServletRequest request){
		try {
			logger.info("获取商铺的限时折扣列表-start");
			long start = System.currentTimeMillis();
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			//商铺编号非空验证
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			//商铺编号格式验证
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pNo", pNo);
			params.put("pSize", pSize);
			params.put("startSize", (pNo-1)*pSize);
			params.put("shopId", shopId);
			/**
			//查询条件
			String week = RequestUtils.getQueryParam(request, "week");//星期几
			//格式HH:mm:ss
			String dayFromTime = CommonValidUtil.validTimeFormat(RequestUtils.getQueryParam(request, "dayFromTime"), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_TIMED_DISCOUNT_52501);//每天开始时间
			String dayToTime = CommonValidUtil.validTimeFormat(RequestUtils.getQueryParam(request, "dayToTime"), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_TIMED_DISCOUNT_52502);//每天结束时间
			//格式 HH:mm:ss
			String weekFromTime = CommonValidUtil.validTimeFormat(RequestUtils.getQueryParam(request, "weekFromTime"),CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_TIMED_DISCOUNT_52503);//每周限定日的开始时间
			String weekToTime = CommonValidUtil.validTimeFormat(RequestUtils.getQueryParam(request, "weekToTime"),CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_TIMED_DISCOUNT_52504);//每周限定日的结束时间
			//格式yyyy-MM-dd HH:mm:ss/yyyy-MM-dd
			String customFromTime = CommonValidUtil.validDateTimeFormat(RequestUtils.getQueryParam(request, "customFromTime"),CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_TIMED_DISCOUNT_52505);//自定义的开始时间
			String customToTime = CommonValidUtil.validDateTimeFormat(RequestUtils.getQueryParam(request, "customToTime"),CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_TIMED_DISCOUNT_52506);//自定义的结束时间
			params.put("dayFromTime", dayFromTime);
			params.put("dayToTime", dayToTime);
			params.put("week", week);
			params.put("weekFromTime", weekFromTime);
			params.put("weekToTime", weekToTime);
			params.put("customFromTime", customFromTime);
			params.put("customToTime", customToTime);
			*/
			Map pModel = shopTimeDiscountService.getShopTimedDiscount(params);
			logger.info("共耗时："+(System.currentTimeMillis()-start));
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取限时折扣信息成功", pModel,DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e){
			logger.error("获取商铺限时折扣异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取商铺限时折扣异常",e);
			throw new APISystemException("获取商铺限时折扣异常", e);
		}
	}
	
	
	/**
	 * 获取限时折扣对应的商品列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShopTimedDiscountGoods",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getShopTimedDiscountGoods(HttpServletRequest request){
		try {
			logger.info("获取限时折扣对应的商品列表-start");
			String discountId = RequestUtils.getQueryParam(request, "discountId");
			//非空校验
			CommonValidUtil.validStrNull(discountId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_TIMED_DISCOUNT_52601);
			//格式校验
			CommonValidUtil.validStrLongFormat(discountId, CodeConst.CODE_PARAMETER_NOT_VALID,  CodeConst.MSG_TIMED_DISCOUNT_52602);
			int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pNo", pNo);
			params.put("pSize", pSize);
			params.put("startSize", (pNo-1)*pSize);
			params.put("discountId", Integer.valueOf(discountId));
			Map pModel = shopTimeDiscountService.getShopTimedDiscountGoods(params);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取信息成功", pModel);
		} catch (ServiceException e){
			logger.error("获取限时折扣对应的商品列表异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取限时折扣对应的商品列表异常",e);
			throw new APISystemException("获取限时折扣对应的商品列表异常", e);
		}
	}
	
	/**
	 * 初始化限时折扣数据
	 * @return
	 */
	@RequestMapping(value="/initialTimedDiscountData")
	public String initialTimedDiscountData(){
		new Thread(){
			public void run() {
				Random r = new Random();
				String[] discountPeriodTypes = new String[]{"每天","每周","自定义"};
				String[] weeks = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期天"};
				int[] discountType = new int[]{0,1};
				for (int i = 0; i < 100; i++) {
					List<Map> timedLists = new ArrayList<Map>();
					for (int j = 0; j < 1000; j++) {
						Map map = new HashMap();
						map.put("shop_id", (r.nextInt(4999) + 1000000000));
						map.put("discount_type", discountType[r.nextInt(2)]);
						map.put("discount_name", "商铺限时折扣"+r.nextInt(10000));
						map.put("discount_status", discountType[r.nextInt(2)]);
						String discountPeriodType =discountPeriodTypes[r.nextInt(3)];
						map.put("discount_period_type", discountPeriodType);
						map.put("day_from_time", "08:00:00");
						map.put("day_to_time", "19:00:00");
						map.put("week", weeks[r.nextInt(7)]);
						map.put("week_from_time", "08:00:00");
						map.put("week_to_time", "19:00:00");
						map.put("custom_from_datetime", "2015-04-28 08:00:15");
						map.put("custom_to_datetime", "2015-04-28 20:00:15");
						map.put("apply_online_flag", discountType[r.nextInt(2)]);
						map.put("apply_offline_flag", discountType[r.nextInt(2)]);
						map.put("discount", "0."+(r.nextInt(8)+1));
						timedLists.add(map);
					}
					shopTimeDiscountService.insertTimedDiscountData(timedLists);
					timedLists = null;
				}
			}
			
		}.start();
		return "success";
	}
	
	/**
	 * 初始化限时折扣商品
	 * @return
	 */
	public String initialTimedGoodsData(){
		return "success";
	}
}
