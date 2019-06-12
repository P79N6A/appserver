package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.Page;

/**
 * 
 * @author zc
 *
 */
@Repository
public class ConfigDaoImpl extends SqlSessionDaoSupport implements IConfigDao
{
    private static final Logger log = LoggerFactory.getLogger(ConfigDaoImpl.class);
    
    
    private String generateNamespace(String tag){
    	return "com.idcq.appserver.dao.common.ConfigDaoImpl." + tag;
    }
    
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }
    
    @Override
    public List<ConfigDto> queryForConfig(ConfigQueryCondition configQueryCondition, Page pageModel)
    {
        if (null == pageModel)
        {
            pageModel = Page.getDefaulPage();
        }
        RowBounds rowBounds = new RowBounds(pageModel.getStartRows(), pageModel.getpSize());
        return super.getSqlSession().selectList(this.generateNamespace("queryForConfig"), configQueryCondition, rowBounds);
    }

    @Override
    public int countForConfig(ConfigQueryCondition configQueryCondition)
    {
        return super.getSqlSession().selectOne(this.generateNamespace("countForConfig"), configQueryCondition);
    }

    @Override
    public void batchUpdate(List<ConfigDto> items)
    {   
        for (ConfigDto configDto : items)
        {
            this.updateShopConfig(configDto);
        }
    }

    @Override
    public void batchInsert(List<ConfigDto> items)
    {   
        super.getSqlSession().insert(this.generateNamespace("batchInsert"), items);
        log.debug("insert " + items.size() + "shop configs...");
    }
    
    @Override
    public void updateShopConfig(ConfigDto configDto){
        this.getSqlSession().update(this.generateNamespace("updateShopConfig"), configDto);
        log.debug("update shop config:" + configDto.getBizId() + "-" + configDto.getConfigKey() + "-" + configDto.getConfigValue());
    }

    @Override
    public List<ConfigDto> queryForAllConfig(ConfigDto configDto)
    {
        return super.getSqlSession().selectList(this.generateNamespace("queryForAllConfig"), configDto);
    }

    @Override
    public void deleteConfig(ConfigDto configDto)
    {
        super.getSqlSession().delete(this.generateNamespace("deleteConfig"), configDto);
        
    }
}
