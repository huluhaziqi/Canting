package cn.caimatou.canting.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

import cn.caimatou.canting.R;

/**
 * Description：验证码输入View
 * <br/><br/>Created by Fausgoal on 15/8/29.
 * <br/><br/>
 */
public class GLVerifyCodeView extends EditText {

    private int textLength;

    private int borderColor;
    private float borderWidth;
    private float borderRadius;

    private int passwordLength;
    private int passwordColor;
    private float passwordWidth;
    private float passwordRadius;

    private Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint;

    private final int defaultContMargin = 1;
    private final int defaultSplitLineWidth = 1;
    private boolean isPassword = false;

    //一个字符或横线占用的宽度
    private int characterWidth;
    //一个数字后中间的间隔
    private int centerSpacing;
    //两字符间隔
    private int characterSpacing;
    private int textSize;
    float textBaseY;

    public GLVerifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //能获取焦点才能弹出软键盘
        setFocusableInTouchMode(true);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerSpacing = getResources().getDimensionPixelSize(R.dimen.dim_15px);
        characterSpacing = getResources().getDimensionPixelSize(R.dimen.dim_15px);
        textSize = getResources().getDimensionPixelSize(R.dimen.dim_42px_sp);

        final Resources res = getResources();

        final int defaultBorderColor = res.getColor(R.color.default_ev_border_color);
        final float defaultBorderWidth = res.getDimension(R.dimen.default_ev_border_width);
        final float defaultBorderRadius = res.getDimension(R.dimen.default_ev_border_radius);

        final int defaultPasswordLength = res.getInteger(R.integer.default_ev_password_length);
        final int defaultPasswordColor = res.getColor(R.color.default_ev_password_color);
        final float defaultPasswordWidth = res.getDimension(R.dimen.default_ev_password_width);
        final float defaultPasswordRadius = res.getDimension(R.dimen.default_ev_password_radius);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        try {
            borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, defaultBorderColor);
            borderWidth = a.getDimension(R.styleable.PasswordInputView_borderWidth, defaultBorderWidth);
            borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, defaultBorderRadius);
            passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, defaultPasswordLength);
            passwordColor = a.getColor(R.styleable.PasswordInputView_passwordColor, defaultPasswordColor);
            passwordWidth = a.getDimension(R.styleable.PasswordInputView_passwordWidth, defaultPasswordWidth);
            passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, defaultPasswordRadius);
            isPassword = a.getBoolean(R.styleable.PasswordInputView_isPassword, false);
        } finally {
            a.recycle();
        }

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在View上点击时弹出软键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //反射调隐藏的focusIn方法，如果不调，在VerifyCodeView之前有EditText等可输入控件时，不会弹出输入法
        //可能有其他google提供的方法，但我没找到
        Class<? extends InputMethodManager> immClass = imm.getClass();
        try {
            Method focusIn = immClass.getDeclaredMethod("focusIn", View.class);
            focusIn.invoke(imm, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imm.viewClicked(this);
        imm.showSoftInput(this, 0);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 外边框
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

        // 内容区
        RectF rectIn = new RectF(rect.left + defaultContMargin,
                rect.top + defaultContMargin,
                rect.right - defaultContMargin,
                rect.bottom - defaultContMargin);
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

        // 分割线
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        if (isPassword) {
            // 密码
            float cx, cy = height / 2;
            float half = width / passwordLength / 2;
            for (int i = 0; i < textLength; i++) {
                cx = width * i / passwordLength + half;
                canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
            }
        } else {
            // 输入验证码
            //计算一个字符的宽度
            if (characterWidth == 0) {
                int w = getWidth() - getPaddingLeft() - getPaddingRight();
                characterWidth = (w - centerSpacing - 2 * characterSpacing) / passwordLength;
            }
            textPaint.setTextSize(textSize);
            textPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            if (textBaseY == 0)
                textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
            //写已输入的验证码
            if (getText().length() > 0) {
                textPaint.setColor(passwordColor);
                String verifyCodeStr = getText().toString();
                char[] chars = verifyCodeStr.toCharArray();
                int x, y = (int) textBaseY;
                for (int i = 0; i < chars.length; i++) {
                    //计算x,y
                    if (i <= 2) {
                        x = (characterWidth + characterSpacing) * i + characterWidth / 2;
                    } else {
                        x = (characterWidth + characterSpacing) * 2 + characterWidth + centerSpacing +
                                (characterWidth + characterSpacing) * (i - 3) + characterWidth / 2;
                    }
                    canvas.drawText(chars, i, 1, x, y, textPaint);
                }
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }
}
