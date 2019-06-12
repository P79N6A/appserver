package com.idcq.appserver.service.user;

import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.user.UserFavoriteDto;


public interface IUserFavoriteService {
	
	/**
	 * 增加用户收藏信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	void favorite(UserFavoriteDto userFavoriteDto) throws Exception;
	/**
	 * 获取用户收藏状态
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getFavoriteStatus(UserFavoriteDto userFavoriteDto) throws Exception;
	/**
	 * 查询商铺收藏信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public PageModel getMyFavoriteShop(Map<String, Object> map) throws Exception;
	/**
	 * 查询商品收藏信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public PageModel getMyFavoriteGoods(Map<String, Object> map) throws Exception;
}
