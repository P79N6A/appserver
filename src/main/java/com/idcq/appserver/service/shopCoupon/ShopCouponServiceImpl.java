package com.idcq.appserver.service.shopCoupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponAvailableGoodsDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponAvailableShopDao;
import com.idcq.appserver.dao.shopCoupon.IShopCouponDao;
import com.idcq.appserver.dao.shopCoupon.IUserShopCouponDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shopCoupon.GoodsCategoryInfo;
import com.idcq.appserver.dto.shopCoupon.RequstCouponDeductModel;
import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableGoodsDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponAvailableShopDto;
import com.idcq.appserver.dto.shopCoupon.ShopCouponDto;
import com.idcq.appserver.dto.shopCoupon.UserShopCouponDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.shopCoupon.enums.CouponStausEnum;
import com.idcq.appserver.service.shopCoupon.enums.IsDeleteEnum;
import com.idcq.appserver.service.shopCoupon.enums.IsUsedTogetherEnum;
import com.idcq.appserver.service.shopCoupon.enums.UserShopCouponStatusEnum;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

@Service
public class ShopCouponServiceImpl implements IShopCouponService {

	private final static Logger logger = LoggerFactory.getLogger(ShopCouponServiceImpl.class);
	@Autowired
	private IShopCouponDao shopCouponDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IShopCouponAvailableGoodsDao shopCouponAvailableGoodsDao;
	@Autowired
	private IUserShopCouponDao 	userShopCouponDao;
	@Autowired
	private IShopMemberDao 	shopMemberDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IShopCouponAvailableShopDao shopCouponAvailableShopDao;

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shopCoupon.IShopCouponService#sendCoupon(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void sendCoupon(Long shopId, Long shopCouponId, String memberIds)
			throws Exception {
		/**
		 * 1、判断优惠券是否存在
		 * 2、关联优惠券和用户关系
		 * 3、更新优惠券数量
		 */
		ShopCouponDto shopCouponDB  = shopCouponDao.getShopCouponById(shopCouponId);
        String[] memberIdArray = memberIds.split(",");
        shopCouponDB.setReceiveNum(memberIdArray.length);
        validShopCoupon(shopCouponDB);
        
        //批量插入优惠券用户关系
        userShopCouponDao.beachInsertUserShopCoupon(buildUserShopCouponList(memberIdArray, shopCouponDB));
        
        //更新优惠券发送数量
        ShopCouponDto shopCouponDto = new ShopCouponDto();
        shopCouponDto.setShopCouponId(shopCouponId.intValue());
        shopCouponDto.setGetTotalNum(shopCouponDB.getGetTotalNum()+memberIdArray.length);
        shopCouponDao.updateShopCoupon(shopCouponDto);
	}
    private void validShopCoupon(ShopCouponDto shopCouponDB) throws Exception{
    	
        CommonValidUtil.validObjectNull(shopCouponDB, CodeConst.CODE_PARAMETER_NOT_EXIST, "优惠券不存在");
        
        if(CouponStausEnum.DISABLE.getValue()==shopCouponDB.getCouponStatus()){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "优惠券已停用");
        }
        if(IsDeleteEnum.IS_DELETE.getValue()==shopCouponDB.getIsDelete()){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "优惠券已删除");
        }
        //本次发送人数
        Integer receiveNum = shopCouponDB.getReceiveNum()==null ? 1 : shopCouponDB.getReceiveNum();
        if(shopCouponDB.getGetTotalNum().intValue()+receiveNum.intValue()>shopCouponDB.getTotalNum().intValue()){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "优惠券领取数量不能大于发行数量");
        }
        
        Date nowDate = new Date();
        Date endDate = shopCouponDB.getEndDate();
        endDate = DateUtils.parse(DateUtils.getDateAfterDay(DateUtils.format(endDate, DateUtils.DATETIME_FORMAT), 1));
        if(DateUtils.compareDate(nowDate, endDate)>0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "优惠券已过期");
        }
        
    }	
	private void sendSmsByUser(ShopCouponDto coupon,UserShopCouponDto userShopCoupon){

	    try {
	    	logger.info("开始发送短信给用户-start");
			SmsReplaceContent src = new SmsReplaceContent();
			 // 短信通知:	//【shopName】尊敬的客户，couponName已经放入您的账户，有效期beginDate至endDate，到店消费couponUsedCondition即可使用。
			Long shopId = coupon.getShopId();
			ShopDto shop = shopDao.getShopById(shopId);
			
	        src.setShopName(shop.getShopName());
	        src.setCouponName(coupon.getCouponName());
	        src.setBeginDate(DateUtils.format(coupon.getBeginDate(), DateUtils.DATE_FORMAT));
	        src.setEndDate(DateUtils.format(coupon.getEndDate(), DateUtils.DATE_FORMAT));
	        src.setMobile(userShopCoupon.getMobile());
	        src.setCouponUsedCondition(coupon.getCouponUsedCondition());
            src.setUsage(CommonConst.SEND_SHOP_COUPON_CONTENT);
            src.setCouponAmount(coupon.getCouponAmount());

			//发送短信
            boolean  succesFlag = sendSmsService.sendSmsMobileCode(src);
	    	logger.info("发送短信结果："+succesFlag);
			
	    	logger.info("发送短信给用户");
		} catch (Exception e) {
			logger.error("发送短信失败"+e.toString());;
		}
	}
	
    private List<UserShopCouponDto> buildUserShopCouponList(String[] memberIdArray,ShopCouponDto shopCouponDB ) throws Exception{
    	
    	List<UserShopCouponDto> userShopCouponDtos = new ArrayList<UserShopCouponDto>();
    	
		for (String memberIdStr : memberIdArray) {
			Integer memberId = NumberUtil.strToInteger(memberIdStr, "memberIds");
			ShopMemberDto shopMember = shopMemberDao.getShopMemberById(memberId.longValue());
	        CommonValidUtil.validObjectNull(shopMember, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST);

			UserShopCouponDto userShopCouponDto = new UserShopCouponDto();
			userShopCouponDto.setBeginDate(shopCouponDB.getBeginDate());
			userShopCouponDto.setCouponStatus(UserShopCouponStatusEnum.NOT_IS_USE.getValue());
			userShopCouponDto.setEndDate(shopCouponDB.getEndDate());
			userShopCouponDto.setGetCouponTime(new Date());
			userShopCouponDto.setShopCouponId(shopCouponDB.getShopCouponId());
			userShopCouponDto.setShopId(shopCouponDB.getShopId());
			userShopCouponDto.setShopMemeberId(memberId);
			userShopCouponDto.setUserId(shopMember.getUserId());
			userShopCouponDto.setMobile(shopMember.getMobile()==null ? "" : shopMember.getMobile().toString());
			Integer index = getShopCouponIndexAndValidmaxNum(memberId, shopCouponDB.getShopCouponId(),null,
					shopCouponDB.getMaxNumPerPerson());
			userShopCouponDto.setShopCouponIndex(index);
			userShopCouponDtos.add(userShopCouponDto);
			
			//单独发送短信用户
			sendSmsByUser(shopCouponDB, userShopCouponDto);
		}
		
		return userShopCouponDtos;
    }
    
    public Integer getShopCouponIndexAndValidmaxNum(Integer shopMemeberId,Integer shopCouponId,Long userId,Integer maxNumPerPerson) throws Exception{
    	//领取多张优惠券,shopCouponIndex加1
    	UserShopCouponDto parms = new UserShopCouponDto();
    	parms.setShopMemeberId(shopMemeberId);
    	parms.setShopCouponId(shopCouponId);
    	parms.setUserId(userId);
    	
    	Integer count = userShopCouponDao.getUserShopCouponCount(parms);
    	
    	if(count>=maxNumPerPerson){
			throw new ValidateException( CodeConst.CODE_PARAMETER_NOT_EXIST, "领取券超过上限");

    	}
    	
		return count==null ? 1 : count+1;
    }
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shopCoupon.IShopCouponService#getCoupon(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void userGetCoupon(Long shopId, Long shopCouponId, String mobile)
			throws Exception {
		/**
		 * 1、验证优惠券、会员是否存在
		 * 2、更新用户优惠券关联表
		 * 3、减少优惠券数量
		 */
		ShopCouponDto shopCouponDB  = shopCouponDao.getShopCouponById(shopCouponId);
		
		validShopCoupon(shopCouponDB);
		
		ShopMemberDto shopMember = validUser(shopId, mobile);
		
    	UserShopCouponDto userShopCouponDto = buildUserShopCoupon(shopMember, shopCouponDB);
        userShopCouponDao.insertUserShopCoupon(userShopCouponDto);
        
        //更新优惠券发送数量
        ShopCouponDto shopCouponDto = new ShopCouponDto();
        shopCouponDto.setShopCouponId(shopCouponId.intValue());
        shopCouponDto.setGetTotalNum(shopCouponDB.getGetTotalNum()+1);
        shopCouponDao.updateShopCoupon(shopCouponDto);
		
	}
	private ShopMemberDto validUser(Long shopId,String mobile) throws Exception{
		ShopMemberDto result = new ShopMemberDto();
		
		ShopMemberDto shopMember = shopMemberDao.getShopMbByMobileAndShopId(mobile, shopId, CommonConst.MEMBER_STATUS_DELETE);
		UserDto userDB = userDao.getUserByMobile(mobile);
		if(shopMember==null && userDB!=null){
			result.setMobile(Long.valueOf(mobile));
			result.setUserId(userDB.getUserId());
		}
		if(shopMember!=null){
			result = shopMember;
		}
		if(shopMember==null && userDB==null){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USER_NOT_EXIST); 
		}
		return result;
	}

	private UserShopCouponDto buildUserShopCoupon(ShopMemberDto shopMember,
			ShopCouponDto shopCouponDB) throws Exception {
		
		UserShopCouponDto userShopCouponDto = new UserShopCouponDto();
		userShopCouponDto.setBeginDate(shopCouponDB.getBeginDate());
		userShopCouponDto.setCouponStatus(UserShopCouponStatusEnum.NOT_IS_USE.getValue());
		userShopCouponDto.setEndDate(shopCouponDB.getEndDate());
		userShopCouponDto.setGetCouponTime(new Date());
		userShopCouponDto.setShopCouponId(shopCouponDB.getShopCouponId());
		userShopCouponDto.setShopId(shopCouponDB.getShopId());
		userShopCouponDto.setShopMemeberId(shopMember.getMemberId()==null ? null : shopMember.getMemberId().intValue());
		userShopCouponDto.setUserId(shopMember.getUserId());
		userShopCouponDto.setMobile(shopMember.getMobile()==null ? "" : shopMember.getMobile().toString());
		Integer memberId = shopMember.getMemberId()==null ? null : shopMember.getMemberId().intValue();
		Long userId = shopMember.getUserId()==null ? null : shopMember.getUserId();
		userShopCouponDto.setShopCouponIndex(getShopCouponIndexAndValidmaxNum(memberId,
				shopCouponDB.getShopCouponId(),userId,shopCouponDB.getMaxNumPerPerson()));


		return userShopCouponDto;
	}
	@Override
	public int updateShopCoupon(ShopCouponDto shopCouponDto) throws Exception {
		if(shopCouponDto.getShopId()!=null){
			Map<String,String> requestMap = new HashMap<String,String>();
			requestMap.put("shopId", shopCouponDto.getShopId()+"");
//			requestMap.put("shopStatus", CommonConst.SHOP_STATUS_NORMAL+"");
			int count = shopDao.getShopCountByMap(requestMap);
			if(count==0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP); 
			}
		}
		if(shopCouponDto.getShopCouponId()!=null){//更新
			shopCouponDao.updateShopCoupon(shopCouponDto);
		}else{
			shopCouponDao.addShopCoupon(shopCouponDto);
			//新增优惠券与商品分类的关系
		}
		//删除所有的优惠券与商品分类的关系
		shopCouponAvailableGoodsDao.delShopCouponAvailableGoods(shopCouponDto);
		if(shopCouponDto.getCouponType() == CommonConst.COUPON_TYPE_IS_GOODS_TYPE){
			String[] GoodsCategoryIds = shopCouponDto.getGoodsCategoryIds().split(",");
			ShopCouponAvailableGoodsDto shopCouponAvailableGoods = null;
			for (String goodsCategoryId : GoodsCategoryIds) {
				shopCouponAvailableGoods = new ShopCouponAvailableGoodsDto();
				shopCouponAvailableGoods.setCouponApplyType(CommonConst.COUPON_APPLY_TYPE);
				shopCouponAvailableGoods.setShopCouponId(shopCouponDto.getShopCouponId());
				shopCouponAvailableGoods.setCouponApplyId(Integer.valueOf(goodsCategoryId));
				shopCouponAvailableGoodsDao.insertShopCouponAvailableGoods(shopCouponAvailableGoods);
			}
		}
		boolean isInsertShop=true;
		if(shopCouponDto.getAvailableShopIds() != null){
			if(shopCouponDto.getShopCouponId()!=null){
				shopCouponAvailableShopDao.delShopCouponAvailableShopDao(shopCouponDto);
			}
			String[] AvailableShopIdsArray = shopCouponDto.getAvailableShopIds().split(",");
			ShopCouponAvailableShopDto shopCouponAvailableShopDto = null;
			for (String availableShopIdStr : AvailableShopIdsArray) {
				CommonValidUtil.validStrLongFmt(availableShopIdStr, CodeConst.CODE_PARAMETER_ILLEGAL, CodeConst.MSG_FORMAT_ERROR_AVAILABLESHOPIDS);
				Long availableShopId = Long.valueOf(availableShopIdStr);
				if(availableShopId.equals(shopCouponDto.getShopId())){
					isInsertShop = false;
				}
				shopCouponAvailableShopDto = new ShopCouponAvailableShopDto();
//				shopCouponAvailableShopDto.setAvailableShopId(availableShopId.intValue());
				shopCouponAvailableShopDto.setShopCouponId(shopCouponDto.getShopCouponId());
				shopCouponAvailableShopDto.setShopId(availableShopId.intValue());
				shopCouponAvailableShopDao.insertShopCouponAvailableShop(shopCouponAvailableShopDto);
			}
		}
		if(isInsertShop){
			if(shopCouponDto.getShopCouponId()!=null){
				shopCouponAvailableShopDao.delShopCouponAvailableShopDao(shopCouponDto);
			}
			ShopCouponAvailableShopDto shopCouponAvailableShopDto = new ShopCouponAvailableShopDto();
			shopCouponAvailableShopDto.setAvailableShopId(shopCouponDto.getShopId().intValue());
			shopCouponAvailableShopDto.setShopCouponId(shopCouponDto.getShopCouponId());
			shopCouponAvailableShopDto.setShopId(shopCouponDto.getShopId().intValue());
			shopCouponAvailableShopDao.insertShopCouponAvailableShop(shopCouponAvailableShopDto);
		}
		return shopCouponDto.getShopCouponId();
	}
	@Override
	public PageModel getShopCouponList(ShopCouponDto shopCouponDto) {
		PageModel pageModel = new PageModel();
		if(shopCouponDto.getGoodsCategoryIds() != null){
			if(shopCouponDto.getGoodsCategoryIds().contains(",")){
				String[] goodsCategoryIds = shopCouponDto.getGoodsCategoryIds().split(",");
				for (String goodsCategoryId : goodsCategoryIds) {
					shopCouponDto.getGoodsCategoryIdsList().add(Integer.valueOf(goodsCategoryId));
				}
			}else{
				shopCouponDto.getGoodsCategoryIdsList().add(Integer.valueOf(shopCouponDto.getGoodsCategoryIds()));
			}
		}
		
		if(shopCouponDto.getUserShopCouponIds() != null){
			if(shopCouponDto.getUserShopCouponIds().contains(",")){
				String[] userShopCouponIds = shopCouponDto.getUserShopCouponIds().split(",");
				for (String userShopCouponId : userShopCouponIds) {
					shopCouponDto.getUserShopCouponIdsList().add(Integer.valueOf(userShopCouponId));
				}
			}else{
				shopCouponDto.getUserShopCouponIdsList().add(Integer.valueOf(shopCouponDto.getUserShopCouponIds()));
			}
		}
		
		int count = shopCouponDao.getShopCouponListCount(shopCouponDto);
		pageModel.setTotalItem(count);
		if(count>0){
			List<Map<String,Object>> shopCouponList = shopCouponDao.getShopCouponList(shopCouponDto);
			for (Map<String, Object> map : shopCouponList) {
				Integer couponType = Integer.parseInt(String.valueOf(map.get("couponType")));
				Integer couponApplyType = null ;
				if(couponType == 2){
				    couponType = 1;
				}
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("shopId", map.get("shopId"));
				param.put("shopCouponId", map.get("shopCouponId"));
				param.put("couponApplyType", couponApplyType);
				String goodsCategoryIds = null;
				String goodsCategoryNames = null;
				List<ShopCouponAvailableGoodsDto> ShopCouponAvailableGoodsDtoList = shopCouponAvailableGoodsDao.getShopCouponAvailableGoodsByMap(param);
				for (ShopCouponAvailableGoodsDto shopCouponAvailableGoodsDto : ShopCouponAvailableGoodsDtoList) {
					if(shopCouponAvailableGoodsDto != null){
						if(shopCouponAvailableGoodsDto.getCouponApplyId() != null){
							if(goodsCategoryIds == null){
								goodsCategoryIds=shopCouponAvailableGoodsDto.getCouponApplyId()+"";
							} else {
							    goodsCategoryIds+=CommonConst.COMMA_SEPARATOR+shopCouponAvailableGoodsDto.getCouponApplyId();
							}
						}
						if(shopCouponAvailableGoodsDto.getCategoryName() != null){
							if(goodsCategoryNames == null){
								goodsCategoryNames=shopCouponAvailableGoodsDto.getCategoryName();
							} else {
							    goodsCategoryNames+=CommonConst.COMMA_SEPARATOR+shopCouponAvailableGoodsDto.getCategoryName();
							}
						}
					}
				}
				map.put("goodsCategoryIds", goodsCategoryIds);
				map.put("goodsCategoryNames", goodsCategoryNames);
			}
			pageModel.setList(shopCouponList);
		}
		return pageModel;
	}

	@Override
	public Map<String, Object> shopCouponDetail(ShopCouponDto shopCouponDto) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ShopCouponDto shopCouponDtoDetail = shopCouponDao.getShopCouponById(Long.valueOf(shopCouponDto.getShopCouponId()));
		CommonValidUtil.validObjectNull(shopCouponDtoDetail, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_SHOPCOUPON_MISS);
		if(shopCouponDto.getShopId()!=null){
			Map<String,String> requestMap = new HashMap<String,String>();
			requestMap.put("shopId", shopCouponDto.getShopId()+"");
			requestMap.put("shopStatus", CommonConst.SHOP_STATUS_NORMAL+"");
			int count = shopDao.getShopCountByMap(requestMap);
			if(count==0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP); 
			}
		}
		/*if(shopCouponDto.getGoodsCategoryIds() != null){
			if(shopCouponDto.getGoodsCategoryIds().contains(",")){
				String[] goodsCategoryIds = shopCouponDto.getGoodsCategoryIds().split(",");
				for (String goodsCategoryId : goodsCategoryIds) {
					shopCouponDto.getGoodsCategoryIdsList().add(Integer.valueOf(goodsCategoryId));
				}
			}else{
				shopCouponDto.getGoodsCategoryIdsList().add(Integer.valueOf(shopCouponDto.getGoodsCategoryIds()));
			}
		}*/
		//适用门店，暂不实现
		/*if(shopCouponDto.getAvailableShopIds() != null){
			if(shopCouponDto.getAvailableShopIds().contains(",")){
				String[] availableShopIds = shopCouponDto.getAvailableShopIds().split(",");
				for (String availableShopId : availableShopIds) {
					shopCouponDto.getAvailableShopIdsList().add(Integer.valueOf(availableShopId));
				}
			}else{
				shopCouponDto.getAvailableShopIdsList().add(Integer.valueOf(shopCouponDto.getAvailableShopIds()));
			}
		}*/
		resultMap = shopCouponDao.shopCouponDetail(shopCouponDto);
		if(resultMap != null){
			Integer shopCouponId = Integer.parseInt(String.valueOf(resultMap.get("shopCouponId")));
			Integer shopId = Integer.parseInt(String.valueOf(resultMap.get("shopId")));
			Integer couponType = Integer.parseInt(String.valueOf(resultMap.get("couponType")));
			if(couponType == 2){
				couponType = 1;
			}else{
				couponType = null;
			}
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("shopId", shopId);
			param.put("shopCouponId", shopCouponId);
			param.put("couponType", couponType);
			String goodsCategoryIds = null;
			String goodsCategoryName = null;
			String availableShopIds = null;
			if(shopCouponId != null && couponType != null){
				List<ShopCouponAvailableGoodsDto> ShopCouponAvailableGoodsDtoList = shopCouponAvailableGoodsDao.getShopCouponAvailableGoodsByMap(param);
				for (ShopCouponAvailableGoodsDto shopCouponAvailableGoodsDto : ShopCouponAvailableGoodsDtoList) {
					if(shopCouponAvailableGoodsDto != null && shopCouponAvailableGoodsDto.getCouponApplyId() != null){
						if(goodsCategoryIds == null){
							goodsCategoryIds=shopCouponAvailableGoodsDto.getCouponApplyId()+"";
							goodsCategoryName=shopCouponAvailableGoodsDto.getCategoryName()+"";
							continue;
						}
						goodsCategoryIds+=","+shopCouponAvailableGoodsDto.getCouponApplyId();
						goodsCategoryName+=","+shopCouponAvailableGoodsDto.getCategoryName();
					}
				}
			}
			boolean isNotExistShop = true;
			resultMap.put("goodsCategoryIds", goodsCategoryIds);
			resultMap.put("goodsCategoryNames", goodsCategoryName);
			if(shopCouponId != null && shopId != null){
				List<ShopCouponAvailableShopDto> ShopCouponAvailableShopDtoList = shopCouponAvailableShopDao.getShopCouponAvailableShopByMap(param);
				for (ShopCouponAvailableShopDto shopCouponAvailableShopDto : ShopCouponAvailableShopDtoList) {
					if(shopCouponAvailableShopDto != null && shopCouponAvailableShopDto.getShopId() != null){
						if(shopCouponAvailableShopDto.getShopId().equals(shopId)){
							isNotExistShop = false;
						}
						if(availableShopIds == null){
							availableShopIds=shopCouponAvailableShopDto.getShopId()+"";
							continue;
						}
						availableShopIds+=","+shopCouponAvailableShopDto.getShopId();
					}
				}
			}
			if(isNotExistShop){
				if(availableShopIds==null){
					availableShopIds=shopId+"";
				}else{
					availableShopIds+=","+shopId;
				}
			}
			resultMap.put("availableShopIds", availableShopIds);
		}
		return resultMap;
	}

	@Override
	public void operateShopCoupon(String shopId,String shopCouponIds, String operateType) {
		if(shopId!=null){
			Map<String,String> requestMap = new HashMap<String,String>();
			requestMap.put("shopId", shopId);
			requestMap.put("shopStatus", CommonConst.SHOP_STATUS_NORMAL+"");
			int count = shopDao.getShopCountByMap(requestMap);
			if(count==0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP); 
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		if(Integer.valueOf(operateType) == CommonConst.OPERATE_TYPE_DELETE ){
			map.put("isDelete", CommonConst.IS_DELETE_TRUE);
		}else{
			map.put("isDelete", CommonConst.IS_DELETE_FALSE);
			if(Integer.valueOf(operateType) == CommonConst.OPERATE_TYPE_STOP){
				map.put("couponStatus",CommonConst.COUPON_IS_STOP);
			}else{
				map.put("couponStatus",CommonConst.COUPON_IS_ENABLE);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			    String dateString = formatter.format(new Date()); 
				map.put("newDate", dateString);
			}
		}
		if(shopCouponIds != null){
			List<Integer> shopCouponIdsList= new ArrayList<Integer>();
			try {
				if(shopCouponIds.contains(",")){
					String[] prerogativeTypeArray = shopCouponIds.split(",");
					Integer[] shopCouponIdsInteger=new Integer[prerogativeTypeArray.length];
					for(int i=0;i<prerogativeTypeArray.length;i++){
						shopCouponIdsInteger[i]=Integer.parseInt(prerogativeTypeArray[i]);
					}
					shopCouponIdsList = Arrays.asList(shopCouponIdsInteger);
				}else{
					shopCouponIdsList.add(Integer.valueOf(shopCouponIds));
				}
			} catch (NumberFormatException e) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_FORMAT_ERROR_SHOPCOUPONIDS); 
			}
			map.put("shopCouponIds", shopCouponIdsList);
		}
		shopCouponDao.operateShopCoupon(map);
	}
	
	@Override
	public Map<String, Object> getCouponDeductAmount(RequstCouponDeductModel requestModel) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Integer> userShopCouponIdList = new LinkedList<Integer>();
		double deductAmount = 0;
		
		resultMap.put("deductAmount", deductAmount);
		resultMap.put("userShopCouponIdList", userShopCouponIdList);
		
		Map<String, Object> tempCouponDeduct = getCouponDeductWithOutFilter(requestModel);
		
		Double tempDeductAmount = Double.valueOf(tempCouponDeduct.get("deductAmount").toString());
		
		if (tempDeductAmount == 0) {
			return tempCouponDeduct;
		}
		
		@SuppressWarnings("unchecked")
		List<Integer> tempShopCouponIdList = (List<Integer>)tempCouponDeduct.get("userShopCouponIdList");
		
		Double userTogetherDeduct = Double.valueOf(0);
		List<Integer> userTogetherCouponIdList = new ArrayList<Integer>();
		
		Map<Integer, Double> notUseTogetherDeduct = new HashMap<Integer, Double>();
		Map<Integer, List<Integer>> notUseTogetherCouponList = new HashMap<Integer, List<Integer>>();
		
		UserShopCouponDto userCoupon = new UserShopCouponDto();
		for (Integer shopCouponId : tempShopCouponIdList) {
			userCoupon.setUserShopCouponId(shopCouponId);
			List<Map<String, Object>> userShopCoupons = shopCouponDao.getUserHoldShopCouponListNotFilter(userCoupon);
			
			if (userShopCoupons.size() == 0) {
				continue;
			}
			
			Integer isUsedTogether = Integer.valueOf(userShopCoupons.get(0).get("isUsedTogether").toString());
			Double couponAmount = Double.valueOf(userShopCoupons.get(0).get("couponAmount").toString());
			Integer couponId = Integer.valueOf(userShopCoupons.get(0).get("shopCouponId").toString());
			
			if (IsUsedTogetherEnum.ENABLE == IsUsedTogetherEnum.valueOf(isUsedTogether)) {
				userTogetherDeduct = NumberUtil.add(userTogetherDeduct, couponAmount);
				userTogetherCouponIdList.add(shopCouponId);
			}else {
				if (notUseTogetherDeduct.get(couponId) == null) {
					notUseTogetherDeduct.put(couponId, couponAmount);
				}else {
					Double temp = Double.valueOf(notUseTogetherDeduct.get(couponId).toString());
					notUseTogetherDeduct.put(couponId, NumberUtil.add(couponAmount, temp));
				}
				
				if (notUseTogetherCouponList.get(couponId) == null) {
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(shopCouponId);
					notUseTogetherCouponList.put(couponId, tempList);
				}else {
					List<Integer> tempList = notUseTogetherCouponList.get(couponId);
					tempList.add(shopCouponId);
				}
			}
		}
		
		if (notUseTogetherDeduct.size() == 0) {
			resultMap.put("deductAmount", userTogetherDeduct);
			userShopCouponIdList.addAll(userTogetherCouponIdList);
			return resultMap;
		}
		Map<Double, Integer> notUseCouponDeductAmountByOrderMapper = new TreeMap<Double, Integer>();
		
		for (Entry<Integer, Double> entry :notUseTogetherDeduct.entrySet()) {
			notUseCouponDeductAmountByOrderMapper.put(entry.getValue(), entry.getKey());
		}
		
		
		Object[] notUseDeductAmountOrderd = notUseCouponDeductAmountByOrderMapper.keySet().toArray();
		int notUseDeductAmountOrderIndex = notUseDeductAmountOrderd.length-1;
			
		Double notUseDeductAmount = Double.valueOf(notUseDeductAmountOrderd[notUseDeductAmountOrderIndex].toString());
			
		if (notUseDeductAmount >= userTogetherDeduct) {
			deductAmount = notUseDeductAmount;
			Integer couponId = notUseCouponDeductAmountByOrderMapper.get(notUseDeductAmount);
			userShopCouponIdList.addAll(notUseTogetherCouponList.get(couponId));
		}else {
			deductAmount = userTogetherDeduct;
			userShopCouponIdList.addAll(userTogetherCouponIdList);
		}
	
		resultMap.put("deductAmount", deductAmount);
		return resultMap;
	}

	private Map<String, Object> getCouponDeductWithOutFilter(RequstCouponDeductModel requestModel) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Integer> userShopCouponIdList = new LinkedList<Integer>();
		double deductAmount = 0;
		
		resultMap.put("deductAmount", deductAmount);
		resultMap.put("userShopCouponIdList", userShopCouponIdList);
		
		double payAmount = requestModel.getPayAmount();
		
		UserShopCouponDto searchUserCoupon = new UserShopCouponDto();
		searchUserCoupon.setShopMemeberId(requestModel.getShopMemberId());
		searchUserCoupon.setShopId(requestModel.getShopId());
		searchUserCoupon.setMobile(requestModel.getMobile());
		Date now = new Date();
		searchUserCoupon.setBeginDate(now);
		searchUserCoupon.setEndDate(now);
		searchUserCoupon.setCouponStatus(UserShopCouponStatusEnum.NOT_IS_USE.getValue());
		searchUserCoupon.setCouponUsedCondition(requestModel.getPayAmount());
		
		Map<String, Object> allGoodsCouponDeduct = getCouponDeductAmountByCondition(searchUserCoupon);
		
		if (requestModel.getGoodsCategoryInfo() == null) {
			return allGoodsCouponDeduct;
		}
		
		double allGoodsDeductAmount = Double.valueOf(allGoodsCouponDeduct.get("deductAmount").toString());
		@SuppressWarnings("unchecked")
		List<Integer> allGoodsShopCouponIdList = (List<Integer>)allGoodsCouponDeduct.get("userShopCouponIdList");
		
		List<Map<String, Object>> designatedGoodsCouponDeductList = new ArrayList<Map<String,Object>>();
		
		for (GoodsCategoryInfo goodsCategoryInfo : requestModel.getGoodsCategoryInfo()) {
			searchUserCoupon.setGoodsCategoryId(goodsCategoryInfo.getGoodsCategoryId());
			searchUserCoupon.setCouponUsedCondition(goodsCategoryInfo.getGoodsCategoryAmount());
			Map<String, Object> designatedGoodsCouponDeduct = getCouponDeductAmountByCondition(searchUserCoupon);
			double designatedGoodsDeductAmount = Double.valueOf(designatedGoodsCouponDeduct.get("deductAmount").toString());
			if (designatedGoodsDeductAmount > 0) {
				designatedGoodsCouponDeductList.add(designatedGoodsCouponDeduct);
			}
		}
		
		if (allGoodsDeductAmount == 0 && designatedGoodsCouponDeductList.size() == 0) {
			return resultMap;
		}
		
		if (allGoodsDeductAmount != 0 && designatedGoodsCouponDeductList.size() == 0) {
			return allGoodsCouponDeduct;
		}
		
		if (allGoodsDeductAmount == 0 && designatedGoodsCouponDeductList.size() != 0) {
			for (Map<String, Object> designatedGoodsCouponDeduct :designatedGoodsCouponDeductList) {
				double designatedGoodsDeductAmount = Double.valueOf(designatedGoodsCouponDeduct.get("deductAmount").toString());
				@SuppressWarnings("unchecked")
				List<Integer> designatedGoodsShopCouponIdList = (List<Integer>)designatedGoodsCouponDeduct.get("userShopCouponIdList");
				
				deductAmount = NumberUtil.add(deductAmount, designatedGoodsDeductAmount);
				userShopCouponIdList.addAll(designatedGoodsShopCouponIdList);
				
			}
			
			resultMap.put("deductAmount", deductAmount);
			return resultMap;
		}
		
		if (allGoodsDeductAmount != 0 && designatedGoodsCouponDeductList.size() != 0) {
			
			for (Map<String, Object> designatedGoodsCouponDeduct :designatedGoodsCouponDeductList) {
				double designatedGoodsDeductAmount = Double.valueOf(designatedGoodsCouponDeduct.get("deductAmount").toString());
				@SuppressWarnings("unchecked")
				List<Integer> designatedGoodsShopCouponIdList = (List<Integer>)designatedGoodsCouponDeduct.get("userShopCouponIdList");
				
				deductAmount = NumberUtil.add(deductAmount, designatedGoodsDeductAmount);
				userShopCouponIdList.addAll(designatedGoodsShopCouponIdList);
				if (deductAmount >= payAmount) {
					resultMap.put("deductAmount", deductAmount);
					return resultMap;
				}
				
			}
			
			UserShopCouponDto userCoupon = new UserShopCouponDto();
			for (Integer userShopCouponId :allGoodsShopCouponIdList) {
				userCoupon.setUserShopCouponId(userShopCouponId);
				List<Map<String, Object>> userShopCoupons = shopCouponDao.getUserShopCouponList(userCoupon);
				
				if (userShopCoupons.size() == 0) {
					continue;
				}
				
				double couponAmount = Double.valueOf(userShopCoupons.get(0).get("couponAmount").toString());
				deductAmount = NumberUtil.add(deductAmount, couponAmount);
				userShopCouponIdList.add(userShopCouponId);
				if (deductAmount >= payAmount) {
					resultMap.put("deductAmount", deductAmount);
					return resultMap;
				}
			}
			
			resultMap.put("deductAmount", deductAmount);
			return resultMap;
		}
		
		return resultMap;
	}

	private Map<String, Object> getCouponDeductAmountByCondition(UserShopCouponDto searchUserCoupon) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Integer> userShopCouponIdList = new LinkedList<Integer>();
		double deductAmount = 0;
		
		resultMap.put("deductAmount", deductAmount);
		resultMap.put("userShopCouponIdList", userShopCouponIdList);

		double payAmount = searchUserCoupon.getCouponUsedCondition() == null ? Double.valueOf(0) : searchUserCoupon.getCouponUsedCondition();
		
		List<Map<String, Object>> userShopCoupons = shopCouponDao.getUserShopCouponList(searchUserCoupon);
		
		if (userShopCoupons== null || userShopCoupons.size() == 0) {
			return resultMap;
		}
		
		Map<Integer, Map<Integer, Set<Integer>>>  shopCouponClassify = new LinkedHashMap<Integer, Map<Integer,Set<Integer>>>();
		Map<Integer, List<Map<String, Object>>> shopCouponsById = new LinkedHashMap<Integer, List<Map<String, Object>>>();
		
		for (Map<String, Object> shopCoupon : userShopCoupons) {
			Integer isUsedTogether = Integer.valueOf(shopCoupon.get("isUsedTogether").toString());
			Integer maxNumPerOrder = Integer.valueOf(shopCoupon.get("maxNumPerOrder").toString());
			Integer shopCouponId = Integer.valueOf(shopCoupon.get("shopCouponId").toString());
			
			List<Map<String, Object>> shopCouponList = shopCouponsById.get(shopCouponId);
			if (shopCouponList == null) {
				shopCouponList = new LinkedList<Map<String, Object>>();
				shopCouponsById.put(shopCouponId, shopCouponList);
			}
			
			shopCouponList.add(shopCoupon);
			Map<Integer, Set<Integer>> shopCouponClassifyByPerOrderNum = shopCouponClassify.get(isUsedTogether);
			if (shopCouponClassifyByPerOrderNum == null) {
				shopCouponClassifyByPerOrderNum = new LinkedHashMap<Integer, Set<Integer>>();
				Set<Integer> shopCouponSet = new LinkedHashSet<Integer>();
				shopCouponSet.add(shopCouponId);
				shopCouponClassifyByPerOrderNum.put(maxNumPerOrder, shopCouponSet);
				shopCouponClassify.put(isUsedTogether, shopCouponClassifyByPerOrderNum);
			}else {
				Set<Integer> shopCouponSet  = shopCouponClassifyByPerOrderNum.get(maxNumPerOrder);
				if (shopCouponSet == null) {
					shopCouponSet = new LinkedHashSet<Integer>();
					shopCouponClassifyByPerOrderNum.put(maxNumPerOrder, shopCouponSet);
				}
				shopCouponSet.add(shopCouponId);
			}
		}
		
		Double useTogetherDeductAmount = Double.valueOf(0);
		List<Integer> useTogetherCouponList = new ArrayList<Integer>();
		
		Map<Double, List<Integer>> notUseTogetherCouponMapper = new TreeMap<Double, List<Integer>>();
		
		for (Integer isUsedTogether : shopCouponClassify.keySet()) {
			Map<Integer, Set<Integer>> shopCouponByIsUsedTogether = shopCouponClassify.get(isUsedTogether);
			if (IsUsedTogetherEnum.ENABLE == IsUsedTogetherEnum.valueOf(isUsedTogether)) {
				
				for (Integer maxNumPerOrder :shopCouponByIsUsedTogether.keySet()) {
					Set<Integer> shopCouponByPerOrder = shopCouponByIsUsedTogether.get(maxNumPerOrder);
					for (Integer shopCouponId : shopCouponByPerOrder) {
						List<Map<String, Object>> userHoldCouponList = shopCouponsById.get(shopCouponId);
						Integer userHoldCouponNum = userHoldCouponList.size();
						
						Integer useCouponNum = maxNumPerOrder <= userHoldCouponNum ? maxNumPerOrder : userHoldCouponNum;
						int index = 1;
						for (Map<String, Object> shopCoupon : userHoldCouponList) {
							if (index > useCouponNum) {
								break;
							}
							
							Double tempUseTogetherDeductAmount = NumberUtil.add(useTogetherDeductAmount, Double.valueOf(shopCoupon.get("couponAmount").toString()));
							if (tempUseTogetherDeductAmount >= payAmount) {
								resultMap.put("deductAmount", useTogetherDeductAmount);
								userShopCouponIdList.addAll(useTogetherCouponList);
								return resultMap;
							}
							useTogetherDeductAmount = tempUseTogetherDeductAmount;
							useTogetherCouponList.add(Integer.valueOf(shopCoupon.get("userShopCouponId").toString()));
							++index;
							
						}
						
					}
				}
			}else {
				
				for (Integer maxNumPerOrder :shopCouponByIsUsedTogether.keySet()) {
					Set<Integer> shopCouponByPerOrder = shopCouponByIsUsedTogether.get(maxNumPerOrder);
					for (Integer shopCouponId : shopCouponByPerOrder) {
						List<Map<String, Object>> userHoldCouponList = shopCouponsById.get(shopCouponId);
						Integer userHoldCouponNum = userHoldCouponList.size();
						
						Integer useCouponNum = maxNumPerOrder <= userHoldCouponNum ? maxNumPerOrder : userHoldCouponNum;
						
						Double notUseTogetherDeductAmountByPerCoupon = Double.valueOf(0);
						List<Integer> notUseTogetherDeductAmountOfCoupons = new ArrayList<Integer>();
						
						int index = 1;
						for (Map<String, Object> shopCoupon : userHoldCouponList) {
							if (index > useCouponNum) {
								break;
							}
							
							Double tempDeductAmount = NumberUtil.add(notUseTogetherDeductAmountByPerCoupon, Double.valueOf(shopCoupon.get("couponAmount").toString()));
							if (tempDeductAmount >= payAmount) {
								resultMap.put("deductAmount", notUseTogetherDeductAmountByPerCoupon);
								userShopCouponIdList.addAll(notUseTogetherDeductAmountOfCoupons);
								return resultMap;
							}
							
							notUseTogetherDeductAmountByPerCoupon = tempDeductAmount;
							notUseTogetherDeductAmountOfCoupons.add(Integer.valueOf(shopCoupon.get("userShopCouponId").toString()));
							++index;
							
						}
						
						if (notUseTogetherDeductAmountByPerCoupon > 0) {
							notUseTogetherCouponMapper.put(notUseTogetherDeductAmountByPerCoupon, notUseTogetherDeductAmountOfCoupons);
						}
						
					}
				}
				
			}
		}
		
		Integer useTogetherTotalNum = useTogetherCouponList.size();
		Integer notUseTogetherTotalNum = notUseTogetherCouponMapper.size();
		
		if (notUseTogetherTotalNum != 0) {
			
			Object[] notUseDeductAmountOrderd = notUseTogetherCouponMapper.keySet().toArray();
			int notUseDeductAmountOrderIndex = notUseDeductAmountOrderd.length-1;
		
			if (useTogetherTotalNum != 0 ) {
				
				for (int index = notUseDeductAmountOrderIndex; index >= 0 ; index--) {
					Double notUseDeductAmount = Double.valueOf(notUseDeductAmountOrderd[index].toString());
					Double totalDeductAmount = NumberUtil.add(useTogetherDeductAmount, notUseDeductAmount);
					if (totalDeductAmount >= payAmount) {
						continue;
					}
					
					resultMap.put("deductAmount", totalDeductAmount);
					userShopCouponIdList.addAll(notUseTogetherCouponMapper.get(notUseDeductAmount));
					userShopCouponIdList.addAll(useTogetherCouponList);
					return resultMap;
				}
				
				Double mostBigNotUseDeductAmount = Double.valueOf(notUseDeductAmountOrderd[notUseDeductAmountOrderIndex].toString());
				
				if (mostBigNotUseDeductAmount > useTogetherDeductAmount) {
					resultMap.put("deductAmount", mostBigNotUseDeductAmount);
					userShopCouponIdList.addAll(notUseTogetherCouponMapper.get(mostBigNotUseDeductAmount));
				}else {
					resultMap.put("deductAmount", useTogetherDeductAmount);
					userShopCouponIdList.addAll(useTogetherCouponList);
				}
				
			}else {
				resultMap.put("deductAmount", notUseDeductAmountOrderd[notUseDeductAmountOrderIndex]);
				userShopCouponIdList.addAll(notUseTogetherCouponMapper.get(notUseDeductAmountOrderd[notUseDeductAmountOrderIndex]));
			}
		}
		else if (useTogetherTotalNum != 0 && notUseTogetherTotalNum == 0) {
			resultMap.put("deductAmount", useTogetherDeductAmount);
			userShopCouponIdList.addAll(useTogetherCouponList);
		}
		
		return resultMap;
	}
	
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shopCoupon.IShopCouponService#getCouponReceiveDetail(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public Map<String, Object> getCouponReceiveDetail(Long shopId ,Long shopCouponId,Integer status,Integer pageNo,Integer pageSize) 
			throws Exception{
		
		ShopCouponDto parms = new ShopCouponDto();
		parms.setShopId(shopId);
		parms.setShopCouponId(shopCouponId.intValue());
		parms.setCouponStatus(status);
		List<Map<String, Object>> coupons = shopCouponDao.getCouponReceiveDetail(shopId, shopCouponId, status, pageNo, pageSize);
		
		Map<String, Object> resultMap   = splitUserShopCouponList(coupons);
		
		return resultMap;
	}
	private Map<String, Object> splitUserShopCouponList(List<Map<String, Object>> coupons) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if(CollectionUtils.isNotEmpty(coupons)){
			
			List<Map<String, Object>> userInfo = new ArrayList<Map<String,Object>>();
			
			for (Map<String, Object> couponDB : coupons) {
				Map<String, Object> user = new HashMap<String, Object>();
				user.put("mobile", couponDB.get("mobile"));
				user.put("getCouponTime", couponDB.get("getCouponTime"));
				user.put("usedCouponTime", couponDB.get("usedCouponTime"));
				user.put("orderId", couponDB.get("orderId"));
				if(couponDB.get("mobile") != null){
					userInfo.add(user);
				}
			}
			
			Map<String, Object> coupon = coupons.get(0);
			resultMap.put("couponName", coupon.get("couponName"));
			resultMap.put("totalNum", coupon.get("totalNum"));
			resultMap.put("getTotalNum", coupon.get("getTotalNum"));
			resultMap.put("usedTotalNum", coupon.get("usedTotalNum"));
			resultMap.put("userInfo", userInfo);
			
		}
		
		return resultMap;
	}
	
	@Override
	public List<ShopCouponDto> getShopCouponListByDtoAndPage(ShopCouponDto shopCouponDto,
			PageModel pageModel) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopCouponDto", shopCouponDto);
		if(pageModel!=null){
			params.put("start", (pageModel.getToPage() - 1) * pageModel.getPageSize());
			params.put("limit", pageModel.getPageSize());
		}
		return shopCouponDao.getShopCouponListByDtoAndPage(params);
	}
	@Override
	public void batchUpdateShopCoupon(List<ShopCouponDto> shopCouponDtoUpdateList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopCouponDtoList", shopCouponDtoUpdateList);
		shopCouponDao.batchUpdateShopCoupon(params);
	}
	@Override
	public List<UserShopCouponDto> getUserShopCouponList(PageModel pageModel) throws Exception {
		UserShopCouponDto userShopCouponDto = new UserShopCouponDto();
		userShopCouponDto.setCouponStatus(CommonConst.USER_SHOP_COUPON_UNUSED);
		if(pageModel!=null){
			userShopCouponDto.setPageNo((pageModel.getToPage() - 1) * pageModel.getPageSize());
			userShopCouponDto.setPageSize(pageModel.getPageSize());
		}
		return userShopCouponDao.getUserShopCouponList(userShopCouponDto);
	}
	@Override
	public void batchUpdateUserShopCoupon(List<UserShopCouponDto> userShopCouponDtoUpdateList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("userShopCouponDtoList", userShopCouponDtoUpdateList);
		userShopCouponDao.batchUpdateUserShopCoupon(params);
//		shopCouponDao.batchUpdateShopCoupon(params);
		
	}
	
	
	
}
