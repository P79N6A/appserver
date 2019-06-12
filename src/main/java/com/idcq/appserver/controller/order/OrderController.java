package com.idcq.appserver.controller.order;

import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idcq.idianmgr.service.shop.IMgrShopService;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;

import com.idcq.appserver.aliscanpay.f2fpay.ToAlipayQrTradePay;
import com.idcq.appserver.aliscanpay.util.AlipayNotify;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.ClientSystemTypeEnum;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.IXorderPayDao;
import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;
import com.idcq.appserver.dto.cashregister.DataDto;
import com.idcq.appserver.dto.cashregister.OnLinePayDto;
import com.idcq.appserver.dto.cashregister.OrderInfoDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.BookInfo;
import com.idcq.appserver.dto.order.DataJsonDto;
import com.idcq.appserver.dto.order.Goods;
import com.idcq.appserver.dto.order.OrderDetailDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.OrderShopResourceGoodDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.dto.order.POSOrderDataDto;
import com.idcq.appserver.dto.order.POSOrderDto;
import com.idcq.appserver.dto.order.POSOrderGoodsDto;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.message.IPushShopMsgService;
import com.idcq.appserver.service.message.IPushUserMsgService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.order.IOrderShopRsrcService;
import com.idcq.appserver.service.order.IXorderService;
import com.idcq.appserver.service.order.OrderServiceImpl;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IDistribTakeoutSettingService;
import com.idcq.appserver.service.shop.IShopRsrcServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.service.user.IPushUserTableService;
import com.idcq.appserver.service.user.IUserAccountService;
import com.idcq.appserver.service.user.IUserAddressService;
import com.idcq.appserver.utils.ArrayUtil;
import com.idcq.appserver.utils.AsyncExecutorUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.PlaceOrderPushMessageTask;
import com.idcq.appserver.utils.RandomUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.utils.thread.EnableShopResourceTask;
import com.idcq.appserver.utils.thread.OrderLogTask;
import com.idcq.appserver.utils.thread.OrderPushUserTask;
import com.idcq.appserver.utils.thread.ThreadPoolManager;
import com.idcq.appserver.wxscan.WXScanUtil;
import com.idcq.appserver.wxscan.WxPayResult;

/**
 * 我的订单controller
 *
 * @author Administrator
 * @date 2015年3月8日
 * @time 下午5:20:07
 */
@Controller public class OrderController extends BaseController
{

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired private ICollectDao collectDao;

    @Autowired public IOrderServcie orderServcie;

    @Autowired private IMemberServcie memberServcie;

    @Autowired private IGoodsServcie goodsServcie;

    @Autowired private ICommonService commonService;

    @Autowired private ISendSmsService sendSmsService;

    @Autowired public IPushUserTableService pushUserTableService;

    @Autowired public IPushShopMsgService pushShopMsgService;

    @Autowired public IPushUserMsgService pushUserMsgService;

    @Autowired public IShopServcie shopServcie;

    @Autowired public IPushService pushService;

    @Autowired public IPacketService packetService;

    //    @Autowired
    //    public IUserService userService;

    @Autowired public IUserAddressService userAddressService;

    @Autowired public IXorderService xorderService;

    @Autowired private ICollectService collectService;

    @Autowired private IDistribTakeoutSettingService distribTakeoutSettingService;

    @Autowired private IShopTechnicianService shopTechnicianService;

    @Autowired private IShopRsrcServcie shopRsrcServcie;

    @Autowired private IOrderShopRsrcService osrService;

    @Autowired private IPayServcie payServcie;

    @Autowired private IUserAccountService userAccountService;

    @Autowired private IUserCashCouponService userCashCouponService;

    @Autowired public IPushUserTableDao pushUserTableDao;

    @Autowired private IPayDao payDao;

    @Autowired private IBusAreaActivityService busAreaActivityService;

    @Autowired private IXorderPayDao xorderPayDao;

    @Autowired private ILevelService levelService;

    @Autowired private IMgrShopService mgrShopService;

    @RequestMapping(value = { "/syncOrderList", "/session/order/syncOrderList", "/token/order/syncOrderList",
            "/service/order/syncOrderList" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = "application/json") 
    @ResponseBody 
    public ResultDto syncOrderList(HttpEntity<String> entity, HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            logger.info("同步收银机交易记录-start" + entity.toString());
            POSOrderDto posOrder = (POSOrderDto) JacksonUtil
                    .postJsonToObj(entity, POSOrderDto.class, DateUtils.DATETIME_FORMAT);
            logger.info("posOrder实体：" + posOrder);
            Long shopId = posOrder.getShopId();
            // 验证商铺ID
            CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            String requestPath = request.getRequestURI();
            if (CommonConst.SYNCORDERLIST_URL.equals(requestPath))
            {
                // token不能为空
                String token = posOrder.getToken();
                CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
                // 商铺存在性
                collectService.queryShopAndTokenExists(shopId, token);
            }
            POSOrderDataDto data = posOrder.getData();
            String orderId = data.getId();
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
            CommonValidUtil.validObjectNull(data, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_POS_DATA);
            Integer scanPay = posOrder.getScanPay();
            Long billerId = data.getBillerId();
            Long cashierId = data.getCashierId();
            CommonValidUtil.validObjectNull(billerId, CodeConst.CODE_PARAMETER_NOT_NULL, "billerId不能为空");
            // 订单状态必填及枚举
            Integer orderStatus = data.getOrderStatus();
            CommonValidUtil
                    .validIntNull(orderStatus, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDER_STATUS);
            // 订单ID不能为空

            if (scanPay != null)
            {
                Double totalAmount = data.getCurrenPayAmount();
                CommonValidUtil
                        .validObjectNull(totalAmount, CodeConst.CODE_PARAMETER_NOT_NULL, "扫码支付currenPayAmount不允许为空");
                if (scanPay == 8)
                {// 微信扫码支付
                    orderId = data.getId() + RandomUtil.getRandomStrByNum(3);
                    // 微信请求二维码
                    Map<String, String> scanResult = WXScanUtil.scanRequest("", totalAmount + "", orderId);
                    if (!StringUtils.isEmpty(scanResult.get("qrcode")))
                    {
                        result.put("qrcode", scanResult.get("qrcode"));
                        dealScanRequest(posOrder, data);
                    }
                }
                else
                {// 支付宝扫码支付
                    String resultJson = ToAlipayQrTradePay.qrPay(orderId, totalAmount + "", "收银机订单支付");// 支付宝预下单返回的结果
                    JSONObject qrResult = JSONObject.fromObject(resultJson);
                    if ("0".equals(qrResult.get("code")))
                    {
                        result.put("orderNo", orderId);
                        result.put("qrcode", qrResult.get("qrcode") + "");
                        dealScanRequest(posOrder, data);
                    }
                }
            }
            else
            {
                // 订单详情不能为空
                // 会员存在性
                String mobile = data.getMobile();
                UserDto user = this.memberServcie.getUserByMobile(mobile);
                int flag = 0;
                /*
                 * if (user != null) {// 会员不存在 status =
                 * this.orderServcie.getOrderStatusById(orderId); } else {//
                 * 非会员订单 status =
                 * this.xorderService.getOrderStatusById(orderId); }
                 */
                //                Integer status = this.orderServcie.getOrderStatusById(orderId);
                OrderDto order = orderServcie.getOrderDtoById(orderId);
                int addOrEditFlag = CommonConst.HANDLE_FLAG_EDIT;
                int addOrderLogFlag = CommonConst.HANDLE_FLAG_ADD;
                if (order == null)
                {
                    logger.info("orderServcie.getOrderStatusById(orderId)查询为null，orderid=" + orderId);
                    // 订单不存在执行新增操作
                    addOrEditFlag = CommonConst.HANDLE_FLAG_ADD;
                }
                else
                {
                    Integer status = order.getOrderStatus();
                    if (status.equals(orderStatus))
                    {
                        addOrderLogFlag = CommonConst.HANDLE_FLAG_EDIT;
                    }
                    // 订单已结账不能修改
                    CommonValidUtil.validIntNoEqual(status, CommonConst.ORDER_STS_YJZ, CodeConst.CODE_INVALID,
                            CodeConst.MSG_NOT_MODIFY_SETTLE);
                }
                posOrder.setAddOrEditFlag(addOrEditFlag);
                posOrder.setAddOrderLogFlag(addOrderLogFlag);
                posOrder.setUser(user);
                if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
                {// 新增操作校验
                    // 订单场景必填及枚举
                    CommonValidUtil.validIntNull(data.getOrderSceneType(), CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_REQUIRED_ORDER_SCENE);
                    // 支付状态必填及枚举
                    Integer payStatus = data.getPayStatus();
                    if (payStatus == null)
                    {
                        data.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
                    }
                    // 订单支付状态对应订单类型
                    if (data.getPayStatus() == CommonConst.PAY_STATUS_PAYED)
                    {
                        // 已支付订单为全额支付订单
                        data.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
                    }
                    else
                    {
                        // 未支付订单为预支付订单
                        data.setOrderType(CommonConst.ORDER_TYPE_REPAY);
                    }
                    // 商品列表不能为空
                    List<POSOrderGoodsDto> POSGoodsList = data.getOrderInfo();
                    CommonValidUtil.validListNull(POSGoodsList, CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_REQUIRED_OGOODS);
                    // 下单员工
                    if (CommonValidUtil.validPositLong(billerId))
                    {
                        flag = this.shopServcie.queryShopEmplExists(shopId, billerId);
                        CommonValidUtil
                                .validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BILLER);
                    }
                    // 收银员
                    if (CommonValidUtil.validPositLong(cashierId))
                    {
                        flag = this.shopServcie.queryShopEmplExists(shopId, cashierId);
                        CommonValidUtil
                                .validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHIER);
                    }
                    // 菜单总价
                    // CommonValidUtil.validDoubleNullZero(data.getTotal(),CodeConst.CODE_PARAMETER_NOT_NULL,
                    // CodeConst.MSG_REQUIRED_TOTALPRICE);
                    if (StringUtils.isNotBlank(data.getSeatIds()))
                    {
                        String[] seatIds = data.getSeatIds().split(",");
                        for (String seatIdStr : seatIds)
                        {
                            orderServcie.checkShopSeatValid(Long.valueOf(seatIdStr), shopId);
                        }
                    }
                }
                else
                {// 修改操作校验
                    // 订单场景非必填及枚举
                    // 订单状态非必填及枚举
                    // 支付状态非必填及枚举
                    // 下单员工
                    if (CommonValidUtil.validPositLong(billerId))
                    {
                        flag = this.shopServcie.queryShopEmplExists(shopId, billerId);
                        CommonValidUtil
                                .validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BILLER);
                    }
                    // 收银员
                    if (CommonValidUtil.validPositLong(cashierId))
                    {
                        flag = this.shopServcie.queryShopEmplExists(shopId, cashierId);
                        CommonValidUtil
                                .validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHIER);
                    }
                    if (StringUtils.isNotBlank(data.getSeatIds()) && !data.getSeatIds().equals(order.getSeatIds()))
                    {
                        String[] seatIds = data.getSeatIds().split(",");
                        for (String seatIdStr : seatIds)
                        {
                            orderServcie.checkShopSeatValid(Long.valueOf(seatIdStr), shopId);
                        }
                    }
                }
                if (orderStatus == CommonConst.ORDER_STS_YKD)
                {
                    // 已开单，支付状态改为未支付
                    data.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
                }
                else if (orderStatus == CommonConst.ORDER_STS_YJZ)
                { // 结算状态
                    // 已结账订单，支付状态必须为已支付
                    CommonValidUtil
                            .validIntEqual(data.getPayStatus(), CommonConst.PAY_STATUS_PAYED, CodeConst.CODE_INVALID,
                                    CodeConst.MSG_PAYSTATUS_BE_PAYED);
                    // 会员订单，支付时间必填
                    if (user != null)
                    {
                        Date payTime = data.getPayTime();
                        CommonValidUtil.validObjectNull(payTime, CodeConst.CODE_PARAMETER_NOT_NULL,
                                CodeConst.MSG_REQUIRED_PAYTIME);
                    }
                    posOrder.setSettleFlag(0);
                }
                if (data.getCouponDiscountPrice() == null && data.getUserShopCouponIdList() != null)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时deductAmount必填");
                }

                if (data.getCouponDiscountPrice() != null && data.getUserShopCouponIdList() == null)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时userShopCouponIdList必填");
                }
                // 同步交易信息、支付信息
                Map<String, Object> map2 = this.orderServcie.syncOrderList(posOrder);
                // 销量统计
                orderServcie.updateGoodsAndShopSoldNum(orderId);
                //插入反结账订单商品线上支付账单
                payServcie.insertReverseShopBill(order);
                result.put("sendRedPacketMoney", map2.get("sendRedPacketMoney"));
                // 支付信息推送
                StringBuilder content = new StringBuilder();
                if (user != null && user.getIsMember() == CommonConst.USER_IS_MEMBER)
                {
                    // 会员订单进行结算orderId
                    if (orderStatus == CommonConst.ORDER_STS_YJZ
                            || (int) map2.get("orderStatus") == CommonConst.ORDER_STS_YJZ)
                    {
                        if (null != order.getIsMember() && order.getIsMember() == 1)
                        {
                            levelService.pushAddPointMessage(5, null, 1, shopId.intValue(), 4, orderId, true);
                        }
                        OrderGoodsSettleUtil.detailOrderGoodsSettle(data.getId(), CommonConst.PAY_TYPE_SINGLE);
                    }
                    content.append("{");
                    content.append("\"action\":\"order\",");
                    content.append("\"orderId\":\"" + map2.get("orderId") + "\",");
                    content.append("\"status\":" + map2.get("orderStatus"));
                    content.append("}");
                    PushDto push = new PushDto();
                    push.setAction("order");
                    push.setContent(content.toString());
                    push.setUserId(user.getUserId());
                    push.setShopId(shopId);
                    this.pushService.pushInfoToUser2(push, CommonConst.USER_TYPE_ZREO);
                }
                else
                {
                    if (orderStatus == CommonConst.ORDER_STS_YJZ
                            || (int) map2.get("orderStatus") == CommonConst.ORDER_STS_YJZ)
                    {
                        //非会员订单结算
                        OrderGoodsSettleUtil.detailSingleXorder(order.getOrderId());
                    }
                }
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "同步收银机交易记录成功", result);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("同步收银机交易记录-系统异常", e);
            throw new APISystemException("同步收银机交易记录-系统异常", e);
        }
    }

    /**
     * 处理扫码请求
     *
     * @Title: dealScanRequest @param @param data @return void 返回类型 @throws
     */
    private void dealScanRequest(POSOrderDto posOrder, POSOrderDataDto data) throws Exception
    {
        String orderId = data.getId();
        Long billerId = data.getBillerId();
        Long cashierId = data.getCashierId();
        Long shopId = posOrder.getShopId();
        String mobile = data.getMobile();
        UserDto user = this.memberServcie.getUserByMobile(mobile);
        Integer status = null;
        int flag = 0;
        status = this.orderServcie.getOrderStatusById(orderId);
        int addOrEditFlag = CommonConst.HANDLE_FLAG_EDIT;
        if (status == null)
        {
            // 订单不存在执行新增操作
            addOrEditFlag = CommonConst.HANDLE_FLAG_ADD;
        }
        else
        {
            // 订单已结账不能修改
            CommonValidUtil.validIntNoEqual(status, CommonConst.ORDER_STS_YJZ, CodeConst.CODE_INVALID,
                    CodeConst.MSG_NOT_MODIFY_SETTLE);
        }
        Integer orderStatus = data.getOrderStatus();
        posOrder.setAddOrEditFlag(addOrEditFlag);
        posOrder.setUser(user);
        if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
        {// 新增操作校验
            // 订单场景必填及枚举
            CommonValidUtil.validIntNull(data.getOrderSceneType(), CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_ORDER_SCENE);
            // 支付状态必填及枚举
            Integer payStatus = data.getPayStatus();
            if (payStatus == null)
            {
                data.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
            }
            // 订单支付状态对应订单类型
            if (data.getPayStatus() == CommonConst.PAY_STATUS_PAYED)
            {
                // 已支付订单为全额支付订单
                data.setOrderType(CommonConst.ORDER_TYPE_ALL_PRICE);
            }
            else
            {
                // 未支付订单为预支付订单(一次未支付完成)
                data.setOrderType(CommonConst.ORDER_TYPE_REPAY);
            }
            // 商品列表不能为空
            List<POSOrderGoodsDto> POSGoodsList = data.getOrderInfo();
            CommonValidUtil
                    .validListNull(POSGoodsList, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OGOODS);
            // 下单员工
            if (CommonValidUtil.validPositLong(billerId))
            {
                flag = this.shopServcie.queryShopEmplExists(shopId, billerId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BILLER);
            }
            // 收银员
            if (CommonValidUtil.validPositLong(cashierId))
            {
                flag = this.shopServcie.queryShopEmplExists(shopId, cashierId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHIER);
            }
            // 菜单总价
            // CommonValidUtil.validDoubleNullZero(data.getTotal(),CodeConst.CODE_PARAMETER_NOT_NULL,
            // CodeConst.MSG_REQUIRED_TOTALPRICE);
        }
        else
        {// 修改操作校验
            // 订单场景非必填及枚举
            // 订单状态非必填及枚举
            // 支付状态非必填及枚举
            // 下单员工
            if (CommonValidUtil.validPositLong(billerId))
            {
                flag = this.shopServcie.queryShopEmplExists(shopId, billerId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BILLER);
            }
            // 收银员
            if (CommonValidUtil.validPositLong(cashierId))
            {
                flag = this.shopServcie.queryShopEmplExists(shopId, cashierId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHIER);
            }
        }
        if (orderStatus == CommonConst.ORDER_STS_YKD)
        {
            // 已开单，支付状态改为未支付
            data.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
        }
        else if (orderStatus == CommonConst.ORDER_STS_YJZ)
        { // 结算状态
            // 已结账订单，支付状态必须为已支付
            CommonValidUtil.validIntEqual(data.getPayStatus(), CommonConst.PAY_STATUS_PAYED, CodeConst.CODE_INVALID,
                    CodeConst.MSG_PAYSTATUS_BE_PAYED);
            // 会员订单，支付时间必填
            if (user != null)
            {
                Date payTime = data.getPayTime();
                CommonValidUtil
                        .validObjectNull(payTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PAYTIME);
            }
            // 订单抹零标志必填 modify date:20150901(不做必填校验)
            // Integer isMaling = data.getIsMaling();
            // CommonValidUtil.validIntNull(isMaling,CodeConst.CODE_PARAMETER_NOT_NULL,
            // CodeConst.MSG_REQUIRED_ORDER_ISMALING);
            posOrder.setSettleFlag(0);
        }
        this.orderServcie.syncOrderList(posOrder);
    }

    /**
     * 分页订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/myorder/getMyOrders", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getMyOrders(
            HttpServletRequest request)
    {
        try
        {
            logger.info("分页获取订单列表-start");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String status = RequestUtils.getQueryParam(request, "queryStatus");
            String goodsNumberStr = RequestUtils.getQueryParam(request, "queryGoodsNum");
            String orderStatusStr = RequestUtils.getQueryParam(request, "orderStatus");
            String payStatusStr = RequestUtils.getQueryParam(request, "payStatus");
            String deleteTypeStr = RequestUtils.getQueryParam(request, "deleteType");
            String queryPayCodeFlag = RequestUtils.getQueryParam(request, "queryPayCodeFlag");
            // 检验user_id是否为空
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
            Integer queryStatus = null;
            if (StringUtils.isNotBlank(status))
            {
                queryStatus = NumberUtil.strToNum(status, "queryStatus");
            }
            Integer goodsNumber = 0;
            if (StringUtils.isNotBlank(goodsNumberStr))
            {
                goodsNumber = NumberUtil.strToNum(goodsNumberStr, "queryGoodsNum");
            }

            Long userId = NumberUtil.strToLong(userIdStr, "userId");

            if (StringUtils.isBlank(deleteTypeStr))
            {
                //为空就设置默认值
                deleteTypeStr = "0,2";
            }
            String[] deleteType = ArrayUtil.toArray(deleteTypeStr, CommonConst.COMMA_SEPARATOR);
            String[] orderStatus = ArrayUtil.toArray(orderStatusStr, CommonConst.COMMA_SEPARATOR);
            String[] payStatus = ArrayUtil.toArray(payStatusStr, CommonConst.COMMA_SEPARATOR);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("queryStatus", queryStatus);
            param.put("deleteType", deleteType);
            param.put("orderStatus", orderStatus);
            param.put("payStatus", payStatus);
            param.put("queryPayCodeFlag", queryPayCodeFlag);
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.orderServcie
                    .getMyOrders(userId, param, goodsNumber, PageModel.handPageNo(pageNO),
                            PageModel.handPageSize(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDERS_SUCCESS, msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单列表-系统异常", e);
            throw new APISystemException("获取订单列表-系统异常", e);
        }
    }

    /**
     * 我的订单个数
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/myorder/getMyOrderNumber", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getMyOrderNumber(
            HttpServletRequest request)
    {
        try
        {
            logger.info("查询我的订单个数-start");
            String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);

            // 检验user_id是否为空
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
            Long userId = NumberUtil.strToLong(userIdStr, CommonConst.USER_ID);

            // 获取我的订单数量
            List<Map<String, Object>> list = this.orderServcie.getMyOrderNumber(userId);

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(1);
            msgList.setrCount(list.size());
            msgList.setLst(list);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDERNUM_SUCCESS, msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取我的订单个数-系统异常", e);
            throw new APISystemException("获取我的订单个数-系统异常", e);
        }
    }

    /**
     * 用户下单
     * <p/>
     * <p>
     * 使用RequestBody声明，接收json自动转换映射到dto<br>
     * consumes必须声明，表示只接受json
     * </p>
     *
     * @param order
     * @param request
     * @return
     */
    @RequestMapping(value = { "/order/placeOrder", "/service/order/placeOrder", "/token/order/placeOrder",
            "/session/order/placeOrder" }, method = RequestMethod.POST, consumes = "application/json") @ResponseBody public ResultDto placeOrder(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("用户下单-start");
            OrderDto order = (OrderDto) JacksonUtil.postJsonToObj(entity, OrderDto.class, DateUtils.DATETIME_FORMAT);
            if (null != order)
            {
                if (order.getConsumerNum() == null)
                {
                    order.setConsumerNum(order.getPeopleNumber());
                }
            }
            if (order.getClientSystemType() == null)
            {
                order.setClientSystemType(ClientSystemTypeEnum.CONSUMER_APP.getValue());
            }
            order.setOrderTime(new Date());
            logger.info("order实体：" + order);
            // 1. 下订单集中常规验证
            placeOrderValid(order, 1);

            // 没有服务费设置默认值0
            if (!NumberUtil.isDouble(order.getLogisticsPrice() + ""))
            {
                order.setLogisticsPrice(0D);
            }
            // 生成订单号
            String orderId = FieldGenerateUtil.generateOrderId(order.getShopId());
            order.setOrderId(orderId);
            order.setLastUpdateTime(new Date());
            // 2. 保存订单信息
            OrderDto pOrder = this.orderServcie.placeOrder(order, 1);
            if (StringUtils.isBlank(pOrder.getPayCode()))
            {
                // 3. 记录订单日志
                OrderLogDto orderLog = new OrderLogDto();
                orderLog.setOrderId(orderId);
                orderLog.setPayStatus(0);
                orderLog.setOrderStatus(order.getOrderStatus());
                orderLog.setRemark("已预订");
                orderLog.setLastUpdateTime(new Date());

                OrderLogTask ot = new OrderLogTask(orderLog);

                List<Runnable> tasks = new ArrayList<Runnable>();
                // 激活预定商铺资源任务(预定商铺资源订单使用的是更新订单接口)
                // if (CommonConst.PLACE_ORDER_LIVE == order.getOrderSceneType()) {
                // EnableShopResourceTask enableRsrc = new
                // EnableShopResourceTask(orderId);
                // tasks.add(enableRsrc);
                // }
                // 4. 推送给商铺和用户
                // 订单预约成功推送给用户(到店点菜不推送)
                // 推收银机
                PlaceOrderPushMessageTask messageTask = new PlaceOrderPushMessageTask(pOrder);
                tasks.add(messageTask);
                // 推用户
                /*
                 * OrderPushUserTask pushTask = new OrderPushUserTask(pOrder);
                 * tasks.add(pushTask);
                 */
                // 保存订单日志任务
                tasks.add(ot);
                // 启动线程池执行批量任务
                AsyncExecutorUtil.executeList(tasks);
            }
            // 5. 响应信息
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", orderId);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_PLACEORDER, map);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("用户下单-系统异常", e);
            throw new APISystemException("用户下单-系统异常", e);
        }
    }

    /**
     * 下订单和修改订单前置数据校验
     *
     * @param order
     * @param handle 1，下单校验；2，更新订单校验
     * @throws Exception
     */
    private void placeOrderValid(OrderDto order, int handle) throws Exception
    {
        Integer isMember = order.getIsMember();
        OrderDto pOrder = null;
        // 订单所属商铺ID
        Long orderShopId = order.getShopId();
        // 订单所属用户ID
        Long userId = order.getUserId();
        if (handle == 2)
        {// 更新订单校验
            String orderId = order.getOrderId();
            // 订单号必填
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
            // 订单存在性
            pOrder = this.orderServcie.getOrderMainById(orderId);
            if (pOrder != null)
            {
                // 订单所属商铺不能被修改
                CommonValidUtil.validLongEqual(orderShopId, pOrder.getShopId(), CodeConst.CODE_CANNOT_MODIFY,
                        CodeConst.MSG_SHOP_NO_SAME);
                // 订单所属会员不能被修改
                CommonValidUtil.validLongEqual(userId, pOrder.getUserId(), CodeConst.USER_CODE_CANNOT_MODIFY,
                        CodeConst.MSG_NOT_SAME_USERID);
                // 订单状态不能修改
                order.setOrderStatus(null);
            }
            else
            {
                // 设置订单新增标志
                order.setAddOrEditFlag(CommonConst.HANDLE_FLAG_ADD);
                // 下订单时间
                order.setOrderTime(new Date());
            }
        }
        // 下订单场景
        Integer orderSceneType = order.getOrderSceneType();
        // 下订单场景必填及检查下订单场景枚举
        CommonValidUtil
                .validIntNull(orderSceneType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDER_SCENE);
        // 商铺ID必填及存在性
        CommonValidUtil.validLongNull(orderShopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        // 订单所属商铺存在性
        ShopDto pShop = this.shopServcie.getShopMainOfCacheById(orderShopId);
        CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        Integer shopStatus = pShop.getShopStatus();
        if (shopStatus == null || shopStatus != 0)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_STATUS);
        }
        // 商铺名称
        order.setShopName(pShop.getShopName());
        order.setIsMember(CommonConst.USER_IS_NOT_MEMBER);//默认为非会员订单
        // 验证会员必填及存在性
        UserDto user = this.memberServcie.getUserByUserId(userId);
        if (null != user)
        {
            order.setIsMember(user.getIsMember());
        }

        // 更新订单接口调用并且是新增订单操作、下订单接口调用需要设置订单状态
        setOrderStatus(order);
        // if ((handle == 2 && addOrEditFlag == CommonConst.HANDLE_FLAG_ADD) ||
        // handle != 2) {
        // setOrderStatus(order);
        // }
        // 订单类型必填及枚举
        Integer orderType = order.getOrderType();
        CommonValidUtil.validIntNull(orderType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDER_TYPE);
        if (orderType == CommonConst.ORDER_TYPE_REPAY)
        {
            // 预订单定金必填(暂时放开)
            // Double prepayMoney = order.getPrepayMoney();
            // CommonValidUtil.validDoubleNull(prepayMoney,
            // CodeConst.CODE_PARAMETER_NOT_NULL,
            // CodeConst.MSG_REQUIRED_PREPAY);
        }
        else
        {
            if (orderSceneType != CommonConst.PLACE_ORDER_SCAN)
            {
                order.setPrepayMoney(null);
                // 非预订单商品列表不能为空
                List<OrderGoodsDto> gList = order.getGoods();
                CommonValidUtil.validListNull(gList, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OGOODS);
            }

        }

        /*------------以下为各类订单场景条件校验-------------*/
        if (orderSceneType == CommonConst.PLACE_ORDER_LIVE)
        {// 到店点菜（预定商铺资源不走下单接口，目前只有自助下单）
            // 餐饮预定到店场景需要获取预定时间
            setServiceTime(order);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_WM)
        {// 外卖订单
            wmOrderValid(order, pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_GOODS)
        {// 便利店商品订单
            goodsOrderValid(order, pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_SERVICE)
        {// 服务订单
            serviceOrderValid(order, pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_BEAUTY)
        {// 丽人订单
            beautyOrderValid(order, pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_VENUE)
        {// 场馆订单
            venueOrderValid(order, pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_QIXIU)
        {// 汽修
            // venueOrderValid(order,pShop);
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_SCAN)// 扫静态二维码订单
        {
            if (isMember != null)
            {
                order.setIsMember(isMember);
            }

            if (StringUtils.isBlank(order.getOrderTitle()))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "扫二维码下单时订单标题不能为空");
            }

            if (StringUtils.isBlank(order.getPayCode()))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "扫二维码下单时订单支付码不能为空");
            }

            if (order.getOrderTotalPrice() == null || order.getOrderTotalPrice() < 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "扫二维码下单时orderTotalPrice不能为空");
            }
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER_SCENE);
        }

        if (order.getCouponDiscountPrice() == null && order.getUserShopCouponIdList() != null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时deductAmount必填");
        }

        if (order.getCouponDiscountPrice() != null && order.getUserShopCouponIdList() == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "使用优惠券时userShopCouponIdList必填");
        }
    }

    private void setOrderStatus(OrderDto order) throws Exception
    {
        Long shopId = order.getShopId();
        // 商铺订单待确认时间限制
        Integer confirmMinute = this.shopServcie.getShopConfirmMinute(shopId);
        // 订单来源
        Integer orderSource = order.getOrderSource();
        // 订单状态
        if (confirmMinute != null && confirmMinute.intValue() > 0)
        {
            order.setOrderStatus(CommonConst.ORDER_STS_DQR);
            if (orderSource == null)
            {
                order.setOrderSource(Integer.valueOf(0));
            }
            else if (orderSource == CommonConst.ORDER_SOURCE_ZZXD)
            {
                // 当自助下单和待确认同时存在时，自助下单订单状态优先
                order.setOrderStatus(CommonConst.ORDER_STS_ZZXD);
            }
        }
        else
        {
            // 扫码下单订单状态为“自助下单（8）”，其他为已预订0
            if (orderSource != null && orderSource == CommonConst.ORDER_SOURCE_ZZXD)
            {
                order.setOrderStatus(CommonConst.ORDER_STS_ZZXD);
            }
            else
            {
                // 非自助下单
                order.setOrderSource(CommonConst.ORDER_SOURCE_FZZXD);
                // 已预订
                order.setOrderStatus(CommonConst.ORDER_STS_YYD);
            }
        }
    }

    /**
     * 外卖订单数据校验
     *
     * @param order
     * @throws Exception
     */
    private void wmOrderValid(OrderDto order, ShopDto shop) throws Exception
    {
        Long userId = order.getUserId();
        Long shopId = shop.getShopId();
        Date orderTime = order.getOrderTime();
        // 验证商铺必须启用外卖
        Integer takeoutFlag = shop.getTakeoutFlag();
        CommonValidUtil.validIntEqual(takeoutFlag, CommonConst.SHOP_TAKEOUT_FLAG_ENABLE, CodeConst.CODE_DISABLE_TAKEOUT,
                CodeConst.MSG_DISABLE_TAKEOUT);
        // 配送方式
        Integer distributionType = order.getDistributionType();
        CommonValidUtil.validIntNoNull(distributionType, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG_REQUIRED_DISTRIB_TYPE);
        if (CommonConst.ORDER_SEND_TYPE_FASTEST == distributionType)
        {// 立即配置
            // 配送时间设置为当前系统时间
            order.setDistributionTime(new Date());
        }
        else
        {// 定时配送
            // 配送时间必填
            CommonValidUtil.validObjectNull(order.getDistributionTime(), CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_SEND_TIME);
        }
        // 获取商铺预定规则
        DistribTakeoutSettingDto takeoutSetting = this.distribTakeoutSettingService
                .getDistribTakeoutSetting(shopId, CommonConst.TAKEOUT_SETTINGTYPE_WM);
        if (takeoutSetting != null)
        {
            // 暂停预定时间范围
            String stopDate = takeoutSetting.getStopDate();
            // 下订单日期不在暂停预定日期范围内
            this.validStopDate(DateUtils.getDateOfDateTime(orderTime), stopDate, CodeConst.CODE_IN_STOP_DATE,
                    CodeConst.MSG_IN_STOP_DATE);
            // 配送时间
            Date distribDate = order.getDistributionTime();
            // 可预定周期
            String week = takeoutSetting.getWeekDay();
            // 配送日期是否在暂停预定时间范围内
            this.validDistributionDate(DateUtils.getDateOfDateTime(distribDate), stopDate);
            // 配送时间是否在可预定周期内
            this.validDistribDateIsInStopWeek(distribDate, week, CodeConst.CODE_DISTRIB_DATE_NOTIN_WEEK,
                    CodeConst.MSG_DISTRIB_DATE_NOTIN_WEEK);
            // 可配送时间范围
            String deliveryTime = takeoutSetting.getDeliveryTime();
            // 配送时间点在可配送时间范围内
            this.validDeliveryTime(DateUtils.getTimeOfDateTime(distribDate), deliveryTime,
                    CodeConst.CODE_DISTRIB_TIME_NOTIN_DELIVERY, CodeConst.MSG_DISTRIB_TIME_NOTIN_DELIVERY);
            // 最低起订金额
            order.setLeastBookPrice(takeoutSetting.getLeastBookPrice());
        }
        // 商品列表必填
        // CommonValidUtil.validListNull(gList, CodeConst.CODE_REQUIRED,
        // CodeConst.MSG_REQUIRED_OGOODS);

        // 配送地址
        Long addressId = order.getAddressId();
        if (!NumberUtil.isPositLong(addressId))
        {
            // 获取默认收货地址
            addressId = this.userAddressService.getDefAddressIdByUser(userId);
            CommonValidUtil.validPositLong(addressId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_DEFADDRESS_NULL);
            order.setAddressId(addressId);
        }
    }

    /**
     * 验证配送时间是否在可配送时间范围内
     *
     * @param deliveryTime 时间段，格式如: 12:00&23:00,12:00&23:00
     * @return
     */
    private void validDeliveryTime(Date time, String deliveryTime, int code, String msg) throws Exception
    {
        if (!StringUtils.isBlank(deliveryTime))
        {
            String[] deliveryTimeArray = deliveryTime.split(",");
            String beginTimeStr;
            Date beginTime;
            String endTimeStr;
            Date endTime;
            boolean flag = false;
            for (String e : deliveryTimeArray)
            {
                if (e.contains("&"))
                {
                    beginTimeStr = e.substring(0, e.indexOf("&"));
                    endTimeStr = e.substring(e.indexOf("&") + 1, e.length());
                    beginTime = DateUtils.getTimeByPattern(beginTimeStr, DateUtils.TIME_FORMAT2);
                    endTime = DateUtils.getTimeByPattern(endTimeStr, DateUtils.TIME_FORMAT2);
                    // CommonValidUtil.validDateOfDateRange(time, beginTime,
                    // endTime, code, msg);
                    flag = CommonValidUtil.validDateOfDateRange(time, beginTime, endTime);
                    if (flag == true)
                    {
                        return;
                    }
                    else
                    {
                        flag = false;
                    }
                }
            }
            if (flag == false)
            {
                throw new ValidateException(code, msg);
            }
        }
    }

    /**
     * 校验下单时间是否在暂停预定时间范围内
     *
     * @param stopDate 时间段，格式如: 2015-07-10&2015-07-12,2015-07-11&2015-07-12
     * @throws Exception
     */
    private void validStopDate(Date date, String stopDate, int code, String msg) throws Exception
    {
        if (!StringUtils.isBlank(stopDate))
        {
            String[] stopDateArray = stopDate.split(",");
            String beginTimeStr;
            Date beginTime;
            String endTimeStr;
            Date endTime;
            for (String e : stopDateArray)
            {
                if (e.contains("&"))
                {
                    beginTimeStr = e.substring(0, e.indexOf("&"));
                    endTimeStr = e.substring(e.indexOf("&") + 1, e.length());
                    beginTime = DateUtils.getTimeByPattern(beginTimeStr, DateUtils.DATE_FORMAT);
                    endTime = DateUtils.getTimeByPattern(endTimeStr, DateUtils.DATE_FORMAT);
                    beginTime = DateUtils.parse(DateUtils.format(beginTime, DateUtils.ZEROTIME_FORMAT));
                    endTime = DateUtils.parse(DateUtils.format(endTime, DateUtils.ENDTIME_FORMAT));
                    CommonValidUtil.validDateNotInDateRange(date, beginTime, endTime, code, msg);
                }
            }
        }
    }

    /**
     * 校验配送时间是否在暂停预定时间范围内
     *
     * @param distribDate
     * @param stopDate
     * @throws Exception
     */
    private void validDistributionDate(Date distribDate, String stopDate) throws Exception
    {
        if (!StringUtils.isBlank(stopDate))
        {
            String[] stopDateArray = stopDate.split(",");
            String beginTimeStr;
            Date beginTime;
            String endTimeStr;
            Date endTime;
            for (String e : stopDateArray)
            {
                if (e.contains("&"))
                {
                    beginTimeStr = e.substring(0, e.indexOf("&"));
                    endTimeStr = e.substring(e.indexOf("&") + 1, e.length());
                    beginTime = DateUtils.getTimeByPattern(beginTimeStr, DateUtils.DATE_FORMAT);
                    endTime = DateUtils.getTimeByPattern(endTimeStr, DateUtils.DATE_FORMAT);
                    beginTime = DateUtils.parse(DateUtils.format(beginTime, DateUtils.ZEROTIME_FORMAT));
                    endTime = DateUtils.parse(DateUtils.format(endTime, DateUtils.ENDTIME_FORMAT));
                    CommonValidUtil.validDateNotInDateRange(distribDate, beginTime, endTime,
                            CodeConst.CODE_DISTRIB_DATE_IN_STOP_DATE, CodeConst.MSG_DISTRIB_DATE_IN_STOP_DATE);
                }
            }
        }
    }

    private void validDistribDateIsInStopWeek(Date distribDate, String stopWeek, int code, String msg) throws Exception
    {
        if (!StringUtils.isBlank(stopWeek))
        {
            String[] stopWeekArray = stopWeek.split(",");
            int week = DateUtils.getWeekByDate(distribDate);
            int flag = 1;
            for (String e : stopWeekArray)
            {
                if (!StringUtils.isBlank(e))
                {
                    if (week == Integer.valueOf(e).intValue())
                    {
                        return;
                    }
                    else
                    {
                        flag = 0;
                    }
                }
            }
            if (flag == 0)
            {
                throw new ValidateException(code, msg);
            }
        }
    }

    /**
     * 便利店商品订单数据校验
     *
     * @param order
     * @param shop
     * @throws Exception
     */
    private void goodsOrderValid(OrderDto order, ShopDto shop) throws Exception
    {
        Long userId = order.getUserId();
        Long shopId = shop.getShopId();
        // 配送方式
        Integer distributionType = order.getDistributionType();
        CommonValidUtil.validIntNoNull(distributionType, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG_REQUIRED_DISTRIB_TYPE);
        if (CommonConst.ORDER_SEND_TYPE_FASTEST == distributionType)
        {// 立即配置
            // 配送时间设置为当前系统时间
            order.setDistributionTime(new Date());
        }
        else
        {// 定时配送
            // 配送时间必填
            CommonValidUtil.validObjectNull(order.getDistributionTime(), CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_SEND_TIME);
        }
        Date distriTime = order.getDistributionTime();
        // 是否启动预定
        Integer bookFlag = shop.getBookFlag();
        CommonValidUtil.validIntEqual(bookFlag, CommonConst.SHOP_BOOK_FLAG_ENABLE, CodeConst.CODE_DISABLE_BOOK,
                CodeConst.MSG_DISABLE_BOOK);
        // 获取商铺预定规则
        DistribTakeoutSettingDto takeoutSetting = this.distribTakeoutSettingService
                .getDistribTakeoutSetting(shopId, CommonConst.TAKEOUT_SETTINGTYPE_PS);
        if (takeoutSetting != null)
        {
            // 可配送时间范围
            String deliveryTime = takeoutSetting.getDeliveryTime();
            // 配送时间在可配送时间范围内
            this.validDeliveryTime(DateUtils.getTimeOfDateTime(distriTime), deliveryTime,
                    CodeConst.CODE_DISTRIB_TIME_NOTIN_DELIVERY, CodeConst.MSG_DISTRIB_TIME_NOTIN_DELIVERY);
            // 最低起订金额
            order.setLeastBookPrice(takeoutSetting.getLeastBookPrice());
        }
        // 配送地址
        Long addressId = order.getAddressId();
        if (!NumberUtil.isPositLong(addressId))
        {
            // 获取默认收货地址
            addressId = this.userAddressService.getDefAddressIdByUser(userId);
            CommonValidUtil.validPositLong(addressId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_DEFADDRESS_NULL);
            order.setAddressId(addressId);
        }
        Double logisticsPrice = order.getLogisticsPrice();
        CommonValidUtil.validDoubleNull(logisticsPrice, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG_REQUIRED_LOGISTICS_PRICE);
    }

    /**
     * 服务订单数据校验
     *
     * @param order
     * @throws Exception
     */
    private void serviceOrderValid(OrderDto order, ShopDto shop) throws Exception
    {
        // 服务方式及枚举
        Integer serviceType = order.getOrderServiceType();
        CommonValidUtil
                .validIntNoNull(serviceType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SERVICE_TYPE);
        Integer flag = null;
        if (serviceType == CommonConst.ORDER_SERVICE_TYPE_DOOR)
        {
            // 验证上门服务标志
            flag = this.shopServcie.getIsHomeService(shop.getShopId());
            CommonValidUtil.validIntEqual(flag, CommonConst.SHOP_BOOK_DOOR_ENABLE, CodeConst.CODE_DISABLE_HOME_SERVICE,
                    CodeConst.MSG_DISABLE_HOME_SERVICE);
        }
        else
        {
            // 验证预定到店标志
            flag = this.shopServcie.getBookFlag(shop.getShopId());
            CommonValidUtil.validIntEqual(flag, CommonConst.SHOP_BOOK_FLAG_ENABLE, CodeConst.CODE_DISABLE_BOOK,
                    CodeConst.MSG_DISABLE_BOOK);
        }
        // 预定时间
        Date startTime = order.getServiceTimeFrom();
        CommonValidUtil.validObjectNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STARTTIME);
        Date stopTime = order.getServiceTimeTo();
        CommonValidUtil.validObjectNull(stopTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STOPTIME);
        // 商品列表
        // CommonValidUtil.validListNull(gList, CodeConst.CODE_REQUIRED,
        // CodeConst.MSG_REQUIRED_OGOODS);
    }

    /**
     * 丽人订单数据校验
     *
     * @param order
     * @param shop
     * @throws Exception
     */
    private void beautyOrderValid(OrderDto order, ShopDto shop) throws Exception
    {
        // 服务方式及枚举
        Integer serviceType = order.getOrderServiceType();
        CommonValidUtil
                .validIntNoNull(serviceType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SERVICE_TYPE);
        Integer flag = null;
        if (serviceType == CommonConst.ORDER_SERVICE_TYPE_DOOR)
        {
            // 验证上门服务标志
            flag = this.shopServcie.getIsHomeService(shop.getShopId());
            CommonValidUtil.validIntEqual(flag, CommonConst.SHOP_BOOK_DOOR_ENABLE, CodeConst.CODE_DISABLE_HOME_SERVICE,
                    CodeConst.MSG_DISABLE_HOME_SERVICE);
        }
        else
        {
            // 验证预定到店标志
            flag = this.shopServcie.getBookFlag(shop.getShopId());
            CommonValidUtil.validIntEqual(flag, CommonConst.SHOP_BOOK_FLAG_ENABLE, CodeConst.CODE_DISABLE_BOOK,
                    CodeConst.MSG_DISABLE_BOOK);
        }
        // 服务开始时间必填
        Date startTime = order.getServiceTimeFrom();
        CommonValidUtil.validObjectNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STARTTIME);
    }

    /**
     * 设置订单服务时间
     * <p/>
     * 目前适用订单场景：<br>
     * 1，餐饮预定；<br>
     * 2，丽人；<br>
     * 3，场馆。
     *
     * @param order
     * @throws Exception
     */
    private void setServiceTime(OrderDto order) throws Exception
    {

        if (order.getOrderSource() == CommonConst.ORDER_SOURCE_FZZXD)
        {// 非自助下单
            OrderShopRsrcDto osr = this.osrService.getOrderShopRsrcLimitOne(order.getOrderId());
            if (osr != null)
            {
                String reserveDateStr = null;
                Date reserveDate = osr.getReserveResourceDate();
                String startTime = osr.getStartTime();
                startTime = !StringUtils.isBlank(startTime) ? startTime : null;
                if (reserveDate != null && !StringUtils.isBlank(startTime))
                {
                    reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT) + " " + startTime;
                    // 设置服务开始时间
                    order.setServiceTimeFrom(DateUtils.parse(reserveDateStr, DateUtils.DATETIME_FORMAT));
                    // 场馆订单场景设置服务结束时间
                    if (order.getOrderSceneType() == CommonConst.PLACE_ORDER_VENUE)
                    {
                        String endTime = osr.getEndTime();
                        endTime = !StringUtils.isBlank(endTime) ? endTime : null;
                        if (!StringUtils.isBlank(endTime))
                        {
                            reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT) + " " + endTime;
                            // 设置服务结束时间
                            order.setServiceTimeTo(DateUtils.parse(reserveDateStr, DateUtils.DATETIME_FORMAT));
                        }
                    }
                }
            }
        }
        else
        {// 自助下单
            // 自助下单服务时间是下单时间
            order.setServiceTimeFrom(new Date());
        }

    }

    /**
     * 场馆订单数据校验
     *
     * @param order
     * @param shop
     * @throws Exception
     */
    private void venueOrderValid(OrderDto order, ShopDto shop) throws Exception
    {
        // 服务方式默认是到店服务
        order.setOrderServiceType(CommonConst.ORDER_SERVICE_TYPE_STORE);
        // 验证预定到店标志
        Integer flag = this.shopServcie.getBookFlag(shop.getShopId());
        CommonValidUtil.validIntEqual(flag, CommonConst.SHOP_BOOK_FLAG_ENABLE, CodeConst.CODE_DISABLE_BOOK,
                CodeConst.MSG_DISABLE_BOOK);
        // 服务开始时间必填
        Date startTime = order.getServiceTimeFrom();
        CommonValidUtil.validObjectNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STARTTIME);
        // 服务结束时间必填
        Date stopTime = order.getServiceTimeTo();
        CommonValidUtil.validObjectNull(stopTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_STOPTIME);
    }

    // 用户下订单校验规则旧逻辑 comment by lujianping date 20150715
    // private void placeOrderValid(OrderDto order, int handle) throws Exception
    // {
    // OrderDto pOrder = null;
    // Long orderShopId = order.getShopId();
    // Long userId = order.getUserId();
    // if (handle == 2) {
    // String orderId = order.getOrderId();
    // // 订单号必填
    // CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_ORDERID);
    // // 订单存在性
    // pOrder = this.orderServcie.getOrderMainById(orderId);
    // // 订单存在，则订单商铺不能变
    // if (pOrder != null) {
    // CommonValidUtil.validLongEqual(orderShopId, pOrder.getShopId(),
    // CodeConst.CODE_CANNOT_MODIFY, CodeConst.MSG_SHOP_NO_SAME);
    // order.setOrderStatus(null);
    // } else {
    // order.setAddOrEditFlag(CommonConst.HANDLE_FLAG_ADD);
    // order.setOrderTime(new Date());
    // order.setOrderStatus(CommonConst.ORDER_STATUS_REPAYED);
    // }
    // }
    // int addOrEditFlag = order.getAddOrEditFlag();
    // /*
    // * 1，必填验证
    // */
    // Integer orderSceneType = order.getOrderSceneType();
    // // 1.1 下订单场景必填及检查下订单场景枚举
    // CommonValidUtil.validIntNull(orderSceneType,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_ORDER_SCENE);
    // // 1.2 商铺ID必填及存在性
    // CommonValidUtil.validPositLong(orderShopId,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_SHOPID);
    //
    // // int num = this.shopServcie.queryShopExists(orderShopId);
    // // CommonValidUtil.validPositInt(num, CodeConst.CODE_MISS_MODEL,
    // // CodeConst.MSG_MISS_SHOP);
    // ShopDto pShop = this.shopServcie.getShopMainOfCacheById(orderShopId);
    // CommonValidUtil.validObjectNull(pShop,
    // CodeConst.CODE_PARAMETER_NOT_EXIST,
    // CodeConst.MSG_MISS_SHOP);
    // order.setShopName(pShop.getShopName());// 商铺名称
    //
    // int num;
    // // 1.3 验证会员必填及存在性
    // num = this.memberServcie.queryUserExists(userId);
    // CommonValidUtil.validPositInt(num, CodeConst.CODE_PARAMETER_NOT_EXIST,
    // CodeConst.MSG_MISS_MEMBER);
    // if (handle == 2) {
    // // 会员和订单所属用户匹配
    // if (addOrEditFlag != CommonConst.HANDLE_FLAG_ADD) {
    // CommonValidUtil.validLongEqual(userId, pOrder.getUserId(),
    // CodeConst.USER_CODE_CANNOT_MODIFY, CodeConst.MSG_NOT_SAME_USERID);
    // }else{
    // if(order.getOrderSource() != null && order.getOrderSource() == 1){
    // order.setOrderStatus(CommonConst.ORDER_STATUS_AUTO);
    // }else{
    // order.setOrderSource(0);
    // order.setOrderStatus(CommonConst.ORDER_STATUS_REPAYED);
    // }
    // }
    // }
    // // 1.4 订单类型必填及枚举
    // Integer orderType = order.getOrderType();
    // CommonValidUtil.validIntNull(orderType,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_ORDER_TYPE);
    // // 预订单，定金必填
    // if (orderType == CommonConst.ORDER_TYPE_REPAY) {
    // Double prepayMoney = order.getPrepayMoney();
    // CommonValidUtil.validDoubleNull(prepayMoney,
    // CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PREPAY);
    // } else {
    // order.setPrepayMoney(null);
    // List<OrderGoodsDto> gList = order.getGoods();
    // CommonValidUtil.validListNull(gList, CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_OGOODS);
    // }
    // /*
    // * 2，条件验证
    // */
    // if (orderSceneType == CommonConst.PLACE_ORDER_LIVE) {// 到店点菜
    // // 2.1订单类型必须全额订单
    // // CommonValidUtil.validIntEqual(orderType,
    // // CommonConst.ORDER_TYPE_ALL_PRICE, CodeConst.CODE_INVALID,
    // // CodeConst.MSG_LIVE_BE_ALLPRICE);
    // // 2.2 商品列表必填
    // // CommonValidUtil.validListNull(gList, CodeConst.CODE_REQUIRED,
    // // CodeConst.MSG_REQUIRED_OGOODS);
    // } else if (orderSceneType == CommonConst.PLACE_ORDER_WM
    // || orderSceneType == CommonConst.PLACE_ORDER_GOODS) {// 外卖及商品
    // // 2.1 商品列表必填
    // // CommonValidUtil.validListNull(gList, CodeConst.CODE_REQUIRED,
    // // CodeConst.MSG_REQUIRED_OGOODS);
    // // 2.2 配送方式
    // Integer distributionType = order.getDistributionType();
    // CommonValidUtil.validIntNoNull(distributionType,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_DISTRIB_TYPE);
    // if (CommonConst.ORDER_SEND_TYPE_FASTEST == distributionType) {// 立即配置
    // // 配送时间，设置默认当前系统时间
    // order.setDistributionTime(new Date());
    // } else {// 定时配送
    // // 配送时间必填
    // CommonValidUtil.validObjectNull(order.getDistributionTime(),
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_SEND_TIME);
    // }
    // // 2.3 配送地址
    // Long addressId = order.getAddressId();
    // if (!NumberUtil.isPositLong(addressId)) {
    // // 获取默认收货地址
    // addressId = this.userAddressService
    // .getDefAddressIdByUser(userId);
    // CommonValidUtil.validPositLong(addressId,
    // CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_DEFADDRESS_NULL);
    // order.setAddressId(addressId);
    // }
    // // 2.4 服务费或物流费（商品订单）
    // if (CommonConst.PLACE_ORDER_GOODS == orderSceneType) {
    // Double logisticsPrice = order.getLogisticsPrice();
    // CommonValidUtil.validDoubleNull(logisticsPrice,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_LOGISTICS_PRICE);
    // }
    // } else if (orderSceneType == CommonConst.PLACE_ORDER_SERVICE) {// 服务订单
    // // 2.1 服务方式及枚举
    // Integer serviceType = order.getOrderServiceType();
    // CommonValidUtil.validIntNoNull(serviceType,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_SERVICE_TYPE);
    // // 2.2 预定时间
    // Date startTime = order.getServiceTimeFrom();
    // CommonValidUtil.validObjectNull(startTime,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_STARTTIME);
    // Date stopTime = order.getServiceTimeTo();
    // CommonValidUtil.validObjectNull(stopTime,
    // CodeConst.CODE_PARAMETER_NOT_NULL,
    // CodeConst.MSG_REQUIRED_STOPTIME);
    // // 2.3 商品列表
    // // CommonValidUtil.validListNull(gList, CodeConst.CODE_REQUIRED,
    // // CodeConst.MSG_REQUIRED_OGOODS);
    // } else {
    // throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
    // CodeConst.MSG_MISS_ORDER_SCENE);
    // }
    // }

    @RequestMapping(value = "/order/updateOrder", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public ResultDto updateOrder(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("修改订单-start");
            OrderDto order = (OrderDto) JacksonUtil.postJsonToObj(entity, OrderDto.class, DateUtils.DATETIME_FORMAT);
            logger.info("order实体：" + order);
            // 1. 更新订单集中数据校验
            placeOrderValid(order, 2);
            // 2. 更新订单信息
            OrderDto orderDto = this.orderServcie.updateOrder(order);
            Integer orderSceneType = order.getOrderSceneType();
            String orderId = order.getOrderId();
            // 3. 餐饮预定到店点菜（预定商铺资源）及服务订单, 激活预定的商铺资源
            if (CommonConst.PLACE_ORDER_LIVE == orderSceneType || CommonConst.PLACE_ORDER_BEAUTY == orderSceneType
                    || CommonConst.PLACE_ORDER_VENUE == orderSceneType)
            {
                // 激活预定的商铺资源
                EnableShopResourceTask enableRsrc = new EnableShopResourceTask(orderId);
                // 启动线程池执行任务
                AsyncExecutorUtil.execute(enableRsrc);
            }
            // 4. 记录订单日志和消息推送商铺和用户
            Integer addOrEditFlag = order.getAddOrEditFlag();
            if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
            {// 新增订单需要推送
                OrderLogDto orderLog = new OrderLogDto();
                orderLog.setOrderId(orderId);
                orderLog.setPayStatus(0);
                orderLog.setOrderStatus(0);
                orderLog.setRemark("已预订");
                orderLog.setLastUpdateTime(new Date());

                OrderLogTask ot = new OrderLogTask(orderLog);
                PlaceOrderPushMessageTask messageTask = new PlaceOrderPushMessageTask(order);
                OrderPushUserTask pushTask = new OrderPushUserTask(orderDto);
                List<Runnable> tasks = new ArrayList<Runnable>();
                // 保存订单日志任务
                tasks.add(ot);
                // 订单消息推送任务
                tasks.add(messageTask);
                // 订单预约成功推送用户
                tasks.add(pushTask);
                // 启动线程池执行批量任务
                AsyncExecutorUtil.executeList(tasks);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", order.getOrderId());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UPDATEORDER, map);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("修改订单-系统异常", e);
            throw new APISystemException("修改订单-系统异常", e);
        }
    }

    /**
     * 获取订单详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/getOrderDetail", produces = "application/json;charset=UTF-8") @ResponseBody public Object getOrderDetail(
            HttpServletRequest request)
    {
        try
        {
            logger.info("获取订单详情-start");
            String orderId = RequestUtils.getQueryParam(request, "orderId");
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
            OrderDetailDto od = this.orderServcie.wrapOrderDetail(orderId, null);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_ORDERDETAIL, od,
                    DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单详情-系统异常", e);
            throw new APISystemException("获取订单详情-系统异常", e);
        }
    }

    /**
     * 用户预定商铺资源
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/reserveShopResource", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public Object reserveShopRsrc(
            HttpEntity<String> entity)
    {
        try
        {
            logger.info("用户预定商铺资源-start");
            OrderShopRsrcDto oShopRsrcDto = (OrderShopRsrcDto) JacksonUtil
                    .postJsonToObj(entity, OrderShopRsrcDto.class);
            reserveShopRsrcValid(oShopRsrcDto);
            this.orderServcie.reserveShopRsrcApp(oShopRsrcDto);
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_RESERV, null);
        }
        catch (JsonMappingException e)
        {
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("用户预定商铺资源-系统异常", e);
            throw new APISystemException("用户预定商铺资源-系统异常", e);
        }
    }

    /**
     * 用户预定商铺资源数据校验
     *
     * @param oShopRsrcDto
     * @throws Exception
     */
    private void reserveShopRsrcValid(OrderShopRsrcDto oShopRsrcDto) throws Exception
    {
        Long userId = oShopRsrcDto.getUserId();
        Long shopId = oShopRsrcDto.getShopId();
        String orderId = oShopRsrcDto.getOrderId();
        Date reserveResourceDate = oShopRsrcDto.getReserveResourceDate();
        String resourceType = oShopRsrcDto.getResourceType();
        String startTime = oShopRsrcDto.getStartTime();
        String endTime = oShopRsrcDto.getEndTime();
        Integer serverMode = oShopRsrcDto.getServerMode();
        int settingType = getSettingType(serverMode);

        CommonValidUtil.validLongNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
        CommonValidUtil.validPositLong(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
        CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        CommonValidUtil.validObjectNull(reserveResourceDate, CodeConst.CODE_PARAMETER_NOT_NULL,
                CodeConst.MSG_REQUIRED_RESOURCE_TYPE);
        CommonValidUtil
                .validObjectNull(resourceType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_RESOURCE_TYPE);
        CommonValidUtil
                .validObjectNull(startTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_START_TIME);

        // 验证用户存在性
        UserDto pUser = this.memberServcie.getUserByUserId(userId);
        CommonValidUtil.validObjectNull(pUser, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        // 验证商铺存在性
        ShopDto pShop = this.shopServcie.getShopMainOfCacheById(shopId);
        CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        // 商铺是否启用预定
        Integer bookFlag = pShop.getBookFlag();
        CommonValidUtil.validIntEqual(bookFlag, CommonConst.SHOP_BOOK_FLAG_ENABLE, CodeConst.CODE_DISABLE_BOOK,
                CodeConst.MSG_DISABLE_BOOK);

        String resourceName;
        // 各种特定场景的数据校验
        if (StringUtils.equals(resourceType, CommonConst.SHOP_RSR_TYPE_JS))
        {// 丽人预定
            // 验证技师信息必填
            Long bizId = oShopRsrcDto.getBizId();
            if (bizId != null)
            {
                CommonValidUtil
                        .validPositLong(bizId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_JS_ID);
                // 验证技师信息存在
                int flag = this.shopTechnicianService.queryTechnicianExists(bizId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_JS);
                resourceName = this.shopTechnicianService.getTechnicianName(bizId);
                oShopRsrcDto.setBizName(resourceName);
            }
            else
            {
                oShopRsrcDto.setBizName("由商家推荐");
            }
        }
        else if (StringUtils.equals(resourceType, CommonConst.SHOP_RSR_TYPE_CD))
        {// 场地预定
            CommonValidUtil
                    .validObjectNull(endTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_END_TIME);
            // 验证场馆信息必填
            Long bizId = oShopRsrcDto.getBizId();
            CommonValidUtil.validLongNull(bizId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CD_ID);
            CommonValidUtil.validPositLong(bizId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CD_ID);
            // 验证场馆信息存在
            int flag = this.shopRsrcServcie.queryResourceExists(bizId);
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_CD);
            resourceName = this.shopRsrcServcie.getResourceName(bizId);
            oShopRsrcDto.setBizName(resourceName);
        }
        else
        {// 餐饮预约到店
            // 获取商铺预定规则(餐饮预约到店、丽人、场地资源，共用规则校验，目前只对餐饮预约到店做校验，丽人和场馆在前端校验，后端暂时放开)
            DistribTakeoutSettingDto takeoutSetting = this.distribTakeoutSettingService
                    .getDistribTakeoutSetting(shopId, settingType);
            if (takeoutSetting != null)
            {
                // 暂停预定时间范围
                String stopDate = takeoutSetting.getStopDate();
                // 预定日期是否在暂停预定时间范围
                this.validStopDate(reserveResourceDate, stopDate, CodeConst.CODE_IN_STOP_DATE,
                        CodeConst.MSG_BOOK_TIME_IN_STOP_DATE);
                // 接单时间范围
                String deliveryTime = takeoutSetting.getDeliveryTime();
                // 预定时间点是否在接单时间范围内
                this.validDeliveryTime(DateUtils.parse(startTime, DateUtils.TIME_FORMAT2), deliveryTime,
                        CodeConst.CODE_BOOK_TIME_NOTIN_DELIVERY, CodeConst.MSG_BOOK_TIME_NOTIN_DELIVERY);
                // 可预定周期
                String week = takeoutSetting.getWeekDay();
                // 配送时间是否在可预定周期内
                this.validDistribDateIsInStopWeek(reserveResourceDate, week, CodeConst.CODE_BOOK_DATE_NOTIN_WEEK,
                        CodeConst.MSG_BOOK_DATE_NOTIN_WEEK);
            }
            oShopRsrcDto.setBizName(resourceType);
        }
        oShopRsrcDto.setOrderId(orderId);
        oShopRsrcDto.setUserId(pUser.getUserId());
        oShopRsrcDto.setUserName(pUser.getNikeName());
        oShopRsrcDto.setMobile(pUser.getMobile());
        oShopRsrcDto.setStatus(0);
        oShopRsrcDto.setReserveResourceDate(reserveResourceDate);
        oShopRsrcDto.setResourceType(resourceType);
        oShopRsrcDto.setShopId(shopId);
        oShopRsrcDto.setStartTime(startTime);
        oShopRsrcDto.setEndTime(endTime);
        oShopRsrcDto.setCreateTime(new Date());
    }

    private int getSettingType(Integer serverMode) throws Exception
    {
        // 预约规则默认是预约到店的规则
        int settingType = CommonConst.TAKEOUT_SETTINGTYPE_DD;
        if (serverMode != null)
        {
            if (serverMode == 1)
            {
                settingType = CommonConst.TAKEOUT_SETTINGTYPE_SM;
            }
            else if (serverMode == 2)
            {
                settingType = CommonConst.TAKEOUT_SETTINGTYPE_DD;
            }
        }
        return settingType;
    }

    /**
     * 用户使用商铺资源
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/useShopResource", produces = "application/json;charset=UTF-8") @ResponseBody public Object useShopResource(
            HttpServletRequest request)
    {

        String orderId = null;
        try
        {
            logger.info("用户使用商铺资源-start");
            String userId = RequestUtils.getQueryParam(request, "userId");
            String resourceId = RequestUtils.getQueryParam(request, "resourceId");
            orderId = RequestUtils.getQueryParam(request, "orderId");
            CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
            CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR);
            CommonValidUtil
                    .validStrNull(resourceId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_RESOURCE_ID);
            CommonValidUtil
                    .validNumStr(resourceId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_RESOURCE_ID_ERROR);
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);

            OrderDto order = orderServcie.getOrderMainById(orderId);
            CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
            this.orderServcie.useShopResource(Long.parseLong(userId), Long.parseLong(resourceId), orderId);
            // Integer orderSceneType = order.getOrderSceneType();
            // Integer orderType = order.getOrderType();
            // 订单类型为自主下单
            if (1 == order.getOrderSource())
            {
                // 获取商品列表
                List<OrderGoodsDto> ogList = this.orderServcie.getOGoodsListByOrderId(orderId);
                order.setGoods(ogList);
                // 推送订单信息至收银机,下订单接口至推送到店点菜情况
                ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
                PlaceOrderPushMessageTask messageTask = new PlaceOrderPushMessageTask(order);
                threadPoolManager.execute(messageTask);
                // 推用户
                OrderPushUserTask pushTask = new OrderPushUserTask(order);
                threadPoolManager.execute(pushTask);
            }
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_USE_RESOURCE, null);
        }
        catch (JsonMappingException e)
        {
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {

            if (e.getCode() == CodeConst.CODE_RESOURCE_STATUS_NOT_AVALIABLE)
            {
                // 自助下单情况：资源已使用，不能下单，删除订单
                orderServcie.delOrder(orderId);
            }

            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("用户使用商铺资源-系统异常", e);
            throw new APISystemException("用户使用商铺资源-系统异常", e);
        }
    }

    // @RequestMapping(value="/order/reserveShopResource",produces="application/json;charset=UTF-8"
    // ,method=RequestMethod.POST,consumes="application/json")
    // @ResponseBody
    // public Object reserveShopRsrc(@RequestBody OrderShopRsrcDto oShopRsrcDto,
    // HttpServletRequest request){
    // try {
    // logger.info("用户预定商铺资源-start");
    // this.orderServcie.reserveShopRsrc(oShopRsrcDto);
    // return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED,
    // CodeConst.MSG_SUCCEED_RESERV, null);
    // } catch (ServiceException e) {
    // throw new APIBusinessException(e);
    // } catch (Exception e) {
    // e.printStackTrace();
    // logger.error("用户预定商铺资源-系统异常", e);
    // throw new APISystemException("用户预定商铺资源-系统异常", e);
    // }
    // }

    // public static void main(String[] args) throws Exception {
    // Map<String,Object> map = new HashMap<String, Object>();
    // map.put("id", 1);
    // map.put("name","ljp");
    //
    // List<Map<String,Object>> gl = new ArrayList<Map<String,Object>>();
    //
    // Map<String,Object> map2 = new HashMap<String, Object>();
    // map2.put("gid", 1);
    // map2.put("name", "333");
    // gl.add(map2);
    // map.put("gList", gl);
    // CodeConst.MSG_SUCCEED_ORDERDETAIL,map));
    // }

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/myorder/cancelOrder") @ResponseBody public ResultDto cancelOrder(
            @ModelAttribute(value = "order") OrderDto order, HttpServletRequest request)
    {
        try
        {
            long start = System.currentTimeMillis();
            logger.info("取消订单-start");
            CommonValidUtil.validObjectNull(order.getOrderId(), CodeConst.CODE_PARAMETER_NOT_NULL, "orderId不能为空");

            Integer status = orderServcie.cancelOrder(order);
            // TODO 消费者申请退订
            order = orderServcie.getOrderMainById(order.getOrderId());
            Long userId = order.getUserId();
            Long shopId = order.getShopId();
            // 推送给收银机
            StringBuilder content = new StringBuilder();
            content.append("{");
            content.append("\"shopId\":" + order.getShopId() + ",");
            content.append("\"action\":\"cashOrder\",");
            content.append("\"data\":{\"id\":\"" + order.getOrderId() + "\",\"orderStatus\":" + order.getOrderStatus()
                    + ",\"payStatus\":" + order.getPayStatus() + "}");
            content.append("}");
            PushDto push = new PushDto();
            push.setAction("cashOrder");
            push.setContent(content.toString());
            push.setUserId(userId);
            push.setShopId(order.getShopId());
            pushService.pushInfoToShop2(push);
            // 推送信息给一点管家
            if (CommonConst.ORDER_STS_YTD == status)
            {
                UserDto userCatch = memberServcie.getUserByUserId(userId);
                commonService.pushShopUserMsg("order", "顾客" + userCatch.getMobile() + "已经取消订单！", shopId);

            }
            if (CommonConst.ORDER_STS_TDZ == status)
            {
                commonService.pushShopUserMsg("order", "您有一笔退订订单需要处理", shopId);
            }
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "取消订单成功", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("取消订单-系统异常", e);
            throw new APISystemException("取消订单-系统异常", e);
        }
    }

    /**
     * 删除订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/myorder/deleteOrder", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto deleteOrder(
            HttpServletRequest request)
    {
        try
        {
            long start = System.currentTimeMillis();
            logger.info("删除订单-start");
            String userId = RequestUtils.getQueryParam(request, "userId");
            String orderId = RequestUtils.getQueryParam(request, "orderId");

            // 校验orderId
            CommonValidUtil.validObjectNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, "orderId不能为空");

            // 校验userId
            CommonValidUtil.validObjectNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
            CommonValidUtil.validNumStr(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);

            orderServcie.deleteOrder(userId, orderId);
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "订单删除成功", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("删除订单-系统异常", e);
            throw new APISystemException("删除订单-系统·异常", e);
        }
    }

    /**
     * 查询商铺预定资源
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/queryReserveShopResource", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto queryReserveShopResource(
            HttpServletRequest request)
    {
        try
        {
            logger.info("查询商铺预定资源列表-start");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            String queryDate = RequestUtils.getQueryParam(request, "queryDate");
            String serverMode = RequestUtils.getQueryParam(request, "serverMode");// 服务方式：1(上门)，2(到店)

            // 检验shopId是否为空
            CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 校验queryDate是否为空
            CommonValidUtil.validStrNull(queryDate, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_DATE);
            CommonValidUtil
                    .validDateStr(queryDate, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_DATE);
            if (null == serverMode)
            {
                serverMode = "2";
            }
            List<Map> osrs = this.orderServcie
                    .getListShopResGroup(Long.parseLong(shopId), queryDate, Integer.parseInt(serverMode));

            MessageListDto msgList = new MessageListDto();
            msgList.setrCount(osrs.size());
            msgList.setLst(osrs);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺预定资源列表成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("查询商铺预定资源列表-系统异常", e);
            throw new APISystemException("查询商铺预定资源列表-系统异常", e);
        }
    }

    @RequestMapping(value = "/addGoods", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public ResultDto addGoods(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {

            Goods goods = (Goods) JacksonUtil.postJsonToObj(entity, Goods.class);

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "新增商品成功", null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new APISystemException("新增商品异常", e);
        }
    }

    /**
     * 预定订单
     *
     * @param item
     * @param request
     * @return
     */
    @RequestMapping(value = "/syncBookList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public ResultDto syncBookList(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("预定信息提交-start");
            OrderShopResourceGoodDto orderShopResourceGoodDto = (OrderShopResourceGoodDto) JacksonUtil
                    .postJsonToObj(entity, OrderShopResourceGoodDto.class);
            // json数据对象
            DataJsonDto data = orderShopResourceGoodDto.getData();
            String mobile = data.getMobile();
            // 检验手机是否为空
            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
            // 根据手机号码查询用户信息
            UserDto user = memberServcie.getUserByMobile(mobile);
            // 商铺id
            Long shopId = orderShopResourceGoodDto.getShopId();
            // 检验shopId是否为空
            CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
            ShopDto shop = shopServcie.getShopById(shopId);
            // 校验token和店铺是否存在
            collectService.queryShopAndTokenExists(shopId, orderShopResourceGoodDto.getToken());
            // 商品信息列表
            List<BookInfo> listBookInfo = data.getBookInfo();
            List<OrderGoodsDto> listOrderGoods = new ArrayList<OrderGoodsDto>();

            // 预付金额
            Double prepayMoney = data.getAdvance();
            OrderDto order = new OrderDto();
            if (null != user)
            {
                order.setUserId(user.getUserId());
            }

            String serviceTimeFrom = data.getEatTimeFrom();
            if (serviceTimeFrom != null)
            {
                order.setServiceTimeFrom(DateUtils.parse(serviceTimeFrom));
            }

            String serviceTimeTo = data.getEatTimeTo();
            if (serviceTimeTo != null)
            {
                order.setServiceTimeTo(DateUtils.parse(serviceTimeTo));
            }
            // 配送方式
            order.setDistributionType(0);
            order.setPrepayMoney(prepayMoney);
            order.setShopName(shop.getShopName());
            // 是否外卖
            if (data.getIsWm())
            {
                order.setOrderServiceType(1);
            }
            else
            {
                order.setOrderServiceType(0);
            }

            // 默认网上支付
            order.setPayTimeType(0);
            order.setShopId(shopId);
            // 默认全额
            order.setOrderType(0);
            // 订单状态默认为预定
            order.setOrderStatus(0);
            // 总金额
            Double orderTotalPrice = 0.0;
            String orderId = data.getId().toString();
            if (CollectionUtils.isNotEmpty(listBookInfo))
            {
                // 以下是goodList对象赋值
                for (BookInfo bookInfo : listBookInfo)
                {
                    Double goodNum = bookInfo.getNum();
                    if (null == goodNum || 0 == goodNum)
                    {
                        goodNum = 1.0;
                    }
                    // 商品订单信息
                    OrderGoodsDto orderGoods = new OrderGoodsDto();
                    GoodsDto goods = goodsServcie.getGoodsById(bookInfo.getDishId());
                    orderGoods.setShopId(shopId);
                    orderGoods.setGoodsId(goods.getGoodsId());
                    orderGoods.setGoodsNumber(goodNum.doubleValue());
                    // 订单id
                    orderGoods.setOrderId(orderId);
                    listOrderGoods.add(orderGoods);
                    // 商品数量
                    Double num = bookInfo.getNum();
                    // 单价
                    float unitPrice = goods.getUnitPrice();
                    // 总金额
                    orderTotalPrice += num * unitPrice;
                }
            }
            // TODO 总金额
            order.setOrderTotalPrice(orderTotalPrice);
            order.setGoods(listOrderGoods);
            order.setOrderId(orderId);
            // 配送时间
            // order.setDistributionTime(DateUtils.parse(DateUtils.ENDTIME_FORMAT,
            // time) );
            if (order.getGoodsPriceBeforeDiscount() == null)
            {
                order.setGoodsPriceBeforeDiscount(0d);
            }
            // double为空，给出默认值
            if (!NumberUtil.isDouble(order.getLogisticsPrice() + ""))
            {
                order.setLogisticsPrice(0D);
            }
            order.setOrderTime(new Date());
            // 预定场景orderSceneType
            Integer orderSceneType = data.getOrderSceneType();
            order.setOrderSceneType(orderSceneType);
            // 支付状态
            Integer payStatus = data.getPayStatus();
            order.setPayStatus(payStatus);
            order.setOrderTitle("订桌");
            // 备注（辣、咸...）
            order.setUserRemark(data.getUserRemark());
            // 手机
            order.setMobile(mobile);
            order.setClientLastTime(data.getClientLastTime());
            order.setClientSystemType(ClientSystemTypeEnum.CASHIER.getValue());
            // 下订单预定资源
            if (user != null)
            {// 会员
                orderServcie.syncBookList(order, orderShopResourceGoodDto, user, 3);
            }
            else
            {// 非会员
                Integer num = orderServcie.queryOrderExists(orderId);
                CommonValidUtil.validIsZero(num, CodeConst.CODE_EXISTS_ORDERID, CodeConst.MSG_EXISTS_ORDERID);
                orderServcie.syncXorder(orderShopResourceGoodDto, order);
            }
            // 1、推送信息 下订单接口已经存在推送信息给用户
            OrderPushUserTask pushTask = new OrderPushUserTask(order);
            // 2.保存订单日志
            OrderLogDto orderLog = new OrderLogDto();
            orderLog.setOrderId(orderId);
            orderLog.setPayStatus(payStatus);
            orderLog.setOrderStatus(0);
            orderLog.setLastUpdateTime(new Date());
            orderLog.setRemark("收银机预订单");
            OrderLogTask ot = new OrderLogTask(orderLog);
            ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
            threadPoolManager.execute(ot);
            if (null != user)
            {
                threadPoolManager.execute(pushTask);
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_SYNCBOOK, null);
        }
        catch (JsonMappingException e)
        {
            e.printStackTrace();
            throw new APIBusinessException(CodeConst.CODE_JSON_ERROR, CodeConst.MSG_JSON_ERROR);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("预定信息提交-系统异常", e);
            throw new APISystemException("预定信息提交-系统异常", e);
        }
    }

    /**
     * 获取订单评论列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/comment/getOrderComments", produces = "application/json;charset=UTF-8") @ResponseBody public String getOrderComments(
            HttpServletRequest request)
    {
        try
        {
            logger.info("获取订单评论列表-start");
            // orderId
            String orderId = RequestUtils.getQueryParam(request, "orderId");
            // 当前页
            String pageNo = RequestUtils.getQueryParam(request, "pNo");
            // 页大小
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            int rCount = memberServcie.getOrderCommentCountById(orderId);
            // 查询列表
            List<Map<String, Object>> lst = memberServcie
                    .getOrderComments(orderId, PageModel.handPageNo(pageNo), PageModel.handPageSize(pageSize), null);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pNo", pageNo);
            map.put("rCount", rCount);
            map.put("lst", lst);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_ORDER_COMMENT, map,
                    DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            this.logger.error("获取订单评论列表失败-系统异常", e);
            throw new APISystemException("获取订单评论列表失败-系统异常", e);
        }
    }

    /**
     * 获取订单评论列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getOrder8List", produces = "application/json;charset=UTF-8") @ResponseBody public String getOrder8List(
            HttpServletRequest request)
    {
        try
        {
            logger.info("获取用户自助下单列表-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String token = RequestUtils.getQueryParam(request, "token");
            // 商铺设备token验证
            CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
            CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
            CommonValidUtil.validNumStr(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "shopId格式错误");
            Long shopId = Long.parseLong(shopIdStr);
            // 校验商铺及商铺设备token是否存在
            collectService.queryShopAndTokenExists(shopId, token);
            ShopDto shop = shopServcie.getShopMainOfCacheById(shopId);
            if (null == shop)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }
            // 查询列表
            List<Map<String, Object>> lst = orderServcie.getOrder8List(shopId);

            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取用户自助下单列表成功！", lst, DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            this.logger.error("获取用户自助下单列表失败-系统异常", e);
            throw new APISystemException("获取用户自助下单列表列表失败-系统异常", e);
        }
    }

    /**
     * 请求一点传奇支付(收银机)，下订单
     *
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = { "/onlinePay", "/session/order/onlinePay", "/service/order/onlinePay",
            "/token/order/onlinePay" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto onlinePay(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        try
        {
            logger.info("请求一点传奇支付-start");
            OnLinePayDto onLinePayDto = (OnLinePayDto) JacksonUtil
                    .postJsonToObj(entity, OnLinePayDto.class, DateUtils.DATETIME_FORMAT);
            Map<String, Object> map = new HashMap<String, Object>();
            String mobile = onLinePayDto.getMobile();

            CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
            CommonValidUtil
                    .validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
            UserDto user = memberServcie.getUserByMobile(mobile);
            CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            String requestPath = request.getRequestURI();
            if (CommonConst.ONLINEPAY_URL.equals(requestPath))
            {
                collectService.queryShopAndTokenExists(onLinePayDto.getShopId(), onLinePayDto.getToken());
            }
            // 确认类型及短信验证码校验
            Integer confirmType = onLinePayDto.getConfirmType();
            if (confirmType != null && confirmType == CommonConst.ONLINE_PAY_CONFIRM_TYPE_DX)
            {
                String veriCode = onLinePayDto.getVeriCode();
                CommonValidUtil
                        .validStrNull(veriCode, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERI_CODE);
                boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode);
                if (!flag)
                {
                    throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
                }
            }

            DataDto data = onLinePayDto.getData();
            String orderId = data.getId();
            int status = CommonConst.ORDER_STS_YKD; // 已开单 默认
            OrderDto order = orderServcie.getOrderMainById(data.getId());
            if (null == order)
            {
                order = new OrderDto();
                order.setUserId(user.getUserId());
                order.setOrderId(orderId);
                order.setOrderTime(data.getCreateTime());
                order.setPayStatus(0);
                order.setGoodsPriceBeforeDiscount(data.getTotal());// 折前商品总价
                order.setGoodsPrice(data.getTotal() * data.getDiscount());// 商品总价（折后)计算有问题、下单会重新计算
                order.setLogisticsPrice(data.getOutfee() == null ? 0 : data.getOutfee());// 费用
                order.setOrderTotalPrice(data.getTotal());
                order.setSettlePrice(data.getMaling());
                order.setMaling(data.getMaling());
                order.setPrepayMoney(data.getAdvance());// 预付金额s
                order.setPayTimeType(0);// 支付时间的类型：网上支付-0，货到付款-1
                order.setOrderSceneType(data.getOrderSceneType() == null ? 1 : data
                        .getOrderSceneType());// 订单场景分类：0（预定单），1（到店点菜订单），2（外卖订单），3（服务订单），4（商品订单）
                order.setOrderType(1); // 全额订单
                order.setShopId(onLinePayDto.getShopId());
                order.setIsMaling(data.getIsMaling());
                Map result = getOrderGoodsList(onLinePayDto, data);
                order.setGoodsPrice((Double) result.get("goodsPrice"));
                order.setGoods((List<OrderGoodsDto>) result.get("listOrderGood"));
                order.setOrderDiscount(data.getAdditionalDiscount()); // 折上折
                order.setCashierId(data.getCashierId());
                order.setOrderStatus(status);
                order.setClientSystemType(ClientSystemTypeEnum.CASHIER.getValue());
                order.setOrderChannelType(2);
                order.setMobile(mobile);
                order.setClientLastTime(data.getClientLastTime());
                order.setMemberDiscount(data.getDiscount());// 会员折扣
                order.setBusinessAreaActivityId(data.getBusinessAreaActivityId());
                this.orderServcie.placeOrder(order, 2);
            }
            else
            {
                Integer orderStatus = order.getOrderStatus();
                if (null == orderStatus || (CommonConst.ORDER_STS_YJZ == orderStatus
                        && CommonConst.REVERSE_SETTLE_FLAG != order.getSettleFlag().intValue())
                        || CommonConst.ORDER_STS_TDZ == orderStatus || CommonConst.ORDER_STS_YTD == orderStatus)
                {
                    // 订单状态为不可支付状态
                    throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR, CodeConst.MSG_ORDER_STATUS_ERROR);
                }
                // 更新结算价为：抹零价
                if (null != data.getMaling())
                {
                    order.setLogisticsPrice(data.getOutfee());
                    order.setSettlePrice(data.getMaling());
                    Map result = getOrderGoodsList(onLinePayDto, data);
                    List<OrderGoodsDto> listOrderGood = (List<OrderGoodsDto>) result.get("listOrderGood");
                    order.setGoods(listOrderGood);
                    order.setGoodsPrice((Double) result.get("goodsPrice"));
                    order.setGoodsPriceBeforeDiscount(data.getTotal());
                    order.setMaling(data.getMaling());
                    order.setOrderDiscount(data.getAdditionalDiscount()); // 折上折
                    order.setCashierId(data.getCashierId());
                    order.setMemberDiscount(data.getDiscount());// 会员折扣
                    order.setMobile(mobile);
                    order.setClientLastTime(data.getClientLastTime());
                    order.setBusinessAreaActivityId(data.getBusinessAreaActivityId());
                    if (order.getUserId() == null)
                    {
                        order.setUserId(user.getUserId());
                    }
                    order.setIsMember(CommonConst.USER_IS_MEMBER);
                    orderServcie.updateOrderMaling(order);
                }
            }

            if (confirmType != null && confirmType == CommonConst.ONLINE_PAY_CONFIRM_TYPE_DX)
            {
                // 生成支付记录及短信方式推送支付记录
                Double redPacketMoney = smsPay(order, mobile, user);
                map.put("sendRedPacketMoney", redPacketMoney);
                // 统计销量
                orderServcie.updateGoodsAndShopSoldNum(orderId);
                //插入反结账订单商品线上支付账单，TODO 坑爹啊，别什么鬼都往control写
                payServcie.insertReverseShopBill(order);
            }
            else
            {
                // 用户app消息推送
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", "onlinePayByLegender");
                jsonObject.put("orderId", order.getOrderId());
                jsonObject.put("shopId", onLinePayDto.getShopId());
                jsonObject.put("amount", onLinePayDto.getData().getPayable());
                commonService.pushUserMsg(jsonObject, user.getUserId(), CommonConst.USER_TYPE_ZREO);
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_CALL, map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("请求一点传奇支付-系统异常", e);
            throw new APISystemException("请求一点传奇支付-系统异常", e);
        }
    }

    /**
     * 短信确认支付生成支付记录及短信推送
     *
     * @param order
     * @return
     * @throws Exception
     */
    private Double smsPay(OrderDto order, String mobile, UserDto user) throws Exception
    {
        Double sendMoney = 0D;
        String orderId = order.getOrderId();
        Long userId = user.getUserId();
        CommonValidUtil.validObjectNull(userId, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_PAY_BY_SMS);
        Integer orderPayType = 0;
        // 订单结算金额
        Double settlePrice = order.getSettlePrice();
        // 已经支付的金额
        BigDecimal payAmount = packetService.queryOrderPayAmount(orderId, orderPayType);
        // 需要支付的金额
        Double needPayAmount = NumberUtil.sub(settlePrice, payAmount.doubleValue());
        if (needPayAmount > 0)
        {
            // 获取验证码时有校验用户账户状态，这里暂时不校验
            UserAccountDto accountDto = this.userAccountService.getAccountMoney(userId);
            // 代金券（消费卡）可用余额
            Double couponBalance = userCashCouponService.getUserCashCouponBalance(userId);
            // 走核心支付流程
            boolean isSettleFlag = this.payServcie.payBySms(order, needPayAmount, accountDto, couponBalance, user);
            // 获取传奇宝账户余额
            UserAccountDto account = this.userAccountService.getAccountMoney(userId);
            if (isSettleFlag)
            {
                sendMoney = packetService.sendRedPacketToUser(order);
                if (sendMoney != 0)
                {
                    Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
                    if (orderRealSettlePrice < 0)
                    {
                        orderRealSettlePrice = 0D;
                    }
                    orderServcie.updateOrderRealSettlePrice(orderId, orderRealSettlePrice, sendMoney);
                }
                // 结算
                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, orderPayType);
            }
            // 短信推送用户支付成功信息
            Double balanceAmount = account.getAmount();
            Double balanceBalance = userCashCouponService.getUserCashCouponBalance(userId); // 支付完了剩余的代金券余额
            Double totelBalanceAmount = NumberUtil
                    .sub((balanceBalance + balanceAmount), account.getFreezeAmount()); // 剩余总额（减去冻结资金加上可用代金券余额）

            final SmsReplaceContent src = new SmsReplaceContent();
            src.setAcountAmount(NumberUtil.formatDouble(totelBalanceAmount, 2)); // 账户余额
            src.setAmount(NumberUtil.formatDouble(needPayAmount, 2)); // 消费金额
            src.setMobile(mobile);
            src.setUsage(CommonConst.SMS_PAY_SUCC);
            new Thread()
            {
                @Override public void run()
                {
                    try
                    {
                        sendSmsService.sendSmsMobileCode(src);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        logger.info("收银机短信支付-发送支付成功短信失败！");
                    }
                }
            }.start();
        }
        return sendMoney;
    }

    /**
     * 构造商品列表
     *
     * @param onLinePayDto
     * @param data
     * @return
     * @throws Exception
     */
    public Map getOrderGoodsList(OnLinePayDto onLinePayDto, DataDto data) throws Exception
    {
        Map result = new HashMap();
        List<OrderInfoDto> list = data.getOrderInfo();
        List<OrderGoodsDto> listOrderGood = new ArrayList<OrderGoodsDto>();
        OrderGoodsDto orderGoodsDto = null;
        OrderInfoDto orderInfoDto = null;
        double goodsPrice = 0.0;
        for (int i = 0, len = list.size(); i < len; i++)
        {
            orderInfoDto = list.get(i);
            orderGoodsDto = new OrderGoodsDto();
            GoodsDto goods = goodsServcie.getGoodsByIds(onLinePayDto.getShopId(), orderInfoDto.getDishId());
            if (goods == null)
            {
                continue;
            }
            orderGoodsDto.setShopId(onLinePayDto.getShopId());
            orderGoodsDto.setGoodsId(orderInfoDto.getDishId());
            orderGoodsDto.setGoodsNumber(orderInfoDto.getNum());
            orderGoodsDto.setStandardPrice(orderInfoDto.getPrice()); // 订单参与会员折扣前的金额
            orderGoodsDto.setGoodsIndex(i);
            orderGoodsDto.setUserRemark(orderInfoDto.getDishRemark());
            orderGoodsDto.setBillerId(orderInfoDto.getBillerId());
            orderGoodsDto.setOrderGoodsDiscount(orderInfoDto.getOrderGoodsDiscount());
            orderGoodsDto.setGoodsName(goods.getGoodsName());
            orderGoodsDto.setSpecsDesc(goods.getSpecsDesc());
            goodsPrice += orderInfoDto.getPromoPrice() * orderInfoDto.getNum();
            listOrderGood.add(orderGoodsDto);
        }
        result.put("listOrderGood", listOrderGood);
        result.put("goodsPrice", goodsPrice);
        return result;
    }

    /**
     * 获取订单编号
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay/getOrderId", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getOrderId(
            HttpServletRequest request) throws Exception
    {
        try
        {
            logger.info("获取订单编号-start");
            String userId = RequestUtils.getQueryParam(request, "userId");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil
                    .validStrLongFormat(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
            CommonValidUtil
                    .validStrLongFormat(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 用户存在性
            Long id = this.memberServcie.authenUserById(Long.valueOf(userId));
            CommonValidUtil.validPositLong(id, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            Long spId = Long.valueOf(shopId);
            int num = this.shopServcie.queryShopExists(spId);
            CommonValidUtil.validPositInt(num, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            // 生成订单号
            String orderId = FieldGenerateUtil.generateOrderId(spId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", orderId);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GEN_ORDERID, map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单编号-系统异常", e);
            throw new APISystemException("获取订单编号-系统异常", e);
        }

    }

    @RequestMapping(value = "/pay/getOrderId2", produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getOrderId2(
            HttpServletRequest request) throws Exception
    {
        try
        {
            logger.info("获取订单编号-start");
            String userId = RequestUtils.getQueryParam(request, "userId");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil
                    .validStrLongFormat(userId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
            CommonValidUtil
                    .validStrLongFormat(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            Long spId = Long.valueOf(shopId);
            String orderId = FieldGenerateUtil.generateOrderId(spId);
            this.orderServcie.saveTestOrderId(orderId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", orderId);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GEN_ORDERID, map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单编号-系统异常", e);
            throw new APISystemException("获取订单编号-系统异常", e);
        }

    }

    /**
     * 支付宝扫码支付
     *
     * @Title: aliScanPayPreOrder @param @param request @param @param
     * hashMap @param @return @return ResultDto 返回类型 @throws
     */
    @RequestMapping(value = "aliScanPreOrder", produces = "application/json;charset=UTF-8") public @ResponseBody ResultDto aliScanPayPreOrder(
            HttpServletRequest request, @RequestBody Map<String, String> hashMap)
    {
        String out_trade_no = hashMap.get("out_order_no");// 订单号
        String total_amount = hashMap.get("total_amount");// 支付总金额
        String subject = hashMap.get("subject");// 标题
        String goodsDetail = hashMap.get("goods_detail");// 商品详情
        String cashierIdStr = hashMap.get("cashierId");// 收银员id
        String orderTotalPrice = hashMap.get("");
        String settlePrice = hashMap.get("settlePrice");// 参与结算的总价
        String result = ToAlipayQrTradePay.qrPay(out_trade_no, total_amount, subject);// 支付宝预下单返回的结果
        JSONObject resultObj = JSONObject.fromObject(result);
        if ("0".equals(resultObj.get("code")))
        {
            OrderDto orderDto = new OrderDto();
            if (!com.alipay.api.internal.util.StringUtils.isEmpty(cashierIdStr))
            {
                orderDto.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);// 支付状态为未支付
            }
        }
        return null;
    }

    /**
     * 支付宝扫码支付异步通知
     *
     * @Title: aliScanPayNotify @param @param request @param @return @return
     * ResultDto 返回类型 @throws
     */
    @RequestMapping(value = "aliScanPayNotify") public @ResponseBody ResultDto aliScanPayNotify(
            HttpServletRequest request)
    {
        try
        {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); )
            {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++)
                {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"),
                // "gbk");
                params.put(name, valueStr);
            }
            logger.error("开始验证支付宝签名");
            if (AlipayNotify.verify(params))
            {// 验证签名
                logger.error("验证支付宝签名通过");
                String orderNo = RequestUtils.getQueryParam(request, "out_trade_no");// 数据库中的订单号
                String notify_time = RequestUtils.getQueryParam(request, "notify_time");// 支付宝异步通知时间
                String notify_type = RequestUtils.getQueryParam(request, "notify_type");// 异步通知类型
                String notify_id = RequestUtils.getQueryParam(request, "notify_id");// 异步通知id
                String sign_type = RequestUtils.getQueryParam(request, "sign_type");
                String sign = RequestUtils.getQueryParam(request, "sign");
                String notify_action_type = RequestUtils.getQueryParam(request, "notify_action_type");
                String trade_no = RequestUtils.getQueryParam(request, "trade_no");
                String app_id = RequestUtils.getQueryParam(request, "app_id");
                String open_id = RequestUtils.getQueryParam(request, "open_id");
                String buyer_logon_id = RequestUtils.getQueryParam(request, "buyer_logon_id");
                String seller_id = RequestUtils.getQueryParam(request, "seller_id");
                String seller_email = RequestUtils.getQueryParam(request, "seller_email");
                String trade_status = RequestUtils.getQueryParam(request, "trade_status");
                String total_amount = RequestUtils.getQueryParam(request, "total_amount");
                String receipt_amount = RequestUtils.getQueryParam(request, "receipt_amount");
                String buyer_pay_amount = RequestUtils.getQueryParam(request, "buyer_pay_amount");// 用户支付金额
                String subject = RequestUtils.getQueryParam(request, "subject");// 订单标题
                // OrderDto
                // orderDto=(OrderDto)DataCacheApi.getObject(CommonConst.ALISCANPAY_KEY+orderNo);
                OrderDto orderDtoEntity = orderServcie.getOrderMainById(orderNo);
                logger.error("用户实付金额:" + buyer_pay_amount + " 订单总金额:" + total_amount + " 订单编号:" + orderNo);
                double buyerPayAmount = Double.parseDouble(buyer_pay_amount);
                Long shopId = null;
                JSONObject pushContent = new JSONObject();
                pushContent.put("action", "order");
                if (orderDtoEntity != null)
                {
                    synchronized (this)
                    {
                        String orderEntityId = orderDtoEntity.getOrderId();
                        pushContent.put("orderType", "1");
                        pushContent.put("orderId", orderDtoEntity.getOrderId());
                        if (orderDtoEntity.getOrderStatus() == CommonConst.ORDER_STS_YJZ)
                        {
                            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GEN_ORDERID,
                                    new HashMap<String, Object>());
                        }
                        double paidMoney = 0;
                        if (CommonConst.ORDER_CHANNEL_POS_CQB == orderDtoEntity.getOrderChannelType())
                        {// 订单下单渠道
                            pushContent.put("action", "orderPayFinish");
                            pushContent.put("collectOrder", true);
                            Integer maxPayIndex = payDao.getMaxPayIndex(orderEntityId);
                            double dataBasePaidMoney = payDao.getSumPayAmount(orderEntityId, null);// 已支付金额
                            paidMoney = dataBasePaidMoney;
                            if (maxPayIndex == null)
                            {
                                maxPayIndex = 0;
                            }
                            maxPayIndex++;
                            OrderServiceImpl.initPushContent(buyerPayAmount, orderEntityId, CommonConst.PAY_TYPE_3RD,
                                    maxPayIndex, pushContent);
                        }
                        // 插入支付记录ORDER_PAY
                        PayDto payDto = new PayDto();
                        payDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                        payDto.setOrderId(orderDtoEntity.getOrderId());
                        payDto.setPayAmount(buyerPayAmount);
                        payDto.setPayeeType(CommonConst.PAYEE_TYPE_PLATFORM);
                        payDto.setPayType(CommonConst.PAY_TYPE_3RD);
                        payDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                        payDto.setShopId(orderDtoEntity.getShopId());
                        Transaction3rdDto transactionDto = new Transaction3rdDto();
                        transactionDto.setOrderId(orderDtoEntity.getOrderId());
                        transactionDto.setUserId(orderDtoEntity.getUserId());
                        transactionDto.setOrderPayType(0);// 单个订单支付
                        transactionDto.setPayAmount(buyerPayAmount);// 支付金额
                        transactionDto.setStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);// 支付成功
                        transactionDto.setRdOrgName("支付宝");
                        transactionDto.setRdTransactionId(trade_no);// 支付宝交易流水号
                        transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_ALISCAN);
                        transactionDto.setRdNotifyTime(DateUtils.parse(notify_time, DateUtils.DATETIME_FORMAT));
                        logger.error("支付宝扫码支付会员订单回调处理");
                        double settlePrice = orderDtoEntity.getSettlePrice();
                        paidMoney = NumberUtil.fmtDouble(paidMoney + buyerPayAmount, 2);
                        if (paidMoney >= settlePrice)
                        {
                            orderServcie.payFinishDeal(transactionDto, payDto, orderNo);
                            pushContent.put("status", CommonConst.ORDER_STS_YJZ);
                            shopId = orderDtoEntity.getShopId();
                            if (shopId != null)
                            {// 推送给用户
                                push(shopId, pushContent);
                            }
                        }

                    }
                }
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_GEN_ORDERID,
                    new HashMap<String, Object>());
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单编号-系统异常", e);
            throw new APISystemException("获取订单编号-系统异常", e);
        }
    }

    /**
     * 推送给用户
     *
     * @Title: push @param @param shopId @param @param
     * pushContent @param @throws Exception @return void 返回类型 @throws
     */
    private void push(Long shopId, JSONObject pushContent) throws Exception
    {
        ShopDto shopDto = shopServcie.getShopById(shopId);
        if (shopDto != null)
        {
            Long userId = shopDto.getPrincipalId();
            PushDto pushDto = new PushDto();
            pushDto.setUserId(userId);
            pushDto.setAction("order");
            pushDto.setContent(pushContent.toString());
            pushDto.setUserId(shopDto.getPrincipalId());
            pushDto.setTitle("订单支付通知");
            if (pushContent.has("collectOrder"))
            {
                pushDto.setShopId(shopId);
                this.pushService.pushInfoToShop2(pushDto);
            }
            else
            {
                List<PushUserTableDto> pushUserTables = pushUserTableDao
                        .getPushUserByUserId(userId, CommonConst.USER_TYPE_TEN);
                String platForm = null;
                for (PushUserTableDto pushUserTable : pushUserTables)
                {
                    if (pushContent.has("collectOrder"))
                    {
                        platForm = CommonConst.GOODS;
                    }
                    else
                    {
                        if (StringUtils.containsIgnoreCase(pushUserTable.getOsInfo(), "ios"))
                        {// ios平台
                            platForm = "ios";
                        }
                        else
                        {// android平台
                            platForm = "and";
                        }
                    }
                    pushDto.setPlatForm(platForm);
                    pushDto.setRegId(pushUserTable.getRegId());
                    pushService.pushInfoToUser(pushDto, CommonConst.USER_TYPE_TEN);
                }
            }
        }
    }

    /**
     * 微信扫码支付
     *
     * @Title: wxScanPayNotify @param @param request @param @return @return
     * String 返回类型 @throws
     */
    @RequestMapping(value = "wxScanPayNotify") public void wxScanPayNotify(HttpServletRequest request,
            HttpServletResponse response)
    {
        String resXml = "";
        try
        {
            String inputLine;
            String notityXml = "";
            try
            {
                while ((inputLine = request.getReader().readLine()) != null)
                {
                    notityXml += inputLine;
                }
                request.getReader().close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            logger.error("微信扫码支付返回的" + notityXml);// 返回的是xml格式
            Map m = parseXmlToList2(notityXml);// 将解析的xml转换为map
            final WxPayResult wpr = new WxPayResult();
            wpr.setAppid(WXScanUtil.getMapVal(m, "appid"));
            wpr.setBankType(WXScanUtil.getMapVal(m, "bank_type"));
            wpr.setCashFee(WXScanUtil.getMapVal(m, "cash_fee"));
            wpr.setFeeType(WXScanUtil.getMapVal(m, "fee_type"));
            wpr.setIsSubscribe(WXScanUtil.getMapVal(m, "is_subscribe"));
            wpr.setMchId(WXScanUtil.getMapVal(m, "mch_id"));
            wpr.setNonceStr(WXScanUtil.getMapVal(m, "nonce_str"));
            wpr.setOpenid(WXScanUtil.getMapVal(m, "openid"));
            String outTradeNo = WXScanUtil.getMapVal(m, "out_trade_no");
            if (outTradeNo.length() == 32)
            {
                outTradeNo = outTradeNo.substring(0, 29);
            }
            wpr.setOutTradeNo(outTradeNo);
            wpr.setResultCode(WXScanUtil.getMapVal(m, "result_code"));
            wpr.setReturnCode(WXScanUtil.getMapVal(m, "return_code"));
            wpr.setSign(WXScanUtil.getMapVal(m, "sign"));
            wpr.setTimeEnd(WXScanUtil.getMapVal(m, "time_end"));
            wpr.setTotalFee(WXScanUtil.getMapVal(m, "total_fee"));
            wpr.setTradeType(WXScanUtil.getMapVal(m, "trade_type"));
            wpr.setTransactionId(WXScanUtil.getMapVal(m, "transaction_id"));
            if ("SUCCESS".equals(wpr.getResultCode()))
            {
                // 支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
            else
            {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        orderServcie.dealWxscanNotify(wpr);
                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();
                    }
                }
            }.start();

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单编号-系统异常", e);
            throw new APISystemException("获取订单编号-系统异常", e);
        }
        finally
        {
            try
            {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            }
            catch (Exception e)
            {
                logger.error("给微信返回报文过程中产生了异常");
            }
        }
    }

    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     * @author ex_yangxiaoyi
     * @see
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) private static Map parseXmlToList2(String xml)
    {
        Map retMap = new HashMap();
        try
        {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0)
            {
                for (Element element : es)
                {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retMap;
    }

    static long sss = 100000;

    /*
     * private static synchronized void incr() { sss++; }
     */

    /**
     * 收银接口-获取订单详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/order/getOrderDetail4CR", "/session/order/getOrderDetail4CR",
            "/token/order/getOrderDetail4CR" }, produces = "application/json;charset=utf-8") @ResponseBody public String getOrderDetail4CR(
            HttpServletRequest request) throws Exception
    {
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String orderId = RequestUtils.getQueryParam(request, "orderId");

        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
        Long shopId = CommonValidUtil
                .validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);

        // 商铺设备token验证
        // CommonValidUtil.validStrNull(token,
        // CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
        // 商铺设备token验证
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        Map model = collectService.getOrderDetail4CR(shopId, orderId);
        Map<String, String> cashierNameMap = collectService.queryEmployee(orderId);
        //             20160510添加cashierUsername返回字段
        if (null == model.get("cashierUsername") || model.get("cashierUsername").equals("老板"))
        {
            UserDto user = shopServcie.getShopPrincipalInfoByShopId(shopId);
            if (user != null)
            {
                model.put("caisherMobile", user.getMobile());
            }
            else
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "指定的用户信息不存在");
            }
        }
        else
        {
            model.put("caisherMobile", cashierNameMap == null ? null : cashierNameMap.get("caisherMobile"));
        }
            /*20160704添加caisherMobile返回字段*/
         /*if(cashierNameMap!=null){
             model.put("caisherMobile", cashierNameMap.get("caisherMobile"));
         }else{
        	 UserDto user = shopServcie.getShopPrincipalInfoByShopId(shopId);
        	 if(user != null){
        		 model.put("caisherMobile", user.getMobile()); 
        	 }else{
        		 throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"指定的用户信息不存在"); 
        	 }
         }*/

        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取订单详情成功！", model, DateUtils.DATETIME_FORMAT);
    }

    /**
     * PCO11:分页获取订单详情列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/token/order/getOrderListDetail", "/service/order/getOrderListDetail",
            "/session/order/getOrderListDetail" }, produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getOrderListDetail(
            HttpServletRequest request)
    {
        try
        {
            logger.info("分页获取订单详情列表-start");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String orderStatusStr = RequestUtils.getQueryParam(request, "orderStatus");
            String startDate = RequestUtils.getQueryParam(request, "startDate");
            String endDate = RequestUtils.getQueryParam(request, "endDate");

            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            CommonValidUtil
                    .validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
            long shopId = Long.valueOf(shopIdStr);
            String[] orderStatus = null;
            if (StringUtils.isNotBlank(orderStatusStr))
            {
                orderStatus = orderStatusStr.split(CommonConst.COMMA_SEPARATOR);
            }
            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtils.isBlank(startDate) && !StringUtils.equals(startDate, "null"))
            {
                startDate = startDate + " 00:00:00";
                params.put("startDate", startDate);
            }
            if (!StringUtils.isBlank(endDate) && !StringUtils.equals(endDate, "null"))
            {
                endDate = endDate + " 23:59:59";
                params.put("endDate", endDate);
            }
            // 查询过滤条件
            params.put("shopId", shopId);
            params.put("orderStatus", orderStatus);
            logger.info("=========查询参数：" + params);
            PageModel pageModel = this.orderServcie
                    .getOrderListDetail(params, PageModel.handPageNo(pageNO), PageModel.handPageSize(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDER_DETAIL_LIST_SUCCESS, msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("分页获取订单详情列表-系统异常", e);
            throw new APISystemException("分页获取订单详情列表-系统异常", e);
        }
    }

    /**
     * 此接口只为处理部分问题订单，不做正式接口
     *
     * @param request
     * @return ResultDto [返回类型说明]
     * @author shengzhipeng
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "dealOrder", produces = "application/json;charset=UTF-8") public @ResponseBody ResultDto dealOrder(
            HttpServletRequest request)
    {
        try
        {
            String orderId = RequestUtils.getQueryParam(request, "orderId");
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
            int status = CommonConst.ORDER_STS_YJZ; // 已开单 默认
            OrderDto order = orderServcie.getOrderMainById(orderId);
            CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_ORDER_NOT_EXIST);
            if (status != order.getOrderStatus())
            {
                order.setOrderStatus(status);
                order.setKefuRemark("人工处理结账订单");
                orderServcie.updateOrder(order);
                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, 0);
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "处理订单结账", null);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("用户下单-系统异常", e);
            throw new APISystemException("用户下单-系统异常", e);
        }
    }

    /**
     * 获取订单详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/order/wrapOrderDetail", "/token/order/wrapOrderDetail",
            "/session/order/wrapOrderDetail" }, produces = "application/json;charset=UTF-8") @ResponseBody public Object wrapOrderDetail(
            HttpServletRequest request)
    {
        try
        {
            logger.info("获取订单详情-start");
            String orderId = RequestUtils.getQueryParam(request, "orderId");
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
            Long userId = null;
            if (StringUtils.isNotBlank(userIdStr))
            {
                userId = NumberUtil.strToLong(userIdStr, "userId");
            }
            OrderDetailDto od = this.orderServcie.wrapOrderDetail(orderId, userId);

            if (od.getIsUpdate())
            {
                //有改动需要推送给收银机
                StringBuilder content = new StringBuilder();
                content.append("{");
                content.append("\"shopId\":" + od.getShopId() + ",");
                content.append("\"action\":\"updateOrder\",");
                content.append("\"data\":{\"id\":\"" + orderId + "\"}");
                content.append("}");
                PushDto push = new PushDto();
                push.setAction("updateOrder");
                push.setContent(content.toString());
                push.setUserId(userId);
                push.setShopId(od.getShopId());
                pushService.pushInfoToShop2(push);
            }
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_ORDERDETAIL, od,
                    DateUtils.DATETIME_FORMAT);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取订单详情-异常", e);
            throw new APISystemException("获取订单详情-异常", e);
        }
    }

    /**
     * PCO14：计算赠送红包金额接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/redPacket/getRedPacketSendMoney", "/token/redPacket/getRedPacketSendMoney",
            "/session/redPacket/getRedPacketSendMoney" }, produces = "application/json;charset=UTF-8") @ResponseBody public Object getRedPacketSendMoney(
            HttpServletRequest request)
    {
        try
        {

            /**
             * 步骤一：校验参数
             *
             * 校验参数合法性。特别注意金额字段为double类型
             */

            /**
             * 步骤二：查询订单满送规则
             *
             * 数据库表：1dcq_business_area_config（商圈活动配置表）
             *
             * 通过groupId、config_code得出满送规则（overVlues、giveVlues）
             */

            /**
             * 步骤三：计算红包金额
             *
             * 1、通过步骤二可以得出满多少（overVlues）、送多少（giveVlues） 规则
             * 2、判断传入参数（money）与overVlues比较，大于等于overVlues即得出sendMoney（红包金额）
             *
             */
            logger.info("计算赠送红包金额接口-start" + request.getQueryString());
            String moneyStr = RequestUtils.getQueryParam(request, "money");
            String businessAreaActivityIdStr = RequestUtils.getQueryParam(request, "businessAreaActivityId");
            String orderId = RequestUtils.getQueryParam(request, "orderId");

            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

            CommonValidUtil.validStrNull(moneyStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MONEY);
            CommonValidUtil.validStrNull(businessAreaActivityIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_MOBILE);

            Double money = NumberUtil.strToDouble(moneyStr, "money");
            Long businessAreaActivityId = NumberUtil.strToLong(businessAreaActivityIdStr, "businessAreaActivityId");

            if (StringUtils.isNotBlank(orderId))
            {
                OrderDto order = orderServcie.getOrderMainById(orderId);
                CommonValidUtil
                        .validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);

                // 比较money和订单实际支付金额
                Double settlePrice = order.getSettlePrice();
                if (!settlePrice.equals(money))
                {
                    throw new ValidateException(CodeConst.CODE_ORDER_ORDER_MONEY_ERROR,
                            CodeConst.MSG_ORDER_ORDER_MONEY_ERROR);
                }
            }

            // 校验商铺id和商圈id是否匹配
            if (StringUtils.isNotBlank(shopIdStr))
            {
                Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
                BusinessAreaShopDto busAreaShop = busAreaActivityService
                        .getBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
                if (busAreaShop == null)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_MATCHING_ERROR,
                            CodeConst.MSG_PARAMETER_MATCHING_ERROR);
                }
            }

            Double sendMoney = packetService.getRedPacketSendMoney(businessAreaActivityId, money);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("sendMoney", sendMoney);

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "计算赠送红包金额接口成功！", resultMap);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("计算赠送红包金额接口-异常", e);
            throw new APISystemException("计算赠送红包金额接口-异常", e);
        }
    }

    /**
     * PU3：获取会员红包列表接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/redPacket/getMemberRedPackets", "/token/redPacket/getMemberRedPackets",
            "/session/redPacket/getMemberRedPackets" }, produces = "application/json;charset=UTF-8") @ResponseBody public Object getMemberRedPackets(
            HttpServletRequest request)
    {
        try
        {
            // 查询会员拥有的红包列表、

            /**
             * 步骤一：校验参数
             *
             * 校验参数合法性、
             */

            /**
             * 步骤二：查询红包所有红包
             *
             * 涉及数据库表： 1dcq_red_packet（红包表）
             *
             * sql逻辑： 通过用户id即可以在1dcq_red_packet查询用户所有红包
             *
             */

            /**
             * 步骤三：单独查询shops
             *
             * 涉及数据库表：1dcq_business_area_activity（商圈配置表）、1dcq_shop
             *
             * sql逻辑： 通过商铺活动id查询（1dcq_business_area_activity）
             * 商圈活动表关联1dcq_business_area_shop（商铺活动店铺表）、 1dcq_shop商铺表。
             */

            logger.info("PU3：获取会员红包列表接口-start" + request.getQueryString());
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String beginDate = RequestUtils.getQueryParam(request, "beginDate");
            String endDate = RequestUtils.getQueryParam(request, "endDate");
            String pNo = RequestUtils.getQueryParam(request, "pNo");
            String pSize = RequestUtils.getQueryParam(request, "pSize");
            String status = RequestUtils.getQueryParam(request, "status");

            Map<String, Object> parms = new HashMap<String, Object>();

            // 校验参数
            CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "userId不能为空");
            Long userId = NumberUtil.strToLong(userIdStr, "userId");
            // 验证用户是否存在
            UserDto user = memberServcie.getUserByUserId(userId);
            if (null == user)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            }

            if (!StringUtils.isBlank(beginDate) && !StringUtils.equals(beginDate, "null"))
            {
                DateUtils.validDateStr(beginDate, DateUtils.DATE_FORMAT);
                beginDate = beginDate + " 00:00:00";
                parms.put("startDate", beginDate);
            }
            if (!StringUtils.isBlank(endDate) && !StringUtils.equals(endDate, "null"))
            {
                DateUtils.validDateStr(beginDate, DateUtils.DATE_FORMAT);
                endDate = endDate + " 23:59:59";
                parms.put("endDate", endDate);
            }

            // 验证商铺
            if (StringUtils.isNotEmpty(shopIdStr))
            {
                Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_SHOPID);
                int flag = this.shopServcie.queryShopExists(shopId);
                CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                parms.put("shopId", shopId);
            }
            if (StringUtils.isNotBlank(status))
            {
                parms.put("status", status);
            }
            parms.put("userId", userId);
            parms.put("pSize", PageModel.handPageSize(pSize));
            parms.put("limit", (PageModel.handPageNo(pNo) - 1) * PageModel.handPageSize(pSize));

            PageModel pageModel = packetService.getMemberRedPackets(parms);

            Map<String, Object> rltMap = new HashMap<String, Object>();
            rltMap.put("pNo", PageModel.handPageNo(pNo));
            rltMap.put("rCount", pageModel.getTotalItem());
            rltMap.put("lst", pageModel.getList());

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "PU3：获取会员红包列表接口成功！", rltMap);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("PU3：获取会员红包列表接口-异常", e);
            throw new APISystemException("PU3：获取会员红包列表接口-异常", e);
        }
    }

    /**
     * PU5：获取会员红包详情接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/redPacket/getRedPacketDetail", "/token/redPacket/getRedPacketDetail",
            "/session/redPacket/getRedPacketDetail" }, produces = "application/json;charset=UTF-8") @ResponseBody public Object getRedPacketDetail(
            HttpServletRequest request)
    {
        try
        {
            logger.info("PU5：获取红包详情接口-start" + request.getQueryString());
            String redPacketIdStr = RequestUtils.getQueryParam(request, "redPacketId");

            // 校验参数
            CommonValidUtil.validStrNull(redPacketIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "redPacketId不能为空");
            Long redPacketId = NumberUtil.strToLong(redPacketIdStr, "redPacketId");

            Map<String, Object> resultMap = packetService.getRedPacketDetail(redPacketId);
            if (resultMap != null)
            {

                String orderId = (String) resultMap.get("sourceOrderId");
                String userID = resultMap.get("userId").toString();
                Map<String, Object> userMap = userAccountService.getRedPackUserDetail(Long.valueOf(userID));
                if (userMap != null)
                {

                    resultMap.putAll(userMap);
                }
                Map<String, Object> orderMap = orderServcie.getRedPackOrderDetail(orderId);
                if (orderMap != null)
                {

                    resultMap.putAll(orderMap);
                }
            }
            return ResultUtil
                    .getResultJson(CodeConst.CODE_SUCCEED, "PU5：获取红包详情接口成功", resultMap, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("PU5：获取红包详情接口-异常", e);
            throw new APISystemException("PU5：获取红包详情接口-异常", e);
        }
    }

    /**
     * PCO16：订单结账接口
     *
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/order/settleOrder", "/token/order/settleOrder",
            "/session/order/settleOrder" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8") @ResponseBody public Object settleOrder(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        logger.info("PCO16：订单结账接口 -start");
        try
        {
            OrderDto order = checkSettleOrderParamValid(entity);
            Double sendRedPacketMoney = orderServcie.settleOrder(order);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("sendRedPacketMoney", sendRedPacketMoney);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "订单结账成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("PCO16：订单结账接口 -系统异常", e);
            throw new APISystemException("PCO16：订单结账接口 -系统异常", e);
        }
    }

    @RequestMapping(value = { "/token/order/countVoucherDeduction", "/service/order/countVoucherDeduction",
            "/session/order/countVoucherDeduction" },
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8") @ResponseBody public Object countVoucherDeduction(
            HttpServletRequest request) throws Exception
    {
        logger.info("PCO19：计算可使用代金券金额接口 -start");
        Map<String, Object> requestMap = getRequestMap(request);
        Object orderId = requestMap.get("orderId");
        if (orderId == null)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "orderId不能空");
        }

        OrderDto orderDto = orderServcie.getOrderDtoById(orderId.toString());
        if (orderDto == null)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单不存在");
        }

        Object userIdRequst = requestMap.get("userId");
        if (userIdRequst == null)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "userId不能空");
        }

        Long userId = CommonValidUtil
                .validStrLongFmt(userIdRequst.toString(), CodeConst.CODE_PARAMETER_NOT_VALID, "userId类型错误");

        UserDto user = memberServcie.getUserByUserId(userId);
        if (null == user)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("avaliVoucherAmount", orderServcie.countVoucherDeduction(userId, orderDto));
        return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "计算可使用代金券金额成功", resultMap, DateUtils.DATETIME_FORMAT);
    }

    private OrderDto checkSettleOrderParamValid(HttpEntity<String> entity) throws Exception
    {
        Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
        String orderId = postParamMap.get("orderId");

        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, "订单Id不能为空");

        OrderDto order = orderServcie.getOrderDtoById(orderId);
        if (order == null)
        {

            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单不存在");
        }

        Integer orderStatus = order.getOrderStatus();

        if ((CommonConst.ORDER_STS_YJZ == orderStatus && CommonConst.REVERSE_SETTLE_FLAG != order.getSettleFlag()
                .intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus || CommonConst.ORDER_STS_YTD == orderStatus)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR, "订单不可结算,订单状态为" + orderStatus);
        }

        Double payAmount = payDao.getSumPayAmount(orderId, null);
        if (payAmount < order.getSettlePrice() || order.getPayStatus() != 1)
        {

            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单未完成支付不能结算");
        }

        return order;
    }

    /**
     * PCO18：订单批量结账接口
     *
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = { "/service/order/bitchSettleOrder", "/token/order/bitchSettleOrder",
            "/session/order/bitchSettleOrder" }, produces = "application/json;charset=UTF-8") @ResponseBody public Object bitchSettleOrder(
            HttpEntity<String> entity, HttpServletRequest request)
    {
        logger.info("PCO18：订单批量结账接口 -start");
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<String> orderIds = new ArrayList<String>();

            Map<String, Object> postParamMap = getRequestMap(request);
            if (postParamMap.get("orderId") != null)
            {
                String orderIdStr = (String) postParamMap.get("orderId");
                String[] orderIdArr = orderIdStr.split(",");
                for (String orderId : orderIdArr)
                {
                    OrderDto order = checkSettleOrderParamValid(orderId);
                    String failOrderId = orderServcie.bitchSettleOrder(order);
                    if (failOrderId != null)
                    {
                        orderIds.add(failOrderId);
                    }
                }
                resultMap.put("failOrderId", orderIds);
            }

            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "订单结账成功", resultMap);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            logger.error("PCO18：订单批量结账接口 -系统异常", e);
            throw new APISystemException("PCO18：订单批量结账接口 -系统异常", e);
        }
    }

    private OrderDto checkSettleOrderParamValid(String orderId) throws Exception
    {

        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, "订单Id不能为空");

        OrderDto order = orderServcie.getOrderDtoById(orderId);
        if (order == null)
        {

            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单不存在");
        }

        Integer orderStatus = order.getOrderStatus();

        if ((CommonConst.ORDER_STS_YJZ == orderStatus && CommonConst.REVERSE_SETTLE_FLAG != order.getSettleFlag()
                .intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus || CommonConst.ORDER_STS_YTD == orderStatus)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR, "订单不可结算,订单状态为" + orderStatus);
        }

        //        Double payAmount = payDao.getSumPayAmount(orderId, null);
        //        if (payAmount < order.getSettlePrice() || order.getPayStatus() != 1)
        //        {
        //
        //            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单未完成支付不能结算");
        //        }

        return order;
    }

    /**
     * PCO17:根据订单码查询订单支付情况
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/token/order/getOrderByCode", "/service/order/getOrderByCode",
            "/session/order/getOrderByCode" }, produces = "application/json;charset=UTF-8") @ResponseBody public ResultDto getOrderByCode(
            HttpServletRequest request)
    {
        try
        {
            logger.info("根据订单码查询订单支付情况-start");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String orderCode = RequestUtils.getQueryParam(request, "payCode");
            String payStatusStr = RequestUtils.getQueryParam(request, "payStatus");
            String payTypeStr = RequestUtils.getQueryParam(request, "payType");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");

            //            CommonValidUtil.validStrNull(orderCode, CodeConst.CODE_PARAMETER_NOT_NULL,
            //                    CodeConst.MSG_REQUIRED_ORDER_CODE);
            CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
            Long shopId = CommonValidUtil
                    .validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);

            // pNo默认认为1
            int pNo = null == pNoStr ? 1 : Integer.valueOf(pNoStr.trim());
            // pSize默认为10
            int pSize = null == pSizeStr ? 10 : Integer.valueOf(pSizeStr.trim());

            Integer payStatus = payStatusStr == null ? null : Integer.valueOf(payStatusStr.trim());
            Integer payType = payTypeStr == null ? null : Integer.valueOf(payTypeStr.trim());
            PageModel model = orderServcie.queryOrderByOrderCode(shopId, orderCode, payStatus, payType, pNo, pSize);
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pNo);
            msgList.setpSize(pSize);
            msgList.setrCount(model.getTotalItem());
            msgList.setLst(model.getList());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_ORDER_LIST_SUCCESS, msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("根据payCode获取订单异常-系统异常", e);
            throw new APISystemException("根据payCode获取订单异常-系统异常", e);
        }
    }

    /**
     * PCO52：微信购买平台商品支付接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/token/order/weixPayByBuyProduct", "/service/order/weixPayByBuyProduct",
            "/session/order/weixPayByBuyProduct" }, produces = "application/json;charset=UTF-8", consumes = "application/json", method = RequestMethod.POST) @ResponseBody public ResultDto weixPayByBuyProduct(
            HttpServletRequest request, HttpEntity<String> entity) throws Exception
    {
        logger.info("根据订单码查询订单支付情况-start");
//        System.out.println(entity.getBody());
//        request.getRequest
        Map<String, String> postParamMap = JacksonUtil.postJsonToMap(entity);
//        String payTypeStr = RequestUtils.getQueryParam(request, "payType");
        String payTypeStr = postParamMap.get("payType");
//        String orderIdStr = RequestUtils.getQueryParam(request, "orderId");
        String orderIdStr = postParamMap.get("orderId");
//        String payMoneyStr = RequestUtils.getQueryParam(request, "payMoney");
        String payMoneyStr = postParamMap.get("payMoney");
//        String clientPayIdStr = RequestUtils.getQueryParam(request, "clientPayId");
        String clientPayIdStr = postParamMap.get("clientPayId");
//        String payClientTimeStr = RequestUtils.getQueryParam(request, "payClientTime");
        String payClientTimeStr = postParamMap.get("payClientTime");
//        String clientSystemStr = RequestUtils.getQueryParam(request, "clientSystem");
        String clientSystemStr = postParamMap.get("clientSystem");

        String payPassword = postParamMap.get("payPassword");

            /* 进行参数验证并获取正确的参数类型 */
        Integer payType = CommonValidUtil
                .validStrIntFmt(payTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, "payType为空或者格式不正确");
        //目前仅支持代金币
        if(payType.intValue() != 1)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "不受支持的payType");
        }
        CommonValidUtil.validStrNull(orderIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "orderId不能为空");
        String orderId = orderIdStr.trim();
        Double payMoney = CommonValidUtil
                .validStrDoubleFmt(payMoneyStr, CodeConst.CODE_PARAMETER_NOT_VALID, "payMoney为空或者格式不正确");
        Integer clientPayId = StringUtils.isNotBlank(clientPayIdStr) ? Integer.valueOf(clientPayIdStr.trim()) : null;
        if (StringUtils.isBlank(payClientTimeStr))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "clientSystem 不能为空");
        }
        Date payClientTime = null;
        try
        {
            payClientTime = DateUtils.parse(payClientTimeStr, "yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception e)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "payClientTime 不能为空");
        }
        Integer clientSystem = StringUtils.isNotBlank(clientSystemStr) ? Integer.valueOf(clientSystemStr.trim()) : null;
        //验证支付密码
        CommonValidUtil.validStrNull(payPassword, CodeConst.CODE_PARAMETER_NOT_NULL, "payPassword不能为空");
        payPassword = payPassword.trim();


        //对order进行验证
        OrderDto order = checkSettleOrderParamValid(orderId);

        //验证支付密码是否正确
        UserDto userDto =  mgrShopService.getUserById(order.getUserId());
        if(!payPassword.equalsIgnoreCase(userDto.getPayPassword()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "支付密码不正确");
        }

        PayDto payDto = new PayDto();
        payDto.setPayType(payType);
        payDto.setPayAmount(payMoney);
        payDto.setOrderId(orderId);
        payDto.setClientPayId(clientPayId);
        //            payDto.setOrderPayTime();
        payDto.setClientSystem(clientSystem);
        payDto.setUserPayTime(payClientTimeStr);
        orderServcie.settleOrder(payDto, payType);

        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "兑换礼品成功！", null);
    }
}
