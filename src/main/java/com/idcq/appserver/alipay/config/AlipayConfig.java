package com.idcq.appserver.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088511218623964";
	public static String appId = "2016072201651302";
	// 收款支付宝账号
	public static String seller_email = "pay@1dcq.com";
	// 商户的私钥
	public static String key = "ae4hxihdfksb6uq6t9f2lc57671ndz2m";
	// 支付宝的公钥，无需修改该值
										   
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static String ali_private_key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJVbbL0MCXZYKLqdAOziy5CKdKc+ZLTHGGmTGFguZua3Phc9fV1UsqQlXaKNUEEMBTMnzUXBIDei7ZhAQDK4S5azj2TmRix1caiYPHN2TLxFzCzryDfS0ryDiGu36ejAtctPZsNyrQvhsEEtN3fPhU+tb6ktAYD4A+w3I/0BVNjBAgMBAAECgYEAhEfg2t6aUtJp4D3F4AhOOsol3yoHj4T2PislWJRgVt7cg0DWk568SV0+vQVjmvjGcjRCPhF6M0x+GmZtdX58krY+sswerVzHEjRr1LitseTc+ZQmMO/LEm8DXskDjpyWHP3RlATqUbDm1v51AOYT3q0rfJ5JD72PKjYEh5giV0ECQQDEDarC2jLFZ/IHi12bdYONSgcN47WwwJIpsiDuIr4lbFOhNsTFOMGyHHWQdBySfaPTq18xor1nXfnJ73vSoZ8lAkEAwwaMK0iUUgFa/SmA0JSGNbeXfhlFJ4Es5liRnkfY6CYoyWE5lxL73BgS7rEMDGfi43FLTBdyZrfxT1fVgdrebQJBALSBsNs+csh2b8yqcEEV7U1E/0G2ii523xwsuU0IB/IEzur7tievngcTVNrvTO9DZncYrcjRgXmOeNGIAYVo8VECQQCXCkccboWQTURutOnyoFSg3aMSIfgQ9FYVv5pnfzVZ9dO4wvebLTSp7GsdOTkfkoWBqIisUaxkKSlWTawpeZyxAkBpbNIvnmBpHIuXtiVv4zl1OGkDkxyJWsRD4RzS9eRifSdunec67/IpYWLMNJM08ZzN+DCM4a9UJkvpz+Ce8ZpZ";
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static String our_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALJ0kf1gIIsrCy9uqSaHqzd/DY8ojlzmxW/JRXTsH8D5UfDvBiFS04tlq4FIacSRRIN8ctR6RgxXgEqaFaLqVTiA7RNsZJmvd/9M/wY0GsRrTt3hgR1h7yAbEzjj1FZyCwdjJizbskl6AdxTk1TLEwatV47AcZnGBqgskHh+D+6HAgMBAAECgYB7k5uxEQsYNEN3ojMCWnqnYJU8KIKFNM1OPtuZPxYyJLSommztUJTreAlO6p1LoKkIQHqoRtARJdrSTmdjPNCruvXdI3hzv68NRakHD2wNMZIOHquMNoUv48gx1f1CZJLqwCin2FYRIfuNjN8gzV2gvMC4dDGPo349JoIXcDpNuQJBANzGQVTBfM0CSm+3Q42RwDicxmbSfRvK58ml3HxtH6Ff6HoVJGTLkvsYZXWR0H2sAMl4RF4qHO8o9l6PELxoEIsCQQDO7cBYIEEmKDfOxqL2krFTCgA8FlE+sXoAKgoKWFZDkkbHm7J2stqHhwFjS8rG1jt7YjbVRiydS76VLYukVf11AkEAwv9FIvRHI9gbIPNQclFC433ta1IM+KhN8/hQd4H2xxUsA/B6laCNMMTKkH57FRUAGPLmB/nQVpjqinh4gsCzowJAOsgtIJuhBi7ck5+3wWWwe89We7s3PyD5cDyA96lab/2In8Fp8/AgxekjCssIAm3vHcCrpPA3UkOnEweKKJfI1QJAL9eY4A1AQ64xu1OlLBJSYvWC1TVqnIG2uGdIa9sGm5PjRQwqg08eHEOX+RMFC/Yo60So3FISLwr70Nz8SQ7gTQ==";
	
	public static String our_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCydJH9YCCLKwsvbqkmh6s3fw2PKI5c5sVvyUV07B/A+VHw7wYhUtOLZauBSGnEkUSDfHLUekYMV4BKmhWi6lU4gO0TbGSZr3f/TP8GNBrEa07d4YEdYe8gGxM449RWcgsHYyYs27JJegHcU5NUyxMGrVeOwHGZxgaoLJB4fg/uhwIDAQAB";
	
	public static String ali_open_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/usr/alipayLog";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type_MD5 = "MD5";
	public static String sign_type_RSA = "RSA";

}
