/**
 * 
 */
package com.idcq.appserver.dao.common;

import java.util.List;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;

/** 
 * @ClassName: IUnitDao 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 下午5:52:22 
 *  
 */
public interface IUnitDao {
	
	public PageModel queryUnitByPage(PageModel pageModel);
	
	Unit queryUnitById(Long unitId)throws Exception;

	/**
	 * 根据单位名称获取通用服务（unit_type=2）和启用（status=1）的单位id
	 * 
	 * @Function: com.idcq.appserver.dao.common.IUnitDao.getUnitIdByName
	 * @Description:
	 *
	 * @param unitName
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午5:58:29
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	Long getUnitIdByName(String unitName, Integer digitScale, Long shopId);
	
	int addGoodsUnit(Unit unit);
	
	List<Unit> queryUnit(Unit condition);
}
