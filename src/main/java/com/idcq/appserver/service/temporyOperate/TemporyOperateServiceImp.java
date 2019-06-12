package com.idcq.appserver.service.temporyOperate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.temporyOperate.ITemporyOperateDao;
import com.idcq.appserver.dto.temporyOperate.TemporyOperateDto;
/**
 * 
* @ClassName: TemporyOperateServiceImp 
* @Description: TODO(对需要索引的表的增删改查记录) 
* @author 张鹏程 
* @date 2015年3月28日 下午5:53:12 
*
 */
@Service
public class TemporyOperateServiceImp implements ITemporyOperateService{
	
	
	@Autowired
	private ITemporyOperateDao temporyOperateDao;
	
	/**
	 * 根据类型和分页记录大小查找
	 */
	public List<TemporyOperateDto> queryList(String type,int pageSize) {
		return temporyOperateDao.queryList(type,pageSize);
	}

	public void deleteByParams(Map<String, Object> params) {
		temporyOperateDao.deleteByParams(params);
	}

}
