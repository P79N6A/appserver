package com.idcq.appserver.service.message;

import com.idcq.appserver.dao.message.IPushShopMsgDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.PushShopMsgDto;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.message.IPushMessageDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息service
 *
 * @author Administrator
 * @date 2015年3月4日
 * @time 下午3:57:03
 */
@Service public class PushMessageServiceImpl implements IPushMessageService
{

    private static final Logger log = LoggerFactory.getLogger(PushMessageServiceImpl.class);

//    private static final WeakHashMap<String, List<String>> cacheRegister = new WeakHashMap();

    @Autowired private IPushMessageDao pushMessageDao;

    @Autowired private IPushShopMsgDao pushShopMsgDao;


    public int saveRegistrationID(String registrationID) throws Exception
    {
        return pushMessageDao.saveRegistrationID(registrationID);
    }

    @Override public PageModel getPushMsgs(Map<String, Object> searchParams)
    {
        Integer bizType = (Integer) searchParams.get("bizType");
        //店铺消息
        if (bizType.intValue() == 1)
        {
            return this.getPushShopMessage(searchParams);
        }
        else if (bizType.intValue() == 2)   //用户消息
        {
            return this.getPushUserMessage(searchParams);
        }
        else
        {
            log.debug("不受支持的bizType类型：" + bizType);
            return null;
        }

    }

    /**
     * 这里采用二级缓存，缓存验证的是消息状态是否有消息待处理
     * @param searchParams
     * @return
     */
    protected PageModel getPushShopMessage(Map<String, Object> searchParams)
    {
//
        PageModel result = new PageModel();

        Long bizId = (Long) searchParams.get("bizId");

        Integer pollAroundFlag = (Integer) searchParams.get("pollAroundFlag");
        //是否是轮询
        boolean isPollAround = pollAroundFlag.intValue() == 1;

        List<String> cacheItems = null;

        String cacheKey = "shop_msgC_" + bizId;
        String cacheFlag = (String)searchParams.get("cacheFlag");
        //首先检查是否是轮询
        if (isPollAround)
        {
//            //先检查缓存，缓存命中则表示没有消息
//            cacheItems = cacheRegister.get(cacheKey);
//            if(null != cacheItems && cacheItems.contains(cacheFlag))    //一级缓存命中
//            {
//                return null;
//            }else {     //否则检查redis中的二级缓存
                try
                {       //兼容缓存不可用
                    cacheItems = (List<String>)DataCacheApi.getObject(cacheKey);
                    if(null != cacheItems && cacheItems.contains(cacheFlag))    //二级缓存命中
                    {
//                        cacheRegister.put(cacheKey, cacheItems);
                        return result;
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
//            }

        }
        List<PushShopMsgDto> dataList = null;
        //当是轮询但二级缓存都没命中的时候或者非轮询的时候进行查询数据库
        int rowNums = pushShopMsgDao.countMsgByConditions(searchParams);
        Integer pNo = (Integer)searchParams.get("pNo");
        if(rowNums > 0)
        {
            Integer pSize = (Integer)searchParams.get("pSize");
            searchParams.put("startNum", (pNo - 1) * pSize);
            searchParams.put("offset", pSize);
            dataList = pushShopMsgDao.searchMsgByConditions(searchParams);
        }

        result.setList(dataList);
        result.setTotalItem(rowNums);
        result.setToPage(pNo);

        //是论询的时候重置缓存,且查询结果为空时
        if(isPollAround && rowNums < 1)
        {
            cacheItems = cacheItems == null ? new ArrayList<String>() : cacheItems;
            cacheItems.add(cacheFlag);
//            cacheRegister.put(cacheKey, cacheItems);
            try
            {        //兼容缓存不可用
                DataCacheApi.setObject(cacheKey, cacheItems);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 未实现
     *
     * @param searchParams
     * @return
     */
    protected PageModel getPushUserMessage(Map<String, Object> searchParams)
    {
        //TODO
        log.debug("暂未支持获取用户消息的操作!");
        return null;
    }

    @Override public void informPushMsgs(Map<String, Object> requestParams)
    {
        Integer messageStatus = (Integer) requestParams.get("messageStatus");
        messageStatus = messageStatus == 1 ? 2 : messageStatus == 2 ? 3 : null;
        if(null == messageStatus)
        {
            log.error("bizType不为为空");
            return;
        }
        requestParams.put("messageStatus", messageStatus);
        Integer bizType = (Integer) requestParams.get("bizType");
        switch (bizType){
            case 1:
                this.informShopPushMsgs(requestParams);
                break;
            case 2:
                this.informUserPushMsgs(requestParams);
                break;
            default:
                log.info("unsupported bizType:" + bizType);
        }
    }

    private void informShopPushMsgs(Map<String, Object> requestParams){
        pushShopMsgDao.updateShopPushMsgStatus(requestParams);
    }

    private void informUserPushMsgs(Map<String, Object> requestParams){
        //TODO
        log.info("unsupported operation on push user msg.");
    }
}
