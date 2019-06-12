package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.ShopCommentDto;


public interface IUserShopCommentDao {
	
	/**
	 * 增加商铺评论
	 * 
	 * @param userGoodsCommentDao
	 * @return
	 * @throws Exception
	 */
	int makeCommentShop(ShopCommentDto shopCommentDto) throws Exception;
	/**
	 * 根据userId、goodid 获取商品列表
	 * @param goodsCommentDto
	 * @return
	 */
	List<ShopCommentDto> getShopCommentTimeById(ShopCommentDto shopCommentDto);


	/**
	 * 更新商铺评论图片
	 * @param map
	 * @throws Exception
	 */
	void updateShopComLogo(Map map) throws Exception ;
	
	/**
	 * 根据用户id和商铺id查询最近的一条评论
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<ShopCommentDto> getShopCommentsByMap(Map map) throws Exception ;
	
	/** 
	* @Title: getUserIdByCommentId 
	* @Description: TODO(根据评论编号找到用户编号) 
	* @param @param commentId    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	Long getUserIdByCommentId(Long commentId) throws Exception;
	/**
	 * 查询shop评论总数
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public Integer getShopCommentsTotal(Long shopId) throws Exception;
	/**
	 * 查询商铺平均分
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getGradeById(Long shopId) throws Exception;
}
