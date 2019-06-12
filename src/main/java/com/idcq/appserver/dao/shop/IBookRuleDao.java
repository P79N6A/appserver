package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.BookRuleDto;

public interface IBookRuleDao {
	
	/**
	 * 根据ID获取指定的预定资源规则
	 * 
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	BookRuleDto getBookRuleById(Long ruleId) throws Exception;
	/**
	 * 获取通用预定设置费用接口
	 * @param shopId
	 * @param pSize
	 * @param pNo
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getShopBooktFeeSetting(Long shopId,Integer pSize,Integer pNo) throws Exception;
	/**
	 * 获取通用预定设置费用总记录数
	 * @param shopId
	 * @return
	 */
	Integer getShopBooktFeeSettingCount(Long shopId);
	
	List<Map<String, Object>> getShopBookRule(Map param) throws Exception;
}
