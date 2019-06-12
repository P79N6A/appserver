package com.idcq.appserver.service.discount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.discount.IShopTimeDiscountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
@Service
public class ShopTimeDiscountServiceImpl implements IShopTimeDiscountService {
	@Autowired
	private IShopTimeDiscountDao shopTimeDiscountDao;
	@Autowired
	private IShopDao shopDao;
	
	public Map getShopTimedDiscount(Map<String, Object> params) throws Exception{
		Long shopId = Long.parseLong(params.get("shopId")+"");
		//商铺存在性校验
		checkShop(shopId);
		int count = shopTimeDiscountDao.getShopTimeDiscountCount(params);
		//一次将数据加载出来，代码遍历分组
		List<Map> timeDiscounts = null;
		if (count > 0) {
			timeDiscounts = shopTimeDiscountDao.getShopTimedDiscount(params);
			if (null != timeDiscounts && timeDiscounts.size()> 0) {
				List<Long> discountIds = new ArrayList<Long>();
				for(Map bean : timeDiscounts){
					if (!CommonValidUtil.isEmpty(bean.get("discountType"))) {
						int discountType = Integer.parseInt(bean.get("discountType")+"");
						//如果打折类型不是全部店铺
						if (discountType == 0) {
							Long discountId = bean.get("discountId") == null?null:Long.valueOf(bean.get("discountId")+"");
							if (null != discountId) {
								discountIds.add(discountId);
							}
						}
					}
					String discountPeriodType =  CommonValidUtil.isEmpty(bean.get("discountPeriodType"))?null:(bean.get("discountPeriodType")+"");
					if (null != discountPeriodType) {
						if ("每天".equals(discountPeriodType)) {
							bean.remove("week");
							bean.remove("weekFromTime");
							bean.remove("weekToTime");
							bean.remove("customFromDatetime");
							bean.remove("customToDatetime");
						}else if ("每周".equals(discountPeriodType)) {
							bean.remove("dayFromTime");
							bean.remove("dayToTime");
							bean.remove("customFromDatetime");
							bean.remove("customToDatetime");
						}else if ("自定义".equals(discountPeriodType)) {
							bean.remove("dayFromTime");
							bean.remove("dayToTime");
							bean.remove("week");
							bean.remove("weekFromTime");
							bean.remove("weekToTime");
						}
					}
				}
				List<Map> discountGoods = null;
				if (discountIds.size() > 0) {
					params.put("discountIds", discountIds);
					discountGoods = shopTimeDiscountDao.getShopTimedDiscountGoodsAlls(params);
					if (null != discountGoods && discountGoods.size() > 0) {
						for(Map bean : timeDiscounts){
							if (!CommonValidUtil.isEmpty(bean.get("discountType"))) {
								int discountType = Integer.parseInt(bean.get("discountType")+"");
								//如果打折类型不是全部店铺
								if (discountType == 0) {
									List<Map> goods = new ArrayList<Map>();
									int discountId = Integer.parseInt(bean.get("discountId")+"");
									int index = 1;
									Iterator<Map> ite = discountGoods.iterator();
									while(ite.hasNext()){
										Map map = ite.next();
										if (index > 10) break;//只取前10条
										String goodsImg = CommonValidUtil.isEmpty(map.get("goodsImg"))?null:""+map.get("goodsImg");
										map.put("goodsImg", FdfsUtil.getFileProxyPath(goodsImg));
										Object obj = map.get("discount_id");
										if (obj == null || "".equals(obj)) {
											continue;
										}
										int discId = Integer.parseInt(obj.toString());
										if (discId == discountId) {
											map.remove("shop_id");
											map.remove("discount_id");
											goods.add(map);
											ite.remove();
										}
										index++;
									}
									bean.put("goods", goods);
								}
							}
						}
					}
				}
			}
		}
		Map pModel = new HashMap();
		pModel.put("pNo", params.get("pNo"));
		pModel.put("count", count);
		pModel.put("lst", timeDiscounts);
		return pModel;
	}
	
	/**
	 * 校验商铺
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	private boolean checkShop(Long shopId) throws Exception {
		//校验商铺
		boolean flag = false;
		ShopDto shopDto = null;
		Integer shopStatus = null;
		Object redisShopData=DataCacheApi.getObject("shop"+shopId);
		if(!(flag =(redisShopData == null))){
			flag =(null == (shopDto=(ShopDto) redisShopData));
		}
		if (flag) {
			Map shop = shopDao.queryShopStatus(shopId);
			if (null == shop || shop.size() <= 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
			}
			shopStatus = CommonValidUtil.isEmpty(shop.get("shop_status"))?null:Integer.parseInt(shop.get("shop_status")+"");
		}else{
			shopStatus = shopDto.getShopStatus();
		}
		
		int status = -1;
		if (shopStatus == null || (status = shopStatus.intValue()) == 99 || status == 1 || status == 2) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP_STATUS);
		}
		return true;
	}
	
	public Map getShopTimedDiscountGoods(Map<String, Object> params) throws Exception{
		int count = shopTimeDiscountDao.getShopTimedDiscountGoodsCount(params);
		List<Map> discountGoods = null;
		if (count > 0) {
			discountGoods = shopTimeDiscountDao.getShopTimedDiscountGoods(params);
			if (null != discountGoods && discountGoods.size() > 0) {
				for(Map m : discountGoods){
					String goodsUrl = CommonValidUtil.isEmpty(m.get("goodsUrl"))?null:FdfsUtil.getFileProxyPath(""+m.get("goodsUrl"));
					String goodsLogo1 = CommonValidUtil.isEmpty(m.get("goodsLogo1"))?null:FdfsUtil.getFileProxyPath(""+m.get("goodsLogo1"));
					String goodsLogo2 = CommonValidUtil.isEmpty(m.get("goodsLogo2"))?null:FdfsUtil.getFileProxyPath(""+m.get("goodsLogo2"));
					if (null != goodsUrl) {
						m.put("goodsUrl", goodsUrl);
					}
					if (null != goodsLogo1) {
						m.put("goodsLogo1", goodsLogo1);
					}
					if (null != goodsLogo2) {
						m.put("goodsLogo2", goodsLogo2);
					}
				}
			}
		}
		Map pModel = new HashMap();
		pModel.put("pNo", params.get("pNo"));
		pModel.put("count", count);
		pModel.put("lst", discountGoods);
		return pModel;
	}

	public Map searchShopTimeDiscount(PageModel model,boolean isDistance) throws Exception{
		List<ShopDto> shopList = model.getList();
		//商铺限时折扣信息
		List<Map> shopTimeDiscounts = shopTimeDiscountDao.searchShopTimeDiscount(shopList);
		List<Map> shops = new ArrayList<Map>();
		int len = shopList.size();
		for (int i = 0; i < len; i++) {
			ShopDto shopDto = shopList.get(i);
			if (null == shopDto)continue;
			Map<String, Object> shop = new HashMap<String, Object>();
			Long shopId = shopDto.getShopId();
			if (isDistance) {
				shop.put("distance", shopDto.getDistance());
			}
			shop.put("shopInfrastructure", shopDto.getShopInfrastructure());
			shop.put("shopName", shopDto.getShopName());
			shop.put("shopId", shopId);
			shop.put("townId", shopDto.getTownId());
			shop.put("districtName", getDistrictName(shopDto.getDistrictId()));
			shop.put("soldNumber", shopDto.getSoldNumber());
			shop.put("redPacketFlag", shopDto.getRedPacketFlag());
			shop.put("cashCouponFlag", shopDto.getCashCouponFlag());
			shop.put("couponFlag", shopDto.getCouponFlag());
			List<Map> timeDiscounts = new ArrayList<Map>();
			if (null != shopTimeDiscounts && shopTimeDiscounts.size() > 0) {
				Long shopid =null;
				Iterator<Map> ite = shopTimeDiscounts.iterator();
				while(ite.hasNext()){
					Map timeDiscount = ite.next();
					shopid = Long.parseLong(timeDiscount.get("shopId")+"");
					if (shopId.intValue() == shopid.intValue()) {
						timeDiscount.remove("shopId");
						String discountPeriodType =  null == timeDiscount.get("discountPeriodType")?null:(timeDiscount.get("discountPeriodType")+"");
						if (null != discountPeriodType) {
							if ("每天".equals(discountPeriodType)) {
								timeDiscount.remove("week");
								timeDiscount.remove("weekFromTime");
								timeDiscount.remove("weekToTime");
								timeDiscount.remove("customFromDatetime");
								timeDiscount.remove("customToDatetime");
							}else if ("每周".equals(discountPeriodType)) {
								timeDiscount.remove("dayFromTime");
								timeDiscount.remove("dayToTime");
								timeDiscount.remove("customFromDatetime");
								timeDiscount.remove("customToDatetime");
							}else if ("自定义".equals(discountPeriodType)) {
								timeDiscount.remove("dayFromTime");
								timeDiscount.remove("dayToTime");
								timeDiscount.remove("week");
								timeDiscount.remove("weekFromTime");
								timeDiscount.remove("weekToTime");
							}
						}
						timeDiscounts.add(timeDiscount);
						ite.remove();
					}
				}
			}
			shop.put("timedDiscounts", timeDiscounts);
			shops.add(shop);
		}
		Map pModel = new HashMap();
		pModel.put("pNo", model.getToPage());
		pModel.put("count", model.getTotalItem());
		pModel.put("lst", shops);
		return pModel;
	}
	
	public String getDistrictName(Integer districtId){
		String districtName = null;
		try {
			if (null != districtId) {
				Object dto = DataCacheApi.getObject(CommonConst.KEY_DISTRICT+districtId);
				if (null != dto) {
					DistrictDto districtDto = (DistrictDto) dto;
					districtName = districtDto.getDistrictName();
				}
			}
		} catch (Exception e) {
			districtName = null;
		}
		return districtName;
	}

	public int insertTimedDiscountData(List<Map> timedLists) {
		return shopTimeDiscountDao.insertTimedDiscountData(timedLists);
	}
	
	

}
