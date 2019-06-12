package com.idcq.appserver.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES对称加密算法帮助类
 * 
 * @author Administrator
 * 
 */
public class AESUtil {
	private static Log logger = LogFactory.getLog(AESUtil.class);
	/**
	 * 加密/解密秘钥
	 */
	public static final String key = "A1B2C3D4F5";

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 * @param radix
	 *            可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * base 64 encode 加码
	 * 
	 * @param bytes
	 * @return
	 */
	public static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * base 64 decode解码
	 * 
	 * @param base64Code
	 * @return
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception {
		return isEmpty(base64Code) ? null : new BASE64Decoder()
				.decodeBuffer(base64Code);
	}

	/**
	 * 是否为空
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isEmpty(String key) {
		return null == key ? true : ((key.trim()).length() == 0 ? true : false);
	}

	/**
	 * 获取byte[]的md5值
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] md5(byte[] bytes) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		return md.digest();
	}

	/**
	 * 获取字符串md5值
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static byte[] md5(String msg) throws Exception {
		return isEmpty(msg) ? null : md5(msg.getBytes());
	}

	/**
	 * 结合base64实现md5加密
	 * 
	 * @param msg
	 *            待加密字符串
	 * @return 获取md5后转为base64
	 * @throws Exception
	 */
	public static String md5Encrypt(String msg) throws Exception {
		return isEmpty(msg) ? null : base64Encode(md5(msg));
	}

	/**
	 * AES加密(不适用于linux)
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes1(String content, String encryptKey)
			throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey()
				.getEncoded(), "AES"));
		return cipher.doFinal(content.getBytes("utf-8"));
	}
	
	/**
	 * AES加密(适用于linux,windows...)
	 * @param content
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey).getEncoded(), "AES"));
		return cipher.doFinal(content.getBytes("utf-8"));
	}

	/**
	 * AES加密为base 64 code
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey)
			throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * AES解密(不适用于linux)
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes1(byte[] encryptBytes,
			String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}
	
	/**
	 * AES解密(适用于linux、windows...)
	 * @param encryptBytes
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes,
			String decryptKey) throws Exception {
		byte[] decryptBytes = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(decryptKey).getEncoded(), "AES"));
			decryptBytes = cipher.doFinal(encryptBytes);
		} catch (Exception e) {
			logger.error("AES解码异常",e);
			return null;
		}
		return new String(decryptBytes);
	}

	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey)
			throws Exception {
		return isEmpty(encryptStr) ? null : aesDecryptByBytes(
				base64Decode(encryptStr), decryptKey);
	}

	/**
	 * 如果服务器系统为linux，则需要用此方法获取key
	 * @param strKey
	 * @return
	 */
	public static SecretKey getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(strKey.getBytes());
			_generator.init(128, secureRandom);
			return _generator.generateKey();
		} catch (Exception e) {
			throw new RuntimeException(" 初始化密钥出现异常 ");
		}
	}

	public static void main(String[] args) throws Exception {
		//String content = "n871vcz";
		//System.out.println("加密前：" + content);
		//System.out.println("加密密钥跟解密密钥:" + key);
		//String encrypt = aesEncrypt(content, key);
		//System.out.println("加密后：" + encrypt);
		//String dencrypt = aesDecrypt(encrypt, key);
		//System.out.println("解密后：" + dencrypt);
		
		//SDK-WSS-010-07020
		//8@8c8b@4
		//http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend
		//System.out.println(aesEncrypt("SDK-WSS-010-07020", key));
		//System.out.println(aesEncrypt("8@8c8b@4", key));
		String a = aesEncrypt("http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend", key);
	    System.out.println(aesEncrypt("http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend", key));
		System.out.println(aesDecrypt(a, key));
	}

}
