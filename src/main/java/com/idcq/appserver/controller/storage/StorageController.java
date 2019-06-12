package com.idcq.appserver.controller.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.storage.OperateShopStorageDto;
import com.idcq.appserver.dto.storage.OperateStorageCheckDto;
import com.idcq.appserver.dto.storage.StorageCheckGoodsDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.ArrayUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
public class StorageController {
		private Log logger  = LogFactory.getLog(getClass());
		
		@Autowired
		private IShopServcie shopService;
		@Autowired
		private IStorageServcie storageServcie;
		@Autowired
		private IShopServcie shopServcie;
		
		@RequestMapping(value = {"/session/storage/operateShopStorage","/token/storage/operateShopStorage", "/service/storage/operateShopStorage"}, 
				method = RequestMethod.POST, consumes = "application/json")
		@ResponseBody
		public ResultDto operateShopStorage(HttpEntity<String> entity,
				HttpServletRequest request) {
			try {
				/*vender	string		否	供应商
				buyer	string		否	采购员[增加字段]*/
				logger.info("PCS25：商铺商品出入库接口-start");
				OperateShopStorageDto operateShopStorage = (OperateShopStorageDto) JacksonUtil.postJsonToObj(entity,
						OperateShopStorageDto.class, DateUtils.DATETIME_FORMAT);
				
				/**校验参数-start**/
				
				Long shopId = operateShopStorage.getShopId();
				// 验证商铺
				if(null == shopId){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
				}
				int flag = this.shopService.queryShopExists(shopId);
				CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOP);
				
				//操作人
				Long operaterId  = operateShopStorage.getOperaterId();
				if(operaterId == null){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"operaterId不能为null");
				}
				String operaterName = operateShopStorage.getOperaterName();
				if(StringUtils.isBlank(operaterName)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"operaterName不能为null");
				}
				
				//操作类型
				Integer storageType = operateShopStorage.getStorageType();
				if(null == storageType){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "storageType不能为空");
				}
				
				//时间
				String storageTime = operateShopStorage.getStorageTime();
				if(StringUtils.isBlank(storageTime)){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "storageTime不能为空");
				}
				if(operateShopStorage!=null && operateShopStorage.getStorageNo()!=null){
					if(storageServcie.isStorageNoExist(operateShopStorage.getStorageNo(),shopId)){
						throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_REQUIRED_STORAGENO);
					}
				}
				storageServcie.operateShopStorage(operateShopStorage);
				/**校验参数-end**/
				logger.info("PCS25：商铺商品出入库接口-start");
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！", null);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,
					CodeConst.MSG_JSON_ERROR);
		} catch (Exception e) {
			logger.error("PCS25：商铺商品出入库接口失败！", e);
			throw new APISystemException("PCS25：商铺商品出入库接口失败！", e);
		}

		}
		
		/**
		 * 
		 * 4.1.13PCS26：查询商品库存变动明细接口[V4.7新增接口]
		 * 
		 * @Description:
		 *
		 * @param request
		 * @return
		 */
	@RequestMapping(value = {"/service/storage/getShopStorageDetail","/token/storage/getShopStorageDetail",
			"/session/storage/getShopStorageDetail"}, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getShopStorageDetail(HttpServletRequest request) {
		try {
          /*
  			shopId	int		是	商铺ID
			token	string		条件	商铺令牌，收银机调用时必传
			goodsId	string		否	商品ID
			querydata	string		否	商品名称、条码、拼音码支持模糊查询
			goodsStatus	int		否	上架状态，下架-0,上架-1
			bizType 	int		条件	业务类型
			出入库=17,盘点=18
			isGroupBy	int	0	否	是否需要根据queryNo分组查询
			1=是，0=否。
			如果=1，bizType必填
			changeType	int		否	库存变动类型：
			bizType=17时：库存变更类型:11=进货入库，12=其他入库，13=销售出库，14=其他出库
			bizType=18时：盘点类型:盘盈=1,盘亏=-1
			bizNo	sting		否	单号，出入库单号或盘点单号，支持模糊查询
			startTime	date		否	查询开始时间
			endTime	date		否	查询结束时间
			pNo	int	1	否	页码,从第1页开始
			pSize	int	10	否	每页记录数
		  */
			logger.info("PCS26：查询商品库存变动明细-start" + request.getQueryString());

			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String goodsIdStr = RequestUtils.getQueryParam(request, "goodsId");
			String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");
			String bizTypeStr = RequestUtils.getQueryParam(request, "bizType");
			String isGroupByStr = RequestUtils.getQueryParam(request, "isGroupBy");
			String changeTypeStr = RequestUtils.getQueryParam(request, "changeType");
			String querydata = RequestUtils.getQueryParam(request, "querydata");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String bizNo = RequestUtils.getQueryParam(request, "bizNo");
			String pNo = RequestUtils.getQueryParam(request, "pNo");
			String pSize = RequestUtils.getQueryParam(request, "pSize");

			Map<String, Object> parms = new HashMap<String, Object>();

			// 校验参数
			if(StringUtils.isNotBlank(goodsIdStr)){
				Long goodsId = NumberUtil.strToLong(goodsIdStr, "goodsId");
				parms.put("goodsId", goodsId);
			}
			if(StringUtils.isNotBlank(bizTypeStr)){
				Long bizType = NumberUtil.strToLong(bizTypeStr, "bizType");
				parms.put("bizType", bizType);
			}
			if(StringUtils.isNotBlank(isGroupByStr)){
				Integer isGroupBy = NumberUtil.strToNum(isGroupByStr, "isGroupBy");
				parms.put("isGroupBy", isGroupBy);
				//是否需要根据queryNo分组查询1=是，0=否。如果=1，bizType必填
				if(isGroupBy==1){
		            CommonValidUtil.validStrNull(bizTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "bizType不能为空");
				}
			}
			else{
				parms.put("isGroupBy", 0);//默认不分组
			}
			if(StringUtils.isNotBlank(changeTypeStr)){
			    String[] changeType = ArrayUtil.toArray(changeTypeStr, null);
				parms.put("changeType", changeType);
			}

			if (!StringUtils.isBlank(startTime)
					&& !StringUtils.equals(startTime, "null")) {
				DateUtils.validDateStr(startTime, DateUtils.DATE_FORMAT);
				startTime = startTime + " 00:00:00";
				parms.put("startTime", startTime);
			}
			if (!StringUtils.isBlank(endTime)
					&& !StringUtils.equals(endTime, "null")) {
				DateUtils.validDateStr(endTime, DateUtils.DATE_FORMAT);
				endTime = endTime + " 23:59:59";
				parms.put("endTime", endTime);
			}

			// 验证商铺
			if (StringUtils.isNotEmpty(shopIdStr)) {
				Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
						CodeConst.CODE_PARAMETER_NOT_VALID,
						CodeConst.MSG_FORMAT_ERROR_SHOPID);
				int flag = this.shopServcie.queryShopExists(shopId);
				CommonValidUtil.validPositInt(flag,
						CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_MISS_SHOP);
				parms.put("shopId", shopId);
			}
			if (StringUtils.isNotBlank(goodsStatus)) {
				parms.put("goodsStatus", NumberUtil.strToNum(goodsStatus, "goodsStatus"));
			}
			
			parms.put("querydata", querydata);
			parms.put("bizNo", bizNo);
			parms.put("pSize", PageModel.handPageSize(pSize));
			parms.put("limit",(PageModel.handPageNo(pNo) - 1) * PageModel.handPageSize(pSize));

			PageModel pageModel = storageServcie.getShopStorageDetail(parms);
			
			Map<String, Object> rltMap = new HashMap<String, Object>();
			rltMap.put("pNo", PageModel.handPageNo(pNo));
			rltMap.put("rCount", pageModel.getTotalItem());
			rltMap.put("lst", pageModel.getList());

			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,
					"PCS26：查询商品库存变动明细！", rltMap,DateUtils.DATETIME_FORMAT);

		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		}        catch (JsonMappingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
		catch (Exception e) {
			e.printStackTrace();
			logger.error("PCS26：查询商品库存变动明细-异常", e);
			throw new APISystemException("PCS26：查询商品库存变动明细-异常", e);
		}
	}
	@RequestMapping(value = {"/session/storage/operateStorageCheck","/token/storage/operateStorageCheck", "/service/storage/operateStorageCheck"}, 
			method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResultDto operateStorageCheck(HttpEntity<String> entity,
			HttpServletRequest request) {
		try {
			
/*			字段名	数据类型	默认值	必填项 	备注
			shopId	int		是	商铺ID
			token	string		条件	商铺令牌，收银机调用时必传
			checkNo	sting		否	盘点单号,没有传值时服务端默认使用如下规则填值
			出库=PD+商铺ID+时间戳 ，
			operaterId	int		是	操作员ID，商铺老板传0
			operaterName	string		是	操作人
			checkTime	datetime		是	盘点时间
			remark	string		否	备注
			goodsList	list		是	商品list
			其中goodsList包括
			字段名	数据类型	默认值	必填项 	备注
			goodsId	string		是	商品ID
			checkNum	double		是	盘后库存数量
			storageCheckRemark	string		否	商品盘点备注
			storagAfterNumber	double		是	盘点后数量*/
			
			logger.info("PCS27：商铺商品盘点接口-start");
			OperateStorageCheckDto operateStorageCheckDto = (OperateStorageCheckDto) JacksonUtil.postJsonToObj(entity,
					OperateStorageCheckDto.class, DateUtils.DATETIME_FORMAT);
			
			/**校验参数-start**/
			
			Long shopId = operateStorageCheckDto.getShopId();
			// 验证商铺
			if(null == shopId){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			}
			int flag = this.shopService.queryShopExists(shopId);
			CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOP);
			
			
			//时间
			String checkTime = operateStorageCheckDto.getCheckTime();
			if(StringUtils.isBlank(checkTime)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "checkTime不能为空");
			}
			//操作人id
			Long operaterId = operateStorageCheckDto.getOperaterId();
			if(operaterId==null){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operaterId不能为空");
			}
			//操作人姓名
			String operaterName = operateStorageCheckDto.getOperaterName();
			if(StringUtils.isBlank(operaterName)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "operaterName不能为空");
			}
			//goodsList
			List<StorageCheckGoodsDto> goodsList = operateStorageCheckDto.getGoodsList();
			if(CollectionUtils.isEmpty(goodsList)){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "goodsList不能为空");
			}
			Long storageCheckId = storageServcie.operateStorageCheck(operateStorageCheckDto);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("storageCheckId", storageCheckId);
			/**校验参数-end**/
			logger.info("PCS27：商铺商品盘点接口-start");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作成功！",resultMap );
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new APIBusinessException(CodeConst.CODE_JSON_ERROR,
					CodeConst.MSG_JSON_ERROR);
		}
		catch(Exception e){
			logger.error("PCS27：商铺商品盘点接口！",e);
			throw new APISystemException("PCS27：商铺商品盘点接口！", e);
		}
		
	}
	/**
	 * 
	 * 4.1.13PCS26：查询商品库存变动明细接口[V4.7新增接口]
	 * 
	 * @Description:
	 *
	 * @param request
	 * @return
	 */
@RequestMapping(value = {"/service/storage/getShopStorageCheckDetail","/token/storage/getShopStorageCheckDetail",
		"/session/storage/getShopStorageCheckDetail"}, produces = "application/json;charset=UTF-8")
@ResponseBody
public Object getShopStorageCheckDetail(HttpServletRequest request) {
	try {
      /*
		shopId	int		是	商铺ID
		token	string		条件	商铺令牌，收银机调用时必传
		goodsId	int		否	商品ID
		querydata	string		否	商品名称、条码、拼音码、供货单位、盘点单号支持模糊查询
		storageCheckNo	string		否	盘点单号
		goodsStatus	int		否	上架状态，下架-0,上架-1
		storageCheckType	string		否	盘点类型:盘盈=1,盘亏=-1,盘准=0（数目符合）1，-1
		startTime	date		否	查询开始时间
		endTime	date		否	查询结束时间
		pNo	int	1	否	页码,从第1页开始
		pSize	int	10	否	每页记录数
 
	  */
		logger.info("PCS28：查询商铺盘点列表-start" + request.getQueryString());

		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String goodsIdStr = RequestUtils.getQueryParam(request, "goodsId");
		String goodsStatus = RequestUtils.getQueryParam(request, "goodsStatus");
		String storageCheckTypeStr = RequestUtils.getQueryParam(request, "storageCheckType");
		String querydata = RequestUtils.getQueryParam(request, "querydata");
		String startTime = RequestUtils.getQueryParam(request, "startTime");
		String endTime = RequestUtils.getQueryParam(request, "endTime");
		String storageCheckNo = RequestUtils.getQueryParam(request, "storageCheckNo");
		String pNo = RequestUtils.getQueryParam(request, "pNo");
		String pSize = RequestUtils.getQueryParam(request, "pSize");

		Map<String, Object> parms = new HashMap<String, Object>();

		// 校验参数
		if(StringUtils.isNotBlank(goodsIdStr)){
			Long goodsId = NumberUtil.strToLong(goodsIdStr, "goodsId");
			parms.put("goodsId", goodsId);
		}
		if(StringUtils.isNotBlank(storageCheckTypeStr)){
			parms.put("storageCheckType",  NumberUtil.strToLong(storageCheckTypeStr, "storageCheckType"));
		}

		if (!StringUtils.isBlank(startTime)
				&& !StringUtils.equals(startTime, "null")) {
			DateUtils.validDateStr(startTime, DateUtils.DATE_FORMAT);
			startTime = startTime + " 00:00:00";
			parms.put("startTime", startTime);
		}
		if (!StringUtils.isBlank(endTime)
				&& !StringUtils.equals(endTime, "null")) {
			DateUtils.validDateStr(endTime, DateUtils.DATE_FORMAT);
			endTime = endTime + " 23:59:59";
			parms.put("endTime", endTime);
		}

		// 验证商铺
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_FORMAT_ERROR_SHOPID);
			int flag = this.shopServcie.queryShopExists(shopId);
			CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_MISS_SHOP);
			parms.put("shopId", shopId);
		if (StringUtils.isNotBlank(goodsStatus)) {
			parms.put("goodsStatus", NumberUtil.strToNum(goodsStatus, "goodsStatus"));
		}
		
		parms.put("querydata", querydata);
		parms.put("storageCheckNo", storageCheckNo);
		parms.put("pSize", PageModel.handPageSize(pSize));
		parms.put("limit",(PageModel.handPageNo(pNo) - 1) * PageModel.handPageSize(pSize));

		PageModel pageModel = storageServcie.getShopStorageCheckDetail(parms);
		
		Map<String, Object> rltMap = new HashMap<String, Object>();
		rltMap.put("pNo", PageModel.handPageNo(pNo));
		rltMap.put("rCount", pageModel.getTotalItem());
		rltMap.put("lst", pageModel.getList());
		
		
		return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED,
				"查询库存盘点流水成功！", rltMap,DateUtils.DATETIME_FORMAT);

	} catch (ServiceException e) {
		throw new APIBusinessException(e);
	}        catch (JsonMappingException e)
    {
        e.printStackTrace();
        throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
    }
	catch (Exception e) {
		e.printStackTrace();
		logger.error("PCS28：查询商铺盘点列表-异常", e);
		throw new APISystemException("PCS28：查询商铺盘点列表-异常", e);
	}
}	
}
