package com.idcq.appserver.controller.membercard;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.membercard.UserMemberCardDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.membercard.IUserMemberCardService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
@RequestMapping(value="/user")
public class UserMemberCardController {
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IUserMemberCardService userMemberCardService;
	@Autowired
	private IMemberServcie memberService;
	
	/**
	 * 查询会员卡余额接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUserMembershipCardMoney")
	@ResponseBody
	public ResultDto getLeftMoney(HttpServletRequest request){
		
		try{
			String queryType = RequestUtils.getQueryParam(request, "queryType");//1：userId和shopId来组合查询 2：会员卡账号accountId来查询
			String accountIdStr = RequestUtils.getQueryParam(request, "accountId");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			//int accountId=0;int shopId = 0;int userId = 0;
			CommonValidUtil.validObjectNull(queryType, CodeConst.CODE_PARAMETER_NOT_NULL,"queryType不能为空");
			CommonValidUtil.validNumStr(queryType, CodeConst.CODE_PARAMETER_NOT_VALID,"queryType格式错误");
			int type = Integer.parseInt(queryType);
			if(2!=type){
				type=1;//输入其他类型，转成默认类型
			}
			UserMemberCardDto dto=null;
			if(type==1){
				CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
				CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
				Long userId = NumberUtil.strToLong(userIdStr, "userId");
				Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
				UserDto user = memberService.getUserByUserId(userId);
				if (user == null) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_USER_ACCOUNT_NOT_EXIST);
				}
				dto = userMemberCardService.getLeftMoney(type, null, userId, shopId);
			}
			if(type==2){
				CommonValidUtil.validObjectNull(accountIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"accountId不能为空");
				Long accountId = NumberUtil.strToLong(accountIdStr, "accountId");
				dto = userMemberCardService.getLeftMoney(type, accountId, null, null);
			}
			if(null==dto){
				dto = new UserMemberCardDto();
				dto.setAmount(0D);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取会员卡余额成功！", dto);
			
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取会员卡余额失败！",e);
			throw new APISystemException("获取会员卡余额失败！", e);
		}
		
		
	}
	
	
}
