package clocklife.shenle.com.base.dao

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import clocklife.shenle.com.R
import slmodule.shenle.com.utils.LogUtils

import java.util.HashSet

/*
 * @author : skywang <wangkuiwu@gmail.com>
 * description : 提供App Widget
 */

class MyAppWidgetProvider : AppWidgetProvider() {

    private val DEBUG = false
    // 更新 widget 的广播对应的action
    private val ACTION_UPDATE_ALL = "com.clocklife.widget.UPDATE_ALL"

    // onUpdate() 在更新 widget 时，被执行，
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        LogUtils.d( "onUpdate(): appWidgetIds.length=" + appWidgetIds.size)

        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (appWidgetId in appWidgetIds) {
            idsSet.add(appWidgetId)
        }
        prtSet()
    }

    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用 
    override fun onAppWidgetOptionsChanged(context: Context,
                                           appWidgetManager: AppWidgetManager, appWidgetId: Int,
                                           newOptions: Bundle) {
        LogUtils.d( "onAppWidgetOptionsChanged")
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions)
    }

    // widget被删除时调用  
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        LogUtils.d( "onDeleted(): appWidgetIds.length=" + appWidgetIds.size)

        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (appWidgetId in appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId))
        }
        prtSet()

        super.onDeleted(context, appWidgetIds)
    }

    // 第一个widget被创建时调用  
    override fun onEnabled(context: Context) {
        LogUtils.d( "onEnabled")
        // 在第一个 widget 被创建时，开启服务
        context.startService(EXAMPLE_SERVICE_INTENT)

        super.onEnabled(context)
    }

    // 最后一个widget被删除时调用  
    override fun onDisabled(context: Context) {
        LogUtils.d( "onDisabled")

        // 在最后一个 widget 被删除时，终止服务
        context.stopService(EXAMPLE_SERVICE_INTENT)

        super.onDisabled(context)
    }


    // 接收广播的回调函数
    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action
        LogUtils.d( "OnReceive:Action: " + action)
        if (ACTION_UPDATE_ALL == action) {
            // “更新”广播
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet)
        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            // “按钮点击”广播
            val data = intent.data
            val buttonId = Integer.parseInt(data.schemeSpecificPart)
            if (buttonId == BUTTON_SHOW) {
                LogUtils.d( "Button wifi clicked")
                Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
            }
        }

        super.onReceive(context, intent)
    }

    // 更新所有的 widget 
    private fun updateAllAppWidgets(context: Context, appWidgetManager: AppWidgetManager, set: Set<*>) {

        LogUtils.d( "updateAllAppWidgets(): size=" + set.size)

        // widget 的id
        var appID: Int
        // 迭代器，用于遍历所有保存的widget的id
        val it = set.iterator()

        while (it.hasNext()) {
            appID = (it.next() as Int).toInt()
            // 随机获取一张图片
            val index = java.util.Random().nextInt(ARR_IMAGES.size)

            if (DEBUG) LogUtils.d( "onUpdate(): index=" + index)
            // 获取 example_appwidget.xml 对应的RemoteViews            
            val remoteView = RemoteViews(context.packageName, R.layout.appwidget)

            // 设置显示图片
            remoteView.setImageViewResource(R.id.iv_show, ARR_IMAGES[index])

            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            remoteView.setOnClickPendingIntent(R.id.btn_show, getPendingIntent(context,
                    BUTTON_SHOW))

            // 更新 widget
            appWidgetManager.updateAppWidget(appID, remoteView)
        }
    }

    private fun getPendingIntent(context: Context, buttonId: Int): PendingIntent {
        val intent = Intent()
        intent.setClass(context, AppWidgetProvider::class.java)
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE)
        intent.data = Uri.parse("custom:" + buttonId)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    // 调试用：遍历set
    private fun prtSet() {
        if (DEBUG) {
            val index = 0
            val size = idsSet.size
            val it = idsSet.iterator()
            LogUtils.d( "total:" + size)
            while (it.hasNext()) {
                LogUtils.d( index.toString() + " -- " + (it.next() as Int).toInt())
            }
        }
    }

    companion object {
        private val TAG = "ExampleAppWidgetProvider"
        // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
        private val idsSet = HashSet<Int>()
        // 按钮信息
        private val BUTTON_SHOW = 1
        // 启动ExampleAppWidgetService服务对应的action
        val EXAMPLE_SERVICE_INTENT = Intent("android.appwidget.action.MY_APP_WIDGET_SERVICE")
        // TODO 图片数组
        private val ARR_IMAGES = intArrayOf(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher,R.mipmap.fatie, R.mipmap.login, R.mipmap.login_bg, R.mipmap.widget_preview)
    }
}