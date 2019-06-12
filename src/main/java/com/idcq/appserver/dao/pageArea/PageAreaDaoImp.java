package com.idcq.appserver.dao.pageArea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pageArea.PageAreaDto;
/**
 * 页面投放
 * @ClassName: PageAreaDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午2:34:15 
 *
 */
@Repository
public class PageAreaDaoImp extends BaseDao<PageAreaDto >implements IPageAreaDao{

	public List<PageAreaDto> getPageAreaUrl(String cityId, String positionType)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("cityId", cityId);
		params.put("positionType", positionType);
		return super.findList(generateStatement("getPageAreaUrl"), params);
	}

}
