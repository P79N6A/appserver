package com.idcq.appserver.controller.shop;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.shop.LauncherIconDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.shop.ILauncherService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * launcher接口控制层
 * 
 * @author Administrator
 * 
 * @date 2016年2月19日
 * @time 上午10:52:16
 */
@Controller
public class LauncherController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ILauncherService lanILauncherService;

	/**
	 * CS23：获取Launcher主页功能图标列表接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/getLauncherIcons", produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getLauncherIcons(HttpServletRequest request) {
		long start = System.currentTimeMillis();
		try {
			logger.info("获取Launcher主页功能图标列表-start");
			String launcherTypeStr = RequestUtils.getQueryParam(request,"launcherType");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			
			// 数据校验
			CommonValidUtil.validStrNull(launcherTypeStr,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_LAUNCHER_TYPE);
			CommonValidUtil.validStrIntFmt(launcherTypeStr,CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_LAUNCHER_TYPE);
			CommonValidUtil.validStrNull(shopIdStr,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validPositLong(shopIdStr,CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			
			int launcherType = Integer.valueOf(launcherTypeStr);
			long shopId = Long.valueOf(shopIdStr);
			
			List<LauncherIconDto> iconList = this.lanILauncherService.getLauncherIcons(launcherType, shopId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,CodeConst.MSG_SUCCEED_GET_LAUNCHER_ICONS, iconList);
		} catch (ServiceException e) {
			logger.error("获取Launcher主页功能图标列表-业务异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取Launcher主页功能图标列表-系统异常", e);
			throw new APISystemException("获取Launcher主页功能图标列表-系统异常", e);
		} finally {
			logger.info("共耗时：" + (System.currentTimeMillis() - start));
		}
	}
	

	/**
	 * CS24：获取 apk 名称接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shop/getAppNames", produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResultDto getAppNames(HttpServletRequest request) {
		long start = System.currentTimeMillis();
		try {
			logger.info("获取 apk 名称-start");
			String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
			
			// 数据校验
			CommonValidUtil.validStrNull(shopIdStr,CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_SHOPID);
			CommonValidUtil.validPositLong(shopIdStr,CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			
			long shopId = Long.valueOf(shopIdStr);
			
			List<Map> appNameList = this.lanILauncherService.getAppNames(shopId);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"获取 apk 名称列表成功！", appNameList);
			
		} catch (ServiceException e) {
			logger.error("获取 apk 名称-业务异常", e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("获取 apk 名称-系统异常", e);
			throw new APISystemException("获取 apk 名称-系统异常", e);
		} finally {
			logger.info("共耗时：" + (System.currentTimeMillis() - start));
		}
	}
	
	
}
