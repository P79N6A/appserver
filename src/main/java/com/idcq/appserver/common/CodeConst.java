package com.idcq.appserver.common;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 业务异常代号
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 下午7:48:12
 */
public class CodeConst {
	
	public static final int CODE_SUCCEED = 0;
	public static final int CODE_FAILURE = 5;
	public static final int CODE_PARTIAL_SUCCESS = 2;
	
	public static final int CODE_REQUESTQRCODE_FAIL=10000;//获取二维码失败
	
	public static final int CODE_SYSTEM_BUSY = -1;
	
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是1XXXXMysql错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_SQL_ERROR = 1149;
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是2XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_SYSTEM_ERROR = 20000;	//系统异常
	
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是3XXXX错误码配置↓↓↓↓dts↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_REQUEST_OFTEN = 30001;//请求过于频繁
	public static final int CODE_COOKIES_INVALIDATE = 30003;//无效cookie
	public static final int CODE_COOKIES_NULL = 30004; //未登录没有cookie
	public static final int CODE_COOKIES_TIMEOUT = 30005; //已失效需重新登录
	public static final int CODE_COMMENT_STATUS_ERROR = 33101;//订单状态异常，不可评论

	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是5XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_JSON_ERROR = 50000; //json无法解析
	public static final int CODE_PARAMETER_NOT_EXIST = 50001;//字段不存在
	public static final int CODE_PARAMETER_NOT_NULL = 50002;//字段不能为空
	public static final int CODE_PARAMETER_NOT_VALID = 50003;//数据格式错误
	public static final int CODE_WEIXIN_NOTIFY_EXCEPTION = 59804;
	public static final int CODE_MOBILE_REGISTERED = 50004;//手机号码已经被注册过
	public static final int CODE_PWD_ERROR = 50005;//密码错误
	public static final int CODE_PAY_PWD_ERROR = 50006;//支付密码错误
	public static final int CODE_WITHDRAW_PWD_ERROR = 50007;//提现密码错误
	public static final int CODE_RESUBMIT_ERROR = 50009; //重复提交
	public static final int CODE_FINAL_STATUS_ERROR = 50010; //数据不可再改
	public static final int CODE_PWD_NOT_SAME = 50011; //密码不一致
	public static final int CODE_PARAMETER_REPEAT = 50012; //字段值重复
	public static final int CODE_PARAMETER_TOOLENGTH = 50013; //字段值超过规定长度
	/**
	 * 敏感词错误码
	 */
	public static final int CODE_SENSITIVE_WORDS_ERROR = 50014;
	public static final int CODE_PRAISE_REPEAT =  52701; //不允许重复点赞
	public static final int CODE_USER_CANCEL_ATTENTION_FAIL = 56801;//取消关注失败
	public static final int CODE_ACCOUNT_NULL = 50971; //会员卡号为空
	public static final int CODE_ACCOUNT_NOT_EXIST = 50972;	//会员卡不存在
	public static final int CODE_PAY_STATUS_SUCCESS = 57201;//订单支付状态为已支付，不可支付
	public static final int CODE_PAY_STATUS_CANCEL = 57202;//订单支付状态为已取消，不支持取消订单
	public static final int CODE_ORDER_STATUS_ERROR = 57203;//订单状态异常，不可支付
	public static final int CODE_ORDER_STATUS_NOT_SUPPORT_CANCEL = 57204;//订单状态不支持取消
	public static final int CODE_USER_REGISTERED = 59802; //用户已注册
	public static final int CODE_WEIXIN_USER_BINDED = 59803; //微信号与会员已经绑定
	public static final int CODE_TECH_WORK_NUMBER_IS_EXIT = 59804; //服务人员工号已存在
	public static final int CODE_VALIDATE_CARDNO_OR_BANKCARDNO_ERROR = 59805;//商铺身份证号或银行卡号验证
	public static final int CODE_PERMISSION_ERROR = 59806;//商铺身份证号或银行卡号验证
	public static final int CODE_PARAMETER_ILLEGAL = 59999;//参数非法
	

	/**
	 * 卡号或密码错误
	 */
	public static final int CODE_CARD_OR_PASSWORD_ERROR = 72415;
	/**
	 * 卡号或密码错误
	 */
	public static final String MSG_CARD_OR_PASSWORD_ERROR = "卡号或密码错误";
    /**
     * 充值卡未生成错误
     */
    public static final int CODE_CARD_IS_USE = 72416;
    /**
     * 充值卡已使用
     */
    public static final String MSG_CODE_CARD_IS_USE = "充值卡已使用"; 
    /**
     * 充值卡未生成错误码
     */
    public static final int CODE_CARD_NOT_IS_EXIST = 72414;
    /**
     * 充值卡未生成
     */
    public static final String MSG_CARD_NOT_IS_EXIST = "充值卡未生成";  
    
    /**
     *  充值卡已使用，不允许导出
     */
    public static final int CODE_CODE_CARD_BATCH_IS_USE = 72416; 
    /**
     * 充值卡已使用，不允许导出
     */
    public static final String MSG_CODE_CARD_BATCH_IS_USE = "充值卡已使用，不允许导出";
    
    /**
     *  充值卡批次状态错误
     */
    public static final int CODE_CODE_CARD_BATCH_STATUS_ERROR = 72417; 
    /**
     * 充值卡批次状态错误
     */
    public static final String MSG_CODE_CARD_BATCH_STATUS_ERROR = "充值卡批次状态异常";
    /**
     *  充值卡批次状态错误
     */
    public static final int CODE_CODE_CARD_BATCH_DATE_ERROR = 72418; 
    /**
     * 充值卡批次状态错误
     */
    public static final String MSG_CODE_CARD_BATCH_DATE_ERROR = "充值卡使用时间不在规定范围内";
    
			
	public static final int CODE_ORDER_STATUS_SEND = 57206;//订单状态已派送，不可退单
	public static final int CODE_ORDER_STATUS_FINISH = 57207;//订单状态已完成，不可退单
	public static final int CODE_ORDER_STATUS_CANCEL = 57208;//订单状态已退单，不可退单
	public static final int CODE_ORDER_NOT_SHOP = 57209;//数据错误，非指定店铺订单
	public static final int CODE_ORDER_STATUS_MISMATCH = 57210;//数据错误，非指定店铺订单
	public static final int CODE_ORDER_NOT_PAY_BY_SMS = 57211; // 非会员订单，不支持短信支付
	public static final int CODE_MEMBER_CARD_NOT_ENOUGH = 57212; // 会员卡余额不足
	public static final int CODE_MEMBER_CARD_NOT_EXIST = 57213; // 会员卡不存在
	
	public static final int CODE_DISABLE_HOME_SERVICE = 55113;//商铺未启用预约上门
	public static final int CODE_DISABLE_TAKEOUT = 55103;//商铺未启用外卖
	public static final int CODE_DISABLE_BOOK = 55108;//商铺未启用预定
	public static final int CODE_LEAST_BOOK_PRICE_ERROR = 55109;//订单最低起订金额不满足
	public static final int CODE_NOTIN_DELIVERY_TIME = 55104;//当前不在商铺接单时间范围内
	public static final int CODE_IN_STOP_DATE = 55105;//商铺当前处于暂停预定时间范围内
	public static final int CODE_DISTRIB_DATE_IN_STOP_DATE = 55106;//配送时间处于暂停预定时间范围内
	public static final int CODE_DISTRIB_DATE_NOTIN_WEEK = 55107;//配送时间不在商铺可预定周期内
	public static final int CODE_BOOK_DATE_NOTIN_WEEK = 55111;//预定时间不在商铺可预定周期内
	public static final int CODE_DISTRIB_TIME_NOTIN_DELIVERY = 55109;//配送时间不在可配送时间范围内
	public static final int CODE_BOOK_TIME_NOTIN_DELIVERY = 55110;//预定时间点不在可预定时间范围内
	public static final int CODE_TECH_TYPE_IS_USED = 55112;// 技师级别已被使用，不能删除
	
	public static final int CODE_ORDER_NOT_BELONG_TO_YOU = 58101;//订单不属于本人
	public static final int CODE_PAY_PWD_YES_SET = 59801;//支付已设定
	public static final int CODE_USER_ACCOUNT_FROZEN_58301 = 58301;//账号被冻结
	public static final int CODE_REDPACKET_USE_58501 = 58501;//该红包不属于您
	public static final int CODE_REDPACKET_USE_58502 = 58502;//该红包已被使用
	public static final int CODE_REDPACKET_USE_58503 = 58503;//该红包还不到使用时间
	public static final int CODE_REDPACKET_USE_58504 = 58504;//该红包已过了使用期限
	public static final int CODE_REDPACKET_USE_58505 = 58505;//该订单已经支付完成了，不需要再支付
	public static final int CODE_REDPACKET_USE_58506 = 58506;//发行该红包的商铺状态异常，暂时不能使用
	public static final int CODE_REDPACKET_USE_58507 = 58507;//该订单已经不需要再进行支付
	public static final int CODE_REDPACKET_USE_58601 = 58601;//手慢了，红包已被别人抢走了
	public static final int CODE_USER_CASHCOUPON_INVALID = 58901;//代金券已经被领完或者不在有效期内
	public static final int CODE_USER_CASHCOUPON_GRAB_ERR = 58902;//领取代金券失败
	public static final int CODE_USER_CASHCOUPON_GRAB_LIMIT = 58903;//已到每天领取上限数
	public static final int CODE_CASHCOUPON_NOT = 59005;//已支付，不必再使用代金券
	public static final int CODE_BE_USED_CC = 59014;//使用过
	public static final int CODE_CASHCOUPON_NO_OTHER_SHOP = 59016;//代金券不能异   店消费
	public static final int CODE_USER_CASHCOUPON_CONDITION_PRICE = 59011;//订单金额未达到可使用代金券的额度
	public static final int CODE_USER_CASHCOUPON_USE_NUMBER = 59012;//已达到订单一次最多可使用张数
	public static final int CODE_USER_CASHCOUPON_USE_TOGETHER_FLAG = 59013;//不允许和同类券一起使用
	public static final int CODE_USER_STATUS_FREEZE_FAIL = 55701; //登录失败，用户状态已冻结
	public static final int CODE_USER_STATUS_LOGOUT_FAIL = 55702;//登录失败，用户状态已注销
	public static final int CODE_USER_STATUS_WAIT_ACTIVE = 55703;//登录失败，用户状态已注销
	public static final int CODE_USER_STATUS_FREEZE = 55704;//登录失败，用户状态已注销
	public static final int CODE_USER_STATUS_LOGOUT = 55705;//登录失败，用户状态已注销
	public static final int CODE_USER_STATUS_ERROR = 55706; //会员状态异常
	public static final int CODE_EMPLOYESS_IS_CHECK_ERROR = 55707; //会员状态异常
	public static final int CODE_PAYPWD_NO_SET = 59301; // 未设置支付密码
	public static final int CODE_FEEDBACK_LENGTH_BEYOND = 56601;//字数应在500以内
	public static final int CODE_USERADDRESS_ADD_ERR =55501;// 新增用户地址失败;
	public static final int CODE_VERICODE_55901=55901;//手机验证码发送失败
	public static final int CODE_VERICODE_53101 = 53101;//验证码错误
	public static final int CODE_ACCOUNT_NOT_BALANCE = 58102;//账户余额不足
	public static final int CODE_ACCOUNT_NOT_BALANCE_PARTPAY = 58103;//账户余额，部分支付
	public static final int CODE_COUPON_STATUS_NOT_AVAILABLE =510601;
	public static final int CODE_COUPON_NOT_AVAILABLE_GOODS =510602;
	public static final int CODE_RESOURCE_STATUS_NOT_AVALIABLE =58001;
	public static final int CODE_COMMENT_TIME_ERROR = 53301;
	public static final int CODE_NOT_IN_OPEN_DATERANGE = 57801;	//不在营业时间范围内
	public static final int CODE_NOT_IN_CONSUME_DATERANGE = 57802;	//当天已经不能再领取
	public static final int CODE_NOT_IN_GRAB_DATERANGE = 57803;	//当天已经不能再领取
	public static final int CODE_RES_DATE_CANNOT_EARLY_CUR = 57804;	//无效
	public static final int CODE_INTEVAL_NO_RULE = 57805;	//无效
	public static final int CODE_NOT_FORWARD_DATE = 57806;	//无效
	public static final int CODE_NOT_FORWARD_HOUR = 57807;	//无效
	public static final int CODE_PAUSE_DATE = 57808;	//无效
	public static final int CODE_NO_ENOUGH_NUM = 57809;	//没有足够的数量
	
	public static final int CODE_CANNOT_MODIFY = 55201;	//不能修改
	public static final int USER_CODE_CANNOT_MODIFY = 55202; //不能修改
	public static final int CODE_EXISTS_ORDERID = 55101;	//已经存在
	public static final int CODE_BANK_IS_BIND = 59101; // 用户与银行卡重复绑定
	public static final int CODE_NOT_FLOAT_GNUM = 57025; // 商品数量不满足计量单位允许的小数位数
	public static final int CODE_PUSH_FAIL = 59805;
	public static final int CODE_PARAMS_NOT_VALID = 59806;
	/**
	 * bean不存在
	 */
	public static final int CODE_BEAN_IS_NULL = 59807;
	
	
	
	/**
	 * 数值范围越界
	 */
	public static final int CODE_NUM_OUT_BOUND = 50020;
	/**
	 * bean不存在
	 */
	public static final int CODE_POINGT_IS_NULL = 59808;
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是6XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_NOT_AVAIL_NUM = 60401;	//有效数量不足
	public static final int CODE_CANNOT_GRAB_CURDATE = 60403;	//当天已经不能再领取
	public static final int CODE_INVALID = 60004; //无效
	public static final int CODE_ERROR_TOKEN = 60007;	//商铺token错误
	public static final int CODE_ERROR_SESSION = 60007;   //session错误
	public static final int CODE_NO_BELONG_TO = 60010; //不属于
	public static final int CODE_FILE_UPLOAD_FAIL = 60901; //文件上传失败，连接文件服务器出问题
	public static final int CODE_ORDER_GROUP_ORDER_NOT_EXIST = 61101; //订单组中的订单不存在
	public static final int CODE_MEMBER_NOT_SMS_PAY = 60111; //该用户不支付短信支付
	public static final int CODE_MEMBER_NOT_ENOUGH_MONEY = 60121; //余额不足，扣除余额XX元，还需支付现金XX元！
	public static final int CODE_MEMBER_NOT_EXIST = 60131; //手机号不是会员
	
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是7XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_SHOP_STATUS_ERROR = 70001;
	public static final int CODE_SHOP_NOT_BINDING = 70002;
	public static final int CODE_SHOP_STATUS_OFFLINE = 70003;//下线
	public static final int CODE_SHOP_STATUS_ARREARS = 70004;//欠费
	public static final int CODE_SHOP_STATUS_PENDING = 70005;//待审核
	public static final int CODE_SHOP_STATUS_BINDING = 70006;//已删除
	
	public static final int CODE_ORDER_STATUS_NOT_SUPPORT_DELETE = 70401;//只有处于已退单或已完成的订单才能删除
	public static final int CODE_ACCOUNT_NOT_ENOUGH = 72401; //用户的账户余额不足
	public static final int CODE_ONLINE_INCOME_NOT_ENOUGH = 72405; //线上营业收入账户余额不足
	public static final int CODE_PLATFORM_REWARD_NOT_ENOUGH = 72406;  //平台奖励账户余额不足
	public static final int CODE_MONEY_NOT_EQUAIL = 72407; //资金之间金额关系错误
	public static final int CODE_ORDER_NOT_EXIST = 72408;//订单不存在
	public static final int CODE_USER_NOT_EXIST = 72409;//用户不存在
	public static final int CODE_GOODS_NOT_EXIST = 72410;//商品不存在
	public static final int CODE_IS_NOT_SIGLE_GOODS_GROUP = 72411;//非单商品的商品族
	public static final int CODE_72402 = 72402;//重复提交支付
	public static final int CODE_72403 = 72403;//跳号提交支付
	public static final int CODE_72404 = 72404;//降号提交支付
	public static final int CODE_72405 = 72405;//超额支付了
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是8XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static final int CODE_USER_NOT_UPDATE_ORDER = 88301;
	public static final int CODE_ORDER_NOT_UPDATE = 88302;
	/**
	 * 订单金额错误
	 */
	public static final int CODE_ORDER_ORDER_MONEY_ERROR = 88303;
	
	/**
     * 订单金额错误
     */
    public static final int CODE_ORDER_PAY_MONEY_ERROR = 88304;
	/**
     * shopId和商圈id不匹配
     */
    public static final int CODE_PARAMETER_MATCHING_ERROR = 88305;
	
	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面是9XXXX错误码配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/*------------------- wifiDog code -----------------*/
	public static final int CODE_SEND_ERROR = 90702;//路由器密码发送失败
	public static final int CODE_PHP_REMAND_FLAG_CLOSE=91101;//提醒已经关闭
	
	 /**
     * 商铺线上收入不足
     */
    public static final int ONLINE_INCOME_AMOUNT_ERROR = 72401;
    
    /**
    * 商铺奖励不足
    */
   public static final int REWARD_AMOUNT_ERROR = 72402;
   
	
	/*======================================================================== message =================================================================================================*/
	/*-------------------- common message --------------*/
	public static final String MSG_404 = "";
	public static final String MSG_COOKIES_INVALIDATE = "请重新登录"; // 无效的cookie信息
    public static final String MSG_COOKIES_TIMEOUT = "请重新登录";// cookie已过期，需重新登录
    public static final String MSG_COOKIES_NULL = "请重新登录";// 不存在cookie，请登录
	public static final String MSG_REQUIRED_NOT_NULL = "字段不能为空";
	public static final String MSG_FORMAT_ERROR_PARAM = "数据格式不对";
	public static final String MSG_PNO_FMT_ERROR = "页码数据格式错误";
	public static final String MSG_PSIZE_FMT_ERROR = "页容量数据格式错误";
	public static final String MSG_PWD_ERROR = "密码错误";
	public static final String MSG_OLD_PWD_ERROR = "旧密码错误";
	public static final String MSG_PARAMETER_REPEAT = "名称重复"; //字段值重复
	public static final String MSG_PAY_PWD_ERROR = "原支付密码错误";
	public static final String MSG_PAYPWD_AUTHEN_ERROR = "支付密码错误";
	public static final String MSG_PAYPWD_NO_SET = "支付密码未设置";
	public static final String MSG_PAYPWD_YES_SET = "支付密码已设置";
	public static final String MSG_VC_ERROR = "短信验证码错误";
	public static final String MSG_VC_TIMEOUT = "验证码超时";
	public static final String MSG_USER_MOBILE_NULL = "不存在该手机的会员";
	public static final String MSG_REQUIRED_USERNAME = "用户名不能为空";
	public static final String MSG_REQUIRED_TURENAME = "真实姓名不能为空";
	public static final String MSG_REQUIRED_MOBILE = "手机号码不能为空";
	public static final String MSG_REQUIRED_MONEY = "money不能为空";
	public static final String MSG_FORMAT_ERROR_MOBILE = "手机号码数据格式错误";
	public static final String MSG_REQUIRED_VERICODE = "验证码不能为空";
	public static final String MSG_REQUIRED_MOBILE_VALID = "手机号码格式不对";
	public static final String MSG_FORMAT_ERROR_IDENTITY_CARD_NO = "身份证号码格式不对";
	public static final String MSG_FORMAT_ERROR_SHOPMANAGERIDENTITYCARDNO = "法人身份证号码格式不对";
	public static final String MSG_REQUIRED_NEWMOBILE = "新手机号码不能为空";
	public static final String MSG_REQUIRED_NEWMOBILE_VALID = "新手机号码格式不对";
	public static final String MSG_REQUIRED_PWD = "密码不能为空";
	public static final String MSG_REQUIRED_NEWPWD = "新密码不能为空";
	public static final String MSG_REQUIRED_PAYPWD = "支付密码不能为空";
	public static final String MSG_REQUIRED_NEWPAYPWD = "新支付密码不能为空";
	public static final String MSG_REQUIRED_CFPWD = "确认密码不能为空";
	public static final String MSG_REQUIRED_BANKCARD = "银行卡不能为空";
	public static final String MSG_REQUIRED_BANKNAME = "银行名称不能为空";
	public static final String MSG_REQUIRED_USER_SOURCE_ID = "用户来源ID不能为空";
	public static final String MSG_FORMAT_ERROR_USER_SOURCE_ID = "用户来源数据格式错误";
	public static final String MSG_FORMAT_ERROR_USER_SOURCE_CHANNEL = "用户来源通道数据格式错误";
	public static final String MSG_BANK_IS_BIND = "银行卡与用户已经绑定,无需再次绑定";
	public static final String MSG_REQUIRED_ACCOUNTNAME = "开户姓名不能为空";
	public static final String MSG_REQUIRED_IDCARD = "身份证不能为空";
	public static final String MSG_NOSAME_PWD = "密码不一致";
	public static final String MSG_NEWPWD_NOSAME_PWD = "两次新密码不一致";
	public static final String MSG_USER_REGISTERED = "用户已被注册";
	public static final String MSG_WEIXIN_USER_BINDED = "微信号与会员已经绑定";
	public static final String MSG_USER_NOT_EXIST = "用户不存在";
	public static final String MSG_USER_IS_NOT_ACTIVITY = "用户未激活";
	public static final String MSG_USER_NOT_SETTLE = "用户还未入驻";
	public static final String MSG_MOBILE_REGISTERED = "手机号码已被注册";
	public static final String MSG_SUCCEED_REG = "注册成功";
	public static final String MSG_SUCCEED_RESETPWD = "重置密码成功";
	public static final String MSG_SUCCEED_RESETMOBILE = "修改手机号码成功";
	public static final String MSG_SUCCEED_MODIFY_PAY_PWD = "支付密码修改成功";
	public static final String MSG_SUCCEED_INSERT_PAY_PWD = "设置支付密码成功";
	public static final String MSG_SUCCEED_VERICODE = "验证成功";
	public static final String MSG_SUCCEED_SMSCODE = "短信验证码发送成功";
	public static final String MSG_SUCCEED_GET_CODE = "获取手机验证码成功";
	public static final String MSG_SUCCEED_CITY = "获取城市信息列表成功";
	public static final String MSG_REQUIRED_INT = "整数不能为空";
	public static final String MSG_INVALID_PARAMETER ="参数不合法";
	public static final String MSG_INVALID_BIZID ="bizId参数不合法";
	public static final String MSG_INVALID_BIZTYPE ="bizType参数不合法";
	public static final String MSG_COMMENTTYPE_IS_NULL ="commentType不能为空";
	public static final String MSG_SERVICEGRADE_IS_NULL ="serviceGrade不能为空";
	public static final String MSG_ENVGRADE_IS_NULL ="envGrade不能为空";
	public static final String MSG_COMMENTCONTENT_IS_NULL ="commentContent不能为空";
	public static final String MSG_BIZID_IS_NULL ="bizId不能为空";
	public static final String MSG_BIZTYPE_IS_NULL ="bizType不能为空";
	public static final String MSG_INVALID_COMMENTTYPE ="commentType参数不合法";
	public static final String MSG_INVALID_STARLEVEL_GRADE ="starLevelGrade参数不合法";
	public static final String MSG_FORMAT_ERROR ="userId格式不合法";
	public static final String MSG_ColumnId_FORMAT_ERROR ="shopColumnId格式不合法";
	public static final String MSG_FORMAT_ERROR_ACCOUNT_ID ="accountId格式不合法";
	public static final String MSG_REQUIRED_VERI_CODE ="验证码不能为空";
	public static final String MSG_JSON_ERROR ="json无法解析";
	public static final String MSG_REGID_SUCCESS = "RegID注册成功";
	public static final String MSG_MYREF_SUCCESS = "获取信息成功";
	public static final String MSG_ORDERS_SUCCESS = "获取订单列表成功";
	public static final String MSG_ORDERNUM_SUCCESS = "获取我的订单个数成功";
	public static final String MSG_REGID_NULL ="regId不能为空";
	public static final String MSG_NOT_FLOAT_GNUM ="商品数量不满足计量单位允许的小数位数";
	public static final String MSG_ORDER_DETAIL_LIST_SUCCESS = "获取订单详情列表成功";	
	public static final String MSG_ORDER_LIST_SUCCESS = "查询成功";	
    public static final String MSG_CONFIG_VALUE_NULL ="商圈活动配置项不能为空";
	/*-------------------- wifiDog message --------------*/
	public static final String MSG_INVALID_WIFIDOG = "无效的wifiDog信息";
	public static final String MSG_MISS_WIFIDOG = "不存在的wifiDog信息";
	public static final String MSG_INVALID_USERTOKEN = "无效的userToken信息";
	public static final String MSG_MISS_USERTOKEN = "不存在的userToken信息";
	public static final String MSG_MISS_ROUTER = "设备信息不存在";
	public static final String MSG_COMMENT_STATUS_ERROR = "订单状态异常,不能评论";//订单状态异常，不可评论
	
	/*-------------------- member message --------------*/
	public static final String MSG_SUCCEED_CALL = "调用成功";
	public static final String MSG_SUCCEED_OPERATION = "操作成功";
	public static final String MSG_SUCCEED_GET_PAYPWD_STATUS = "查询支付密码设定状态成功";
	public static final String MSG_REQUIRED_MEMBER = "会员不能为空";
	public static final String MSG_SUCCEED_MEMBER = "获取会员基本资料成功";
	public static final String MSG_SUCCEED_LOGIN = "登录成功";
	public static final String MSG_SUCCEED_BINDCARD = "绑定银行卡成功";
	public static final String MSG_SUCCEED_UNBINDCARD = "解除绑定银行卡成功！";
	public static final String MSG_SUCCEED_EDITUSER = "编辑会员资料成功";
	public static final String MSG_MISS_MEMBER = "用户信息不存在";
	public static final String MSG_MISS_SHOP_MEMBER_CARD = "店内会员卡不存在";
	public static final String MSG_SUCCEED_AUTHUSER = "实名认证成功！";
	public static final String MSG_SUCCEED_BANKCARDS = "获取银行卡列表成功！";
	public static final String MSG_USER_ACCOUNT_NOT_EXIST = "用户账户信息不存在！";
	public static final String MSG_USER_ACCOUNT_FROZEN = "用户账户已冻结！";	
	public static final String MSG_SUCCEED_AUTHPAYPWD = "验证支付密码成功！";
	public static final String MSG_SUCCEED_USER_BILL = "获取账单成功！";
	public static final String MSG_SUCCEED_USER_CARD = "获取用户会员卡列表成功";
	public static final String MSG_SUCCEED_ORDER_COMMENT = "获取订单评论列表成功";
	public static final String MSG_SUCCEED_CARD_INFO = "获取用户会员卡详细信息成功";
	public static final String MSG_SUCCEED_ACCOUNT_MONEY = "获取传奇宝账户余额成功";
	public static final String MSG_SUCCEED_REDPACKET_MONEY = "获取红包余额成功";
	public static final String MSG_SUCCEED_QUERY_HISTORY = "获取搜索历史成功";
	public static final String MSG_SUCCEED_DELETE_HISTORY = "清空历史搜索条件成功";
	public static final String MSG_ACCOUNT_NULL = "会员卡不能为空";
	public static final String MSG_ACCOUNT_NOT_EXIST = "会员卡不存在";
	public static final String MSG_COMMENT_SUCCESS = "评论成功";
	public static final String MSG_MESSAGE_SUCCESS = "获取信息成功";
	public static final String MSG_USER_STATUS_UNUSUAL = "用户状态异常";
	public static final String MSG_USER_STATUS_FREEZE = "帐号已冻结";
	public static final String MSG_USER_STATUS_LOGOUT = "帐号已注销";
	public static final String MSG_USER_STATUS_WAIT_ACTIVE = "帐号未激活，请登录一点传奇客户端进行激活";
	public static final String MSG_USER_STATUS_FREEZE_FAIL  = "登录失败，此帐号已被冻结";
	public static final String MSG_USER_STATUS_LOGOUT_FAIL = "登录失败，此帐号已被注销";
	public static final String MSG_ERROR_SESSION = "sessionId无效";   //session错误
	
	public static final String MSG_REQUIRED_ACCOUNT_TYPE = "账户类型不能为空";
	public static final String MSG_REQUIRED_USER_ID = "用户ID不能为空";
	public static final String MSG_REQUIRED_ADDR_ID = "地址ID不能为空";
	public static final String MSG_REQUIRED_REFER_MOBILE = "被推荐人手机号码不能为空";
	public static final String MSG_REQUIRED_REFER_CODE = "推荐码不能为空";
	public static final String MSG_FORMAT_ERROR_USERID = "用户ID数据格式错误";
	public static final String MSG_LEN_ERROR_USERID = "用户ID参数过长";
	public static final String MSG_REQUIRED_RESOURCE_TYPE = "resourceType不能为空";
	public static final String MSG_REQUIRED_START_TIME = "预订的开始时间不能为空";
	public static final String MSG_FMT_ERROR_START_TIME = "预定开始时间数据格式错误";
	public static final String MSG_REQUIRED_END_TIME = "预定结束时间不能为空";
	public static final String MSG_FMT_ERROR_END_TIME = "预定结束时间数据格式错误";
	
	public static final String MSG_REQUIRED_OPEN_ID = "微信用户openId不能为空";
	
	public static final String MSG_REFER_CODE_54701 = "该手机号码已被推荐过";
	public static final String MSG_ACCOUNT_NOT_BALANCE = "账户余额不足";
	public static final String MSG_SUCCEED_GET_USERVOUCHERINFO = "获取用户抵用券汇总信息成功";
	public static final String MSG_VERICODE_55901 = "验证码发送失败";
	public static final String MSG_VERICODE_55902 = "验证码用途不能为空";
	public static final String MSG_REQUIRED_USAGE = "usage不能为空";
	/*-------------------- shop message --------------*/
	public static final String MSG_MISS_SHOP = "指定的商铺不存在";
	public static final String MSG_MISS_SHOPMEMBER = "指定的店内会员不存在";
	public static final String MSG_MISS_USER = "指定的用户不存在";
	public static final String MSG_MISS_SERVER_USER = "指定的服务人员不存在";
	public static final String MSG_MISS_GOOD = "指定的商品不存在";
	public static final String MSG_MISS_SHOP_ID = "商铺ID不能为空";
	public static final String MSG_MISS_GOOD_ID = "商品ID不能为空";
	public static final String MSG_FORMAT_ERROR_SHOP_ID = "商铺ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_GOODS_ID = "商品ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_GOODS_CATEGORY_ID = "商品分类数据格式错误";
	public static final String MSG_FORMAT_ERROR_CASHIER_ID = "收银员ID数据格式错误";
	public static final String MSG_LEN_ERROR_SHOP_ID = "商铺ID参数过长";
	public static final String MSG_FORMAT_ERROR_GOODS_NUM = "商品数量格式错误";
	public static final String MSG_FORMAT_ERROR_GOODS_INDEX = "商品排序格式错误";
	public static final String MSG_REQUIRED_SHOPID = "商铺ID不能为空";
	public static final String MSG_REQUIRED_ORDER_CODE = "payCode不能为空";
	public static final String MSG_REQUIRED_STORAGENO = "该单号已存在，不允许重复";
	public static final String MSG_REQUIRED_SEATID = "商铺座位ID不能为空";
	public static final String MSG_REQUIRED_SERVICE_TYPE = "服务方式不能为空";
	public static final String MSG_NOT_ONLINE_SHOP = "商铺不处于上线状态";
	public static final String MSG_NOT_IN_OPENTIME = "当前不在商铺营业时间内";
	public static final String MSG_COMMENT_TIME_ERROR = "不能重复对同一商铺或商品进行评论";
	public static final String MSG_ERROR_SHOP_TOKEN = "商铺设备token错误";
	public static final String MSG_MISS_SHOP_STATUS="商铺状态异常";
	public static final String MSG_SHOP_NOT_BINDING = "未绑定指定商铺";
	/*-------------------- order message --------------*/ 
	public static final String MSG_FORMAT_ERROR_ORDERSTATUS = "订单状态数据格式错误";
	public static final String MSG_PAYED_ODDER = "订单已经完成支付";
	public static final String MSG_INVALID_ODDER_STATUS = "无效的订单状态";
	public static final String MSG_INVALID_RERVERSE_DATE = "预定日期不能早于当前时间";
	public static final String MSG_PAYSTATUS_BE_PAYED = "订单为结账状态，支付状态必须是已支付";
	public static final String MSG_PAYAMOUNT_LESS = "实收金额小于应支付金额";
	public static final String MSG_REQUIRED_PAYTIME = "支付时间不能为空";
	public static final String MSG_REQUIRED_SHOPS = "商铺ID不能为空";
	public static final String MSG_REQUIRED_OGOODS = "订单的商品不能为空";
	public static final String MSG_REQUIRED_POS_DATA = "收银机订单详情不能为空";
	public static final String MSG_REQUIRED_BILLER_ID = "下单员ID不能为空";
	public static final String MSG_REQUIRED_CASHIER_ID = "收银员ID不能为空";
	public static final String MSG_REQUIRED_GPRICE = "商品价格不能为空";
	public static final String MSG_REQUIRED_GNUM = "商品数量不能为空";
	public static final String MSG_REQUIRED_ORDER_SCENE = "下订单场景类别不能为空";
	public static final String MSG_REQUIRED_DISTRIB_TYPE = "配送方式不能为空";
	public static final String MSG_REQUIRED_ORDER_TYPE = "订单类型不能为空";
	public static final String MSG_REQUIRED_ORDER_STATUS = "订单状态不能为空";
	public static final String MSG_REQUIRED_ORDER_ISMALING = "订单抹零标志不能为空";
	public static final String MSG_REQUIRED_SEND_TIME = "配送时间不能为空";
	public static final String MSG_REQUIRED_STARTTIME = "预约服务开始时间不能为空";
	public static final String MSG_REQUIRED_STOPTIME = "预约服务结束时间不能为空";
	public static final String MSG_REQUIRED_OTOTAL_PRICE = "订单总价不能为空";
	public static final String MSG_REQUIRED_PAY_REASON= "订单支付原因不能为空";
	public static final String MSG_REQUIRED_TARGET_ID= "支付目标Id不能为空";
	public static final String MSG_REQUIRED_PAYAMOUNT = "订单支付金额不能为空";
	public static final String MSG_REQUIRED_CLIENT_SYSTEM = "发起交易的客户系统ID不能为空";
	public static final String MSG_REQUIRED_PAY_CHANNEL = "支付渠道ID不能为空";
	public static final String MSG_REQUIRED_SUB_PAY_MODEL = "支付渠道子支付方式不能为空";
	public static final String MSG_MISS_BOOK_RULE = "指定预约资源规则不存在";
	public static final String MSG_MISS_ORDER_SCENE = "指定订单场景类别不存在";
	public static final String MSG_MISS_BILLER = "下单员信息不存在";
	public static final String MSG_MISS_CASHIER = "收银员信息不存在";
	public static final String MSG_ERROR_TOKEN = "收银机token错误";
	public static final String MSG_EXISTS_ORDERID = "订单号已经存在";
	public static final String MSG_NOT_SAME_USERID = "订单所属会员ID不能被修改";
	public static final String  MSG_ORDER_NOT_EXIST = "订单不存在";
	public static final String  MSG_ORDER_ORDER_MONEY_ERROR ="参数money与订单实付金额不一致";
	public static final String  MSG_ORDER_IS_EXIST = "订单已生成";
	public static final String  MSG_INTEVAL_NO_RULE = "预定的时间段不属于指定资源";
	public static final String MSG_REQUIRED_SENDTYPE = "订单配置方式不能为空";
	public static final String MSG_REQUIRED_PAYTYPE = "支付方式不能为空";
	public static final String MSG_REQUIRED_PAY_STATUS = "订单支付状态不能为空";
	public static final String MSG_REQUIRED_ORDERID = "订单ID不能为空";
	public static final String MSG_REQUIRED_ORDERTYPE="订单类型不能为空";
	public static final String MSG_REQUIRED_PREPAY = "定金不能为空";
	public static final String MSG_REQUIRED_LOGISTICS_PRICE = "服务费或物流费不能为空";
	public static final String MSG_FORMAT_ERROR_ORDERID = "订单ID数据格式错误";
	public static final String MSG_REQUIRED_TOTALPRICE = "订单总价不能为空";
	public static final String MSG_DEFADDRESS_NULL = "没有默认收货地址";
	public static final String MSG_REQUIRED_GOODS_SHOP= "订单中商品所属商铺不能为空";
	public static final String MSG_REQUIRED_GOODS_NUM= "商品数量不能为空";
	public static final String MSG_REQUIRED_OSRCS= "预定资源列表不能为空";
	public static final String MSG_SUCCEED_PLACEORDER = "下单成功";
	public static final String MSG_SUCCEED_UPDATEORDER = "更新订单成功";
	public static final String MSG_SUCCEED_ORDERDETAIL = "获取订单详情成功";
	public static final String MSG_SUCCEED_RESERV = "预定成功";
	public static final String MSG_PAUSE_DATE = "预约日期暂停预定商铺资源";
	public static final String MSG_NOT_FORWARD_DATE = "不满足提前预定天数";
	public static final String MSG_NOT_FORWARD_HOUR = "不满足提前预定小时数";
	public static final String MSG_NOT_ZERO_GNUM = "商品数量不能为0或小于0";
	public static final String MSG_SUCCEED_GEN_ORDERID = "获取订单ID成功";
	public static final String MSG_SUCCEED_MESSAGE = "获取消息列表成功";
	public static final String MSG_MISS_ORDER = "不存在指定的订单信息";
	public static final String MSG_REQUIRED_DATE = "日期不能为空";
	public static final String MSG_FORMAT_ERROR_DATE = "日期格式错误";
	public static final String MSG_MISS_OGOODS = "该订单不存在商品信息列表";
	public static final String MSG_TERMINAL_TYPE_IS_NULL = "终端类型不能为空";
	public static final String MSG_RDORG_NAME_IS_NULL = "第三方支付平台名称不能为空";
	public static final String CODE_USER_ORDER_REPEAT = "同一个用户不能重复相同的订单";
	public static final String MSG_TRANSACTION_IS_EXIT = "账单信息已经提交，请耐心等待审核";
	public static final String MSG_TRANSACTIONID_IS_NULL = "rdTransactionId不能为空";
	public static final String MSG_3RD_TRANSACTIONID_IS_NULL = "3rdtransactionId不能为空";
	public static final String MSG_PAYCHANNELID_FORMAT_ERROR ="用户支付渠道id格式不合法";
	public static final String MSG_STATUS_IS_NULL ="交易状态不能为空";
	public static final String MSG_STATUS_FORMAT_ERROR ="交易状态格式不合法";
	public static final String MSG_PAYAMOUNT_FORMAT_ERROR ="金额格式不合法";
	public static final String MSG_AMOUNT_IS_NULL ="金额不能为空";
	public static final String MSG_REQUIRED_CHARGETYPE = "充值类型不能为空";
	public static final String MSG_NO_BELONG_TO_SHOP = "商品不属于同一个商铺";
	public static final String MSG_SHOP_NO_SAME = "订单所属商铺不能修改";
	public static final String MSG_NOT_MODIFY_SETTLE = "订单已经结账，不能被修改";
	public static final String MSG_LIVE_BE_ALLPRICE = "到店点菜必须为全额支付订单";
	public static final String MSG_MEMBER_CARD_NOT_ENOUGH = "会员卡余额不足";
	public static final String MSG_MEMBER_CARD_NOT_EXIST = "会员卡余额不足";
	 /**
     * bean不存在
     */
    public static final String MSG_BEAN_IS_NULL = "bean不存在";
	 /**
     * 执行点不存在
     */
    public static final String MSG_POINT_IS_NULL = "执行点不存在";
	
	/***会员卡信息**/
	public static final String MSG_REQUIRED_SHOPMC_MOBILE = "会员卡手机号不能为空";
	
	public static final String MSG_REQUIRED_SHOPMC_CHARGE_MONEY = "充值金额不能为空";
	
	//交易成功状态标示
	public static final Integer CODE_STATUS_SUCCSSS = 1;
	public static final String MSG_ORDER_NOT_BELONG_TO_YOU = "订单不属于本人";
	public static final String MSG_ORDER_GROUP_ORDER_NOT_EXIST = "订单组中的订单不存在";//订单组中的订单不存在
	public static final String MSG_ORDER_GROUP_NOT_EXIST = "订单组不存在";//订单组不存在
	public static final String MSG_ORDER_ID_NULLMSG_ORDER_ID_NULL = "订单id不能为空";//订单组不存在
	public static final String MSG_ORDER_STATUS_ERROR = "订单状态异常，不可支付";//订单状态异常，不可支付
	public static final String MSG_ORDER_GOODS_SETTLE_ERROR = "没有有效的订单分成比例";//没有有效的订单分成比例
	public static final String MSG_ORDER_STATUS_NOT_SUPPORT_CANCEL = "订单状态不支持取消";//订单状态不支持取消
	public static final String MSG_ORDER_STATUS_NOT_SUPPORT_DELETE = "只有处于已退单或已完成的订单才能删除";
	
	public static final String MSG_DISABLE_HOME_SERVICE = "商铺未启用预约上门";
	public static final String MSG_DISABLE_TAKEOUT = "商铺未启用外卖";
	public static final String MSG_DISABLE_BOOK = "商铺未启用预定";
	public static final String MSG_LEAST_BOOK_PRICE_ERROR = "订单最低起订金额不满足";
	public static final String MSG_NOTIN_DELIVERY_TIME = "当前不在商铺接单时间范围内";
	public static final String MSG_IN_STOP_DATE = "商铺当前处于暂停预定时间范围内";
	public static final String MSG_DISTRIB_DATE_IN_STOP_DATE = "配送时间处于暂停预定时间范围内"; 
	public static final String MSG_DISTRIB_DATE_NOTIN_WEEK = "配送时间不在商铺可预定周期内"; 
	public static final String MSG_BOOK_DATE_NOTIN_WEEK = "预定时间不在商铺可预定周期内"; 
	public static final String MSG_DISTRIB_TIME_NOTIN_DELIVERY = "配送时间不在可配送时间范围内"; 
	public static final String MSG_BOOK_TIME_NOTIN_DELIVERY = "预定时间点不在可预定时间范围内"; 
	public static final String MSG_BOOK_TIME_IN_STOP_DATE = "预定日期处于暂停预定时间范围内"; 
	public static final String MSG_ORDER_NOT_PAY_BY_SMS = "非会员订单，不支持短信支付";
	
	/*-------------------- reserve shop resource --------------*/
	public static final String MSG_REQUIRED_JS_ID = "技师ID不能为空";
	public static final String MSG_REQUIRED_CD_ID = "场地ID不能为空";
	public static final String MSG_FORMAT_ERROR_JS_ID = "技师ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_CD_ID = "场地ID数据格式错误";
	public static final String MSG_MISS_SHOP_JS = "技师信息不存在";
	public static final String MSG_MISS_SHOP_CD = "场地信息不存在";
	
	/*-------------------- cart message --------------*/
	public static final String MSG_SUCCEED_UPDATECART = "更新购物车商品成功";
	public static final String MSG_SUCCEED_SYNCBOOK = "预定成功";
	public static final String MSG_SUCCEED_GETCART = "获取购物车商品列表成功";
	/*-------------------- redPacket message --------------*/
	public static final String MSG_REQUIRED_REDPACKET_ID = "红包ID不能为空";
	public static final String MSG_MISS_REDPACKET_NOT_EXIST = "红包不存在";
	public static final String MSG_FORMAT_ERROR_REDPACKET_ID = "红包ID数据格式错误";
	public static final String MSG_REQUIRED_RED_BATCH_NO = "红包批次号不能为空";
	public static final String MSG_REDPACKET_USE_58501 = "该红包不属于您";//
	public static final String MSG_REDPACKET_USE_58502 = "该红包已被使用";//
	public static final String MSG_REDPACKET_USE_58503 = "该红包还不到使用时间";
	public static final String MSG_REDPACKET_USE_58504 = "该红包已经过了使用期限";
	public static final String MSG_REDPACKET_USE_58505 = "该订单已经支付完成，不需要再进行支付";
	public static final String MSG_REDPACKET_USE_58506 = "发行该红包的商铺状态异常，暂时不能使用";
	public static final String MSG_REDPACKET_USE_58507 = "该订单已经不需要再进行支付";
	public static final String MSG_REDPACKET_USE_58601 = "手慢了，红包已被别人抢走了";
	public static final String MSG_FORMAT_TIME_ERROR_REDPACKET = "红包使用期限时间格式有误";
	public static final String MSG_ERROR_ORDER_TYPE_FMT = "订单支付类型格式有误";
	public static final String MSG_PAY_LIST_SUCCESS = "获取我的支付列表成功";
	public static final String MSG_NOFITY_3RDPAY_STATUS_SUCCESS = "成功接收到反馈";
	public static final String MSG_WITHDRAW_SUCCESS = "发起提现成功";
	public static final String MSG_SEARCH_WITHDRAW_SUCCESS = "获取信息成功";
	public static final String MSG_CHARGE_SUCCESS = "发起充值成功";
	public static final String MSG_REQUIRED_AMOUNT = "充值金额不能为空";
	/*-------------------- redPacket message --------------*/
	public static final String MSG_MISS_GOODS = "不存在指定商品信息";
	public static final String MSG_REQUIRED_GOODS_ID = "商品ID不能为空";
	/*-------------------- ad message --------------*/
	
	public static final String MSG_REQUIRED_PROVINCEID = "省ID不能为空";
	public static final String MSG_REQUIRED_CITYID = "城市ID不能为空";
	public static final String MSG_FORMAT_ERROR_CITYID = "城市ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_ADSPACEID = "广告位ID数据格式错误";
	public static final String MSG_REQUIRED_ADSPACE_CODE = "广告位编码不能为空";
	/*-------------------- goods message --------------*/
	public static final String MSG_MISS_TOPTYPE = "不存在该top商品分类";
	/*-------------------- shop message --------------*/
	public static final String MSG_FORMAT_ERROR_SHOPID = "商铺ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_TRANSACTIONID = "交易记录ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_WITHDRAWID = "提现ID数据格式错误";
	public static final String MSG__REQUIRED_GOODSBARCODE = "商品条形码不能为空";
	public static final String MSG_FORMAT_ERROR_SHOPTYPE = "店铺类型数据格式错误";
	public static final String MSG_FORMAT_ERROR_ACTIVITYSTATUS = "活动状态数据格式错误";
	public static final String MSG_FORMAT_ERROR_COLUMNID = "活动状态数据格式错误";
	public static final String MSG_FORMAT_ERROR_ACTIVITYID = "商铺活动ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_CASHIERID = "收银员ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_RESOURCE_ID = "商铺资源ID数据格式错误";
	public static final String MSG_MISS_RSRCGROUP = "商铺资源组不存在";
	public static final String MSG_MISS_TIMEINT = "商铺可用时间段不存在";
	public static final String MSG_REQUIRED_GROUPID = "商铺资源组ID不能为空";
	public static final String MSG_REQUIRED_TIMEINTID = "商铺可用时间段不能为空";
	public static final String MSG_REQUIRED_RSRCNUM = "预定资源个数不能为空";
	public static final String MSG_REQUIRED_RESERV_DATE = "预定时间日期不能为空";
	public static final String MSG_REQUIRED_ZANTYPE = "点赞类型不能为空";
	public static final String MSG_FORMAT_ERROR_ZANTYPE = "zanType数据格式错误";
	public static final String MSG_REQUIRED_BIZID = "bizId不能为空";
	public static final String MSG_REQUIRED_BIZTYPE = "bizType不能为空";
	public static final String MSG_FORMAT_ERROR_BIZTYPE = "bizType数据格式错误";
	public static final String MSG_REQUIRED_FEEDBACK_TYPE = "反馈类型不能为空";
	public static final String MSG_FORMAT_ERROR_FEEDBACK_TYPE = "反馈类型数据格式错误";
	public static final String MSG_FORMAT_ERROR_BIZID = "bizId数据格式错误";
	public static final String MSG_SUCCESS_ZAN = "点赞成功";
	public static final String MSG_SUCCESS_GET_TAKEOUT = "获取商铺外卖设置信息成功";
	public static final String MSG_MISS_RESOURCE = "商铺资源不存在";
	public static final String MSG_RESOURCE_STATUS_NOT_AVALIABLE = "商铺资源已被预订或使用";
	public static final String MSG_REQUIRED_RESOURCE_ID = "商铺资源ID不能为空";
	public static final String MSG_RESOURCE_ID_ERROR = "商铺资源ID不合法";
	public static final String MSG_SUCCEED_USE_RESOURCE = "使用资源成功";
	public static final String MSG_NO_ENOUGH_RSCRGROUP = "没有足够的资源组";
	public static final String MSG_SUCCEED_UPLOADFILE = "上传成功";
	public static final String MSG_USER_CANCEL_ATTENTION_FAIL="用户取消关注失败，用户未关注此商铺";//取消关注失败
	public static final String MSG_SHOP_ACCOUNT_NOT_EXIST="商铺账户不存在";//商铺账户不存在
	public static final String MSG_MISS_DIS_BOOK_RULE = "店铺外卖费用设置信息不存在";
	public static final String MSG_MISS_TIME_BOOK_RULE = "店铺时间预定规则信息不存在";
	public static final String MSG_FORMAT_ERROR_TAKEOUT_SETTYPE = "店铺外卖费用设置类型数据格式错误";
	public static final String MSG_REQUIRED_SETTING_TYPE = "设置类型不能为空";
	public static final String MSG_POSITIVE_SETTING_VALUE = "设置值必须为正数";
	public static final String MSG_REQUIRED_SETTING_VALUE = "设置值不能为空";
	
	/*-------------------- shop member message --------------*/
	public static final String MSG_MIS_MEMBER_SOURCE = "导入会员source不能为空";
	public static final String MSG_MIS_MEMBERS = "导入会员members不能为空";
	
	/*-------------------- app message --------------*/
	public static final String MSG_REQUIRED_APP_ID = "App编号不能为空";
	public static final String MSG_FORMAT_ERR_APP_ID = "App编号格式错误";
	public static final String MSG_REQUIRED_APP_VERSION = "App版本号不能为空";
	public static final String MSG_MISS_APP_DATA = "App应用信息不存在";
	public static final String MSG_MISS_APP_VERSION = "未查询到最新版本信息";
	
	public static final String MSG_REQUIRED_FEEDBACKID="反馈编号不能为空";
	/*-----------------------CASHCOUPON message - ------------*/
	public static final String MSG_BE_USED_CC = "代金券已经被使用过";
	public static final String MSG_USER_CASHCOUPON_INVALID = "代金券已经被领完或者不在有效期内";
	public static final String MSG_USER_CASHCOUPON_GRAB_ERR = "领取代金券失败";
	public static final String MSG_USER_CASHCOUPON_GRAB_LIMIT ="已达到每天领取上限";
	public static final String MSG_FORMAT_ERROR_UCCID = "代金券ID数据格式错误";
	public static final String MSG_PAY_STATUS_SUCCESS = "订单已支付";
	public static final String MSG_CASHCOUPON_NOT = "已支付完成（剩余额度<=0），不必再使用代金券！";
	public static final String MSG_USER_CASHCOUPON_NOT_AND_USED="用户不具备该代金券或代金券已经被使用";
	public static final String MSG_CASHCOUPON_NONE_AND_INVALID ="代金券不存在或者代金券不在有效期内";
	public static final String MSG_CASHCOUPON_OTHER_SHOP = "该代金券不能在异店消费";//该代金券不能在异店消费
	public static final String MSG_CASHCOUPON_NO_GOODS = "订单中没有商品";//订单中没有商品
	public static final String MSG_USER_CASHCOUPON_CONDITION_PRICE ="订单金额未达到可使用代金券的额度";
	public static final String MSG_USER_CASHCOUPON_USE_NUMBER = "已达到订单一次最多可使用张数";
	public static final String MSG_USER_CC_USEFUL_PER_ORDER = "已达到订单一次最多可使用张数";
	public static final String MSG_USER_CASHCOUPON_USE_TOGETHER_FLAG = "不允许和同类券一起使用";
	public static final String MSG_REQUIRED_CASHCOUNPONID = "代金券ID不能为空";
	public static final String MSG_FORMAT_ERROR_CASHCOUNPONID = "代金券ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_PAYTYPE = "订单支付类型数据格式错误";
	public static final String MSG_MISS_CASHCOUPON = "指定的代金券不存在";
	public static final String MSG_USER_CC_NOTUSE_SAME = "不能和同类券一起使用";
	
	public static final String MSG_FEEDBACK_LENGTH_BEYOND = "字数应在500以内";
	public static final String MSG_NOT_IN_COUSUME_DATERANGE = "不在有效消费时间范围内";
	
	/*-----------------------coupon message - ------------*/
	public static final String MSG_COUPON_NOT_EXIST = "优惠券不存在或不属于此用户";
	public static final String MSG_COUPON_STATUS_NOT_AVAILABLE = "优惠券不可用";
	public static final String MSG_COUPON_NOT_AVAILABLE_GOODS = "此优惠券不可用于支付非优惠的商品";
	public static final String MSG_FORMAT_ERROR_COUPONSTATUS = "优惠券状态数据格式错误";
	public static final String MSG_FORMAT_ERROR_COUNPONID = "优惠券ID数据格式错误";
	public static final String MSG_REQUIRED_COUNPONID = "优惠券ID不能为空";
	public static final String MSG_MISS_COUPON = "指定的优惠券不存在";
	public static final String MSG_NOT_AVAIL_NUM_COUPON = "优惠券可用数量不足";
	public static final String MSG_NOT_IN_GRAB_DATERANGE = "不在有效领取时间范围内";
	public static final String MSG_CANNOT_GRAB_CURDATE = "当天已经不能再领取优惠券";
	/*---------------user address------------------------*/
	public static final String MSG_USERADDRESS_NONE = "地址不存在";//删除用户地址失败或该用户地址不存在
	public static final String MSG_USERADDRESS_ADD_ERR = "新增用户地址失败！";
	
	/*---------------column message------------------------*/
	public static final String MSG_FORMAT_ERROR_PCOLUMNID = "父级栏目ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_COLUMNTYPE = "栏目类型数据格式错误";
	/*---------------------2016年5月4日------------------------------*/
	public static final String MSG_FORMAT_ERROR_SHOPCLASSIFY = "商户类型数据格式错误";
	/*===================== message end ================*/
	public static final String MSG_FAIL_MOBILE_SMS = "手机短信发送失败";
	public static final String MSG_SUCC_MOBILE_SMS = "手机短信发送成功";
	public static final String MSG_REQUEST_OFTEN = "请求过于频繁，请稍后重试";
	/*-----------------------timedDiscount message - ------------*/
	public static final String MSG_TIMED_DISCOUNT_52501 = "每天开始时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52502 = "每天结束时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52503 = "每周限定日开始时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52504 = "每周限定日结束时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52505 = "自定义开始时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52506 = "自定义结束时间格式错误";
	public static final String MSG_TIMED_DISCOUNT_52601 = "折扣ID不能为空";
	public static final String MSG_TIMED_DISCOUNT_52602 = "折扣ID格式错误";
	public static final String MSG_DATAFORMAT_ERROR = "时间格式错误";
	/*-----------------------collect message - ------------*/
	public static final String MSG_REQUIRED_TOKEN = "商铺设备token不能为空";
	public static final String MSG_REQUIRED_STANDARD_PRICE = "目录价不能为空";
	public static final String MSG_FORMAT_ERROR_STANDARD_PRICE = "目录价数据格式错误";
	public static final String MSG_REQUIRED_APK_NM = "APK名称不能为空";
	public static final String MSG_SHOP_DEVICE_NULL = "商铺设备不存在";
	public static final String MSG_SHOP_STATUS_ERROR = "商铺状态异常";
	public static final String MSG_SHOP_TYPE_ERROR = "不是商铺管理者";
	public static final String MSG_VERSION_TYPE_NULL = "商铺类型不能为空";
	public static final String MSG_SHOP_STATUS_OFFLINE = "商铺已下线";//下线
	public static final String MSG_SHOP_STATUS_ARREARS = "商铺保证金已欠费，请尽快充值";//欠费
	public static final String MSG_SHOP_STATUS_PENDING = "商铺处于审核中";//待审核
	public static final String MSG_SHOP_STATUS_BINDING = "商铺已被删除";//已删除
	
	public static final String MSG_PUSH_TRIGGER_SUCCESS="推送触发成功";
	public static final String MSG_REQUIRED_SNID = "设备唯一识别码不能为空";
	public static final String MSG_REQUIRED_VERSION = "版本号不能为空";
	public static final String MSG_REQUIRED_WARE_NAME = "软件名称不能为空";
	public static final String MSG_REQUIRED_MAC = "设备mac地址不能为空";
	public static final String MSG_REQUIRED_DEVICE_OWNER_TYPE="设备归属类型数据有误";
	public static final String MSG_REQUIRED_CMD="操作类型数据有误";
	/*--------------------------PHP--------------------------*/
	public static final String MSG_AES_DEC_ERROR = "AES解码异常";
	
	/*--------------------------帮助信息--------------------------*/
	public static final String HELP_CATEGORY_FORMAT_ERROR = "帮助信息分类ID格式错误";
	public static final String HELP_CATEGORY_PARENT_FORMAT_ERROR = "帮助信息父分类ID格式错误";
	public static final String SETTING_KEY_NOT_NULL = "settingKey不能为空";
	
	/*--------------------------消息信息--------------------------*/
	public static final String MSG_MESSAGE_SETTING_IS_CLOSED = "消息发送配置已关闭";
	
	public static final String MSG_USER_NOSHOP_USER = "该会员不是商铺会员";
	
	/*--------------------------商品族--------------------------*/
	public static final String MSG_MISS_GOODSGROUP = "不存在商品族信息";
	public static final String MSG_MISS_GOODSGROUP_ID = "商品族ID不能为空";
	public static final String MSG_FORMAT_ERROR_GOODSGROUP_ID = "商品族ID数据格式错误";
	public static final String MSG_FMT_ERROR_GOODSGROUP_RPO_VALUEID = "商品族属性值ID数据格式错误";
	public static final String MSG_MISS_GOODSGROUP_GOODS = "不存在商品族商品信息";
	
	public static final String MSG_REQUIRED_CATEGORY_ID = "商品族分类ID不能为空";
	public static final String MSG_FORMAT_ERROR_CATEGORY_ID = "商品族分类ID数据格式错误";
	
	public static final String MSG_REQUIRED_WEEKDAY = "星期不能为空";
	public static final String MSG_REQUIRED_PRICE = "价格不能为空";
	/*==========================丽人板块==========================*/
	
	/*--------------------------预约设置--------------------------*/
	public static final String MSG_REQUIRED_BK_SETTING_OPERATE_TYPE = "预约设置操作类型不能为空";
	public static final String MSG_REQUIRED_DELIVERY_TIME = "接单时间不能为空";
	
	public static final String MSG_ORDER_NOT_UPDATE = "该订单不允许此操作";
	
	public static final String MSG_USER_NOT_UPDATE_ORDER = "该用户无权操作此订单";
	/**
     * 会员卡批次不存在
     */
    public static final Integer CODE_CASH_CARD_BATCH_NOT_EXIST = 72416;
    
    public static final Integer CODE_SHOP_NOT_EXISTS = 72400;
    
    public static final Integer CODE_SHOP_STATUS_ABNORMAL = 72422;
	/**
	 * 会员卡批次不存在
	 */
	public static final String MSG_CASH_CARD_BATCH_NOT_EXIST = "会员卡批次不存在";
	/**
	 * 会员卡批次不存在
	 */
	public static final String MSG_REQUIRED_CARDNO = "银行卡号不能为空";
	
	//- 商品套餐(次卡)-//
	public static final String MSG_GOODS_SET_NAME_NULL = "套餐名称不能为空";
	public static final String MSG_GOODS_SET_TYPE_NULL = "套餐类型不能为空";
	public static final String MSG_GOODS_STANDARDPRICE_NULL = "套餐价格不能为空";
	public static final String MSG_GOODS_PINYIN_CODE_NULL = "速记码不能为空";
	public static final String MSG_GOODS_UNITNAME_NULL = "计量单位名称不能为空";
	public static final String MSG_GOODS_LIST_NULL = "套餐服务项目不能为空";
	public static final String MSG_SHOP_ID_NULL = "商铺ID不能为空";
	
	/*-------------------- launcher message --------------*/
	public static final String MSG_REQUIRED_LAUNCHER_TYPE = "launcher类型不能为空";
	public static final String MSG_FORMAT_ERROR_LAUNCHER_TYPE = "launcher类型数据格式错误";
	public static final String MSG_SUCCEED_GET_LAUNCHER_ICONS = "获取launcher桌面图标列表成功";
	public static final String MSG_validate_CARDNO_OR_BANKCARDNO_ERROR="验证错误";
	public static final String MSG_REQUIRED_BIZIDSTR = "业务ID不能为空";
	public static final String MSG_REQUIRED_GOODSUNITNAME = "商品单位不能为空";
	
	/*-------------------- 店内会员 --------------*/
	public static final String MSG_VALIDATE_MEMBER_DUP_EXISTS_ERROR="店内会员手机号已存在";
	public static final String MSG_VALIDATE_MEMBER_DUP_NAME_EXISTS_ERROR="店内会员名字已存在";
	public static final String MSG_VALIDATE_MEMBER_DUP_QQ_EXISTS_ERROR="店内会员qq已存在";
	public static final String MSG_VALIDATE_MEMBER_DUP_WX_EXISTS_ERROR="店内会员微信号已存在";
	public static final String MSG_VALIDATE_MEMBER_NO_EXISTS_ERROR="店内会员不存在";
	public static final String MSG_VALIDATE_SOURCE_FILE_ERROR="源文件格式不正确或者没有数据";
	public static final String MSG_REQUIRED_CORE_INFO_VALID="手机号不能为空";
	
	/*--------------------------商圈活动--------------------------*/
	public static final String MSG_REQUIRED_CLIENT_TYPE = "终端类型不能为空";
	public static final String MSG_REQUIRED_BUSAREA_ACT_ID = "商圈活动ID不能为空";
	public static final String MSG_FORMAT_ERROR_BUSAREA_ACT_ID = "商圈活动ID数据格式错误";
	public static final String MSG_FORMAT_ERROR_CLIENT_TYPE = "终端类型数据格式错误";
	public static final String MSG_FORMAT_ERROR_USER_SOURCE_TYPE = "用户来源类型数据格式错误";
	public static final String MSG_INVALID_CLIENT_TYPE ="终端类型参数不合法";
	public static final String MSG_INVALID_OPT_FB_TYPE ="操作反馈类型参数不合法";
	public static final String MSG_INVALID_PROGRAM_TYPE ="程序类型参数不合法";
	public static final String MSG_INVALID_EXECUTE_POINT ="没有找到对应的程序执行点";
	public static final String MSG_INVALID_USER_SOURCE_TYPE ="用户来源类型参数不合法";
    public static final String MSG_PARAMETER_MATCHING_ERROR = "商圈id和商铺id不匹配";
	public static final String MSG_INVALID_FB_TYPE ="反馈类型参数不合法";
	public static final String MSG_SUCCESS_OPT_FB ="操作反馈成功！";
	public static final String MSG_MISS_BUSAREA_STAT ="不存在指定的商圈统计记录！";
	public static final String MSG_MISS_MESSAGE ="不存在指定的消息记录！";
	public static final String MSG_APPLY_BUSAREA_NOT_SUPORT_TOURIST ="不支持游客自行获取商圈活动资格";
	public static final String MSG_PBA2_1 = "未找到商圈活动数据";
	public static final String MSG_PBA2_2 = "未找到商圈活动配置数据";
	public static final String MSG_PBA2_3 = "报名截止时间不能为空";
	public static final String MSG_PBA2_4 = "活动开始时间不能为空";
	public static final String MSG_PBA2_5 = "报名截止时间不能大于活动开始时间";
	public static final String MSG_PBA2_6 = "报名开始时间不能大于报名截止时间";
	public static final String MSG_PBA2_7 = "活动结束时间不能为空";
	public static final String MSG_PBA2_8 = "活动开始时间不能大于活动结束时间";
	public static final String MSG_PBA2_9 = "活动配置项类型不能为空";
	public static final String MSG_PBA2_10 = "活动配置项代码不能为空";
	public static final String MSG_PBA2_11 = "活动配置项值不能为空";
	public static final String MSG_PBA2_12 = "商圈活动类型模板数据不存在";
	public static final String MSG_PBA2_13 = "活动规则主题数据不存在";
	public static final String MSG_PBA2_14 = "当前商铺在该时间段内已经存在活动";
	public static final String MSG_PBA2_15 = "活动报名截止日期必须小于活动开始日期";
	public static final String MSG_PARAMETER_ERROR = "非发起商家不能修改商圈活动";
	public static final String MSG_MISS_GOODSUNIT = "未找到商品单位数据";
	public static final String MSG_GOODSUNITNAME_EXIST = "商品单位已存在，请勿重复添加";
	public static final String MSG_GOODSUNITNAME_COMMON = "不允许删除公用单位";
	public static final String MSG_GOODSUNIT_USED = "该单位已被使用，不能被删除";
	
	/*--------------------------------会员等级-----------------------------*/
	public static final String MSG_LEVEL_ID_NOT_NULL = "等级ID不能为空！";
	public static final String MSG_FORMAT_ERROR_LEVELID = "等级ID格式错误！";
	public static final String MSG_FORMAT_ERROR_LEVELTYPE="等级类型格式错误！";
	public static final String MSG_FORMAT_ERROR_LEVELCONDITION="等级达成条件值格式错误";
	public static final String MSG_FORMAT_ERROR_PREROGATIVEID="特权Id格式不正确";
	public static final String MSG_PREROGATIVEID_NOT_NULL = "特权id不能为空";
	public static final String MSG_FORMAT_ERROR_OPERATETYPE = "操作类型格式错误";
	public static final String MSG_OPERATETYPE_NOT_NULL = "操作类型不能为空";
	public static final String MSG_LEVEL_TYPE_NOT_NULL = "等级类型不能为空";
	public static final String MSG_FORMAT_ERROR_PREROGATIVETYPE="特权类型格式不正确";
	public static final String MSG_FORMAT_ERROR_RULETYPE="规则类型格式不正确";
	public static final String MSG_FORMAT_ERROR_TASKTYPE="任务类型格式不正确";
	public static final String MSG_FORMAT_ERROR_SUBRULETYPE="积分规则子类型格式不正确";
	public static final String MSG_MISS_LEVEL = "指定的等级不存在";
	public static final String MSG_MISS_PREROGATIVE = "指定的特权不存在";
	public static final String MSG_MISS_POINTRULE = "指定的积分规则不存在";
	public static final String MSG_EXIST_POINTRULE = "请勿重复添加相同的积分规则";
	public static final String MSG_FORMAT_ERROR_POINTSOURCETYPE = "积分来源类型格式不正确";
	public static final String MSG_EXIST_LEVEL = "请勿重复添加相同的等级";
	public static final String MSG_CLIENT_SYSTEM_NOT_NULL = "客户系统标识不能为空";
	public static final String MSG_FORMAT_ERROR_CLIENT_SYSTEM = "客户系统标识格式不正确";
	
	public static final String MSG_FORMAT_ERROR_ROLEMODE = "登录角色格式不正确";
	public static final String MSG_FORMAT_ERROR_ISSENDSMS = "是否发送短信格式不正确";
	public static final String MSG_EMPLOYEE_IS_CHECK_FALSE = "用户手机还未通过验证";
	
	/*-----------------------------商铺优惠券---------------------------------------*/
	public static final String MSG_SHOP_COUPON_NOT_NULL = "商铺优惠券信息不能为空";
	public static final String MSG_FORMAT_ERROR_SHOP_COUPON_TYPE = "商铺优惠券格式不正确";
	public static final String MSG_FORMAT_ERROR_USED_TOGETHER = "商铺优惠券是否允许与其他优惠券共用格式不正确";
	public static final String MSG_FORMAT_ERROR_SHARED = "商铺优惠券是否允许共享格式不正确";
	public static final String MSG_REQUIRED_SHOPCOUPONID = "商铺优惠券ID必填";
	public static final String MSG_SHOPCOUPONID_NOT_NULL = "商铺优惠券ID不能为空";
	public static final String MSG_FORMAT_ERROR_SHOPCOUPONIDS = "商铺优惠券ID格式不正确";
	public static final String MSG_FORMAT_ERROR_GOODSCATEGORYIDS = "商铺优惠券的优惠券适用类型为商品类别时商品分类必填";
	public static final String MSG_EMPLOYEE_NOT_NULL = "雇员Id不能为空";
	public static final String MSG_PARAMETER_NOT_ALL_EXIST = "平台会员id与平台会员手机号和平台会员密码不能同时存在";
	public static final String MSG_MOBILE_NOT_NULL = "手机号码不能为空";
	public static final String MSG_PASSWROD_NOT_NULL = "密码不能为空";
	public static final String MSG_PASSWROD_ERROR = "密码错误";
	public static final String MSG_FORMAT_ERROR_EMPLOYEE_ID = "雇员Id格式不正确";
	public static final String MSG_FORMAT_ERROR_AVAILABLESHOPIDS = "适用门店Id格式不正确";
	public static final String MSG_SHOPCOUPON_MISS = "指定的优惠券不存在";
	public static final String MSG_SHOP_COUPON_NAME_NOT_NULL = "优惠券名称不能为空";
	public static final String MSG_SHOP_COUPON_AMOUNT_NOT_NULL = "优惠券面额不能为空";
	public static final String MSG_SHOP_COUPON_TOTAL_NUM_NOT_NULL = "优惠券发行总数不能为空";
	public static final String MSG_BEGIN_DATE_NOT_NULL = "优惠券有效期开始日期不能为空";
	public static final String MSG_END_DATE_NOT_NULL = "优惠券有效期结束日期不能为空";
	public static final String MSG_IS_SHARED_NOT_NULL = "是否允许分享领取不能为空";
	public static final String MSG_MAX_NUMPER_PERSON_NOT_NULL = "每人限领优惠券数不能为空";
	
	public static final String MSG_CATEGORYIDS_CATEGORYID_NOT_ALL_EXIST = "categoryId和categoryIds不能同时传递";
	public static final String MSG_FORMAT_ERROR_ORDERBYSTR = "orderBy数据格式不对";
	public static final String MSG_FORMAT_ERROR_ORDERBYMODESTR = "orderByMode数据格式不对";
	public static final String MSG_FORMAT_ERROR_GOODSGROUPIDSTR = "goodsGroupIdStr数据格式不对";
	public static final String MSG_NOTIFYTYPE_NOT_NULL = "notifyType不能为空";
	public static final String MSG_FORMAT_ERROR_NOTIFYTYPE = "notifyType数据格式不正确";
	public static final String MSG_FORMAT_ERROR_RECEIVERIDSTR = "receiverIdStr数据格式不正确";
	public static final String MSG_RECEIVERIDSTR_NOT_NULL = "receiverIdStr数据格式不正确";
	public static final String MSG_STARTTIME_NOT_NULL = "开始时间不能为空";
	public static final String MSG_FORMAT_ERROR_SEARCHFLAG = "searchFlag数据格式不正确";
	
	public static final String MSG_USERFLAG_NOT_NULL = "userFlag不能为空";
	
	public static final String MSG_SMSCONTENT_ID_NULL = "短信内容不能为空";
	public static final String MSG_SMSTYPE_ID_NULL = "短信账户类型不能为空";
	
	public static final String MSG_FORMAT_ERROR_MEMBERIDS = "memberIds数据格式错误";
	public static final String MSG_FORMAT_ERROR_USERIDS = "userIds数据格式错误";
	
	public static final String MSG_SHOPMEMBERLEVELNAME_NOT_NULL = "等级名称不能为空";
	public static final String MSG_CONSUMEMINAMOUNT_NOT_NULL = "等级消费金额最小值不能为空";
	public static final String MSG_DISCOUNT_NOT_NULL = "折扣比例不能为空";
	public static final String MSG_ISAUTOUPGRATE_NOT_NULL = "是否允许自动升级不能为空";
	
}

