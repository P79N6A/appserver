package com.idcq.appserver.controller.shopMemberLevel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shop.ShopMemberLevelDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.ShopMemberLevel.IShopMemberLevelService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class ShopMemberLevelController {
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private IShopMemberLevelService shopMemberLevelService;
	@Autowired
	private ICollectService collectService;
    
	 /**
     * PSM12：新增/编辑店内会员等级接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/updateShopMemberLevel", "/token/shopMember/updateShopMemberLevel",
                    "/session/shopMember/updateShopMemberLevel"}, method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateShopMemberLevel(HttpEntity<String> entity, HttpServletRequest request) {
        logger.info("新增/编辑店内会员等级接口-start");
        try {
            ShopMemberLevelDto shopMemberLevelDto = (ShopMemberLevelDto) JacksonUtil.postJsonToObj(entity, ShopMemberLevelDto.class,
                    DateUtils.DATE_FORMAT);
            if(shopMemberLevelDto == null){
            	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "编辑店内会员等级参数不能为空");
            }else{
            	if(shopMemberLevelDto.getIsDelete() == null){
            		shopMemberLevelDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
            	}
            	shopMemberLevelDto.setCreateTime(new Date());
            }
            CommonValidUtil.validLongNull(shopMemberLevelDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 校验商铺是否存在
            collectService.checkShopExists(shopMemberLevelDto.getShopId());
            CommonValidUtil.validStrNull(shopMemberLevelDto.getShopMemberLevelName(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_SHOPMEMBERLEVELNAME_NOT_NULL);
            CommonValidUtil.validDoubleNull(shopMemberLevelDto.getConsumeMinAmount(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_CONSUMEMINAMOUNT_NOT_NULL);
            CommonValidUtil.validDoubleNull(shopMemberLevelDto.getDiscount(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_DISCOUNT_NOT_NULL);
            CommonValidUtil.validIntNull(shopMemberLevelDto.getIsAutoUpgrate(), CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_ISAUTOUPGRATE_NOT_NULL);
            Map<String, Integer> rusultMap = shopMemberLevelService.updateShopMemberLevel(shopMemberLevelDto);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "编辑店内会员等级成功", rusultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("新增/编辑店内会员等级接口-系统异常", e);
            throw new APISystemException("新增/编辑店内会员等级接口-系统异常", e);
        }
    }
    
    /**
     *	PSM13：查询店内会员等级列表接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/getShopMemberLevelList", "/token/shopMember/getShopMemberLevelList",
                    "/session/shopMember/getShopMemberLevelList"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopMemberLevelList(HttpServletRequest request) {
        logger.info("查询店铺内会员信息列表接口-start" + request.getQueryString());
        try {
        	Map<String,Object> paramMap = new HashMap<String, Object>();
        	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        	String shopMemberLevelName = RequestUtils.getQueryParam(request, "shopMemberLevelName");
        	String consumeAmountStr = RequestUtils.getQueryParam(request, "consumeAmount");
        	String discountStr = RequestUtils.getQueryParam(request, "discount");
        	String isAutoUpgrateStr = RequestUtils.getQueryParam(request, "isAutoUpgrate");
        	String isSearchMemberNumStr = RequestUtils.getQueryParam(request, "isSearchMemberNum");
        	Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "店铺Id类型错误");
        	paramMap.put("shopId", String.valueOf(shopId));
        	paramMap.put("shopMemberLevelName", shopMemberLevelName);
        	if(consumeAmountStr != null){
        		CommonValidUtil.validStrDoubleFmt(consumeAmountStr, CodeConst.CODE_PARAMETER_NOT_VALID, "消费金额格式不正确");
        		paramMap.put("consumeAmount", consumeAmountStr);
        	}
        	if(discountStr != null){
        		CommonValidUtil.validStrDoubleFmt(discountStr, CodeConst.CODE_PARAMETER_NOT_VALID, "店内会员优惠折扣比例格式不正确");
        		paramMap.put("discount", discountStr);
        	}
        	
        	if(isAutoUpgrateStr != null){
        		CommonValidUtil.validStrIntFmt(isAutoUpgrateStr, CodeConst.CODE_PARAMETER_NOT_VALID, "是否允许自动升级格式不正确");
        		paramMap.put("isAutoUpgrate", isAutoUpgrateStr);
        	}
        	Integer isSearchMemberNum = 0;
        	if(isSearchMemberNumStr != null){
        		CommonValidUtil.validStrIntFmt(isSearchMemberNumStr, CodeConst.CODE_PARAMETER_NOT_VALID, "是否查询等级下的店内会员数格式不正确");
        		isSearchMemberNum = Integer.valueOf(isSearchMemberNumStr);
        	}
        	paramMap.put("isSearchMemberNum", isSearchMemberNum);
        	paramMap.put("isDelete", CommonConst.IS_DELETE_FALSE);
        	
        	Map<String,Object> resultMap = shopMemberLevelService.getShopMemberLevelList(paramMap);
//            responseData.put("lst", resultList);
//            responseData.put("rCount", shopMemberService.queryShopMemberCount(requestMap));
//            responseData.put("pNo", requestMap.get("pNo"));
        	return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询店内会员等级列表成功！", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询店内会员等级列表接口-系统异常", e);
            throw new APISystemException("查询店内会员等级列表接口-系统异常", e);
        }
    }
    
    /**
     * PSM14：删除店内会员等级接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/operateShopMemberLevel", "/token/shopMember/operateShopMemberLevel",
                    "/session/shopMember/operateShopMemberLevel"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object operateShopMemberLevel(HttpServletRequest request) {
        logger.info("删除店内会员等级接口-start");
        try {
        	Map<String,Object> paramMap = new HashMap<String, Object>();
        	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        	String shopMemberLevelIdStr = RequestUtils.getQueryParam(request, "shopMemberLevelId");
        	String operateTypeStr = RequestUtils.getQueryParam(request, "operateType");
        	
        	CommonValidUtil.validStrNull(shopMemberLevelIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_LEVEL_ID_NOT_NULL);
        	if(shopIdStr!=null){
    			CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
    		}
    		CommonValidUtil.validStrNull(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_OPERATETYPE_NOT_NULL);
    		CommonValidUtil.validStrLongFmt(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
    		if(Integer.valueOf(operateTypeStr)!=CommonConst.OPERATE_TYPE_DELETE 
    				&& Integer.valueOf(operateTypeStr)!=CommonConst.OPERATE_TYPE_ADD
    				&& Integer.valueOf(operateTypeStr)!=CommonConst.OPERATE_TYPE_UPDATE){
    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_OPERATETYPE);
    		}
    		int count = shopMemberLevelService.deleteShopMemberLevel(shopIdStr,shopMemberLevelIdStr,operateTypeStr);
        	 if(count>0){
        		 return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除店内会员等级成功",null);
        	 }else{
        		 return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除店内会员等级失败",null);
        	 }
            
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("删除店内会员等级接口-系统异常", e);
            throw new APISystemException("删除店内会员等级接口-系统异常", e);
        }
    }
    
    
}
