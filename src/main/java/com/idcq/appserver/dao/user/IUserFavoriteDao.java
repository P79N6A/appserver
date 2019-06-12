package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.UserFavoriteDto;
import com.idcq.appserver.dto.user.UserFavoriteHistoryDto;


public interface IUserFavoriteDao {
	
	/**
	 * 增加用户收藏信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Long insertUserFavorite(UserFavoriteDto userFavoriteDto) throws Exception;
	/**
	 * 增加用户收藏记录
	 * @param user
	 * @return
	 */
	Long insertUserFavoriteHistory(UserFavoriteHistoryDto userFavoriteHistory) throws Exception;
	/**
	 * 删除用户收藏信息
	 * @throws Exception
	 */
	Integer deleteUserFavorite(UserFavoriteDto userFavoriteDto)throws Exception;
	/**
	 * 获取单个用户收藏信息
	 * @param userFavoriteDto
	 * @return
	 */
	UserFavoriteDto getFavoriteInfo(UserFavoriteDto userFavoriteDto)throws Exception;
	/**
	 * 获取我的收藏店铺列表接口
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMyFavoriteShop(Map<String, Object> map)throws Exception;
	/**
	 * 获取我的收藏店铺列表接口
	 * @param map
	 * @return
	 */
	Integer getMyFavoriteShopCount(Map<String, Object> map)throws Exception;
	/**
	 * 获取我的收藏商品列表接口
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMyFavoriteGoods(Map<String, Object> map)throws Exception;
	/**
	 * 获取我的收藏商品列表接口
	 * @param map
	 * @return
	 */
	Integer getMyFavoriteGoodsCount(Map<String, Object> map)throws Exception;
}
