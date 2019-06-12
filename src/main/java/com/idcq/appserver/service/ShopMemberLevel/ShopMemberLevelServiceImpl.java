package com.idcq.appserver.service.ShopMemberLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.controller.BaseController;
import com.idcq.appserver.dao.ShopMemberLevel.IShopMemberLevelDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.shop.ShopMemberLevelDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;

@Service
public class ShopMemberLevelServiceImpl implements IShopMemberLevelService {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private IShopMemberLevelDao shopMemberLevelDao;
	
	@Autowired
	private IShopMemberDao shopMemberDao;
	
	@Autowired
	private IOrderDao orderDao;

	@Override
	public Map<String, Integer> updateShopMemberLevel(ShopMemberLevelDto shopMemberLevelDto) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopMemberLevelDto.getShopId());
		param.put("wholeShopMemberLevelName", shopMemberLevelDto.getShopMemberLevelName());
		param.put("isDelete", CommonConst.IS_DELETE_FALSE);
		param.put("shopMemberLevelId", shopMemberLevelDto.getShopMemberLevelId());
		param.put("isNotEqual", 1);
		List<ShopMemberLevelDto> shopMemberLevelDtoList = shopMemberLevelDao.getShopMemberLevelList(param);
		if(shopMemberLevelDtoList != null && shopMemberLevelDtoList.size()>0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "同一店铺内不能存在同名的会员等级");
		}
		param.remove("wholeShopMemberLevelName");
		param.put("consumeAmount", shopMemberLevelDto.getConsumeMinAmount());
		List<ShopMemberLevelDto> shopMemberLevelDtoList1 = shopMemberLevelDao.getShopMemberLevelList(param);
		List<ShopMemberLevelDto> shopMemberLevelDtoList2 = null;
		Double consumeMaxAmout =shopMemberLevelDto.getConsumeMaxAmout();
		if(consumeMaxAmout != null && consumeMaxAmout != 0){
			param.put("consumeAmount", shopMemberLevelDto.getConsumeMaxAmout());
			shopMemberLevelDtoList2 = shopMemberLevelDao.getShopMemberLevelList(param);
		}
		
		param.remove("consumeAmount");
		param.put("overConsumeMinAmount", shopMemberLevelDto.getConsumeMinAmount());
		if(consumeMaxAmout != null && consumeMaxAmout != 0){
			param.put("overConsumeMaxAmount", shopMemberLevelDto.getConsumeMaxAmout());
		}
		List<ShopMemberLevelDto> shopMemberLevelDtoList3 = shopMemberLevelDao.getShopMemberLevelList(param);
		if((shopMemberLevelDtoList1 != null && shopMemberLevelDtoList1.size()>0) || 
				(shopMemberLevelDtoList2 != null && shopMemberLevelDtoList2.size()>0)|| 
				(shopMemberLevelDtoList3 != null && shopMemberLevelDtoList3.size()>0)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "同一店铺内两个等级的金额范围不能交叉");
		}
		Integer shopMemberLevelId = null;
		if(null != shopMemberLevelDto.getShopMemberLevelId()){
			shopMemberLevelDao.updateShopMemberLevel(shopMemberLevelDto);
			shopMemberLevelId = shopMemberLevelDto.getShopMemberLevelId();
		}else{
			shopMemberLevelDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
			shopMemberLevelId = shopMemberLevelDao.addShopMemberLevelDto(shopMemberLevelDto);
			shopMemberDao.updateShopMemberByLevelEntity(shopMemberLevelDto);
		}
		resultMap.put("shopMemberLevelId", shopMemberLevelId);
		return resultMap;
	}

	@Override
	public Map<String, Object> getShopMemberLevelList(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ShopMemberLevelDto> shopMemberLevelDtoList = shopMemberLevelDao.getShopMemberLevelList(paramMap);
		Integer isSearchMemberNum = (Integer) paramMap.get("isSearchMemberNum");
		if(isSearchMemberNum == 1){
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("shopId", paramMap.get("shopId"));
			for (ShopMemberLevelDto shopMemberLevelDto : shopMemberLevelDtoList) {
				param.put("shopMemberLevelId",shopMemberLevelDto.getShopMemberLevelId());
				int containMemberNum = shopMemberDao.queryShopMemberCount(param);
				shopMemberLevelDto.setContainMemberNum(containMemberNum);
			}
		}
		resultMap.put("lst", shopMemberLevelDtoList);
		int count = 0;
		if(shopMemberLevelDtoList != null && shopMemberLevelDtoList.size()>0){
			count = shopMemberLevelDtoList.size();
		}
		resultMap.put("rCount", count);
		return resultMap;
	}

	/**
	 * 升级会员等级：
	 * 情况1：如果消费为0属于对应的店内会员的某个等级，
	 *        则将新加入的店内会员加入该等级
	 * 情况2：如果店内会员消费了(order_status为已完成)，则查询该会员的消费总额
	 *        是否达到了对应的某个等级，如果该之前存在等
	 *        级，且要求自动升级，则该店内会员就进行升级。
	 * @param mobile 会员手机号,必传 
	 * @param shopId 会员所在店铺Id,必传 
	 * @param flag 新增会员-1；下单结算-2,必传 
	 * @throws Exception 
	 */
	@Override
	public void upgrateShopMemberLevel(String mobile, Long shopId, int flag) {
		try {
			logger.info("升级会员等级-start");
			CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			CommonValidUtil.validStrNull(mobile, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MOBILE_NOT_NULL);
			if(flag == 1){//新增会员-1
				//查询是否存在消费金额为0的等级
				Map<String,Object> paramMap = new HashMap<String, Object>();
				paramMap.put("shopId", shopId);
				paramMap.put("consumeAmount", 0);
				paramMap.put("isDelete", CommonConst.IS_DELETE_FALSE);
				List<ShopMemberLevelDto> shopMemberLevelDtoList = shopMemberLevelDao.getShopMemberLevelList(paramMap);
				if(shopMemberLevelDtoList != null && shopMemberLevelDtoList.size() > 0){
					ShopMemberLevelDto shopMemberLevelDto = shopMemberLevelDtoList.get(0);
					ShopMemberDto shopMemberDto =shopMemberDao.getShopMbByMobileAndShopId(mobile, shopId, CommonConst.MEMBER_STATUS_DELETE);
					if(shopMemberDto != null){
						shopMemberDto.setShopMemberLevelId(shopMemberLevelDto.getShopMemberLevelId());
						shopMemberDao.updateShopMemberById(shopMemberDto);
					}
				}
			}else if (flag == 2){//下单结算-2
				//查询店内会员加入该店以来在该店的消费额度
				Map<String,Object> paramMap = new HashMap<String, Object>();
				paramMap.put("shopId", shopId);
				paramMap.put("memberStatus", CommonConst.MEMBER_STATUS_DELETE);
				paramMap.put("orderStatus", CommonConst.ORDER_STATUS_FINISH);
				paramMap.put("mobile", mobile);
				Double settlePriceSum1 = orderDao.getShopMemberSettlePriceSum(paramMap);
				double settlePriceSum = 0;
				if(settlePriceSum1 != null){
					settlePriceSum = Double.valueOf(settlePriceSum1);
				}
				//通过手机号查询店内会员的等级
				ShopMemberDto shopMemberDto =shopMemberDao.getShopMbByMobileAndShopId(mobile, shopId, CommonConst.MEMBER_STATUS_DELETE);
				if(shopMemberDto != null){
					if(shopMemberDto.getShopMemberLevelId() == null){
						Map<String,Object> param = new HashMap<String, Object>();
						param.put("shopId", shopId);
						param.put("consumeAmount", settlePriceSum);
						param.put("isDelete", CommonConst.IS_DELETE_FALSE);
						List<ShopMemberLevelDto> shopMemberLevelDtoList = shopMemberLevelDao.getShopMemberLevelList(param);
						if(shopMemberLevelDtoList != null && shopMemberLevelDtoList.size() > 0){
							//如果该会员当前的消费总额能查询到对应的会员，在升级
							ShopMemberLevelDto shopMemberLevelDto = shopMemberLevelDtoList.get(0);
							shopMemberDto.setShopMemberLevelId(shopMemberLevelDto.getShopMemberLevelId());
							shopMemberDao.updateShopMemberById(shopMemberDto);
						}
					}else{
						double consumeMinAmount = shopMemberDto.getConsumeMinAmount()==null?0:Double.valueOf(shopMemberDto.getConsumeMinAmount());
						double consumeMaxAmout = shopMemberDto.getConsumeMaxAmout()==null?0:Double.valueOf(shopMemberDto.getConsumeMaxAmout());
						//如果再当前等级的消费范围内，则不需要升级，如果当前的消费总额低于当前的会员的等级也不需要升级，否则升级
						if(shopMemberDto.getIsAutoUpgrate() != null && shopMemberDto.getIsAutoUpgrate() == 1){
							if(!(settlePriceSum < consumeMinAmount ||(consumeMinAmount<=settlePriceSum && consumeMaxAmout>settlePriceSum))){
								//不在当前等级的范围内
								Map<String,Object> param = new HashMap<String, Object>();
								param.put("shopId", shopId);
								param.put("consumeAmount", settlePriceSum);
								param.put("isDelete", CommonConst.IS_DELETE_FALSE);
								List<ShopMemberLevelDto> shopMemberLevelDtoList = shopMemberLevelDao.getShopMemberLevelList(param);
								if(shopMemberLevelDtoList != null && shopMemberLevelDtoList.size() > 0){
									//如果该会员当前的消费总额能查询到对应的会员，在升级
									ShopMemberLevelDto shopMemberLevelDto = shopMemberLevelDtoList.get(0);
									shopMemberDto.setShopMemberLevelId(shopMemberLevelDto.getShopMemberLevelId());
									shopMemberDao.updateShopMemberById(shopMemberDto);
								}
							}
						}
					}
				}
				logger.info("升级会员等级-end");
			}else{
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "flag参数传递不符合要求");
			}
		} catch (Exception e) {
			logger.error("升级会员等级-系统异常", e);
			e.printStackTrace();
		}
		
	}

	@Override
	public int deleteShopMemberLevel(String shopIdStr,String shopMemberLevelIdStr,String operateTypeStr){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopIdStr);
		map.put("shopMemberLevelId", shopMemberLevelIdStr);
		return shopMemberLevelDao.deleteShopMemberLevel(map);
	}
	
}
