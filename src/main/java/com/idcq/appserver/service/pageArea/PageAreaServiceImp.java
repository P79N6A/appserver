package com.idcq.appserver.service.pageArea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.pageArea.IPageAreaDao;
import com.idcq.appserver.dto.pageArea.PageAreaDto;
/**
 * 页面投放 
 * @ClassName: PageAreaServiceImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午3:26:14 
 *
 */
@Service
public class PageAreaServiceImp implements IPageAreaService{
	
	@Autowired
	private IPageAreaDao pageAreaDao;
	public List<PageAreaDto>getPageAreaUrl(String positionType,String cityId)throws Exception
	{
		return pageAreaDao.getPageAreaUrl(cityId, positionType);
	}
	
}
