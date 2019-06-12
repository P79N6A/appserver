package com.idcq.appserver.dto.common;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Page
{
    private static final Logger log = LoggerFactory.getLogger(Page.class);

    public static final Integer FIRST_PAGE = 1;

    public static final Integer BASE_PAGE_SIZE = 20;

    private Integer pNo = FIRST_PAGE;

    private Integer pSize = BASE_PAGE_SIZE;

    private Integer startRows = 0;

    private Integer totalNums = null;

    private LinkedList<String> orderBy = null;

    private LinkedList<String> direction = null;

    private Page()
    {

    }

    public Page(Integer pNo, Integer pSize, Integer totalNums)
    {
        if (totalNums != null && pSize != null)
        {
            this.pNo = pNo;
            this.pSize = pSize;
        }
        if (totalNums != null)
        {
            this.totalNums = totalNums;
        }
    }

    public Page(Integer pNo, Integer pSize)
    {
        if (null != pNo)
        {
            this.pNo = pNo;
        }
        if (null != pSize)
        {
            this.pSize = pSize;
        }
    }

    public Page(String pNo, String pSize)
    {
        this(getPageNo(pNo), getPageSize(pSize));
    }

    public static Page getDefaulPage()
    {
        return new Page();
    }

    public static Integer getPageSize(String pSize)
    {
        try
        {
            return Integer.valueOf(pSize.trim());
        }
        catch (Exception e)
        {
            log.debug("参数无法解析使用默认分页大小", e);
            return BASE_PAGE_SIZE;
        }
    }

    public static Integer getPageNo(String pNo)
    {
        try
        {
            return Integer.valueOf(pNo.trim());
        }
        catch (Exception e)
        {
            log.debug("参数无法解析使用默认分页大小", e);
            return FIRST_PAGE;
        }
    }

    public Integer getpNo()
    {
        return pNo;
    }

    public void setpNo(Integer pNo)
    {
        this.pNo = pNo;
    }

    public Integer getpSize()
    {
        return pSize;
    }

    public void setpSize(Integer pSize)
    {
        this.pSize = pSize;
    }

    public Integer getStartRows()
    {
        if (pNo > 0)
        {
            startRows = (pNo - 1) * this.pSize;
        }
        return startRows;
    }

    public void setStartRows(Integer startRows)
    {
        this.startRows = startRows;
    }

    public Integer getTotalNums()
    {
        return totalNums;
    }

    public void setTotalNums(Integer totalNums)
    {
        this.totalNums = totalNums;
    }

    public LinkedList<String> getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(LinkedList<String> orderBy)
    {
        this.orderBy = orderBy;
    }

    public LinkedList<String> getDirection()
    {
        return direction;
    }

    public void setDirection(LinkedList<String> direction)
    {
        this.direction = direction;
    }

}
