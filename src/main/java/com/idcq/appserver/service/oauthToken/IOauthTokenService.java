package com.idcq.appserver.service.oauthToken;

import java.util.Map;

import com.idcq.appserver.dto.oauthToken.OauthTokenRequest;

public interface IOauthTokenService {
	Map<String, Object> getOauthToken(OauthTokenRequest requestModel) throws Exception;
}
