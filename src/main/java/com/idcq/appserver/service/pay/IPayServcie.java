package com.idcq.appserver.service.pay;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.pay.WithdrawRequestModel;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;

public interface IPayServcie {
	
	/**
	 * 新增交易流水
	 * @return
	 * @throws Exception
	 */
	public Integer addTransaction(TransactionDto transactionDto) throws Exception; 
	
	/**
	 * 新增支付
	 * @return
	 * @throws Exception
	 */
	public Long addOrderPay(PayDto payDto) throws Exception;
	/**
	 * 使用第三方支付接口
	 * 增加第三方支付信息
	 * @return
	 * @throws Exception
	 */
	public String payBy3rd(Transaction3rdDto transaction3rdDto) throws Exception;
	/**
	 *  我的第三方支付列表接口
	 * @param userId 用户id
	 * @param status 交易状态
	 * @param rdOrgName 第三方支付名称
	 * @param pageNo 当前页
	 * @param pageSiz 页大小
	 * @return
	 */
	List<Map<String, Object>> getMy3rdPay(Long userId ,int status,String rdOrgName,int pageNo,int pageSiz) throws Exception;;
	/**
	 * 我的第三方支付列表总数
	 * @param userId 用户id
	 * @param status 交易状态
	 * @param rdOrgName 第三方支付名称
	 * @param pageNo 当前页
	 * @param pageSiz 页大小
	 * @return 
	 */
	int getMy3rdPayCount(Long userId ,int status,String rdOrgName,int pageNo,int pageSiz) throws Exception;;
	/**
	 * 修改第三方支付状态
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception 
	 */
	Map<String,Object> nofity3rdPayStatus(Transaction3rdDto transaction3rdDto,Integer userBillType) throws Exception;
	
	public Map<String,Object> addOrderTransAndPay(TransactionDto transactionDto,PayDto payDto) throws Exception;
	
	/**
	 * 记录订单日志
	 * @param order
	 * @param remark
	 * @throws Exception
	 */
	public void saveOrderLog(OrderDto order,String remark) throws Exception;
	
	public double getSumPayAmount(String orderId) throws Exception;
	
	/**
	 * 获取商品id根据orderId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Long getShopIdByOrderId(String orderId) throws Exception;
	/**
	 * 用户发起提现接口
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception
	 */
	Long withdrawByUser(WithdrawDto withdrawDto) throws Exception;
	/**
	 * 用户发起提现接口
	 * @param transaction3rdDto
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> withdrawByShop(ShopWithDrawDto shopWithDrawDto) throws Exception;
	/**
	 * 查询用户提现记录
	 * @param userId 用户id
	 * @param startTime 查询开始时间(查询起始时间，为空时表示查询一周内)
	 * @param endTime  查询结束时间(查询截止时间，为空时表示当天)
	 * @param pageNo 当前页码
	 * @param pageSize 页大小
	 * @return
	 */
	List<Map<String, Object>> getWithdrawList(Map<String,Object> map,int pageNo,int pageSize) throws Exception;
	
	/**
	 *  查询用户提现记录总数
	 * @param userId 用户id
	 * @param startTime 查询开始时间(查询起始时间，为空时表示查询一周内)
	 * @param endTime  查询结束时间(查询截止时间，为空时表示当天)
	 * @return 
	 */
	int getWithdrawListCount(Map<String,Object> map) throws Exception;	
	
	/**
	 * 查询订单组待支付金额
	 * @param orderGroupId
	 * @return
	 * @throws Exception
	 */
	public Map getOrdersToBePayedAmount(String orderGroupId,int orderPayType) throws Exception;
	
	/**
	 * 形成订单支付组
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	public String groupOrders(String orderIds) throws Exception;
	
	/**
	 * 检查指定订单是否使用代金券支付过
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int checkOrderIsPayByCash(String orderId,Long uccId,Integer orderPayType) throws Exception;
	
	public void detailOrderGoodsSettle(Long userId, String orderId) throws Exception;
	
	public void updateOrderGroupStatus(List<Map> list, int payStatus,Integer orderStatus,String remark)  throws Exception;
	
	/**
	 * 查询订单组的订单列表
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<Map> queryOrderGroupById(String orderId) throws Exception;
	
	/**
	 * 查询订单组需要支付的总金额
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Double getSumOrderGroupAmount(String orderId) throws Exception;
    /**
     * 删除支付信息
     * @return
     * @throws Exception
     */
    void updateTransactionByid(Long transactionId,String rdTransactionId) throws Exception;

    /**
     * 页面支付，处理支付宝返回结果
     * @param transaction3rdDto
     * @throws Exception
     */
	void handleAlipayBack(Transaction3rdDto transaction3rdDto) throws Exception;
	/**
	 * 服务端查询提现记录
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getServiceWithdrawList(Integer withdrawStatus,Long userId,String mobile,int pageNo,int pageSize) throws Exception;

	/**
	 * 管理员处理提现接口
	 * @param paramMap
	 */
	ShopWithDrawDto adminWithdraw(Map<String, Object> paramMap) throws Exception;
	
	
	/**
	 * 查询传奇宝账户余额
	 * 2015.6.30 新增
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserAccountDto getAccountMoney(Long userId) throws Exception;
	/**
	 * 查询商铺提现记录接口
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel  返回类型 
	 * @throws
	 */
	PageModel getShopWithdrawList(Map<String, Object> map)throws Exception;
	/**
	 * 查询商铺充值记录
	 * @Title: getShopWithdrawList 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel  返回类型 
	 * @throws
	 */
	PageModel getShopRechargeList(Map<String, Object> map)throws Exception;

	/**
	 * P40：查询订单支付详情接口
	 * @param orderId
	 * @return
	 */
	public List<Map> getOrderPayDetail(String orderId) throws Exception;

	/**
	 * 取消交易的时候要更新用户账单状态
	 * @param transactionId
	 */
	public void cancelRecharge(Long transactionId);
	
	/**
	 * 短信支付
	 * 
	 * @Function: com.idcq.appserver.service.pay.IPayServcie.payBySms
	 * @Description:
	 *
	 * @param order 订单
	 * @param needPayAmount 需要支付金额
	 * @param UserAccountDto 传奇宝宝账户信息
	 * @param couponBalance  代金券可用余额
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月23日 下午2:39:36
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    shengzhipeng       v1.0.0         create
	 */
	boolean payBySms(OrderDto order, Double needPayAmount, UserAccountDto accountDto, Double couponBalance, UserDto user) throws Exception;

	public void adminWithdrawForUser(Map<String, Object> paramMap) throws Exception;
	
	BankCardDto getBankCardByCard(String cardNumber, String userId,Integer account) throws Exception;

	public int getShopWithdrawListCount(Map<String, Object> mapParameter) throws Exception;

	public List<Map<String, Object>> getShopWithdrawList(
			Map<String, Object> mapParameter, Integer pageNo, Integer pageSize) throws Exception;
	   /**
     * 使用第三方支付接口
     * @param transaction3rdDto
     * @return
     * @throws Exception
     */
    Long addPayBy3rd(Transaction3rdDto transaction3rdDto) throws Exception;

    /**
     * 查询交易流水
     * @Function: com.idcq.appserver.service.pay.IPayServcie.ge3rdPayById
     * @Description:
     *
     * @param transaction3rdDto
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年11月27日 下午2:49:45
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年11月27日    ChenYongxin      v1.0.0         create
     */
    Transaction3rdDto ge3rdPayById(Transaction3rdDto transaction3rdDto) throws Exception;
    
    /**
     * 构建用户账单
     * @Title: buildUserBill 
     * @param @param payDto
     * @param @param order
     * @param @param userAccount
     * @param @param billType
     * @param @param billDirection
     * @param @return
     * @param @throws Exception
     * @return UserBillDto    返回类型 
     * @throws
     */
    public UserBillDto buildUserBill(PayDto payDto,OrderDto order,UserAccountDto userAccount,String billType,Integer billDirection) throws Exception;
    
    /**
     * 构建 平台账单
     * @Title: buildPlatformBillBy3Rd 
     * @param @param userBillType
     * @param @param userId
     * @param @param money
     * @param @param order
     * @param @param transactionId
     * @param @param userAccount
     * @param @param billType
     * @param @param billDirection
     * @param @return
     * @param @throws Exception
     * @return PlatformBillDto    返回类型 
     * @throws
     */
    public PlatformBillDto buildPlatformBillBy3Rd(Integer userBillType,Long userId,Double money,OrderDto order,Long transactionId,
            UserAccountDto userAccount,String billType,Integer billDirection) throws Exception;
    /**
     * 支付结果查询接口
     * 
     * @Function: com.idcq.appserver.service.pay.IPayServcie.getPayResult
     * @Description:
     *
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月17日 下午3:27:20
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年12月17日    ChenYongxin      v1.0.0         create
     */
    public Map<String, Object>getPayResult(Map<String, Object> map) throws Exception;

	public Map<String, String> findBankNameByCardNo(String cardNo);
	
	 /**
     * 使用红包支付，返回实际支付金额
     * @param payAmount 需要支付的金额
     * @param order 订单
     * @param userId 用户ID
     * @param isAllow 不足时是否允许支付
     * @return Double 实际支付金额
     * @author  shengzhipeng
     * @date  2016年3月15日
     */
    Double payByRedPacket(Double payAmount, OrderDto order, Long userId, boolean isAllow,Integer clientSystem) throws Exception;
    
    void useRedPacket(OrderDto order, RedPacket redPacket, Double amount, Long userId,Integer clientSystem) throws Exception;
    /**
     * 第三方支付成功修改交易记录
     * 
     * @Function: com.idcq.appserver.service.pay.IPayServcie.updateTransactionAfterRdPaySuccess
     * @Description:
     *
     * @param transactionDto
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年4月5日 下午2:21:34
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年4月5日    ChenYongxin      v1.0.0         create
     */
    public void updateTransactionAfterRdPaySuccess(TransactionDto transactionDto) throws Exception;
    
	/**
	 * 商铺总部提现接口
	 * @param requestModel 提现接收model
	 * @return 提现id
	 * @throws Exception
	 */
    Map<String, Object> headquartersWithdraw(WithdrawRequestModel requestModel) throws Exception;
    
	/**
	 * 保证金转换
	 */
    void depositChange(Long shopId,Integer accountType,Double money, Integer billType) throws Exception;
	/**
	 * 插入反结账订单商品线上支付账单
	 */
    void insertReverseShopBill(OrderDto orderDto) throws Exception;
	/**
	 * 插入店铺流水
	 * 
	 * @Function: com.idcq.appserver.service.pay.impl.PayServiceImpl.insertShopBill
	 * @Description:
	 *
	 * @param shopId 商铺id
	 * @param billType 账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,红包=3(停用),提现=4,充值=5,推荐奖励=6,提现退回=7,
	 * 冻结资金=8,解冻资金=9,转充=10,充值卡充值=11,购买插件=12,锁提现流转转入=21,连锁提现转出=22,发红包=41',
	 * @param billDirection '账单类型:1（账户资金增加）,-1（账户资金减少）',
	 * @param BillDesc 描述
	 * @param billStatus 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
	 * @param accountType 账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
	 * @param accountAmount 处理前账号余额
	 * @param accountAfterAmount 使用后余额
	 * @param money 本次账单金额
	 * @param billTitle 账单标题
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月30日 下午4:44:08
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月30日    ChenYongxin      v1.0.0         create
	 */
    public ShopBillDto insertShopBill(Long shopId,String billType,Integer billDirection,String billDesc,Integer billStatus,
			Integer accountType,Double accountAmount,Double accountAfterAmount,Double money,String billTitle,Long transactionId,String orderId) throws Exception;

}

