package clocklife.shenle.com.base.receiver

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import clocklife.shenle.com.base.dao.MyAppWidgetProvider

/*
 * @author : skywang <wangkuiwu@gmail.com>
 * description : 周期性更新AppWidget的服务
 */

class MyAppWidgetService : Service() {

    // 更新 widget 的广播对应的action
    // 周期性更新 widget 的线程
    private var mUpdateThread: UpdateThread? = null
    private var mContext: Context? = null
    // 更新周期的计数
    private var count = 0

    override fun onCreate() {

        // 创建并开启线程UpdateThread
        mUpdateThread = UpdateThread()
        mUpdateThread!!.start()

        mContext = this.applicationContext

        super.onCreate()
    }

    override fun onDestroy() {
        // 中断线程，即结束线程。
        if (mUpdateThread != null) {
            mUpdateThread!!.interrupt()
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /*
     * 服务开始时，即调用startService()时，onStartCommand()被执行。
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)

        return Service.START_STICKY
    }

    private inner class UpdateThread : Thread() {

        override fun run() {
            super.run()

            try {
                count = 0
                while (true) {
                    Log.d(TAG, "run ... count:" + count)
                    count++

                    val updateIntent = Intent(MyAppWidgetProvider.EXAMPLE_SERVICE_INTENT)
                    mContext!!.sendBroadcast(updateIntent)

                    Thread.sleep(UPDATE_TIME.toLong())
                }
            } catch (e: InterruptedException) {
                // 将 InterruptedException 定义在while循环之外，意味着抛出 InterruptedException 异常时，终止线程。
                e.printStackTrace()
            }

        }
    }

    companion object {

        private val TAG = "MyAppWidgetService"
        // 周期性更新 widget 的周期
        private val UPDATE_TIME = 5000
    }
}