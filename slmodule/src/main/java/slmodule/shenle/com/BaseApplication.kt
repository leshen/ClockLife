package slmodule.shenle.com

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.support.multidex.MultiDexApplication
import com.mob.MobSDK

import com.raizlabs.android.dbflow.config.FlowManager

import java.util.ArrayList
import java.util.LinkedList

import slmodule.shenle.com.utils.UIUtils
import zyt.xunfeilib.XunFeiHelper
import android.support.multidex.MultiDex
import cn.jpush.im.android.api.JMessageClient
import com.aitangba.swipeback.ActivityLifecycleHelper


open class BaseApplication : MultiDexApplication() {
    var isDayOrNight = false//是否是夜间模式
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        application = this
        mainThread = Thread.currentThread()
        mainThreadId = android.os.Process.myTid()
        mainThreadHandler = Handler()
        mainLoopler = mainLooper
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
        initDB()
        initTongJi()
        initLib()
        initXunFei()
        initMB()
    }

    /**
     * mob
     */
    private fun initMB() {
        MobSDK.init(this, UIUtils.getString(R.string.MobAppKey), UIUtils.getString(R.string.MobAppSecret))
    }

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
        JMessageClient.init(applicationContext,true)
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
