package com.idcq.appserver.utils.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;

public class ParseShopMemberSource
{

    private static final Logger log = LoggerFactory.getLogger(ParseShopMemberSource.class);

    /**
     * 将json形式的导入源解析成 List<ShopMemberDto> 形式的会员信息列表, 注意这里并不会校验数据
     * 
     * @param json json形式的会员数据
     * @return List<ShopMemberDto> 形式的会员信息列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List<ShopMemberDto> parseJson(String json)
    {
        try
        {
            List<ShopMemberDto> result = new ArrayList<ShopMemberDto>();
            List<Map<String, String>> memebersLst = (List<Map<String, String>>) JacksonUtil.jsonToObject(json,
                    List.class, null);
            for (Map<String, String> tempStr : memebersLst)
            {
                ShopMemberDto dto = new ShopMemberDto();
                dto.setName(tempStr.get("name"));
                String mobileStr = tempStr.get("mobile");
                if (mobileStr != null && mobileStr.trim().length() > 0)
                {
                    mobileStr = mobileStr.trim();
                    /* 处理以+86开头号码的情况 */
                    if (mobileStr.indexOf("+86") > -1)
                    {
                        mobileStr = mobileStr.substring(3);
                    }
                    else if (mobileStr.indexOf("86") == 0)
                    {
                        mobileStr = mobileStr.substring(2);
                    }
                    mobileStr = mobileStr.replaceAll(" ", "");
                    mobileStr = mobileStr.replaceAll("-", "");
                    mobileStr = mobileStr.trim();
                    dto.setMobile(Long.parseLong(mobileStr));
                }
                else
                {
                    dto.setMobile(null);
                }
                result.add(dto);
            }
            return result;
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MIS_MEMBER_SOURCE);
        }
    }

    @SuppressWarnings("deprecation")
    public static List<ShopMemberDto> parseExcel(InputStream in, Integer modelVersion)
    {
        try
        {
            List<ShopMemberDto> result = new ArrayList<ShopMemberDto>();
            Map<Integer, List<String>> parseResult = null;
            try
            {
                parseResult = ExcelParser.parseExcelXlsx(in);
            }
            catch (Exception e)
            {
                log.info(e.getMessage(), e);
            }

            // 没有数据
            if (parseResult == null || parseResult.size() <= 1)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_VALIDATE_SOURCE_FILE_ERROR);
            }
            ShopMemberDto tempDto = null;
            List<String> memberElememts = null;
            String tempStr = null;
            Integer tempInteger = null;
            Long tempLong = null;
            boolean isOldVersion = 0 == modelVersion.intValue();
            int birthColumn = isOldVersion ? 7 : 5;
            int addrColumn = isOldVersion ? 8 : 6;
            int remarkColumn = isOldVersion ? 9 : 7;
            for (int i = 1; i < parseResult.size(); i++)
            {
                memberElememts = parseResult.get(i);
                tempDto = new ShopMemberDto();
                // 设置姓名
                tempDto.setName(memberElememts.get(0));

                // 设置性别
                tempStr = memberElememts.get(1);
                if (tempStr == null)
                {
                    tempInteger = 2;
                }
                else
                {
                    try
                    {
                        tempInteger = Integer.parseInt(tempStr);
                    }
                    catch (Exception e)
                    {
                        log.info(e.getMessage(), e);
                        if (tempStr.indexOf("男") > -1)
                        {
                            tempInteger = 0;
                        }
                        else if (tempStr.indexOf("女") > -1)
                        {
                            tempInteger = 1;
                        }
                        else
                        {
                            tempInteger = 2;
                        }
                    }
                }
                tempDto.setSex(tempInteger);

                // 设置手机号
                tempStr = memberElememts.get(2);
                if (tempStr == null)
                {
                    tempLong = null;
                }
                else
                {
                    try
                    {
                        tempLong = Long.parseLong(tempStr);
                    }
                    catch (Exception e)
                    {
                        log.info(e.getMessage(), e);
                        tempLong = null;
                    }
                }
                tempDto.setMobile(tempLong);

                // 设置等级
                // ///////////////////////////////////////
                tempStr = memberElememts.get(3);
                if (tempStr == null)
                {
                    tempInteger = 0;
                }
                else
                {
                    try
                    {
                        tempInteger = Integer.parseInt(tempStr);
                    }
                    catch (Exception e)
                    {
                        log.info(e.getMessage(), e);
                        tempInteger = 0;
                    }
                }
                tempDto.setGrade(tempInteger);

                // 设置会员卡号
                tempDto.setMemberCardNo(memberElememts.get(4));

                if (isOldVersion) // 旧版本添加微信qq
                {
                    // 设置微信
                    tempDto.setWeixinId(memberElememts.get(5));

                    // 设置qq
                    tempStr = memberElememts.get(6);
                    if (tempStr == null)
                    {
                        tempLong = null;
                    }
                    else
                    {
                        try
                        {
                            tempLong = Long.parseLong(tempStr);
                        }
                        catch (Exception e)
                        {
                            log.info(e.getMessage(), e);
                            tempLong = null;
                        }
                    }
                    tempDto.setQq(tempLong);
                }

                // 设置生日
                tempStr = memberElememts.get(birthColumn);
                Date date = null;
                boolean hasYear = false;
                if (tempStr == null || tempStr.trim().length() < 1)
                { // 如果没有年份
                    date = null;
                }
                else
                { // 有年份
                    try
                    {
                        date = DateUtils.parse(tempStr);
                        if (date != null)
                        { // 年月日全
                            hasYear = true;
                        }
                    }
                    catch (Exception e)
                    {
                        date = null;
                        log.info(e.getMessage(), e);
                    }
                    if (date == null)
                    { // 没有年份
                        try
                        {
                            date = DateUtils.parse("2000-" + tempStr.trim());
                        }
                        catch (Exception e)
                        {
                            date = null;
                        }
                    }
                }
                Calendar calendar = Calendar.getInstance();
                if (date != null)
                {
                    calendar.setTime(date);
                }
                if (hasYear)
                { // 有年份
                    tempDto.setBirthdayYear(Integer.valueOf(calendar.get(Calendar.YEAR)));
                }
                if (date != null)
                { // 设置日期
                    tempDto.setBirthdayDate(
                            (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                }

                // 设置地址
                tempDto.setAddress(memberElememts.get(addrColumn));

                // 设置备注
                tempDto.setMemberDesc(memberElememts.get(remarkColumn));

                if (!isOldVersion)
                {
                    String tags = "";
                    String temp = null;
                    for (int t = 0; t < 3; t++)
                    {
                        temp = memberElememts.get(8 + t);
                        if (null != temp && temp.trim().length() > 0)
                        {
                            if (tags.length() > 0)
                            {
                                tags = tags + ":" + temp;
                            }
                            else
                            {
                                tags = temp;
                            }
                        }
                    }
                    if (tags.length() > 0)
                    {
                        tempDto.setTags(tags);
                    }
                }
                result.add(tempDto);
            }
            return result;
        }
        catch (Exception e)
        {
            log.info(e.getMessage(), e);
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MIS_MEMBER_SOURCE);
        }
    }
}
