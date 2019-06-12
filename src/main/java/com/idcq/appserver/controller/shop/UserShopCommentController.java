package com.idcq.appserver.controller.shop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 铺评论列controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:38:07
 */
@Controller
@RequestMapping(value="/comment")
public class UserShopCommentController {
	
	private final Logger logger = Logger.getLogger(UserShopCommentController.class);
	@Autowired
	public IShopServcie shopServcie;
	
	/**
	 * 获取商铺评论
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShopComments")
	@ResponseBody
	public ResultDto getShopComments(HttpServletRequest request){
		try {
			logger.info("获取指定商铺评论-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			Long shopId=NumberUtil.strToLong(shopIdStr,"shopId");
			Integer pageNo=PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pageSize=PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			PageModel pageModel= this.shopServcie.getShopComments(shopId,pageNo,pageSize);
			
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			dataList.add(msgList);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取指定商铺评论成功", msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取指定商铺评论-系统异常", e);
			throw new APISystemException("获取指定商铺评论-系统异常", e);
		}
	}
	
}
