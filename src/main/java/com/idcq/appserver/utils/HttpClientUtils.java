package com.idcq.appserver.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.collections.CollectionUtils;
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
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.idcq.appserver.common.CommonConst;
class AnyTrustStrategy implements TrustStrategy{
	public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		return true;
	}
}
/**
 * HttpClient 连接帮助类
 * @author nie_jq
 *
 */
public class HttpClientUtils {
	private static final Log logger= LogFactory.getLog(HttpClientUtils.class);
	private static volatile HttpClientUtils instance;
	public static CloseableHttpClient httpClient = null;
	private static PoolingHttpClientConnectionManager connManager = null;
	private static RequestConfig config;
	private ConnectionConfig connConfig;
	private SocketConfig socketConfig;
	private ConnectionSocketFactory plainSF;
	private KeyStore trustStore;
	private SSLContext sslContext;
	private LayeredConnectionSocketFactory sslSF;
	private Registry<ConnectionSocketFactory> registry;
	public static String defaultEncoding= "utf-8";
	private volatile BasicCookieStore cookieStore;	
	private static int bufferSize= 1024;
	private static int SOCKET_TOMEOUT=5000;//socket超时时间，单位毫秒
	private static int CONNECTION_TIMEOUT = 5000;//连接超时时间，单位毫秒
	private static int MAX_TOTAL=400;//整个连接池最大连接数
	private static int DF_MAX_ROUTER=200;//单个路由最大并发连接数
	private HttpClientUtils() {
		// 设置连接参数
		connConfig = ConnectionConfig.custom().setCharset(Charset.forName(defaultEncoding)).build();
		socketConfig = SocketConfig.custom().setSoTimeout(SOCKET_TOMEOUT).build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
		plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		// 指定信任密钥存储对象和连接套接字工厂
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
			sslSF = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		registry = registryBuilder.build();
		// 设置连接管理器
		connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		connManager.setMaxTotal(MAX_TOTAL);
		connManager.setDefaultMaxPerRoute(DF_MAX_ROUTER);
		// 指定cookie存储对象
		cookieStore = new BasicCookieStore();
		// 构建客户端
		httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setConnectionManager(connManager).build();
		config = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TOMEOUT).build();
	}

	public static HttpClientUtils getInstance() {
		synchronized (HttpClientUtils.class) {
			if (instance == null) {
				instance = new HttpClientUtils();
			}
			return instance;
		}
	}
	
	/**
	 * 参数设置
	 * @param params
	 * @return
	 */
	public static List<NameValuePair> paramsConverter(Map<String, String> params){
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Entry<String, String>> paramsSet= params.entrySet();
		for (Entry<String, String> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
		}
		return nvps;
	}
	
	/**
	 * 读取响应内容
	 * @param in
	 * @param encoding 编码
	 * @return
	 */
	public static String readStream(InputStream in, String encoding){
		if (in == null){
			return null;
		}
		try {
			InputStreamReader inReader= null;
			if (encoding == null){
				inReader= new InputStreamReader(in, defaultEncoding);
			}else{
				inReader= new InputStreamReader(in, encoding);
			}
			char[] buffer= new char[bufferSize];
			int readLen= 0;
			StringBuffer sb= new StringBuffer();
			while((readLen= inReader.read(buffer))!=-1){
				sb.append(buffer, 0, readLen);
			}
			inReader.close();
			return sb.toString();
		} catch (IOException e) {
			logger.error("读取返回内容出错", e);
		}
		return null;
	}
	
	/* **************************httpClient httpGet request**************************************** */
	/**
	 * 基本的Get请求
	 * @param url 请求url
	 * @param queryParams 请求头的查询参数
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String doGet(String url, Map<String, String> queryParams)throws Exception{
		URIBuilder builder=null;
		CloseableHttpResponse response = null;
		HttpGet get = null;
		String result = "";
		try {
			get = new HttpGet();
			builder = new URIBuilder(url);
			//填入查询参数
			if (queryParams!=null && !queryParams.isEmpty()){
				builder.setParameters(HttpClientUtils.paramsConverter(queryParams));
			}
			get.setConfig(config);
			get.setURI(builder.build());
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			logger.info("httpClient-doGet-response:"+result);
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
			//释放资源
			if (null != get) {
				get.releaseConnection();
			}
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/* **************************httpClient httpPost request**************************************** */
	/**
	 * 基本的Post请求
	 * @param url 请求url
	 * @param formParams post表单的参数
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String doPost(String url, Map<String, String> formParams)throws Exception {
		CloseableHttpResponse response = null;
		String result = null;
		HttpPost post = null;
		//填入表单参数
		try {
			post = new HttpPost(url);
			if (formParams!=null && !formParams.isEmpty()){
				post.setEntity(new UrlEncodedFormEntity(HttpClientUtils.paramsConverter(formParams),defaultEncoding));
			}
			post.setConfig(config);
			response = httpClient.execute(post);
			// 获取返回结果
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
			logger.info("httpClient-doPost-response:"+result);
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
		}finally{
			//释放资源
			if (null != post) {
				post.releaseConnection();
			}
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 解析返回结果
	 * @param xmlStr
	 * @return
	 */
	public int convertXml(String xmlStr) {
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
	
	 /**
     * 解析返回结果
     * <br/>
     * 创蓝返回结果解析
     * @param xmlStr
     * @return
     */
    public Map<String, Object> convertStr_cl(String str) {
        Map<String, Object> map = new HashMap<String, Object>();
        int result = -999;
        String msgId = null;
        //根据换行符分隔
        String[] results =  str.split("\n");
        if (null != results && results.length >= 1)
        {
            String[] codes = results[0].split(CommonConst.COMMA_SEPARATOR);
            if (null != codes && codes.length == 2)
            {
                result = Integer.valueOf(codes[1]);
            }
            //20160614
            if(codes.length >= 2) 
            {
                msgId = codes[1];
            }
        }
        map.put("sendStatus", result);
        map.put("msgId", msgId);
        return map;
    }

	/**
	 * 解析返回结果
	 * <br/>
	 * 漫道科技返回结果解析
	 * @param xmlStr
	 * @return
	 */
	public int convertXml_md(String xmlStr) {
	    //for test
	    System.out.println(xmlStr);
		int result = -999;
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			// 获取根节点
			Element root = document.getRootElement();
			// 获取返回结果
			long re = Long.parseLong(root.getTextTrim());
			if (re > 0) {
				result= 0;
			}else{
				result= (int) re;
			}
		} catch (DocumentException e) {
			logger.error("解析发送短信返回结果异常",e);
			result = -999;
		} catch (Exception e) {
			logger.error("解析发送短信返回结果-系统异常",e);
			result = -999;
		}
		return result;
	}
	

	/**
	 * 对解析结果进行校验
	 * @param xmlStr
	 * @param channelKey 通道key
	 * @return
	 */
	public Map analyticResult(String xmlStr,String channelKey) {
	    Map<String, Object> map = new HashMap<String, Object>();
		int re = -999;
		String infoMsg = "";
		if((CommonConst.SMS_MD_CHANNEL_KEY).equals(channelKey)){
			re = convertXml_md(xmlStr);
			err_log_md(re);
			map.put("sendStatus", re);
		} 
		else if((CommonConst.SMS_CL_CHANNEL_KEY).equals(channelKey))
		{
		    map = convertStr_cl(xmlStr);
		    re = (int) map.get("sendStatus");
		    infoMsg = err_log_cl(re);
		}
		else
		{
			re = convertXml(xmlStr);
			err_log_zq(re);
			map.put("sendStatus", re);
		}
		map.put("infoMsg", infoMsg);
		return map;
	}
	
	public String err_log_cl(int re)
	{
	    String msg = null;
	    switch (re)
        {
            case 0:logger.info("发送成功");
            msg="发送成功";
            break;
            case 101:logger.error("无此用户");
            msg="无此用户";
                break;
            case 102:logger.error("密码错误");
            msg="密码错误";
                break;
            case 103:logger.error("提交过快（提交速度超过流速限制）");
            msg = "提交过快（提交速度超过流速限制）";
                break;
            case 104:logger.error("系统忙（因平台侧原因，暂时无法处理提交的短信）");
            msg = "系统忙（因平台侧原因，暂时无法处理提交的短信）";
                break;
            case 105:logger.error("敏感短信（短信内容包含敏感词）");
            msg = "敏感短信（短信内容包含敏感词）";
                break;
            case 106:logger.error("消息长度错（>536或<=0）");
            msg = "消息长度错（>536或<=0）";
                break;
            case 107:logger.error("包含错误的手机号码");
            msg = "包含错误的手机号码";
                break;
            case 108:logger.error("手机号码个数错（群发>50000或<=0;单发>200或<=0）");
            msg = "手机号码个数错（群发>50000或<=0;单发>200或<=0）";
                break;
            case 109:logger.error("手机号码个数错（群发>50000或<=0;单发>200或<=0）");
            msg = "手机号码个数错（群发>50000或<=0;单发>200或<=0）";
                break;
            case 110:logger.error("不在发送时间内");
            msg = "不在发送时间内";
                break;
            case 111:logger.error("超出该账户当月发送额度限制");
            msg = "超出该账户当月发送额度限制";
                break;
            case 112:logger.error("超出该账户当月发送额度限制");
            msg = "超出该账户当月发送额度限制";
                break;    
            case 113:logger.error("extno格式错（非数字或者长度不对）");
            msg = "extno格式错（非数字或者长度不对）";
                break;
            case 114:logger.error("自动审核驳回");
            msg = "自动审核驳回";
                break;
            case 115:logger.error("签名不合法，未带签名（用户必须带签名的前提下）");
            msg = "签名不合法，未带签名（用户必须带签名的前提下）";
                break;
            case 116:logger.error("IP地址认证错,请求调用的IP地址不是系统登记的IP地址");
            msg = "IP地址认证错,请求调用的IP地址不是系统登记的IP地址";
                break;
            case 117:logger.error("用户没有相应的发送权限");
            msg = "用户没有相应的发送权限";
                break;
            case 118:logger.error("用户已过期");
            msg = "用户已过期";
                break;
            case 119:logger.error("无此用户");
            msg = "无此用户";
                break;
            default: logger.error("程序出异常了");
            msg = "程序出异常了";
                break;
        }
	    return msg;
	}
	
	public void err_log_md(int re){
		if (re == -999) {
			logger.error("发送短信返回结果异常");
		} else if (re == -2) {
			logger.error("帐号/密码不正确");
		} else if (re == -4) {
			logger.error("余额不足支持本次发送");
		} else if (re == -5) {
			logger.error("数据格式错误");
		} else if (re == -6) {
			logger.error("参数有误");
		} else if (re == -7) {
			logger.error("权限受限");
		} else if (re == -8) {
			logger.error("流量控制错误");
		} else if (re == -9) {
			logger.error("扩展码权限错误");
		} else if (re == -10) {
			logger.error("内容长度长");
		} else if (re == -11) {
			logger.error("内部数据库错误");
		} else if (re == -12) {
			logger.error("序列号状态错误");
		} else if (re == -14) {
			logger.error("服务器写文件失败");
		} else if (re == -17) {
			logger.error("没有权限");
		} else if (re == -19) {
			logger.error("禁止同时使用多个接口地址");
		} else if (re == -20) {
			logger.error("相同手机号，相同内容重复提交");
		} else if (re == -22) {
			logger.error("Ip鉴权失败");
		} else if (re == -23) {
			logger.error("缓存无此序列号信息");
		} else if (re == -601) {
			logger.error("序列号为空，参数错误");
		} else if (re == -602) {
			logger.error("序列号格式错误，参数错误");
		} else if (re == -603) {
			logger.error("密码为空，参数错误");
		} else if (re == -604) {
			logger.error("手机号码为空，参数错误");
		} else if (re == -605) {
			logger.error("内容为空，参数错误");
		} else if (re == -606) {
			logger.error("ext长度大于9，参数错误");
		} else if (re == -607) {
			logger.error("参数错误 扩展码非数字");
		} else if (re == -608) {
			logger.error("参数错误 定时时间非日期格式");
		} else if (re == -609) {
			logger.error("rrid长度大于18,参数错误");
		} else if (re == -610) {
			logger.error("参数错误 rrid非数字");
		} else if (re == -611) {
			logger.error("参数错误 内容编码不符合规范");
		} else if (re == -623) {
			logger.error("手机个数与内容个数不匹配");
		} else if (re == -624) {
			logger.error("扩展个数与手机个数不匹配");
		}
	}
	public void err_log_zq(int re){
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
		}
	}
	
	public boolean isSendSuccess(int re){
		if (re == 0) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		//根据Ip地址获取位置信息
		String url = "http://ip.taobao.com/service/getIpInfo.php";
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", "183.15.83.28");
		url = "http://192.168.1.121:9101/appServer/interface/common/checkVeriCode?mobile=15899773751&veriCode=15415";
		params = null;
		HttpClientUtils httpUtil = HttpClientUtils.getInstance();
		String result = null;
		try {
			result=httpUtil.doPost(url, params);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}
}
