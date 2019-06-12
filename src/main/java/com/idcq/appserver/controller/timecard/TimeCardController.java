package com.idcq.appserver.controller.timecard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.TimeCardDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.goods.IGoodsSetService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * @ClassName: TimeCardController
 * @Description: 次卡管理
 * @author dengjihai
 * @date 2016年2月16日
 */
@Controller
public class TimeCardController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	IGoodsServcie goodsServcie;
	@Autowired
	IGoodsSetService goodsSetService;

	/** 
	* @Title: TimeCardController.java
	* @Description: 获取店铺次卡
	* @param @param request
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	@RequestMapping(value = {"/goods/getShopGoodsSet","/token/goods/getShopGoodsSet"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getShopGoodsSet(HttpServletRequest request) throws Exception {
		try{
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOP_ID_NULL);
			String goodsSetType = RequestUtils.getQueryParam(request, "goodsSetType"); // 套餐类型3000：套餐
																						// 4000：次卡
			String goodsSetName = RequestUtils.getQueryParam(request, "goodsSetName");// 套餐名称
			String pNo = RequestUtils.getQueryParam(request, "pNo");
			String pSize = RequestUtils.getQueryParam(request, "pSize");
			if(StringUtils.isEmpty(pNo)){
				pNo="1";
			}
			if(StringUtils.isEmpty(pSize)){
				pSize="20";
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopId", shopId);
			if(StringUtils.isEmpty(goodsSetType)){
				param.put("goodsSetType", "3000");//默认值是3000
			}else{
				param.put("goodsSetType", goodsSetType);
			}
			param.put("goodsSetName", goodsSetName);
			param.put("goodsStatus", 1);//状态为上架的
			PageModel pageModel = goodsServcie.getShopGoodsList(param, PageModel.handPageNo(pNo),
					PageModel.handPageSize(pSize));
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setrCount(pageModel.getTotalItem());
			msgList.setLst(pageModel.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺次卡列表成功！", msgList);
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("添加套餐接口    -异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加套餐接口    -系统异常", e);
			throw new APISystemException("添加套餐接口    -系统异常", e);
		} 
	}

	/**
	 * @Title: TimeCardController.java @Description: 新增套餐（次卡） @param @param
	 * request @param @param entity @param @return @param @throws
	 * Exception @throws
	 */
	@RequestMapping(value = {"/goods/updateGoodsSet","/token/goods/updateGoodsSet"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addTimeCard(HttpServletRequest request, HttpEntity<String> entity) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("添加套餐-start"+entity.toString());
			Map<String, Object> result = new HashMap<String, Object>();
			TimeCardDto goodsDto=JSON.parseObject(entity.getBody(),new TypeReference<TimeCardDto>(){});
			logger.info("goodsDto参数：" + goodsDto);
			String goodsSetName = goodsDto.getGoodsSetName();// 套餐名称
			Integer goodsSetType = goodsDto.getGoodsSetType();// 套餐类型
			Double standardPrice = goodsDto.getStandardPrice();// 套餐金额
			String pinyinCode = goodsDto.getPinyinCode();// 速记码
			Long goodsId = goodsDto.getGoodsId();//
			String unitName = goodsDto.getUnitName();//计量单位名称
			Integer validityTerm =goodsDto.getValidityTerm();//有效期，月数
			// 校验套餐名称
			CommonValidUtil.validStrNull(goodsSetName, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_SET_NAME_NULL);
			// 校验套餐类型
			CommonValidUtil.validIntNull(goodsSetType, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_SET_TYPE_NULL);
			// 校验套餐价格
			CommonValidUtil.validDoubleNull(standardPrice, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_STANDARDPRICE_NULL);
			// 校验速记码
			CommonValidUtil.validStrNull(pinyinCode, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_PINYIN_CODE_NULL);
			// 校验计量单位
			CommonValidUtil.validStrNull(unitName, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_UNITNAME_NULL);
			// 校验套餐内的商品列表
			List<GoodsSetDto> goodsSetDtoList = goodsDto.getGoodsList();
			CommonValidUtil.validListNull(goodsSetDtoList, CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_GOODS_LIST_NULL);
			;
			// 添加套餐（新增次卡）
			goodsDto.setGoodsName(goodsSetName);
			goodsDto.setGoodsType(goodsSetType);
			goodsDto.setStandardPrice(standardPrice);
			goodsDto.setValidityTerm(validityTerm);
			Long goodsSetId = goodsServcie.updateGoodsDtO(goodsDto, goodsSetDtoList);
			result.put("goodSetId", goodsSetId);
			if (CommonValidUtil.isEmpty(goodsId)) {
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "添加套餐成功", result);
			} else {
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "修改套餐成功", result);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("添加套餐接口    -异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加套餐接口    -系统异常", e);
			throw new APISystemException("添加套餐接口    -系统异常", e);
		} finally {
			logger.info("添加套餐接口共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
		}
	}

}
