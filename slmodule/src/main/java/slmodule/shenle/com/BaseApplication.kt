package slmodule.shenle.com

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import com.mob.MobSDK

import com.raizlabs.android.dbflow.config.FlowManager

import java.util.ArrayList
import java.util.LinkedList

import io.rong.imlib.RongIMClient
import slmodule.shenle.com.utils.UIUtils
import zyt.xunfeilib.XunFeiHelper

//点播不需要

open class BaseApplication : Application() {
    var isDayOrNight = false//是否是夜间模式

    override fun onCreate() {
        application = this
        mainThread = Thread.currentThread()
        mainThreadId = android.os.Process.myTid()
        mainThreadHandler = Handler()
        mainLoopler = mainLooper
        super.onCreate()
        initDB()
        initTongJi()
        initRongYun()
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
     * 融云lib
     */
    private fun initRongYun() {
        RongIMClient.init(this)
    }

    fun addActivity(activity: Activity) {
        mActivities.add(activity)
    }

    override fun onTerminate() {
        finishAll()
        onDestory()
        System.exit(0)
        super.onTerminate()
    }

    /**
     * 销毁的其他
     */
    private fun onDestory() {
        // 融云
        if (RongIMClient.getInstance() != null)
            RongIMClient.getInstance().disconnect()
        Process.killProcess(Process.myPid())
    }

    /** 推出应用  */
    fun exitApp() {
        finishAll()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun removeActivity(activity: Activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
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
        /** 记录所有活动的Activity  */
        private val mActivities = LinkedList<Activity>()

        val activities: List<Activity>
            get() = mActivities

        /** 记录处于前台的Activity  */
        /** 获取当前处于前台的activity  */
        /** 获取当前处于前台的activity  */
        var foregroundActivity: Activity? = null

        var cdeInitSuccess: Boolean = false

        fun killThisPackageIfRunning(context: Context,
                                     packageName: String) {
            val activityManager = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.killBackgroundProcesses(packageName)
        }

        /** 关闭所有Activity  */
        fun finishAll() {
            var copy: List<Activity>? = null
            synchronized(mActivities) {
                copy = ArrayList(mActivities)
            }
            for (activity in copy!!) {
                activity.finish()
                if (mActivities.contains(activity)) {
                    mActivities.remove(activity)
                }
            }
        }

        /** 关闭所有Activity，除了参数传递的Activity  */
        fun finishAll(except: BaseActivity) {
            var copy: List<Activity>? = null
            synchronized(mActivities) {
                copy = ArrayList(mActivities)
            }
            for (activity in copy!!) {
                if (activity !== except) {
                    activity.finish()
                }
            }
        }

        /** 是否有启动的Activity  */
        fun hasActivity(): Boolean {
            return mActivities.size > 0
        }

        /** 获取当前处于栈顶的activity，无论其是否处于前台  */
        val currentActivity: Activity?
            get() {
                var copy: List<Activity>? = null
                synchronized(mActivities) {
                    copy = ArrayList(mActivities)
                }
                if (copy!!.size > 0) {
                    return copy!![copy!!.size - 1]
                }
                return null
            }

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
