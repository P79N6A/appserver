/**
 * 
 */
package com.idcq.appserver.dao.bank;

import com.idcq.appserver.dto.common.PageModel;

/** 
 * @ClassName: IBankDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月15日 下午2:37:42 
 *  
 */
public interface IBankDao {
	/**
	 * 
	* @Title: getBankList 
	* @Description: TODO(分页获取银行卡列表) 
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return PageModel    返回类型 
	* @throws
	 */
	public PageModel getBankList(PageModel pageModel)throws Exception;
	/**
	 * 根据银行卡名称查询logo
	 * @param bankName
	 */
	Long getBankLogoByName(String bankName);
}	
