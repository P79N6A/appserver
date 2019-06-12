package com.idcq.appserver.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.idcq.appserver.listeners.ContextInitListener;

/**
 * 公共的常量
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:03:06
 */
/**
 * @author Administrator
 *
 */
public class CommonConst {
	/*-----------------------redis缓存key常量--------------------------*/
    public static final String CASHCARD_PASSWORD_KEY = "1dcq1368";//如果是使用漫
	//public static final String REDIS_VERICODE="veriCode:";//短信验证码
	public static final String REDIS_VERICODE_OBJ="veriCodeObj:";//短信验证码缓存对象
	public static final String REDIS_SMSIP = "smsIp:";//IP
	public static final String REDIS_SMS_PARAM = "smsParam:";//短信平台参数数据key
	public static final String SMS_MD_CHANNEL_KEY = "md";//如果是使用漫道短信科技的通道，则在获取url等参数时，需要在key前面加上此key
	public static final String SMS_ZQ_CHANNEL_KEY = "zq";//如果是使用志晴短信科技的通道，则在获取url等参数时（当前默认使用）
	public static final String SMS_CL_CHANNEL_KEY = "cl";//如果是使用创蓝短信科技的通道，则在获取url等参数时（当前默认使用）
	public static final String SMS_CONTENT_SIGN = "【1点传奇】";//短信内容签名
	public static final String SMS_URL="smsUrl";//短信平台url
	public static final String SMS_SN="smsSn";//短信平台sn
	public static final String SMS_PWD="smsPwd";//短信平台pwd
	public static final String SMS_GENERAL="smsGeneral";//短信平台通用模板
	public static final String REDIS_USER_RED_PACKET="userRedPacket:";//用户查询红包缓存关键字
	public static final String QR_REDPACKET_KEYS = "qrRedPacketKeys:";
	public static final String REDIS_RED_PACKET="redPacket:";//红包缓存关键字
	public static final String KEY_SHOP_DEVICE = "shopDevice:";
	public static final String KEY_JSESSIONID = "session:";
	public static final String KEY_SHOP = "ShopDto:";	//商铺
	public static final String KEY_GOODS = "GoodsDto:";	//商品
	public static final String KEY_ORDER = "order:";	//订单
	public static final String KEY_USER = "UserDto:";	//用户
	public static final String KEY_MOBILE = "mobile:";	//用户
	public static final String KEY_WEIXIN_NO = "weixinNo:";	//用户
	public static final String KEY_PAY_CHANNEL = "payChannel:";
	public static final String KEY_SUB_PAY_MODEL = "subPayModel:";
	public static final String KEY_ADDRESS = "address:"; //地址
	public static final String KEY_ADDRESS_ID = "addressId"; //地址
	public static final String KEY_COUPON = "coupon:";	//优惠券
	public static final String KEY_PROVINCE = "ProvinceDto:";	//省
	public static final String KEY_CITY = "CitiesDto:";	//市
	public static final String KEY_DISTRICT = "DistrictDto:";	//区
	public static final String KEY_TOWN = "TownDto:"; // 镇
	public static final String KEY_ORDER_INCRINT = "oIncrInt"; // 订单号自增长后缀
	public static final String KEY_AD_KEYLIST = "adKeyList"; // 广告key的列表
	public static final String KEY_SEPARATOR_COLON = ":"; // 分隔符：冒号
	public static final String KEY_SMS_CHANNEL = "sms_channel";
	public static final String KEY_GOODS_PICTURE = "goodsPicture:";
	public static final String KEY_GOODSGROUP_PICTURE = "goodsGroupPicture:";
	public static final String KEY_SHOP_TOP_GOODS="shopTopGoods:";//商铺销量最高的两个
	public static final String KEY_CITY_OPERATION_CLASSES="cityOperationClasses:";//城市运营分类
	public static final String KEY_USER_PRAISE = "praise:"; //保存点赞用户的id
	public static final String KEY_CLASSIFY_OPERATION_SEARCH = "classifikey:"; //保存点赞用户的id
	
	public static final String KEY_UNIT="Unit:";//单位缓存
	
	public static final String KEY_GOODSCATEGORY="GoodsCategoryDto:";
	
	public static final String KEY_SHOP_LOGO="shop_logo:";//商铺Logo
	
	public static final String KEY_GOODS_SET="GoodsSetDto:";
	
	
	public static  final String KEY_SEND_ACTIVITY="ActivityMsg:";//商圈活动结束后发送消息
	/* ---- SHOP_DEVICE 中存在的字段 ---- */
	public static final String TOKEN = "token";
	public static final String SHOP_ID = "shopId";
	public static final String REG_ID = "regId";
	
	public static final String JSESSIONID = "JSESSIONID";
	
	/*-------操作标志-------------------*/
	public static final int HANDLE_FLAG_ADD = 1;	//新增操作
	public static final int HANDLE_FLAG_EDIT = 0;	//修改操作
	
	/*-------shopMode5种类型------------------------*/
	/**
	 * 餐饮行业
	 */
	public static final String RESTAURANT = "restaurant";
	public static final String FASTRESTAURANT = "fastrestaurant";
	/**
	 * 服务行业
	 */
	public static final String SERVICE = "service";
	/**
	 * 零售行业
	 */
	public static final String GOODS = "goods";
	/**
	 * 汽修
	 */
	public static final String AUTO = "auto";
	
	public static final int CACHE_USER_OUT_TIME = 7200; //用户信息缓存失效时间
	
	public static final int CACHE_SESSION_OUT_TIME = 1296000; //session缓存失效时间
	/**
	 * 心跳缓存时间
	 */
	public static final int CACHE_HEARTBEAT_LOG_OUT_TIME = 60; 
	
	/*---------缓存实体对象对应的statement --- */
	public static final String CACHE_STATEMENT_ID = ".getById";
	
	/**
	 * 定时发送短信任务开关，关闭
	 */
	public static String SMS_TIMER_SWITCH_OFF = "off";
	/**
	 * 定时发送短信任务开关，开启
	 */
	public static String SMS_TIMER_SWITCH_ON = "on";//
	
	/**
	 * 访问者IP缓存时间
	 */
	public static int CACHE_TIME_IP = 80;//访问者IP缓存时间，单位秒
	/**
	 * 是否要做IP限制
	 */
	public static boolean IS_IP_LIMIT = false;
	/**
	 * 短信验证码缓存时间
	 */
	public static int CACHE_TIME_CODE = 30*60;//验证码缓存时间，单位秒，默认30分钟
	/**
	 * 短信平台相关参数缓存时间
	 */
	public static int CACHE_TIME_SMSPARAM = 60*30;//短信平台参数缓存时间，单位秒，默认缓存时间30分钟
	public static int CACHE_TIME_SMS_CHANNEL = 3*60;//短信发送通道缓存时间，单位秒，默认3分钟
	public static int SHOP_DEAD_TIME = 7;//商铺欠费服务停止延后时间，单位天，默认7天
	public static int ORDER_RSRC_CALL_TIME = 3;//订单预定资源过期提醒，提前多少小时提醒，默认3小时
	public static String ORDER_RSRC_CALL_TIME_KEY = "order_rsrc_call_time";//查询预订单提前提醒时间配置key
	public static final int PAY_STATUS_PAY_SUCCESS=1; //订单已支付
	public static final int PAY_STATUS_PAYED=1; //订单已支付
	public static final int PAY_STATUS_NOT_PAY=0; //订单未支付
	public static final int PAY_STATUS_PAY_FAILURE=2;//订单未支失败
	public static final int PAY_STATUS_PAY_CANCEL=3;//订单已取消
	
	public static final int ORDER_STATUS_WAIT_PAY=1;//订单状态：未消费-0,待支付-1,待评价-2,已完成-3
	public static final int ORDER_STATUS_WAIT_COMMENT=2;//订单状态：未消费-0,待支付-1,待评价-2,已完成-3
	public static final int ORDER_STATUS_FINISH=3;
	                                                    
	
	
	public static final int PAY_TYPE_3RD =0;//第三方支付
	public static final int PAY_TYPE_CSB =1;//传奇宝支付
	public static final int PAY_TYPE_CASH_COUPON=2;// 代金券支付
	public static final int PAY_TYPE_RED_PACKET=3;//红包支付
	public static final int PAY_TYPE_COUPON=4;//优惠劵支付
	public static final int PAY_TYPE_CARD =5;//会员卡
	public static final int PAY_TYPE_CASH =6;//现金支付
	public static final int PAY_TYPE_POS =7;//POS支付
	public static final int PAY_TYPE_WXSCAN=8;//微信扫码支付
	public static final int PAY_TYPE_ALI=0;//微信扫码支付
	public static final int PAY_TYPE_JHK=10;//建行信用卡
	public static final int PAY_TYPE_CONSUM=11;//消费金
	public static final int PAY_TYPE_REWARD=12;//平台奖励
	public static final int PAY_TYPE_VOUCHER = 13;//代金券支付1币
	public static final int PAY_TYPE_CASH_COUPON_MONEY = 14;//3币代金币
	public static final int PAY_TYPE_TIMECARD=15;//次卡支付
	public static final int PAY_TYPE_OTHER=16;
	public static final int PAYEE_TYPE_PLATFORM =0;//传奇平台
	public static final int PAYEE_TYPE_SHOP =1;//商铺
	
	// ==============================用户状态 status================================
	public static final int USER_WAIT_ACTIVE_STATUS = 0;//用户状态 待激活-0,正常-1,冻结-2,注销-3
	public static final int USER_NORMAL_STATUS = 1;//用户状态 待激活-0,正常-1,冻结-2,注销-3
	public static final int USER_FREEZE_STATUS = 2;//用户状态 待激活-0,正常-1,冻结-2,注销-3
	public static final int USER_LOGOUT_STATUS = 3;//用户状态 待激活-0,正常-1,冻结-2,注销-3
	
	//==============================用户类型userType================================
	public static final int USER_TYPE_MEMBER =0;//会员-0,店铺管理者-10,经销商-20,代理商-30
	public static final int USER_TYPE_SHOP =10;//会员-0,店铺管理者-10,经销商-20,代理商-30
	public static final int USER_TYPE_DISTRIBUTOR =20;//会员-0,店铺管理者-10,经销商-20,代理商-30
	public static final int USER_TYPE_AGENT =30;//会员-0,店铺管理者-10,经销商-20,代理商-30
	public static final int USER_TYPE_IS_NOT_MEMBER =1;//会员-0,非会员-1店铺管理者-10,经销商-20,代理商-30
	//==============================用户是否会员================================
	public static final int USER_TO_ACTIVATE =2;
	public static final int USER_IS_MEMBER =1;
	public static final int USER_IS_NOT_MEMBER =0;
	//==============================会员注册方式================================
	public static final int USER_REGISTER_TYPE_IS_NOT_MEMBER =0;
	public static final int USER_REGISTER_TYPE_WEIXIN_PUBLIC =8;
/*	4.消费返利 5.推荐会员 6.推荐店铺[会员] 7.服务店铺[服务人员] 8.市级代理 9.区县代理 10.乡镇代理*/
	public static final String USER_TYPE_REWARD_CONSUME ="消费返利";
    public static final String USER_TYPE_REWARD_USER ="推荐会员";
    public static final String USER_TYPE_REWARD_SHOP ="推荐店铺";
    public static final String USER_TYPE_REWARD_SERVER ="服务店铺";
    public static final String USER_TYPE_REWARD_AGENT_CITY ="市级代理 ";
    public static final String USER_TYPE_REWARD_AGENT_COUNTY ="区县代理";
    public static final String USER_TYPE_REWARD_AGENT_TOWNS ="乡镇代理";
    public static final String USER_TYPE_REWARD_OPERATORS ="乡镇代理";
    public static final String USER_TYPE_REWARD_CARRIER ="运营商";
    public static final String USER_TYPE_INTEGRATION_PROMOTION ="店铺整合推广员";
    public static final String USER_TYPE_INTEGRATION_FACILITATE ="店铺整合促成人";
    public static final String USER_TYPE_INTEGRATION_PRINCIPAL ="被整合店铺店主";
    
	//==============================用户类型referType================================
    /**
     * 账户推荐-0
     */
	public static final int USER_REFERTYPE_MEMBER =0;
	/**
	 * 商铺推荐-1
	 */
	public static final int USER_REFERTYPE_SHOP =1;
	
	// ==============================商铺状态 shop_status================================
	/**
	 * 正常
	 */
	public static final int SHOP_NORMAL_STATUS = 0;//审核中-99,正常-0,下线-1,删除-2,欠费-3
	/**
	 * 商铺类型：便利店
	 */
	public static final int SHOP_TYPE_STORE = 13;
	/**
	 * 欠费
	 */
	public static final int SHOP_LACK_MONEY_STATUS=3;//欠费
	/**
	 * 下线
	 */
	public static final int SHOP_OFFLINE_STATUS=1;
	/**
     * 待审核
     */
    public static final int SHOP_AUDIT_STATUS=99;
    /**
     * 删除
     */
    public static final int SHOP_DEL_STATUS=2;
	
	//==============================状态状态 account_status================================
	public static final int ACCOUNT_FREEZE_STATUS = 0;//冻结-0
	public static final int ACCOUNT_NORMAL_STATUS = 1;//正常-1
	
	//==============================会员注册方式register_type================================
	public static final String REGISTER_TYPE_QRCODE = "1"; //1-二维码
	public static final String REGISTER_TYPE_SHOP = "2"; //2-商家后台推荐注册
	public static final String REGISTER_TYPE_COLLECT = "3"; //3-收银机注册会员
	public static final String REGISTER_TYPE_APP = "4"; //4-APP注册
	
	// ==============================订单状态-order_status================================
	public static final int ORDER_STS_YYD = 0;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_YKD = 1;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_PSZ = 2;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_YJZ = 3;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_TDZ = 4;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_YTD = 5;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_ZZXD = 8;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	public static final int ORDER_STS_DQR = 9;//订单状态；已预定-0,已开单-1,派送中-2,已完成-3，退单中-4,已退单-5,自助下单-8,待确认-9
	
	// ==============================订单来源-order_source================================
	public static final int ORDER_SOURCE_ZZXD = 1;//自助下单-1
	public static final int ORDER_SOURCE_FZZXD = 0;//非自助下单-0	
	
	//==============================座位订单状态-seat_order_status================================
	public static final int SEAT_ORDER_STATUS_NOT_PAY = 1;//未完成订单
	public static final int SEAT_ORDER_STATUS_FINISH_WEIXIN_USER = 2;//微信用户已完成的座位订单
	//==============================订单删除类型-delete_type================================
	public static final int ORDER_DELETE_TYPE_WSC = 0; //订单删除类型：未删=0,用户删=1,商家删=2,两者都删=3
	public static final int ORDER_DELETE_TYPE_YHS = 1; //订单删除类型：未删=0,用户删=1,商家删=2,两者都删=3
	public static final int ORDER_DELETE_TYPE_SJS = 2; //订单删除类型：未删=0,用户删=1,商家删=2,两者都删=3
	public static final int ORDER_DELETE_TYPE_LDS = 3; //订单删除类型：未删=0,用户删=1,商家删=2,两者都删=3
	
	//==============================收银机对订单操作类型-operationType================================
		
	public static final String DELETE_ORDER_COLL = "0"; //订单操作类型：未删=0 恢复=1
		
	public static final String RESUME_ORDER_COLL = "1"; //订单操作类型：未删=0 恢复=1
	
	//===============================订单支付状态=============================================
	
	public static final Integer ORDER_PAYSTS_FINISHED=1;//支付已完成
	
	/**
	 * 分组单个结果
	 */
	public static final Integer GROUP_SINGLE=1;
	/**
	 * 可以进行支付的订单状态集合
	 */
	public static final Set<Integer> PAY_OK_FLAG = new HashSet<Integer>();
	static{
		PAY_OK_FLAG.add(ORDER_STS_YYD);
		PAY_OK_FLAG.add(ORDER_STS_YKD);
		PAY_OK_FLAG.add(ORDER_STS_PSZ);
		PAY_OK_FLAG.add(ORDER_STS_ZZXD);
		PAY_OK_FLAG.add(ORDER_STS_DQR);
	}
	/**
	 * 在完全支付时，只有支付前在此集合中的订单状态，才会将支付后的订单状态修改为已结账
	 */
	public static final Set<Integer> UP_ODR_STS_YJZ = new HashSet<Integer>();
	static{
		UP_ODR_STS_YJZ.add(ORDER_STS_YKD);
		UP_ODR_STS_YJZ.add(ORDER_STS_PSZ);
	}
	//红包状态
	public static final int RED_PACKET_USE_FLAG_NOT_USE=1;//可用
	
	//代金券状态
	public static final int COUPON_STATUS_NOT_USE=0;//未使用
	
	//资源状态
	public static final int RESOURCE_STATUS_NOT_IN_USE=0;//未被使用
	public static final int RESOURCE_STATUS_IN_USE=1;//1（已被使用）
	public static final int RESOURCE_STATUS_IN_ORDER=2;//已被预订
	
	/*-----------------------页码--------------------------*/
	public static final String PAGE_NO = "pNo";
	
	/*-----------------------每页条数--------------------------*/
	public static final String PAGE_SIZE = "pSize";
	
	/*-----------------------用户ID--------------------------*/
	public static final String USER_ID = "userId";
	
	public static final String GOODS_HOT = "HOT";//热卖
	
	public static final String GOODS_GOOD = "GOOD";//好评
	/*-----------------------限时折扣--------------------------*/
	public static final String PERIOD_TYPE_DAILY = "每天";//
	public static final String PERIOD_TYPE_WEEKLY = "每周";//
	public static final String PERIOD_TYPE_CUSTOM = "自定义";//
	public static final String CN_MONDAY = "星期一";//
	public static final String CN_TUESDAY = "星期二";//
	public static final String CN_WEDNESDAY = "星期三";//
	public static final String CN_THURSDAY = "星期四";//
	public static final String CN_FRIDAY = "星期五";//
	public static final String CN_SATURDAY = "星期六";//
	public static final String CN_SUNDAY = "星期日";//
	
	public static final String CONFIG_KEY_CR = "收银机API地址";//查询关键字
	public static final String CONFIG_KEY_ROUT = "路由器API地址";//查询关键字
	/**
	 * 设备类型-收银机
	 */
	public static final String DEVICE_TYPE_CASH = "收银机";
	
	public static final int RED_PACKET_USE_FLAG=1;//红包使用标记 0-未使用 1-已使用
	public static final int RED_PACKET_NOT_USE_FLAG=0;//红包使用标记 0-未使用 1-已使用
	/**
	 * get方法前缀
	 */
	public static final String GET_PREFIX="get";
	
	//========================redis相关常量============
	public static final String REDIS_IP="redis.ip";
	public static final String REDIS_PORT="redis.port";
	public static final String REDIS_MAX_IDLE="redis.maxIdle";
	public static final String REDIS_MAX_ACTIVE="redis.maxActive";
	public static final String REDIS_MAX_WAIT="redis.maxWait";
	
	//================================与搜索相关的常量信息===============================
	/**------------------------solr prop文件中的属性key------------------------------**/
	
	public static final String SOLR_SERVER_MODE="solr.mode";
	
	/*单节点模式*/
	public static final String SOLR_SINGLE_NODE_MODE="1";
	
	/*集群模式*/
	public static final String SOLR_CLOUD_NODE_MODE="2";
	
	/*solr服务器地址*/
	public static final String SOLR_SERVER_HOST="solr.host";
	
	public static final String SOLR_NGINX_HOST="solr.nginxHost";
	
	/*solr默认的collection*/
	public static final String SOLR_DEFAULT_COLLECTION="solr.collection";
	
	public static final String SOLR_QUERY_ALL="*:*";
	
	/**
	 * MQ服务器地址
	 */
    public static final String MQ_SERVER_HOST="mq.host";
    /**
     * MQ服务器端口
     */
    public static final String MQ_SERVER_PORT="mq.port";
    /**
     * MQ服务器端口
     */
    public static final String MQ_SERVER_INSTANCE_NAME="Producer";
    /**
     * MQ生产者唯一唯一标示
     */
    public static final String MQ_SERVER_PRODUCER_GROUP_NAME="1dcq";
	
	/**
	 * 默认的数据增量方式
	 */
	public static final String DEFAULT_CATCH_MODE="timeStamp";
	
	
	/**
	 * 商品前缀
	 */
	public static final String GOODS_ID_PREFIX="goods";
	
	/**
	 * 商品族
	 */
	
	public static final String GOODS_GROUP_ID_PREFIX="goodsGroup";
	/**
	 * 商铺前缀
	 */
	public static final String SHOP_ID_PREFIX="shop";
	
	public static final String ORDER_TYPE_XORDER="1";
	
	public static final String SEARCH_BY_USER="0";//用户搜索
	
	public static final String SEARCH_BY_OPERATION="1";//运营分类搜索
	
	public static final Map<String,String>SHOPS_SEARCH_MAP=new HashMap<String,String>();
	static{
		SHOPS_SEARCH_MAP.put("shopName", "goodsName");
		SHOPS_SEARCH_MAP.put("shopId", "goodsShopId");
		SHOPS_SEARCH_MAP.put("shopId", "primaryId");
		SHOPS_SEARCH_MAP.put("shopLogoId", "shopImage");
		SHOPS_SEARCH_MAP.put("columnId", "shopColumnId");
		SHOPS_SEARCH_MAP.put("serverMode", "goodsServerMode");
	}
	
	public static final Map<String,String>GOODS_SEARCH_MAP=new HashMap<String,String>();
	
	static{
		GOODS_SEARCH_MAP.put("shopId", "goodsShopId");	
		GOODS_SEARCH_MAP.put("goodsId", "primaryId");
		GOODS_SEARCH_MAP.put("shopName", "relateShopName");
		GOODS_SEARCH_MAP.put("unitId", "unit");
		Properties properties = ContextInitListener.COMMON_PROPS;
		if (null != properties) {
			//String cacheTimeCode = properties.getProperty("cache_time_code");
			//CACHE_TIME_CODE = (null == cacheTimeCode?CACHE_TIME_CODE:Integer.parseInt(cacheTimeCode));
			String cacheTimeIp = properties.getProperty("cache_time_ip");
			CACHE_TIME_IP = (null == cacheTimeIp?CACHE_TIME_IP:Integer.parseInt(cacheTimeIp));
			String isIpLimit = properties.getProperty("is_ip_limit");
			IS_IP_LIMIT = (null == isIpLimit?IS_IP_LIMIT:Boolean.parseBoolean(isIpLimit));
			String cacheTimeSmsparam = properties.getProperty("cache_time_smsparam");
			CACHE_TIME_SMSPARAM = (null == cacheTimeSmsparam?CACHE_TIME_SMSPARAM:Integer.parseInt(cacheTimeSmsparam));
			String shopDeadTime = properties.getProperty("shop_dead_time");
			SHOP_DEAD_TIME = (null == shopDeadTime?SHOP_DEAD_TIME:Integer.parseInt(shopDeadTime));
			String orderRsrcCallTime = properties.getProperty("order_rsrc_call_time");
			ORDER_RSRC_CALL_TIME = (null == orderRsrcCallTime?ORDER_RSRC_CALL_TIME:Integer.parseInt(orderRsrcCallTime));
		}
	}
	
	public static final Map<String,String>SEARCH_SHOPS_MAP=new HashMap<String,String>();
	static{
		SEARCH_SHOPS_MAP.put( "goodsName","shopName");
		SEARCH_SHOPS_MAP.put("goodsShopId","shopId" );
		SEARCH_SHOPS_MAP.put( "primaryId","shopId");
		SEARCH_SHOPS_MAP.put("shopImage", "shopLogoId");
		SEARCH_SHOPS_MAP.put("shopColumnPid", "columnId");
		SEARCH_SHOPS_MAP.put("goodsServerMode","serverMode");
	}
	
	public static final Map<String,String>GET_GOODS_GROUP_DETAIL=new HashMap<String,String>();
	static{
		GET_GOODS_GROUP_DETAIL.put("goodsGroupId", "goodsId");
	}
	
	
	public static final Map<String,String>SEARCH_GOODS_MAP=new HashMap<String,String>();
	static{
		SEARCH_GOODS_MAP.put( "primaryId","goodsId");
		SEARCH_GOODS_MAP.put( "unit","unitId");
	}
	/**
	 * 爬取临时索引记录表的分页大小
	 */
	public static final int    CATCH_PAGE_SIZE=20;
	
	/**
	 * 标识索引记录增加操作
	 */
	public static final String INDEX_ADD="1";
	
	/**
	 * 标识索引记录修改操作
	 */
	public static final String INDEX_UPDATE="2";
	
	/**
	 * 标识索引记录删除操作
	 */
	public static final String INDEX_DELETE="3";
	
	/**
	 * 索引类型为商铺
	 */
	public static final String INDEX_TYPE_IS_SHOP="1";
	
	/**
	 * 索引类型为商品
	 */
	public static final String INDEX_TYPE_IS_GOODS="2";
	
	
	public static final String LOCATION_FIELD="shopLocation";
	
	/**
	 * 正序排序
	 */
	public static final int SEARCH_ORDER_ASC=1;
	
	/**
	 * 逆序排序
	 */
	public static final int SEARCH_ORDER_DESC=2;
	
	public static final String REFRESH_SHOP="1";
	
	/**
	 * 刷新商品
	 */
	public static final String REFRESH_GOODS="2";
	
	public static final Map<String,String>SEARCH_ORDERBY_MAP=new HashMap<String,String>();
	
	/**
	 * 按照离我最近排序
	 */
	public static final String LOACTION_ORDERBY="0";
	static{
		SEARCH_ORDERBY_MAP.put("1", "orderByStarLevelGrade");
		SEARCH_ORDERBY_MAP.put("2", "orderBySoldNumber");
		SEARCH_ORDERBY_MAP.put("3", "orderByMemberDiscount");
		
	}
	
	
	public static final Map<String,String>SEARCH_GOODS_ORDERBY_MAP=new HashMap<String,String>();
	static{
		SEARCH_GOODS_ORDERBY_MAP.put("1", "orderByStarLevelGrade");
		SEARCH_GOODS_ORDERBY_MAP.put("2", "orderBySoldNumber");
		
	}
	//=================================与搜索相关的常量信息结束====================================
	
	public static final String MEMBER = "会员";
	public static final String SHOPER = "店铺管理员";
	public static final String SHOPS = "店铺";
	public static final String DISTRIBUTOR = "经销商";
	public static final String AGENT = "代理商";
	
	public static final String MEMBER_CHARGE = "会员充值";
	public static final String SHOPER_CHARGE = "商铺管理员充值";
	
	public static final String CHARGE_TITLE = "即时充值，即时到账";
	
	public static final String CHARGE = "充值";
	
	public static final String CHARGEBACK = "退款";
	
	public static final String REFER_MEMBER = "推荐奖励";
	
	public static final String MAN = "先生";
	
	public static final String MADAM = "女士";
	
	public static final String HUMAN = "先生/女士";
	
	public static final String MEMBER_ADD = "会员添加";
	
	public static final String INTERFACE ="/interface";
	
	/**
	 * 商户反馈成功
	 */
	public static final String MERCHANT_FEEDBACK_SUCCESS="0";
	
	//=======================================推送相关的action常量信息============================
	
	public static final String ACTION_REG_SHOP="regShop";
	
	public static final String SHOP = "shop";
	
	public static final String ACTION_SHOP_DATA_UPDATE="shopDataUpdate";
	
	public static final String ACTION_ORDER_DATA_UPDATE="order";
	
	public static final String ACTION_COMMENT="comment";
	
	/*-----------------------app下订单场景--------------------------*/
	public static final int PLACE_ORDER_LIVE = 1;		//到店点菜
	public static final int PLACE_ORDER_WM = 2;			//外卖
	public static final int PLACE_ORDER_SERVICE = 3;	//服务订单
	public static final int PLACE_ORDER_GOODS = 4;		//商品订单
	public static final int PLACE_ORDER_BEAUTY = 5;		//丽人订单
	public static final int PLACE_ORDER_VENUE = 6;		//场馆订单
	public static final int PLACE_ORDER_QIXIU = 7;    //场馆订单
	public static final int PLACE_ORDER_SCAN = 8;    //扫静态二维码订单
	/*-----------------------下订单渠道--------------------------*/
	public static final int ORDER_CHANNEL_APP = 1;			//app下订单
	public static final int ORDER_CHANNEL_POS_CQB = 2;		//POS机请求传奇宝支付
	public static final int ORDER_CHANNEL_POS_REPAY = 3;	//POS预定
	
	public static final int ORDER_CHANNEL_MERCHANTBACK=3;//商铺后台下单
	/*-----------------------配送方式--------------------------*/
	public static final int ORDER_SEND_TYPE_FASTEST = 0;	//立即配送
	public static final int ORDER_SEND_TYPE_TIMER = 1;		//定时配送
	
	public static final Integer SHOP_SUPPORT_TIME_DISCOUNT=1;//商铺支持限时折扣
	
	
	public static final Integer SHOP_SUPPORT_COUPON=2;//代表支持优惠券
	
	public static final Integer SHOP_SUPPORT_CASH_COUPON=3;//代表支持代金卷
	/*-----------------------订单支付类型order_pay_type--------------------------*/
	/**
	 * 单订单支付
	 */
	public static final int PAY_TYPE_SINGLE = 0;
	/**
	 * 多个订单支付
	 */
	public static final int PAY_TYPE_GROUP = 1;
	
	public static final int PAY_TYPE_CHARGE = 2;
	/*-----------------------订单类型--------------------------*/
	public static final int ORDER_TYPE_REPAY = 0;
	public static final int ORDER_TYPE_ALL_PRICE = 1;
	public static final int ORDER_TYPE_COUPON = 2;
	public static final int ORDER_TYPE_PAY_PAIMARY_AGENT = 3;
	public static final int ORDER_TYPE_PAY_MIDDLE_AGENT = 4;
	public static final int ORDER_TYPE_PAY__PAIMARY_UPGRADE_MIDDLE_AGENT = 5;
	/**
	 * 购买V产品
	 */
	public static final int ORDER_TYPE_PAY_V_PRODUCT = 6;
	/**
	 * 购买系统商品
	 */
	public static final int ORDER_TYPE_PAY_SYSTEM_PRODUCT = 7;


	/*-----------------------商品类型--------------------------*/
	public static final int GOODS_TYPE_GOODS = 1000;
	public static final int GOODS_TYPE_SERVICE = 2000;
	public static final int GOODS_TYPE_SET = 3000;
	/*-----------------------外卖费用设置类型--------------------------*/
	public static final int TAKEOUT_SETTINGTYPE_DD = 2;//到店
	public static final int TAKEOUT_SETTINGTYPE_PS = 1;//配送
	public static final int TAKEOUT_SETTINGTYPE_WM = 3;//外卖
	public static final int TAKEOUT_SETTINGTYPE_SM = 4;//预约上门
	/*-----------------------商铺是否启用外卖标志--------------------------*/
	public static final int SHOP_TAKEOUT_FLAG_ENABLE = 1;//启用外卖
	public static final int SHOP_TAKEOUT_FLAG_DISABLE = 0;//不启用外卖
	/*-----------------------商铺是否启用预定标志--------------------------*/
	public static final int SHOP_BOOK_FLAG_ENABLE = 1;//启用外卖
	public static final int SHOP_BOOK_FLAG_DISABLE = 0;//不启用外卖
	/*-----------------------商铺是否启用预约上门服务--------------------------*/
	public static final int SHOP_BOOK_DOOR_DISABLE = 0;//不启用
	public static final int SHOP_BOOK_DOOR_ENABLE = 1;//启用
	/*-----------------------预定商铺资源资源类型--------------------------*/
	public static final String SHOP_RSR_TYPE_JS = "2";//技师
	public static final String SHOP_RSR_TYPE_CD = "3";//场地
	
	
	/*-----------------------用户类型，会员-0,店铺管理者-10 ------*/
	
	public static final Integer USER_TYPE_ZREO = 0;
	
	public static final Integer USER_TYPE_TEN = 10;
	
	//上传图片支持的格式
	public static final String IMAGE_FORMAT = "BMP|JPEG|JP2|JPG|GIF|TIFF|PNG|EXIF|WBMP|MBM";
	
	//1dcq_setting：短信验证码有效时间及重复请求时间，格式：{"retime":"60","eftime":"30"}
	public static final String SETTING_CODE_MOBILE_CODE="Common";
	public static final String SETTING_KEY_MOBILE_CODE="appRegistration";
	public static final String SETTING_KEY_PRAISE_CODE="praise";
	//短信验证码默认有效时间数据库取值key
	public static final String MOBILE_CODE_TIMEOUT_KEY="eftime";
	/**
	 * 配置表自动好评key
	 */
	public static final String SETTING_CODE_PRAISE_KEY="ptime";
	
	
	public static final String PAY_REDPACKET_SPLIT_KEY = ";";//红包支付，多个红包分隔关键字	
	
	public static final String PAY_CASH_COUPON_SPLIT_KEY = ";";//代金券支付，多个代金券分隔关键字
	
	
	/**
	 * 店铺状态正常
	 */
	public static final Integer SHOP_STATUS_NORMAL=0;
	/**
	 * 店铺状态删除
	 */
	public static final Integer SHOP_STATUS_DELETE=3;
	
	/**
	 * 商品状态正常
	 */
	public static final Integer GOODS_STATUS_NORMAL=1;
	
	//支付状态：待反馈支付进度-0,支付成功-1,支付失败-2
	public static final Integer TRANSACTION_STATUS_WAIT = 0;
	public static final Integer TRANSACTION_STATUS_FINISH = 1;
	public static final Integer TRANSACTION_STATUS_FAIL = 2;

	//交易记录交易类型：0（消费），1（充值），2（会员卡充值）
	public static final Integer TRANSACTION_TYPE_CONSUME = 0;
	public static final Integer TRANSACTION_TYPE_PAY = 1;
	
	public static final int TRANSACTION_TYPE_ALISCAN=5;
	
	public static final int TRANSACTION_TYPE_WXSCAN=6;

	/**
	 * bill状态进行中
	 */
	public static final Integer BILL_STATUS_FLAG_PROCESS = 1;
	/**
	 * bill状态完成
	 */
	public static final Integer BILL_STATUS_FLAG_FINISH = 0;
	
	/**
	 * 商铺账单已完成
	 */
	public static final Integer SHOP_BILL_STATUS_OVER = 2;
	
	/**
     * 商铺账单处理中
     */
    public static final Integer SHOP_BILL_STATUS_ING = 1;
	
    /**
     * 平台账单处理中
     */
    public static final Integer PLATFORM_BILL_STATUS_ING = 1;
    
	/**
     * 平台账单已完成 -2
     */
    public static final Integer PLATFORM_BILL_STATUS_OVER = 2;
	/**
	 * 账户资金减少 -1
	 */
	public static final Integer BILL_DIRECTION_DOWN = -1;
	/**
	 * 账户资金增加 1
	 */
	public static final Integer BILL_DIRECTION_ADD = 1; 
	//----------------------短信下发获取内容key---------------------------	
	public static final String MSG_SETTING_KEY_SMS_PWD="addAgency";//php后台发送经销商、代理商账号密码，查询短信内容模板key
	
	/* ----------------------短信内容替换值start------------------------------- */
	public static final String PHP_SMS_USERNAME="username";//php后台发送经销商、代理商账号密码，内容中需要替换的关键字
	public static final String PHP_SMS_MOBILE="mobile";//php后台发送经销商、代理商账号密码，内容中需要替换的关键字
	public static final String PHP_SMS_PWD="pwd";//php后台发送经销商、代理商账号密码，内容中需要替换的关键字
	
	public static final String SMS_AMOUNT = "amount";
	public static final String SMS_ACOUNT_AMOUNT = "acountAmount";
	public static final String SMS_SHOP_NAME = "shopName";
	public static final String SMS_CODE = "code";
	/* ----------------------短信内容替换值end------------------------------- */
	
	public static final String USER_REGISTER="注册";
	public static final String USER_MODIFY_PWD="修改密码";
	public static final String USER_MODIFY_MOBILE="更改手机号码";
	public static final String USER_BACK_PWD="找回密码";
	public static final String USER_ADD="会员添加";
	public static final String SEND_ROUTER_PWD="路由器密码";
	public static final String BACK_PAY_PWD="找回支付密码";
	public static final String RESET_PAY_PWD="重设支付密码";
	public static final String LACK_OF_BALANCE = "余额不足";
	public static final String SMS_PAY_CODE = "短信支付验证码";
	public static final String SMS_PAY_SUCC = "短信支付成功";
	public static final String USER_REGISTER1="注册1";
	public static final String USER_MODIFY_PWD1="修改密码1";
	public static final String USER_MODIFY_MOBILE1="更改手机号码1";
	public static final String USER_BACK_PWD1="找回密码1";
	public static final String USER_ADD1="会员添加1";
	public static final String SEND_ROUTER_PWD1="路由器密码1";
	public static final String BACK_PAY_PWD1="找回支付密码1";
	public static final String RESET_PAY_PWD1="重设支付密码1";
	public static final String LACK_OF_BALANCE1 = "余额不足1";
	public static final String SMS_PAY_CODE1 = "短信支付验证码1";
	public static final String SMS_PAY_SUCC1 = "短信支付成功1";
	public static final String BACK_SMS_PAY_CODE = "后台短信支付"; //验证码
	public static final String BACK_SMS_PAY_CODE1 = "后台短信支付1";
	public static final String SMS_PART_PAY="余额不足,部分支付";//短信支付，余额全部支付
	public static final String SHOP_DEPOSIT_ARREARS = "保证金欠费";
	public static final String SHOP_DEPOSIT_ARREARS1 = "保证金欠费1";
	public static final String SHOP_DEPOSIT_LESS = "保证金余额不足";
	public static final String SHOP_DEPOSIT_LESS1 = "保证金余额不足1";
	public static final String USER_FOR_REGISTRATION = "帮人注册";
	public static final String USER_FOR_REGISTRATION1 = "帮人注册1";
	public static final String USER_REGISTR_SUCCESS = "注册成功";
	public static final String USER_REGISTR_SUCCESS1 = "注册成功";
	public static final String USER_REG="reg";
	public static final String USER_REG1="reg1";
	public static final String BIND_WEIXIN_USER="bindWeiXinUser";
	public static final String BIND_WEIXIN_USER1="bindWeiXinUser1";
	public static final String SHOP_AUDIT_PASS = "shop_audit_pass";
	public static final String SHOP_AUDIT_NOPASS = "shop_audit_nopass";
	public static final String ADD_MEMBER_CARD = "addMemberCard";
	public static final String SET_PAY_PASSWORD = "setPayPassword";
	public static final String USER_LOGIN = "login";
	public static final String ADD_EMPLOYE = "addEmploye";
	public static final String WECHAT_RECHARGE = "weChatRecharge";
	public static final String WECHAT_PAY = "weChatPay";
	public static final String SMS_PAY_CQB = "sms_pay_cqb";
	public static final String SMS_PAY_XFK = "sms_pay_xfk";
	public static final String SMS_PAY_HB = "sms_pay_hb";
	public static final String SMS_PAY_CQB_XFK = "sms_pay_cqb_xfk";
	public static final String SMS_PAY_CQB_HB = "sms_pay_cqb_hb";
	public static final String SMS_PAY_XFK_HB = "sms_pay_xfk_hb";
	
	public static final String JOIN_ACTIVITY = "joinActivity";//参与活动短信模板
	public static final String CANCLE_ACTIVITY = "cancleActivity";//取消活动短信模板
	public static final String RED_PACKET_EXPIRED = "redPacketExpired";//红包过期提醒短信模板
	
	/**
	 * 充值成功-用户
	 */
	public static final String CHARGE_USER = "charge_user";
    /**
     * 充值成功-商铺
     */
    public static final String CHARGE_SHOP = "charge_shop";
	public static Set<String> SMS_CONTENT_KEYS = new HashSet<String>();
	static{
		SMS_CONTENT_KEYS.add(USER_REGISTER);
		SMS_CONTENT_KEYS.add(USER_MODIFY_PWD);
		SMS_CONTENT_KEYS.add(USER_MODIFY_MOBILE);
		SMS_CONTENT_KEYS.add(USER_BACK_PWD);
		SMS_CONTENT_KEYS.add(USER_ADD);
		SMS_CONTENT_KEYS.add(SEND_ROUTER_PWD);
		SMS_CONTENT_KEYS.add(BACK_PAY_PWD);
		SMS_CONTENT_KEYS.add(RESET_PAY_PWD);
		//2015-09-06添加
		SMS_CONTENT_KEYS.add(LACK_OF_BALANCE);
		SMS_CONTENT_KEYS.add(SMS_PAY_CODE);
		SMS_CONTENT_KEYS.add(SMS_PAY_SUCC);
		
		//备用模板，当1分钟内同一手机号码重复获取验证码时，取前一条的验证码发送不同的短信内容
		SMS_CONTENT_KEYS.add(USER_REGISTER1);
		SMS_CONTENT_KEYS.add(USER_MODIFY_PWD1);
		SMS_CONTENT_KEYS.add(USER_MODIFY_MOBILE1);
		SMS_CONTENT_KEYS.add(USER_BACK_PWD1);
		SMS_CONTENT_KEYS.add(USER_ADD1);
		SMS_CONTENT_KEYS.add(SEND_ROUTER_PWD1);
		SMS_CONTENT_KEYS.add(BACK_PAY_PWD1);
		SMS_CONTENT_KEYS.add(RESET_PAY_PWD1);
		SMS_CONTENT_KEYS.add(LACK_OF_BALANCE1);
		SMS_CONTENT_KEYS.add(SMS_PAY_CODE1);
		SMS_CONTENT_KEYS.add(SMS_PAY_SUCC1);
		SMS_CONTENT_KEYS.add(BACK_SMS_PAY_CODE);
		SMS_CONTENT_KEYS.add(SMS_PAY_CQB);
		SMS_CONTENT_KEYS.add(SMS_PAY_XFK);
		SMS_CONTENT_KEYS.add(SMS_PAY_HB);
		SMS_CONTENT_KEYS.add(SMS_PAY_CQB_XFK);
		SMS_CONTENT_KEYS.add(SMS_PAY_CQB_HB);
		SMS_CONTENT_KEYS.add(SMS_PAY_XFK_HB);
		SMS_CONTENT_KEYS.add(SMS_PART_PAY);
		SMS_CONTENT_KEYS.add(SHOP_DEPOSIT_ARREARS);
		SMS_CONTENT_KEYS.add(SHOP_DEPOSIT_ARREARS1);
		SMS_CONTENT_KEYS.add(SHOP_DEPOSIT_LESS);
		SMS_CONTENT_KEYS.add(SHOP_DEPOSIT_LESS1);
	    SMS_CONTENT_KEYS.add(USER_FOR_REGISTRATION);
	    SMS_CONTENT_KEYS.add(USER_FOR_REGISTRATION1);
	    SMS_CONTENT_KEYS.add(USER_REGISTR_SUCCESS);
	    SMS_CONTENT_KEYS.add(USER_REGISTR_SUCCESS1);
	    SMS_CONTENT_KEYS.add(USER_REG);
        SMS_CONTENT_KEYS.add(USER_REG1);
        SMS_CONTENT_KEYS.add(BIND_WEIXIN_USER);
        SMS_CONTENT_KEYS.add(BIND_WEIXIN_USER1);
        SMS_CONTENT_KEYS.add(SHOP_AUDIT_PASS);
        SMS_CONTENT_KEYS.add(SHOP_AUDIT_NOPASS);
        SMS_CONTENT_KEYS.add(CHARGE_USER);
        SMS_CONTENT_KEYS.add(CHARGE_SHOP);
        SMS_CONTENT_KEYS.add(ADD_MEMBER_CARD);
        SMS_CONTENT_KEYS.add(SET_PAY_PASSWORD);
        SMS_CONTENT_KEYS.add(USER_LOGIN);
        SMS_CONTENT_KEYS.add(WECHAT_RECHARGE);
        SMS_CONTENT_KEYS.add(WECHAT_PAY);
        
        SMS_CONTENT_KEYS.add(JOIN_ACTIVITY);
        SMS_CONTENT_KEYS.add(CANCLE_ACTIVITY);
        SMS_CONTENT_KEYS.add(RED_PACKET_EXPIRED);
	}
	/**
	 * 限制发送短信，如果是这个集合中的key，则需要进行是否是平台会员校验，如果不是，则抛出异常
	 * 用户找回密码、修改密码、更改手机号码、找回密码、找回支付密码、重设支付密码等操作时，需要验证是否为已注册用户，如果不是，则提示未注册
	 */
	public static Set<String> LIMIT_SMS_CONTENT_KEYS = new HashSet<String>();
	static{
		LIMIT_SMS_CONTENT_KEYS.add(USER_MODIFY_PWD);
		LIMIT_SMS_CONTENT_KEYS.add(USER_BACK_PWD);
		LIMIT_SMS_CONTENT_KEYS.add(SEND_ROUTER_PWD);
		LIMIT_SMS_CONTENT_KEYS.add(BACK_PAY_PWD);
		LIMIT_SMS_CONTENT_KEYS.add(RESET_PAY_PWD);
		LIMIT_SMS_CONTENT_KEYS.add(SET_PAY_PASSWORD);
		LIMIT_SMS_CONTENT_KEYS.add(WECHAT_RECHARGE);
		LIMIT_SMS_CONTENT_KEYS.add(WECHAT_PAY);
	}
	/**
	 * 限制发送短信，如果是这个集合中的key，则需要进行是否是平台会员校验，如果是，则抛出异常
	 *  用户注册，如果为已经是注册用户,则不需要发送验证码，提示已经注册
	 */
	public static Set<String> LIMIT_SMS_CONTENT_KEYS_REG = new HashSet<String>();
	static{
		LIMIT_SMS_CONTENT_KEYS_REG.add(USER_REGISTER);
		LIMIT_SMS_CONTENT_KEYS_REG.add(USER_MODIFY_MOBILE);
		LIMIT_SMS_CONTENT_KEYS_REG.add(USER_FOR_REGISTRATION);
		LIMIT_SMS_CONTENT_KEYS_REG.add(USER_REG);
	}
	
    /**
     * 防攻击
     */
    public static Set<String> LIMIT_SMS_PREVENT_ATTACK_KEYS = new HashSet<String>();
    static
    {
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_REGISTER);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_MODIFY_MOBILE);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_FOR_REGISTRATION);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_MODIFY_PWD);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_BACK_PWD);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_REG);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(BIND_WEIXIN_USER);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(ADD_MEMBER_CARD);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(SET_PAY_PASSWORD);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(USER_LOGIN);
        LIMIT_SMS_PREVENT_ATTACK_KEYS.add(ADD_EMPLOYE);
    }
	
	/*------------------------收银机退单------------------------------*/
	//同意退单
	public static final String AUDITFLAG_ZERO = "0";
	//按支付方式退款
	public static final String REFUNDTYPE_ONE = "1";
	
	/*------------------------------Solr节点的状态--------------------------------*/
	public static final Integer SOLR_NODE_NORMAL=1;
	
	public static final Integer SOLR_NODE_REMOVE=2;//节点移除 表示action 也可表示状态
	
	public static final Integer SOLR_NODE_CONFIRM_REMOVE=3;//待重建
	
	public static final Integer SOLR_NODE_INDEXING=4;//正在索引
	
	public static final Integer SOLR_NODE_INDEX_END=5;//索引结束
	
	public static final Integer SOLR_NODE_RECOVERY=1;//节点恢复 表示 action
	
	public static final String  GOODS_GROUP_SPLIT_STR="##,";
	
	public static final String REFRESH_SOLR_SUFFIX_URL="/interface/refreshSolrStatus";//刷新solr机器的状态
	
	public static final String SOLR_EXCEPTION_INFORM_KEY="solr.exceptionInformServer";
	
	
	public static final int CODE_COMMENT_TYPE_GOODS =1;
	public static final int CODE_COMMENT_TYPE_SHOP =2;
	public static final int CODE_COMMENT_TYPE_ORDER =3;
	
	//mac地址正则表达式
	public static final String PATTERN_MAC = "[A-F0-9]{2}:[A-F0-9]{2}:[A-F0-9]{2}:[A-F0-9]{2}:[A-F0-9]{2}:[A-F0-9]{2}";  
	
	//平台服务备注默认商铺编号为0
	public static final long COOKING_DETAIL_DFT_SHOPID=0;
	
	
	
	//推荐类型
	public static final Integer USER_RECOMMAND_BY_USER=0;//用户推荐
	
	public static final Integer USER_RECOMMAND_BY_SHOP=1;//商铺推荐
	
	public static final Integer SHOP_RECOMMAND_BY_SHOP=10;//代表商铺推荐商铺
	
	public static final double DEFAULT_GIVE_AMOUNT=500.0;
	
	
	
	
	/**
	 * 业务类型为商品
	 */
	public static final Integer BIZ_TYPE_IS_GOODS=8;
	/**
	 * 附件类型为商品族
	 */
	public static final Integer BIZ_TYPE_IS_GOODS_GROUP=9;
	/**
	 * 附件类型为商铺
	 */
	public static final Integer BIZ_TYPE_IS_SHOP=1;
	/**
	 * 附件类型为用户
	 */
	public static final Integer BIZ_TYPE_IS_USER=2;
	/**
	 * 图片类型为缩略图
	 */
	public static final Integer PIC_TYPE_IS_SUONUE=1;
	/**
	 * 轮播图
	 */
	public static final Integer PIC_TYPE_IS_CYCLE_PLAY=2;
	/**
	 * 法人(商铺)=3(biz_index =1营业执照,=2组织机构代码证,=3税务登记证,=4经营许可证(原，现需要单独出来))
	 */
	public static final Integer PIC_TYPE_IS_BUS_LICENCE=3;
	/**
	 * 图片类型为个人技能证书
	 */
	public static final Integer PIC_TYPE_IS_PERSONAL_SKILL=5;
	/**
	 * biz_index =1营业执照
	 */
	public static final Integer PIC_BIZ_INDEX_IS_BUS_LICENSE=1;
	/**
	 * biz_index =2组织机构代码证
	 */
	public static final Integer PIC_BIZ_INDEX_IS_ORG_CODE=2;
	/**
	 * biz_index ==3税务登记证
	 */
	public static final Integer PIC_BIZ_INDEX_IS_TAX_REG_CERTIFICATE=3;
	/**
	 * biz_index=4经营许可证(原，现需要单独出来)
	 */
	public static final Integer PIC_BIZ_INDEX_IS_BUS_CERTIFICATE=4;
	

	
	//代金券账单类型
	public static final Integer USER_CASHCOUPON_GET = 1; //获取
	public static final Integer USER_CASHCOUPON_USE = 2; //使用
	public static final Integer USER_CASHCOUPON_BACK = 3; //返回
	
	/*============================丽人板块=================================*/

	/*------------------------------商铺预约设置------------------------------*/
	public static final int BK_SETTING_OP_TYPE_XZ = 0; //操作类型，0-新增；1修改
	public static final int BK_SETTING_OP_TYPE_XG = 1; //操作类型，0-新增；1修改
	/*------------------------------商品族操作------------------------------*/
	public static final int GGROUP_OP_TYPE_XZ = 0; //操作类型，0-新增；1修改
	public static final int GGROUP_OP_TYPE_XG = 1; //操作类型，0-新增；1修改
	/*------------------------------商品族上下架操作------------------------------*/
	public static final int GGROUP_UPDATE_OP_TYPE_SC = 0; //操作类型，0-删除；1-下架；2-上架
	public static final int GGROUP_UPDATE_OP_TYPE_SJ = 2; //操作类型，0-删除；1-下架；2-上架
	public static final int GGROUP_UPDATE_OP_TYPE_XJ = 1; //操作类型，0-删除；1-下架；2-上架
	
	/*----------------订单操作类型operateType----------------------*/
	
	//操作类型，0-接单，1-拒绝接单，2-开单，3-结账，4-同意退订，5-拒绝退订
	public static final String OPERATE_TYPE_JD = "0"; 
	public static final String OPERATE_TYPE_JJJD = "1"; 
	public static final String OPERATE_TYPE_KD = "2"; 
	public static final String OPERATE_TYPE_JZ = "3"; 
	public static final String OPERATE_TYPE_TYTD = "4"; 
	public static final String OPERATE_TYPE_JJTD = "5"; 
	
	/*----------------分隔符----------------------*/
	public static final String COMMA_SEPARATOR = ","; // 分隔符：逗号
	public static final String SEPARATOR_JH = "#";//#号分隔符
	public static final String SEPARATOR_HG = "-";//-分隔符
	public static final String SPACE = " ";//空格
	
	/*----------1dcq_order_shop_resource表status的值---------*/
	public static final int OSRESOURCE_STATUS_INVALID = 0;
	public static final int OSRESOURCE_STATUS_VALID = 1;
	
	/*-----------------一点管家登录接口登录用户的类型------------------*/
	public static final int SHOP_OWNER_TYPE = 1;
	public static final int SHOP_EMPLOYEE_TYPE = 2;
	
	/*--------------雇员状态----------------------------*/
	public static final int SHOP_EMPLOYEE_STATUS_NORMAL = 1;
	/*--------------已验证----------------------------*/
	public static final int SHOP_EMPLOYEE_IS_CHECK = 1;
	/*--------------没验证----------------------------*/
	public static final int SHOP_EMPLOYEE_NOT_IS_CHECK = 0;
	
	/*----------------------order_service_type对应值--------------------*/
	public static final int ORDER_SERVICE_TYPE_STORE = 0; //到店服务-0
	public static final int ORDER_SERVICE_TYPE_DOOR = 1; //上门服务-1
	
	/*----------------------online_pay_confirm_type对应值--------------------*/
	public static final int ONLINE_PAY_CONFIRM_TYPE_DX = 1; //短信确认支付-1
	public static final int ONLINE_PAY_CONFIRM_TYPE_APP = 0; //APP确认支付-1
	
	/*---------------------技师状态-------------------------*/
	/**
	 * 技师状态-服务中
	 */
	public static final int TECH_STATUS_FWZ = 1;
	/**
	 * 技师状态-空闲中
	 */
	public static final int TECH_STATUS_KXZ= 2;
	/**
	 * 技师状态-休息中
	 */
	public static final int TECH_STATUS_XXZ = 3;
	
	/*---------------------班次类型----------------------*/
	/**
	 * 班次类型-休息
	 */
	public static final int CLASSES_TYPE_XX = 0;
	/**
	 * 班次类型-早班
	 */
	public static final int CLASSES_TYPE_ZB = 1;
	/**
	 * 班次类型-晚班
	 */
	public static final int CLASSES_TYPE_WB = 2;
	/**
	 * 班次类型-通班
	 */
	public static final int CLASSES_TYPE_TB = 3;
	
	/*---------------------缓存失效时间----------------------*/
	public static final int CACHE_EX_TIME_SHOP = 60*60;//商铺表1小时
	public static final int CACHE_EX_TIME_GOODS = 12*60*60;//商品表12小时
	/**
	 * 附件缓存失效时间 1天
	 */
	public static final int CACH_EX_TIME_ATTACHMENT = 86400;
	public static final String NIKE_NAME_PREFIX ="1dcq_";
	/*---------------------app配置key----------------------*/
	public static final String APP_CFG_KEY_GOODS_SOLD_LAST_UPDATE_TIME ="goods_sold_last_update_time";
	
	
	/*--------------------------------------------------*/
	/**
	 * 内存中存放的Properties静态对象
	 */
	public static Map<String, Properties> PROP_MAP = new HashMap<String, Properties>();
	static{
		PROP_MAP.put("redis", ContextInitListener.REDIS_PROPS);
		PROP_MAP.put("jpush", ContextInitListener.JPUSH_PROPS);
		PROP_MAP.put("solr", ContextInitListener.SOLR_PROPS);
		PROP_MAP.put("common", ContextInitListener.COMMON_PROPS);
		PROP_MAP.put("threadPool", ContextInitListener.THREAD_POOL_PROPS);
	}
	
	/*------------------------商品状态----------------------------------*/
	/**
	 * 商品状态-下架
	 */
	public static final int GOODS_STATUS_XJ = 0;
	/**
	 * 商品状态-上架
	 */
	public static final int GOODS_STATUS_SJ = 1;
	/**
	 * 商品状态-删除
	 */
	public static final int GOODS_STATUS_SC = 2;
	/**
	 * 商品状态-草稿
	 */
	public static final int GOODS_STATUS_CG = 3;
	
	/*------------------------商品族状态----------------------------------*/
	/**
	 * 商品族状态-下架
	 */
	public static final int GOODS_GROUP_STS_XJ = 0;
	/**
	 * 商品族状态-上架
	 */
	public static final int GOODS_GROUP_STS_SJ = 1;
	/**
	 * 商品族状态-删除
	 */
	public static final int GOODS_GROUP_STS_SC = 2;
	/**
	 * 商品族状态-草稿
	 */
	public static final int GOODS_GROUP_STS_CG = 3;
	
	//==========================分账描述====billDesc start=====================
	public static final String BILL_DESC_RECOMMAND_SHOP="推荐奖励-推荐店铺";
	
	public static final String BILL_DESC_RECOMMAND_USER="推荐奖励-推荐用户";
	
	
	//==========================分账类型=====billType start====================
	public static final String BILL_PAY_REWARD="支付会员奖励";
	
	public static final String BILL_TYPE_COMSUME="消费";
	
	public static final String BILL_TYPE_SHOPINCOME="店铺收入";
	
	public static final String BILL_TYPE_DEPOSIT_CHANGE="划扣保证金";

	public static final String BILL_TYPE_PLATFORM_PRESENT="兑换平台礼品";
	public static final int MONEY_SOURCE_CQB = 1;//资金来源-传奇宝
	public static final int MONEY_SOURCE_BZJ = 10;//资金来源-保证金
	public static final int MONEY_SOURCE_CP = 11;//资金来源-消费币
	/**
	 * 积分设置
	 */
	public static final String MONEY_TO_INTEGRAL = "moneyToIntegral";
	/**
	 * 打印设置
	 */
	public static final String PRINT_ITEMS = "printItems";
	/**
	 * 保证金欠费
	 */
	public static final String NOT_ENOUGH = "保证金欠费";
    /**
     * 保证金余额不足
     */
	public static final String NEARLY_NOT_ENOUGH = "保证金余额不足";
	
	//=========================是否参与打折goods_settle_flag======================
	/**
	 * 打折
	 */
	public static final int GOODS_SETTLE_FLAG_TRUE = 1;//打折
	/**
	 * 不打折
	 */
	public static final int GOODS_SETTLE_FLAG_FALSE = 0;//不打折
	//=========================是否参与返点goods_rebate_flag======================
	/**
	 * 返点
	 */
	public static final int GOODS_REBATE_FLAG_TRUE = 1; //返点
	/**
	 * 不返点
	 */
	public static final int GOODS_REBATE_FLAG_FALSE = 0; // 不返点
	
	//=========================是否结算======================
	
	public static final int USER_SETTLE_FLAG_TRUE = 1; //用户已结算
	
	public static final int SHOP_SETTLE_FLAG_TRUE = 1; //店铺已结算
	
	//=========================订单结算方式 订单表settle_type======================
	/**
	 * 按订单实际结算价order_real_settle_price
	 */
	public static final int ORDER_SETTLE_ONE = 1;
	/**
	 * 按商品目录价结算
	 */
	public static final int ORDER_SETTLE_ZERO = 0; 
	/**
	 * '结算方式: 1-按抽成比例结算
	 */
	public static final int SETTLE_TYPE_PERCENTAGE= 1;	
	/**
	 * '结算方式: 2-按折扣结算'
	 */
	public static final int SETTLE_TYPE_DISCOUNT = 2;
	/**
	 *  重新结算类型，1为直接重新结算，2为先回滚，再结算
	 */
	public static final String ROLL_BACK_ONE = "1";
	/**
	 * 重新结算类型，1为直接重新结算，2为先回滚，再结算
	 */
	public static final String ROLL_BACK_TWO = "2";
	 /**
     * 销售商品类型=1
     */
    public static final String BILL_TYPE_SELL = "1";
    /**
     * 推荐奖励=6
     */
   public static final String BILL_TYPE_REWARD = "6";
   
   /**
    * 借记卡公钥后30位
    */
   public static final String DEBIT_CARD_PUBLIC_KEY_ALL = "30819d300d06092a864886f70d010101050003818b0030818702818100c97f661ab054503b742769ce6a5f65cfe97adc3385a59910026020f4981a29e62b588ace130b656ca446c3add00ba090067292c56ac881696d9de6d6617c426620d4c08e45607cb90a870ea43bccc38fe701d406b5d3c9a4874392139d56294c6cd38d34f51e19da8fd82ffa58973f623a4a4dd493725f999117ec2a3958de67020111";
   /**
    * 贷记卡公钥后30位
    */
   public static final String CREDIT_CARD_PUBLIC_KEY_ALL = "30819d300d06092a864886f70d010101050003818b0030818702818100ae110cef959720184bb0e19f50c600e9afeeda47f40930cffab7150545e848f8593454dcc8a859e9fd078f1eec24978a6362829b2f5d3222285d19245b71c6ebdfe8c86be1e42e58fc701847c00c38372478664e04021dc01ed0181a79d619183aeb83809cb3e71c4821e063b6bb04917b54e0ff48d9f8d3dc3af892ef641109020111";
   
   /**
    * 借记卡公钥
    */
   public static final String DEBIT_CARD_PUBLIC_KEY = "93725f999117ec2a3958de67020111";
   /**
    * 贷记卡公钥
    */
   public static final String CREDIT_CARD_PUBLIC_KEY = "48d9f8d3dc3af892ef641109020111";
   /**
    * 类型-借记卡
    */
   public static final String POS_TYPE_DEBIT_CARD = "1";
   /**
    * 类型-贷记卡
    */
   public static final String POS_TYPE_CREDIT_CARD = "2";
   /**
    * 类型-B2B
    */
   public static final String POS_TYPE_B2B = "3";
   /**
    * 建设银行请求url前缀
    */
   public static final String CCB_REQUEST_URL = "https://ibsbjstar.ccb.com.cn/app/ccbMain?";
 
   /**
    * 商户代码,固定
    */
   public static final String MERCHANTID = "105584073990442";
   /**
    * 
    * 柜台代码：751849699（借记卡）
    */
   public static final String POSID_DEBIT_CARD = "751849699";
   /**
    * 
    * 柜台代码：578932632（贷记卡）
    */
   public static final String POSID_CREDIT_CARD = "578932632";
   /**
    * 
    * 556524841(B2B)
    */
   public static final String POSID_B2B = "556524841";
   /**
    *  柜台代码：支行代码
    */
   public static final String BRANCHID = "442000000";
   /**
    * 初始化订单号
    */
   public static final long INIT_ORDER_ID = 10000;
   /**
    * 币种-默认人民币
    */
   public static final String CURCODE = "01";
   /**
    * 交易码
    */
   public static final String TXCODE="520100";
   /**
    * 防钓鱼接口
    */
   public static final int TYPE=1;
   /**
    * 网关类型 ：W0Z1或W0Z2    仅显示帐号支付标签
    */
   public static final String GATEWAY="W1Z1";
   /**
    * 客户注册信息
    */
   public static final String REGINFO= "1dcq-reginfo";
   /**
    * 商品信息
    */
   public static final String PROINFO= "1dcq-proinfo";
   /**
    * 成功标示
    */
   public static final String SUCCESS_TYPE = "Y";
   /**
    * 失败标示
    */
   public static final String FAIL_TYPE = "N";
   /**
    * 账户类型
    */
   public static final String ACC_TYPE = "30";
   
   /**
    * 建设银行名称
    */
   public static final String CCB_NAME = "建设银行";
   public static final String RD_ORG_NAME_WEIXIN = "微信";
   /**
    * 请求来源-php后台
    */
   public static final String TERMINAL_TYPE_PHP = "php后台";
   /**
    * 请求来源-php后台
    */
   public static final String TERMINAL_TYPE_ANDROID = "Android客户端";
   
   /**
    * 请求来源-WEB
    */
   public static final String TERMINAL_TYPE_WEB = "WEB";
   /**
    * 转充
    */
   public static final String TERMINAL_TYPE_CHARGE = "转充";
   public static final String SHOP_MEMBER_CARD_RECHARGE = "card_charge";
   public static final String SHOP_MEMBER_CARD_RECHARGE_SEND = "card_charge_send";
   /**
    * 商铺充值类型
    */
   public static final int TRANSACTION_TYPE_SHOP= 3;
   /**
    * 商铺充值类型
    */
   public static final int TRANSACTION_TYPE_INNER_CHARGE= 4;
   /**
    * order类型
    */
   public static final int TRANSACTION_TYPE_ORDER= 0;
   /**
    * 购买店铺插件
    */
   public static final int TRANSACTION_TYPE_SHOP_PLUGIN= 7;
   /**
    * 用户充值类型
    */
   public static final int TRANSACTION_TYPE_USER= 1;
   /**
    * 单订单支付
    */
   public static final int ORDER_PAY_TYPE_SINGLE= 0;
   

	
	//-------------------扫码支付常量---------------
	
	public static final String ALISCANPAY_KEY="Aliscanpay";
	
	//------------------账单类型------------------
	public static final String BILL_TYPE_WITH_DRAW="4";//提现
	
	public static final String BILL_TYPE_RECOMMAND_REWARD="6";//推荐奖励
	
	public static final String BILL_TYPE_SALE="1";//销售商品
	
	public static final int BILL_SEARCH_XORDERTYPE=1;//查询非会员订单表
	
	public static final int BILL_SEARCH_ORDER_TYPE=2;//查询会员订单
	
	public static final String IDGJ_RECOMMAND_REWARD_NAME="一点传奇";
	
	public static final String IDGJ_SALE_GOODS_NAME="收银";
	
	public static final String IDGJ_SALE_DRAW_NAME="提现";
	
	
	//==============1dcq_shop_bill表中account_type账户类型值========
    /**
     * 线上营业收入
     */
    public static final int SHOP_ACCOUNT_TYPE_INCOME = 0;
    /**
     * 平台奖励
     */
    public static final int SHOP_ACCOUNT_TYPE_REWARD = 1;
    /**
     * 冻结资金
     */
    public static final int SHOP_ACCOUNT_TYPE_FREEZE = 2;
    /**
     * 保证金
     */
    public static final int SHOP_ACCOUNT_TYPE_DEPOSIT = 3;
    
    /**
     * 生意金
     */
    public static final int SHOP_ACCOUNT_TYPE_MARKET = 4;
    
    //==============1dcq_shop_bill表中shop_bill_type账户类型值========
    /**
     * 销售商品
     */
    public static final String SHOP_BILL_TYPE_SALE = "1";
    /**
     * 支付平台服务费
     */
    public static final String SHOP_BILL_TYPE_PAY = "2";
    /**
     * 购买红包
     */
    public static final String SHOP_BILL_TYPE_REDPACKET = "3";
    /**
     * 提现
     */
    public static final String SHOP_BILL_TYPE_WITHDRAW = "4";
    /**
     * 充值
     */
    public static final String SHOP_BILL_TYPE_RECHARGE = "5";
    /**
     * 推荐奖励
     */
    public static final String SHOP_BILL_TYPE_REWARD = "6";
    /**
     * 提现退回
     */
    public static final String SHOP_BILL_TYPE_WITHDRAW_BACK = "7";
    /**
     * 冻结资金
     */
    public static final String SHOP_BILL_TYPE_FREEZE = "8";
    /**
     * 解冻资金
     */
    public static final String SHOP_BILL_TYPE_UNFREEZE = "9";
    /**
     * 转充
     */
    public static final String SHOP_BILL_TYPE_TRANSFER = "10";
    /**
     * 充值卡充值
     */
    public static final String SHOP_BILL_TYPE_CASH_CARDR = "11";
    /**
     * 购买店铺插件
     */
    public static final String SHOP_BILL_TYPE_PLUGIN = "12";
    /**
     * 连锁提现转出
     */
    public static final String SHOP_BILL_TYPE_OUT = "22";
    /**
     * 连锁提现转入
     */
    public static final String SHOP_BILL_TYPE_ENTER = "21";
    
    /**
     * 划扣套餐费用
     */
    public static final String SHOP_BILL_TYPE_PACKAGE_PAY = "30";
    
    /**
     * 停用扣除保证金
     */
    public static final String SHOP_BILL_TYPE_DEPOSIT_PAY = "31";
    
    /**
     * 退还赠送保证金
     */
    public static final String SHOP_BILL_TYPE_DEPOSIT_BACK = "32";
    
    /**
     * 发红包
     */
    public static final String SHOP_BILL_TYPE_SEND_REDPACKET = "41";
    /**
     * 提现手续费
     */
    public static final String SHOP_BILL_TYPE_WITHDRAW_COMMISSION = "44";
    /**
     * 退回提现手续费
     */
    public static final String SHOP_BILL_TYPE_RETURN_WITHDRAW_COMMISSION = "77";
    
    /**
     * 返还生意金
     */
    public static final String SHOP_BILL_TYPE_RETURN_MARKET = "60";
    
    /**
     * 消费生意金
     */
    public static final String SHOP_BILL_TYPE_CONSUME_MARKET = "61";
    
    /**
     * 购买V产品返利
     */
    public static final String SHOP_BILL_TYPE_CONSUME_V_REBATE = "62";
    
    //==============1dcq_user_bill表中account_type账户类型值========
    /**
     * 平台奖励
     */
    public static final int USER_ACCOUNT_TYPE_REWARD = 1;
    /**
     * 冻结资金
     */
    public static final int USER_ACCOUNT_TYPE_FREEZE = 2;
    /**
     * 消费金
     */
    public static final int USER_ACCOUNT_TYPE_MONETARY = 3;
    
    /**
     * 消费币
     */
    public static final int  USER_ACCOUNT_TYPE_CONSUM = 5;
    
    /**
     * 代金券
     */
    public static final int  USER_ACCOUNT_TYPE_VOUCHER = 6;
    
    /**
     * 红包
     */
    public static final int USER_ACCOUNT_TYPE_RED_PACKET = 4;
    /**
     * 系统商铺ID
     */
    public static final Long SHOP_ID_SYSTEM_TYPE = 1L;

    
  //==============1dcq_user_bill表中user_bill_type账户类型值========
    /**
     * 消费
     */
    public static final int USER_BILL_TYPE_CONSUME = 1;
    /**
     * 提现
     */
    public static final int USER_BILL_TYPE_WITHDRAW = 2;
    /**
     * 提现退回
     */
    public static final int USER_BILL_TYPE_WITHDRAW_BACK = 3;
    /**
     * 消费返利
     */
    public static final int USER_BILL_TYPE_CONSUMER_REBATE = 4;
    /**
     * 推荐会员奖励 
     */
    public static final int USER_BILL_TYPE_USER_REWARD = 5;
    /**
     * 推荐店铺奖励
     */
    public static final int USER_BILL_TYPE_SHOP_REWARD = 6;
    /**
     * 服务店铺奖励
     */
    public static final int USER_BILL_TYPE_SERVICE_SHOP_REWARD = 7;
    /**
     * 市级代理奖励
     */
    public static final int USER_BILL_TYPE_CITY_AGENT_REWARD = 8;
    /**
     * 区县代理奖励
     */
    public static final int USER_BILL_TYPE_AREA_AGENT_REWARD = 9;
    /**
     * 乡镇代理奖励
     */
    public static final int USER_BILL_TYPE_TOWN_AGENT_REWARD = 10;

    /**
     * 冻结资金
     */
    public static final int USER_BILL_TYPE_FREEZE = 11;
    /**
     * 解冻资金
     */
    public static final int USER_BILL_TYPE_UNFREEZE = 12;
    /**
     * 退款
     */
    public static final int USER_BILL_TYPE_BACK = 13;
    
    /**
     * 运营商奖励
     */
    public static final int USER_BILL_TYPE_OPERATORS_REWARD = 14;
    /**
     * 充值卡充值充值
     */
    public static final int USER_BILL_TYPE_CASH_CARD = 15;
    
    /**
     * 连锁店推广员奖励
     */
    public static final int USER_BILL_TYPE_INTEGRATION_PROMOTION = 16;
    /**
     * 连锁店促成人奖励
     */
    public static final int USER_BILL_TYPE_INTEGRATION_FACILITATE = 17;
    /**
     * 连锁店店主奖励
     */
    public static final int USER_BILL_TYPE_INTEGRATION_PRINCIPAL = 18;
    
    /**
     * 推荐代理商奖励
     */
    public static final int USER_BILL_TYPE_RECOMMEND_AGENT_AWARD = 19;
    
    
    /**
     * 推荐经销商奖励
     */
    public static final int USER_BILL_TYPE_RECOMMED_DEALER_AWARD = 20;
    
    /**
     * 支付消费返利倍额扣减金
     */
    public static final int USER_BILL_TYPE_CONSUM_REBATE = 21;
    
    /**
     * 代理费返还
     */
    public static final int USER_BILL_TYPE_AGENT_REBATE = 22;
    /**
     * 分公司管理补贴
     */
    public static final int USER_BILL_TYPE_COMPANY_REBATE = 26;
    /**
     * 组长管理费收益
     */
    public static final int USER_BILL_TYPE_HEADMAN_REBATE = 27;
    
    /**
     * 支付宝充值
     */
    public static final int USER_BILL_TYPE_ALIPAY = 30;

    /**
     * 农行借记卡充值
     */
    public static final int USER_BILL_TYPE_ABC_DEBIT = 34;
    /**
     * 农行信用卡充值
     */
    public static final int USER_BILL_TYPE_ABC_CREDIT = 35;
    
    /**
     * 微信充值
     */
    public static final int USER_BILL_TYPE_WEIXIN = 33;
    /**
     * 收红包
     */
    public static final int USER_BILL_TYPE_R_RED_PACKET = 40;
    
    /**
     * 发红包
     */
    public static final int USER_BILL_TYPE_S_RED_PACKET = 41;
    
    /**
     * 农行借记卡充值
     */
    public static final int USER_BILL_TYPE_CCB_DEBIT = 31;
    /**
     * 建行信用卡充值
     */
    public static final int USER_BILL_TYPE_CCB_CREDIT = 32;

	/**
	 * 兑换平台奖励
	 */
    public static final int USER_BILL_TYPE_GET_PLATFORM_PRESENT = 50;

    //==============1dcq_platform_bill表中platform_bill_type账户类型值========
    /**
     * 消费支付
     */
    public static final int PLATFORM_BILL_TYPE_PAY = 1;
    /**
     * 商铺线上营业收入
     */
    public static final int PLATFORM_BILL_TYPE_ONLINE = 2;
    /**
     * 销售提成
     */
    public static final int PLATFORM_BILL_TYPE_SALE = 3;
    /**
     * 支付会员奖励 
     */
    public static final int PLATFORM_BILL_TYPE_MEMBERAWARD = 4;
    /**
     * 支付推荐会员奖励
     */
    public static final int PLATFORM_BILL_TYPE_RECOMAWARDS = 5;
    /**
     * 支付推荐商铺奖
     */
    public static final int PLATFORM_BILL_TYPE_SHOPRWARDS = 6;
    /**
     * 支付服务店铺费
     */
    public static final int PLATFORM_BILL_TYPE_SERVE = 7;
    /**
     * 支付一级代理费
     */
    public static final int PLATFORM_BILL_TYPE_1AGENT = 8;
    /**
     * 支付二级代理费
     */
    public static final int PLATFORM_BILL_TYPE_2AGENT = 9;
    /**
     * 支付三级代理费
     */
    public static final int PLATFORM_BILL_TYPE_3AGENT = 10;
    /**
     * 支付连锁推广员费
     */
    public static final int PLATFORM_BILL_TYPE_INTEGRATION_PROMOTION = 16;
    /**
     * 支付连锁店促成人费
     */
    public static final int PLATFORM_BILL_TYPE_INTEGRATION_FACILITATE = 17;
    /**
     * 支付连锁店主费
     */
    public static final int PLATFORM_BILL_TYPE_INTEGRATION_PRINCIPAL = 18;
    
    /**
     * 支付推荐代理奖励
     */
    public static final int PLATFORM_BILL_TYPE_REFER_AGENT = 19;
    /**
     * 支付推荐经销奖励
     */
    public static final int PLATFORM_BILL_TYPE_REFER_AGENCY = 20;
    /**
     * 购买红包
     */
    public static final int PLATFORM_BILL_TYPE_RED = 11;
    
    /**
     * 退款
     */
    public static final int PLATFORM_BILL_TYPE_BACK = 13;
    
    /**
     * 支付运营商奖励
     */
    public static final int PLATFORM_BILL_TYPE_OPERATORS_REWARD = 14;
    
    /**
     * 插件收入
     */
    public static final int PLATFORM_BILL_TYPE_PLUGIN = 15;
    
    /**
     * 支付店铺购买产品返利
     */
    public static final int PLATFORM_BILL_TYPE_BUY_V_BACK = 22;
    /**
     * 会员制单返还生意金
     */
    public static final int PLATFORM_BILL_TYPE_BACK_MARKET = 23;
    
    /**
     * 支付代理费返还
     */
    public static final int PLATFORM_BILL_TYPE_BACK_AGENT = 24;
    
    /**
     * 收取返利税金
     */
    public static final int PLATFORM_BILL_TYPE_TAX_DEDUCTION = 25;
    
    /**
     * 划扣套餐费用
     */
    public static final int PLATFORM_BILL_TYPE_PACKAGE_INCOME = 30;
    
    /**
     * 停用扣除保证金
     */
    public static final int PLATFORM_BILL_TYPE_DEPOSIT_INCOME = 31;
    /**
     * 退还赠送保证金
     */
    public static final int PLATFORM_BILL_TYPE_DEPOSIT_BACK = 32;
    
    /**
     * 收红包
     */
    public static final int PLATFORM_BILL_TYPE_R_RED_PACKET = 40;
    
    /**
     * 发红包
     */
    public static final int PLATFORM_BILL_TYPE_S_RED_PACKET = 41;
    
    
  //==============1dcq_shop_setting表中setting_type账户类型值======== 
    /**
     * 积分设置=1
     */
    public static final int SHOP_SETTING_TYPE_POINTS = 1;
    /**
     * 打印设置=2
     */
    public static final int SHOP_SETTING_TYPE_PRINT = 2;
    /**
     * 自动充值设置=3
     */
    public static final int SHOP_SETTING_TYPE_CHARGE = 3;
    /**
     * 是否自动转充开关 0为关闭 1为开启
     */
    public static final String SHOP_SETTING_BAIL_FLAG = "bail_flag";
    /**
     * 保证金预警额度，低于此保证金时预警，如果设置自动转充，此时启动自动转充。默认200
     */
    public static final String SHOP_SETTING_BAIL_ALTER_AMOUNT = "bail_alter_amount";
    /**
     * 保证金额度，自动转充保证金到此金额，默认500
     */
    public static final String SHOP_SETTING_BAIL_AMOUNT = "bail_amount";
    /**
     * 是否自动转充开关 0为关闭
     */
    public static final String SHOP_SETTING_BAIL_FLAG_FALSE = "0";
    /**
     * 是否自动转充开关 1为开启
     */
    public static final String SHOP_SETTING_BAIL_FLAG_TRUE = "1";
    /**
     * 自动转充开始，默认50
     */
    public static final double SHOP_SETTING_BAIL_ALTER_AMOUNT_VALUE = 50;
    /**
     * 充值到多少，默认300
     */
    public static final double SHOP_SETTING_BAIL_AMOUNT_VALUE = 300;
    /**
     * 线上店铺自动转充开始，默认20
     */
    public static final double ONLINE_SETTING_BAIL_ALTER_AMOUNT_VALUE = 20;
    /**
     * 线上店铺充值到多少，默认100
     */
    public static final double ONLINE_SETTING_BAIL_AMOUNT_VALUE = 100;
    /**
     * 商铺类型
     */
    public static final String SHOP_TYPE= "1";
    /**
     * 用户类型
     */
    public static final String USER_TYPE= "2";
    /**
     * 订单类型
     */
    public static final String ORDER_TYPE= "3";
  
    // ====================组合支付类型start====================
    /**
 	 * 组合支付-会员卡支付
 	 */
 	public static final int MULTI_PAYTYPE_MEMBERCARD = 5;
    /**
 	 * 组合支付-现金支付
 	 */
 	public static final int MULTI_PAYTYPE_CASH = 6;
 	/**
 	 * 组合支付-POS支付
 	 */
 	public static final int MULTI_PAYTYPE_POS = 7;
 	/**
 	 * 组合支付-传奇宝支付
 	 */
 	public static final int MULTI_PAYTYPE_CQB = 1;
 	/**
 	 * 消费卡
 	 */
 	public static final int MULTI_PAYTYPE_XFK = 2;
 	/**
 	 * 红包
 	 */
 	public static final int MULTI_PAYTYPE_HB = 3;
 	
	/**
 	 * 组合支付-会员卡支付
 	 */
 	public static final int MULTI_PAYTYPE_SHOP_MEMBER_CARD = 5;
 	
 	/**
 	 * 组合支付-短信支付
 	 */
 	public static final int MULTI_PAYTYPE_SMS = 8;
 	
 	  /**
     * 组合支付-免单
     */
    public static final int MULTI_PAYTYPE_FREE = 22;
    
    /**
     * 组合支付-自定义支付1
     */
    public static final int MULTI_PAYTYPE_CUSTOM1 = 23;
    
    /**
     * 组合支付-自定义支付2
     */
    public static final int MULTI_PAYTYPE_CUSTOM2 = 24;
    
    /**
     * 组合支付-自定义支付3
     */
    public static final int MULTI_PAYTYPE_CUSTOM3 = 25;
 	
 	
 	/**
 	 * 不进行支付，只保存订单信息
 	 */
 	public static final int MULTI_PAYTYPE_NO_PAY = -1;
    // ====================组合支付类型end====================
 	// ====================平台账单金额来源start====================
 	/**
 	 * 支付宝
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_ZFB = 0;
 	/**
 	 * 传奇宝
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_CQB = 1;
 	/**
 	 * 消费卡
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_XFK = 2;
 	/**
 	 * 微信支付
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_WX = 3;
 	/**
 	 * 建行借记卡
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_JHK = 4;
 	/**
 	 * 建行信用卡
 	 */
 	public static final int PLT_BILL_MNY_SOURCE_JHXYK = 5;
 	
 	/**
     * 红包
     */
    public static final int PLT_BILL_MNY_SOURCE_HB = 7;
    
    /**
     * 保证金
     */
    public static final int PLT_BILL_MNY_SOURCE_DEPOSIT = 10;

 	// ====================平台账单金额来源end====================
 	
 	public static final int ORDER_IS_MEMBER=1;//非会员订单
 	
 	public static final int ORDER_NOT_MEMBER=0;//非会员订单
 	/**
 	 * 结账=1
 	 */
 	public static final int AUTO_SETTLE_FLAG_TRUE = 1;
 	public static final int AUTO_SETTLE_FLAG_FLASE = 0;
 	/**
 	 * 不结账=0
 	 */
 	public static final int AUTO_SETTLE_FLAG_FALSE = 0;
 	/**
 	 * 反结账
 	 */
 	public static final int REVERSE_SETTLE_FLAG = 2;

 	
 	//============================payChannel=======
 	public static final int PAY_CHANNEL_ALI = 0;
 	public static final int PAY_CHANNEL_WEIXIN = 2;
 	public static final int PAY_CHANNEL_OTHER = 5;

 	/**
 	 * 账单可见
 	 */
 	public static final int USER_BILL_IS_SHOW = 1;
 	  /**
     * 账单不可见
     */
    public static final int USER_BILL_NOT_IS_SHOW = 0;
    /**
     * 心跳间隔时间
     */
    public static final String HEART_BEAT_SECS = "heart_beat_secs";
   /**
    * 充值卡状态-无效
    */
    public static final Integer CARD_STATUS_LOSE_EFFICACY = 0;

    /**
     * 充值卡状态-有效
     */
    public static final Integer CARD_STATUS_LOSE_EFFECTIVE = 1;

    /**
     * 充值卡状态-删除
     */
    public static final Integer CARD_STATUS_LOSE_DELETE = 2;
    /**
     * 充值卡批次状态-无效
     */
     public static final Integer CARD_BATCH_STATUS_LOSE_EFFICACY = 0;

     /**
      * 充值卡批次状态-有效
      */
     public static final Integer CARD_BATCH_STATUS_LOSE_EFFECTIVE = 1;

     /**
      * 充值卡批次状态-删除
      */
     public static final Integer CARD_BATCH_STATUS_LOSE_DELETE = 2;
    
  //============================充值卡批次表=======
    public static final int CASH_CARD_BATCH_BUILD_STATUS_NOT_GENERATE = 0;
    public static final int CASH_CARD_BATCH_BUILD_STATUS_GENERATEING = 1;
    public static final int CASH_CARD_BATCH_BUILD_STATUS_GENERATEED = 2;
    
    //============================批次操作记录表=======
    
    //===================1dcq_shop_account表中arrears_flag字段============
    /**
     * 0=未欠费
     */
    public static final int ARREARS_FLAG_FALSE = 0;
    /**
     * 1=欠费
     */
    public static final int ARREARS_FLAG_TURE = 1;
    
    /**
     * 收银机同步订单交易记录接口
     */
    public static final String SYNCORDERLIST_URL = "/appServer/interface/syncOrderList";
    /**
     * 收银机一点传奇交易记录接口
     */
    public static final String ONLINEPAY_URL = "/appServer/interface/onlinePay";
    
    public static final String TOKEN_PREFIX = "/token";
    
    public static final String GETBILLSTAT_URL = "/appServer/interface/shop/getBillStat";
    
    public static final String DAYORDERSTAT_URL = "/appServer/interface/shop/getDayOrderStat";
    
    public static final String UPDATEGOODS_URL = "/appServer/interface/session/goods/updateGoods";
    
    public static final String QUERYSTANDARDGOODS_URL = "/appServer/interface/common/queryStandardGoods";
    
    public static final String PAYVERICODE_URL = "/appServer/interface/getPayVeriCode";
    
    public static final String FEEDBACK_URL = "/appServer/interface/feedBack";
    
    public static final String FEEDBACKLIST_URL = "/appServer/interface/getShopFeedbackList";

    public static final String MULTIPLEPAY_URL = "/appServer/interface/multiplePay";
    
    public static final String SHOPCONFIGURESETTING_URL = "/appServer/interface/shop/shopConfigureSetting";
    
    public static final String QUERY_SHOPCONFIGURESETTING_URL = "/appServer/interface/shop/queryShopConfigureSetting";
    
    public static final String MODIFYPWD_URL = "/appServer/interface/session/user/modifyPwd";
    
    public static final String ADMINWITHDRAW_URL = "/appServer/interface/serverCommunicate/adminWithdraw";
    
    public static final String WITHDRAW_URL = "/appServer/interface/serverCommunicate/withdraw";

    public static final String GETSHOPGOODS_URL = "/appServer/interface/shop/getShopGoods";

    
    //会员卡相关常量信息
    public static final int CARD_BILL_TYPE_CHARGE=0;//充值
    
    public static final int CARD_BILL_TYPE_CONSUME=1;//消费
    
    public static final int CARD_BILL_TYPE_BUY_TIMECARD=2;//账单类型购买次卡
    
    public static final int CARD_CHARGE_TYPE_MONEY=6;//现金
    public static final int CARD_CHARGE_TYPE_SHOPMEMBERCARD=7;//现金
    
    public static final String PAY_MODE_SHOP_TIMECARD="15";
    
    public static final int CARD_TIME_CARD_TYPE=2;//购买次卡
    

    /**
     * 发送消息渠道类型-jpush
     */
    public static final String NOTIFY_CHANNEL = "jpush";
    
    /**
     * 发送消息渠道类型-短信
     */
    public static final String NOTIFY_CHANNEL_SMS = "sms";
    public static final Integer NOTIFY_CHANNEL_SMS_INT = 1;
    /**
     * 发送消息渠道类型-jpush
     */
    public static final String NOTIFY_CHANNEL_JPUSH = "jpush";
    public static final Integer NOTIFY_CHANNEL_JPUSH_INT = 2;
    public static Map<String,Integer> NOTIFY_CHANNEL_MAP = new HashMap<String, Integer>();
    static{
    	NOTIFY_CHANNEL_MAP.put(NOTIFY_CHANNEL_SMS, NOTIFY_CHANNEL_SMS_INT);
    	NOTIFY_CHANNEL_MAP.put(NOTIFY_CHANNEL_JPUSH, NOTIFY_CHANNEL_JPUSH_INT);
    }
    
    /**
     * 向商铺发送消息
     */
    public static final int NOTIRY_TYPE_SHOP = 2;
    
    /**
     * 向会员发送消息
     */
    public static final int NOTIRY_TYPE_MEMBER = 1;
    /**
     * 配置项代码--满
     */
    public static final String CONFIG_CODE_OVER = "over";
    /**
     * 配置项代码--送
     */
    public static final String CONFIG_CODE_GIVE= "give";
    
    
    /**
     * 商圈活动类型推广短信模板configKey
     */
    public static final String TRADING_AREA_MODEL_USER = "tradingAreaModel_User_";
    /**
     * 商圈活动类型推广短信模板configKey
     */
    public static final String TRADING_AREA_MODEL_SHOP = "tradingAreaModel_Shop_";
    /**
     * 商圈活动用户推广短信，在最后面追加商圈活动ID，如：tradingAreaActivities_User_1111
     */
    public static final String TRADING_AREA_ACTIVITIES_USER = "tradingAreaActivities_User_";
    /**
     * 商圈活动商家推广短信，在最后面追加商圈活动ID，如：tradingAreaActivities_Shop_321
     */
    public static final String TRADING_AREA_ACTIVITIES_SHOP = "tradingAreaActivities_Shop_";
    
    /**
     * 营销通道
     */
    public static final String SMS_TYPE_SEO = "1";
    
    /**
     * 查询店铺时查询是否是新开店铺
     */
    public static final String IS_NEW_SHOP="1";
    
    public  static final String NEW_SHOP_JUDGE_PARAM_NAME="newShopJudgeParam";
	public static final String GETACTIVITYLIST_URL = "/appServer/interface/service/busAreaActivity/getActivityList";
	
	/**
	 * 商铺审核状态，2--审核被拒绝
	 */
	public static final String AUDIT_STATUS_NOPASS = "2";
	
    /**
     * 出库单号前缀
     */
    public static final String STORAGE_NOPREFIX_CK = "CK";
    /**
     * 入库单号前缀
     */
    public static final String STORAGE_NOPREFIX_RK = "RK";
    /**
     * 盘点单号前缀
     */
    public static final String STORAGE_NOPREFIX_PD = "PD";
    
    /**
     * 线上签约
     */
    public static final int IS_SING_ONLINE = 2;
    
    /***************************商铺等级***********************************/
    public static final int IS_DELETE_TRUE= 1;//删除
    public static final int IS_DELETE_FALSE = 0;//不删除
    
    public static final int OPERATE_TYPE_ADD = 1;//操作类型：1-新增；2-更新；3-删除
	public static final int OPERATE_TYPE_DELETE = 3;//操作类型：1-新增；2-更新；3-删除
	public static final int OPERATE_TYPE_UPDATE = 2;//操作类型：1-新增；2-更新；3-删除
	
	public static final int LEVEL_TYPE_SHOP = 1;//等级类型:1=店铺， 2=会员
	public static final int LEVEL_TYPE_MEMBER = 2;//等级类型:1=店铺， 2=会员
	
	public static final int PREROGATIVE_TYPE_REFER=1;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_REPORT=2;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_SHOW=3;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_SERVICE=4;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_DESIGN=5;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_RATE=6;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_BILLDATE=7;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_CONSUM=8;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
	public static final int PREROGATIVE_TYPE_SEND=9;//特权类型推荐类=1，报道类=2，展示类=3，服务类=4，策划设计类=5，费率类=6，账期类=7，消费类=8，赠送类=9
    
	public static final int RULE_TYPE_SETTILE=1;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_BINDING=2;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_RELEASEGOODS=3;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_REFER=4;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_ORDER=5;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_REGIST=6;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_COMMENT=7;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	public static final int RULE_TYPE_CONSUM=8;//规则类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	
	public static final int TASK_TYPE_COMMON=1;//任务类型，常规性任务=1，非常规性任务=2
	public static final int TASK_TYPE_UNCOMMON=2;//任务类型，常规性任务=1，非常规性任务=2
	
	public static final int SUB_RULE_TYPE_SETTLE_SHOP=1;//商家入驻类： 1=店铺入驻
	public static final int SUB_RULE_TYPE_BINDING_SERVICE=1;//绑定类： 1=服务号， 2=订阅号
	public static final int SUB_RULE_TYPE_BINDING_SUBSCRIPTION=2;//绑定类： 1=服务号， 2=订阅号
	public static final int SUB_RULE_TYPE_RELEASE_GOODS=1;//发布商品类： 1=首次发布商品
	public static final int SUB_RULE_TYPE_REFER_SHOP=1;//推荐类： 1=推荐店铺， 2=推荐会员，3=首次推荐会员
	public static final int SUB_RULE_TYPE_REFER_MEMBER=2;//推荐类： 1=推荐店铺， 2=推荐会员，3=首次推荐会员
	public static final int SUB_RULE_TYPE_REFER_MEMBER_FIRST=3;//推荐类： 1=推荐店铺， 2=推荐会员，3=首次推荐会员
	public static final int SUB_RULE_TYPE_ORDER_NO=1;//制单类： 1=制单数量， 2=制单金额，3=首次制单收银
	public static final int SUB_RULE_TYPE_ORDER_MONEY=2;//制单类： 1=制单数量， 2=制单金额，3=首次制单收银
	public static final int SUB_RULE_TYPE_ORDER_CASH=3;//制单类： 1=制单数量， 2=制单金额，3=首次制单收银
	
	public static final int SUB_RULE_TYPE_REGIST_MEMBER=1;//注册类： 1=注册会员
	public static final int SUB_RULE_TYPE_COMMON_FIVE=1;//评价类： 1=五星， 2=四星， 3=三星
	public static final int SUB_RULE_TYPE_COMMON_FOURE=2;//评价类： 1=五星， 2=四星， 3=三星
	public static final int SUB_RULE_TYPE_COMMON_THREE=3;//评价类： 1=五星， 2=四星， 3=三星
	
	public static final int CLIENT_SYSTEM_IS_SYJ=1;//发起交易的客户系统。1：收银机;2：管家 APP;3： o2o 后台
	public static final int CLIENT_SYSTEM_IS_GJ=2;//发起交易的客户系统。1：收银机;2：管家 APP;3： o2o 后台
	public static final int CLIENT_SYSTEM_IS_O2O=3;//发起交易的客户系统。1：收银机;2：管家 APP;3： o2o 后台
	
	public static final String REBATE_CONTENT = "rebate_content";
	
	public static final String SEND_SHOP_COUPON_CONTENT = "send_shop_coupon";

	/**
	 * 已验证
	 */
	public static final Integer USER_IS_CHECK = 1;
	/**
	 * 无验证
	 */
	public static final Integer USER_NOT_IS_CHECK = 0;
	/**
	 * 会员状态：1=正常
	 */
	public static final int MEMBER_STATUS_NORMAL = 1;
	/**
	 * 会员状态：2=停用
	 */
	public static final int MEMBER_STATUS_STOP = 2;
	/**
     * 会员状态：3=删除
     */
	public static final int MEMBER_STATUS_DELETE = 3;
	/**
	 * 会员卡状态：1=正常
	 */
	public static final int MEMBERCARD_STATUS_NORMAL = 1;
	/**
	 * 会员卡状态：2=停用
	 */
    public static final int MEMBERCARD_STATUS_STOP = 2;
    /**
     * 会员卡状态：3=删除
     */
    public static final int MEMBERCARD_STATUS_DELETE = 3;
	public static final int SEND_SMS_TRUE=1;//是否发送短信： 0-否； 1-是
	public static final int SEND_SMS_FALSE=0;//是否发送短信： 0-否； 1-是
	
	public static final String CARD_TYPE_IS_MASTER_CARD="1";//卡类型:1=充值卡,2=次卡
	public static final String CARD_TYPE_IS_SECONDARY_CARD="2";//卡类型:1=充值卡,2=次卡
	/**
	 * 审核通过
	 */
	public static final Integer WITHDRAW_STATUS_AUDITING_PASS = 2;
	/**
	 * 提现成功
	 */
	public static final Integer WITHDRAW_STATUS_SUCCESS = 4;
	
	/**
	 * 待审核
	 */
	public static final Integer WITHDRAW_STATUS_TODO = 0;
	
	/*------------------------店铺优惠券---------------------------*/
	/**
	 * //优惠券适用类型：全部商品-1
	 */
	public static final Integer COUPON_TYPE_IS_ALL_GOODS = 1;
	/**
	 * //优惠券适用类型：指定商品类别-2
	 */
	public static final Integer COUPON_TYPE_IS_GOODS_TYPE = 2;
	
	/**
	 * 是否允许与其他优惠券共用：允许-1，不允许-0
	 */
	public static final Integer USED_TOGETHER_IS_TRUE = 1;
	
	/**
	 * 是否允许与其他优惠券共用：允许-1，不允许-0
	 */
	public static final Integer USED_TOGETHER_IS_FALSE = 0;
	
	/**
	 * 是否允许分享领取：允许-1，不允许-0
	 */
	public static final Integer SHARED_IS_FALSE = 0;
	
	/**
	 * 是否允许分享领取：允许-1，不允许-0
	 */
	public static final Integer SHARED_IS_TRUE = 1;
	
	/**
	 * 优惠券状态：停用-0
	 */
	public static final Integer COUPON_IS_STOP = 0;
	
	/**
	 * 优惠券状态：启用-1
	 */
	public static final Integer COUPON_IS_ENABLE = 1;
	
	/**
	 * 优惠券状态：过期-2
	 */
	public static final Integer COUPON_IS_OUT_OF_DATE = 2;
	
	public static final int OPERATE_TYPE_STOP = 2;//操作类型：2-停用；
	public static final int OPERATE_TYPE_ENABLE = 1;//操作类型：1-启用；
	/**
	 * 优惠券适用类型：商品分类-1
	 */
	public static final int COUPON_APPLY_TYPE = 1;
	/**
	 * 非平台会员-0；
	 */
	public static final int IS_NOT_USER = 0;
	
	/**
	 * 平台 会员-1
	 */
	public static final int IS_USER = 1;
	/**
	 * 本店推荐的平台会员-2
	 */
	public static final int IS_SHOP_REF_USER = 2;
	
	/**
	 * 用户优惠券状态：未使用-0
	 */
	public static final Integer USER_SHOP_COUPON_UNUSED = 0;
	
	/**
	 * 用户优惠券状态：已使用-1
	 */
	public static final Integer USER_SHOP_COUPON_USED = 1;
	
	/**
	 * 用户优惠券状态：过期-2
	 */
	public static final Integer USER_SHOP_COUPON_EXPIRE  = 2;
	
	/**
	 * 自定义商品的ID
	 */
	public static final int CUSTOM_GOODS_FLAG = -1;
	
	/**
	 * 消费
	 */
	public static final int INCOME_TYPE_CONSUME = 1;
	/**
	 * 充值
	 */
	public static final int INCOME_TYPE_CHARGE = 2;
	
	public static final double PRICE_LIMIT = 10000000;
	
	/**
	 * 短信账户类型：0:验证账户类型
	 */
	public static final int SMS_TYPE_ACCOUNT = 0;
	
	/**
	 * 短信账户类型：1:营销短信类型
	 */
	public static final int SMS_TYPE_MARKETING_SMS = 1;
	
	/**
     * 是否已发送生日短信：1-已发送；
     */
	public static final int IS_SEND_BIRTHDAY_SMS_IS_TRUE = 1;
	
	/**
     * 是否已发送生日短信：0-未发送
     */
	public static final int IS_SEND_BIRTHDAY_SMS_IS_FALSE = 0;
	
	/**
     * 短信模版类型：1：生日提醒短信
     */
	public static final int SMS_MODEL_TYPE_IS_BIRTHDAY = 1;
	
}


