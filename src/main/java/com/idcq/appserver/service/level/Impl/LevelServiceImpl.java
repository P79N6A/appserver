package com.idcq.appserver.service.level.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.level.ILevelDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.level.AddPointDetailModel;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.dto.level.LevelDto;
import com.idcq.appserver.dto.level.LevelValidDto;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.level.PointRuleValidDto;
import com.idcq.appserver.dto.level.PrerogativeDto;
import com.idcq.appserver.dto.level.PrerogativeValidDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.level.processor.IPointProcessor;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.mq.MqProduceApi;
import com.idcq.idianmgr.dto.shop.ShopBean;
@Service
public class LevelServiceImpl implements ILevelService {
	private final static Logger logger = LoggerFactory.getLogger(LevelServiceImpl.class);	
	@Autowired
	private ILevelDao levelDao;
	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IGoodsDao goodsDao;
	@Autowired
	private IUserDao userDao;
	
	@Override
	public Map<String,Object> getLevelDetail(String levelId, String shopId) throws Exception {
		LevelDto  levelDto = levelDao.getLevelDetail(levelId, shopId);
		Map<String,Object> reusltMap = new HashMap<String, Object>();
		if(levelDto!=null){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("levelId", levelDto.getLevelId());
			List<PrerogativeDto> prerogativeDtos = levelDao.getPrerogativeList(map);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> map1=null;
			for (PrerogativeDto prerogativeDto : prerogativeDtos) {
				if(prerogativeDto !=null ){
					if(prerogativeDto.getPrerogativeImageUrl() != null){
						prerogativeDto.setPrerogativeImageUrl(FdfsUtil.getFileProxyPath(levelDto.getLevelImageUrl()));
					}
					map1=new HashMap<String, Object>();
					map1.put("prerogativeId", prerogativeDto.getPrerogativeId());
					map1.put("prerogativeName", prerogativeDto.getPrerogativeName());
					map1.put("prerogativeValue", prerogativeDto.getPrerogativeValue());
					map1.put("prerogativeImageUrl", prerogativeDto.getPrerogativeImageUrl());
					map1.put("sortBy", prerogativeDto.getSortBy());
					map1.put("prerogativeDesc", prerogativeDto.getPrerogativeDesc());
					listMap.add(map1);
				}
			}
			
			if(levelDto.getLevelImageUrl() != null){
				levelDto.setLevelImageUrl(FdfsUtil.getFileProxyPath(levelDto.getLevelImageUrl()));
			}
			LevelDto nextLevel = getNextLevel(levelDto.getLevelId());
			reusltMap.put("levelName", levelDto.getLevelName());
			reusltMap.put("levelCondition", levelDto.getLevelCondition());
			reusltMap.put("sortBy", levelDto.getSortBy());
			reusltMap.put("levelDesc", levelDto.getLevelDesc());
			reusltMap.put("levelImageUrl", levelDto.getLevelImageUrl());
			if(nextLevel != null){
				reusltMap.put("nextLevelId",nextLevel.getLevelId());
				reusltMap.put("nextLevelCondition", nextLevel.getLevelCondition());
				reusltMap.put("nextLevelName", nextLevel.getLevelName());
				
			}else{
				reusltMap.put("nextLevelId",null);
				reusltMap.put("nextLevelCondition", null);
				reusltMap.put("nextLevelName", null);
			}
			reusltMap.put("prerogativeList", listMap);
			
		}
		return reusltMap;
	}
	@Override
	public PageModel getLevelList(Map<String, Object> map) throws Exception {
		PageModel pageModel = new PageModel();
		int count = levelDao.getLevelListCount(map);
		pageModel.setTotalItem(count);
		List<Map<String,Object>> reusltMapList = new ArrayList<Map<String,Object>>();
		if(count>0){
			Map<String,Object> map1=null;
			List<LevelDto> levelDtoList = levelDao.getLevelList(map);
			for (LevelDto levelDto : levelDtoList) {
				map1 = new HashMap<String, Object>();
				if(levelDto !=null && levelDto.getLevelImageUrl() !=null){
					levelDto.setLevelImageUrl(FdfsUtil.getFileProxyPath(levelDto.getLevelImageUrl()));
				}
				map1.put("levelId", levelDto.getLevelId());
				map1.put("levelName", levelDto.getLevelName());
				map1.put("levelCondition", levelDto.getLevelCondition());
				map1.put("sortBy", levelDto.getSortBy());
				map1.put("levelDesc", levelDto.getLevelDesc());
				map1.put("levelImageUrl", levelDto.getLevelImageUrl());
				reusltMapList.add(map1);
			}
			pageModel.setList(reusltMapList);
		}
		
		return pageModel;
	}
	@Override
	public PageModel getPrerogativeList(Map<String, Object> map) throws Exception {
		PageModel pageModel = new PageModel();
		String shopId = (String) map.get("shopId");
		int count = levelDao.getPrerogativeListCount(map);
		pageModel.setTotalItem(count);
		if(count>0){
			Map<String,Object> resutlMap =null;
			List<Map<String,Object>> resutlList=new ArrayList<Map<String,Object>>();
			List<PrerogativeDto> prerogativeDtoList = levelDao.getPrerogativeList(map);
			for (PrerogativeDto prerogativeDto : prerogativeDtoList) {
				if(prerogativeDto !=null){
					if(prerogativeDto.getPrerogativeImageUrl() != null){
						prerogativeDto.setPrerogativeImageUrl(FdfsUtil.getFileProxyPath(prerogativeDto.getPrerogativeImageUrl()));
					}
					if(prerogativeDto.getPrerogativeId()!=null){
						Map<String,Object> idMap = new HashMap<String,Object>();
						idMap.put("prerogativeId", prerogativeDto.getPrerogativeId());
						prerogativeDto.setLevelIdList(levelDao.getLevelIdByPrerogativeId(idMap));
					}
				}
				
			}
			
			if(shopId != null){//查询出该店铺的等级
				//更新商铺信息
		        ShopDto shopDB = shopDao.getShopById(Long.valueOf(shopId));
				if (null == shopDB) {
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_MISS_SHOP);
				}
				Map<String,Object> map1 = new HashMap<String, Object>();
				map1.put("shopId", shopId);
				List<PrerogativeDto> prerogativeDtoListOfShop = levelDao.getPrerogativeByMap(map1);
				Set<Integer> shopPrerogativeSet = new HashSet<Integer>();
				for (PrerogativeDto shopPrerogative : prerogativeDtoListOfShop) {
					shopPrerogativeSet.add(shopPrerogative.getPrerogativeId());
				}
				
				for (PrerogativeDto prerogativeDto : prerogativeDtoList) {
					if (shopPrerogativeSet.contains(prerogativeDto.getPrerogativeId())) {
						prerogativeDto.setHoldFlag(1);
					} else  {
						prerogativeDto.setHoldFlag(0);
					}
				}
				
			}
			for (PrerogativeDto prerogativeDto : prerogativeDtoList) {
				resutlMap = new HashMap<String, Object>(); 
				resutlMap.put("prerogativeId", prerogativeDto.getPrerogativeId());
				resutlMap.put("prerogativeName", prerogativeDto.getPrerogativeName());
				resutlMap.put("prerogativeImageUrl", prerogativeDto.getPrerogativeImageUrl());
				resutlMap.put("sortBy", prerogativeDto.getSortBy());
				resutlMap.put("prerogativeDesc", prerogativeDto.getPrerogativeDesc());
				resutlMap.put("levelIdList", prerogativeDto.getLevelIdList());
				resutlMap.put("holdFlag", prerogativeDto.getHoldFlag());
				resutlList.add(resutlMap);
			}
			pageModel.setList(resutlList);
		}
		return pageModel;
	}
	@Override
	public PrerogativeDto getPrerogativeDetail(Map<String, Object> map) throws Exception {
		PrerogativeDto  prerogativeDto = levelDao.getPrerogativeDetail(map);
		if(prerogativeDto !=null && prerogativeDto.getPrerogativeImageUrl() != null){
			prerogativeDto.setPrerogativeImageUrl(FdfsUtil.getFileProxyPath(prerogativeDto.getPrerogativeImageUrl()));
		}
		return prerogativeDto;
	}
	@Override
	public PageModel getPointDetail(PointDetailDto pointDto) throws Exception {
		PageModel pageModel = new PageModel();
		int count = levelDao.getPointDetailCount(pointDto);
		pageModel.setTotalItem(count);
		if(count>0){
			List<PointDetailDto> pointDetailDto = levelDao.getPointDetail(pointDto);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			for (PointDetailDto pointDetailDto1 : pointDetailDto) {
				if(pointDetailDto1 !=null){
					map= new HashMap<String, Object>();
					UserDto userDto = null;
					if(pointDetailDto1.getBizType()==CommonConst.BIZ_TYPE_IS_SHOP){
						ShopDto shopDto = shopDao.getShopByIdWithoutCache(pointDetailDto1.getBizId().longValue());
						map.put("pointTargetName", shopDto==null?"":shopDto.getShopName());
						userDto = userDao.getDBUserById(shopDto.getPrincipalId());
					}else if(pointDetailDto1.getBizType()==CommonConst.BIZ_TYPE_IS_USER){
						userDto = userDao.getDBUserById(pointDetailDto1.getBizId().longValue());
						map.put("pointTargetName", userDto==null?"":userDto.getUserName());
					}
					map.put("pointTargetMobile", userDto==null?"":userDto.getMobile());
					map.put("pointTargetId", pointDetailDto1.getBizId());
					map.put("pointDetailId", pointDetailDto1.getPointDtailId());
					map.put("pointSourceType", pointDetailDto1.getPointSourceType());
					map.put("pointDetailTitle", pointDetailDto1.getPointDetailTitle());
					map.put("pointValue", pointDetailDto1.getPointValue());
					map.put("pointAfterValue", pointDetailDto1.getPointAferValue());
					map.put("createTime", pointDetailDto1.getCreateTime());
					map.put("operaterId", pointDetailDto1.getOperaterId());
					map.put("operaterName", pointDetailDto1.getOperaterName());
					map.put("remark", pointDetailDto1.getRemark());
					map.put("ruleType", pointDetailDto1.getRuleType());
					map.put("ruleName", pointDetailDto1.getRuleName());
					listMap.add(map);
				}
			}
			pageModel.setList(listMap);
		}
		return pageModel;
	}
	
	@Override
	public PageModel getPointDetailByBizIds(PointDetailDto pointDetailDto) throws Exception {
		PageModel pageModel = new PageModel();
		Map<String,Object> requestMap  =  new HashMap<String, Object>();
		if(pointDetailDto.getBizIds() != null){
			String[] bizIdsArray = pointDetailDto.getBizIds().split(",");
			List<String> bizIdList= Arrays.asList(bizIdsArray);
			requestMap.put("bizIds", bizIdList);
		}
		requestMap.put("shopId", pointDetailDto.getShopId());
		requestMap.put("bizType", pointDetailDto.getBizType());
		requestMap.put("pointRuleId", pointDetailDto.getPointRuleId());
		requestMap.put("pointSourceType", pointDetailDto.getPointSourceType());
		requestMap.put("pointSourceId", pointDetailDto.getPointSourceId());
		requestMap.put("lowerPointValue", pointDetailDto.getLowerPointValue());
		requestMap.put("upperPointValue", pointDetailDto.getUpperPointValue());
		requestMap.put("startTime", pointDetailDto.getStartTime());
		requestMap.put("endTime", pointDetailDto.getEndTime());
		requestMap.put("taskType", pointDetailDto.getTaskType());
		requestMap.put("pNo", pointDetailDto.getpNo());
		requestMap.put("pSize", pointDetailDto.getpSize());
		int count = levelDao.getPointDetailCount(requestMap);
		pageModel.setTotalItem(count);
		if(count>0){
			List<PointDetailDto> pointDetailDtos = levelDao.getPointDetail(requestMap);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			for (PointDetailDto pointDetailDto1 : pointDetailDtos) {
				if(pointDetailDto1 !=null){
					map= new HashMap<String, Object>();
					UserDto userDto = null;
					if(pointDetailDto1.getBizType()==CommonConst.BIZ_TYPE_IS_SHOP){
						ShopDto shopDto = shopDao.getShopByIdWithoutCache(pointDetailDto1.getBizId().longValue());
						map.put("pointTargetName", shopDto==null?"":shopDto.getShopName());
						userDto = userDao.getDBUserById(shopDto.getPrincipalId());
					}else if(pointDetailDto1.getBizType()==CommonConst.BIZ_TYPE_IS_USER){
						userDto = userDao.getDBUserById(pointDetailDto1.getBizId().longValue());
						map.put("pointTargetName", userDto==null?"":userDto.getUserName());
					}
					map.put("pointTargetMobile", userDto==null?"":userDto.getMobile());
					map.put("pointTargetId", pointDetailDto1.getBizId());
					map.put("pointDetailId", pointDetailDto1.getPointDtailId());
					map.put("pointSourceType", pointDetailDto1.getPointSourceType());
					map.put("pointDetailTitle", pointDetailDto1.getPointDetailTitle());
					map.put("pointValue", pointDetailDto1.getPointValue());
					map.put("pointAfterValue", pointDetailDto1.getPointAferValue());
					map.put("createTime", pointDetailDto1.getCreateTime());
					map.put("operaterId", pointDetailDto1.getOperaterId());
					map.put("operaterName", pointDetailDto1.getOperaterName());
					map.put("remark", pointDetailDto1.getRemark());
					map.put("ruleType", pointDetailDto1.getRuleType());
					map.put("ruleName", pointDetailDto1.getRuleName());
					listMap.add(map);
				}
			}
			pageModel.setList(listMap);
		}
		return pageModel;
	}
	@Override
	public void operateLevelInfo(String shopId, String levelIds, String operateType) throws Exception {
		String[] levelIdArray = null;
		if(levelIds.contains(",")){
			levelIdArray = levelIds.split(","); 
		}else{
			levelIdArray = new String[1];
			levelIdArray[0]=levelIds;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("isDelete", CommonConst.IS_DELETE_TRUE);
		if(operateType != null &&Integer.valueOf(operateType)==CommonConst.OPERATE_TYPE_DELETE){
			for (int i = 0; i < levelIdArray.length; i++) {
				CommonValidUtil.validStrIntFmt(levelIdArray[i],CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_FORMAT_ERROR_LEVELID);
				LevelDto level = levelDao.getLevelDetail(levelIdArray[i], shopId);
				CommonValidUtil.validObjectNull(level, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_LEVEL);
				map.put("levelId", levelIdArray[i]);
				levelDao.delLevelInfoByConditions(map);
			}
		}
	}
	@Override
	public void operatePrerogativeInfo(String shopId, String prerogativeIds,String operateType) throws Exception {
		String[] prerogativeIdArray = null;
		if(prerogativeIds.contains(",")){
			prerogativeIdArray = prerogativeIds.split(","); 
		}else{
			prerogativeIdArray = new String[1];
			prerogativeIdArray[0]=prerogativeIds;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("isDelete", CommonConst.IS_DELETE_TRUE);
		if(operateType != null && Integer.valueOf(operateType)==CommonConst.OPERATE_TYPE_DELETE){
			for (int i = 0; i < prerogativeIdArray.length; i++) {
				Map<String,Object> map1 = new HashMap<String, Object>();
				map1.put("prerogativeId", prerogativeIdArray[i]);
				map1.put("isDelete", CommonConst.IS_DELETE_FALSE);
				PrerogativeDto prerogative = levelDao.getPrerogativeDetail(map1);
				CommonValidUtil.validObjectNull(prerogative, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_PREROGATIVE);
				map.put("prerogativeId", prerogativeIdArray[i]);
				levelDao.delPrerogativeInfoByConditions(map);
			}
		}
		
	}
	@Override
	public PageModel getPointRuleList(PointRuleDto pointRuleDto) {
		PageModel pageModel = new PageModel();
		pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		int count = levelDao.getPointRuleListCount(pointRuleDto);
		pageModel.setTotalItem(count);
		List<Map<String,Object>> resultMapList =new ArrayList<Map<String,Object>>();
		if(count>0){
			Map<String,Object> map =null;
			List<PointRuleDto> pointRuleDtos = levelDao.getPointRuleList(pointRuleDto);
			for (PointRuleDto pointRuleDto2 : pointRuleDtos) {
				map = new HashMap<String, Object>();
				map.put("pointRuleId", pointRuleDto2.getPointRuleId());
				map.put("ruleType", pointRuleDto2.getRuleType());
				map.put("taskType", pointRuleDto2.getTaskType());
				map.put("subRuleType", pointRuleDto2.getSubRuleType());
				map.put("ruleName", pointRuleDto2.getRuleName());
				map.put("ruleDetail", pointRuleDto2.getRuleDetail());
				map.put("pointValue", pointRuleDto2.getPointValue());
				resultMapList.add(map);
			}
			pageModel.setList(resultMapList);
			pageModel.setToPage(pointRuleDto.getPageNo()+1);
		}
		return pageModel;
	}
	@Override
	public int operateLevelPrerogativeInfo(String shopId, String prerogativeIds,
			String levelId, String operateType) throws Exception {
		int operateTypeIntValue = Integer.valueOf(operateType);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("levelId", levelId);
		String[] prerogativeIdArray = null;
		if(prerogativeIds.contains(",")){
			prerogativeIdArray = prerogativeIds.split(","); 
		}else{
			prerogativeIdArray = new String[1];
			prerogativeIdArray[0]=prerogativeIds;
		}
		LevelDto level = levelDao.getLevelDetail(levelId, shopId);
		CommonValidUtil.validObjectNull(level, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_LEVEL);
	
		if(operateTypeIntValue == CommonConst.OPERATE_TYPE_DELETE){//删除
			map.put("isDelete", CommonConst.IS_DELETE_TRUE);
			for (int i = 0; i < prerogativeIdArray.length; i++) {
				map.put("prerogativeId", prerogativeIdArray[i]);
				levelDao.delLevelPrerogativeInfo(map);
			}
		}else{
			if(operateTypeIntValue == CommonConst.OPERATE_TYPE_UPDATE){//如果是修改，先删除，后新增
				levelDao.delLevelPrerogativeInfo(map);
			}
			for (int i = 0; i < prerogativeIdArray.length; i++) {
				map.put("prerogativeId", prerogativeIdArray[i]);
				PrerogativeDto prerogative = levelDao.getPrerogativeDetail(map);
				CommonValidUtil.validObjectNull(prerogative, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_PREROGATIVE);
				levelDao.addLevelPrerogativeInfo(map);
			}
		}
		return 0;
	}
	
	@Override
	public int updateLevelInfo(LevelValidDto levelDto) throws Exception {
		int levelId=0;
		if(levelDto != null){
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("levelType", levelDto.getLevelType());
			map.put("levelCondition", levelDto.getLevelCondition());
			map.put("levelName", levelDto.getLevelName());
			map.put("sortBy", levelDto.getSortBy());
			map.put("isDelete", CommonConst.IS_DELETE_FALSE);
			List<LevelDto> levelDtoList = levelDao.getLevelList(map);
			if(levelDtoList!=null && levelDtoList.size()>0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_EXIST_LEVEL);
			}
			if(levelDto.getLevelId() != null){
				LevelDto levelDtoDetail = levelDao.getLevelDetail(String.valueOf(levelDto.getLevelId()), null);
				CommonValidUtil.validObjectNull(levelDtoDetail, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_LEVEL);
				levelId = levelDto.getLevelId();
				levelDao.updateLevelInfo(levelDto);
			}else{
				levelId = levelDao.addLevelInfo(levelDto);
			}
		}
		return levelId;
	}

	public int updatePrerogativeInfo(PrerogativeValidDto prerogativeDto) throws Exception {
		int prerogativeId=0;
		if(prerogativeDto != null){
			prerogativeDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
			if(prerogativeDto.getPrerogativeId() != null){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("prerogativeId", prerogativeDto.getPrerogativeId());
				PrerogativeDto prerogativeDto1 = levelDao.getPrerogativeDetail(map);
				CommonValidUtil.validObjectNull(prerogativeDto1, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_PREROGATIVE);
				prerogativeId= prerogativeDto.getPrerogativeId();
				levelDao.updatePrerogativeInfo(prerogativeDto);
			}else{
				prerogativeId = levelDao.addPrerogativeInfo(prerogativeDto);
			}
		}
		return prerogativeId;		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.level.ILevelService#addPoint()
	 */
	@Override
	public void addPoint(AddPointDetailModel addPointDetailModel,Long operaterId,String operaterName) throws Exception {
		
		Integer bizType = addPointDetailModel.getBizType();
		
		//业务类型:店铺类型=1,会员类型=2
		if(bizType==1){
			
	        ShopDto shopDB = shopDao.getShopById(addPointDetailModel.getBizId().longValue());
			if (null == shopDB) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_MISS_SHOP);
			}
			
			//导入商铺特殊处理
			if(addPointDetailModel.getRuleType()==3){
				deailAddGoodsByFrist(shopDB,addPointDetailModel,operaterId,operaterName);
			}
			else{
				addShopPoint(addPointDetailModel,shopDB, operaterId,operaterName);
			}
		}
		
		
	}
	/**
	 * 处理首次发布商品
	 * @throws Exception 
	 * 
	 */
	public void deailAddGoodsByFrist(ShopDto shopDB,AddPointDetailModel addPointDetailModel,Long operaterId,String operaterName) throws Exception{
		
		PointRuleDto pointRuleDto = new PointRuleDto();
		pointRuleDto.setRuleType(3);//发布商品类3
		pointRuleDto.setSubRuleType(1);//首次发布商品1
		pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
		List<PointRuleDto> pointRuleDtos = levelDao.getPointRuleList(pointRuleDto);
		
        if(CollectionUtils.isEmpty(pointRuleDtos)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "积分规则不存在");
        }
		
		Integer goodsId = validGoods(addPointDetailModel.getPointSourceId());
		PointRuleDto rule = pointRuleDtos.get(0);
		PointDetailDto pointDetailDto = new PointDetailDto();
		pointDetailDto.setPointRuleId(rule.getPointRuleId());
		pointDetailDto.setBizId(goodsId);
		pointDetailDto.setBizType(1);
		
		int count = getPointDetailCount(pointDetailDto);
		
       /*		
                       积分规则类型	积分规则类型代码	积分规则子类型代码
		商家入驻类	1	1=店铺入驻
		绑定类	2	1=服务号，2=订阅号
		发布商品类	3	1=首次发布商品
		推荐类	4	1=推荐店铺，2=推荐会员，3=首次推荐会员
		制单类	5	1=制单数量，2=制单金额，3=首次制单收银
		注册类	6	1=注册会员
		评价类	7	1=五星，2=四星，3=三星
		消费类	8	占无
		高级功能升级	9	1=双屏机，2=盒子
		优秀商家代表主讲培训	10	1=主讲培训，2=分享
		商家参与培训出勤次数	11	1=线上，2=线下
		活动参与率	12	1=自主发起活动，2=参与公司活动
		特殊贡献	13	1=给予活动奖品赞助、为公司提出建议并被采纳（包括bug）
		
		*/
		if (count <= 0) {
	        Integer pointValue = rule.getPointValue();
	        Integer shopPoint = shopDB.getShopPoint()==null ? 0 : shopDB.getShopPoint();
			Integer afterPoint =  shopPoint + pointValue;
			
			//更新商铺信息
			afterPoint = updateShopByPoint(addPointDetailModel.getBizId().longValue(), shopDB.getLevelId(), pointValue);
			
			insertPointDetail(addPointDetailModel.getBizId(), addPointDetailModel.getBizType(), 
					rule.getPointValue(), afterPoint, 
					rule.getRuleName(), rule.getPointRuleId(), 
					addPointDetailModel.getPointSourceType(), 
					addPointDetailModel.getPointSourceId(),
					operaterId,operaterName,addPointDetailModel.getRemark());
		}
	}
	public Integer validGoods(String pointSourceId) throws Exception{
		Integer goodsId = 0;
		//校验goods
        CommonValidUtil.validObjectNull(pointSourceId, CodeConst.CODE_PARAMETER_NOT_NULL, "pointSourceId不能为空");
        goodsId  = Integer.valueOf(pointSourceId);
		GoodsDto goodsDtoInfo = goodsDao.getGoodsById(goodsId.longValue());
		CommonValidUtil.validObjectNull(goodsDtoInfo, CodeConst.CODE_PARAMETER_NOT_NULL, "商品ID:"+goodsId+"不存在");
		
		return goodsId;
	}

	public int updatePointRule(PointRuleValidDto pointRuleDto) throws Exception {
		int pointRuleId=0;
		if(pointRuleDto != null){
			if(pointRuleDto.getPointRuleId() != null){
				PointRuleDto pointRuleDto1 = new PointRuleDto();
				pointRuleDto1.setPointRuleId(pointRuleDto.getPointRuleId());
				List<PointRuleDto> PointRuleDtolist = levelDao.getPointRuleList(pointRuleDto1);
				CommonValidUtil.validObjectNull(PointRuleDtolist, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_POINTRULE);
				
				pointRuleId= pointRuleDto.getPointRuleId();
				levelDao.updatePointRule(pointRuleDto);
			}else{
				//同一规则类型，同一任务类型和同一积分子规则的类型
				PointRuleDto pointRuleDto1 = new PointRuleDto();
				pointRuleDto1.setRuleType(pointRuleDto.getRuleType());
				pointRuleDto1.setTaskType(pointRuleDto.getTaskType());
				pointRuleDto1.setSubRuleType(pointRuleDto.getSubRuleType());
				List<PointRuleDto> PointRuleDtolist = levelDao.getPointRuleList(pointRuleDto1);
				if(PointRuleDtolist!=null && PointRuleDtolist.size()>0){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_EXIST_POINTRULE);
				}
				pointRuleId = levelDao.addPointRule(pointRuleDto);
			}
		}
		return pointRuleId;
		
	}
	
   public void addShopPoint(AddPointDetailModel addPointDetailModel,ShopDto shopDB,Long operaterId,String operaterName) throws Exception {
		
		/**
		 * 1、查询规则 
		 * 2、记录积分流水 
		 * 3、增加店铺积分、等级 
		 */
		
	   if (addPointDetailModel.getRuleType() == 1 && addPointDetailModel.getSubRuleType() == 1) {
			PointRuleDto pointRuleDto = new PointRuleDto();
			pointRuleDto.setRuleType(1);
			pointRuleDto.setSubRuleType(1);
			pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
			List<PointRuleDto> ruleList = levelDao.getPointRuleList(pointRuleDto);
			
			if (ruleList == null ||ruleList.size() == 0) {
				logger.info("找不到店铺入驻积分规则");
				return;
			}
			
			PointRuleDto rule = ruleList.get(0);
			PointDetailDto pointDetailDto = new PointDetailDto();
			pointDetailDto.setPointRuleId(rule.getPointRuleId());
			pointDetailDto.setBizId(addPointDetailModel.getBizId());
			pointDetailDto.setBizType(addPointDetailModel.getBizType());
			pointDetailDto.setPointSourceType(addPointDetailModel.getPointSourceType());
			pointDetailDto.setPointSourceId(addPointDetailModel.getPointSourceId());
			int count = getPointDetailCount(pointDetailDto);
			
			if (count > 0) {
				return;
			}
	   }
	   
	   if (addPointDetailModel.getRuleType() == 4 && addPointDetailModel.getSubRuleType() == 1) {
			PointRuleDto pointRuleDto = new PointRuleDto();
			//发布商品类3、首次发布商品1
			pointRuleDto.setRuleType(4);
			pointRuleDto.setSubRuleType(1);
			pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
			List<PointRuleDto> ruleList = levelDao.getPointRuleList(pointRuleDto);
			
			if (ruleList == null ||ruleList.size() == 0) {
				logger.info("找不到首次推荐店铺积分规则");
				return;
			}
			
			if (addPointDetailModel.getPointSourceId() == null) {
				logger.info("推荐店铺id为空，不予加分  ruleType:{} subRuleType:{} bizId:{}",4,1,addPointDetailModel.getBizId());
				return;
			}
			
			Long recommendShopId = Long.valueOf(addPointDetailModel.getPointSourceId());
			PointRuleDto rule = ruleList.get(0);
			PointDetailDto pointDetailDto = new PointDetailDto();
			pointDetailDto.setPointRuleId(rule.getPointRuleId());
			pointDetailDto.setBizId(addPointDetailModel.getBizId());
			pointDetailDto.setBizType(addPointDetailModel.getBizType());
			pointDetailDto.setPointSourceType(addPointDetailModel.getPointSourceType());
			pointDetailDto.setPointSourceId(recommendShopId.toString());
			int count = getPointDetailCount(pointDetailDto);
			
			if (count > 0) {
				return;
			}
			
			ShopDto recommendShop = shopDao.getShopById(recommendShopId);
			if (shopDB.getPrincipalId() != null && shopDB.getPrincipalId().equals(recommendShop.getPrincipalId())) {
				logger.info("推荐自己的店铺不予加分，加分店铺id:{} 推荐店铺id:{} 店铺负责人uerId:{}",shopDB.getShopId(),recommendShopId,shopDB.getPrincipalId());
				return;
			}
	   }
	   
	   if (addPointDetailModel.getRuleType() == 14) {
		   
		    PointRuleDto pointRuleDto = new PointRuleDto();
			pointRuleDto.setRuleType(addPointDetailModel.getRuleType());
			pointRuleDto.setSubRuleType(addPointDetailModel.getSubRuleType());
			pointRuleDto.setIsDelete(CommonConst.IS_DELETE_FALSE);
			List<PointRuleDto> ruleList = levelDao.getPointRuleList(pointRuleDto);
			
			if (ruleList == null ||ruleList.size() == 0) {
				logger.info("找不到分享类积分规则");
				return;
			}
			
			PointRuleDto rule = ruleList.get(0);
			
			PointDetailDto searchDto = new PointDetailDto();
			searchDto.setBizType(1);
			searchDto.setBizId(shopDB.getShopId().intValue());
			searchDto.setPointRuleId(rule.getPointRuleId());
			
			Date now = new Date();
			Calendar cal = Calendar.getInstance();  
			cal.setTime(now);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date startTime = cal.getTime();
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
			Date endTime = cal.getTime();
			
			searchDto.setStartTime(startTime);
			searchDto.setEndTime(endTime);
			
			int getPointValuePerDay = getPointDetailValueSum(searchDto);
			if (getPointValuePerDay >= 1) {
				logger.info("当日分享累计积分达到限制   subRuleType:{}",addPointDetailModel.getSubRuleType());
				return;
			}
	   }
		//查询规则 
		PointRuleDto pointRuleDto = new PointRuleDto();
		pointRuleDto.setRuleType(addPointDetailModel.getRuleType());
		pointRuleDto.setSubRuleType(addPointDetailModel.getSubRuleType());
		List<PointRuleDto>  pointRuleDtos = levelDao.getPointRuleList(pointRuleDto);
        if(CollectionUtils.isEmpty(pointRuleDtos)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "积分规则不存在");
        }
        PointRuleDto pointRule = pointRuleDtos.get(0);
        Integer pointValue = pointRule.getPointValue();
		
		//更新商铺信息
        Integer shopPoint = shopDB.getShopPoint()==null ? 0 : shopDB.getShopPoint();
		Integer afterPoint =  shopPoint + pointValue;
		afterPoint = updateShopByPoint(addPointDetailModel.getBizId().longValue(),shopDB.getLevelId(), pointValue);
		
		//记录流水
        insertPointDetail(addPointDetailModel.getBizId(), addPointDetailModel.getBizType(), pointValue, afterPoint, 
        		pointRule.getRuleName(), pointRule.getPointRuleId(), addPointDetailModel.getPointSourceType(), 
        		addPointDetailModel.getPointSourceId(),operaterId,operaterName,addPointDetailModel.getRemark());
		
	}
	/**
	 * 获取等级id
	 * 
	 */
    public Integer getlevel(Integer levelId,Integer afterPoint) throws Exception{
    	
    	LevelDto nextLevel = getNextLevel(levelId);
    	if (nextLevel == null) {
    		return levelId;
    	}
    	
    	Integer levelCondition  = nextLevel.getLevelCondition();
    	//达到下一等级积分下限，升级！！！
    	if(afterPoint>=levelCondition){
    		levelId = nextLevel.getLevelId();
    	}
    	
    	return levelId;
    }
    
    private LevelDto getNextLevel(Integer levelId) {
    	Map<String, Object> parms = new HashMap<String, Object>();
    	
    	parms.put("levelType", 1);//店铺等级类型
    	parms.put("isDelete", 0);//'是否删除：1=是，0=否',
    	List<LevelDto>  levelDtos = levelDao.getLevelList(parms);
    	
    	//获取下一等级
    	Boolean isNext = false;
    	LevelDto nextLevel = null;
    	for (LevelDto level : levelDtos) {
    		if (isNext == true) {
    			nextLevel = level;
    			break;
    		}
    		
    		if (level.getLevelId().equals(levelId)) {
    			isNext = true;
    		}
    	}
    	
    	return nextLevel;
    }
    
    @Override
	public int updateShopByPoint(Long shopId,Integer levelId,Integer addPoint) throws Exception{
		
		ShopBean shopBean = new ShopBean();
		if (addPoint < 0) {
			Integer shopPoint = shopDao.getShopPoint(shopId);
			if ((shopPoint+addPoint) < 0) {
				addPoint = -shopPoint;
			}
		}
		shopBean.setShopPoint(addPoint);
		shopBean.setShopId(shopId);
		shopDao.updateShopPoint(shopBean);
		
		int afterPoint = shopDao.getShopPoint(shopId);
		Integer afterLevelId = getlevel(levelId, afterPoint);
		if (afterLevelId != levelId) {
			shopBean.setLevelId(afterLevelId);
			shopBean.setShopPoint(null);
			shopDao.updateShopPoint(shopBean);
		}
		
		DataCacheApi.del(CommonConst.KEY_SHOP + shopBean.getShopId());
		return afterPoint;
	}
	/**
	 * 插入积分流水
	 * 
	 * @Function: com.idcq.appserver.service.level.Impl.LevelServiceImpl.updateShopByPoint
	 * @Description:
	 *
	 * @param bizId 业务id
	 * @param bizType '业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,
	 *        银行=7,商品=8,商品族=9,技师=10,商品分类=11,launcher主页图标=12,商圈活动=13,收银机日志=14,
	 *        商圈活动类型=15,消息中心消息=16,出入库记录=17,盘点记录=18,栏目=19',
	 * @param pointValue 积分
	 * @param afterPoint  处理后积分值
	 * @param pointDetailTitle  积分标题
	 * @param pointRuleId  积分规则id
	 * @param idpointSourceType  积分来源类型
	 * @param pointSourceId  积分来源id
	 * @throws Exception
	 */
	@Override
	public void insertPointDetail(Integer bizId,Integer bizType,Integer pointValue,Integer afterPoint,
			String pointDetailTitle,Integer pointRuleId,Integer pointSourceType,String pointSourceId,Long operaterId,String operaterName,String remark) throws Exception{
		
        PointDetailDto pointDetailDto = new PointDetailDto();
        pointDetailDto.setBizId(bizId);
        pointDetailDto.setBizType(bizType);
        pointDetailDto.setCreateTime(new Date());
        pointDetailDto.setPointValue(pointValue);
        pointDetailDto.setPointAferValue(afterPoint);
        pointDetailDto.setPointDetailTitle(pointDetailTitle);
        pointDetailDto.setPointRuleId(pointRuleId);
        pointDetailDto.setPointSourceType(pointSourceType);
        pointDetailDto.setPointSourceId(pointSourceId);
        pointDetailDto.setOperaterId(operaterId);
        pointDetailDto.setOperaterName(operaterName);
        pointDetailDto.setRemark(remark);
        levelDao.insertPointDetail(pointDetailDto);
	}
	
	@Override
	public int getPointDetailCount(PointDetailDto pointDetailDto){
		return levelDao.getPointDetailCount(pointDetailDto);
	}
	
	@Override
	public int getPointDetailValueSum(PointDetailDto pointDetailDto) {
		return levelDao.getPointDetailValueSum(pointDetailDto);
	}
	@Override
	public void pushAddPointMessage(Integer ruleType,Integer subRuleType, 
									Integer pointTargetType,Integer pointTargetId,
									Integer pointSourceType,String pointSourceId,Boolean asyn) {
		
		if (ruleType == null || pointTargetType == null || pointTargetId == null) {
			logger.info("messageModel缺少关键信息   ruleType:"+ruleType+
											"pointTargetType:"+pointTargetType+
											"pointTargetId"+pointTargetId);
			return;
		}
		
		String topic = "calculatePointMessage";
		String tag = "calculatePoint";
		
		CalculatePointMessageModel messageModel = new CalculatePointMessageModel();
		messageModel.setRuleType(ruleType);
		messageModel.setSubRuleType(subRuleType);
		messageModel.setPointTargetType(pointTargetType);
		messageModel.setPointTargetId(pointTargetId);
		messageModel.setPointSourceType(pointSourceType);
		messageModel.setPointSourceId(pointSourceId);
		
		asyn = asyn == null ? true : asyn;
		try {
			String messageBody = JSON.toJSONString(messageModel);
			logger.info("推送异步积分消息内容："+messageBody);
			if (asyn) {
				MqProduceApi.setMessage(topic, tag, messageBody);
			} else {
			    final IPointProcessor pointProcessor = (IPointProcessor)BeanFactory.getBean("pointRuleType:"+
						messageModel.getRuleType());
			    
				if (pointProcessor == null) {
					logger.info("积分规则处理器不存在---pointRuleType"+messageModel.getRuleType());
					return;
				}
		
				pointProcessor.processPoint(messageModel);
			}
			
		} catch (Exception e) {
			logger.info("推送异步积分消息失败", e);
		}
		
	}
}
