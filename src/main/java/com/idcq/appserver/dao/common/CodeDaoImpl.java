package com.idcq.appserver.dao.common;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.CodeDto;

@Repository
public class CodeDaoImpl extends BaseDao<CodeDto> implements ICodeDao {

    @Override
    public List<CodeDto> getCodeByType(String codeType) throws Exception
    {
        return (List<CodeDto>)super.findList("getCodeByType", codeType);
    }

}
