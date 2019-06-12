package com.idcq.appserver.service.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.goods.*;

import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;

public interface IGoodsServcie {

	/**
	 * 验证商品存在性
	 * 
	 * @param shopId 商铺ID，非必填
	 * @param goodsId 商品ID，非必填
	 * @return
	 * @throws Exception
	 */
	int validGoodsExists(Long shopId, Long goodsId) throws Exception ;
	GoodsCategoryDto addGoodsCategory(Long goodsCategoryId,Long branchShopId) throws Exception;
	void updateGoods(Map<String, Object> requestMap,GoodsDto goodsDto,GoodsGroupDto goodsGroupDto,
			 List<GroupPropertyModel> groupPropertysModel,List<GoodsSetDto> goodsSetList) throws Exception;
	Map<String, Object> importGoodsByExcel(Long shopId,Integer templateType,MultipartFile file) throws Exception;
	/**
	 * 获取指定ID的商品
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	GoodsDto getGoodsById(Long id) throws Exception ;
	
	/**
	 * 获取指定的商品
	 * 
	 * @param shopId	非必填
	 * @param goodsId	必填
	 * @return
	 * @throws Exception
	 */
	GoodsDto getGoodsByIds(Long shopId, Long goodsId) throws Exception ;
	
	/**
	 * 获取热卖商品列表
	 * 
	 * @param topGoods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getTopGoodsList(TopGoodsDto topGoods, Integer page, Integer pageSize) throws Exception;
	
	/**
	 * 获取商品评论
	 * @param goodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsComments(Long goodsId, Integer page, Integer pageSize) throws Exception ;
	
	/**
	 * 获取商铺中的商品分类
	 * @param shopId
	 * @param page
	 * @param pageSize
	 * @param goodsType 
	 * @return
	 * @throws Exception
	 */
	PageModel getShopGoodsCategory(Long shopId,Integer columnId,String goodsGroupId,String parentCategoryId, Integer page, Integer pageSize, Integer goodsType) throws Exception ;
	
	/**
	 * 获取商品列表
	 * 
	 * @param goods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsList(GoodsDto goods, Integer page, Integer pageSize) throws Exception ;
	
	/**
	 * 为商品全文索引获取商品列表
	 * 
	 * @param goods
	 * @param startId
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsDto> getGoodsListToIndex(GoodsDto goods, Integer startId, Integer pageSize) throws Exception ;
	
	int getGoodsLastId() throws Exception;
	
	/**
	 * 查询商品列表
	 * @param idList
	 * @return
	 */
	List<GoodsDto> getGoodsListByIds(List<Long> idList);
	
	/**
	 * 针对查询搜索获得商品列表
	 * @param goods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsListForSearch(GoodsDto goods,PageModel pageModel,int... queryTotalCount) throws Exception ;
	
	/**
	 * 根据goodsSetId查询
	* @Title: getGoodsSetById 
	* @Description: TODO
	* @param @param goodsSetId
	* @param @return
	* @param @throws Exception
	* @return GoodsDto    返回类型 
	* @throws
	 */
	GoodsDto getGoodsSetById(Long goodsSetId)throws Exception;
	
	List<GoodsDto>getGoodsGroupByShopIdList(List<Long>shopIdList,Integer goodsNum)throws Exception;
	
	
	List<GoodsDto>getGoodsByGroupMap(Map<String,Object> parms)throws Exception;
	/**
	 * 收银机：获取搜菜品信息
	 * 
	 * @Function: com.idcq.appserver.service.goods.IGoodsServcie.getGoodsList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月20日 上午10:49:06
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月20日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getGoodsList(Map<String, Object> map)throws Exception;

	/**
	 * 获取商品分类，父子结构
	 * @param shopId
	 * @param pageNO
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getShopFullCategory(Long shopId, Integer page, Integer pageSize) throws Exception;

//	void saveGoodsBarcode(Map<String, String> goodsBarcodeMap);
	
	/** 
	* @Title: IGoodsServcie.java
	* @Description:  更新套餐。
	* @param @param goodsDto
	* @param @throws Exception    
	* @throws 
	*/
	public Long updateGoodsDtO(GoodsDto goodsDto,List<GoodsSetDto> goodsSetDtoList) throws Exception;
	
	/** 
	* @Title: IGoodsServcie.java
	* @Description: 求和出商品总价
	* @param @param list
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	Double sumGoodsPirce(List<GoodsSetDto> list) throws Exception;
	
	/** 
	* @Title: IGoodsSetDao.java
	* @Description: 查询出商铺的套餐list
	* @param @param param
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	public PageModel  getShopGoodsList(Map<String, Object> param,Integer page,Integer pageSize) throws Exception;

	/**
	 * 新增/修改商铺单位接口
	 * @param goodsUnitDto
	 */
	void updateGoodsUnit(GoodsUnitDto goodsUnitDto);
	
	/**
	 * 删除商铺单位成功
	 * @param result
	 */
	void delGoodsUnit(Map<String, Object> result);
	
	/**
	 * 查询商品单位
	 * @param result
	 * @param pageSize 
	 * @param pageNO 
	 * @return 
	 */
	PageModel getGoodsUnitList(Map<String, Object> result, Integer pageNO, Integer pageSize);
	/**
	 * 查询不属于该店铺的商铺单位
	 * @param unitId
	 * @param shopId
	 * @return
	 */
	int getGoodsUnit(Integer unitId);

	int getGoodsByUnitId(Integer unitId);

	Map<String, Object> isExistSameName(Map<String, Object> map);

	PageModel getGoodsSalestatistics(Map<String, Object> param);
	
	void syncGoods(SyncGoodsInfoDto syncGoodsInfo,List<ShopDto> branchShopList) throws Exception;

	Double getGoodsSaleSalestatistics(Map<String, Object> param);

	boolean processGoodsSoldOut(Long shopId, List<Map<String, String>> goodsList);

	List<GoodsSoldOutDto> getSoldOuts(Long shopId);
	/**
	 * 增加、更新增值服务
	 * 
	 * @Function: com.idcq.appserver.service.goods.IGoodsServcie.updateGoodsAvs
	 * @Description:
	 *
	 * @param updateGoodsAvs
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月20日 下午3:22:07
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月20日    ChenYongxin      v1.0.0         create
	 */
	public List<Map<String, Object>> updateGoodsAvs(UpdateGoodsAvs updateGoodsAvs) throws Exception;
	
	public List<GoodsAvsDto> getGoodsAvsList(GoodsAvsDto goodsAvs);
	public int getGoodsAvsCount(GoodsAvsDto goodsAvs);
	/**
	 * 删除增值服务
	 * 
	 * @Function: com.idcq.appserver.service.goods.IGoodsServcie.deleteGoodsAvs
	 * @Description:
	 *
	 * @param goodsAvsId
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月20日 下午3:21:51
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月20日    ChenYongxin      v1.0.0         create
	 */
	public int deleteGoodsAvs(Long goodsAvsId);
}
