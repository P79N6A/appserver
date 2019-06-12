package com.idcq.appserver.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIHttpClient {

	public static String get(String url)throws Exception{
		return get(url,"");
	}
	public static String get(String url,String cookie)throws Exception{
		HttpURLConnection conn = getConnection(url,cookie);
		InputStream in = conn.getInputStream();
		String encoding = getContentEncoding(conn);
		String content = getContent(in,encoding);
		in.close();
		return content;
	}
	
	public static String post(String strURL, String params)throws Exception {
		return post(strURL, params, null);
	}
	/**
	 * 发送HttpPost请求
	 * 
	 * @param strURL
	 *            服务地址
	 * @param params
	 *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
	 * @return 成功:返回json字符串<br/>
	 */
	public static String post(String strURL, String params, String cookie) {
		try {
			HttpURLConnection connection = getConnection(strURL, cookie);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.setRequestProperty("Content-Length",
					"" + params.length());
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.flush();
			out.close();
			// 读取响应
			InputStream is = connection.getInputStream();
			byte[] data = new byte[params.length()];
			byte[] temp = new byte[512];
			int readLen = 0;
			int destPos = 0;
			while ((readLen = is.read(temp)) > 0) {
				System.arraycopy(temp, 0, data, destPos, readLen);
				destPos += readLen;
			}
			String result = new String(data, "UTF-8"); // utf-8编码
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}
	
	/**
	 * 建立连接
	 * @param urlstr
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	private static HttpURLConnection getConnection(String urlstr,String cookie)throws Exception{
		HttpURLConnection conn = getHttpConnection(urlstr);
		if(cookie != null && cookie.length() > 0){
			conn.setRequestProperty("Cookie", cookie);
		}
		return conn;
	}
	
	private static HttpURLConnection getHttpConnection(String urlstr)throws Exception{
		URL url = new URL(urlstr);
		return (HttpURLConnection) url.openConnection();
	}
	
	public static String getContent(InputStream in)throws Exception{
		return getContent(in,"UTF-8");
	}
	public static String getContent(InputStream in,String encoding)throws Exception{
		if(encoding == null || encoding.equals("")){
			encoding = "UTF-8";
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buff = new byte[1024*1024];
		int len = 0;
		while((len = in.read(buff)) != -1){
			bout.write(buff,0,len);
		}
		bout.close();
		return new String(bout.toByteArray(),encoding);
	}
	public static String getContentEncoding(HttpURLConnection conn){
		if(conn == null){
			return "";
		}
		String contentEncoding = conn.getContentEncoding();
		if(contentEncoding != null && !contentEncoding.equals("")){
			return contentEncoding;
		}
		String contentType = conn.getContentType();
		if(contentType == null || contentType.equals("")){
			return "";
		}
		Matcher m = Pattern.compile("(?i)charset\\s*=\\s*([-\\w]+)").matcher(contentType);
		if(m.find()){
			return m.group(1);
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
//		String url = "http://192.168.1.121:8080/appServer/interface/user/modifyBaseInfo";
//		String parameters = "{\"userId\":1179341046,\"imgBig\":\"001.png\",\"imgSmall\":\"001_small.png\",\"nikeName\":\"贪吃羊\",\"sex\":0,\"provinceId\":1,\"cityId\":1}";
//		String body = post(url, parameters);
	//	System.out.println(get("http://localhost:8080/appServer/interface/common/getVeriCode?mobile=15899773751&usage=用户注册"));
	
		String url = "{\"userId\":1,\"distributionType\":1,\"distributionTime\":\"2015-03-20 12:22:22\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 12:22:22\",\"serviceTimeTo\":\"2015-03-20 12:22:22\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"goods\":[{\"shopId\":660165405,\"goodsId\":30}]}";
		System.out.println(post("http://192.168.20.80:8080/appServer/interface/order/placeOrder", url));
	}
}
