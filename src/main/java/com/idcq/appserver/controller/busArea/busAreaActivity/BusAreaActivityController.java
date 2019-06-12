
package com.idcq.appserver.controller.busArea.busAreaActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActShopTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActStatusEnum;
import com.idcq.appserver.common.enums.BusAreaOperateTypeEnum;
import com.idcq.appserver.common.enums.ProgramTypeEnum;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.activity.BusinessAreaConfigDto;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.appserver.utils.thread.ThreadPoolManager;

/**
 * 商圈活动
 * 
 * @author Administrator
 * 
 */
@Controller
public class BusAreaActivityController {
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IBusAreaActivityService busAreaActivityService;
	@Autowired
	private IShopServcie shopServcie;

	/**
	 * PBA1：获取商圈活动配置接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { 
			"/service/busAreaActivity/getDynamicConfig",
			"/token/busAreaActivity/getDynamicConfig",
			"/session/busAreaActivity/getDynamicConfig" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getDynamicConfig(HttpServletRequest request) {
		logger.info("获取商圈活动配置接口-start" + request.getQueryString());
		try {
			String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
			String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
			
			
			/**
			 * TODO
			 * 步骤一：获取商圈活动配置列表
			 * 
			 * bizId验证非空
			 * bizType=13，bizId为商圈活动ID，
			 * bizType=15或未传值，bizId为商圈活动类型ID
			 *
			 */
			
			
			/**
			 * 步骤二：没有可以抽取的功能
			 * 
			 */
			
			/**
			 * 步骤三：
			 * bizType=13，bizId为商圈活动ID，根据商圈活动ID以及业务主键类型=13这两个条件 查询商圈活动配置表
			 * bizType=15，bizId为商圈活动类型ID，根据商圈活动类型ID以及业务主键类型=15这两个条件 查询商圈活动类型配置表
			 * 
			 */
			 // bizId验证非空
			CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZIDSTR);
			CommonValidUtil.validStrLongFmt(bizIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BIZID);
			Integer bizId =Integer.valueOf(bizIdStr);
			int bizType=0;
			if(bizTypeStr!=null){
				CommonValidUtil.validStrLongFmt(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BIZTYPE);
				bizType =Integer.parseInt(bizTypeStr);
			}
			List<Map> mapList = new ArrayList<Map>();
			if(bizType==BizTypeEnum.BUSAREA_ACTIVITY.getValue()){//13
				//bizType=13，bizId为商圈活动ID，
				mapList = busAreaActivityService.getBusAreaConfig(bizId);
			}else if(bizType==BizTypeEnum.BUSAREA_ACTIVITY_TYPE.getValue() || bizType==0){
				//bizType=15或未传值，bizId为商圈活动类型ID
				mapList = busAreaActivityService.getDynamicConfig(bizId);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lst", mapList);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					"获取配置成功！", map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取商圈活动配置接口-系统异常", e);
			throw new APISystemException("获取商圈活动配置接口-系统异常", e);
		}
	}

	/**
	 * PBA2:新增/修改商圈活动接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/service/busAreaActivity/updateActivity",
			"/token/busAreaActivity/updateActivity",
			"/session/busAreaActivity/updateActivity" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateActivity(HttpEntity<String> entity,HttpServletRequest request) {
		logger.info("新增/修改商圈活动接口-start");
		try {
			final BusinessAreaActivityDto businessAreaActivityDto = (BusinessAreaActivityDto) JacksonUtil
					.postJsonToObj(entity, BusinessAreaActivityDto.class,
							DateUtils.DATE_FORMAT);
			/**
			 * 步骤一：参数有效性校验
			 * 注意必填字段及字段类型
			 */
			updateActivityValidParams(businessAreaActivityDto);
			Map<String, Object> map = new HashMap<String, Object>();
			final Long businessAreaActivityId = busAreaActivityService.updateActivity(businessAreaActivityDto);
			/**
			 * 步骤七：返回活动ID
			 * 
			 */
			map.put("businessAreaActivityId",businessAreaActivityId );
			/**
			 * 步骤八：通知可参与商铺
			 * 
			 */
			ThreadPoolManager pool=ThreadPoolManager.getInstance();
			pool.execute(new Runnable(){
				@Override
				public void run() {
					try {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("shopId", businessAreaActivityDto.getShopId());
						params.put("businessAreaActivityId", businessAreaActivityId);
						logger.info("开始通知商铺，执行插入点："+params);
						ProgramUtils.executeBeanByExecutePointCode("businessAreaPushShopMessagePoint", 1, params);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增/修改商圈活动接口！",map);
		} catch (ServiceException e) {
			logger.error("新增/修改商圈活动接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("新增/修改商圈活动接口-系统异常", e);
			e.printStackTrace();
			throw new APISystemException("新增/修改商圈活动接口-系统异常", e);
		}
	}
	
	private void updateActivityValidParams(BusinessAreaActivityDto businessAreaActivityDto) throws Exception{
		if (businessAreaActivityDto == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_PBA2_1);
		}
		if (businessAreaActivityDto.getActivityRules() == null || businessAreaActivityDto.getActivityRules().size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_PBA2_2);
		}
		if (null == businessAreaActivityDto.getShopId()) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_ID_NULL);
		}
		if (null == businessAreaActivityDto.getSignEndDate()) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_3);
		}
		if (null == businessAreaActivityDto.getBeginDate()) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_4);
		}
		//报名开始时间，如果不填，默认为活动发布时间
		if (null == businessAreaActivityDto.getSignBeginDate()) {
			
			businessAreaActivityDto.setSignBeginDate(DateUtils.getZeroTimeOfCurDate());
		}
		//报名开始时间不能大于报名截止时间
		int re = DateUtils.compareDate(businessAreaActivityDto.getSignEndDate(),businessAreaActivityDto.getSignBeginDate());
		if (re < 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PBA2_6);
		}
		if (null == businessAreaActivityDto.getEndDate()) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_7);
		}
		//活动结束时间不能小于活动开始时间
		re = DateUtils.compareDate(businessAreaActivityDto.getBeginDate(),businessAreaActivityDto.getEndDate());
		if (re > 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PBA2_8);
		}
		//报名截止日期必须大于活动开始日期
		if(businessAreaActivityDto.getSignEndDate() != null && businessAreaActivityDto.getBeginDate() != null){
			re = DateUtils.compareDate(businessAreaActivityDto.getSignEndDate(),businessAreaActivityDto.getBeginDate());
			if (re >= 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PBA2_15);
			}
		}
		
		for(BusinessAreaConfigDto dto : businessAreaActivityDto.getActivityRules()){
			if (null == dto.getConfigType()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_9);
			}
			if (null == dto.getConfigCode()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_10);
			}
			if (null == dto.getConfigValue()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PBA2_11);
			}
		}
		
		// 验证商铺存在性
		Long shopId = businessAreaActivityDto.getShopId();
		if (shopId != null) {
			ShopDto pShop = shopServcie.getShopById(shopId);
			CommonValidUtil
					.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_MISS_SHOP);
			
			businessAreaActivityDto.setShopCity(pShop.getCityId());
			businessAreaActivityDto.setShopDistrictId(pShop.getDistrictId());
			businessAreaActivityDto.setShopName(pShop.getShopName());
		}
		
		//校验商铺类型,必须为发起方
		Long businessAreaActivityId = businessAreaActivityDto.getBusinessAreaActivityId();
		if(businessAreaActivityId != null){
	        BusinessAreaShopDto busAreaShop = busAreaActivityService.getBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
	        if(busAreaShop != null){
	        	if(BusAreaActShopTypeEnum.START_SHOP.getValue() != busAreaShop.getShopType()){
					throw new ValidateException(CodeConst.CODE_PERMISSION_ERROR, CodeConst.MSG_PARAMETER_ERROR);
	        	}
	        	
	        }
		}
		//同一时间段内只能发布一个活动
		//通过商铺Id查询出所有的活动
		List<BusinessAreaActivityDto> businessAreaActivityDtoList =busAreaActivityService.getActivitiesByShopId(businessAreaActivityDto);
		for (BusinessAreaActivityDto businessAreaActivityDB : businessAreaActivityDtoList) {
			//报名开始时间不能大于报名截止时间
			//当前活动的结束时间只能小于数据库中已存在的活动的开始之间
			if(null != businessAreaActivityDto.getBeginDate()){
				int isTrue = DateUtils.compareDate(businessAreaActivityDto.getBeginDate(),businessAreaActivityDB.getEndDate());
				//当前活动的开始时间只能大于数据库中已存在的活动的开始之间
				int isTrue1 = DateUtils.compareDate(businessAreaActivityDB.getBeginDate(),businessAreaActivityDto.getEndDate());
				if(isTrue<=0 && isTrue1<=0){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_PBA2_14);
				}
			}
			
		}
		//非法关键字检测
		SensitiveWordsUtil.checkSensitiveWords("businessAreaName", businessAreaActivityDto.getBusinessAreaName());
		SensitiveWordsUtil.checkSensitiveWords("activityDesc", businessAreaActivityDto.getActDesc());

	}

	/**
	 * PBA3：获取商圈活动列表接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { 
			"/service/busAreaActivity/getActivityList",
			"/token/busAreaActivity/getActivityList",
			"/session/busAreaActivity/getActivityList" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getActivityList(HttpServletRequest request) {
		logger.info("获取商圈活动列表接口-start ?" + request.getQueryString());
		try {
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String shopTypeStr = RequestUtils.getQueryParam(request, "shopType");
			String cityIdStr = RequestUtils.getQueryParam(request, "cityId");
			String activityStatusStr = RequestUtils.getQueryParam(request,"activityStatus");
			String columnIdStr = RequestUtils.getQueryParam(request,"columnId");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");

			
			/**
			 * 步骤一：查询全部活动列表、我发布的活动列表或者我参与的活动列表
			 * 
			 * 步骤二：无
			 */
			
			
			/**
			 * 步骤三：
			 * 1、shopType不填写：查询同城所有活动；shopType=1：查询发起商铺（shopId是活动发起商家；shopType=2:查询参与商铺（shopId是参与的商铺））
			 * 2、查询同城时，shopId所代表的商铺的cityId必须与请求中的cityId一致
			 * 3、根据shopTypeStr的查询条件，查询出相应的活动，其中涉及到的表有商圈活动表（1dcq_business_area_activity）、 
			 * 商铺表（1dcq_shop）
			 */
			Long shopId=null;
			Integer shopType=null;
			Integer cityId=null;
			Integer activityStatus=null;
			Integer columnId=null;
			if(shopIdStr!=null){
				shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
				int flag = this.shopServcie.queryShopExists(Long.valueOf(shopId));
		        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
			}
			if(shopTypeStr!=null){
				CommonValidUtil.validStrIntFormat(shopTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPTYPE);
				shopType=Integer.valueOf(shopTypeStr);
			}
			if(cityIdStr!=null){
				CommonValidUtil.validStrIntFormat(cityIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CITYID);
				cityId=Integer.valueOf(cityIdStr);
			}
			
			if(activityStatusStr!=null){
				CommonValidUtil.validStrIntFormat(activityStatusStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ACTIVITYSTATUS);
				activityStatus=Integer.valueOf(activityStatusStr);
			}
			if(columnIdStr!=null){
				CommonValidUtil.validStrIntFormat(columnIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_COLUMNID);
				columnId=Integer.valueOf(columnIdStr);
			}
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("shopType", shopType);
			map.put("cityId", cityId);
			map.put("columnId", columnId);
			map.put("activityStatus", activityStatus);
			map.put("pageNo", (pNo - 1) * pSize);
			map.put("pageSize", pSize);
			//PageModel pageModel=busAreaActivityService.getActivityList1(map,isTokenOrSession);
		//	PageModel pageModel=busAreaActivityService.getActivityList(map,isTokenOrSession);
			PageModel pageModel = busAreaActivityService.getBusAreaActivityList(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,
					"获取商圈活动列表接口成功！", resultMap, DateUtils.DATE_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("获取商圈活动列表接口-系统异常", e);
			throw new APISystemException("获取商圈活动列表接口-系统异常", e);
		}
	}

	/**
	 * PBA4：查看商圈活动详情接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/service/busAreaActivity/getActivityDetail",
			"/token/busAreaActivity/getActivityDetail",
			"/session/busAreaActivity/getActivityDetail" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getActivityDetail(HttpServletRequest request) {
		logger.info("查看商圈活动详情接口-start" + request.getQueryString());
		try {
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String businessAreaActivityIdStr = RequestUtils.getQueryParam(
					request, "businessAreaActivityId");
			
			
			/**
			 * 步骤一：查询活动详情
			 */
			
			
			/**
			 * 步骤二：无
			 */
			
			
			/**
			 * 步骤三：
			 * 1、活动ID（businessAreaActivityIdStr）为必填项，校验
			 * 2、通过商铺ID以及活动ID查询商圈活动详情，其中涉及到的表：商圈活动表（1dcq_business_area_activity），
			 * 	   通过商圈活动ID查询1dcq_attachment_relation以及1dcq_attachment查询出海报的actPosterIds以及以及actPosterUrls
			 *    通过商圈活动店铺表（s1dcq_business_area_shop）且shop_type=2来查询参与活动店铺数
			 *    通过查询商圈活动效果统计表（1dcq_business_area_stat）查询阅读人数 ，分享人数
			 *    通过活动ID查询 商圈活动配置表 配置表+活动类型配置表+商圈活动类型模板表，查询出 activityModelConfigs对象数组
			 */
			Long shopId=null;
			if(shopIdStr!=null){
				shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			}
			CommonValidUtil.validStrNull(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZIDSTR);
			//验证businessAreaActivityIdStr为Integer
			CommonValidUtil.validStrIntFormat(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ACTIVITYID);
			Integer businessAreaActivityId = Integer.valueOf(businessAreaActivityIdStr);
			Map<String, Object> map = busAreaActivityService.getActivityDetail(shopId,businessAreaActivityId);
			
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,
					"查看商圈活动详情接口成功！", map, DateUtils.DATE_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("查看商圈活动详情接口-系统异常", e);
			throw new APISystemException("查看商圈活动详情接口-系统异常", e);
		}
	}

	/**
	 * PBA5：活动操作接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/service/busAreaActivity/operateActivity",
			"/token/busAreaActivity/operateActivity",
			"/session/busAreaActivity/operateActivity" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object operateActivity(HttpServletRequest request) {
		/**
		 * 步骤一：校验查询参数
		 * 
		 * 校验请求参数中的必填参数是否填写，校验请求参数的类型
		 * 
		 * 
		 * 对请求参数校验完后，需要根据操作类型对要操作的活动进行异常判
		 * 操作类型分为：1=参与活动，2=参与店铺取消参与，3=发起方取消活动，4发布活动
		 * 操作类型为发布活动的异常校验
		 * 1：是否已经发动该活动
		 * 其他操作类型
		 * 1:需要判断该活动是否存在
		 * 2：活动是否已过有效期
		 * 3：活动是否已被取消
		 * 4：是否已经结束
		 * @return
		 */
		
		/**
		 * 步骤二：根据操作类型，执行相应的活动操作
		 * 
		 * 操作类型分为：1=参与活动，2=参与店铺取消参与，3=发起方取消活动，4发布活动
		 * 
		 * 发布活动：
		 * 
		 * 参与活动：
		 * 
		 * 数据库：1dcq_business_area_activity
	     * 	    1dcq_business_area_shop
	     * 		
		 * @return
		 */
		logger.info("活动操作接口-start" + request.getQueryString());
		try {
			Map<String, Object> requestMap = checkParamForOperateActivity(request);
			busAreaActivityService.operateActivity(requestMap);
			Integer operateType = Integer.valueOf(requestMap.get("operateType").toString());
			String resultMsg = operateType == 1 ? "参与活动成功" : operateType == 2 ? "取消报名成功" : "取消活动成功";
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, resultMsg,
					null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("活动操作接口-系统异常", e);
			throw new APISystemException("活动操作接口-系统异常", e);
		}
	}

	private Map<String, Object> checkParamForOperateActivity(HttpServletRequest request) throws Exception {
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String businessAreaActivityIdStr = RequestUtils.getQueryParam(
				request, "businessAreaActivityId");
		String operateTypeStr = RequestUtils.getQueryParam(request,
				"operateType");
		
		Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
				CodeConst.CODE_PARAMETER_NOT_VALID,"店铺Id类型错误");
	
		int flag = this.shopServcie.queryShopExists(Long.valueOf(shopId));
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
        
        requestMap.put("shopId", shopId);
        
        Integer operateType = 0;
        if (operateTypeStr != null) {
        	operateType =  CommonValidUtil.validStrIntFmt(operateTypeStr, 
													CodeConst.CODE_PARAMETER_NOT_EXIST,
													"操作类型错误");
        	requestMap.put("operateType",operateType);
        }else {
        	operateType =  BusAreaOperateTypeEnum.JOIN_ACTIVITY.getIndex();
        	requestMap.put("operateType", operateType);
        }
        
        CommonValidUtil.validObjectNull(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "活动Id不能为空");
        
        Long businessAreaActivityId = CommonValidUtil.validStrLongFmt(businessAreaActivityIdStr, 
        															CodeConst.CODE_PARAMETER_NOT_VALID, 
        															"活动Id类型错误");
        
        BusinessAreaActivityDto activity = busAreaActivityService.getBusinessAreaActivityById(businessAreaActivityId);
        BusinessAreaShopDto busAreaShop = busAreaActivityService.getBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
        Date nowTime = new Date();
       
        if (activity == null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺活动不存在");
        }
        
        Date endDate = activity.getEndDate();
        Date signEndDate = activity.getSignEndDate();
		Calendar cal = Calendar.getInstance();  
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		activity.setEndDate(cal.getTime());
		
		cal.setTime(signEndDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		activity.setSignEndDate(cal.getTime());
		
    	if(activity.getActivityStatus().equals(BusAreaActStatusEnum.STOP.getValue())
    			|| activity.getEndDate().before(nowTime)) {
    		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺活动以结束");
    	}
    	
        if (operateType.equals(BusAreaOperateTypeEnum.JOIN_ACTIVITY.getIndex())) {
        	if (busAreaShop != null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "该商铺已经参加活动");
        	}
        	if (!activity.getActivityStatus().equals(BusAreaActStatusEnum.APPLYING.getValue()) 
        			|| activity.getSignEndDate().before(nowTime)) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺活动结束报名");
        	}
        }else if (operateType.equals(BusAreaOperateTypeEnum.CANCEL_BY_JOINER.getIndex())) {
        	if (busAreaShop == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "该商铺没有参加次活动无法取消");
        	}
        	if(activity.getActivityStatus().equals(BusAreaActStatusEnum.RUNNING.getValue()) 
        			|| activity.getSignEndDate().before(nowTime)) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺活动正在进行不能取消");
        	}
        }else if (operateType.equals(BusAreaOperateTypeEnum.CANCEL_BY_ORIGINATE.getIndex())) {
        	
        	if (!shopId.equals(activity.getShopId())) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "该商铺不是发起活动的商铺");
        	}
        	if (busAreaShop == null) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "该商铺活动已经取消");
        	}
        	if (activity.getActivityStatus().equals(BusAreaActStatusEnum.RUNNING.getValue()) 
        			|| activity.getBeginDate().before(nowTime)) {
        		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺活动正在进行不能取消");
        	}
        	
        	if (activity.getActNum().intValue() != 0) {
        		if (!(activity.getActNum().intValue() == 1 && shopId.equals(activity.getShopId()))) {
        			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "已经有商铺参加不能取消");
        		}
        	}
        }
        
        requestMap.put("businessAreaActivityId", businessAreaActivityId);
		return requestMap;
	}
	/**
	 * PBA6：获取商圈活动的参与商家列表接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/service/busAreaActivity/getActivityShopList",
			"/token/busAreaActivity/getActivityShopList",
			"/session/busAreaActivity/getActivityShopList" }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getActivityShopList(HttpServletRequest request) {
		logger.info("获取商圈活动的参与商家列表接口-start" + request.getQueryString());
		try {
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String businessAreaActivityIdStr = RequestUtils.getQueryParam(
					request, "businessAreaActivityId");
			String shopTypeStr = RequestUtils
					.getQueryParam(request, "shopType");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			
			
			/**
			 * 步骤一：查询参与商圈活动的商家列表。
			 */
			
			
			/**
			 * 步骤二：无
			 */
			
			/**
			 * 步骤三：
			 * 
			 * 1、如果鉴权token不为空，在shopId为必填
			 * 2、 商圈活动ID（businessAreaActivityId）验证必填
			 * 3、涉及到的表：商圈活动店铺表（1dcq_business_area_shop）+店铺表（1dcq_shop）+
			 *    通过商圈活动ID查询1dcq_attachment_relation以及1dcq_attachment查询商铺logo的路径（shopLogoUrl）
			 */
			
			Long shopId=null;
			if(shopIdStr!=null){
				shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			}
			CommonValidUtil.validStrNull(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BIZIDSTR);
			//验证businessAreaActivityIdStr为Integer
			CommonValidUtil.validStrIntFormat(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ACTIVITYID);
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("businessAreaActivityId", businessAreaActivityIdStr);
			map.put("shopType", shopTypeStr);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			PageModel pageModel=busAreaActivityService.getActivityShopList(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,
					"获取商圈活动的参与商家列表接口成功！", resultMap, DateUtils.DATE_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取商圈活动的参与商家列表接口-系统异常", e);
			throw new APISystemException("获取商圈活动的参与商家列表接口-系统异常", e);
		}
	}
	
	/**
	 * PBA7：取得参与活动资格接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/service/busAreaActivity/applyBusinessArea",
			"/token/busAreaActivity/applyBusinessArea",
			"/session/busAreaActivity/applyBusinessArea"}, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object applyBusinessArea(HttpServletRequest request) {
		logger.info("获取商圈活动资格接口-start" + request.getQueryString());
		try {
			Map<String,Object> paramsMap = RequestUtils.getRequestObjectMapWithPrefix(request, "");
			/**
			 * 步骤一：接口入参合法性校验
			 * 
			 * 校验参数必填、枚举、数据存在性
			 */
			
//			int flag = busAreaActivityService.applyBusinessArea(busAreaActId,userSourceType,userSourceId,userSourceChannel,mobileStr);
//			int flag = busAreaActivityService.applyBusinessArea(paramsMap);
    		/**
    		 * 步骤二：根据程序配置找到并执行具体的业务bean
    		 * 
    		 * 注册成平台会员、店内会员、生成活动资格记录
    		 */
    		
//			int flag = (Integer)ProgramUtils.executeBeanByProgramConfigCode("test2",12,paramsMap);
			String msg = (String)ProgramUtils.executeBeanByExecutePointCode("businessAreaApplyBusinessAreaPoint", 
					ProgramTypeEnum.APPLY_BUSAREA.getValue(), paramsMap);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,msg, null);
		} catch (ServiceException e) {
			logger.error("获取商圈活动资格接口异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取商圈活动资格接口--系统异常", e);
			throw new APISystemException("获取商圈活动资格接口--系统异常", e);
		}
	}
	
	/**
	 * PBA8:判断发布修改商圈活动的有效性
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/service/busAreaActivity/validBusinessActivity",
			"/token/busAreaActivity/validBusinessActivity",
			"/session/busAreaActivity/validBusinessActivity" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object validBusinessActivity(HttpEntity<String> entity,HttpServletRequest request) {
		logger.info("判断发布修改商圈活动的有效性接口-start");
		try {
			final BusinessAreaActivityDto businessAreaActivityDto = (BusinessAreaActivityDto) JacksonUtil
					.postJsonToObj(entity, BusinessAreaActivityDto.class,
							DateUtils.DATE_FORMAT);
			/**
			 * 步骤一：参数有效性校验
			 * 注意必填字段及字段类型
			 */
			updateActivityValidParams(businessAreaActivityDto);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "判断发布修改商圈活动的有效性接口！",null);
		} catch (ServiceException e) {
			logger.error("判断发布修改商圈活动的有效性接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("判断发布修改商圈活动的有效性接口-系统异常", e);
			e.printStackTrace();
			throw new APISystemException("判断发布修改商圈活动的有效性接口-系统异常", e);
		}
	}
	
//	/**
//	 * PBA7：取得参与活动资格接口
//	 * 
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = { "/busAreaActivity/applyBusinessArea",
//			"/service/busAreaActivity/applyBusinessArea",
//			"/token/busAreaActivity/applyBusinessArea",
//	"/session/busAreaActivity/applyBusinessArea" }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object applyBusinessArea(HttpServletRequest request) {
//		logger.info("获取商圈活动资格接口-start" + request.getQueryString());
//		try {
//			String busAreaActIdStr = RequestUtils.getQueryParam(request, "businessAreaActivityId");
//			String mobileStr = RequestUtils.getQueryParam(request, "mobile");
//			String veriCodeStr = RequestUtils.getQueryParam(request, "veriCode");
//			String userSourceTypeStr = RequestUtils.getQueryParam(request, "userSourceType");
//			String userSourceIdStr = RequestUtils.getQueryParam(request, "userSourceId");
//			String userSourceChannelStr = RequestUtils.getQueryParam(request, "userSourceChannel");
//			
//			
//			/**
//			 * 步骤一：接口入参合法性校验
//			 */
//			
//			/*校验活动ID必填及存在性*/
//			CommonValidUtil.validStrNull(busAreaActIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BUSAREA_ACT_ID);
//			CommonValidUtil.validStrLongFmt(busAreaActIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BUSAREA_ACT_ID);
//			long busAreaActId = Long.valueOf(busAreaActIdStr);
//			/*校验手机号码必填及格式合法性*/
//			CommonValidUtil.validStrNull(mobileStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
//			CommonValidUtil.validMobileStr(mobileStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_MOBILE);
//			/*校验userSourceType是否有值及枚举*/
//			int userSourceType;
//			if(!StringUtils.isBlank(userSourceTypeStr)){
//				CommonValidUtil.validStrIntFmt(userSourceTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_TYPE);
//				userSourceType = Integer.valueOf(userSourceTypeStr);
//				BusAreaActUserSourceTypeEnum.getNameByValue(userSourceType);
//			}else{
//				userSourceType = BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue();
//			}
//			/*根据userSourceType校验短信验证码*/
//			if(userSourceType == BusAreaActUserSourceTypeEnum.OTHERS.getValue()){
//				//用户来源未知，需要短信验证
//				CommonValidUtil.validStrNull(veriCodeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERICODE);
//				//验证短信验证码
//				
//			}
//			/*校验getSourceType是否有值及枚举*/
//			Long userSourceId = null;
//			if(userSourceType == BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue()){
//				//商铺导入，用户来源ID必填
//				CommonValidUtil.validStrNull(userSourceIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_SOURCE_ID);
//				CommonValidUtil.validStrLongFmt(userSourceIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_ID);
//				userSourceId = Long.valueOf(userSourceIdStr);
//			}
//			Integer userSourceChannel = null;
//			if(!StringUtils.isBlank(userSourceChannelStr)){
//				CommonValidUtil.validStrIntFmt(userSourceChannelStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_CHANNEL);
//				userSourceChannel = Integer.valueOf(userSourceChannelStr);
//			}
//			
//			/**
//			 * 步骤二：调用服务层接口生成用户活动资格数据
//			 * 
//			 * 注册成平台会员、店内会员、生成活动资格记录
//			 */
//			
//			int flag = busAreaActivityService.applyBusinessArea(busAreaActId,userSourceType,userSourceId,userSourceChannel,mobileStr);
//			String msg = (flag == 1) ? "获取商圈活动资格成功！" : "该手机号码已经获取商圈活动资格！";
//			
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,msg, null);
//		} catch (ServiceException e) {
//			logger.error("获取商圈活动资格接口异常",e);
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			this.logger.error("获取商圈活动资格接口--系统异常", e);
//			throw new APISystemException("获取商圈活动资格接口--系统异常", e);
//		}
//	}
	

}
