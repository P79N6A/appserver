/*package com.idcq.appserver.controller.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.cashcard.ICashCardService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.discount.IShopTimeDiscountService;
import com.idcq.appserver.service.goods.IGoodsServcie;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IShopConfigureSettingService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.AESUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.mq.goods.GoodsMessageUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

*//**
 * 非校验token控制
 * 
 * @author Administrator
 * 
 * @date 2015年12月7日
 * @time 下午11:38:08
 *//*
@Controller
@RequestMapping(value="/session")
public class SessionController {

	private static final Logger logger = Logger.getLogger(SessionController.class);

	@Autowired
	public IShopServcie shopServcie;
	@Autowired
	private IShopTimeDiscountService shopTimeDiscountService;
	@Autowired
	private IMemberServcie memberService;
	@Autowired
	private IPayServcie payServcie;
	@Autowired
	private ICollectService collectService;
	@Autowired
	private IShopConfigureSettingService shopSettingService;
	@Autowired
	private IOrderServcie orderService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IShopServcie shopService;
	@Autowired
	private ICashCardService cashCardService;
    @Autowired
    private IGoodsServcie goodService;
    @Autowired
    private ISendSmsService sendSmsService;

    *//**
     * 转换账户类型
     * @param accountTypeStr
     * @return
     *//*
    private  Integer convertAccountType(String accountTypeStr) {
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