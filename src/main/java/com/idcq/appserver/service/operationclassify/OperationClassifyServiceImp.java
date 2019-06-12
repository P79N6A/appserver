package com.idcq.appserver.service.operationclassify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.CommonResultConst;
import com.idcq.appserver.dao.operationclassify.IOperationClassifyDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.operationclassify.OperationClassifyDto;
import com.idcq.appserver.utils.DataConvertUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
/**
 * 运营分类相关操作service
 * @ClassName: OperationClassifyService 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午1:44:28 
 *
 */
@Service
public class OperationClassifyServiceImp implements IOperationClassifyService{
	@Autowired
	private IOperationClassifyDao operationClassifyDao;
	
	/**
	 * 获取运营分类
	 * @Title: getOperationClassify 
	 * @param @param cityId
	 * @param @param parentClassifyId
	 * @param @param pNo
	 * @param @param pSize
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getOperationClassify(String cityId,
			String parentClassifyId, String pNo, String pSize, int order) throws Exception {
		String key=CommonConst.KEY_CITY_OPERATION_CLASSES+"_"+cityId+"_"+parentClassifyId+"_"+pNo+"_"+pSize+"_" + order;
//		PageModel pageModel=null;
		PageModel pageModel=(PageModel)DataCacheApi.getObject(key);
		if(pageModel!=null)
		{
			return pageModel;
		}
		Map<Integer,OperationClassifyDto>dtoMap=new HashMap<Integer,OperationClassifyDto>();
		Integer count=operationClassifyDao.getOperationClassifyCountByCondition(cityId, parentClassifyId);//查找该城市该父级分类下的子分类条数
		if(count!=null&&count.intValue()==0)
		{
			pageModel= operationClassifyDao.getOperationClassify(null, pNo, pSize, parentClassifyId, order);
			queryChildCount(pageModel,dtoMap);
			DataCacheApi.setObjectEx(key, pageModel, 21600);
			return pageModel;
		}
		pageModel= operationClassifyDao.getOperationClassify(cityId, pNo, pSize, parentClassifyId, order);
		queryChildCount(pageModel,dtoMap);
		DataCacheApi.setObjectEx(key, pageModel, 21600);
		pageModel.setTotalItem(count);
		return pageModel;
	}
	
	
	/**
	 * 获取id列表
	 * @Title: getIdFromDtoList 
	 * @param @param dtoList
	 * @param @return
	 * @return List<Long>    返回类型 
	 * @throws
	 */
	private List<Integer>getIdFromDtoList(List<OperationClassifyDto>dtoList,Map<Integer,OperationClassifyDto>dtoMap)
	{
		List<Integer>idList=new ArrayList<Integer>();
		for(OperationClassifyDto dto:dtoList)
		{
			idList.add(dto.getClassifyId());
			dtoMap.put(dto.getClassifyId(), dto);
		}
		return idList;	
	}
	
	/**
	 * 查询运营分类的下级个数
	 * @Title: queryChildCount 
	 * @param @param pageModel
	 * @param @param dtoMap
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	private void queryChildCount(PageModel pageModel,Map<Integer,OperationClassifyDto>dtoMap)throws Exception
	{
		List<OperationClassifyDto>dtoList=pageModel.getList();
		if(dtoList!=null&&dtoList.size()>0)
		{
			List<Integer>idList=getIdFromDtoList(dtoList,dtoMap);
			List<OperationClassifyDto>childCountList=operationClassifyDao.queryChildCountByParentList(idList);
			parseResult(childCountList, dtoMap);
			pageModel.setList(DataConvertUtil.convertCollectionToListMap(dtoList, CommonResultConst.GET_OPERATION_CLASSIFY));
		}
	}
	
	/**
	 * 解析查询结果
	 * @Title: parseResult 
	 * @param @param childCountList
	 * @param @param dtoMap
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	private void parseResult(List<OperationClassifyDto>childCountList,Map<Integer,OperationClassifyDto>dtoMap)throws Exception
	{
		if(childCountList.size()>0)
		{
			for(OperationClassifyDto operationClassifyDto:childCountList)
			{
				OperationClassifyDto dto=dtoMap.get(operationClassifyDto.getClassifyId());
				if(dto!=null)
				{
					if(operationClassifyDto.getChildCount()!=null&&operationClassifyDto.getChildCount()>0)
					{
						dto.setHasChildren(1);
					}
					else
					{
						dto.setHasChildren(0);
					}
				}
			}
		}
	}


    @Override
    public List<OperationClassifyDto> queryOperationClassifyDto(OperationClassifyDto operationClassifyDto,
            Integer pSize, Integer pNo)
    {
        return operationClassifyDao.queryOperationClassify(operationClassifyDto, pSize, pNo);
    }
	

}	

