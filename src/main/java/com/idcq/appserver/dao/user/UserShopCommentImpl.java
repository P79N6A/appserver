package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.ShopCommentDto;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class UserShopCommentImpl extends BaseDao<ShopCommentDto> implements IUserShopCommentDao{
	
	public int makeCommentShop(ShopCommentDto shopCommentDto) throws Exception {
		return (Integer)super.insert(generateStatement("makeCommentShop"), shopCommentDto);
	}

	public List<ShopCommentDto> getShopCommentTimeById(
			ShopCommentDto shopCommentDto) {
		return (List)super.findList(generateStatement("getShopCommentTimeById"), shopCommentDto);
	}
	
	public void updateShopComLogo(Map map) throws Exception {
		super.update(generateStatement("updateShopComLogo"), map);
	}

	public List<ShopCommentDto> getShopCommentsByMap(Map map)
			throws Exception {
		return  super.findList(generateStatement("getShopCommentsByMap"), map);
	}
	
	/**
	 * 
	* @Title: getUserIdByCommentId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param commentId
	* @param @return
	* @param @throws Exception    设定文件 
	* @throws
	 */
	public Long getUserIdByCommentId(Long commentId) throws Exception {
		return (Long)super.selectOne(generateStatement("getUserIdByCommentId"),commentId);
	}

	public Integer getShopCommentsTotal(Long shopId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getShopCommentsTotal"), shopId);
	}

	@Override
	public Map<String, Object> getGradeById(Long shopId) throws Exception {
		return  (Map)super.selectOne(generateStatement("getGradeById"), shopId);
	}
	
	
}
