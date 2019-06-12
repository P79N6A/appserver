package com.idcq.appserver.service.wifidog;


public interface IWifiDogService {
	/**
	 * 获取版本及配置服务器（B服务器）的地址列表
	 * @param snId
	 * @return
	 */
	String queryServerAddress(String snId,String softwareName) throws Exception;
}
