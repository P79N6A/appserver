package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.shop.ILauncherIconDao;
import com.idcq.appserver.dto.shop.LauncherIconDto;
import com.idcq.appserver.utils.FdfsUtil;

@Service
public class LauncherServiceImpl implements ILauncherService{
		
	@Autowired
	private ILauncherIconDao launcherIconDao;
	
	
	@Override
	public List<LauncherIconDto> getLauncherIcons(int launcherType, long shopId) throws Exception{
		List<LauncherIconDto> iconList = launcherIconDao.getLauncherIconListByType(launcherType, shopId);
		if(iconList != null && iconList.size() > 0){
			String uri = null;
			String fileServerHost = FdfsUtil.getFileProxyServer();
			for(LauncherIconDto e : iconList){
				uri = e.getIconUrl();
				if(!StringUtils.isBlank(uri)){
					e.setIconUrl(FdfsUtil.getFileFQN(fileServerHost, uri));
				}
			}
		}
		return iconList;
	}


	@Override
	public List<Map> getAppNames(long shopId) throws Exception {
		return launcherIconDao.getAppNames(shopId);
	}

	
	
}
