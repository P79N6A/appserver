package com.idcq.appserver.dao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaShopDto;

@Repository
public class BusinessAreaShopDaoImpl extends BaseDao<BusinessAreaShopDto> implements IBusinessAreaShopDao {

    @Override
    public int addBusinessAreaShop(BusinessAreaShopDto businessAreaShopDto) throws Exception {
        return super.insert("addBusinessAreaShop",businessAreaShopDto);
    }

    @Override
    public int delBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("businessAreaActivityId", businessAreaActivityId);
        map.put("shopId", shopId);
        return super.delete("delBusinessAreaShopByCompKey", map);
    }

    @Override
    public int delBusinessAreaShopByActivityId(Long businessAreaActivityId)throws Exception {
    	return super.delete("delBusinessAreaShopByActivityId", businessAreaActivityId);
    }
    @Override
    public int updateBusinessAreaShopByCompKey(BusinessAreaShopDto businessAreaShopDto) throws Exception {
        return super.update("updateBusinessAreaShopByCompKey",businessAreaShopDto);
    }

    @Override
    public BusinessAreaShopDto getBusinessAreaShopByCompKey(Long businessAreaActivityId, Long shopId) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("businessAreaActivityId", businessAreaActivityId);
        map.put("shopId", shopId);
        return (BusinessAreaShopDto)super.selectOne("getBusinessAreaShopByCompKey", map);
    }

    @Override
    public List<BusinessAreaShopDto> getBusinessAreaShopList(BusinessAreaShopDto businessAreaShopDto, int pageNo,
            int pageSize) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", (pageNo-1)*pageSize);                   
        map.put("limit", pageSize);
        map.put("businessAreaShopDto", businessAreaShopDto);
        return super.findList(generateStatement("getBusinessAreaShopList"), map);
    }
    
    /**
     * 条件查询获取总条数
     * @Title: getBusinessAreaShopCount 
     * @param @param businessAreaShopDto
     * @param @return  
     * @throws
     */
	public Integer getBusinessAreaShopCount(
			BusinessAreaShopDto businessAreaShopDto) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessAreaShopDto", businessAreaShopDto);
        return (Integer)super.selectOne(generateStatement("getBusinessAreaShopCount"), map);
	}

}
