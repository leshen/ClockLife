package clocklife.shenle.com.base.receiver

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import clocklife.shenle.com.base.dao.ClockDao
import kotlinx.android.synthetic.main.layout_float_window.view.*
import slmodule.shenle.com.floatwindow.FloatWindowManager
import slmodule.shenle.com.utils.LogUtils
import slmodule.shenle.com.utils.UIUtils


/**
 * 闹钟定时
 * Created by shenle on 2017/9/5.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (ClockDao.ACTION_1 == intent.action) {
            LogUtils.d("AlarmReceiver ok")
            val uri = intent.data
            val content = uri.getQueryParameter("content")
            val title = uri.getQueryParameter("title")
            val type = uri.getQueryParameter("type")
            UIUtils.showToastSafe("闹钟广播接收 content=${content},type=${type},title=${title}")
            val smallWindow = FloatWindowManager.getInstance().getmSmallWindow()
            smallWindow.tv_title.text = title
            smallWindow.tv_content.text = content
            FloatWindowManager.getInstance().showFloatWindow(context)
            return
        }else{
            UIUtils.showToastSafe("闹钟广播${intent.data}")
        }
    }
}