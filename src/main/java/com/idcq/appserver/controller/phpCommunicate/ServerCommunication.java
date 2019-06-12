package com.idcq.appserver.controller.phpCommunicate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.AsynchronousTask.producer.MqPusher;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.commonconf.UserFeedbackDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.commonconf.IUserFeedbackService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.SessionUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.mq.MqProduceApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.utils.solr.SolrOperateService;

/**
 * 与php进行通信
* @ClassName: ServerCommunication 
* @Description: TODO(与php所有的通信接口方法) 
* @author 张鹏程 
* @date 2015年4月13日 上午10:58:28 
*
 */
@Controller
public class ServerCommunication {
	
	private Log  logger=LogFactory.getLog(getClass());
	@Autowired
	private  IShopServcie shopService;
	@Autowired
	private IOrderServcie orderServcie;
	
	/**
	 * 推送
	 */
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	public IShopDao shopDao;
	/**
	 * 用户
	 */
	@Autowired
	private IUserFeedbackService userFeedBackService;
	
	/**
	 * 会员
	 */
	@Autowired
	private IMemberServcie memberService;
	
	@Autowired
	public IPushService pushService;
	@Autowired
	private IGoodsServcie goodsService;
	
	@Autowired
	private IShopAccountDao shopAccountDao;
	
	@Autowired
	private IShopBillDao shopBillDao;
	@Autowired
	private ILevelService levelService;
	/**
	 * 店铺审核通过后推送
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/shopAudit",produces="application/json;charset=utf-8")
	public @ResponseBody String regShopPush(HttpServletRequest request) throws Exception
	{
			String shopId=request.getParameter("shopId");
			CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,"商铺编号不能为空");
			Long numberShopId= NumberUtil.strToLong(shopId, "shopId");
			String auditStatus = request.getParameter("auditStatus");
			ShopDto shopDto=shopService.getShopEssentialInfoById(numberShopId);
			if(shopDto==null)
			{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在", new JSONObject());
			}
			else
			{
			    Long userId=shopDto.getPrincipalId();
			    if(!CommonConst.AUDIT_STATUS_NOPASS.equals(auditStatus)) {
	                JSONObject obj=new JSONObject();
	                obj.put("action", "regShop");
	                obj.put("shopId", shopId);
	                commonService.pushUserMsg(obj, userId,10);
	                UserDto user = memberService.getUserByUserId(userId);
	                if (null != user)
	                {
	                    SmsReplaceContent src = new SmsReplaceContent();
	                    src.setMobile(user.getMobile());
	                    src.setShopName(shopDto.getShopName());
	                    src.setUsage(CommonConst.SHOP_AUDIT_PASS);
	                    sendSmsService.sendSmsMobileCode(src);
	                }
					commonService.checkForRebateForShopRegister(shopDto);
			    } else {
			        UserDto user = memberService.getUserByUserId(userId);
			        SmsReplaceContent src = new SmsReplaceContent();
                    src.setMobile(user.getMobile());
                    src.setShopName(shopDto.getShopName());
                    src.setUsage(CommonConst.SHOP_AUDIT_NOPASS);
                    sendSmsService.sendSmsMobileCode(src);
			    }
			}
			if (shopDto.getHeadShopId() != null) {
				MqPusher.pushMessage("SyncGoodsByAudit", shopId);
			}
			
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
	}

	/**
	 * 刷新单个用户缓存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/userInfoChange",produces="application/json;charset=utf-8")
	public @ResponseBody String userInfoChange(HttpServletRequest request)
	{
		try{
			String userIdStr = request.getParameter("userId");
			CommonValidUtil.validObjectNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,"userId不能为空");
			Long userId = NumberUtil.strToLong(userIdStr, "userId");
			memberService.refreshUser(userId);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
		}catch(ServiceException e)
		{
			logger.error("刷新单个用户缓存异常",e);
			throw new APIBusinessException(e);
		}catch(Exception e)
		{
			throw new APISystemException("刷新单个用户缓存异常", e);
		}
	}
	
	
	/**
	 * 处理意见反馈
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/dealFeedBack",produces="application/json;charset=utf-8")
	@ResponseBody
	public String feedBackDeal(HttpServletRequest request)
	{
		try{
			String feedBackId=request.getParameter("feedBackId");//反馈ID
			CommonValidUtil.validNumStr(feedBackId, CodeConst.CODE_PARAMETER_NOT_VALID,"dealFeedBack格式错误");
			UserFeedbackDto userFeedBackDto=userFeedBackService.queryByFeedBackId(Long.parseLong(feedBackId));
			JSONObject pushTarget=new JSONObject();
			pushTarget.put("feedbackId",feedBackId);
			pushTarget.put("action","feedback");
			commonService.pushUserMsg(pushTarget,userFeedBackDto.getUserId(),0);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, new HashMap());
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("反馈意见失败！",e);
			throw new APISystemException("反馈意见失败！", e);
		}
	}
	
	
	/**
	 * 商铺信息有变动时
	 * 基本信息推送
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/shopInfoChange",produces="application/json;charset=utf-8")
	public@ResponseBody String doEssentialInfoPush(HttpServletRequest request)
	{
		try{
			commonShopPush(CommonConst.ACTION_SHOP_DATA_UPDATE, request);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("基本信息推送失败！",e);
			throw new APISystemException("基本信息推送失败！", e);
		}
	}
	
	/**
	 * 商铺信息有变动时
	 * 基本信息推送
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/orderInfoChange",produces="application/json;charset=utf-8")
	public@ResponseBody String orderInfoChangePush(HttpServletRequest request){
		try{
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_ORDERID);
			OrderDto order = this.orderServcie.getOrderMainById(orderId);
			JSONObject pushContent = new JSONObject();
			pushContent.put("action", "order");
			pushContent.put("orderId", order.getOrderId());
			pushContent.put("status", order.getOrderStatus());
			PushDto push = new PushDto();
			push.setAction("order");
			push.setContent(pushContent.toString());
			push.setUserId(order.getUserId());
			pushService.pushInfoToUser2(push,CommonConst.USER_TYPE_ZREO);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("订单信息推送失败！",e);
			throw new APISystemException("订单信息推送失败！", e);
		}
	}
	
	@RequestMapping(value="serverCommunicate/AdsInfoChange",produces="application/json;charset=utf-8")
	public@ResponseBody String adsInfoChange(HttpServletRequest request){
		try{
			String cityId = RequestUtils.getQueryParam(request, "cityId");
			CommonValidUtil.validStrNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_CITYID);
			CommonValidUtil.validPositLong(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CITYID);
			String adSpaceCode = RequestUtils.getQueryParam(request, "adSpaceCode");
			CommonValidUtil.validStrNull(adSpaceCode, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_ADSPACE_CODE);
			String key = CommonConst.KEY_AD_KEYLIST+":cityId:"+cityId+":adSpaceCode:"+adSpaceCode;
			List<String> keys = (List<String>)DataCacheApi.getObject(key);
			if(keys != null && keys.size() > 0){
				for(String e : keys){
					DataCacheApi.del(e);
				}
			}
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("广告信息推送失败！",e);
			throw new APISystemException("广告信息推送失败！", e);
		}
	}
	
	/**
	 * 
	 * @Title: commentInfoChange 
	 * @Description: TODO(管理员对用户的评论发生改变时触发此接口) 
	 * @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="serverCommunicate/commentInfoChange",produces="application/json;charset=utf-8")
	public  @ResponseBody String commentInfoChange(HttpServletRequest request)
	{
		try{
			String commentId=request.getParameter("commentId");//评论编号
			String commentType=request.getParameter("commentType");//评论类型
			CommonValidUtil.validObjectNull(commentType, CodeConst.CODE_PARAMETER_NOT_NULL,"评论类型不能为空");
			CommonValidUtil.validObjectNull(commentType, CodeConst.CODE_PARAMETER_NOT_NULL,"评论类型不能为空");
			CommonValidUtil.validNumStr(commentType, CodeConst.CODE_PARAMETER_NOT_NULL,"评论类型格式不对");
			CommonValidUtil.validNumStr(commentId, CodeConst.CODE_PARAMETER_NOT_VALID, "数字类型格式不对");
			Long userId=memberService.findUserIdByCommentId(Long.parseLong(commentId), Integer.parseInt(commentType));
			JSONObject pushTarget=new JSONObject();
			pushTarget.put("action", "comment");
			pushTarget.put("commentId", commentId);
			commonService.pushUserMsg(pushTarget, userId,0);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, new HashMap());
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("反馈意见失败！",e);
			throw new APISystemException("反馈意见失败！", e);
		}
	}
	
	/**
	 * 推荐商铺注册成功
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/referShopRegist",produces="application/json;charset=utf-8")
	@ResponseBody
	public ResultDto referShopRegist (HttpServletRequest request) {
		try{
			logger.info("荐商铺注册成功推送 -start");
			String shopIdStr =  RequestUtils.getQueryParam(request, "shopId"); //商铺编号
			CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
			Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
			ShopDto shopDetail = this.shopService.getShopEssentialInfoById(shopId);
			CommonValidUtil.validObjectNull(shopDetail, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			//刷新缓存
			DataCacheApi.setObject(CommonConst.KEY_SHOP + shopIdStr, shopDetail);
			
			//推送信息
			JSONObject pushTarget=new JSONObject();
			pushTarget.put("action", "refRegShop");
			pushTarget.put("shopId", shopId);
			this.commonService.pushUserMsg(pushTarget, shopDetail.getReferUserId(),0);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_PUSH_TRIGGER_SUCCESS, null);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			logger.error("推荐商铺注册成功推送失败！",e);
			throw new APISystemException("推荐商铺注册成功推送失败！", e);
		}
	}
	
	/**
	 * 通用的商铺信息推送封装
	 * @param action
	 * @param request
	 * @throws Exception
	 */
	private void commonShopPush(String action,HttpServletRequest request,String... pushToUser)throws Exception
	{
		final String shopId=request.getParameter("shopId");
		CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,"商铺编号不能为空");
		Long numberShopId= NumberUtil.strToLong(shopId, "shopId");
		ShopDto shopDto=shopService.getShopEssentialInfoById(numberShopId);
		if(shopDto!=null)
		{
			//刷新solr信息
			new Thread(){
				public void run(){
					SolrOperateService.refreshShop(shopId);
				}
				
			}.start();
			//刷新缓存
			DataCacheApi.setObjectEx(CommonConst.KEY_SHOP + shopId,shopDto,43200);
		
			JSONObject pushTarget=new JSONObject();
			pushTarget.put("action", action);
			pushTarget.put("shopId",shopId );  
			pushTarget.put("lastUpdate", DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
			PushDto push = new PushDto();
			push.setShopId(numberShopId);
			push.setAction(action);
			pushTarget.put("shopStatus", shopDto.getShopStatus());
			if(shopDto.getShopStatus()!=null&&shopDto.getShopStatus().intValue()==3)
			{
				pushTarget.put("shopDeadTime",DateUtils.format(DateUtils.addDays(shopDto.getLastUpdateTime(), 7),DateUtils.DATETIME_FORMAT));
			}
			push.setContent(pushTarget.toString());
			this.pushService.pushInfoToShop2(push);
			refreshRelateCache(shopId);
		}
		else
		{
			logger.error("给商铺推送信息失败,商铺信息查找失败 ,商铺Id为"+shopId);
		}
	}
	
	/**
	 * 刷新店铺关联的其它缓存
	 * @Title: refreshRelateCache 
	 * @param @param shopId
	 * @return void    返回类型 
	 * @throws
	 */
	private void refreshRelateCache(String shopId)
	{
		try{
		//==============刷新商铺logo--start======
		String key=CommonConst.KEY_SHOP_LOGO+shopId;
		DataCacheApi.del(key);
		//=============刷新店铺logo--end==========
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * 发送手机短信
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/sendMobileSms",produces="text/plain;charset=utf-8")
	@ResponseBody
	public String sendMobileSms(HttpServletRequest request){
		try {
			logger.info("发送手机短信-start");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String usage = RequestUtils.getQueryParam(request, "usage");
			String code = RequestUtils.getQueryParam(request, "code");
			//手机号码非空校验
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
			//手机号码格式校验
			CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
			//发送code非空校验
			CommonValidUtil.validStrNull(code, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
			//usage非空校验
			CommonValidUtil.validStrNull(usage, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USAGE);
			/*
			 *服务端 传的是明文密码，不需要再解码，如果传的是加密后的，则需要进行转码
			if (CommonConst.SEND_ROUTER_PWD.equals(usage)) {
				try {
					//如果是下发路由器密码，则需要将密码解码
					code = AESUtil.aesDecrypt(code, AESUtil.key);
				} catch (Exception e) {
					logger.error("路由器密码解码异常",e);
					throw new ValidateException(CodeConst.CODE_AES_DEC_ERROR, CodeConst.MSG_AES_DEC_ERROR);
				}
			}*/
			SmsReplaceContent src = new SmsReplaceContent();
			src.setMobile(mobile);
			src.setUsage(usage);
			src.setCode(code);
			boolean flag = sendSmsService.sendSmsMobileCode(src);
			if (flag) {
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCC_MOBILE_SMS, null);
			}else{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SEND_ERROR, CodeConst.MSG_FAIL_MOBILE_SMS, null);
			}
		} catch (ServiceException e) {
			logger.error("发送手机短信异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("发送手机短信异常",e);
			throw new APISystemException("发送手机短信异常", e);
		}
	}
	
	@RequestMapping(value="serverCommunicate/refreshSolr")
	public @ResponseBody String refreshSolr(HttpServletRequest request)
	{
		JSONObject obj=new JSONObject();
		try{
			String ids= RequestUtils.getQueryParam(request, "ids");
			String type=RequestUtils.getQueryParam(request, "refreshType");
			if(CommonConst.REFRESH_SHOP.equals(type))
			{
				SolrOperateService.refreshShop(ids);
			}
			else if(CommonConst.REFRESH_GOODS.equals(type))
			{
				SolrOperateService.refreshGoods(ids);
			}
			obj.put("success", true);
			return obj.toString();
		}catch(Exception e)
		{
			e.printStackTrace();
			obj.put("success", false);
			return obj.toString();
		}
	}
	
	/**
	 * 商品信息发生改变时推送(M8)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/goodsInfoChange",produces="text/plain;charset=utf-8")
	@ResponseBody
	public String goodsInfoChange(HttpServletRequest request){
		try {
			String goodsIdStr = RequestUtils.getQueryParam(request, "goodsId");
			String refSolrFlag = RequestUtils.getQueryParam(request, "refSolrFlag");//是否刷新solr，0为刷新，1为不刷新
			CommonValidUtil.validStrNull(goodsIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_MISS_GOOD_ID);
			Long goodsId = CommonValidUtil.validStrLongFmt(goodsIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_GOODS_ID);
			boolean flag= false;
			GoodsDto goodsDto = goodsService.getGoodsSetById(goodsId);
			if(goodsDto!=null){
				Long shopId=goodsDto.getShopId();
				DataCacheApi.del(CommonConst.KEY_SHOP_TOP_GOODS+shopId);
			}
			
			String key = CommonConst.KEY_GOODS+goodsId;
			DataCacheApi.del(key);
			if ("0".equals(refSolrFlag)) {
			    // TODO 还需要刷新solr
	            flag = SolrOperateService.refreshGoods(goodsIdStr);
	            
	            if (!flag) {
	                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"后台刷新商品信息缓存失败");
	            }
			}
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "推送触发成功", null);
		} catch (ServiceException e) {
			logger.error("后台刷新商品信息缓存异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("后台刷新商品信息缓存系统异常",e);
			throw new APISystemException("后台刷新商品信息缓存系统异常", e);
		}
	}
	
	/**
	 * 发送经销商、代理商账号密码信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="serverCommunicate/sendAdminMobileSms",produces="text/plain;charset=utf-8")
	@ResponseBody
	public String sendAdminMobileSms(HttpServletRequest request){
		try {
			logger.info("发送手机短信-start");
			String userName = RequestUtils.getQueryParam(request, "userName");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String usage = RequestUtils.getQueryParam(request, "usage");
			String code = RequestUtils.getQueryParam(request, "code");
			String agentType = RequestUtils.getQueryParam(request, "agentType");
			//用户名称非空校验
			CommonValidUtil.validStrNull(userName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USERNAME);
			//手机号码非空校验
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
			//手机号码格式校验
			CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
			//发送code非空校验
			CommonValidUtil.validStrNull(code, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
			//usage非空校验
			CommonValidUtil.validStrNull(usage, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USAGE);
			if (StringUtils.isBlank(usage)) {
				usage = CommonConst.MSG_SETTING_KEY_SMS_PWD;
			}
			CommonValidUtil.validStrNull(agentType, CodeConst.CODE_PARAMETER_NOT_NULL, "agentType不允许为空");
			SmsReplaceContent src = new SmsReplaceContent();
			src.setMobile(mobile);
			src.setUsage(usage);
			src.setUsername(userName);
			src.setShopManagerName(agentType);
			src.setPwd(code);
			boolean flag = sendSmsService.sendSmsMobileCode(src);
			if (flag) {
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCC_MOBILE_SMS, null);
			}else{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_SEND_ERROR, CodeConst.MSG_FAIL_MOBILE_SMS, null);
			}
		} catch (ServiceException e) {
			logger.error("发送手机短信异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("发送手机短信异常",e);
			throw new APISystemException("发送手机短信异常", e);
		}
	}
	
	/**
	 * 重新分账
	* @Title: reSplitAccount 
	* @param @param request
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value="serverCommunicate/reSplitAccount",produces="text/plain;charset=utf-8")
	public @ResponseBody String reSplitAccount(HttpServletRequest request)
	{
		JSONObject obj=new JSONObject();
		try {
			String orderId=RequestUtils.getQueryParam(request, "orderId");
			CommonValidUtil.validStrNull(orderId,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_OPEN_ID);
			OrderDto orderDto=orderServcie.getOrderById(orderId);
			if(orderDto!=null)
			{
				OrderGoodsSettleUtil.detailSingleOrder(orderDto.getOrderId());
			}
			obj.put("success", true);
		} catch (Exception e) {
			obj.put("success", false);
		}
		return obj.toString();
	}
	
	/**
	 * 微信号注册
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/serverCommunicate/weixinRegister", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String weixinRegister(@RequestParam("openId") String openId){
		try {
			logger.info("微信号注册-start");
			
			CommonValidUtil.validStrNull(openId,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_OPEN_ID);
			UserDto userDto = memberService.getUserByWeiXinNo(openId);
			if (userDto != null)
				throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_USER_REGISTERED);
			
			userDto = memberService.saveUserFromWeiXin(openId);
			DataCacheApi.setObjectEx(CommonConst.KEY_USER + userDto.getUserId(), userDto, CommonConst.CACHE_USER_OUT_TIME);
			DataCacheApi.setex(CommonConst.KEY_WEIXIN_NO + openId, userDto.getUserId().toString(), CommonConst.CACHE_USER_OUT_TIME);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("userId", userDto.getUserId());
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "微信号注册用户成功", resultMap);
		} catch (ServiceException e) {
			logger.error("微信号注册异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("微信号注册异常",e);
			throw new APISystemException("微信号注册异常", e);
		}
	}
	
	/**
	 * 微信号获取用户账户ID
	 * @param openId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/serverCommunicate/getUserByOpenId", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUserByOpenId(HttpServletRequest request) throws Exception{
		logger.info("微信号获取用户账户ID-start");
		String openId = RequestUtils.getQueryParam(request, "openId");
		String loginFlag = RequestUtils.getQueryParam(request, "loginFlag");
		CommonValidUtil.validStrNull(openId,
				CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_REQUIRED_OPEN_ID);
		
		UserDto userDto = memberService.getUserByWeiXinNo(openId);
		
		if (userDto == null) 
			throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("userId", userDto.getUserId());
		resultMap.put("isMember", userDto.getIsMember());
		resultMap.put("mobile", userDto.getMobile());
		resultMap.put("rebatesLevel", userDto.getRebatesLevel());
		resultMap.put("isShopManager", userDto.getIsShopManager());
		if(StringUtils.isNotBlank(loginFlag) && "1".equals(loginFlag))
		{
		     //创建会话id
            String sessionId = SessionUtil.dealSession(request);
            logger.info("生成的sessionId:" + sessionId);
            resultMap.put("sessionId", sessionId);
		}
		return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "微信号获取用户成功", resultMap);
	}
	
	/**
	 * 手机号获取用户接口 
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/serverCommunicate/getUserByMobile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUserByMobile(@RequestParam("mobile") String mobile){
		try {
			logger.info("手机号获取用户-start");
			
			CommonValidUtil.validStrNull(mobile,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MOBILE);
			
			CommonValidUtil.validMobileStr(mobile, 
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_REQUIRED_MOBILE_VALID);
			
			UserDto userDto = memberService.getUserByMobile(mobile);
			if (userDto == null) 
				throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("userId", userDto.getUserId());
			resultMap.put("rebatesLevel", userDto.getRebatesLevel());
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "手机号获取用户成功", resultMap);
		} catch (ServiceException e) {
			logger.error("手机号获取用户异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("手机号获取用户异常",e);
			throw new APISystemException("手机号获取用户异常", e);
		}
	}
	
	/**
	 * 微信号绑定会员接口  
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/serverCommunicate/bindWeixinUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String bindWeixinUser(HttpServletRequest request) {
		try {
			logger.info("微信号绑定会员接口 -start");
			String openId = RequestUtils.getQueryParam(request, "openId");
			String mobile = RequestUtils.getQueryParam(request, "mobile");
			String veriCode = RequestUtils.getQueryParam(request, "veriCode");
			String referUserIdStr = RequestUtils.getQueryParam(request, "referUserId");			
			CommonValidUtil.validStrNull(openId,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_OPEN_ID);
			
			CommonValidUtil.validStrNull(mobile,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MOBILE);
			
			CommonValidUtil.validMobileStr(mobile, 
					CodeConst.CODE_PARAMETER_NOT_VALID,
					CodeConst.MSG_REQUIRED_MOBILE_VALID);
			
			CommonValidUtil.validStrNull(veriCode,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_VERICODE);
			
			if(!StringUtils.isBlank(veriCode)) { 
				boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode);
				if (!flag) {
					throw new ValidateException(CodeConst.CODE_VERICODE_53101,CodeConst.MSG_VC_ERROR);
				}
			}
			Long referUserId = null;
			if(StringUtils.isNotBlank(referUserIdStr)) {
			    referUserId = NumberUtil.strToLong(referUserIdStr, "referUserId");
			}
			Boolean isAddPoint = false;
			UserDto userDtoOfmobile = memberService.getUserByMobile(mobile);
			UserDto userDtoOfOpenId = memberService.getUserByWeiXinNo(openId);
			if (userDtoOfmobile != null &&  userDtoOfOpenId != null) {
				if (userDtoOfmobile.getUserId().equals(userDtoOfOpenId.getUserId()))
					throw new ValidateException(CodeConst.CODE_WEIXIN_USER_BINDED,CodeConst.MSG_WEIXIN_USER_BINDED);
				
				if(userDtoOfOpenId.getIsMember() == CommonConst.USER_IS_MEMBER) {
                    userDtoOfOpenId.setWeixinNo(null);
                } else {
                    Map<String, String> updateParam = new HashMap<String, String>();
                    updateParam.put("newUserId", userDtoOfmobile.getUserId().toString());
                    updateParam.put("userId", userDtoOfOpenId.getUserId().toString());
                    updateParam.put("kefuRemark", "订单来源于微信用户userId:"+userDtoOfOpenId.getUserId().toString()+
                            ",openId:"+openId);
                    updateParam.put("serverLastTime", System.currentTimeMillis()+"");
                    orderServcie.updateUserId(updateParam);
                    //把用户注销掉
                    userDtoOfOpenId.setStatus(CommonConst.USER_LOGOUT_STATUS);
                }
				memberService.updateWeiXinNo(userDtoOfOpenId);
                if(userDtoOfmobile.getIsMember() != CommonConst.USER_IS_MEMBER ) {
                    userDtoOfmobile.setReferType(CommonConst.USER_REFERTYPE_MEMBER);
                    userDtoOfmobile.setReferUserId(referUserId);
                }
				userDtoOfmobile.setWeixinNo(openId);
				userDtoOfmobile.setStatus(CommonConst.USER_NORMAL_STATUS);
				userDtoOfmobile.setIsMember(CommonConst.USER_IS_MEMBER);
				memberService.updateBaseInfo(userDtoOfmobile);
				String weixinNoKey = CommonConst.KEY_WEIXIN_NO + openId;
				String weixinUserKey = CommonConst.KEY_USER + userDtoOfOpenId.getUserId();
				String mobileUserKey = CommonConst.KEY_USER + userDtoOfmobile.getUserId();
				DataCacheApi.del(weixinNoKey);
				DataCacheApi.del(weixinUserKey);
				DataCacheApi.del(mobileUserKey);
			}
			
			if (userDtoOfOpenId != null && userDtoOfmobile == null) {
				userDtoOfmobile = memberService.registerUserFromWeiXinBind(openId, mobile, referUserId);
				if(userDtoOfOpenId.getIsMember() == CommonConst.USER_IS_MEMBER) {
                    userDtoOfOpenId.setWeixinNo(null);
				} else {
				    userDtoOfOpenId.setStatus(CommonConst.USER_LOGOUT_STATUS);
				}
				memberService.updateWeiXinNo(userDtoOfOpenId);
				String weixinNoKey = CommonConst.KEY_WEIXIN_NO + openId;
				String weixinUserKey = CommonConst.KEY_USER + userDtoOfOpenId.getUserId();
				DataCacheApi.del(weixinNoKey);
				DataCacheApi.del(weixinUserKey);
				if(userDtoOfOpenId.getIsMember() == 0) {
					Map<String, String> updateParam = new HashMap<String, String>();
					updateParam.put("newUserId", userDtoOfmobile.getUserId().toString());
					updateParam.put("userId", userDtoOfOpenId.getUserId().toString());
					updateParam.put("kefuRemark", "订单来源于微信用户userId:"+userDtoOfOpenId.getUserId().toString()+
							",openId:"+openId);
					updateParam.put("serverLastTime", System.currentTimeMillis()+"");
					orderServcie.updateUserId(updateParam);
				}
			}
			
			if (userDtoOfOpenId == null && userDtoOfmobile != null) {
				userDtoOfmobile.setWeixinNo(openId);
				if(userDtoOfmobile.getIsMember() != CommonConst.USER_IS_MEMBER ) {
                    userDtoOfmobile.setReferType(CommonConst.USER_REFERTYPE_MEMBER);
                    userDtoOfmobile.setReferUserId(referUserId);
                    userDtoOfmobile.setIsMember(CommonConst.USER_IS_MEMBER);
                    userDtoOfmobile.setStatus(CommonConst.USER_NORMAL_STATUS);
                }
				memberService.updateBaseInfo(userDtoOfmobile);
				isAddPoint = true;
			}
			if (userDtoOfmobile == null && userDtoOfOpenId == null) {
				userDtoOfmobile = memberService.registerUserFromWeiXinBind(openId, mobile, referUserId);
				isAddPoint = true;
			}
			
			if (isAddPoint) {
				List<Long> shopIdList = shopDao.getIdListByPrincipalId(userDtoOfmobile.getUserId());
				for (Long shopId :shopIdList){
					levelService.pushAddPointMessage(2, 3, 1, shopId.intValue(), 2, userDtoOfmobile.getUserId().toString(),true);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
		    resultMap.put("userId", userDtoOfmobile.getUserId());
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "微信号绑定会员成功", resultMap);
		} catch (ServiceException e) {
			logger.error("微信号绑定会员异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("微信号绑定会员异常",e);
			throw new APISystemException("微信号绑定会员异常", e);
		}
	}
	
	

    /**
     * 解除微信号绑定会员接口  
     * @param openId
     * @return
     */
    @RequestMapping(value = "/serverCommunicate/unbindWeixinUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String unbindWeixinUser(HttpServletRequest request) {
        try {
            logger.info("解除微信号绑定会员接口 -start");
            String openId = RequestUtils.getQueryParam(request, "openId");
            CommonValidUtil.validStrNull(openId,
                    CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_OPEN_ID);
            
            UserDto userDtoOfOpenId = memberService.getUserByWeiXinNo(openId);
            if (userDtoOfOpenId != null) {
                userDtoOfOpenId.setWeixinNo(null);
                memberService.updateWeiXinNo(userDtoOfOpenId);
                String weixinNoKey = CommonConst.KEY_WEIXIN_NO + openId;
                String weixinUserKey = CommonConst.KEY_USER + userDtoOfOpenId.getUserId();
                DataCacheApi.del(weixinNoKey);
                DataCacheApi.del(weixinUserKey);
               
            }
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "解除绑定绑定会员成功！", null);
        } catch (ServiceException e) {
            logger.error("微信号绑定会员异常",e);
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("微信号绑定会员异常",e);
            throw new APISystemException("微信号绑定会员异常", e);
        }
    }
	
	/**
	 * 座位获取订单接口  
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/serverCommunicate/getSeatOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getSeatOrder(HttpServletRequest request) {
		logger.info("座位获取订单 -start");
		String openIdStr = RequestUtils.getQueryParam(request, "openId");
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String seatIdStr = RequestUtils.getQueryParam(request, "seatId");
		String orderStatusStr = RequestUtils.getQueryParam(request, "orderStatus");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			CommonValidUtil.validStrNull(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SHOPID);
			
			Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"商铺Id数据格式错误");
			
			requestMap.put("shopId", shopId);
			
			CommonValidUtil.validStrNull(seatIdStr,
					CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_SEATID);
			
			Integer seatId = CommonValidUtil.validStrIntFmt(seatIdStr,
					CodeConst.CODE_PARAMETER_NOT_VALID,
					"商铺座位Id数据格式错误");
			
			requestMap.put("seatId", seatId);
			
			if (openIdStr != null) {
				CommonValidUtil.validObjectNull(memberService.getUserByWeiXinNo(openIdStr),
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
				requestMap.put("openId", openIdStr);
			}
		
			if (orderStatusStr != null) {
				Integer orderStatus = CommonValidUtil.validStrIntFmt(orderStatusStr,
						CodeConst.CODE_PARAMETER_NOT_VALID,
						"orderStatus数据格式错误");
				if (openIdStr == null && orderStatus == CommonConst.SEAT_ORDER_STATUS_FINISH_WEIXIN_USER)
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"orderStatus参数无效");
				requestMap.put("orderStatus", orderStatus);
			}
			
			List<Map<String, Object> > resultMapList = orderServcie.getSeatOrder(requestMap);
			if (resultMapList == null)
				throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "座位获取订单成功", resultMapList);
		} catch (ServiceException e) {
			logger.error("座位获取订单异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("座位获取订单异常",e);
			throw new APISystemException("座位获取订单异常", e);
		}
	}
	
	
	@RequestMapping(value="sendMqMsg")
	public @ResponseBody String sendMqMsg(HttpServletRequest request){
		try{
			String msg=request.getParameter("msg");
			JSONObject obj=new JSONObject();
			obj.put("success","true");
			MqProduceApi.setMessage("topic1", "tag1", msg);
			return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "座位获取订单成功", obj);
		} catch (ServiceException e) {
			logger.error("座位获取订单异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("座位获取订单异常",e);
			throw new APISystemException("座位获取订单异常", e);
		}
	}
}
