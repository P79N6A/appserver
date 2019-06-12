package com.idcq.appserver.service.order;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.RechargeEnum;
import com.idcq.appserver.common.billStatus.RewardsEnum;
import com.idcq.appserver.common.enums.ClientSystemTypeEnum;
import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
import com.idcq.appserver.common.msgPusher.busMsg.msgSender.BusMsgSender;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.coupon.ICouponDao;
import com.idcq.appserver.dao.discount.ITimedDiscountGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dao.goods.IShopResourceGroupDao;
import com.idcq.appserver.dao.goods.IShopTimeIntevalDao;
import com.idcq.appserver.dao.order.IMyOrdersDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderGoodsServiceTechDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dao.order.IPOSOrderDetailDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.order.IXorderGoodsDao;
import com.idcq.appserver.dao.orderSettle.IOrderSettleDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.pay.IXorderPayDao;
import com.idcq.appserver.dao.pay.OrderGoodsSettleDao;
import com.idcq.appserver.dao.shop.IBookRuleDao;
import com.idcq.appserver.dao.shop.IDistributionTakeoutDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopRsrcDao;
import com.idcq.appserver.dao.shop.IShopRsrcGroupDao;
import com.idcq.appserver.dao.shop.IShopTimeIntDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponAvailableGoodsDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponDao;
import com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserAddressDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.common.PageResult;
import com.idcq.appserver.dto.coupon.CouponDto;
import com.idcq.appserver.dto.discount.TimedDiscountGoodsDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.ShopResourceGroupDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.BookInfo;
import com.idcq.appserver.dto.order.DataJsonDto;
import com.idcq.appserver.dto.order.MyOrdersDto;
import com.idcq.appserver.dto.order.OrderDetailDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderGoodsServiceTech;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.OrderModelForApp;
import com.idcq.appserver.dto.order.OrderPayModelForApp;
import com.idcq.appserver.dto.order.OrderSettleDto;
import com.idcq.appserver.dto.order.OrderShopResourceGoodDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.dto.order.OsrsDto;
import com.idcq.appserver.dto.order.POSOrderDataDto;
import com.idcq.appserver.dto.order.POSOrderDetailDto;
import com.idcq.appserver.dto.order.POSOrderDto;
import com.idcq.appserver.dto.order.POSOrderGoodsDto;
import com.idcq.appserver.dto.order.SeatInfo;
import com.idcq.appserver.dto.order.XorderGoodsDto;
import com.idcq.appserver.dto.pay.OrderGoodsSettle;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.pay.XorderPayDto;
import com.idcq.appserver.dto.shop.BookRuleDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;
import com.idcq.appserver.dto.shop.ShopRsrcDto;
import com.idcq.appserver.dto.shop.ShopRsrcGroupDto;
import com.idcq.appserver.dto.shop.ShopTimeIntDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.ShopUserVantages;
import com.idcq.appserver.dto.user.ShopUserVantagesLog;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserAddressDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.goods.IShopTimeIntevalService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IDistribTakeoutSettingService;
import com.idcq.appserver.service.shop.IShopBillService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.service.shopCoupon.enums.UserShopCouponStatusEnum;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardService;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.wxscan.WxPayResult;
import com.idcq.idianmgr.dao.shop.IShopCashierDao;

/**
 * @author Administrator
 * @date 2015年3月8日
 * @time 下午5:14:50
 */
@Service public class OrderServiceImpl implements IOrderServcie
{

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired public IOrderDao orderDao;

    @Autowired public ICommonDao commonDao;

    @Autowired public IOrderGoodsDao orderGoodsDao;

    @Autowired public IUserAddressDao userAddressDao;

    @Autowired public IPayDao payDao;

    @Autowired public IXorderPayDao xorderPayDao;

    @Autowired public IPacketDao packetDao;

    @Autowired public ITransactionDao transactionDao;

    @Autowired public IUserCashCouponDao userCouponDao;

    @Autowired public IGoodsDao goodsDao;

    @Autowired public IMyOrdersDao myOrdersDao;

    @Autowired public IUserDao userDao;

    @Autowired public IUserAccountDao userAccountDao;

    @Autowired public IShopDao shopDao;

    @Autowired public IOrderShopRsrcDao osrDao;

    @Autowired public IShopRsrcDao shopRsrcDao;

    @Autowired public IShopRsrcGroupDao shopRsrcGroupDao;

    @Autowired public IShopTimeIntDao shopTimeIntDao;

    @Autowired private IShopResourceGroupDao shopResourceGroupDao;

    @Autowired private IShopTimeIntevalDao shopTimeIntevalDao;

    @Autowired public IOrderShopRsrcDao orderShopRsrcDao;

    @Autowired public ITimedDiscountGoodsDao timedDiscountGoodsDao;

    @Autowired public IPushUserTableDao pushUserTableDao;

    @Autowired public IShopDeviceDao shopDeviceDao;

    @Autowired public OrderGoodsSettleDao orderGoodsSettleDao;

    @Autowired public IAgentDao agentDao;

    @Autowired public IShopBillDao shopBillDao;

    @Autowired public IPlatformBillDao platformBillDao;

    @Autowired public IUserBillDao userBillDao;

    @Autowired public ICouponDao couponDao;

    @Autowired public IXorderDao xoderDao;

    @Autowired public IXorderGoodsDao xorderGoodsDao;

    @Autowired public IBookRuleDao bookRuleDao;

    @Autowired public IOrderLogDao orderLogDao;

    @Autowired public IShopAccountDao shopAccountDao;

    @Autowired private ICollectService collectService;

    @Autowired private IDistribTakeoutSettingService distribTakeoutSettingService;

    @Autowired private IDistributionTakeoutDao distributionTakeoutDao;

    @Autowired private IShopTimeIntevalService shopTimeIntevalService;

    @Autowired private IGoodsCategoryDao goodsCategoryDao;

    @Autowired private IOrderShopRsrcService orderShopRsrcService;

    @Autowired private IShopTechnicianService shopTechnicianService;

    @Autowired private IPayServcie payServcie;

    @Autowired private ICollectDao collectDao;

    @Autowired private IShopCouponDao shopCouponDao;

    @Autowired private IShopCouponAvailableGoodsDao shopCouponAvailableGoodsDao;

    @Autowired private IUserShopCouponDao userShopCouponDao;

    @Autowired private IShopBillService shopBillService;
    /*
     * @Autowired private IShopConfigureSettingDao shopSettingDao;
     */

    /*
     * @Autowired private IConfigDao configDao;
     */

    @Autowired private IAttachmentDao attachmentDao;

    @Autowired private ITransaction3rdDao transaction3rdDao;

    @Autowired private IXorderDao xorderDao;

    @Autowired private IXorderService xorderService;

    @Autowired private IPushService pushService;

    @Autowired private IShopServcie shopService;

    @Autowired private IPayServcie payServiceImp;

    @Autowired private IPOSOrderDetailDao posOrderDetailDao;

    @Autowired private IOrderGoodsServiceTechDao orderGoodsServiceTechDao;

    @Autowired private IShopMemberCardService shopMemberCardService;

    @Autowired private IAttachmentRelationDao attachmentRelationDao;

    @Autowired private ICommonService commonService;

    @Autowired private IPacketService packetService;

    @Autowired private IShopCashierDao shopCashierDao;

    @Autowired private IStorageServcie storageService;

    @Autowired private BusMsgSender busMsgSender;

    @Autowired private IOrderSettleDao orderSettleDao;

    @Autowired private IGoodsSetDao goodsSetDao;

    public int saveTestOrderId(String orderId) throws Exception
    {
        return this.orderDao.saveTestOrderId(orderId);
    }

    public PageModel getMyOrders(Long userId, Map param, Integer goodsNumber, int page, int pageSize) throws Exception
    {

        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        List<MyOrdersDto> list = this.myOrdersDao.getMyOrders(userId, param, page, pageSize);
        if (CollectionUtils.isNotEmpty(list))
        {
            for (MyOrdersDto myOrdersDto : list)
            {
                String orderId = myOrdersDto.getOrderId();
                // 已支付金额
                BigDecimal payedAmount = this.packetDao.queryOrderPayAmount(orderId, CommonConst.PAY_TYPE_SINGLE);
                // 已支付
                Double payAmount = payedAmount.doubleValue();
                myOrdersDto.setPayedAmount(payAmount);
                // 需要支付的金额
                Double settlePrice = myOrdersDto.getSettlePrice();
                // 未支付
                if (null != settlePrice && settlePrice > payAmount)
                {
                    myOrdersDto.setNotPayedAmount(NumberUtil.sub(settlePrice, payAmount));
                }
                else
                {
                    myOrdersDto.setNotPayedAmount(0D);
                }

                // 设置订单相关的商铺信息
                Long shopId = myOrdersDto.getShopId();
                ShopDto shopDto = (ShopDto) DataCacheApi.getObject(CommonConst.KEY_SHOP + shopId);
                if (null != shopDto)
                {
                    myOrdersDto.setShopName(shopDto.getShopName());
                    myOrdersDto.setMemberDiscount(shopDto.getMemberDiscount());
                    myOrdersDto.setColumnId(shopDto.getColumnId() + "");
                }
                else
                {
                    Map map = this.myOrdersDao.getShopNameByShopId(myOrdersDto.getShopId());
                    if (null != map)
                    {
                        myOrdersDto.setShopName((String) map.get("shopName"));
                        myOrdersDto.setMemberDiscount(Double.parseDouble(map.get("memberDiscount") + ""));
                        myOrdersDto.setColumnId(map.get("columnId") + "");
                    }
                }

                // 设置订单商品
                if (goodsNumber > 0)
                {
                    // 新增goodsList
                    List<Map<String, Object>> orderGoodsList = orderGoodsDao
                            .getOrderGoodsListByIdForLimit(orderId, goodsNumber);
                    if (CollectionUtils.isNotEmpty(orderGoodsList))
                    {
                        myOrdersDto.setOrderGoodsList(orderGoodsList);
                    }
                }
                // 商铺图片
                AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
                attachmentRelationDto.setBizId(myOrdersDto.getShopId());
                attachmentRelationDto.setBizType(1);
                attachmentRelationDto.setPicType(1);
                List<AttachmentRelationDto> attachmentList = attachmentRelationDao
                        .findByCondition(attachmentRelationDto);
                if (CollectionUtils.isNotEmpty(attachmentList))
                {
                    String logoUrl = attachmentList.get(0).getFileUrl();
                    myOrdersDto.setShopLogoUrl(FdfsUtil.getFileProxyPath(logoUrl));
                }
            }
        }
        int totalItem = this.myOrdersDao.getMyOrdersCount(userId, param);
        PageModel pm = new PageModel();
        pm.setToPage(page);
        pm.setPageSize(pageSize);
        pm.setTotalItem(totalItem);
        pm.setList(list);
        return pm;
    }

    public Map<String, Object> syncOrderList(POSOrderDto posOrder) throws Exception
    {

        int addOrEditFlag = posOrder.getAddOrEditFlag();
        POSOrderDataDto data = posOrder.getData();
        // 获取用户信息
        UserDto user = posOrder.getUser();
        Long shopId = posOrder.getShopId();
        String orderId = data.getId();
        // 下订单场景类别
        Integer orderSceneType = data.getOrderSceneType();
        Integer orderStatus = data.getOrderStatus();
        Integer payStatus = data.getPayStatus();
        Integer orderType = data.getOrderType();
        orderType = 1;// 默认全额支付订单 add by ljp 20160531
        // 订单消费人数
        Integer consumerNum = data.getConsumerNum();
        // 座位ID
        String seatIds = data.getSeatIds();
        Integer isMaling = data.getIsMaling();
        OrderDto newOrder = new OrderDto();
        // 折前商品总价格
        Double goodsPriceBeforeDiscount = 0D;
        // 商品总价格
        Double goodsPrice = 0d;
        // 服务费
        Double logisticsPrice = data.getOutfee();
        // 商品项列表
        List<POSOrderGoodsDto> POSGoodsList = data.getOrderInfo();
        // 常规验证
        int orderGoodsNumber = posOrderValid(shopId, POSGoodsList);
        // 会员订单商品列表
        List<OrderGoodsDto> ogList = new ArrayList<OrderGoodsDto>();
        // 商品技师服务列表
        List<OrderGoodsServiceTech> goodsTechList = new ArrayList<OrderGoodsServiceTech>();
        // 订单标题
        StringBuilder orderTitle = new StringBuilder();
        // 商品项商品数量
        Double dishNum = null;
        // 订单商品折前总价
        BigDecimal bigPriceBeforeDicount = new BigDecimal(0);
        // 订单商品折后总价
        BigDecimal bigGoodsPrice = new BigDecimal(0);
        // 收银机实收订单金额
        BigDecimal bigPosSettlePrice = new BigDecimal(0);
        // 商品项参与结算价格
        BigDecimal bigGoodsSettlePrice = new BigDecimal(0);
        BigDecimal bigGoodsRequiredPrice = new BigDecimal(0);
        // 商品项原价
        BigDecimal bigStandardPrice = null;
        GoodsDto pGoods = null;
        Long goodsId = null;
        // 商品是否参与结算标志
        Integer goodsSettleFlag = null;
        Integer goodsRebateFlag = null;
        // 客户端商铺的会员折扣（接口传值）
        Double cMemberDiscount = data.getDiscount();
        BigDecimal bigMemberDiscount = null;

        // 订单中参与折扣商品的总价add by huangrui on 20150915
        // BigDecimal discountPrice = new BigDecimal(0);
        BigDecimal rebatePrice = new BigDecimal(0);
        // 商品项列表索引
        int goodsItemIndex = 0;
        int isMember = 0; // 默认为非会员
        String orderMobile = data.getMobile();
        Long userId = null;
        if (user != null && null != user.getIsMember())
        {
            isMember = user.getIsMember() == 1 ? 1 : 0;
            userId = user.getUserId();
        }

        /*
         * 开始计算价格
         */
        if (POSGoodsList != null && POSGoodsList.size() > 0)
        {
            // 平台店铺会员折扣
            Double sMemberDiscount = this.shopDao.getMemberDiscount(shopId);
            sMemberDiscount = sMemberDiscount == null ? 1D : (sMemberDiscount <= 0D ? 1D : sMemberDiscount);
            // client端会员折扣
            cMemberDiscount = cMemberDiscount == null ? 1D : (cMemberDiscount <= 0D ? 1D : cMemberDiscount);
            for (POSOrderGoodsDto g : POSGoodsList)
            {
                /** 批量添加订单商品技师列表* update by dengjh 20160229 */
                if (g.getTechs() != null)
                {
                    for (Map<String, Object> map : g.getTechs())
                    {
                        if (map.get("techId") != null)
                        {
                            OrderGoodsServiceTech ogs = new OrderGoodsServiceTech();
                            ogs.setOrderGoodsId(g.getDishId().intValue());// goodsId
                            ogs.setTechID(Integer.parseInt(map.get("techId").toString()));
                            goodsTechList.add(ogs);
                        }
                    }
                }

                OrderGoodsDto og = new OrderGoodsDto();
                og.setBillerId(g.getBillerId());
                og.setOrderGoodsDiscount(g.getOrderGoodsDiscount());
                og.setShopId(shopId);
                goodsId = g.getDishId();
                og.setGoodsId(goodsId);
                dishNum = g.getNum();
                og.setGoodsNumber(dishNum);
                if (goodsId == CommonConst.CUSTOM_GOODS_FLAG)
                {
                    pGoods = new GoodsDto();
                    pGoods.setGoodsSettleFlag(CommonConst.GOODS_SETTLE_FLAG_FALSE);
                    pGoods.setGoodsRebateFlag(CommonConst.GOODS_REBATE_FLAG_TRUE);
                    pGoods.setStandardPrice(g.getPrice());
                    pGoods.setGoodsName(g.getGoodsName());
                    pGoods.setSpecsDesc(g.getSpecsDesc());
                }
                else
                {
                    pGoods = this.goodsDao.getGoodsByIds(og.getShopId(), goodsId);
                    // 校验商品是否存在
                    if (pGoods == null)
                    {
                        logger.error("商品数据有误，商品ID为：" + goodsId);
                        continue;
                    }
                }

                // 商品是否参与打折
                goodsSettleFlag = pGoods.getGoodsSettleFlag();
                // 商品是否参与返点
                goodsRebateFlag = pGoods.getGoodsRebateFlag();
                Double standardPrice = pGoods.getStandardPrice();
                if (StringUtils.isNotBlank(g.getSpecsDesc()))
                {
                    standardPrice = g.getStandardPrice();
                }

                // 商品原价=商品目录价格x商品数量
                bigStandardPrice = new BigDecimal(standardPrice + "").multiply(new BigDecimal(dishNum + ""));
                // 商品参与结算价格=上传价格x商品数量
                bigGoodsSettlePrice = new BigDecimal(g.getPrice() + "").multiply(new BigDecimal(dishNum + ""));
                // 订单商品折前总价，为记录价格的总和
                bigPriceBeforeDicount = bigPriceBeforeDicount.add(bigStandardPrice);
                if (CommonConst.USER_IS_MEMBER == isMember)
                { // 会员
                    if (goodsSettleFlag == CommonConst.GOODS_SETTLE_FLAG_TRUE)
                    { // 如果商品支持打折
                        // 客户端打折
                        bigMemberDiscount = new BigDecimal(cMemberDiscount + "");
                        // 系统计算的结算价 (原价原折扣后的价格)
                        bigGoodsRequiredPrice = bigStandardPrice.multiply(new BigDecimal(sMemberDiscount + ""));
                        // 客户端改价后按客户端折扣计算的折扣价 (上传价与上传折扣的价格)
                        bigGoodsSettlePrice = bigGoodsSettlePrice.multiply(bigMemberDiscount);
                    }
                    else
                    { // 不支持打折
                        // 商品本该支付的金额
                        bigGoodsRequiredPrice = bigStandardPrice;
                    }
                    if (goodsRebateFlag == CommonConst.GOODS_REBATE_FLAG_TRUE)
                    {
                        // 返点金额
                        rebatePrice = rebatePrice.add(bigGoodsSettlePrice);
                    }
                }
                else
                { // 非会员 商品本该支付价格
                    bigGoodsRequiredPrice = bigStandardPrice;
                }

                og.setGoodsSettlePrice(bigGoodsSettlePrice.doubleValue()); // 每个商品项参与结算的总价格
                // 记录价格
                og.setGoodsRequiredPrice(bigGoodsRequiredPrice.doubleValue());
                // 记录目录单价
                og.setStandardPrice(standardPrice);
                og.setUserRemark(g.getDishRemark());
                og.setGoodsName(pGoods.getGoodsName());
                og.setSpecsDesc(g.getSpecsDesc());
                // 记录折算总价
                bigGoodsPrice = bigGoodsPrice.add(bigGoodsRequiredPrice);
                // 上传价格总价
                bigPosSettlePrice = bigPosSettlePrice.add(bigGoodsSettlePrice);
                if (goodsItemIndex < 2)
                {
                    orderTitle.append(pGoods.getGoodsName());
                    orderTitle.append("、");
                }
                goodsItemIndex++;
                og.setGoodsIndex(goodsItemIndex);
                og.setGoodsSettleFlag(goodsSettleFlag);
                og.setGoodsImg(getGoodsImg(goodsId));
                og.setGoodsRebateFlag(goodsRebateFlag);
                og.setUnitName(pGoods.getUnitName());
                og.setSettleUnitPrice(g.getPrice());
                og.setIsCancle(g.getIsCancle());
                ogList.add(og);
            }
            if (goodsTechList.size() > 0)
            {
                /* 批量添加订单商品技师列表* update by dengjh 20160229 */
                orderGoodsServiceTechDao.batchAddOrderGoodsServiceTech(goodsTechList);
            }
        }
        String title = dealTitle(addOrEditFlag, orderId, orderTitle);
        if (POSGoodsList.size() > 2)
        {
            title += "...";
        }
        // 折前订单记录总价
        goodsPriceBeforeDiscount = bigPriceBeforeDicount.doubleValue();
        // 折后订单记录总价，与上相对
        goodsPrice = bigGoodsPrice.doubleValue();
        BigDecimal bigLogisticsPrice = null;
        // 封装订单
        boolean isWm = data.getIsWm();
        Double orderDiscount = data.getAdditionalDiscount();// 折上折
        newOrder.setOrderId(orderId); // 订单ID
        newOrder.setShopId(shopId); // 商铺ID
        newOrder.setOrderSceneType(orderSceneType); // 全额订单
        newOrder.setOrderType(orderType); // 全额订单
        newOrder.setSettleFlag(posOrder.getSettleFlag()); // 已结算
        newOrder.setOrderStatus(orderStatus); // 订单状态
        newOrder.setPayStatus(payStatus); // 已支付
        newOrder.setConsumerNum(consumerNum);
        newOrder.setOrderChannelType(CommonConst.ORDER_CHANNEL_POS_CQB);
        newOrder.setClientSystemType(ClientSystemTypeEnum.CASHIER.getValue());
        newOrder.setSeatIds(seatIds);
        newOrder.setGoodsNumber(orderGoodsNumber <= 0 ? null : orderGoodsNumber);
        newOrder.setOrderServiceType(isWm == true ? 1 : 0);// 订单服务方式
        newOrder.setUserRemark(data.getUserRemark());
        newOrder.setIsMaling(isMaling);
        newOrder.setIsWait(data.getIsWait());
        // newOrder.setLastUpdateTime(new Date()); TODO 删除最后更新时间 2016.1.27
        // 原因：同步交易记录会根据此时间和clientLastTime比较
        newOrder.setOrderDiscount((orderDiscount != null && orderDiscount > 0) ? orderDiscount : 1D);
        newOrder.setCashierId(data.getCashierId());
        // 客户端折扣
        newOrder.setMemberDiscount(cMemberDiscount);
        newOrder.setClientLastTime(data.getClientLastTime());
        newOrder.setBusinessAreaActivityId(data.getBusinessAreaActivityId());
        newOrder.setCashierUsername(data.getCashierUsername());
        // //新增操作
        if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
        {
            Date orderTime = data.getCreateTime();
            newOrder.setOrderTime(orderTime != null ? orderTime : new Date());// 下单时间
        }
        newOrder.setUserId(userId); // 会员ID
        // 商品列表不为空，则需计算订单价格
        if (!CommonValidUtil.validListNull(POSGoodsList))
        {
            newOrder.setGoodsPrice(goodsPrice);
            bigLogisticsPrice = new BigDecimal((logisticsPrice == null ? 0d : logisticsPrice) + "");
            newOrder.setSettlePrice(new BigDecimal(goodsPrice).add(bigLogisticsPrice)
                    .doubleValue());// *********************************************************?????
            newOrder.setOrderTotalPrice(newOrder.getSettlePrice());// 订单总价
            newOrder.setGoodsPriceBeforeDiscount(goodsPriceBeforeDiscount); // 折前商品总价
            newOrder.setOrderTitle(title); // 订单标题
        }
        // 订单结算金额：实收金额有值则以实收金额作为结算金额，否则以收银机商品单价累加作为结算金额
        if (data.getPaidAmount() != null && (data.getPaidAmount() > 0 || (data.getPaidAmount() == 0
                && data.getOrderStatus() == CommonConst.ORDER_STS_YJZ)))
        { // 结单费不包含服务费
            newOrder.setSettlePrice(
                    data.getPaidAmount()); // *********************************************************?????
        }
        else
        {
            newOrder.setSettlePrice(
                    bigPosSettlePrice.doubleValue());// *********************************************************?????
        }
        newOrder.setLogisticsPrice(logisticsPrice);

        // 设置订单中参与折扣商品总价 add by huangrui in 20150915
        if (StringUtils.equals(data.getPayMode(), CommonConst.PAY_MODE_SHOP_TIMECARD))
        {
            data.setPaidAmount(data.getTotal());
            newOrder.setOrderTotalPrice(data.getTotal());
            newOrder.setGoodsPrice(data.getTotal());
            newOrder.setSettlePrice(data.getTotal());
            newOrder.setOrderRealSettlePrice(data.getTotal());
        }
        else
        {
            newOrder.setOrderRealSettlePrice(NumberUtil.min(rebatePrice.doubleValue(), newOrder.getSettlePrice()));
        }
        newOrder.setSettleType(shopDao.getShopById(shopId).getOrderSettleType());// 设置订单结算方式
        // -会员订单
        newOrder.setIsMember(isMember);
        newOrder.setMobile(orderMobile);
        // 处理订单
        if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
        {// 新增
            // 设置订单通道类型 add by
            // ljp 20151026
            newOrder.setOrderChannelType(2);
            newOrder.setClientSystemType(ClientSystemTypeEnum.CASHIER.getValue());
            if (data.getCouponDiscountPrice() != null)
            {
                Double tempSettlePrice = newOrder.getSettlePrice();
                Double deductPrice = data.getCouponDiscountPrice();
                if (deductPrice >= tempSettlePrice)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
                            "优惠券抵扣金额不能超过需支付金额   couponDiscountPrice：" + deductPrice + " settlePrice:"
                                    + tempSettlePrice);
                }
                newOrder.setSettlePrice(NumberUtil.sub(tempSettlePrice, deductPrice));
                updateShopCoupon(data.getUserShopCouponIdList(), orderId, newOrder.getShopId(), newOrder.getMobile());
            }
            this.orderDao.saveOrder(newOrder);
        }
        else
        {// 修改
            this.orderDao.updateOrder(newOrder);
            // 修改时不管是否传入商品列表，直接删除旧的商品列表 lujianping 20150712
            this.orderGoodsDao.delGoodsByOrderId(orderId);
            if (StringUtils.isNotBlank(data.getSeatIds()) && !data.getSeatIds().equals(orderStatus))
            {
                collectDao.updateShopResourceStatus(orderId, CommonConst.RESOURCE_STATUS_NOT_IN_USE);
            }

        }
        // 保存订单商品列表
        for (OrderGoodsDto og2 : ogList)
        {
            og2.setOrderId(data.getId());
            this.orderGoodsDao.saveOrderGoods(og2);
        }
        // 保存支付信息
        if (orderStatus == CommonConst.ORDER_STS_YJZ && payStatus == CommonConst.PAY_STATUS_PAYED)
        {
            // 支付类型
            Integer payType = null;
            if (StringUtils.equals(data.getPayMode(), "刷卡"))
            {
                payType = CommonConst.PAY_TYPE_POS;
            }
            else if (StringUtils.equals(data.getPayMode(), "现金"))
            {
                payType = CommonConst.PAY_TYPE_CASH;
            }
            else if (StringUtils.equals(data.getPayMode(), "5"))
            {
                payType = CommonConst.PAY_TYPE_CARD;
                ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
                shopMemberCardDto.setConsumeMoney(newOrder.getSettlePrice());
                shopMemberCardDto.setShopId(shopId);
                shopMemberCardDto.setOrderId(orderId);
                shopMemberCardDto.setMobile(posOrder.getData().getMobile());
                shopMemberCardService.shopMemberCardComsume(shopMemberCardDto, orderId);// 会员卡消费
                // 记录transaction记录
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = format.format(new Date());
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setUserId(userId);
                transactionDto.setOrderId(orderId);
                transactionDto.setPayAmount(newOrder.getSettlePrice());// 支付金额
                transactionDto.setTransactionTime(nowTime);
                transactionDto.setStatus(1);// 支付成功
                transactionDto.setUserPayChannelId(new Long(1));
                transactionDto.setOrderPayType(0);
                transactionDto.setLastUpdateTime(nowTime);
                transactionDto.setTransactionType(0);
                // 生成流水记录
                transactionDao.addTransaction(transactionDto);
            }
            else if (StringUtils.equals(data.getPayMode(), CommonConst.PAY_MODE_SHOP_TIMECARD))
            {
                payType = CommonConst.PAY_TYPE_TIMECARD;
                List<ShopMemberCardDto> shopMemberCardList = new ArrayList<ShopMemberCardDto>();// 每一种服务的消费都当做一次次卡消费
                for (POSOrderGoodsDto g : POSGoodsList)// 针对
                {
                    ShopMemberCardDto shopMemberCardItem = new ShopMemberCardDto();
                    shopMemberCardItem.setCardId(g.getCardId());// 设置消费的卡
                    shopMemberCardItem.setOrderId(orderId);// 设置订单编号
                    shopMemberCardItem.setGoodsName(g.getGoodsName());// 次卡消费的是哪个商品
                    shopMemberCardItem.setMobile(posOrder.getData().getMobile());// 哪个会员
                    shopMemberCardItem.setShopId(shopId);// 哪家店铺
                    shopMemberCardItem.setGoodsId(g.getDishId());// 消费的是哪个商品
                    shopMemberCardList.add(shopMemberCardItem);// 添加一次次卡消费
                }
                shopMemberCardService.shopTimeCardComsume(shopMemberCardList);
            }
            addOrderPay(data, shopId, orderId, payType);
        }
        if (posOrder.getAddOrderLogFlag() == CommonConst.HANDLE_FLAG_ADD)
        {
            // 保存订单日志信息
            saveOrderLog(data.getCashierId(), orderId, orderStatus, payStatus);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 订单完成释放1dcq_shop_resource表中的资源
        if (orderStatus == CommonConst.ORDER_STS_YJZ)
        {
            collectDao.updateShopResourceStatus(orderId, CommonConst.RESOURCE_STATUS_NOT_IN_USE);
            if (user == null)
            {
                // 非会员订单生成账务统计记录 add by lujianping 20150924
                this.handleAccountingStatByUser(newOrder);
            }
            Double sendMoney = packetService.sendRedPacketToUser(newOrder);
            if (sendMoney != 0)
            {
                Double orderRealSettlePrice = NumberUtil.sub(newOrder.getOrderRealSettlePrice(), sendMoney);
                if (orderRealSettlePrice < 0)
                {
                    orderRealSettlePrice = 0D;
                }
                orderDao.updateOrderRealSettlePrice(orderId, orderRealSettlePrice, sendMoney);
            }
            map.put("sendRedPacketMoney", sendMoney);

            // 修改库存
            storageService.insertShopStorageByOrderId(orderId, shopId);
        }
        else if (orderStatus == CommonConst.ORDER_STS_YKD)
        {
            // 预定资源
            reservResource(seatIds, orderId);
        }

        map.put("orderId", orderId);
        map.put("orderStatus", orderStatus);
        return map;
    }

    private void saveOrderLog(Long operateId, String orderId, Integer orderStatus, Integer payStatus) throws Exception
    {
        OrderLogDto orderLog = new OrderLogDto();
        orderLog.setOrderId(orderId);
        orderLog.setOrderStatus(orderStatus);
        orderLog.setPayStatus(payStatus);
        orderLog.setRemark(OrderStatusEnum.getStatusName(orderStatus));
        orderLog.setUserId(operateId);
        orderLog.setLastUpdateTime(new Date());
        this.orderLogDao.saveOrderLog(orderLog);
    }

    /**
     * 处理订单标题
     *
     * @param addOrEditFlag
     * @param orderId
     * @param orderTitle
     * @return String [返回类型说明]
     * @throws Exception [参数说明]
     * @throws throws    [违例类型] [违例说明]
     * @author shengzhipeng
     * @see [类、类#方法、类#成员]
     */
    private String dealTitle(int addOrEditFlag, String orderId, StringBuilder orderTitle) throws Exception
    {
        // 订单标题，去掉最后的"、"
        String title = orderTitle.toString();
        if (title.length() > 1 && title.substring(title.length() - 1).equals("、"))
        {
            title = title.substring(0, title.length() - 1);
        }
        else
        {
            if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
            {
                // 新增订单时，商品列表为空，需要从预订商铺资源中拼凑orderTitle
                ShopRsrcGroupDto shopRsrcGroup = this.shopRsrcGroupDao.getRsrcGroupLimitOneByOrderId(orderId);
                if (shopRsrcGroup != null)
                {
                    title = shopRsrcGroup.getBookType() + shopRsrcGroup.getMinPeople() + "-" + shopRsrcGroup
                            .getMaxPeople() + "人桌";
                }
            }
        }
        return title;
    }

    /**
     * 获取商品缩略图
     *
     * @param goodsId
     * @return String 缩略图地址
     * @author shengzhipeng
     */
    private String getGoodsImg(Long goodsId)
    {

        try
        {
            List<String> urls = attachmentDao.getAttachUrlListByCondition(goodsId, CommonConst.BIZ_TYPE_IS_GOODS,
                    CommonConst.PIC_TYPE_IS_SUONUE);
            if (CollectionUtils.isNotEmpty(urls))
            {
                return urls.get(0);
            }
        }
        catch (Exception e)
        {
            logger.error("获取附件地址异常" + e);
        }
        return null;
    }

    /**
     * 预定商铺资源
     *
     * @param seatIds 座位id
     * @param orderId
     * @throws Exception
     * @Function: com.idcq.appserver.service.order.OrderServiceImpl.
     * reservResource
     * @Description:
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年9月16日 下午7:24:33
     * <p/>
     * Modification History: Date Author Version Description
     * ----
     * ------------------------------------------------------
     * ------- 2015年9月16日 shengzhipeng v1.0.0 create
     */
    public void reservResource(String seatIds, String orderId) throws Exception
    {
        if (StringUtils.isNotBlank(seatIds) && !seatIds.equals("-1") && !seatIds.equals("0"))
        {
            String[] resourceIds = seatIds.split(CommonConst.COMMA_SEPARATOR);
            for (String resourceId : resourceIds)
            {
                Map param = new HashMap();
                param.put("resourceId", resourceId);
                param.put("orderId", orderId);
                shopRsrcDao.useShopResource(param);
            }
        }
    }

    /**
     * 添加会员订单支付信息
     *
     * @param data
     * @param shopId
     * @param orderId
     * @param payType
     * @throws Exception
     */
    private void addOrderPay(POSOrderDataDto data, Long shopId, String orderId, Integer payType) throws Exception
    {
        // 订单结算金额
        BigDecimal amount = packetDao.queryOrderAmount(orderId);
        // 订单已支付金额
        BigDecimal payAmount = this.packetDao.queryOrderPayAmount(orderId, 0);
        // 订单未支付金额
        BigDecimal needPayAmount = amount.subtract(payAmount);
        // 校验实收金额不能小于未支付金额
        BigDecimal paidAmount = new BigDecimal(data.getPaidAmount());
        // 保留2位小数
        double paidAmountDouble = paidAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        CommonValidUtil.validDoubleGreaterThan(paidAmountDouble, needPayAmount.doubleValue(), CodeConst.CODE_INVALID,
                CodeConst.MSG_PAYAMOUNT_LESS);

        if (needPayAmount.doubleValue() > 0)
        {
            PayDto payDto = new PayDto();
            payDto.setOrderId(orderId);
            payDto.setPayType(payType);
            payDto.setPayAmount(needPayAmount.doubleValue());
            payDto.setOrderPayType(0);
            payDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
            payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP); // 收款人类型
            payDto.setShopId(shopId);
            payDto.setUserPayTime(DateUtils
                    .format(data.getPayTime() == null ? new Date() : data.getPayTime(), DateUtils.DATETIME_FORMAT));
            payDto.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
            payDto.setAutoSettleFlag(CommonConst.AUTO_SETTLE_FLAG_TRUE);
            // TODO不明确
            // payDto.setPayChannel(payChannel);
            String mobile = data.getMobile();
            UserDto userDto = userDao.getUserByMobile(mobile);
            if (userDto != null)
            {
                payDto.setUserId(userDto.getUserId());
                logger.info("userId:" + userDto.getUserId());
            }

            this.payDao.addOrderPay(payDto);
        }
    }

    /**
     * 添加非会员订单支付信息
     *
     * @param data
     * @param shopId
     * @param orderId
     * @param payType
     * @throws Exception
     */
    private void addXorderPay(POSOrderDataDto data, Long shopId, String orderId, Integer payType) throws Exception
    {
        // 订单结算金额
        BigDecimal amount = packetDao.queryXorderAmount(orderId);
        // 订单已支付金额
        BigDecimal xpayAmount = this.packetDao.queryXorderPayAmount(orderId, 0);
        // 订单未支付金额
        BigDecimal needPayAmount = amount.subtract(xpayAmount);
        // 校验实收金额不能小于未支付金额
        CommonValidUtil
                .validDoubleGreaterThan(data.getPaidAmount(), needPayAmount.doubleValue(), CodeConst.CODE_INVALID,
                        CodeConst.MSG_PAYAMOUNT_LESS);

        XorderPayDto payDto = new XorderPayDto();
        payDto.setXorderId(orderId);
        payDto.setPayType(payType);
        payDto.setPayAmount(needPayAmount.doubleValue() < 0 ? 0D : needPayAmount.doubleValue());
        payDto.setOrderPayType(0);
        payDto.setOrderPayTime(data.getPayTime());
        payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP); // 收款人类型
        payDto.setShopId(shopId);

        this.xorderPayDao.addXorderPayDto(payDto);
    }

    /**
     * pos收银机同步交易记录数据验证
     *
     * @param shopId
     * @param POSGoodsList
     * @throws Exception
     */
    private int posOrderValid(Long shopId, List<POSOrderGoodsDto> POSGoodsList) throws Exception
    {
        // 验证商品列表
        int orderGoodsNumber = 0;
        Long goodsId = null;
        Double goodsNumber = null;
        if (POSGoodsList != null && POSGoodsList.size() > 0)
        {
            for (POSOrderGoodsDto g : POSGoodsList)
            {
                goodsId = g.getDishId();
                CommonValidUtil
                        .validObjectNull(goodsId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_ID);
                goodsNumber = g.getNum();

                // 商品存在性,-1代表自定义价格的商品，属于系统初始化，没有店铺
                if (goodsId != CommonConst.CUSTOM_GOODS_FLAG)
                {
                    int gId = this.goodsDao.validGoodsExists(shopId, goodsId);
                    CommonValidUtil.validPositInt(gId, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
                }

                // 数量(订单商品项商品数量改为支付小数 20150703)
                CommonValidUtil
                        .validDoubleNull(goodsNumber, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GNUM);
                CommonValidUtil.validDoubleGreaterThan(goodsNumber, 0D, CodeConst.CODE_PARAMETER_NOT_NULL,
                        CodeConst.MSG_NOT_ZERO_GNUM);
                // 获取商品计量单位
                Integer digitScale = this.goodsDao.getDigitScaleOfGoodsUnit(goodsId);
                CommonValidUtil.validDecimals(goodsNumber, digitScale, CodeConst.CODE_NOT_FLOAT_GNUM,
                        CodeConst.MSG_NOT_FLOAT_GNUM);
                // 累计订单商品数量
                orderGoodsNumber += (digitScale == null || digitScale == 0) ? goodsNumber : 1;
            }
        }
        return orderGoodsNumber;
    }

    // channel： 1，下单；2，更新订单
    public OrderDto placeOrder(OrderDto order, int channel) throws Exception
    {
        int addOrEditFlag = order.getAddOrEditFlag();
        // 订单商品列表验证
        goodsListValid(order, channel);
        // 验证自助下单，资源是否占用
        seatIdValid(order);
        List<OrderGoodsDto> oGoodsList = order.getGoods();
        Long orderShopId = order.getShopId();

        // 订单号校验
        String orderId = order.getOrderId();
        if (!StringUtils.isBlank(orderId))
        {
            // 检查订单号唯一性
            int num = this.orderDao.queryOrderExists(orderId);
            CommonValidUtil.validIsZero(num, CodeConst.CODE_EXISTS_ORDERID, CodeConst.MSG_EXISTS_ORDERID);
        }
        else
        {
            // 订单号为空则生成订单号
            orderId = FieldGenerateUtil.generateOrderId(orderShopId);
        }
        // 服务费
        Double logisticsPrice = order.getLogisticsPrice();
        // 折前商品总价
        Double goodsPriceBeforeDiscount = 0D;
        // 折后商品总价
        Double goodsPrice = 0D;
        // 抹零价
        Double maling = order.getMaling();
        // 订单标题
        String orderTitle = null;

        // 计算折前、折后、总价等价钱
        Map<String, Object> map = getOrderPrice(order, channel);
        goodsPrice = (Double) map.get("goodsPrice");
        goodsPriceBeforeDiscount = (Double) map.get("goodsPriceBeforeDiscount");
        orderTitle = (String) map.get("orderTitle") == null ? "" : (String) map.get("orderTitle");
        String title = dealTitle(addOrEditFlag, orderId, new StringBuilder(orderTitle));
        if (oGoodsList.size() > 2)
        {
            title += "...";
        }
        Double memberDiscount = (Double) map.get("memberDiscount");
        Double rebatePrice = (Double) map.get("rebatePrice");
        oGoodsList = order.getGoods();
        // 折后商品总价
        BigDecimal bigGoodsPrice = null;
        // 服务费
        BigDecimal bigLogisticsPrice = null;

        // 折后商品总价
        order.setGoodsPrice(goodsPrice);
        bigGoodsPrice = new BigDecimal(goodsPrice + "");
        bigLogisticsPrice = new BigDecimal(logisticsPrice + "");
        Integer orderType = order.getOrderType();
        // 参与结算的总价 = 折后商品总价 + 服务费
        double settlePrice = bigGoodsPrice.add(bigLogisticsPrice).doubleValue();
        // 预付订单
        if (orderType == CommonConst.ORDER_TYPE_REPAY)
        {
            Double prepayAmount = order.getPrepayMoney();
            prepayAmount = (prepayAmount == null ? 0D : prepayAmount);
            // 预付订单预定金若大于商品金额，则以预定金作为结算金额
            order.setSettlePrice(prepayAmount >= settlePrice ? prepayAmount : settlePrice);
        }
        else
        {
            order.setSettlePrice(settlePrice);
        }

        if (order.getCouponDiscountPrice() != null)
        {
            Double tempSettlePrice = order.getSettlePrice();
            Double deductPrice = order.getCouponDiscountPrice();
            if (deductPrice >= tempSettlePrice)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
                        "优惠券抵扣金额不能超过需支付金额   couponDiscountPrice：" + deductPrice + " settlePrice:" + tempSettlePrice);
            }
            order.setSettlePrice(NumberUtil.sub(tempSettlePrice, deductPrice));
            updateShopCoupon(order.getUserShopCouponIdList(), orderId, order.getShopId(), order.getMobile());
        }
        Double orderTotalPrice = order.getOrderTotalPrice();
        if (order.getOrderSceneType() == CommonConst.PLACE_ORDER_SCAN)
        {
            order.setSettlePrice(orderTotalPrice);
            order.setOrderRealSettlePrice(orderTotalPrice);
        }

        order.setOrderTotalPrice(order.getSettlePrice()); // 订单总价
        order.setOrderRealSettlePrice(NumberUtil.min(order.getSettlePrice(), rebatePrice));
        order.setOrderId(orderId);
        if (StringUtils.isBlank(order.getOrderTitle()))
        {
            order.setOrderTitle(title);
        }
        order.setGoodsPriceBeforeDiscount(goodsPriceBeforeDiscount);// 折前商品总价
        order.setMemberDiscount(memberDiscount);
        order.setSettleType((Integer) map.get("orderSettleType"));
        // 兼容收银机接口请求一点传奇支付（非会员转会员订单，删除旧的非会员订单信息）
        if (channel == 2)
        {
            maling = order.getMaling();
            if (maling != null)
            {
                order.setSettlePrice(maling);
            }
        }
        order.setInputAddress(getAddress(order.getAddressId()));
        order.setServerLastTime(System.currentTimeMillis());
        Long userId = order.getUserId();
        if (null != userId)
        {
            UserDto userDto = userDao.getUserById(userId);
            if (null != userDto)
            {
                order.setMobile(userDto.getMobile());
            }
        }
        // 保存订单
        this.orderDao.saveOrder(order);
        // 保存订单商品列表
        if (oGoodsList != null && oGoodsList.size() > 0)
        {
            for (OrderGoodsDto og2 : oGoodsList)
            {
                og2.setOrderId(orderId);
            }
            this.orderGoodsDao.saveOrderGoodsBatch(oGoodsList);
        }
        return order;
    }

    /**
     * 获取座位订单
     *
     * @param requestMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSeatOrder(Map<String, Object> requestMap) throws Exception
    {
        Object orderStatus = requestMap.get("orderStatus");
        String orderTimeSection = null;
        if (orderStatus == null || Integer.valueOf(orderStatus.toString()) == 1)
        {
            orderTimeSection = "24";
        }
        else
        {
            orderTimeSection = commonDao.getSmsValueByKey("order_time_section");
        }
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - Integer.valueOf(orderTimeSection) * 60 * 60 * 1000);
        requestMap.put("startTime", startTime);
        requestMap.put("endTime", endTime);
        return orderDao.getSeatOrder(requestMap);
    }

    /**
     * 根据地址ID获取详细地址 <功能详细描述>
     *
     * @param addressId
     * @return String 详细地址
     * @author shengzhipeng
     */
    private String getAddress(Long addressId)
    {
        if (addressId == null)
        {
            return null;
        }
        UserAddressDto userAddress = null;
        try
        {
            userAddress = userAddressDao.getAddressDetialById(addressId);
        }
        catch (Exception e)
        {
            logger.error("根据地址ID获取详细地址失败" + addressId);
        }
        if (null == userAddress)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(userAddress.getProvinceName()).append(userAddress.getCityName()).append(userAddress.getDistrictName())
                .append(userAddress.getTownName()).append(userAddress.getAddress());
        return sb.toString();
    }

    private Map<String, Object> getOrderPrice(OrderDto order, Integer channel) throws Exception
    {
        List<OrderGoodsDto> oGoodsList = order.getGoods();
        Double goodsPriceBeforeDiscount = 0D;
        Double goodsPrice = 0D;
        BigDecimal bigStandardPrice = null;
        BigDecimal bigGoodsPriceBeforeDiscount = new BigDecimal(0);
        BigDecimal bigGoodsSettlePrice = new BigDecimal(0);
        BigDecimal bigGoodsRequiredPrice = new BigDecimal(0);
        BigDecimal bigGoodsPrice = new BigDecimal(0);

        // 打折商品的总价，不打折的不计算在内
        BigDecimal discountPrice = new BigDecimal(0);

        // 返点商品总价
        BigDecimal rebatePrice = new BigDecimal(0);

        StringBuilder orderTitle = new StringBuilder();
        // 商品项列表索引
        int goodsItemIndex = 0;
        // 不打折
        Integer goodsSettleFlag = CommonConst.GOODS_SETTLE_FLAG_FALSE;
        // 不返点
        Integer goodsRebateFlag = CommonConst.GOODS_REBATE_FLAG_FALSE;
        Double memberDiscount = null;
        BigDecimal bigMemberDiscount = null;
        ShopDto shop = this.shopDao.getShopById(order.getShopId());
        if (channel == 2)
        {
            memberDiscount = order.getMemberDiscount();
        }
        else
        {
            memberDiscount = shop.getMemberDiscount();
            if (order.getIsMember() != null && order.getIsMember() == CommonConst.USER_IS_NOT_MEMBER)
            {
                memberDiscount = 1D;
            }
        }
        Double memberDiscount2 = memberDiscount == null ? 1D : (memberDiscount <= 0D ? 1D : memberDiscount);
        if (order.getOrderType() != CommonConst.ORDER_TYPE_COUPON)
        {

            // 普通商品及服务订单价格
            if (oGoodsList != null && oGoodsList.size() > 0)
            {
                List<OrderGoodsDto> orderGoodsDtos = new ArrayList<OrderGoodsDto>();
                /*
                 * 计算 rice）计算
                 */
                for (OrderGoodsDto og : oGoodsList)
                {
                    Long goodsId = og.getGoodsId();
                    Double settleUnitPrice = og.getStandardPrice();
                    GoodsDto pGoods = this.goodsDao.getGoodsByIds(og.getShopId(), goodsId);
                    if (pGoods == null)
                    {
                        logger.error("商品数据有误，商品ID为：" + goodsId);
                        continue;
                    }
                    if (pGoods.getGoodsType() != null && pGoods.getGoodsType() == 3000)
                    {
                        //套餐有统一的goodsGroup
                        //                        String goodsGroup = null;
                        String goodsGroup = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
                        //                        pGoods.setgoods
                        og.setSetGoodsGroup(goodsGroup);
                        Double numble = og.getGoodsNumber();
                        //商品是套餐
                        List<GoodsSetDto> goodsSetList = goodsSetDao.getGoodsIdListByGoodsSetId(goodsId);
                        for (GoodsSetDto goodsSet : goodsSetList)
                        {
                            OrderGoodsDto orderGoodsSet = new OrderGoodsDto();
                            Double goodsSetNum = goodsSet.getGoodsNumber();
                            BigDecimal goodsSetPrice = goodsSet.getPrice();
                            orderGoodsSet.setGoodsSetId(goodsId);
                            orderGoodsSet.setGoodsType(5000);//套餐内商品
                            orderGoodsSet.setGoodsId(goodsSet.getGoodsId());
                            orderGoodsSet.setShopId(og.getShopId());
                            orderGoodsSet.setGoodsNumber(goodsSetNum * numble);
                            orderGoodsSet.setStandardPrice(goodsSetPrice.doubleValue());
                            orderGoodsSet.setGoodsSettlePrice(
                                    NumberUtil.multiply(goodsSetPrice.doubleValue(), goodsSetNum));// 折后价
                            orderGoodsSet.setGoodsRequiredPrice(
                                    NumberUtil.multiply(goodsSetPrice.doubleValue(), goodsSetNum));// 本应支付的金额
                            orderGoodsSet.setGoodsIndex(goodsSet.getGoodsIndex());
                            orderGoodsSet.setSettleUnitPrice(goodsSetPrice.doubleValue());
                            orderGoodsSet.setGoodsName(goodsSet.getGoodsName());
                            orderGoodsSet.setSetGoodsGroup(goodsSet.getGoodsGroup());
                            orderGoodsSet.setSetGoodsGroup(goodsGroup);
                            orderGoodsSet.setGoodsSettleFlag(goodsSet.getGoodsSettleFlag());
                            orderGoodsDtos.add(orderGoodsSet);
                        }
                    }
                    if (null == settleUnitPrice)
                    {
                        settleUnitPrice = pGoods.getStandardPrice();
                    }
                    // 累计折前商品总价
                    bigStandardPrice = new BigDecimal(settleUnitPrice + "")
                            .multiply(new BigDecimal(og.getGoodsNumber()));
                    bigGoodsSettlePrice = bigStandardPrice;
                    bigGoodsRequiredPrice = bigStandardPrice;
                    if (channel == 2)
                    {
                        // 请求一点传奇支付商品项结算价
                        bigGoodsSettlePrice = new BigDecimal(og.getStandardPrice() + "")
                                .multiply(new BigDecimal(og.getGoodsNumber()));
                        settleUnitPrice = og.getStandardPrice();
                    }
                    bigGoodsPriceBeforeDiscount = bigGoodsPriceBeforeDiscount.add(bigStandardPrice);
                    goodsSettleFlag = pGoods.getGoodsSettleFlag();
                    goodsRebateFlag = pGoods.getGoodsRebateFlag();
                    if (goodsSettleFlag == CommonConst.GOODS_SETTLE_FLAG_TRUE)
                    {// 参与打折
                        // 累计打折商品总价
                        discountPrice = discountPrice.add(bigStandardPrice);

                        bigMemberDiscount = new BigDecimal(memberDiscount2 + "");
                        // 每个商品项参与结算的金额
                        bigGoodsSettlePrice = bigGoodsSettlePrice.multiply(bigMemberDiscount);
                        bigGoodsRequiredPrice = bigStandardPrice.multiply(bigMemberDiscount);
                    }
                    if (goodsRebateFlag == CommonConst.GOODS_REBATE_FLAG_TRUE)
                    { // 参与返点
                        rebatePrice = rebatePrice.add(bigGoodsSettlePrice);
                    }
                    og.setGoodsSettlePrice(bigGoodsSettlePrice.doubleValue());
                    og.setGoodsRequiredPrice(bigGoodsRequiredPrice.doubleValue());
                    og.setStandardPrice(settleUnitPrice);
                    // 累计折后商品总价
                    bigGoodsPrice = bigGoodsPrice.add(bigGoodsRequiredPrice);
                    // 最多取两个商品名称拼凑成订单标题
                    if (goodsItemIndex < 2)
                    {
                        orderTitle.append(pGoods.getGoodsName());
                        orderTitle.append("、");
                    }
                    goodsItemIndex++;
                    String specsDesc = og.getSpecsDesc();
                    if (StringUtils.isBlank(specsDesc))
                    {
                        specsDesc = pGoods.getSpecsDesc();
                    }
                    og.setGoodsIndex(goodsItemIndex);
                    og.setGoodsName(pGoods.getGoodsName());
                    og.setSpecsDesc(specsDesc);
                    og.setGoodsSettleFlag(goodsSettleFlag);
                    og.setUnitName(pGoods.getUnitName());
                    og.setGoodsImg(getGoodsImg(goodsId));
                    og.setGoodsRebateFlag(goodsRebateFlag);
                    og.setGoodsType(pGoods.getGoodsType());
                    og.setSettleUnitPrice(settleUnitPrice);
                }
                oGoodsList.addAll(orderGoodsDtos);
            }
        }
        else
        {// 优惠券订单价格
            CouponDto coupon = this.couponDao.getCouponById(order.getCouponId());
            Double couponPrice = Double.valueOf(coupon.getPrice() + "");
            Double couponValue = Double.valueOf(coupon.getValue() + "");
            Integer couponNum = order.getCouponNum();
            bigGoodsPrice = new BigDecimal(couponPrice + "")
                    .multiply(new BigDecimal((couponNum == null ? 1 : couponNum) + ""));
            bigGoodsPriceBeforeDiscount = new BigDecimal(couponValue + "")
                    .multiply(new BigDecimal((couponNum == null ? 1 : couponNum) + ""));
            orderTitle.append(coupon.getCouponName());
            orderTitle.append("、");
            if (oGoodsList != null && oGoodsList.size() > 0)
            {
                /*
                 * 计算单个商品项参与计算的金额，不查询商品表和限时折扣表，以会员价（vipPrice）计算
                 */
                for (OrderGoodsDto og : oGoodsList)
                {
                    og.setGoodsSettlePrice(bigGoodsPrice.doubleValue());
                }
            }
        }
        goodsPriceBeforeDiscount = bigGoodsPriceBeforeDiscount.doubleValue();
        goodsPrice = bigGoodsPrice.doubleValue();
        String orderTitle2 = null;
        if (orderTitle.length() > 0)
        {
            orderTitle2 = orderTitle.substring(0, orderTitle.lastIndexOf("、"));
        }
        else
        {// 订单标题为空
            int addOrEditFlag = order.getAddOrEditFlag();
            if (StringUtils.isBlank(order.getOrderTitle()) && addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
            {
                orderTitle2 = this.getOrderTitle(order);
            }
        }
        // 获取订单标题
        Integer orderSceneType = order.getOrderSceneType();
        if (orderSceneType == CommonConst.PLACE_ORDER_WM || orderSceneType == CommonConst.PLACE_ORDER_GOODS)
        {
            // 外卖订单和商品订单最低起订金额校验
            BigDecimal leastBookPrice = order.getLeastBookPrice();
            if (leastBookPrice != null && leastBookPrice.doubleValue() > 0)
            {
                CommonValidUtil.validDoubleGreaterThan(goodsPriceBeforeDiscount, leastBookPrice.doubleValue(),
                        CodeConst.CODE_LEAST_BOOK_PRICE_ERROR, CodeConst.MSG_LEAST_BOOK_PRICE_ERROR);
            }
        }
        // 场馆预定订单标题生成
        if (orderSceneType == CommonConst.PLACE_ORDER_VENUE)
        {
            order.setOrderTitle(orderTitle2);
            orderTitle2 = this.getOrderTitle(order);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsPrice", goodsPrice);
        map.put("goodsPriceBeforeDiscount", goodsPriceBeforeDiscount);
        map.put("orderTitle", orderTitle2);
        map.put("memberDiscount", memberDiscount);
        map.put("discountPrice", discountPrice.doubleValue());
        map.put("rebatePrice", rebatePrice.doubleValue());
        map.put("orderSettleType", shop.getOrderSettleType());
        return map;
    }

    /**
     * 验证扫码下单情况，资源是否预定
     */
    private void seatIdValid(OrderDto order) throws Exception
    {

        // 订单来源，=1为用户自助下单，!=1为非自助
        Integer orderSource = order.getOrderSource();
        String seatIds = order.getSeatIds();

        // 1:自助下单 需要验证是否占用资源
        if (orderSource != null && orderSource == 1 && StringUtils.isNotBlank(seatIds))
        {

            Long seatId = NumberUtil.stringToLong(seatIds);

            ShopRsrcDto resource = this.shopRsrcDao.getShopRsrcById(seatId);
            CommonValidUtil.validObjectNull(resource, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RESOURCE);
            // '资源使用状态：0（未被使用），1（已被使用），2（已被预订）'
            if (2 == resource.getResourceStatus() || 1 == resource.getResourceStatus())
            {// 商铺资源不可用
                throw new ValidateException(CodeConst.CODE_RESOURCE_STATUS_NOT_AVALIABLE,
                        CodeConst.MSG_RESOURCE_STATUS_NOT_AVALIABLE);
            }

            // 更新商铺资源状态
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", order.getUserId());
            param.put("resourceId", seatId);
            param.put("orderId", order.getOrderId());
            shopRsrcDao.useShopResource(param);
        }
    }

    @Override public void checkShopSeatValid(Long seatId, Long shopId) throws Exception
    {
        ShopRsrcDto resource = this.shopRsrcDao.getShopRsrcById(seatId);
        CommonValidUtil.validObjectNull(resource, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RESOURCE);
        if (shopId != null && !shopId.equals(resource.getShopId()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RESOURCE);
        }
        // '资源使用状态：0（未被使用），1（已被使用），2（已被预订）'
        if (2 == resource.getResourceStatus() || 1 == resource.getResourceStatus())
        {// 商铺资源不可用
            throw new ValidateException(CodeConst.CODE_RESOURCE_STATUS_NOT_AVALIABLE,
                    CodeConst.MSG_RESOURCE_STATUS_NOT_AVALIABLE);
        }
    }

    /**
     * 下订单校验商品列表数据合法性
     *
     * @param order
     * @param channel
     * @throws Exception
     */
    private void goodsListValid(OrderDto order, int channel) throws Exception
    {
        /*
         * 1,非app下单商品列表必填 2,app预订单，商品列表非空 3,app非预订单，商品列表非空
         */
        List<OrderGoodsDto> oGoodsList = order.getGoods();
        Long orderShopId = order.getShopId();
        if (CommonConst.ORDER_TYPE_COUPON == order.getOrderType())
        {// 购买优惠券订单
            Long couponId = order.getCouponId();
            CommonValidUtil
                    .validPositLong(couponId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_COUNPONID);
            Long num = this.couponDao.queryCouponExists(couponId);
            CommonValidUtil.validPositLong(num, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_COUPON);
        }
        int num = 0;
        int orderGoodsNumber = 0;
        Integer digitScale = null;
        if (CollectionUtils.isNotEmpty(oGoodsList))
        {
            // 商品列表不能为空
            CommonValidUtil.validListNull(oGoodsList, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OGOODS);
            // 验证订单商品列表
            Long goodsId = null;
            Long shopId = null;
            Double goodsNumber = null;
            for (OrderGoodsDto og : oGoodsList)
            {
                shopId = og.getShopId();
                goodsId = og.getGoodsId();
                goodsNumber = og.getGoodsNumber();
                // 验证每个订单中每个商品必填数据
                CommonValidUtil
                        .validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GOODS_SHOP);
                CommonValidUtil
                        .validPositLong(goodsId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OGOODS);
                // 是否是同一商铺中的商品
                CommonValidUtil.validLongEqual(orderShopId, shopId, CodeConst.CODE_NO_BELONG_TO,
                        CodeConst.MSG_NO_BELONG_TO_SHOP);
                if (goodsNumber == null || goodsNumber <= 0)
                {// 每件商品默认数量一件
                    og.setGoodsNumber(1D);
                    goodsNumber = 1D;
                }
                else
                {
                    // 验证商品数量是否满足计量单位允许的小数位数
                    digitScale = this.goodsDao.getDigitScaleOfGoodsUnit(goodsId);
                    CommonValidUtil.validDecimals(goodsNumber, digitScale, CodeConst.CODE_NOT_FLOAT_GNUM,
                            CodeConst.MSG_NOT_FLOAT_GNUM);
                }
                // 验证商品存在性
                num = this.goodsDao.validGoodsExists(shopId, goodsId);
                CommonValidUtil.validPositInt(num, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
                // 累计订单商品数量（支持小数的商品项以一个计算）
                orderGoodsNumber += (digitScale == null || digitScale == 0) ? goodsNumber : 1;
            }
        }
        order.setGoodsNumber(orderGoodsNumber <= 0 ? null : orderGoodsNumber);
    }

    /**
     * 验证优惠券存在性
     * <p>
     * 先查redis，再查库表
     * </p>
     *
     * @param couponId
     * @return
     * @throws Exception
     */
    private Long queryCouponExistsFast(Long couponId) throws Exception
    {
        String couponRedis = DataCacheApi.get(CommonConst.KEY_COUPON + couponId);
        if (!StringUtils.isBlank(couponRedis))
        {
            CouponDto coupon = (CouponDto) JacksonUtil.jsonToObject(couponRedis, CouponDto.class, null);
            return coupon.getCouponId();
        }
        else
        {
            return this.couponDao.queryCouponExists(couponId);
        }
    }

    private int queryGoodsExistsFast(Long shopId, Long goodsId) throws Exception
    {
        String goodsRedis = DataCacheApi.get(CommonConst.KEY_GOODS + shopId + ":" + goodsId);
        if (!StringUtils.isBlank(goodsRedis))
        {
            return 1;
        }
        else
        {
            return this.goodsDao.validGoodsExists(shopId, goodsId);
        }
    }

    /**
     * 获取每个商品的折扣
     *
     * @param discount
     * @return
     * @throws Exception
     */
    private Float getGoodsDiscount(TimedDiscountGoodsDto discount) throws Exception
    {
        if (discount == null)
        {
            return 1F;
        }
        String periodType = discount.getDiscountPeriodType(); // 折扣时间类型
        boolean flag;
        if (StringUtils.equals(periodType, CommonConst.PERIOD_TYPE_DAILY))
        { // 每天
            flag = CommonValidUtil.validCurTimeOfDateRange(discount.getDayFromTime(), discount.getDayToTime());
            return flag == true ? discount.getDiscount() : 1;
        }
        else if (StringUtils.equals(periodType, CommonConst.PERIOD_TYPE_WEEKLY))
        { // 每周
            int weekDay = DateUtils.getDayOfWeek();
            String weekDayCn = discount.getWeek();
            switch (weekDay)
            {
                case Calendar.SUNDAY + 6:
                    if (StringUtils.equals(CommonConst.CN_SUNDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.MONDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_MONDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.TUESDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_TUESDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.WEDNESDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_WEDNESDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.THURSDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_THURSDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.FRIDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_FRIDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                case Calendar.SATURDAY - 1:
                    if (StringUtils.equals(CommonConst.CN_SATURDAY, weekDayCn))
                    {
                        return getDiscountWeekly(discount);
                    }
                    else
                    {
                        return 1F;
                    }
                default:
                    return 1F;
            }
        }
        else if (StringUtils.equals(periodType, CommonConst.PERIOD_TYPE_CUSTOM))
        { // 自定义
            flag = CommonValidUtil
                    .validCurDateTimeOfRange(discount.getCustomFromDatetime(), discount.getCustomToDatetime());
            return flag == true ? discount.getDiscount() : 1F;
        }
        return 1F;

    }

    /**
     * 获取周折扣
     *
     * @param discount
     * @return
     * @throws Exception
     */
    private Float getDiscountWeekly(TimedDiscountGoodsDto discount) throws Exception
    {
        boolean flag = CommonValidUtil.validCurTimeOfDateRange(discount.getWeekFromTime(), discount.getWeekToTime());
        return flag == true ? discount.getDiscount() : 1;
    }

    public void updateOrderMaling(OrderDto order) throws Exception
    {
        // 商品列表数据校验
        goodsListValid(order, 1);
        // 商品列表
        List<OrderGoodsDto> oGoodsList = order.getGoods();
        // 服务费
        Double logisticsPrice = order.getLogisticsPrice();
        // 折前商品总价
        Double goodsPriceBeforeDiscount = 0D;
        // 折后商品总价
        Double goodsPrice = 0D;
        // 返点商品总价
        Double rebatePrice = 0D;
        // 抹零价
        Double maling = order.getMaling();
        BigDecimal bigLogisticsPrice = new BigDecimal((logisticsPrice == null ? 0 : logisticsPrice) + "");
        // 计算订单金额
        if (!CommonValidUtil.validListNull(oGoodsList))
        {
            Map<String, Object> map = getOrderPrice(order, 2);
            goodsPrice = (Double) map.get("goodsPrice");
            goodsPriceBeforeDiscount = (Double) map.get("goodsPriceBeforeDiscount");
            order.setOrderTitle((String) map.get("orderTitle"));
            rebatePrice = (Double) map.get("rebatePrice");
            oGoodsList = order.getGoods();
            // 订单金额
            order.setGoodsPriceBeforeDiscount(goodsPriceBeforeDiscount);
            order.setGoodsPrice(goodsPrice);
            order.setSettleType((Integer) map.get("orderSettleType"));
            // 订单总价 = 商品折前价 + 服务费
            order.setOrderTotalPrice(new BigDecimal(goodsPrice + "").add(bigLogisticsPrice).doubleValue());
            // 删除旧的订单商品列表
            this.orderGoodsDao.delGoodsByOrderId(order.getOrderId());
            oGoodsList = order.getGoods();
            for (OrderGoodsDto og2 : oGoodsList)
            {
                og2.setOrderId(order.getOrderId());
                this.orderGoodsDao.saveOrderGoods(og2);
            }
        }
        else
        {
            // 商品列表为空，则订单金额为空
            order.setGoodsPriceBeforeDiscount(null);
            order.setGoodsPrice(null);
            order.setOrderTotalPrice(null);
            order.setSettleType(CommonConst.ORDER_SETTLE_ONE);
        }
        // 订单结算金额
        if (maling != null && maling > 0)
        {
            order.setSettlePrice(maling);
        }

        order.setOrderRealSettlePrice(NumberUtil.min(order.getSettlePrice(), rebatePrice));
        order.setLastUpdateTime(new Date());
        // 修改订单信息
        orderDao.updateOrder(order);

        if (CommonConst.ORDER_STS_YJZ == order.getOrderStatus())
        {
            // 修改库存
            storageService.insertShopStorageByOrderId(order.getOrderId(), order.getShopId());
        }
    }

    public int updateUserId(Map<String, String> updateParam) throws Exception
    {
        return orderDao.updateUserId(updateParam);
    }

    public OrderDto updateOrder(OrderDto order) throws Exception
    {
        // 订单商品列表验证
        goodsListValid(order, 1);
        // 订单ID
        String orderId = order.getOrderId();
        // 商品列表
        List<OrderGoodsDto> oGoodsList = order.getGoods();
        // 服务费
        Double logisticsPrice = order.getLogisticsPrice();
        // 折前商品总价
        Double goodsPriceBeforeDiscount = 0D;
        // 折后商品总价
        Double goodsPrice = 0D;
        // 返点商品总价
        Double rebatePrice = 0D;
        // 抹零价
        Double maling = order.getMaling();
        // 会员折扣
        Double memberDiscount = null;
        BigDecimal bigGoodsPrice = null;
        BigDecimal bigLogisticsPrice = new BigDecimal((logisticsPrice == null ? 0 : logisticsPrice) + "");
        if (!CommonValidUtil.validListNull(oGoodsList))
        {// 订单中具备商品列表
            Map<String, Object> map = getOrderPrice(order, 1);
            goodsPrice = (Double) map.get("goodsPrice");
            goodsPriceBeforeDiscount = (Double) map.get("goodsPriceBeforeDiscount");
            order.setOrderTitle((String) map.get("orderTitle"));
            order.setSettleType((Integer) map.get("orderSettleType"));
            // 设置参与打折商品的总价
            rebatePrice = (Double) map.get("rebatePrice");
            oGoodsList = order.getGoods();
            memberDiscount = (Double) map.get("memberDiscount");
        }
        else
        {// 订单没有商品列表
            int addOrEditFlag = order.getAddOrEditFlag();
            if (addOrEditFlag == CommonConst.HANDLE_FLAG_ADD)
            {
                order.setOrderTitle(getOrderTitle(order));
            }
            order.setSettleType(CommonConst.ORDER_SETTLE_ONE);
            memberDiscount = this.shopDao.getMemberDiscount(order.getShopId());
        }
        bigGoodsPrice = new BigDecimal(goodsPrice + "");
        if (maling != null && maling > 0)
        {
            order.setGoodsPrice(maling); // 折后商品总价
            order.setSettlePrice(maling); // 参与结算的总价 = 折后商品总价 + 服务费
            order.setOrderTotalPrice(maling); // 订单总价
        }
        else
        {
            if (CommonValidUtil.validListNull(oGoodsList))
            {

                // 订单商品列表为空，各种金额也为空
                goodsPriceBeforeDiscount = null; // 折前商品总价
                order.setGoodsPrice(null);
                order.setOrderTotalPrice(null);
                // 参与结算的总价 = 折后商品总价 + 服务费
                Integer orderType = order.getOrderType();
                if (orderType == CommonConst.ORDER_TYPE_REPAY)
                {
                    Double prepayAmount = order.getPrepayMoney();
                    order.setSettlePrice(prepayAmount);
                }
                else
                {
                    order.setSettlePrice(null);
                }
            }
            else
            {

                // 订单商品列表不为空，各种金额由商品列表计算得到
                order.setGoodsPrice(goodsPrice);
                // order.setSettlePrice(bigGoodsPrice.add(bigLogisticsPrice).doubleValue());
                // 参与结算的总价 = 折后商品总价 + 服务费
                Integer orderType = order.getOrderType();
                if (orderType == CommonConst.ORDER_TYPE_REPAY)
                {
                    Double prepayAmount = order.getPrepayMoney();
                    double settlePrice = bigGoodsPrice.add(bigLogisticsPrice).doubleValue();
                    order.setSettlePrice(prepayAmount >= settlePrice ? prepayAmount : settlePrice);
                }
                else
                {
                    order.setSettlePrice(bigGoodsPrice.add(bigLogisticsPrice).doubleValue());
                }
                order.setOrderTotalPrice(order.getSettlePrice());
            }
        }
        order.setOrderId(orderId);
        order.setGoodsPriceBeforeDiscount(goodsPriceBeforeDiscount);
        order.setOrderRealSettlePrice(NumberUtil.min(rebatePrice, order.getSettlePrice()));
        order.setInputAddress(getAddress(order.getAddressId()));
        order.setLastUpdateTime(new Date());
        order.setMemberDiscount(memberDiscount);
        order.setServerLastTime(System.currentTimeMillis());

        // 记录手机号码
        Long userId = order.getUserId();
        if (userId != null)
        {
            UserDto userDto = userDao.getUserById(userId);
            order.setMobile(userDto.getMobile() == null ? null : userDto.getMobile());
        }

        int flag = order.getAddOrEditFlag();
        if (flag == CommonConst.HANDLE_FLAG_ADD)
        {// 新增订单
            this.orderDao.saveOrder(order);
        }
        else
        {// 修改订单
            this.orderDao.updateOrder(order);
            if (oGoodsList != null && oGoodsList.size() > 0)
            {
                // 删除旧的订单商品列表
                this.orderGoodsDao.delGoodsByOrderId(orderId);
            }

            if (CommonConst.ORDER_STS_YJZ == order.getOrderStatus())
            {
                // 修改库存
                storageService.insertShopStorageByOrderId(orderId, order.getShopId());
            }
        }
        /*
         * 保存订单商品列表
         */
        if (oGoodsList != null && oGoodsList.size() > 0)
        {
            for (OrderGoodsDto og2 : oGoodsList)
            {
                og2.setOrderId(orderId);
                this.orderGoodsDao.saveOrderGoods(og2);
            }
        }
        // 商品订单价格不变
        // // 抹零价和商品折后价不等，按比例修改商品参与结算的金额
        // if (maling != null && maling > 0 && maling.doubleValue() !=
        // goodsPrice.doubleValue())
        // {
        // Double ratio = new BigDecimal(maling + "").divide(bigGoodsPrice, 4,
        // BigDecimal.ROUND_HALF_UP).doubleValue();
        // this.orderGoodsDao.updateSettlePrice(orderId, ratio);
        // }
        OrderDto pOrder = this.orderDao.getOrderById(orderId);
        return pOrder;
    }

    /**
     * 获取订单标题
     * <p/>
     * 目前只支持餐饮预定到店消费和场馆服务订单：<br>
     * 1，餐饮预定到店消费订单已预定资源类型作为标题；<br>
     * 2，场馆订单有预定资源所属商品分类名称拼凑而成。
     *
     * @param order
     * @return
     * @throws Exception
     */
    private String getOrderTitle(OrderDto order) throws Exception
    {
        Integer orderSceneType = order.getOrderSceneType();
        String orderId = order.getOrderId();
        String orderTitle = order.getOrderTitle();
        Integer orderSource = order.getOrderSource();
        if (orderSceneType == CommonConst.PLACE_ORDER_LIVE && (orderSource == null || orderSource == 0))
        {// 餐饮预定到店消费
            // 获取预定资源类型(resourceType)作为订单标题
            String resourceType = this.orderShopRsrcDao.getResourceTypeByOrderId(orderId);
            orderTitle = resourceType;
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_VENUE)
        {// 场馆订单
            // 若是场馆预定，获取商品分类作为标题
            Map<String, Object> orderShopResource = this.orderShopRsrcDao.getResourceByOrderId(orderId);
            if (orderShopResource != null)
            {
                String resourceType = (String) orderShopResource.get("resourceType");
                Long resourceId = (Long) orderShopResource.get("bizId");
                // Long categoryId =
                // this.shopRsrcDao.getCategoryIdByRsrId(resourceId);
                Map<String, Object> rsrMap = this.shopRsrcDao.getCategoryIdAndRsrNameByRsrId(resourceId);
                List<String> categoryList = new ArrayList<String>();
                Long categoryId = null;
                String rsrName = null;
                if (rsrMap != null)
                {
                    categoryId = (Long) rsrMap.get("categoryId");
                    rsrName = (String) rsrMap.get("resourceName");
                    categoryList.add(rsrName);
                }
                // 商品分类列表（包含层级）
                Map<String, Object> map = null;
                // 循环获取商品分类名称（包含上下级）
                do
                {
                    map = this.goodsCategoryDao.getGoodsCategoryById(categoryId);
                    categoryId = null;
                    if (map != null)
                    {
                        categoryList.add((String) map.get("categoryName"));
                        categoryId = (Long) map.get("pId");
                    }
                }
                while (categoryId != null && categoryId > 0L);
                // 倒排商品列表，以空格隔开作为订单标题
                if (categoryList != null && categoryList.size() > 0)
                {
                    StringBuilder orderTitle2 = new StringBuilder();
                    for (int i = categoryList.size() - 1; i >= 0; i--)
                    {
                        orderTitle2.append(categoryList.get(i));
                        orderTitle2.append(" ");
                    }
                    if (orderTitle2.length() > 0)
                    {
                        orderTitle = orderTitle2.substring(0, orderTitle2.lastIndexOf(" "));
                    }
                }
            }
        }
        else if (orderSceneType == CommonConst.PLACE_ORDER_BEAUTY)
        {// 丽人订单
            // 丽人场景以商品名称作为标题，若前面已经得到订单标题则直接返回
            orderTitle = order.getOrderTitle();
        }
        return orderTitle;

    }

    public OrderDetailDto wrapOrderDetail(String orderId, Long userId) throws Exception
    {
        // 获取订单
        OrderDto pModel = this.orderDao.getOrderById(orderId);
        if (pModel == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER);
        }
        OrderGoodsDto og = new OrderGoodsDto();
        og.setOrderId(orderId);
        // 获取订单商品
        List<OrderGoodsDto> ogList = this.orderGoodsDao.getOGoodsListByOrderId(og);
        String url = null;
        if (ogList != null && ogList.size() > 0)
        {
            String proxyServer = FdfsUtil.getFileProxyServer();
            for (OrderGoodsDto goods : ogList)
            {
                url = goods.getGoodsImg();
                if (!StringUtils.isBlank(url))
                {
                    goods.setGoodsImg(FdfsUtil.getFileFQN(proxyServer, url));
                }
                goods.setMemberPrice(NumberUtil.divide(goods.getGoodsSettlePrice(), goods.getGoodsNumber()));
            }
        }
        pModel.setGoods(ogList);
        // 商品数量
        OrderDetailDto orderDetail = new OrderDetailDto();
        PropertyUtils.copyProperties(orderDetail, pModel);
        // app显示的订单总价是折前价 modify date:20150902
        orderDetail.setPrepayMoney(pModel.getPrepayMoney());
        orderDetail.setSettlePrice(pModel.getSettlePrice());
        orderDetail.setLogisticsPrice(pModel.getLogisticsPrice());
        orderDetail.setMemberTotalPrice(pModel.getSettlePrice());
        pModel.setOrderRealSettlePrice(pModel.getSettlePrice());
        if (orderDetail.getPayStatus() != CommonConst.PAY_STATUS_PAY_SUCCESS)
        {
            // 新增业务逻辑 非会员订单
            switchOrder(orderId, userId, pModel, ogList, orderDetail);
        }
        // 已支付金额
        BigDecimal payedAmount = this.packetDao.queryOrderPayAmount(orderId, CommonConst.PAY_TYPE_SINGLE);
        // 订单总价
        // Double totalPrice = pModel.getOrderTotalPrice();
        // 未支付金额
        Double notPayedAmount = null;
        BigDecimal settlePrice = packetDao.queryOrderAmount(orderId);
        // orderDetail.setGoodsNumber(goodsNumber);
        orderDetail.setPayedAmount(payedAmount.doubleValue());
        if (settlePrice != null && settlePrice.doubleValue() > payedAmount.doubleValue())
        {
            notPayedAmount = settlePrice.subtract(payedAmount).doubleValue();
        }
        else
        {
            notPayedAmount = 0D;
        }

        orderDetail.setNotPayedAmount(notPayedAmount < 0 ? 0 : notPayedAmount);
        orderDetail.setSeatName(getSeatName(pModel.getSeatIds()));

        // 设置shopLogoUrl（相对路径）
        AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
        attachmentRelationDto.setBizId(pModel.getShopId());
        attachmentRelationDto.setBizType(1);
        attachmentRelationDto.setPicType(1);
        List<AttachmentRelationDto> attachmentList = attachmentRelationDao.findByCondition(attachmentRelationDto);
        if (CollectionUtils.isNotEmpty(attachmentList))
        {
            String logoUrl = attachmentList.get(0).getFileUrl();
            orderDetail.setShopLogoUrl(FdfsUtil.getFileProxyPath(logoUrl));
        }
        Map param = new HashMap();
        param.put("orderId", orderId);
        List<Map> orderPays = collectDao.getOrderPayDetail(param);
        orderDetail.setPayList(orderPays);
        //        order.put("payList", orderPays);
        return orderDetail;
    }

    private String getSeatName(String seatIdStr)
    {
        String seatName = null;
        if (StringUtils.isNotBlank(seatIdStr))
        {
            String[] seatIds = seatIdStr.split(CommonConst.COMMA_SEPARATOR);
            List<String> seatNames = shopRsrcDao.getSeatNameBySeatIds(seatIds);
            if (CollectionUtils.isNotEmpty(seatNames))
            {
                StringBuffer sb = new StringBuffer();
                for (String str : seatNames)
                {
                    sb.append(str).append(CommonConst.COMMA_SEPARATOR);
                }
                seatName = sb.toString().substring(0, sb.length() - 1);
            }
        }
        return seatName;
    }

    /**
     * 将订单由会员转非会员同时计算会员价
     *
     * @param orderId     订单id
     * @param userId      用户id
     * @param pModel      订单对象
     * @param ogList      商品列表
     * @param orderDetail 订单详情
     * @author shengzhipeng
     */
    private void switchOrder(String orderId, Long userId, OrderDto pModel, List<OrderGoodsDto> ogList,
            OrderDetailDto orderDetail) throws Exception
    {
        Boolean isUpdate = false;
        Integer isMember = pModel.getIsMember();
        if (isMember != null && isMember == 0)
        {
            List<Long> orderGoodsIds = new ArrayList<Long>();
            Double memberDiscount = null;
            if (CollectionUtils.isNotEmpty(ogList))
            {
                Long shopId = pModel.getShopId();
                memberDiscount = this.shopDao.getMemberDiscount(shopId);
                if (memberDiscount == null || memberDiscount <= 0D)
                {
                    memberDiscount = 1D;
                }
                BigDecimal memberTotalPrice = new BigDecimal(0);
                BigDecimal orderRealSettlePrice = new BigDecimal(0);
                for (OrderGoodsDto orderGoods : ogList)
                {
                    Long orderGoodsId = orderGoods.getId();
                    Integer settleFlag = orderGoods.getGoodsSettleFlag();
                    Integer rebateFlag = orderGoods.getGoodsRebateFlag();
                    BigDecimal goodsSettlePrice = new BigDecimal(0);
                    if (null != settleFlag && settleFlag == CommonConst.GOODS_SETTLE_FLAG_TRUE)
                    {
                        goodsSettlePrice = new BigDecimal(orderGoods.getGoodsSettlePrice() + "")
                                .multiply(new BigDecimal(memberDiscount + ""));
                        orderGoodsIds.add(orderGoodsId);
                    }
                    else
                    {
                        goodsSettlePrice = new BigDecimal(orderGoods.getGoodsSettlePrice() + "");
                    }
                    if (null != rebateFlag && rebateFlag == CommonConst.GOODS_REBATE_FLAG_TRUE)
                    {
                        orderRealSettlePrice = orderRealSettlePrice.add(goodsSettlePrice);
                    }
                    memberTotalPrice = memberTotalPrice.add(goodsSettlePrice);
                }
                pModel.setOrderRealSettlePrice(memberTotalPrice.compareTo(orderRealSettlePrice) < 0 ? memberTotalPrice
                        .doubleValue() : orderRealSettlePrice.doubleValue());
                orderDetail.setMemberTotalPrice(memberTotalPrice.doubleValue());
            }

            if (userId != null)
            {
                UserDto user = userDao.getUserById(userId);
                CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
                if (!userId.equals(pModel.getUserId()))
                {
                    // 非会员，修改订单拥有人
                    pModel.setOrderId(orderId);
                    pModel.setUserId(userId);
                    pModel.setLastUpdateTime(new Date());
                    orderDetail.setUserId(userId);
                    pModel.setServerLastTime(System.currentTimeMillis());
                    isUpdate = true;
                }
                if (user.getIsMember() == CommonConst.USER_IS_MEMBER)
                {
                    pModel.setSettlePrice(orderDetail.getMemberTotalPrice());
                    pModel.setIsMember(CommonConst.USER_IS_MEMBER);
                    pModel.setMobile(user.getMobile());
                    pModel.setMemberDiscount(memberDiscount);
                    // 修改返回的结果中订单为会员订单和会员结算价
                    orderDetail.setSettlePrice(orderDetail.getMemberTotalPrice());
                    orderDetail.setIsMember(CommonConst.USER_IS_MEMBER);
                    if (CollectionUtils.isNotEmpty(orderGoodsIds))
                    {
                        orderGoodsDao.batchUpdateSettlePriceBy(orderGoodsIds, memberDiscount);
                    }
                    pModel.setServerLastTime(System.currentTimeMillis());
                    isUpdate = true;
                }
                // 更新
                if (isUpdate)
                {
                    orderDetail.setIsUpdate(isUpdate);
                    orderDao.updateOrder(pModel);
                }
            }
        }
    }

    public OrderDto getOrderById(String orderId, int handle) throws Exception
    {
        // 获取订单
        OrderDto pModel = this.orderDao.getOrderById(orderId);
        if (pModel == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER);
        }
        if (handle == 1)
        {
            OrderGoodsDto og = new OrderGoodsDto();
            og.setOrderId(orderId);
            // 获取订单商品
            List<OrderGoodsDto> ogList = this.orderGoodsDao.getOGoodsListByOrderId(og);
            if (ogList == null || ogList.size() <= 0)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_OGOODS);
            }
            pModel.setGoods(ogList);
        }
        return pModel;
    }

    public OrderDto getOrderById(String orderId) throws Exception
    {
        // 获取订单
        OrderDto pModel = this.orderDao.getOrderById(orderId);
        OrderGoodsDto og = new OrderGoodsDto();
        og.setOrderId(orderId);
        // 获取订单商品
        List<OrderGoodsDto> ogList = this.orderGoodsDao.getOGoodsListByOrderId(og);
        pModel.setGoods(ogList);
        return pModel;
    }

    public OrderDto getOrderDtoById(String orderId) throws Exception
    {
        return this.orderDao.getOrderById(orderId);
    }

    public BigDecimal queryOrderAmount(String orderId) throws Exception
    {
        return packetDao.queryOrderAmount(orderId);
    }

    public OrderDto getOrderMainById(String orderId) throws Exception
    {
        return this.orderDao.getOrderMainById(orderId);
    }

    @Override public Double settleOrder(OrderDto order) throws Exception
    {
        String orderId = order.getOrderId();
        order.setOrderStatus(CommonConst.ORDER_STATUS_FINISH);
        Double sendMoney = Double.valueOf(0);
        if (order.getIsMember() == 1)
        {
            sendMoney = packetService.sendRedPacketToUser(order);
            if (sendMoney != 0)
            {
                // 更新订单结算价格
                Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
                if (orderRealSettlePrice < 0)
                {
                    orderRealSettlePrice = 0D;
                }
                order.setOrderRealSettlePrice(orderRealSettlePrice);
                order.setSendRedPacketMoney(sendMoney);
                orderDao.updateOrder(order);
            }
            OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, CommonConst.ORDER_PAY_TYPE_SINGLE);
        }
        else if (order.getIsMember() == 0)
        {
            Double payAmount = payDao.getSumPayAmount(orderId, null);
            generatePlatformBill(order, CommonConst.PLT_BILL_MNY_SOURCE_CQB, payAmount);
            generateShopMiddleBill(order, payAmount);
            OrderGoodsSettleUtil.detailSingleXorder(orderId);
            handleAccountingStatByUser(order);
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_ORDER_NOT_EXIST, "订单会员状态错误无法结算");
        }

        Date now = new Date();
        order.setServerLastTime(now.getTime());
        orderDao.updateOrder(order);

        collectDao.updateShopResourceStatus(orderId, CommonConst.RESOURCE_STATUS_NOT_IN_USE);

        return sendMoney;
    }

    @Override public double countVoucherDeduction(Long userId, OrderDto orderDto) throws Exception
    {
        if (userId == null || orderDto == null)
        {
            return 0;
        }

        ShopDto orderShop = shopDao.getShopById(orderDto.getShopId());
        if (orderShop.getShopIdentity() != null && orderShop.getShopIdentity() != 2)
        {
            return 0;
        }

        ConfigQueryCondition queryCondition = new ConfigQueryCondition();
        queryCondition.setBizType(1);
        queryCondition.setBizId(orderDto.getShopId());
        queryCondition.setConfigKeys(new String[] { "voucher_amount_used_min_money", "voucher_used_ratio" });
        PageResult<ConfigDto> result = commonService.queryForConfig(queryCondition);

        List<ConfigDto> configList = result.getLst();
        if (CollectionUtils.isEmpty(configList))
        {
            logger.info("代金券使用配置为空，代金券可用余额为0");
            return 0;
        }
        Map<String, Object> configMap = new HashMap<String, Object>();

        for (ConfigDto config : configList)
        {
            configMap.put(config.getConfigKey(), config.getConfigValue());
        }

        Object voucherAmountLimitConfig = configMap.get("voucher_amount_used_min_money");
        Double voucherAmountLimit =
                voucherAmountLimitConfig == null ? 0 : Double.valueOf(voucherAmountLimitConfig.toString());

        Object voucherUsedRatioConfig = configMap.get("voucher_used_ratio");
        Double voucherUsedRatio =
                voucherUsedRatioConfig == null ? 0 : Double.valueOf(voucherUsedRatioConfig.toString());

        UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
        Double voucherAmount = userAccount.getVoucherAmount();
        if (voucherAmount <= voucherAmountLimit)
        {
            logger.info("代金券不满足使用条件，代金券可用余额为0");
            return 0;
        }

        Double settlePrice = orderDto.getSettlePrice();
        Double voucherAvaliUsedAmount = NumberUtil.multiply(voucherUsedRatio, settlePrice);

        return voucherAvaliUsedAmount <= voucherAmount ? voucherAvaliUsedAmount : voucherAmount;
    }

    public Integer cancelOrder(OrderDto order) throws Exception
    {
        String reason = null;
        String orderId = order.getOrderId();
        order = orderDao.getOrderById(orderId);
        CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
        // 订单未支付或支付失败允许取消订单
        if (CommonConst.PAY_STATUS_PAY_CANCEL == order.getPayStatus())
        {
            throw new ValidateException(CodeConst.CODE_PAY_STATUS_CANCEL, "订单状态为已取消,不允许取消订单");
        }
        int orderStatus = order.getOrderStatus();

        // 订单状态为已预订或待确认才能退单
        if (CommonConst.ORDER_STS_YYD != orderStatus && CommonConst.ORDER_STS_DQR != orderStatus)
        {
            throw new ValidateException(CodeConst.CODE_ORDER_STATUS_NOT_SUPPORT_CANCEL, "只有订单状态为已预订或待确认才能退单");
        }
        // 默认退单中
        int status = CommonConst.ORDER_STS_TDZ;
        // 待取消状态自动退为已退单
        // 获取商铺设置
        Map<String, Object> setting = null;

        // 订单场景分类：1（到店点菜订单,非外卖订单），2（外卖订单），3（服务订单），4（商品订单）
        if (order.getOrderSceneType() != null)
            setting = orderDao.getDistributionTakeoutSetting(order.getShopId(), order.getOrderSceneType());
        logger.info("=====================获取商铺设置：" + setting + " \n商铺ID：" + order.getShopId());

        // 1.判断商铺是否参与自动退单功能,不参与则退单中；2.待确认的单可直接退； 3.餐饮类的直接退
        if (shopSupportAutoCancelOrder(setting) || CommonConst.ORDER_STS_DQR == orderStatus
                || order.getOrderSceneType() == 1)
        {
            // 判断是否在自动退单时间前，在的话自动退单，否则退单中
            if (userCancelOrderInAutoTime(setting, order) || CommonConst.ORDER_STS_DQR == orderStatus
                    || order.getOrderSceneType() == 1)
            {
                // 设置状态已退单
                status = CommonConst.ORDER_STS_YTD;
                reason = "用户取消订单";
                // 退款
                logger.info("=====================用户自动退单条件满足，开始退款...");
                collectService.dealRefund(order);
                logger.info("=====================用户自动退单条件满足，退款完毕。");
            }

        }

        // 更新订单状态
        logger.info("==================更新订单状态：" + OrderStatusEnum.getStatusName(status));
        updateOrderStatus(order, status, reason);
        // 记录订单日志表
        saveOrderLog(orderId, "订单退订", null);
        return status;
    }

    // 判断是否在自动退单时间前
    private boolean userCancelOrderInAutoTime(Map<String, Object> setting, OrderDto order)
    {

        if (setting == null)
            return false;

        // 不允许自动退的时间大小，单位分
        Long cancelBeforeMinute = (Long) setting.get("cancelBeforeMinute");
        logger.debug("商铺设置的自动退单时间：" + cancelBeforeMinute);

        // 预定时间
        Date reserveTime = getUserReserveTime(order);
        logger.debug("用户下单的预定时间：" + DateUtils.format(reserveTime, null));

        // 计算预定时间与当前时间间隔多久
        int intervalTime = computeIntervalTime(reserveTime);
        logger.debug("计算预定时间与当前时间间隔:" + intervalTime + " 分");

        if (intervalTime > cancelBeforeMinute)
            return true;

        return false;
    }

    // 计算预定时间与当前时间间隔多久
    private static int computeIntervalTime(Date reserveTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long start = calendar.getTimeInMillis();
        calendar.setTime(reserveTime);
        long end = calendar.getTimeInMillis();

        return (int) (end - start) / 60000;
    }

    // 获取用户订单的预定时间
    private Date getUserReserveTime(OrderDto order)
    {

        // 订单场景分类：1（到店点菜订单,非外卖订单），2（外卖订单），3（服务订单），4（商品订单）
        if (order.getOrderSceneType() == 2)
        {
            return order.getDistributionTime();
        }

        // 下面的是到店服务

        Map<String, Object> map = orderDao.getUserReserveTime(order.getOrderId());

        // 如果没有预定时间，默认为当前时间
        if (map == null)
        {
            return new Date();
        }

        // 预定日期
        Date date = (Date) map.get("reserve_resource_date");
        // 预定时间
        Date time = (Date) map.get("start_time");

        Date userReserveTime = DateUtils.parse(DateUtils.getDate(date) + " " + DateUtils.getTime(time));

        return userReserveTime;
    }

    // 商铺是否参与自动退单功能
    private boolean shopSupportAutoCancelOrder(Map<String, Object> setting)
    {

        // 商铺没有设置则默认不支持自动退单
        if (setting == null)
            return false;

        Integer cancelAnyTime = (Integer) setting.get("cancelAnyTime");

        // 是否参与随时退，是=1,否=0
        if (cancelAnyTime != null && cancelAnyTime == 1)
            return true;
        return false;
    }

    // 更新订单状态
    private void updateOrderStatus(OrderDto order, int status, String reason) throws Exception
    {
        order.setOrderStatus(status);
        order.setIsActiveRefund(1);// 用户主动退单
        order.setLastUpdateTime(new Date());
        if (reason != null)
            order.setRefuseReason(reason);
        this.orderDao.updateOrder(order);

        if (CommonConst.ORDER_STS_YJZ == order.getOrderStatus())
        {
            // 修改库存
            storageService.insertShopStorageByOrderId(order.getOrderId(), order.getShopId());
        }
    }

    /**
     * @param orderShopRsrc
     * @throws Exception
     */
    public void reserveShopRsrcApp(OrderShopRsrcDto orderShopRsrc) throws Exception
    {
        // 检查资源是否可用
        boolean flag = checkResourceValid(orderShopRsrc);
        if (!flag)
        {
            throw new ValidateException(CodeConst.CODE_RESOURCE_STATUS_NOT_AVALIABLE,
                    CodeConst.MSG_RESOURCE_STATUS_NOT_AVALIABLE);
        }
        String orderId = orderShopRsrc.getOrderId();
        // 检查订单是否绑定了旧的商铺资源
        Integer count = this.osrDao.getOrderShopRsrcCount(orderId);
        if (count != null && count > 0)
        {
            // 订单绑定了旧的资源，则删除旧的资源
            this.osrDao.delOrderShopRsrc(orderId);
        }
        // 保存预定资源
        this.osrDao.saveOrderShopRsrc(orderShopRsrc);
    }

    /**
     * 检查资源是否可预定
     *
     * @param orderShopRsrc
     * @return
     * @throws Exception
     */
    private boolean checkResourceValid(OrderShopRsrcDto orderShopRsrc) throws Exception
    {
        boolean flag = false;
        if ("3".equals(orderShopRsrc.getResourceType()))
        {// 预定场地类资源
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shopId", orderShopRsrc.getShopId());
            map.put("resourceId", orderShopRsrc.getBizId());
            map.put("resourceDate", DateUtils.format(orderShopRsrc.getReserveResourceDate(), DateUtils.DATE_FORMAT));
            map.put("beginTime", orderShopRsrc.getStartTime());
            map.put("endTime", orderShopRsrc.getEndTime());
            flag = this.shopDao.isUsedResource(map);
        }
        return !flag;
    }

    // 方法备份，目前收银机会使用此接口，APP预订使用reserveShopRsrcApp
    public void reserveShopRsrc(OrderShopRsrcDto orderShopRsrc) throws Exception
    {
        // 订单号不能为空
        String orderId = orderShopRsrc.getOrderId();
        CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        // 预定资源列表不能为空
        List<OsrsDto> osrList = orderShopRsrc.getOsrs();
        CommonValidUtil.validListNull(osrList, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_OSRCS);
        // 商铺存在性
        long shopId = orderShopRsrc.getShopId();
        ShopDto pShop = this.shopDao.getNormalShopById(shopId);
        CommonValidUtil.validObjectNull(pShop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        // 验证商铺处于营业时间
        CommonValidUtil.validCurTimeOfDateRange(pShop.getStartBTime(), pShop.getStopBTime(),
                CodeConst.CODE_NOT_IN_OPEN_DATERANGE, CodeConst.MSG_NOT_IN_OPENTIME);
        // 验证
        Long userId = orderShopRsrc.getUserId();
        String userName = orderShopRsrc.getUserName();
        String mobile = orderShopRsrc.getMobile();
        String startTime = orderShopRsrc.getStartTime();
        String endTime = orderShopRsrc.getEndTime();
        String resourceType = orderShopRsrc.getResourceType();

        Long intevalId = 0L; // 可预订时段ID
        Integer resourceNumber = 0; // 预订资源数量
        Long ruleId = 0L; // 预订规则ID
        ShopTimeIntDto timeInteval = null;
        String[] intevals = null; // 暂停预订日期
        String[] timePoint = null;
        Date startDate = null; // 暂停预定开始时间
        Date endDate = null; // 暂停预定结束时间
        Date reserveDate = null; // 预定日期
        Integer forwardDates = 0; // 最早提前预定天数
        Double forwardHours = 0D; // 最早提前预定小时数
        Long rId = null; // 预定规则ID
        String reserveDate2 = null;
        long curDate = 0;
        ShopRsrcGroupDto pRsrcGroup;
        for (OsrsDto o : osrList)
        {
            // groupId = o.getGroupId();
            rId = o.getGroupId(); // 最新需求，改为bookruleId
            intevalId = o.getIntevalId();
            resourceNumber = o.getResourceNumber();
            CommonValidUtil
                    .validPositInt(resourceNumber, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_RSRCNUM);
            reserveDate = o.getReserveResourceDate();
            CommonValidUtil.validObjectNull(reserveDate, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_RESERV_DATE);
            // 预定日期不能早于当前系统日期
            curDate = DateUtils.parse(DateUtils.format(new Date(), DateUtils.ZEROTIME_FORMAT)).getTime();
            CommonValidUtil.validLongGreater(reserveDate.getTime(), curDate, CodeConst.CODE_RES_DATE_CANNOT_EARLY_CUR,
                    CodeConst.MSG_INVALID_RERVERSE_DATE);
            // -------
            BookRuleDto pRule = null;

            if (orderId.length() >= 30)
            {
                CommonValidUtil.validPositLong(rId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_GROUPID);
                CommonValidUtil
                        .validPositLong(intevalId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TIMEINTID);
                // 预定规则存在性
                pRule = this.bookRuleDao.getBookRuleById(rId);
                CommonValidUtil
                        .validObjectNull(pRule, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_BOOK_RULE);
                // 商铺资源组存在性(主要获取最大人数和最小人数)
                pRsrcGroup = shopRsrcGroupDao.getRsrcGroupById(shopId, pRule.getGroupId().longValue());
                CommonValidUtil
                        .validObjectNull(pRsrcGroup, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RSRCGROUP);
                o.setMinPeople(pRsrcGroup.getMinPeople());
                o.setMaxPeople(pRsrcGroup.getMaxPeople());
                // 可用时段存在性
                // timeId =
                // shopTimeIntDao.getShopTimeIdById(shopId,o.getIntevalId());//暂不用
                timeInteval = shopTimeIntDao.getShopTimeById(o.getIntevalId());
                CommonValidUtil
                        .validObjectNull(timeInteval, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_TIMEINT);
                // 预定时间段属于预定规则
                ruleId = timeInteval.getBookRuleId();
                CommonValidUtil
                        .validLongEqual(rId, ruleId, CodeConst.CODE_INTEVAL_NO_RULE, CodeConst.MSG_INTEVAL_NO_RULE);

                // 验证该资源在预定时段已经被预定数量，并验证是否可以继续预定
                // long usedNum = this.osrDao.queryRsrcGroupNumOfUsed(shopId,
                // groupId, intevalId);
                reserveDate2 = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT);
                long usedNum = this.osrDao.queryRsrcGroupNumOfUsed2(shopId, ruleId, intevalId, reserveDate2);
                if (resourceNumber > (pRule.getBookNumber() - usedNum))
                {
                    throw new ValidateException(CodeConst.CODE_NO_ENOUGH_NUM, CodeConst.MSG_NO_ENOUGH_RSCRGROUP);
                }
                // 开始时间
                startTime = timeInteval.getStartTime();
                // 结束时间
                endTime = timeInteval.getEndTime();
                // 资源类型
                resourceType = pRule.getBookType();
            }
            else
            {
                // timeInteval =
                // shopTimeIntDao.getShopTimeById(o.getIntevalId());
                // if(timeInteval == null){
                // ShopTimeIntDto time = new ShopTimeIntDto();
                // String startTime = orderShopRsrc.getStartTime();
                // String endTime = orderShopRsrc.getEndTime();
                // time.setShopId(shopId);
                // time.setStartTime(startTime);
                // time.setEndTime(endTime);
                // time.setCreateTime(new Date());
                // //新增一条时间段
                // intevalId = this.shopTimeIntDao.saveTimeInteval(time);
                // }else{
                // intevalId = timeInteval.getIntevalId();
                // }
            }
            // -------
            // 收银机的预定资源订单不校验预定规则,app预订资源订单需要校验预定规则
            if (orderId.length() >= 30)
            {
                // ruleId = timeInteval.getBookRuleId();
                // 验证预定规则
                // BookRuleDto pRule = this.bookRuleDao.getBookRuleById(ruleId);
                // CommonValidUtil.validObjectNull(pRule,
                // CodeConst.CODE_MISS_MODEL, CodeConst.MSG_MISS_BOOK_RULE);
                // 验证暂停预定日期范围
                String invalidInteval = pRule.getUnavailableDatetimeFrom();
                if (invalidInteval != null)
                {
                    intevals = invalidInteval.split(",");
                    for (String inteval : intevals)
                    {
                        timePoint = inteval.split("-");
                        if (timePoint != null && timePoint.length >= 2)
                        {
                            startDate = DateUtils.parse(timePoint[0], DateUtils.DATE_FORMAT2);
                            endDate = DateUtils.parse(timePoint[1], DateUtils.DATE_FORMAT2);
                            CommonValidUtil
                                    .validDateNotInDateRange(reserveDate, startDate, endDate, CodeConst.CODE_PAUSE_DATE,
                                            CodeConst.MSG_PAUSE_DATE);
                        }
                    }
                }
                double dates;
                if (!CommonValidUtil.validDateEqual(DateUtils.getCurrentDate(DateUtils.DATE_FORMAT), reserveDate))
                {
                    // 验证最早可提前几天预定
                    forwardDates = pRule.getBookDates();
                    forwardHours = pRule.getBookHours();
                    dates = new BigDecimal(forwardDates).multiply(new BigDecimal(24 * 60 * 60 * 1000)).doubleValue();
                    CommonValidUtil.validDateGreateThan(new Date(), reserveDate, dates, CodeConst.CODE_NOT_FORWARD_DATE,
                            CodeConst.MSG_NOT_FORWARD_DATE);
                }
                else
                {// 预定当天需要提前
                    // 需提前几小时预定
                    dates = new BigDecimal(forwardHours).multiply(new BigDecimal(60 * 60 * 1000)).doubleValue();
                    CommonValidUtil.validDateGreateThan(DateUtils.getCurTimeOfCurDate(),
                            DateUtils.getTime(timeInteval.getStartTime()), dates, CodeConst.CODE_NOT_FORWARD_HOUR,
                            CodeConst.MSG_NOT_FORWARD_HOUR);
                }
            }
        }
        // 检查该订单是否存在已经预定的商铺资源
        Integer count = this.osrDao.getOrderShopRsrcCount(orderId);
        if (count != null && count > 0)
        {
            // 删除旧的预定资源组
            this.osrDao.delOrderShopRsrc(orderId);
        }
        // 保存预定资源组
        for (OsrsDto o : osrList)
        {
            OrderShopRsrcDto osr = new OrderShopRsrcDto();
            osr.setOrderId(orderShopRsrc.getOrderId());

            osr.setUserId(userId);
            osr.setUserName(userName);
            osr.setMobile(mobile);
            osr.setStartTime(startTime);
            osr.setEndTime(endTime);
            osr.setResourceType(resourceType);

            osr.setShopId(orderShopRsrc.getShopId());
            // osr.setGroupId(groupId);
            osr.setBookRuleId(ruleId);
            osr.setIntevalId(intevalId);
            osr.setResourceNumber(resourceNumber);
            osr.setReserveResourceDate(o.getReserveResourceDate());
            osr.setCreateTime(new Date());
            osr.setStatus(0);
            osr.setMinPeople(o.getMinPeople());
            osr.setMaxPeople(o.getMaxPeople());
            this.osrDao.saveOrderShopRsrc(osr);
        }

    }

    /**
     * 查询商铺预定资源
     */
    public List<Map> getListShopResGroup(Long shopId, String queryDate, int serverMode) throws Exception
    {
        Map param = new HashMap();
        param.put("shopId", shopId);
        param.put("queryDate", queryDate);
        List<Map> osrs = new ArrayList<Map>();// 结果集合
        List<Map> result = null;
        // 获取商铺可预订的时间
        if (1 == serverMode)
        {// 上门
            param.put("settingType", 4);
            param.put("ruleType", 1);
            result = distributionTakeoutDao.getDeliveryTime(param);
        }
        else
        {
            // 到店
            param.put("settingType", 2);
            param.put("ruleType", 1);
            result = distributionTakeoutDao.getDeliveryTime(param);
        }

        List<Map> listFinal = new ArrayList<Map>();
        if (null != result && result.size() > 0)
        {
            int start = 1;
            Long interval = (long) (30 * 60 * 1000);// 时间间隔
            for (int i = 0, len = result.size(); i < len; i++)
            {
                List<Map> temp = shopTimeIntevalService
                        .getTimeInterval((Time) result.get(i).get("begin_time"), (Time) result.get(i).get("end_time"),
                                interval, start);
                start += temp.size();
                listFinal.addAll(temp);
            }
        }

        // 获取商铺座位类型
        List<ShopResourceGroupDto> list = shopResourceGroupDao.getListSRG(shopId, 1, 100);
        ShopResourceGroupDto shopResourceGroupDto = null;
        Map shopResourceGroupMap = null;
        for (int i = 0, len = list.size(); i < len; i++)
        {
            shopResourceGroupDto = list.get(i);
            shopResourceGroupMap = new HashMap();
            shopResourceGroupMap.put("resourceType", shopResourceGroupDto.getResourceType());
            shopResourceGroupMap.put("intevals", listFinal);
            osrs.add(shopResourceGroupMap);
        }
        return osrs;
    }

    public int useShopResource(Long userId, Long resourceId, String orderId) throws Exception
    {
        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        ShopRsrcDto resource = this.shopRsrcDao.getShopRsrcById(resourceId);
        CommonValidUtil.validObjectNull(resource, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_RESOURCE);
        if (2 == resource.getResourceStatus() || 1 == resource.getResourceStatus())
        {// 商铺资源不可用
            throw new ValidateException(CodeConst.CODE_RESOURCE_STATUS_NOT_AVALIABLE,
                    CodeConst.MSG_RESOURCE_STATUS_NOT_AVALIABLE);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("resourceId", resourceId);
        param.put("orderId", orderId);
        return shopRsrcDao.useShopResource(param);
    }

    public int queryOrderExists(String orderId) throws Exception
    {
        return this.orderDao.queryOrderExists(orderId);
    }

    public List<Map<String, Object>> getMyOrderNumber(Long userId) throws Exception
    {
        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();

        /*
         * queryStatus 对应的orderStatus 0（待消费）： 0（已预定）和 9（待确认） 1（待结账）
         * ：1（已开单）和2（派送中） 和 8（扫码下单） 2（待评价） ：3（已完成） 3（退订） ： 4 （退单中 和 5 （已退单）
         */
        List<Map<String, Object>> list = this.myOrdersDao.getMyOrderNumber(userId);

        // 已完成已评价的需要过滤掉需要过滤的个数
        // int filterNum = this.myOrdersDao.getFiterOrderNum(userId);
        // 4种状态，可以通过一个数组来存在对应的个数
        int[] total = new int[4];
        Integer orderStatus;
        long num;
        for (Map<String, Object> param : list)
        {
            orderStatus = Integer.valueOf(param.get("orderStatus") + "");
            num = (long) param.get("number");
            if (CommonConst.ORDER_STS_YYD == orderStatus || CommonConst.ORDER_STS_DQR == orderStatus
                    || CommonConst.ORDER_STS_ZZXD == orderStatus)
            {
                total[0] += num;
            }
            else if (CommonConst.ORDER_STS_YKD == orderStatus || CommonConst.ORDER_STS_PSZ == orderStatus)
            {
                total[1] += num;
            }
            else if (CommonConst.ORDER_STS_YJZ == orderStatus)
            {
                total[2] = (int) num;
            }
            else if (CommonConst.ORDER_STS_TDZ == orderStatus || CommonConst.ORDER_STS_YTD == orderStatus)
            {
                total[3] += num;
            }
        }
        for (int i = 0; i < 4; i++)
        {
            Map<String, Object> tmp = new HashMap<String, Object>();
            // if(i == 0) {
            // //待消费需要过滤的个数
            // total[i] -= filterNum;
            // }
            tmp.put("queryStatus", i);
            tmp.put("number", total[i]);
            resList.add(tmp);
        }

        return resList;
    }

    /**
     * 处理订单结算
     *
     * @param userid
     * @param shopid
     * @param goodsid
     * @param orderid
     * @throws Exception
     */
    public OrderGoodsSettle dealOrderGoodsSettle(Long userId, Long shopId, Long goodsId, String orderId,
            Map orderGoodsSettleSetting) throws Exception
    {
        // 用户信息
        UserDto user = userDao.getUserById(userId);
        // 订单信息
        OrderDto order = orderDao.getOrderById(orderId);
        CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
        // 商铺信息
        ShopDto shop = shopDao.getShopById(shopId);
        // 获取商品信息
        GoodsDto goods = goodsDao.getGoodsById(goodsId);
        if (CommonConst.GOODS_REBATE_FLAG_FALSE == goods.getGoodsRebateFlag())
        {
            // 商品不参与扣点，不进行分账
            logger.info("不参与扣点的商品，不进行分账goodsId:" + goods.getGoodsId());
            return null;
        }
        // 获取订单商品
        OrderGoodsDto orderGoods = new OrderGoodsDto();
        orderGoods.setOrderId(orderId);
        orderGoods.setShopId(shopId);
        orderGoods.setGoodsId(goodsId);
        orderGoods = orderGoodsDao.getOrderGoodsDto(orderGoods);
        OrderGoodsSettle orderGoodsSettle = buildOrderGoodsSettle(user, shop, order, orderGoods, goods,
                orderGoodsSettleSetting);
        if (null != orderGoodsSettle)
        {
            // 生成结算账单
            orderGoodsSettleDao.saveOrderGoodsSettle(orderGoodsSettle);
            if (orderGoodsSettle.getPlatformTotalIncomeRatio() > 0)
            {
                // 生成平台账单
                insertPlatformBill(orderGoodsSettle, user);
                // 生成用户账单
                insertUserBill(orderGoodsSettle, goods, order, user, shop);
            }
        }
        return orderGoodsSettle;
    }

    public void dealOrder(OrderDto order, Map orderGoodsSettleSetting) throws Exception
    {
        String orderId = order.getOrderId();
        Long userId = order.getUserId();
        List<OrderGoodsSettle> orderGoodsSettles = new ArrayList<OrderGoodsSettle>();

        // 当状态为已结账状态、全额付款、已支付状态才执行分账
        if (null != order && 3 == order.getOrderStatus())
        {// 订单状态：已预定-0,已开单-1,派送中-2,已结账-3，退单中-4,已退单-5,已完成-6,已删除-7',
            logger.info("满足线程分账条件" + order.getOrderStatus());
            OrderGoodsDto orderGoods = new OrderGoodsDto();
            IOrderGoodsDao orderGoodsDao = BeanFactory.getBean(IOrderGoodsDao.class);
            orderGoods.setOrderId(orderId);
            List<OrderGoodsDto> list = orderGoodsDao.getOGoodsListByOrderId(orderGoods);
            CommonValidUtil.validObjectNull(orderGoodsSettleSetting, CodeConst.CODE_PARAMETER_NOT_EXIST,
                    CodeConst.MSG_ORDER_GOODS_SETTLE_ERROR);
            for (OrderGoodsDto orderGoodsDto : list)
            {
                OrderGoodsSettle orderGoodsSettle = dealOrderGoodsSettle(userId, orderGoodsDto.getShopId(),
                        orderGoodsDto.getGoodsId(), orderId, orderGoodsSettleSetting);
                if (null != orderGoodsSettle)
                {
                    orderGoodsSettles.add(orderGoodsSettle);
                }
            }
            // 计算商铺的相关账单
            Double payAmount = payDao.getSumPayAmount(orderId, 1);// 商铺已收款项
            Double platformTotalPrice = orderGoodsSettleDao.getPlatformTotalPrice(orderId);// 平台的总收入
            // 生成商铺账单
            insertShopBill(order, payAmount, platformTotalPrice, 0D);
            // 更新订单表分成信息 add by huangrui in 20150915
            updateOrderSettleMsg(orderId, orderGoodsSettles);

        }
        else
        {
            logger.info("不满足线程分账条件" + order.getOrderStatus());
        }
    }

    public void splitOrderByPrice(OrderDto order, Map orderGoodsSettleSetting) throws Exception
    {
        String orderId = order.getOrderId();
        Long userId = order.getUserId();
        // 当状态为已结账状态、全额付款、已支付状态才执行分账
        if (null != order && 3 == order.getOrderStatus())
        {
            // 订单状态：已预定-0,已开单-1,派送中-2,已结账-3，退单中-4,已退单-5,已完成-6,自动下单-8,待确认-9',
            // 用户信息
            UserDto user = userDao.getUserById(userId);
            CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
            // 商铺信息
            ShopDto shop = shopDao.getShopByIdWithoutCache(order.getShopId());
            // 检查店铺信息是否正常，雇员信息是否存在。
            CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            // '结算方式: 1-按抽成比例结算,2-按折扣结算'
            Double shopIncomeRatio = shop.getPlatformDiscount(); // 平台折扣
            // 按订单总价结算 settingTpe=1的时候， 平台收入 （会员的结算折扣-店铺的结算折扣）* 结算价格
            Double platformTotalIncomeRatio = shop.getPercentage();
            if (platformTotalIncomeRatio < 0D)
            {
                throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST,
                        "店铺(" + shop.getShopName() + ")订单的分成比例异常");
            }
            Double shopIncomePrice = 0D; // 店铺收入
            Double platformNetIncome = 0D; // 平台净收入
            Double userSharePrice = 0D; // 会员返点
            Double userRefSharePrice = 0D; // 推荐人收入
            String userRefShareName = null; // 推荐人手机号
            Long userRefShareUserId = null;
            Double shopRefSharePrice = 0D; // (推荐店铺人收入)、
            String shopRefShareUserName = null; // (推荐店铺人手机号)、
            Long shopRefShareUserId = null;
            Double shopServeSharePrice = 0D; // (店铺服务人员收入)、
            String shopServeShareUserName = null; // (店铺服务人员手机号)、
            Long shopServeShareUserId = null;

            Double level1AgentPrice = 0D; // (一级代理收入)、
            String level1AgentShareUserName = null; // (一级代理手机号)、
            Long level1AgentShareUserId = null;
            Double level2AgentPrice = 0D; // (二级代理收入)、
            String level2AgentShareUserName = null; // (二级代理手机号)、
            Long level2AgentShareUserId = null;
            Double level3AgentPrice = 0D; // (三级代理收入)、
            String level3AgentShareUserName = null; // (三级代理手机号)
            Long level3AgentShareUserId = null;
            Double operatorsPrice = 0D; // 运营商的金额
            String operatorsUserName = null; // (运营商代理手机号)
            Long operatorsUserId = null;

            Double integrationPromotionUserPrice = 0D; // 连锁店铺整合推广人收入
            String integrationPromotionUserName = null; // 连锁店铺整合推广人手机号
            Long integrationPromotionUserId = null; // 连锁店铺整合推广人id

            Double integrationfacilitateUserPrice = 0D; // 连锁店铺整合促成人收入
            String integrationFacilitateUserName = null; // 连锁店铺整合促成人手机号
            Long integrationFacilitateUserId = null; // 连锁店铺整合促成人id

            Double integrationFrincipalUserPrice = 0D; // 连锁店铺店主收入
            String integrationFrincipalUserName = null; // 连锁店铺店主手机号
            Long integrationFrincipalUserId = null; // 连锁店铺店主id

            // 实收款
            Double settlePrice = NumberUtil.formatDouble(order.getSettlePrice(), 4);
            // 真实的结算价格 2015.11.10修改
            Double realSettlePrice = NumberUtil.formatDouble(order.getOrderRealSettlePrice(), 4);
            Double platformIncome = NumberUtil
                    .formatDouble(NumberUtil.multiply(platformTotalIncomeRatio, realSettlePrice), 4); // 平台总收入
            shopIncomePrice = NumberUtil.sub(settlePrice, platformIncome); // 店铺收入
            // =
            // 结算价格-平台总收入
            if (orderGoodsSettleSetting != null)
            {
                // 用户收入:自身返点计算
                double userRatio = ((BigDecimal) orderGoodsSettleSetting.get("userShareRatio")).doubleValue(); // 返点比例
                userSharePrice = NumberUtil.formatDouble(NumberUtil.multiply(platformIncome, userRatio), 4); // 会员自身返点
                double userRefShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("userRefShareRatio"))
                        .doubleValue();// 用户推荐人分成比例
                // 会员推荐人分成计算 店铺推荐和会员推荐比例是一样的
                if (user.getReferType() != null)
                {

                    int referType = user.getReferType();
                    if (CommonConst.USER_RECOMMAND_BY_SHOP == referType)
                    {
                        // 店铺方式推荐
                        ShopDto referShop = shopDao.getShopById(user.getReferShopId());
                        userRefShareName = userDao.getUserById(referShop.getPrincipalId()).getMobile();
                        userRefShareUserId = referShop.getPrincipalId();
                        userRefSharePrice = NumberUtil
                                .formatDouble(NumberUtil.multiply(platformIncome, userRefShareRatio), 4);
                    }
                    else if (CommonConst.USER_RECOMMAND_BY_USER == referType)
                    {
                        // 会员方式推荐，需要判断会员的类型
                        UserDto refUser = userDao.getUserById(user.getReferUserId());

                        if (refUser != null)
                        {
                            userRefShareName = refUser.getMobile();
                            userRefShareUserId = refUser.getUserId();
                            userRefSharePrice = NumberUtil
                                    .formatDouble(NumberUtil.multiply(platformIncome, userRefShareRatio), 4);
                        }
                    }
                    else
                    {
                        logger.info("会员推荐方式不匹配，不参与分成");
                    }
                }
                else
                {
                    logger.info("该会员不存在推荐人，不参与分成" + "订单id为" + orderId);
                }
                // 店铺推荐人分成
                double shopRefShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("shopRefShareRatio"))
                        .doubleValue(); // 分成比例
                double shopServeShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("shopServeShareRatio"))
                        .doubleValue();// 服务人员分成比例
                double level1AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level1AgentShareRatio"))
                        .doubleValue();// 一级代理分成比例
                double level2AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level2AgentShareRatio"))
                        .doubleValue();// 二级代理分成比例
                double level3AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level3AgentShareRatio"))
                        .doubleValue();// 三级代理分成比例
                double operatorsShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("operatorsShareRatio"))
                        .doubleValue();// 运营商分成比例

                double integrationPromotionRatio = (
                        null == shop.getIntegrationPromotionRatio() ? new BigDecimal(0) : shop
                                .getIntegrationPromotionRatio()).doubleValue();// 推广员分成比例
                double integrationFacilitateRatio = (
                        null == shop.getIntegrationFacilitateRatio() ? new BigDecimal(0) : shop
                                .getIntegrationFacilitateRatio()).doubleValue();// 促成人分成比例
                double integrationPrincipalRatio = (
                        null == shop.getIntegrationPrincipalRatio() ? new BigDecimal(0) : shop
                                .getIntegrationPrincipalRatio()).doubleValue();// 连锁店主分成比例

                // 店铺状态正常才进行分成
                UserDto shopRefUser = userDao.getUserById(shop.getReferUserId());
                if (shopRefUser != null)
                {
                    shopRefShareUserName = shopRefUser.getMobile();// 店铺推荐人手机号码
                    shopRefShareUserId = shopRefUser.getUserId();
                    shopRefSharePrice = NumberUtil.formatDouble(platformIncome * shopRefShareRatio, 4);
                }
                // 店铺服务人员分成
                logger.info("商铺服务人员id：" + shop.getShopServerUserId());
                UserDto shopServerUser = userDao.getUserById(shop.getShopServerUserId());
                if (null != shopServerUser)
                {
                    shopServeShareUserName = shopServerUser.getMobile(); // 服务员手机号码
                    shopServeShareUserId = shopServerUser.getUserId();
                    shopServeSharePrice = NumberUtil
                            .formatDouble(NumberUtil.multiply(platformIncome, shopServeShareRatio), 4);
                }
                else
                {
                    logger.info("不存在服务人员，不参与分成");
                }
                // 一级代理分成
                AgentDto level1Agent = null;
                if (null != shop.getCityId())
                {
                    level1Agent = agentDao.getAgent(null, shop.getCityId(), null, null, 31, order.getOrderTime());
                }
                if (level1Agent != null)
                {
                    if (level1Agent.getAgentShareRatio() != null && level1Agent.getAgentShareRatio() != 0)
                    {
                        level1AgentShareRatio = level1Agent.getAgentShareRatio();
                    }
                    UserDto level1AgentUser = userDao.getUserById(level1Agent.getUserId());
                    if (level1AgentUser != null)
                    {
                        level1AgentPrice = NumberUtil
                                .formatDouble(NumberUtil.multiply(platformIncome, level1AgentShareRatio), 4);
                        level1AgentShareUserName = level1AgentUser.getMobile();
                        level1AgentShareUserId = level1AgentUser.getUserId();
                    }
                }
                else
                {
                    logger.info("一级代理不存在，不参与分成");
                }
                // 二级代理分成
                AgentDto level2Agent = null;
                if (null != shop.getCityId() && null != shop.getDistrictId())
                {
                    level2Agent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(), null, 32,
                            order.getOrderTime());
                }
                if (level2Agent != null)
                {
                    if (level2Agent.getAgentShareRatio() != null && level2Agent.getAgentShareRatio() != 0)
                    {
                        level2AgentShareRatio = level2Agent.getAgentShareRatio();
                    }
                    UserDto level2AgentUser = userDao.getUserById(level2Agent.getUserId());
                    if (level2AgentUser != null)
                    {
                        level2AgentPrice = NumberUtil
                                .formatDouble(NumberUtil.multiply(platformIncome, level2AgentShareRatio), 4);
                        level2AgentShareUserName = level2AgentUser.getMobile();
                        level2AgentShareUserId = level2AgentUser.getUserId();
                    }
                }
                else
                {
                    logger.info("二级代理不存在，不参与分成");
                }
                // 三级代理分成
                AgentDto level3Agent = null;
                if (null != shop.getCityId() && null != shop.getDistrictId() && null != shop.getTownId())
                {
                    level3Agent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                            shop.getTownId().longValue(), 33, order.getOrderTime());
                }
                if (level3Agent != null)
                {
                    if (level3Agent.getAgentShareRatio() != null && level3Agent.getAgentShareRatio() != 0)
                    {
                        level3AgentShareRatio = level3Agent.getAgentShareRatio();
                    }
                    UserDto level3AgentUser = userDao.getUserById(level3Agent.getUserId());
                    if (level3AgentUser != null)
                    {
                        level3AgentPrice = NumberUtil
                                .formatDouble(NumberUtil.multiply(platformIncome, level3AgentShareRatio), 4);
                        level3AgentShareUserName = level3AgentUser.getMobile();
                        level3AgentShareUserId = level3AgentUser.getUserId();
                    }
                }
                else
                {
                    logger.info("三级代理不存在，不参与分成");
                }

                AgentDto operators = null;
                if (null != shop.getCityId() && null != shop.getDistrictId() && null != shop.getTownId())
                {
                    operators = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                            shop.getTownId().longValue(), 50, order.getOrderTime());
                }
                if (operators != null)
                {
                    if (operators.getAgentShareRatio() != null && operators.getAgentShareRatio() != 0)
                    {
                        operatorsShareRatio = operators.getAgentShareRatio();
                    }
                    UserDto operatorsUser = userDao.getUserById(operators.getUserId());
                    if (operatorsUser != null)
                    {
                        operatorsPrice = NumberUtil
                                .formatDouble(NumberUtil.multiply(platformIncome, operatorsShareRatio), 4);
                        operatorsUserName = operatorsUser.getMobile();
                        operatorsUserId = operatorsUser.getUserId();
                    }
                }
                else
                {
                    logger.info("运营商不存在，不参与分成");
                }

                UserDto tempUser = null;
                // 推广人分成
                integrationPromotionUserId = shop.getIntegrationPromotionUserId();
                if (null != integrationPromotionUserId)
                {
                    tempUser = userDao.getUserById(integrationPromotionUserId);
                    if (tempUser != null)
                    {

                        integrationPromotionUserName = tempUser.getMobile();
                        integrationPromotionUserPrice = NumberUtil
                                .formatDouble(platformIncome * integrationPromotionRatio, 4);
                    }
                }
                // 促成人分成
                integrationFacilitateUserId = shop.getIntegrationFacilitateUserId();
                if (null != integrationPromotionUserId)
                {
                    tempUser = userDao.getUserById(integrationFacilitateUserId);
                    if (tempUser != null)
                    {
                        integrationFacilitateUserName = tempUser.getMobile();
                        integrationfacilitateUserPrice = NumberUtil
                                .formatDouble(platformIncome * integrationFacilitateRatio, 4);
                    }
                }
                // 店主分成
                integrationFrincipalUserId = shop.getIntegrationPrincipalUserId();
                if (null != integrationPromotionUserId)
                {
                    tempUser = userDao.getUserById(integrationPromotionUserId);
                    if (tempUser != null)
                    {
                        integrationFrincipalUserName = tempUser.getMobile();
                        integrationFrincipalUserPrice = NumberUtil
                                .formatDouble(platformIncome * integrationPrincipalRatio, 4);
                    }
                }

                platformNetIncome =
                        platformIncome - userSharePrice - userRefSharePrice - shopRefSharePrice - shopServeSharePrice
                                - level1AgentPrice - level2AgentPrice - level3AgentPrice - operatorsPrice
                                - integrationPromotionUserPrice - integrationfacilitateUserPrice
                                - integrationFrincipalUserPrice;

                /*
                 * NumberUtil.sub(platformIncome, userSharePrice);
                 * NumberUtil.sub(shopRefSharePrice, shopServeSharePrice);
                 * NumberUtil.sub(NumberUtil.sub(level1AgentPrice,
                 * level2AgentPrice),level3AgentPrice);
                 */

                BigDecimal bdPlatformNetIncome = new BigDecimal(platformNetIncome);
                bdPlatformNetIncome = bdPlatformNetIncome.setScale(4, BigDecimal.ROUND_HALF_UP);
                platformNetIncome = bdPlatformNetIncome.doubleValue();
                OrderGoodsSettle orderGoodsSettle = new OrderGoodsSettle();
                orderGoodsSettle.setOrderId(order.getOrderId());
                orderGoodsSettle.setUserId(user.getUserId());
                orderGoodsSettle.setOrderTime(order.getOrderTime());
                orderGoodsSettle.setOrderGoodsPriceBeforeDiscount(order.getGoodsPriceBeforeDiscount());
                orderGoodsSettle.setOrderGoodsPrice(order.getGoodsPrice());
                orderGoodsSettle.setOrderLogisticsPrice(order.getLogisticsPrice());
                orderGoodsSettle.setOrderTotalPrice(order.getOrderTotalPrice());
                orderGoodsSettle.setOrderSettlePrice(order.getSettlePrice());
                orderGoodsSettle.setShopId(order.getShopId());
                orderGoodsSettle.setShopIncomeRatio(shopIncomeRatio);// 商铺收入比例
                orderGoodsSettle.setShopMemberDiscount(shop.getMemberDiscount());// 会员折扣率
                // 计算商铺收入
                orderGoodsSettle.setShopIncomePrice(shopIncomePrice);
                // orderGoodsSettle.setPlatformTotalIncomeRatio(platformTotalIncomeRatio);
                orderGoodsSettle.setUserRefShareUserType(user.getReferType());
                if (user.getReferType() != null && CommonConst.USER_RECOMMAND_BY_USER == user.getReferType())
                {
                    orderGoodsSettle.setUserRefShareUserId(user.getReferUserId());
                }
                else
                {
                    orderGoodsSettle.setUserRefShareUserId(user.getReferShopId());
                }
                orderGoodsSettle.setUserRefSharePrice(userRefSharePrice);
                orderGoodsSettle.setUserSharePrice(userSharePrice);
                orderGoodsSettle.setShopRefShareUserId(shopRefShareUserId);
                orderGoodsSettle.setShopRefShareUserType(shop.getReferUserType());
                orderGoodsSettle.setShopRefSharePrice(shopRefSharePrice);
                orderGoodsSettle.setShopServeSharePrice(shopServeSharePrice);
                orderGoodsSettle.setLevel1AgentPrice(level1AgentPrice);
                orderGoodsSettle.setLevel2AgentPrice(level2AgentPrice);
                orderGoodsSettle.setLevel3AgentPrice(level3AgentPrice);
                orderGoodsSettle.setOperatorsPrice(operatorsPrice);
                orderGoodsSettle.setPlatformNetIncomePrice(platformNetIncome);
                orderGoodsSettle.setPlatformTotalIncomePrice(platformIncome);
                /* 20160504添加连锁店 */
                orderGoodsSettle.setIntegrationFacilitateUserId(integrationFacilitateUserId);
                orderGoodsSettle.setIntegrationfacilitateUserPrice(integrationfacilitateUserPrice);
                orderGoodsSettle.setIntegrationFrincipalUserId(integrationFrincipalUserId);
                orderGoodsSettle.setIntegrationFrincipalUserPrice(integrationFrincipalUserPrice);
                orderGoodsSettle.setIntegrationPromotionUserId(integrationPromotionUserId);
                orderGoodsSettle.setIntegrationPromotionUserPrice(integrationPromotionUserPrice);
                // 生成平台账单
                insertPlatformBill(orderGoodsSettle, user);
                // 生成用户账单
                insertUserBill(orderGoodsSettle, null, order, user, shop);
                // 计算商铺的相关账单
                Double payAmount = payDao.getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_SHOP);// 商铺已收款项
                //查询订单使用代金券金额
                Double voucherPayAmount = payDao
                        .getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_PLATFORM, CommonConst.PAY_TYPE_VOUCHER);
                //加上代金券支付金额
                payAmount = NumberUtil.add(payAmount, voucherPayAmount);
                // 生成商铺账单
                insertShopBill(order, payAmount, platformIncome, voucherPayAmount);
                // 红包相关账单
                insertBillByRedPacket(order);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("shop_income_price", shopIncomePrice);
                map.put("platform_total_income_price", platformIncome);
                map.put("platform_net_income_price", platformNetIncome);
                map.put("user_share_price", userSharePrice);
                map.put("user_ref_share_price", userRefSharePrice);
                map.put("user_ref_share_user_name", userRefShareName);
                map.put("shop_ref_share_price", shopRefSharePrice);
                map.put("shop_ref_share_user_name", shopRefShareUserName);
                map.put("shop_serve_share_price", shopServeSharePrice);
                map.put("shop_serve_share_user_name", shopServeShareUserName);
                map.put("level1_agent_price", level1AgentPrice);
                map.put("level1_agent_share_user_name", level1AgentShareUserName);
                map.put("level2_agent_price", level2AgentPrice);
                map.put("level2_agent_share_user_name", level2AgentShareUserName);
                map.put("level3_agent_price", level3AgentPrice);
                map.put("level3_agent_share_user_name", level3AgentShareUserName);
                map.put("orderId", orderId);

                map.put("shop_income_ratio", shopIncomeRatio);
                map.put("platform_total_income_ratio", platformTotalIncomeRatio);
                map.put("user_share_ratio", userRatio);
                map.put("user_ref_share_ratio", userRefShareRatio);
                map.put("user_ref_share_user_id", userRefShareUserId);
                map.put("shop_ref_share_ratio", shopRefShareRatio);
                map.put("shop_ref_share_user_id", shopRefShareUserId);
                map.put("shop_serve_share_ratio", shopServeShareRatio);
                map.put("shop_serve_share_user_id", shopServeShareUserId);
                map.put("level1_agent_share_ratio", level1AgentShareRatio);
                map.put("level1_agent_share_user_id", level1AgentShareUserId);
                map.put("level2_agent_share_ratio", level2AgentShareRatio);
                map.put("level2_agent_share_user_id", level2AgentShareUserId);
                map.put("level3_agent_share_ratio", level3AgentShareRatio);
                map.put("level3_agent_share_user_id", level3AgentShareUserId);
                map.put("operator_share_price", operatorsPrice);
                map.put("operator_share_ratio", operatorsShareRatio);
                map.put("operator_share_user_name", operatorsUserName);
                map.put("operator_share_user_id", operatorsUserId);

                /* 20160504添加连锁店 */

                map.put("integration_promotion_price", integrationPromotionUserPrice);
                map.put("integration_promotion_ratio", integrationPromotionRatio);
                map.put("integration_promotion_user_name", integrationPromotionUserName);
                map.put("integration_promotion_user_id", integrationPromotionUserId);

                map.put("integration_facilitate_price", integrationfacilitateUserPrice);
                map.put("integration_facilitate_ratio", integrationFacilitateRatio);
                map.put("integration_facilitate_user_name", integrationFacilitateUserName);
                map.put("integration_facilitate_user_id", integrationFacilitateUserId);

                map.put("integration_principal_price", integrationFrincipalUserPrice);
                map.put("integration_principal_ratio", integrationPrincipalRatio);
                map.put("integration_principal_user_name", integrationFrincipalUserName);
                map.put("integration_principal_user_id", integrationFrincipalUserId);
                OrderSettleDto orderSettle = new OrderSettleDto();
                orderSettle.setOrderId(orderId);
                //                orderSettleDao.saveOrderSettle(orderSettle);
                orderDao.updateorderSettleMsg(map);
                //                orderSettleDao.updateOrderSettle(map);
                logger.info("-----------------更新订单表的分账信息 --end");
            }
            else
            {
                logger.info("没有配置分成比例，不进行分成操作");
            }
        }
        else
        {
            logger.info("不满足分账条件" + order.getOrderStatus());
        }

    }

    /**
     * 更新订单表分成信息
     *
     * @param orderId
     * @param orderGoodsSettles
     */
    private void updateOrderSettleMsg(String orderId, List<OrderGoodsSettle> orderGoodsSettles)
    {
        try
        {
            logger.debug("-----------------更新订单表的分账信息 --start");
            Double shop_income_price = 0D; // 店铺收入
            Double platform_net_income_price = 0D; // 平台净收入
            Double platform_total_income_price = 0D; // 平台总收入
            Double user_share_price = 0D; // 会员消费返点
            Double user_ref_share_user_price = 0D; // 推荐人收入
            String user_ref_share_user_name = null; // 推荐人手机号
            Double shop_ref_share_user_price = 0D; // (推荐店铺人收入)、
            String shop_ref_share_user_name = null; // (推荐店铺人手机号)、
            Double shop_serve_share_price = 0D; // (店铺服务人员收入)、
            String shop_serve_share_user_name = null; // (店铺服务人员手机号)、
            Double level1_agent_price = 0D; // (一级代理收入)、
            String level1_agent_share_user_name = null; // (一级代理手机号)、
            Double level2_agent_price = 0D; // (二级代理收入)、
            String level2_agent_share_user_name = null; // (二级代理手机号)、
            Double level3_agent_price = 0D; // (三级代理收入)、
            String level3_agent_share_user_name = null; // (三级代理手机号)

            // 2015.10.27 需求新增以下15个字段
            Double shop_income_ratio = 0D; // 店铺收入比例
            Double platform_total_income_ratio = 0D; // 平台分成比例
            Double user_share_ratio = 0D; // 用户分成比例
            Double user_ref_share_ratio = 0D; // 会员推荐人分成比例
            Long user_ref_share_user_id = null; // 会员推荐人ID
            Double shop_ref_share_ratio = 0D; // 店铺推荐人分成比例
            Long shop_ref_share_user_id = null; // 店铺推荐人ID
            Double shop_serve_share_ratio = 0D; // 店铺服务人员分成比例
            Long shop_serve_share_user_id = null; // 店铺服务人员ID
            Double level1_agent_share_ratio = 0D; // 一级代理分成比例
            Long level1_agent_share_user_id = null; // 一级代理人用户ID
            Double level2_agent_share_ratio = 0D; // 二级代理分成比例
            Long level2_agent_share_user_id = null; // 二级代理人用户ID
            Double level3_agent_share_ratio = 0D; // 三级代理分成比例
            Long level3_agent_share_user_id = null; // 三级代理人代理ID

            /* 20160505添加连锁店分成信息 */
            Long integration_promotion_user_id = null;
            String integration_promotion_user_name = null;
            Double integration_promotion_ratio = 0D;
            Double integration_promotion_price = 0D;

            Long integration_facilitate_user_id = null;
            String integration_facilitate_user_name = null;
            Double integration_facilitate_ratio = 0D;
            Double integration_facilitate_price = 0D;

            Long integration_principal_user_id = null;
            String integration_principal_user_name = null;
            Double integration_principal_ratio = 0D;
            Double integration_principal_price = 0D;

            if (CollectionUtils.isEmpty(orderGoodsSettles))
            {
                logger.debug("没有商品分账信息，不做更新订单表中的分账信息");
                return;
            }
            // 该处处理接口有等商讨
            if (orderGoodsSettles.get(0) != null)
            {

                shop_income_ratio = orderGoodsSettles.get(0).getShopIncomeRatio();
                platform_total_income_ratio = orderGoodsSettles.get(0).getPlatformTotalIncomeRatio();
                user_share_ratio = orderGoodsSettles.get(0).getUserShareRatio();
                user_ref_share_ratio = orderGoodsSettles.get(0).getUserRefShareRatio();
                user_ref_share_user_id = orderGoodsSettles.get(0).getUserRefShareUserId();
                shop_ref_share_ratio = orderGoodsSettles.get(0).getShopRefShareRatio();
                shop_ref_share_user_id = orderGoodsSettles.get(0).getShopRefShareUserId();
                shop_serve_share_ratio = orderGoodsSettles.get(0).getShopServeShareRatio();
                shop_serve_share_user_id = orderGoodsSettles.get(0).getShopServeShareUserId();
                level1_agent_share_ratio = orderGoodsSettles.get(0).getLevel1AgentShareRatio();
                level1_agent_share_user_id = orderGoodsSettles.get(0).getLevel1AgentShareUserId();
                level2_agent_share_ratio = orderGoodsSettles.get(0).getLevel2AgentShareRatio();
                level2_agent_share_user_id = orderGoodsSettles.get(0).getLevel2AgentShareUserId();
                level3_agent_share_ratio = orderGoodsSettles.get(0).getLevel3AgentShareRatio();
                level3_agent_share_user_id = orderGoodsSettles.get(0).getLevel3AgentShareUserId();

                /* 20160505添加连锁店 */
                integration_promotion_user_id = orderGoodsSettles.get(0).getIntegrationPromotionUserId();
                integration_promotion_ratio = orderGoodsSettles.get(0).getIntegrationPromotionRatio();
                integration_facilitate_user_id = orderGoodsSettles.get(0).getIntegrationFacilitateUserId();
                integration_facilitate_ratio = orderGoodsSettles.get(0).getIntegrationFacilitateRatio();
                integration_principal_user_id = orderGoodsSettles.get(0).getIntegrationFrincipalUserId();
                integration_principal_ratio = orderGoodsSettles.get(0).getIntegrationPrincipalRatio();
            }

            for (OrderGoodsSettle settle : orderGoodsSettles)
            {
                if (settle == null)
                    continue;
                shop_income_price += getNotNullValue(settle.getShopIncomePrice());
                platform_total_income_price += getNotNullValue(settle.getPlatformTotalIncomePrice());
                platform_net_income_price += getNotNullValue(settle.getPlatformNetIncomePrice());
                user_share_price += getNotNullValue(settle.getUserSharePrice());
                user_ref_share_user_price += getNotNullValue(settle.getUserRefSharePrice());
                shop_ref_share_user_price += getNotNullValue(settle.getShopRefSharePrice());
                shop_serve_share_price += getNotNullValue(settle.getShopServeSharePrice());
                level1_agent_price += getNotNullValue(settle.getLevel1AgentPrice());
                level2_agent_price += getNotNullValue(settle.getLevel2AgentPrice());
                level3_agent_price += getNotNullValue(settle.getLevel3AgentPrice());

                /* 20160505添加连锁店铺 */
                integration_promotion_price += getNotNullValue(settle.getIntegrationPromotionUserPrice());
                integration_facilitate_price += getNotNullValue(settle.getIntegrationfacilitateUserPrice());
                integration_principal_price += getNotNullValue(settle.getIntegrationFrincipalUserPrice());
            }
            Integer type = orderGoodsSettles.get(0).getUserRefShareUserType();
            Long refShareUserId = orderGoodsSettles.get(0).getUserRefShareUserId();
            if (!isNull(refShareUserId))
            {
                if (!isNull(type) && type == 1)
                { // 推荐人为店铺
                    user_ref_share_user_name = userDao.getDBUserById(shopDao.getUserByShopId(refShareUserId))
                            .getMobile();
                }
                else
                {
                    // 推荐人手机号
                    user_ref_share_user_name = userDao.getDBUserById(orderGoodsSettles.get(0).getUserRefShareUserId())
                            .getMobile();
                }
            }

            // 推荐店铺人手机号
            if (!isNull(orderGoodsSettles.get(0).getShopRefShareUserId()))
            {
                shop_ref_share_user_name = userDao.getDBUserById(orderGoodsSettles.get(0).getShopRefShareUserId())
                        .getMobile();
            }
            // 店铺服务人员手机号
            if (!isNull(orderGoodsSettles.get(0).getShopServeShareUserId()))
            {
                shop_serve_share_user_name = userDao.getDBUserById(orderGoodsSettles.get(0).getShopServeShareUserId())
                        .getMobile();
            }
            // 一级代理手机号
            if (!isNull(orderGoodsSettles.get(0).getLevel1AgentShareUserId()))
            {
                level1_agent_share_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getLevel1AgentShareUserId()).getMobile();
            }
            // 二级代理手机号
            if (!isNull(orderGoodsSettles.get(0).getLevel2AgentShareUserId()))
            {
                level2_agent_share_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getLevel2AgentShareUserId()).getMobile();
            }
            // 三级代理手机号
            if (!isNull(orderGoodsSettles.get(0).getLevel3AgentShareUserId()))
            {
                level3_agent_share_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getLevel3AgentShareUserId()).getMobile();
            }

            /* 20160505添加连锁店铺 */
            if (!isNull(orderGoodsSettles.get(0).getIntegrationPromotionUserId()))
            {
                integration_promotion_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getIntegrationPromotionUserId()).getMobile();
            }
            if (!isNull(orderGoodsSettles.get(0).getIntegrationFacilitateUserId()))
            {
                integration_facilitate_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getIntegrationFacilitateUserId()).getMobile();
            }
            if (!isNull(orderGoodsSettles.get(0).getIntegrationFrincipalUserId()))
            {
                integration_principal_user_name = userDao
                        .getDBUserById(orderGoodsSettles.get(0).getIntegrationFrincipalUserId()).getMobile();
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shop_income_price", shop_income_price);
            map.put("platform_total_income_price", platform_total_income_price);
            map.put("platform_net_income_price", platform_net_income_price);
            map.put("user_share_price", user_share_price);
            map.put("user_ref_share_price", user_ref_share_user_price);
            map.put("user_ref_share_user_name", user_ref_share_user_name);
            map.put("shop_ref_share_price", shop_ref_share_user_price);
            map.put("shop_ref_share_user_name", shop_ref_share_user_name);
            map.put("shop_serve_share_price", shop_serve_share_price);
            map.put("shop_serve_share_user_name", shop_serve_share_user_name);
            map.put("level1_agent_price", level1_agent_price);
            map.put("level1_agent_share_user_name", level1_agent_share_user_name);
            map.put("level2_agent_price", level2_agent_price);
            map.put("level2_agent_share_user_name", level2_agent_share_user_name);
            map.put("level3_agent_price", level3_agent_price);
            map.put("level3_agent_share_user_name", level3_agent_share_user_name);

            map.put("shop_income_ratio", shop_income_ratio);
            map.put("platform_total_income_ratio", platform_total_income_ratio);
            map.put("user_share_ratio", user_share_ratio);
            map.put("user_ref_share_ratio", user_ref_share_ratio);
            map.put("user_ref_share_user_id", user_ref_share_user_id);
            map.put("shop_ref_share_ratio", shop_ref_share_ratio);
            map.put("shop_ref_share_user_id", shop_ref_share_user_id);
            map.put("shop_serve_share_ratio", shop_serve_share_ratio);
            map.put("shop_serve_share_user_id", shop_serve_share_user_id);
            map.put("level1_agent_share_ratio", level1_agent_share_ratio);
            map.put("level1_agent_share_user_id", level1_agent_share_user_id);
            map.put("level2_agent_share_ratio", level2_agent_share_ratio);
            map.put("level2_agent_share_user_id", level2_agent_share_user_id);
            map.put("level3_agent_share_ratio", level3_agent_share_ratio);
            map.put("level3_agent_share_user_id", level3_agent_share_user_id);

            /* 20160505添加连锁店铺 */
            map.put("integration_promotion_user_id", integration_promotion_user_id);
            map.put("integration_promotion_user_name", integration_promotion_user_name);
            map.put("integration_promotion_ratio", integration_promotion_ratio);
            map.put("integration_promotion_price", integration_promotion_price);

            map.put("integration_facilitate_user_id", integration_facilitate_user_id);
            map.put("integration_facilitate_user_name", integration_facilitate_user_name);
            map.put("integration_facilitate_ratio", integration_facilitate_ratio);
            map.put("integration_facilitate_price", integration_facilitate_price);

            map.put("integration_principal_user_id", integration_principal_user_id);
            map.put("integration_principal_user_name", integration_principal_user_name);
            map.put("integration_principal_ratio", integration_principal_ratio);
            map.put("integration_principal_price", integration_principal_price);

            map.put("orderId", orderId);

            orderDao.updateorderSettleMsg(map);
            logger.debug("-----------------更新订单表的分账信息 --end");

        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isNull(Long id)
    {
        return (id == null || id == 0L);
    }

    private boolean isNull(Double money)
    {
        return (money == null || money == 0D);
    }

    private boolean isNull(Integer type)
    {
        return (type == null);
    }

    private double getNotNullValue(Double v)
    {
        if (v == null)
        {
            return 0d;
        }
        else
        {
            return v.doubleValue();
        }
    }

    /**
     * 结算处理
     */
    public void dealSettle(OrderDto order, Long userId, Map orderGoodsSettleSetting) throws Exception
    {
        String orderId = order.getOrderId();

        // 如果userId为空则使用订单中的userId
        if (userId == null)
            userId = order.getUserId();

        // 如果结算周期设置为0，则立马触发结算
        Long userSettleDelayDays = (Long) orderGoodsSettleSetting.get("userSettleDelayDays");// 用户结算周期
        Long shopSettleDelayDays = (Long) orderGoodsSettleSetting.get("shopSettleDelayDays");// 商铺结算周期
        if (null != shopSettleDelayDays && shopSettleDelayDays.intValue() == 0)
        {
            // 触发商铺分账
            orderGoodsSettleDao.updateOrderGoodsSettleShop(userId, order.getShopId(), null, orderId, 1);
        }

        if (null != userSettleDelayDays && userSettleDelayDays.intValue() == 0)
        {
            // 触发用户分账
            orderGoodsSettleDao.updateOrderGoodsSettle(userId, order.getShopId(), null, orderId, 1);
        }
    }

    /**
     * 结算是红包相关的订单
     *
     * @param order
     * @author shengzhipeng
     * @date 2016年3月15日
     */
    public void insertBillByRedPacket(OrderDto order) throws Exception
    {
        // 根据订单编号获取账单
        String orderId = order.getOrderId();
        List<PlatformBillDto> platformBills = platformBillDao.getPlatformBillByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(platformBills))
        {
            for (PlatformBillDto platformBillDto : platformBills)
            {
                if (CommonConst.PLATFORM_BILL_TYPE_S_RED_PACKET == platformBillDto.getPlatformBillType())
                {
                    // 如果存在平台发出去的红包，结算时需要从店铺线上收入扣回
                    platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_R_RED_PACKET);
                    platformBillDto.setMoney(Math.abs(platformBillDto.getMoney()));
                    platformBillDto.setBillType("收红包");
                    platformBillDao.insertPlatformBillMiddle(platformBillDto);
                    ShopBillDto shopBillDto = new ShopBillDto();
                    shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_SEND_REDPACKET);
                    shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
                    shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
                    shopBillDto.setMoney(-Math.abs(platformBillDto.getMoney()));
                    shopBillDto.setShopId(order.getShopId());
                    shopBillDto.setOrderId(orderId);
                    shopBillDto.setSettlePrice(order.getSettlePrice());
                    shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_INCOME);
                    shopBillDto.setCreateTime(new Date());
                    shopBillDto.setBillTitle(order.getOrderTitle());
                    shopBillDto.setComment("发送用户红包" + Math.abs(platformBillDto.getMoney()));
                    shopBillDto.setRedPacketId(platformBillDto.getRedPacketId());
                    shopBillDao.insertShopMiddleBill(shopBillDto);
                }
            }
        }
    }

    public void orderVantages(OrderDto order) throws Exception
    {
        if (null != order && 3 == order.getOrderStatus())
        {
            Long shopId = order.getShopId();
            // 结算价格
            Double settlePrice = order.getSettlePrice();
            Long userId = order.getUserId();
            // 获取商铺会员原有积分
            BigDecimal oldUserVantages = userDao.getUserVantagesByUserIdAndShopId(userId, shopId);
            logger.info("会员原有积分：" + oldUserVantages);
            ShopUserVantages userVantage = new ShopUserVantages();
            // 积分系数
            Double settingValue = getOrderVantagesSetting(shopId, settlePrice);
            // 本次订单积分： 会员积分 = 积分系数 * 结算价格/100(按照100元多少个积分计算)
            if (null != settingValue && settingValue != 0D)
            {
                Double integral = settingValue * settlePrice / 100;
                logger.info("本次订单积分积分：" + integral);
                // 第一次增加
                if (oldUserVantages == null)
                {
                    userVantage.setVantages(integral);
                    userVantage.setCreateTime(new Date());
                    userVantage.setLastUpdateTime(new Date());
                    userVantage.setUserId(userId);
                    userVantage.setShopId(shopId);
                    // 插入积分设置
                    userDao.insertShopUserVantages(userVantage);
                }
                else
                {// 后面都更新操作
                    // 最后积分 = 原有积分+本次订单积分
                    Double newVantages = oldUserVantages.doubleValue() + integral;
                    logger.info("本次积分：" + newVantages);
                    userVantage.setVantages(newVantages);
                    userVantage.setCreateTime(new Date());
                    userVantage.setLastUpdateTime(new Date());
                    userVantage.setUserId(userId);
                    userVantage.setShopId(shopId);
                    userDao.updateShopUserVantages(userVantage);
                }

                // 记录日志
                ShopUserVantagesLog userVantagesLog = new ShopUserVantagesLog();
                userVantagesLog.setCreateTime(new Date());
                userVantagesLog.setLastUpdateTime(new Date());
                userVantagesLog.setUserId(userId);
                userVantagesLog.setShopId(shopId);
                userVantagesLog.setOrderId(order.getOrderId());
                // 每100元可兑换多少积分
                userVantagesLog.setMoneyToVantages(settingValue);
                // 兑换金额
                userVantagesLog.setExchangeMoney(settlePrice);
                // 可兑换积分
                userVantagesLog.setExchangeVantages(integral);
                userDao.insertShopUserVantageslog(userVantagesLog);
            }
        }
    }

    /**
     * 根据订单积分设置
     *
     * @throws Exception
     */
    public Double getOrderVantagesSetting(Long shopId, Double settlePrice) throws Exception
    {
        // 积分设置
        Double settingValue = 0D;
        Integer settionType = 1;
        // List<ShopConfigureSettingDto> listDB =
        // shopSettingDao.queryShopConfigureSetting(shopId, settionType);
        ConfigQueryCondition configQueryCondition = new ConfigQueryCondition();
        configQueryCondition.setShopId(shopId);
        configQueryCondition.setConfigGroups(new String[] { "POINT_CONFIG" });
        PageResult<ConfigDto> dtos = commonService.queryForConfig(configQueryCondition);
        List<ConfigDto> configs = dtos.getLst();
        if (CollectionUtils.isNotEmpty(configs))
        {
            for (ConfigDto ssd : configs)
            {
                String settingKey = ssd.getConfigKey();
                String settingValueStr = ssd.getConfigValue();
                if (StringUtils.isNotBlank(settingKey) && StringUtils.isNotBlank(settingValueStr))
                {
                    // 如果配置项是积分设置
                    if (CommonConst.MONEY_TO_INTEGRAL.equals(settingKey))
                    {
                        settingValue = Double.valueOf(settingValueStr);
                        break;
                    }
                }

            }
        }
        /*
         * if (CollectionUtils.isNotEmpty(listDB)) { for
         * (ShopConfigureSettingDto ssd : listDB) { String settingKey =
         * ssd.getSettingKey(); String settingValueStr = ssd.getSettingValue();
         * if (StringUtils.isNotBlank(settingKey) &&
         * StringUtils.isNotBlank(settingValueStr)) { // 如果配置项是积分设置 if
         * (CommonConst.MONEY_TO_INTEGRAL.equals(settingKey)) { settingValue =
         * Double.valueOf(settingValueStr); break; } }
         * 
         * } }
         */
        return settingValue;
    }

    /**
     * 统计会员订单支付信息
     *
     * @param order
     * @throws Exception
     * @Function: com.idcq.appserver.service.order.OrderServiceImpl.
     * handleAccountingStatByUser
     * @Description:
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年9月23日 上午11:43:39
     * <p/>
     * Modification History: Date Author Version Description
     * ----
     * ------------------------------------------------------
     * ------- 2015年9月23日 ChenYongxin v1.0.0 create
     */
    public void handleAccountingStatByUser(OrderDto order) throws Exception
    {
        if (null != order && 3 == order.getOrderStatus())
        {
            logger.info("插入财务统计-start");
            Map<String, Object> pMap = new HashMap<String, Object>();
            ShopIncomeStatDto shopIncomeStat = new ShopIncomeStatDto();
            String orderId = order.getOrderId();
            pMap.put("orderId", orderId);
            logger.info("插入财务统计--orderId:" + orderId);
            // 查询pos和现金支付信息
            Map<String, Object> payMap = payDao.getAmountByOrderIdAndPayType(pMap);
            if (null != payMap && 0 != payMap.size())
            {
                BigDecimal memberCardPay = (BigDecimal) payMap.get("memberCardPay");
                BigDecimal cashPay = (BigDecimal) payMap.get("cashPay");
                BigDecimal posPay = (BigDecimal) payMap.get("posPay");
                shopIncomeStat.setMemberCardPay(memberCardPay.doubleValue());
                shopIncomeStat.setCashPay(cashPay.doubleValue());
                shopIncomeStat.setPosPay(posPay.doubleValue());
                shopIncomeStat.setFreePay(Double.valueOf(payMap.get("freePay").toString()));
                shopIncomeStat.setCustomPay1(Double.valueOf(payMap.get("customPay1").toString()));
                shopIncomeStat.setCustomPay2(Double.valueOf(payMap.get("customPay2").toString()));
                shopIncomeStat.setCustomPay3(Double.valueOf(payMap.get("customPay3").toString()));
                logger.info("会员卡:" + memberCardPay.doubleValue());
                logger.info("现金支付:" + cashPay.doubleValue());
                logger.info("POS支付:" + posPay.doubleValue());
            }
            Map<String, Object> payeeMap = payDao.getAmountByOrderIdAndPayeeType(pMap);
            Double voucherPay = payDao
                    .getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_PLATFORM, CommonConst.PAY_TYPE_VOUCHER);
            if (null != payeeMap && 0 != payeeMap.size())
            {
                BigDecimal onLinePay = (BigDecimal) payeeMap.get("onLinePay");
                //先把3币算到线上支付中
                //                onLinePay =onLinePay.subtract(new BigDecimal(voucherPay.toString()));
                logger.info("线上支付:" + onLinePay.doubleValue());
                shopIncomeStat.setOnLinePay(onLinePay.doubleValue());
            }
            shopIncomeStat.setVoucherPay(voucherPay);
            // 商铺服务费
            Double serveMoney = 0D;
            // 商铺账务统计
            shopIncomeStat.setOrderId(orderId);
            shopIncomeStat.setSettlePrice(order.getSettlePrice());
            shopIncomeStat.setOrderTime(order.getOrderTime());
            shopIncomeStat.setOrderTitle(order.getOrderTitle());
            shopIncomeStat.setMobile(getMobileById(order.getUserId()));
            shopIncomeStat.setShopId(order.getShopId());
            shopIncomeStat.setFinishTime(order.getOrderFinishTime());
            shopIncomeStat.setIncomeType(CommonConst.INCOME_TYPE_CONSUME);
            shopIncomeStat.setChargePrice(0D);
            Date statDate = order.getClientFinishTime();
            if (statDate == null)
            {
                statDate = order.getOrderFinishTime();
            }
            shopIncomeStat.setStatDate(statDate);
            shopIncomeStat.setCashierId(order.getCashierId());
            if (null != order.getUserId())
            {
                shopIncomeStat.setPlatformServeSharePrice(serveMoney);
            }
            else
            {
                shopIncomeStat.setPlatformServeSharePrice(0d);
            }
            // 插入财务统计
            shopDao.insertAccountingStat(shopIncomeStat);
            logger.info("插入财务统计-end");
        }
        else
        {
            logger.info("订单不满足统计条件，订单状态为：" + order.getOrderStatus());
        }
    }

    /**
     * 计算店铺服务费
     *
     * @throws Exception
     */
    public Double computeServeMoneyByOrder(OrderDto order) throws Exception
    {
        Long shopId = order.getShopId();
        ShopDto shop = shopDao.getShopById(shopId);
        // 获取分成比例
        //        Double percentage = getPercentage(shop);
        Double percentage = shop.getPercentage();
        logger.info("商铺分成比例" + percentage);
        if (percentage == 0D)
        {
            return 0D;
        }

        // 结算价格
        Double orderRealSettlePrice = order.getOrderRealSettlePrice() == null ? 0D : order.getOrderRealSettlePrice();
        if (null != shop.getShopIdentity() && shop.getShopIdentity() == 1)
        {
            orderRealSettlePrice = order.getSettlePrice();
        }
        logger.info("orderRealSettlePrice:" + orderRealSettlePrice);
        // 服务费=分成比例*结算价格
        Double serveMoney = NumberUtil.multiply(percentage, orderRealSettlePrice);
        logger.info("最后算出服务费:" + serveMoney);
        if (serveMoney <= 0)
        {
            serveMoney = 0D;
        }
        return serveMoney;
    }

    /**
     * 获取商品分成比例
     *
     * @param shop
     * @return
     * @Function: com.idcq.appserver.service.order.OrderServiceImpl.
     * getPercentage
     * @Description:
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月5日 下午2:03:32
     * <p/>
     * Modification History: Date Author Version Description
     * ----
     * ------------------------------------------------------
     * ------- 2015年12月5日 ChenYongxin v1.0.0 create
     */
    public Double getPercentage(ShopDto shop)
    {
        // '结算方式: 1-按抽成比例结算,2-按折扣结算'
        Integer settleType = shop.getSettleType();
        Double shopIncomeRatio = shop.getPlatformDiscount(); // 平台折扣
        Double memberDiscount = shop.getMemberDiscount(); // 会员折扣
        // 按订单总价结算 settingTpe=1的时候， 平台收入 （会员的结算折扣-店铺的结算折扣）* 结算价格
        Double platformTotalIncomeRatio = 0D;
        // 如果结算方式为：2-按折扣结算
        if (CommonConst.SETTLE_TYPE_PERCENTAGE == settleType)
        {
            // 否则取'平台抽成比例, 比如抽成3.5%，存入0.035',
            platformTotalIncomeRatio = shop.getPercentage();
            logger.info("按平台抽出比例分成,抽出比例：" + platformTotalIncomeRatio);
        }
        else
        {
            if (null == shopIncomeRatio || null == memberDiscount)
            {
                throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST,
                        "店铺(" + shop.getShopName() + ")订单的分成比例异常");
            }
            platformTotalIncomeRatio = NumberUtil.sub(memberDiscount, shopIncomeRatio);
            logger.info("按平台折扣分成，折扣比例：" + platformTotalIncomeRatio);
        }
        if (platformTotalIncomeRatio < 0D)
        {
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺(" + shop.getShopName() + ")订单的分成比例异常");
        }
        return platformTotalIncomeRatio;
    }

    /**
     * 构建分账结算数据
     *
     * @param orderGoods
     * @param goods
     * @param orderGoodsSettleSetting
     * @return
     * @throws Exception
     */
    public OrderGoodsSettle buildOrderGoodsSettle(UserDto user, ShopDto shop, OrderDto order, OrderGoodsDto orderGoods,
            GoodsDto goods, Map orderGoodsSettleSetting) throws Exception
    {
        Double shopIncomeRatio = shop.getPlatformDiscount();
        Double platformTotalIncomeRatio = NumberUtil.sub(shop.getMemberDiscount(), shopIncomeRatio);
        if (shop.getSettleType() == CommonConst.SETTLE_TYPE_PERCENTAGE)
        {
            if (shop.getPercentage() == null)
            {
                logger.error("商铺的分账方式设置为按抽成比例，但是抽成比例未设置，分账失败!");
                return null;
            }
            else
            {
                platformTotalIncomeRatio = shop.getPercentage();
            }
        }
        else
        {
            if (null == shopIncomeRatio || null == shop.getMemberDiscount())
            {
                logger.error("商铺的分账方式设置为按折扣，会员和平台分成比例为空，分账失败!");
                return null;
            }
        }
        Double goodStandardPrice = NumberUtil.formatDouble(goods.getStandardPrice() * orderGoods.getGoodsNumber(), 4);
        OrderGoodsSettle orderGoodsSettle = new OrderGoodsSettle();
        orderGoodsSettle.setOrderId(order.getOrderId());
        orderGoodsSettle.setUserId(user.getUserId());
        orderGoodsSettle.setOrderTime(order.getOrderTime());
        orderGoodsSettle.setOrderGoodsPriceBeforeDiscount(order.getGoodsPriceBeforeDiscount());
        orderGoodsSettle.setOrderGoodsPrice(order.getGoodsPrice());
        orderGoodsSettle.setOrderLogisticsPrice(order.getLogisticsPrice());
        orderGoodsSettle.setOrderTotalPrice(order.getOrderTotalPrice());
        orderGoodsSettle.setOrderSettlePrice(order.getSettlePrice());
        orderGoodsSettle.setOrderGoodsId(orderGoods.getId());
        orderGoodsSettle.setShopId(order.getShopId());
        orderGoodsSettle.setGoodsId(goods.getGoodsId());
        orderGoodsSettle.setGoodsNumber(orderGoods.getGoodsNumber());
        orderGoodsSettle.setGoodsStandardPrice(goodStandardPrice);// 商品计算价格：商品的目录价*商品数量
        orderGoodsSettle.setShopIncomeRatio(shopIncomeRatio);// 商铺收入比例
        orderGoodsSettle.setShopMemberDiscount(shop.getMemberDiscount());// 会员折扣率
        // 计算商铺收入
        // double shopIncomeRatio=1-platPercentage;//商铺的分成比例
        orderGoodsSettle.setShopIncomePrice(NumberUtil.formatDouble(shopIncomeRatio * goodStandardPrice, 4));

        orderGoodsSettle.setPlatformTotalIncomeRatio(platformTotalIncomeRatio);
        orderGoodsSettle.setUserRefShareUserType(user.getReferType());
        // if(user)
        if (user.getReferType() != null && CommonConst.USER_RECOMMAND_BY_USER.compareTo(user.getReferType()) == 0)
        {
            orderGoodsSettle.setUserRefShareUserId(user.getReferUserId());
        }
        else
        {
            orderGoodsSettle.setUserRefShareUserId(user.getReferShopId());
        }
        // 计算平台收入
        double platformIncome = NumberUtil.formatDouble(platformTotalIncomeRatio * goodStandardPrice, 4);
        orderGoodsSettle.setPlatformTotalIncomePrice(platformIncome);
        if (orderGoodsSettleSetting != null)
        {
            // 用户收入:自身返点计算
            double userRatio = ((BigDecimal) orderGoodsSettleSetting.get("userShareRatio")).doubleValue();
            orderGoodsSettle.setUserShareRatio(userRatio);
            orderGoodsSettle.setUserSharePrice(NumberUtil.formatDouble(platformIncome * userRatio, 4));
            orderGoodsSettle.setUserShareSettleFlag(0);
            // 会员推荐人分成计算
            double userRefShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("userRefShareRatio")).doubleValue();
            orderGoodsSettle.setUserRefShareRatio(userRefShareRatio);
            // orderGoodsSettle.setUserRefShareUserId(user.getReferUserId());
            if (user.getReferType() != null && CommonConst.USER_RECOMMAND_BY_SHOP.compareTo(user.getReferType()) == 0)
            { // 用户的推荐类型是商铺{
                if (null == user.getReferShopId())
                {
                    logger.info(
                            "会员推荐店铺id为空，不参与分成" + "订单id为" + orderGoodsSettle.getOrderId() + ",商品id为" + orderGoodsSettle
                                    .getGoodsId());
                    orderGoodsSettle.setUserRefSharePrice(0D);
                }
                else
                {
                    ShopDto recommandShop = shopDao.getShopById(user.getReferShopId());
                    if (recommandShop == null)
                    {
                        logger.info("会员推荐店铺在数据库中不存在，不参与分成" + "订单id为" + orderGoodsSettle.getOrderId() + ",商品id为"
                                + orderGoodsSettle.getGoodsId());
                        orderGoodsSettle.setUserRefSharePrice(0D);
                    }
                    else
                    {
                        orderGoodsSettle
                                .setUserRefSharePrice(NumberUtil.formatDouble(platformIncome * userRefShareRatio, 4));
                    }
                }
            }
            else
            {
                if (null == user.getReferUserId() || 0 == user.getReferUserId())
                {
                    logger.info("会员推荐人为空，不参与分成");
                    orderGoodsSettle.setUserRefSharePrice(0D);
                }
                else
                {
                    UserDto refUser = userDao.getUserById(user.getReferUserId());
                    if (refUser != null)
                    {
                        orderGoodsSettle
                                .setUserRefSharePrice(NumberUtil.formatDouble(platformIncome * userRefShareRatio, 4));
                    }
                    else
                    {
                        logger.info("会员推荐人账户不存在");
                        orderGoodsSettle.setUserRefSharePrice(0D);
                    }
                }
            }
            orderGoodsSettle.setUserRefShareSettleFlag(0);
            // 店铺推荐人分成计算
            double shopRefShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("shopRefShareRatio")).doubleValue();
            orderGoodsSettle.setShopRefShareUserId(shop.getReferUserId());
            orderGoodsSettle.setShopRefShareUserType(shop.getReferUserType());
            orderGoodsSettle.setShopRefShareRatio(shopRefShareRatio);
            if (null == shop.getReferUserId() || 0 == shop.getReferUserId())
            {
                orderGoodsSettle.setShopRefSharePrice(0D);
                logger.info("店铺没有推荐人");
            }
            else
            {
                UserDto shopRefUser = userDao.getUserById(shop.getReferUserId());
                if (shopRefUser != null)
                {
                    orderGoodsSettle
                            .setShopRefSharePrice(NumberUtil.formatDouble(platformIncome * shopRefShareRatio, 4));
                }
                else
                {
                    logger.info("店铺推荐人账户不存在");
                    orderGoodsSettle.setShopRefSharePrice(0D);
                }
            }
            orderGoodsSettle.setShopRefShareSettleFlag(0);
            // 店铺服务人员分成
            double shopServeShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("shopServeShareRatio"))
                    .doubleValue();
            orderGoodsSettle.setShopServeShareSettleFlag(0);
            orderGoodsSettle.setShopServeShareUserId(shop.getShopServerUserId());
            orderGoodsSettle.setShopServeShareRatio(shopServeShareRatio);
            if (null == shop.getShopServerUserId() || 0 == shop.getShopServerUserId())
            {
                logger.info("店铺服务人员不存在，不参与分成");
                orderGoodsSettle.setShopServeSharePrice(0D);
            }
            else
            {
                UserDto shopServerUser = userDao.getUserById(shop.getShopServerUserId());
                if (shopServerUser != null)
                {
                    orderGoodsSettle
                            .setShopServeSharePrice(NumberUtil.formatDouble(platformIncome * shopServeShareRatio, 4));
                }
                else
                {
                    logger.info("店铺服务人员账户不存在");
                    orderGoodsSettle.setShopServeSharePrice(0D);
                }

            }
            // 一级代理分成
            double level1AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level1AgentShareRatio"))
                    .doubleValue();
            orderGoodsSettle.setLevel1AgentShareSettleFlag(0);
            AgentDto level1Agent = null;
            if (null != shop.getCityId())
            {
                level1Agent = agentDao.getAgent(null, shop.getCityId(), null, null, 31, order.getOrderTime());
            }
            if (level1Agent != null)
            {
                if (level1Agent.getAgentShareRatio() != null && level1Agent.getAgentShareRatio() != 0)
                {
                    level1AgentShareRatio = level1Agent.getAgentShareRatio();
                }
                orderGoodsSettle.setLevel1AgentShareUserId(level1Agent.getUserId());
                orderGoodsSettle
                        .setLevel1AgentPrice(NumberUtil.formatDouble(platformIncome * level1AgentShareRatio, 4));
            }
            else
            {
                logger.info("一级代理不存在，不参与分成");
                orderGoodsSettle.setLevel1AgentPrice(0D);
            }
            // 二级代理分成
            double level2AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level2AgentShareRatio"))
                    .doubleValue();
            orderGoodsSettle.setLevel2AgentShareSettleFlag(0);
            AgentDto level2Agent = null;
            if (null != shop.getCityId() && null != shop.getDistrictId())
            {
                level2Agent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(), null, 32,
                        order.getOrderTime());
            }
            if (level2Agent != null)
            {
                if (level2Agent.getAgentShareRatio() != null && level2Agent.getAgentShareRatio() != 0)
                {
                    level2AgentShareRatio = level2Agent.getAgentShareRatio();
                }
                orderGoodsSettle.setLevel2AgentShareUserId(level2Agent.getUserId());
                orderGoodsSettle
                        .setLevel2AgentPrice(NumberUtil.formatDouble(platformIncome * level2AgentShareRatio, 4));
            }
            else
            {
                logger.info("二级代理不存在，不参与分成");
                orderGoodsSettle.setLevel2AgentPrice(0D);
            }
            // 三级代理分成
            double level3AgentShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("level3AgentShareRatio"))
                    .doubleValue();

            orderGoodsSettle.setLevel3AgentShareSettleFlag(0);
            AgentDto level3Agent = null;
            if (null != shop.getCityId() && null != shop.getDistrictId() && null != shop.getTownId())
            {
                level3Agent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                        shop.getTownId().longValue(), 33, order.getOrderTime());
            }
            if (level3Agent != null)
            {

                if (level3Agent.getAgentShareRatio() != null && level3Agent.getAgentShareRatio() != 0)
                {
                    level3AgentShareRatio = level3Agent.getAgentShareRatio();
                }
                orderGoodsSettle.setLevel3AgentShareUserId(level3Agent.getUserId());
                orderGoodsSettle
                        .setLevel3AgentPrice(NumberUtil.formatDouble(platformIncome * level3AgentShareRatio, 4));
            }
            else
            {
                logger.info("三级代理不存在，不参与分成");
                orderGoodsSettle.setLevel3AgentPrice(0D);
            }

            double operatorsShareRatio = ((BigDecimal) orderGoodsSettleSetting.get("operatorsShareRatio"))
                    .doubleValue();// 运营商分成比例
            AgentDto operators = null;
            if (null != shop.getCityId() && null != shop.getDistrictId() && null != shop.getTownId())
            {
                operators = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                        shop.getTownId().longValue(), 50, order.getOrderTime());
            }
            if (operators != null)
            {
                if (operators.getAgentShareRatio() != null && operators.getAgentShareRatio() != 0)
                {
                    operatorsShareRatio = operators.getAgentShareRatio();
                }
                UserDto operatorsUser = userDao.getUserById(operators.getUserId());
                if (operatorsUser != null)
                {
                    orderGoodsSettle
                            .setOperatorsPrice(NumberUtil.formatDouble(platformIncome * operatorsShareRatio, 4));
                }
            }
            else
            {
                logger.info("运营商不存在，不参与分成");
            }
            orderGoodsSettle.setLevel1AgentShareRatio(level1AgentShareRatio);
            orderGoodsSettle.setLevel2AgentShareRatio(level2AgentShareRatio);
            orderGoodsSettle.setLevel3AgentShareRatio(level3AgentShareRatio);

            /* 20160505添加连锁店铺 */
            // 设置连锁店推广员分成
            Long integrationPromotionUserId = shop.getIntegrationPromotionUserId();
            Double integrationPromotionShareRatio = (
                    shop.getIntegrationPromotionRatio() == null ? new BigDecimal(0) : shop
                            .getIntegrationPromotionRatio()).doubleValue();
            Double integrationPromotionPrice = NumberUtil
                    .formatDouble(platformIncome * integrationPromotionShareRatio, 4);
            orderGoodsSettle.setIntegrationPromotionUserId(integrationPromotionUserId);
            orderGoodsSettle.setIntegrationPromotionUserPrice(integrationPromotionPrice);
            orderGoodsSettle.setIntegrationPromotionRatio(integrationPromotionShareRatio);
            // 设置连锁店促成人分成
            Long integrationFacilitateUserId = shop.getIntegrationFacilitateUserId();
            Double integrationFacilitateUserShareRatio = (
                    shop.getIntegrationFacilitateRatio() == null ? new BigDecimal(0) : shop
                            .getIntegrationFacilitateRatio()).doubleValue();
            Double integrationFacilitatePrice = NumberUtil
                    .formatDouble(platformIncome * integrationFacilitateUserShareRatio, 4);
            orderGoodsSettle.setIntegrationFacilitateUserId(integrationFacilitateUserId);
            orderGoodsSettle.setIntegrationfacilitateUserPrice(integrationFacilitatePrice);
            orderGoodsSettle.setIntegrationFacilitateRatio(integrationFacilitateUserShareRatio);
            // 连锁店主分成
            Long integrationPrincipalUserId = shop.getIntegrationPrincipalUserId();
            Double integrationPrincipalUserShareRatio = (
                    shop.getIntegrationPrincipalRatio() == null ? new BigDecimal(0) : shop
                            .getIntegrationPrincipalRatio()).doubleValue();
            Double integrationPrincipalUserPrice = NumberUtil
                    .formatDouble(platformIncome * integrationPrincipalUserShareRatio, 4);
            orderGoodsSettle.setIntegrationFacilitateUserId(integrationPrincipalUserId);
            orderGoodsSettle.setIntegrationfacilitateUserPrice(integrationPrincipalUserPrice);
            orderGoodsSettle.setIntegrationPrincipalRatio(integrationPrincipalUserShareRatio);

            // 平台净收入
            Double platformNetIncome = 0.0;
            platformNetIncome =
                    platformIncome - orderGoodsSettle.getUserSharePrice() - orderGoodsSettle.getUserRefSharePrice()
                            - orderGoodsSettle.getShopRefSharePrice() - orderGoodsSettle.getShopServeSharePrice()
                            - orderGoodsSettle.getLevel1AgentPrice() - orderGoodsSettle.getLevel2AgentPrice()
                            - orderGoodsSettle.getLevel3AgentPrice() - orderGoodsSettle.getOperatorsPrice()
                            - integrationPromotionPrice - integrationFacilitatePrice - integrationPrincipalUserPrice;
            BigDecimal bdPlatformNetIncome = new BigDecimal(platformNetIncome);
            bdPlatformNetIncome = bdPlatformNetIncome.setScale(4, BigDecimal.ROUND_HALF_UP);
            platformNetIncome = bdPlatformNetIncome.doubleValue();
            orderGoodsSettle.setPlatformNetIncomePrice(platformNetIncome);
        }
        return orderGoodsSettle;
    }

    /**
     * 插入用户账单
     *
     * @Title: insertUserBill @param @param orderGoodsSettle @param @param
     * goods @param @param user @param @param shop @param @throws
     * Exception @return void 返回类型 @throws
     */
    public void insertUserBill(OrderGoodsSettle orderGoodsSettle, GoodsDto goods, OrderDto order, UserDto user,
            ShopDto shop) throws Exception
    {
        UserBillDto userBillDto = null;
        PlatformBillDto platformBillDto = null;
        UserDto userDto = null;
        if (orderGoodsSettle.getUserSharePrice() != 0D)
        {

            // 用户账单
            // userBillDto = buildUserBill(orderGoodsSettle, goods, user,
            // "推荐奖励", order.getOrderTitle(),
            // CommonConst.BILL_DIRECTION_ADD,
            // CommonConst.USER_BILL_TYPE_CONSUMER_REBATE,
            // CommonConst.USER_ACCOUNT_TYPE_REWARD);
            /* 20160511应要求将bill_title改为店铺名 */
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", order.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_CONSUMER_REBATE,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            userBillDto.setUserId(user.getUserId());
            userBillDto.setMoney(orderGoodsSettle.getUserSharePrice());
            userDto = userDao.getUserById(userBillDto.getUserId());
            if (null != userDto)
            {
                userBillDao.insertUserBillMiddle(userBillDto);
                platformBillDto = buildPlatformBill(orderGoodsSettle, user, "支付会员奖励", CommonConst.BILL_DIRECTION_DOWN,
                        CommonConst.PLATFORM_BILL_STATUS_ING, CommonConst.PLATFORM_BILL_TYPE_MEMBERAWARD);
                platformBillDto.setMoney(-orderGoodsSettle.getUserSharePrice());
                platformBillDto.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB); // 资金来源跟去向，传奇宝
                platformBillDto.setBillDesc("平台分账");
                platformBillDao.insertPlatformBillMiddle(platformBillDto);
            }
        }
        /* 20160504连锁店铺 */
        // 连锁店铺推广人
        if (orderGoodsSettle.getIntegrationPromotionUserId() != null
                && orderGoodsSettle.getIntegrationPromotionUserPrice() != 0)
        {
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", order.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_INTEGRATION_PROMOTION,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            // set userRole
            UserDto temp = userDao.getUserById(Long.valueOf(orderGoodsSettle.getIntegrationPromotionUserId()));
            if (temp != null)
            {
                userBillDto.setUserRole(temp.getUserType() + "");
            }
            // 给推荐会员的人分账，生成用户和平台账单
            insertCommmonPlateBill(orderGoodsSettle.getIntegrationPromotionUserId(), null, orderGoodsSettle,
                    userBillDto, userDto, orderGoodsSettle.getIntegrationPromotionUserPrice(), "支付连锁店推广员奖励",
                    CommonConst.PLATFORM_BILL_TYPE_INTEGRATION_PROMOTION);
        }
        // 连锁店铺促成人
        if (orderGoodsSettle.getIntegrationFacilitateUserId() != null
                && orderGoodsSettle.getIntegrationfacilitateUserPrice() != 0)
        {
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", order.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_INTEGRATION_FACILITATE,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            // set userRole
            UserDto temp = userDao.getUserById(Long.valueOf(orderGoodsSettle.getIntegrationFacilitateUserId()));
            if (temp != null)
            {
                userBillDto.setUserRole(temp.getUserType() + "");
            }
            // 给推荐会员的人分账，生成用户和平台账单
            insertCommmonPlateBill(orderGoodsSettle.getIntegrationFacilitateUserId(), null, orderGoodsSettle,
                    userBillDto, userDto, orderGoodsSettle.getIntegrationfacilitateUserPrice(), "支付连锁店促成人奖励",
                    CommonConst.PLATFORM_BILL_TYPE_INTEGRATION_FACILITATE);
        }
        // 连锁店主
        if (orderGoodsSettle.getIntegrationFrincipalUserId() != null
                && orderGoodsSettle.getIntegrationFrincipalUserPrice() != 0)
        {
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", order.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_INTEGRATION_PRINCIPAL,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            // set userRole
            UserDto temp = userDao.getUserById(Long.valueOf(orderGoodsSettle.getIntegrationFrincipalUserId()));
            if (temp != null)
            {
                userBillDto.setUserRole(temp.getUserType() + "");
            }
            // 给推荐会员的人分账，生成用户和平台账单
            insertCommmonPlateBill(orderGoodsSettle.getIntegrationFrincipalUserId(), null, orderGoodsSettle,
                    userBillDto, userDto, orderGoodsSettle.getIntegrationFrincipalUserPrice(), "支付连锁店主奖励",
                    CommonConst.PLATFORM_BILL_TYPE_INTEGRATION_PRINCIPAL);
        }

        // 消费的用户是会员推荐的，用户账单
        if (user.getReferType() != null
                && CommonConst.USER_RECOMMAND_BY_USER.compareTo(user.getReferType()) == 0)// 如果用户是用户推荐的
        {
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", user.getMobile(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_USER_REWARD,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            // 给推荐会员的人分账，生成用户和平台账单
            insertCommmonPlateBill(user.getReferUserId(), null, orderGoodsSettle, userBillDto, userDto,
                    orderGoodsSettle.getUserRefSharePrice(), "支付推荐会员奖励", CommonConst.PLATFORM_BILL_TYPE_RECOMAWARDS);
        }
        else
        // 消费的用户是店铺推荐的
        {
            ShopBillDto shopBillDto = buildShopBill(orderGoodsSettle, user, CommonConst.SHOP_BILL_TYPE_REWARD,
                    user.getMobile(), CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_ACCOUNT_TYPE_REWARD);// 生成商铺账单
            Long referShopId = user.getReferShopId();// 代表用户是被这个商铺推荐的
            if (referShopId != null)
            {
                ShopDto referShopDto = shopDao.getShopById(referShopId);
                if (referShopDto != null)// 触发此分账
                {
                    shopBillDto.setShopId(referShopId);
                    insertCommonPlateBillForShop(orderGoodsSettle, user, shopBillDto,
                            orderGoodsSettle.getUserRefSharePrice(), "支付推荐会员奖励",
                            CommonConst.PLATFORM_BILL_TYPE_RECOMAWARDS);
                }
            }
        }
        // 消费所在店铺的推荐人是用户
        if (shop.getReferUserType() != null
                && CommonConst.SHOP_RECOMMAND_BY_SHOP.compareTo(shop.getReferUserType()) == 0)
        {
            ShopDto referShop = null;
            Long referShopId = shop.getReferShopId();
            if (referShopId != null && referShopId != 0)
            {
                referShop = shopDao.getShopById(referShopId);
            }
            else
            {
                Long referShopPrincipalId = shop.getReferUserId();// 店铺的推荐者为其它店铺的负责人
                if (referShopPrincipalId != null)
                {
                    referShop = shopDao.getShopByPrincipalId(referShopPrincipalId);
                }
            }
            ShopBillDto shopBillDto = buildShopBill(orderGoodsSettle, user, CommonConst.SHOP_BILL_TYPE_REWARD,
                    shop.getShopName(), CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_ACCOUNT_TYPE_REWARD);
            if (referShop != null)
            {
                shopBillDto.setShopId(referShop.getShopId());
                insertCommonPlateBillForShop(orderGoodsSettle, user, shopBillDto,
                        orderGoodsSettle.getShopRefSharePrice(), "支付推荐商铺奖励", CommonConst.PLATFORM_BILL_TYPE_SHOPRWARDS);
            }
        }
        else
        {
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_SHOP_REWARD,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);
            // 给推荐对铺的分账相应扣除平台账户，生成平台账单
            insertCommmonPlateBill(shop.getReferUserId(), null, orderGoodsSettle, userBillDto, user,
                    orderGoodsSettle.getShopRefSharePrice(), "支付推荐商铺奖励", CommonConst.PLATFORM_BILL_TYPE_SHOPRWARDS);
        }
        // 店铺服务人员用户账单
        userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_SERVICE_SHOP_REWARD,
                CommonConst.USER_ACCOUNT_TYPE_REWARD);
        // 相应扣除平台账户给服务人员的分账，生成平台账单
        insertCommmonPlateBill(shop.getShopServerUserId(), null, orderGoodsSettle, userBillDto, user,
                orderGoodsSettle.getShopServeSharePrice(), "支付服务店铺费", CommonConst.PLATFORM_BILL_TYPE_SERVE);
        if (shop.getCityId() != null)
        {
            // 一级代理账单
            userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                    CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_CITY_AGENT_REWARD,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD);

            AgentDto agent1 = agentDao
                    .getAgent(null, shop.getCityId(), null, null, 31, orderGoodsSettle.getOrderTime());
            // 插入一级代理商账单
            if (agent1 != null)
            {
                insertCommmonPlateBill(agent1.getUserId(), agent1, orderGoodsSettle, userBillDto, userDto,
                        orderGoodsSettle.getLevel1AgentPrice(), "支付地市级代理费", CommonConst.PLATFORM_BILL_TYPE_1AGENT);
            }
            if (null != shop.getDistrictId())
            {
                // 二级代理账单
                userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                        CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_AREA_AGENT_REWARD,
                        CommonConst.USER_ACCOUNT_TYPE_REWARD);
                AgentDto agent2 = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(), null, 32,
                        orderGoodsSettle.getOrderTime());
                // 插入二级代理商账单
                if (agent2 != null)
                {
                    insertCommmonPlateBill(agent2.getUserId(), agent2, orderGoodsSettle, userBillDto, userDto,
                            orderGoodsSettle.getLevel2AgentPrice(), "支付区县级代理费", CommonConst.PLATFORM_BILL_TYPE_2AGENT);
                }
                if (null != shop.getTownId())
                {
                    // 三级代理账单
                    userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                            CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_TOWN_AGENT_REWARD,
                            CommonConst.USER_ACCOUNT_TYPE_REWARD);
                    AgentDto agent3 = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                            shop.getTownId().longValue(), 33, orderGoodsSettle.getOrderTime());
                    // 插入三级代理商账单
                    if (agent3 != null)
                    {
                        insertCommmonPlateBill(agent3.getUserId(), agent3, orderGoodsSettle, userBillDto, userDto,
                                orderGoodsSettle.getLevel3AgentPrice(), "支付乡镇级代理费",
                                CommonConst.PLATFORM_BILL_TYPE_3AGENT);
                    }

                    // 运营商账单
                    userBillDto = buildUserBill(orderGoodsSettle, goods, user, "推荐奖励", shop.getShopName(),
                            CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_BILL_TYPE_OPERATORS_REWARD,
                            CommonConst.USER_ACCOUNT_TYPE_REWARD);
                    AgentDto operators = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(),
                            shop.getTownId().longValue(), 50, orderGoodsSettle.getOrderTime());
                    // 插入运营商商账单
                    if (operators != null)
                    {
                        insertCommmonPlateBill(operators.getUserId(), operators, orderGoodsSettle, userBillDto, userDto,
                                orderGoodsSettle.getOperatorsPrice(), "支付运营商奖励",
                                CommonConst.PLATFORM_BILL_TYPE_OPERATORS_REWARD);
                    }
                }
            }

        }
    }

    /**
     * 插入平台分账账单
     *
     * @Title: insertCommmonPlateBill @param relateUserId关联的用户编号
     * 获利人的userId @param agent 代理商 @param shop @param
     * orderGoodsSettle @param userBillDto 分账关联的账单对象 @param user @param
     * billMoney 账单金额 @param billType 账单类型 @param @throws
     * Exception @return void 返回类型 @throws
     */
    private void insertCommmonPlateBill(Long relateUserId, AgentDto agent, OrderGoodsSettle orderGoodsSettle,
            UserBillDto userBillDto, UserDto user, Double billMoney, String billType, int platformBillType)
            throws Exception
    {
        if (!isNull(billMoney))
        {
            if (relateUserId != null)
            {
                userBillDto.setUserId(relateUserId);
            }
            if (agent != null)
            {
                userBillDto.setAgentId(agent.getAgentId());
                userBillDto.setUserRole(NumberUtil.getStr(agent.getAgentType()));
            }
            userBillDto.setMoney(billMoney);
            userBillDao.insertUserBillMiddle(userBillDto);
            PlatformBillDto platformBillDto = buildPlatformBill(orderGoodsSettle, user, billType,
                    CommonConst.BILL_DIRECTION_DOWN, CommonConst.PLATFORM_BILL_STATUS_ING, platformBillType);
            platformBillDto.setMoney(-billMoney);
            platformBillDto.setBillDesc("平台分账");
            platformBillDto.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB); // 资金来源跟去向，传奇宝
            platformBillDao.insertPlatformBillMiddle(platformBillDto);
        }
    }

    /**
     * 针对商铺账单有变更，对应生成平台账单
     *
     * @Title: insertCommonPlateBillForShop @param shopId 参与分账的商铺 @param
     * orderGoodsSettle @param userDto 哪个用户消费的 @param shopBillDto
     * 生成的商铺订单 @param billMoney 账单金额 @param billType 账单类型 @param @throws
     * Exception @return void 返回类型 @throws
     */
    private void insertCommonPlateBillForShop(OrderGoodsSettle orderGoodsSettle, UserDto userDto,
            ShopBillDto shopBillDto, Double billMoney, String billType, int platformBillType) throws Exception
    {
        if (userDto != null && orderGoodsSettle != null && shopBillDto != null && !isNull(billMoney))
        {
            shopBillDto.setMoney(billMoney);
            ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopBillDto.getShopId());
            Double accountAfterAmount = billMoney;
            if (null != shopBillDto)
            {
                shopBillDto.setAccountAmount(shopAccountDto.getAmount());
                accountAfterAmount = NumberUtil.add(shopAccountDto.getRewardAmount(), billMoney);
            }
            shopBillDto.setAccountAfterAmount(accountAfterAmount);
            shopBillDao.insertShopMiddleBill(shopBillDto);
            PlatformBillDto platformBillDto = buildPlatformBill(orderGoodsSettle, userDto, billType,
                    CommonConst.BILL_DIRECTION_DOWN, CommonConst.PLATFORM_BILL_STATUS_ING, platformBillType);
            platformBillDto.setMoney(-billMoney);
            platformBillDto.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB); // 资金来源跟去向，传奇宝
            platformBillDto.setBillDesc("平台分账");
            platformBillDao.insertPlatformBillMiddle(platformBillDto);
        }
    }

    /**
     * 记录商铺账单
     *
     * @param order              订单对象
     * @param UserDto            消费者
     * @param payAmount          商铺已收款
     * @param platformTotalPrice 平台总收入
     * @throws Exception
     */
    public void insertShopBill(OrderDto order, Double payAmount, Double platformTotalPrice, Double voucherPayAmount)
            throws Exception
    {
        /**
         * 分成逻辑： 商铺线上营业收入账单收入=订单实付款-店铺已收款项 保证金账单收入=-平台总收入
         * 所有商铺账单存在两条账单一条店铺线上营业收入新增，另一条保证金减少
         *
         */
        Double onlineBillMoney = NumberUtil.sub(order.getSettlePrice(), payAmount); // 账单流水
        // 平台账单
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
        platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBillDto.setOrderId(order.getOrderId());
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setConsumerUserId(order.getUserId());
        UserDto user = userDao.getUserById(order.getUserId());
        platformBillDto.setConsumerMobile(user.getMobile());
        platformBillDto.setGoodsSettlePrice(order.getOrderRealSettlePrice());
        platformBillDto.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB); // 资金来源跟去向，传奇宝
        if (onlineBillMoney > 0)
        {
            // 营业收入账单
            ShopBillDto onlineBillDto = new ShopBillDto();
            createShopBill(onlineBillDto, order, payAmount, platformTotalPrice, onlineBillMoney,
                    CommonConst.SHOP_BILL_TYPE_SALE, CommonConst.SHOP_ACCOUNT_TYPE_INCOME);
            StringBuffer commentBuffer = new StringBuffer();
            commentBuffer.append("店铺线上营业收入").append(NumberUtil.roundDoubleToStr(onlineBillMoney, 4)).append("元")
                    .append(" = ").append("用户实付款").append(order.getSettlePrice()).append("元 - 店铺已收款金额")
                    .append(payAmount).append("元");
            onlineBillDto.setComment(commentBuffer.toString());
            shopBillDao.insertShopMiddleBill(onlineBillDto);

            platformBillDto.setBillType("支付商铺线上营业收入");
            platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_ONLINE);
            platformBillDto.setMoney(-onlineBillMoney);
            platformBillDto.setBillDesc("返还商铺线上营业收入");
            platformBillDao.insertPlatformBillMiddle(platformBillDto);

        }

        if (voucherPayAmount > 0)
        {
            Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
            Object voucherMoney4RatioConfig = configMap.get("b2_voucher_ratio_money4");  //红店每人返利比例
            Double voucherMoney4Ratio =
                    voucherMoney4RatioConfig == null ? 0 : Double.valueOf(voucherMoney4RatioConfig.toString());
            Double voucherRebateMoney = NumberUtil.multiply(voucherPayAmount, voucherMoney4Ratio);
            ShopBillDto marketBillDto = new ShopBillDto();
            createShopBill(marketBillDto, order, payAmount, platformTotalPrice, voucherRebateMoney,
                    CommonConst.SHOP_BILL_TYPE_SALE, CommonConst.SHOP_ACCOUNT_TYPE_MARKET);
            marketBillDto.setComment("用户使用代金券支付，代金券转成生意金,每日返还比例：" + voucherMoney4Ratio);
            shopBillDao.insertShopMiddleBill(marketBillDto);
            platformBillDto.setBillType("支付商铺线上代金券转生意金");
            platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_BACK_MARKET);
            platformBillDto.setMoney(-voucherRebateMoney);
            platformBillDto.setBillDesc("支付商铺线上代金券转生意金");
            platformBillDao.insertPlatformBillMiddle(platformBillDto);
            shopAccountDao
                    .updateShopAccount(order.getShopId(), null, null, null, null, null, null, null, null, null, null,
                            voucherPayAmount, voucherRebateMoney);
        }

        ShopBillDto depositBillDto = new ShopBillDto();

        createShopBill(depositBillDto, order, payAmount, platformTotalPrice, -platformTotalPrice,
                CommonConst.SHOP_BILL_TYPE_PAY, CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT);
        StringBuffer commentBuffer = new StringBuffer();
        commentBuffer.append("支付平台服务费").append(NumberUtil.roundDoubleToStr(platformTotalPrice, 4));
        depositBillDto.setComment(commentBuffer.toString());
        shopBillDao.insertShopMiddleBill(depositBillDto);
    }

    private void createShopBill(ShopBillDto shopBillDto, OrderDto order, Double payAmount, Double platformTotalPrice,
            Double money, String billType, int accountType)
    {
        shopBillDto.setBillType(billType);
        if (money >= 0)
        {
            shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        }
        else
        {
            shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
        }
        shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
        shopBillDto.setMoney(money);
        shopBillDto.setShopId(order.getShopId());
        shopBillDto.setOrderId(order.getOrderId());
        shopBillDto.setSettlePrice(order.getSettlePrice());
        shopBillDto.setPlatformTotalIncomePrice(platformTotalPrice);
        shopBillDto.setPayAmount(payAmount);
        shopBillDto.setAccountType(accountType);
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setBillTitle(order.getOrderTitle());
        shopBillDto.setBillDesc("分账");
    }

    public void insertPlatformBill(OrderGoodsSettle orderGoodsSettle, UserDto user) throws Exception
    {
        if (orderGoodsSettle.getPlatformTotalIncomePrice() != 0D)
        {
            PlatformBillDto platformBillDto = null;
            // 平台收入账单记录
            platformBillDto = buildPlatformBill(orderGoodsSettle, user, "销售提成", CommonConst.BILL_DIRECTION_ADD,
                    CommonConst.PLATFORM_BILL_STATUS_ING, CommonConst.PLATFORM_BILL_TYPE_SALE);
            platformBillDto.setMoney(orderGoodsSettle.getPlatformTotalIncomePrice());
            platformBillDto.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB); // 资金来源跟去向，传奇宝
            platformBillDto.setBillDesc("平台收入");
            platformBillDao.insertPlatformBillMiddle(platformBillDto);
        }

    }

    /**
     * 构造平台账单
     *
     * @param orderGoodsSettle 对象
     * @param user             消费者对象
     * @param billType         账单类型 仅参考
     * @param billDirection    账单类型:1（账户资金增加）,-1（账户资金减少）
     * @param billStatus
     * @param platformBillType 账单类型 数字枚举
     * @return
     */
    public PlatformBillDto buildPlatformBill(OrderGoodsSettle orderGoodsSettle, UserDto user, String billType,
            int billDirection, int billStatus, int platformBillType)
    {
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillType(billType);
        platformBillDto.setBillDirection(billDirection);
        platformBillDto.setBillStatus(billStatus);
        platformBillDto.setOrderId(orderGoodsSettle.getOrderId());
        platformBillDto.setGoodsId(orderGoodsSettle.getGoodsId());
        platformBillDto.setGoodsNumber(orderGoodsSettle.getGoodsNumber());
        platformBillDto.setGoodsSettlePrice(orderGoodsSettle.getGoodsStandardPrice());
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setConsumerUserId(user != null ? user.getUserId() : Long.valueOf(0));
        platformBillDto.setConsumerMobile(user != null ? user.getMobile() : "0");
        platformBillDto.setPlatformBillType(platformBillType);
        return platformBillDto;
    }

    /**
     * 构造用户账单
     *
     * @param orderGoodsSettle
     * @param goods
     * @param user
     * @param billType
     * @param billDirection
     * @param billStatus
     * @return
     */
    public UserBillDto buildUserBill(OrderGoodsSettle orderGoodsSettle, GoodsDto goods, UserDto user, String billType,
            String billTitle, int billDirection, int userBillType, int accountType)
    {
        UserBillDto userBillDto = new UserBillDto();
        userBillDto.setBillType(billType);
        userBillDto.setUserRole(NumberUtil.getStr(user.getUserType()));
        userBillDto.setAccountType(accountType);
        userBillDto.setUserBillType(userBillType);
        userBillDto.setBillDirection(billDirection);
        userBillDto.setBillStatus(RewardsEnum.HAVE_NOT_SETTLEMENT.getValue());
        userBillDto.setOrderId(orderGoodsSettle.getOrderId());
        userBillDto.setGoodsId(orderGoodsSettle.getGoodsId());
        userBillDto.setGoodsNumber(orderGoodsSettle.getGoodsNumber());
        userBillDto.setGoodsSettlePrice(orderGoodsSettle.getGoodsStandardPrice());
        userBillDto.setCreateTime(new Date());
        userBillDto.setBillTitle(billTitle);
        if (goods != null && null != goods.getGoodsLogo1())
        {
            userBillDto.setBillLogo(goods.getGoodsLogo1());
        }
        userBillDto.setBillDesc("分账");
        userBillDto.setConsumerUserId(user.getUserId());
        userBillDto.setConsumerMobile(user.getMobile());
        userBillDto.setBillStatusFlag(1);
        return userBillDto;
    }

    /**
     * 构建商铺账单
     *
     * @Title: buildShopBill @param orderGoodsSettle @param goods @param
     * shopDto @param billType @param billTitle @param
     * billDirection @param accountType 账单类型 @return ShopBillDto
     * 返回类型 @throws
     */
    private ShopBillDto buildShopBill(OrderGoodsSettle orderGoodsSettle, UserDto user, String billType,
            String billTitle, int billDirection, int accountType)
    {
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setBillType(billType);
        shopBillDto.setBillDirection(billDirection);
        shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_ING);
        shopBillDto.setOrderId(orderGoodsSettle.getOrderId());
        shopBillDto.setGoodsId(orderGoodsSettle.getGoodsId());
        shopBillDto.setGoodsNumber(orderGoodsSettle.getGoodsNumber());
        shopBillDto.setBillDesc(billTitle);
        shopBillDto.setBillTitle(billTitle);
        shopBillDto.setGoodsSettlePrice(orderGoodsSettle.getGoodsStandardPrice());
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setAccountType(accountType);
        shopBillDto.setConsumerMobile(user.getMobile());
        shopBillDto.setConsumerUserId(user.getUserId());
        return shopBillDto;
    }

    @Override public Long getGroupIdByName(String groupName, Long shopId) throws Exception
    {
        return osrDao.getGroupIdByName(groupName, shopId);
    }

    @Override public List<Map<String, Object>> getIntevalByShopId(Long shopId) throws Exception
    {
        return osrDao.getIntevalByShopId(shopId);
    }

    @Override public void syncBookList(OrderDto order, OrderShopResourceGoodDto orderShopResourceGoodDto, UserDto user,
            int handle) throws Exception
    {
        DataJsonDto data = orderShopResourceGoodDto.getData();
        String mobile = data.getMobile();
        String userName = data.getpName();
        Long userId = order.getUserId();
        // 预定商铺资源
        Date serviceTimeFrom = order.getServiceTimeFrom();
        // 预定商铺资源
        Date serviceTimeTo = order.getServiceTimeTo();
        String serviceTimeFromStr = DateUtils.format(serviceTimeFrom, DateUtils.DATETIME_FORMAT);
        String serviceTimeToStr = DateUtils.format(serviceTimeTo, DateUtils.DATETIME_FORMAT);
        List<SeatInfo> listSeaInfo = data.getSeatInfo();
        if (CollectionUtils.isNotEmpty(listSeaInfo))
        {
            treserveShopRsrc(order.getOrderId(), order.getShopId(), serviceTimeFromStr, serviceTimeToStr, userName,
                    mobile, userId, listSeaInfo);
        }
        order.setIsMember(CommonConst.USER_IS_MEMBER);
        // 预定下订单
        placeOrder(order, handle);
        // 支付状态
        Integer payStatus = data.getPayStatus();
        // 预付款
        Double advance = data.getAdvance();
        // 增加支付流水
        if (1 == payStatus && advance > 0)
        {
            PayDto payDto = new PayDto();
            payDto.setPayType(CommonConst.PAY_TYPE_CASH);
            payDto.setPayAmount(advance);
            // 现金支付 TODO
            payDto.setOrderPayType(0);
            payDto.setOrderId(order.getOrderId());
            String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
            payDto.setLastUpdateTime(nowTime);
            payDto.setOrderPayTime(nowTime);
            // 商铺收款
            payDto.setPayeeType(1);
            // 商铺id
            payDto.setShopId(orderShopResourceGoodDto.getShopId());
            payDao.addOrderPay(payDto);
            // 增加支付账单 TODO 现金支付 没有出平台账户故而去除记录
            // UserBillDto userBillDto = new UserBillDto();
            // userBillDto.setBillTitle("消费");
            // userBillDto.setBillType("消费");
            // userBillDto.setMoney(0 - advance);
            // // 已预定状态
            // userBillDto.setBillStatus(ConsumeEnum.ORDERED.getValue());
            // userBillDto.setCreateTime(new Date());
            // // '账单状态的进行中标记：1（进行中），0（已完成）',
            // userBillDto.setBillStatusFlag(1);
            // // 账单类型:1（账户资金增加）,-1（账户资金减少）
            // userBillDto.setBillDirection(-1);
            // if (null != order.getOrderId()) {
            // userBillDto.setOrderId(order.getOrderId());
            // }
            // // 手机号码
            // if (StringUtils.isNotBlank(mobile)) {
            // userBillDto.setConsumerMobile(mobile);
            // userBillDto.setUserId(user.getUserId());
            // }
            // userBillDao.insertUserBill(userBillDto);
        }
    }

    public void add(OrderDto order, List<SeatInfo> listSeaInfo, int handle)
    {

    }

    /**
     * 非会员预定
     *
     * @param xorder
     * @param order
     * @param handle
     * @throws Exception
     */
    public void syncXorder(OrderShopResourceGoodDto xorder, OrderDto order) throws Exception
    {
        DataJsonDto data = xorder.getData();
        List<SeatInfo> listSeaInfo = data.getSeatInfo();
        // 预定下订单
        addXOrder(xorder);
        // 预定商铺资源
        Date serviceTimeFrom = order.getServiceTimeFrom();
        Date serviceTimeTo = order.getServiceTimeTo();
        String serviceTimeFromStr = DateUtils.format(serviceTimeFrom, DateUtils.DATETIME_FORMAT);
        String serviceTimeToStr = DateUtils.format(serviceTimeTo, DateUtils.DATETIME_FORMAT);
        String mobile = data.getMobile();
        String userName = data.getpName();
        treserveShopRsrc(order.getOrderId(), xorder.getShopId(), serviceTimeFromStr, serviceTimeToStr, userName, mobile,
                null, listSeaInfo);
    }

    /**
     * 插入非会员订单
     *
     * @param order
     * @param listSeaInfo
     * @param handle
     * @throws Exception
     */
    public void addXOrder(OrderShopResourceGoodDto xorder) throws Exception
    {
        Long shopId = xorder.getShopId();
        DataJsonDto data = xorder.getData();
        OrderDto newOrder = new OrderDto();
        String xorderId = data.getId();
        newOrder.setOrderId(xorderId);
        newOrder.setShopId(shopId);
        newOrder.setOrderType(1);
        String timeStr = data.getTime();
        Date createTime = DateUtils.parse(timeStr);
        newOrder.setPayStatus(data.getPayStatus());
        newOrder.setSettleFlag(1);
        // newXorder.setUserInfo(data.getMobile());TODO 目前不记录手机号码
        newOrder.setMobile(data.getMobile());
        newOrder.setOrderSceneType(data.getIsWm() == true ? 1 : 0);
        newOrder.setOrderTime(createTime);
        List<BookInfo> listBookInfo = data.getBookInfo();
        Double goodsTotal = 0d;
        if (CollectionUtils.isNotEmpty(listBookInfo))
        {
            goodsTotal = computeTotalPrice(listBookInfo);
            newOrder.setGoodsPriceBeforeDiscount(goodsTotal);
            // 折后 TODO
            newOrder.setGoodsPrice(goodsTotal);
        }
        // 默认未结算
        newOrder.setSettleFlag(0);
        newOrder.setOrderSceneType(data.getOrderSceneType());
        // 默认已开单
        newOrder.setOrderStatus(0);
        // TODO 记录非会员手机号码
        newOrder.setIsMember(CommonConst.USER_IS_NOT_MEMBER);
        // 增加非会员订单信息
        this.orderDao.saveOrder(newOrder);
        // //增加非会员商品信息
        addXOrderList(listBookInfo, shopId, xorderId, goodsTotal, data.getUserRemark());
    }

    /**
     * 插入非会员订单
     *
     * @throws Exception
     */
    public void addXOrderList(List<BookInfo> listBookInfo, Long shopId, String xorderId, Double goodsTotal,
            String userRemark) throws Exception
    {
        if (CollectionUtils.isNotEmpty(listBookInfo))
        {
            for (BookInfo book : listBookInfo)
            {
                OrderGoodsDto orderGoods = new OrderGoodsDto();
                orderGoods.setGoodsId(book.getDishId());
                orderGoods.setGoodsNumber(book.getNum().doubleValue());
                orderGoods.setShopId(shopId);
                orderGoods.setOrderId(xorderId);
                orderGoods.setGoodsSettlePrice(goodsTotal);
                orderGoods.setUserRemark(userRemark);
                this.orderGoodsDao.saveOrderGoods(orderGoods);
            }
        }
    }

    /**
     * TODO 计算商品总价格
     *
     * @param listBookInfo
     * @return
     * @throws Exception
     */
    public Double computeTotalPrice(List<BookInfo> listBookInfo) throws Exception
    {
        Double orderTotalPrice = 0d;
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
                GoodsDto goods = goodsDao.getGoodsById(bookInfo.getDishId());
                // 商品数量
                Double num = bookInfo.getNum();
                // 单价
                float unitPrice = goods.getUnitPrice();
                // 总金额
                orderTotalPrice += num * unitPrice;
            }
        }
        return orderTotalPrice;
    }

    /**
     * 收银机预定商铺资源
     */
    public void treserveShopRsrc(String orderId, Long shopId, String serviceTimeFrom, String serviceTimeTo,
            String userName, String mobile, Long userId, List<SeatInfo> listSeaInfo) throws Exception
    {
        /*
         * groupId int 是 资源分组ID，比如大台 intevalId int 是 时段id，比如中午时段 resourceNumber
         * int 是 预定资源个数 reserveResourceDate date 是 预约商铺资源的日期
         */
        List<OsrsDto> osrs = new ArrayList<OsrsDto>();
        OrderShopRsrcDto oShopRsrcDto = new OrderShopRsrcDto();
        oShopRsrcDto.setOrderId(orderId);
        oShopRsrcDto.setShopId(shopId);
        // 预定日期
        String serviceTimeFromArr[] = serviceTimeFrom.split(" ");
        Date reserveResourceDate = DateUtils.parse(serviceTimeFromArr[0], DateUtils.DATE_FORMAT);
        oShopRsrcDto.setReserveResourceDate(reserveResourceDate);
        // 时间段id
        List<Map<String, Object>> listTime = getIntevalByShopId(shopId);
        Long intevalId = getIntevalId(serviceTimeFrom, listTime);
        String groupName = "";
        for (SeatInfo seatInfo : listSeaInfo)
        {
            OsrsDto osrsDto = new OsrsDto();
            groupName = seatInfo.getSeatCate();
            Integer num = seatInfo.getSeatNum();
            // 资源分组id
            Long groupId = getGroupIdByName(groupName, shopId);
            // 时间段id
            osrsDto.setIntevalId(intevalId);
            osrsDto.setGroupId(groupId);
            osrsDto.setResourceNumber(num);
            osrsDto.setReserveResourceDate(reserveResourceDate);
            // 预约
            osrs.add(osrsDto);
        }
        oShopRsrcDto.setOsrs(osrs);
        oShopRsrcDto.setStartTime(serviceTimeFrom);
        oShopRsrcDto.setEndTime(serviceTimeTo);
        oShopRsrcDto.setUserName(userName);
        oShopRsrcDto.setMobile(mobile);
        oShopRsrcDto.setResourceType(groupName);
        if (null != userId)
        {
            oShopRsrcDto.setUserId(userId);
        }
        // 预定商铺资源
        reserveShopRsrc(oShopRsrcDto);
    }

    /**
     * 获取时间ID
     *
     * @param serviceTimeFrom
     * @param listTime
     */
    public Long getIntevalId(String serviceTimeFrom, List<Map<String, Object>> listTime)
    {
        Long intevalId = null;
        if (StringUtils.isNotBlank(serviceTimeFrom) && null != listTime && listTime.size() != 0)
        {
            String[] serviceTimeFromArray = serviceTimeFrom.split(" ");
            Date serviceTime = DateUtils.parse(serviceTimeFromArray[1], DateUtils.TIME_FORMAT);
            for (Map<String, Object> map : listTime)
            {
                Date endTime = (Date) map.get("endTime");
                Date startTime = (Date) map.get("startTime");
                int strResult = serviceTime.compareTo(startTime);
                int endResult = serviceTime.compareTo(endTime);
                if (strResult >= 0 && endResult <= 0)
                {
                    intevalId = (Long) map.get("intevalId");
                    break;
                }
            }
        }
        return intevalId;
    }

    public Integer getOrderStatusById(String orderId) throws Exception
    {
        return this.orderDao.getOrderStatusById(orderId);
    }

    public void deleteOrder(String userId, String orderId) throws Exception
    {
        OrderDto order = orderDao.getOrderById(orderId);
        CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);

        // 校验用户存不存
        UserDto user = userDao.getUserById(Long.valueOf(userId));
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_ACCOUNT_NOT_EXIST);

        int orderStatus = order.getOrderStatus();
        // 默认为用户删除
        int deleteType = CommonConst.ORDER_DELETE_TYPE_YHS;
        if (CommonConst.ORDER_STS_YTD == orderStatus || CommonConst.ORDER_STS_YJZ == orderStatus)
        {
            Integer originalType = order.getDeleteType() == null ? 0 : order.getDeleteType();
            if (CommonConst.ORDER_DELETE_TYPE_SJS == originalType)
            {
                // 如果商家已删除，则需修改为都删除
                deleteType = CommonConst.ORDER_DELETE_TYPE_LDS;
            }
            orderDao.updateOrderDeleteType(order.getOrderId(), deleteType);
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_ORDER_STATUS_NOT_SUPPORT_DELETE,
                    CodeConst.MSG_ORDER_STATUS_NOT_SUPPORT_DELETE);
        }
    }

    public void delOrder(String orderId)
    {
        // 删除订单商品
        orderDao.delOrderGoods(orderId);
        // 删除订单
        orderDao.delOrder(orderId);
        // 保存订单日志
        OrderDto order = new OrderDto();
        order.setOrderId(orderId);
        try
        {
            saveOrderLog(orderId, "商铺资源已经被占用，订单无效", null);
        }
        catch (Exception e)
        {
            logger.error("扫码下单-商铺资源无效-保存订单日志系统异常");
        }
    }

    public List<Map<String, Object>> getOrder8List(Long shopId) throws Exception
    {
        List<Map<String, Object>> list = orderDao.getOrder8List(shopId);
        for (Map<String, Object> map : list)
        {
            Long userId = (Long) map.get("userId");
            String mobile = getMobileById(userId);
            map.put("mobile", mobile);
            map.remove("userId");
        }
        return list;
    }

    /**
     * 根据userid查询手机号码
     */
    public String getMobileById(Long userId) throws Exception
    {
        UserDto user = userDao.getDBUserById(userId);
        String mobile = "";
        if (null != user)
        {
            mobile = user.getMobile();
        }
        return mobile;
    }

    public List<OrderGoodsDto> getOGoodsListByOrderId(String orderId) throws Exception
    {
        OrderGoodsDto og = new OrderGoodsDto();
        og.setOrderId(orderId);
        return this.orderGoodsDao.getOGoodsListByOrderId(og);
    }

    /**
     * 自动完成订单
     *
     * @Title: autoFinishOrder @param @param orderIds @param @throws
     * Exception @return void 返回类型 @throws
     */
    public void autoFinishOrder(List<String> orderIdList) throws Exception
    {
        batchUpdateOrderStatus(orderIdList);
        List<OrderLogDto> orderLogDtoList = new ArrayList<OrderLogDto>();
        for (String orderId : orderIdList)
        {
            OrderLogDto orderLogDto = new OrderLogDto();
            orderLogDto.setOrderId(orderId);
            orderLogDto.setPayStatus(CommonConst.ORDER_PAYSTS_FINISHED);// 订单支付已完成
            orderLogDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
            orderLogDto.setLastUpdateTime(new Date());
            orderLogDto.setRemark("状态已预订或已完成，支付状态为已支付的订单，超过预定时间24小时，自动将订单变为已完成");
            orderLogDtoList.add(orderLogDto);
            shopTechnicianService
                    .updateTechnicianWorKStatusByOrderId(orderId, CommonConst.TECH_STATUS_KXZ);// 将技师的状态变为空闲
        }
        orderLogDao.batchSaveOrderLogs(orderLogDtoList);
        splitMoney(orderIdList);
    }

    private void splitMoney(List<String> orderIdList)
    {
        for (String orderId : orderIdList)
        {
            try
            {
                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, 0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void batchUpdateOrderStatus(List<String> orderIdList) throws Exception
    {
        if (orderIdList.size() > 0)
        {
            orderDao.batchUpdateOrderStatus(orderIdList, OrderStatusEnum.SETTLE.getValue());
            // 释放商铺资源
            orderShopRsrcService.releaseShopResource(orderIdList);

        }

    }

    public List<String> getOrderIdsByCondition() throws Exception
    {
        return orderDao.getOrderIdsByCondition();
    }

    @Override public void updateOrderStatus(String orderId, int status) throws Exception
    {
        orderDao.updateOrderStatus(orderId, status);
    }

    @Override public void saveOrderLog(String orderId, String remark, Long userId) throws Exception
    {
        OrderDto order = orderDao.getOrderById(orderId);
        if (order == null)
        {
            logger.info("指定订单不存在，订单ID：" + orderId + ";不写订单日志");
            return;
        }
        OrderLogDto orderLogDto = new OrderLogDto();
        orderLogDto.setOrderId(order.getOrderId());
        orderLogDto.setPayStatus(order.getPayStatus());
        orderLogDto.setOrderStatus(order.getOrderStatus());
        orderLogDto.setRemark(remark);
        orderLogDto.setUserId(userId);
        orderLogDto.setLastUpdateTime(new Date());
        orderLogDao.saveOrderLog(orderLogDto);

    }

    public void dealSettleByPrice(OrderDto order, Map orderGoodsSettleSetting) throws Exception
    {
        String orderId = order.getOrderId();
        // 如果结算周期设置为0，则立马触发结算
        Long userSettleDelayDays = (Long) orderGoodsSettleSetting.get("userSettleDelayDays");// 用户结算周期
        Long shopSettleDelayDays = (Long) orderGoodsSettleSetting.get("shopSettleDelayDays");// 商铺结算周期
        if (null != shopSettleDelayDays && shopSettleDelayDays.intValue() == 0 && order.getShopSettleFlag() != 1)
        {
            Date nowDate = new Date();
            logger.info("-----------店铺结算开始---------");
            List<ShopBillDto> shopBills = shopBillDao.getShopBillMiddleByOrderId(orderId); // 临时表中的账单

            ShopDto shop = shopDao.getShopById(order.getShopId());
            //商铺账户
            ShopAccountDto account = shopAccountDao.getShopAccount(order.getShopId());
            //返利比例
            Double platformTotalIncomeRatio = shop.getPercentage();
            //平台获得奖励，保证金不足从线上收入
            Double platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getOrderRealSettlePrice());
            //保证金余额
            Double balance = account.getDepositAmount();

            if (null != shop.getShopIdentity() && (shop.getShopIdentity() == 1 || shop.getShopIdentity() == 2))
            {
                platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getSettlePrice());
            }
            if (CollectionUtils.isNotEmpty(shopBills))
            {
                List<Long> shopBillIds = new ArrayList<Long>();
                for (ShopBillDto shopBill : shopBills)
                {
                    Long shopId = shopBill.getShopId();
                    if (shop != null)
                    {
                        Double onlineIncome = 0D; // 线上营业收入
                        Double rewardIncome = 0D; // 平台奖励收入
                        Double supportDeposit = 0D; // 保证金支持
                        Double marketIncome = 0D; // 生意金支持
                        ShopAccountDto shopAccount = shopAccountDao.getShopAccount(shopId);
                        if (shopAccount != null)
                        {
                            Double money = shopBill.getMoney();
                            if (money != 0)
                            {
                                if (CommonConst.SHOP_ACCOUNT_TYPE_INCOME == shopBill.getAccountType())
                                {
                                    // 营业收入
                                    onlineIncome = money;
                                    shopBill.setAccountAfterAmount(NumberUtil
                                            .add(onlineIncome, shopAccount.getOnlineIncomeAmount()));// 结算后该账户余额
                                }
                                else if (CommonConst.SHOP_ACCOUNT_TYPE_REWARD == shopBill.getAccountType())
                                {
                                    // 推荐奖励类型
                                    rewardIncome = money;
                                    shopBill.setAccountAfterAmount(
                                            NumberUtil.add(rewardIncome, shopAccount.getRewardAmount())); // 结算后该账户余额
                                }
                                else if (CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT == shopBill.getAccountType())
                                {
                                    onlineIncome = shopBillDao
                                            .getShopBillSumMoney(orderId, CommonConst.SHOP_ACCOUNT_TYPE_INCOME);
                                    //保证金小于服务费
                                    if (onlineIncome > 0)
                                    {
                                        logger.info("线上收入转入保证金-start");
                                        Double onlineIncomeAfterAmount = 0d;
                                        Double supportDepositAfterAmount = 0d;
                                        //线上收入大于服务费
                                        if (onlineIncome >= platformTotalIncome)
                                        {
                                            //从线上收入扣钱,更新账户差值
                                            onlineIncome = -platformTotalIncome;
                                            //处理后线上收入余额
                                            onlineIncomeAfterAmount = NumberUtil
                                                    .sub(shopAccount.getOnlineIncomeAmount(), platformTotalIncome);
                                            //处理后保证金余额
                                            supportDepositAfterAmount = NumberUtil
                                                    .add(shopAccount.getDepositAmount(), platformTotalIncome);
                                            supportDeposit = 0D;
                                        }
                                        else
                                        {//线上收入小于服务费
                                            //保证金需要扣出的
                                            supportDeposit = -NumberUtil.sub(platformTotalIncome, onlineIncome);
                                            onlineIncome = -onlineIncome;
                                            onlineIncomeAfterAmount = shopAccount.getOnlineIncomeAmount();
                                            supportDepositAfterAmount = NumberUtil
                                                    .add(shopAccount.getDepositAmount(), onlineIncome);
                                            platformTotalIncome = onlineIncome;

                                        }

                                        //增加一条线上收入扣减记录
                                        payServcie.insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER,
                                                CommonConst.BILL_DIRECTION_DOWN,
                                                "线上收入转入保证金" + -platformTotalIncome + "元",
                                                CommonConst.SHOP_BILL_STATUS_OVER, CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
                                                account.getAmount(), onlineIncomeAfterAmount, -platformTotalIncome,
                                                "转充", null, orderId);
                                        //增加一条保证金增加记录
                                        payServcie.insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER,
                                                CommonConst.BILL_DIRECTION_ADD, "线上收入转入保证金" + platformTotalIncome + "元",
                                                CommonConst.SHOP_BILL_STATUS_OVER,
                                                CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT, account.getAmount(),
                                                supportDepositAfterAmount, platformTotalIncome, "转充", null, orderId);

                                        shopBill.setAccountAfterAmount(NumberUtil
                                                .add(supportDeposit, shopAccount.getDepositAmount())); // 结算后该账户余额
                                        logger.info("线上收入转入保证金-end");
                                    }
                                    else
                                    {
                                        // 保证金类型
                                        supportDeposit = money; // 为负值
                                        shopBill.setAccountAfterAmount(NumberUtil
                                                .add(supportDeposit, shopAccount.getDepositAmount())); // 结算后该账户余额
                                    }

                                }
                                else if (CommonConst.SHOP_ACCOUNT_TYPE_MARKET == shopBill.getAccountType())
                                {
                                    // 生意金类型
                                    marketIncome = money;
                                    shopBill.setAccountAfterAmount(
                                            NumberUtil.add(marketIncome, shopAccount.getMarketAmount())); // 结算后该账户余额
                                }
                                // 更新账户余额
                                shopAccountDao
                                        .updateShopAccount(shopId, money, onlineIncome, rewardIncome, rewardIncome,
                                                supportDeposit, null, null, marketIncome, marketIncome, null);

                                shopBill.setAccountAmount(shopAccount.getAmount());// 结算前总账户余额
                                shopBill.setSettleTime(nowDate);
                                shopBill.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
                                // 保存账单到正式表
                                shopBillDao.insertShopBill(shopBill);
                            }
                        }
                    }
                    else
                    {
                        shopBillIds.add(shopBill.getBillId());
                        logger.info("getShopById查询商铺为null，店铺ID为：" + shopId);
                    }
                }
                if (shopBillIds.size() == 0)
                {
                    shopBillIds = null;
                }
                // 删除临时表中的记录
                shopBillDao.deleteShopBillMiddle(orderId, shopBillIds);

            }
            OrderDto newOrder = new OrderDto();
            newOrder.setOrderId(orderId);
            newOrder.setShopSettleFlag(CommonConst.SHOP_SETTLE_FLAG_TRUE); // 已结算
            newOrder.setShopSettleTime(nowDate);
            orderDao.updateOrder(newOrder);
            List<PlatformBillDto> platformBillDtos = platformBillDao.getPlatformBillMiddleByOrderId(orderId);
            if (CollectionUtils.isNotEmpty(platformBillDtos))
            {
                for (PlatformBillDto platformBillDto : platformBillDtos)
                {
                    platformBillDto.setSettleTime(nowDate);
                    platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
                    platformBillDao.insertPlatformBill(platformBillDto);
                }

                platformBillDao.deletePlatformBillMiddle(orderId);
            }
        }
        if (null != userSettleDelayDays && userSettleDelayDays.intValue() == 0 && order.getSettleFlag() == 0)
        {
            logger.info("-----------用户结算开始---------");
            // 触发用户分账,查询临时表中账单
            List<UserBillDto> userBills = userBillDao.getUserBillMiddleByOrderId(orderId);
            if (CollectionUtils.isNotEmpty(userBills))
            {
                List<Long> userBillIds = new ArrayList<Long>();
                for (UserBillDto userBill : userBills)
                {
                    Double money = userBill.getMoney();
                    if (money != 0)
                    {
                        Long referUserId = userBill.getUserId();// 推荐人ID
                        UserDto user = userDao.getUserById(referUserId);
                        if (user != null)
                        {
                            UserAccountDto userAccount = userAccountDao.getAccountMoney(referUserId); // 推荐人账户
                            if (null != userAccount)
                            {
                                userAccountDao
                                        .updateUserAccount(referUserId, money, money, money, null, null, null, null,
                                                null, null, null, null, null, null, null);
                                userBill.setAccountAfterAmount(NumberUtil.add(money, userAccount.getRewardAmount()));
                                userBill.setAccountAmount(userAccount.getAmount());// 结算前总账户余额
                                userBill.setSettleTime(new Date());
                                userBill.setBillStatus(RewardsEnum.HAVE_SETTLEMENT.getValue());
                                userBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
                                userBill.setIsShow(CommonConst.USER_BILL_IS_SHOW);
                                // 保存账单到正式表
                                userBillDao.insertUserBill(userBill);
                            }
                        }
                        else
                        {
                            userBillIds.add(userBill.getBillId());
                            logger.info("getAccountMoney查询用户推荐人为空，用户ID:" + referUserId);
                        }
                    }
                }
                if (userBillIds.size() == 0)
                {
                    userBillIds = null; // 如果没有置为空，为底层sql
                }
                // 删除临时表中的记录
                userBillDao.deleteUserBillMiddle(orderId, userBillIds);

            }
            // 更新订单结算状态
            OrderDto newOrder = new OrderDto();
            newOrder.setOrderId(orderId);
            newOrder.setSettleFlag(CommonConst.USER_SETTLE_FLAG_TRUE); // 已结算
            newOrder.setSettleTime(new Date());
            orderDao.updateOrder(newOrder);
        }

    }

    /**
     * 处理非会员订单结算
     *
     * @Title: dealXorderSettle @param @param order @param @param
     * orderGoodsSettleSetting @return void 返回类型 @throws
     */
    public void dealXorderSettle(OrderDto order, Map orderGoodsSettleSetting) throws Exception
    {
        Date nowDate = new Date();
        logger.info("-----------店铺结算开始---------");
        List<ShopBillDto> shopBills = shopBillDao.getShopBillMiddleByOrderId(order.getOrderId()); // 临时表中的账单
        if (CollectionUtils.isNotEmpty(shopBills))
        {
            List<Long> shopBillIds = new ArrayList<Long>();
            for (ShopBillDto shopBill : shopBills)
            {
                Long shopId = shopBill.getShopId();
                ShopDto shop = shopDao.getShopById(shopId);
                if (shop != null)
                {
                    Double onlineIncome = 0D; // 线上营业收入
                    Double rewardIncome = 0D; // 平台奖励收入
                    Double supportDeposit = 0D; // 保证金支持
                    ShopAccountDto shopAccount = shopAccountDao.getShopAccount(shopId);
                    if (shopAccount != null)
                    {
                        Double money = shopBill.getMoney();
                        if (CommonConst.SHOP_ACCOUNT_TYPE_INCOME == shopBill.getAccountType())
                        {
                            // 营业收入
                            onlineIncome = money;
                            shopBill.setAccountAfterAmount(
                                    NumberUtil.add(onlineIncome, shopAccount.getOnlineIncomeAmount()));// 结算后该账户余额
                        }
                        else if (CommonConst.SHOP_ACCOUNT_TYPE_REWARD == shopBill.getAccountType())
                        {
                            // 推荐奖励类型
                            rewardIncome = money;
                            shopBill.setAccountAfterAmount(
                                    NumberUtil.add(rewardIncome, shopAccount.getRewardAmount())); // 结算后该账户余额
                        }
                        else if (CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT == shopBill.getAccountType())
                        {
                            // 保证金类型
                            supportDeposit = money; // 为负值
                            shopBill.setAccountAfterAmount(
                                    NumberUtil.add(supportDeposit, shopAccount.getDepositAmount())); // 结算后该账户余额
                        }
                        // 更新账户余额
                        shopAccountDao.updateShopAccount(shopId, money, onlineIncome, rewardIncome, rewardIncome,
                                supportDeposit, null, null, null, null, null);

                        shopBill.setAccountAmount(shopAccount.getAmount());// 结算前总账户余额
                        shopBill.setSettleTime(nowDate);
                        shopBill.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
                        // 保存账单到正式表
                        shopBillDao.insertShopBill(shopBill);
                    }
                }
                else
                {
                    shopBillIds.add(shopBill.getBillId());
                    logger.info("getShopById查询商铺为null，店铺ID为：" + shopId);
                }
            }
            if (shopBillIds.size() == 0)
            {
                shopBillIds = null;
            }
            // 删除临时表中的记录
            shopBillDao.deleteShopBillMiddle(order.getOrderId(), shopBillIds);

        }
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setSettleFlag(CommonConst.USER_SETTLE_FLAG_TRUE);
        orderDto.setShopSettleFlag(CommonConst.SHOP_SETTLE_FLAG_TRUE);
        orderDto.setShopSettleTime(new Date());
        orderDto.setSettleTime(new Date());
        orderDao.updateOrder(orderDto);
        List<PlatformBillDto> platformBillDtos = platformBillDao.getPlatformBillMiddleByOrderId(order.getOrderId());
        if (CollectionUtils.isNotEmpty(platformBillDtos))
        {
            for (PlatformBillDto platformBillDto : platformBillDtos)
            {
                platformBillDto.setSettleTime(nowDate);
                platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
                platformBillDao.insertPlatformBill(platformBillDto);
            }

            platformBillDao.deletePlatformBillMiddle(order.getOrderId());
        }
    }

    @Override public void aliscanPreOrder(OrderDto order) throws Exception
    {

    }

    /**
     * 简单的订单更新
     *
     * @Title: updateOrderOnly @param @param orderDto @param @throws
     * Exception @throws
     */
    public void updateOrderOnly(OrderDto orderDto) throws Exception
    {
        orderDao.updateOrder(orderDto);
    }

    /**
     * 一点管家订单详情
     *
     * @Title: getIdgjOrderDetail @param @param
     * orderId @param @return @param @throws Exception @throws
     */
    public OrderDto getIdgjOrderDetail(String orderId) throws Exception
    {
        OrderDto orderDto = orderDao.queryIdgjOrderDetail(orderId);
        UserDto userDto = userDao.getUserById(orderDto.getUserId());
        if (userDto != null)
        {// 判断会员是否存在
            orderDto.setMobile(userDto.getMobile());// 用户会员账号
        }
        if (StringUtils.isBlank(orderDto.getCashierUsername()))
        {
            orderDto.setCashierUsername("老板");
            if (orderDto.getCashierId() != null)
            {
                // 如果是0,就取老板的名称
                // 雇员取雇员表的用户名
                Map map = shopCashierDao.getShopCashierById(orderDto.getCashierId());
                if (map != null)
                {
                    orderDto.setCashierUsername(map.get("loginName") + "");
                }
            }
        }
        List<PayDto> payList = payDao.getOrderPayList(orderId, CommonConst.PAY_STATUS_PAY_SUCCESS);
        if (payList != null && payList.size() > 0)
        {
            orderDto.setPayType(payList.get(0).getPayType());// 设置订单支付类型
        }
        return orderDto;
    }

    public OrderDto queryOrderDtoById(String orderId) throws Exception
    {
        return this.orderDao.getOrderById(orderId);
    }

    public Integer getMaxPayIndex(String orderId, int flag) throws Exception
    {
        if (flag == 0)
        {
            // 非会员订单
            return xorderPayDao.getMaxPayIndex(orderId);
        }
        else
        {
            // 会员订单
            return payDao.getMaxPayIndex(orderId);
        }
    }

    public XorderGoodsDto getXorderGoodsByGoodsId(XorderGoodsDto dto) throws Exception
    {
        return xorderGoodsDao.getXorderGoodsDto(dto);
    }

    public int insertOrUpdateOrderDto(OrderDto orderDto, boolean orderFlag, boolean xorderFlag) throws Exception
    {

        if (orderFlag)
        {
            // 新增
            orderDao.saveOrder(orderDto);
        }
        else
        {

            // 修改
            orderDao.updateOrder(orderDto);

            if (CommonConst.ORDER_STS_YJZ == orderDto.getOrderStatus())
            {
                // 修改库存
                storageService.insertShopStorageByOrderId(orderDto.getOrderId(), orderDto.getShopId());
            }

        }
        if (CollectionUtils.isNotEmpty(orderDto.getGoods()))
        {
            // 先删除订单商品
            orderGoodsDao.delGoodsByOrderId(orderDto.getOrderId());
            // 再插入所有的订单商品
            orderGoodsDao.saveOrderGoodsBatch(orderDto.getGoods());
        }
        // 如果存在非会员订单记录，则需要将非会员支付记录移至会员支付记录中来
        /*
         * if(!xorderFlag){ List<XorderPayDto> xorderpayDtos =
         * xorderPayDao.getAllPayLog(orderDto.getOrderId()); if(xorderpayDtos !=
         * null && xorderpayDtos.size() > 0){ List<PayDto> orderPays = new
         * ArrayList<PayDto>(); for(XorderPayDto xorderPayDto:xorderpayDtos){
         * PayDto payDto = new PayDto();
         * payDto.setOrderId(orderDto.getOrderId());
         * payDto.setPayType(xorderPayDto.getPayType());
         * payDto.setPayAmount(xorderPayDto.getPayAmount());
         * payDto.setShopId(xorderPayDto.getShopId());
         * payDto.setOrderPayType(xorderPayDto.getOrderPayType());
         * payDto.setLastUpdateTime
         * (DateUtils.format(xorderPayDto.getLastUpdateTime(),
         * DateUtils.DATETIME_FORMAT));
         * payDto.setPayeeType(xorderPayDto.getPayeeType());
         * payDto.setOrderPayTime
         * (DateUtils.format(xorderPayDto.getOrderPayTime(),
         * DateUtils.DATETIME_FORMAT));
         * payDto.setOrderPayType(xorderPayDto.getOrderPayType());
         * payDto.setPayId((xorderPayDto.getPayId() ==
         * null?null:Long.parseLong(xorderPayDto.getPayId()+"")));
         * payDto.setPayIndex(xorderPayDto.getPayIndex());
         * payDto.setUserPayTime(DateUtils.format(xorderPayDto.getUserPayTime(),
         * DateUtils.DATETIME_FORMAT)); payDao.addOrderPay(payDto); } }
         * //清除非会员订单记录 clearXorderDtoRecord(orderDto.getOrderId()); }
         */
        return 1;
    }

    @Override public void updateShopCoupon(List<Integer> userHoldShopcouponIds, String orderId, Long shopId,
            String mobile) throws Exception
    {
        Date now = new Date();
        if (CollectionUtils.isNotEmpty(userHoldShopcouponIds))
        {
            /*
    		 * 1、更新优惠券数量
    		 * 2、更新用户持有店铺优惠券状态
    		 * 
    		 */
            for (Integer userHoldShopcouponId : userHoldShopcouponIds)
            {

                UserShopCouponDto userHoldCoupon = validUserShopCoupon(userHoldShopcouponId.longValue(), shopId, mobile,
                        now);

                //更新用户持有优惠券状态
                UserShopCouponDto userShopCouponDto = new UserShopCouponDto();
                userShopCouponDto.setUserShopCouponId(userHoldShopcouponId);
                userShopCouponDto.setCouponStatus(UserShopCouponStatusEnum.IS_USE.getValue());
                userShopCouponDto.setUsedCouponTime(new Date());
                userShopCouponDto.setOrderId(orderId);
                userShopCouponDao.updateUserShopCouponByCouponId(userShopCouponDto);
                //查询数量
                UserShopCouponDto parms = new UserShopCouponDto();
                parms.setShopCouponId(userHoldCoupon.getShopCouponId());
                parms.setCouponStatus(UserShopCouponStatusEnum.IS_USE.getValue());
                Integer count = userShopCouponDao.getUserShopCouponCount(parms);

                //更新优惠券数量
                ShopCouponDto shopCouponDto = new ShopCouponDto();
                shopCouponDto.setShopCouponId(userHoldCoupon.getShopCouponId());
                shopCouponDto.setUsedTotalNum(count);
                shopCouponDao.updateShopCoupon(shopCouponDto);
            }
        }
    }

    private UserShopCouponDto validUserShopCoupon(Long userHoldShopcouponId, Long shopId, String mobile, Date now)
            throws Exception
    {
        UserShopCouponDto search = new UserShopCouponDto();
        search.setUserShopCouponId(userHoldShopcouponId.intValue());
        search.setCouponStatus(UserShopCouponStatusEnum.NOT_IS_USE.getValue());
        search.setShopId(shopId);
        search.setMobile(mobile);
        List<UserShopCouponDto> userShopCoupon = userShopCouponDao.getUserShopCouponList(search);

        if (userShopCoupon == null || userShopCoupon.size() == 0)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, " 用户持有优惠券不可用：userShopCouponId:" +
                    userHoldShopcouponId + " shopId:" + shopId + " mobile" + mobile);
        }

        UserShopCouponDto userHoldCoupon = userShopCoupon.get(0);

        if (!(userHoldCoupon.getBeginDate().before(now) && userHoldCoupon.getEndDate().after(now)))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, " 用户持有优惠券不在有效期：userShopCouponId:" +
                    userHoldShopcouponId + " shopId:" + shopId + " mobile" + mobile);
        }

        return userHoldCoupon;
    }

    public int insertOrUpdateXOrderDto(OrderDto orderDto, boolean xorderFlag, List<OrderGoodsDto> orderGoodsDtos)
            throws Exception
    {
        if (xorderFlag)
        {
            // 新增
            orderDao.saveOrder(orderDto);
        }
        else
        {
            // 修改
            orderDao.updateOrder(orderDto);
        }
        if (CollectionUtils.isNotEmpty(orderGoodsDtos))
        {

            // 先删除订单商品
            orderGoodsDao.delGoodsByOrderId(orderDto.getOrderId());
            // 再插入所有的订单商品
            for (OrderGoodsDto orderGoods : orderGoodsDtos)
            {
                orderGoodsDao.saveOrderGoods(orderGoods);
            }
        }
        return 1;
    }

    public OrderGoodsDto getOrderGoodsByGoodsId(OrderGoodsDto dto) throws Exception
    {
        return orderGoodsDao.queryOrderGoodsDto(dto);
    }

    public int clearXorderDtoRecord(String orderId) throws Exception
    {
        // 清除订单商品记录
        orderGoodsDao.delGoodsByOrderId(orderId);
        // 清除订单支付记录
        payDao.deletePayByOrderId(orderId);
        // 清除订单记录
        orderDao.delOrder(orderId);
        return 1;
    }

    public Map<String, Object> getAllOrderList(List<Long> shopList, Integer dateType, String startDate, String endDate,
            List<Integer> orderStatuss, Integer orderTransactionType, Integer payType, Integer billerId,
            Integer cashierId, int pNo, int pSize, Long userId, String yearOnYearStr, String ringStr) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stNo", (pNo - 1) * pSize);
        param.put("pSize", pSize);
        param.put("shopId", shopList);
        param.put("dateType", dateType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("orderStatuss", orderStatuss);
        param.put("orderTransactionType", orderTransactionType);
        param.put("payType", payType);
        param.put("billerId", billerId);
        param.put("cashierId", cashierId);
        param.put("userId", userId);
        // 查询实收金额合计、平台服务费合计
        Double orderTotalPriceTotal = 0d;
        Double platformTotalIncomePriceTotal = 0d;
        Map amountState = orderDao.getAllOrderListAmountState(param);
        if (null != amountState && amountState.size() > 0)
        {
            orderTotalPriceTotal = Double.parseDouble(amountState.get("orderTotalPriceTotal") + "");
            platformTotalIncomePriceTotal = Double.parseDouble(amountState.get("platformTotalIncomePriceTotal") + "");
        }

        Double cashPayTotal = 0d;
        Double posPayTotal = 0d;
        Double onLinePayTotal = 0d;
        Double memberCardPayTotal = 0d;


        Double freePayTotal = 0d;
        Double customPay1Total = 0d;
        Double customPay2Total = 0d;
        Double customPay3Total = 0d;



        Map payAmountState = orderDao.getPayAmountStateByOrderIds(param);
        if (null != payAmountState && payAmountState.size() > 0)
        {
            cashPayTotal = Double.parseDouble(payAmountState.get("cashPayTotal") + "");
            posPayTotal = Double.parseDouble(payAmountState.get("posPayTotal") + "");
            onLinePayTotal = Double.parseDouble(payAmountState.get("onLinePayTotal") + "");
            memberCardPayTotal = Double.parseDouble(payAmountState.get("memberCardPayTotal") + "");

            freePayTotal = Double.parseDouble(payAmountState.get("freePayTotal") + "");
            customPay1Total = Double.parseDouble(payAmountState.get("customPay1Total") + "");
            customPay2Total = Double.parseDouble(payAmountState.get("customPay2Total") + "");
            customPay3Total = Double.parseDouble(payAmountState.get("customPay3Total") + "");

        }
        // 查询明细列表
        Integer rCount = orderDao.getAllOrderListCount(param);
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        if (rCount != 0)
        {
            orderList = orderDao.getAllOrderListDetail(param);
            if (null != orderList && orderList.size() > 0)
            {
                List<String> orderIds = new ArrayList<String>();
                // List<String> xorderIds = new ArrayList<String>();
                for (Map<String, Object> bean : orderList)
                {
                    Integer orderFlag = Integer.parseInt(bean.get("orderFlag") + "");
                    String orderId = (String) bean.get("orderId");

                    String orderTile = bean.get("orderTitle") == null ? "" : bean.get("orderTitle").toString();
                    String remark = bean.get("remark") == null || bean.get("remark").equals("") ? "" :
                            "-" + bean.get("remark").toString();
                    bean.put("orderTitle", orderTile + remark);

                    // 1代表会员订单
                    if (orderFlag == 1)
                    {
                        orderIds.add(orderId);
                    } /*
                       * else if(orderFlag == 2){ xorderIds.add(orderId); }
                       */
                    bean.remove("orderFlag");
                    bean.put("cashPay", 0);
                    bean.put("posPay", 0);
                    bean.put("onLinePay", 0);
                    bean.put("memberCardPay", 0);

                    bean.put("freePay", 0);
                    bean.put("customPay1", 0);
                    bean.put("customPay2", 0);
                    bean.put("customPay3", 0);
                }
                if (orderIds.size() > 0)
                {
                    List<Map> orderPays = payDao.getAllOrderPayDetail(orderIds);
                    if (null != orderPays && orderPays.size() > 0)
                    {
                        for (Map<String, Object> bean : orderList)
                        {
                            String orderId = (String) bean.get("orderId");
                            for (Map payBean : orderPays)
                            {
                                String dbOrderId = (String) payBean.get("orderId");
                                if (orderId.equals(dbOrderId))
                                {
                                    Double cashPay = Double.parseDouble(payBean.get("cashPay") + "");
                                    Double posPay = Double.parseDouble(payBean.get("posPay") + "");
                                    Double onLinePay = Double.parseDouble(payBean.get("onlinePay") + "");
                                    Double memberCardPay = Double.parseDouble(payBean.get("memberCardPay") + "");

                                    Double freePay = Double.parseDouble(payBean.get("freePay") + "");
                                    Double customPay1 = Double.parseDouble(payBean.get("customPay1") + "");
                                    Double customPay2 = Double.parseDouble(payBean.get("customPay2") + "");
                                    Double customPay3 = Double.parseDouble(payBean.get("customPay3") + "");


                                    bean.put("cashPay", cashPay);
                                    bean.put("posPay", posPay);
                                    bean.put("onLinePay", onLinePay);
                                    bean.put("memberCardPay", memberCardPay);


                                    bean.put("freePay", freePay);
                                    bean.put("customPay1", customPay1);
                                    bean.put("customPay2", customPay2);
                                    bean.put("customPay3", customPay3);
                                    break;
                                }
                            }
                        }
                    }
                }
                /*
                 * if (xorderIds.size() > 0) { List<Map> xorderPays =
                 * xorderPayDao.getXorderPayDetail(xorderIds); if (null !=
                 * xorderPays && xorderPays.size() > 0) { for(Map<String,Object>
                 * bean : orderList){ String orderId = (String)
                 * bean.get("orderId"); for(Map payBean : xorderPays){ String
                 * dbOrderId = (String) payBean.get("orderId");
                 * if(orderId.equals(dbOrderId)){ Double cashPay =
                 * Double.parseDouble(payBean.get("cashPay")+""); Double posPay
                 * = Double.parseDouble(payBean.get("posPay")+""); Double
                 * onLinePay = Double.parseDouble(payBean.get("onlinePay")+"");
                 * bean.put("cashPay", NumberUtil.formatDouble(cashPay, 4));
                 * bean.put("posPay", NumberUtil.formatDouble(posPay, 4));
                 * bean.put("onLinePay", NumberUtil.formatDouble(onLinePay, 4));
                 * break; } } } } }
                 */

            }
        }
        Map preAmountState = null;
        Double preYearOrderTotalPriceTotal = 0d;
        if (yearOnYearStr != null && (yearOnYearStr.equals("1") || yearOnYearStr.equals("2")))
        {//日/月同比
            param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 3));
            param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 3));
            preAmountState = orderDao.getAllOrderListAmountState(param);
            preYearOrderTotalPriceTotal = Double.parseDouble(preAmountState.get("orderTotalPriceTotal") + "");
        }

        Map ringAmountState = null;
        Double ringOrderTotalPriceTotal = 0d;
        if (ringStr != null)
        {
            if (ringStr.equals("1"))
            {//日环比
                param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 1));
                param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 1));
                ringAmountState = orderDao.getAllOrderListAmountState(param);
                ringOrderTotalPriceTotal = Double.parseDouble(ringAmountState.get("orderTotalPriceTotal") + "");
            }
            else if (ringStr.equals("2"))
            {//月环比
                param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 2));
                param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 2));
                ringAmountState = orderDao.getAllOrderListAmountState(param);
                ringOrderTotalPriceTotal = Double.parseDouble(ringAmountState.get("orderTotalPriceTotal") + "");
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("pNo", pNo);
        resultMap.put("rCount", rCount);
        resultMap.put("orderTotalPriceTotal", orderTotalPriceTotal);
        resultMap.put("platformTotalIncomePriceTotal", NumberUtil.formatDouble(platformTotalIncomePriceTotal, 4));
        resultMap.put("cashPayTotal", NumberUtil.formatDouble(cashPayTotal, 4));
        resultMap.put("posPayTotal", NumberUtil.formatDouble(posPayTotal, 4));
        resultMap.put("onLinePayTotal", NumberUtil.formatDouble(onLinePayTotal, 4));
        resultMap.put("memberCardPayTotal", NumberUtil.formatDouble(memberCardPayTotal, 4));

        resultMap.put("freePayTotal", NumberUtil.formatDouble(freePayTotal, 4));
        resultMap.put("customPay1Total", NumberUtil.formatDouble(customPay1Total, 4));
        resultMap.put("customPay2Total", NumberUtil.formatDouble(customPay2Total, 4));
        resultMap.put("customPay3Total", NumberUtil.formatDouble(customPay3Total, 4));

        resultMap.put("preYearOrderTotalPriceTotal", preYearOrderTotalPriceTotal);
        resultMap.put("ringOrderTotalPriceTotal", ringOrderTotalPriceTotal);
        resultMap.put("lst", orderList);
        return resultMap;
    }

    /**
     * @param dateString 日期字符串
     * @param patten     日期格式
     * @param flag       转换为上一日期：1：上一日；2：上一月；3：上一年
     * @return
     */
    public String getDateString(String dateString, String patten, int flag)
    {
        if (dateString == null || patten == null || !(flag == 1 || flag == 2 || flag == 3))
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Date dateParse = null;
        Calendar calendar = Calendar.getInstance();
        try
        {
            dateParse = sdf.parse(dateString);
            calendar.setTime(dateParse);
            if (flag == 1)
            {
                System.out.println("calendar.DAY_OF_YEAR:" + calendar.DAY_OF_YEAR);
                calendar.add(calendar.DAY_OF_YEAR, -1);
                //				dateParse.setDate(dateParse.getDate()-1);
            }
            else if (flag == 2)
            {
                System.out.println("calendar.MONTH:" + calendar.MONTH);
                calendar.add(calendar.MONTH, -1);
                //				dateParse.setMonth(dateParse.getMonth()-1);
            }
            else if (flag == 3)
            {
                System.out.println("calendar.YEAR:" + calendar.YEAR);
                calendar.add(calendar.YEAR, -1);
                //				dateParse.setYear(dateParse.getYear()-1);
            }
        }
        catch (ParseException e)
        {
            logger.error("日期数据格式异常");
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 支付完成的处理
     *
     * @Title: payFinishDeal @param @param transactionDto @param @param
     * payDto @param @param orderNo @param @throws Exception @throws
     */
    public void payFinishDeal(Transaction3rdDto transactionDto, PayDto payDto, String orderNo) throws Exception
    {
        OrderDto orderDto = orderDao.getOrderById(orderNo);
        orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);// 订单支付状态已支付
        orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);// 订单状态设置为已结账
        orderDto.setServerLastTime(new Date().getTime());
        updateOrderOnly(orderDto);// 仅仅简单更新订单
        OrderLogDto orderLogDto = new OrderLogDto();
        orderLogDto.setOrderId(orderDto.getOrderId());
        orderLogDto.setPayStatus(orderDto.getPayStatus());
        orderLogDto.setOrderStatus(orderDto.getOrderStatus());
        orderLogDto.setLastUpdateTime(new Date());
        orderLogDto.setRemark("支付宝支付");
        orderLogDto.setUserId(orderDto.getUserId());
        orderLogDao.saveOrderLog(orderLogDto);
        // 更新销量
        updateGoodsAndShopSoldNum(orderNo);
        //插入反结账订单商品线上支付账单
        payServcie.insertReverseShopBill(orderDto);
        if (orderDto != null)
        {
            if (orderDto.getUserId() != null)
            {
                payDto.setUserId(orderDto.getUserId());
            }
            generateTransactionAndPay(transactionDto, payDto);// 生成交易记录
            if (CommonConst.ORDER_IS_MEMBER == orderDto.getIsMember())
            {
                if (orderDto.getUserId() != null)
                {// 会员订单
                    generateScanPayUserBill(orderDto, transactionDto, payDto);
                    // 发红包 start add by ljp 20160322
                    Double sendMoney = packetService.sendRedPacketToUser(orderDto);
                    if (sendMoney != 0)
                    {
                        // 更新订单结算价格
                        Double orderRealSettlePrice = NumberUtil.sub(orderDto.getOrderRealSettlePrice(), sendMoney);
                        if (orderRealSettlePrice < 0)
                        {
                            orderRealSettlePrice = 0D;
                        }
                        orderDto.setOrderRealSettlePrice(orderRealSettlePrice);
                        orderDto.setSendRedPacketMoney(sendMoney);
                        orderDao.updateOrder(orderDto);
                    }
                    // 发红包end

                }
                if (CommonConst.ORDER_STS_YJZ == orderDto.getOrderStatus())
                {// 订单状态为已完成
                    OrderGoodsSettleUtil.detailSingleOrder(orderNo);// 开始分账
                    // 修改库存
                    storageService.insertShopStorageByOrderId(orderDto.getOrderId(), orderDto.getShopId());
                }
            }
            else
            {
                generatePlatformBill(orderDto, CommonConst.PLT_BILL_MNY_SOURCE_ZFB, payDto.getPayAmount());// 平台收入为支付宝
                generateShopMiddleBill(orderDto, payDto.getPayAmount());// 插入店铺账单
                if (CommonConst.ORDER_STS_YJZ == orderDto.getOrderStatus())
                {// 订单状态为已完成
                    OrderGoodsSettleUtil.detailSingleXorder(orderNo);// 开始分账
                }
                handleAccountingStatByUser(orderDto);
            }
        }
    }

    /**
     * 生成一条平台收入记录
     *
     * @Title: generatePlatformBill @param @param xorderDto @param @param
     * moneySource @param @throws Exception @return void 返回类型 @throws
     */
    public void generatePlatformBill(OrderDto orderDto, Integer moneySource, Double billMoney) throws Exception
    {
        String billDesc = "平台收入(支付宝支付)";
        if (CommonConst.PLT_BILL_MNY_SOURCE_WX == moneySource)
        {
            billDesc = "平台收入(微信支付)";
        }
        PlatformBillDto platformBillDto = new PlatformBillDto();// 平台账单
        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);// 增加
        platformBillDto.setBillDesc(billDesc);
        platformBillDto.setBillStatus(CommonConst.BILL_STATUS_FLAG_PROCESS);// 账单状态
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setMoney(billMoney);// 收入金额
        platformBillDto.setOrderId(orderDto.getOrderId());// 订单编号
        platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);// 销售收入
        platformBillDto.setConsumerUserId(0l);// 代表是非会员订单
        platformBillDto.setConsumerMobile("非会员");
        platformBillDto.setMoneySource(moneySource);
        platformBillDto.setBillType("平台收入");
        platformBillDao.insertPlatformBillMiddle(platformBillDto);

        // --------------------------出账--------------------------------
        PlatformBillDto expendPlatformBillDto = new PlatformBillDto();// 平台账单
        expendPlatformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);// 增加
        expendPlatformBillDto.setBillDesc("返还商铺线上营业收入");
        expendPlatformBillDto.setBillStatus(CommonConst.BILL_STATUS_FLAG_PROCESS);// 账单状态
        expendPlatformBillDto.setCreateTime(new Date());
        expendPlatformBillDto.setMoney(-billMoney);// 收入金额
        expendPlatformBillDto.setOrderId(orderDto.getOrderId());// 订单编号
        expendPlatformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_ONLINE);// 销售收入
        expendPlatformBillDto.setConsumerUserId(0l);// 代表是非会员订单
        expendPlatformBillDto.setConsumerMobile("非会员");
        expendPlatformBillDto.setMoneySource(moneySource);
        expendPlatformBillDto.setBillType("支付商铺线上营业收入");
        platformBillDao.insertPlatformBillMiddle(expendPlatformBillDto);
    }

    /**
     * 生成店铺账单
     *
     * @Title: generateShopMiddleBill @param @param orderDto @param @throws
     * Exception @return void 返回类型 @throws
     */
    public void generateShopMiddleBill(OrderDto orderDto, Double billMoney) throws Exception
    {
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setShopId(orderDto.getShopId());
        shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        shopBillDto.setConsumerUserId(0l);// 非会员订单
        shopBillDto.setMoney(billMoney);
        shopBillDto.setOrderId(orderDto.getOrderId());
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setBillDesc("销售收入");
        shopBillDto.setBillType(CommonConst.BILL_TYPE_SALE);
        shopBillDto.setBillTitle(orderDto.getOrderTitle());
        // TODO 变为常量
        shopBillDto.setAccountType(0);// 线上营业收入
        shopBillDto.setPayAmount(billMoney);
        shopBillDto.setAccountAmount(0d);
        shopBillDto.setAccountAfterAmount(0d);
        shopBillDao.insertShopMiddleBill(shopBillDto);
    }

    /**
     * 生成用户的消费账单
     *
     * @Title: generateScanPayUserBill @param @param orderDto @param @throws
     * Exception @return void 返回类型 @throws
     */
    public void generateScanPayUserBill(OrderDto orderDto, Transaction3rdDto transactionDto, PayDto payDto)
            throws Exception
    {
        // 增加充值记录（2015.8.27需求，消费的时候先记录一条充值记录，再增加一条消费记录）
        UserBillDto userChargeBill = payServiceImp.buildUserBill(payDto, orderDto, null, "充值", 1);
        UserAccountDto userAccount = userAccountDao.getAccountMoney(orderDto.getUserId());
        userChargeBill.setAccountAmount(userAccount.getAmount());
        userChargeBill.setMoney(transactionDto.getPayAmount());
        userChargeBill
                .setAccountAfterAmount(NumberUtil.add(transactionDto.getPayAmount(), userAccount.getCouponAmount()));
        userChargeBill.setBillDesc("第三方生成的充值账单记录");
        userChargeBill.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
        userChargeBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        // 需要设置交易号2015.9.30
        userChargeBill.setTransactionId(transactionDto.getTransactionId());
        // 用户账单logo类型
        userChargeBill.setUserBillType(CommonConst.USER_BILL_TYPE_ALIPAY);
        // 消费金
        userChargeBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
        userChargeBill.setIsShow(CommonConst.USER_BILL_NOT_IS_SHOW);
        userChargeBill.setUserRole(0 + "");
        userChargeBill.setSettleTime(new Date());
        userBillDao.insertUserBill(userChargeBill);

        // 增加消费记录
        UserBillDto userBillDto = payServiceImp.buildUserBill(payDto, orderDto, null, "消费", -1);

        // 截取小数4位
        userBillDto.setMoney(-transactionDto.getPayAmount());
        // 消费
        userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
        // 消费金
        userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
        userBillDto.setAccountAfterAmount(userAccount.getCouponAmount());
        userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        userBillDto.setAccountAmount(userAccount.getAmount());
        userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
        userBillDto.setUserRole(0 + "");
        userBillDto.setSettleTime(new Date());
        userBillDao.insertUserBill(userBillDto);
        // 记录平台账单
        PlatformBillDto platformBillDto = payServiceImp
                .buildPlatformBillBy3Rd(CommonConst.USER_BILL_TYPE_ALIPAY, orderDto.getUserId(),
                        transactionDto.getPayAmount(), orderDto, transactionDto.getTransactionId(), userAccount, "消费支付",
                        CommonConst.BILL_DIRECTION_ADD);
        platformBillDao.insertPlatformBill(platformBillDto);
    }

    /**
     * 生成交易记录和第三方支付记录
     *
     * @Title: generateTransactionAndPay @param @param
     * transactionDto @param @param payDto @param @throws
     * Exception @return void 返回类型 @throws
     */
    public void generateTransactionAndPay(Transaction3rdDto transactionDto, PayDto payDto) throws Exception
    {
        transaction3rdDao.addTransaction(transactionDto);
        Long transactionId = transactionDto.getTransactionId();
        payDto.setPayId(transactionId);// 设置payId
        if (payDto.getPayAmount() > 0)
        {
            payDao.addOrderPay(payDto);
        }
    }

    /**
     * 处理微信扫码支付
     *
     * @Title: dealWxscanNotify @param @param wpr @param @throws
     * Exception @throws
     */
    public void dealWxscanNotify(WxPayResult wpr) throws Exception
    {
        String outTradeNo = wpr.getOutTradeNo();
        OrderDto orderDto = null;
        synchronized (this)
        {// 避免各种原因导致多次请求一下过来,此处到底需要同步么？？------------可以采用乐观锁解决
            // 代表此订单是费会员订单
            orderDto = orderDao.getOrderById(outTradeNo);// 会员订单
            orderDto.setServerLastTime(new Date().getTime());
            if (orderDto != null)
            {
                if (CommonConst.ORDER_STS_YJZ == orderDto.getOrderStatus() || CommonConst.ORDER_STS_YTD == orderDto
                        .getOrderStatus())
                {
                    logger.error("订单状态不对，回调停止处理,订单状态为" + orderDto.getOrderStatus());
                    return;
                }
            }
            JSONObject pushContent = new JSONObject();// 推送内容
            Double currentPayAmount = NumberUtil
                    .fmtDouble(Double.parseDouble(wpr.getTotalFee()) / 100, 2);// 本次支付总金额.微信单位是分
            pushContent.put("action", "orderPayFinish");
            if (orderDto != null)
            {// 代表此单是会员订单
                double paidDoubleAmount = payDao.getSumPayAmount(orderDto.getOrderId(), null);
                paidDoubleAmount = NumberUtil.fmtDouble(paidDoubleAmount + currentPayAmount, 2);
                if (paidDoubleAmount >= orderDto.getSettlePrice())
                {
                    orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);// 已结账
                    orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
                    orderDto.setServerLastTime(new Date().getTime());
                    orderDao.updateOrder(orderDto);
                    String orderId = orderDto.getOrderId();
                    // 修改库存
                    storageService.insertShopStorageByOrderId(orderId, orderDto.getShopId());
                    // 更新销量
                    updateGoodsAndShopSoldNum(orderId);
                    //插入反结账订单商品线上支付账单
                    payServcie.insertReverseShopBill(orderDto);
                    // 日志
                    OrderLogDto orderLogDto = new OrderLogDto();
                    orderLogDto.setOrderId(orderDto.getOrderId());
                    orderLogDto.setPayStatus(orderDto.getPayStatus());
                    orderLogDto.setOrderStatus(orderDto.getOrderStatus());
                    orderLogDto.setLastUpdateTime(new Date());
                    orderLogDto.setUserId(orderDto.getUserId());
                    orderLogDto.setRemark("微信支付");
                    orderLogDao.saveOrderLog(orderLogDto);
                    pushContent.put("status", CommonConst.ORDER_STS_YJZ);
                }
                Integer currentIndex = payDao.getMaxPayIndex(orderDto.getOrderId());// 最大的支付
                if (currentIndex == null)
                {
                    currentIndex = 0;
                }
                currentIndex++;
                pushContent.put("orderId", orderDto.getOrderId());
                if (CommonConst.ORDER_CHANNEL_POS_CQB == orderDto.getOrderChannelType())
                {// 订单下单渠道
                    pushContent.put("collectOrder", true);
                    initPushContent(currentPayAmount, orderDto.getOrderId(), CommonConst.PAY_TYPE_WXSCAN, currentIndex,
                            pushContent);
                }
                Long shopId = orderDto.getShopId();
                if (shopId != null)
                {// 推送给用户
                    push(shopId, pushContent);
                }
                // ----------------------------------推送的内容
                PayDto payDto = new PayDto();
                payDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                payDto.setOrderId(orderDto.getOrderId());
                payDto.setPayAmount(currentPayAmount);// 本次支付金额
                payDto.setPayeeType(CommonConst.PAYEE_TYPE_PLATFORM);
                payDto.setPayType(CommonConst.PAY_TYPE_WXSCAN);
                payDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                payDto.setShopId(orderDto.getShopId());
                payDto.setUserId(orderDto.getUserId());
                Transaction3rdDto transactionDto = new Transaction3rdDto();
                transactionDto.setOrderId(orderDto.getOrderId());
                transactionDto.setOrderPayType(0);// 单个订单支付
                transactionDto.setPayAmount(currentPayAmount);// 支付金额
                transactionDto.setStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);// 支付成功
                transactionDto.setRdOrgName("微信");
                transactionDto.setUserId(orderDto.getUserId());
                transactionDto.setRdTransactionId(wpr.getTransactionId());// 微信交易流水号
                transactionDto.setTransactionType(CommonConst.TRANSACTION_TYPE_WXSCAN);
                transactionDto.setRdNotifyTime(new Date());
                if (orderDto != null)
                {
                    generateTransactionAndPay(transactionDto, payDto);// 生成交易记录
                    if (CommonConst.ORDER_IS_MEMBER == orderDto.getIsMember())
                    {
                        if (orderDto.getUserId() != null)
                        {// 会员订单
                            generateScanPayUserBill(orderDto, transactionDto, payDto);
                        }
                        // 发红包start add by ljp 20160322
                        Double sendMoney = packetService.sendRedPacketToUser(orderDto);
                        if (sendMoney != 0)
                        {
                            // 更新订单结算价格
                            Double orderRealSettlePrice = NumberUtil.sub(orderDto.getOrderRealSettlePrice(), sendMoney);
                            if (orderRealSettlePrice < 0)
                            {
                                orderRealSettlePrice = 0D;
                            }
                            orderDto.setOrderRealSettlePrice(orderRealSettlePrice);
                            orderDto.setSendRedPacketMoney(sendMoney);
                            orderDao.updateOrder(orderDto);
                        }

                        // 发红包end
                        if (CommonConst.ORDER_STS_YJZ == orderDto.getOrderStatus())
                        {// 订单状态为已完成
                            OrderGoodsSettleUtil.detailSingleOrder(orderDto.getOrderId());// 开始分账
                        }
                    }
                    else
                    {
                        generatePlatformBill(orderDto, CommonConst.PLT_BILL_MNY_SOURCE_WX,
                                payDto.getPayAmount());// 平台收入为支付宝
                        generateShopMiddleBill(orderDto, payDto.getPayAmount());// 插入店铺账单
                        OrderGoodsSettleUtil.detailSingleXorder(orderDto.getOrderId());
                        handleAccountingStatByUser(orderDto);
                    }
                }
            }
        }
    }

    /**
     * 初始化推送内容
     *
     * @Title: initPushContent @param @param amount @param @param
     * payType @param @param obj @param @return @return JSONObject
     * 返回类型 @throws
     */
    public static JSONObject initPushContent(Double amount, String orderId, Integer payType, Integer payIndex,
            JSONObject obj)
    {
        obj.put("payIndex", payIndex);
        obj.put("orderId", orderId);
        obj.put("payType", payType);
        obj.put("payMoney", amount);//
        obj.put("ssMoney", amount);// 本次实收金额
        obj.put("payClientTime", new Date().getTime());
        return obj;
    }

    /**
     * 推送给用户
     *
     * @Title: push @param @param shopId @param @param
     * pushContent @param @throws Exception @return void 返回类型 @throws
     */
    private void push(Long shopId, JSONObject pushContent) throws Exception
    {
        ShopDto shopDto = shopService.getShopById(shopId);
        if (shopDto != null)
        {
            Long userId = shopDto.getPrincipalId();
            PushDto pushDto = new PushDto();
            pushDto.setUserId(userId);
            pushDto.setAction("order");
            pushDto.setContent(pushContent.toString());
            pushDto.setShopId(shopDto.getShopId());
            pushDto.setUserId(shopDto.getPrincipalId());
            pushDto.setTitle("订单支付通知");
            if (pushContent.has("collectOrder"))
            {
                pushDto.setShopId(shopId);
                pushService.pushInfoToShop2(pushDto);
            }
            else
            {
                List<PushUserTableDto> pushUserTables = pushUserTableDao
                        .getPushUserByUserId(shopDto.getPrincipalId(), CommonConst.USER_TYPE_TEN);
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

    public Map<String, Object> getDayOrderStat(Long userId, Long shopId, Integer dateType, String startDate,
            String endDate, List<Integer> orderStatuss, Integer orderTransactionType, Integer payType, Integer billerId,
            Integer cashierId, int pNo, int pSize, String yearOnYear, String ring) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stNo", (pNo - 1) * pSize);
        param.put("pSize", pSize);
        List<Long> shopIds = new ArrayList<Long>();
        shopIds.add(shopId);
        param.put("shopId", shopIds);
        param.put("dateType", dateType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("orderStatuss", orderStatuss);
        param.put("orderTransactionType", orderTransactionType);
        param.put("payType", payType);
        param.put("billerId", billerId);
        param.put("cashierId", cashierId);
        param.put("userId", userId);
        // 查询实收金额合计、平台服务费合计
        Double orderTotalPriceTotal = 0d;
        Double platformTotalIncomePriceTotal = 0d;
        Map amountState = orderDao.getAllOrderListAmountState(param);
        if (null != amountState && amountState.size() > 0)
        {
            orderTotalPriceTotal = Double.parseDouble(amountState.get("orderTotalPriceTotal") + "");
            platformTotalIncomePriceTotal = Double.parseDouble(amountState.get("platformTotalIncomePriceTotal") + "");
        }

        Double cashPayTotal = 0d;
        Double posPayTotal = 0d;
        Double onLinePayTotal = 0d;
        Double memberCardPayTotal = 0d;

        Double freePayTotal = 0d;
        Double customPay1Total = 0d;
        Double customPay2Total = 0d;
        Double customPay3Total = 0d;

        Map payAmountState = orderDao.getPayAmountStateByOrderIds(param);
        if (null != payAmountState && payAmountState.size() > 0)
        {
            cashPayTotal = Double.parseDouble(payAmountState.get("cashPayTotal") + "");
            posPayTotal = Double.parseDouble(payAmountState.get("posPayTotal") + "");
            onLinePayTotal = Double.parseDouble(payAmountState.get("onLinePayTotal") + "");
            memberCardPayTotal = Double.parseDouble(payAmountState.get("memberCardPayTotal") + "");

            freePayTotal = Double.parseDouble(payAmountState.get("freePayTotal") + "");
            customPay1Total = Double.parseDouble(payAmountState.get("customPay1Total") + "");
            customPay2Total = Double.parseDouble(payAmountState.get("customPay2Total") + "");
            customPay3Total = Double.parseDouble(payAmountState.get("customPay3Total") + "");

        }
        // 查询明细列表
        Integer rCount = orderDao.getDayOrderStatCount(param);
        Integer orderListCount = orderDao.getAllOrderListCount(param);
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        if (rCount != 0)
        {
            orderList = orderDao.getDayOrderStatDetail(param);
            if (null != orderList && orderList.size() > 0)
            {
                List<Map<String, Object>> orderPayList = orderDao.getDayOrderPayStat(param);
                for (Map<String, Object> bean : orderList)
                {
                    String orderDate = CommonValidUtil.isEmpty(bean.get("orderDate")) ? null : bean.get("orderDate")
                            .toString();
                    if (!StringUtils.isEmpty(orderDate))
                    {
                        for (Map<String, Object> node : orderPayList)
                        {
                            String payOrderDate = CommonValidUtil.isEmpty(node.get("orderDate")) ? null : node
                                    .get("orderDate").toString();
                            if (StringUtils.equals(orderDate, payOrderDate))
                            {
                                bean.put("cashPay", node.get("cashPay"));
                                bean.put("posPay", node.get("posPay"));
                                bean.put("onLinePay", node.get("onLinePay"));

                                bean.put("freePay", node.get("freePay"));
                                bean.put("customPay1", node.get("customPay1"));
                                bean.put("customPay2", node.get("customPay2"));
                                bean.put("customPay3", node.get("customPay3"));
                                break;
                            }
                        }
                    }
                }
            }
        }

        Map preAmountState = null;
        Double preYearOrderTotalPriceTotal = 0d;
        if (yearOnYear != null && (yearOnYear.equals("1") || yearOnYear.equals("2")))
        {//日/月同比
            param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 3));
            param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 3));
            preAmountState = orderDao.getAllOrderListAmountState(param);
            preYearOrderTotalPriceTotal = Double.parseDouble(preAmountState.get("orderTotalPriceTotal") + "");
        }

        Map ringAmountState = null;
        Double ringOrderTotalPriceTotal = 0d;
        if (ring != null)
        {
            if (ring.equals("1"))
            {//日环比
                param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 1));
                param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 1));
                ringAmountState = orderDao.getAllOrderListAmountState(param);
                ringOrderTotalPriceTotal = Double.parseDouble(ringAmountState.get("orderTotalPriceTotal") + "");
            }
            else if (ring.equals("2"))
            {//月环比
                param.put("startDate", getDateString(startDate, "yyyy-MM-dd", 2));
                param.put("endDate", getDateString(endDate, "yyyy-MM-dd", 2));
                ringAmountState = orderDao.getAllOrderListAmountState(param);
                ringOrderTotalPriceTotal = Double.parseDouble(ringAmountState.get("orderTotalPriceTotal") + "");
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("pNo", pNo);
        resultMap.put("rCount", rCount);
        resultMap.put("orderTotalPriceTotal", orderTotalPriceTotal);
        resultMap.put("platformTotalIncomePriceTotal", NumberUtil.formatDouble(platformTotalIncomePriceTotal, 4));
        resultMap.put("cashPayTotal", NumberUtil.formatDouble(cashPayTotal, 4));
        resultMap.put("posPayTotal", NumberUtil.formatDouble(posPayTotal, 4));
        resultMap.put("onLinePayTotal", NumberUtil.formatDouble(onLinePayTotal, 4));
        resultMap.put("memberCardPayTotal", NumberUtil.formatDouble(memberCardPayTotal, 4));


        resultMap.put("freePayTotal", NumberUtil.formatDouble(freePayTotal, 4));
        resultMap.put("customPay1Total", NumberUtil.formatDouble(customPay1Total, 4));
        resultMap.put("customPay2Total", NumberUtil.formatDouble(customPay2Total, 4));
        resultMap.put("customPay3Total", NumberUtil.formatDouble(customPay3Total, 4));



        resultMap.put("preYearOrderTotalPriceTotal", preYearOrderTotalPriceTotal);
        resultMap.put("ringOrderTotalPriceTotal", ringOrderTotalPriceTotal);
        resultMap.put("lst", orderList);
        resultMap.put("orderTotalNum", orderListCount);
        return resultMap;
    }

    @Override public PageModel getOrderListDetail(Map<String, Object> params, int page, int pageSize) throws Exception
    {
        params.put("pNo", page - 1);
        params.put("pSize", pageSize);
        PageModel pm = new PageModel();
        // 获取主订单列表大小
        int totalItem = posOrderDetailDao.getOrderListDetailCount(params);
        // 获取主订单列表
        List<POSOrderDetailDto> orderList = posOrderDetailDao.getOrderListDetail(params, page, pageSize);
        OrderGoodsDto order = null;
        List<OrderGoodsDto> orderGoodsList = null;
        List<PayDto> payList = null;
        if (orderList != null && orderList.size() > 0)
        {
            for (POSOrderDetailDto e : orderList)
            {
                order = new OrderGoodsDto();
                order.setOrderId(e.getOrderId());
                // 包装每个单的商品列表
                orderGoodsList = orderGoodsDao.getOGoodsListByOrderId(order);
                e.setGoods(DataConvertUtil.odgdsList2POSOdgdsList(orderGoodsList));
                // 包装每个单的支付列表 --收银机要求返回正常的数据
                payList = payDao.getOrderPayList(e.getOrderId(), CommonConst.PAY_STATUS_PAY_SUCCESS);
                e.setPaies(DataConvertUtil.odpayList2POSOdpayList(payList));
            }
        }
        pm.setList(orderList);
        pm.setTotalItem(totalItem);
        pm.setToPage(page);
        pm.setPageSize(pageSize);
        return pm;
    }

    public void updatePlatformServePrice(OrderDto order) throws Exception
    {
        Double serveMoney = computeServeMoneyByOrder(order);
        if (serveMoney == 0D)
        {
            return;
        }
        String orderId = order.getOrderId();
        orderDao.updatePlatformServePrice(serveMoney, orderId);
    }

    @Override public void pushOrder(String orderId, Long userId, Double payAmount)
    {
        try
        {
            // 支付成功，向用户推送消息，并将推送的消息记录

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "pay");
            jsonObject.put("orderId", orderId);
            jsonObject.put("amount", payAmount);
            commonService.pushUserMsg(jsonObject, userId, CommonConst.USER_TYPE_ZREO);

            // 向收银机推送
            OrderDto order = getOrderMainById(orderId);
            if (order != null)
            {
                // TODO 空指针
                logger.info("订单信息：orderId=" + orderId + ",orderStatus=" + order.getOrderStatus() + ",orderPayStatus="
                        + order.getPayStatus() + "。");
                // 订单状态为待确认、支付状态为已支付，向一点管家推送订单确认消息
                if (order.getOrderStatus() == CommonConst.ORDER_STS_DQR
                        && order.getPayStatus() == CommonConst.PAY_STATUS_PAYED)
                {
                    commonService.pushShopUserMsg("confirmOrder", "您有新订单，请在10分钟内处理", order.getShopId());
                }

                if (null != order)
                {
                    Integer orderStatus = order.getOrderStatus();
                    // 如果订单还是已预订状态则需要给收银机进行推送 -- 暂时不推
                    // if (null != orderStatus && CommonConst.ORDER_STS_YYD
                    // == orderStatus) {
                    // PlaceOrderPushMessageUtil.detailSingleOrder(orderId);
                    // }
                    Long shopId = order.getShopId();
                    if (null != shopId)
                    {
                        //向店铺收银机推送
                        BusMsg busMsg = new BusMsg();
                        busMsg.setBusCode("THIRD_PAY_PUSH_SHOP_CASHIER");
                        busMsg.setCreateTime(new Date());
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("order", order);
                        //                        params.put("payReason", payReason);
                        busMsg.setParams(params);
                        try
                        {
                            busMsgSender.sendBusMsg(busMsg);
                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
                        //临时性质，保持原有业务流程
                        StringBuilder content = new StringBuilder();
                        content.append("{");
                        content.append("\"shopId\":" + shopId + ",");
                        content.append("\"action\":\"cashOrder\",");
                        content.append("\"data\":{\"id\":\"" + orderId + "\",\"orderStatus\":" + orderStatus
                                + ",\"payStatus\":" + order.getPayStatus() + "}");
                        content.append("}");
                        PushDto push = new PushDto();
                        push.setAction("cashOrder");
                        push.setContent(content.toString());
                        push.setUserId(userId);
                        push.setShopId(shopId);
                        pushService.pushInfoToShop2(push);
                    }
                }
            }
        }

        catch (Exception e)
        {
            logger.error("支付消息推送异常", e);
        }
    }

    @Override public int updateOrderRealSettlePrice(String orderId, Double orderRealSettlePrice,
            Double sendRedPacketMoney) throws Exception
    {
        return this.orderDao.updateOrderRealSettlePrice(orderId, orderRealSettlePrice, sendRedPacketMoney);
    }

    @Override public void updateGoodsAndShopSoldNum(String orderId) throws Exception
    {
        // 查询订单
        OrderDto order = orderDao.getOrderMainById(orderId);
        if (order.getOrderStatus() == CommonConst.ORDER_STATUS_FINISH)
        {

            // 更新商铺订单销量,每次新增销售数量1次
            if (CommonConst.REVERSE_SETTLE_FLAG != order.getSettleFlag().intValue())
            {
                shopDao.incrShopSoldNum(order.getShopId(), 1);
            }
            // 查询订单商品销量
            List<OrderGoodsDto> orderGoodsDtos = orderGoodsDao.getOGoodsListByOrderId(orderId);
            // 更新商品数销量
            if (CollectionUtils.isNotEmpty(orderGoodsDtos))
            {
                for (OrderGoodsDto orderGoodsDto : orderGoodsDtos)
                {
                    goodsDao.incrGoodsSoldNum(orderGoodsDto.getGoodsId(),
                            Math.round(getChangeGoodsNum(orderGoodsDto, order)));
                }
            }
        }

    }

    /**
     * 获取反结账后的订单商品变更数量
     */
    private Double getChangeGoodsNum(OrderGoodsDto orderGoodsDto, OrderDto order)
    {
        Double changeNum = orderGoodsDto.getGoodsNumber();
        //0-正常退菜，1-反结账退菜，2-正常加菜，3-反结账加菜
        Integer isCancle = orderGoodsDto.getIsCancle() == null ? 2 : orderGoodsDto.getIsCancle().intValue();
        //反结账
        if (CommonConst.REVERSE_SETTLE_FLAG == order.getSettleFlag().intValue())
        {
            //反结账正常情况不修改库存
            if (isCancle == 0 || isCancle == 2)
            {
                changeNum = 0D;
            }
            if (isCancle == 1)
            {
                changeNum = -orderGoodsDto.getGoodsNumber();
            }
            if (isCancle == 3)
            {
                changeNum = orderGoodsDto.getGoodsNumber();
            }
        }
        return changeNum;
    }

    @Override public void dealSettleOrderByShopId(Long shopId) throws Exception
    {
        // 处理结算
        List<String> orderIds = orderDao.getNotSettleOrderIds(shopId, null, null);
        if (CollectionUtils.isNotEmpty(orderIds))
        {
            for (String orderId : orderIds)
            {
                logger.info("充值后处理未结算订单ID:" + orderId);
                OrderGoodsSettleUtil.detailSingleOrder(orderId);
            }
        }
    }

    @Override public void dealOrderSettle(OrderDto order, Map orderGoodsSettleSetting) throws Exception
    {
        //线上账户金额转到保证金账户
        //    	payServiceImp.dealDepositAmount(order.getShopId(), order.getPlatformTotalIncome());
        splitOrderByPrice(order, orderGoodsSettleSetting);
        dealSettleByPrice(order, orderGoodsSettleSetting);
    }

    @Override public PageModel queryOrderByOrderCode(Long shopId, String payCode, Integer payStatus, Integer payType,
            int pNo, int pSize)
    {
        int count = orderDao.countOrderByShopIdAndOrderCode(shopId, payCode, payStatus, payType);
        List<OrderDto> orderDtos = orderDao
                .queryOrderByShopIdAndOrderCode(shopId, payCode, payStatus, payType, pNo, pSize);
        PageModel rs = new PageModel();
        rs.setTotalItem(count);
        if (count <= 0)
        {
            return rs;
        }
        if (null == orderDtos || orderDtos.isEmpty())
        {
            return rs;
        }
        // OrderDto orderDto = orderDtos.get(0);

        if (null != orderDtos)
        {
            OrderModelForApp tempResult = null;
            List data = rs.getList();
            for (OrderDto orderDto : orderDtos)
            {
                List<Map<String, Object>> pays = null;
                try
                {
                    pays = payDao.getPayLogByOrderId(orderDto.getOrderId());
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                tempResult = new OrderModelForApp();
                tempResult.setOrderTitle(orderDto.getOrderTitle());
                tempResult.setOrderId(orderDto.getOrderId());
                tempResult.setPayStatus(orderDto.getPayStatus());
                tempResult.setShopName(orderDto.getShopName());
                tempResult.setSettlePrice(orderDto.getSettlePrice());
                tempResult.setSettleTime(orderDto.getSettleTime());
                if (null != pays && !pays.isEmpty())
                {
                    List<OrderPayModelForApp> payDtos = new ArrayList<OrderPayModelForApp>();
                    OrderPayModelForApp orderPayModelForApp = null;
                    for (Map<String, Object> tempMap : pays)
                    {
                        orderPayModelForApp = new OrderPayModelForApp();
                        Double payAmount = tempMap.get("payMoney") == null ? null : Double
                                .parseDouble(tempMap.get("payMoney") + "");
                        orderPayModelForApp.setPayAmount(payAmount);
                        Integer payType1 = tempMap.get("payType") == null ? null : (Integer) tempMap.get("payType");
                        orderPayModelForApp.setPayType(payType1);
                        Object obj = tempMap.get("paidTime");
                        if (null != obj)
                        {
                            Timestamp time = (Timestamp) obj;
                            Date date = new Date(time.getTime());
                            orderPayModelForApp.setOrderPayTime(DateUtils.format(date, DateUtils.DATETIME_FORMAT));
                        }
                        payDtos.add(orderPayModelForApp);
                    }
                    tempResult.setPayList(payDtos);
                }
                data.add(tempResult);
            }
            // rs.getl
        }
        return rs;
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.order.IOrderServcie#bitchSettleOrder(com.idcq.appserver.dto.order.OrderDto)
     */
    @Override public String bitchSettleOrder(OrderDto order)
    {

        String failOrderId = null;
        String orderId = order.getOrderId();

        try
        {
            if (order.getIsMember() == 1)
            {
                Double sendMoney = packetService.sendRedPacketToUser(order);
                if (sendMoney != 0)
                {
                    // 更新订单结算价格
                    Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
                    if (orderRealSettlePrice < 0)
                    {
                        orderRealSettlePrice = 0D;
                    }
                    order.setOrderRealSettlePrice(orderRealSettlePrice);
                    order.setSendRedPacketMoney(sendMoney);
                }
            }
            // 更新订单
            order.setOrderStatus(CommonConst.ORDER_STATUS_FINISH);
            order.setServerLastTime(new Date().getTime());
            orderDao.updateOrder(order);

            // 插入账单支付记录
            if (CommonConst.ORDER_STS_YKD == order.getOrderStatus())
            {
                insertOrderPayForCash(orderId, order.getSettlePrice(), order.getUserId(), order.getShopId(), null,
                        CommonConst.PAY_TYPE_CASH, order.getClientSystemType());
            }

            collectDao.updateShopResourceStatus(orderId, CommonConst.RESOURCE_STATUS_NOT_IN_USE);
            if (order.getIsMember() == 1)
            {
                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId, CommonConst.ORDER_PAY_TYPE_SINGLE);
            }
            else
            {
                OrderGoodsSettleUtil.detailSingleXorder(orderId);
                handleAccountingStatByUser(order);
            }
        }
        catch (Exception e)
        {
            logger.error("批量结账失败！订单id：" + orderId);
            failOrderId = orderId;

        }

        return failOrderId;

    }

    private void insertOrderPayForCash(String orderId, Double payAmount, Long userId, Long shopId, Long payId,
            Integer PayType, Integer clientSystem) throws Exception
    {

        if (payAmount > 0)
        {
            PayDto orderPay = new PayDto();
            String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);

            orderPay.setOrderId(orderId);
            orderPay.setPayAmount(payAmount);
            orderPay.setOrderPayType(CommonConst.PAY_TYPE_SINGLE);
            orderPay.setUserId(userId);
            orderPay.setOrderPayTime(nowTime);
            orderPay.setLastUpdateTime(nowTime);
            orderPay.setPayeeType(CommonConst.PAYEE_TYPE_SHOP);
            orderPay.setShopId(shopId);
            orderPay.setPayId(payId);
            orderPay.setPayType(PayType);
            orderPay.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
            orderPay.setClientSystem(clientSystem);

            payDao.addOrderPay(orderPay);
        }

    }

    public Map<String, Object> getRedPackOrderDetail(String orderId) throws Exception
    {
        return orderDao.getRedPackOrderDetail(orderId);
    }

    @Override public void updateShopResourceStatus(String orderId)
    {
        collectDao.updateShopResourceStatus(orderId, CommonConst.RESOURCE_STATUS_NOT_IN_USE);
    }

    @Override
    public void settleOrder(PayDto payDto, Integer moneyType) throws Exception
    {
        String orderId = payDto.getOrderId();
        logger.info("开始兑换礼品相关结算:" + orderId);
        Double payMoney = payDto.getPayAmount();
        OrderDto order = orderDao.getOrderById(orderId);
        Long userId = order.getUserId();
        UserDto userDto = userDao.getUserById(order.getUserId());
        UserAccountDto userAccountDto = userAccountDao.getAccountMoney(userId);
        Date now = new Date();
        Long shopId = order.getShopId();
        shopId = shopId == null ? 1l : shopId;
        order.setOrderStatus(CommonConst.ORDER_STATUS_FINISH);
        if (order.getIsMember() == 1)
        {
            Double settlePrice = order.getGoodsPrice();
            if (payMoney < settlePrice)
            {
                throw new ValidateException(CodeConst.CODE_FAILURE, "支付金额有误");
            }
            if(userAccountDto.getConsumeAmount() < settlePrice)
            {
                throw new ValidateException(CodeConst.CODE_FAILURE, "账户余额不足");
            }
            //更新用户账户
            userAccountDao.updateUserAccount(userId, -settlePrice, null, null, null, null, null, -settlePrice, null, null, null, null, null, null, null);

            List<OrderGoodsDto> orderGoodsDtos = orderGoodsDao.getOGoodsListByOrderId(orderId);
            //目前只有单商品
            OrderGoodsDto orderGoodsDto = orderGoodsDtos.get(0);

            Double curAmount = userAccountDto.getAmount();
            /* 开始插入账单 */

            //插入用户账单
            UserBillDto userBillDto = new UserBillDto();
            userBillDto.setAccountAmount(curAmount);
            userBillDto.setAccountAfterAmount(NumberUtil.sub(userAccountDto.getConsumeAmount(), settlePrice));
            userBillDto.setBillType(CommonConst.BILL_TYPE_PLATFORM_PRESENT);
            userBillDto.setUserRole(NumberUtil.getStr(userDto.getUserType()));
            //消费币
            userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_CONSUM);
            userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_GET_PLATFORM_PRESENT);
            userBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
            userBillDto.setBillStatus(RewardsEnum.HAVE_SETTLEMENT.getValue());
            userBillDto.setOrderId(orderId);
            Long goodsId = orderGoodsDto.getGoodsId();
            userBillDto.setGoodsId(goodsId);
            userBillDto.setGoodsNumber(orderGoodsDto.getGoodsNumber());
            userBillDto.setGoodsSettlePrice(settlePrice);
            userBillDto.setCreateTime(new Date());
            GoodsDto goodsDto = goodsDao.getGoodsById(goodsId);
            userBillDto.setBillTitle("平台礼品:" + goodsDto.getGoodsName());
            userBillDto.setBillLogo(goodsDto.getGoodsLogo1());
            userBillDto.setBillDesc("兑换平台礼品");
            userBillDto.setConsumerUserId(userDto.getUserId());
            userBillDto.setConsumerMobile(userDto.getMobile());
            userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_PROCESS);
            userBillDto.setUserId(userId);
            userBillDto.setMoney(-settlePrice);
            userBillDao.insertUserBill(userBillDto);

            //插入平台账单
            PlatformBillDto platformBillDto = new PlatformBillDto();
            platformBillDto.setBillType(CommonConst.BILL_TYPE_PLATFORM_PRESENT);
            platformBillDto.setConsumerUserId(userId);
            platformBillDto.setCreateTime(now);
            platformBillDto.setSettleTime(now);
            platformBillDto.setBillDesc("用户兑换平台礼品");
            platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
            platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
            platformBillDto.setConsumerMobile(userDto.getMobile());
            platformBillDto.setGoodsId(goodsId);
            platformBillDto.setGoodsNumber(orderGoodsDto.getGoodsNumber());
            platformBillDto.setGoodsSettlePrice(settlePrice);
            platformBillDto.setMoney(settlePrice);
            platformBillDto.setMoneySource(CommonConst.MONEY_SOURCE_CP);
            platformBillDto.setOrderId(orderId);
            platformBillDto.setPlatformBillType(CommonConst.USER_BILL_TYPE_GET_PLATFORM_PRESENT);
            platformBillDto.setShopId(shopId);
            platformBillDao.insertPlatformBill(platformBillDto);


            //开始插入order_pay
            payDto.setOrderPayTime(DateUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
            payDto.setOrderPayType(0);
            payDto.setPayeeType(CommonConst.PAYEE_TYPE_PLATFORM); // 收款人类型
            payDto.setShopId(shopId);
            payDto.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
            payDto.setAutoSettleFlag(CommonConst.AUTO_SETTLE_FLAG_FLASE);
            payDto.setUserId(userId);
            payDto.setPayType(CommonConst.PAY_TYPE_CASH_COUPON_MONEY);
            this.payDao.addOrderPay(payDto);

            order.setSettleFlag(CommonConst.USER_SETTLE_FLAG_TRUE);
            order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
            order.setServerLastTime(now.getTime());
            orderDao.updateOrder(order);

        }
        else
        {
            throw new ValidateException(CodeConst.CODE_FAILURE, "非会员不受支持");
        }

    }

}
