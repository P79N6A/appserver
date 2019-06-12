package com.idcq.appserver.service.file;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.enums.FileCategory;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;

/**
 * 该service提供通用的文件操作
 * @author zc
 * @createDate 2016-04-11
 */
public interface IFileService
{
    /**
     * 获得指定类型文件数量
     * @param category 文件类别
     * @param bizId 类别所性业务id,如shopId
     * @return
     */
    int getFileNums(FileCategory category, String bizId) throws Exception;

    /**
     * 获得指定文件类型中指定顺序的文件的二进制流与metadata
     * @param category 文件类别
     * @param bizId 如shopId
     * @param sequence 文件在该文件集中的次序, 为第sequence张，值的范围为>=1(不再校验)，不填默认为1；
     * @return
     */
    Map<String, Object> getFile(FileCategory category, String bizId, int sequence) throws Exception;
    
    /**
     * 获得指定文件类型中指定顺序的文件的二进制流与metadata
     * @param category 文件类别
     * @param bizId 如shopId
     * @param sequence 文件在该文件集中的次序, 为第sequence张，值的范围为>=1(不再校验)，不填默认为1；
     * @return
     */
    Map<String, Object> loadFile(Attachment attachment) throws Exception;

    /**
     * 为指定业务上传文件
     * @param mFile
     * @param fileInfo 内部必须包括bizId, bizType, mineType, picType,
     *        可包含picType,这个方法不会对必填与否进行校验
     * @return
     */
    void uploadFile(List<MultipartFile> mFiles, Map<String, String> fileInfo) throws Exception;

    /**
     * 这里注意除了该描述中提到的处理外，不会对attachment与attachmentRelation实体再做任何处理
     * @param attachment,当上传物理文件后将设置fileUrl,并会设置与mFile相关的信息
     * @param attachmentRelation
     *        ，插入attachment后将设置attachmentRelation中的attachmentId
     *        ,特别注意这里不会处理bizIndex
     * @param mFile
     */
    void uploadSingleFileForBiz(Attachment attachment, AttachmentRelationDto attachmentRelation, MultipartFile mFile)
            throws Exception;
    /**
     * 根据附件ID查询附件地址，或者根据附件表的bizId和bizType获取最新的附件地址
     * @param attachementId
     * @param bizId
     * @param bizType
     * @return
     * @author  shengzhipeng
     * @date  2016年7月23日
     */
    String getAttachmentUrl(Long attachementId, Long bizId, String bizType)throws Exception;
}
