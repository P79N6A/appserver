package com.idcq.idianmgr.common;

import java.util.HashMap;
import java.util.Map;

public class MgrCommonConst {
	/**
	 * 将商品族中的数据copy给商品
	 * <p>商品族跟商品字段对应[key=商品族，value=商品]</p>
	 */
	public static Map<String, String> GOODSGROUP_GOODS_FIELD = new HashMap<String, String>();
	static{
		GOODSGROUP_GOODS_FIELD.put("goodsGroupId", "goodsGroupId");
		GOODSGROUP_GOODS_FIELD.put("goodsServerMode", "goodsServerMode");
		GOODSGROUP_GOODS_FIELD.put("shopId", "shopId");
		GOODSGROUP_GOODS_FIELD.put("goodsName", "goodsName");
		GOODSGROUP_GOODS_FIELD.put("goodsDesc", "goodsDesc");
		GOODSGROUP_GOODS_FIELD.put("goodsDetailDesc", "goodsDetailDesc");
		GOODSGROUP_GOODS_FIELD.put("minPrice", "minPrice");
		GOODSGROUP_GOODS_FIELD.put("maxPrice", "maxPrice");
		GOODSGROUP_GOODS_FIELD.put("unitId", "unitId");
		GOODSGROUP_GOODS_FIELD.put("goodsLogo", "goodsLogo");
		GOODSGROUP_GOODS_FIELD.put("goodsStatus", "goodsStatus");
		GOODSGROUP_GOODS_FIELD.put("recommendFlag", "recommendFlag");
		GOODSGROUP_GOODS_FIELD.put("hotFlag", "hotFlag");
		GOODSGROUP_GOODS_FIELD.put("pinyincode", "pinyinCode");
		GOODSGROUP_GOODS_FIELD.put("useTime", "useTime");
		GOODSGROUP_GOODS_FIELD.put("keepTime", "keepTime");
	}
}
