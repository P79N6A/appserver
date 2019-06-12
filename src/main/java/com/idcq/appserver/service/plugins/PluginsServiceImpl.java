package com.idcq.appserver.service.plugins;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.plugins.IPluginsDao;
import com.idcq.appserver.dao.plugins.IShopPluginDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.common.Page;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.plugin.PluginDto;
import com.idcq.appserver.dto.plugin.PluginModel;
import com.idcq.appserver.dto.plugin.ShopPluginDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.IChuanQiPayService;

@Service
public class PluginsServiceImpl implements IPluginsService {
	private static final Logger logger = Logger.getLogger(PluginsServiceImpl.class);
	@Autowired
	private IPluginsDao pluginsDao;
	@Autowired
	private IShopPluginDao shopPluginDao;
	@Autowired
	private IChuanQiPayService chuanQiPayService;
	@Autowired
	public IPlatformBillDao platformBillDao;
	
	@Override
	public Map<String, Object> buyShopPlugin(Map<String, Object> requestMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Long shopId = Long.valueOf(requestMap.get("shopId").toString());
		Integer pluginId = Integer.valueOf(requestMap.get("pluginId").toString());
		Integer buyNumber = Integer.valueOf(requestMap.get("buyNumber").toString());
		Integer payChannel = Integer.valueOf(requestMap.get("payChannel").toString());
		Double money = Double.valueOf(requestMap.get("money").toString());
		Date startTime = new Date();
		PluginDto pluginDto = pluginsDao.getPluginById(pluginId);
		ShopPluginDto shopPluginDto = shopPluginDao.getShopPlugin(pluginId, shopId);
		Integer chargeWay = pluginDto.getCharge_way();
		
		if (pluginDto.getStatus() != 1) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"插件未审核");
		}
		if (chargeWay > 2) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"暂不支付该种计费方式  chargeWay:"+chargeWay);
		}
		if (chargeWay == 1 || chargeWay == 2) {
			checkMoneyValid(pluginDto, buyNumber, money);
		}
		if (shopPluginDto != null) {
			startTime = shopPluginDto.getEndTime();
		}
		shopPluginDto = buildShopPluginDtoByChargeWay(shopId, pluginId, buyNumber,
				money, startTime, chargeWay,payChannel);
		shopPluginDao.insertShopPlugin(shopPluginDto);
		
		if (payChannel == 0 && chargeWay != 0) {
			chuanQiPayService.payShopPlugin(pluginDto, shopPluginDto);
		}
		resultMap.put("shopPluginId", shopPluginDto.getShopPluginId());
		return resultMap;
	}
	
	private void checkMoneyValid(PluginDto pluginDto,Integer buyNumber,Double payMoney) throws Exception{
		BigDecimal needPayMoney = pluginDto.getMoney().multiply(BigDecimal.valueOf(buyNumber.longValue()));
		Boolean valid = needPayMoney.compareTo(BigDecimal.valueOf(payMoney.doubleValue())) == 0 ? true : false; 
		if (valid == false) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"支付金额校验错误  发起支付金额："+payMoney+"应该支付金额："+needPayMoney);
		}
	}
	
	private ShopPluginDto buildShopPluginDtoByChargeWay(Long shopId,Integer pluginId,
														Integer buyNumber,Double money,
														Date startTime,Integer chargeWay,
														Integer payChannel) throws Exception{
		
		ShopPluginDto shopPlugin = new ShopPluginDto();
		shopPlugin.setShopId(shopId.intValue());
		shopPlugin.setPluginId(pluginId);
		shopPlugin.setBuyNumber(buyNumber);
		shopPlugin.setMoney(money);
		shopPlugin.setOrderTime(new Date());
		shopPlugin.setBeginTime(startTime);
		shopPlugin.setEndTime(getShopPluginEndTimeByChargeWay(chargeWay, startTime));
		if (chargeWay == 0) {
			shopPlugin.setIsPaid(1);
			shopPlugin.setIsSettled(1);
		} else {
			shopPlugin.setIsPaid(0);
			shopPlugin.setIsSettled(0);
		}
		shopPlugin.setIsActive(1);
		return shopPlugin;
	}
	
	private Date getShopPluginEndTimeByChargeWay(Integer chargeWay,Date startTime) throws Exception{
		Date endTime = null;
		Calendar cal = Calendar.getInstance();  
		cal.setTime(startTime);
		if (chargeWay == 0 || chargeWay == 1) {
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+5);
			endTime = cal.getTime();
		} else if(chargeWay == 2) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
			endTime = cal.getTime();
		} else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"暂不支付该种计费方式  chargeWay:"+chargeWay);
		}
		return endTime;
	}
	@Override
	public Boolean queryPluginIsExist(Integer pluginId) throws Exception{
		return pluginsDao.queryPluginIsExist(pluginId);
	}
	
	@Override
	public ShopPluginDto getShopPluginById(Integer shopPluginId) throws Exception {
		return shopPluginDao.getShopPluginById(shopPluginId);
	}
	
	@Override
	public void insertPlatformBillForShopPlugin(TransactionDto transaction,Integer moneySource,String billType) throws Exception {
		PlatformBillDto platformBill =buildPlatformBillForSHopPlugin(transaction, moneySource, "店铺购买插件");
        platformBillDao.insertPlatformBill(platformBill);
	}
	private PlatformBillDto buildPlatformBillForSHopPlugin(TransactionDto transaction,Integer moneySource,String billType) {
		PlatformBillDto platformBill = new PlatformBillDto();
        platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PLUGIN);
        platformBill.setBillType(billType);
        platformBill.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBill.setMoney(transaction.getPayAmount());
        platformBill.setTransactionId(transaction.getTransactionId());
        platformBill.setOrderId(transaction.getOrderId());
        platformBill.setCreateTime(new Date());
        platformBill.setSettleTime(new Date());
        platformBill.setBillDesc(billType);
    	platformBill.setConsumerUserId(Long.valueOf(0));
    	platformBill.setConsumerMobile("0");
        platformBill.setMoneySource(moneySource);
        return platformBill;
	}
    
    public PageModel getShopPlugins(Map<String, Object> pMap) throws Exception{
        PageModel pageModel = new PageModel();
        // 总记录数
        Integer count = pluginsDao.getShopPluginsCount(pMap);
        if(count!=null && count>0){
            List<Map<String, Object>> shopPluginList = pluginsDao.getShopPlugins(pMap);
            pageModel.setList(shopPluginList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }
    public PageModel getPlugins(Map<String, Object> pMap) throws Exception{
        PageModel pageModel = new PageModel();
        // 总记录数
        Integer count = pluginsDao.getPluginsCount(pMap);
        if(count!=null && count>0){
            List<Map<String, Object>> pluginList = pluginsDao.getPlugins(pMap);
            pageModel.setList(pluginList);
            pageModel.setTotalItem(count);
        }
        return pageModel;        
    }

    @Override
    public PageModel getPluginUpdateInfoForShop(PluginModel pluginModel, Page page)
    {
		PageModel model = new PageModel();
		int count = shopPluginDao.getPluginCountForShop(pluginModel);
		model.setTotalItem(count);
		List<PluginModel> list = shopPluginDao.getPluginUpdateInfoForShop(pluginModel, page);
		model.setList(list);
        return model;
    }

	@Override
	public Map<String, Object> getPluginDetail(Map<String, Object> searchParam) {
		Map<String, Object> rs  = null;
//				= new HashMap<String, Object>();
		rs = pluginsDao.getPluginDetail(searchParam);
		rs = rs ==null ? new HashMap<String, Object>() : rs;
		return rs;
	}

	@Override
	public void recordPluginDownload(Integer shopId, Integer pluginId, Integer attachmentId, String sn) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("attachmentId", attachmentId);
		logger.debug("根据attachmentId获取版本信息:" + attachmentId);
		Map<String, Object> tempRs = pluginsDao.getPluginVersionInfo(queryParams);
		String versionNum = (String)tempRs.get("versionNum");
		queryParams.clear();
		queryParams.put("versionNum", versionNum);
		queryParams.put("shopId", shopId);
		queryParams.put("pluginId", pluginId);
		queryParams.put("isPaid", 1);
		queryParams.put("isSettled", 1);
		queryParams.put("isActive", 1);
		queryParams.put("sn", sn);
		queryParams.put("orderTime", new Date());
		logger.debug("插入下载记录");
		pluginsDao.insertPluginUseRecord(queryParams);
		logger.debug("记录下载次数");
		pluginsDao.incrementDownloadCount(pluginId);
	}
}
