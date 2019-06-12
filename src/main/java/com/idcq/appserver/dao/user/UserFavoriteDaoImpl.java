package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.UserFavoriteDto;
import com.idcq.appserver.dto.user.UserFavoriteHistoryDto;
@Repository
public class UserFavoriteDaoImpl  extends BaseDao<UserFavoriteDto> implements IUserFavoriteDao {

	@Override
	public Long insertUserFavorite(UserFavoriteDto userFavoriteDto)
			throws Exception {
		return (long) super.insert(generateStatement("insertUserFavorite"),userFavoriteDto);
	}

	@Override
	public Long insertUserFavoriteHistory(UserFavoriteHistoryDto userFavoriteHistory) throws Exception {
		return (long) super.insert(generateStatement("insertUserFavoriteHistory"),userFavoriteHistory);
	}

	@Override
	public Integer deleteUserFavorite(UserFavoriteDto userFavoriteDto) throws Exception {
		return super.delete(generateStatement("deleteUserFavorite"),userFavoriteDto);
	}

	@Override
	public UserFavoriteDto getFavoriteInfo(UserFavoriteDto userFavoriteDto) {
		return (UserFavoriteDto) super.selectOne(generateStatement("getFavoriteInfo"), userFavoriteDto);
	}

	@Override
	public List<Map<String, Object>> getMyFavoriteShop(Map<String, Object> map)
			throws Exception {
		return (List)super.findList(generateStatement("getMyFavoriteShop"), map);
	}

	@Override
	public Integer getMyFavoriteShopCount(Map<String, Object> map)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getMyFavoriteShopCount"), map);
	}

	@Override
	public List<Map<String, Object>> getMyFavoriteGoods(Map<String, Object> map)
			throws Exception {
		return (List)super.findList(generateStatement("getMyFavoriteGoods"), map);
	}

	@Override
	public Integer getMyFavoriteGoodsCount(Map<String, Object> map)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getMyFavoriteGoodsCount"), map);
	}

}
