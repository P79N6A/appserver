//package com.idcq.appserver.controller.message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.idcq.appserver.common.CodeConst;
//import com.idcq.appserver.dto.ResultDto;
//import com.idcq.appserver.dto.common.PageModel;
//import com.idcq.appserver.dto.message.MessageDto;
//import com.idcq.appserver.dto.message.MessageListDto;
//import com.idcq.appserver.exception.APIBusinessException;
//import com.idcq.appserver.exception.APISystemException;
//import com.idcq.appserver.exception.ServiceException;
//import com.idcq.appserver.service.message.IMessageService;
//import com.idcq.appserver.utils.RequestUtils;
//import com.idcq.appserver.utils.ResultUtil;
//
//
///**
// * 消息controller
// * 
// * @author Administrator
// * 
// * @date 2015年3月4日
// * @time 下午3:57:55
// */
//@Controller
//@RequestMapping(value="/msg")
//public class MessageController {
//	
//	private final Log logger = LogFactory.getLog(getClass());
//	
//	@Autowired
//	public IMessageService messageService;
//	
//	/**
//	 * 分页获取消息列表
//	 * 
//	 * @param request
//	 * @return
//	 */
////	@RequestMapping(value="/getMsg")
////	@ResponseBody
////	public ResultDto getMessages(HttpServletRequest request){
////		try {
////			logger.info("分页获取消息列表-start");
////			String msgType = RequestUtils.getQueryParam(request, "msgType");
////			String pageNO = RequestUtils.getQueryParam(request, "pNo");
////			String pageSize = RequestUtils.getQueryParam(request, "pSize");
////			MessageDto message = new MessageDto();
////			message.setMsgType(Integer.valueOf(msgType));
////			/*
////			 * 首先检索符合条件的总记录数
////			 * 然后检索数据列表
////			 * 最后封装到pageModel
////			 */
////			PageModel pageModel = this.messageService.
////					getMsgList(message, Integer.valueOf(pageNO), Integer.valueOf(pageSize));
////			
////			MessageListDto msgList = new MessageListDto();
////			msgList.setpNo(pageModel.getToPage());
////			msgList.setpSize(pageModel.getPageSize());
////			msgList.setLst(pageModel.getList());
////			
////			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
////			dataList.add(msgList);
////			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取消息列表成功", msgList);
////		} catch (ServiceException e) {
////			throw new APIBusinessException(e);
////		} catch (Exception e) {
////			e.printStackTrace();
////			logger.error("获取消息列表-系统异常", e);
////			throw new APISystemException("获取消息列表-系统异常", e);
////		}
////	}
//	
//}
