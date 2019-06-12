package com.idcq.idianmgr.service.goodsGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.pinyin.PinyinUtil;
import com.idcq.idianmgr.common.MgrCodeConst;
import com.idcq.idianmgr.common.MgrCommonConst;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupCategoryRelationDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupProValuesDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsGroupPropertyDao;
import com.idcq.idianmgr.dao.goodsGroup.IGoodsPropertyDao;
import com.idcq.idianmgr.dao.shop.ICategoryDao;
import com.idcq.idianmgr.dao.shop.IShopTechnicianRefGoodsDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupHandleDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupSimpleDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsPropertyDto;
import com.idcq.idianmgr.dto.goodsGroup.TmpGoodsDto;
import com.idcq.idianmgr.dto.goodsGroup.TmpGoodsGroupDto;
import com.idcq.idianmgr.dto.shop.CategoryDto;
import com.idcq.idianmgr.dto.shop.ShopTechnicianRefGoodsDto;
import com.idcq.idianmgr.dto.shop.TempCateGoryDto;
import com.idcq.idianmgr.dto.shop.TempGoodsPropertyDto;

@Service
public class GoodsGroupServiceImpl implements IGoodsGroupService {
	private final Logger logger = Logger.getLogger(GoodsGroupServiceImpl.class);
	
	@Autowired
	private IGoodsGroupPropertyDao goodsGroupPropertyDao;
	@Autowired
	private IGoodsGroupProValuesDao goodsGroupProValuesDao;
	@Autowired
	private IGoodsGroupDao goodsGroupDao;
	@Autowired
	private IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IGoodsGroupCategoryRelationDao goodsGroupCategoryRelationDao;
	@Autowired
	private IShopTechnicianRefGoodsDao shopTechnicianRefGoodsDao;
	@Autowired
	private IGoodsPropertyDao goodsPropertyDao;
	@Autowired
	private IGoodsDao goodsDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private ICategoryDao categoryDao;
	@Autowired
	private IAttachmentDao atteAttachmentDao;
	
	public List<Map> getGoodsGroupGoodsPrice(Long shopId, Long goodsGroupId)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("goodsGroupId", goodsGroupId);
		//TODO  校验shop是否正常
		
		//TODO 查询商品族下所有商品及价格
		List<Map> goodsList = goodsGroupDao.getGoodsListByGroupId(param);
		if (null == goodsList || goodsList.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP);
		}
		//TODO 查询商品下商品属性
		List<Map> goodsPropertys = goodsGroupDao.getGoodsPropertyListByGoodsIds(goodsList);
		
		List<Map> resultMap = null;
		if (goodsPropertys != null && goodsPropertys.size() > 0 ) {
			resultMap = new ArrayList<Map>();
			for(Map goods : goodsList){
				Long goodsId = CommonValidUtil.isEmpty(goods.get("goodsId"))?null:Long.parseLong(goods.get("goodsId")+"");
				String goodsProsValueIds = "";
				String proValues = "";
				Iterator<Map> ite = goodsPropertys.iterator();
				while(ite.hasNext()){
					Map property = ite.next();
					Long proGoodsId = CommonValidUtil.isEmpty(property.get("goods_id"))?null:Long.parseLong(property.get("goods_id")+"");
					if (goodsId != null && proGoodsId != null &&  goodsId.longValue() == proGoodsId.longValue()) {
						Long proValuesId = CommonValidUtil.isEmpty(property.get("pro_values_id"))?null:Long.parseLong(property.get("pro_values_id")+"");
						String proValue = (String) property.get("pro_value");
						goodsProsValueIds = goodsProsValueIds+(proValuesId+",");
						proValues = proValues+(proValue+"+");
						ite.remove();
					}
				}
				if (!StringUtils.isBlank(goodsProsValueIds)) {
					goods.put("goodsProsValueIds", goodsProsValueIds.substring(0,goodsProsValueIds.length()-1));
				}
				if (!StringUtils.isBlank(proValues)) {
					goods.put("proValues", proValues.substring(0,proValues.length()-1));
				}
				goods.put("goodsGroupId", goodsGroupId);
				resultMap.add(goods);
			}
		}
		return resultMap;
	}
	@Override
	public List getGoodsGroupPros(Map param) throws Exception {
		List resultList=new ArrayList();
		Map temp=null;
		List<GoodsGroupPropertyDto> goodsGroupProList=goodsGroupPropertyDao.getGoodsGroupProList(param);
		GoodsGroupPropertyDto ggpd=null;
		List<GoodsGroupProValuesDto> goodsGroupProValuesList=null;
		for(int i=0,len=goodsGroupProList.size();i<len;i++){
			ggpd=goodsGroupProList.get(i);
			param.put("groupPropertyId", ggpd.getGroupPropertyId());
			temp=new HashMap();
			temp.put("groupPropertyId", ggpd.getGroupPropertyId());
			temp.put("groupPropertyName", ggpd.getGroupPropertyName());
			temp.put("goodsGroupId", ggpd.getGoodsGroupId());
			goodsGroupProValuesList=goodsGroupProValuesDao.getGoodsGroupProValuesList(param);
			temp.put("groupProValues",goodsGroupProValuesList);
			resultList.add(temp);
		}
		return resultList;
	}
	
	@Override
	public void delGroupPro(Map param) throws Exception {
		Integer bizType=Integer.parseInt((String) param.get("bizType"));
		Integer bizId=Integer.parseInt((String) param.get("bizId"));
		if(bizType==1){
			goodsGroupProValuesDao.delGoodsGroupProValue(bizId);
			param.put("proValuesId", bizId);
			goodsPropertyDao.delGoodsProperty(param);
		}else{
			goodsGroupPropertyDao.delGoodsGroupProperty(bizId);
			param.put("groupPropertyId", bizId);
			goodsPropertyDao.delGoodsProperty(param);
		}
	}

	@Override
	public Long operateGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception {
		Long shopId = goodsGroupPropertyDto.getShopId();
		Long ggId = goodsGroupPropertyDto.getGoodsGroupId();
		String name=goodsGroupPropertyDto.getGroupPropertyName();
		Long gpId = goodsGroupPropertyDto.getGroupPropertyId();
		Integer opType = goodsGroupPropertyDto.getOperateType();
		// 数据校验
		// 商铺ID必填及存在性
		CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
		CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		Integer flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 商品族ID必填及存在性
		CommonValidUtil.validLongNull(ggId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_ID);
		CommonValidUtil.validPositLong(ggId, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_GG_ID);
		flag = this.goodsGroupDao.queryGoodsGroupExists(ggId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG);
		// 商品族属性名必填
		CommonValidUtil.validStrNull(name,CodeConst.CODE_PARAMETER_NOT_NULL,MgrCodeConst.MSG_FM_ERROR_GG_NAME);
		
		int operateType=0;
		if(null != opType){
			operateType=opType;
		}
		
		if(operateType==1){//修改
			CommonValidUtil.validLongNull(gpId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_PRO_ID);
			CommonValidUtil.validPositLong(gpId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_FM_ERROR_GG_PRO_ID);
			Map param=new HashMap();
			param.put("groupPropertyId", gpId);
			GoodsGroupPropertyDto g=goodsGroupPropertyDao.getGoodsGroupProperty(param);
			CommonValidUtil.validObjectNull(g, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_GOODS_GROUP_PRO_NOT_EXISTS);
			goodsGroupPropertyDao.updateGoodsGroupPro(goodsGroupPropertyDto);
		}else{
			
//			CommonValidUtil.validStrNull(goodsGroupPropertyDto.getGoodsGroupId().toString(),CodeConst.CODE_PARAMETER_NOT_VALID,MgrCodeConst.MSG_FM_ERROR_GG_ID);
			goodsGroupPropertyDao.insertGoodsGroupPro(goodsGroupPropertyDto);
		}
		Long result=goodsGroupPropertyDto.getGroupPropertyId();
		return result;
	}

	@Override
	public Long operateGoodsGroupProVal(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception {
		Long shopId = goodsGroupProValuesDto.getShopId();
		Long ggId = goodsGroupProValuesDto.getGoodsGroupId();
		String name=goodsGroupProValuesDto.getProValue();
		Long gpId = goodsGroupProValuesDto.getGroupPropertyId();
		Integer operateType = goodsGroupProValuesDto.getOperateType();
		// 数据校验
		// 商铺ID必填及存在性
		CommonValidUtil.validLongNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
		CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		Integer flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 商品族ID必填及存在性
		CommonValidUtil.validLongNull(ggId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_ID);
		CommonValidUtil.validPositLong(ggId, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_GG_ID);
		flag = this.goodsGroupDao.queryGoodsGroupExists(ggId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG);
		//商品族属性ID校验
		CommonValidUtil.validLongNull(gpId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_BIZ_ID_NOT_NULL);
		// 商品族属性值名称必填
		CommonValidUtil.validStrNull(name,CodeConst.CODE_PARAMETER_NOT_NULL,MgrCodeConst.MSG_FM_ERROR_GGV_NAME);
		if(null == operateType){
			operateType=0;
		}
		if(operateType==1){//修改
			if(null==goodsGroupProValuesDto.getProValuesId()){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,MgrCodeConst.MSG_GOODS_GROUP_PRO_VALUE_ID_NOT_NULL);
			}
			Map param=new HashMap();
			param.put("proValuesId", goodsGroupProValuesDto.getProValuesId());
			GoodsGroupProValuesDto g=goodsGroupProValuesDao.getGoodsGroupProValues(param);
			CommonValidUtil.validObjectNull(g, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_GOODS_GROUP_PRO_VALUE_NOT_EXISTS);
			goodsGroupProValuesDao.updateGoodsGroupProValue(goodsGroupProValuesDto);
		}else{
			goodsGroupProValuesDao.insertGoodsGroupProValue(goodsGroupProValuesDto);
		}
		Long result=goodsGroupProValuesDto.getProValuesId();
		return result;
	}
	
	@Override
	public Long operateGoodsGroup(GoodsGroupHandleDto goodsGroupHandleDto)
			throws Exception {
		// 数据校验
		this.operaGoodsGroupValid(goodsGroupHandleDto);
		Long shopId = goodsGroupHandleDto.getShopId(); 
		Long goodsGroupId = goodsGroupHandleDto.getGoodsGroupId();
		Integer operateType = goodsGroupHandleDto.getOperateType();
		String goodsName = goodsGroupHandleDto.getGoodsName();
		Double servicePrice = goodsGroupHandleDto.getServicePrice();
		Integer goodsServerMode = goodsGroupHandleDto.getGoodsServerMode();
		Double workTime = goodsGroupHandleDto.getWorkTime();
		Integer keepDay = goodsGroupHandleDto.getKeepDay();
		String goodsDesc = goodsGroupHandleDto.getGoodsDesc();
		String attachementIds = goodsGroupHandleDto.getAttachementIds();
		String goodsCategoryIds = goodsGroupHandleDto.getGoodsCategoryIds();
		String techIds = goodsGroupHandleDto.getTechIds();
		Long goodsLogoId = goodsGroupHandleDto.getGoodsLogoId();
		
		// 封装数据
		// 商品族
		GoodsGroupDto group = new GoodsGroupDto();
		group.setGoodsGroupId(goodsGroupId);
		group.setShopId(shopId);
		group.setGoodsLogoId(goodsLogoId);
		group.setGoodsServerMode(goodsServerMode);
		group.setGoodsName(goodsName);
		group.setGoodsDesc(goodsDesc);
		group.setCreateTime(new Date());
		group.setLastUpdateTime(new Date());
		group.setMaxPrice(new BigDecimal(servicePrice));
		group.setMinPrice(new BigDecimal(servicePrice));
		group.setUnitId(0L);
		group.setPinyincode(PinyinUtil.getPinYinHeadChar(goodsName));
		group.setUseTime(workTime);
		group.setKeepTime(keepDay);
		// 附件表（轮播图）
		List<AttachmentRelationDto> attRelaList = new ArrayList<AttachmentRelationDto>();
		// 附件表（缩略图）
		List<AttachmentRelationDto> logo = new ArrayList<AttachmentRelationDto>();
		String[] ids = null;
		int flag = 0;
		if(!StringUtils.isBlank(attachementIds)){
			ids = attachementIds.split(",");
			int i = 0;
			for(String e : ids){
				if(!StringUtils.isBlank(e)){
					// 检查附件ID合法性
					CommonValidUtil.validPositLong(e, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_EEROR_GG_ATTEM_ID);
					CommonValidUtil.validObjectNull(this.atteAttachmentDao.queryAttachmentById(Long.valueOf(e)), 
							CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG_ATTEM);
					i++;
					AttachmentRelationDto attRela = new AttachmentRelationDto();
					attRela.setAttachmentId(Long.valueOf(e));
					attRela.setBizType(9);
					attRela.setPicType(2);
					attRela.setBizIndex(i);
					
					attRelaList.add(attRela);
				}
			}
		}
		// 附件表（缩略图）
		CommonValidUtil.validObjectNull(this.atteAttachmentDao.queryAttachmentById(goodsLogoId), 
				CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG_ATTEM);
		AttachmentRelationDto LogoAttRela = new AttachmentRelationDto();
		LogoAttRela.setAttachmentId(goodsLogoId);
		LogoAttRela.setBizType(9);
		LogoAttRela.setPicType(1);
		LogoAttRela.setBizIndex(1);
		logo.add(LogoAttRela);
		// 服务分类
		List<GoodsGroupCategoryRelationDto> ggcrList = new ArrayList<GoodsGroupCategoryRelationDto>();
		if(!StringUtils.isBlank(goodsCategoryIds)){
			ids = goodsCategoryIds.split(",");
			int i = 0;
			for(String e : ids){
				if(!StringUtils.isBlank(e)){
					// 检查服务分类ID合法性
					CommonValidUtil.validPositLong(e, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_EEROR_GG_CATEGORY_ID);
					flag = this.goodsGroupCategoryRelationDao.queryGoodsCategoryExists(Long.valueOf(e));
					CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG_CATEGORY);
					i++;
					GoodsGroupCategoryRelationDto ggcr = new GoodsGroupCategoryRelationDto();
					ggcr.setGroupCategoryId(Long.valueOf(e));
					ggcr.setParentCategoryId(this.goodsGroupCategoryRelationDao.getParentGoodsCategoryId(Long.valueOf(e)));
					ggcr.setCreateTime(new Date());
					ggcr.setCrIndex(i);
					ggcr.setCrStatus(1);
				
					ggcrList.add(ggcr);
				}
			}
		}
		// 技师
		List<ShopTechnicianRefGoodsDto> stList = new ArrayList<ShopTechnicianRefGoodsDto>();
		if(!StringUtils.isBlank(techIds)){
			ids = techIds.split(",");
			int i = 0;
			for(String e : ids){
				if(!StringUtils.isBlank(e)){
					// 检查技师ID合法性
					CommonValidUtil.validPositLong(e, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_EEROR_GG_TECH_ID);
					flag = this.goodsGroupDao.queryTechniExists(Long.valueOf(e));
					CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG_TECH);
					i++;
					ShopTechnicianRefGoodsDto st = new ShopTechnicianRefGoodsDto();
					st.setTechnicianId(Long.valueOf(e));
					stList.add(st);
				}
			}
		}
		// 入库
		if(operateType == CommonConst.GGROUP_OP_TYPE_XZ){//新增
			if(goodsGroupId != null && goodsGroupId > 0){
				// 删除旧的记录
				this.goodsGroupDao.delGoodsGroupById(goodsGroupId);
			}
			group.setGoodsGroupId(null);
			// 商品族入库
			goodsGroupId = this.goodsGroupDao.addGoodsGroup(group);
			// 附件入库（轮播图）
			if(attRelaList != null && attRelaList.size() > 0){
				this.setGoodsGroupIdOfAttaRela(attRelaList, goodsGroupId);
				this.attachmentRelationDao.addAttachmentRelationBatch(attRelaList);
			}
			// logo入库（缩略图）
			attRelaList = new ArrayList<AttachmentRelationDto>();
			attRelaList.add(LogoAttRela);
			this.setGoodsGroupIdOfAttaRela(attRelaList, goodsGroupId);
			this.attachmentRelationDao.addAttachmentRelationBatch(attRelaList);
			// 服务分类入库
			if(ggcrList != null && ggcrList.size() > 0){
				this.setGoodsGroupIdOfGroupCategoryRela(ggcrList, goodsGroupId);
				this.goodsGroupCategoryRelationDao.addGoodsGroupCategoryRelationBatch(ggcrList);
			}
			// 技师入库
			if(stList != null && stList.size() > 0){
				this.setGoodsGroupIdOfTechRefGoods(stList,goodsGroupId);
				this.shopTechnicianRefGoodsDao.addShopTechnicianRefGoodsBatch(stList);
			}
		}else{//修改
			// 商品族ID必填
			CommonValidUtil.validLongNull(goodsGroupId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_ID);
			CommonValidUtil.validPositLong(goodsGroupId, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_GG_ID);
			// 修改时检查改商品族有没关联商品族属性，若有则不修改商品族最小和最大值
			int proValNum = this.goodsGroupDao.queryGgPropertyByGgId(goodsGroupId);
			if(NumberUtil.isPositInt(proValNum)){
				group.setMinPrice(null);
				group.setMaxPrice(null);
			}
			// 商品族入库
			this.goodsGroupDao.updateGoodsGroup(group);
			// 附件入库
			if(attRelaList != null && attRelaList.size() > 0){
				// 封装轮播图信息列表
				this.setGoodsGroupIdOfAttaRela(attRelaList, goodsGroupId);
				// 删除旧的轮播图
				this.attachmentRelationDao.delAttachmentRelationByCondition(goodsGroupId,CommonConst.BIZ_TYPE_IS_GOODS_GROUP,
						CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
				// 新的轮播图入库
				this.attachmentRelationDao.addAttachmentRelationBatch(attRelaList);
			}
			if(logo != null && logo.size() > 0){
				// 封装缩略图信息
				this.setGoodsGroupIdOfAttaRela(logo, goodsGroupId);
				// 删除旧的缩略图
				this.attachmentRelationDao.delAttachmentRelationByCondition(goodsGroupId,CommonConst.BIZ_TYPE_IS_GOODS_GROUP,
						CommonConst.PIC_TYPE_IS_SUONUE);
				// 新的缩略图入库
				this.attachmentRelationDao.addAttachmentRelationBatch(logo);
			}
			// 服务分类入库
			if(ggcrList != null && ggcrList.size() > 0){
				this.setGoodsGroupIdOfGroupCategoryRela(ggcrList, goodsGroupId);
				this.goodsGroupCategoryRelationDao.delGoodsGroupCategoryRelationByGgId(goodsGroupId);
				this.goodsGroupCategoryRelationDao.addGoodsGroupCategoryRelationBatch(ggcrList);
			}
			// 技师入库
			if(stList != null && stList.size() > 0){
				this.setGoodsGroupIdOfTechRefGoods(stList,goodsGroupId);
				this.shopTechnicianRefGoodsDao.delShopTechnicianRefGoodsByGgId(goodsGroupId);
				this.shopTechnicianRefGoodsDao.addShopTechnicianRefGoodsBatch(stList);
			}else{//技师列表为空则删除旧的技师列表
				this.shopTechnicianRefGoodsDao.delShopTechnicianRefGoodsByGgId(goodsGroupId);
			}
			// 重新绑定商品族下关联商品的轮播图和缩略图
			updateGoodsListOfGg(group,attRelaList,logo,servicePrice);
		}
		return goodsGroupId;
	}
	
	/**
	 * 修改商品族时同步更新商品列表部分属性
	 * <p>步骤：</p>
	 * 1,同步商品族名称等基本信息到商品列表;<br>
	 * 2,给商品列表重新绑定轮播图;<br>
	 * 3,给商品列表重新轮播图。
	 * @param gg	商品族
	 * @param ggPicList	商品族轮播图列表
	 * @param ggLogo	商品族缩略图列表
	 * @return
	 * @throws Exception
	 */
	private int updateGoodsListOfGg(GoodsGroupDto gg,List<AttachmentRelationDto> ggPicList,
			List<AttachmentRelationDto> ggLogo,Double servicePrice) throws Exception{
		// 获取商品族关联商品的ID集合
		List<Object> list = this.goodsDao.getGoodsIdListOfGg(gg.getGoodsGroupId());
		// 批量同步基本信息
		GoodsDto goods = new GoodsDto();
		if(list != null && list.size() == 1){
			goods.setStandardPrice((servicePrice == null || servicePrice.doubleValue() <= 0) ? null : servicePrice.doubleValue());
		}
		goods.setGoodsGroupId(gg.getGoodsGroupId());
		goods.setGoodsDesc(gg.getGoodsDesc());
		goods.setUseTime(gg.getUseTime());
		goods.setKeepTime(gg.getKeepTime());
		goods.setGoodsServerMode(gg.getGoodsServerMode());
		goods.setGoodsName(gg.getGoodsName());
		this.goodsDao.syncGoodsInfoOfGg(goods);
		
		List<Long> goodsIdList = new ArrayList<Long>();
		getIdList(list,goodsIdList);
		// 批量更新轮播图
		reBindAttach(ggPicList, goodsIdList, CommonConst.BIZ_TYPE_IS_GOODS, CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
		// 批量更新缩略图
		reBindAttach(ggLogo, goodsIdList, CommonConst.BIZ_TYPE_IS_GOODS, CommonConst.PIC_TYPE_IS_SUONUE);
		// 批量更新商品名称
		updateGoodsNameBatch(goodsIdList,gg.getGoodsName());
		return 1;
	}
	
	private void getIdList(List<Object> list , List<Long> goodsIdList) throws Exception{
		if(list != null && list.size() > 0){
			for(Object e : list){
				goodsIdList.add((Long)e);
			}
		}
	}
	
	/**
	 * 批量修改商品名称
	 * @param gIdList
	 * @throws Exception
	 */
	private void updateGoodsNameBatch(List<Long> gIdList,String ggName) throws Exception{
		// 更新商品名称，格式：商品族名称（商品属性名称+商品属性名称）
		List<Map<String,Object>> goodsNameList = new ArrayList<Map<String,Object>>();
		String proVal = null;
		List<Map> gProList = null;
		StringBuilder goodsName = null;
		for(Long e : gIdList){
			gProList = this.goodsPropertyDao.getGoodsProListByGoodsId(e);
			if(gProList != null && gProList.size() > 0){
				goodsName = new StringBuilder(ggName);
				goodsName.append(" ");
				for(Map g : gProList){
					proVal = (String)g.get("proValue");
					goodsName.append(proVal);
					goodsName.append("+");
				}
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("goodsId", e);
				map.put("goodsName", goodsName.substring(0,goodsName.lastIndexOf("+")));
				goodsNameList.add(map);
			}
		}
		if(goodsNameList != null && goodsNameList.size() > 0){
			this.goodsDao.updateGoodsNameBatch(goodsNameList);
		}
	}
	
	private void reBindAttach(List<AttachmentRelationDto> attachList,
			List<Long> goodsIdList,Integer bizType,Integer picType) throws Exception {
		if(goodsIdList != null && goodsIdList.size() > 0){
			List<AttachmentRelationDto> goodsPicList = new ArrayList<AttachmentRelationDto>();
			for(Long e : goodsIdList){
				goodsPicList.addAll(setGoodsIdOfAttaRela(attachList,e,picType));
			}
			// 批量删除旧的附件列表
			this.attachmentRelationDao.delAttachmentRelationByConditionBatch(goodsIdList, bizType, picType);
			// 批量保存新的附件列表
			this.attachmentRelationDao.addAttachmentRelationBatch(goodsPicList);
		}
	}
	
	
	/**
	 * 新增/修改商品族校验
	 * @param goodsGroupHandleDto
	 * @throws Exception
	 */
	private void operaGoodsGroupValid(GoodsGroupHandleDto goodsGroupHandleDto) throws Exception{
		Long shopId = goodsGroupHandleDto.getShopId(); 
		Integer operateType = goodsGroupHandleDto.getOperateType();
		String goodsName = goodsGroupHandleDto.getGoodsName();
		Double servicePrice = goodsGroupHandleDto.getServicePrice();
		Integer goodsServerMode = goodsGroupHandleDto.getGoodsServerMode();
		String attachementIds = goodsGroupHandleDto.getAttachementIds();
		String goodsCategoryIds = goodsGroupHandleDto.getGoodsCategoryIds();
		Long goodsLogoId = goodsGroupHandleDto.getGoodsLogoId();
		// 操作类型
		if(operateType == null){
			goodsGroupHandleDto.setOperateType(0);
		}
		// 商铺ID必填及存在性
		CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
		Integer flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 服务名称必填
		CommonValidUtil.validStrNull(goodsName, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_NAME);
		// 服务价格必填
		CommonValidUtil.validDoubleNullZero(servicePrice, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_SERVICE_PRICE);
		// 服务方式必填
		CommonValidUtil.validIntNull(goodsServerMode, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_SERVER_MODEL);
		// 附件IDS必填及存在性
		CommonValidUtil.validStrNull(attachementIds, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_ATTEM_ID);
		// 服务分类IDS必填及存在性
		CommonValidUtil.validStrNull(goodsCategoryIds, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_CATEGORY_ID);
		// logo必填
		CommonValidUtil.validPositLong(goodsLogoId, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_LOGO_ID);
		
	}
	//TODO ...
	public int updateGoodsPro(TmpGoodsGroupDto tmpGoodsGroupDto) throws Exception {
		String goodsGroupIdStr = tmpGoodsGroupDto.getGoodsGroupId();
		String shopIdStr = tmpGoodsGroupDto.getShopId();
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
		CommonValidUtil.validStrNull(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_GOODSGROUP_ID);
		Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		Long goodsGroupId = CommonValidUtil.validStrLongFmt(goodsGroupIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_GOODSGROUP_ID);
		//根据商品族编号查询商品族信息
		GoodsGroupDto goodsGroupDto = goodsGroupDao.getGoodsGroupById(goodsGroupId);
		if (null == goodsGroupDto) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP);
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("goodsGroupId", goodsGroupId);
		//1.请求参数商品数据集合（商品价格、商品编号、商品属性数据）
		List<TmpGoodsDto> tmpGoodsDtos = tmpGoodsGroupDto.getGoods();
		//查询当前商品族内所有商品编号集合
		List<Long> dbGoodsIds = goodsGroupDao.getGoodsIdsByGroupIdAndShopId(param);
		if (null == dbGoodsIds || dbGoodsIds.size() <= 0) {
			//throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP_GOODS);
			dbGoodsIds = new ArrayList<Long>();
		}
		//需要删除的商品编号集合
		List<Long> delGoodsIds = new ArrayList<Long>();
		//需要直接添加的数据集
		List<TmpGoodsDto> addGoodsDtos = new ArrayList<TmpGoodsDto>();
		//需要修改的数据集
		List<TmpGoodsDto> updGoodsDtos = new ArrayList<TmpGoodsDto>();
		List<Long> updGoodsIds = new ArrayList<Long>();
		//最高价格跟最低价格
		Double minPrice = 0d;
		Double maxPrice = 0d;
		boolean flag = true;
		//2.进行比对，将需要新增、修改、删除的数据分离
		for(TmpGoodsDto dto : tmpGoodsDtos){
			Long goodsId = dto.getGoodsId();
			Double price = dto.getGoodsPrice();
			if (null == price) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PRICE);
			}
			//属性值编号集合
			String goodsProsValueIds = dto.getGoodsProsValueIds();
			CommonValidUtil.validStrNull(goodsProsValueIds, CodeConst.CODE_PARAMETER_NOT_NULL, "属性值编号不能为空");
			if (null != goodsId) {
				if (dbGoodsIds.contains(goodsId)) {
					//相等的，则需要添加到需要修改的集合中
					updGoodsDtos.add(dto);
					//已经需要修改的商品。。。
					updGoodsIds.add(goodsId);
				}else{
					//有上传商品编号，但是跟数据库中比对不上，则认为无效，需要新增
					addGoodsDtos.add(dto);
				}
			}else{
				//上传参数中没有goodsId的数据，添加到需要直接新增的集合中
				addGoodsDtos.add(dto);
			}
			//获取最高最低价格
			if (flag) {
				minPrice = price;
				maxPrice = price;
				flag = false;
			}
			if (price < minPrice) {
				minPrice = price;
			}
			if (price > maxPrice) {
				maxPrice = price;
			}
		}
		for(Long dbGoodsId : dbGoodsIds){
			if (!updGoodsIds.contains(dbGoodsId)) {
				delGoodsIds.add(dbGoodsId);
			}
		}
		//商品族属性值编号集合，此集合中的数据都需要将is_select置为选中
		List<Long> goodsGroupProValuesIds = new ArrayList<Long>();
		//新增
		//1.查询商品族信息，将商品族的信息copy给每一份商品
		//2.添加商品
		//3.添加商品属性
		List<GoodsPropertyDto> goodsPropertyDtos = new ArrayList<GoodsPropertyDto>();
		if (addGoodsDtos.size() > 0) {
			for(TmpGoodsDto dto : addGoodsDtos){
				GoodsDto goodsDto = new GoodsDto();
				//将商品族属性复制给商品
				DataConvertUtil.propertyConvert(goodsGroupDto, goodsDto, MgrCommonConst.GOODSGROUP_GOODS_FIELD);
				//PropertyUtils.copyProperties(goodsDto, goodsGroupDto);
				//设置商品属性数据
				goodsDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
				goodsDto.setStandardPrice(dto.getGoodsPrice());
				goodsDto.setGoodsStatus(CommonConst.GOODS_STATUS_SJ);//默认草稿状态
				goodsDto.setGoodsType(CommonConst.GOODS_TYPE_SERVICE);//商品类型：实物-1000,服务-2000,套餐-3000
				goodsDto.setDiscountPrice(0d);//折扣价
				goodsDto.setVipPrice(0d);//会员价
				goodsDto.setSequence(1);
				goodsDto.setUnitId(goodsGroupDto.getUnitId() == null?0:goodsGroupDto.getUnitId());
				goodsDto.setCreateTime(new Date());
				//属性值编号集合
				String goodsProsValueIds = dto.getGoodsProsValueIds();
				String proValues = "";
				List<GoodsPropertyDto> tmpGoodsPropertyDtos = new ArrayList<GoodsPropertyDto>();
				if (!StringUtils.isBlank(goodsProsValueIds)) {
					String[] strs = goodsProsValueIds.split(",");
					for (int i = 0; i < strs.length; i++) {
						Long proValuesId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_GOODSGROUP_RPO_VALUEID);
						Map<String,Object> param1 = new HashMap<String, Object>();
						param1.put("proValuesId", proValuesId);
						GoodsGroupProValuesDto ggpvDto = goodsGroupProValuesDao.getGoodsGroupProValues(param1);
						if (ggpvDto != null) {
							proValues = proValues+ggpvDto.getProValue()+"+";
						}
						//根据属性值编号查询属性编号
						GoodsGroupPropertyDto ggpDto = goodsGroupPropertyDao.getGoodsGroupPropertyByProValuesId(proValuesId);
						if (null != ggpDto) {
							GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
							goodsPropertyDto.setGroupPropertyId(ggpDto.getGroupPropertyId());
							goodsPropertyDto.setProValuesId(proValuesId);
							tmpGoodsPropertyDtos.add(goodsPropertyDto);
							
							if (!goodsGroupProValuesIds.contains(proValuesId)) {
								goodsGroupProValuesIds.add(proValuesId);
							}
						}
					}
				}
				String goodsName = goodsDto.getGoodsName();
				if (!StringUtils.isBlank(goodsName)) {
					//设置商品pinyinCode
					goodsDto.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
					goodsName = (goodsName+" ");
				}else{
					goodsName = "";
				}
				if (!"".equals(proValues)) {
					//设置商品名称：商品族名称+属性值名称
					goodsDto.setGoodsName(goodsName+(proValues.substring(0, proValues.length()-1)));
				}
				goodsDto.setLastUpdateTime(new Date());
				goodsDto.setGoodsDesc(goodsDto.getGoodsDesc() == null?goodsName:goodsDto.getGoodsDesc());
				goodsDto.setGoodsDetailDesc(goodsDto.getGoodsDetailDesc() == null?goodsName:goodsDto.getGoodsDetailDesc());
				//需要先保存商品信息，返回商品编号
				goodsDao.addGoodsDto(goodsDto);
				if (tmpGoodsPropertyDtos.size() > 0) {
					for(GoodsPropertyDto goodsPropertyDto:tmpGoodsPropertyDtos){
						goodsPropertyDto.setGoodsId(goodsDto.getGoodsId());
						goodsPropertyDtos.add(goodsPropertyDto);
					}
				}
				goodsDto = null;
				tmpGoodsPropertyDtos = null;
			}
		}
		//修改
		//1.修改商品价格
		//2.删除商品属性
		//3.重新添加商品属性
		if (updGoodsDtos.size() > 0) {
			List<Long> tmpGoodsIds = new ArrayList<Long>();
			for(TmpGoodsDto dto : updGoodsDtos){
				Long goodsId = dto.getGoodsId();
				//修改商品价格
				GoodsDto goodsDto = new GoodsDto();
				goodsDto.setGoodsId(goodsId);
				goodsDto.setStandardPrice(dto.getGoodsPrice());
				goodsDao.updateGoodsStandardPrice(goodsDto);
				//属性值编号集合
				String goodsProsValueIds = dto.getGoodsProsValueIds();
				if (!StringUtils.isBlank(goodsProsValueIds)) {
					String[] strs = goodsProsValueIds.split(",");
					for (int i = 0; i < strs.length; i++) {
						Long proValuesId = CommonValidUtil.validStrLongFmt(strs[i], CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_GOODSGROUP_RPO_VALUEID);
						//根据属性值编号查询属性编号
						GoodsGroupPropertyDto ggpDto = goodsGroupPropertyDao.getGoodsGroupPropertyByProValuesId(proValuesId);
						if (null != ggpDto) {
							GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
							goodsPropertyDto.setGoodsId(goodsId);
							goodsPropertyDto.setGroupPropertyId(ggpDto.getGroupPropertyId());
							goodsPropertyDto.setProValuesId(proValuesId);
							goodsPropertyDtos.add(goodsPropertyDto);
							
							if (!goodsGroupProValuesIds.contains(proValuesId)) {
								goodsGroupProValuesIds.add(proValuesId);
							}
						}
					}
				}
				tmpGoodsIds.add(goodsId);
			}
			if (tmpGoodsIds.size() > 0) {
				goodsPropertyDao.batchDelGoodsPropertyByGoodsId(tmpGoodsIds);
			}
		}
		//批量保存商品属性信息（新增、修改中产生的新的商品属性记录）
		if (goodsPropertyDtos.size() > 0) {
			goodsPropertyDao.batchAddGoodsProperty(goodsPropertyDtos);
		}
		//删除
		//1.将商品逻辑删除[上架状态，下架-0,上架-1，删除-2]
		//2.物理删除商品属性
		if (delGoodsIds.size() > 0) {
			goodsDao.batchUpdateGoodsById(delGoodsIds);
			goodsPropertyDao.batchDelGoodsPropertyByGoodsId(delGoodsIds);
		}
		
		if (goodsGroupProValuesIds.size() > 0) {
			//先将商品族属性值的is_select全部设为0
			goodsGroupProValuesDao.updateIsSelectEqZero(goodsGroupId);
			//将选中的商品族属性值的is_select设为1，选中
			goodsGroupProValuesDao.updateIsSelectByIds(goodsGroupProValuesIds);
		}
		//TODO  将商品族中所有的商品属性跟属性值查询出来，后续需要根据属性值查询属性的操作，都从此结果集中查询，避免多次请求数据库
		//修改商品族最高最低价格，并将商品族的状态置为草稿
		GoodsGroupDto ggDto = new GoodsGroupDto();
		ggDto.setGoodsGroupId(goodsGroupId);
		ggDto.setMinPrice(new BigDecimal(minPrice+""));
		ggDto.setMaxPrice(new BigDecimal(maxPrice+""));
		goodsGroupDao.updateGoodsGroup(ggDto);
		refreshGroupGoodsCache(shopId,goodsGroupId);
		return 0;
	}
	
	public void refreshGroupGoodsCache(final Long shopId,final Long goodsGroupId){
		new Thread(){
			@Override
			public void run() {
				logger.info("开始刷新商品族及族内商品缓存--start");
				try {
					if(null != shopId){
						String ggKey = CommonConst.KEY_SHOP_TOP_GOODS+shopId;
						DataCacheApi.del(ggKey);
					}
					if(null != goodsGroupId){
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("shopId", shopId);
						param.put("goodsGroupId", goodsGroupId);
						List<Long> goodsIds = goodsGroupDao.getGoodsIdsByGroupIdAndShopId(param);
						if (goodsIds != null && goodsIds.size() > 0) {
							for(Long goodsId : goodsIds)
							{
								String key = CommonConst.KEY_GOODS+goodsId;
								DataCacheApi.del(key);
							}
						}
					}
				} catch (Exception e) {
					logger.info("刷新商品族及商品缓存异常",e);
				}
			}
		}.start();
	}
	
	@Override
	public GoodsGroupDto findGoodsGroupByGroupId(Long groupId) throws Exception {
		// TODO Auto-generated method stub
		return goodsGroupDao.getGoodsGroupById(groupId);
	}

	private void setGoodsGroupIdOfAttaRela(List<AttachmentRelationDto> attRelaList,Long ggId) throws Exception{
		for(AttachmentRelationDto e : attRelaList){
			e.setBizId(Long.valueOf(ggId+""));
		}
	}
	
	
	/**
	 * 设置商品轮播图附件关联
	 * @param attRelaList
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	private List<AttachmentRelationDto> setGoodsIdOfAttaRela(List<AttachmentRelationDto> attRelaList,
			Long goodsId,Integer picType) throws Exception{
		List<AttachmentRelationDto> attRelaList2 = new ArrayList<AttachmentRelationDto>();
		for(AttachmentRelationDto e : attRelaList){
			AttachmentRelationDto attRela = new AttachmentRelationDto();
			PropertyUtils.copyProperties(attRela, e);
			attRela.setBizId(Long.valueOf(goodsId+""));
			attRela.setBizType(CommonConst.BIZ_TYPE_IS_GOODS);
			attRela.setPicType(picType);
			attRelaList2.add(attRela);
		}
		return attRelaList2;
	}
	private void setGoodsGroupIdOfGroupCategoryRela(List<GoodsGroupCategoryRelationDto> ggcrList,Long ggId) throws Exception{
		for(GoodsGroupCategoryRelationDto e : ggcrList){
			e.setGoodsGroupId(ggId);
		}
	}
	private void setGoodsGroupIdOfTechRefGoods(List<ShopTechnicianRefGoodsDto> stList,Long ggId) throws Exception{
		for(ShopTechnicianRefGoodsDto e : stList){
			e.setGoodsGroupId(ggId);
		}
	}
	public int updateGoodsGroupStatus(HttpServletRequest request) throws Exception {
		String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
		String goodsGroupIds = RequestUtils.getQueryParam(request, "goodsGroupIds");
		String operateTypeStr = RequestUtils.getQueryParam(request, "operateType");//0-删除，1-下架,2-上架
		
		// 商铺ID必填及存在性
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_SHOPID);
		CommonValidUtil.validPositLong(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOPID);
		Long shopId = Long.valueOf(shopIdStr);
		Integer flag = this.shopDao.queryNormalShopExists(shopId);
		CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
		// 商品族ID不能为空
		CommonValidUtil.validStrNull(goodsGroupIds, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_ID);
		// 操作类型必填
		CommonValidUtil.validStrNull(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, MgrCodeConst.MSG_REQUIRED_GG_OP_TYPE);
		CommonValidUtil.validPositLong(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, MgrCodeConst.MSG_FM_ERROR_GG_OP_TYPE);
		Integer operateType = Integer.valueOf(operateTypeStr);
		
		String[] idStrs = goodsGroupIds.split(",");
		List<Long> ggList = new ArrayList<Long>();
		for(String e : idStrs){
			if(NumberUtil.isPositLongStr(e)){
				ggList.add(Long.valueOf(e));
			}
		}
		
		Integer status = 2;
		if(operateType == null || operateType == CommonConst.GGROUP_UPDATE_OP_TYPE_SC){//删除
			List<Map<String,Object>> ids = new ArrayList<Map<String,Object>>();
			flag = 0;
			for(Long e : ggList){
				// 检查商品族存在性
				flag = this.goodsGroupDao.queryGoodsGroupExists(e);
				CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, MgrCodeConst.MSG_MISS_GG);
				// 检查有无待处理订单
				flag = this.goodsGroupDao.queryValidOrderByGgId(e);
				if(flag > 0){
					throw new ValidateException(MgrCodeConst.CODE_EXISTS_INVALID_ORDER, MgrCodeConst.MSG_EXISTS_INVALID_ORDER);
				}
			}
			status = 2;
		}else if(operateType == CommonConst.GGROUP_UPDATE_OP_TYPE_XJ){//修改
			status =  0;
		}else{
			status =  1;
		}
		for(Long e : ggList){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("shopId", shopId);
			map.put("ggId", e);
			map.put("status", status);
			flag = 1;
			// 上架操作需要检查商品族下面有没商品
			if(operateType == CommonConst.GGROUP_UPDATE_OP_TYPE_SJ){
				// 检查商品族下面有没商品，没有的话则新增一条
				flag = checkGoods(e);
			}
			// 修改商品族状态
			this.goodsGroupDao.updateGoodsGroupStatus(map);
			if(flag == 1){
				// 修改商品状态
				this.goodsDao.updateGoodsStatus(map);
			}
			// 刷新redis缓存
			delGoodsCacheOfGg(e);
		}
		// 删除商品族缓存
		delGoodsGroupCache(shopId);
		return status;
	}
	
	private void delGoodsGroupCache(Long shopId) throws Exception{
		String key = CommonConst.KEY_SHOP_TOP_GOODS+shopId;
		DataCacheApi.del(key);
	}
	
	/**
	 * 刷新商品族下商品缓存
	 * @param ggId
	 * @throws Exception
	 */
	private void delGoodsCacheOfGg(Long ggId) throws Exception{
		List<Object> list = this.goodsDao.getGoodsIdListOfGg(ggId);
		List<Long> goodsIdList = new ArrayList<Long>();
		getIdList(list,goodsIdList);
		if(goodsIdList.size() > 0){
			String key ;
			for(Long id : goodsIdList){
				key = CommonConst.KEY_GOODS+id;
				DataCacheApi.del(key);
			}
		}
			
	}
	
	/**
	 * 检查商品族下面有没商品，没有的话则新增一条
	 * @param ggId
	 * @throws Exception
	 */
	private int checkGoods(Long ggId) throws Exception{
		// 检查商品族下商品
		int flag = this.goodsGroupDao.queryGoodsExistsByGgId(ggId);
		if(flag <= 0){
			// 获取商品族信息
			GoodsGroupDto gg = this.goodsGroupDao.getGoodsGroupById(ggId);
			Long goodsGroupId = gg.getGoodsGroupId();
			// 新增一条上架商品
			GoodsDto goods = new GoodsDto();
			goods.setGoodsGroupId(goodsGroupId);
			goods.setGoodsServerMode(gg.getGoodsServerMode());
			goods.setGoodsName(gg.getGoodsName());
			BigDecimal price = gg.getMinPrice();
			goods.setStandardPrice(price == null ? 0D : price.doubleValue());
			goods.setDiscountPrice(0D);
			goods.setVipPrice(0D);
			goods.setFinalPrice(0D);
			goods.setUnitId(0L);
			String desc = gg.getGoodsDesc();
			goods.setGoodsDesc(StringUtils.isBlank(desc) ? "暂无商品描述" : desc);	
			goods.setGoodsType(2000);
			goods.setShopId(gg.getShopId());
			goods.setGoodsStatus(1);
			goods.setPinyinCode(gg.getPinyincode());
			goods.setCreateTime(new Date());
			goods.setLastUpdateTime(new Date());
			goods.setUseTime(gg.getUseTime());
			goods.setKeepTime(gg.getKeepTime());
			goods.setSequence(1);
			// 添加商品
			Long goodsId = this.goodsDao.addGoodsDto(goods);
			
			List<AttachmentRelationDto> attRelaList = new ArrayList<AttachmentRelationDto>();
			
			// 获取商品族轮播图ID集合
			List<Long> attachRelatIdList = this.attachmentRelationDao.getAttachRelatIdListByCondition(ggId, CommonConst.BIZ_TYPE_IS_GOODS_GROUP, CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
			// 封装商品轮播图信息列表
			getAttachRelatList(attachRelatIdList, attRelaList, goodsId,CommonConst.BIZ_TYPE_IS_GOODS,CommonConst.PIC_TYPE_IS_CYCLE_PLAY);
			// 商品轮播图入库
			this.attachmentRelationDao.addAttachmentRelationBatch(attRelaList);
			
			// 获取商品族logoId
			Long goodsLogoId = gg.getGoodsLogoId();
			// 封装商品缩略图信息列表
			attachRelatIdList.clear();
			attachRelatIdList.add(goodsLogoId);
			getAttachRelatList(attachRelatIdList, attRelaList, goodsId,CommonConst.BIZ_TYPE_IS_GOODS,CommonConst.PIC_TYPE_IS_SUONUE);
			// 商品缩略图入库
			this.attachmentRelationDao.addAttachmentRelationBatch(attRelaList);
			return 0;
		}
		return 1;
	}
	
	/**
	 * 封装附件信息列表
	 * @param attachRelatIdList
	 * @param attRelaList
	 * @param bizId
	 * @param bizType
	 * @param picType
	 */
	private void getAttachRelatList(List<Long> attachRelatIdList,
			List<AttachmentRelationDto> attRelaList,Long bizId,Integer bizType,Integer picType) {
		attRelaList.clear();
		int i = 0;
		if(attachRelatIdList.size() > 0){
			for(Long e : attachRelatIdList){
				i++;
				AttachmentRelationDto LogoAttRela = new AttachmentRelationDto();
				LogoAttRela.setAttachmentId(e);
				LogoAttRela.setBizId(bizId);
				LogoAttRela.setBizType(bizType);
				LogoAttRela.setPicType(picType);
				LogoAttRela.setBizIndex(i);
				attRelaList.add(LogoAttRela);
			}
		}
	}
	
	public PageModel getGoodsGroupList(Long shopId, String goodsKey,Integer status, int pNo,
			int pSize) throws Exception {
		PageModel pm = new PageModel();
		pm.setToPage(pNo);
		pm.setPageSize(pSize);
		pm.setTotalItem(this.goodsGroupDao.getGoodsGroupCount(shopId, goodsKey, status));
		List<GoodsGroupDto> list = this.goodsGroupDao.getGoodsGroupList(shopId, goodsKey,status, pNo, pSize);
		List<GoodsGroupSimpleDto> ls = new ArrayList<GoodsGroupSimpleDto>();
		if(list != null && list.size() > 0){
			String url = null;
			BigDecimal minPrice;
			BigDecimal maxPrice;
			StringBuilder priceStr = null;
			for(GoodsGroupDto e : list){
				url = e.getGoodsLogo();
				if(!StringUtils.isBlank(url)){
					// 拼上文件服务器地址
					e.setGoodsLogo(FdfsUtil.getFileProxyPath(url));
				}
				// 拼接价格区间
				minPrice = e.getMinPrice();
				maxPrice = e.getMaxPrice();
				priceStr = new StringBuilder();
				if(minPrice != null && minPrice.doubleValue() > 0){
					priceStr.append(NumberUtil.roundBigDecimalToStr(minPrice,2));
				}
				if(maxPrice != null && maxPrice.doubleValue() > 0){
					if(priceStr.length() > 0){
						if(minPrice.doubleValue() != maxPrice.doubleValue()){//最大值和最小值不相等才显示价格区间
							priceStr.append("-");
							priceStr.append(NumberUtil.roundBigDecimalToStr(maxPrice,2));
						}
					}else{
						priceStr.append(NumberUtil.roundBigDecimalToStr(maxPrice,2));
					}
				}
				e.setGoodsPrice(priceStr.toString());
				// 封装响应数据列表
				GoodsGroupSimpleDto simple = new GoodsGroupSimpleDto();
				PropertyUtils.copyProperties(simple, e);
				ls.add(simple);
			}
		}
		pm.setList(ls);
		return pm;
	}
	
	public List<Map> getShopPreResourceSetting(Long shopId, Long categoryId)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("categoryId", categoryId);
		//1.根据分类编号在1dcq_goods_group_category_relation表中查询出关联的goods_group_id
		Long goodsGroupId = goodsGroupCategoryRelationDao.getGoodsGroupIdBycategoryId(param);
		if (null == goodsGroupId) {
			return null;
			//throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP);
		}
		param.put("goodsGroupId", goodsGroupId);
		//2.根据goods_group_id和shopId查询商品族内所有商品及商品属性信息
		List<Map> goodsList = goodsGroupDao.getGoodsListByGroupId(param);
		if (null == goodsList || goodsList.size() <= 0) {
			return null;
			//throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP_GOODS);
		}
		// 查询商品下商品属性
		List<Map> goodsPropertys = goodsGroupDao.getGoodsPropertyListByGoodsIds(goodsList);
		//进行返回结果组装
		List<Map> resultMap = null;
		if (goodsPropertys != null && goodsPropertys.size() > 0 ) {
			resultMap = new ArrayList<Map>();
			for(Map property : goodsPropertys){
				Map data = new HashMap();
				Long proGoodsId = CommonValidUtil.isEmpty(property.get("goods_id"))?null:Long.parseLong(property.get("goods_id")+"");
				for(Map goods : goodsList){
					Long goodsId = CommonValidUtil.isEmpty(goods.get("goodsId"))?null:Long.parseLong(goods.get("goodsId")+"");
					if (goodsId != null && proGoodsId != null &&  goodsId.longValue() == proGoodsId.longValue()) {
						Double goodsPrice = CommonValidUtil.isEmpty(goods.get("goodsPrice"))?null:Double.parseDouble(goods.get("goodsPrice")+"");
						data.put("goodsId", goodsId);
						data.put("price", goodsPrice);
					}
				}
				String proValue= CommonValidUtil.isEmpty(property.get("pro_value"))?null:(property.get("pro_value")+"");
				if (!StringUtils.isBlank(proValue)) {
					//将礼拜几与时间段分隔取出
					String[] strs1 = proValue.split("#");
					Integer weekDay = Integer.parseInt(strs1[0]);
					String times = strs1[1];
					if (!StringUtils.isBlank(times)) {
						String[] strs2 = times.split("-");
						data.put("fromTime", strs2[0]);
						data.put("toTime", strs2[1]);
					}
					data.put("weekDay", weekDay);
				}
				resultMap.add(data);
			}
		}
		return resultMap;
	}
	public int operatePreResource(TempCateGoryDto tempCateGoryDto)
			throws Exception {
		//1.参数校验
		Map<String,Object> param = validParam(tempCateGoryDto);
		//2.操作商品族（有则更新价格，没有则新增商品族）
		GoodsGroupDto goodsGroupDto = getGoodsGroupBycategoryIdAndShopId(param);
		param.put("goodsGroupId", goodsGroupDto.getGoodsGroupId());
		//3.获取商品族属性（没有则新增）
		GoodsGroupPropertyDto goodsGroupPropertyDto = getGoodsGroupPropertyByGroupId(param);
		//4.操作数据（商品族属性值、商品族商品、商品族商品属性的增、删、改）
		operateData(param, tempCateGoryDto.getResources(), goodsGroupDto, goodsGroupPropertyDto);
		return 1;
	}
	/**
	 * 建立商品族图片关联关系
	 * @param goodsGroupDto
	 * @return
	 * @throws Exception 
	 */
	private int createAttachmentRelation(GoodsGroupDto goodsGroupDto,Long categoryId) throws Exception{
		//获取商品族缩略图
		AttachmentRelationDto arDto = new AttachmentRelationDto();
		arDto.setBizId(categoryId);
		arDto.setBizType(11);
		arDto.setPicType(1);
		List<AttachmentRelationDto> logs = attachmentRelationDao.findByCondition(arDto);
		//获取商品族轮播图
		arDto = new AttachmentRelationDto();
		arDto.setBizId(categoryId);
		arDto.setBizType(11);
		arDto.setPicType(2);
		List<AttachmentRelationDto> pics = attachmentRelationDao.findByCondition(arDto);
		//商品族图片关联关系集合
		List<AttachmentRelationDto> goodsGroupPics = new ArrayList<AttachmentRelationDto>();
		//建立商品跟缩略图的关联关系
		if (logs != null && logs.size() > 0) {
			for(AttachmentRelationDto bean : logs){
				AttachmentRelationDto a1 = new AttachmentRelationDto();
				a1.setBizId(goodsGroupDto.getGoodsGroupId());
				a1.setBizType(9);
				a1.setPicType(1);
				a1.setAttachmentId(bean.getAttachmentId());
				a1.setBizIndex(bean.getBizIndex());
				goodsGroupPics.add(a1);
			}
		}
		//建立商品跟轮播图的关联关系
		if (pics != null && pics.size() > 0) {
			for(AttachmentRelationDto bean : pics){
				AttachmentRelationDto a1 = new AttachmentRelationDto();
				a1.setBizId(goodsGroupDto.getGoodsGroupId());
				a1.setBizType(9);
				a1.setPicType(2);
				a1.setAttachmentId(bean.getAttachmentId());
				a1.setBizIndex(bean.getBizIndex());
				goodsGroupPics.add(a1);
			}
		}
		//批量添加附件关联关系
		if (goodsGroupPics.size() > 0) {
			attachmentRelationDao.addAttachmentRelationBatch(goodsGroupPics);
		}
		return 1;
	}
	
	/**
	 * 操作数据
	 * @param param
	 * @param resources
	 * @param goodsGroupDto
	 * @param goodsGroupPropertyDto
	 * @return
	 * @throws Exception
	 */
	private int operateData(Map<String,Object> param,List<TempGoodsPropertyDto> resources,GoodsGroupDto goodsGroupDto,GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception{
		//查询当前商品族内所有商品编号集合
		List<Long> dbGoodsIds = getGoodsIdsByGroupIdAndShopId(param);
		//需要新增的数据
		List<TempGoodsPropertyDto> addDtos = new ArrayList<TempGoodsPropertyDto>();
		//需要修改的数据
		List<TempGoodsPropertyDto> updDtos = new ArrayList<TempGoodsPropertyDto>();
		//需要删除的数据
		List<Long> delGoodsIds = new ArrayList<Long>();
		List<Long> tmpGoodsIds = new ArrayList<Long>();
		for(TempGoodsPropertyDto dto : resources){
			Long goodsId = dto.getGoodsId();
			if (null != goodsId) {
				if (dbGoodsIds.contains(goodsId)) {
					//相等的，则需要添加到需要修改的集合中
					updDtos.add(dto);
					//已经需要修改的商品。。。
					tmpGoodsIds.add(goodsId);
				}else{
					//有上传商品编号，但是跟数据库中比对不上，则认为无效，需要新增
					addDtos.add(dto);
				}
			}else{
				//上传参数中没有goodsId的数据，添加到需要直接新增的集合中
				addDtos.add(dto);
			}
		}
		//获取需要删除的商品
		for(Long dbGoodsId : dbGoodsIds){
			if (!tmpGoodsIds.contains(dbGoodsId)) {
				delGoodsIds.add(dbGoodsId);
			}
		}
		//新增...
		if (addDtos.size() > 0) {
			addOperate(addDtos, goodsGroupDto, goodsGroupPropertyDto);
		}
		//修改...
		if (updDtos.size() > 0) {
			updateOperate(updDtos, goodsGroupPropertyDto);
		}
		//删除...
		if (delGoodsIds.size() > 0) {
			deleteOperate(delGoodsIds);
		}
		return 1;
	}
	
	/**
	 * 参数校验
	 * @param tempCateGoryDto
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> validParam(TempCateGoryDto tempCateGoryDto) throws Exception{
		String categoryIdStr = tempCateGoryDto.getCategoryId();
		String shopIdStr = tempCateGoryDto.getShopId();
		//商铺编号参数校验
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
		Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		//分类编号参数校验
		CommonValidUtil.validStrNull(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CATEGORY_ID);
		Long categoryId = CommonValidUtil.validStrLongFmt(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CATEGORY_ID);
		List<TempGoodsPropertyDto> resources = tempCateGoryDto.getResources();
		//最高价格跟最低价格
		Double minPrice = 0d;
		Double maxPrice = 0d;
		boolean flag = true;
		for(TempGoodsPropertyDto dto : resources){
			Integer weekDay = dto.getWeekDay();
			String fromTime = dto.getFromTime();
			String toTime = dto.getToTime();
			if(null == weekDay){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_WEEKDAY);
			}
			CommonValidUtil.validStrNull(fromTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_START_TIME);
			CommonValidUtil.validDateTimeStr(fromTime, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_START_TIME,"HH:mm");
			CommonValidUtil.validStrNull(toTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_END_TIME);
			CommonValidUtil.validDateTimeStr(toTime, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_END_TIME,"HH:mm");
			Double price = dto.getPrice();
			if (null == price) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PRICE);
			}
			//获取最高最低价格
			if (flag) {
				minPrice = price;
				maxPrice = price;
				flag = false;
			}
			if (price < minPrice) {
				minPrice = price;
			}
			if (price > maxPrice) {
				maxPrice = price;
			}
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("categoryId", categoryId);
		param.put("minPrice", minPrice);
		param.put("maxPrice", maxPrice);
		return param;
	}
	
	/**
	 * 进行删除操作
	 * @param delGoodsIds
	 * @return
	 * @throws Exception
	 */
	private int deleteOperate(List<Long> delGoodsIds) throws Exception{
		//删除商品属性记录
		//删除商品族属性值记录
		//删除商品记录
		List<Long> proValuesIds = goodsPropertyDao.getGoodsPropertyIdByGoodsIds(delGoodsIds);
		goodsPropertyDao.batchDelGoodsPropertyByGoodsId(delGoodsIds);
		if (null != proValuesIds && proValuesIds.size() > 0) {
			goodsGroupProValuesDao.batchDelGoodsGroupProValuesDtoByIds(proValuesIds);
		}
		goodsDao.batchUpdateGoodsById(delGoodsIds);
		return 1;
	}
	
	/**
	 * 进行修改操作
	 * @param updDtos
	 * @param goodsGroupPropertyDto
	 * @return
	 * @throws Exception
	 */
	private int updateOperate(List<TempGoodsPropertyDto> updDtos,GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception{
		//修改商品价格
		//修改商品族属性值记录
		for(TempGoodsPropertyDto dto : updDtos){
			Integer weekDay = dto.getWeekDay();
			String fromTime = dto.getFromTime();
			String toTime = dto.getToTime();
			Double price = dto.getPrice();
			Integer order = dto.getOrder();
			String proValue = weekDay+CommonConst.SEPARATOR_JH+fromTime+CommonConst.SEPARATOR_HG+toTime;
			Long goodsId = dto.getGoodsId();
			//修改商品价格
			GoodsDto goodsDto = new GoodsDto();
			goodsDto.setGoodsId(goodsId);
			goodsDto.setStandardPrice(price);
			goodsDao.updateGoodsStandardPrice(goodsDto);
			List<Long> goodsIds = new ArrayList<Long>();
			goodsIds.add(goodsId);
			//根据商品编号查询商品属性值编号
			List<Long> proValuesIds = goodsPropertyDao.getGoodsPropertyIdByGoodsIds(goodsIds);
			//有则修改，没有则新增
			if (proValuesIds != null && proValuesIds.size() > 0) {
				GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
				ggpvDto.setProValuesId(proValuesIds.get(0));
				ggpvDto.setProValue(proValue);
				ggpvDto.setValuesOrder(order == null?1:order);
				goodsGroupProValuesDao.updateGoodsGroupProValue(ggpvDto);
			}else{
				//添加商品族属性值记录
				GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
				ggpvDto.setProValue(proValue);
				ggpvDto.setValuesOrder(order == null?1:order);
				ggpvDto.setGroupPropertyId(goodsGroupPropertyDto.getGroupPropertyId());
				Long proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(ggpvDto);
				//添加商品属性记录
				addGoodsPropertyDto(goodsId, goodsGroupPropertyDto.getGroupPropertyId(), proValuesId);
			}
		}
		return 1;
	}
	
	/**
	 * 进行添加操作
	 * @param addDtos
	 * @param goodsGroupDto
	 * @param goodsGroupPropertyDto
	 * @return
	 * @throws Exception
	 */
	private int addOperate(List<TempGoodsPropertyDto> addDtos,GoodsGroupDto goodsGroupDto,GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception{
		//新增商品族属性值记录
		//新增商品记录
		//新增商品属性记录
		//获取商品族缩略图
		AttachmentRelationDto arDto = new AttachmentRelationDto();
		arDto.setBizId(goodsGroupDto.getGoodsGroupId());
		arDto.setBizType(9);
		arDto.setPicType(1);
		List<AttachmentRelationDto> logs = attachmentRelationDao.findByCondition(arDto);
		//获取商品族轮播图
		arDto = new AttachmentRelationDto();
		arDto.setBizId(goodsGroupDto.getGoodsGroupId());
		arDto.setBizType(9);
		arDto.setPicType(2);
		List<AttachmentRelationDto> pics = attachmentRelationDao.findByCondition(arDto);
		//商品图片关联关系集合
		List<AttachmentRelationDto> goodsPics = new ArrayList<AttachmentRelationDto>();
		for(TempGoodsPropertyDto dto : addDtos){
			Integer weekDay = dto.getWeekDay();
			String fromTime = dto.getFromTime();
			String toTime = dto.getToTime();
			Double price = dto.getPrice();
			Integer order = dto.getOrder();
			String proValue = weekDay+CommonConst.SEPARATOR_JH+fromTime+CommonConst.SEPARATOR_HG+toTime;
			GoodsDto goodsDto = new GoodsDto();
			//将商品族属性复制给商品
			DataConvertUtil.propertyConvert(goodsGroupDto, goodsDto, MgrCommonConst.GOODSGROUP_GOODS_FIELD);
			//PropertyUtils.copyProperties(goodsDto, goodsGroupDto);
			//设置商品属性数据
			String goodsName = goodsGroupDto.getGoodsName();
			goodsDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
			goodsDto.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
			goodsDto.setStandardPrice(price);
			goodsDto.setGoodsStatus(1);//默认上架状态
			goodsDto.setGoodsType(2000);//商品类型：实物-1000,服务-2000,套餐-3000
			goodsDto.setDiscountPrice(0d);//折扣价
			goodsDto.setVipPrice(0d);//会员价
			goodsDto.setSequence(1);
			goodsDto.setCreateTime(new Date());
			goodsDto.setLastUpdateTime(new Date());
			goodsDto.setUnitId(goodsGroupDto.getUnitId() == null?0:goodsGroupDto.getUnitId());
			goodsDto.setGoodsDesc(goodsDto.getGoodsDesc() == null?goodsName:goodsDto.getGoodsDesc());
			goodsDto.setGoodsDetailDesc(goodsDto.getGoodsDetailDesc() == null?goodsName:goodsDto.getGoodsDetailDesc());
			//保存商品信息，返回商品编号
			goodsDao.addGoodsDto(goodsDto);
			Long goodsId = goodsDto.getGoodsId();
			//插入商品族属性值记录，返回商品族属性值主键
			GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
			ggpvDto.setGroupPropertyId(goodsGroupPropertyDto.getGroupPropertyId());
			ggpvDto.setProValue(proValue);
			ggpvDto.setValuesOrder(order == null?1:order);
			Long proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(ggpvDto);
			//添加商品属性记录
			addGoodsPropertyDto(goodsId, goodsGroupPropertyDto.getGroupPropertyId(), proValuesId);
			//建立商品跟缩略图的关联关系
			if (logs != null && logs.size() > 0) {
				for(AttachmentRelationDto bean : logs){
					AttachmentRelationDto a1 = new AttachmentRelationDto();
					a1.setBizId(goodsId);
					a1.setBizType(8);
					a1.setPicType(1);
					a1.setAttachmentId(bean.getAttachmentId());
					a1.setBizIndex(bean.getBizIndex());
					goodsPics.add(a1);
				}
			}
			//建立商品跟轮播图的关联关系
			if (pics != null && pics.size() > 0) {
				for(AttachmentRelationDto bean : pics){
					AttachmentRelationDto a1 = new AttachmentRelationDto();
					a1.setBizId(goodsId);
					a1.setBizType(8);
					a1.setPicType(2);
					a1.setAttachmentId(bean.getAttachmentId());
					a1.setBizIndex(bean.getBizIndex());
					goodsPics.add(a1);
				}
			}
		}
		//批量添加附件关联关系
		if (goodsPics.size() > 0) {
			attachmentRelationDao.addAttachmentRelationBatch(goodsPics);
		}
		return 1;
	}
	
	/**
	 * 添加商品属性
	 * @param goodsId
	 * @param groupPropertyId
	 * @param proValuesId
	 * @return
	 * @throws Exception
	 */
	private int addGoodsPropertyDto(Long goodsId,Long groupPropertyId,Long proValuesId) throws Exception{
		GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
		goodsPropertyDto.setGoodsId(goodsId);
		goodsPropertyDto.setGroupPropertyId(groupPropertyId);
		goodsPropertyDto.setProValuesId(proValuesId);
		return goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);
	}
	
	/**
	 * 查询当前商品族内所有商品编号集合
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private List<Long> getGoodsIdsByGroupIdAndShopId(Map param) throws Exception{
		List<Long> dbGoodsIds = goodsGroupDao.getGoodsIdsByGroupIdAndShopId(param);
		if (null == dbGoodsIds || dbGoodsIds.size() <= 0) {
			dbGoodsIds = new ArrayList<Long>();
		}
		return dbGoodsIds;
	}
	
	/**
	 * 查询商品族属性，如果没有，则新增
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private GoodsGroupPropertyDto getGoodsGroupPropertyByGroupId(Map param) throws Exception{
		Long shopId = Long.parseLong(param.get("shopId")+"");
		Long goodsGroupId = Long.parseLong(param.get("goodsGroupId")+"");
		GoodsGroupPropertyDto goodsGroupPropertyDto = goodsGroupPropertyDao.getGoodsGroupPropertyByGroupId(goodsGroupId);
		//没有则添加
		if (null == goodsGroupPropertyDto) {
			goodsGroupPropertyDto = new GoodsGroupPropertyDto();
			goodsGroupPropertyDto.setGroupPropertyName("时间段");//默认为时间段
			goodsGroupPropertyDto.setShopId(shopId);
			goodsGroupPropertyDto.setPropertyOrder(1);
			goodsGroupPropertyDto.setPropertyType(2);//属性类型：枚举型=1,时间型=2
			goodsGroupPropertyDto.setGoodsGroupId(goodsGroupId);
			Long groupPropertyId = goodsGroupPropertyDao.addGoodsGroupProBackId(goodsGroupPropertyDto);
			goodsGroupPropertyDto.setGroupPropertyId(groupPropertyId);
		}
		return goodsGroupPropertyDto;
	}
	/**
	 * 查询商品族，如果没有，则新增
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private GoodsGroupDto getGoodsGroupBycategoryIdAndShopId(Map param) throws Exception{
		GoodsGroupDto goodsGroupDto = goodsGroupDao.getGoodsGroupBycategoryIdAndShopId(param);
		Long shopId = Long.parseLong(param.get("shopId")+"");
		Long categoryId = Long.parseLong(param.get("categoryId")+"");
		BigDecimal minPrice = new BigDecimal(param.get("minPrice")+"");
		BigDecimal maxPrice = new BigDecimal(param.get("maxPrice")+"");
		if (null == goodsGroupDto) {
			//没有则新增商品族
			goodsGroupDto = new GoodsGroupDto();
			goodsGroupDto.setShopId(shopId);
			String goodsName = getCategoryName(categoryId);
			goodsGroupDto.setGoodsName(goodsName);
			goodsGroupDto.setPinyincode(PinyinUtil.getPinYinHeadChar(goodsName));
			goodsGroupDto.setMinPrice(minPrice);
			goodsGroupDto.setMaxPrice(maxPrice);
			goodsGroupDto.setUnitId(0l);
			goodsGroupDto.setGoodsStatus(1);//默认上架
			goodsGroupDto.setCreateTime(new Date());
			goodsGroupDto.setLastUpdateTime(new Date());
			goodsGroupDto.setGoodsServerMode(2);//服务方式为到店
			Long goodsGroupId = goodsGroupDao.addGoodsGroup(goodsGroupDto);
			goodsGroupDto.setGoodsGroupId(goodsGroupId);
			//添加商品族分类关联关系
			GoodsGroupCategoryRelationDto ggcrDto = new GoodsGroupCategoryRelationDto();
			ggcrDto.setCreateTime(new Date());
			ggcrDto.setGoodsGroupId(goodsGroupId);
			ggcrDto.setGroupCategoryId(categoryId);
			ggcrDto.setCrIndex(null);//启用
			ggcrDto.setLastUpdateTime(new Date());
			ggcrDto.setCrStatus(1);
			ggcrDto.setParentCategoryId(null);
			List<GoodsGroupCategoryRelationDto> ggcrDtos = new ArrayList<GoodsGroupCategoryRelationDto>();
			ggcrDtos.add(ggcrDto);
			goodsGroupCategoryRelationDao.addGoodsGroupCategoryRelationBatch(ggcrDtos);
			//将分类的附件关系复制给商品族（缩略图、轮播图）
			createAttachmentRelation(goodsGroupDto,categoryId);
			//将当前分类的所有父分类下的商品族状态置为删除
			updateGoodsGroupStatusIsDel(categoryId, shopId);
		}else{
			//修改商品族最高最低价格
			GoodsGroupDto ggDto = new GoodsGroupDto();
			ggDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
			ggDto.setMinPrice(minPrice);
			ggDto.setMaxPrice(maxPrice);
			goodsGroupDao.updateGoodsGroup(ggDto);
		}
		//刷新缓存
		refreshGroupGoodsCache(shopId,goodsGroupDto.getGoodsGroupId());
		return goodsGroupDto;
	}
	
	/**
	 * 如果给子分类添加价格，需要将所有父分类对应的商品族的状态置为已删除
	 * @param categoryId
	 * @param shopId
	 * @throws Exception
	 */
	private void updateGoodsGroupStatusIsDel(Long categoryId,Long shopId)throws Exception{
		Long parentCategoryId = categoryDao.getParentCategoryIdByChildId(categoryId);
		if (parentCategoryId == null || parentCategoryId.intValue() == 0) {
			return;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("categoryId", parentCategoryId);
		param.put("shopId", shopId);
		GoodsGroupDto goodsGroupDto = goodsGroupDao.getGoodsGroupBycategoryIdAndShopId(param);
		if(null != goodsGroupDto){
			Long goodsGroupId = goodsGroupDto.getGoodsGroupId();
			if (null != goodsGroupId) {
				//将当前商品族的状态置为已删除
				param.put("ggId", goodsGroupId);
				param.put("status", CommonConst.GOODS_GROUP_STS_SC);
				goodsGroupDao.updateGoodsGroupStatus(param);
				//将当前商品族下面的所有商品状态置为已删除
				goodsDao.updateGoodsStatsIsDelByGoodsGroupId(goodsGroupId, CommonConst.GOODS_STATUS_SC);
			}
		}
		updateGoodsGroupStatusIsDel(parentCategoryId, shopId);
	}
	
	/*
	public int operatePreResource_bak(TempCateGoryDto tempCateGoryDto)
			throws Exception {
		//1.参数校验
		String categoryIdStr = tempCateGoryDto.getCategoryId();
		String shopIdStr = tempCateGoryDto.getShopId();
		//商铺编号参数校验
		CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MISS_SHOP_ID);
		Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
		//分类编号参数校验
		CommonValidUtil.validStrNull(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_CATEGORY_ID);
		Long categoryId = CommonValidUtil.validStrLongFmt(categoryIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CATEGORY_ID);
		List<TempGoodsPropertyDto> resources = tempCateGoryDto.getResources();
		//最高价格跟最低价格
		Double minPrice = 0d;
		Double maxPrice = 0d;
		boolean flag = true;
		for(TempGoodsPropertyDto dto : resources){
			Integer weekDay = dto.getWeekDay();
			String fromTime = dto.getFromTime();
			String toTime = dto.getToTime();
			if(null == weekDay){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_WEEKDAY);
			}
			CommonValidUtil.validStrNull(fromTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_START_TIME);
			CommonValidUtil.validDateTimeStr(fromTime, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_START_TIME,"HH:mm");
			CommonValidUtil.validStrNull(toTime, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_END_TIME);
			CommonValidUtil.validDateTimeStr(toTime, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FMT_ERROR_END_TIME,"HH:mm");
			Double price = dto.getPrice();
			if (null == price) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_PRICE);
			}
			//获取最高最低价格
			if (flag) {
				minPrice = price;
				maxPrice = price;
				flag = false;
			}
			if (price < minPrice) {
				minPrice = price;
			}
			if (price > maxPrice) {
				maxPrice = price;
			}
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopId", shopId);
		param.put("categoryId", categoryId);
		//2.根据商铺编号+分类编号查询商品族信息
		GoodsGroupDto goodsGroupDto = goodsGroupDao.getGoodsGroupBycategoryIdAndShopId(param);
		if (null == goodsGroupDto) {
			//2.1 没有查询到商品族信息
			//2.1.1 添加商品族记录：商品族名称为分类一级、二级分类名称组合
			//2.1.2 添加商品族属性记录：
			//2.1.3 添加商品族属性值记录：
			//2.1.4 添加商品族商品记录：
			//2.1.5 添加商品族属性记录：
			//2.1.6 添加商品族及分类关联关系表
			//插入商品族信息，返回商品族编号
			goodsGroupDto = new GoodsGroupDto();
			goodsGroupDto.setShopId(shopId);
			String goodsName = getCategoryName(Integer.parseInt(categoryIdStr));
			goodsGroupDto.setGoodsName(goodsName);
			goodsGroupDto.setPinyincode(PinyinUtil.getPinYinHeadChar(goodsName));
			goodsGroupDto.setMinPrice(new BigDecimal(minPrice+""));
			goodsGroupDto.setMaxPrice(new BigDecimal(maxPrice+""));
			goodsGroupDto.setUnitId(0l);
			goodsGroupDto.setGoodsStatus(1);//默认上架
			goodsGroupDto.setCreateTime(new Date());
			goodsGroupDto.setLastUpdateTime(new Date());
			goodsGroupDto.setGoodsServerMode(2);//服务方式为到店
			Long goodsGroupId = goodsGroupDao.addGoodsGroup(goodsGroupDto);
			//添加商品族分类关联关系
			GoodsGroupCategoryRelationDto ggcrDto = new GoodsGroupCategoryRelationDto();
			ggcrDto.setCreateTime(new Date());
			ggcrDto.setGoodsGroupId(goodsGroupId);
			ggcrDto.setGroupCategoryId(categoryId);
			ggcrDto.setCrIndex(null);//启用
			ggcrDto.setLastUpdateTime(new Date());
			ggcrDto.setCrStatus(1);
			ggcrDto.setParentCategoryId(null);
			List<GoodsGroupCategoryRelationDto> ggcrDtos = new ArrayList<GoodsGroupCategoryRelationDto>();
			ggcrDtos.add(ggcrDto);
			goodsGroupCategoryRelationDao.addGoodsGroupCategoryRelationBatch(ggcrDtos);
			//插入商品族属性记录，返回商品族属性编号
			GoodsGroupPropertyDto ggpDto = new GoodsGroupPropertyDto();
			ggpDto.setGroupPropertyName("时间段");//默认为时间段
			ggpDto.setShopId(shopId);
			ggpDto.setPropertyOrder(1);
			ggpDto.setPropertyType(2);//属性类型：枚举型=1,时间型=2
			ggpDto.setGoodsGroupId(goodsGroupId);
			Long groupPropertyId = goodsGroupPropertyDao.addGoodsGroupProBackId(ggpDto);
			for(TempGoodsPropertyDto dto : resources){
				Integer weekDay = dto.getWeekDay();
				String fromTime = dto.getFromTime();
				String toTime = dto.getToTime();
				Double price = dto.getPrice();
				String proValue = weekDay+CommonConst.SEPARATOR_JH+fromTime+CommonConst.SEPARATOR_HG+toTime;
				//插入商品族属性值记录，返回商品族属性值主键
				GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
				ggpvDto.setGroupPropertyId(groupPropertyId);
				ggpvDto.setProValue(proValue);
				ggpvDto.setValuesOrder(1);
				Long proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(ggpvDto);
				GoodsDto goodsDto = new GoodsDto();
				//将商品族属性复制给商品
				DataConvertUtil.propertyConvert(goodsGroupDto, goodsDto, MgrCommonConst.GOODSGROUP_GOODS_FIELD);
				//PropertyUtils.copyProperties(goodsDto, goodsGroupDto);
				//设置商品属性数据
				goodsDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
				goodsDto.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
				goodsDto.setStandardPrice(price);
				goodsDto.setGoodsStatus(1);//默认上架状态
				goodsDto.setGoodsType(2000);//商品类型：实物-1000,服务-2000,套餐-3000
				goodsDto.setDiscountPrice(0d);//折扣价
				goodsDto.setVipPrice(0d);//会员价
				goodsDto.setSequence(1);
				goodsDto.setCreateTime(new Date());
				goodsDto.setLastUpdateTime(new Date());
				goodsDto.setUnitId(goodsGroupDto.getUnitId() == null?0:goodsGroupDto.getUnitId());
				goodsDto.setGoodsDesc(goodsDto.getGoodsDesc() == null?goodsName:goodsDto.getGoodsDesc());
				goodsDto.setGoodsDetailDesc(goodsDto.getGoodsDetailDesc() == null?goodsName:goodsDto.getGoodsDetailDesc());
				//保存商品信息，返回商品编号
				goodsDao.addGoodsDto(goodsDto);
				Long goodsId = goodsDto.getGoodsId();
				GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
				goodsPropertyDto.setGoodsId(goodsId);
				goodsPropertyDto.setGroupPropertyId(groupPropertyId);
				goodsPropertyDto.setProValuesId(proValuesId);
				goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);
			}
		}else{
			//2.2 有查询到商品族信息
			//2.2.1 查询出商品族内所有商品编号记录
			//2.2.2 将上传参数中的goodsId跟DB中的goodsId比对，生成三种可能情况：删除、修改、新增
			// TODO ...
			Long goodsGroupId = goodsGroupDto.getGoodsGroupId();
			//修改商品族最高最低价格
			GoodsGroupDto ggDto = new GoodsGroupDto();
			ggDto.setGoodsGroupId(goodsGroupId);
			ggDto.setMinPrice(new BigDecimal(minPrice+""));
			ggDto.setMaxPrice(new BigDecimal(maxPrice+""));
			goodsGroupDao.updateGoodsGroup(ggDto);
			//查询当前商品族内所有商品编号集合
			param.put("goodsGroupId", goodsGroupId);
			List<Long> dbGoodsIds = goodsGroupDao.getGoodsIdsByGroupIdAndShopId(param);
			//需要新增的数据
			List<TempGoodsPropertyDto> addDtos = new ArrayList<TempGoodsPropertyDto>();
			//需要修改的数据
			List<TempGoodsPropertyDto> updDtos = new ArrayList<TempGoodsPropertyDto>();
			//需要删除的数据
			List<Long> delGoodsIds = new ArrayList<Long>();
			List<Long> tmpGoodsIds = new ArrayList<Long>();
			if (null == dbGoodsIds || dbGoodsIds.size() <= 0) {
				//throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_GOODSGROUP_GOODS);
				dbGoodsIds = new ArrayList<Long>();
			}
			for(TempGoodsPropertyDto dto : resources){
				Long goodsId = dto.getGoodsId();
				if (null != goodsId) {
					if (dbGoodsIds.contains(goodsId)) {
						//相等的，则需要添加到需要修改的集合中
						updDtos.add(dto);
						//已经需要修改的商品。。。
						tmpGoodsIds.add(goodsId);
					}else{
						//有上传商品编号，但是跟数据库中比对不上，则认为无效，需要新增
						addDtos.add(dto);
					}
				}else{
					//上传参数中没有goodsId的数据，添加到需要直接新增的集合中
					addDtos.add(dto);
				}
			}
			//获取需要删除的商品
			for(Long dbGoodsId : dbGoodsIds){
				if (!tmpGoodsIds.contains(dbGoodsId)) {
					delGoodsIds.add(dbGoodsId);
				}
			}
			Long groupPropertyId = null;
			//获取商品族商品属性信息
			GoodsGroupPropertyDto ggpDto1 = goodsGroupPropertyDao.getGoodsGroupPropertyByGroupId(goodsGroupId);
			//没有则添加
			if (null == ggpDto1) {
				ggpDto1 = new GoodsGroupPropertyDto();
				ggpDto1.setGroupPropertyName("时间段");//默认为时间段
				ggpDto1.setShopId(shopId);
				ggpDto1.setPropertyOrder(1);
				ggpDto1.setPropertyType(2);//属性类型：枚举型=1,时间型=2
				ggpDto1.setGoodsGroupId(goodsGroupId);
				groupPropertyId = goodsGroupPropertyDao.addGoodsGroupProBackId(ggpDto1);
			}else{
				groupPropertyId = ggpDto1.getGroupPropertyId();
			}
			//新增...
			if (addDtos.size() > 0) {
				//新增商品族属性值记录
				//新增商品记录
				//新增商品属性记录
				for(TempGoodsPropertyDto dto : addDtos){
					Integer weekDay = dto.getWeekDay();
					String fromTime = dto.getFromTime();
					String toTime = dto.getToTime();
					Double price = dto.getPrice();
					String proValue = weekDay+CommonConst.SEPARATOR_JH+fromTime+CommonConst.SEPARATOR_HG+toTime;
					
					GoodsDto goodsDto = new GoodsDto();
					//将商品族属性复制给商品
					DataConvertUtil.propertyConvert(goodsGroupDto, goodsDto, MgrCommonConst.GOODSGROUP_GOODS_FIELD);
					//PropertyUtils.copyProperties(goodsDto, goodsGroupDto);
					//设置商品属性数据
					String goodsName = goodsGroupDto.getGoodsName();
					goodsDto.setGoodsGroupId(goodsGroupDto.getGoodsGroupId());
					goodsDto.setPinyinCode(PinyinUtil.getPinYinHeadChar(goodsName));
					goodsDto.setStandardPrice(price);
					goodsDto.setGoodsStatus(1);//默认上架状态
					goodsDto.setGoodsType(2000);//商品类型：实物-1000,服务-2000,套餐-3000
					goodsDto.setDiscountPrice(0d);//折扣价
					goodsDto.setVipPrice(0d);//会员价
					goodsDto.setSequence(1);
					goodsDto.setCreateTime(new Date());
					goodsDto.setLastUpdateTime(new Date());
					goodsDto.setUnitId(goodsGroupDto.getUnitId() == null?0:goodsGroupDto.getUnitId());
					goodsDto.setGoodsDesc(goodsDto.getGoodsDesc() == null?goodsName:goodsDto.getGoodsDesc());
					goodsDto.setGoodsDetailDesc(goodsDto.getGoodsDetailDesc() == null?goodsName:goodsDto.getGoodsDetailDesc());
					//保存商品信息，返回商品编号
					goodsDao.addGoodsDto(goodsDto);
					Long goodsId = goodsDto.getGoodsId();
					//插入商品族属性值记录，返回商品族属性值主键
					GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
					ggpvDto.setGroupPropertyId(groupPropertyId);
					ggpvDto.setProValue(proValue);
					ggpvDto.setValuesOrder(1);
					Long proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(ggpvDto);
					//添加商品属性记录
					GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
					goodsPropertyDto.setGoodsId(goodsId);
					goodsPropertyDto.setGroupPropertyId(groupPropertyId);
					goodsPropertyDto.setProValuesId(proValuesId);
					goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);
				}
			}
			//修改...
			if (updDtos.size() > 0) {
				//修改商品价格
				//修改商品族属性值记录
				for(TempGoodsPropertyDto dto : updDtos){
					Integer weekDay = dto.getWeekDay();
					String fromTime = dto.getFromTime();
					String toTime = dto.getToTime();
					Double price = dto.getPrice();
					String proValue = weekDay+CommonConst.SEPARATOR_JH+fromTime+CommonConst.SEPARATOR_HG+toTime;
					Long goodsId = dto.getGoodsId();
					//修改商品价格
					GoodsDto goodsDto = new GoodsDto();
					goodsDto.setGoodsId(goodsId);
					goodsDto.setStandardPrice(price);
					goodsDao.updateGoodsStandardPrice(goodsDto);
					List<Long> goodsIds = new ArrayList<Long>();
					goodsIds.add(goodsId);
					//根据商品编号查询商品属性值编号
					List<Long> proValuesIds = goodsPropertyDao.getGoodsPropertyIdByGoodsIds(goodsIds);
					//有则修改，没有则新增
					if (proValuesIds != null && proValuesIds.size() > 0) {
						GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
						ggpvDto.setProValuesId(proValuesIds.get(0));
						ggpvDto.setProValue(proValue);
						goodsGroupProValuesDao.updateGoodsGroupProValue(ggpvDto);
					}else{
						//添加商品族属性值记录
						GoodsGroupProValuesDto ggpvDto = new GoodsGroupProValuesDto();
						ggpvDto.setProValue(proValue);
						ggpvDto.setGroupPropertyId(groupPropertyId);
						Long proValuesId = goodsGroupProValuesDao.addGoodsGroupProValueBackId(ggpvDto);
						//添加商品属性记录
						GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
						goodsPropertyDto.setGoodsId(goodsId);
						goodsPropertyDto.setGroupPropertyId(groupPropertyId);
						goodsPropertyDto.setProValuesId(proValuesId);
						goodsPropertyDao.addGoodsPropertyDto(goodsPropertyDto);
					}
					
				}
			}
			//删除...
			if (delGoodsIds.size() > 0) {
				//删除商品属性记录
				//删除商品族属性值记录？？
				//删除商品记录
				List<Long> proValuesIds = goodsPropertyDao.getGoodsPropertyIdByGoodsIds(delGoodsIds);
				goodsPropertyDao.batchDelGoodsPropertyByGoodsId(delGoodsIds);
				if (null != proValuesIds && proValuesIds.size() > 0) {
					goodsGroupProValuesDao.batchDelGoodsGroupProValuesDtoByIds(proValuesIds);
				}
				goodsDao.batchUpdateGoodsById(delGoodsIds);
			}
		}
		return 1;
	}
	*/
	/**
	 * 获取商品族名称
	 * <b>
	 * 	使用递归方式，将当前分类名称及所有上级分类名称返回
	 * </b>
	 * @param categoryId
	 * @return
	 */
	private String getCategoryName(Long categoryId){
		String goodsName = "";
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryId(categoryId);
		List<CategoryDto> categoryDtos = categoryDao.getCategorys(categoryDto);
		if (categoryDtos != null && categoryDtos.size() > 0) {
			CategoryDto dto1 = categoryDtos.get(0);
			goodsName = dto1.getCategoryName();
			String tmpName = getCategoryName(dto1.getParentCategoryId());
			if (!StringUtils.isBlank(tmpName)) {
				goodsName = (tmpName+"("+goodsName+")");
			}
		}
		return goodsName;
	}
	
	/**
	 * 根据商品族编号查找商品族
	 * @Title: getGoodsGroupById 
	 * @param @param parseLong
	 * @param @return  
	 * @throws
	 */
	public GoodsGroupDto getGoodsGroupById(long goodsGroupId) throws Exception{
		return goodsGroupDao.getGoodsGroupById(goodsGroupId);
	}
	
	/**
	 * 根据商铺id获取商品族列表 
	 * @Title: getGoodsGroupListByShopId 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getGoodsGroupListByCondition(GoodsGroupDto goodsGroupDto,PageModel pageModel) throws Exception {
		pageModel= goodsGroupDao.getGoodsGroupListByCondition(goodsGroupDto,pageModel);
		convertGoodsGroup(pageModel);
		pageModel.setList(DataConvertUtil.convertCollectionToListMap(pageModel.getList(),CommonResultConst.GET_SHOP_GOODS));
		return pageModel;
	}
	
	/**
	 * 转换商品族
	 * @Title: convertGoodsGroup 
	 * @param @param pageModel
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	private void convertGoodsGroup(PageModel pageModel)throws Exception
	{
		List<Long>goodsGroupIdList=new ArrayList<Long>();
		List<GoodsGroupDto>groupDtoList=pageModel.getList();
		Map<Long,GoodsGroupDto>groupMap=new HashMap<Long,GoodsGroupDto>();
		if(groupDtoList!=null)
		{
			for(GoodsGroupDto dto:groupDtoList)
			{
				groupMap.put(dto.getGoodsGroupId(), dto);
			}
		}
		for(GoodsGroupDto goodsGroupDto:groupDtoList)
		{
			String goodsLogo=DataCacheApi.get(CommonConst.KEY_GOODSGROUP_PICTURE+goodsGroupDto.getGoodsGroupId());
			if(StringUtils.isEmpty(goodsLogo))
			{
				goodsGroupIdList.add(goodsGroupDto.getGoodsGroupId());
			}
			else
			{
				goodsGroupDto.setGoodsLogo1(goodsLogo);
			}
		}
		AttachmentRelationDto attachmentRelationDto=new AttachmentRelationDto();
		attachmentRelationDto.setBizType(CommonConst.BIZ_TYPE_IS_GOODS_GROUP);
		attachmentRelationDto.setPicType(CommonConst.PIC_TYPE_IS_SUONUE);
		if(goodsGroupIdList.size()==0)
		{
			return;
		}
		List<AttachmentRelationDto>dtoList=attachmentRelationDao.findByConditionIn(attachmentRelationDto, goodsGroupIdList);
		for(AttachmentRelationDto dto:dtoList)
		{
			GoodsGroupDto groupDto=groupMap.get((long)dto.getBizId());
			if(groupDto!=null)
			{
				String logo=FdfsUtil.getFileProxyPath(dto.getFileUrl());
				DataCacheApi.setex(CommonConst.KEY_GOODSGROUP_PICTURE+dto.getBizId(), logo, 7200);//存放两个小时
				groupDto.setGoodsLogo1(logo);
			}
		}
	}
	@Override
	public List<GoodsGroupDto> getListByShopIdListAndNum(
			List<Long> needQueryShopIdForGoods, int num)throws Exception {
		return goodsGroupDao.getListByShopIdListAndNum(needQueryShopIdForGoods,num);
	}
	
}
