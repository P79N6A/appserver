package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.UserShopCommentDto;

/**
 * 商铺评论dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class UserShopCommentDaoImpl extends BaseDao<UserShopCommentDto>implements IUserShopCommentDao{
	
	public List<UserShopCommentDto> getShopComments(Long shopId, int page, int pageSize)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return  super.findList(generateStatement("getShopComments"), map);
	}

	public Integer getShopCommentsTotal(Long shopId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getShopCommentsTotal"), shopId);
	}
	
	
}
