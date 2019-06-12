package com.idcq.appserver.controller.membercard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.membercard.IUserMemberBillService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
@RequestMapping(value="/user")
public class UserMemberBillController {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IUserMemberBillService userBillService;
	
	/**
	 * 查询用户会员卡账单接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserMembershipCardBill")
	@ResponseBody
	public ResultDto getUserBill(HttpServletRequest request){
		try{
			String billType = RequestUtils.getQueryParam(request, "billType");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			
			int pno = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int psize = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pSize"));
			String uid = RequestUtils.getQueryParam(request, "userId");
			String sid = RequestUtils.getQueryParam(request, "shopId");
			String aid = RequestUtils.getQueryParam(request, "accountId");
			
			CommonValidUtil.validObjectNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			CommonValidUtil.validNumStr(uid, CodeConst.CODE_PARAMETER_NOT_VALID,"userId格式错误");
			CommonValidUtil.validObjectNull(sid, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			CommonValidUtil.validNumStr(sid, CodeConst.CODE_PARAMETER_NOT_VALID,"shopId格式错误");
			Long accountId = null;
			if(!StringUtils.isBlank(aid)) {
				accountId = NumberUtil.strToLong(aid, "accountId");
			}
			Long userId = Long.parseLong(uid);
			Long shopId = Long.parseLong(sid);
			
			//当前时间
//			Date curDate1 = DateUtils.getCurrentDate("YYYY-MM-DD 23:59:59");
			Date curDate1 = DateUtils.getCurrentDate("yyyy-MM-dd 23:59:59");
			Date curDate2 = DateUtils.getCurrentDate("yyyy-MM-dd 00:00:00");
			if(StringUtils.isBlank(startTime)){
				Calendar c = Calendar.getInstance();
			    c.setTime(curDate2);
			    c.add(Calendar.DATE, -7);//为空时，一周内信息
			    startTime = DateUtils.format(c.getTime(), "yyyy-MM-dd 00:00:00");
				
			}
			if(StringUtils.isBlank(endTime)){ 
				endTime =  DateUtils.format(curDate1, "yyyy-MM-dd 23:59:59");
			}
			
			Map<String,Object> map =  new HashMap<String,Object>();
			if(!StringUtils.isBlank(billType)){
				map.put("billType", billType);
			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			
			map.put("userId", userId);
			map.put("shopId",shopId);
			if(accountId != null){
				map.put("accountId", accountId);
			}
			
			PageModel page = userBillService.getPageUserBill(pno, psize, map);
			
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(pno);
			msgDto.setpSize(psize);
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			
			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			dataList.add(msgDto);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询会员卡账单成功！", dataList);
			
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询会员卡账单失败！",e);
			throw new APISystemException("查询会员卡账单失败！", e);
		}
		
		
	}
	
}
