package com.idcq.appserver.service.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IUnitDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IShopGoodsDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.storage.IShopStorageCheckDetailDao;
import com.idcq.appserver.dao.storage.IShopStorageCheckNoteDao;
import com.idcq.appserver.dao.storage.IShopStorageDetailDao;
import com.idcq.appserver.dao.storage.IShopStorageNoteDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.storage.OperateShopStorageDto;
import com.idcq.appserver.dto.storage.OperateStorageCheckDto;
import com.idcq.appserver.dto.storage.ShopStorageCheckDetailDto;
import com.idcq.appserver.dto.storage.ShopStorageCheckNoteDto;
import com.idcq.appserver.dto.storage.ShopStorageDetailDto;
import com.idcq.appserver.dto.storage.ShopStorageNoteDto;
import com.idcq.appserver.dto.storage.StorageCheckGoodsDto;
import com.idcq.appserver.dto.storage.StorageGoodsDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.pinyin.PinyinUtil;
import com.idcq.idianmgr.dao.shop.IShopCashierDao;

/**
 * 库存service
 * 
 * @author Administrator
 * 
 * @date 2016年4月6日
 * @time 下午3:22:35
 */
@Service
public class StorageServiceImpl implements IStorageServcie {

	private static final Logger logger = Logger
			.getLogger(StorageServiceImpl.class);
	@Autowired
	private IShopGoodsDao shopGoodsDao;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	public IShopStorageDetailDao shopStorageDetailDao;
	@Autowired
	public IShopStorageNoteDao shopStorageNoteDao;
	@Autowired
	public IGoodsDao goodsDao;
	@Autowired
	public IUnitDao unitDao;
	@Autowired
	public  ICollectService collectService;
	@Autowired
	public IShopStorageCheckDetailDao shopStorageCheckDetailDao;
	@Autowired
	public IShopStorageCheckNoteDao shopStorageCheckNoteDao;
	@Autowired
	public  IOrderGoodsDao orderGoodsDao;
	@Autowired
	public  IOrderDao orderDao;
	@Autowired
	public  IShopCashierDao cashierDao;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idcq.appserver.service.storage.IStorageServcie#operateShopStorage
	 * (java.util.Map)
	 */
	@Override
	public void operateShopStorage(OperateShopStorageDto operateShopStorage)
			throws Exception {

		/**
		 * 1、假如没有传goodsId，则认为goods不存在，走新增goods流程。 
		 * 2、若传了goodsId，则更新goods信息，。
		 * 3、增加出入库信息，记录出入库详单流水
		 */

		// 记录出库单信息
		Long storageId  = insertShopStorageNote(operateShopStorage);
		
		//记录出库单详情
		batchInsertShopStorageDetail(operateShopStorage,storageId,17);// '业务主键类型:出入库记录=17,盘点记录=18'

	}
	
	@Override
	public void insertShopStorage(GoodsDto goodsDto) throws Exception {
		OperateShopStorageDto operateShopStorage = buildOperateShopStorageByGoodsDto(goodsDto);
		Long storageId  = insertShopStorageNote(operateShopStorage);
		
		ShopStorageDetailDto shopStorageDetail = new ShopStorageDetailDto();

		shopStorageDetail.setBizId(storageId);// 库存记录id
		shopStorageDetail.setBizType(17);// '业务主键类型:出入库记录=17,盘点记录=18'
		shopStorageDetail.setChangeType(operateShopStorage.getStorageType());
		shopStorageDetail.setCreateTime(new Date());
		shopStorageDetail.setDetailRemark(operateShopStorage.getStorageRemark());
		shopStorageDetail.setGoodsId(goodsDto.getGoodsId());
		shopStorageDetail.setStorageAfterNumber(goodsDto.getStorageAfterNumber() == null ? goodsDto.getStorageTotalNumber() : goodsDto.getStorageAfterNumber());
		shopStorageDetail.setStorageNumber(goodsDto.getStorageTotalNumber());
		shopStorageDetail.setStoragePrice(Double.valueOf(0));
		shopStorageDetail.setGoodsTotalPrice(Double.valueOf(0));
		shopStorageDetail.setUnitId(goodsDto.getUnitId());
		shopStorageDetail.setUnitName(goodsDto.getUnitName());
		List<ShopStorageDetailDto> storageList = new ArrayList<ShopStorageDetailDto>();
		storageList.add(shopStorageDetail);
		shopStorageDetailDao.batchInsertShopStorageDetail(storageList);
	}
	
	/**
	 * 根据goodsDto构建OperateShopStorageDto
	 *
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 * 
	 */
	private OperateShopStorageDto buildOperateShopStorageByGoodsDto(GoodsDto goodsDto) throws Exception{
		OperateShopStorageDto operateShopStorage = new OperateShopStorageDto();
		
	    operateShopStorage.setOperaterId(0L);
	    operateShopStorage.setOperaterName("");
	    operateShopStorage.setShopId(goodsDto.getShopId());
	    operateShopStorage.setStorageRemark("添加商品入库");
	    operateShopStorage.setStorageTime(DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
	    operateShopStorage.setStorageType(12);//入库
	    operateShopStorage.setStorageNo(FieldGenerateUtil.generatebitStorageNoId(CommonConst.STORAGE_NOPREFIX_RK)+goodsDto.getShopId()+1);
	    
	    return operateShopStorage;
	}
	
	public void insertShopStorageByOrderId(String orderId,Long shopId) throws Exception {
		
		OrderGoodsDto orderGoodsParms = new OrderGoodsDto();
		orderGoodsParms.setOrderId(orderId);
		//查询订单所有商品
		List<OrderGoodsDto> goodsList = orderGoodsDao.getOGoodsListByOrderId(orderGoodsParms);
		
		if(CollectionUtils.isNotEmpty(goodsList)){
		    
		    //查看出入库基本信息
		    
            OperateShopStorageDto operateShopStorage = buildOperateShopStorageByOrderGoodsDto( orderId, shopId);
            
            //判断单号是否存在
            ShopStorageNoteDto storageNoteDB = shopStorageNoteDao.getStorageNoByStorageNo(orderId);
            Long storageId  = null;
            if(storageNoteDB==null){
            	storageId  = insertShopStorageNote(operateShopStorage);
            }
            else{
            	 updateShopStorageNote(operateShopStorage);
            	 storageId = storageNoteDB.getStorageId();
            }
            
			//TODO 后续修改成批量处理
			for (OrderGoodsDto orderGoods : goodsList) {
				//处理库存
				insertShopStorageByOrderGoods(orderGoods,storageId);
				
			}
		}

	}
	/**
	 * 根据OrderGoodsDto插入库存流水信息
	 *
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 * 
	 */	
	public void insertShopStorageByOrderGoods(OrderGoodsDto orderGoods,Long storageId) throws Exception {
		
		//商品信息
		GoodsDto goodsDB = goodsDao.getGoodsByIdFromDb(orderGoods.getGoodsId());
		
		//有库存才更新
		if(goodsDB!=null && goodsDB.getStorageTotalNumber() != null){
			
			// 插入库存流水
			List<ShopStorageDetailDto> storageList = buildShopStorageDetailAndUpdateGoods(goodsDB,orderGoods, storageId);
			if(CollectionUtils.isNotEmpty(storageList)){
				shopStorageDetailDao.batchInsertShopStorageDetail(storageList);
			}
			
		}
	}
	/**
	 * 构建流水详情、更新商品
	 * 
	 * @param orderGoods
	 * @param storageId
	 * @return
	 * @throws Exception
	 *
	 */
	private List<ShopStorageDetailDto> buildShopStorageDetailAndUpdateGoods(GoodsDto goodsDB,OrderGoodsDto orderGoods,Long storageId) throws Exception{
		
		List<ShopStorageDetailDto> storageList = new ArrayList<ShopStorageDetailDto>();
		ShopStorageDetailDto shopStorageDetail = new ShopStorageDetailDto();
		
		Double changeNum = getChangeGoodsNum(orderGoods);
		//库存-商品数量
		goodsDB.setStorageTotalNumber(NumberUtil.sub(goodsDB.getStorageTotalNumber(), changeNum));
		goodsDao.updateGoods(goodsDB);
    	//更新缓存
    	DataCacheApi.delObject("GoodsDto:"+goodsDB.getGoodsId());
		
    	if(changeNum > 0){
    		shopStorageDetail.setChangeType(13);//销售出库
    		shopStorageDetail.setDetailRemark("销售出库");
    	}
    	else{
    		shopStorageDetail.setChangeType(12);//其他入库
    		shopStorageDetail.setDetailRemark("其他入库");
    	}
		//构建流水
		shopStorageDetail.setBizId(storageId);// 库存记录id 
		shopStorageDetail.setBizType(17);// '业务主键类型:出入库记录=17,盘点记录=18'
		shopStorageDetail.setCreateTime(new Date());
		shopStorageDetail.setGoodsId(orderGoods.getGoodsId());
		shopStorageDetail.setStorageNumber(changeNum);
		shopStorageDetail.setStorageAfterNumber(goodsDB.getStorageTotalNumber());
		shopStorageDetail.setStoragePrice(goodsDB.getStandardPrice());// 目前无参数传递，默认为0
		shopStorageDetail.setGoodsTotalPrice(NumberUtil.multiply(goodsDB.getStandardPrice(), changeNum));
		shopStorageDetail.setUnitName(orderGoods.getUnitName());
		storageList.add(shopStorageDetail);
		
		return storageList;
	}
    /**
     * 获取反结账后的订单变更数量
     * @throws Exception 
     */
	private Double getChangeGoodsNum(OrderGoodsDto orderGoodsDto) throws Exception{
		Double changeNum = orderGoodsDto.getGoodsNumber();
		//0-正常退菜，1-反结账退菜，2-正常加菜，3-反结账加菜
    	Integer isCancle = orderGoodsDto.getIsCancle()==null ? 2 : orderGoodsDto.getIsCancle().intValue();
    	OrderDto order = orderDao.getOrderById(orderGoodsDto.getOrderId());
    	//反结账
    	if(CommonConst.REVERSE_SETTLE_FLAG==order.getSettleFlag().intValue()){
    		//反结账正常情况不修改库存
    		if(isCancle==0 || isCancle==2){
    			changeNum = 0D;
    		}
    		
    		if(isCancle==1){
    			changeNum = -orderGoodsDto.getGoodsNumber();
    		}
    		if(isCancle==3){
    			changeNum = orderGoodsDto.getGoodsNumber();
    		}
    	}
		return changeNum;
	}
	private OperateShopStorageDto buildOperateShopStorageByOrderGoodsDto(String orderId,Long shopId) throws Exception{
		
		OperateShopStorageDto operateShopStorage = new OperateShopStorageDto();
	    operateShopStorage.setShopId(shopId);
	    operateShopStorage.setStorageRemark("销售商品");
	    operateShopStorage.setStorageTime(DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
	    operateShopStorage.setStorageType(13);//销售出库
	    operateShopStorage.setStorageNo(orderId);
	    
		OrderDto orderDB = orderDao.getOrderById(orderId);
		Long cashierId = orderDB.getCashierId();
    	operateShopStorage.setOperaterId(cashierId);

		Map<String, Object> cashierDB  = cashierDao.getShopCashierById(cashierId);
	    if(cashierDB!=null){
		    operateShopStorage.setOperaterName((String) cashierDB.get("username"));
	    }else{
		    operateShopStorage.setOperaterName("老板");
	    }
	    
	    return operateShopStorage;
	}
	/**
	 * 记录出库单详情
	 * bizType,业务主键类型:出入库记录=17,盘点记录=18'
	 */
	public void batchInsertShopStorageDetail(OperateShopStorageDto operateShopStorage,Long storageId,Integer bizType ) throws Exception{
		
		// '业务主键类型:出入库记录=17,盘点记录=18'
		List<ShopStorageDetailDto> shopStorageDetailDtos = deailShopStorageDetailAndGoods(operateShopStorage, storageId,bizType);
		//批量插入库存流水
		shopStorageDetailDao.batchInsertShopStorageDetail(shopStorageDetailDtos);
		
	}
	
    /**
     * 组装出库单详情参数、goods
     * 
     * @throws Exception 
     * 
     */
	public List<ShopStorageDetailDto> deailShopStorageDetailAndGoods(OperateShopStorageDto operateShopStorage, Long storageId,Integer BizType)
			throws Exception {
		
		List<ShopStorageDetailDto> shopStorageDetailDtos = new ArrayList<ShopStorageDetailDto>();
		List<StorageGoodsDto> goodsList = operateShopStorage.getGoodsList();
		
		if (CollectionUtils.isNotEmpty(goodsList)) {
			
			//便利goosList，返回主键保存到出入库库存流水
			for (StorageGoodsDto storageGoodsDto : goodsList) {

				ShopStorageDetailDto shopStorageDetail = new ShopStorageDetailDto();

				/*** 库存记录实体公共start ***/
				
				//操作类型。1=进货入库，2=其他入库，3=销售出库，4=其他出库
				Integer storageType = operateShopStorage.getStorageType()==null ? storageGoodsDto.getChangeType() : operateShopStorage.getStorageType();
				//校验goodsId，当出货的时候
				
				shopStorageDetail.setBizId(storageId);// 库存记录id
				shopStorageDetail.setBizType(BizType);// '业务主键类型:出入库记录=17,盘点记录=18'
				shopStorageDetail.setChangeType(storageType);
				shopStorageDetail.setCreateTime(new Date());
				shopStorageDetail.setDetailRemark(storageGoodsDto.getStorageBillRemark());
				shopStorageDetail.setStoragePrice(storageGoodsDto.getStoragePrice()==null ? 0 : storageGoodsDto.getStoragePrice());
				shopStorageDetail.setUnitId(storageGoodsDto.getUnitId());
				shopStorageDetail.setUnitName(storageGoodsDto.getUnitName());
				shopStorageDetail.setDigitScale(storageGoodsDto.getDigitScale());
				/** 库存记录实体公共end ***/

				// 商品id
				Long goodsId = storageGoodsDto.getGoodsId();
				// 出、入库商品数量
				Double storageNumber = storageGoodsDto.getStorageNumber() == null ? 1D : storageGoodsDto.getStorageNumber();
				storageNumber  = storageNumber<0 ? Math.abs(storageNumber) : storageNumber;
				
				// 没有传goodsId，则需要增加goods
				if (goodsId == null) {
					
					logger.info("PCS25：商铺商品出入库接口：goodsId为null");
					//校验goodsId
					verifyGoodsId(storageType, goodsId);
					
					//  增加goods
					goodsId = addGoods(storageGoodsDto, operateShopStorage);
					shopStorageDetail.setGoodsId(goodsId);
					shopStorageDetail.setStorageAfterNumber(storageNumber);
					shopStorageDetail.setStorageNumber(storageNumber);
				}
				// 传了goodsId，则更新goods信息
				else {
				 
					logger.info("商铺商品出入库：goodsId=" + goodsId);
					
					GoodsDto goodsDB = goodsDao.getGoodsById(goodsId);

					CommonValidUtil.validObjectNull(goodsDB,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_GOOD);
					Double storageTotalNumber = 0D;
					if(operateShopStorage.getIsUpdateGoods()) {
					     // 库存总量
					    storageTotalNumber = goodsDB.getStorageTotalNumber() == null ? 0D : goodsDB.getStorageTotalNumber();
					}
					shopStorageDetail.setGoodsId(goodsId);
					shopStorageDetail.setStorageAfterNumber(getStorageTotalNumber(storageType,storageTotalNumber,storageGoodsDto.getStorageNumber()));// 出入库后库存
					shopStorageDetail.setStorageNumber(storageNumber);// 本次数量
					if(operateShopStorage.getIsUpdateGoods()) {
					    //  更新goods
	                    updateGoods(storageGoodsDto, goodsDB,storageType);
					}
				} 
				
				//单商品总价
				Double goodsTotalPrice = storageGoodsDto.getGoodsTotalPrice()==null ? 0 : storageGoodsDto.getGoodsTotalPrice();
				if(null!=storageGoodsDto.getStoragePrice()){
					shopStorageDetail.setGoodsTotalPrice(NumberUtil.multiply(storageGoodsDto.getStoragePrice(), storageNumber));
				}
				else{
					shopStorageDetail.setGoodsTotalPrice(goodsTotalPrice);
				}

				shopStorageDetailDtos.add(shopStorageDetail);

			}
		}
		return shopStorageDetailDtos;
	}
	/**
	 * 校验goodsId
	 * 
	 * @Function: com.idcq.appserver.service.storage.StorageServiceImpl.verifyGoodsId
	 * @Description:
	 *
	 * @param storageType
	 * @param goodsId
	 * @return
	 * @throws Exception 
	 * 
	 */
    private void verifyGoodsId(Integer storageType,Long goodsId) throws Exception{
    	//出库 减库存
    	if(13==storageType || 14==storageType){
            CommonValidUtil.validObjectNull(goodsId, CodeConst.CODE_PARAMETER_NOT_NULL, "goodsId不能为空");
    	}
    }

    /**
     * 增加商品信息
     * 
     * @throws Exception 
     * 
     */	
    private Long addGoods(StorageGoodsDto storageGoodsDto,OperateShopStorageDto operateShopStorage) throws Exception{
        
    	GoodsDto goods = new GoodsDto();
    	goods.setGoodsName(storageGoodsDto.getGoodsName());
    	goods.setShopId(operateShopStorage.getShopId());
    	Double standardPrice = storageGoodsDto.getStandardPrice()==null ? 0 : storageGoodsDto.getStandardPrice();
    	goods.setStandardPrice(standardPrice);
    	goods.setDiscountPrice(standardPrice);
    	goods.setVipPrice(standardPrice);
    	String pinyinCode = PinyinUtil.getPinYinHeadChar(storageGoodsDto.getGoodsName());
    	goods.setPinyinCode(pinyinCode);//首字母拼音
    	goods.setBarcode(storageGoodsDto.getBarcode());
    	goods.setLastUpdateTime(new Date());
    	goods.setAlarmNumberMax(storageGoodsDto.getAlarmNumberMax());
    	goods.setAlarmNumberMin(storageGoodsDto.getAlarmNumberMin());
    	goods.setStorageTotalNumber(storageGoodsDto.getStorageNumber());
    	goods.setGoodsCategoryId(storageGoodsDto.getGoodsCategoryId());
    	Long unitId = collectService.saveUnit(storageGoodsDto.getUnitName()==null ? "个" : storageGoodsDto.getUnitName(), 
    			operateShopStorage.getShopId(), storageGoodsDto.getDigitScale()==null ?  0 : storageGoodsDto.getDigitScale());
    	goods.setUnitId(unitId);
    	goods.setUnitName(storageGoodsDto.getUnitName());
    	//增加商品
    	goodsDao.addGoodsDto(goods);
    	
    	return goods.getGoodsId();
    }
    /**
     * 更新商品信息
     * 
     * @throws Exception 
     * 
     */	
    private void updateGoods(StorageGoodsDto storageGoodsDto,GoodsDto goodsDB,Integer storageType) throws Exception{
    	
    	GoodsDto goods = new GoodsDto();
    	
    	goods.setGoodsId(storageGoodsDto.getGoodsId());
    	goods.setAlarmNumberMax(storageGoodsDto.getAlarmNumberMax());
    	goods.setAlarmNumberMin(storageGoodsDto.getAlarmNumberMin());
    	
    	//入库 加库存
    	goods.setStorageTotalNumber(getStorageTotalNumber(storageType,goodsDB.getStorageTotalNumber(), storageGoodsDto.getStorageNumber()));
    	
    	//更新商铺库存信息
    	goodsDao.updateGoods(goods);
    	
    	//更新缓存
    	DataCacheApi.delObject("GoodsDto:"+goods.getGoodsId());
    	
    }
    /**
     * 
     * 
     * @Function: com.idcq.appserver.service.storage.StorageServiceImpl.getStorageTotalNumber
     * @Description:
     *
     * @param storageType 库存类型
     * @param beforTotalNumber 出入库钱库存
     * @param storageNumber 本次出入库数量
     * @return 出入库后数量
     */
    private Double getStorageTotalNumber(Integer storageType,Double beforTotalNumber,Double storageNumber){
    	Double storageTotalNumber  = 0D;
    	//入库、盘点 加库存
    	if(11==storageType || 12==storageType || 1==storageType || -1==storageType || 0==storageType){
    		storageTotalNumber = NumberUtil.add(beforTotalNumber, storageNumber);
    	}
    	//出库 减库存
    	if(13==storageType || 14==storageType ){
    		storageTotalNumber = NumberUtil.sub(beforTotalNumber, storageNumber);
    	}
		return storageTotalNumber;
    }
    
	/**
	 * 记录出库单信息
	 * 
	 */
	public Long insertShopStorageNote(OperateShopStorageDto operateShopStorage)
			throws Exception {
		
		ShopStorageNoteDto shopStorageNoteDto = new ShopStorageNoteDto();
		// 出入库单号
		String storageNo = operateShopStorage.getStorageNo();
		Integer storageType = operateShopStorage.getStorageType();
		if (StringUtils.isBlank(storageNo)) {
			shopStorageNoteDto.setStorageNo(getStorageNo(storageType,operateShopStorage.getShopId()));
		}
		else{
			
			shopStorageNoteDto.setStorageNo(storageNo);
		}
		if(operateShopStorage.getTotalPrice()!=null){// 是否需要计算总价
			shopStorageNoteDto.setTotalPrice(operateShopStorage.getTotalPrice());
		}
		
		shopStorageNoteDto.setCreateTime(new Date());
		shopStorageNoteDto.setOperaterId(operateShopStorage.getOperaterId());
		shopStorageNoteDto.setOperaterName(operateShopStorage.getOperaterName());
		shopStorageNoteDto.setShopId(operateShopStorage.getShopId());
		shopStorageNoteDto.setStorageRemark(operateShopStorage.getStorageRemark());
		shopStorageNoteDto.setStorageTime(new Date());
		shopStorageNoteDto.setStorageType(storageType);
		shopStorageNoteDto.setBuyer(operateShopStorage.getBuyer());
		shopStorageNoteDto.setVender(operateShopStorage.getVender());
		
		shopStorageNoteDao.insertShopStorageNote(shopStorageNoteDto);
		
		return shopStorageNoteDto.getStorageId();
	}
	
	
	/**
	 * 记录出库单信息
	 * 
	 */
	public Long updateShopStorageNote(OperateShopStorageDto operateShopStorage)
			throws Exception {
		
		ShopStorageNoteDto shopStorageNoteDto = new ShopStorageNoteDto();
		// 出入库单号
		String storageNo = operateShopStorage.getStorageNo();
		Integer storageType = operateShopStorage.getStorageType();
		if (StringUtils.isBlank(storageNo)) {
			shopStorageNoteDto.setStorageNo(getStorageNo(storageType,operateShopStorage.getShopId()));
		}
		else{
			
			shopStorageNoteDto.setStorageNo(storageNo);
		}
		if(operateShopStorage.getTotalPrice()!=null){// 是否需要计算总价
			shopStorageNoteDto.setTotalPrice(operateShopStorage.getTotalPrice());
		}
		
		shopStorageNoteDto.setCreateTime(new Date());
		shopStorageNoteDto.setOperaterId(operateShopStorage.getOperaterId());
		shopStorageNoteDto.setOperaterName(operateShopStorage.getOperaterName());
		shopStorageNoteDto.setShopId(operateShopStorage.getShopId());
		shopStorageNoteDto.setStorageRemark(operateShopStorage.getStorageRemark());
		shopStorageNoteDto.setStorageTime(new Date());
		shopStorageNoteDto.setStorageType(storageType);
		shopStorageNoteDto.setBuyer(operateShopStorage.getBuyer());
		shopStorageNoteDto.setVender(operateShopStorage.getVender());
		
		shopStorageNoteDao.updateShopStorageNote(shopStorageNoteDto);
		
		return shopStorageNoteDto.getStorageId();
	}
	
    /**
     * 获取出入库单号
     * 
     * @throws Exception 
     * 
     */
	public String getStorageNo(Integer storageType,Long shopId) throws Exception {
		String storageNo = "";
	     /*
	      * storageType:
	      * 11=进货入库，12=其他入库
	      * 13=销售出库，14=其他出库
	      * 
	      */
		boolean isStorageNoExist=true;
		while (isStorageNoExist) {
			if(storageType==11 || storageType==12){
				storageNo  = FieldGenerateUtil.generatebitStorageNoId(CommonConst.STORAGE_NOPREFIX_RK+shopId);
			}
			if(storageType==13 || storageType==14){
				storageNo  = FieldGenerateUtil.generatebitStorageNoId(CommonConst.STORAGE_NOPREFIX_CK+shopId);
			}
			
			isStorageNoExist = isStorageNoExist(storageNo,shopId);
		}
		
		return storageNo;
	}
	
	public boolean isStorageNoExist(String storageNo,Long shopId ){
		int n  = shopStorageNoteDao.isStorageNoExist(storageNo,shopId);
		return n>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.storage.IStorageServcie#getShopStorageDetail(java.util.Map)
	 */
	@Override
	public PageModel getShopStorageDetail(Map<String, Object> parm) throws Exception {
		
		PageModel pageModel = new PageModel();
		
		//结果list
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		//总记录数
		Integer totalItem = 0;
		
		Integer isGroupBy = (Integer) parm.get("isGroupBy");
		//是否需要根据queryNo分组查询1=是，0=否。
		if(isGroupBy==0){//不分组查询
			
			totalItem = shopStorageNoteDao.getShopStorageCountByMap(parm);
			if(totalItem != null && totalItem != 0){
				resultList = shopStorageNoteDao.getShopStorageByMap(parm);
			}
			
		}
		if(isGroupBy==1){//分组查询
			
			//1、插入出入库记录  2、根据出入库id查询出入库单号详情
			totalItem = shopStorageNoteDao.getShopStorageNoteBeseInfoCount(parm);
			if(totalItem != null && totalItem != 0){
				List<Map<String, Object>> storageNoteList  = shopStorageNoteDao.getShopStorageNoteBeseInfo(parm);
				//查询组装返回结果
				resultList = getShopStorageDetailList(storageNoteList);
			}
			
		}
		
		pageModel.setList(resultList);
		pageModel.setTotalItem(totalItem);
		
		return pageModel;
	}
	/**
	 * 根据出入库id查询出入库单号详情
	 * 
	 * @Description:
	 *
	 * @return
	 *
	 */
	public List<Map<String, Object>> getShopStorageDetailList(List<Map<String, Object>> storageNoteList){
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(CollectionUtils.isNotEmpty(storageNoteList)){
			
			for (Map<String, Object> storageNoteDB : storageNoteList) {
				
				Long bizId = (Long) storageNoteDB.get("bizId");
				List<Map<String, Object>> detailList  = shopStorageDetailDao.getShopStorageDetailList(bizId, 17);//默认传出入库类型
//				for (Map<String, Object> map : detailList) {
//						String goodsProValuesIds = (String) map.get("goodsProValuesIds");
//						StringBuilder goodsProValuesNameBuilder = new StringBuilder("");
//						if(goodsProValuesIds!=null){
//							String[] ids = goodsProValuesIds.split("\\,");
//							for (String id : ids) {
//								String propertyValue = shopGoodsDao.getDtoProValueName(id);
//								if(propertyValue!=null){
//									goodsProValuesNameBuilder.append(propertyValue+",");
//								}
//							}
//						}
//						if(goodsProValuesNameBuilder!=null&&goodsProValuesNameBuilder.length()>0&&goodsProValuesNameBuilder.lastIndexOf(",")==goodsProValuesNameBuilder.length()-1){
//							map.put("goodsProValuesName", goodsProValuesNameBuilder.substring(0, goodsProValuesNameBuilder.length()-1));
//						}
//				}
				
				storageNoteDB.put("goodsList", detailList);
				//返回结果
				resultList.add(storageNoteDB);
			}
		}
		
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.storage.IStorageServcie#operateStorageCheck(com.idcq.appserver.dto.storage.OperateStorageCheckDto)
	 */
	@Override
	public Long operateStorageCheck(OperateStorageCheckDto operateStorageCheck)
			throws Exception {
		/**
		 * 1、增加盘点信息
		 * 2、增加盘点流水
		 * 3、盘点有误差的时候，进行增减库存，并记录库存流水，更新商品缓存
		 */
		Long storageCheckId = 0L;
		
		Long shopId = operateStorageCheck.getShopId();
		//单号
		String checkNo = operateStorageCheck.getCheckNo();
		if(StringUtils.isBlank(checkNo)){
			checkNo = FieldGenerateUtil.generatebitStorageNoId(CommonConst.STORAGE_NOPREFIX_PD + shopId);
		}
		
		//验证重复
		Integer count = shopStorageCheckNoteDao.queryStorageCheckNoExist(checkNo);
		if(count!=null && count>0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_REPEAT, CodeConst.MSG_REQUIRED_STORAGENO);
		}
		
		// 增加盘点信息 
		storageCheckId = insertShopStorageCheckNote(operateStorageCheck, checkNo);
		
		// 增加盘点流水
		insertShopStorageCheckDeail(operateStorageCheck, checkNo,storageCheckId);
		
		return storageCheckId;
		
	}
	/**
	 * 
	 * 增加盘点基本信息
	 * @param operateStorageCheck
	 * @return
	 * @throws Exception 
	 *
	 */
	public Long insertShopStorageCheckNote(OperateStorageCheckDto operateStorageCheck,String checkNo) throws Exception{                                                                                                                                                                                                               
		
		ShopStorageCheckNoteDto shopStorageCheckNote = new ShopStorageCheckNoteDto();
		shopStorageCheckNote.setCreateTime(new Date());
		shopStorageCheckNote.setGoodsTotalPrice(0d);
		shopStorageCheckNote.setOperaterId(operateStorageCheck.getOperaterId());
		shopStorageCheckNote.setOperaterName(operateStorageCheck.getOperaterName());
		shopStorageCheckNote.setShopId(operateStorageCheck.getShopId());
		shopStorageCheckNote.setStorageCheckNo(checkNo);
		shopStorageCheckNote.setStorageCheckRemark(operateStorageCheck.getRemark());
		shopStorageCheckNote.setStorageCheckTime(DateUtils.parse(operateStorageCheck.getCheckTime()));
		
		shopStorageCheckNoteDao.insertShopStorageCheckNote(shopStorageCheckNote);
		
		return shopStorageCheckNote.getStorageCheckId();
		
	}
	/**
	 * 
	 * 增加盘点流水
	 * 
	 * @Function: com.idcq.appserver.service.storage.StorageServiceImpl.insertShopStorageCheckNote
	 * @Description:
	 *
	 * @param operateStorageCheck
	 * @param checkNo
	 * @return
	 * @throws Exception
	 */
	public void insertShopStorageCheckDeail(OperateStorageCheckDto operateStorageCheck,String storageCheckNo,Long storageCheckId) throws Exception{                                                                                                                                                                                                               
		
		List<ShopStorageCheckDetailDto> shopStorageCheckDetails = new ArrayList<ShopStorageCheckDetailDto>();
		//出入库流水商品
		List<StorageGoodsDto>  goodsList = new ArrayList<StorageGoodsDto>();
		//盘点商品
		List<StorageCheckGoodsDto> storageCheckGoodsDto  = operateStorageCheck.getGoodsList();
		for (StorageCheckGoodsDto checkGoods : storageCheckGoodsDto) {
			
			//盘后数量
			Double afterNumber = checkGoods.getStoragAfterNumber()==null ? 0d : checkGoods.getStoragAfterNumber();
			//盘前数量
			Double beforeNumber = checkGoods.getCheckNum()==null ? 0D : checkGoods.getCheckNum();
			//差异数量
			Double differenceNum = NumberUtil.sub(afterNumber, beforeNumber);
			//类型
			Integer storageCheckType = getStorageCheckType(differenceNum);
			
			ShopStorageCheckDetailDto shopStorageCheckDetailDto = new ShopStorageCheckDetailDto();
			shopStorageCheckDetailDto.setCheckNum(afterNumber);
			shopStorageCheckDetailDto.setCheckTotalPrice(0D);
			shopStorageCheckDetailDto.setGoodsTotalPrice(0D);
			shopStorageCheckDetailDto.setStoragePrice(0D);
			shopStorageCheckDetailDto.setCreateTime(new Date());
			shopStorageCheckDetailDto.setDifferenceNum(differenceNum);
			shopStorageCheckDetailDto.setGoodsId(checkGoods.getGoodsId());
			shopStorageCheckDetailDto.setGoodsTotalNum(beforeNumber);
			shopStorageCheckDetailDto.setShopId(operateStorageCheck.getShopId());
			shopStorageCheckDetailDto.setStorageCheckId(storageCheckId);
			shopStorageCheckDetailDto.setStorageCheckNo(storageCheckNo);
			shopStorageCheckDetailDto.setStorageCheckRemark(checkGoods.getStorageCheckRemark());
			shopStorageCheckDetailDto.setStorageCheckType(storageCheckType);
			shopStorageCheckDetails.add(shopStorageCheckDetailDto);
			
			// 修改库存信息
			StorageGoodsDto storageGoods = new StorageGoodsDto();
			storageGoods.setGoodsId(checkGoods.getGoodsId());
			storageGoods.setStorageBillRemark(checkGoods.getStorageCheckRemark());
			storageGoods.setStoragePrice(0D);
			storageGoods.setStorageNumber(differenceNum);
			storageGoods.setChangeType(storageCheckType);
			goodsList.add(storageGoods);
		}
		//插入盘点详情
		shopStorageCheckDetailDao.batchInsertShopStorageCheckDetail(shopStorageCheckDetails);
		//批量插入库存详情
		operateShopStorageByCheck(operateStorageCheck,goodsList, storageCheckNo, storageCheckId);
	}
	/**
	 * 盘点：处理出入库流水
	 * 
	 * @Function: com.idcq.appserver.service.storage.StorageServiceImpl.operateShopStorageByCheck
	 * @Description:
	 *
	 * @param operateStorageCheck
	 * @param storageCheckNo
	 * @throws Exception
	 */
	public void operateShopStorageByCheck(OperateStorageCheckDto operateStorageCheck,List<StorageGoodsDto>  goodsList,String storageCheckNo,Long storageCheckId) throws Exception{
		
		OperateShopStorageDto operateShopStorage = new OperateShopStorageDto();
		
		
		operateShopStorage.setOperaterId(operateStorageCheck.getOperaterId());
		operateShopStorage.setOperaterName(operateStorageCheck.getOperaterName());
		operateShopStorage.setShopId(operateStorageCheck.getShopId());
		operateShopStorage.setStorageNo(storageCheckNo);
		operateShopStorage.setStorageRemark(operateStorageCheck.getRemark());
		operateShopStorage.setStorageTime(operateStorageCheck.getCheckTime());
		operateShopStorage.setTotalPrice(0D);
		operateShopStorage.setGoodsList(goodsList);
		
		// 构造参数
		batchInsertShopStorageDetail(operateShopStorage, storageCheckId,18);// '业务主键类型:出入库记录=17,盘点记录=18'
		
	}
	/**
	 * 获取盘点类型
	 * 
	 * @Function: com.idcq.appserver.service.storage.StorageServiceImpl.getStorageCheckType
	 * @Description:
	 *
	 * @param differenceNum
	 * @return
	 */
	private Integer getStorageCheckType(Double differenceNum){
		Integer storageCheckType = 0;
		/*盘点类型:盘盈=1,盘亏=-1,盘准=0（数目符合）*/
		if(differenceNum>0){
			storageCheckType = 1;
		}
		if(differenceNum==0){
			storageCheckType = 0;
		}
		if(differenceNum<0){
			storageCheckType = -1;
		}
		
		return storageCheckType;
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.storage.IStorageServcie#getShopStorageCheckDetail(java.util.Map)
	 */
	@Override
	public PageModel getShopStorageCheckDetail(Map<String, Object> parms)
			throws Exception {
		
		PageModel pageModel = new PageModel();
		Integer  totalItem = shopStorageCheckNoteDao.getShopStorageCheckNoteBeseInfoCount(parms);
		if(totalItem != null){
			
			List<Map<String, Object>>  resultList = shopStorageCheckNoteDao.getShopStorageCheckNoteBeseInfo(parms);
			
			Long goodsId = (Long) parms.get("goodsId");
			resultList = updateResultList(resultList,goodsId);
			pageModel.setList(resultList);
			pageModel.setTotalItem(totalItem);
			
		}
		
		return pageModel;
	}
	
	private List<Map<String, Object>> updateResultList(List<Map<String, Object>>  storageCheckList,Long goodsId) throws Exception{
		
		
		for (Map<String, Object> storageCheck : storageCheckList) {
			
			storageCheck.remove("goodsId");
			Long checkDetailId = (Long) storageCheck.get("storageCheckId");
			List<Map<String, Object>>  goodsList = shopStorageCheckDetailDao.getShopStorageCheckDetailList(checkDetailId,goodsId);
			storageCheck.put("goodsList", updateGoodsList(goodsList));
		}
		
		
		return storageCheckList;
	}
	private List<Map<String, Object>> updateGoodsList(List<Map<String, Object>>  goodsList) throws Exception{
		
		if(CollectionUtils.isNotEmpty(goodsList)){
			
			for (Map<String, Object> goods : goodsList) {
				//查询缓存
				GoodsDto goodsDB = goodsDao.getGoodsById((Long)goods.get("goodsId"));
				goods.put("unitName",goodsDB.getUnitName() );
			}
			
		}

		
		return goodsList;
	}

}
