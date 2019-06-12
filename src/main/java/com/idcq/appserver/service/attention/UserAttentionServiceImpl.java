package com.idcq.appserver.service.attention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.attention.IUserAttentionDao;
import com.idcq.appserver.dto.attention.UserAttentionDto;
import com.idcq.appserver.dto.common.PageModel;

@Service
public class UserAttentionServiceImpl implements IUserAttentionService {

	@Autowired
	private IUserAttentionDao userAttentionDao;
	
	public PageModel getMyAttention(int pageNo, int pageSize, long userId)
			throws Exception {
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		page.setList(userAttentionDao.getUserAttentionList(pageNo, pageSize, userId));
		page.setTotalItem(userAttentionDao.getUserAttentionTotal(userId));
		
		return page;
	}

	public int addUserAttention(UserAttentionDto userAttentionDto)
			throws Exception {
		return userAttentionDao.addUserAttention(userAttentionDto);
	}
	
	/**
	 * 获取用户是否关注此商铺
	 */
	public int getCountByUserIdAndShopId(long userId, long shopId) throws Exception {
		return userAttentionDao.getCountByUserIdAndShopId(userId, shopId);
	}
	
	/**
	 * 取消关注
	 */
	public int cancelUserAttention(long userId, long shopId) throws Exception {
		return userAttentionDao.cancelUserAttention(userId, shopId);
	}

}
