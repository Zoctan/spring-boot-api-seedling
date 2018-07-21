package com.zoctan.seedling.util;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

/**
 * 身份证相关工具
 * <p>
 * 【中华人民共和国国家标准GB11643-1999】中有关公民身份号码的规定：公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
 * 排列顺序从左至右依次为：6位数字地址码，8位数字出生日期码，3位数字顺序码和1位数字校验码。
 * 1-2位数字：所在省份的代码；
 * 3-4位数字：所在城市的代码；
 * 5-6位数字：所在区县的代码；
 * 7-14位数字：出生年、月、日；
 * 15-16位数字：所在地的派出所的代码；
 * 17位数字表示性别：奇数表示男性，偶数表示女性；（在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。）
 * 18位数字是校检码：检验身份证的正确性。校检码可以是数字0~9和字符X。
 * <p>
 * 校验码的计算方法为：
 * 1.将前面的身份证号码17位数分别乘以不同的系数。从第1位到第17位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；
 * 2.将这17位数字和系数相乘的结果相加；
 * 3.用加出来和除以11，看余数是多少；
 * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2；
 * （比如余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2）
 *
 * @author Zoctan
 * @date 2018/07/19
 */
public class IdCardUtils {
    /**
     * 省代码表
     */
    private static final Hashtable<String, String> PROVINCE_CODE = new Hashtable<String, String>() {{
        this.put("11", "北京");
        this.put("12", "天津");
        this.put("13", "河北");
        this.put("14", "山西");
        this.put("15", "内蒙古");
        this.put("21", "辽宁");
        this.put("22", "吉林");
        this.put("23", "黑龙江");
        this.put("31", "上海");
        this.put("32", "江苏");
        this.put("33", "浙江");
        this.put("34", "安徽");
        this.put("35", "福建");
        this.put("36", "江西");
        this.put("37", "山东");
        this.put("41", "河南");
        this.put("42", "湖北");
        this.put("43", "湖南");
        this.put("44", "广东");
        this.put("45", "广西");
        this.put("46", "海南");
        this.put("50", "重庆");
        this.put("51", "四川");
        this.put("52", "贵州");
        this.put("53", "云南");
        this.put("54", "西藏");
        this.put("61", "陕西");
        this.put("62", "甘肃");
        this.put("63", "青海");
        this.put("64", "宁夏");
        this.put("65", "新疆");
        this.put("71", "台湾");
        this.put("81", "香港");
        this.put("82", "澳门");
        this.put("91", "国外");
    }};

    /**
     * 最后一位校验码
     */
    private static final String[] LAST_CODE = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private IdCardUtils() {

    }

    /**
     * 判断二代身份证合法性
     *
     * @param idCard 身份证
     * @return Boolean
     */
    public static Boolean validate(@NotNull final String idCard) {
        if (idCard == null) {
            return false;
        }

        // 18位长度
        if (idCard.length() != 18) {
            System.err.println("长度不对");
            return false;
        }

        // 前17位全为数字
        final String idCard17 = idCard.substring(0, 17);
        if (!isDigital(idCard17)) {
            System.err.println("前17位不全为数字");
            return false;
        }

        // 校验省份
        final String provinceCode = idCard.substring(0, 2);
        if (!PROVINCE_CODE.containsKey(provinceCode)) {
            System.err.println("省份不对");
            return false;
        }

        // 校验生日
        final String birthday = idCard.substring(6, 14);
        try {
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate.parse(birthday, dateTimeFormatter);
        } catch (final Exception e) {
            System.err.println("生日不对");
            return false;
        }

        // 校验第18位
        final String idCard18Code = idCard.substring(17, 18);
        int powerSum = 0;
        for (int i = 0; i < 17; i++) {
            powerSum += Integer.parseInt(String.valueOf(idCard17.charAt(i))) * POWER[i];
        }
        // 将对权值和取11模得到余数
        final String lastCode = LAST_CODE[powerSum % 11];
        // 身份的第18位与算出来的校码进行匹配
        return idCard18Code.equalsIgnoreCase(lastCode);
    }

    /**
     * 判断是否全为数字
     *
     * @param string 字符串
     * @return Boolean
     */
    private static Boolean isDigital(final String string) {
        final char[] cs = string.toCharArray();
        for (final char c : cs) {
            if (48 > c || c > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取生日
     * 生日格式：yyyy-mm-dd
     *
     * @param idCard 身份证
     * @return 生日
     */
    public static String getBirthday(@NotNull final String idCard) {
        if (!validate(idCard)) {
            return null;
        }
        final String birthday = idCard.substring(6, 14);
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthday, dateTimeFormatter).toString();
    }

    /**
     * 获取性别
     *
     * @param idCard 身份证
     * @return 男 | 女
     */
    public static String getSex(@NotNull final String idCard) {
        if (!validate(idCard)) {
            return null;
        }
        final String sex = idCard.substring(16, 17);
        return (Integer.valueOf(sex) & 1) == 0 ? "女" : "男";
    }

    /**
     * 获取省份
     *
     * @param idCard 身份证
     * @return 省份
     */
    public static String getProvince(@NotNull final String idCard) {
        if (!validate(idCard)) {
            return null;
        }
        final String provinceCode = idCard.substring(0, 2);
        return PROVINCE_CODE.get(provinceCode);
    }
}
