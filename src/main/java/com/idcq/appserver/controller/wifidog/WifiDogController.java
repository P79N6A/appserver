package com.idcq.appserver.controller.wifidog;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.wifidog.IWifiDogService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
/**
 * 获取版本及配置服务器（B服务器）的地址列表 controller
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 下午8:12:25
 */
@Controller
public class WifiDogController {
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	IWifiDogService wifiDogService;
	/**
	 * 获取版本及配置服务器（B服务器）的地址列表<br/>
	 * 每行一个地址，多个地址以“/r/n”分隔
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getaddress",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public Object getAddress(HttpServletRequest request){
		String re = "";
		try {
			logger.info("获取版本及配置服务器（B服务器）的地址列表-start");
			//路由器上软件的名字
			String softwareName = RequestUtils.getQueryParam(request, "softwareName");
			//当前版本号
			String currentVersion = RequestUtils.getQueryParam(request, "currentVersion");
			//路由器唯一标识
			String snId = RequestUtils.getQueryParam(request, "SNID");
			CommonValidUtil.validStrNull(softwareName, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_WARE_NAME);
			CommonValidUtil.validStrNull(currentVersion, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERSION);
			CommonValidUtil.validStrNull(snId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SNID);
			re = wifiDogService.queryServerAddress(snId,softwareName);
			//先查询商铺路由器表是否存在此路由器
			/*
			List<Map> address = wifiDogService.queryServerAddress(snId,softwareName);
			if (null == address) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "未查询到相关信息");
			}
			for (int i = 0; i < address.size(); i++) {
				re += (address.get(i).get("config_value") +"\r\n");
			}*/
		} catch (ServiceException e){
			logger.error("获取版本及配置服务器的地址列表时系统异常",e);
			//throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取版本及配置服务器的地址列表时系统异常",e);
			//throw new APISystemException("获取版本及配置服务器的地址列表时系统异常", e);
		}
		return re;
	}
}
