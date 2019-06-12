/**
 * 
 */
package com.idcq.appserver.service.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.bank.IBankDao;
import com.idcq.appserver.dto.common.PageModel;

/** 
 * @ClassName: BankServiceImp 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 张鹏程 
 * @date 2015年4月15日 下午2:37:04 
 *  
 */
@Service
public class BankServiceImp implements IBankService {
	
	
	@Autowired
	private IBankDao bankDao;
	/** 
	* @Title: getBankList 
	* @Description: TODO(获取银行列表) 
	* @param @param pageModel
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public PageModel getBankList(Integer pNo,Integer pSize) throws Exception {
		PageModel pageModel = new PageModel();
		pageModel.setPageSize(pSize);
		pageModel.setToPage(pNo);
		return bankDao.getBankList(pageModel);
	}

}
