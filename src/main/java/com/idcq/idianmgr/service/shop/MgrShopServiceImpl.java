package com.idcq.idianmgr.service.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.column.IColumnDao;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.goods.IGoodsCategoryDao;
import com.idcq.appserver.dao.goods.IShopGoodsDao;
import com.idcq.appserver.dao.permission.IRoleDao;
import com.idcq.appserver.dao.permission.IUserRoleDao;
import com.idcq.appserver.dao.shop.IBookTimeRuleDao;
import com.idcq.appserver.dao.shop.IDistribTakeoutSettingDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopMemberDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.ShopGoodsDto;
import com.idcq.appserver.dto.permission.RoleDto;
import com.idcq.appserver.dto.permission.UserRoleDto;
import com.idcq.appserver.dto.shop.BookTimeRuleDto;
import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.wxscan.MD5Util;
import com.idcq.idianmgr.common.MgrCodeConst;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dao.shop.ICategoryDao;
import com.idcq.idianmgr.dao.shop.IShopCashierDao;
import com.idcq.idianmgr.dao.shop.ITechTypeDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.shop.CategoryDto;
import com.idcq.idianmgr.dto.shop.ShopBean;
import com.idcq.idianmgr.dto.shop.ShopBookSettingDto;
import com.idcq.idianmgr.dto.shop.ShopCashierParams;
import com.idcq.idianmgr.dto.shop.TechTypeDto;

@Service
public class MgrShopServiceImpl implements IMgrShopService {
	
	private static final Logger logger = Logger.getLogger(MgrShopServiceImpl.class);

	@Autowired
	private IDistribTakeoutSettingDao distribTakeoutSettingDao;
	@Autowired
	private IBookTimeRuleDao bookTimeRuleDao;
	@Autowired
	private ICategoryDao categoryDao;
	@Autowired
	private ITechTypeDao techTypeDao;
	@Autowired
	private IGoodsGroupDao goodsGroupDao;
	
	@Autowired
	private IGoodsCategoryDao goodsCategoryDao;
	
	@Autowired
	private IShopDao shopDao;
	
	@Autowired
	private IShopCashierDao shopCashierDao;
	
	@Autowired
	private IColumnDao columnDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IAttachmentDao attachmentDao;
	
	@Autowired
	private IMemberServcie memberServcie;
	
	@Autowired
	private IUserRoleDao userRoleDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IShopGoodsDao shopGoodsDao;
	
	@Autowired
	private IShopMemberDao shopMemberDao;
	
	@Override
	public int setShopBookSetting(ShopBookSettingDto bkSettingDto)
			throws Exception {
		// 数据校验
		Long shopId = bkSettingDto.getShopId();
		Integer operateType = bkSettingDto.getOperateType();
		String deliveryTime = bkSettingDto.getDeliveryTime();
		String stopDate = bkSettingDto.getStopDate();
		String weekDate = bkSettingDto.getWeekDay();
		Integer settingType = bkSettingDto.getSettingType();
		Long settingId = bkSettingDto.getSettingId();
		
		CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID    );
		CommonValidUtil.validIntNull(operateType, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_BK_SETTING_OPERATE_TYPE);
		CommonValidUtil.validStrNull(deliveryTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_DELIVERY_TIME);
		// 封装预约规则数据
		DistribTakeoutSettingDto tkSettingDto = new DistribTakeoutSettingDto();
		tkSettingDto.setShopId(shopId);
		tkSettingDto.setSettingId(settingId);
		settingType = (settingType == null ? 2 : settingType);
		tkSettingDto.setSettingType(settingType);
		tkSettingDto.setMaxReserveBeforeDay(bkSettingDto.getMaxReserveBeforeDay());
		tkSettingDto.setMinReserveBeforeHour(bkSettingDto.getMinReserveBeforeHour());
		tkSettingDto.setDeliveryDistribution(bkSettingDto.getDeliveryDistribution());
		tkSettingDto.setLeastBookPrice(bkSettingDto.getLeastBookPrice());
		tkSettingDto.setIsCashOnDelivery(bkSettingDto.getIsCashOnDelivery());
		tkSettingDto.setDeliveryPrice(bkSettingDto.getDeliveryPrice());
		tkSettingDto.setIsReduction(bkSettingDto.getIsReduction());
		tkSettingDto.setReduction(bkSettingDto.getReduction());
		tkSettingDto.setRemark(bkSettingDto.getRemark());
		tkSettingDto.setCreateTime(new Date());
		// 封装预约时间规则数据
		List<BookTimeRuleDto> ruleList = new ArrayList<BookTimeRuleDto>();
		String[] deliveryTimeArray = null;
		String beginTimeStr = null;
		String endTimeStr = null;
		Date beginTime = null;
		Date endTime = null;
		// 接单时间
		if(!StringUtils.isBlank(deliveryTime)){
			deliveryTimeArray = deliveryTime.split(",");
			for(String e : deliveryTimeArray){
				if(!StringUtils.isBlank(e) && e.contains("&")){
					beginTimeStr = e.substring(0,e.indexOf("&"));
					endTimeStr = e.substring(e.indexOf("&")+1,e.length());
					beginTime = DateUtils.getTimeByPattern(beginTimeStr, DateUtils.TIME_FORMAT2);
					endTime = DateUtils.getTimeByPattern(endTimeStr, DateUtils.TIME_FORMAT2);
					
					BookTimeRuleDto rule = new BookTimeRuleDto();
					rule.setBeginTime(beginTime);
					rule.setEndTime(endTime);
					rule.setRuleType(1);
					rule.setCreateTime(new Date());
					
					ruleList.add(rule);
				}
			}
			
		}
		// 暂停预定日期
		if(!StringUtils.isBlank(stopDate)){
			deliveryTimeArray = stopDate.split(",");
			for(String e : deliveryTimeArray){
				if(!StringUtils.isBlank(e) && e.contains("&")){
					beginTimeStr = e.substring(0,e.indexOf("&"));
					endTimeStr = e.substring(e.indexOf("&")+1,e.length());
					beginTime = DateUtils.getTimeByPattern(beginTimeStr, DateUtils.DATE_FORMAT);
					endTime = DateUtils.getTimeByPattern(endTimeStr, DateUtils.DATE_FORMAT);
					
					BookTimeRuleDto rule = new BookTimeRuleDto();
					rule.setStopBeginDate(beginTime);
					rule.setStopEndDate(endTime);
					rule.setRuleType(3);
					rule.setCreateTime(new Date());
					
					ruleList.add(rule);
				}
			}
			
		}
		// 可预定周期
		if(!StringUtils.isBlank(weekDate)){
			deliveryTimeArray = weekDate.split(",");
			for(String e : deliveryTimeArray){
				if(!StringUtils.isBlank(e)){
					BookTimeRuleDto rule = new BookTimeRuleDto();
					rule.setWeekDay(Integer.valueOf(e));
					rule.setRuleType(2);
					rule.setCreateTime(new Date());
					ruleList.add(rule);
				}
			}
			
		}
		// 预定及外卖费用规则入库
		if(operateType == CommonConst.BK_SETTING_OP_TYPE_XZ){//新增
			if(settingId != null && settingId > 0){
				this.bookTimeRuleDao.delTimeRuleDtoBySettingId(settingId);
				this.distribTakeoutSettingDao.delDistribTakeoutSetting(settingId);
			}else{
				settingId = this.distribTakeoutSettingDao.getDistribTakeoutSettingByType(shopId,settingType);
				this.bookTimeRuleDao.delTimeRuleDtoBySettingId(settingId);
				this.distribTakeoutSettingDao.delDistribTakeoutSetting(settingId);
//				this.distribTakeoutSettingDao.delDistribTakeoutSettingByType(shopId,settingType);
			}
			tkSettingDto.setSettingId(null);
			settingId = this.distribTakeoutSettingDao.addDistribTakeoutSetting(tkSettingDto);
			if(ruleList != null){
				for(BookTimeRuleDto e : ruleList){
					e.setSettingId(settingId);
					// 预定时间规则入库
					this.bookTimeRuleDao.addTimeRuleDto(e);
				}
			}
		}else{//修改
			// 设置ID必填
			CommonValidUtil.validLongNull(settingId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_SETTING_ID);
			CommonValidUtil.validPositLong(settingId, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_SETTING_ID);
			this.distribTakeoutSettingDao.updateDistribTakeoutSetting(tkSettingDto);
			// 删除旧时间规则
			this.bookTimeRuleDao.delTimeRuleDtoBySettingId(settingId);
			// 新增时间规则
			if(ruleList != null){
				for(BookTimeRuleDto e : ruleList){
					e.setSettingId(settingId);
					// 预定时间规则入库
					this.bookTimeRuleDao.addTimeRuleDto(e);
				}
			}
		}
		return 1;
	}
	

	@Override
	public String operateCategory(CategoryDto categoryDto) throws Exception{

		if (categoryDto.getParentCategoryId() == null) {
			categoryDto.setParentCategoryId(0l);
		}

		// 校验名称唯一
		if (categoryDao.isNameExist(categoryDto)) {
			throw new ServiceException(CodeConst.CODE_PARAMETER_REPEAT,
					"该分类已存在");
		}

		//新增操作
		if(categoryDto.isAdd()){
			//新增分类
			logger.info("-----------------新增分类开始");
			Integer cateIndex = categoryDao.getCategoryIndexBy(categoryDto);
			if(cateIndex != null)
			{
			    categoryDto.setCategoryIndex(cateIndex + 1);
			}
			else 
			{
			    categoryDto.setCategoryIndex(1);
			}
			categoryDao.insertCategory(categoryDto);
			logger.info("-----------------新增分类结束");
			
		}else {
			//修改操作
			logger.info("-----------------修改分类开始");
			categoryDao.updateCategory(categoryDto);
			logger.info("-----------------修改分类结束");
			
			
			//更新商品族名称(场地类)
			logger.info("-----------------修改分类对应商品族名称开始");
			updateGoodsGroupName(categoryDto);
			logger.info("-----------------修改分类对应商品族名称结束");
		}
		//新增轮播图片与分类关系
		dealPictureRelation(categoryDto.getCategoryId(),categoryDto.getCarouselAttachmentIds(), "11", "2",null,null);
		
		//新增缩略图与分类关系
		dealPictureRelation(categoryDto.getCategoryId(), categoryDto.getLogoId(), "11", "1",null,null);
		
		
		return categoryDto.getCategoryId()+"";
		
	}


	/**
	 * 组装商品族名称
	 * 格式：一级分类名称(二级分类名称)
	 * @param categoryDto
	 * @return
	 */
	private void updateGoodsGroupName(CategoryDto categoryDto) {

		try {
			Map<String, Object> category = goodsCategoryDao
					.getGoodsCategoryById(categoryDto.getCategoryId());
			if (category != null && category.size() != 0) {
				Long pId = (Long) category.get("pId");
				String categoryName = (String) category.get("categoryName");

				if (pId == null || pId == 0) {
					// 1.这种情况修改的是一级分类，那么他下面的二级分类对应的商品族名称都要修改
					List<GoodsCategoryDto> categoryDtos = goodsCategoryDao
							.getGoodsCategoryByCategoryPid(categoryDto
									.getCategoryId());
					if (CollectionUtils.isNotEmpty(categoryDtos)) {
						for (GoodsCategoryDto c : categoryDtos) {
							categoryDao.updateGroupGoodsName(categoryName + "("
									+ c.getCategoryName() + ")",
									c.getGoodsCategoryId());
						}
					} else {
						// 只有一级分类
						categoryDao.updateGroupGoodsName(categoryName,
								categoryDto.getCategoryId());
					}
				} else {
					// 2.这种情况修改的是二级分类，那么只需要修改其对应的商品族名称即可
					Map<String, Object> parentCategory = goodsCategoryDao
							.getGoodsCategoryById(pId);
					String parentCategoryName = (String) parentCategory
							.get("categoryName");
					categoryDao.updateGroupGoodsName(parentCategoryName + "("
							+ categoryName + ")", categoryDto.getCategoryId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 
	 * 新增单张图(可重用)
	 * 逻辑：先删除关联关系->新增关联关系
	 * @param bizId  业务ID
	 * @param logonIds 图片ID
	 * @param bizType 业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师=10,商品分类=11,launcher主页图标=12,商圈活动=13,收银机日志=14,商圈活动类型=15,消息中心消息=16,出入库记录=17,盘点记录=18
	 * @param picType  图片类型:缩略=1;轮播=2;法人(商铺)=3(biz_index =1营业执照,=2组织机构代码证,=3税务登记证,=4经营许可证(原，现需要单独出来));身份证=4(biz_index=1正，=2反);个人技能证书=5，商铺经营许可证=6
	 * @param bizIndex 展示顺序
	 * @param fileNos 证书等编号
	 */
	private void dealPictureRelation(Long bizId, String logonIds, String bizType,
			String picType,String bizIndex,String fileNos) {
		// 先删除关系
		logger.info("开始新增图片关联关系，业务ID=" + bizId + "; 图片ID=" + logonIds);
		if (bizId == null)
			return;
		if (logonIds==null || StringUtils.isEmpty(logonIds) || "null".equals(logonIds)) {
			logger.info("客户端没有传图片ID，不能建立关联关系");
			return;
		}
		
		categoryDao.delAttachmentRelation(buildAttachmentRelation(bizId, null,
				bizType, picType,bizIndex));
		logger.info("删除图片与业务关联关系完毕");

		// 再增加关系
		if (logonIds.indexOf(",") > 0) {
			String[] attachmentIds = logonIds.split(",");
			String[] fileNosArray=null;
			if(fileNos!=null){
				fileNosArray=fileNos.split(",");
				if(attachmentIds.length!=fileNosArray.length){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "证件数量与证件编号的数量不一致");
				}
			}
			for (int i = 0; i < attachmentIds.length; i++) {
				categoryDao.insertAttachmentRelation(buildAttachmentRelation(
						bizId, attachmentIds[i], bizType, picType,bizIndex));
				if(fileNosArray != null){
					updateAttachment(attachmentIds[i],fileNosArray[i]);
				}
			}
		} else {
			categoryDao.insertAttachmentRelation(buildAttachmentRelation(bizId,
					logonIds, bizType, picType,bizIndex));
			if(fileNos!=null){
				updateAttachment(logonIds,fileNos);
			}
		}
		DataCacheApi.del(CommonConst.KEY_SHOP_LOGO + bizId);
		logger.info("创建关联关系完毕");
		logger.info("完成新增图片关联关系，业务ID=" + bizId + "; 图片ID=" + logonIds);
	}
	
	private void updateAttachment(String attachmentId, String fileNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("attachmentId", attachmentId);
		map.put("fileNo", fileNo);
		attachmentDao.updateAttachment(map);
	}

	private Map<String, Object> buildAttachmentRelation(Long bizId,String attachmentId,String bizType, String picType,String bizIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(bizId != null)
			map.put("bizId", bizId);
		if(attachmentId != null)
			map.put("attachementId", attachmentId);
		if(bizType != null)
			map.put("bizType", bizType);// 业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师=10,商品分类=11,launcher主页图标=12,商圈活动=13,收银机日志=14,商圈活动类型=15,消息中心消息=16,出入库记录=17,盘点记录=18
		if(picType != null)
			map.put("picType", picType); // 图片类型:缩略=1;轮播=2;法人(商铺)=3(biz_index =1营业执照,=2组织机构代码证,=3税务登记证,=4经营许可证(原，现需要单独出来));身份证=4(biz_index=1正，=2反);个人技能证书=5，商铺经营许可证=6
		if(bizIndex != null)
			map.put("bizIndex", bizIndex);//展示顺序
		return map;
	}


	@Override
	public void delCategory(CategoryDto categoryDto) throws Exception {
		
		logger.info("------------------开始处理分类 shopId="+categoryDto.getShopId());
		String[] categoryIds = categoryDto.getCategoryIds().split(",");
		// 删除操作
		if (categoryDto.isDel()) {
			//刷新缓存
			new RefreshGoodsCache(categoryIds,Long.parseLong(categoryDto.getShopId())).start();
			for (int i = 0; i < categoryIds.length; i++) {
				
				// 先删除子分类和子分类的关联关系
				logger.info("-----------删除子分类开始");
				for(CategoryDto child : getChildCategorys(Long.parseLong(categoryIds[i]))){
					deleteCategory(child.getCategoryId());
				}
				
				//删除分类
				deleteCategory(Long.parseLong(categoryIds[i]));
				
				logger.info("-----------删除子分类结束");
				
			}
		} else if(categoryDto.isStop()){
			//刷新缓存
			new RefreshGoodsCache(categoryIds,Long.parseLong(categoryDto.getShopId())).start();
			// 停用操作
			for (int i = 0; i < categoryIds.length; i++) {
				categoryDto.setCategoryId(Long.parseLong(categoryIds[i]));
				categoryDto.setStopDate(categoryDto.getStopDate());
				categoryDto.setStatus(0);// 上架状态，禁用-0,启用-1
				categoryDao.updateCategory(categoryDto);
				logger.info("------------------停用分类成功");
				
				//更新商品族状态“下架”
				updateGroupGoodsOfCatgory(0, Long.parseLong(categoryIds[i]));
				logger.info("------------------下架商品族成功");
				
				// 停用子类，有的话
				for (CategoryDto child : getChildCategorys(Long.parseLong(categoryIds[i]))) {
					child.setStopDate(categoryDto.getStopDate());
					child.setStatus(0);// 上架状态，禁用-0,启用-1
					categoryDao.updateCategory(child);
					logger.info("------------------停用子分类成功");
					
					//更新商品族状态“下架”
					updateGroupGoodsOfCatgory(0, child.getCategoryId());
					logger.info("------------------下架商品族成功");
				}
			}
		} else if(categoryDto.isRestart()){
			// 启用操作
			for (int i = 0; i < categoryIds.length; i++) {
				categoryDto.setCategoryId(Long.parseLong(categoryIds[i]));
				categoryDto.setStatus(1);// 上架状态，禁用-0,启用-1
				categoryDto.setStopDate("");
				categoryDao.updateCategory(categoryDto);
				logger.info("------------------启用分类成功");
				
				//更新商品族状态“上架”
				updateGroupGoodsOfCatgory(1, Long.parseLong(categoryIds[i]));
				logger.info("------------------上架商品族成功");
				// 停用子类，有的话
				for (CategoryDto child : getChildCategorys(Long
						.parseLong(categoryIds[i]))) {
					child.setStopDate("");
					child.setStatus(1);// 上架状态，禁用-0,启用-1
					categoryDao.updateCategory(child);
					logger.info("------------------启用子分类成功");
					
					//更新商品族状态“上架”
					updateGroupGoodsOfCatgory(1, child.getCategoryId());
					logger.info("------------------上架商品族成功");
				}
			}
		}
		logger.info("------------------处理分类完毕。");
	}
	
	private void deleteCategory(Long categoryId){
		
		// 更新对应的商品族状态为“已删除”
		updateGroupGoodsOfCatgory(2,categoryId);
		
		
		// 删除关联关系
		logger.info("------------------删除分类关联关系开始，分类ID=" + categoryId);
		categoryDao.deleteCategoryRelation(categoryId+"");
		logger.info("------------------删除分类关联关系结束，分类ID=" + categoryId);
		
		logger.info("------------------删除分类开始，categoryId="+categoryId);
		categoryDao.deleteCategory(categoryId);
		logger.info("------------------删除分类成功");	
	}
	
	class RefreshGoodsCache extends Thread{
		private Long shopId;
		private String[] categoryIds;
		public RefreshGoodsCache(String[] categoryIds,Long shopId){
			this.shopId = shopId;
			this.categoryIds = categoryIds;
		}
		@Override
		public void run() {
			logger.info("开始刷新分类商品缓存--start");
			for (int i = 0; i < categoryIds.length; i++) {
				try {
					refreshGoodsCache(Long.parseLong(categoryIds[i]),shopId);
				} catch (Exception e) {
					logger.error("清除分类编号："+categoryIds[i]+"异常了，继续下一轮循环",e);
					continue;
				}
				
			}
		}
		
	}
	
	private void refreshGoodsCache(Long categoryId,Long shopId){
		//TODO ....
		if (categoryId == null || categoryId.intValue() == 0) {
			return;
		}
		Long childCategoryId = null;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("categoryId", categoryId);
			param.put("shopId", shopId);
			GoodsGroupDto goodsGroupDto = goodsGroupDao.getGoodsGroupBycategoryIdAndShopId(param);
			if(null != goodsGroupDto){
				String ggKey = CommonConst.KEY_SHOP_TOP_GOODS+shopId;
				DataCacheApi.del(ggKey);
				param.put("goodsGroupId", goodsGroupDto.getGoodsGroupId());
				List<Long> goodsIds = goodsGroupDao.getGoodsIdsByGroupIdAndShopId(param);
				if (goodsIds != null && goodsIds.size() > 0) {
					for(Long goodsId : goodsIds)
					{
						String key = CommonConst.KEY_GOODS+goodsId;
						DataCacheApi.del(key);
					}
				}
			}
			childCategoryId = categoryDao.getChildCategoryIdByCategoryId(categoryId);
			//递归调用
			refreshGoodsCache(childCategoryId, shopId);
		} catch (Exception e) {
			logger.info("一点管家场地-清除分类下商品缓存异常，分类编号："+childCategoryId,e);
		}
	}
	
	private void updateGroupGoodsOfCatgory(int status, Long categoryId){
		logger.info("------------------更新分类对应的商品族状态开始，分类ID=" + categoryId);
		int s = status; //上架状态，下架-0,上架-1,删除-2,草稿-3
		categoryDao.updateGroupGoodsOfCatgory(s,categoryId);
		logger.info("------------------更新分类对应的商品族状态结束，分类ID=" + categoryId);
	}
	
	// 获取子分类
	private List<CategoryDto> getChildCategorys(Long parentCategoryId){
		CategoryDto param = new CategoryDto();
		param.setParentCategoryId(parentCategoryId);
		List<CategoryDto> list = categoryDao.getCategorys(param);
		if(list == null)
			list = new ArrayList<CategoryDto>();
		return list;
	}


	@Override
	public String updateTechType(TechTypeDto techTypeDto) throws Exception {
		
		// 校验名称唯一
		if(techTypeDao.isNameExist(techTypeDto)){
			throw new ServiceException(CodeConst.CODE_PARAMETER_REPEAT, "该级别已存在");
		}
		
		// 新增 --
		if(techTypeDto.isAdd()||StringUtils.isEmpty(techTypeDto.getTechTypeId())){
			
			techTypeDto.setTypeOrder(1); //排序默认都是1
			techTypeDto.setIsValid(1); // 是否有效：有效=1，失效(删除)=0
			if(techTypeDto.getParentTechTypeId() == null)
				techTypeDto.setParentTechTypeId("0");
			logger.info("--------------------新增技师级别开始");
			techTypeDto.setCreateTime(new Date());
			techTypeDao.addTechType(techTypeDto);
			logger.info("--------------------新增技师级别结束");
		}else {
			// 修改
			logger.info("--------------------修改技师级别开始");
			techTypeDto.setLastUpdateTime(new Date());
			techTypeDao.updateTechType(techTypeDto);
			logger.info("--------------------修改技师级别结束");
		}
		return techTypeDto.getTechTypeId();
	}


	@Override
	public void delTechType(TechTypeDto techTypeDto) throws Exception {
		String[] techTypeIds = techTypeDto.getTechTypeIds().split(",");
		TechTypeDto model = null;
		for (int i = 0; i < techTypeIds.length; i++) {
			
			//判断技师级别是否使用，使用了不能删除
			if(techTypeDao.isTechTypeUsed(techTypeIds[i]))
				throw new ServiceException(CodeConst.CODE_TECH_TYPE_IS_USED,"技师级别已被使用，不能删除");
			
			model = new TechTypeDto();
			model.setTechTypeId(techTypeIds[i]);
			model.setShopId(techTypeDto.getShopId());
			model.setIsValid(0); //是否有效：有效=1，失效(删除)=0
			techTypeDao.updateTechType(model);
			logger.info("---------------删除技师级别完毕");
		}
	}



	@Override
	public PageModel getTechTypes(TechTypeDto techTypeDto, Integer page,
			Integer pageSize) throws Exception {
		PageModel pageModel = new PageModel();
		pageModel.setPageSize(pageSize);
		pageModel.setToPage(page);
		pageModel.setTotalItem(techTypeDao.getTechTypeCount(techTypeDto));
		pageModel.setList(techTypeDao.getTechTypeDtos(techTypeDto, page, pageSize));
		return pageModel;
	}


	@Override
	public void saveShop(ShopDto shopDto, ShopBean shopBean) throws Exception {
		
		// 校验用户是否存在
		UserDto userDto = userDao.getUserById(shopDto.getPrincipalId());
		if(userDto == null){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "会员不存在");
		}
		if(isExistSameNameShop(shopDto.getPrincipalId(),null,shopDto.getShopName())){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该帐号已存在同名商铺");
		}
		if(shopDto.getMemberDiscount() != null &&
				shopDto.getMemberDiscount() == 0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "会员折扣不能为0");
		}
		/*if(shopDto.getReferUserId()!=null && shopDto.getReferUserId().equals(userDto.getUserId())){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "推荐会员不能是本人");
		}*/
		//添加一张经营许可证信息进入店铺表
		shopDto.setBusinessCertificatePicIds(getOnefromArrayString(shopBean.getBusinessCertificatePicIds()));
		shopDto.setBusinessCertificates(getOnefromArrayString(shopBean.getBusinessCertificates()));
		
		//查询服务人员信息
		if(shopBean.getShopServeUserMobile()!=null){
			UserDto serverUserDto =userDao.getUserByMobile(shopBean.getShopServeUserMobile());
			CommonValidUtil.validObjectNull(serverUserDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_USER);
			shopDto.setShopServerUserId(serverUserDto.getUserId());
		}
		shopDao.saveShop(shopDto);
		// 处理商铺Logon
		dealPictureRelation(Long.valueOf(shopDto.getShopId()+""), String.valueOf(shopBean.getShopLogoId()), "1", "1",null,null);
		
		// 处理商铺环境图
		if(shopBean.getShopSettingImgIds() != null){
			dealPictureRelation(Long.valueOf(shopDto.getShopId()+""), shopBean.getShopSettingImgIds(), "1", "2",null,null);
		}
		
		// 创建商铺账户
		saveShopAccount(shopDto.getShopId());
		
		// 更新用户是店铺管理者
		updateUserIsManager(shopDto.getPrincipalId());
		
		// 处理身份证图片，身份证是保存在1dcq_user表里面
		updateUserIdentity(shopBean);
		if(shopBean.getBusinessCertificatePicIds() != null){
			//处理商铺中经营许可证图片的关系
			dealPictureRelation(Long.valueOf(shopDto.getShopId()+""), String.valueOf(shopBean.getBusinessCertificatePicIds()), "1", "3","4",shopBean.getBusinessCertificates());
		}
		if(shopBean.getSkillsCertificatePicIds() != null){
			//处理商铺中个人技能证书图片的关系
			dealPictureRelation(userDto.getUserId(), String.valueOf(shopBean.getSkillsCertificatePicIds()), "2", "5",null,shopBean.getSkillsCertificateNos());
		}
		
	}
	
	public String getOnefromArrayString(String arryStrings){
		String arryString = null;
		if(arryStrings != null){
			if(arryStrings.contains(",")){
//				if(NumberUtil.isNumer(arryStrings.split(",")[0])){
					arryString = arryStrings.split(",")[0];
//				}
			}else{
//				if(NumberUtil.isNumer(arryStrings)){
					arryString = arryStrings;
//				}
			}
		}
		return arryString;
	}
	
	/**
	 * 查询同一负责人有相同名字的店铺
	 * @param shopId
	 * @param shopName
	 * @return
	 */
	public boolean isExistSameNameShop(Long userId,Long shopId,String shopName){
		if(userId==null && shopName==null){
			return false;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("shopName", shopName);
		map.put("shopId", shopId);
		int n = shopDao.getShopByShopIdAndShopName(map);
		return n>0?true:false;
	}

	// 更新用户为店铺管理者
	private void updateUserIsManager(Long principalId) {
		userDao.updateUserIsManager(principalId);
	}


	/**
	 * 创建商铺账户
	 * @param shopId
	 */
	private void saveShopAccount(Long shopId) throws Exception{
		Map<String, Object> shopAccount = new HashMap<String, Object>();
		shopAccount.put("shopId", shopId);
		shopAccount.put("amount", 0);
		shopAccount.put("accountStatus", 1); // 账户状态，正常-1，冻结-0
		shopDao.saveShopAccount(shopAccount);
		
	}


	@Override
	public Map<String, Object> optShopCashier(ShopCashierParams shopCashier) throws Exception {
		// TODO 后续如果需要校验商铺状态，则在此新增 
		//1、根据手机号码查询用户表是否存在用户2、假如不存在则增加增加user，并在雇员表保存改userId 3、假如存在则查询雇员表是否有userId，没有就更新userId
		Map<String, Object> param = new HashMap<String, Object>();
		String  password = "123456";
		if(null != shopCashier.getCashierId()){
			param.put("cashierId", shopCashier.getCashierId());
			int count = shopCashierDao.findShopCashierExists(param);
			if(count == 0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "收银员不存在");
			}
			//修改
			shopCashierDao.updateShopCashier(shopCashier);
		}else{
			
			NumberUtil.isMobileNO(shopCashier.getLoginName(), "loginName");
			param.put("loginName", shopCashier.getLoginName());
			param.put("shopId", shopCashier.getShopId());
			int count = shopCashierDao.findShopCashierExists(param);
			if(count > 0){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "登录账户已经存在");
			}
			
			//手机
			String mobile  = shopCashier.getMobile()==null ? shopCashier.getLoginName() : shopCashier.getMobile();
			shopCashier.setMobile(mobile);
			//保存密文密码
			password = mobile.substring(mobile.length() - 6) ;
			shopCashier.setPassword(MD5Util.getMD5Str(password));
			//新增
			shopCashierDao.insertShopCashier(shopCashier);
			//默认赋予手机权限
			addEmployeeRoleType(shopCashier.getShopId(), shopCashier.getCashierId());
		}
		
		
		//增加雇员自动转换成会员
		Map<String, Object> rlt = new HashMap<String, Object>();
		rlt.put("isSendFlag", false);//默认不发送
		//短信验证才自动注册
		if(shopCashier.getGeneratePwdMode()!=null && 1==shopCashier.getGeneratePwdMode()){
			shopCashier.setPassword(password);//明文
			rlt = autoChangeUser(shopCashier);
		}
	    
		logger.info("操作收银员最后结果："+shopCashier);
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("cashierId", shopCashier.getCashierId());
		reMap.putAll(rlt);
		
		return reMap;
	}
	/**
	 * 雇员转换成会员
	 * @throws Exception 
	 */
	public Map<String, Object> autoChangeUser(ShopCashierParams shopCashier) throws Exception{
		/*
		 1、根据手机号码查询用户表是否存在用户
		 2、假如不存在则增加增加user，并在雇员表保存改userId 
		 3、假如存在则查询雇员表是否有userId，没有就更新userId
		*/
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//是否发送提示短信
		boolean isSendFlag = false;
		Long userId = null;
		String mobile  = shopCashier.getMobile()==null ? shopCashier.getLoginName() : shopCashier.getMobile();
		
		if(StringUtils.isNotBlank(mobile)){
			UserDto userDB = userDao.getUserByMobile(mobile);
			
			resultMap.put("password", shopCashier.getPassword());
			String md5Password = MD5Util.getMD5Str(shopCashier.getPassword());
			if(userDB == null || userDB.getIsMember() == CommonConst.USER_TO_ACTIVATE){
				UserDto pram = memberServcie.saveUser(mobile,md5Password, null, null,
						shopCashier.getShopId().toString(), CommonConst.REFRESH_SHOP, Integer.valueOf(CommonConst.REGISTER_TYPE_SHOP));
				userId = pram.getUserId();
				//新注册需要发送短信
				isSendFlag = true;

			}
			else{
				userId = userDB.getUserId();
				md5Password = userDB.getPassword();
			}
			
			//关联用户
			shopCashier.setUserId(userId);
			shopCashier.setShopId(shopCashier.getShopId());
			shopCashier.setCheckTime(new Date());
			shopCashier.setIsCheck(CommonConst.USER_IS_CHECK);
			shopCashier.setPassword(md5Password);
			shopCashierDao.updateShopCashier(shopCashier);
		}
		resultMap.put("userId", userId);
		resultMap.put("isSendFlag", isSendFlag);
		
		
		return resultMap;
	}

	/**
	 * 增加雇员权限
	 * 
	 */
	public void addEmployeeRoleType(Long shopId,Long cashierId) throws Exception {
		/**
		 * 1、获取商铺角色信息
		 * 2、绑定收银员角色关系
		 */
		
		//获取商铺角色信息
		RoleDto roleDto = new RoleDto();
		roleDto.setRoleType(3);//'O2O后台管理=1，店铺后台管理=2，收银=3，收单=4'
		roleDto.setShopId(shopId);
		roleDto.setUserstate(1);//'管理员角色是否禁用，''1''代表正常，''2''代表禁用',
		PageModel page = new PageModel();
		page.setPageSize(10);//页大小
		page.setToPage(1);//当前页
		List<RoleDto> roles = roleDao.getRole(roleDto, page);
		
		//绑定收银员角色
		if (CollectionUtils.isNotEmpty(roles)) {
			UserRoleDto userRoleDto = new UserRoleDto();
			RoleDto role = roles.get(0);
			
			userRoleDto.setRoleId(role.getId());
			userRoleDto.setUserId(cashierId);
			//'用户类型ID：1（user，user_id对应到1dcq_user表）,2(admin,user_id对应到1dcq_admin),3(employee,user_id对应到1dcq_shop_employee)'
			userRoleDto.setUserTypeId(3);
			userRoleDao.insertUserRole(userRoleDto);
		}
		
	}
	

	@Override
	public void delShopCashier(Long shopId, Long cashierId) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("cashierId", cashierId);
		shopCashierDao.deleteShopCashier(param);
		
	}


	@Override
	public void updateShop(ShopBean shopBean) throws Exception {
		
		//校验商铺是否存在
		ShopDto shopDto = shopDao.getShopById(shopBean.getShopId());
		if(shopDto == null){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
		}
		
		/*if(shopDto.getReferUserId()!=null && shopDto.getReferUserId().equals(shopBean.getUserId())){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "推荐会员不能是本人");
		}*/
		
		//校验会员是否存在
		Long num = userDao.authenUserById(shopBean.getUserId());
		CommonValidUtil.validPositLong(num, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
		// 处理商铺Logon
		dealPictureRelation(Long.valueOf(shopBean.getShopId()+""), String.valueOf(shopBean.getShopLogoId()), "1", "1",null,null);
		
		// 处理商铺环境图,传递空过来表示换进图全部删除，null表示没有修改
		if(shopBean.getShopSettingImgIds()!=null && shopBean.getShopSettingImgIds()==""){
			categoryDao.delAttachmentRelation(buildAttachmentRelation(Long.valueOf(shopBean.getShopId()+""), null,"1", "2",null));
		}                           
		dealPictureRelation(Long.valueOf(shopBean.getShopId()+""), shopBean.getShopSettingImgIds(), "1", "2",null,null);
		if(shopBean.getBusinessCertificatePicIds() != null){
			//处理商铺中经营许可证图片的关系
			dealPictureRelation(Long.valueOf(shopBean.getShopId()+""), String.valueOf(shopBean.getBusinessCertificatePicIds()), "1", "3","4",shopBean.getBusinessCertificates());
		}
		if(shopBean.getSkillsCertificatePicIds() != null){
			//处理商铺中个人技能证书图片的关系
			categoryDao.delAttachmentRelation(buildAttachmentRelation(shopBean.getUserId(), null,"2", "5",null));
			dealPictureRelation(shopBean.getUserId(), String.valueOf(shopBean.getSkillsCertificatePicIds()), "2", "5",null,shopBean.getSkillsCertificateNos());
		}
		// 处理身份证图片，身份证是保存在1dcq_user表里面
//		updateUserIdentity(shopBean);
//		if(shopDto.getAuditStatus()!=1){0
		if(shopBean.getReferMobileOrUserId()!=null){
			String refeUser=shopBean.getReferMobileOrUserId();
			UserDto refeUserDto = null;
			if(refeUser.length() > 10) {
				//代表是手机号码
				refeUserDto =userDao.getUserByMobile(refeUser);
			} else {
				//userId
				Long referUserId = NumberUtil.strToLong(refeUser, "refeUser");
				refeUserDto =userDao.getUserById(referUserId);
			}
			if(refeUserDto!=null){
				shopBean.setReferUserId(refeUserDto.getUserId());
				shopBean.setReferUserType(CommonConst.USER_TYPE_MEMBER);
			}
		}
//			userDao.updateUser(shopBean.getUserId(),shopBean.getIdentityCardNo());
		addShopAndColumnRelation(shopBean);
//		}
		if(isExistSameNameShop(shopDto.getPrincipalId(),shopBean.getShopId(),shopBean.getShopName())){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "该帐号已存在同名商铺");
		}
		//添加一张经营许可证信息进入店铺表
		shopBean.setBusinessCertificatePicIds(getOnefromArrayString(shopBean.getBusinessCertificatePicIds()));
		shopBean.setBusinessCertificates(getOnefromArrayString(shopBean.getBusinessCertificates()));
		// 更新商铺信息
        if(shopBean.getIs3In1()==null){
        	 shopBean.setIs3In1(0);
        }
        if(shopBean.getShopClassify()==null){
        	shopBean.setShopClassify(2);
        }
        //查询服务人员信息
		if(shopBean.getShopServeUserMobile()!=null){
			UserDto serverUserDto =userDao.getUserByMobile(shopBean.getShopServeUserMobile());
			CommonValidUtil.validObjectNull(serverUserDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_USER);
			shopBean.setShopServerUserId(serverUserDto.getUserId());
		}
		String identity = shopBean.getIdCardImgs();
		if(identity != null && StringUtils.isNotEmpty(identity) && identity.indexOf(",")>0){
			shopBean.setShopManagerIdentityCardPic1(identity.split(",")[0]);
			shopBean.setShopManagerIdentityCardPic2(identity.split(",")[1]);
//			shopBean.setPrincipalIdentityCardNo(shopBean.getPrincipalIdentityCardNo()==null?shopBean.getShopManagerIdentityCardNo():shopBean.getPrincipalIdentityCardNo());
//			shopBean.setPrincipalIdentityCardPicId1(shopBean.getPrincipalIdentityCardPicId1()==null?Integer.valueOf(identity.split(",")[0]):shopBean.getPrincipalIdentityCardPicId1());
//			shopBean.setPrincipalIdentityCardPicId2(shopBean.getPrincipalIdentityCardPicId2()==null?Integer.valueOf(identity.split(",")[1]):shopBean.getPrincipalIdentityCardPicId2());
		}
		shopDao.updateShop(shopBean);
	}
	


	/**
	 * 更新用户身份证图片
	 * @param shopBean
	 */
	private void updateUserIdentity(ShopBean shopBean){
		try {
			String identity = shopBean.getIdCardImgs();
			if(StringUtils.isNotEmpty(identity) && identity.indexOf(",")>0){
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("userId", shopBean.getUserId());
				map.put("card1", identity.split(",")[0]);
				map.put("card2", identity.split(",")[1]);
				if(shopBean.getIdentityCardNo() != null){
					map.put("identityCardNo", shopBean.getIdentityCardNo());
				}
				userDao.updateIdentity(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void addShopAndColumnRelation(ShopBean shopBean) throws Exception{
		// 先删关系
		columnDao.delColumnRelation(shopBean.getShopId());
		
		// 后增关系
		String columnIds = shopBean.getSubColumnId();
		if(StringUtils.isEmpty(columnIds)){
			logger.info("没有选择二级行业");
			return;
		}
		if(columnIds.indexOf(",") > 0){
			String[] ids = columnIds.split(",");
			for(int i=0; i < ids.length; i++){
				columnDao.addColumnRelation(shopBean.getShopId(), Integer.parseInt(ids[i]));
			}
		}else{
			columnDao.addColumnRelation(shopBean.getShopId(), Integer.parseInt(columnIds));
		}
			
	}


	@Override
	public List<Map> getShopCashiers(long shopId) throws Exception {
		return shopCashierDao.getShopCashiers(shopId);
	}


	@Override
	public UserDto getUser(String mobile) throws Exception {
		return userDao.getUserByMobile(mobile);
	}


	@Override
	public UserDto getUserByMobile(String refeUser) throws Exception {
		return this.userDao.getUserByMobile(refeUser);
	}


	@Override
	public UserDto getUserById(Long referUserId) throws Exception {
		return this.userDao.getUserById(referUserId);
	}


	@Override
	public boolean isExistSameShopName(String shopIdStr, String userIdStr,
			String shopName) throws Exception {
		if(shopIdStr != null){
			Long shopId = Long.valueOf(shopIdStr);
			ShopDto shopDto = shopDao.getShopByIdWithoutCache(shopId);
			CommonValidUtil.validObjectNull(shopDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		}
		Long userId = Long.valueOf(userIdStr);
		UserDto user = userDao.getUserById(userId);
		CommonValidUtil.validObjectNull(user, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_USER);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopIdStr);
		map.put("userId", userId);
		map.put("shopName", shopName);
		int count = shopDao.getShopByShopIdAndShopName(map);
		return count==0?false:true;
	}


	@Override
	public Map<String, Integer> getObjCountByCondition(Long shopId,
			String startTime, String endTime, String searchObject) {
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		List<String> StringList = Arrays.asList(searchObject.split(","));
		if(StringList.contains("1")){//查询需要进行生日提醒店内会员数
			Map<String,String> param = new HashMap<String, String>();
			param.put("shopId", shopId+"");
			if(null == startTime && null == endTime){
				startTime = DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
				endTime = DateUtils.format(DateUtils.addDays(new Date(), 6), DateUtils.DATE_FORMAT);
			}
			if(null != startTime && null != endTime){
				Date startTimeDate = DateUtils.parse(startTime,  DateUtils.DATE_FORMAT);
				Date endTimeDate = DateUtils.parse(endTime,  DateUtils.DATE_FORMAT);
				if(endTimeDate.getYear()-startTimeDate.getYear()==1){
					param.put("startTime", "2000-"+startTime.substring(5));
					param.put("continueStartTime", "2000-12-31");
					param.put("continueEndTime", "2000-01-01");
					param.put("endTime", "2000-"+endTime.substring(5));
				}else if(endTimeDate.getYear()-startTimeDate.getYear()== 0){
					param.put("startTime", "2000-"+startTime.substring(5));
					param.put("endTime", "2000-"+endTime.substring(5));
				}else  if(endTimeDate.getYear()-startTimeDate.getYear()>1){
					param.put("startTime", null);
					param.put("endTime", null);
				}
			}else if(null != startTime){
				param.put("startTime", "2000-"+startTime.substring(5));
			}else{
				param.put("endTime", "2000-"+endTime.substring(5));
			}
			param.put("memberStatus", CommonConst.MEMBER_STATUS_DELETE+"");
			List<Map<String,Integer>> list = shopMemberDao.queryBirthDayMemberNum(param);
			if(null != list && list.size() >0){
				resultMap.put("membersCount",list.size() );
				for (Map<String, Integer> map : list) {
					Integer isSendBirthdaySms = map.get("isSendBirthdaySms");
					if(null != isSendBirthdaySms && isSendBirthdaySms==0){
						resultMap.put("isUnSendMember",1 );
						break;
					}else{
						resultMap.put("isUnSendMember",0 );
					}
				}
				
			}else{
				resultMap.put("membersCount",0);
				resultMap.put("isUnSendMember",0);
			}
		}
		if(StringList.contains("2")){//查询需要进行预警库存商品数
			//getShopGoods?shopId=1234568099&goodsStatus=99&goodsType=1000&storeAlarmType=2&searchRange=1&pNo=1&pSize=10
			 ShopGoodsDto shopGoodsDto = new ShopGoodsDto();
			 shopGoodsDto.setShopId(shopId);
             shopGoodsDto.setGoodsStatus("99");
             shopGoodsDto.setGoodsType(1000);
             shopGoodsDto.setStorageAlarmType("2");
             shopGoodsDto.setSearchRange(1);
             PageModel pageModel = new PageModel();
             pageModel.setToPage(1);
             pageModel.setPageSize(100000);
             int count = shopGoodsDao.getCountShopGoods(shopGoodsDto,pageModel);
             resultMap.put("alarmGoodsCount", count);
		}
		
		return resultMap;
	}
	
	

}
