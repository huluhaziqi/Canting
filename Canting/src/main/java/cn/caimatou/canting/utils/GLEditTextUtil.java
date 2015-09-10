package cn.caimatou.canting.utils;

import android.widget.EditText;

import cn.caimatou.canting.common.GLConst;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/8.
 * <br/><br/>
 */
public class GLEditTextUtil {
    private GLEditTextUtil() {
    }

    /**
     * 将光标移动到指定的EditText中
     *
     * @param editText   editText
     * @param isSetEmpty true 并将editText设置为空
     */
    public static void setEditTextFocus(final EditText editText, final boolean isSetEmpty) {
        if (null == editText) {
            return;
        }
        if (isSetEmpty)
            editText.setText("");
        // 设置光标到指定文本框内
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();

        if (editText.getText().length() != GLConst.NONE) {
            editText.setSelection(editText.getText().length()); // 将光标移动到输入内容的最后
        }
    }
}
