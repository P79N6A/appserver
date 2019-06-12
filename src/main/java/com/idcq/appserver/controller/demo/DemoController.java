package com.idcq.appserver.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.dto.demo.UserDto2;
import com.idcq.appserver.service.demo.IDemoService;
import com.idcq.appserver.service.goods.IGoodsServcie;

/**
 * controller demo
 * 
 * @author Administrator
 * 
 * @date 2015年3月2日
 * @time 下午4:42:39
 */
@Controller
@RequestMapping(value="/main")
public class DemoController {
	private  final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public IDemoService demoService;
	@Autowired 
	public IGoodsServcie goodsServcie;
	
	
	
//	@RequestMapping(value="/createGoodsIndex",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto createGoodsIndex(){	
//		//商品的索引文档目录
//		String gPath = "D:\\lucene_index\\goods_index";
//		try {
//			logger.info("创建商品全文索引-start");
//			//获取商品列表
//			PageModel pm = this.goodsServcie.getGoodsList(null, 1, 10);
//			//建立索引
//			int lastGId = LuceneUtil2.createGoodsIndex(gPath,pm.getList());
//			//lastGId写入application.properties
//			String file = "properties/application.properties";
//			Map<String,String> map = new HashMap<String, String>();
////			map.put("goods.index.last.id",lastGId+"");
//			map.put("goods","333");
//			PropertyUtil.writeProperty(file, map);
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "建立商品全文索引成功", null);
//		} catch (ServletException e) {
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			logger.error("建立商品索引-系统异常",e);
//			throw new APISystemException("建立商品索引-系统异常", e);
//		}
//	}
//	
//	@RequestMapping(value="/createTextIndex",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto createtextIndex(){	
//		//商品的索引文档目录
////		String gPath = "D:\\lucene_index";
//		String gPath = "D:\\lucene2.4.0_index";
//		String sPath = "D:\\lucene_src";
//		try {
//			logger.info("创建商品全文索引-start");
//			LuceneUtil2.createTextFileIndexPaoding(sPath, gPath);
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "建立商品全文索引成功", null);
//		} catch (ServletException e) {
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			logger.error("建立商品索引-系统异常",e);
//			throw new APISystemException("建立商品索引-系统异常", e);
//		}
//	}
//	
//	@RequestMapping(value="/updateGoodsIndex",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto updateGoodsIndex(){	
//		//商品的索引文档目录
//		String gPath = "D:\\lucene_index\\goods_index";
//		try {
//			logger.info("更新商品全文索引-start");
//			//获取商品列表
//			PageModel pm = this.goodsServcie.getGoodsList(null, 1, 10);
//			//建立索引
//			LuceneUtil2.updateGoodsIndex(gPath,pm.getList());
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "更新商品全文索引成功", null);
//		} catch (ServletException e) {
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			logger.error("更新商品索引-系统异常",e);
//			throw new APISystemException("更新商品索引-系统异常", e);
//		}
//	}
//	
//	@RequestMapping(value="/delGoodsIndex",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto delGoodsIndex(){	
//		//商品的索引文档目录
//		String gPath = "D:\\lucene_index\\goods_index";
//		try {
//			logger.info("删除商品全文索引-start");
//			//获取商品列表
//			PageModel pm = this.goodsServcie.getGoodsList(null, 1, 10);
//			//建立索引
//			List<Integer> ids = new ArrayList<Integer>();
//			ids.add(1);
//			ids.add(2);
//			ids.add(3);
//			LuceneUtil2.delIndex(gPath,GoodsField.GOODS_ID,ids);
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "删除商品全文索引成功", null);
//		} catch (ServletException e) {
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			logger.error("删除商品索引-系统异常",e);
//			throw new APISystemException("删除商品索引-系统异常", e);
//		}
//	}
//	
//	@RequestMapping(value="/searchGoodsIndex",produces="application/json;charset=UTF-8")
//	@ResponseBody
//	public ResultDto searchGoodsIndex(HttpServletRequest request){	
//		try {
//			logger.info("搜索商品-start");
//			String queryStr = RequestUtils.getQueryParam(request, "queryStr");
//			//商品的索引文档目录
//			String gPath = "D:\\lucene_index\\goods_index";
//			//建立索引
//			List<GoodsDto> list = LuceneUtil2.searchGoodsList(gPath,queryStr);
//			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "搜索商品成功", list);
//		} catch (ServletException e) {
//			throw new APIBusinessException(e);
//		} catch (Exception e) {
//			logger.error("搜索商品-系统异常",e);
//			throw new APISystemException("搜索商品-系统异常", e);
//		}
//	}
	
	/**
	 * restful get demo
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	public UserDto2 getIndex(@PathVariable long id,
		    HttpServletRequest request, HttpServletResponse response){
		if(logger.isInfoEnabled()){
			logger.info("Restful get call");
			logger.info("id : "+id);
		}
		UserDto2 user = new UserDto2();
		user.setId(id);
		return user;
	}
	
	/**
	 * restful post demo
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/{id}/{name}",method=RequestMethod.POST)
	@ResponseBody
	public UserDto2 getIndex2(@PathVariable long id,@PathVariable String name){
		if(logger.isInfoEnabled()){
			logger.info("Restful post call");
			logger.info("id : "+id + " name : "+name);
		}
		UserDto2 user = new UserDto2();
		user.setId(id);
		user.setName(name);
		return user;
	}
	
	/**
	 * general http post
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.POST)
	@ResponseBody
	public UserDto2 getIndex3(@ModelAttribute(value="user")UserDto2 user){
		if(logger.isInfoEnabled()){
			logger.info("general http post");
			logger.info("id : "+user.getId() + " name : "+user.getName());
		}
		return user;
	}
	
	/**
	 * test service
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/testService",method=RequestMethod.GET)
	@ResponseBody
	public UserDto2 getIndex4() throws Exception{
		if(logger.isInfoEnabled()){
			logger.info("general get -testService");
		}
		UserDto2 user = this.demoService.getUserById(1);
		if(logger.isInfoEnabled()){
			logger.info("id : "+user.getId() + " name : "+user.getName());
		}
		return user;
	}
	
	/**
	 * test db
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/testDb",method=RequestMethod.GET)
	@ResponseBody
	public UserDto2 getIndex5(@ModelAttribute(value="user")UserDto2 user) throws Exception{
		if(logger.isInfoEnabled()){
			logger.info("DB insert");
		}
		user = this.demoService.insertUser(user);
		if(logger.isInfoEnabled()){
			logger.info("id : "+user.getId() + " name : "+user.getName());
		}
		return user;
	}
	
	@RequestMapping(value="/testReturn",method=RequestMethod.POST)
	@ResponseBody
	public String getIndex6() throws Exception{
		if(logger.isInfoEnabled()){
			logger.info("test not return");
		}
		return "";
	}
}
