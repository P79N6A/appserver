package com.idcq.appserver.service.wifidog;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.exception.ValidateException;
@Service
public class WifiDogServiceImpl implements IWifiDogService {
	@Autowired
	IShopDeviceDao shopDeviceDao;
	public String queryServerAddress(String snId,String softwareName) throws Exception {
		//先查询商铺路由器表是否存在此路由器
		int shopDevice = shopDeviceDao.queryShopDeviceBySnid(snId);
		if (shopDevice == 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_ROUTER);
		}
		//如果存在，查询应用信息表，根据软件名字跟运行平台，查询出app编号appId
		//Map param = new HashMap();
		//param.put("softwareName", softwareName);
		//param.put("appPlatform", "路由器");
		//param.put("configName", CommonConst.CONFIG_NAME_ROUT);
		//List<Map> appConfig = shopDeviceDao.queryAppConfigInfo(param);
		String re = "";
		String addStr = shopDeviceDao.getCRAddress(CommonConst.CONFIG_KEY_ROUT);
		//List<String> lists = shopDeviceDao.getCRAddress(CommonConst.CONFIG_NAME_ROUT);
		if (!StringUtils.isEmpty(addStr)) {
			re = addStr.replaceAll(",", "\r\n");
			/*
			String[] strs = addStr.split(",");
			for (int i = 0; i < strs.length; i++) {
				if(strs[i] == null)continue;
				re +=(strs[i]+"\r\n");
			}*/
		}
		return re;
	}

}
