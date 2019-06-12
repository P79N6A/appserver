package com.idcq.appserver.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

public class DateUtils {
	private static final Log logger = LogFactory.getLog(DateUtils.class);
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT1 = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT2 = "MM/dd/yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIME_FORMAT2 = "HH:mm";
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
	public static final String MILLI_TIMESTAMP_FORMAT = "yyMMddHHmmssSSS";//精确到毫秒时间戳
	public static final String ZEROTIME_FORMAT = "yyyy-MM-dd 00:00:00";	//当天零点
	public static final String ENDTIME_FORMAT = "yyyy-MM-dd 23:59:59";	//当天的最后一秒	
	
	/**
	 * 
	 * 功能描述：获取当前系统时间
	 * 
	 * @author bingzhong.qin
	 *         <p>
	 *         创建日期 ：2014-1-2 下午2:35:54
	 *         </p>
	 * 
	 * @param parse
	 *            时间格式
	 * @return
	 * @throws ParseException
	 * 
	 *             <p>
	 *             修改历史 ：(修改人，修改时间，修改原因/内容)
	 *             </p>
	 */
	public static Date getCurrentDate(String parse) throws ParseException {

		if (StringUtils.isBlank(parse)) {
			parse = DATETIME_FORMAT;
		}
		SimpleDateFormat df = new SimpleDateFormat(parse);
		return parse(df.format(new Date()), parse);
	}

	/**
	 * 
	 * 功能描述：string 转换成date，默认模式
	 * 
	 * @author bingzhong.qin
	 *         <p>
	 *         创建日期 ：2013-12-31 下午2:15:31
	 *         </p>
	 * 
	 * @param source
	 * @return
	 * 
	 *         <p>
	 *         修改历史 ：(修改人，修改时间，修改原因/内容)
	 *         </p>
	 */
	public static Date parse(String source) {
		try {
			SimpleDateFormat format = new SimpleDateFormat();
			if (source.indexOf(":") > 0)
				format.applyPattern(DATETIME_FORMAT);
			else {
				format.applyPattern(DATE_FORMAT);
			}
			return format.parse(source.trim());
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * 功能描述：string 转换成date,自定义格式
	 * 
	 * @author bingzhong.qin
	 *         <p>
	 *         创建日期 ：2013-12-31 下午2:14:30
	 *         </p>
	 * 
	 * @param source
	 * @param formatString
	 * @return
	 * 
	 *         <p>
	 *         修改历史 ：(修改人，修改时间，修改原因/内容)
	 *         </p>
	 */
	public static Date parse(String source, String formatString) {
	    if (StringUtils.isNotBlank(source)) {
	        try {
	            SimpleDateFormat format = new SimpleDateFormat();
	            format.applyPattern(formatString.trim());
	            return format.parse(source.trim());
	        } catch (ParseException e) {
	            logger.error(e);
	            e.printStackTrace();
	        }
	    }
		return null;
	}
	
	/**
	 * 
	 * 功能描述：date转换成string
	 * 
	 * @author bingzhong.qin
	 *         <p>
	 *         创建日期 ：2013-12-31 下午2:11:42
	 *         </p>
	 * 
	 * @param date
	 * @param FormatString
	 * @return
	 * 
	 *         <p>
	 *         修改历史 ：(修改人，修改时间，修改原因/内容)
	 *         </p>
	 */
	public static String format(Date date, String FormatString) {
		SimpleDateFormat format = null;
		if(date == null)
			return null;
		if (StringUtils.isBlank(FormatString)) {
			FormatString = DATETIME_FORMAT;
		}
		format = new SimpleDateFormat(FormatString);
		return format.format(date);
	}

	/***************************************************************************
	 * dateStrToLong
	 * 
	 * @see 2005-11-15
	 * @param dateStr
	 *            日期字符串yyyy-mm-dd
	 * @return long theDay
	 **************************************************************************/
	public static long dateStrToLong(String dateStr) {
		long theDate = System.currentTimeMillis();
		Date thisDate = stringToDate(dateStr);
		if (thisDate != null) {
			theDate = thisDate.getTime();
		}
		return theDate;
	}
	
  
	public static String longStrToDate(long date, String FormatString) {
	    SimpleDateFormat format = null;
        if(date == 0)
            return null;
        if (StringUtils.isBlank(FormatString)) {
            FormatString = DATETIME_FORMAT;
        }
        format = new SimpleDateFormat(FormatString);
        return format.format(date);
    
	}

	/***************************************************************************
	 * stringToDate 把字符型"yyyy-MM-dd"转换成日期型
	 * 
	 * @param s
	 *            String 需要转换的日期时间字符串
	 * @return theDate Date
	 **************************************************************************/
	public static Date stringToDate(String s) {
		Date theDate = null;
		try {
			if (s != null) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
				theDate = dateFormatter.parse(s);
			} else {
				theDate = null;
			}
		} catch (ParseException pe) {
			// plogger.error(e); e.printStackTrace();
			theDate = null;
		}
		return theDate;
	}
	
	
	/**
	 * 获取指定日期是周几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekStrByDate(Date date){  
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
		if(week_index<0){  
			week_index = 0;  
		}   
		return weeks[week_index];  
	}  
	
	public static int getWeekByDate(Date date){  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
		if(week_index<=0){  
			week_index = 7;  
		}   
		return week_index;  
	}  
	
	public static String currPK() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String d1 = sdf.format(d);

		try {
			Thread.sleep(50);/* 沉睡50毫秒 */
		} catch (InterruptedException e) {
			logger.error(e);
			e.printStackTrace();
		}

		return d1;

	}

	/**
	 * 获取系统当前秒级时间戳
	 * 
	 * @return
	 */
	public static long getSecondStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 传入时间跟当前时间比较
	 * 
	 * @param dateStr
	 * @return
	 */
	public static int compareDate(String dateStr) {
		DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
		Date newDate = new Date();
		Date paramDate;
		try {
			paramDate = df.parse(dateStr);
			return newDate.compareTo(paramDate);
		} catch (ParseException e) {
			logger.error(e);
			return -2;
		}
	}
	/**
	 * 验证时间格式是否合格
	 * @param timeStr 时间字符串
	 * @param timeFormart 需要验证的时间格式
	 * @return
	 */
	public static boolean isValidDate(String timeStr, String timeFormart) {
		boolean dateFlag = false;
		try {
			// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
			DateFormat dateFormat = new SimpleDateFormat(timeFormart);
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			dateFormat.setLenient(false);
			dateFormat.parse(timeStr);
			dateFlag = true;
		} catch (Exception e) {
			dateFlag = false;
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_DATAFORMAT_ERROR);
		}
		return dateFlag;
	}
	
	/**
	 * 获取当天零点
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Date getZeroTimeOfCurDate() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(ZEROTIME_FORMAT);
		String s =sdf.format(new Date());
		return sdf.parse(s);
	}
	
	public static Date getZeroTimeOfYesterDay() throws Exception{
		// 获取昨天这时候的日期时间
		Date nowOfYesterday = addSeconds(new Date(), -24*60*60);
		SimpleDateFormat sdf = new SimpleDateFormat(ZEROTIME_FORMAT);
		String s =sdf.format(nowOfYesterday);
		return sdf.parse(s);
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Date getCurTimeOfCurDate() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		String s =sdf.format(new Date());
		return sdf.parse(s);
	}
	public static Date getTime(String timeStr) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.parse(timeStr);
	}
	
	public static String getTimeStrByPattern(Date time ,String dateFormat) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(time);
	}
	
	public static Date getTimeByPattern(String time,String dateFormat) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.parse(time);
	}
	
	
	public static String getCurDate() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		String s =sdf.format(new Date());
		return s;
	}
	
	public static Date getEndTimeOfCurDate() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(ENDTIME_FORMAT);
		String s =sdf.format(new Date());
		sdf = new SimpleDateFormat(DATETIME_FORMAT);
		return sdf.parse(s);
	}
	
	/**
	 * 获取当前日期为周几
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getDayOfWeek() throws Exception{
		Calendar now = Calendar.getInstance();
		//一周第一天是否为星期天
		boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
		//获取周几
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		if(isFirstSunday){
		  weekDay = weekDay - 1;
		  if(weekDay == 0){
		    weekDay = 7;
		  }
		}
		return weekDay;
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int getDayOfWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
	
	
	
	 /**
     * Adds a number of years to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of months to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of weeks to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of days to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of hours to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of minutes to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of seconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }
    
    
    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     * @deprecated Will become privately scoped in 3.0
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
	/**
	 * 获取当前时间延后day天后的时间
	 * @param datetime
	 * @param day
	 * @return
	 */
	public static String getDateAfterDay(String datetime,int day){
		DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(df.parse(datetime));
			c.add(Calendar.DATE, day);
			Date d = c.getTime();
			return df.format(d);
		} catch (ParseException e) {
			logger.error("获取当前时间延后day天后的时间-ParseException",e);
		} catch (Exception e){
			logger.error("获取当前时间延后day天后的时间-Exception",e);
		}
		return null;
	}
	
	/**
	 * 获取当前时间延后day天后的时间
	 * @param datetime
	 * @param day
	 * @param fmt
	 * @return
	 */
	public static String getDateAfterDay(String datetime,int day,String fmt){
		if (StringUtils.isEmpty(fmt)) {
			fmt = DATETIME_FORMAT;
		}
		DateFormat df = new SimpleDateFormat(fmt);
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(df.parse(datetime));
			c.add(Calendar.DATE, day);
			Date d = c.getTime();
			return df.format(d);
		} catch (ParseException e) {
			logger.error("获取当前时间延后day天后的时间-ParseException",e);
		} catch (Exception e){
			logger.error("获取当前时间延后day天后的时间-Exception",e);
		}
		return null;
	}
	
	/**
	 * 获取当前时间延后hour小时后的时间
	 * @param datetime
	 * @param day
	 * @return
	 */
	public static String getDateAfterHour(String datetime,int hour){
		DateFormat df = new SimpleDateFormat(TIME_FORMAT);
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(df.parse(datetime));
			c.add(Calendar.HOUR, hour);
			Date d = c.getTime();
			return df.format(d);
		} catch (ParseException e) {
			logger.error("获取当前时间延后hour小时的时间-ParseException",e);
		} catch (Exception e){
			logger.error("获取当前时间延后hour小时的时间-Exception",e);
		}
		return null;
	}
	/**
	 * 将日期字符串替换成新的格式
	 * @param dateTime
	 * @param fmtStr
	 * @return
	 */
	public static String convertFmtDateTimeStr(String dateTime,String oldFmtStr,String newFmtStr){
		if (StringUtils.isEmpty(oldFmtStr)) {
			oldFmtStr = DATETIME_FORMAT;
		}
		if (StringUtils.isEmpty(newFmtStr)) {
			newFmtStr = DATETIME_FORMAT1;
		}
		try {
			DateFormat df = new SimpleDateFormat(oldFmtStr);
			dateTime = new SimpleDateFormat(newFmtStr).format(df.parse(dateTime));
		} catch (ParseException e) {
			logger.error("日期转换异常",e);
		} catch (Exception e) {
			logger.error("日期转换系统异常",e);
		}
		return dateTime;
	}
	
	/**
	 * 获取日期
	 * @param date
	 * @return
	 */
	public static String getDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.format(date);
	}
	
	public static Date getDateOfDateTime(Date date) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		String dateStr =  format.format(date);
		return format.parse(dateStr);
	}
	
	public static Date getTimeOfDateTime(Date date) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2);
		String dateStr =  format.format(date);
		return format.parse(dateStr);
	}
	
	public static String getTime(Date date){
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		return format.format(date);
	}
	
	/**
	 * 计算传入日期时间跟当前日期时间比较
	 * 如果传入日期>当前日期=false
	 * @param dtStr
	 * @return
	 */
	public static boolean validDateTime(String dtStr){
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
		try {
			if(!StringUtils.isBlank(dtStr)){
				long reqDateTime = format.parse(dtStr).getTime(); 
				long nowDateTime = new Date().getTime();
				if((reqDateTime - nowDateTime) <= 0){
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("比较两个日期时间异常",e);
			return false;
		}
		return true;
	}
	
	
//	public static void main(String[] args) {
//		Calendar now = Calendar.getInstance();
//		//一周第一天是否为星期天
//		boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
//		//获取周几
//		int weekDay = now.get(Calendar.DAY_OF_WEEK);
//		System.out.println(weekDay);
//		//若一周第一天为星期天，则-1
//		if(isFirstSunday){
//		  weekDay = weekDay - 1;
//		  if(weekDay == 0){
//		    weekDay = 7;
//		  }
//		}
//		System.out.println(weekDay);
//		System.out.println(Calendar.THURSDAY);
//		System.out.println(Calendar.SATURDAY);
//	}
	/**
	 * 验证时间格式是否合格
	 * @param timeStr 时间字符串
	 * @param timeFormart 需要验证的时间格式
	 * @return
	 */
	public static Date validDateStr(String timeStr, String timeFormart) {
		Date date = new Date();
		try {
			// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
			DateFormat dateFormat = new SimpleDateFormat(timeFormart);
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			dateFormat.setLenient(false);
			date = dateFormat.parse(timeStr);
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_DATAFORMAT_ERROR);
		}
		return date;
	}
	
	
	/**
	 * 
	 * 计算开始时间与结束时间的时间间隔的绝对值，单位为分
	 * @Function: com.idcq.idianmgr.quartz.HandleOrderStatusJob.computeIntervalTime
	 * @Description:
	 *
	 * @param reserveTime
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月1日 下午2:29:00
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    shengzhipeng       v1.0.0         create
	 */
	public static int computeIntervalTime(Date startTime, Date endTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		long start = calendar.getTimeInMillis();
		calendar.setTime(endTime);
		long end = calendar.getTimeInMillis();
		return (int)  Math.abs((end - start) / 60000);
	}
	
	 /**
     * 某月第一天
     * @return
     */
	public static String getFirstDayInMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }
    
    /**
     * 某月最后一天
     * @return
     */
	public static String getLastDayInMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
        return str.toString();

    }
	
	/**
	 * 比较两个日期大小
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static int compareDate(Date arg1,Date arg2){
		return arg1.compareTo(arg2);
	}
	
	public static void main(String[] args) {
		Date data= null;
		String date =DateUtils.format(data==null?new Date():data, DateUtils.DATETIME_FORMAT);
		System.out.println(date);
	}
	
}