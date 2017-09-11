package slmodule.shenle.com

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.util.Log
import cn.jpush.im.android.api.JMessageClient
import com.aitangba.swipeback.ActivityLifecycleHelper
import com.mob.MobSDK
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.RePluginCallbacks
import com.qihoo360.replugin.RePluginConfig
import com.qihoo360.replugin.RePluginEventCallbacks
import com.raizlabs.android.dbflow.config.FlowManager
import slmodule.shenle.com.utils.UIUtils
import zyt.xunfeilib.XunFeiHelper


open class BaseApplication : MultiDexApplication() {
    var isDayOrNight = false//是否是夜间模式
    private var isFirstLaunch: Boolean = true
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (isFirstLaunch) {
            // 首次启动
            Thread(Runnable {
                isFirstLaunch = false
                MultiDex.install(base)
            }).start()
        } else {
            // 非首次启动
            MultiDex.install(base)
        }
        RePlugin.App.attachBaseContext(this, createConfig())
    }

    /**
     * RePlugin允许提供各种“自定义”的行为，让您“无需修改源代码”，即可实现相应的功能
     */
    protected fun createConfig(): RePluginConfig {
        val c = RePluginConfig()

        // 允许“插件使用宿主类”。默认为“关闭”
        c.isUseHostClassIfNotFound = true

        // FIXME RePlugin默认会对安装的外置插件进行签名校验，这里先关掉，避免调试时出现签名错误
        c.verifySign = !BuildConfig.DEBUG

        // 针对“安装失败”等情况来做进一步的事件处理
        c.eventCallbacks = HostEventCallbacks(this)
        c.callbacks = HostCallbacks(this)

        // FIXME 若宿主为Release，则此处应加上您认为"合法"的插件的签名，例如，可以写上"宿主"自己的。
        // RePlugin.addCertSignature("AAAAAAAAA");

        return c
    }

    /**
     * 宿主针对RePlugin的自定义行为
     */
    private inner class HostCallbacks constructor(context: Context) : RePluginCallbacks(context) {

        override fun onPluginNotExistsForActivity(context: Context?, plugin: String?, intent: Intent?, process: Int): Boolean {
            // FIXME 当插件"没有安装"时触发此逻辑，可打开您的"下载对话框"并开始下载。
            // FIXME 其中"intent"需传递到"对话框"内，这样可在下载完成后，打开这个插件的Activity
            if (BuildConfig.DEBUG) {
                Log.d("sl", "onPluginNotExistsForActivity: Start download... p=$plugin; i=$intent")
            }
            return super.onPluginNotExistsForActivity(context, plugin, intent, process)
        }

    }

    private inner class HostEventCallbacks(context: Context) : RePluginEventCallbacks(context) {

        override fun onInstallPluginFailed(path: String?, code: InstallResult?) {
            // FIXME 当插件安装失败时触发此逻辑。您可以在此处做“打点统计”，也可以针对安装失败情况做“特殊处理”
            // 大部分可以通过RePlugin.install的返回值来判断是否成功
            if (BuildConfig.DEBUG) {
                Log.d("sl", "onInstallPluginFailed: Failed! path=$path; r=$code")
            }
            super.onInstallPluginFailed(path, code)
        }

        override fun onStartActivityCompleted(plugin: String?, activity: String?, result: Boolean) {
            // FIXME 当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
            super.onStartActivityCompleted(plugin, activity, result)
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
        RePlugin.App.onCreate();
        application = this
        mainThread = Thread.currentThread()
        mainThreadId = android.os.Process.myTid()
        mainThreadHandler = Handler()
        mainLoopler = mainLooper
        initDB()
        initTongJi()
        initLib()
        initXunFei()
        initMB()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onConfigurationChanged(newConfig);
    }

    /**
     * mob
     */
    private fun initMB() {
        try {
            MobSDK.init(this, UIUtils.getString(R.string.MobAppKey), UIUtils.getString(R.string.MobAppSecret))

        }catch (e:Exception){
            e.printStackTrace()
        }}

    /**
     * DBFlow
     */
    private fun initDB() {
        FlowManager.init(this)
    }

    /**
     * 讯飞统计
     */
    private fun initTongJi() {

    }

    /**
     * 讯飞语音
     */
    private fun initXunFei() {
        XunFeiHelper.init(this)
    }

    /**
     * 及时通讯lib
     */
    open fun initLib() {
        JMessageClient.init(applicationContext, true)
//        JMessageClient.setDebugMode(true)
    }


    override fun onTerminate() {
        onDestory()
        System.exit(0)
        super.onTerminate()
    }

    /**
     * 销毁的其他
     */
    private fun onDestory() {
        Process.killProcess(Process.myPid())
    }

    /** 推出应用  */
    fun exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }


    companion object {
        /** ，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了  */
        lateinit var application: BaseApplication
            private set
        /** 主线程ID  */
        /** 获取主线程ID  */
        var mainThreadId = -1
            private set
        /** 主线程  */
        /** 获取boolean主线程  */
        lateinit var mainThread: Thread
            private set
        /** 主线程Handler  */
        /** 获取主线程的handler  */
        lateinit var mainThreadHandler: Handler
            private set
        /** 主线程Looper  */
        /** 获取主线程的looper  */
        var mainLoopler: Looper? = null
            private set

        fun getInstance(): BaseApplication {
            return application
        }

        /**
         * 获得当前进程号

         * @param context
         * *
         * @return
         */
        fun getCurProcessName(context: Context): String? {
            val pid = android.os.Process.myPid()
            val activityManager = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in activityManager
                    .runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
            return null
        }
    }

}
