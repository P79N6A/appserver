package com.idcq.appserver.service.poster.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.poster.IPosterActivityDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.service.poster.IPosterActivityService;
import com.idcq.appserver.utils.FdfsUtil;

@Service
public class PosterActivityServiceImpl implements IPosterActivityService{

	@Autowired
	private IPosterActivityDao posterActivityDao;
	@Autowired
	private IAttachmentDao attachmentDao; 
	
	@Override
	public List<Map> getPosterList() throws Exception {
		List<Map> list = attachmentDao.getAttachUrlListByCondition("一点管家商圈活动模版2016", "商圈活动海报模版");
		return list;
	}
	@Override
	public Map saveAttachment(Map map) throws Exception {
		/*map.put("fileSize", data.length);
		map.put("url", url);*/
		Attachment attachment=new Attachment();
		attachment.setBizId(Long.valueOf(String.valueOf(map.get("posterId"))));
		attachment.setBizType(String.valueOf(map.get("bizType")));
		attachment.setCreateTime(new Date());
		if(map.get("bizType")!=null){
			attachment.setFileName("商圈活动海报模版");
		}else{
			attachment.setFileName("商圈活动海报");
		}
		
		if(map.get("fileSize")!=null){
			attachment.setFileSize(Double.valueOf(Long.valueOf(map.get("fileSize").toString())/1024));
		}
		
		attachment.setFileType("jpeg");
		if(map.get("url")!=null){
			attachment.setFileUrl(map.get("url").toString());
		}
		
		attachment.setUploadUserId(null);
		attachment.setUploadUserType(null);
		attachmentDao.saveAttachmentByPoster(attachment);
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("attachementId", attachment.getAttachmentId());
		map1.put("fileUrl", FdfsUtil.getFileProxyPath(attachment.getFileUrl()));
		return map1;
	}
	
	
	

}
