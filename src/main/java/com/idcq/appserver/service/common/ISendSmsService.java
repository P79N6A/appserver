package com.idcq.appserver.service.common;

import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

public interface ISendSmsService {
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
	 * @param isFirst 是否取用优先级高的 true=优先级高 false=优先级低
	 * @return
	 * @throws Exception
	 */
	public String getSmsChannel(boolean isFirst) throws Exception;
	
	
	/**
	 * 发送短信验证码
	 * @param replaceContent 短信发送内容
	 * @return
	 * @throws Exception
	 */
	public boolean sendSmsMobileCode(SmsReplaceContent replaceContent)throws Exception;
	
	/**
	 * 校验短信验证码(已过期，新调用的请使用check开头的方法)
	 * @param mobile
	 * @param usage
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public boolean getSmsCodeIsOk(String mobile,String usage,String code) throws Exception;
	
	/**
	 * 校验短信验证码(已过期，新调用的请使用check开头的方法)
	 * @param mobile
	 * @param usage
	 * @param code
	 * @param delFalg 校验完后是否需要清空缓存
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public boolean getSmsCodeIsOk(String mobile, String usage, String code, boolean delFalg)throws Exception;
	
	/**
	 * 校验短信验证码
	 * @param mobile
	 * @param usage
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public boolean checkSmsCodeIsOk(String mobile,String usage,String code) throws Exception;
	
	/**
	 * 校验短信验证码
	 * @param mobile
	 * @param usage
	 * @param code
	 * @param delFalg 校验完后是否需要清空缓存
	 * @return
	 * @throws Exception
	 */
	public boolean checkSmsCodeIsOk(String mobile, String usage, String code, boolean delFalg)throws Exception;
	
	
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
	
	/**
	 * 校验验证码(已过期，新调用的请使用check开头的方法)
	 * <b>消费代金券专用，因为验证码需要加密判断</b>
	 * @param mobile
	 * @param usage
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public boolean getSmsCodeIsOkByCashCoupon(String mobile,String usage,String code)throws Exception ;
	/**
	 * 校验验证码
	 * <b>消费代金券专用，因为验证码需要加密判断</b>
	 * @param mobile
	 * @param usage
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public boolean checkSmsCodeIsOkByCashCoupon(String mobile,String usage,String code)throws Exception ;
	
	public void insertSmsMobileCode(SmsReplaceContent replaceContent)
			throws Exception;
	
	public  boolean sendSmsByContent(String mobile,String content,String usage);
	
	boolean checkSmsAttack(String mobile,String usage) throws Exception;

	String doContent(String content,SmsReplaceContent replaceContent) throws IllegalArgumentException, IllegalAccessException;

	/**
	 * 处理3721相关的定时短信推送
	 */
	void sendAll3721RemainedMsg();
}
