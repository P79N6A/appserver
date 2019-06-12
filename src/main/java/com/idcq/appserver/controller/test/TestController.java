package com.idcq.appserver.controller.test;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.goods.BatchUpdateGoodsModel;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class TestController extends BaseController {

	public static void main(String[] args) throws Exception {
		logger.debug("name:{}  sex:{}", "name","nv");
	}
	private final static Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value={"/pay/test"},
	        		produces="application/json;charset=UTF-8")
	
	
	@ResponseBody
	public Object test(HttpServletRequest request,String orderId) throws Exception{
		//TestModel model = getRequestModel(request, TestModel.class);
		BatchUpdateGoodsModel updateGoodsModel = getRequestModel(request, BatchUpdateGoodsModel.class);
		return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "测试接口成功",updateGoodsModel);
	}
}
