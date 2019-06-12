package com.idcq.appserver.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.UserAddressDto;

@Repository
public class UserAddrressDaoImpl extends BaseDao<UserAddressDto> implements
		IUserAddressDao {

	public UserAddressDto getAddressDetialById(Long addressId) throws Exception {
		return (UserAddressDto)super.selectOne(generateStatement("getAddressDetialById"), addressId);
	}

	public List<UserAddressDto> getListUserAddress(UserAddressDto dto, int pageNo,
			int pageSize) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", dto.getUserId());
		map.put("defaultFlag", dto.getDefaultFlag());
		map.put("n", (pageNo-1)*pageSize);
		map.put("m",pageSize);
		return super.findList(generateStatement("getListUserAddress"), map);
	}

	public int getCountUserAddress(UserAddressDto dto) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("userId", dto.getUserId());
		map.put("defaultFlag", dto.getDefaultFlag());
		return (Integer)super.selectOne(generateStatement("getCountUserAddress"), map);
	}

	public int insertUserAddress(UserAddressDto dto) {
		return super.insert(generateStatement("insertUserAddress"), dto);
	}

	public int deleteUserAddress(UserAddressDto dto) {
		return super.delete(generateStatement("deleteUserAddress"), dto);
	}

	public UserAddressDto getDefAddressByUser(UserAddressDto ua)
			throws Exception {
		return (UserAddressDto)super.selectOne(generateStatement("getDefAddressByUser"), ua);
	}
	
	public int updateDefaultFlag(Long userId){
		
		return super.update(generateStatement("updateDefaultFlag"), userId);
		
	}

	public List<UserAddressDto> getListUserAddressByUsreId(Long userId)
			throws Exception {
		return super.findList(generateStatement("getListUserAddressByUsreId"), userId);
	}
	
	public UserAddressDto getAddressById(Long addressId) {
		return (UserAddressDto) super.selectOne(generateStatement("getAddressById"), addressId);
	}

	public int updateSetDefaultFlag(Long addressId) throws Exception {
		
		return super.update(generateStatement("updateSetDefaultFlag"), addressId);
	}

	public Long getDefAddressIdByUser(Long userId) throws Exception {
//		//查缓存
//		String redisVal = DataCacheApi.get(CommonConst.KEY_ADDRESS_ID+CommonConst.KEY_SEPARATOR_COLON+userId);
//		if(!StringUtils.isBlank(redisVal)){
//			return Long.valueOf(redisVal);
//		}else{
//			//查库
//			Long addressId = (Long)super.selectOne(generateStatement("getDefAddressIdByUser"), userId);
//			//入缓存
//			DataCacheApi.set(CommonConst.KEY_ADDRESS_ID+CommonConst.KEY_SEPARATOR_COLON+userId, addressId+"");
//			return addressId;
//		}
		return (Long)super.selectOne(generateStatement("getDefAddressIdByUser"), userId);
	}

	public int updateUserAddress(UserAddressDto dto) throws Exception{
		return super.update(generateStatement("updateUserAddress"), dto);
	}
	
}
