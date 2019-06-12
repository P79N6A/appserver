package com.idcq.idianmgr.controller.goodsGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.goods.IShopGoodsService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.idianmgr.common.MgrCodeConst;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupHandleDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
import com.idcq.idianmgr.dto.goodsGroup.TmpGoodsGroupDto;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;

/**
 * 商品族
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value={"/goodsGroup", "/service/goodsGroup",
        "/token/goodsGroup","/session/goodsGroup"})
public class GoodsGroupController {
	
	private static final Logger logger = Logger.getLogger(GoodsGroupController.class);
	@Autowired
	public IGoodsGroupService goodsGroupService;
	@Autowired
    public IShopServcie shopServcie;
	@Autowired
	public IShopGoodsService shopGoodsService;
	/**
	 * MG8：查询商品族内商品价格
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getGoodsGroupGoodsPrice",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto getGoodsGroupGoodsPrice(HttpServletRequest request){
		long startTime = System.currentTimeMillis();
		try {
			logger.info("==================>>查询商品族内商品价格-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String goodsGroupIdStr = RequestUtils.getQueryParam(request, "goodsGroupId");
			//商铺编号参数校验
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			//商品族编号参数校验
			CommonValidUtil.validStrNull(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_GOODSGROUP_ID);
			Long goodsGroupId = CommonValidUtil.validStrLongFmt(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_GOODSGROUP_ID);
			List<Map> list = goodsGroupService.getGoodsGroupGoodsPrice(shopId,goodsGroupId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取信息成功", list);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询商品族内商品价格-系统异常", e);
			throw new APISystemException("查询商品族内商品价格-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * MG9：商品族内商品价格更新接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsPro",method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Object updateGoodsPro(HttpServletRequest request,HttpEntity<String> entity){
		long startTime = System.currentTimeMillis();
		try {
			logger.info("商品族内商品价格更新接口-start");
			TmpGoodsGroupDto tmpGoodsGroupDto = (TmpGoodsGroupDto) JacksonUtil.postJsonToObj(entity,TmpGoodsGroupDto.class);
			if (tmpGoodsGroupDto != null && tmpGoodsGroupDto.getGoods() != null && tmpGoodsGroupDto.getGoods().size() > 0) {
				goodsGroupService.updateGoodsPro(tmpGoodsGroupDto);
			}
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功", null);
		} catch (ServiceException e) {
			logger.error("商品族内商品价格更新-异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("商品族内商品价格更新-系统异常", e);
			throw new APISystemException("商品族内商品价格更新-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * 查询商品族属性
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getGoodsGroupPros", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getGoodsGroupPros(HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("查询商品族属性-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String goodsGroupIdStr = RequestUtils.getQueryParam(request, "goodsGroupId");
			List<Long> goodsGroupIds = new ArrayList<Long>();
			CommonValidUtil.validStrNull(shopIdStr,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			ShopDto shop = shopServcie.getShopById(shopId);
			CommonValidUtil.validObjectNull(shop,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_MISS_SHOP);
			if(StringUtils.isNotBlank(goodsGroupIdStr)) {
			    Long goodsGroupId = NumberUtil.strToLong(goodsGroupIdStr, "goodsGroupId");
			    goodsGroupIds.add(goodsGroupId);
			} else {
			    goodsGroupIds = shopGoodsService.getGoodsGroupIdByShopId(shopId);
			}
			
			Map param = new HashMap();
			param.put("shopId", shopId);
			List list = new ArrayList();;
			if(CollectionUtils.isNotEmpty(goodsGroupIds)) {
			    param.put("goodsGroupIds", goodsGroupIds);
			    list = goodsGroupService.getGoodsGroupPros(param);
			} 
			
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询商品族属性成功",list);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询商品族属性-系统异常", e);
			throw new APISystemException("查询商品族属性-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * 商品族属性更新
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operateGoodsGroupPro",method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes = "application/json;charset=UTF-8")
	@ResponseBody
	public Object operateGoodsGroupPro(HttpEntity<String> entity,HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("商品族属性更新-start");
			GoodsGroupPropertyDto goodsGroupPropertyDto = (GoodsGroupPropertyDto) JacksonUtil.postJsonToObj(entity,GoodsGroupPropertyDto.class);
			Long result=0L;
			if(null !=goodsGroupPropertyDto){
				result=goodsGroupService.operateGoodsGroupPro(goodsGroupPropertyDto);
			}
			Map resultMap=new HashMap();
			resultMap.put("groupPropertyId", result);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "商品族属性更新成功",resultMap);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商品族属性更新-系统异常", e);
			throw new APISystemException("商品族属性更新-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * 商品族属性值更新
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operateGoodsGroupProVal", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes = "application/json;charset=UTF-8")
	@ResponseBody
	public Object operateGoodsGroupProVal(HttpEntity<String> entity,HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("商品族属性值更新-start");
			GoodsGroupProValuesDto goodsGroupProValuesDto = (GoodsGroupProValuesDto) JacksonUtil.postJsonToObj(entity,GoodsGroupProValuesDto.class);
			Long result=0L;
			if(null !=goodsGroupProValuesDto){
				result=goodsGroupService.operateGoodsGroupProVal(goodsGroupProValuesDto);
			}
			Map resultMap=new HashMap();
			resultMap.put("proValuesId", result);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "商品族属性值更新成功",resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商品族属性值更新-系统异常", e);
			throw new APISystemException("商品族属性值更新-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * MG7：属性/属性值删除接口
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delGroupPro", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object delGroupPro(HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("属性/属性值删除-start");
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String bizType = RequestUtils.getQueryParam(request, "bizType");//业务类型：0-商品族属性，1-商品族属性值
			String bizId = RequestUtils.getQueryParam(request, "bizId");//商品族属性ID/属性值ID，当bizType为0时，此为商品族属性ID，当bizType为1时，此为属性值ID
			CommonValidUtil.validStrNull(shopId,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validPositInt(shopId,CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_SHOPID);
			CommonValidUtil.validStrNull(bizType,CodeConst.CODE_PARAMETER_NOT_NULL,MgrCodeConst.MSG_BIZ_TYPE_NOT_NULL);
			CommonValidUtil.validNumStr(bizType,CodeConst.CODE_PARAMETER_NOT_VALID,MgrCodeConst.MSG_FORMAT_ERROR_BIZ_TYPE);
			CommonValidUtil.validStrNull(bizId,CodeConst.CODE_PARAMETER_NOT_NULL,MgrCodeConst.MSG_BIZ_ID_NOT_NULL);
			CommonValidUtil.validPositInt(bizId,CodeConst.CODE_PARAMETER_NOT_VALID,MgrCodeConst.MSG_FORMAT_ERROR_BIZ_ID);
			Map param=new HashMap();
			param.put("shopId", shopId);
			param.put("bizType", bizType);
			param.put("bizId", bizId);
			goodsGroupService.delGroupPro(param);
			
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "属性/属性值删除成功",null, DateUtils.TIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("属性/属性值删除-系统异常", e);
			throw new APISystemException("商品族属性值更新-系统异常", e);
		}finally{
			logger.info("共耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		}
	}
	
	/**
	 * 添加/修改商品族
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsGroup", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8",consumes = "application/json")
	@ResponseBody
	public Object updateGoodsGroup(HttpEntity<String> entity) {
		try {
			logger.info("添加/修改商品族-start");
			GoodsGroupHandleDto goodsGroup = (GoodsGroupHandleDto) JacksonUtil.postJsonToObj(entity,GoodsGroupHandleDto.class);
			Long ggId = this.goodsGroupService.operateGoodsGroup(goodsGroup);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("goodsGroupId", ggId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "添加商品族成功！", map);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加/修改商品族-系统异常", e);
			throw new APISystemException("添加/修改商品族-系统异常", e);
		}
	}
	
	/**
	 * 更新服务状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsGroupStatus", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateGoodsGroupStatus(HttpServletRequest request) {
		try {
			logger.info("更新服务状态-start");
			this.goodsGroupService.updateGoodsGroupStatus(request);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "更新服务状态成功！", null);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新服务状态-系统异常", e);
			throw new APISystemException("更新服务状态-系统异常", e);
		}
	}
	
	/**
	 * 获取商铺服务列表（商品族列表）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getGoodsList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getGoodsList(HttpServletRequest request) {
		try {
			logger.info("获取商铺服务列表-start");
			String shopId = RequestUtils.getQueryParam(request, "shopId");
			String goodsKey = RequestUtils.getQueryParam(request, "goodsKey");
			String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");
			String pNo = RequestUtils.getQueryParam(request, "pNo");
			String pSize = RequestUtils.getQueryParam(request, "pSize");
			
			// 商铺ID必填
			CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validStrLongFormat(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			Integer status = null;
			if(!StringUtils.isBlank(goodsStatus)){
				CommonValidUtil.validStrIntFormat(goodsStatus, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_EEROR_GG_STATUS);
				status = Integer.valueOf(goodsStatus);
			}
			PageModel pm = this.goodsGroupService.getGoodsGroupList(Long.valueOf(shopId), goodsKey, 
					status,PageModel.handPageNo(pNo), PageModel.handPageSize(pSize));
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pm.getToPage());
			msgList.setpSize(pm.getPageSize());
			msgList.setrCount(pm.getTotalItem());
			msgList.setLst(pm.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺服务列表成功！", msgList);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,CodeConst.MSG_JSON_ERROR);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取商铺服务列表-系统异常", e);
			throw new APISystemException("获取商铺服务列表-系统异常", e);
		}
	}
}
