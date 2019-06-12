package com.idcq.appserver.service.common;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.idcq.appserver.dto.common.CodeDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.common.PageResult;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.dto.common.SmsDto;
import com.idcq.appserver.dto.common.SysConfigureDto;
import com.idcq.appserver.dto.common.UserPermission;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.rebates.ShopRebatesDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

public interface ICommonService {
	/**
	 * 获取手机短信下发参数 appId固定为99999
	 * @param appId
	 * @return
	 */
	public List<Map> querySmsParam(Long appId) throws Exception;
	
	 public Map<String, Object> getConfigByGroup(String configGroup);
	/**
	 * 向用户推送信息
	 * @param jsonObject 推送的消息体
	 * @param userId 用户id
	 * @param userType 用户角色  会员-0,店铺管理者-10 默认为0
	 * @throws Exception
	 */
	public void pushUserMsg(JSONObject jsonObject, Long userId, Integer userType)throws Exception;
	public void pushUserMsg(String action,Long userId, Integer userType,SmsReplaceContent pushReplaceContent) throws Exception;
   /**
    * 插入待推送信息（未发送）
    * 
    * @param action
    * @param bizId
    * @param bizType
    * @param pushReplaceContent
    * @throws Exception
    *
    */
	public void insertUserMsg(String action,Long bizId, Integer bizType,SmsReplaceContent pushReplaceContent) throws Exception;
	
	/**
	 * 
	 * @param action 推送场景标识
	 * @param content 推送内容
	 * @param userId 用户编号
	 * @param userType 用户角色 会员-0,店铺管理者-10 默认为0
	 * @throws Exception
	 */
	public void pushUserMsg(String action,String content,Long userId,Integer userType)throws Exception;
	
	/**
	 * 根据传入的key获取短信验证码所需的value：<br/>先从redis中取，如果不存在，则从数据库中取，并将查询出的value值存入redis中
	 * @param key
	 * @param isAesDecrypt 是否解码
	 * @return
	 */
	public String getSmsValueByKey(String key,boolean isAesDecrypt);
	
	/**
	 * 获取1dcq_setting表中的配置value信息
	 * @param code
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int getSettingValue(String code,String key)throws Exception;
	
	/**
	 * 发送短信验证码
	 * @param mobile 手机号码
	 * @param code 验证码
	 * @param usage 查询短信内容关键字
	 * @return
	 * @throws Exception
	 */
	public boolean sendSmsMobileCode(String mobile,String code,String usage)throws Exception;
	
	/**
	 * 短信支付短信发送
	 * @param mobile 手机号码
	 * @param acountAmount 账户余额
	 * @param amount 消费金额
	 * @param shopName 商铺名称
	 * @param code 验证码
	 * @param usage 查询短信内容关键字
	 * @return
	 * @throws Exception
	 */
	public boolean sendSmsMobileCode(String mobile,Double acountAmount,Double amount,String shopName,String code,String usage)throws Exception;
	
	/**
	 * php后台发送经销商、代理商账号密码
	 * @param userName 用户名称
	 * @param mobile 手机号码
	 * @param code 发送的密码
	 * @param usage 获取短信内容的关键字
	 * @return
	 * @throws Exception
	 */
	public boolean sendSmsMobileCode(String userName,String mobile,String code,String usage)throws Exception;
	
	/**
	 * 从setting中获取关于我们等信息
	 * @param code
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Map getSettingValueByKey(String key) throws Exception;
	
	/**
	 * 保存短信发送记录
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public int saveSendSmsRecord(SendSmsRecordDto dto);
	
	/**
	 * 发送短信
	 * @Title: sendSms 
	 * @param @param mobile
	 * @param @param content
	 * @param @return
	 * @return boolean    返回类型 
	 * @throws
	 */
	public  boolean sendSms(String mobile,String content);
	
	
	public boolean sendSmsBatch(String mobile,String content) throws Exception;
	
	
	/**
	 * <h3>获取发送短信的通道</h3>
	 * <table>
	 * 	<tr>
	 * 		<td>md</td>
	 * 		<td>漫道科技通道</td>
	 * </tr>
	 * 	<tr>
	 * 		<td>zq</td>
	 * 		<td>志晴科技通道</td>
	 * </tr>
	 * 	<tr>
	 * 		<td>...</td>
	 * 		<td>...</td>
	 * </tr>
	 * </table>
	 * @return
	 * @throws Exception
	 */
	public String getSmsChannel() throws Exception;
	
	/**
	 * 根据配置key查询系统配置信息
	 * @param key 
	 * @return
	 * @throws Exception
	 */
	public SysConfigureDto getSysConfigureDtoByKey(String key) throws Exception;
	/**
	 * 推送信息到一点管家
	 * 
	 * @Function: com.idcq.appserver.service.common.ICommonService.pushShopUserMsg
	 * @Description:
	 *
	 * @param action
	 * @param content 推送内容
	 * @param shopId
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月6日 上午11:16:22
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月6日    ChenYongxin      v1.0.0         create
	 */
	public void pushShopUserMsg(String action,String content,Long shopId)throws Exception;


	/**
	 * 查询短信登陆
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean login(String userName, String password) throws Exception;


	/**
	 * 查询短信列表
	 * @param mobile
	 * @return
	 */
	public List<SendSmsRecordDto> getSmsListByMobile(String mobile) throws Exception;


	/**
	 * 写查询日志
	 * @param userName
	 * @param mobile
	 * @throws Exception
	 */
	public void writeQueryLog(String userName, String mobile) throws Exception;
	
	/**
	 * 短信群发，获取手机号码
	 * @return
	 * @throws Exception
	 */
	public Map getMobileInfo() throws Exception;
	
	/**
	 * 更新手机号码发送状态
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public int updateStatus(String mobile) throws Exception;
    /**
     * 查询心跳
     * @param heartbeatLogDto
     * @return
     * @throws Exception
     */
    void heartbeat(Long shopId,Integer systemType) throws Exception;
    /**
     *  获取客户端是否在线
     * @param shopId
     * @param systemType 系统类型 1=收银机2=管家
     * @param intervalTimes 心跳间隔次数。超过间隔次数无心跳的，视为离线。
     * @throws Exception
     */
    Map<String, Object> getClientOnlineStatus(Long shopId,Integer systemType,Integer intervalTimes) throws Exception;
    List<Map<String, Object>> getUserAuthList(UserPermission userPermission) throws Exception;
    /**
     * 根据配置项key串查询配置项值
     * @param configureKeys key值，多个逗号分隔
     * @return List key-value 列表
     * @author  shengzhipeng
     */
    List<Map> getSysConfiguresByKeys(Map<String, Object> requestMap) throws Exception;
    /**
     * 调用此接口获取code。同一codeType的code按次序号排序。
     * @param codeType
     * @return
     * @throws Exception
     */
    List<CodeDto> getCodeByType(String codeType) throws Exception;
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param mobile
     * @param clientType
     * @param action
     * @param data
     * @author  shengzhipeng
     * @date  2016年3月5日
     */
    void launchPush(String mobile, String clientType, String action, String data)throws Exception;
    
    /**
     * 获取消息通知列表
     * @param messageCenterDto
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageModel getMessageList(MessageCenterDto messageCenterDto,int pageNo,int pageSize) throws Exception;
    
    /**
     * 商圈操作回馈
     * 统计商圈活动分享次数
     * @param paramMap
     * @return
     * @throws Exception
     */
    String BusAreaOperateFeedback(Map<String,Object> paramMap) throws Exception; 
    
    /**
     * 消息操作回馈
     * @param paramMap
     * @return
     * @throws Exception
     */
    String MsgOperateFeedback(Map<String,Object> paramMap) throws Exception; 
    
    /**
     * 查询配置接口，会返回符合条件的所有配置
     * @param configQueryCondition
     * @param page
     * @return
     */
    PageResult<ConfigDto> queryForConfig(ConfigQueryCondition configQueryCondition);
    
    /**
     * 更新/添加指定商铺配置，如果其中有一个配置不合法，则全部不添加
     * @param shopId
     * @param configs
     */
    void updateConfigForShop(Long shopId, List<ConfigDto> configs);
    
    /**
     * 根据指定条件获取{@link ConfigDto},注定，仅凭configKey已无法完全确定一个配置，必须同时有config,bizId,bizType全可确定唯一的配置
     * @param configKey
     * @return
     */
    ConfigDto getConfigDto(ConfigDto searchCondition);
    
    /**
     * 根据指定条件获取{@link ConfigDto},注定，仅凭configKey已无法完全确定一个配置，必须同时有config,bizId,bizType全可确定唯一的配置
     * @param configKey
     * @return
     */
    List<ConfigDto> queryConfigDto(ConfigDto searchCondition);
    /**
     * 该方法会删除商铺已有的配置，重新生成商铺的初始化配置
     * @param shopId
     */
    void initShopConfig(Long shopId);
    /**
     * 客户端获取emqtt初始化信息，包括url,userName,password
     * @return
     */
    Map<String, Object> getAllMqttInitInfos();
    
    /**
     * 获得用户相关的mqtt信息主要包括topic
     * @return
     */
    Map<String, Object> getMqttInitInfoForClient(String clientType, Integer bizType, Integer bizId);
    
    int deleteConfigs(ConfigQueryCondition searchCondition);


	public void sendSmsByEntity(SmsDto smsDto) throws NumberFormatException, Exception;
	/**
	 *               
     * PC28代理商信息变动通知接口
	 * 
	 * @Function: com.idcq.appserver.service.common.ICommonService.agentInfoChange
	 * @Description:
	 *
	 * @param agentId
	 * @param userId
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月9日 下午4:59:55
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月9日    ChenYongxin      v1.0.0         create
	 */
	public Double agentInfoChange(Long agentId,Long userId, String orderId) throws Exception;


	/**
	 * 在店铺通过审核后，校验是否需要进行相关返现操作，如果是，则直接进行相关操作
	 * @param shopDto
     */
	void checkForRebateForShopRegister(ShopDto shopDto) throws Exception;

	/**
	 * 进行店铺购买V商品店铺自身的返点工作，会更新shopAccount,插入shopBill、platformbill，但是不会处理shop_rebates表信息
	 * @param shopRebatesDto
     */
	void shopBuyvMoneyRebates(ShopRebatesDto shopRebatesDto, boolean insert, Long principalId, Map configMap) throws Exception;
	
	void agentRebate(Long agentId, Long userId, Double slottingFee, Double dRebatesRatioByYear,
	            Double dRebatesDayMoney) throws Exception;
}
