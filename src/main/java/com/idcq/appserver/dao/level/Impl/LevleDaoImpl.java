package com.idcq.appserver.dao.level.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.level.ILevelDao;
import com.idcq.appserver.dto.level.LevelDto;
import com.idcq.appserver.dto.level.LevelValidDto;
import com.idcq.appserver.dto.level.PointDetailDto;
import com.idcq.appserver.dto.level.PointRuleDto;
import com.idcq.appserver.dto.level.PointRuleValidDto;
import com.idcq.appserver.dto.level.PrerogativeDto;
import com.idcq.appserver.dto.level.PrerogativeValidDto;
@Repository
public class LevleDaoImpl extends BaseDao<LevelDto> implements ILevelDao {

	@Override
	public LevelDto getLevelDetail(String levelId, String shopId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", levelId);
		params.put("shopId", shopId);
		params.put("isDelete", CommonConst.IS_DELETE_FALSE);
		return (LevelDto)super.selectOne(generateStatement("getLevelDetail"), params);
	}

	@Override
	public List<PrerogativeDto> getPrerogativeList(Map<String, Object> map) {
		return (List)super.findList(generateStatement("getPrerogativeList"),map);
	}

	@Override
	public PrerogativeDto getPrerogativeDetail(Map<String, Object> map) {
		return (PrerogativeDto)super.selectOne(this.generateStatement("getPrerogativeDetail"), map);
	}

	@Override
	public List<PointDetailDto> getPointDetail(PointDetailDto pointDto) {
		return (List)super.findList(generateStatement("getPointDetail"),pointDto);
	}

	@Override
	public int delLevelInfoByConditions(Map<String, Object> map) {
		return super.update(generateStatement("delLevelInfoByConditions"),map);
	}

	@Override
	public int delPrerogativeInfoByConditions(Map<String, Object> map) {
		return super.update(generateStatement("delPrerogativeInfoByConditions"),map);
	}

	@Override
	public List<LevelDto> getLevelList(Map<String, Object> map) {
		return (List)super.findList(generateStatement("getLevelDetail"),map);
	}

	@Override
	public int getPrerogativeListCount(Map<String, Object> map) {
		return (Integer)selectOne(generateStatement("getPrerogativeListCount"),map);
	}

	@Override
	public int getLevelListCount(Map<String, Object> map) {
		return (Integer)selectOne(generateStatement("getLevelDetailCount"),map);
	}

	@Override
	public int getPointDetailCount(PointDetailDto pointDetailDto) {
		return (Integer)selectOne(generateStatement("getPointDetailCount"),pointDetailDto);
	}

	@Override
	public List<PointRuleDto> getPointRuleList(PointRuleDto pointRuleDto) {
		return (List)super.findList(generateStatement("getPointRuleList"),pointRuleDto);
	}

	@Override
	public int getPointRuleListCount(PointRuleDto pointRuleDto) {
		return (Integer)selectOne(generateStatement("getPointRuleListCount"),pointRuleDto);
	}
	
	@Override
	public int insertPointDetail(PointDetailDto pointDetailDto) {
		return super.insert(generateStatement("insertPointDetail"), pointDetailDto);
	}
	@Override
	public int delLevelPrerogativeInfo(Map<String, Object> map) {
		return super.update(generateStatement("delLevelPrerogativeInfo"),map);
	}

	@Override
	public void addLevelPrerogativeInfo(Map<String, Object> map) {
		this.insert(generateStatement("addLevelPrerogativeInfo"), map);
	}

	@Override
	public int updateLevelInfo(LevelValidDto memberLevelDto) {
		return super.update(generateStatement("updateLevelInfo"),memberLevelDto);
	}

	@Override
	public int addLevelInfo(LevelValidDto memberLevelDto) {
		this.insert(generateStatement("addLevelInfo"), memberLevelDto);
		int levelId = memberLevelDto.getLevelId();
		return levelId;
	}

	@Override
	public int updatePrerogativeInfo(PrerogativeValidDto prerogativeDto) {
		return super.update(generateStatement("updatePrerogativeInfo"),prerogativeDto);
//		return 0;
	}

	@Override
	public int addPrerogativeInfo(PrerogativeValidDto prerogativeDto) {
		this.insert(generateStatement("addPrerogativeInfo"), prerogativeDto);
		int prerogativeId = prerogativeDto.getPrerogativeId();
		return prerogativeId;
	}

	@Override
	public int updatePointRule(PointRuleValidDto pointRuleDto) {
		return super.update(generateStatement("updatePointRule"),pointRuleDto);
	}

	@Override
	public int addPointRule(PointRuleValidDto pointRuleDto) {
		this.insert(generateStatement("addPointRule"), pointRuleDto);
		int pointRuleId = pointRuleDto.getPointRuleId();
		return pointRuleId;
				
	}

	@Override
	public List<PrerogativeDto> getPrerogativeByMap(Map<String, Object> map) {
		return (List)super.findList(generateStatement("getPrerogativeByMap"),map);
	}

	@Override
	public List<Integer> getLevelIdByPrerogativeId(Map<String, Object> map) {
		return (List)super.findList(generateStatement("getLevelIdByPrerogativeId"),map);
		
	}

	@Override
	public int getPointDetailCount(Map<String, Object> map) {
		return (Integer)selectOne(generateStatement("getPointDetailCountByMap"),map);
	}

	@Override
	public List<PointDetailDto> getPointDetail(Map<String, Object> requestMap) {
		return (List)super.findList(generateStatement("getPointDetailByMap"),requestMap);
	}
	
	@Override
	public int getPointDetailValueSum(PointDetailDto pointDetailDto) {
		return (int)selectOne(generateStatement("getPointDetailValueSum"),pointDetailDto);
	}
	

}
