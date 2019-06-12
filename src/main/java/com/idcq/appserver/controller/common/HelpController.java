package com.idcq.appserver.controller.common;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;


/**
 * 公共组件controller
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午10:06:50
 */
@Controller
//@RequestMapping(value="/common")
public class HelpController {
	private final Logger logger = Logger.getLogger(HelpController.class);
	
	@Autowired
	private ICommonService commonService;
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/help/get1dcqSetting",produces="application/json;charset=utf-8")
	@ResponseBody
	public String get1dcqSetting(HttpServletRequest request){
		try {
			Long start=System.currentTimeMillis();
			logger.info("获取设置信息-start");
			
			String settingKey = RequestUtils.getQueryParam(request, "settingKey");
			CommonValidUtil.validObjectNull(settingKey, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.SETTING_KEY_NOT_NULL);
			Map map=(Map) DataCacheApi.getObject("1dcqSetting_"+settingKey);
			if(map==null){
				logger.info("从DB查询...");
				map=commonService.getSettingValueByKey(settingKey);
				boolean flag=DataCacheApi.setObjectEx("1dcqSetting_"+settingKey, (Serializable) map,300);
				//boolean flag=DataCacheApi.setObject("1dcqSetting_"+settingKey, (Serializable) map);
				logger.info("数据保存到cache:"+flag);
			}else{
				logger.info("从cache查询...");
			}
			Long end=System.currentTimeMillis();
			logger.info("获取设置信息共耗时："+(end-start));
			return ResultUtil.getResultJsonStr(0, "获取设置信息成功", map);
		} catch (ServiceException e) {
			this.logger.error("获取设置信息-异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取设置信息-系统异常",e);
			throw new APISystemException("获取设置信息-系统异常", e);
		}
	}
	
}
