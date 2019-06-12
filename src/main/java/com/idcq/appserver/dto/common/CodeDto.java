/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.common.CodeDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年1月12日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.common;

import java.io.Serializable;

/**
 * 编码实体类
 * 
 * @author ChenYongxin
 *
 */
public class CodeDto implements Serializable
{

    private static final long serialVersionUID = -4028053865201109136L;
/*    `code_type` varchar(32) NOT NULL COMMENT '代码类型',
    `code` varchar(8) NOT NULL COMMENT '代码',
    `code_name` varchar(64) DEFAULT NULL COMMENT '代码名称',
    `code_index` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
    `code_desc` varchar(512) DEFAULT NULL COMMENT '备注',*/
    private String codeType;
    private String code;
    private String codeName;
    private String codeIndex;
    private String codeDesc;
    public String getCodeType()
    {
        return codeType;
    }
    public void setCodeType(String codeType)
    {
        this.codeType = codeType;
    }
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getCodeName()
    {
        return codeName;
    }
    public void setCodeName(String codeName)
    {
        this.codeName = codeName;
    }
    public String getCodeIndex()
    {
        return codeIndex;
    }
    public void setCodeIndex(String codeIndex)
    {
        this.codeIndex = codeIndex;
    }
    public String getCodeDesc()
    {
        return codeDesc;
    }
    public void setCodeDesc(String codeDesc)
    {
        this.codeDesc = codeDesc;
    }
    

}
