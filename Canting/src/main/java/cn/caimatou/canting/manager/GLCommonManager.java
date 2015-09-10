package cn.caimatou.canting.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.caimatou.canting.R;
import cn.caimatou.canting.common.GLViewManager;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/5/8.
 * <br/><br/>
 */
public class GLCommonManager {
    /**
     * 添加快捷方式
     *
     * @param context
     */
    public static void addShortcut(Context context, int text, int icon) {
        // add shortcut for the first time
        Intent shortcutIntent = new Intent();
        ComponentName componentName = new ComponentName(context, context.getClass());
        shortcutIntent.setComponent(componentName);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent addShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        addShortcut.putExtra("duplicate", false);
        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(text));
        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon));
        context.sendBroadcast(addShortcut);
    }

//    /**
//     * 关闭应用
//     *
//     * @param context
//     */
//    public static void finishApp(final Context context) {
//        DialogManager.onAffirm(context, R.string.cn_whether_exit,
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        finishAppNow();
//                    }
//                });
//    }

    public static void finishAppNow() {
        GLViewManager.getIns().popAllActivity();
    }

    @SuppressWarnings("deprecation")
    public static void killProcess(Context context) {
        if (Build.VERSION.SDK_INT <= 7) {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
        } else {
            // 停止跟踪
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }
    }

    public static void makeText(Context context, String text) {
        // 创建一个自定义的Toast提示消息
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.gl_toast, null, false);
        TextView tvMessage = (TextView) toastRoot.findViewById(R.id.tvMessage);
        tvMessage.setText(text);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        // 显示消息
        toast.show();
    }

    public static boolean isActivityRunning(String className) {
        return GLViewManager.getIns().isExist(className);
    }

    public static Activity getActivity(String className) {
        return GLViewManager.getIns().getActivity(className);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 打开系统网络设置
     *
     * @param context context
     */
    public static void startWirelessSetting(Context context) {
        if (null == context) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //3.0以上打开设置界面
            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }
}
