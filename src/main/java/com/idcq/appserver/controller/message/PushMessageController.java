package com.idcq.appserver.controller.message;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.message.MessageDto;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.home.IHomeServcie;
import com.idcq.appserver.service.message.IMessageSettingService;
import com.idcq.appserver.service.message.IPushMessageService;
import com.idcq.appserver.service.user.IPushUserTableService;

/**
 * 消息controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:57:55
 */
@Controller
//@RequestMapping(value="")
public class PushMessageController {
	
	private final Log logger = LogFactory.getLog(getClass());
	private String registrationId="";
	private String content="{\"action\":\"regUser\",\"mobile\":\"13601234567\"}";
	@Autowired
	public IPushMessageService pushMessageService;
	@Autowired
	public IMessageSettingService messageSettingService;
	@Autowired
	public IHomeServcie homeService;
	@Autowired
	public IPushUserTableService pushUserTableService;
	
	/**
	 * 消息通知,推送给所有设备
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/push/sendMsg")
	@ResponseBody
	public ResultDto sendMsg(HttpServletRequest request){
		try {
			logger.info("消息推送-start");
			String appKey ="cefc0b88444e400ceba6db63";  
			String masterSecret = "829cc09752614093c8c44d06"; 
			Jpush.pushToAll(content); 
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "消息推送成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("消息推送-系统异常", e);
			throw new APISystemException("消息推送-系统异常", e);
		}
	}
	
	/**
	 * 消息通知指定的设备
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/push/sendMsgToTarget")
	@ResponseBody
	public ResultDto sendMsgToTarget(HttpServletRequest request){
		try {
			logger.info("指定设备消息推送-start");
			String registrationID = RequestUtils.getQueryParam(request, "registrationID");
			Jpush.sendPushToTarget("and",CommonConst.USER_TYPE_ZREO,registrationID,null,content); 
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "指定设备消息推送成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("指定设备消息推送-系统异常", e);
			throw new APISystemException("指定设备消息推送-系统异常", e);
		}
	}
	
	/**
	 * 商铺发布的消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/push/shopMessage")
	@ResponseBody
	public ResultDto shopMessage(HttpServletRequest request){
		try {
			logger.info("商铺发布的消息-start");
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String messageId = RequestUtils.getQueryParam(request, "messageId");
			CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			CommonValidUtil.validObjectNull(messageId, CodeConst.CODE_PARAMETER_NOT_NULL,"messageId不能为空");
			MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey("shopMessage");
			if(null != messageSettingDto && 1==messageSettingDto.getRemandFlag()) {
				StringBuffer sb=new StringBuffer();
				sb.append("{\"action\":\"shopMessage\",");
				sb.append("\"shopId\":\""+shopId+"\",");
				sb.append("\"messageId\":\""+messageId+"\"}");
				Jpush.pushToAll(sb.toString()); 
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商铺发布的消息推送成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商铺发布的消息-系统异常", e);
			throw new APISystemException("商铺发布的消息-系统异常", e);
		}
	}
	
	/**
	 * 平台发布的营销信息，广告优惠信息推送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/push/platformMessage")
	@ResponseBody
	@SuppressWarnings("all")
	public ResultDto platformMessage(HttpServletRequest request){
		try {
			logger.info("平台发布的消息-start");
			String messageId = RequestUtils.getQueryParam(request, "messageId");
			CommonValidUtil.validObjectNull(messageId, CodeConst.CODE_PARAMETER_NOT_NULL,"messageId不能为空");
			MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey("message");
			if(null != messageSettingDto && 1==messageSettingDto.getRemandFlag()) {
				MessageDto message=homeService.getMessageById(Long.parseLong(messageId));
				String userType=message.getTargetUserType();
				CommonValidUtil.validObjectNull(userType, CodeConst.CODE_PARAMETER_NOT_NULL,"推送的目标对象(targetUserType)不能为空");
				Long provinceId=message.getProvinceId();
				Long cityId=message.getCityId();
				Long districtId=message.getDistrictId();
				Long townId=message.getTownId();
				StringBuffer sb=new StringBuffer();
				sb.append("{\"action\":\"message\",");
				sb.append("\"messageId\":\""+messageId+"\"}");
				Map param=new HashMap();
				param.put("provinceId", provinceId);
				param.put("cityId", cityId);
				param.put("districtId", districtId);
				param.put("townId", townId);
				List<PushUserTableDto> list=null;
				if(CommonConst.SHOPS.equals(userType)){//商铺
					param.put("userType", CommonConst.USER_TYPE_SHOP);
				}else if(CommonConst.DISTRIBUTOR.equals(userType)){//经销商
					param.put("userType", CommonConst.USER_TYPE_DISTRIBUTOR);
				}else if(CommonConst.AGENT.equals(userType)){ //代理商
					param.put("userType", CommonConst.USER_TYPE_AGENT);
				}else{//会员
					param.put("userType", CommonConst.USER_TYPE_MEMBER);
				}
				list=pushUserTableService.getPushUserByRegion(param);
				if(null !=list && list.size()>0){
					PushUserTableDto p=null;
					String content=sb.toString();
					for(int i=0,len=list.size();i<len;i++){
						p=list.get(i);
						Jpush.sendPushToTarget(p.getOsInfo(), p.getUserType(), p.getRegId(),null,content);
					}
				}
				homeService.updateMessage(message);
				
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "平台发布的消息推送成功", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("平台发布的消息-系统异常", e);
			throw new APISystemException("平台发布的消息-系统异常", e);
		}
	}

	/**
	 *PC29：查/轮询推送消息接口
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/push/getMessages")
	@ResponseBody
	public ResultDto getMessages(HttpServletRequest request) throws Exception{
			logger.info("查询相关的推送消息-start");
			String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
			String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
			String messageStatusStr = RequestUtils.getQueryParam(request, "messageStatus");
			String actionsStr = RequestUtils.getQueryParam(request, "actions");
			String startDateStr = RequestUtils.getQueryParam(request, "startDate");
			String endDateStr = RequestUtils.getQueryParam(request, "endDate");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			String pollAroundFlagStr = RequestUtils.getQueryParam(request, "pollAroundFlag");

			Integer pollAroundFlag = StringUtils.isBlank(pollAroundFlagStr) ? 1 : Integer.valueOf(pollAroundFlagStr.trim());
			//查询参数
			Map<String, Object> searchParams = new HashMap<>();

			if(pollAroundFlag.intValue() == 1)		//如果为轮询，则以下参数固定为默认值
			{
				messageStatusStr = null;
				startDateStr = null;
				endDateStr = null;
				pNoStr = null;
				pSizeStr = null;
			}

			//当前时间
			Date now = new Date();

			//开始校验并格式化请求参数
			Integer bizType = CommonValidUtil.validStrIntFmt(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "bizType为空或者格式不正确");
			if(bizType.intValue() == 2)
			{
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "暂不支持bizType为2的类型");
			}
			Long bizId = CommonValidUtil.validStrLongFmt(bizIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "bizId为空或者格式不正确");
			Integer messageStatus = StringUtils.isBlank(messageStatusStr) ? 1 : Integer.valueOf(messageStatusStr.trim());
			//与数据库对应
			if(messageStatus != 1)
			{
				messageStatus = 3;
			}
			List<String> actions = StringUtils.isBlank(actionsStr) ? null : Arrays.asList(actionsStr.trim().split(","));
			//默认开始时间为当前时间前一日
			Date startDate = StringUtils.isBlank(startDateStr) ? null : DateUtils.parse(startDateStr);
			if(startDate == null)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
				startDate = calendar.getTime();
			}
			Date endDate = StringUtils.isBlank(endDateStr) ? now : DateUtils.parse(endDateStr.toString());
			Integer pNo = StringUtils.isBlank(pNoStr) ? 1 : Integer.valueOf(pNoStr.trim());
			Integer pSize = StringUtils.isBlank(pSizeStr) ? 10 : Integer.valueOf(pSizeStr.trim());

			if(pollAroundFlag.intValue() == 1)
			{
				searchParams.put("cacheFlag", bizIdStr + "$_$" + actionsStr);
			}
//			NonCachingClassLoaderRepository.SoftHashMap
			searchParams.put("bizType", bizType);
			searchParams.put("bizId", bizId);
			searchParams.put("messageStatus", messageStatus);
			searchParams.put("actions", actions);

			searchParams.put("startDate", startDate);
			searchParams.put("endDate", endDate);
			searchParams.put("pNo", pNo);
			searchParams.put("pSize", pSize);

			searchParams.put("pollAroundFlag", pollAroundFlag);
			PageModel pageModel = pushMessageService.getPushMsgs(searchParams);
			Map<String, Object> rs = new HashMap<>();
			rs.put("pNo", pNo);
			rs.put("rCount", pageModel.getTotalItem());
			rs.put("lst", pageModel.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取成功", rs);
	}


	/**
	 *PC30：上报推送消息接口
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/push/informMessages", "/token/push/informMessages"}, method = RequestMethod.POST, consumes="application/json", produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto informMessages(HttpServletRequest request, HttpEntity<String> entity) throws Exception{
		logger.info("上报推送消息接口-start");
		Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
		String bizTypeStr = postParamMap.get("bizType");
		String bizIdStr = postParamMap.get("bizId");
		String messageStatusStr = postParamMap.get("messageStatus");
		String actionsStr = postParamMap.get("actions");
		String messageIdsStr = postParamMap.get("messageIds");

		//开始格式化并验证参数
		Long bizId = CommonValidUtil.validStrLongFmt(bizIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "bizId为空或者格式不正确");
		Integer bizType = CommonValidUtil.validStrIntFmt(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "bizType为空或者格式不正确");
		//暂仅支持商铺消息
		if(bizType != 1)
		{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "不受支持的bizType类型");
		}
		Integer messageStatus = StringUtils.isBlank(messageStatusStr) ? 2 : Integer.valueOf(messageStatusStr.trim());
		boolean hasActions = StringUtils.isNotBlank(actionsStr);
		boolean hasMessageIds = StringUtils.isNotBlank(messageIdsStr);
		if((!hasActions) && (!hasMessageIds))
		{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "actions与messageIds不能全为空");
		}
		List<String> actions = null;
		if(hasActions)
		{
			actions = Arrays.asList(actionsStr.trim().split(","));
		}
		List<Long> messageIds = null;
		if(hasMessageIds)
		{
			messageIds = new ArrayList<>();
			for(String tempId : messageIdsStr.trim().split(",")){
				messageIds.add(Long.valueOf(tempId));
			}
		}

		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("bizType", bizType);
		requestParams.put("bizId", bizId);
		requestParams.put("messageStatus", messageStatus);
		requestParams.put("actions", actions);
		requestParams.put("messageIds", messageIds);

		pushMessageService.informPushMsgs(requestParams);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "上报成功", null);
	}


}
