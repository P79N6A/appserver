package com.idcq.appserver.controller.idgj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.shop.ShopController;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.order.IXorderService;
import com.idcq.appserver.service.shop.IShopBillService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 一点管家账单
 * @ClassName: AccountController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年11月24日 上午11:50:04 
 *
 */
@RequestMapping(value="idgj")
@Controller
public class AccountController {
	
	private static final Logger logger = Logger.getLogger(ShopController.class);
	
	@Autowired
	private IShopServcie shopService;
	
	
	@Autowired
	private IShopBillService shopBillService;
	
	@Autowired
	private IXorderService xOrderService;
	/**
	 * 一点管家获得店铺账单
	 * @Title: getIdgjShopBill 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="getIdgjShopBill",produces = "application/json;charset=UTF-8")
	public @ResponseBody String getIdgjShopBill(HttpServletRequest request){
		try{
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			String billType = RequestUtils.getQueryParam(request, "billType");
			String billStatus = RequestUtils.getQueryParam(request,
					"billStatus");
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String pNoStr = RequestUtils.getQueryParam(request, "pNo");
			String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
			Map<String, Object> map = new HashMap<String, Object>();
			if(!StringUtils.isEmpty(startTime)){
				startTime+=" 00:00:01";
			}
			if(!StringUtils.isEmpty(endTime)){
				endTime+=" 23:59:59";
			}
			// 验证商铺
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SHOPID);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_FORMAT_ERROR_SHOPID);
			// 商铺存在性
			int flag = shopService.queryShopExists(shopId);
			CommonValidUtil
					.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_MISS_SHOP);
			// 验证商铺
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SHOPID);
			// 商铺存在性
			CommonValidUtil
					.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_MISS_SHOP);
			map.put("shopId", shopId);
			// 分页默认20条，第一页
			Integer pNo = PageModel.handPageNo(pNoStr);
			Integer pSize = PageModel.handPageSizeFor20(pSizeStr);
			map.put("n", (pNo - 1) * pSize);
			map.put("m", pSize);
			// billType
			if (StringUtils.isNotBlank(billType)) {
				CommonValidUtil.validStrIntFormat(billType,
						CodeConst.CODE_PARAMETER_NOT_VALID, "billType格式错误");
				map.put("billType", billType);
			}
			// billStatus
			if (StringUtils.isNotBlank(billStatus)) {
				CommonValidUtil.validStrIntFormat(billStatus,
						CodeConst.CODE_PARAMETER_NOT_VALID, "billStatus格式错误");
				map.put("billStatus", billStatus);
			}
			// startTime
			if (StringUtils.isNotBlank(startTime)) {
				CommonValidUtil.validDateTimeFormat(startTime,
						CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
				map.put("startTime", startTime);
			}
			// endTime
			if (StringUtils.isNotBlank(endTime)) {
				CommonValidUtil.validDateTimeFormat(endTime,
						CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
				map.put("endTime", endTime);
			}
			PageModel pageModel = shopService.getIdgjShopBill(map);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("lst", pageModel.getList());
			resultMap.put("rCount", pageModel.getTotalItem());
			resultMap.put("pNo", pNo);
			logger.info("查询商铺账单成功-end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
					"查询商铺账单成功！", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询商铺账单成功-系统异常", e);
			throw new APISystemException("查询商铺账单成功-系统异常", e);
		}
	}
	
	
	/**
	 * 获得一点管家账务统计
	 * @Title: getIdcqBillStatistics 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="getIdcqBillStatistics",produces = "application/json;charset=UTF-8")
	public @ResponseBody String getIdcqBillStatistics(HttpServletRequest request){
		try{
			String startTime = RequestUtils.getQueryParam(request, "startTime");
			String endTime = RequestUtils.getQueryParam(request, "endTime");
			String shopId=RequestUtils.getQueryParam(request, "shopId");
			Map<String,Object>params=new HashMap<String,Object>();
			if(!StringUtils.isEmpty(startTime)){
				startTime+=" 00:00:01";
			}
			if(!StringUtils.isEmpty(endTime)){
				endTime+=" 23:59:59";
			}
			params.put("startTime",startTime);
			params.put("endTime",endTime);
			params.put("shopId",shopId);
			Map<String, Object>resultMap=shopService.getIdcqBillStatistics(params);
			logger.info("查询商铺账户基本信息-end");
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
					"统计账单成功！", resultMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询商铺账户基本信息-系统异常", e);
			throw new APISystemException("查询商铺账户基本信息-系统异常", e);
		}
		
	}
	
	/**
	 * 获取一点管家账单详情
	 * @Title: getIdcqOrderDetail 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="getIdcqBillDetail",produces = "application/json;charset=UTF-8")
	public @ResponseBody String getIdcqBillDetail(HttpServletRequest request){
		try{
			String billId=RequestUtils.getQueryParam(request, "billId");//账单详情
			String type=RequestUtils.getQueryParam(request, "type");//查询类型
			String shopIdStr=RequestUtils.getQueryParam(request, "shopId");//商铺编号
			if(CommonConst.BILL_SEARCH_ORDER_TYPE==Integer.parseInt(type)){//会员账单
				ShopBillDto shopBillDto=shopBillService.queryShopBillById(Integer.parseInt(billId));//
				if(shopBillDto!=null){
					if(CommonConst.BILL_TYPE_WITH_DRAW.equals(shopBillDto.getBillType())){//提现
						return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
								"查询提现账单详情成功！", shopBillService.queryDrawShopBillDto(shopBillDto));
					}else if(CommonConst.BILL_TYPE_RECOMMAND_REWARD.equals(shopBillDto.getBillType())){//推荐奖励
						return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
								"查询推荐奖励账单详情成功！", shopBillService.queryRecommandRewardBill(shopBillDto));
					}
					else if(CommonConst.BILL_TYPE_SALE.equals(shopBillDto.getBillType())){
						return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
								"查询收银账单详情成功！", shopBillService.queryCashierBill(shopBillDto));
					}
				}
			}else{//非会员账单
				XorderDto xorderDto=xOrderService.getXOrderById(billId);
				if(xorderDto!=null){
					xOrderService.getCashierBillDetail(xorderDto);
				}
			}
		return null;
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询商铺账户基本信息-系统异常", e);
			throw new APISystemException("查询商铺账户基本信息-系统异常", e);
		}
	}
		
}
