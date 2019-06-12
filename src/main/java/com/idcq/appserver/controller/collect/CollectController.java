
package com.idcq.appserver.controller.collect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.controller.goods.StandardGoodsDto;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.commonconf.ShopFeedBackDto;
import com.idcq.appserver.dto.goods.GoodsAvsDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.order.MultiPayDto;
import com.idcq.appserver.dto.order.MultiPayOrderDto;
import com.idcq.appserver.dto.order.MultiPayOrderInfoDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderGoodsServiceTech;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.dto.shop.ShopDeviceDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.commonconf.IShopFeedBackService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.order.IOrderShopRsrcService;
import com.idcq.appserver.service.order.IXorderService;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.user.IUserAccountService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.mq.goods.GoodsMessageUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

/**
 * 收银接口controller
 * @author nie_jq
 * 
 */
@Controller
public class CollectController
{
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ICollectService collectService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private IShopFeedBackService shopFeedBackService;

    @Autowired
    private IShopServcie shopService;

    @Autowired
    private IOrderShopRsrcService orderShopRsrcService;

    @Autowired
    private IGoodsServcie goodsService;

    @Autowired
    private IMemberServcie memberServcie;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IUserCashCouponService userCashCouponService;

    @Autowired
    public IXorderService xorderService;

    @Autowired
    public IOrderServcie orderServcie;

    @Autowired
    private IGoodsServcie goodsServcie;

    @Autowired
    private IShopServcie shopServcie;

    @Autowired
    private IPacketService packetServcie;

    @Autowired
    private IGoodsSetDao goodsSetDao;
    
	@Autowired
	private IGoodsDao goodsDao;
    /**
     * 初始化接口（菜单表、员工表、座位信息表）
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getShopData", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getShopData(HttpServletRequest request) throws Exception
    {
        logger.info("初始化接口（菜单表、员工表、座位信息表）-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");

        String needGoods = RequestUtils.getQueryParam(request, "needGoods"); // 0：不返回商品
                                                                             // 1：返回商品

        // 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);

        Map pModel = collectService.getShopData(shopId, needGoods, token);

        return ResultUtil.getResultJson(0, "调用成功", pModel, DateUtils.DATETIME_FORMAT);
    }

    /**
     * 下发手机验证码
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/captchaMember", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto captchaMember(HttpServletRequest request) throws Exception
    {
        long start = System.currentTimeMillis();
        logger.info("收银接口，下发手机验证码-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        String usage = RequestUtils.getQueryParam(request, "usage");
        // 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 手机号码验证
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_REQUIRED_MOBILE_VALID);
        // 校验商铺及商铺设备token是否存在
        collectService.queryShopAndTokenExists(shopId, token);
        if (CommonConst.IS_IP_LIMIT)
        {
            String ip = RequestUtils.getIpAddr(request);
            logger.info("访问者的IP地址: " + ip);
            // IP限制
            CommonValidUtil.validRequestId(ip, mobile);
        }
        if (StringUtils.isEmpty(usage))
        {
            usage = CommonConst.USER_REGISTER;
        }
        SmsReplaceContent content = new SmsReplaceContent();
        content.setMobile(mobile);
        content.setUsage(usage);
        content.setCacheCodeFlag(true);// 需要缓存验证码
        content.setCreateCodeFlag(true);// 需要创建验证码
        boolean flag = sendSmsService.sendSmsMobileCode(content);
        logger.info("共耗时：" + (System.currentTimeMillis() - start));
        if (flag)
        {
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_SMSCODE, null);
        }
        else
        {
            return ResultUtil.getResult(CodeConst.CODE_VERICODE_55901, CodeConst.MSG_VERICODE_55901, null);
        }
    }

    /**
     * 收银机登录
     * 
     * @param user
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/vertifyDevice", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String vertifyDevice(HttpServletRequest request) throws Exception
    {
        logger.info("收银机登录-start");
        Map map = this.collectService.vertifyDevice(request);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, map,
                DateUtils.DATETIME_FORMAT);
    }

    /**
     * 会员添加
     * @param request
     * @return
     */
    @RequestMapping(value = "/registerMember", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto registerMember(HttpServletRequest request) throws Exception
    {
            logger.info("会员注册-start");

            Map<String, String> map = this.collectService.registerMember(request);
            if (map.get("passWord") != null)
            {
                // 发送短信失败不影响注册
                try
                {
                    SmsReplaceContent content = new SmsReplaceContent();
                    content.setMobile(map.get("mobile"));
                    content.setCode(map.get("passWord"));
                    content.setUsage(CommonConst.MEMBER_ADD);
                    sendSmsService.sendSmsMobileCode(content);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    logger.error("发送短信密码失败", e);
                }
            }

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, null);
    }

    /**
     * 会员查询
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value =
    { "/searchMember", "/service/user/searchMember", "/session/user/searchMember", "/token/user/searchMember" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultDto searchMember(HttpServletRequest request) throws Exception
    {
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String moneyStr = RequestUtils.getQueryParam(request, "money");
        String businessAreaActivityIdStr = RequestUtils.getQueryParam(request, "businessAreaActivityId");
        Long shopId = null;
        Double money = null;
        Long businessAreaActivityId = null;
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_REQUIRED_MOBILE_VALID);
        if (StringUtils.isNotBlank(shopIdStr))
        {
            shopId = NumberUtil.strToLong(shopIdStr, "shopId");
        }
        if (StringUtils.isNotBlank(moneyStr))
        {
            money = NumberUtil.strToDouble(moneyStr, "money");
        }
        if (StringUtils.isNotBlank(businessAreaActivityIdStr))
        {
            businessAreaActivityId = NumberUtil.strToLong(businessAreaActivityIdStr, "businessAreaActivityId");
        }
        Map<String, Object> map = this.collectService.searchMember(mobile, shopId, money, businessAreaActivityId);
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, map);
    }

    /**
     * 商户反馈接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = {"feedBack","/session/feedBack","/session/shop/feedBack","/service/shop/feedBack","/token/shop/feedBack"}, produces = "application/json;charset=utf-8", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody
    String merchantFeedBack(HttpServletRequest request, @RequestBody
    Map<String, String> hashMap) throws Exception
    {
        String shopId = hashMap.get("shopId");
        String description = hashMap.get("description");
        String clientSystemTypeStr = hashMap.get("clientSystemType");
        String attachementIds = hashMap.get("attachementIds");
        String feedbackTypeStr = hashMap.get("feedbackType");
        Integer clientSystemType = 1; //代表收银机
        if (StringUtils.isNotBlank(clientSystemTypeStr)) {
            clientSystemType = NumberUtil.strToInteger(clientSystemTypeStr, "clientSystemType");
        }
        Integer feedbackType = 0; //代表其他问题
        if (StringUtils.isNotBlank(feedbackTypeStr)) {
            feedbackType = NumberUtil.strToInteger(feedbackTypeStr, "feedbackType");
        }
        String token = hashMap.get("token");
        CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
        CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, "商铺编号格式错误");
        String url = request.getRequestURI();
        if(url.equals(CommonConst.FEEDBACK_URL)){
        	// 商铺设备token验证
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            // 校验商铺及商铺设备token是否存在
            collectService.queryShopAndTokenExists(Long.parseLong(shopId), token);
        }
        ShopFeedBackDto shopFeedBackDto = new ShopFeedBackDto();
        shopFeedBackDto.setFeedback(description);
        shopFeedBackDto.setShopId(Long.parseLong(shopId));
        shopFeedBackDto.setCreateTime(new Date());
        shopFeedBackDto.setClientSystemType(clientSystemType);
        shopFeedBackDto.setFeedbackType(feedbackType);
        shopFeedBackService.insertShopFeedBack(shopFeedBackDto, attachementIds);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "成功提交商户反馈", null);
}

    /**
     * 查询商铺反馈历史接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = {"getShopFeedbackList","/token/shop/getShopFeedbackList","/session/shop/getShopFeedbackList","/service/shop/getShopFeedbackList" }, produces = "application/json;charset=utf-8")
    public @ResponseBody
    String getShopFeedbackList(HttpServletRequest request) throws Exception
    {
        String shopId = request.getParameter("shopId");
        String token = request.getParameter("token");
        // 校验商铺及商铺设备token是否存在
        CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
        CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, "商铺编号格式错误");
        long shopLongValue = Long.parseLong(shopId);
        String url = request.getRequestURI();
        if(url.equals(CommonConst.FEEDBACKLIST_URL)){ 
            // 商铺设备token验证
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            collectService.queryShopAndTokenExists(shopLongValue, token);
        }
       
        PageModel pageModel = new PageModel();
        initPageModel(pageModel, request);
        pageModel = shopFeedBackService.getShopFeedbackList(shopLongValue, pageModel);
        MessageListDto msgList = new MessageListDto();
        msgList.setpNo(pageModel.getToPage());
        msgList.setpSize(pageModel.getPageSize());
        msgList.setLst(DataConvertUtil.convertCollectionToListMap(pageModel.getList(),
                CommonResultConst.MERCHANT_FEEDBACK_LIST));
        msgList.setrCount(pageModel.getTotalItem());
        List<MessageListDto> dataList = new ArrayList<MessageListDto>();
        dataList.add(msgList);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取反馈信息成功", msgList, DateUtils.DATETIME_FORMAT);
    }
    
    
    /**
     * 查询商铺反馈详情接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = {"/token/shop/getShopFeedbackInfo","/session/shop/getShopFeedbackInfo","/service/shop/getShopFeedbackInfo"}, produces = "application/json;charset=utf-8")
    public @ResponseBody String getShopFeedbackInfo(HttpServletRequest request) throws Exception
    {
        String feedbackIdStr = request.getParameter("feedbackId");
        CommonValidUtil.validObjectNull(feedbackIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "feedbackId不能为空");
        Long feedbackId = NumberUtil.strToLong(feedbackIdStr, "feedbackId");
        ShopFeedBackDto shopFeedBackDto = shopFeedBackService.getShopFeedbackInfo(feedbackId);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取反馈信息成功", shopFeedBackDto);
    }

    /**
     * 初始化分页Model
     * @Title: initPageModel
     * @param @param pageModel
     * @param @param request
     * @return void 返回类型
     * @throws
     */
    private void initPageModel(PageModel pageModel, HttpServletRequest request)
    {
        String pNo = request.getParameter("pNo");
        String pSize = request.getParameter("pSize");
        if (StringUtils.isEmpty(pNo) || !NumberUtil.isNumer(pNo))
        {
            pageModel.setToPage(1);
        }
        else
        {
            pageModel.setToPage(Integer.parseInt(pNo));
        }
        if (StringUtils.isEmpty(pSize) || !NumberUtil.isNumer(pSize))
        {
            pageModel.setPageSize(10);
        }
        else
        {
            pageModel.setPageSize(Integer.parseInt(pSize));
        }
    }

    /**
     * 获取APK最新版本接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getLastestVesion", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getLastestVesion(HttpServletRequest request) throws Exception
    {
        logger.info("获取APK最新版本接口-start");
        String appIds = RequestUtils.getQueryParam(request, "appIds");
        // APK名称验证
        CommonValidUtil.validStrNull(appIds, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_APK_NM);
        Map pModel = collectService.getLastestVesion(appIds);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, pModel,
                DateUtils.DATETIME_FORMAT);
    }

    /**
     * 获取API地址接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getCRAddress", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getCRAddress(HttpServletRequest request) throws Exception
    {
        logger.info("获取API地址接口-start");
        String snId = RequestUtils.getQueryParam(request, "SNID");
        String appName = RequestUtils.getQueryParam(request, "appName");
        CommonValidUtil.validStrNull(snId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SNID);
        String[] result = collectService.getCRAddress(snId, appName);
        return ResultUtil.getResultJsonStr(0, "获取成功", result);
    }

    /**
     * 商家确认的取消订单接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/shopAuditCancelOrder", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto shopAuditCancelOrder(HttpServletRequest request) throws Exception
    {
        logger.info("商家确认的取消订单接口-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");
        String orderId = RequestUtils.getQueryParam(request, "orderId");
        // 是否同意退单 0（同意用户的取消订单），1（拒绝用户的取消订单）
        String auditFlag = RequestUtils.getQueryParam(request, "auditFlag");
        // 商家拒绝退单理由
        String refuseReason = RequestUtils.getQueryParam(request, "refuseReason");
        // 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        if (StringUtils.isBlank(auditFlag))
        {
            // 默认就是同意退单
            auditFlag = CommonConst.AUDITFLAG_ZERO;
        }
        collectService.queryShopAndTokenExists(shopId, token);

        // 这个1代表按付款方式退款，其他方式支付直接退回传奇宝
        collectService.shopCancelOrder(shopId, orderId, auditFlag, CommonConst.REFUNDTYPE_ONE, refuseReason);
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, null);
    }

    /**
     * 免费商家确认的取消订单接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/shopAdmin/shopAuditCancelOrder", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultDto freeShopAuditCancelOrder(HttpServletRequest request) throws Exception
    {
        logger.info("商家确认的取消订单接口-start");

        String userId = RequestUtils.getQueryParam(request, "userId");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String orderId = RequestUtils.getQueryParam(request, "orderId");

        // 是否同意退单 0（同意用户的取消订单），1（拒绝用户的取消订单）
        String auditFlag = RequestUtils.getQueryParam(request, "auditFlag");

        // 现金退款还是付款方式退款 1（付款方式退款）0现金退款
        String refundType = RequestUtils.getQueryParam(request, "refundType");
        // 商家拒绝退单理由
        String refuseReason = RequestUtils.getQueryParam(request, "refuseReason");
        // 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        CommonValidUtil.validStrNull(refundType, CodeConst.CODE_PARAMETER_NOT_NULL, "refundType不能为空");
        if (StringUtils.isBlank(auditFlag))
        {
            // 同意退款
            auditFlag = CommonConst.AUDITFLAG_ZERO;
        }
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        collectService.shopCancelOrder(shopId, orderId, auditFlag, refundType, refuseReason);
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, null);
    }

    /**
     * 收银接口-获取订单详情
     * @param request
     * @return
     * @throws Exception 
     */

    @RequestMapping(value = "/getOrderDetail4CR", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getOrderDetail4CR(HttpServletRequest request) throws Exception
    {
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String orderId = RequestUtils.getQueryParam(request, "orderId");
        String token = RequestUtils.getQueryParam(request, "token");// 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        collectService.queryShopAndTokenExists(shopId, token);
         Map model = collectService.getOrderDetail4CR(shopId, orderId);
        
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取订单详情成功！", model, DateUtils.DATETIME_FORMAT);
    }

    /**
     * 收银接口-获取订单支付详情
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getOrderPayDetail", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getOrderPayDetail(HttpServletRequest request) throws Exception
    {
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String orderId = RequestUtils.getQueryParam(request, "orderId");
        String token = RequestUtils.getQueryParam(request, "token");// 商铺编号验证
        String orderSearchType = RequestUtils.getQueryParam(request, "orderSearchType");// 订单查询类型
        String queryOrderStatusFlag = RequestUtils.getQueryParam(request, "orderSearchType");// 查询订单状态
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        List<Map> model = collectService.getOrderPayDetail(shopId, orderId, token);
        if (!StringUtils.isEmpty(queryOrderStatusFlag))
        {
            int orderStatus = collectService.getOrderStatus(orderId);
            initOrderStatus(model, orderStatus);
        }
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取订单支付信息成功！", model, DateUtils.DATETIME_FORMAT);
    }

    /**
     * 初始化订单状态
     * @Title: initOrderStatus
     * @param @param payList
     * @param @param status
     * @return void 返回类型
     * @throws
     */
    private void initOrderStatus(List<Map> payList, int status)
    {
        for (Map map : payList)
        {
            map.put("orderStatus", status);
        }
    }

    /**
     * 收银机上报regId
     * 
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/reportRegId", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String reportRegId(HttpServletRequest request) throws Exception
    {
        logger.info("收银机上报regId-start");
        String sn = RequestUtils.getQueryParam(request, "sn");
        String token = RequestUtils.getQueryParam(request, "token");
        String regId = RequestUtils.getQueryParam(request, "regId");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        CommonValidUtil.validStrNull(sn, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(regId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REGID_NULL);
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        this.collectService.reportRegId(sn, regId, token, shopIdStr);

        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, null);
    }

    /**
     * 修订店铺内路由器的单个mac白名单接口
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/shop/modifyAWifiMacWhiteList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String modifyAWifiMacWhiteList(HttpServletRequest request) throws Exception
    {
        logger.info("修订店铺内路由器的单个mac白名单接口-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");// 商铺编号验证
        String mac = RequestUtils.getQueryParam(request, "MAC");
        String cmdStr = RequestUtils.getQueryParam(request, "cmd");
        String deviceName = RequestUtils.getQueryParam(request, "deviceName");
        String deviceType = RequestUtils.getQueryParam(request, "deviceType");
        String deviceOwnerTypeStr = RequestUtils.getQueryParam(request, "deviceOwnerType");
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(mac, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MAC);
        int cmd = 1;// 操作类型：1（新增，默认），2（删除），3（修改）
        if (!StringUtils.isEmpty(cmdStr))
        {
            cmd = CommonValidUtil.validStrIntFmt(cmdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_CMD);
            if (!(cmd == 1 || cmd == 2 || cmd == 3))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_CMD);
            }
        }
        ShopDeviceDto shopDeviceDto = new ShopDeviceDto();
        shopDeviceDto.setShopId(shopId);
        shopDeviceDto.setDeviceToken(token);
        shopDeviceDto.setSnId(mac);
        shopDeviceDto.setDeviceName(deviceName);
        shopDeviceDto.setCreateTime(new Date());
        shopDeviceDto.setLastContactTime(new Date());
        if (cmd == 3)
        {
            // 修改操作，如果客户端未上传设备类型及设备归属类型，则不能赋予默认值，如果设备归属类型不为空，则需要做格式校验
            shopDeviceDto.setDeviceType(deviceType);
            if (!StringUtils.isEmpty(deviceOwnerTypeStr))
            {
                int deviceOwnerType = CommonValidUtil.validStrIntFmt(deviceOwnerTypeStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_DEVICE_OWNER_TYPE);
                if (!(deviceOwnerType == 0 || deviceOwnerType == 1))
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
                            CodeConst.MSG_REQUIRED_DEVICE_OWNER_TYPE);
                }
                shopDeviceDto.setDeviceOwnerType(deviceOwnerType);
            }
        }
        else if (cmd == 1)
        {
            // 新增操作
            int deviceOwnerType = 1;// 默认平台设备
            if (!StringUtils.isEmpty(deviceOwnerTypeStr))
            {
                deviceOwnerType = CommonValidUtil.validStrIntFmt(deviceOwnerTypeStr,
                        CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_DEVICE_OWNER_TYPE);
                if (!(deviceOwnerType == 0 || deviceOwnerType == 1))
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
                            CodeConst.MSG_REQUIRED_DEVICE_OWNER_TYPE);
                }
            }
            shopDeviceDto.setDeviceOwnerType(deviceOwnerType);
            if (StringUtils.isEmpty(deviceType))
            {
                deviceType = "其他";// 默认
            }
            shopDeviceDto.setDeviceType(deviceType);
        }
        collectService.modifyAWifiMacWhiteList(shopDeviceDto, cmd);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
    }

    /**
     * @throws Exception 
     * 获取商铺预定订单列表
     * @Title: getShopBookOrders
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "getShopBookOrders", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getShopBookOrders(HttpServletRequest request) throws Exception
    {
        String shopIdParam = RequestUtils.getQueryParam(request, "shopId");
        CommonValidUtil.validStrNull(shopIdParam, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdParam, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        String token = RequestUtils.getQueryParam(request, "token");
        String bookTimeFrom = RequestUtils.getQueryParam(request, "bookTimeFrom");
        String bookTimeTo = RequestUtils.getQueryParam(request, "bookTimeTo");
        if (!StringUtils.isEmpty(bookTimeFrom))
        {
            CommonValidUtil.validDateTimeStr(bookTimeFrom, CodeConst.CODE_PARAMETER_NOT_VALID, "预定时间查询参数格式不对");
        }
        else
        {
            bookTimeFrom = DateUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00";

        }
        if (!StringUtils.isEmpty(bookTimeTo))
        {
            CommonValidUtil.validDateTimeStr(bookTimeTo, CodeConst.CODE_PARAMETER_NOT_VALID, "预定时间查询参数格式不对");
        }
        else
        {
            bookTimeTo = DateUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59";
        }
        // 商铺设备token验证
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        collectService.queryShopAndTokenExists(shopId, token);
        List<OrderShopRsrcDto> queryResult = orderShopRsrcService.getShopBookOrders(shopId, bookTimeFrom,
                bookTimeTo);
        List<Map<String, Object>> convertResult = DataConvertUtil.convertListObjToMap(queryResult,
                CommonResultConst.GET_SHOP_BOOK_ORDERS);
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, convertResult,
                DateUtils.DATE_FORMAT);
    }

    /**
     * 获取店铺内路由器的MAC白名单接口
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/shop/getWifiMacWhitelist", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getWifiMacWhitelist(HttpServletRequest request) throws Exception
    {
        logger.info("获取店铺内路由器的MAC白名单接口-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");// 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        List<Map> list = collectService.getWifiMacWhitelist(shopId, token);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", list);
    }

    /**
     * 获取店铺管理者所拥有的店铺列表接口
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getOwnShopList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getOwnShopList(HttpServletRequest request) throws Exception
    {
        logger.info("获取店铺管理者所拥有的店铺列表接口-start");
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        String userPassword = RequestUtils.getQueryParam(request, "userPassword");
        String shopMode = RequestUtils.getQueryParam(request, "shopMode");
        String authentication = RequestUtils.getQueryParam(request, "authentication");
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_REQUIRED_MOBILE_VALID);
        if(!"100".equals(authentication))
            CommonValidUtil.validStrNull(userPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);

        List<Map> list = collectService.getOwnShopList(mobile, userPassword, shopMode, authentication);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", list);
}

    /**
     * 
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.updateGoodsPrice
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @throws Exception 
     * @date:2015年11月5日 下午5:45:42
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年11月5日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value = "/updateGoodsPrice", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateGoodsPrice(HttpServletRequest request) throws Exception
    {
        logger.info("收银机上报商品时价-start");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");
        String goodsIdStr = RequestUtils.getQueryParam(request, "goodsId");
        String standardPriceStr = RequestUtils.getQueryParam(request, "standardPrice");
        // shopId必填
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        CommonValidUtil.validPositLong(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
        Long shopId = Long.valueOf(shopIdStr);
        // token必填及存在性
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 校验店铺和token
        collectService.queryShopAndTokenExists(shopId, token);
        // goodsId必填及存在性
        CommonValidUtil
                .validStrNull(goodsIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_ID);
        CommonValidUtil.validPositLong(goodsIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_GOODS_ID);
        // standardPrice必填
        CommonValidUtil.validStrNull(standardPriceStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG_REQUIRED_STANDARD_PRICE);
        CommonValidUtil.validDoubleStr(standardPriceStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_STANDARD_PRICE);

        collectService.updateGoodsPrice(shopId, Long.valueOf(goodsIdStr), Double.valueOf(standardPriceStr));
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
    }

    /**
     * 密码验证接口
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.checkPassword
     * @Description:
     * 
     * @param request （shopId, token, password）
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @throws Exception 
     * @date:2015年8月20日 上午9:04:38
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年8月20日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value = "/checkPassword", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkPassword(HttpServletRequest request) throws Exception
    {
        logger.info("验证店铺管理员密码接口 -start");
        String password = RequestUtils.getQueryParam(request, "password");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String token = RequestUtils.getQueryParam(request, "token");// 商铺编号验证
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
        boolean flag = collectService.checkPassword(shopId, token, password);
        if (!flag)
        {
            return ResultUtil.getResultJsonStr(CodeConst.CODE_PWD_ERROR, CodeConst.MSG_PWD_ERROR, null);
        }
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
    }

    /**
     * 获取商铺菜品信息接口
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.getGoodsList
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @throws Exception 
     * @date:2015年8月20日 上午11:26:11
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年8月20日 ChenYongxin v1.0.0 create
     */
    @RequestMapping(value = "/getGoodsList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getGoodsList(HttpServletRequest request) throws Exception
    {
        logger.info("获取商铺菜品信息接口 -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        // 令牌
        String token = RequestUtils.getQueryParam(request, "token");
        // 分类id
        String goodsCategoryIdStr = RequestUtils.getQueryParam(request, "goodsCategoryId");
        // 菜品分类ID
        String isSupportMarketPricesStr = RequestUtils.getQueryParam(request, "isSupportMarketPrices");
        // 参数map
        Map<String, Object> mapParam = new HashMap<String, Object>();

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 校验token
        collectService.queryShopAndTokenExists(shopId, token);
        if (StringUtils.isNotBlank(goodsCategoryIdStr))
        {
            Integer goodsCategoryId = CommonValidUtil.validStrIntFmt(goodsCategoryIdStr,
                    CodeConst.CODE_PARAMETER_NOT_VALID, "goodsCategoryId格式错误");
            mapParam.put("goodsCategoryId", goodsCategoryId);
        }

        if (StringUtils.isNotBlank(isSupportMarketPricesStr))
        {
            Integer isSupportMarketPrices = CommonValidUtil.validStrIntFmt(isSupportMarketPricesStr,
                    CodeConst.CODE_PARAMETER_NOT_VALID, "isSupportMarketPrices格式错误");
            mapParam.put("isSupportMarketPrices", isSupportMarketPrices);
        }
        mapParam.put("shopId", shopId);
        List<Map<String, Object>> resultList = goodsService.getGoodsList(mapParam);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", resultList);
    }

    /**
     * 获取传奇宝支付时的手机短信验证码接口
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.getPayVeriCode
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @throws Exception 
     * @date:2015年9月6日 上午10:48:54
     * 
     *                 Modification History: Date Author Version Description
     *                 ----
     *                 ------------------------------------------------------
     *                 ------- 2015年9月6日 ChenYongxin v1.0.0 create 2015年12月30日
     *                 LuJianPing v1.0.1 modify:响应code区分是否足额支付
     */
    @RequestMapping(value =
    { "/getPayVeriCode", "/session/getPayVeriCode" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getPayVeriCode(HttpServletRequest request) throws Exception
    {
        int code = CodeConst.CODE_SUCCEED;
        /*
         * shopId int 是 商铺编号 token String 是 设备令牌 mobile String 是 手机号码
         * payAmount Double 是 订单的待支付金额
         */
        logger.info("获取传奇宝支付时的手机短信验证码接口 -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

        // 手机
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        // 订单的待支付金额
        String payAmountStr = RequestUtils.getQueryParam(request, "payAmount");
        // 订单id
        // String orderIdStr = RequestUtils.getQueryParam(request,
        // "orderId");
        // if(StringUtils.isNotBlank(orderIdStr)){
        // OrderDto order = orderServcie.getOrderMainById(orderIdStr);
        // CommonValidUtil.validObjectNull(order,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_NOT_EXIST);
        // }

        String partPayFlag = RequestUtils.getQueryParam(request, "partPayFlag");// 是否部分支付
        if (StringUtils.isEmpty(partPayFlag))
        {
            partPayFlag = "0";
        }
        // shopid
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        // 手机
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "手机不能为空");
        // 支付金额
        CommonValidUtil.validStrNull(payAmountStr, CodeConst.CODE_PARAMETER_NOT_NULL, "支付金额不能为空");
        Double payAmount = CommonValidUtil.validStrDoubleFmt(payAmountStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                "支付金额格式错误");
        if (payAmount == 0)
        {
            CommonValidUtil.validStrDoubleFmt(payAmountStr, CodeConst.CODE_PAY_STATUS_SUCCESS,
                    CodeConst.MSG_PAY_STATUS_SUCCESS);
        }
        String url = request.getRequestURI();
        if (CommonConst.PAYVERICODE_URL.equals(url))
        {
            // 令牌
            String token = RequestUtils.getQueryParam(request, "token");
            // token
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            collectService.queryShopAndTokenExists(shopId, token);
        }

        // 上面有校验店铺
        ShopDto pShop = this.shopService.getShopMainOfCacheById(shopId);
        // 发送验证码
        UserDto userDB = memberServcie.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, "会员不存在");
        Long userId = userDB.getUserId();
        // 在该店铺可用红包金额
        Double redPacketAmount = packetServcie.getRedPacketAmountBy(shopId, userId,
                RedPacketStatusEnum.USEABLE.getValue());
        //计算代金券可用金额
        OrderDto orderDto = new OrderDto();
        orderDto.setShopId(shopId);
        orderDto.setSettlePrice(payAmount);
        Double avaliVoucherAmount = orderServcie.countVoucherDeduction(userId, orderDto);
        // 消费卡余额
        Double couponBalance = userCashCouponService.getUserCashCouponBalance(userId);
        // 账户当前余额
        Double amount = NumberUtil.add(redPacketAmount, couponBalance, avaliVoucherAmount);
        // 冻结金额
        Double freezeAmount = payAmount;
        // 查询传奇宝账户余额
        UserAccountDto account = userAccountService.getAccountMoney(userId);

        // 传奇宝账户正常
        if (null != account && CommonConst.ACCOUNT_NORMAL_STATUS == account.getAccountStatus())
        {
            // amount += account.getAmount();
            // freezeAmount += account.getFreezeAmount();
            // 账户余额=消费金余额+平台奖励余额
            BigDecimal couponAmount = NumberUtil.formatVal(account.getCouponAmount() + "", 2);
            BigDecimal rewardAmount = NumberUtil.formatVal(account.getRewardAmount() + "", 2);
            amount = (amount + (Double.parseDouble(couponAmount.add(rewardAmount) + "")));
            logger.info("用户账户消费金余额：" + couponAmount + "，奖励余额：" + rewardAmount + "，总余额（消费卡+消费金+奖励）：" + amount);
        }
        // 减去冻结金额后的的实际余额
        Double remainMoney = NumberUtil.sub(amount, freezeAmount);
        SmsReplaceContent src = new SmsReplaceContent();
        // 余额不足，发送短信
        if (remainMoney < 0)
        {
            if (null == account)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "用户传奇宝账号不存在");
            }
            if (CommonConst.ACCOUNT_FREEZE_STATUS == account.getAccountStatus())
            {
                throw new ValidateException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301,
                        CodeConst.MSG_USER_ACCOUNT_FROZEN);
            }
            src.setMobile(mobile);
            src.setAmount(NumberUtil.formatDouble(payAmount, 2));
            // src.setAcountAmount(NumberUtil.fmtDouble(amount-account.getFreezeAmount(),
            // 2));
            src.setAcountAmount(NumberUtil.formatDouble(amount, 2));
            src.setShopName(pShop.getShopName());
            if ("0".equals(partPayFlag))
            {
                src.setUsage(CommonConst.LACK_OF_BALANCE);
            }
            else if ("1".equals(partPayFlag))
            {
                Properties properties = ContextInitListener.COMMON_PROPS;
                double lowerAmount = 0.1;
                if (null != properties)
                {
                    String lowerAmountStr = properties.getProperty("sms_pay_lower_amount");
                    try
                    {
                        lowerAmount = Double.parseDouble(lowerAmountStr);
                        logger.info("读取配置文件中短信支付下限金额：" + lowerAmount);
                    }
                    catch (Exception e)
                    {
                        logger.warn("读取短信支付金额下限异常，使用默认下限金额操作：" + lowerAmount, e);
                    }
                }

                if (amount > lowerAmount)
                {
                    src.setCacheCodeFlag(true);
                    src.setCreateCodeFlag(true);
                    src.setUsage(CommonConst.SMS_PART_PAY);
                    sendSmsService.sendSmsMobileCode(src);
                    // 余额不足，部分支付
                    code = CodeConst.CODE_ACCOUNT_NOT_BALANCE_PARTPAY;
                }
                else
                {
                    throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,
                            CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
                }
            }

            if ("0".equals(partPayFlag))
            {
                throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE, CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
            }
        }// 余额足够，发送短信
        else
        {
            src.setMobile(mobile);
            src.setAmount(NumberUtil.formatDouble(payAmount, 2));
            src.setShopName(pShop.getShopName());
            src.setCacheCodeFlag(true);
            src.setCreateCodeFlag(true);
            src.setUsage(CommonConst.SMS_PAY_CODE);
            sendSmsService.sendSmsMobileCode(src);
        }
        return ResultUtil.getResultJsonStr(code, "获取信息成功", null);
    }

    /**
     * 验证传奇宝余额是否充足
     * @throws Exception 
     * 
     */
    @RequestMapping(value = "/session/isBalanceOk", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String isBalanceOk(HttpServletRequest request) throws Exception
    {
        int code = CodeConst.CODE_SUCCEED;
        /*
         * shopId int 是 商铺编号 token String 是 设备令牌 mobile String 是 手机号码
         * payAmount Double 是 订单的待支付金额
         */
        logger.info("验证传奇宝余额是否充足接口 -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

        // 手机
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        // 订单的待支付金额
        String payAmountStr = RequestUtils.getQueryParam(request, "payAmount");
        // 订单id
        // String orderIdStr = RequestUtils.getQueryParam(request,
        // "orderId");
        // if(StringUtils.isNotBlank(orderIdStr)){
        // OrderDto order = orderServcie.getOrderMainById(orderIdStr);
        // CommonValidUtil.validObjectNull(order,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_NOT_EXIST);
        // }

        String partPayFlag = RequestUtils.getQueryParam(request, "partPayFlag");// 是否部分支付
        if (StringUtils.isEmpty(partPayFlag))
        {
            partPayFlag = "0";
        }
        // shopid
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        // 手机
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, "手机不能为空");
        // 支付金额
        CommonValidUtil.validStrNull(payAmountStr, CodeConst.CODE_PARAMETER_NOT_NULL, "支付金额不能为空");
        Double payAmount = CommonValidUtil.validStrDoubleFmt(payAmountStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                "支付金额格式错误");
        if (payAmount == 0)
        {
            CommonValidUtil.validStrDoubleFmt(payAmountStr, CodeConst.CODE_PAY_STATUS_SUCCESS,
                    CodeConst.MSG_PAY_STATUS_SUCCESS);
        }
        String url = request.getRequestURI();
        if (CommonConst.PAYVERICODE_URL.equals(url))
        {
            // 令牌
            String token = RequestUtils.getQueryParam(request, "token");
            // token
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            collectService.queryShopAndTokenExists(shopId, token);
        }

        // 上面有校验店铺
        ShopDto pShop = this.shopService.getShopMainOfCacheById(shopId);
        // 发送验证码
        UserDto userDB = memberServcie.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, "会员不存在");
        Long userId = userDB.getUserId();
        // 消费卡余额
        Double couponBalance = userCashCouponService.getUserCashCouponBalance(userId);
        // 在该店铺可用红包金额
        Double redPacketAmount = packetServcie.getRedPacketAmountBy(shopId, userId,
                RedPacketStatusEnum.USEABLE.getValue());
        //计算代金券可用金额
        OrderDto orderDto = new OrderDto();
        orderDto.setShopId(shopId);
        orderDto.setSettlePrice(payAmount);
        Double avaliVoucherAmount = orderServcie.countVoucherDeduction(userId, orderDto);
        // 账户当前余额
        Double amount = NumberUtil.add(redPacketAmount, couponBalance, avaliVoucherAmount);
        // 冻结金额
        Double freezeAmount = payAmount;
        // 查询传奇宝账户余额
        UserAccountDto account = userAccountService.getAccountMoney(userId);
        // 传奇宝账户正常
        if (null != account && CommonConst.ACCOUNT_NORMAL_STATUS == account.getAccountStatus())
        {
            // amount += account.getAmount();
            // freezeAmount += account.getFreezeAmount();
            // 账户余额=消费金余额+平台奖励余额
            BigDecimal couponAmount = NumberUtil.formatVal(account.getCouponAmount() + "", 2);
            BigDecimal rewardAmount = NumberUtil.formatVal(account.getRewardAmount() + "", 2);
            amount = (amount + (Double.parseDouble(couponAmount.add(rewardAmount) + "")));
            logger.info("用户账户消费金余额：" + couponAmount + "，奖励余额：" + rewardAmount + "，总余额（消费卡+消费金+奖励）：" + amount);
        }
        // 红包金额

        // 减去冻结金额后的的实际余额
        Double remainMoney = NumberUtil.sub(amount, freezeAmount);
        SmsReplaceContent src = new SmsReplaceContent();
        // 余额不足，发送短信
        if (remainMoney < 0)
        {
            if (null == account)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "用户传奇宝账号不存在");
            }
            if (CommonConst.ACCOUNT_FREEZE_STATUS == account.getAccountStatus())
            {
                throw new ValidateException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301,
                        CodeConst.MSG_USER_ACCOUNT_FROZEN);
            }
            src.setMobile(mobile);
            src.setAmount(NumberUtil.formatDouble(payAmount, 2));
            src.setAcountAmount(NumberUtil.formatDouble(amount, 2));
            src.setShopName(pShop.getShopName());
            if ("0".equals(partPayFlag))
            {
                src.setUsage(CommonConst.LACK_OF_BALANCE);
            }
            else if ("1".equals(partPayFlag))
            {
                Properties properties = ContextInitListener.COMMON_PROPS;
                double lowerAmount = 0.1;
                if (null != properties)
                {
                    String lowerAmountStr = properties.getProperty("sms_pay_lower_amount");
                    try
                    {
                        lowerAmount = Double.parseDouble(lowerAmountStr);
                        logger.info("读取配置文件中短信支付下限金额：" + lowerAmount);
                    }
                    catch (Exception e)
                    {
                        logger.warn("读取短信支付金额下限异常，使用默认下限金额操作：" + lowerAmount, e);
                    }
                }

                if (amount > lowerAmount)
                {
                    src.setCacheCodeFlag(true);
                    src.setCreateCodeFlag(true);
                    src.setUsage(CommonConst.SMS_PART_PAY);
                    sendSmsService.sendSmsMobileCode(src);
                    // 余额不足，部分支付
                    code = CodeConst.CODE_ACCOUNT_NOT_BALANCE_PARTPAY;
                }
                else
                {
                    throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,
                            CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
                }
            }

            if ("0".equals(partPayFlag))
            {
                throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE, CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
            }
        }
        return ResultUtil.getResultJsonStr(code, "验证传奇宝余额是否充足成功", null);
    }

    @RequestMapping(value = "/delAndResumeOrder", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delAndResumeOrder(HttpServletRequest request) throws Exception
    {
        logger.info("删除、恢复订单操作 -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        // 令牌
        String token = RequestUtils.getQueryParam(request, "token");
        // 订单id
        String orderId = RequestUtils.getQueryParam(request, "orderId");
        // 操作分类标识 0：代表删除，1：代表恢复
        String operationType = RequestUtils.getQueryParam(request, "operationType");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(operationType, CodeConst.CODE_PARAMETER_NOT_NULL, "操作类型不允许为空");

        collectService.queryShopAndTokenExists(shopId, token);

        collectService.delAndResumeOrder(shopId, orderId, operationType);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
    }

    /**
     * 上报店铺应用版本信息
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.reportAppVersion
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @throws Exception 
     * @date:2015年9月21日 下午2:20:11
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月21日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value = "/reportAppVersion", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String reportAppVersion(HttpServletRequest request) throws Exception
    {
        logger.info("上报店铺应用版本信息 -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        // 令牌
        String token = RequestUtils.getQueryParam(request, "token");
        // 应用名称
        String appName = RequestUtils.getQueryParam(request, "appName");
        // 应用描述
        String appDesc = RequestUtils.getQueryParam(request, "appDesc");
        // 应用版本
        String appVersion = RequestUtils.getQueryParam(request, "appVersion");
        // 序列号
        String sn = RequestUtils.getQueryParam(request, "sn");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        CommonValidUtil.validStrNull(appName, CodeConst.CODE_PARAMETER_NOT_NULL, "应用名称不允许为空");
        CommonValidUtil.validStrNull(appVersion, CodeConst.CODE_PARAMETER_NOT_NULL, "应用版本号不允许为空");
        CommonValidUtil.validStrNull(sn, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        collectService.queryShopAndTokenExists(shopId, token);

        collectService.reportAppVersion(shopId, appName, appDesc, appVersion, sn);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "调用成功", null);
    }

    /**
     * 获取标品商品信息接口
     * 
     * @Function: com.idcq.appserver.controller.collect.CollectController.
     *            queryStandardGoods
     * @Description:
     * 
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @throws Exception 
     * @date:2015年9月21日 下午2:21:06
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月21日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value =
    { "/common/queryStandardGoods", "/session/common/queryStandardGoods" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryStandardGoods(HttpServletRequest request) throws Exception
    {
        logger.info("获取标品商品信息接口  -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        // 令牌
        String token = RequestUtils.getQueryParam(request, "token");
        // 商品条码
        String barcode = RequestUtils.getQueryParam(request, "barcode");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        String url = request.getRequestURI();
        if (url.contains(CommonConst.QUERYSTANDARDGOODS_URL))
        {
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            // 校验token和店铺信息是否正常
            collectService.queryShopAndTokenExists(shopId, token);
        }
        CommonValidUtil.validStrNull(barcode, CodeConst.CODE_PARAMETER_NOT_NULL, "商品条码不允许为空");
        Map map = collectService.queryStandardGoods(barcode);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取标品商品信息成功", map);
    }

    /**
     * 通过条形码获取商品信息接口
     * @throws Exception 
     */
    @RequestMapping(value = "/session/common/queryGoodsByBarcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryGoodsByBarcode(HttpServletRequest request) throws Exception
    {
        logger.info("通过条形码获取商品信息接口  -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        // 商品条码
        String barcode = RequestUtils.getQueryParam(request, "barcode");
        
        String goodsStatusStr = RequestUtils.getQueryParam(request, "goodsStatus");
        
        String searchFlagStr = RequestUtils.getQueryParam(request, "searchFlag");
        
        Integer searchFlag = null;
        if(null != searchFlagStr){
        	searchFlag = CommonValidUtil.validStrIntFmt(searchFlagStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SEARCHFLAG);
        }else{
        	searchFlag = 1;
        }
        
        Integer goodsStatus = 1;
        if(StringUtils.isBlank(goodsStatusStr)){
        	goodsStatus = 1;//默认上架状态'上架状态，下架-0,上架-1,删除-2,草稿-3,4全部'
        }
//            goodsStatus = NumberUtil.strToNum(goodsStatusStr, "goodsStatus");
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);

        CommonValidUtil.validStrNull(barcode, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG__REQUIRED_GOODSBARCODE);
        List<Map> map = collectService.queryGoodsByBarcode(shopId, barcode,goodsStatus,searchFlag);
        if(searchFlag == 1){
        	if(null != map && map.size() > 0){
        		return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "通过条形码获取商品信息接口成功", map.get(0));
        	}else{
        		return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "通过条形码获取商品信息接口成功", null);
        	}
        }
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "通过条形码获取商品信息接口成功", map);

    }

    /**
     * 34.同步商品信息接口
     * 
     * @Function: 
     *            com.idcq.appserver.controller.collect.CollectController.synGoodsInfo
     * @Description: 新增或修改商品信息，如果单位在单位表中找不到需要新增单位
     * 
     * @param entity
     * @param request
     * @return
     * 
     * @version:v1.0
     * @author:shengzhipeng
     * @throws Exception 
     * @date:2015年9月21日 下午2:44:48
     * 
     *                  Modification History: Date Author Version Description
     *                  ----
     *                  ------------------------------------------------------
     *                  ------- 2015年9月21日 shengzhipeng v1.0.0 create
     */
    @RequestMapping(value = {"/shop/synGoodsInfo","/service/shop/synGoodsInfo","/token/shop/synGoodsInfo","/session/shop/synGoodsInfo"}, 
    				produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String synGoodsInfo(HttpEntity<String> entity, HttpServletRequest request) throws Exception
    {
        logger.info("同步商品信息接口   -start");

        StandardGoodsDto standardGoodsDto = (StandardGoodsDto) JacksonUtil.postJsonToObj(entity,
                StandardGoodsDto.class);
        Map map = collectService.syncGoodsInfo(standardGoodsDto);
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "同步商品成功！", map);
    }

    @RequestMapping(value =
    { "/shop/queryShopAccount", "session/shop/queryShopAccount", "service/shop/queryShopAccount",
            "token/shop/queryShopAccount" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryShopAccount(HttpServletRequest request) throws Exception
    {
        logger.info("查询商铺账户余额接口    -start");
        // shopid
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        Map<String, Object> parms = new HashMap<String, Object>();
        List<Long> shopIds = new ArrayList<Long>();
        shopIds.add(shopId);
    	parms.put("shopId",shopIds);
        Map<String, Object> map = shopService.getShopAccountMoney(parms);
        if (null == map)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账号不存在");
        }
        return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询商铺账户余额成功！", map);
    }

    /**
     * @throws Exception 
     * 组合支付
     * @Title: multiplePay
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value =
    { "/multiplePay", "/service/order/multiplePay", "/token/order/multiplePay", "/session/order/multiplePay" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = "application/json")
    public @ResponseBody
    String multiplePay(HttpServletRequest request, HttpEntity<String> entity) throws Exception 
    {
        logger.info("组合支付-start" + entity.toString());
        MultiPayDto multiPayDto = (MultiPayDto) JacksonUtil.postJsonToObj(entity, MultiPayDto.class,
                DateUtils.DATETIME_FORMAT);
        logger.info("multiPayDto参数：" + multiPayDto);
        String requestPath = request.getRequestURI();
        if (CommonConst.MULTIPLEPAY_URL.equals(requestPath))
        {
            String token = multiPayDto.getToken();
            // token不能为空
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            // 1.1 商铺及token校验
            collectService.queryShopAndTokenExists(multiPayDto.getShopId(), token);
        }
        if (null != multiPayDto && multiPayDto.getData() != null)
        {
            // 参数格式合法性校验
            validReqParams(multiPayDto);
            synchronized (this)
            {
                // 参数有效性校验
                Map<String, Object> re = validMultiplePay(multiPayDto);
                List<OrderGoodsServiceTech> serviceTechs = getGoodsTechList(multiPayDto);
                //是否非会员订单
                int isWhatOrder = (int) re.get("isOrder");
                Map<String, Object> result = null;
                if (isWhatOrder == 0)
                {
                    // 非会员订单支付
                    OrderDto xorderDto = (OrderDto) re.get("order");

                    result = collectService.multiplePayFromXorder(multiPayDto, xorderDto, serviceTechs);
                    List<Map<String, Object>> paidList = collectService.getPayRecordByOrderId(multiPayDto.getData()
                            .getId(), 1);
                    result.put("paidList", paidList);
                }
                else
                {
                    // 会员订单支付
                    OrderDto orderDto = (OrderDto) re.get("order");
                    result = collectService.multiplePayFromOrder(multiPayDto, orderDto, serviceTechs);
                    List<Map<String, Object>> paidList = collectService.getPayRecordByOrderId(multiPayDto.getData()
                            .getId(), 1);
                    result.put("paidList", paidList);
                    Integer payType = multiPayDto.getPayType();
                    String mobile = multiPayDto.getData().getMobile();
                    //会员卡支付发送短信
                    if(null != payType && payType == CommonConst.MULTI_PAYTYPE_SHOP_MEMBER_CARD) {
                        SmsReplaceContent sms = new SmsReplaceContent();
                        String payMoney =  result.get("payMoney").toString();
                        String afterAmount = result.get("afterAmount").toString();
                        sms.setUsage("card_pay");
                        sms.setAmount(Double.parseDouble(NumberUtil.formatVal(afterAmount + "", 2) + ""));//消费后的余额
                        ShopDto shopDto = shopServcie.getShopById(multiPayDto.getShopId());
                        sms.setShopName(shopDto.getShopName());
                        sms.setMoney(Double.parseDouble(NumberUtil.formatVal(payMoney + "", 2) + "")); // 消费金额
                        sms.setMobile(mobile);
                        sms.setConsumeDate(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT1));
                        sendSmsService.sendSmsMobileCode(sms);
                    }
                    // 需要发送验证码告诉消费者支付金额及余额
                    SmsReplaceContent src = buildSmsContent(result, mobile);
                    logger.info("短信支付成功短信内容：" + src.toString());
                    // 支付类型为-1时也不用发短信
                    if (!"sms_pay".equals(src.getUsage())
                            && (null != payType && payType != -1))
                    {
                        sendSmsService.sendSmsMobileCode(src);
                    }
                    result.remove("smsPayAmount");
                    int type = multiPayDto.getPayType();
                    if (type == CommonConst.MULTI_PAYTYPE_CQB)
                    {
                        result.remove("onLinePayment");
                    }
                    else if (type == CommonConst.MULTI_PAYTYPE_XFK)
                    {
                        result.remove("cashCouponPayment");
                    }
                    else if (type == CommonConst.MULTI_PAYTYPE_HB)
                    {
                        result.remove("usedRedPacketMoney");
                    }
                    else if (type == CommonConst.MULTI_PAYTYPE_SMS)
                    {
                        result.remove("onLinePayment");
                        result.remove("cashCouponPayment");
                        result.remove("usedRedPacketMoney");
                    }
                    
                   /* //增加店内会员消费次数--------该功能移动订单结算接口
                  	if (orderDto.getUserId() != null && multiPayDto.getPayType().intValue() != -1) {
        				shopMemberValify.isShopMemberValify(orderDto.getShopId(),userDao.getMobileByUserId(orderDto.getUserId()), (BigDecimal) result.get("payMoney"),true);
        			}*/
                }
          
                result.put("payIndex", multiPayDto.getPayIndex());
                result.put("paidTime", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                result.put("payType", multiPayDto.getPayType());
                return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "支付成功！", result, DateUtils.DATETIME_FORMAT);
            }
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "参数有误");
        }
    }

    /**
     * 组合支付参数合法性校验
     * @param multiPayDto
     * @throws Exception
     */
    private void validReqParams(MultiPayDto multiPayDto) throws Exception
    {
        // 商铺信息及支付信息参数合法性校验
        Integer payType = multiPayDto.getPayType();
        if (multiPayDto.getShopId() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商铺编号不能为空");
        } else {
            ShopDto pShop = this.shopServcie.getShopMainOfCacheById(multiPayDto.getShopId());
            CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            Integer shopStatus = pShop.getShopStatus();
            if (shopStatus == null || shopStatus != 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_STATUS);
            }
        }
        if (payType == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "支付方式不能为空");
        }
        if (multiPayDto.getPayMoney() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "需支付金额不能为空");
        }
        if (multiPayDto.getPayClientTime() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "客户端支付时间不能为空");
        }
        if (multiPayDto.getPayIndex() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "订单内支付次序不能为空");
        }
        if ((payType == CommonConst.MULTI_PAYTYPE_SMS || payType == CommonConst.MULTI_PAYTYPE_CQB
                || payType == CommonConst.MULTI_PAYTYPE_XFK || payType == CommonConst.MULTI_PAYTYPE_HB)
                && multiPayDto.getVeriCode() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "验证码不能为空");
        }
        // 组合支付订单信息参数合法性校验
        MultiPayOrderDto payOrderDto = multiPayDto.getData();
        if (payOrderDto.getDeductAmount() == null && payOrderDto.getUserShopCouponIdList() != null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时deductAmount必填");
        }
        
        if (payOrderDto.getDeductAmount() != null && payOrderDto.getUserShopCouponIdList() == null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时userShopCouponIdList必填");
        }
        if (payOrderDto.getCreateTime() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "下单时间不能为空");
        }
        if (payOrderDto.getOrderSceneType() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "订单场景分类不能为空");
        }
        if (payOrderDto.getDiscount() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "会员折扣不能为空");
        }
        if (payOrderDto.getSettlePrice() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "订单折算价格不能为空");
        }
        if (payOrderDto.getBillerId() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "下单员ID不能为空");
        }
        CommonValidUtil.validStrNull(payOrderDto.getId(), CodeConst.CODE_PARAMETER_NOT_NULL, "订单编号不能为空");
        // 组合支付订单详细信息参数合法性校验
        List<MultiPayOrderInfoDto> payOrderInfoDtos = payOrderDto.getOrderInfo();
        for (MultiPayOrderInfoDto payOrderInfoDto : payOrderInfoDtos)
        {
            if (payOrderInfoDto.getDishId() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商品编号不能为空");
            }
            if (payOrderInfoDto.getStandardPrice() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商品目录价格不能为空");
            }
            if (payOrderInfoDto.getShopPrice() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "改价或商铺折后价不能为空");
            }
            if (payOrderInfoDto.getStandardPrice() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "会员折后价不能为空");
            }
            if (payOrderInfoDto.getNum() == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "商品数量不能为空");
            }
        }
    }

    private Map<String, Object> validMultiplePay(MultiPayDto multiPayDto) throws Exception
    {
        // 1.2订单校验
        MultiPayOrderDto payOrderDto = multiPayDto.getData();// 支付订单信息
        String orderId = payOrderDto.getId();
        Integer payType = multiPayDto.getPayType();
        boolean paySmsFlag = false;
        if (paySmsFlag = (payType == CommonConst.MULTI_PAYTYPE_SMS || payType == CommonConst.MULTI_PAYTYPE_CQB
                || payType == CommonConst.MULTI_PAYTYPE_XFK || payType == CommonConst.MULTI_PAYTYPE_HB))
        {
            CommonValidUtil.validStrNull(payOrderDto.getMobile(), CodeConst.CODE_PARAMETER_NOT_NULL, "手机号码不能为空");
            // 验证码校验
            validVerCode(payOrderDto.getMobile(), multiPayDto.getVeriCode() + "");
        }
        return validOrder(multiPayDto, paySmsFlag, orderId);
    }

    /**
     * 短信验证码校验
     * @param multiPayDto
     * @throws Exception
     */
    private void validVerCode(String mobile, String veriCode) throws Exception
    {
        CommonValidUtil.validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
        boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode);
        if (!flag)
        {
            throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
        }
    }

    /**
     * 订单校验
     * @param multiPayDto
     * @param paySmsFlag 是否短信支付
     * @param orderId
     * @return map
     * @throws Exception
     */
    public Map<String, Object> validOrder(MultiPayDto multiPayDto, boolean paySmsFlag, String orderId) throws Exception
    {
        OrderDto orderDto = orderServcie.queryOrderDtoById(orderId);
        int status = CommonConst.ORDER_STS_YKD; // 已开单 默认
        boolean orderExistsFlag = true;// 是否存在会员订单 true-不存在 false-存在
        boolean xorderExistsFlag = true;// 是否存在非会员订单 true-不存在 false-存在
        int isWhatOrder = 0;// 当次支付最终订单标识 0-非会员订单 1-会员订单
        boolean orderDtoIsNull = false;// 是否存在订单
        if (orderDto != null)
        {
            Integer isMemberFlag = orderDto.getIsMember();
            if (null != isMemberFlag)
            {
                isWhatOrder = isMemberFlag;
                orderExistsFlag = getOrderExistsFlag(isMemberFlag);
                xorderExistsFlag = getXOrderExistsFlag(isMemberFlag);
            }
            status = orderDto.getOrderStatus();
            Integer orderSatus = multiPayDto.getData().getOrderStatus();
            if ((CommonConst.ORDER_STS_ZZXD == status || CommonConst.ORDER_STS_DQR == status
                    || CommonConst.ORDER_STS_YYD == status) && orderSatus != null && orderSatus != CommonConst.ORDER_STS_YJZ ) {
                status = multiPayDto.getData().getOrderStatus();
            }
        }
        else
        {
            orderDto = new OrderDto();
            orderDtoIsNull = true;
        }
        MultiPayOrderDto data = multiPayDto.getData();
        String mobile = data.getMobile();
        UserDto user = memberServcie.getUserByMobile(mobile);
        if (user != null) {
        	orderDto.setUserId(user.getUserId());
        }
        
        if ((null != user && user.getIsMember() == CommonConst.USER_IS_MEMBER) || paySmsFlag || !orderExistsFlag)
        {
            isWhatOrder = 1;
        }
        Integer payIndex = multiPayDto.getPayIndex();// 当前支付序号
        if ((CommonConst.ORDER_STS_YJZ == status && CommonConst.REVERSE_SETTLE_FLAG != data.getSettleFlag().intValue()) 
        		|| CommonConst.ORDER_STS_TDZ == status
                || CommonConst.ORDER_STS_YTD == status)
        {
            // 订单状态为不可支付状态
            throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR, CodeConst.MSG_ORDER_STATUS_ERROR);
        }
        Integer dbPayIndex = orderServcie.getMaxPayIndex(orderId, 1);// 数据库中最大的支付序号，为null则没有支付记录
        // 校验支付序号
        if (null != dbPayIndex)
        {
            if (payIndex.intValue() < dbPayIndex.intValue())
            {
                throw new ValidateException(CodeConst.CODE_72404, "降号提交支付-" + dbPayIndex);
            }
            if (payIndex.intValue() == dbPayIndex.intValue())
            {
                throw new ValidateException(CodeConst.CODE_72402, "重复提交支付-" + dbPayIndex);
            }
            if (payIndex.intValue() > (dbPayIndex.intValue() + 1))
            {
                throw new ValidateException(CodeConst.CODE_72403, "跳号提交支付-" + dbPayIndex);
            }
        }
        else
        {
            if (payIndex.intValue() != 1)
            {
                throw new ValidateException(CodeConst.CODE_72403, "跳号提交支付");
            }
        }
        Map<String, Object> re = new HashMap<String, Object>();
        re.put("isOrder", isWhatOrder);
        if (isWhatOrder == 0)
        {
            // 非会员订单
            if (!xorderExistsFlag)
            {
                // 如果不是第一次支付，查询订单支付记录
                BigDecimal payAmount = collectService.queryOrderPayAmount(orderId, 0);
                Double settlePrice = multiPayDto.getData().getSettlePrice();
                if (null != payAmount && settlePrice != null)
                {
                    BigDecimal price = new BigDecimal(settlePrice + "");
                    if (payAmount.compareTo(price) > 0 && CommonConst.REVERSE_SETTLE_FLAG != data.getSettleFlag().intValue())
                    {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "订单支付金额大于订单总金额！");
                    }
                }

            }
            List<OrderGoodsDto> orderGoodsDtos = convertToXOrder(multiPayDto, status, orderDto, xorderExistsFlag);
            orderDto.setOrderChannelType(CommonConst.ORDER_CHANNEL_POS_CQB);// 订单下单渠道为收银机
            orderServcie.insertOrUpdateXOrderDto(orderDto, orderDtoIsNull, orderGoodsDtos);
            if (orderDtoIsNull && orderDto.getCouponDiscountPrice() != null) {
            	orderServcie.updateShopCoupon(orderDto.getUserShopCouponIdList(), orderId, multiPayDto.getShopId(), orderDto.getMobile());
            }
            orderDto.setGoods(orderGoodsDtos);
            re.put("order", orderDto);
        }
        else
        {
            // 会员订单
            if (!orderExistsFlag)
            {
                // 如果不是第一次支付，查询订单支付记录
                BigDecimal payAmount = collectService.queryOrderPayAmount(orderId, 0);
                Double settlePrice = multiPayDto.getData().getSettlePrice();
                if (null != payAmount && settlePrice != null)
                {
                    BigDecimal price = new BigDecimal(settlePrice + "");
                    if (payAmount.compareTo(price) > 0 && CommonConst.REVERSE_SETTLE_FLAG != data.getSettleFlag().intValue())
                    {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "订单支付金额大于订单总金额鸟~");
                    }
                }
            }
            convertToOrder(multiPayDto, status, orderDto, orderExistsFlag);
            orderDto.setOrderChannelType(CommonConst.ORDER_CHANNEL_POS_CQB);// 订单下单渠道为收银机
            // 生成订单记录，有则新增，无则新增
            orderServcie.insertOrUpdateOrderDto(orderDto, orderDtoIsNull, xorderExistsFlag);
            if (orderDtoIsNull && orderDto.getCouponDiscountPrice() != null) {
            	orderServcie.updateShopCoupon(orderDto.getUserShopCouponIdList(), orderId, multiPayDto.getShopId(), orderDto.getMobile());
            }
            
            //1、更新优惠券数量 2、更新用户持有店铺优惠券状态
            re.put("order", orderDto);
        }
        return re;
    }

    private boolean getOrderExistsFlag(Integer isMemberFlag)
    {
        if (isMemberFlag == 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean getXOrderExistsFlag(Integer isMemberFlag)
    {
        if (isMemberFlag == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 将参数转为订单对象
     * @param multiPayDto
     * @param status 转换成订单还是非会员订单 0-会员订单 1-非会员订单
     * @param orderDto
     * @param orderExists
     * @throws Exception
     */
    private void convertToOrder(MultiPayDto multiPayDto, Integer status, OrderDto orderDto, boolean orderExists)
            throws Exception
    {
        logger.info("====开始将请求参数转换为订单信息");
        // goods_price_before_discount：未做任何修改之前的价格
        // goods_price：折后商品总价，包括改价、打折
        // order_total_price：goods_price+logistics_price（折后总价+物流费用）
        // settle_price：实收金额
        MultiPayOrderDto payOrderDto = multiPayDto.getData();
        String mobile = payOrderDto.getMobile();
        Long shopId = multiPayDto.getShopId();
        ShopDto shopDto = shopService.getShopById(multiPayDto.getShopId());
        // Double memberDiscount =
        // collectService.getMemberDiscount(multiPayDto.getShopId());
        Double memberDiscount = (payOrderDto.getDiscount() == null ? 1.0 : (payOrderDto.getDiscount().doubleValue() <= 0 ? 1.0 : payOrderDto
                .getDiscount()));
        logger.info("=====会员折扣：" + memberDiscount);
        UserDto user = memberServcie.getUserByMobile(payOrderDto.getMobile());
        if(null != user) {
            orderDto.setUserId(user.getUserId());
            orderDto.setMobile(mobile);
        }
        if(orderDto.getUserId() == null) {
            CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        } 
        orderDto.setOrderId(payOrderDto.getId());
        orderDto.setOrderTime(payOrderDto.getCreateTime());
        orderDto.setLastUpdateTime(new Date());
        orderDto.setPayStatus(0);
        orderDto.setPrepayMoney(0d);// 预付金额s
        orderDto.setPayTimeType(0);// 支付时间的类型：网上支付-0，货到付款-1
        orderDto.setOrderSceneType(payOrderDto.getOrderSceneType());// 订单场景分类：0（预定单），1（到店点菜订单），2（外卖订单），3（服务订单），4（商品订单）
        orderDto.setOrderType(1); // 全额订单*
        orderDto.setShopId(shopId);// 订单所属商铺
        orderDto.setIsMaling(payOrderDto.getIsMaling());
        orderDto.setOrderDiscount(payOrderDto.getAdditionalDiscount());
        orderDto.setSettleFlag(payOrderDto.getSettleFlag());

        Map<String, Object> map = getOrderGoodsList(multiPayDto, memberDiscount, orderExists);
        orderDto.setLogisticsPrice(payOrderDto.getOutfee() == null ? 0 : payOrderDto.getOutfee());// 物流费用
        orderDto.setGoodsPriceBeforeDiscount(Double.parseDouble(map.get("goodsPriceBeforeDiscount") + ""));// 折前商品总价
        orderDto.setGoodsPrice(Double.parseDouble(map.get("goodsPrice") + ""));// 商品总价（折后)
        orderDto.setOrderTotalPrice(orderDto.getGoodsPrice() + orderDto.getLogisticsPrice());// 订单总价(折后价+物流费用)
        orderDto.setSettlePrice(payOrderDto.getSettlePrice());// 参与结算的价格
        orderDto.setShopMemberDiscount(payOrderDto.getShopMemberDiscount());

        if (payOrderDto.getDeductAmount() != null) {
        	orderDto.setCouponDiscountPrice(payOrderDto.getDeductAmount());
        }
      
        if (payOrderDto.getUserShopCouponIdList() != null) {
        	orderDto.setUserShopCouponIdList(payOrderDto.getUserShopCouponIdList());
        }
        
        
        orderDto.setOrderRealSettlePrice(NumberUtil.min(Double.parseDouble("" + map.get("orderRealSettlePrice")),
                orderDto.getSettlePrice()));// 平台实收款内参与结算金额
        orderDto.setGoods((List<OrderGoodsDto>) map.get("orderGoodsDtos"));// 订单商品
        orderDto.setGoodsNumber(Integer.parseInt(map.get("orderGoodsNum") + ""));// 订单商品数量
        orderDto.setOrderDiscount(payOrderDto.getAdditionalDiscount()); // 折上折
        orderDto.setCashierId(payOrderDto.getCashierId());
        orderDto.setOrderTitle(map.get("orderTitle") + "");
        orderDto.setOrderStatus(status);
        orderDto.setIsMember(CommonConst.USER_IS_MEMBER);
        // orderDto.setOrderChannelType(2);//下单方式
        orderDto.setMemberDiscount(memberDiscount);// 会员折扣
        Long businessAreaActivityId = multiPayDto.getData().getBusinessAreaActivityId();
        if(businessAreaActivityId != null) {
            orderDto.setBusinessAreaActivityId(businessAreaActivityId);
        }
        orderDto.setRemark(payOrderDto.getUserRemark());
        orderDto.setClientSystemType(multiPayDto.getClientSystem());
        orderDto.setClientFinishTime(DateUtils.parse(multiPayDto.getPayClientTime(), DateUtils.DATETIME_FORMAT));
        orderDto.setCashierUsername(payOrderDto.getCashierUsername());
        orderDto.setIsWait(payOrderDto.getIsWait());
        orderDto.setOrderDiscountPrice(payOrderDto.getOrderDiscountPrice());
        orderDto.setTokenId(payOrderDto.getTokenId());
        orderDto.setConsumeType(payOrderDto.getConsumeType());
        if(StringUtils.isNotBlank(payOrderDto.getSeatIds()) && !payOrderDto.getSeatIds().equals("-1") && !payOrderDto.getSeatIds().equals("0")) {
            if (!payOrderDto.getSeatIds().equals(orderDto.getSeatIds())) {
                String[] seatIds = payOrderDto.getSeatIds().split(",");
                for (String seatIdStr : seatIds) {
                    orderServcie.checkShopSeatValid(Long.valueOf(seatIdStr), shopId);
                }
            }
            orderDto.setSeatIds(payOrderDto.getSeatIds());
            if (null != status && status == CommonConst.ORDER_STS_YKD)
            {
                //防止改桌造成占用两个桌子，先对资源进行释放在进行预订
                orderServcie.updateShopResourceStatus(orderDto.getOrderId());
                // 预定资源
                orderServcie.reservResource(payOrderDto.getSeatIds(), orderDto.getOrderId());
            }
        }
        if (null != shopDto && orderExists)
        {
            orderDto.setSettleType(shopDto.getOrderSettleType());
        }
        
        logger.info("====最终订单信息：" + orderDto);
    }

    /**
     * 获取会员订单商品信息
     * @param multiPayDto
     * @param memberDiscount 会员折扣 非会员订单默认为1
     * @param orderExists 订单是否存在
     * @return
     * @throws Exception
     */
    private Map<String, Object> getOrderGoodsList(MultiPayDto multiPayDto, Double memberDiscount, boolean orderExists)
            throws Exception
    {
        logger.info("====开始获取订单商品信息");
        Map<String, Object> map = new HashMap<String, Object>();
        MultiPayOrderDto payOrderDto = multiPayDto.getData();
        List<MultiPayOrderInfoDto> payOrderInfoDtos = payOrderDto.getOrderInfo();
        Long shopId = multiPayDto.getShopId();
        String orderId = payOrderDto.getId();
        Double goodsPrice = 0d;// 折后价
        Double goodsPriceBeforeDiscount = 0d;// 折前价
        Double orderRealSettlePrice = 0d;// 参与结算的价格
        StringBuilder orderTitle = new StringBuilder("");// 订单标题
        double goodsNum = 0;
        List<OrderGoodsDto> orderGoodsDtos = new ArrayList<OrderGoodsDto>();
        OrderGoodsDto orderGoodsDto = null;
        if (CollectionUtils.isEmpty(payOrderInfoDtos))
        {
            goodsPrice = multiPayDto.getData().getSettlePrice();
            goodsPriceBeforeDiscount = multiPayDto.getData().getOrderPrice();
            map.put("orderGoodsDtos", orderGoodsDto);// 会员订单商品信息
            map.put("goodsPrice", goodsPrice);// 订单折后价格
            map.put("orderRealSettlePrice", goodsPrice);// 订单参与结算的价格
            map.put("goodsPriceBeforeDiscount", goodsPriceBeforeDiscount == null ? goodsPrice : goodsPriceBeforeDiscount);// 订单折前价格
            map.put("orderGoodsNum", 0);// 订单商品数：订单商品总数向下取整，如：4.8，则取4
            map.put("orderTitle", "管家收银");// 订单商品数
            return map;
        }
        int i = 0;
        // 商品技师服务列表
        for (MultiPayOrderInfoDto payOrderInfo : payOrderInfoDtos)
        {
            Long goodsId = payOrderInfo.getDishId();
            GoodsDto goods = goodsServcie.getGoodsByIds(shopId, goodsId);
            // 商品打折标记(默认1-打折)
            Integer goodsSettleFlag = 0;
            // 商品返点标记(默认1-返点)
            Integer goodsRebateFlag = 0;
            // 商品订单级别是否打折标记(默认1-返点)
            Integer isOrderDiscount = 1;
            // 商品名称
            String goodsUnitName = "";
            String goodsName = "";
            String specsDesc = "";
            boolean queryFlag = false;
            Integer goodsType = 1000;
            // 如果不是首次支付，则从订单商品表中先查询
            if (!orderExists)
            {
                OrderGoodsDto params = new OrderGoodsDto();
                params.setGoodsId(goodsId);
                params.setOrderId(orderId);
                params.setShopId(shopId);
                OrderGoodsDto dto = orderServcie.getOrderGoodsByGoodsId(params);
                if (dto != null)
                {
                    goodsSettleFlag = dto.getGoodsSettleFlag();
                    goodsRebateFlag = dto.getGoodsRebateFlag();
                    isOrderDiscount = dto.getIsOrderDiscount();
                    goodsName = dto.getGoodsName();
                    specsDesc = dto.getSpecsDesc();
                    queryFlag = true;
                }
            }
            if (!queryFlag)
            {
                if(goodsId == CommonConst.CUSTOM_GOODS_FLAG) {
                    goodsSettleFlag = CommonConst.GOODS_SETTLE_FLAG_FALSE;
                    goodsRebateFlag = CommonConst.GOODS_REBATE_FLAG_TRUE;
                    goodsName = "自定义商品";
                    specsDesc = payOrderInfo.getSpecsDesc();
                    if(goods!=null){
                        goodsType = goods.getGoodsType();
                    }
                } else {
                    if (goods == null)
                    {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺商品数据不存在");
                    }
                    goodsSettleFlag = goods.getGoodsSettleFlag();
                    goodsRebateFlag = goods.getGoodsRebateFlag();
                    isOrderDiscount = goods.getIsOrderDiscount();
                    goodsName = goods.getGoodsName();
                    specsDesc = goods.getSpecsDesc();
                    goodsUnitName = goods.getUnitName();
                    goodsType = goods.getGoodsType();
                }
               
            }
            if(null!=goods){
                goodsType = goods.getGoodsType();
            }
            /*
             * 订单商品： goods_required_price：会员折扣*原价*数量 非会员就没有会员折扣
             * goods_settle_price：会员折扣*改价后的价格*数量 非会员就没有会员折扣
             * order_goods_discount：菜品自定折扣（）
             * 
             * 订单： order_real_settle_price 真正参与结算的价格（服务端分成用） 商品返点*数量
             * settle_price 实收款 order_total_price 物流费+goods_price
             * goods_price_before_discount 目录价*数量 goods_price 目录价*数量*会员折扣 不一定都乘以
             * 菜不打折不乘
             */
            
            // 商品改价后的价格
            Double shopPrice = payOrderInfo.getShopPrice();
            // 商品目录价
            Double standardPrice = payOrderInfo.getStandardPrice();
            // 商品增值服务
            List<GoodsAvsDto> goodsAvsList = payOrderInfo.getAvsSpecsList();
            String avsSpecs = null;
            if (CollectionUtils.isNotEmpty(goodsAvsList)) {
            	List<Map<String, Object>> goodsAvsMapList = new LinkedList<Map<String,Object>>();
            	Double goodsAvsTotalPrice = Double.valueOf(0);
            	for (GoodsAvsDto goodsAvs : goodsAvsList) {
            		Long goodsAvsId = goodsAvs.getGoodsAvsId();
            		String avsName = goodsAvs.getAvsName();
            		Double avsPrice = goodsAvs.getAvsPrice();
            		if (goodsAvsId == null) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务Id不能为空");
            		}
            		
            		if (StringUtils.isBlank(avsName)) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务名称不能为空");
            		}
            		
            		if (avsPrice == null) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务价格不能为空");
            		}
            		goodsAvsTotalPrice = NumberUtil.add(goodsAvsTotalPrice, avsPrice);
            		Map<String, Object> goodsAvsMap = new LinkedHashMap<String, Object>();
            		goodsAvsMap.put("goodsAvsId", goodsAvsId);
            		goodsAvsMap.put("avsName", avsName);
            		goodsAvsMap.put("avsPrice", avsPrice);
            		goodsAvsMapList.add(goodsAvsMap);
            	}
            	
       /*     	if (!NumberUtil.add(goodsAvsTotalPrice,standardPrice).equals(shopPrice)) {
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
            									"下单商品含有增值服务时改价后shopPrice校验错误   商品增值服务总价格："+goodsAvsTotalPrice+
            									"商品目录价："+standardPrice+"改价后的价格："+shopPrice);
            	}*/
            	
            	avsSpecs = JSON.toJSONString(goodsAvsMapList);
            }
            
            // 订单商品数量
            Double num = payOrderInfo.getNum() == null ? 1 : payOrderInfo.getNum();
            // 计算订单折后价：折后价=商品改价后的价格*商品数量*会员折扣
            Double goodsSettlePrice = NumberUtil.multiply(shopPrice, num);
            // 商品本应支付的金额：会员折扣*原价*数量
            Double goodsRequiredPrice = NumberUtil.multiply(standardPrice, num);

            // 计算订单级别的价格
            // 计算订单折前价
            goodsPriceBeforeDiscount = NumberUtil
                    .add(NumberUtil.multiply(standardPrice, num), goodsPriceBeforeDiscount);// (standardPrice*num+goodsPriceBeforeDiscount);
            // 折后价
            if (goodsSettleFlag == 1)
            {
                goodsPrice = NumberUtil.add(
                        NumberUtil.multiply(NumberUtil.multiply(shopPrice, num), memberDiscount), goodsPrice);
                goodsSettlePrice = NumberUtil.multiply(goodsSettlePrice, memberDiscount);
                goodsRequiredPrice = NumberUtil.multiply(goodsRequiredPrice, memberDiscount);
            }
            else
            {
                goodsPrice = NumberUtil.add(NumberUtil.multiply(shopPrice, num), goodsPrice);
            }
            // 参与结算的价格(参与返点的商品才能参与结算)
            if (goodsRebateFlag == 1)
            {
                if (goodsSettleFlag == 1)
                {
                    orderRealSettlePrice = NumberUtil.add(
                            NumberUtil.multiply(NumberUtil.multiply(shopPrice, num), memberDiscount),
                            orderRealSettlePrice);
                }
                else
                {
                    orderRealSettlePrice = NumberUtil.add(NumberUtil.multiply(shopPrice, num), orderRealSettlePrice);
                }
            }
            Double orderGoodsDiscount = payOrderInfo.getOrderGoodsDiscount() == null ? 1.0 : payOrderInfo
                    .getOrderGoodsDiscount();

            orderGoodsDto = new OrderGoodsDto();
            orderGoodsDto.setShopId(shopId);
            orderGoodsDto.setOrderId(orderId);
            orderGoodsDto.setGoodsId(goodsId);
            orderGoodsDto.setGoodsNumber(num);
            orderGoodsDto.setStandardPrice(standardPrice);// 目录价
            orderGoodsDto.setGoodsSettlePrice(goodsSettlePrice);// 折后价
            orderGoodsDto.setGoodsRequiredPrice(goodsRequiredPrice);// 本应支付的金额
            orderGoodsDto.setGoodsIndex(i);
            orderGoodsDto.setUserRemark(payOrderInfo.getDishRemark());
            orderGoodsDto.setBillerId(payOrderInfo.getBillerId() == null ? payOrderDto.getBillerId() : payOrderInfo
                    .getBillerId());
            orderGoodsDto.setGoodsName(goodsName);
            orderGoodsDto.setSpecsDesc(specsDesc);
            orderGoodsDto.setOrderGoodsDiscount(orderGoodsDiscount);
            orderGoodsDto.setGoodsRebateFlag(goodsRebateFlag);// 返点标记
            orderGoodsDto.setGoodsSettleFlag(goodsSettleFlag);
            orderGoodsDto.setSettleUnitPrice(shopPrice);
            Integer isCancle = payOrderInfo.getIsCancle() == null ? 2 : payOrderInfo.getIsCancle();
            orderGoodsDto.setIsCancle(isCancle);
            orderGoodsDto.setIsOrderDiscount(isOrderDiscount);
            orderGoodsDto.setAvsSpecs(avsSpecs);
            orderGoodsDto.setGoodsType(goodsType);
            if (StringUtils.isNotBlank(goodsUnitName)) {
            	orderGoodsDto.setUnitName(goodsUnitName);
            }
            Integer isConsumeOuter = payOrderInfo.getIsConsumeOuter();
            isConsumeOuter = isConsumeOuter == null ? 0 : isConsumeOuter;
            orderGoodsDto.setIsConsumeOuter(isConsumeOuter);
            orderGoodsDtos.add(orderGoodsDto);
            
            //增加订单套餐内商品
            if (goodsType.equals(3000)) {
            	List<GoodsSetDto> goodsSetList = payOrderInfo.getSetGoodsList();
            	String setGoodsGroup = payOrderInfo.getSetGoodsGroup();
            	if (StringUtils.isNotBlank(setGoodsGroup)) {
            		orderGoodsDto.setSetGoodsGroup(setGoodsGroup);
            	}
            	
            	if (CollectionUtils.isEmpty(goodsSetList)) {
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"套餐内商品列表不能空。套餐goodsId:"+goodsId);
            	}
            	
        		for (GoodsSetDto goodsSet : goodsSetList) {
        	 		OrderGoodsDto orderGoodsSet = new OrderGoodsDto();
        	 		Double goodsSetNum = goodsSet.getGoodsNumber();
        	 		BigDecimal goodsSetPrice = goodsSet.getPrice();
        	 		if (goodsSet.getGoodsId() == null) {
    	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品id不能为空");
    	    		}
    	    		
    	    		if (goodsSetNum == null) {
    	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品数量不能为空");
    	    		}
    	    		
    	    		if (goodsSetPrice == null) {
    	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品原价不能为空");
    	    		}
    	    	
    	    		//int isExist = goodsDao.queryGoodsExists(goodsSet.getGoodsId());
    	    		GoodsDto g = goodsDao.getGoodsById(goodsSet.getGoodsId());
    	    		if (g == null) {
    	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品不存在  goodsId:"+goodsSet.getGoodsId());
    	    		}
        	 		
            		orderGoodsSet.setGoodsSetId(goodsId);
            		orderGoodsSet.setGoodsType(5000);//套餐内商品
            		orderGoodsSet.setGoodsId(goodsSet.getGoodsId());
            		orderGoodsSet.setShopId(shopId);
            		orderGoodsSet.setOrderId(orderId);
            		orderGoodsSet.setGoodsNumber(goodsSetNum);
            		orderGoodsSet.setStandardPrice(goodsSetPrice.doubleValue());
            		orderGoodsSet.setGoodsSettlePrice(NumberUtil.multiply(goodsSetPrice.doubleValue(), goodsSetNum));// 折后价
            		orderGoodsSet.setGoodsRequiredPrice(NumberUtil.multiply(goodsSetPrice.doubleValue(), goodsSetNum));// 本应支付的金额
            		orderGoodsSet.setSettleUnitPrice(goodsSetPrice.doubleValue());
            		orderGoodsSet.setGoodsIndex(goodsSet.getGoodsIndex());
            		orderGoodsSet.setBillerId(payOrderInfo.getBillerId() == null ? payOrderDto.getBillerId() : payOrderInfo
            									.getBillerId());
            		orderGoodsSet.setGoodsName(goodsSet.getGoodsName());
            		orderGoodsSet.setSetGoodsGroup(setGoodsGroup);
            		orderGoodsSet.setGoodsSettleFlag(g.getGoodsSettleFlag());
            		orderGoodsSet.setUnitName(goodsSet.getUnitName());
            		orderGoodsDtos.add(orderGoodsSet);
        		}
           
            }
            goodsNum = (num + goodsNum);
            if (i < 2)
            {
                orderTitle.append(goodsName + "、");
            }
            i++;
        }
       
        map.put("orderGoodsDtos", orderGoodsDtos);// 会员订单商品信息
        map.put("goodsPrice", goodsPrice);// 订单折后价格
        map.put("orderRealSettlePrice", orderRealSettlePrice);// 订单参与结算的价格
        map.put("goodsPriceBeforeDiscount", goodsPriceBeforeDiscount);// 订单折前价格
        map.put("orderGoodsNum", (int) goodsNum);// 订单商品数：订单商品总数向下取整，如：4.8，则取4
        if (orderTitle.length() > 1)
        {
            String title = orderTitle.toString().substring(0, orderTitle.toString().length() - 1);
            if (payOrderInfoDtos.size() > 2)
            {
                title += "...";
            }
            map.put("orderTitle", title);
        }
        else
        {
            map.put("orderTitle", orderTitle);// 订单商品数
        }
        logger.info("=======最终获取的订单商品等信息：" + map);
        return map;
    }

    /**
     * 将参数转为订单对象
     * @param multiPayDto
     * @param status
     * @param xorderDto
     * @param orderExists
     * @return
     * @throws Exception
     */
    private List<OrderGoodsDto> convertToXOrder(MultiPayDto multiPayDto, Integer status, OrderDto xorderDto,
            boolean xorderExists) throws Exception
    {
        logger.info("====开始将参数转换为非会员订单");
        MultiPayOrderDto payOrderDto = multiPayDto.getData();

        Long shopId = multiPayDto.getShopId();
        xorderDto.setOrderId(payOrderDto.getId());
        xorderDto.setOrderTime(payOrderDto.getCreateTime());
        xorderDto.setLastUpdateTime(new Date());
        xorderDto.setPayStatus(0);
        xorderDto.setPrepayMoney(0d);// 预付金额s
        xorderDto.setPayTimeType(0);// 支付时间的类型：网上支付-0，货到付款-1
        xorderDto.setOrderSceneType(payOrderDto.getOrderSceneType());// 订单场景分类：0（预定单），1（到店点菜订单），2（外卖订单），3（服务订单），4（商品订单）
        xorderDto.setOrderType(1); // 全额订单*
        xorderDto.setShopId(shopId);// 订单所属商铺
        xorderDto.setIsMaling(payOrderDto.getIsMaling());
        // 获取订单商品等信息
        Map<String, Object> map = getXorderGoodsList(multiPayDto, xorderExists);
        xorderDto.setLogisticsPrice(payOrderDto.getOutfee() == null ? 0 : payOrderDto.getOutfee());// 物流费用
        xorderDto.setGoodsPriceBeforeDiscount(Double.parseDouble(map.get("goodsPriceBeforeDiscount") + ""));// 折前商品总价
        xorderDto.setGoodsPrice(Double.parseDouble(map.get("goodsPrice") + ""));// 商品总价（折后)
        xorderDto.setOrderTotalPrice(xorderDto.getGoodsPrice() + xorderDto.getLogisticsPrice());// 订单总价(折后价+物流费用)
        xorderDto.setSettlePrice(payOrderDto.getSettlePrice());// 参与结算的价格
        xorderDto.setGoodsNumber(Integer.parseInt(map.get("orderGoodsNum") + ""));// 订单商品数量
        xorderDto.setOrderDiscount(payOrderDto.getAdditionalDiscount()); // 折上折
        xorderDto.setCashierId(payOrderDto.getCashierId());
        xorderDto.setOrderTitle(map.get("orderTitle") + "");
        xorderDto.setOrderStatus(status);
        xorderDto.setMemberDiscount(payOrderDto.getDiscount());
        xorderDto.setMobile(multiPayDto.getData().getMobile());
        xorderDto.setIsMember(CommonConst.USER_IS_NOT_MEMBER);
        xorderDto.setRemark(payOrderDto.getUserRemark());
        xorderDto.setClientSystemType(multiPayDto.getClientSystem());
        xorderDto.setClientFinishTime(DateUtils.parse(multiPayDto.getPayClientTime(), DateUtils.DATETIME_FORMAT));
        xorderDto.setCashierUsername(payOrderDto.getCashierUsername());
        xorderDto.setIsWait(payOrderDto.getIsWait());
        xorderDto.setSettleFlag(payOrderDto.getSettleFlag());
        xorderDto.setOrderDiscountPrice(payOrderDto.getOrderDiscountPrice());
        xorderDto.setShopMemberDiscount(payOrderDto.getShopMemberDiscount());

        String TokenId = payOrderDto.getTokenId();
        if(!StringUtils.isBlank(TokenId))
        {
            xorderDto.setTokenId(TokenId);
        }
        Integer consumeType = payOrderDto.getConsumeType();
        if(null != consumeType)
        {
            xorderDto.setConsumeType(consumeType);
        }
        if (payOrderDto.getDeductAmount() != null) {
        	 xorderDto.setCouponDiscountPrice(payOrderDto.getDeductAmount());
        }
       
        if (payOrderDto.getUserShopCouponIdList() != null) {
        	xorderDto.setUserShopCouponIdList(payOrderDto.getUserShopCouponIdList());
        }
        if(StringUtils.isNotBlank(payOrderDto.getSeatIds()) && !payOrderDto.getSeatIds().equals("-1") && !payOrderDto.getSeatIds().equals("0")) {
            if (!payOrderDto.getSeatIds().equals(xorderDto.getSeatIds())) {
                String[] seatIds = payOrderDto.getSeatIds().split(",");
                for (String seatIdStr : seatIds) {
                    orderServcie.checkShopSeatValid(Long.valueOf(seatIdStr), shopId);
                }
            }
            xorderDto.setSeatIds(payOrderDto.getSeatIds());
            if (null != status && status == CommonConst.ORDER_STS_YKD)
            {
                //防止改桌造成占用两个桌子，先对资源进行释放在进行预订
                orderServcie.updateShopResourceStatus(xorderDto.getOrderId());
                // 预定资源
                orderServcie.reservResource(payOrderDto.getSeatIds(), xorderDto.getOrderId());
            }
        }
        
        // orderDto.setOrderChannelType(2);//下单方式
        List<OrderGoodsDto> xorderGoodsDtos = (List<OrderGoodsDto>) map.get("xorderGoodsDtos");
        logger.info("=======非会员订单信息：" + xorderDto);
        logger.info("=======非会员订单商品信息：" + xorderGoodsDtos);
        return xorderGoodsDtos;
    }

    /**
     * @title: getGoodsTechList
     * @description: 获取订单商品技师服务列表
     * @param @param multiPayDto
     * @param @return 设定文件
     * @return List<OrderGoodsServiceTech> 返回类型
     * @throws
     */
    public List<OrderGoodsServiceTech> getGoodsTechList(MultiPayDto multiPayDto)
    {
        MultiPayOrderDto payOrderDto = multiPayDto.getData();
        List<MultiPayOrderInfoDto> payOrderInfoDtos = payOrderDto.getOrderInfo();
        // 商品技师服务列表
        List<OrderGoodsServiceTech> goodsTechList = new ArrayList<OrderGoodsServiceTech>();
        for (MultiPayOrderInfoDto payOrderInfo : payOrderInfoDtos)
        {
            /** 批量添加订单商品技师列表* update by dengjh 20160229 */
            if (null != payOrderInfo.getTechs())
            {
                for (Map<String, Object> map : payOrderInfo.getTechs())
                {
                    if (map.get("techId") != null)
                    {
                        OrderGoodsServiceTech ogs = new OrderGoodsServiceTech();
                        ogs.setOrderGoodsId(payOrderInfo.getDishId().intValue());// goodsId
                        ogs.setTechID(Integer.parseInt(map.get("techId").toString()));
                        goodsTechList.add(ogs);
                    }
                }
            }
        }
        return goodsTechList;
    }

    /**
     * 获取非会员订单商品信息
     * @param multiPayDto
     * @param orderExists 订单是否存在
     * @return
     * @throws Exception
     */
    private Map<String, Object> getXorderGoodsList(MultiPayDto multiPayDto, boolean orderExists) throws Exception
    {
        logger.info("====开始获取订单商品信息");
        MultiPayOrderDto payOrderDto = multiPayDto.getData();
        List<MultiPayOrderInfoDto> payOrderInfoDtos = payOrderDto.getOrderInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        MultiPayOrderDto data = multiPayDto.getData();
        Integer settleFlag = data.getSettleFlag()==null ? 0 : data.getSettleFlag();
        if (CollectionUtils.isEmpty(payOrderInfoDtos))
        {

            map.put("xorderGoodsDtos", null);// 非会员订单商品信息
            map.put("goodsPrice", multiPayDto.getData().getSettlePrice());// 订单折后价格 TODO
            map.put("goodsPriceBeforeDiscount", multiPayDto.getData().getOrderPrice() == null ?
            		multiPayDto.getData().getSettlePrice() : data.getOrderPrice());// 订单折前价格
            map.put("orderGoodsNum", 0);// 订单商品数：订单商品总数向下取整，如：4.8，则取4
            map.put("orderTitle", "管家收银");// 订单商品数
            return map;
        }
        Long shopId = multiPayDto.getShopId();
        String xorderId = payOrderDto.getId();
        Double goodsPrice = 0d;// 折后价
        Double goodsPriceBeforeDiscount = 0d;// 折前价
        StringBuilder orderTitle = new StringBuilder("");// 订单标题
        double goodsNum = 0;
        List<OrderGoodsDto> orderGoodsDtos = new ArrayList<OrderGoodsDto>();
        OrderGoodsDto orderGoodsDto = null;
        int i = 0;
        for (MultiPayOrderInfoDto payOrderInfo : payOrderInfoDtos)
        {
            Long goodsId = payOrderInfo.getDishId();
            String unitName = null;
            // 商品名称
            String goodsName = null;
            String specsDesc = null;
            boolean queryFlag = false;
            Integer goodsSettleFlag = 0;
            Integer isOrderDiscount = 1;
            Integer goodsType = 1000;
            GoodsDto goods = goodsServcie.getGoodsByIds(shopId, goodsId);
            if (goodsId != CommonConst.CUSTOM_GOODS_FLAG  && goods == null)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺商品数据不存在");
            }
            
            // 如果不是首次支付，则重订单商品表中先查询
            if (!orderExists)
            {
                OrderGoodsDto params = new OrderGoodsDto();
                params.setGoodsId(goodsId);
                params.setOrderId(xorderId);
                params.setShopId(shopId);
                OrderGoodsDto dto = orderServcie.getOrderGoodsByGoodsId(params);
                if (dto != null)
                {
                    goodsName = dto.getGoodsName();
                    specsDesc = dto.getSpecsDesc();
                    goodsSettleFlag = dto.getGoodsSettleFlag();
                    isOrderDiscount = dto.getIsOrderDiscount();
                    goodsType = dto.getGoodsType();
                /*    if(dto.getIsConsumeOuter() != null && payOrderInfo.getIsConsumeOuter() == null)
                    {
                        payOrderInfo.setIsConsumeOuter(dto.getIsConsumeOuter());
                    }*/
                    queryFlag = true;
                }
            }
            if (!queryFlag)
            {
                if(goodsId == CommonConst.CUSTOM_GOODS_FLAG) {
                    goodsName = "自定义商品";
                    specsDesc = payOrderInfo.getSpecsDesc();
                } else {
                    unitName = goods.getUnitName();
                    goodsName = goods.getGoodsName();
                    specsDesc = goods.getSpecsDesc();
                    goodsSettleFlag = goods.getGoodsSettleFlag();
                    isOrderDiscount = goods.getIsOrderDiscount();
                    goodsType = goods.getGoodsType();
                }
                
            }
            /*
             * 订单商品： goods_required_price：会员折扣*原价*数量 非会员就没有会员折扣
             * goods_settle_price：会员折扣*改价后的价格*数量 非会员就没有会员折扣
             * order_goods_discount：菜品自定折扣（）
             * 
             * 订单： order_real_settle_price 真正参与结算的价格（服务端分成用） 商品返点*数量
             * settle_price 实收款 order_total_price 物流费+goods_price
             * goods_price_before_discount 目录价*数量 goods_price 目录价*数量*会员折扣 不一定都乘以
             * 菜不打折不乘
             */
            // 商品改价后的价格
            Double shopPrice = payOrderInfo.getPlatformPrice();
            // 商品目录价
            Double standardPrice = payOrderInfo.getStandardPrice();
            //商品增值服务信息
            List<GoodsAvsDto> goodsAvsList = payOrderInfo.getAvsSpecsList();
            String avsSpecs = null;
            if (CollectionUtils.isNotEmpty(goodsAvsList)) {
            	List<Map<String, Object>> goodsAvsMapList = new LinkedList<Map<String,Object>>();
            	Double goodsAvsTotalPrice = Double.valueOf(0);
            	for (GoodsAvsDto goodsAvs : goodsAvsList) {
            		Long goodsAvsId = goodsAvs.getGoodsAvsId();
            		String avsName = goodsAvs.getAvsName();
            		Double avsPrice = goodsAvs.getAvsPrice();
            		if (goodsAvsId == null) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务Id不能为空");
            		}
            		
            		if (StringUtils.isBlank(avsName)) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务名称不能为空");
            		}
            		
            		if (avsPrice == null) {
            			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "下单商品含有增值服务时 增值服务价格不能为空");
            		}
            		goodsAvsTotalPrice = NumberUtil.add(goodsAvsTotalPrice, avsPrice);
            		Map<String, Object> goodsAvsMap = new LinkedHashMap<String, Object>();
            		goodsAvsMap.put("goodsAvsId", goodsAvsId);
            		goodsAvsMap.put("avsName", avsName);
            		goodsAvsMap.put("avsPrice", avsPrice);
            		goodsAvsMapList.add(goodsAvsMap);
            	}
            	
       /*     	if (!NumberUtil.add(goodsAvsTotalPrice,standardPrice).equals(shopPrice)) {
            		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
            									"下单商品含有增值服务时改价后shopPrice校验错误   商品增值服务总价格："+goodsAvsTotalPrice+
            									"商品目录价："+standardPrice+"改价后的价格："+shopPrice);
            	}*/
            	
            	avsSpecs = JSON.toJSONString(goodsAvsMapList);
            }
            
           
            // 订单商品数量
            Double num = payOrderInfo.getNum() == null ? 1 : payOrderInfo.getNum();
            // 计算订单折后价：折后价=商品改价后的价格*商品数量*会员折扣
            Double goodsSettlePrice = NumberUtil.multiply(shopPrice, num);
            // 商品本应支付的金额：会员折扣*原价*数量
            Double goodsRequiredPrice = NumberUtil.multiply(standardPrice, num);

            //计算折钱折后价
            //不是反结账2
            if(2!=settleFlag.intValue()){
                // 计算订单折前价
                goodsPriceBeforeDiscount = NumberUtil
                        .add(NumberUtil.multiply(standardPrice, num), goodsPriceBeforeDiscount);
                // 折后价
                goodsPrice = NumberUtil.add(NumberUtil.multiply(shopPrice, num), goodsPrice);
            }
            else{
            	//TODO 计算反结账价格
                // isCancle	int	2	否	0-正常退菜，1-反结账退菜，2-正常加菜，3-反结账加菜
            	Integer iscancle = payOrderInfo.getIsCancle()==null ? 2 : payOrderInfo.getIsCancle();
            	if(iscancle==0 || iscancle==1){
                    // 计算订单折前价
                    goodsPriceBeforeDiscount = NumberUtil
                            .sub(NumberUtil.multiply(standardPrice, num), goodsPriceBeforeDiscount);
                    // 折后价
                    goodsPrice = NumberUtil.sub(NumberUtil.multiply(shopPrice, num), goodsPrice);
            	}
            	if(iscancle==2&&iscancle==3){
                    // 计算订单折前价
                    goodsPriceBeforeDiscount = NumberUtil
                            .add(NumberUtil.multiply(standardPrice, num), goodsPriceBeforeDiscount);
                    // 折后价
                    goodsPrice = NumberUtil.add(NumberUtil.multiply(shopPrice, num), goodsPrice);
            	}
            }


            // 参与结算的价格(参与返点的商品才能参与结算)
            Double orderGoodsDiscount = payOrderInfo.getOrderGoodsDiscount() == null ? 1.0 : payOrderInfo
                    .getOrderGoodsDiscount();
            orderGoodsDto = new OrderGoodsDto();
            orderGoodsDto.setOrderId(xorderId);
            orderGoodsDto.setShopId(shopId);
            orderGoodsDto.setGoodsId(goodsId);
            orderGoodsDto.setGoodsNumber(num);
            orderGoodsDto.setStandardPrice(standardPrice);
            orderGoodsDto.setGoodsSettlePrice(goodsSettlePrice);
            orderGoodsDto.setGoodsRequiredPrice(goodsRequiredPrice);
            orderGoodsDto.setGoodsIndex(i);
            orderGoodsDto.setUserRemark(payOrderInfo.getDishRemark());
            orderGoodsDto.setBillerId(payOrderInfo.getBillerId() == null ? payOrderDto.getBillerId() : payOrderInfo
                    .getBillerId());
            orderGoodsDto.setGoodsName(goodsName);
            orderGoodsDto.setSpecsDesc(specsDesc);
            orderGoodsDto.setOrderGoodsDiscount(orderGoodsDiscount);
            orderGoodsDto.setSettleUnitPrice(shopPrice);
            Integer isCancle = payOrderInfo.getIsCancle() == null ? 2 : payOrderInfo.getIsCancle();
            orderGoodsDto.setIsCancle(isCancle);
            orderGoodsDto.setIsOrderDiscount(isOrderDiscount);
            orderGoodsDto.setGoodsSettleFlag(goodsSettleFlag);
            orderGoodsDto.setAvsSpecs(avsSpecs);
            orderGoodsDto.setGoodsType(goodsType);
            Integer isConsumeOuter = payOrderInfo.getIsConsumeOuter();
            isConsumeOuter = null == isConsumeOuter ? 0 : isConsumeOuter;
            orderGoodsDto.setIsConsumeOuter(isConsumeOuter);
            if (unitName != null) {
            	orderGoodsDto.setUnitName(unitName);
            }
            orderGoodsDtos.add(orderGoodsDto);
            
            //增加订单套餐内商品
            if(goodsType!=null){
                if (goodsType.equals(3000)) {
                	List<GoodsSetDto> goodsSetList = payOrderInfo.getSetGoodsList();
                	String setGoodsGroup = payOrderInfo.getSetGoodsGroup();
                	if (StringUtils.isNotBlank(setGoodsGroup)) {
                		orderGoodsDto.setSetGoodsGroup(setGoodsGroup);
                	}
                	
                	if (CollectionUtils.isEmpty(goodsSetList)) {
                		throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"套餐内商品列表不能空。套餐goodsId:"+goodsId);
                	}
                	
            		for (GoodsSetDto goodsSet : goodsSetList) {
            	 		OrderGoodsDto orderGoodsSet = new OrderGoodsDto();
            	 		Double goodsSetNum = goodsSet.getGoodsNumber();
            	 		BigDecimal goodsSetPrice = goodsSet.getPrice();
            	 		if (goodsSet.getGoodsId() == null) {
        	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品id不能为空");
        	    		}
        	    		
        	    		if (goodsSetNum == null) {
        	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品数量不能为空");
        	    		}
        	    		
        	    		if (goodsSetPrice == null) {
        	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品原价不能为空");
        	    		}
        	    	
        	    		//int isExist = goodsDao.queryGoodsExists(goodsSet.getGoodsId());
        	    		GoodsDto g = goodsDao.getGoodsById(goodsSet.getGoodsId());
        	    		if (g == null) {
        	    			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"套餐内商品不存在  goodsId:"+goodsSet.getGoodsId());
        	    		}
            	 		
                		orderGoodsSet.setGoodsSetId(goodsId);
                		orderGoodsSet.setGoodsType(5000);//套餐内商品
                		orderGoodsSet.setGoodsId(goodsSet.getGoodsId());
                		orderGoodsSet.setShopId(shopId);
                		orderGoodsSet.setOrderId(xorderId);
                		orderGoodsSet.setGoodsNumber(goodsSetNum);
                		orderGoodsSet.setStandardPrice(goodsSetPrice.doubleValue());
                		orderGoodsSet.setGoodsSettlePrice(NumberUtil.multiply(goodsSetPrice.doubleValue(), goodsSetNum));// 折后价
                		orderGoodsSet.setGoodsRequiredPrice(orderGoodsSet.getStandardPrice());// 本应支付的金额
                		orderGoodsSet.setGoodsIndex(goodsSet.getGoodsIndex());
                		orderGoodsSet.setBillerId(payOrderInfo.getBillerId() == null ? payOrderDto.getBillerId() : payOrderInfo
                									.getBillerId());
                		orderGoodsSet.setGoodsName(goodsSet.getGoodsName());
                		orderGoodsSet.setSetGoodsGroup(setGoodsGroup);
                		orderGoodsSet.setUnitName(unitName);
                		orderGoodsSet.setGoodsSettleFlag(g.getGoodsSettleFlag());
                		orderGoodsDtos.add(orderGoodsSet);
            		}
                }
            }

            
            goodsNum = (num + goodsNum);
            if (i < 2)
            {
                orderTitle.append(goodsName + "、");
            }
            i++;
        }
        map.put("xorderGoodsDtos", orderGoodsDtos);// 非会员订单商品信息
        map.put("goodsPrice", goodsPrice);// 订单折后价格
        map.put("goodsPriceBeforeDiscount", goodsPriceBeforeDiscount);// 订单折前价格
        map.put("orderGoodsNum", (int) goodsNum);// 订单商品数：订单商品总数向下取整，如：4.8，则取4
        String title = orderTitle.toString().substring(0, orderTitle.toString().length() - 1);
        if (payOrderInfoDtos.size() > 2)
        {
            title += "...";
        }
        map.put("orderTitle", title);// 订单商品数
        logger.info("=======最终获取的订单商品等信息：" + map);
        return map;
    }

    private SmsReplaceContent buildSmsContent(Map map, String mobile)
    {
        BigDecimal init = new BigDecimal(0);
        SmsReplaceContent src = new SmsReplaceContent();
        BigDecimal onLinePayment = (BigDecimal) map.get("onLinePayment");
        BigDecimal cashCouponPayment = (BigDecimal) map.get("cashCouponPayment");
        BigDecimal usedRedPacketMoney = (BigDecimal) map.get("usedRedPacketMoney");
        BigDecimal smsPayAmount = (BigDecimal) map.get("smsPayAmount");
        if (onLinePayment == null)
        {
            onLinePayment = init;
        }
        if (cashCouponPayment == null)
        {
            cashCouponPayment = init;
        }
        if (usedRedPacketMoney == null)
        {
            usedRedPacketMoney = init;
        }
        if (smsPayAmount == null)
        {
            smsPayAmount = init;
        }
        src.setOnLinePayment(NumberUtil.formatDouble(onLinePayment.doubleValue(), 2));
        src.setCashCouponPayment(NumberUtil.formatDouble(cashCouponPayment.doubleValue(), 2));
        src.setConsumeDate(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
        src.setUsedRedPacketMoney(NumberUtil.formatDouble(usedRedPacketMoney.doubleValue(), 2));
        StringBuffer usage = new StringBuffer();
        usage.append("sms_pay");
        if (onLinePayment.compareTo(init) > 0)
        {
            usage.append("_cqb");//传奇宝
        }
        if (cashCouponPayment.compareTo(init) > 0)
        {
            usage.append("_xfk");//消费卡
        }
        if (usedRedPacketMoney.compareTo(init) > 0)
        {
            usage.append("_hb");//红包
        }
        src.setUsage(usage.toString());
        src.setAmount(Double.parseDouble(NumberUtil.formatVal(smsPayAmount + "", 2) + "")); // 消费金额
        src.setMobile(mobile);
        return src;
    }

    /**
     * 商品状态变更接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/shop/updateGoodsInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateGoodsInfo(HttpServletRequest request)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            logger.info("商品状态变更接口    -start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            String goodsIdStrs = RequestUtils.getQueryParam(request, "goodsIds");
            String operateTypeStr = RequestUtils.getQueryParam(request, "operateType");
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOPID);
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            CommonValidUtil.validStrNull(goodsIdStrs, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_GOODS_ID);
            List<Long> goodsIds = new ArrayList<Long>();
            String[] strs = goodsIdStrs.split(CommonConst.COMMA_SEPARATOR);
            for (int i = 0; i < strs.length; i++)
            {
                Long goodsId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_GOODS_ID);
                goodsIds.add(goodsId);
            }
            Integer operateType = null;
            if (StringUtils.isEmpty(operateTypeStr))
            {
                // 默认操作：上架
                operateType = 1;
            }
            else
            {
                operateType = CommonValidUtil.validStrIntFmt(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "操作类型格式错误");
            }
            if (operateType == 1 || operateType == 2 || operateType == 0)
            {
                int re = collectService.updateGoodsInfo(shopId, goodsIds, operateType);
                Map<String, Object> result = null;
                if (re > 0)
                {
                    result = new HashMap<String, Object>();
                    result.put("goodsIds", goodsIdStrs);
                    result.put("operateTypeStr", operateTypeStr);
                }
                // 推送消息
                GoodsMessageUtil.pushGoodsMessage(goodsIds, shopId, operateType);
                return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "商品状态更新成功", result);
            }
            else
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "未知的操作类型");
            }
        }
        catch (ServiceException e)
        {
            logger.error("商品状态变更接口    -异常", e);
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("商品状态变更接口    -系统异常", e);
            throw new APISystemException("商品状态变更接口    -系统异常", e);
        }
        finally
        {
            logger.info("共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        }
    }
    	
    
    /**
     * 获取店铺总条数
     * 
     * @Function: com.idcq.appserver.controller.collect.CollectController.getShopGoodsCount
     * @Description:
     *
     * @param request
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年10月25日 上午9:42:46
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年10月25日    ChenYongxin      v1.0.0         create
     */
    @RequestMapping(value={"/shop/getShopGoodsCount", "/service/shop/getShopGoodsCount"}, produces = "application/json;charset=utf-8")
    public  @ResponseBody Object getShopGoodsCount(HttpServletRequest request){
    	logger.info("获取商铺 商品总条数-start");
    	try{
	    	String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
	    	CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
	    	int re = collectService.getShopGoodsCount(Long.parseLong(shopIdStr));
	    	Map<String,Object>resultMap=new HashMap<String,Object>();
	    	resultMap.put("totalCount",re);
	    	return resultMap;
    	 }
        catch (Exception e)
        {
            logger.error("商品状态变更接口    -异常", e);
            throw new APIBusinessException(e);
        }
    }
    
    /**
     * CS22：获取店铺商品信息接口
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = {"/shop/getShopGoods", "/service/shop/getShopGoods"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getShopGoods(HttpServletRequest request) throws Exception
 {
		logger.info("获取店铺商品信息接口-start");
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String token = RequestUtils.getQueryParam(request, "token");

		String status = RequestUtils.getQueryParam(request, "status"); // 商品状态:下架-0,上架-1,删除-2,草稿-3

		String pNoStr = RequestUtils.getQueryParam(request, "pNo");
		String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
		String isNeedCheckStr = RequestUtils.getQueryParam(request,
				"isNeedCheck");
		String queryData = RequestUtils.getQueryParam(request, "queryData");
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		String lastUpdateTime = RequestUtils.getQueryParam(request,
				"lastUpdateTime");
		String goodsGroupFlag = RequestUtils.getQueryParam(request,
				"goodsGroupFlag");// 1 只查有goodsGroupId的 2.只查没有goodsGroupId的

		String sellModeStr = RequestUtils.getQueryParam(request, "sellMode");
		Integer sellMode = StringUtils.isBlank(sellModeStr) ? 102 : Integer
				.valueOf(sellModeStr.trim());

		// token校验
		if (CommonConst.GETSHOPGOODS_URL.equals(request.getRequestURI())) {
			collectService.queryShopAndTokenExists(shopId, token);
		}
		// isNeedCheck
		Integer isNeedCheck = null;
		if (StringUtils.isNotBlank(isNeedCheckStr)) {
			isNeedCheck = NumberUtil.strToNum(isNeedCheckStr, "isNeedCheck");
		}
		String searchKey = RequestUtils.getQueryParam(request, "searchKey");
		String storageAlarmType = RequestUtils.getQueryParam(request,
				"storageAlarmType");
		String goodsCategoryId = RequestUtils.getQueryParam(request,
				"goodsCategoryId");
		// 分页默认10条，第1页
		Integer pNo = PageModel.handPageNo(pNoStr);
		Integer pSize = PageModel.handPageSize(pSizeStr);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("status", status);
		map.put("n", (pNo - 1) * pSize);
		map.put("m", pSize);
		map.put("isNeedCheck", isNeedCheck);
		map.put("queryData", queryData);
		map.put("searchKey", searchKey);
		map.put("storageAlarmType", storageAlarmType);
		map.put("goodsCategoryId", goodsCategoryId);
		map.put("lastUpdateTime", lastUpdateTime);
		map.put("goodsGroupFlag", goodsGroupFlag);

		map.put("sellMode", sellMode);

		PageModel pageModel = collectService.getShopGoods(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("lst", pageModel.getList());
		if (!request.getRequestURI().contains("/service/shop/getShopGoods")) {
			// 代表收银机请求，第一次全量加载商品
			resultMap.put("lastUpdateTime",
					DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		}
		resultMap.put("rCount", pageModel.getTotalItem());
		resultMap.put("pNo", pNo);

		return ResultUtil.getResultJson(0, "调用成功", resultMap,
				DateUtils.DATETIME_FORMAT);
	}

    /**
     * 校验token
     * @param shopIdStr
     * @param token
     * @return
     * @throws Exception
     */
    private Long checkToken(String shopIdStr, String token) throws Exception
    {
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        collectService.queryShopAndTokenExists(shopId, token);
        return shopId;
    }

}
