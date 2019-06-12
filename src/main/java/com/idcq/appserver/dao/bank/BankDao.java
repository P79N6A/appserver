/**
 * 
 */
package com.idcq.appserver.dao.bank;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bank.BankDto;
import com.idcq.appserver.dto.common.PageModel;

/** 
 * @ClassName: BankDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月15日 下午2:37:54 
 *  
 */
@Repository
public class BankDao  extends BaseDao<BankDto> implements IBankDao{

	/** 
	* @Title: getBankList 
	* @Description: TODO(分页获取银行列表) 
	* @param @param pageModel
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	public PageModel getBankList(PageModel pageModel) throws Exception {
		
		return super.findPagedList(generateStatement("getBankList"), generateStatement("getBankCount"), pageModel);
	}

	public Long getBankLogoByName(String bankName) {
		return (Long) super.selectOne(generateStatement("getBankLogoByName"),bankName);
	}

}
