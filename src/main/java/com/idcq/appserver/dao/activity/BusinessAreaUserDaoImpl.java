package com.idcq.appserver.dao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.activity.BusinessAreaUserDto;

@Repository
public class BusinessAreaUserDaoImpl extends BaseDao<BusinessAreaUserDto>implements IBusinessAreaUserDao{

	@Override
	public int addBusinessAreaUser(
			BusinessAreaUserDto businessAreaUserDto) throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addBusinessAreaUser",businessAreaUserDto);
	}

	@Override
	public int delBusinessAreaUserById(Long businessAreaActivityId,Long memberId)
			throws Exception {
		// TODO Auto-generated method stub
		BusinessAreaUserDto businessAreaUserDto = new BusinessAreaUserDto();
		businessAreaUserDto.setBusinessAreaActivityId(businessAreaActivityId);
		businessAreaUserDto.setMemberId(memberId);
		return super.delete("delBusinessAreaUserById",businessAreaUserDto);
	}

	@Override
	public int delBusinessAreaUserByActivityId(Long businessAreaActivityId) throws Exception {
		return super.delete("delBusinessAreaUserByActivityId",businessAreaActivityId);
	}
	public int delBusinessAreaUserByShopId(Long shopId) throws Exception {
		return super.delete("delBusinessAreaUserByShopId",shopId);
	}
	@Override
	public int updateBusinessAreaUserById(
			BusinessAreaUserDto businessAreaUserDto) throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateBusinessAreaUserById",businessAreaUserDto);
	}

	@Override
	public BusinessAreaUserDto getBusinessAreaUserById(
			Long businessAreaActivityId,Long memberId) throws Exception {
		// TODO Auto-generated method stub
		BusinessAreaUserDto businessAreaUserDto = new BusinessAreaUserDto();
		businessAreaUserDto.setBusinessAreaActivityId(businessAreaActivityId);
		businessAreaUserDto.setMemberId(memberId);
		return (BusinessAreaUserDto)super.selectOne("getBusinessAreaUserById",businessAreaUserDto);
	}

	@Override
	public List<BusinessAreaUserDto> getBusinessAreaUserList(
			BusinessAreaUserDto businessAreaUserDto, int pageNo,
			int pageSize) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start", (pageNo - 1) * pageSize);
		map.put("limit", pageSize);
		return super.findList("getBusinessAreaUserList", map);
	}
	
	
}