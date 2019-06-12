package com.idcq.appserver.controller.catering;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.catering.ICateringServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 餐饮业controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午4:21:32
 */
@Controller
@RequestMapping(value = "/catering")
public class CateringController
{

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public ICateringServcie cateringServcie;

    @Autowired
    public IShopServcie shopServcie;

    /**
     * 分页获取餐饮业服务列表
     * 
     * @param catering
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCatering")
    @ResponseBody
    public ResultDto getCateringtList(@ModelAttribute(value = "shop")
    ShopDto shop, HttpServletRequest request)
    {
        try
        {
            logger.info("分页获取餐饮业服务列表-start");
            String pageNO = RequestUtils.getQueryParam(request, "pageNo");
            String pageSize = RequestUtils.getQueryParam(request, "pageSize");
            String typeId = RequestUtils.getQueryParam(request, "typeId");
            shop.setShopType(Integer.valueOf(typeId));
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.shopServcie.getShopList(shop, Integer.valueOf(pageNO), Integer.valueOf(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取餐饮业服务列表成功", msgList);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("获取餐饮业服务列表-系统异常", e);
            throw new APISystemException("获取餐饮业服务列表-系统异常", e);
        }
    }

}
