package com.idcq.appserver.utils.httpClient;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil {
	
	
	public boolean sendGetRequest(String url,Map<String,Object>params)
	{
		return true;
	}
	
	
	/**
	 * 发送post请求
	* @Title: sendPostRequest 
	* @param @param url
	* @param @param params
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	public static String  sendPostRequest(String url,Map<String,Object>params)
	{
		HttpClient httpClient=new HttpClient();
		PostMethod post=new PostMethod(url);	
		NameValuePair []paramArray=new NameValuePair[params.size()];
		int index=0;
		for(Entry<String,Object>entry:params.entrySet())
		{
			NameValuePair postParams=new NameValuePair();
			postParams.setName(entry.getKey());
			postParams.setValue(entry.getValue()+"");
			paramArray[index++]=postParams;
		}
		post.setRequestBody(paramArray);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			httpClient.executeMethod(post);
			String responseResult= post.getResponseBodyAsString();
			post.releaseConnection();
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
