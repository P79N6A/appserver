package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.BookRuleDto;

@Service
public class BookRuleDaoImpl extends BaseDao<BookRuleDto> implements IBookRuleDao{

	public BookRuleDto getBookRuleById(Long ruleId) throws Exception {
		return (BookRuleDto)super.selectOne(generateStatement("getBookRuleById"), ruleId);
	}

	@Override
	public List<Map<String, Object>> getShopBooktFeeSetting(Long shopId,
			Integer pSize, Integer pNo) throws Exception {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("shopId", shopId);
		map.put("n", (pNo-1)*pSize);
		map.put("m",pSize);
		return (List)super.findList(generateStatement("getShopBooktFeeSetting"), map);
	}

	@Override
	public Integer getShopBooktFeeSettingCount(Long shopId) {
		return (Integer) super.selectOne(generateStatement("getShopBooktFeeSettingCount"), shopId);
	}

	@Override
	public List<Map<String, Object>> getShopBookRule(Map param) throws Exception {
		return (List)super.findList(generateStatement("getShopBookRule"), param);
	}
	
}
