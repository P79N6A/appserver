package com.idcq.appserver.controller.cache;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
        
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.CacheData;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 
 * @author zhq
 *
 */
@Controller
//@RequestMapping(value="/interface")
public class CacheController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 获取缓存中的jianzhi
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/getObject")
	@ResponseBody
	public Object getObject(HttpServletRequest request){
		try {
			logger.info("从cache获取数据-start");
			String key = RequestUtils.getQueryParam(request, "key");
			CommonValidUtil.validObjectNull(key, CodeConst.CODE_PARAMETER_NOT_NULL,"key不能为空");
			Object value=(Object) DataCacheApi.getObject(key);
			Gson gson = new Gson();
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "从cache获取数据成功",  gson.fromJson(gson.toJson(value), Object.class));
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从cache获取数据-系统异常", e);
			throw new APISystemException("从cache获取数据-系统异常", e);
		}
	}
	
	/**
	 * 获取缓存中的jianzhi
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/setObject")
	@ResponseBody
	public Object setObject(HttpServletRequest request){
		try {
			logger.info("设置缓存-start");
			String key = RequestUtils.getQueryParam(request, "key");
			String value = RequestUtils.getQueryParam(request, "value");
			boolean flag = DataCacheApi.setObject(key, value);
			if(flag){
				return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "设置缓存成功", null);	
			}else{
				return ResultUtil.getResult(CodeConst.CODE_SYSTEM_BUSY, "设置缓存失败", null);	
			}
			
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置缓存-系统异常", e);
			throw new APISystemException("设置缓存-系统异常", e);
		}
	}
	
	/**
	 * 获取某一类的缓存值
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/getObjects")
	@ResponseBody
	public Object getObjects(HttpServletRequest request){
		try {
			logger.info("从cache获取数据-start");
			String key = RequestUtils.getQueryParam(request, "key");
			List<String> keys = DataCacheApi.keys(key+"*");
			
			List<CacheData> values = new ArrayList<CacheData>();
			CacheData cacheData = null;
			for(String k: keys){
				cacheData = new CacheData();
				cacheData.setKey(k);
				cacheData.setValue(DataCacheApi.getObject(k));
				values.add(cacheData);
			}
			Gson gson = new Gson();
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "从cache获取数据成功",  gson.fromJson(gson.toJson(values), List.class));
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从cache获取数据-系统异常", e);
			throw new APISystemException("从cache获取数据-系统异常", e);
		}
	}
	
	
	
	/**
	 * 获取缓存中的jianzhi
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/get")
	@ResponseBody
	public Object get(HttpServletRequest request){
		try {
			logger.info("从cache获取数据-start");
			String key = RequestUtils.getQueryParam(request, "key");
			CommonValidUtil.validObjectNull(key, CodeConst.CODE_PARAMETER_NOT_NULL,"key不能为空");
			String value=DataCacheApi.getInc(key);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "从cache获取数据成功", value);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从cache获取数据-系统异常", e);
			throw new APISystemException("从cache获取数据-系统异常", e);
		}
	}
	
	/**
	 * 获取商铺中的商品分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/getKeys")
	@ResponseBody
	public Object getKeys(HttpServletRequest request){
		try {
			logger.info("从cache获取key-start");
			String key = RequestUtils.getQueryParam(request, "key");
			CommonValidUtil.validObjectNull(key, CodeConst.CODE_PARAMETER_NOT_NULL,"key不能为空");
			List<String> value=DataCacheApi.keys(key);
			//String value=(String) DataCacheApi.getObject(key);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "从cache获取key成功", value);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("从cache获取key-系统异常", e);
			throw new APISystemException("从cache获取key-系统异常", e);
		}
	}
	
	/**
	 * 刷新缓存
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/refresh")
	@ResponseBody
	public Object refresh(HttpServletRequest request){
		try {
			logger.info("刷新cache数据-start");
			return DataCacheApi.flushDb();
//			String key = RequestUtils.getQueryParam(request, "key");
//			if(StringUtils.isBlank(key)){
//				return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_NULL, "缓存key不能为空", null);
//			}else{
//				if(DataCacheApi.exists(key)){
//					DataCacheApi.del(key);
//					DataCacheApi.flushDb();
//					return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "刷新指定key:"+key+"数据成功", null);
//				}else{
//					return ResultUtil.getResult(CodeConst.CODE_SUCCEED, key+"缓存不存在", null);
//				}
//			}
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("刷新cache数据-系统异常", e);
			throw new APISystemException("刷新cache数据-系统异常", e);
		}
	}
	
	/**
	 * 模糊刷新匹配的缓存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cache/refreshByPattern")
	@ResponseBody
	public Object refreshByPattern(HttpServletRequest request){
		try {
			logger.info("刷新cache数据-start");
			String key = RequestUtils.getQueryParam(request, "key");
			if(StringUtils.isBlank(key)) {
			    return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_NULL, "缓存key不能为空", null);
			}
			DataCacheApi.delByPattern(key+"*");
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "刷新指定类型key:"
					+ key + "数据成功", null);

		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("刷新cache数据-系统异常", e);
			throw new APISystemException("刷新cache数据-系统异常", e);
		}
	}
	
	
}
