package com.idcq.idianmgr.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 一点管家公共接口service
 * @author shengzhipeng
 * @date:2015年7月30日 上午11:44:09
 */
public interface IMgrCommonService {
	

	/**
	 * 上传文件
	 * @Function: com.idcq.idianmgr.service.common.ICommonService.commonUploadFile
	 * @Description: 上传文件接口，将信息保存到附件表，返回附件id和文件地址
	 *
	 * @param userId 用户Id
	 * @param mimeType 文件类型
	 * @param myfile 文件
	 * @param bizId 业务ID
	 * @param bizType 业务类型
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:szp
	 * @date:2015年7月30日 下午12:37:03
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日  shengzhipeng       v1.0.0         create
	 */
	Map<String, Object> commonUploadFile(Long userId, String mimeType, MultipartFile myfile, Long bizId, String bizType) throws Exception;
	
	/**
	 * 一点管家登录接口
	 * 
	 * @Function: com.idcq.idianmgr.service.common.IMgrCommonService.shopLogin
	 * @Description:
	 *
	 * @param mobile
	 * @param password
	 * @return 
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @param roleMode 
	 * @date:2015年8月3日 下午1:45:42
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月3日    shengzhipeng       v1.0.0         create
	 */
	Map shopLogin(String mobile, String password, Integer roleMode) throws Exception;

	Map shopManageLogin(String mobile, String password, Integer roleMode) throws Exception;

	void upgradeEmployee( Long shopId,Long employeeId, Long userId, String mobile,
			String password) throws Exception;

	Map<String, Object> saveLoginInfo(Map<String, Object> infoParams);

}
