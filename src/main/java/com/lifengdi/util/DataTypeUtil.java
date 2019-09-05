package com.lifengdi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据类型判断
 * @author 李锋镝
 * @date Create at 15:36 2019/4/4
 */
public class DataTypeUtil {

    /**
     * 判断是否是数字类型的
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (StringUtils.isBlank(cs)) {
            return true;
        }
        Pattern pattern = Pattern.compile("[-+]?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(cs);
        return isNum.matches();
    }

    public static boolean isBoolean(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.equals("true") || str.equals("false");
    }

    /**
     * 判断字符串是否是指定的时间格式
     * @param str str
     * @param format 格式
     * @return boolean
     */
    public static boolean isDateTime(final String str, final String format) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是Array<Number>
     * @param str str
     * @return boolean
     */
    public static boolean isArrayNumber(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSONArray jsonArray = JSON.parseArray(str);
            Iterator<Object> iterator = jsonArray.iterator();
            boolean result = false;
            while (iterator.hasNext()) {
                result = isNumeric(iterator.next().toString());
                if (!result) {
                    return result;
                }
            }
            return result;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<String>
     * @param str str
     * @return boolean
     */
    public static boolean isArrayString(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSON.parseArray(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<Boolean>
     * @param str str
     * @return boolean
     */
    public static boolean isArrayBoolean(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSONArray jsonArray = JSON.parseArray(str);
            Iterator<Object> iterator = jsonArray.iterator();
            boolean result = false;
            while (iterator.hasNext()) {
                result = isBoolean(iterator.next().toString());
                if (!result) {
                    return result;
                }
            }
            return result;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<DateTime>
     * @param str str
     * @param format format
     * @return boolean
     */
    public static boolean isArrayDateTime(final String str, final String format) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSONArray jsonArray = JSON.parseArray(str);
            Iterator<Object> iterator = jsonArray.iterator();
            boolean result = false;
            while (iterator.hasNext()) {
                result = isDateTime(iterator.next().toString(), format);
                if (!result) {
                    return result;
                }
            }
            return result;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是时间戳（秒）
     * @param str str
     * @return boolean
     */
    public static boolean isTimestampSec(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        // 秒 长度是十位
        if (StringUtils.isNumeric(str)) {
            return str.length() == 10;
        }
        return false;
    }

    /**
     * 判断字符串是否是时间戳（毫秒）
     * @param str str
     * @return boolean
     */
    public static boolean isTimestampMill(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        // 毫秒 长度是13位
        if (StringUtils.isNumeric(str)) {
            return str.length() == 13;
        }
        return false;
    }

    public static void main(String[] a) {

//        DateTime dateTime = new DateTime();
//        System.out.println(dateTime.getYear());
//        System.out.println(dateTime.getMonthOfYear());
//
//        System.out.println(isDateTime("2019-06-01T15:59:59.656Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));;
//

        String str = "[{bizCode=file_sys, url=http://prifileqa.kanche.com/file_sys/2019/5/1000299365-LFPM4ADP6D1A10733.pdf}, {bizCode=file_sys, url=http://prifileqa.kanche.com/file_sys/2019/5/1000318927-LFMBEC4D6C0137728.pdf}]";
//        System.out.println(JSONArray.);
    }
}
