package com.idcq.appserver.controller.goods;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.goods.IShopResourceGroupService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

@Controller
@RequestMapping(value="/goods")
public class ShopResourceGroupController {
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IShopResourceGroupService shopResourceGroupService;
	
	
	@RequestMapping(value="/getShopResourceGroup")
	@ResponseBody
	public ResultDto getShopResourceGroup(HttpServletRequest request){
		try{
			String sid = RequestUtils.getQueryParam(request, "shopId");
			CommonValidUtil.validObjectNull(sid, CodeConst.CODE_PARAMETER_NOT_NULL,"shopId不能为空");
			Long shopId = CommonValidUtil.validStrLongFmt(sid, CodeConst.CODE_PARAMETER_NOT_VALID,"shopId格式错误");
			int pageNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pageSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			PageModel page = shopResourceGroupService.getPageSRG(shopId, pageNo, pageSize);
			
			MessageListDto msgDto = new MessageListDto();
			msgDto.setpNo(pageNo);
			msgDto.setpSize(pageSize);
			msgDto.setrCount(page.getTotalItem());
			msgDto.setLst(page.getList());
			
			//List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			//dataList.add(msgDto);
			
			return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取商铺资源分组成功！", msgDto);
		}catch(ServiceException e){
			throw new APIBusinessException(e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取商铺资源分组失败", e);
			throw new APISystemException("获取商铺资源分组失败", e);
		}
		
		
	}
	
}
