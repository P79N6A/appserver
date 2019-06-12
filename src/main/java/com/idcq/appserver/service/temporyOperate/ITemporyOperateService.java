package com.idcq.appserver.service.temporyOperate;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
/**
 * 
* @ClassName: ITemporyOperateService 
* @Description: TODO(记录用户的操作记录，主要是对需要建索引的表) 
* @author 张鹏程 
* @date 2015年3月28日 下午5:50:35 
*
 */
public interface ITemporyOperateService {
	
	/**
	 * 查询列表
	 * @param model
	 * @return
	 */
	public List<TemporyOperateDto> queryList(String type,int pageSize);
	
	/**
	 * 通过参数删除
	 * @param params
	 */
	public void deleteByParams(Map<String,Object>params);
}
