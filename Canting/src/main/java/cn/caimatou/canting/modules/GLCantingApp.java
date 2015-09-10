package cn.caimatou.canting.modules;

import android.app.Application;
import android.content.Intent;
import android.os.Process;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.caimatou.canting.common.GLViewManager;
import cn.caimatou.canting.services.GLGlobalService;
import cn.caimatou.canting.threadpool.GLThreadPool;


/**
 * Description：Application
 * <br/><br/>Created by Fausgoal on 15/8/26.
 * <br/><br/>
 */
public class GLCantingApp extends Application implements Thread.UncaughtExceptionHandler {

    private GLThreadPool mThreadPool = null;

    private static GLCantingApp ins = null;

    public static GLCantingApp getIns() {
        return ins;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;
        startGlobalService();
        initImageLoader();
    }

    private void startGlobalService() {
        Intent globalService = new Intent(this, GLGlobalService.class);
        globalService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(globalService);
    }

    /**
     * 初始化线程池对象
     *
     * @return mThreadPool
     */
    public synchronized GLThreadPool getThreadPool() {
        if (null == mThreadPool) {
            mThreadPool = new GLThreadPool();
        }
        return mThreadPool;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        // clear activity stacks
        GLViewManager.getIns().popAllActivity();
        // Notice 两个作用一样，只会执行一样
//        System.exit(GLConst.ONE);
        Process.killProcess(Process.myPid());
    }

    /**
     * 初始化 imageLoader
     */
    private static void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getIns())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
