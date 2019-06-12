package com.idcq.appserver.dao.operationclassify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.operationclassify.OperationClassifyDto;
/**
 * 运营分类
 * @ClassName: OperationClassifyDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午1:49:35 
 *
 */
@Repository
public class OperationClassifyDaoImp extends BaseDao<OperationClassifyDto>  implements IOperationClassifyDao{
	
	/**
	 * 获取下级运营分类
	 * @Title: getOperationClassify 
	 * @param @param cityId
	 * @param @param pNo
	 * @param @param pSize
	 * @param @param parentClassifyId
	 * @param @param order 1-升序，2-降序
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public PageModel getOperationClassify(String cityId, String pNo,
			String pSize, String parentClassifyId, int order) throws Exception {
		if(StringUtils.isEmpty(pNo))
		{
			pNo="1";
		}
		if(StringUtils.isEmpty(pSize))
		{
			pSize="10";
		}
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("cityId", cityId);
		params.put("start", (Integer.parseInt(pNo)-1)*10);
		params.put("limit", Integer.parseInt(pSize));
		params.put("parentClassifyId", parentClassifyId);
		params.put("order", order);
		return super.findPagedList(generateStatement("getOperationClassify"), generateStatement("getOperationClassifyCount"), params);
	}
	
	/**
	 * 获得运营分类的条数
	 * @Title: getOperationClassifyCount 
	 * @param @param cityId
	 * @param @param parentClassifyId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public Integer getOperationClassifyCountByCondition(String cityId,
			String parentClassifyId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("cityId", cityId);
		params.put("parentClassifyId", parentClassifyId);
		return (Integer)super.selectOne(generateStatement("getOperationClassifyCountByCondition"), params);
	}

	@Override
	public List<OperationClassifyDto> queryChildCountByParentList(
			List<Integer> idList) throws Exception {
		return super.findList(generateStatement("queryChildCountByParentList"),idList);
	}

    @Override
    public List<OperationClassifyDto> queryOperationClassify(OperationClassifyDto operationClassifyDto, Integer pSize, Integer pNo)
    {   
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("classifyId", operationClassifyDto.getClassifyId());
        searchMap.put("start", (pNo-1)*pSize);
        searchMap.put("limit", pSize);
        return super.findList(generateStatement("getOperationClassifyNom"),searchMap);
    }

}
