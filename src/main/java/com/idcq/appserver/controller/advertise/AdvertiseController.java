package com.idcq.appserver.controller.advertise;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.advertise.AdvertiseDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.advertise.IAdvertiseService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;

/**
 * 
 * 广告接口控制层 主要包括:获取广告列表接口
 * @author  shengzhipeng
 * @version  [V1.0.0, 2016年2月13日]
 * @see  [相关类/方法] 
 * @since  [产品/模块版本]
 * Modification History:
 * Date          Author       Version     Description
 * -----------------------------------------------------------------
 * 2016年2月13日      shengzhipeng         V1.0.0        create
 */
@Controller
public class AdvertiseController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public IAdvertiseService advertiseService;

    @Autowired
    public IShopServcie shopServcie;

    /**
     * 一点传奇APP--A1获取广告列表接口
     * 获取广告列表接口实现方法 主要通过广告位编码分页获取指定城市的广告列表，如果归属地为空而店铺ID不为空， 默认查询店铺所属归属地广告
     * @param request
     * @return Object [广告列表组成的json字符串]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     * @author shengzhipeng
     * @date 2016年2月13日
     */
    @RequestMapping(value = { "/ad/getAD", "/token/common/getAD", "/service/common/getAD", "/session/common/getAD" }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAdvertises(HttpServletRequest request) {
        try {
            logger.info("分页获取广告列表-start");
            String cityId = RequestUtils.getQueryParam(request, "cityId");
            String adSpaceId = RequestUtils.getQueryParam(request, "adSpaceCode");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil.validStrNull(adSpaceId, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_ADSPACE_CODE);
            AdvertiseDto advertise = new AdvertiseDto();
            if (StringUtils.isNotBlank(cityId)) {
                CommonValidUtil.validPositLong(cityId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_CITYID);
                advertise.setCityCode(Long.valueOf(cityId));
            } else {
                if (StringUtils.isNotBlank(shopId)) {
                    ShopDto shop = shopServcie.getShopById(NumberUtil.strToLong(shopId, shopId));
                    if (shop != null) {
                        advertise.setCityCode(shop.getCityId());
                    }
                }
            }
            advertise.setAdPosId(adSpaceId);
            advertise.setCurTime(new Date());
            advertise.setAdPosIds(adSpaceId.split(CommonConst.COMMA_SEPARATOR));
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            String result = this.advertiseService.getAdListInCacheByCity(advertise, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            return result;
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取广告列表-系统异常", e);
            throw new APISystemException("获取广告列表-系统异常", e);
        }
    }
}
