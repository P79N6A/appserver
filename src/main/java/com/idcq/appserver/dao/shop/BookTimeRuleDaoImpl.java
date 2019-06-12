package com.idcq.appserver.dao.shop;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.BookTimeRuleDto;

/**
 * 预定时间规则dao
 * 
 * @author Administrator
 * 
 * @date 2015年7月14日
 * @time 下午5:19:39
 */
@Repository
public class BookTimeRuleDaoImpl extends BaseDao<BookTimeRuleDto> implements IBookTimeRuleDao{

	public List<BookTimeRuleDto> getTimeRuleListBySettingId(Long settingId)
			throws Exception {
		return super.findList("getTimeRuleListBySettingId",settingId);
	}

	public int addTimeRuleDto(BookTimeRuleDto dto) throws Exception {
		return super.insert("addTimeRuleDto",dto);
	}

	public int delTimeRuleDtoBySettingId(Long settingId) throws Exception {
		return super.delete("delTimeRuleDtoBySettingId", settingId);
	}
	
	
	
}
