package com.idcq.appserver.dao.activity;

import java.util.List;

import com.idcq.appserver.dto.activity.BusinessAreaUserDto;


public interface IBusinessAreaUserDao {
	/**
	 * 新增商圈用户
	 * @param businessAreaUserDto
	 * @return
	 * @throws Exception
	 */
	int addBusinessAreaUser(BusinessAreaUserDto businessAreaUserDto) throws Exception;
	
	/**
	 * 删除指定的商圈用户
	 * @param businessAreaUserId
	 * @return
	 * @throws Exception
	 */
	int delBusinessAreaUserById(Long businessAreaActivityId,Long memberId) throws Exception;
	int delBusinessAreaUserByActivityId(Long businessAreaActivityId) throws Exception;
	int delBusinessAreaUserByShopId(Long shopId) throws Exception;
	/**
	 * 修改指定的商圈用户
	 * @param businessAreaUserDto
	 * @return
	 * @throws Exception
	 */
	int updateBusinessAreaUserById(BusinessAreaUserDto businessAreaUserDto) throws Exception;
	
	/**
	 * 获取指定的商圈用户
	 * @param businessAreaUserId
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	BusinessAreaUserDto getBusinessAreaUserById(Long businessAreaActivityId,Long memberId) throws Exception;
	
	/**
	 * 获取商圈用户列表
	 * @param businessAreaUserDto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<BusinessAreaUserDto> getBusinessAreaUserList(BusinessAreaUserDto businessAreaUserDto,int pageNo,int pageSize) throws Exception;
	
}