package com.idcq.appserver.service.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.pay.IXorderPayDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.XorderPayDto;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;

@Service
public class XorderServiceImpl implements IXorderService{
	@Autowired
	public IXorderDao xorderDao;
	
	@Autowired
	private ITransaction3rdDao transactionDao;
	
	@Autowired
	private IXorderPayDao xorderPayDao;
	
	@Autowired
	private IPlatformBillDao platformBillDao;
	
	@Autowired
	private IShopBillDao shopBillDao;
	
	@Autowired
	private IOrderServcie orderService;
	public int queryXorderExists(String orderId) throws Exception {
		return this.xorderDao.queryXorderExists(orderId);
	}

	public Integer getOrderStatusById(String orderId) throws Exception {
		return this.xorderDao.getOrderStatusById(orderId);
	}

	public XorderDto getXOrderById(String orderId) throws Exception {
		return this.xorderDao.getXOrderById(orderId);
	}
	
	/**
	 * 修改非会员订单
	 * @Title: updateXOrder 
	 * @param @param orderDto
	 * @param @throws Exception  
	 * @throws
	 */
	public void updateXOrder(XorderDto orderDto) throws Exception {
		xorderDao.updateXorderDto(orderDto)	;
	}

	/**
	 * 获取收银订单的详情
	 * @Title: getCashierBillDetail 
	 * @param @param dto
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	public Map<String, Object> getCashierBillDetail(XorderDto dto)
			throws Exception {
		Map<String,Object>resultJson=new HashMap<String,Object>();
		if(dto!=null){
			resultJson.put("money", dto.getSettlePrice());
			resultJson.put("billTime", DateUtils.format(dto.getOrderTime(),DateUtils.DATETIME_FORMAT));//账单详情
			resultJson.put("remark",dto.getOrderTitle());
			//TO_DO 从xorder_pay查找记录
			//resultJson.put("pay_type",dto.get)
			resultJson.put("orderNo",dto.getXorderId());//订单编号
		}
		return resultJson;
	}
	
	/**
	 * 生成交易记录和支付记录
	 * @Title: generateTransactionAndPay 
	 * @param @param transactionDto
	 * @param @param xorderPayDto  
	 * @throws
	 */
	public void generateTransactionAndPay(Transaction3rdDto transactionDto,
			XorderPayDto xorderPayDto) throws Exception{
		int transactionId=transactionDao.addTransaction(transactionDto);
		transactionDto.setTransactionId((long)transactionId);
		xorderPayDto.setPayId(transactionId);
		xorderPayDao.addXorderPayDto(xorderPayDto);
	}
	
	/**
	 * 一点管家非会员订单详情
	 * @Title: getIdgjXorderDetail 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public XorderDto getIdgjXorderDetail(String orderId) throws Exception {
		XorderDto xorderDto=xorderDao.getXOrderById(orderId);
		List<XorderPayDto> payList= xorderPayDao.queryXorderPayList(orderId);
		if(payList!=null&&payList.size()>0){
			xorderDto.setPayType(payList.get(0).getPayType());
		}
		return xorderDto;
	}
	
	/**
	 * 生成一条平台收入记录
	 * @Title: generatePlatformBill 
	 * @param @param xorderDto
	 * @param @param moneySource
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void generatePlatformBill(XorderDto xorderDto,Integer moneySource)throws Exception{
		String billDesc="平台收入(支付宝支付)";
		if(CommonConst.PLT_BILL_MNY_SOURCE_WX==moneySource){
			billDesc="平台收入(微信支付)";
		}
		PlatformBillDto platformBillDto=new PlatformBillDto();//平台账单
		platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);//增加
		platformBillDto.setBillDesc(billDesc);
		platformBillDto.setBillStatus(CommonConst.BILL_STATUS_FLAG_PROCESS);//账单状态
		platformBillDto.setCreateTime(new Date());
		platformBillDto.setMoney(xorderDto.getSettlePrice());//收入金额
		platformBillDto.setOrderId(xorderDto.getXorderId());//订单编号
		platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);//销售收入
		platformBillDto.setConsumerUserId(0l);//代表是非会员订单
		platformBillDto.setConsumerMobile("非会员");
		platformBillDto.setMoneySource(moneySource);
		platformBillDto.setBillType("平台收入");
		platformBillDao.insertPlatformBillMiddle(platformBillDto);
		
		//--------------------------出账--------------------------------
		PlatformBillDto expendPlatformBillDto=new PlatformBillDto();//平台账单
		expendPlatformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);//增加
		expendPlatformBillDto.setBillDesc("返还商铺线上营业收入");
		expendPlatformBillDto.setBillStatus(CommonConst.BILL_STATUS_FLAG_PROCESS);//账单状态
		expendPlatformBillDto.setCreateTime(new Date());
		expendPlatformBillDto.setMoney(-xorderDto.getSettlePrice());//收入金额
		expendPlatformBillDto.setOrderId(xorderDto.getXorderId());//订单编号
		expendPlatformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_ONLINE);//销售收入
		expendPlatformBillDto.setConsumerUserId(0l);//代表是非会员订单
		expendPlatformBillDto.setConsumerMobile("非会员");
		expendPlatformBillDto.setMoneySource(moneySource);
		expendPlatformBillDto.setBillType("支付商铺线上营业收入");
		platformBillDao.insertPlatformBillMiddle(expendPlatformBillDto);
	}
	
	/**
	 * 生成店铺账单
	 * @Title: generateShopMiddleBill 
	 * @param @param orderDto
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	public void generateShopMiddleBill(XorderDto orderDto)throws Exception{
		ShopBillDto shopBillDto=new ShopBillDto();
		shopBillDto.setShopId(orderDto.getShopId());
		shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
		shopBillDto.setConsumerUserId(0l);//非会员订单
		shopBillDto.setMoney(orderDto.getSettlePrice());
		shopBillDto.setOrderId(orderDto.getXorderId());
		shopBillDto.setCreateTime(new Date());
		shopBillDto.setBillDesc("销售收入");
		shopBillDto.setBillType(CommonConst.BILL_TYPE_SALE);
		shopBillDto.setBillTitle(orderDto.getOrderTitle());
		//TODO 变为常量
		shopBillDto.setAccountType(0);//线上营业收入
		shopBillDto.setPayAmount(orderDto.getSettlePrice());
		shopBillDto.setAccountAmount(0d);
		shopBillDto.setAccountAfterAmount(0d);
		shopBillDao.insertShopMiddleBill(shopBillDto);
	}

	
	
}
