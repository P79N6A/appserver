package com.idcq.appserver.controller.attention;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.attention.UserAttentionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.attention.IUserAttentionService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;


@Controller
@RequestMapping(value="/myattention")
public class UserAttentionController {
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IUserAttentionService userAttentionService;
	@Autowired
	private IMemberServcie memberService;
	@Autowired
	private IShopServcie shopService;
	
	@RequestMapping(value="/getMyAttention")
	@ResponseBody
	public ResultDto getMyAttention(HttpServletRequest request){
		try{
			String uid = RequestUtils.getQueryParam(request, "userId");
			CommonValidUtil.validObjectNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			CommonValidUtil.validNumStr(uid, CodeConst.CODE_PARAMETER_NOT_VALID,"userId格式错误");
			
			Integer pageNo=PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
			Integer pageSize=PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
			
			Long userId = NumberUtil.strToLong(uid,"userId");
			UserDto user=memberService.getUserByUserId(userId);
			
			PageModel page = userAttentionService.getMyAttention(pageNo, pageSize, userId);
						
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(pageNo);
			msgDto.setpSize(pageSize);
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取用户关注列表成功！", msgDto);
			
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取用户关注列表失败！", e);
			throw new APISystemException("获取用户关注列表失败", e);
		}
		
		
	}
	
	/**
	 * 用户关注店铺、取消店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addMyAttention")
	@ResponseBody
	public ResultDto addMyAttention(HttpServletRequest request){
		long start=System.currentTimeMillis();
		logger.info("start:"+start);
		try{
			String uid = RequestUtils.getQueryParam(request, "userId");
			String sid = RequestUtils.getQueryParam(request, "shopId");
			String operType = RequestUtils.getQueryParam(request, "operType");//1:关注  2:取消关注
			CommonValidUtil.validObjectNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			CommonValidUtil.validNumStr(uid, CodeConst.CODE_PARAMETER_NOT_VALID,"userId格式错误");
			CommonValidUtil.validObjectNull(sid, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			CommonValidUtil.validNumStr(sid, CodeConst.CODE_PARAMETER_NOT_VALID,"shopId格式错误");
			long userId = Integer.parseInt(uid);
			long shopId = Integer.parseInt(sid);
			UserDto user=memberService.getUserByUserId(userId);//校验用户是否存在
			CommonValidUtil.validObjectNull(user,
					CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
			//校验商铺是否存在
			ShopDto pModel = shopService.getShopById(shopId);
			CommonValidUtil.validObjectNull(pModel, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			if(operType==null){
				operType="1";
			}
			CommonValidUtil.validNumStr(operType, CodeConst.CODE_PARAMETER_NOT_VALID,"operType格式错误");
			int count = userAttentionService.getCountByUserIdAndShopId(userId, shopId);
			if(2 == Integer.parseInt(operType)){//取消关注
				//判断是否已经关注过
				if(count==0){
					return ResultUtil.getResult(CodeConst.CODE_USER_CANCEL_ATTENTION_FAIL,CodeConst.MSG_USER_CANCEL_ATTENTION_FAIL, null);
				}
				userAttentionService.cancelUserAttention(userId, shopId);
				logger.info("共耗时:"+(System.currentTimeMillis()-start));
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "取消关注成功！", null);
			}else{
				//判断是否已经关注过
				//int count = userAttentionService.getCountByUserIdAndShopId(userId, shopId);
				if(count>=1){
					return ResultUtil.getResult(CodeConst.CODE_SYSTEM_BUSY, "该店铺已经被该用户关注！", null);
				}
				//如果没关注过，进行关注操作
				UserAttentionDto userAttentionDto = new UserAttentionDto();
				userAttentionDto.setUserId(userId);
				userAttentionDto.setShopId(shopId);
				userAttentionDto.setCreateTime(new Date());
				int flag = userAttentionService.addUserAttention(userAttentionDto);
				logger.info("共耗时:"+(System.currentTimeMillis()-start));
				if(flag==1){
					return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "关注店铺成功！", null);
				}else{
					return ResultUtil.getResult(CodeConst.CODE_SYSTEM_BUSY, "关注店铺失败", null);
				}
			}
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("关注店铺失败！", e);
			throw new APISystemException("关注店铺失败", e);
		}
	}
	
	
}
