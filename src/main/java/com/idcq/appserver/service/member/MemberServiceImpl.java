package com.idcq.appserver.service.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dao.common.IConfigDao;
import com.idcq.appserver.dao.user.*;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.user.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.BankInfo;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.RewardsEnum;
import com.idcq.appserver.dao.bank.IBankCardDao;
import com.idcq.appserver.dao.cashcoupon.ICashCouponDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.coupon.ICouponDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.pay.IWithdrawDao;
import com.idcq.appserver.dao.region.ICitiesDao;
import com.idcq.appserver.dao.region.IProvinceDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.region.ITownDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.dto.common.MobileAttributionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ConfigPropertiesInitListener;
import com.idcq.appserver.service.ShopMemberLevel.IShopMemberLevelService;
import com.idcq.appserver.service.bill.IUserBillService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.MobileUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RandomCode;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.SmsVeriCodeUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
import com.idcq.appserver.wxscan.MD5Util;

/**
 * 会员service
 *
 * @author Administrator
 * @date 2015年3月3日
 * @time 下午5:12:05
 */
@Service public class MemberServiceImpl implements IMemberServcie
{
    @Autowired public IUserDao userDao;

    @Autowired public IBankCardDao bankCardDao;

    @Autowired public IUserAccountDao userAccountDao;

    @Autowired public IUserBillDao userBillDao;

    @Autowired public IPacketDao packetDao;

    @Autowired public IUserAddressDao userAddressDao;

    @Autowired public ICitiesDao citiesDao;

    @Autowired public IRegionDao regionDao;

    @Autowired public IProvinceDao provinceDao;

    @Autowired public ITownDao townDao;

    @Autowired private ICouponDao couponDao;

    @Autowired private ICashCouponDao cashCouponDao;

    @Autowired private IUserShopCommentDao userShopCommentDao;

    @Autowired private IUserGoodsCommentDao userGoodsCommentDao;

    @Autowired public IShopDao shopDao;

    @Autowired public IGoodsDao goodDao;

    @Autowired public IPushUserTableDao pushUserDao;

    @Autowired public IUserOrderCommentDao userOrderCommentDao;

    @Autowired public IOrderDao orderDao;

    @Autowired public IPayServcie payService;

    @Autowired private IWithdrawDao withdrawDao;

    @Autowired private ITransaction3rdDao transaction3rdDao;

    @Autowired private IShopAccountDao shopAccountDao;

    @Autowired private ISendSmsService sendSmsService;

    @Autowired private IAttachmentRelationDao attachmentRelationDao;

    @Autowired private IAgentDao agentDao;

    @Autowired private IShopMemberDao shopMemberDao;

    @Autowired private ILevelService levelService;

    @Autowired private IShopMemberCardService shopMemberCardService;

    @Autowired private IUserBillService userBillService;

    @Autowired private IShopMemberLevelService shopMemberLevelService;

    @Autowired private IConfigDao configDao;

    @Autowired private IBranchOfficeDao branchOfficeDao;

    private final Log logger = LogFactory.getLog(getClass());

    public int queryUserExists(String mobile) throws Exception
    {
        return this.userDao.queryUserExistsByMobile(mobile);
    }

    public String getPasswordByMobile(String mobile) throws Exception
    {
        if (StringUtils.isBlank(mobile))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        }
        return userDao.getPasswordByMobile(mobile);
    }

    public UserDto getUserByMobile(String mobile) throws Exception
    {
        UserDto d = userDao.getUserByMobile(mobile);
        return d;
    }

    public UserDto getUserByMobileFromDB(String mobile) throws Exception
    {
        UserDto d = userDao.getUserByMobileFromDB(mobile);
        return d;
    }

    public UserDto getUserByWeiXinNo(String openId) throws Exception
    {
        UserDto userDto = userDao.getUserByWeiXinNo(openId);
        return userDto;
    }

    public UserDto getUserByWeiXinNoFromDB(String openId) throws Exception
    {
        UserDto userDto = userDao.getUserByWeiXinNoFromDB(openId);
        return userDto;
    }

    private UserDto buildUserDtoForWeiXinRegister(String openId)
    {
        UserDto userDto = new UserDto();
        userDto.setUserType(CommonConst.USER_TYPE_IS_NOT_MEMBER);
        userDto.setUserType2(CommonConst.USER_TYPE_IS_NOT_MEMBER);
        userDto.setIsMember(CommonConst.USER_IS_NOT_MEMBER);
        userDto.setStatus(CommonConst.USER_NORMAL_STATUS);
        Random random = new Random();
        userDto.setMobile(getRandomMobile(random));
        userDto.setPassword(MD5Util.getMD5Str(getRandomPassword(random)));
        userDto.setCreateTime(new Date());
        userDto.setRegisterType(String.valueOf(CommonConst.USER_REGISTER_TYPE_IS_NOT_MEMBER));
        userDto.setWeixinNo(openId);
        return userDto;
    }

    private Integer getRandomNum(Random random)
    {
        Integer randomNum = (int) random.nextInt(10);
        return randomNum;
    }

    private String getRandomMobile(Random random)
    {
        StringBuilder mobileBuilder = new StringBuilder();
        for (int i = 0; i < 11; i++)
        {
            mobileBuilder.append(getRandomNum(random));
        }
        return mobileBuilder.toString();
    }

    private String getRandomPassword(Random random)
    {
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            passwordBuilder.append(getRandomNum(random));
        }
        return passwordBuilder.toString();
    }

    public UserDto saveUserFromWeiXin(String openId) throws Exception
    {
        UserDto userDto = buildUserDtoForWeiXinRegister(openId);
        return this.userDao.saveUser(userDto);
    }

    public UserDto registerUserFromWeiXinBind(String openId, String mobile, Long referUserId) throws Exception
    {
        UserDto userDto = buildUserDto(mobile, null, CommonConst.USER_REGISTER_TYPE_WEIXIN_PUBLIC, openId);
        if (null != referUserId)
        {
            userDto.setReferType(CommonConst.USER_REFERTYPE_MEMBER);
            userDto.setReferUserId(referUserId);
        }
        this.userDao.saveUser(userDto);
        sendPassWordToUser(mobile, userDto.getConfPassword());
        createAccountForUser(userDto);
        return userDto;
    }

    private void sendPassWordToUser(String mobile, String password)
    {
        try
        {
            SmsReplaceContent src = new SmsReplaceContent();
            src.setMobile(mobile);
            src.setCode(password);
            src.setUsage(CommonConst.USER_REGISTR_SUCCESS);
            sendSmsService.sendSmsMobileCode(src);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("发送短信验证码失败", e);
        }
    }

    public void createAccountForUser(UserDto user) throws Exception
    {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountStatus(CommonConst.ACCOUNT_NORMAL_STATUS);
        userAccountDto.setCreateTime(new Date());
        userAccountDto.setTelephone(user.getMobile());
        userAccountDto.setUserId(user.getUserId());
        userAccountDto.setAmount(0d);
        userAccountDto.setUserRole(CommonConst.MEMBER);

        this.userAccountDao.saveAccount(userAccountDto);
    }

    public UserDto saveUser(String mobile, String password, String veriCode, String refecode, String refeUser,
            String refeType, Integer registerType) throws Exception
    {
        if (StringUtils.isNotBlank(veriCode))
        {
            //如果验证码不能为空，则需要验证短信验证码
            boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode);
            if (!flag)
            {
                throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
            }
        }
        UserDto userDB = this.userDao.getUserByMobile(mobile);
        if (userDB != null)
        {
            if (userDB.getIsMember() == CommonConst.USER_IS_MEMBER)
            {
                throw new ValidateException(CodeConst.CODE_MOBILE_REGISTERED, CodeConst.MSG_MOBILE_REGISTERED);
            }
            else
            {
                //推荐人信息
                userDB.setIsMember(CommonConst.USER_IS_MEMBER);
                userDB.setCreateTime(new Date());
                if (StringUtils.isBlank(password))
                {
                    //默认密码为手机号码后6位
                    String randPassWord = mobile.substring(mobile.length() - 6);
                    password = MD5Util.getMD5Str(randPassWord);
                    userDB.setConfPassword(randPassWord);
                }
                userDB.setPassword(password);
                setRefeUserInfo(mobile, refecode, refeUser, userDB, refeType);

                //初始化点等级
                if (userDB.getRebatesLevel() == null)
                {
                    userDB.setRebatesLevel("normal_ratio");
                }
                this.userDao.updateUser(userDB);
                return userDB;
            }

        }

        UserDto user = buildUserDto(mobile, password, registerType, null);
        //推荐人信息
        setRefeUserInfo(mobile, refecode, refeUser, user, refeType);
        // 注册该用户
        this.userDao.saveUser(user);
        //创建用户账户
        createAccountForUser(user);

        return user;
    }

    public static void main(String[] args)
    {
        System.out.println(MD5Util.getMD5Str("123456"));
    }

    public void addShopMember(Long userId, String refeUser, String refeType, String mobile) throws Exception
    {
        if (StringUtils.isNotBlank(refeUser))
        {
            if (String.valueOf(CommonConst.USER_REFERTYPE_SHOP).equals(refeType))
            {
                //店铺推荐
                ShopDto shopDto = shopDao.getShopById(NumberUtil.strToLong(refeUser, "refeUser"));
                CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                //同时将该平台会员添加会店内会员
                userToShopMember(userId, shopDto.getShopId(), mobile);
            }
        }

    }

    /**
     * 构建用户实体对象
     *
     * @param mobile       手机号码
     * @param password     密码，为空随机生成6位密码
     * @param registerType 注册方式，为空默认为4-APP注册
     * @param openId       微信唯一标识
     * @return UserDto 用户对象
     * @author shengzhipeng
     * @date 2016年3月5日
     */
    private UserDto buildUserDto(String mobile, String password, Integer registerType, String openId)
    {
        //默认为激活状态 （应市场要求）
        Integer status = CommonConst.USER_NORMAL_STATUS;
        UserDto user = new UserDto();
        user.setUserName(mobile);
        user.setNikeName(CommonConst.NIKE_NAME_PREFIX + RandomCode.getMedimRandomCode(6));
        user.setMobile(mobile);
        if (StringUtils.isBlank(password))
        {
            String randPassWord = mobile.substring(mobile.length() - 6);
            password = MD5Util.getMD5Str(randPassWord);
            user.setConfPassword(randPassWord);
        }
        user.setPassword(password);
        Date date = new Date();
        user.setCreateTime(date);
        user.setLastUpdateTime(date);
        user.setUserType(CommonConst.USER_TYPE_MEMBER);
        user.setUserType2(CommonConst.USER_TYPE_MEMBER);
        user.setRegisterType(CommonConst.REGISTER_TYPE_APP);
        user.setIsMember(CommonConst.USER_IS_MEMBER);
        if (registerType != null)
        {
            user.setRegisterType(String.valueOf(registerType));
        }
        user.setWeixinNo(openId);
        // 获取手机归属地信息
/*		if(!StringUtils.isBlank(mobile)){
            MobileAttributionDto mad = MobileUtil.getAddressByMobile(mobile);
			if (mad != null) {
				// 城市名称
				String cityName = mad.getCity();
				if (StringUtils.isNotBlank(cityName)) {
					cityName = cityName + "市";
					Map<String, Object> cityMap = citiesDao.getCityInfoByName(cityName);
					if(cityMap != null) {
						Long provinceId = (Long) cityMap.get("provinceId");
						Long cityId = (Long) cityMap.get("cityId");
						user.setProvinceId(provinceId);
						user.setCityId(cityId);
					}
				}
			}
		}*/
        user.setStatus(status);
        user.setRebatesLevel("normal_ratio");
        return user;
    }

    /**
     * 设置推荐人信息，app帮人注册传refecode，h5推荐注册传refeUser
     *
     * @param mobile
     * @param refecode 这个是app帮人注册入口
     * @param refeUser 这个是H5页面注册传值refeType=0,填入UserID或手机号码；refeType=1,填入ShopId。
     * @param user
     * @param refeType 推荐用户类型：0：用户 1：店铺
     * @throws Exception
     */
    private void setRefeUserInfo(String mobile, String refecode, String refeUser, UserDto user, String refeType)
            throws Exception
    {
        UserDto refeUserDto = null;
        ShopDto shopDto = null;
        if (StringUtils.isNotBlank(refeUser))
        {
            if (String.valueOf(CommonConst.USER_REFERTYPE_SHOP).equals(refeType))
            {
                //店铺推荐
                shopDto = shopDao.getShopById(NumberUtil.strToLong(refeUser, "refeUser"));
                if (null != shopDto)
                {
                    Long userId = shopDto.getPrincipalId();
                    user.setReferUserId(userId);
                    String referMobile = userDao.getUserById(userId).getMobile();
                    user.setReferMobile(referMobile);
                    user.setReferType(CommonConst.USER_REFERTYPE_SHOP);
                    user.setReferShopId(shopDto.getShopId());
                    if (StringUtils.isNotBlank(user.getRegisterType()) && !user.getRegisterType()
                            .equals(CommonConst.REGISTER_TYPE_APP))
                    {
                        user.setRegisterType(CommonConst.REGISTER_TYPE_SHOP);
                    }
                    //					//同时将该平台会员添加会店内会员
                    //					userToShopMember(shopDto.getShopId(),mobile);
                }
                else
                {
                    CommonValidUtil
                            .validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
                }
            }
            else
            {
                if (refeUser.length() > 10)
                {
                    //代表是手机号码
                    refeUserDto = this.userDao.getUserByMobile(refeUser);
                }
                else
                {
                    //userId
                    Long referUserId = NumberUtil.strToLong(refeUser, "refeUser");
                    refeUserDto = this.userDao.getUserById(referUserId);
                }
            }
        }
        if (null != refeUserDto)
        {
            user.setReferUserId(refeUserDto.getUserId());
            user.setReferMobile(refeUserDto.getMobile());
            user.setReferType(CommonConst.USER_REFERTYPE_MEMBER);
        }
        else
        {
            //如果refeUser没有传值或者没有找到推荐人就通过推荐码去查询
            if (StringUtils.isNotBlank(refecode) && shopDto == null)
            {
                //推荐人
                List<UserDto> referUserList = this.userDao.getReferUserBy(mobile, refecode);
                if (CollectionUtils.isNotEmpty(referUserList))
                {
                    UserDto referUser = referUserList.get(0);
                    user.setReferUserId(referUser.getUserId());
                    user.setReferMobile(referUser.getMobile());
                    //账户推荐
                    user.setReferType(CommonConst.USER_REFERTYPE_MEMBER);
                }
            }
        }
    }

    private void userToShopMember(Long userId, Long shopId, String mobile) throws Exception
    {
        ShopMemberDto shopMemberDto = shopMemberDao
                .getShopMbByMobileAndShopId(mobile, shopId, CommonConst.MEMBER_STATUS_DELETE);
        if (shopMemberDto == null)
        {
            logger.info("开始将该平台会员添加到店内会员");
            shopMemberDto = new ShopMemberDto();
            shopMemberDto.setCreateTime(new Date());
            shopMemberDto.setGrade(0);
            shopMemberDto.setMemberStatus(1);
            shopMemberDto.setMobile(Long.valueOf(mobile));
            shopMemberDto.setName(mobile);
            shopMemberDto.setShopId(shopId);
            shopMemberDto.setUserId(userId);
            shopMemberDao.addShopMember(shopMemberDto);
            logger.info("已将该平台会员添加到店内会员");
            logger.info("开始给平台会员添加会员卡");
            this.addShopMemberCard(shopMemberDto);
            if (shopMemberDto.getMobile() != null && shopMemberDto.getShopId() != null)
            {
                shopMemberLevelService
                        .upgrateShopMemberLevel(shopMemberDto.getMobile().toString(), shopMemberDto.getShopId(), 1);
            }
            logger.info("结束给平台会员添加会员卡");

        }

    }

    private void addShopMemberCard(ShopMemberDto shopMemberDto) throws Exception
    {
        if (shopMemberDto != null)
        {
            String mobile = shopMemberDto.getMobile() == null ? "" : shopMemberDto.getMobile().toString();
            boolean isExist = false;
            Long memberId = shopMemberDto.getMemberId();
            if (null != memberId)
            {
                isExist = shopMemberCardService.checkCardExistByMid(memberId);
            }
            else
            {
                isExist = shopMemberCardService.checkCardExist(shopMemberDto.getShopId(), mobile);
            }
            if (!isExist)
            {
                ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
                shopMemberCardDto.setCardType(CommonConst.CARD_TYPE_IS_MASTER_CARD);
                shopMemberCardDto.setMemberId(memberId);
                shopMemberCardDto.setMobile(mobile);
                shopMemberCardDto.setShopId(shopMemberDto.getShopId());
                shopMemberCardDto.setSex(shopMemberDto.getSex() == null ? 1 : shopMemberDto.getSex());
                shopMemberCardDto.setName(shopMemberDto.getName());
                shopMemberCardDto.setOperaterId(null);
                shopMemberCardDto.setUsedNum(0);
                Integer cardId = shopMemberCardService.insertShopMemberCard(shopMemberCardDto);
            }
        }

    }

    public int updatePassword(HttpServletRequest request) throws Exception
    {

        String mobile = RequestUtils.getQueryParam(request, "mobile");
        String veriCode = RequestUtils.getQueryParam(request, "veriCode");//验证码
        String oldPassword = RequestUtils.getQueryParam(request, "oldPassword");//原密码
        String password = RequestUtils.getQueryParam(request, "password");//新密码
        String confPassword = RequestUtils.getQueryParam(request, "confPassword");//确认密码
        CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_NEWPWD);
        CommonValidUtil.validStrNull(confPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CFPWD);

        //veriCode和oldPassword两者判断一个即可，如果有oldPassword代表修改密码，反之为重置密码
        if (StringUtils.isBlank(veriCode))
        {
            //验证手机验证码（里面有检验手机号码）在调用这个接口前验证码已经验证了，不需要再验证，在验证会出错。
            //			CommonValidUtil.validVeriCode(mobile, veriCode);
            // 短信支付
            //            boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, veriCode, true);
            //            if (!flag) {
            //                // 验证不通过
            //                throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
            //            }
            CommonValidUtil.validStrNull(oldPassword, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
        }
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);

        if (!StringUtils.equals(password, confPassword))
        {
            throw new ValidateException(CodeConst.CODE_PWD_NOT_SAME, CodeConst.MSG_NOSAME_PWD);
        }
        UserDto pUser = this.userDao.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(pUser, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        if (StringUtils.isBlank(veriCode))
        {
            if (!StringUtils.equals(pUser.getPassword(), oldPassword))
            {
                throw new ValidateException(CodeConst.CODE_PWD_ERROR, CodeConst.MSG_PWD_ERROR);
            }
        }
        pUser.setPassword(password);
        int num = this.userDao.updatePassword(pUser);
        //雇员密码
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("password", password);
        shopDao.updateEmployeePwd(map);
        //更新缓存
        DataCacheApi.setObjectEx(CommonConst.KEY_USER + pUser.getUserId(), pUser, CommonConst.CACHE_USER_OUT_TIME);
        return num;
    }

    public int updateBaseInfo(UserDto user) throws Exception
    {
        CitiesDto citiesDto = citiesDao.getCityById(user.getCityId());
        if (null != citiesDto)
        {
            user.setResidentTown(citiesDto.getCityName());
        }

        return this.userDao.updateUser(user);
    }

    public int bindBankCard(BankCardDto bankCard) throws Exception
    {
        Long userId = bankCard.getUserId();
        if (StringUtils.isBlank(bankCard.getCardNumber()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BANKCARD);
        }

        // 校验银行卡号合法性
        if (!BankInfo.checkBankCard(bankCard.getCardNumber()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "银行卡号不正确");
        }

        // 根据银行卡号判断银行
        String bankName = getBankNameByCard(bankCard.getCardNumber());
        if (bankName != null && bankName.indexOf(".") > 0)
        {
            bankName = bankName.substring(0, bankName.indexOf("."));
            bankCard.setBankName(bankName);
        }

        //		if (StringUtils.isBlank(bankCard.getBankName())) {
        //			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
        //					CodeConst.MSG_REQUIRED_BANKNAME);
        //		}
        if (StringUtils.isBlank(bankCard.getName()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ACCOUNTNAME);
        }
        //		if (StringUtils.isBlank(bankCard.getIdNum())) {
        //			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
        //					CodeConst.MSG_REQUIRED_IDCARD);
        //		} TODO 变更：去除身份证号和

        // 查询用户是否存在
        if (bankCard.getAccountType() == 1)
        {
            UserDto userDB = userDao.getUserById(userId);
            CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            bankCard.setPhone(userDB.getMobile());

        }
        else
        {
            ShopDto shopDto = shopDao.getShopById(userId);
            CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
            UserDto userDto = userDao.getUserById(shopDto.getPrincipalId());
            CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺管理者不存在");
            bankCard.setPhone(userDto.getMobile());
            if (StringUtils.isBlank(bankCard.getIdNum()))
            {
                bankCard.setIdNum(shopDto.getShopManagerIdentityCardNo());
            }
        }

        boolean isBindFlag = isBind(userId, bankCard.getCardNumber());
        if (isBindFlag)
        {
            // 重复绑定
            throw new ValidateException(CodeConst.CODE_BANK_IS_BIND, CodeConst.MSG_BANK_IS_BIND);
        }
        // 会员和银行卡：1对n关系,在关联表中插入记录

        bankCard.setIdentityType("身份证");
        return this.bankCardDao.saveBankCard(bankCard);
    }

    /**
     * 根据卡号判断是哪家银行
     *
     * @param cardNumber
     * @return
     */
    private String getBankNameByCard(String cardNumber)
    {
        return BankInfo.getNameOfBank(cardNumber.substring(0, 6), 0);
    }

    @Override public int unBindBankCard(BankCardDto bankCard) throws Exception
    {
        return bankCardDao.unBindBankCard(bankCard);
    }

    /**
     * 判断银行卡是否与用户已经绑定
     *
     * @param userId
     * @param cardNumber
     * @throws Exception
     */
    public boolean isBind(Long userId, String cardNumber) throws Exception
    {
        Integer count = bankCardDao.getBankCardByMap(userId, cardNumber);
        if (null == count || 0 == count)
        {
            return false;
        }
        return true;
    }

    public int updateMobile(Map<String, String> hashMap) throws Exception
    {

        String userIdStr = hashMap.get(CommonConst.USER_ID);
        String mobile = hashMap.get("mobile");
        String password = hashMap.get("password");
        String newMobile = hashMap.get("newMobile");
        String veriCode = hashMap.get("veriCode");
        CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
        CommonValidUtil.validStrNull(password, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
        CommonValidUtil.validStrNull(newMobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_NEWMOBILE);
        CommonValidUtil
                .validMobileStr(newMobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_NEWMOBILE_VALID);
        boolean flag = sendSmsService.checkSmsCodeIsOk(newMobile, null, veriCode);
        if (!flag)
        {
            throw new ValidateException(CodeConst.CODE_VERICODE_53101, CodeConst.MSG_VC_ERROR);
        }
        Long userId = NumberUtil.strToLong(userIdStr, "userId");
        // 验证用户的存在性
        UserDto pUser = this.userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(pUser, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);

        // 根据原手机号获取会员
        pUser = this.userDao.getUserByMobile(mobile);
        CommonValidUtil.validObjectNull(pUser, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_MOBILE_NULL);

        // 查看新号码是否已存在
        UserDto userDB = this.userDao.getUserByMobile(newMobile);
        if (userDB != null && !StringUtils.equals(mobile, newMobile))
        {
            throw new ValidateException(CodeConst.CODE_MOBILE_REGISTERED, CodeConst.MSG_MOBILE_REGISTERED);
        }

        // 判定密码
        if (!StringUtils.equals(password, pUser.getPassword()))
        {
            throw new ValidateException(CodeConst.CODE_PWD_ERROR, CodeConst.MSG_PWD_ERROR);
        }
        pUser.setNewMobile(newMobile);

        // 设置新手机号
        int num = this.userDao.updateMobile(pUser);
        if (num != 0)
        {
            pUser.setMobile(newMobile);
            DataCacheApi.del(CommonConst.KEY_MOBILE + mobile);
            DataCacheApi.setObjectEx(CommonConst.KEY_USER + userIdStr, pUser, CommonConst.CACHE_USER_OUT_TIME);
            DataCacheApi.setex(CommonConst.KEY_MOBILE + newMobile, String.valueOf(pUser.getUserId()),
                    CommonConst.CACHE_USER_OUT_TIME);
        }

        //修改完成以后需要更新缓存数据
        return num;
    }

    public int updateWeiXinNo(UserDto updateUserDto) throws Exception
    {
        return userDao.updateWeiXinNo(updateUserDto);
    }

    public UserDto login(UserDto user) throws Exception
    {
        String mobile = user.getMobile();
        // 校验是否为空
        CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);

        // 校验手机号码
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
        //校验密码
        String passWord = user.getPassword();
        String veriCode = user.getVeriCode();

        if (passWord == null && veriCode == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "登陆密码和登陆验证码至少填写一个");
        }
        //根据手机号码从数据库中获取用户信息
        UserDto userDB = this.userDao.getUserByMobileFromDB(mobile);
        Date date = new Date();
        if (StringUtils.isNotBlank(passWord))
        {
            // 校验对象是否为空
            CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            //对比密码
            if (!StringUtils.equals(passWord, userDB.getPassword()))
            {
                throw new ValidateException(CodeConst.CODE_PWD_ERROR, CodeConst.MSG_PWD_ERROR);
            }
        }
        else
        {
            boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, user.getUsage(), veriCode);
            if (!flag)
            {
                //验证不通过
                throw new ValidateException(CodeConst.CODE_VERICODE_53101, "验证码错误，请重新输入！");
            }
            if (null == userDB)
            {
                //用户不存在即注册
                userDB = buildUserDto(mobile, null, null, null);
                userDB.setSn(user.getSn());
                userDB.setFirstLoginTime(date);
                userDB.setLastLoginTime(date);
                // 注册该用户
                this.userDao.saveUser(userDB);
                //创建用户账户
                createAccountForUser(userDB);
                sendPassWordToUser(mobile, userDB.getConfPassword());
            }
        }

        //校验状态
        Integer status = userDB.getStatus();
        CommonValidUtil.validObjectNull(status, CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
        if (CommonConst.USER_FREEZE_STATUS == userDB.getStatus())
        {
            throw new ValidateException(CodeConst.CODE_USER_STATUS_FREEZE_FAIL, CodeConst.MSG_USER_STATUS_FREEZE_FAIL);
        }
        else if (CommonConst.USER_LOGOUT_STATUS == userDB.getStatus())
        {
            throw new ValidateException(CodeConst.CODE_USER_STATUS_LOGOUT_FAIL, CodeConst.MSG_USER_STATUS_LOGOUT_FAIL);
        }

        //拼接地址
        setUserAdssInfo(userDB);

        //首次登录时间
        if (userDB.getFirstLoginTime() == null)
        {
            userDB.setFirstLoginTime(date);
        }
        if (Integer.valueOf(2).equals(userDB.getIsMember()))
        {
            userDB.setCreateTime(new Date());
        }
        userDB.setIsMember(CommonConst.USER_IS_MEMBER);
        //是否需要删除缓存
        boolean lastLoginFlag = true;

        //参数SN
        String sn = user.getSn();

        //更新用户和mac地址的绑定
        if (StringUtils.isNotBlank(sn) && !StringUtils.equals(userDB.getSn(), sn))
        {

            //统一转为大写校验格式
            String mac = sn.toUpperCase();
            if (Pattern.compile(CommonConst.PATTERN_MAC).matcher(mac).matches())
            {
                userDB.setSn(sn);
                userDao.updateUserMac(userDB);
                lastLoginFlag = false;
            }
        }
        //如果用户初始状态为待激活需要将用户状态修改为正常
        if (CommonConst.USER_WAIT_ACTIVE_STATUS == status)
        {
            //代表第一次登陆，将状态改为正常状态
            userDB.setStatus(CommonConst.USER_NORMAL_STATUS);
            userDao.updateUserStatus(userDB);
            lastLoginFlag = false;
        }
        if (lastLoginFlag)
        {
            userDB.setLastLoginTime(date);
            userDao.updateUserLastLoginTime(userDB);
        }

        return userDB;
    }

    public UserDto getUserByUserId(Long userId) throws Exception
    {
        return userDao.getUserById(userId);
    }

    public UserDto getDBUserById(Long userId) throws Exception
    {
        return userDao.getDBUserById(userId);
    }

    public int updatePayPassword(UserDto user) throws Exception
    {
        return userDao.updatePayPassword(user);
    }

    public int authRealName(UserDto user) throws Exception
    {
        // userid
        Long userId = user.getUserId();
        // 真实姓名
        String trueName = user.getTrueName();
        // 身份证号码
        String identityCardNo = user.getIdentityCardNo();
        if (!NumberUtil.isPositLong(user.getUserId()))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
        }
        if (StringUtils.isBlank(trueName))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TURENAME);
        }
        if (StringUtils.isBlank(identityCardNo))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_IDCARD);
        }
        // 更新用户身份证、真实姓名
        return userDao.authRealName(user);
    }

    public UserAccountDto getAccountMoney(Long userId) throws Exception
    {
        return userAccountDao.getAccountMoney(userId);
    }

    public PageModel getUserBankCards(Long userId, Integer pNo, Integer pSize) throws Exception
    {
        // 验证用户的存在性
        // UserDto pUser = this.userDao.getUserById(userId);
        // if(pUser == null){
        // throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
        // CodeConst.MSG_MISS_MEMBER);
        // }
        // 总记录数
        int count = bankCardDao.getBankCardCountByUser(userId);
        BankCardDto card = new BankCardDto();
        card.setUserId(userId);
        List<BankCardDto> cards = this.bankCardDao.getBankCardListByUser(card, pNo, pSize);
        cards = updateImgUrl(cards);
        PageModel pm = new PageModel();
        pm.setList(cards);
        pm.setTotalItem(count);
        return pm;
    }

    /**
     * 增加图片url绝对路径返回
     *
     * @param list
     * @return
     * @throws Exception
     */
    public List<BankCardDto> updateImgUrl(List<BankCardDto> cards) throws Exception
    {
        List<BankCardDto> newlist = new ArrayList<BankCardDto>();
        if (CollectionUtils.isNotEmpty(cards))
        {
            for (BankCardDto card : cards)
            {
                String bankLogoUrl = card.getBankLogoUrl();
                bankLogoUrl = FdfsUtil.getFileProxyPath(bankLogoUrl);
                card.setBankLogoUrl(bankLogoUrl);
                newlist.add(card);
            }
        }
        return newlist;
    }

    public int authPayPassword(UserDto user) throws Exception
    {

        // 查询用户是否存在
        UserDto userDB = this.userDao.getUserByMobile(user.getMobile());
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        String payPwd = userDB.getPayPassword();

        // 支付密码有可能未设置
        if (StringUtils.isBlank(payPwd))
        {
            throw new ValidateException(CodeConst.CODE_PAYPWD_NO_SET, CodeConst.MSG_PAYPWD_NO_SET);
        }
        else if (StringUtils.equals(user.getPayPassword(), payPwd))
        {
            return 1;
        }
        else
        {
            throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
        }
    }

    public List<Map<String, Object>> getUserMembershipCardList(Long userId, Integer pageNo, Integer pageSize)
            throws Exception
    {
        // 查询用户会员列表
        return userDao.getUserMembershipCardList(userId, pageNo, pageSize);
    }

    public PageModel getUserBill(Long userId, String billType, String startTime, String endTime, Integer page,
            Integer pageSize, Integer billStatusFlag) throws Exception
    {

        UserDto user = this.userDao.getUserById(userId);
        if (user == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("billType", billType);
        map.put("billStatusFlag", billStatusFlag);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("n", (page - 1) * pageSize);
        map.put("m", pageSize);
        map.put("isShow", CommonConst.USER_BILL_IS_SHOW);
        List<UserBillDto> list = this.userBillDao.getUserBill(map);
        List<UserBillDto> resultlist = null;
        if (null != list && list.size() > 0)
        {
            UserBillDto bill = null;
            String url = null;
            resultlist = new ArrayList();
            for (int i = 0, len = list.size(); i < len; i++)
            {
                bill = list.get(i);
                url = bill.getBillLogoUrl();
                bill.setBillLogoUrl(FdfsUtil.getFileProxyPath(url));
                resultlist.add(bill);
            }
        }
        // 获取总记录数
        PageModel pm = new PageModel();
        pm.setTotalItem(this.userBillDao.getUserBillTotal(map));
        pm.setToPage(page);
        pm.setPageSize(pageSize);
        pm.setList(resultlist);
        return pm;
    }

    public int getUserMembershipCardListCount(Long userId)
    {
        return userDao.getUserMembershipCardListCount(userId);
    }

    public Map<String, Object> getUserMembershipCardInfo(Long accountId) throws Exception
    {
        if (!NumberUtil.isPositLong(accountId))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);
        }
        return userDao.getUserMembershipCardInfo(accountId);
    }

    public String insertUserReferInfo(Long userId, String referMobile) throws Exception
    {
        int isUser = packetDao.queryUserInfo(userId);
        if (isUser == 0)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        }
        Map userRefer = new HashMap();
        userRefer.put("referMobile", referMobile);
        userRefer.put("userId", userId);
        Map reMap = userDao.queryUserReferCode(userRefer);
        Integer randomCode = null;
        String createTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
        if (reMap == null || reMap.size() <= 0)
        {
            randomCode = SmsVeriCodeUtil.getIntNum(100000, 899999);
            userRefer.put("userId", userId);
            userRefer.put("referCode", randomCode.intValue());
            userRefer.put("createTime", createTime);
            userDao.isnertUserReferInfo(userRefer);
        }
        else
        {
            if (CommonValidUtil.isEmpty(reMap.get("refer_code")))
            {
                randomCode = SmsVeriCodeUtil.getIntNum(100000, 899999);
                reMap.put("refer_code", randomCode);
                reMap.put("create_time", createTime);
                //修改数据
                userDao.updateUserReferCode(reMap);
            }
            else
            {
                randomCode = Integer.parseInt(reMap.get("refer_code") + "");
            }
        }

        return randomCode.intValue() + "";
    }

    public int verifyReferCode(String mobile, String referCode) throws Exception
    {
        if (null == referCode || "".equals(referCode))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_REFER_CODE);
        }
        if (null == mobile || "".equals(mobile))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_REFER_MOBILE);
        }
        CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_MOBILE_VALID);
        Map param = new HashMap();
        param.put("referMobile", mobile.trim());
        param.put("referCode", referCode.trim());
        int re = userDao.queryUserReferInfo(param);
        return re;
    }

    public List<Map<String, Object>> getSearchHistory(Long userId, Integer pNo, Integer pSize) throws Exception
    {
        return userDao.getSearchHistory(userId, pNo, pSize);
    }

    public int getSearchHistoryCount(Long userId) throws Exception
    {
        return userDao.getSearchHistoryCount(userId);
    }

    public int deleteUserSearchHistory(Long userId, String searchContent) throws Exception
    {
        return userDao.deleteUserSearchHistory(userId, searchContent);
    }

    /**
     * 设置用户的省、市、区、镇信息
     *
     * @param user
     * @throws Exception
     */
    private void setUserAdssInfo(UserDto user) throws Exception
    {
        // 设置省、市、区、镇信息
        ProvinceDto provinceDto = provinceDao.getProvinceById(user.getProvinceId());
        if (null != provinceDto)
        {
            user.setProvinceName(provinceDto.getProvinceName());
        }
        CitiesDto citiesDto = citiesDao.getCityById(user.getCityId());
        if (null != citiesDto)
        {
            user.setCityName(citiesDto.getCityName());
        }
        DistrictDto districtDto = regionDao.getDistrictById(user.getDistrictId());
        if (null != districtDto)
        {
            user.setDistrictName(districtDto.getDistrictName());
        }
        TownDto townDto = townDao.getTownById(user.getTownId());
        if (null != townDto)
        {
            user.setTownName(townDto.getTownName());
        }
    }

    public Map<String, Object> getUserVoucherInfo(Long userId) throws Exception
    {

        // 状态可用
        Integer couponStatus = 1;
        Integer useStatus = 1;
        // 查询用户可用优惠劵
        Integer couponNumber = couponDao.getUserCouponCountBy(userId, couponStatus, useStatus);

        // 查询用户可用代金券
        Integer status = 0;
        Integer cashCouponNumber = cashCouponDao.getUserCashCouponCountBy(userId, status, useStatus);

        // 查询用户可用红包
        //		Integer redPacketNumber = packetDao.getPacketCountBy(userId, status);

        // 绑定的银行卡个数
        Integer bankCardNumber = bankCardDao.getBankCardCountByUser(userId);

        //查询代金券余额
        Map<String, Object> amountMap = cashCouponDao.getCashCouponAmount(userId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("couponNumber", couponNumber);
        map.put("cashCouponNumber", cashCouponNumber);
        map.put("redPacketNumber", 0);
        map.put("bankCardNumber", bankCardNumber);
        map.putAll(amountMap);
        return map;
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#getMyRewardType(java.lang.Long, java.lang.Integer, java.lang.Integer)
     */
    @Override public List<Map<String, Object>> getMyRewardType(Long userId, String[] accountTypes) throws Exception
    {

        /*
         * 账单类型: 4.消费返利 5.推荐会员 6.推荐店铺[会员] 7.服务店铺[服务人员] 8.市级代理 9.区县代理 10.乡镇代理14.运营商
         * rewardType   int     是
         * rewardTypeName  String      是
         */
       /* List<Map<String, Object>> reList = new ArrayList<Map<String,Object>>();
        
        // 所有人都可以拥有的角色
        reList.addAll(splitUserRewardList());
        
        // 服务人员
        Integer serverCount = shopDao.queryUserIsShopServer(userId);
        if(serverCount>0){
            reList.addAll(splitShopServerRewardList());
        }
        // 代理商
        List<AgentDto> agentList = agentDao.getAgentListByUserId(userId);
        if(CollectionUtils.isNotEmpty(agentList)){
            reList.addAll(splitAgentRewardList(agentList));
        }*/
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        List<Integer> existsRewardType = userBillService.getMyRewardType(userId, accountTypes);
        if (existsRewardType != null && existsRewardType.size() > 0)
        {
            reList.addAll(this.convertForRewardType(existsRewardType));
        }
        return reList;
    }

    private List<Map<String, Object>> convertForRewardType(List<Integer> types)
    {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        Iterator<Integer> iterator = types.iterator();
        Map<String, Object> tempMap = null;
        Integer temp = null;
        while (iterator.hasNext())
        {
            tempMap = new HashMap<>();
            temp = iterator.next();
            if (null == temp)
            {
                continue;
            }
            switch (temp)
            {
                case 0:      //平台奖励
                    tempMap.put("rewardType", 0);
                    tempMap.put("rewardTypeName", "平台奖励");
                    break;
                case 4:        //消费返利
                    tempMap.put("rewardType", 4);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_CONSUME);
                    break;
                case 5:        //推荐会员
                    tempMap.put("rewardType", 5);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_USER);
                    break;
                case 6:        //推荐店铺[会员]
                    tempMap.put("rewardType", 6);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_SHOP);
                    break;
                case 7:        //服务店铺[服务人员]
                    tempMap.put("rewardType", 7);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_SERVER);
                    break;
                case 8:        //市级代理
                    tempMap.put("rewardType", 8);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_CITY);
                    break;
                case 9:        //区县代理
                    tempMap.put("rewardType", 9);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_COUNTY);
                    break;
                case 10:        //乡镇代理
                    tempMap.put("rewardType", 10);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_TOWNS);
                    break;
                case 14:    //运营商
                    tempMap.put("rewardType", 14);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_CARRIER);
                    break;
                case 16:        //店铺整合推广员奖励
                    tempMap.put("rewardType", 16);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_PROMOTION);
                    break;
                case 17:        //店铺整合促成人奖励
                    tempMap.put("rewardType", 17);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_FACILITATE);
                    break;
                case 18:        //被整合店铺店主奖励
                    tempMap.put("rewardType", 18);
                    tempMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_PRINCIPAL);
                    break;
                case 19:    //推荐代理奖励
                    tempMap.put("rewardType", 19);
                    tempMap.put("rewardTypeName", "推荐代理");
                    break;
                case 20:    //推荐经销商奖励
                    tempMap.put("rewardType", 20);
                    tempMap.put("rewardTypeName", "推荐经销商");
                    break;
                case 22:    //代理费返还
                    tempMap.put("rewardType", 22);
                    tempMap.put("rewardTypeName", "代理费返还");
                    break;

		        case 26:    //分公司管理补贴
                    tempMap.put("rewardType", 26);
                    tempMap.put("rewardTypeName", "分公司管理补贴");
                    break;
		        case 27:    //组长收益
                    tempMap.put("rewardType", 27);
                    tempMap.put("rewardTypeName", "组长收益");
                    break;
				default:
					break;
			}
			reList.add(tempMap);
		}
		return reList;
	}



    /**
     * 组合用户角色奖励
     * 账单类型: 4.消费返利 5.推荐会员 6.推荐店铺[会员] 7.服务店铺[服务人员] 8.市级代理 9.区县代理 10.乡镇代理
     * rewardType   int     是
     * rewardTypeName  String      是
     */
    private List<Map<String, Object>> splitUserRewardList()
    {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

        Map<String, Object> consumeRewardMap = new HashMap<String, Object>();
        consumeRewardMap.put("rewardType", 4);
        consumeRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_CONSUME);
        reList.add(consumeRewardMap);

        Map<String, Object> userRewardMap = new HashMap<String, Object>();
        userRewardMap.put("rewardType", 5);
        userRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_USER);
        reList.add(userRewardMap);

        Map<String, Object> shopRewardMap = new HashMap<String, Object>();
        shopRewardMap.put("rewardType", 6);
        shopRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_SHOP);
        reList.add(shopRewardMap);

        /**
         * 16.店铺整合推广员奖励 integration_promotion
         17.店铺整合促成人奖励 integrationFacilitate
         18.被整合店铺店主奖励 integrationPrincipal
         */
        Map<String, Object> integrationPromotionMap = new HashMap<String, Object>();
        integrationPromotionMap.put("rewardType", 16);
        integrationPromotionMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_PROMOTION);
        reList.add(integrationPromotionMap);

        Map<String, Object> integrationFacilitateMap = new HashMap<String, Object>();
        integrationFacilitateMap.put("rewardType", 17);
        integrationFacilitateMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_FACILITATE);
        reList.add(integrationFacilitateMap);

        Map<String, Object> integrationPrincipalMap = new HashMap<String, Object>();
        integrationPrincipalMap.put("rewardType", 18);
        integrationPrincipalMap.put("rewardTypeName", CommonConst.USER_TYPE_INTEGRATION_PRINCIPAL);
        reList.add(integrationPrincipalMap);

        return reList;
    }

    /**
     * 组合服务人员奖励奖励
     * 组合用户角色奖励
     * 账单类型: 4.消费返利 5.推荐会员 6.推荐店铺[会员] 7.服务店铺[服务人员] 8.市级代理 9.区县代理 10.乡镇代理
     * rewardType   int     是
     * rewardTypeName  String      是
     */
    private List<Map<String, Object>> splitShopServerRewardList()
    {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

        Map<String, Object> consumeRewardMap = new HashMap<String, Object>();
        consumeRewardMap.put("rewardType", 7);
        consumeRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_SERVER);
        reList.add(consumeRewardMap);
        return reList;
    }

    /**
     * 组合代理奖励奖励
     * 组合用户角色奖励
     * 账单类型: 4.消费返利 5.推荐会员 6.推荐店铺[会员] 7.服务店铺[服务人员] 8.市级代理 9.区县代理 10.乡镇代理
     * rewardType   int     是
     * rewardTypeName  String      是
     */
    private List<Map<String, Object>> splitAgentRewardList(List<AgentDto> agentList)
    {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        /*
         * 经销商-20一级代理-31,二级代理-32,三级代理-33' 运营商-50,
         */
        for (AgentDto agent : agentList)
        {
            Integer agentType = agent.getAgentType();
            if (31 == agentType)
            {
                Map<String, Object> consumeRewardMap = new HashMap<String, Object>();
                consumeRewardMap.put("rewardType", 8);
                consumeRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_CITY);
                reList.add(consumeRewardMap);
            }
            else if (32 == agentType)
            {
                Map<String, Object> userRewardMap = new HashMap<String, Object>();
                userRewardMap.put("rewardType", 9);
                userRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_COUNTY);
                reList.add(userRewardMap);
            }
            else if (33 == agentType)
            {
                Map<String, Object> shopRewardMap = new HashMap<String, Object>();
                shopRewardMap.put("rewardType", 10);
                shopRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_AGENT_TOWNS);
                reList.add(shopRewardMap);
            }
            else if (50 == agentType)
            {
                Map<String, Object> shopRewardMap = new HashMap<String, Object>();
                shopRewardMap.put("rewardType", 14);
                shopRewardMap.put("rewardTypeName", CommonConst.USER_TYPE_REWARD_CARRIER);
                reList.add(shopRewardMap);
            }
        }
        return reList;
    }

    public Long makeComment(String commentType, String bizId, String userId, String serviceGrade, String envGrade,
            String commentContent, String logisticsGrade) throws Exception
    {

        Long commentId = null;
        // 验证参数
        verifyParameter(commentType, bizId, userId, serviceGrade, envGrade, commentContent, logisticsGrade);
        Long uid = Long.parseLong(userId);

        // 如果类型为商品
        if (CommonConst.CODE_COMMENT_TYPE_GOODS == Integer.parseInt(commentType))
        {
            Long bid = NumberUtil.strToLong(bizId, "bizId");
            // 判断goodId是否存在
            Integer goodCount = goodDao.getCountByGoodId(bid);
            if (null == goodCount || 0 == goodCount)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
            }
            GoodsCommentDto userGoodsCommentDto = new GoodsCommentDto();
            userGoodsCommentDto.setUserId(uid);
            userGoodsCommentDto.setGoodsId(bid);
            userGoodsCommentDto.setServiceGrade(Double.parseDouble(serviceGrade));
            if (StringUtils.isNotBlank(envGrade))
            {
                userGoodsCommentDto.setEnvGrade(Double.parseDouble(envGrade));
            }
            userGoodsCommentDto.setCommentContent(commentContent);
            userGoodsCommentDto.setCommentStatus((byte) 1);//默认状态为正常 (正常-1，被删除-0,隐藏-2)
            List<GoodsCommentDto> listGoods = userGoodsCommentDao.getGoodsCommentTimeById(userGoodsCommentDto);
            verifyGoodsCommentTime(listGoods);
            userGoodsCommentDao.makeGoodsComment(userGoodsCommentDto);
            // 获取主键
            commentId = userGoodsCommentDto.getCommentId();
        }

        // 如果类型为商铺
        else if (CommonConst.CODE_COMMENT_TYPE_SHOP == Integer.parseInt(commentType))
        {
            Long bid = NumberUtil.strToLong(bizId, "bizId");
            // 判断shopId是否存在
            Integer shopCount = shopDao.getCountByShopId(bid);
            if (null == shopCount || 0 == shopCount)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
            }
            ShopCommentDto userShopCommentDto = new ShopCommentDto();
            userShopCommentDto.setUserId(uid);
            userShopCommentDto.setShopId(bid);
            userShopCommentDto.setServiceGrade(Double.parseDouble(serviceGrade));
            if (StringUtils.isNotBlank(envGrade))
            {
                userShopCommentDto.setEnvGrade(Double.parseDouble(envGrade));

            }
            userShopCommentDto.setCommentContent(commentContent);
            userShopCommentDto.setCommentStatus((byte) 1);//默认状态为正常 (正常-1，被删除-0,隐藏-2)
            List<ShopCommentDto> listShop = userShopCommentDao.getShopCommentTimeById(userShopCommentDto);
            verifyShopCommentTime(listShop);
            userShopCommentDao.makeCommentShop(userShopCommentDto);
            // 获取主键
            commentId = userShopCommentDto.getCommentId();
        }

        //如果类型为订单
        else if (CommonConst.CODE_COMMENT_TYPE_ORDER == Integer.parseInt(commentType))
        {

            OrderDto order = this.orderDao.getOrderById(bizId);
            CommonValidUtil.validObjectNull(order, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_ORDER_NOT_EXIST);
            Long shopid = order.getShopId();
            List<Map<String, Object>> list = userOrderCommentDao.getOrderCommentById(bizId, 1, 3, uid);
            verifyOrderCommentTime(list);
            //商铺
            ShopCommentDto userShopCommentDto = new ShopCommentDto();
            userShopCommentDto.setUserId(uid);
            userShopCommentDto.setShopId(shopid);
            userShopCommentDto.setOrderId(bizId);
            userShopCommentDto.setServiceGrade(Double.parseDouble(serviceGrade));
            if (StringUtils.isNotBlank(envGrade))
            {
                userShopCommentDto.setEnvGrade(Double.parseDouble(envGrade));
            }
            userShopCommentDto.setCommentContent(commentContent);
            userShopCommentDto.setCommentStatus((byte) 1);
            if (StringUtils.isNotBlank(logisticsGrade))
            {
                userShopCommentDto.setLogisticsGrade(Double.parseDouble(logisticsGrade));
            }
            List<ShopCommentDto> listShop = userShopCommentDao.getShopCommentTimeById(userShopCommentDto);
            verifyShopCommentTime(listShop);
            userShopCommentDao.makeCommentShop(userShopCommentDto);
            //7.13变更，给商铺评论表插入数据的同时，订单评论表也插入一条
            OrderCommentDto orderCommentDto = new OrderCommentDto();
            orderCommentDto.setUserId(uid);
            orderCommentDto.setOrderId(bizId);
            orderCommentDto.setShopId(shopid);
            orderCommentDto.setServiceGrade(Double.parseDouble(serviceGrade));
            if (StringUtils.isNotBlank(envGrade))
            {
                orderCommentDto.setEnvGrade(Double.parseDouble(envGrade));
            }
            orderCommentDto.setCommentContent(commentContent);
            orderCommentDto.setCommentStatus((byte) 1);
            if (StringUtils.isNotBlank(logisticsGrade))
            {
                orderCommentDto.setLogisticsGrade(Double.parseDouble(logisticsGrade));
            }
            userOrderCommentDao.makeOrderComment(orderCommentDto);
            //当评论状态等于3(已结账)时候才允许更新订单状态
            commentId = userShopCommentDto.getCommentId();
            if (3 == order.getOrderStatus())
            {
                //更新订单状态
                orderDao.updateOrderStatusById(bizId);
                //记录订单日志
                //payService.saveOrderLog(order, "评论商铺");
            }
            else
            {
                //订单状态为不可支付状态
                throw new ValidateException(CodeConst.CODE_COMMENT_STATUS_ERROR, CodeConst.MSG_COMMENT_STATUS_ERROR);
            }

            //评论订单需要增加积分
            addPointByOrder(order, userId, bizId, serviceGrade, envGrade, logisticsGrade);

        }
        return commentId;
    }

    private void addPointByOrder(OrderDto order, String userId, String bizId, String serviceGrade, String envGrade,
            String logisticsGrade)
    {
        //异步增加积分
        Integer grade = getGrade(serviceGrade, envGrade, logisticsGrade);
        //大于三分才增加积分
        if (grade >= 3)
        {
            /**
             *
             *
             * @param ruleType 积分规则类型
             * @param pointTargetType 积分规则子类型
             * @param pointTargetId 等级类型:1=店铺，2=会员
             * @param subRuleType 类型对应的Id
             * @param pointSourceType 积分来源类型
             * @param pointSourceId  积分来源Id
             *
             */

            levelService
                    .pushAddPointMessage(7, getSubRuleTypeByGrade(grade), 1, order.getShopId().intValue(), 2, userId,
                            true);

        }
    }

    /**
     * 获取最大积分
     */
    private static Integer getGrade(String serviceGradeStr, String envGradeStr, String logisticsGradeStr)
    {

        //星级大于3星都发送消息
        Double serviceGrade = 5D;
        Double envGrade = 5D;
        Double logisticsGrade = 5D;

        if (StringUtils.isBlank(logisticsGradeStr))
        {
            logisticsGrade = 0D;
        }
        else
        {
            logisticsGrade = Double.valueOf(logisticsGradeStr);
        }

        if (StringUtils.isBlank(envGradeStr))
        {
            envGrade = 0D;
        }
        else
        {
            envGrade = Double.valueOf(envGradeStr);
        }

        if (StringUtils.isBlank(serviceGradeStr))
        {
            serviceGrade = 0D;
        }
        else
        {
            serviceGrade = Double.valueOf(serviceGradeStr);
        }

        return NumberUtil.getMax(serviceGrade.intValue(), envGrade.intValue(), logisticsGrade.intValue());
    }

    /**
     * 根据积分获取规则类型
     *
     * @return
     */
    private Integer getSubRuleTypeByGrade(Integer grade)
    {

        //评价类：1=五星，2=四星，3=三星
        Integer subRuleType = 0;

        if (grade == 3)
        {
            subRuleType = 3;
        }
        if (grade == 4)
        {
            subRuleType = 2;
        }
        if (grade == 5)
        {
            subRuleType = 1;
        }
        return subRuleType;
    }

    public int getCommentTotalNumber(String commentType, String bizId) throws Exception
    {
        Integer totalNumber = 0;
        // 非空判断
        if (StringUtils.isEmpty(commentType) || StringUtils.isBlank(commentType))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_COMMENTTYPE_IS_NULL);
        }
        if (StringUtils.isEmpty(bizId) || StringUtils.isBlank(bizId))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_BIZID_IS_NULL);
        }
        // 评论类型
        if (!NumberUtil.isNumeric(commentType))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_INVALID_COMMENTTYPE);
        }
        // 如果类型为商品
        if (CommonConst.CODE_COMMENT_TYPE_GOODS == Integer.parseInt(commentType))
        {
            if (!NumberUtil.isNumeric(bizId))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商品id格式错误");
            }
            // 判断goodId是否存在
            totalNumber = userGoodsCommentDao.getGoodsCommentsTotal(Long.parseLong(bizId));

        }// 如果类型为商铺
        else if (CommonConst.CODE_COMMENT_TYPE_SHOP == Integer.parseInt(commentType))
        {
            if (!NumberUtil.isNumeric(bizId))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "商铺id格式错误");
            }
            // 判断shopId是否存在
            totalNumber = userShopCommentDao.getShopCommentsTotal(Long.parseLong(bizId));
        }//如果类型为订单
        else if (CommonConst.CODE_COMMENT_TYPE_ORDER == Integer.parseInt(commentType))
        {
            totalNumber = userOrderCommentDao.getOrderCommentCountById(bizId);
        }
        return totalNumber;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date
     * @return 最后评论时间到现在的天数
     */
    public int daysBetween(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time1 = cal.getTimeInMillis();
        cal.setTime(new Date());
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 验证商品评论时间是否在配置时间段里面
     */
    private boolean verifyGoodsCommentTime(List<GoodsCommentDto> listGoods)
    {
        boolean isOK = false;
        if (null != listGoods && 0 != listGoods.size())
        {
            GoodsCommentDto goodsCommentDto = listGoods.get(0);
            Properties props = ConfigPropertiesInitListener.TIMECONFIG;
            String commentDaysStr = props.getProperty("commentDays");
            //重复评论限制的天数
            Integer limitDays = Integer.parseInt(commentDaysStr);
            Date commentTime = goodsCommentDto.getCommentTime();
            //实际天数
            Integer commentDas = daysBetween(commentTime);
            // 评论时间不在配置范围,即表示可以评论
            if (commentDas > limitDays)
            {
                isOK = true;
            }
            else
            {
                throw new ValidateException(CodeConst.CODE_COMMENT_TIME_ERROR, CodeConst.MSG_COMMENT_TIME_ERROR);
            }
        }
        return isOK;
    }

    /**
     * 验证商铺评论时间是否在配置时间段里面
     */
    public boolean verifyShopCommentTime(List<ShopCommentDto> list)
    {
        boolean isOK = false;
        if (null != list && 0 != list.size())
        {
            ShopCommentDto shopCommentDto = list.get(0);
            Properties props = ConfigPropertiesInitListener.TIMECONFIG;
            String commentDaysStr = props.getProperty("commentDays");
            //重复评论限制的天数
            Integer limitDays = Integer.parseInt(commentDaysStr);
            Date commentTime = shopCommentDto.getCommentTime();
            //实际天数
            Integer commentDas = daysBetween(commentTime);
            // 评论时间不在配置范围,即表示可以评论
            if (commentDas > limitDays)
            {
                isOK = true;
            }
            else
            {
                throw new ValidateException(CodeConst.CODE_COMMENT_TIME_ERROR, CodeConst.MSG_COMMENT_TIME_ERROR);
            }
        }
        return isOK;

    }

    /**
     * 验证订单评论时间是否在配置时间段里面
     */
    public boolean verifyOrderCommentTime(List<Map<String, Object>> list)
    {
        boolean isOK = false;
        if (CollectionUtils.isNotEmpty(list))
        {
            Date commentTime = (Date) list.get(0).get("commentTime");
            Properties props = ConfigPropertiesInitListener.TIMECONFIG;
            String commentDaysStr = props.getProperty("commentDays");
            //重复评论限制的天数
            Integer limitDays = Integer.parseInt(commentDaysStr);
            //Date commentTime = shopCommentDto.getCommentTime();
            //实际天数
            Integer commentDas = daysBetween(commentTime);
            // 评论时间不在配置范围,即表示可以评论
            if (commentDas > limitDays)
            {
                isOK = true;
            }
            else
            {
                throw new ValidateException(CodeConst.CODE_COMMENT_TIME_ERROR, CodeConst.MSG_COMMENT_TIME_ERROR);
            }
        }
        return isOK;

    }

    /**
     * 验证用户评论参数合法性
     *
     * @param userId
     * @param pNo
     * @param pSize
     * @throws Exception
     * @throws NumberFormatException
     */
    public void verifyParameter(String commentType, String bizId, String userId, String serviceGrade, String envGrade,
            String commentContent, String logisticsGrade) throws Exception
    {

        CommonValidUtil.validStrNull(userId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MEMBER);

        Long uid = NumberUtil.strToLong(userId, "userId");

        // 验证用户是否存在
        UserDto user = userDao.getUserById(uid);
        if (null == user)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        }
        // 非空判断
        if (StringUtils.isBlank(commentType))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_COMMENTTYPE_IS_NULL);
        }
        if (StringUtils.isBlank(bizId))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_BIZID_IS_NULL);
        }
        if (StringUtils.isBlank(serviceGrade))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_SERVICEGRADE_IS_NULL);
        }
        if (StringUtils.isNotBlank(logisticsGrade))
        {
            // 配送速度评分
            NumberUtil.isDouble("logisticsGrade", logisticsGrade);
        }
        if (StringUtils.isNotBlank(envGrade))
        {
            // 环境评分
            NumberUtil.isDouble("envGrade", envGrade);
        }
        //		if ( StringUtils.isBlank(commentContent)) {
        //			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
        //					CodeConst.MSG_COMMENTCONTENT_IS_NULL);
        //		}
        // bizId(shopId或goodsId)
        //		if (!NumberUtil.isNumeric(bizId)) {
        //			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
        //					CodeConst.MSG_INVALID_BIZID);
        //		}
        // 评论类型
        if (!NumberUtil.isNumeric(commentType))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_INVALID_COMMENTTYPE);
        }
        // 评论类型
        NumberUtil.isDouble("serviceGrade", serviceGrade);
    }

    /**
     * 判断shopId和goodId是否存在
     *
     * @param shopId
     * @param goodId
     * @return
     */
    public void ValidGoodAndShopISEXIST(Long shopId, Long goodId)
    {
        Integer shopCount = shopDao.getCountByShopId(shopId);
        if (null == shopCount || 0 == shopCount)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        Integer goodCount = goodDao.getCountByGoodId(goodId);
        if (null == goodCount || 0 == goodCount)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOOD);
        }
    }

    public Long authenUserById(Long userId) throws Exception
    {
        return this.userDao.authenUserById(userId);
    }

    public Map<String, String> updateLogo(Long userId, String usageType, Long bizId, String mimeType,
            MultipartFile myfile) throws Exception
    {
        //验证用户的存在性
        UserDto userDB = this.userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);

        String url = FdfsUtil.uploadFile(mimeType, myfile);
        Map<String, Object> param = new HashMap<String, Object>();
        //代表更新用户头像
        param.put("userId", userId);
        param.put("usageType", usageType);
        param.put("url", url);
        param.put("bizId", bizId);
        Long commentId = null;

        if (usageType.startsWith("1"))
        {

            //更新用户头像
            this.userDao.updateUserLogo(param);
            //删除缓存
            DataCacheApi.del(CommonConst.KEY_USER + userId);
        }
        else if (usageType.startsWith("2"))
        {

            //更新商品评论表
            List<GoodsCommentDto> goodsCommentList = this.userGoodsCommentDao.getGoodSCommentsByMap(param);
            if (CollectionUtils.isNotEmpty(goodsCommentList))
            {

                commentId = goodsCommentList.get(0).getCommentId();
                param.put("commentId", commentId);
                this.userGoodsCommentDao.updateGoosComLogo(param);
            }

        }
        else if (usageType.startsWith("3"))
        {

            //更新商铺评论表
            List<ShopCommentDto> shopCommentList = this.userShopCommentDao.getShopCommentsByMap(param);
            if (CollectionUtils.isNotEmpty(shopCommentList))
            {

                commentId = shopCommentList.get(0).getCommentId();
                param.put("commentId", commentId);
                this.userShopCommentDao.updateShopComLogo(param);
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        String realPath = FdfsUtil.getFileProxyPath(url);

        map.put("urls", realPath);
        return map;
    }

    public void addRegId(HttpServletRequest request) throws Exception
    {
        String regId = RequestUtils.getQueryParam(request, "regId");
        String osInfo = RequestUtils.getQueryParam(request, "osInfo");
        String userIdStr = RequestUtils.getQueryParam(request, CommonConst.USER_ID);
        String userTypeStr = RequestUtils.getQueryParam(request, "userType");
        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");

        CommonValidUtil.validStrNull(regId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REGID_NULL);
        Long userId = null;
        Integer userType;
        if (StringUtils.isNotBlank(userIdStr))
        {

            //校验用户信息
            userId = NumberUtil.strToLong(userIdStr, "userId");
            UserDto user = this.userDao.getUserById(userId);
            CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        }
        else
        {
            if (StringUtils.isNotBlank(shopIdStr))
            {
                userId = shopDao.getUserIdByShopIed(NumberUtil.strToLong(shopIdStr, "shopId"));
            }
        }
        if (StringUtils.isBlank(userTypeStr))
        {
            //为空给默认值0
            userTypeStr = "0";
        }
        userType = NumberUtil.strToNum(userTypeStr, "userType");

        operatePushUser(osInfo, regId, userId, userType);
    }

    public int savePushUser(PushUserTableDto pushUser) throws Exception
    {
        return this.pushUserDao.savePushUser(pushUser);
    }

    public List<PushUserTableDto> getPushUserByUserId(Long userId, Integer userType) throws Exception
    {
        return this.pushUserDao.getPushUserByUserId(userId, userType);
    }

    public PushUserTableDto getPushUserByRegId(String regId) throws Exception
    {
        return this.pushUserDao.getPushUserByRegId(regId);
    }

    public int updatePushUser(PushUserTableDto pushUser) throws Exception
    {
        return this.pushUserDao.updatePushUser(pushUser);
    }

    /**
     * 操作推送注册信息，有就更新，没有就新增
     *
     * @param osInfo
     * @param regId
     * @param userId
     * @return
     */
    private void operatePushUser(String osInfo, String regId, Long userId, Integer userType)
    {
        PushUserTableDto pushUser = new PushUserTableDto();
        pushUser.setUserId(userId);
        pushUser.setRegId(regId);
        pushUser.setOsInfo(osInfo);
        pushUser.setUserType(userType);
        pushUser.setCreateTime(new Date());
        //防止客户端传null字符串
        if (null != userId && !"null".equals(regId))
        {
            //先根据regId或者userId删除注册码
            this.pushUserDao.deletePushUserByRedId(regId, userId, userType);
            //再新增注册码
            this.pushUserDao.savePushUser(pushUser);
        }
    }

    /**
     * @param @param  commentId
     * @param @param  commentType
     * @param @return
     * @param @throws Exception
     * @throws
     * @Title: findUserIdByCommentId
     * @Description: TODO(根据评论编号找到用户编号)
     */
    public Long findUserIdByCommentId(Long commentId, Integer commentType) throws Exception
    {
        if (commentType == CommonConst.CODE_COMMENT_TYPE_GOODS)
        {
            return userGoodsCommentDao.getUserIdByCommentId(commentId);
        }
        else if (commentType == CommonConst.CODE_COMMENT_TYPE_SHOP)
        {
            return userShopCommentDao.getUserIdByCommentId(commentId);
        }
        else
        {
            throw new ServiceException("评论类型不存在");
        }
    }

    public PageModel getMyRefUsers(Long userId, Integer pNo, Integer pSize) throws Exception
    {
        //验证用户的存在性
        UserDto userDB = this.userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        List<Map> list = (List) this.userDao.getMyRefUsers(userId, pNo, pSize);
        if (CollectionUtils.isNotEmpty(list))
        {
            for (Map map : list)
            {
                String mobile = (String) map.get("mobile");
                //代表结算成功状态
                Integer billStatus = RewardsEnum.HAVE_SETTLEMENT.getValue();
                String billType = CommonConst.REFER_MEMBER;
                Double amount = this.userDao.sumAmount(mobile, billStatus, billType, userId);
                map.put("amount", amount);
            }
        }
        int totalItem = this.userDao.getMyRefUsersCount(userId);
        PageModel pm = new PageModel();
        pm.setToPage(pNo);
        pm.setPageSize(pSize);
        pm.setTotalItem(totalItem);
        pm.setList(list);
        return pm;
    }

    public List<UserDto> getReferUserBy(String mobile, String refecode) throws Exception
    {
        return this.userDao.getReferUserBy(mobile, refecode);
    }

    @Override public List<Map<String, Object>> getOrderComments(String orderId, Integer pNo, Integer pSize, Long userId)
            throws Exception
    {
        return userOrderCommentDao.getOrderCommentById(orderId, pNo, pSize, userId);
    }

    @Override public int getOrderCommentCountById(String commentId)
    {
        if (StringUtils.isEmpty(commentId))
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
        }
        return userOrderCommentDao.getOrderCommentCountById(commentId);
    }

    public UserDto getBaseInfo(Long userId) throws Exception
    {
        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        setUserAdssInfo(user);
        user.setBigLogo(FdfsUtil.getFileProxyPath(user.getBigLogo()));
        user.setSmallLogo(FdfsUtil.getFileProxyPath(user.getSmallLogo()));
        return user;
    }

    public Map<String, Object> getRedPacketTotalMoney(Long userId) throws Exception
    {
        UserDto user = userDao.getUserById(userId);
        CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);

        Integer status = 0;
        // 查询用户可用红包
        Integer redPacketNumber = this.packetDao.getPacketCountBy(userId, status);
        Double redPacketAmount = this.packetDao.getPacketAmountBy(userId, status);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalNumber", redPacketNumber);
        map.put("totalAmount", redPacketAmount);

        return map;
    }

    /**
     * @param @param  userSearchHistory
     * @param @throws Exception
     * @throws
     * @Title: insertUserSearchHistory
     */
    @Override public void insertUserSearchHistory(UserSearchHistory userSearchHistory) throws Exception
    {
        userDao.insertUserSearchHistory(userSearchHistory);
    }

    /**
     * @param @param  userSearchHistory
     * @param @return
     * @param @throws Exception
     * @throws
     * @Title: getCountByUserIdAndSearchContent
     */
    public int getCountByUserIdAndSearchContent(UserSearchHistory userSearchHistory) throws Exception
    {
        return userDao.getCountByUserIdAndSearchContent(userSearchHistory);
    }

    public void refreshUser(Long userId) throws Exception
    {
        UserDto userDto = userDao.getDBUserById(userId);

        // 校验对象是否为空
        CommonValidUtil.validObjectNull(userDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
        String userKey = CommonConst.KEY_USER + userId;
        String mobileKey = CommonConst.KEY_MOBILE + userDto.getMobile();
        DataCacheApi.setex(mobileKey, userId + "", CommonConst.CACHE_USER_OUT_TIME);
        DataCacheApi.setObjectEx(userKey, userDto, CommonConst.CACHE_USER_OUT_TIME);
    }

    @Override public Map<String, Object> getUserBillDetail(Long billId) throws Exception
    {
        // 先获取账单
        Map<String, Object> billMap = userBillDao.getUserBillById(billId);
        if (billMap == null)
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "账单不存在:billId=" + billId);

        // 设置下单月份
        Date createTime = (Date) billMap.get("createTime");
        billMap.put("billMonth", DateUtils.format(createTime, "yyyy-MM"));

        // 设置图片全路径
        billMap.put("billLogoUrl", FdfsUtil.getFileProxyPath((String) billMap.get("billLogoUrl")));

        // 账单类型
        String billType = (String) billMap.get("billType");

        // 获取交易号后从map删除
        String transactionId = null;
        if (billMap.get("transactionId") != null)
            transactionId = billMap.get("transactionId").toString();
        billMap.remove("transactionId");

        // 获取结账时间后从map删除
        Date settleTime = (Date) billMap.get("settleTime");
        billMap.remove("settleTime");

        String orderId = (String) billMap.get("orderId");
        //billMap.remove("orderId");// 获取订单ID后从map删除 、 新增加orderId返回 2016.1.13 by cyx

        // 获取用户ID后从map删除
        String userId = null;
        if (billMap.get("userId") != null)
            userId = billMap.get("userId").toString();
        billMap.remove("userId");

        // 提现类型
        if (StringUtils.isNotBlank(billType) && billType.indexOf("提现") >= 0)
        {

            if (StringUtils.isNotBlank(transactionId))
            {
                Map<String, Object> withdrawMap = withdrawDao.getWithdrawInfoById(Long.valueOf(transactionId));
                if (withdrawMap != null)
                {
                    billMap.put("bankName", withdrawMap.get("bankName"));

                    // 银行卡号去后4位
                    if (withdrawMap.get("cardNo") != null)
                        billMap.put("cardNo", NumberUtil.showAfter4OfCarNumber(withdrawMap.get("cardNo").toString()));
                    billMap.put("payType", "支付宝");
                    billMap.put("withdrawTime", withdrawMap.get("withdrawTime"));
                }
            }

        }

        // 充值类型
        if (StringUtils.isNotBlank(billType) && billType.indexOf("充值") >= 0 && transactionId != null)
        {
            Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
            transaction3rdDto.setTransactionId(Long.valueOf(transactionId));

            Transaction3rdDto transaction3rdDtoDB = transaction3rdDao.ge3rdPayById(transaction3rdDto);
            if (transaction3rdDtoDB != null)
            {
                billMap.put("transactionId", transactionId);
                billMap.put("thirdNotifyTime", transaction3rdDtoDB.getRdNotifyTime());
                billMap.put("accountId", transaction3rdDtoDB.getUserId());
            }
        }
        // 奖励类型
        if (StringUtils.isNotBlank(billType) && billType.indexOf("奖励") >= 0)
        {
            billMap.put("orderId", orderId);
            billMap.put("settleTime", settleTime);
        }

        // 消费类型
        if (StringUtils.isNotBlank(billType) && billType.indexOf("消费") >= 0)
        {
            billMap.put("orderId", orderId);
        }

        // 退款类型
        if (StringUtils.isNotBlank(billType) && billType.indexOf("退款") >= 0)
        {
            billMap.put("orderId", orderId);
        }

        return billMap;
    }

    public Map<String, Object> getPayPwdStatus(Long userId, int type) throws Exception
    {
        // 查询用户是否存在
        String pwd = null;
        if (type == 1)
        {
            UserDto user = userDao.getUserById(userId);
            CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
            pwd = user.getPayPassword();
        }
        else
        {
            // 校验商铺是否存在
            Map<String, Object> shop = shopDao.getPayPassWordById(userId);
            CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
            pwd = (String) shop.get("payPassword");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        //支付密码是否设定状态，1：已设定  0：未设定
        Integer payPwdStatus = 0;
        if (StringUtils.isNotBlank(pwd))
        {
            payPwdStatus = 1;
        }
        map.put("payPwdStatus", payPwdStatus);

        return map;
    }

    @Override public ShopAccountDto getShopAccountById(Long shopId) throws Exception
    {
        return shopAccountDao.getShopAccount(shopId);
    }

    /**
     * 修改用户搜索历史记录
     *
     * @param @param  userSearchHistory
     * @param @throws Exception
     * @throws
     * @Title: updateUserSearchHistory
     */
    public void updateUserSearchHistory(UserSearchHistory userSearchHistory) throws Exception
    {
        userDao.updateUserSearchHistory(userSearchHistory);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#getUserXbill(java.util.Map)
     */
    @Override public PageModel getUserXbill(Map<String, Object> pMap) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = userDao.getUserXbillCount(pMap);
        if (count != 0)
        {
            List<Map<String, Object>> resultList = userDao.getUserXbill(pMap);
            pageModel.setTotalItem(count);
            pageModel.setList(resultList);
        }
        return pageModel;
    }

    @Override public PageModel getBillDetail(Map<String, Object> requestParamMap) throws Exception
    {
        PageModel pageModel = new PageModel();
        int totalItem = userDao.getBillDetailCount(requestParamMap);
        if (totalItem > 0)
        {
            List<Map<String, Object>> resultList = userDao.getBillDetail(requestParamMap);
            pageModel.setList(resultList);
        }
        pageModel.setTotalItem(totalItem);
        return pageModel;
    }

    /**
     * 更新用户归属地信息
     *
     * @param mobile
     * @param userId
     * @throws Exception
     * @Function: com.idcq.appserver.service.member.MemberServiceImpl.updateProvinceInfo
     * @Description:
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年11月9日 下午3:16:01
     * <p/>
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年11月9日    ChenYongxin      v1.0.0         create
     */
    private void updateProvinceInfo(String mobile, Long userId) throws Exception
    {
        logger.info("手机号码：" + mobile);
        // 获取手机归属地信息
        MobileAttributionDto mad = MobileUtil.getAddressByMobile(mobile);
        if (mad != null)
        {
            // 城市名称
            String cityName = mad.getCity();
            if (StringUtils.isNotBlank(cityName))
            {
                cityName = cityName + "市";
                // 查询省份城市信息
                Map<String, Object> resultMap = citiesDao.getCityInfoByName(cityName);
                if (null != resultMap && resultMap.size() != 0)
                {
                    Long provinceId = (Long) resultMap.get("provinceId");
                    Long cityId = (Long) resultMap.get("cityId");
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("provinceId", provinceId);
                    pMap.put("cityId", cityId);
                    pMap.put("userId", userId);
                    // 更新归属地
                    userDao.updateUserPlace(pMap);
                }
            }
        }
    }

    /**
     * 更新临时表手机号码归属地
     *
     * @param mobile
     * @throws Exception
     * @Description:
     */
    private void updateNoCityMobile(String mobile) throws Exception
    {
        logger.info("手机号码：" + mobile);
        // 获取手机归属地信息
        MobileAttributionDto mad = MobileUtil.getAddressByMobile(mobile);
        if (mad != null)
        {
            // 城市名称
            String cityName = mad.getCity();
            if (StringUtils.isNotBlank(cityName))
            {
                cityName = cityName + "市";
                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("cityName", cityName);
                pMap.put("mobile", mobile);
            }
        }

    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#getNoCityMobile(java.lang.Integer, java.lang.Integer)
     */
    @Override public List<Map<String, Object>> getNoCityMobile(Integer limit, Integer pNo) throws Exception
    {
        return userDao.getNoCityMobile(limit, pNo);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#updateCityMobile(java.util.Map)
     */
    @Override public int updateCityMobile(Map<String, Object> map) throws Exception
    {
        return userDao.updateCityMobile(map);
    }

    @Override public void setShopPayPwd(Long shopId, String payPwd) throws Exception
    {
        ShopDto shopDto = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
        shopDao.updateShopPayPwd(shopId, payPwd);
    }

    @Override public void updateShopPayPwd(Long shopId, String payPassword, String newPayPwd) throws Exception
    {
        // 校验商铺是否存在
        Map<String, Object> shop = shopDao.getPayPassWordById(shopId);
        CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");

        // 校验原支付密码
        String pwdFromDb = (String) shop.get("payPassword");
        if (!payPassword.equals(pwdFromDb))
        {
            throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAY_PWD_ERROR);
        }
        // 更新
        shopDao.updateShopPayPwd(shopId, newPayPwd);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#getBillStat(java.util.Map)
     */
    @Override public List<Map<String, Object>> getBillStat(Map<String, Object> parpMap) throws Exception
    {
        return userDao.getBillStat(parpMap);
    }

    @Override public void authShopPayPassword(String shopId, String payPassword) throws Exception
    {
        // 校验商铺是否存在
        Map<String, Object> shop = shopDao.getPayPassWordById(Long.valueOf(shopId));
        CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
        // 原支付密码
        String pwdFromDb = (String) shop.get("payPassword");
        // 支付密码有可能未设置
        if (StringUtils.isBlank(pwdFromDb))
        {
            throw new ValidateException(CodeConst.CODE_PAYPWD_NO_SET, CodeConst.MSG_PAYPWD_NO_SET);
        }
        else if (!StringUtils.equals(pwdFromDb, payPassword))
        {
            throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR, CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
        }

    }

    public Map<String, Object> getAccountingStat(Long userId, String startTimeStr, String endTimeStr) throws Exception
    {
        //检验用户合法性
        UserDto userDto = userDao.getUserById(userId);
        if (null == userDto)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "用户信息不存在");
        }
        //是否区域代理
        boolean isAgent = (userDto.getUserType() == null ? false : userDto.getUserType() == 30);
        //查询该会员是否店铺服务人员
        Integer count = shopDao.queryUserIsShopServer(userId);
        boolean isShopServer = (count > 0);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTimeStr);
        params.put("endTime", endTimeStr);
        params.put("userId", userId);
        //账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,
        //8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值'
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //消费金额(订单金额)
        params.put("userBillType", 1);
        Double orderAmount = userBillDao.getUserAccountingStat(params);
        resultMap.put("orderAmount", NumberUtil.formatDoubleStr(orderAmount, 4));
        //消费金：去掉了
		/*params.put("userBillType", 1);
		Double shoppingAmount = userBillDao.getAccountingStat(params);
		resultMap.put("shoppingAmount", shoppingAmount);*/
        //消费返利
        params.put("userBillType", 4);
        Double shoppingReward = userBillDao.getUserAccountingStat(params);
        resultMap.put("shoppingReward", NumberUtil.formatDoubleStr(shoppingReward, 4));
        //推荐会员奖励
        params.put("userBillType", 5);
        Double addUserReward = userBillDao.getUserAccountingStat(params);
        resultMap.put("addUserReward", NumberUtil.formatDoubleStr(addUserReward, 4));
        //推荐商铺奖励
        params.put("userBillType", 6);
        Double addShopReward = userBillDao.getUserAccountingStat(params);
        resultMap.put("addShopReward", NumberUtil.formatDoubleStr(addShopReward, 4));
        //平台奖励= 消费返利(5) +推荐会员(10) +推荐店铺(32)+服务店铺(220)+代理(500)
        Double rewardAmount = (shoppingReward + addUserReward + addShopReward);
        if (isShopServer)
        {
            //服务店铺奖励
            params.put("userBillType", 7);
            Double serviceReward = userBillDao.getUserAccountingStat(params);
            resultMap.put("serviceReward", NumberUtil.formatDoubleStr(serviceReward, 4));
            rewardAmount += serviceReward;
        }
        if (isAgent)
        {
            //代理奖励列表
            List<Map<String, Object>> agentRewardList = userBillDao.getAgentRewardList(params);
            if (null != agentRewardList && agentRewardList.size() > 0)
            {
                for (Map<String, Object> bean : agentRewardList)
                {
                    Double agentReward = Double.parseDouble(bean.get("agentReward") + "");
                    rewardAmount = (rewardAmount + agentReward);
                    bean.put("agentReward", NumberUtil.formatDoubleStr(agentReward, 4));
                }
            }
            resultMap.put("agentRewardList", agentRewardList);
        }
        resultMap.put("rewardAmount", NumberUtil.formatDoubleStr(rewardAmount, 4));

        //累计总平台奖励：取用户账户表中的累计平台奖励
        UserAccountDto account = userAccountDao.getAccountMoney(userId);
        Double rewardTotal = 0d;
        if (null != account)
        {
            rewardTotal = account.getRewardTotal();
        }
        resultMap.put("rewardTotal", NumberUtil.formatDoubleStr(rewardTotal, 4));
        return resultMap;
    }

    @Override public Map<String, Object> rewardsum(Long userId) throws Exception
    {
        return userAccountDao.rewardsum(userId);
    }

    @Override public PageModel allrewards(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = userBillDao.allrewardsCount(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = userBillDao.allrewards(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    @Override public PageModel getMyRefUserList(Map<String, Object> map) throws Exception
    {
        PageModel pageModel = new PageModel();
        Integer count = userDao.getMyRefUserListCount(map);
        if (count != null && count != 0)
        {
            List<Map<String, Object>> resultList = userDao.getMyRefUserList(map);
            pageModel.setList(resultList);
            pageModel.setTotalItem(count);
        }
        return pageModel;
    }

    @Override public void updateUser(Long userId, String identityCardNo)
    {
        userDao.updateUser(userId, identityCardNo);
    }

    @Override public int updateIsMember(long userId, int isMember) throws Exception
    {
        return userDao.updateIsMember(userId, isMember);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.member.IMemberServcie#getUserAuditInfo(java.lang.Long)
     */
    @Override public Map<String, Object> getUserAuditInfo(Long withdrawId) throws Exception
    {
		/*
		 * 基准余额：最早一个未提现成功的前一条记录 收入总和：orderPay表统计线上收入 提现总和：withdraw表统计
		 * 可提现余额：基准点余额+收入总和-提现总和
		 */
        Map<String, Object> reaultMap = new HashMap<String, Object>();

        // 获取提现信息
        WithdrawDto withdrawDto = withdrawDao.getWithdrawById(withdrawId);
        CommonValidUtil.validObjectNull(withdrawDto, CodeConst.CODE_PARAMETER_NOT_EXIST, "提现记录不存在");
        Double withDrawAmount = withdrawDto.getAmount();
        Date withdrawTime = withdrawDto.getApplyTime();
        Long userId = withdrawDto.getUserId();
        Double nextWithdrawAmount = withdrawDto.getNextWithdrawAmount();

        // 账号信息
        UserAccountDto userAccountDB = userAccountDao.getAccountMoney(userId);

        // 冻结金额
        Double freezeAmount = userAccountDB.getFreezeAmount();

        // 获取基准余额，最近未审核id最小的一条记录的钱一条记录
        Map<String, Object> standardWithdrawDto = withdrawDao.getStandardWithdrawMoney(userId);
        Date standardWithdrawTime = null;
        BigDecimal standardMoney = new BigDecimal(0);
        if (standardWithdrawDto != null)
        {
            standardWithdrawTime = (Date) standardWithdrawDto.get("withdrawTime");
            standardMoney = (BigDecimal) standardWithdrawDto.get("nextWithdrawAmount");
        }

        // 获取成功提现总额
        Map<String, Object> withdrawSuccessTotalMoneyDto = withdrawDao
                .getUserWithdrawTotalMoney(userId, CommonConst.WITHDRAW_STATUS_SUCCESS, standardWithdrawTime,
                        withdrawTime);
        BigDecimal withdrawSuccessTotalMoney = new BigDecimal(0);
        if (withdrawSuccessTotalMoneyDto != null)
        {
            withdrawSuccessTotalMoney = (BigDecimal) withdrawSuccessTotalMoneyDto.get("withdrawTotalMoney");
        }

        // 获取审核通过提现总额
        Map<String, Object> withdrawAuditingPassTotalMoneyDto = withdrawDao
                .getUserWithdrawTotalMoney(userId, CommonConst.WITHDRAW_STATUS_AUDITING_PASS, standardWithdrawTime,
                        withdrawTime);
        BigDecimal withdrawAuditingPassTotalMoney = new BigDecimal(0);
        if (withdrawAuditingPassTotalMoneyDto != null)
        {
            withdrawAuditingPassTotalMoney = (BigDecimal) withdrawAuditingPassTotalMoneyDto.get("withdrawTotalMoney");
        }

        // 获取待审核提现总额
        Map<String, Object> withdrawTodoTotalMoneyDto = withdrawDao
                .getUserWithdrawTotalMoney(userId, CommonConst.WITHDRAW_STATUS_TODO, standardWithdrawTime,
                        withdrawTime);
        BigDecimal withdrawTodoTotalMoney = new BigDecimal(0);
        if (withdrawTodoTotalMoneyDto != null)
        {
            withdrawTodoTotalMoney = (BigDecimal) withdrawTodoTotalMoneyDto.get("withdrawTotalMoney");
        }

        // 奖励金额增加
        Map<String, Object> rewardDto = userBillDao
                .getTotalMoneyByWithdraw(userId, CommonConst.BILL_DIRECTION_ADD, CommonConst.USER_ACCOUNT_TYPE_REWARD,
                        standardWithdrawTime, withdrawTime);
        BigDecimal rewardMoney = new BigDecimal(0);
        if (rewardDto != null)
        {
            rewardMoney = (BigDecimal) rewardDto.get("money");
        }

        // 奖励金额减少负值
        Map<String, Object> consumeDto = userBillDao
                .getTotalMoneyByWithdraw(userId, CommonConst.BILL_DIRECTION_DOWN, CommonConst.USER_ACCOUNT_TYPE_REWARD,
                        standardWithdrawTime, withdrawTime);
        BigDecimal consumeMoney = new BigDecimal(0);
        if (consumeDto != null)
        {
            consumeMoney = (BigDecimal) consumeDto.get("money");//负值
        }

        // 可提现余额：基准点余额+收入总和-(提现总和)-转充金额-本次提现后余额-手续费
        BigDecimal withdrawTotalMoney = withdrawSuccessTotalMoney.add(withdrawAuditingPassTotalMoney)
                .add(withdrawTodoTotalMoney);
        //误差值=奖励-提现总和-消费总额-奖励余额
        BigDecimal errorValues = rewardMoney.subtract(withdrawTotalMoney).add(consumeMoney).subtract(new BigDecimal(nextWithdrawAmount));

        reaultMap.put("withdrawId", withdrawId);
        reaultMap.put("withDrawAmount", NumberUtil.formatDouble(withDrawAmount.doubleValue(), 2));
        reaultMap.put("consumeMoney", NumberUtil.formatDouble(consumeMoney.doubleValue(), 2));
        reaultMap.put("incomeTotalMoney", NumberUtil.formatDouble(rewardMoney.doubleValue(), 2));
        reaultMap.put("standardMoney", NumberUtil.formatDouble(standardMoney.doubleValue(), 2));
        reaultMap.put("withdrawSuccessTotalMoney", NumberUtil.formatDouble(withdrawSuccessTotalMoney.doubleValue(), 2));
        reaultMap.put("withdrawAuditingPassTotalMoney",
                NumberUtil.formatDouble(withdrawAuditingPassTotalMoney.doubleValue(), 2));
        reaultMap.put("withdrawTodoTotalMoney", NumberUtil.formatDouble(withdrawTodoTotalMoney.doubleValue(), 2));
        reaultMap.put("withdrawTotalMoney", NumberUtil.formatDouble(withdrawTotalMoney.doubleValue(), 2));
        reaultMap.put("freezeAmount", NumberUtil.formatDouble(freezeAmount.doubleValue(), 2));
        reaultMap.put("nextWithdrawAmount", NumberUtil.formatDouble(nextWithdrawAmount.doubleValue(), 2));
        reaultMap.put("withdrawTime", withdrawTime);
        reaultMap.put("standardTime", standardWithdrawTime);
        reaultMap.put("errorValues", NumberUtil.formatDouble(errorValues.doubleValue(), 2));

        return reaultMap;
    }


//    private void dealAgentReferCount(Long userId, int transformNumber, int level) throws Exception
//    {
//        if (level > 2)
//        {
//            return;
//        }
//        List<Integer> agentTypes = new ArrayList<>();
//        //3721经销商type
//        agentTypes.add(22);
//        agentTypes.add(23);
//        List<AgentDto> agentDtos = agentDao.getAgentByUserIdAndAgentTypes(userId, agentTypes);
//        if (null != agentDtos && agentDtos.size() > 0)
//        {
//            Long agentReferUserId = null;
//            Long hasDealtReferUserId = null;
//            UserDto updateUser = null;
//            UserDto tempUser = null;
//            for (AgentDto agentDto : agentDtos)
//            {
//                agentReferUserId = agentDto.getReferUserId();
//                //如果两级经销商是同一个人，则只操作一次
//                if (null != hasDealtReferUserId && hasDealtReferUserId.equals(agentReferUserId))
//                {
//                    continue;
//                }
//
//                if (null != agentReferUserId)
//                {
//                    hasDealtReferUserId = agentReferUserId;
//                    tempUser = userDao.getUserById(agentReferUserId);
//                    if (null != tempUser)
//                    {
//                        updateUser = new UserDto();
//                        if ((!tempUser.getIsGroupLeader()) && (
//                                tempUser.getReferAgentNum() + tempUser.getReferAgentNum2() + 2 > transformNumber))
//                        {
//                            updateUser.setIsGroupLeader(true);
//                        }
//                        updateUser.setUserId(agentReferUserId);
//                        updateUser.setReferAgentNum(level == 1 ? 1 : 0);
//                        updateUser.setReferAgentNum2(level == 1 ? 0 : 1);
//                        userDao.updateUser(updateUser);
//                        //处理经经销商推荐人的推荐人
//                        this.dealAgentReferCount(agentReferUserId, transformNumber, level + 1);
//                    }
//
//                }
//            }
//        }
//    }
}
