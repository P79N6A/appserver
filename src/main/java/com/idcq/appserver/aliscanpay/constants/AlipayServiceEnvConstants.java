

/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.idcq.appserver.aliscanpay.constants;

/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝服务窗获取*/							
    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET      = "GBK";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET           = "GBK";
    /**签名类型-视支付宝服务窗要求*/
    public static final String SIGN_TYPE         = "RSA";
    
    
    public static final String PARTNER           = "2088511218623964";
	// 商户收款账号
	public static final String SELLER            = "pay@1dcq.com";

	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJVbbL0MCXZYKLqdAOziy5CKdKc+ZLTHGGmTGFguZua3Phc9fV1UsqQlXaKNUEEMBTMnzUXBIDei7ZhAQDK4S5azj2TmRix1caiYPHN2TLxFzCzryDfS0ryDiGu36ejAtctPZsNyrQvhsEEtN3fPhU+tb6ktAYD4A+w3I/0BVNjBAgMBAAECgYEAhEfg2t6aUtJp4D3F4AhOOsol3yoHj4T2PislWJRgVt7cg0DWk568SV0+vQVjmvjGcjRCPhF6M0x+GmZtdX58krY+sswerVzHEjRr1LitseTc+ZQmMO/LEm8DXskDjpyWHP3RlATqUbDm1v51AOYT3q0rfJ5JD72PKjYEh5giV0ECQQDEDarC2jLFZ/IHi12bdYONSgcN47WwwJIpsiDuIr4lbFOhNsTFOMGyHHWQdBySfaPTq18xor1nXfnJ73vSoZ8lAkEAwwaMK0iUUgFa/SmA0JSGNbeXfhlFJ4Es5liRnkfY6CYoyWE5lxL73BgS7rEMDGfi43FLTBdyZrfxT1fVgdrebQJBALSBsNs+csh2b8yqcEEV7U1E/0G2ii523xwsuU0IB/IEzur7tievngcTVNrvTO9DZncYrcjRgXmOeNGIAYVo8VECQQCXCkccboWQTURutOnyoFSg3aMSIfgQ9FYVv5pnfzVZ9dO4wvebLTSp7GsdOTkfkoWBqIisUaxkKSlWTawpeZyxAkBpbNIvnmBpHIuXtiVv4zl1OGkDkxyJWsRD4RzS9eRifSdunec67/IpYWLMNJM08ZzN+DCM4a9UJkvpz+Ce8ZpZ";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    /** 服务窗appId  */
    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id
    public static final String APP_ID            = " 2015111100758933";

    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南
    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
    //public static final String PRIVATE_KEY       = "ae4hxihdfksb6uq6t9f2lc57671ndz2m";
    
    
    public static final String PRIVATE_KEY       = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMhhDhmNCO"
    		+ "GjQyoCShheeM6Dow0kO0u3esappyf9i3NuJf5cffroHDY0cFke1SHIxu3cEiV5gqgGBpCXBExC21OlFLKUZdAYwLaR2NeZz"
    		+ "LIoVyAGkDZ+aSSqKMzxKBgOje1Frlq5kcAsnenXnm+hZM4MhRZY5pISU7I5cI1M19x3AgMBAAECgYBI31MbY7kpJhDVBEQi"
    		+ "HKleoWz46IZQAQg7m5NY7dJ7RkG3AbgnOaaY5/U71AS65qPB+Vj4G/qyVI6qouFDQsU0P4XBK3DmlXI3CZKc4cPo+5rXX/"
    		+ "olr8JAy/greVx5D32g/fC6LQKzIGOy23KcPmCjOf93yKAoApqJ6J1Ml99ukQJBAON9R67JOLcYO1kFAw9+6ohb6A6Uz+qL"
    		+ "ZllgcRsFcpwQtTq5UCzbWUZIVvRLgnghhEkgEiBusJzcXuH7BtU5vasCQQDhffm9I5Vbw3UaFzFRC8eAUaE+JajDr4Ikbv/"
    		+ "8Ha3u1y4Ubwkn+XIPAxzgIPv5HMZ6/jpVIDTHb2oqHogx2BhlAkEA4qVYm61i0hhUWJaDvBaIeB6JDq34kAcei4FobrF4x"
    		+ "EBpLv6eAHLxePJtn6rza9iIwYowRRvC3iU4axD8d8e1TQJBALnte7ze2EqfsbDfqsRe6BMAO+nYd31S/AY6mEwz1LT5LH+g"
    		+ "xnX1knyaXnbX+6v+dBH6CfMi6SIA1hMAteAThJkCQBq4OnQu34EK36WDsdVtaGM4z5e+u31LOaw+2KiYJObjcsAmZ5Z6GjX"
    		+ "n43VZWkTdt94lZ34x9oEzQ0v4rZKAW9Q=";

    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
    public static final String PUBLIC_KEY        = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIYQ4ZjQjho0MqAkoYXnjOg6MNJDtLt3rGqacn/YtzbiX+XH366Bw2NHBZHtUhyMbt3BIleYKoBgaQlwRMQttTpRSylGXQGMC2kdjXmcyyKFcgBpA2fmkkqijM8SgYDo3tRa5auZHALJ3p155voWTODIUWWOaSElOyOXCNTNfcdwIDAQAB";
    /**支付宝网关*/
    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

    /**授权访问令牌的授权类型*/
    public static final String GRANT_TYPE        = "authorization_code";
    
    public static void main(String[] args) {
		System.out.println(ALIPAY_PUBLIC_KEY.equals("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB"));
	}
}