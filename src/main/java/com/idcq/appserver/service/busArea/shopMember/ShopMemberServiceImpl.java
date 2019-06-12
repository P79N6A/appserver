package com.idcq.appserver.service.busArea.shopMember;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IConfigDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponDao;
import com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao;
import com.idcq.appserver.dao.shopMemberCard.IShopMemberCardDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shop.ShopMemberStatInfo;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.ShopMemberLevel.IShopMemberLevelService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

@Service
public class ShopMemberServiceImpl implements IShopMemberService {

    private static final Logger log = LoggerFactory.getLogger(ShopMemberServiceImpl.class);

    @Autowired
    private IMemberServcie memberService;

    @Autowired
    private IShopMemberDao shopMemberDao;

    @Autowired
    public IUserDao userDao;

    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private IShopMemberCardService shopMemberCardService;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IShopCouponDao shopCouponDao;
    @Autowired
    private IUserShopCouponDao userShopCouponDao;
    @Autowired
    private IShopMemberCardDao shopMemberCardDao;
    @Autowired
    private IConfigDao configDao;
    @Autowired
    private IShopMemberLevelService shopMemberLevelService;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Map<String, String>> addShopMembers(List<ShopMemberDto> shopMemberDtos, Integer validator) {

        // 默认插入时间
        Date insertDate = new Date();

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> tempMap = null;
        for (ShopMemberDto dto : shopMemberDtos) {
            Long mobile = dto.getMobile();
            if (null == mobile) {
                continue;
            }
            // 设置统一默认插入时间
            if (dto.getCreateTime() == null) {
                dto.setCreateTime(insertDate);
            }
            try {
                if (dto.getShopId() != null) {
                    ShopMemberDto shopMemberDtoDetail = shopMemberDao.getShopMbByMobileAndShopId(String.valueOf(dto.getMobile()), dto.getShopId(), CommonConst.MEMBER_STATUS_DELETE);
                    if (shopMemberDtoDetail != null) {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_VALIDATE_MEMBER_DUP_EXISTS_ERROR);
                    }
                }

                this.addShopMember(dto, validator);
                //保存店内会员卡信息
                this.addShopMemberCard(dto);
                if(dto.getMobile() != null && dto.getShopId() != null){
                	shopMemberLevelService.upgrateShopMemberLevel(dto.getMobile().toString(), dto.getShopId(), 1);
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
                tempMap = new HashMap<String, String>();
                tempMap.put("mobile", dto.getMobile() + "");
                tempMap.put("reason", e.getMessage());
                tempMap.put("name", dto.getName());
                if (validator == 1) {
                    tempMap.put("weixinNo", dto.getWeixinId());
                    if (dto.getQq() != null) {
                        tempMap.put("qq", dto.getQq() + "");
                    } else {
                        tempMap.put("qq", null);
                    }
                }
                result.add(tempMap);
            }
        }
        return result;
    }

    @Override
    public ShopMemberDto getShopMemberDetail(Long memberId) throws Exception {
        return shopMemberDao.getShopMemberById(memberId);
    }

    @Override
    public List<ShopMemberDto> queryShopMemberList(Map<String, Object> requestMap) throws Exception {
        List<ShopMemberDto> shopMemberDtoList = new ArrayList<ShopMemberDto>();
        
        String birthdayStartDate = (String)requestMap.get("birthdayStartDate");
        String birthdayEndDate = (String)requestMap.get("birthdayEndDate");
        
        if(null != birthdayStartDate && null != birthdayEndDate){
			Date startTimeDate = DateUtils.parse(birthdayStartDate,  DateUtils.DATE_FORMAT);
			Date endTimeDate = DateUtils.parse(birthdayEndDate,  DateUtils.DATE_FORMAT);
			if(endTimeDate.getYear()-startTimeDate.getYear() == 1){
				requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
				requestMap.put("continueStartTime", "2000-12-31");
				requestMap.put("continueEndTime", "2000-01-01");
				requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
			}else if(endTimeDate.getYear()-startTimeDate.getYear() == 0){
				requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
				requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
			}else  if(endTimeDate.getYear()-startTimeDate.getYear() > 1){
				requestMap.put("birthdayStartDate", null);
				requestMap.put("birthdayEndDate", null);
			}
		}else if(null != birthdayStartDate){
			requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
		}else if(null!=birthdayEndDate){
			requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
		}
        
//        requestMap.put("birthdayStartDate", birthdayStartDate);
//        requestMap.put("birthdayEndDate", birthdayEndDate);
        
        
        shopMemberDtoList = shopMemberDao.getShopMemberList(requestMap);
        for (ShopMemberDto shopMemberDto : shopMemberDtoList) {
            if (shopMemberDto != null) {
                if (shopMemberDto.getUserId() != null) {
                    //查询消费次数和消费总金额
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("shopId", shopMemberDto.getShopId());
                    paramMap.put("userId", shopMemberDto.getUserId());
                    //paramMap.put("mobile", shopMemberDto.getMobile());
                    paramMap.put("orderStatus", CommonConst.ORDER_STATUS_FINISH);
                    paramMap.put("startDate", DateUtils.format(shopMemberDto.getCreateTime(), DateUtils.DATETIME_FORMAT));
                    
                    
                    Map<String, Object> map = orderDao.getOrderCountAndSettleMoney(paramMap);
                    if (map != null) {
                        if (map.get("purchaseNum") != null) {
                            shopMemberDto.setPurchaseNum(Integer.valueOf(map.get("purchaseNum") + ""));
                        } else {
                            shopMemberDto.setPurchaseNum(0);
                        }
                        if (map.get("purchaseMoney") != null) {
                            shopMemberDto.setPurchaseMoney(Double.valueOf(map.get("purchaseMoney") + ""));
                        } else {
                            shopMemberDto.setPurchaseMoney(0.0);
                        }
                    } else {
                        shopMemberDto.setPurchaseNum(0);
                        shopMemberDto.setPurchaseMoney(0.0);
                    }
                }
                UserDto userDto = null;
                if (shopMemberDto.getMobile() != null) {
                    userDto = userDao.getUserByMobile(shopMemberDto.getMobile() + "");
                    if (userDto != null && userDto.getIsMember() == CommonConst.USER_IS_MEMBER) {
                        if (shopMemberDto.getShopId() != null && userDto.getReferShopId() != null
                                && userDto.getReferShopId().equals(shopMemberDto.getShopId())) {
                            shopMemberDto.setIsUser(CommonConst.IS_SHOP_REF_USER);
                        } else {
                            shopMemberDto.setIsUser(CommonConst.IS_USER);
                        }
                    } else {
                        shopMemberDto.setIsUser(CommonConst.IS_NOT_USER);
                    }
                } else {
                    shopMemberDto.setIsUser(CommonConst.IS_NOT_USER);
                }
            }
        }
        return shopMemberDtoList;
    }

    @Override
    public Integer getShopMemberCount(Long shopId) throws Exception {
        return shopMemberDao.getShopMemberCount(shopId);
    }

    @Override
    public Map<String, Object> weixinBindShopMember(String openId, Long shopId, String nickName) throws Exception {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("weixinNo", openId);
        queryMap.put("shopId", shopId);

        List<ShopMemberDto> shopMemberList = shopMemberDao.getShopMemberList(queryMap);
        if (shopMemberList.size() > 0) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "该微信账号已经是店铺会员");
        }

        ShopMemberDto shopMember = new ShopMemberDto();
        shopMember.setWeixinNo(openId);
        shopMember.setName(nickName);
        shopMember.setShopId(shopId);
        UserDto userDto = null;

        userDto = userDao.getUserByWeiXinNo(openId);
        if (userDto == null) {
            try {
                userDto = memberService.saveUserFromWeiXin(openId);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        if (userDto != null) {
            if (CommonConst.USER_IS_NOT_MEMBER == userDto.getIsMember()) {
                userDto.setIsMember(CommonConst.USER_TO_ACTIVATE);
                userDao.updateUser(userDto);
            }
            DataCacheApi.del(CommonConst.KEY_USER + userDto.getUserId());
            shopMember.setUserId(userDto.getUserId());
        }
        shopMemberDao.addShopMember(shopMember);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("memberId", shopMember.getMemberId());
        return resultMap;
    }

    @Override
    public int delShopMemberByIds(Map<String, Object> requestMap) throws Exception {
        requestMap.put("memberStatus", CommonConst.MEMBER_STATUS_DELETE);
        int num = shopMemberDao.delShopMemberByIds(requestMap);
        //同时删除会员卡
        requestMap.put("cardStatus", CommonConst.MEMBERCARD_STATUS_DELETE);
        shopMemberCardService.delShopMemberCardByIds(requestMap);
        return num;
    }

    @Override
    public ShopMemberDto addShopMember(ShopMemberDto shopMemberDto, Integer validator) {
        try {
            // 校验数据
            switch (validator) {
                case 0:
                    this.shopMemberCommonValidate(shopMemberDto);
                    // 验证姓名是否为空
                    if (shopMemberDto.getName() == null || shopMemberDto.getName().trim().length() < 1) {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_REQUIRED_USERNAME);
                    }
                    break;
                case 1:
                    this.shopMemberCoreInfoValidate(shopMemberDto);
                    break;
                default:
                    break;
            }
            /* 验证推荐信息 */
            String referType = shopMemberDto.getRefeType() == null ? 1 + "" : shopMemberDto.getRefeType().toString();
            String referUser = referType.equals("1") ? shopMemberDto.getShopId().toString() : "";

            if (referType.equals("0") && shopMemberDto.getReferMobile() == null) {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "填写推荐人类型为用户时 须填写推荐人手机号");
            }

            if (referType.equals("0")) {
                UserDto referUserDto = userDao.getUserByMobile(shopMemberDto.getReferMobile());

                if (referUserDto == null) {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "推荐人不存在");
                }

                referUser = referUserDto.getUserId().toString();
            }

            // 验证用户名是否为空
//            ShopMemberDto validateDto = new ShopMemberDto();
//            validateDto.setShopId(shopMemberDto.getShopId());
//            validateDto.setMobile(shopMemberDto.getMobile());
//            validateDto.setQq(shopMemberDto.getQq());
//            validateDto.setWeixinId(shopMemberDto.getWeixinId());
//            this.shopMemberExistsValidate(validateDto, null);

            // 验证推荐人
            boolean referFlag = false;
            String veriCode = shopMemberDto.getVeriCode();
            if (StringUtils.isNotEmpty(veriCode)) {
                // 验证验证码
                referFlag = sendSmsService.checkSmsCodeIsOk(shopMemberDto.getMobile().toString(), null, veriCode);
                if (!referFlag) {
                    throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
                }
            }
            // 设置创建时间
            if (shopMemberDto.getCreateTime() == null) {
                shopMemberDto.setCreateTime(new Date());
            }

            UserDto userDto = this.addUserByMobile(shopMemberDto, referFlag);
            if (userDto != null) {
                shopMemberDto.setUserId(userDto.getUserId());
            }
            // 设置店内会员等级
            if (shopMemberDto.getGrade() == null) {
                shopMemberDto.setGrade(0);
            }
            /* 保存店内会员 */
            shopMemberDao.addShopMember(shopMemberDto);
            if (userDto != null && StringUtils.isNotBlank(userDto.getConfPassword()) && userDto.getIsMember() == 1) {
                try {
                    SmsReplaceContent src = new SmsReplaceContent();
                    src.setMobile(userDto.getMobile());
                    src.setCode(userDto.getConfPassword());
                    src.setUsage(CommonConst.USER_REGISTR_SUCCESS);
                    sendSmsService.sendSmsMobileCode(src);
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
//                    e.printStackTrace();
                }
            }

        } catch (ServiceException e1) {
            throw e1;
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new ValidateException(CodeConst.CODE_SYSTEM_ERROR, e.getMessage());
        }
        return shopMemberDto;
    }

    private UserDto addUserByMobile(ShopMemberDto shopMemberDto, boolean active) throws Exception {
        /* 验证平台会员存在性 */
        UserDto userDto = null;
        /* 当手机号不为空时检查平台会员账号是否已存在 ，若不存在则添加平台会员 */
        if (shopMemberDto.getMobile() != null) {
            userDto = userDao.getUserByMobile(shopMemberDto.getMobile() + "");
            if (userDto == null || userDto.getUserId() == null) { // 不存在,添加平台会员
                try {
                    int memberFlag = CommonConst.USER_IS_MEMBER;
                    String referType = null;
                    String refeUser = null;
                    if (active) {
                        referType = String.valueOf(CommonConst.USER_REFERTYPE_SHOP);
                        refeUser = String.valueOf(shopMemberDto.getShopId());
                    } else {
                        memberFlag = CommonConst.USER_TO_ACTIVATE; // 2代表非会员
                    }
                    userDto = memberService.saveUser(shopMemberDto.getMobile() + "", null, null, null,
                            refeUser, referType, null);
                    // ///////////////////////////////////////////////////////////////////////////////////////
                    userDto.setIsMember(memberFlag); // 1代码是会员
                    userDto.setAddress(shopMemberDto.getAddress());
                    // userDto.setBirthday(shopMemberDto.getBirthdayDate());
                    userDto.setCreateTime(shopMemberDto.getCreateTime());
                    if (shopMemberDto.getQq() != null) {
                        userDto.setQqNo(shopMemberDto.getQq() + "");
                    }
                    userDto.setSex(shopMemberDto.getSex());
                    userDto.setWeixinNo(shopMemberDto.getWeixinNo());
                    userDao.updateUser(userDto);
                    DataCacheApi.del(CommonConst.KEY_USER + userDto.getUserId());
                } catch (ValidateException e) { // 已存在平台账号
                    log.debug(e.getMessage(), e);
                    userDto = memberService.getUserByMobile(shopMemberDto.getMobile() + "");
                }

            }

        }
        return userDto;
    }

    @Override
    public Long updateShopMember(ShopMemberDto shopMemberDto) throws Exception {
        // Integer memberId = null;
        //
        boolean referFlag = false;
        String veriCode = shopMemberDto.getVeriCode();
        if (StringUtils.isNotEmpty(veriCode)) {
            // 验证验证码
            referFlag = sendSmsService.checkSmsCodeIsOk(shopMemberDto.getMobile().toString(), null, veriCode);
            if (!referFlag) {
                throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
            }
        }
        ShopMemberDto searchDto = new ShopMemberDto();
        searchDto.setShopId(shopMemberDto.getShopId());
        /*
         * 根据memberId更新 (优先级最高)
         */
        if (shopMemberDto.getMemberId() != null) {
            /* 验证属性*(name,mobile,qq,weixinId)是否存在 */
            ShopMemberDto validatDto = new ShopMemberDto();
            validatDto.setShopId(shopMemberDto.getShopId());
            // mobile存在的话校验mobile
            Long mobile = shopMemberDto.getMobile();
            if (mobile != null) {
                this.shopMemberCommonValidate(shopMemberDto);
                validatDto.setMemberId(shopMemberDto.getMemberId());
                validatDto.setMobile(shopMemberDto.getMobile());
            }
            validatDto.setQq(shopMemberDto.getQq());
            validatDto.setWeixinId(shopMemberDto.getWeixinId());
            this.shopMemberExistsValidate(validatDto, shopMemberDto.getMemberId());
            if (mobile != null) {
                searchDto.setMemberId(shopMemberDto.getMemberId());
                ShopMemberDto existsDto = shopMemberDao.getShopMemberByCoreInfo(searchDto).get(0);
                if (shopMemberDto.getMobile() != null && existsDto.getMobile() == null) { // 只考虑添加手机号的情境
                    UserDto userDto = this.addUserByMobile(shopMemberDto, referFlag);
                    shopMemberDto.setUserId(userDto.getUserId());
                }
            }
            shopMemberDao.updateShopMemberById(shopMemberDto);
            //更新会员卡姓名
            ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
            shopMemberCardDto.setMemberId(shopMemberDto.getMemberId());
            shopMemberCardDto.setName(shopMemberDto.getName());
            shopMemberCardDto.setSex(shopMemberDto.getSex());
            shopMemberCardDto.setBirthday(shopMemberDto.getBirthdayDate());
            shopMemberCardDao.updateShopMemberCard(shopMemberCardDto);
            return shopMemberDto.getMemberId();
           
        }
        // 更新时标志位不能为空
        this.shopMemberCoreInfoValidate(shopMemberDto);
        try {
            this.shopMemberExistsValidate(shopMemberDto, null);
        } catch (ValidateException e) {
            if (CodeConst.CODE_USER_REGISTERED == e.getCode()) {
                if (CodeConst.MSG_VALIDATE_MEMBER_DUP_EXISTS_ERROR.equals(e.getMessage())) { // 根据手机号更新
                    shopMemberDao.updateShopMemberByMobile(shopMemberDto);
                    searchDto.setMobile(shopMemberDto.getMobile());
                    return shopMemberDao.getShopMemberByCoreInfo(searchDto).get(0).getMemberId();

                }
//                else if (CodeConst.MSG_VALIDATE_MEMBER_DUP_WX_EXISTS_ERROR.equals(e.getMessage()))
//                { // 根据微信更新
//                    searchDto.setWeixinId(shopMemberDto.getWeixinId());
//                    ShopMemberDto existsDto = shopMemberDao.getShopMemberByCoreInfo(searchDto).get(0);
//                    if (shopMemberDto.getMobile() != null && existsDto.getMobile() == null)
//                    { // 只考虑添加手机号的情境
//                        this.addUserByMobile(shopMemberDto, referFlag);
//                    }
//                    shopMemberDao.updateShopMemberByWeixin(shopMemberDto);
//
//                    return existsDto.getMemberId();
//                }
//                else if (CodeConst.MSG_VALIDATE_MEMBER_DUP_QQ_EXISTS_ERROR.equals(e.getMessage()))
//                { // 根据qq更新
//                    searchDto.setQq(shopMemberDto.getQq());
//                    ShopMemberDto existsDto = shopMemberDao.getShopMemberByCoreInfo(searchDto).get(0);
//                    if (shopMemberDto.getMobile() != null && existsDto.getMobile() == null)
//                    { // 只考虑添加手机号的情境
//                        UserDto userDto = this.addUserByMobile(shopMemberDto, referFlag);
//                        shopMemberDto.setUserId(userDto.getUserId());
//                    }
//                    shopMemberDao.updateShopMemberByQq(shopMemberDto);
//                    return shopMemberDao.getShopMemberByCoreInfo(searchDto).get(0).getMemberId();
//                }
            }
        }
        return null;
    }

    /**
     * 这个方法验证店内会员的 shopId
     *
     * @param shopMemberDto
     * @throws Exception
     */
    private void shopMemberCommonValidate(ShopMemberDto shopMemberDto) throws Exception {
        CommonValidUtil.validPositLong(shopMemberDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID,
                CodeConst.MSG_FORMAT_ERROR_SHOPID);
        // 手机号是否有效
        boolean mobileValidate = CommonValidUtil.validateMobile(shopMemberDto.getMobile());
        if (!mobileValidate) {
            throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_REQUIRED_MOBILE_VALID);
        }

    }

    /**
     * 这个方法校验给定参数的店内会员是否存在，shopId为必填的，但这里不会校验空注意，这里会采用或的逻辑，即dto里的属性有一个在店内会员里存在，
     * 即是存在的
     *
     * @param memberId传入则会对比查出的memberId ，如果相同，不视为重复存在
     * @param shopMemberDto
     */
    public void shopMemberExistsValidate(ShopMemberDto shopMemberDto, Long memberId) {

        shopMemberDto.setMemberStatus(CommonConst.MEMBER_STATUS_DELETE);
        List<ShopMemberDto> searchResults = shopMemberDao.searchShopMbByShopIdAndInfos(shopMemberDto);
        Long mobile = shopMemberDto.getMobile();
        Long qq = shopMemberDto.getQq();
        String weixinId = shopMemberDto.getWeixinId();
        ValidateException mobileDuplication = null;
        ValidateException weixinDuplication = null;
        ValidateException qqDuplication = null;
        if (searchResults == null || searchResults.size() == 0) {
            return;
        }
        for (ShopMemberDto temp : searchResults) {
            if (temp.getMemberId().equals(memberId)) {
                continue;
            }
            if (mobile != null && mobile.equals(temp.getMobile())) {
                mobileDuplication = new ValidateException(CodeConst.CODE_USER_REGISTERED,
                        CodeConst.MSG_VALIDATE_MEMBER_DUP_EXISTS_ERROR);
            }
//            else if (qq != null && qq.equals(temp.getQq()))
//            {
//                qqDuplication = new ValidateException(CodeConst.CODE_USER_REGISTERED,
//                        CodeConst.MSG_VALIDATE_MEMBER_DUP_QQ_EXISTS_ERROR);
//            }
//            else if (weixinId != null && weixinId.equals(temp.getWeixinId()))
//            {
//                weixinDuplication = new ValidateException(CodeConst.CODE_USER_REGISTERED,
//                        CodeConst.MSG_VALIDATE_MEMBER_DUP_WX_EXISTS_ERROR);
//            }
        }
        /* 按优先级抛出异常 */
        if (mobileDuplication != null) {
            throw mobileDuplication;
        }
//        else if (weixinDuplication != null)
//        {
//            throw weixinDuplication;
//        }
//        else if (qqDuplication != null)
//        {
//            throw qqDuplication;
//        }

    }

    /*
     * private static class ResultMsg{ private boolean result = false; private
     * String msg = null; public boolean isResult() { return result; } public
     * void setResult(boolean result) { this.result = result; } public String
     * getMsg() { return msg; } public void setMsg(String msg) { this.msg = msg;
     * }
     * 
     * }
     */

    /**
     * 对excel中传过来的数据进行验证,验证shopId不能为空，mobile,qq,微信不能全为空
     *
     * @param shopMemberDto
     */
    private void shopMemberCoreInfoValidate(ShopMemberDto shopMemberDto) throws Exception {
        try {
            this.shopMemberCommonValidate(shopMemberDto);
        } catch (Exception e) { // 手机号无效
            if (shopMemberDto.getQq() == null
                    && (shopMemberDto.getWeixinId() == null || shopMemberDto.getWeixinId().trim() == "")) { // 微信号，qq号都为空
                if (shopMemberDto.getMobile() == null || shopMemberDto.getMobile().toString().equals("")) { // 手机号无效
                    throw new ValidateException(CodeConst.CODE_USER_REGISTERED, CodeConst.MSG_REQUIRED_CORE_INFO_VALID);
                } else {
                    throw e;
                }
            }
        }
    }

    @Override
    public Integer queryShopMemberCount(Map<String, Object> requestMap) throws Exception {
    	String birthdayStartDate = (String)requestMap.get("birthdayStartDate");
        String birthdayEndDate = (String)requestMap.get("birthdayEndDate");
        if(null != birthdayStartDate && null != birthdayEndDate){
			Date startTimeDate = DateUtils.parse(birthdayStartDate,  DateUtils.DATE_FORMAT);
			Date endTimeDate = DateUtils.parse(birthdayEndDate,  DateUtils.DATE_FORMAT);
			if(endTimeDate.getYear()-startTimeDate.getYear() == 1){
				requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
				requestMap.put("continueStartTime", "2000-12-31");
				requestMap.put("continueEndTime", "2000-01-01");
				requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
			}else if(endTimeDate.getYear()-startTimeDate.getYear() == 0){
				requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
				requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
			}else  if(endTimeDate.getYear()-startTimeDate.getYear() > 1){
				requestMap.put("birthdayStartDate", null);
				requestMap.put("birthdayEndDate", null);
			}
		}else if(null != birthdayStartDate){
			requestMap.put("birthdayStartDate", "2000-"+birthdayStartDate.substring(5));
		}else if(null!=birthdayEndDate){
			requestMap.put("birthdayEndDate", "2000-"+birthdayEndDate.substring(5));
		}
        return shopMemberDao.queryShopMemberCount(requestMap);
    }

    @Override
    public Map<String, String> editShopMember(ShopMemberDto shopMemberDto) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (shopMemberDto.getMemberId() == null) {  //如果memberId为空则为添加店内会员

            if (shopMemberDto.getShopId() != null && shopMemberDto.getMobile() != null) {   //检查是否是已删除的原有会员
                String mobile = String.valueOf(shopMemberDto.getMobile());
                ShopMemberDto shopMemberDtoDetail = shopMemberDao.getShopMbByMobileAndShopId(mobile, shopMemberDto.getShopId(), CommonConst.MEMBER_STATUS_DELETE);
                if (shopMemberDtoDetail != null) {  //如果是已删除的原有会员则校验码必不为空
                    if (StringUtils.isBlank(shopMemberDto.getVeriCode())) {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_VALIDATE_MEMBER_DUP_EXISTS_ERROR);
                    }
                    this.memberService.saveUser(mobile, null, shopMemberDto.getVeriCode(), null, String.valueOf(shopMemberDto.getShopId()), String.valueOf(CommonConst.USER_RECOMMAND_BY_SHOP), Integer.valueOf(CommonConst.REGISTER_TYPE_COLLECT));
                    resultMap.put("msg", "录入店铺会员信息接口成功！");
                    resultMap.put("memberId", shopMemberDtoDetail.getMemberId() + "");
                    return resultMap;

                }
            }
            // 测试添加店内会员，若成功则为添加
            ShopMemberDto dto = this.addShopMember(shopMemberDto, 1);
            //保存店内会员卡信息
            this.addShopMemberCard(shopMemberDto);
            if(dto.getMobile() != null && dto.getShopId() != null){
            	shopMemberLevelService.upgrateShopMemberLevel(dto.getMobile().toString(), dto.getShopId(), 1);
            }
            resultMap.put("msg", "录入店铺会员信息接口成功！");
            resultMap.put("memberId", dto.getMemberId() + "");
            return resultMap;
        }//memberId不为空则为更新会员
        /* 更新店内会员 */
        Long memberId = this.updateShopMember(shopMemberDto);
        resultMap.put("msg", "编辑会员信息成功");
        resultMap.put("memberId", memberId + "");
        return resultMap;
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @param shopMemberDto
     * @return void [返回类型说明]
     * @throws Exception [参数说明]
     * @throws throws    [违例类型] [违例说明]
     * @author shengzhipeng
     * @date 2016年8月9日
     * @see [类、类#方法、类#成员]
     */
    private void addShopMemberCard(ShopMemberDto shopMemberDto) throws Exception {
        if (shopMemberDto != null) {
            String mobile = shopMemberDto.getMobile() == null ? "" : shopMemberDto.getMobile().toString();
            boolean isExist = false;
            Long memberId = shopMemberDto.getMemberId();
            if (null != memberId) {
                isExist = shopMemberCardService.checkCardExistByMid(memberId);
            } else {
                isExist = shopMemberCardService.checkCardExist(shopMemberDto.getShopId(), mobile);
            }
            if (!isExist) {
                ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
                shopMemberCardDto.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);
                shopMemberCardDto.setMobile(mobile);
                shopMemberCardDto.setShopId(shopMemberDto.getShopId());
                shopMemberCardDto.setSex(shopMemberDto.getSex());
                shopMemberCardDto.setName(shopMemberDto.getName());
                shopMemberCardDto.setOperaterId(null);
                shopMemberCardDto.setBirthday(shopMemberDto.getBirthdayDate());
                shopMemberCardDto.setMemberId(memberId);
                Integer cardId = shopMemberCardService.insertShopMemberCard(shopMemberCardDto);
            }
        }

    }

    /**
     * 验证该会员是否 为店铺会员，
     *
     * @param shopId：店铺ID
     * @param mobile：手机号
     * @param isUpdatePurchaseNo:是否更新店铺会员的消费次数
     * @return
     * @throws Exception
     */
    @Override
    public boolean isShopMemberValify(Long shopId, String mobile, BigDecimal purchaseMoney, boolean isUpdatePurchaseNo) throws Exception {
        if (shopId == null || StringUtils.isBlank(mobile) || purchaseMoney == null) {
            return false;
        }
        IShopMemberDao shopMemberDao = BeanFactory.getBean(IShopMemberDao.class);
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("shopId", shopId);
        requestMap.put("mobile", mobile);
        requestMap.put("purchaseMoney", purchaseMoney);
        int updateNum = 0;
        if (isUpdatePurchaseNo) {
            //更新店铺会员消费次数
            updateNum = shopMemberDao.updateShopMemberPurchaseNum(requestMap);
        }
        return updateNum > 0 ? true : false;
    }

    @Override
    public ShopMemberDto getShopMemberDetailByMap(Map<String, Object> map) throws Exception {
        ShopMemberDto shopMemberDto = shopMemberDao.getShopMemberByIdMap(map);
        if (null != shopMemberDto) {
            UserShopCouponDto userShopCouponDto = new UserShopCouponDto();
            userShopCouponDto.setShopId(shopMemberDto.getShopId());
            userShopCouponDto.setShopMemeberId(shopMemberDto.getMemberId().intValue());
            int total = 0;
            userShopCouponDto.setCouponStatus(CommonConst.USER_SHOP_COUPON_UNUSED);//未使用
            int count = userShopCouponDao.getUserShopCouponCount(userShopCouponDto);
            shopMemberDto.setUnUsedShopCouponNum(count);
            total = count;
            userShopCouponDto.setCouponStatus(CommonConst.USER_SHOP_COUPON_USED);//已使用
            count = userShopCouponDao.getUserShopCouponCount(userShopCouponDto);
            shopMemberDto.setUsedShopCouponNum(count);
            total = total + count;
            userShopCouponDto.setCouponStatus(CommonConst.USER_SHOP_COUPON_EXPIRE);//过期
            count = userShopCouponDao.getUserShopCouponCount(userShopCouponDto);
            shopMemberDto.setExpireShopCouponNum(count);
            total = total + count;
            shopMemberDto.setUserShopCouponNum(total);
        }
        return shopMemberDto;
    }

    @Override
    public Map<String, Object> getShopMemberStat(Map<String, Object> queryParams) throws Exception {
        log.debug("开始查询店内会员数量统计信息:" + queryParams);
        Map<String, Object> rs = new HashMap<String, Object>();
        Long shopId = (Long) queryParams.get("shopId");
        //    Date startDate = (Date)queryParams.get("startDate");
        //  Date endDate = (Date)queryParams.get("endDate");
        //查询指定时间段的会员增加数量
        int timeZonesAddNum = shopMemberDao.countShopMemberByTime(queryParams);
        //查询会员总数
        queryParams.clear();
        queryParams.put("shopId", shopId);
        int memberTotalNum = shopMemberDao.countShopMemberByTime(queryParams);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        // Date todayStart =
        //查询今天增加的会员数量
        queryParams.put("startDate", dateFormat.parse(year + "-" + month + "-" + date));
        queryParams.put("endDate", new Date());
        int todayAddNum = shopMemberDao.countShopMemberByTime(queryParams);
        //查询昨日增加会员数量
        queryParams.put("startDate", dateFormat.parse(year + "-" + month + "-" + (date - 1)));
        queryParams.put("endDate", dateFormat.parse(year + "-" + month + "-" + date));
        int yesterdayAddNum = shopMemberDao.countShopMemberByTime(queryParams);
        rs.put("timeZonesAddNum", timeZonesAddNum);
        rs.put("memberTotalNum", memberTotalNum);
        rs.put("todayAddNum", todayAddNum);
        rs.put("yesterdayAddNum", yesterdayAddNum);
        return rs;
    }

    //    public static void main(String[]args){
//        System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//    }

    @Override
    public Map<String, Object> getShopMemberStatDetail(Map<String, Object> queryParams) throws Exception {

        Integer rowCount = shopMemberDao.countShopMemberStatDetail(queryParams);
        Map<String, Object> rs = new HashMap<String, Object>();
        Object obj = queryParams.get("pNo");
        Integer pNo = obj == null ? 1 : (Integer) obj;
        if (rowCount > 0) {
            obj = queryParams.get("pSize");
            Integer pSize = obj == null ? 10 : (Integer) obj;
            queryParams.put("start", pSize * (pNo - 1));
            queryParams.put("end", pSize * pNo);
            List<ShopMemberStatInfo> tempRs = shopMemberDao.getShopMemberStatDetail(queryParams);
            rs.put("lst", tempRs);
        }
        rs.put("pNo", pNo);
        rs.put("rCount", rowCount);
        return rs;
    }

    @Override
    public List<Object> getShopMemberInfo(Long shopId, Integer detail, String params) throws Exception {
        //是否返回相应会员列表
        boolean getMemberList = 1 == detail.intValue();
        //是否是查询主界面条目
        boolean seachAll = false;
        Integer pNo = 1;
        Integer pSize = 10;
        List<Object> result = new LinkedList<Object>();
        if (StringUtils.isNotBlank(params)) {
            Map<String, String> paramMap = JacksonUtil.simpleParseJson2Map(params.trim());
            if (paramMap == null || paramMap.isEmpty() || StringUtils.isBlank(paramMap.get("type")))  //进一步确认是否只是查询主界面
            {
                seachAll = true;
            } else {
                String pNoStr = paramMap.get("pNo");
                pNo = StringUtils.isBlank(pNoStr) ? pNo : Integer.valueOf(pNoStr.trim());

                String pSizeStr = paramMap.get("pSize");
                pSize = StringUtils.isBlank(pSizeStr) ? pSize : Integer.valueOf(pSizeStr.trim());

                String typeStr = paramMap.get("type").trim();
                String ranges = paramMap.get("ranges");
                List<Map<String, String>> tempCondtion = null;
                if(StringUtils.isNotBlank(ranges)) {
                    ObjectMapper mapper = new ObjectMapper();
                    JavaType temp = mapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
                    JavaType finalType = mapper.getTypeFactory().constructParametricType(List.class, temp);
                    tempCondtion = mapper.readValue(ranges.trim(), finalType);
                }
                Integer type = Integer.valueOf(typeStr);
                Map<String, Object> searchResult = this.searchMemberContents(type, tempCondtion, shopId, detail, pNo, pSize);
                result.add(searchResult);
            }
        } else    //否则只是查询主界面
        {
            seachAll = true;
        }
        if (seachAll) {
            detail = 2;
            List<Map<String, String>> rangeParams = null;
            Map<String, String> valueMap = null;
            String[] groupArray = {"CONSUMENUM_GROUP","CONSUMEMONEY_GROUP","LASTESTCONSUME_GROUP"};
            ConfigQueryCondition configQueryCondition = new ConfigQueryCondition();
            for (int i = 0; i < groupArray.length; i++) {
            	rangeParams = new ArrayList<Map<String,String>>();
            	String[] groupArray1= {groupArray[i]};
            	configQueryCondition.setConfigGroups(groupArray1);
            	configQueryCondition.setOrderBy(1);
            	configQueryCondition.setOrderByMode(1);
            	configQueryCondition.setBizId(shopId);
            	configQueryCondition.setBizType(CommonConst.BIZ_TYPE_IS_SHOP);
            	List<ConfigDto> configDtos= configDao.queryForConfig(configQueryCondition,null);
            	if(configDtos != null && configDtos.size() > 0){
            		for (ConfigDto configDto : configDtos) {
            			valueMap = new HashMap<String, String>();
						if(configDto.getConfigValue().contains(",")){
							String[] configValues = configDto.getConfigValue().split(",");
							valueMap.put("from", configValues[0]);
							valueMap.put("to", configValues[1]);
						}else{
							valueMap.put("from", configDto.getConfigValue());
						}
						rangeParams.add(valueMap);
					}
            	}
            	result.add(this.searchMemberContents(i+1, rangeParams, shopId, detail, pNo, pSize)); 
			}
            result.add(this.searchMemberContents(4, null, shopId, detail, pNo, pSize));
           
           /* result.add(this.searchMemberContents(1, null, shopId, detail, pNo, pSize));
            result.add(this.searchMemberContents(2, null, shopId, detail, pNo, pSize));
            result.add(this.searchMemberContents(3, null, shopId, detail, pNo, pSize));
            result.add(this.searchMemberContents(4, null, shopId, detail, pNo, pSize));*/
        }
        return result;
    }

    private void dealMemberRelationShip(List<Map<String, Object>> dataBaseMap){
        if(null == dataBaseMap || dataBaseMap.isEmpty()){
            return;
        }
        UserDto userDto = null;
        Iterator<Map<String, Object>> it = dataBaseMap.iterator();
        Map<String, Object> temp = null;
        String mobile = null;
        while (it.hasNext()){
            temp = it.next();
            mobile = temp.get("mobile") + "";
            if(StringUtils.isNotBlank(mobile))
            {
                try {
                    userDto = userDao.getUserByMobile(mobile);
                    if (userDto != null && userDto.getIsMember() == CommonConst.USER_IS_MEMBER) {
                        if (temp.get("shopId") != null && userDto.getReferShopId() != null
                                && userDto.getReferShopId().equals(temp.get("shopId"))) {
                            temp.put("isUser", CommonConst.IS_SHOP_REF_USER);
                        } else {
                            temp.put("isUser", CommonConst.IS_USER);
                        }
                    } else {
                        temp.put("isUser", CommonConst.IS_NOT_USER);
                    }
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                    temp.put("isUser", CommonConst.IS_NOT_USER);
                }
            } else {
                temp.put("isUser", CommonConst.IS_NOT_USER);
            }
            temp.remove("shopId");
        }
    }

    private Map<String, Object> searchMemberContents(boolean getDetail, Map<String, Object> searchCondition, Integer type) {

        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> dataBaseMap = null;
        int count = 0;
        switch (type) {
            case 1:
                count = shopMemberDao.countMemberByConsumeCount(searchCondition);
                if (count > 0 && getDetail && (((Integer)searchCondition.get("start")) < count)) {
                    dataBaseMap = shopMemberDao.queryMemberByConsumeCount(searchCondition);
                    this.dealMemberRelationShip(dataBaseMap);
                    result.put("details", dataBaseMap);
                }
                break;
            case 2:
                count = shopMemberDao.countMemberByConsumeAmount(searchCondition);
                //数量
                result.put("rowCounts", count);
                if (count > 0 && getDetail && (((Integer)searchCondition.get("start")) < count)) {
                    dataBaseMap = shopMemberDao.queryMemberByConsumeAmount(searchCondition);
                    this.dealMemberRelationShip(dataBaseMap);
                    result.put("details", dataBaseMap);
                }
                break;
            case 3:
                if (searchCondition.get("searchWithout") != null) {   //搜索不在某个范围内
                    count = shopMemberDao.countMemberByWithoutConsumeTime(searchCondition);
                    //数量
                    if (count > 0 && getDetail && (((Integer)searchCondition.get("start")) < count)) {
                        dataBaseMap = shopMemberDao.queryMemberByWithoutConsumeTime(searchCondition);
                        this.dealMemberRelationShip(dataBaseMap);
                        result.put("details", dataBaseMap);
                    }
                } else {
                    count = shopMemberDao.countMemberBylastConsumeTime(searchCondition);
                    //数量
                    if (count > 0 && getDetail && (((Integer)searchCondition.get("start")) < count)) {
                        dataBaseMap = shopMemberDao.queryMemberBylastConsumeTime(searchCondition);
                        this.dealMemberRelationShip(dataBaseMap);
                        result.put("details", dataBaseMap);
                    }
                }
                break;
            case 4:
                count = shopMemberDao.countMemberByWithoutConsume(searchCondition);
                if (count > 0 && getDetail && (((Integer)searchCondition.get("start")) < count)) {
                    dataBaseMap = shopMemberDao.queryMemberByWithoutConsume(searchCondition);
                    this.dealMemberRelationShip(dataBaseMap);
                    result.put("details", dataBaseMap);
                }
                //数量
                result.put("rowCounts", count);
                break;
            default:
                log.info("不受支持的type值:" + type);
            break;
        }
        //数量
        result.put("rowCounts", count);
        result.put("details", dataBaseMap);
        return result;
    }


    private void initQueryParams(Integer start, Integer end, boolean getDetail, Map<String, Object> searchCondtions, Long shopId) {
        searchCondtions.put("shopId", shopId);
        if (getDetail) {
            searchCondtions.put("start", start);
            searchCondtions.put("end", end);
        }
    }


    /**
     * 获取查询范围参数
     *
     * @param type        查询类型，1-按消费次数，2-按消费金额，3-最近一次消息，4-未消费过的会员
     * @param rangeParams
     * @return
     */
    private Map<String, Object> searchMemberContents(Integer type, List<Map<String, String>> rangeParams, Long shopId, Integer detail, Integer pNo, Integer pSize) throws Exception {
        List<Map<String, Object>> rs = new LinkedList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        //是否获得会员列表
        boolean getDetail = 1 == detail;
        int start = 0;
        int end = 0;
        if (getDetail) {
            start = (pNo - 1) * pSize;
            end = pNo * pSize;
        }
        Map<String, Object> tempResult = null;
        switch (type) {
            case 1:     //按消费次数统计参数
                if (rangeParams == null || rangeParams.isEmpty()) {           //取默认的分组
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 1);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "1");
                    rs.add(tempResult);

                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 3);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "3");
                    rs.add(tempResult);

                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 5);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "5");
                    rs.add(tempResult);

                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 10);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "10");
                    rs.add(tempResult);
                } else {//取自定义分组内
                    Iterator<Map<String, String>> iterator = rangeParams.iterator();
                    Map<String, String> temp = null;
                    Map<String, Object> tempRs = null;
                    String fromStr = null;
                    String toStr = null;
                    while (iterator.hasNext()) {
                        tempRs = new HashMap<String, Object>();
                        temp = iterator.next();
                        fromStr = temp.get("from");
                        fromStr = (fromStr == null || fromStr.trim().equals("-1")) ? "0" : fromStr.trim();
                        tempRs.put("from", Integer.valueOf(fromStr));
                        toStr = temp.get("to");
                        if (!(toStr == null || toStr.trim().equals("-1"))) {
                            tempRs.put("to", Integer.valueOf(toStr.trim()));
                        }
                        tempRs.put("shopId", shopId);
                        this.initQueryParams(start, end, getDetail, tempRs, shopId);
                        tempResult = this.searchMemberContents(getDetail, tempRs, type);
                        tempResult.put("to", toStr);
                        tempResult.put("from", fromStr);
                        rs.add(tempResult);
                    }
                }
                break;
            case 2: //按消费金额统计参数
                if (rangeParams == null || rangeParams.isEmpty()) {           //取默认的分组
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 0d);
                    tempMap.put("to", 50d);
                    tempMap.put("shopId", shopId);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("to", "50");
                    tempResult.put("from", "0");
                    rs.add(tempResult);

                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 50d);
                    tempMap.put("to", 500d);
                    tempMap.put("shopId", shopId);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "50");
                    tempResult.put("to", "500");
                    rs.add(tempResult);

                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 500d);
                    tempMap.put("to", 1000d);
                    tempMap.put("shopId", shopId);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "500");
                    tempResult.put("to", "1000");
                    rs.add(tempResult);


                    tempMap = new HashMap<String, Object>();
                    tempMap.put("from", 1000d);
                    tempMap.put("shopId", shopId);
                    this.initQueryParams(start, end, getDetail, tempMap, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempMap, type);
                    tempResult.put("from", "1000");
                    rs.add(tempResult);
                } else {//取自定义分组内
                    Iterator<Map<String, String>> iterator = rangeParams.iterator();
                    Map<String, String> temp = null;
                    Map<String, Object> tempRs = null;
                    String fromStr = null;
                    String toStr = null;
                    while (iterator.hasNext()) {
                        tempRs = new HashMap<String, Object>();
                        temp = iterator.next();
                        fromStr = temp.get("from");
                        fromStr = (fromStr == null || fromStr.trim().equals("-1")) ? "0" : fromStr.trim();
                        tempRs.put("from", Double.valueOf(fromStr));
                        toStr = temp.get("to");
                        if (!(toStr == null || toStr.trim().equals("-1"))) {
                            tempRs.put("to", Double.valueOf(toStr.trim()));
                        }
                        tempRs.put("shopId", shopId);
                        this.initQueryParams(start, end, getDetail, tempRs, shopId);
                        tempResult = this.searchMemberContents(getDetail, tempRs, type);
                        tempResult.put("to", toStr);
                        tempResult.put("from", fromStr);
                        rs.add(tempResult);
                    }
                }
                break;
            case 3:      //按最近一次消费时间统计参数
                Calendar calendar = Calendar.getInstance();
                int days = calendar.get(Calendar.DAY_OF_YEAR);
                if (rangeParams == null || rangeParams.isEmpty()) {           //取默认的分组
                    Map<String, String> tempMap = new HashMap<String, String>();
                    rangeParams = new LinkedList<Map<String, String>>();
//                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("from", "0");
                    tempMap.put("to", "3");
                    rangeParams.add(tempMap);

                    tempMap = new HashMap<String, String>();
                    tempMap.put("from", "0");
                    tempMap.put("to", "7");
                    rangeParams.add(tempMap);

                    tempMap = new HashMap<String, String>();
                    tempMap.put("from", "0");
                    tempMap.put("to", "30");
                    rangeParams.add(tempMap);


                    tempMap = new HashMap<String, String>();
                    //一段时间内未消费时，from < to
                    tempMap.put("from", "30");
                    tempMap.put("to", "-2");
                    rangeParams.add(tempMap);
                }
                Iterator<Map<String, String>> iterator = rangeParams.iterator();
                Map<String, String> temp = null;
                Map<String, Object> tempRs = null;
                String fromStr = null;
                String toStr = null;
                while (iterator.hasNext()) {
                    tempRs = new HashMap<String, Object>();
                    temp = iterator.next();
                    fromStr = temp.get("from");
                    fromStr = (fromStr == null || fromStr.trim().equals("-1")) ? "0" : fromStr.trim();
                    Date toDate = null;
                    int fromInt = Integer.valueOf(fromStr);
                    if (fromInt == 0) {
                        toDate = new Date();
                    } else {
                        calendar.set(Calendar.DAY_OF_YEAR, days - fromInt + 1);
                        toDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
                    }
                    tempRs.put("to", toDate);
                    toStr = temp.get("to");
                    if (!(toStr == null || toStr.trim().equals("-1"))) {
                        calendar.set(Calendar.DAY_OF_YEAR, days - Integer.valueOf(toStr.trim()));
                        Date fromDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
                        tempRs.put("from", fromDate);
                        if (Integer.valueOf(toStr.trim()) < -1) { //to小于-1小则查询不在该范围内的
                            tempRs.put("searchWithout", 1);
                        }
                    }
                    tempRs.put("shopId", shopId);
                    this.initQueryParams(start, end, getDetail, tempRs, shopId);
                    tempResult = this.searchMemberContents(getDetail, tempRs, type);
                    tempResult.put("to", toStr);
                    tempResult.put("from", fromStr);
                    rs.add(tempResult);
                }
                break;
            case 4:     //从未消费的会员
                Map<String, Object> tempRs4 = new HashMap<String, Object>();
                this.initQueryParams(start, end, getDetail, tempRs4, shopId);
                tempResult = this.searchMemberContents(getDetail, tempRs4, type);
                tempResult.put("to", "0");
                tempResult.put("from", "0");
                rs.add(tempResult);
                break;
            default:
                log.info("参数type值不受支持：" + type);
                break;
        }
        result.put("type", type);
        result.put("lst", rs);
        return result;
    }

    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR, -3);
        System.out.println(dateFormat.format(calendar.getTime()));

    }

	@Override
	public Map<String, Object> getMemberConsumerStat(Map<String, Object> searchParams) {
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer rCount = shopMemberDao.getMemberConsumerStatCount(searchParams);
		List<Map> list = new ArrayList<Map>();
		if(rCount > 0) {
			list = shopMemberDao.getMemberConsumerStat(searchParams);
		}
		resultMap.put("lst", list);
		resultMap.put("rCount", rCount);
		return resultMap;
	}

	@Override
	public int updateShopMemberExceptDelAndCurrentMonth(
			ShopMemberDto shopMemberDto) {
		return shopMemberDao.updateShopMemberExceptDelAndCurrentMonth(shopMemberDto);
	}

	
    
}
