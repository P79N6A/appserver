package com.idcq.appserver.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 会员信息controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午7:43:09
 */
@Controller
public class CommentController {
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	public IMemberServcie memberService;
	@Autowired
	public IPayServcie payServcie;	
	@Autowired
	public ILevelService levelService;
	@Autowired
	public IOrderServcie orderService;
	/**
	 * 用户评论商品商铺
	 *  
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/comment/makeComment",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object makeComment(HttpServletRequest request){
		try {
			logger.info("用户评论商品商铺-start");
			//评论类型
			String commentType = RequestUtils.getQueryParam(request, "commentType");
			//shopId或goodsId
			String bizId = RequestUtils.getQueryParam(request, "bizId");
			//userId
			String userId = RequestUtils.getQueryParam(request, "userId");
			//服务评分
			String serviceGrade = RequestUtils.getQueryParam(request, "serviceGrade");
			//环境评分
			String envGrade = RequestUtils.getQueryParam(request, "envGrade");
			//速度评分
			String logisticsGrade = RequestUtils.getQueryParam(request, "logisticsGrade");
			//评论内容
			String commentContent = RequestUtils.getQueryParam(request, "commentContent");
			Long commentId = memberService.makeComment(commentType, bizId, userId, serviceGrade,envGrade, commentContent,logisticsGrade);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("commentId", commentId);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_COMMENT_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("用户评论商品商铺-系统异常",e);
			throw new APISystemException("用户评论商品商铺-系统异常", e);
		}
	}
	
	/**
	 * 用户评论商品商铺
	 *  
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/comment/getCommentTotalNumber",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getCommentTotalNumber(HttpServletRequest request){
		try {
			logger.info("获取评论信息-start");
			//评论类型
			String commentType = RequestUtils.getQueryParam(request, "commentType");
			//shopId或goodsId或orderid
			String bizId = RequestUtils.getQueryParam(request, "bizId");
			int totalNumber = memberService.getCommentTotalNumber(commentType, bizId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalNumber", totalNumber);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_MESSAGE_SUCCESS, map);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取评论信息-系统异常",e);
			throw new APISystemException("获取评论信息-系统异常", e);
		}
	}		
	
}
