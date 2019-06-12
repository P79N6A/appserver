package com.idcq.appserver.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 公共校验工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月19日
 * @time 上午9:17:43
 */
public class CommonValidUtil {
	private static Log logger = LogFactory.getLog(CommonValidUtil.class);

	private static final String MOBILE_REG = "1[3|4|5|7|8|9][0-9]{9}";
	
	private static final String DECIMAL_REG = "^\\d+(\\.\\d+)?$";
	
	private static final String INTEGER_REG = "^\\d+$";

	/**
	 * 验证字符串是否为空
	 * <p>
	 * 若为空，则直接抛出验证异常
	 * </p>
	 * 
	 * @param str
	 *            待验证的字符串
	 * @param errorCode
	 *            错误代码
	 * @param message
	 *            错误信息
	 * @throws Exception
	 */
	public static void validStrNull(String str, int errorCode, String message)
			throws Exception {
		if (StringUtils.isBlank(str)) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证字符串不能超过规定长度
	 * @param str
	 * @param length
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validStrLength(String str,int length, int errorCode, String message)
			throws Exception {
		try {
			if (str.length() > length) {
				throw new ValidateException(errorCode, message);
			}
		} catch (Exception e) {
			throw new ValidateException(errorCode, e.getMessage());
		}
		
	}

	/**
	 * 验证字符串是否可转换成int
	 * <p>
	 * 注意：在使用此验证之前，请先对字符串做非空校验<br>
	 * 若不能转换成int型，则抛出格式验证异常
	 * </p>
	 * 
	 * @param str
	 *            待验证字符串
	 * @param errorCode
	 *            错误代码
	 * @param message
	 *            错误信息
	 * @throws Exception
	 */
	public static void validStrLongFormat(String str, int errorCode,
			String message) throws Exception {
		try {
			Long.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
	}
	/**
	 * 验证字符串是否可转换成long
	 * <p>
	 * 注意：在使用此验证之前，请先对字符串做非空校验<br>
	 * 若不能转换成long型，则抛出格式验证异常
	 * </p>
	 * @param str
	 * @param errorCode
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static Long validStrLongFmt(String str, int errorCode,
			String message) throws Exception {
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
	}
	/**
	 * 	 * 验证字符串是否可转换成double
	 * 
	 * @Function: com.idcq.appserver.utils.CommonValidUtil.validStrLongFmt
	 * @Description:
	 *
	 * @param str
	 * @param errorCode
	 * @param message
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月6日 上午11:20:40
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月6日    ChenYongxin      v1.0.0         create
	 */
	public static Double validStrDoubleFmt(String str, int errorCode,
			String message) throws Exception {
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validStrIntFormat(String str, int errorCode,
			String message) throws Exception {
		try {
			Integer.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static int validStrIntFmt(String str, int errorCode,
			String message) throws Exception {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validStrEqual(String str1, String str2,int errorCode,
			String message) throws Exception {
		if(str1 == null || str2 == null){
			throw new ValidateException(errorCode, message);
		}
		if(!StringUtils.equals(str1, str2)){
			throw new ValidateException(errorCode, message);
		}
	}

//	public static void main(String[] args) throws Exception {
//		//validPositLong("-33333333", 32, "格式错误");
//		
//		Integer i = new Integer(1);
//		Integer i2 = new Integer(1);
//		//System.out.println(i == i2);
//		
//		String jsonStr = "{\"retime\":\"60\",\"eftime\":\"30\"}";
//		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//		System.out.println(jsonObject.get("retime"));
//	}

	/**
	 * 验证是否为正整数
	 * <p>
	 * 若是为空或0，则直接抛出验证异常
	 * </p>
	 * 
	 * @param num
	 *            待验证数字
	 * @param errorCode
	 *            错误代码
	 * @param message
	 *            错误信息
	 * @throws Exception
	 */
	public static void validPositLong(Long num, int errorCode, String message)
			throws Exception {
		if (!NumberUtil.isPositLong(num)) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static boolean validPositLong(Long num)
			throws Exception {
		if (!NumberUtil.isPositLong(num)) {
			return false;
		}
		return true;
	}
	
	public static void validPositInt(Integer num, int errorCode, String message)
			throws Exception {
		if (!NumberUtil.isPositInt(num)) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证小数位数是否满足指定的位数
	 * 
	 * @param num	待判断的小数
	 * @param digitScale	指定允许的小数位数，若为null则默认以0计算
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDecimals(Double num, Integer digitScale, int errorCode, String message)
			throws Exception {
		int digitScale2 = NumberUtil.getValidDecimals(num+"");
		if(digitScale == null){
			digitScale = 0;
		}
		if(digitScale2 > digitScale){
			throw new ValidateException(errorCode, message);
		}
		
	}
	
//	public static boolean isDecimal(String orginal){  
//        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);  
//    }  
//	
//	private static boolean isMatch(String regex, String orginal){  
//        if (orginal == null || orginal.trim().equals("")) {  
//            return false;  
//        }  
//        Pattern pattern = Pattern.compile(regex);  
//        Matcher isNum = pattern.matcher(orginal);  
//        return isNum.matches();  
//    }  
	
	/**
	 * 验证Integer是否为0
	 * <p>
	 * 大于0则抛异常
	 * </p>
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validIsZero(Long num, int errorCode, String message)
			throws Exception {
		if (num > 0) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validIsZero(Integer num, int errorCode, String message)
			throws Exception {
		if (num > 0) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证整数相等
	 * 
	 * @param num1
	 * @param num2
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validIntEqual(Integer num1, Integer num2, int errorCode,
			String message) throws Exception {
		if(num1 == null || num2 == null){
			throw new ValidateException(errorCode, message);
		}
		if (num1.intValue() != num2.intValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validLongEqual(Long num1, Long num2, int errorCode,
			String message) throws Exception {
		if(num1 == null || num2 == null){
			throw new ValidateException(errorCode, message);
		}
		if (num1.longValue() != num2.longValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证整数不相等
	 * <p>
	 * 	相等则直接抛异常
	 * </p>
	 * @param num1
	 * @param num2
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validLongNoEqual(Long num1, Long num2, int errorCode,
			String message) throws Exception {
		if (num1.longValue() == num2.longValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validIntNoEqual(Integer num1, Integer num2, int errorCode,
			String message) throws Exception {
		if (num1.intValue() == num2.intValue()) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证字符串是否为合法Long
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validPositLong(String num, int errorCode, String message)
			throws Exception {
		validNumStr(num, errorCode, message);
		Long i;
		try {
			i = Long.valueOf(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
		if (!NumberUtil.isPositLong(i)) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validPositInt(String num, int errorCode, String message)
			throws Exception {
		validNumStr(num, errorCode, message);
		Integer i;
		try {
			i = Integer.valueOf(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(errorCode, message);
		}
		if (!NumberUtil.isPositInt(i)) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证是否为纯数字字符串
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validNumStr(String num, int errorCode, String message)
			throws Exception {
		validStrNull(num, errorCode, message);
		boolean flag = NumberUtil.isNumeric(num);
		if (!flag) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * @Title: validDoubleStr
	 * @Description: TODO(验证是否为double类型)
	 * @param @param num
	 * @param @param errorCode
	 * @param @param message
	 * @param @throws Exception
	 * @return void 返回类型
	 * @throws
	 */
	public static void validDoubleStr(String num, int errorCode, String message)
			throws Exception {
		validStrNull(num, errorCode, message);
		boolean flag = NumberUtil.isDouble(num);
		if (!flag) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 比较Double大小
	 * <p>
	 * 	one小于two直接抛出异常
	 * </p>
	 * @param one
	 * @param two
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDoubleGreaterThan(Double one,Double two, int errorCode, String message)
			throws Exception {
		if (one.doubleValue() < two.doubleValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证Integer是否为空
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validLongNull(Long num, int errorCode, String message)
			throws Exception {
		if (num == null) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validIntNull(Integer num, int errorCode, String message)
			throws Exception {
		if (num == null) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证Integer不能为空
	 * <p>
	 * 	num为空，直接抛异常
	 * </p>
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validIntNoNull(Integer num, int errorCode, String message)
			throws Exception {
		if (num == null) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 比较Integer大小 
	 * <p>
	 * 	one小于two直接抛出异常
	 * </p>
	 * 
	 * @param one
	 * @param two
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validIntGreaterThan(Integer one,Integer two, int errorCode, String message)
			throws Exception {
		if (one.intValue() < two.intValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 比较Integer大小 
	 * <p>
	 * 	one小于或等于two直接抛出异常
	 * </p>
	 * @param one
	 * @param two
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validLongGreaterEqual(Long one,Long two, int errorCode, String message)
			throws Exception {
		if (one.longValue() <= two.longValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 比较Long大小
	 * 
	 * @param one
	 * @param two
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validLongGreater(Long one,Long two, int errorCode, String message)
			throws Exception {
		if (one.longValue() < two.longValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validIntGreaterEqual(Integer one,Integer two, int errorCode, String message)
			throws Exception {
		if (one.intValue() <= two.intValue()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证Double是否为空
	 * <p>
	 * 为空则直接抛出业务异常
	 * </p>
	 * 
	 * @param db
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDoubleNull(Double db, int errorCode, String message)
			throws Exception {
		if (db == null) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 判断Double是否为空或0
	 * 
	 * @param db
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDoubleNullZero(Double db, int errorCode,
			String message) throws Exception {
		if (db == null || db.doubleValue() == 0) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 判断Double为空或0
	 * 
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public static boolean validDoubleNullZero(Double db) throws Exception {
		if (db == null || db.doubleValue() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断Float为空或0
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static boolean validFloatNullZero(Float f) throws Exception {
		if (f == null || f.floatValue() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证Integer是否为0
	 * 
	 * @param num
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validIntZero(Integer num, int errorCode, String message)
			throws Exception {
		if (num.intValue() == 0) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validLongNotZero(Long num, int errorCode, String message)
			throws Exception {
		if (num.longValue() != 0) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validIntNotZero(Integer num, int errorCode, String message)
			throws Exception {
		if (num.intValue() != 0) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证集合是否为空
	 * 
	 * @param list
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validListNull(List list, int errorCode, String message)
			throws Exception {
		if (list == null || list.size() <= 0) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static boolean validListNull(List list)
			throws Exception {
		if (list == null || list.size() <= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean validArrayNull(Object[] objs)
			throws Exception {
		if (objs == null || objs.length <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证对象为null
	 * 
	 * @param obj
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validObjectNull(Object obj, int errorCode, String message)
			throws Exception {
		if (obj == null || obj.equals("")) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证日期时间格式
	 * <p>
	 * format : yyyy-MM-dd HH:mm:ss
	 * </p>
	 * 
	 * @param dateStr
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateTimeStr(String dateStr, int errorCode,
			String message) throws Exception {
		if (StringUtils.isBlank(dateStr)) {
			throw new ValidateException(errorCode, message);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_FORMAT);
		try {
			sdf.parse(dateStr);
		} catch (Exception e) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证日期时间格式
	 * @param dateStr
	 * @param errorCode
	 * @param message
	 * @param dateFmt 日期时间格式，不传默认为yyyy-MM-dd HH:mm:ss
	 * @throws Exception
	 */
	public static void validDateTimeStr(String dateStr, int errorCode,
			String message,String dateFmt) throws Exception {
		if (StringUtils.isBlank(dateFmt)) {
			dateFmt = DateUtils.DATETIME_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
		try {
			sdf.parse(dateStr);
		} catch (Exception e) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证日期时间格式
	 * <p>
	 * format : yyyy-MM-dd HH:mm:ss
	 * </p>
	 * 
	 * @param dateStr
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateTimeString(String dateStr, int errorCode,
			String message) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_FORMAT);
		try {
			sdf.parse(dateStr);
		} catch (Exception e) {
			throw new ValidateException(errorCode, message);
		}
	}
	/**
	 * 验证日期格式，yyyy-MM-dd HH:mm:ss/yyyy-MM-dd/yyyy-MM-dd HH:mm
	 * 
	 * @param timeStr
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static String validDateTimeFormat(String timeStr, int errorCode,
			String message) {
		String re = timeStr;
		if (!StringUtils.isBlank(re)) {
			try {
				new SimpleDateFormat(DateUtils.DATETIME_FORMAT).parse(timeStr);
			} catch (ParseException e) {
				try {
					new SimpleDateFormat(DateUtils.DATE_FORMAT).parse(timeStr);
				} catch (ParseException e1) {
					try {
						new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeStr);
					} catch (ParseException e2) {
						throw new ValidateException(errorCode, message);
					}
				}
			}
		}
		return re == null ? null : re.trim();
	}
	
	/**
	 * 验证时间日期格式
	 * 
	 * @param timeStr 时间日期
	 * @param pattern	时间日期正则
	 * @param errorCode
	 * @param message
	 */
	public static void validDateTimeFormat(String timeStr, String pattern ,int errorCode,
			String message) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			sdf.parse(timeStr);
		} catch (ParseException e) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证时间格式 HH:mm:ss
	 * 
	 * @param timeStr
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static String validTimeFormat(String timeStr, int errorCode,
			String message) {
		String re = timeStr;
		if (!StringUtils.isBlank(re)) {
			try {
				new SimpleDateFormat("HH:mm:ss").parse(timeStr);
			} catch (ParseException e) {
				throw new ValidateException(errorCode, message);
			}
		}
		return re == null ? null : re.trim();
	}

	/**
	 * 手机号码格式验证
	 * 
	 * @param mobile
	 * @param errorCode
	 * @param message
	 */
	public static void validMobileStr(String mobile, int errorCode,
			String message) {
		if (!Pattern.compile(MOBILE_REG).matcher(mobile).matches()) {
			throw new ValidateException(errorCode, message);
		}
	}

	public static Boolean validDecimalStr(String decimalStr) {
		return Pattern.compile(DECIMAL_REG).matcher(decimalStr).matches();
	}
	
	public static Boolean validIntegerStr(String integerStr) {
		return Pattern.compile(INTEGER_REG).matcher(integerStr).matches();
	}
	/**
	 * 验证日期格式
	 * <p>
	 * format : yyyy-MM-dd
	 * </p>
	 * 
	 * @param dateStr
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static Date validDateStr(String date, int errorCode, String message)
			throws Exception {
		if (StringUtils.isBlank(date)) {
			throw new ValidateException(errorCode, message);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证当前日期时间是否在时间范围内
	 * 
	 * @param startDate
	 *            开始时间，为null则不验证
	 * @param endDate
	 *            结束时间，为null则不验证
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validCurDateOfDateRange(Date startDate, Date endDate,
			int errorCode, String message) throws Exception {
		long curTime = System.currentTimeMillis();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
	}
	
	/**
	 * 验证日期在指定的日期区间内
	 * <P>验证失败则立即抛出异常
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateOfDateRange(Date date , Date startDate, Date endDate,
			int errorCode, String message) throws Exception {
		long curTime = date.getTime();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
	}
	
	public static boolean validDateOfDateRange(Date date , Date startDate, Date endDate) throws Exception {
		long curTime = date.getTime();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				return false;
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				return false;
			}
		}
		return true;
	}
	
	public static void validCurDateNotOfDateRange(Date startDate, Date endDate,
			int errorCode, String message) throws Exception {
		long curTime = System.currentTimeMillis();
		if (startDate != null && endDate != null) {
			if (curTime >= startDate.getTime() && curTime <= endDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
	}
	
	/**
	 * 验证指定日期是否在指定日期范围内
	 * 
	 * @param date	需验证的日期
	 * @param startDate
	 * @param endDate
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateNotInDateRange(Date date,Date startDate, Date endDate,
			int errorCode, String message) throws Exception {
		long curTime = date.getTime();
		if (curTime >= startDate.getTime() && curTime <= endDate.getTime()) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static boolean validDateEqual(Date one,Date two) throws Exception {
		if (one.getTime() != two.getTime()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 验证某个日期是否比指定日期晚几天
	 * <p>
	 * 	以one:two方式做比较
	 * </p>
	 * @param one
	 * @param two
	 * @param num long型，由延迟的天数（晚几天）以毫秒折算得到
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateGreateThan(Date one,Date two, long num,
			int errorCode, String message) throws Exception {
		long oneTime = one.getTime();
		long twoTime = two.getTime();
		long diffrent = oneTime - twoTime;
		if (diffrent < num) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	/**
	 * 验证某个日期是否比指定日期晚几天
	 * <p>
	 * 	以one:two方式做比较
	 * </p>
	 * @param one
	 * @param two
	 * @param num double型，由延迟的天数（晚几天）以毫秒折算得到
	 * @param errorCode
	 * @param message
	 * @throws Exception
	 */
	public static void validDateGreateThan(Date one,Date two, double num,
			int errorCode, String message) throws Exception {
		long oneTime = one.getTime();
		long twoTime = two.getTime();
		long diffrent = twoTime - oneTime;
		if (diffrent > num) {
			throw new ValidateException(errorCode, message);
		}
	}
	
	public static void validTimeGreateThan(Date one,Date two, long num,
			int errorCode, String message) throws Exception {
		long oneTime = one.getTime();
		long twoTime = two.getTime();
		long diffrent = twoTime - oneTime;
		if (diffrent < num) {
			throw new ValidateException(errorCode, message);
		}
	}

	/**
	 * 验证当前日期时间是在在指定范围内
	 * 
	 * @param startDate
	 *            开始时间，为null则不验证
	 * @param endDate
	 *            结束时间，为null则不验证
	 * @return
	 * @throws Exception
	 */
	public static boolean validCurDateTimeOfRange(Date startDate, Date endDate)
			throws Exception {
		long curTime = System.currentTimeMillis();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				return false;
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证当前时间是否在时间范围内
	 * 
	 * @param startDate
	 *            开始时间，为null则不验证
	 * @param endDate
	 *            结束时间，为null则不验证
	 * @return
	 * @throws Exception
	 */
	public static boolean validCurTimeOfDateRange(Date startDate, Date endDate,
			int errorCode, String message) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.TIME_FORMAT);
		String timeStr = sdf.format(new Date());
		long curTime = sdf.parse(timeStr).getTime();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				throw new ValidateException(errorCode, message);
			}
		}
		return true;
	}

	/**
	 * 验证当前时间是否在时间范围内
	 * 
	 * @param startDate
	 *            开始时间，为null则不验证
	 * @param endDate
	 *            结束时间，为null则不验证
	 * @return
	 * @throws Exception
	 */
	public static boolean validCurTimeOfDateRange(Date startDate, Date endDate)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.TIME_FORMAT);
		String timeStr = sdf.format(new Date());
		long curTime = sdf.parse(timeStr).getTime();
		if (startDate != null) {
			if (curTime < startDate.getTime()) {
				return false;
			}
		}
		if (endDate != null) {
			if (curTime > endDate.getTime()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 请求IP限制
	 * 
	 * @param requestIP
	 *            请求IP
	 * @param jedis
	 *            缓存IP信息
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	public static boolean validRequestId(String requestIP,String mobile) {
		if (null != requestIP) {
			String key = CommonConst.REDIS_SMSIP + requestIP +":"+ mobile;
			String cacheIP = DataCacheApi.get(key);
			if (null == cacheIP) {
				DataCacheApi.setex(key, requestIP, CommonConst.CACHE_TIME_IP);
			} else {
				if (!StringUtils.equals(requestIP, cacheIP)) {
					return true;
				} else {
					throw new ValidateException(CodeConst.CODE_REQUEST_OFTEN,
							CodeConst.MSG_REQUEST_OFTEN);
				}
			}
		}
		return true;
	}

	/**
	 * 验证是否最新版本
	 * 
	 * @param requestVersion
	 * @param dbVersion
	 * @return
	 */
	public static boolean validVersionIsNew(String curVersion, String dbVersion) {
		return StringUtils.isBlank(dbVersion)?false:!(curVersion.equals(dbVersion));
	}

	/**
	 * 验证当前页码
	 * <br/>
	 * <b>
	 * 	如果参数未传入或传入格式错误或传入小于等于0的数，则赋予默认页码1
	 * </b>
	 * @param pNo
	 * @return
	 */
	public static int validCurrentPage(String pNo) {
		int currentPage = 0;
		try {
			currentPage = Integer.parseInt(pNo);
		} catch (Exception e) {
			logger.warn("pNo不合法:"+ pNo +"--赋予默认值: "+PageModel.FIRST_PAGE);
		}
		return currentPage <= 0 ? PageModel.FIRST_PAGE : currentPage;
	}

	/**
	 * 验证当前每页显示数量
	 * <br/>
	 * <b>
	 * 	如果参数未传入或传入格式错误或传入小于等于0的数，则赋予默认显示数量10
	 * </b>
	 * @param pSize
	 * @return
	 */
	public static int validPageSize(String pSize) {
		int pageSize = 0;
		try {
			pageSize = Integer.parseInt(pSize);
		} catch (Exception e) {
			logger.warn("pSize不合法:"+ pageSize+"--赋予默认值: "+PageModel.PAGE_SIZE);
		}
		return pageSize <= 0 ? PageModel.PAGE_SIZE : pageSize;
	}
	
	/**
	 * 验证object类型是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		return obj == null?true:"".equals(obj);
	}
	
	public static Object convertJsonStr(String jsonStr,String key){
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonStr);
			return jsonObject.get(key);
		} catch (Exception e) {
			logger.error("jsonObject转换异常",e);
		}
		return null;
	}
	    
	/**
	 * 校验商铺状态
	 * @param shopStatus 商铺状态
	 * @param fiterStatus 可不用校验的商铺状态
	 * @return void [返回类型说明]
	 * @author  shengzhipeng
	 */
	public static void validShopStatus(Integer shopStatus, Integer[] fiterStatus)
            throws Exception {
	    if (null == shopStatus)
        {
	        throw new ValidateException(CodeConst.CODE_SHOP_STATUS_ERROR,
                    CodeConst.MSG_SHOP_STATUS_ERROR);
        }
	    
	    if(fiterStatus != null && fiterStatus.length >0)
	    {
	        for (Integer integer : fiterStatus)
            {
                if(shopStatus.equals(integer))
                {
                    return;
                }
            }
	    }
	    // 校验商铺状态
        if (CommonConst.SHOP_OFFLINE_STATUS == shopStatus)
        {
            throw new ValidateException(CodeConst.CODE_SHOP_STATUS_OFFLINE,
                    CodeConst.MSG_SHOP_STATUS_OFFLINE);
        } 
        else if(CommonConst.SHOP_DEL_STATUS == shopStatus)
        {
            throw new ValidateException(CodeConst.CODE_SHOP_STATUS_BINDING,
                    CodeConst.MSG_SHOP_STATUS_BINDING);
        }
        else if (CommonConst.SHOP_LACK_MONEY_STATUS == shopStatus) {
            throw new ValidateException(CodeConst.CODE_SHOP_STATUS_ARREARS,
                    CodeConst.MSG_SHOP_STATUS_ARREARS);
            //店铺状态不为0，代表异常
        }
        else if(CommonConst.SHOP_AUDIT_STATUS == shopStatus)
        {
            throw new ValidateException(CodeConst.CODE_SHOP_STATUS_PENDING,
                    CodeConst.MSG_SHOP_STATUS_PENDING);
        }
	}
	
	/**
	 * 验证手机号码格式的有效性
	 * 
	 * @param mobile
	 * @return true为有效，false为无效
	 */
	public static boolean validateMobile(Long mobile){
		try {
			validMobileStr(mobile+"", 0, null);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
//	/**
//	 * 验证身份证号
//	 * @param identityCardNo
//	 * @return
//	 */
//	public static boolean isLegalIdentityCardNo(String identityCardNo){
//		identityCardNo = identityCardNo.trim();
//		String year =null;
//		String month = null;
//		String day = null;
//		if(identityCardNo.length() == 15){
//			year = identityCardNo.substring(6,8);
//			month = identityCardNo.substring(8,10);  
//			day = identityCardNo.substring(10,12);
//			if(isInteger(year) && isInteger(month) && isInteger(day)){
//				Date  temp_date = new Date(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day));
//				// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
//				String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(temp_date); 
//				if(Integer.valueOf(yearLast) !=Integer.valueOf(year)   
//			              ||temp_date.getMonth()!=Integer.valueOf(month)-1   
//			              ||temp_date.getDate()!=Integer.valueOf(day)){   
//			                return false;   
//			        }else{   
//			            return true;   
//			        }   
//			}
//			
//		}else if(identityCardNo.length() == 18){
//			year = identityCardNo.substring(6,10);
//			month = identityCardNo.substring(10,12);  
//			day = identityCardNo.substring(12,14);
//			if(isInteger(year) && isInteger(month) && isInteger(day)){
//				Date  temp_date = new Date(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day));
//				// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
//			      if(temp_date.getYear()!=Integer.valueOf(year)   
//			              ||temp_date.getMonth()!=Integer.valueOf(month)-1   
//			              ||temp_date.getDate()!=Integer.valueOf(day)){   
//			                return false;   
//			        }else{   
//			            return true;   
//			        }   
//			}
//		}
//		
//		return false;
//	}
	
	public static boolean isInteger(String value) {
		 try {
		   Integer.parseInt(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
	}
}
