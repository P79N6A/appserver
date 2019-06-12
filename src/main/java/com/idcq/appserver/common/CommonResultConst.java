package com.idcq.appserver.common;

import java.util.HashSet;
import java.util.Set;

/**
 * 每个接口需要返回的常量
* @ClassName: CommonResultConst 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 张鹏程 
* @date 2015年4月2日 上午10:28:49 
*
 */
public class CommonResultConst {
	
	
	/**
	 * 搜索店铺接口返回常量
	 */
	public static final Set<String>SEARCH_SHOP_RETURN_FIELDS=new HashSet<String>();
	static{
		SEARCH_SHOP_RETURN_FIELDS.add("shopId");
		SEARCH_SHOP_RETURN_FIELDS.add("shopName");
		SEARCH_SHOP_RETURN_FIELDS.add("shopLogoUrl");
		SEARCH_SHOP_RETURN_FIELDS.add("starLevelGrade");
		SEARCH_SHOP_RETURN_FIELDS.add("serviceGrade");
		SEARCH_SHOP_RETURN_FIELDS.add("envGrade");
		SEARCH_SHOP_RETURN_FIELDS.add("startBTime");
		SEARCH_SHOP_RETURN_FIELDS.add("stopBTime");
		SEARCH_SHOP_RETURN_FIELDS.add("address");
		SEARCH_SHOP_RETURN_FIELDS.add("telephone");
		SEARCH_SHOP_RETURN_FIELDS.add("shopInfrastructure");
		SEARCH_SHOP_RETURN_FIELDS.add("longitude");
		SEARCH_SHOP_RETURN_FIELDS.add("latitude");
		SEARCH_SHOP_RETURN_FIELDS.add("redPacketFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("fullSentFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("couponFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("distance");
		SEARCH_SHOP_RETURN_FIELDS.add("soldNumber");
		SEARCH_SHOP_RETURN_FIELDS.add("cashCouponFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("couponFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("timedDiscountFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("memberDiscount");
		SEARCH_SHOP_RETURN_FIELDS.add("shopMode");
		SEARCH_SHOP_RETURN_FIELDS.add("districtId");
		SEARCH_SHOP_RETURN_FIELDS.add("districtName");
		SEARCH_SHOP_RETURN_FIELDS.add("townId");
		SEARCH_SHOP_RETURN_FIELDS.add("townName");
		SEARCH_SHOP_RETURN_FIELDS.add("takeoutFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("leastBookPrice");
		SEARCH_SHOP_RETURN_FIELDS.add("columnId");
		SEARCH_SHOP_RETURN_FIELDS.add("shopHours");
		SEARCH_SHOP_RETURN_FIELDS.add("businessAreaActivityFlag");
		SEARCH_SHOP_RETURN_FIELDS.add("isRecommend");
		SEARCH_SHOP_RETURN_FIELDS.add("recommendReason");
		SEARCH_SHOP_RETURN_FIELDS.add("shopIdentity");
	}
	
	public static final Set<String>GET_SHOP_DETAIL=new HashSet<String>();
	static{
		GET_SHOP_DETAIL.add("goodsName");
		GET_SHOP_DETAIL.add("goodsType");
		GET_SHOP_DETAIL.add("goodsNo");
		GET_SHOP_DETAIL.add("goodsLogo1");
		GET_SHOP_DETAIL.add("goodsLogo2");
		GET_SHOP_DETAIL.add("goodsUrl");
		GET_SHOP_DETAIL.add("soldNumber");
		GET_SHOP_DETAIL.add("zanNumber");
		GET_SHOP_DETAIL.add("standardPrice");
		GET_SHOP_DETAIL.add("discountPrice");
		GET_SHOP_DETAIL.add("vipPrice");
		GET_SHOP_DETAIL.add("finalPrice");
		GET_SHOP_DETAIL.add("unit");
		GET_SHOP_DETAIL.add("unitName");
		GET_SHOP_DETAIL.add("goodsCategoryId");
		GET_SHOP_DETAIL.add("goodsCategoryName");
		GET_SHOP_DETAIL.add("goodsDesc");
		GET_SHOP_DETAIL.add("goodsDetailDesc");
		GET_SHOP_DETAIL.add("taste");
		GET_SHOP_DETAIL.add("spiciness");
		GET_SHOP_DETAIL.add("isExpert");
		GET_SHOP_DETAIL.add("expertIds");
		GET_SHOP_DETAIL.add("goodsSettleFlag");
		GET_SHOP_DETAIL.add("attachementIds");
		GET_SHOP_DETAIL.add("goodsGroupLogoUrls");
		GET_SHOP_DETAIL.add("goodsGroupCategoryIds");
		GET_SHOP_DETAIL.add("goodsGroupCategoryNames");
		GET_SHOP_DETAIL.add("techIds");
		GET_SHOP_DETAIL.add("techNames");
		GET_SHOP_DETAIL.add("minPrice");
		GET_SHOP_DETAIL.add("maxPrice");
		GET_SHOP_DETAIL.add("useTime");
		GET_SHOP_DETAIL.add("keepTime");
		GET_SHOP_DETAIL.add("goodsServerMode");
		GET_SHOP_DETAIL.add("goodsLogoId");
		GET_SHOP_DETAIL.add("isSupportMarketPrices");
		GET_SHOP_DETAIL.add("specsDesc");
		GET_SHOP_DETAIL.add("storageTotalNumber");
		GET_SHOP_DETAIL.add("alarmNumberMax");
		GET_SHOP_DETAIL.add("alarmNumberMin");
		GET_SHOP_DETAIL.add("costPrice");
		GET_SHOP_DETAIL.add("goodsProValuesIds");  
		GET_SHOP_DETAIL.add("goodsProValuesNames");
		GET_SHOP_DETAIL.add("digitScale");
		GET_SHOP_DETAIL.add("goodsStatus");
		GET_SHOP_DETAIL.add("goodsPriceSpec");
		GET_SHOP_DETAIL.add("shopId");
	}
	
	public static final Set<String>GET_SHOP_CASH_COUPONS=new HashSet<String>();
	static{
		GET_SHOP_CASH_COUPONS.add("cashCouponId");
		GET_SHOP_CASH_COUPONS.add("cashCouponName");
		GET_SHOP_CASH_COUPONS.add("cashCouponDesc");
		GET_SHOP_CASH_COUPONS.add("cashCouponImg");
		GET_SHOP_CASH_COUPONS.add("heatDegree");
		GET_SHOP_CASH_COUPONS.add("totalNumber");
		GET_SHOP_CASH_COUPONS.add("availableNumber");
		GET_SHOP_CASH_COUPONS.add("value");
		GET_SHOP_CASH_COUPONS.add("price");
		GET_SHOP_CASH_COUPONS.add("conditionPrice");
		GET_SHOP_CASH_COUPONS.add("issueFromDate");
		GET_SHOP_CASH_COUPONS.add("issueToDate");
		GET_SHOP_CASH_COUPONS.add("obtainNumberPerDayPerPerson");
		GET_SHOP_CASH_COUPONS.add("useNumberPerOrder");
		GET_SHOP_CASH_COUPONS.add("useTogetherFlag");
		GET_SHOP_CASH_COUPONS.add("startTime");
		GET_SHOP_CASH_COUPONS.add("stopTime");
	}
	
	public static final Set<String>GET_SHOP_CASH_SHOP_FIELDS=new HashSet<String>();
	static{
		GET_SHOP_CASH_SHOP_FIELDS.add("shopId");
		GET_SHOP_CASH_SHOP_FIELDS.add("shopName");
		GET_SHOP_CASH_SHOP_FIELDS.add("shopInfrastructure");
		GET_SHOP_CASH_SHOP_FIELDS.add("distance");
		GET_SHOP_CASH_SHOP_FIELDS.add("cashCoupons");
		GET_SHOP_CASH_SHOP_FIELDS.add("districtName");
	}
	
	public static final Set<String> GET_SHOP_COUPON_FIELDS=new HashSet<String>();
	static{
		GET_SHOP_COUPON_FIELDS.add("shopId");
		GET_SHOP_COUPON_FIELDS.add("shopName");
		GET_SHOP_COUPON_FIELDS.add("shopInfrastructure");
		GET_SHOP_COUPON_FIELDS.add("distance");
		GET_SHOP_COUPON_FIELDS.add("coupons");
		GET_SHOP_COUPON_FIELDS.add("districtName");
	}
	
	public static final Set<String> GET_SHOP_COUPON=new HashSet<String>();
	static{
		GET_SHOP_COUPON.add("couponId");
		GET_SHOP_COUPON.add("couponName");
		GET_SHOP_COUPON.add("couponDesc");
		GET_SHOP_COUPON.add("goodsId");
		GET_SHOP_COUPON.add("goodsName");
		GET_SHOP_COUPON.add("couponImg");
		GET_SHOP_COUPON.add("heatDegree");
		GET_SHOP_COUPON.add("totalNumber");
		GET_SHOP_COUPON.add("availableNumber");
		GET_SHOP_COUPON.add("value");
		GET_SHOP_COUPON.add("price");
		GET_SHOP_COUPON.add("issueFromDate");
		GET_SHOP_COUPON.add("issueToDate");
		GET_SHOP_COUPON.add("obtainNumberPerDayPerPerson");
		GET_SHOP_COUPON.add("startTime");
		GET_SHOP_COUPON.add("stopTime");
	}
	
	public static final Set<String> MERCHANT_FEEDBACK_LIST=new HashSet<String>();
	static{
	    MERCHANT_FEEDBACK_LIST.add("feedbackId");
		MERCHANT_FEEDBACK_LIST.add("feedback");
		MERCHANT_FEEDBACK_LIST.add("feedbackTime");
		MERCHANT_FEEDBACK_LIST.add("handleSuggestion");
		MERCHANT_FEEDBACK_LIST.add("handleTime");
		MERCHANT_FEEDBACK_LIST.add("handleStatus");
		MERCHANT_FEEDBACK_LIST.add("clientSystemType");
		MERCHANT_FEEDBACK_LIST.add("feedbackType");
	}
	
	
	public static final Set<String>SEARCH_GOODS=new HashSet<String>();
	static{
		SEARCH_GOODS.add("goodsId");
		SEARCH_GOODS.add("goodsName");
		SEARCH_GOODS.add("goodsType");
		SEARCH_GOODS.add("shopId");
		SEARCH_GOODS.add("shopName");
		SEARCH_GOODS.add("goodsNo");
		SEARCH_GOODS.add("goodsLogo1");
		SEARCH_GOODS.add("goodsLogo2");
		SEARCH_GOODS.add("goodsUrl");
		SEARCH_GOODS.add("soldNumber");
		SEARCH_GOODS.add("zanNumber");
		SEARCH_GOODS.add("standardPrice");
		SEARCH_GOODS.add("discountPrice");
		SEARCH_GOODS.add("vipPrice");
		SEARCH_GOODS.add("finalPrice");
		SEARCH_GOODS.add("goodsSettleFlag");
		SEARCH_GOODS.add("unit");
		SEARCH_GOODS.add("distance");
		SEARCH_GOODS.add("redPacketFlag");
		SEARCH_GOODS.add("cashCouponFlag");
		SEARCH_GOODS.add("couponFlag");
		SEARCH_GOODS.add("districtName");
	}
	
	public static final Set<String>GET_SHOP_BOOK_ORDERS=new HashSet<String>();
	static{
		GET_SHOP_BOOK_ORDERS.add("orderId");
		GET_SHOP_BOOK_ORDERS.add("mobile");
		GET_SHOP_BOOK_ORDERS.add("userName");
		GET_SHOP_BOOK_ORDERS.add("resourceType");
		GET_SHOP_BOOK_ORDERS.add("maxPeople");
		GET_SHOP_BOOK_ORDERS.add("minPeople");
		GET_SHOP_BOOK_ORDERS.add("reserveResourceDate");
		GET_SHOP_BOOK_ORDERS.add("startTime");
		GET_SHOP_BOOK_ORDERS.add("endTime");
		GET_SHOP_BOOK_ORDERS.add("pNum");
	}
	
	public static final Set<String>GET_SHOP_GOODS=new HashSet<String>();
	static{
		GET_SHOP_GOODS.add("goodsId");
		GET_SHOP_GOODS.add("goodsName");
		GET_SHOP_GOODS.add("goodsType");
		GET_SHOP_GOODS.add("goodsServerMode");
		GET_SHOP_GOODS.add("index");
		GET_SHOP_GOODS.add("goodsNo");
		GET_SHOP_GOODS.add("goodsLogo1");
		GET_SHOP_GOODS.add("goodsLogo2");
		GET_SHOP_GOODS.add("goodsUrl");
		GET_SHOP_GOODS.add("soldNumber");
		GET_SHOP_GOODS.add("zanNumber");
		GET_SHOP_GOODS.add("standardPrice");
		GET_SHOP_GOODS.add("discountPrice");
		GET_SHOP_GOODS.add("vipPrice");
		GET_SHOP_GOODS.add("finalPrice");
		GET_SHOP_GOODS.add("goodsSettleFlag");
		GET_SHOP_GOODS.add("unit");
		GET_SHOP_GOODS.add("unitName");
		GET_SHOP_GOODS.add("takeout");
		GET_SHOP_GOODS.add("goodsGroupId");
		GET_SHOP_GOODS.add("goodsStatus");
		GET_SHOP_GOODS.add("minPrice");
		GET_SHOP_GOODS.add("maxPrice");
		GET_SHOP_GOODS.add("isSupportMarketPrices");
		GET_SHOP_GOODS.add("goodsCategoryName");
		GET_SHOP_GOODS.add("goodsCategoryId");
		GET_SHOP_GOODS.add("storageTotalNumber");
		GET_SHOP_GOODS.add("alarmNumberMax");
		GET_SHOP_GOODS.add("alarmNumberMin");
		GET_SHOP_GOODS.add("pinyinCode");
		GET_SHOP_GOODS.add("barcode");
		GET_SHOP_GOODS.add("digitScale");
		GET_SHOP_GOODS.add("unitId");
	}
	
	public static final Set<String>GET_GROUP_GOOD=new HashSet<String>();
	static{
		GET_GROUP_GOOD.add("goodsId");
		GET_GROUP_GOOD.add("goodsName");
		GET_GROUP_GOOD.add("goodsLogo");
		GET_GROUP_GOOD.add("soldNumber");
		GET_GROUP_GOOD.add("zanNumber");
		GET_GROUP_GOOD.add("goodsServerMode");
		GET_GROUP_GOOD.add("categoryId");
		GET_GROUP_GOOD.add("minPrice");
		GET_GROUP_GOOD.add("maxPrice");
		GET_GROUP_GOOD.add("goodsGroupId");
	}
	public static final Set<String>GET_GROUP_SHOP=new HashSet<String>();
	static{
		GET_GROUP_SHOP.add("shopId");
		GET_GROUP_SHOP.add("shopName");
		GET_GROUP_SHOP.add("shopMode");
		GET_GROUP_SHOP.add("shopLogoUrl");
		GET_GROUP_SHOP.add("starLevelGrade");
		GET_GROUP_SHOP.add("serviceGrade");
		GET_GROUP_SHOP.add("envGrade");
		GET_GROUP_SHOP.add("address");
		GET_GROUP_SHOP.add("telephone");
		GET_GROUP_SHOP.add("memberDiscount");
		GET_GROUP_SHOP.add("shopInfrastructure");
		GET_GROUP_SHOP.add("longitude");
		GET_GROUP_SHOP.add("latitude");
		GET_GROUP_SHOP.add("redPacketFlag");
		GET_GROUP_SHOP.add("cashCouponFlag");
		GET_GROUP_SHOP.add("couponFlag");
		GET_GROUP_SHOP.add("timedDiscountFlag");
		GET_GROUP_SHOP.add("soldNumber");
		GET_GROUP_SHOP.add("distance");
		GET_GROUP_SHOP.add("districtId");
		GET_GROUP_SHOP.add("districtName");
		GET_GROUP_SHOP.add("townId");
		GET_GROUP_SHOP.add("townName");
		GET_GROUP_SHOP.add("goods");
		GET_GROUP_SHOP.add("columnId");
		GET_GROUP_SHOP.add("businessAreaActivityFlag");
	}
	
	public static final Set<String>GET_SCHEDULE_SETTING=new HashSet<String>();
	static{
		GET_SCHEDULE_SETTING.add("classesType");
		GET_SCHEDULE_SETTING.add("classesDate");
	}
	
	public static final Set<String>GET_CLASSES_SETTING=new HashSet<String>();
	static{
		GET_CLASSES_SETTING.add("classesType");
		GET_CLASSES_SETTING.add("workTime");
	}
	
	public static final Set<String>GET_GOODS_GROUP_DETAIL=new HashSet<String>();
	static{
		GET_GOODS_GROUP_DETAIL.add("goods");
		GET_GOODS_GROUP_DETAIL.add("groupProperties");
	}
	
	public static final Set<String>GET_GOODS_GROUP_DETAIL_FOR_GOODS=new HashSet<String>();
	static{
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsId");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsGroupId");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsName");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsLogo");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("soldNumber");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("zanNumber");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsServerMode");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("standardPrice"); 
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsSettleFlag");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("storageTotalNumber");
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("unitName"); 
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsProValuesIds"); 
		GET_GOODS_GROUP_DETAIL_FOR_GOODS.add("goodsProValuesNames"); 
	}
	
	public static final Set<String>GET_GOODS_GROUP_DETAIL_FOR_PROPS=new HashSet<String>();
	static{
		GET_GOODS_GROUP_DETAIL_FOR_PROPS.add("groupPropertyId");
		GET_GOODS_GROUP_DETAIL_FOR_PROPS.add("groupPropertyName");
		GET_GOODS_GROUP_DETAIL_FOR_PROPS.add("groupPropertyValues");
	}
	public static final Set<String>GET_GOODS_GROUP_DETAIL_FOR_PROP_VALUES=new HashSet<String>();
	static{
		GET_GOODS_GROUP_DETAIL_FOR_PROP_VALUES.add("proValuesId");
		GET_GOODS_GROUP_DETAIL_FOR_PROP_VALUES.add("proValue");
	}
	
	public static final Set<String>GET_OPERATION_CLASSIFY=new HashSet<String>();
	static{
		GET_OPERATION_CLASSIFY.add("classifyId");
		GET_OPERATION_CLASSIFY.add("classifyName");
		GET_OPERATION_CLASSIFY.add("classifyKey");
		GET_OPERATION_CLASSIFY.add("hasChildren");
		GET_OPERATION_CLASSIFY.add("classifyImgUrl");
	}
	
	public static final Set<String>IDGJ_ORDER_DETAIL=new HashSet<String>();
	static{
		IDGJ_ORDER_DETAIL.add("orderId");
	}
	
	public static final Set<String>IDGJ_XORDER_DETAIL=new HashSet<String>();
	static{
		
	}
}
