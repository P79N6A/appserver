package com.idcq.appserver.dao.common;

import java.util.List;

import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.Page;

/**
 * 有关表1dcq_config的操作接口
 * @author zc
 *
 */
public interface IConfigDao
{
    /**
     * 根据传入条件查询配置，传入的查询条件皆以and的条件参与查询，这个方法不会对参数进行任何校验, 支持分页查询,但不支持排序等功能
     * @param configDto
     * @param pageModel 分页参数，若为null 则为默认分页查询{@link Page.getDefaultPage()}
     * @return
     */
    List<ConfigDto> queryForConfig(ConfigQueryCondition configQueryCondition, Page pageModel);
    
    /**
     * 查询出符合条件所有{@link ConfigDto}
     * @param configDto
     * @return
     */
    List<ConfigDto> queryForAllConfig(ConfigDto configDto);
    
    /**
     * 查询指定条件的{@link ConfigDto}数量
     * @param configDto 传入的查询条件皆以and的条件参与查询，这个方法不会对参数进行任何校验
     * @return
     */
    int countForConfig(ConfigQueryCondition configQueryCondition);
    
    void batchUpdate(List<ConfigDto> items);
    
    void batchInsert(List<ConfigDto> items);
    
    void updateShopConfig(ConfigDto configDto);
    
    void deleteConfig(ConfigDto configDto);
}
