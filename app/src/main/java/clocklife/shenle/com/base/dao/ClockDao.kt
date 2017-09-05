package clocklife.shenle.com.base.dao

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Context.ALARM_SERVICE
import android.net.Uri
import clocklife.shenle.com.base.receiver.AlarmReceiver
import cn.jpush.im.android.api.model.Conversation


/**
 * Created by shenle on 2017/9/5.
 */
class ClockDao {
    companion object {
        val ACTION_1 = "android.alarm.clocklife.action"
        fun setAlarmTime(context: Context, triggerAtMillis: Long,content: String,type:String) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,AlarmReceiver::class.java)
            intent.setAction(ACTION_1);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            var uri = Uri
                    .parse("ClockLife://" + context.getApplicationInfo().packageName)
                    .buildUpon()
                    .appendPath("clockevent")
                    .appendQueryParameter(
                            "content",
                            content)
                    .appendQueryParameter(
                            "type",
                            type)
                    .build()
            intent.setData(uri)
            val sender = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            //闹铃间隔， 这里设为1分钟闹一次，在第2步我们将每隔1分钟收到一次广播
            //int interval = 60 * 1000;
            //am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
            am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender)
        }
        fun cancleAlarm(context :Context){
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.setAction("repeating")
            val sender = PendingIntent
                    .getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val alarm = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.cancel(sender)
        }
    }

}