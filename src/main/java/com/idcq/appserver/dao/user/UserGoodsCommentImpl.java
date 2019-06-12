package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.GoodsCommentDto;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class UserGoodsCommentImpl extends BaseDao<GoodsCommentDto> implements IUserGoodsCommentDao{
	
	public int makeGoodsComment(GoodsCommentDto goodsCommentDao)
			throws Exception {
		return (Integer)super.insert(generateStatement("makeGoodsComment"), goodsCommentDao);
	}

	public List<GoodsCommentDto> getGoodsCommentTimeById(
			GoodsCommentDto goodsCommentDto) {
		return (List)super.findList(generateStatement("getGoodsCommentTimeById"), goodsCommentDto);
	}
	
	public void updateGoosComLogo(Map map) throws Exception {
		super.update(generateStatement("updateGoosComLogo"), map);
	}

	public List<GoodsCommentDto> getGoodSCommentsByMap(Map map) {
		return super.findList(generateStatement("getGoodSCommentsByMap"), map);
	}
	
	/**
	 * 根据评论编号找到用户编号
	 */
	public Long getUserIdByCommentId(Long commentId) throws Exception {
		return (Long)super.selectOne(generateStatement("getUserIdByCommentId"), commentId);
	}
	public Integer getGoodsCommentsTotal(Long goodsId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getGoodsCommentsTotal"), goodsId);
	}
}
