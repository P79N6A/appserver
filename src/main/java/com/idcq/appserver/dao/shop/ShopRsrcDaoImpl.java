package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopRsrcDto;
import com.idcq.appserver.dto.shop.ShopRsrcPramDto;

/**
 * 商铺资源dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午7:25:21
 */
@Repository
public class ShopRsrcDaoImpl extends BaseDao<ShopRsrcDto> implements IShopRsrcDao{

	public ShopRsrcDto getShopRsrcById(Long resourceId) throws Exception {
		return (ShopRsrcDto) super.selectOne("getShopRsrcById", resourceId);
	}
	
	public int useShopResource(Map param) throws Exception {
		return super.update("useShopResource", param);
	}

	public List<Map> getShopRsrcList(Long shopId, Long groupId) throws Exception {
		Map param=new HashMap();
		param.put("shopId",shopId);
		param.put("groupId",groupId);
		return (List)this.findList("getShopRsrcList", param);
	}

	@Override
	public int updateShopResource(ShopRsrcDto shopRsrcDto) throws Exception {
		return super.update("updateShopResource", shopRsrcDto);
	}

	@Override
	public List<ShopRsrcDto> getShopResourceList(ShopRsrcDto shopRsrcDto)
			throws Exception {
		return (List<ShopRsrcDto>)this.findList("getShopResourceList", shopRsrcDto);
	}
	
	@Override
	public List<Map> getShopGroupResourceList(Long shopId) {
		return (List)this.findList("getShopGroupResourceList", shopId);
	}

	public List<Map<String, Object>> getShopCategoryResource(Map<String, Object> map) {
		return (List)super.findList(generateStatement("getShopCategoryResource"),map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopRsrcDao#insertShopCategoryResource(com.idcq.appserver.dto.shop.ShopRsrcDto)
	 */
	@Override
	public Integer insertShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)
			throws Exception {
		return super.insert(generateStatement("insertShopCategoryResource"),shopRsrcPramDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopRsrcDao#updateShopCategoryResource(com.idcq.appserver.dto.shop.ShopRsrcDto)
	 */
	@Override
	public Integer updateShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)
			throws Exception {
		return super.update(generateStatement("updateShopCategoryResource"),shopRsrcPramDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopRsrcDao#deleteShopCategoryResource(com.idcq.appserver.dto.shop.ShopRsrcDto)
	 */
	@Override
	public Integer deleteShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)
			throws Exception {
			return super.delete(generateStatement("deleteShopCategoryResource"),shopRsrcPramDto);
	}
	@Override
	public int queryShopResourceExists(Long resourceId) throws Exception {
		return (int)super.selectOne("queryShopResourceExists", resourceId);
	}

	@Override
	public String getShopResourceName(Long resourceId) throws Exception {
		return (String)super.selectOne("getShopResourceName", resourceId);
	}

	@Override
	public int queryResourceExists(Long resourceId) throws Exception {
		return (int)super.selectOne("queryResourceExists", resourceId);
	}

	@Override
	public Long getCategoryIdByRsrId(Long resourceId) throws Exception {
		return (Long)super.selectOne("getCategoryIdByRsrId", resourceId);
	}

	@Override
	public Map<String, Object> getCategoryIdAndRsrNameByRsrId(Long resourceId)
			throws Exception {
		return (Map<String, Object>)super.selectOne("getCategoryIdAndRsrNameByRsrId", resourceId);
	}

    public List<String> getSeatNameBySeatIds(String[] seatIds)
    {
        return this.getSqlSession().selectList("getSeatNameBySeatIds", seatIds);
    }	
}
