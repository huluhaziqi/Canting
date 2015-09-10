package cn.caimatou.canting.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLResourcesUtil;
import cn.caimatou.canting.widget.GLCustomDialog;

/**
 * Description：弹框管理类
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLDialogManager {
    private GLDialogManager() {
    }

    public static Dialog createDialog(final Context context, String title,
                                      CharSequence message, String positiveButtonText, String negativeButtonText, boolean isCancelable,
                                      DialogInterface.OnClickListener positiveButtonClickListener,
                                      DialogInterface.OnClickListener negativeButtonClickListener) {
        GLCustomDialog.Builder dialog = new GLCustomDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(isCancelable);
        // 确定按钮
        dialog.setPositiveButton(positiveButtonText, positiveButtonClickListener);
        // 取消按钮
        dialog.setNegativeButton(negativeButtonText, negativeButtonClickListener);
        return dialog.createDialog();
    }

    public static void showConfirmDialog(final Context context, String message) {
        Dialog dialog = createDialog(context, GLResourcesUtil.getString(R.string.dialog_title_tips),
                message, GLResourcesUtil.getString(R.string.confirm), null,
                false, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确定按钮
                        dialog.dismiss();
                    }
                }, null);
        dialog.show();
    }

    public static Dialog showTwoBtnDialog(final Context context, String title, String message,
                                          String positiveButtonText, String negativeButtonText,
                                          DialogInterface.OnClickListener positiveButtonClickListener,
                                          DialogInterface.OnClickListener negativeButtonClickListener) {
        Dialog dialog = createDialog(context, title, message, positiveButtonText, negativeButtonText, false, positiveButtonClickListener, negativeButtonClickListener);
        dialog.show();
        return dialog;
    }

    public static Dialog onAffirm(Context context, String message, String buttonText, DialogInterface.OnClickListener positiveButtonClickListener) {
        Dialog dialog = createDialog(context, GLResourcesUtil.getString(R.string.dialog_title_tips), message,
                buttonText, GLResourcesUtil.getString(R.string.cancel), false,
                positiveButtonClickListener, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
        return dialog;
    }

    public static View getDialogTitleView(Context context, String title) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gl_dialog_title, null);
        TextView textView = (TextView) view.findViewById(R.id.tvDialogTitle);
        textView.setText(title);
        return view;
    }
}
