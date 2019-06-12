package com.idcq.appserver.service.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.IUnitDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserFavoriteDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserFavoriteDto;
import com.idcq.appserver.dto.user.UserFavoriteHistoryDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;

@Service
public class UserFavoriteServiceImpl implements IUserFavoriteService {

	@Autowired
	private IUserFavoriteDao userFavoriteDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IGoodsDao goodsDao;
	@Autowired
	private IUnitDao unitDao;
	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;

	@Override
	public void favorite(UserFavoriteDto userFavoriteDto) throws Exception {
		Integer operType = userFavoriteDto.getOperType();
		Integer bizType = userFavoriteDto.getBizType();
		Long bizId = userFavoriteDto.getBizId();
		// String favoriteUrl = getFavoriteUrl( bizType, bizId);TODO
		// 目前接口文档没有该参数，故现在去除
		// userFavoriteDto.setFavoriteUrl(favoriteUrl);
		//商品属于商品族，后台自动收藏商品族。
		if(1==operType&&(1==bizType||2==bizType)){
			Long goodsGroupId = goodsDao.queryGoodGroupExist(bizId);
			if(null!=goodsGroupId){
				userFavoriteDto.setBizId(goodsGroupId);
				//商品族类型：2
				userFavoriteDto.setBizType(2);
			}
		}
		// 1、判断重复收藏2、数据查询用户收藏信息,保存至收藏历史记录表
		UserFavoriteDto ufDB = userFavoriteDao.getFavoriteInfo(userFavoriteDto);
		// 收藏
		if (1 == operType) {
			// 不能重复增加
			if (null == ufDB) {
				userFavoriteDao.insertUserFavorite(userFavoriteDto);
			} else {
				throw new ServiceException(CodeConst.CODE_RESUBMIT_ERROR,
						"不能重复收藏商铺或商品");
			}

		}
		// 取消收藏
		if (2 == operType) {
			if (null != ufDB) {
				// 删除收藏信息
				userFavoriteDao.deleteUserFavorite(userFavoriteDto);
				// 增加收藏历史记录
				UserFavoriteHistoryDto userFavoriteHistory = new UserFavoriteHistoryDto();
				userFavoriteHistory.setCreateTime(ufDB.getCreateTime());
				userFavoriteHistory.setFavoriteUrl(ufDB.getFavoriteUrl());
				userFavoriteHistory.setFavoriteId(ufDB.getFavoriteId());
				userFavoriteHistory.setUserId(userFavoriteDto.getUserId());
				userFavoriteHistory.setBizId(userFavoriteDto.getBizId());
				userFavoriteHistory.setBizType(userFavoriteDto.getBizType());
				userFavoriteDao.insertUserFavoriteHistory(userFavoriteHistory);
			} else {
				throw new ServiceException(CodeConst.CODE_RESUBMIT_ERROR,
						"不能重复取消商铺或商品");
			}
		}
	}

	/**
	 * 获取收藏的logo
	 * 
	 * @param bizType
	 * @param bizId
	 * @return
	 * @throws Exception
	 */
	private String getFavoriteUrl(Integer bizType, Long bizId) throws Exception {
		String favoriteUrl = "";
		// 商铺
		if (0 == bizType) {
			ShopDto shop = shopDao.getShopById(bizId);
			if (null != shop) {
				String logoUrl = shop.getShopLogoUrl();
				if (StringUtils.isNotBlank(logoUrl)){
					favoriteUrl = FdfsUtil.getFileProxyPath(logoUrl);
				}
			}
		}
		// 商品
		if (0 == bizType) {
			String logo1 = goodsDao.getGoodsLogo1ById(bizId);
			if (StringUtils.isNotBlank(logo1)) {
				favoriteUrl = FdfsUtil.getFileProxyPath(logo1);
			}
		}
		return favoriteUrl;
	}

	@Override
	public Map<String, Object> getFavoriteStatus(UserFavoriteDto userFavoriteDto)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserFavoriteDto ufDB = userFavoriteDao.getFavoriteInfo(userFavoriteDto);
		// 存在，则表示已经收藏
		if (null != ufDB) {
			resultMap.put("favoriteStatus", 1);
		} else {
			resultMap.put("favoriteStatus", 0);
		}
		return resultMap;
	}

	@Override
	public PageModel getMyFavoriteShop(Map<String, Object> map)
			throws Exception {
		Double longitude = (Double) map.get("longitude");
		Double latitude = (Double) map.get("latitude");
		PageModel pageModel = new PageModel();
		Integer count = userFavoriteDao.getMyFavoriteShopCount(map);
		if (count != 0) {
			List<Map<String, Object>> resultList = userFavoriteDao
					.getMyFavoriteShop(map);
			resultList = updateFavoriteShopList(resultList, longitude, latitude);
			pageModel.setTotalItem(count);
			pageModel.setList(resultList);
		}
		return pageModel;
	}

	/**
	 * 重新组装返回结果-shopMap
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> updateFavoriteShopList(
			List<Map<String, Object>> resultList, Double parLongitude,
			Double parLatitude) throws Exception {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> mapDB : resultList) {
			// 商铺缓存数据
			Long shopId = (Long) mapDB.get("shopId");
			// 经纬度
			BigDecimal longitudeDB = (BigDecimal) mapDB.get("longitude");
			BigDecimal latitudeDB = (BigDecimal) mapDB.get("latitude");
			// 当经纬度都有值才计算经纬度
			if (parLongitude != null && parLatitude != null
					&& longitudeDB != null && latitudeDB != null) {
				Integer distance = (int) computeDistance(parLongitude,
						parLatitude, longitudeDB.doubleValue(),
						latitudeDB.doubleValue());
				mapDB.put("distance", distance);
			}
			Map<String, Object> shopMap = getShopInfo(shopId);
			mapDB.putAll(shopMap);
			newList.add(mapDB);
		}
		return newList;
	}

	/**
	 * 获取缓存商铺数据
	 * 
	 * @param unitId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getShopInfo(Long shopId) throws Exception {
		Map<String, Object> shopMap = new HashMap<String, Object>();
		// 查询缓存
		ShopDto shopDB = shopDao.getShopExtendByIdAndStatus(shopId, null);
		String logoUrl = "";
		if (null != shopDB) {
			logoUrl = DataCacheApi.get(CommonConst.KEY_SHOP_LOGO + shopId);
			// 数据库
			if (StringUtils.isBlank(logoUrl)) {
				AttachmentRelationDto attPar = new AttachmentRelationDto();
				// 商铺
				attPar.setBizType(1);
				// 缩略图
				attPar.setPicType(1);
				// 商铺id
				attPar.setBizId(shopId);
				// 缩略图
				List<AttachmentRelationDto> listDB = attachmentRelationDao
						.findByCondition(attPar);
				if (CollectionUtils.isNotEmpty(listDB)) {
					logoUrl = FdfsUtil.getFileProxyPath(listDB.get(0)
							.getFileUrl());
					DataCacheApi.setex(CommonConst.KEY_SHOP_LOGO + shopId,
							logoUrl, 86400);// 一天的缓存时间
				}
			}
			shopMap.put("shopLogoUrl", logoUrl);
			shopMap.put("redPacketFlag", shopDB.getRedPacketFlag());
			shopMap.put("cashCouponFlag", shopDB.getCashCouponFlag());
			shopMap.put("couponFlag", shopDB.getCouponFlag());
			shopMap.put("timedDiscountFlag", shopDB.getTimedDiscountFlag());
		}
		return shopMap;
	}

	@Override
	public PageModel getMyFavoriteGoods(Map<String, Object> map)
			throws Exception {
		PageModel pageModel = new PageModel();
		Integer count = userFavoriteDao.getMyFavoriteGoodsCount(map);
		if (count != 0) {
			List<Map<String, Object>> mapList = userFavoriteDao
					.getMyFavoriteGoods(map);
			mapList = updateFavoriteGoodsList(mapList);
			pageModel.setTotalItem(count);
			pageModel.setList(mapList);
		}
		return pageModel;
	}

	/**
	 * 构造maplist 为了避免关联多表查询，故缓存中取相应数据
	 * 
	 * @param mapList
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> updateFavoriteGoodsList(
			List<Map<String, Object>> mapList) throws Exception {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> mapDB : mapList) {
			Long shopId = (Long) mapDB.get("shopId");
			Long goodsId = (Long) mapDB.get("goodsId");
			Long unitId = (Long) mapDB.get("unit");

			// 缓存查询单位信息
			String unitName = getUnit(unitId);
			mapDB.put("unit", unitName);
			String fileUrl = getAttachment(goodsId);
			mapDB.put("goodsLogo", FdfsUtil.getFileProxyPath(fileUrl));
			// 缓存查询商铺信息
			ShopDto shopDto = shopDao.getNormalShopById(shopId);
			if (null != shopDto) {
				mapDB.put("shopName", shopDto.getShopName());
				mapDB.put("address", shopDto.getAddress());
			}
			newList.add(mapDB);
		}
		return newList;

	}

	/**
	 * 获取单位信息
	 * 
	 * @param unitId
	 * @return
	 * @throws Exception
	 */
	public String getUnit(Long unitId) throws Exception {
		String unitName = "";
		Unit unit = unitDao.queryUnitById(unitId);
		if (null != unit) {
			unitName = unit.getUnitName();
		}
		return unitName;
	}

	/**
	 * 获取附件信息
	 * 
	 * @param unitId
	 * @return
	 * @throws Exception
	 */
	public String getAttachment(Long goodsId) throws Exception {
		String fileUrl = "";
		// 数据库
		AttachmentRelationDto attPar = new AttachmentRelationDto();
		// 商品
		attPar.setBizType(8);
		// 缩略图
		attPar.setPicType(1);
		// 商铺id
		attPar.setBizId(goodsId);
		// 缩略图
		List<AttachmentRelationDto> listDB = attachmentRelationDao.findByCondition(attPar);
		if(CollectionUtils.isNotEmpty(listDB)){
			fileUrl = listDB.get(0).getFileUrl();
		}
		return fileUrl;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param longitude1
	 *            第一点经度
	 * @param latitude1
	 *            第一点纬度
	 * @param longitude2
	 *            第二点经度
	 * @param latitude2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double computeDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double a, b, R;
		R = 6371393; // 地球半径
		latitude1 = latitude1 * Math.PI / 180.0;
		latitude2 = latitude2 * Math.PI / 180.0;
		a = latitude1 - latitude2;
		b = (longitude1 - longitude2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1)
						* Math.cos(latitude2) * sb2 * sb2));
		return d;
	}

}
