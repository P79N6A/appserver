package com.idcq.appserver.dao.activity;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaStatDto;

@Repository
public class BusinessAreaStatDaoImpl extends BaseDao<BusinessAreaStatDto>implements IBusinessAreaStatDao{

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaStatDao#getBusinessAreaStatById(com.idcq.appserver.dto.activity.BusinessAreaStatDto)
     */
    @Override
    public BusinessAreaStatDto getBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto)
    {
        return (BusinessAreaStatDto) super.selectOne("getBusinessAreaStatByCompKey", businessAreaStatDto);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaStatDao#insertSelective(com.idcq.appserver.dto.activity.BusinessAreaStatDto)
     */
    @Override
    public int addBusinessAreaStat(BusinessAreaStatDto businessAreaStatDto)
    {
        return super.insert("addBusinessAreaStat", businessAreaStatDto);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaStatDao#updateSelective(com.idcq.appserver.dto.activity.BusinessAreaStatDto)
     */
    @Override
    public int updateBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto)
    {
        return super.update("updateBusinessAreaStatByCompKey", businessAreaStatDto);

    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.activity.IBusinessAreaStatDao#deleteSelective(com.idcq.appserver.dto.activity.BusinessAreaStatDto)
     */
    @Override
    public int delBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto)
    {
        return super.update("delBusinessAreaStatByCompKey", businessAreaStatDto);
    }

	@Override
	public int addUpSharedNumByCompKey(BusinessAreaStatDto businessAreaStatDto) {
		// TODO Auto-generated method stub
		return super.update("addUpSharedNumByCompKey", businessAreaStatDto);
	}

	@Override
	public int addUpReadNumByCompKey(BusinessAreaStatDto businessAreaStatDto) {
		// TODO Auto-generated method stub
		return super.update("addUpReadNumByCompKey", businessAreaStatDto);
	}

	@Override
	public int addUpTransmitNumByCompKey(BusinessAreaStatDto businessAreaStatDto) {
		// TODO Auto-generated method stub
		return super.update("addUpTransmitNumByCompKey", businessAreaStatDto);
	}

	@Override
	public int getCountCompKey(BusinessAreaStatDto businessAreaStatDto) {
		return (int)super.selectOne("getCountCompKey", businessAreaStatDto);
	}
	
	
    
    

}