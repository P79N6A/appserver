package com.idcq.appserver.controller.attention;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.attention.AttentionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.attention.IAttentionServcie;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;


/**
 * 我的关注controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:02:58
 */
@Controller
@RequestMapping(value="/myattention")
public class AttentionController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public IAttentionServcie attentionServcie;
	
	/**
	 * 分页获取我的关注列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getMyAttention2")
	@ResponseBody
	public ResultDto getOrderList(@ModelAttribute(value="order")AttentionDto atten ,HttpServletRequest request){
		try {
			logger.info("获取我的关注列表-start");
			String pageNO = RequestUtils.getQueryParam(request,"pageNo");
			String pageSize = RequestUtils.getQueryParam(request,"pageSize");
			/*
			 * 首先检索符合条件的总记录数
			 * 然后检索数据列表
			 * 最后封装到pageModel
			 */
			PageModel pageModel = this.attentionServcie.getAttenList(atten, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));
			
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取我的关注列表成功", msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取我的关注列表-系统异常", e);
			throw new APISystemException("获取我的关注列表-系统异常", e);
		}
	}
	
}
