package com.idcq.appserver.service.shop;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dao.shop.IShopTechnicianDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopTechnicianDto;
import com.idcq.appserver.dto.shop.ShopTechnicianRefGoodsDto;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.idianmgr.dao.shop.ICategoryDao;

/**
 * 技师service
 * @author Administrator
 * 
 * @date 2015年7月29日
 * @time 下午5:51:29
 */
@Service
public class ShopTechnicianServiceImpl implements IShopTechnicianService{
	
	@Autowired
	public IShopTechnicianDao shopTechnicianDao;
	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private ICategoryDao categoryDao;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IOrderShopRsrcDao orderShopRsrcDao;
	@Override
	public int queryTechnicianExists(Long id) throws Exception {
		return this.shopTechnicianDao.queryTechnicianExists(id);
	}

	@Override
	public Long insertAndUpdateShopTechnician(ShopTechnicianDto shopTechnicianDto)
			throws Exception {
		Integer  operateType = shopTechnicianDto.getOperateType();
		String goodsGroupIds = shopTechnicianDto.getGoodsGroupIds();
		Long techId = shopTechnicianDto.getTechId();
		//增加
		if(0==operateType){
			shopTechnicianDao.insertShopTechnician(shopTechnicianDto);
			Long dbTechId = shopTechnicianDto.getTechId();
			//增加关联关系
			addShopTechRefGoods(dbTechId, goodsGroupIds);
//			//增加附件关联表数据
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("attachementId", shopTechnicianDto.getAttachementId());
//			//增加技师返回的技师id
//			map.put("bizId",dbTechId );
//			//技师类型
//			map.put("bizType",10);
//			//缩略图
//			map.put("picType",1);
//			categoryDao.insertAttachmentRelation(map);
		}
		else{
			//更新
			Integer  count = shopTechnicianDao.updateShopTechnician(shopTechnicianDto);
			if(count!=0){
				//删除关联关系
				ShopTechnicianRefGoodsDto strf = new ShopTechnicianRefGoodsDto();
				strf.setTechnicianId(techId);
				shopTechnicianDao.deleteShopTechRefGoods(strf);
				//重新赋值
				addShopTechRefGoods(techId, goodsGroupIds);
				
//				//修改附件关联表数据
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("attachementId", shopTechnicianDto.getAttachementId());
//				//增加技师返回的技师id
//				map.put("bizId", techId);
//				//技师类型
//				map.put("bizType",10);
//				//缩略图
//				map.put("picType",1);
//				categoryDao.updateAttachmentRelation(map);
			}
		}
		return shopTechnicianDto.getTechId();
	}
	/**
	 * 增加技师和商品族关联关系
	 * 
	 * @Function: com.idcq.appserver.service.shop.ShopTechnicianServiceImpl.deailGroupIds
	 * @Description:
	 *
	 * @param goodsGroupIds
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @throws Exception 
	 * @date:2015年7月30日 下午4:42:28
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	private void addShopTechRefGoods(Long techId,String goodsGroupIds) throws Exception{
		List<ShopTechnicianRefGoodsDto> list = new ArrayList<ShopTechnicianRefGoodsDto>();
		if(StringUtils.isNotBlank(goodsGroupIds)){
			String[] groupIds = goodsGroupIds.split(",");
			for (String groupIdStr : groupIds) {
				ShopTechnicianRefGoodsDto strf = new ShopTechnicianRefGoodsDto();
				Long groupId = Long.parseLong(groupIdStr);
				strf.setTechnicianId(techId);
				strf.setGoodsGroupId(groupId);
				list.add(strf);
			}
			shopTechnicianDao.insertShopTechRefGoods(list);
		}
	}


	@Override
	public void deleteShopTechnician(Long shopId ,String techIdStr)
			throws Exception {
		//1、删除技师主表数据
		List<Long> techIds = deleteBatcheShopTechnician(shopId, techIdStr);
		//2、删除关联表数据
		shopTechnicianDao.deleteBatcheShopTechRefGoods(techIds);
	}
	/**
	 * 批量删除技师数据 
	 * @throws Exception 
	 */
	public List<Long> deleteBatcheShopTechnician(Long shopId ,String techIdsStr) throws Exception{
		List<Long> ids = new ArrayList<Long>();
		if(StringUtils.isNotBlank(techIdsStr)){
			String[] techIds = techIdsStr.split(",");
			for (String techIdStr : techIds) {
				Long techId = Long.parseLong(techIdStr);
				ids.add(techId);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", ids);
			map.put("shopId", shopId);
			//shopTechnicianDao.deleteBatchShopTechnician(map);
			//软删除，更新is_valid为0
			shopTechnicianDao.updateBatchShopTechnician(map);
	}
		return ids;
}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopTechnicianService#deleteShopTechRefGoods(com.idcq.appserver.dto.shop.ShopTechnicianRefGoodsDto)
	 */
	@Override
	public Integer deleteShopTechRefGoods(
			ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception {
		return shopTechnicianDao.deleteShopTechRefGoods(shopTechnicianRefGoodsDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopTechnicianService#getTechnicianServiceItems(java.util.Map)
	 */
	@Override
	public PageModel getTechnicianServiceItems(Map<String, Object> map)
			throws Exception {
		PageModel pageModel = new PageModel();
		Integer count = shopTechnicianDao.getTechnicianServiceItemsCount(map);
		if(0!=count){
			List<Map<String, Object>>  dbList = shopTechnicianDao.getTechnicianServiceItems(map);
			dbList = updateTechList(dbList);
			pageModel.setList(dbList);
			pageModel.setTotalItem(count);;
		}
		return pageModel;
	}
	private List<Map<String, Object>> updateTechList(List<Map<String, Object>> mapList) throws Exception{
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> mapDB : mapList) {
			Long goodsGroupId = (Long) mapDB.get("goodsGroupId");
			BigDecimal minPrice = (BigDecimal) mapDB.get("minPrice");
			BigDecimal maxPrice = (BigDecimal) mapDB.get("maxPrice");
			String standardPrice= "";
			//价格范围
			//如果最低价和最高价一样，取最低价返回
			if(0==minPrice.compareTo(maxPrice)){
				standardPrice = minPrice.toString();
			}
			else{
				standardPrice = minPrice.toString()+"-"+maxPrice.toString();
			}
			mapDB.put("standardPrice", standardPrice);
			//移除多余key
			mapDB.remove("minPrice");
			mapDB.remove("maxPrice");
			//获取附件缓存信息
			//查询图片关联表
			AttachmentRelationDto attPar = new AttachmentRelationDto();
			attPar.setBizId(goodsGroupId); 
			//商品族
			attPar.setBizType(9);
			//缩略图
			attPar.setPicType(1);
			String fileUrl = "";
			List<AttachmentRelationDto> listAtt = attachmentRelationDao.findByCondition(attPar);
			if(CollectionUtils.isNotEmpty(listAtt)){
				fileUrl = listAtt.get(0).getFileUrl();
			}
			mapDB.put("goodsLogo", FdfsUtil.getFileProxyPath(fileUrl)); 
			newList.add(mapDB);
		}
		return newList;
		
	}
	/**
	 * 获取附件信息
	 * @param attachmentId
	 * @return
	 * @throws Exception 
	 */
	public String getAttachment(Long attachmentId) throws Exception{
		String fileUrl = "";
		//缓存
		Attachment attachment = attachmentDao.queryAttachmentById(attachmentId);
		if(null!=attachment){
			fileUrl = attachment.getFileUrl();
		}
		return fileUrl;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopTechnicianService#getScheduleSetting(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getScheduleSetting(Map<String, Object> mapPar)
			throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		String timesPar = (String) mapPar.get("times");
		//如果时间有值，查询订单数即可
		if(StringUtils.isNotBlank(timesPar)){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Integer num = shopTechnicianDao.getScheduleSettingCount(mapPar);
			resultMap.put("num", num);
			resultMap.put("times", timesPar);
			resultMap.put("status", 1);
			resultList.add(resultMap);
		}
		else{
			String workTime = shopTechnicianDao.getTechWorkTimeByMap(mapPar);
			if(StringUtils.isNotBlank(workTime)){
				//order_shop_resource 已经占用资源
				List<Map<String, Object>>  resourceList =  shopTechnicianDao.getScheduleSetting(mapPar);
				//处理返回结果
				String[]  workArray = workTime.split("-");
				Time begin = Time.valueOf(workArray[0]+":00");
				Time end = Time.valueOf(workArray[1]+":00");
				//合并list
				resultList = getTimeInterval(begin, end, resourceList);
			}
		}
		return resultList;
	}
	/**
	 * 拆分时间点
	 * @param begin
	 * @param end
	 * @param start 顺序标记
	 * @return
	 */
	public List<Map<String, Object>> getTimeInterval(Time begin,Time end,List<Map<String, Object>>  resourceList){
		DateFormat df = new SimpleDateFormat("HH:mm");
		Long beginTime=begin.getTime();
		Long endTime=end.getTime();
		Long interval=(long) (30*60*1000);//时间间隔
		List<Map<String, Object>> timeInterval=new ArrayList<Map<String, Object>>();
		Map<String, Object> temp = new HashMap<String, Object>();
		Long tempVal = beginTime;
		temp.put("times", df.format(beginTime));
		//闲置
		temp.put("status", 0);
		//无接单数
		temp.put("num", 0);
		timeInterval.add(temp);
		while (true) {
			temp = new HashMap<String, Object>();
			tempVal = tempVal + interval;
			if (endTime >= tempVal) {
				String formatTime = df.format(tempVal);
				//默认值
				temp.put("times",formatTime);
				temp.put("status", 0);
				temp.put("num", 0);
				for (Map<String, Object> resourceDB : resourceList) {
					Time timesDB = (Time) resourceDB.get("times");
					Long statusDB = (Long) resourceDB.get("status");
					//Integer numDB = (Integer) resourceDB.get("num");
					if(StringUtils.equals(formatTime+":00", timesDB.toString())){
						String timesDBstr = timesDB.toString();
						timesDBstr = timesDBstr.substring(0,timesDBstr.lastIndexOf(":"));
						//已预定
						temp.put("times",timesDBstr);
						temp.put("status", statusDB);
						temp.put("num", 0);
					}
				}
				timeInterval.add(temp);
			}else{
				break;
			}
		}
		return timeInterval;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopTechnicianService#getTechnicianList(java.util.Map)
	 */
	@Override
	public PageModel getTechnicianList(Map<String, Object> map)
			throws Exception {
		PageModel pageModel = new PageModel();
		String serviceTime = (String) map.get("serviceTime");
		Long shopId = (Long) map.get("shopId");
		//避免表关联太多，先将查询条件查询出来
		if(StringUtils.isNotBlank(serviceTime)){
			Integer classType = shopTechnicianDao.getClassesTypeByTime(serviceTime,shopId);
			map.put("classesType",classType);
		}
		Integer count = shopTechnicianDao.getTechnicianListCount(map);
		if(0!=count){
			List<Map<String, Object>>  dbList = shopTechnicianDao.getTechnicianList(map);
			dbList = updateTechDBList(dbList);
			pageModel.setList(dbList);
			pageModel.setTotalItem(count);
		}
		return pageModel;
	}
	/**
	 * 更新商铺技师返回结果数据
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	private List<Map<String, Object>> updateTechDBList(List<Map<String, Object>> dbList) throws Exception{
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> mapDB : dbList) {
			Long techId = (Long) mapDB.get("techId");
			//查询图片关联表
			AttachmentRelationDto attPar = new AttachmentRelationDto();
			attPar.setBizId(techId);
			//技师
			attPar.setBizType(10);
			//缩略图
			attPar.setPicType(1);
			String fileUrl = "";
			List<AttachmentRelationDto> listAtt = attachmentRelationDao.findByCondition(attPar);
			if(CollectionUtils.isNotEmpty(listAtt)){
				fileUrl = listAtt.get(0).getFileUrl();
			}
			//查询服务项目数
			Integer serviceItemNum = shopTechnicianDao.getTechServiceCount(techId);
			mapDB.put("serviceItemNum", serviceItemNum);
			if(StringUtils.isNotBlank(fileUrl)){
				fileUrl = FdfsUtil.getFileProxyPath(fileUrl);
			}
			//获取技师logo缓存信息
			mapDB.put("techLogo", fileUrl);
			newList.add(mapDB);
		}
		return newList;
		
	}

	public String getTechnicianName(Long id) throws Exception {
		return this.shopTechnicianDao.getTechnicianName(id);
	}

	public int updateTechnicianWorkStatus(List<Long> techIds,int workStatus)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("workStatus", workStatus);
		param.put("techIds", techIds);
		return shopTechnicianDao.updateTechnicianWorkStatus(param);
	}

	public List<Long> getTechIdList(
			Map<String, Object> param) throws Exception {
		return shopTechnicianDao.getTechIdList(param);
	}

	public int updateTechnicianWorKStatusByOrderId(String orderId,
			int workStatus) throws Exception {
		//根据订单状态、行业查询订单资源对应的技师编号
		Long techId = orderShopRsrcDao.getTechIdByOrderId(orderId, CommonConst.SHOP_RSR_TYPE_JS);
		//将该技师的工作状态置为workStatus
		if (techId != null) {
			if (workStatus == CommonConst.TECH_STATUS_KXZ) {
				//如果是释放技师资源，需要判断当前技师是否还有未完成订单
				//订单状态；已开单-1，退单中-4
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("orderId", orderId);
				param.put("resourceType", CommonConst.SHOP_RSR_TYPE_JS);
				param.put("techId", techId);
				Integer count = orderShopRsrcDao.getTechServerOrderSrc(param);
				if(count.intValue() > 0){
					return 0;
				}
			}
			List<Long> techIds = new ArrayList<Long>();
			techIds.add(techId);
			updateTechnicianWorkStatus(techIds, workStatus);
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopTechnicianService#getTechnicianOrderNum(java.util.Map)
	 */
	@Override
	public Map<String, Object> getTechnicianOrderNum(Map<String, Object> param)
			throws Exception {
		return shopTechnicianDao.getTechnicianOrderNum(param);
	}

	@Override
	public Integer validateTechExit(Map<String, Object> map) throws Exception {
		return shopTechnicianDao.validateTechExit(map);
	}
	
}
