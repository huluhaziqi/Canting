package cn.caimatou.canting.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLPixelsUtil;
import cn.caimatou.canting.utils.GLStringUtil;

/**
 * Description：自定义的Dialog
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLCustomDialog extends Dialog {

    private GLCustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context mContext = null;
        private String mTitle = null;
        private CharSequence mMessage = null;
        private String mPositiveButtonText = null;
        private String mNegativeButtonText = null;
        private View mContentView = null;
        private View mOnlyContentView = null;
        private boolean bIsCancelable = true;
        private int mTheme = R.style.Dialog;

        protected OnClickListener mPositiveButtonClickListener = null, mNegativeButtonClickListener = null;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * Set the Dialog message from String
         */
        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         */
        public Builder setMessage(int message) {
            this.mMessage = mContext.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         */
        public Builder setTitle(int title) {
            this.mTitle = (String) mContext.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         */
        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         */
        public Builder setContentView(View v) {
            this.mContentView = v;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         */
        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         */
        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean isCancelable) {
            this.bIsCancelable = isCancelable;
            return this;
        }

        public Builder setTheme(int theme) {
            this.mTheme = theme;
            return this;
        }

        public GLCustomDialog createDialog() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final GLCustomDialog dialog = new GLCustomDialog(mContext, this.mTheme);

            if (null == this.mOnlyContentView) {
                View layout = inflater.inflate(R.layout.gl_custom_dialog_layout, null);
                dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                // set the dialog title
                TextView tvTitle = (TextView) layout.findViewById(R.id.title);
                TextView tvLineView = (TextView) layout.findViewById(R.id.title_line);
                if (GLStringUtil.isNotEmpty(mTitle)) {
                    tvTitle.setText(mTitle);
                } else {
                    tvTitle.setVisibility(View.GONE);
                    tvLineView.setVisibility(View.GONE);
                }

                // set the cancel button
                Button btnNegative = (Button) layout.findViewById(R.id.negativeButton);
                ImageView imageView = (ImageView) layout.findViewById(R.id.imdiv);
                if (GLStringUtil.isNotEmpty(mNegativeButtonText)) {
                    btnNegative.setText(mNegativeButtonText);

                    if (null != mNegativeButtonClickListener) {
                        btnNegative.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mNegativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            }

                        });
                    }
                } else {
                    // if no cancel button just set the visibility to GONE
                    btnNegative.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }

                // set the confirm button
                Button btnPositive = (Button) layout.findViewById(R.id.positiveButton);
                if (GLStringUtil.isNotEmpty(mPositiveButtonText)) {
                    btnPositive.setText(mPositiveButtonText);

                    if (null != mPositiveButtonClickListener) {
                        btnPositive.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mPositiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }

                        });
                    }
                } else {
                    // if no confirm button just set the visibility to GONE
                    btnPositive.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }

                // set the content message
                TextView tvMessage = (TextView) layout.findViewById(R.id.message);
                if (GLStringUtil.isNotEmpty(mMessage)) {
                    tvMessage.setText(mMessage);
                } else if (null != mContentView) {
                    // if no message set
                    LinearLayout content = (LinearLayout) layout.findViewById(R.id.content);
                    content.removeAllViews();
                    content.addView(mContentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                }

                Point point = GLPixelsUtil.getDeviceSize(mContext);
                int width = (int) (point.x * 0.95);//手机屏幕的宽度
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, WindowManager.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                dialog.setContentView(layout, params);
            } else {
                Point point = GLPixelsUtil.getDeviceSize(mContext);
                int width = (int) (point.x * 0.95);//手机屏幕的宽度
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, WindowManager.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                dialog.setContentView(this.mOnlyContentView, params);
            }
            dialog.setCancelable(bIsCancelable);

            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);

            return dialog;
        }
    }
}
