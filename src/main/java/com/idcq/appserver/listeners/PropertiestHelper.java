package com.idcq.appserver.listeners;

import java.util.Properties;

import org.springframework.beans.BeansException;
/**
 * 获取properties
 * @author chenyongxin
 *
 */
public class PropertiestHelper  {
    private static Properties properties;
    
    public void setProperties(Properties propertie) throws BeansException  
    {  
    	PropertiestHelper.properties = propertie;
    }  
  /**
   * 获取properties对象
   * @return
   */
    public static Properties getProperties()  
    {  
        return properties;  
    }  

}
