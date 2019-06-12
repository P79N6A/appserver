package com.idcq.appserver.service.advertise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.advertise.IAdvertiseDao;
import com.idcq.appserver.dto.advertise.AdvertiseDto;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 广告service
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午9:03:53
 */
@Service
public class AdvertiseServiceImpl implements IAdvertiseService {

    @Autowired
    public IAdvertiseDao advertiseDao;

    public PageModel getAdListByCity(AdvertiseDto advertise, int page, int pageSize) throws Exception {
        PageModel pageModel = new PageModel();
        int rCount = this.advertiseDao.getAdListCount(advertise);
        List<AdvertiseDto> list = null;
        if(rCount > 0) {
            // 检索列表
            list = this.advertiseDao.getAdList(advertise, page, pageSize);
        }
        // 获取总记录数
        pageModel.setTotalItem(rCount);
        // 页容量
        pageModel.setPageSize(pageSize);
        // 页码
        pageModel.setToPage(page);
        pageModel.setList(list);
        return pageModel;
    }

    public String getAdListInCacheByCity(AdvertiseDto advertise, int page, int pageSize) throws Exception {
        // 緩存key = ADS:cityId:1001:adSpaceCode:index:pNo:1:pSize:10
        Long cityId = advertise.getCityCode();
        String adSpaceCode = advertise.getAdPosId();
        String cacheKey = "ADS:cityId:" + cityId + ":adSpaceCode:" + adSpaceCode + ":pNo:" + page + ":pSize:"
                + pageSize;
        String cacheValue = DataCacheApi.get(cacheKey);
        if (cacheValue != null) {
            //緩存中查询到了直接返回
            return cacheValue.toString();
        } else {
            String result = null;
            int rCount = this.advertiseDao.getAdListCount(advertise);
            if (rCount > 0) {
                //查询到数量大于0才去查询列表，可以减少不必要的查询
                List<AdvertiseDto> list = this.advertiseDao.getAdList(advertise, page, pageSize);
                // 拼凑logo url
                for (AdvertiseDto e : list) {
                    String picUrl = e.getAdPic();
                    if (!StringUtils.isBlank(picUrl)) {
                        e.setAdPic(FdfsUtil.getFileProxyPath(picUrl));
                    }
                }
                Map<String, Object> pModel = new HashMap<String, Object>();
                pModel.put("pNo", page);
                pModel.put("count", rCount);
                pModel.put("lst", list);
                result = ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取广告列表成功", pModel, DateUtils.DATETIME_FORMAT);
                // 将结果放入redis缓存中,缓存30分钟
                DataCacheApi.setex(cacheKey, result, 1800);
                // key值加入key列表缓存起来
                saveADkeyToKeyList(cacheKey, cityId, adSpaceCode);
            } else {
                Map<String, Object> pModel = new HashMap<String, Object>();
                pModel.put("pNo", page);
                pModel.put("count", rCount);
                pModel.put("lst", new ArrayList<ColumnDto>());
                result = ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取广告列表成功", pModel, DateUtils.DATETIME_FORMAT);
            }
            return result;
        }
    }

    /**
     * 添加广告列表缓存对象key的值到key值缓存列表中
     * 如A为广告列表，B为A缓存的key值，此方法的作用是缓存B
     * @param key 缓存A的Key值B
     * @param cityId 城市ID
     * @param adSpaceCode 广告位编码
     * @author  shengzhipeng
     * @date  2016年2月13日
     */
    private void saveADkeyToKeyList(String key, Long cityId, String adSpaceCode) {
        //广告列表key的缓存对象的key值=adKeyList:cityId:1001:adSpaceCode:index
        String adListKey = CommonConst.KEY_AD_KEYLIST + ":cityId:" + cityId + ":adSpaceCode:" + adSpaceCode;
        List<String> val = (List<String>) DataCacheApi.getObject(adListKey);
        if (val == null) {
            val = new ArrayList<String>();
        }
        val.add(key);
        DataCacheApi.setObject(adListKey, val);
    }
}
