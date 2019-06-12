
package com.idcq.appserver.common;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;


/**
 * Copyright (C) 2016 Asiainfo-Linkage
 * velocity模板工具类
 *
 * @className:com.idcq.appserver.common.HelloVelocity
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年3月17日     ChenYongxin       v1.0.0        create
 *
 *
 */
public class VelocityUtils {
	/**
	 * 获取模板配置信息
	 * 
	 * @Function: com.idcq.appserver.common.VelocityUtils.getTemplateStr
	 * @Description:
	 *
	 * @param template模板文件名，默认在“\src\main\resources”目录
	 * @param para 参数map，key可以查看对应资源文件
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月21日 下午3:00:42
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月21日    ChenYongxin      v1.0.0         create
	 */
	public static String getTemplateStr(String template,
			Map<String, String> para) {
		VelocityEngine ve = new VelocityEngine();
		StringWriter writer = new StringWriter();
		
		try {
			
			Properties properties = new Properties();
			// 将当前路径设置到VelocityEngine 中
			properties.setProperty(ve.FILE_RESOURCE_LOADER_PATH,
					Thread.currentThread().getContextClassLoader().getResource("").getPath());
			properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
			properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
			
			ve.init(properties);
			
			Template t = ve.getTemplate(template);
			VelocityContext context = new VelocityContext();
			if (para.size() > 0) {
				for (String key : para.keySet()) {
					context.put(key, para.get(key));
				}
			}
			t.merge(context, writer);
		} catch (Exception e) {
			throw new RuntimeException("Error commiting transaction! cause:"
					+ e.getMessage());
		}
		return writer.toString();
	}
	
	public static String getValueByTemplate(String model, Map<String, String> param) {
		if (model == null || model.equals("")) {
			return "";
		}
		StringBuilder valueBuilder = new StringBuilder();
		for (String modelKey : model.substring(1).split("\\$")) {
			valueBuilder.append(param.get(modelKey));
		}
		return valueBuilder.toString();
	}

	public static void setShareTitleByTemplate(StringBuilder shareTileBuilder, String model,String shopName,String overValue,String giveValue) {
		String shareTile = model.replaceFirst("\\$shop", shopName).
								replaceFirst("\\$overValues", overValue).
								replaceFirst("\\$giveValues", giveValue);
		
		shareTileBuilder.append("，").append(shareTile);
		
	}
	public static void main(String[] a) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("over", "买 ");
		para.put("overValues", "100");
		para.put("give", "送");
		para.put("giveValues", "20");
		String as = getTemplateStr("VelocityBusinessAreaModel.vm", para);
		System.out.println(as);
	}
}
