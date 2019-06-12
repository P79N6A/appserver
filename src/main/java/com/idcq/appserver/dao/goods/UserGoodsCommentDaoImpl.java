package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.UserGoodsCommentDto;

/**
 * 商品评论dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class UserGoodsCommentDaoImpl extends BaseDao<UserGoodsCommentDto>implements IUserGoodsCommentDao{
	
	public List<UserGoodsCommentDto> getGoodsComments(Long goodsId, Integer page,Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsId", goodsId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return  super.findList(generateStatement("getGoodsComments"), map);
	}

	public Integer getGoodsCommentsTotal(Long goodsId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getGoodsCommentsTotal"), goodsId);
	}
	
	
}
