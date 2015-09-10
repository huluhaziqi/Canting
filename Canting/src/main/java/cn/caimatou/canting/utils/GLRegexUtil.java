package cn.caimatou.canting.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description：验证正则表达式的工具类
 * <br/><br/>Created by Fausgoal on 15/8/28.
 * <br/><br/>
 */
public class GLRegexUtil {

    /**
     * 邮箱正则表达式
     */
    private static final String REGEX_EMAIL = "([a-zA-Z0-9%_.+\\-]+)@([a-zA-Z0-9.\\-]+?\\.[a-zA-Z]{2,6})"; //"^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.(?:com|cn)$"; // "([a-zA-Z0-9%_.+\\-]+)@([a-zA-Z0-9.\\-]+?\\.[a-zA-Z]{2,6})";

    /**
     * http或https开头或www开头的网址正则表达式
     */
    private static final String REGEX_HOST = "\\b(((h|H)ttps?)://)?([www.]|[WWW.])?([a-zA-Z0-9\\-.]{0,61}?[a-zA-Z0-9]\\.)+([a-zA-Z]{2,6})+(?:(?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?";

    /**
     * 电话正则表达式
     */
    private static final String REGEX_PHONE = "\\b(0[0-9]{2,3}\\-)?([0-9]{7,11})\\b";

    /**
     * 手机号正则表达式
     */
    private static final String REGEX_MOBILE_PHONE = "^(13|15|18|17)[0-9]{9}$";

    /**
     * 身份证正则表达式
     */
    private static final String REGEX_CARDNUMBER = "^(\\d{14}|\\d{17})(\\d|[xX])$";

    /**
     * 中文和字母同时出现正则表达式
     */
    private static final String CHARACTER_AND_CHINESE = "^([\\u4e00-\\u9fa5]+|[A-Za-z]+)$";

    /**
     * 验证数字
     */
    private static final String REGEX_NUMBER = "^[0-9]*$";

    /**
     * 验证是中文、字母、数字
     */
    private static final String CHARACTER_AND_CHINESE_AND_NUM = "^([\\u4e00-\\u9fa50-9]+|[A-Za-z0-9]+)$";

    private GLRegexUtil() {
    }

    public static boolean isMobilePhone(CharSequence value) {
        return check(value, REGEX_MOBILE_PHONE);
    }

    public static boolean isPhone(CharSequence value) {
        return check(value, REGEX_PHONE);
    }

    public static boolean isEmail(CharSequence value) {
        return check(value, REGEX_EMAIL);
    }

    public static boolean isHost(CharSequence value) {
        return check(value, REGEX_HOST);
    }

    public static boolean isIdCardNum(CharSequence value) {
        return check(value, REGEX_CARDNUMBER);
    }

    public static boolean isChatterAndChiness(CharSequence value) {
        return check(value, CHARACTER_AND_CHINESE);
    }

    public static boolean isNumber(CharSequence value) {
        return check(value, REGEX_NUMBER);
    }

    public static boolean isNumAndChaterAndChiness(CharSequence value) {
        return check(value, CHARACTER_AND_CHINESE_AND_NUM);
    }

    private static boolean check(CharSequence value, String pattern) {
        if (GLStringUtil.isEmpty(value) || GLStringUtil.isEmpty(pattern)) {
            return false;
        }
        Matcher matcher = Pattern.compile(pattern).matcher(value);
        return matcher.find();
    }
}
