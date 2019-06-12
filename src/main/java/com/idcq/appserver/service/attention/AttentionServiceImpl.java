package com.idcq.appserver.service.attention;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.attention.IAttentionDao;
import com.idcq.appserver.dto.attention.AttentionDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.exception.ValidateException;


/**
 * 我的关注service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:01:02
 */
@Service
public class AttentionServiceImpl implements IAttentionServcie{

	@Autowired
	public IAttentionDao attentionDao;

	public PageModel getAttenList(AttentionDto atten, int page, int pageSize)
			throws Exception {
		if(atten.getMemberId() <= 0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_MEMBER);
		}
//		List<AttentionDto> list = this.attentionDao.getAttenList(atten, page, pageSize);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		List<AttentionDto> list = new ArrayList<AttentionDto>();
		AttentionDto sh = new AttentionDto();
		sh.setId(1);
		sh.setMemberId(3);
		list.add(sh);
		pm.setList(list);
		return pm;
	}
	
	
	
}
