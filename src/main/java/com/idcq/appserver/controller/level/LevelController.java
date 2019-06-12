package com.idcq.appserver.controller.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.level.AddPointDetailModel;
import com.idcq.appserver.dto.level.AddPointModel;
import com.idcq.appserver.dto.level.LevelValidDto;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.level.PointRuleValidDto;
import com.idcq.appserver.dto.level.PrerogativeDto;
import com.idcq.appserver.dto.level.PrerogativeValidDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class LevelController extends BaseController {
	
	@Autowired
	public ILevelService levelService;
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private ICollectService collectService;
	
	/**
	 * PCL1： 查询等级详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getLevelDetail","/service/level/getLevelDetail","/session/level/getLevelDetail"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getLevelDetail(HttpServletRequest request) throws Exception{
		logger.info("PCL1： 查询等级详情接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String levelId = RequestUtils.getQueryParam(request, "levelId");
		CommonValidUtil.validStrNull(levelId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_LEVEL_ID_NOT_NULL);
		CommonValidUtil.validStrIntFmt(levelId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELID);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		Map<String,Object> resultMap =levelService.getLevelDetail(levelId,shopId);
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, " 查询等级详情接口成功",resultMap, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCL2： 创建/修改等级接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/level/updateLevelInfo",
			"/service/level/updateLevelInfo",
			"/session/level/updateLevelInfo" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateLevelInfo(HttpServletRequest request) throws Exception {
		LevelValidDto levelDto = getRequestModel(request, LevelValidDto.class);
		if(levelDto!=null && levelDto.getLevelId()==null){
			CommonValidUtil.validIntNoNull(levelDto.getLevelType(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_LEVEL_TYPE_NOT_NULL);
			if(levelDto.getLevelType() !=CommonConst.LEVEL_TYPE_SHOP && levelDto.getLevelType() !=CommonConst.LEVEL_TYPE_MEMBER){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELTYPE);
			}
		}
		int levelId = levelService.updateLevelInfo(levelDto);
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("levelId", levelId);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "创建/修改等级成功！",map);
	}
	
	/**
	 * PCL3： 等级操作接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/operateLevelInfo","/service/level/operateLevelInfo","/session/level/operateLevelInfo"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object operateLevelInfo(HttpServletRequest request) throws Exception{
		logger.info("PCL3： 等级操作接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		//操作等级 Id,多个以逗号分隔
		String levelId = RequestUtils.getQueryParam(request, "levelId");
		//操作类型： 1=删除
		String operateType = RequestUtils.getQueryParam(request, "operateType");
		CommonValidUtil.validStrNull(levelId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_LEVEL_ID_NOT_NULL);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		CommonValidUtil.validStrNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_OPERATETYPE_NOT_NULL);
		CommonValidUtil.validStrLongFmt(operateType, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		if(Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_DELETE 
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_ADD
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_UPDATE){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		}
		levelService.operateLevelInfo(shopId,levelId,operateType);
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "等级操作成功",null);
	}
	
	/**
	 * PCL4： 查询特权详情接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getPrerogativeDetail","/service/level/getPrerogativeDetail","/session/level/getPrerogativeDetail"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getPrerogativeDetail(HttpServletRequest request) throws Exception{
		logger.info("PCL4： 查询特权详情接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String prerogativeId = RequestUtils.getQueryParam(request, "prerogativeId");
		Map<String,Object> map = new HashMap<String,Object>();
		CommonValidUtil.validStrNull(prerogativeId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PREROGATIVEID_NOT_NULL);
		CommonValidUtil.validStrIntFmt(prerogativeId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_PREROGATIVEID);
		map.put("prerogativeId", prerogativeId);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			map.put("shopId", shopId);
		}
		map.put("isDelete", CommonConst.IS_DELETE_FALSE);
		PrerogativeDto prerogativeDto =levelService.getPrerogativeDetail(map);
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询特权详情接口成功",prerogativeDto, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCL5： 创建/修改特权接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/level/updatePrerogativeInfo",
			"/service/level/updatePrerogativeInfo",
			"/session/level/updatePrerogativeInfo" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updatePrerogativeInfo(HttpServletRequest request) throws Exception {
		logger.info("PCL5： 创建/修改特权接口-start");
		PrerogativeValidDto prerogativeDto = getRequestModel(request, PrerogativeValidDto.class);
		if(prerogativeDto!=null && prerogativeDto.getPrerogativeType() != null){
			if(prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_REFER
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_BILLDATE
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_CONSUM
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_DESIGN
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_RATE
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_REPORT
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_SEND
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_SERVICE
				&& prerogativeDto.getPrerogativeType()!=CommonConst.PREROGATIVE_TYPE_SHOW){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELTYPE);
			}
		}
		int prerogativeId =levelService.updatePrerogativeInfo(prerogativeDto);
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("prerogativeId", prerogativeId);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "创建/修改特权接口成功！",map);
	}
	
	
	/**
	 * PCL6： 特权操作接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/operatePrerogativeInfo","/service/level/operatePrerogativeInfo","/session/level/operatePrerogativeInfo"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object operatePrerogativeInfo(HttpServletRequest request) throws Exception{
		logger.info("PCL6： 特权操作接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String prerogativeId = RequestUtils.getQueryParam(request, "prerogativeId");
		String operateType = RequestUtils.getQueryParam(request, "operateType");
		CommonValidUtil.validStrNull(prerogativeId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PREROGATIVEID_NOT_NULL);
//		CommonValidUtil.validStrIntFmt(prerogativeId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELID);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		CommonValidUtil.validStrNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_OPERATETYPE_NOT_NULL);
		CommonValidUtil.validStrLongFmt(operateType, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		if(Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_DELETE 
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_ADD
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_UPDATE){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		}
		levelService.operatePrerogativeInfo(shopId,prerogativeId,operateType);
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "特权操作成功",null);
	}
	
	/**
	 * PCL7： 查询等级列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getLevelList","/service/level/getLevelList","/session/level/getLevelList"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getLevelList(HttpServletRequest request) throws Exception{
		logger.info("PCL7： 查询等级列表接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String levelType = RequestUtils.getQueryParam(request, "levelType");
		String startLevelCondition = RequestUtils.getQueryParam(request, "startLevelCondition");
		String endLevelCondition = RequestUtils.getQueryParam(request, "endLevelCondition");
		String pNoStr = RequestUtils.getQueryParam(request, "pNo");
		String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
		Map<String,Object> map = new HashMap<String,Object>();
		if(levelType != null){
			CommonValidUtil.validStrLongFmt(levelType, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELTYPE);
			if(Integer.valueOf(levelType)!=CommonConst.LEVEL_TYPE_MEMBER && Integer.valueOf(levelType)!=CommonConst.LEVEL_TYPE_SHOP){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELTYPE);
			}
			map.put("levelType", levelType);
		}
		if(startLevelCondition !=null){
			CommonValidUtil.validStrLongFmt(startLevelCondition, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELCONDITION);
			map.put("startLevelCondition", startLevelCondition);
		}
		if(endLevelCondition != null){
			CommonValidUtil.validStrLongFmt(endLevelCondition, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELCONDITION);
			map.put("endLevelCondition", endLevelCondition);
		}
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			map.put("shopId", shopId);
		}
		// 分页默认10条，第1页
		Integer pNo = PageModel.handPageNo(pNoStr);
		Integer pSize = PageModel.handPageSize(pSizeStr);
		map.put("pNo", (pNo - 1) * pSize);
		map.put("pSize", pSize);
		map.put("isDelete", CommonConst.IS_DELETE_FALSE);
		PageModel pageModel =levelService.getLevelList(map);
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("pNo", pNo);
		resultMap.put("rCount", pageModel.getTotalItem());
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询等级列表成功",resultMap, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCL8： 查询特权列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getPrerogativeList","/service/level/getPrerogativeList","/session/level/getPrerogativeList"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getPrerogativeList(HttpServletRequest request) throws Exception{
		logger.info("PCL8： 查询特权列表接口-start" + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String levelId = RequestUtils.getQueryParam(request, "levelId");
		//特权类型，多个以逗号分隔
		String prerogativeType = RequestUtils.getQueryParam(request, "prerogativeType");
		String pNoStr = RequestUtils.getQueryParam(request, "pNo");
		String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
		Map<String,Object> map = new HashMap<String,Object>();
		if(levelId != null){
			CommonValidUtil.validStrIntFmt(levelId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_LEVELID);
			map.put("levelId", levelId);
		}
		if(prerogativeType != null){
			List<String> prerogativeTypeList= new ArrayList<String>();
			if(prerogativeType.contains(",")){
				String[] prerogativeTypeArray = prerogativeType.split(",");
				prerogativeTypeList = Arrays.asList(prerogativeTypeArray);
			}else{
				prerogativeTypeList.add(prerogativeType);
			}
			map.put("prerogativeType", prerogativeTypeList);
		}
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			map.put("shopId", shopId);
		}
		// 分页默认10条，第1页
		Integer pNo = PageModel.handPageNo(pNoStr);
		Integer pSize = PageModel.handPageSize(pSizeStr);
		map.put("pNo", (pNo - 1) * pSize);
		map.put("pSize", pSize);
		map.put("isPaging", 1);
		map.put("isDelete", CommonConst.IS_DELETE_FALSE);
		PageModel pageModel =levelService.getPrerogativeList(map);
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("pNo", pNo);
		resultMap.put("rCount", pageModel.getTotalItem());
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询特权列表接口成功",resultMap, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCL9： 等级特权操作接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/operateLevelPrerogativeInfo","/service/level/operateLevelPrerogativeInfo","/session/level/operateLevelPrerogativeInfo"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object operateLevelPrerogativeInfo(HttpServletRequest request) throws Exception{
		logger.info("PCL9： 等级特权操作接口-start " + request.getQueryString());
		String shopId = RequestUtils.getQueryParam(request, "shopId");
		String prerogativeIds = RequestUtils.getQueryParam(request, "prerogativeIds");
		String levelId = RequestUtils.getQueryParam(request, "levelId");
		String operateType = RequestUtils.getQueryParam(request, "operateType");
		CommonValidUtil.validStrNull(levelId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_LEVEL_ID_NOT_NULL);
		CommonValidUtil.validStrNull(prerogativeIds, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_PREROGATIVEID_NOT_NULL);
		if(shopId!=null){
			CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		}
		CommonValidUtil.validStrNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_OPERATETYPE_NOT_NULL);
		CommonValidUtil.validStrLongFmt(operateType, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		if(Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_DELETE
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_ADD
				&& Integer.valueOf(operateType)!=CommonConst.OPERATE_TYPE_UPDATE){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
		}
		levelService.operateLevelPrerogativeInfo(shopId,prerogativeIds,levelId,operateType);
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "等级特权操作成功",null);
	}
	
	/**
	 * PCL10： 创建/修改积分规则接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {
			"/token/level/updatePointRule",
			"/service/level/updatePointRule",
			"/session/level/updatePointRule" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updatePointRule(HttpServletRequest request) throws Exception {
		logger.info("PCL10： 创建/修改积分规则接口-start");
		PointRuleValidDto pointRuleDto = getRequestModel(request, PointRuleValidDto.class);
		validPointRule(pointRuleDto);
		int pointRuleId = levelService.updatePointRule(pointRuleDto);
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("pointRuleId", pointRuleId);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, " 创建/修改积分规则成功！",map);
	}
	
	private void validPointRule(PointRuleValidDto pointRuleDto) {
		if(pointRuleDto!=null){
			if(pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_SETTILE
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_BINDING
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_COMMENT
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_CONSUM
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_ORDER
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_REFER
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_REGIST
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_RELEASEGOODS
					){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_RULETYPE);
			}
			if(pointRuleDto.getTaskType()!=CommonConst.TASK_TYPE_COMMON
					&& pointRuleDto.getTaskType()!=CommonConst.TASK_TYPE_UNCOMMON
					){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_RULETYPE);
			}
			if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_SETTILE){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_SETTLE_SHOP){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_BINDING){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_BINDING_SERVICE
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_BINDING_SUBSCRIPTION
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_RELEASEGOODS){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_REFER_MEMBER_FIRST
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_REFER){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_REFER_MEMBER
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_REFER_MEMBER_FIRST
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_REFER_SHOP
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_ORDER){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_ORDER_CASH
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_ORDER_MONEY
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_ORDER_NO
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_REGIST){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_REGIST_MEMBER
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}else if(pointRuleDto.getRuleType() == CommonConst.RULE_TYPE_COMMENT){
				if(pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_COMMON_FIVE
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_COMMON_FOURE
						&& pointRuleDto.getSubRuleType()!=CommonConst.SUB_RULE_TYPE_COMMON_THREE
						){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SUBRULETYPE);
				}
			}
		}
	}

	/**
	 * PCL11： 查询积分明细
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getPointDetail","/service/level/getPointDetail","/session/level/getPointDetail"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getPointDetail(HttpServletRequest request) throws Exception{
		logger.info("PCL10： 查询积分明细接口-start" + request.getQueryString());
		String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
        String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
        String clientSystemStr = RequestUtils.getQueryParam(request, "clientSystem");
        Integer pNo = PageModel.handPageNo(pageNO);
        Integer pSize = PageModel.handPageSize(pageSize);
		PointDetailDto pointDetailDto = getRequestModel(request, PointDetailDto.class);
		pointDetailDto.setpNo((pNo-1)*pSize);
		pointDetailDto.setpSize(pSize);
		CommonValidUtil.validStrNull(clientSystemStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_CLIENT_SYSTEM_NOT_NULL);
		CommonValidUtil.validStrIntFmt(clientSystemStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CLIENT_SYSTEM);
		Integer clientSystem = Integer.valueOf(clientSystemStr);
		PageModel pageModel = new PageModel();
		if(clientSystem == CommonConst.CLIENT_SYSTEM_IS_GJ || clientSystem == CommonConst.CLIENT_SYSTEM_IS_SYJ){
			if(pointDetailDto!=null){
				CommonValidUtil.validStrNull(pointDetailDto.getBizIds(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_BIZID_IS_NULL);
				CommonValidUtil.validStrIntFormat(pointDetailDto.getBizIds(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_FORMAT_ERROR_BIZID);
				CommonValidUtil.validIntNoNull(pointDetailDto.getBizType(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_BIZTYPE_IS_NULL);
				if(pointDetailDto.getBizType()!=null 
						&& Integer.valueOf(pointDetailDto.getBizType())!=CommonConst.BIZ_TYPE_IS_SHOP 
						&& Integer.valueOf(pointDetailDto.getBizType())!=CommonConst.BIZ_TYPE_IS_USER){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BIZTYPE);
				}
				Integer pointSourceType = pointDetailDto.getPointSourceType();
				if(pointSourceType!=null){
					if(pointSourceType!=CommonConst.RULE_TYPE_BINDING 
							&& pointSourceType!=CommonConst.RULE_TYPE_COMMENT
							&& pointSourceType!=CommonConst.RULE_TYPE_CONSUM
							&& pointSourceType!=CommonConst.RULE_TYPE_ORDER
							&& pointSourceType!=CommonConst.RULE_TYPE_REFER
							&& pointSourceType!=CommonConst.RULE_TYPE_REGIST
							&& pointSourceType!=CommonConst.RULE_TYPE_RELEASEGOODS
							&& pointSourceType!=CommonConst.RULE_TYPE_SETTILE){
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_POINTSOURCETYPE);
					}
				}
				pointDetailDto.setBizId(Integer.valueOf(pointDetailDto.getBizIds()));
				pointDetailDto.setBizIds(null);
				pageModel  =levelService.getPointDetail(pointDetailDto);
			}else{
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_BIZID_IS_NULL);
			}
		}else if(clientSystem == CommonConst.CLIENT_SYSTEM_IS_O2O){
			pageModel  =levelService.getPointDetailByBizIds(pointDetailDto);
		}else{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CLIENT_SYSTEM);
		}
		
		
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("pNo", pNo);
		resultMap.put("rCount", pageModel.getTotalItem());
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, " 查询积分明细成功",resultMap, DateUtils.DATETIME_FORMAT);
	}
	
	/**
	 * PCL12： 查询积分规则列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/token/level/getPointRuleList","/service/level/getPointRuleList","/session/level/getPointRuleList"},produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getPointRuleList(HttpServletRequest request) throws Exception{
		logger.info("PCL12： 查询积分规则列表接口-start" + request.getQueryString());
		PointRuleDto pointRuleDto = getRequestModel(request, PointRuleDto.class);
		String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
        String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
        Integer pNo = PageModel.handPageNo(pageNO);
        Integer pSize = PageModel.handPageSize(pageSize);
        pointRuleDto.setPageNo((pNo-1)*pSize);
        pointRuleDto.setPageSize(pSize);
		if(pointRuleDto!=null){
			if(pointRuleDto.getRuleType() !=null && pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_SETTILE
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_BINDING
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_COMMENT
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_CONSUM
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_ORDER
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_REFER
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_REGIST
					&& pointRuleDto.getRuleType() != CommonConst.RULE_TYPE_RELEASEGOODS
					){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_RULETYPE);
			}
		}
		PageModel pageModel  =levelService.getPointRuleList(pointRuleDto);
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		resultMap.put("pNo", pNo);
		resultMap.put("rCount", pageModel.getTotalItem());
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询积分规则列表成功",resultMap);
	}
	
	/**
	 * PCL13：增加店铺/会员积分
	 * 
	 * @Function: com.idcq.appserver.controller.level.MemberLevelController.test
	 * @Description:
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年5月25日 上午10:48:01
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年5月25日    ChenYongxin      v1.0.0         create
	 */
	@RequestMapping(value={"/token/level/addPoint","/service/level/addPoint","/session/level/addPoint"},produces="application/json;charset=UTF-8")
	@ResponseBody 
	public Object addPoint(HttpServletRequest request) throws Exception {
		/**
		 * 1、查询规则 
		 * 2、记录积分流水 
		 * 3、增加店铺积分 
		 * 4、更新店铺等级
		 */

		AddPointModel addPointModel = getRequestModel(request, AddPointModel.class);
		
		List<AddPointDetailModel>  apointList = addPointModel.getAddPointList();
		
		if(CollectionUtils.isNotEmpty(apointList)){
			
			for (AddPointDetailModel addPointDetailModel : apointList) {
				
				levelService.addPoint(addPointDetailModel,addPointModel.getOperaterId(),addPointModel.getOperaterName());
				
			}
		}
		
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "增加积分成功", null);
	}	

}
