/**
 * Copyright (C) 2015 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.utils.mobile
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2015年11月6日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.MobileAttributionDto;
import com.idcq.appserver.dto.common.SysConfigureDto;
import com.idcq.appserver.service.common.ICommonService;

import net.sf.json.JSONObject;

public class MobileUtil {
    private static Logger logger = Logger.getLogger(MobileUtil.class);

    public final static String MOBILE_ATTRIBUTION_VALUE = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone=";

    public final static String API_KEY = "c956235e46e8297e7a72d461179b48d8";

    // "http://apistore.baidu.com/microservice/mobilephone?tel=";
    // http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone=
    /**
     * 获取手机号码归属地
     * 
     * @Function: com.idcq.appserver.utils.MobileUtil.getAddressByMobile
     * @Description:
     * 
     * @param mobile
     * @return
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年11月9日 下午3:31:52
     * 
     *  Modification History: Date Author Version Description
     *  ------------------------------------------------------
     *                2015年11月9日   ChenYongxin  v1.0.0 create
     *                2016年02月16日 shengzhipeng v1.0.1 更换API
     */
    public static MobileAttributionDto getAddressByMobile(String mobile) {
        MobileAttributionDto mad = new MobileAttributionDto();
//        ISysConfigureDao sysConfigureDao = BeanFactory.getBean(ISysConfigureDao.class);
//        ISysConfigureDao sysConfigureDao = BeanFactory.getBean(ISysConfigureDao.class);
        ICommonService commonService = BeanFactory.getBean(ICommonService.class);
        try {
            String mobileAttributionValue = "";
            // 默认webservice
         /*   SysConfigureDto mobileAttributionDto = sysConfigureDao.getSysConfigureDtoByKey("mobileAttributionKey");
            if (mobileAttributionDto != null) {
                mobileAttributionValue = mobileAttributionDto.getConfigureValue();
            } else {
                mobileAttributionValue = MOBILE_ATTRIBUTION_VALUE;
            }*/
            ConfigDto searchCondition = new ConfigDto();
            searchCondition.setBizId(0l);
            searchCondition.setBizType(0);
            searchCondition.setConfigKey("mobileAttributionKey");
            ConfigDto config = commonService.getConfigDto(searchCondition);
//            SysConfigureDto mobileAttributionDto = sysConfigureDao.getSysConfigureDtoByKey("mobileAttributionKey");
            if (config != null) {
                mobileAttributionValue = config.getConfigValue();
            } else {
                mobileAttributionValue = MOBILE_ATTRIBUTION_VALUE;
            }
            // 百度webservice
            String str = getJsonContent(mobileAttributionValue + mobile);
            JSONObject obj = (JSONObject) JSONObject.fromObject(str).get("retData");
            mad = (MobileAttributionDto) JSONObject.toBean(obj, MobileAttributionDto.class);
        } catch (Exception e) {
            logger.error("webservice查询手机归属地失败！号码为：" + mobile);
        }
        return mad;

    }

    public static String getJsonContent(String urlStr) {
    	HttpURLConnection httpConn = null;
        try {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            httpConn = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("apikey", API_KEY);
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200) {
                return ConvertStream2Json(httpConn.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	httpConn.disconnect();
        }
        return "";
    }

    private static String ConvertStream2Json(InputStream inputStream) throws IOException {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
        }
        // 将内存流转换为字符串
        jsonStr = new String(out.toByteArray());
        return jsonStr;
    }

    /**
     * @param urlAll :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "c956235e46e8297e7a72d461179b48d8");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String str = getJsonContent(MOBILE_ATTRIBUTION_VALUE + "18611112222");
        JSONObject obj = (JSONObject) JSONObject.fromObject(str).get("retData");
        MobileAttributionDto mad = (MobileAttributionDto) JSONObject.toBean(obj, MobileAttributionDto.class);
        System.out.println(mad.getProvince());

        String httpUrl = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber";
        String httpArg = "phone=18611112222";
        String jsonResult = request(httpUrl, httpArg);
        JSONObject obj1 = (JSONObject) JSONObject.fromObject(jsonResult).get("retData");
        MobileAttributionDto mad1 = (MobileAttributionDto) JSONObject.toBean(obj1, MobileAttributionDto.class);
        System.out.println(mad1.getCity());
        // System.out.println(jsonResult);
    }

}
