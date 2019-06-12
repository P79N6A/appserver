package com.idcq.appserver.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.text.NumberFormat;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 数字工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 下午7:30:16
 */
public class NumberUtil {

    /**
     * 计算数字字符串有效的小数位数
     * 
     * @param digit 数字字符串
     * @return 有效的小数位数
     */
    public static int getValidDecimals(String digit) {
        boolean flag = false;
        do {
            if (!digit.contains(".")) {
                return 0;
            }
            int len = digit.length() - 1;
            String lastChar = digit.charAt(len) + "";
            if (lastChar.equals("0")) {
                digit = digit.substring(0, len);
                flag = true;
            } else {
                if (".".equals(lastChar)) {
                    digit = digit.substring(0, len);
                }
                flag = false;
            }
            // System.out.println(digit);
        } while (flag);
        BigDecimal bigD = new BigDecimal(digit);
        return bigD.scale();
    }

    // public static String roundDoubleToStr(Double num ,int scale){
    // if(num == null){
    // return null;
    // }
    // // 获取小数位数（排除小数位末尾的0）
    // // int validScale = getValidDecimals(num+"");
    // // 获取全部小数位数
    // int fullScale = new BigDecimal(num+"").scale();
    // if(fullScale > scale){//超过指定的位数需要截取
    // return fmtDouble(num, scale)+"";
    // }else{//小于指定位数需要补零
    // int offSet = scale - fullScale;
    // StringBuilder numStr = new StringBuilder(num+"");
    // for(int i=0;i<offSet;i++){
    // // 末尾补零
    // numStr.append(0);
    // }
    // return numStr.toString();
    // }
    // }

    /**
     * 是否正整数
     * 
     * @param num 不等于空并且大于0为正整数
     * @return
     */
    public static boolean isPositLong(Long num) {
        if (num == null || num <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isPositLongStr(String num) {
        if (StringUtils.isBlank(num)) {
            return false;
        }
        try {
            long num2 = Long.parseLong(num);
            if (num2 <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isPositInt(Integer num) {
        if (num == null || num <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否数字字符串
     * @param str
     * @return
     */
    public static boolean isNumer(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        return pattern.matcher(str).matches();
    }

    /**
     * 数字字符串转Integer类型
     * 
     * @param str 要转换的数字字符
     * @param propertyName 属性名称
     * @return
     * @throws Exception
     */
    public static Integer strToNum(String str, String propertyName) throws Exception {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Integer num;
        try {
            num = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return num;
    }

    /**
     * 数字字符串转Long类型
     * 
     * @param str 要转换的数字字符
     * @param propertyName 属性名称
     * @return
     * @throws Exception
     */
    public static Long strToLong(String str, String propertyName) throws Exception {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Long num;
        try {
            num = Long.valueOf(str);
        } catch (NumberFormatException e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return num;
    }

    /**
     * 数字字符串转Integer类型
     * 
     * @param str 要转换的数字字符
     * @param propertyName 属性名称
     * @return
     * @throws Exception
     */
    public static Integer strToInteger(String str, String propertyName) throws Exception {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Integer num;
        try {
            num = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return num;
    }

    public static boolean isDouble(String propertyName, String propertyValue) {
        boolean isDouble = false;
        try {
            Double.parseDouble(propertyValue);
            isDouble = true;
        } catch (NumberFormatException ex) {
            isDouble = false;
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return isDouble;
    }

    public static Double strToDouble(String str, String propertyName) throws Exception {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Double num;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return num;
    }

    public static Float strToFloat(String str, String propertyName) throws Exception {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Float num;
        try {
            num = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return num;
    }

    /**
     * 判断字符串是否能转换成double
     * 
     * @param numStr
     * @return
     */
    public static boolean isDouble(String numStr) {
        boolean isDouble = false;
        try {
            Double.parseDouble(numStr);
            isDouble = true;
        } catch (Exception e) {
            isDouble = false;
        }
        return isDouble;
    }

    public static boolean isLong(String propertyName, String propertyValue) {
        boolean isLong = false;
        try {
            Long.parseLong(propertyValue);
            isLong = true;
        } catch (NumberFormatException ex) {
            isLong = false;
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return isLong;
    }

    /**
     * 银行卡号屏蔽
     * @param cardNumber
     * @return
     */
    public static String deailCarNumber(String cardNumber) {
        try {
            if (StringUtils.isNotEmpty(cardNumber)) {
                int length = cardNumber.length();
                int fristIndex = (length - 8) / 2;
                int endIndex = fristIndex + 8;
                cardNumber = cardNumber.substring(0, fristIndex) + "********" + cardNumber.substring(endIndex, length);
            }
        } catch (Exception e) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_INVALID_PARAMETER);
        }
        return cardNumber;
    }

    /**
     * 按指定小数位数四舍五入并返回字符串
     * @param num
     * @param scale
     * @return
     * @throws Exception
     */
    public static String roundBigDecimalToStr(BigDecimal num, int scale) throws Exception {
        if (num == null) {
            return null;
        }
        num = num.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return num.toString();
    }

    /**
     * 按指定小数位数四舍五入并返回字符串
     * @param num
     * @param scale
     * @return
     * @throws Exception
     */
    public static String roundDoubleToStr(Double num, int scale) {
        if (num == null) {
            return null;
        }
        BigDecimal big = new BigDecimal(num + "");
        big = big.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return big.toString();
    }

    /**
     * 获取银行卡号后4位
     * @param cardNumber
     * @return
     */
    public static String showAfter4OfCarNumber(String cardNumber) {
        try {
            if (StringUtils.isNotEmpty(cardNumber) && cardNumber.length() > 4) {
                cardNumber = cardNumber.substring(cardNumber.length() - 4);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardNumber;
    }

    public static double formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("#.000");
        double result = Double.parseDouble(df.format(num));
        return result;
    }

    /**
     * 格式化double数据，保留len长度小数，不做四舍五入
     * @param num
     * @param len
     * @return
     */
    public static double formatDouble(Double num, int len) {
        if(num == null) {
            num = 0D;
        }
        BigDecimal bd = new BigDecimal(num + "");
        bd = bd.setScale(len, BigDecimal.ROUND_DOWN);
        double result = bd.doubleValue();
        return result;
    }

    /**
     * 请各位同学不要改我的代码 格式化double数据，保留len长度小数，自动四舍五入
     * @param num
     * @param len
     * @return
     */
    public static double formatDoubleForSolr(double num, int len) {
        String fmt = "";
        for (int i = 0; i < len; i++) {
            fmt += "0";
        }
        DecimalFormat df = new DecimalFormat("#." + fmt);
        double result = Double.parseDouble(df.format(num));
        return result;
    }

    /**
     * 格式化float数据保留len小数
     * @Title: formatFloat
     * @param @param num
     * @param @param len
     * @param @return
     * @return double 返回类型
     * @throws
     */
    public static float formatFloat(float num, int len) {
        String fmt = "";
        for (int i = 0; i < len; i++) {
            fmt += "0";
        }
        DecimalFormat df = new DecimalFormat("#." + fmt);
        float result = Float.parseFloat(df.format(num));
        return result;
    }

    /**
     * 格式化double数据保留len小数
     * @Title: formatFloat
     * @param @param num
     * @param @param len
     * @param @return
     * @return double 返回类型
     * @throws
     */
    public static double fmtDouble(double num, int len) {
        String fmt = "";
        for (int i = 0; i < len; i++) {
            fmt += "0";
        }
        DecimalFormat df = new DecimalFormat("#." + fmt);
        double result = Double.parseDouble(df.format(num));
        return result;
    }

    public static Long stringToLong(String str) {
        Long num = null;
        try {
            num = Long.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return num;
        }
        return num;
    }

    public static Integer stringToInteger(String str) {
        Integer num = null;
        try {
            num = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return num;
        }
        return num;
    }

    /**
     * 　　* 两个Double数相加 d1+d2 　　*
     * @param d1 　　* @param d2 　　*
     * @return Double 　　
     */
    public static Double add(Double d1, Double d2) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static Double add(Double... args) {
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = 0D;
            }
            total = total.add(new BigDecimal(args[i].toString()));
        }
        return total.setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 　　* 两个Double数相乘 d1*d2 　
     * @param d1 　　 * @param d2 　　
     * @return Double 　　
     */
    public static Double multiply(Double d1, Double d2) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static Double multiply(Double... args) {
        if (null != args && args.length > 0) {
            BigDecimal total = new BigDecimal(args[0].toString());
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    if (args[i] == null) {
                        args[i] = 0D;
                    }
                    total = total.multiply(new BigDecimal(args[i].toString()));
                }
            }
            return total.setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
        }
        return 0D;
    }
    
    /**
     * 　 两个Double数相除 d1/d2
     * @param d1 　　 * @param d2
     * @return Double 　　
     */
    public static Double divide(Double d1, Double d2) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        // return b1.divide(b2).doubleValue();
        return new Double(b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 　 两个Double数相除 d1/d2
     * @param d1 　　 * @param d2
     * @return Double 　　
     */
    public static Double divide(Double d1, Double d2, int n) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        // return b1.divide(b2).doubleValue();
        return new Double(b1.divide(b2, n, BigDecimal.ROUND_DOWN).doubleValue());
    }

    /**
     * 两个Double数相减 d1-d2 　　
     * @param d1
     * @param d2 　 　* @return Double 　　
     */
    public static Double sub(Double d1, Double d2) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 求两个数的最小值
     * @param d1
     * @param d2
     * @return 最小值
     * @author shengzhipeng
     */
    public static Double min(Double d1, Double d2) {
        if (d1 == null) {
            d1 = 0D;
        }
        if (d2 == null) {
            d2 = 0D;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        if (b1.compareTo(b2) < 0) {
            return b1.doubleValue();
        } else {
            return b2.doubleValue();
        }
    }

    /**
     * 引用类型的整型转字符串
     * @param param
     * @return 字符串
     * @return String [返回类型说明]
     * @author shengzhipeng
     */
    public static String getStr(Integer param) {
        if (null == param) {
            return null;
        }
        return param.toString();
    }

    /**
     * 引用类型的长整型转字符串
     * @param param
     * @return 字符串
     * @return String [返回类型说明]
     * @author shengzhipeng
     */
    public static String getStr(Long param) {
        if (null == param) {
            return null;
        }
        return param.toString();
    }

    /**
     * 转换 double科学计数法
     * @param param
     * @return 字符串
     * @return String [返回类型说明]
     * @author shengzhipeng
     */
    public static String formatDoubleStr(Double doubleStr, int length) {
        if(doubleStr == 0D || doubleStr == null) {
            return "0";
        }
        BigDecimal bg = new BigDecimal(doubleStr.toString());
        bg = bg.setScale(length, BigDecimal.ROUND_DOWN);
        return bg.toString();
    }

    public static String formatDoubleStr2BigDecimalStr(String doubleStr, int length) {
        if (StringUtils.isBlank(doubleStr)) {
            doubleStr = "0";
        }

        BigDecimal bg = new BigDecimal(doubleStr);
        bg = bg.setScale(length, BigDecimal.ROUND_DOWN);
        return bg.toString();
    }

    /**
     * 将数据库查出的结果集中的Double字段转换成BigDecimal类型，避免Double类型字段显示其科学计数
     * @param doubleResultList 结果集
     * @param doubleFields Double字段名
     * @param scale 精度
     */
    public static void formatDoubleResult2BigDecimalResult(List<Map<String, Object>> doubleResultList,
            String[] doubleFields, int scale) {
        if (doubleResultList.isEmpty()) {
            return;
        }

        if (doubleFields == null || doubleFields.length == 0) {
            return;
        }

        for (Map<String, Object> doubleResultMap : doubleResultList) {
            for (String doubleField : doubleFields) {
                String doubleStr = doubleResultMap.get(doubleField).toString();
                if (StringUtils.isBlank(doubleStr)) {
                    continue;
                }
                doubleResultMap.put(doubleField, formatDoubleStr2BigDecimalStr(doubleStr, scale));
            }
        }
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     * 
     * @param longitude1 第一点经度
     * @param latitude1 第一点纬度
     * @param longitude2 第二点经度
     * @param latitude2 第二点纬度
     * @return 返回距离 单位：米
     */
    public static double computeDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double a, b, R;
        R = 6371393; // 地球半径
        latitude1 = latitude1 * Math.PI / 180.0;
        latitude2 = latitude2 * Math.PI / 180.0;
        a = latitude1 - latitude2;
        b = (longitude1 - longitude2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1) * Math.cos(latitude2) * sb2 * sb2));
        return d;
    }

    /**
     * 保留scale位小数，向下取整
     * @param v 值
     * @param scale 小数位数
     * @return BigDecimal
     */
    public static BigDecimal formatVal(String v, int scale) {
        BigDecimal big = new BigDecimal(v);
        big = big.setScale(2, BigDecimal.ROUND_DOWN);
        return big;
    }

    /**
     * 1. 应付金额>对应账户余额，应付金额余最小整数。 2. 1元>应付金额>对应账户余额，用完账户资金。 3. 账户资金不足1元时直接扣完。
     * @param payAmount 需要支付的金额
     * @param amount 账户余额
     * @return double 需要支付的金额
     * @author shengzhipeng
     * @date 2016年3月12日
     */
    public static BigDecimal countRecentNum(BigDecimal payAmount, BigDecimal amount) {
        // if(payAmount.compareTo(BigDecimal.ONE) < 0) {TODO 会议结论，去除新规则，保持原来
        // return payAmount;
        // }
        // if(payAmount.compareTo(amount) > 0) {
        // //先得到需要支付金额的整数部分
        // BigDecimal needPayInt = payAmount.setScale(0,BigDecimal.ROUND_FLOOR);
        // //取到需要支付金额的小数部分
        // BigDecimal needPayFloat = payAmount.subtract(needPayInt).abs();
        // if(needPayFloat.compareTo(amount) > 0) {
        // //余额小于需要支付金额的小数部分直接扣完，不需要处理
        // return payAmount;
        // }
        // //用余额减去需要支付的金额的小数部分再取整
        // BigDecimal temp =
        // amount.subtract(needPayFloat).abs().setScale(0,BigDecimal.ROUND_FLOOR);
        // //最后得到需要真正需要支付的金额
        // BigDecimal needPayAmount = temp.add(needPayFloat);
        // return needPayAmount;
        // }
        return payAmount;
    }

    /**
     * 验证是不是正整数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean NegatineNumber(String value) {
        String V_POSITIVE_NUMBER = "^[1-9]\\d*|0$";
        return match(V_POSITIVE_NUMBER, value);
    }

    /**
     * 验证正浮点数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean Posttive_float(String value) {
        String V_POSTTIVE_FLOAT = "^[0-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";
        return match(V_POSTTIVE_FLOAT, value);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param 比较三个数最大值
     */
    public static int getMax(int a, int b, int c) {

        return Math.max(Math.max(a, b), c);

    }

    /**
     * @param 判定手机号码格式
     */
    public static boolean isMobileNO(String mobiles, String propertyName) {
        boolean isMobile = true;
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        isMobile = m.matches();
        if (!isMobile) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, propertyName
                    + CodeConst.MSG_INVALID_PARAMETER);
        }
        return isMobile;
    }

    public static Double convertNum(Double value) {
        try {
            if (value == null) {
                value = 0D;
            }
            NumberFormat nf = NumberFormat.getInstance();
            // 不使用千分位，即展示为11672283.234，而不是11,672,283.234
            nf.setGroupingUsed(false);
            // 设置数的小数部分所允许的最小位数
            nf.setMinimumFractionDigits(0);
            // 设置数的小数部分所允许的最大位数
            nf.setMaximumFractionDigits(4);
            return Double.valueOf(nf.format(nf.parse(value.toString()).doubleValue()));
        } catch (Exception e) {
            return 0D;
        }

    }
    
}
