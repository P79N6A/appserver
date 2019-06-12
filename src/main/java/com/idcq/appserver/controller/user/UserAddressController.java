package com.idcq.appserver.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.user.UserAddressDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.user.IUserAddressService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
@RequestMapping(value="/user")
public class UserAddressController {
		private Log logger  = LogFactory.getLog(getClass());
		
		@Autowired
		private IUserAddressService userAddressService;
		@Autowired
		private IMemberServcie memberService;
		
		/**
		 * 获取用户地址
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/getUserAddresses",produces="application/json;charset=UTF-8")
		@ResponseBody
		public ResultDto getUserAddresses(HttpServletRequest request){
			try{
				String uid = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
				
				String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
				String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
				String defaultFlagStr = RequestUtils.getQueryParam(request, "defaultFlag");
				
				CommonValidUtil.validStrNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
				Long userId = NumberUtil.strToLong(uid, "userId");
				Integer defaultFlag = null;
				if(StringUtils.isNotBlank(defaultFlagStr)) {
					defaultFlag = NumberUtil.strToNum(defaultFlagStr, "defaultFlag");
				}
				UserAddressDto userAddressDto = new UserAddressDto();
				userAddressDto.setUserId(userId);
				userAddressDto.setDefaultFlag(defaultFlag);
				PageModel page = userAddressService.getListUserAddress(userAddressDto, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));
				MessageListDto msgDto = new MessageListDto();
				msgDto.setpNo(page.getToPage());
				msgDto.setpSize(page.getPageSize());
				msgDto.setrCount(page.getTotalItem());
				msgDto.setLst(page.getList());
				
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取用户地址成功！", msgDto);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("获取用户地址失败！",e);
				throw new APISystemException("获取用户地址失败！", e);
			}
		}
		
		/**
		 * 新增地址
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/addUserAddress",produces="application/json;charset=UTF-8")
		@ResponseBody
		public ResultDto addUserAddress(HttpServletRequest request){
			try{
				//必填字段校验
				UserAddressDto userAddressDto = getUserAddressInfo(request);
				int flag = userAddressService.addUserAddress(userAddressDto);
				if(flag==1){
					return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增用户地址成功！", null);
				}else {
					return ResultUtil.getResult(CodeConst.CODE_USERADDRESS_ADD_ERR, CodeConst.MSG_USERADDRESS_ADD_ERR, null);
				}
				
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("新增用户地址失败！",e);
				throw new APISystemException("新增用户地址失败！", e);
			}
			
		}

		
		
		/**
		 * 删除地址
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/deleteUserAddress",produces="application/json;charset=UTF-8")
		@ResponseBody
		public ResultDto deleteUserAddress(HttpServletRequest request){
			try{
				
				//必填字段校验
				//userId
				UserAddressDto userAddressDto = setAddressInfo(request);
				
				this.userAddressService.deleteUserAddress(userAddressDto);
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除用户地址成功！", null);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("删除用户地址失败！",e);
				throw new APISystemException("删除用户地址失败！", e);
			}
			
		}

		
		/**
		 * 设置默认地址
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/setDefaultUserAddress",produces="application/json;charset=UTF-8")
		@ResponseBody
		public ResultDto setDefaultUserAddress(HttpServletRequest request) {
			logger.info("设置默认地-start");
			try {
				UserAddressDto userAddressDto = setAddressInfo(request);
				this.userAddressService.setDefaultUserAddress(userAddressDto);
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_OPERATION, null);
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("设置默认地址失败！",e);
				throw new APISystemException("设置默认地址失败！", e);
			}
		}
		
		/**
		 * 修改地址
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/modifyUserAddress",produces="application/json;charset=UTF-8")
		@ResponseBody
		public ResultDto modifyUserAddress(HttpServletRequest request) {
			logger.info("修改用户地址-start");
			try {
				String aid = RequestUtils.getQueryParam(request, "addressId");
				CommonValidUtil.validStrNull(aid, CodeConst.CODE_PARAMETER_NOT_NULL, "addressId不能为空");
				Long addressId = NumberUtil.strToLong(aid, "addressId");
				UserAddressDto userAddressDto = getUserAddressInfo(request);
				userAddressDto.setAddressId(addressId);
				int num = this.userAddressService.updateUserAddress(userAddressDto);
				if(num == 1){
					return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_OPERATION, null);
				}else {
					return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USERADDRESS_NONE, null);
				}
			}catch(ServiceException e){
				throw new APIBusinessException(e);
			}catch(Exception e){
				logger.error("修改用户地址失败！",e);
				throw new APISystemException("修改用户地址失败！", e);
			}
		}
		
		/**
		 * 封装用户地址信息
		 * @param request
		 * @return
		 * @throws Exception
		 */
		private UserAddressDto getUserAddressInfo(HttpServletRequest request)
				throws Exception {
			//userId
			String uid = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validStrNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			
			Long userId = NumberUtil.strToLong(uid, "userId");
			
			//defaultFlag 默认地址
			String dflag = RequestUtils.getQueryParam(request, "defaultFlag");
			CommonValidUtil.validStrNull(dflag, CodeConst.CODE_PARAMETER_NOT_NULL,"defaultFlag不能为空");
			Integer defaultFlag =  NumberUtil.strToNum(dflag, "defaultFlag");
			if(1 !=defaultFlag ){
				defaultFlag = 0;
			}				
			
			//userName
			String userName = RequestUtils.getQueryParam(request, "userName");
			CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_NULL,"userName不能为空");
			
			//mobile
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,"mobile不能为空");
			CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
			//provinceId
			String pid = RequestUtils.getQueryParam(request, "provinceId");
			CommonValidUtil.validStrNull(pid, CodeConst.CODE_PARAMETER_NOT_NULL,"provinceId不能为空");
			Long provinceId = NumberUtil.strToLong(pid, "provinceId");
			
			//cityId
			String cid = RequestUtils.getQueryParam(request, "cityId");
			CommonValidUtil.validStrNull(cid, CodeConst.CODE_PARAMETER_NOT_NULL,"cityId不能为空");
			Long cityId = NumberUtil.strToLong(cid, "cityId");
			
			//districtId
			String did = RequestUtils.getQueryParam(request, "districtId");
			CommonValidUtil.validStrNull(did, CodeConst.CODE_PARAMETER_NOT_NULL,"districtId不能为空");
			Long districtId = NumberUtil.strToLong(did, "districtId");
			String tid = RequestUtils.getQueryParam(request, "townId");
			Long townId = null;
			if(StringUtils.isNotBlank(tid)) {
				 townId = NumberUtil.strToLong(tid, "townId");
			}
			//address
			String address = RequestUtils.getQueryParam(request, "address");
			CommonValidUtil.validStrNull(address, CodeConst.CODE_PARAMETER_NOT_NULL,"详细地址不能为空");


			UserAddressDto userAddressDto = new UserAddressDto();
			userAddressDto.setUserId(userId);
			userAddressDto.setDefaultFlag(defaultFlag);
			userAddressDto.setUserName(userName);
			userAddressDto.setMobile(mobile);
			userAddressDto.setProvinceId(provinceId);
			userAddressDto.setCityId(cityId);
			userAddressDto.setDistrictId(districtId);
			userAddressDto.setTownId(townId);
			userAddressDto.setAddress(address);
			return userAddressDto;
		}
		
		/**
		 * 设置地址信息，包含userId和addressId
		 * @param request
		 * @return
		 * @throws Exception
		 */
		private UserAddressDto setAddressInfo(HttpServletRequest request)
				throws Exception {
			String uid = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
			CommonValidUtil.validStrNull(uid, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			//addressId
			String aid = RequestUtils.getQueryParam(request, "addressId");
			CommonValidUtil.validStrNull(aid, CodeConst.CODE_PARAMETER_NOT_NULL,"addressId不能为空");
			Long userId = NumberUtil.strToLong(uid, "userId");
			Long addressId = NumberUtil.strToLong(aid, "addressId");
			
			UserAddressDto userAddressDto = new UserAddressDto();
			userAddressDto.setUserId(userId);
			userAddressDto.setAddressId(addressId);
			return userAddressDto;
		}
		
}
