package com.idcq.appserver.controller.shopMemberCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 店铺会员卡
 * @ClassName: ShopMemberCardControlelr 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 上午10:27:33 
 *
 */
@Controller
public class ShopMemberCardController {
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 会员卡
	 */
	@Autowired
	private IShopMemberCardService shopMemberCardService;
	@Autowired
	private ISendSmsService sendSmsService;
	
	/**
	 * 修改店铺会员卡
	 * @Title: updateShopMemberCard 
	 * @param @param request
	 * @param @param hashMap
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	//last update time by dengjh 20160217
	@RequestMapping(value={"/updateShopMemberCard","/token/pay/updateShopMemberCard","/session/pay/updateShopMemberCard","/service/pay/updateShopMemberCard"},produces="application/json;charset=utf-8",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody String updateShopMemberCard(HttpServletRequest request,@RequestBody Map<String, String> hashMap){
		try{
			logger.info("PCP40：添加/修改店内会员卡接口 -- start");
			String shopIdStr = hashMap.get("shopId");
			String mobile=hashMap.get("mobile");
			String sex=hashMap.get("sex");
			String veriCode =hashMap.get("veriCode");
			String billerId=hashMap.get("billerId");
			//商铺编号验证
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			//会员卡手机号验证
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPMC_MOBILE);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			//如果验证码不为空，则需要校验验证码
			if (!StringUtils.isBlank(veriCode)) {
				boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode);
				if (!flag) {
					throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
				}
			}
			boolean isExist=shopMemberCardService.checkCardExist(shopId,mobile);
			ShopMemberCardDto shopMemberCardDto=new ShopMemberCardDto();
			String cardType=hashMap.get("cardType");
			//如果 是次卡
			if(cardType!=null&&cardType.equals("2")){
				String goodsSetId=hashMap.get("goodsSetId");//购买套餐id
				CommonValidUtil.validStrNull(goodsSetId, CodeConst.CODE_PARAMETER_NOT_NULL, "购买套餐ID不能为空");
				String startDate=hashMap.get("startDate");
				String endDate =hashMap.get("endDate");
				CommonValidUtil.validStrNull(startDate, CodeConst.CODE_PARAMETER_NOT_NULL, "套餐开始日期不能为空");
				CommonValidUtil.validStrNull(endDate, CodeConst.CODE_PARAMETER_NOT_NULL, "套餐结束日期不能为空");
				shopMemberCardDto.setValidStartTime(DateUtils.stringToDate(startDate));
				shopMemberCardDto.setValidEndTime(DateUtils.stringToDate(endDate));
				shopMemberCardDto.setGoodsSetId(goodsSetId);
				isExist=false; //  开次卡，可购买重复的套餐id
			}
			shopMemberCardDto.setBillerId(billerId);
			shopMemberCardDto.setCardType(cardType);
			if(!isExist){//会员卡不存在
				String billId=hashMap.get("billId");
				if(StringUtils.isEmpty(billId)){
					billId="0";
				}
				if(StringUtils.isEmpty(sex)){
					sex="2";
				}
				shopMemberCardDto.setMobile(hashMap.get("mobile"));
				shopMemberCardDto.setShopId(shopId);
				shopMemberCardDto.setSex(Integer.parseInt(sex));
				shopMemberCardDto.setName(hashMap.get("userName"));
				shopMemberCardDto.setBirthday(hashMap.get("birthday"));
				shopMemberCardDto.setOperaterId(Integer.parseInt(billId));
				Integer cardId=shopMemberCardService.insertShopMemberCard(shopMemberCardDto);//次卡id
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("carId", cardId);
				return ResultUtil.getResultJsonStr(0, "成功添加店铺会员卡",map);
			}else{
				if(StringUtils.isEmpty(sex)){
					sex="2";
				}
				shopMemberCardDto.setMobile(hashMap.get("mobile"));
				shopMemberCardDto.setShopId(shopId);
				shopMemberCardDto.setSex(Integer.parseInt(sex));
				shopMemberCardDto.setName(hashMap.get("userName"));
				shopMemberCardDto.setBirthday(hashMap.get("birthday"));
				shopMemberCardService.updateShopMemberCard(shopMemberCardDto);
				return ResultUtil.getResultJsonStr(0, "成功修改店铺会员卡",new HashMap());
			}
		} catch (ServiceException e){
			logger.error("操作店铺会员卡异常 ",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("操作店铺会员卡异常",e);
			throw new APISystemException("操作店铺会员卡异常", e);
		}
	}
	
	/**
	 * 店铺会员卡充值
	 * @Title: chargeShopMemberCard 
	 * @param @param hashMap
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value={"/token/pay/chargeShopMemberCard","/session/pay/chargeShopMemberCard","/service/pay/chargeShopMemberCard"},produces="application/json;charset=utf-8",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody String chargeShopMemberCard(HttpServletRequest request, @RequestBody Map<String, String> hashMap){
		try{
		    logger.info("请求参数：" + hashMap.toString());
			String shopIdStr = hashMap.get("shopId");
			String mobile=hashMap.get("mobile");
			String chargeMoney=hashMap.get("chargeMoney");
			String presentMoney=hashMap.get("presentMoney");
			String billerId=hashMap.get("billerId");
			String opertaterIdStr=hashMap.get("opertaterId");
			//变更，做兼容处理
			if(StringUtils.isBlank(billerId)){
			    billerId=hashMap.get("billId");
			}
			String payType=hashMap.get("payType");
			String isSendSmsStr=hashMap.get("isSendSms");
			String cardBillDesc=hashMap.get("cardBillDesc");
			String clientRechargeTime = hashMap.get("clientRechargeTime");
			//商铺编号验证
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
			//会员卡手机号验证
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPMC_MOBILE);
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
			CommonValidUtil.validStrNull(chargeMoney, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPMC_CHARGE_MONEY);//验证充值金额
			CommonValidUtil.validStrNull(payType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYTYPE);
			if(StringUtils.isEmpty(presentMoney)){//赠送金额不能为空
				presentMoney="0";
			}
			if(StringUtils.isBlank(billerId)){
			    billerId="0";
			}
			Integer isSendSms = null;
			if(isSendSmsStr != null){
				CommonValidUtil.validStrIntFmt(isSendSmsStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ISSENDSMS);
				isSendSms = Integer.valueOf(isSendSmsStr);
				if(isSendSms!=CommonConst.SEND_SMS_FALSE && isSendSms != CommonConst.SEND_SMS_TRUE){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_ISSENDSMS);
				}
			}
			if(StringUtils.isBlank(opertaterIdStr)) {
			    opertaterIdStr = billerId;
			}
			Double presentMoneyNum=NumberUtil.strToDouble(presentMoney, "presentMoney");//赠送金额
			Double chargeMoneyNum=NumberUtil.strToDouble(chargeMoney, "chargeMoney");//充值金额
			Integer opertaterId=NumberUtil.strToInteger(opertaterIdStr, "opertaterId");
			ShopMemberCardDto shopMemberCardDto=new ShopMemberCardDto();
			shopMemberCardDto.setMobile(mobile);
			shopMemberCardDto.setChargeMoney(chargeMoneyNum);
			shopMemberCardDto.setPresentMoney(presentMoneyNum);
			shopMemberCardDto.setShopId(shopId);
			shopMemberCardDto.setOperaterId(opertaterId);
			shopMemberCardDto.setIsSendSms(isSendSms);
			shopMemberCardDto.setCardDesc(cardBillDesc);
			shopMemberCardDto.setBillerId(billerId);
			shopMemberCardDto.setClientRechargeTime(DateUtils.parse(clientRechargeTime,DateUtils.DATETIME_FORMAT));
			shopMemberCardDto.setPayType(NumberUtil.strToInteger(payType, "payType"));
			Map<String,Object> result = shopMemberCardService.chargeShopMemberCard(shopMemberCardDto);
			return ResultUtil.getResultJsonStr(0, "店内会员卡充值成功",result);
		} catch (ServiceException e){
			logger.error("操作店铺会员卡异常 ",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("操作店铺会员卡异常",e);
			throw new APISystemException("操作店铺会员卡异常", e);
		}
	}
	
	/**
	 * 
	 * @Title: getShopMemberCards 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value={"/getShopMemberCards","/token/pay/getShopMemberCards","/session/pay/getShopMemberCards","/service/pay/getShopMemberCards"},produces="application/json;charset=utf-8",method=RequestMethod.GET)
	public @ResponseBody String getShopMemberCards(HttpServletRequest request){
		try{
			String shopId=RequestUtils.getQueryParam(request,"shopId");
			String userId=RequestUtils.getQueryParam(request,"userId");
			String mobile=RequestUtils.getQueryParam(request, "mobile");
			String pNo=RequestUtils.getQueryParam(request, "pNo");
			String pSize=RequestUtils.getQueryParam(request, "pSize");
			String cardType =RequestUtils.getQueryParam(request, "cardType");
			String birthday=RequestUtils.getQueryParam(request, "birthday");
			if(StringUtils.isEmpty(pNo)){
				pNo="1";
			}
			if(StringUtils.isEmpty(pSize)){
				pSize="10";
			}
			ShopMemberCardDto shopMemberCardDto=new ShopMemberCardDto();
			shopMemberCardDto.setMobile(mobile);
			shopMemberCardDto.setCardType(cardType);
			if(StringUtils.isNotBlank(shopId)) {
			    shopMemberCardDto.setShopId(NumberUtil.strToLong(shopId, "shopId"));
			}
			if(StringUtils.isNotBlank(userId)) {
			    shopMemberCardDto.setUserId(NumberUtil.strToLong(userId, "userId"));
			}
			shopMemberCardDto.setBirthday(birthday);
			PageModel pageModel=new PageModel();
			pageModel.setToPage(Integer.parseInt(pNo));
			pageModel.setPageSize(Integer.parseInt(pSize));
			pageModel=shopMemberCardService.queryShopMemberCardList(shopMemberCardDto, pageModel);
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			dataList.add(msgList);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询店铺会员卡成功",
					msgList, DateUtils.DATETIME_FORMAT);
		} catch (ServiceException e){
			logger.error("查询店铺会员卡异常 ",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("查询店铺会员卡异常",e);
			throw new APISystemException("查询店铺会员卡异常", e);
		}
	}
	
}
