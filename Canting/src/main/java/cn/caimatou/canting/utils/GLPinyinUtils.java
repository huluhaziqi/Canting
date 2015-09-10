package cn.caimatou.canting.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.caimatou.canting.R;
import cn.caimatou.canting.modules.GLCantingApp;

/**
 * Created by Rosicky on 15/8/31.
 */
public class GLPinyinUtils {
    private static HashMap<String, String> specialWords = GLPinyinUtils.specialChineseWordInit();

    private static HashMap<String, String> specialChineseWordInit() {
        String[] arrayChinese = GLCantingApp.getIns().getResources().getStringArray(R.array.polyPhoneChinese);
        String[] arrayCharacters = GLCantingApp.getIns().getResources().getStringArray(R.array.polyPhoneCharacters);
        HashMap<String, String> words = new HashMap<String, String>();
        for (int i = 0; i < arrayChinese.length; i++) {
            words.put(arrayChinese[i], arrayCharacters[i]);
        }
        return words;
    }
    @Deprecated
    public static Set<String> getAllPinYin(List<String> pinyin) {
        Set<String> result = new HashSet<String>();
        result.add("");
        for (String s : pinyin) {
            Set<String> tmp = new HashSet<String>();
            String[] array = s.split(" ");
            for (String ss : result) {
                for (int i = 0; i < array.length; i++) {
                    tmp.add(ss + array[i]);
                    tmp.add(ss + array[i].charAt(0));
                }
            }
            if (!tmp.isEmpty()) {
                result.clear();
                result = tmp;
            }
        }
        return result;
    }

    /**²»Ö§³Ö¶àÒô×ÖºÍÊ××ÖÄ¸È«Æ´»ìºÏ
     * */
    public static Set<String> getAllPinyinForSearch(List<String> pinyin) {
        Set<String> result = new HashSet<String>();
        String all = "";
        String head = "";
        for (String s : pinyin) {
            String[] array = s.split(" ");
            if (array.length > 0) {
                all += array[0];
                head += array[0].charAt(0);
            }
        }
        result.add(all);
        result.add(head);
        return result;
    }

    /**Ö§³Ö¶àÒô×Ö, ²»Ö§³ÖÊ××ÖÄ¸È«Æ´»ìºÏ
     * */
    public static Set<String> getAllPinyinForSearch2(List<String> pinyin) {
        Set<String> resultAll = new HashSet<String>();
        Set<String> resultHead = new HashSet<String>();
        resultAll.add("");
        resultHead.add("");
        for (String s : pinyin) {
            Set<String> tmp = new HashSet<String>();
            Set<String> tmp2 = new HashSet<String>();
            String[] array = s.split(" ");
            for (int i = 0; i < array.length; i++) {
                for (String ss : resultAll) {
                    tmp.add(ss + array[i]);
                }
                for (String h : resultHead) {
                    tmp2.add(h + array[i].charAt(0));
                }
            }
            if (!tmp.isEmpty()) {
                resultAll.clear();
                resultHead.clear();
                resultAll = tmp;
                resultHead = tmp2;
            }
        }
        resultAll.addAll(resultHead);
        return resultAll;
    }

    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String output = "";
        if (inputString == null || inputString.length() == 0) {
            return output;
        }
        try {
            if (java.lang.Character.toString(inputString.charAt(0)).matches("[\\u4E00-\\u9FA5]+")) {
                String[] temp = toHanyuPinyinStringArray(inputString.charAt(0), format);
                if (temp != null) {
                    output += TextUtils.join(" ", temp);
                } else {
                    output += java.lang.Character.toString(inputString.charAt(0)).toLowerCase();
                }
                temp = null;
            } else
                output += java.lang.Character.toString(inputString.charAt(0)).toLowerCase();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String[] getSinglePingYinAndCategoryChar(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String[] output = {"", "*"};
        if (inputString == null || inputString.length() == 0) {
            return output;
        }
        try {
            char firstChar = inputString.charAt(0);
            String firstString = java.lang.Character.toString(firstChar);
            if (firstString.matches("[\\u4E00-\\u9FA5]+")) {
                String[] temp = toHanyuPinyinStringArray(firstChar, format);
                if (temp != null && temp.length > 0) {
                    output[0] = temp[0];
                    output[1] = (temp[0].charAt(0) + "").toUpperCase();
                } else {
                    output[0] = firstString.toLowerCase();
                    output[1] = "*";
                }
                temp = null;
            } else if (isLetter(firstString)) {
                output[0] = firstString.toLowerCase();
                output[1] = firstString.toUpperCase();
            } else {
                output[0] = firstString.toLowerCase();
                output[1] = "*";
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String converterToFirstSpell(String chinese) {
        String pinyinName = "";
        String firstChar = chinese.substring(0, 1);
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        if (firstChar.matches("[\\u4E00-\\u9FA5]+")) {
            try {
                String[] temp = toHanyuPinyinStringArray(firstChar.toCharArray()[0], defaultFormat);
                if (temp != null) {
                    pinyinName += temp[0].charAt(0);
                } else {
                    pinyinName += "*";
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else if (isLetter(firstChar)) {
            pinyinName += firstChar.toUpperCase();
        } else {
            pinyinName += "*";
        }
        return pinyinName;
    }

    public static String getPinyingFirstChar(String s) {
        String pinyinfirstChar = "";
        String firstChar = s.substring(0, 1);
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        if (firstChar.matches("[\\u4E00-\\u9FA5]+")) {
            try {
                String[] temp = toHanyuPinyinStringArray(firstChar.toCharArray()[0], format);
                if (temp != null) {
                    pinyinfirstChar = temp[0].substring(0, 1);
                } else {
                    pinyinfirstChar = firstChar;
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            pinyinfirstChar = firstChar;
        }
        return pinyinfirstChar;
    }

    public static String[] toHanyuPinyinStringArray(char chineseWord, HanyuPinyinOutputFormat format) throws BadHanyuPinyinOutputFormatCombination {
        String word = "" + chineseWord;
        if (specialWords.containsKey(word)) {
            if (HanyuPinyinCaseType.LOWERCASE == format.getCaseType()) {
                return new String[] { specialWords.get(word).toLowerCase() };
            } else {
                return new String[] { specialWords.get(word).toUpperCase() };
            }
        }
        return PinyinHelper.toHanyuPinyinStringArray(chineseWord, format);
    }

    public static boolean isNumber(String condition){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(condition);
        return matcher.matches();
    }

    public static boolean isLetter(String condition){
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(condition);
        return matcher.matches();
    }

    public static boolean isLetterOrNumber(String condition){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(condition);
        return matcher.matches();
    }
}
