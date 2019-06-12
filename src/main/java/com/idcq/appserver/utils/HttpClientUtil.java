package com.idcq.appserver.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.idcq.appserver.listeners.ContextInitListener;
public class HttpClientUtil { 
	private static Log logger = LogFactory.getLog(HttpClientUtil.class);
	private static int CONNECTION_TIMEOUT = 3*1000;//请求超时时间，单位毫秒
	private static int SOCKE_TIMEOUT = 3*1000;//等待数据超时时间，单位毫秒
	//private static final long CONN_MANAGER_TIMEOUT = 500L; //该值就是连接不够用的时候等待超时时间
	static{
		Properties properties = ContextInitListener.COMMON_PROPS;
		if (properties != null) {
			String connectionTimout = properties.getProperty("connection_timout");
			CONNECTION_TIMEOUT = (null == connectionTimout?CONNECTION_TIMEOUT:Integer.parseInt(connectionTimout));
			String socketTimout = properties.getProperty("socke_timout");
			SOCKE_TIMEOUT = (null == socketTimout?SOCKE_TIMEOUT:Integer.parseInt(socketTimout));
		}
	}
	public static String sendGet(String url, List<NameValuePair> params)
			throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String body = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			// get请求
			HttpGet get = new HttpGet(url);
			if (null != params) {
				// 设置参数
				String str = EntityUtils.toString(new UrlEncodedFormEntity(
						params,"utf-8"));
				get.setURI(new URI(get.getURI().toString() + "?" + str));
			}
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKE_TIMEOUT).build();
			get.setConfig(config);
			// 发送请求
			response = httpClient.execute(get);
			// 获取返回数据
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity,"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("httpClient-get-UnsupportedEncodingException ", e);
			throw new UnsupportedEncodingException(e.getMessage());
		} catch (SocketException e) {
			logger.error("httpClient-get-SocketException ", e);
			throw new SocketException(e.getMessage());
		} catch (IOException e) {
			logger.error("httpClient-get-IOException ", e);
			throw new IOException(e.getMessage());
		} catch (URISyntaxException e) {
			logger.error("httpClient-get-URISyntaxException ", e);
			throw new URISyntaxException("", e.getMessage());
		} catch (Exception e){
			logger.error("httpClient-get-Exception ", e);
			throw new Exception(e.getMessage());
		} finally{
			if (null != response) {
				response.close();
			}
			if (null != httpClient) {
				httpClient.close();
			}
		}
		return body;
	}

	public static String sendPost(String url, List<NameValuePair> params)
			throws Exception {
		CloseableHttpClient httpClient = null;
		String re = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			// 超时设置
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKE_TIMEOUT).build();
			post.setConfig(config);
			// 设置参数
			post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			// 发送请求
			response = httpClient.execute(post);
			// 获取返回结果
			HttpEntity entity = response.getEntity();
			re = EntityUtils.toString(entity, "utf-8");
			logger.info("返回结果："+re);
		} catch (UnsupportedEncodingException e) {
			logger.error("httpClient-post-UnsupportedEncodingException ", e);
			throw new UnsupportedEncodingException(e.getMessage());
		} catch (ClientProtocolException e) {
			logger.error("httpClient-post-ClientProtocolException ", e);
			throw new ClientProtocolException(e.getMessage());
		} catch (SocketException e) {
			logger.error("httpClient-post-SocketException ", e);
			throw new SocketException(e.getMessage());
		} catch (IOException e) {
			logger.error("httpClient-post-IOException ", e);
			throw new IOException(e.getMessage());
		} catch (Exception e){
			logger.error("httpClient-post-Exception ", e);
			throw new Exception(e.getMessage());
		} finally{
			if (null != response) {
				response.close();
			}
			if (null != httpClient) {
				httpClient.close();
			}
		}
		return re;
	}

	public static int convertXml(String xmlStr) {
		int result = -999;
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			// 获取根节点
			Element root = document.getRootElement();
			// 获取返回结果
			result = Integer.parseInt(root.getTextTrim());
		} catch (DocumentException e) {
			logger.error("解析发送短信返回结果异常",e);
			result = -999;
		} catch (Exception e) {
			logger.error("解析发送短信返回结果-系统异常",e);
			result = -999;
		}
		return result;
	}

	public static boolean analyticResult(String xmlStr) {
		int re = convertXml(xmlStr);
		if (re == -999) {
			logger.error("发送短信返回结果异常");
		} else if (re == -1) {
			logger.error("用户名或密码错误");
		} else if (re == -2) {
			logger.error("发送短信余额不足");
		} else if (re == -6) {
			logger.error("参数有误");
		} else if (re == -7) {
			logger.error("权限受限");
		} else if (re == -8) {
			logger.error("Ip失败");
		} else if (re == -11) {
			logger.error("内部数据库错误");
		} else if (re == 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		String u = "http://ip.taobao.com/service/getIpInfo.php";
		//NameValuePair sn = new BasicNameValuePair("Sn", "SDK-ZQ-SJHY-0817");
		//NameValuePair pwd = new BasicNameValuePair("Pwd", "n871vcz");
		//NameValuePair mobile = new BasicNameValuePair("mobile", "15899773751");
		//NameValuePair content = new BasicNameValuePair("content", "一点传奇测试");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ip", "183.15.83.28"));
		//params.add(sn);
		//params.add(pwd);
		//params.add(mobile);
		//params.add(content);
		// String result = sendGet("http://ip.taobao.com/service/getIpInfo.php",
		// params);
		String result = sendPost(u, params);
		System.out.println(result);
	}
}
