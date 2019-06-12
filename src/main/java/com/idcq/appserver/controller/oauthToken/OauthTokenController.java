package com.idcq.appserver.controller.oauthToken;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.oauthToken.OauthTokenRequest;
import com.idcq.appserver.service.oauthToken.IOauthTokenService;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class OauthTokenController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(OauthTokenController.class);
	
	@Autowired
	private IOauthTokenService oauthTokenService;
	
	@RequestMapping(value = {
			"/token/oauthToken/getOauthToken",
			"/service/oauthToken/getOauthToken",
			"/session/oauthToken/getOauthToken" },produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getOauthToken(HttpServletRequest request) throws Exception {
		logger.info("POT1：获取第三方授权访问令牌 ----start");
		OauthTokenRequest requestModel = getRequestModel(request, OauthTokenRequest.class);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取授权令牌成功", oauthTokenService.getOauthToken(requestModel));
	}
}
