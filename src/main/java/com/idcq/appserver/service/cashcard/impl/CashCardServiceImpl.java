package com.idcq.appserver.service.cashcard.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.RechargeEnum;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.cashcard.ICashCardBatchDao;
import com.idcq.appserver.dao.cashcard.ICashCardBatchLogDao;
import com.idcq.appserver.dao.cashcard.ICashCardDao;
import com.idcq.appserver.dao.cashcard.ICashCardUseLogDao;
import com.idcq.appserver.dao.membercard.IUserMemberBillDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.cashcard.CashCardBatchDto;
import com.idcq.appserver.dto.cashcard.CashCardBatchLogDto;
import com.idcq.appserver.dto.cashcard.CashCardDto;
import com.idcq.appserver.dto.cashcard.CashCardUseLogDto;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcard.ICashCardService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.mq.MqProduceApi;

@Service("generateCashCard")
public class CashCardServiceImpl implements ICashCardService {

    private static final Logger logger = Logger.getLogger(CashCardServiceImpl.class);
    @Autowired
    private ICashCardBatchDao cashCardBatchDao;
    
    @Autowired
    private ICashCardBatchLogDao cashCardBatchLogDao;
    
    @Autowired
    private ICashCardDao cashCardDao;
    
    @Autowired
    private ICashCardUseLogDao cashCardUseLogDao;
    
    @Autowired
    private IUserMemberBillDao userMemberBillDao;
    
    @Autowired
    private IUserAccountDao userAccountDao;
    
    @Autowired
    private IShopAccountDao shopAccountDao;
    
    @Autowired
    private IShopBillDao shopBillDao;
    
    @Autowired
    private IShopDao shopDao;
    
    @Autowired
    private IPushService pushService;
    
    @Autowired
    private IUserDao userDao;
	
	@Override
	public CashCardBatchDto getCashCardBatchBybatchIdFromDb(Long cashCardBatchId) throws Exception {
		return cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
	}
	
	@Override
	public void checkCashCardBatchvalidity(Long cashCardBatchId, Boolean asynchronous) throws Exception {
		CashCardBatchDto cashCashCardBatch = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
		if (cashCashCardBatch == null)
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该充值卡批次不存在");
		
		Integer buildStatus = cashCashCardBatch.getBuildStatus();
		if (asynchronous == false && buildStatus == CommonConst.CASH_CARD_BATCH_BUILD_STATUS_GENERATEING) 
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该充值卡批次正在生成");
		
		if (buildStatus == CommonConst.CASH_CARD_BATCH_BUILD_STATUS_GENERATEED)
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该充值卡批次已经生成");
	}
	
	@Override
	public void generateCashCardAsynchronously(Map<String, Object> requestMap) throws Exception{
		Long cashCardBatchId = (Long)requestMap.get("cashCardBatchId");
		Integer operaterId = (Integer)requestMap.get("operaterId");
		updateCashCardBatchBeforeGenerate(cashCardBatchId);
		pushMessageForGenerateCashCardAsynchronously(cashCardBatchId, operaterId);
	}
	
	@Override
	public Runnable createAsynchronousTask(final Map<String, Object> asynchronousTaskRequestMap) {
		return new Runnable() {
			@Override
			public void run() {
				if (asynchronousTaskRequestMap == null || asynchronousTaskRequestMap.size() == 0) {
					logger.error("异步批次生成充值卡请求参数为空");
					return;
				}
				Long cashCardBatchId = Long.valueOf(asynchronousTaskRequestMap.get("id").toString());
				Integer operaterId = Integer.valueOf(asynchronousTaskRequestMap.get("userId").toString());
				try {
					checkCashCardBatchvalidity(cashCardBatchId,true);
					generateCashCard(cashCardBatchId, operaterId);
				} catch (Exception e) {
					logger.error("异步批次生成充值卡失败："+e.getMessage()+
							" cashCardBatchId:"+cashCardBatchId+" operaterId:"+operaterId,e);
					e.printStackTrace();
				}
				
			}
		};
	}
	private void pushMessageForGenerateCashCardAsynchronously(Long cashCardBatchId,Integer operaterId) throws Exception {
		String topic = "AsynchronousTask";
		String tag = "generateCashCard";
		Map<String, Object> messageContent = new HashMap<String, Object>();
		messageContent.put("id", cashCardBatchId);
		messageContent.put("userId",operaterId);
		try {
			MqProduceApi.setMessage(topic, tag, JacksonUtil.object2Json(messageContent));
		} catch (Exception e) {
			logger.error("推送异步批次生成充值卡消息失败", e);
			throw new ValidateException(CodeConst.CODE_PUSH_FAIL, "推送异步批次生成充值卡消息失败");
		}
	}
	private void updateCashCardBatchBeforeGenerate(Long cashCardBatchId) throws Exception {
		CashCardBatchDto cashCardBatch = new CashCardBatchDto();
		cashCardBatch.setCashCardBatchId(cashCardBatchId);
		cashCardBatch.setBuildStatus(CommonConst.CASH_CARD_BATCH_BUILD_STATUS_GENERATEING);
		try {
			cashCardBatchDao.updateCashCardBatch(cashCardBatch);
		} catch (Exception e) {
			logger.error("异步批次生成充值卡前  更新批次状态为正在生成错误", e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, "异步批次生成充值卡前  更新批次状态为正在生成错误");
		}
	}
	@Override
	public void generateCashCard(Long cashCardBatchId,Integer operaterId) throws Exception {
		CashCardBatchDto cashCardBatch = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
		Double money = cashCardBatch.getMoney();
		Integer cardNum = cashCardBatch.getCardNum();
		if (cardNum <= 0 || money <= 0)
			return;
		String batchNo = generateUniqueBatchNo();
		List<CashCardDto> cashCardList = getCashCards(cashCardBatchId,cardNum,batchNo);
		if (cashCardList.size() == 0)
			return;
		cashCardDao.batchInsertCashCard(cashCardList);
		cashCardBatch.setBatchNo(batchNo);
		cashCardBatch.setBuildStatus(CommonConst.CASH_CARD_BATCH_BUILD_STATUS_GENERATEED);
		cashCardBatchDao.updateCashCardBatch(cashCardBatch);
		
		insertCashCardBatchLog(cashCardBatchId, operaterId);
	}
	
	private void insertCashCardBatchLog(Long cashCardBatchId,Integer operaterId) throws Exception {
		CashCardBatchLogDto batchLog = new CashCardBatchLogDto();
		batchLog.setCashCardBatchId(cashCardBatchId);
		batchLog.setOperateType(1);
		batchLog.setOpertaterId(operaterId);
		batchLog.setOperateTime(new Date());
		batchLog.setLogDesc("生成充值卡");
		cashCardBatchLogDao.insertCashCardBatchLog(batchLog);
	}
	private List<CashCardDto> getCashCards(Long cashCardBatchId,Integer cardNum,String batchNo) throws Exception {
		List<CashCardDto> cashCardList = new ArrayList<CashCardDto>();
		Set<String> generatedCashCardNoSet = new HashSet<String>();
		Date now = new Date();
		for (Integer index=0; index < cardNum; index++) {
			String cashCardNo = null;
			for (;;) {
				cashCardNo = generateCashCardNo(batchNo,getCashCardOffset(index.toString()));
				if (generatedCashCardNoSet.contains(cashCardNo)){
					continue;
				}
				else {
					generatedCashCardNoSet.add(cashCardNo);
					break;
				}
			}
			String passWord = getRandomPassword();
			CashCardDto cashCard = new CashCardDto();
			cashCard.setCashCardNo(cashCardNo);
			cashCard.setCashCardBatchId(cashCardBatchId);
			cashCard.setCardPassword(passWord);
			cashCard.setCardStatus(1);
			cashCard.setCardIndex(index.longValue()+1);
			cashCard.setCreateTime(now);
			cashCardList.add(cashCard);
		}
		generatedCashCardNoSet = null;
		return cashCardList;
	}
	private String getCashCardOffset(String index) {
		StringBuilder cashCardOffset = new StringBuilder();
		if (index.length() == 1)
			cashCardOffset.append("000").append(index);
		else if (index.length() == 2)
			cashCardOffset.append("00").append(index);
		else if (index.length() == 3)
			cashCardOffset.append("0").append(index);
		else if (index.length() == 4) 
			cashCardOffset.append(index);
		else {
			Integer offset = Integer.valueOf(index.toString()) - Integer.valueOf("10000");
			cashCardOffset.append(getCashCardOffset(offset.toString()));
		}
		return cashCardOffset.toString();
	}
	private String generateCashCardNo(String batchNo,String cashCardOffset) {
		Random random = new Random();
		StringBuilder cashCardNo = new StringBuilder();
		cashCardNo.append(batchNo.charAt(0));
		cashCardNo.append(getRandomNum(random,false));
		cashCardNo.append(batchNo.charAt(1));
		cashCardNo.append(getRandomNum(random,false));
		cashCardNo.append(getRandomNum(random,false));
		cashCardNo.append(batchNo.charAt(2));
		cashCardNo.append(getRandomNum(random,false));
		cashCardNo.append(batchNo.charAt(3));
		cashCardNo.append(cashCardOffset);
		return cashCardNo.toString();
	}
	
	private String generateUniqueBatchNo() throws Exception{
		for (;;) {
			String batchNo = generateBatchNo();
			Boolean isDuplicate = cashCardBatchDao.batchNOIsDuplicate(batchNo);
			if (isDuplicate == false)
				return batchNo;
		}
	}
	private String generateBatchNo() {
		Random random = new Random();
		StringBuilder batchNo = new StringBuilder();
		batchNo.append(getRandomNum(random,true));
		for (int i=0; i < 3; i++) {
			batchNo.append(getRandomNum(random,false));
		}
		return batchNo.toString();
	}
	
	private Integer getRandomNum(Random random,Boolean isFirst){
		if (isFirst)
			return 1+random.nextInt(9);
		else
			return random.nextInt(10);
	}
	
	private String getRandomPassword() throws Exception {
		Random random = new Random();
		StringBuilder passwordBuilder = new StringBuilder();
		passwordBuilder.append(getRandomNum(random,true));
		for (int i=0; i < 7; i++) {
			passwordBuilder.append(getRandomNum(random,false));
		}
		String password = passwordBuilder.toString();
		return AESUtil.aesEncrypt(password, CommonConst.CASHCARD_PASSWORD_KEY);
	}
	/* (non-Javadoc)
     * @see com.idcq.appserver.service.cashcard.ICashCardService#useCashCard(java.util.Map)
     */
    @Override
    public Map<String, Object> useCashCard(Map<String, Object> pMap) throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CashCardDto cashCardDB = cashCardDao.queryCashCard(pMap);
        // 验证卡号密码卡号密码
        if (cashCardDB == null)
        {
            throw new ValidateException(CodeConst.CODE_CARD_OR_PASSWORD_ERROR, CodeConst.MSG_CARD_OR_PASSWORD_ERROR);
        }
        else
        {
            
            Integer status = cashCardDB.getCardStatus();
            // 验证充值卡是否已经使用
            if (CommonConst.CARD_STATUS_LOSE_EFFICACY == status)
            {
                throw new ValidateException(CodeConst.CODE_CARD_IS_USE, CodeConst.MSG_CODE_CARD_IS_USE);
            }
            
            
            //验证批次
            CashCardBatchDto CashCardBatchDB = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardDB.getCashCardBatchId());
            if(CashCardBatchDB!=null){
                //批次状态
                Integer batchStatus = CashCardBatchDB.getBatchStatus();
                //批次无效
                if (CommonConst.CARD_BATCH_STATUS_LOSE_EFFICACY == batchStatus)
                {
                    throw new ValidateException(CodeConst.CODE_CODE_CARD_BATCH_STATUS_ERROR, CodeConst.MSG_CODE_CARD_BATCH_STATUS_ERROR);
                }
                
                //验证充值卡批次时间范围
                Date beginTime = CashCardBatchDB.getBeginTime();
                Date endTime = CashCardBatchDB.getEndTime();
                //当前时间大于批次开始时间、结束时间小于批次结束时间
                if(beginTime.compareTo(new Date())>0 
                        || endTime.compareTo(new Date())<0){
                    throw new ValidateException(CodeConst.CODE_CODE_CARD_BATCH_DATE_ERROR, CodeConst.MSG_CODE_CARD_BATCH_DATE_ERROR);
                }
            }
        }
        // 获取CashCardUseLogDto部分数据
        CashCardUseLogDto cardUseLogDto = getCashCardUseLogDto(pMap);
        // 充值类型
        Integer accountType = (Integer) pMap.get("accountType");
        Long accountId = cardUseLogDto.getAccountId();

        // 下面处理账务逻辑

        if (2 == accountType)
        {// 商铺充值
            resultMap = shopcharge(cashCardDB, accountId);
        }
        else
        {// 用户充值
            resultMap = usercharge(cashCardDB, accountId);
        }

        String cashCardNo = (String) pMap.get("cashCardNo");
        // 更新充值卡状态
        cashCardDao.updateCardStatus(cashCardNo, 0);

        String remark = (String) pMap.get("remark");
        Long operaterId = (Long) pMap.get("operaterId");
        Integer fromSystem = (Integer) pMap.get("fromSystem");

        // 插入充值卡使用记录
        cardUseLogDto.setAccountId(accountId);
        cardUseLogDto.setAccountType(accountType);
        cardUseLogDto.setCashCardBatchId(cashCardDB.getCashCardBatchId());
        cardUseLogDto.setCashCardNo(cashCardNo);
        cardUseLogDto.setFromSystem(fromSystem);
        cardUseLogDto.setOpertaterId(operaterId);
        cardUseLogDto.setUseTime(new Date());
        cardUseLogDto.setLogDesc(remark);
        
        
        //操作人ID。来源系统收银机、后台时，shopId，其他的userId。
        if(fromSystem!=1 
                ||  fromSystem!=6){
            ShopDto shop = shopDao.getShopById(operaterId);
            if(shop!=null){
                cardUseLogDto.setOpertaterName(shop.getShopName()); 
            } 
        }
        else
        {
            //操作人名字，可以存放手机号码，真实名字
            UserDto operater = userDao.getUserById(operaterId);
            if(operater!=null){
                String trueName = operater.getTrueName();
                if(StringUtils.isNotBlank(trueName)){
                    cardUseLogDto.setOpertaterName(trueName); 
                }
                else{
                    cardUseLogDto.setOpertaterName(operater.getMobile()); 
                }
            }
            else{
                throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
            }
        }
        
        //插入充值卡使用记录
        cashCardUseLogDao.insertCashCardUseLog(cardUseLogDto);
        
        resultMap.put("chargeType", cardUseLogDto.getChargeType());
        resultMap.put("accountName", cardUseLogDto.getAccountName());
        resultMap.put("accounMobile", cardUseLogDto.getAccountMobile());
        return resultMap;
    }
    /**
     * 获取bizid
     * 获取用户
     * @throws Exception 
     */
    private CashCardUseLogDto getCashCardUseLogDto(Map<String, Object> pMap) throws Exception
    {
        CashCardUseLogDto cardUseLogDto = new CashCardUseLogDto();
        Long bizId = (Long) pMap.get("bizId");
        Integer accountType = (Integer) pMap.get("accountType");
        String mobile = (String) pMap.get("mobile");
        if (accountType == 2)
        {// 商铺账户
         // 传了商铺id
            if (bizId != null)
            {
                ShopDto shop = shopDao.getShopById(bizId);
                if (shop == null)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                }
                cardUseLogDto.setAccountId(shop.getShopId());
                cardUseLogDto.setAccountName(shop.getShopName());
                //查询user
                UserDto user = userDao.getUserById(shop.getPrincipalId());
                if (user == null)
                {
                    throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
                }
                cardUseLogDto.setAccountMobile(user.getMobile());
                cardUseLogDto.setChargeType(2);//商铺充值类型
            }
            else
            {// 没传id,只传手机号码
                UserDto user = userDao.getUserByMobile(mobile);
                if (user == null)
                {
                    throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
                }
                ShopDto shop = shopDao.getShopByPrincipalId(user.getUserId());
                if (shop == null)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                }
                cardUseLogDto.setAccountId(shop.getShopId());
                cardUseLogDto.setAccountName(shop.getShopName());
                cardUseLogDto.setAccountMobile(mobile);
                cardUseLogDto.setChargeType(2);//商铺充值类型
            }

        }
        else
        {// 用户账户
         // 传了用户id
            if (bizId != null)
            {
                UserDto user = userDao.getUserById(bizId);
                if (user == null)
                {
                    throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
                }
                cardUseLogDto.setAccountId(user.getUserId());
                cardUseLogDto.setAccountName(user.getTrueName());
                cardUseLogDto.setAccountMobile(user.getMobile());
                cardUseLogDto.setChargeType(1);//用户充值类型
            }
            else
            {// 传了手机
                UserDto user = userDao.getUserByMobile(mobile);
                if (user == null)
                {
                    throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);
                }
                cardUseLogDto.setAccountId(user.getUserId());
                cardUseLogDto.setAccountName(user.getTrueName());
                cardUseLogDto.setAccountMobile(user.getMobile());
                cardUseLogDto.setChargeType(1);//用户充值类型
            }
        }
        return cardUseLogDto;
    }
    /**
     * 会员卡用户充值
     * @param cashCardDB 
     * @param bizId 用户id或商铺id
     */
    private Map<String, Object> usercharge(CashCardDto cashCardDB,Long bizId) throws Exception{
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //批次id
        Long cashCardBatchId  = cashCardDB.getCashCardBatchId();
        //查询会员卡批次
        CashCardBatchDto cashCardBatchDB = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
        if(cashCardBatchId==null){
            throw new ValidateException(CodeConst.CODE_CASH_CARD_BATCH_NOT_EXIST,CodeConst.MSG_CASH_CARD_BATCH_NOT_EXIST);
        }
        //充值金额
       double money = cashCardBatchDB.getMoney();
       //TODO 1、增加userbill  2、更新账号余额
       UserChargeDto userCharge = new UserChargeDto();
       userCharge.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
       userCharge.setBillDesc(CommonConst.MEMBER_CHARGE);
       userCharge.setOrderId(cashCardDB.getCashCardNo());
       userCharge.setAmount(money);
       userCharge.setBillDirection(1);//充值代表资金增加
       userCharge.setUserRole(CommonConst.MEMBER);
       userCharge.setCreateTime(new Date());
       userCharge.setBillType(CommonConst.CHARGE);
       userCharge.setUserId(bizId);
       userCharge.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
       userCharge.setBillTitle("充值"+money+"元");
       userCharge.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
       userCharge.setUserBillType(CommonConst.USER_BILL_TYPE_CASH_CARD);
       
       //用户账户
       UserAccountDto userAccount = userAccountDao.getAccountMoney(bizId);
       if(userAccount==null){
           throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST,CodeConst.MSG_ACCOUNT_NOT_EXIST);

       }
       // 充值前余额
       Double accountAmount =userAccount.getAmount();
       Double couponAmount = userAccount.getCouponAmount();
       //充值后余额
       Double accountMoney = NumberUtil.add(couponAmount, money);
       userCharge.setAccountAmount(accountAmount);
       userCharge.setAccountAfterAmount(accountMoney);

       userMemberBillDao.saveChargeBill(userCharge);
       
       // 更新传奇宝账号余额
       userAccountDao.updateUserAccount(bizId, money, null, null, money, null,null,null,null,null,null,null,null,null,null);
       
       resultMap.put("billId", userCharge.getBillId());
       resultMap.put("money", NumberUtil.formatDouble(money,2));
       resultMap.put("afterAmount", NumberUtil.formatDouble(accountMoney, 2));
       resultMap.put("afterAllAmount", NumberUtil.formatDouble(NumberUtil.add(accountMoney, userAccount.getRewardAmount()), 2));
       resultMap.put("accountId", userAccount.getAccountId());
       return resultMap;
    }
    /**
     * 商铺充值
     * @param cashCardDB
     * @param bizId
     */
    public Map<String, Object> shopcharge(CashCardDto cashCardDB,Long bizId)throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //批次id
        Long cashCardBatchId  = cashCardDB.getCashCardBatchId();
        //查询会员卡批次
        CashCardBatchDto cashCardBatchDB = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
        if(cashCardBatchId==null){
            throw new ValidateException(CodeConst.CODE_CASH_CARD_BATCH_NOT_EXIST,CodeConst.MSG_CASH_CARD_BATCH_NOT_EXIST);
        }
        // 更新商铺账户金额
        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(bizId);

        if (shopAccountDto == null)
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账号不存在，请检查1dcq_shop_account表是否存在shopId="
                    + bizId);

        // 若充值后账户余额大于0，须更新商铺状态 商家状态:审核中-99,正常-0,下线-1,删除-2,欠费-3 
        //防止缓存有问题，直接查询数据库
        ShopDto shopDto = shopDao.getShopEssentialInfo(bizId);
        if (null == shopDto)
        {
            logger.error("shopDao.getShopEssentialInfo()查询商铺不存在");
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        
        // 更新传奇宝账号余额, 防止账号余额为null
        Double amount = shopAccountDto.getAmount();
        if (amount == null){
            amount = 0d;
        }
        Double accountAfterAmountBefore = shopAccountDto.getDepositAmount();
        double money = cashCardBatchDB.getMoney();
        Double accountAfterAmount = NumberUtil.add(accountAfterAmountBefore, money);
        Integer arrearsFlag = null;
        if(accountAfterAmount >= 0)
        {
            arrearsFlag = CommonConst.ARREARS_FLAG_FALSE;
        }
        shopAccountDao.updateShopAccount(bizId, money, null, null, null, money, null, arrearsFlag,null,null, null);
        
//        afterAmount
//        afterAllAmount
        // 插入商铺流水
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setBillStatus(2); // 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setShopId(bizId);
        shopBillDto.setMoney(money);
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_CASH_CARDR);
        shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);//1代表增加
        shopBillDto.setAccountAmount(amount);
        shopBillDto.setBillDesc("充值卡充值充值账单记录");
        shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT);//保证金
        shopBillDto.setAccountAfterAmount(accountAfterAmount);
        shopBillDto.setBillTitle("充值"+NumberUtil.formatDoubleStr(money, 2));
        shopBillDto.setOrderId(cashCardDB.getCashCardNo());
        shopBillDao.insertShopBill(shopBillDto);
        
        //充值后余额。
        //商铺包括：线上营业收入+平台奖励+保证金
        Double afterAllAmount = NumberUtil.add(shopAccountDto.getRewardAmount(), accountAfterAmount);
        resultMap.put("billId", shopBillDto.getBillId());
        resultMap.put("money", NumberUtil.formatDouble(money, 2));
        resultMap.put("afterAmount", NumberUtil.formatDouble(accountAfterAmount, 2));
        resultMap.put("afterAllAmount", NumberUtil.formatDouble(afterAllAmount, 2));
        resultMap.put("accountId", shopAccountDto.getShopAccountId());

        int status = shopDto.getShopStatus();
        logger.info("更新前商铺状态："+status+",充值后保证金余额："+accountAfterAmount);
        if (shopDto != null && status == CommonConst.SHOP_LACK_MONEY_STATUS
                && accountAfterAmount >= 0)
        {
            shopDto.setShopStatus(CommonConst.SHOP_NORMAL_STATUS);
            shopDao.updateShopStatus(shopDto);
            // 店铺状态有修改，清空缓存
            DataCacheApi.del(CommonConst.KEY_SHOP + bizId);
            try
            {
                logger.info("推送消息到收银机start");
                // 推送消息到收银机
                JSONObject pushTarget = new JSONObject();
                pushTarget.put("action", CommonConst.ACTION_SHOP_DATA_UPDATE);
                pushTarget.put("shopId", bizId);
                pushTarget.put("lastUpdate", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                PushDto push = new PushDto();
                push.setShopId(bizId);
                push.setAction(CommonConst.ACTION_SHOP_DATA_UPDATE);
                pushTarget.put("shopStatus", CommonConst.SHOP_NORMAL_STATUS);// 正常状态
                push.setContent(pushTarget.toString());
                logger.info("商铺id：" + bizId);
                pushService.pushInfoToShop2(push);
            }
            catch (Exception e)
            {
                logger.error("推送消息到收银机失败" + "，商铺id：" + bizId);
            }
        }
        return resultMap;
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.cashcard.ICashCardService#downloadCashCard(java.util.Map)
     */
    @Override
    public Map<String, Object> downloadCashCard(Map<String, Object> pMap) throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Long cashCardBatchId = (Long) pMap.get("cashCardBatchId");
        CashCardBatchDto  cashCardBatchDto  = cashCardBatchDao.getCashCardBatchBybatchIdFromDb(cashCardBatchId);
        //验证充值卡是否生成
        if(cashCardBatchDto==null){
            throw new ValidateException(CodeConst.CODE_CARD_NOT_IS_EXIST, CodeConst.MSG_CARD_NOT_IS_EXIST);
        }
        //充值卡已使用，不允许导出
        Integer count = cashCardBatchLogDao.queryCashBardBatchIsExist(cashCardBatchId);
        //充值卡已使用，不允许导出
        if(count!=null && count>0){
            throw new ValidateException(CodeConst.CODE_CODE_CARD_BATCH_IS_USE, CodeConst.MSG_CODE_CARD_BATCH_IS_USE);
        }
        List<CashCardDto> cashCardList  = cashCardDao.getCashCardByBatchId(cashCardBatchId);
        StringBuffer fileContent = new StringBuffer();
        String fileName = cashCardBatchDto.getCardName() + "_" + cashCardBatchDto.getMoney()
                +"_"+DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
        /*
         * String 是 文件名称。 1)文件名格式：卡名称+”_”+面额+”_”+导出日期 2)文件扩展名CVS。
         * fileContent String 是
         * 文件内容字串。按CVS格式组织。内容包括：卡号、密码明文、面额、有效期起止日期。第一行标题
         * ，从第二行开始数据。按卡次序号排序。
         * 与php约定，每个属性间“，”隔开，每行“；”分割
         */
        //第一行标题
        fileContent.append("卡号");
        fileContent.append(",");
        fileContent.append("密码");
        fileContent.append(",");
        fileContent.append("面额");
        fileContent.append(",");
        fileContent.append("开始日期");
        fileContent.append(",");
        fileContent.append("截止日期");
        fileContent.append(";");
        if(CollectionUtils.isNotEmpty(cashCardList)){
            for (CashCardDto cashCard : cashCardList)
            {
                fileContent.append(cashCard.getCashCardNo());
                fileContent.append(",");
                fileContent.append(AESUtil.aesDecrypt(cashCard.getCardPassword(), CommonConst.CASHCARD_PASSWORD_KEY));
                fileContent.append(",");
                fileContent.append(cashCardBatchDto.getMoney());
                fileContent.append(",");
                fileContent.append(DateUtils.format(cashCardBatchDto.getBeginTime(), DateUtils.DATE_FORMAT));
                fileContent.append(",");
                fileContent.append( DateUtils.format(cashCardBatchDto.getEndTime(), DateUtils.DATE_FORMAT));
                fileContent.append(";");
            }
            
        }

        resultMap.put("fileName", fileName);
        resultMap.put("fileContent", fileContent);
        
        return resultMap;
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.cashcard.ICashCardService#getCashCardUsed(java.util.Map)
     */
    @Override
    public Map<String, Object> getCashCardUsed(Map<String, Object> pMap) throws Exception
    {
        Map<String, Object>  resultMap = new    HashMap<String, Object>();
        
        Integer accountType = (Integer) pMap.get("accountType");
        if(accountType==1){//用户
            resultMap = cashCardUseLogDao.getCashCardUsedByUser(pMap);
        }
        else{
            resultMap = cashCardUseLogDao.getCashCardUsedByShop(pMap);
        }
        //"afterAmount":957, "fromSystem":3, "operaterId":1, "money":500
        if(resultMap!=null){
            BigDecimal afterAmount  = (BigDecimal) resultMap.get("afterAmount");
            BigDecimal money  = (BigDecimal) resultMap.get("money");
            //金额保留小数点两位
            if(afterAmount!=null){
                resultMap.put("afterAmount", NumberUtil.formatDouble(afterAmount.doubleValue(),2));
            }
            if(money!=null){
                resultMap.put("money", NumberUtil.formatDouble(money.doubleValue(),2));
            }
        }
        return resultMap;
    }
}
