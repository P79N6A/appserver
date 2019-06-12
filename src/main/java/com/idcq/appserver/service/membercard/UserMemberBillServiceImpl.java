package com.idcq.appserver.service.membercard;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.RechargeEnum;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.membercard.IUserMemberBillDao;
import com.idcq.appserver.dao.membercard.IUserMemberCardDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.membercard.UserMemberCardDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;

@Service
public class UserMemberBillServiceImpl implements IUserMemberBillService {
	private final Logger logger = Logger.getLogger(UserMemberBillServiceImpl.class);
	@Autowired
	private IUserMemberBillDao userBillDao;
	@Autowired
	public IUserDao userDao;
	@Autowired
	private IUserMemberCardDao userMemberCardDao;
	@Autowired
	public ITransaction3rdDao iTransaction3rdDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	IUserAccountDao userAccountDao;
	
	@Autowired
	private IShopBillDao shopBillDao;
	
	/**
	 * 查询用户会员卡账单接口
	 */
	public PageModel getPageUserBill(Integer pageNo, Integer pageSize,
			Map<String, Object> map) throws Exception {
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		page.setTotalItem(userBillDao.getCountUserBill(map));
		page.setList(userBillDao.getListUserBill(pageNo, pageSize, map));
		
		return page;
	}

	public Map<String, Object> saveCharge(HttpServletRequest request) throws Exception {
		
		//获取参数
		String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
		String chargeType = RequestUtils.getQueryParam(request, "chargeType");
		String umcIdStr = RequestUtils.getQueryParam(request, "umcId");
		String amountStr = RequestUtils.getQueryParam(request, "amount");
		String terminalType = RequestUtils.getQueryParam(request, "terminalType");
		String thirdOrgName = RequestUtils.getQueryParam(request, "3rdOrgName");
		
		//校验参数
		CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
		CommonValidUtil.validStrNull(amountStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_AMOUNT);
		CommonValidUtil.validStrNull(terminalType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_TERMINAL_TYPE_IS_NULL);
		CommonValidUtil.validStrNull(thirdOrgName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_3RD_TRANSACTIONID_IS_NULL);
		
		Long userId = NumberUtil.strToLong(userIdStr, CommonConst.USER_ID);
		Double amount = NumberUtil.strToDouble(amountStr, "amount");
		
		Long accountId = null;
		//验证用户的存在性
		UserDto userDB = this.userDao.getUserById(userId);
		CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST,  CodeConst.MSG_MISS_MEMBER);	
		
		Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
		
		if(("2").equals(chargeType)){
			CommonValidUtil.validStrNull(umcIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_ACCOUNT_NULL);
			accountId = NumberUtil.strToLong(umcIdStr, "umcId");
			UserMemberCardDto userCard = userMemberCardDao.getMemberCardInfoById(accountId);
			CommonValidUtil.validObjectNull(userCard, CodeConst.CODE_PARAMETER_NOT_EXIST,  CodeConst.MSG_ACCOUNT_NOT_EXIST);		
			//充值类型 代表充值到会员卡
			transaction3rdDto.setTransactionType(2);
		} else {
			//默认为传奇宝
			chargeType = "1";

			//充值到传奇宝需要验证传奇宝账号是否被冻结 --4、16变更
			UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
			if (userAccount.getAccountStatus() == 0) {
				throw new APIBusinessException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301,  CodeConst.MSG_USER_ACCOUNT_FROZEN);
			}
			//充值类型 充值到传奇宝
			transaction3rdDto.setTransactionType(1);
		}
		
		transaction3rdDto.setUserId(userId);
	
		//代表待反馈支付进度（未支付）
        transaction3rdDto.setStatus(0);
        transaction3rdDto.setTerminalType(terminalType);
        transaction3rdDto.setRdOrgName(thirdOrgName);
        transaction3rdDto.setPayAmount(amount);
        transaction3rdDto.setTransactionTime(new Date());

        iTransaction3rdDao.payBy3rd(transaction3rdDto);
        
        Long transactionId = transaction3rdDto.getTransactionId();
        UserChargeDto userCharge = new UserChargeDto();
        userCharge.setUserId(userId);
        userCharge.setAmount(amount);
        userCharge.setTerminalType(terminalType);
        userCharge.setThirdOrgName(thirdOrgName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("transactionId", FieldGenerateUtil.generatebitOrderId(transactionId));
        
        // 推送信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("chargeType", chargeType);
        if (("2").equals(chargeType))
        {
            jsonObject.put("shopId", userMemberCardDao.queryShopIdByUmcId(accountId));
            jsonObject.put("umcId", accountId);
        }
        jsonObject.put("amount", userCharge.getAmount());
        map.put("pushJson", jsonObject);
        return map;
		
		//充值情况不需要填写这两个值 --4、16变更 充值会员卡需要计入商户会员卡账户
//		userCharge.setConsumerMobile(userDB.getMobile());
//		userCharge.setConsumerUserId(userId);
//		if(2 == transaction3rdDto.getTransactionType()) {
		//充值到会员卡暂时不做考虑
//			UserMemberShipBillDto userMemberShipBillDto = new UserMemberShipBillDto(); 
//			userMemberShipBillDto.setUmcId(umcId);
//			userMemberShipBillDto.setUserId(userId);
//			userMemberShipBillDto.setBillType(CommonConst.CHARGE);
//			userMemberShipBillDto.setBillDirection(billDirection);
//			userMemberShipBillDto.setBillStatus(billStatus);
//			userMemberShipBillDto.setMoney(money);
//			userMemberShipBillDto.setTransactionId(transactionId);
//			userMemberShipBillDto.setCreateTime(new Date());
//			userMemberShipBillDto.setBillDesc(CommonConst.MEMBER_CHARGE);
//		}
		
		//待反馈进度
//		userCharge.setBillStatus(RechargeEnum.FEEDBACK_PROCESS.getValue());
//		userCharge.setBillDesc(CommonConst.MEMBER_CHARGE);
//		userCharge.setTransactionId(transactionId);
//		//充值代表资金增加
//		userCharge.setBillDirection(1);
//		userCharge.setUserRole(CommonConst.MEMBER);
//		userCharge.setCreateTime(new Date());
//		userCharge.setBillType(CommonConst.CHARGE);
//		//充值中
//		userCharge.setBillStatusFlag(CommonConst.BILL_STATUS_ONE);
//		
//		//TODO这个标题怎么写暂时未定
//		userCharge.setBillTitle(CommonConst.CHARGE_TITLE);
//		
//		// 设置账户余额
//		userCharge.setAccountAmount(userAccountDao.getAccountMoney(userId).getAmount());
//		
//		userBillDao.saveChargeBill(userCharge);
	}

	@Override
	public void saveCharge(UserChargeDto userCharge) throws Exception {
		userBillDao.saveChargeBill(userCharge);
	}

	@Override
	public int insertShopBill(ShopBillDto shopBillDto) throws Exception {
		return shopBillDao.insertShopBill(shopBillDto);
	}

}
