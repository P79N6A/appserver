package com.idcq.appserver.service.busArea.shopMember.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;

@Service("operpateShopMember")
public class OperatShopMemberProcessor implements IProcessor {

	@Autowired
	private IShopMemberService shopMemberService;
	
	@Override
	public Object exective(Map<String, Object> params) throws Exception{
		shopMemberService.delShopMemberByIds(params);
		return CodeConst.CODE_SUCCEED;
	}

}
