package com.idcq.appserver.service.collect;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dto.shop.*;
import com.idcq.appserver.exception.BusinessException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.controller.goods.StandardGoodsDto;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IUserXBillDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.common.ISysConfigureDao;
import com.idcq.appserver.dao.common.IUnitDao;
import com.idcq.appserver.dao.coupon.IUserCouponDao;
import com.idcq.appserver.dao.discount.IShopTimeDiscountDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.goods.IGoodsSetDao;
import com.idcq.appserver.dao.goods.IShopGoodsDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderGoodsServiceTechDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.pay.IXorderPayDao;
import com.idcq.appserver.dao.region.ICitiesDao;
import com.idcq.appserver.dao.region.IProvinceDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.shop.ITakeoutSetDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.UserXBillDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.MobileAttributionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.GoodsUnitDto;
import com.idcq.appserver.dto.order.MultiPayDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsServiceTech;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardDto;
import com.idcq.appserver.dto.storage.OperateShopStorageDto;
import com.idcq.appserver.dto.storage.StorageGoodsDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardService;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.BillUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.MobileUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.TokenUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.pinyin.PinyinUtil;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;
import com.idcq.appserver.wxscan.MD5Util;
import com.idcq.idianmgr.service.common.IMgrCommonService;

@Service
public class CollectServiceImpl implements ICollectService {
	private final static Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);
	@Autowired
	private IShopGoodsDao shopGoodsDao;
	@Autowired
	private ICollectDao collectDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IShopDeviceDao shopDeviceDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IUserAccountDao userAccountDao;
	@Autowired
	private IShopTimeDiscountDao shopTimeDiscountDao;
	@Autowired
	public IOrderDao orderDao;
	@Autowired
	public IPayDao payDao;
	@Autowired
	public IUserCashCouponDao userCashCouponDao;
	@Autowired
	public IPacketDao packetDao;
	@Autowired
	public IOrderLogDao orderLogDao;
	@Autowired
	public IXorderDao xorderDao;
	@Autowired
	public IUserCouponDao userCouponDao;
	@Autowired
	private ITakeoutSetDao takeoutSetDao;
	@Autowired
	public IUserBillDao userBillDao;
	@Autowired
	public IUserXBillDao useXBillDao;
	@Autowired
	public IOrderShopRsrcDao osrDao;
	@Autowired
	public IGoodsDao goodsDao;
	@Autowired
	public ICommonService commonService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IUnitDao unitDao;
	@Autowired
	public ICitiesDao citiesDao;
	@Autowired
	public ITransactionDao transactionDao;
	@Autowired
	private IXorderPayDao xorderPayDao;
	@Autowired
	public IOrderGoodsDao orderGoodsDao;
	@Autowired
    public IPlatformBillDao platformBillDao;
	@Autowired
	public ISysConfigureDao sysConfigureDao;
	@Autowired
    private IOrderGoodsServiceTechDao orderGoodsServiceTechDao;
	@Autowired
	public IRegionDao regionDao;
	@Autowired
	public IProvinceDao provinceDao;
	@Autowired
    public IPayServcie payServcie;
	@Autowired
    public IShopMemberDao shopMemberDao;
	@Autowired
    public IPacketService packetService;
    @Autowired
    private IUserCashCouponService userCashCouponService;
    @Autowired
    private IShopMemberCardService shopMemberCardService;
    @Autowired
    private IBusinessAreaActivityDao businessAreaActivityDao;
    @Autowired
    private IStorageServcie storageServcie;
    @Autowired
    private IShopMemberService shopMemberService;
    @Autowired
    private IOrderServcie orderService;
	@Autowired
	private ILevelService levelService;
	@Autowired
	private IMgrCommonService mgrCommonService;
	@Autowired
	private IGoodsSetDao goodsSetDao;

	public Map getShopData(Long shopId, String needGoods, String token) throws Exception {
		// ***************************查询列表信息***********************************************//
//	    SysConfigureDto sysConfigure = sysConfigureDao.getSysConfigureDtoByKey(CommonConst.HEART_BEAT_SECS);
	    ConfigDto searchCondition = new ConfigDto();
	    searchCondition.setBizId(0l);
	    searchCondition.setBizType(0);
	    searchCondition.setConfigKey(CommonConst.HEART_BEAT_SECS);
	    ConfigDto config = commonService.getConfigDto(searchCondition);
	    String heartBeatSecs = null;
	    if(config != null)
	    {
	        heartBeatSecs = config.getConfigValue();
	    }
		// 商铺信息
		Map shopInfo = getShopInfo(shopId, token);
		
		// 商铺服务信息
		List<Map> goods = null;
		
		// 0：不返回商品  1：返回商品  2015.12.15 modify by huangrui
		if(!"0".equals(needGoods)){
			goods = convertGoodsImg(collectDao.queryShopGoodsInfo(shopId));
			
			if (CollectionUtils.isNotEmpty(goods)) {
				//增加套餐内商品信息
				for (Map good : goods) {
					Integer goodsType = Integer.valueOf(good.get("goodsType").toString());
					if (goodsType.equals(3000)) {
						Long goodsId = Long.valueOf(good.get("dishId").toString());
						List<GoodsSetDto> goodsSetList = goodsSetDao.getGoodsIdListByGoodsSetId(goodsId);
						good.put("setGoodsList", goodsSetList);
					}
				}
			}
		}
				
		
		// 商铺服务分类信息
		List<Map> goodsCategory = collectDao.queryShopGoodsCategoryInfo(shopId);
	
		// 商铺员工信息
		List<Map> employees = collectDao.queryShopEmployeeInfo(shopId);
		
		// 商铺座位资源信息
		List<Map> resources = collectDao.queryShopResourceInfo(shopId);
	
		// 商铺附加费信息
		List<Map> extraFeeInfos = collectDao.queryExtraFeeInfo(shopId);
		
		// 商铺服务限时折扣信息
		// List<Map> timedDiscounts = getShopTimedDiscount(shopId);
		// 外卖设置信息
		// Map takeoutSetInfo = takeoutSetDao.queryTakeoutSetInit(shopId);
		//
		Integer settingType = CommonConst.TAKEOUT_SETTINGTYPE_WM;
		String shopMode = (String) shopInfo.get("shopMode");
		if (CommonConst.GOODS.equals(shopMode)) {
			
			//便利店模式
			settingType = CommonConst.TAKEOUT_SETTINGTYPE_PS;
		}
		
		// 外卖设置信息
		Map takeoutSetInfo = collectDao.getDistriTakeoutSetInit(shopId,
				settingType);
		
		// 商铺资源位置分类信息
		List<Map> poistionInfos = collectDao.qaueryShopResourcePositionInfo(shopId);
		
		// 商铺服务备注信息
		List<Map> cookingDetails = collectDao.qaueryShopCookingDetailInfo(shopId);
		if (cookingDetails == null || cookingDetails.size() <= 0) {
			
			// 获取平台提供的通用备注
			Long tmpShopId = CommonConst.COOKING_DETAIL_DFT_SHOPID;
			cookingDetails = collectDao.qaueryShopCookingDetailInfo(tmpShopId);
		}
		
		// ***************************查询lastUpdateTime***********************************************//
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		
		// 菜单
		Map<String, Object> goodMap = new HashMap<String, Object>();
		goodMap.put("menus", goods);
		param.put("paramStr", "goods");
		goodMap.put("lastUpdate", collectDao.queryLastUpdate(param));
		
		// 商品类别
		Map<String, Object> goodsCategoryMap = new HashMap<String, Object>();
		goodsCategoryMap.put("goodsCategory", goodsCategory);
		
		// 员工
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employees", employees);
		param.put("paramStr", "employee");
		employeeMap.put("lastUpdate", collectDao.queryLastUpdate(param));
		
		// 座位
		Map<String, Object> resourceMap = new HashMap<String, Object>();
		resourceMap.put("seats", resources);
		param.put("paramStr", "seats");
		resourceMap.put("lastUpdate", collectDao.queryLastUpdate(param));

        // 附加费
		Map<String, Object> extraFeeMap = new HashMap<String, Object>();
		extraFeeMap.put("extraFees", extraFeeInfos);
		param.put("paramStr", "extraFee");
		extraFeeMap.put("lastUpdate", collectDao.queryLastUpdate(param));
		
		// 限时折扣
		// Map<String, Object> timedDiscountMap = new HashMap<String, Object>();
		// timedDiscountMap.put("timedDiscounts", timedDiscounts);
		// param.put("paramStr", "timed");
		// timedDiscountMap.put("lastUpdate",
		// collectDao.queryLastUpdate(param));
		
		// 资源位置分类信息
		Map<String, Object> positionMap = new HashMap<String, Object>();
		positionMap.put("positions", poistionInfos);
		
		// 商铺服务备注信息
		Map<String, Object> cookingDetailMap = new HashMap<String, Object>();
		cookingDetailMap.put("cookingDetails", cookingDetails);

		param = null;
		Map<String, Object> model = new HashMap<String, Object>();
		
		// 删除shop对象中多余的返回值
		shopInfo.remove("shopMode");
		model.put("heartBeatSecs", heartBeatSecs);
		model.put("shopInfo", shopInfo);//
		model.put("menuInfo", goodMap);//
		model.put("goodsCategoryInfo", goodsCategoryMap);
		model.put("employeeInfo", employeeMap);//
		model.put("seatInfo", resourceMap);//
		model.put("extraFeeInfo", extraFeeMap);//
		// model.put("timedDiscountInfo", timedDiscountMap);//限时折扣信息，暂时取消
		model.put("takeoutSetInfo", takeoutSetInfo);//
		model.put("positionInfo", positionMap);//
		model.put("cookingDetailInfo", cookingDetailMap);//
		return model;
	}

	private List<Map> convertGoodsImg(List<Map> goods) throws Exception {
		if (null != goods && goods.size() > 0) {
			for (Map m : goods) {
				String img = CommonValidUtil.isEmpty(m.get("img")) ? null : m
						.get("img") + "";
				if (null != img) {
					m.put("img", FdfsUtil.getFileProxyPath(img));
				}
			}
		}
		return goods;
	}

	/**
	 * 初始化商铺限时折扣信息
	 * 
	 * @param shopId
	 * @return
	 */
	private List<Map> getShopTimedDiscount(Long shopId) {
		List<Map> shopTimeDiscounts = new ArrayList<Map>();
		try {
			ShopDto dto = new ShopDto();
			dto.setShopId(shopId);
			List<ShopDto> shopList = new ArrayList<ShopDto>();
			shopList.add(dto);
			// 商铺限时折扣信息
			shopTimeDiscounts = shopTimeDiscountDao
					.searchShopTimeDiscount(shopList);
			if (null != shopTimeDiscounts && shopTimeDiscounts.size() > 0) {
				List<Long> discountIds = new ArrayList<Long>();
				for (Map m : shopTimeDiscounts) {
					if (!CommonValidUtil.isEmpty(m.get("discountType"))) {
						int discountType = Integer.parseInt(m
								.get("discountType") + "");
						// 如果打折类型不是全部店铺
						if (discountType == 0) {
							discountIds.add(Long.valueOf(m.get("discountId")
									+ ""));
						}
					}
					String discountPeriodType = CommonValidUtil.isEmpty(m
							.get("discountPeriodType")) ? null : (m
							.get("discountPeriodType") + "");
					if (null != discountPeriodType) {
						if ("每天".equals(discountPeriodType)) {
							m.remove("week");
							m.remove("weekFromTime");
							m.remove("weekToTime");
							m.remove("customFromDatetime");
							m.remove("customToDatetime");
						} else if ("每周".equals(discountPeriodType)) {
							m.remove("dayFromTime");
							m.remove("dayToTime");
							m.remove("customFromDatetime");
							m.remove("customToDatetime");
						} else if ("自定义".equals(discountPeriodType)) {
							m.remove("dayFromTime");
							m.remove("dayToTime");
							m.remove("week");
							m.remove("weekFromTime");
							m.remove("weekToTime");
						}
					}
				}
				List<Map> goodIds = null;
				if (discountIds.size() > 0) {
					goodIds = shopTimeDiscountDao
							.getShopTimedDiscountGoodsId(discountIds);
					if (null != goodIds && goodIds.size() > 0) {
						for (Map m : shopTimeDiscounts) {
							if (!CommonValidUtil.isEmpty(m.get("discountType"))) {
								int discountType = Integer.parseInt(m
										.get("discountType") + "");
								// 如果打折类型不是全部店铺
								if (discountType == 0) {
									Long discountId = Long.valueOf(m
											.get("discountId") + "");
									List<Map> temp = new ArrayList<Map>();
									Iterator<Map> ite = goodIds.iterator();
									while (ite.hasNext()) {
										Map d = ite.next();
										Long discountid = Long.valueOf(d
												.get("discount_id") + "");
										if (discountId.longValue() == discountid
												.longValue()) {
											d.remove("discount_id");
											temp.add(d);
											ite.remove();
										}
									}
									m.put("goods", temp);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("收银机接口，初始化限时折扣数据异常", e);
		}
		return shopTimeDiscounts;
	}

	public Map getLastestVesion(String appIds)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		String[] keys = appIds.split(",");
		param.put("arrays", keys);
		List<Map> versions = collectDao.queryAppVersionInfo(param);
		if (null != versions && versions.size() > 0) {
			for (Map map : versions) {
				String url = CommonValidUtil.isEmpty(map.get("url")) ? null
						: map.get("url") + "";
				map.put("url", FdfsUtil.getFileProxyPath(url));
			}
		}
		// 版本
		Map<String, Object> versionMap = new HashMap<String, Object>();
		versionMap.put("versions", versions);
		param.put("paramStr", "version");
		versionMap.put("lastUpdate", collectDao.queryLastUpdate(param));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("versionInfo", versionMap);//
		return model;
	}

	private Map getShopInfo(Long shopId, String token) throws Exception {
		Map shopDto = collectDao.queryShopInfoStatus(shopId);// 不需要校验状态
		if (null == shopDto || shopDto.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_SHOP);
		}
		checkToken(shopId, token);
		Integer status = shopDto.get("shopStatus") == null ? null : Integer
				.valueOf(shopDto.get("shopStatus") + "");
		if (status != null && status.intValue() == 3) {
			String shopDeadTime = shopDto.get("shopDeadTime") + "";
			shopDto.put("shopDeadTime", DateUtils.getDateAfterDay(shopDeadTime,
					CommonConst.SHOP_DEAD_TIME));
		} else {
			shopDto.remove("shopDeadTime");
		}
		double memberDiscount = CommonValidUtil.isEmpty(shopDto
				.get("memberDiscount")) ? 1.0 : Double.valueOf(shopDto
				.get("memberDiscount") + "");
		if (memberDiscount >= 1 || memberDiscount <= 0) {
			memberDiscount = 1;
		}
		shopDto.put("memberDiscount", memberDiscount);
		setShopAdssInfo(shopDto);
		return shopDto;
	}

    private void setShopAdssInfo(Map shopDto) throws Exception {
        // 设置省、市、区、镇信息
        ProvinceDto provinceDto = provinceDao.getProvinceById(NumberUtil.stringToLong(shopDto.get("provinceId")+""));
        if (null != provinceDto) {
            shopDto.put("provinceName", provinceDto.getProvinceName());
        }
        CitiesDto citiesDto = citiesDao.getCityById(NumberUtil.stringToLong(shopDto.get("cityId")+""));
        if (null != citiesDto) {
            shopDto.put("cityName", citiesDto.getCityName());
        }
        DistrictDto districtDto = regionDao.getDistrictById(NumberUtil.stringToLong(shopDto.get("districtId")+""));
        if (null != districtDto) {
            shopDto.put("districtName", districtDto.getDistrictName());
        }
    }

	public String[] getCRAddress(String snId, String appName) throws Exception {
		// 先查询商铺路由器表是否存在此路由器
		int shopDevice = shopDeviceDao.queryShopDeviceBySnid(snId);
		if (shopDevice == 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_ROUTER);
		}
		String key = CommonConst.CONFIG_KEY_CR;
		if(!StringUtils.isBlank(appName)) {
			key = CommonConst.CONFIG_KEY_CR + "_" + appName;
		}
		String addStr = shopDeviceDao.getCRAddress(key);
		String[] adds = null;
		if (!StringUtils.isEmpty(addStr)) {
			adds = addStr.split(",");
		}
		return adds;
	}

	@Deprecated
	public boolean queryShopAndTokenExistsBak(Long shopId, String token)
			throws Exception {
		// 校验商铺
		// int count = shopDao.queryShopExists(shopId);//校验状态
		Map shop = shopDao.queryShopStatus(shopId);
		if (null == shop || shop.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_SHOP);
		}
		Integer shopStatus = CommonValidUtil.isEmpty(shop.get("shop_status")) ? null
				: Integer.parseInt(shop.get("shop_status") + "");
		int status = 0;
		if (shopStatus == null || (status = shopStatus.intValue()) == 99
				|| status == 1 || status == 2) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_SHOP_STATUS);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("token", token);
		// 商铺token校验
		int tokenCnt = shopDeviceDao.queryShopTokenExists(param);
		if (tokenCnt == 0) {
			throw new ValidateException(CodeConst.CODE_ERROR_TOKEN,
					CodeConst.MSG_ERROR_SHOP_TOKEN);
		}
		return true;
	}

	public boolean queryShopAndTokenExists(Long shopId, String token)
			throws Exception {
		boolean flag = false;
		flag = checkShop(shopId);// 返回true，表示校验通过，不同过会抛出异常
		// 商铺token校验
		if (flag) {
			flag = checkToken(shopId, token);// 返回true，表示校验通过，不同过会抛出异常
		}
		return flag;
	}
	
	public boolean checkShopExists(Long shopId) throws Exception{
		return this.checkShop(shopId);
	}
	
	/**
	 * 校验商铺
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	private boolean checkShop(Long shopId) throws Exception {
		// 校验商铺
		boolean flag = false;
		ShopDto shopDto = null;
		Integer shopStatus = null;
		Object redisShopData = DataCacheApi.getObject("shop" + shopId);
		if (!(flag = (redisShopData == null))) {
			shopDto = (ShopDto) redisShopData;
			shopStatus = shopDto.getShopStatus();
		}
		if (flag) {
			Map shop = shopDao.queryShopStatus(shopId);
			if (null == shop || shop.size() <= 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_MISS_SHOP);
			}
			shopStatus = CommonValidUtil.isEmpty(shop.get("shop_status")) ? null
					: Integer.parseInt(shop.get("shop_status") + "");
		}
		CommonValidUtil.validShopStatus(shopStatus, new Integer[]{CommonConst.SHOP_LACK_MONEY_STATUS});
		return true;
	}

	/**
	 * 校验商铺token
	 * 
	 * @param shopId
	 * @param token
	 * @return
	 */
	private boolean checkToken(Long shopId, String token) {
		boolean flag;
		String cacheToken = null;
		Object obj = DataCacheApi.getObject(CommonConst.KEY_SHOP_DEVICE
				+ shopId);
		if (obj != null) {
			Map<String, String> map = (Map) obj;
			cacheToken = map.get(CommonConst.TOKEN);
		}
		flag = (null != cacheToken && StringUtils.equals(cacheToken, token));
		if (!flag) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopId", shopId);
			param.put("token", token);
			// 商铺token校验
			int tokenCnt = shopDeviceDao.queryShopTokenExists(param);
			if (tokenCnt == 0) {
				throw new ValidateException(CodeConst.CODE_ERROR_TOKEN,
						CodeConst.MSG_ERROR_SHOP_TOKEN);
			}
			flag = true;
		}
		return flag;
	}

	public Map vertifyDevice(HttpServletRequest request) throws Exception {

		String sn = RequestUtils.getQueryParam(request, "sn");
		String mobile = RequestUtils.getQueryParam(request, "mobile");
		String userPassword = RequestUtils.getQueryParam(request, "userPassword");
		String regId = RequestUtils.getQueryParam(request, "regId");
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		//是否需要更新token,0代表需要，1代表不需要
		String isUpdateToken = RequestUtils.getQueryParam(request, "isUpdateToken");
		CommonValidUtil.validStrNull(sn, CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_REQUIRED_SHOPID);
		CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_REQUIRED_MOBILE);
		CommonValidUtil.validMobileStr(mobile,
				CodeConst.CODE_PARAMETER_NOT_VALID,
				CodeConst.MSG_REQUIRED_MOBILE_VALID);
		CommonValidUtil.validStrNull(userPassword,
				CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PWD);
		
		// 检验设备
		Map<String, Object> param = this.shopDeviceDao.getShopInfoBySn(sn);
		if (null == param) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_SHOP_DEVICE_NULL);
		}

		
		//校验用户信息
		UserDto userDB = checkUserInfo(mobile, userPassword);
		
		// 校验商铺
		List<Map<String, Object>> list = this.shopDeviceDao.getShopInfoByParam(
				mobile, shopIdStr);
		if (CollectionUtils.isEmpty(list)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_SHOP);
		}
		if (list.size() > 1 && StringUtils.isBlank(shopIdStr)) {
			throw new ValidateException(CodeConst.CODE_SHOP_NOT_BINDING,
					CodeConst.MSG_SHOP_NOT_BINDING);
		}
		Map<String, Object> map = list.get(0);
		
		// 校验商铺状态
		String shopStatus = map.get("shopStatus") + "";
		CommonValidUtil.validShopStatus(NumberUtil.stringToInteger(shopStatus), null);
		//插入登录日志
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("shopId", map.get("shopId"));
		logMap.put("infoType", new Integer(1));
		logMap.put("clientType", new Integer(1));
		logMap.put("mobile", mobile);
		mgrCommonService.saveLoginInfo(logMap);
		String oldToken = param.get("deviceToken") +"";
        if(StringUtils.isNotBlank(oldToken) && "1".equals(isUpdateToken)) {
            map.put(CommonConst.TOKEN, oldToken);

            // 返回中不需要userId 和 regId
            map.remove(CommonConst.USER_ID);
            map.remove("shopStatus");

            // 设置商铺图片
            String shopImg = (String) map.get("shopImg");
            
            //拼接图片全路径
            shopImg = FdfsUtil.getFileProxyPath(shopImg);
            map.put("shopImg", shopImg);
            return map;
        }
        
		// 先清空上一次登录的缓存信息,有可能是第一次登录，shopId有可能为空
		Object obj = param.get("shopId");
		if (null != obj) {
			DataCacheApi.del(CommonConst.KEY_SHOP_DEVICE + obj);
		}

		String token = TokenUtil.getNewToken();
		String shopId = map.get(CommonConst.SHOP_ID) + "";
		String key = CommonConst.KEY_SHOP_DEVICE + shopId;

		// 先更新商铺所有的设备编号的token和regId为空
		this.shopDeviceDao.updateShopDeviceRegNull(null, null, shopId);

		// 更新token和regid信息到1dcq_shop_device表中
		this.shopDeviceDao.updateShopDeviceRegBy(sn, regId, token, shopId);

		// 如果用户初始状态为待激活需要将用户状态修改为正常
		if (CommonConst.USER_WAIT_ACTIVE_STATUS == userDB.getStatus()) {
			// 代表第一次登陆，将状态改为正常状态
			Long userId = userDB.getUserId();
			userDB.setStatus(CommonConst.USER_NORMAL_STATUS);
			userDao.updateUserStatus(userDB);
			DataCacheApi.del(CommonConst.KEY_USER + userId);
			DataCacheApi.del(CommonConst.KEY_MOBILE + mobile);
		}

		Map<String, String> cache = new HashMap<String, String>();
		cache.put(CommonConst.TOKEN, token);
		cache.put(CommonConst.SHOP_ID, shopId);
		cache.put(CommonConst.REG_ID, regId);

		// 将登录信息放缓存
		DataCacheApi.setObject(key, cache);
		map.put(CommonConst.TOKEN, token);

		// 返回中不需要userId 和 regId
		map.remove(CommonConst.USER_ID);
		map.remove("shopStatus");

		// 设置商铺图片
		String shopImg = (String) map.get("shopImg");
		
		//拼接图片全路径
		shopImg = FdfsUtil.getFileProxyPath(shopImg);
		map.put("shopImg", shopImg);
		return map;
	}

	public Map<String, String> registerMember(HttpServletRequest request)
			throws Exception {
		String verifyCode = RequestUtils.getQueryParam(request, "verifyCode");
		String mobile = RequestUtils.getQueryParam(request, "mobile");
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String token = RequestUtils.getQueryParam(request, "token");

		CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_MOBILE);
		CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_REQUIRED_MOBILE_VALID);
		CommonValidUtil.validStrNull(shopIdStr,
				CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
		CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_REQUIRED_TOKEN);
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		// 验证验证码
		boolean flag = sendSmsService.checkSmsCodeIsOk(mobile, null, verifyCode);
		if (!flag) {
			throw new ValidateException(CodeConst.CODE_VERICODE_53101,CodeConst.MSG_VC_ERROR);
		}
		queryShopAndTokenExists(shopId, token);
		// 校验用户是否已经存在
		UserDto userDB = this.userDao.getUserByMobile(mobile);
		UserDto user = new UserDto();
		Long userId = this.shopDeviceDao.getUserIdByShopId(shopId);
		 Map<String, String> map = new HashMap<String, String>();
		if (userDB != null ) {
		    if(userDB.getIsMember() == CommonConst.USER_IS_MEMBER){
		        throw new ValidateException(CodeConst.CODE_MOBILE_REGISTERED,
	                    CodeConst.MSG_MOBILE_REGISTERED);
		    } else {
		        // 商铺推荐
		        user.setReferType(CommonConst.USER_REFERTYPE_SHOP);
		        // 商家推荐注册
		        user.setRegisterType(CommonConst.REGISTER_TYPE_COLLECT);
		        user.setReferShopId(shopId);
		        user.setReferUserId(userId);
		        user.setUserType(CommonConst.USER_TYPE_MEMBER);
		        user.setUserType2(CommonConst.USER_TYPE_MEMBER);
		        user.setIsMember(CommonConst.USER_IS_MEMBER);
		        user.setCreateTime(new Date());
				if(userDB.getRebatesLevel() == null){
					user.setRebatesLevel("normal_ratio");
				}
		        userDao.updateUser(user);
		        // 清除会员缓存
		        String mobileKey = CommonConst.KEY_MOBILE + mobile;
		        DataCacheApi.delObject(mobileKey);
		        DataCacheApi.delObject(CommonConst.KEY_USER + userId);
		       
		        map.put("mobile", mobile);

		        return map;
		        
		    }
		}

		user.setUserName(mobile);
		user.setMobile(mobile);

		//默认密码为手机号码后6位
		String passWord = mobile.substring(mobile.length() - 6);

		// 对密码进行加密
		String mdPassWord = MD5Util.getMD5Str(passWord);
		user.setPassword(mdPassWord);
		Date date = new Date();
		user.setCreateTime(date);
		user.setLastUpdateTime(date);
		user.setStatus(CommonConst.USER_NORMAL_STATUS);

		// 商铺推荐
		user.setReferType(CommonConst.USER_REFERTYPE_SHOP);
		// 商家推荐注册
		user.setRegisterType(CommonConst.REGISTER_TYPE_COLLECT);
		user.setReferShopId(shopId);
		user.setReferUserId(userId);
		user.setUserType(CommonConst.USER_TYPE_MEMBER);
		user.setUserType2(CommonConst.USER_TYPE_MEMBER);
		user.setIsMember(CommonConst.USER_IS_MEMBER);
		// 获取手机归属地信息
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
		user.setRebatesLevel("normal_ratio");
		// 注册该用户
		this.userDao.saveUser(user);
		UserAccountDto userAccountDto = new UserAccountDto();

		// 正常状态
		userAccountDto.setAccountStatus(CommonConst.ACCOUNT_NORMAL_STATUS);
		userAccountDto.setCreateTime(new Date());
		userAccountDto.setTelephone(mobile);
		userAccountDto.setUserId(user.getUserId());
		userAccountDto.setAmount(0d);
		userAccountDto.setUserRole(CommonConst.MEMBER);

		// 创建账号
		this.userAccountDao.saveAccount(userAccountDto);

		// 商铺推荐会员添加
		Map<String, Object> userRefer = new HashMap<String, Object>();
		userRefer.put("referMobile", mobile);
		userRefer.put("userId", userId);
		String createTime = DateUtils.format(new Date(),
				DateUtils.DATETIME_FORMAT);
		userRefer.put("createTime", createTime);
		this.userDao.isnertUserReferInfo(userRefer);
		map.put("mobile", mobile);
		map.put("passWord", passWord);

		return map;
	}

	public Map<String, Object> searchMember(String mobile, Long shopId, Double money, Long businessAreaActivityId)
			throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    if(null != shopId) {
	        ShopMemberDto shopMemberDto = shopMemberDao.getShopMbByMobileAndShopId(mobile, shopId, CommonConst.MEMBER_STATUS_DELETE);
	        if(shopMemberDto != null) {
	            map.put("shopMemberInfo", shopMemberDto);
	        }
	     
	        List<BusinessAreaActivityDto> activitys = businessAreaActivityDao.getBusinessAreaActivityBy(shopId, businessAreaActivityId, DateUtils.format(new Date(), DateUtils.DATE_FORMAT));
	        if (CollectionUtils.isNotEmpty(activitys)) {
	            List<Object> list = new ArrayList<Object>();
	            for (BusinessAreaActivityDto activity : activitys) {
	                Map<String, Object> activityInfo = new HashMap<String, Object>();
	              
	                Double sendMoney = packetService.getRedPacketSendMoney(activity.getBusinessAreaActivityId(), money);
	                activityInfo.put("businessAreaActivityId", activity.getBusinessAreaActivityId());
	                activityInfo.put("sendMoney", sendMoney);
	                activityInfo.put("activityRuleName", activity.getActivityRuleName());
	                list.add(activityInfo);
	            }
	            map.put("activityInfo", list);
	        }
	    }
	    
		UserDto userDB = this.userDao.getUserByMobile(mobile);
		if(userDB != null) {
	        String name = userDB.getTrueName();
	        if (StringUtils.isNotBlank(name)) {
	            // 取首位再来拼接
	            name = name.substring(0, 1);
	            if (userDB.getSex() == 0) {
	                //男-0，女-1,未知-2
	                // 性别男用先生来代替后两位
	                name += CommonConst.MAN;
	            } else if (userDB.getSex() == 1) {

	                // 性别女用女士来代替后两位
	                name += CommonConst.MADAM;
	            } else {

	                // 不知的用先生/女士来代替
	                name += CommonConst.HUMAN;
	            }
	        } else {

	            // 如果没有真实姓名就用昵称替代
	            name = userDB.getNikeName();
	        }
	        String imgSmall = FdfsUtil.getFileProxyPath(userDB.getSmallLogo());
	        Long vantages = shopDao.getVantagesBy(shopId, userDB.getUserId());
	        //在该店铺可用红包金额
	        Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userDB.getUserId(), RedPacketStatusEnum.USEABLE.getValue());
	        Double voucherAmount = userAccountDao.getAccountMoney(userDB.getUserId()).getVoucherAmount();
	        map.put("isMember", userDB.getIsMember());
	        map.put("name", name);
	        map.put("userImg", imgSmall);
	        map.put("vantages", vantages == null ? 0 : vantages);
	        map.put("redPacketAmount", redPacketAmount);
	        map.put("userId", userDB.getUserId());
	        map.put("voucherAmount", voucherAmount);
			map.put("rebatesLevel", userDB.getRebatesLevel());
		}
		
		return map;
	}

	public void shopCancelOrder(Long shopId, String orderId, String auditFlag,
			String refundType, String refuseReason) throws Exception {
		OrderDto order = this.orderDao.getOrderMainById(orderId);
		CommonValidUtil.validObjectNull(order,
				CodeConst.CODE_PARAMETER_NOT_NULL,
				CodeConst.MSG_ORDER_NOT_EXIST);
		if (!shopId.equals(order.getShopId())) {
			throw new ValidateException(CodeConst.CODE_ORDER_NOT_SHOP,
					"数据错误，非指定店铺订单");
		}
		Integer orderStatus = order.getOrderStatus();
		// 订单已完成3, 订单派送中-2, 已退单 5
		if (CommonConst.ORDER_STS_PSZ == orderStatus) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_SEND,
					"订单状态为已派送,不允许取消订单");
		} else if (CommonConst.ORDER_STS_YJZ == orderStatus) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_FINISH,
					"订单状态为已完成,不允许取消订单");
		} else if (CommonConst.ORDER_STS_YTD == orderStatus) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_CANCEL,
					"订单状态已退单,不允许取消订单");
		}
		OrderDto orderInfo = new OrderDto();
		orderInfo.setOrderId(orderId);
		orderInfo.setPayStatus(order.getPayStatus());
		// 同意退单
		if ((CommonConst.AUDITFLAG_ZERO).equals(auditFlag)) {
			if (CommonConst.REFUNDTYPE_ONE.equals(refundType)) {
				// 退款操作
				dealRefund(order);
			}
			// 退现金，不需要操作传奇宝账户
			// 将订单改为已退单、同意取消订单
			orderInfo.setRefuseReason(refuseReason);
			orderInfo.setOrderStatus(CommonConst.ORDER_STS_YTD);

		} else {

			// 商家拒绝退单，回退订单状态
			OrderLogDto orderLog = orderLogDao.queryEntityByOrderIdDescLimit1(orderId);
			if (null != orderLog) {
				orderInfo.setOrderStatus(orderLog.getOrderStatus());
			} else {
				orderInfo.setOrderStatus(CommonConst.ORDER_STS_YYD);
			}
			orderInfo.setRefuseReason(refuseReason);
		}
		String remark = "商家处理订单操作";
		ShopDto shopDto = shopDao.getNormalShopById(shopId);
		CommonValidUtil
				.validObjectNull(shopDto,
						CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_MISS_SHOP);
		// 处理订单状态
		dealOrderStatus(orderInfo, shopDto.getPrincipalId(), remark);
		collectDao.updateShopResourceStatus(orderId,CommonConst.RESOURCE_STATUS_NOT_IN_USE);
//		}
	}

	/**
	 * 处理退款和释放资源操作
	 * 
	 * @param shopId
	 * @param orderId
	 * @param order
	 * @throws Exception
	 */
	public synchronized void dealRefund(OrderDto order) throws Exception {

		logger.info("---------------开始处理退款和释放资源操作---------------");
		// 释放资源
		String orderId = order.getOrderId();
		// 为了防止多次退款操作，每次退款前再查询一次订单状态
		Integer status = orderDao.getOrderStatusById(orderId);
		if (CommonConst.ORDER_STS_YTD == status) {
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_CANCEL,
					"订单状态已退单,不允许取消订单");
		}
		// 释放1dcq_order_shop_resource表中的资源
		osrDao.updateStatusByOrderId(orderId,
				CommonConst.OSRESOURCE_STATUS_INVALID);
		// 释放1dcq_shop_resource表中的资源
		collectDao.updateShopResourceStatus(orderId,
				CommonConst.RESOURCE_STATUS_NOT_IN_USE);
		if (order.getIsMember() == CommonConst.USER_IS_NOT_MEMBER) 
		{
		    //非会员订单不处理退款
		    return;
		}
		// 退款
		Long billLogo = shopDeviceDao.getLogoIdByShopId(order.getShopId());
		List<PayDto> payList = payDao.getOrderPayList(orderId,CommonConst.PAY_STATUS_PAY_SUCCESS);
		if (CollectionUtils.isNotEmpty(payList)) {
			for (PayDto payDto : payList) 
			{
                if (payDto.getPayType() == null) 
				{
					continue;
				}
				int payType = payDto.getPayType();
				Long payId = payDto.getPayId();
				Double payAmount = payDto.getPayAmount();
				if (payType == CommonConst.PAY_TYPE_CASH_COUPON) {// 代金券支付
					UserCashCouponDto userCashCouponDto = userCashCouponDao
							.getUserCashCouponInfo(payId);
					if (null != userCashCouponDto) {
						Double accountAmount = 0D;
						Double usedPrice = userCashCouponDto.getUsedPrice();
						// 余额
						accountAmount = NumberUtil.sub(userCashCouponDto.getPrice(), usedPrice);
						// 退款后使用额
						usedPrice = NumberUtil.sub(usedPrice, payAmount);
						usedPrice = usedPrice < 0 ? 0 : usedPrice;
						// 退代金券操作
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("payId", payId);
						param.put("usedPrice", usedPrice);
						userCashCouponDao.updateUserCashCoupon(param);
						// 记录账单
						addUserXBill(order, payDto, accountAmount);
					}

				} else if (payType == CommonConst.PAY_TYPE_RED_PACKET) {// 红包支付
				    RedPacket redPacket = packetDao.queryRedPacketById(payId);
				    if (null == redPacket) {
				        return;
				    }
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("redPacketId", payId);
					param.put("status", RedPacketStatusEnum.USEABLE.getValue());
					param.put("payAmount", payAmount);
					// 更新红包金额
					packetDao.updateRedPacketFlag(param);
					Double accountAmount = redPacket.getAmount();
					Double accountAfterAmount = NumberUtil.add(accountAmount, payAmount);
					//记录账单
					addUserBill(accountAmount, accountAfterAmount, payAmount, order, billLogo, CommonConst.USER_ACCOUNT_TYPE_RED_PACKET);
				} else if (payType == CommonConst.PAY_TYPE_COUPON) {// 优惠劵
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("payId", payId);
					param.put("couponStatus", 1);
					userCouponDao.updateUserCouponByMap(param);// 更优惠劵状态
				}
			}
			UserBillDto userBillDto = new UserBillDto();
	        userBillDto.setOrderId(orderId);
	        userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
	        List<UserBillDto> userBills = userBillDao.getUserBillByDto(userBillDto);
			if (CollectionUtils.isNotEmpty(userBills)) 
	        {
			    UserAccountDto userAccount = userAccountDao.getAccountMoney(order.getUserId());
		        if (null == userAccount) 
		        {
		            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_ACCOUNT_NOT_EXIST);
		        }
		        Double accountAmount = userAccount.getAmount();
                Double couponAmount = userAccount.getCouponAmount();
                Double rewardAmount = userAccount.getRewardAmount();
	            for (UserBillDto userBill : userBills)
	            {
	                Double useMoney = Math.abs(userBill.getMoney());
	                
	                Integer accountType = userBill.getAccountType();
	                accountAmount = NumberUtil.add(accountAmount, useMoney);
	                if(null == accountType)
	                {
	                    logger.error("账单记录有误，账单ID为："+userBill.getBillId());;
	                    continue;
	                }
	                if(CommonConst.USER_ACCOUNT_TYPE_REWARD == accountType)
	                {
	                    rewardAmount = NumberUtil.add(rewardAmount, useMoney);
	                    addUserBill(accountAmount, rewardAmount, useMoney, order, billLogo, accountType);
	                    userAccountDao.updateUserAccount(order.getUserId(), useMoney, useMoney, null, null, null,
	                    									null,null,null,null,null,null,null,null,null);
	                } 
	                else if (CommonConst.USER_ACCOUNT_TYPE_MONETARY == accountType)
	                {
	                    couponAmount = NumberUtil.add(couponAmount, useMoney);
	                    addUserBill(accountAmount, couponAmount, useMoney, order, billLogo, accountType);
	                    userAccountDao.updateUserAccount(order.getUserId(), useMoney, null, null, useMoney, null,
	                    								null,null,null,null,null,null,null,null,null);
	                }
	            }
	        }
			List<PlatformBillDto>  platformBills = platformBillDao.getPlatformBillByOrderId(orderId);
			if (CollectionUtils.isNotEmpty(userBills)) 
			{
			    for (PlatformBillDto platformBillDto : platformBills)
                {
			        //退款对平台来说是钱减少
			        platformBillDto.setMoney(-platformBillDto.getMoney());
			        platformBillDto.setBillType(CommonConst.CHARGEBACK);
			        platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_BACK);
			        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			        platformBillDto.setBillDesc(CommonConst.CHARGEBACK);
			        platformBillDao.insertPlatformBill(platformBillDto);
                }
			}
		}
		
	}

	/**
	 * 用户退单保存账单
	 * 
	 * @param order
	 * @param payAmount
	 * @param billDesc
	 * @throws Exception
	 */
	private void addUserBill(Double accountAmount, Double accountAfterAmount, Double money, OrderDto order, Long billLogo, Integer accountType) throws Exception {
		UserBillDto userBillDto = new UserBillDto();
		userBillDto.setBillTitle(CommonConst.CHARGEBACK + money);
		userBillDto.setBillType(CommonConst.CHARGEBACK);
		userBillDto.setMoney(money);
		// 已预定状态
		userBillDto.setBillStatus(ConsumeEnum.HAVE_CHARGEBACK.getValue());
		userBillDto.setCreateTime(new Date());
		// '账单状态的进行中标记：1（进行中），0（已完成）',
		userBillDto.setBillStatusFlag(0);
		// 账单类型:1（账户资金增加）,-1（账户资金减少）
		userBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
		userBillDto.setOrderId(order.getOrderId());
		userBillDto.setUserId(order.getUserId());
		userBillDto.setBillDesc(CommonConst.CHARGEBACK);
		userBillDto.setBillLogo(billLogo);
		userBillDto.setOrderPayType(CommonConst.PAY_TYPE_SINGLE);
		
		userBillDto.setAccountAmount(accountAmount);
		  
		userBillDto.setAccountAfterAmount(accountAfterAmount);
		userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_BACK);
		userBillDto.setAccountType(accountType);
		//设置账单不可现
		userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
		userBillDao.insertUserBill(userBillDto);

	}

	private void addUserXBill(OrderDto order, PayDto payDto,
			Double accountAmount) throws Exception {
		UserXBillDto userXBill = new UserXBillDto();
		userXBill.setUserId(order.getUserId());
		userXBill.setUccId(payDto.getPayId());
		userXBill.setOrderPayType(payDto.getOrderPayType());
		userXBill.setOrderId(order.getOrderId());
		userXBill.setMoney(payDto.getPayAmount());
		userXBill.setCreateTime(new Date());
		userXBill.setBillType(CommonConst.USER_CASHCOUPON_BACK);
		userXBill.setBillTitle(CommonConst.CHARGEBACK + payDto.getPayAmount());
		userXBill.setBillStatus(ConsumeEnum.HAVE_CHARGEBACK.getValue());
		userXBill.setBillDesc("消费卡退款");
		userXBill.setAccountAmount(accountAmount);
		useXBillDao.insertUserXBillDao(userXBill);
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public Map getOrderDetail4CR(Long shopId, String orderId)
			throws Exception {
		Map order = collectDao.getOrderDetail4CR(orderId);
		if (null == order || order.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_ORDER);
		}
		BigDecimal payedAmount = this.packetDao.queryOrderPayAmount(orderId, CommonConst.PAY_TYPE_SINGLE);
        order.put("payedAmount", payedAmount);
		List<Map> goods = collectDao.getOrderGoodsDetail(orderId);
//		for (Map map : goods) {
//			String goodsProValuesIds = (String) map.get("goodsProValuesIds");
//			
//			StringBuilder goodsProValuesNameBuilder = new StringBuilder("");
//			if(goodsProValuesIds!=null){
//				String[] ids = goodsProValuesIds.split("\\,");
//				for (String id : ids) {
//					String propertyValue = shopGoodsDao.getDtoProValueName(id);
//					if(propertyValue!=null){
//						goodsProValuesNameBuilder.append(propertyValue+",");
//					}
//				}
//			}
//			if(goodsProValuesNameBuilder!=null&&goodsProValuesNameBuilder.length()>0&&goodsProValuesNameBuilder.lastIndexOf(",")==goodsProValuesNameBuilder.length()-1){
//				map.put("goodsProValuesName", goodsProValuesNameBuilder.substring(0, goodsProValuesNameBuilder.length()-1));
//			}
//			
//		}
		
		order.put("goods", goods);
		
		Map param = new HashMap();
		param.put("orderId", orderId);
		List<Map> orderPays = collectDao.getOrderPayDetail(param);
		order.put("payList", orderPays);
		order.put("orderId", orderId);
		return order;
	}

	public List<Map> getOrderPayDetail(Long shopId, String orderId, String token)
			throws Exception {
		queryShopAndTokenExists(shopId, token);
		int cnt = orderDao.queryOrderExists(orderId);
		if (cnt == 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_ORDER);
		}
		Map param = new HashMap();
		param.put("orderId", orderId);
		// int totalCount = collectDao.getOrderPayDetailCount(param);
		List<Map> orderPays = collectDao.getOrderPayDetail(param);
		return orderPays;
	}

	public void reportRegId(String sn, String regId, String token,
			String shopIdStr) throws Exception {
		// 检验设备
		Map<String, Object> param = this.shopDeviceDao.getShopInfoBySn(sn);
		if (null == param) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_SHOP_DEVICE_NULL);
		}
		Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
		queryShopAndTokenExists(shopId, token);
		this.shopDeviceDao.updateShopDeviceRegBy(sn, regId, token, shopIdStr);
		// 先清空上一次登录的缓存信息,有可能是第一次登录，shopId有可能为空
		Object obj = param.get("shopId");
		if (null != obj) {
			DataCacheApi.del(CommonConst.KEY_SHOP_DEVICE + obj);
		}
		Map<String, String> cache = new HashMap<String, String>();
		cache.put(CommonConst.TOKEN, token);
		cache.put(CommonConst.SHOP_ID, shopIdStr);
		cache.put(CommonConst.REG_ID, regId);

		// 将信息放缓存
		DataCacheApi.setObject(CommonConst.KEY_SHOP_DEVICE + shopIdStr, cache);
	}

	public void modifyAWifiMacWhiteList(ShopDeviceDto shopDeviceDto, int cmd)
			throws Exception {
		// 校验商铺及token
		queryShopAndTokenExists(shopDeviceDto.getShopId(),
				shopDeviceDto.getDeviceToken());
		int cn = shopDeviceDao.queryShopDeviceBySnid(shopDeviceDto.getSnId());
		if (cn == 0) {
			if (cmd == 2 || cmd == 3) {
				String key = (cmd == 2 ? "删除" : "修改");
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
						"设备信息不存在，无法执行" + key + "操作");
			} else if (cmd == 1) {// 新增设备
				shopDeviceDto.setDeviceStatus(3);// 默认已绑定
				shopDeviceDao.insertShopDevice(shopDeviceDto);
			}
		} else {
			if (cmd == 1) {
				// 如果已经存在，则修改
				shopDeviceDao.updateShopDeviceBySnId(shopDeviceDto);
				// throw new
				// ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
				// "设备信息已存在，无法执行新增操作");
			} else if (cmd == 2) {// 删除设备
				shopDeviceDao.deleteShopDeviceBySnId(shopDeviceDto);
			} else if (cmd == 3) {// 修改设备
				shopDeviceDao.updateShopDeviceBySnId(shopDeviceDto);
			}
		}

	}

	public List<Map> getWifiMacWhitelist(Long shopId, String token)
			throws Exception {
		// 校验商铺及token
		queryShopAndTokenExists(shopId, token);
		List<Map> list = shopDeviceDao.getWifiMacWhitelist(shopId);
		return list;
	}

	@Override
	public List<Map> getOwnShopList(String mobile, String userPassword,
			String shopMode, String authentication) throws Exception {
		UserDto user = null;
		//校验用户状态和密码
		if(!"100".equals(authentication))
		{
			user = checkUserInfo(mobile, userPassword);
		}
		if(null == user)
		{
			user = userDao.getUserByMobile(mobile);
			if(null == user)
			{
				return null;
			}
		}
		//根据所属类型获取店铺列表
		List<Map> list = shopDeviceDao.getOwnShopList(user.getUserId(),
				shopMode, authentication);
		return list;
	}

	public int dealOrderStatus(OrderDto orderInfo, Long userId, String remark)
			throws Exception {

		orderInfo.setLastUpdateTime(new Date());
		int num = orderDao.updateOrder(orderInfo);
		if (num > 0) {
			OrderLogDto orderLogDto = new OrderLogDto();
			orderLogDto.setLastUpdateTime(new Date());
			orderLogDto.setOrderId(orderInfo.getOrderId());
			orderLogDto.setOrderStatus(orderInfo.getOrderStatus());
			orderLogDto.setUserId(userId);
			orderLogDto.setPayStatus(orderInfo.getPayStatus());
			orderLogDto.setRemark(remark);
			orderLogDao.saveOrderLog(orderLogDto);
		}

		return num;
	}

	/**
	 * 校验用户状态和密码
	 * 
	 * @param mobile
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	private UserDto checkUserInfo(String mobile, String userPassword)
			throws Exception {

		//校验用户状态
		UserDto userDB = checkUserStatus(mobile);
		
		// 校验密码
		if (!StringUtils.equals(userPassword, userDB.getPassword())) {
			throw new ValidateException(CodeConst.CODE_PWD_ERROR,
					CodeConst.MSG_PWD_ERROR);
		}
		return userDB;
	}

	/**
	 * 校验用户状态
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	private UserDto checkUserStatus(String mobile) throws Exception {
		
		// 根据用户名获取密码,这里使用手机登陆
		UserDto userDB = this.userDao.getUserByMobile(mobile);

		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);

		// 校验用户状态1代表正常状态
		Integer status = userDB.getStatus();
		CommonValidUtil.validObjectNull(status, CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
		if (CommonConst.USER_FREEZE_STATUS == status) {
			
			//状态冻结
			throw new ValidateException(CodeConst.CODE_USER_STATUS_FREEZE, CodeConst.MSG_USER_STATUS_FREEZE_FAIL);
		} else if (CommonConst.USER_LOGOUT_STATUS == status) {
			
			//注销
			throw new ValidateException(CodeConst.CODE_USER_STATUS_LOGOUT,
					CodeConst.MSG_USER_STATUS_LOGOUT_FAIL);
		}
		return userDB;
	}

	public List<OrderDto> timeProcessingOrder(Long userId, String remark)
			throws Exception {
		List<OrderDto> returnList = null;
		List<OrderDto> orderList = orderDao
				.getOrderDtoByStatus(CommonConst.ORDER_STS_DQR);
		OrderDto orderInfo;
		if (CollectionUtils.isNotEmpty(orderList)) {
			returnList = new ArrayList<OrderDto>();
			for (OrderDto orderDto : orderList) {
				// 超过时间需要自动将订单修改为已退单，同时还要进行修改订单状态操作
				dealRefund(orderDto);
				orderInfo = new OrderDto();
				orderInfo.setOrderId(orderDto.getOrderId());
				orderInfo.setOrderStatus(CommonConst.ORDER_STS_YTD);
				orderInfo.setPayStatus(orderDto.getPayStatus());
				if(orderInfo.getPayStatus() == 0){
					// add by huangrui 2015.9.9
					orderInfo.setRefuseReason("超时用户未支付自动退单");
				}else{
					orderInfo.setRefuseReason("超时不接单自动退单");
				}
				dealOrderStatus(orderInfo, userId, remark);
				returnList.add(orderDto);
			}
		}
		return returnList;
	}

	@Override
	public int updateGoodsPrice(Long shopId, Long goodsId, Double standardPrice)
			throws Exception {
		// goodsId存在性校验
		int flag = this.goodsDao.queryGoodsExists(goodsId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST,
				CodeConst.MSG_MISS_GOOD);
		GoodsDto goods = new GoodsDto();
		goods.setGoodsId(goodsId);
		// 目录价
		goods.setStandardPrice(standardPrice);
		// 获取商铺会员折扣
		Double memberDiscount = this.shopDao.getMemberDiscount(shopId);
		// 折扣率等于null或小于等于0，则直接返回1
		memberDiscount = (memberDiscount == null ? 1D
				: (memberDiscount <= 0 ? 1D : memberDiscount));
		// 会员价 ＝　折扣价　＝ 目录价　×　商铺会员折扣
		Double vipPrice = new BigDecimal(standardPrice + "").multiply(
				new BigDecimal(memberDiscount + "")).doubleValue();
		goods.setVipPrice(vipPrice);
		goods.setDiscountPrice(vipPrice);
		this.goodsDao.updateGoodsPrice(goods);
		// 删除商品缓存
		String key = CommonConst.KEY_GOODS + goodsId;
		DataCacheApi.delObject(key);
		return 1;
	}

	@Override
	public boolean checkPassword(Long shopId, String token, String password)
			throws Exception {
		queryShopAndTokenExists(shopId, token);
		String passwordDB = shopDeviceDao.getPasswordByShopId(shopId);
		if (StringUtils.equals(password, passwordDB)) {
			return true;
		}
		return false;

	}
	
	public void delAndResumeOrder(Long shopId, String orderId,
			String operationType) throws Exception {
		//会员
		OrderDto orderDto = orderDao.getOrderMainById(orderId);
		if (null == orderDto) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_ORDER);
		}
		if (orderDto != null && !shopId.equals(orderDto.getShopId())) {
			throw new ValidateException(CodeConst.CODE_ORDER_NOT_SHOP,
					"数据错误，非指定店铺订单");
		}
		if(null != orderDto){
			if (CommonConst.ORDER_STS_YJZ != orderDto.getOrderStatus() && CommonConst.ORDER_STS_YTD != orderDto.getOrderStatus()) {
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_MISMATCH,
						"订单状态不是已完成和已退订,不允许删除和恢复订单");
			} 
			int oldDeleteType = orderDto.getDeleteType() == null  ? CommonConst.ORDER_DELETE_TYPE_WSC : orderDto.getDeleteType();
			delOrderDeleteType(orderId, operationType, orderDto, oldDeleteType);
			
		}
		
	}

	/**
	 * 修改会员订单和非会员订单是否被删除
	 * 
	 * @Function: com.idcq.appserver.service.collect.CollectServiceImpl.delOrderDeleteType
	 * @Description:
	 *
	 * @param orderId
	 * @param operationType
	 * @param orderDto
	 * @param xorderDto
	 * @param oldDeleteType
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月16日 下午2:49:29
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月16日    shengzhipeng       v1.0.0         create
	 */
	private void delOrderDeleteType(String orderId, String operationType, OrderDto orderDto, int oldDeleteType)
			throws Exception {
		Integer deleteType = null;
		boolean updateFlag = false;
		logger.info("更新前订单delete_type字段为：" + oldDeleteType);
		if (CommonConst.DELETE_ORDER_COLL.equals(operationType)) {
			//删除操作
			if(oldDeleteType == CommonConst.ORDER_DELETE_TYPE_WSC) {
				//代表商家删除
				deleteType = CommonConst.ORDER_DELETE_TYPE_SJS;
				updateFlag = true;
			} else if(oldDeleteType == CommonConst.ORDER_DELETE_TYPE_YHS) {
				//代表商家和用户都删除
				deleteType = CommonConst.ORDER_DELETE_TYPE_LDS;
				updateFlag = true;
			}
		} else if (CommonConst.RESUME_ORDER_COLL.equals(operationType)){
			//恢复操作
			if(oldDeleteType == CommonConst.ORDER_DELETE_TYPE_SJS) {
				//只有商家删除恢复就是未删除
				deleteType = CommonConst.ORDER_DELETE_TYPE_WSC;
				updateFlag = true;
			} else if(oldDeleteType == CommonConst.ORDER_DELETE_TYPE_LDS) {
				//商家和用户都删除恢复就是用户删除
				deleteType = CommonConst.ORDER_DELETE_TYPE_YHS;
				updateFlag = true;
			}
		}
		if(updateFlag) {
			logger.info("更新订单delete_type字段为：" + deleteType);
			if (null != orderDto) {
				orderDao.updateOrderDeleteType(orderId, deleteType);
			}  
		}
	}

	public void reportAppVersion(Long shopId, String appName, String appDesc,
			String appVersion, String sn) throws Exception {
		String shopAppVersion = collectDao.getShopAppVersion(appName, shopId);
		if (StringUtils.isBlank(shopAppVersion)){
			//不存在就新增
			ShopAppVersionDto shopAppVersionDto = getShopAppVersionDto(shopId, appName, appDesc, appVersion, sn);
			collectDao.addAppVersion(shopAppVersionDto);
			collectDao.addAppVersionLog(shopAppVersionDto);
		} else {
			if(!shopAppVersion.equals(appVersion)) {
				//版本号不相同就需要更新版本号
				ShopAppVersionDto shopAppVersionDto = getShopAppVersionDto(shopId, appName, appDesc, appVersion, sn);
				collectDao.updateAppVersion(shopAppVersionDto);
				collectDao.addAppVersionLog(shopAppVersionDto);
			} 
		}
		
	}
	
	private ShopAppVersionDto getShopAppVersionDto(Long shopId, String appName, String appDesc,
			String appVersion, String sn){
		ShopAppVersionDto shopAppVersionDto = new ShopAppVersionDto();
		shopAppVersionDto.setAppDesc(appDesc);
		shopAppVersionDto.setAppVersion(appVersion);
		shopAppVersionDto.setShopId(shopId);
		shopAppVersionDto.setSnId(sn);
		shopAppVersionDto.setAppName(appName);
		return shopAppVersionDto;
	}

	public Map queryStandardGoods(String barcode) {
		return collectDao.getStandardGoodsByBarCode(barcode);
	}

	public Map syncGoodsInfo(StandardGoodsDto standardGoodsDto)
			throws Exception {
		//shopid
		Long shopId = standardGoodsDto.getShopId();
		//令牌
		String token = standardGoodsDto.getToken();
		//商品条码
		String barcode = standardGoodsDto.getBarcode();
		
		String unitName = standardGoodsDto.getUnitName();
		//商品类型
		Integer goodsType= standardGoodsDto.getGoodsType();//商品类型
		Long goodsId = standardGoodsDto.getGoodsId();
		CommonValidUtil.validObjectNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
//		CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_TOKEN);
		CommonValidUtil.validStrNull(standardGoodsDto.getGoodsName(), CodeConst.CODE_PARAMETER_NOT_NULL, "goodsName不允许为空");
		CommonValidUtil.validObjectNull(standardGoodsDto.getPrice(), CodeConst.CODE_PARAMETER_NOT_NULL, "price不允许为空");
        //非法关键字屏蔽
        SensitiveWordsUtil.checkSensitiveWords("unitName", unitName);
        SensitiveWordsUtil.checkSensitiveWords("goodsName", standardGoodsDto.getGoodsName());

		//获取单价id
		int digitScale = standardGoodsDto.getDigitScale() == null ? 0 : standardGoodsDto.getDigitScale();
		Long unitId = saveUnit(unitName, shopId, digitScale);
		
		ShopDto shopDto = shopDao.getShopById(shopId);
		CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP);
		Double memberDiscount = shopDto.getMemberDiscount() == null ? 1 : shopDto.getMemberDiscount();
		//目录价
		Double standardPrice = standardGoodsDto.getPrice();
		//折扣价
		Double discountPrice = standardPrice * memberDiscount;
		//商品名字
		String goodsName = standardGoodsDto.getGoodsName();
		GoodsDto goodsDto = new GoodsDto();
		goodsDto.setIsNeedCheck(standardGoodsDto.getIsNeedCheck());
		goodsDto.setShopId(shopId);
		goodsDto.setGoodsNo(barcode);
		String goodsNo = standardGoodsDto.getGoodsNo();
		if(StringUtils.isNotBlank(goodsNo)) {
		    goodsDto.setGoodsNo(standardGoodsDto.getGoodsNo());
		}
		
		goodsDto.setBarcode(barcode);
		goodsDto.setGoodsName(goodsName);
		goodsDto.setStandardPrice(standardPrice);
		goodsDto.setVipPrice(discountPrice);
		goodsDto.setDiscountPrice(discountPrice);
		goodsDto.setUnitId(unitId);
		goodsDto.setUnitName(unitName);
		goodsDto.setGoodsStatus(CommonConst.GOODS_STATUS_SJ); //上架状态
		String pinyinCode = PinyinUtil.getPinYinHeadChar(goodsName);
		goodsDto.setPinyinCode(pinyinCode);//首字母拼音
		goodsDto.setSpecsDesc(standardGoodsDto.getSpecifications());
		goodsDto.setGoodsCategoryId(standardGoodsDto.getGoodsCategoryId());
		goodsDto.setGoodsPriceSpec(standardGoodsDto.getGoodsPriceSpec());
        if (standardGoodsDto.getStorageTotalNumber() != null) {
        	goodsDto.setStorageTotalNumber(standardGoodsDto.getStorageTotalNumber());
        }
        
        if (standardGoodsDto.getAlarmNumberMax() != null) {
        	goodsDto.setAlarmNumberMax(standardGoodsDto.getAlarmNumberMax());
        }
        
        if (standardGoodsDto.getAlarmNumberMin() != null) {
        	goodsDto.setAlarmNumberMin(standardGoodsDto.getAlarmNumberMin());
        
        }

		goodsDto.setSellMode(standardGoodsDto.getSellMode());
		goodsDto.setSellModeValue(standardGoodsDto.getSellModeValue());

		if ((null == goodsId || goodsId == 0) && StringUtils.isNotBlank(barcode)) { 
			//先查询商品是否存在，根据shopId和条形码barcode
			goodsId = goodsDao.getShopGoodsIdByBarcode(shopId, barcode);
		}
		if (null == goodsId || goodsId == 0) {
			//新增
			goodsDto.setCreateTime(new Date());
			goodsDto.setGoodsType(goodsType);
			goodsDao.addGoodsDto(goodsDto);
			goodsId = goodsDto.getGoodsId();
			//库存不为0需要进行入库操作
			if (null != standardGoodsDto.getStorageTotalNumber() && standardGoodsDto.getStorageTotalNumber() != 0D) {
			    OperateShopStorageDto operateShopStorage = new OperateShopStorageDto();
			    operateShopStorage.setOperaterId(0L);
			    operateShopStorage.setOperaterName("");
			    operateShopStorage.setShopId(shopId);
			    operateShopStorage.setStorageRemark("添加商品入库");
			    operateShopStorage.setStorageTime(DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
			    operateShopStorage.setStorageType(12);
			    operateShopStorage.setStorageNo(FieldGenerateUtil.generatebitStorageNoId(CommonConst.STORAGE_NOPREFIX_RK+shopId));
			    List<StorageGoodsDto> goodsList = new ArrayList<StorageGoodsDto>();
			    StorageGoodsDto storageGoodsDto = new StorageGoodsDto();
			    storageGoodsDto.setGoodsId(goodsId);
			    storageGoodsDto.setStorageNumber(goodsDto.getStorageTotalNumber());
			    storageGoodsDto.setAlarmNumberMax(standardGoodsDto.getAlarmNumberMax());
			    storageGoodsDto.setAlarmNumberMin(standardGoodsDto.getAlarmNumberMin());
			    //计算价格-start
			    Double price = standardGoodsDto.getPrice()==null ? 0D : standardGoodsDto.getPrice();
			    Double storageTotalNumber =  goodsDto.getStorageTotalNumber()==null ? 0D : standardGoodsDto.getStorageTotalNumber();
			    storageGoodsDto.setStoragePrice(price);
			    storageGoodsDto.setGoodsTotalPrice(NumberUtil.multiply(price, storageTotalNumber));
			    //计算价格-end
			    goodsList.add(storageGoodsDto);
			    operateShopStorage.setGoodsList(goodsList);
			    operateShopStorage.setIsUpdateGoods(false);//不更新商品数量
			    storageServcie.operateShopStorage(operateShopStorage);
			}
		} else {
			//修改
			goodsDto.setGoodsId(goodsId);
			goodsDao.updateGoodsDto(goodsDto);
			//清空缓存
			DataCacheApi.del(CommonConst.KEY_GOODS);
		}
		Map map = new HashMap();
		map.put("goodsId", goodsId);
		map.put("pinyinCode", pinyinCode);
		return map;
	}
	
	/**
	 * 获取单位id
	 * 
	 * @Function: com.idcq.appserver.service.collect.CollectServiceImpl.getUnitId
	 * @Description:
	 *
	 * @param unitName 单位名称
	 * @param shopId 店铺id
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午4:43:22
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	public Long saveUnit(String unitName, Long shopId, Integer digitScale) {
		Long unitId = unitDao.getUnitIdByName(unitName, digitScale, shopId);
		if (null == unitId) {
			Unit unit= new Unit();
			unit.setUnitName(unitName);
			unit.setStatus(1); //上架状态
			unit.setUnitType(2); //通用
			unit.setDigitScale(digitScale==null ? 2 : digitScale); 
			unit.setShopId(shopId);
			unitDao.addGoodsUnit(unit);
			return unit.getUnitId();
		}
		return unitId;
	}

	public Map<String, Object> multiplePayFromXorder(MultiPayDto multiPayDto,
			OrderDto xorderDto,List<OrderGoodsServiceTech> serviceTechs) throws Exception {
		int orderStatus=xorderDto.getOrderStatus();
		//反结账订单状态为已完成也可以支付
		if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=xorderDto.getSettleFlag().intValue()) 
				|| CommonConst.ORDER_STS_TDZ == orderStatus 
				|| CommonConst.ORDER_STS_YTD == orderStatus){
			//订单状态为不可支付状态
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
		}
		Integer payType = multiPayDto.getPayType();
		Map<String,Object> result = new HashMap<String, Object>();
		boolean payFlag = true;
		switch(payType.intValue()){
			case CommonConst.MULTI_PAYTYPE_CASH:
				//现金支付
				result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.PAY_TYPE_CASH);
				break;
			case CommonConst.MULTI_PAYTYPE_POS:
				//POS 支付
				result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.PAY_TYPE_POS);
				break;
			case CommonConst.MULTI_PAYTYPE_SHOP_MEMBER_CARD:
				result = shopMemberCardMutiPay(xorderDto, multiPayDto);
				result.put("orderStatus", CommonConst.ORDER_STS_YJZ);
				xorderDto.setLastUpdateTime(new Date());
				xorderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
				xorderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
				orderDao.updateOrder(xorderDto);
		         //生成订单操作日志
	            saveOrderLog(multiPayDto.getData().getCashierId(), xorderDto, "组合支付"+" 收银员操作");
				break;
           case CommonConst.MULTI_PAYTYPE_FREE:
                //免单
                result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_FREE);
                break;
           case CommonConst.MULTI_PAYTYPE_CUSTOM1:
                //自定义支付1
                result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM1);
                break;
           case CommonConst.MULTI_PAYTYPE_CUSTOM2:
                //自定义支付2
                result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM2);
                break;
           case CommonConst.MULTI_PAYTYPE_CUSTOM3:
                //自定义支付3
                result = cashMultiPayXorder(xorderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM3);
                break;
           case CommonConst.MULTI_PAYTYPE_NO_PAY:
			    logger.info("只生产订单信息，不进行支付");
			    payFlag = false;
			    break;
		}
		if (payFlag) {
		    if(serviceTechs != null && serviceTechs.size() > 0){
	            orderGoodsServiceTechDao.batchAddOrderGoodsServiceTech(serviceTechs);
	        }
	        if(String.valueOf(CommonConst.ORDER_STS_YJZ).equals(result.get("orderStatus")+"")) {
	            //统计销量
	            orderService.updateGoodsAndShopSoldNum(multiPayDto.getData().getId());
                //插入反结账订单商品线上支付账单
                payServcie.insertReverseShopBill(xorderDto);
	            //修改库存
                storageServcie.insertShopStorageByOrderId(xorderDto.getOrderId(),xorderDto.getShopId());
                //释放商铺资源
	            collectDao.updateShopResourceStatus(xorderDto.getOrderId(), CommonConst.RESOURCE_STATUS_NOT_IN_USE);
	            //非会员订单结算
	            OrderGoodsSettleUtil.detailSingleXorder(xorderDto.getOrderId());
	            //订单信息统计
	            orderService.handleAccountingStatByUser(xorderDto);
	        }
		}
		return result;
	}
	
	/**
	 * 非会员订单现金支付
	 * @param xorderDto 非会员订单
	 * @param multiPayDto 支付请求参数
	 * @param payType 支付类型
	 * @throws Exception
	 */
	private Map<String,Object> cashMultiPayXorder(OrderDto orderDto,MultiPayDto multiPayDto, int payType) throws Exception{
		String orderId = orderDto.getOrderId();
	    // 获取订单实际需要支付的金额(settlePrice)
        BigDecimal needPayAmount = packetDao.queryOrderAmount(orderId);
        logger.info("====订单实际需要支付金额："+needPayAmount);
        // 获取订单实际已经支付的金额(单订单支付)
        BigDecimal beforePayAmount =  packetDao.queryOrderPayAmount(orderId,0);
        if(beforePayAmount == null){
            beforePayAmount = new BigDecimal(0);
        }
		logger.info("====订单已支付金额："+beforePayAmount);
		//订单需要支付总金额
		if (beforePayAmount.compareTo(needPayAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()) {
			throw new ValidateException(CodeConst.CODE_72405, "超额支付");
		}
		//订单剩余支付金额：实际需要支付金额-已经支付金额
		BigDecimal surplusAmount = needPayAmount.subtract(beforePayAmount);
		logger.info("====订单剩余支付金额："+surplusAmount);
		//本次希望支付的金额
		BigDecimal wantAmount = new BigDecimal(multiPayDto.getPayMoney()+"");
		logger.info("====订单本次希望支付金额："+wantAmount);
		if(wantAmount.compareTo(surplusAmount) > 0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "不需要支付这么多了");
		}
		if(wantAmount.compareTo(surplusAmount) == 0){
			logger.info("=======完全支付");
			//完全支付：改变订单状态、订单支付状态，记录订单日志
		    orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
		    orderDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
		    OrderDto xDto = new OrderDto();
			xDto.setOrderId(orderId);
			xDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
			xDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			orderDao.updateOrder(xDto);
			
			OrderLogDto orderLogDto=new OrderLogDto();
			orderLogDto.setOrderId(orderId);
			orderLogDto.setPayStatus(orderDto.getPayStatus());
			orderLogDto.setOrderStatus(orderDto.getOrderStatus());
			orderLogDto.setLastUpdateTime(new Date());
			orderLogDto.setUserId(multiPayDto.getData().getCashierId());
			orderLogDto.setRemark((payType == CommonConst.PAY_TYPE_CASH?"现金支付":"POS支付")+" 收银员操作");
			orderLogDao.saveOrderLog(orderLogDto);
	        
		}
		PayDto orderPayDto = new PayDto();
		double  payAmount =Double.parseDouble(wantAmount+"");
		orderPayDto.setOrderId(orderId);
		orderPayDto.setPayType(payType);
		orderPayDto.setPayAmount(payAmount);
		orderPayDto.setOrderPayType(0);
		orderPayDto.setOrderPayTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		orderPayDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP); // 收款人类型
		orderPayDto.setShopId(multiPayDto.getShopId());
		orderPayDto.setPayIndex(multiPayDto.getPayIndex());
		orderPayDto.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		orderPayDto.setChangeMoney(multiPayDto.getOddChange());//找零金额
		orderPayDto.setRealMoney(multiPayDto.getRealCharges());//实收金额
		orderPayDto.setUserId(orderDto.getUserId());
		if(payAmount>0){
			this.payDao.addOrderPay(orderPayDto);
		}
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("payMoney", wantAmount);
		result.put("orderPaidMoney", beforePayAmount.add(wantAmount));
		result.put("orderPayStatus", orderDto.getPayStatus());
		result.put("orderStatus", orderDto.getOrderStatus());
		return result;
	}

	public Map<String, Object> multiplePayFromOrder(MultiPayDto multiPayDto,
			OrderDto orderDto,List<OrderGoodsServiceTech> serviceTechs) throws Exception {
		Integer payType = multiPayDto.getPayType();
		Integer orderStatus = orderDto.getOrderStatus();
		Integer payStatus = orderDto.getPayStatus();
		logger.info("支付前的订单状态：" + orderStatus);
		Map<String,Object> result = new HashMap<String, Object>();
		boolean payFlag = true;
		switch(payType.intValue()){
			case CommonConst.MULTI_PAYTYPE_CQB:
				//传奇宝支付
				result = cqbMultiPay(orderDto, multiPayDto);
				break;
			case CommonConst.MULTI_PAYTYPE_XFK:
                //消费卡支付
                result = cfkMultiPay(orderDto, multiPayDto);
                break;
			case CommonConst.MULTI_PAYTYPE_HB:
                //红包支付
                result = hbMultiPay(orderDto, multiPayDto);
                break;
			case CommonConst.MULTI_PAYTYPE_CASH:
				//现金支付
				result = cashMultiPay(orderDto, multiPayDto,CommonConst.PAY_TYPE_CASH);
				break;
			case CommonConst.MULTI_PAYTYPE_POS:
				//POS 支付
				result = cashMultiPay(orderDto, multiPayDto,CommonConst.PAY_TYPE_POS);
				break;
			case CommonConst.MULTI_PAYTYPE_SMS:
				//短信支付
				result = smsMultiPay(orderDto, multiPayDto);
				break;
			case CommonConst.MULTI_PAYTYPE_NO_PAY:
				logger.info("只生产订单信息，不进行支付");
				payFlag = false;
				break;
			case CommonConst.MULTI_PAYTYPE_SHOP_MEMBER_CARD:
				result = shopMemberCardMutiPay(orderDto, multiPayDto);
				break;
			case CommonConst.MULTI_PAYTYPE_FREE:
                //免单
                result = cashMultiPay(orderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_FREE);
                break;
			case CommonConst.MULTI_PAYTYPE_CUSTOM1:
                //自定义支付1
                result = cashMultiPay(orderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM1);
                break;
			case CommonConst.MULTI_PAYTYPE_CUSTOM2:
                //自定义支付2
                result = cashMultiPay(orderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM2);
                break;
			case CommonConst.MULTI_PAYTYPE_CUSTOM3:
                //自定义支付3
                result = cashMultiPay(orderDto, multiPayDto,CommonConst.MULTI_PAYTYPE_CUSTOM3);
                break;
			default:
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "未知的支付类型");
		}
		if(serviceTechs != null && serviceTechs.size() > 0){
			orderGoodsServiceTechDao.batchAddOrderGoodsServiceTech(serviceTechs);
		}
		if (payFlag) {
			String orderId = orderDto.getOrderId();
			BigDecimal needPayAmount = packetDao.queryOrderAmount(orderId);
	        logger.info("====订单实际需要支付金额："+needPayAmount);
	        // 获取订单实际已经支付的金额(单订单支付)
	        BigDecimal beforePayAmount =  packetDao.queryOrderPayAmount(orderId,0);
	        if(beforePayAmount == null){
	            beforePayAmount = new BigDecimal(0);
	        }
	        logger.info("====订单已经支付金额："+beforePayAmount);
	        if(orderStatus == CommonConst.ORDER_STS_YJZ 
	        		&& needPayAmount.compareTo(beforePayAmount) != 0
	        		&& CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()) {
	            throw new ValidateException(CodeConst.CODE_ORDER_PAY_MONEY_ERROR, "订单实际支付金额与需要支付金额不符");
	        }
	        if(needPayAmount.compareTo(beforePayAmount) == 0){
	          
	            orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
	            //修改订单状态及支付状态
	            OrderDto oDto = new OrderDto();
	            //发送红包
	            Double sendMoney = packetService.sendRedPacketToUser(orderDto);
	            if(sendMoney != 0) {
	                //更新订单结算价格
	                Double orderRealSettlePrice = NumberUtil.sub(orderDto.getOrderRealSettlePrice(), sendMoney);
	                if (orderRealSettlePrice < 0) {
	                    orderRealSettlePrice = 0D;
	                }
	                oDto.setOrderRealSettlePrice(orderRealSettlePrice);
	                oDto.setSendRedPacketMoney(sendMoney);
	                result.put("sendRedPacketMoney", sendMoney);
	                
	            }
	           
	            oDto.setOrderId(orderId);
	            oDto.setLastUpdateTime(new Date());
	            oDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
	            oDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
	            payStatus = CommonConst.PAY_STATUS_PAYED;
	            orderStatus = CommonConst.ORDER_STS_YJZ;
	            orderDao.updateOrder(oDto);
	            
	            //生成订单操作日志
	            saveOrderLog(multiPayDto.getData().getCashierId(), oDto, "组合支付"+" 收银员操作");
	            
				//修改库存
				storageServcie.insertShopStorageByOrderId(orderId,orderDto.getShopId());
				
				//释放商铺资源
	            collectDao.updateShopResourceStatus(orderDto.getOrderId(), CommonConst.RESOURCE_STATUS_NOT_IN_USE);
	            
	            //统计销量
	            orderService.updateGoodsAndShopSoldNum(orderId);
	            
                //插入反结账订单商品线上支付账单
                payServcie.insertReverseShopBill(orderDto);
                
                //商铺统计（反结账）
                handleAccountingStatByReverse(orderDto);

	            
	            if (orderDto.getIsMember() == 1) {
	            	 levelService.pushAddPointMessage(5, null, 1, orderDto.getShopId().intValue(), 4, orderId,true);
	            }
	            if(multiPayDto.getAutoSettleFlag() == null || multiPayDto.getAutoSettleFlag() == CommonConst.AUTO_SETTLE_FLAG_TRUE) {
	                //结算
	                OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId,0);
	            }
	        }
			
	        result.put("orderPayStatus", payStatus);
	        result.put("orderStatus", orderStatus);
	        result.put("orderPaidMoney", beforePayAmount);
		}
		return result;
	}
	/**
	 * 修改统计信息,反结账
	 */
	public void handleAccountingStatByReverse(OrderDto orderDto) throws Exception{
		Integer settleFlag = orderDto.getSettleFlag();
		if(settleFlag!=null){
			if(CommonConst.REVERSE_SETTLE_FLAG == settleFlag.intValue()){
				//删除原始统计信息
				shopDao.deleteShopIncomeStat(orderDto.getOrderId());
				//插入统计信息
				orderService.handleAccountingStatByUser(orderDto);
			}
		}

	}	
	
	/**
     * 短信组合支付,红包支付
     * @param orderDto
     * @param multiPayDto
     * @return
     * @throws Exception
     */
    private Map<String,Object> hbMultiPay(OrderDto orderDto,MultiPayDto multiPayDto) throws Exception{
        String orderId = orderDto.getOrderId();
        //计算订单实际需要支付金额
        Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
        // 获取订单实际已经支付的金额(单订单支付)
//        BigDecimal beforePayAmount =  amountMap.get("beforePayAmount");
        //本次希望支付的金额
        BigDecimal wantAmount = amountMap.get("wantAmount");
        //订单剩余支付金额
        BigDecimal surplusAmount = amountMap.get("surplusAmount");

        //红包支付金额
        BigDecimal usedRedPacketMoney = new BigDecimal(0);
        
        if(wantAmount.compareTo(surplusAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
            throw new ValidateException(CodeConst.CODE_72405, "超额支付");
        }
        logger.info("====订单本次实际需要支付金额："+wantAmount);
        
        UserDto user = userDao.getUserByMobile(multiPayDto.getData().getMobile());
        if(null == user){
            user = userDao.getUserById(orderDto.getUserId()); 
        }
        logger.info("会员信息："+user);
        Long userId = user.getUserId();
        //使用短信支付的总金额（消费卡支付金额+传奇宝支付金额）
//        BigDecimal smsPayAmount = new BigDecimal(0);
        //剩余支付金额
//        BigDecimal payBalance = wantAmount;
        //红包剩余金额
//        BigDecimal redPacketPrice = new BigDecimal(0);
        
        //红包支付
        Map<String,Object> result = redPacketPayFromSmsPay(multiPayDto, orderDto, userId, wantAmount, true);
//        redPacketPrice = (BigDecimal) result.get("redPacketPrice");
        usedRedPacketMoney = (BigDecimal) result.get("payAmount");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("payMoney", usedRedPacketMoney);
//        resultMap.put("orderPaidMoney", beforePayAmount.add((wantAmount.subtract(payBalance))));
//        resultMap.put("orderPayStatus", orderDto.getPayStatus());
//        resultMap.put("orderStatus", orderDto.getOrderStatus());
//        resultMap.put("redPacketPrice", redPacketPrice);
//        resultMap.put("smsPayAmount", smsPayAmount);//本次短信支付金额
        resultMap.put("smsPayAmount", usedRedPacketMoney);//本次短信支付金额
        resultMap.put("usedRedPacketMoney", usedRedPacketMoney);
        
        return resultMap;
    }
	
	/**
     * 短信组合支付,消费卡
     * @param orderDto
     * @param multiPayDto
     * @return
     * @throws Exception
     */
    private Map<String,Object> cfkMultiPay(OrderDto orderDto,MultiPayDto multiPayDto) throws Exception{
        String orderId = orderDto.getOrderId();
        //计算订单实际需要支付金额
        Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
        // 获取订单实际已经支付的金额(单订单支付)
//        BigDecimal beforePayAmount =  amountMap.get("beforePayAmount");
        //本次希望支付的金额
        BigDecimal wantAmount = amountMap.get("wantAmount");
        //订单剩余支付金额
        BigDecimal surplusAmount = amountMap.get("surplusAmount");

        if(wantAmount.compareTo(surplusAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
            throw new ValidateException(CodeConst.CODE_72405, "超额支付");
        }
        logger.info("====订单本次实际需要支付金额："+wantAmount);
        
        UserDto user = userDao.getUserByMobile(multiPayDto.getData().getMobile());
        if(null == user){
            user = userDao.getUserById(orderDto.getUserId()); 
        }
        logger.info("会员信息："+user);
        Long userId = user.getUserId();
        //使用短信支付的总金额（消费卡支付金额+传奇宝支付金额）
//        BigDecimal smsPayAmount = new BigDecimal(0);
        //消费卡剩余金额
//        BigDecimal uccPrice = new BigDecimal(0);
         //消费卡支付金额
        BigDecimal cashCouponPayment = new BigDecimal(0);
        
        //消费卡支付
        Map<String,Object> result = cashCouponPayFromSmsPay(multiPayDto, orderDto, userId, wantAmount, true);
//        uccPrice = (BigDecimal) result.get("uccPrice");
        cashCouponPayment = (BigDecimal) result.get("payAmount");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("payMoney", cashCouponPayment);
//        resultMap.put("orderPaidMoney", beforePayAmount.add((wantAmount.subtract(payBalance))));
//        resultMap.put("orderPayStatus", orderDto.getPayStatus());
//        resultMap.put("orderStatus", orderDto.getOrderStatus());
//        resultMap.put("uccPrice", uccPrice);
        resultMap.put("smsPayAmount", cashCouponPayment);//本次短信支付金额
        return resultMap;
    }
	
    private Map<String, Object> shopMemberCardMutiPay(OrderDto orderDto,MultiPayDto multiPayDto) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        String orderId = orderDto.getOrderId();
        Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
        BigDecimal beforePayAmount = amountMap.get("beforePayAmount");
        //订单希望支付金额
        BigDecimal wantAmount = amountMap.get("wantAmount");
        //订单剩余支付金额
        BigDecimal surplusAmount = amountMap.get("surplusAmount");

        if(wantAmount.compareTo(surplusAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
            throw new ValidateException(CodeConst.CODE_72405, "超额支付");
        }
        logger.info("====订单本次店内会员卡实际需要支付金额："+wantAmount);
        
        ShopMemberCardDto shopMemberCardDto = new ShopMemberCardDto();
        shopMemberCardDto.setConsumeMoney(wantAmount.doubleValue());
        shopMemberCardDto.setShopId(orderDto.getShopId());
        shopMemberCardDto.setOrderId(orderId);
        String shopMemberMobile = multiPayDto.getData().getMobile();
        
        if (shopMemberMobile == null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "店铺会员手机号不能为空");
        }
        
        shopMemberCardDto.setMobile(shopMemberMobile);
        Double afterAmount = shopMemberCardService.shopMemberCardComsume(shopMemberCardDto, orderId);// 会员卡消费
        
		//生成订单支付记录
        if(shopMemberCardDto.getConsumeMoney()>0){
        	String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
    		PayDto payDto = new PayDto();
    		payDto.setOrderId(orderId);
    		payDto.setLastUpdateTime(nowTime);
    		payDto.setOrderPayTime(nowTime);
    		payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP);
    		payDto.setPayId(shopMemberCardDto.getCardId().longValue());
    		payDto.setPayType(CommonConst.PAY_TYPE_CARD);
    		payDto.setOrderPayType(0);
    		payDto.setPayAmount(shopMemberCardDto.getConsumeMoney());
    		payDto.setShopId(shopMemberCardDto.getShopId());
    		payDto.setPayIndex(multiPayDto.getPayIndex());
    		payDto.setUserPayTime(multiPayDto.getPayClientTime());
    		payDto.setChangeMoney(multiPayDto.getOddChange());//找零金额
    		payDto.setRealMoney(multiPayDto.getRealCharges());//实收金额
    		payDto.setClientSystem(multiPayDto.getClientSystem());
    		payDto.setUserId(shopMemberCardDto.getUserId());
    		payDao.addOrderPay(payDto);
        }
		
        result.put("payMoney", wantAmount);
		result.put("orderPaidMoney", beforePayAmount.add(wantAmount));
		result.put("orderPayStatus", CommonConst.PAY_STATUS_PAYED);
		result.put("afterAmount", afterAmount);
    	return result;
    }
	/**
	 * 短信组合支付，优先级：消费金>传奇宝>平台奖励>推荐奖励
	 * @param orderDto
	 * @param multiPayDto
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> smsMultiPay(OrderDto orderDto,MultiPayDto multiPayDto) throws Exception{
		String orderId = orderDto.getOrderId();
		//计算订单实际需要支付金额
		Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
		// 获取订单实际已经支付的金额(单订单支付)
//		BigDecimal beforePayAmount =  amountMap.get("beforePayAmount");
		//本次希望支付的金额
		BigDecimal wantAmount = amountMap.get("wantAmount");
		//订单剩余需要支付金额
		BigDecimal surplusAmount = amountMap.get("surplusAmount");

		if(wantAmount.compareTo(surplusAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
			//wantAmount = surplusAmount;
			throw new ValidateException(CodeConst.CODE_72405, "超额支付");
		}
		logger.info("====订单本次实际需要支付金额："+wantAmount);
		

		UserDto user = userDao.getUserByMobile(multiPayDto.getData().getMobile());
		if(null == user){
            user = userDao.getUserById(orderDto.getUserId()); 
        }
		logger.info("会员信息："+user);
		Long userId = user.getUserId();
		//使用短信支付的总金额（消费卡支付金额+传奇宝支付金额）
		BigDecimal smsPayAmount = new BigDecimal(0);
		//短信支付标识：只要使用了一次支付，则true，则需要记录transcation
		String smsPayflag = "fail";
		//剩余支付金额（多次支付）
		BigDecimal payBalance = wantAmount;
		//消费卡剩余金额
//		BigDecimal uccPrice = new BigDecimal(0);
//		//传奇宝余额
//		BigDecimal cqbBalance = new BigDecimal(0);
		//传奇宝支付金额
		BigDecimal onLinePayment = new BigDecimal(0);
		//消费卡支付金额
		BigDecimal cashCouponPayment = new BigDecimal(0);
		//红包支付金额
		BigDecimal usedRedPacketMoney = new BigDecimal(0);
		
		//账户总金额
		UserAccountDto accountDto = userAccountDao.getAccountMoney(userId);
        //消费金余额
        Double couponAmount = accountDto.getCouponAmount();
        //平台奖励余额
        Double rewardAmount = accountDto.getRewardAmount();
        
        OrderDto temp = new OrderDto();
        temp.setShopId(orderDto.getShopId());
        temp.setSettlePrice(wantAmount.doubleValue());
        //可用代金券
        Double avaliVoucherAmount = orderService.countVoucherDeduction(userId, temp);
        //传奇宝账户可用于支付的余额
        Double accountUsableBalance = NumberUtil.add(couponAmount, rewardAmount, avaliVoucherAmount);
        //在该店铺可用红包金额
        Double redPacketAmount = packetDao.getRedPacketAmountBy(orderDto.getShopId(), userId, RedPacketStatusEnum.USEABLE.getValue());
        Double couponBalance = userCashCouponService.getUserCashCouponBalance(userId);
        //可用总余额
        Double totleUsableBalance = NumberUtil.add(NumberUtil.add(accountUsableBalance, couponBalance), redPacketAmount);
        if(totleUsableBalance == 0) {
            throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE, CodeConst.MSG_ACCOUNT_NOT_BALANCE);
        }
        //重新计算需要支付的金额
        wantAmount = NumberUtil.countRecentNum(wantAmount, new BigDecimal(totleUsableBalance.toString()));
        
		Map<String,Object> redPacketResult = redPacketPayFromSmsPay(multiPayDto, orderDto, userId, wantAmount, false);
		BigDecimal balance = (BigDecimal) redPacketResult.get("payBalance");
		//红包支付金额
		usedRedPacketMoney = (BigDecimal) redPacketResult.get("payAmount");
		//消费卡支付
		Map<String,Object> result = cashCouponPayFromSmsPay(multiPayDto, orderDto, userId, balance, false);
//		uccPrice = (BigDecimal) result.get("uccPrice");
		cashCouponPayment = (BigDecimal) result.get("payAmount");
		//传奇宝支付
		cqbMulitPay(result, userId, multiPayDto, orderDto);
		payBalance = (BigDecimal) result.get("payBalance");
//		cqbBalance = (BigDecimal) result.get("cqbBalance");
		onLinePayment = (BigDecimal) result.get("cqbPayAmount");
		smsPayAmount = new BigDecimal(result.get("smsPayAmount").toString()).add(usedRedPacketMoney);
		smsPayflag = (String) result.get("smsPayflag");
		List<PayDto> payDtos = (List<PayDto>) result.get("payDtos");
		List<UserBillDto> userBillDtos = (List<UserBillDto>) result.get("userBillDtos");
		PlatformBillDto platformBillDto = (PlatformBillDto) result.get("platformBillDto");
		
		if (smsPayflag != null && "success".equals(smsPayflag)) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime=format.format(new Date());
			TransactionDto transactionDto=new TransactionDto();
			transactionDto.setUserId(userId);
			transactionDto.setOrderId(orderId);
			transactionDto.setPayAmount(Double.parseDouble(result.get("cqbPayAmount")+""));//支付金额
			transactionDto.setTransactionTime(nowTime);
			transactionDto.setStatus(1);//支付成功
			transactionDto.setUserPayChannelId(new Long(1));
			transactionDto.setOrderPayType(0);
			transactionDto.setLastUpdateTime(nowTime);
			transactionDto.setTransactionType(0);
			//生成流水记录
			transactionDao.addTransaction(transactionDto);
			
			if (payDtos != null && payDtos.size() > 0) {
				for(PayDto payDto : payDtos){
					payDto.setPayId(transactionDto.getTransactionId());
					if(payDto.getPayAmount()>0){
						payDao.addOrderPay(payDto);
					}
				}
			}
			if (userBillDtos != null && userBillDtos.size() > 0) {
				for(UserBillDto userBillDto : userBillDtos){
					userBillDto.setTransactionId(transactionDto.getTransactionId());
					userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
					userBillDao.insertUserBill(userBillDto);
				}
			}
			if (null != platformBillDto) {
				platformBillDto.setTransactionId(transactionDto.getTransactionId());
				platformBillDao.insertPlatformBill(platformBillDto);
			}
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("payMoney", wantAmount.subtract(payBalance));
//		resultMap.put("orderPaidMoney", beforePayAmount.add((wantAmount.subtract(payBalance))));
//		resultMap.put("uccPrice", uccPrice);
//		resultMap.put("cqbBalance", cqbBalance);
		resultMap.put("smsPayAmount", smsPayAmount);//本次短信支付金额
//		resultMap.put("redPacketPrice", redPacketResult.get("redPacketPrice"));
		resultMap.put("onLinePayment", onLinePayment);
		resultMap.put("cashCouponPayment", cashCouponPayment);
		resultMap.put("usedRedPacketMoney", usedRedPacketMoney);
		
		
		return resultMap;
	}
	
	/**
	 * 短信支付-传奇宝支付（消费金、奖励支付）
	 * @param preResult 上一次支付结果
	 * @param userId 用户编号
	 * @param multiPayDto 
	 * @param orderDto 订单信息
	 * @throws Exception
	 */
	private void cqbMulitPay(Map<String,Object> preResult,Long userId,MultiPayDto multiPayDto,OrderDto orderDto) throws Exception{
		//使用短信支付的总金额（消费卡支付金额+传奇宝支付金额）
		BigDecimal smsPayAmount = (BigDecimal) preResult.get("smsPayAmount");
		//短信支付标识：只要使用了一次支付，则true，则需要记录transcation
		String smsPayflag = (String) preResult.get("smsPayflag");
		//剩余支付金额（要支付的金额）
		
		BigDecimal payBalance = (BigDecimal) preResult.get("payBalance");
		
//		ShopDto shop = shopDao.getShopEssentialInfo(orderDto.getShopId());
		Boolean canUseVoucher = false;
//		if (shop.getShopIdentity() == 2) {
//			canUseVoucher = true;//代金券只能在红店使用
//		}
		
		//足额支付标记
		boolean fullPayFlag = false;
		//传奇宝余额(消费金+平台奖励)
		BigDecimal cqbBalance = new BigDecimal(0);
		//账户中amount字段
		BigDecimal cqbAmount = new BigDecimal(0);
		//本次使用传奇宝支付金额
        BigDecimal cqbPayAmount = new BigDecimal(0);
		//如果使用消费金未支付完，则使用传奇宝支付
		// 查询传奇宝账户
		UserAccountDto account = userAccountDao.getAccountMoney(userId);
		logger.info("====使用传奇宝支付，账户信息："+account);
		if(null != account){
	
			//代金券
			BigDecimal voucherAmount = new BigDecimal(account.getVoucherAmount() == null ? 0 : account.getVoucherAmount());
			BigDecimal avaliVoucherAmount = new BigDecimal(orderService.countVoucherDeduction(userId, orderDto));
			//消费金
			BigDecimal couponAmount = new BigDecimal((account.getCouponAmount() == null?0d:account.getCouponAmount())+"");
			//奖励余额
			BigDecimal rewardAmount = new BigDecimal((account.getRewardAmount() == null?0d:account.getRewardAmount())+"");
			logger.info("消费金余额："+couponAmount);
			logger.info("奖励余额："+rewardAmount);
			if (canUseVoucher) {
				logger.info("代金券余额：{}",voucherAmount);
				logger.info("代金券可用金额：{}",avaliVoucherAmount);
				cqbBalance = couponAmount.add(rewardAmount).add(avaliVoucherAmount);
				logger.info("传奇宝实际余额(传奇宝余额=消费金+奖励+代金券可用金额)："+cqbBalance);
			}else {
				cqbBalance = couponAmount.add(rewardAmount);
				logger.info("传奇宝实际余额(传奇宝余额=消费金+奖励)："+cqbBalance);
			}
			 //传奇宝总余额
            cqbAmount = cqbBalance;
            logger.info("传奇宝余额-amount："+cqbAmount);
			//重新计算需要支付的金额
			payBalance = NumberUtil.countRecentNum(payBalance, cqbBalance);
			
			if (payBalance.compareTo(new BigDecimal(0)) > 0) {//还需要继续支付
				List<PayDto> payDtos = new ArrayList<PayDto>();
				List<UserBillDto> userBillDtos = new ArrayList<UserBillDto>();
				//每次支付的金额
				BigDecimal payAmount = new BigDecimal(0);
				//支付后消费金余额
				BigDecimal afterCouponAmount = couponAmount;
				//支付后奖励余额
				BigDecimal afterRewardAmount = rewardAmount;
				
				BigDecimal afterVoucherAmount = voucherAmount;
				BigDecimal tmpCouponAmount = NumberUtil.formatVal(couponAmount+"", 2);//只取2位小数进行计算
				//如果未支付完，则使用平台奖励支付
				fullPayFlag = false;
				payAmount = new BigDecimal(0);
				
				if (avaliVoucherAmount.compareTo(new BigDecimal(0)) > 0 && canUseVoucher) {
					smsPayflag = "success";
					if (payBalance.compareTo(avaliVoucherAmount) > 0) {
						payBalance = payBalance.subtract(avaliVoucherAmount);//本次支付后剩余金额
						payAmount = avaliVoucherAmount;//本次支付金额
					}else{
						fullPayFlag = true;
						payAmount = payBalance;//本次支付金额
						payBalance = new BigDecimal(0);//本次支付后剩余金额
					}
					
					afterVoucherAmount = afterVoucherAmount.subtract(payAmount);
					cqbPayAmount = cqbPayAmount.add(payAmount);
					cqbBalance = cqbBalance.subtract(payAmount);//传奇宝余额=支付前余额-本次支付金额
					useCqbPay(userBillDtos,payDtos,orderDto, userId, payAmount, fullPayFlag, cqbAmount,afterVoucherAmount,multiPayDto,CommonConst.USER_ACCOUNT_TYPE_VOUCHER,CommonConst.PAY_TYPE_VOUCHER);
					cqbAmount = cqbAmount.subtract(payAmount);
					
					logger.info("支付后代金券余额："+afterVoucherAmount);
				}
				
				if (payBalance.compareTo(new BigDecimal(0)) > 0) {//还需要继续支付
					if(tmpCouponAmount.compareTo(new BigDecimal(0)) > 0){//消费金金额大于0
						smsPayflag = "success";
						if (payBalance.compareTo(tmpCouponAmount) > 0) {
							payBalance = payBalance.subtract(tmpCouponAmount);//本次支付后剩余金额
							payAmount = tmpCouponAmount;//本次支付金额
						}else{
							fullPayFlag = true;
							payAmount = payBalance;//本次支付金额
							payBalance = new BigDecimal(0);//本次支付后剩余金额
						}
						//支付后消费金余额=支付前消费金余额-支付金额
						afterCouponAmount = couponAmount.subtract(payAmount);
						cqbPayAmount = cqbPayAmount.add(payAmount);
						cqbBalance = cqbBalance.subtract(payAmount);//传奇宝余额=支付前余额-本次支付金额
						useCqbPay(userBillDtos,payDtos,orderDto, userId, payAmount, fullPayFlag, cqbAmount,afterCouponAmount,multiPayDto,CommonConst.USER_ACCOUNT_TYPE_MONETARY,CommonConst.PAY_TYPE_CONSUM);
						cqbAmount = cqbAmount.subtract(payAmount);
					}
					logger.info("支付后消费金余额："+afterCouponAmount);
				}

				if (payBalance.compareTo(new BigDecimal(0)) > 0) {//还需要继续支付
					BigDecimal tmpRewardAmount = NumberUtil.formatVal(rewardAmount+"", 2);//只取2位小数进行计算
					if(tmpRewardAmount.compareTo(new BigDecimal(0)) > 0){//奖励金额大于0
						smsPayflag = "success";
						if (payBalance.compareTo(tmpRewardAmount) > 0) {
							payBalance = payBalance.subtract(tmpRewardAmount);//本次支付后剩余金额
							payAmount = tmpRewardAmount;//本次支付金额
						}else{
							fullPayFlag = true;
							payAmount = payBalance;//本次支付金额
							payBalance = new BigDecimal(0);//本次支付后剩余金额
						}
						//支付后消费金余额=支付前消费金余额-支付金额
						afterRewardAmount = rewardAmount.subtract(payAmount);
						cqbPayAmount = cqbPayAmount.add(payAmount);
						cqbBalance = cqbBalance.subtract(payAmount);//传奇宝余额=支付前余额-本次支付金额
						useCqbPay(userBillDtos,payDtos,orderDto, userId, payAmount, fullPayFlag, cqbAmount,afterRewardAmount,multiPayDto,CommonConst.USER_ACCOUNT_TYPE_REWARD,CommonConst.PAY_TYPE_REWARD);
						cqbAmount = cqbAmount.subtract(payAmount);
					}
				}
				logger.info("支付后奖励余额："+afterRewardAmount);
				//变更账户信息
				userAccountDao.updateUserAccountByKey(account.getAccountId(), cqbAmount.doubleValue(), afterRewardAmount.doubleValue(), afterCouponAmount.doubleValue(), null, null,afterVoucherAmount.doubleValue());
				
				preResult.put("payDtos", payDtos);
				preResult.put("userBillDtos", userBillDtos);
				PlatformBillDto platformBillDto = buildPlatformBill(userId, Double.parseDouble(cqbPayAmount+""), orderDto, "消费支付",1, multiPayDto.getData().getMobile(),CommonConst.PLT_BILL_MNY_SOURCE_CQB);
				preResult.put("platformBillDto", platformBillDto);
			}
			logger.info("支付后传奇宝余额："+cqbBalance);
			//整个短信支付总支付金额
			smsPayAmount = smsPayAmount.add(cqbPayAmount);
		}
		preResult.put("payBalance", payBalance);
		preResult.put("cqbPayAmount", cqbPayAmount);
		preResult.put("smsPayAmount", smsPayAmount);
		preResult.put("smsPayflag", smsPayflag);
		preResult.put("cqbBalance", cqbBalance);
		logger.info("传奇宝支付后："+preResult);
	}
	
	/**
	 * 红包支付
	 * @param multiPayDto 组合对象
	 * @param orderDto 订单对象
	 * @param userId 支付人ID
	 * @param wantAmount 需要支付金额
	 * @param isSingle 单次支付还是组合支付 ，单次 -- true， 组合 -- false
	 * @return
	 * @return Map<String,Object> [返回类型说明]
	 * @author  shengzhipeng
	 * @date  2016年3月16日
	 */
	private Map<String,Object> redPacketPayFromSmsPay(MultiPayDto multiPayDto,OrderDto orderDto,Long userId,BigDecimal wantAmount, boolean isSingle) throws Exception{
	    
	    //红包剩余金额
        BigDecimal redPacketPrice = new BigDecimal(0);
        //本次支付金额
        BigDecimal payAmount = new BigDecimal(0);
        //红包支付过后剩余支付金额
        BigDecimal payBalance = wantAmount;
        if(wantAmount.compareTo(new BigDecimal(0)) != 0) {
          //支付金额累加
            Long shopId= orderDto.getShopId();
            List<RedPacket> redPackets = packetDao.getRedPacketBy(orderDto.getShopId(), userId, RedPacketStatusEnum.USEABLE.getValue());
            if (CollectionUtils.isNotEmpty(redPackets)) {
                //可用红包金额
                Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
                if (isSingle) {
                    wantAmount = NumberUtil.countRecentNum(wantAmount, new BigDecimal(redPacketAmount));
                }
                if(redPacketAmount > 0) {
                    
                    if (redPacketAmount < wantAmount.doubleValue()) {
                        //直接扣减
                        for (RedPacket redPacket : redPackets) {
                            Long redPacketId = redPacket.getRedPacketId();
                            Double amount = redPacket.getAmount(); 
                            useRedPacket(orderDto, redPacket, redPacket.getAmount());
                            PayDto payDto = buildPayDto(userId, amount, multiPayDto, CommonConst.PAY_TYPE_RED_PACKET, orderDto.getOrderId());
                            payDto.setPayId(redPacketId);
                            if(payDto.getPayAmount()>0){
                            	payDao.addOrderPay(payDto);
                            }
                        }
                        payAmount = new BigDecimal(redPacketAmount.toString());  
                    } else {
                        //可用红包金额大于需要支付的金额
                        Double residualAmount = wantAmount.doubleValue();
                        for (RedPacket redPacket : redPackets) {
                            Double amount = redPacket.getAmount(); // 单张红包余额
                            Long redPacketId = redPacket.getRedPacketId();
                            if (amount >= residualAmount) {
                                //一张够用
                                logger.info("一张红包足够支付，红包ID:" + redPacketId);
                                useRedPacket(orderDto, redPacket, residualAmount);
                                PayDto payDto = buildPayDto(userId, residualAmount, multiPayDto, CommonConst.PAY_TYPE_RED_PACKET, orderDto.getOrderId());
                                payDto.setPayId(redPacketId);
                                if(payDto.getPayAmount()>0){
                               	 payDao.addOrderPay(payDto);
                               }
                                break;
                            } else {
                                //一张不够用
                                logger.info("一张红包不够支付，需要多次支付，每次支付红包ID:" + redPacketId);
                                PayDto payDto = buildPayDto(userId, amount, multiPayDto, CommonConst.PAY_TYPE_RED_PACKET, orderDto.getOrderId());
                                useRedPacket(orderDto, redPacket, amount);
                                payDto.setPayId(redPacketId);
                                if(payDto.getPayAmount()>0){
                                	 payDao.addOrderPay(payDto);
                                }
                                residualAmount = NumberUtil.sub(residualAmount, amount);
                            }
                        }
                        payAmount = wantAmount;
                        redPacketPrice = new BigDecimal(redPacketAmount.toString()).subtract(payAmount);
                    }
                    payBalance = wantAmount.subtract(payAmount);
                }
            }
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("payBalance", payBalance);
        result.put("payAmount", payAmount);
        result.put("redPacketPrice", redPacketPrice);
        logger.info("红包支付后："+result);
        return result;
	}
	
	   /**
     * 1、红包使用后需要更新红包状态和金额
     * 2、保存用户使用红包账单
     * 3、保存平台收到红包账单
     * @param order 订单对象
     * @param redPacket 红包
     * @param amount 使用金额
     * @author  shengzhipeng
     * @date  2016年3月15日
     */
    private void useRedPacket(OrderDto order, RedPacket redPacket, Double amount) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Long redPacketId = redPacket.getRedPacketId();
        param.put("redPacketId", redPacket.getRedPacketId());
        if (NumberUtil.sub(redPacket.getAmount(), amount) > 0) {
        	param.put("status", RedPacketStatusEnum.USEABLE.getValue());
        }else {
        	 param.put("status", RedPacketStatusEnum.USED.getValue());
        }
        param.put("payAmount", -amount);
        packetDao.updateRedPacketFlag(param);
        UserBillDto userBillDto = BillUtil.buildUserBillForRedPacket(order, redPacket, amount, true);
        userBillDao.insertUserBill(userBillDto);
        PlatformBillDto billDto = BillUtil.buildPlatformBill(order, amount, CommonConst.PLT_BILL_MNY_SOURCE_HB, CommonConst.PLATFORM_BILL_TYPE_R_RED_PACKET, "红包支付", true);
        billDto.setBillDesc("用户使用红包支付");
        billDto.setRedPacketId(redPacketId);
        platformBillDao.insertPlatformBill(billDto);
    }
	
	/**
	 * 消费卡支付
	 * @param multiPayDto
	 * @param orderDto
	 * @param userId
	 * @param wantAmount 希望支付的金额
	 * @param isSingle 是否单次支付，true--单次， false--组合
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> cashCouponPayFromSmsPay(MultiPayDto multiPayDto,OrderDto orderDto,Long userId,BigDecimal wantAmount, boolean isSingle) throws Exception{
		//用户持有的消费卡
		Map cashCouponMap = userCashCouponDao.queryUserCashCouponsByUserId(userId);
		//消费卡剩余金额
		BigDecimal uccPrice = new BigDecimal(0);
		//本次支付金额
		BigDecimal payAmount = new BigDecimal(0);
		//消费卡支付过后剩余支付金额
		BigDecimal payBalance = wantAmount;
		//支付金额累加
		BigDecimal smsPayAmount = new BigDecimal(0);
//		String smsPayflag = "fail";
		boolean fullPayFlag = false;
		if(payBalance.compareTo(new BigDecimal(0)) > 0) {
		  //校验消费卡
	        if(validUserCashCoupon(cashCouponMap)){
	            uccPrice = new BigDecimal(cashCouponMap.get("uccPrice")+"");
	            //代金券余额
	            BigDecimal couponBalance = CommonValidUtil.isEmpty(cashCouponMap.get("uccPrice"))?new BigDecimal(0):new BigDecimal(cashCouponMap.get("uccPrice")+"");
	            logger.info("=====优先使用消费卡支付，消费卡余额："+couponBalance);
	            BigDecimal tmpCouponBalance = NumberUtil.formatVal(couponBalance+"",2);
	            if(tmpCouponBalance.compareTo(new BigDecimal(0)) > 0){

	                if(isSingle) {
	                    wantAmount = NumberUtil.countRecentNum(wantAmount, tmpCouponBalance);
	                }
//	                smsPayflag = "success";
	                if (wantAmount.compareTo(tmpCouponBalance)>0) {
	                    payBalance = wantAmount.subtract(tmpCouponBalance);
	                    //只取2位小数进行计算
	                    payAmount = tmpCouponBalance;
	                }else{
	                    fullPayFlag = true;
	                    payAmount = wantAmount;
	                    payBalance = new BigDecimal(0);
	                }
	                //使用消费卡支付
	                useCouponPay(orderDto,userId,payAmount,fullPayFlag,cashCouponMap,multiPayDto);
	                //使用后余额=使用前余额-支付金额
	                uccPrice = uccPrice.subtract(payAmount);
	                smsPayAmount = smsPayAmount.add(payAmount);
	            }
	        }
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("payBalance", payBalance);
		result.put("payAmount", payAmount);
		result.put("smsPayAmount", smsPayAmount);
		result.put("uccPrice", uccPrice);
//		result.put("smsPayflag", smsPayflag);
		logger.info("消费卡支付后："+result);
		return result;
	}
	
	
	/**
	 * 短信支付-使用传奇宝支付
	 * @param userBillDtos 账单记录
	 * @param payDtos 支付记录
	 * @param orderDto 订单
	 * @param userId 用户编号
	 * @param payAmount 支付金额
	 * @param fullPayFlag 足额支付标识
	 * @param amount 支付前传奇宝账户余额
	 * @param accountAmount 账户余额（消费金/奖励）
	 * @param multiPayDto 
	 * @param payType 支付类型 
	 * @throws Exception
	 */
	private void useCqbPay(List<UserBillDto> userBillDtos,List<PayDto> payDtos,OrderDto orderDto,Long userId,BigDecimal payAmount,boolean fullPayFlag,BigDecimal amount,BigDecimal accountAmount,MultiPayDto multiPayDto,Integer accountType,Integer payType)throws Exception{
		boolean flag =false;
		String orderId = orderDto.getOrderId();
//		if(fullPayFlag){
//			logger.info("=====足额支付");
//			//足额支付：需要改变订单状态，订单支付状态，触发分账，记录订单日志
//			Integer beforeOrderStatus = orderDto.getOrderStatus();
//			OrderDto oDto = new OrderDto();
//			oDto.setOrderId(orderId);
//			oDto.setLastUpdateTime(new Date());
//			oDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//			orderDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//			logger.info("支付前的订单状态："+beforeOrderStatus);
//			if(beforeOrderStatus == CommonConst.ORDER_STS_YKD || beforeOrderStatus==CommonConst.ORDER_STS_PSZ){
//				orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//				orderDto.setLastUpdateTime(new Date());
//				oDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//				oDto.setLastUpdateTime(new Date());
//				collectDao.updateShopResourceStatus(orderDto.getOrderId(),CommonConst.RESOURCE_STATUS_NOT_IN_USE);
//				flag = true;
//			}
//			orderDao.updateOrder(oDto);
//			//记录订单日志
//			saveOrderLog(multiPayDto.getData().getCashierId(),orderDto,"传奇宝支付"+" 收银员操作");
//		}
		//记录用户账单
		UserBillDto userBillDto = buildUserBill(orderDto.getOrderTitle(),orderDto.getOrderStatus(), payAmount.doubleValue(), orderId, userId,amount.doubleValue(),accountAmount.doubleValue(), 0, "消费",-1,accountType,1);
		userBillDtos.add(userBillDto);
		//记录支付记录（消费金+奖励支付，只记录一条支付记录，传奇宝消费）
		PayDto payDto = buildPayDto(userId, payAmount.doubleValue(), multiPayDto, payType, orderId);
		payDtos.add(payDto);
//		if(flag){
//			//触发分账
//			OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId,0);
//		}
	}

    private PayDto buildPayDto(Long userId, Double payAmount, MultiPayDto multiPayDto, Integer payType,
            String orderId) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=format.format(new Date());
		PayDto payDto=new PayDto();
		payDto.setOrderId(orderId);
		payDto.setPayType(payType);// 传奇宝支付
		payDto.setPayAmount(payAmount);//支付金额
		payDto.setOrderPayType(0);
		payDto.setUserId(userId);
		payDto.setOrderPayTime(nowTime);
		payDto.setLastUpdateTime(nowTime);
		payDto.setPayeeType(0);
		payDto.setShopId(multiPayDto.getShopId());
		payDto.setPayIndex(multiPayDto.getPayIndex());
		payDto.setUserPayTime(multiPayDto.getPayClientTime());
		payDto.setChangeMoney(multiPayDto.getOddChange());//找零金额
		payDto.setRealMoney(multiPayDto.getRealCharges());//实收金额
		if (multiPayDto.getClientSystem() != null) {
			payDto.setClientSystem(multiPayDto.getClientSystem());
		}
        return payDto;
    }
	
	/**
	 * 短信支付-使用消费金支付
	 * @param orderDto 订单
	 * @param userId 用户编号
	 * @param payAmount 支付金额
	 * @param fullPayFlag 足额支付标识
	 * @param userCashCoupon 消费卡
	 * @param multiPayDto 
	 * @throws Exception
	 */
	private void useCouponPay(OrderDto orderDto,Long userId,BigDecimal payAmount,boolean fullPayFlag,Map userCashCoupon,MultiPayDto multiPayDto)throws Exception{
		boolean flag =false;
		String orderId = orderDto.getOrderId();
		logger.info("消费卡支付金额为："+payAmount);
//		if (fullPayFlag) {
//			// TODO 需要生成各种记录
//			//支付前的订单状态
//			Integer beforeOrderStatus = orderDto.getOrderStatus();
//			logger.info("支付前的订单状态："+beforeOrderStatus);
//			//修改订单状态及支付状态
//			OrderDto oDto = new OrderDto();
//			oDto.setOrderId(orderId);
//			oDto.setLastUpdateTime(new Date());
//			Integer afterOrderStatus = beforeOrderStatus;
//			if (beforeOrderStatus != null && CommonConst.UP_ODR_STS_YJZ.contains(beforeOrderStatus)) {
//				afterOrderStatus = CommonConst.ORDER_STS_YJZ;
//				//支付完全，需要进行分账
//				flag = true;
//			}
//			oDto.setOrderStatus(afterOrderStatus);
//			oDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
//			orderDao.updateOrder(oDto);
//			orderDto.setOrderStatus(afterOrderStatus);
//			orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
//			//生成订单操作日志
//			saveOrderLog(multiPayDto.getData().getCashierId(),orderDto, "消费卡支付"+" 收银员操作");
//		}
		//TODO //生成各种记录
		//生成订单支付记录
		String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
		PayDto payDto = new PayDto();
		payDto.setOrderId(orderId);
		payDto.setLastUpdateTime(nowTime);
		payDto.setOrderPayTime(nowTime);
		payDto.setPayeeType(0);
		Long uccId = (Long) userCashCoupon.get("ucc_id");
		payDto.setPayId(uccId);
		payDto.setPayType(CommonConst.PAY_TYPE_CASH_COUPON);// 代金券支付
		payDto.setOrderPayType(0);
		payDto.setPayAmount(payAmount.doubleValue());
		payDto.setShopId(multiPayDto.getShopId());
		payDto.setPayIndex(multiPayDto.getPayIndex());
		payDto.setUserPayTime(multiPayDto.getPayClientTime());
		payDto.setChangeMoney(multiPayDto.getOddChange());//找零金额
		payDto.setRealMoney(multiPayDto.getRealCharges());//实收金额
		payDto.setClientSystem(multiPayDto.getClientSystem());
		payDto.setUserId(userId);
		if(payAmount.doubleValue()>0){
			payDao.addOrderPay(payDto);
		}

		//生成代金券支付账单记录
		UserXBillDto userXBill = new UserXBillDto();
		userXBill.setUserId(userId);
		userXBill.setUccId(uccId);
		userXBill.setOrderPayType(payDto.getOrderPayType());
		userXBill.setOrderId(payDto.getOrderId());
		userXBill.setMoney(payAmount.doubleValue()*-1);
		userXBill.setCreateTime(new Date());
		userXBill.setBillType(CommonConst.USER_CASHCOUPON_USE);
		userXBill.setBillTitle("消费卡消费");
		userXBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());//账单状态为已完成
		userXBill.setBillDesc("订单支付");
		userXBill.setAccountAmount(CommonValidUtil.isEmpty(userCashCoupon.get("uccPrice"))?0d:Double.parseDouble(userCashCoupon.get("uccPrice")+""));
		useXBillDao.insertUserXBillDao(userXBill);
		
		PlatformBillDto platformBillDto = buildPlatformBill(userId, payAmount.doubleValue(), orderDto, "消费支付", 1, multiPayDto.getData().getMobile(), CommonConst.PLT_BILL_MNY_SOURCE_XFK);
		platformBillDao.insertPlatformBill(platformBillDto);
		
		//修改代金券使用金额
		//支付前代金券已使用金额
		Double usedPrice = CommonValidUtil.isEmpty(userCashCoupon.get("used_price"))?0d:(Double.parseDouble(userCashCoupon.get("used_price")+""));
		logger.info("支付前，消费券使用金额："+usedPrice.doubleValue());
		logger.info("支付完成后，更新消费券使用金额："+(usedPrice.doubleValue()+payAmount.doubleValue()));
		//支付后代金券使用金额=支付前使用金额+当前支付使用金额
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("usedPrice", (usedPrice+payAmount.doubleValue()));
		param.put("payId", payDto.getPayId());
		userCashCouponDao.updateUserCashCoupon(param);
		
//		if (flag) {
//			// TODO 分账
//			//满额支付才进行结算
//			OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId,0);
//		}
	}
	
	/**
	 * 校验消费卡是否可用
	 * @param ucc
	 * @return
	 * @throws Exception
	 */
	private boolean validUserCashCoupon(Map ucc)throws Exception{
		if(null == ucc || ucc.size() <=0){
			logger.info("=======不存在消费卡"+ucc);
			return false;
		}
		boolean re = true;
		Long uccId = (Long) ucc.get("ucc_id");
		//代金券使用商铺，为null表示使用所有
		Long shopId = (Long) ucc.get("shop_id");
		// TODO 本期可不实现 后期需要判断该代金券适用商铺
		//持有代金券有效最后时间
		String endTime = CommonValidUtil.isEmpty(ucc.get("end_time"))?null:(ucc.get("end_time")+"");
		//发行代金券有效最后时间，如果endTime=null，则取stopTime进行校验
		String stopTime = CommonValidUtil.isEmpty(ucc.get("stop_time"))?null:(ucc.get("stop_time")+"");
		String uccUserStopDateTime = null;
		if(StringUtils.isBlank(endTime)){
			if(!StringUtils.isBlank(stopTime)){
				uccUserStopDateTime = stopTime;
			}
		}else{
			uccUserStopDateTime = endTime;
		}
		if(!DateUtils.validDateTime(uccUserStopDateTime)){
			logger.info("编号["+uccId+"]"+"该代金券已经过期");
			re = false;
		}
		//代金券发行面额
		BigDecimal price = CommonValidUtil.isEmpty(ucc.get("price"))?null:(new BigDecimal(ucc.get("price")+""));
 		logger.info("=============>>代金券面额price="+price);
		//该代金券已经使用面额，剩余使用金额=代金券发行面额-已经使用面额
		BigDecimal usedPrice = CommonValidUtil.isEmpty(ucc.get("used_price"))?null:(new BigDecimal(ucc.get("used_price")+""));
 		logger.info("=============>>代金券已使用金额usedPrice="+usedPrice);
		//代金券实际可用金额
		BigDecimal uccPrice = new BigDecimal(0.0);
		if(null != price && price.doubleValue() > 0){
			if(null != usedPrice){
				uccPrice = price.subtract(usedPrice);
				if(uccPrice.doubleValue() <= 0){
					logger.info("编号["+uccId+"]"+"该代金券可用金额为0");
	 				re = false;
				}
			}else{
				uccPrice = price;
			}
 			//将代金券实际可用金额存入对象中
			logger.info("=============>>代金券剩余金额uccPrice="+uccPrice);
 			ucc.put("uccPrice", uccPrice);
		}else{
			re = false;
		}
		return re;
	}
	
	/**
	 * 现金组合支付(POS/CAHS)
	 * @param orderDto
	 * @param multiPayDto
	 * @param payType
	 * @throws Exception 
	 */
	private Map<String,Object> cashMultiPay(OrderDto orderDto,MultiPayDto multiPayDto,int payType) throws Exception{
		String orderId = orderDto.getOrderId();
		//计算订单实际需要支付金额
		Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
		// 获取订单实际已经支付的金额(单订单支付)
		BigDecimal beforePayAmount =  amountMap.get("beforePayAmount");
		//本次希望支付的金额
		BigDecimal wantAmount = amountMap.get("wantAmount");
		//订单剩余支付金额
		BigDecimal surplusAmount = amountMap.get("surplusAmount");
		logger.info("====订单本次希望支付金额："+wantAmount);
		if(wantAmount.compareTo(surplusAmount) > 0 && CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
			//wantAmount = surplusAmount;
			throw new ValidateException(CodeConst.CODE_72405, "超额支付");
		}
		logger.info("====订单本次实际需要支付金额："+wantAmount);
		
		//判断是否支付完成
//		int orderStatus=orderDto.getOrderStatus();
//		boolean flag = false;
		UserDto user = userDao.getUserByMobile(multiPayDto.getData().getMobile());
		if(null == user){
		    user = userDao.getUserById(orderDto.getUserId()); 
		}
		//本次支付实际支付金额
//		if(wantAmount.compareTo(surplusAmount) == 0){
//			logger.info("=====足额支付");
//			//足额支付：需要改变订单状态，订单支付状态，触发分账，记录订单日志
//			OrderDto oDto = new OrderDto();
//			oDto.setOrderId(orderId);
//			oDto.setLastUpdateTime(new Date());
//			oDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//			orderDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//			if(orderStatus == CommonConst.ORDER_STS_YKD || orderStatus==CommonConst.ORDER_STS_PSZ){
//				orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//				oDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//				collectDao.updateShopResourceStatus(orderDto.getOrderId(),CommonConst.RESOURCE_STATUS_NOT_IN_USE);
//				flag = true;
//			}
//			orderDao.updateOrder(oDto);
//			//记录订单日志
//			String remark = (payType == CommonConst.PAY_TYPE_POS?"POS支付":"现金支付"+" 收银员操作");
//			saveOrderLog(multiPayDto.getData().getCashierId(),orderDto,remark);
//		}
		//记录支付信息
		Double payAmount = Double.parseDouble(wantAmount+"");
		if (payAmount.doubleValue() > 0 
				|| (payAmount.doubleValue() < 0  && CommonConst.REVERSE_SETTLE_FLAG == orderDto.getSettleFlag().intValue())) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime=format.format(new Date());
			PayDto payDto=new PayDto();
			payDto.setOrderId(orderId);
			payDto.setPayType(payType);//现金支付
			payDto.setPayAmount(payAmount);//支付金额
			payDto.setOrderPayType(0);
			payDto.setUserId(user.getUserId());
			payDto.setOrderPayTime(nowTime);
			payDto.setLastUpdateTime(nowTime);
			payDto.setPayeeType(CommonConst.PAYEE_TYPE_SHOP);//现金支付属于商铺收款
			payDto.setShopId(multiPayDto.getShopId());
			payDto.setPayIndex(multiPayDto.getPayIndex());
			payDto.setUserPayTime(multiPayDto.getPayClientTime());
			payDto.setChangeMoney(multiPayDto.getOddChange());//找零金额
			payDto.setRealMoney(multiPayDto.getRealCharges());//实收金额
			payDto.setClientSystem(multiPayDto.getClientSystem());
			payDao.addOrderPay(payDto);
		}

//		if(flag){
//			//触发分账
//			OrderGoodsSettleUtil.detailOrderGoodsSettle(orderDto.getOrderId(), 0);
//		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("payMoney", wantAmount);
//		result.put("orderPaidMoney", beforePayAmount.add(wantAmount));
//		result.put("orderPayStatus", orderDto.getPayStatus());
//		result.put("orderStatus", orderDto.getOrderStatus());
		return result;
	}
	/**
	 * 获取订单实际支付金额
	 * @param orderId
	 * @param payMoney
	 * @return
	 * @throws Exception
	 */
	private Map<String,BigDecimal> getWantAmount(String orderId,Double payMoney,OrderDto orderDto) throws Exception{
		// 获取订单实际需要支付的金额(settlePrice)
		BigDecimal needPayAmount = packetDao.queryOrderAmount(orderId);
		logger.info("====订单实际需要支付金额："+needPayAmount);
		// 获取订单实际已经支付的金额(单订单支付)
		BigDecimal beforePayAmount =  packetDao.queryOrderPayAmount(orderId,0);
		if(beforePayAmount == null){
			beforePayAmount = new BigDecimal(0);
		}
		logger.info("====订单已经支付金额："+beforePayAmount);
		if(needPayAmount.compareTo(beforePayAmount)<0 
				&& CommonConst.REVERSE_SETTLE_FLAG != orderDto.getSettleFlag().intValue()){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "实际已支付金额超过订单金额");
		}
		//订单剩余支付金额：实际需要支付金额-已经支付金额
		BigDecimal surplusAmount = needPayAmount.subtract(beforePayAmount);
		logger.info("====订单剩余支付金额："+surplusAmount);
		//本次希望支付的金额
		BigDecimal wantAmount = new BigDecimal(payMoney+"");
		logger.info("====订单本次希望支付金额："+wantAmount);
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		resultMap.put("beforePayAmount", beforePayAmount);
		resultMap.put("wantAmount", wantAmount);
		resultMap.put("surplusAmount", surplusAmount);
		return resultMap;
	}
	/**
	 * 传奇宝组合支付，只使用传奇宝账号的钱
	 * @param orderDto
	 * @param multiPayDto
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> cqbMultiPay(OrderDto orderDto,MultiPayDto multiPayDto)throws Exception{
		String orderId = orderDto.getOrderId();
		//计算订单实际需要支付金额
		Map<String,BigDecimal> amountMap = getWantAmount(orderId, multiPayDto.getPayMoney(),orderDto);
		// 获取订单实际已经支付的金额(单订单支付)
//		BigDecimal beforePayAmount =  amountMap.get("beforePayAmount");
		//本次希望支付的金额
		BigDecimal wantAmount = amountMap.get("wantAmount");
		//订单剩余支付金额
		BigDecimal surplusAmount = amountMap.get("surplusAmount");

		//传奇宝支付金额
        BigDecimal onLinePayment = new BigDecimal(0);
        
        
		if(wantAmount.compareTo(surplusAmount) > 0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "支付金额超过剩余支付金额了");
		}
		logger.info("====订单本次实际需要支付金额："+wantAmount);
		
		UserDto user = userDao.getUserByMobile(multiPayDto.getData().getMobile());
		if(null == user){
            user = userDao.getUserById(orderDto.getUserId()); 
        }
		Long userId = user.getUserId();
		Map<String,Object> initResult = new HashMap<String, Object>();
		initResult.put("payBalance", wantAmount);
		initResult.put("smsPayAmount", new BigDecimal(0));
		initResult.put("uccPrice", new BigDecimal(0));
		initResult.put("smsPayflag", "fail");
		//调用传奇宝支付
		cqbMulitPay(initResult, userId, multiPayDto, orderDto);
		onLinePayment = (BigDecimal) initResult.get("cqbPayAmount");
//		BigDecimal payBalance = (BigDecimal) initResult.get("payBalance");
		BigDecimal cqbBalance = (BigDecimal) initResult.get("cqbBalance");
		BigDecimal smsPayAmount = (BigDecimal) initResult.get("smsPayAmount");
		String smsPayflag = (String) initResult.get("smsPayflag");
		List<PayDto> payDtos = (List<PayDto>) initResult.get("payDtos");
		List<UserBillDto> userBillDtos = (List<UserBillDto>) initResult.get("userBillDtos");
		PlatformBillDto platformBillDto = (PlatformBillDto) initResult.get("platformBillDto");
		
		if (smsPayflag != null && "success".equals(smsPayflag)) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime=format.format(new Date());
			TransactionDto transactionDto=new TransactionDto();
			transactionDto.setUserId(userId);
			transactionDto.setOrderId(orderId);
			transactionDto.setPayAmount(Double.parseDouble(smsPayAmount+""));//支付金额
			transactionDto.setTransactionTime(nowTime);
			transactionDto.setStatus(1);//支付成功
			transactionDto.setUserPayChannelId(new Long(1));
			transactionDto.setOrderPayType(0);
			transactionDto.setLastUpdateTime(nowTime);
			transactionDto.setTransactionType(0);
			//生成流水记录
			transactionDao.addTransaction(transactionDto);
			
			if (payDtos != null && payDtos.size() > 0) {
				for(PayDto payDto : payDtos){
					payDto.setPayId(transactionDto.getTransactionId());
					if(payDto.getPayAmount()>0){
						payDao.addOrderPay(payDto);
					}
					
				}
			}
			if (userBillDtos != null && userBillDtos.size() > 0) {
				for(UserBillDto userBillDto : userBillDtos){
					userBillDto.setTransactionId(transactionDto.getTransactionId());
					userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
					userBillDao.insertUserBill(userBillDto);
				}
			}
			if (null != platformBillDto) {
				platformBillDto.setTransactionId(transactionDto.getTransactionId());
				platformBillDao.insertPlatformBill(platformBillDto);
			}
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("payMoney", onLinePayment);
//		resultMap.put("orderPaidMoney", beforePayAmount.add((wantAmount.subtract(payBalance))));
//		resultMap.put("orderPayStatus", orderDto.getPayStatus());
//		resultMap.put("orderStatus", orderDto.getOrderStatus());
//		resultMap.put("cqbBalance", cqbBalance);
		resultMap.put("smsPayAmount", smsPayAmount);
        resultMap.put("onLinePayment", onLinePayment);
		return resultMap;
	}
	/**
	 * 
	 * @param orderTitle 订单title
	 * @param orderStatus 支付后订单状态
	 * @param payAmount 支付金额
	 * @param orderId 订单编号
	 * @param userId 用户编号
	 * @param amount 支付前传奇宝余额
	 * @param accountAfterAmount 支付后变动金额（消费金支付，则为消费金支付后金额；奖励支付，则为奖励支付后金额）
	 * @param orderPayType 订单支付类型：0-单个订单
	 * @param billType 账单类型:提现,充值,消费,推荐奖励,店铺收入,退款
	 * @param billDirection 账单类型:1（账户资金增加）,-1（账户资金减少）
	 * @param accountType 账户类型：1=平台奖励，2=冻结资金，3=消费金
	 * @param userBillType 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
	 * @return
	 * @throws Exception
	 */
	public UserBillDto buildUserBill(String orderTitle,Integer orderStatus,Double payAmount,String orderId,Long userId,
			Double amount,Double accountAfterAmount,Integer orderPayType,String billType,
			Integer billDirection,Integer accountType,Integer userBillType) throws Exception{
		int billStatus=orderStatus+20;
		UserBillDto userBillDto=new UserBillDto();
		userBillDto.setBillType(billType);
		userBillDto.setBillDirection(billDirection);
		userBillDto.setBillStatus(billStatus);//已预订
		//修改账单记录为负数
		userBillDto.setMoney(0-payAmount);
		//消费记录账户余额
		userBillDto.setAccountAmount(amount);
		userBillDto.setAccountAfterAmount(accountAfterAmount);
		userBillDto.setOrderId(orderId);
		userBillDto.setCreateTime(new Date());
		userBillDto.setBillDesc("消费");
		userBillDto.setUserId(userId);
		userBillDto.setConsumerUserId(userId);
		userBillDto.setOrderPayType(orderPayType);
		if(billStatus==ConsumeEnum.ORDERED.getValue() || billStatus==ConsumeEnum.HAVE_ORDER.getValue() || billStatus==ConsumeEnum.DISPATCH.getValue()
				|| billStatus==ConsumeEnum.CHARGEBACKING.getValue()){
			userBillDto.setBillStatusFlag(1);
		}else{
			userBillDto.setBillStatusFlag(0);
		}
		UserDto user= userDao.getUserById(userId);
		userBillDto.setConsumerMobile(user.getMobile());
		userBillDto.setAccountType(accountType);
		userBillDto.setUserBillType(userBillType);
		userBillDto.setBillTitle(orderTitle);
		return userBillDto;
	}
	
	/**
	 * 封装平台账单
	 * @param userId 用户编号
	 * @param payAmount 支付金额
	 * @param order 订单
	 * @param billType 账单类型
	 * @param billDirection 账单描述
	 * @param mobile 手机号码
	 * @param moneySource 资金来源
	 * @return
	 * @throws Exception
	 */
	private PlatformBillDto buildPlatformBill(Long userId,Double payAmount,OrderDto order,String billType,Integer billDirection,String mobile,Integer moneySource) throws Exception {
		PlatformBillDto platformBill = new PlatformBillDto();
		platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
		platformBill.setBillType(billType);
		platformBill.setBillDirection(billDirection);
		platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
		platformBill.setMoney(payAmount);
		platformBill.setOrderId(order.getOrderId());
		platformBill.setGoodsNumber(Double.valueOf(order.getGoodsNumber()));
		platformBill.setGoodsSettlePrice(order.getSettlePrice());
		platformBill.setCreateTime(new Date());
		platformBill.setSettleTime(new Date());
		platformBill.setBillDesc(billType);
		platformBill.setConsumerUserId(userId);
		platformBill.setConsumerMobile(mobile);
		platformBill.setMoneySource(moneySource);
		return platformBill;
	}
	
	
	/**
	 * 保存日志log
	 * @param order
	 * @param remark
	 * @throws Exception
	 */
	public void saveOrderLog(Long operateId,OrderDto order,String remark) throws Exception{
		OrderLogDto orderLogDto=new OrderLogDto();
		orderLogDto.setOrderId(order.getOrderId());
		orderLogDto.setPayStatus(order.getPayStatus());
		orderLogDto.setOrderStatus(order.getOrderStatus());
		orderLogDto.setLastUpdateTime(new Date());
		orderLogDto.setRemark(remark);
		orderLogDto.setUserId(operateId);
		orderLogDao.saveOrderLog(orderLogDto);
	}

	public List<Map<String, Object>> getPayRecordByOrderId(String orderId,
			int flag) throws Exception {
		if(flag == 0){
			return xorderPayDao.getPayLogByXorderId(orderId);
		}else{
			return payDao.getPayLogByOrderId(orderId);
		}
	}

	public int updateGoodsInfo(Long shopId, List<Long> goodsIds,
			Integer operateType) throws Exception {
		int re = goodsDao.updateGoodsInfoStatus(shopId,goodsIds,operateType);
		return re;
	}

	public PageModel getShopGoods(Map<String, Object> map) throws Exception {
		PageModel pageModel = new PageModel();
	    Integer count = collectDao.getShopGoodsCount(map);
	    if(count!=null&&count!=0){
		    List<Map> resultList = convertGoodsImg(collectDao.getShopGoods(map));
			if (CollectionUtils.isNotEmpty(resultList)) {
				//增加套餐内商品信息
				for (Map good : resultList) {
					Integer goodsType = Integer.valueOf(good.get("goodsType").toString());
					if (goodsType == 3000) {
						Long goodsId = Long.valueOf(good.get("dishId").toString());
						List<GoodsSetDto> goodsSetList = goodsSetDao.getGoodsIdListByGoodsSetId(goodsId);
						good.put("setGoodsList", goodsSetList);
					}
				}
			}
		    pageModel.setList(resultList);
		    pageModel.setTotalItem(count);
	    }
		return pageModel;
	}
	
	/**
	 * 获取非会员订单支付详情
	 * @Title: getXorderPayDetail 
	 * @param @param shopId
	 * @param @param orderId
	 * @param @param token
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<Map> getXorderPayDetail(Long shopId, String orderId,
			String token)throws Exception {
		queryShopAndTokenExists(shopId, token);
		List<Map>resultList=xorderPayDao.getXorderPayList(orderId);
		return resultList;
	}
	
	public BigDecimal queryOrderPayAmount(String orderId, int orderPayType)
			throws Exception {
		return packetDao.queryOrderPayAmount(orderId, orderPayType);
	}
	/**
	 * 获取会员订单状态
	 * @Title: getOrderStatus 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public int getOrderStatus(String orderId) throws Exception {
		OrderDto orderDto=orderDao.getOrderById(orderId);
		if(orderDto!=null){
			return orderDto.getOrderStatus();
		}
		return 0;
	}
	
	/**
	 * 获取非会员订单状态
	 * @Title: getXorderStatus 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public int getXorderStatus(String orderId) throws Exception {
		XorderDto xorderDto=xorderDao.getXOrderById(orderId);
		if(xorderDto!=null){
			return xorderDto.getOrderStatus();
		}
		return 0;
	}
	
	

	public BigDecimal queryXorderPayAmount(String xorderId) throws Exception {
		return xorderPayDao.getTotalPayAmountByXorderId(xorderId);
	}

	@Override
	public Map<String,String> queryEmployee(String orderId) {
		return collectDao.queryEmployee(orderId);
	}

	@Override
	public List<Map> queryGoodsByBarcode(Long shopId,String barcode,Integer goodsStatus,Integer searchFlag) throws Exception {
		List<Map> mapList =collectDao.queryGoodsByBarcode(shopId,barcode,goodsStatus,searchFlag);
		if(null != mapList && mapList.size() > 0){
			for (Map map : mapList) {
				if(map!=null && !CommonValidUtil.isEmpty("fileUrl")){
					//根据商品ID查询商品图片
					String img=map.get("fileUrl")+"";
					map.remove("fileUrl");
					map.put("goodsLogo1", FdfsUtil.getFileProxyPath(img));
				}
			}
		}
		
		return mapList;
	}
	
	@Override
	public void batchInsertCookingDetail(List<ShopCookingDetails> cookDetailList) throws Exception {
		collectDao.batchInsertCookingDetail(cookDetailList);
	}

	@Override
	public Long getGoodsUnitByUnitName(String unitName, Long shopId) {
		Map<String, Object> result = new HashMap<String, Object>();
    	result.put("unitName", unitName);
    	result.put("shopId", shopId);
		List<GoodsUnitDto> goodsList=goodsDao.getGoodsUnitByUnitNameAndShopId(result);
		int unitId=0;
		if(goodsList!=null && goodsList.size()>0){
			unitId = goodsList.get(0).getUnitId();
		}
		return (long) unitId;
	}

    @Override
    public List<Unit> queryUnitsForShop(Integer shopId)
    {   
        List<Unit> rs = new LinkedList<Unit>();
        Unit searchCondition = new Unit();
        //首先查询商铺配置
        searchCondition.setShopId(Long.parseLong(shopId + ""));
        List<Unit> shopUs = unitDao.queryUnit(searchCondition);
        if(null != shopUs){
            rs.addAll(shopUs);
        }
        //查询所有公用配置
        searchCondition.setShopId(null);
        searchCondition.setSourceType(0);
        List<Unit> commonUs = unitDao.queryUnit(searchCondition);
        if(null != shopUs){
            rs.addAll(commonUs);
        }
        return rs;
    }

	@Override
	public Map<String, Object> getShopIncomeStat(Map<String, Object> params) throws Exception{
		Map<String, Object> rs = new HashMap<String, Object>();
		logger.debug("查询指定时间内的收入统计" + params);
		Map<String, BigDecimal> tempRs = collectDao.getShopIncomeStat(params);
		if(null != tempRs && tempRs.size() > 0)
		{
			List<String> list = this.getMoney(tempRs);
			rs.put("timeZonesOrderMoney", list.get(0));
			rs.put("timeZonesChargeMoney", list.get(1));
			rs.put("timeZonesMemberCardPay", list.get(2));
		}else{
			rs.put("timeZonesOrderMoney", "0");
			rs.put("timeZonesChargeMoney", "0");
			rs.put("timeZonesMemberCardPay", "0");
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date today = dateFormat.parse(dateFormat.format(calendar.getTime()));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		Date yesterday = dateFormat.parse(dateFormat.format(calendar.getTime()));
		params.put("endDate", new Date());
		params.put("startDate", today);
		logger.debug("查询当天内的收入统计" + params);
		tempRs = collectDao.getShopIncomeStat(params);
		if(null != tempRs && tempRs.size() > 0)
		{
			List<String> list = this.getMoney(tempRs);
			rs.put("todayOrderMoney", list.get(0));
			rs.put("todayChargeMoney", list.get(1));
			rs.put("todayMemberCardPay", list.get(2));
		}else {
			rs.put("todayOrderMoney", "0");
			rs.put("todayChargeMoney", "0");
			rs.put("todayMemberCardPay", "0");
		}
		params.put("endDate", today);
		params.put("startDate", yesterday);
		logger.debug("查询昨天内的收入统计" + params);
		tempRs = collectDao.getShopIncomeStat(params);
		if(null != tempRs && tempRs.size() > 0)
		{
			List<String> list = this.getMoney(tempRs);
			rs.put("yesterdayOrderMoney", list.get(0));
			rs.put("yesterdayChargeMoney", list.get(1));
			rs.put("yesterdayMemberCardPay", list.get(2));
		}else{
			rs.put("yesterdayOrderMoney", "0");
			rs.put("yesterdayChargeMoney", "0");
			rs.put("yesterdayMemberCardPay", "0");
		}
		return rs;
	}

	private LinkedList<String>  getMoney(Map<String, BigDecimal> params){
		LinkedList<String> rs = new LinkedList<String>();
		BigDecimal settlePrice = params.get("settlePrice");
		settlePrice = settlePrice == null ? new BigDecimal(0) : settlePrice;
		BigDecimal chargePrice = params.get("chargePrice");
		chargePrice = chargePrice == null ? new BigDecimal(0) : chargePrice;
		BigDecimal memberCardPay = params.get("memberCardPay");
		memberCardPay = memberCardPay == null ? new BigDecimal(0) : memberCardPay;
		rs.add(this.convertBigDecimal(settlePrice,2));
		rs.add(this.convertBigDecimal(chargePrice,2));
		rs.add(this.convertBigDecimal(memberCardPay,2));
		return rs;
	}

	private String convertBigDecimal(BigDecimal de, int precision){
		String str = de.toString();
		int dotPosition = str.indexOf(".");
		if( str.length() - dotPosition - 1 > precision){
			str = str.substring( 0,dotPosition + precision + 1);
		}
		return  str;
	}

	@Override public Map<String, Object> getSalerPerformanceByOrder(Map<String, Object> searchParams)
	{
		Map<String, Object> rs = new HashMap<>();
		this.initEmployeeInfo((Long)searchParams.get("employeeId"), rs);
		int rowCounts = collectDao.countSalerPerformanceDtailByOrder(searchParams);
		rs.put("rCount", rowCounts);
		rs.put("pNo", searchParams.get("pNo"));
		if(rowCounts <= 0 || rowCounts < ((Integer)searchParams.get("pNo") - 1) * ((Integer)searchParams.get("pSize")))
		{
			rs.put("lst", null);
		}else{
			rs.put("lst", collectDao.getSalerPerformanceDtailByOrder(searchParams));
		}
		return rs;
	}

	@Override public Map<String, Object> getSalerPerformanceByOrderGoods(Map<String, Object> searchParams)
	{
		Map<String, Object> rs = new HashMap<>();
		this.initEmployeeInfo((Long)searchParams.get("employeeId"), rs);
		int rowCounts = collectDao.countSalerPerformanceDtailByOrderGoods(searchParams);
		rs.put("rCount", rowCounts);
		rs.put("pNo", searchParams.get("pNo"));
		if(rowCounts <= 0 || rowCounts < ((Integer)searchParams.get("pNo") - 1) * ((Integer)searchParams.get("pSize")))
		{
			rs.put("lst", null);
		}else{
			rs.put("lst", collectDao.getSalerPerformanceDtailByOrderGoods(searchParams));
		}
		return rs;
	}

	private void initEmployeeInfo(Long employeeId, Map<String, Object> params){
		Map<String, String> param = new HashMap<>();
		param.put("employeeId",employeeId + "");
		List<ShopEmployeeDto> employeeResult = shopDao.queryEmployeeListByMap(param);
		if(null == employeeResult || employeeResult.size() == 0)
		{
			throw new BusinessException("employeeId对应的雇员不存在");
		}
		ShopEmployeeDto shopEmployeeDto = employeeResult.get(0);
		params.put("employeeId", shopEmployeeDto.getEmployeeId());
		params.put("employeeName", shopEmployeeDto.getUserName());
	}

	/**
	 * 获取商品条数
	 * @throws Exception 
	 */
	public int getShopGoodsCount(long shopId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId",shopId);
		return collectDao.getShopSimpleCount(params);
	}

}