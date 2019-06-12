/**
 * 
 */
package com.idcq.appserver.service.bank;

import com.idcq.appserver.dto.common.PageModel;

/** 
 * @ClassName: IBankService 
 * @Description: TODO(银行列表操作) 
 * @author 张鹏程 
 * @date 2015年4月15日 下午2:35:31 
 *  
 */
public interface IBankService {
	
	
	public PageModel getBankList(Integer pNo,Integer pSize)throws Exception;
}
