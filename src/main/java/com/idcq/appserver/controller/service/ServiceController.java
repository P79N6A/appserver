/*package com.idcq.appserver.controller.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.region.ITownDao;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.order.OrderDetailDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcard.ICashCardService;
import com.idcq.appserver.service.cashcoupon.ICashCouponService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.coupon.ICouponService;
import com.idcq.appserver.service.discount.IShopTimeDiscountService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.order.IXorderService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IDistribTakeoutSettingService;
import com.idcq.appserver.service.shop.IShopConfigureSettingService;
import com.idcq.appserver.service.shop.IShopRsrcServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.service.shop.ITakeoutSetService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.service.goodsGroup.IGoodsGroupService;

*//**
 * 非校验token控制
 * 
 * @author Administrator
 * 
 * @date 2015年12月7日
 * @time 下午11:38:08
 *//*
@Controller
@RequestMapping(value="/service")
public class ServiceController {

	private static final Logger logger = Logger.getLogger(ServiceController.class);
	@Autowired
	public IMemberServcie memberServcie;
	@Autowired
	public IShopServcie shopServcie;
	@Autowired
	public IShopRsrcServcie shopRsrcServcie;
	@Autowired
	private ICashCouponService cashCouponService;
	@Autowired
	private IShopTimeDiscountService shopTimeDiscountService;
	@Autowired
	private ICouponService couponService;
	@Autowired
	private IMemberServcie memberService;
	@Autowired
	private ITakeoutSetService takeoutSetService;
	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private IRegionDao regionDao;
	@Autowired
	private ITownDao townDao;
	@Autowired
	private IPayServcie payServcie;
	@Autowired
	private IDistribTakeoutSettingService distribTakeoutSettingService;
	@Autowired
	private IShopTechnicianService iShopTechnicianService;
	@Autowired
	private IShopTechnicianService shopTechnicianService;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IGoodsGroupService goodsGroupService;
	@Autowired
	private ICollectService collectService;
	@Autowired
	private IShopConfigureSettingService shopSettingService;
	@Autowired
	private IOrderServcie orderService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IOrderServcie orderServcie;
	@Autowired
	private IXorderService xOrderService;
	@Autowired
	private ICommonService commonService;
	@Autowired
    private IShopServcie shopService;
	@Autowired
	private IGoodsServcie goodService;
	@Autowired
    private ICashCardService cashCardService;
	
	
    *//**
     * 转换账户类型
     * @param accountTypeStr
     * @return
     *//*
    private Integer convertAccountType(String accountTypeStr) {
        if("1".equals(accountTypeStr))
            return 0;
        if("2".equals(accountTypeStr))
            return 1;
        if("3".equals(accountTypeStr))
            return 3;
        if("4".equals(accountTypeStr))
            return 2;
        return null;
    }
    

	
	
 
    *//**
     * 验证shopId公共方法
     * 
     * @Function: com.idcq.appserver.controller.session.SessionController.validShopId
     * @Description:
     *
     * @param shopIdStr
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月23日 下午8:40:06
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年12月23日    ChenYongxin      v1.0.0         create
     *//*
    public Long validShopId(String shopIdStr) throws Exception{
        CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr,
                CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        //校验店铺是否存在
        ShopDto shopDto  = shopServcie.getShopById(shopId);
        if (shopDto == null) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_SHOP);
        }
        return shopId;
    }

}
*/