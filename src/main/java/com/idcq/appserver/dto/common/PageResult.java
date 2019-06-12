package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.List;
/**
 * 分页返回结果
 * @author Administrator
 *
 * @param <T>
 */
public class PageResult<T> implements Serializable
{
    private static final long serialVersionUID = 5801822626691879906L;
    
    private Integer pNo;
    private Integer rCount;
    private List<T> lst;
    public Integer getpNo()
    {
        return pNo;
    }
    public void setpNo(Integer pNo)
    {
        this.pNo = pNo;
    }
    public Integer getrCount()
    {
        return rCount;
    }
    public void setrCount(Integer rCount)
    {
        this.rCount = rCount;
    }
    public List<T> getLst()
    {
        return lst;
    }
    public void setLst(List<T> lst)
    {
        this.lst = lst;
    }
    
    /**
     * for test
     * @throws Exception 
     */
/*    public static void main(String[] args) throws Exception 
    {
        PageResult<ConfigDto> dtos = new  PageResult<ConfigDto>();
        dtos.setpNo(10);
        dtos.setrCount(100);
        List<ConfigDto> temps = new ArrayList<ConfigDto>();
        ConfigDto dto = new ConfigDto();
        dto.setBizType(1);
        dto.setConfigGroup("kdfkd");
        temps.add(dto);
        temps.add(dto);
        temps.add(dto);
        dtos.setLst(temps);
        System.out.println(ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "查询系统配置项成功", dtos));
    }*/
    
}
