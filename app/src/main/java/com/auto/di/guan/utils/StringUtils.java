package com.auto.di.guan.utils;

import android.content.Context;
import android.text.TextUtils;


import com.auto.di.guan.BaseApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    /**
     * 读取string.xml中的字符串。根据资源Id读取字符串文本（有时候Fragment没有Attached到Activity时，
     * 直接用Fragment里的getString方法获取资源导致崩溃，可以通过此方法获取资源）
     * 
     * @param id
     * @return
     */
    public static String getString(int id) {
        final Context context = BaseApp.getInstance().getApplicationContext();
        return getString(context, id);
    }
    
    /**
     * 读取string.xml中的字符串
     * @param context
     * @param id
     * @return
     */
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }
    
    /**
     * 返回字符串的长度，且一个中文字当4个英文符号
     * @param content
     * @return
     */
    public static int getLengthEx(String content) {
        int length = 0;
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int unicodeNum = (int) chars[i];
            if (unicodeNum >= 19968 && unicodeNum <= 171941) {
                length += 4;
            } else {
                length += 1;
            }
        }
        return length;
    }
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    /**
     * 截取字符串中所有的数字
     * @param content
     * @return
     */
    public static String getNums(String content) {
        Matcher m = Pattern.compile("(\\d+)").matcher(content.replace(" ", ""));
        StringBuffer buffer = new StringBuffer();
        while (m.find()) {
            buffer.append(m.group());
        }
        return buffer.toString();
    }
    
    /**
     * 返回content字符串中，第一个完整的数字。比如：Str98 就返回 98 比如：Str87uyuy232 就返回 87
     * 业务需求：因为历史原因，如今有信用户的生日格式已确定的至少有三种：2012-09-09 2012年9月9日 2012/09/09
     * 所以用这个方法来获取用户的出生年份，然后计算用户的年龄
     * @param content
     * @return 异常情况返回-1
     */
    public static int getNumsOfStart(String content) {
        int result = -1;
        if (!TextUtils.isEmpty(content)) {
            Matcher matcher = Pattern.compile("\\d+").matcher(content);
            if (matcher.find()) {
                result = Integer.valueOf(matcher.group());
            }
        }
        return result;
    }

    /**
     * 是否为合法的ip地址
     * @param ipValue
     * @return
     */
    public static boolean isIPv4(String ipValue) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipValue);
        return matcher.matches();
    }
    
    /**
     * 是否为一个整形数字
     * @param content
     * @return
     */
    public static boolean isIntNum(String content) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(content).matches();
    }
    
    /**
     * 字符串中是否包含有中文字符
     * @param content
     * @return
     */
    public static boolean containsZh(String content) {
        return Pattern.compile("[\u4e00-\u9fa5]").matcher(content).find();
    }

    /**
     * 验证是否是邮箱格式
     * @param emailContent
     * @return
     */
    public static boolean isEmail(String emailContent) {
        Pattern pattern = Pattern.compile("[A-Z0-9a-z._-]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,4}");
        // .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = pattern.matcher(emailContent);
        return m.matches();
    }
    
    /**
     * 将手机号码转成安全的数字格式:15212345612 -> 152 1234 5612
     * @param phone
     * @return
     */
    public static String formatPhoneNum(String phone) {
        // 字符串倒置并转为数组
        char[] reversePhone = new StringBuffer(phone).reverse().toString().toCharArray();
        StringBuilder builder = new StringBuilder();
        int m = 0;
        for (int i = 0; i < reversePhone.length; i++) {
            m++;
            if (m != 0 && m % 4 == 0)
                builder.append(reversePhone[i] + " ");
            else
                builder.append(reversePhone[i]);
        }
        return builder.reverse().toString();
    }

    /**
     * 格式化字符串，去除“空格、回车、换行、制表符”
     * @author: amos
     * @date: 16/3/17 上午10:51
     */
    public static String formatString(String s) {
        String dest = "";
        if (s!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(s);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 是否包含非法字符，如“空格、回车、换行、制表符”
     * @author: amos
     * @date: 16/3/17 上午11:38
     */
    public static boolean isContainIllegalChar(String s) {
        if (s!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(s);
            return m.find();
        }
        return false;
    }
}
