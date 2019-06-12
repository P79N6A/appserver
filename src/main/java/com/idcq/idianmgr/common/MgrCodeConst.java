package com.idcq.idianmgr.common;

/**
 * 一点管家业务异常代号
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 下午7:48:12
 */
public class MgrCodeConst {
	/*===================================code========================================*/
	public static final int CODE_EXISTS_INVALID_ORDER = 80101;//商品族属性ID不能为空
	
	/*===================================msg========================================*/
	public static final String MSG_GOODS_GROUP_PRO_ID_NOT_NULL = "商品族属性ID不能为空";//商品族属性ID不能为空
	public static final String MSG_GOODS_GROUP_PRO_VALUE_ID_NOT_NULL = "商品族属性值ID不能为空";//商品族属性值ID不能为空
	public static final String MSG_BIZ_TYPE_NOT_NULL = "业务类型bizType不能为空";//业务类型bizType不能为空
	public static final String MSG_FORMAT_ERROR_BIZ_TYPE = "业务类型bizType数据格式错误";
	public static final String MSG_BIZ_ID_NOT_NULL = "商品族属性ID/属性值ID不能为空";//业务类型bizId不能为空
	public static final String MSG_FM_ERROR_GG_NAME = "商品族属性不能为空";//商品族属性不能为空
	public static final String MSG_FM_ERROR_GGV_NAME = "商品族属性值不能为空";//商品族属性值不能为空
	public static final String MSG_FORMAT_ERROR_BIZ_ID = "业务类型bizId格式错误";
	public static final String MSG_GOODS_GROUP_PRO_NOT_EXISTS = "商品族属性不存在";//商品族属性不存在
	public static final String MSG_GOODS_GROUP_PRO_VALUE_NOT_EXISTS = "商品族属性值不存在";//商品族属性值不存在
	
	/*-----------------------------------goodsGroup-------------------------------*/
	public static final String MSG_EXISTS_INVALID_ORDER = "商品族存在待处理的订单，不能被删除";
	public static final String MSG_REQUIRED_GG_NAME = "服务名称不能为空";
	public static final String MSG_REQUIRED_GG_ID = "商品族ID不能为空";
	public static final String MSG_REQUIRED_GG_PRO_ID = "商品族属性ID不能为空";
	public static final String MSG_FM_ERROR_GG_PRO_ID = "商品族属性ID数据格式错误";
	public static final String MSG_REQUIRED_SETTING_ID = "店铺外卖费用设置ID不能为空";
	public static final String MSG_REQUIRED_GG_OP_TYPE = "操作类型不能为空";
	public static final String MSG_FM_ERROR_GG_OP_TYPE = "操作类型数据格式错误";
	public static final String MSG_FM_ERROR_SETTING_ID = "店铺外卖费用设置ID数据格式错误";
	public static final String MSG_FM_ERROR_GG_ID = "商品族ID数据格式错误";
	public static final String MSG_REQUIRED_GG_SERVICE_PRICE = "服务价格不能为空";
	public static final String MSG_REQUIRED_GG_SERVER_MODEL = "服务方式不能为空";
	public static final String MSG_REQUIRED_GG_ATTEM_ID = "附件ID不能为空";
	public static final String MSG_FM_EEROR_GG_ATTEM_ID = "附件ID数据格式错误";
	public static final String MSG_REQUIRED_GG_CATEGORY_ID = "服务分类ID不能为空";
	public static final String MSG_REQUIRED_GG_LOGO_ID = "商品族logoID不能为空";
	public static final String MSG_FM_EEROR_GG_CATEGORY_ID = "服务分类ID数据格式错误";
	public static final String MSG_MISS_GG_ATTEM = "附件信息不存在";
	public static final String MSG_MISS_GG_CATEGORY = "服务分类不存在";
	public static final String MSG_MISS_GG = "商品族不存在";
	public static final String MSG_FM_EEROR_GG_TECH_ID = "技师ID数据格式错误";
	public static final String MSG_FM_EEROR_GG_STATUS = "服务状态数据格式错误";
	public static final String MSG_MISS_GG_TECH = "技师信息不存在";
	/*-----------------------------------order-------------------------------*/
	public static final String MSG_FM_ERROR_HANDERID = "操作人ID数据格式错误";
	
	
	public static final int QUERY_DAY = 30; //单位天，
	
	
}

