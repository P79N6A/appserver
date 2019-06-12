/**
 * 
 */
package com.idcq.appserver.dao.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;

/** 
 * @ClassName: UnitDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 下午5:52:40 
 *  
 */
@Repository
public class UnitDaoImp extends BaseDao<Unit> implements IUnitDao {

	/** 
	* @Title: queryUnitByPage 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param pageModel
	* @param @return  
	* @throws 
	*/
	@Override
	public PageModel queryUnitByPage(PageModel pageModel) {
		return super.findPagedList(generateStatement("getUnitByPage"), generateStatement("getUnitTotalCount"), pageModel);
	}

	@Override
	public Unit queryUnitById(Long unitId) throws Exception {
		return (Unit)HandleCacheUtil.getEntityCacheByClass(Unit.class, unitId, 21600);
	}
	
	public Long getUnitIdByName(String unitName, Integer digitScale, Long shopId) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("unitName", unitName);
	    map.put("digitScale", digitScale);
	    map.put("shopId", shopId);
		return super.getSqlSession().selectOne(generateStatement("getUnitIdByName"), map);
	}

	public int addGoodsUnit(Unit unit) {
		return insert(generateStatement("addGoodsUnit"), unit);
	}

    @Override
    public List<Unit> queryUnit(Unit condition)
    {   
        if(null == condition){
            return null;
        }
        return super.getSqlSession().selectList(generateStatement("queryUnit"), condition);
    }
	
	
}
