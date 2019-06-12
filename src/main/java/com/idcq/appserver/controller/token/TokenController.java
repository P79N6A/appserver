/*package com.idcq.appserver.controller.token;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.CodeDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcard.ICashCardService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

*//**
 * 非校验token控制
 * 
 * @author Administrator
 * 
 * @date 2015年12月7日
 * @time 下午11:38:08
 *//*
@Controller
@RequestMapping(value="/token")
public class TokenController {

	private static final Logger logger = Logger.getLogger(TokenController.class);
	@Autowired
	private IPayServcie payServcie;
	@Autowired
	private ICollectService collectService;
	@Autowired
	private ICommonService commonService;
	@Autowired
    private IShopServcie shopService;
	@Autowired
    private ICashCardService cashCardService;
	@Autowired
    private IOrderServcie orderServcie;
	@Autowired
    private ISendSmsService sendSmsService;
	
	
   
}
*/