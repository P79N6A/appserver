package com.idcq.appserver.service.user;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.user.UserAddressDto;

public interface IUserAddressService {
	/**
	 * 获取指定地址的详细信息
	 * 
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	UserAddressDto getAddressDetialById(Long addressId) throws Exception;
	
	/**
	 * 获取用户地址列表
	 */
	PageModel getListUserAddress(UserAddressDto dto,int pageNo,int pageSize) throws Exception ;
	
	/**
	 * 新增用户地址
	 */
	int addUserAddress(UserAddressDto dto) throws Exception;
	
	/**
	 * 删除用户地址
	 */
	int deleteUserAddress(UserAddressDto dto) throws Exception;
	
	/**
	 * 获取用户默认收件地址
	 * 
	 * @param ua
	 * @return
	 * @throws Exception
	 */
	UserAddressDto getDefAddressByUser(UserAddressDto ua) throws Exception;	
	
	/**
	 * 获取指定用户的默认收货地址
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Long getDefAddressIdByUser(Long userId) throws Exception;	
	
	/**
	 * 设置默认地址
	 * @param dto
	 * @throws Exception
	 */
	void setDefaultUserAddress(UserAddressDto dto) throws Exception;	
	
	/**
	 * 更新地址
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int updateUserAddress(UserAddressDto dto)throws Exception;	
}
