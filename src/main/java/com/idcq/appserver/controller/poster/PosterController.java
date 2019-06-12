package com.idcq.appserver.controller.poster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.poster.Poster;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.poster.IPosterActivityService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.PosterImageUtil;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 商圈活动
 * 
 * @author Administrator
 * 
 */
@Controller
public class PosterController {
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IPosterActivityService posterActivityService;


	/**
	 * 创建海报图片接口
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = {
			"/service/poster/createPoster",
			"/token/poster/createPoster",
			"/session/poster/createPoster" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object createPoster(HttpEntity<String> entity,HttpServletRequest request) {
		logger.info("创建海报图片接口-start"+entity.getBody() );
		try {
			
			Poster poster = (Poster)JacksonUtil.postJsonToObj(entity, Poster.class,DateUtils.DATE_FORMAT);
			Integer posterId = null;
			String logoText = null;
			String dateTime = null;//"活动时间：2016-04-01至2016-04-16";  
			String posterContent = null;
			String bizType=null;
			if(poster!=null){
				posterId=poster.getPosterID();
				logoText=poster.getPosterTitle();
				dateTime=poster.getPosterDate();
				posterContent=poster.getPosterContent();
				bizType=poster.getBizType();
			}
			if(posterContent==null){
				 CommonValidUtil.validObjectNull(posterContent, CodeConst.CODE_PARAMETER_NOT_NULL, "海报内容posterContent不能为空");
			}
	    	String[] posterContentArray=posterContent.split(",");
	    	ArrayList<String> list = new ArrayList<String>();
	    	for (int i=0;i<posterContentArray.length;i++) {
				list.add(posterContentArray[i]);
			}
	        //1=======创建海报
	    	Map map=null;
	    	Map returnMap = new HashMap();
        	PosterImageUtil posterImageUtil = new PosterImageUtil();
        	
        	map =posterImageUtil.createPorster(posterId,logoText,dateTime,list,request);
        	map.put("bizType", bizType);
        	map.put("posterId", posterId);
        	if(map!=null){
        		returnMap = posterActivityService.saveAttachment(map);
        	}
	        //2=======拷贝海报到资源服务器
	        return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"创建海报成功！", returnMap);
		}catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("创建海报-系统异常", e);
			throw new APISystemException("创建海报-系统异常", e);
		}
	}
	/**
	 * 获取海报图片列表接口
	 * 
	 * @param request
	 * @return String
	 * @throws Exception 
	 */
	@RequestMapping(value = {
			"/service/poster/getPosterList",
			"/token/poster/getPosterList",
			"/session/poster/getPosterList" },  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getPosterList(HttpServletRequest request) throws Exception {
		try {
			List<Map> list = posterActivityService.getPosterList();
			List<Map> mapList = new ArrayList<Map>();
			Map<String,Object> returnMap = new HashMap<String,Object>();
			Map<String,Object> map=null;
			for (Map listmap : list) {
				if(listmap!=null){
					map = new HashMap<String,Object>();
					map.put("posterID", listmap.get("bizId"));
					map.put("imgUrl", FdfsUtil.getFileProxyPath(String.valueOf(listmap.get("fileUrl"))));
					mapList.add(map);
				}
			}
			returnMap.put("lst", mapList);
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED,"获取海报成功！", returnMap);
		} catch (ServiceException e) {
			throw new APIBusinessException(e);
		} catch (Exception e) {
			this.logger.error("获取海报-系统异常", e);
			throw new APISystemException("获取海报-系统异常", e);
		}
	}
	
}
