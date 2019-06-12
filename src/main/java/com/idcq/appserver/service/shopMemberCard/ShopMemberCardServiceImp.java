package com.idcq.appserver.service.shopMemberCard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dao.goods.IShopMemberCardGoodsDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.shopMemberCard.IShopMemberCardBillDao;
import com.idcq.appserver.dao.shopMemberCard.IShopMemberCardDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.ShopMemberCardGoods;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;


/**
 * 店铺会员卡业务逻辑层
 * 
 * @ClassName: ShopMemberCardServiceImp
 * @Description: TODO
 * @author 张鹏程
 * @date 2016年1月14日 下午2:21:38
 *
 */
@Service
public class ShopMemberCardServiceImp implements IShopMemberCardService {

	/**
	 * 店铺会员卡数据访问层
	 */
	@Autowired
	private IShopMemberCardDao shopMemberCardDao;

	/**
	 * 会员账单
	 */
	@Autowired
	private IShopMemberCardBillDao shopMemberCardBillDao;

	@Autowired
	IShopMemberCardGoodsDao shopMemberCardGoodsDao;

	@Autowired
	IGoodsSetDao goodsSetDao;

	@Autowired
	IGoodsDao goodsDao;

	@Autowired
	private IUserDao userDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IShopMemberDao shopMemberDao;
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private IPayDao payDao;

	/**
	 * 操作店铺会员卡 @Title: delShopMemberCardByIds @param @param
	 * shopMemberCardDto @param @throws Exception @throws
	 */
	public void delShopMemberCardByIds(Map<String, Object> requestMap) throws Exception {
	    
	    shopMemberCardDao.delShopMemberCardByIds(requestMap);
	}

	/**
	 * 判断会员卡是否存在 @Title: checkCardExist @param @param shopId @param @param
	 * mobile @param @return @param @throws Exception @throws
	 */
	public boolean checkCardExist(Long shopId, String mobile) throws Exception {
		ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
		shopMemberCardDto.setMobile(mobile);
		shopMemberCardDto.setShopId(shopId);
		shopMemberCardDto.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);//针对充值卡
		List<ShopMemberCardDto> cards = shopMemberCardDao.getShopMemberCardList(shopMemberCardDto);
		if (cards != null && cards.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean checkCardExistByMid(Long memberId)throws Exception {
	    ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
        shopMemberCardDto.setMemberId(memberId);
        shopMemberCardDto.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);//针对充值卡
        List<ShopMemberCardDto> cards = shopMemberCardDao.getShopMemberCardList(shopMemberCardDto);
        if (cards != null && cards.size() > 0) {
            return true;
        }
        return false;
	}

	/**
	 * 添加店铺会员卡 @Title: insertShopMemberCard @param @param
	 * shopMemberCardDto @param @throws Exception @throws
	 */
	public Integer insertShopMemberCard(ShopMemberCardDto shopMemberCardDto) throws Exception {
		UserDto userDto = userDao.getUserByMobile(shopMemberCardDto.getMobile());
		if (userDto == null) {// 不存在此会员则，插入会员
			userDto = new UserDto();
			userDto.setMobile(shopMemberCardDto.getMobile());
			userDto.setUserType(CommonConst.USER_TYPE_ZREO);
			userDto.setUserType2(CommonConst.USER_TYPE_ZREO);
			userDto.setIsMember(CommonConst.USER_IS_MEMBER);
			userDto.setStatus(CommonConst.USER_NORMAL_STATUS);// 用户状态为正常
			userDto.setPassword(shopMemberCardDto.getMobile());
			userDto.setSex(shopMemberCardDto.getSex());
			userDto.setRegisterType(CommonConst.REGISTER_TYPE_COLLECT);// 会员
																		// 的注册类型
			userDao.saveUser(userDto);
		}
		if (shopMemberCardDto.getOperaterId() == null) {
			shopMemberCardDto.setOperaterId(0);
		}
		shopMemberCardDto.setCardMoney(0d);
		shopMemberCardDto.setAmount(0d);
		shopMemberCardDto.setUsedMoney(0d);
		shopMemberCardDto.setOpertaterTime(new Date());
		shopMemberCardDto.setUserId(userDto.getUserId());
		shopMemberCardDao.insertShopMemberCard(shopMemberCardDto);// 会员卡id
		Integer cardId=shopMemberCardDto.getCardId();
		// 如果是次卡，就添加次卡限定商品。否则就不添加
		if (shopMemberCardDto.getCardType() != null && shopMemberCardDto.getCardType().equals("2")) {
			String goodsSetId = shopMemberCardDto.getGoodsSetId();// 套餐id
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopMemberCardDto.getShopId());
			map.put("goodsId", goodsSetId);
			List<GoodsSetDto> goodsSetsList = goodsSetDao.getGoodsSetList(map);// 该套餐下所有的服务
			CommonValidUtil.validListNull(goodsSetsList,CodeConst.CODE_PARAMETER_NOT_NULL,"该套餐不存在");
			List<ShopMemberCardGoods> goodsList = new ArrayList<ShopMemberCardGoods>();
			for (GoodsSetDto goodsSetDto : goodsSetsList) {
				ShopMemberCardGoods shopMemberCardGoods = new ShopMemberCardGoods();
				shopMemberCardGoods.setCardId(Long.valueOf(cardId));
				shopMemberCardGoods.setGoodsId(goodsSetDto.getGoodsId());
				shopMemberCardGoods.setGoodsName(goodsSetDto.getGoodsName());
				shopMemberCardGoods.setPrice(goodsSetDto.getPrice());
				shopMemberCardGoods.setGoodsNumber(goodsSetDto.getGoodsNumber().intValue());
				goodsList.add(shopMemberCardGoods);
			}
			shopMemberCardGoodsDao.batchInsertShopMemberCardGoods(goodsList);
			ShopMemberCardBillDto shopMemberCardBillDto = new ShopMemberCardBillDto();
			shopMemberCardBillDto.setCardId(cardId);
			shopMemberCardBillDto.setCardBillType(CommonConst.CARD_TIME_CARD_TYPE);// 购买次卡
			GoodsDto goodsDto = goodsDao.getGoodsById(Long.valueOf(shopMemberCardDto.getGoodsSetId()));// 次卡信息
			shopMemberCardBillDto.setChargeMoney(goodsDto.getStandardPrice());// 账单金额=购买次卡的金额
			shopMemberCardBillDto.setChargeType(CommonConst.CARD_CHARGE_TYPE_MONEY);
			shopMemberCardBillDto.setShopId(shopMemberCardDto.getShopId().intValue());
			shopMemberCardBillDto.setBillTime(new Date());
			shopMemberCardBillDto.setPresentMoney(shopMemberCardDto.getPresentMoney());// 赠送金额
			shopMemberCardBillDto.setAfterAmount(shopMemberCardBillDto.getAfterAmount());//
			shopMemberCardBillDao.insertShopMemberCardBill(shopMemberCardBillDto);
		}
		return cardId;
	}

	@Override
	public PageModel queryShopMemberCardList(ShopMemberCardDto shopMemberCardDto, PageModel pageModel)
			throws Exception {
		return shopMemberCardDao.getShopMemberCards(shopMemberCardDto, pageModel);
	}

	/**
	 * 充值店铺会员卡 @Title: chargeShopMemberCard @param @param
	 * shopMemberCardDto @param @throws Exception @throws
	 */
	public Map<String, Object> chargeShopMemberCard(ShopMemberCardDto shopMemberCardDto) throws Exception {
		UserDto userDto = userDao.getUserByMobile(shopMemberCardDto.getMobile());
		if (userDto == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_MEMBER);
		}
		ShopMemberCardDto searchCondition = new ShopMemberCardDto();
		searchCondition.setMobile(shopMemberCardDto.getMobile());
		searchCondition.setShopId(shopMemberCardDto.getShopId());
		searchCondition.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);//充值只针对充值卡
		searchCondition.setCardStatus(CommonConst.MEMBERCARD_STATUS_NORMAL);
		Double amount = NumberUtil.add(shopMemberCardDto.getChargeMoney(),shopMemberCardDto.getPresentMoney());
		List<ShopMemberCardDto> shopMemberCardList = shopMemberCardDao.getShopMemberCardList(searchCondition);
		Double beforeAmount = 0D;
		if (shopMemberCardList.size() > 0) {
			shopMemberCardDto.setCardId(shopMemberCardList.get(0).getCardId());
			beforeAmount = shopMemberCardList.get(0).getAmount();
		}else{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_MEMBER_CARD);
		}
		shopMemberCardDto.setUserId(userDto.getUserId());
		shopMemberCardDto.setAmount(amount);// 需要增加的金额为充值金额+赠送金额
		shopMemberCardDao.chargeShopMemberCard(shopMemberCardDto);// 将账户金额改变
		String orderId = "CZ" + FieldGenerateUtil.generatebitOrderId(Long.valueOf(shopMemberCardDto.getCardId()));
		
        PayDto payDto = new PayDto();
        payDto.setOrderId(orderId);
        payDto.setPayType(shopMemberCardDto.getPayType());
        payDto.setPayAmount(shopMemberCardDto.getChargeMoney());
        payDto.setOrderPayType(CommonConst.PAY_TYPE_CHARGE);
        payDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
        payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP); // 收款人类型
        payDto.setShopId(shopMemberCardDto.getShopId());
        payDto.setUserPayTime(DateUtils.getCurDate());
        payDto.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
        payDto.setAutoSettleFlag(CommonConst.AUTO_SETTLE_FLAG_TRUE);
        payDto.setUserId(userDto.getUserId());
        if(shopMemberCardDto.getChargeMoney()>0){
            this.payDao.addOrderPay(payDto);
        }		
		ShopMemberCardBillDto shopMemberCardBillDto = new ShopMemberCardBillDto();// 生成充值账单
		shopMemberCardBillDto.setChargeMoney(shopMemberCardDto.getChargeMoney());// 充值金额
		shopMemberCardBillDto.setPresentMoney(shopMemberCardDto.getPresentMoney());// 赠送金额
		shopMemberCardBillDto.setShopId(shopMemberCardDto.getShopId().intValue());
		shopMemberCardBillDto.setBillAmount(amount);// 账单金额为充值金额+赠送金额
		shopMemberCardBillDto.setCardId(shopMemberCardDto.getCardId());
		shopMemberCardBillDto.setChargeType(shopMemberCardDto.getPayType());
		shopMemberCardBillDto.setCardBillType(CommonConst.CARD_BILL_TYPE_CHARGE);// 类型为充值
		shopMemberCardBillDto.setCardBillDesc(shopMemberCardDto.getCardDesc());
		shopMemberCardBillDto.setOpertaterId(shopMemberCardDto.getOperaterId());
		shopMemberCardBillDto.setAfterAmount(NumberUtil.add(amount, beforeAmount));
		shopMemberCardBillDto.setOrderId(orderId);
		shopMemberCardBillDto.setPayId(payDto.getPayId());
		shopMemberCardBillDto.setBillerId(shopMemberCardDto.getBillerId());
		shopMemberCardBillDto.setClientRechargeTime(shopMemberCardDto.getClientRechargeTime());
		shopMemberCardBillDao.insertShopMemberCardBill(shopMemberCardBillDto);// 插入用户账单
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("orderId", orderId);
		shopMemberCardList = shopMemberCardDao.getShopMemberCardList(searchCondition);
		if (shopMemberCardList.size() > 0) {
			ShopMemberCardDto chargeShopMemberCardDto = shopMemberCardList.get(0);
			chargeShopMemberCardDto.getCardMoney();
			result.put("afterAmount", chargeShopMemberCardDto.getAmount());
			result.put("billMoney", amount);
			result.put("usedTimes", shopMemberCardDto.getUsedNum());
			result.put("totalUsedAmount", shopMemberCardDto.getUsedMoney());
		}
		
		//记录店铺营业统计
		ShopIncomeStatDto shopIncomeStat = new ShopIncomeStatDto();
		shopIncomeStat.setOrderId(orderId);
		shopIncomeStat.setSettlePrice(0D);
		shopIncomeStat.setChargePrice(shopMemberCardDto.getChargeMoney());
		shopIncomeStat.setOrderTime(new Date());
		shopIncomeStat.setOrderTitle("会员卡充值");
        shopIncomeStat.setMobile(shopMemberCardDto.getMobile());
        shopIncomeStat.setShopId(shopMemberCardDto.getShopId());
        shopIncomeStat.setFinishTime(new Date());
        shopIncomeStat.setIncomeType(CommonConst.INCOME_TYPE_CHARGE);
        shopIncomeStat.setStatDate(new Date());
        shopIncomeStat.setCashierId(Long.valueOf(shopMemberCardDto.getOperaterId()));
        shopIncomeStat.setPlatformServeSharePrice(0D);
        shopIncomeStat.setFreePay(0D);
        shopIncomeStat.setCustomPay1(0D);
        shopIncomeStat.setCustomPay2(0D);
        shopIncomeStat.setCustomPay3(0D);
        Integer payType = shopMemberCardDto.getPayType();
        if(payType != null && payType == CommonConst.PAY_TYPE_CASH) {
            shopIncomeStat.setCashPay(shopMemberCardDto.getChargeMoney());
        } else if (payType != null && payType == CommonConst.PAY_TYPE_CASH) {
            shopIncomeStat.setPosPay(shopMemberCardDto.getChargeMoney());
        } 
        // 插入财务统计
        shopDao.insertAccountingStat(shopIncomeStat);
         
		//TODO 短信通知
		if(shopMemberCardDto.getIsSendSms() != null && shopMemberCardDto.getIsSendSms() == 1){
			 // 需要发送验证码告诉消费者支付金额及余额
            SmsReplaceContent src = buildSmsContent(result, shopMemberCardDto);
            sendSmsService.sendSmsMobileCode(src);
		}
		return result;
	}
	
	  private SmsReplaceContent buildSmsContent(Map map, ShopMemberCardDto shopMemberCardDto) throws Exception{
	        BigDecimal init = new BigDecimal(0);
	        SmsReplaceContent src = new SmsReplaceContent();
	        //账户余额
	        BigDecimal amount=null;
	        ShopMemberCardDto shopMemberCard = shopMemberCardDao.getShopMemberCardById(shopMemberCardDto.getCardId());
	        if(shopMemberCard != null){
	        	  amount = new BigDecimal(shopMemberCard.getAmount());
	        }
	        //充值金额
            Double presentMoney = shopMemberCardDto.getPresentMoney();
	        //充值金额
	        BigDecimal chargeMoney =new BigDecimal(shopMemberCardDto.getChargeMoney());
	        ShopDto shopDto = shopDao.getShopByIdWithoutCache(shopMemberCardDto.getShopId());
	        if (null == shopDto) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOP);
			}
	        ShopMemberDto shopMemberDto = shopMemberDao.getShopMbByMobileAndShopId(shopMemberCardDto.getMobile(), shopMemberCardDto.getShopId(), CommonConst.MEMBER_STATUS_DELETE);
	        if (null == shopMemberDto) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOPMEMBER);
			}
	        if (amount == null)
	        {
	        	amount = init;
	        }
	        if (chargeMoney == null)
	        {
	        	chargeMoney = init;
	        }
	        if (presentMoney == null)
            {
	            presentMoney = init.doubleValue();
            }
	        src.setShopName(shopDto.getShopName());
	        src.setChargeMoney(chargeMoney.doubleValue());
	        src.setAmount(amount.doubleValue());
	        src.setConsumeDate(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
	        src.setShopManagerName(shopMemberDto.getName());
	        src.setUsage(CommonConst.SHOP_MEMBER_CARD_RECHARGE);
	        if(presentMoney > 0) {
	            src.setUsage(CommonConst.SHOP_MEMBER_CARD_RECHARGE_SEND);
	            src.setMoney(presentMoney);
	        }
	        src.setMobile(shopMemberCardDto.getMobile());
	        return src;
	    }

	/**
	 * 店铺会员卡消费 @Title: shopMemberCardComsume @param @param
	 * shopMemberCardDto @param @param orderId @param @throws Exception @throws
	 */
	public Double shopMemberCardComsume(ShopMemberCardDto shopMemberCardDto, String orderId) throws Exception {
		ShopMemberCardDto searchCondition = new ShopMemberCardDto();
		searchCondition.setMobile(shopMemberCardDto.getMobile());
		searchCondition.setShopId(shopMemberCardDto.getShopId());
		searchCondition.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);
		searchCondition.setCardStatus(CommonConst.MEMBERCARD_STATUS_NORMAL);
		List<ShopMemberCardDto> shopMemberCardList = shopMemberCardDao.getShopMemberCardList(searchCondition);
		if (CollectionUtils.isEmpty(shopMemberCardList)) {
		    // 会员卡不够支付异常
            throw new ValidateException(CodeConst.CODE_MEMBER_CARD_NOT_EXIST,
                    CodeConst.MSG_MEMBER_CARD_NOT_EXIST);
		} 
		ShopMemberCardDto useMemberCard = shopMemberCardList.get(0);
        if (useMemberCard.getAmount() != null && useMemberCard.getAmount().doubleValue() >= shopMemberCardDto
                .getConsumeMoney().doubleValue()) {// 会员卡足够支付
            shopMemberCardDto.setCardId(useMemberCard.getCardId());
            shopMemberCardDto.setUserId(useMemberCard.getUserId());
        } else {
            // 会员卡不够支付异常
            throw new ValidateException(CodeConst.CODE_MEMBER_CARD_NOT_ENOUGH,
                    CodeConst.MSG_MEMBER_CARD_NOT_ENOUGH);
        }
        Double afterAmount = NumberUtil.sub(useMemberCard.getAmount(), shopMemberCardDto.getConsumeMoney());
		shopMemberCardDao.consumeShopMemberCard(shopMemberCardDto);// 将账户金额改变
		ShopMemberCardBillDto shopMemberCardBillDto = new ShopMemberCardBillDto();// 生成充值账单
		shopMemberCardBillDto.setShopId(shopMemberCardDto.getShopId().intValue());
		shopMemberCardBillDto.setBillAmount(shopMemberCardDto.getConsumeMoney());// 账单金额为充值金额+赠送金额
		shopMemberCardBillDto.setAfterAmount(afterAmount);
		shopMemberCardBillDto.setChargeType(CommonConst.CARD_CHARGE_TYPE_MONEY);
		shopMemberCardBillDto.setCardId(shopMemberCardDto.getCardId());
		shopMemberCardBillDto.setCardBillType(CommonConst.CARD_BILL_TYPE_CONSUME);// 类型为消费
		shopMemberCardBillDto.setOrderId(orderId);
		shopMemberCardBillDao.insertShopMemberCardBill(shopMemberCardBillDto);// 插入用户账单
		return afterAmount;
		
	}

	/**
	 * 修改店铺会员卡 @Title: updateShopMemberCard @param @param
	 * shopMemberCardDto @throws
	 */
	public void updateShopMemberCard(ShopMemberCardDto shopMemberCardDto) throws Exception {
		ShopMemberCardDto searchCondition = new ShopMemberCardDto();
		searchCondition.setMobile(shopMemberCardDto.getMobile());
		searchCondition.setShopId(shopMemberCardDto.getShopId());
		searchCondition.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);
		List<ShopMemberCardDto> shopMemberCardList = shopMemberCardDao.getShopMemberCardList(searchCondition);
		if (shopMemberCardList.size() > 0) {
			ShopMemberCardDto useMemberCard = shopMemberCardList.get(0);
			shopMemberCardDto.setCardId(useMemberCard.getCardId());
			shopMemberCardDao.updateShopMemberCard(shopMemberCardDto);
		}
		Integer cardId=shopMemberCardDto.getCardId();
		// 如果是次卡，就添加 次卡限定商品。否则就不添加
		if (shopMemberCardDto.getCardType() != null && shopMemberCardDto.getCardType().equals("2")) {
			String goodsSetId = shopMemberCardDto.getGoodsSetId();// 套餐id
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopId", shopMemberCardDto.getShopId());
			map.put("goodsId", goodsSetId);
			List<GoodsSetDto> goodsSetsList = goodsSetDao.getGoodsSetList(map);// 该套餐下所有的服务
			CommonValidUtil.validListNull(goodsSetsList,CodeConst.CODE_PARAMETER_NOT_NULL,"该套餐不存在");
			List<ShopMemberCardGoods> goodsList = new ArrayList<ShopMemberCardGoods>();
			for (GoodsSetDto goodsSetDto : goodsSetsList) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("goodsId", goodsSetDto.getGoodsId());
				param.put("cardId", cardId);
				ShopMemberCardGoods cardGoods=shopMemberCardGoodsDao.getMemberCardGoods(param);
				//如果不存在， 就添加，否则会出错
				if(cardGoods==null){
					ShopMemberCardGoods shopMemberCardGoods = new ShopMemberCardGoods();
					shopMemberCardGoods.setCardId(Long.valueOf(cardId));
					shopMemberCardGoods.setGoodsId(goodsSetDto.getGoodsId());
					shopMemberCardGoods.setGoodsName(goodsSetDto.getGoodsName());
					shopMemberCardGoods.setPrice(goodsSetDto.getPrice());
					shopMemberCardGoods.setGoodsNumber(goodsSetDto.getGoodsNumber().intValue());
					goodsList.add(shopMemberCardGoods);
				}
			}
			if(goodsList.size()>0){
				shopMemberCardGoodsDao.batchInsertShopMemberCardGoods(goodsList);
				ShopMemberCardBillDto shopMemberCardBillDto = new ShopMemberCardBillDto();
				shopMemberCardBillDto.setCardId(cardId);
				shopMemberCardBillDto.setCardBillType(CommonConst.CARD_TIME_CARD_TYPE);// 购买次卡
				GoodsDto goodsDto = goodsDao.getGoodsById(Long.valueOf(shopMemberCardDto.getGoodsSetId()));// 次卡信息
				shopMemberCardBillDto.setChargeMoney(goodsDto.getStandardPrice());// 账单金额=购买次卡的金额
				shopMemberCardBillDto.setChargeType(CommonConst.CARD_CHARGE_TYPE_MONEY);
				shopMemberCardBillDto.setShopId(shopMemberCardDto.getShopId().intValue());
				shopMemberCardBillDto.setBillTime(new Date());
				shopMemberCardBillDto.setPresentMoney(shopMemberCardDto.getPresentMoney());// 赠送金额
				shopMemberCardBillDto.setAfterAmount(shopMemberCardBillDto.getAfterAmount());//
				shopMemberCardBillDao.insertShopMemberCardBill(shopMemberCardBillDto);
			}
		}
	}
	
	/**
	 * 店铺次卡消费
	 * @Title: shopTimeCardComsume 
	 * @param @param shopCardList
	 * @param @throws Exception  
	 * @throws
	 */
	public void shopTimeCardComsume(List<ShopMemberCardDto> shopCardList)
			throws Exception {
		List<ShopMemberCardBillDto>shopMemberCardBillDtoList=new ArrayList<ShopMemberCardBillDto>();
		for(ShopMemberCardDto shopMemberCardDto:shopCardList){
			ShopMemberCardBillDto shopMemberCardBillDto=new ShopMemberCardBillDto();//生成充值账单
			shopMemberCardBillDto.setShopId(shopMemberCardDto.getShopId().intValue());
			shopMemberCardBillDto.setChargeType(CommonConst.CARD_CHARGE_TYPE_MONEY);
			shopMemberCardBillDto.setCardId(shopMemberCardDto.getCardId());
			shopMemberCardBillDto.setCardBillType(CommonConst.CARD_BILL_TYPE_CONSUME);//类型为次卡购买
			shopMemberCardBillDto.setOrderId(shopMemberCardDto.getOrderId());
			shopMemberCardBillDto.setBillTime(new Date());
			shopMemberCardBillDtoList.add(shopMemberCardBillDto);
		}
		shopMemberCardBillDao.batchInsertShopMemberCardBill(shopMemberCardBillDtoList);//插入用户账单
		shopMemberCardGoodsDao.batchUseTimesCard(shopCardList);
	}
}
