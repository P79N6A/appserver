package com.idcq.idianmgr.controller.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.idianmgr.dto.shop.ShopClassesDto;
import com.idcq.idianmgr.dto.shop.ShopTechnicianClassesDto;
import com.idcq.idianmgr.service.home.IShopTechnicianClassesService;
import com.idcq.idianmgr.service.shop.IShopClassesService;
/**
 * 排班设置
 * @ClassName: ScheduleSettingController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 上午10:40:40 
 *
 */
@Controller
public class ScheduleSettingController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	/**
	 * 店铺排班
	 */
	@Autowired
	private IShopTechnicianClassesService shopTechnicianClassesService;
	
	/**
	 * 店铺排班
	 */
	@Autowired
	private IShopClassesService shopClassesService;
	
	@Autowired
	private IShopServcie shopService;
	
	@Autowired
	private IShopServcie appServerShopService;
	
	@Autowired
	private IShopTechnicianService shopTechnicianService;
	/**
	 * 获取商铺排班设置
	 * @Title: getScheduleSetting 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="shopManage/getScheduleSetting",produces="application/json;charset=utf-8")
	public @ResponseBody String getScheduleSetting(HttpServletRequest request)
	{
		try{
			String shopId=RequestUtils.getQueryParam(request, "shopId");//商铺编号
			String startDate=RequestUtils.getQueryParam(request, "startDate");//开始时间
			String endDate=RequestUtils.getQueryParam(request, "endDate");//结束时间
			String techId=RequestUtils.getQueryParam(request, "techId");//技师编号
			CommonValidUtil.validObjectNull(shopId,
					CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			CommonValidUtil.validNumStr(shopId+"", CodeConst.CODE_PARAMETER_NOT_VALID,
					"shopId格式错误");
			CommonValidUtil.validObjectNull(techId,
					CodeConst.CODE_PARAMETER_NOT_NULL, "techId不能为空");
			CommonValidUtil.validNumStr(techId, CodeConst.CODE_PARAMETER_NOT_VALID,
					"techId格式错误");
			CommonValidUtil.validObjectNull(startDate,
					CodeConst.CODE_PARAMETER_NOT_NULL, "startDate不能为空");
			CommonValidUtil.validDateStr(startDate, CodeConst.CODE_PARAMETER_NOT_VALID,
					"startDate格式错误");
			CommonValidUtil.validObjectNull(endDate,
					CodeConst.CODE_PARAMETER_NOT_NULL, "endDate不能为空");
			CommonValidUtil.validDateStr(endDate, CodeConst.CODE_PARAMETER_NOT_VALID,
					"endDate格式错误");
			if(!judgeShopExist(Long.parseLong(shopId)))
	        {
	         	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺不存在",
							new HashMap<String,String>());
	        }
			List<ShopTechnicianClassesDto>shopClassesDtoList=shopTechnicianClassesService.getScheduleSetting(shopId,startDate,endDate,techId);
			MessageListDto msgList = new MessageListDto();
			msgList.setLst(DataConvertUtil.convertListObjToMap(shopClassesDtoList,CommonResultConst.GET_SCHEDULE_SETTING));
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取排班设置成功",
					msgList, DateUtils.DATE_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取排班设置-系统异常", e);
			throw new APISystemException("获取排班设置-系统异常", e);
		}
	}
	
	/**
	 * 班次设置接口
	 * @Title: setClassSetting 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="shopManage/setClassSetting",produces="application/json;charset=utf-8",consumes="application/json",method=RequestMethod.POST)
	public @ResponseBody String setClassSetting(HttpServletRequest request,@RequestBody Map<String,Object> hashMap)
	{
		try{
			Object shopId=hashMap.get("shopId");
			CommonValidUtil.validObjectNull(shopId,
					CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			CommonValidUtil.validNumStr(shopId+"", CodeConst.CODE_PARAMETER_NOT_VALID,
					"shopId格式错误");
            if(!judgeShopExist(Long.parseLong(shopId+"")))
            {
            	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺不存在",
						new HashMap<String,String>());
            }
            Object classes=hashMap.get("classes");
            if(classes==null)
            {

	        	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "排班参数classes为空",
						new HashMap<String,String>());
            }
			JSONArray array=JSONArray.fromObject(classes);
			Collection objectArray=JSONArray.toCollection(array); 
			List<ShopClassesDto>shopClassesList=new ArrayList<ShopClassesDto>();
			boolean validate=true;
			if(array!=null&&!array.isEmpty())
			{
				Iterator it=objectArray.iterator();  
			    while(it.hasNext())  
			    {  
			        JSONObject testJSONObj=JSONObject.fromObject(it.next());  
			        ShopClassesDto shopClassesObj=(ShopClassesDto) JSONObject.toBean(testJSONObj,ShopClassesDto.class);
			        if(shopClassesObj.getClassType()==null||shopClassesObj.getWorkTime()==null)
			        {
			        	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "排版设置信息参数缺失",
								new HashMap<String,String>());
			        }
			        shopClassesList.add(shopClassesObj);
			        shopClassesObj.setCreateTime(new Date());
			        shopClassesObj.setShopId(Long.parseLong(shopId+""));
			    }  
			}
			if(shopClassesList.size()>0)
			{
				shopClassesService.deleteByShopId(Long.parseLong(shopId+""));
				shopClassesService.batchSetClassSetting(shopClassesList);
			}
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "设置排班成功",
					new HashMap<String,String>(), DateUtils.TIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("排版设置-系统异常", e);
			throw new APISystemException("排版设置-系统异常", e);
		}
	}
	
	/**
	 * 获取商铺班次
	 * @Title: getClassesSetting 
	 * @param @param request
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="shopManage/getClassSetting",produces="application/json;charset=utf-8")
	public @ResponseBody String getClassesSetting(HttpServletRequest request)
	{
		try
		{
			String shopId=RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validObjectNull(shopId,
					CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
			CommonValidUtil.validNumStr(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
					"shopId格式错误");
			PageModel pageModel=new PageModel();
			MessageListDto msgList = new MessageListDto();
			List<ShopClassesDto>list=shopClassesService.getShopClassesList(Long.parseLong(shopId));
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(DataConvertUtil.convertListObjToMap(list,CommonResultConst.GET_CLASSES_SETTING));
			msgList.setrCount(list.size());
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取商铺班次成功！",
					msgList, DateUtils.TIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("排版设置-系统异常", e);
			throw new APISystemException("获取商铺班次-系统异常", e);
		}
	}
	
	
	/**
	 * 设置技师排班
	 * @Title: setScheduleSetting 
	 * @param @param request
	 * @param @param hashMap
	 * @param @return
	 * @return String    返回类型 
	 * @throws
	 */
	@RequestMapping(value="shopManage/setScheduleSetting",produces="application/json;charset=utf-8")
	public @ResponseBody String setScheduleSetting(HttpServletRequest request,@RequestBody Map<String, Object> hashMap)
	{	
		try{
			Object shopId=hashMap.get("shopId");//商铺编号
			if(shopId==null)
			{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "shopId不能为空", new JSONObject());
			}
			Object shopTarget=DataCacheApi.getObject("shop"+shopId);
			if(shopTarget==null)
			{
				shopTarget=appServerShopService.getShopById(Long.parseLong(shopId+""));
				if(shopTarget==null)
				{
					return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在", new JSONObject());
				}
				DataCacheApi.setObjectEx("shop"+shopId, shopTarget,43200);
			}
			Object teachId=hashMap.get("techId");//技师编号
			if(teachId==null)
			{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "teachId不能为空", new JSONObject());
			}
			Integer teachCount=shopTechnicianService.queryTechnicianExists(Long.parseLong(teachId+""));
			if(teachCount!=null&&teachCount.intValue()==0)
			{
				return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_EXIST, "技师编号不存在", new JSONObject());
			}
			Object days=hashMap.get("days");
			if(days==null)
			{
				
			}
			JSONArray array=JSONArray.fromObject(days);
			Collection objectArray=JSONArray.toCollection(array); 
			List<ShopTechnicianClassesDto>shopClassesList=new ArrayList<ShopTechnicianClassesDto>();
			String startDate=null;
			String endDate=null;
			ShopTechnicianClassesDto shopClassesParamObj=new ShopTechnicianClassesDto();
			if(array!=null&&!array.isEmpty())
			{
				Iterator it=objectArray.iterator();  
			    while(it.hasNext())  
			    {  
			        JSONObject testJSONObj=JSONObject.fromObject(it.next());  
			        ShopTechnicianClassesDto shopClassesObj=(ShopTechnicianClassesDto) JSONObject.toBean(testJSONObj,ShopTechnicianClassesDto.class);
			        if(shopClassesObj==null)
			        {
			        	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_NULL, "排班子元素为空", new JSONObject());
			        }
			        if(shopClassesObj.getClassesDate()==null||testJSONObj.getString("classesDate")==null)
			        {
			        	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_NULL, "days中classesDate为空", new JSONObject());
			        }
			        if(shopClassesObj.getClassesType()==null)
			        {
			        	return ResultUtil.getResultJsonStr(CodeConst.CODE_PARAMETER_NOT_NULL, "days中classesType为空", new JSONObject());
			        }
			        Date classedDate= DateUtils.parse(testJSONObj.getString("classesDate"), DateUtils.DATE_FORMAT);
			        if(startDate==null)
			        {
			        	startDate=DateUtils.getFirstDayInMonth(classedDate);
			        	endDate=DateUtils.getLastDayInMonth(classedDate);
			        	shopClassesParamObj.setStartDate(startDate);
			        	shopClassesParamObj.setEndDate(endDate);
			        }
			        shopClassesObj.setClassesDate(classedDate);
			        shopClassesList.add(shopClassesObj);
			        shopClassesObj.setTechId(teachId+"");
			        shopClassesObj.setCreateTime(new Date());
			        shopClassesObj.setShopId(Long.parseLong(shopId+""));
			    }  
			}
			if(shopClassesList.size()>0)
			{
				shopClassesParamObj.setShopId(Long.parseLong(shopId+""));
				shopClassesParamObj.setTechId(teachId+"");
				shopTechnicianClassesService.deleteByCondition(shopClassesParamObj);
				shopTechnicianClassesService.setScheduleSetting(shopClassesList);
			}
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "排班设置成功",
					new HashMap<String,String>(), DateUtils.TIME_FORMAT);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("排班设置-系统异常", e);
			throw new APISystemException("排班设置-系统异常", e);
		}
	}
	
	public static boolean judgeShopExist(Long shopId)throws Exception
	{
		IShopServcie service=BeanFactory.getBean(IShopServcie.class);
		ShopDto shopDto=(ShopDto)DataCacheApi.getObject(CommonConst.KEY_SHOP+shopId);
		if(shopDto==null)//判断店铺是否存在
		{
			shopDto=service.getShopById(shopId);
			if(shopDto==null)
			{
				return false;
			}
		}
		return true;
	}
}
