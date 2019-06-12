package com.idcq.appserver.service.commonconf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.commonconf.IUserFeedbackDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.commonconf.UserFeedbackDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;

@Service
public class UserFeedbackServiceImpl implements IUserFeedbackService{

	@Autowired
	private IUserFeedbackDao userFeedbackDao;
	@Autowired
	private IShopDeviceDao shopDeviceDao;
	@Autowired
	public IUserDao userDao;
	
	public int giveUserFeedback(UserFeedbackDto dto) throws Exception {
		
		// 根据userId判断用户是否存在
		UserDto userDB = this.userDao.getUserById(dto.getUserId());
		
		// 校验对象是否为空
		CommonValidUtil.validObjectNull(userDB,
						CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		
		return userFeedbackDao.giveFeedback(dto);
	}

	public Map queryVersion(Long userId, Long appId, String curVersion)
			throws Exception {
		Map pModel = new HashMap();
		//查询appId对应的信息是否存在
		/*
		int existenceAppInfo =  shopDeviceDao.queryAppInfo(appId);
		if (existenceAppInfo == 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_APP_DATA);
		}else{
			
		}
		*/
		Map appVersion = shopDeviceDao.queryAppVersion(appId);
		if (null == appVersion || appVersion.size() <= 0) {
		    return null;
		}else {
			String dbVersion = null ==appVersion.get("version_id")?null:appVersion.get("version_id")+"";
			if (CommonValidUtil.validVersionIsNew(curVersion,dbVersion )) {
				pModel.put("isLatest", appVersion.get("lastest_flag"));
				pModel.put("latestVersion", appVersion.get("version_id"));
				pModel.put("versionDesc", appVersion.get("version_desc"));
				String downloadUrl = FdfsUtil.getFileProxyPath((String) appVersion.get("download_url"));
				pModel.put("downloadUrl", downloadUrl);
				pModel.put("versionSize", appVersion.get("version_size"));
			}
		}
		return pModel;
	}

	@Override
	public UserFeedbackDto queryByFeedBackId(Long feedBackId)
			throws Exception {
		if(null==feedBackId)
		{
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_FEEDBACKID);
		}
		return null;
	}
	
}
