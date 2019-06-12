package com.idcq.appserver.dao.msgPush;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dto.message.AppPushRecordDto;

@Repository
public class PushRecordDaoImpl extends SqlSessionDaoSupport implements PushRecordDao {
    
//  @Autowired
//  protected GtaJdbcBaseDao jdbcBaseDao;
    private static final String mark = "com.idcq.appserver.dto.message.pushRecord.";
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
        
    }
    @Override
    public void saveRecord(AppPushRecordDto appPushRecordDto)
    {
        this.getSqlSession().insert(mark + "saveRecord", appPushRecordDto);
        
    }
    @Override
    public void updateRecordById(AppPushRecordDto appPushRecordDto)
    {
        this.getSqlSession().insert(mark + "updateRecordById", appPushRecordDto);
    }

    
    

}
