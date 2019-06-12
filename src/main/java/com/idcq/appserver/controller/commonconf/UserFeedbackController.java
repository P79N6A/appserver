package com.idcq.appserver.controller.commonconf;

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
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.commonconf.UserFeedbackDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.commonconf.IShopFeedBackService;
import com.idcq.appserver.service.commonconf.IUserFeedbackService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
@RequestMapping(value="/commonconf")
public class UserFeedbackController {
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IUserFeedbackService userFeedbackService;
	
	@Autowired
	private IShopFeedBackService shopFeedBackService;
	@RequestMapping(value="/giveFeedback")
	@ResponseBody
	public ResultDto giveFeedback(HttpServletRequest request) throws Exception{
		String uid = RequestUtils.getQueryParam(request, "userId");
		String feedback = RequestUtils.getQueryParam(request, "feedback");
		String createTime = RequestUtils.getQueryParam(request, "createTime");
		CommonValidUtil.validObjectNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
		CommonValidUtil.validObjectNull(feedback, CodeConst.CODE_PARAMETER_NOT_NULL,"feedback不能为空");
		CommonValidUtil.validObjectNull(createTime, CodeConst.CODE_PARAMETER_NOT_NULL,"createTime不能为空");
		Long userId = NumberUtil.strToLong(uid, "userId");
		Date dateTime = DateUtils.parse(createTime, DateUtils.DATETIME_FORMAT);
		
		//校验是否是日期格式
		CommonValidUtil.validObjectNull(dateTime, CodeConst.CODE_PARAMETER_NOT_VALID,"createTime参数不合法");
		
		if(feedback.length() > 500){
			throw new ValidateException(CodeConst.CODE_FEEDBACK_LENGTH_BEYOND, CodeConst.MSG_FEEDBACK_LENGTH_BEYOND);
		}
		
		UserFeedbackDto userFeedbackDto = new UserFeedbackDto();
		userFeedbackDto.setUserId(userId);
		userFeedbackDto.setFeedback(feedback);
		
		userFeedbackDto.setCreateTime(dateTime);
		
		this.userFeedbackService.giveUserFeedback(userFeedbackDto);
		
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "反馈意见成功！", null);
	}
	
	
	/**
	 * 手机端APP版本检测
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/checkVersion",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto checkVersion(HttpServletRequest request) throws Exception{
		Map map = new HashMap();
		Map pModel = new HashMap();
		logger.info("APP版本检测");
		String userIdStr = RequestUtils.getQueryParam(request, "userId");
		String appIdStr = RequestUtils.getQueryParam(request, "appId");
		String curVersion = RequestUtils.getQueryParam(request, "curVersion");
		//CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
		Long userId = null;
		if (!StringUtils.isEmpty(userIdStr)) {
			userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
		}
		CommonValidUtil.validStrNull(appIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_APP_ID);
		Long appId = CommonValidUtil.validStrLongFmt(appIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERR_APP_ID);
		CommonValidUtil.validStrNull(curVersion, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_APP_VERSION);
		pModel = userFeedbackService.queryVersion(userId,appId,curVersion);
		map.put("obj", pModel);
		return ResultUtil.getResult(0, "版本检测成功！",map);
	}
	
}
