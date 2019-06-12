package com.idcq.appserver.controller.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.controller.shop.ShopController;
import com.idcq.appserver.dto.pageArea.PageAreaDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.pageArea.IPageAreaService;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
/**
 * 页面投放控制器
 * @ClassName: PageAreaController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午3:32:22 
 *
 */
@Controller
public class PageAreaController {
	

	private static final Logger logger = Logger.getLogger(ShopController.class);
	
	@Autowired
	private IPageAreaService pageAreaService;
	
	@RequestMapping(value="index/getPageAreaUrl", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getPageAreaUrl(HttpServletRequest request)
	{
		try{
			String positionType=RequestUtils.getQueryParam(request, "positionType");
			if(StringUtils.isEmpty(positionType))
			{
				positionType="0";
			}
			String cityId=RequestUtils.getQueryParam(request, "cityId");
			Map<String,Object>dataMap=new HashMap<String,Object>();
			List<PageAreaDto>pageAreaList=pageAreaService.getPageAreaUrl(positionType, cityId);
			if(pageAreaList!=null&&pageAreaList.size()>0)
			{
				dataMap.put("url", pageAreaList.get(0).getPageAreaUrl());
			}
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取投放页面URL成功",
					dataMap, DateUtils.TIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索店铺-系统异常", e);
			throw new APISystemException("搜索店铺-系统异常", e);
		}
	}
}
