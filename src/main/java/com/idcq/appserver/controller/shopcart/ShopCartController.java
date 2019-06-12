package com.idcq.appserver.controller.shopcart;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.order.OrderShopResourceGoodDto;
import com.idcq.appserver.dto.shopcart.ShopCartDto;
import com.idcq.appserver.dto.shopcart.ShopCartItemDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shopcart.IShopCartServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 购物车controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午3:39:59
 */
@Controller
// @RequestMapping(value="/cart")
public class ShopCartController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	public IShopCartServcie shopCartServcie;
	@Autowired
	public IMemberServcie memberService;

	/**
	 * 分页购物车商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cart/getCart", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopCartList(HttpServletRequest request) {
		try {
			logger.info("分页获取购物车商品列表-start");
			String pageNO = RequestUtils.getQueryParam(request,
					CommonConst.PAGE_NO);
			String pageSize = RequestUtils.getQueryParam(request,
					CommonConst.PAGE_SIZE);
			String userId = RequestUtils.getQueryParam(request, "userId");
			String shopColumnId = RequestUtils.getQueryParam(request, "shopColumnId");	
			verifyParameter(userId, pageNO, pageSize);
			ShopCartItemDto item = new ShopCartItemDto();
			item.setUserId(Long.valueOf(userId));
			if(null!=shopColumnId){
				// shopColumnId参数不合法
				if (!NumberUtil.isNumeric(shopColumnId)) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
							CodeConst.MSG_ColumnId_FORMAT_ERROR);
				}
				item.setShopColumnId(Integer.valueOf(shopColumnId));
			}
			/*
			 * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
			 */
			PageModel pageModel = this.shopCartServcie.getGoodsListByCart(item,
					PageModel.handPageNo(pageNO),
					PageModel.handPageSize(pageSize));

			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GETCART,
					msgList);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取购物车列表-系统异常", e);
			throw new APISystemException("获取购物车商品列表-系统异常", e);
		}
	}

	/**
	 * 更新购物车
	 * 
	 * @param item
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cart/updateCart", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResultDto updateCart(HttpEntity<String> entity,
			HttpServletRequest request) {
		try {
			logger.info("更新购物车-start");
			ShopCartDto cart = (ShopCartDto)JacksonUtil.postJsonToObj(entity, ShopCartDto.class);
			this.shopCartServcie.updateCart(cart);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,
					CodeConst.MSG_SUCCEED_UPDATECART, null);
		} catch (JsonMappingException e){
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新购物车-系统异常", e);
			throw new APISystemException("更新购物车-系统异常", e);
		}
	}

	public static void main(String[] args) throws Exception {
		String jsonStr = "{\"shopId\":1,\"data\": {\"id\":11111,\"webBookId\":111,\"time\":\"2015-03-20 19:00:00\",\"eatTime\":\"2015-10-10,21:00-22:00\",\"pNum\":10,\"pName\":\"王小二\",\"mobile\":111111111,\"advance\":200,\"discount\":8.5,\"seatInfo\":[{\"seatNum\":1,\"seatCate\":\"包房\"}],\"bookInfo\":[{\"dishId\":1,\"num\":2}]}}";
		OrderShopResourceGoodDto sc = (OrderShopResourceGoodDto) new ObjectMapper().readValue(jsonStr,
				OrderShopResourceGoodDto.class);
		System.out.println(sc);
	}

	/**
	 * 验证userId、pNo、pSize参数合法性
	 * 
	 * @param userId
	 * @param pNo
	 * @param pSize
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void verifyParameter(String userId, String pNo, String pSize)
			throws Exception {
		if(null==userId){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MEMBER);
		}
		// userId参数不合法
		if (!NumberUtil.isNumeric(userId)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_FORMAT_ERROR);
		}
		// 验证userid是否为空
		if ((StringUtils.isEmpty(userId))) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MEMBER);
		}
		// 验证int
		CommonValidUtil.validPositLong(userId, CodeConst.CODE_PARAMETER_NOT_VALID,
				CodeConst.MSG_FORMAT_ERROR_USERID);
		// 验证用户是否存在
		UserDto user = memberService.getUserByUserId(Long.parseLong(userId));
		if (null == user) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_MEMBER);
		}
		// 验证int
		CommonValidUtil.validPositLong(userId, CodeConst.CODE_PARAMETER_NOT_VALID,
				CodeConst.MSG_FORMAT_ERROR_USERID);
		if (null == pNo) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_PNO_FMT_ERROR);
		}
		// pNo参数不合法
		if (!NumberUtil.isNumeric(pNo)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_PNO_FMT_ERROR);
		}
		if (null == pSize) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_PSIZE_FMT_ERROR);
		}
		// pSize参数不合法
		if (!NumberUtil.isNumeric(pSize)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_PSIZE_FMT_ERROR);
		}

	}

}
