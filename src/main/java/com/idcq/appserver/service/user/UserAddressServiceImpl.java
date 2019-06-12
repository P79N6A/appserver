package com.idcq.appserver.service.user;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.region.ICitiesDao;
import com.idcq.appserver.dao.region.IProvinceDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.region.ITownDao;
import com.idcq.appserver.dao.user.IUserAddressDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;
import com.idcq.appserver.dto.user.UserAddressDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;


@Service
public class UserAddressServiceImpl implements IUserAddressService {

	@Autowired
	private IUserAddressDao userAddressDao;
	@Autowired
	public IUserDao userDao;
	@Autowired
	public ICitiesDao citiesDao;
	@Autowired
	public IRegionDao regionDao;
	@Autowired
	public IProvinceDao provinceDao;
	@Autowired
	public ITownDao townDao;
	
	public PageModel getListUserAddress(UserAddressDto dto, int pageNo, int pageSize)
			throws Exception {
		
		// 根据userId判断用户是否存在
		UserDto userDB = this.userDao.getUserById(dto.getUserId());
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		List<UserAddressDto> userAddressList = userAddressDao.getListUserAddress(dto, pageNo, pageSize);
		if(CollectionUtils.isNotEmpty(userAddressList)) {
			for (UserAddressDto userAddress : userAddressList) {
				
				//设置省、市、区
				setUserAdssInfo(userAddress);
			}
		}
		PageModel page = new PageModel();
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		page.setTotalItem(userAddressDao.getCountUserAddress(dto));
		page.setList(userAddressList);
		return page;
	}

	public int addUserAddress(UserAddressDto dto) throws Exception{
		Long userId = dto.getUserId();
		// 根据userId判断用户是否存在
		UserDto userDB = this.userDao.getUserById(userId);
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
		dto.setCreateTime(new Date());
		if(1==dto.getDefaultFlag()){//是默认地址
			//改掉原默认地址,userId+defaultFlag
			userAddressDao.updateDefaultFlag(userId);
			DataCacheApi.del(CommonConst.KEY_ADDRESS_ID+CommonConst.KEY_SEPARATOR_COLON+userId);
		}
		return userAddressDao.insertUserAddress(dto);
	}

	public int deleteUserAddress(UserAddressDto dto) throws Exception {
		// 根据userId判断用户是否存在
		UserDto userDB = this.userDao.getUserById(dto.getUserId());
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		return userAddressDao.deleteUserAddress(dto);
	}
	
	/**
	 * 设置用户的省、市、区、信息
	 * 
	 * @param user
	 * @throws Exception
	 */
	private void setUserAdssInfo(UserAddressDto userAddress) throws Exception {
		
		// 设置省、市、区信息
		ProvinceDto provinceDto = provinceDao.getProvinceById(userAddress
				.getProvinceId());
		if (null != provinceDto) {
			userAddress.setProvinceName(provinceDto.getProvinceName());
		}
		CitiesDto citiesDto = citiesDao.getCityById(userAddress.getCityId());
		if (null != citiesDto) {
			userAddress.setCityName(citiesDto.getCityName());
		}
		DistrictDto districtDto = regionDao.getDistrictById(userAddress
				.getDistrictId());
		if (null != districtDto) {
			userAddress.setDistrictName(districtDto.getDistrictName());
		}
		TownDto townDto = townDao.getTownById(userAddress.getTownId());
		if (null != townDto) {
			userAddress.setTownName(townDto.getTownName());
		}
	}


	public UserAddressDto getDefAddressByUser(UserAddressDto ua)
			throws Exception {
		return this.userAddressDao.getDefAddressByUser(ua);
	}

	public void setDefaultUserAddress(UserAddressDto dto) throws Exception {
	 
		Long userId = dto.getUserId();
		Long addressId = dto.getAddressId();
		
		// 根据userId判断用户是否存在
		UserDto userDB = this.userDao.getUserById(userId);
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
		UserAddressDto userAddressDto = this.userAddressDao.getAddressById(addressId);
		
		CommonValidUtil.validObjectNull(userAddressDto,
				CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_USERADDRESS_NONE);
		
		//将原来的默认地址设置为非默认
		userAddressDao.updateDefaultFlag(userId);
		
		//设置新的默认地址
		userAddressDao.updateSetDefaultFlag(addressId);
		
		//把原来的默认地址缓存删除掉
		DataCacheApi.del(CommonConst.KEY_ADDRESS_ID+CommonConst.KEY_SEPARATOR_COLON+userId);
	}

	public Long getDefAddressIdByUser(Long userId) throws Exception {
		return this.userAddressDao.getDefAddressIdByUser(userId);
	}

	public int updateUserAddress(UserAddressDto dto) throws Exception {
		
		// 根据userId判断用户是否存在
		Long userId = dto.getUserId();
		UserDto userDB = this.userDao.getUserById(userId);
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		if(1==dto.getDefaultFlag()){//是默认地址
			//改掉原默认地址,userId+defaultFlag
			userAddressDao.updateDefaultFlag(userId);
			DataCacheApi.del(CommonConst.KEY_ADDRESS_ID+CommonConst.KEY_SEPARATOR_COLON+userId);
		}  
		return this.userAddressDao.updateUserAddress(dto);
	}

	public UserAddressDto getAddressDetialById(Long addressId) throws Exception {
		CommonValidUtil.validPositLong(addressId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ADDR_ID);
		return this.userAddressDao.getAddressDetialById(addressId);
	}

	
}
