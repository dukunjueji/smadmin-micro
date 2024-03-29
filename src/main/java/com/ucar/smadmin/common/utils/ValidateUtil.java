package com.ucar.smadmin.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 验证工具类
 *
 * @author 吴佰川（baichuan.wu@ucarinc.com）创建
 * @version 1.0
 * @date 2018/10/26 14:55
 */
public final class ValidateUtil {

    /** 整数 */
    private static final String V_INTEGER = "^-?[1-9]\\d*$";

    /** 正整数 */
    private static final String V_Z_INDEX = "^[1-9]\\d*$";

    /** 负整数 */
    private static final String V_NEGATIVE_INTEGER = "^-[1-9]\\d*$";

    /** 数字 */
    private static final String V_NUMBER = "^([+-]?)\\d*\\.?\\d+$";

    /** 正数 */
    private static final String V_POSITIVE_NUMBER = "^[1-9]\\d*|0$";

    /** 负数 */
    private static final String V_NEGATINE_NUMBER = "^-[1-9]\\d*|0$";

    /** 浮点数 */
    private static final String V_FLOAT = "^([+-]?)\\d*\\.\\d+$";

    /** 正浮点数 */
    private static final String V_POSTTIVE_FLOAT = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";

    /** 负浮点数 */
    private static final String V_NEGATIVE_FLOAT = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";

    /** 非负浮点数（正浮点数 + 0） */
    private static final String V_UNPOSITIVE_FLOAT = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$";

    /** 非正浮点数（负浮点数 + 0） */
    private static final String V_UN_NEGATIVE_FLOAT = "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$";

    /** 邮件 */
    private static final String V_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    /** 颜色 */
    private static final String V_COLOR = "^[a-fA-F0-9]{6}$";

    /** url */
    private static final String V_URL = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";

    /** 仅中文 */
    private static final String V_CHINESE = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";

    /** 仅ACSII字符 */
    private static final String V_ASCII = "^[\\x00-\\xFF]+$";

    /** 邮编 */
    private static final String V_ZIPCODE = "^\\d{6}$";

    /** 手机 */
    private static final String V_PHONE = "^1[3456789]\\d{9}$";

    /** 电话 */
    private static final String V_MOBILE = "^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}$";

    /** ip地址 */
    private static final String V_IP4 = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";

    /** 非空 */
    private static final String V_NOTEMPTY = "^\\S+$";

    /** 图片 */
    private static final String V_PICTURE = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";

    /** 压缩文件 */
    private static final String V_RAR = "(.*)\\.(rar|zip|7zip|tgz)$";

    /** 年月日期 */
    private static final String V_YEAR_MONTH = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(0[1234567879]|1[012]))$";
    /** QQ号码 */
    private static final String V_QQ_NUMBER = "^[1-9]*[1-9][0-9]*$";

    /** 电话号码的函数(包括验证国内区号,国际区号,分机号) */
    private static final String V_TEL = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";

    /** 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串 */
    private static final String V_USERNAME = "^\\w+$";

    /** 字母 */
    private static final String V_LETTER = "^[A-Za-z]+$";

    /** 大写字母 */
    private static final String V_LETTER_U = "^[A-Z]+$";

    /** 小写字母 */
    private static final String V_LETTER_I = "^[a-z]+$";

    /** 身份证 */
    private static final String V_IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

    /** 验证密码(数字和英文同时存在) */
    private static final String V_PASSWORD_REG = "[A-Za-z]+[0-9]";

    /** 验证密码长度(6-18位) */
    private static final String V_PASSWORD_LENGTH = "^\\d{6,18}$";

    /** 验证一个月的31天 */
    private static final String V_31DAYS = "^((0?[1-9])|((1|2)[0-9])|30|31)$";

    /**
     * 验证价格
     */
    private static final String V_PRICE = "^([1-9]{1}[0-9]{0,}(\\.[0-9]{0,2})?|0(\\.[0-9]{0,2})?|\\.[0-9]{1,2})$";

    /**
     * 验证金额
     */
    private static final String V_MONEY = "^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$";
    /**
     * 中国人名
     */
    private static final String V_PERSON_NAME = "[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*";

    private ValidateUtil(){}

    /**
     * 验证是不是整数
     * @param value 要验证的字符串 要验证的字符串
     * @return  如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isInteger(String value){
        return match(V_INTEGER,value);
    }

    /**
     * 验证是不是正整数
     * @param value 要验证的字符串
     * @return  如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUnsignedInteger(String value){
        return match(V_Z_INDEX,value);
    }

    /**
     * 验证是不是负整数
     * @param value 要验证的字符串
     * @return  如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNegativeInteger(String value){
        return match(V_NEGATIVE_INTEGER,value);
    }

    /**
     * 验证是不是数字
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNumber(String value){
        return match(V_NUMBER,value);
    }

    /**
     * 验证是不是正数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPositiveNumber(String value){
        return match(V_POSITIVE_NUMBER,value);
    }

    /**
     * 验证是不是负数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNegatineNumber(String value){
        return match(V_NEGATINE_NUMBER,value);
    }

    /**
     * 验证一个月的31天
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isThirtyOneDays(String value){
        return match(V_31DAYS,value);
    }

    /**
     * 验证是不是ASCII
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isASCII(String value){
        return match(V_ASCII,value);
    }


    /**
     * 验证是不是中文
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChinese(String value){
        return match(V_CHINESE,value);
    }
    /**
     * 验证是不是颜色
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isColor(String value){
        return match(V_COLOR,value);
    }

    /**
     * 验证日期年月格式2017-07
     * @param value
     * @return
     */
    public static boolean isYearMonth(String value){
        return match(V_YEAR_MONTH,value);
    }


    /**
     * 验证是不是邮箱地址
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String value){
        return match(V_EMAIL,value);
    }

    /**
     * 验证是不是浮点数
     * @param value 要验证的字符串
     * @return  如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isFloat(String value){
        return match(V_FLOAT,value);
    }

    /**
     * 验证是不是正确的身份证号码
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIdNo(String value){
        return match(V_IDCARD,value);
    }

    /**
     * 验证是不是正确的IP地址
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIp(String value){
        return match(V_IP4,value);
    }

    /**
     * 验证是不是字母
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLetter(String value){
        return match(V_LETTER,value);
    }

    /**
     * 验证是不是小写字母
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLowerCaseLetter(String value){
        return match(V_LETTER_I,value);
    }

    /**
     * 验证是不是大写字母
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUpperCaseLetter(String value){
        return match(V_LETTER_U,value);
    }


    /**
     * 验证是不是手机号码
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isCellphone(String value){
        return match(V_PHONE,value);
    }

    /**
     * 验证是不是电话
     * @param value
     * @return
     */
    public static boolean isMobile(String value){
        return match(V_MOBILE,value);
    }

    /**
     * 验证是不是负浮点数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNegativeFloat(String value){
        return match(V_NEGATIVE_FLOAT,value);
    }

    /**
     * 验证非空
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNotempty(String value){
        return match(V_NOTEMPTY,value);
    }

    /**
     * 验证密码的长度(6~18位)
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNumberLength(String value){
        return match(V_PASSWORD_LENGTH,value);
    }

    /**
     * 验证密码(数字和英文同时存在)
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPasswordReg(String value){
        return match(V_PASSWORD_REG,value);
    }

    /**
     * 验证图片
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPicture(String value){
        return match(V_PICTURE,value);
    }

    /**
     * 验证正浮点数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPosttiveFloat(String value){
        return match(V_POSTTIVE_FLOAT,value);
    }

    /**
     * 验证QQ号码
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isQQnumber(String value){
        return match(V_QQ_NUMBER,value);
    }

    /**
     * 验证压缩文件
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isRar(String value){
        return match(V_RAR,value);
    }

    /**
     * 验证电话
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTel(String value){
        return match(V_TEL,value);
    }

    /**
     * 验证非正浮点数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUnNegativeFloat(String value){
        return match(V_UN_NEGATIVE_FLOAT,value);
    }

    /**
     * 验证非负浮点数
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUnpositiveFloat(String value){
        return match(V_UNPOSITIVE_FLOAT,value);
    }

    /**
     * 验证URL
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUrl(String value){
        return match(V_URL,value);
    }

    /**
     * 验证用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUserName(String value){
        return match(V_USERNAME,value);
    }

    /**
     * 验证邮编
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isZipcode(String value){
        return match(V_ZIPCODE,value);
    }

    /**
     * 是否是价格
     * @param value
     * @return
     */
    public static boolean isPrice(String value){
        return match(V_PRICE,value);
    }

    /**
     * 判断是否金额格式
     * @param value
     * @return
     */
    public static boolean isMoney(String value){
        return match(V_MONEY,value);
    }

    /**
     * 是否是中国人姓名
     *
     * @param value
     */
    public static boolean isPersonName(String value) {
        return match(V_PERSON_NAME,value);
    }
    /**
     * @param regex 正则表达式字符串
     * @param str 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
