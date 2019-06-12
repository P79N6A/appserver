package com.idcq.appserver.service.file;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.service.plugins.IPluginsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.FileCategory;
import com.idcq.appserver.dao.common.IAttachmentDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.Attachment;
import com.idcq.appserver.dto.common.AttachmentRelationDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.idianmgr.dao.shop.IShopCashierDao;

@Service
public class FileServiceImpl implements IFileService
{
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private IShopDao shopDao;

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IShopCashierDao shopCashierDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IAttachmentRelationDao attachmentRelationDao;

    @Autowired
    private IPluginsService pluginsService;

    @Override
    public int getFileNums(FileCategory category, String bizId) throws Exception
    {
        int re = 0;
        try
        {
            if (category == null)
            {
                throw new ServiceException(5002, "文件类型不能为空");
            }
            log.debug("获得文件数量, category:" + category.name() + "bizId:" + bizId);
            // 放置查询条件
            Map<String, Object> searchMap = new HashMap<String, Object>();
            switch (category)
            {
                case SHOP_LOG: // 获得商铺logo的数量
                    // 设置搜索条件为商铺Id, 商铺-1，图片类型为缩略图-1
                    this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 1);
                    break;
                case SHOP_HOME_IMAGE: // 获得商铺图片（轮播图）的数量
                    // 设置搜索条件为商铺Id, 商铺-1，图片类型为轮播图-2
                    this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 2);
                    // re = shopDao.getBizLogoCount(searchMap);
                    break;
                case GOODS_IMAGE:
                    this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.GOODS.getValue(), 1);
                    // re = shopDao.getBizLogoCount(searchMap);
                    break;
                case SHOP_CONFIG_FILE:// 商铺配置文件
                    this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 3);
                    // re = shopDao.getBizLogoCount(searchMap);
                default:
                    break;
            }
            re = shopDao.getBizLogoCount(searchMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw e;
        }

        return re;
    }

    @Override
    public Map<String, Object> getFile(FileCategory category, String bizId, int sequence) throws Exception
    {
        Map<String, Object> result = null;
        if (category == null)
        {
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_NULL, "文件类型不能为空");
        }
        /* 搜索该种类该bizId下的所有文件数量 */
        int totalCount = this.getFileNums(category, bizId);
        if (totalCount < 1)
        {
            throw new ServiceException(CodeConst.CODE_NUM_OUT_BOUND, "指定类别下不存在文件");
        }
        if (sequence > totalCount)
        {
            throw new ServiceException(CodeConst.CODE_NUM_OUT_BOUND, "指定文件次序不存在");
        }

        Map<String, Object> fileInfo = this.getFileInfo(category, bizId, sequence);
        // 获得二进制文件与metadata
        result = FdfsUtil.downFileFromFtds((String) fileInfo.get("fileUrl"));
        // 适应文件名
        if (result.get("fileName") == null)
        {
            // 数据库中的文件名
            String dbFileName = (String) fileInfo.get("fileName");
            String type = (String) fileInfo.get("fileType");
            if (type != null && type.length() > 0 && dbFileName != null && dbFileName.indexOf(type) < 0)
            {
                dbFileName = dbFileName + "." + type;
            }
            result.put("fileName", dbFileName);
        }
        result.put("fileType", fileInfo.get("fileType"));
        return result;
    }

    /**
     * 获取文件信息
     * @param category
     * @param bizId
     * @param sequence
     * @return 文件名、类型、实际ftds地址等信息
     * @throws Exception
     */
    private Map<String, Object> getFileInfo(FileCategory category, String bizId, int sequence) throws Exception
    {
        /* 搜索该种类该bizId下的所有文件数量 */
        int totalCount = this.getFileNums(category, bizId);
        if (totalCount < 1)
        {
            throw new ServiceException(CodeConst.CODE_NUM_OUT_BOUND, "指定类别下不存在文件");
        }
        if (sequence > totalCount)
        {
            throw new ServiceException(CodeConst.CODE_NUM_OUT_BOUND, "指定文件次序不存在");
        }
        Map<String, Object> searchMap = new HashMap<String, Object>();
        /* 到指定位置搜索，以达到搜索优化的目的 */
        this.buildAttachementSearchMap(searchMap, bizId, category);
        searchMap.put("n", sequence - 1);
        searchMap.put("m", 1);
        Map<String, Object> resultMap = null;
        List<Map<String, Object>> resultMaps = shopDao.getBizLogo(searchMap);
        Map<String, Object> tempMap = resultMaps.get(0);
        System.out.println(null + "");
        if (totalCount == 1 || null == tempMap.get("bizIndex") || (sequence + "").equals("" + tempMap.get("bizIndex"))) // 如果搜索结果是唯一的或者bizIndex等于sequence则为取到值,或者bizIndex没有值
        {
            resultMap = tempMap;
        }
        else
        { // 否则遍历查询，这里（不采取分页查询）,每个指定类下图片不会很多
            searchMap.put("n", 0);
            searchMap.put("m", totalCount);
            resultMaps = shopDao.getBizLogo(searchMap);
            for (Map<String, Object> temp : resultMaps)
            {
                if ((sequence + "").equals("" + tempMap.get("bizIndex")))
                {
                    resultMap = temp;
                    break;
                }
            }
        }
        return resultMap;
    }

    /**
     * 将给定参数放至map里面，不包含分页数据
     * @param searchMap
     * @param bizId
     * @param bizType
     * @param picType
     */
    private void buildAttachementSearchMap(Map<String, Object> searchMap, String bizId, FileCategory category)
    {
        switch (category)
        {
            case SHOP_LOG: // 获得商铺logo的数量
                // 设置搜索条件为商铺Id, 商铺-1，图片类型为缩略图-1
                this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 1);
                break;
            case SHOP_HOME_IMAGE: // 获得商铺图片（轮播图）的数量
                // 设置搜索条件为商铺Id, 商铺-1，图片类型为轮播图-2
                this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 2);
                break;
            case GOODS_IMAGE:
                // 设置搜索条件为商品Id, 商铺-8，图片类型为轮播图-1
                this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.GOODS.getValue(), 1);
                break;
            case SHOP_CONFIG_FILE:
                // 获取商铺配置文件
                this.buildAttachementSearchMap(searchMap, bizId, BizTypeEnum.SHOP.getValue(), 3);
                break;
            default:
                break;
        }
    }

    /**
     * 将给定参数放至map里面，不包含分页数据
     * @param searchMap
     * @param bizId
     * @param bizType
     * @param picType
     */
    private void buildAttachementSearchMap(Map<String, Object> searchMap, String bizId, int bizType, int picType)
    {
        searchMap.put("bizId", bizId);
        searchMap.put("bizType", bizType);
        searchMap.put("picType", picType);
    }

    @Override
    public void uploadFile(List<MultipartFile> mFiles, Map<String, String> fileInfo) throws Exception
    {
        // 取参
        String userIdStr = fileInfo.get("userId");
        String mimeType = fileInfo.get("mimeType");
        String bizIdStr = fileInfo.get("bizId");

        String categoryCode = fileInfo.get("categoryCode");

        FileCategory fCategory = FileCategory.getFileCategory(categoryCode);
        if (fCategory == null)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "未受支持的categoryCode");
        }
        Map<String, String> categoryInfo = this.getCategoryInfo(fCategory);
        String bizTypeStr = categoryInfo.get("bizType");
        String picTypeStr = categoryInfo.get("picType");
        Integer bizType = NumberUtil.strToInteger(bizTypeStr, "bizType");
        Integer picType = NumberUtil.strToInteger(picTypeStr, "picType");
        // 参数校验并转化为指定类型
        Long bizId = NumberUtil.strToLong(bizIdStr, "bizId");

        /* 如果上传userId则校验userId是否正确，并且对应userId是否存在 */
        Long userId = NumberUtil.strToLong(userIdStr, "userId");
        if (userId != null)
        {
            // 验证用户的存在性
            UserDto userDB = this.userDao.getUserById(userId);
            if (null == userDB)
            {
                Map<String, Long> param = new HashMap<String, Long>();
                param.put("cashierId", userId);
                int count = shopCashierDao.findShopCashierExists(param);
                if (count == 0)
                {
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "操作用户不存在");
                }
            }
        }
        /* 初始化该次业务标准Attachment */
        Attachment attachment = new Attachment();
        attachment.setFileType(mimeType);
        attachment.setCreateTime(new Date());
        attachment.setUploadUserId(userId);
        attachment.setUploadUserType(1); // 用户上传
        attachment.setBizId(bizId);

        /* 初始化该次业务标准AttachmentRelation */
        AttachmentRelationDto attachmentRelation = new AttachmentRelationDto();
        attachmentRelation.setBizId(bizId);
        int existsNum = this.getFileNums(FileCategory.SHOP_CONFIG_FILE, bizId + "");
        attachmentRelation.setBizIndex(existsNum + 1);
        attachmentRelation.setBizType(bizType);
        attachmentRelation.setPicType(picType);

        // 上传文件
        for (MultipartFile mFile : mFiles)
        {
            // 初始化attachmentId
            attachment.setAttachmentId(null);
            // 还原attachmentRelation
            attachmentRelation.setAttachmentId(null);
            attachmentRelation.setAttachmentRelationId(null);

            // 调用保存
            this.uploadSingleFileForBiz(attachment, attachmentRelation, mFile);

            // 处理bizIndex递增
            attachmentRelation.setBizIndex(attachmentRelation.getBizIndex() + 1);
        }

    }

    /**
     * 根据文件类型获得文件类型的具体参数，如bizType,picType,这里以String形式统一返回，该处具体实现后续完善
     * @param fileCategory
     * @return
     */
    public Map<String, String> getCategoryInfo(FileCategory fileCategory)
    {
        Map<String, String> resultMap = new HashMap<String, String>();
        switch (fileCategory)
        {
            case SHOP_LOG:
                resultMap.put("bizType", 1 + "");
                resultMap.put("picType", 1 + "");
                break;
            case SHOP_HOME_IMAGE:
                resultMap.put("bizType", 1 + "");
                resultMap.put("picType", 2 + "");
                break;
            case SHOP_CONFIG_FILE:
                resultMap.put("bizType", 1 + "");
                resultMap.put("picType", 3 + "");
                break;
            case GOODS_IMAGE:
                resultMap.put("bizType", 8 + "");
                resultMap.put("picType", 1 + "");
                break;

            default:
                throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_VALID, "未受支持的文件类型" + fileCategory.getCode());
        }
        return resultMap;
    }

    /**
     * 这里注意除了该描述中提到的处理外，不会对attachment与attachmentRelation实体再做任何处理
     * @param attachment,当上传物理文件后将设置fileUrl,并会设置与mFile相关的信息
     * @param attachmentRelation
     *        ，插入attachment后将设置attachmentRelation中的attachmentId
     *        ,特别注意这里不会处理bizIndex
     * @param mFile
     */
    @Override
    public void uploadSingleFileForBiz(Attachment attachment, AttachmentRelationDto attachmentRelation,
            MultipartFile mFile) throws Exception
    {
        /* 上传物理文件 */
        String fileUrl = FdfsUtil.uploadFile(attachment.getFileType(), mFile);

        /* 处理attachment */
        // 设置与mFile相关的信息
        String fileName = mFile.getOriginalFilename();
        /*String tem=new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
        System.out.println(tem);
        return;*/
        attachment.setFileName(fileName);
        attachment.setFileSize(Double.valueOf(mFile.getSize() / 1024));
        // 设置所得的fileUrl
        attachment.setFileUrl(fileUrl);
        // 添加attachment
        attachmentDao.saveAttachment(attachment);

        // 添加attachmentRelation
        List<AttachmentRelationDto> list = new ArrayList<AttachmentRelationDto>();
        list.add(attachmentRelation);
        attachmentRelation.setAttachmentId(attachment.getAttachmentId());
        attachmentRelationDao.addAttachmentRelationBatch(list);
    }

    @Override
    public Map<String, Object> loadFile(Attachment attachment) throws Exception
    {
        Map<String, Object> params = attachment.getPluginDownladRecord();
        if(null != params && params.size() > 0)
        {
            log.debug("插入应用下载记录");
            pluginsService.recordPluginDownload((Integer) params.get("shopId"), (Integer)params.get("pluginId"), (Integer)params.get("attachmentId"), (String) params.get("sn"));
        }
        Map<String, Object> fileInfo = null;
        Map<String, Object> searchCondition = new HashMap<String, Object>();
        if(attachment.getBizId() != null){  //优先根据bizId与bizType获取文件，只取第一个
            searchCondition.put("bizId", attachment.getBizId());
            searchCondition.put("bizType", attachment.getBizType());
            List<Map<String, Object>> temp = shopDao.getAttachmentInfoAnd(searchCondition);
            if(temp != null && temp.size() > 0){
                fileInfo = temp.get(0);
            }
        }else if(attachment.getAttachmentId() != null){ //根据attachmentId获取
            searchCondition.put("attachmentId", attachment.getAttachmentId());
            fileInfo = shopDao.getAttachmentInfo(searchCondition);
        }
        Map<String, Object> result = null;
        if(fileInfo != null) {
            result = new HashMap<String, Object>();
            Map<String, Object> tem = FdfsUtil.downFileFromFtds((String) fileInfo.get("fileUrl"));
            if(null != tem){
                result.putAll(tem);
            }
            if (null != null && result.get("fileName") == null)
            {
                // 数据库中的文件名
                String dbFileName = (String) fileInfo.get("fileName");
                String type = (String) fileInfo.get("fileType");
                if (type != null && type.length() > 0 && dbFileName != null && dbFileName.indexOf(type) < 0)
                {
                    dbFileName = dbFileName + "." + type;
                }
                result.put("fileName", dbFileName);
            }
            result.put("fileType", fileInfo.get("fileType"));
        }
        return result;
    }

    @Override
    public String getAttachmentUrl(Long attachementId, Long bizId, String bizType) throws Exception {
        return attachmentDao.getAttachmentUrl(attachementId, bizId, bizType);
    }
}
