package com.idcq.appserver.service.level;

import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.level.AddPointDetailModel;
import com.idcq.appserver.dto.level.LevelValidDto;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.level.PointRuleValidDto;
import com.idcq.appserver.dto.level.PrerogativeDto;
import com.idcq.appserver.dto.level.PrerogativeValidDto;

public interface ILevelService {

	Map<String,Object> getLevelDetail(String levelId, String shopId) throws Exception;

	PageModel getLevelList(Map<String, Object> map) throws Exception;

	PageModel getPrerogativeList(Map<String, Object> map) throws Exception;

	PrerogativeDto getPrerogativeDetail(Map<String, Object> map) throws Exception;

	PageModel getPointDetail(PointDetailDto pointDto) throws Exception;

	void operateLevelInfo(String shopId, String levelId, String operateType) throws Exception;

	void operatePrerogativeInfo(String shopId, String prerogativeId,String operateType) throws Exception;

	PageModel getPointRuleList(PointRuleDto pointRuleDto);

	int operateLevelPrerogativeInfo(String shopId, String prerogativeIds,
			String levelId, String operateType) throws Exception;

	int updateLevelInfo(LevelValidDto memberLevelDto) throws Exception;
	/**
	 * 增加积分
	 * 
	 * @Function: com.idcq.appserver.service.level.ILevelService.addPoint
	 * @Description:
	 *
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年5月25日 上午11:02:57
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年5月25日    ChenYongxin      v1.0.0         create
	 */
	void addPoint(AddPointDetailModel addPointDetailModel,Long operaterId,String operaterName) throws Exception;

	int updatePrerogativeInfo(PrerogativeValidDto prerogativeDto) throws Exception;

	int updatePointRule(PointRuleValidDto pointRuleDto) throws Exception;
	
	int updateShopByPoint(Long shopId,Integer levelId,Integer afterPoint) throws Exception;
	
	void insertPointDetail(Integer bizId,Integer bizType,Integer pointValue,Integer afterPoint,
				String pointDetailTitle,Integer pointRuleId,Integer pointSourceType,String pointSourceId,
				Long operaterId,String operaterName,String remark) throws Exception;
	
	void pushAddPointMessage(Integer ruleType,Integer subRuleType, 
			Integer pointTargetType,Integer pointTargetId,
			Integer pointSourceType,String pointSourceId,Boolean asyn);
	
	int getPointDetailCount(PointDetailDto pointDto);

	PageModel getPointDetailByBizIds(PointDetailDto pointDetailDto) throws Exception;
	int getPointDetailValueSum(PointDetailDto pointDetailDto);
}
