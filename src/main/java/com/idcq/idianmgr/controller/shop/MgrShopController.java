package com.idcq.idianmgr.controller.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.util.Hash;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shop.IShopConfigureSettingService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.ArrayUtil;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.IdcardValidator;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.idianmgr.dto.shop.CategoryDto;
import com.idcq.idianmgr.dto.shop.ShopBean;
import com.idcq.idianmgr.dto.shop.ShopBookSettingDto;
import com.idcq.idianmgr.dto.shop.ShopCashierParams;
import com.idcq.idianmgr.dto.shop.TechTypeBean;
import com.idcq.idianmgr.dto.shop.TechTypeDto;
import com.idcq.idianmgr.dto.shop.TempCateGoryDto;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;
import com.idcq.idianmgr.service.shop.IMgrShopService;

@Controller
public class MgrShopController {
	private static final Logger logger = Logger.getLogger(MgrShopController.class);
	
	@Autowired
	public IMgrShopService mgrShopService;
	@Autowired
	public IGoodsGroupService goodsGroupService;
	
	@Autowired
	public IGoodsServcie goodsService;
	@Autowired
	public IShopServcie shopServcie;
	@Autowired
    private IShopConfigureSettingService shopSettingService;
	@Autowired
	private ICommonService commonService;
	@Autowired
    private IMemberServcie memberService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IUserDao userService;
	/**
	 * 设置店铺预约设置
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/shopManage/setShopBookSetting", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8",consumes = "application/json")
	@ResponseBody
	public Object setShopBookSetting(HttpEntity<String> entity) {
		try {
			logger.info("设置店铺预约设置-start");
			ShopBookSettingDto bkSetting = (ShopBookSettingDto) JacksonUtil.postJsonToObj(entity,ShopBookSettingDto.class);
			this.mgrShopService.setShopBookSetting(bkSetting);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "设置预约设置信息成功！", null);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置店铺预约设置-系统异常", e);
			throw new APISystemException("设置店铺预约设置-系统异常", e);
		}
	}
	
	/**
	 * 添加/修改服务分类接口
	 * 
	 * @version:v1.0
	 * @author:huangrui
	 * @date:2015年7月30日 下午2:15:01
	 * 
	 * * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2016年03月203日    nie_jq                    修改入口URL，支持收银机调用
	 * 
	 */
	@RequestMapping(value = {"/shopManage/addGoodsCategory","/token/shop/addGoodsCategory","/token/shopManage/addGoodsCategory"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addGoodsCategory(HttpEntity<String> entity) {
		try {
			logger.info("添加/修改服务分类接口-start");
			CategoryDto categoryDto = (CategoryDto) JacksonUtil.postJsonToObj(
					entity, CategoryDto.class);

			// 分类名必填
			CommonValidUtil.validStrNull(categoryDto.getCategoryName(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "分类名不能为空");
			SensitiveWordsUtil.checkSensitiveWords("categoryName", categoryDto.getCategoryName());
			
			//分类名不能超过8个字
			CommonValidUtil.validStrLength(categoryDto.getCategoryName(), 8, 
					CodeConst.CODE_PARAMETER_TOOLENGTH, "分类名称不能超过8个字");
			
			// 商品ID必填
			CommonValidUtil.validStrNull(categoryDto.getShopId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			if(!categoryDto.isAdd()){
				// 分类Id必填
				CommonValidUtil.validLongNull(categoryDto.getCategoryId(),
						CodeConst.CODE_PARAMETER_NOT_NULL, "分类Id不能为空");
			}
			
			String categoryId = mgrShopService.operateCategory(categoryDto);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("goodsCategoryId", categoryId);
			DataCacheApi.del(CommonConst.KEY_GOODSCATEGORY+categoryId);
			logger.info("添加/修改服务分类接口-end");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作服务分类成功", data);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加/修改服务分类接口-系统异常", e);
			throw new APISystemException("添加/修改服务分类接口-系统异常", e);
		}
	}
	
	
	
	/**
	 * MS9：删除服务分类接口
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"/shopManage/operateGoodsCategory","/token/shopManage/operateGoodsCategory"}, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object operateGoodsCategory(HttpServletRequest request) {
		try {
			logger.info("删除服务分类接口-start");
			CategoryDto categoryDto =new CategoryDto();
			
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String categoryIds = RequestUtils.getQueryParam(request, "categoryIds");
			String operateType = RequestUtils.getQueryParam(request, "operateType");
			String stopDate = RequestUtils.getQueryParam(request, "stopDate");
			categoryDto.setShopId(shopId);
			categoryDto.setCategoryIds(categoryIds);
			if(StringUtils.isNotEmpty(operateType))
				categoryDto.setOperateType(Integer.parseInt(operateType));
			categoryDto.setStopDate(stopDate);

			// 分类Id必填
			CommonValidUtil.validStrNull(categoryDto.getCategoryIds(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "分类ID不能为空");
			// 商铺ID必填
			CommonValidUtil.validStrNull(categoryDto.getShopId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			mgrShopService.delCategory(categoryDto);
			
			
			
			logger.info("删除服务分类接口-end");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除服务分类成功",
					null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除服务分类接口-系统异常", e);
			throw new APISystemException("删除服务分类接口-系统异常", e);
		}
	}
	
	/**
	 * MS14：查询场地分类定价接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/shopManage/getShopPreResourceSetting")
	@ResponseBody
	public ResultDto getShopPreResourceSetting(HttpServletRequest request){
		long startTime = System.currentTimeMillis();
		try {
			logger.info("==================>>查询商品族内商品价格-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String categoryIdStr = RequestUtils.getQueryParam(request, "categoryId");
			//商铺编号参数校验
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			//分类编号参数校验
			CommonValidUtil.validStrNull(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CATEGORY_ID);
			Long categoryId = CommonValidUtil.validStrLongFmt(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CATEGORY_ID);
			List<Map> list = goodsGroupService.getShopPreResourceSetting(shopId,categoryId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取信息成功", list);
		} catch (ServiceException e) {
			logger.error("查询场地分类定价接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询场地分类定价接口-系统异常", e);
			throw new APISystemException("查询场地分类定价接口-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	/**
	 * MS15：场地分类定价接口
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/shopManage/operatePreResource",method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResultDto operatePreResource(HttpServletRequest request,HttpEntity<String> entity){
		long startTime = System.currentTimeMillis();
		try {
			logger.info("==================>>场地分类定价接口-start");
			TempCateGoryDto tempCateGoryDto = (TempCateGoryDto) JacksonUtil.postJsonToObj(entity,TempCateGoryDto.class);
			if (tempCateGoryDto != null && tempCateGoryDto.getResources() != null && tempCateGoryDto.getResources().size() > 0) {
				goodsGroupService.operatePreResource(tempCateGoryDto);
			}else{
				throw new ValidateException(CodeConst.CODE_JSON_ERROR, "上传参数有误");
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增预定资源成功", null);
		} catch (ServiceException e) {
			logger.error("场地分类定价接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("场地分类定价接口-系统异常", e);
			throw new APISystemException("场地分类定价接口-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * MS10：添加/修改技师级别接口
	 * @param entity
	 * @return
	 */
	@RequestMapping(value={"/updateTechType","/token/shop/updateTechType"}, method = RequestMethod.POST, consumes = "application/json",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateTechType(HttpEntity<String> entity) {
		try {
			logger.info("添加/修改技师级别接口-start");
			TechTypeBean techType= (TechTypeBean) JacksonUtil.postJsonToObj(
					entity, TechTypeBean.class);

			// 技师级别名必填
			CommonValidUtil.validStrNull(techType.getTechTypeName(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "技师级别名不能为空");
			// 商铺ID必填
			CommonValidUtil.validStrNull(techType.getShopId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			// 操作必填
			CommonValidUtil.validIntNull(techType.getOperateType(),
								CodeConst.CODE_PARAMETER_NOT_NULL, "操作类型不能为空");
			TechTypeDto techTypeDto = new TechTypeDto();
			BeanUtils.copyProperties(techTypeDto, techType);
			
			// 修改操作，techTypeId必传
			if(!techTypeDto.isAdd()){
				CommonValidUtil.validStrNull(techTypeDto.getTechTypeId(),
						CodeConst.CODE_PARAMETER_NOT_NULL, "techTypeId不能为空");
			}
			String techTypeId =mgrShopService.updateTechType(techTypeDto);
			logger.info("添加/修改技师级别接口-end");
			Map<String, Object> map = new HashMap<>();
			map.put("techTypeId", techTypeId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作技师级别成功",
					map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加/修改技师级别接口-系统异常", e);
			throw new APISystemException("添加/修改技师级别接口-系统异常", e);
		}
	}
	
	/**
	 * MS12：删除技师级别接口
	 * @param entity
	 * @return
	 */
	@RequestMapping(value={"/delTechType","/token/shop/delTechType"},  method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object delTechType(HttpServletRequest request) {
		try {
			logger.info("删除技师级别接口-start");
			TechTypeDto techTypeDto = new TechTypeDto();
			
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String techTypeIds = RequestUtils.getQueryParam(request, "techTypeIds");
			String operateType = RequestUtils.getQueryParam(request, "operateType");
			
			techTypeDto.setShopId(shopId);
			techTypeDto.setTechTypeIds(techTypeIds);
			if(StringUtils.isNotBlank(operateType))
				techTypeDto.setOperateType(Integer.parseInt(operateType));

			// 技师级别ID必填
			CommonValidUtil.validStrNull(techTypeDto.getTechTypeIds(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "技师级别ID不能为空");
			// 商铺ID必填
			CommonValidUtil.validStrNull(techTypeDto.getShopId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			mgrShopService.delTechType(techTypeDto);
			
			logger.info("删除技师级别接口-end");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除技师级别成功",
					null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除技师级别接口-系统异常", e);
			throw new APISystemException("删除技师级别接口-系统异常", e);
		}
	}
	
	/**
	 * MS11：获取商铺技师级别接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/getShopTechGrade","/token/shop/getShopTechGrade"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getMyOrders(HttpServletRequest request) {
		try {
			logger.info("分页获取商铺技师级别接口-start");
			String pageNO = RequestUtils.getQueryParam(request,CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request,CommonConst.PAGE_SIZE);
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String techTypeId = RequestUtils.getQueryParam(request, "techTypeId");
			// 检验shopId是否为空
			CommonValidUtil.validStrNull(shopId,CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			TechTypeDto techTypeDto = new TechTypeDto();
			techTypeDto.setShopId(shopId);
			techTypeDto.setParentTechTypeId(techTypeId);
			PageModel pageModel = mgrShopService.getTechTypes(techTypeDto,
					PageModel.handPageNo(pageNO),
					PageModel.handPageSize(pageSize));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pNo", pageModel.getToPage());
			map.put("rCount", pageModel.getTotalItem());
			map.put("lst", pageModel.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"获取技师类别成功！", map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺技师级别接口-系统异常", e);
			throw new APISystemException("获取商铺技师级别接口-系统异常", e);
		}
	}
	
	/**
	 * MS17：获取商铺分类接口(场地类)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/shopManage/getShopFullCategory")
	@ResponseBody
	public ResultDto getShopFullCategory(HttpServletRequest request){
		try {
			logger.info("获取商铺分类接口(场地类)-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			CommonValidUtil.validNumStr(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,"shopId参数不合法");
			Integer pageNO=PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pageSize=PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			Long shopId=NumberUtil.strToLong(shopIdStr, "shopId");
			PageModel pageModel= goodsService.getShopFullCategory(shopId,pageNO,pageSize);
			
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			dataList.add(msgList);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取分类成功", msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺分类接口(场地类)-系统异常", e);
			throw new APISystemException("获取商铺分类接口(场地类)-系统异常", e);
		}
	}
	
	/**
	 * MS38：验证同一账户下的店铺是否同名
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shopManage/isExistSameShopName", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto isExistSameShopName(HttpServletRequest request){
		try {
			logger.info("验证同一账户下的店铺是否同名接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String shopName = RequestUtils.getQueryParam(request, "shopName");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			if(shopIdStr != null){
				CommonValidUtil.validNumStr(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			}
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_USER_ID);
			CommonValidUtil.validNumStr(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validObjectNull(shopName, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_SHOPID);
			boolean isExist = mgrShopService.isExistSameShopName(shopIdStr,userIdStr,shopName);
			Map<String,Object> resultMap = new HashMap<String, Object>();
			resultMap.put("isExist", isExist?1:2);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "验证成功", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("验证同一账户下的店铺是否同名接口-系统异常", e);
			throw new APISystemException("验证同一账户下的店铺是否同名接口-系统异常", e);
		}
	}
	
	/**
	 * MS19：新增商铺接口
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/shopManage/addShopDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addShopDetail(HttpEntity<String> entity) {
		try {
			logger.info("新增商铺接口-start");
			ShopBean shopBean= (ShopBean) JacksonUtil.postJsonToObj(
					entity, ShopBean.class);

			// 店名必填
			CommonValidUtil.validStrNull(shopBean.getShopName(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "店名不能为空");
			// 会员ID必填
			CommonValidUtil.validLongNull(shopBean.getUserId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "会员ID不能为空");
			// 店主名必填
			CommonValidUtil.validStrNull(shopBean.getShopkeeper(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "店主姓名不能为空");
			// 省份必填
			CommonValidUtil.validIntNoNull(shopBean.getProvinceId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "省份不能为空");
			// 城市必填
			CommonValidUtil.validIntNoNull(shopBean.getCityId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "城市不能为空");
			// 区县必填
			CommonValidUtil.validIntNoNull(shopBean.getDistrictId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "区县不能为空");
			// 乡镇必填
			CommonValidUtil.validIntNoNull(shopBean.getTownId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "乡镇不能为空");
			//非法字符校验
			SensitiveWordsUtil.checkSensitiveWords("shopDesc", shopBean.getShopDesc());
			
//			 身份证号必填
//			CommonValidUtil.validStrNull(shopBean.getIdentityCardNo(),
//					CodeConst.CODE_PARAMETER_NOT_NULL, "身份证号不能为空");
			if(shopBean.getIdentityCardNo() != null && !IdcardValidator.isValidatedAllIdcard(shopBean.getIdentityCardNo())){
				throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_FORMAT_ERROR_IDENTITY_CARD_NO);
			}
			if(shopBean.getShopManagerIdentityCardNo() != null ){
				if(!IdcardValidator.isValidatedAllIdcard(shopBean.getShopManagerIdentityCardNo())){
					throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_FORMAT_ERROR_SHOPMANAGERIDENTITYCARDNO);
				}
			}
			
			if(shopBean.getShopServeUserMobile() != null){
				// 手机号是否有效
				CommonValidUtil.validStrLongFmt(shopBean.getShopServeUserMobile(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
		        boolean mobileValidate = CommonValidUtil.validateMobile(Long.valueOf(shopBean.getShopServeUserMobile()));
		        if (!mobileValidate)
		        {
		            throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_REQUIRED_MOBILE_VALID);
		        }
			}
			
			// 营业执照号必填
//			CommonValidUtil.validStrNull(shopBean.getBusinessLicenceNo(),
//					CodeConst.CODE_PARAMETER_NOT_NULL, "营业执照号不能为空");
			
			// 分类必填
			CommonValidUtil.validIntNoNull(shopBean.getColumnId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "分类不能为空");
			
			ShopDto shopDto = buildShop(shopBean);
			
			mgrShopService.saveShop(shopDto, shopBean);
//			memberService.updateUser(shopBean.getUserId(),shopBean.getIdentityCardNo());
			Long shopId = shopDto.getShopId();
			// 新增商铺与二级行业关系
			shopBean.setShopId(shopId);
			
			mgrShopService.addShopAndColumnRelation(shopBean);
			String value = CommonConst.ONLINE_SETTING_BAIL_AMOUNT_VALUE +"";
	/*		SysConfigureDto sysConfigureDto = commonService.getSysConfigureDtoByKey(CommonConst.SHOP_SETTING_BAIL_AMOUNT);
			if (null != sysConfigureDto)
			{
			    value = sysConfigureDto.getConfigureValue();
			}*/
			commonService.initShopConfig(shopId);
			/*ConfigDto searchCondition = new ConfigDto();
			searchCondition.setBizId(0l);
			searchCondition.setBizType(0);
			searchCondition.setConfigKey(CommonConst.SHOP_SETTING_BAIL_AMOUNT);
			ConfigDto config = commonService.getConfigDto(searchCondition);
			if (null != config)
			{
			    value = config.getConfigValue();
			}
		     //如果是线上签约的店铺，需要设置自动转充保证金配置
            ShopSettingsDto shopSetting = new ShopSettingsDto();
            shopSetting.setShopId(shopId);
            shopSetting.setSettingType(CommonConst.SHOP_SETTING_TYPE_CHARGE);
            List<ShopConfigureSettingDto> lst = new ArrayList<ShopConfigureSettingDto>();
            lst.add(getSetting(shopId, CommonConst.SHOP_SETTING_BAIL_FLAG, CommonConst.SHOP_SETTING_BAIL_FLAG_FALSE));
            lst.add(getSetting(shopId, CommonConst.SHOP_SETTING_BAIL_ALTER_AMOUNT, CommonConst.ONLINE_SETTING_BAIL_ALTER_AMOUNT_VALUE));
            
            lst.add(getSetting(shopId, CommonConst.SHOP_SETTING_BAIL_AMOUNT, value));//保证不欠费就可以
            shopSetting.setLst(lst);
            shopSettingService.saveConfigureSetting(shopSetting);*/
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopId", shopDto.getShopId());
			
			logger.info("新增商铺接口-end");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增商铺成功！",
					param);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增商铺接口-系统异常", e);
			throw new APISystemException("新增商铺接口-系统异常", e);
		}
	}
	
    private ShopConfigureSettingDto getSetting(Long shopId, String settingKey, Object settingValue)
    {
        ShopConfigureSettingDto shopConfigureSettingDto = new ShopConfigureSettingDto();
        shopConfigureSettingDto.setSettingKey(settingKey);
        shopConfigureSettingDto.setSettingValue(String.valueOf(settingValue));
        shopConfigureSettingDto.setSettingType(CommonConst.SHOP_SETTING_TYPE_CHARGE);
        shopConfigureSettingDto.setShopId(shopId);
        return shopConfigureSettingDto;
    }
	
	/**
	 * MS20：修改商铺接口
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/shopManage/updateShopDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateShopDetail(HttpEntity<String> entity) {
		try {
			final IMemberServcie memService=BeanFactory.getBean(IMemberServcie.class);
			logger.info("修改商铺接口-start");
			ShopBean shopBean= (ShopBean) JacksonUtil.postJsonToObj(
					entity, ShopBean.class);
			// 店铺ID必填
			CommonValidUtil.validLongNull(shopBean.getShopId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "商铺ID不能为空");
			
			// 会员ID必填
			CommonValidUtil.validLongNull(shopBean.getUserId(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "会员ID不能为空");
			
			// 店铺名必填
			CommonValidUtil.validStrNull(shopBean.getShopName(),
					CodeConst.CODE_PARAMETER_NOT_NULL, "店名不能为空");
			if(shopBean.getIdentityCardNo() != null){
				if(!IdcardValidator.isValidatedAllIdcard(shopBean.getIdentityCardNo())){
					throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_FORMAT_ERROR_IDENTITY_CARD_NO);
				}
			}
			
			if(shopBean.getShopManagerIdentityCardNo() != null ){
				if(!IdcardValidator.isValidatedAllIdcard(shopBean.getShopManagerIdentityCardNo())){
					throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_FORMAT_ERROR_SHOPMANAGERIDENTITYCARDNO);
				}
			}
			if(shopBean.getShopServeUserMobile() != null){
				// 手机号是否有效
				CommonValidUtil.validStrLongFmt(shopBean.getShopServeUserMobile(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
		        boolean mobileValidate = CommonValidUtil.validateMobile(Long.valueOf(shopBean.getShopServeUserMobile()));
		        if (!mobileValidate)
		        {
		            throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_REQUIRED_MOBILE_VALID);
		        }
			}
			//关键非法字符校验
			SensitiveWordsUtil.checkSensitiveWords("shopDesc", shopBean.getShopDesc());
			mgrShopService.updateShop(shopBean);
//			if(StringUtils.isNotBlank(shopBean.getIdentityCardNo())) {
//			    memService.updateUser(shopBean.getUserId(),shopBean.getIdentityCardNo());
//			}
			DataCacheApi.del(CommonConst.KEY_SHOP + shopBean.getShopId());
			logger.info("修改商铺接口-end");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "修改商铺成功！", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改商铺接口-系统异常", e);
			throw new APISystemException("修改商铺接口-系统异常", e);
		}
	}

	private ShopDto buildShop(ShopBean shopBean) throws Exception {
		ShopDto shopDto = new ShopDto();
		shopDto.setShopStatus(99); // 审核中
		shopDto.setServerMode(0); // 服务方式：0（无），1(代表上门)，2(代表到店),3(代表上门到店)
		shopDto.setShopName(shopBean.getShopName());
		shopDto.setShopType(1); // 商铺模式：1（直接买单模式(类似嘉旺)），2（先服务后买单模式）
		shopDto.setPrincipalId(Long.valueOf(shopBean.getUserId()+""));
		shopDto.setTelephone(shopBean.getTelephone());
		shopDto.setProvinceId(shopBean.getProvinceId());
		shopDto.setCityId(Long.valueOf(shopBean.getCityId()==null?"0":shopBean.getCityId()+""));
		shopDto.setDistrictId(shopBean.getDistrictId());
		shopDto.setTownId(shopBean.getTownId());
		shopDto.setAddress(shopBean.getAddress());
		shopDto.setColumnId(shopBean.getColumnId());
		shopDto.setMemberDiscount(shopBean.getMemberDiscount());
		shopDto.setAuditStatus(0); // 审核状态：0-待审核,1-审核通过,2-审核被拒
		shopDto.setTakeoutFlag(0); // 商铺是否启用外卖，0不启用，1启用
		shopDto.setBookFlag(0); // 是否启用预定，0不启用，1启用
		shopDto.setConfirmMinute(0);// 订单确认时间，单位分钟：无需确认=0(默认)，10分钟确认=10
		shopDto.setMalling(0); // 抹零设置: 0无抹零设置, 1抹元, 2抹角
		shopDto.setIsHomeService(0); // 是否启用预约上门：是=1(默认)，否=0
		shopDto.setSettleType(1); // 结算方式: 1-按抽成比例结算,2-按折扣结算
		shopDto.setOrderSettleType(1); // 订单结算方式：0-按商品目录价结算,1-按订单总价结算
		shopDto.setSign(0); // 入驻类型：0=一点管家、1=双屏收银机、2=新单屏收银机、3=单屏收银机、4=新一点盒子、5=新一点管家、6=新双屏收银机
		shopDto.setShopHours(shopBean.getShopHours());
		shopDto.setShopDesc(shopBean.getShopDesc());
		shopDto.setContractValidFrom(DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
		shopDto.setContractValidTo(DateUtils.format(DateUtils.addYears(new Date(), 10),DateUtils.DATE_FORMAT)); // 10年
		shopDto.setBusinessLicenceId(shopBean.getBusinessLicenceId());
		shopDto.setPercentage(shopBean.getPercentage());
		shopDto.setBusinessLicenceNo(shopBean.getBusinessLicenceNo());
		shopDto.setLatitude(shopBean.getLatitude());
		shopDto.setLongitude(shopBean.getLongitude());
		shopDto.setShopLogoId(shopBean.getShopLogoId());
		shopDto.setShopMode("common");//先设置为通用类型
		if(shopBean.getReferMobileOrUserId()!=null){
			String refeUser=shopBean.getReferMobileOrUserId();
			UserDto refeUserDto = null;
			if(refeUser.length() > 10) {
				//代表是手机号码
				refeUserDto =mgrShopService.getUserByMobile(refeUser);
			} else {
				//userId
				Long referUserId = NumberUtil.strToLong(refeUser, "refeUser");
				refeUserDto =mgrShopService.getUserById(referUserId);
			}
			if(refeUserDto!=null){
				shopDto.setReferUserId(refeUserDto.getUserId());
				shopDto.setReferUserType(CommonConst.USER_TYPE_MEMBER);
			}
		}
		shopDto.setShopClassify(shopBean.getShopClassify()==null?2:shopBean.getShopClassify());
		shopDto.setOrganizationCode(shopBean.getOrganizationCode());
		shopDto.setOrganizationCodePicId(shopBean.getOrganizationCodePicId());
		shopDto.setOrganizationCodePic(shopBean.getOrganizationCodePic());
		shopDto.setTaxRegistrationCertificate(shopBean.getTaxRegistrationCertificate());
		shopDto.setTaxRegistrationCertificatePic(shopBean.getTaxRegistrationCertificatePic());
		shopDto.setTaxRegistrationCertificatePicId(shopBean.getTaxRegistrationCertificatePicId());
		shopDto.setBusinessCertificatePicIds(shopBean.getBusinessCertificatePicIds());
		shopDto.setBusinessCertificatePics(shopBean.getBusinessCertificatePics());
		shopDto.setBusinessCertificates(shopBean.getBusinessCertificates());
		shopDto.setAuthorizationPic(shopBean.getAuthorizationPic());
		shopDto.setAuthorizationPicId(shopBean.getAuthorizationPicId());
		shopDto.setSkillsCertificateNos(shopBean.getSkillsCertificateNos());
		shopDto.setSkillsCertificatePicIds(shopBean.getSkillsCertificateNos());
		shopDto.setSkillsCertificatePics(shopBean.getSkillsCertificatePics());
		shopDto.setIs3In1(shopBean.getIs3In1()==null?0:shopBean.getIs3In1());
	    shopDto.setLevelId(shopBean.getLevelId());
	    shopDto.setLevelName(shopBean.getLevelName());
	    shopDto.setShopPoint(shopBean.getShopPoint());
	    shopDto.setIsRecommend(shopBean.getIsRecommend());
		shopDto.setShopManagerName(shopBean.getShopkeeper());
		shopDto.setShopManagerIdentityCardNo(shopBean.getIdentityCardNo());
		shopDto.setBusinessCertificateValidTo(shopBean.getBusinessCertificateValidTo());
		shopDto.setPrincipalIdentityCardPicId1(shopBean.getPrincipalIdentityCardPicId1());
		shopDto.setPrincipalIdentityCardPicId2(shopBean.getPrincipalIdentityCardPicId2());
		shopDto.setPrincipalIdentityCardNo(shopBean.getPrincipalIdentityCardNo());
		String identity = shopBean.getIdCardImgs();
		if(StringUtils.isNotEmpty(identity) && identity.indexOf(",")>0) {
		    shopDto.setShopManagerIdentityCardPic1(identity.split(",")[0]);
	        shopDto.setShopManagerIdentityCardPic2(identity.split(",")[1]);
		}
		shopDto.setShopManagerIsCorporate(shopBean.getShopManagerIsCorporate()==null?0:shopBean.getShopManagerIsCorporate());
		return shopDto;
	}
	
	
	/**
	 * 新增或修改收银员 接口
	 * @return
	 */
	@RequestMapping(value={"/shopManage/optShopCashier","/token/shop/optShopCashier","/service/shop/optShopCashier","/session/shop/optShopCashier"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object optShopCashier(HttpEntity<String> entity){
		try {
			
			//TODO 后续按照新的UI来校验必填
			logger.info("新增或修改收银员接口-start");
			ShopCashierParams shopCashier= (ShopCashierParams) JacksonUtil.postJsonToObj(entity, ShopCashierParams.class);
			if(null == shopCashier.getShopId()){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
			}
			CommonValidUtil.validStrLongFmt(shopCashier.getShopId()+"",CodeConst.CODE_PARAMETER_NOT_VALID, "商铺编号格式错误");
			CommonValidUtil.validStrNull(shopCashier.getLoginName(),CodeConst.CODE_PARAMETER_NOT_NULL, "账号不能为空");
			/*if(null==shopCashier.getGeneratePwdMode()){
				//默认手动添加
				shopCashier.setGeneratePwdMode(0);
			}*/
			/*if(shopCashier.getGeneratePwdMode()==null || shopCashier.getGeneratePwdMode()!=1){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "请升级App版本再进行新增/修改收银员信息");
			}*/
			if(null==shopCashier.getGeneratePwdMode() && null==shopCashier.getPassword()){//密码校验
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "密码不能为空");
			}
			
			//验证登录名称是否是老板
			validLoginNameIsBoss(shopCashier.getLoginName(), shopCashier.getShopId());

			Map<String, Object> map = mgrShopService.optShopCashier(shopCashier);
			 
			//发送注册成功短信
			boolean isSendFlag = (boolean) map.get("isSendFlag");
			if(isSendFlag){
				sendSmsToUser(shopCashier.getMobile(), (String)map.get("password"));
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"操作收银员成功！", map);
		} catch (ServiceException e) {
			logger.error("新增或修改收银员接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("新增或修改收银员接口-系统异常", e);
			throw new APISystemException("新增或修改收银员接口-系统异常", e);
		}
	}
	/**
	 * 验证登录名是否老板
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void validLoginNameIsBoss(String loginName,Long shopId) throws Exception {
		
		UserDto userDB = userService.getUserByMobile(loginName);
		if(null!=userDB){
			ShopDto shopDB = shopServcie.getShopByPrincipalId(userDB.getUserId());
			if(shopDB!=null){
				if(shopId.equals(shopDB.getShopId())){
					throw new ValidateException(CodeConst.CODE_PARAMETER_ILLEGAL, "员工登录名不能为老板手机号码");
				}
			}
		}
		
	}

	/*
	 * 发送明文密码给用户
	 */
	private void sendSmsToUser(String mobile, String password) {
		try {
			
			SmsReplaceContent src = new SmsReplaceContent();
			src.setMobile(mobile);
			src.setCode(password);
			src.setUsage(CommonConst.USER_REGISTR_SUCCESS);
			sendSmsService.sendSmsMobileCode(src);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送短信验证码失败", e);
		}
	}
	
	/**
	 * 删除收银员 接口
	 * @return
	 */
	@RequestMapping(value={"/shopManage/delShopCashier","/token/shop/delShopCashier","/service/shop/delShopCashier","/session/shop/delShopCashier"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object delShopCashier(HttpServletRequest request){
		try {
			logger.info("删除收银员接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,CodeConst.CODE_PARAMETER_NOT_VALID, "商铺编号格式错误");
			CommonValidUtil.validStrNull(cashierIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "收银员编号不能为空");
			
			Long cashierId = CommonValidUtil.validStrLongFmt(cashierIdStr,CodeConst.CODE_PARAMETER_NOT_VALID, "收银员编号格式错误");
			mgrShopService.delShopCashier(shopId,cashierId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"删除收银员成功！", null);
		} catch (ServiceException e) {
			logger.error("删除收银员接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("删除收银员接口-系统异常", e);
			throw new APISystemException("删除收银员接口-系统异常", e);
		}
	}
	
	/**
	 * MS23：查询收银员列表接口
	 * @return
	 */
	@RequestMapping(value={"/shopManage/getShopCashiers","/token/shop/getShopCashiers","/service/shop/getShopCashiers","/session/shop/getShopCashiers"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getShopCashiers(HttpServletRequest request){
		try {
			logger.info("查询收银员列表接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
			
			List<Map> data = mgrShopService.getShopCashiers(Long.parseLong(shopIdStr));
			Map map = new HashMap();
			map.put("lst", data);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"获取收银员列表成功！", map);
		} catch (ServiceException e) {
			logger.error("查询收银员列表接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询收银员列表接口-系统异常", e);
			throw new APISystemException("查询收银员列表接口-系统异常", e);
		}
	} 
	
	
	/**
	 * MS24：查询会员接口
	 * @return
	 */
	@RequestMapping(value = "/shopManage/getUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getUser(HttpServletRequest request){
		try {
			logger.info("查询会员接口-start");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "手机号不能为空");
			
			UserDto userDto = mgrShopService.getUser(mobile);
			Map<String, Object> map = new HashMap<String, Object>();
			if(userDto != null){
				map.put("isUser",true);
				map.put("userId", userDto.getUserId());
			}else{
				map.put("isUser", false);
			}
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"查询会员成功！", map);
			
			
		} catch (ServiceException e) {
			logger.error("查询会员接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询会员接口-系统异常", e);
			throw new APISystemException("查询会员接口-系统异常", e);
		}
	} 
	
	/**
	 * MS24：账务统计查询接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shopManage/getAccountingStat", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAccountingStat(HttpServletRequest request) {
		try {
			logger.info("一点管家账务统计查询接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			Map<String, Object> map = new HashMap<String, Object>();
			//校验商铺及商铺设备token
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_FORMAT_ERROR_SHOPID);
			Long cashierId = null;
			if(!StringUtils.isEmpty(cashierIdStr)){
				cashierId = CommonValidUtil.validStrLongFmt(cashierIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_FORMAT_ERROR_CASHIERID);
			}
			map.put("shopId", shopId);
			map.put("cashierId", cashierId);
			// 分页默认20条，第一页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			// startTime
			CommonValidUtil.validStrNull(startTime,
					CodeConst.CODE_PARAMETER_NOT_NULL,"startTime不能为空");
			CommonValidUtil.validDateTimeFormat(startTime,
					CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
			map.put("startTime", startTime);
			// endTime
			CommonValidUtil.validStrNull(endTime,
					CodeConst.CODE_PARAMETER_NOT_NULL,"endTime不能为空");
			CommonValidUtil.validDateTimeFormat(endTime,
					CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
			map.put("endTime", endTime);
			PageModel pageModel = shopServcie.getAccountingStat(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Map<String,Object>> list = pageModel.getList();
			
			double salesIncomeTotal = 0;	//总营业收入
			double serviceTotal = 0;		//总平台服务费
			double spanCashTotal = 0;		//现金总额
			double spanPosTotal = 0;		//pos总额
			double spanOnLineTotal = 0;		//在线支付总额
			
			
			if(list != null && list.size() > 0){
				for(Map e : list){
					if(e != null){
						salesIncomeTotal += (e.get("total") == null ? 0d : ((BigDecimal)e.get("total")).doubleValue());
						serviceTotal += (e.get("shopServiceSharePrice") == null ? 0d : ((BigDecimal)e.get("shopServiceSharePrice")).doubleValue());
						spanCashTotal += (e.get("cashTotal") == null ? 0d : ((BigDecimal)e.get("cashTotal")).doubleValue());
						spanPosTotal += (e.get("posTotal") == null ? 0d : ((BigDecimal)e.get("posTotal")).doubleValue());
						spanOnLineTotal += (e.get("onLineTotal") == null ? 0d : ((BigDecimal)e.get("onLineTotal")).doubleValue());
					}
				}
			}
			//总平台奖励
			resultMap.put("salesIncomeTotal", salesIncomeTotal);
			//resultMap.put("rewardTotal", rewardTotal);
			resultMap.put("serviceTotal", serviceTotal);
			resultMap.put("spanCashTotal", spanCashTotal);
			resultMap.put("spanPosTotal", spanPosTotal);
			resultMap.put("spanOnLineTotal", spanOnLineTotal);
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			logger.info("一点管家账务统计查询接口-end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
					"一点管家账务统计查询成功", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("一点管家账务统计查询接口-系统异常", e);
			throw new APISystemException("一点管家账务统计查询接口-系统异常", e);
		}
	}
	
	@RequestMapping(value="/shopManage/queryShopAccount", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String queryShopAccount(HttpServletRequest request) {
		try {
			logger.info("一点管家查询商铺账户余额接口    -start");
			//shopid
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			  
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			
			Map<String, Object> parms = new HashMap<String, Object>();
			List<Long> shopIds = new ArrayList<Long>();
			shopIds.add(shopId);//TODO 后续连锁店需求，需要查询总店下所有的分店id出来
			parms.put("shopId", shopIds);
			Map<String, Object> map = shopServcie.getShopAccountMoney(parms);
			if(null==map){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账号不存在"); 
			}
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "一点管家查询商铺账户余额成功！", map);
		} catch (ServiceException e) {
			logger.error("一点管家查询商铺账户余额接口    -异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("一点管家查询商铺账户余额接口    -系统异常", e);
			throw new APISystemException("一点管家查询商铺账户余额接口    -系统异常", e);
		}
	}
	
	/**
	 * MS26：查询店铺账户账单明细接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shopManage/getBillDetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getBillDetail(HttpServletRequest request) {
		try {
			logger.info("查询店铺账户账单明细接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String accountTypeStr = RequestUtils.getQueryParam(request, "accountType"); // 账户类型. 1:线上营业收入2:平台奖励3:保证金
			
			String moneyTypeStr = RequestUtils.getQueryParam(request, "moneyType"); // 收支类型
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			
			
			CommonValidUtil.validStrNull(accountTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "账户类型不能为空");
			CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL,"startTime不能为空");
			CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
			
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			int[] accountTypes = convertType(ArrayUtil.toArray(accountTypeStr, null));
			map.put("shopId", shopId);
			map.put("accountType", accountTypes);
			map.put("moneyType", ArrayUtil.toArray(moneyTypeStr, null));
			map.put("startTime", startTime);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			
			// endTime
			if(StringUtils.isNotEmpty(endTime)){
				CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
			}else {
				endTime = DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
			}
			map.put("endTime", endTime);
			
			PageModel pageModel = shopServcie.getBillDetail(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			
			logger.info("查询店铺账户账单明细接口-end");
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "店铺账户账单明细查询成功", resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询店铺账户账单明细接口-系统异常", e);
			throw new APISystemException("查询店铺账户账单明细接口-系统异常", e);
		}
	}
	
    /**
     * 接口传递的accountType的值与数据库对应的值进行转换
     * 1:线上营业收入  --> 0
     * 2:平台奖励  --> 1
     * @param accountTypes 账户类型数组
     * @return void [返回类型说明]
     * @author  shengzhipeng
     * @date  2016年2月22日
     */
	private int[] convertType(String[] accountTypes) throws Exception {
        int[] intAccountTypes = new int[accountTypes.length];
        if(accountTypes != null && accountTypes.length > 0) {
            for (int i = 0; i < accountTypes.length; i++) {
                String string = accountTypes[i];
                if("1".equals(string)) {
                    intAccountTypes[i] = 0;
                } else if ("2".equals(string)) {
                    intAccountTypes[i] = 1;
                } else {
                    intAccountTypes[i] = NumberUtil.strToNum(accountTypes[i], "accountTypes");
                }
                
            }
        }
        return intAccountTypes;
    }
	
	
	/**
	 * MS28：查询店铺推荐会员列表接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/getShopRefMembers", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getShopRefMembers(HttpServletRequest request) {
		try {
			logger.info("查询店铺推荐会员列表接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			
			map.put("shopId", shopId);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			
			PageModel pageModel = shopServcie.getShopRefMembers(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			
			logger.info("查询店铺推荐会员列表接口-end");
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询店铺推荐会员列表接口成功", resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询店铺推荐会员列表接口-系统异常", e);
			throw new APISystemException("查询店铺推荐会员列表接口-系统异常", e);
		}
	}
	
	/**
	 * MS29：销售统计接口
	 * 根据日期、收银员、支付方式以及顾客类型（会员和非会员）查询销售统计接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/getShopSalestatistics", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getShopSalestatistics(HttpServletRequest request) {
		try {
			logger.info("销售统计接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String cashierIdStr = RequestUtils.getQueryParam(request, "cashierId");
			String queryModeStr = RequestUtils.getQueryParam(request, "queryMode");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			String memberTypeStr = RequestUtils.getQueryParam(request, "memberType");
			
			// 分页默认10条，第1页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSize(pSizeStr);
			
			//Map<String, Object> map = new HashMap<String, Object>();
			Integer cashierId=null;
			if(cashierIdStr!=null && cashierIdStr!="null"){
				cashierId=NumberUtil.stringToInteger(cashierIdStr);
			}
			Integer queryMode = 0;
			if(queryModeStr!= null && !queryModeStr.isEmpty()){
				queryMode=NumberUtil.stringToInteger(queryModeStr);
			}
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validStrNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_STARTTIME_NOT_NULL);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			PageModel pageModel = shopServcie.getShopSalestatistics(shopId,cashierId,queryMode,memberTypeStr,startTime,endTime,pNo,pSize);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			
			logger.info("销售统计接口-end");
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "销售统计查询成功", resultMap, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询销售统计接口-系统异常", e);
			throw new APISystemException("查询销售统计接口-系统异常", e);
		}
	}
	
	/**
	 * MS30：商铺身份证号或银行卡号验证接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/verifyCardNo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String verifyCardNo(HttpServletRequest request) {
		try {
			logger.info("商铺身份证号或银行卡号验证接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String verifyModeStr = RequestUtils.getQueryParam(request, "verifyMode");
			String identityCardNo = RequestUtils.getQueryParam(request, "identityCardNo");
			String bankCardNo = RequestUtils.getQueryParam(request, "bankCardNo");
			
			Integer verifyMode=NumberUtil.stringToInteger(verifyModeStr==null?"0":verifyModeStr);
			
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("verifyMode", verifyMode);
			map.put("identityCardNo", identityCardNo);
			map.put("bankCardNo", bankCardNo);
			
			boolean flag = shopServcie.verifyCardNo(map);
			
			if (!flag) {
				return ResultUtil.getResultJsonStr(CodeConst.CODE_VALIDATE_CARDNO_OR_BANKCARDNO_ERROR, CodeConst.MSG_validate_CARDNO_OR_BANKCARDNO_ERROR, null);
			}
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "验证成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商铺身份证号或银行卡号验证接口-系统异常", e);
			throw new APISystemException("商铺身份证号或银行卡号验证接口-系统异常", e);
		}
	}
	
	/**
	 * MS37：获取店内会员统计信息接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/getShopMemberStatisticsInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getShopMemberStatisticsInfo(HttpServletRequest request) {
		try {
			logger.info("获取店内会员统计信息接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			Map<String,Object> resutlMap = shopServcie.getShopMemberStatisticsInfo(map);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取店内会员统计信息成功", resutlMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取店内会员统计信息接口-系统异常", e);
			throw new APISystemException("获取店内会员统计信息接口-系统异常", e);
		}
	}
	
	/**
	 * PSM46：根据条件统计相应对象的数量
	 * @return
	 */
	@RequestMapping(value={"/token/shop/getObjCountByCondition","/service/shop/getObjCountByCondition","/session/shop/getObjCountByCondition"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getObjCountByCondition(HttpServletRequest request){
		try {
			logger.info("根据条件统计相应对象的数量接口-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String searchObject = RequestUtils.getQueryParam(request, "searchObject");
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
			CommonValidUtil.validStrNull(searchObject, CodeConst.CODE_PARAMETER_NOT_NULL, "searchObjectStr不能为空");
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, "shopId数据格式不正确");
			Map<String,Integer> resultMap = mgrShopService.getObjCountByCondition(shopId,startTime,endTime,searchObject);
//			List<Map> data = mgrShopService.getShopCashiers(Long.parseLong(shopIdStr));
//			Map map = new HashMap();
//			map.put("lst", data);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"根据条件统计相应对象的数量成功！", resultMap);
		} catch (ServiceException e) {
			logger.error("根据条件统计相应对象的数量接口-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("根据条件统计相应对象的数量接口-系统异常", e);
			throw new APISystemException("根据条件统计相应对象的数量接口-系统异常", e);
		}
	} 

	
}
