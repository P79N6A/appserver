package com.idcq.appserver.service.storage;

import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.storage.OperateShopStorageDto;
import com.idcq.appserver.dto.storage.OperateStorageCheckDto;

public interface IStorageServcie {

	
	/**
	 * PCS1：商铺商品出入库
	 * @param operateShopStorage
	 * @return
	 * @throws Exception
	 */
	public void operateShopStorage(OperateShopStorageDto operateShopStorage)throws Exception;
	/**
	 * 查询商品库存变动明细
	 * 
	 * @param operateShopStorage
	 * @return
	 * @throws Exception
	 */
	public PageModel getShopStorageDetail(Map<String, Object> parm)throws Exception;
	
	/**
	 * PCS27：商铺商品盘点接口
	 * @param operateStorageCheck
	 * @return 盘点id
	 * @throws Exception
	 */
	public Long operateStorageCheck(OperateStorageCheckDto operateStorageCheck)throws Exception;


	void insertShopStorage(GoodsDto goodsDto) throws Exception;
	
	/**
	 *根据orderGoods更新单个库存信息
	 *
	 * @param orderGoods
	 * @throws Exception
	 *
	 */
	public void insertShopStorageByOrderGoods(OrderGoodsDto orderGoods,Long storageId) throws Exception;
	/**
	 *根据orderId 更新订单所有商品库存信息
	 *
	 * @param orderGoods
	 * @throws Exception
	 *
	 */
	public void insertShopStorageByOrderId(String orderId,Long shopId) throws Exception;
	public boolean isStorageNoExist(String storageNo, Long shopId);
	/**
	 *根据orderId 更新订单所有商品库存信息
	 *
	 * @param parms
	 * @throws Exception
	 *
	 */
	PageModel getShopStorageCheckDetail(Map<String, Object> parms) throws Exception; 

}
