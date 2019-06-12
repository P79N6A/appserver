package com.idcq.appserver.service.busArea.busAreaActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.VelocityUtils;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActShopJoinTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActShopTypeEnum;
import com.idcq.appserver.common.enums.BusAreaActStatusEnum;
import com.idcq.appserver.common.enums.BusAreaActUserSourceTypeEnum;
import com.idcq.appserver.common.enums.ShopMbStatusEnum;
import com.idcq.appserver.common.enums.ShopTypeEnum;
import com.idcq.appserver.common.enums.UserIsMemberEnum;
import com.idcq.appserver.dao.activity.IActivityModelConfigDao;
import com.idcq.appserver.dao.activity.IBusinessAreaActivityDao;
import com.idcq.appserver.dao.activity.IBusinessAreaConfigDao;
import com.idcq.appserver.dao.activity.IBusinessAreaModelDao;
import com.idcq.appserver.dao.activity.IBusinessAreaShopDao;
import com.idcq.appserver.dao.activity.IBusinessAreaStatDao;
import com.idcq.appserver.dao.activity.IBusinessAreaUserDao;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.region.ICitiesDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dto.activity.ActivityModelConfigDto;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.activity.BusinessAreaConfigDto;
import com.idcq.appserver.dto.activity.BusinessAreaModelDto;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;
import com.idcq.appserver.dto.activity.BusinessAreaStatDto;
import com.idcq.appserver.dto.activity.BusinessAreaUserDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
//import com.idcq.appserver.service.collect.CollectServiceImpl;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.ProgramUtils;


@Service
public class BusAreaActivityServiceImpl implements IBusAreaActivityService{

	private Log logger = LogFactory.getLog(BusAreaActivityServiceImpl.class);
	
	@Autowired
	private IBusinessAreaActivityDao businessAreaActivityDao;
	@Autowired
	private IActivityModelConfigDao activityModelConfigDaoImpl;
	@Autowired
	private IBusinessAreaConfigDao businessAreaConfigDao;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IMemberServcie memberServcie;
	@Autowired
	public ICitiesDao citiesDao;
	@Autowired
	public IShopMemberDao shopMemberDao;
	@Autowired
	public IBusinessAreaUserDao businessAreaUserDao;
	@Autowired
	private IBusinessAreaShopDao businessAreaShopDao;
	@Autowired
	private IAttachmentDao attachmentDao;
	@Autowired
	private IBusinessAreaStatDao businessAreaStatDao;
	@Autowired
	private IBusinessAreaModelDao businessAreaModelDao;
	@Autowired
	private ICommonDao commonDao;
	@Autowired
	private IShopDao shopDao;
	
	@Override
	public List<Map> getDynamicConfig(Integer modelId) throws Exception {
		ActivityModelConfigDto activityModelConfigDto =new ActivityModelConfigDto();
		activityModelConfigDto.setModelId(modelId);
		return activityModelConfigDaoImpl.getActivityModelConfigListByModelId(activityModelConfigDto);
		
	}

	@Override
	public List<Map> getBusAreaConfig(Integer businessAreaActivityId) {
		BusinessAreaConfigDto businessAreaConfigDto=new BusinessAreaConfigDto(); 
		businessAreaConfigDto.setBusinessAreaActivityId(Long.valueOf(businessAreaActivityId));
		return businessAreaConfigDao.getBusinessAreaConfigByActivityId(businessAreaConfigDto);
	}

	@Override
	public void operateActivity(Map<String, Object> requestMap) throws Exception {
		Integer operateType = Integer.valueOf(requestMap.get("operateType").toString());
		ProgramUtils.executeBeanByProgramConfigCode("operateProcessor_13_"+operateType, 1, requestMap);
	}
	
	@Override
	public PageModel getBusAreaActivityList(Map<String, Object> map) throws Exception {
		PageModel pageModel = new PageModel();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		int count = 0;
		if (map.get("shopId") != null) {
			Long shopId = Long.valueOf(map.get("shopId").toString());
			count = businessAreaActivityDao.getBusAreaActivityListWithShopIdCount(map);
			if(count > 0) {
			    resultList = businessAreaActivityDao.getBusAreaActivityListWithShopId(map);
			    for (Map<String, Object> result : resultList) {
	                Long queryShopId = Long.valueOf(result.get("shopId").toString());
	                Long businessAreaActivityId = Long.valueOf(result.get("businessAreaActivityId").toString());
	                BusinessAreaShopDto shop = businessAreaShopDao.getBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
	                if (!shopId.equals(queryShopId) && shop == null) {
	                    result.put("shopType", 0);
	                }
	                
	                if (!shopId.equals(queryShopId) && shop != null) {
	                    result.put("shopType", 2);
	                }
	            }
			}
		} else {
		    count = businessAreaActivityDao.getBusAreaActivityListWithOutShopIdCount(map);
		    if(count > 0) {
		        resultList = businessAreaActivityDao.getBusAreaActivityListWithOutShopId(map);
		    }
		}
		setActPosterUrlsToResult(resultList);
		pageModel.setList(resultList);
		pageModel.setTotalItem(count);
		return pageModel;
	}
	
	private void setActPosterUrlsToResult(List<Map<String, Object>> resultList) throws Exception{
		if (resultList == null || resultList.size() == 0) {
			return;
		}
		AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
		attachmentRelationDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());
		List<Long> activityIdList = new LinkedList<Long>();
		for (Map<String, Object> result : resultList) {
			Long activityId = Long.valueOf(result.get("businessAreaActivityId").toString());
			activityIdList.add(activityId);
		}
		List<AttachmentRelationDto> attachmentRelationDtoList = attachmentRelationDao.findByConditionIn(attachmentRelationDto, activityIdList);
		
		Map<Long, String> posterUrlIndexMap = new HashMap<Long, String>();
		
		for (AttachmentRelationDto att : attachmentRelationDtoList) {
			String url = posterUrlIndexMap.get(att.getBizId());
			if (url == null) {
				posterUrlIndexMap.put(att.getBizId(), FdfsUtil.getFileProxyPath(att.getFileUrl()));
			}else {
				posterUrlIndexMap.put(att.getBizId(), url+","+FdfsUtil.getFileProxyPath(att.getFileUrl()));
			}
		}
		
		for (Map<String, Object> result : resultList) {
			Long activityId = Long.valueOf(result.get("businessAreaActivityId").toString());
			result.put("actPosterUrls", posterUrlIndexMap.get(activityId));
		}
	}
	
	@Override
	public PageModel getActivityList(Map<String, Object> map) throws Exception {
		PageModel pageModel=new PageModel();
		 Integer count=0;
		 String shopId=null;
		 int n=1;
		if(map.get("shopId")!=null){
			n=0;
			shopId = map.get("shopId").toString();
			if(shopId!=null){//根据商铺ID查询该商铺是否存在
				n = shopDao.getCountByShopId(Long.valueOf(shopId));
				
			}
		}
		String shopType=null;
		BusinessAreaShopDto businessAreaShopDto=new BusinessAreaShopDto();
		//Todo 城市过滤 状态过滤
		if(map.get("shopType")!=null){
			shopType=map.get("shopType")+"";
		}
		if(shopType==null){//查询全部店铺
			BusinessAreaActivityDto activityDto=new BusinessAreaActivityDto();
			if(!StringUtils.isEmpty(shopId)){
				ShopDto shopDto=shopDao.getShopById(Long.parseLong(shopId));
				if(shopDto!=null){
					activityDto.setCityId(shopDto.getCityId());
				}
			}
			if(map.get("activityStatus")!=null){
				activityDto.setActivityStatus(Integer.parseInt(map.get("activityStatus")+""));
			}
			List<BusinessAreaActivityDto>buesinessAreaActivityList=businessAreaActivityDao.getBusinessAreaActivityList(activityDto, Integer.parseInt(map.get("pageNo")+""), Integer.parseInt(map.get("pageSize")+""));
			pageModel.setTotalItem(businessAreaActivityDao.getBusinessAreaActivityCount(activityDto));
			if(buesinessAreaActivityList.size()>0){
				Map<Long,BusinessAreaActivityDto>activityMap=parseActivityToMap(buesinessAreaActivityList);
				List<Long>activityIdList=parseActivityToIdList(buesinessAreaActivityList);
				AttachmentRelationDto attachmentRelationDto=new AttachmentRelationDto();
				attachmentRelationDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());
				if(!StringUtils.isEmpty(shopId)){//店铺id不为空
					BusinessAreaShopDto areaShopDto=new BusinessAreaShopDto();
					areaShopDto.setShopId(Long.parseLong(shopId));
					areaShopDto.setActivityIdList(activityIdList);
					List<BusinessAreaShopDto>businessAreaShopList=businessAreaShopDao.getBusinessAreaShopList(areaShopDto, Integer.parseInt(map.get("pageNo")+""), 100000);
					parseDataFormShopToActivity(businessAreaShopList, activityMap);
				}
				//附件列表
				List<AttachmentRelationDto>attachmentRelationDtoList=attachmentRelationDao.findByConditionIn(attachmentRelationDto, activityIdList);
				parseActivityPoster(activityMap, attachmentRelationDtoList);//封装商圈的海报
				
			}
			pageModel.setList(buesinessAreaActivityList);
		}else if(ShopTypeEnum.START.getValue().equals(shopType)||ShopTypeEnum.JOIN.getValue().equals(shopType)){//我发起的店铺
			businessAreaShopDto.setShopType(Integer.parseInt(shopType));
			businessAreaShopDto.setShopId(Long.parseLong(shopId));
			if(!StringUtils.isEmpty(shopId)){
				ShopDto shopDto=shopDao.getShopById(Long.parseLong(shopId));
				if(shopDto!=null){
					businessAreaShopDto.setCityId(shopDto.getCityId());
				}
			}
			if(map.get("activityStatus")!=null){
				businessAreaShopDto.setActivityStatus(Integer.parseInt(map.get("activityStatus")+""));
			}
			List<BusinessAreaShopDto>shopDtoList=businessAreaShopDao.getBusinessAreaShopList(businessAreaShopDto, Integer.parseInt(map.get("pageNo")+""), Integer.parseInt(map.get("pageSize")+""));
			Integer totalCount=businessAreaShopDao.getBusinessAreaShopCount(businessAreaShopDto);
			pageModel.setTotalItem(totalCount);
			if(shopDtoList.size()>0){
				Map<Long,BusinessAreaShopDto>businessAreaShopMap=parseActivityShopToMap(shopDtoList);//转换为map
				List<Long>activityIdList=parseActivityShopToIdList(shopDtoList);//获取活动的id列表
				//通过id列表找到活动
				List<BusinessAreaActivityDto>businessAreaActivityList=businessAreaActivityDao.getActivityListByActivityIdList(activityIdList);
				Map<Long,BusinessAreaActivityDto>activityMap=parseActivityToMap(businessAreaActivityList);
				parseDataForActivity(businessAreaActivityList,businessAreaShopMap);
				AttachmentRelationDto attachmentRelationDto=new AttachmentRelationDto();
				attachmentRelationDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());
				//附件列表
				List<AttachmentRelationDto>attachmentRelationDtoList=attachmentRelationDao.findByConditionIn(attachmentRelationDto, activityIdList);
				parseActivityPoster(activityMap, attachmentRelationDtoList);
				pageModel.setList(businessAreaActivityList);
			}
			else{
				pageModel.setList(new ArrayList<BusinessAreaActivityDto>());
			}
		}
		return pageModel;
	}
	
	
	
	public PageModel getActivityList1(Map<String, Object> map, boolean isTokenOrSession) throws Exception {
		 PageModel pageModel=new PageModel();
		 Integer count=0;
		 List<Map> list= new ArrayList<Map>();
		 String shopId=String.valueOf(map.get("shopId")==null?"":map.get("shopId"));
		 String shopType=String.valueOf(map.get("shopType")==null?"":map.get("shopType"));
		 if(!shopId.isEmpty()){//shoId存在
			//查询与我店铺相关的活动（我参与、我发布的、我未参与的）
			if(isTokenOrSession){//收银机等请求service不鉴权,不查询出我未参与的
				count=businessAreaActivityDao.getActivitiesNoShopTypeCount(map);
				if(count!=0){
					list=businessAreaActivityDao.getActivitiesNoShopType(map);
				}
			}else{//查询我参与、我发布、我未参与的
				if(!shopType.isEmpty()){
					count=businessAreaActivityDao.getActivitiesNoShopTypeCount(map);
					if(count!=0){
						list=businessAreaActivityDao.getActivitiesNoShopType(map);
					}
				}else{
					count=businessAreaActivityDao.getActivitiesAppCount(map);
					if(count!=0){
						list=businessAreaActivityDao.getActivitiesApp(map);
					}
				}
				
			}
			
		}else{
			//shopId不存在，说明是查询所有，所有活动的shop_type都为1
			count=businessAreaActivityDao.getActivitiesNoShopIdCount(map);
			list=businessAreaActivityDao.getActivitiesNoShopId(map);
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(list!=null && list.size()!=0){
			for (Map map1 : list) {
				String url = String.valueOf(map1.get("actPosterUrls"));
				if(!StringUtils.isBlank(url)&& !url.equals("") && !url.equals("null")){
					map1.put("actPosterUrls", FdfsUtil.getFileProxyPath(url));
				}else{
					map1.put("actPosterUrls","");
				}
				resultList.add(map1);
			}
		}
		pageModel.setTotalItem(count);
		pageModel.setList(resultList);
		return pageModel;
	}
	
	/**
	 * 将活动店铺解析成map
	 * @Title: parseActivityShopToMap 
	 * @param @param businessAreaShopDtoList
	 * @return void    返回类型 
	 * @throws
	 */
	private Map<Long,BusinessAreaShopDto> parseActivityShopToMap(List<BusinessAreaShopDto>businessAreaShopDtoList){
		Map<Long,BusinessAreaShopDto>businessAreaShopMap=new HashMap<Long,BusinessAreaShopDto>();
		for(BusinessAreaShopDto businessAreaShopDto:businessAreaShopDtoList){
			businessAreaShopMap.put(businessAreaShopDto.getBusinessAreaActivityId(), businessAreaShopDto);
		}
		return businessAreaShopMap;
	}
	
	/**
	 * 将商圈列表转换为map
	 * @Title: parseActivityToMap 
	 * @param @param businessAreaActivityList
	 * @param @return
	 * @return Map<Long,BusinessAreaActivityDto>    返回类型 
	 * @throws
	 */
	private Map<Long,BusinessAreaActivityDto>parseActivityToMap(List<BusinessAreaActivityDto>businessAreaActivityList){
		Map<Long,BusinessAreaActivityDto>businessAreaActivityMap=new HashMap<Long,BusinessAreaActivityDto>();
		for(BusinessAreaActivityDto businessAreaActivityDto:businessAreaActivityList){
			businessAreaActivityMap.put(businessAreaActivityDto.getBusinessAreaActivityId(), businessAreaActivityDto);
		}
		return businessAreaActivityMap;
	}
	
	/**
	 * 根据活动店铺列表获取活动idList
	 * @Title: parseActivityShopToIdList 
	 * @param @param businessAreaShopDtoList
	 * @param @return
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	private List<Long>parseActivityShopToIdList(List<BusinessAreaShopDto>businessAreaShopDtoList){
		List<Long>activityIdList=new ArrayList<Long>();
		for(BusinessAreaShopDto areaShopDto:businessAreaShopDtoList){
			activityIdList.add(areaShopDto.getBusinessAreaActivityId());
		}
		return activityIdList;
	}
	
	/**
	 * 获取商圈活动id列表
	 * @Title: parseActivityToIdList 
	 * @param @param businessAreaActivityDto
	 * @param @return
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	private List<Long>parseActivityToIdList(List<BusinessAreaActivityDto>businessAreaActivityDto){
		List<Long>activityIdList=new ArrayList<Long>();
		for(BusinessAreaActivityDto areaActivityDto:businessAreaActivityDto){
			activityIdList.add(areaActivityDto.getBusinessAreaActivityId());
		}
		return activityIdList;
	}
	
	/**
	 * 封装商圈活动中的数据
	 * @Title: parseDataForActivity 
	 * @param @param businessAreaActivityList
	 * @param @param businessAreaShopMap
	 * @return void    返回类型 
	 * @throws
	 */
	private void parseDataForActivity(List<BusinessAreaActivityDto>businessAreaActivityList,Map<Long,BusinessAreaShopDto>businessAreaShopMap){
		for(BusinessAreaActivityDto businessAreaActivityDto:businessAreaActivityList){
			BusinessAreaShopDto businessAreaShopDto=businessAreaShopMap.get(businessAreaActivityDto.getBusinessAreaActivityId());//商圈活动相关店铺
			if(businessAreaShopDto!=null){
				businessAreaActivityDto.setShopType(businessAreaShopDto.getShopType());
				businessAreaActivityDto.setShopId(businessAreaShopDto.getShopId());
			}
		}
	}
	
	/**
	 * 封装商圈活动中的数据
	 * @Title: parseDataForActivity 
	 * @param @param businessAreaActivityList
	 * @param @param businessAreaShopMap
	 * @return void    返回类型 
	 * @throws
	 */
	private void parseDataFormShopToActivity(List<BusinessAreaShopDto>businessAreaShopList,Map<Long,BusinessAreaActivityDto>businessAreaActivityMap){
		for(BusinessAreaShopDto businessAreaShopDto:businessAreaShopList){
			BusinessAreaActivityDto businessAreaActivityDto=businessAreaActivityMap.get(businessAreaShopDto.getBusinessAreaActivityId());//商圈活动相关店铺
			if(businessAreaActivityDto!=null){
				businessAreaActivityDto.setShopType(businessAreaShopDto.getShopType());
				businessAreaActivityDto.setShopId(businessAreaShopDto.getShopId());
			}
		}
	}
	
	/**
	 * 解析活动海报
	 * @Title: parseActivityPoster 
	 * @param @param activityList
	 * @param @param attachmentRelationList
	 * @return void    返回类型 
	 * @throws
	 */
	private void parseActivityPoster(Map<Long,BusinessAreaActivityDto>activityMap,List<AttachmentRelationDto>attachmentRelationList){
		for(AttachmentRelationDto attachmentRelationDto:attachmentRelationList){//将同一商圈活动的海报路径合并起来
			try{
				BusinessAreaActivityDto activityDto=activityMap.get(attachmentRelationDto.getBizId());
				if(activityDto!=null){
					if(!StringUtils.isEmpty(activityDto.getActPosterUrls())){
						activityDto.setActPosterUrls(activityDto.getActPosterUrls()+","+FdfsUtil.getFileProxyPath(attachmentRelationDto.getFileUrl()));
					}else{
						activityDto.setActPosterUrls(FdfsUtil.getFileProxyPath(attachmentRelationDto.getFileUrl()));
					}
				}
			}catch(Exception e){
				
			}
		}
	}

	public Long updateActivity(BusinessAreaActivityDto businessAreaActivityDto)
			throws Exception {
		/**
		 * 步骤二：保存商圈活动信息
		 * 
		 * 1.如果发布活动与已存在活动时间上存在交叉（报名时间交叉、活动时间交叉），是否可以发布新的活动(服务端先不校验)
		 * 2.已经存在则修改，否则新增
		 * 3.如果活动已经存在，那么状态只有为未发布的活动才允许修改
		 * 4.将活动类型模板数据复制到活动表中
		 * 5.操作表：1dcq_business_area_activity
		 * 
		 */
		Long businessAreaActivityId = businessAreaActivityDto.getBusinessAreaActivityId();
		boolean updateFlag = false;
		if (null != businessAreaActivityId) {
			BusinessAreaActivityDto dbActivityDto = businessAreaActivityDao.getBusinessAreaActivityById(businessAreaActivityId);
			if (dbActivityDto != null) {
				Integer activityStatus = dbActivityDto.getActivityStatus();
				//只有未发布的活动才允许修改
				if (activityStatus != 0) {
					if(activityStatus==1){
						Map<String,Object> map=new HashMap<String, Object>();
						map.put("businessAreaActivityId", dbActivityDto.getBusinessAreaActivityId());
						map.put("shopType",2);
						int n = businessAreaActivityDao.getActivityShopListCount(map);
						if(n>0){
							throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该活动有商家参与"+"，不支持修改");
						}
					}else{
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "活动"+BusAreaActStatusEnum.getNameByValue(activityStatus)+"，不支持修改");
					}
				}
				
				updateFlag = true;
			}
		}
		BusinessAreaModelDto businessAreaModelDto = businessAreaModelDao.getBusinessAreaModelById(businessAreaActivityDto.getModelId());
		if (businessAreaModelDto == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_PBA2_12);
		}
		//将活动类型模板copy至活动表中
		//businessAreaActivityDto.setActDesc(businessAreaModelDto.getActivityDescModel());
		businessAreaActivityDto.setActivityProtocol(businessAreaModelDto.getActivityProtocolModel());
		businessAreaActivityDto.setActivityShareTitle(businessAreaModelDto.getActivityShareTitleModel());
		businessAreaActivityDto.setActivityRuleModle(businessAreaModelDto.getActivityRuleModle());
		
		if (updateFlag) {
			//修改
			businessAreaConfigDao.delBusinessAreaConfigByActivityId(businessAreaActivityId);
			//删除
		}else{
			businessAreaActivityDto.setActNum(1);
			businessAreaActivityDao.addBusinessAreaActivity(businessAreaActivityDto);
		}
		
		/**
		 * 步骤四：保存商圈活动配置信息
		 * 
		 * 1.操作表：1dcq_business_area_config
		 * 
		 */
		List<BusinessAreaConfigDto> rulesDtos = businessAreaActivityDto.getActivityRules();
		Map<Integer, Map<String, String>> configByGroup = new HashMap<Integer, Map<String,String>>();
	
		//TODO 生成activityRuleName
		for(BusinessAreaConfigDto configDto : rulesDtos){
			configDto.setBusinessAreaActivityId(businessAreaActivityDto.getBusinessAreaActivityId());
			
			Map<String, String> config = configByGroup.get(configDto.getGroupId());
			if (config == null) {
				config = new HashMap<String, String>();
				configByGroup.put(configDto.getGroupId(), config);
			}
			
			if (configDto.getConfigCode().equals("over")) {
				//该数据来自活动模版配置表
				configDto.setConfigName("满");
				configDto.setConfigDesc("满送红包活动满多少钱配置");
				config.put("overName", configDto.getConfigName());
				config.put("overValue", configDto.getConfigValue());
			}else if (configDto.getConfigCode().equals("give")) {
				//该数据来自活动模版配置表
				configDto.setConfigName("送");
				configDto.setConfigDesc("满送红包活动送多少钱红包配置");
				config.put("giveName", configDto.getConfigName());
				config.put("giveValue", configDto.getConfigValue());
			}
			
			businessAreaConfigDao.addBusinessAreaConfig(configDto);
		}
		
		StringBuilder activityShareTitle = new StringBuilder();
		for (Integer groupId :  configByGroup.keySet()) {
			System.out.println( configByGroup.get(groupId).size());
			if(configByGroup.get(groupId).size()!=0 ){
				Map<String, String> config = configByGroup.get(groupId);
				BusinessAreaConfigDto busConfig = new BusinessAreaConfigDto();
				busConfig.setBusinessAreaActivityId(businessAreaActivityDto.getBusinessAreaActivityId());
				busConfig.setConfigType(2);
				Map<String, String> para = new HashMap<String, String>();
				para.put("over",config.get("overName"));
				para.put("overValues", config.get("overValue"));
				para.put("give", config.get("giveName"));
				para.put("giveValues", config.get("giveValue"));
				para.put("gift", "红包");
				busConfig.setConfigValue(VelocityUtils.getValueByTemplate(businessAreaModelDto.getActivityRuleModle(), para));
				busConfig.setConfigCode("configName");
				busConfig.setConfigName("配置名称");
				busConfig.setConfigDesc("配置规则名称详细说明-自动生成");
				busConfig.setGroupId(groupId);
				businessAreaConfigDao.addBusinessAreaConfig(busConfig);
				VelocityUtils.setShareTitleByTemplate(activityShareTitle, businessAreaModelDto.getActivityShareTitleModel(),
						businessAreaActivityDto.getShopName(),config.get("overValue"), config.get("giveValue"));
			}
		}
		
		businessAreaActivityDto.setActivityRuleName(businessAreaConfigDao.getbusAreaConfigNameByActivityId(businessAreaActivityDto.getBusinessAreaActivityId()));
		businessAreaActivityDto.setActivityShareTitle(activityShareTitle.toString().substring(1));
		//保存时是否发布，默认发布
		Integer isRelease = businessAreaActivityDto.getIsRelease();
		if (isRelease == null) {
			isRelease = 1;
		}
		businessAreaActivityDto.setActivityStatus(isRelease);
		//TODO 生成activityRuleName
		businessAreaActivityDao.updateBusinessAreaActivityById(businessAreaActivityDto);
			
		businessAreaActivityId = businessAreaActivityDto.getBusinessAreaActivityId();
		if (businessAreaActivityId == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "保存商圈活动信息失败");
		}

		/**
		 * 步骤三：建立附件关联关系
		 * 
		 * 1.操作表：1dcq_attachment_relation
		 * 
		 */
		String actPosterIds = businessAreaActivityDto.getActPosterIds();
		if (!StringUtils.isEmpty(actPosterIds)) {
			List<AttachmentRelationDto> attachmentRelationDtos = new ArrayList<AttachmentRelationDto>();
			String[] strs = actPosterIds.split(",");
			for (int i = 0; i < strs.length; i++) {
				Long attachmentId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID, "活动封面附件ID类型错误");
				AttachmentRelationDto attachmentRelationDto = new AttachmentRelationDto();
				attachmentRelationDto.setAttachmentId(attachmentId);
				attachmentRelationDto.setBizId(businessAreaActivityId);
				attachmentRelationDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());
				attachmentRelationDto.setPicType(1);//缩略图
				attachmentRelationDto.setBizIndex(i);
				attachmentRelationDtos.add(attachmentRelationDto);
			}
			if (updateFlag) {
				//如果是修改操作，且当前从新上传了附件ID，则先删除之前的附件关联关系
				attachmentRelationDao.delAttachmentRelationByCondition(businessAreaActivityId, BizTypeEnum.BUSAREA_ACTIVITY.getValue(), 1);
			}
			if (attachmentRelationDtos.size() > 0) {
				attachmentRelationDao.addAttachmentRelationBatch(attachmentRelationDtos);
			}
		}

		

		/**
		 * 步骤五：将发起商铺添加进入商圈活动商铺列表中
		 * 
		 * 1.操作表：1dcq_business_area_shop
		 * 
		 */
		if (!updateFlag) {
			//如果是新增，则添加到商圈活动参与商铺信息表中
			BusinessAreaShopDto businessAreaShopDto = new BusinessAreaShopDto();
			businessAreaShopDto.setBusinessAreaActivityId(businessAreaActivityId);
			businessAreaShopDto.setShopId(businessAreaActivityDto.getShopId());
			businessAreaShopDto.setClientSystemType(businessAreaShopDto.getClientSystemType());
			businessAreaShopDto.setJoinType(BusAreaActShopJoinTypeEnum.MGR_PUSH.getValue());
			businessAreaShopDto.setJoinTime(new Date());
			businessAreaShopDto.setShopType(BusAreaActShopTypeEnum.START_SHOP.getValue());
			businessAreaShopDao.addBusinessAreaShop(businessAreaShopDto);
			
			/**
			 * 步骤六：新增活动统计记录（修改则不理会）
			 * 
			 * 1.新增时同时增加一条统计初始化记录
			 */
			BusinessAreaStatDto businessAreaStatDto = new BusinessAreaStatDto();
			businessAreaStatDto.setBusinessAreaActivityId(businessAreaActivityId);
			businessAreaStatDto.setShopId(businessAreaActivityDto.getShopId());
			businessAreaStatDao.addBusinessAreaStat(businessAreaStatDto);
		}
		
		
		/**
		 * 步骤七：将活动类型短信模板复制一份存为活动短信模板
		 * 
		 * 1.活动类型短信模板（通知用户）：tradingAreaModel_User_模板ID
		 * 2.活动类型短信模板（通知商铺）：tradingAreaModel_Shop_模板ID
		 * 
		 * 3.活动类型短信通知用户模板code规则：tradingAreaActivities_User_活动ID
		 * 4.商圈活动短信通知商铺模板code规则：tradingAreaActivities_Shop_活动ID
		 * 
		 * 5.1跟2是定活动时静态配置的，当发布活动时，需要将1复制一份，以3方式命名存储，将2复制一份，以4方式命名存储
		 */
		List<String> configKeys = new ArrayList<String>();
		configKeys.add(CommonConst.TRADING_AREA_MODEL_SHOP+businessAreaActivityDto.getModelId());
		configKeys.add(CommonConst.TRADING_AREA_MODEL_USER+businessAreaActivityDto.getModelId());
		List<Map> modelValues = commonDao.getConfigValueByKeys(configKeys);
		if (null != modelValues && modelValues.size() > 0) {
			for(Map bean : modelValues){
				String configKey = (String) bean.get("configKey");
				String configValue = (String) bean.get("configValue");
				if (!StringUtils.isEmpty(configKey)) {
					Map<String, String> appConfig = new HashMap<String, String>();
					String activityConfigKey = null;
					if (StringUtils.equals(configKey, CommonConst.TRADING_AREA_MODEL_SHOP+businessAreaActivityDto.getModelId())) {
						activityConfigKey = CommonConst.TRADING_AREA_ACTIVITIES_SHOP+businessAreaActivityId;
					}else if (StringUtils.equals(configKey, CommonConst.TRADING_AREA_MODEL_USER+businessAreaActivityDto.getModelId())) {
						activityConfigKey = CommonConst.TRADING_AREA_ACTIVITIES_USER+businessAreaActivityId;
					}
					commonDao.addConfigValueByKey(configValue,activityConfigKey);
				}
			}
		}
		return businessAreaActivityId;
	}
	
	@Override
	public int applyBusinessArea(long busAreaActId, int userSourceType,
			Long userSourceId, Integer userSourceChannel, String mobile, String veriCode)
			throws Exception {
		int flag = 0;
		String refeUser = null;
		String refeType = null;
		Integer registerType = null;
		
		
		/**
		 * 步骤一：生成活动资格数据
		 * 
		 * 1，先判断是否平台会员；
		 * 2，1结果为是则判断是否已经获取到资格；
		 * 3，2结果为否则获取在商圈活动会员表插入一条记录，为是则直接返回。
		 * 
		 */
		
		if(userSourceType == BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue()){
			refeUser = userSourceId +"";
			refeType = "1";
			registerType = 2;
		}
		
		/*判断平台会员是否存在*/
		UserDto user = memberServcie.getUserByMobile(mobile);
		if(user == null){
			//未注册平台会员，生成待激活用户
//			memberServcie.saveUser(mobile, password, veriCode, refecode, refeUser, refeType, registerType);
			user = memberServcie.saveUser(mobile, null, veriCode, null, refeUser, refeType, registerType);
			memberServcie.updateIsMember(user.getUserId(),UserIsMemberEnum.WAIT_ACTIVE.getValue());
		}
		
		ShopMemberDto shopMemberDto = null;
		Long memberId = null;
		Long shopId = null;
		/*用户来源类型是否商铺*/
		if(userSourceType == BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue()){
			/*判断店内会员是否存在*/
			ShopMemberDto pShopMemberDto = shopMemberDao.getShopMbByMobileAndShopId(mobile, userSourceId, CommonConst.MEMBER_STATUS_DELETE);
			if(pShopMemberDto == null){
				//绑定到店内会员
				shopMemberDto = new ShopMemberDto();
				shopMemberDto.setCreateTime(new Date());
				shopMemberDto.setMemberStatus(ShopMbStatusEnum.NORMAL.getValue());
				shopMemberDto.setUserId(user.getUserId());
				shopMemberDto.setMobile(Long.valueOf(mobile));
				shopMemberDto.setShopId(userSourceId);
				shopMemberDto.setSourceType(userSourceType);
				shopMemberDao.addShopMember(shopMemberDto);
				
			}else{
				shopMemberDto = pShopMemberDto;
			}
			memberId = shopMemberDto.getMemberId();
			shopId = shopMemberDto.getShopId();
		}else{
			throw new ValidateException(CodeConst.CODE_FAILURE, CodeConst.MSG_APPLY_BUSAREA_NOT_SUPORT_TOURIST);
		}
		
		/*判断是否获取到活动资格*/
		BusinessAreaUserDto businessAreaUserDto = businessAreaUserDao.getBusinessAreaUserById(busAreaActId, memberId);
		if(businessAreaUserDto == null){
			//没有资格，生成资格记录
			businessAreaUserDto = new BusinessAreaUserDto();
			businessAreaUserDto.setMemberId(memberId);
			businessAreaUserDto.setBusinessAreaActivityId(busAreaActId);
			businessAreaUserDto.setGetSourceType(userSourceType);
			businessAreaUserDto.setMobile(Long.valueOf(mobile));
			businessAreaUserDto.setShopId(shopId);
			businessAreaUserDto.setUserSourceType(userSourceType);
			businessAreaUserDto.setUserId(user.getUserId());
			businessAreaUserDao.addBusinessAreaUser(businessAreaUserDto);
			flag = 1;
		}
		
		return flag;
	}
	
	
	
    @Override
	public String applyBusinessArea(Map<String,? extends Object> paramsMap)
			throws Exception {
    	
    	String busAreaActIdStr = (String)paramsMap.get("businessAreaActivityId");
		String mobileStr = (String)paramsMap.get("mobile");
		String veriCodeStr = (String)paramsMap.get("veriCode");
		String userSourceTypeStr = (String)paramsMap.get("userSourceType");
		String userSourceIdStr = (String)paramsMap.get("userSourceId");
		String userSourceChannelStr = (String)paramsMap.get("userSourceChannel");
		
		
		/**
		 * 步骤一：接口入参合法性校验
		 */
		
		/*校验活动ID必填及存在性*/
		CommonValidUtil.validStrNull(busAreaActIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BUSAREA_ACT_ID);
		CommonValidUtil.validStrLongFmt(busAreaActIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_BUSAREA_ACT_ID);
		long busAreaActId = Long.valueOf(busAreaActIdStr);
		/*校验手机号码必填及格式合法性*/
		CommonValidUtil.validStrNull(mobileStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_MOBILE);
		CommonValidUtil.validMobileStr(mobileStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_MOBILE);
		/*校验userSourceType是否有值及枚举*/
		int userSourceType;
		if(!StringUtils.isBlank(userSourceTypeStr)){
			CommonValidUtil.validStrIntFmt(userSourceTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_TYPE);
			userSourceType = Integer.valueOf(userSourceTypeStr);
			BusAreaActUserSourceTypeEnum.getNameByValue(userSourceType);
		}else{
			userSourceType = BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue();
		}
		/*根据userSourceType校验短信验证码*/
		if(userSourceType == BusAreaActUserSourceTypeEnum.OTHERS.getValue()){
			//用户来源未知，需要短信验证
			CommonValidUtil.validStrNull(veriCodeStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_VERICODE);
			//验证短信验证码
			
		}
		/*校验getSourceType是否有值及枚举*/
		Long userSourceId = null;
		if(userSourceType == BusAreaActUserSourceTypeEnum.SHOP_IMPORT.getValue()){
			//商铺导入，用户来源ID必填
			CommonValidUtil.validStrNull(userSourceIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_SOURCE_ID);
			CommonValidUtil.validStrLongFmt(userSourceIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_ID);
			userSourceId = Long.valueOf(userSourceIdStr);
		}
		Integer userSourceChannel = null;
		if(!StringUtils.isBlank(userSourceChannelStr)){
			CommonValidUtil.validStrIntFmt(userSourceChannelStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USER_SOURCE_CHANNEL);
			userSourceChannel = Integer.valueOf(userSourceChannelStr);
		}
		
		int flag = applyBusinessArea(busAreaActId,userSourceType,userSourceId,userSourceChannel,mobileStr,veriCodeStr);
		String msg = (flag == 1) ? "获取商圈活动资格成功！" : "该手机号码已经获取商圈活动资格！";
		return msg;
		
	}

	@Override
	public BusinessAreaActivityDto getBusinessAreaActivityById(Long businessAreaActivityId) throws Exception {
		return businessAreaActivityDao.getBusinessAreaActivityById(businessAreaActivityId);
	}
    
    @Override
    public BusinessAreaShopDto getBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception {
    	return businessAreaShopDao.getBusinessAreaShopByCompKey(businessAreaActivityId, shopId);
    }
	@Override
	public Map<String, Object> getActivityDetail(Long shopId,Integer businessAreaActivityId) throws NumberFormatException, Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		BusinessAreaActivityDto businessAreaActivityDto= businessAreaActivityDao.getBusinessAreaActivityById(Long.valueOf(businessAreaActivityId.toString()));
		if (businessAreaActivityDto != null) {
			map.put("businessAreaName",
					businessAreaActivityDto.getBusinessAreaName());
			map.put("actNum", businessAreaActivityDto.getActNum());
			map.put("signEndDate", DateUtils.format(businessAreaActivityDto.getSignEndDate(), DateUtils.DATE_FORMAT));
			map.put("beginDate", DateUtils.format(businessAreaActivityDto.getBeginDate(), DateUtils.DATE_FORMAT));
			map.put("endDate", DateUtils.format(businessAreaActivityDto.getEndDate(), DateUtils.DATE_FORMAT));
			map.put("activityDesc", businessAreaActivityDto.getActDesc());
			map.put("activityStatus",
					businessAreaActivityDto.getActivityStatus());
			map.put("townId", businessAreaActivityDto.getTownId());
			map.put("townName", businessAreaActivityDto.getTownName());
			map.put("columnId", businessAreaActivityDto.getColumnId());
			map.put("columnName", businessAreaActivityDto.getColumnName());
			map.put("shopName", businessAreaActivityDto.getShopName());
			map.put("shopId", businessAreaActivityDto.getShopId());
			map.put("activityRuleName",
					businessAreaActivityDto.getActivityRuleName());
			Long busAreaActivityId = businessAreaActivityDto
					.getBusinessAreaActivityId();
			// 查询附件ID以及url
			List<Attachment> attachmentList = attachmentDao
					.queryAttachmentBybizId(busAreaActivityId);
			String actPosterIds = "";
			String actPosterUrls = "";
			String actBizIds = "";
			for (int i = 0; i < attachmentList.size(); i++) {
				Attachment attachment = attachmentList.get(i);
				if(i!=attachmentList.size()-1){
					actPosterIds += attachment.getAttachmentId() + ",";
					if (attachment.getFileUrl() != null && !attachment.getFileUrl().equals("")) {
						actPosterUrls += FdfsUtil.getFileProxyPath(attachment
								.getFileUrl()) + ",";
					}
					if(attachment.getBizId()!=null){
						actBizIds+=attachment.getBizId()+",";
					}
				}else{
					actPosterIds += attachment.getAttachmentId() ;
					if (attachment.getFileUrl() != null && !attachment.getFileUrl().equals("")) {
						actPosterUrls += FdfsUtil.getFileProxyPath(attachment
								.getFileUrl());
					}
					if(attachment.getBizId()!=null){
						actBizIds+=attachment.getBizId();
					}
				
				}
			}
			map.put("actPosterUrls", actPosterUrls);
			map.put("actPosterIds", actPosterIds);
			map.put("bizIds", actBizIds);


			// 查询阅读人数以及分享人数统计
			BusinessAreaStatDto businessAreaStatDto = new BusinessAreaStatDto();
			businessAreaStatDto.setBusinessAreaActivityId(busAreaActivityId);
			businessAreaStatDto.setShopId(businessAreaActivityDto.getShopId());
			businessAreaStatDto = businessAreaStatDao
					.getBusinessAreaStatByCompKey(businessAreaStatDto);
			if (businessAreaStatDto != null) {
				map.put("viewNum", businessAreaStatDto.getReadNum());
				map.put("shareNum", businessAreaStatDto.getShareNum());
			} else {
				map.put("viewNum", 0);
				map.put("shareNum", 0);
			}

			// 查询活动配置规则
			BusinessAreaConfigDto businessAreaConfigDto = new BusinessAreaConfigDto();
			businessAreaConfigDto
					.setBusinessAreaActivityId(businessAreaActivityDto
							.getBusinessAreaActivityId());
			List<Map> list = businessAreaConfigDao
					.getBusinessAreaConfigByActivityId(businessAreaConfigDto);
			map.put("activityModelConfigs", list);

		}
		
		
		return map;
	}

	@Override
	public PageModel getActivityShopList(Map<String, Object> map) throws Exception {
		PageModel pageModel=new PageModel();
		int count = businessAreaActivityDao.getActivityShopListCount(map);
		if(count!=0){
			pageModel.setTotalItem(count);
			List<Map> list=new ArrayList<Map>();
			list = businessAreaActivityDao.getActivityShopList(map);
			
			for (Map businessAreaActivityDtoMap : list) {
				if(businessAreaActivityDtoMap.get("shopLogoUrl")!=null){
					businessAreaActivityDtoMap.put("shopLogoUrl", FdfsUtil.getFileProxyPath(businessAreaActivityDtoMap.get("shopLogoUrl")+""));
				}
			}
			pageModel.setList(list);
		}
		return pageModel;
	}

	@Override
	public List<BusinessAreaActivityDto> getActivitiesByShopId(
			BusinessAreaActivityDto businessAreaActivityDto) {
		return businessAreaActivityDao.getActivitiesByShopId(businessAreaActivityDto);
		
	}
	
	/**
	 * 获取活动列表
	 * @Title: getActivityListByCondition 
	 * @param @param businessAreaActivityDto
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<BusinessAreaActivityDto> getActivityListByCondition(
			BusinessAreaActivityDto businessAreaActivityDto) throws Exception {
		return businessAreaActivityDao.getBusinessAreaActivityListByCondition(businessAreaActivityDto, null);
	}

}
