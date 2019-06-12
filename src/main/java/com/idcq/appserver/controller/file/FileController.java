package com.idcq.appserver.controller.file;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.FileCategory;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.file.DownloadUtils;
import com.idcq.appserver.service.file.IFileService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 文件操作controller，目前主要处理文件获取功能
 * @author zc
 * @createDate 2016-04-11
 */
@Controller
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String GET_FILE_COUNT = "c";

    @Autowired
    private IFileService fileService;

    /**
     * 获得文件
     * @param category 文件种类
     * @param bizId 业务id，如所需的shopId
     * @param sequence 为数据型，所取第几个文件；当为字符c时，获得该种类业务的文件数量
     * @return 文件或者文件数量
     */
    @RequestMapping(value =
    { "/f/{category}/{bizId}/{sequence}" })
    public void getFile(HttpServletResponse response, @PathVariable("category") String category,
            @PathVariable("bizId") String bizId, @PathVariable("sequence") String sequence)
    {
        log.debug("is getting file count:" + sequence);
        Map<String, Object> result = null;
        try
        {
            // 是否是获取文件数据
            boolean isGetCount = false;
            // 文件次序，不填默认1
            int fSequence = 1;

            // 文件种类，必填
            FileCategory fCategory = FileCategory.getFileCategory(category);
            if (fCategory == null)
            {
                throw new IllegalArgumentException();
            }
            log.debug("获得:" + fCategory.name());
            // 获得文件数量
            if (null != sequence && GET_FILE_COUNT.equalsIgnoreCase(sequence.trim()))
            {
                log.debug("这次请求获得文件数量");
                isGetCount = true;
            }
            else
            // 获得指定位置文件url
            {
                if (null != sequence)
                {
                    fSequence = Integer.valueOf(sequence);
                    if (fSequence < 1)
                    {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "sequence次序不能为负");
                    }
                }
            }
            if (!isGetCount) // 取文件流
            {
                Map<String, Object> map = fileService.getFile(fCategory, bizId, fSequence);
                byte[] data = (byte[]) map.get("file");
                response.setHeader("Content-Disposition", DownloadUtils
                        .getContentDisposition((String) map.get("fileType"), (String) map.get("fileName")));
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType(DownloadUtils.getContentType((String) map.get("fileType")));
                OutputStream out = response.getOutputStream();
                out.write(data);
                out.flush();
                out.close();
            }
            else
            // 获得指定文件数量
            {
                Integer count = fileService.getFileNums(fCategory, bizId);
                result = new HashMap<String, Object>();
                result.put("count", count);
                ResultDto dto = ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取文件数量成功", result);
                response.setContentType("text/javascript;charset=UTF-8");
                Writer writer = response.getWriter();
                writer.write(JacksonUtil.object2Json(dto));
                writer.flush();
                writer.close();
            }
            // ResultUtil.getResult(code, msg, map)
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            throw new APISystemException("操作失败-系统异常", e);
        }
    }
    
    @RequestMapping(value =
        { "/token/common/downloadFile", "/session/common/downloadFile",
                "/service/common/downloadFile" }, method = RequestMethod.GET)
        public void dowmloadFile(HttpServletResponse response, String attachementId, HttpServletRequest request, String bizType, String bizId, Integer pluginId, String sn, Integer shopId)
        {
            log.debug("download file" + attachementId);
            boolean byAttachmentId = !StringUtils.isEmpty(attachementId);
            boolean byBiz = (!StringUtils.isEmpty(bizId)) && (!StringUtils.isEmpty(bizType));
            if( (!byAttachmentId) && (!byBiz)){
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "attachementId或 bizType、 bizId不能全为空");
            }
            try
            {       
                    Attachment att = new Attachment();
                    if(byBiz){
//                        att.setBizId(Long.parseLong(bizId));
                        att.setBizId(CommonValidUtil.validStrLongFmt(bizId, CodeConst.CODE_PARAMETER_NOT_VALID, "bizId格式不正确"));
                        String encodedBizType = null;
                        try {
                            encodedBizType = new String(bizType.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            log.debug(e.getMessage(), e);
                            encodedBizType = bizType;
                        }
                        att.setBizType(encodedBizType);
                    }else if(byAttachmentId){
                        att.setAttachmentId(CommonValidUtil.validStrLongFmt(attachementId, CodeConst.CODE_PARAMETER_NOT_VALID, "attachementId格式不正确"));
                    }
                  //  boolean record = pluginId != null || sn != null;
                    if(pluginId != null || sn != null) {
                        if ((pluginId == null || sn == null || shopId == null)) {
                            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, "pluginId与sn同传或者同时不传,当pluginId与sn存在时shopId必填");
                        }
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("shopId", shopId);
                        params.put("pluginId", pluginId);
                        params.put("sn", sn);
                        params.put("attachmentId", Integer.valueOf(attachementId));
                        att.setPluginDownladRecord(params);
                    }

                    Map<String, Object> map = fileService.loadFile(att);
                    if(null != map) {
                        byte[] data = (byte[]) map.get("file");
                        response.setHeader("Content-Disposition", DownloadUtils
                                .getContentDisposition((String) map.get("fileType"), (String) map.get("fileName")));
                        response.addHeader("Content-Length", "" + data.length);
                        response.setContentType(DownloadUtils.getContentType((String) map.get("fileType")));
                        OutputStream out = response.getOutputStream();
                        out.write(data);
                        out.flush();
                        out.close();
                    } else {
                        throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "文件不存在");
                    }
            }
            catch (ServiceException e)
            {
                throw new APIBusinessException(e);
            }
            catch (Exception e)
            {
                throw new APISystemException("操作失败-系统异常", e);
            }
        }


    /**
     * 为业务上传文件
     * @param request
     * @return
     */
    @RequestMapping(value =
    { "/file/commonUploadFile", "/token/file/commonUploadFile", "/session/file/commonUploadFile",
            "/service/file/commonUploadFile" }, method = RequestMethod.POST)
    @ResponseBody
    public Object uploadFileByBiz(HttpServletRequest request) throws Exception
    {
        try
        {
            MultipartRequest mRequest = (MultipartRequest) request;
            /* 获取请求参数，并校验必填参数,但不会校验请求参数类型 */
            String userIdStr = RequestUtils.getQueryParam(request, "userId");
            String mimeType = RequestUtils.getQueryParam(request, "mimeType");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
            String categoryCode = RequestUtils.getQueryParam(request, "categoryCode");
            // String bizType = RequestUtils.getQueryParam(request, "bizType");
            // String picType = RequestUtils.getQueryParam(request, "picType");
            CommonValidUtil.validStrNull(mimeType, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "mimeType" + CodeConst.MSG_REQUIRED_NOT_NULL);
            CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "bizId" + CodeConst.MSG_REQUIRED_NOT_NULL);
            CommonValidUtil.validStrNull(categoryCode, CodeConst.CODE_PARAMETER_NOT_NULL,
                    "categoryCode" + CodeConst.MSG_REQUIRED_NOT_NULL);
            // CommonValidUtil.validStrNull(bizType,
            // CodeConst.CODE_PARAMETER_NOT_NULL, "bizType"
            // + CodeConst.MSG_REQUIRED_NOT_NULL);
            // CommonValidUtil.validStrNull(picType,
            // CodeConst.CODE_PARAMETER_NOT_NULL, "picType"
            // + CodeConst.MSG_REQUIRED_NOT_NULL);
            // Long userId = NumberUtil.strToLong(userIdStr, "userId");
            // Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");
            /* 设置文件信息 */
            Map<String, String> fileInfo = new HashMap<String, String>();
            fileInfo.put("userId", userIdStr);
            fileInfo.put("mimeType", mimeType);
            fileInfo.put("bizId", bizIdStr);
            fileInfo.put("categoryCode", categoryCode);
            // fileInfo.put("bizType", bizType);
            // fileInfo.put("picType", picType);
            List<MultipartFile> mFiles = new ArrayList<MultipartFile>();
            Iterator<Entry<String, MultipartFile>> it = mRequest.getFileMap().entrySet().iterator();
            while (it.hasNext())
            {
                mFiles.add(it.next().getValue());
            }
            fileService.uploadFile(mFiles, fileInfo);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            throw new APISystemException("上传失败-系统异常", e);
        }
        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_UPLOADFILE, null);
    }
    
    /**
     * 为业务上传文件
     * @param request
     * @return
     */
    @RequestMapping(value =
    {"/token/common/getAttachmentUrl", "/session/common/getAttachmentUrl",
            "/service/common/getAttachmentUrl" }, method = RequestMethod.GET)
    @ResponseBody
    public Object getAttachmentUrl(HttpServletRequest request) throws Exception
    {
        try
        {
            /* 获取请求参数，并校验必填参数,但不会校验请求参数类型 */
            String attachementIdStr = RequestUtils.getQueryParam(request, "attachementId");
            String bizIdStr = RequestUtils.getQueryParam(request, "bizId");
            String bizType = RequestUtils.getQueryParam(request, "bizType");
            Long attachementId = null;
            Long bizId = null;
            if(StringUtils.isBlank(attachementIdStr)) {
                CommonValidUtil.validStrNull(bizIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                        "bizId" + CodeConst.MSG_REQUIRED_NOT_NULL);
                CommonValidUtil.validStrNull(bizType, CodeConst.CODE_PARAMETER_NOT_NULL,
                        "bizType" + CodeConst.MSG_REQUIRED_NOT_NULL);
                bizId = NumberUtil.strToLong(bizIdStr, "bizId");
            } else {
                attachementId = NumberUtil.strToLong(attachementIdStr, "attachementId");
                
            }
            String fileUrl  = fileService.getAttachmentUrl(attachementId, bizId, bizType);
            fileUrl = FdfsUtil.getFileProxyPath(fileUrl);
            Map<String, String> map = new HashMap<String, String>();
            map.put("attachmentUrl", fileUrl);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "调用成功", map);
        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            throw new APISystemException("上传失败-系统异常", e);
        }
       
    }

}
