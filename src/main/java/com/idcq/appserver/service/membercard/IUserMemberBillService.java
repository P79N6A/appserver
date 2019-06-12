package com.idcq.appserver.service.membercard;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.membercard.UserChargeDto;

public interface IUserMemberBillService {

	/**
	 * 获取会员卡账单
	 */
	PageModel getPageUserBill(Integer pageNo,Integer pageSize,Map<String,Object> map) throws Exception;
	
	/**
	 * 保存用户充值到账单表
	 * @param request
	 * @return 返回交易编号
	 * @throws Exception
	 */
	Map<String, Object> saveCharge(HttpServletRequest request) throws Exception;

	void saveCharge(UserChargeDto userCharge) throws Exception;
	
	/**
	 * 生成商铺账单
	 * @param shopBillDto
	 * @return
	 * @throws Exception
	 */
	int insertShopBill(ShopBillDto shopBillDto) throws Exception; 
	
}
