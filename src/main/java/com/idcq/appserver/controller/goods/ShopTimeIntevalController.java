package com.idcq.appserver.controller.goods;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.goods.IShopTimeIntevalService;
import com.idcq.appserver.service.shop.IDistributionTakeoutService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.JedisPoolUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;


@Controller
@RequestMapping(value="/goods")
public class ShopTimeIntevalController {
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IShopTimeIntevalService shopTimeIntevalService;
	@Autowired
	private IDistributionTakeoutService distributionTakeoutService;
	
	@Autowired
	private IShopServcie shopService;
	
	@RequestMapping("/getShopTimeInteval")
	@ResponseBody
	private ResultDto getShopTimeInteval(HttpServletRequest request){
		Jedis jedis=null;
		try{
			String sid = RequestUtils.getQueryParam(request, "shopId");
			String serverMode = RequestUtils.getQueryParam(request, "serverMode");//服务方式：1(上门)，2(到店)
			if(null==serverMode){
				serverMode="2";
			}
			CommonValidUtil.validStrNull(sid, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			Long shopId = CommonValidUtil.validStrLongFmt(sid, CodeConst.CODE_PARAMETER_NOT_VALID,"shopId格式错误");
			ShopDto shop = shopService.getShopById(shopId);
			if (shop == null) {
				return ResultUtil.getResult(CodeConst.CODE_PARAMETER_NOT_EXIST,"商铺不存在", new JSONObject());
			}
			PageModel page = shopTimeIntevalService.getPage(shopId,Integer.parseInt(serverMode), 1, 100);
			
			MessageListDto msgDto = new MessageListDto();
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺服务时段成功！", msgDto);
			
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取商铺服务时段失败", e);
			throw new APISystemException("获取商铺服务时段失败", e);
		}finally{
			if(jedis!=null)
			{
				JedisPoolUtils.returnRes(jedis);
			}
		}
		
	}
	
	
}
