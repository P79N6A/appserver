package com.idcq.appserver.service.oauthToken;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.oauthToken.OauthTokenRequest;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.ali.util.AliPayUtil;

@Service
public class OauthTokenServiceImpl implements IOauthTokenService {
	private final static Logger logger = LoggerFactory.getLogger(OauthTokenServiceImpl.class);
	
	@Override
	public Map<String, Object> getOauthToken(OauthTokenRequest requestModel) throws Exception {
		Map<String, Object> oauthMap = new HashMap<String, Object>();
		String oauthInfo = "";
		if (requestModel.getOauthChannel() == 1) {
			oauthInfo = AliPayUtil.queryOauthToken(requestModel.getOauthCode());
		}else {
			logger.info("不支持该授权渠道，渠道代码：{}", requestModel.getOauthChannel());
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"占不支持该授权渠道");
		}
		
		oauthMap.put("oauthToken", oauthInfo);
		return oauthMap;
	}
}
