package com.idcq.appserver.dao.user;

import java.util.List;

import com.idcq.appserver.dto.user.UserAddressDto;

public interface IUserAddressDao {
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
	List<UserAddressDto> getListUserAddress(UserAddressDto dto,int pageNo,int pageSize) ;
	
	
	/**
	 * 获取用户地址总数
	 */
	int getCountUserAddress(UserAddressDto dto) ;
	
	
	/**
	 * 新增用户地址
	 */
	int insertUserAddress(UserAddressDto dto);
	
	
	/**
	 * 删除用户地址
	 */
	int deleteUserAddress(UserAddressDto dto) ;
	
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
	
	int updateDefaultFlag(Long userId);
	
	/**
	 * 根据Id查询地址
	 * @param addressId
	 * @return
	 */
	public UserAddressDto getAddressById(Long addressId);
	
	/**
	 * 根据用户userId查询用户地址，将默认地址排在首位
	 * @param userId
	 * @return
	 */
	List<UserAddressDto> getListUserAddressByUsreId(Long userId) throws Exception;	
	
	/**
	 * 跟新为默认地址
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	int updateSetDefaultFlag(Long addressId) throws Exception;	
	
	/**
	 * 更新地址
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int updateUserAddress(UserAddressDto dto)throws Exception;	
}
