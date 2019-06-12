package com.idcq.appserver.dao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaModelDto;

@Repository
public class BusinessAreaModelDaoImpl extends BaseDao<BusinessAreaModelDto> implements IBusinessAreaModelDao {

    @Override
    public int addBusinessAreaModel(BusinessAreaModelDto businessAreaModelDto) throws Exception {
        return super.insert("addBusinessAreaModel",businessAreaModelDto);
    }

    @Override
    public int delBusinessAreaModelById(Long businessAreaModelId) throws Exception {
        return super.delete("delBusinessAreaModelById", businessAreaModelId);
    }

    @Override
    public int updateBusinessAreaModelById(BusinessAreaModelDto businessAreaModelDto) throws Exception {
        return super.update("updateBusinessAreaModelById",businessAreaModelDto);
    }

    @Override
    public BusinessAreaModelDto getBusinessAreaModelById(Long businessAreaModelId) throws Exception {
        return (BusinessAreaModelDto)super.selectOne("getBusinessAreaModelById", businessAreaModelId);
    }

    @Override
    public List<BusinessAreaModelDto> getBusinessAreaModelList(BusinessAreaModelDto businessAreaModelDto, int pageNo,
            int pageSize) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bm", businessAreaModelDto);
        map.put("n", (pageNo-1)*pageSize);                   
        map.put("m", pageSize);    
        return super.findList(generateStatement("getBusinessAreaModelList"), map);
    }

}
