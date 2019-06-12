package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.ShopGoodsDto;

@Repository
public class ShopGoodsDaoImpl extends BaseDao<ShopGoodsDto> implements
		IShopGoodsDao {

	public List<ShopGoodsDto> getListShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy, Integer pageNo, Integer pageSize,String goodsServerMode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopId);
		map.put("goodsCategoryId", convertListToStr(goodsCategoryList));
		map.put("orderBy", orderBy);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		map.put("goodsServerMode", goodsServerMode);
		return super.findList(this.generateStatement("getListShopGoods"), map);
	}
	
	/**
	 * 将list转化为
	* @Title: convertListToStr 
	* @param @param categoryIdList
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	private String convertListToStr(List<Long>categoryIdList)
	{
		if(categoryIdList==null||categoryIdList.size()==0)
		{
			return null;
		}
		StringBuffer buffer=new StringBuffer("-1");
		for(Long categoryId:categoryIdList)
		{
			buffer.append(","+categoryId);
		}
		return buffer.toString();
	}
	public int getCountShopGoods(Long shopId, List<Long> goodsCategoryList, Integer orderBy,String goodsServerMode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopId);
		map.put("goodsCategoryId", convertListToStr(goodsCategoryList));
		map.put("orderBy", orderBy);
		map.put("goodsServerMode", goodsServerMode);
		return (Integer)super.selectOne(this.generateStatement("getCountShopGoods"), map);
	}

	
	public ShopGoodsDto getDtoShopGoods(Long goodsId) {
		
		return (ShopGoodsDto)super.selectOne(this.generateStatement("getDtoShopGoods"), goodsId);
	}

	
	public String getDtoProValueName(String proValuesId){
		return (String) super.selectOne(this.generateStatement("getDtoProValueName"), proValuesId);
	}
	
	@Override
	public List<ShopGoodsDto> getListShopGoodsByCondition(
			ShopGoodsDto shopGoodsDto, PageModel pageModel) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopGoodsDto.getShopId());
		map.put("goodsCategoryId", convertListToStr(shopGoodsDto.getGoodsCategoryList()));
		map.put("orderBy", shopGoodsDto.getOrderBy());
		map.put("n", (pageModel.getToPage()-1)*pageModel.getPageSize());
		map.put("m", pageModel.getPageSize());
		map.put("goodsServerMode", shopGoodsDto.getGoodsServerModeParam());
		map.put("goodsStatus", shopGoodsDto.getGoodsStatus());
		map.put("goodsName", shopGoodsDto.getGoodsName());
		map.put("goodsType", shopGoodsDto.getGoodsType());
		map.put("storageAlarmType", shopGoodsDto.getStorageAlarmType());
		map.put("isNeedCheck", shopGoodsDto.getIsNeedCheck());
		map.put("pinyinCode", shopGoodsDto.getPinyinCode());
		map.put("queryData", shopGoodsDto.getQueryData());
		
		map.put("barcode", shopGoodsDto.getBarcode());
		map.put("storageNum", shopGoodsDto.getStorageNum());
		map.put("orderByMode", shopGoodsDto.getOrderByMode());
		map.put("searchRange", shopGoodsDto.getSearchRange());
		return super.findList(this.generateStatement("getListShopGoods"), map);
	}

	@Override
	public int getCountShopGoods(ShopGoodsDto shopGoodsDto, PageModel pageModel) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopGoodsDto.getShopId());
		map.put("goodsCategoryId", convertListToStr(shopGoodsDto.getGoodsCategoryList()));
		map.put("goodsServerMode", shopGoodsDto.getGoodsServerModeParam());
		map.put("goodsStatus", shopGoodsDto.getGoodsStatus());
		map.put("goodsName", shopGoodsDto.getGoodsName());
		map.put("goodsType", shopGoodsDto.getGoodsType());
		map.put("storageAlarmType", shopGoodsDto.getStorageAlarmType());
		map.put("isNeedCheck", shopGoodsDto.getIsNeedCheck());
		map.put("queryData", shopGoodsDto.getQueryData());
		
		map.put("barcode", shopGoodsDto.getBarcode());
		map.put("storageNum", shopGoodsDto.getStorageNum());
		map.put("orderByMode", shopGoodsDto.getOrderByMode());
		map.put("searchRange", shopGoodsDto.getSearchRange());
		return (Integer)super.selectOne(this.generateStatement("getCountShopGoods"), map);
	}

	@Override
	public Map<String, Object> getShopGoodsStatisticsByCondition(
			ShopGoodsDto shopGoodsDto, PageModel pageModel) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopGoodsDto.getShopId());
		map.put("goodsCategoryId", convertListToStr(shopGoodsDto.getGoodsCategoryList()));
		map.put("orderBy", shopGoodsDto.getOrderBy());
		map.put("n", (pageModel.getToPage()-1)*pageModel.getPageSize());
		map.put("m", pageModel.getPageSize());
		map.put("goodsServerMode", shopGoodsDto.getGoodsServerModeParam());
		map.put("goodsStatus", shopGoodsDto.getGoodsStatus());
		map.put("goodsName", shopGoodsDto.getGoodsName());
		map.put("goodsType", shopGoodsDto.getGoodsType());
		map.put("storageAlarmType", shopGoodsDto.getStorageAlarmType());
		map.put("isNeedCheck", shopGoodsDto.getIsNeedCheck());
		map.put("pinyinCode", shopGoodsDto.getPinyinCode());
		map.put("queryData", shopGoodsDto.getQueryData());
		
		map.put("barcode", shopGoodsDto.getBarcode());
		map.put("storageNum", shopGoodsDto.getStorageNum());
		map.put("orderByMode", shopGoodsDto.getOrderByMode());
		return  (Map<String, Object>)super.selectOne(this.generateStatement("getShopGoodsStatisticsByCondition"), map);
	}
	
}
