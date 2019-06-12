package com.idcq.appserver.dao.level;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.level.LevelDto;
import com.idcq.appserver.dto.level.LevelValidDto;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.level.PointRuleValidDto;
import com.idcq.appserver.dto.level.PrerogativeDto;
import com.idcq.appserver.dto.level.PrerogativeValidDto;

public interface ILevelDao {

	LevelDto getLevelDetail(String levelId, String shopId);

	List<PrerogativeDto> getPrerogativeList(Map<String, Object> map);

	PrerogativeDto getPrerogativeDetail(Map<String, Object> map);

	List<PointDetailDto> getPointDetail(PointDetailDto pointDto);

	int delLevelInfoByConditions(Map<String, Object> map);

	int delPrerogativeInfoByConditions(Map<String, Object> map);

	List<LevelDto> getLevelList(Map<String, Object> map);

	int getPrerogativeListCount(Map<String, Object> map);

	int getLevelListCount(Map<String, Object> map);

	int getPointDetailCount(PointDetailDto pointDto);

	List<PointRuleDto> getPointRuleList(PointRuleDto pointRuleDto);

	int getPointRuleListCount(PointRuleDto pointRuleDto);
	
	int insertPointDetail(PointDetailDto pointDetailDto);

	int delLevelPrerogativeInfo(Map<String, Object> map);

	void addLevelPrerogativeInfo(Map<String, Object> map);

	int updateLevelInfo(LevelValidDto memberLevelDto);

	int addLevelInfo(LevelValidDto memberLevelDto);

	int updatePrerogativeInfo(PrerogativeValidDto prerogativeDto);

	int addPrerogativeInfo(PrerogativeValidDto prerogativeDto);

	int updatePointRule(PointRuleValidDto pointRuleDto);

	int addPointRule(PointRuleValidDto pointRuleDto);

	List<PrerogativeDto> getPrerogativeByMap(Map<String, Object> map);

	List<Integer> getLevelIdByPrerogativeId(Map<String, Object> map);

	int getPointDetailCount(Map<String, Object> map);

	List<PointDetailDto> getPointDetail(Map<String, Object> requestMap);
	
	int getPointDetailValueSum(PointDetailDto pointDetailDto);

}
