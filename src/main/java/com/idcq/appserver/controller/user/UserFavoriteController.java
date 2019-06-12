package com.idcq.appserver.controller.user;

import java.util.HashMap;
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
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.dto.user.UserFavoriteDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.user.IUserFavoriteService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class UserFavoriteController {
		private Log logger  = LogFactory.getLog(getClass());
		
		@Autowired
		private IUserFavoriteService userFavoriteService;
		@Autowired
		private IMemberServcie memberService;
		
		/**
		 * 新增收藏
		 * @param request
		 * @return
		 */
		@RequestMapping(value="user/favorite")
		@ResponseBody
		public ResultDto favorite(HttpServletRequest request){
			try{
				logger.info("用户收藏/取消收藏-start");
				String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
				String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
				String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
				String operTypeStr = RequestUtils.getQueryParam(request, "operType");
				String favoriteUrl = RequestUtils.getQueryParam(request, "favoriteUrl");
				UserFavoriteDto userFavoriteDto = new UserFavoriteDto();
				/*****参数校验******/
				//userId
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
				Long userId = NumberUtil.strToLong(userIdStr, "userId");
				// 根据userId判断用户是否存在
				UserDto userDB = this.memberService.getUserByUserId(userId);
				// 校验对象是否为空
				CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				userFavoriteDto.setUserId(userId);
				//bizId
				CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId不能为空");
				Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");
				userFavoriteDto.setBizId(bizId);
				//operType
				if(StringUtils.isBlank(operTypeStr)){
					userFavoriteDto.setOperType(1);
				}else{
					Integer operType = NumberUtil.strToNum(operTypeStr, "operType");
					userFavoriteDto.setOperType(operType);
				}
				//bizType
				if(StringUtils.isBlank(bizTypeStr)){
					userFavoriteDto.setBizType(0);
				}else{
					Integer bizType = NumberUtil.strToNum(bizTypeStr, "bizType");
					userFavoriteDto.setBizType(bizType);
				}
				userFavoriteDto.setFavoriteUrl(favoriteUrl);
				//必填字段校验
				userFavoriteService.favorite(userFavoriteDto);
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！", null);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("用户收藏/取消收藏失败！",e);
				throw new APISystemException("用户收藏/取消收藏失败！", e);
			}
			
		}
		/**
		 * 获取用户是否收藏接口
		 * @param request
		 * @return
		 */
		@RequestMapping(value="user/getFavoriteStatus")
		@ResponseBody
		public ResultDto getFavoriteStatus(HttpServletRequest request){
			try{
				logger.info("获取用户是否收藏接口-start");
				String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
				String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
				String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
				UserFavoriteDto userFavoriteDto = new UserFavoriteDto();
				/*****参数校验******/
				//userId
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
				Long userId = NumberUtil.strToLong(userIdStr, "userId");
				// 根据userId判断用户是否存在
				UserDto userDB = this.memberService.getUserByUserId(userId);
				// 校验对象是否为空
				CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				userFavoriteDto.setUserId(userId);
				//bizId
				CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizId不能为空");
				Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");
				userFavoriteDto.setBizId(bizId);
				//bizType
				if(StringUtils.isBlank(bizTypeStr)){
					userFavoriteDto.setBizType(0);
				}else{
					Integer bizType = NumberUtil.strToNum(bizTypeStr, "bizType");
					userFavoriteDto.setBizType(bizType);
				}
				//必填字段校验
				Map<String, Object>  resultMap = userFavoriteService.getFavoriteStatus(userFavoriteDto);
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！", resultMap);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("获取用户是否收藏接口失败！",e);
				throw new APISystemException("获取用户是否收藏接口失败！", e);
			}
			
		}
		/**
		 *U38：获取我的收藏店铺列表
		 * @param request
		 * @return
		 */
		@RequestMapping(value="user/getMyFavoriteShop")
		@ResponseBody
		public ResultDto getMyFavoriteShop(HttpServletRequest request){
			try{
				logger.info("U38：获取我的收藏店铺列表-start");
				String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
				String pNoStr = RequestUtils.getQueryParam(request, "pNo");
				String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
				//经度
				String longitudeStr = RequestUtils.getQueryParam(request, "longitude");
				//维度
				String latitudeStr = RequestUtils.getQueryParam(request, "latitude");
				//参数map
				Map<String, Object> paramMap = new HashMap<String, Object>();
				/*****参数校验******/
				//userId
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
				Long userId = NumberUtil.strToLong(userIdStr, "userId");
				// 根据userId判断用户是否存在
				UserDto userDB = this.memberService.getUserByUserId(userId);
				// 校验对象是否为空
				CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				// 页码
				Integer pNo = PageModel.handPageNo(pNoStr);
				Integer pSize = PageModel.handPageSize(pSizeStr);
				//经度
				if(StringUtils.isNotBlank(longitudeStr)){
					Double longitude = NumberUtil.strToDouble(longitudeStr, "longitude");
					paramMap.put("longitude", longitude);
				}
				//经度
				if(StringUtils.isNotBlank(latitudeStr)){
					Double latitude = NumberUtil.strToDouble(latitudeStr, "latitude");
					paramMap.put("latitude", latitude);
				}

				Map<String, Object> resultMap = new HashMap<String, Object>();
				paramMap.put("pSize", pSize);
				paramMap.put("skip", (pNo - 1) * pSize);
				paramMap.put("userId", userId);
				PageModel pageModel = userFavoriteService.getMyFavoriteShop(paramMap);
				resultMap.put("lst", pageModel.getList());
				resultMap.put("pNo", pNo);
				resultMap.put("rcount", pageModel.getTotalItem());
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取我的收藏店铺列表成功！", resultMap);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("U38：获取我的收藏店铺列表！",e);
				throw new APISystemException("U38：获取我的收藏店铺列表！", e);
			}
			
		}
		/**
		 *U38：获取我的收藏商品列表
		 * @param request
		 * @return
		 */
		@RequestMapping(value="user/getMyFavoriteGoods")
		@ResponseBody
		public ResultDto getMyFavoriteGoods(HttpServletRequest request){
			try{
				logger.info("U38：获取我的收藏店铺列表-start");
				String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
				String pNoStr = RequestUtils.getQueryParam(request, "pNo");
				String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
				/*****参数校验******/
				//userId
				CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
				Long userId = NumberUtil.strToLong(userIdStr, "userId");
				// 根据userId判断用户是否存在
				UserDto userDB = this.memberService.getUserByUserId(userId);
				// 校验对象是否为空
				CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				// 页码
				Integer pNo = PageModel.handPageNo(pNoStr);
				Integer pSize = PageModel.handPageSize(pSizeStr);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				paramMap.put("pSize", pSize);
				paramMap.put("skip", (pNo - 1) * pSize);
				paramMap.put("userId", userId);
				PageModel pageModel = userFavoriteService.getMyFavoriteGoods(paramMap);
				resultMap.put("lst", pageModel.getList());
				resultMap.put("pNo", pNo);
				resultMap.put("rcount", pageModel.getTotalItem());
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取我的收藏商品列表成功！", resultMap);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("U38：获取我的收藏商品列表！",e);
				throw new APISystemException("U38：获取我的收藏商品列表！", e);
			}
			
		}	

}
