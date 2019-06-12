package com.idcq.appserver.service.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.city.ICityDao;
import com.idcq.appserver.dao.column.IColumnDao;
import com.idcq.appserver.dao.help.IHelpCategoryDao;
import com.idcq.appserver.dao.help.IHelpDao;
import com.idcq.appserver.dao.message.IMessageDao;
import com.idcq.appserver.dao.region.ICitiesDao;
import com.idcq.appserver.dao.region.IProvinceDao;
import com.idcq.appserver.dao.region.IRegionDao;
import com.idcq.appserver.dao.region.ITownDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.city.CityDto;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.HelpOf1dsxyDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.help.HelpCategoryDto;
import com.idcq.appserver.dto.help.HelpDto;
import com.idcq.appserver.dto.message.MessageDto;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 首页service
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:56:25
 */
@Service
public class HomeServiceImpl implements IHomeServcie {

	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	public ICityDao cityDao;
	@Autowired
	public IColumnDao columnDao;
	@Autowired
	public IMessageDao messageDao;
	@Autowired
	public IRegionDao regionDao;
	@Autowired
	public IProvinceDao provinceDao;
	@Autowired
	public ICitiesDao citiesDao;
	@Autowired
	public IUserDao userDao;
	@Autowired
	public ITownDao townDao;
	@Autowired
	public IHelpDao helpDao;
	@Autowired
	public IHelpCategoryDao helpCategoryDao;

	public PageModel getCityList(CityDto city, int page, int pageSize)
			throws Exception {
		if (city.getpCode() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MEMBER);
		}
		// List<OrderDto> list = this.cityDao.getCityList(city, page, pageSize);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		List<CityDto> list = new ArrayList<CityDto>();
		CityDto sh = new CityDto();
		sh.setId(1);
		sh.setpCode(2);
		list.add(sh);
		pm.setList(list);
		return pm;
	}

	public PageModel getColumnList(ColumnDto column, int page, int pageSize)
			throws Exception {
		CommonValidUtil.validPositLong(column.getCityId(),
				CodeConst.CODE_PARAMETER_NOT_VALID,
				CodeConst.MSG_FORMAT_ERROR_CITYID);
		int num = this.columnDao.getColumnListCount(column);
		List<ColumnDto> list = this.columnDao.getColumnList(column, page,
				pageSize);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(num);
		pm.setList(list);
		return pm;
	}

	public String getColumnListInCache(ColumnDto column, int page, int pageSize)
			throws Exception {
		// 栏目列表的缓存Key=COLUMN:cityId:1001:parentColumnId:1:columnType:1:pNo:1:pSize:10
		Long cityId = column.getCityId();
		Long parentColumnId = column.getParentColumnId();
		Integer columnType = column.getColumnType();
		Integer shopClassify = column.getShopClassify();
		String cacheKey = "COLUMN:cityId:" + cityId + ":parentColumnId:" + parentColumnId +":columnType:"+columnType+ ":shopClassify:" + shopClassify+ ":pNo:" + page + ":pSize:" + pageSize;
		String cacheValue = DataCacheApi.get(cacheKey);
		//缓存存在直接返回，不存在则查询数据库
		if (cacheValue != null) {
			return cacheValue.toString();
		} else {
		    String result = null;
		    int rCount = this.columnDao.getColumnListCount(column);
		    if (rCount > 0) {
		        List<ColumnDto> list = this.columnDao.getColumnList(column, page, pageSize);
                if(CollectionUtils.isNotEmpty(list)){
                    for(ColumnDto columnDto : list){
                        String url = columnDto.getColumnImgUrl();
                        if(!StringUtils.isBlank(url)){
                            columnDto.setColumnImgUrl(FdfsUtil.getFileProxyPath(url));
                        }
                        // 判断是否有子分类
                        if(columnDao.hasChildrenColumn(columnDto.getColumnId())){
                            columnDto.setHasChildren(1);
                        }else {
                            columnDto.setHasChildren(0);
                        }
                    }
                }
                Map<String, Object> pModel = new HashMap<String, Object>();
                pModel.put("pNo", page);
                pModel.put("count", rCount);
                pModel.put("lst", list);
                result = ResultUtil.getResultJson(0, "获取首页栏目列表成功", pModel,DateUtils.DATETIME_FORMAT);
                // 将结果放入redis缓存中,缓存30分钟
                DataCacheApi.setex(cacheKey, result, 1800);
            } else {
                Map<String, Object> pModel = new HashMap<String, Object>();
                pModel.put("pNo", page);
                pModel.put("count", rCount);
                pModel.put("lst", new ArrayList<ColumnDto>());
                result = ResultUtil.getResultJson(0, "获取首页栏目列表成功", pModel,DateUtils.DATETIME_FORMAT);
            }
		    return result;
		}
	
	}

	public PageModel getMessageList(MessageDto msg, int page, int pageSize)
			throws Exception {
		Integer msgType = msg.getMsgType();
		DataCacheApi dataCacheApi = DataCacheApi.getInstance();
		PageModel pageModel = null;
		// 系统消息：如果不存在key，查询数据库缓存到redis，如果存在直接返回
		if (null != msgType && 0 == msgType) {
			pageModel = (PageModel) dataCacheApi.getObject("system:message:"
					+ page + ":" + pageSize);
			if (null == pageModel) {
				pageModel = getMessagePageModel(msg, page, pageSize);
				dataCacheApi.setObjectEx("system:message:" + page + ":"
						+ pageSize, pageModel, 60);
			}
		}
		if (null != msgType && 1 == msgType) {// 商铺消息
			pageModel = (PageModel) dataCacheApi.getObject("shop:message:"
					+ page + ":" + pageSize + ":" + msg.getShopId());
			if (null == pageModel) {
				pageModel = getMessagePageModel(msg, page, pageSize);
				dataCacheApi.setObjectEx("shop:message:" + page + ":"
						+ pageSize + ":" + msg.getShopId(), pageModel, 60);
			}
		}
		if (null == msgType) {// 查询所有
			pageModel = (PageModel) dataCacheApi.getObject("all:message:"
					+ page + ":" + pageSize);
			if (null == pageModel) {
				pageModel = getMessagePageModel(msg, page, pageSize);
				dataCacheApi
						.setObjectEx("all:message:" + page + ":" + pageSize,
								pageModel, 60);
			}

		}
		return pageModel;
	}

	/**
	 * 数据库查询消息记录
	 */
	public PageModel getMessagePageModel(MessageDto msg, int page, int pageSize)
			throws Exception {
		PageModel pageModel = new PageModel();
		int num = this.messageDao.getMsgListCount(msg);
		List<MessageDto> list = new ArrayList<MessageDto>();
		if (num > 0) {
			list = this.messageDao.getMsgList(msg, page, pageSize);
			list = updateImgUrl(list);
			pageModel.setToPage(page);
			pageModel.setPageSize(pageSize);
			pageModel.setTotalItem(num);
			pageModel.setList(list);
		}
		return pageModel;
	}

	/**
	 * 增加图片url绝对路径返回
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<MessageDto> updateImgUrl(List<MessageDto> list)
			throws Exception {
		List<MessageDto> newlist = new ArrayList<MessageDto>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (MessageDto messageDto : list) {
				String imgUrl = messageDto.getMsgImg();
				imgUrl = FdfsUtil.getFileProxyPath(imgUrl);
				messageDto.setMsgImg(imgUrl);
				newlist.add(messageDto);
			}
		}
		return newlist;
	}

	@Override
	public Integer addMessage(MessageDto messageDto) {
		return messageDao.addMessage(messageDto);
	}

	public PageModel getDistricts(Long cityId, int page, int pageSize)
			throws Exception {
	    int rCount = this.regionDao.getDistrictsTotal(cityId);
	    List<DistrictDto> list = null;
	    if (rCount > 0) {
	        list = this.regionDao.getDistricts(cityId, page,
	                pageSize);
	    }
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(rCount);
		pm.setList(list);
		return pm;
	}

	public List<ProvinceDto> getAllProvinces() throws Exception {
		List<ProvinceDto> list = this.provinceDao.getAllProvinces();
		return list;
	}

	public PageModel getCitis(Long provinceId, String cityName, int page,
			int pageSize) throws Exception {
	    int rCount = this.citiesDao.getCitisTotal(provinceId, cityName);
	    List<CitiesDto> list = null;
	    if (rCount > 0) {
	        list = this.citiesDao.getCitis(provinceId, cityName,
	                page, pageSize);
	    }
		
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(rCount);
		pm.setList(list);
		return pm;
	}

	public PageModel getTowns(Long districtId, int page, int pageSize)
			throws Exception {
	    int rCount = this.townDao.getTownsTotal(districtId);
	    List<TownDto> list = null;
	    if (rCount > 0) {
	        list = this.townDao.getTowns(districtId, page, pageSize);
	    }
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(rCount);
		pm.setList(list);
		return pm;
	}

	public List<Map> getAllCitis() throws Exception {
		List<Map> list = this.citiesDao.getAllCitis();
		return list;
	}

	public Map<String, Object> getCityInfoByName(String cityName)
			throws Exception {
		Map<String, Object> map = this.citiesDao.getCityInfoByName(cityName);
		return map;
	}

	@Override
	public PageModel getHelpList(HelpDto help, int page, int pageSize)
			throws Exception {

		int num = this.helpDao.getHelpCount(help);
		List<HelpDto> list = this.helpDao.getHelpList(help, page, pageSize);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(num);
		pm.setList(list);
		return pm;
	}

	@Override
	public PageModel getHelpCategoryList(HelpCategoryDto helpCategory,
			int page, int pageSize) throws Exception {
		int num = this.helpCategoryDao.getHelpCategoryCount(helpCategory);
		List<HelpCategoryDto> list = this.helpCategoryDao.getHelpCategoryList(
				helpCategory, page, pageSize);
		PageModel pm = new PageModel();
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		pm.setTotalItem(num);
		pm.setList(list);
		return pm;
	}

	@Override
	public MessageDto getMessageById(Long id) throws Exception {
		return messageDao.getMsgById(id);
	}
	
	@Override
	public void updateMessage(MessageDto messageDto) {
		messageDao.updateMessage(messageDto);
	}

	@Override
	public PageModel getHelpOfYDSXYList(Map<String, Object> param) {
		int num = this.helpDao.getHelpOfYDSXYCount(param);
		List<HelpOf1dsxyDto> list = this.helpDao.getHelpOfYDSXYList(param);
		PageModel pageModel = new PageModel();
		pageModel.setTotalItem(num);
		pageModel.setList(list);
		return pageModel;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.home.IHomeServcie#getAllDistricits()
	 */
	@Override
	public List<DistrictDto> getAllDistricits() throws Exception {
		
		return regionDao.getAllDistricits();
	}
	
	
}
