package com.idcq.appserver.service.pageArea;

import java.util.List;

import com.idcq.appserver.dto.pageArea.PageAreaDto;
/**
 * 页面投放
 * @ClassName: IPageAreaService 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午3:27:11 
 *
 */
public interface IPageAreaService {
	
	public List<PageAreaDto>getPageAreaUrl(String positionType,String cityId)throws Exception;
}
