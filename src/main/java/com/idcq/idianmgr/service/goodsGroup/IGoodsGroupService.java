package com.idcq.idianmgr.service.goodsGroup;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupHandleDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;
import com.idcq.idianmgr.dto.goodsGroup.TmpGoodsGroupDto;
import com.idcq.idianmgr.dto.shop.TempCateGoryDto;

public interface IGoodsGroupService {
	
	/**
	 * 查询商品族内商品价格
	 * @param shopId
	 * @param goodsGroupId
	 * @return
	 * @throws Exception
	 */
	List<Map> getGoodsGroupGoodsPrice(Long shopId, Long goodsGroupId) throws Exception;
	
	public List getGoodsGroupPros(Map param)  throws Exception;
	
	public void delGroupPro(Map param) throws Exception;
	
	/**
	 * 更新/新增商品族属性
	 * @param goodsGroupPropertyDto
	 * @return
	 */
	public Long operateGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception;
	
	/**
	 * 更新/新增商品族属性值
	 * @param goodsGroupProValuesDto
	 * @return
	 * @throws Exception
	 */
	public Long operateGoodsGroupProVal(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception;

	/**
	 * 操作商品族内商品价格
	 * @param goodsGroupPropertyDto
	 * @return
	 * @throws Exception
	 */
	int updateGoodsPro(TmpGoodsGroupDto tmpGoodsGroupDto) throws Exception;
	
	GoodsGroupDto findGoodsGroupByGroupId(Long groupId)throws Exception;
	
	/**
	 * 新增/修改商品族
	 * @param goodsGroupHandleDto
	 * @return
	 * @throws Exception
	 */
	Long operateGoodsGroup(GoodsGroupHandleDto goodsGroupHandleDto) throws Exception;
	
	/**
	 * 更新商品状态
	 * @param shopId
	 * @param ggList
	 * @param operateType
	 * @return
	 * @throws Exception
	 */
	int updateGoodsGroupStatus(HttpServletRequest request) throws Exception;
	
	/**
	 * 分页获取商品族列表
	 * @param shopId
	 * @param goodsKey
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsGroupList(Long shopId,String goodsKey,Integer status,int pNo,int pSize) throws Exception;

	/**
	 * 查询场地分类定价接口
	 * @param shopId
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopPreResourceSetting(Long shopId, Long categoryId) throws Exception;

	/**
	 * 场地分类定价接口
	 * @param tempCateGoryDto
	 * @return
	 * @throws Exception
	 */
	int operatePreResource(TempCateGoryDto tempCateGoryDto) throws Exception;

	GoodsGroupDto getGoodsGroupById(long parseLong) throws Exception;
	
	/**
	 * 获取商品族列表
	 * @Title: getGoodsGroupListByShopId 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception
	 * @return PageModel    返回类型 
	 * @throws
	 */
	PageModel getGoodsGroupListByCondition(GoodsGroupDto goodsGroupDto,PageModel pageModel)throws Exception;
	
	/**
	 * 获取销量最高的两个商品族
	 * @Title: getListByShopIdListAndNum 
	 * @param @param needQueryShopIdForGoods
	 * @param @param i
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsGroupDto>    返回类型 
	 * @throws
	 */
	List<GoodsGroupDto> getListByShopIdListAndNum(
			List<Long> needQueryShopIdForGoods, int i)throws Exception;
	
	/**
	 * 刷新商品族及族内商品缓存
	 * @param shopId
	 * @param goodsGroupId
	 */
	public void refreshGroupGoodsCache(Long shopId,Long goodsGroupId);
	
}
